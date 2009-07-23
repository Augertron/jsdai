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
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SMixed_complex_types.*;
import jsdai.SLayered_interconnect_simple_template_mim.CTeardrop_by_length_template;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxTeardrop_by_length_template_armx extends CTeardrop_by_length_template_armx implements EMappedXIMEntity
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
		a2 = set_instanceX(a2, value);
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

		setTemp("AIM", CTeardrop_by_length_template.definition);

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
		
      // Teardrop_taper_start_distance
      setTeardrop_taper_start_distance(context, this);
		
      // setTeardrop_taper_end_distance
		setTeardrop_taper_end_distance(context, this);
      
		// Clean ARM specific attributes
		unsetId_x(null);
		// - this is DERIVED to some magic string
		// unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
      // unsetOf_stratum_technology(null);
      
      unsetTeardrop_taper_start_distance(null);
		unsetTeardrop_taper_end_distance(null);
		
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
      
      // Teardrop_taper_start_distance
      unsetTeardrop_taper_start_distance(context, this);
		
      // setTeardrop_taper_end_distance
		unsetTeardrop_taper_end_distance(context, this);
      
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  part_template_definition &lt;=
	*  {product_definition
	*  [product_definition.name = 'teardrop by length template']
	*  [product_definition.description = 'material addition feature template']}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ETeardrop_by_length_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxTeardrop_template_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, ETeardrop_by_length_template_armx armEntity) throws SdaiException
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
/*	public static void setOf_stratum_technology(SdaiContext context, EStratum_feature_template_armx armEntity) throws SdaiException
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
/*	public static void unsetOf_stratum_technology(SdaiContext context, EStratum_feature_template_armx armEntity) throws SdaiException
    {
		CxStratum_feature_template_armx.unsetOf_stratum_technology(context, armEntity);		
    }
*/
	//********** "teardrop_by_length_template" attributes

	/**
	* Sets/creates data for teardrop_taper_start_distance attribute.
	*
	* <p>
	*  attribute_mapping teardrop_taper_start_distance_length_data_element (teardrop_taper_start_distance
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
	* 	{representation_item.name = `teardrop taper start distance'} 
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
	public static void setTeardrop_taper_start_distance(SdaiContext context, ETeardrop_by_length_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetTeardrop_taper_start_distance(context, armEntity);
		
		if (armEntity.testTeardrop_taper_start_distance(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armTeardrop_taper_start_distance =  
					armEntity.getTeardrop_taper_start_distance(null);

			CLength_measure_with_unit$measure_representation_item aimLength;
			if (! (armTeardrop_taper_start_distance instanceof EMeasure_representation_item)) {
				aimLength = (CLength_measure_with_unit$measure_representation_item) 
					context.working_model.substituteInstance(armTeardrop_taper_start_distance,
					CLength_measure_with_unit$measure_representation_item.definition);
				aimLength.setName(null, "");
			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
			}
			else{
				aimLength = (CLength_measure_with_unit$measure_representation_item)
					armTeardrop_taper_start_distance;
			}

			ERepresentation representation = 
				CxAP210ARMUtilities.findRepresentationShort(context,
						armEntity, "teardrop parametric data", null);
			aimLength.setName(null,
			"teardrop taper start distance");
			representation.getItems(null).addUnordered(aimLength);
		}
	}


	/**
	* Unsets/deletes data for teardrop_taper_start_distance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetTeardrop_taper_start_distance(SdaiContext context, ETeardrop_by_length_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.clearRepresentationItemsShort(context, armEntity, "teardrop parametric data",
		"teardrop taper start distance");
	}


	/**
	* Sets/creates data for teardrop_taper_end_distance attribute.
	*
	* <p>
	*  attribute_mapping teardrop_taper_end_distance_length_data_element (teardrop_taper_end_distance
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
	* 	{representation_item.name = `teardrop taper end distance'} 
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
	public static void setTeardrop_taper_end_distance(SdaiContext context, ETeardrop_by_length_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetTeardrop_taper_end_distance(context, armEntity);
		
		if (armEntity.testTeardrop_taper_end_distance(null))
		{ 
			jsdai.SMeasure_schema.ELength_measure_with_unit 
				armTeardrop_taper_end_distance =  
					armEntity.getTeardrop_taper_end_distance(null);

			CLength_measure_with_unit$measure_representation_item aimLength;
			if (! (armTeardrop_taper_end_distance instanceof EMeasure_representation_item)) {
				aimLength = (CLength_measure_with_unit$measure_representation_item) 
					context.working_model.substituteInstance(armTeardrop_taper_end_distance,
					CLength_measure_with_unit$measure_representation_item.definition);
				aimLength.setName(null, "");
			//EA armLeast_lead_length_below_seating_plane.setAimInstance(aimLength);
			}
			else{
				aimLength = (CLength_measure_with_unit$measure_representation_item)
					armTeardrop_taper_end_distance;
			}

			ERepresentation representation = 
				CxAP210ARMUtilities.findRepresentationShort(context,
						armEntity, "teardrop parametric data", null);
			aimLength.setName(null,
					"teardrop taper end distance");
			representation.getItems(null).addUnordered(aimLength);
		}
	}


	/**
	* Unsets/deletes data for teardrop_taper_end_distance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetTeardrop_taper_end_distance(SdaiContext context, ETeardrop_by_length_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.clearRepresentationItemsShort(context, armEntity, "teardrop parametric data",
			"teardrop taper end distance");
	}
	
	
}
