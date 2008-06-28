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

package jsdai.SPart_template_2d_shape_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPart_template_shape_with_parameters_xim.*;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxStructured_template_planar_shape_model extends CStructured_template_planar_shape_model implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// FROM CRepresentation.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a0$;
	}
	// ENDOF FROM CRepresentation.java

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

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
	mapping_constraints;
		{shape_representation <=
		representation
		[representation.name = 'ppsm']
		[(representation.description = 'footprint definition shape model')
		(representation.description = 'breakout footprint definition shape model')
		(representation.description = 'structured template planar shape model')]
		[representation <-
		property_definition_representation.used_representation
		property_definition_representation
		{property_definition_representation =>
		shape_definition_representation}
		property_definition_representation.definition ->
		property_definition =>
		product_definition_shape =>
		part_template_definition =>
		geometric_template =>
		structured_template]}
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStructured_template_planar_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPart_template_planar_shape_model.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "structured template planar shape model");
	}

	public static void unsetMappingConstraints(SdaiContext context, EStructured_template_planar_shape_model armEntity) throws SdaiException
	{
		CxPart_template_planar_shape_model.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.unsetDerviedDescription(context, armEntity);
	}	
	
	//********** "part_template_planar_shape" attributes

	/**
	* Sets/creates data for shape_characterized_part_template attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	* Path: SR <- PDR -> PD -> PTD
	*/
	public static void setShape_characterized_definition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.setShape_characterized_definition(context, armEntity);		
	}


	/**
	* Unsets/deletes data for shape_characterized_part_template attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	* SR <- PDR -> PD -> PTD, remove all PDRs
	*/
	public static void unsetShape_characterized_definition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.unsetShape_characterized_definition(context, armEntity);		
	}


	/**
	* Sets/creates data for shape_environment attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- RR -> R -> DRI
	public static void setShape_environment(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.setShape_environment(context, armEntity);		
	}


	/**
	* Unsets/deletes data for shape_environment attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetShape_environment(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.unsetShape_environment(context, armEntity);		
	}


	/**
	* Sets/creates data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- RR ->R -> DRI
	public static void setShape_material_condition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.setShape_material_condition(context, armEntity);		
	}


	/**
	* Unsets/deletes data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetShape_material_condition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.unsetShape_material_condition(context, armEntity);		
	}
	
}
