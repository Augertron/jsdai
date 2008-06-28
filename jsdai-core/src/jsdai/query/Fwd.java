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
import org.w3c.dom.Node;

/**
 *
 * Created: Thu May  8 20:07:00 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

abstract public class Fwd extends Constraint {
	public static final String TYPE = "fwd";

	protected String aggr;
	protected String aggrSize;

	protected void parse(Node containerNode, ConstraintContainer parent,
						 Context context) throws SdaiException {
		Node xmlAttribute;

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "ent");
		String entityName = null;
		if(xmlAttribute != null) {
			entityName = xmlAttribute.getNodeValue();
		}

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "attr");
		if(xmlAttribute == null) {
			throw new SdaiException(SdaiException.FN_NAVL,
									SdaiQueryEngine.formatMessage(containerNode, 
																  "Attribute attr is required", null));
		}
		String attributeName = xmlAttribute.getNodeValue();

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "aggr");
		aggr = xmlAttribute != null ? xmlAttribute.getNodeValue() : null;

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "aggr-size");
		aggrSize = xmlAttribute != null ? xmlAttribute.getNodeValue() : null;

		xmlAttribute = containerNode.getAttributes().getNamedItemNS(null, "target");
		String targetName = xmlAttribute != null ? xmlAttribute.getNodeValue() : null;
		setParameters(entityName, attributeName, targetName, containerNode, context);
		parseFromConstraintList(containerNode, null, context, true, 
								context.getRegConstraintFactory());
		context.childProcess();
	}

	protected void executeChildren(Context context, boolean split) throws SdaiException {
		super.executeChildren(context, split);
		context.childProcess();
	}

	public String getType() {
		return TYPE;
	}

	abstract protected void setParameters(String entityName, String attributeName, String targetName,
										  Node containerNode, Context context) throws SdaiException;

} // Fwd
