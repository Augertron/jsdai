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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import jsdai.dictionary.ANamed_type;
import jsdai.dictionary.EAggregation_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EData_type;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ENamed_type;
import jsdai.dictionary.ESchema_definition;
import jsdai.dictionary.ESelect_type;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.CEntityDefinition;
import jsdai.lang.EEntity;
import jsdai.lang.QueryResultSet;
import jsdai.lang.QuerySource;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiQuery;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;
import jsdai.mapping.ESchema_mapping;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * Created: Wed Nov 13 10:53:23 2002
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class LocalSdaiQuery extends SdaiQueryEngine implements SdaiQuery {

	private static final String MAPPING_URI = "mapping:";
	private static final int MAPPING_URI_LEN = JSDAI_URI_LEN + MAPPING_URI.length();
	protected static final Object allTypesToken =
		new Object() {
			public String toString() { return "- Unspecified -"; }
		};

	protected SdaiSession session;
	protected ASdaiModel domain;
	protected AEntity querySourceInstances;
	protected ESchema_definition defaultSchema;

	private Map parameters;

	private interface Creator {
		Constraint create();
	}

	private interface SCreator {
		Constraint create(ESchema_definition schema);
	}

	private interface MCreator {
		Constraint create(ASdaiModel mappingDomain, ESchema_definition schema);
	}

	// IMPORTANT! This is a sorted list synchronized with SdaiQueryEngine.completeConstraints
	private static final SCreator schemaConstrCreators[] = new SCreator[] {
		/*and      */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new And(); } 
			},

		/*fwd      */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new SchemaFwd(schema); }
			},

		/*intersect*/new SCreator() {
				public Constraint create(ESchema_definition schema) { return new Intersect(); }
			},

		/*inv      */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new SchemaInv(schema); }
			},

		/*items    */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new Items(); }
			},

		/*not      */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new Not(); }
			},

		/*or       */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new Or(); }
			},

		/*type     */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new SchemaType(schema); }
			},

		/*union    */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new Union(); }
			},

		/*val      */new SCreator() {
				public Constraint create(ESchema_definition schema) { return new SchemaVal(schema); }
			},
	};

	// IMPORTANT! This is a sorted list synchronized with SdaiQueryEngine.completeConstraints
	private static final MCreator mappingConstrCreators[] = new MCreator[] {
		/*and      */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new And();
				} 
			},

		/*fwd      */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new MappingFwd(mappingDomain, schema);
				}
			},

		/*intersect*/new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new Intersect();
				}
			},

		/*inv      */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new MappingInv(mappingDomain, schema);
				}
			},

		/*items    */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) { 
					return new Items();
				}
			},

		/*not      */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new Not();
				}
			},

		/*or       */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new Or();
				}
			},

		/*type     */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new MappingType(mappingDomain, schema);
				}
			},

		/*union    */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new Union();
				}
			},

		/*val      */new MCreator() {
				public Constraint create(ASdaiModel mappingDomain, ESchema_definition schema) {
					return new MappingVal(mappingDomain, schema);
				}
			},
	};

	// IMPORTANT! This is a sorted list
	private static final String limitedConstraints[] = new String[] {
		"and",
		"intersect",
		"items",
		"not",
		"or",
		"union",
	};

	// IMPORTANT! This is a sorted list
	private static final Creator limitedConstrCreators[] = new Creator[] {
		/*and      */new Creator() {
				public Constraint create() { return new And(); } 
			},

		/*intersect*/new Creator() {
				public Constraint create() { return new Intersect(); }
			},

		/*items    */new Creator() {
				public Constraint create() { return new Items(); }
			},

		/*not      */new Creator() {
				public Constraint create() { return new Not(); }
			},

		/*or       */new Creator() {
				public Constraint create() { return new Or(); }
			},

		/*union    */new Creator() {
				public Constraint create() { return new Union(); }
			},
	};

	// IMPORTANT! This is a sorted list
	private static final SdaiQueryEngine.Creator valConstraintCreators[] = new SdaiQueryEngine.Creator[] {
		/*and   */new SdaiQueryEngine.Creator() { public Constraint create() { return new SchemaValAnd(); } },
		/*eq    */new SdaiQueryEngine.Creator() { public Constraint create() { return new SchemaEq(); } },
		/*neq   */new SdaiQueryEngine.Creator() { public Constraint create() { return new SchemaNeq(); } },
		/*nregex*/new SdaiQueryEngine.Creator() { public Constraint create() { return new SchemaNregex(); } },
		/*or    */new SdaiQueryEngine.Creator() { public Constraint create() { return new SchemaValOr(); } },
		/*regex */new SdaiQueryEngine.Creator() { public Constraint create() { return new SchemaRegex(); } },
	};

	public LocalSdaiQuery(SdaiSession session) throws SdaiException {
		super(true);
		this.session = session;
	}

	public static SdaiQuery create(SdaiSession session, Element querySpec) throws SdaiException{
		LocalSdaiQuery lsq = new LocalSdaiQuery(session);
		lsq.parse(querySpec);
		return lsq;
	}

	public jsdai.query.SdaiQueryEngine.Creator getValConstraintCreator(int idx){
		return valConstraintCreators[idx];
	}

	// Implementation of jsdai.lang.SdaiQuery

