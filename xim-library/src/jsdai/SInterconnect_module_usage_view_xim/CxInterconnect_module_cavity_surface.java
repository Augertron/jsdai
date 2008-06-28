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
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxInterconnect_module_cavity_surface extends CInterconnect_module_cavity_surface implements EMappedXIMEntity{

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

		setTemp("AIM", CShape_aspect.definition);

		setMappingConstraints(context, this);
		
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);
		
		// reference_surface 
		setReference_surface(context, this); 
		
		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);

		// reference_surface 
		unsetReference_surface(null); 
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);

			// reference_surface 
			unsetReference_surface(context, this); 
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  shape_aspect
	 *  {shape_aspect.description = 'interconnect module cavity surface'}
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
			EInterconnect_module_surface_feature armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxInterconnect_module_surface_feature.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "interconnect module cavity surface");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EInterconnect_module_surface_feature armEntity) throws SdaiException {
		CxInterconnect_module_surface_feature.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}


	/**
	 * Sets/creates data for material_state_change.
	 * 
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
	*
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

	//********** "interconnect_module_cavity_surface" attributes

	/**
	* Sets/creates data for reference_surface attribute.
	*
	* <p>
	*  attribute_mapping reference_surface_interconnect_module_surface_feature (reference_surface
	* , (*PATH*), interconnect_module_surface_feature);
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `reference surface'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect
	* 	{(shape_aspect.description = `interconnect module primary surface')
	* 	(shape_aspect.description = `interconnect module secondary surface')
	* 	(shape_aspect.description = `interconnect module surface feature')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setReference_surface(SdaiContext context, EInterconnect_module_cavity_surface armEntity) throws SdaiException
	{
		//unset old values
		unsetReference_surface(context, armEntity);

		if (armEntity.testReference_surface(null))
		{

			EInterconnect_module_surface_feature armReference_surface = (EInterconnect_module_surface_feature)armEntity.getReference_surface(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "reference surface");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armReference_surface);
		}
	}


	/**
	* Unsets/deletes data for reference_surface attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetReference_surface(SdaiContext context, EInterconnect_module_cavity_surface armEntity) throws SdaiException
	{
	   AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("reference surface")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
	
	
}