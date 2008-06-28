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
import jsdai.query.QueryLib;
import jsdai.query.QueryLibProvider;
import jsdai.query.QueryLibProvider;
import org.w3c.dom.Element;

/**
 *
 * Created: Tue Jun 17 11:41:13 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class SimpleQueryLibProvider extends QueryLibProvider {

	private Map queryLibMap;

	public SimpleQueryLibProvider() {
		queryLibMap = new HashMap();
	}
	
	// Implementation of jsdai.query.QueryLibProvider

	/**
	 * Describe <code>createQueryLib</code> method here.
	 *
	 * @param queryLibId a <code>String</code> value
	 * @return a <code>QueryLib</code> value
	 * @exception SdaiException if an error occurs
	 */
	public QueryLib createQueryLib(String queryLibId, SdaiQueryEngine engine, 
								   Element queryElem) throws SdaiException {
		QueryLib queryLib = new QueryLib(queryLibId);
		queryLib.parse(engine, queryElem);
		queryLibMap.put(queryLibId, queryLib);
		return queryLib;
	}

	public QueryLib findQueryLib(String queryLibId) throws SdaiException {
		return (QueryLib)queryLibMap.get(queryLibId);
	}
	
} // SimpleQueryLibProvider
