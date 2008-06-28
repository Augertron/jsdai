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

package jsdai.SAssembly_module_design_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SComponent_feature_xim.*;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SPhysical_component_feature_mim.CPhysical_component_terminal;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;

public class CxMovable_packaged_component_join_terminal extends CMovable_packaged_component_join_terminal implements EMappedXIMEntity{

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

		setTemp("AIM", CPhysical_component_terminal.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		// local_swappable
		setLocal_swappable(context, this);
		
      // global_swappable
		setGlobal_swappable(context, this);
		
		// swap_code		
		setSwap_code(context, this);
		
		// wire_terminal_length
		setWire_terminal_length(context, this);
		
		// Clean ARM
		unsetDefinition(null);

		// local_swappable
		unsetLocal_swappable(null);
		
      // global_swappable
		unsetGlobal_swappable(null);
		
		// swap_code		
		unsetSwap_code(null);

		// wire_terminal_length
		unsetWire_terminal_length(null);
		
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

			// wire_terminal_length
			unsetWire_terminal_length(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  physical_component_terminal &lt;=
	 *  component_terminal &lt;=
	 *  component_feature &lt;=				
	 *  shape_aspect
	 *  {shape_aspect
	 *  [shape_aspect.description = 'movable packaged component join terminal']
	 *  [shape_aspect.product_definitional = .TRUE.]}
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
			EMovable_packaged_component_join_terminal armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxPackaged_component_join_terminal.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "movable packaged component join terminal");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EMovable_packaged_component_join_terminal armEntity) throws SdaiException {
		CxPackaged_component_join_terminal.unsetMappingConstraints(context, armEntity);
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

	//********** "component_terminal" attributes

	/**
	* Sets/creates data for local_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.setLocal_swappable(context, armEntity);		
	}


	/**
	* Unsets/deletes data for local_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.unsetLocal_swappable(context, armEntity);
	}


	/**
	* Sets/creates data for global_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setGlobal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.setGlobal_swappable(context, armEntity);		
	}


	/**
	* Unsets/deletes data for global_swappable attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetGlobal_swappable(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.unsetGlobal_swappable(context, armEntity);
	}


	/**
	* Sets/creates data for swap_code attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setSwap_code(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.setSwap_code(context, armEntity);		
	}


	/**
	* Unsets/deletes data for swap_code attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetSwap_code(SdaiContext context, EComponent_terminal_armx armEntity) throws SdaiException
	{
		CxComponent_terminal_armx.unsetSwap_code(context, armEntity);
	}

	//********** "Movable_packaged_component_join_terminal" attributes

	/**
	* Sets/creates data for wire_terminal_length attribute.
	*
	* <p>
	*  attribute_mapping maximum_wire_terminal_length (maximum_wire_terminal_length
	* , length_measure_with_unit);
	* 	physical_component_terminal &lt;=
	*  component_terminal &lt;=
	*  component_feature &lt;=	
	*  shape_aspect
	*  shape_definition = shape_aspect
	*  shape_definition
	*  characterized_definition = shape_definition 
	*  characterized_definition &lt;-
	*  property_definition.definition 
	*  property_definition &lt;-
	*  property_definition_representation.definition
	*  property_definition_representation
	*  property_definition_representation.used_representation -&gt;
	*  representation
	*  representation.items[i] -&gt; 
	*  {representation_item 
	*  representation_item.name = 'maximum wire length'}
	*  representation_item =&gt; 
	*  measure_representation_item &lt;=
	*  measure_with_unit =&gt;
	*  length_measure_with_unit
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// ST <- PD <- PDR -> R -> LMWU
	public static void setWire_terminal_length(SdaiContext context, EMovable_packaged_component_join_terminal armEntity) throws SdaiException
	{
		//unset old values
		unsetWire_terminal_length(context, armEntity);

		if (armEntity.testWire_terminal_length(null))
		{
	      ETolerance_characteristic characteristic = armEntity.getWire_terminal_length(null);
	      
	      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "wire length", characteristic);
		}
	}


	/**
	* Unsets/deletes data for wire_terminal_length attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetWire_terminal_length(SdaiContext context, EMovable_packaged_component_join_terminal armEntity) throws SdaiException
	{
		String keyword = "wire length";
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, keyword);
	}

	/**
	* Sets/creates data for max/min_wire_length attributes.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	static void setRepresentation_item(SdaiContext context, EMovable_packaged_component_join_terminal armEntity, String representationItemName, ELength_measure_with_unit length)throws SdaiException{
		// Get/create representation
		jsdai.SRepresentation_schema.ARepresentation representations =
			CxAP210ARMUtilities.getAllRepresentationsOfShapeAspect(armEntity, context, null);
		jsdai.SRepresentation_schema.ERepresentation representation;
		// Take first suitable, since geometry is not likely to happen for technology things
		if(representations.getMemberCount() > 0)
			representation = representations.getByIndex(1);
		else{
			// R
			representation = (jsdai.SRepresentation_schema.ERepresentation)
				context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
         representation.setName(null, "");
			// Context
			jsdai.SRepresentation_schema.ERepresentation_context representation_context =
            CxAP210ARMUtilities.createRepresentation_context(context,
                                                                       "", "", true);
			representation.setContext_of_items(null, representation_context);
			// PD
			EProperty_definition propDef = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.class);
			propDef.setDefinition(null, armEntity);
			propDef.setName(null, "");
			// PDR
			EProperty_definition_representation propDefRep = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.class);
			propDefRep.setDefinition(null, propDef);
			propDefRep.setUsed_representation(null, representation);
		}
		EMeasure_representation_item item;
		if(length instanceof EMeasure_representation_item){
			item = (EMeasure_representation_item)length;
		}
		else{
			item = (EMeasure_representation_item)
				context.working_model.substituteInstance(length, CLength_measure_with_unit$measure_representation_item.definition);
		}
		item.setName(null, representationItemName);
		// R -> Item
		ARepresentation_item items;
		if(representation.testItems(null))
			items = representation.getItems(null);
		else
			items = representation.createItems(null);
		items.addUnordered(item);
	}
	
	
}