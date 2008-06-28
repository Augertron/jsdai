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

package jsdai.SLayered_interconnect_module_design_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SLayered_interconnect_module_design_mim.CStructured_layout_component_sub_assembly_relationship;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SPart_template_2d_shape_xim.ETemplate_location_in_structured_template;
import jsdai.SPart_template_shape_with_parameters_xim.EPart_template_shape_model;
import jsdai.SPhysical_unit_design_view_xim.CxNext_assembly_usage_occurrence_relationship_armx;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_structure_schema.EProduct_definition_occurrence_relationship;

public class CxStructured_layout_component_sub_assembly_relationship_armx extends CStructured_layout_component_sub_assembly_relationship_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Product_definition_occurrence_relationship
	// attribute (current explicit or supertype explicit) : occurrence, base type: entity product_definition
/*	public static int usedinOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_instance(a8);
	}
	public jsdai.SProduct_definition_schema.EProduct_definition getOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return (jsdai.SProduct_definition_schema.EProduct_definition)get_instance(a8);
	}*/
	public void setOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition value) throws SdaiException {
		a8 = set_instance(a8, value);
	}
	public void unsetOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a8$;
	}
	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(EProduct_definition_occurrence_relationship type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a6$;
	}
	// END of taken from PDOR
	
	// Taken from PDR
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a0$;
	}

	//<01> generating methods for consolidated attribute:  name
