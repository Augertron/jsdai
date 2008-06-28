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
 * Created: Tue Jun 17 20:40:12 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryFwd extends ConstraintContainer {
	public static final String TYPE = "query-fwd";

	private Context inFwdContext;
	private Context outFwdContext;
	private String attributeName;
	private String targetName;

	protected void parse(Node containerNode, ConstraintContainer parent,
						 Context context) throws SdaiException {
		Node xmlAttribute;

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "attr");
		if(xmlAttribute == null) {
			throw new SdaiException(SdaiException.FN_NAVL,
									SdaiQueryEngine.formatMessage(containerNode, 
																  "Attribute attr is required", null));
		}
		attributeName = xmlAttribute.getNodeValue();

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "target");
		targetName = xmlAttribute != null ? xmlAttribute.getNodeValue() : null;

		inFwdContext = context.dup(false, false);
		parseFromConstraintList(containerNode, null, context,
								false, context.getRegConstraintFactory());
		outFwdContext = context.childContext;
	}

	protected void execute(Context context) throws SdaiException {
		for(Constraint constraint = constraints; constraint != null; constraint = constraint.next) {
			constraint.execute(context);
		}
	}

	protected void executeInv(Context context) throws SdaiException {
		for(Constraint constraint = constraintsInv; constraint != null; constraint = constraint.prev) {
			constraint.executeInv(context);
		}
	}

	public String getType() {
		return TYPE;
	}

	public final Context getInFwdContext() {
		return inFwdContext;
	}

	public final Context getOutFwdContext() {
		return outFwdContext;
	}

	public final Context getInInvContext() {
		return outFwdContext;
	}

	public final Context getOutInvContext() {
		return inFwdContext;
	}

	public final Object getKey() {
		return targetName != null ? 
			(Object)new TwoStrings(attributeName, targetName) : (Object)attributeName;
	}

	public static Object makeKey(String attributeName, String targetName) {
		return targetName != null ?
			(Object)new TwoStrings(attributeName, targetName) : (Object)attributeName;
	}

} // QueryFwd
