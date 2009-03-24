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
import jsdai.SLand_xim.ELand_armx;
import jsdai.SLand_xim.EPlated_passage_dependent_land_armx;
import jsdai.SLayered_interconnect_module_design_mim.CStructured_layout_component_sub_assembly_relationship;
import jsdai.SLayered_interconnect_module_design_mim.EStructured_layout_component_sub_assembly_relationship;
import jsdai.SLayered_interconnect_module_design_xim.CxInternal_probe_access_area_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxProbe_access_area_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxStratum_feature_template_component_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxStructured_layout_component_sub_assembly_relationship_armx;
import jsdai.SLayered_interconnect_module_design_xim.EInternal_probe_access_area_armx;
import jsdai.SLayered_interconnect_module_design_xim.EProbe_access_area_armx;
import jsdai.SLayered_interconnect_module_design_xim.EStratum_feature_template_component_armx;
import jsdai.SLayered_interconnect_module_design_xim.EStructured_layout_component_sub_assembly_relationship_armx;
import jsdai.SLksoft_extensions_xim.CxStructured_layout_component_sub_assembly_relationship_with_component_xim;
import jsdai.SPhysical_unit_design_view_xim.CxAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition_formation;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SAssembly_structure_xim.CxNext_assembly_usage_occurrence_relationship_armx;
import jsdai.SAssembly_structure_xim.CxProduct_occurrence_definition_relationship_armx;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;

