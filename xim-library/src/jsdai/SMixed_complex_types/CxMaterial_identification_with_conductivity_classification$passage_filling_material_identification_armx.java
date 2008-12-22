/*
 * $Id$passage_filling_material_identification_armx.java,v 1.6 2008/05/29 16:08:46 vaidas Exp $
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
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: LKSoft</p>
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConductivity_material_aspects_xim.CxMaterial_identification_with_conductivity_classification;
import jsdai.SFabrication_technology_xim.CxPassage_filling_material_identification_armx;

public class CxMaterial_identification_with_conductivity_classification$passage_filling_material_identification_armx extends CMaterial_identification_with_conductivity_classification$passage_filling_material_identification_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMaterial_designation_with_conductivity_classification$passage_filling_material_identification.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CMaterial_identification_with_conductivity_classification$passage_filling_material_identification_armx armEntity) throws SdaiException
	{
		CxMaterial_identification_with_conductivity_classification.setMappingConstraints(context, armEntity);
		CxPassage_filling_material_identification_armx.setMappingConstraints(context, armEntity);
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CMaterial_identification_with_conductivity_classification$passage_filling_material_identification_armx armEntity) throws SdaiException
	{
		CxMaterial_identification_with_conductivity_classification.unsetMappingConstraints(context, armEntity);
		CxPassage_filling_material_identification_armx.unsetMappingConstraints(context, armEntity);		
	}

}
