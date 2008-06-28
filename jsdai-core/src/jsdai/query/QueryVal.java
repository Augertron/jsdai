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
 * Created: Tue Jun 17 20:40:35 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class QueryVal extends ConstraintContainer {
	public static final String TYPE = "query-val";

	private Context inValContext;
	private Context outValContext;
	private String attributeName;
	private String selectName;

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

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "select");
		selectName = xmlAttribute != null ? xmlAttribute.getNodeValue() : null;

		inValContext = context.dup(false, false);
		parseFromConstraintList(containerNode, null, context,
								false, context.getRegConstraintFactory());
		outValContext = context.childContext;
	}

	protected void execute(Context context) throws SdaiException {
		for(Constraint constraint = constraints; constraint != null; constraint = constraint.next) {
			constraint.execute(context);
		}
	}

	protected boolean isChildValueResult(Node childNode) throws SdaiException {
		Node sibling = childNode.getNextSibling();
		while(sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE) {
			sibling = sibling.getNextSibling();
		}
		return sibling == null;
	}

	public String getType() {
		return TYPE;
	}

	public final Context getInValContext() {
		return inValContext;
	}

	public final Context getOutValContext() {
		return outValContext;
	}

	public final Object getKey() {
		return selectName != null ? 
			(Object)new TwoStrings(attributeName, selectName) : (Object)attributeName;
	}

	public static Object makeKey(String attributeName, String selectName) {
		return selectName != null ? 
			(Object)new TwoStrings(attributeName, selectName) : (Object)attributeName;
	}

} // QueryVal
