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
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lksoft.util.ArgumentsParser;

public final class IsoDbCreator {

	private IsoDbCreator() { }

	private static Object[] parseArgs(String[] args) {
		ArgumentsParser.Value stepmodArg = new ArgumentsParser.Value("-stepmod", true);
		stepmodArg.setValueName("stepmodDir");
		stepmodArg.setDescription("Path to stepmod. Example: c:/workspace/stepmod");

		ArgumentsParser.Value sufixArg = new ArgumentsParser.Value("-sufix", false);
		sufixArg.setValueName("sufix1[;sufix2[;...]]");
		sufixArg.setDescription("List of semicolumn seperated sufixes to be appended to module names.");

		List argList = Arrays.asList(new Object[] {stepmodArg, sufixArg});
		if (!ArgumentsParser.parse(args, argList)) {
			System.out.println(ArgumentsParser.getUsage(ModulesLister.class.getName(), argList));
			return null;
		}

		File stepmodDir = new File(stepmodArg.getValue());
		if (!stepmodDir.exists() || !stepmodDir.isDirectory()) {
			System.out.println("Specified stepmod dir does not exists: " + stepmodDir);
			return null;
		}

		List sufixes = new LinkedList();
		if (sufixArg.getIsSet()) {
			String s = sufixArg.getValue();
			StringTokenizer st = new StringTokenizer(s, ";");
			while (st.hasMoreTokens()) {
				sufixes.add(st.nextToken());
			}
		}

		if (sufixes.size() == 0) {
			sufixes.add("");
		}

		return new Object[] {stepmodDir, sufixes};
	}

	public static void main(String[] args) {
		Object[] parsedArgs = parseArgs(args);
		if (parsedArgs == null) {
			return;
		}

		File stepmod = (File) parsedArgs[0];
		List sufixes = (List) parsedArgs[1];

		File[] modules = new File(stepmod, "data/modules").listFiles();
		for (int i = 0; i < modules.length; i++) {
			File file = new File(modules[i], "module.xml");
			if (!file.exists()) {
				continue;
			}

			String[] attrs = getModuleAttrs(file);
			if (attrs != null) {
				for (Iterator j = sufixes.iterator(); j.hasNext();) {
					String sufix = (String) j.next();
					System.out.println(attrs[0] + sufix + "," + getIsoNr(attrs[1], attrs[2]));
				}
			}
		}
	}

	public static String getIsoNr(String status, String part) {
		return "ISO/" + status + " 10303-" + part;
	}

	public static String[] getModuleAttrs(File file) {
		Document module;
		try {
			module = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		} catch (Exception e) {
			System.err.println("Unable to parse: " + file);
			return null;
		}

		Element root = module.getDocumentElement();
		String name = root.getAttribute("name");
		if (name == null) {
			System.err.println("Unable to get name for: " + file);
			return null;
		}

		String part = root.getAttribute("part");
		if (part == null) {
			System.err.println("Unable to get part for: " + file);
			return null;
		}

		String status = root.getAttribute("status");
		if (status == null) {
			System.err.println("Unable to get status for: " + file);
			return null;
		}

		return new String[] {name, status, part};
	}
}
