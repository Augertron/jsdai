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
import jsdai.SFabrication_technology_mim.EStratum_technology_occurrence;
import jsdai.SFabrication_technology_xim.AStratum_technology_occurrence_armx;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SPart_template_2d_shape_mim.CPart_template_keepout_shape_allocation_to_stratum_stack;
import jsdai.SPart_template_shape_with_parameters_xim.EPart_template_keepout_shape_model;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;

public class CxPart_template_keepout_shape_allocation_to_stratum_stack_armx extends CPart_template_keepout_shape_allocation_to_stratum_stack_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// FROM CRepresentation.java
	// ENDOF FROM CRepresentation.java

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPart_template_keepout_shape_allocation_to_stratum_stack.definition);

		setMappingConstraints(context, this);

		// shape_characterized_part_template
		setKept_out_layers(context, this);
		
		setKeepout_shape(context, this);
		
		// clean ARM
		// shape_characterized_part_template
		unsetKept_out_layers(null);
		unsetKeepout_shape(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetKept_out_layers(context, this);
		
		unsetKeepout_shape(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		part_template_keepout_shape_allocation_to_stratum_stack <=
		[representation]
		[representation_relationship]
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPart_template_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart_template_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
	}	
	
	//********** "part_template_planar_shape" attributes

	/**
	* Sets/creates data for kept_out_layers attribute.
	* attribute_mapping kept_out_layers(kept_out_layers, $PATH, Stratum_technology_occurrence_armx);
		part_template_keepout_shape_allocation_to_stratum_stack <=
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship.name = 'kept out layers'}
		property_definition_relationship.relating_property_definition ->
		property_definition =>
		stratum_technology_occurrence
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // PD <- PDR -> STO
	public static void setKept_out_layers(SdaiContext context, EPart_template_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		unsetKept_out_layers(context, armEntity);
		if(armEntity.testKept_out_layers(null)){
			AStratum_technology_occurrence_armx layers = armEntity.getKept_out_layers(null);
			SdaiIterator iter = layers.createIterator();
			while(iter.next()){
				EStratum_technology_occurrence layer = layers.getCurrentMember(iter);
				EProperty_definition_relationship epdr = (EProperty_definition_relationship)
					context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
				epdr.setRelated_property_definition(null, (EProperty_definition)armEntity);
				epdr.setRelating_property_definition(null, layer);
				epdr.setName(null, "kept out layers");
			}
		}
	}


	/**
	* Unsets/deletes data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetKept_out_layers(SdaiContext context, EPart_template_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, (EProperty_definition)armEntity, context.domain, apdr);
		SdaiIterator iter = apdr.createIterator();
		while(iter.next()){
			EProperty_definition_relationship epdr = apdr.getCurrentMember(iter);
			if((epdr.testName(null))&&(epdr.getName(null).equals("kept out layers"))){
				epdr.deleteApplicationInstance();
			}
		}
				
	}

	/**
	* Sets/creates data for kept_out_layers attribute.
	* attribute_mapping kept_out_layers(kept_out_layers, $PATH, Stratum_technology_occurrence_armx);
		part_template_keepout_shape_allocation_to_stratum_stack <=
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship.name = 'kept out layers'}
		property_definition_relationship.relating_property_definition ->
		property_definition =>
		stratum_technology_occurrence
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // PD <- PDR -> STO
	public static void setKeepout_shape(SdaiContext context, EPart_template_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		unsetKeepout_shape(context, armEntity);
		if(armEntity.testKeepout_shape(null)){
			EPart_template_keepout_shape_model shape = armEntity.getKeepout_shape(null);
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, (EProperty_definition)armEntity);
			epdr.setUsed_representation(null, shape);
		}
	}


	/**
	* Unsets/deletes data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetKeepout_shape(SdaiContext context, EPart_template_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, (EProperty_definition)armEntity, context.domain, apdr);
		SdaiIterator iter = apdr.createIterator();
		while(iter.next()){
			EProperty_definition_representation epdr = apdr.getCurrentMember(iter);
			epdr.deleteApplicationInstance();
		}
				
	}
	
	
}
