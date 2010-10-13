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

import jsdai.SIdentification_assignment_mim.AApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.AIdentification_item;
import jsdai.SIdentification_assignment_mim.CApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.EApplied_identification_assignment;
import jsdai.SManagement_resources_schema.CIdentification_role;
import jsdai.SManagement_resources_schema.EIdentification_role;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SShape_dimension_schema.ADimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_size;
import jsdai.SShape_dimension_schema.EDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.EShape_dimension_representation;
import jsdai.lang.A_string;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxSize_dimension extends CSize_dimension implements EMappedXIMEntity
{

	/* Taken from Dimensional_size 
	/// methods for attribute: name, base type: STRING
	public boolean testName(EDimensional_size type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EDimensional_size type) throws SdaiException {
		return get_string(a1);
	}
	public void setName(EDimensional_size type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EDimensional_size type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EDimensional_size type) throws SdaiException {
		return a1$;
	}*/

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDimensional_size.definition);

		setMappingConstraints(context, this);

		// (* From supertype in the ARM - Geometric_dimension *)
		// id : STRING;
		setId(context, this);
		
		// single_value : dimension_value_select;
		setSingle_value(context, this);

		// lower_range : OPTIONAL measure_representation_item;
		setLower_range(context, this);
		
		// upper_range : OPTIONAL measure_representation_item;
		setUpper_range(context, this);
		
		// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
		setNotes(context, this);
		
		// theoretical_exact : OPTIONAL BOOLEAN;
		setTheoretical_exact(context, this);
		
		// auxiliary         : OPTIONAL BOOLEAN;
		setAuxiliary(context, this);
		
		// (* Its own attributes *)	  
		// envelope_principle : OPTIONAL BOOLEAN;
		setEnvelope_principle(context, this);
		
		// id : STRING;
		unsetId(null);
		
		// single_value : dimension_value_select;
		unsetSingle_value(null);

		// lower_range : OPTIONAL measure_representation_item;
		unsetLower_range(null);
		
		// upper_range : OPTIONAL measure_representation_item;
		unsetUpper_range(null);
		
		// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
		unsetNotes(null);
		
		// theoretical_exact : OPTIONAL BOOLEAN;
		unsetTheoretical_exact(null);
		
		// auxiliary         : OPTIONAL BOOLEAN;
		unsetAuxiliary(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// id : STRING;
			unsetId(context, this);
			
			// single_value : dimension_value_select;
			unsetSingle_value(context, this);

			// lower_range : OPTIONAL measure_representation_item;
			unsetLower_range(context, this);
			
			// upper_range : OPTIONAL measure_representation_item;
			unsetUpper_range(context, this);
			
			// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
			unsetNotes(context, this);
			
			// theoretical_exact : OPTIONAL BOOLEAN;
			unsetTheoretical_exact(context, this);
			
			// auxiliary         : OPTIONAL BOOLEAN;
			unsetAuxiliary(context, this);
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
	public static void setMappingConstraints(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for origin attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping id(id, identification_assignment.assigned_id);
		dimensional_size identification_item = dimensional_size 
		identification_item <- 
		applied_identification_assignment.items[i] 
		applied_identification_assignment <= 
		identification_assignment 
		{identification_assignment.role -> 
		identification_role 
		identification_role.name = 'size id'} 
		identification_assignment.assigned_id
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DS <- AIA 
	public static void setId(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		unsetId(context, armEntity);
		if(armEntity.testId(null)){
			String value = armEntity.getId(null);
			// IR
			LangUtils.Attribute_and_value_structure[] irStructure = {
					new LangUtils.Attribute_and_value_structure(
							CIdentification_role.attributeName(null), "size id")
			};
			EIdentification_role eir = (EIdentification_role) 
				LangUtils.createInstanceIfNeeded(context,
						CIdentification_role.definition, irStructure);
			// AIA
			LangUtils.Attribute_and_value_structure[] aiaStructure = {
					new LangUtils.Attribute_and_value_structure(
							CApplied_identification_assignment.attributeRole(null), eir),
					new LangUtils.Attribute_and_value_structure(
							CApplied_identification_assignment.attributeAssigned_id(null), value)
			};
			EApplied_identification_assignment eaia = (EApplied_identification_assignment) 
				LangUtils.createInstanceIfNeeded(context,
						CApplied_identification_assignment.definition, aiaStructure);
			AIdentification_item items;
			if(eaia.testItems(null)){
				items = eaia.getItems(null);
			}else{
				items = eaia.createItems(null);
			}
			items.addUnordered(armEntity);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		AApplied_identification_assignment aaia = new AApplied_identification_assignment();
		CApplied_identification_assignment.usedinItems(null, armEntity, context.domain, aaia);
		for(int i=1;i<=aaia.getMemberCount();i++){
			aaia.getByIndex(i).deleteApplicationInstance();
		}
	}
	

	/**
	 * Sets/creates data for lower_range attribute.
	 * 
	 * <p>
	 * 
attribute_mapping lower_range(lower_range, $PATH, measure_representation_item);
	dimensional_size 
	dimensional_characteristic = dimensional_size
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
	public static void setLower_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
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
	public static void unsetLower_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetLower_range(context, armEntity);
	}

	/**
	 * Sets/creates data for upper_range attribute.
	 * 
	 * <p>
	 * 
attribute_mapping upper_range(upper_range, $PATH, measure_representation_item);
	dimensional_size 
	dimensional_characteristic = dimensional_size
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
	public static void setUpper_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
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
	public static void unsetUpper_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetUpper_range(context, armEntity);		
	}
	
	/**
	 * Sets/creates data for single_value attribute.
	 * 
	 * <p>
	 * 
attribute_mapping single_value(single_value, $PATH, measure_representation_item);

-- add more contraints to avoid multiple matched
-- GL added
(* I don't think we need it anymore
!{dimensional_size 
dimensional_characteristic = dimensional_size
dimensional_characteristic <-
plus_minus_tolerance.toleranced_dimension
plus_minus_tolerance} *)
dimensional_size 
dimensional_characteristic = dimensional_size
dimensional_characteristic <- 
dimensional_characteristic_representation.dimension 
dimensional_characteristic_representation 
dimensional_characteristic_representation.representation -> 
shape_dimension_representation <= 
shape_representation <=  
representation  
representation.items[i] -> 
representation_item 
!{representation_item
representation_item.name = 'lower range'}
!{representation_item
representation_item.name = 'upper range'}
!{representation_item => 
qualified_representation_item}
representation_item => 
measure_representation_item
end_attribute_mapping;

attribute_mapping single_value(single_value, $PATH, Value_limit);
dimensional_size 
dimensional_characteristic = dimensional_size
dimensional_characteristic <- 
dimensional_characteristic_representation.dimension 
dimensional_characteristic_representation 
dimensional_characteristic_representation.representation -> 
shape_dimension_representation <= 
shape_representation <=  
representation  
representation.items[i] -> 
representation_item 
!{representation_item
representation_item.name = 'lower range'}
!{representation_item
representation_item.name = 'upper range'}
representation_item => 
[measure_representation_item]
[qualified_representation_item]
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
	public static void setSingle_value(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		unsetSingle_value(context, armEntity);
		if(armEntity.testSingle_value(null)){
			ERepresentation_item value = (ERepresentation_item)armEntity.getSingle_value(null);
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
	public static void unsetSingle_value(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetSingle_value(context, armEntity);
	}
	
	/**
	 * Sets/creates data for notes attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping notes(notes, descriptive_representation_item.description);
		dimensional_size 
		dimensional_characteristic = dimensional_size
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
		descriptive_representation_item
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
	// DS <- DCR -> SDR -> DRI
	public static void setNotes(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
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
	public static void unsetNotes(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetNotes(context, armEntity);
	}

	/**
	 * Sets/creates data for theoretical_exact attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping theoretical_exact(theoretical_exact, descriptive_representation_item.description);
		dimensional_size 
		dimensional_characteristic = dimensional_size
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
	// DS <- DCR -> SDR -> DRI
	public static void setTheoretical_exact(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
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
	public static void unsetTheoretical_exact(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetTheoretical_exact(context, armEntity);
	}

	/**
	 * Sets/creates data for theoretical_exact attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping auxiliary(auxiliary, descriptive_representation_item.description);
		dimensional_size 
		dimensional_characteristic = dimensional_size
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
	public static void setAuxiliary(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
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
	public static void unsetAuxiliary(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetAuxiliary(context, armEntity);
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
	// DS <- DCR -> SDR -> DRI
	public static void setEnvelope_principle(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		unsetEnvelope_principle(context, armEntity);
		if(armEntity.testEnvelope_principle(null)){
			boolean value = armEntity.getEnvelope_principle(null);
			if(value){
				EShape_dimension_representation esdr = CxGDTCommon.setDimension_value(context, armEntity, null);
				esdr.setName(null, "envelope tolerance");
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
	public static void unsetEnvelope_principle(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			esdr.setName(null, "");
		}
	}
	
}