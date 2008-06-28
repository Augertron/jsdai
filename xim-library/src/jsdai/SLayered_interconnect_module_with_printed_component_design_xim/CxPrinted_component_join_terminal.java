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
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SComponent_feature_xim.*;
import jsdai.SLayered_interconnect_module_design_mim.CLaminate_component_join_terminal;
import jsdai.SLayered_interconnect_module_design_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPrinted_component_join_terminal extends CPrinted_component_join_terminal implements EMappedXIMEntity{

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

		setTemp("AIM", CLaminate_component_join_terminal.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		// local_swappable
		setLocal_swappable(context, this);
		
      // global_swappable
		setGlobal_swappable(context, this);
		
		// swap_code		
		setSwap_code(context, this);
		
		// Stratum_concept_implementation
		setStratum_concept_implementation(context, this);
		
		// Clean ARM
		// unsetDefinition(null);

		// local_swappable
		unsetLocal_swappable(null);
		
      // global_swappable
		unsetGlobal_swappable(null);
		
		// swap_code		
		unsetSwap_code(null);
		
		unsetStratum_concept_implementation(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetDefinition(context, this);

			// local_swappable
			unsetLocal_swappable(context, this);
			
	      // global_swappable
			unsetGlobal_swappable(context, this);
			
			// swap_code		
			unsetSwap_code(context, this);

			// Stratum_concept_implementation
			unsetStratum_concept_implementation(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  laminate_component_join_terminal &lt;=
	 *  laminate_component_feature &lt;=				
	 *  component_feature &lt;=				
	 *  shape_aspect
	 *  {shape_aspect
	 *  [shape_aspect.description = 'printed component join terminal']
	 *  [shape_aspect.product_definitional = .TRUE.]}
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
			EPrinted_component_join_terminal armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxLaminate_component_join_terminal_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "printed component join terminal");
		armEntity.setProduct_definitional(null, ELogical.TRUE);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPrinted_component_join_terminal armEntity) throws SdaiException {
		CxLaminate_component_join_terminal_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
		armEntity.unsetProduct_definitional(null);
	}

	//********** "component_feature" attributes

	/**
	* Sets/creates data for definition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> SA
	public static void setDefinition(SdaiContext context, EComponent_feature_armx armEntity) throws SdaiException
	{
		CxComponent_feature_armx.setDefinition(context, armEntity);		
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
		CxComponent_feature_armx.unsetDefinition(context, armEntity);		
	}

	//********** "component_terminal" attributes

	/**
	* Sets/creates data for local_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.setLocal_swappable(context, armEntity);		
	}


	/**
	* Unsets/deletes data for local_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.unsetLocal_swappable(context, armEntity);
	}


	/**
	* Sets/creates data for global_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setGlobal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.setGlobal_swappable(context, armEntity);		
	}


	/**
	* Unsets/deletes data for global_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetGlobal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.unsetGlobal_swappable(context, armEntity);
	}


	/**
	* Sets/creates data for swap_code attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setSwap_code(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.setSwap_code(context, armEntity);		
	}


	/**
	* Unsets/deletes data for swap_code attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetSwap_code(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.unsetSwap_code(context, armEntity);
	}

	//********** "printed_component_join_terminal" attributes

	/**
	* Sets/creates data for stratum_concept_implementation attribute.
	*
	* <p>
	*  attribute_mapping stratum_concept_implementation_stratum_feature (stratum_concept_implementation
	* , (*PATH*), stratum_feature);
	* 	component_terminal &lt;=
	*  component_feature &lt;=
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'implementation'}
	*  shape_aspect_relationship.relating_shape_aspect -&gt;
	*  shape_aspect
	*  shape_aspect.of_shape -&gt;
	*  product_definition_shape =&gt;
	*  assembly_component &lt;=
	*  component_definition &lt;=
	*  product_definition
	*  {[product_definition =&gt;
	*  component_definition =&gt; 
	*  assembly_component]
	*  [product_definition.name = 'laminate component']
	*  [product_definition.frame_of_reference -&gt;
	*  product_definition_context &lt;=
	*  application_context_element
	*  application_context_element.name = 'layout occurrence']}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_concept_implementation(SdaiContext context, EPrinted_component_join_terminal armEntity) throws SdaiException
	{
		//unset old values
		unsetStratum_concept_implementation(context, armEntity);

		if (armEntity.testStratum_concept_implementation(null))
		{

			EStratum_feature_template_component_armx armStratum_concept_implementation = armEntity.getStratum_concept_implementation(null);
			// SA
		   LangUtils.Attribute_and_value_structure[] saStructure =
			 {
				new LangUtils.Attribute_and_value_structure(
						CShape_aspect.attributeOf_shape(null), armStratum_concept_implementation)
			};
		   EShape_aspect esa = (EShape_aspect)
						LangUtils.createInstanceIfNeeded(
												context,
												CShape_aspect.definition,
												saStructure);
			if(!esa.testName(null))
				esa.setName(null, "");
			if(!esa.testProduct_definitional(null))
				esa.setProduct_definitional(null, ELogical.UNKNOWN);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			shape_aspect_relationship.setName(null, "implementation");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, esa);

		}
	}


	/**
	* Unsets/deletes data for stratum_concept_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStratum_concept_implementation(SdaiContext context, EPrinted_component_join_terminal armEntity) throws SdaiException
	{
		//shape_aspect_relationship
		EShape_aspect_relationship shape_aspect_relationship = null;
		AShape_aspect_relationship aShape_aspect_relationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aShape_aspect_relationship);
		for (int i = 1; i <= aShape_aspect_relationship.getMemberCount(); i++) {
			shape_aspect_relationship = aShape_aspect_relationship.getByIndex(i);
			if (shape_aspect_relationship.testName(null) && shape_aspect_relationship.getName(null).equals("implementation")) {
				shape_aspect_relationship.deleteApplicationInstance();
			}
		}
	}
	
	
}