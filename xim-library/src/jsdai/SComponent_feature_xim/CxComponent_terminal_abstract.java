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

package jsdai.SComponent_feature_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SComponent_feature_mim.CComponent_feature;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxComponent_terminal_abstract extends CComponent_terminal_abstract implements EMappedXIMEntity{

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

		setTemp("AIM", CComponent_feature.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		// local_swappable
		setLocal_swappable(context, this);
		
      // global_swappable
		setGlobal_swappable(context, this);
		
		// swap_code		
		setSwap_code(context, this);
		
		// Clean ARM
		unsetDefinition(null);

		// local_swappable
		unsetLocal_swappable(null);
		
      // global_swappable
		unsetGlobal_swappable(null);
		
		// swap_code		
		unsetSwap_code(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetDefinition(context, this);

			// local_swappable
			unsetLocal_swappable(context, this);
			
	      // global_swappable
			unsetGlobal_swappable(context, this);
			
			// swap_code		
			unsetSwap_code(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  component_feature &lt;=
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
			EComponent_terminal_abstract armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxComponent_feature_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EComponent_terminal_abstract armEntity) throws SdaiException {
		CxComponent_feature_armx.unsetMappingConstraints(context, armEntity);
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

	//********** "component_terminal" attributes

	/**
	* Sets/creates data for local_swappable attribute.
	*
	* <p>
	*  attribute_mapping local_swappable (local_swappable
	* , descriptive_representation_item);
	* 	component_terminal <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `local swappable'}
	* 	representation_item =>
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `false')
	* 	(descriptive_representation_item.description = `true')}
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocal_swappable(SdaiContext context, EComponent_terminal_abstract armEntity) throws SdaiException
	{
		//unset old values
		unsetLocal_swappable(context, armEntity);

		if (armEntity.testLocal_swappable(null))
		{

			boolean armLocal_swappable = armEntity.getLocal_swappable(null);

			//representation
			ERepresentation representation = CxAP210ARMUtilities.findRepresentation(context, armEntity, null, null);

			//descriptive_representation_item
			EDescriptive_representation_item descriptive_representation_item = (EDescriptive_representation_item) context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			descriptive_representation_item.setName(null, "local swappable");
			descriptive_representation_item.setDescription(null, new Boolean(armLocal_swappable).toString());
			representation.getItems(null).addUnordered(descriptive_representation_item);
		}
	}


	/**
	* Unsets/deletes data for local_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocal_swappable(SdaiContext context, EComponent_terminal_abstract armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.clearRepresentationItems(context, armEntity, null, null, "local swappable");
	}


	/**
	* Sets/creates data for global_swappable attribute.
	*
	* <p>
	*  attribute_mapping global_swappable (global_swappable
	* , descriptive_representation_item);
	* 	component_terminal <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `global swappable'}
	* 	representation_item =>
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `false')
	* 	(descriptive_representation_item.description = `true')}
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setGlobal_swappable(SdaiContext context, EComponent_terminal_abstract armEntity) throws SdaiException
	{
		//unset old values
		unsetGlobal_swappable(context, armEntity);

		if (armEntity.testGlobal_swappable(null))
		{
			boolean armGlobal_swappable = armEntity.getGlobal_swappable(null);

			//representation
		    ERepresentation representation = CxAP210ARMUtilities.findRepresentation(context, armEntity, null, null);

			//descriptive_representation_item
			EDescriptive_representation_item descriptive_representation_item = (CDescriptive_representation_item) context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			descriptive_representation_item.setName(null, "global swappable");
			descriptive_representation_item.setDescription(null, new Boolean(armGlobal_swappable).toString());
			representation.getItems(null).addUnordered(descriptive_representation_item);
		}
	}


	/**
	* Unsets/deletes data for global_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetGlobal_swappable(SdaiContext context, EComponent_terminal_abstract armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.clearRepresentationItems(context, armEntity, null, null, "global swappable");
	}


	/**
	* Sets/creates data for swap_code attribute.
	*
	* <p>
	*  attribute_mapping swap_code (swap_code
	* , descriptive_representation_item.description);
	* 	component_terminal <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `swap code'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	descriptive_representation_item.description
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setSwap_code(SdaiContext context, EComponent_terminal_abstract armEntity) throws SdaiException
	{
		//unset old values
		unsetSwap_code(context, armEntity);

		if (armEntity.testSwap_code(null))
		{

			String armSwap_code = armEntity.getSwap_code(null);

		    //representation
			ERepresentation representation = CxAP210ARMUtilities.findRepresentation(context, armEntity, null, null);

			//descriptive_representation_item
			EDescriptive_representation_item descriptive_representation_item = (EDescriptive_representation_item) context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			descriptive_representation_item.setName(null, "swap code");
			descriptive_representation_item.setDescription(null, armSwap_code);
			representation.getItems(null).addUnordered(descriptive_representation_item);
		}
	}


	/**
	* Unsets/deletes data for swap_code attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetSwap_code(SdaiContext context, EComponent_terminal_abstract armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.clearRepresentationItems(context, armEntity, null, null, "swap code");
	}

	
}