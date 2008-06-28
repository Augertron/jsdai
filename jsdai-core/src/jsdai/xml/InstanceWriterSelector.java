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

package jsdai.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import jsdai.lang.JsdaiLangAccessor;
import jsdai.lang.SdaiRepository;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * This class is a way to write (create) JSDAI population
 * from XML parsing events. It acts as XML contents handler and
 * can be used in any environment which generates XML parsing events
 * (eg. XML text file parsing). 
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class InstanceWriterSelector extends JsdaiLangAccessor implements ContentHandler {

	private InstanceWriter[] instanceWriters;
	private InstanceWriter currentWriter;
	private Map rootElementPrefixes;

	/**
	 * Creates a new instance writer selector for creating population in
	 * specified repository with default instance writers.
	 *
	 * @param repository The repository
	 */
	public InstanceWriterSelector(SdaiRepository repository) {
		this.instanceWriters = new InstanceWriter[] 
			{ new LateBindingWriter(repository), new EarlyBindingV2Writer(repository) };
	}


	/**
	 * Creates a new instance writer selector with specified instance writers.
	 *
	 * @param instanceWriters array of specific instance writers
	 */
	public InstanceWriterSelector(InstanceWriter[] instanceWriters) {
		this.instanceWriters = instanceWriters;
	}

	/**
	 * Reads and parses XML text representation from the input stream.
	 * This is a convenient method perform parsing in one call. It
	 * It creates a new XML reader, enables the namespace support
	 * and invokes the parsing.
	 *
	 * @param location The input source which defines the location
	 * @param handler The content handler, eg. JSDAI instance reader
	 * @exception SAXException if SAX error occurs
	 * @exception IOException if I/O error occurs
	 * @exception ParserConfigurationException if parser configuration error occurs
	 */
	static public void parse(InputStream location, ContentHandler handler)
	throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
		xmlReader.setContentHandler(handler);
		xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
		xmlReader.parse(new InputSource(location));
	}

	// Implementation of org.xml.sax.ContentHandler

	/**
	 * Accepts notification about element start events.
	 *
	 * @param namespaceURI The namespace URI. Namespace processing has to be
	 *                     enabled in XML reader
	 * @param localName The local name (without prefix) 
	 * @param qName The ignored qualified name (can be null)
	 * @param attr The attributes attached to the element
	 * @exception SAXException if SAX error occurs
	 */
	public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
	throws SAXException {
		if(currentWriter == null) {
			for(int i = 0; i < instanceWriters.length; i++) {
				String[] prefixes = null;
				String[] uris = null;
				if(rootElementPrefixes != null) {
					prefixes = (String[])rootElementPrefixes.keySet().toArray(new String[0]);
					uris = (String[])rootElementPrefixes.values().toArray(new String[0]);
					rootElementPrefixes.clear();
				}
				if(instanceWriters[i].startRootElement(namespaceURI, localName, qName,
													   attr, prefixes, uris)) {
					currentWriter = instanceWriters[i];
					break;
				}
			}
			if(currentWriter == null) {
				throw new SAXNotSupportedException("Element was not recognized: " + localName);
			}
		} else {
			currentWriter.startElement(namespaceURI, localName, qName, attr);
		}
	}

	/**
	 * Accepts notification about element end events.
	 *
	 * @param namespaceURI The namespace URI. Namespace processing has to be
	 *                     enabled in XML reader
	 * @param localName The local name (without prefix) 
	 * @param qName The ignored qualified name (can be null)
	 * @exception SAXException if SAX error occurs
	 */
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		currentWriter.endElement(namespaceURI, localName, qName);
	}

	/**
	 * Accepts notification about character events.
	 *
	 * @param charArray The characters from the XML document.
	 * @param start The start position in the array.
	 * @param length The number of characters to read from the array.
	 * @exception SAXException if SAX error occurs
	 */
	public void characters(char charArray[], int start, int length) throws SAXException {
		currentWriter.characters(charArray, start, length);
	}

	/**
	 * Accepts notification about document start events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void startDocument() throws SAXException {
		for(int i = 0; i < instanceWriters.length; i++) {
			instanceWriters[i].startDocument();
		}
	}

	/**
	 * Accepts notification about document end events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void endDocument() throws SAXException {
		currentWriter.endDocument();
		currentWriter = null;
	}

	/**
	 * Skits notification about processing instruction events.
	 *
	 * @param target The processing instruction target.
	 * @param data The processing instruction data.
	 */
	public void processingInstruction(String target, String data) throws SAXException {
	}

	/**
	 * Skips notification about prefix mapping start events.
	 *
	 * @param prefix The Namespace prefix being declared.
	 * @param uri The Namespace URI the prefix is mapped to.
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		if(currentWriter == null) {
			if(rootElementPrefixes == null) {
				rootElementPrefixes = new HashMap();
			}
			rootElementPrefixes.put(prefix, uri);
		} else {
			currentWriter.startPrefixMapping(prefix, uri);
		}
	}


	/**
	 * Skips notification about prefix mapping end events.
	 *
	 * @param prefix The Namespace prefix being declared.
	 * @exception SAXException if an error occurs
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		currentWriter.endPrefixMapping(prefix);
	}

	/**
	 * Ignores the document locator.
	 *
	 * @param locator The document locator
	 */
	public void setDocumentLocator(Locator locator) {
	}

	/**
	 * Skips notification about ignorable whitespace events.
	 *
	 * @param charArray The characters from the XML document.
	 * @param start The start position in the array.
	 * @param length The number of characters to read from the array.
	 */
	public void ignorableWhitespace(char[] charArray, int start, int length) throws SAXException {
		
	}

	/**
	 * Skips notification about skipped entity events.
	 *
	 * @param name The name of the skipped entity.
	 */
	public void skippedEntity(String name) throws SAXException {
		
	}

} // InstanceWriterSelector
