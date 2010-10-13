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

package jsdai.SLayered_interconnect_module_design_xim;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $$
* $ $
*/

//import jsdai.SProduct_definition_schema.*;
import jsdai.SPart_view_definition_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
//import jsdai.SProduct_property_representation_schema.*;
import jsdai.SFabrication_technology_xim.EStratum_technology_occurrence_armx;
import jsdai.SLayered_interconnect_module_design_mim.CStratum;
import jsdai.SMaterial_property_definition_schema.*;
//import jsdai.SRepresentation_schema.*;
import jsdai.lang.*;
import jsdai.libutil.*;
//import jsdai.dictionary.*;

public class CxStratum_armx extends CStratum_armx implements EMappedXIMEntity
{

	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a6$;
	}
	// ENDOF From CProperty_definition.java

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

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}
	
	// From STRATUM - property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a8);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a8 = get_instance_select(a8);
		return (EEntity)a8;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a8 = set_instanceX(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
	}
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "Part_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string
		// setAdditional_characterization(context, this);

		//additional_context - this is DERIVED to some magic string
		// setAdditional_contexts(context, this);

		// Id_x
//		setId_x(context, this);
		
		//********** "stratum" attributes

		// Definition_x
		setDefinition_x(context, this);

		// Clean ARM
		// unsetAdditional_characterization(null); - this is DERIVED to some magic string
		// unsetAdditional_contexts(null);
		unsetDefinition_x(null);
		
//		unsetId_x(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "Part_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string 
		// unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		// Id_x
//		unsetId_x(context, this);
		
		//********** "stratum" attributes

		// definition_x
		setDefinition_x(context, this);
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	stratum <=
	* 	product_definition
	* 	{product_definition
	* 	(product_definition.name = `design layer')
	* 	(product_definition.name = `documentation layer')
	* 	(product_definition.name = `generic stratum')}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		CxPart_view_definition.setMappingConstraints(context, armEntity);
//		CxItem_shape.setMappingConstraints(context, armEntity);
		// CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "generic stratum");
		// AIM gap
		armEntity.setDefinition(null, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		CxPart_view_definition.unsetMappingConstraints(context, armEntity);
//		CxItem_shape.unsetMappingConstraints(context, armEntity);
		// CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "");
	}
	
	//********** "managed_design_object" attributes

	//********** "ee_product_definition" attributes
	/**
	* Sets/creates data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAdditional_characterization(SdaiContext context, EPart_view_definition armEntity) throws SdaiException
	{
		CxPart_view_definition.setAdditional_characterization(context, armEntity);
	}

	/**
	* Unsets/deletes data for product_definition_id attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAdditional_contexts(SdaiContext context, EPart_view_definition armEntity) throws SdaiException
	{
		CxPart_view_definition.unsetAdditional_contexts(context, armEntity);
	}

   /**
    * Sets/creates data for id_x attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
/* Removed from XIM - see bug #3610	
   public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
   	CxItem_shape.setId_x(context, armEntity);
   }
*/
 /**
  * Unsets/deletes data for id_x attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
/* Removed from XIM - see bug #3610	
 public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
 	CxItem_shape.unsetId_x(context, armEntity);
 }
*/

	// Stratum

	/**
	* Sets/creates data for of_technology attribute.
	*
	* <p>
	*  attribute_mapping of_technology_stratum_technology (of_technology
	* , (*PATH*), stratum_technology);
	* 	stratum <=
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_relationship.related_property_definition
	* 	{property_definition_relationship
	* 	property_definition_relationship.name = `technology usage'}
	* 	property_definition_relationship
	* 	property_definition_relationship.relating_property_definition ->
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_object
	* 	characterized_object =>
	* 	stratum_technology
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDefinition_x(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetDefinition_x(context, armEntity);

		if (armEntity.testDefinition_x(null)){

			EStratum_technology_occurrence_armx armOf_technology = armEntity.getDefinition_x(null);

			//second property_definition
		   //property_definition_relationship
			jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship 
				relationship = (jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship) context.working_model.createEntityInstance(jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship.definition);
			relationship.setRelated_property_definition(null, armEntity);
			relationship.setRelating_property_definition(null, armOf_technology);
			relationship.setName(null, "definition");
			relationship.setDescription(null, "");
		}
	}


	/**
	* Unsets/deletes data for of_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDefinition_x(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		//property_definition_relationship
		AProperty_definition_relationship aRelationship = new AProperty_definition_relationship();
		EProperty_definition_relationship relationship = null;
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, aRelationship);
		for (int j = 1; j <= aRelationship.getMemberCount(); j++) {
			relationship = aRelationship.getByIndex(j);
			if (relationship.testName(null) && relationship.getName(null).equals("definition")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
}
