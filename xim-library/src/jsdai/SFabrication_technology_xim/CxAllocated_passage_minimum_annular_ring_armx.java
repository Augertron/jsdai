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

package jsdai.SFabrication_technology_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFabrication_technology_mim.CAllocated_passage_minimum_annular_ring;
import jsdai.SFabrication_technology_xim.EStratum_technology_occurrence_armx;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SMixed_complex_types.*;

public class CxAllocated_passage_minimum_annular_ring_armx extends CAllocated_passage_minimum_annular_ring_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAllocated_passage_minimum_annular_ring.definition);

		setMappingConstraints(context, this);
		

		// supported_external_minimum_annular_ring   : OPTIONAL length_measure_with_unit;
		setSupported_external_minimum_annular_ring(context, this);
		
        // supported_internal_minimum_annular_ring : OPTIONAL length_measure_with_unit;
		setSupported_internal_minimum_annular_ring(context, this);
		
        // associated_passage_allocation : passage_technology_allocation_to_stack_model_armx;
		setAssociated_passage_allocation(context, this);
		
        // associated_stratum_technology_occurrence : stratum_technology_occurrence_armx;
		setAssociated_stratum_technology_occurrence(context, this);

	    // unsupported_minimum_annular_ring          : OPTIONAL length_measure_with_unit;
		setUnsupported_minimum_annular_ring(context, this);
		
	    // minimum_fabrication_allowance             : OPTIONAL length_measure_with_unit;	   
		setMinimum_fabrication_allowance(context, this);		
		
		
		// Clean ARM
	    // Supported_external_minimum_annular_ring : OPTIONAL length_measure_with_unit;
		unsetSupported_external_minimum_annular_ring(null);
		
        // Supported_internal_minimum_annular_ring : OPTIONAL length_measure_with_unit;
		unsetSupported_internal_minimum_annular_ring(null);
		
        // associated_passage_allocation : passage_technology_allocation_to_stack_model_armx;
		unsetAssociated_passage_allocation(null);
		
        // associated_stratum_technology_occurrence : stratum_technology_occurrence_armx;
		unsetAssociated_stratum_technology_occurrence(null);

	    // unsupported_minimum_annular_ring          : OPTIONAL length_measure_with_unit;
		unsetUnsupported_minimum_annular_ring(null);
		
	    // minimum_fabrication_allowance             : OPTIONAL length_measure_with_unit;	   
		unsetMinimum_fabrication_allowance(null);		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// as_finished_inter_stratum_extent 		
		// unsetAs_finished_inter_stratum_extent(context, this);
		
	    // Supported_external_minimum_annular_ring : OPTIONAL length_measure_with_unit;
		unsetSupported_external_minimum_annular_ring(context, this);
		
        // Supported_internal_minimum_annular_ring : OPTIONAL length_measure_with_unit;
		unsetSupported_internal_minimum_annular_ring(context, this);
		
        // associated_passage_allocation : passage_technology_allocation_to_stack_model_armx;
		unsetAssociated_passage_allocation(context, this);
		
        // associated_stratum_technology_occurrence : stratum_technology_occurrence_armx;
		unsetAssociated_stratum_technology_occurrence(context, this);

	    // unsupported_minimum_annular_ring          : OPTIONAL length_measure_with_unit;
		unsetUnsupported_minimum_annular_ring(context, this);
		
	    // minimum_fabrication_allowance             : OPTIONAL length_measure_with_unit;	   
		unsetMinimum_fabrication_allowance(context, this);		
	}
	
   public static void cleanAIM_stuff(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
   SdaiException {
	   // SPECIFICS - remove all users, since they will become invalid anyway
	   // 1) PD
	   AProperty_definition users = new AProperty_definition();
	   CProperty_definition.usedinDefinition(null, armEntity, context.domain, users);
	   for (int i = 1; i <= users.getMemberCount(); ) {
	      EProperty_definition user = users.getByIndex(i);
	      AProperty_definition_representation users2 = new AProperty_definition_representation();
	      CProperty_definition_representation.usedinDefinition(null, user,
	                                                           context.domain, users2);
	      for (int j = 1; j <= users2.getMemberCount(); ) {
	         EProperty_definition_representation user2 = users2.getByIndex(j);
	         // stuff needed for deletion of representation
	         ERepresentation er = user2.getUsed_representation(null);
	         users2.removeByIndex(j);
	         if ( (!er.testItems(null)) || (er.getItems(null).getMemberCount() == 0)) {
	            AEntity users3 = new AEntity();
	            er.findEntityInstanceUsers(context.domain, users3);
	            // Representation is used only from this passage_technology,
	            // which is going to be removed.
	            if (users3.getMemberCount() == 1) {
	               er.deleteApplicationInstance();
	            }
	         }
	         // delete PDR
	         user2.deleteApplicationInstance();
	      }
	      users.removeByIndex(i);
	      user.deleteApplicationInstance();
	   }
	   // 2) SARs and all remaining ones
	   AEntity allUsers = new AEntity();
	   armEntity.findEntityInstanceUsers(context.domain, allUsers);
	   for (int i = 1; i <= allUsers.getMemberCount(); ) {
	      EEntity user = allUsers.getByIndexEntity(i);
	      // only this we can remove safely
	      if (user instanceof EShape_aspect_relationship) {
	         allUsers.removeByIndex(i);
	         user.deleteApplicationInstance();
	      }
	      else {
	         i++;
	      }
	   }
   }
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		allocated_passage_minimum_annular_ring <=
		characterized_object
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws SdaiException
	{
	}

   //********** "Allocated_passage_minimum_annular_ring_armx" attributes

 /**
  * Sets/creates data for as_finished_passage_extent attribute.
  *
  * <p>
	attribute_mapping Supported_external_minimum_annular_ring(Supported_external_minimum_annular_ring, $PATH, length_measure_with_unit);
		allocated_passage_minimum_annular_ring <=
		characterized_object
		characterized_definition = characterized_object
		characterized_definition <-
		property_definition.definition
		{property_definition.name = 'supported external minimum annular ring'}
		property_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation
		representation.items[i] ->
		representation_item =>
		measure_representation_item <=
		measure_with_unit =>
		length_measure_with_unit
	end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setSupported_external_minimum_annular_ring(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
    //unset old values
    unsetSupported_external_minimum_annular_ring(context, armEntity);

    if (armEntity.testSupported_external_minimum_annular_ring(null)) {
      ELength_measure_with_unit elmwu = armEntity.getSupported_external_minimum_annular_ring(null);
      ERepresentation_item item;
      if(elmwu instanceof ERepresentation_item){
    	  item = (ERepresentation_item)elmwu;
      }else{
    	  item = (ERepresentation_item)
    	  	context.working_model.substituteInstance(elmwu, CLength_measure_with_unit$measure_representation_item.definition); 
      }
      ARepresentation reps = new ARepresentation();
      CRepresentation.usedinItems(null, item, context.domain, reps);
      ERepresentation rep = null;
      if(reps.getMemberCount() > 0){
    	  rep = reps.getByIndex(1);
      }else{
    	  rep = CxAP210ARMUtilities.createRepresentation(context, CRepresentation.definition, "", false);
    	  rep.createItems(null).addUnordered(item);
      }
      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "supported external minimum annular ring", rep);
    }
 }

 /**
  * Unsets/deletes data for Supported_external_minimum_annular_ring attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetSupported_external_minimum_annular_ring(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
 	CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "supported external minimum annular ring");
 }

 public static void setUnsupported_minimum_annular_ring(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
 SdaiException {
 //unset old values
 unsetUnsupported_minimum_annular_ring(context, armEntity);

 if (armEntity.testUnsupported_minimum_annular_ring(null)) {
   ELength_measure_with_unit elmwu = armEntity.getUnsupported_minimum_annular_ring(null);
   ERepresentation_item item;
   if(elmwu instanceof ERepresentation_item){
 	  item = (ERepresentation_item)elmwu;
   }else{
 	  item = (ERepresentation_item)
 	  	context.working_model.substituteInstance(elmwu, CLength_measure_with_unit$measure_representation_item.definition); 
   }
   ARepresentation reps = new ARepresentation();
   CRepresentation.usedinItems(null, item, context.domain, reps);
   ERepresentation rep = null;
   if(reps.getMemberCount() > 0){
 	  rep = reps.getByIndex(1);
   }else{
 	  rep = CxAP210ARMUtilities.createRepresentation(context, CRepresentation.definition, "", false);
 	  rep.createItems(null).addUnordered(item);
   }
   CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "unsupported minimum annular ring", rep);
 }
}

/**
* Unsets/deletes data for unsupported minimum annular ring attribute.
*
* @param context SdaiContext.
* @param armEntity arm entity.
* @throws SdaiException
*/
public static void unsetUnsupported_minimum_annular_ring(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
 SdaiException {
	CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "unsupported minimum annular ring");
}
 
 /**
  * Sets/creates data for Supported_internal_minimum_annular_ring attribute.
  *
  * <p>
	attribute_mapping Supported_internal_minimum_annular_ring(Supported_internal_minimum_annular_ring, $PATH, length_measure_with_unit);
		allocated_passage_minimum_annular_ring <=
		characterized_object
		characterized_definition = characterized_object
		characterized_definition <-
		property_definition.definition
		{property_definition.name = 'supported internal minimum annular ring'}
		property_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation
		representation.items[i] ->
		representation_item =>
		measure_representation_item <=
		measure_with_unit =>
		length_measure_with_unit
	end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setSupported_internal_minimum_annular_ring(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
    //unset old values
    unsetSupported_internal_minimum_annular_ring(context, armEntity);

    if (armEntity.testSupported_internal_minimum_annular_ring(null)) {
      ELength_measure_with_unit elmwu = armEntity.getSupported_internal_minimum_annular_ring(null);
      ERepresentation_item item;
      if(elmwu instanceof ERepresentation_item){
    	  item = (ERepresentation_item)elmwu;
      }else{
    	  item = (ERepresentation_item)
    	  	context.working_model.substituteInstance(elmwu, CLength_measure_with_unit$measure_representation_item.definition); 
      }
      ARepresentation reps = new ARepresentation();
      CRepresentation.usedinItems(null, item, context.domain, reps);
      ERepresentation rep = null;
      if(reps.getMemberCount() > 0){
    	  rep = reps.getByIndex(1);
      }else{
    	  rep = CxAP210ARMUtilities.createRepresentation(context, CRepresentation.definition, "", false);
    	  rep.createItems(null).addUnordered(item);
      }
      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "supported internal minimum annular ring", rep);
    }
 }

 /**
  * Unsets/deletes data for Supported_internal_minimum_annular_ring attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetSupported_internal_minimum_annular_ring(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
 	CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "supported internal minimum annular ring");
 }

 /**
  * Sets/creates data for associated_passage_allocation attribute.
  *
  * <p>
	attribute_mapping associated_passage_allocation(associated_passage_allocation, $PATH, Passage_technology_allocation_to_stack_model_armx);
		allocated_passage_minimum_annular_ring <=
		characterized_object
		characterized_definition = characterized_object
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'associated passage allocation'}
		property_definition_relationship.relating_property_definition ->
		property_definition =>
		product_definition_shape =>
		part_template_definition =>
		stratum_sub_stack =>
		passage_technology_allocation_to_stack_model
	end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setAssociated_passage_allocation(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
    //unset old values
	unsetAssociated_passage_allocation(context, armEntity);

    if (armEntity.testAssociated_passage_allocation(null)) {
      APassage_technology_allocation_to_stack_model_armx aptatsm = armEntity.getAssociated_passage_allocation(null);
      SdaiIterator iter = aptatsm.createIterator();
      while(iter.next()){
    	  EPassage_technology_allocation_to_stack_model_armx eptatsm = aptatsm.getCurrentMember(iter);  
	      // PD
	      AProperty_definition apd = new AProperty_definition();
	      CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
	      EProperty_definition epd;
	      if(apd.getMemberCount() > 0){
	    	  epd = apd.getByIndex(1);
	      }else{
	    	  epd = (EProperty_definition)context.working_model.createEntityInstance(CProperty_definition.definition);
	    	  epd.setDefinition(null, armEntity);
	      }
	      if(!epd.testName(null)){
	    	  epd.setName(null, "");
	      }
	      EProperty_definition_relationship epdr = (EProperty_definition_relationship)
	      	context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
	      epdr.setRelated_property_definition(null, epd);
	      epdr.setName(null, "associated passage allocation");
	      epdr.setRelating_property_definition(null, eptatsm);
      }
    }
 }

 /**
  * Unsets/deletes data for associated_passage_allocation attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetAssociated_passage_allocation(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
     AProperty_definition apd = new AProperty_definition();
     CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
     for(int i=1,countPD=apd.getMemberCount(); i<=countPD;i++){
    	 EProperty_definition epd = apd.getByIndex(i);      
         AProperty_definition_relationship apdr = new AProperty_definition_relationship();
         CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apd);
         for(int j=1,countPDR=apdr.getMemberCount(); j<=countPDR;j++){
        	 EProperty_definition_relationship epdr = apdr.getByIndex(j);
        	 if((epdr.testName(null))&&(epdr.getName(null).equals("associated passage allocation"))){
        		 epdr.deleteApplicationInstance();
        	 }
         }
     }
 }

 /**
  * Sets/creates data for associated_stratum_technology_occurrence attribute.
  *
  * <p>
	attribute_mapping associated_stratum_technology_occurrence(associated_stratum_technology_occurrence, $PATH, Stratum_technology_occurrence_armx);
		allocated_passage_minimum_annular_ring <=
		characterized_object
		characterized_definition = characterized_object
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'associated stratum technology occurrence'}
		property_definition_relationship.relating_property_definition ->
		property_definition =>
		stratum_technology_occurrence
	end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setAssociated_stratum_technology_occurrence(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
    //unset old values
	 unsetAssociated_stratum_technology_occurrence(context, armEntity);

    if (armEntity.testAssociated_stratum_technology_occurrence(null)) {
    	EStratum_technology_occurrence_armx estoa = armEntity.getAssociated_stratum_technology_occurrence(null);
      // PD
      AProperty_definition apd = new AProperty_definition();
      CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
      EProperty_definition epd;
      if(apd.getMemberCount() > 0){
    	  epd = apd.getByIndex(1);
      }else{
    	  epd = (EProperty_definition)context.working_model.createEntityInstance(CProperty_definition.definition);
    	  epd.setDefinition(null, armEntity);
      }
      if(!epd.testName(null)){
    	  epd.setName(null, "");
      }
      EProperty_definition_relationship epdr = (EProperty_definition_relationship)
      	context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
      epdr.setRelated_property_definition(null, epd);
      epdr.setName(null, "associated stratum technology occurrence");
      epdr.setRelating_property_definition(null, estoa);
    }
 }

 /**
  * Unsets/deletes data for associated_stratum_technology_occurrence attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetAssociated_stratum_technology_occurrence(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
    SdaiException {
     AProperty_definition apd = new AProperty_definition();
     CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
     for(int i=1,countPD=apd.getMemberCount(); i<=countPD;i++){
    	 EProperty_definition epd = apd.getByIndex(i);      
         AProperty_definition_relationship apdr = new AProperty_definition_relationship();
         CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apdr);
         for(int j=1,countPDR=apdr.getMemberCount(); j<=countPDR;j++){
        	 EProperty_definition_relationship epdr = apdr.getByIndex(j);
        	 if((epdr.testName(null))&&(epdr.getName(null).equals("associated stratum technology occurrence"))){
        		 epdr.deleteApplicationInstance();
        	 }
         }
     }
 } 

 public static void setMinimum_fabrication_allowance(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
 SdaiException {
 //unset old values
 unsetMinimum_fabrication_allowance(context, armEntity);

 if (armEntity.testMinimum_fabrication_allowance(null)) {
   ELength_measure_with_unit elmwu = armEntity.getMinimum_fabrication_allowance(null);
   ERepresentation_item item;
   if(elmwu instanceof ERepresentation_item){
 	  item = (ERepresentation_item)elmwu;
   }else{
 	  item = (ERepresentation_item)
 	  	context.working_model.substituteInstance(elmwu, CLength_measure_with_unit$measure_representation_item.definition); 
   }
   ARepresentation reps = new ARepresentation();
   CRepresentation.usedinItems(null, item, context.domain, reps);
   ERepresentation rep = null;
   if(reps.getMemberCount() > 0){
 	  rep = reps.getByIndex(1);
   }else{
 	  rep = CxAP210ARMUtilities.createRepresentation(context, CRepresentation.definition, "", false);
 	  rep.createItems(null).addUnordered(item);
   }
   CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "minimum fabrication allowance", rep);
 }
}

/**
* Unsets/deletes data for minimum fabrication allowance attribute.
*
* @param context SdaiContext.
* @param armEntity arm entity.
* @throws SdaiException
*/
public static void unsetMinimum_fabrication_allowance(SdaiContext context, EAllocated_passage_minimum_annular_ring_armx armEntity) throws
 SdaiException {
	CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "minimum fabrication allowance");
}

}
