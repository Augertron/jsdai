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

package jsdai.SAltered_package_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SAltered_package_mim.*;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.EShape_aspect;

public class CxLength_trimmed_terminal_armx extends CLength_trimmed_terminal_armx implements EMappedXIMEntity{


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

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLength_trimmed_terminal.definition);

		setMappingConstraints(context, this);
		
		// Part_feature
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);

		// terminal_to_be_altered
		setTerminal_to_be_altered(context, this);
		
		// trimmed_length 
		setTrimmed_length (context, this);

		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);

		// terminal_to_be_altered
		unsetTerminal_to_be_altered(null);

		// maximum_trimmed_length 
		unsetTrimmed_length (null);

		// maximum_trimmed_length 
		unsetTrimmed_length (null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);

			// terminal_to_be_altered
			unsetTerminal_to_be_altered(context, this);

			// trimmed_length 
			unsetTrimmed_length (context, this);

	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  length_trimmed_terminal &lt;=
	 *  altered_package_terminal &lt;=
	 *  package_terminal &lt;=
	 *  shape_aspect
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
			ELength_trimmed_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxAltered_package_terminal_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ELength_trimmed_terminal_armx armEntity) throws SdaiException {
		CxAltered_package_terminal_armx.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for material_state_change.
	 * 
	 * <p>
	 *  shape_aspect &lt;-
	 *  shape_aspect_relationship.related_shape_aspect
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'precedent feature'}
	 *  shape_aspect_relationship.relating_shape_aspect -&gt;
	 *  shape_aspect
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
////////////////////////////////////////////////////////////////////
	/**
	* Sets/creates data for material_state_change attribute.
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setMaterial_state_change(context, armEntity);
	}


	/**
	* Unsets/deletes data for material_state_change attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetMaterial_state_change(context, armEntity);		
	}


	/**
	* Sets/creates data for precedent_feature attribute.
	*
	*  attribute_mapping precedent_feature_part_feature (precedent_feature
	* , (*PATH*), part_feature);
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setPrecedent_feature(context, armEntity);
	}


	/**
	* Unsets/deletes data for precedent_feature attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetPrecedent_feature(context, armEntity);		
	}
	

///////////////////////////////////////
//New since WD26
///////////////////////////////////////

	//********** "altered_package_terminal" attributes

	/**
	* Sets/creates data for terminal_to_be_altered attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTerminal_to_be_altered(SdaiContext context, EAltered_package_terminal_armx armEntity) throws SdaiException
	{
		CxAltered_package_terminal_armx.setTerminal_to_be_altered(context, armEntity);
	}


	/**
	* Unsets/deletes data for terminal_to_be_altered attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTerminal_to_be_altered(SdaiContext context, EAltered_package_terminal_armx armEntity) throws SdaiException
	{
		CxAltered_package_terminal_armx.unsetTerminal_to_be_altered(context, armEntity);
	}


	//********** "length_trimmed_terminal" attributes

	/**
	* Sets/creates data for Trimmed_length attribute.
	*
	* <p>
	*  attribute_mapping maximum_trimmed_length_length_data_element (maximum_trimmed_length
	* , (*PATH*), length_data_element);
	* 	length_trimmed_terminal <=
	* 	altered_package_terminal <=
	* 	package_terminal <=
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
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `maximum trimmed length'}
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
	public static void setTrimmed_length(SdaiContext context, ELength_trimmed_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetTrimmed_length(context, armEntity);

		if (armEntity.testTrimmed_length(null)){
	      ETolerance_characteristic characteristic = armEntity.getTrimmed_length(null);
	      
	      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "trimmed length", characteristic);
		}
	}


	/**
	* Unsets/deletes data for trimmed_length attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTrimmed_length(SdaiContext context, ELength_trimmed_terminal_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "trimmed length");
	}
	
	
}