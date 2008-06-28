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
import jsdai.SFabrication_technology_xim.AStratum_technology_occurrence_link_armx;
import jsdai.SFabrication_technology_xim.EPassage_technology_armx;
import jsdai.SFabrication_technology_xim.EStratum_technology_occurrence_armx;
import jsdai.SFabrication_technology_xim.EStratum_technology_occurrence_link_armx;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SPart_template_2d_shape_mim.CPassage_technology_allocation_to_stack_model;
import jsdai.SProduct_property_definition_schema.EProperty_definition;

public class CxPassage_technology_allocation_to_stack_model_armx extends CPassage_technology_allocation_to_stack_model_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a1$;
	}

	
	// Taken from Product_definition_relationship
	/// methods for attribute: id, base type: STRING
/*	
	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
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

	/// methods for attribute: name, base type: STRING
	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
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
	// ENDOF Taken from Product_definition_relationship

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPassage_technology_allocation_to_stack_model.definition);

		setMappingConstraints(context, this);
		
	    setSingle_stratum_passage_location(context, this);
        setStratum_technology_sequence(context, this);
        setAssociated_stackup(context, this);
        setAllocated_technology(context, this);

	    unsetSingle_stratum_passage_location(null);
        unsetStratum_technology_sequence(null);
        unsetAssociated_stackup(null);		
        unsetAllocated_technology(null); 
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
	    unsetSingle_stratum_passage_location(context, this);
        unsetStratum_technology_sequence(context, this);
        unsetAssociated_stackup(context, this);
        unsetAllocated_technology(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{[stratum_specific_part_template_location_in_padstack_definition &lt;=
	*  assembly_component_usage &lt;=
	*  product_definition_usage &lt;=
	*  product_definition_relationship
	*  product_definition_relationship.name = 'part template location in padstack definition']
	*  [stratum_specific_part_template_location_in_padstack_definition &lt;=
	*  property_definition]}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
//		if(!armEntity.testName(null)){
			armEntity.setName(null, "");
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for single_stratum_passage_location constraints.
	*
	* <p>
		attribute_mapping single_stratum_passage_location(single_stratum_passage_location, $PATH, Stratum_technology_occurrence_armx);
			passage_technology_allocation_to_stack_model <=
			property_definition <-
			property_definition_relationship.related_property_definition
			{property_definition_relationship
			property_definition_relationship.name = 'single stratum passage location'}
			property_definition_relationship.relating_property_definition ->
			property_definition =>
			stratum_technology_occurrence
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PTATSM <- PDR -> STO
	public static void setSingle_stratum_passage_location(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		unsetSingle_stratum_passage_location(context, armEntity);
		if(armEntity.testSingle_stratum_passage_location(null)){
			EStratum_technology_occurrence_armx esto = armEntity.getSingle_stratum_passage_location(null);
			EProperty_definition_relationship epdr = (EProperty_definition_relationship)
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setRelated_property_definition(null, armEntity);
			epdr.setRelating_property_definition(null, esto);
			epdr.setName(null, "single stratum passage location");
		}
	}

	public static void unsetSingle_stratum_passage_location(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
		SdaiIterator apdrIter = apdr.createIterator();
		while(apdrIter.next()){
			EProperty_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
			if((epdr.testName(null))&&(epdr.getName(null).equals("single stratum passage location"))){
				epdr.deleteApplicationInstance();
			}
		}			
	}

	/**
	* Sets/creates data for stratum_technology_sequence constraints.
	*
	* <p>
		attribute_mapping stratum_technology_sequence(stratum_technology_sequence, $PATH, Stratum_technology_occurrence_link_armx);
			passage_technology_allocation_to_stack_model <=
			property_definition <-
			property_definition_relationship.related_property_definition
			{property_definition_relationship
			property_definition_relationship.name = 'stratum technology sequence'}
			property_definition_relationship.relating_property_definition ->
			property_definition =>
			stratum_technology_occurrence_link
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PTATSM <- PDR -> STO
	public static void setStratum_technology_sequence(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		unsetStratum_technology_sequence(context, armEntity);
		if(armEntity.testStratum_technology_sequence(null)){
			AStratum_technology_occurrence_link_armx asto = armEntity.getStratum_technology_sequence(null);
			SdaiIterator iter = asto.createIterator();
			while(iter.next()){
				EStratum_technology_occurrence_link_armx esto = asto.getCurrentMember(iter); 
				EProperty_definition_relationship epdr = (EProperty_definition_relationship)
					context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
				epdr.setRelated_property_definition(null, armEntity);
				epdr.setRelating_property_definition(null, esto);
				epdr.setName(null, "stratum technology sequence");
				epdr.setDescription(null, "");
			}
		}
	}

	public static void unsetStratum_technology_sequence(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
		SdaiIterator apdrIter = apdr.createIterator();
		while(apdrIter.next()){
			EProperty_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
			if((epdr.testName(null))&&(epdr.getName(null).equals("stratum technology sequence"))){
				epdr.deleteApplicationInstance();
			}
		}			
	}

	/**
	* Sets/creates data for associated_stackup constraints.
	*
	* <p>
		attribute_mapping associated_stackup(associated_stackup, $PATH, Design_stack_model_armx);
			passage_technology_allocation_to_stack_model <=
			property_definition <-
			property_definition_relationship.related_property_definition
			{property_definition_relationship
			property_definition_relationship.name = 'associated stackup'}
			property_definition_relationship.relating_property_definition ->
			property_definition =>
			product_definition_shape =>
			part_template_definition =>
			stratum_stack_model =>
			design_stack_model
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PTATSM <- PDR -> STO
	public static void setAssociated_stackup(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		unsetAssociated_stackup(context, armEntity);
		if(armEntity.testAssociated_stackup(null)){
			EDesign_stack_model_armx edsma = armEntity.getAssociated_stackup(null);
			EProperty_definition_relationship epdr = (EProperty_definition_relationship)
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setRelated_property_definition(null, armEntity);
			epdr.setRelating_property_definition(null, edsma);
			epdr.setName(null, "associated stackup");
			// AIM gap
			epdr.setDescription(null, "");
		}
	}

	public static void unsetAssociated_stackup(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
		SdaiIterator apdrIter = apdr.createIterator();
		while(apdrIter.next()){
			EProperty_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
			if((epdr.testName(null))&&(epdr.getName(null).equals("associated stackup"))){
				epdr.deleteApplicationInstance();
			}
		}			
	}
	
	/**
	* Sets/creates data for allocated_technology constraints.
	*
	* <p>
		attribute_mapping allocated_technology(allocated_technology, $PATH, Passage_technology_armx);
			passage_technology_allocation_to_stack_model <=
			stratum_sub_stack <=
			part_template_definition <=
			product_definition_shape <=
			property_definition <-
			property_definition_relationship.related_property_definition
			{property_definition_relationship
			property_definition_relationship.name = 'allocated technology'}
			property_definition_relationship.relating_property_definition ->
			property_definition.definition ->
			characterized_definition
			characterized_definition = characterized_object
			characterized_object =>
			passage_technology
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PTATSM <- PDR -> STO
	public static void setAllocated_technology(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		unsetAllocated_technology(context, armEntity);
		if(armEntity.testAllocated_technology(null)){
			EPassage_technology_armx ept = armEntity.getAllocated_technology(null);
			EProperty_definition_relationship epdr = (EProperty_definition_relationship)
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setRelated_property_definition(null, armEntity);
			epdr.setRelating_property_definition(null, ept);
			epdr.setName(null, "allocated technology");
			// AIM gap
			epdr.setDescription(null, "");
		}
	}

	public static void unsetAllocated_technology(SdaiContext context, EPassage_technology_allocation_to_stack_model_armx armEntity) throws SdaiException
	{
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
		SdaiIterator apdrIter = apdr.createIterator();
		while(apdrIter.next()){
			EProperty_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
			if((epdr.testName(null))&&(epdr.getName(null).equals("allocated technology"))){
				epdr.deleteApplicationInstance();
			}
		}			
	}
	
}
