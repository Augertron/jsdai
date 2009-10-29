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

package jsdai.SExtended_measure_representation_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SQualified_measure_schema.*;

public class CxMeasure_item_with_precision extends CMeasure_item_with_precision implements EMappedXIMEntity{

	// Taken from CQualified_representation_item
	//methods for attribute: qualifiers, base type: SET OF SELECT
/*	public static int usedinQualifiers(EQualified_representation_item type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testQualifiers(EQualified_representation_item type) throws SdaiException {
		return test_aggregate(a3);
	}
	public AValue_qualifier getQualifiers(EQualified_representation_item type) throws SdaiException {
		if (a3 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a3;
	}*/
	public AValue_qualifier createQualifiers(EQualified_representation_item type) throws SdaiException {
		a3 = (AValue_qualifier)create_aggregate_class(a3, a3$, AValue_qualifier.class, 0);
		return a3;
	}
	public void unsetQualifiers(EQualified_representation_item type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeQualifiers(EQualified_representation_item type) throws SdaiException {
		return a3$;
	}
	// ENDOF Taken from CQualified_representation_item
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMeasure_representation_item$qualified_representation_item.definition);
		
		setMappingConstraints(context, this);
		
		setSignificant_digits(context, this);
		
		unsetSignificant_digits(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetSignificant_digits(context, this);		
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EMeasure_item_with_precision armEntity) throws SdaiException {
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
			EMeasure_item_with_precision armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for mapping constraints.
		attribute_mapping significant_digits(significant_digits, precision_qualifier.precision_value);
			qualified_representation_item 
			qualified_representation_item.qualifiers[i] ->
			value_qualifier = precision_qualifier
			precision_qualifier.precision_value
		end_attribute_mapping;
 	 * <p>
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setSignificant_digits(SdaiContext context,
			EMeasure_item_with_precision armEntity) throws SdaiException {
		unsetSignificant_digits(context, armEntity);
		if(armEntity.testSignificant_digits(null)){
			int digits = armEntity.getSignificant_digits(null);
			// PQ
            LangUtils.Attribute_and_value_structure[] pqStructure = {
            		new LangUtils.Attribute_and_value_structure(
                    CPrecision_qualifier.attributePrecision_value(null), new Integer(digits))};
            EPrecision_qualifier epq = (EPrecision_qualifier)
            	LangUtils.createInstanceIfNeeded(context,
                    CPrecision_qualifier.definition,
                    pqStructure);
			AValue_qualifier qualifiers = armEntity.createQualifiers(null);
            qualifiers.addUnordered(epq);
		}
	}
	
	public static void unsetSignificant_digits(SdaiContext context,
			EMeasure_item_with_precision armEntity) throws SdaiException {
		armEntity.unsetQualifiers(null);
	}	
}