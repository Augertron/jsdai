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
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SPart_template_2d_shape_mim.CStratum_stack_dependent_template;
import jsdai.SPart_template_2d_shape_xim.CxMulti_stratum_structured_template_armx;
import jsdai.SPart_template_2d_shape_xim.EMulti_stratum_structured_template_armx;
import jsdai.SPhysical_layout_template_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxStratum_stack_dependent_template_armx extends CStratum_stack_dependent_template_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition
	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a6$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a6);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a6 = get_instance_select(a6);
		return (EEntity)a6;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a6 = set_instance(a6, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a6 = unset_instance(a6);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a6$;
	}
	
	// END OF Property_definition

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
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_stack_dependent_template.definition);

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
		
		setLocation(context, this);
		
		setStack(context, this);

		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
		unsetLocation(null);
		unsetStack(null);
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
	public static void setMappingConstraints(SdaiContext context, EStratum_stack_dependent_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxMulti_stratum_structured_template_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_stack_dependent_template_armx armEntity) throws SdaiException
	{
		CxMulti_stratum_structured_template_armx.unsetMappingConstraints(context, armEntity);	
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
 	* Sets/creates data for location attribute.
 	*
	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/

 	public static void setLocation(SdaiContext context, EMulti_stratum_structured_template_armx armEntity) throws SdaiException
 	{
 		CxMulti_stratum_structured_template_armx.setLocation(context, armEntity);
 	}


 	/**
 	* Unsets/deletes data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetLocation(SdaiContext context, EMulti_stratum_structured_template_armx armEntity) throws SdaiException
   {
 		CxMulti_stratum_structured_template_armx.unsetLocation(context, armEntity);
 	}
 	
	/**
	 * Sets/creates data for stack.
	 * <p>
	 * attribute_mapping stack(stack, $PATH, Library_stack_model_armx);
			stratum_stack_dependent_template <=
			multi_stratum_structured_template <=
			structured_template <=
			geometric_template <=
			part_template_definition <=
			product_definition_shape <=
			property_definition <-
			property_definition_relationship.related_property_definition
			{property_definition_relationship
			property_definition_relationship.name = 'associated item'}
			property_definition_relationship.relating_property_definition ->
			property_definition =>
			product_definition_shape =>
			part_template_definition =>
			stratum_stack_model =>
			library_stack_model
	 * end_attribute_mapping;
	 * </p>
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// 1) SSDT <- PDR -> LSM
 	
	public static void setStack(SdaiContext context, EStratum_stack_dependent_template_armx armEntity)
		throws SdaiException {
		//unset old values
		unsetStack(context, armEntity);
		if (armEntity.testStack(null)) {
			ELibrary_stack_model_armx stack = armEntity.getStack(null);
			
			EProperty_definition_relationship epdr = (EProperty_definition_relationship) 
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setDescription(null, "");
			epdr.setName(null, "stack");
			// PDR -> PD
			epdr.setRelating_property_definition(null, stack);
			// FD <- PDR
			epdr.setRelated_property_definition(null, armEntity);
		}
	}

	public static void unsetStack(SdaiContext context, EStratum_stack_dependent_template_armx armEntity)
		throws SdaiException {
		// 1) strategy FD <- PDR -> AGCD
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
		for (int i = 1; i <= apdr.getMemberCount();) {
			EProperty_definition_relationship epdr = apdr.getByIndex(i);
			if ((epdr.testName(null)) && (epdr.getName(null).equals("stack"))) {
				apdr.removeByIndex(i);
				epdr.deleteApplicationInstance();
			} else {
				i++;
			}
		}
	}
}
