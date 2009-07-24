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

//package jsdai.tools;
package jsdai.express_g.util.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import jsdai.express_g.SdaieditPlugin;

import org.eclipse.ui.console.MessageConsoleStream;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.lksoft.util.ArgumentsParser;

public final class IPAdder {


	static boolean already_printed = false;
	
	private IPAdder() { }

	private static File[] parseArgs(String[] args) {
		ArgumentsParser.Value stepmodArg = new ArgumentsParser.Value("-stepmod", true);
		stepmodArg.setValueName("stepmodDir");
		stepmodArg.setDescription("Path to stepmod. Example: c:/workspace/stepmod");

		ArgumentsParser.Value schemasArg = new ArgumentsParser.Value("-schemas", true);
		schemasArg.setValueName("schemasFile");
		schemasArg.setDescription("File that contains list of schemas that need their XML to be regenerated.");

		ArgumentsParser.Value ipArg = new ArgumentsParser.Value("-ip", true);
		ipArg.setValueName("ipsFile");
		ipArg.setDescription("File that contains list of informal propositions.");

		List argList = Arrays.asList(new Object[] {stepmodArg, schemasArg, ipArg});
		if (!ArgumentsParser.parse(args, argList)) {
//RR			System.out.println(ArgumentsParser.getUsage(ExpressXmlGenerator.class.getName(), argList));
			return null;
		}

		File stepmodDir = new File(stepmodArg.getValue());
		if (!stepmodDir.exists() || !stepmodDir.isDirectory()) {
			System.out.println("Specified stepmod dir does not exists: " + stepmodDir);
			return null;
		}

		File schemasFile = new File(schemasArg.getValue());
		if (!schemasFile.exists() || !schemasFile.isFile()) {
			System.out.println("Specified schemas file does not exists, or is a directory: " + schemasFile);
			return null;
		}

		File ipFile = new File(ipArg.getValue());
		if (!ipFile.exists() || !ipFile.isFile()) {
			System.out.println("Specified informal propositions file does not exists, or is a directory: " + ipFile);
			return null;
		}

		return new File[] {stepmodDir, schemasFile, ipFile};
	}

