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

package jsdai.SLayered_interconnect_complex_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SFabrication_technology_mim.EStratum_technology_occurrence;
import jsdai.SFabrication_technology_xim.AStratum_technology_occurrence_armx;
import jsdai.SLayered_interconnect_complex_template_mim.CPhysical_unit_keepout_shape_allocation_to_stratum_stack;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation_item;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ERepresentation_relationship;

public class CxPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx extends CPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// FROM CRepresentation.java
	// Taken from CRepresentation.java
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

	// attribute (current explicit or supertype explicit) : context_of_items, base type: entity representation_context
/*	public static int usedinContext_of_items(ERepresentation type, ERepresentation_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testContext_of_items(ERepresentation type) throws SdaiException {
		return test_instance(a2);
	}
	public ERepresentation_context getContext_of_items(ERepresentation type) throws SdaiException {
		return (ERepresentation_context)get_instance(a2);
	}*/
	public void setContext_of_items(ERepresentation type, ERepresentation_context value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetContext_of_items(ERepresentation type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeContext_of_items(ERepresentation type) throws SdaiException {
		return a2$;
	}
	// Taken from Representation_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_relationship type) throws SdaiException {
		return test_string(a3);
	}
	public String getName(ERepresentation_relationship type) throws SdaiException {
		return get_string(a3);
	} */
	public void setName(ERepresentation_relationship type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(ERepresentation_relationship type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_relationship type) throws SdaiException {
		return a3$;
	}
	// ENDOF taken from Representation
	
	// methods for attribute: items, base type: SET OF ENTITY
/*	public static int usedinItems(ERepresentation type, ERepresentation_item instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}*/
	public boolean testItems2(ERepresentation type) throws SdaiException {
		return test_aggregate(a1);
	}
	public ARepresentation_item getItems2(ERepresentation type) throws SdaiException {
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
	// ENDOF FROM CRepresentation.java

	// FROM CRepresentation_relationship.java
	// attribute (current explicit or supertype explicit) : rep_2, base type: entity representation
/*	public static int usedinRep_2(ERepresentation_relationship type, ERepresentation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a6$, domain, result);
	}
	public boolean testRep_2(ERepresentation_relationship type) throws SdaiException {
		return test_instance(a6);
	}
	public ERepresentation getRep_2(ERepresentation_relationship type) throws SdaiException {
		return (ERepresentation)get_instance(a6);
	}*/
	public void setRep_2(ERepresentation_relationship type, ERepresentation value) throws SdaiException {
		a6 = set_instance(a6, value);
	}
	public void unsetRep_2(ERepresentation_relationship type) throws SdaiException {
		a6 = unset_instance(a6);
	}
	public static jsdai.dictionary.EAttribute attributeRep_2(ERepresentation_relationship type) throws SdaiException {
		return a6$;
	}
	// ENDOF FROM CRepresentation_relationship.java

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPhysical_unit_keepout_shape_allocation_to_stratum_stack.definition);

		setMappingConstraints(context, this);

        // stack_model : library_stack_model_armx;
		setStack_model(context, this);
		
		// swappable : BOOLEAN;
		setSwappable(context, this);
        
		// kept_out_layers : SET [1:?] OF stratum_technology_occurrence;
		setKept_out_layers(context, this);
		
		// clean ARM
		// shape_characterized_part_template
		unsetKept_out_layers(null);
		unsetStack_model(null);
		unsetSwappable(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetKept_out_layers(context, this);
		
        // stack_model : library_stack_model_armx;
		unsetStack_model(context, this);
		
		// swappable : BOOLEAN;
		unsetSwappable(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		part_template_keepout_shape_allocation_to_stratum_stack <=
		[representation]
		[representation_relationship]
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gaps
		armEntity.setName((ERepresentation)null, "");
		armEntity.setName((ERepresentation_relationship)null, "");
		if(armEntity.testKeepout_shape(null)){
			// Not sure which context really to take
			ERepresentation_context erc = armEntity.getKeepout_shape(null).getContext_of_items(null);
			armEntity.setContext_of_items(null, erc);
		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
	}	
	
	//********** "part_template_planar_shape" attributes

	/**
	* Sets/creates data for kept_out_layers attribute.
	* attribute_mapping kept_out_layers(kept_out_layers, $PATH, Stratum_technology_occurrence_armx);
		part_template_keepout_shape_allocation_to_stratum_stack <=
		representation <-
		representation_relationship.rep_1
		{representation_relationship.name = 'kept out layers'}
		representation_relationship.rep_2 ->
		representation <-
		property_definition_representation.used_representation
		property_definition_representation.definition ->
		property_definition =>
		stratum_technology_occurrence
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- RR ->R <- PDR -> STO
	public static void setKept_out_layers(SdaiContext context, EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		unsetKept_out_layers(context, armEntity);
		if(armEntity.testKept_out_layers(null)){
			AStratum_technology_occurrence_armx layers = armEntity.getKept_out_layers(null);
			SdaiIterator iter = layers.createIterator();
			while(iter.next()){
				EStratum_technology_occurrence layer = layers.getCurrentMember(iter);
		          // PDR
		          LangUtils.Attribute_and_value_structure[] pdrStructure = {
		             new LangUtils.Attribute_and_value_structure(
		             CProperty_definition_representation.attributeDefinition(null),
		             layer)
		          };
		          EProperty_definition_representation epdr = (EProperty_definition_representation)
		             LangUtils.createInstanceIfNeeded(context, CProperty_definition_representation.definition, pdrStructure);
		        ERepresentation er;  
				if(epdr.testUsed_representation(null)){
					er = epdr.getUsed_representation(null);
				}else{
					er = CxAP210ARMUtilities.createRepresentation(context, "", false);
					epdr.setUsed_representation(null, er);
					// dummy item to feel AIM gap
					ERepresentation_item eri = (ERepresentation_item)
						context.working_model.createEntityInstance(CRepresentation_item.definition);
					eri.setName(null, "");
					er.createItems(null).addUnordered(eri);
				}
		        ERepresentation_relationship err = (ERepresentation_relationship)
		        	context.working_model.createEntityInstance(CRepresentation_relationship.definition);
		        err.setRep_1(null, armEntity);
		        err.setRep_2(null, er);
		        err.setName(null, "kept out layers");
			}
		}
	}


	/**
	* Unsets/deletes data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetKept_out_layers(SdaiContext context, EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		ARepresentation_relationship arr = new ARepresentation_relationship();
		CRepresentation_relationship.usedinRep_1(null, armEntity, context.domain, arr);
		for(int i=1;i<=arr.getMemberCount();i++){
			ERepresentation_relationship err = arr.getByIndex(i);
			if((err.testName(null))&&(err.getName(null).equals("kept out layers"))){
				err.deleteApplicationInstance();
			}
		}
				
	}

	/**
	* Sets/creates data for setStack_model attribute.
	* attribute_mapping stack_model(stack_model, $PATH, Library_stack_model_armx);
		physical_unit_keepout_shape_allocation_to_stratum_stack <=
		representation_relationship
		representation_relationship.rep_2 ->
		representation <-
		property_definition_representation.used_representation
		property_definition_representation
		{property_definition_representation.description = 'stack model'}
		property_definition_representation.definition ->
		property_definition =>
		product_definition_shape =>
		part_template_definition =>
		stratum_stack_model =>
		library_stack_model
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // PUKSATSS -> R <- PDR -> LSM
	public static void setStack_model(SdaiContext context, EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		unsetStack_model(context, armEntity);
		if(armEntity.testStack_model(null)){
			ELibrary_stack_model_armx model = armEntity.getStack_model(null);
	          AProperty_definition_representation apdr = new AProperty_definition_representation();
	          CProperty_definition_representation.usedinDefinition(null, model, context.domain, apdr);
	          SdaiIterator iter = apdr.createIterator();
	          EProperty_definition_representation suitablePDR = null;
	          String name = "stack model";
	          while(iter.next()){
	        	  EProperty_definition_representation epdr = apdr.getCurrentMember(iter);
	        	  String description = 
	        		  CxAP210ARMUtilities.getProperty_definition_representationName(context, epdr);
	        	  if(name.equals(description)){
	        		  suitablePDR = epdr;
	        		  break;
	        	  }
	          }
	          if(suitablePDR == null){
	        	  suitablePDR = (EProperty_definition_representation)
	        	  	context.working_model.createEntityInstance(CProperty_definition_representation.definition);
	        	  	suitablePDR.setDefinition(null, model);
	        	  	CxAP210ARMUtilities.setProperty_definition_representationDescription(context, 
	        	  			suitablePDR, name);
	          }
	          
	        ERepresentation er;  
			if(suitablePDR.testUsed_representation(null)){
				er = suitablePDR.getUsed_representation(null);
			}else{
				er = CxAP210ARMUtilities.createRepresentation(context, "", false);
				// dummy item to feel AIM gap
				ERepresentation_item eri = (ERepresentation_item)
					context.working_model.createEntityInstance(CRepresentation_item.definition);
				eri.setName(null, "");
				er.createItems(null).addUnordered(eri);
				suitablePDR.setUsed_representation(null, er);
			}
			armEntity.setRep_2(null, er);
		}
	}


	/**
	* Unsets/deletes data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStack_model(SdaiContext context, EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		armEntity.unsetRep_2(null);
	}

	/**
	* Sets/creates data for kept_out_layers attribute.
	* attribute_mapping swappable(swappable, $PATH);
		physical_unit_keepout_shape_allocation_to_stratum_stack <=
		representation
		representation.items[1] ->
		representation_item
		{representation_item.name = 'swappable'}
		representation_item =>
		descriptive_representation_item
		{(descriptive_representation_item.description = 'TRUE')
		(descriptive_representation_item.description = 'FALSE')} 
		descriptive_representation_item.description
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // PUKSATSS -> DRI
	public static void setSwappable(SdaiContext context, CxPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		unsetSwappable(context, armEntity);
		if(armEntity.testSwappable(null)){
			boolean swappable = armEntity.getSwappable(null);
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, "swappable");
			if(swappable){
				edri.setDescription(null, "TRUE");
			}else{
				edri.setDescription(null, "FALSE");
			}
			ARepresentation_item items;
			if(armEntity.testItems2(null)){
				items = armEntity.getItems2(null);
			}else{
				items = armEntity.createItems(null);
			}
			items.addUnordered(edri);
			
		}
	}


	/**
	* Unsets/deletes data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetSwappable(SdaiContext context, CxPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx armEntity) throws SdaiException
	{
		if(!armEntity.testItems2(null)){
			return;
		}
		ARepresentation_item items = armEntity.getItems2(null);
		for(int i=1;i<=items.getMemberCount();i++){
			ERepresentation_item item = items.getByIndex(i);
			if(item instanceof EDescriptive_representation_item){
				item.deleteApplicationInstance();
			}
		}
		if(items.getMemberCount() == 0){
			armEntity.unsetItems(null);
		}
	}
	
	
}