// 	/**
// 	 * Describe <code>execute</code> method here.
// 	 *
// 	 * @exception SdaiException if an error occurs
// 	 */
// 	public void execute() throws SdaiException {
// 		throw new SdaiException(SdaiException.FN_NAVL);
// 	}



	protected Context newContext(boolean executeContext) throws SdaiException {
		return new LocalContext(this, executeContext);
	}

	protected Object createNamespaceHandler(String namespaceURI, Element queryElem) throws SdaiException {
		if(namespaceURI.equals(QUERY_V1_URL)) {
			return new SchemaDefaultFactory(this);
		} else if(namespaceURI.equals(QUERY_V1_1_URL)) {
			return new LimitedFactory();
		} else if(namespaceURI.startsWith(JSDAI_URI)) {
			if(namespaceURI.startsWith(SCHEMA_URI, JSDAI_URI_LEN)) {
				ESchema_definition schema = findSchemaDefinition(namespaceURI.substring(SCHEMA_URI_LEN));
				return new SchemaCompleteFactory(schema);
			} else if(namespaceURI.startsWith(MAPPING_URI, JSDAI_URI_LEN)) {
				ASdaiModel mappingDomain = new ASdaiModel();
				ESchema_definition schema = 
					findMappingDomAndSrcSchema(namespaceURI.substring(MAPPING_URI_LEN), mappingDomain);
				return new MappingCompleteFactory(mappingDomain, schema);
			} else if(namespaceURI.startsWith(QUERY_LIB_URI, JSDAI_URI_LEN)) {
				String queryLibId = namespaceURI.substring(QUERY_LIB_URI_LEN);
				if(!localQueryLibsPreparsed && queryLibId.charAt(0) == '#') {
					preparseLocalQueryLibs(queryElem);
				}
				QueryLib queryLib = findQueryLib(queryLibId);
				return new QueryLibCompleteFactory(queryLib);
			}
		}
		throw new SdaiException(SdaiException.FN_NAVL,
								"Unsupported namespace URI in query element: " + namespaceURI);
	}

	static protected String displayName(Object type) throws SdaiException {
		return (type instanceof EEntity_definition) ?
			((EEntity_definition)type).getName(null) : type.toString();
	}

	static protected EAttribute getAttribute(ESchema_definition schema, String entityName,
											 String attributeName, Node containerNode,
											 LocalContext localContext,
											 boolean meta) throws SdaiException {
		// Getting attribute
		EEntity_definition entity;
		if(entityName != null) {
			entity = JsdaiLangAccessorLocal.findSchemaEntityDefinition(schema, entityName);
			Iterator ctsIter = localContext.currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				Iterator currTypeIter = cType.iterator();
				while(currTypeIter.hasNext()) {
					Object currTypeObject = currTypeIter.next();
					EEntity_definition currType = null;
					if(currTypeObject == allTypesToken
					   || ((currType = LocalContext.getEntityType(currTypeObject, meta)) != entity
						   && !((CEntityDefinition)currType).isSubtypeOf(entity))) {
						throw
							new SdaiException(SdaiException.FN_NAVL,
											  formatMessage(containerNode,
															"Incompatible types",
															displayName(currType != null ?
																		currType :
																		currTypeObject) + " and " +
															entity.getName(null)));
					}
				}
			}
			return JsdaiLangAccessorLocal.findAttribute(entity, attributeName);
		} else {
			EAttribute attribute = null;
			Iterator ctsIter = localContext.currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				Iterator currTypeIter = cType.iterator();
				while(currTypeIter.hasNext()) {
					Object currTypeObject = currTypeIter.next();
					if(currTypeObject == allTypesToken) {
						throw
							new SdaiException(SdaiException.FN_NAVL,
											  formatMessage(containerNode, "No instances selected", null));
					}
					EAttribute currAttr = JsdaiLangAccessorLocal
						.findAttribute(LocalContext.getEntityType(currTypeObject, meta), attributeName);
					if(attribute != null && currAttr != attribute) {
						throw
							new SdaiException(SdaiException.FN_NAVL,
											  formatMessage(containerNode, "Ambiguous attributes",
															attribute.toString() + " and " +
															currAttr.toString()));
					}
					attribute = currAttr;
				}
			}
			return attribute;
		}
	}

	// FIXME!!! Needs aggregation type support - done?
	protected void getFinalDomainTypes(EEntity domain, Set finalTypes) throws SdaiException {
		if(domain instanceof EDefined_type) {
			EEntity dtDomain = ((EDefined_type)domain).getDomain(null);
			getFinalDomainTypes(dtDomain, finalTypes);
		} else if(domain instanceof ESelect_type) {
			ANamed_type selections = ((ESelect_type)domain).getSelections(null, session.getSdaiContext());
			SdaiIterator selIter = selections.createIterator();
			while(selIter.next()) {
				ENamed_type selection = selections.getCurrentMember(selIter);
				getFinalDomainTypes(selection, finalTypes);
			}
		} else if(domain instanceof EAggregation_type) {
			EData_type abType = ((EAggregation_type)domain).getElement_type(null);
			getFinalDomainTypes(abType, finalTypes);
		} else {
			finalTypes.add(domain);
		}

	}

	protected void setDefaultSchema(String schemaName) throws SdaiException {
		defaultSchema = findSchemaDefinition(schemaName);
	}

	private ESchema_definition findSchemaDefinition(String schemaName) throws SdaiException {
		SdaiModel schemaModel = session.getSystemRepository()
			.findSdaiModel(schemaName.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX);
		if(schemaModel == null) {
			throw new SdaiException(SdaiException.FN_NAVL,
									"Unknown schema name in query element: " + schemaName);
		}
		if(schemaModel.getMode() == SdaiModel.NO_ACCESS) {
			schemaModel.startReadOnlyAccess();
		}
		return (ESchema_definition)schemaModel.getInstances(ESchema_definition.class).getByIndexEntity(1);
	}

	protected QueryLibProvider getQueryLibProvider(int scope) throws SdaiException {
		QueryLibProvider queryLibProvider;
		switch(scope) {
		case SdaiQueryEngine.SCOPE_GLOBAL: {
			//FIXME: implement
			throw new SdaiException(SdaiException.FN_NAVL, "Global scope in not implemented yet");
		}
		case SdaiQueryEngine.SCOPE_SESSION: {
			synchronized(session) {
				queryLibProvider = JsdaiLangAccessorLocal.getQueryLibProvider(session);
				if(queryLibProvider == null) {
					queryLibProvider = new SimpleQueryLibProvider();
					JsdaiLangAccessorLocal.setQueryLibProvider(session, queryLibProvider);
				}
			}
			break;
		}
		case SdaiQueryEngine.SCOPE_TRANSACTION: {
			SdaiTransaction transaction = session.getActiveTransaction();
			synchronized(transaction) {
				queryLibProvider = JsdaiLangAccessorLocal.getQueryLibProvider(transaction);
				if(queryLibProvider == null) {
					queryLibProvider = new SimpleQueryLibProvider();
					JsdaiLangAccessorLocal.setQueryLibProvider(transaction, queryLibProvider);
				}
			}
			break;
		}
		case SdaiQueryEngine.SCOPE_LOCAL: {
			if(localQueryLibProvider == null) {
				localQueryLibProvider = new SimpleQueryLibProvider();
			}
			queryLibProvider = localQueryLibProvider;
			break;
		}
		default:
			throw new SdaiException(SdaiException.SY_ERR, 
									"Unrecognized query lib provider scope: " + scope);
		}
		return queryLibProvider;
	}

	protected void queryScope(int scope) {
		// FIXME: The local query scope is ignored at the moment
	}

	private QueryLib findQueryLib(String queryLibId) throws SdaiException {
		QueryLib queryLib;
		if(localQueryLibProvider != null) {
			queryLib = localQueryLibProvider.findQueryLib(queryLibId);
			if(queryLib != null) {
				return queryLib;
			}
		} else {
			QueryLibProvider queryLibProvider;
			SdaiTransaction transaction = session.getActiveTransaction();
			if((queryLibProvider = JsdaiLangAccessorLocal.getQueryLibProvider(transaction)) != null) {
				queryLib = queryLibProvider.findQueryLib(queryLibId);
				if(queryLib != null) {
					return queryLib;
				}
			} else if((queryLibProvider = JsdaiLangAccessorLocal.getQueryLibProvider(session)) != null) {
				queryLib = queryLibProvider.findQueryLib(queryLibId);
				if(queryLib != null) {
					return queryLib;
				}
			}
			//FIXME: add search in global scope
		}
		throw new SdaiException(SdaiException.FN_NAVL,
								"Query library was not found: " + queryLibId);
	}

	private ESchema_definition findMappingDomAndSrcSchema(String mappingName,
														  ASdaiModel mappingDomain) throws SdaiException {
		SdaiModel mappingModel = session.getSystemRepository()
			.findSdaiModel(mappingName.toUpperCase() + SdaiSession.MAPPING_NAME_SUFIX);
		if(mappingModel == null) {
			throw new SdaiException(SdaiException.FN_NAVL,
									"Unknown mapping name in query element: " + mappingName);
		}
		if(mappingModel.getMode() == SdaiModel.NO_ACCESS) {
			mappingModel.startReadOnlyAccess();
		}
		mappingDomain.addByIndex(1, mappingModel);
		ESchema_mapping schemaMapping = (ESchema_mapping)
			mappingModel.getInstances(ESchema_mapping.class).getByIndexEntity(1);
		return schemaMapping.getSource(null);
	}

	// moved from SdaiQueryEngine.java :

	public void setQuerySource(QuerySource qs) throws SdaiException {
		domain = qs.getQuerySourceDomain();
		querySourceInstances = qs.getQuerySourceInstances();
	}

	public void setDomain(ASdaiModel domain) throws SdaiException {
		this.domain = domain;
	}

	public void execute() throws SdaiException {
		try {
			for(Result result = resultHead; result != null; result = result.next) {
				//			schema = getSchemaFromName(result.getSchemaName());
				result.execute(newContext(true));
			}
		} finally {
			if(parameters != null) {
				parameters.clear();
			}
		}
	}

 	public void execute(QuerySource qs) throws SdaiException {
		// preset domain in which to perform a query
		// deal with schema name during parsing;
		domain = qs.getQuerySourceDomain();
		querySourceInstances = qs.getQuerySourceInstances();
		try {
			for(Result result = resultHead; result != null; result = result.next) {
				//			schema = getSchemaFromName(result.getSchemaName());
				result.execute(newContext(true));
			}
		} finally {
			if(parameters != null) {
				parameters.clear();
			}
		}
 	}

	public void execute(QuerySource qs, ASdaiModel domain) throws SdaiException {
		this.domain = domain;
		// this.domain = qs.getDomain(); // need to set domain somehow
		try {
			for(Result result = resultHead; result != null; result = result.next) {
				// need to set invDomain
				result.execute(newContext(true));
			}
		} finally {
			if(parameters != null) {
				parameters.clear();
			}
		}
	}

 	public void setParameter(String name, Object value) throws SdaiException {
		if(name == null || name.length() == 0) {
			throw new SdaiException(SdaiException.SY_ERR, "Parameter name is missing");
		}
		if(allowedParams == null
				|| !(allowedParams.isEmpty() || allowedParams.contains(name))) {
			throw new SdaiException(SdaiException.SY_ERR, "Illegal parameter name");
		}
		if(parameters == null) {
			parameters = new HashMap();
		}
		parameters.put(name, value.toString());
 	}

	public AEntity getResult(String name) throws SdaiException {
		if(resultMap == null) {
			prepareResultMap();
		}

		// rewrite List to AEntity
		Context resultContext = ((Result)resultMap.get(name)).getResultContext();
		List lInst = ((LocalContext)resultContext).getResultInstances();
		AEntity aInst = new AEntity();
		ListIterator lIter = lInst.listIterator();
		
 		while(lIter.hasNext()){
 			aInst.addUnordered((EEntity)lIter.next());
 		}

 		return aInst;
	}

	public AEntity getResult(int index) throws SdaiException {
		if(results == null) {
			prepareResultArrays();
		}
		//return results[index].resultInstances;
		//FIXME: rewrite list to AEntity
		Context resultContext = results[index - 1].getResultContext();
		List lInst = ((LocalContext)resultContext).getResultInstances();
		AEntity aInst = new AEntity();
		ListIterator lIter = lInst.listIterator();
		
 		while(lIter.hasNext()){
 			aInst.addUnordered((EEntity)lIter.next());
 		}

 		return aInst;
	}

	public QueryResultSet getResultSet(String name) throws SdaiException {
		if(resultMap == null) {
			prepareResultMap();
		}

		Context resultContext = ((Result)resultMap.get(name)).getResultContext();
		return new LocalQueryResultSet((LocalContext)resultContext);
	}

	public QueryResultSet getResultSet(int index) throws SdaiException {
		if(results == null) {
			prepareResultArrays();
		}
		Context resultContext = results[index - 1].getResultContext();
		return new LocalQueryResultSet((LocalContext)resultContext);
	}

