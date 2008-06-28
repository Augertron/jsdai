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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lksoft.util.ArgumentsParser;

public final class ModulesLister {

	private ModulesLister() { }

	public static Set getModuleNames(File repoIndexFile, String theProject) {
		if (!repoIndexFile.exists()) {
			System.err.println("File not found: " + repoIndexFile);
			return Collections.EMPTY_SET;
		}

		Document repoIndex;
		try {
			repoIndex = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(repoIndexFile);
		} catch (Exception e) {
			System.err.println("Unable to parse: " + repoIndexFile);
			return Collections.EMPTY_SET;
		}

		Element root = repoIndex.getDocumentElement();
		Element modules = Utils.getSubElement(root, "modules");
		if (modules == null) {
			System.err.println("Unable to find modules in: " + repoIndexFile);
			return Collections.EMPTY_SET;
		}

		Set result = new HashSet();

		NodeList children = modules.getChildNodes();
		for (int i = 0, n = children.getLength(); i < n; i++) {
			Node child = children.item(i);
			if (!(child instanceof Element)) {
				continue;
			}

			Element elem = (Element) child;
			if (elem.getTagName().equalsIgnoreCase("module")) {
				String project = elem.getAttribute("project");
				if (project.equalsIgnoreCase(theProject)) {
					result.add(elem.getAttribute("name").toLowerCase());
				}
			}
		}

		return result;
	}

	private static Object[] parseArgs(String[] args) {
		ArgumentsParser.Value repArg = new ArgumentsParser.Value("-index", true);
		repArg.setValueName("repositoryIndex");
		repArg.setDescription("Path to repository index. Example: c:/workspace/stepmod/repository_index.xml");

		ArgumentsParser.Value projectArg = new ArgumentsParser.Value("-project", true);
		projectArg.setValueName("project");
		projectArg.setDescription("Name of project that owns modules to be included.");

		ArgumentsParser.Value sufixArg = new ArgumentsParser.Value("-sufix", false);
		sufixArg.setValueName("sufix1[;sufix2[;...]]");
		sufixArg.setDescription("List of semicolumn seperated sufixes to be appended to module names.");

		List argList = Arrays.asList(new Object[] {repArg, projectArg, sufixArg});
		if (!ArgumentsParser.parse(args, argList)) {
			System.out.println(ArgumentsParser.getUsage(ModulesLister.class.getName(), argList));
			return null;
		}

		File repFile = new File(repArg.getValue());
		if (!repFile.exists() || !repFile.isFile()) {
			System.out.println("Specified repository index does not exists: " + repFile);
			return null;
		}

		String project = projectArg.getValue();

		List sufixes = new LinkedList();
		if (sufixArg.getIsSet()) {
			String s = sufixArg.getValue();
			StringTokenizer st = new StringTokenizer(s, ";");
			while (st.hasMoreTokens()) {
				sufixes.add(st.nextToken());
			}
		}

		return new Object[] {repFile, project, sufixes};
	}

	public static void main(String[] args) {
		Object[] parsedArgs = parseArgs(args);
		if (parsedArgs == null) {
			return;
		}

		File repIndex = (File) parsedArgs[0];
		String project = (String) parsedArgs[1];
		List sufixes = (List) parsedArgs[2];
		boolean appendSufixes = sufixes.size() > 0;

		Set modules = new TreeSet(getModuleNames(repIndex, project));
		for (Iterator i = modules.iterator(); i.hasNext();) {
			String module = (String) i.next();
			if (appendSufixes) {
				for (Iterator j = sufixes.iterator(); j.hasNext();) {
					String sufix = (String) j.next();
					System.out.println(module + sufix);
				}
			} else {
				System.out.println(module);
			}
		}
	}
}
