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
import jsdai.query.Val;
import org.w3c.dom.Node;

/**
 *
 * Created: Mon Jun 16 15:39:43 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryLibVal extends Val {
	private QueryLib queryLib;
	private QueryVal queryVal;
	private Constraint contextConstraint;

	public QueryLibVal(QueryLib queryLib) {
		this.queryLib = queryLib;
	}

	protected void setParameters(String entityName, String attributeName, String selectNames,
								 Node containerNode, Context context) throws SdaiException {
		if(entityName == null) {
			throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
									formatMessage(containerNode, 
												  "Attribute ent is required for query lib fwd", null));
		}
		QueryEnt queryEnt = queryLib.getQueryEnt(entityName, containerNode);
		queryVal = queryEnt.getQueryVal(attributeName, selectNames, containerNode);
		if(!context.isNarrowedBy(queryVal.getInValContext())) {
			throw new SdaiException(SdaiException.FN_NAVL, SdaiQueryEngine.
									formatMessage(containerNode, 
												  "Query-fwd is not compatible with the current context",
												  null));
		}
		context.assign(queryVal.getOutValContext());
	}

	protected void execute(Context context) throws SdaiException {
		contextConstraint = context.contextConstraint;
		context.contextConstraint = this;
		try {
			queryVal.execute(context);
		} finally {
			context.contextConstraint = contextConstraint;
		}
	}

	protected void executeChildren(Context context, boolean split) throws SdaiException {
		if(constraints != null) {
			constraints.execute(context);
		}
		if(contextConstraint != this && contextConstraint.containsValConstraints()) {
			contextConstraint.executeChildren(context, split);
		}
	}

	public boolean isValueResult() {
		return contextConstraint == this ?
			valueResult : valueResult && contextConstraint.isValueResult();
	}

	public boolean containsValConstraints() {
		return contextConstraint == this ?
			constraints != null : constraints != null || contextConstraint.containsValConstraints();
	}

} // QueryLibVal
