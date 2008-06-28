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

package jsdai.SPhysical_unit_design_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFeature_and_connection_zone_xim.CxConnection_zone;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_property_assignment_xim.EShape_element;


public class CxConnection_zone_in_design_view extends CConnection_zone_in_design_view implements EMappedXIMEntity
{

	// From CShape_element.java
	//	methods for attribute: element_name, base type: STRING
/*	public boolean testElement_name(EShape_element type) throws SdaiException {
		return testName((jsdai.SProduct_property_definition_schema.EShape_aspect)null);
	}
	public String getElement_name(EShape_element type) throws SdaiException {
		return getName((jsdai.SProduct_property_definition_schema.EShape_aspect)null);
	}*/
	public void setElement_name(EShape_element type, String value) throws SdaiException {
		setName((jsdai.SProduct_property_definition_schema.EShape_aspect)null, value);
	}
	public void unsetElement_name(EShape_element type) throws SdaiException {
		unsetName((jsdai.SProduct_property_definition_schema.EShape_aspect)null);
	}
	public static jsdai.dictionary.EAttribute attributeElement_name(EShape_element type) throws SdaiException {
		return attributeName((jsdai.SProduct_property_definition_schema.EShape_aspect)null);
	}
	
	// From CShape_aspect.java
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

		setTemp("AIM", CShape_aspect.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	{shape_aspect
	* 	shape_aspect.description = `connection zone'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EConnection_zone_in_design_view armEntity) throws SdaiException
	{
		CxConnection_zone.setMappingConstraints(context, armEntity);
		armEntity.setName(null, "connection zone in design view");
	}

	public static void unsetMappingConstraints(SdaiContext context, EConnection_zone_in_design_view armEntity) throws SdaiException
	{
		CxConnection_zone.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName(null);
	}
	
	
}
