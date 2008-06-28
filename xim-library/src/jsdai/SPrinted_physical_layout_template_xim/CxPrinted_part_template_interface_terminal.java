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

public class CxPrinted_part_template_interface_terminal extends CPrinted_part_template_interface_terminal implements EMappedXIMEntity{

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

		setTemp("AIM", CPrinted_part_template_terminal.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
      // connection_zone_category 
		setConnection_zone_category(context, this);
		
		// Clean ARM
		// Connection_area
		unsetConnection_area(null);
		
      // connection_zone_category 
		unsetConnection_zone_category(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
	      // connection_zone_category 
			unsetConnection_zone_category(context, this);

	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  printed_part_template_terminal &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'interface terminal'}
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
			EPrinted_part_template_interface_terminal armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxPrinted_part_template_terminal_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "interface terminal");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPrinted_part_template_interface_terminal armEntity) throws SdaiException {
		CxPrinted_part_template_terminal_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
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

}