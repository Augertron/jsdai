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

import java.util.HashMap;
import java.util.Map;
import jsdai.lang.SdaiException;
import jsdai.query.ConstraintContainer;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * Created: Tue Jun 17 20:13:46 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryEnt extends ConstraintContainer {
	public static final String TYPE = "result";

	public static final String QUERY_TYPE_ELEM = "query-type";
	public static final String QUERY_FWD_ELEM = "query-fwd";
	public static final String QUERY_VAL_ELEM = "query-val";

	private String name;
	private QueryType queryType;
	private Map queryFwdMap;
	private Map queryValMap;

	QueryEnt() {
		queryFwdMap = new HashMap();
		queryValMap = new HashMap();
	}

	protected void parse(Node containerNode, ConstraintContainer parent,
						 Context context) throws SdaiException {
		NamedNodeMap attributeMap = containerNode.getAttributes();
		Node xmlAttribute;
		xmlAttribute = attributeMap.getNamedItemNS(null, "name");
		if(xmlAttribute == null) {
			throw new SdaiException(SdaiException.FN_NAVL,
									SdaiQueryEngine.formatMessage(containerNode, 
																  "Attribute name is required", null));
		}
		name = xmlAttribute.getNodeValue();
		boolean isQueryEntEmpty = true;
		Context queryTypeContext = null;
		for(Node queryChild = containerNode.getFirstChild(); queryChild != null;
			queryChild = queryChild.getNextSibling()) {
			if(queryChild.getNodeType() != Node.ELEMENT_NODE) continue;
			if(!context.query.isNamespaceHandler(queryChild.getNamespaceURI())) {
				throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
										formatMessage(queryChild, "Unknown namespace: ",
													  queryChild.getNamespaceURI()));
			}
			if(isQueryEntEmpty && queryChild.getLocalName().equals(QUERY_TYPE_ELEM)) {
				queryType = new QueryType();
				queryType.parse(queryChild, this, context.dup(false, false));
				queryTypeContext = queryType.getTypeContext();
			} else if(!isQueryEntEmpty && queryChild.getLocalName().equals(QUERY_FWD_ELEM)) {
				QueryFwd queryFwd = new QueryFwd();
				Context queryFwdContext = queryTypeContext.dup(false, false);
				queryFwd.parse(queryChild, this, queryFwdContext);
				queryFwdMap.put(queryFwd.getKey(), queryFwd);
			} else if(!isQueryEntEmpty && queryChild.getLocalName().equals(QUERY_VAL_ELEM)) {
				QueryVal queryVal = new QueryVal();
				Context queryValContext = queryTypeContext.dup(false, false);
				queryVal.parse(queryChild, this, queryValContext);
				queryValMap.put(queryVal.getKey(), queryVal);
			} else {
				throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
										formatMessage(queryChild, "Unrecognized element", null));
			}
			isQueryEntEmpty = false;
		}
		if(isQueryEntEmpty) {
			throw new SdaiException(SdaiException.FN_NAVL, "Query-ent is empty");
		}
	}

	public void execute(Context context) throws SdaiException {
		throw new SdaiException(SdaiException.SY_ERR, 
								"Method QueryEnt.execute CAN NEVER BE CALLED");
	}

	public final String getName() {
		return name;
	}

	public String getType() {
		return TYPE;
	}

	final QueryType getQueryType() {
		return queryType;
	}

	final QueryFwd getQueryFwd(String attributeName, String targetName, 
							   Node containerNode) throws SdaiException {
		QueryFwd queryFwd;
		queryFwd = (QueryFwd)queryFwdMap.get(QueryFwd.makeKey(attributeName, targetName));
		if(queryFwd == null && targetName != null) {
			queryFwd = (QueryFwd)queryFwdMap.get(attributeName);
		}

		if(queryFwd == null) {
			throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
									formatMessage(containerNode, 
												  "Query-fwd with the attribute " + attributeName + 
												  " and target name " + targetName + " not found", null));
		}
		return queryFwd;
	}

	final QueryVal getQueryVal(String attributeName, String selectName, 
							   Node containerNode) throws SdaiException {
		QueryVal queryVal;
		queryVal = (QueryVal)queryValMap.get(QueryVal.makeKey(attributeName, selectName));
		if(queryVal == null && selectName != null) {
			queryVal = (QueryVal)queryValMap.get(attributeName);
		}

		if(queryVal == null) {
			throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
									formatMessage(containerNode, 
												  "Query-fwd with the attribute " + attributeName + 
												  " and target name " + selectName + " not found", null));
		}
		return queryVal;
	}

} // QueryEnt
