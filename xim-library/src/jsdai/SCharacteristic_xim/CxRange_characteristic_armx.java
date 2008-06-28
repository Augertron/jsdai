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

package jsdai.SCharacteristic_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SCharacteristic_mim.CRange_characteristic;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.*;

public class CxRange_characteristic_armx extends CRange_characteristic_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from CRepresentation
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a0$;
	}
	// ENDOF Taken from CRepresentation
	
	// Taken from coordinate_representation_item
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SRepresentation_schema.ERepresentation_item type) throws SdaiException {
		return test_string(a3);
	}
	public String getName(jsdai.SRepresentation_schema.ERepresentation_item type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setName(jsdai.SRepresentation_schema.ERepresentation_item type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(jsdai.SRepresentation_schema.ERepresentation_item type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SRepresentation_schema.ERepresentation_item type) throws SdaiException {
		return a3$;
	}
	
	// ENDOF taken from coordinated_representation_item

	// ENDOF taken from CDescriptive_representation_item	
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EDescriptive_representation_item type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EDescriptive_representation_item type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EDescriptive_representation_item type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(EDescriptive_representation_item type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EDescriptive_representation_item type) throws SdaiException {
		return a4$;
	}
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CRange_characteristic.definition);

		setMappingConstraints(context, this);
		
		// range_type 
		setRange_type(context, this);
		
		// Clean ARM
		// range_type 
		unsetRange_type(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// range_type 
		unsetRange_type(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	coordinated_representation_item &lt;=
	*  [representation]
	*  [representation_item
	*  {[representation_item =&gt;
	*  descriptive_representation_item]
	*  [representation_item
	*  representation_item.name = 'range characteristic']}]
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ERange_characteristic_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		armEntity.setName((ERepresentation)null, "range characteristic");
	}

	public static void unsetMappingConstraints(SdaiContext context, ERange_characteristic_armx armEntity) throws SdaiException
	{
		armEntity.unsetName((ERepresentation)null);
	}


	//********** "range_characteristic" attributes

	/**
	* Sets/creates data for range_type attribute.
	*
	* <p>
	*  attribute_mapping range_type (range_type
	* );
	* 	coordinated_representation_item <= 
	* 	representation_item => 
	* 	descriptive_representation_item 
	* 	descriptive_representation_item.description 
	* 	{(descriptive_representation_item.description = `closed') 
	* 	(descriptive_representation_item.description = `open') 
	* 	(descriptive_representation_item.description = `lower_open') 
	* 	(descriptive_representation_item.description = `upper_open')} 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void setRange_type(SdaiContext context, ERange_characteristic_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRange_type(context, armEntity);
		
		if (armEntity.testRange_type(null))
		{ 
			int armRange_type = armEntity.getRange_type(null);
			String value = ERange_class.toString(armRange_type).toLowerCase().replace('_', ' ');
			armEntity.setDescription(null, value);
		}
	}


	/**
	* Unsets/deletes data for range_type attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetRange_type(SdaiContext context, ERange_characteristic_armx armEntity) throws SdaiException
	{
		armEntity.unsetDescription(null);
	}

	
	
}
