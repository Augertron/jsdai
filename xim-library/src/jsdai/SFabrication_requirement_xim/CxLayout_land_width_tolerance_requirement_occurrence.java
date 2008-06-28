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

package jsdai.SFabrication_requirement_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SInterconnect_placement_requirements_mim.CLayout_spacing_requirement_occurrence;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRequirement_decomposition_mim.CPredefined_requirement_view_definition;
import jsdai.SRequirement_decomposition_xim.*;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxLayout_land_width_tolerance_requirement_occurrence extends CLayout_land_width_tolerance_requirement_occurrence implements EMappedXIMEntity
{

	
	// Taken from property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProduct_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProduct_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProduct_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return a0$;
	}

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

	public static jsdai.dictionary.EAttribute attributeDefinition(EProduct_definition type) throws SdaiException {
		return a2$;
	}
	// END OF taken from property_definition
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CPredefined_requirement_view_definition.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

			//required_analytical_representation
			setRequired_analytical_representation(context, this);


	      //********** "Layout_land_width_tolerance_requirement_occurrence" attributes

	      setMating_feature_pitch_class(context, this);

	      setMaximum_negative_deviation(context, this);


	      // Clean ARM
	      // unsetAssociated_definition(null);
	      unsetRequired_analytical_representation(null);
	      unsetRequired_functional_specification(null);
	      // unsetRequired_part(null);
	      unsetRequired_characteristic(null);

	      unsetMating_feature_pitch_class(null);
	      unsetMaximum_negative_deviation(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "Requirement_definition_property" attributes

		//required_analytical_representation
		unsetRequired_analytical_representation(context, this);

		//required_characteristic
		unsetRequired_characteristic(context, this);

		//required_material - INVERSE
		// setRequired_material(context, this);

      //********** "Layout_land_width_tolerance_requirement_occurrence" attributes
	      unsetMating_feature_pitch_class(context, this);

	      unsetMaximum_negative_deviation(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	mapping_constraints;
		predefined_requirement_view_definition <=
		product_definition
		{product_definition
		product_definition.description = 'layout land width tolerance requirement'}

	end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ELayout_land_width_tolerance_requirement_occurrence armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "layout land width tolerance requirement");
	}

	public static void unsetMappingConstraints(SdaiContext context, ELayout_land_width_tolerance_requirement_occurrence armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}
	
	
	//********** "managed_design_object" attributes

	//********** "Requirement_definition_property" attributes

	/**
	* Sets/creates data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_analytical_representation(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_analytical_representation(context, armEntity);
	}


	/**
	* Sets/creates data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void setRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.setRequired_specification(context, armEntity);
	}
*/

	/**
	* Unsets/deletes data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void unsetRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.unsetRequired_specification(context, armEntity);
	}
*/

	/**
	* Sets/creates data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_characteristic(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_characteristic(context, armEntity);
	}

	//********** "Layout_land_width_tolerance_requirement_occurrence" attributes

	/**
	* Sets/creates data for Mating_feature_pitch_class attribute.
	*
	*
	attribute_mapping mating_feature_pitch_class(mating_feature_pitch_class, $PATH, Pitch_class);
		predefined_requirement_view_definition <=
		product_definition			
		characterized_product_definition = product_definition
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		{property_definition.name = 'mating feature pitch class'}
		property_definition <-
		property_definition_representation.definition
		property_definition_representation.used_representation ->
		representation
	end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PRVD <- PD <- PDR -> R
	public static void setMating_feature_pitch_class(SdaiContext context, ELayout_land_width_tolerance_requirement_occurrence armEntity) throws SdaiException
	{
		unsetMating_feature_pitch_class(context, armEntity);
		if (armEntity.testMating_feature_pitch_class(null)){
			EPitch_class pitchClass = armEntity.getMating_feature_pitch_class(null);
			// PD
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "mating feature pitch class");
		   // PDR
           EProperty_definition_representation epdr = (EProperty_definition_representation)
           		context.working_model.createEntityInstance(CProperty_definition_representation.definition);
           epdr.setDefinition(null, epd);
           epdr.setUsed_representation(null, pitchClass);
      }
   }


	/**
	* Unsets/deletes data for reference_design_object_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> DO, remove DO
	public static void unsetMating_feature_pitch_class(SdaiContext context, ELayout_land_width_tolerance_requirement_occurrence armEntity) throws SdaiException
	{
		String keyword = "mating feature pitch class";
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterPD = apd.createIterator();
		while(iterPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterPD);
			if((epd.testName(null))&&(epd.getName(null).equals(keyword))){
				AProperty_definition_relationship apdr = new AProperty_definition_relationship();
				CProperty_definition_relationship.usedinRelating_property_definition(null, epd, context.domain, apdr);
				SdaiIterator iterPDR = apdr.createIterator();
				while(iterPDR.next()){
					EProperty_definition_relationship epdr = apdr.getCurrentMember(iterPDR);
					epdr.deleteApplicationInstance();
				}
				epd.deleteApplicationInstance();
			}
		}
	}

	/**
	* Sets/creates data for Maximum_negative_deviation attribute.
	*
	*
	attribute_mapping maximum_negative_deviation(maximum_negative_deviation, $PATH, length_measure_with_unit);
		predefined_requirement_view_definition <=
		product_definition			
		characterized_product_definition = product_definition
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		{property_definition.name = 'maximum negative deviation'}
		property_definition <-
		property_definition_representation.definition 
		property_definition_representation.used_representation ->
		representation
		representation.items[i] ->
		representation_item =>
		measure_representation_item <=
		measure_with_unit =>
		length_measure_with_unit
	end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PRVD <- PD <- PDR -> R -> LMWU
	public static void setMaximum_negative_deviation(SdaiContext context, ELayout_land_width_tolerance_requirement_occurrence armEntity) throws SdaiException
	{
		unsetMaximum_negative_deviation(context, armEntity);
		if (armEntity.testMaximum_negative_deviation(null)){
			ELength_measure_with_unit elmwu = armEntity.getMaximum_negative_deviation(null);
			if(!(elmwu instanceof EMeasure_representation_item)){
		       elmwu =
		           (CLength_measure_with_unit$measure_representation_item)
		           context.working_model.substituteInstance(elmwu, CLength_measure_with_unit$measure_representation_item.definition);
			}
			// PD
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "maximum negative deviation");
			// R
			LangUtils.Attribute_and_value_structure[] repStructure = {new LangUtils.Attribute_and_value_structure(
					CRepresentation.attributeItems(null), elmwu)};
			ERepresentation er = (ERepresentation)
				LangUtils.createInstanceIfNeeded(context, CRepresentation.definition,
					repStructure);
			if(!er.testName(null)){
				er.setName(null, "");
			}
			if(!er.testContext_of_items(null)){
				  LangUtils.Attribute_and_value_structure[] rcStructure =
					 {
						new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
						new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "")
					};

				  	ERepresentation_context representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
													context,
													jsdai.SRepresentation_schema.CRepresentation_context.definition,
													rcStructure);
				  	er.setContext_of_items(null, representation_context);
			}
		   // PDR
           EProperty_definition_representation epdr = (EProperty_definition_representation)
           		context.working_model.createEntityInstance(CProperty_definition_representation.definition);
           epdr.setDefinition(null, epd);
           epdr.setUsed_representation(null, er);
      }
   }


	/**
	* Unsets/deletes data for Maximum_negative_deviation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> DO, remove DO
	public static void unsetMaximum_negative_deviation(SdaiContext context, ELayout_land_width_tolerance_requirement_occurrence armEntity) throws SdaiException
	{
		String keyword = "maximum negative deviation";
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterPD = apd.createIterator();
		while(iterPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterPD);
			if((epd.testName(null))&&(epd.getName(null).equals(keyword))){
				AProperty_definition_relationship apdr = new AProperty_definition_relationship();
				CProperty_definition_relationship.usedinRelating_property_definition(null, epd, context.domain, apdr);
				SdaiIterator iterPDR = apdr.createIterator();
				while(iterPDR.next()){
					EProperty_definition_relationship epdr = apdr.getCurrentMember(iterPDR);
					epdr.deleteApplicationInstance();
				}
				epd.deleteApplicationInstance();
			}
		}
	}
	
}
