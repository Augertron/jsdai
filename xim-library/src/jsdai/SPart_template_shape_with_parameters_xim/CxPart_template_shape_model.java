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

package jsdai.SPart_template_shape_with_parameters_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPart_template_xim.ETemplate_definition;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_parameters_xim.*;

public class CxPart_template_shape_model extends CPart_template_shape_model implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

		setMappingConstraints(context, this);

		// shape_characterized_part_template
		setShape_characterized_definition(context, this);
		
      // shape_environment
		setShape_environment(context, this);
		
      // shape_material_condition 
		setShape_material_condition(context, this);

		// clean ARM
		// shape_characterized_part_template
		unsetShape_characterized_definition(null);
		
      // shape_environment
		unsetShape_environment(null);
		
      // shape_material_condition 
		unsetShape_material_condition(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// shape_characterized_part_template
		unsetShape_characterized_definition(context, this);
		
      // shape_environment
		unsetShape_environment(context, this);
		
      // shape_material_condition 
		unsetShape_material_condition(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	*  representation &lt;-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  property_definition =&gt;
	*  product_definition_shape =&gt;
	*  part_template_definition}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
	}	
	
	//********** "part_template_planar_shape" attributes

	/**
	* Sets/creates data for shape_characterized_part_template attribute.
	*
	* <p>
	*  attribute_mapping shape_characterized_part_template_part_template (shape_characterized_part_template
	* , (*PATH*), part_template);
	* 	shape_representation <=
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
	* 	shape_aspect =>
	* 	part_template_definition
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	* Path: SR <- PDR -> PD -> PTD
	*/
	public static void setShape_characterized_definition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		//unset old values
		unsetShape_characterized_definition(context, armEntity);

		if (armEntity.testShape_characterized_definition(null))
		{

			AGeometric_template_armx templates = armEntity.getShape_characterized_definition(null);
			SdaiIterator iter = templates.createIterator();
			while(iter.next()){
				EGeometric_template_armx template = templates.getCurrentMember(iter);
				// PTD
				// SR <- PDR -> PD
				EShape_definition_representation epdr = (EShape_definition_representation)
					context.working_model.createEntityInstance(CShape_definition_representation.definition); 
				epdr.setDefinition(null, template);
				epdr.setUsed_representation(null, armEntity);
			}
		}
	}


	/**
	* Unsets/deletes data for shape_characterized_part_template attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	* SR <- PDR -> PD -> PTD, remove all PDRs
	*/
	public static void unsetShape_characterized_definition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		AProperty_definition_representation relationships = new AProperty_definition_representation();
		CProperty_definition_representation.usedinUsed_representation(null,
			armEntity, context.domain, relationships);
      // More checking is needed, because the same mapping is used
      // for shape_characterized_assembly_component, which might used
      // in the case of complex AC2DS+PTPS
      SdaiIterator iterator = relationships.createIterator();
		while (iterator.next()){
			EProperty_definition_representation relationship = relationships.getCurrentMember(iterator);
         if((relationship.testDefinition(null))&&
            (relationship.getDefinition(null) instanceof ETemplate_definition)){
               relationship.deleteApplicationInstance();
         }
         // i++;
		}

	}


	/**
	* Sets/creates data for shape_environment attribute.
	*
	* <p>
	*  attribute_mapping shape_environment (shape_environment
	* , descriptive_representation_item);
	* 	shape_representation <=
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	{representation.name = `shape environment'}
	* 	representation.items[i] ->
	* 	representation_item
	* 	{(representation_item.name = `manufacturing')
	* 	(representation_item.name = `end user application')}
	* 	representation_item =>
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- RR -> R -> DRI
	public static void setShape_environment(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		//unset old values
		unsetShape_environment(context, armEntity);

		if (armEntity.testShape_environment(null))
		{
			int armShape_environment = armEntity.getShape_environment(null);
         // R, search by hand, since we need a combination
         jsdai.SRepresentation_schema.ERepresentation repAIM = null;
         top: for(int i=1;i<=context.domain.getMemberCount();i++){
            SdaiModel model = context.domain.getByIndex(i);
            AEntity ar = model.getEntityExtentInstances(CRepresentation.definition);
            for(int j=1;j<=ar.getMemberCount();j++){
               jsdai.SRepresentation_schema.ERepresentation er = (jsdai.SRepresentation_schema.ERepresentation)ar.getByIndexEntity(j);
               if((er.testName(null))&&(er.getName(null).equals("shape environment"))&&
                  er.testItems(null)){
                  ARepresentation_item items = er.getItems(null);
                  for(int k=1;k<=items.getMemberCount();k++){
                     ERepresentation_item item = items.getByIndex(k);
                     if(item instanceof EDescriptive_representation_item){
                        if((item.getName(null).equals("manufacturing"))&&
                           (EApplication_environment.MANUFACTURING == armShape_environment)){
                           repAIM = er;
                           break top;
                        }
                        if((item.getName(null).equals("end user application"))&&
                           (EApplication_environment.END_USER_APPLICATION == armShape_environment)){
                           repAIM = er;
                           break top;
                        }
                     }
                  }
               }
            }
         }
         // Create a new
         if(repAIM == null){
            // R
            repAIM = (jsdai.SRepresentation_schema.ERepresentation)
               context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
            repAIM.setName(null, "shape environment");
            // Context
            jsdai.SRepresentation_schema.ERepresentation_context representation_context =
               CxAP210ARMUtilities.createRepresentation_context(context,
                                                                          "", "", true);
            repAIM.setContext_of_items(null, representation_context);
            // DRI
            EDescriptive_representation_item edri = (EDescriptive_representation_item)
               context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
            edri.setName(null, EApplication_environment.toString(armShape_environment).toLowerCase().replace('_',' '));
            edri.setDescription(null, "");
            repAIM.createItems(null).addUnordered(edri);
         }
         // SR <- RR -> R
         ERepresentation_relationship err = (ERepresentation_relationship)
            context.working_model.createEntityInstance(CRepresentation_relationship.definition);
         err.setRep_1(null, armEntity);
         err.setRep_2(null, repAIM);
         err.setName(null, "");
		}
	}


	/**
	* Unsets/deletes data for shape_environment attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetShape_environment(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
      ARepresentation_relationship arr = new ARepresentation_relationship();
      CRepresentation_relationship.usedinRep_1(null, armEntity, context.domain, arr);
      SdaiIterator iterator = arr.createIterator();
      while (iterator.next()){
         ERepresentation_relationship err = arr.getCurrentMember(iterator);
         if(err.testRep_2(null)){
            ERepresentation er = err.getRep_2(null);
            // security check in order to avoid various subtypes with hidden attributes
            if(er.getInstanceType() != CRepresentation.definition){
            	continue;
            }
            if((er.testName(null))&&(er.getName(null).equals("shape environment"))){
               // arr.removeByIndex(i);
               err.deleteApplicationInstance();
            }
            // else
               // i++;
         }
         // else
            // i++;
      }
	}


	/**
	* Sets/creates data for shape_material_condition attribute.
	*
	* <p>
	*  attribute_mapping shape_material_condition (shape_material_condition
	* , descriptive_representation_item);
	* 	shape_representation <=
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	{representation.name = `shape material condition'}
	* 	representation.items[i] ->
	* 	representation_item
	* 	{(representation_item.name = `maximum material condition')
	* 	(representation_item.name = `minimum material condition')
	* 	(representation_item.name = `nominal material condition')}
	* 	representation_item =>
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- RR ->R -> DRI
	public static void setShape_material_condition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
		//System.err.println("PTPSs.shape_material_condition"+" AIM "+armEntity.getTemp("AIM")+" test "+armEntity.testShape_material_condition(null));
		//unset old values
		unsetShape_material_condition(context, armEntity);

		if (armEntity.testShape_material_condition(null))
		{

			int armShape_material_condition = armEntity.getShape_material_condition(null);
         // R, search by hand, since we need a combination
         jsdai.SRepresentation_schema.ERepresentation repAIM = null;
         top: for(int i=1, n1=context.domain.getMemberCount();i<=n1;i++){
            SdaiModel model = context.domain.getByIndex(i);
            AEntity ar = model.getEntityExtentInstances(CRepresentation.definition);
            for(int j=1, n2=ar.getMemberCount();j<=n2;j++){
               jsdai.SRepresentation_schema.ERepresentation er = (jsdai.SRepresentation_schema.ERepresentation)ar.getByIndexEntity(j);
               if((er.testName(null))&&(er.getName(null).equals("shape material condition"))&&
                  er.testItems(null)){
                  ARepresentation_item items = er.getItems(null);
                  for(int k=1, n3=items.getMemberCount();k<=n3;k++){
                     ERepresentation_item item = items.getByIndex(k);
                     if(item instanceof EDescriptive_representation_item){
                        if((item.getName(null).equals("maximum material condition"))&&
                           (EMaterial_condition.MAXIMUM_MATERIAL_CONDITION == armShape_material_condition)){
                           repAIM = er;
                           break top;
                        }
                        if((item.getName(null).equals("minimum material condition"))&&
                           (EMaterial_condition.MINIMUM_MATERIAL_CONDITION == armShape_material_condition)){
                           repAIM = er;
                           break top;
                        }
                        if((item.getName(null).equals("nominal material condition"))&&
                           (EMaterial_condition.NOMINAL_MATERIAL_CONDITION == armShape_material_condition)){
                           repAIM = er;
                           break top;
                        }
                     }
                  }
               }
            }
         }
         // Create a new
         if(repAIM == null){
            // R
            repAIM = (jsdai.SRepresentation_schema.ERepresentation)
               context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
            repAIM.setName(null, "shape material condition");
            // Context
            jsdai.SRepresentation_schema.ERepresentation_context representation_context =
               CxAP210ARMUtilities.createRepresentation_context(context,
                                                                          "", "", true);
            repAIM.setContext_of_items(null, representation_context);
            // DRI
            EDescriptive_representation_item edri = (EDescriptive_representation_item)
               context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
            edri.setName(null, EMaterial_condition.toString(armShape_material_condition).toLowerCase().replace('_',' '));
            edri.setDescription(null, "");
            repAIM.createItems(null).addUnordered(edri);
         }
         // SR <- RR -> R
         ERepresentation_relationship err = (ERepresentation_relationship)
            context.working_model.createEntityInstance(CRepresentation_relationship.definition);
         err.setRep_1(null, armEntity);
         err.setRep_2(null, repAIM);
         err.setName(null, "");
		 //System.err.println("PTPSs.shape_material_condition LINK "+err+" to "+repAIM);
		}
	}


	/**
	* Unsets/deletes data for shape_material_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetShape_material_condition(SdaiContext context, EPart_template_shape_model armEntity) throws SdaiException
	{
      ARepresentation_relationship arr = new ARepresentation_relationship();
      CRepresentation_relationship.usedinRep_1(null, armEntity, context.domain, arr);
      SdaiIterator iterator = arr.createIterator();
      while (iterator.next()){
         ERepresentation_relationship err = arr.getCurrentMember(iterator);
         if(err.testRep_2(null)){
            ERepresentation er = err.getRep_2(null);
            // security check in order to avoid various subtypes with hidden attributes
            if(er.getInstanceType() != CRepresentation.definition){
            	continue;
            }
            if((er.testName(null))&&(er.getName(null).equals("shape material condition"))){
               // arr.removeByIndex(i);
               err.deleteApplicationInstance();
            }
            // else
            //   i++;
         }
         //else
         //   i++;
      }

	}
	
	
}
