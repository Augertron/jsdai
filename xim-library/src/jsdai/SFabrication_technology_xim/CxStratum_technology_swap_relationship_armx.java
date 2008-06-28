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
import jsdai.util.LangUtils;
import jsdai.SFabrication_technology_mim.CStratum_technology_occurrence_swap_relationship;
import jsdai.SFabrication_technology_mim.CStratum_technology_swap_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.CShape_aspect;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;

public class CxStratum_technology_swap_relationship_armx extends CStratum_technology_swap_relationship_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

    // Taken from Property_definition_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProperty_definition_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProperty_definition_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProperty_definition_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition_relationship type) throws SdaiException {
		return a0$;
	}

	//<01> generating methods for consolidated attribute:  description
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProperty_definition_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProperty_definition_relationship type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProperty_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProperty_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProperty_definition_relationship type) throws SdaiException {
		return a1$;
	}

	// attribute (current explicit or supertype explicit) : relating_property_definition, base type: entity property_definition
/*	public static int usedinRelating_property_definition(EProperty_definition_relationship type, jsdai.SProduct_property_definition_schema.EProperty_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testRelating_property_definition(EProperty_definition_relationship type) throws SdaiException {
		return test_instance(a2);
	}
	public jsdai.SProduct_property_definition_schema.EProperty_definition getRelating_property_definition(EProperty_definition_relationship type) throws SdaiException {
		return (jsdai.SProduct_property_definition_schema.EProperty_definition)get_instance(a2);
	}*/
	public void setRelating_property_definition(EProperty_definition_relationship type, jsdai.SProduct_property_definition_schema.EProperty_definition value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetRelating_property_definition(EProperty_definition_relationship type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_property_definition(EProperty_definition_relationship type) throws SdaiException {
		return a2$;
	}

	// attribute (current explicit or supertype explicit) : related_property_definition, base type: entity property_definition
/*	public static int usedinRelated_property_definition(EProperty_definition_relationship type, jsdai.SProduct_property_definition_schema.EProperty_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testRelated_property_definition(EProperty_definition_relationship type) throws SdaiException {
		return test_instance(a3);
	}
	public jsdai.SProduct_property_definition_schema.EProperty_definition getRelated_property_definition(EProperty_definition_relationship type) throws SdaiException {
		return (jsdai.SProduct_property_definition_schema.EProperty_definition)get_instance(a3);
	}*/
	public void setRelated_property_definition(EProperty_definition_relationship type, jsdai.SProduct_property_definition_schema.EProperty_definition value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetRelated_property_definition(EProperty_definition_relationship type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeRelated_property_definition(EProperty_definition_relationship type) throws SdaiException {
		return a3$;
	}
	
    // ENDOF taken from Property_definition_relationship
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_technology_swap_relationship.definition);

		setMappingConstraints(context, this);

		// setPrimary_stratum_technology(context, this);

		// setSecondary_stratum_technology(context, this);
		
		// unsetPrimary_stratum_technology(null);
		// unsetSecondary_stratum_technology(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// unsetPrimary_stratum_technology(context, this);

		// unsetSecondary_stratum_technology(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_technology_swap_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
//		if(!armEntity.testName((EProperty_definition_relationship)null)){
			armEntity.setName((EProperty_definition_relationship)null, "");
//		}
//		if(!armEntity.testDescription((EProperty_definition_relationship)null)){
			armEntity.setDescription((EProperty_definition_relationship)null, "");
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_technology_swap_relationship_armx armEntity) throws SdaiException
	{
	}
	
	/**
	* Sets/creates data for attribute primary_stratum_technology.
	attribute_mapping primary_stratum_technology(primary_stratum_technology, $PATH, Stratum_technology_armx);
		stratum_technology_swap_relationship <=
		property_definition_relationship
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_object
		characterized_object =>
		stratum_technology
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// STSR.ing -> PD -> ST
/*	public static void setPrimary_stratum_technology(SdaiContext context, EStratum_technology_swap_relationship_armx armEntity) throws SdaiException
	{
		unsetPrimary_stratum_technology(context, armEntity);
		
		if(armEntity.testPrimary_stratum_technology(null)){
			EStratum_technology_armx est = armEntity.getPrimary_stratum_technology(null);
	         // PD -> ST
			LangUtils.Attribute_and_value_structure[] pdStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CProperty_definition.attributeDefinition(null),
					est)};

			EProperty_definition epd = (EProperty_definition) LangUtils
					.createInstanceIfNeeded(context,
							CProperty_definition.definition,
							pdStructure);
			if(!epd.testName(null))
				epd.setName(null, "");
			// STSR.ing -> PD
			armEntity.setRelating_property_definition(null, epd);
		}
	}

	public static void unsetPrimary_stratum_technology(SdaiContext context, EStratum_technology_swap_relationship_armx armEntity) throws SdaiException
	{
		armEntity.unsetRelating_property_definition(null);
	}*/
	
	/**
	* Sets/creates data for attribute secondary_stratum_technology.
	attribute_mapping secondary_stratum_technology(secondary_stratum_technology, $PATH, Stratum_technology_armx);
		stratum_technology_swap_relationship <=
		property_definition_relationship
		property_definition_relationship.related_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_object
		characterized_object =>
		stratum_technology
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// STSR.ing -> PD -> ST
/*	public static void setSecondary_stratum_technology(SdaiContext context, EStratum_technology_swap_relationship_armx armEntity) throws SdaiException
	{
		unsetPrimary_stratum_technology(context, armEntity);
		
		if(armEntity.testSecondary_stratum_technology(null)){
			EStratum_technology_armx est = armEntity.getSecondary_stratum_technology(null);
	         // PD -> ST
			LangUtils.Attribute_and_value_structure[] pdStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CProperty_definition.attributeDefinition(null),
					est)};

			EProperty_definition epd = (EProperty_definition) LangUtils
					.createInstanceIfNeeded(context,
							CProperty_definition.definition,
							pdStructure);
			if(!epd.testName(null))
				epd.setName(null, "");
			// STSR.ed -> PD
			armEntity.setRelated_property_definition(null, epd);
		}
	}

	public static void unsetSecondary_stratum_technology(SdaiContext context, EStratum_technology_swap_relationship_armx armEntity) throws SdaiException
	{
		armEntity.unsetRelated_property_definition(null);
	}
*/	
}
