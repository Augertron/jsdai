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

package jsdai.SShape_property_assignment_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_property_definition_schema.*;

public class CxShape_element extends CShape_element implements EMappedXIMEntity {

	EEntity getAimInstance(){
		return this;
	}
	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	// ENDOF From CShape_aspect.java
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		// Wait for fix
/*TODO		if (getModified()) {
			setModified(false);
		} else {
			return;
		}*/

		setTemp("AIM", CShape_aspect.definition);

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
	 * mapping_constraints; product_definition
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		// Just kind of AIM gap
		armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		armEntity.unsetProduct_definitional(null);
	}

	public static void setId_x(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		if(armEntity.testId_x(null)){
			String id = armEntity.getId_x(null);
			CxAP210ARMUtilities.setId(context, armEntity, id);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		CxAP210ARMUtilities.unsetId(context, armEntity);
	}
	
}