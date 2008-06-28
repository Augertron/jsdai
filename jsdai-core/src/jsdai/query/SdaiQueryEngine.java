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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import jsdai.lang.SdaiException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * Created: Tue Nov 12 16:25:14 2002
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class SdaiQueryEngine {

	public static final String JSDAI_URI = "jsdai:";
	public static final int JSDAI_URI_LEN = JSDAI_URI.length();
	public static final String SCHEMA_URI = "schema:";
	public static final int SCHEMA_URI_LEN = JSDAI_URI_LEN + SCHEMA_URI.length();
	public static final String QUERY_LIB_URI = "query-lib:";
	public static final int QUERY_LIB_URI_LEN = JSDAI_URI_LEN + QUERY_LIB_URI.length();
	public static final String QUERY_V1_URL = "http://www.lksoft.com/SDAI/Query/V1";
	public static final String QUERY_V1_1_URL = "http://www.lksoft.com/SDAI/Query/V1.1";
	public static final String QUERY_ELEM = "query";
	public static final String QUERY_LIB_ELEM = "query-lib";
	public static final String DOMAIN_ELEM = "domain";
	public static final String RESULT_ELEM = "result";
	public static final String QUERY_ELEMENT_PREFIXES_ATTR = "query-element-prefixes";
	public static final String ID_ATTR = "id";
	public static final String IDREF_ATTR = "idref";
	public static final String SCOPE_ATTR = "scope";
	public static final String REMOVE_ATTR = "remove";
	public static final String DEFAULT_PREFIX = "#default";
	public static final String XMLNS_PREFIX = "xmlns";
	public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";

	public static final int SCOPE_GLOBAL = 0;
	public static final int SCOPE_SESSION = 1;
	public static final int SCOPE_TRANSACTION = 2;
	public static final int SCOPE_LOCAL = 3;

	private static final String SCOPE_VALUES = "globalsessiontransaction";
	private static final int SCOPE_GLOBAL_IDX = 0;
	private static final int SCOPE_SESSION_IDX = SCOPE_GLOBAL_IDX + 6;
	private static final int SCOPE_TRANSACTION_IDX = SCOPE_SESSION_IDX + 7;

	static {
		if(SCOPE_GLOBAL_IDX != 0
		   || SCOPE_SESSION_IDX != SCOPE_GLOBAL_IDX + "global".length()
		   || SCOPE_TRANSACTION_IDX != SCOPE_SESSION_IDX + "session".length()) {
			throw new ExceptionInInitializerError("Wrong constants SCOPE_GLOBAL_IDX or " + 
												  "SCOPE_SESSION_IDX or SCOPE_TRANSACTION_IDX");
		}
	}

	// Model regularConstraints list
	// IMPORTANT! This is a sorted list
// 	private static Object regularConstraints[][] = new Object[][] {
// 		new Object[] { "and",       And.class},
// 		new Object[] { "fwd",       Fwd.class},
// 		new Object[] { "intersect", Intersect.class},
// 		new Object[] { "inv",       Inv.class},
// 		new Object[] { "not",       Not.class},
// 		new Object[] { "or",        Or.class},
// 		new Object[] { "type",      Type.class},
// 		new Object[] { "union",     Union.class},
// 		new Object[] { "val",       Val.class},
// 	};

	// IMPORTANT! This is a sorted list
// 	private static Object valConstraints[][] = new Object[][] {
// 		new Object[] { "and",       ValAnd.class},
// 		new Object[] { "eq",        Eq.class},
// 		new Object[] { "neq",       Neq.class},
// 		new Object[] { "or",        ValOr.class},
// 	};
	public interface Creator {
		Constraint create();
	}

	private interface LCreator {
		Constraint create(QueryLib queryLib);
	}

	// IMPORTANT! This is a sorted list
	protected static final String completeConstraints[] = new String[] {
		"and",
		"fwd",
		"intersect",
		"inv",
		"items",
		"not",
		"or",
		"type",
		"union",
		"val",
	};

	// IMPORTANT! This is a sorted list
	private static final String valConstraints[] = new String[] {
		"and",
		"eq",
		"neq",
		"nregex",
		"or",
		"regex",
	};

	public abstract Creator getValConstraintCreator(int idx);

	

	protected final ConstraintFactory valConstraintFactory = new ConstraintFactory() {
			public Constraint newConstraint(Node constraint) throws SdaiException {
				if(!namespaceMap.containsKey(constraint.getNamespaceURI())) {
					throw new SdaiException(SdaiException.FN_NAVL,
											formatMessage(constraint, "Unknown namespace: ",
														  constraint.getNamespaceURI()));
				}
				int constraintIdx = Arrays.binarySearch(valConstraints, constraint.getLocalName());
				if(constraintIdx < 0) {
					throw new SdaiException(SdaiException.FN_NAVL,
											formatMessage(constraint, "Unrecognized constraint", null));
				}
				//return valConstraintCreators[constraintIdx].create();
				return getValConstraintCreator(constraintIdx).create();
			}
		};

	// IMPORTANT! This is a sorted list synchronized with SdaiQueryEngine.completeConstraints
	private static final LCreator queryLibConstrCreators[] = new LCreator[] {
		/*and      */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new And(); } 
			},

		/*fwd      */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new QueryLibFwd(queryLib); }
			},

		/*intersect*/new LCreator() {
				public Constraint create(QueryLib queryLib) { return new Intersect(); }
			},

		/*inv      */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new QueryLibInv(queryLib); }
			},

		/*items    */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new Items(); }
			},

		/*not      */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new Not(); }
			},

		/*or       */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new Or(); }
			},

		/*type     */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new QueryLibType(queryLib); }
			},

		/*union    */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new Union(); }
			},

		/*val      */new LCreator() {
				public Constraint create(QueryLib queryLib) { return new QueryLibVal(queryLib); }
			},
	};

	protected Result resultHead;
	protected int resultCount;
	protected Result results[];
	protected String resultNames[];
	protected Map resultMap;
	private Map namespaceMap;
	protected QueryLibProvider localQueryLibProvider;
	protected boolean localQueryLibsPreparsed;
	public final boolean allowParams;
	protected Set allowedParams;

	protected SdaiQueryEngine(boolean allowParams) {
		clearResults();
		this.allowParams = allowParams;
		this.allowedParams = null;
	}

	protected void parse(Element queryElem) throws SdaiException {
		clearResults();
		Result resultTail = null;
		if(namespaceMap == null) {
			namespaceMap = new HashMap();
		} else {
			namespaceMap.clear();
		}
		
		StringTokenizer prefixesTokenizer = 
			new StringTokenizer(queryElem.getAttributeNS(QUERY_V1_1_URL, QUERY_ELEMENT_PREFIXES_ATTR));
		if(!prefixesTokenizer.hasMoreTokens() 
		   && QUERY_V1_1_URL.equals(queryElem.getNamespaceURI())) {
			prefixesTokenizer = 
				new StringTokenizer(queryElem.getAttributeNS(null, QUERY_ELEMENT_PREFIXES_ATTR));
		}
		if(prefixesTokenizer.hasMoreTokens()) {
			namespaceMap.put(QUERY_V1_1_URL, createNamespaceHandler(QUERY_V1_1_URL, queryElem));
		} else {
			namespaceMap.put(QUERY_V1_URL, createNamespaceHandler(QUERY_V1_URL, queryElem));
		}
		while(prefixesTokenizer.hasMoreTokens()) {
			String prefix = prefixesTokenizer.nextToken();
			if(prefix.equals(DEFAULT_PREFIX)) {
				prefix = XMLNS_PREFIX;
			}
			String prefixURI = queryElem.getAttributeNS(XMLNS_URI, prefix);
			if(prefixURI.length() > 0) {
				namespaceMap.put(prefixURI, createNamespaceHandler(prefixURI, queryElem));
			}
		}
// 		if(queryElem.getNamespaceURI().equals(QUERY_V1_URL)
// 		   && queryElem.getLocalName().equals(QUERY_ELEM)) {
		if(isNamespaceHandler(queryElem.getNamespaceURI())) {
			String scopeString = queryElem.getAttributeNS(null, SCOPE_ATTR);
			int scope;
			if(scopeString.length() == 0) {
				scope = SCOPE_TRANSACTION;
			} else {
				switch(SCOPE_VALUES.indexOf(scopeString)) {
				case SCOPE_GLOBAL_IDX: {
					scope = SCOPE_GLOBAL;
					break;
				}
				case SCOPE_SESSION_IDX: {
					scope = SCOPE_SESSION;
					break;
				}
				case SCOPE_TRANSACTION_IDX: {
					scope = SCOPE_TRANSACTION;
					break;
				}
				default:
					throw new SdaiException(SdaiException.FN_NAVL,
											formatMessage(queryElem, 
														  "Unrecognized scope value", 
														  scopeString));
				}
			}
			if(queryElem.getLocalName().equals(QUERY_ELEM)) {
				queryScope(scope);
				boolean isQueryEmpty = true;
				for(Node queryChild = queryElem.getFirstChild(); queryChild != null;
					queryChild = queryChild.getNextSibling()) {
					if(queryChild.getNodeType() != Node.ELEMENT_NODE) continue;
					if(!isNamespaceHandler(queryChild.getNamespaceURI())) {
						throw new SdaiException(SdaiException.FN_NAVL,
												formatMessage(queryChild, "Unknown namespace: ",
															  queryChild.getNamespaceURI()));
					}
					if(queryChild.getLocalName().equals(DOMAIN_ELEM)) {
					} else if(queryChild.getLocalName().equals(RESULT_ELEM)) {
						Result result = newResult();
						result.parse(queryChild, null, newContext(false));
						resultTail = addResult(result, resultTail);
					} else {
						throw new SdaiException(SdaiException.FN_NAVL,
												formatMessage(queryChild, "Unrecognized element", null));
					}
					isQueryEmpty = false;
				}
				if(isQueryEmpty) {
					throw new SdaiException(SdaiException.FN_NAVL, "Query is empty");
				}
			} else if(queryElem.getLocalName().equals(QUERY_LIB_ELEM)) {
				Attr queryLibAttr = queryElem.getAttributeNodeNS(null, IDREF_ATTR);
				if(queryLibAttr != null ) {
					if(queryElem.getAttributeNodeNS(null, ID_ATTR) != null) {
						throw new SdaiException(SdaiException.FN_NAVL,
												formatMessage(queryElem, 
															  "id can not be specified if idref is used", 
															  null));
					}
					//FIXME: handle remove attribute
					//queryElem.getAttributeNS(null, REMOVE_ATTR);
				} else {
					queryLibAttr = queryElem.getAttributeNodeNS(null, ID_ATTR);
					if(queryLibAttr != null) {
						QueryLibProvider queryLibProvider = getQueryLibProvider(scope);
						QueryLib queryLib = 
							queryLibProvider.createQueryLib(queryLibAttr.getValue(), this, queryElem);
					} else {
						throw new SdaiException(SdaiException.FN_NAVL,
												formatMessage(queryElem, 
															  "Missing id attribute", null));
					}
				}
			} else {
				throw new SdaiException(SdaiException.FN_NAVL, 
										"Query or query-lib element expected as top node: " +
										queryElem.getNamespaceURI() + " " + queryElem.getLocalName());
			}
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, 
									"Unrecognized namespace of the top element: " +
									queryElem.getNamespaceURI() + " " + queryElem.getLocalName());
		}
	}

	public String[] getResultNames() throws SdaiException {
		if(results == null) {
			prepareResultArrays();
		}
		return resultNames;
	}

	public final void addAllowedParameter(String paramName) {
		if(allowedParams == null) {
			allowedParams = new HashSet();
		}
		allowedParams.add(paramName);
	}

	protected final void prepareResultMap() {
		resultMap = new HashMap();
		for(Result result = resultHead; result != null; result = result.next) {
			resultMap.put(result.name, result);
		}
	}

	protected final void prepareResultArrays() {
		results = new Result[resultCount];
		resultNames = new String[resultCount];
		int resultIdx = 0;
		for(Result result = resultHead; result != null; result = result.next) {
			results[resultIdx] = result;
			resultNames[resultIdx] = result.name;
			resultIdx++;
		}
	}

	private void clearResults() {
		resultHead = null;
		resultCount = 0;
		results = null;
		resultNames = null;
		resultMap = null;
	}

	private Result addResult(Result result, Result resultTail) {
		if(resultHead == null) {
			resultHead = result;
		} else {
			resultTail.next = result;
		}
		result.next = null;

		resultCount++;
		results = null;
		resultNames = null;
		resultMap = null;

		return result;
	}

	private Result removeResult(Result result, Result resultTail) {
		if(resultHead == result) {
			resultHead = result.next;
			if(resultHead == null) {
				resultTail = null;
			}
		} else {
			Result prevResult = resultHead;
			while(prevResult != null && prevResult.next != result) prevResult = prevResult.next;
			if(prevResult != null) {
				prevResult.next = result.next;
				if(resultTail == result) {
					resultTail = prevResult;
				}
			}
		}
		result.next = null;

		resultCount--;
		results = null;
		resultNames = null;
		resultMap = null;

		return resultTail;
	}

	protected Result newResult() {
		return new Result();
	}

	protected void preparseLocalQueryLibs(Element queryElem) throws SdaiException {
		//FIXME: implement preparsing
		localQueryLibProvider = getQueryLibProvider(SCOPE_LOCAL);
		localQueryLibsPreparsed = true;
	}

	public final Object getNamespaceHandler(String namespaceURI) {
		return namespaceMap.get(namespaceURI);
	}

	public final boolean isNamespaceHandler(String namespaceURI) {
		return namespaceMap.containsKey(namespaceURI);
	}

	protected abstract Context newContext(boolean executeContext) throws SdaiException;
	protected abstract Object createNamespaceHandler(String namespaceURI, 
													 Element queryElem) throws SdaiException;
	protected abstract void setDefaultSchema(String schema_name) throws SdaiException;
	protected abstract QueryLibProvider getQueryLibProvider(int scope) throws SdaiException;
	protected abstract void queryScope(int scope) throws SdaiException;
