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

package jsdai.SAssembly_technology_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SAssembly_technology_mim.CAssembly_joint;
import jsdai.SProduct_property_definition_schema.*;

public class CxAssembly_joint_armx extends CAssembly_joint_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Assembly_joint - Shape_aspect

	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(jsdai.SProduct_property_definition_schema.EShape_aspect type) throws SdaiException {
		return test_logical(a7);
	}
	public int getProduct_definitional(jsdai.SProduct_property_definition_schema.EShape_aspect type) throws SdaiException {
		return get_logical(a7);
	}*/
	public void setProduct_definitional(jsdai.SProduct_property_definition_schema.EShape_aspect type, int value) throws SdaiException {
		a7 = set_logical(value);
	}
	public void unsetProduct_definitional(jsdai.SProduct_property_definition_schema.EShape_aspect type) throws SdaiException {
		a7 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(jsdai.SProduct_property_definition_schema.EShape_aspect type) throws SdaiException {
		return a7$;
	}
	// ENDOF Taken from Assembly_joint - Shape_aspect

	// Taken from Shape_aspect_relationship
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
	// ENDOF Taken from Shape_aspect_relationship
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAssembly_joint.definition);

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
	* 	assembly_joint &lt;= 
	*  [shape_aspect_relationship] 
	*  [shape_aspect]
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EAssembly_joint_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
		// if(!armEntity.testProduct_definitional(null))
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
		// if(!armEntity.testName((EShape_aspect_relationship)null))
			armEntity.setName((EShape_aspect_relationship)null, "");
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EAssembly_joint_armx armEntity) throws SdaiException
	{
	}
	
}
