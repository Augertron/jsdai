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

import jsdai.SMeasure_schema.EMeasure_with_unit;
import jsdai.SProduct_property_definition_schema.AShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.SQualified_measure_schema.AMeasure_qualification;
import jsdai.SQualified_measure_schema.AValue_qualifier;
import jsdai.SQualified_measure_schema.CMeasure_qualification;
import jsdai.SQualified_measure_schema.CPrecision_qualifier;
import jsdai.SQualified_measure_schema.CType_qualifier;
import jsdai.SQualified_measure_schema.EMeasure_qualification;
import jsdai.SQualified_measure_schema.EPrecision_qualifier;
import jsdai.SQualified_measure_schema.EType_qualifier;
import jsdai.SShape_tolerance_schema.AGeometric_tolerance_relationship;
import jsdai.SShape_tolerance_schema.ATolerance_zone;
import jsdai.SShape_tolerance_schema.ATolerance_zone_definition;
import jsdai.SShape_tolerance_schema.CGeometric_tolerance;
import jsdai.SShape_tolerance_schema.CGeometric_tolerance_relationship;
import jsdai.SShape_tolerance_schema.CGeometric_tolerance_with_defined_unit;
import jsdai.SShape_tolerance_schema.CGeometric_tolerance_with_defined_unit$modified_geometric_tolerance;
import jsdai.SShape_tolerance_schema.CModified_geometric_tolerance;
import jsdai.SShape_tolerance_schema.CTolerance_zone;
import jsdai.SShape_tolerance_schema.CTolerance_zone_definition;
import jsdai.SShape_tolerance_schema.CTolerance_zone_form;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_relationship;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_with_defined_unit;
import jsdai.SShape_tolerance_schema.EModified_geometric_tolerance;
import jsdai.SShape_tolerance_schema.ETolerance_zone;
import jsdai.SShape_tolerance_schema.ETolerance_zone_definition;
import jsdai.SShape_tolerance_schema.ETolerance_zone_form;
import jsdai.lang.EEntity;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxGeometric_tolerance_armx extends CGeometric_tolerance_armx implements EMappedXIMEntity
{

	/* Taken from EGeometric_tolerance_with_defined_unit */
	//going through all the attributes: #5629499534230365=EXPLICIT_ATTRIBUTE('unit_size',#5629499534230363,0,#5629499534230845,$,.F.);
	//<01> generating methods for consolidated attribute:  unit_size
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : unit_size, base type: entity measure_with_unit
/*	public static int usedinUnit_size(EGeometric_tolerance_with_defined_unit type, jsdai.SMeasure_schema.EMeasure_with_unit instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		return test_instance(a4);
	}
	public jsdai.SMeasure_schema.EMeasure_with_unit getUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		return (jsdai.SMeasure_schema.EMeasure_with_unit)get_instance(a4);
	}*/
	public void setUnit_size(EGeometric_tolerance_with_defined_unit type, jsdai.SMeasure_schema.EMeasure_with_unit value) throws SdaiException {
		a4 = set_instance(a4, value);
	}
	public void unsetUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributeUnit_size(EGeometric_tolerance_with_defined_unit type) throws SdaiException {
		return a4$;
	}
	
	/* Taken from EModified_geometric_tolerance */
	//going through all the attributes: #5629499534230368=EXPLICIT_ATTRIBUTE('modifier',#5629499534230366,0,#5629499534230212,$,.F.);
	//<01> generating methods for consolidated attribute:  modifier
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute:modifier, base type: ENUMERATION
/*	public boolean testModifier(EModified_geometric_tolerance type) throws SdaiException {
		return test_enumeration(a5);
	}
	public int getModifier(EModified_geometric_tolerance type) throws SdaiException {
		return get_enumeration(a5);
	}*/
	public void setModifier(EModified_geometric_tolerance type, int value) throws SdaiException {
		a5 = set_enumeration(value, a5$);
	}
	public void unsetModifier(EModified_geometric_tolerance type) throws SdaiException {
		a5 = unset_enumeration();
	}
	public static jsdai.dictionary.EAttribute attributeModifier(EModified_geometric_tolerance type) throws SdaiException {
		return a5$;
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
				setTemp("AIM", CGeometric_tolerance_with_defined_unit$modified_geometric_tolerance.definition);
			}else{
				setTemp("AIM", CModified_geometric_tolerance.definition);
			}
		}else{
			if(testSegment_size(null)){
				setTemp("AIM", CGeometric_tolerance_with_defined_unit.definition);				
			}else{
				setTemp("AIM", CGeometric_tolerance.definition);
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
		
		// composer : OPTIONAL Geometric_tolerance_armx;
		setComposer(context, this);
		
		setTolerance_zone_attributes(context, this);
		
		// clean ARM
        // significant_digits : OPTIONAL INTEGER;
		unsetSignificant_digits(null);
		
        // value_determination : OPTIONAL STRING;
		unsetValue_determination(null);
		
		// modification : OPTIONAL limit_condition;
		unsetModification(null);
		
		// segment_size : OPTIONAL measure_with_unit;
		unsetSegment_size(null);
		
		// composer : OPTIONAL Geometric_tolerance_armx;
		unsetComposer(null);
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
			
			// composer : OPTIONAL Geometric_tolerance_armx;
			unsetComposer(context, this);
			
			unsetTolerance_zone_attributes(context, this);			
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
//		CxTolerance_zone_armx.setMappingConstraints(context, armEntity);
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
//		CxTolerance_zone_armx.unsetMappingConstraints(context, armEntity);
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
			cleanupPath(context, emwu, EPrecision_qualifier.class);
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
			cleanupPath(context, emwu, EType_qualifier.class);
		}
	}

	/**
	 * Sets/creates data for Significant_digits attribute.
	 * 
	 * <p>
	attribute_mapping modification(modification, $PATH, limit_condition);
		geometric_tolerance => 
		modified_geometric_tolerance 
		modified_geometric_tolerance.modifier ->
		limit_condition
		(limit_condition = .MAXIMUM_MATERIAL_CONDITION.)
		(limit_condition = .LEAST_MATERIAL_CONDITION.)
		(limit_condition = .REGARDLESS_OF_FEATURE_SIZE.)
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
	public static void setModification(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		unsetModification(context, armEntity);
		if(armEntity.testModification(null)){
			int value = armEntity.getModification(null);
			armEntity.setModifier(null, value);
		}
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
		armEntity.unsetModifier(null);
	}

	/**
	 * Sets/creates data for Significant_digits attribute.
	 * 
	 * <p>
	attribute_mapping segment_size(segment_size, $PATH, measure_with_unit);
		geometric_tolerance => 
		geometric_tolerance_with_defined_unit 
		geometric_tolerance_with_defined_unit.unit_size -> 
		measure_with_unit =>
		length_measure_with_unit
	end_attribute_mapping;	 * </p>
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
		unsetSegment_size(context, armEntity);
		if(armEntity.testSegment_size(null)){
			EMeasure_with_unit value = armEntity.getSegment_size(null);
			armEntity.setUnit_size(null, value);
		}
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
		armEntity.unsetUnit_size(null);
	}

	/**
	 * Sets/creates data for Significant_digits attribute.
	 * 
	 * <p>
	attribute_mapping composer(composer, $PATH, Geometric_tolerance_armx);
		geometric_tolerance <-
		geometric_tolerance_relationship.relating_geometric_tolerance
		geometric_tolerance_relationship
		{geometric_tolerance_relationship.name = 'composition'}
		geometric_tolerance_relationship.related_geometric_tolerance ->
		geometric_tolerance
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// GT <- GTR -> GT 
	public static void setComposer(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		unsetComposer(context, armEntity);
		if(armEntity.testComposer(null)){
			EGeometric_tolerance_armx egt = armEntity.getComposer(null);
			EGeometric_tolerance_relationship egtr = (EGeometric_tolerance_relationship)
				context.working_model.createEntityInstance(CGeometric_tolerance_relationship.definition);
			egtr.setName(null, "composition");
			egtr.setRelating_geometric_tolerance(null, armEntity);
			egtr.setRelated_geometric_tolerance(null, egt);
			egtr.setDescription(null, "");
		}
	}

	/**
	 * Unsets/deletes mapping for attribute Significant_digits.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetComposer(SdaiContext context,
			EGeometric_tolerance_armx armEntity) throws SdaiException {
		AGeometric_tolerance_relationship agtr = new AGeometric_tolerance_relationship();
		CGeometric_tolerance_relationship.usedinRelating_geometric_tolerance(null, armEntity, context.domain, agtr);
		for(int i=1,count=agtr.getMemberCount(); i<=count; i++){
			EGeometric_tolerance_relationship egtr = agtr.getByIndex(i);
			if(egtr.getName(null).equals("composition")){
				egtr.deleteApplicationInstance();
			}
		}
	}

	public static void cleanupPath(SdaiContext context, EMeasure_with_unit emwu, Class type)throws SdaiException {
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

	// Tolerance_zone_armx stuff
	
	public static void setTolerance_zone_attributes(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		unsetTolerance_zone_attributes(context, armEntity);
		ATolerance_zone_definition atzd = new ATolerance_zone_definition();
		CTolerance_zone_definition.usedinZone(null, armEntity, context.domain, atzd);
		// If at least one attribute is set, than we create it
		if((armEntity.testAffected_plane(null))||(armEntity.testModel_coordinate_system(null))||
				(armEntity.testForm_type(null))||(atzd.getMemberCount() > 0)){
			ETolerance_zone etz = (ETolerance_zone)
				context.working_model.createEntityInstance(CTolerance_zone.definition);
			etz.setProduct_definitional(null, ELogical.FALSE);
			if(armEntity.testApplied_to(null)){
				EShape_aspect esa = armEntity.getApplied_to(null);
				if(esa.testOf_shape(null)){
					etz.setOf_shape(null, esa.getOf_shape(null));
				}
				if(esa.testName(null)){
					etz.setName(null, esa.getName(null));
				}else{
					etz.setName(null, "");
				}
				
			}
			etz.createDefining_tolerance(null).addUnordered(armEntity);
			setAffected_plane(context, armEntity, etz);
			setModel_coordinate_system(context, armEntity, etz);
			setForm_type(context, armEntity, etz);
			for(int i=1,count=atzd.getMemberCount();i<=count;i++){
				ETolerance_zone_definition etzd = atzd.getByIndex(i);
				etzd.setZone(null, etz);
			}
			armEntity.unsetAffected_plane(null);
			armEntity.unsetModel_coordinate_system(null);
			armEntity.unsetForm_type(null);
		}
	}
	
	public static void unsetTolerance_zone_attributes(SdaiContext context, EGeometric_tolerance_armx armEntity) throws SdaiException {
		ATolerance_zone atz = new ATolerance_zone();
		CTolerance_zone.usedinDefining_tolerance(null, armEntity, context.domain, atz);
		for(int i=1,count=atz.getMemberCount();i<=count;i++){
			atz.getByIndex(i).deleteApplicationInstance();
		}
	}
	
	/**
	 * Sets/creates data for affected_plane attribute.
	 * 
	 * <p>
	attribute_mapping affected_plane(affected_plane, $PATH, Axis_placement_shape_element);
		tolerance_zone <=
		shape_aspect <- 
		shape_aspect_relationship.related_shape_aspect 
		shape_aspect_relationship
		{shape_aspect_relationship.name = 'affected plane association'} 
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAffected_plane(SdaiContext context, ETolerance_zone_armx armEntity, ETolerance_zone mimEntity) throws SdaiException {
		unsetAffected_plane(context, mimEntity);
		if(armEntity.testAffected_plane(null)){
			EAxis_placement_shape_element eapse = armEntity.getAffected_plane(null);
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, mimEntity);
			esar.setName(null, "affected plane association");
			esar.setRelating_shape_aspect(null, eapse);
		}
	}

	public static void unsetAffected_plane(SdaiContext context, ETolerance_zone mimEntity) throws SdaiException {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, mimEntity, context.domain, asar);
		for(int i=1,count=asar.getMemberCount();i<=count;i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if((esar.testName(null))&&((esar.getName(null).equals("affected plane association")))){
				esar.deleteApplicationInstance();
			}
		}
	}	
	
	/**
	 * Sets/creates data for model_coordinate_system attribute.
	 * 
	 * <p>
	attribute_mapping model_coordinate_system(model_coordinate_system, $PATH, Axis_placement_shape_element);
		tolerance_zone <=
		shape_aspect <- 
		shape_aspect_relationship.related_shape_aspect 
		shape_aspect_relationship
		{shape_aspect_relationship.name = 'model coordinate system'}
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setModel_coordinate_system(SdaiContext context, ETolerance_zone_armx armEntity, ETolerance_zone mimEntity) throws SdaiException {
		unsetModel_coordinate_system(context, mimEntity);
		if(armEntity.testModel_coordinate_system(null)){
			EAxis_placement_shape_element eapse = armEntity.getModel_coordinate_system(null);
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, mimEntity);
			esar.setName(null, "model coordinate system");
			esar.setRelating_shape_aspect(null, eapse);
		}
	}

	public static void unsetModel_coordinate_system(SdaiContext context, ETolerance_zone mimEntity) throws SdaiException {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, mimEntity, context.domain, asar);
		for(int i=1,count=asar.getMemberCount();i<=count;i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if((esar.testName(null))&&((esar.getName(null).equals("model coordinate system")))){
				esar.deleteApplicationInstance();
			}
		}
	}		
	
	/**
	 * Sets/creates data for model_coordinate_system attribute.
	 * 
	 * <p>
	attribute_mapping form_type(form_type, $PATH, tolerance_zone_form.name);
		tolerance_zone.form ->
		tolerance_zone_form
		tolerance_zone_form.name
		{(tolerance_zone_form.name = 'cylindrical')
		(tolerance_zone_form.name = 'spherical')
		(tolerance_zone_form.name = 'parallelepiped')
		(tolerance_zone_form.name = 'conical')
		(tolerance_zone_form.name = 'non uniform')
		(tolerance_zone_form.name != '').UNKOWN.}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setForm_type(SdaiContext context, ETolerance_zone_armx armEntity, ETolerance_zone mimEntity) throws SdaiException {
		unsetForm_type(context, mimEntity);
		if(armEntity.testForm_type(null)){
			int form = armEntity.getForm_type(null);
			String name;
//			if(form == ETolerance_zone_type.UNKOWN){
//				name = "";
//			}else{
				name = ETolerance_zone_type.toString(form).replace("_", " ").toLowerCase();
//			}
			// TZF
			LangUtils.Attribute_and_value_structure[] tzfStructure = {
					new LangUtils.Attribute_and_value_structure(
							CTolerance_zone_form.attributeName(null), name)
			};
			ETolerance_zone_form etzf = (ETolerance_zone_form) 
				LangUtils.createInstanceIfNeeded(context,
						CTolerance_zone_form.definition, tzfStructure);
			mimEntity.setForm(null, etzf);
		}
	}

	public static void unsetForm_type(SdaiContext context, ETolerance_zone mimEntity) throws SdaiException {
		mimEntity.unsetForm(null);
	}			
	
}