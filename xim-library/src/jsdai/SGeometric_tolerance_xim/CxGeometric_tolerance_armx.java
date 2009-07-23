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

import jsdai.SDimension_tolerance_xim.CxPlus_minus_bounds;
import jsdai.SMeasure_schema.EMeasure_with_unit;
import jsdai.SQualified_measure_schema.CMeasure_qualification;
import jsdai.SQualified_measure_schema.CPrecision_qualifier;
import jsdai.SQualified_measure_schema.CType_qualifier;
import jsdai.SQualified_measure_schema.EMeasure_qualification;
import jsdai.SQualified_measure_schema.EPrecision_qualifier;
import jsdai.SQualified_measure_schema.EType_qualifier;
import jsdai.SShape_tolerance_schema.CGeometric_tolerance;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxGeometric_tolerance_armx extends CGeometric_tolerance_armx implements EMappedXIMEntity
{
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CGeometric_tolerance.definition);

		setMappingConstraints(context, this);

        // significant_digits : OPTIONAL INTEGER;
		setSignificant_digits(context, this);
		
        // value_determination : OPTIONAL STRING;
		setValue_determination(context, this);
		
		// clean ARM
        // significant_digits : OPTIONAL INTEGER;
		unsetSignificant_digits(null);
		
        // value_determination : OPTIONAL STRING;
		unsetValue_determination(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
	        // significant_digits : OPTIONAL INTEGER;
			unsetSignificant_digits(context, this);
			
	        // value_determination : OPTIONAL STRING;
			unsetValue_determination(context, this);
			
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
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
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
		unsetSignificant_digits(context, armEntity);
		if(armEntity.testSignificant_digits(null)){
			int value = armEntity.getSignificant_digits(null);
			if(!armEntity.testMagnitude(null)){
				SdaiSession.println(" Magnitude must be set for "+armEntity);
				return;
			}
			// PQ
			LangUtils.Attribute_and_value_structure[] pqStructure = {
					new LangUtils.Attribute_and_value_structure(
							CPrecision_qualifier.attributePrecision_value(null), new Integer(value))
			};
			EPrecision_qualifier epq = (EPrecision_qualifier) 
				LangUtils.createInstanceIfNeeded(context,
						CPrecision_qualifier.definition, pqStructure);
			// MQ-Lower
			EMeasure_qualification emqLower = (EMeasure_qualification)
				context.working_model.createEntityInstance(CMeasure_qualification.definition);
			emqLower.setQualified_measure(null, armEntity.getMagnitude(null));
			emqLower.createQualifiers(null).addUnordered(epq);
			emqLower.setName(null, "");
			emqLower.setDescription(null, "");
		}
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
		if(armEntity.testMagnitude(null)){
			EMeasure_with_unit emwu = armEntity.getMagnitude(null);
			CxPlus_minus_bounds.cleanupPath(context, emwu, EPrecision_qualifier.class);
		}
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
		unsetValue_determination(context, armEntity);
		if(armEntity.testValue_determination(null)){
			String value = armEntity.getValue_determination(null);
			if(!armEntity.testMagnitude(null)){
				SdaiSession.println(" Magnitude must be set for "+armEntity);
				return;
			}
			// PQ
			LangUtils.Attribute_and_value_structure[] tqStructure = {
					new LangUtils.Attribute_and_value_structure(
							CType_qualifier.attributeName(null), value)
			};
			EType_qualifier etq = (EType_qualifier) 
				LangUtils.createInstanceIfNeeded(context,
						CType_qualifier.definition, tqStructure);
			// MQ-Lower
			EMeasure_qualification emqLower = (EMeasure_qualification)
				context.working_model.createEntityInstance(CMeasure_qualification.definition);
			emqLower.setQualified_measure(null, armEntity.getMagnitude(null));
			emqLower.createQualifiers(null).addUnordered(etq);
			emqLower.setName(null, "");
			emqLower.setDescription(null, "");
		}
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
		if(armEntity.testMagnitude(null)){
			EMeasure_with_unit emwu = armEntity.getMagnitude(null);
			CxPlus_minus_bounds.cleanupPath(context, emwu, EType_qualifier.class);
		}
	}
	
}