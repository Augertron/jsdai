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

package jsdai.SPart_template_2d_shape_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFabrication_technology_mim.EStratum_technology;
import jsdai.SFabrication_technology_mim.EStratum_technology_occurrence;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SPart_template_2d_shape_mim.CStratum_specific_template_location;
import jsdai.SPart_template_2d_shape_xim.CxTemplate_location_in_structured_template;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.EProperty_definition;

public class CxStratum_specific_template_location_armx extends CStratum_specific_template_location_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Product_definition_relationship
	/// methods for attribute: id, base type: STRING
	
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
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
	
	// Taken from Product_definition_relationship
	/// methods for attribute: id, base type: STRING
// TODO	
/*	
	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
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

	/// methods for attribute: name, base type: STRING
	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
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
	// ENDOF Taken from Product_definition_relationship

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_specific_template_location.definition);

		setBound_stratum(context, this);
		
		setMappingConstraints(context, this);
		
		setPlacement_status(context, this);
		
		
		unsetPlacement_status(null);
		unsetBound_stratum(null);
		// System.err.println(" SSTL "+this);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetBound_stratum(context, this);
		
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
	public static void setMappingConstraints(SdaiContext context, EStratum_specific_template_location_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxTemplate_location_in_structured_template.setMappingConstraints(context, armEntity);
//		if(!armEntity.testName((EProperty_definition)null)){
			armEntity.setName((EProperty_definition)null, "");
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_specific_template_location_armx armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.unsetMappingConstraints(context, armEntity);
	}

	/**
	* Sets/creates data for bound_stratum mapping constraints.
	*
	* <p>
	attribute_mapping bound_stratum(bound_stratum, $PATH, stratum_technology_occurrence_or_stratum_technology);
		stratum_specific_template_location <=
		(property_definition
		property_definition.definition -> characterized_definition
		characterized_definition = characterized_object 
		characterized_object =>
		stratum_technology)
		(property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'bound stratum'}
		property_definition_relationship.relating_property_definition ->
		property_definition =>
		stratum_technology_occurrence)
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SSTL <- PDR -> STO
	// SSTL -> ST
	public static void setBound_stratum(SdaiContext context, EStratum_specific_template_location_armx armEntity) throws SdaiException
	{
		unsetBound_stratum(context, armEntity);
		if(armEntity.testBound_stratum(null)){
			EEntity stratum = armEntity.getBound_stratum(null);
			if(stratum instanceof EStratum_technology){
				armEntity.setDefinition(null, stratum);
			}else{
				EProperty_definition_relationship epdr = (EProperty_definition_relationship)
					context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
				epdr.setRelated_property_definition(null, armEntity);
				epdr.setName(null, "bound stratum");
				epdr.setDescription(null, "");
				epdr.setRelating_property_definition(null, (EStratum_technology_occurrence)stratum);
				// AIM gap
				armEntity.setDefinition(null, armEntity);
			}
		}
	}

	public static void unsetBound_stratum(SdaiContext context, EStratum_specific_template_location_armx armEntity) throws SdaiException
	{
		// Case 1
		armEntity.unsetDefinition(null);
		// Case 2		
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
		SdaiIterator iter = apdr.createIterator();
		while(iter.next()){
			EProperty_definition_relationship epdr = apdr.getCurrentMember(iter);
			if((epdr.testName(null))&&(epdr.getName(null).equals("bound stratum"))){
				epdr.deleteApplicationInstance();
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
