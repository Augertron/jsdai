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
import jsdai.SLand_xim.CxLand_armx;
import jsdai.SLand_xim.CxPlated_passage_dependent_land_armx;
import jsdai.SLand_xim.CxUnsupported_passage_dependent_land_armx;
import jsdai.SLand_xim.ELand_armx;
import jsdai.SLand_xim.EPlated_passage_dependent_land_armx;
import jsdai.SLand_xim.EUnsupported_passage_dependent_land_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxStratum_feature_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxStratum_feature_template_component_armx;
import jsdai.SLayered_interconnect_module_design_xim.EStratum_feature_armx;
import jsdai.SLayered_interconnect_module_design_xim.EStratum_feature_template_component_armx;
import jsdai.SLksoft_extensions_xim.CxStratum_feature_template_component_with_stratum_feature_xim;
import jsdai.SPhysical_unit_design_view_xim.CxAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition_formation;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.EProperty_definition;

public class CxStratum_feature_template_component_with_stratum_feature_xim$unsupported_passage_dependent_land_armx extends CStratum_feature_template_component_with_stratum_feature_xim$unsupported_passage_dependent_land_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a8 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a8 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a8$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a9 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a9 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a9$;
	}
	
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a11$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a11);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a11);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a11 = set_instanceX(a11, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a11 = unset_instance(a11);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a11$;
	}
	
	// Product_view_definition
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a4);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a4$;
	}

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a5$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a5);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a5 = get_instance(a5);
		return (EProduct_definition_formation)a5;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a5 = set_instanceX(a5, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a5 = unset_instance(a5);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a5$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a6$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a6);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a6 = get_instance(a6);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a6;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a6 = set_instanceX(a6, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a6 = unset_instance(a6);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a6$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a14);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a14);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a14 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a14 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a14$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a16$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a16);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a16 = get_instance_select(a16);
		return (EEntity)a16;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a16 = set_instanceX(a16, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a16 = unset_instance(a16);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a16$;
	}
	
	// ENDOF From CProperty_definition.java
	
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

		setTemp("AIM", CStratum_feature_template_component_with_stratum_feature$unsupported_passage_dependent_land.definition); //$structured_layout_component_sub_assembly_relationship_with_component.definition);

		setMappingConstraints(context, this);
		
        // second_location : OPTIONAL template_location_in_structured_template;
		// setSecond_location(context, this);
		
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
		
		// feature_of_size : BOOLEAN;
		setFeature_of_size(context, this);
		
		// reference_plated_passage : plated_passage_armx;
		setReference_passage(context, this);

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
		
		// feature_of_size : BOOLEAN;
		unsetFeature_of_size(null);
		
		// reference_passage
		unsetReference_passage(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
	
        // second_location : OPTIONAL template_location_in_structured_template;
		// unsetSecond_location(context, this);
		
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

		// feature_of_size : BOOLEAN;
		unsetFeature_of_size(context, this);
		
		// reference_passage
		unsetReference_passage(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CStratum_feature_template_component_with_stratum_feature_xim$unsupported_passage_dependent_land_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxUnsupported_passage_dependent_land_armx.setMappingConstraints(context, armEntity);
		CxStratum_feature_template_component_with_stratum_feature_xim.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CStratum_feature_template_component_with_stratum_feature_xim$unsupported_passage_dependent_land_armx armEntity) throws SdaiException
	{
		CxUnsupported_passage_dependent_land_armx.unsetMappingConstraints(context, armEntity);
		CxStratum_feature_template_component_with_stratum_feature_xim.unsetMappingConstraints(context, armEntity);
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

	
	// Reference_plated_passage
	public static void setReference_passage(SdaiContext context, EUnsupported_passage_dependent_land_armx armEntity) throws SdaiException
	{
		CxUnsupported_passage_dependent_land_armx.setReference_passage(context, armEntity);
	}
	
	public static void unsetReference_passage(SdaiContext context, EUnsupported_passage_dependent_land_armx armEntity) throws SdaiException
	{
		CxUnsupported_passage_dependent_land_armx.unsetReference_passage(context, armEntity);
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

	//********** "stratum_feature" attributes

	/**
	* Sets/creates data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	* // SF <- PD <- PDR -> R -> DRI
	*/
	public static void setFeature_of_size(SdaiContext context, EStratum_feature_armx armEntity) throws SdaiException
	{
		CxStratum_feature_armx.setFeature_of_size(context, armEntity);		
	}


	/**
	* Unsets/deletes data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetFeature_of_size(SdaiContext context, EStratum_feature_armx armEntity) throws SdaiException
	{
		CxStratum_feature_armx.unsetFeature_of_size(context, armEntity);		
	}

	// reference_plated_passage
	public static void setReference_plated_passage(SdaiContext context, EPlated_passage_dependent_land_armx armEntity) throws SdaiException
	{
		CxPlated_passage_dependent_land_armx.setReference_plated_passage(context, armEntity);
	}
	
	public static void unsetReference_plated_passage(SdaiContext context, EPlated_passage_dependent_land_armx armEntity) throws SdaiException
	{
		CxPlated_passage_dependent_land_armx.unsetReference_plated_passage(context, armEntity);
	}
	
	
}
