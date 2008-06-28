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
import jsdai.SGeneric_material_aspects_xim.CxMaterial_property_armx;
import jsdai.SMaterial_property_definition_schema.CMaterial_property;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProperty_assignment_xim.CxApplied_independent_property;
import jsdai.SProperty_assignment_xim.EApplied_independent_property;

public class CxApplied_independent_material_property$material_property_armx extends CApplied_independent_material_property$material_property_armx implements EMappedXIMEntity{

	// Taken from CProperty_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a3);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a3$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMaterial_property.definition);
		
		setMappingConstraints(context, this);
		
		// base_independent_property
		setBase_independent_property(context, this);
		
		// clean ARM
		//	base_independent_property
		unsetBase_independent_property(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		//	base_independent_property
		unsetBase_independent_property(context, this);
		
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
			CApplied_independent_material_property$material_property_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxApplied_independent_material_property.setMappingConstraints(context, armEntity);
		CxMaterial_property_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CApplied_independent_material_property$material_property_armx armEntity) throws SdaiException {
		CxApplied_independent_material_property.unsetMappingConstraints(context, armEntity);
		CxMaterial_property_armx.unsetMappingConstraints(context, armEntity);
		
	}

	/* Sets/creates data for Base_independent_property attribute.
	*
		attribute_mapping base_independent_property(base_independent_property, $PATH, general_property);
			property_definition
			property_definition = derived_property_select
			derived_property_select <- 
			general_property_association.derived_definition
			general_property_association.base_definition -> general_property
      end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 
	public static void setBase_independent_property(SdaiContext context, EApplied_independent_property armEntity) throws SdaiException
	{
		CxApplied_independent_property.setBase_independent_property(context, armEntity);
	}


	/**
	* Unsets/deletes data for Base_independent_property attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetBase_independent_property(SdaiContext context, EApplied_independent_property armEntity) throws SdaiException
	{
		CxApplied_independent_property.setBase_independent_property(context, armEntity);		
	}
	
	
}