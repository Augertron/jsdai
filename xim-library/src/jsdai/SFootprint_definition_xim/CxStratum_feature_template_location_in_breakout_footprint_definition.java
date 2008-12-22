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
import jsdai.SLayered_interconnect_complex_template_xim.CxStratum_specific_template_location_armx;
import jsdai.SLayered_interconnect_complex_template_xim.CxTemplate_location_in_structured_template;
import jsdai.SLayered_interconnect_complex_template_xim.ETemplate_location_in_structured_template;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SLayered_interconnect_complex_template_mim.CStratum_specific_template_location;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;

public class CxStratum_feature_template_location_in_breakout_footprint_definition extends CStratum_feature_template_location_in_breakout_footprint_definition implements EMappedXIMEntity
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
	/// methods for attribute: description, base type: STRING
	/*	public boolean testDescription(EProduct_definition_relationship type) throws SdaiException {
			return test_string(a2);
		}
		public String getDescription(EProduct_definition_relationship type) throws SdaiException {
			return get_string(a2);
		}*/
		public void setDescription(EProduct_definition_relationship type, String value) throws SdaiException {
			a2 = set_string(value);
		}
		public void unsetDescription(EProduct_definition_relationship type) throws SdaiException {
			a2 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition_relationship type) throws SdaiException {
			return a2$;
		}
	
	// ENDOF Taken from Product_definition_relationship

	// From Property_definition
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

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProperty_definition type) throws SdaiException {
		return test_string(a8);
	}
	public String getDescription(EProperty_definition type) throws SdaiException {
		return get_string(a8);
	}*/
	public void setDescription(EProperty_definition type, String value) throws SdaiException {
		a8 = set_string(value);
	}
	public void unsetDescription(EProperty_definition type) throws SdaiException {
		a8 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProperty_definition type) throws SdaiException {
		return a8$;
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
		a9 = set_instance(a9, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a9$;
	}
	// ENDOF from Property_definition	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_specific_template_location.definition);
	
        // original_padstack : part_feature_based_template_location;
		setOriginal_padstack(context, this);
		
        // breakout_padstack : template_location_in_structured_template;
		setBreakout_padstack(context, this);
		
		setBound_stratum(context, this);

		setMappingConstraints(context, this);
		
		setPlacement_status(context, this);
		
		
		unsetPlacement_status(null);
		
		// original_padstack : part_feature_based_template_location;
		unsetOriginal_padstack(null);
		
        // breakout_padstack : template_location_in_structured_template;
		unsetBreakout_padstack(null);
		
		unsetBound_stratum(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// original_padstack : part_feature_based_template_location;
		unsetOriginal_padstack(context, this);
		
        // breakout_padstack : template_location_in_structured_template;
		unsetBreakout_padstack(context, this);
		
		unsetBound_stratum(context, this);
		
		unsetPlacement_status(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxStratum_specific_template_location_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
	{
		CxStratum_specific_template_location_armx.unsetMappingConstraints(context, armEntity);
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setBound_stratum(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxStratum_specific_template_location_armx.setBound_stratum(context, armEntity);
	}

	public static void unsetBound_stratum(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
	{
		CxStratum_specific_template_location_armx.unsetBound_stratum(context, armEntity);
	}
	
	
	/**
	* Sets/creates data for Original_padstack attribute.
	*attribute_mapping original_padstack(original_padstack, $PATH, Part_feature_based_template_location);
		stratum_specific_template_location <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		characterized_product_definition = product_definition_relationship
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship.name = 'original padstack'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_product_definition
		characterized_product_definition
		characterized_product_definition = product_definition_relationship
		product_definition_relationship =>
		product_definition_usage =>
		assembly_component_usage
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SSTL <- PD <- PDR -> PD -> ACU
	public static void setOriginal_padstack(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
	{
		unsetOriginal_padstack(context, armEntity);
		if(armEntity.testOriginal_padstack(null)){
			EPart_feature_based_template_location location = armEntity.getOriginal_padstack(null);
			// PDR -> ACU
			LangUtils.Attribute_and_value_structure[] structure = {
				new LangUtils.Attribute_and_value_structure(
				   CProperty_definition.attributeDefinition(null), 
				   location)};
			EProperty_definition epd = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, structure);
			if(!epd.testName(null)){
				epd.setName(null, "");
			}
			// SSTL <- PD
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
			epdr.setName(null, "original padstack");
			epdr.setDescription(null, "");
		}
	}

	public static void unsetOriginal_padstack(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
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
				if((epdr.testName(null))&&(epdr.getName(null).equals("original padstack"))){
					epdr.deleteApplicationInstance();
				}
			}			
		}
	}

	
	/**
	* Sets/creates data for breakout_padstack attribute.
	*attribute_mapping breakout_padstack(breakout_padstack, $PATH, Template_location_in_structured_template);
		stratum_specific_template_location <=
		assembly_component_usage <=
		product_definition_usage <=
		product_definition_relationship
		characterized_product_definition = product_definition_relationship
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship.name = 'breakout padstack'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_product_definition
		characterized_product_definition
		characterized_product_definition = product_definition_relationship
		{product_definition_relationship.name = 'tlist'}
		product_definition_relationship =>
		product_definition_usage =>
		assembly_component_usage
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SSTL <- PD <- PDR -> PD -> ACU
	public static void setBreakout_padstack(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
	{
		unsetBreakout_padstack(context, armEntity);
		if(armEntity.testBreakout_padstack(null)){
			ETemplate_location_in_structured_template location = armEntity.getBreakout_padstack(null);
			// PDR -> ACU
			LangUtils.Attribute_and_value_structure[] structure = {
				new LangUtils.Attribute_and_value_structure(
				   CProperty_definition.attributeDefinition(null), 
				   location)};
			EProperty_definition epd = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, structure);
			if(!epd.testName(null)){
				epd.setName(null, "");
			}
			// SSTL <- PD
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
			epdr.setName(null, "breakout padstack");
			epdr.setDescription(null, "");
		}
	}

	public static void unsetBreakout_padstack(SdaiContext context, EStratum_feature_template_location_in_breakout_footprint_definition armEntity) throws SdaiException
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
				if((epdr.testName(null))&&(epdr.getName(null).equals("breakout padstack"))){
					epdr.deleteApplicationInstance();
				}
			}			
		}
	}
	
	public static void setPlacement_status(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.setPlacement_status(context, armEntity);
	}

	public static void unsetPlacement_status(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.unsetPlacement_status(context, armEntity);
	}
	
}
