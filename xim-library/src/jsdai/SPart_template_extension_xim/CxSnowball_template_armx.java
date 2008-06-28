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

package jsdai.SPart_template_extension_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SMeasure_schema.*;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SPart_template_extension_mim.CSnowball_template;
import jsdai.SPhysical_layout_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxSnowball_template_armx extends CSnowball_template_armx implements EMappedXIMEntity
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

		setTemp("AIM", CSnowball_template.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		
		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		setId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string
		// setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

      // setOf_stratum_technology(context, this);
		
      // snowball_start_distance
      setSnowball_start_distance(context, this);
      
      // snowball_end_distance
      setSnowball_end_distance(context, this);
      
      // snowball_initial_spacing
		setSnowball_initial_spacing(context, this);
      
      // snowball_end_spacing
		setSnowball_end_spacing(context, this);
      
      // snowball_initial_radius
      setSnowball_initial_radius(context, this);
		
      // snowball_end_radius
		setSnowball_end_radius(context, this);
      
      // snowball_quantity_per_trace
		setSnowball_quantity_per_trace(context, this);
      
      
		// Clean ARM specific attributes
		unsetId_x(null);
		// - this is DERIVED to some magic string
		// unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
      // unsetOf_stratum_technology(null);
      
      unsetSnowball_start_distance(null);
      unsetSnowball_end_distance(null);
		unsetSnowball_initial_spacing(null);
		unsetSnowball_end_spacing(null);
      unsetSnowball_initial_radius(null);
		unsetSnowball_end_radius(null);
		unsetSnowball_quantity_per_trace(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		unsetId_x(context, this);

		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string
		// unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

      // unsetOf_stratum_technology(context, this);
      
      // snowball_start_distance
      unsetSnowball_start_distance(context, this);
      
      // snowball_end_distance
      unsetSnowball_end_distance(context, this);
      
      // snowball_initial_spacing
		unsetSnowball_initial_spacing(context, this);
      
      // snowball_end_spacing
		unsetSnowball_end_spacing(context, this);
      
      // snowball_initial_radius
      unsetSnowball_initial_radius(context, this);
		
      // snowball_end_radius
		unsetSnowball_end_radius(context, this);
      
      // snowball_quantity_per_trace
		unsetSnowball_quantity_per_trace(context, this);
      
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  part_template_definition &lt;=
	*  {product_definition
	*  [product_definition.name = 'snowball template']
	*  [product_definition.description = 'material addition feature template']}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxTeardrop_template_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxTeardrop_template_armx.unsetMappingConstraints(context, armEntity);
	}	
	//********** "managed_design_object" attributes

	//********** "item_shape" attributes
    /**
     * Sets/creates data for Id_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
       CxItem_shape.setId_x(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
      CxItem_shape.unsetId_x(context, armEntity);
   }

 	//********** "product_view_definition" attributes
    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_contexts(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
   }

    // Template_definition, originally developed in Part_template_definition
 	/**
 	* Sets/creates data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
    // PTD <- PD <- PDR -> R -> RI <- AGA -> CT
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
    
    
	/**
	* Sets/creates data for of_stratum_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 	// PTD <- SA <- SAR
/* 	
	public static void setOf_stratum_technology(SdaiContext context, EStratum_feature_template_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_armx.setOf_stratum_technology(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for of_stratum_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* 	
	public static void unsetOf_stratum_technology(SdaiContext context, EStratum_feature_template_armx armEntity) throws SdaiException
    {
		CxStratum_feature_template_armx.unsetOf_stratum_technology(context, armEntity);		
    }
*/
	//********** "snowball_template" attributes

    private static void setRepresentation_item(SdaiContext context, 
    		ESnowball_template_armx armEntity,
            jsdai.SRepresentation_schema.ERepresentation_item item,
            String name)throws SdaiException{

    	String repName = "teardrop parametric data";
    	ARepresentation representations = new ARepresentation();
    	CRepresentation.usedinItems(null, item, context.domain, representations);
    	jsdai.SRepresentation_schema.ERepresentation suitableRepresentation = null;
    	if(representations.getMemberCount() > 0){
    		for(int i=1,count=representations.getMemberCount(); i<=count; i++){
    			ERepresentation rep = representations.getByIndex(i);
    			if((rep.testName(null))&&(rep.getName(null).equals(repName))){
    				suitableRepresentation = rep;
    				break;
    			}
    		}
    	}
    	if(suitableRepresentation == null){
    		suitableRepresentation = (jsdai.SRepresentation_schema.ERepresentation)
            	context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.definition);
    		suitableRepresentation.setName(null, repName);
    		// Context
    		jsdai.SRepresentation_schema.ERepresentation_context representation_context =
    			CxAP210ARMUtilities.createRepresentation_context(context,
                                                                       "", "", true);
    		suitableRepresentation.setContext_of_items(null, representation_context);
    		suitableRepresentation.createItems(null).addUnordered(item);
    	}
    	// PD
        EProperty_definition propDef = (EProperty_definition)
    		context.working_model.createEntityInstance(CProperty_definition.definition);
        propDef.setDefinition(null, armEntity);
        propDef.setName(null, name);
    	// PDR
        EProperty_definition_representation propDefRep = (EProperty_definition_representation)
        	context.working_model.createEntityInstance(CProperty_definition_representation.class);
        propDefRep.setDefinition(null, propDef);
        propDefRep.setUsed_representation(null, suitableRepresentation);
    }
 	
 	
	/**
	* Sets/creates data for snowball_start_distance attribute.
	*
	* <p>
	*  attribute_mapping snowball_start_distance_length_data_element (snowball_start_distance
	* , (*PATH*), length_data_element);
	* 	part_template_definition <=
	*  product_definition_shape <= 
	* 	property_definition <- 
	* 	property_definition_representation.definition 
	* 	property_definition_representation 
	* 	property_definition_representation.used_representation -> 
	* 	representation 
	* 	{representation.name = `teardrop parametric data'} 
	* 	representation.items[i] -> 
	* 	representation_item 
	* 	{representation_item.name = `snowball start distance'} 
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
	// PTD <- PD <- PDR -> R -> LMWU
	public static void setSnowball_start_distance(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSnowball_start_distance(context, armEntity);
		
		if (armEntity.testSnowball_start_distance(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armSnowball_parameter =  
				armEntity.getSnowball_start_distance(null);

			ERepresentation_item length;
   		if (! (armSnowball_parameter instanceof jsdai.SQualified_measure_schema.EMeasure_representation_item)) {
   			length = (ERepresentation_item)context.working_model.substituteInstance(armSnowball_parameter,
   					CLength_measure_with_unit$measure_representation_item.definition);
			length.setName(null, "");
   			
   			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
   		}
   		else
   			length = (ERepresentation_item)armSnowball_parameter;

   		setRepresentation_item(context, armEntity, length, "snowball start distance");
		}
	}


	/**
	* Unsets/deletes data for snowball_start_distance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetSnowball_start_distance(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "snowball start distance");
	}


	/**
	* Sets/creates data for snowball_end_distance attribute.
	*
	* <p>
	*  attribute_mapping snowball_end_distance_length_data_element (snowball_end_distance
	* , (*PATH*), length_data_element);
	* 	part_template_definition <= 
	* 	shape_aspect 
	* 	shape_definition = shape_aspect 
	* 	characterized_definition = shape_definition 
	* 	characterized_definition <- 
	* 	property_definition.definition 
	* 	property_definition <- 
	* 	property_definition_representation.definition 
	* 	property_definition_representation 
	* 	property_definition_representation.used_representation -> 
	* 	representation 
	* 	{representation.name = `teardrop parametric data'} 
	* 	representation.items[i] -> 
	* 	representation_item 
	* 	{representation_item.name = `snowball end distance'} 
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
	public static void setSnowball_end_distance(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSnowball_end_distance(context, armEntity);
		
		if (armEntity.testSnowball_end_distance(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armSnowball_parameter =  
				armEntity.getSnowball_end_distance(null);

			ERepresentation_item length;
   		if (! (armSnowball_parameter instanceof jsdai.SQualified_measure_schema.EMeasure_representation_item)) {
   			length = (ERepresentation_item)context.working_model.substituteInstance(armSnowball_parameter,
   					CLength_measure_with_unit$measure_representation_item.definition);
   			length.setName(null, "");   			
   			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
   		}
   		else
   			length = (ERepresentation_item)armSnowball_parameter;

   		setRepresentation_item(context, armEntity, length, "snowball end distance");
		}
	}


	/**
	* Unsets/deletes data for snowball_end_distance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetSnowball_end_distance(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "snowball end distance");
	}


	/**
	* Sets/creates data for snowball_initial_spacing attribute.
	*
	* <p>
	*  attribute_mapping snowball_initial_spacing_length_data_element (snowball_initial_spacing
	* , (*PATH*), length_data_element);
	* 	part_template_definition <= 
	* 	shape_aspect 
	* 	shape_definition = shape_aspect 
	* 	characterized_definition = shape_definition 
	* 	characterized_definition <- 
	* 	property_definition.definition 
	* 	property_definition <- 
	* 	property_definition_representation.definition 
	* 	property_definition_representation 
	* 	property_definition_representation.used_representation -> 
	* 	representation 
	* 	{representation.name = `teardrop parametric data'} 
	* 	representation.items[i] -> 
	* 	representation_item 
	* 	{representation_item.name = `snowball initial spacing'} 
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
	public static void setSnowball_initial_spacing(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSnowball_initial_spacing(context, armEntity);
		
		if (armEntity.testSnowball_initial_spacing(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armSnowball_parameter =  
				armEntity.getSnowball_initial_spacing(null);

			ERepresentation_item length;
   		if (! (armSnowball_parameter instanceof jsdai.SQualified_measure_schema.EMeasure_representation_item)) {
   			length = (ERepresentation_item)context.working_model.substituteInstance(armSnowball_parameter,
   					CLength_measure_with_unit$measure_representation_item.definition);
   			length.setName(null, "");
   			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
   		}
   		else
   			length = (ERepresentation_item)armSnowball_parameter;

   		setRepresentation_item(context, armEntity, length, "snowball initial spacing");
		}
	}


	/**
	* Unsets/deletes data for snowball_initial_spacing attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetSnowball_initial_spacing(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "snowball initial spacing");
	}


	/**
	* Sets/creates data for snowball_end_spacing attribute.
	*
	* <p>
	*  attribute_mapping snowball_end_spacing_length_data_element (snowball_end_spacing
	* , (*PATH*), length_data_element);
	* 	part_template_definition <= 
	* 	shape_aspect 
	* 	shape_definition = shape_aspect 
	* 	characterized_definition = shape_definition 
	* 	characterized_definition <- 
	* 	property_definition.definition 
	* 	property_definition <- 
	* 	property_definition_representation.definition 
	* 	property_definition_representation 
	* 	property_definition_representation.used_representation -> 
	* 	representation 
	* 	{representation.name = `teardrop parametric data'} 
	* 	representation.items[i] -> 
	* 	representation_item 
	* 	{representation_item.name = `snowball end spacing'} 
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
	public static void setSnowball_end_spacing(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSnowball_end_spacing(context, armEntity);
		
		if (armEntity.testSnowball_end_spacing(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armSnowball_parameter =  
				armEntity.getSnowball_end_spacing(null);

			ERepresentation_item length;
   		if (! (armSnowball_parameter instanceof jsdai.SQualified_measure_schema.EMeasure_representation_item)) {
   			length = (ERepresentation_item)context.working_model.substituteInstance(armSnowball_parameter,
   					CLength_measure_with_unit$measure_representation_item.definition);
   			length.setName(null, "");   			
   			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
   		}
   		else
   			length = (ERepresentation_item)armSnowball_parameter;

   		setRepresentation_item(context, armEntity, length, "snowball end spacing");
		}
	}


	/**
	* Unsets/deletes data for snowball_end_spacing attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetSnowball_end_spacing(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "snowball end spacing");
	}


	/**
	* Sets/creates data for snowball_initial_radius attribute.
	*
	* <p>
	*  attribute_mapping snowball_initial_radius_length_data_element (snowball_initial_radius
	* , (*PATH*), length_data_element);
	* 	part_template_definition <= 
	* 	shape_aspect 
	* 	shape_definition = shape_aspect 
	* 	characterized_definition = shape_definition 
	* 	characterized_definition <- 
	* 	property_definition.definition 
	* 	property_definition <- 
	* 	property_definition_representation.definition 
	* 	property_definition_representation 
	* 	property_definition_representation.used_representation -> 
	* 	representation 
	* 	{representation.name = `teardrop parametric data'} 
	* 	representation.items[i] -> 
	* 	representation_item 
	* 	{representation_item.name = `snowball initial radius'} 
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
	public static void setSnowball_initial_radius(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSnowball_initial_radius(context, armEntity);
		
		if (armEntity.testSnowball_initial_radius(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armSnowball_parameter =  
				armEntity.getSnowball_initial_radius(null);

			ERepresentation_item length;
   		if (! (armSnowball_parameter instanceof jsdai.SQualified_measure_schema.EMeasure_representation_item)) {
   			length = (ERepresentation_item)context.working_model.substituteInstance(armSnowball_parameter,
   					CLength_measure_with_unit$measure_representation_item.definition);
   			length.setName(null, "");   			
   			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
   		}
   		else
   			length = (ERepresentation_item)armSnowball_parameter;

   		setRepresentation_item(context, armEntity, length, "snowball initial radius");
		}
	}


	/**
	* Unsets/deletes data for snowball_initial_radius attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetSnowball_initial_radius(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "snowball initial radius");
	}


	/**
	* Sets/creates data for snowball_end_radius attribute.
	*
	* <p>
	*  attribute_mapping snowball_end_radius_length_data_element (snowball_end_radius
	* , (*PATH*), length_data_element);
	* 	part_template_definition <= 
	* 	shape_aspect 
	* 	shape_definition = shape_aspect 
	* 	characterized_definition = shape_definition 
	* 	characterized_definition <- 
	* 	property_definition.definition 
	* 	property_definition <- 
	* 	property_definition_representation.definition 
	* 	property_definition_representation 
	* 	property_definition_representation.used_representation -> 
	* 	representation 
	* 	{representation.name = `teardrop parametric data'} 
	* 	representation.items[i] -> 
	* 	representation_item 
	* 	{representation_item.name = `snowball end radius'} 
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
	public static void setSnowball_end_radius(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSnowball_end_radius(context, armEntity);
		
		if (armEntity.testSnowball_end_radius(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armSnowball_parameter =  
				armEntity.getSnowball_end_radius(null);

			ERepresentation_item length;
   		if (! (armSnowball_parameter instanceof jsdai.SQualified_measure_schema.EMeasure_representation_item)) {
   			length = (ERepresentation_item)context.working_model.substituteInstance(armSnowball_parameter,
   					CLength_measure_with_unit$measure_representation_item.definition);
   			length.setName(null, "");   			
   			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
   		}
   		else
   			length = (ERepresentation_item)armSnowball_parameter;

   		setRepresentation_item(context, armEntity, length, "snowball end radius");
		}
	}


	/**
	* Unsets/deletes data for snowball_end_radius attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetSnowball_end_radius(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "snowball end radius");
	}


	/**
	* Sets/creates data for snowball_quantity_per_trace attribute.
	*
	* <p>
	*  attribute_mapping snowball_quantity_per_trace (snowball_quantity_per_trace
	* );
	* 	part_template_definition <= 
	* 	shape_aspect 
	* 	shape_definition = shape_aspect 
	* 	characterized_definition = shape_definition 
	* 	characterized_definition <- 
	* 	property_definition.definition 
	* 	property_definition <- 
	* 	property_definition_representation.definition 
	* 	property_definition_representation 
	* 	property_definition_representation.used_representation -> 
	* 	representation 
	* 	{representation.name = `teardrop parametric data'} 
	* 	representation.items[i] -> 
	* 	representation_item 
	* 	{representation_item.name = `snowball quantity per trace'} 
	* 	representation_item => 
	* 	measure_representation_item <= 
	* 	measure_with_unit 
	* 	{measure_with_unit.unit_component -> 
	* 	unit 
	* 	unit = named_unit 
	* 	named_unit => 
	* 	context_dependent_unit} 
	* 	measure_with_unit.value_component -> 
	* 	measure_value 
	* 	measure_value = count_measure 
	* 	count_measure 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	// PTD <- PD <- PDR -> R -> MRI {MRI -> CDU} -> CM
	public static void setSnowball_quantity_per_trace(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSnowball_quantity_per_trace(context, armEntity);
		
		if (armEntity.testSnowball_quantity_per_trace(null))
		{ 
			int armSnowball_quantity_per_trace = armEntity.getSnowball_quantity_per_trace(null);
			jsdai.SQualified_measure_schema.EMeasure_representation_item 
				emri = (jsdai.SQualified_measure_schema.EMeasure_representation_item)
				context.working_model.createEntityInstance(jsdai.SQualified_measure_schema.CMeasure_representation_item.definition);
         // Dimensionl exponents
         LangUtils.Attribute_and_value_structure[] exponentsStructure =
            {new LangUtils.Attribute_and_value_structure(CDimensional_exponents.attributeAmount_of_substance_exponent(null), new Double(0)),
            new LangUtils.Attribute_and_value_structure(CDimensional_exponents.attributeElectric_current_exponent(null), new Double(0)),
            new LangUtils.Attribute_and_value_structure(CDimensional_exponents.attributeLength_exponent(null), new Double(0)),
            new LangUtils.Attribute_and_value_structure(CDimensional_exponents.attributeLuminous_intensity_exponent(null), new Double(0)),
            new LangUtils.Attribute_and_value_structure(CDimensional_exponents.attributeMass_exponent(null), new Double(0)),
            new LangUtils.Attribute_and_value_structure(CDimensional_exponents.attributeThermodynamic_temperature_exponent(null), new Double(0)),
            new LangUtils.Attribute_and_value_structure(CDimensional_exponents.attributeTime_exponent(null), new Double(0))
         };
         EDimensional_exponents exponents  = (EDimensional_exponents)
            LangUtils.createInstanceIfNeeded(context, CDimensional_exponents.definition, exponentsStructure);
         // Unit
         LangUtils.Attribute_and_value_structure[] unitStructure =
            {new LangUtils.Attribute_and_value_structure(
           CContext_dependent_unit.attributeDimensions(null),
           exponents),
            new LangUtils.Attribute_and_value_structure(
            CContext_dependent_unit.attributeName(null), "count") // any meaningful string
         };
         EContext_dependent_unit unit  = (EContext_dependent_unit)
            LangUtils.createInstanceIfNeeded(context, CContext_dependent_unit.definition, unitStructure);
         // MRI -> Unit
         emri.setUnit_component(null, unit);
         emri.setValue_component(null, armSnowball_quantity_per_trace, (ECount_measure)null);
         if(!emri.testName(null)){
        	 emri.setName(null, "");
         }

         setRepresentation_item(context, armEntity, emri, "snowball quantity per trace");
		}
	}


	/**
	* Unsets/deletes data for snowball_quantity_per_trace attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetSnowball_quantity_per_trace(SdaiContext context, ESnowball_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "snowball quantity per trace");
	}

		
	
}