public class CxInternal_probe_access_area_armx$land_armx$structured_layout_component_sub_assembly_relationship_with_component_xim extends CInternal_probe_access_area_armx$land_armx$structured_layout_component_sub_assembly_relationship_with_component_xim implements EMappedXIMEntity,XimEntityStandalone
{
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
	return ((CEntity)instance).makeUsedin(definition, a25$, domain, result);
}
public boolean testOf_shape(EShape_aspect type) throws SdaiException {
	return test_instance(a25);
}
public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
	return (EProduct_definition_shape)get_instance(a25);
}*/
public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
	a25 = set_instance(a25, value);
}
public void unsetOf_shape(EShape_aspect type) throws SdaiException {
	a25 = unset_instance(a25);
}
public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
	return a25$;
}
	
	
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
		return (EProduct_definition_formation)a8;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a9 = set_instance(a9, value);
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
		a10 = set_instance(a10, value);
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
		return test_string(a20);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a20);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a20 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a20 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a20$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a22$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a22);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a22 = get_instance_select(a22);
		return (EEntity)a22;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a22 = set_instance(a22, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a22 = unset_instance(a22);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a22$;
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
		a14 = set_instance(a14, value);
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
		a8 = set_instance(a8, value);
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
		a12 = set_instance(a12, value);
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

		setTemp("AIM", CInternal_probe_access_area$land.definition); //$structured_layout_component_sub_assembly_relationship_with_component.definition);

		setMappingConstraints(context, this);
		
        // first_location : template_location_in_structured_template;
		setFirst_location(context, this);
		
		setSecond_location(context, this);
		
        // second_location : OPTIONAL template_location_in_structured_template;
		// setSecond_location(context, this);
		
        // overriding_shape : OPTIONAL part_template_shape_model;
		setOverriding_shape(context, this);
		
		// design_specific_placement
		setDesign_specific_placement(context, this);
		
		// relating_view : product_view_definition;
        setRelating_view(context, this);
		
        // made derived        
		// related_view : product_occurrence;		
        setRelated_view(context, this);
		
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
		
		// causal_item : OPTIONAL limd_restriction_basis_item;
		setCausal_item(context, this);
		
		// implementation_or_resident_stratum : stratum_feature_or_stratum;
		setImplementation_or_resident_stratum(context, this);
		
		// alternate_land_definition  
		setAlternate_land_definition (context, this); 
		
		// probed_layout_item
		setProbed_layout_item(context, this);
	 	
        // Connection_area
		setConnection_area(context, this);
		
		// stratum_feature_material_stackup
        setStratum_feature_material_stackup(context, this); 			
		
		// reference_designator
		setReference_designator(context, this);		
		
		// stratum_feature_implementation : stratum_feature_armx;
		setStratum_feature_implementation(context, this);
		
        // first_location : template_location_in_structured_template;
		unsetFirst_location(null);
		
		unsetSecond_location(null);
		
        // second_location : OPTIONAL template_location_in_structured_template;
		// unsetSecond_location(null);
		
        // overriding_shape : OPTIONAL part_template_shape_model;
		unsetOverriding_shape(null);
		
		unsetDesign_specific_placement(null);
		
		// relating_view : product_view_definition;
        unsetRelating_view(null);
		
        // made derived        
		// related_view : product_occurrence;		
        // unsetRelated_view(null);
        
		// Assembly_component
		//Id
		unsetId_x((EAssembly_component_armx)null);
		
		//id - goes directly into AIM
		
		//additional_characterization
		// setAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(null);

		unsetDerived_from(null);
		
		// causal_item : OPTIONAL limd_restriction_basis_item;
		unsetCausal_item(null);
		
		// implementation_or_resident_stratum : stratum_feature_or_stratum;
		unsetImplementation_or_resident_stratum(null);
		
		// alternate_land_definition  
		unsetAlternate_land_definition (null);
		
		// probed_layout_item
		unsetProbed_layout_item(null);
	 	
        // Connection_area
		unsetConnection_area(null);
		
		// stratum_feature_material_stackup
        unsetStratum_feature_material_stackup(null); 			
		
		// reference_designator
		unsetReference_designator(null);

		// stratum_feature_implementation : stratum_feature_armx;
		unsetStratum_feature_implementation(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
	
        // first_location : template_location_in_structured_template;
		unsetFirst_location(context, this);
		
		unsetSecond_location(context, this);
		
        // second_location : OPTIONAL template_location_in_structured_template;
		// unsetSecond_location(context, this);
		
        // overriding_shape : OPTIONAL part_template_shape_model;
		unsetOverriding_shape(context, this);
		
		// design_specific_placement
		unsetDesign_specific_placement(context, this);
		
		// relating_view : product_view_definition;
        unsetRelating_view(context, this);
		
        // made derived        
		// related_view : product_occurrence;		
        unsetRelated_view(context, this);
        
		// Assembly_component
		//Id
		unsetId_x(context, this);
		
		//id - goes directly into AIM
		
		//additional_characterization
		// setAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		unsetDerived_from(context, this);
		
		// causal_item : OPTIONAL limd_restriction_basis_item;
		unsetCausal_item(context, this);
		
		// implementation_or_resident_stratum : stratum_feature_or_stratum;
		unsetImplementation_or_resident_stratum(context, this);
		
		// alternate_land_definition  
		unsetAlternate_land_definition (context, this);
		
		// probed_layout_item
		unsetProbed_layout_item(context, this);
	 	
        // Connection_area
		unsetConnection_area(context, this);
		
		// stratum_feature_material_stackup
        unsetStratum_feature_material_stackup(context, this);
        
		// reference_designator
		unsetReference_designator(context, this);		
		
		// stratum_feature_implementation : stratum_feature_armx;
		unsetStratum_feature_implementation(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CInternal_probe_access_area_armx$land_armx$structured_layout_component_sub_assembly_relationship_with_component_xim armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxLand_armx.setMappingConstraints(context, armEntity);
		CxProbe_access_area_armx.setMappingConstraints(context, armEntity);
		CxStructured_layout_component_sub_assembly_relationship_with_component_xim.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CInternal_probe_access_area_armx$land_armx$structured_layout_component_sub_assembly_relationship_with_component_xim armEntity) throws SdaiException
	{
		CxLand_armx.unsetMappingConstraints(context, armEntity);
		CxProbe_access_area_armx.unsetMappingConstraints(context, armEntity);
		CxStructured_layout_component_sub_assembly_relationship_with_component_xim.unsetMappingConstraints(context, armEntity);
	}
	
	/**
	* Sets/creates data for First_location mapping constraints.
	attribute_mapping first_location(first_location, $PATH, Template_location_in_structured_template);
		structured_layout_component_sub_assembly_relationship <=
		next_assembly_usage_occurrence_relationship <=
		next_assembly_usage_occurrence <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		characterized_product_definition = product_definition_relationship
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'first location'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition = characterized_product_definition
		characterized_product_definition = product_definition_relationship
		product_definition_relationship
		product_definition_relationship =>
		product_definition_usage =>
		assembly_component_usage
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SLCSAR <- PD <- PDR -> PD -> ACU
	public static void setFirst_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_with_component_xim.setFirst_location(context, armEntity);
	}

	public static void setSecond_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_with_component_xim.setSecond_location(context, armEntity);		
	}
	

	public static void unsetFirst_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		CxStructured_layout_component_sub_assembly_relationship_with_component_xim.unsetFirst_location(context, armEntity);
	}

	public static void unsetSecond_location(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		CxStructured_layout_component_sub_assembly_relationship_with_component_xim.unsetSecond_location(context, armEntity);
	}

	/**
	* Sets/creates data for First_location mapping constraints.
	attribute_mapping overriding_shape(overriding_shape, $PATH, Part_template_shape_model);
		structured_layout_component_sub_assembly_relationship <=
		next_assembly_usage_occurrence_relationship <=
		next_assembly_usage_occurrence <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		characterized_product_definition = product_definition_relationship
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition
		represented_definition = property_definition
		represented_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation =>
		shape_representation

	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SLCSAR <- PD <- PDR -> SR
	public static void setOverriding_shape(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_armx.setOverriding_shape(context, armEntity);
	}

	public static void unsetOverriding_shape(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		CxStructured_layout_component_sub_assembly_relationship_armx.unsetOverriding_shape(context, armEntity);		
	}
	

	/**
	* Sets/creates data for design_specific_placement mapping constraints.
	attribute_mapping design_specific_placement(design_specific_placement, $PATH);
		structured_layout_component_sub_assembly_relationship <=
		next_assembly_usage_occurrence_relationship <=
		next_assembly_usage_occurrence <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		product_definition_relationship.name
		(product_definition_relationship.name = 'design specific placement') 
		(product_definition_relationship.name = 'design independent placement') 
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDesign_specific_placement(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException
	{
		CxStructured_layout_component_sub_assembly_relationship_armx.setDesign_specific_placement(context, armEntity);
	}
	
	public static void unsetDesign_specific_placement(SdaiContext context, EStructured_layout_component_sub_assembly_relationship_armx armEntity) throws SdaiException{
		CxStructured_layout_component_sub_assembly_relationship_armx.unsetDesign_specific_placement(context, armEntity);
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

	public static void setReference_designator(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.setReference_designator(context, armEntity);
	}
	
	public static void unsetReference_designator(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.unsetReference_designator(context, armEntity);
	}

	public static void setRelating_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxProduct_occurrence_definition_relationship_armx.setRelating_view(context, armEntity);
	}
	
	public static void unsetRelating_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxProduct_occurrence_definition_relationship_armx.unsetRelating_view(context, armEntity);
	}
	
	EStructured_layout_component_sub_assembly_relationship aimInstance;
	
	public EEntity getAimInstance(SdaiContext context) throws SdaiException {
		if(aimInstance == null){
			aimInstance = (EStructured_layout_component_sub_assembly_relationship)
				context.working_model.createEntityInstance(CStructured_layout_component_sub_assembly_relationship.definition);
		}
		return aimInstance;
	}
	
	public void unsetAimInstance(SdaiContext context) throws SdaiException{
		aimInstance = null;
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
	public static void setReference_plated_passage(SdaiContext context, EPlated_passage_dependent_land_armx armEntity) throws SdaiException
	{
		CxPlated_passage_dependent_land_armx.setReference_plated_passage(context, armEntity);
	}
	
	public static void unsetReference_plated_passage(SdaiContext context, EPlated_passage_dependent_land_armx armEntity) throws SdaiException
	{
		CxPlated_passage_dependent_land_armx.unsetReference_plated_passage(context, armEntity);
	}

/* Stratum_feature_template_component_armx attributes */

	// Causal_item
	public static void setCausal_item(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.setCausal_item(context, armEntity);
	}
	
	public static void unsetCausal_item(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.unsetCausal_item(context, armEntity);
	}

	// Implementation_or_resident_stratum
	public static void setImplementation_or_resident_stratum(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.setImplementation_or_resident_stratum(context, armEntity);
	}
	
	public static void unsetImplementation_or_resident_stratum(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.unsetImplementation_or_resident_stratum(context, armEntity);
	}

	// LAND_ARMX
	
	// alternate_land_definition
	public static void setAlternate_land_definition(SdaiContext context, ELand_armx armEntity) throws SdaiException
	{
		CxLand_armx.setAlternate_land_definition(context, armEntity);
	}
	
	public static void unsetAlternate_land_definition(SdaiContext context, ELand_armx armEntity) throws SdaiException
	{
		CxLand_armx.setAlternate_land_definition(context, armEntity);
	}
	
	//********** "probe_access_area" attributes

	/**
	* Sets/creates data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setProbed_layout_item(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.setProbed_layout_item(context, armEntity);
	}


	/**
	* Unsets/deletes data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetProbed_layout_item(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.unsetProbed_layout_item(context, armEntity);
	}


	/**
	* Sets/creates data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.setConnection_area(context, armEntity);		
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.unsetConnection_area(context, armEntity);		
	}

	/**
	* Sets/creates data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_feature_material_stackup(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.setStratum_feature_material_stackup(context, armEntity);		
	}


	/**
	* Unsets/deletes data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStratum_feature_material_stackup(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.unsetStratum_feature_material_stackup(context, armEntity);		
	}
	
	/**
	* Sets/creates data for stratum_feature_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_feature_implementation(SdaiContext context, EInternal_probe_access_area_armx armEntity) throws SdaiException
	{
		CxInternal_probe_access_area_armx.setStratum_feature_implementation(context, armEntity);		
	}


	/**
	* Unsets/deletes data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStratum_feature_implementation(SdaiContext context, EInternal_probe_access_area_armx armEntity) throws SdaiException
	{
		CxInternal_probe_access_area_armx.unsetStratum_feature_implementation(context, armEntity);		
	}
	
}