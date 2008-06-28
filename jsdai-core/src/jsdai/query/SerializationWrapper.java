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

package jsdai.query;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 *
 * Created: Tue Jun  3 10:48:03 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class SerializationWrapper implements Externalizable {
	transient private Element element;
	static private ThreadLocal localTransformer = new ThreadLocal();
	static private ThreadLocal localDocumentBuilder = new ThreadLocal();

	public SerializationWrapper(Element element)  {
		this.element = element;
	}

	public SerializationWrapper()  {
	}

	public final Element getElement() {
		return element;
	}

    public void writeExternal(ObjectOutput stream) throws IOException {
		try {
			Transformer transformer = (Transformer)localTransformer.get();
			if(transformer == null) {
				transformer = TransformerFactory.newInstance().newTransformer();
				Properties outputProperties = new Properties();
				outputProperties.setProperty(OutputKeys.METHOD, "xml");
				outputProperties.setProperty(OutputKeys.INDENT, "no");
				transformer.setOutputProperties(outputProperties);
				localTransformer.set(transformer);
			}
			transformer.transform(new DOMSource(SerializableProxyElement.newProxy(element)),
								  new StreamResult((OutputStream)stream));
			stream.write('\32');
		} catch (TransformerConfigurationException e) {
			throw (IOException)(new InvalidObjectException(e.toString())).initCause(e);
		} catch (TransformerException e) {
			throw (IOException)(new InvalidObjectException(e.toString())).initCause(e);
		}
	}

	public void readExternal(ObjectInput stream) throws IOException {
		try {
			DocumentBuilder documentBuilder = (DocumentBuilder)localDocumentBuilder.get();
			if(documentBuilder == null) {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setNamespaceAware(true);
				documentBuilderFactory.setIgnoringComments(true);
				documentBuilderFactory.setIgnoringElementContentWhitespace(true);
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				localDocumentBuilder.set(documentBuilder);
			}
			Document document = documentBuilder.parse(new XMLInputStream(stream));
			element = document.getDocumentElement();
		} catch(ParserConfigurationException e) {
			throw (IOException)(new InvalidObjectException(e.toString())).initCause(e);
		} catch(SAXException e) {
			throw (IOException)(new InvalidObjectException(e.toString())).initCause(e);
		}
	}

	private static class XMLInputStream extends InputStream {
		private ObjectInput stream;
		private boolean eofReached;

		private XMLInputStream(ObjectInput stream) {
			this.stream = stream;
			eofReached = false;
		}

		public int read() throws IOException {
			if(!eofReached) {
				int inByte = stream.read();
				if(inByte == '\32') {
					inByte = -1;
				}
				eofReached = inByte < 0;
				return inByte;
			} else {
				return -1;
			}
		}

	}
} // SerializationWrapper
