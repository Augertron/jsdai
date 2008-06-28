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

package jsdai.SPerson_organization_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SPerson_organization_schema.COrganization_relationship;
import jsdai.SPerson_organization_schema.EOrganization_relationship;

public class CxOrganization_relationship__legal_succession
		extends
		COrganization_relationship__legal_succession implements EMappedXIMEntity {

	EEntity getAimInstance(){
		return this;
	}

	// Taken from Organization_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EOrganization_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EOrganization_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EOrganization_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EOrganization_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EOrganization_relationship type) throws SdaiException {
		return a0$;
	}
	// ENDOF Taken from Organization_relationship

	public int attributeState = ATTRIBUTES_MODIFIED;	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", COrganization_relationship.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
	 *  product_definition
	 *  {product_definition.frame_of_reference -&gt; product_definition_context
	 *  product_definition_context &lt;= application_context_element
	 *  application_context_element.name = 'part definition'}
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EOrganization_relationship__legal_succession armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "legal succession");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EOrganization_relationship__legal_succession armEntity) throws SdaiException {
		armEntity.unsetName(null);
	}

}