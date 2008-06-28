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

import jsdai.lang.*;

import java.io.IOException;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.InputSource;

/**
 * This class is an abstract implementation of <code>org.xml.sax.XMLReader</code>
 * suited for SDAI linking with JAXP. It mostly contains empty stub methods that
 * can be overwritten by extending classes.
 *
 * @author  Vaidas Nargelas
 * @version $Revision$
 * @see org.xml.sax.XMLReader
 */
public abstract class SdaiXmlReader extends JsdaiLangAccessor implements XMLReader {

	protected ContentHandler contentHandler;
	
	protected AttributesImpl attributes = new AttributesImpl();

	protected SdaiXmlReader() { }

	/**
	 * Empty implementation of <code>XMLReader.getFeature</code> method.
	 *
	 * @param name The feature name
	 * @return false
	 * @exception SAXNotRecognizedException if an error occurs
	 * @exception SAXNotSupportedException if an error occurs
	 */
	public boolean getFeature (String name)
	throws SAXNotRecognizedException, SAXNotSupportedException {
// 		System.out.println("getFeature: " + name);
		return false;
	}
		
	/**
	 * Empty implementation of <code>XMLReader.setFeature</code> method.
	 *
	 * @param name The feature name
	 * @param value The feature value
	 * @exception SAXNotRecognizedException if an error occurs
	 * @exception SAXNotSupportedException if an error occurs
	 */
	public void setFeature (String name, boolean value)
	throws SAXNotRecognizedException, SAXNotSupportedException {
// 		System.out.println("setFeature: " + name);
	}

	/**
	 * Empty implementation of <code>XMLReader.getProperty</code> method.
	 *
	 * @param name The property name
	 * @return null
	 * @exception SAXNotRecognizedException if an error occurs
	 * @exception SAXNotSupportedException if an error occurs
	 */
	public Object getProperty (String name)
	throws SAXNotRecognizedException, SAXNotSupportedException {
// 		System.out.println("getProperty: " + name);
		return null;
	}

	/**
	 * Empty implementation of <code>XMLReader.setProperty</code> method.
	 *
	 * @param name The property name
	 * @param value The property value
	 * @exception SAXNotRecognizedException if an error occurs
	 * @exception SAXNotSupportedException if an error occurs
	 */
	public void setProperty (String name, Object value)
	throws SAXNotRecognizedException, SAXNotSupportedException {
// 		System.out.println("setProperty: " + name);
	}

	/**
	 * Empty implementation of <code>XMLReader.setEntityResolver</code> method.
	 *
	 * @param resolver The entity resolver
	 */
	public void setEntityResolver (EntityResolver resolver) {
	}

	/**
	 * Empty implementation of <code>XMLReader.getEntityResolver</code> method.
	 *
	 * @return null
	 */
	public EntityResolver getEntityResolver () {
		return null;
	}

	/**
	 * Empty implementation of <code>XMLReader.setDTDHandler</code> method.
	 *
	 * @param handler The DTD handler
	 */
	public void setDTDHandler (DTDHandler handler) {
	}

	/**
	 * Empty implementation of <code>XMLReader.getDTDHandler</code> method.
	 *
	 * @return null
	 */
	public DTDHandler getDTDHandler () {
		return null;
	}

	/**
	 * Empty implementation of <code>XMLReader.setContentHandler</code> method.
	 *
	 * @param handler The context handler
	 */
	public void setContentHandler (ContentHandler handler) {
		contentHandler = handler;
	}

	/**
	 * Empty implementation of <code>XMLReader.getContentHandler</code> method.
	 *
	 * @return null
	 */
	public ContentHandler getContentHandler () {
		return contentHandler;
	}

	/**
	 * Empty implementation of <code>XMLReader.setErrorHandler</code> method.
	 *
	 * @param handler The error handler
	 */
	public void setErrorHandler (ErrorHandler handler) {
	}

	/**
	 * Empty implementation of <code>XMLReader.getErrorHandler</code> method.
	 *
	 * @return null
	 */
	public ErrorHandler getErrorHandler () {
		return null;
	}

	/**
	 * Empty implementation of <code>XMLReader.parse(String)</code> method.
	 * It invokes <code>XMLReader.parse(InputSource)</code> method with null parameter.
	 *
	 * @param systemId The system id string
	 * @exception IOException if an error occurs
	 * @exception SAXException if an error occurs
	 */
	public void parse (String systemId)
	throws IOException, SAXException {
		parse((InputSource)null);
	}
}
