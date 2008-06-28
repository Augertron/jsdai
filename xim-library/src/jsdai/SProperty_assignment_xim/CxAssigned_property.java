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

package jsdai.SProperty_assignment_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.dictionary.EEntity_definition;
import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SMaterial_property_definition_schema.EMaterial_property;
import jsdai.SMaterial_property_representation_schema.CMaterial_property_representation;
import jsdai.SMaterial_property_representation_schema.EMaterial_property_representation;
import jsdai.SMeasure_schema.EMeasure_with_unit;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;

public class CxAssigned_property extends CAssigned_property implements EMappedXIMEntity{

	// Taken from CProperty_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a0$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProperty_definition.definition);
		// id_x : OPTIONAL STRING;
		setId_x(context, this);
		
		// representation : OPTIONAL property_value_select;
		setRepresentation(context, this);
		
		unsetId_x(null);
		unsetRepresentation(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		// id_x : OPTIONAL STRING;
		unsetId_x(context, this);
		
		// representation : OPTIONAL property_value_select;
		unsetRepresentation(context, this);
		
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
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
			EApplied_independent_property armEntity) throws SdaiException {
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
			EApplied_independent_property armEntity) throws SdaiException {
	}

	/* Sets/creates data for Representation attribute.
	 * 
	(* XIM specific add-on *)
	attribute_mapping representation(representation, $PATH, measure_with_unit);
		property_definition
		represented_definition = property_definition
		represented_definition <-
		property_definition_representation.definition
		property_definition_representation 
		property_definition_representation
		property_definition_representation.used_representation ->
		representation
		(* GL eliminate some questionable options *)
		!{representation =>
		range_characteristic}
		representation.items[i] ->
		representation_item
		representation_item =>
		measure_representation_item <=
		measure_with_unit
	end_attribute_mapping;
	
	attribute_mapping representation(representation, $PATH);
		property_definition
		represented_definition = property_definition
		represented_definition <-
		property_definition_representation.definition
		property_definition_representation 
		property_definition_representation
		property_definition_representation.used_representation ->
		representation
		representation.items[i] ->
		representation_item
		{representation_item =>
		descriptive_representation_item}
		representation_item.name
	end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 
	public static void setRepresentation(SdaiContext context, EAssigned_property armEntity) throws SdaiException
	{
		//unset old values
		unsetRepresentation(context, armEntity);
		ERepresentation_item eri;
		if (armEntity.testRepresentation(null) == 1){
			EMeasure_with_unit emwu = (EMeasure_with_unit)armEntity.getRepresentation(null);
			eri = CxAP210ARMUtilities.upgradeToMRI(context.working_model, (EMeasure_with_unit)emwu);
		}else if (armEntity.testRepresentation(null) == sRepresentationText){
			String name = (String)armEntity.getRepresentation(null, null);
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, name);
			edri.setDescription(null, "");
			eri = edri;
		}else{
			return;
		}
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinItems(null, eri, context.domain, ar);
		ERepresentation er;
		if(ar.getMemberCount() == 0){
			er = CxAP210ARMUtilities.createRepresentation(context, "", false);
			er.createItems(null).addUnordered(eri);
		}else{
			er = ar.getByIndex(1);
		}
		// PDR
		EEntity_definition type = CProperty_definition_representation.definition;
		if(armEntity instanceof EMaterial_property){
			type = CMaterial_property_representation.definition;
			// TODO - not clear what to do with data_enironment_armx here
		}
		EProperty_definition_representation epdr = (EProperty_definition_representation)
			context.working_model.createEntityInstance(type);
		epdr.setUsed_representation(null, er);
		epdr.setDefinition(null, armEntity);
	}


	/**
	* Unsets/deletes data for Representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRepresentation(SdaiContext context, EAssigned_property armEntity) throws SdaiException
	{
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, apdr);
		for(int i=1;i<=apdr.getMemberCount();i++){
			EProperty_definition_representation epdr = apdr.getByIndex(i);
			epdr.deleteApplicationInstance();
		}
		
	}
	
	/* Sets/creates data for Id_x attribute.
	 * 
	attribute_mapping id_x(id_x, id_attribute.attribute_value);
		property_definition = id_attribute_select
		id_attribute_select <- id_attribute.identified_item
		id_attribute.attribute_value
	end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 
	public static void setId_x(SdaiContext context, EAssigned_property armEntity) throws SdaiException
	{
		//unset old values
		unsetId_x(context, armEntity);
		if (armEntity.testId_x(null)){
			String idx = armEntity.getId_x(null);
			CxAP210ARMUtilities.setId(context, armEntity, idx);
		}
	}

	/**
	* Unsets/deletes data for Id_x attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetId_x(SdaiContext context, EAssigned_property armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetId(context, armEntity);		
	}
	
}