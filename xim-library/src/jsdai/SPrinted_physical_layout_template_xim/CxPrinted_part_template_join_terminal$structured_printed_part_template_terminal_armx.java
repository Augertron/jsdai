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

package jsdai.SPrinted_physical_layout_template_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SPrinted_physical_layout_template_mim.*;
import jsdai.SProduct_property_definition_schema.EShape_aspect;

public class CxPrinted_part_template_join_terminal$structured_printed_part_template_terminal_armx extends CPrinted_part_template_join_terminal$structured_printed_part_template_terminal_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a2);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a2$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a4);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a4);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a4 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a4 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a4$;
	}
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStructured_printed_part_template_terminal.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
      // connection_zone_category 
		setConnection_zone_category(context, this);
		
		setAssociated_composition_relationship(context, this);
		
		// Clean ARM
		// Connection_area
		unsetConnection_area(null);
		
      // connection_zone_category 
		unsetConnection_zone_category(null);

		unsetAssociated_composition_relationship(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
	      // connection_zone_category 
			unsetConnection_zone_category(context, this);

			unsetAssociated_composition_relationship(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  printed_part_template_terminal &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'join terminal'}
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
			CPrinted_part_template_join_terminal$structured_printed_part_template_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPrinted_part_template_join_terminal.setMappingConstraints(context, armEntity);
		CxStructured_printed_part_template_terminal_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CPrinted_part_template_join_terminal$structured_printed_part_template_terminal_armx armEntity) throws SdaiException {
		CxPrinted_part_template_join_terminal.unsetMappingConstraints(context, armEntity);
		CxStructured_printed_part_template_terminal_armx.unsetMappingConstraints(context, armEntity);
	}

	//********** "printed_part_template_terminal" attributes

	/**
	* Sets/creates data for connection_zone_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_zone_category(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.setConnection_zone_category(context, armEntity);		
	}


	/**
	* Unsets/deletes data for connection_zone_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_zone_category(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.unsetConnection_zone_category(context, armEntity);
	}


	/**
	* Sets/creates data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.setConnection_area(context, armEntity);		
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.unsetConnection_area(context, armEntity);		
	}

	/**
	* Sets/creates data for Associated_composition_relationship attribute.
	*
	*attribute_mapping associated_composition_relationship(associated_composition_relationship, $PATH, Template_location_in_structured_template);
		structured_printed_part_template_terminal <=
		printed_part_template_terminal <=
		shape_aspect
		shape_definition = shape_aspect
		shape_definition
		characterized_definition = shape_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'associated composition relationship'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_product_definition
		characterized_product_definition
		characterized_product_definition = product_definition_relationship
		{product_definition_relationship
		product_definition_relationship.name = 'tlist'}
		product_definition_relationship =>
		product_definition_usage =>
		assembly_component_usage
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SPPTT <- PD <- PDR -> PD -> ACU
	public static void setAssociated_composition_relationship(SdaiContext context, EStructured_printed_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxStructured_printed_part_template_terminal_armx.setAssociated_composition_relationship(context, armEntity);
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssociated_composition_relationship(SdaiContext context, EStructured_printed_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxStructured_printed_part_template_terminal_armx.unsetAssociated_composition_relationship(context, armEntity);
	}
	
}