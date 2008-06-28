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

package jsdai.SInterconnect_module_usage_view_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SInterconnect_module_usage_view_mim.CInterconnect_definition;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxPcb_usage_view
		extends
		CPcb_usage_view implements EMappedXIMEntity {

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

			// SETTING DERIVED
			// setDefinition(null, this);

			// Interconnect_module_usage_view
	      setThickness_over_metal_requirement(context, this);
	      setThickness_over_dielectric_requirement(context, this);
	      setMeasurement_condition(context, this);
	      setLocated_thickness_requirement(context, this);
	      setImplemented_function(context, this);
			
			
			// Clean ARM specific attributes - this is DERIVED to some magic string
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			unsetId_x(null);

	      unsetThickness_over_metal_requirement(null);
	      unsetThickness_over_dielectric_requirement(null);
	      unsetMeasurement_condition(null);
	      unsetLocated_thickness_requirement(null);
	      unsetImplemented_function(null);
			
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
			
			// unsetDefinition(null);

			// Interconnect_module_usage_view
	      unsetThickness_over_metal_requirement(context, this);
	      unsetThickness_over_dielectric_requirement(context, this);
	      unsetMeasurement_condition(context, this);
	      unsetLocated_thickness_requirement(context, this);
	      unsetImplemented_function(context, this);

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
	 *  product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'physical design usage'}
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
			EInterconnect_module_usage_view armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxInterconnect_module_usage_view.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EInterconnect_module_usage_view armEntity) throws SdaiException {
		CxInterconnect_module_usage_view.unsetMappingConstraints(context, armEntity);
	}

	//********** "Product_view_definition" attributes
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
	//********** "interconnect_module_usage_view" attributes

	/**
	* Sets/creates data for thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setThickness_over_metal_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.setThickness_over_metal_requirement(context, armEntity);		
	}


	/**
	* Unsets/deletes data for thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetThickness_over_metal_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.unsetThickness_over_metal_requirement(context, armEntity);
	}

	/**
	* Sets/creates data for thickness_over_dielectric_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setThickness_over_dielectric_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.setThickness_over_dielectric_requirement(context, armEntity);		
	}


	/**
	* Unsets/deletes data for thickness_over_dielectric_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetThickness_over_dielectric_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.unsetThickness_over_dielectric_requirement(context, armEntity);
	}


	/**
	* Sets/creates data for measurement_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMeasurement_condition(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.setMeasurement_condition(context, armEntity);		
	}


	/**
	* Unsets/deletes data for measurement_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMeasurement_condition(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.unsetMeasurement_condition(context, armEntity);		
	}


	/**
	* Sets/creates data for located_thickness_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocated_thickness_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.setLocated_thickness_requirement(context, armEntity);		
	}


	/**
	* Unsets/deletes data for located_thickness_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocated_thickness_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.unsetLocated_thickness_requirement(context, armEntity);		
	}


	/**
	* Sets/creates data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setImplemented_function(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.setImplemented_function(context, armEntity);		
	}


	/**
	* Unsets/deletes data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplemented_function(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxInterconnect_module_usage_view.unsetImplemented_function(context, armEntity);
	}


}