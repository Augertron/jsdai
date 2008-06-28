/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

package jsdai.tools;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.lksoft.util.ArgumentsParser;

import jsdai.lang.*;

public final class ExpressXmlGenerator {

	private static final String REPOSITORY_NAME = "ExpressCompilerRepo";

	private ExpressXmlGenerator() { }

	private static File[] parseArgs(String[] args) {
		ArgumentsParser.Value stepmodArg = new ArgumentsParser.Value("-stepmod", true);
		stepmodArg.setValueName("stepmodDir");
		stepmodArg.setDescription("Path to stepmod. Example: c:/workspace/stepmod");

		ArgumentsParser.Value schemasArg = new ArgumentsParser.Value("-schemas", true);
		schemasArg.setValueName("schemasFile");
		schemasArg.setDescription("File that contains list of schemas that need their XML to be regenerated.");

		List argList = Arrays.asList(new Object[] {stepmodArg, schemasArg});
		if (!ArgumentsParser.parse(args, argList)) {
			System.out.println(ArgumentsParser.getUsage(ExpressXmlGenerator.class.getName(), argList));
			return null;
		}

		File schemasFile = new File(schemasArg.getValue());
		if (!schemasFile.exists() || !schemasFile.isFile()) {
			System.out.println("Specified schemas file does not exists, or is a directory: " + schemasFile);
			return null;
		}

		File stepmodDir = new File(stepmodArg.getValue());
		if (!stepmodDir.exists() || !stepmodDir.isDirectory()) {
			System.out.println("Specified stepmod dir does not exists. Creating: " + stepmodDir);
			if (!stepmodDir.mkdirs()) {
				System.out.println("Unable to create dir: " + stepmodDir);
				return null;
			}
		}

		return new File[] {stepmodDir, schemasFile};
	}

	public static void main(String[] args)
		throws SdaiException {

		File[] parsedArgs = parseArgs(args);
		if (parsedArgs == null) {
			return;
		}

		File stepmodDir = parsedArgs[0];
		File schemasFile = parsedArgs[1];

		boolean success = false;

		try {
			System.out.println("Opening repo: " + REPOSITORY_NAME);
			SdaiRepository repo = Utils.getRepo(REPOSITORY_NAME);
			if (repo != null) {
				System.out.println("Reading schemas file: " + schemasFile);
				Set schemas = Utils.toLowerCase(new HashSet(Utils.readListFile(schemasFile)));
				System.out.println("Converting schemas...");
				try {
					success = convertSchemas(stepmodDir, schemas, repo);
				} catch (SdaiException e) {
					e.printStackTrace();
				}
			}
		} catch (SdaiException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			SdaiSession session = SdaiSession.getSession();
			if (session != null) {
				session.closeSession();
			}
		}

		if (success) {
			System.out.println("Success.");
		} else {
			System.out.println("Failed.");
		}
	}

	private static boolean convertSchemas(File stepmodDir, Set schemaNames, SdaiRepository repo)
		throws SdaiException {

		File modulesDir = new File(stepmodDir, "data/modules");
		if (!modulesDir.exists() || !modulesDir.isDirectory()) {
			System.out.println("Modules dir does not exist. Creating: " + modulesDir);
			if (!modulesDir.mkdirs()) {
				System.out.println("Unable to create dir: " + modulesDir);
				return false;
			}
		}

		File resourcesDir = new File(stepmodDir, "data/resources");
		if (!resourcesDir.exists() || !resourcesDir.isDirectory()) {
			System.out.println("Resources dir does not exist. Creating: " + resourcesDir);
			if (!resourcesDir.mkdirs()) {
				System.out.println("Unable to create dir: " + resourcesDir);
				return false;
			}
		}

		ASchemaInstance schemas = repo.getSchemas();

		for (SdaiIterator it1 = schemas.createIterator(); it1.next();) {
			SchemaInstance schema = schemas.getCurrentMember(it1);
			String schemaName = schema.getName();
			if (!schemaNames.contains(schemaName.toLowerCase())) {
				continue;
			}

			File moduleDir;
			String xmlFile;
			if (schemaName.matches("\\w+(?>_arm)|(?>_ARM)")) {
				moduleDir = new File(modulesDir, schemaName.substring(0, schemaName.length() - 4));
				xmlFile = "arm.xml";
			} else if (schemaName.matches("\\w+(?>_mim)|(?>_MIM)")) {
				moduleDir = new File(modulesDir, schemaName.substring(0, schemaName.length() - 4));
				xmlFile = "mim.xml";
			} else {
				moduleDir = new File(resourcesDir, schemaName);
				xmlFile = schemaName + ".xml";
			}

			if (!moduleDir.exists() || !moduleDir.isDirectory()) {
				if (!moduleDir.mkdirs()) {
					System.out.println("Module does not exist: " + moduleDir.getPath());
					continue;
				}
			}

			File path = new File(moduleDir, xmlFile);
			ExpressXmlConverter.convertSchemaInstance(schema, path.getPath());
			System.out.println("Created: " + path.getPath());

			schemaNames.remove(schemaName);
		}

		for (Iterator i = schemaNames.iterator(); i.hasNext();) {
			String schemaName = (String) i.next();
			System.out.println("Schema " + schemaName + " was not found in the repository.");
		}

		return true;
	}
}
