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
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SExtended_measure_representation_xim.CxMeasure_item_with_precision;
import jsdai.SExtended_measure_representation_xim.EMeasure_item_with_precision;
import jsdai.SQualified_measure_schema.AValue_qualifier;
import jsdai.SQualified_measure_schema.EQualified_representation_item;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;

public class CxLength_measure_with_unit$measure_item_with_precision extends CLength_measure_with_unit$measure_item_with_precision implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	

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
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CLength_measure_with_unit$measure_representation_item$qualified_representation_item.definition);

		setMappingConstraints(context, this);

		setSignificant_digits(context, this);
		
		unsetSignificant_digits(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetSignificant_digits(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	 product_definition_relationship
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CLength_measure_with_unit$measure_item_with_precision armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxMeasure_item_with_precision.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CLength_measure_with_unit$measure_item_with_precision armEntity) throws SdaiException
	{
		CxMeasure_item_with_precision.unsetMappingConstraints(context, armEntity);
	}
	
	
	//********** "managed_design_object" attributes

	public static void setSignificant_digits(SdaiContext context, EMeasure_item_with_precision armEntity) throws SdaiException{
		CxMeasure_item_with_precision.setSignificant_digits(context, armEntity);
	}
	
	public static void unsetSignificant_digits(SdaiContext context, EMeasure_item_with_precision armEntity) throws SdaiException{
		CxMeasure_item_with_precision.unsetSignificant_digits(context, armEntity);
	}

}
