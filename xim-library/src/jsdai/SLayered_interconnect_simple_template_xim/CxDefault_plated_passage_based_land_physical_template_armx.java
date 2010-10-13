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
import jsdai.SLayered_interconnect_simple_template_mim.CDefault_plated_passage_based_land_physical_template;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;

public class CxDefault_plated_passage_based_land_physical_template_armx extends CDefault_plated_passage_based_land_physical_template_armx implements EMappedXIMEntity
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
		return test_string(a4);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a4$;
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

		setTemp("AIM", CDefault_plated_passage_based_land_physical_template.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		
		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
//		setId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

      // setOf_stratum_technology(context, this);
      
      // minimum_fabrication_allowance
		// Removed per 2507, comment #2		
		// setMinimum_fabrication_allowance(context, this);
      
      // of_passage_technology
		setOf_passage_technology(context, this);
      
      // of_stratum_extent
		// setOf_stratum_extent(context, this);
      
      // minimum_annular_ring
		// Removed per 2507, comment #2		
		// setMinimum_annular_ring(context, this);
      
      
		// Clean ARM specific attributes
//		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
      // unsetOf_stratum_technology(null);
		// Removed per 2507, comment #2
		// unsetMinimum_fabrication_allowance(null);
		unsetOf_passage_technology(null);
		// unsetOf_stratum_extent(null);
		// Removed per 2507, comment #2
		// unsetMinimum_annular_ring(null);
      
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
//		unsetId_x(context, this);

		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

      // unsetOf_stratum_technology(context, this);

      // minimum_fabrication_allowance
		// Removed per 2507, comment #2		
		// unsetMinimum_fabrication_allowance(context, this);
      
      // of_passage_technology
		unsetOf_passage_technology(context, this);
      
      // of_stratum_extent
		// unsetOf_stratum_extent(context, this);
      
      // minimum_annular_ring
		// Removed per 2507, comment #2		
		// unsetMinimum_annular_ring(context, this);
      
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  land_physical_template &lt;=
	*  part_template_definition &lt;=
	*  {product_definition
	*  product_definition.description = 'default plated passage based'}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EDefault_plated_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// no more constraints as it is ABS and only subtypes have more magic strings
		CxDefault_passage_based_land_physical_template_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription((EProduct_definition)null, "default plated passage based");
	}

	public static void unsetMappingConstraints(SdaiContext context, EDefault_plated_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((EProduct_definition)null);
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
/* Removed from XIM - see bug #3610
    public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
       CxItem_shape.setId_x(context, armEntity);
    }
*/
  /**
   * Unsets/deletes data for Id_x attribute.
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
 	//********** "product_view_definition" attributes
    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_characterization(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
   }

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
	//********** "default_passage_based_land_physical_template" attributes

	/**
	* Sets/creates data for minimum_fabrication_allowance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 	/* Removed per 2507, comment #2
	public static void setMinimum_fabrication_allowance(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.setMinimum_fabrication_allowance(context, armEntity);
	}
*/

	/**
	* Unsets/deletes data for minimum_fabrication_allowance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 	/* Removed per 2507, comment #2 	
	public static void unsetMinimum_fabrication_allowance(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.unsetMinimum_fabrication_allowance(context, armEntity);		
	}
*/

	/**
	* Sets/creates data for of_passage_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setOf_passage_technology(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.setOf_passage_technology(context, armEntity);		
	}


	/**
	* Unsets/deletes data for of_passage_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetOf_passage_technology(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.unsetOf_passage_technology(context, armEntity);		
	}


	/**
	* Sets/creates data for of_stratum_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed during modularization		
	public static void setOf_stratum_extent(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.setOf_stratum_extent(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for of_stratum_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed during modularization		
	public static void unsetOf_stratum_extent(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.unsetOf_stratum_extent(context, armEntity);		
	}
*/

	/**
	* Sets/creates data for minimum_annular_ring attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed per 2507, comment #2	
	public static void setMinimum_annular_ring(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.setMinimum_annular_ring(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for minimum_annular_ring attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed per 2507, comment #2	
	public static void unsetMinimum_annular_ring(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxDefault_passage_based_land_physical_template_armx.unsetMinimum_annular_ring(context, armEntity);		
	}*/
}
