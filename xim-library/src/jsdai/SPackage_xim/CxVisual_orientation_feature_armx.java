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

package jsdai.SPackage_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SPackage_mim.CVisual_orientation_feature;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.AShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;

public class CxVisual_orientation_feature_armx extends CVisual_orientation_feature_armx implements EMappedXIMEntity{

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

		setTemp("AIM", CVisual_orientation_feature.definition);

		setMappingConstraints(context, this);
		
		// Part_feature
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);

		setAssociated_body_vertical_extent(context, this);
		
		setAssociated_terminal(context, this);
		
		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);
	
		unsetAssociated_body_vertical_extent(null);
		
		unsetAssociated_terminal(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);
			
			unsetAssociated_body_vertical_extent(context, this);
			
			unsetAssociated_terminal(context, this);			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
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
			EVisual_orientation_feature_armx armEntity) throws SdaiException {
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
			EVisual_orientation_feature_armx armEntity) throws SdaiException {
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

	// Visual_orientation_feature attributes
	/**
	* Sets/creates data for Associated_body_vertical_extent attribute.
	*
	attribute_mapping associated_body_vertical_extent(associated_body_vertical_extent, $PATH, Package_body_surface_armx);
		visual_orientation_feature <=
		shape_aspect <-
		shape_aspect_relationship.related_shape_aspect
		{shape_aspect_relationship
		shape_aspect_relationship.name = 'associated body vertical extent'}
		shape_aspect_relationship
		shape_aspect_relationship.relating_shape_aspect ->
		(shape_aspect =>
		package_body_surface =>
		package_body_top_surface)
		(shape_aspect =>
		package_body_surface =>
		package_body_bottom_surface)
	end_attribute_mapping;	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// VOF <- SAR -> PBS
	public static void setAssociated_body_vertical_extent(SdaiContext context, EVisual_orientation_feature_armx armEntity) throws SdaiException
	{
		unsetAssociated_body_vertical_extent(context, armEntity);
		if(armEntity.testAssociated_body_vertical_extent(null)){
			APackage_body_surface_armx surfaces = armEntity.getAssociated_body_vertical_extent(null);
			SdaiIterator iter = surfaces.createIterator();
			while(iter.next()){
				EPackage_body_surface_armx surface = surfaces.getCurrentMember(iter);
				// SAR
				EShape_aspect_relationship esar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				esar.setRelated_shape_aspect(null, armEntity);
				esar.setRelating_shape_aspect(null, surface);
				esar.setName(null, "associated body vertical extent");
			}
		}
	}


	/**
	* Unsets/deletes data for Associated_body_vertical_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssociated_body_vertical_extent(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		SdaiIterator iter = asar.createIterator();
		while(iter.next()){
			EShape_aspect_relationship esar = asar.getCurrentMember(iter);
			if((esar.testName(null))&&(esar.getName(null).equals("associated body vertical extent"))){
				esar.deleteApplicationInstance();
			}
		}
	}
	

	/**
	* Sets/creates data for Associated_terminal attribute.
	*
	attribute_mapping associated_terminal(associated_terminal, $PATH, Package_terminal_armx);
		visual_orientation_feature <=
		shape_aspect <-
		shape_aspect_relationship.related_shape_aspect
		{shape_aspect_relationship
		shape_aspect_relationship.name = 'associated terminal'}
		shape_aspect_relationship
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect =>
		placed_feature =>
		package_terminal
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// VOF <- SAR -> PBS
	public static void setAssociated_terminal(SdaiContext context, EVisual_orientation_feature_armx armEntity) throws SdaiException
	{
		unsetAssociated_terminal(context, armEntity);
		if(armEntity.testAssociated_terminal(null)){
			EPackage_terminal_armx terminal = armEntity.getAssociated_terminal(null);
			// SAR
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, armEntity);
			esar.setRelating_shape_aspect(null, terminal);
			esar.setName(null, "associated terminal");
		}
	}


	/**
	* Unsets/deletes data for Associated_terminal attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssociated_terminal(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		SdaiIterator iter = asar.createIterator();
		while(iter.next()){
			EShape_aspect_relationship esar = asar.getCurrentMember(iter);
			if((esar.testName(null))&&(esar.getName(null).equals("associated terminal"))){
				esar.deleteApplicationInstance();
			}
		}
	}
	
	
}