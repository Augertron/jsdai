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

import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SShape_dimension_schema.ADimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_size;
import jsdai.SShape_dimension_schema.EDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.EDimensional_size;
import jsdai.SShape_dimension_schema.EShape_dimension_representation;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxRadial_size_dimension extends CRadial_size_dimension implements EMappedXIMEntity
{

	/* Taken from Dimensional_size */
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
	}

	
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

		setEnvelope_principle(context, this);		
		
		// radius_type : STRING;
		setRadius_type(context, this);
		
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
	
		unsetEnvelope_principle(null);
		
		// radius_type : STRING;
		unsetRadius_type(null);
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
			
			unsetEnvelope_principle(context, this);
			
			// radius_type : STRING;
			unsetRadius_type(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		{dimensional_size
		dimensional_size.name = 'radius'}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, ERadial_size_dimension armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxSize_dimension.setMappingConstraints(context, armEntity);
		armEntity.setName(null, "radius");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, ERadial_size_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName(null);
	}

	/**
	 * Sets/creates data for origin attribute.
	 * 
	 * <p>
	 * 
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
		CxSize_dimension.setId(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetId(context, armEntity);
	}
	

	/**
	 * Sets/creates data for single_value attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setSingle_value(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.setSingle_value(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetSingle_value(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetSingle_value(context, armEntity);
	}

	/**
	 * Sets/creates data for Lower_range attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setLower_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.setLower_range(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLower_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetLower_range(context, armEntity);
	}

	/**
	 * Sets/creates data for Lower_range attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setUpper_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.setUpper_range(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetUpper_range(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetUpper_range(context, armEntity);
	}
	
	/**
	 * Sets/creates data for notes attribute.
	 * 
	 * <p>
	 * 
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
		CxSize_dimension.setNotes(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetNotes(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetNotes(context, armEntity);
	}

	/**
	 * Sets/creates data for envelope_principle attribute.
	 * 
	 * <p>
	 * 
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
		CxSize_dimension.setEnvelope_principle(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetEnvelope_principle(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetEnvelope_principle(context, armEntity);
	}

	/**
	 * Sets/creates data for theoretical_exact attribute.
	 * 
	 * <p>
	 * 
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
		CxSize_dimension.setTheoretical_exact(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetTheoretical_exact(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetTheoretical_exact(context, armEntity);
	}

	/**
	 * Sets/creates data for auxiliary attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DS <- DCR -> SDR -> DRI
	public static void setAuxiliary(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.setAuxiliary(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetAuxiliary(SdaiContext context, ESize_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetAuxiliary(context, armEntity);
	}
	
	/**
	 * Sets/creates data for orientation attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping radius_type(radius_type, (descriptive_representation_item.description)(descriptive_representation_item.description));
		(dimensional_size 
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
		{representation_item.name = 'radius type'}
		representation_item =>
		descriptive_representation_item
		{descriptive_representation_item.description = 'centred'})
		(dimensional_size 
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
		{representation_item.name = 'radius type'}
		representation_item =>
		descriptive_representation_item
		{descriptive_representation_item.description = 'adjoining'})
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
	public static void setRadius_type(SdaiContext context, ERadial_size_dimension armEntity) throws SdaiException {
		unsetRadius_type(context, armEntity);
		if(armEntity.testRadius_type(null)){
			int value = armEntity.getRadius_type(null);

			EDescriptive_representation_item edri = 
				(EDescriptive_representation_item)context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, "radius type");
			edri.setDescription(null, ERadius_type.toString(value).toLowerCase());
			CxGDTCommon.setDimension_value(context, armEntity, edri);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetRadius_type(SdaiContext context, ERadial_size_dimension armEntity) throws SdaiException {
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			if(CxGDTCommon.isSubtypeNonModifiable(esdr)){
				continue;
			}
			ARepresentation_item items = esdr.getItems(null);
			for(int j=1,count2=items.getMemberCount(); j<=count2; j++){
				ERepresentation_item item = items.getByIndex(j);
				// security check not to mess with 'dimension_value'
				if((item.testName(null))&&(item.getName(null).equals("radius type"))){
					item.deleteApplicationInstance();
				}
			}
		}
	}
}