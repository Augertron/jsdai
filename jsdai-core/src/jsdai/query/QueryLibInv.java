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
import jsdai.query.Inv;
import org.w3c.dom.Node;

/**
 *
 * Created: Mon Jun 16 15:38:28 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryLibInv extends Inv {
	private QueryLib queryLib;
	private QueryFwd queryFwd;

	public QueryLibInv(QueryLib queryLib) {
		this.queryLib = queryLib;
	}

	protected void setParameters(String entityName, String attributeName,
								 Node containerNode, Context context) throws SdaiException {
		QueryEnt queryEnt = queryLib.getQueryEnt(entityName, containerNode);
		queryFwd = queryEnt.getQueryFwd(attributeName, null, containerNode);
		if(!context.isNarrowedBy(queryFwd.getInInvContext())) {
			throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
									formatMessage(containerNode, 
												  "Query-fwd is not compatible with the current context",
												  null));
		}
		context.assign(queryFwd.getOutInvContext());
	}

	protected void execute(Context context) throws SdaiException {
		queryFwd.executeInv(context);
		executeChildren(context, true);
	}

} // QueryLibInv
