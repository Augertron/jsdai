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

package jsdai.SPackaged_connector_model_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPackaged_connector_model_mim.CPackaged_connector_terminal_relationship;
import jsdai.SProduct_property_definition_schema.*;

public class CxPackaged_connector_terminal_relationship_armx extends CPackaged_connector_terminal_relationship_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Packaged_connector_terminal_relationship - Shape_aspect_relationship

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EShape_aspect_relationship type) throws SdaiException {
		return test_string(a4);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EShape_aspect_relationship type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EShape_aspect_relationship type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EShape_aspect_relationship type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EShape_aspect_relationship type) throws SdaiException {
		return a4$;
	}
	// ENDOF Taken from Packaged_connector_terminal_relationship - Shape_aspect_relationship

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

		setTemp("AIM", CPackaged_connector_terminal_relationship.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	packaged_connector_terminal_relationship &lt;=
	*  [shape_aspect]
	*  [shape_aspect_relationship]
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPackaged_connector_terminal_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
		armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
		armEntity.setName((EShape_aspect_relationship)null, "");
		armEntity.setName((EShape_aspect)null, "");
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EPackaged_connector_terminal_relationship_armx armEntity) throws SdaiException
	{
	}
	
}