/*	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a1$;
	}

	
	// ENDOF taken from PDR
	
	// From Property_definition
	// -2- methods for SELECT attribute: definition
/* Made is renamed from property_definition.definition	
	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a12$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a12);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a12 = get_instance_select(a12);
		return (EEntity)a12;
	}

	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a12 = set_instance(a12, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a12 = unset_instance(a12);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a12$;
	}
*/	
	// ENDOF from Property_definition

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStructured_layout_component_sub_assembly_relationship.definition);

		setMappingConstraints(context, this);
		
        // first_location : template_location_in_structured_template;
		setFirst_location(context, this);
		
		setSecond_location(context, this);
		
        // second_location : OPTIONAL template_location_in_structured_template;
		// setSecond_location(context, this);
		
        // overriding_shape : OPTIONAL part_template_shape_model;
		setOverriding_shape(context, this);
		
		// design_specific_placement
		setDesign_specific_placement(context, this);
		
		CxNext_assembly_usage_occurrence_relationship_armx.processAssemblyTrick(context, this);
		
        // first_location : template_location_in_structured_template;
		unsetFirst_location(null);
		
		unsetSecond_location(null);
		
        // second_location : OPTIONAL template_location_in_structured_template;
		// unsetSecond_location(null);
		
        // overriding_shape : OPTIONAL part_template_shape_model;
		unsetOverriding_shape(null);
		
		unsetDesign_specific_placement(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
	
        // first_location : template_location_in_structured_template;
		unsetFirst_location(context, this);
		
		unsetSecond_location(context, this);
		
        // second_location : OPTIONAL template_location_in_structured_template;
		// unsetSecond_location(context, this);
		
        // overriding_shape : OPTIONAL part_template_shape_model;
		unsetOverriding_shape(context, this);
		
		// design_specific_placement
		unsetDesign_specific_placement(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxNext_assembly_usage_occurrence_relationship_armx.setMappingConstraints(context, armEntity);
//		if(!armEntity.testName((EProduct_definition_relationship)null)){
			armEntity.setName((EProduct_definition_relationship)null, "");
//		}
//		if(!armEntity.testId((EProduct_definition_relationship)null)){
			armEntity.setId((EProduct_definition_relationship)null, armEntity.getRelated_product_definition(null).getId(null));
//		}
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.unsetMappingConstraints(context, armEntity);
	}
	
	/**
	* Sets/creates data for First_location mapping constraints.
	attribute_mapping first_location(first_location, $PATH, Template_location_in_structured_template);
		structured_layout_component_sub_assembly_relationship <=
		next_assembly_usage_occurrence_relationship <=
		next_assembly_usage_occurrence <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		characterized_product_definition = product_definition_relationship
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'first location'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition = characterized_product_definition
		characterized_product_definition = product_definition_relationship
		product_definition_relationship
		product_definition_relationship =>
		product_definition_usage =>
		assembly_component_usage
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SLCSAR <- PD <- PDR -> PD -> ACU
	public static void setFirst_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		unsetFirst_location(context, armEntity);
		if(armEntity.testFirst_location(null)){
			ETemplate_location_in_structured_template location = armEntity.getFirst_location(null);
			setLocation(context, armEntity, location, "first location");
			
		}
	}

	public static void setSecond_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		unsetSecond_location(context, armEntity);
		if(armEntity.testSecond_location(null)){
			ETemplate_location_in_structured_template location = armEntity.getSecond_location(null);
			setLocation(context, armEntity, location, "second location");
			
		}
	}
	
	/**
	 * @param context
	 * @param armEntity
	 * @param location
	 * @throws SdaiException
	 */
	private static void setLocation(SdaiContext context, 
			EStructured_layout_component_sub_assembly_relationship_armx armEntity, 
			ETemplate_location_in_structured_template location,
			String name) throws SdaiException {
		// PDR -> ACU
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, location, context.domain, apd);
		EProperty_definition epd = null;
		if(apd.getMemberCount() > 0){
			epd = apd.getByIndex(1);
		}else{
			epd = (EProperty_definition)context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, location);
			epd.setName(null, "");
		}
		// SLCSAR <- PD
		AProperty_definition apd2 = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd2);
		EProperty_definition epd2 = null;
		if(apd2.getMemberCount() > 0){
			epd2 = apd2.getByIndex(1);
		}else{
			epd2 = (EProperty_definition)context.working_model.createEntityInstance(CProperty_definition.definition);
			epd2.setDefinition(null, armEntity);
			epd2.setName(null, "");
		}
		
		EProperty_definition_relationship epdr = (EProperty_definition_relationship)
			context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
		epdr.setRelated_property_definition(null, epd2);
		epdr.setRelating_property_definition(null, epd);
		epdr.setName(null, name);
		epdr.setDescription(null, "");
	}

	public static void unsetFirst_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		unsetLocation(context, armEntity, "first location");
	}

	public static void unsetSecond_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		unsetLocation(context, armEntity, "second location");
	}

	/**
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	private static void unsetLocation(SdaiContext context, 
			EStructured_layout_component_sub_assembly_relationship_armx armEntity,
			String name) throws SdaiException {
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator apdIter = apd.createIterator();
		while(apdIter.next()){
			EProperty_definition epd = apd.getCurrentMember(apdIter);
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apdr);
			SdaiIterator apdrIter = apdr.createIterator();
			while(apdrIter.next()){
				EProperty_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
				if((epdr.testName(null))&&(epdr.getName(null).equals(name))){
					epdr.deleteApplicationInstance();
				}
			}			
		}
	}


	/**
	* Sets/creates data for second_location mapping constraints.
	attribute_mapping second_location(second_location, $PATH, Template_location_in_structured_template);
		structured_layout_component_sub_assembly_relationship <=
		property_definition
		property_definition.definition ->
		product_definition_relationship
		{product_definition_relationship.name = 'tlist'}
		product_definition_relationship =>
		product_definition_usage =>
		assembly_component_usage
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Made is renamed from property_definition.definition	
	// SLCSAR <- PD <- PDR -> PD -> ACU
	public static void setSecond_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		unsetSecond_location(context, armEntity);
		if(armEntity.testSecond_location(null)){
			ETemplate_location_in_structured_template location = armEntity.getSecond_location(null);
			armEntity.setDefinition(null, location);
		}
	}
	
	public static void unsetSecond_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		armEntity.unsetDefinition(null);
	}
*/	
	/**
	* Sets/creates data for First_location mapping constraints.
	attribute_mapping overriding_shape(overriding_shape, $PATH, Part_template_shape_model);
		structured_layout_component_sub_assembly_relationship <=
		next_assembly_usage_occurrence_relationship <=
		next_assembly_usage_occurrence <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		characterized_product_definition = product_definition_relationship
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition
		represented_definition = property_definition
		represented_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation =>
		shape_representation

	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SLCSAR <- PD <- PDR -> SR
	public static void setOverriding_shape(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		unsetOverriding_shape(context, armEntity);
		if(armEntity.testOverriding_shape(null)){
			EPart_template_shape_model shape = armEntity.getOverriding_shape(null);
			// SLCSAR <- PD
			LangUtils.Attribute_and_value_structure[] structure2 = {
				new LangUtils.Attribute_and_value_structure(
				   CProperty_definition.attributeDefinition(null), 
				   armEntity)};
			EProperty_definition epd2 = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, structure2);
			if(!epd2.testName(null)){
				epd2.setName(null, "");
			}
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setUsed_representation(null, shape);
			epdr.setDefinition(null, epd2);
		}
	}

	public static void unsetOverriding_shape(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator apdIter = apd.createIterator();
		while(apdIter.next()){
			EProperty_definition epd = apd.getCurrentMember(apdIter);
			AProperty_definition_representation apdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
			SdaiIterator apdrIter = apdr.createIterator();
			while(apdrIter.next()){
				EProperty_definition_representation epdr = apdr.getCurrentMember(apdrIter);
				if((epdr.testUsed_representation(null))&&(epdr.getUsed_representation(null) instanceof EShape_representation)){
					epdr.deleteApplicationInstance();
				}
			}			
		}
	}
	

	/**
	* Sets/creates data for design_specific_placement mapping constraints.
	attribute_mapping design_specific_placement(design_specific_placement, $PATH);
		structured_layout_component_sub_assembly_relationship <=
		next_assembly_usage_occurrence_relationship <=
		next_assembly_usage_occurrence <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		product_definition_relationship.name
		(product_definition_relationship.name = 'design specific placement') 
		(product_definition_relationship.name = 'design independent placement') 
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDesign_specific_placement(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		unsetDesign_specific_placement(context, armEntity);
		if(armEntity.testDesign_specific_placement(null)){
			boolean placement = armEntity.getDesign_specific_placement(null);
			if(placement){
				armEntity.setName((EProduct_definition_relationship)null, "design specific placement");
			}else{
				armEntity.setName((EProduct_definition_relationship)null, "design independent placement");
			}
		}
	}
	
	public static void unsetDesign_specific_placement(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		armEntity.unsetName((EProduct_definition_relationship)null);
	}

	
}
