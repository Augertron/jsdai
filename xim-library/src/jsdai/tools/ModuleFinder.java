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

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * @author Viktoras Kovaliovas
 */
public final class ModuleFinder {

	private ModuleFinder() { }

	public static final Pattern PAT_BLOCK_COMMENTS = Pattern.compile("(?>(?>[*][)])|\\A)(.*?)(?>(?>[(][*])|\\z)", Pattern.DOTALL);
	public static final Pattern PAT_USEFROM = Pattern.compile("^(.*?)USE\\s+FROM\\s+(\\w+)", Pattern.MULTILINE);
	public static final Pattern PAT_REFERENCEFROM = Pattern.compile("^(.*?)REFERENCE\\s+FROM\\s+(\\w+)", Pattern.MULTILINE);

	public static final String PROP_STEPMOD_DIR = "stepmod";
	public static final String PROP_FIXED_DIR = "fixed";
	public static final String TYPE = "type";
	public static final String PROP_ROOT_MODULES = "roots";

	private static void getUsefroms(Matcher m, Set usefroms) {
		while (m.find()) {
			String prefix = m.group(1);
			if (prefix.indexOf("--") >= 0) {
				continue;
			}

			String schema = m.group(2).toLowerCase();
			String module;
			if (schema.endsWith("_schema") || schema.startsWith("aic_")) {
				module = schema;
			} else {
				module = schema.substring(0, schema.lastIndexOf('_'));
			}

			usefroms.add(module);
		}
	}

	public static Set[] getUsefromsEx(File file) {
		// read express
		String express;
		try {
			express = Utils.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		// get refered modules
		Set usefroms = new HashSet();
		Set reffroms = new HashSet();

		Matcher blocks = PAT_BLOCK_COMMENTS.matcher(express);
		while (blocks.find()) {
			String uncommnented = blocks.group(1);
			if (uncommnented == null) {
				continue;
			}

			Matcher m = PAT_USEFROM.matcher(uncommnented);
			getUsefroms(m, usefroms);

			m = PAT_REFERENCEFROM.matcher(uncommnented);
			getUsefroms(m, reffroms);
		}

		return new Set[] {usefroms, reffroms};
	}

	public static Set getUsefroms(File file) {
		Set[] usefroms = getUsefromsEx(file);
		if (usefroms == null) {
			return null;
		}

		usefroms[0].addAll(usefroms[1]);
		return usefroms[0];
	}

	public static SortedSet getExpress(Properties props) {
		Set uncheckedModules = new HashSet();
		Set checkedModules = new HashSet();
		SortedSet expressFiles = new TreeSet();

		// get root modules
		Set rootModules = new HashSet();

		String s = props.getProperty(PROP_ROOT_MODULES);
		StringTokenizer st = new StringTokenizer(s, ",");
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			rootModules.add(s);
		}

		uncheckedModules.addAll(rootModules);

		// get paths
		String stepmod = props.getProperty(PROP_STEPMOD_DIR);
		if (!new File(stepmod).exists()) {
			System.err.println("Stepmod dir does not exist: " + stepmod);
			return expressFiles;
		}

		String fixedmodules = props.getProperty(PROP_FIXED_DIR) + "/";
		if (!new File(stepmod).exists()) {
			System.err.println("Stepmod dir does not exist: " + stepmod);
			return expressFiles;
		}

		String modulesDir = stepmod + "/data/modules/";
		if (!new File(modulesDir).exists()) {
			System.err.println("Modules dir does not exist: " + modulesDir);
			return expressFiles;
		}

		String irDir = stepmod + "/data/resources/";
		if (!new File(irDir).exists()) {
			System.err.println("Resources dir does not exist: " + irDir);
			return expressFiles;
		}

		String type = props.getProperty(TYPE);

		if (!type.equals("arm") && !type.equals("mim") && !type.equals("xim")) {

			System.err.println("Unresolved module type: " + type);
			return expressFiles;
		}

//		Set printed = new HashSet();

		// get all modules used directly, or indirectly by root modules
		while (uncheckedModules.size() > 0) {
			String module = (String) uncheckedModules.iterator().next();
			uncheckedModules.remove(module);

			// identify express file
			boolean isRes;
			String fileName;
			if (module.endsWith("_schema") || module.startsWith("aic_")) {
				fileName = module + "/" + module + ".exp";
				isRes = true;
				// file = new File(irDir + module + "/" + module + ".exp");
			} else {
				fileName = module + "/" + type + ".exp";
				isRes = false;
				// file = new File(modulesDir + module + "/" + type + ".exp");
			}

			File file = new File(fixedmodules + fileName);
			if (!file.exists()) {
				if (isRes) {
					file = new File(irDir + fileName);
				} else {
					file = new File(modulesDir + fileName);
				}
			} else {
				System.out.println("Taken from fixed dir: " + fileName);
			}

			if (!file.exists()) {
				System.err.println("Unable to find required file: " + file);
				continue;
			}

			// Set[] usefromsEx = getUsefromsEx(file);
			// if (usefromsEx == null)
			// continue;
			//
			// for (Iterator i = usefromsEx[0].iterator(); i.hasNext();) {
			// String mod = (String) i.next();
			// if (!printed.contains(mod))
			// print("USE FROM", mod, type);
			// }
			//
			// printed.addAll(usefromsEx[0]);
			//
			// for (Iterator i = usefromsEx[1].iterator(); i.hasNext();) {
			// String mod = (String) i.next();
			// if (!printed.contains(mod))
			// print("REFERENCE FROM", mod, type);
			// }
			//
			// printed.addAll(usefromsEx[1]);
			//
			// Set usefroms = new HashSet();
			// usefroms.addAll(usefromsEx[0]);
			// usefroms.addAll(usefromsEx[1]);

			Set usefroms = getUsefroms(file);
			if (usefroms == null) {
				continue;
			}

			// add module to modules set
			checkedModules.add(module);
			expressFiles.add(file.getAbsolutePath());

			usefroms.removeAll(checkedModules);
			uncheckedModules.addAll(usefroms);
		}

		return expressFiles;
	}

//	private static void print(String prefix, String module, String type) {
//		if (module.endsWith("_schema") || module.startsWith("aic_"))
//			System.out.println(prefix + " " + module + ";");
//		else
//			System.out.println(prefix + " " + module + "_" + type + ";");
//	}

	/**
	 * Prints all the modules that are reachable by specified root modules.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Only the properties file should be specified.");
			return;
		}

		Properties props;
		try {
			props = Utils.loadProperties(args[0]);
		} catch (IOException e) {
			System.err.println("Fatal error has occurred: " + e);
			return;
		}

		// print modules
		SortedSet modules = getExpress(props);
		for (Iterator i = modules.iterator(); i.hasNext();) {
			String module = (String) i.next();
			System.out.println(module);
		}
	}
}