// 	abstract public List getResultList(String name) throws SdaiException;
// 	abstract public List getResultList(int index) throws SdaiException;

	public String toString() {
		StringBuffer asString = new StringBuffer();
		asString.append("(SdaiQueryEngine:");
		for(Result result = resultHead; result != null;
			result = result.next) {
			asString.append(" ");
			asString.append(result.toString());
		}
		asString.append(" )");
		return asString.toString();
	}

	public static String formatMessage(Node node, String message1, String message2) {
		StringBuffer message = new StringBuffer(message1);
		boolean bottom = true;
		for( ; node != null && node.getParentNode() != null; node = node.getParentNode()) {
			int position = 0;
			for(Node sibling = node; sibling != null; sibling = sibling.getPreviousSibling()) {
				if(sibling.getNodeType() == Node.ELEMENT_NODE) position++;
			}
			message.append(bottom ? " at " : " in ");
			message.append(node.getNodeName());
			message.append("(");
			message.append(Integer.toString(position));
			message.append(")");
			bottom = false;
		}
		if(message2 != null) {
			message.append(": ");
			message.append(message2);
		}
		return message.toString();
	}

	protected static class QueryLibCompleteFactory implements ConstraintFactory {
		private QueryLib queryLib;

		QueryLibCompleteFactory(QueryLib queryLib) {
			this.queryLib = queryLib;
		}

		public Constraint newConstraint(Node constraint) throws SdaiException {
			int constraintIdx = Arrays.binarySearch(completeConstraints, constraint.getLocalName());
			if(constraintIdx < 0) {
				throw new SdaiException(SdaiException.FN_NAVL,
										formatMessage(constraint, "Unrecognized constraint", null));
			}
			return queryLibConstrCreators[constraintIdx].create(queryLib);
		}
	}

	/*
	 * Constraint and result classes
	 */


	protected static class Result extends ConstraintContainer {
		public static final String TYPE = "result";

		protected String name;
		protected String schema_name;
		public Result next;
		//protected List resultInstances;
		protected Context context;

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			NamedNodeMap attributeMap = containerNode.getAttributes();
			Node xmlAttribute;
			xmlAttribute = attributeMap.getNamedItemNS(null, "name");
			name = xmlAttribute != null ? xmlAttribute.getNodeValue() : null;
			if(containerNode.getNamespaceURI().equals(QUERY_V1_URL)) {
				xmlAttribute = attributeMap.getNamedItemNS(null, "schema");
				if(xmlAttribute == null) {
					throw new SdaiException(SdaiException.FN_NAVL,
											formatMessage(containerNode, "Attribute schema is required", 
														  null));
				}
				schema_name = xmlAttribute.getNodeValue();
				context.query.setDefaultSchema(schema_name);
			}
			parseFromConstraintList(containerNode, null, context,
									false, context.getRegConstraintFactory());
		}

		public void execute(Context context) throws SdaiException {
			for(Constraint constraint = constraints; constraint != null; constraint = constraint.next) {
				context.contextConstraint = constraint;
				try {
					constraint.execute(context);
				} finally {
					context.contextConstraint = null;
				}
			}
			this.context = context;
			//resultInstances -= context.getResultInstances();
		}

		public String getType() {
			return TYPE;
		}

		protected final String getSchemaName() {
			return schema_name;
		}

		public final Context getResultContext() {
			return context;
		}
	}


	public static abstract class GroupConstraint extends Constraint {
		protected void parseFromConstraintList(Node containerNode, ConstraintContainer parent,
											  Context context, boolean split,
											  ConstraintFactory constraintFactory) throws SdaiException {
			//Object consList[][]
			for(Node constraintChild = containerNode.getFirstChild(); constraintChild != null;
				constraintChild = constraintChild.getNextSibling()) {
				if(constraintChild.getNodeType() != Node.ELEMENT_NODE) continue;
				if(!context.query.namespaceMap.containsKey(constraintChild.getNamespaceURI())) {
					throw new SdaiException(SdaiException.FN_NAVL,
											formatMessage(constraintChild, "Unknown namespace: ",
														  constraintChild.getNamespaceURI()));
				}
				Grp grp = new Grp();
				Context childContext = context.dup(true, false);
				if(constraintChild.getLocalName().equals("grp")) {
					grp.parseFromConstraintList(constraintChild, this, childContext,
												false, childContext.getRegConstraintFactory());
				} else {
					Constraint newConstraint = constraintFactory.newConstraint(constraintChild);
// 					int classIdx = Arrays.binarySearch(consList, constraintChild.getLocalName(), this);
// 					if(classIdx < 0) {
// 						throw new SdaiException(SdaiException.FN_NAVL,
// 												formatMessage(constraintChild, "Unrecognized constraint",
// 															  null));
// 					}
// 					Constraint newConstraint;
// 					try {
// 						newConstraint = (Constraint)((Class)consList[classIdx][1]).newInstance();
// 					} catch(IllegalAccessException e) {
// 						throw new SdaiException(SdaiException.SY_ERR, e);
// 					} catch(InstantiationException e) {
// 						throw new SdaiException(SdaiException.SY_ERR, e);
// 					}
					newConstraint.parse(constraintChild, parent != null ? parent : this, childContext);
					grp.addConstraint(newConstraint);
				}
				addConstraint(grp);
				context.addChildContext(childContext);
			}
		}

		protected void executeChildren(Context context, boolean split) throws SdaiException {
			for(Constraint grp = constraints; grp != null; grp = grp.next) {
				Context childContext = context.dup(split, false);
				for(Constraint constraint = grp.constraints; constraint != null;
					constraint = constraint.next) {
					childContext.contextConstraint = constraint;
					try {
						constraint.execute(childContext);
					} finally {
						childContext.contextConstraint = null;
					}
				}
				context.addChildContext(childContext);
			}
		}

		protected void executeChildrenInv(Context context, boolean split) throws SdaiException {
			for(Constraint grp = constraints; grp != null; grp = grp.next) {
				Context childContext = context.dup(split, false);
				for(Constraint constraint = grp.constraintsInv; constraint != null;
					constraint = constraint.prev) {
					childContext.contextConstraint = constraint;
					try {
						constraint.executeInv(childContext);
					} finally {
						childContext.contextConstraint = null;
					}
				}
				context.addChildContext(childContext);
			}
		}

	}

	public static class Intersect extends GroupConstraint {
		public static final String TYPE = "intersect";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			parseFromConstraintList(containerNode, null, context,
									false, context.getRegConstraintFactory());
			context.childIntersect();
		}

		protected void execute(Context context) throws SdaiException {
			executeChildren(context, false);
			context.childIntersect();
		}

		protected void executeInv(Context context) throws SdaiException {
			executeChildrenInv(context, false);
			context.childIntersect();
		}

		public String getType() {
			return TYPE;
		}

	}

	public static class Union extends GroupConstraint {
		public static final String TYPE = "union";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			parseFromConstraintList(containerNode, null, context,
									false, context.getRegConstraintFactory());
			context.childUnion();
		}

		protected void execute(Context context) throws SdaiException {
			executeChildren(context, false);
			context.childUnion();
		}

		protected void executeInv(Context context) throws SdaiException {
			executeChildrenInv(context, false);
			context.childUnion();
		}

		public String getType() {
			return TYPE;
		}

	}

	public static class And extends GroupConstraint {
		public static final String TYPE = "and";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			parseFromConstraintList(containerNode, null, context,
									true, context.getRegConstraintFactory());
			context.childAnd();
		}

		protected void execute(Context context) throws SdaiException {
			executeChildren(context, true);
			context.childAnd();
		}

		protected void executeInv(Context context) throws SdaiException {
			executeChildrenInv(context, true);
			context.childAnd();
		}

		public String getType() {
			return TYPE;
		}

	}

	public static class Or extends GroupConstraint {
		public static final String TYPE = "or";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			parseFromConstraintList(containerNode, null, context,
									true, context.getRegConstraintFactory());
			context.childOr();
		}

		protected void execute(Context context) throws SdaiException {
			executeChildren(context, true);
			context.childOr();
		}

		protected void executeInv(Context context) throws SdaiException {
			executeChildrenInv(context, true);
			context.childOr();
		}

		public String getType() {
			return TYPE;
		}

	}

	public static class Items extends GroupConstraint {
		public static final String TYPE = "items";

		protected boolean includeInstances;

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			if(parent.getType() != Result.TYPE) {
				throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine
										.formatMessage(containerNode, 
													   "item can only be used as result's " + 
													   "top level constraint", 
													   null));
			}
			Node sibling = containerNode.getNextSibling();
			while(sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE) {
				sibling = sibling.getNextSibling();
			}
			if(sibling != null) {
				throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine
										.formatMessage(containerNode, 
													   "item has to be the last constraint", null));
			}
			Node xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "instances");
			includeInstances = true;
			if(xmlAttribute != null) {
				includeInstances = xmlAttribute.getNodeValue().equals("include");
			}

			parseFromConstraintList(containerNode, null, context,
									true, context.getRegConstraintFactory());
		}

		protected void execute(Context context) throws SdaiException {
			for(Constraint grp = constraintsInv; grp != null; grp = grp.prev) {
				Context childContext = makeChildContext(context);
				for(Constraint constraint = grp.constraints; constraint != null;
					constraint = constraint.next) {
					childContext.contextConstraint = constraint;
					try {
						constraint.execute(childContext);
					} finally {
						childContext.contextConstraint = null;
					}
				}
				addChildToContext(context, childContext);
			}

			if(!includeInstances) {
				context.resultSetOffset = 0;
			}
		}

		protected void executeForwardDirection(Context context) throws SdaiException {
			for(Constraint grp = constraints; grp != null; grp = grp.next) {
				Context childContext = makeChildContext(context);
				for(Constraint constraint = grp.constraints; constraint != null;
					constraint = constraint.next) {
					childContext.contextConstraint = constraint;
					try {
						constraint.execute(childContext);
					} finally {
						childContext.contextConstraint = null;
					}
				}
				addChildToContext(context, childContext);
			}

			if(!includeInstances) {
				context.resultSetOffset = 0;
			}
		}

		protected Context makeChildContext(Context context) {
			Context childContext = context.dup(true, true);
			return childContext;
		}

		protected void addChildToContext(Context context, Context childContext) {
			context.addChildContext(childContext);
		}

		protected boolean isChildValueResult(Node childNode) throws SdaiException {
			if(childNode.getParentNode().getLocalName().equals("grp")) {
				Node sibling = childNode.getNextSibling();
				while(sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE) {
					sibling = sibling.getNextSibling();
				}
				return sibling == null;
			} else {
				return true;
			}
		}

		public String getType() {
			return TYPE;
		}

	}

	protected static class Grp extends Constraint {
		public static final String TYPE = "grp";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			throw new SdaiException(SdaiException.SY_ERR,
									formatMessage(containerNode, "Grp.parse can never be called", null));
		}

		protected void execute(Context context) throws SdaiException {
			throw new SdaiException(SdaiException.SY_ERR, "Grp.execute can never be called");
		}

		public String getType() {
			return TYPE;
		}

		protected void parseFromConstraintList(Node containerNode, ConstraintContainer parent,
											  Context context, boolean split,
											  ConstraintFactory constraintFactory) throws SdaiException {
			super.parseFromConstraintList(containerNode, parent, context, split, constraintFactory);
			context.assign(context.childContext);
		}
	}

	public static class Not extends Constraint {
		public static final String TYPE = "not";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			// Specification has changed (I mean, needs to be updated)
// 			if(containerNode.getFirstChild()!= null) {
// 				throw new SdaiException(SdaiException.FN_NAVL,
// 										formatMessage(containerNode, "Not can not have children", null));
// 			}

			parseFromConstraintList(containerNode, null, context,
									true, context.getRegConstraintFactory());
			context.childNot();
		}

		protected void execute(Context context) throws SdaiException {
			executeChildren(context, true);
			context.childNot();
		}

		public String getType() {
			return TYPE;
		}
	}

	public abstract static class Eq extends Constraint {
		public static final String TYPE = "eq";

		protected String expected;
		protected boolean paramExpected;

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			parent.checkSiblings(containerNode);			
			Node xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "param");
			if(xmlAttribute != null) {
				if(!context.query.allowParams) {
					throw new SdaiException(SdaiException.FN_NAVL, "Parameters are not allowed in the query");
				}
				expected = xmlAttribute.getNodeValue();
				context.query.addAllowedParameter(expected);
				paramExpected = true;
			} else {
				expected = containerNode.getFirstChild().getNodeValue();
				paramExpected = false;
			}
		}

		abstract protected void execute(Context context) throws SdaiException;

		public String getType() {
			return TYPE;
		}
	}

	// Spec needs updating
	public abstract static class Neq extends Eq {
		public static final String TYPE = "neq";

		abstract protected void execute(Context context) throws SdaiException;

		public String getType() {
			return TYPE;
		}
	}

	public abstract static class Regex extends Eq {
		public static final String TYPE = "regex";

		abstract protected void execute(Context context) throws SdaiException;

		public String getType() {
			return TYPE;
		}
	}

	public abstract static class Nregex extends Eq {
		public static final String TYPE = "nregex";

		abstract protected void execute(Context context) throws SdaiException;

		public String getType() {
			return TYPE;
		}
	}

	public abstract static class ValAnd extends Constraint {
		public static final String TYPE = "valand";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			parent.checkSiblings(containerNode); // FIXME!!! Maybe split parameter should be true?
			parseFromConstraintList(containerNode, null, context, false, 
									context.query.valConstraintFactory);
			context.childContext = null;
		}

		abstract protected void execute(Context context) throws SdaiException;

		public String getType() {
			return TYPE;
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

	public abstract static class ValOr extends Constraint {
		public static final String TYPE = "valor";

		protected void parse(Node containerNode, ConstraintContainer parent,
							 Context context) throws SdaiException {
			parent.checkSiblings(containerNode);
			parseFromConstraintList(containerNode, null, context, false, 
									context.query.valConstraintFactory);
			context.childContext = null;
		}

		abstract protected void execute(Context context) throws SdaiException;

		public String getType() {
			return TYPE;
		}
	}

} // SdaiQueryEngine

/*
Local Variables:
compile-command: "ant -emacs -find build-local.xml sdai.query.engine.test"
End:
*/
