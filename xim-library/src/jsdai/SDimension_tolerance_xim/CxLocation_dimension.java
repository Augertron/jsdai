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
*/

import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SShape_dimension_schema.CDirected_dimensional_location;
import jsdai.SGeometry_schema.EGeometric_representation_context;
import jsdai.SGeometry_schema.EPlacement;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.SProduct_property_representation_schema.AShape_definition_representation;
import jsdai.SProduct_property_representation_schema.CShape_definition_representation;
import jsdai.SProduct_property_representation_schema.EShape_definition_representation;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SShape_dimension_schema.ADimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_location;
import jsdai.SShape_dimension_schema.EDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.EShape_dimension_representation;
import jsdai.lang.A_string;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxLocation_dimension extends CLocation_dimension implements EMappedXIMEntity
{

	/* Taken from Shape_aspect_relationship */
/*	public static int usedinRelating_shape_aspect(EShape_aspect_relationship type, EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return test_instance(a2);
	}
	public EShape_aspect getRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return (EShape_aspect)get_instance(a2);
	}
	public void setRelating_shape_aspect(EShape_aspect_relationship type, EShape_aspect value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return a2$;
	}*/

	//going through all the attributes: #5629499534229212=EXPLICIT_ATTRIBUTE('related_shape_aspect',#5629499534229207,3,#5629499534229200,$,.F.);
	//<01> generating methods for consolidated attribute:  related_shape_aspect
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : related_shape_aspect, base type: entity shape_aspect
/*	public static int usedinRelated_shape_aspect(EShape_aspect_relationship type, EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return test_instance(a3);
	}
	public EShape_aspect getRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return (EShape_aspect)get_instance(a3);
	}
	public void setRelated_shape_aspect(EShape_aspect_relationship type, EShape_aspect value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a3 = unset_instance(a3);
	}*/
	public static jsdai.dictionary.EAttribute attributeRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return a3$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		System.err.println(" LD "+this);
		if((testDirected(null))&&(getDirected(null))){
			setTemp("AIM", CDirected_dimensional_location.definition);
		}else{
			setTemp("AIM", CDimensional_location.definition);
		}

		setMappingConstraints(context, this);

		// id : STRING;
		setId_x(context, this);
			
		// single_value : OPTIONAL measure_representation_item;
		setSingle_value(context, this);

		// lower_range : OPTIONAL measure_representation_item;
		setLower_range(context, this);

		// upper_range : OPTIONAL measure_representation_item;
		setUpper_range(context, this);
		
		// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
		setNotes(context, this); 
		
		// theoretical_exact : BOOLEAN;		
		setTheoretical_exact(context, this);

		// auxiliary         : BOOLEAN;	
		setAuxiliary(context, this);

		// orientation : OPTIONAL axis2_placement;	
		setOrientation(context, this);
		
		// (* Its own attributes *)	  
		// origin		: placed_element_select;
//		setOrigin(context, this);
		
		// target		: placed_element_select;
//		setTarget(context, this);
		
		// envelope_principle : OPTIONAL BOOLEAN;
		setEnvelope_principle(context, this);
		
		// Unset attributes
		
		unsetEnvelope_principle(null);
		
		// id : STRING;
		unsetId_x(null);
		
		// single_value : OPTIONAL measure_representation_item;
		unsetSingle_value(null);

		// lower_range : OPTIONAL measure_representation_item;
		unsetLower_range(null);

		// upper_range : OPTIONAL measure_representation_item;
		unsetUpper_range(null);
		
		// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
		unsetNotes(null); 
		
		// theoretical_exact : BOOLEAN;		
		unsetTheoretical_exact(null);

		// auxiliary         : BOOLEAN;	
		unsetAuxiliary(null);

		// orientation : OPTIONAL axis2_placement;	
		unsetOrientation(null);
		
		// (* Its own attributes *)	  
		// directed 	: OPTIONAL BOOLEAN;
		unsetDirected(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// id : STRING;
			unsetId_x(context, this);
			
			// single_value : OPTIONAL measure_representation_item;
			unsetSingle_value(context, this);

			// lower_range : OPTIONAL measure_representation_item;
			unsetLower_range(context, this);

			// upper_range : OPTIONAL measure_representation_item;
			unsetUpper_range(context, this);
			
			// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
			unsetNotes(context, this); 
			
			// theoretical_exact : BOOLEAN;		
			unsetTheoretical_exact(context, this);

			// auxiliary         : BOOLEAN;	
			unsetAuxiliary(context, this);

			// orientation : OPTIONAL axis2_placement;	
			unsetOrientation(context, this);
			
			// origin		: placed_element_select;
//			unsetOrigin(context, this);
			
			// target		: placed_element_select;
//			unsetTarget(context, this);
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
	public static void setMappingConstraints(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for origin attribute.
	 * 
	 * <p>
	 * 
attribute_mapping origin(origin, $PATH, Shape_element);
(dimensional_location <= 
shape_aspect_relationship 
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect)
end_attribute_mapping;

attribute_mapping origin(origin, $PATH, Derived_shape_element);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect =>
derived_shape_aspect

end_attribute_mapping;

attribute_mapping origin(origin, $PATH, constructive_element_select);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item 


end_attribute_mapping;
// DL -> SA <- PD <- PDR -> R -> C
attribute_mapping origin(origin, $PATH, Curve);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
curve

end_attribute_mapping;
// DL -> SA <- PD <- PDR -> R -> POC
attribute_mapping origin(origin, $PATH, Point_on_curve);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
point  =>
point_on_curve

end_attribute_mapping;
// DL -> SA <- PD <- PDR -> R -> POS/DP
attribute_mapping origin(origin, ($PATH)($PATH), Point_on_surface);
((dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
point =>
point_on_surface)
)((dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
point =>
degenerate_pcurve)
)
end_attribute_mapping;
// DL -> SA <- PD <- PDR -> R -> S
attribute_mapping origin(origin, ($PATH)($PATH), Surface);
(dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
surface
)(dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
surface =>
oriented_surface
)
end_attribute_mapping;
 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL -> SA 
	// DL -> SA <- PD <- PDR -> R -> POC
/* Take from MIM	
	public static void setOrigin(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetOrigin(context, armEntity);
		if(armEntity.testOrigin(null)){
			EEntity value = armEntity.getOrigin(null);
			if(value instanceof EShape_aspect){
				armEntity.setRelating_shape_aspect(null, (EShape_aspect)value);
				return;
			}else if(value instanceof ERepresentation_item){
				EShape_aspect esa = CxGDTCommon.getSuitableShape_aspect(context, value);
				armEntity.setRelating_shape_aspect(null, esa);
			}else{
				System.err.println(" Unsupported case for location_dimention.origin "+value);
			}
		}
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/*	
	public static void unsetOrigin(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		armEntity.unsetRelating_shape_aspect(null);
	}
*/
	/**
	 * Sets/creates data for target attribute.
	 * 
	 * <p>
	 * 
attribute_mapping target(target, $PATH, Shape_element);
dimensional_location <= 
shape_aspect_relationship 
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
end_attribute_mapping;

attribute_mapping target(target, $PATH, Derived_shape_element);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect =>
derived_shape_aspect

end_attribute_mapping;

attribute_mapping target(target, $PATH, constructive_element_select);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item


end_attribute_mapping;

attribute_mapping target(target, $PATH, Curve);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
curve

end_attribute_mapping;

attribute_mapping target(target, $PATH, Point_on_curve);
dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
point =>
point_on_curve

end_attribute_mapping;

attribute_mapping target(target, ($PATH)($PATH), Point_on_surface);
((dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
point =>
point_on_surface)
)((dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
point =>
degenerate_pcurve)
)
end_attribute_mapping;

attribute_mapping target(target, ($PATH)($PATH), Surface);
(dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
surface
)(dimensional_location <=
shape_aspect_relationship
shape_aspect_relationship.related_shape_aspect ->
shape_aspect
shape_definition = shape_aspect
shape_definition
characterized_definition = shape_definition
characterized_definition <-
property_definition.definition
property_definition
represented_definition = property_definition
represented_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item =>
geometric_representation_item =>
surface =>
oriented_surface
)
end_attribute_mapping;
)
end_attribute_mapping;
 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL -> SA 
	// DL -> SA <- PD <- PDR -> R -> POC
/* Use MIM directly	
	public static void setTarget(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetTarget(context, armEntity);
		if(armEntity.testTarget(null)){
			EEntity value = armEntity.getTarget(null);
			if(value instanceof EShape_aspect){
				armEntity.setRelated_shape_aspect(null, (EShape_aspect)value);
				return;
			}else if(value instanceof ERepresentation_item){
				EShape_aspect esa = CxGDTCommon.getSuitableShape_aspect(context, value);
				armEntity.setRelated_shape_aspect(null, esa);
			}else{
				System.err.println(" Unsupported case for location_dimention.target "+value);
			}
		}
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/*	
	public static void unsetTarget(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		armEntity.unsetRelated_shape_aspect(null);
	}
*/
	/**
	 * Sets/creates data for dimension_value attribute.
	 * 
	 * <p>
	 * 
attribute_mapping single_value(single_value, $PATH, measure_representation_item);
-- add more contraints to avoid multiple matched
(* I don't think we need it anymore
!{dimensional_location 
dimensional_characteristic = dimensional_location
dimensional_characteristic <-
plus_minus_tolerance.toleranced_dimension
plus_minus_tolerance} *)
dimensional_location 
dimensional_characteristic = dimensional_location
dimensional_characteristic <- 
dimensional_characteristic_representation.dimension 
dimensional_characteristic_representation 
dimensional_characteristic_representation.representation -> 
shape_dimension_representation <= 
shape_representation <=  
representation  
representation.items[i] -> 
representation_item
!{representation_item => 
qualified_representation_item}
!{representation_item
representation_item.name = 'lower range'}
!{representation_item
representation_item.name = 'upper range'}
representation_item => 
measure_representation_item
end_attribute_mapping;

	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL <- DCR -> SDR (-> MRI)
	// DL <- PMT
	public static void setSingle_value(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetSingle_value(context, armEntity);
		if(armEntity.testSingle_value(null)){
			EMeasure_representation_item value = armEntity.getSingle_value(null);
			CxGDTCommon.setDimension_value(context, armEntity, value);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetSingle_value(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetSingle_value(context, armEntity);
	}

	/**
	 * Sets/creates data for lower_range attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping lower_range(lower_range, $PATH, measure_representation_item);
		(* I don't think we need it anymore
		!{dimensional_location 
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <-
		plus_minus_tolerance.toleranced_dimension
		plus_minus_tolerance} *)
		dimensional_location 
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <- 
		dimensional_characteristic_representation.dimension 
		dimensional_characteristic_representation 
		dimensional_characteristic_representation.representation -> 
		shape_dimension_representation <=
		representation
		representation.items[i] ->
		{representation_item
		representation_item.name = 'lower range'}
		representation_item =>
		measure_representation_item
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL <- DCR -> SDR (-> MRI)
	// DL <- PMT
	public static void setLower_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetLower_range(context, armEntity);
		if(armEntity.testLower_range(null)){
			EMeasure_representation_item value = armEntity.getLower_range(null);
			CxGDTCommon.setDimension_value(context, armEntity, value);
			value.setName(null, "lower range");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLower_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetLower_range(context, armEntity);
	}

	/**
	 * Sets/creates data for upper_range attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping upper_range(upper_range, $PATH, measure_representation_item);
		(* I don't think we need it anymore
		!{dimensional_location 
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <-
		plus_minus_tolerance.toleranced_dimension
		plus_minus_tolerance} *)
		dimensional_location 
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <- 
		dimensional_characteristic_representation.dimension 
		dimensional_characteristic_representation 
		dimensional_characteristic_representation.representation -> 
		shape_dimension_representation <=
		representation
		representation.items[i] ->
		{representation_item
		representation_item.name = 'upper range'}
		representation_item =>
		measure_representation_item
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL <- DCR -> SDR (-> MRI)
	// DL <- PMT
	public static void setUpper_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetUpper_range(context, armEntity);
		if(armEntity.testUpper_range(null)){
			EMeasure_representation_item value = armEntity.getUpper_range(null);
			CxGDTCommon.setDimension_value(context, armEntity, value);
			value.setName(null, "upper range");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetUpper_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetUpper_range(context, armEntity);
	}
	
	/**
	 * Sets/creates data for notes attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping notes(notes, descriptive_representation_item.description);
		dimensional_location 
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <- 
		dimensional_characteristic_representation.dimension 
		dimensional_characteristic_representation 
		dimensional_characteristic_representation.representation -> 
		shape_dimension_representation <= 
		shape_representation <=  
		representation  
		representation.items[i] -> 
		representation_item 
		{representation_item.name = 'dimensional note'} 
		representation_item => 
		descriptive_representation_item.description
		!{(descriptive_representation_item.description = 'theoretical')
		(descriptive_representation_item.description = 'auxiliary')}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL <- DCR -> SDR -> DRI
	// DL <- PMT
	public static void setNotes(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetNotes(context, armEntity);
		if(armEntity.testNotes(null)){
			A_string values = armEntity.getNotes(null);
			for(int i=1,count=values.getMemberCount(); i<=count; i++){
				String value = values.getByIndex(i);
				EDescriptive_representation_item edri = 
					(EDescriptive_representation_item)context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
				edri.setName(null, "dimensional note");
				edri.setDescription(null, value);
				CxGDTCommon.setDimension_value(context, armEntity, edri);
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetNotes(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetNotes(context, armEntity);		
	}

	/**
	 * Sets/creates data for theoretical_exact attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping theoretical_exact(theoretical_exact, descriptive_representation_item.description);
		dimensional_location 
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <- 
		dimensional_characteristic_representation.dimension 
		dimensional_characteristic_representation 
		dimensional_characteristic_representation.representation -> 
		shape_dimension_representation <= 
		shape_representation <=  
		representation  
		representation.items[i] -> 
		representation_item 
		{representation_item.name = 'dimensional note'} 
		representation_item => 
		descriptive_representation_item.description
		{descriptive_representation_item.description = 'theoretical'}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL <- DCR -> SDR -> DRI
	// DL <- PMT
	public static void setTheoretical_exact(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetTheoretical_exact(context, armEntity);
		if(armEntity.testTheoretical_exact(null)){
			boolean value = armEntity.getTheoretical_exact(null);
			if(value){
				EDescriptive_representation_item edri = 
					(EDescriptive_representation_item)context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
				edri.setName(null, "dimensional note");
				edri.setDescription(null, "theoretical");
				CxGDTCommon.setDimension_value(context, armEntity, edri);
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetTheoretical_exact(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetTheoretical_exact(context, armEntity);		
	}

	/**
	 * Sets/creates data for auxiliary attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping auxiliary(auxiliary, descriptive_representation_item.description);
		dimensional_location 
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <- 
		dimensional_characteristic_representation.dimension 
		dimensional_characteristic_representation 
		dimensional_characteristic_representation.representation -> 
		shape_dimension_representation <= 
		shape_representation <=  
		representation  
		representation.items[i] -> 
		representation_item 
		{representation_item.name = 'dimensional note'} 
		representation_item => 
		descriptive_representation_item.description
		{descriptive_representation_item.description = 'auxiliary'}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL <- DCR -> SDR -> DRI
	// DL <- PMT
	public static void setAuxiliary(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetAuxiliary(context, armEntity);
		if(armEntity.testAuxiliary(null)){
			boolean value = armEntity.getAuxiliary(null);
			if(value){
				EDescriptive_representation_item edri = 
					(EDescriptive_representation_item)context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
				edri.setName(null, "dimensional note");
				edri.setDescription(null, "auxiliary");
				CxGDTCommon.setDimension_value(context, armEntity, edri);
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetAuxiliary(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetAuxiliary(context, armEntity);		
	}
	
	/**
	 * Sets/creates data for orientation attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping orientation(orientation, $PATH, axis2_placement);
		angular_location <=
		dimensional_location <=
		shape_aspect_relationship
		shape_definition = shape_aspect_relationship
		shape_definition
		characterized_definition = shape_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation
		representation.items[i] ->
		{representation_item
		representation_item.name = 'orientation'}
		representation_item =>
		geometric_representation_item =>
		placement =>
		(axis2_placement_2d)
		(axis2_placement_3d)
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// AL <- PD <- PDR -> R -> P
	public static void setOrientation(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetOrientation(context, armEntity);
		if(armEntity.testOrientation(null)){
			EPlacement ep = (EPlacement)armEntity.getOrientation(null);
			EShape_dimension_representation esdr = CxGDTCommon.setDimension_value(context, armEntity, ep);
			// need to make rep_conext -> geometric_rep_ontext
			ERepresentation_context erc = esdr.getContext_of_items(null);
			if(!(erc instanceof EGeometric_representation_context)){
				EProduct_definition_shape epds = armEntity.getRelated_shape_aspect(null).getOf_shape(null);
				AShape_definition_representation asdr = new AShape_definition_representation();
				CShape_definition_representation.usedinDefinition(null, epds, context.domain, asdr);
				CShape_definition_representation.usedinDefinition(null, epds, null, asdr);
				if(asdr.getMemberCount() > 0){
					esdr.unsetContext_of_items(null);
					LangUtils.deleteInstanceIfUnused(context.domain, erc);
					EShape_definition_representation esdr1 = asdr.getByIndex(1);
					esdr.setContext_of_items(null, esdr1.getUsed_representation(null).getContext_of_items(null));
				}
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetOrientation(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetOrientation(context, armEntity);
	}

	/**
	 * Sets/creates data for envelope_principle attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping envelope_principle(envelope_principle, representation.name);
		dimensional_size
		dimensional_characteristic = dimensional_size
		dimensional_characteristic <-
		dimensional_characteristic_representation.dimension
		dimensional_characteristic_representation
		dimensional_characteristic_representation.representation ->
		shape_dimension_representation <=
		shape_representation <=
		representation
		representation.name
		{representation.name = 'envelope tolerance'}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DS <- DCR -> SDR
	public static void setEnvelope_principle(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetEnvelope_principle(context, armEntity);
		if(armEntity.testEnvelope_principle(null)){
			boolean value = armEntity.getEnvelope_principle(null);
			EShape_dimension_representation esdr = CxGDTCommon.setDimension_value(context, armEntity, null);
			if(value){
				esdr.setName(null, "envelope requirement");
			}else{
				esdr.setName(null, "independency");
			}
		}
	}

	/**
	 * Unsets/deletes data for envelope_principle attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetEnvelope_principle(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			esdr.setName(null, "");
		}
	}

	/**
	 * Sets/creates data for id_x attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping id_x(id_x, shape_aspect_relationship.id);
		dimensional_location <=
		shape_aspect_relationship
		shape_aspect_relationship.id
	
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DS <- DCR -> SDR
	public static void setId_x(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		unsetId_x(context, armEntity);
		if(armEntity.testId_x(null)){
			String value = armEntity.getId_x(null);
			CxAP210ARMUtilities.setId(context, armEntity, value);
		}
	}

	/**
	 * Unsets/deletes data for id_x attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxAP210ARMUtilities.unsetId(context, armEntity);
	}
	
}