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

package jsdai.SPart_feature_function_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SPart_feature_function_mim.CPart_mounting_feature;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPart_mounting_feature_armx extends CPart_mounting_feature_armx implements EMappedXIMEntity{

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

		setTemp("AIM", CPart_mounting_feature.definition);

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
	 *  part_mounting_feature &lt;=
	 *  shape_aspect
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
			EPart_mounting_feature_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_feature.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPart_mounting_feature_armx armEntity) throws SdaiException {
		CxPart_feature.unsetMappingConstraints(context, armEntity);
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
	
	//********** "part_mounting_feature" attributes

	/**
	* Sets/creates data for Connection_area attribute.
	*
	*/
	public static void setConnection_area(SdaiContext context, EPart_mounting_feature_armx armEntity) throws SdaiException
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
	public static void unsetConnection_area(SdaiContext context, EPart_mounting_feature_armx armEntity) throws SdaiException
	{
		CxShape_feature.unsetConnection_area(context, armEntity);
	}
	
	
}