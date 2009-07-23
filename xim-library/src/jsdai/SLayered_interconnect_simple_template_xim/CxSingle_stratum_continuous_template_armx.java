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
import jsdai.SLayered_interconnect_simple_template_mim.CSingle_stratum_continuous_template;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxSingle_stratum_continuous_template_armx extends CSingle_stratum_continuous_template_armx implements EMappedXIMEntity
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

		setTemp("AIM", CSingle_stratum_continuous_template.definition);

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

		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		// SETTING DERIVED
		// unsetDefinition(null);

		// Kind of AIM gap
		unsetName(null);
		
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

	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	*  mapping_constraints;
	* 	part_template_definition &lt;=
	*  product_definition 
	*  {product_definition
	*  product_definition.description = 'component termination passage template'}
	*  end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ESingle_stratum_continuous_template_armx armEntity) throws SdaiException{
		CxSingle_stratum_template_armx.setMappingConstraints(context, armEntity);
		CxContinuous_template_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, ESingle_stratum_continuous_template_armx armEntity) throws SdaiException{
		CxSingle_stratum_template_armx.unsetMappingConstraints(context, armEntity);
		CxContinuous_template_armx.unsetMappingConstraints(context, armEntity);
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

}


