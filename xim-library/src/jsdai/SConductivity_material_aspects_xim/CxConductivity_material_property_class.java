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

package jsdai.SConductivity_material_aspects_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SClass_xim.CxClass_armx;
import jsdai.SClass_xim.EClass_armx;
import jsdai.SClassification_schema.CClass;

public class CxConductivity_material_property_class extends CConductivity_material_property_class implements EMappedXIMEntity
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
		{(group.description = 'thermal conductivity')
		(group.description = 'electrical conductivity')}
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EConductivity_material_property_class armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "thermal conductivity");
	}

	public static void unsetMappingConstraints(SdaiContext context, EConductivity_material_property_class armEntity) throws SdaiException
	{
		armEntity.unsetDescription(null);
	}

//	 Class_armx
	public static void setId_x(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
		CxClass_armx.setId_x(context, armEntity);
	}

	public static void unsetId_x(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
		CxClass_armx.unsetId_x(context, armEntity);
	}

}
