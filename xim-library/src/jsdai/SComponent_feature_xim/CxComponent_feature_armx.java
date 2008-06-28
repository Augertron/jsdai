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

package jsdai.SComponent_feature_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SComponent_feature_mim.CComponent_feature;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_property_assignment_xim.CxShape_element;

public class CxComponent_feature_armx extends CComponent_feature_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
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

		setTemp("AIM", CComponent_feature.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		// Clean ARM
		unsetDefinition(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetDefinition(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  component_feature &lt;=
	 *  shape_aspect
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
			EComponent_feature_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_element.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EComponent_feature_armx armEntity) throws SdaiException {
		CxShape_element.unsetMappingConstraints(context, armEntity);
	}

	//********** "component_feature" attributes

	/**
	* Sets/creates data for definition attribute.
	*
	* <p>
	*  attribute_mapping definition_land_template_terminal (definition
	* , (*PATH*), land_template_terminal);
	* 	(component_terminal <=)
	* 	(laminate_component_interface_terminal <=)
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `instantiated feature'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	land_template_terminal
	*  end_attribute_mapping;
	*  attribute_mapping definition_component_termination_passage_template_terminal (definition
	* , (*PATH*), component_termination_passage_template_terminal);
	* 	(component_terminal <=)
	* 	(laminate_component_interface_terminal <=)
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `instantiated feature'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect
	* 	{shape_aspect
	* 	(shape_aspect.description =  `component termination passage template interface terminal')
	* 	(shape_aspect.description = `component termination passage template join terminal')}
	*  end_attribute_mapping;
	*  attribute_mapping definition_part_feature (definition
	* , (*PATH*), part_feature);
	* 	(component_terminal <=)
	* 	(component_interface_terminal <=)
	* 	(component_feature <=)
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `instantiated feature'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect
	* 	{([shape_aspect =>
	* 	composite_shape_aspect]
	* 	[shape_aspect
	* 	shape_aspect.description = `part group feature'])
	* 	(shape_aspect.description = `part generic feature')
	* 	(shape_aspect.description = `polarity indication feature')
	* 	(shape_aspect.description = `interconnect module edge segment surface')
	* 	(shape_aspect.description = `interconnect module edge surface')
	* 	(shape_aspect.description = `interconnect module primary surface')
	* 	(shape_aspect.description = `interconnect module secondary surface')
	* 	(shape_aspect.description = `interconnect module surface feature')
	* 	(shape_aspect =>
	* 	primary_orientation_feature)
	* 	(shape_aspect =>
	* 	secondary_orientation_feature)
	* 	(shape_aspect =>
	* 	package_body)
	* 	(shape_aspect =>
	* 	part_tooling_feature)
	* 	(shape_aspect =>
	* 	thermal_feature)
	* 	(shape_aspect =>
	* 	part_mounting_feature)
	* 	(shape_aspect =>
	* 	package_terminal)
	* 	(shape_aspect =>
	* 	assembly_module_terminal)
	* 	(shape_aspect =>
	* 	interconnect_module_terminal)
	* 	(shape_aspect =>
	* 	minimally_defined_bare_die_terminal)
	* 	(shape_aspect =>
	* 	packaged_part_terminal)
	* 	(shape_aspect =>
	* 	package_body_surface)}
	*  end_attribute_mapping;
	*  attribute_mapping definition_printed_part_template_terminal (definition
	* , (*PATH*), printed_part_template_terminal);
	* 	(component_terminal <=)
	* 	(laminate_component_interface_terminal <=)
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `instantiated feature'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	printed_part_template_terminal
	*  end_attribute_mapping;
	*  attribute_mapping definition_via_template_terminal (definition
	* , (*PATH*), via_template_terminal);
	* 	(component_terminal <=)
	* 	(laminate_component_interface_terminal <=)
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `instantiated feature'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect
	* 	{shape_aspect
	* 	shape_aspect.description = `via template terminal'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> SA
	public static void setDefinition(SdaiContext context, EComponent_feature_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetDefinition(context, armEntity);

		if (armEntity.testDefinition(null))
		{
		   String keyword = "instantiated feature";
		   EShape_aspect armDefinition = (EShape_aspect) armEntity.getDefinition(null);
			// SA (template)
			// System.err.println(" ARM "+armDefinition);
		   // SAR
			EShape_aspect_relationship relationship = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.class);
		   relationship.setName(null, keyword);
			// SA <- SAR -> SA
			relationship.setRelated_shape_aspect(null, armEntity);
			relationship.setRelating_shape_aspect(null, armDefinition);
		}
	}


	/**
	* Unsets/deletes data for definition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDefinition(SdaiContext context, EComponent_feature_armx armEntity) throws SdaiException
	{
		String keyword = "instantiated feature";
		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null,
			armEntity, context.domain, relationships);
		for(int i=1;i<=relationships.getMemberCount();){
			EShape_aspect_relationship relationship = relationships.getByIndex(i);
		   if((relationship.testName(null))&&
				(relationship.getName(null).equals(keyword))){
				relationships.removeByIndex(i);
				relationship.deleteApplicationInstance();
		   }
			else
				i++;
		}
	}


	
}