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

import jsdai.SDimension_tolerance_mim.CExternally_defined_dimension_definition;
import jsdai.SExternal_reference_schema.CExternal_source;
import jsdai.SExternal_reference_schema.EExternal_source;
import jsdai.SExternal_reference_schema.EExternally_defined_item;
import jsdai.SExternal_reference_schema.EMessage;
import jsdai.SMixed_complex_types.CDimensional_size_with_path$externally_defined_dimension_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SShape_dimension_schema.EDimensional_size_with_path;
import jsdai.SSupport_resource_schema.EIdentifier;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxExternally_defined_size_dimension extends CExternally_defined_size_dimension implements EMappedXIMEntity
{

	/* Taken from EExternally_defined_item */
	//going through all the attributes: #5629499534229299=EXPLICIT_ATTRIBUTE('item_id',#5629499534229297,0,#5629499534229285,$,.F.);
	//<01> generating methods for consolidated attribute:  item_id
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// -2- methods for SELECT attribute: item_id
/*	public int testItem_id(EExternally_defined_item type) throws SdaiException {
		return test_select(a0, a0$$);
	}

	public String getItem_id(EExternally_defined_item type, jsdai.SSupport_resource_schema.EIdentifier node1) throws SdaiException { // case 2
		return get_string_select(a0, a0$$, 2);
	}
	public String getItem_id(EExternally_defined_item type, EMessage node1) throws SdaiException { // case 3
		return get_string_select(a0, a0$$, 3);
	}
*/
	public void setItem_id(EExternally_defined_item type, String value, jsdai.SSupport_resource_schema.EIdentifier node1) throws SdaiException { // case 2
		a2 = set_string(value);
		a2$$ = 2;
	}
	public void setItem_id(EExternally_defined_item type, String value, EMessage node1) throws SdaiException { // case 3
		a2 = set_string(value);
		a2$$ = 3;
	}

	public void unsetItem_id(EExternally_defined_item type) throws SdaiException {
		a2 = unset_select(a0);
		a2$$ = 0;
	}

	public static jsdai.dictionary.EAttribute attributeItem_id(EExternally_defined_item type) throws SdaiException {
		return a2$;
	}

	//going through all the attributes: #5629499534229345=EXPLICIT_ATTRIBUTE('source',#5629499534229342,1,#5629499534229332,$,.F.);
	//<01> generating methods for consolidated attribute:  source
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : source, base type: entity external_source
/*	public static int usedinSource(EExternally_defined_item type, EExternal_source instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testSource(EExternally_defined_item type) throws SdaiException {
		return test_instance(a1);
	}
	public EExternal_source getSource(EExternally_defined_item type) throws SdaiException {
		return (EExternal_source)get_instance(a1);
	}*/
	public void setSource(EExternally_defined_item type, EExternal_source value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetSource(EExternally_defined_item type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeSource(EExternally_defined_item type) throws SdaiException {
		return a3$;
	}
	
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

	/* Taken from EDimensional_location_with_path */
	//going through all the attributes: #5629499534230326=EXPLICIT_ATTRIBUTE('path',#5629499534230324,0,#5629499534229191,$,.F.);
	//<01> generating methods for consolidated attribute:  path
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : path, base type: entity shape_aspect
/*	public static int usedinPath(EDimensional_size_with_path type, jsdai.SProduct_property_definition_schema.EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testPath(EDimensional_size_with_path type) throws SdaiException {
		return test_instance(a4);
	}
	public jsdai.SProduct_property_definition_schema.EShape_aspect getPath(EDimensional_size_with_path type) throws SdaiException {
		return (jsdai.SProduct_property_definition_schema.EShape_aspect)get_instance(a4);
	}*/
	public void setPath(EDimensional_size_with_path type, jsdai.SProduct_property_definition_schema.EShape_aspect value) throws SdaiException {
		a4 = set_instance(a4, value);
	}
	public void unsetPath(EDimensional_size_with_path type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributePath(EDimensional_size_with_path type) throws SdaiException {
		return a4$;
	}
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		if(testUsed_path(null)){
			setTemp("AIM", CDimensional_size_with_path$externally_defined_dimension_definition.definition);
		}else{
			setTemp("AIM", CExternally_defined_dimension_definition.definition);
		}

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
		
		// used_path : OPTIONAL Measurement_path;
		setUsed_path(context, this);
		
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
	
		// used_path : OPTIONAL Measurement_path;
		unsetUsed_path(null);
		
		unsetEnvelope_principle(null);		
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
			
			// used_path : OPTIONAL Measurement_path;
			unsetUsed_path(context, this);
			
			unsetEnvelope_principle(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		externally_defined_dimension_definition <=
		externally_defined_item
		{ externally_defined_item.item_id -> 
		source_item
		source_item = 'external size dimension'}
		{ externally_defined_item.source -> 
		external_source
		external_source.source_id ->
		source_item
		source_item = 'external size dimension specification' }
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EExternally_defined_size_dimension armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxSize_dimension.setMappingConstraints(context, armEntity);
		armEntity.setItem_id(null, "external size dimension", (EIdentifier)null);
		// external_source
		LangUtils.Attribute_and_value_structure[] esStructure = {
				new LangUtils.Attribute_and_value_structure(
						CExternal_source.attributeSource_id(null), "external size dimension specification", context.schema.getDefinedType("identifier"))
		};
		EExternal_source es = (EExternal_source) 
			LangUtils.createInstanceIfNeeded(context,
					CExternal_source.definition, esStructure);
		armEntity.setSource(null, es);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, EExternally_defined_size_dimension armEntity) throws SdaiException {
		CxSize_dimension.unsetMappingConstraints(context, armEntity);
		armEntity.unsetItem_id(null);
		armEntity.unsetSource(null);
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
	// DL <- DCR -> SDR (-> MRI)
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
	 * Sets/creates data for lower_range attribute.
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
	// DL <- DCR -> SDR (-> MRI)
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
	 * Sets/creates data for upper_range attribute.
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
	// DL <- DCR -> SDR (-> MRI)
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
	 * Sets/creates data for orientation attribute.
	 * 
	 * <p>
	 * 
	attribute_mapping used_path(used_path, $PATH, Measurement_path);
		externally_defined_dimension_definition <=
		dimensional_size =>
		dimensional_size_with_path 
		dimensional_size_with_path.path -> 
		shape_aspect 
		shape_definition = shape_aspect 
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
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DLWP.path -> SA <- PD <- PDR -> R 
	public static void setUsed_path(SdaiContext context, EExternally_defined_size_dimension armEntity) throws SdaiException {
		unsetUsed_path(context, armEntity);
		if(armEntity.testUsed_path(null)){
			EShape_aspect esa = armEntity.getUsed_path(null);
			armEntity.setPath(null, esa);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetUsed_path(SdaiContext context, EExternally_defined_size_dimension armEntity) throws SdaiException {
		armEntity.unsetPath(null);
	}
	
}