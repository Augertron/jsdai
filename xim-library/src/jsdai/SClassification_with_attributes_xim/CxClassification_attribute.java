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

package jsdai.SClassification_with_attributes_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_property_definition_schema.AGeneral_property_association;
import jsdai.SProduct_property_definition_schema.CGeneral_property_association;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EGeneral_property;
import jsdai.SProduct_property_definition_schema.EGeneral_property_association;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SQualified_measure_schema.AMeasure_representation_item;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CCompound_representation_item;
import jsdai.SRepresentation_schema.ECompound_representation_item;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ESet_representation_item;

public class CxClassification_attribute extends CClassification_attribute implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProperty_definition.definition);

		setMappingConstraints(context, this);

		// Definitional
		setAllowed_values(context, this);

		// id_x
		setId_x(context, this);
		
		// Attribute_definition
		setAttribute_definition(context, this);

		// clean ARM
		// version_id
		unsetAllowed_values(null);
		
		unsetId_x(null);
		
		unsetAttribute_definition(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// version_id
		unsetAllowed_values(context, this);
	
		// id_x
		unsetId_x(context, this);
		
		// Attribute_definition
		unsetAttribute_definition(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  
		{property_definition
		derived_property_select = property_definition
		derived_property_select <- 
		general_property_association.derived_definition
		general_property_association.base_definition -> 
		general_property}
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// Constraints are covered in attribute - 'attribute_definition'
	public static void setMappingConstraints(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for attribute allowed_values.
	*
	* <p>
	* attribute_mapping allowed_values(allowed_values, $PATH, measure_representation_item);
		represented_definition = property_definition
		represented_definition <- property_definition_representation.definition
		{property_definition_representation.name='allowed values'}
		property_definition_representation.used_representation -> representation
		representation.items[i] -> representation_item
		representation_item => compound_representation_item
		compound_representation_item.item_element -> compound_item_definition
		compound_item_definition = set_representation_item
		set_representation_item[i] -> representation_item
		representation_item => measure_representation_item
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PD <- PDR -> R -> CRI -> MRI
	public static void setAllowed_values(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
		unsetAllowed_values(context, armEntity);
		if(armEntity.testAllowed_values(null)){
			AMeasure_representation_item amri = armEntity.getAllowed_values(null);
			// EPDR
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, armEntity);
			CxAP210ARMUtilities.setProperty_definition_representationName(context, epdr, "allowed values");
			// R
			ERepresentation er = CxAP210ARMUtilities.createRepresentation(context, "", false);
			epdr.setUsed_representation(null, er);
			ARepresentation_item items; 
			if(er.testItems(null)){
				items = er.getItems(null);
			}else{
				items = er.createItems(null);
			}
			// CRI
			ECompound_representation_item ecri = (ECompound_representation_item)
				context.working_model.createEntityInstance(CCompound_representation_item.definition);
			ecri.setName(null, "");
			items.addUnordered(ecri);
			// CRI -> MRI
			ARepresentation_item criItems = ecri.createItem_element(null, (ESet_representation_item)null);
			SdaiIterator iter = amri.createIterator();
			while(iter.next()){
				EMeasure_representation_item emri = amri.getCurrentMember(iter);
				criItems.addUnordered(emri);
			}
			
		}
	}

	public static void unsetAllowed_values(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, apdr);
		for(int i=1;i<=apdr.getMemberCount();i++){
			EProperty_definition_representation epdr = apdr.getByIndex(i);
			String epdrName = CxAP210ARMUtilities.getProperty_definition_representationName(context, epdr);
			if((epdrName != null)&&(epdrName.equals("allowed values"))){
				ERepresentation er = epdr.getUsed_representation(null);
				epdr.deleteApplicationInstance();
				if(CxAP210ARMUtilities.countEntityUsers(context, er) == 0){
					ARepresentation_item items = er.getItems(null);
					ERepresentation_context erc = er.getContext_of_items(null);
					er.deleteApplicationInstance();
					if(CxAP210ARMUtilities.countEntityUsers(context, er) == 0){
						erc.deleteApplicationInstance();
					}
					for(int j=1;j<=items.getMemberCount();j++){
						ERepresentation_item item = items.getByIndex(j);
						if(item instanceof ECompound_representation_item){
							item.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	* Sets/creates data for attribute allowed_values.
	*
	* <p>
	* attribute_mapping attribute_definition(attribute_definition, $PATH, general_property);
		property_definition
		derived_property_select = property_definition
		derived_property_select <-
		general_property_association.derived_definition
		general_property_association
		{general_property_association.name = 'attribute definition'}
		general_property_association.base_definition ->
		general_property
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PD <- GPA -> GP
	public static void setAttribute_definition(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
		unsetAttribute_definition(context, armEntity);
		if(armEntity.testAttribute_definition(null)){
			EGeneral_property egp = armEntity.getAttribute_definition(null);
			
			// GPA
			EGeneral_property_association egpa = (EGeneral_property_association)
				context.working_model.createEntityInstance(CGeneral_property_association.definition);
			egpa.setDerived_definition(null, armEntity);
			egpa.setName(null, "attribute definition");
			egpa.setBase_definition(null, egp);
		}
	}

	public static void unsetAttribute_definition(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
		AGeneral_property_association agpa = new AGeneral_property_association();
		CGeneral_property_association.usedinDerived_definition(null, armEntity, context.domain, agpa);
		for(int i=1;i<=agpa.getMemberCount();i++){
			EGeneral_property_association egpa = agpa.getByIndex(i);
			if((egpa.testName(null))&&(egpa.getName(null).equals("attribute definition"))){
				egpa.deleteApplicationInstance();
			}
		}
	}

	/**
	* Sets/creates data for attribute id_x.
	*
	* <p>
	* attribute_mapping id_x(id_x, id_attribute.attribute_value);
		property_definition = id_attribute_select 
		id_attribute_select <- id_attribute.identified_item
		id_attribute.attribute_value
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PD <- GPA -> GP
	public static void setId_x(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
		unsetId_x(context, armEntity);
		if(armEntity.testId_x(null)){
			String id_x = armEntity.getId_x(null);
			CxAP210ARMUtilities.setProperty_definitionId(context, armEntity, id_x);
		}
	}

	public static void unsetId_x(SdaiContext context, EClassification_attribute armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetDerivedName(context, armEntity);
	}
	
}
