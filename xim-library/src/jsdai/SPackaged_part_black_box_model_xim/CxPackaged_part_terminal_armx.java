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

package jsdai.SPackaged_part_black_box_model_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFunctional_assignment_to_part_xim.CxPart_terminal;
import jsdai.SPackage_xim.*;
import jsdai.SPackaged_part_black_box_model_mim.CPackaged_part_terminal;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPackaged_part_terminal_armx extends CPackaged_part_terminal_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
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

		setTemp("AIM", CPackaged_part_terminal.definition);

		setMappingConstraints(context, this);
		
		// Part_feature
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);

		// terminal_of_package 
		setTerminal_of_package(context, this); 
		
		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);
		
		// terminal_of_package 
		unsetTerminal_of_package(null); 
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);

			// terminal_of_package 
			unsetTerminal_of_package(context, this); 
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  packaged_part_terminal &lt;=
	 *  shape_aspect
	 *  {(shape_aspect.description = 'interface terminal')
	 *  (shape_aspect.description = 'join terminal')}
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
			EPackaged_part_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_terminal.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPackaged_part_terminal_armx armEntity) throws SdaiException {
		CxPart_terminal.unsetMappingConstraints(context, armEntity);
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

	//********** "packaged_part_terminal" attributes

	/**
	* Sets/creates data for terminal_of_package attribute.
	*
	*
	*  attribute_mapping terminal_of_package_package_terminal (terminal_of_package
	* , (*PATH*), package_terminal);
	* 	packaged_part_terminal <=
	* 	{shape_aspect
	* 	shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition <-
	* 	product_definition_relationship.related_product_definition
	* 	product_definition_relationship
	* 	{product_definition_relationship.name = `used package'}
	* 	product_definition_relationship.relating_product_definition ->
	* 	{product_definition =>
	* 	physical_unit =>
	* 	(package)
	* 	(externally_defined_physical_unit =>
	* 	externally_defined_package)
	* 	(externally_defined_physical_unit =>
	* 	library_defined_physical_unit =>
	* 	library_defined_package)}
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition =>
	* 	product_definition_shape <-
	* 	shape_aspect.of_shape
	* 	shape_aspect =>
	* 	package_terminal}
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `terminal of package'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	package_terminal
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// Just PPT <- SAR -> PT
	public static void setTerminal_of_package(SdaiContext context, EPackaged_part_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetTerminal_of_package(context, armEntity);

		if (armEntity.testTerminal_of_package(null))
		{
			APackage_terminal_armx armTerminal_of_packages = armEntity.getTerminal_of_package(null);
         for (int i = 1; i <= armTerminal_of_packages.getMemberCount(); i++) {
            EPackage_terminal_armx armTerminal_of_package = armTerminal_of_packages.getByIndex(i);
            // Just create SAR
            EShape_aspect_relationship relationship = (EShape_aspect_relationship)
               context.working_model.createEntityInstance(CShape_aspect_relationship.class);
            relationship.setName(null, "terminal of package");
            relationship.setRelating_shape_aspect(null, armEntity);
            relationship.setRelated_shape_aspect(null, armTerminal_of_package);
         }

		}
	}


	/**
	* Unsets/deletes data for terminal_of_package attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTerminal_of_package(SdaiContext context, EPackaged_part_terminal_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
		for(int i=1;i<=relationships.getMemberCount();){
			EShape_aspect_relationship relationship = relationships.getByIndex(i);
			if((relationship.testName(null))&&(relationship.getName(null).equals("terminal of package"))){
            relationships.removeByIndex(i);
            relationship.deleteApplicationInstance();
         }
         else
            i++;
		}
	}
	
	
}