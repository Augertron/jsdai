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

package jsdai.SPhysical_unit_3d_design_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SContextual_shape_positioning_xim.CxContextual_shape_representation;
import jsdai.SContextual_shape_positioning_xim.CxGeometric_composition_with_operator_transformation;
import jsdai.SPhysical_unit_3d_design_view_mim.CComponent_3d_location;
import jsdai.SRepresentation_schema.ERepresentation_relationship;

public class CxComponent_3d_location_armx extends CComponent_3d_location_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From RR
	/// methods for attribute: name, base type: STRING
	public boolean testName(ERepresentation_relationship type) throws SdaiException {
		return test_string(a2);
	}
	public String getName(ERepresentation_relationship type) throws SdaiException {
		return get_string(a2);
	}
	public void setName(ERepresentation_relationship type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetName(ERepresentation_relationship type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_relationship type) throws SdaiException {
		return a2$;
	}
	// ENDOF RR
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CComponent_3d_location.definition);

		setMappingConstraints(context, this);
		
		// placement_fixed
		setPlacement_fixed(context, this);
		
		// clean ARM
		// placement_fixed
		unsetPlacement_fixed(null);
		

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// placement_fixed
		unsetPlacement_fixed(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	component_2d_location &lt;=
	*  [context_dependent_shape_representation]
	*  [shape_representation_relationship]
	*  [representation_relationship_with_transformation]
	*  [definitional_representation_relationship]
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EComponent_3d_location_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxContextual_shape_representation.setMappingConstraints(context, armEntity);
		CxGeometric_composition_with_operator_transformation.setMappingConstraints(context, armEntity);
		armEntity.setName(null, "component 3d location");
	}

	public static void unsetMappingConstraints(SdaiContext context, EComponent_3d_location_armx armEntity) throws SdaiException
	{
		CxContextual_shape_representation.unsetMappingConstraints(context, armEntity);
		CxGeometric_composition_with_operator_transformation.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName(null);
	}

	//**********component_2d_location attributes
	/**
	* Sets/creates data for placement_fixed attribute.
	* @param context SdaiContext.
	* @param armEntity arm entity.
	*
	*  attribute_mapping placement_fixed (placement_fixed
	* , descriptive_representation_item.description);
	*  component_2d_location &lt;=
	*  context_dependent_shape_representation
	*  context_dependent_shape_representation.description 
	*  {(context_dependent_shape_representation.description = 'placement fixed')
	*  (context_dependent_shape_representation.description = 'placement not fixed')}	
	* end_attribute_mapping;
	*
	*/
	// CL -> DRI
	public static void setPlacement_fixed(SdaiContext context, EComponent_3d_location_armx armEntity) throws SdaiException {
		unsetPlacement_fixed(context, armEntity);
		if (armEntity.testPlacement_fixed(null)) {
			
			boolean armPlacement_fixed = armEntity.getPlacement_fixed(null);

			if(armPlacement_fixed)
				armEntity.setDescription(null, "placement fixed");
			else
				armEntity.setDescription(null, "placement not fixed");

		}
	}

	public static void unsetPlacement_fixed(SdaiContext context, EComponent_3d_location_armx armEntity) throws SdaiException {
		if (armEntity.testPlacement_fixed(null)) {
//			System.err.println(" in Cx of C2DL - unsetting "+armEntity.getAimInstance());
			armEntity.unsetDescription(null);
		}
	}
	
}