// 	public List getResultList(String name) throws SdaiException {
// 		if(resultMap == null) {
// 			prepareResultMap();
// 		}

// 		// rewrite List to AEntity
// 		Context resultContext = ((Result)resultMap.get(name)).getResultContext();
// 		List lInst = ((LocalContext)resultContext).getResultInstances();
// 		return lInst;
// 	}

// 	public List getResultList(int ind) throws SdaiException {
// 		if(results == null) {
// 			prepareResultArrays();
// 		}

// 		Context resultContext = results[ind].getResultContext();
// 		List lInst = ((LocalContext)resultContext).getResultInstances();
// 		return lInst;
// 	}

	static String getRealExpected(Context context, String expected, boolean paramExpected) {
		String realExpected = expected;
		if(paramExpected) {
			final Map parameters = ((LocalSdaiQuery)context.query).parameters;
			Object realExpectedObj = parameters != null ? parameters.get(expected) : null;
			if(realExpectedObj != null) {
				realExpected = realExpectedObj.toString();
			}
		}
		return realExpected;
	}

    // copied from SdaiQueryEngine
	static protected class SchemaEq extends Eq {
		protected void execute(Context context) throws SdaiException {
			String realExpected = getRealExpected(context, expected, paramExpected);
			List valueList = context.getValueList();
			ListIterator valueListIter = valueList.listIterator();
			while(valueListIter.hasNext()) {
				Object value = valueListIter.next();
				if(value == null) continue;
				boolean matches = false;
				if(value instanceof Object[]) {
					Object[] values = (Object[])value;
					for(int i = 0; i < values.length; i++) {
						if(values[i].equals(realExpected)) {
							matches = true;
							break;
						}
					}
				} else {
					matches = value.equals(realExpected);
				}
				if(!matches) {
					valueListIter.set(null);
				}
			}
		}
	}

	// Spec needs updating
	static protected class SchemaNeq extends Neq {
		protected void execute(Context context) throws SdaiException {
			String realExpected = getRealExpected(context, expected, paramExpected);
			List valueList = context.getValueList();
			ListIterator valueListIter = valueList.listIterator();
			while(valueListIter.hasNext()) {
				Object value = valueListIter.next();
				if(value == null) continue;
				boolean matches = true;
				if(value instanceof Object[]) {
					Object[] values = (Object[])value;
					for(int i = 0; i < values.length; i++) {
						if(values[i].equals(realExpected)) {
							matches = false;
							break;
						}
					}
				} else {
					matches = !value.equals(realExpected);
				}
				if(!matches) {
					valueListIter.set(null);
				}
			}
		}
	}

	static protected class SchemaRegex extends Regex {
		protected Pattern expectedPattern;

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			super.parse(containerNode, parent, context);
			if(!paramExpected) {
				expectedPattern = Pattern.compile(expected);
			} else {
				expectedPattern = null;
			}
		}

		protected void execute(Context context) throws SdaiException {
			Pattern realExpPattern;
			if(paramExpected) {
				String realExpected = getRealExpected(context, expected, paramExpected);
				realExpPattern = Pattern.compile(realExpected);
			} else {
				realExpPattern = expectedPattern;
			}
			List valueList = context.getValueList();
			ListIterator valueListIter = valueList.listIterator();
			while(valueListIter.hasNext()) {
				Object value = valueListIter.next();
				if(value == null) continue;
				boolean matches = false;
				if(value instanceof Object[]) {
					Object[] values = (Object[])value;
					for(int i = 0; i < values.length; i++) {
						if(values[i] instanceof CharSequence 
						   && realExpPattern.matcher((CharSequence)values[i]).find(0)) {
							matches = true;
							break;
						}
					}
				} else {
					matches = 
						value instanceof CharSequence 
						&& realExpPattern.matcher((CharSequence)value).find(0);
				}
				if(!matches) {
					valueListIter.set(null);
				}
			}
		}
	}

	static protected class SchemaNregex extends Nregex {
		protected Pattern expectedPattern;

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			super.parse(containerNode, parent, context);
			if(!paramExpected) {
				expectedPattern = Pattern.compile(expected);
			} else {
				expectedPattern = null;
			}
		}

		protected void execute(Context context) throws SdaiException {
			Pattern realExpPattern;
			if(paramExpected) {
				String realExpected = getRealExpected(context, expected, paramExpected);
				realExpPattern = Pattern.compile(realExpected);
			} else {
				realExpPattern = expectedPattern;
			}
			List valueList = context.getValueList();
			ListIterator valueListIter = valueList.listIterator();
			while(valueListIter.hasNext()) {
				Object value = valueListIter.next();
				if(value == null) continue;
				boolean matches = true;
				if(value instanceof Object[]) {
					Object[] values = (Object[])value;
					for(int i = 0; i < values.length; i++) {
						if(values[i] instanceof CharSequence 
						   && realExpPattern.matcher((CharSequence)values[i]).find(0)) {
							matches = false;
							break;
						}
					}
				} else {
					matches = 
						!(value instanceof CharSequence 
						  && realExpPattern.matcher((CharSequence)value).find(0));
				}
				if(!matches) {
					valueListIter.set(null);
				}
			}
		}
	}

	static protected class SchemaValAnd extends ValAnd {
		protected void execute(Context context) throws SdaiException {
			List valueList = context.getValueList();
			for(Constraint constraint = constraints; constraint != null; constraint = constraint.next) {
				Context childContext = context.dupVal();
				constraint.execute(childContext);
				ListIterator valueListIter = valueList.listIterator();
				ListIterator childValLstIter = childContext.getValueList().listIterator();
				while(valueListIter.hasNext()) {
					Object value = valueListIter.next();
					Object childValue = childValLstIter.next();
					if(childValue == null) {
						valueListIter.set(null);
					}
				}
			}
		}
	}

