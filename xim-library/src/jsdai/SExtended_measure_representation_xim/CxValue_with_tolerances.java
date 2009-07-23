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
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SQualified_measure_schema.*;
import jsdai.SMixed_complex_types.*;

public class CxValue_with_tolerances extends CValue_with_tolerances implements EMappedXIMEntity{

	// Methods from CQualified_representation_item
	// methods for attribute: qualifiers, base type: SET OF SELECT
/*	public static int usedinQualifiers(EQualified_representation_item type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}*/
	private boolean testQualifiers2(EQualified_representation_item type) throws SdaiException {
		return test_aggregate(a1);
	}
	private AValue_qualifier getQualifiers2(EQualified_representation_item type) throws SdaiException {
		if (a1 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a1;
	}
	public AValue_qualifier createQualifiers(EQualified_representation_item type) throws SdaiException {
		a1 = (AValue_qualifier)create_aggregate_class(a1, a1$, AValue_qualifier.class, 0);
		return a1;
	}
	public void unsetQualifiers(EQualified_representation_item type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeQualifiers(EQualified_representation_item type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF Methods from CQualified_representation_item	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMeasure_representation_item$qualified_representation_item.definition);

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
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			// Lower_limit
			unsetLower_limit(context, this);
			
			// Upper_limit
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
			EValue_with_tolerances armEntity) throws SdaiException {
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
			EValue_with_tolerances armEntity) throws SdaiException {
	}


	/**
	 * Sets/creates data for environment_characterization attribute.
	attribute_mapping lower_limit(lower_limit, qualitative_uncertainty.uncertainty_value);
		qualified_representation_item 
		qualified_representation_item.qualifiers[i] ->
		value_qualifier = uncertainty_qualifier
		{uncertainty_qualifier.measure_name='lower limit'}
		uncertainty_qualifier =>
		standard_uncertainty
		standard_uncertainty.uncertainty_value
	end_attribute_mapping;
 	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// QRI -> SU
	public static void setLower_limit(SdaiContext context, EValue_with_tolerances armEntity) throws SdaiException {
		unsetLower_limit(context, armEntity);
		if(armEntity.testLower_limit(null)){
			double value = armEntity.getLower_limit(null);
			EStandard_uncertainty esu = (EStandard_uncertainty)
				context.working_model.createEntityInstance(CStandard_uncertainty.definition);
			esu.setMeasure_name(null, "lower limit");
			esu.setUncertainty_value(null, value);
			esu.setDescription(null, "");
			fillQualifiers(armEntity, esu);
		}
	}
	private static void fillQualifiers(EValue_with_tolerances armEntity,
			EStandard_uncertainty esu) throws SdaiException {
		AValue_qualifier qualifiers;
		if(armEntity instanceof CxValue_with_tolerances){
			CxValue_with_tolerances cxEntity = (CxValue_with_tolerances)armEntity;
			if(cxEntity.testQualifiers2(null)){
				qualifiers = cxEntity.getQualifiers2(null);
			}else{
				qualifiers = cxEntity.createQualifiers(null);
			}
		}else if(armEntity instanceof CxResistance_measure_with_unit$value_with_tolerances){
			CxResistance_measure_with_unit$value_with_tolerances cxEntity = (CxResistance_measure_with_unit$value_with_tolerances)armEntity;
			if(cxEntity.testQualifiers2(null)){
				qualifiers = cxEntity.getQualifiers2(null);
			}else{
				qualifiers = cxEntity.createQualifiers(null);
			}
		}else{
			SdaiSession.getSession().printlnSession("Not supported subtype of value_with_tolerances "+armEntity);
			return;
		}
		qualifiers.addUnordered(esu);
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetLower_limit(SdaiContext context,
			EValue_with_tolerances armEntity) throws SdaiException {
		AValue_qualifier qualifiers = getQualifiers(armEntity);
		if(qualifiers == null){
			return;
		}

		for(int i=1; i<=qualifiers.getMemberCount();){
			EEntity qualifier = qualifiers.getByIndex(i);
			if(qualifier instanceof EStandard_uncertainty){
				EStandard_uncertainty esu = (EStandard_uncertainty)qualifier;
				if((esu.testMeasure_name(null))&&
					(esu.getMeasure_name(null).equals("lower limit"))){
					qualifiers.removeUnordered(esu);
					LangUtils.deleteInstanceIfUnused(context.domain, esu);
					continue;
				}
			}
			i++;
		}
	}
	private static AValue_qualifier getQualifiers(
			EValue_with_tolerances armEntity)
			throws SdaiException {
		AValue_qualifier qualifiers = null;		
		if(armEntity instanceof CxValue_with_tolerances){
			CxValue_with_tolerances cxEntity = (CxValue_with_tolerances)armEntity;
			if(!cxEntity.testQualifiers2(null)){
				return null;
			}
			qualifiers = cxEntity.getQualifiers2(null);
		}else if(armEntity instanceof CxResistance_measure_with_unit$value_with_tolerances){
			CxResistance_measure_with_unit$value_with_tolerances cxEntity = (CxResistance_measure_with_unit$value_with_tolerances)armEntity;
			if(!cxEntity.testQualifiers2(null)){
				return null;
			}
			qualifiers = cxEntity.getQualifiers2(null);
		}
		return qualifiers;
	}

	/**
	 * Sets/creates data for environment_characterization attribute.
	attribute_mapping upper_limit(upper_limit, qualitative_uncertainty.uncertainty_value);
		qualified_representation_item 
		qualified_representation_item.qualifiers[i] ->
		value_qualifier = uncertainty_qualifier
		{uncertainty_qualifier.measure_name='upper limit'}
		uncertainty_qualifier =>
		standard_uncertainty
		standard_uncertainty.uncertainty_value
	end_attribute_mapping;
 	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// QRI -> SU
	public static void setUpper_limit(SdaiContext context, EValue_with_tolerances armEntity) throws SdaiException {
		unsetUpper_limit(context, armEntity);
		if(armEntity.testUpper_limit(null)){
			double value = armEntity.getUpper_limit(null);
			EStandard_uncertainty esu = (EStandard_uncertainty)
				context.working_model.createEntityInstance(CStandard_uncertainty.definition);
			esu.setMeasure_name(null, "upper limit");
			esu.setUncertainty_value(null, value);
			esu.setDescription(null, "");
			fillQualifiers(armEntity, esu);
		}
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetUpper_limit(SdaiContext context,
			EValue_with_tolerances armEntity) throws SdaiException {
		AValue_qualifier qualifiers = getQualifiers(armEntity);
		if(qualifiers == null){
			return;
		}
		for(int i=1; i<=qualifiers.getMemberCount();){
			EEntity qualifier = qualifiers.getByIndex(i);
			if(qualifier instanceof EStandard_uncertainty){
				EStandard_uncertainty esu = (EStandard_uncertainty)qualifier;
				if((esu.testMeasure_name(null))&&
					(esu.getMeasure_name(null).equals("upper limit"))){
					qualifiers.removeUnordered(esu);
					LangUtils.deleteInstanceIfUnused(context.domain, esu);
					continue;
				}
			}
			i++;
		}
	}
	
}