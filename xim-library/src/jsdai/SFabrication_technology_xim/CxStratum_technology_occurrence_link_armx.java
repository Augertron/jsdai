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
import jsdai.SFabrication_technology_mim.CStratum_technology_occurrence_link;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SProduct_property_definition_schema.EProperty_definition;

public class CxStratum_technology_occurrence_link_armx extends CStratum_technology_occurrence_link_armx implements EMappedXIMEntity
{

	// Taken from PD
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
	// End of taken from PD
	
	// Taken from PDR
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition_relationship type) throws SdaiException {
		return test_string(a3);
	}
	public String getName(EProperty_definition_relationship type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setName(EProperty_definition_relationship type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(EProperty_definition_relationship type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition_relationship type) throws SdaiException {
		return a3$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProperty_definition_relationship type) throws SdaiException {
		return test_string(a4);
	}
	public String getDescription(EProperty_definition_relationship type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(EProperty_definition_relationship type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(EProperty_definition_relationship type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProperty_definition_relationship type) throws SdaiException {
		return a4$;
	}
	
	// ENDOF taken from PDR
	
	public int attributeState = ATTRIBUTES_MODIFIED;	


	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_technology_occurrence_link.definition);

		setMappingConstraints(context, this);
		
		// base_stratum_technology_occurrence : OPTIONAL up_or_down;
		setBase_stratum_technology_occurrence(context, this);
		
		// base_stratum_technology_occurrence : OPTIONAL up_or_down;
		unsetBase_stratum_technology_occurrence(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// base_stratum_technology_occurrence : OPTIONAL up_or_down;
		unsetBase_stratum_technology_occurrence(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_technology_occurrence_link_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// Fill AIM gap
//		if(!armEntity.testName((EProperty_definition)null)){
			armEntity.setName((EProperty_definition)null, "");
//		}
//		if(!armEntity.testName((EProperty_definition_relationship)null)){
			armEntity.setName((EProperty_definition_relationship)null, "");
//		}
//		if(!armEntity.testDescription((EProperty_definition_relationship)null)){
			armEntity.setDescription((EProperty_definition_relationship)null, "");
//		}
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_technology_occurrence_link_armx armEntity) throws SdaiException
	{
	}
	
	/**
	* Sets/creates data for base_stratum_technology_occurrence attribute.
	*
	attribute_mapping base_stratum_technology_occurrence(base_stratum_technology_occurrence, $PATH);
		stratum_technology_occurrence_link <=
		stratum_technology_occurrence_relationship <=
		property_definition
		property_definition.name
		{(property_definition.name = 'independent')
		(property_definition.name = 'precedent')
		(property_definition.name = 'subsequent')}
	end_attribute_mapping;	
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setBase_stratum_technology_occurrence(SdaiContext context, EStratum_technology_occurrence_link_armx armEntity) throws SdaiException
	{
		unsetBase_stratum_technology_occurrence(context, armEntity);
		if(armEntity.testBase_stratum_technology_occurrence(null)){
			int baseSTO = armEntity.getBase_stratum_technology_occurrence(null);
			String value = EUp_or_down.toString(baseSTO).toLowerCase().replace('_', ' ');
			armEntity.setName((EProperty_definition)null, value);
		}
	}

	public static void unsetBase_stratum_technology_occurrence(SdaiContext context, EStratum_technology_occurrence_link_armx armEntity) throws SdaiException
	{
		armEntity.setName((EProperty_definition)null, "");
	}
	
}
