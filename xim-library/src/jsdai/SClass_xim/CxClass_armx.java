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

package jsdai.SClass_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SClassification_schema.CClass;

public class CxClass_armx extends CClass_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CClass.definition);

		setMappingConstraints(context, this);
		
		setId_x(context, this);
		
		unsetId_x(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetId_x(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		class <=
		group
		{group.description = 'electrical conductivity'}
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
	}


	public static void setId_x(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
		if(armEntity.testId_x(null)){
			String id = armEntity.getId_x(null);
			CxAP210ARMUtilities.setGroupId(context, armEntity, id);
		}
	}

	public static void unsetId_x(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetGroupId(context, armEntity);
	}

	
}
