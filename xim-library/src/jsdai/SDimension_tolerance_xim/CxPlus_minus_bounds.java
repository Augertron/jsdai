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

package jsdai.SDimension_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SMeasure_schema.EMeasure_with_unit;
import jsdai.SQualified_measure_schema.AMeasure_qualification;
import jsdai.SQualified_measure_schema.AValue_qualifier;
import jsdai.SQualified_measure_schema.CMeasure_qualification;
import jsdai.SQualified_measure_schema.CPrecision_qualifier;
import jsdai.SQualified_measure_schema.CType_qualifier;
import jsdai.SQualified_measure_schema.EMeasure_qualification;
import jsdai.SQualified_measure_schema.EPrecision_qualifier;
import jsdai.SQualified_measure_schema.EType_qualifier;
import jsdai.SShape_tolerance_schema.CTolerance_value;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxPlus_minus_bounds extends CPlus_minus_bounds implements EMappedXIMEntity
{
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CTolerance_value.definition);

		setMappingConstraints(context, this);

        // significant_digits : OPTIONAL INTEGER;
		setSignificant_digits(context, this);
		
        // value_determination : OPTIONAL STRING;		
		setValue_determination(context, this);

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
			EPlus_minus_bounds armEntity) throws SdaiException {
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
			EPlus_minus_bounds armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for significant_digits attribute.
	 * 
	 * <p>
	attribute_mapping significant_digits(significant_digits, precision_qualifier.precision_value);
		[tolerance_value
		tolerance_value.lower_bound ->
		measure_with_unit <-]
		[tolerance_value
		tolerance_value.upper_bound ->
		measure_with_unit <-]
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
	// PMT -> DS/DC <- DCR -> SDR -> MRI 
	public static void setSignificant_digits(SdaiContext context,
			EPlus_minus_bounds armEntity) throws SdaiException {
		unsetSignificant_digits(context, armEntity);
		if(armEntity.testSignificant_digits(null)){
			int value = armEntity.getSignificant_digits(null);
			if((!armEntity.testLower_bound(null))||(!armEntity.testUpper_bound(null))){
				SdaiSession.println(" Lower and upper bounds must be set for "+armEntity);
				return;
			}
			EMeasure_with_unit lowerBound = armEntity.getLower_bound(null);
			EMeasure_with_unit upperBound = armEntity.getUpper_bound(null);
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
			emqLower.setQualified_measure(null, lowerBound);
			emqLower.createQualifiers(null).addUnordered(epq);
			emqLower.setName(null, "");
			emqLower.setDescription(null, "");
			// MQ-Upper
			EMeasure_qualification emqUpper = (EMeasure_qualification)
				context.working_model.createEntityInstance(CMeasure_qualification.definition);
			emqUpper.setQualified_measure(null, upperBound);
			emqUpper.createQualifiers(null).addUnordered(epq);
			emqUpper.setName(null, "");
			emqUpper.setDescription(null, "");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetSignificant_digits(SdaiContext context,
			EPlus_minus_bounds armEntity) throws SdaiException {
		if(armEntity.testLower_bound(null)){
			EMeasure_with_unit emwu = armEntity.getLower_bound(null);
			cleanupPath(context, emwu, EPrecision_qualifier.class);
		}
		if(armEntity.testUpper_bound(null)){
			EMeasure_with_unit emwu = armEntity.getUpper_bound(null);
			cleanupPath(context, emwu, EPrecision_qualifier.class);
		}
	}

	public static void cleanupPath(SdaiContext context, EMeasure_with_unit emwu, Class type)
			throws SdaiException {
		AMeasure_qualification amq = new AMeasure_qualification();
		CMeasure_qualification.usedinQualified_measure(null, emwu, context.domain, amq);
		for(int i=1,n=amq.getMemberCount(); i<=n; i++){
			EMeasure_qualification emq = amq.getByIndex(i);
			if(emq.testQualifiers(null)){
				AValue_qualifier elements = emq.getQualifiers(null);
				if((elements.getMemberCount() == 1)&&
					(elements.getByIndex(1).isInstanceOf(type))){
					emq.deleteApplicationInstance();		
				}else{
					for(int j=1,count=elements.getMemberCount(); j<=count;){
						EEntity ee = elements.getByIndex(j);
						if(ee.isInstanceOf(type)){
							elements.removeByIndex(j);
						}else{
							j++;
						}
					}
				}
			}
		}
	}

	/**
	 * Sets/creates data for value_determination attribute.
	 * 
	 * <p>
	attribute_mapping value_determination(value_determination, type_qualifier.name);
		tolerance_value
		[tolerance_value.lower_bound ->]
		[tolerance_value.upper_bound ->] 
		measure_with_unit <- 
		measure_qualification.qualified_measure measure_qualification 
		measure_qualification.qualifiers[i] -> 
		value_qualifier value_qualifier = type_qualifier type_qualifier 
		type_qualifier.name
		{(type_qualifier.name)
		(type_qualifier.name = 'required')
		(type_qualifier.name = 'designed')
		(type_qualifier.name = 'calculated')
		(type_qualifier.name = 'measured')
		(type_qualifier.name = 'estimated')}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PMT -> DS/DC <- DCR -> SDR -> MRI 
	public static void setValue_determination(SdaiContext context,
			EPlus_minus_bounds armEntity) throws SdaiException {
		unsetValue_determination(context, armEntity);
		if(armEntity.testValue_determination(null)){
			String value = armEntity.getValue_determination(null);
			if((!armEntity.testLower_bound(null))||(!armEntity.testUpper_bound(null))){
				SdaiSession.println(" Lower and upper bounds must be set for "+armEntity);
				return;
			}
			EMeasure_with_unit lowerBound = armEntity.getLower_bound(null);
			EMeasure_with_unit upperBound = armEntity.getUpper_bound(null);
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
			emqLower.setQualified_measure(null, lowerBound);
			emqLower.createQualifiers(null).addUnordered(etq);
			emqLower.setName(null, "");
			emqLower.setDescription(null, "");
			// MQ-Upper
			EMeasure_qualification emqUpper = (EMeasure_qualification)
				context.working_model.createEntityInstance(CMeasure_qualification.definition);
			emqUpper.setQualified_measure(null, upperBound);
			emqUpper.createQualifiers(null).addUnordered(etq);
			emqUpper.setName(null, "");
			emqUpper.setDescription(null, "");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetValue_determination(SdaiContext context,
			EPlus_minus_bounds armEntity) throws SdaiException {
		if(armEntity.testLower_bound(null)){
			EMeasure_with_unit emwu = armEntity.getLower_bound(null);
			cleanupPath(context, emwu, EType_qualifier.class);
		}
		if(armEntity.testUpper_bound(null)){
			EMeasure_with_unit emwu = armEntity.getUpper_bound(null);
			cleanupPath(context, emwu, EType_qualifier.class);
		}
	}

}