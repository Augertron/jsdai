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
import java.lang.StringBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import jsdai.dictionary.ADefined_type;
import jsdai.dictionary.EAggregation_type;
import jsdai.dictionary.EArray_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EBag_type;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EEnumeration_type;
import jsdai.dictionary.EList_type;
import jsdai.dictionary.ESet_type;
import jsdai.lang.ASdaiModel;
import jsdai.lang.A_string;
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
import jsdai.lang.ELogical;
import jsdai.lang.JsdaiLangAccessor;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;


/**
 *
 * Created: Wed Feb 11 18:14:06 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class Iso1030328Writer extends InstanceWriter {

	protected static String ISO_10303_28 = "iso_10303_28";
	protected static String ISO_10303_28_HEADER = "iso_10303_28_header";
	protected static String SCHEMA_POPULATION = "schema_population";     
	private static String DOCUMENT_NAME = "document_name";
	private static String TIME_STAMP = "time_stamp";
	private static String AUTHOR = "author";
	private static String ORIGINATING_ORGANIZATION = "originating_organization";
	private static String AUTHORIZATION = "authorization";
	private static String ORIGINATING_SYSTEM = "originating_system";
	private static String PREPROCESSOR_VERSION = "preprocessor_version";     
	private static String DOCUMENTATION = "documentation";

	protected Map modelIds;
	protected Map schInstances;

	protected Iso1030328HeaderHandler iso1030328HeaderHandler;
	private DocumentNameHandler documentNameHandler;
	private TimeStampHandler timeStampHandler;
	private AuthorHandler authorHandler;
	private OriginatingOrganizationHandler originatingOrganizationHandler;
	private AuthorizationHandler authorizationHandler;
	private OriginatingSystemHandler originatingSystemHandler;
	private PreprocessorVersionHandler preprocessorVersionHandler;

	protected String iso1030328HeaderNamespace;

	public Iso1030328Writer(SdaiRepository repository) {
		super(repository);
	}


	/**
	 * Accepts notification about document start events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void startDocument() throws SAXException {
		super.startDocument();

		modelIds = new HashMap();
		schInstances = new HashMap();
		iso1030328HeaderHandler = new Iso1030328HeaderHandler();
		documentNameHandler = new DocumentNameHandler();
		timeStampHandler = new TimeStampHandler();
		authorHandler = new AuthorHandler();
		originatingOrganizationHandler = new OriginatingOrganizationHandler();
		authorizationHandler = new AuthorizationHandler();
		originatingSystemHandler = new OriginatingSystemHandler();
		preprocessorVersionHandler = new PreprocessorVersionHandler();
	}

	/**
	 * Accepts notification about document end events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void endDocument() throws SAXException {
		super.endDocument();

		try {
			Iterator schInstIter = schInstances.entrySet().iterator();
			while(schInstIter.hasNext()) {
				Map.Entry schInstEntry = (Map.Entry)schInstIter.next();
				SchemaInstance schInstance = (SchemaInstance)schInstEntry.getKey();
				String governedSections = (String)schInstEntry.getValue();
				StringTokenizer gsTokenizer = new StringTokenizer(governedSections);
				while (gsTokenizer.hasMoreTokens()) {
					String modelId = gsTokenizer.nextToken();
					Object modelObject = modelIds.get(modelId);
					if(modelObject != null) {
						SdaiModel model = (SdaiModel)modelObject;
						schInstance.addSdaiModel(model);
					} else {
						throw new SAXNotRecognizedException("Model " + modelId + 
															" not found in governed_sections of " +
															schInstance.getName());
					}
				}
			}
		} catch(SdaiException e) {
			//e.printStackTrace();
			SAXException wrapper = new SAXException(e.toString());
			wrapper.initCause(e);
			throw wrapper;
		}

		modelIds = null;
		schInstances = null;
		iso1030328HeaderHandler = null;
		documentNameHandler = null;
		timeStampHandler = null;
		authorHandler = null;
		originatingOrganizationHandler = null;
		authorizationHandler = null;
		originatingSystemHandler = null;
		preprocessorVersionHandler = null;
	}

	private class Iso1030328HeaderHandler extends ContextHandler {
		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(iso1030328HeaderNamespace.equals(valueOf(namespaceURI)))  {
				if(localName.equals(DOCUMENT_NAME)) {
					pushHandler(documentNameHandler, DOCUMENT_NAME);
					return documentNameHandler;
				} else if(localName.equals(TIME_STAMP)) {
					pushHandler(timeStampHandler, TIME_STAMP);
					return timeStampHandler;
				} else if(localName.equals(AUTHOR)) {
					pushHandler(authorHandler, AUTHOR);
					return authorHandler;
				} else if(localName.equals(ORIGINATING_ORGANIZATION)) {
					pushHandler(originatingOrganizationHandler, ORIGINATING_ORGANIZATION);
					return originatingOrganizationHandler;
				} else if(localName.equals(AUTHORIZATION)) {
					pushHandler(authorizationHandler, AUTHORIZATION);
					return authorizationHandler;
				} else if(localName.equals(ORIGINATING_SYSTEM)) {
					pushHandler(originatingSystemHandler, ORIGINATING_SYSTEM);
					return originatingSystemHandler;
				} else if(localName.equals(PREPROCESSOR_VERSION)) {
					pushHandler(preprocessorVersionHandler, PREPROCESSOR_VERSION);
					return preprocessorVersionHandler;
				} else if(localName.equals(DOCUMENTATION)) {
					pushHandler(contextHandler, DOCUMENTATION);
					return contextHandler;
				} else {
					return super.newHandlerForElement(namespaceURI, localName, qname);
				}
			} else {
				throw new SAXNotSupportedException("Unknown iso_10303_28_header child: " + localName);
			}
		}
	}

	private class DocumentNameHandler extends CharContextHandler {
		protected void endElement()
		throws SAXException, SdaiException {
			addStrings(repository.getDescription(), charDataBuf.toString());
			super.endElement();
		}
	}

	private class TimeStampHandler extends CharContextHandler {
	}

	private class AuthorHandler extends CharContextHandler {
		protected void endElement()
		throws SAXException, SdaiException {
			addStrings(repository.getAuthor(), charDataBuf.toString());
			super.endElement();
		}
	}

	private class OriginatingOrganizationHandler extends CharContextHandler {
		protected void endElement()
		throws SAXException, SdaiException {
			addStrings(repository.getOrganization(), charDataBuf.toString());
			super.endElement();
		}
	}

	private class AuthorizationHandler extends CharContextHandler {
		protected void endElement()
		throws SAXException, SdaiException {
			repository.setAuthorization(charDataBuf.toString());
			super.endElement();
		}
	}

	private class OriginatingSystemHandler extends CharContextHandler {
		protected void endElement()
		throws SAXException, SdaiException {
			repository.setOriginatingSystem(charDataBuf.toString());
			super.endElement();
		}
	}

	private class PreprocessorVersionHandler extends CharContextHandler {
		protected void endElement()
		throws SAXException, SdaiException {
			repository.setPreprocessorVersion(charDataBuf.toString());
			super.endElement();
		}
	}

} // Iso1030328Writer
