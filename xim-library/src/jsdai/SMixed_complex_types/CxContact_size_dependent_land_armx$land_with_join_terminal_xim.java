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

package jsdai.SMixed_complex_types;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLand_xim.CxContact_size_dependent_land_armx;
import jsdai.SLand_xim.CxLand_armx;
import jsdai.SLand_xim.ELand_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxStratum_feature_template_component_armx;
import jsdai.SLayered_interconnect_module_design_xim.EStratum_feature_template_component_armx;
import jsdai.SLksoft_extensions_xim.CxLand_with_join_terminal_xim;
import jsdai.SPhysical_unit_design_view_xim.CxAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition_formation;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SAssembly_structure_xim.CxNext_assembly_usage_occurrence_relationship_armx;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;
import jsdai.SComponent_feature_xim.CxComponent_feature_armx;
import jsdai.SComponent_feature_xim.CxComponent_terminal_armx;
import jsdai.SComponent_feature_xim.EComponent_feature_armx;
import jsdai.SComponent_feature_xim.EComponent_terminal_armx;
import jsdai.SLand_mim.CContact_size_dependent_land$land_with_join_terminal;

public class CxContact_size_dependent_land_armx$land_with_join_terminal_xim extends CContact_size_dependent_land_armx$land_with_join_terminal_xim implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From CShape_aspect.java
	/*	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
	return test_string(a23);
	}
	public Object getOf_shape(EShape_aspect type) throws SdaiException {
		return get_string(a23);
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a23 = set_instanceX(a23, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a23 = unset_instance(a23);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a23$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a24);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a24);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a24 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a24 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a24$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
	/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a22);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a22);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a22 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a22 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a22$;
	}
	
	// ENDOF From CShape_aspect.java
	
	
	// Product_view_definition
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a8);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a8);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a8 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a8 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a8$;
	}

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a9);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a9 = get_instance(a9);
		return (EProduct_definition_formation)a9;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a9 = set_instanceX(a9, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a9$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a10$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a10);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a10 = get_instance(a10);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a10;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a10 = set_instanceX(a10, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a10 = unset_instance(a10);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a10$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a18);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a18);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a18 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a18 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a18$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a20$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a20);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a20 = get_instance_select(a20);
		return (EEntity)a20;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a20 = set_instanceX(a20, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a20 = unset_instance(a20);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a20$;
	}
	
	// ENDOF From CProperty_definition.java
	
	// Taken from PDR
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a11$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a12 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a12 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a12$;
	}
	
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a14$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a14);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a14);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a14 = set_instanceX(a14, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a14 = unset_instance(a14);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a14$;
	}
	// ENDOF taken from PDR
	
	
	// Taken from Product_definition_occurrence_relationship
	// attribute (current explicit or supertype explicit) : occurrence, base type: entity product_definition
/*	public static int usedinOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_instance(a8);
	}
	public jsdai.SProduct_definition_schema.EProduct_definition getOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return (jsdai.SProduct_definition_schema.EProduct_definition)get_instance(a8);
	}
	public void setOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition value) throws SdaiException {
		a8 = set_instanceX(a8, value);
	}
	public void unsetOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a8$;
	}
*/	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return get_string(a6);
	}
	public void setName(EProduct_definition_occurrence_relationship type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a6$;
	}
	// END of taken from PDOR
*/	
	// Taken from PDR
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
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
*/
	//<01> generating methods for consolidated attribute:  name
