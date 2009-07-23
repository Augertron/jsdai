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
import jsdai.SLayered_interconnect_module_design_mim.CArea_component$stratum_feature_template_component_with_stratum_feature;
import jsdai.SLayered_interconnect_module_design_xim.CxArea_component_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxStratum_feature_armx;
import jsdai.SLayered_interconnect_module_design_xim.EArea_component_armx;
import jsdai.SLayered_interconnect_module_design_xim.EStratum_feature_armx;
import jsdai.SLksoft_extensions_xim.CxStratum_feature_template_component_with_stratum_feature_xim;
import jsdai.SPhysical_unit_design_view_xim.CxAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition_formation;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;

public class CxArea_component_armx$stratum_feature_template_component_with_stratum_feature_xim extends CArea_component_armx$stratum_feature_template_component_with_stratum_feature_xim implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a22);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a22);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a22 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a22 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a22$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
	/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a20);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a20);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a20 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a20 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a20$;
	}
	
	// ENDOF From CShape_aspect.java
	
	
	// Product_view_definition
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a6$;
	}

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a7$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a7);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a7 = get_instance(a7);
		return (EProduct_definition_formation)a7;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a7 = set_instanceX(a7, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a7 = unset_instance(a7);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a7$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a8);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a8 = get_instance(a8);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a8;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a8 = set_instanceX(a8, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a8$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a16);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a16);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a16 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a16 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a16$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a18$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a18);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a18 = get_instance_select(a18);
		return (EEntity)a18;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a18 = set_instanceX(a18, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a18 = unset_instance(a18);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a18$;
	}
	
	// ENDOF From CProperty_definition.java
	
	// Taken from PDR
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a9 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a9 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a9$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a10 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a10 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a10$;
	}
	
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a12$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a12);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a12);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a12 = set_instanceX(a12, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a12 = unset_instance(a12);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a12$;
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

		setTemp("AIM", CArea_component$stratum_feature_template_component_with_stratum_feature.definition);

		setMappingConstraints(context, this);
		
		// Assembly_component
		//Id
		setId_x(context, this);
		
		//id - goes directly into AIM
		
		//additional_characterization
		// setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

		setDerived_from(context, this);
		
		
		// feature_of_size
		setFeature_of_size(context, this);
		
		// area_component_armx
	    // explicitly_created : BOOLEAN;
		setExplicitly_created(context, this);
		
        // is_base : BOOLEAN;
		setIs_base(context, this);
		
        // replaced_component : OPTIONAL area_component_armx;
		setReplaced_component(context, this);
		
		// Assembly_component
		//Id
		unsetId_x((EAssembly_component_armx)null);
		
		//id - goes directly into AIM
		
		//additional_characterization
		// setAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(null);

		unsetDerived_from(null);
        
		unsetFeature_of_size(null);
		
		// area_component_armx
	    // explicitly_created : BOOLEAN;
		unsetExplicitly_created(null);
		
        // is_base : BOOLEAN;
		unsetIs_base(null);
		
        // replaced_component : OPTIONAL area_component_armx;
		unsetReplaced_component(null);
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
		
		// feature_of_size
		unsetFeature_of_size(context, this);
		
		// area_component_armx
	    // explicitly_created : BOOLEAN;
		unsetExplicitly_created(context, this);
		
        // is_base : BOOLEAN;
		unsetIs_base(context, this);
		
        // replaced_component : OPTIONAL area_component_armx;
		unsetReplaced_component(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CArea_component_armx$stratum_feature_template_component_with_stratum_feature_xim armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxStratum_feature_template_component_with_stratum_feature_xim.setMappingConstraints(context, armEntity);
		CxArea_component_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CArea_component_armx$stratum_feature_template_component_with_stratum_feature_xim armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_with_stratum_feature_xim.unsetMappingConstraints(context, armEntity);
		CxArea_component_armx.unsetMappingConstraints(context, armEntity);
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

	// Area_component_armx
	
	/**
	* Sets/creates data for explicitly_created attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setExplicitly_created(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxArea_component_armx.setExplicitly_created(context, armEntity);		
	}


	/**
	* Unsets/deletes data for explicitly_created attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetExplicitly_created(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxArea_component_armx.unsetExplicitly_created(context, armEntity);		
	}
	
	
	/**
	* Sets/creates data for is_base attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setIs_base(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxArea_component_armx.setIs_base(context, armEntity);		
	}


	/**
	* Unsets/deletes data for is_base attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetIs_base(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxArea_component_armx.unsetIs_base(context, armEntity);		
	}
	
	/**
	* Sets/creates data for replaced_component attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setReplaced_component(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxArea_component_armx.setReplaced_component(context, armEntity);		
	}


	/**
	* Unsets/deletes data for replaced_component attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetReplaced_component(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxArea_component_armx.unsetReplaced_component(context, armEntity);		
	}
	
}
