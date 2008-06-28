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

package jsdai.SGeneric_material_aspects_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SMaterial_property_definition_schema.CMaterial_property;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProperty_assignment_xim.CxAssigned_property;
import jsdai.SProperty_assignment_xim.EAssigned_property;

public class CxMaterial_property_armx extends CMaterial_property_armx implements EMappedXIMEntity{

	// Taken from CProperty_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a0$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMaterial_property.definition);
		
		// id_x : OPTIONAL STRING;
		setId_x(context, this);
		// DERIVED
		// representation : OPTIONAL property_value_select;
		// setRepresentation(context, this);
		
		// clean ARM
		// id_x : OPTIONAL STRING;
		unsetId_x(null);
		
		// representation : OPTIONAL property_value_select;
		// unsetRepresentation(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		// id_x : OPTIONAL STRING;
		unsetId_x(context, this);
		
		// representation : OPTIONAL property_value_select;
		// unsetRepresentation(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
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
			EMaterial_property_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EMaterial_property_armx armEntity) throws SdaiException {
	}
	/**
	 * Sets/creates data for id_x attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setId_x(SdaiContext context, EAssigned_property armEntity) throws SdaiException {
		CxAssigned_property.setId_x(context, armEntity);
	}

	/**
	 * Unsets/deletes data for id_x attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context, EAssigned_property armEntity) throws SdaiException {
		CxAssigned_property.unsetId_x(context, armEntity);
	}
	
	/**
	 * Sets/creates data for representation attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/*	public static void setRepresentation(SdaiContext context, EAssigned_property armEntity) throws SdaiException {
		CxAssigned_property.setRepresentation(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes data for representation attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/*	public static void unsetRepresentation(SdaiContext context, EAssigned_property armEntity) throws SdaiException {
		CxAssigned_property.unsetRepresentation(context, armEntity);
	}
*/		
}