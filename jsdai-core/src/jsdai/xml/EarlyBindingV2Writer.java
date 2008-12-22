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

import java.lang.StringBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import jsdai.dictionary.ANamed_type;
import jsdai.dictionary.EAggregation_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EBoolean_type;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EEnumeration_type;
import jsdai.dictionary.EExplicit_attribute;
import jsdai.dictionary.EInteger_type;
import jsdai.dictionary.ELogical_type;
import jsdai.dictionary.ENamed_type;
import jsdai.dictionary.ENumber_type;
import jsdai.dictionary.EReal_type;
import jsdai.dictionary.ESelect_type;
import jsdai.dictionary.ESimple_type;
import jsdai.dictionary.EString_type;
import jsdai.lang.A_string;
import jsdai.lang.EEntity;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * Created: Thu Feb 12 14:44:29 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class EarlyBindingV2Writer extends Iso1030328Writer {

	private static final String BASE_NAMESPACE = "urn:iso10303-28:ex";
	private static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	private static final String EXPRESS = "express";
	private static final String UOS = "uos";
	private static final int ATTR_NONAGG = 0;
	private static final int ATTR_ATTR_AGG = 1;
	private static final int ATTR_AGG_AGG = 2;
	private static final int ATTR_INHERITED = 3;

	private Iso1030328Handler iso1030328Handler;
	private ExpressHandler expressHandler;
	private SchemaPopulationHandler schemaPopulationHandler;
	private UosHandler uosHandler;
	private EntityInstHandler entityInstHandler;
	private AttributeHandler attributeHandler;
	private AttrTypeValueHandler attrTypeValueHandler;

	private Map expressSchemaIds;
	private EEntity_definition entityDef;
	private EAttribute[] attributes;
	private boolean[] attrCharData;
	private int[] attrAggType;
	private int attributeTop;
	private EEntity[] attrDomains;
	private int attrDomainTop;
	private int schemaInstanceNum;

	public EarlyBindingV2Writer(SdaiRepository repository) {
		super(repository);
		iso1030328HeaderNamespace = BASE_NAMESPACE;
	}

	protected boolean startRootElement(String namespaceURI, String localName, String qName,
									   Attributes attr, String[] prefixes,
									   String[] uris) throws SAXException {
		if(BASE_NAMESPACE.equals(namespaceURI)) {
			if(localName.equals(ISO_10303_28) && "2.0".equals(attr.getValue("", "version"))) {
				ctxTop = 0;
				ctxHandlerStack[0] = iso1030328Handler;
				ctxElemStack[0] = ISO_10303_28;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Accepts notification about document start events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void startDocument() throws SAXException {
		super.startDocument();

		iso1030328Handler = new Iso1030328Handler();
		expressHandler = new ExpressHandler();
		schemaPopulationHandler = new SchemaPopulationHandler();
		uosHandler = new UosHandler();
		entityInstHandler = new EntityInstHandler();
		attributeHandler = new AttributeHandler();
		attrTypeValueHandler = new AttrTypeValueHandler();

		expressSchemaIds = new HashMap();
		attributes = new EAttribute[STACK_DEPTH];
		attrCharData = new boolean[STACK_DEPTH];
		attrAggType = new int[STACK_DEPTH];
		attributeTop = -1;
		attrDomains = new EEntity[STACK_DEPTH];
		attrDomainTop = -1;

		try {
			schemaInstanceNum = repository.getSchemas().getMemberCount();
		} catch(SdaiException e) {
			//e.printStackTrace();
			SAXException wrapper = new SAXException(e.toString());
			wrapper.initCause(e);
			throw wrapper;
		}
	}

	/**
	 * Accepts notification about document end events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void endDocument() throws SAXException {
		super.endDocument();

		iso1030328Handler = null;
		expressHandler = null;
		schemaPopulationHandler = null;
		uosHandler = null;
		entityInstHandler = null;
		attributeHandler = null;
		attrTypeValueHandler = null;

		expressSchemaIds = null;
		attributes = null;
		attrCharData = null;
		attrAggType = null;
		attrDomains = null;
	}

	private void processAttributeStart(int attrType, String refId,
									   boolean inherited) throws SAXException, SdaiException {
		attrAggType[attrDepth] = inherited ? attrAggType[attrDepth - 1] : ATTR_NONAGG;
		processAttributeStartNested(attrType, attrDomains[attrDomainTop], refId, inherited);
	}

	private boolean processAttributeStartNested(int attrType, EEntity domain, String refId,
												boolean inherited) throws SAXException, SdaiException {
		if(domain instanceof EAggregation_type) {
			if(inherited) {
				attrAggType[attrDepth] = ATTR_INHERITED;
				if(refId != null) {
					return processAttributeStartNested(ATTR_ATTR_AGG,
													   ((EAggregation_type)domain).getElement_type(null),
													   refId, inherited);
				}
			} else if(attrType > ATTR_NONAGG) {
				attrAggType[attrDepth] = ATTR_AGG_AGG;
				pushCreator(new AggregateAggCr());
			} else {
				attrAggType[attrDepth] = ATTR_ATTR_AGG;
				pushCreator(new AggregateAttCr());
				if(refId != null) {
					return processAttributeStartNested(ATTR_ATTR_AGG,
													   ((EAggregation_type)domain).getElement_type(null),
													   refId, inherited);
				}
			}
			return refId == null;
		} else if(domain instanceof ESelect_type) {
			ANamed_type selections =
				((ESelect_type)domain).getSelections(null, repository.getSession().getSdaiContext());
			SdaiIterator selectionIter = selections.createIterator();
			while(selectionIter.next()) {
				ENamed_type selection = selections.getCurrentMember(selectionIter);
				if(refId != null || attrSelectTop[attrDepth] > 0) {
					if(processAttributeStartNested(attrType, selection, refId, inherited)) {
						return true;
					}
				}
			}
			return false;
		} else if(domain instanceof EDefined_type) {
			return processAttributeStartNested(attrType, ((EDefined_type)domain).getDomain(null),
											   refId, inherited);
		} else if(domain instanceof EEntity_definition) {
			if(refId != null) {
				if(attrType == ATTR_NONAGG) {
					pushCreator(new EntityRefAttCr(refId));
				} else {
					pushCreator(new EntityRefAggCr(refId));
				}
				return true;
			} else {
				return false;
			}
		} else {
			return refId == null;
		}
	}

	private void processAttributeEnd() throws SAXException, SdaiException {
		switch(attrAggType[attrDepth]) {
		case ATTR_ATTR_AGG:
			pushCreator(new AggregateAttEn());
			break;
		case ATTR_AGG_AGG:
			pushCreator(new AggregateAggEn());
			break;
		}
	}

	private void processAttributeValue(String value) throws SAXException, SdaiException {
		EEntity domain = attrDomains[attrDomainTop];
		if(domain instanceof ESimple_type) {
			if(domain instanceof EString_type) {
				if(attrAggType[attrDepth] > ATTR_NONAGG) {
					pushCreator(new StringLitAggCr(value));
				} else {
					pushCreator(new StringLitAttCr(value));
				}
			} else if(domain instanceof EReal_type || domain instanceof ENumber_type) {
				double doubleVal = Double.parseDouble(value);
				if(attrAggType[attrDepth] > ATTR_NONAGG) {
					pushCreator(new RealLitAggCr(doubleVal));
				} else {
					pushCreator(new RealLitAttCr(doubleVal));
				}
			} else if(domain instanceof EInteger_type) {
				int intVal = Integer.parseInt(value);
				if(attrAggType[attrDepth] > ATTR_NONAGG) {
					pushCreator(new IntegerLitAggCr(intVal));
				} else {
					pushCreator(new IntegerLitAttCr(intVal));
				}
			} else if(domain instanceof EBoolean_type || domain instanceof ELogical_type) {
				if("true".equals(value) || "false".equals(value) || "unknown".equals(value)) {
					int logicalVal = ELogical.toInt(value.toUpperCase());
					if(attrAggType[attrDepth] > ATTR_NONAGG) {
						pushCreator(new LogicalLitAggCr(logicalVal));
					} else {
						pushCreator(new LogicalLitAttCr(logicalVal));
					}
				} else {
					throw new SAXNotSupportedException("Expected true/false/unknown but found: " +
													   value + " for attribute: " +
													   attributes[attributeTop]);
				}
			}
		} else if(domain instanceof EEnumeration_type) {
			EEnumeration_type enumType = (EEnumeration_type)domain;
			A_string elements = enumType.getElements(null);
			SdaiIterator elementIter = elements.createIterator();
			int enumIndex = 1;
			while(elementIter.next()) {
				if(elements.getCurrentMember(elementIter).equalsIgnoreCase(value)) {
					if(attrAggType[attrDepth] > ATTR_NONAGG) {
						pushCreator(new EnumRefAggCr(enumIndex));
					} else {
						pushCreator(new EnumRefAttCr(enumIndex));
					}
					enumIndex = 0;
					break;
				}
				enumIndex++;
			}
			if(enumIndex > 0) {
				throw new SAXNotSupportedException("Unknown enumeration value: " + value +
												   " for attribute: " + attributes[attributeTop]);
			}
		} else if(domain instanceof EAggregation_type) {
		} else if(domain instanceof ESelect_type) {
		} else if(domain instanceof EDefined_type) {
			attrDomains[++attrDomainTop] = ((EDefined_type)domain).getDomain(null);
			processAttributeValue(value);
			attrDomainTop--;
		} else if(domain instanceof EEntity_definition) {
		}
	}

	private void fillSelectPath(String path, EDefined_type extraDefType,
								boolean top) throws SAXException, SdaiException {
		attrSelectTop[attrDepth] = 0;
		if(attrSelect[attrDepth] == null) {
			attrSelect[attrDepth] = new EDefined_type[STACK_DEPTH];
		}
		if(path != null) {
			StringTokenizer pathTokenizer = new StringTokenizer(path);
			while(pathTokenizer.hasMoreTokens()) {
				String definedTypeName = pathTokenizer.nextToken();
				EDefined_type definedType = findDefinedType(definedTypeName, null);
				attrSelect[attrDepth][attrSelectTop[attrDepth]++] = definedType;
			}
		} if(!top) {
			System.arraycopy(attrSelect[attrDepth - 1], 0, attrSelect[attrDepth], 0, STACK_DEPTH);
			attrSelectTop[attrDepth] = attrSelectTop[attrDepth - 1];
		}
		if(extraDefType != null) {
			attrSelect[attrDepth][attrSelectTop[attrDepth]++] = extraDefType;
			pushCreator(new SelectLitCr(extraDefType));
		}
	}

	private String formatAttributeName(String name) {
		String lowercasedName = name.toLowerCase();
		StringBuffer buf = new StringBuffer(lowercasedName);
		if(lowercasedName.startsWith("x-m-l")) {
			buf = buf.replace(0, 5, "xml");
		}
		return buf.toString();
	}

	private String formatEntityName(String name) {
		String lowercasedName = name.toLowerCase();
		StringBuffer buf = new StringBuffer(lowercasedName);
		if(lowercasedName.startsWith("x-m-l")) {
			buf = buf.replace(0, 5, "xml");
		}
		for(int i = 1; i < buf.length(); i++) {
			if(buf.charAt(i) == '-') {
				buf.setCharAt(i, '+');
			}
		}
		return buf.toString();
	}

	private class Iso1030328Handler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			if(!"2.0".equals(attr.getValue("", "version"))) {
				throw new SAXNotSupportedException("Unsupported binding version");
			}
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(BASE_NAMESPACE.equals(namespaceURI)) {
				if(localName.equals(ISO_10303_28_HEADER)) {
					pushHandler(iso1030328HeaderHandler, ISO_10303_28_HEADER);
					return iso1030328HeaderHandler;
				} else if(localName.equals(SCHEMA_POPULATION)) {
					pushHandler(schemaPopulationHandler, SCHEMA_POPULATION);
					return schemaPopulationHandler;
				} else if(localName.equals(EXPRESS)) {
					pushHandler(expressHandler, EXPRESS);
					return expressHandler;
				} else if(localName.equals(UOS)) {
					pushHandler(uosHandler, UOS);
					return uosHandler;
				} else {
					return super.newHandlerForElement(namespaceURI, localName, qname);
				}
			} else {
				throw new SAXNotSupportedException("Expected iso_10303_28_header/" +
												   "schema_population/express/uos");
			}
		}
	}

	private class ExpressHandler extends TerminalHandler {
		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			String schemaName = attr.getValue("", "schema_name");
			String id = attr.getValue("", "id");
			expressSchemaIds.put(id, findSchemaClass(schemaName));
		}
	}

	private class SchemaPopulationHandler extends TerminalHandler {
		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			String governingSchema = attr.getValue("", "governing_schema");
			String name = attr.getValue("", "name");
			schemaInstanceNum++;
			if(name == null) {
				name = "schema" + Integer.toString(schemaInstanceNum);
			}
			repository.createSchemaInstance(name, (Class)expressSchemaIds.get(governingSchema));
		}

	}

	private class UosHandler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			String id = attr.getValue("", "id");
			String express = attr.getValue("", "express");
			String description = attr.getValue("", "description");
			String modelName = description != null ? description : id;
			SdaiModel oldModel = repository.findSdaiModel(modelName);
			if(oldModel != null) {
				oldModel.deleteSdaiModel();
			}
			model = repository.createSdaiModel(modelName, (Class)expressSchemaIds.get(express));
			SdaiContext context = new SdaiContext(model);
			repository.getSession().setSdaiContext(context);
			pushCreator(new SdaiContextCr(context));
			model.startReadWriteAccess();
			modelIds.put(id, model);
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				pushHandler(entityInstHandler, localName);
			} else {
				throw new SAXNotSupportedException("Expected entity instance");
			}
			return entityInstHandler;
		}
	}

	private class EntityInstHandler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			EEntity newInstance;
			try {
				String entName = formatEntityName(ctxElemStack[ctxTop]);
				newInstance = model.createEntityInstance(findEntityDefintion(entName, null));
			} catch (SdaiException e) {
				processRecoverableHandlerException(e);
				return;
			}
			String idString = attr.getValue("", "id");
			entityDef = newInstance.getInstanceType();
			if(idString != null) {
				createdInstances.put(idString, newInstance);
				pushCreator(new EntityInstIdCr(idString));
			} else {
				pushCreator(new EntityInstInstCr(newInstance));
			}
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				pushHandler(attributeHandler, localName);
				return attributeHandler;
			} else {
				throw new SAXNotSupportedException("Expected attribute");
			}
		}

	}

	private class AttributeHandler extends CharContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			boolean reference = "true".equals(attr.getValue(XSI_NAMESPACE, "nil"));
			EAttribute attribute;
			try {
				String attributeName = ctxElemStack[ctxTop];
				int attrNameSeparatorPos = attributeName.indexOf('.');
				EEntity_definition attributeEntityDef;
				if(attrNameSeparatorPos >= 0) {
					String entityName = formatEntityName(attributeName.substring(0, attrNameSeparatorPos));
					attributeEntityDef = findEntityDefintion(entityName, null);
					attributeName = attributeName.substring(attrNameSeparatorPos + 1);
				} else {
					attributeEntityDef = entityDef;
				}
				attribute = findAttribute(attributeEntityDef, formatAttributeName(attributeName));
			} catch (SdaiException e) {
				processRecoverableHandlerException(e);
				return;
			}

			try {
				attrDepth++;
				attrCharData[attrDepth] = !reference;
				fillSelectPath(attr.getValue("", "path"), null, true);
			} catch (SdaiException e) {
				attrDepth--;
				processRecoverableHandlerException(e);
				return;
			}

			super.handleElement(attr);
			String refId = reference ? attr.getValue("", "ref") : null;
			attributes[++attributeTop] = attribute;
			attrDomains[++attrDomainTop] = ((EExplicit_attribute)attribute).getDomain(null);
			pushCreator(new AttributeInstCr(attribute));
			processAttributeStart(ATTR_NONAGG, refId, false);
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				attrCharData[attrDepth] = false;
				if(!localName.startsWith("Seq-")) {
					pushHandler(attrTypeValueHandler, localName);
					return attrTypeValueHandler;
				} else {
					return super.newHandlerForElement(namespaceURI, localName, qname);
				}
			} else if(BASE_NAMESPACE.equals(namespaceURI)) {
				attrCharData[attrDepth] = false;
				return super.newHandlerForElement(namespaceURI, localName, qname);
			} else {
				throw new SAXNotSupportedException("Unsupported attribute value: " + localName +
												   " in namespace: " + namespaceURI);
			}
		}

		protected void endElement() throws SAXException, SdaiException {
			if(attrCharData[attrDepth]) {
				processAttributeValue(charDataBuf.toString());
			}
			if(--attributeTop >= 0) {
				pushCreator(new AttributeInstCr(attributes[attributeTop]));
			}
			processAttributeEnd();
			attrDomainTop--;
			attrDepth--;
			super.endElement();
		}

	}

	private class AttrTypeValueHandler extends CharContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			boolean reference = "true".equals(attr.getValue(XSI_NAMESPACE, "nil"));
			EDefined_type definedType;
			try {
				String name = formatEntityName(ctxElemStack[ctxTop]);
				ENamed_type namedType = findNamedType(name);

				definedType = namedType instanceof EDefined_type ?
						(EDefined_type)namedType : null;
			} catch (SdaiException e) {
				processRecoverableHandlerException(e);
				return;
			}

			attrDepth++;
			attrCharData[attrDepth] = definedType != null;
			fillSelectPath(attr.getValue("", "path"), definedType, false);

			super.handleElement(attr);
			String refId = reference ? attr.getValue("", "ref") : null;
			processAttributeStart(attrAggType[attrDepth - 1], refId, definedType == null);
			attrDomainTop++;
			attrDomains[attrDomainTop] = definedType != null ?
				definedType.getDomain(null) : attrDomains[attrDomainTop - 1];
		}

		protected void endElement() throws SAXException, SdaiException {
			if(attrCharData[attrDepth]) {
				processAttributeValue(charDataBuf.toString());
			}
			processAttributeEnd();
			attrDepth--;
			attrDomainTop--;
			super.endElement();
		}

	}

} // EarlyBindingV2Writer
