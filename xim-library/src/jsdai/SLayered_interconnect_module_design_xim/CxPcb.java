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
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SInterconnect_module_usage_view_mim.CInterconnect_definition;
import jsdai.SPhysical_unit_design_view_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxPcb
		extends
		CPcb implements EMappedXIMEntity {

	// Product_view_definition

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

	// From property_definition
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
		a8 = set_instance(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
	}

	// Taken from Physical_unit - Property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a6$;
	}
	// ENDOF Taken from Physical_unit - Property_definition
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CInterconnect_definition.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			// From Item_shape
			setId_x(context, this);

			// usage_view
			setUsage_view(context, this);
			
			// used_stack_model
			setUsed_stack_model(context, this);
			
			// SETTING DERIVED
			// setDefinition(null, this);
			
			// Clean ARM specific attributes - this is DERIVED to some magic string
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			unsetId_x(null);
			unsetUsage_view(null);
			unsetUsed_stack_model(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization - this is DERIVED to some magic string
			// unsetAdditional_characterization(context, this);

			//id_x
			unsetId_x(context, this);
			
			// usage_view
			unsetUsage_view(context, this);
			
			// used_stack_model
			unsetUsed_stack_model(context, this);
			
			// unsetDefinition(null);
			// this.deleteApplicationInstance();
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; physical_unit &lt;=
	 *  product_definition
	 *  {product_definition
	 *  [product_definition.name = 'interconnect module']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'physical design']}
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EInterconnect_module armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxInterconnect_module.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EInterconnect_module armEntity) throws SdaiException {
		CxInterconnect_module.unsetMappingConstraints(context, armEntity);	
	}

	//********** "design_discipline_item_definition" attributes

	public static void setId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		//unset old values
		CxItem_shape.setId_x(context, armEntity);
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);	
	}


	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
	}
	//********** "physical_unit_design_view" attributes

	/**
	* Sets/creates data for usage_view attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setUsage_view(SdaiContext context, EPart_design_view armEntity) throws SdaiException
	{
		CxPart_design_view.setUsage_view(context, armEntity);
	}


	/**
	* Unsets/deletes data for usage_view attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetUsage_view(SdaiContext context, EPart_design_view armEntity) throws SdaiException
	{
		CxPart_design_view.unsetUsage_view(context, armEntity);
	}
	
	/**
	* Sets/creates data for sed_stack_model attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setUsed_stack_model(SdaiContext context, EInterconnect_module armEntity) throws SdaiException
	{
		CxInterconnect_module.setUsed_stack_model(context, armEntity);
	}


	/**
	* Unsets/deletes data for used_stack_model attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetUsed_stack_model(SdaiContext context, EInterconnect_module armEntity) throws SdaiException
	{
		CxInterconnect_module.unsetUsed_stack_model(context, armEntity);
	}

}