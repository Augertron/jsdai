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

import jsdai.SAic_geometric_tolerances.CSymmetry_tolerance;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.libutil.EMappedXIMEntity;

public class CxSymmetry_tolerance_armx  extends CSymmetry_tolerance_armx implements EMappedXIMEntity
{
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CSymmetry_tolerance.definition);

		setMappingConstraints(context, this);

        // significant_digits : OPTIONAL INTEGER;
		setSignificant_digits(context, this);
		
        // value_determination : OPTIONAL STRING;
		setValue_determination(context, this);
		
		// TODO - implement this method
		if(testAffected_plane(null)){
			SdaiSession.println(" Need to implement setter for CxSymmetry_tolerance_armx.affected_plane "+this);
		}
		// setAffected_plane(context, this);
		
		// clean ARM
        // significant_digits : OPTIONAL INTEGER;
		unsetSignificant_digits(null);
		
        // value_determination : OPTIONAL STRING;
		unsetValue_determination(null);
		
		unsetAffected_plane(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
	        // significant_digits : OPTIONAL INTEGER;
			unsetSignificant_digits(context, this);
			
	        // value_determination : OPTIONAL STRING;
			unsetValue_determination(context, this);
	
			// TODO - implement this method
			// unsetAffected_plane(context, this);			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			ESymmetry_tolerance_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxGeometric_tolerance_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ESymmetry_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for Significant_digits attribute.
	 * 
	 * <p>
	attribute_mapping significant_digits(significant_digits, precision_qualifier.precision_value);
		geometric_tolerance 
		geometric_tolerance.magnitude -> 
		measure_with_unit <- 
		measure_qualification.qualified_measure 
		measure_qualification 
		measure_qualification.qualifiers[i] -> 
		value_qualifier 
		value_qualifier = precision_qualifier 
		precision_qualifier 
		precision_qualifier.precision_value
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// GT <- MQ -> PQ 
	public static void setSignificant_digits(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setSignificant_digits(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute Significant_digits.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetSignificant_digits(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetSignificant_digits(context, armEntity);
	}

	/**
	 * Sets/creates data for Significant_digits attribute.
	 * 
	 * <p>
	attribute_mapping value_determination(value_determination, type_qualifier.name);
		geometric_tolerance 
		geometric_tolerance.magnitude -> 
		measure_with_unit <- 
		measure_qualification.qualified_measure 
		measure_qualification 
		measure_qualification.qualifiers[i] -> 
		value_qualifier 
		value_qualifier = type_qualifier 
		type_qualifier 
		type_qualifier.name
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// GT <- MQ -> PQ 
	public static void setValue_determination(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setValue_determination(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute Significant_digits.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetValue_determination(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetValue_determination(context, armEntity);
	}
	
}