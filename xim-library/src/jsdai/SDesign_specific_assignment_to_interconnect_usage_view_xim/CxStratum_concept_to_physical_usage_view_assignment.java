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

package jsdai.SDesign_specific_assignment_to_interconnect_usage_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_interconnect_module_design_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxStratum_concept_to_physical_usage_view_assignment extends CStratum_concept_to_physical_usage_view_assignment implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Shape_aspect_relationship

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EShape_aspect_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EShape_aspect_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EShape_aspect_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect_relationship type) throws SdaiException {
		return a0$;
	}

	// attribute (current explicit or supertype explicit) : related_shape_aspect, base type: entity shape_aspect
/*	public static int usedinRelated_shape_aspect(EShape_aspect_relationship type, EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return test_instance(a3);
	}
	public EShape_aspect getRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a3 = get_instance(a3);
		return (EShape_aspect)a3;
	}*/
	public void setRelated_shape_aspect(EShape_aspect_relationship type, EShape_aspect value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return a3$;
	}

	// ENDOF Taken from Shape_aspect_relationship

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_aspect_relationship.definition);

		setMappingConstraints(context, this);

		// Assigned_design_object
		setAssigned_design_object(context, this);
		
		// clean ARM
		unsetAssigned_design_object(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// Assigned_design_object
		unsetAssigned_design_object(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_aspect_relationship
	*  shape_aspect_relationship.name = 'stratum concept to physical usage view assignment'}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_concept_to_physical_usage_view_assignment armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		armEntity.setName(null, "stratum concept to physical usage view assignment");
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_concept_to_physical_usage_view_assignment armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
	//**********stratum_concept_to_physical_usage_view_assignment attributes
	/**
	* Sets/creates data for assigned_design_object attribute.
	* @param context SdaiContext.
	* @param armEntity arm entity.
	*
	*  attribute_mapping assigned_design_object_stratum (assigned_design_object
	* , (*PATH*), stratum);
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect
	* 	shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	{product_definition
	* 	[product_definition.name = `interconnect module']
	* 	[product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `physical occurrence']}
	* 	product_definition =>
	* 	stratum
	*  end_attribute_mapping;
	*  // SAR.ed -> SA -> PDS -> S
	*  attribute_mapping assigned_design_object_inter_stratum_feature (assigned_design_object
	* , (*PATH*), inter_stratum_feature);
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	component_shape_aspect =>
	* 	inter_stratum_feature
	*  end_attribute_mapping;
	*  // SAR.ed -> ISF
	*  attribute_mapping assigned_design_object_stratum_feature (assigned_design_object
	* , (*PATH*), stratum_feature);
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	stratum_feature
	*  end_attribute_mapping;
	*  attribute_mapping assigned_design_object_laminate_component (assigned_design_object
	* , (*PATH*), laminate_component);
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	(shape_aspect =>
	* 	component_shape_aspect)
	* 	(shape_aspect
	* 	shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	{product_definition.description = `laminate component'})
	*  end_attribute_mapping;
	*  attribute_mapping assigned_design_object_stratum_surface (assigned_design_object
	* , (*PATH*), stratum_surface);
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	stratum_surface
	*  end_attribute_mapping;
	*  1) SAR.ed -> SA -> PDS -> S
	*  2) SAR.ed -> ISF
	*  3) SAR.ed -> SF
	*  4) SAR.ed -> CSA
	*  5) SAR.ed -> SA -> PDS -> PD
	*  6) SAR.ed -> SS
	*/
	public static void setAssigned_design_object(SdaiContext context, EStratum_concept_to_physical_usage_view_assignment armEntity) throws SdaiException {
		unsetAssigned_design_object(context, armEntity);
		if (armEntity.testAssigned_design_object(null)) {

			EEntity armAssigned_design_object = armEntity.getAssigned_design_object(null);
			// Cases 2, 3, 4, 6
			if(armAssigned_design_object instanceof EShape_aspect){
				armEntity.setRelated_shape_aspect(null, (EShape_aspect)armAssigned_design_object);
			}
			// Cases 1, 5
			else if(armAssigned_design_object instanceof EProduct_definition_shape){
				// SA -> PDS
				AShape_aspect asa = new AShape_aspect();
				EProduct_definition_shape epds = (EProduct_definition_shape)armAssigned_design_object;
				CShape_aspect.usedinOf_shape(null, epds, 
						context.domain, asa);
				SdaiIterator iterator = asa.createIterator();
				EShape_aspect suitableESA = null;
				while(iterator.next()){
					EShape_aspect esa = asa.getCurrentMember(iterator);
					if(esa instanceof EStratum_feature_armx)
						continue;
					suitableESA = esa;
					break;	
				}
				if(suitableESA == null){
					suitableESA = (EShape_aspect)context.working_model.createEntityInstance(CShape_aspect.definition);
					suitableESA.setProduct_definitional(null, ELogical.UNKNOWN);
					suitableESA.setName(null, "");
					suitableESA.setOf_shape(null, epds);
				}
				// SAR -> SA
				armEntity.setRelated_shape_aspect(null, suitableESA);
			}
			else
				throw new SdaiException(SdaiException.ED_NVLD," this type is not supported here "+armEntity);
		}
	}

	public static void unsetAssigned_design_object(SdaiContext context, EStratum_concept_to_physical_usage_view_assignment armEntity) throws SdaiException {
		armEntity.unsetRelated_shape_aspect(null);
	}

	

}
