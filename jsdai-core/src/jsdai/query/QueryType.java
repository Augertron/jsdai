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
import jsdai.query.ConstraintContainer;
import org.w3c.dom.Node;

/**
 *
 * Created: Tue Jun 17 20:39:40 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryType extends ConstraintContainer {
	public static final String TYPE = "query-type";

	private Context typeContext;

	protected void parse(Node containerNode, ConstraintContainer parent,
						 Context context) throws SdaiException {
		parseFromConstraintList(containerNode, null, context,
								false, context.getRegConstraintFactory());
		typeContext = context.childContext;
	}

	protected void execute(Context context) throws SdaiException {
		for(Constraint constraint = constraints; constraint != null; constraint = constraint.next) {
			constraint.execute(context);
		}
	}

	public String getType() {
		return TYPE;
	}

	public final Context getTypeContext() {
		return typeContext;
	}

} // QueryType
