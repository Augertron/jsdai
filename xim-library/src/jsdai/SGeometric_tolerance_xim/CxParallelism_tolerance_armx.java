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

import jsdai.SAic_geometric_tolerances.CParallelism_tolerance;
import jsdai.SMixed_complex_types.CGeometric_tolerance_with_defined_unit$modified_geometric_tolerance$parallelism_tolerance;
import jsdai.SMixed_complex_types.CGeometric_tolerance_with_defined_unit$parallelism_tolerance;
import jsdai.SMixed_complex_types.CModified_geometric_tolerance$parallelism_tolerance;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_with_defined_unit;
import jsdai.SShape_tolerance_schema.EModified_geometric_tolerance;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxParallelism_tolerance_armx extends CParallelism_tolerance_armx implements EMappedXIMEntity
{

	/* Taken from EGeometric_tolerance_with_defined_unit */
	//going through all the attributes: #5629499534230365=EXPLICIT_ATTRIBUTE('unit_size',#5629499534230363,0,#5629499534230845,$,.F.);
	//<01> generating methods for consolidated attribute:  unit_size
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : unit_size, base type: entity measure_with_unit
/*	public static int usedinUnit_size(EGeometric_tolerance_with_defined_unit type, jsdai.SMeasure_schema.EMeasure_with_unit instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a5$, domain, result);
	}
	public boolean testUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		return test_instance(a5);
	}
	public jsdai.SMeasure_schema.EMeasure_with_unit getUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		return (jsdai.SMeasure_schema.EMeasure_with_unit)get_instance(a5);
	}*/
	public void setUnit_size(EGeometric_tolerance_with_defined_unit type, jsdai.SMeasure_schema.EMeasure_with_unit value) throws SdaiException {
		a5 = set_instance(a5, value);
	}
	public void unsetUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		a5 = unset_instance(a5);
	}
	public static jsdai.dictionary.EAttribute attributeUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		return a5$;
	}
	
	/* Taken from EModified_geometric_tolerance */
	//going through all the attributes: #5629499534230368=EXPLICIT_ATTRIBUTE('modifier',#5629499534230366,0,#5629499534230212,$,.F.);
	//<01> generating methods for consolidated attribute:  modifier
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute:modifier, base type: ENUMERATION
/*	public boolean testModifier(EModified_geometric_tolerance type) throws SdaiException {
		return test_enumeration(a6);
	}
	public int getModifier(EModified_geometric_tolerance type) throws SdaiException {
		return get_enumeration(a6);
	}*/
	public void setModifier(EModified_geometric_tolerance type, int value) throws SdaiException {
		a6 = set_enumeration(value, a6$);
	}
	public void unsetModifier(EModified_geometric_tolerance type) throws SdaiException {
		a6 = unset_enumeration();
	}
	public static jsdai.dictionary.EAttribute attributeModifier(EModified_geometric_tolerance type) throws SdaiException {
		return a6$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		if(testModification(null)){
			if(testSegment_size(null)){
				setTemp("AIM", CGeometric_tolerance_with_defined_unit$modified_geometric_tolerance$parallelism_tolerance.definition);
			}else{
				setTemp("AIM", CModified_geometric_tolerance$parallelism_tolerance.definition);
			}
		}else{
			if(testSegment_size(null)){
				setTemp("AIM", CGeometric_tolerance_with_defined_unit$parallelism_tolerance.definition);				
			}else{
				setTemp("AIM", CParallelism_tolerance.definition);
			}
		}

		setMappingConstraints(context, this);

		
		// modification : OPTIONAL limit_condition;
		setModification(context, this);
		
		// segment_size : OPTIONAL measure_with_unit;
		setSegment_size(context, this);

        // significant_digits : OPTIONAL INTEGER;
		setSignificant_digits(context, this);
		
        // value_determination : OPTIONAL STRING;
		setValue_determination(context, this);
		
		setComposer(context, this);
		
		setTolerance_zone_attributes(context, this);
		
		// affected_plane : OPTIONAL axis2_placement;
		// setAffected_plane(context, this);
		
		// clean ARM
        // significant_digits : OPTIONAL INTEGER;
		unsetSignificant_digits(null);
		
        // value_determination : OPTIONAL STRING;
		unsetValue_determination(null);
		
		// modification : OPTIONAL limit_condition;
		unsetModification(null);
		
		// segment_size : OPTIONAL measure_with_unit;
		unsetSegment_size(null);
		
		unsetComposer(null);
		
		// affected_plane : OPTIONAL axis2_placement;
		// unsetAffected_plane(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
	        // significant_digits : OPTIONAL INTEGER;
			unsetSignificant_digits(context, this);
			
	        // value_determination : OPTIONAL STRING;
			unsetValue_determination(context, this);
			
			// modification : OPTIONAL limit_condition;
			unsetModification(context, this);
			
			// segment_size : OPTIONAL measure_with_unit;
			unsetSegment_size(context, this);
			
			unsetComposer(context, this);
			
			unsetTolerance_zone_attributes(context, this);
			
			// affected_plane : OPTIONAL axis2_placement;
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
			EParallelism_tolerance_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxGeometric_tolerance_with_datum_reference_xim.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EParallelism_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_with_datum_reference_xim.unsetMappingConstraints(context, armEntity);
	}

	public static void setTolerance_zone_attributes(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setTolerance_zone_attributes(context, armEntity);
	}

	public static void unsetTolerance_zone_attributes(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetTolerance_zone_attributes(context, armEntity);
	}
	
	/**
	 * Sets/creates data for Significant_digits attribute.
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

	/**
	 * Sets/creates data for Significant_digits attribute.
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
	// GT <- MQ -> PQ 
	public static void setModification(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setModification(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute Significant_digits.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetModification(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetModification(context, armEntity);
	}

	/**
	 * Sets/creates data for Significant_digits attribute.
	 * 
	 * <p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// GT <- MQ -> PQ 
	public static void setSegment_size(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setSegment_size(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute Significant_digits.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetSegment_size(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetSegment_size(context, armEntity);
	}

	/**
	 * Sets/creates data for Affected_plane attribute.
	 * 
	attribute_mapping affected_plane(affected_plane, $PATH, axis2_placement);
		line_profile_tolerance <= 
		geometric_tolerance <-
		tolerance_zone.defining_tolerance[i]
		tolerance_zone <=
		shape_aspect <- 
		shape_aspect_relationship.relating_shape_aspect 
		shape_aspect_relationship 
		{shape_aspect_relationship.name = 'affected plane association'} 
		shape_aspect_relationship.related_shape_aspect -> 
		shape_aspect 
		shape_definition = shape_aspect
		shape_definition characterized_definition = shape_definition 
		characterized_definition <- 
		property_definition.definition
		property_definition 
		represented_definition = property_definition 
		represented_definition <- 
		property_definition_representation.definition 
		property_definition_representation 
		property_definition_representation.used_representation -> 
		representation
		{representation =>
		shape_representation}
		representation.items[i] ->
		representation_item
		representation_item =>
		geometric_representation_item
		geometric_representation_item =>
		placement => 
		(axis2_placement_3d)
	end_attribute_mapping;
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// LPT <- TZ <- SAR -> SA <- PD <- PDR -> SR -> P
	/* As Lothar suggessted - we remove it for now
	public static void setAffected_plane(SdaiContext context, ELine_profile_tolerance_armx armEntity) throws SdaiException {
		if(armEntity.testAffected_plane(null)){
			EPlacement ep = (EPlacement)armEntity.getAffected_plane(null);
			// TZ
			LangUtils.Attribute_and_value_structure[] tzStructure = {
					new LangUtils.Attribute_and_value_structure(
							CTolerance_zone.attributeDefining_tolerance(null), armEntity)
			};
			ETolerance_zone etz = (ETolerance_zone) 
				LangUtils.createInstanceIfNeeded(context,
						CTolerance_zone.definition, tzStructure);
			if(!etz.testForm(null)){
				// TZF
				LangUtils.Attribute_and_value_structure[] tzfStructure = {
						new LangUtils.Attribute_and_value_structure(
								CTolerance_zone_form.attributeName(null), "")
				};
				ETolerance_zone_form etzf = (ETolerance_zone_form) 
					LangUtils.createInstanceIfNeeded(context,
							CTolerance_zone_form.definition, tzfStructure);
				etz.setForm(null, etzf);
			}
			if(!etz.testName(null)){
				etz.setName(null, "");
			}
			if(!etz.testProduct_definitional(null)){
				etz.setProduct_definitional(null, ELogical.UNKNOWN);
			}
			// SR
			ARepresentation ar = new ARepresentation();
			CRepresentation.usedinItems(null, ep, context.domain, ar);
			//...
		}
	}
*/
	/**
	 * Unsets/deletes mapping for attribute Significant_digits.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/*	public static void unsetAffected_plane(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetSegment_size(context, armEntity);
	}
*/
	
	/**
	 * Sets/creates data for composer attribute.
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
	// GT <- MQ -> PQ 
	public static void setComposer(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setComposer(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute composer.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetComposer(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetComposer(context, armEntity);
	}
	
}