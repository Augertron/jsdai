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
import jsdai.SFabrication_technology_mim.CStratum_technology_occurrence_swap_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SProduct_property_definition_schema.EProperty_definition;

public class CxStratum_technology_occurrence_swap_relationship_armx extends CStratum_technology_occurrence_swap_relationship_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Property_definition
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
	// ENDOF taken from Property_definition 

    // Taken from Property_definition_relationship
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

	//<01> generating methods for consolidated attribute:  description
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
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
    // ENDOF taken from Property_definition_relationship
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_technology_occurrence_swap_relationship.definition);

		setMappingConstraints(context, this);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_technology_occurrence_swap_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
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

	public static void unsetMappingConstraints(SdaiContext context, EStratum_technology_occurrence_swap_relationship_armx armEntity) throws SdaiException
	{
	}
}
