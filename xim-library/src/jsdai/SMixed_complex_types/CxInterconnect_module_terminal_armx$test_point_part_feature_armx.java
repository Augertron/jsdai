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
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SInterconnect_module_usage_view_xim.CxInterconnect_module_terminal_armx;
import jsdai.SInterconnect_module_usage_view_xim.EInterconnect_module_terminal_armx;
import jsdai.SPart_feature_function_xim.CxTest_point_part_feature_armx;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.EShape_aspect;

public class CxInterconnect_module_terminal_armx$test_point_part_feature_armx extends CInterconnect_module_terminal_armx$test_point_part_feature_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a3);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a3$;
	}

	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a5);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a5);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a5 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a5 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a5$;
	}
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CInterconnect_module_terminal$test_point_part_feature.definition);

		setMappingConstraints(context, this);
		
		// Part_feature
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);

      // Connection_area		
		setConnection_area(context, this);
		
		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);
		
      // Connection_area		
		unsetConnection_area(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);

	      // Connection_area		
			unsetConnection_area(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  interconnect_module_terminal &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.of_shape -&gt;
	 *  product_definition_shape &lt;=
	 *  property_definition
	 *  property_definition.definition -&gt;
	 *  characterized_definition
	 *  characterized_definition = characterized_product_definition
	 *  characterized_product_definition
	 *  characterized_product_definition = product_definition
	 *  product_definition
	 *  product_definition.name = 'interconnect module'}
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
			CInterconnect_module_terminal_armx$test_point_part_feature_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxInterconnect_module_terminal_armx.setMappingConstraints(context, armEntity);
		CxTest_point_part_feature_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CInterconnect_module_terminal_armx$test_point_part_feature_armx armEntity) throws SdaiException {
		CxInterconnect_module_terminal_armx.unsetMappingConstraints(context, armEntity);
		CxTest_point_part_feature_armx.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for material_state_change.
	 * 
	 * <p>
	 *  shape_aspect &lt;-
	 *  shape_aspect_relationship.related_shape_aspect
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'precedent feature'}
	 *  shape_aspect_relationship.relating_shape_aspect -&gt;
	 *  shape_aspect
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
////////////////////////////////////////////////////////////////////
	/**
	* Sets/creates data for material_state_change attribute.
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setMaterial_state_change(context, armEntity);
	}


	/**
	* Unsets/deletes data for material_state_change attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetMaterial_state_change(context, armEntity);		
	}


	/**
	* Sets/creates data for precedent_feature attribute.
	*
	*  attribute_mapping precedent_feature_part_feature (precedent_feature
	* , (*PATH*), part_feature);
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setPrecedent_feature(context, armEntity);
	}


	/**
	* Unsets/deletes data for precedent_feature attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetPrecedent_feature(context, armEntity);		
	}


	//********** "interconnect_module_terminal" attributes

	/**
	* Sets/creates data for Connection_area attribute.
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EInterconnect_module_terminal_armx armEntity) throws SdaiException
	{
		CxInterconnect_module_terminal_armx.setConnection_area(context, armEntity);		
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EInterconnect_module_terminal_armx armEntity) throws SdaiException
	{
		CxInterconnect_module_terminal_armx.unsetConnection_area(context, armEntity);		
	}
	
	
}