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

package jsdai.SMixed_complex_types;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFootprint_definition_xim.CxBreakout_footprint_definition_armx;
import jsdai.SFootprint_definition_xim.CxFootprint_definition_armx;
import jsdai.SFootprint_definition_xim.EBreakout_footprint_definition_armx;
import jsdai.SFootprint_definition_xim.EFootprint_definition_armx;
import jsdai.SLayered_interconnect_complex_template_xim.CxMulti_stratum_structured_template_armx;
import jsdai.SLayered_interconnect_complex_template_xim.CxStratum_stack_dependent_template_armx;
import jsdai.SLayered_interconnect_complex_template_xim.EMulti_stratum_structured_template_armx;
import jsdai.SLayered_interconnect_complex_template_xim.EStratum_stack_dependent_template_armx;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxBreakout_footprint_definition_armx$stratum_stack_dependent_template_armx extends CBreakout_footprint_definition_armx$stratum_stack_dependent_template_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Physical_unit - Property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a9);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a9);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a9 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a9 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a9$;
	}
	// ENDOF Taken from Physical_unit - Property_definition

	// Product_view_definition
	// From property_definition
/*	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a11);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a11 = get_instance_select(a11);
		return (EEntity)a11;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a11 = set_instance(a11, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a11 = unset_instance(a11);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a11$;
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
		return d3$;
	}
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CBreakout_footprint_definition$stratum_stack_dependent_template.definition);

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
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);
		
		// setReference_breakout(context, this);
		
		setStack(context, this);

		setReference_footprint(context, this);
		
		setLocation(context, this);
		
		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
		unsetStack(null);
		unsetReference_footprint(null);
		unsetLocation(null);
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
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		unsetStack(context, this);
		
		unsetReference_footprint(context, this);
		
		unsetLocation(context, this);
//		 It is derived, so can't unset it unsetLocation(context, this);

		// unsetReference_breakout(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	footprint_definition &lt;= 
	* 	part_template_definition &lt;=
	* 	product_definition
	* 	{[product_definition
	* 	product_definition.formation -&gt;
	* 	product_definition_formation
	* 	product_definition_formation.of_product -&gt;
	* 	product &lt;-
	* 	product_related_product_category.products[i]
	* 	product_related_product_category &lt;=
	* 	product_category
	* 	product_category.name = 'template model'] (* Comes from supertype *)
	* 	[product_definition
	* 	product_definition.frame_of_reference -&gt;
	* 	product_definition_context &lt;=
	* 	application_context_element
	* 	application_context_element.name = 'layout design usage']} issue 5 in stepmod	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CBreakout_footprint_definition_armx$stratum_stack_dependent_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxBreakout_footprint_definition_armx.setMappingConstraints(context, armEntity);
		CxStratum_stack_dependent_template_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CBreakout_footprint_definition_armx$stratum_stack_dependent_template_armx armEntity) throws SdaiException
	{
		CxBreakout_footprint_definition_armx.unsetMappingConstraints(context, armEntity);
		CxStratum_stack_dependent_template_armx.unsetMappingConstraints(context, armEntity);
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

	/**
	 * Sets/creates data for stack.
	 * <p>
	 * </p>
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// 1) SSDT <- PDR -> LSM
 	
	public static void setStack(SdaiContext context, EStratum_stack_dependent_template_armx armEntity)
		throws SdaiException {
		CxStratum_stack_dependent_template_armx.setStack(context, armEntity);
	}

	public static void unsetStack(SdaiContext context, EStratum_stack_dependent_template_armx armEntity)
		throws SdaiException {
		CxStratum_stack_dependent_template_armx.unsetStack(context, armEntity);
	}
	
	
	public static void setReference_footprint(SdaiContext context, EBreakout_footprint_definition_armx armEntity)
		throws SdaiException {
		CxBreakout_footprint_definition_armx.setReference_footprint(context, armEntity);
	}

	public static void unsetReference_footprint(SdaiContext context, EBreakout_footprint_definition_armx armEntity)
		throws SdaiException {
		CxBreakout_footprint_definition_armx.unsetReference_footprint(context, armEntity);
	}

	public static void setLocation(SdaiContext context, EMulti_stratum_structured_template_armx armEntity)throws SdaiException {
		CxMulti_stratum_structured_template_armx.setLocation(context, armEntity);
	}

	public static void unsetLocation(SdaiContext context, EMulti_stratum_structured_template_armx armEntity)throws SdaiException {
		CxMulti_stratum_structured_template_armx.unsetLocation(context, armEntity);
	}
	
}
