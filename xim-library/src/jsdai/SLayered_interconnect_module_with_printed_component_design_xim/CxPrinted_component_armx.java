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

package jsdai.SLayered_interconnect_module_with_printed_component_design_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_interconnect_module_with_printed_component_design_mim.*;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SPhysical_unit_design_view_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;

public class CxPrinted_component_armx
		extends
			CPrinted_component_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from PDR
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a10 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a10 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a10$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a11$;
	}
	
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a13$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a13);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a13);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a13 = set_instanceX(a13, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a13 = unset_instance(a13);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a13$;
	}
	// ENDOF taken from PDR

	// Product_view_definition

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_formation)a2;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a2$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a3);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = get_instance(a3);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a3;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a3 = set_instanceX(a3, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a3$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a7);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a7);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a7 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a7 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a7$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a9);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a9 = get_instance_select(a9);
		return (EEntity)a9;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a9 = set_instanceX(a9, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a9$;
	}
	
	// ENDOF From CProperty_definition.java
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPrinted_component.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
//			setId_x(context, this);
			
			//id - goes directly into AIM
			
			//additional_characterization
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			
			
			

			setDerived_from(context, this);
			
			// stratum_feature_implementation
			// setStratum_feature_implementation(context, this);
			
         // printed_component_stack
			// setPrinted_component_stack(context, this);
			
         // required_material_stack
			// setRequired_material_stack(context, this);
         
			// footprint_implementation
			// setFootprint_implementation(context, this);
			
			// Clean ARM specific attributes
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
//			unsetId_x(null);

			// unsetStratum_feature_implementation(null);
			// unsetPrinted_component_stack(null);
			// unsetRequired_material_stack(null);
			// unsetFootprint_implementation(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// Kind of AIM gap
			// unsetDefinition(null);

			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization
			// unsetAdditional_characterization(context, this);

			
			
			
			
			unsetDerived_from(context, this);
			
