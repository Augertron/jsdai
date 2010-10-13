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

package jsdai.SConstruction_geometry_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConstruction_geometry_mim.CConstructive_geometry_representation_relationship;
import jsdai.SRepresentation_schema.*;

public class CxConstructive_geometry_association extends CConstructive_geometry_association implements EMappedXIMEntity
{

	// Taken from Representation_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_relationship type) throws SdaiException {
		return a0$;
	}
	
	// ENDOF Taken from Representation_relationship
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CConstructive_geometry_representation_relationship.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EConstructive_geometry_association armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
		armEntity.setName(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, EConstructive_geometry_association armEntity) throws SdaiException
	{
	}
	
}
