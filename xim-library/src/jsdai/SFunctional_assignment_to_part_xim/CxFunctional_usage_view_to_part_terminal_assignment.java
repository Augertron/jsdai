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

package jsdai.SFunctional_assignment_to_part_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_property_definition_schema.*;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxFunctional_usage_view_to_part_terminal_assignment extends CFunctional_usage_view_to_part_terminal_assignment implements EMappedXIMEntity
{


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
	
	// end of taken from 
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CShape_aspect_relationship.definition);

			setMappingConstraints(context, this);

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
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'functional terminal allocation'}	
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EFunctional_usage_view_to_part_terminal_assignment armEntity) throws SdaiException
	{
		armEntity.setName(null, "functional terminal allocation");
	}

	public static void unsetMappingConstraints(SdaiContext context, EFunctional_usage_view_to_part_terminal_assignment armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
}
