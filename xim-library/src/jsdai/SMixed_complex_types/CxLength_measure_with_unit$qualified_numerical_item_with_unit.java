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

package jsdai.SMixed_complex_types;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SQualified_measure_xim.CxQualified_numerical_item_with_unit;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;

public class CxLength_measure_with_unit$qualified_numerical_item_with_unit extends CLength_measure_with_unit$qualified_numerical_item_with_unit implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CLength_measure_with_unit$measure_representation_item$qualified_representation_item.definition);

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
	* 	 product_definition_relationship
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CLength_measure_with_unit$qualified_numerical_item_with_unit armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxQualified_numerical_item_with_unit.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CLength_measure_with_unit$qualified_numerical_item_with_unit armEntity) throws SdaiException
	{
		CxQualified_numerical_item_with_unit.unsetMappingConstraints(context, armEntity);
	}
	
	
	//********** "managed_design_object" attributes

}
