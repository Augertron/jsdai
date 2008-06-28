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

package jsdai.SFootprint_definition_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SPart_template_2d_shape_xim.CxTemplate_location_in_structured_template;
import jsdai.SPart_template_2d_shape_xim.ETemplate_location_in_structured_template;
import jsdai.SPhysical_unit_usage_view_xim.EPart_feature;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_structure_schema.CAssembly_component_usage;
import jsdai.SProduct_structure_schema.EAssembly_component_usage;

public class CxPart_feature_based_template_location extends CPart_feature_based_template_location implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Product_definition_relationship
	/// methods for attribute: id, base type: STRING
/*	
	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from Product_definition_relationship

	// Taken from Assembly_component_usage
	/// methods for attribute: reference_designator, base type: STRING
/*	public boolean testReference_designator(EAssembly_component_usage type) throws SdaiException {
		return test_string(a5);
	}
	public String getReference_designator(EAssembly_component_usage type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setReference_designator(EAssembly_component_usage type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetReference_designator(EAssembly_component_usage type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeReference_designator(EAssembly_component_usage type) throws SdaiException {
		return a5$;
	}
	// ENDOF taken from Assembly_component_usage
	
	// Taken from TLIST
	/// methods for attribute: reference_designation, base type: STRING
/*	public boolean testReference_designation(ETemplate_location_in_structured_template type) throws SdaiException {
		return testReference_designator((jsdai.SProduct_structure_schema.EAssembly_component_usage)null);
	}
	public String getReference_designation(ETemplate_location_in_structured_template type) throws SdaiException {
		return getReference_designator((jsdai.SProduct_structure_schema.EAssembly_component_usage)null);
	}*/
	public void setReference_designation(ETemplate_location_in_structured_template type, String value) throws SdaiException {
		setReference_designator((jsdai.SProduct_structure_schema.EAssembly_component_usage)null, value);
	}
	public void unsetReference_designation(ETemplate_location_in_structured_template type) throws SdaiException {
		unsetReference_designator((jsdai.SProduct_structure_schema.EAssembly_component_usage)null);
	}
	public static jsdai.dictionary.EAttribute attributeReference_designation(ETemplate_location_in_structured_template type) throws SdaiException {
		return attributeReference_designator((jsdai.SProduct_structure_schema.EAssembly_component_usage)null);
	}
	// ENDOF taken from TLIST
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAssembly_component_usage.definition);

		setMappingConstraints(context, this);
		
		setReference_feature(context, this);
		
		setPlacement_status(context, this);
		
		
		unsetPlacement_status(null);
		unsetReference_feature(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetReference_feature(context, this);
		
		unsetPlacement_status(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{[stratum_specific_part_template_location_in_padstack_definition &lt;=
	*  assembly_component_usage &lt;=
	*  product_definition_usage &lt;=
	*  product_definition_relationship
	*  product_definition_relationship.name = 'part template location in padstack definition']
	*  [stratum_specific_part_template_location_in_padstack_definition &lt;=
	*  property_definition]}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPart_feature_based_template_location armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxTemplate_location_in_structured_template.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart_feature_based_template_location armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.unsetMappingConstraints(context, armEntity);
	}

	/**
	* Sets/creates data for reference_feature constraints.
	*
	* <p>
		attribute_mapping reference_feature(reference_feature, $PATH, Shape_element);
			assembly_component_usage <=
			product_definition_usage <=
			product_definition_relationship
			characterized_product_definition = product_definition_relationship
			characterized_definition = characterized_product_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			property_definition_relationship.related_property_definition
			{property_definition_relationship
			property_definition_relationship.name = 'reference feature'}
			property_definition_relationship.relating_property_definition ->
			property_definition.definition ->
			characterized_definition = shape_definition
			shape_definition = shape_aspect
			shape_aspect
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// ACU <- PD <- PDR -> PD -> SA
	public static void setReference_feature(SdaiContext context, EPart_feature_based_template_location armEntity) throws SdaiException
	{
		unsetReference_feature(context, armEntity);
		if(armEntity.testReference_feature(null)){
			EPart_feature feature = armEntity.getReference_feature(null);
			// PDR -> SA
			LangUtils.Attribute_and_value_structure[] structure = {
				new LangUtils.Attribute_and_value_structure(
				   CProperty_definition.attributeDefinition(null), 
				   feature)};
			EProperty_definition epd = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, structure);
			if(!epd.testName(null)){
				epd.setName(null, "");
			}
			// ACU <- PD
			LangUtils.Attribute_and_value_structure[] structure2 = {
				new LangUtils.Attribute_and_value_structure(
				   CProperty_definition.attributeDefinition(null), 
				   armEntity)};
			EProperty_definition epd2 = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, structure2);
			if(!epd2.testName(null)){
				epd2.setName(null, "");
			}
			EProperty_definition_relationship epdr = (EProperty_definition_relationship)
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setRelated_property_definition(null, epd2);
			epdr.setRelating_property_definition(null, epd);
			epdr.setName(null, "reference feature");
			epdr.setDescription(null, "");
			// SELF\template_location_in_structured_template.reference_designation : STRING :=  reference_feature \ shape_element . element_name ;
			armEntity.setReference_designation(null, feature.getElement_name(null));
			// AIM gap as well as trying to not violate UR on product_definition_usage
			armEntity.setId(null, feature.getElement_name(null));
		}
	}

	public static void unsetReference_feature(SdaiContext context, EPart_feature_based_template_location armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator apdIter = apd.createIterator();
		while(apdIter.next()){
			EProperty_definition epd = apd.getCurrentMember(apdIter);
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apdr);
			SdaiIterator apdrIter = apdr.createIterator();
			while(apdrIter.next()){
				EProperty_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
				if((epdr.testName(null))&&(epdr.getName(null).equals("reference feature"))){
					epdr.deleteApplicationInstance();
				}
			}			
		}
	}

	public static void setPlacement_status(SdaiContext context, EPart_feature_based_template_location armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.setPlacement_status(context, armEntity);
	}

	public static void unsetPlacement_status(SdaiContext context, EPart_feature_based_template_location armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.unsetPlacement_status(context, armEntity);
	}

	
}
