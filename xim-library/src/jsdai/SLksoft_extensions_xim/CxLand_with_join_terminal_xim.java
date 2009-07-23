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

package jsdai.SLksoft_extensions_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.libutil.XimEntityStandalone;
import jsdai.SComponent_feature_xim.*;
import jsdai.SLand_mim.CLand_with_join_terminal;
import jsdai.SLand_xim.CxLand_armx;
import jsdai.SLand_xim.CxLand_join_terminal;
import jsdai.SLayered_interconnect_module_design_mim.CLaminate_component_join_terminal;
import jsdai.SLayered_interconnect_module_design_mim.ELaminate_component_join_terminal;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;

public class CxLand_with_join_terminal_xim extends CLand_with_join_terminal_xim implements EMappedXIMEntity, XimEntityStandalone{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a20);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a20);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a20 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a20 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a20$;
	}

	/*	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
	return test_string(a21);
	}
	public Object getOf_shape(EShape_aspect type) throws SdaiException {
		return get_string(a21);
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a21 = set_instanceX(a21, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a21 = unset_instance(a21);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a21$;
	}
	
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a22);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a22);
	}
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a22 = set_logical(value);
	} */
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a22 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a22$;
	}
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLand_with_join_terminal.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		// local_swappable
		setLocal_swappable(context, this);
		
      // global_swappable
		setGlobal_swappable(context, this);
		
		// swap_code		
		setSwap_code(context, this);
		
		// Clean ARM
		unsetDefinition((EComponent_feature_armx)null);

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
	 *  laminate_component_join_terminal &lt;=
	 *  laminate_component_feature &lt;=
	 *  component_feature &lt;=				
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'land join terminal'}
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
			ELand_with_join_terminal_xim armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxLand_join_terminal.setMappingConstraints(context, armEntity);
		CxLand_armx.setMappingConstraints(context, armEntity);
		armEntity.setOf_shape(null, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ELand_with_join_terminal_xim armEntity) throws SdaiException {
		CxLand_join_terminal.unsetMappingConstraints(context, armEntity);
		CxLand_armx.unsetMappingConstraints(context, armEntity);
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

	ELaminate_component_join_terminal aimInstance;
	
	public EEntity getAimInstance(SdaiContext context) throws SdaiException {
		if(aimInstance == null){
			aimInstance = (ELaminate_component_join_terminal)
				context.working_model.createEntityInstance(CLaminate_component_join_terminal.definition);
		}
		return aimInstance;
	}
	
	public void unsetAimInstance(SdaiContext context) throws SdaiException{
		aimInstance = null;
	}
	
	
}