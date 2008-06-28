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

package jsdai.SAssembly_module_with_interconnect_component_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SComponent_feature_xim.*;
import jsdai.SInterface_component_mim.CPhysical_component_interface_terminal;
import jsdai.SInterface_component_xim.CxPhysical_component_interface_terminal_armx;
import jsdai.SPhysical_unit_design_view_xim.EConnection_zone_in_design_view;
import jsdai.SProduct_property_definition_schema.*;

public class CxInterconnect_component_interface_terminal extends CInterconnect_component_interface_terminal implements EMappedXIMEntity{

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

		setTemp("AIM", CPhysical_component_interface_terminal.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		setConnection(context, this);
		
		// Clean ARM
		unsetDefinition(null);
		
		unsetConnection(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetDefinition(context, this);
			
			unsetConnection(context, this);

	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  physical_component_interface_terminal &lt;=
	 *  physical_component_feature &lt;=
	 *  component_feature &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'interconnect component interface terminal'}
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
			EInterconnect_component_interface_terminal armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxPhysical_component_interface_terminal_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "interconnect component interface terminal");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EInterconnect_component_interface_terminal armEntity) throws SdaiException {
		CxPhysical_component_interface_terminal_armx.unsetMappingConstraints(context, armEntity);
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
	

	//********** "interconnect_component_interface_terminal" attributes

	/**
	* Sets/creates data for connection attribute.
	*
	* <p>
	*  attribute_mapping connection_connection_zone (connection
	* , (*PATH*), connection_zone);
	* 	component_interface_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `terminal connection zone'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect
	* 	{shape_aspect
	* 	shape_aspect.description = `connection zone'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection(SdaiContext context, EInterconnect_component_interface_terminal armEntity) throws SdaiException
	{
		//unset old values
		unsetConnection(context, armEntity);

		if (armEntity.testConnection(null))
		{
			EConnection_zone_in_design_view armConnection = armEntity.getConnection(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "terminal connection zone");
			shape_aspect_relationship.setRelating_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelated_shape_aspect(null, armConnection);
		}
	}


	/**
	* Unsets/deletes data for connection attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection(SdaiContext context, EInterconnect_component_interface_terminal armEntity) throws SdaiException
	{
		EShape_aspect_relationship shape_aspect_relationship = null;
		AShape_aspect_relationship aShape_aspect_relationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, aShape_aspect_relationship);
		for (int i = 1; i <= aShape_aspect_relationship.getMemberCount(); i++) {
			shape_aspect_relationship = aShape_aspect_relationship.getByIndex(i);
			if (shape_aspect_relationship.testName(null) && shape_aspect_relationship.getName(null).equals("terminal connection zone")) {
				shape_aspect_relationship.deleteApplicationInstance();
			}
		}
	}
	
	
}