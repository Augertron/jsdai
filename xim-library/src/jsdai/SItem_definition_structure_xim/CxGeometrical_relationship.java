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

package jsdai.SItem_definition_structure_xim;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.EMapped_item;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;

public class CxGeometrical_relationship extends CGeometrical_relationship implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Product_definition_relationship
	/// methods for attribute: name, base type: STRING
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
	// ENDOF Taken from Product_definition_relationship
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CProduct_definition_relationship.definition);

		setMappingConstraints(context, this);

		setDefinition_placement(context, this);
		
		// clean ARM
		unsetDefinition_placement(null);		

	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetDefinition_placement(context, this);		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	 product_definition_relationship
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EGeometrical_relationship armEntity) throws SdaiException
	{
		armEntity.setName(null, "geometrical relationship");
	}

	public static void unsetMappingConstraints(SdaiContext context, EGeometrical_relationship armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
	
	/* Sets/creates data for Base_independent_property attribute.
	*
		attribute_mapping definition_placement(definition_placement, $PATH, Geometric_model_relationship_with_transformation);
			product_definition_relationship 
			{product_definition_relationship.name = 'geometrical relationship' }  
			[product_definition_relationship.relating_product_definition -> product_definition 
			characterized_product_definition = product_definition 
			characterized_definition = characterized_product_definition 
			characterized_definition <- property_definition.definition 
			property_definition  
			{property_definition => product_definition_shape} 
			represented_definition = property_definition 
			represented_definition <- property_definition_representation.definition 
			property_definition_representation  
			{property_definition_representation => shape_definition_representation} 
			property_definition_representation.used_representation -> representation <- 
			{representation => shape_representation} 
			representation_relationship.rep_2] 
			[product_definition_relationship.related_product_definition -> product_definition 
			characterized_product_definition = product_definition 
			characterized_definition = characterized_product_definition 
			characterized_definition <- property_definition.definition 
			property_definition  
			{property_definition => product_definition_shape} 
			represented_definition = property_definition 
			represented_definition <- property_definition_representation.definition 
			property_definition_representation  
			{property_definition_representation => shape_definition_representation} 
			property_definition_representation.used_representation -> representation <-  {representation => shape_representation} 
			representation_relationship.rep_1] 
			[characterized_product_definition = product_definition_relationship 
			characterized_definition = characterized_product_definition 
			characterized_definition <- property_definition.definition 
			property_definition => product_definition_shape <-  context_dependent_shape_representation.represented_product_relation 
			context_dependent_shape_representation 
			context_dependent_shape_representation.representation_relation -> shape_representation_relationship <=]  
			representation_relationship =>  representation_relationship_with_transformation
		end_attribute_mapping;
		
		attribute_mapping definition_placement(definition_placement, $PATH, Geometric_placement_operation);
			product_definition_relationship 
			{product_definition_relationship.name = 'geometrical relationship' } 	
			[product_definition_relationship.relating_product_definition -> product_definition 
			characterized_product_definition = product_definition 
			characterized_definition = characterized_product_definition 
			characterized_definition <- property_definition.definition 
			property_definition  
			{property_definition => product_definition_shape} 
			represented_definition = property_definition 
			represented_definition <- property_definition_representation.definition 
			property_definition_representation  
			{property_definition_representation => shape_definition_representation} 
			property_definition_representation.used_representation -> representation  
			{representation => shape_representation} 
			representation.items[i] -> representation_item =>] 
			[product_definition_relationship.related_product_definition -> product_definition 
			characterized_product_definition = product_definition 
			characterized_definition = characterized_product_definition 
			characterized_definition <- property_definition.definition 
			property_definition  
			{property_definition => product_definition_shape} 
			represented_definition = property_definition 
			represented_definition <- property_definition_representation.definition 
			property_definition_representation  
			{property_definition_representation => shape_definition_representation} 
			property_definition_representation.used_representation -> representation <-  {representation => shape_representation} 
			representation_map.mapped_representation 
			representation_map <- mapped_item.mapping_source] 
			[characterized_product_definition = product_definition_relationship 
			characterized_definition = characterized_product_definition 
			characterized_definition <- property_definition.definition 
			property_definition  
			{property_definition => product_definition_shape} 
			represented_definition = property_definition 
			represented_definition <- property_definition_representation.definition 
			property_definition_representation  
			{property_definition_representation => shape_definition_representation} 
			property_definition_representation.used_representation -> representation 
			{representation => shape_representation} 
			representation.items[i] -> representation_item =>]  
			mapped_item
		end_attribute_mapping;
		
		
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 
	// PDR.ing -> PD <- PDS <- SDR -> SR <- RR.rep_2
	// PDR.ed -> PD <- PDS <- SDR -> SR <- RR.rep_1
	// PDR <- PDS <- CDSR -> RRWT
	// OR
	// PDR.ing -> PD | <- PDS <- SDR -> SR -> | MI
	// PDR.ed ->  PD | <- PDS <- SDR -> | SR <- RM <- MI
	// PDR <- | PDS <- SDR -> SR -> | MI
	// Implementation with minimal adoption to stepmod is taken from Cx class from Ida_step schema - setPropertyDefinitionForPlacement
	// TODO - need to implement this method for all possible cases
	public static void setDefinition_placement(SdaiContext context, EGeometrical_relationship armEntity) throws SdaiException
	{
		//unset old values
		// unsetDefinition_placement(context, armEntity);

		if (armEntity.testDefinition_placement(null))
		{
			EMapped_item aimPlacement = (EMapped_item)armEntity.getDefinition_placement(null);
			
			// Eproperty_definition => Eproduct_definition_shape
			EProduct_definition_shape productDefinitionShape = (EProduct_definition_shape) context.working_model
					.createEntityInstance(CProduct_definition_shape.definition);
			productDefinitionShape.setName(null, "");
			productDefinitionShape.setDefinition(null, armEntity);
			//
			EShape_definition_representation shapeDefinitionRepresentation = (EShape_definition_representation) context.working_model
					.createEntityInstance(EShape_definition_representation.class);
			shapeDefinitionRepresentation.setDefinition(null, productDefinitionShape);

			EShape_representation shapeRepresentation = (EShape_representation) context.working_model
					.createEntityInstance(CShape_representation.definition);
			shapeRepresentation.setName(null,"");

			ERepresentation_context representationContext = (ERepresentation_context) context.working_model
					.createEntityInstance(ERepresentation_context.class);
			representationContext.setContext_identifier(null, "");
			representationContext.setContext_type(null, "");
			shapeRepresentation.setContext_of_items(null, representationContext);
			shapeRepresentation.createItems(null).addUnordered(aimPlacement);
			shapeDefinitionRepresentation.setUsed_representation(null, shapeRepresentation);
		}
	}


	/**
	* Unsets/deletes data for Base_independent_property attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDefinition_placement(SdaiContext context, EGeometrical_relationship armEntity) throws SdaiException
	{
		// According Implementation from Cx class from Ida_step schema - unsetPropertyDefinitionForPlacement,
		// nothing is done in this method
	}


}
