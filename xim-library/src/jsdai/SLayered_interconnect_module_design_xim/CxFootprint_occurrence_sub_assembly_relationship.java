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
import jsdai.SLayered_interconnect_module_design_mim.CFootprint_occurrence_product_definition_relationship;
// import jsdai.SAp210_electronic_assembly_interconnect_and_packaging_design_xim.CFootprint_occurrence_product_definition_relationship$next_assembly_usage_occurrence;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_structure_schema.*;


public class CxFootprint_occurrence_sub_assembly_relationship extends CFootprint_occurrence_sub_assembly_relationship implements EMappedXIMEntity
{

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
	}
	public void setOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition value) throws SdaiException {
		a8 = set_instance(a8, value);
	}
	public void unsetOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a8$;
	}
*/	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return get_string(a6);
	}
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
*/	
	// Taken from PDR
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a0);
	}
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a0$;
	}
*/
	//<01> generating methods for consolidated attribute:  name
/*	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a1);
	}
	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a1$;
	}
*/
	
	// ENDOF taken from PDR

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		
		setTemp("AIM", CFootprint_occurrence_product_definition_relationship.definition);

		setMappingConstraints(context, this);

		// sub_assembly_reference_designation
		setSub_assembly_reference_designation(context, this);
		
      // terminal_location 		
		setTerminal_location(context, this);
		
		// Clean ARM
		// sub_assembly_reference_designation
		unsetSub_assembly_reference_designation(null);
		
      // terminal_location 		
		unsetTerminal_location(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// sub_assembly_reference_designation
		unsetSub_assembly_reference_designation(context, this);
		
      // terminal_location 		
		unsetTerminal_location(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	footprint_occurrence_product_definition_relationship &lt;=
	*  product_definition_relationship
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EFootprint_occurrence_sub_assembly_relationship armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EFootprint_occurrence_sub_assembly_relationship armEntity) throws SdaiException
	{
	}

	/* Sets/creates data for sub_assembly_reference_designation attribute.
	*
	* <p>
	* attribute_mapping sub_assembly_reference_designation_part_template_location_in_padstack_definition (sub_assembly_reference_designation
	* , (*PATH*), part_template_location_in_padstack_definition);
	*  footprint_occurrence_shape_aspect_relationship <= 
	*  shape_aspect_relationship <- 
	*  property_definition.definition 
	*  property_definition <- 
	*  property_definition_relationship.related_property_definition 
	*  property_definition_relationship 
	*  {property_definition_relationship 
	*  property_definition_relationship.name = `terminal location'} 
	*  property_definition_relationship.relating_property_definition -> 
	*  property_definition  
	*  property_definition.definition -> 
	*  characterized_definition = characterized_product_definition 
	*  characterized_product_definition = product_definition_relationship 
	*  product_definition_relationship => 
	*  {product_definition_relationship 
	*  product_definition_relationship.name = `part template location in padstack definition'} 
	*  product_definition_usage => 
	*  assembly_component_usage 
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// FOSAR <- PD <- PDR -> PD -> ACU
	public static void setSub_assembly_reference_designation(SdaiContext context, EFootprint_occurrence_sub_assembly_relationship armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_armx.setSecond_location(context, armEntity);
		//unset old values
/*		unsetSub_assembly_reference_designation(context, armEntity);

		if (armEntity.testSub_assembly_reference_designation(null))
		{
			ETemplate_location_in_structured_template armSub_assembly_reference_designation = 
				(ETemplate_location_in_structured_template)armEntity.getSub_assembly_reference_designation(null);
			// PD -> ACU
			LangUtils.Attribute_and_value_structure[] pdStructure2 = {
			   new LangUtils.Attribute_and_value_structure(
			   CProperty_definition.attributeDefinition(null),
				armSub_assembly_reference_designation)
			};
			EProperty_definition epd2 = (EProperty_definition)
			   LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure2);
			if (!epd2.testName(null)) {
			   epd2.setName(null, "");
			}
			// FOSAR <- PD
			LangUtils.Attribute_and_value_structure[] pdStructure1 = {
			   new LangUtils.Attribute_and_value_structure(
			   CProperty_definition.attributeDefinition(null),
			   armEntity)
			};
			EProperty_definition epd1 = (EProperty_definition)
			   LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure1);
			if (!epd1.testName(null)) {
			   epd1.setName(null, "");
			}
			// PD <- PDR -> PD
         EProperty_definition_relationship epdr = (EProperty_definition_relationship) 
         	context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
         epdr.setRelated_property_definition(null, epd1);
         epdr.setRelating_property_definition(null, epd2);
         epdr.setName(null, "terminal location");
		}
*/		
	}


	/**
	* Unsets/deletes data for sub_assembly_reference_designation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetSub_assembly_reference_designation(SdaiContext context, EFootprint_occurrence_sub_assembly_relationship armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_armx.unsetSecond_location(context, armEntity);
/*      AProperty_definition apd = new AProperty_definition();
      CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
      for(int i=1;i<=apd.getMemberCount();i++){
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, apd.getByIndex(i), context.domain, apdr);
			for(int j=1;j<=apdr.getMemberCount();j++){
				EProperty_definition_relationship epdr = apdr.getByIndex(j);
				if(epdr.getName(null).equals("terminal location")){
					EAssembly_component_usage eacu = (EAssembly_component_usage)epdr.getRelating_property_definition(null).getDefinition(null);
					if(eacu.getName(null).equals("part template location in padstack definition"))
						epdr.deleteApplicationInstance(); 
				}
			}
      }
*/       
	}


	/**
	* Sets/creates data for terminal_location attribute.
	*
	* <p>
	* attribute_mapping terminal_location_padstack_location_in_footprint_definition (terminal_location
	* , (*PATH*), padstack_location_in_footprint_definition);
	*  footprint_occurrence_shape_aspect_relationship <= 
	*  shape_aspect_relationship <- 
	*  property_definition.definition 
	*  property_definition <- 
	*  property_definition_relationship.related_property_definition 
	*  property_definition_relationship 
	*  {property_definition_relationship  
	*  property_definition_relationship.name = `terminal location'} 
	*  property_definition_relationship.relating_property_definition -> 
	*  property_definition 
	*  property_definition.definition -> 
	*  characterized_definition = characterized_product_definition 
	*  characterized_product_definition = product_definition_relationship 
	*  product_definition_relationship => 
	*  {product_definition_relationship 
	*  product_definition_relationship.name = `padstack location in footprint definition'} 
	*  product_definition_usage => 
	*  assembly_component_usage 
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTerminal_location(SdaiContext context, EFootprint_occurrence_sub_assembly_relationship armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_armx.setFirst_location(context, armEntity);
		//unset old values
/*		unsetTerminal_location(context, armEntity);

		if (armEntity.testTerminal_location(null))
		{
			EPart_feature_based_template_location armSub_assembly_reference_designation = armEntity.getTerminal_location(null);
			// PD -> ACU
			LangUtils.Attribute_and_value_structure[] pdStructure2 = {
			   new LangUtils.Attribute_and_value_structure(
			   CProperty_definition.attributeDefinition(null),
				armSub_assembly_reference_designation)
			};
			EProperty_definition epd2 = (EProperty_definition)
			   LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure2);
			if (!epd2.testName(null)) {
			   epd2.setName(null, "");
			}
			// FOSAR <- PD
			LangUtils.Attribute_and_value_structure[] pdStructure1 = {
			   new LangUtils.Attribute_and_value_structure(
			   CProperty_definition.attributeDefinition(null),
			   armEntity)
			};
			EProperty_definition epd1 = (EProperty_definition)
			   LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure1);
			if (!epd1.testName(null)) {
			   epd1.setName(null, "");
			}
			// PD <- PDR -> PD
		 EProperty_definition_relationship epdr = (EProperty_definition_relationship) 
			context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
		 epdr.setRelated_property_definition(null, epd1);
		 epdr.setRelating_property_definition(null, epd2);
		 epdr.setName(null, "terminal location");
		}
*/		
	}


	/**
	* Unsets/deletes data for terminal_location attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTerminal_location(SdaiContext context, EFootprint_occurrence_sub_assembly_relationship armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_armx.unsetFirst_location(context, armEntity);
/*	  
		AProperty_definition apd = new AProperty_definition();
	  CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
	  for(int i=1;i<=apd.getMemberCount();i++){
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, apd.getByIndex(i), context.domain, apdr);
			for(int j=1;j<=apdr.getMemberCount();j++){
				EProperty_definition_relationship epdr = apdr.getByIndex(j);
				if(epdr.getName(null).equals("terminal location")){
					EAssembly_component_usage eacu = (EAssembly_component_usage)epdr.getRelating_property_definition(null).getDefinition(null);
					// Need to invoke in order to make sure magic strings are set
					if(eacu instanceof EMappedXIMEntity){
						EMappedXIMEntity instance = (EMappedXIMEntity)eacu;
						instance.createAimData(context);
					}
					if(eacu.getName(null).equals("padstack location in footprint definition"))
						epdr.deleteApplicationInstance(); 
					}
			}
	  }
*/	   
	}
	

}
