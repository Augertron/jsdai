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

package jsdai.SExtended_geometric_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $Revision$
* $Id$
*/

import jsdai.SExtended_geometric_tolerance_mim.*;
import jsdai.SGeometry_schema.EDirection;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.*;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxDatum_based_length_measure extends CDatum_based_length_measure implements EMappedXIMEntity
{

	// Take from CRepresentation
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a0$;
	}
	// methods for attribute: items, base type: SET OF ENTITY
/*	public static int usedinItems(ERepresentation type, ERepresentation_item instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}*/
	boolean testItems2(ERepresentation type) throws SdaiException {
		return test_aggregate(a1);
	}
	ARepresentation_item getItems2(ERepresentation type) throws SdaiException {
		return (ARepresentation_item)get_aggregate(a1);
	}
	public ARepresentation_item createItems(ERepresentation type) throws SdaiException {
		a1 = (ARepresentation_item)create_aggregate_class(a1, a1$,  ARepresentation_item.class, 0);
		return a1;
	}
	public void unsetItems(ERepresentation type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(ERepresentation type) throws SdaiException {
		return a1$;
	}
	// ENDOF taken from CRepresentation
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", jsdai.SRepresentation_schema.CRepresentation.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "datum_based_length_measure" attributes
		//measure
		setMeasure(context, this);

		//measure_orientation
		setMeasure_orientation(context, this);

		//of_datum
		setOf_datum(context, this);

		unsetMeasure(null);
		unsetMeasure_orientation(null);
		unsetOf_datum(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException
	{
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "datum_based_length_measure" attributes
		//measure
		unsetMeasure(context, this);

		//measure_orientation
		unsetMeasure_orientation(context, this);

		//of_datum
		unsetOf_datum(context, this);

	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	{representation 
	* 	(representation.name = `maximum positive component height') 
	* 	(representation.name = `maximum negative component height') 
	* 	(representation.name = `maximum mounting clearance') 
	* 	(representation.name = `minimum mounting clearance')} 
	*  end_mapping_constraints;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		//unset old data
		unsetMappingConstraints(context, armEntity);
		
		// CxManaged_design_object.setMappingConstraints(context, armEntity);
		// By default we just pick one of the strings
		armEntity.setName(null, "maximum positive component height");
	}

	/**
	* Unsets/deletes mapping constraint data.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMappingConstraints(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		// CxManaged_design_object.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName(null);
	}

	//********** "managed_design_object" attributes

	//********** "datum_based_length_measure" attributes

	private static void setItem(EDatum_based_length_measure armEntity,
			ERepresentation_item eri) throws SdaiException {
		ARepresentation_item items;
		if(armEntity instanceof CxDatum_based_length_measure){
			CxDatum_based_length_measure cx = (CxDatum_based_length_measure)armEntity;
			if(cx.testItems2(null))
				items = cx.getItems2(null);
			else
				items = cx.createItems(null);
			items.addUnordered(eri);
		}else{
			System.err.println("Unsupported subtype "+armEntity);
		}
	}

	/**
	* Sets/creates data for measure attribute.
	*
	* <p>
	*  attribute_mapping measure_orientation (measure_orientation
	* );
	* 	representation 
	* 	representation.items[i] -> 
	* 	{representation_item 
	* 	representation_item.name = `descriptive orientation'} 
	* 	representation_item => 
	* 	descriptive_representation_item 
	* 	{descriptive_representation_item 
	* 	(descriptive_representation_item.description = `normal') 
	* 	(descriptive_representation_item.description = `reversed')} 
	*  end_attribute_mapping;
	*  attribute_mapping measure_length_data_element (measure
	* , (*PATH*), length_data_element);
	* 	representation 
	* 	representation.items[i] -> 
	* 	{representation_item 
	* 	representation_item.name = `measure'} 
	* 	representation_item => 
	* 	measure_representation_item <= 
	* 	measure_with_unit => 
	* 	length_measure_with_unit 
	*  end_attribute_mapping;
	*  attribute_mapping measure_orientation_direction (measure_orientation
	* , (*PATH*), direction);
	* 	representation 
	* 	representation.items[i] -> 
	* 	{representation_item 
	* 	representation_item.name = `geometric orientation'} 
	* 	representation_item => 
	* 	geometric_representation_item => 
	* 	direction 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void setMeasure(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		//unset old values
		unsetMeasure(context, armEntity);
		
		if (armEntity.testMeasure(null))
		{ 
			ELength_measure_with_unit armMeasure = armEntity.getMeasure(null);
			EEntity aimLength = armMeasure;
			if (! (aimLength instanceof jsdai.SQualified_measure_schema.EMeasure_representation_item)) {
				aimLength = context.working_model.substituteInstance(aimLength,
					CLength_measure_with_unit$measure_representation_item.definition);
			}
			EMeasure_representation_item emri = (EMeasure_representation_item)
				aimLength;
			emri.setName(null, "measure");
			setItem(armEntity, emri);
		}
	}

	/**
	* Unsets/deletes data for measure attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetMeasure(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		if(armEntity instanceof CxDatum_based_length_measure){
			CxDatum_based_length_measure cx = (CxDatum_based_length_measure)armEntity;
			if(cx.testItems2(null)){
				ARepresentation_item items = cx.getItems2(null);
				for(int i=1;i<=items.getMemberCount();){
					ERepresentation_item item = items.getByIndex(i);
					if(item.getName(null).equals("measure"))
						items.removeByIndex(i);
					else
						i++;
				}
			}
		}
	}


	/**
	* Sets/creates data for measure_orientation attribute.
	*
	* <p>
	*  attribute_mapping measure_orientation (measure_orientation
	* );
	* 	representation 
	* 	representation.items[i] -> 
	* 	{representation_item 
	* 	representation_item.name = `descriptive orientation'} 
	* 	representation_item => 
	* 	descriptive_representation_item 
	* 	{descriptive_representation_item 
	* 	(descriptive_representation_item.description = `normal') 
	* 	(descriptive_representation_item.description = `reversed')} 
	*  end_attribute_mapping;
	*  attribute_mapping measure_orientation_direction (measure_orientation
	* , (*PATH*), direction);
	* 	representation 
	* 	representation.items[i] -> 
	* 	{representation_item 
	* 	representation_item.name = `geometric orientation'} 
	* 	representation_item => 
	* 	geometric_representation_item => 
	* 	direction 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void setMeasure_orientation(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		//unset old values
		unsetMeasure_orientation(context, armEntity);
		int result = armEntity.testMeasure_orientation(null);
		
		if (result != 0){ 
			jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
			ERepresentation_item item;
			// ENUM
			if(CDatum_based_length_measure.sMeasure_orientationMeasure_orientation == result){
				int measure = armEntity.getMeasure_orientation(null, null);
				String value = EMeasure_orientation.toString(measure).toLowerCase();
				// DRI
	       	LangUtils.Attribute_and_value_structure[] structure = {
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	jsdai.SQualified_measure_schema.CDescriptive_representation_item.attributeName(null), 
	   			   	"descriptive orientation"),
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	jsdai.SQualified_measure_schema.CDescriptive_representation_item.attributeDescription(null), 
	   					value)
	   			   };
	       	jsdai.SQualified_measure_schema.EDescriptive_representation_item
	   		   edri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item)
					LangUtils.createInstanceIfNeeded(context, jsdai.SQualified_measure_schema.CDescriptive_representation_item.definition, structure);
	       	item = edri;
			}
			// DIRECTION
			else{
				EEntity armValue = armEntity.getMeasure_orientation(null);
				EDirection edAIM = (EDirection)armValue;
				edAIM.setName(null, "geometric orientation");
				item = edAIM;
			}
			setItem(armEntity, item);
		}
	}


	/**
	* Unsets/deletes data for measure_orientation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetMeasure_orientation(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		if(armEntity instanceof CxDatum_based_length_measure){
			CxDatum_based_length_measure cx = (CxDatum_based_length_measure)armEntity;
			if(cx.testItems2(null)){
				ARepresentation_item items = cx.getItems2(null);
				for(int i=1;i<=items.getMemberCount();){
					ERepresentation_item item = items.getByIndex(i);
					if((item.getName(null).equals("geometric orientation"))||
							(item.getName(null).equals("descriptive orientation")))
						items.removeByIndex(i);
					else
						i++;
				}
			}
		}

	}


	/**
	* Sets/creates data for of_datum attribute.
	*
	* <p>
	*  attribute_mapping of_datum_datum_plane (of_datum
	* , (*PATH*), datum_plane);
	* 	representation <- 
	* 	property_definition_representation.used_representation 
	* 	property_definition_representation 
	* 	property_definition_representation.definition -> 
	* 	property_definition 
	* 	property_definition.definition -> 
	* 	characterized_definition 
	* 	characterized_definition = shape_definition 
	* 	shape_definition 
	* 	shape_definition = shape_aspect 
	* 	{shape_aspect 
	* 	shape_aspect.description = `plane'} 
	* 	shape_aspect => 
	* 	physical_unit_datum 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	// R <- PDR -> PD -> PUD
	public static void setOf_datum(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		//unset old values
		unsetOf_datum(context, armEntity);
		
		if (armEntity.testOf_datum(null))
		{ 
			jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
			EDatum_plane armOf_datum = armEntity.getOf_datum(null);
			// TODO armOf_datum.createAimData(context);
			// PD -> ACU
			LangUtils.Attribute_and_value_structure[] pdS = {
				new LangUtils.Attribute_and_value_structure(
					jsdai.SProduct_property_definition_schema.CProperty_definition.attributeDefinition(null),
					armOf_datum),
			};
			jsdai.SProduct_property_definition_schema.EProperty_definition pd =
				(jsdai.SProduct_property_definition_schema.EProperty_definition)
			LangUtils.createInstanceIfNeeded(context, jsdai.SProduct_property_definition_schema.CProperty_definition.definition,pdS);
			if(!pd.testName(null))
				pd.setName(null, "");
			//PDR->PD
			jsdai.SProduct_property_representation_schema.EProperty_definition_representation 
				pdr = (jsdai.SProduct_property_representation_schema.EProperty_definition_representation)
				context.working_model.createEntityInstance(jsdai.SProduct_property_representation_schema.CProperty_definition_representation.definition);
			pdr.setDefinition(null,pd);
			pdr.setUsed_representation(null, aimEntity);
	
		}
	}


	/**
	* Unsets/deletes data for of_datum attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetOf_datum(SdaiContext context, EDatum_based_length_measure armEntity) throws SdaiException
	{
		jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinUsed_representation(null, aimEntity, context.domain, apdr);
		for(int i=1;i<=apdr.getMemberCount();i++){
			EProperty_definition_representation epdr = apdr.getByIndex(i);
			EEntity definition = epdr.getDefinition(null);
			if(definition instanceof EProperty_definition){
				EProperty_definition epd = (EProperty_definition)definition;
				if(epd.getDefinition(null) instanceof EDatum_plane){
					epdr.deleteApplicationInstance();
				}
			}
		}
		
	}

}
