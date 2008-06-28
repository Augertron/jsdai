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

package jsdai.SItem_definition_structure_xim;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SPart_occurrence_xim.CxPart_occurrence_definition_relationship;
import jsdai.SProduct_structure_schema.CAssembly_component_usage;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;

public class CxAssembled_part_association extends CAssembled_part_association implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CAssembly_component_usage.definition);

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
	public static void setMappingConstraints(SdaiContext context, EAssembled_part_association armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPart_occurrence_definition_relationship.setMappingConstraints(context, armEntity);
		if(!armEntity.testName(null))
			armEntity.setName(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, EAssembled_part_association armEntity) throws SdaiException
	{
		CxPart_occurrence_definition_relationship.unsetMappingConstraints(context, armEntity);
	}
	
	
	//********** "managed_design_object" attributes

}
