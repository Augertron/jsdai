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

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

/**
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class CEntityDefinitionDeprecated extends CEntity_or_view_definition implements EEntityDefinitionDeprecated {
	/** Can not be explicitly created */
    protected CEntityDefinitionDeprecated() {
    }

	public boolean testExplicit_attributes(EEntity_definition type) throws SdaiException {
		return true;
	}

	public AExplicit_attribute getExplicit_attributes(EEntity_definition type) throws SdaiException {
		AExplicit_attribute explicitAttributes = new AExplicit_attribute();
		AExplicit_attribute orderedAttributes = new AExplicit_attribute();
		CExplicit_attribute.usedinParent(null, (EEntity_definition)this, null, explicitAttributes);
		SdaiIterator explicitAttributeIter = explicitAttributes.createIterator();
		while(explicitAttributeIter.next()) {
			EExplicit_attribute explicitAttribute = 
				explicitAttributes.getCurrentMember(explicitAttributeIter);
			if(explicitAttribute.testOrder(null)) {
				int attributeOrder = explicitAttribute.getOrder(null);
				int orderedAttributesCount = orderedAttributes.getMemberCount();
				if(orderedAttributesCount <= attributeOrder) {
					while(orderedAttributesCount <= attributeOrder) {
						orderedAttributes.addByIndex(++orderedAttributesCount, explicitAttribute);
					}
				} else {
					orderedAttributes.setByIndex(attributeOrder + 1, explicitAttribute);
				}
			}
		}
		return orderedAttributes;
	}
}
