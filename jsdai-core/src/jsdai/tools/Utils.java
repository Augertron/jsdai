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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jsdai.lang.ASdaiRepository;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;

final class Utils {

	public static final String EXPRESS_XML_DOCTYPE_VALUE = "../../../dtd/express.dtd";

	private Utils() { }

	public static SdaiRepository getRepo(String name)
		throws SdaiException {

		SdaiSession session = SdaiSession.openSession();
		session.setSdaiContext(new SdaiContext());
		session.startTransactionReadWriteAccess();
		ASdaiRepository arep = session.getKnownServers();

		SdaiRepository repository = null;
		for (SdaiIterator si = arep.createIterator(); si.next();) {
			SdaiRepository tempRepository = arep.getCurrentMember(si);
			if (tempRepository.getName().equals(name)) {
				repository = tempRepository;
				break;
			}
		}

		if (repository == null) {
			System.out.println("Repository " + name + " not found. Unable to proceed.");
			return null;
		}
		if(!repository.isActive())
			repository.openRepository();

		return repository;
	}

	public static List readListFile(File schemasFile)
		throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(schemasFile));
		List schemas = new LinkedList();
		try {
			String s;
			while ((s = reader.readLine()) != null) {
				schemas.add(s);
			}
		} finally {
			reader.close();
		}

		return schemas;
	}

	public static String read(File file)
		throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(baos);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String s;
		while ((s = reader.readLine()) != null) {
			writer.println(s);
		}

		reader.close();
		writer.close();

		return baos.toString();
	}

	public static Element getSubElement(Element parent, String tagName) {
		NodeList children = parent.getChildNodes();
		for (int i = 0, n = children.getLength(); i < n; i++) {
			Node child = children.item(i);
			if (!(child instanceof Element)) {
				continue;
			}

			Element elem = (Element) child;
			if (elem.getTagName().equalsIgnoreCase(tagName)) {
				return elem;
			}
		}

		return null;
	}

	public static void saveToFile(String fileName, Document what, String dtd)
		throws IOException, TransformerException {

		TransformerFactory fact = TransformerFactory.newInstance();
		Transformer t = fact.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		if(dtd != null){
			t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
		}
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
		t.transform(new DOMSource(what), new StreamResult(new File(fileName)));
	}

	public static Map readMapFile(File file)
		throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(file));

		Map res = new HashMap();
		try {
			String s;
			while ((s = reader.readLine()) != null) {
				// skip comments
				if (!s.startsWith("#")) {
					StringTokenizer st = new StringTokenizer(s, ",");
					if (st.countTokens() == 2) {
						String key = st.nextToken();
						String value = st.nextToken();

						if (res.containsKey(key)) {
							System.err.println("Dublicated key: " + key);
						}

						res.put(key, value);
					} else {
						System.err.println("Invalid line format: " + s);
					}
				}
			}
		} finally {
			reader.close();
		}

		return res;
	}

	public static Set toLowerCase(Set set) {
		Set newSet = new HashSet();
		for (Iterator i = set.iterator(); i.hasNext();) {
			String s = (String) i.next();
			newSet.add(s.toLowerCase());
		}

		return newSet;
	}

	public static Properties loadProperties(String file)
		throws IOException {

		Properties props = new Properties();
		FileInputStream input = new FileInputStream(file);
		props.load(input);
		input.close();

		return props;
	}
}
