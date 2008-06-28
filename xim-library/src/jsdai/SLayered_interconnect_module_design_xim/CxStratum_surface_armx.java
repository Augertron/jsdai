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

package jsdai.SLayered_interconnect_module_design_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_interconnect_module_design_mim.CStratum_surface;
import jsdai.SProduct_property_definition_schema.*;

public class CxStratum_surface_armx extends CStratum_surface_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Shape_aspect

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EShape_aspect type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EShape_aspect type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EShape_aspect type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
		return a0$;
	}

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
	// ENDOF Taken from Shape_aspect

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_surface.definition);

		setMappingConstraints(context, this);

		// name_x
		setName_x(context, this);
		
		// clean ARM
		unsetName_x(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// name_x
		unsetName_x(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	stratum_surface &lt;=
	*  shape_aspect
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_surface_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
//		if(!armEntity.testProduct_definitional(null))
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
//		if(!armEntity.testName(null))
			armEntity.setName(null, "");
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_surface_armx armEntity) throws SdaiException
	{
	}
	

	/**
	* Sets/creates data for name attribute.
	*
	* <p>
	*  attribute_mapping name (name
	* );
	* 	stratum_surface
	* 	stratum_surface <=
	* 	shape_aspect
	* 	{shape_aspect
	* 	(shape_aspect.description = `primary surface')
	* 	(shape_aspect.description = `secondary surface')}
	* 	shape_aspect.description
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping name (name
	* );
	* 	stratum_surface
	* 	stratum_surface <=
	* 	shape_aspect
	* 	{shape_aspect
	* 	(shape_aspect.description = `primary surface')
	* 	(shape_aspect.description = `secondary surface')}
	* 	shape_aspect.description
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setName_x(SdaiContext context, EStratum_surface_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetName_x(context, armEntity);

		if (armEntity.testName_x(null))
		{

			int armName = armEntity.getName_x(null);
		   String value = EStratum_surface_designation.toString(armName).toLowerCase().replace('_', ' ');
			armEntity.setDescription(null, value);
		}
	}


	/**
	* Unsets/deletes data for name attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetName_x(SdaiContext context, EStratum_surface_armx armEntity) throws SdaiException
	{
		armEntity.unsetDescription(null);
	}
	
	
}
