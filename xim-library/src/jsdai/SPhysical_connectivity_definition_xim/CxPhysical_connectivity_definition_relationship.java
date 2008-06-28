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

package jsdai.SPhysical_connectivity_definition_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPhysical_connectivity_definition_relationship extends CPhysical_connectivity_definition_relationship implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


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
	// ENDOF Taken from Shape_aspect_relationship
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_aspect_relationship.definition);

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
	* 	physical_connectivity_definition &lt;=
	*  shape_aspect
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPhysical_connectivity_definition_relationship armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "physical connectivity definition relationship");
	}

	public static void unsetMappingConstraints(SdaiContext context, EPhysical_connectivity_definition_relationship armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
}
