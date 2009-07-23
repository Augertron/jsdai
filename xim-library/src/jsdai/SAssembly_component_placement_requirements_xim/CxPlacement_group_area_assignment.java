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

package jsdai.SAssembly_component_placement_requirements_xim;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SProduct_property_definition_schema.CProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.CShape_aspect;
import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxPlacement_group_area_assignment extends CPlacement_group_area_assignment implements EMappedXIMEntity
{
	// Taken from CShape_aspect_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EShape_aspect_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EShape_aspect_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EShape_aspect_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect_relationship type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect_relationship type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect_relationship type) throws SdaiException {
		return a1$;
	}

	// attribute (current explicit or supertype explicit) : relating_shape_aspect, base type: entity shape_aspect
/*	public static int usedinRelating_shape_aspect(EShape_aspect_relationship type, EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return test_instance(a2);
	}
	public EShape_aspect getRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a2 = get_instance(a2);
		return (EShape_aspect)a2;
	}*/
	public void setRelating_shape_aspect(EShape_aspect_relationship type, EShape_aspect value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return a2$;
	}
	// ENDOF Taken from CShape_aspect_relationship
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CShape_aspect_relationship.definition);

		setMappingConstraints(context, this);

		setPlaced_group(context, this);
		
		//********** "managed_design_object" attributes

		unsetPlaced_group(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetPlaced_group(context, this);		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
		{shape_aspect_relationship
		shape_aspect_relationship.name = 'area impacted group'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPlacement_group_area_assignment armEntity) throws SdaiException
	{
		armEntity.setName(null, "area impacted group");
	}

	public static void unsetMappingConstraints(SdaiContext context, EPlacement_group_area_assignment armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
	
	//********** "managed_design_object" attributes

	/**
	* Sets/creates data for mapping constraints for attribute placed_group.
	*
	* <p>
	*  attribute_mapping placed_group(placed_group, $PATH, Placement_group_requirement_definition);
			shape_aspect_relationship
			shape_aspect_relationship.relating_shape_aspect ->
			shape_aspect
			shape_aspect.of_shape ->
			product_definition_shape <=
			property_definition
			property_definition.definition ->
			characterized_definition
			characterized_definition = characterized_product_definition
			characterized_product_definition
			characterized_product_definition = product_definition
			{product_definition <-
			product_definition_context_association.definition
			product_definition_context_association
			{product_definition_context_association.role ->
			product_definition_context_role
			product_definition_context_role.name = 'part definition type'}
			product_definition_context_association.frame_of_reference ->
			product_definition_context <=
			application_context_element
			application_context_element.name = 'design requirement'}
			product_definition =>
			component_definition =>
			group_product_definition
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SAR -> SA -> PDS -> GPD
	public static void setPlaced_group(SdaiContext context, EPlacement_group_area_assignment armEntity) throws SdaiException
	{
		unsetPlaced_group(context, armEntity);
		if(armEntity.testPlaced_group(null)){
			EPlacement_group_requirement_definition group = armEntity.getPlaced_group(null);
			// PDS -> GPD
            LangUtils.Attribute_and_value_structure[] pdStructure = {
                    new LangUtils.Attribute_and_value_structure(
                    CProduct_definition_shape.attributeDefinition(null), group)};
            EProduct_definition_shape epd = (EProduct_definition_shape)
                    LangUtils.createInstanceIfNeeded(context,
                    CProduct_definition_shape.definition,
                    pdStructure);
            if (!epd.testName(null))
            	epd.setName(null, "");
			// SA -> PDS
            LangUtils.Attribute_and_value_structure[] saStructure = {
                    new LangUtils.Attribute_and_value_structure(
                    CShape_aspect.attributeOf_shape(null), epd)};
            EShape_aspect esa = (EShape_aspect)
                    LangUtils.createInstanceIfNeeded(context,
                    CShape_aspect.definition,
                    saStructure);
            if (!esa.testName(null))
            	esa.setName(null, "");
            if (!esa.testProduct_definitional(null))
            	esa.setProduct_definitional(null, ELogical.UNKNOWN);
            
            armEntity.setRelating_shape_aspect(null, esa);
		}
	}

	public static void unsetPlaced_group(SdaiContext context, EPlacement_group_area_assignment armEntity) throws SdaiException
	{
		armEntity.unsetRelating_shape_aspect(null);
	}
	
}
