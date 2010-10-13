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

package jsdai.SFootprint_definition_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.SLayered_interconnect_complex_template_xim.CxStructured_template_planar_shape_model;
import jsdai.SPart_template_shape_with_parameters_xim.CxPart_template_shape_model;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SProduct_property_representation_schema.EShape_representation;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_relationship;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;

public class CxFootprint_definition_shape_model extends CFootprint_definition_shape_model implements EMappedXIMEntity
{

	// From CRepresentation.java
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

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

		setMappingConstraints(context, this);

      setShape_characterized_definition(context, this);
      setShape_environment(context, this);
      setShape_material_condition(context, this);
      setReference_shape(context, this);
		
      unsetShape_characterized_definition(null);
      unsetShape_environment(null);
      unsetShape_material_condition(null);
      unsetReference_shape(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      unsetShape_characterized_definition(context, this);
      unsetShape_environment(context, this);
      unsetShape_material_condition(context, this);
      unsetReference_shape(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	* 	representation
	* 	[representation.name = 'planar projected shape']
	* 	[representation.description = 'footprint definition shape']
	* 	[representation &lt;-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition -&gt;
	* 	property_definition
	* 	property_definition.definition -&gt;
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition =&gt;
	* 	footprint_definition]}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxStructured_template_planar_shape_model.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "footprint definition shape model");
	}

	public static void unsetMappingConstraints(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
		CxStructured_template_planar_shape_model.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "");
	}

	//********** "footprint_definition_shape" attributes

	/**
	* Sets/creates data for shape_characterized_footprint_definition attribute.
	*
	* <p>
	attribute_mapping shape_characterized_definition(shape_characterized_definition, $PATH, Generic_footprint_definition_armx);
		shape_representation <=
		representation <-
		property_definition_representation.used_representation
		property_definition_representation
		{property_definition_representation =>
		shape_definition_representation}
		property_definition_representation.definition ->
		property_definition =>
		product_definition_shape =>
		part_template_definition =>
		geometric_template =>
		structured_template =>
		multi_stratum_structured_template =>
		generic_footprint_definition

	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- PDR -> PD -> FD
	public static void setShape_characterized_definition(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.setShape_characterized_definition(context, armEntity);
	}


	/**
	* Unsets/deletes data for shape_characterized_footprint_definition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetShape_characterized_definition(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.unsetShape_characterized_definition(context, armEntity);
	}


	/**
	* Sets/creates data for shape_environment attribute.
	*
	* <p>
	*  attribute_mapping shape_environment (shape_environment
	* , descriptive_representation_item);
	* 	shape_representation <=
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	{representation.name = `shape environment'}
	* 	representation.items[i] ->
	* 	representation_item
	* 	{(representation_item.name = `manufacturing')
	* 	(representation_item.name = `end user application')}
	* 	representation_item =>
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setShape_environment(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
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
	public static void unsetShape_environment(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.unsetShape_environment(context, armEntity);
	}


	/**
	* Sets/creates data for shape_material_condition attribute.
	*
	* <p>
	*  attribute_mapping shape_material_condition (shape_material_condition
	* , descriptive_representation_item);
	* 	shape_representation <=
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	{representation.name = `shape material condition'}
	* 	representation.items[i] ->
	* 	representation_item
	* 	{(representation_item.name = `maximum material condition')
	* 	(representation_item.name = `minimum material condition')
	* 	(representation_item.name = `nominal material condition')}
	* 	representation_item =>
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setShape_material_condition(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
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
	public static void unsetShape_material_condition(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
		CxPart_template_shape_model.unsetShape_material_condition(context, armEntity);
	}


	/**
	* Sets/creates data for reference_shape attribute.
	*
	* <p>
	attribute_mapping reference_shape_physical_unit_3d_shape (reference_shape
   , (*PATH*), physical_unit_3d_shape);

	   shape_representation <= 
	   representation <- 
	   representation_relationship.rep_2 
	   representation_relationship 
	   {representation_relationship 
	   representation_relationship.name = `reference shape'} 
	   representation_relationship.rep_1 -> 
	   representation => 
	   {[representation.name = `3d bound volume shape'] 
	   [representation.description != `keepout shape'] 
	   [representation <- 
	   property_definition_representation.used_representation 
	   property_definition_representation 
	   property_definition_representation.definition -> 
	   property_definition 
	   property_definition.definition -> 
	   characterized_definition 
	   characterized_definition = characterized_product_definition 
	   characterized_product_definition 
	   characterized_product_definition = product_definition 
	   product_definition => 
	   physical_unit]} 
	   shape_representation 

	end_attribute_mapping;

	attribute_mapping reference_shape_physical_unit_planar_shape (reference_shape
   , (*PATH*), physical_unit_planar_shape);

	   shape_representation <= 
	   representation <- 
	   representation_relationship.rep_2 
	   representation_relationship 
	   {representation_relationship 
	   representation_relationship.name = `reference shape'} 
	   representation_relationship.rep_1 -> 
	   representation => 
	   {[representation.name = `planar projected shape'] 
	   [representation.description != `keepout shape'] 
	   [representation <- 
	   property_definition_representation.used_representation 
	   property_definition_representation 
	   property_definition_representation.definition -> 
	   property_definition 
	   property_definition.definition -> 
	   characterized_definition 
	   characterized_definition = characterized_product_definition 
	   characterized_product_definition 
	   characterized_product_definition = product_definition 
	   product_definition => 
	   physical_unit]} 
	   shape_representation 

	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- PDR -> PD -> FD
	public static void setReference_shape(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
		//unset old values
		unsetReference_shape(context, armEntity);

		if (armEntity.testReference_shape(null)){
			EShape_representation armShape = (EShape_representation)armEntity.getReference_shape(null);
		 // RR
		 ERepresentation_relationship err = (ERepresentation_relationship)
			context.working_model.createEntityInstance(CRepresentation_relationship.definition);
		 err.setRep_2(null, armEntity);
		 err.setRep_1(null, armShape);
		 err.setName(null, "reference shape");
		}
	}


	/**
	* Unsets/deletes data for shape_characterized_footprint_definition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetReference_shape(SdaiContext context, EFootprint_definition_shape_model armEntity) throws SdaiException
	{
	  ARepresentation_relationship arr = new ARepresentation_relationship();
	  CRepresentation_relationship.usedinRep_2(null, armEntity, context.domain, arr);
	  for(int i=1;i<=arr.getMemberCount();i++){
		 ERepresentation_relationship err = arr.getByIndex(i);
		// This should be enough check to delete it
		if(err.getName(null).equals("reference shape"))
			err.deleteApplicationInstance();
	  }
	}
	
}
