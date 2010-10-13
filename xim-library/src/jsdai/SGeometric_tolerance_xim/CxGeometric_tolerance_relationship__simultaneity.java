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

package jsdai.SGeometric_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SShape_tolerance_schema.CGeometric_tolerance_relationship;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_relationship;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxGeometric_tolerance_relationship__simultaneity extends CGeometric_tolerance_relationship__simultaneity implements EMappedXIMEntity
{
	// From CShape_aspect.java
	//going through all the attributes: #5629499534230354=EXPLICIT_ATTRIBUTE('name',#5629499534230352,0,#5629499534229354,$,.F.);
	//<01> generating methods for consolidated attribute:  name
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EGeometric_tolerance_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EGeometric_tolerance_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EGeometric_tolerance_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EGeometric_tolerance_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EGeometric_tolerance_relationship type) throws SdaiException {
		return a0$;
	}

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CGeometric_tolerance_relationship.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		{geometric_tolerance_relationship
		geometric_tolerance_relationship.name = 'simultaneity'}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EGeometric_tolerance_relationship__simultaneity armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "simultaneity");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, EGeometric_tolerance_relationship__simultaneity armEntity) throws SdaiException {
		armEntity.unsetName(null);
	}

}