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

package jsdai.SAssembly_module_usage_view_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SAssembly_module_usage_view_mim.CAssembly_module_terminal;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SFunctional_assignment_to_part_xim.CxPart_terminal;
import jsdai.SPackaged_connector_model_xim.EPackaged_part_interface_terminal;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_structure_schema.ENext_assembly_usage_occurrence;

public class CxAssembly_module_terminal_armx extends CAssembly_module_terminal_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a1$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAssembly_module_terminal.definition);

		setMappingConstraints(context, this);
		
		// Part_feature
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);

		// reference_terminal
		setReference_terminal(context, this);
		
      // related_connector
		setRelated_connector(context, this);
		
      // terminal_connection_zone		
		setConnection_area(context, this);
		
		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);
		
		// reference_terminal
		unsetReference_terminal(null);
		
      // related_connector
		unsetRelated_connector(null);
		
      // terminal_connection_zone		
		unsetConnection_area(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);

			// reference_terminal
			unsetReference_terminal(context, this);
			
	      // related_connector
			unsetRelated_connector(context, this);
			
	      // terminal_connection_zone		
			unsetConnection_area(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  assembly_module_terminal &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.of_shape -&gt;
	 *  product_definition_shape &lt;=
	 *  property_definition
	 *  property_definition.definition -&gt;
	 *  characterized_definition
	 *  characterized_definition = characterized_product_definition
	 *  characterized_product_definition
	 *  characterized_product_definition = product_definition
	 *  product_definition
	 *  product_definition.name = 'assembly module'}
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
			EAssembly_module_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_terminal.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EAssembly_module_terminal_armx armEntity) throws SdaiException {
		CxPart_terminal.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for material_state_change.
	 * 
	 * <p>
	 *  shape_aspect &lt;-
	 *  shape_aspect_relationship.related_shape_aspect
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'precedent feature'}
	 *  shape_aspect_relationship.relating_shape_aspect -&gt;
	 *  shape_aspect
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
////////////////////////////////////////////////////////////////////
	/**
	* Sets/creates data for material_state_change attribute.
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setMaterial_state_change(context, armEntity);
	}


	/**
	* Unsets/deletes data for material_state_change attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetMaterial_state_change(context, armEntity);		
	}


	/**
	* Sets/creates data for precedent_feature attribute.
	*
	*  attribute_mapping precedent_feature_part_feature (precedent_feature
	* , (*PATH*), part_feature);
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setPrecedent_feature(context, armEntity);
	}


	/**
	* Unsets/deletes data for precedent_feature attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetPrecedent_feature(context, armEntity);		
	}

	//********** "assembly_module_terminal" attributes

	/**
	* Sets/creates data for reference_terminal attribute.
	*
	* <p>
	*  attribute_mapping reference_terminal_packaged_part_interface_terminal (reference_terminal
	* , (*PATH*), packaged_part_interface_terminal);
	* 	assembly_module_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `reference terminal'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect
	* 	{shape_aspect
	* 	shape_aspect.description = `interface terminal'}
	* 	shape_aspect =>
	* 	packaged_part_terminal
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setReference_terminal(SdaiContext context, EAssembly_module_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetReference_terminal(context, armEntity);

		if (armEntity.testReference_terminal(null))
		{

			EPackaged_part_interface_terminal armReference_terminal = armEntity.getReference_terminal(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "reference terminal");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armReference_terminal);
		}
	}


	/**
	* Unsets/deletes data for reference_terminal attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetReference_terminal(SdaiContext context, EAssembly_module_terminal_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("reference terminal")) {
				relationship.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for related_connector attribute.
	*
	* <p>
	* attribute_mapping related_connector_next_higher_assembly_relationship (related_connector
	* , (*PATH*), next_higher_assembly_relationship);
	*  assembly_module_terminal <= 
	*  shape_aspect <- 
	*  shape_aspect_relationship.related_shape_aspect 
	*  shape_aspect_relationship 
	*  {shape_aspect_relationship 
	*  shape_aspect_relationship.name = `related connector'} 
	*  shape_aspect_relationship.relating_shape_aspect -> 
	*  shape_aspect.of_shape -> 
	*  product_definition_shape <= 
	*  property_definition 
	*  property_definition.definition -> 
	*  characterized_definition 
	*  characterized_definition = characterized_product_definition 
	*  characterized_product_definition 
	*  characterized_product_definition = product_definition_relationship 
	*  product_definition_relationship => 
	*  {[product_definition_relationship 
	*  product_definition_relationship.relating_product_definition ->  
	*  {product_definition 
	*  product_definition.frame_of_reference -> 
	*  product_definition_context <= 
	*  application_context_element 
	*  application_context_element.name = `physical design usage'} 
	*  product_definition => 
	*  physical_unit] 
	*  [product_definition_relationship 
	*  product_definition_relationship.related_product_definition -> 
	*  {product_definition => 
	*  {product_definition 
	*  product_definition.description = `packaged connector component'} 
	*  component_definition => 
	*  packaged_component}]} 
	*  product_definition_usage => 
	*  assembly_component_usage
	* end_attribute_mapping; 	
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// AMT <- SAR -> SA -> PDS -> ACU.ing -> PU; ed -> PC
	public static void setRelated_connector(SdaiContext context, EAssembly_module_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRelated_connector(context, armEntity);

		if (armEntity.testRelated_connector(null))
		{
			ENext_assembly_usage_occurrence_relationship_armx 
				armConnector = armEntity.getRelated_connector(null);
	      // PDS
	      LangUtils.Attribute_and_value_structure[] pdsStructure = {
	         new LangUtils.Attribute_and_value_structure(
	         CProduct_definition_shape.attributeDefinition(null),
	         armConnector)
	      };
	      EProduct_definition_shape epds = (EProduct_definition_shape)
	         LangUtils.createInstanceIfNeeded(context, CProduct_definition_shape.definition, pdsStructure);
	      if (!epds.testName(null)) {
	         epds.setName(null, "");
	      }
         // SA -> PDS
         LangUtils.Attribute_and_value_structure[] saStructure = {
            new LangUtils.Attribute_and_value_structure(
            CShape_aspect.attributeOf_shape(null), epds)};
         EShape_aspect esa = (EShape_aspect)
            LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition, saStructure);
         if(!esa.testName(null))
            esa.setName(null, "");
         if(!esa.testProduct_definitional(null))
            esa.setProduct_definitional(null, ELogical.UNKNOWN);
         // SAR - always newly created
         EShape_aspect_relationship
            shape_aspect_relationship = (EShape_aspect_relationship)
            (context.working_model.createEntityInstance(CShape_aspect_relationship.class));
         shape_aspect_relationship.setName(null, "related connector");
         shape_aspect_relationship.setRelating_shape_aspect(null, esa);
         shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
		}
	}


	/**
	* Unsets/deletes data for related_connector attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRelated_connector(SdaiContext context, EAssembly_module_terminal_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("related connector")) {
				relationship.deleteApplicationInstance();
			}
		}
	}

	/**
	* Sets/creates data for terminal_connection_zone attribute.
	*
	* <p>
	*  attribute_mapping terminal_connection_zone_connection_zone (terminal_connection_zone
	* , (*PATH*), connection_zone);
	* 	assembly_module_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `terminal connection zone'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect
	* 	{shape_aspect
	* 	shape_aspect.description = `connection zone'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EAssembly_module_terminal_armx armEntity) throws SdaiException
	{
		CxShape_feature.setConnection_area(context, armEntity);
	}


	/**
	* Unsets/deletes data for terminal_connection_zone attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EAssembly_module_terminal_armx armEntity) throws SdaiException
	{
		CxShape_feature.unsetConnection_area(context, armEntity);
	}
	
	
	
}