/*	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
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
	
	// ENDOF taken from PDR
	
	// From Property_definition
	// -2- methods for SELECT attribute: definition
/* Made is renamed from property_definition.definition	
	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a12$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a12);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a12 = get_instance_select(a12);
		return (EEntity)a12;
	}

	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a12 = set_instanceX(a12, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a12 = unset_instance(a12);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a12$;
	}
*/	
	// ENDOF from Property_definition

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CContact_size_dependent_land$land_with_join_terminal.definition); //$structured_layout_component_sub_assembly_relationship_with_component.definition);

		setMappingConstraints(context, this);
		
		// CxNext_assembly_usage_occurrence_relationship_armx.processAssemblyTrick(context, this);
		
		// Assembly_component
		//Id
		setId_x(context, this);
		
		//id - goes directly into AIM
		
		//additional_characterization
		// setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

		setDerived_from(context, this);
		
		setAlternate_land_definition(context, this);
		
		setImplementation_or_resident_stratum(context, this);
		
		setCausal_item(context, this);

		setDefinition(context, this);
		
		// local_swappable
		setLocal_swappable(context, this);
		
      // global_swappable
		setGlobal_swappable(context, this);
		
		// swap_code		
		setSwap_code(context, this);
		
		// Clean ARM
		unsetDefinition((EComponent_feature_armx)null);

		// local_swappable
		unsetLocal_swappable(null);
		
      // global_swappable
		unsetGlobal_swappable(null);
		
		// swap_code		
		unsetSwap_code(null);
		
		// Assembly_component
		//Id
		unsetId_x((EAssembly_component_armx)null);
		
		//id - goes directly into AIM
		
		//additional_characterization
		// setAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(null);

		unsetDerived_from(null);
        
		unsetImplementation_or_resident_stratum(null);
		
		unsetAlternate_land_definition(null);
		
		unsetCausal_item(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
	
		// Assembly_component
		//Id
		unsetId_x(context, this);
		
		//id - goes directly into AIM
		
		//additional_characterization
		// setAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		unsetDerived_from(context, this);
		
		unsetAlternate_land_definition(context, this);
		
		unsetImplementation_or_resident_stratum(context, this);
		
		unsetCausal_item(context, this);
		
		unsetDefinition(context, this);
		
		// local_swappable
		unsetLocal_swappable(context, this);
		
      // global_swappable
		unsetGlobal_swappable(context, this);
		
		// swap_code		
		unsetSwap_code(context, this);
	
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CContact_size_dependent_land_armx$land_with_join_terminal_xim armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxContact_size_dependent_land_armx.setMappingConstraints(context, armEntity);
		CxLand_with_join_terminal_xim.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CContact_size_dependent_land_armx$land_with_join_terminal_xim armEntity) throws SdaiException
	{
		CxContact_size_dependent_land_armx.unsetMappingConstraints(context, armEntity);
		CxLand_with_join_terminal_xim.unsetMappingConstraints(context, armEntity);
	}
	
    /* made derived	*/
	public static void setRelated_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.setRelated_view(context, armEntity);
	}
	
	public static void unsetRelated_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.unsetRelated_view(context, armEntity);
	}

	// Assembly_component
	public static void setId_x(SdaiContext context, EAssembly_component_armx armEntity) throws SdaiException
	{
		CxAssembly_component_armx.setId_x(context, armEntity);
	}
	
	public static void unsetId_x(SdaiContext context, EAssembly_component_armx armEntity) throws SdaiException
	{
		CxAssembly_component_armx.unsetId_x(context, armEntity);
	}
	
	
	//additional_context
	public static void setAdditional_contexts(SdaiContext context, EAssembly_component_armx armEntity) throws SdaiException
	{
		CxAssembly_component_armx.setAdditional_contexts(context, armEntity);
	}
	
	public static void unsetAdditional_contexts(SdaiContext context, EAssembly_component_armx armEntity) throws SdaiException
	{
		CxAssembly_component_armx.unsetAdditional_contexts(context, armEntity);
	}

	// Derived_from
	public static void setDerived_from(SdaiContext context, EAssembly_component_armx armEntity) throws SdaiException
	{
		CxAssembly_component_armx.setDerived_from(context, armEntity);
	}
	
	public static void unsetDerived_from(SdaiContext context, EAssembly_component_armx armEntity) throws SdaiException
	{
		CxAssembly_component_armx.unsetDerived_from(context, armEntity);
	}
	
	/* Stratum_feature_template_component */	
	// implementation_or_resident_stratum
	public static void setImplementation_or_resident_stratum(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.setImplementation_or_resident_stratum(context, armEntity);
	}
	
	public static void unsetImplementation_or_resident_stratum(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.unsetImplementation_or_resident_stratum(context, armEntity);
	}
	
	// causal_item
	public static void setCausal_item(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.setCausal_item(context, armEntity);
	}
	
	public static void unsetCausal_item(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.unsetCausal_item(context, armEntity);
	}

	// alternate_land_definition
	public static void setAlternate_land_definition(SdaiContext context, ELand_armx armEntity) throws SdaiException
	{
		CxLand_armx.setAlternate_land_definition(context, armEntity);
	}
	
	public static void unsetAlternate_land_definition(SdaiContext context, ELand_armx armEntity) throws SdaiException
	{
		CxLand_armx.unsetAlternate_land_definition(context, armEntity);
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
	
}
