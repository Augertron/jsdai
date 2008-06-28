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
import jsdai.SComponent_feature_xim.*;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SLayered_interconnect_module_design_mim.CLaminate_component_feature;
import jsdai.SProduct_property_definition_schema.*;

public class CxConductive_interconnect_element_terminal extends CConductive_interconnect_element_terminal implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a1$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	// ENDOF From CShape_aspect.java

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLaminate_component_feature.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
      // location
		setLocation(context, this);
		
		// Clean ARM
		unsetDefinition(null);

		// Connection_area
		unsetConnection_area(null);
		
      // location
		unsetLocation(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetDefinition(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
	      // location
			unsetLocation(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  laminate_component_feature &lt;=
	 *  component_feature &lt;=				
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'conductive interconnect element terminal'}
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
			EConductive_interconnect_element_terminal armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxShape_feature.setMappingConstraints(context, armEntity);
		CxLaminate_component_feature_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "conductive interconnect element terminal");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EConductive_interconnect_element_terminal armEntity) throws SdaiException {
		CxLaminate_component_feature_armx.unsetMappingConstraints(context, armEntity);
		CxShape_feature.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}

	//********** "component_feature" attributes

	/**
	* Sets/creates data for definition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> SA
	public static void setDefinition(SdaiContext context, EComponent_feature_armx armEntity) throws SdaiException
	{
		CxComponent_feature_armx.setDefinition(context, armEntity);		
	}


	/**
	* Unsets/deletes data for definition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDefinition(SdaiContext context, EComponent_feature_armx armEntity) throws SdaiException
	{
		CxComponent_feature_armx.unsetDefinition(context, armEntity);		
	}


	//********** "conductive_interconnect_element_terminal" attributes

	/**
	* Sets/creates data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EConductive_interconnect_element_terminal armEntity) throws SdaiException
	{
		CxShape_feature.setConnection_area(context, armEntity);
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EConductive_interconnect_element_terminal armEntity) throws SdaiException
	{
		CxShape_feature.unsetConnection_area(context, armEntity);
	}

	/**
	* Sets/creates data for location attribute.
	*
	* <p>
	 attribute_mapping location_layer_connection_point (location
	, (*PATH*), layer_connection_point);
		component_terminal <= 
		shape_aspect <- 
		shape_aspect_relationship.related_shape_aspect 
		shape_aspect_relationship 
		{shape_aspect_relationship 
		shape_aspect_relationship.name = `terminal location'} 
		shape_aspect_relationship.relating_shape_aspect -> 
		shape_aspect => 
		layer_connection_point 
	 end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocation(SdaiContext context, EConductive_interconnect_element_terminal armEntity) throws SdaiException
	{
		//unset old values
		unsetLocation(context, armEntity);

		if (armEntity.testLocation(null))
		{
			ELayer_connection_point_armx armLocation = armEntity.getLocation(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "terminal location");
			shape_aspect_relationship.setRelating_shape_aspect(null, armLocation);
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
		}
	}

	/**
	* Unsets/deletes data for location attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation(SdaiContext context, EConductive_interconnect_element_terminal armEntity) throws SdaiException
	{
		EShape_aspect_relationship shape_aspect_relationship = null;
		AShape_aspect_relationship aShape_aspect_relationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aShape_aspect_relationship);
		for (int i = 1; i <= aShape_aspect_relationship.getMemberCount(); i++) {
			shape_aspect_relationship = aShape_aspect_relationship.getByIndex(i);
			if (shape_aspect_relationship.testName(null) && shape_aspect_relationship.getName(null).equals("terminal location")) {
				shape_aspect_relationship.deleteApplicationInstance();
			}
		}
	}


	
	
}