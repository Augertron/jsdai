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

import jsdai.lang.SdaiException;
import jsdai.query.Type;
import org.w3c.dom.Node;

/**
 *
 * Created: Mon Jun 16 15:39:09 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryLibType extends Type {
	private QueryLib queryLib;
	private QueryType queryType;

	public QueryLibType(QueryLib queryLib) {
		this.queryLib = queryLib;
	}

	protected void setParameters(String entityName,
								 Node containerNode, Context context) throws SdaiException {
		QueryEnt queryEnt = queryLib.getQueryEnt(entityName, containerNode);
		queryType = queryEnt.getQueryType();
		Context queryTypeContext = queryType.getTypeContext();
		if(!context.isNarrowedBy(queryTypeContext)) {
			throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
									formatMessage(containerNode, 
												  "Query-type is not compatible with the current context",
												  null));
		}
		context.assign(queryTypeContext);
	}

	protected void execute(Context context) throws SdaiException {
		context.contextConstraint = this;
		try {
			queryType.execute(context);
		} finally {
			context.contextConstraint = null;
		}
		executeChildren(context, true);
	}

} // QueryLibType
