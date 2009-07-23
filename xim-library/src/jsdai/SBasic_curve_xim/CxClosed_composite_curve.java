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

package jsdai.SBasic_curve_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGeometry_schema.CComposite_curve;

public class CxClosed_composite_curve extends CClosed_composite_curve implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CComposite_curve.definition);

		setMappingConstraints(context, this);
		// Removed according SEDS-1672
		// setIdentification(context, this);
		
		//	Removed according SEDS-1672
		// unsetIdentification(null);

		//********** "managed_design_object" attributes

	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		//	Removed according SEDS-1672
		// unsetIdentification(context, this);
		
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	{shape_aspect
	* 	shape_aspect.description = `connection zone'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EClosed_composite_curve armEntity) throws SdaiException
	{
	}

	public static void unsetMappingConstraints(SdaiContext context, EClosed_composite_curve armEntity) throws SdaiException
	{
	}
	
}
