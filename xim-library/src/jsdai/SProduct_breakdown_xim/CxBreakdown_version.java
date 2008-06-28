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

package jsdai.SProduct_breakdown_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SApplication_context_schema.EProduct_definition_context;
import jsdai.SProduct_definition_schema.*;

public class CxBreakdown_version extends CBreakdown_version implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_definition_formation.definition);

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
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// Tricky mapping to [product_definition_formation][product_definition]
	public static void setMappingConstraints(SdaiContext context, EBreakdown_version armEntity) throws SdaiException
	{
		// Check if it does not have product_definition attached to it - create it
		AProduct_definition apd = new AProduct_definition();
		CProduct_definition.usedinFormation(null, armEntity, context.domain, apd);
		if(apd.getMemberCount() == 0){
			EProduct_definition epd = (EProduct_definition)
				context.working_model.createEntityInstance(CProduct_definition.definition);
			epd.setId(null, "");
			epd.setFormation(null, armEntity);
			EProduct_definition_context epdc = CxAP210ARMUtilities.createProduct_definition_context(context, "", "", true);
			epd.setFrame_of_reference(null, epdc);
			// System.err.println(" BV "+epd);
		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EBreakdown_version armEntity) throws SdaiException
	{
	}	
	
}
