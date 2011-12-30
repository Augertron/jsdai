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

import jsdai.SShape_tolerance_schema.ATolerance_zone_target;
import jsdai.SShape_tolerance_schema.CParallelism_tolerance;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_with_defined_area_unit;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_with_defined_unit;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_with_modifiers;
import jsdai.SShape_tolerance_schema.EModified_geometric_tolerance;
import jsdai.SShape_tolerance_schema.ETolerance_zone;
import jsdai.SShape_tolerance_schema.EUnequally_disposed_geometric_tolerance;
import jsdai.lang.A_enumeration;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxParallelism_tolerance_armx extends CParallelism_tolerance_armx implements EMappedXIMEntity
{

	//going through all the attributes: #5629499534230550=EXPLICIT_ATTRIBUTE('modifiers',#5629499534230548,0,#5629499534691833,$,.F.);
	//<01> generating methods for consolidated attribute:  modifiers
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// methods for attribute: modifiers, base type: SET OF ENUMERATION
/*	public boolean testModifiers(EGeometric_tolerance_with_modifiers type) throws SdaiException {
		return test_aggregate(a17);
	}
	public A_enumeration getModifiers(EGeometric_tolerance_with_modifiers type) throws SdaiException {
		return (A_enumeration)get_aggregate(a17);
	}*/
	public A_enumeration createModifiers(EGeometric_tolerance_with_modifiers type) throws SdaiException {
		a17 = create_aggregate_enumeration(a17, a17$, 0);
		return a17;
	}
	public void unsetModifiers(EGeometric_tolerance_with_modifiers type) throws SdaiException {
		unset_aggregate(a17);
		a17 = null;
	}
	public static jsdai.dictionary.EAttribute attributeModifiers(EGeometric_tolerance_with_modifiers type) throws SdaiException {
		return a17$;
	}
	
	//going through all the attributes: #5629499534230612=EXPLICIT_ATTRIBUTE('defining_tolerance',#5629499534230610,0,#5629499534691835,$,.F.);
	//<01> generating methods for consolidated attribute:  defining_tolerance
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// methods for attribute: defining_tolerance, base type: SET OF SELECT
/*	public static int usedinDefining_tolerance(ETolerance_zone type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a13$, domain, result);
	}
	public boolean testDefining_tolerance(ETolerance_zone type) throws SdaiException {
		return test_aggregate(a13);
	}
	public ATolerance_zone_target getDefining_tolerance(ETolerance_zone type) throws SdaiException {
		if (a13 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a13;
	}*/
	public ATolerance_zone_target createDefining_tolerance(ETolerance_zone type) throws SdaiException {
		a13 = (ATolerance_zone_target)create_aggregate_class(a13, a13$, ATolerance_zone_target.class, 0);
		return a13;
	}
	public void unsetDefining_tolerance(ETolerance_zone type) throws SdaiException {
		unset_aggregate(a13);
		a13 = null;
	}
	public static jsdai.dictionary.EAttribute attributeDefining_tolerance(ETolerance_zone type) throws SdaiException {
		return a13$;
	}
	
	//going through all the attributes: #5629499534230540=EXPLICIT_ATTRIBUTE('area_type',#5629499534230538,0,#5629499534230490,$,.F.);
	//<01> generating methods for consolidated attribute:  area_type
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute:area_type, base type: ENUMERATION
/*	public boolean testArea_type(EGeometric_tolerance_with_defined_area_unit type) throws SdaiException {
		return test_enumeration(a6);
	}
	public int getArea_type(EGeometric_tolerance_with_defined_area_unit type) throws SdaiException {
		return get_enumeration(a6);
	}*/
	public void setArea_type(EGeometric_tolerance_with_defined_area_unit type, int value) throws SdaiException {
		a6 = set_enumeration(value, a6$);
	}
	public void unsetArea_type(EGeometric_tolerance_with_defined_area_unit type) throws SdaiException {
		a6 = unset_enumeration();
	}
	public static jsdai.dictionary.EAttribute attributeArea_type(EGeometric_tolerance_with_defined_area_unit type) throws SdaiException {
		return a6$;
	}
	
	//going through all the attributes: #5629499534230625=EXPLICIT_ATTRIBUTE('displacement',#5629499534230623,0,#5629499534231041,$,.F.);
	//<01> generating methods for consolidated attribute:  displacement
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : displacement, base type: entity length_measure_with_unit
/*	public static int usedinDisplacement(EUnequally_disposed_geometric_tolerance type, jsdai.SMeasure_schema.ELength_measure_with_unit instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a18$, domain, result);
	}
	public boolean testDisplacement(EUnequally_disposed_geometric_tolerance type) throws SdaiException {
		return test_instance(a18);
	}
	public jsdai.SMeasure_schema.ELength_measure_with_unit getDisplacement(EUnequally_disposed_geometric_tolerance type) throws SdaiException {
		return (jsdai.SMeasure_schema.ELength_measure_with_unit)get_instance(a18);
	}*/
	public void setDisplacement(EUnequally_disposed_geometric_tolerance type, jsdai.SMeasure_schema.ELength_measure_with_unit value) throws SdaiException {
		a18 = set_instance(a18, value);
	}
	public void unsetDisplacement(EUnequally_disposed_geometric_tolerance type) throws SdaiException {
		a18 = unset_instance(a18);
	}
	public static jsdai.dictionary.EAttribute attributeDisplacement(EUnequally_disposed_geometric_tolerance type) throws SdaiException {
		return a18$;
	}
	
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
		return test_enumeration(a8);
	}
	public int getModifier(EModified_geometric_tolerance type) throws SdaiException {
		return get_enumeration(a8);
	}*/
	public void setModifier(EModified_geometric_tolerance type, int value) throws SdaiException {
		a8 = set_enumeration(value, a8$);
	}
	public void unsetModifier(EModified_geometric_tolerance type) throws SdaiException {
		a8 = unset_enumeration();
	}
	public static jsdai.dictionary.EAttribute attributeModifier(EModified_geometric_tolerance type) throws SdaiException {
		return a8$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		CxGeometric_tolerance_armx.setType(context, this, CParallelism_tolerance.definition);
		
		setMappingConstraints(context, this);

		
		// modification : OPTIONAL limit_condition;
		setModification(context, this);
		
		// modification_new : OPTIONAL SET [1:?] OF geometric_tolerance_modifier;
		setModification_new(context, this);
		
		// unequally_disposed_tolerance_zone_displacement : OPTIONAL length_measure_with_unit;
		setUnequally_disposed_tolerance_zone_displacement(context, this);
		
		// Area_unit_type : OPTIONAL area_unit_type;
		setArea_unit_type(context, this);
		
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
		
		// modification_new : OPTIONAL SET [1:?] OF geometric_tolerance_modifier;
		unsetModification_new(null);
		
		// unequally_disposed_tolerance_zone_displacement : OPTIONAL length_measure_with_unit;
		unsetUnequally_disposed_tolerance_zone_displacement(null);
		
		// Area_unit_type : OPTIONAL area_unit_type;
		unsetArea_unit_type(null);
		
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
			
			// modification_new : OPTIONAL SET [1:?] OF geometric_tolerance_modifier;
			unsetModification_new(context, this);
			
			// unequally_disposed_tolerance_zone_displacement : OPTIONAL length_measure_with_unit;
			unsetUnequally_disposed_tolerance_zone_displacement(context, this);
			
			// Area_unit_type : OPTIONAL area_unit_type;
			unsetArea_unit_type(context, this);
			
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
	 * Sets/creates data for unequally_disposed_tolerance_zone_displacement attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setUnequally_disposed_tolerance_zone_displacement(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setUnequally_disposed_tolerance_zone_displacement(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute unequally_disposed_tolerance_zone_displacement.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetUnequally_disposed_tolerance_zone_displacement(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetUnequally_disposed_tolerance_zone_displacement(context, armEntity);
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
	 * Sets/creates data for modification_new attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setModification_new(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setModification_new(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute modification_new.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetModification_new(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetModification_new(context, armEntity);
	}

	/**
	 * Sets/creates data for Area_unit_type attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setArea_unit_type(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.setArea_unit_type(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute Area_unit_type.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetArea_unit_type(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		CxGeometric_tolerance_armx.unsetArea_unit_type(context, armEntity);
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