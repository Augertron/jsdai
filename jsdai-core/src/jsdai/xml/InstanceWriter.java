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

import java.io.PrintWriter;
import java.lang.StringBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import jsdai.dictionary.ADefined_type;
import jsdai.dictionary.EAggregation_type;
import jsdai.dictionary.EArray_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EBag_type;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EList_type;
import jsdai.dictionary.ENamed_type;
import jsdai.dictionary.ESet_type;
import jsdai.lang.A_string;
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
import jsdai.lang.JsdaiLangAccessor;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 * This class is a way to write (create) JSDAI population
 * from XML parsing events. It acts as XML contents handler and
 * can be used in any environment which generates XML parsing events
 * (eg. XML text file parsing).
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class InstanceWriter extends JsdaiLangAccessor implements ContentHandler {

	protected static final int STACK_DEPTH = 20;
	private static final int DICTIONARY_DATA_LENGTH = "_DICTIONARY_DATA".length();

	private Map rootElementPrefixes;

	protected SdaiRepository repository;
	protected SdaiModel model;
	protected Locator locator;
	protected int ctxTop;
	protected ContextHandler ctxHandlerStack[];
	protected String ctxElemStack[];
	protected StringBuffer charDataBuf;
	private Map definedTypes;

	protected Map createdInstances;
	protected EEntity currentInstance;
	protected String currentInstanceId;
	protected EAttribute currentAttribute;
	protected EDefined_type[][] attrSelect;
	protected int[] attrSelectTop;
	protected int attrDepth;
	protected Aggregate[] aggregates;
	protected int[] aggIndexes;
	protected AggMemberHnd aggMemberHnd;

	protected ContextHandler contextHandler;

	private Creator headCreator;
	private Creator currentCreator;

	private BagSetMemberHnd bagSetMemberHnd;
	private ListMemberHnd listMemberHnd;

// 	private long startMillis;

	/**
	 * Creates a new instance writer for creating population in
	 * specified repository.
	 *
	 * @param repository The repository
	 */
	protected InstanceWriter(SdaiRepository repository) {
		this.repository = repository;
		this.locator = null;
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
		try {
			if(ctxTop < 0) {
				String[] prefixes = null;
				String[] uris = null;
				if(rootElementPrefixes != null) {
					prefixes = (String[])rootElementPrefixes.keySet().toArray(new String[0]);
					uris = (String[])rootElementPrefixes.values().toArray(new String[0]);
					rootElementPrefixes.clear();
				}
				if(!startRootElement(namespaceURI, localName, qName, attr, prefixes, uris)) {
					throw new SAXNotSupportedException("Unexpected root element: " + localName);
				}
			} else {
				ctxHandlerStack[ctxTop].newHandlerForElement(namespaceURI, localName, qName);
			}
			ctxHandlerStack[ctxTop].handleElement(attr);
		} catch(SdaiException e) {
			//e.printStackTrace();
			SAXException wrapper = new SAXException(e.toString());
			wrapper.initCause(e);
			throw wrapper;
		}
	}

	protected abstract boolean startRootElement(String namespaceURI, String localName, String qName,
												Attributes attr, String[] prefixes,
												String[] uris) throws SAXException;

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
		try {
			ctxHandlerStack[ctxTop].endElement();
		} catch(SdaiException e) {
			//e.printStackTrace();
			SAXException wrapper = new SAXException(e.toString());
			wrapper.initCause(e);
			throw wrapper;
		}
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
		try {
			ctxHandlerStack[ctxTop].characters(charArray, start, length);
		} catch(SdaiException e) {
			//e.printStackTrace();
			SAXException wrapper = new SAXException(e.toString());
			wrapper.initCause(e);
			throw wrapper;
		}
	}

	/**
	 * Accepts notification about document start events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void startDocument() throws SAXException {
// 		startMillis = System.currentTimeMillis();
		ctxTop = -1;
		ctxHandlerStack = new ContextHandler[STACK_DEPTH];
		ctxElemStack = new String[STACK_DEPTH];
		contextHandler = new ContextHandler();

		createdInstances = new HashMap();
		definedTypes = new HashMap();
		currentInstance = null;
		currentInstanceId = null;
		currentAttribute = null;
		attrSelect = new EDefined_type[STACK_DEPTH][];
		attrSelectTop = new int[STACK_DEPTH];
		aggregates = new Aggregate[STACK_DEPTH];
		aggIndexes = new int[STACK_DEPTH];

		charDataBuf = new StringBuffer();
		headCreator = new Creator();
		currentCreator = headCreator;

		bagSetMemberHnd = new BagSetMemberHnd();
		listMemberHnd = new ListMemberHnd();
	}

	/**
	 * Accepts notification about document end events.
	 *
	 * @exception SAXException if SAX error occurs
	 */
	public void endDocument() throws SAXException {
// 			System.out.print("Import from XML time (1st pass): ");
// 			System.out.println((System.currentTimeMillis() - startMillis)/1000.0);
		Creator creator = headCreator.next;
		currentCreator = null;
		headCreator = null;
		while(creator != null) {
			try {
				creator.processCreate(this);
				creator = creator.next;
			} catch(SdaiException e) {
				//e.printStackTrace();
				SAXException wrapper = new SAXException("creator: " + creator +
														"\ninstance id: " + currentInstanceId +
														"\ninstance: " + currentInstance +
														"\nattribute: " + currentAttribute + "\n" + e);
				wrapper.initCause(e);
				throw wrapper;
			}
		}

		ctxHandlerStack = null;
		ctxElemStack = null;
		contextHandler = null;
		createdInstances = null;
		definedTypes = null;
		currentInstance = null;
		currentInstanceId = null;
		currentAttribute = null;
		attrSelect = null;
		aggregates = null;
		aggIndexes = null;
		charDataBuf = null;
		aggMemberHnd = null;

		bagSetMemberHnd = null;
		listMemberHnd = null;

// 		System.out.print("Import from XML time: ");
// 		System.out.println((System.currentTimeMillis() - startMillis)/1000.0);
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
		if(ctxTop < 0) {
			if(rootElementPrefixes == null) {
				rootElementPrefixes = new HashMap();
			}
			rootElementPrefixes.put(prefix, uri);
		} else {
			startPrefixMappingImpl(prefix, uri);
		}
	}

	protected void startPrefixMappingImpl(String prefix, String uri) throws SAXException {
	}


	/**
	 * Skips notification about prefix mapping end events.
	 *
	 * @param prefix The Namespace prefix being declared.
	 * @exception SAXException if an error occurs
	 */
	public void endPrefixMapping(String prefix) throws SAXException {

	}

	/**
	 * Ignores the document locator.
	 *
	 * @param locator The document locator
	 */
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
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

	protected String getErrorPrefix() {
		if(locator == null) {
			return "";
		} else {
			String fileLocation = locator.getSystemId();
			if(fileLocation == null) {
				fileLocation = locator.getPublicId();
			}
			if(fileLocation == null) {
				fileLocation = "";
			}
			return fileLocation + ":" + locator.getLineNumber() + ":" + locator.getColumnNumber() + ": ";
		}
	}

	protected Class findSchemaClass(String schemaName)
	throws SAXException, SdaiException {
		SdaiModel dictionaryModel = getSchemaModelFromSystemRepository(schemaName);
		if(dictionaryModel != null) {
			schemaName = dictionaryModel.getName();
			schemaName = schemaName.substring(0, schemaName.length() - DICTIONARY_DATA_LENGTH);
		}
		String formattedSchemaName;
		if(schemaName.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA")) {
			formattedSchemaName = "jsdai.dictionary.SDictionary";
		} else if(schemaName.equalsIgnoreCase("SDAI_MAPPING_SCHEMA")) {
			formattedSchemaName = "jsdai.mapping.SMapping";
		} else {
			formattedSchemaName = schemaName.substring(0, 1).toUpperCase() +
				schemaName.substring(1).toLowerCase();
			formattedSchemaName = "jsdai.S" + formattedSchemaName + ".S" + formattedSchemaName;
		}
		try {
			return Class.forName(formattedSchemaName);
		} catch(Exception e) {
			throw new SAXException(e.toString());
		}
	}

	protected EDefined_type findDefinedType(String definedTypeName, String schemaName)
	throws SAXException, SdaiException {
		if(schemaName == null) {
			return findSchemaDefinedType(model, definedTypeName);
		} else {
			Map defTypesForSchema = (Map)definedTypes.get(schemaName);
			if(defTypesForSchema == null) {
				defTypesForSchema = new HashMap();
				SdaiRepository dictionaryRep =
					model.getUnderlyingSchema().findEntityInstanceSdaiModel().getRepository();
				SdaiModel schemaModel =
					dictionaryRep.findSdaiModel(schemaName.toUpperCase() +
												SdaiSession.DICTIONARY_NAME_SUFIX);
				ADefined_type definedTypeAgg =
					(ADefined_type)schemaModel.getInstances(EDefined_type.class);
				SdaiIterator definedTypeIter = definedTypeAgg.createIterator();
				while(definedTypeIter.next()) {
					EDefined_type definedType = definedTypeAgg.getCurrentMember(definedTypeIter);
					defTypesForSchema.put(definedType.getName(null), definedType);
				}
				definedTypes.put(schemaName, defTypesForSchema);
			}
			return (EDefined_type)defTypesForSchema.get(definedTypeName);
		}
	}

	protected EEntity_definition findEntityDefintion(String entityName, String schemaName)
	throws SAXException, SdaiException {
		EEntity_definition entDefinition;
		if(schemaName != null) {
			SdaiRepository dictionaryRep =
				model.getUnderlyingSchema().findEntityInstanceSdaiModel().getRepository();
			SdaiModel schemaModel =
				dictionaryRep.findSdaiModel(schemaName.toUpperCase() +
											SdaiSession.DICTIONARY_NAME_SUFIX);
			entDefinition = findDictionaryEntityDefinition(schemaModel, entityName);
		} else {
			entDefinition = findSchemaEntityDefinitionFast(model, entityName);
			if(entDefinition == null) {
				throw new SdaiException(SdaiException.EI_NEXS, entityName + " in model " + model);
			}
		}
		return entDefinition;
	}

	protected ENamed_type findNamedType(String name) throws SAXException, SdaiException {
		ENamed_type namedType = findSchemaEntityDefinitionFast(model, name);
		if(namedType == null) {
			namedType = findSchemaDefinedTypeFast(model, name);
			if(namedType == null) {
				throw new SdaiException(SdaiException.ED_NDEF, "Named type not found: " + name);
			}
		}
		return namedType;
	}

	protected void processRecoverableHandlerException(SdaiException e) throws SdaiException {
		PrintWriter logWriter = repository != null ? repository.getSession().getLogWriterSession() : null;
		if(logWriter == null) {
			logWriter = SdaiSession.getLogWriter();
		}
		if(logWriter != null) {
			logWriter.print("WARNING: Recoverable exception ");
			e.printStackTrace(logWriter);
		}
		ctxHandlerStack[ctxTop] = contextHandler;
	}

	protected void pushCreator(Creator creator) {
		currentCreator.next = creator;
		currentCreator = creator;
	}

	protected static void addStrings(A_string strings, String values) throws SdaiException {
		StringTokenizer valueTokenizer = new StringTokenizer(values, "\n\r");
		int stringsIdx = strings.getMemberCount();
		while(valueTokenizer.hasMoreTokens()) {
			String value = valueTokenizer.nextToken().trim();
			if(!value.equals("")) {
				strings.addByIndex(++stringsIdx, value);
			}
		}
	}

	static protected String valueOf(String string) {
		return string == null ? "" : string;
	}

	private void setAggType() throws SdaiException {
		EAggregation_type aggType = aggregates[attrDepth].getAggregationType();
		if(aggType instanceof EBag_type) {
			aggMemberHnd = bagSetMemberHnd;
		} else if(aggType instanceof ESet_type) {
			aggMemberHnd = bagSetMemberHnd;
		} else if(aggType instanceof EList_type) {
			aggMemberHnd = listMemberHnd;
		} else if(aggType instanceof EArray_type) {
			aggMemberHnd = listMemberHnd;
		}
	}

	protected class ContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			pushHandler(contextHandler, null);
			return contextHandler;
		}

		protected void pushHandler(ContextHandler handler, String elem)
		throws SAXException, SdaiException {
			ctxHandlerStack[++ctxTop] = handler;
			ctxElemStack[ctxTop] = elem;
		}

		protected ContextHandler popHandler()
		throws SAXException, SdaiException {
			ctxTop--;
			return ctxHandlerStack[ctxTop + 1];
		}

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
		}

		protected void endElement()
		throws SAXException, SdaiException {
			popHandler();
		}

		protected void characters(char charArray[], int start, int length)
		throws SAXException, SdaiException {
		}
	}

	protected class CharContextHandler extends ContextHandler {

		protected void handleElement(Attributes attr)
		throws SAXException, SdaiException {
			charDataBuf.setLength(0);
		}

		protected void characters(char charArray[], int start, int length)
		throws SAXException, SdaiException {
			charDataBuf.append(charArray, start, length);
		}

		protected void endElement() throws SAXException, SdaiException {
			charDataBuf.setLength(0);
			popHandler();
		}
	}

	protected class TerminalHandler extends ContextHandler {

		protected ContextHandler newHandlerForElement(String namespaceURI, String localName, String qname)
		throws SAXException, SdaiException {
			throw new SAXNotSupportedException("Unexpected child " + localName);
		}
	}

	private abstract class AggMemberHnd {
		protected abstract void addAggMember(int member) throws SdaiException;

		protected abstract void addAggMember(double member) throws SdaiException;

		protected abstract void addAggMember(Object member) throws SdaiException;

		protected abstract Aggregate addAggAggregate() throws SdaiException;
	}

	private class BagSetMemberHnd extends AggMemberHnd {
		protected void addAggMember(int member) throws SdaiException {
			aggIndexes[attrDepth]++;
			aggregates[attrDepth].addUnordered(member, attrSelect[attrDepth]);
		}

		protected void addAggMember(double member) throws SdaiException {
			aggIndexes[attrDepth]++;
			aggregates[attrDepth].addUnordered(member, attrSelect[attrDepth]);
		}

		protected void addAggMember(Object member) throws SdaiException {
			aggIndexes[attrDepth]++;
			aggregates[attrDepth].addUnordered(member, attrSelect[attrDepth]);
		}

		protected Aggregate addAggAggregate() throws SdaiException {
			aggIndexes[attrDepth]++;
			return aggregates[attrDepth].createAggregateUnordered(attrSelect[attrDepth]);
		}
	}

	private class ListMemberHnd extends AggMemberHnd {
		protected void addAggMember(int member) throws SdaiException {
// 			int aggMemberCount = aggregates[attrDepth].getMemberCount() + 1;
			aggregates[attrDepth].addByIndex(++aggIndexes[attrDepth], member, attrSelect[attrDepth]);
		}

		protected void addAggMember(double member) throws SdaiException {
// 			int aggMemberCount = aggregates[attrDepth].getMemberCount() + 1;
			aggregates[attrDepth].addByIndex(++aggIndexes[attrDepth], member, attrSelect[attrDepth]);
		}

		protected void addAggMember(Object member) throws SdaiException {
// 			int aggMemberCount = aggregates[attrDepth].getMemberCount() + 1;
			aggregates[attrDepth].addByIndex(++aggIndexes[attrDepth], member, attrSelect[attrDepth]);
		}

		protected Aggregate addAggAggregate() throws SdaiException {
// 			int aggMemberCount = aggregates[attrDepth].getMemberCount() + 1;
			return aggregates[attrDepth].addAggregateByIndex(++aggIndexes[attrDepth],
																attrSelect[attrDepth]);
		}
	}

	private static class Creator {
		private Creator next;

		protected Creator() {
			next = null;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException { }
	}

	protected static class SdaiContextCr extends Creator {

		private final SdaiContext context;

		protected SdaiContextCr(SdaiContext context) {
			this.context = context;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			context.working_model.getRepository().getSession().setSdaiContext(context);
		}
	}

	protected static class EntityInstInstCr extends Creator {
		private EEntity instance;

		protected EntityInstInstCr(EEntity instance) {
			this.instance = instance;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentInstanceId = "";
			instanceWriter.currentInstance = instance;
		}

		public String toString() {
			return "instance: " + instance;
		}

	}

	protected static class EntityInstIdCr extends Creator {
		private String idString;

		protected EntityInstIdCr(String idString) {
			this.idString = idString;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentInstanceId = idString;
			instanceWriter.currentInstance = (EEntity)instanceWriter.createdInstances.get(idString);
		}

		public String toString() {
			return "idString: " + idString;
		}

	}

	protected static class AttributeInstCr extends Creator {
		private EAttribute attribute;

		protected AttributeInstCr(EAttribute attribute) {
			this.attribute = attribute;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentAttribute = attribute;
			instanceWriter.attrDepth = 0;
			instanceWriter.attrSelectTop[0] = 0;
			if(instanceWriter.attrSelect[0] == null) {
				instanceWriter.attrSelect[0] = new EDefined_type[STACK_DEPTH];
			}
			instanceWriter.attrSelect[0][0] = null;
		}

	}

	protected static class AggregateAttCr extends Creator {
		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.aggregates[instanceWriter.attrDepth + 1] =
				instanceWriter.currentInstance.createAggregate(instanceWriter.currentAttribute,
												instanceWriter.attrSelect[instanceWriter.attrDepth]);
			instanceWriter.aggIndexes[instanceWriter.attrDepth + 1] = 0;
			instanceWriter.attrDepth++;
			instanceWriter.attrSelectTop[instanceWriter.attrDepth] = 0;
			if(instanceWriter.attrSelect[instanceWriter.attrDepth] == null) {
				instanceWriter.attrSelect[instanceWriter.attrDepth] = new EDefined_type[STACK_DEPTH];
			}
			instanceWriter.attrSelect[instanceWriter.attrDepth][0] = null;
			instanceWriter.setAggType();
		}

	}

	protected static class AggregateAttEn extends Creator {
		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.attrDepth--;
		}

	}

	protected static class AggregateAggCr extends Creator {
		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.aggregates[instanceWriter.attrDepth + 1] =
				instanceWriter.aggMemberHnd.addAggAggregate();
			instanceWriter.aggIndexes[instanceWriter.attrDepth + 1] = 0;
			instanceWriter.attrDepth++;
			instanceWriter.attrSelectTop[instanceWriter.attrDepth] = 0;
			if(instanceWriter.attrSelect[instanceWriter.attrDepth] == null) {
				instanceWriter.attrSelect[instanceWriter.attrDepth] = new EDefined_type[STACK_DEPTH];
			}
			instanceWriter.attrSelect[instanceWriter.attrDepth][0] = null;
			instanceWriter.setAggType();
		}

	}

	protected static class AggregateAggEn extends Creator {
		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.attrDepth--;
			instanceWriter.setAggType();
		}

	}

	protected static class EntityRefAttCr extends Creator {
		private String refId;

		EntityRefAttCr(String refId) {
			this.refId = refId;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			EEntity referencedInstance = (EEntity)instanceWriter.createdInstances.get(refId);
			instanceWriter.currentInstance.set(instanceWriter.currentAttribute, referencedInstance,
											   instanceWriter.attrSelect[instanceWriter.attrDepth]);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "refId: " + refId;
		}

	}

	protected static class EntityRefAggCr extends Creator {
		private String refId;

		EntityRefAggCr(String refId) {
			this.refId = refId;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			EEntity referencedInstance = (EEntity)instanceWriter.createdInstances.get(refId);
			instanceWriter.aggMemberHnd.addAggMember(referencedInstance);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "refId: " + refId;
		}

	}