	public static void main(String[] args) {

		File[] parsedArgs = parseArgs(args);
		if (parsedArgs == null) {
			return;
		}

		File stepmodDir = parsedArgs[0];
		File schemasFile = parsedArgs[1];
		File ipFile = parsedArgs[2];

		boolean success = false;

		try {
			System.out.println("Reading informal propositions: " + ipFile);
			Map ipMap = readIPs(ipFile);
			System.out.println("Reading schemas file: " + schemasFile);
			Set schemas = new HashSet(Utils.readListFile(schemasFile));
			System.out.println("Adding IPs...");
			success = addIPs(stepmodDir, schemas, ipMap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (success) {
			System.out.println("Success.");
		} else {
			System.out.println("Failed.");
		}
	}

	private static Map readIPs(File file)
		throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(file));

		Map ipMap = new HashMap();

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				// skip comments and empty lines
				line = line.trim();
				if (line.length() == 0 || line.startsWith(";")) {
					continue;
				}

				StringTokenizer st = new StringTokenizer(line, "=");
				String s1 = st.nextToken();
				String s2 = st.nextToken();

				st = new StringTokenizer(s1, ".");
				String moduleName = st.nextToken().toLowerCase();
				String entityName = st.nextToken().toLowerCase();

				Map moduleMap = (Map) ipMap.get(moduleName);
				if (moduleMap == null) {
					moduleMap = new HashMap();
					ipMap.put(moduleName, moduleMap);
				}

				SortedSet ips = (SortedSet) moduleMap.get(entityName);
				if (ips == null) {
					ips = new TreeSet();
					moduleMap.put(entityName, ips);
				}

				st = new StringTokenizer(s2, ",");
				while (st.hasMoreTokens()) {
					String ipName = st.nextToken();
					ips.add(ipName);
				}
			}
		} finally {
			reader.close();
		}

		return ipMap;
	}

	private static boolean addIPs(File stepmodDir, Set schemas, Map ipMap) {
		File modulesDir = new File(stepmodDir, "data/modules");
		if (!modulesDir.exists() || !modulesDir.isDirectory()) {
			System.out.println("Modules dir does not exist.");
			return false;
		}

		File resourcesDir = new File(stepmodDir, "data/resources");
		if (!resourcesDir.exists() || !resourcesDir.isDirectory()) {
			System.out.println("Resources dir does not exist.");
			return false;
		}

		for (Iterator i = schemas.iterator(); i.hasNext();) {
			String schemaName = (String) i.next();
			schemaName = schemaName.toLowerCase();
			Map moduleMap = (Map) ipMap.get(schemaName);
			if (moduleMap == null || moduleMap.size() == 0) {
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

			if (moduleDir.exists() && moduleDir.isDirectory()) {
				File path = new File(moduleDir, xmlFile);
				if (path.exists() && path.isFile()) {
					if (addIPs(path, moduleMap)) {
						System.out.println("IPs added to: " + path.getPath());
					}
				} else {
					System.out.println("File does not exists: " + path.getPath());
				}
			} else {
				System.out.println("Module does not exist: " + moduleDir.getPath());
			}
		}

		return true;
	}


  public static void addIPsForSchema(String schema_name, String xml_file, String informal_propositions, MessageConsoleStream stream) {

//System.out.println("schema name: " + schema_name);
//System.out.println("xml file: " + xml_file);
//System.out.println("informal propositions: " + informal_propositions);

  	schema_name = schema_name.toLowerCase();
  	File path = new File(xml_file);
		if (!path.exists() || !path.isFile()) {
			stream.println("Specified xml file does not exist: " + xml_file);
			return;
		}
		File ipFile = new File(informal_propositions);
		if (!ipFile.exists() || !ipFile.isFile()) {
			if (!already_printed) {
				stream.println("WARNING! informal_propositions.txt not found in the current project, the step is skipped");
				already_printed = true;
			}
			return;
		}
		Map ipMap;
		try {
			ipMap = readIPs(ipFile);
			Map moduleMap = (Map) ipMap.get(schema_name);
			if (moduleMap == null || moduleMap.size() == 0) {
				return;
			}		  
			boolean result =	addIPs(path, moduleMap);
		  if (!result) {
		  	stream.println("WARNING! failed to add IPs for schema: " + schema_name);
		  }
		} catch (IOException e) {
			stream.println("problems with IPAdder");
			SdaieditPlugin.log(e);
			e.printStackTrace();
		}
  }

	private static boolean addIPs(File xmlFile, Map moduleMap) {
		Document module;
		
		if (moduleMap == null) {
			return false;
		}
		
		try {
			module = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
		} catch (SAXParseException e) {
			e.printStackTrace();
			System.out.println("Unable to parse: " + xmlFile);
			return false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.out.println("Unable to parse: " + xmlFile);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to parse: " + xmlFile);
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			System.out.println("Unable to parse: " + xmlFile);
			return false;
		}

		Element elem = module.getDocumentElement();
		elem = Utils.getSubElement(elem, "schema");
		if (elem == null) {
			System.out.println("Unable to parse: " + xmlFile + "; because schema tag was not found.");
			return false;
		}

		boolean ipAdded = false;
		NodeList children = elem.getChildNodes();
		for (int i = 0, n = children.getLength(); i < n; i++) {
			Node child = children.item(i);
			if (!(child instanceof Element)) {
				continue;
			}

			elem = (Element) child;
			if (!elem.getTagName().equalsIgnoreCase("entity")) {
				continue;
			}

			String entityName = elem.getAttribute("name").toLowerCase();
			SortedSet ips = (SortedSet) moduleMap.get(entityName);
			if (ips != null) {
				addIPs(module, elem, ips);
				ipAdded = true;
			}
		}

		try {
			Utils.saveToFile(xmlFile.getPath(), module, Utils.EXPRESS_XML_DOCTYPE_VALUE);
		} catch (IOException e) {
			System.out.println("Unable to save modified file: " + xmlFile);
			ipAdded = false;
		} catch (TransformerException e) {
			System.out.println("Unable to save modified file: " + xmlFile);
			ipAdded = false;
		}

		return ipAdded;
	}

	private static void addIPs(Document doc, Element parent, SortedSet ips) {
		for (Iterator i = ips.iterator(); i.hasNext();) {
			String ipName = (String) i.next();
			Element wrElem = doc.createElement("where");
			parent.appendChild(wrElem);
			Attr atr = doc.createAttribute("label");
			atr.setValue(ipName);
			wrElem.setAttributeNode(atr);
		}
	}
}
