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

package jsdai.SLayered_interconnect_simple_template_xim;

/**
* @author Giedrius Liutkus
* @version $ $
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFabrication_technology_xim.EPassage_technology_armx;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SLayered_interconnect_simple_template_mim.CInter_stratum_feature_template;
import jsdai.SPart_template_shape_with_parameters_xim.CxGeometric_template_armx;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.CxProduct_view_definition;
import jsdai.SProduct_view_definition_xim.EProduct_view_definition;
import jsdai.SShape_property_assignment_xim.CxItem_shape;
import jsdai.SShape_property_assignment_xim.EItem_shape;

public class CxInter_stratum_feature_template_armx extends CInter_stratum_feature_template_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition

	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a2);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (EEntity)a2;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a2$;
	}
	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a0$;
	}

	// END OF Property_definition

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a5);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a5$;
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
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CInter_stratum_feature_template.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		// SETTING DERIVED
		// setDefinition(null, this);

		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		setId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

		//********** "inter_stratum_feature_template" attributes
		//of_passage_technology
		setOf_passage_technology(context, this);

		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
		unsetOf_passage_technology(null);
		
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		// SETTING DERIVED
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		unsetId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		//********** "inter_stratum_feature_template" attributes
		//of_passage_technology
		unsetOf_passage_technology(context, this);

	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	*  mapping_constraints;
	* 	part_template_definition <=
	* 	[externally_defined_item]
	* 	[{shape_aspect
	* 	shape_aspect.description = `inter stratum feature template'}]
	*  end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EInter_stratum_feature_template_armx armEntity) throws SdaiException{
		CxGeometric_template_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EInter_stratum_feature_template_armx armEntity) throws SdaiException{
		CxGeometric_template_armx.unsetMappingConstraints(context, armEntity);
	}

	//********** "managed_design_object" attributes


	//********** "shape_element" attributes

	/**
	* Sets/creates data for name attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException
	{
		CxItem_shape.setId_x(context, armEntity);
	}


	/**
	* Unsets/deletes data for name attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException
	{
		CxItem_shape.unsetId_x(context, armEntity);
	}

	/**
	* Sets/creates data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);
	}


	/**
	* Unsets/deletes data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
	}

	/**
	* Sets/creates data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);
	}


	/**
	* Unsets/deletes data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
	}
	


	//********** "physical_feature_or_part_template" attributes

	//********** "template_definition" attributes



	/**
	* Sets/creates data for physical_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPhysical_characteristic(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
	{
		CxTemplate_definition.setPhysical_characteristic(context, armEntity);
	}


	/**
	* Unsets/deletes data for physical_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPhysical_characteristic(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
	{
		CxTemplate_definition.unsetPhysical_characteristic(context, armEntity);
	}


	//********** "inter_stratum_feature_template" attributes

	/**
	* Sets/creates data for of_passage_technology attribute.
	*
	*
	*  attribute_mapping of_passage_technology_passage_technology (of_passage_technology
	* , (*PATH*), passage_technology);
	* part_template_definition &lt;=
	* product_definition_shape &lt;-
	* shape_aspect.of_shape
	* shape_aspect &lt;-
	* shape_aspect_relationship.related_shape_aspect
	* shape_aspect_relationship
	* {shape_aspect_relationship
	* shape_aspect_relationship.name = 'inter stratum feature passage technology'}
	* shape_aspect_relationship.relating_shape_aspect -&gt;
	* shape_aspect =&gt;
	* passage_technology
	* 
	*  end_attribute_mapping;

	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setOf_passage_technology(SdaiContext context, EInter_stratum_feature_template_armx armEntity) throws SdaiException
	{
      //unset old values
      unsetOf_passage_technology(context, armEntity);

		if (armEntity.testOf_passage_technology(null)){
			EPassage_technology_armx armOf_passage_technology = armEntity.getOf_passage_technology(null);
			// Safety check for derived attributes
//			if(((CEntity)epd).testAttributeFast(CProperty_definition.attributeName(null), null) >= 0){
//			}
				// PDR
	         EProperty_definition_relationship epdr = 
	         	(EProperty_definition_relationship) 
					context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
	
	         String isft = "technology usage";
					// According SEDS 527 - do nothing more
	         epdr.setName(null,isft);
	
	         //setting related
	         epdr.setRelated_property_definition(null, armEntity);
	
	         //setting relating
	         epdr.setRelating_property_definition(null, armOf_passage_technology);
	         
             epdr.setDescription(null,"");

		}

	}


	/**
	* Unsets/deletes data for of_passage_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetOf_passage_technology(SdaiContext context, EInter_stratum_feature_template_armx armEntity) throws SdaiException
	{
      AProperty_definition_relationship apdr = new AProperty_definition_relationship();
      CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
      if(apdr.getMemberCount() == 0)
      	return;
      SdaiIterator iter = apdr.createIterator();
      String isft = "technology usage";
      while(iter.next()){
      	EProperty_definition_relationship epdr = apdr.getCurrentMember(iter);
         	if((epdr.testName(null))&&(epdr.getName(null).equals(isft)))
         		epdr.deleteApplicationInstance();
      }
    }

}