// 	private static class TypeLitCr extends Creator {
// 		private String typeNameLocal;
// 		private String typeSchemaNameLocal;

// 		TypeLitCr(String typeNameLocal, String typeSchemaNameLocal) {
// 			this.typeNameLocal = typeNameLocal;
// 			this.typeSchemaNameLocal = typeSchemaNameLocal;
// 		}

// 		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
// 			instanceWriter.typeName = typeNameLocal;
// 			instanceWriter.typeSchemaName = typeSchemaNameLocal;
// 		}

// 	}

	protected static class SelectLitCr extends Creator {
		private EDefined_type definedType;

		SelectLitCr(EDefined_type definedType) {
			this.definedType = definedType;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.attrSelect[instanceWriter.attrDepth]
				[instanceWriter.attrSelectTop[instanceWriter.attrDepth]] = definedType;
			instanceWriter.attrSelect[instanceWriter.attrDepth]
				[++instanceWriter.attrSelectTop[instanceWriter.attrDepth]] = null;
		}

		public String toString() {
			return "definedType: " + definedType;
		}

	}

	protected static class IntegerLitAttCr extends Creator {
		private int intVal;

		IntegerLitAttCr(int intVal) {
			this.intVal = intVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentInstance.set(instanceWriter.currentAttribute, intVal,
											   instanceWriter.attrSelect[instanceWriter.attrDepth]);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "intVal: " + intVal;
		}

	}

	protected static class IntegerLitAggCr extends Creator {
		private int intVal;

		IntegerLitAggCr(int intVal) {
			this.intVal = intVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.aggMemberHnd.addAggMember(intVal);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "intVal: " + intVal;
		}

	}

	protected static class RealLitAttCr extends Creator {
		private double doubleVal;

		protected RealLitAttCr(double doubleVal) {
			this.doubleVal = doubleVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentInstance.set(instanceWriter.currentAttribute, doubleVal,
											   instanceWriter.attrSelect[instanceWriter.attrDepth]);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "doubleVal: " + doubleVal;
		}

	}

	protected static class RealLitAggCr extends Creator {
		private double doubleVal;

		RealLitAggCr(double doubleVal) {
			this.doubleVal = doubleVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.aggMemberHnd.addAggMember(doubleVal);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "doubleVal: " + doubleVal;
		}

	}

	protected static class LogicalLitAttCr extends Creator {
		private int logicalVal;

		protected LogicalLitAttCr(int logicalVal) {
			this.logicalVal = logicalVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentInstance.set(instanceWriter.currentAttribute, logicalVal,
											   instanceWriter.attrSelect[instanceWriter.attrDepth]);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "logicalVal: " + logicalVal;
		}

	}

	protected static class LogicalLitAggCr extends Creator {
		private int logicalVal;

		LogicalLitAggCr(int logicalVal) {
			this.logicalVal = logicalVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.aggMemberHnd.addAggMember(logicalVal);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "logicalVal: " + logicalVal;
		}

	}

	protected static class StringLitAttCr extends Creator {
		private String stringVal;

		protected StringLitAttCr(String stringVal) {
			this.stringVal = stringVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentInstance.set(instanceWriter.currentAttribute, stringVal,
											   instanceWriter.attrSelect[instanceWriter.attrDepth]);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "stringVal: " + stringVal;
		}

	}

	protected static class StringLitAggCr extends Creator {
		private String stringVal;

		StringLitAggCr(String stringVal) {
			this.stringVal = stringVal;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.aggMemberHnd.addAggMember(stringVal);
// 			System.out.println(currentInstance);
		}

		public String toString() {
			return "stringVal: " + stringVal;
		}

	}

	protected static class EnumRefAttCr extends Creator {
		private int enumIndex;

		protected EnumRefAttCr(int enumIndex) {
			this.enumIndex = enumIndex;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.currentInstance.set(instanceWriter.currentAttribute, enumIndex,
											   instanceWriter.attrSelect[instanceWriter.attrDepth]);
		}

		public String toString() {
			return "enumIndex: " + enumIndex;
		}

	}

	protected static class EnumRefAggCr extends Creator {
		private int enumIndex;

		EnumRefAggCr(int enumIndex) {
			this.enumIndex = enumIndex;
		}

		protected void processCreate(InstanceWriter instanceWriter) throws SdaiException {
			instanceWriter.aggMemberHnd.addAggMember(enumIndex);
		}

		public String toString() {
			return "enumIndex: " + enumIndex;
		}

	}

} // InstanceWriter
