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

package jsdai.SMixed_complex_types;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SExtended_measure_representation_xim.CxValue_limit;
import jsdai.SExtended_measure_representation_xim.CxValue_with_tolerances;
import jsdai.SExtended_measure_representation_xim.EValue_with_tolerances;
import jsdai.SQualified_measure_schema.AValue_qualifier;
import jsdai.SQualified_measure_schema.CMeasure_representation_item$qualified_representation_item;
import jsdai.SQualified_measure_schema.EQualified_representation_item;

public class CxResistance_measure_with_unit$value_with_tolerances extends CResistance_measure_with_unit$value_with_tolerances implements EMappedXIMEntity{

	public boolean testQualifiers2(EQualified_representation_item type) throws SdaiException {
		return test_aggregate(a2);
	}
	public AValue_qualifier getQualifiers2(EQualified_representation_item type) throws SdaiException {
		if (a2 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a2;
	}
	public AValue_qualifier createQualifiers(EQualified_representation_item type) throws SdaiException {
		a2 = (AValue_qualifier)create_aggregate_class(a2, a2$, AValue_qualifier.class, 0);
		return a2;
	}
	public void unsetQualifiers(EQualified_representation_item type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeQualifiers(EQualified_representation_item type) throws SdaiException {
		return a2$;
	}
	// ENDOF Methods from CQualified_representation_item	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMeasure_representation_item$qualified_representation_item$resistance_measure_with_unit.definition);

			setMappingConstraints(context, this);

			// values
			setLower_limit(context, this);
			
			setUpper_limit(context, this);

			// Clean ARM specific attributes
			unsetLower_limit(null);
			unsetUpper_limit(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			// values
			unsetLower_limit(context, this);
			
			unsetUpper_limit(context, this);
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
			CResistance_measure_with_unit$value_with_tolerances armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxValue_with_tolerances.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CResistance_measure_with_unit$value_with_tolerances armEntity) throws SdaiException {
		CxValue_with_tolerances.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for Lower_limit.
	 */
	public static void setLower_limit(SdaiContext context,
			EValue_with_tolerances armEntity) throws SdaiException {
		CxValue_with_tolerances.setLower_limit(context, armEntity);
	}

	/**
	 * unSets/deletes data for Lower_limit.
	 */
	public static void unsetLower_limit(SdaiContext context,
			EValue_with_tolerances armEntity) throws SdaiException {
		CxValue_with_tolerances.unsetLower_limit(context, armEntity);
	}

	/**
	 * Sets/creates data for Upper_limit.
	 */
	public static void setUpper_limit(SdaiContext context,
			EValue_with_tolerances armEntity) throws SdaiException {
		CxValue_with_tolerances.setUpper_limit(context, armEntity);
	}

	/**
	 * unSets/deletes data for Upper_limit.
	 */
	public static void unsetUpper_limit(SdaiContext context,
			EValue_with_tolerances armEntity) throws SdaiException {
		CxValue_with_tolerances.unsetUpper_limit(context, armEntity);
	}
	
}