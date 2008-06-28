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

package jsdai.SFabrication_technology_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFabrication_technology_mim.EStratum_technology_occurrence_link;
import jsdai.SProduct_property_definition_schema.*;

public class CxComponent_termination_passage_template_join_terminal extends CComponent_termination_passage_template_join_terminal implements EMappedXIMEntity{

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

		setTemp("AIM", CShape_aspect.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
		// Disallowed_inter_stratum_extent
		setDisallowed_inter_stratum_extent(context, this);
		
		
		// Clean ARM
		unsetConnection_area(null);

		// disallowed_inter_stratum_extent 
		unsetDisallowed_inter_stratum_extent(null); 
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
			// Disallowed_inter_stratum_extent
			unsetDisallowed_inter_stratum_extent(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 * {[shape_aspect
	 *  (shape_aspect.description =  'component termination passage template interface terminal')
	 *  (shape_aspect.description =  'component termination passage template join terminal')]
	 *  [shape_aspect
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
			EComponent_termination_passage_template_join_terminal armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxComponent_termination_passage_template_terminal.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "component termination passage template join terminal");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EComponent_termination_passage_template_join_terminal armEntity) throws SdaiException {
		CxComponent_termination_passage_template_terminal.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}


	//********** "component_termination_passage_template_terminal" attributes

	/**
	* Sets/creates data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EComponent_termination_passage_template_terminal armEntity) throws SdaiException
	{
		CxComponent_termination_passage_template_terminal.setConnection_area(context, armEntity);		
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EComponent_termination_passage_template_terminal armEntity) throws SdaiException
	{
		CxComponent_termination_passage_template_terminal.unsetConnection_area(context, armEntity);		
	}

	//********** "component_termination_passage_template_join_terminal" attributes

	/**
	* Sets/creates data for disallowed_inter_stratum_extent attribute.
	*
	* <p>
	*  attribute_mapping disallowed_inter_stratum_extent_inter_stratum_extent (disallowed_inter_stratum_extent
	* , (*PATH*), inter_stratum_extent);
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `disallowed inter stratum extent'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect
	* 	shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	{property_definition
	* 	property_definition.description = `finished stratum extent'}
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition_relationship
	* 	product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `inter stratum extent'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDisallowed_inter_stratum_extent(SdaiContext context, EComponent_termination_passage_template_join_terminal armEntity) throws SdaiException
	{
		//unset old values
		unsetDisallowed_inter_stratum_extent(context, armEntity);

		if (armEntity.testDisallowed_inter_stratum_extent(null))
		{

			AStratum_technology_occurrence_link_armx aArmDisallowed_inter_stratum_extent = armEntity.getDisallowed_inter_stratum_extent(null);

			EStratum_technology_occurrence_link armDisallowed_inter_stratum_extent = null;

			for (int i = 1; i <= aArmDisallowed_inter_stratum_extent.getMemberCount(); i++) {
				armDisallowed_inter_stratum_extent = aArmDisallowed_inter_stratum_extent.getByIndex(i);

				//shape_aspect_relationship
				EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
				shape_aspect_relationship.setName(null, "disallowed inter stratum extent");
				shape_aspect_relationship.setRelating_shape_aspect(null, armEntity);

				//shape_aspect
				EShape_aspect shape_aspect = (EShape_aspect) context.working_model.createEntityInstance(EShape_aspect.class);
				shape_aspect.setName(null, "");
				shape_aspect.setProduct_definitional(null, ELogical.UNKNOWN);
				shape_aspect_relationship.setRelated_shape_aspect(null, shape_aspect);

				//product_definition_shape
				EProduct_definition_shape product_definition_shape = (EProduct_definition_shape) context.working_model.createEntityInstance(EProduct_definition_shape.class);
				product_definition_shape.setName(null, "");
				product_definition_shape.setDescription(null, "finished stratum extent");
				shape_aspect.setOf_shape(null, product_definition_shape);
				product_definition_shape.setDefinition(null, armDisallowed_inter_stratum_extent);
			}
		}
	}


	/**
	* Unsets/deletes data for disallowed_inter_stratum_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDisallowed_inter_stratum_extent(SdaiContext context, EComponent_termination_passage_template_join_terminal armEntity) throws SdaiException
	{
	    //shape_aspect_relationship
	    AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("disallowed inter stratum extent")) {

			   //shape_aspect
			   if (relationship.testRelated_shape_aspect(null)) {
			      EShape_aspect shape_aspect = relationship.getRelated_shape_aspect(null);

				  //product_definition_shape
				  if (shape_aspect.testOf_shape(null)) {
				     EProduct_definition_shape product_definition_shape = shape_aspect.getOf_shape(null);
					 if (product_definition_shape.testDescription(null) && product_definition_shape.getDescription(null).equals("finished stratum extent")) {

						//delete relationship
						relationship.deleteApplicationInstance();

						//delete shape_aspect
						if (CxAP210ARMUtilities.countEntityUsers(context, shape_aspect) == 0) {
				           shape_aspect.deleteApplicationInstance();
						}

						//delete product_definition_shape
						if (CxAP210ARMUtilities.countEntityUsers(context, product_definition_shape) == 0) {
				           product_definition_shape.deleteApplicationInstance();
						}
					 }
				  }
			   }
			}
		}
	}	
	
}