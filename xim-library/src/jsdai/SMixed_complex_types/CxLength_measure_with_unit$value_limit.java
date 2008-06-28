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

public class CxLength_measure_with_unit$value_limit extends CLength_measure_with_unit$value_limit implements EMappedXIMEntity{

	// Methods from CQualified_representation_item
	// methods for attribute: qualifiers, base type: SET OF SELECT
/*	public static int usedinQualifiers(EQualified_representation_item type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testQualifiers(EQualified_representation_item type) throws SdaiException {
		return test_aggregate(a2);
	}
	public AValue_qualifier getQualifiers(EQualified_representation_item type) throws SdaiException {
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
*/	
	// ENDOF Methods from CQualified_representation_item	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLength_measure_with_unit$measure_representation_item$qualified_representation_item.definition);

			setMappingConstraints(context, this);

			// values
			// setLimit_qualifier(context, this);

			// Clean ARM specific attributes
			// unsetLimit_qualifier(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//environment_characterization
			// unsetLimit_qualifier(context, this);

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
			CLength_measure_with_unit$value_limit armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxValue_limit.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CLength_measure_with_unit$value_limit armEntity) throws SdaiException {
		CxValue_limit.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for environment_characterization attribute.
		attribute_mapping limit_qualifier(limit_qualifier, type_qualifier.name
		);
			qualified_representation_item 
     		qualified_representation_item.qualifiers[i] -> 
     		value_qualifier = type_qualifier 
     		type_qualifier
     		{(type_qualifier.name = 'maximum') 
     		(type_qualifier.name = 'minimum')}
		end_attribute_mapping;
 	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/*	
	public static void setLimit_qualifier(SdaiContext context, CLength_measure_with_unit$value_limit armEntity) throws SdaiException {
		CxValue_limit.setLimit_qualifier(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/*	
	public static void unsetLimit_qualifier(SdaiContext context, CLength_measure_with_unit$value_limit armEntity) throws SdaiException {
		CxValue_limit.unsetLimit_qualifier(context, armEntity);		
	}
*/
}