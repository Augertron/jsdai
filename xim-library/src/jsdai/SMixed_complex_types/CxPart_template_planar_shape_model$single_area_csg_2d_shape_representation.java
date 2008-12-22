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

package jsdai.SMixed_complex_types;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConstructive_solid_geometry_2d_mim.CSingle_area_csg_2d_shape_representation;
import jsdai.SLayered_interconnect_complex_template_xim.*;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxPart_template_planar_shape_model$single_area_csg_2d_shape_representation extends CPart_template_planar_shape_model$single_area_csg_2d_shape_representation implements EMappedXIMEntity
{


	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a3);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a3$;
	}
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CSingle_area_csg_2d_shape_representation.definition);

		setMappingConstraints(context, this);

		// shape_characterized_part_template
		setShape_characterized_definition(context, this);
		
      // shape_environment
		setShape_environment(context, this);
		
      // shape_material_condition 
		setShape_material_condition(context, this);

		// clean ARM
		// shape_characterized_part_template
		unsetShape_characterized_definition(null);
		
      // shape_environment
		unsetShape_environment(null);
		
      // shape_material_condition 
		unsetShape_material_condition(null);
      
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// shape_characterized_part_template
		unsetShape_characterized_definition(context, this);
		
      // shape_environment
		unsetShape_environment(context, this);
		
      // shape_material_condition 
		unsetShape_material_condition(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	*  representation				
	*  [representation.id = 'ac2ds']
	*  [representation.name = 'planar projected shape']
	*  [representation &lt;-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  {property_definition =&gt;
	*  product_definition_shape}
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition
	*  characterized_product_definition = product_definition
	*  product_definition =&gt;
	*  component_definition =&gt;
	*  assembly_component]}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CPart_template_planar_shape_model$single_area_csg_2d_shape_representation armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		//	Order is importatnt as AC2DS is subtype of FSD and only in this way magic strings can be synchronized		
		CxPart_template_planar_shape_model.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CPart_template_planar_shape_model$single_area_csg_2d_shape_representation armEntity) throws SdaiException
	{
		//	Order is importatnt as AC2DS is subtype of FSD and only in this way magic strings can be synchronized
		CxPart_template_planar_shape_model.unsetMappingConstraints(context, armEntity);
	}

	// Part_template_planar_shape
	// shape_characterized_part_template
	public static void setShape_characterized_definition(SdaiContext context, EPart_template_planar_shape_model armEntity) throws SdaiException {
		CxPart_template_planar_shape_model.setShape_characterized_definition(context, armEntity);		
	}
	
	public static void unsetShape_characterized_definition(SdaiContext context, EPart_template_planar_shape_model armEntity) throws SdaiException {
		CxPart_template_planar_shape_model.unsetShape_characterized_definition(context, armEntity);		
	}
	
   // shape_environment
	public static void setShape_environment(SdaiContext context, EPart_template_planar_shape_model armEntity) throws SdaiException {
		CxPart_template_planar_shape_model.setShape_environment(context, armEntity);		
	}
	
	public static void unsetShape_environment(SdaiContext context, EPart_template_planar_shape_model armEntity) throws SdaiException {
		CxPart_template_planar_shape_model.unsetShape_environment(context, armEntity);		
	}

	
   // shape_material_condition
	public static void setShape_material_condition(SdaiContext context, EPart_template_planar_shape_model armEntity) throws SdaiException {
		CxPart_template_planar_shape_model.setShape_material_condition(context, armEntity);		
	}

	public static void unsetShape_material_condition(SdaiContext context, EPart_template_planar_shape_model armEntity) throws SdaiException {
		CxPart_template_planar_shape_model.unsetShape_material_condition(context, armEntity);		
	}
	
}