//			unsetId_x(context, this);			

			// stratum_feature_implementation
			// unsetStratum_feature_implementation(context, this);
			
         // printed_component_stack
			// unsetPrinted_component_stack(context, this);
			
         // required_material_stack
			// unsetRequired_material_stack(context, this);
         
			// footprint_implementation
			// unsetFootprint_implementation(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  printed_component &lt;=
	 *  assembly_component &lt;=
	 *  component_definition &lt;= 
	 *  product_definition 
	 *  {product_definition 
	 *  [product_definition.description = 'printed component']
	 *  [product_definition.frame_of_reference -&gt; product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'layout occurrence']}
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
			EPrinted_component_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxAssembly_component_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription((EProduct_definition)null, "printed component");
		CxSingle_instance.setMappingConstraints(context, armEntity);
		// CxAP210ARMUtilities.assignPart_definition_type(context, armEntity, "layout occurrence");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPrinted_component_armx armEntity) throws SdaiException {
		CxAssembly_component_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((EProduct_definition)null);
		CxSingle_instance.unsetMappingConstraints(context, armEntity);
		// CxAP210ARMUtilities.deassignPart_definition_type(context, armEntity);
	}

	//********** "design_discipline_item_definition" attributes
	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);		
	}

	/**
	 * Sets/creates data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);		
	}

// Product_occurrence attributes	
	
	/**
	 * Sets/creates data for derived_from attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PDU -> PD
	public static void setDerived_from(SdaiContext context,
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		CxDefinition_based_product_occurrence.setDerived_from(context, armEntity);
	}

	/**
	 * Unsets/deletes data for Quantity_criterion attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetDerived_from(SdaiContext context,
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		CxDefinition_based_product_occurrence.unsetDerived_from(context, armEntity);		
	}
	
///// Item_shape
	/**
	 * Sets/creates data for id_x attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		CxItem_shape.setId_x(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes data for Quantity_criterion attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);		
	}
*/	
	//********** "printed_component" attributes

	/**
	* Sets/creates data for stratum_feature_implementation attribute.
	*
	*
	*  attribute_mapping stratum_feature_implementation_stratum_feature (stratum_feature_implementation
	* , (*PATH*), stratum_feature);
	* 	printed_component &lt;=
	*  assembly_component &lt;=					
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'stratum feature implementation'}
	*  shape_aspect_relationship.relating_shape_aspect -&gt;
	*  shape_aspect =&gt;
	*  stratum_feature
	* end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Removed	
	public static void setStratum_feature_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		if (armEntity.testStratum_feature_implementation(null))
		{

			AStratum_feature_armx aArmStratum_feature_implementation = armEntity.getStratum_feature_implementation(null);

			//unset old values
			unsetStratum_feature_implementation(context, armEntity);

			EStratum_feature_armx armStratum_feature_implementation = null;

			//shape_aspect
			EShape_aspect shape_aspect = null;
			AShape_aspect aShape_aspect = new AShape_aspect();
			CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aShape_aspect);
			if (aShape_aspect.getMemberCount() > 0) {
				shape_aspect = aShape_aspect.getByIndex(1);

			} else {
				shape_aspect = (EShape_aspect) context.working_model.createEntityInstance(CShape_aspect.definition);
				shape_aspect.setName(null, "");
				shape_aspect.setOf_shape(null, armEntity);
				shape_aspect.setProduct_definitional(null, ELogical.UNKNOWN);
			}


			for (int i = 1; i <= aArmStratum_feature_implementation.getMemberCount(); i++) {
				armStratum_feature_implementation = aArmStratum_feature_implementation.getByIndex(i);

				//shape_aspect_relationship
				EShape_aspect_relationship relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
				relationship.setName(null, "stratum feature implementation");
				relationship.setRelated_shape_aspect(null, shape_aspect);
				relationship.setRelating_shape_aspect(null, armStratum_feature_implementation);
			}
		}
	}
*/

	/**
	* Unsets/deletes data for stratum_feature_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Removed	
	public static void unsetStratum_feature_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
			//shape_aspect
			EShape_aspect shape_aspect = null;
			AShape_aspect aShape_aspect = new AShape_aspect();
			CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aShape_aspect);
			for (int j = 1; j <= aShape_aspect.getMemberCount(); j++) {
				shape_aspect = aShape_aspect.getByIndex(j);
				EShape_aspect_relationship relationship = null;
				AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
				CShape_aspect_relationship.usedinRelated_shape_aspect(null, shape_aspect, context.domain, aRelationship);

				for (int k = 1; k <= aRelationship.getMemberCount(); k++) {
					relationship = aRelationship.getByIndex(k);
					if (relationship.testName(null) && relationship.getName(null).equals("stratum feature implementation")) {
						relationship.deleteApplicationInstance();
					}
				}
			}
	}
*/

	/**
	* Sets/creates data for printed_component_stack attribute.
	*
	*
	*  attribute_mapping printed_component_stack_printed_component_link (printed_component_stack
	* , (*PATH*), printed_component_link);
	* 	printed_component <=
	* 	product_definition <-
	* 	product_definition_relationship.relating_product_definition
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `printed component stack'}
	* 	product_definition_relationship.related_product_definition ->
	* 	product_definition =>
	* 	printed_component_link
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Moved to subtypes	
	public static void setPrinted_component_stack(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		if (armEntity.testPrinted_component_stack(null))
		{
			APrinted_component_link_armx aArmPrinted_component_stack = armEntity.getPrinted_component_stack(null);

			//unset old values
			unsetPrinted_component_stack(context, armEntity);

			EPrinted_component_link_armx armPrinted_component_stack = null;
			EProduct_definition_relationship product_definition_relationship = null;
			for (int i = 1; i <= aArmPrinted_component_stack.getMemberCount(); i++) {
				armPrinted_component_stack = aArmPrinted_component_stack.getByIndex(i);

				product_definition_relationship = (EProduct_definition_relationship) context.working_model.createEntityInstance(EProduct_definition_relationship.class);
				product_definition_relationship.setId(null, "");
				product_definition_relationship.setName(null, "printed component stack");
				product_definition_relationship.setRelated_product_definition(null, armEntity);
				product_definition_relationship.setRelating_product_definition(null, armPrinted_component_stack);
			}
		}
	}
*/

	/**
	* Unsets/deletes data for printed_component_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/*	
	public static void unsetPrinted_component_stack(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);
		EProduct_definition_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("printed component stack")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
*/

	/**
	* Sets/creates data for required_material_stack attribute.
	*
	*
	*  attribute_mapping required_material_stack_layer (required_material_stack
	* , (*PATH*), layer);
	* 	printed_component <=
	* 	product_definition_shape <-
	* 	shape_aspect.of_shape
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `required material stack'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	layer
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Moved to subtype	
	public static void setRequired_material_stack(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		if (armEntity.testRequired_material_stack(null))
		{

			EStratum_feature_based_pattern_armx armRequired_material_stack = armEntity.getRequired_material_stack(null);
			//unset old values
			unsetRequired_material_stack(context, armEntity);

			//set new values

			//shape_aspect
			EShape_aspect shape_aspect = null;
			AShape_aspect aShape_aspect = new AShape_aspect();
			CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aShape_aspect);
			if (aShape_aspect.getMemberCount() > 0) {
				shape_aspect = aShape_aspect.getByIndex(1);

			} else {
				shape_aspect = (EShape_aspect) context.working_model.createEntityInstance(CShape_aspect.definition);
				shape_aspect.setName(null, "");
				shape_aspect.setOf_shape(null, armEntity);
				shape_aspect.setProduct_definitional(null, ELogical.UNKNOWN);
			}

			//shape_aspect_relationship
			EShape_aspect_relationship relationship = (EShape_aspect_relationship) 
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			relationship.setName(null, "required material stack");
			relationship.setRelated_shape_aspect(null, shape_aspect);
			relationship.setRelating_shape_aspect(null, armRequired_material_stack);

		}
	}
*/

	/**
	* Unsets/deletes data for required_material_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Moved to subtype	
	public static void unsetRequired_material_stack(SdaiContext context, EPrinted_component armEntity) throws SdaiException
	{
			//shape_aspect
			EShape_aspect shape_aspect = null;
			AShape_aspect aShape_aspect = new AShape_aspect();
			CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aShape_aspect);
			for (int j = 1; j <= aShape_aspect.getMemberCount(); j++) {
				shape_aspect = aShape_aspect.getByIndex(j);
				EShape_aspect_relationship relationship = null;
				AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
				CShape_aspect_relationship.usedinRelated_shape_aspect(null, shape_aspect, context.domain, aRelationship);

				for (int k = 1; k <= aRelationship.getMemberCount(); k++) {
					relationship = aRelationship.getByIndex(k);
					if (relationship.testName(null) && relationship.getName(null).equals("required material stack")) {
						relationship.deleteApplicationInstance();
					}
				}
			}
	}
*/
	/**
	* Sets/creates data for footprint_implementation attribute.
	*
	*
	*  attribute_mapping required_material_stack_layer (footprint_implementation
	* , (*PATH*), footprint_occurrence);
	* 	printed_component &lt;=
	*  assembly_component &lt;=					
	*  component_definition &lt;=
	*  product_definition &lt;-
	*  product_definition_relationship.related_product_definition
	*  product_definition_relationship
	*  {product_definition_relationship
	*  product_definition_relationship.name = 'footprint implementation'}
	*  product_definition_relationship.relating_product_definition -&gt;
	*  product_definition =&gt;
	*  component_definition =&gt;
	*  assembly_component =&gt;
	*  assembly_group_component_definition =&gt;
	*  footprint_occurrence 
	* end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed
	public static void setFootprint_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		if (armEntity.testFootprint_implementation(null))
		{

			EFootprint_occurrence_armx armFootprint = armEntity.getFootprint_implementation(null);
			//unset old values
			unsetFootprint_implementation(context, armEntity);

			//set new values
			EProduct_definition_relationship relationship = (EProduct_definition_relationship)
				context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
			
			relationship.setRelated_product_definition(null, armEntity);
			relationship.setRelating_product_definition(null, armFootprint);

		}
	}
*/

	/**
	* Unsets/deletes data for footprint_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Removed	
	public static void unsetFootprint_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		EProduct_definition_relationship epdr = null;
		AProduct_definition_relationship apdr = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, apdr);
		for (int j = 1; j <= apdr.getMemberCount(); j++) {
				epdr = apdr.getByIndex(j);
				if (epdr.testName(null) && epdr.getName(null).equals("footprint implementation")) {
						epdr.deleteApplicationInstance();
				}
		}
	}
*/	

}