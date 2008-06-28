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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.lksoft.util.ArgumentsParser;

public final class ModuleScopeRegenerator {

	private static final String SCOPE_PREFIX = "items within the scope of application module ";

	private ModuleScopeRegenerator() { }

	private static File[] parseArgs(String[] args) {
		ArgumentsParser.Value stepmodArg = new ArgumentsParser.Value("-stepmod", true);
		stepmodArg.setValueName("stepmodDir");
		stepmodArg.setDescription("Path to stepmod. Example: c:/workspace/stepmod");

		ArgumentsParser.Value modulesArg = new ArgumentsParser.Value("-modules", true);
		modulesArg.setValueName("modulesFile");
		modulesArg.setDescription("File that contains list of modules that need their scopes to be regenerated.");

		List argList = Arrays.asList(new Object[] {stepmodArg, modulesArg});
		if (!ArgumentsParser.parse(args, argList)) {
			System.out.println(ArgumentsParser.getUsage(ExpressXmlGenerator.class.getName(), argList));
			return null;
		}

		File stepmodDir = new File(stepmodArg.getValue());
		if (!stepmodDir.exists() || !stepmodDir.isDirectory()) {
			System.out.println("Specified stepmod dir does not exists: " + stepmodDir);
			return null;
		}

		File schemasFile = new File(modulesArg.getValue());
		if (!schemasFile.exists() || !schemasFile.isFile()) {
			System.out.println("Specified modules file does not exists, or is a directory: " + schemasFile);
			return null;
		}

		return new File[] {stepmodDir, schemasFile};
	}

	public static void main(String[] args)
		throws IOException {

		File[] parsedArgs = parseArgs(args);
		if (parsedArgs == null) {
			return;
		}

		File stepmodDir = parsedArgs[0];
		File modulesFile = parsedArgs[1];

		System.out.println("Reading modules file: " + modulesFile);
		Set modules = new HashSet(Utils.readListFile(modulesFile));

		System.out.println("Regenerating scopes...");
		regenerateScope(stepmodDir, modules);

		System.out.println("Done.");
	}

	private static void regenerateScope(File stepmodDir, Set modules) {
		File modulesDir = new File(stepmodDir, "data/modules");

		for (Iterator i = modules.iterator(); i.hasNext();) {
			String module = (String) i.next();
			File moduleDir = new File(modulesDir, module);
			if (moduleDir.exists() && moduleDir.isDirectory()) {
				regenerateScope(moduleDir);
			} else {
				System.err.println("Module directory does not exist: " + moduleDir);
			}
		}
	}

	private static String getIsoNr(File modulesDir, String armSchema) {
		String module = armSchema.substring(0, armSchema.indexOf("_arm")).toLowerCase();
		File moduleDir = new File(modulesDir, module);
		if (!moduleDir.exists()) {
			return null;
		}

		String[] attrs = IsoDbCreator.getModuleAttrs(new File(moduleDir, "module.xml"));
		if (attrs == null) {
			return null;
		}

		return IsoDbCreator.getIsoNr(attrs[1], attrs[2]);
	}

