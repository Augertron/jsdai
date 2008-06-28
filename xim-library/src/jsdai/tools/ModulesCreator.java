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
import java.util.Arrays;
import java.util.List;

import com.lksoft.util.ArgumentsParser;

public final class ModulesCreator {

	private ModulesCreator() { }

	public static final String SCRIPT_LOCATION = "utils";

	public static boolean createModule(File scriptDir, String moduleName) {
		Runtime runtime = Runtime.getRuntime();
		String s1 = "WScript.exe mkmodule_mod.wsf /mn:" + moduleName;
		Process process = null;
		try {
			process = runtime.exec(s1, null, scriptDir);
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException ioexception) {
			System.out.println("EXCEPTION: " + ioexception);
			System.out.println("Command line is incorrect as parser cannot be found\nat the given location. Exiting program");
			return false;
		}

		return true;
	}

	public static void createModuleArmDescriptions(File scriptDir, String moduleName) {
		String xmlFileName = "../data/modules/" + moduleName + "/arm.xml";

		Runtime runtime = Runtime.getRuntime();
		String s1 = "WScript.exe extractDescriptions_mod.wsf /fn:" + xmlFileName;
		System.out.println(s1);
		Process process = null;
		try {
			process = runtime.exec(s1, null, scriptDir);
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException ioexception) {
			System.out.println("EXCEPTION: " + ioexception);
			System.out.println("Command line is incorrect as parser cannot be found\nat the given location. Exiting program");
			System.exit(0);
		}
	}

	private static Object[] parseArgs(String[] args) {
		ArgumentsParser.Value stepmodArg = new ArgumentsParser.Value("-stepmod", true);
		stepmodArg.setValueName("stepmodDir");
		stepmodArg.setDescription("Path to stepmod. Example: c:/workspace/stepmod");

		ArgumentsParser.Value modulesArg = new ArgumentsParser.Value("-module", true);
		modulesArg.setValueName("module");
		modulesArg.setDescription("Name of new module to be created.");

		List argList = Arrays.asList(new Object[] {stepmodArg, modulesArg});
		if (!ArgumentsParser.parse(args, argList)) {
			System.out.println(ArgumentsParser.getUsage(ModulesCreator.class.getName(), argList));
			return null;
		}

		File stepmodDir = new File(stepmodArg.getValue());
		if (!stepmodDir.exists() || !stepmodDir.isDirectory()) {
			System.out.println("Specified stepmod dir does not exists: " + stepmodDir);
			return null;
		}

		String module = modulesArg.getValue();

		return new Object[] {stepmodDir, module};
	}

	/**
	 * Runs a custom script (located in etc/jsdai/tools/stepmod/utils) that
	 * creates a new specified module. The script must be copied to stepmod dir.
	 */
	public static void main(String[] args) {
		Object[] parsedArgs = parseArgs(args);
		if (parsedArgs == null) {
			return;
		}

		File stepmodDir = (File) parsedArgs[0];
		String module = (String) parsedArgs[1];

		File scriptLoc = new File(stepmodDir, SCRIPT_LOCATION);
		if (createModule(scriptLoc, module)) {
			createModuleArmDescriptions(scriptLoc, module);
		}
	}
}