// 	static protected class ValNot extends Constraint {
// 		protected void parse(Node containerNode, ConstraintContainer parent,
// 							 Context context) throws SdaiException {
// 			Node sibling = containerNode.getNextSibling();
// 			while(sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE) {
// 				sibling = sibling.getNextSibling();
// 			}
// 			if(sibling != null && sibling.getNamespaceURI().equals(QUERY_V1_URL)
// 			   && sibling.getLocalName().equals("not")) {
// 				throw new SdaiException(SdaiException.FN_NAVL,
// 										formatMessage(containerNode, "val has too many children", null));
// 			}
// 		}
// 	}

	static protected class SchemaValOr extends ValOr {
		protected void execute(Context context) throws SdaiException {
			List valueList = context.getValueList();
			List resValList = new ArrayList(Collections.nCopies(valueList.size(), null));
			for(Constraint constraint = constraints; constraint != null; constraint = constraint.next) {
				Context childContext = context.dupVal();
				constraint.execute(childContext);
				ListIterator valueListIter = valueList.listIterator();
				ListIterator childValLstIter = childContext.getValueList().listIterator();
				ListIterator resValIter = resValList.listIterator();
				while(valueListIter.hasNext()) {
					Object value = valueListIter.next();
					Object childValue = childValLstIter.next();
					resValIter.next();
					if(childValue != null) {
						resValIter.set(value);
					}
				}
			}
			Collections.copy(valueList, resValList);
		}
	}

	static private class SchemaDefaultFactory implements ConstraintFactory {
		LocalSdaiQuery query;

		SchemaDefaultFactory(LocalSdaiQuery query) {
			this.query = query;
		}

		public Constraint newConstraint(Node constraint) throws SdaiException {
			int constraintIdx = Arrays.binarySearch(completeConstraints, constraint.getLocalName());
			if(constraintIdx < 0) {
				throw new SdaiException(SdaiException.FN_NAVL,
										formatMessage(constraint, "Unrecognized constraint", null));
			}
			return schemaConstrCreators[constraintIdx].create(query.defaultSchema);
		}
	}

	static private class SchemaCompleteFactory implements ConstraintFactory {
		ESchema_definition schema;

		SchemaCompleteFactory(ESchema_definition schema) {
			this.schema = schema;
		}

		public Constraint newConstraint(Node constraint) throws SdaiException {
			int constraintIdx = Arrays.binarySearch(completeConstraints, constraint.getLocalName());
			if(constraintIdx < 0) {
				throw new SdaiException(SdaiException.FN_NAVL,
										formatMessage(constraint, "Unrecognized constraint", null));
			}
			return schemaConstrCreators[constraintIdx].create(schema);
		}
	}

	static private class MappingCompleteFactory implements ConstraintFactory {
		ASdaiModel mappingDomain;
		ESchema_definition schema;

		MappingCompleteFactory(ASdaiModel mappingDomain, ESchema_definition schema) {
			this.mappingDomain = mappingDomain;
			this.schema = schema;
		}

		public Constraint newConstraint(Node constraint) throws SdaiException {
			int constraintIdx = Arrays.binarySearch(completeConstraints, constraint.getLocalName());
			if(constraintIdx < 0) {
				throw new SdaiException(SdaiException.FN_NAVL,
										formatMessage(constraint, "Unrecognized constraint", null));
			}
			return mappingConstrCreators[constraintIdx].create(mappingDomain, schema);
		}
	}

	static private class LimitedFactory implements ConstraintFactory {

		public Constraint newConstraint(Node constraint) throws SdaiException {
			int constraintIdx = Arrays.binarySearch(limitedConstraints, constraint.getLocalName());
			if(constraintIdx < 0) {
				throw new SdaiException(SdaiException.FN_NAVL,
										formatMessage(constraint, "Unrecognized constraint", null));
			}
			return limitedConstrCreators[constraintIdx].create();
		}
	}

} // LocalSdaiQuery

/*
  Local Variables:
  compile-command: "ant -emacs -find build-local.xml local.sdai.query.test"
  End:
*/
