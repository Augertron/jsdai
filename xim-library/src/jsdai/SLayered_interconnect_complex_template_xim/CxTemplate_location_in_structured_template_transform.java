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

package jsdai.SLayered_interconnect_complex_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SContextual_shape_positioning_xim.CxGeometric_placement;
import jsdai.SContextual_shape_positioning_xim.CxGeometric_placement_operation;
import jsdai.SGeometry_schema.EAxis2_placement_2d;
import jsdai.SGeometry_schema.EGeometric_representation_context;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_structure_schema.*;
import jsdai.SRepresentation_schema.*;


public class CxTemplate_location_in_structured_template_transform extends CTemplate_location_in_structured_template_transform implements EMappedXIMEntity, RepresentationMapAttributesImplementer
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Representation_item
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_item type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation_item type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation_item type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation_item type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_item type) throws SdaiException {
		return a0$;
	}
	// ENDOF Taken from Representation_item

	// Taken from Mapped_item

	// attribute (current explicit or supertype explicit) : mapping_source, base type: entity representation_map
/*	public static int usedinMapping_source(EMapped_item type, ERepresentation_map instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}*/
	public boolean testMapping_source2(EMapped_item type) throws SdaiException {
		return test_instance(a1);
	}
	public ERepresentation_map getMapping_source2(EMapped_item type) throws SdaiException {
		a1 = get_instance(a1);
		return (ERepresentation_map)a1;
	}
	public void setMapping_source(EMapped_item type, ERepresentation_map value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetMapping_source(EMapped_item type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeMapping_source(EMapped_item type) throws SdaiException {
		return a1$;
	}

	// ENDOF taken from Mapped_item	
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMapped_item.definition);

		setMappingConstraints(context, this);
		
		// padstack_shape
		setAssembly_shape(context, this);
      // template_shape
		setTemplate_shape(context, this);
		
      // reference_location 
		setReference_location(context, this);

		setSource(context, this);
		
		// clean ARM
		// padstack_shape
		unsetAssembly_shape(null);
		
      // template_shape
		unsetTemplate_shape(null);
		
      // reference_location 
		unsetReference_location(null);
		
		unsetSource(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// padstack_shape
		unsetAssembly_shape(context, this);
		
      // Template_shape
		unsetTemplate_shape(context, this);
		
      // reference_location 
		unsetReference_location(context, this);
		
		unsetSource(context, this);		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{mapped_item &lt;=
	*  representation_item
	*  representation_item.name = 'part template location in padstack definition transform'}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxGeometric_placement.setMappingConstraints(context, armEntity);
		armEntity.setName(null, "tlistt");
	}

	public static void unsetMappingConstraints(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
		CxGeometric_placement.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName(null);
	}


	//********** "part_template_location_in_padstack_definition_transform" attributes

	/**
	* Sets/creates data for padstack_shape attribute.
	*
	* <p>
	*  attribute_mapping padstack_shape_padstack_definition_shape (padstack_shape
	* , (*PATH*), padstack_definition_shape);
	* 	mapped_item <=
	* 	representation_item <-
	* 	representation.items[i]
	* 	{[representation
	* 	representation.name = `planar projected shape']
	* 	[representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition =>
	* 	padstack_definition]}
	* 	representation =>
	* 	shape_representation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // MI <- R {<- PDR -> PD -> PD}
	public static void setAssembly_shape(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
		//unset old values
		unsetAssembly_shape(context, armEntity);

		if (armEntity.testAssembly_shape(null))
		{
			EShape_representation armPadstack_shape = armEntity.getAssembly_shape(null);
			//EA armPadstack_shape.createAimData(context);
         ARepresentation_item items;
         if(armPadstack_shape.testItems(null))
            items = armPadstack_shape.getItems(null);
         else
            items = armPadstack_shape.createItems(null);
         if(!items.isMember(armEntity)){
        	 items.addUnordered(armEntity);
         }
		}
	}


	/**
	* Unsets/deletes data for padstack_shape attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // The simillar path exist for another attribute so removal have to be very secure
	public static void unsetAssembly_shape(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
      jsdai.SRepresentation_schema.ARepresentation representations = new jsdai.SRepresentation_schema.ARepresentation();
      jsdai.SRepresentation_schema.CRepresentation.usedinItems(null, armEntity, context.domain, representations);
      for(int i=1;i<=representations.getMemberCount();i++){
         jsdai.SRepresentation_schema.ERepresentation representation = representations.getByIndex(i);
         AProperty_definition_representation apdr = new AProperty_definition_representation();
         CProperty_definition_representation.usedinUsed_representation(null, representation, context.domain, apdr);
         for(int j=1;j<=representations.getMemberCount();j++){
            EProperty_definition_representation epdr = apdr.getByIndex(j);
            EEntity ee = epdr.getDefinition(null);
            if(ee instanceof EProperty_definition){
               EProperty_definition epd = (EProperty_definition)ee;
               if(epd.getDefinition(null) instanceof EPadstack_definition_armx){
                  representation.getItems(null).removeUnordered(armEntity);
               }
            }
         }
      }
	}


	/**
	* Sets/creates data for template_shape attribute.
	*
	* <p>
	*  attribute_mapping template_shape_part_template_planar_shape (template_shape
	* , (*PATH*), part_template_planar_shape);
	* 	mapped_item
	* 	mapped_item.mapping_source ->
	* 	representation_map
	* 	{representation_map
	* 	representation_map.mapping_origin ->
	* 	representation_item
	* 	[representation_item.name = `origin']
	* 	[representation_item =>
	* 	geometric_representation_item =>
	* 	axis2_placement_2d]}
	* 	representation_map.mapped_representation ->
	* 	{[representation
	* 	representation.name = `planar projected shape']
	* 	[representation
	* 	representation.items[i] ->
	* 	representation_item =>
	* 	geometric_representation_item =>
	* 	axis2_placement_2d]
	* 	[representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	{property_definition =>
	* 	product_definition_shape}
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition
	* 	{product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `template definition'}]}
	* 	representation =>
	* 	shape_representation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // MI -> RM -> SR {<- PDR -> PDS -> PD -> ACE }
	public static void setTemplate_shape(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
		//unset old values
		unsetTemplate_shape(context, armEntity);

		if (armEntity.testTemplate_shape(null))
		{
			EPart_template_planar_shape_model armTemplate_shape = (EPart_template_planar_shape_model)armEntity.getTemplate_shape(null);
			//EA armTemplate_shape.createAimData(context);
         // Get origin
         EAxis2_placement_2d originARM = CxAP210ARMUtilities.getOriginFromShape(armTemplate_shape);
		 //EA originARM.createAimData(context);
			// Cache results, since this is one of the bottlenecks in AIM-AIM conversion
			ERepresentation_map repMap = null;
			Object rmCandidate = armTemplate_shape.getTemp("RM");
			if(rmCandidate == null){
				ARepresentation_map rep_maps = new ARepresentation_map(); 
				CRepresentation_map.usedinMapped_representation(null, armTemplate_shape, context.domain, rep_maps);
				if(rep_maps.getMemberCount() > 0){
					SdaiIterator iter = rep_maps.createIterator();
					while(iter.next()){
						ERepresentation_map rep_candidate = rep_maps.getCurrentMember(iter);
						if((rep_candidate.testMapping_origin(null))&&(rep_candidate.getMapping_origin(null) == originARM)){
							repMap = rep_candidate;
							break;
						}
					}
				}
				if(repMap == null){
					repMap = (ERepresentation_map)
						context.working_model.createEntityInstance(CRepresentation_map.definition);
					repMap.setMapped_representation(null, armTemplate_shape);
					repMap.setMapping_origin(null, originARM);
				}
				armTemplate_shape.setTemp("RM", repMap);
            // System.err.println(" RM "+repMap+" ORIGIN "+originARM);
			}
			else{
				repMap = (ERepresentation_map)rmCandidate;
			}
         // MI -> RM
         armEntity.setMapping_source(null, repMap);
		}
	}


	/**
	* Unsets/deletes data for template_shape attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTemplate_shape(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
      armEntity.unsetMapping_source(null);
	}


	/**
	* Sets/creates data for reference_location attribute.
	*
	* <p>
	*  attribute_mapping reference_location_part_template_location_in_padstack_definition (reference_location
	* , (*PATH*), part_template_location_in_padstack_definition);
	* 	mapped_item <=
	* 	representation_item <-
	* 	representation.items[i]
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	{property_definition =>
	* 	product_definition_shape}
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `part template location in padstack definition'}
	* 	product_definition_relationship =>
	* 	product_definition_usage =>
	* 	assembly_component_usage
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
      // MI <- SR <- SDR -> PDS -> ACU
	public static void setReference_location(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
		//unset old values
		unsetReference_location(context, armEntity);

		if (armEntity.testReference_location(null))
		{
			ETemplate_location_in_structured_template armReference_location = armEntity.getReference_location(null);
         // PDS -> ACU
         LangUtils.Attribute_and_value_structure[] pdsStructure = {
            new LangUtils.Attribute_and_value_structure(
            CProduct_definition_shape.attributeDefinition(null),
				armReference_location)
         };
         EProduct_definition_shape epds = (EProduct_definition_shape)
            LangUtils.createInstanceIfNeeded(context, CProduct_definition_shape.definition, pdsStructure);
         if(!epds.testName(null))
            epds.setName(null, "");
         // PDR -> PDS
         LangUtils.Attribute_and_value_structure[] pdrStructure = {
            new LangUtils.Attribute_and_value_structure(
            CShape_definition_representation.attributeDefinition(null),
            epds)
         };
         EProperty_definition_representation epdr = (EProperty_definition_representation)
            LangUtils.createInstanceIfNeeded(context, CShape_definition_representation.definition, pdrStructure);
         jsdai.SRepresentation_schema.ERepresentation representation;
         // R <- PDR
         if((epdr.testUsed_representation(null))&&(epdr.getUsed_representation(null) instanceof EShape_representation)){
			representation = epdr.getUsed_representation(null);
		 }else{
            representation = CxAP210ARMUtilities.createRepresentation(context, CShape_representation.definition, "", false);
            EGeometric_representation_context egrc = 
            	CxAP210ARMUtilities.createGeometric_representation_context(context, "", "", 2, true);
            representation.setContext_of_items(null, egrc);
            epdr.setUsed_representation(null, representation);
         }
         // MI <- R
         ARepresentation_item items;
         if(representation.testItems(null))
            items = representation.getItems(null);
         else
            items = representation.createItems(null);
         if(!items.isMember(armEntity)){         
        	 items.addUnordered(armEntity);
         }
		}
	}


	/**
	* Unsets/deletes data for reference_location attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetReference_location(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
      jsdai.SRepresentation_schema.ARepresentation representations = new jsdai.SRepresentation_schema.ARepresentation();
      jsdai.SRepresentation_schema.CRepresentation.usedinItems(null, armEntity, context.domain, representations);
      for(int i=1;i<=representations.getMemberCount();i++){
         jsdai.SRepresentation_schema.ERepresentation representation = representations.getByIndex(i);
         AProperty_definition_representation apdr = new AProperty_definition_representation();
         CProperty_definition_representation.usedinUsed_representation(null, representation, context.domain, apdr);
         for(int j=1;j<=apdr.getMemberCount();){
            EProperty_definition_representation epdr = apdr.getByIndex(j);
            EEntity ee = epdr.getDefinition(null);
            if(ee instanceof EProduct_definition_shape){
               EProperty_definition epd = (EProperty_definition)ee;
               if(epd.getDefinition(null) instanceof EAssembly_component_usage){
                  EAssembly_component_usage
                     eacu = (EAssembly_component_usage)
                     epd.getDefinition(null);
                  if(eacu.getName(null).equals("part template location in padstack definition")){
                     representation.getItems(null).removeUnordered(armEntity);
                     continue;
                  }
               }
            }
            j++;
         }
      }
	}

	/**
	* Sets/creates data for source attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setSource(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
		CxGeometric_placement_operation.setSource(context, armEntity);
	}

	public static void unsetSource(SdaiContext context, ETemplate_location_in_structured_template_transform armEntity) throws SdaiException
	{
		CxGeometric_placement_operation.unsetSource(context, armEntity);
	}

	

}