	private static void regenerateScope(File moduleDir) {
		List useFroms = new ArrayList(gatherUseFroms(moduleDir, "arm.xml"));
		Collections.sort(useFroms);

		File file = new File(moduleDir, "module.xml");
		if (!file.exists()) {
			System.err.println("Module file does not exist: " + file);
			return;
		}

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setValidating(false);
			docFactory.setCoalescing(true);

			DocumentBuilder builder = docFactory.newDocumentBuilder();
			Document loadedDoc = builder.parse(file);

			NodeList nodeList = loadedDoc.getElementsByTagName("inscope");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				deleteScope(node);

				// after the old inscope statements are deleted, we form new ones
				for (Iterator j = useFroms.iterator(); j.hasNext();) {
					String schemaNodeValue = (String) j.next();
					String isoNumber = getIsoNr(moduleDir.getParentFile(), schemaNodeValue);
					if (isoNumber == null) {
						isoNumber = "ISO/CD-TS 10303-xxxx";
					}

					Element liNode = loadedDoc.createElement("li");

					// module_ref tag
					Element modRefNode = loadedDoc.createElement("module_ref");

					// linkend
					int li = schemaNodeValue.lastIndexOf("_arm");
					String linkendName = schemaNodeValue.substring(0, li);
					linkendName = linkendName.toLowerCase();
					String formedString = linkendName + ":1_scope";
					modRefNode.setAttribute("linkend", formedString);

					// separate module name
					int li2 = schemaNodeValue.lastIndexOf("_arm");
					String moduleName = schemaNodeValue.substring(0, li2);
					moduleName = moduleName.replace('_', ' ');
					Text linkendTextNode = loadedDoc.createTextNode(moduleName);
					modRefNode.appendChild(linkendTextNode);

					// Text1
					Text textNode1 = loadedDoc.createTextNode(SCOPE_PREFIX);

					// Text1
					String formedString2 = ", " + isoNumber;
					if (!j.hasNext()) {
						formedString2 += ".";
					} else {
						formedString2 += ";";
					}

					Text textNode2 = loadedDoc.createTextNode(formedString2);

					// add all nodes
					liNode.appendChild(textNode1);
					liNode.appendChild(modRefNode);
					liNode.appendChild(textNode2);

					// add li node to the inscope
					node.appendChild(liNode);
				}
			}

			DocumentType docType = loadedDoc.getDoctype();
			if (docType != null) {
				Utils.saveToFile(file.getCanonicalPath(), loadedDoc, docType.getSystemId());
				System.out.println("Regenerated: " + file);
			} else {
				System.out.println("Failed to regenerate: " + file);
			}
		} catch (TransformerConfigurationException e) {
			System.out.println("PROBLEM: " + e.getMessage());
			e.printStackTrace();
		} catch (TransformerException e) {
			System.out.println("PROBLEM: " + e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			System.out.println("PROBLEM: " + e.getMessage());
			e.printStackTrace();
		} catch (SAXException sxe) {
			Exception x = sxe;
			if (sxe.getException() != null) {
				x = sxe.getException();
			}
			x.printStackTrace();
		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		}
	}

	private static void deleteScope(Node node) {
		List remove = new LinkedList();

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (!(child instanceof Element)) {
				continue;
			}

			Element elem = (Element) child;
			if (!elem.getTagName().equalsIgnoreCase("li")) {
				continue;
			}

			NodeList imgNodes = elem.getChildNodes();
			for (int j = 0; j < imgNodes.getLength(); j++) {
				Node imgNode = imgNodes.item(j);
				String nodeValue = imgNode.getNodeValue();
				if (nodeValue != null && nodeValue.startsWith(SCOPE_PREFIX)) {
					remove.add(child);
					break;
				}
			}
		}

		for (Iterator i = remove.iterator(); i.hasNext();) {
			Node child = (Node) i.next();
			node.removeChild(child);
		}

		node.normalize();

		childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child instanceof Text) {
				node.removeChild(child);
			}
		}
	}

	private static Collection gatherUseFroms(File modulesDir, String xmlFile) {
		File file = new File(modulesDir, xmlFile);
		if (!file.exists()) {
			System.err.println("File not exists: " + file);
			return null;
		}

		Collection usefroms = new LinkedList();

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setValidating(false);
			docFactory.setCoalescing(true);

			Document doc = docFactory.newDocumentBuilder().parse(file);

			NodeList nodeList = doc.getElementsByTagName("interface");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element elem = (Element) nodeList.item(i);
				String kindNodeValue  = elem.getAttribute("kind");
				if (kindNodeValue.equalsIgnoreCase("use")) {
					String schemaNodeValue  = elem.getAttribute("schema");
					usefroms.add(schemaNodeValue);
				}
			}
		} catch (ParserConfigurationException e) {
			System.out.println("PROBLEM: " + e.getMessage());
			e.printStackTrace();
		} catch (SAXException sxe) {
			Exception x = sxe;
			if (sxe.getException() != null) {
				x = sxe.getException();
			}

			x.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return usefroms;
	}
}
