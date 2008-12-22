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

import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EEnumeration_type;
import jsdai.lang.A_string;
import jsdai.lang.EEntity;
import jsdai.lang.ELogical;
import jsdai.lang.SchemaInstance;
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
 * Created: Wed Feb 11 17:59:32 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class LateBindingWriter extends Iso1030328Writer {

	private static String EXPRESS_DATA = "express_data";
	private static String SCHEMA_INSTANCE = "schema_instance";
	private static String ENTITY_INSTANCE = "entity_instance";
	private static String PARTIAL_ENTITY_INSTANCE = "partial_entity_instance";
	private static String ATTRIBUTE_INSTANCE = "attribute_instance";
	private static String INHERITED_ATTRIBUTE_INSTANCE = "inherited_attribute_instance";
	private static String UNSET = "unset";
	private static String ENTITY_INSTANCE_REF = "entity_instance_ref";
	private static String INTEGER_LITERAL = "integer_literal";
	private static String NUMBER_LITERAL = "number_literal";
	private static String REAL_LITERAL = "real_literal";
	private static String BOOLEAN_LITERAL = "boolean_literal";
	private static String LOGICAL_LITERAL = "logical_literal";
	private static String BINARY_LITERAL = "binary_literal";
	private static String STRING_LITERAL = "string_literal";
	private static String BAG_LITERAL = "bag_literal";
	private static String SET_LITERAL = "set_literal";
	private static String LIST_LITERAL = "list_literal";
	private static String ARRAY_LITERAL = "array_literal";
	private static String TYPE_LITERAL = "type_literal";
	private static String ENUMERATION_REF = "enumeration_ref";

	private String expressDataId;
	private String expressDataName;
	private EEntity_definition[] entityDef;
	private int entityDefTop;
	private String typeName;
	private String typeSchemaName;
	private int schemaInstanceNum;

	private EDefined_type selectDefinedType;

	private Iso1030328Handler iso1030328Handler;
	private ExpressDataHandler expressDataHandler;
	private SchemaPopulationHandler schemaPopulationHandler;
	private SchemaInstHandler schemaInstHandler;
	private EntityInstHandler entityInstHandler;
	private PartEntityInstHnd partEntityInstHnd;
	private AttributeInstTwoHnd attributeInstTwoHnd;
	private TerminalHandler terminalHandler;

	private static int ATTR_HND_INTEGER = 0;
	private static int ATTR_HND_REAL = 1;
	private static int ATTR_HND_STRING = 2;
	private static int ATTR_HND_LOGICAL = 3;
	private static int ATTR_HND_ENTITY_REF = 4;
	private static int ATTR_HND_TYPE = 5;
	private static int ATTR_HND_SELECT = 6;
	private static int ATTR_HND_AGGREGATE = 7;
	private static int ATTR_HND_ENUM = 8;
	private static int ATTR_HND_TERMINAL = 9;
	private ContextHandler[] attrAttHandlers;
	private ContextHandler[] attrAggHandlers;

	public LateBindingWriter(SdaiRepository repository) {
		super(repository);
		iso1030328HeaderNamespace = "";
	}

	protected boolean startRootElement(String namespaceURI, String localName, String qName,
									   Attributes attr, String[] prefixes,
									   String[] uris) throws SAXException {
		// Handle the element at root
		if((valueOf(namespaceURI).equals(""))) {
			if(localName.equals(ISO_10303_28)
			   && "LB".equals(attr.getValue("", "representation_category"))
			   && "PDTS".equals(attr.getValue("", "version"))) {
				ctxTop = 0;
				ctxHandlerStack[0] = iso1030328Handler;
				ctxElemStack[0] = ISO_10303_28;
				return true;
			} else if(localName.equals(EXPRESS_DATA)) {
				ctxTop = 0;
				ctxHandlerStack[0] = expressDataHandler;
				ctxElemStack[0] = EXPRESS_DATA;
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
		expressDataHandler = new ExpressDataHandler();
		schemaPopulationHandler = new SchemaPopulationHandler();
		schemaInstHandler = new SchemaInstHandler();
		entityInstHandler = new EntityInstHandler();
		partEntityInstHnd = new PartEntityInstHnd();
		attributeInstTwoHnd = new AttributeInstTwoHnd();
		terminalHandler = new TerminalHandler();
		attrAttHandlers = new ContextHandler[]
			{ new IntegerLitAttHnd(), new RealLitAttHnd(), new StringLitAttHnd(),
			  new LogicalLitAttHnd(), new EntityRefAttHnd(), null, null,
			  new AggregateAttHnd(), new EnumRefAttHnd(), terminalHandler };
		attrAttHandlers[ATTR_HND_TYPE] = new TypeLitHnd(attrAttHandlers);
		attrAttHandlers[ATTR_HND_SELECT] = new SelectLitHnd(attrAttHandlers);
		attrAggHandlers = new ContextHandler[]
			{ new IntegerLitAggHnd(), new RealLitAggHnd(), new StringLitAggHnd(),
			  new LogicalLitAggHnd(), new EntityRefAggHnd(), null, null,
			  new AggregateAggHnd(), new EnumRefAggHnd(), terminalHandler };
		attrAggHandlers[ATTR_HND_TYPE] = new TypeLitHnd(attrAggHandlers);
		attrAggHandlers[ATTR_HND_SELECT] = new SelectLitHnd(attrAggHandlers);

		typeName = null;
		typeSchemaName = null;
		selectDefinedType = null;
		entityDef = new EEntity_definition[STACK_DEPTH];
		entityDefTop = 0;

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
		expressDataHandler = null;
		schemaPopulationHandler = null;
		schemaInstHandler = null;
		entityInstHandler = null;
		partEntityInstHnd = null;
		attributeInstTwoHnd = null;
		attrAttHandlers = null;
		attrAggHandlers = null;
		entityDef = null;
		typeName = null;
		typeSchemaName = null;
		selectDefinedType = null;
	}

	private class Iso1030328Handler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			if(!"LB".equals(attr.getValue("", "representation_category"))) {
				throw new SAXNotSupportedException("Only late binding is supported");
			}
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				if(localName.equals(ISO_10303_28_HEADER)) {
					pushHandler(iso1030328HeaderHandler, ISO_10303_28_HEADER);
					return iso1030328HeaderHandler;
				} else if(localName.equals(SCHEMA_POPULATION)) {
					pushHandler(schemaPopulationHandler, SCHEMA_POPULATION);
					return schemaPopulationHandler;
				} else if(localName.equals(EXPRESS_DATA)) {
					pushHandler(expressDataHandler, EXPRESS_DATA);
					return expressDataHandler;
				} else {
					return super.newHandlerForElement(namespaceURI, localName, qname);
				}
			} else {
				throw new SAXNotSupportedException("Expected iso_10303_28_header/" +
												   "schema_population/express_data");
			}
		}
	}

	private class ExpressDataHandler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			expressDataId = attr.getValue("", "id");
			expressDataName = attr.getValue("", "name");
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("") && localName.equals(SCHEMA_INSTANCE)) {
				pushHandler(schemaInstHandler, SCHEMA_INSTANCE);
			} else {
				throw new SAXNotSupportedException("Expected schema_instance but found: " + localName);
			}
			return schemaInstHandler;
		}
	}

	private class SchemaPopulationHandler extends TerminalHandler {
		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			String governingSchema = attr.getValue("", "governing_schema");
			String governedSections = attr.getValue("", "governed_sections");
			String name = attr.getValue("", "name");
			schemaInstanceNum++;
			if(name == null) {
				name = "schema" + Integer.toString(schemaInstanceNum);
			}
			SchemaInstance schInstance =
				repository.createSchemaInstance(name, findSchemaClass(governingSchema));
			schInstances.put(schInstance, governedSections);
		}

	}

	private class SchemaInstHandler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			if(ctxTop > 0 && ctxElemStack[ctxTop - 1] == EXPRESS_DATA) {
				String schemaName = attr.getValue("", "schema_name");
				SdaiModel oldModel = repository.findSdaiModel(expressDataName);
				if(oldModel != null) {
					oldModel.deleteSdaiModel();
				}
				model = repository.createSdaiModel(expressDataName, findSchemaClass(schemaName));
				SdaiContext context = new SdaiContext(model);
				repository.getSession().setSdaiContext(context);
				pushCreator(new SdaiContextCr(context));
				model.startReadWriteAccess();
				modelIds.put(expressDataId, model);
			} else {
				throw new
					SAXNotSupportedException("Element schema_instance has to be child of express_data");
			}
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("") && localName.equals(ENTITY_INSTANCE)) {
				pushHandler(entityInstHandler, ENTITY_INSTANCE);
			} else {
				throw new SAXNotSupportedException("Expected entity_instance");
			}
			return entityInstHandler;
		}
	}

	private class EntityInstHandler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			if(ctxTop > 0 && ctxElemStack[ctxTop - 1] == SCHEMA_INSTANCE) {
				EEntity newInstance;
				try {
					String entName = attr.getValue("", "express_entity_name");
					String schemaName = attr.getValue("", "schema_name");
					newInstance = model.createEntityInstance(findEntityDefintion(entName, schemaName));
				} catch (SdaiException e) {
					processRecoverableHandlerException(e);
					return;
				}
				String idString = attr.getValue("", "id");
				entityDefTop = 0;
				entityDef[0] = newInstance.getInstanceType();
				if(idString != null) {
					createdInstances.put(idString, newInstance);
					pushCreator(new EntityInstIdCr(idString));
				} else {
					pushCreator(new EntityInstInstCr(newInstance));
				}
			} else {
				throw new SAXNotSupportedException
					("Element entity_instance has to be child of schema_instance");
			}
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				if(localName.equals(ATTRIBUTE_INSTANCE)) {
					pushHandler(attributeInstTwoHnd, ATTRIBUTE_INSTANCE);
					return attributeInstTwoHnd;
				} else if(localName.equals(INHERITED_ATTRIBUTE_INSTANCE)) {
					pushHandler(attributeInstTwoHnd, INHERITED_ATTRIBUTE_INSTANCE);
					return attributeInstTwoHnd;
				} else if(localName.equals(PARTIAL_ENTITY_INSTANCE)) {
					pushHandler(partEntityInstHnd, PARTIAL_ENTITY_INSTANCE);
					return partEntityInstHnd;
				} else {
					return super.newHandlerForElement(namespaceURI, localName, qname);
				}
			} else {
				throw new SAXNotSupportedException("Expected entity_instance");
			}
		}

	}

	private class PartEntityInstHnd extends EntityInstHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			try {
				String entName = attr.getValue("", "express_entity_name");
				String schemaName = attr.getValue("", "schema_name");
				entityDef[++entityDefTop] = findEntityDefintion(entName, schemaName);
			} catch (SdaiException e) {
				processRecoverableHandlerException(e);
				return;
			}
		}

		protected void endElement()
		throws SAXException, SdaiException {
			entityDefTop--;
			popHandler();
		}
	}

	private class AttributeBaseHnd extends ContextHandler {

		protected ContextHandler makeHandlerForElement(ContextHandler handlers[], String localName)
		throws SAXException, SdaiException {
			if(localName.equals(INTEGER_LITERAL)) {
				pushHandler(handlers[ATTR_HND_INTEGER], INTEGER_LITERAL);
				return handlers[ATTR_HND_INTEGER];
			} else if(localName.equals(STRING_LITERAL) || localName.equals(BINARY_LITERAL)) {
				pushHandler(handlers[ATTR_HND_STRING], STRING_LITERAL);
				return handlers[ATTR_HND_STRING];
			} else if(localName.equals(REAL_LITERAL) || localName.equals(NUMBER_LITERAL)) {
				pushHandler(handlers[ATTR_HND_REAL], REAL_LITERAL);
				return handlers[ATTR_HND_REAL];
			} else if(localName.equals(LOGICAL_LITERAL) || localName.equals(BOOLEAN_LITERAL)) {
				pushHandler(handlers[ATTR_HND_LOGICAL], LOGICAL_LITERAL);
				return handlers[ATTR_HND_LOGICAL];
			} else if(localName.equals(UNSET)) {
				pushHandler(handlers[ATTR_HND_TERMINAL], UNSET);
				return handlers[ATTR_HND_TERMINAL];
			} else if(localName.equals(ENTITY_INSTANCE_REF)) {
				pushHandler(handlers[ATTR_HND_ENTITY_REF], ENTITY_INSTANCE_REF);
				return handlers[ATTR_HND_ENTITY_REF];
			} else if(localName.equals(TYPE_LITERAL)) {
				pushHandler(handlers[ATTR_HND_TYPE], TYPE_LITERAL);
				return handlers[ATTR_HND_TYPE];
			} else if(localName.equals(BAG_LITERAL) || localName.equals(SET_LITERAL)
			|| localName.equals(LIST_LITERAL) || localName.equals(ARRAY_LITERAL)) {
				pushHandler(handlers[ATTR_HND_AGGREGATE], ARRAY_LITERAL);
				return handlers[ATTR_HND_AGGREGATE];
			} else {
				return super.newHandlerForElement("", localName, "");
			}
		}

	}

	private class AttributeInstTwoHnd extends AttributeBaseHnd {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			EAttribute attribute;
			try {
				attribute = findAttribute(entityDef[entityDefTop],
						attr.getValue("", "express_attribute_name"));
			} catch (SdaiException e) {
				processRecoverableHandlerException(e);
				return;
			}
			pushCreator(new AttributeInstCr(attribute));
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				return makeHandlerForElement(attrAttHandlers, localName);
			} else {
				throw new SAXNotSupportedException("Unexpected child");
			}
		}
	}

	private class AggregateAttHnd extends AttributeBaseHnd {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			pushCreator(new AggregateAttCr());
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				return makeHandlerForElement(attrAggHandlers, localName);
			} else {
				throw new SAXNotSupportedException("Unexpected child");
			}
		}

		protected void endElement()
		throws SAXException, SdaiException {
			pushCreator(new AggregateAttEn());
			popHandler();
		}
	}

	private class AggregateAggHnd extends AttributeBaseHnd {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			pushCreator(new AggregateAggCr());
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				return makeHandlerForElement(attrAggHandlers, localName);
			} else {
				throw new SAXNotSupportedException("Unexpected child");
			}
		}

		protected void endElement()
		throws SAXException, SdaiException {
			pushCreator(new AggregateAggEn());
			popHandler();
		}
	}

	private class EntityRefAttHnd extends TerminalHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			String refId = attr.getValue("", "refid");
			pushCreator(new EntityRefAttCr(refId));
		}

	}

	private class EntityRefAggHnd extends TerminalHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			String refId = attr.getValue("", "refid");
			pushCreator(new EntityRefAggCr(refId));
		}

	}

	private class TypeBaseHnd extends ContextHandler {

		protected ContextHandler makeHandlerForElement(ContextHandler handlers[], String localName)
			throws SAXException, SdaiException {
			if(localName.equals(INTEGER_LITERAL)) {
				pushHandler(handlers[ATTR_HND_INTEGER], INTEGER_LITERAL);
				return handlers[ATTR_HND_INTEGER];
			} else if(localName.equals(STRING_LITERAL) || localName.equals(BINARY_LITERAL)) {
				pushHandler(handlers[ATTR_HND_STRING], STRING_LITERAL);
				return handlers[ATTR_HND_STRING];
			} else if(localName.equals(REAL_LITERAL) || localName.equals(NUMBER_LITERAL)) {
				pushHandler(handlers[ATTR_HND_REAL], REAL_LITERAL);
				return handlers[ATTR_HND_REAL];
			} else if(localName.equals(LOGICAL_LITERAL) || localName.equals(BOOLEAN_LITERAL)) {
				pushHandler(handlers[ATTR_HND_LOGICAL], LOGICAL_LITERAL);
				return handlers[ATTR_HND_LOGICAL];
			} else if(localName.equals(UNSET)) {
				pushHandler(handlers[ATTR_HND_TERMINAL], UNSET);
				return handlers[ATTR_HND_TERMINAL];
			} else if(localName.equals(ENTITY_INSTANCE_REF)) {
				pushHandler(handlers[ATTR_HND_ENTITY_REF], ENTITY_INSTANCE_REF);
				return handlers[ATTR_HND_ENTITY_REF];
			} else if(localName.equals(TYPE_LITERAL)) {
				pushHandler(handlers[ATTR_HND_SELECT], TYPE_LITERAL);
				return handlers[ATTR_HND_SELECT];
			} else if(localName.equals(ENUMERATION_REF)) {
				pushHandler(handlers[ATTR_HND_ENUM], ENUMERATION_REF);
				return handlers[ATTR_HND_ENUM];
			} else if(localName.equals(BAG_LITERAL) || localName.equals(SET_LITERAL)
			|| localName.equals(LIST_LITERAL) || localName.equals(ARRAY_LITERAL)) {
				pushHandler(handlers[ATTR_HND_AGGREGATE], ARRAY_LITERAL);
				return handlers[ATTR_HND_AGGREGATE];
			} else {
				return super.newHandlerForElement("", localName, "");
			}
		}

	}

	private class TypeLitHnd extends TypeBaseHnd {

		private ContextHandler[] handlers;

		private TypeLitHnd(ContextHandler[] handlers) {
			this.handlers = handlers;
		}

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			typeName = attr.getValue("", "express_type_name");
			typeSchemaName = attr.getValue("", "schema_name");
			selectDefinedType = null;
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				return makeHandlerForElement(handlers, localName);
			} else {
				throw new SAXNotSupportedException("Expected type_literal");
			}
		}
	}

	private class SelectLitHnd extends TypeBaseHnd {

		private ContextHandler[] handlers;

		private SelectLitHnd(ContextHandler[] handlers) {
			this.handlers = handlers;
		}

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			try {
				String selectName = attr.getValue("", "express_type_name");
				String schemaName = attr.getValue("", "schema_name");
				selectDefinedType = findDefinedType(selectName, schemaName);
			} catch (SdaiException e) {
				processRecoverableHandlerException(e);
				return;
			}
			pushCreator(new SelectLitCr(selectDefinedType));
		}

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")) {
				return makeHandlerForElement(handlers, localName);
			} else {
				throw new SAXNotSupportedException("Expected type_literal");
			}
		}
	}

	private class IntegerLitAttHnd extends CharContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("Integer can not have child elements");
		}

		protected void endElement()
		throws SAXException, SdaiException {
			int intVal = Integer.parseInt(charDataBuf.toString());
			pushCreator(new IntegerLitAttCr(intVal));
			super.endElement();
		}
	}

	private class IntegerLitAggHnd extends CharContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("Integer can not have child elements");
		}

		protected void endElement()
		throws SAXException, SdaiException {
			int intVal = Integer.parseInt(charDataBuf.toString());
			pushCreator(new IntegerLitAggCr(intVal));
			super.endElement();
		}
	}

	private class RealLitAttHnd extends CharContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("Real can not have child elements");
		}

		protected void endElement()
		throws SAXException, SdaiException {
			double doubleVal = Double.parseDouble(charDataBuf.toString());
			pushCreator(new RealLitAttCr(doubleVal));
			super.endElement();
		}
	}

	private class RealLitAggHnd extends CharContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("Real can not have child elements");
		}

		protected void endElement()
		throws SAXException, SdaiException {
			double doubleVal = Double.parseDouble(charDataBuf.toString());
			pushCreator(new RealLitAggCr(doubleVal));
			super.endElement();
		}
	}

	private class LogicalLitAttHnd extends ContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("") && (localName.equals("true") ||
													localName.equals("false") ||
													localName.equals("unknown"))) {
				int logicalVal = ELogical.toInt(localName.toUpperCase());
				pushCreator(new LogicalLitAttCr(logicalVal));
				pushHandler(terminalHandler, null);
				return terminalHandler;
			} else {
				throw new SAXNotSupportedException("Expected true/false/unknown");
			}
		}
	}

	private class LogicalLitAggHnd extends ContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			if(valueOf(namespaceURI).equals("")	&& (localName.equals("true") ||
													localName.equals("false") ||
													localName.equals("unknown"))) {
				int logicalVal = ELogical.toInt(localName.toUpperCase());
				pushCreator(new LogicalLitAggCr(logicalVal));
				pushHandler(terminalHandler, null);
				return terminalHandler;
			} else {
				throw new SAXNotSupportedException("Expected true/false/unknown");
			}
		}
	}

	private class StringLitAttHnd extends CharContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("String can not have child elements");
		}

		protected void endElement()
		throws SAXException, SdaiException {
			String stringVal = charDataBuf.toString();
			pushCreator(new StringLitAttCr(stringVal));
			super.endElement();
		}
	}

	private class StringLitAggHnd extends CharContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("String can not have child elements");
		}

		protected void endElement()
		throws SAXException, SdaiException {
			String stringVal = charDataBuf.toString();
			pushCreator(new StringLitAggCr(stringVal));
			super.endElement();
		}
	}

	private abstract class EnumRefBaseHnd extends CharContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("Enumeration_ref can not have child elements");
		}

		protected abstract void setEnum(int enumIndex) throws SdaiException;

		protected void endElement()
		throws SAXException, SdaiException {
			String enumValue = charDataBuf.toString();
			EDefined_type definedType;
			try {
				definedType = selectDefinedType != null ? selectDefinedType : findDefinedType(typeName, typeSchemaName);
			} catch (SdaiException e) {
				processRecoverableHandlerException(e);
				return;
			}
			EEnumeration_type enumType =
				(EEnumeration_type)definedType.getDomain(null);
			A_string elements = enumType.getElements(null);
			SdaiIterator elementIter = elements.createIterator();
			int enumIndex = 1;
			while(elementIter.next()) {
				if(elements.getCurrentMember(elementIter).equalsIgnoreCase(enumValue)) {
					setEnum(enumIndex);
					super.endElement();
					return;
				}
				enumIndex++;
			}
			throw new SAXNotSupportedException("Unknown enumeration value: " + enumValue);
		}
	}

	private class EnumRefAttHnd extends EnumRefBaseHnd {

		protected void setEnum(int enumIndex) throws SdaiException {
			pushCreator(new EnumRefAttCr(enumIndex));
		}

	}

	private class EnumRefAggHnd extends EnumRefBaseHnd {

		protected void setEnum(int enumIndex) throws SdaiException {
			pushCreator(new EnumRefAggCr(enumIndex));
		}

	}


} // LateBindingWriter
