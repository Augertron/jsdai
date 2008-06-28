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
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * Created: Mon Jun 16 13:09:19 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryLib {
	public static final String QUERY_ENT_ELEM = "query-ent";

	public final String queryLibId;

	private Map queryEntMap;

	QueryLib(String queryLibId) {
		this.queryLibId = queryLibId;
		queryEntMap = new HashMap();
	}

	public void parse(SdaiQueryEngine engine, Element queryElem) throws SdaiException {
		queryEntMap.clear();
		boolean isQueryLibEmpty = true;
		for(Node queryChild = queryElem.getFirstChild(); queryChild != null;
			queryChild = queryChild.getNextSibling()) {
			if(queryChild.getNodeType() != Node.ELEMENT_NODE) continue;
			if(!engine.isNamespaceHandler(queryChild.getNamespaceURI())) {
				throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
										formatMessage(queryChild, "Unknown namespace: ",
													  queryChild.getNamespaceURI()));
			}
			if(queryChild.getLocalName().equals(QUERY_ENT_ELEM)) {
				QueryEnt queryEnt = new QueryEnt();
				queryEnt.parse(queryChild, null, engine.newContext(false));
				queryEntMap.put(queryEnt.getName(), queryEnt);
			} else {
				throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
										formatMessage(queryChild, "Unrecognized element", null));
			}
			isQueryLibEmpty = false;
		}
		if(isQueryLibEmpty) {
			throw new SdaiException(SdaiException.FN_NAVL, "Query is empty");
		}
	}

	QueryEnt getQueryEnt(String name) throws SdaiException {
		QueryEnt queryEnt = (QueryEnt)queryEntMap.get(name);
		if(queryEnt == null) {
			throw new SdaiException(SdaiException.FN_NAVL, 
									"Query-ent with the name " + name + "not found");
		}
		return queryEnt;
	}

	QueryEnt getQueryEnt(String name, Node containerNode) throws SdaiException {
		QueryEnt queryEnt = (QueryEnt)queryEntMap.get(name);
		if(queryEnt == null) {
			throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
									formatMessage(containerNode, 
												  "Query-ent with the name " + name + "not found", null));
		}
		return queryEnt;
	}

// 	public void parse(SdaiQueryEngine engine, Element queryElem) {
// 	}

} // QueryLib
