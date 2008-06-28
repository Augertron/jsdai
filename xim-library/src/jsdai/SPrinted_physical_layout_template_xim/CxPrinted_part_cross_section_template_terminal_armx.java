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

package jsdai.SPrinted_physical_layout_template_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SPrinted_physical_layout_template_mim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPrinted_part_cross_section_template_terminal_armx extends CPrinted_part_cross_section_template_terminal_armx implements EMappedXIMEntity{

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

		setTemp("AIM", CPrinted_part_cross_section_template_terminal.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
      // connection_zone_category 
		setConnection_zone_category(context, this);
		
		// cross_section_definition
		// TODO setAssociated_definition(context, this);
		
      // material_to_left_of_terminal
		setMaterial_to_left_of_terminal(context, this);
		
      // material_to_right_of_terminal
		setMaterial_to_right_of_terminal(context, this);
		
      // material_to_top_of_terminal
		setMaterial_to_top_of_terminal(context, this);
		
      // material_to_bottom_of_terminal 		
		setMaterial_to_bottom_of_terminal(context, this);
		
		// Clean ARM
		// Connection_area
		unsetConnection_area(null);
		
      // connection_zone_category 
		unsetConnection_zone_category(null);

		// cross_section_definition
		unsetAssociated_definition(null);
		
      // material_to_left_of_terminal
		unsetMaterial_to_left_of_terminal(null);
		
      // material_to_right_of_terminal
		unsetMaterial_to_right_of_terminal(null);
		
      // material_to_top_of_terminal
		unsetMaterial_to_top_of_terminal(null);
		
      // material_to_bottom_of_terminal 		
		unsetMaterial_to_bottom_of_terminal(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
	      // connection_zone_category 
			unsetConnection_zone_category(context, this);

			// cross_section_definition
			// TODO unsetAssociated_definition(context, this);
			
	      // material_to_left_of_terminal
			unsetMaterial_to_left_of_terminal(context, this);
			
	      // material_to_right_of_terminal
			unsetMaterial_to_right_of_terminal(context, this);
			
	      // material_to_top_of_terminal
			unsetMaterial_to_top_of_terminal(context, this);
			
	      // material_to_bottom_of_terminal 		
			unsetMaterial_to_bottom_of_terminal(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  printed_part_cross_section_template_terminal &lt;=
	 *  {[printed_part_template_terminal
	 *  groupable_item = printed_part_template_terminal
	 *  groupable_item &lt;-
	 *  applied_group_assignment.items[i]
	 *  applied_group_assignment &lt;=
	 *  group_assignment
	 *  group_assignment.assigned_group -&gt;
	 *  {group =&gt;
	 *  printed_part_template_terminal_connection_zone_category}
	 *  group
	 *  group.name
	 *  {(group.name = 'area edge segment')
	 *  (group.name = 'curve edge segment')}]
	 *  [printed_part_template_terminal &lt;=
	 *  shape_aspect
	 *  shape_aspect.of_shape -&gt;
	 *  product_definition_shape &lt;=
	 *  property_definition
	 *  property_definition.definition -&gt;
	 *  characterized_definition
	 *  characterized_definition = characterized_product_definition
	 *  characterized_product_definition
	 *  characterized_product_definition = product_definition
	 *  product_definition
	 *  [product_definition
	 *  product_definition.formation -&gt;
	 *  product_definition_formation
	 *  product_definition_formation.of_product -&gt;
	 *  product &lt;-
	 *  product_related_product_category.products[i]
	 *  product_related_product_category &lt;=
	 *  product_category
	 *  product_category.name = 'template model']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'template definition']]}
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
			EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxPrinted_part_template_terminal_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException {
		CxPrinted_part_template_terminal_armx.unsetMappingConstraints(context, armEntity);
	}

	//********** "printed_part_template_terminal" attributes

	/**
	* Sets/creates data for connection_zone_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_zone_category(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.setConnection_zone_category(context, armEntity);		
	}


	/**
	* Unsets/deletes data for connection_zone_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_zone_category(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.unsetConnection_zone_category(context, armEntity);
	}


	/**
	* Sets/creates data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.setConnection_area(context, armEntity);		
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.unsetConnection_area(context, armEntity);		
	}

	//********** "printed_part_cross_section_template_terminal" attributes


	/**
	* Sets/creates data for material_to_left_of_terminal attribute.
	*
	* <p>
	*  attribute_mapping material_to_left_of_terminal_printed_part_template_material_link (material_to_left_of_terminal
	* , (*PATH*), printed_part_template_material_link);
	* 	printed_part_cross_section_template_terminal <=
	* 	printed_part_template_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `material to left of terminal'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	printed_part_template_material_link
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_to_left_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaterial_to_left_of_terminal(context, armEntity);

		if (armEntity.testMaterial_to_left_of_terminal(null))
		{

			EPrinted_part_template_material_link armMaterial_to_left_of_terminal = armEntity.getMaterial_to_left_of_terminal(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "material to left of terminal");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armMaterial_to_left_of_terminal);
		}
	}


	/**
	* Unsets/deletes data for material_to_left_of_terminal attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_to_left_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("material to left of terminal")) {
				relationship.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for material_to_right_of_terminal attribute.
	*
	* <p>
	*  attribute_mapping material_to_right_of_terminal_printed_part_template_material_link (material_to_right_of_terminal
	* , (*PATH*), printed_part_template_material_link);
	* 	printed_part_cross_section_template_terminal <=
	* 	printed_part_template_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `material to right of terminal'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	printed_part_template_material_link
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_to_right_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaterial_to_right_of_terminal(context, armEntity);

		if (armEntity.testMaterial_to_right_of_terminal(null))
		{
			EPrinted_part_template_material_link armMaterial_to_right_of_terminal = armEntity.getMaterial_to_right_of_terminal(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "material to right of terminal");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armMaterial_to_right_of_terminal);
		}
	}


	/**
	* Unsets/deletes data for material_to_right_of_terminal attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_to_right_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("material to right of terminal")) {
				relationship.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for material_to_top_of_terminal attribute.
	*
	* <p>
	*  attribute_mapping material_to_top_of_terminal_printed_part_template_material_link (material_to_top_of_terminal
	* , (*PATH*), printed_part_template_material_link);
	* 	printed_part_cross_section_template_terminal <=
	* 	printed_part_template_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `material to top of terminal'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	printed_part_template_material_link
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_to_top_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaterial_to_top_of_terminal(context, armEntity);

		if (armEntity.testMaterial_to_top_of_terminal(null))
		{

			EPrinted_part_template_material_link armMaterial_to_top_of_terminal = armEntity.getMaterial_to_top_of_terminal(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "material to top of terminal");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armMaterial_to_top_of_terminal);
		}
	}


	/**
	* Unsets/deletes data for material_to_top_of_terminal attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_to_top_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("material to top of terminal")) {
				relationship.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for material_to_bottom_of_terminal attribute.
	*
	* <p>
	*  attribute_mapping material_to_bottom_of_terminal_printed_part_template_material_link (material_to_bottom_of_terminal
	* , (*PATH*), printed_part_template_material_link);
	* 	printed_part_cross_section_template_terminal <=
	* 	printed_part_template_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `material to bottom of terminal'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	printed_part_template_material_link
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_to_bottom_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaterial_to_bottom_of_terminal(context, armEntity);

		if (armEntity.testMaterial_to_bottom_of_terminal(null))
		{

			EPrinted_part_template_material_link armMaterial_to_bottom_of_terminal = armEntity.getMaterial_to_bottom_of_terminal(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "material to bottom of terminal");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armMaterial_to_bottom_of_terminal);

		}
	}


	/**
	* Unsets/deletes data for material_to_bottom_of_terminal attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_to_bottom_of_terminal(SdaiContext context, EPrinted_part_cross_section_template_terminal_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("material to bottom of terminal")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
	
	
}