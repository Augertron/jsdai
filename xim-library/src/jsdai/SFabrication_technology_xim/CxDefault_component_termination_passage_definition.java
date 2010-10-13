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
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SFabrication_technology_mim.CPassage_technology;
import jsdai.SProduct_property_definition_schema.ECharacterized_object;
import jsdai.SProduct_property_definition_schema.EProperty_definition;

public class CxDefault_component_termination_passage_definition extends CDefault_component_termination_passage_definition implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a0$;
	}
	// ENDOF From CProperty_definition.java

	// FROM Characterized_object
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(ECharacterized_object type) throws SdaiException {
		return test_string(a4);
	}
	public String getDescription(ECharacterized_object type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(ECharacterized_object type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(ECharacterized_object type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(ECharacterized_object type) throws SdaiException {
		return a4$;
	}
	// ENDOF FROM Characterized_object	
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPassage_technology.definition);

		setMappingConstraints(context, this);
		
		// as_finished_inter_stratum_extent 		
		//setAs_finished_inter_stratum_extent(context, this);
		
		// allowed_component_terminal_extent
		setAllowed_component_terminal_extent(context, this);
		
		// EX Parameters
      // plated_passage
		setPlated_passage(context, this);
      
		// as_finished_passage_extent
		setAs_finished_passage_extent(context, this);
		
      // as_finished_deposition_thickness
		setAs_finished_deposition_thickness(context, this);
		
		// maximum_aspect_ratio
		setMaximum_aspect_ratio(context, this);
		
		setPassage_terminus_condition(context, this);
		
		// Clean ARM
		// as_finished_inter_stratum_extent 		
		//unsetAs_finished_inter_stratum_extent(null);

		// allowed_component_terminal_extent
		unsetAllowed_component_terminal_extent(null);

		// EX Parameters
      // plated_passage
		unsetPlated_passage(null);
      
		// as_finished_passage_extent
		unsetAs_finished_passage_extent(null);
		
      // as_finished_deposition_thickness
		unsetAs_finished_deposition_thickness(null);
		
		// maximum_aspect_ratio
		unsetMaximum_aspect_ratio(null);
		
		unsetPassage_terminus_condition(null);		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// as_finished_inter_stratum_extent 		
		// unsetAs_finished_inter_stratum_extent(context, this);
		
		// allowed_component_terminal_extent
		unsetAllowed_component_terminal_extent(context, this);

		// EX Parameters
      // plated_passage
		unsetPlated_passage(context, this);
      
		// as_finished_passage_extent
		unsetAs_finished_passage_extent(context, this);
		
      // as_finished_deposition_thickness
		unsetAs_finished_deposition_thickness(context, this);
		
		// maximum_aspect_ratio
		unsetMaximum_aspect_ratio(context, this);
		
		unsetPassage_terminus_condition(context, this);
		
		CxPassage_technology_armx.cleanAIM_stuff(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	passage_technology &lt;=
	*  shape_aspect
	*  {shape_aspect
	*  shape_aspect.description = 'default component termination passage definition'}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EDefault_component_termination_passage_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPassage_technology_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription((ECharacterized_object)null, "default component termination passage definition");
	}

	public static void unsetMappingConstraints(SdaiContext context, EDefault_component_termination_passage_definition armEntity) throws SdaiException
	{
		CxPassage_technology_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((ECharacterized_object)null);
	}

   //********** "passage_technology" attributes
 /**
  * Sets/creates data for Plated_passage attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // PT <- PD <- PDR -> R
 public static void setPlated_passage(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.setPlated_passage(context, armEntity);
 }

 /**
  * Unsets/deletes data for plated_passage attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetPlated_passage(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.unsetPlated_passage(context, armEntity);
 }

 /**
  * Sets/creates data for As_finished_passage_extent attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // PT <- PD <- PDR -> R
 public static void setAs_finished_passage_extent(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.setAs_finished_passage_extent(context, armEntity);
 }

 /**
  * Unsets/deletes data for As_finished_passage_extent attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetAs_finished_passage_extent(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.unsetAs_finished_passage_extent(context, armEntity);
 }

 /**
  * Sets/creates data for As_finished_deposition_thickness attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // PT <- PD <- PDR -> R
 public static void setAs_finished_deposition_thickness(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.setAs_finished_deposition_thickness(context, armEntity);
 }

 /**
  * Unsets/deletes data for As_finished_deposition_thickness attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetAs_finished_deposition_thickness(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.unsetAs_finished_deposition_thickness(context, armEntity);
 }

 /**
  * Sets/creates data for maximum_aspect_ratio attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // PT <- PD <- PDR -> R
 public static void setMaximum_aspect_ratio(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.setMaximum_aspect_ratio(context, armEntity);
 }

 /**
  * Unsets/deletes data for Maximum_aspect_ratio attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetMaximum_aspect_ratio(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.unsetPlated_passage(context, armEntity);
 }

 /**
  * Sets/creates data for Passage_terminus_condition attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // PT <- PD <- PDR -> R
 public static void setPassage_terminus_condition(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.setPassage_terminus_condition(context, armEntity);
 }

 /**
  * Unsets/deletes data for Passage_terminus_condition attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetPassage_terminus_condition(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	CxPassage_technology_armx.unsetPassage_terminus_condition(context, armEntity);
 }
 
 
	//********** "default_component_termination_passage_definition" attributes

	/**
	* Sets/creates data for allowed_component_terminal_extent attribute.
	*
	* <p>
	*  attribute_mapping allowed_component_terminal_extent_length_data_element (minimum_allowed_component_terminal_extent
	* , (*PATH*), length_data_element);
	* 	passage_technology <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	{representation
	* 	representation.name = `physical characteristics representation'}
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `minimum allowed component terminal extent'}
	* 	representation_item =>
	* 	measure_representation_item <=
	* 	measure_with_unit =>
	* 	length_measure_with_unit
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAllowed_component_terminal_extent(SdaiContext context, EDefault_component_termination_passage_definition armEntity) throws SdaiException
	{
		//unset old values
		unsetAllowed_component_terminal_extent(context, armEntity);
		if (armEntity.testAllowed_component_terminal_extent(null))
		{
	      ETolerance_characteristic characteristic = armEntity.getAllowed_component_terminal_extent(null);
	      
	      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "allowed component terminal extent", characteristic);
		}
	}


	/**
	* Unsets/deletes data for allowed_component_terminal_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAllowed_component_terminal_extent(SdaiContext context, EDefault_component_termination_passage_definition armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "allowed component terminal extent");
	}

	
}
