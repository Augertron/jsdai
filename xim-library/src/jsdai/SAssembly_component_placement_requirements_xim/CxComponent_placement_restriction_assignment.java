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

package jsdai.SAssembly_component_placement_requirements_xim;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SAssembly_component_placement_requirements_mim.EItem_restricted_requirement;
import jsdai.SAssembly_component_placement_requirements_mim.EMounting_restriction_area;
import jsdai.SCharacteristic_xim.ELength_tolerance_characteristic;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SNon_feature_shape_element_xim.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.*;

public class CxComponent_placement_restriction_assignment extends CComponent_placement_restriction_assignment implements EMappedXIMEntity
{

	// name - a0
	// protected String a0;
	// items - explicit - current entity
	// protected ARepresentation_item a1;
	// context_of_items - explicit - current entity
	// protected Object a2;
	// id - derived - current entity
	

	/*----------- Taken 1:1 from Representation -----------*/
/*
	public boolean testName(ERepresentation type) throws SdaiException {
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
	private boolean testItems2(ERepresentation type) throws SdaiException {
		return test_aggregate(a1);
	}
	private ARepresentation_item getItems2(ERepresentation type) throws SdaiException {
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

	// attribute (current explicit or supertype explicit) : context_of_items, base type: entity representation_context
/*	public static int usedinContext_of_items(ERepresentation type, ERepresentation_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testContext_of_items(ERepresentation type) throws SdaiException {
		return test_instance(a2);
	}
	public ERepresentation_context getContext_of_items(ERepresentation type) throws SdaiException {
		a2 = get_instance(a2);
		return (ERepresentation_context)a2;
	}*/
	public void setContext_of_items(ERepresentation type, ERepresentation_context value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetContext_of_items(ERepresentation type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeContext_of_items(ERepresentation type) throws SdaiException {
		return a2$;
	}
	
	/*----------- ENDOF Taken 1:1 from Representation -----------*/	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", jsdai.SRepresentation_schema.CRepresentation.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "component_placement_restriction_assignment" attributes
		//maximum_negative_component_height
		setMaximum_negative_component_height(context, this);

		//maximum_positive_component_height
		setMaximum_positive_component_height(context, this);

		// Order is important here !!!
		// 1) requirement
		setRequirement(context, this);

		// 2) area
		setArea(context, this);

		// 2) volume
		setVolume(context, this);

		// components_permitted
		setComponents_permitted(context, this);

		// mounting_clearance
		setMounting_clearance(context, this);

		// Clean ARM attributes, since otherwise we would not be able to downgrade to AIM later on.
		unsetMaximum_negative_component_height(null);
		unsetMaximum_positive_component_height(null);
		unsetRequirement(null);
		unsetArea(null);
		unsetVolume(null);
		unsetComponents_permitted(null);
		unsetMounting_clearance(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "component_placement_restriction_assignment" attributes
		//maximum_negative_component_height
		unsetMaximum_negative_component_height(context, this);

		//maximum_positive_component_height
		unsetMaximum_positive_component_height(context, this);

		// Order is important here !!!
		// 1) requirement
		unsetRequirement(context, this);

		// 2) area
		unsetArea(context, this);

		// 2) volume
		unsetVolume(context, this);

		// components_permitted
		unsetComponents_permitted(context, this);

		// mounting_clearance
		unsetMounting_clearance(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	representation
	* 	{representation.name = `component placement restriction assignment'}
	*  end_mapping_constraints;
	* </p>
	* <p>
	*  mapping_constraints;
	* 	representation
	* 	{representation.name = `component placement restriction assignment'}
	*  end_mapping_constraints;
	* </p>
	* <p>
	*  mapping_constraints;
	* 	representation
	* 	{representation.name = `component placement restriction assignment'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		armEntity.setName(null, "component placement restriction assignment");
		// Context
		jsdai.SRepresentation_schema.ERepresentation_context repContext =
         CxAP210ARMUtilities.createRepresentation_context(context,
                                                                    "", "", true);
		// R -> RC
		armEntity.setContext_of_items(null, repContext);
	}

	public static void unsetMappingConstraints(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
		armEntity.unsetContext_of_items(null);
	}
	
	
	//********** "managed_design_object" attributes

	//********** "component_placement_restriction_assignment" attributes

	/**
	* Sets/creates data for maximum_negative_component_height attribute.
	*
	* <p>
	*  attribute_mapping maximum_negative_component_height_datum_based_length_measure (maximum_negative_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	representation
	* 	{representation.name = `maximum negative component height'}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping maximum_negative_component_height_datum_based_length_measure (maximum_negative_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	representation
	* 	{representation.name = `maximum negative component height'}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping maximum_negative_component_height_datum_based_length_measure (maximum_negative_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	representation
	* 	{representation.name = `maximum negative component height'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_negative_component_height(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetMaximum_negative_component_height(context, armEntity);

		if (armEntity.testMaximum_negative_component_height(null))
		{
			ELength_tolerance_characteristic armMaximum_negative_component_height = armEntity.getMaximum_negative_component_height(null);

			
			ERepresentation_relationship rep_rel = (ERepresentation_relationship)
				context.working_model.createEntityInstance(CRepresentation_relationship.definition);
			rep_rel.setRep_1(null, armEntity);
			rep_rel.setName(null, "maximum negative component height");
			rep_rel.setRep_2(null, armMaximum_negative_component_height);
		}
	}


	/**
	* Unsets/deletes data for maximum_negative_component_height attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_negative_component_height(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
	     ARepresentation_relationship relationships = new ARepresentation_relationship();
	     CRepresentation_relationship.usedinRep_1(null, aimEntity, context.domain, relationships);
	     for (int i = 1; i <= relationships.getMemberCount(); ) {
	        ERepresentation_relationship temp = relationships.getByIndex(i);
	        if ( (temp.getRep_2(null).testName(null)) && (temp.getRep_2(null).getName(null).equals("maximum negative component height"))) {
	           relationships.removeByIndex(i);
	           temp.deleteApplicationInstance();
	        }
	        else {
	           i++;
	        }
	     }
	}


	/**
	* Sets/creates data for maximum_positive_component_height attribute.
	*
	* <p>
	*  attribute_mapping maximum_positive_component_height_datum_based_length_measure (maximum_positive_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	{representation.name = `maximum positive component height'}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping maximum_positive_component_height_datum_based_length_measure (maximum_positive_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	{representation.name = `maximum positive component height'}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping maximum_positive_component_height_datum_based_length_measure (maximum_positive_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	{representation.name = `maximum positive component height'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_positive_component_height(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetMaximum_positive_component_height(context, armEntity);

		if (armEntity.testMaximum_positive_component_height(null))
		{
			ELength_tolerance_characteristic armMaximum_positive_component_height = armEntity.getMaximum_positive_component_height(null);
			
			ERepresentation_relationship rep_rel = (ERepresentation_relationship)
				context.working_model.createEntityInstance(CRepresentation_relationship.definition);
			rep_rel.setRep_1(null, armEntity);
			rep_rel.setName(null, "maximum positive component height");
			rep_rel.setRep_2(null, armMaximum_positive_component_height);
		}
	}


	/**
	* Unsets/deletes data for maximum_positive_component_height attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_positive_component_height(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
	     ARepresentation_relationship relationships = new ARepresentation_relationship();
	     CRepresentation_relationship.usedinRep_1(null, aimEntity, context.domain, relationships);
	     for (int i = 1; i <= relationships.getMemberCount(); ) {
	        ERepresentation_relationship temp = relationships.getByIndex(i);
	        if ( (temp.getRep_2(null).testName(null)) && (temp.getRep_2(null).getName(null).equals("maximum positive component height"))) {
	           relationships.removeByIndex(i);
	           temp.deleteApplicationInstance();
	        }
	        else {
	           i++;
	        }
	     }
	}


	/**
	* Sets/creates data for area attribute.
	*
	* <p>
      representation <-
      representation_relationship.rep_1
      {representation_relationship
      representation_relationship.name = `component placement restriction assignment area'}
      representation_relationship.rep_2 ->
      representation <-
      property_definition_representation.used_representation
      property_definition_representation
      property_definition_representation.definition ->
      property_definition
      property_definition.definition ->
      characterized_definition
      characterized_definition = shape_definition
      shape_definition
      shape_definition = shape_aspect
      shape_aspect =>
      mounting_restriction_area
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// OLD: R <- PDR -> GRP -> !!! MRA, problem - can't do it
   // Changed to R <-r1 RR r2-> R <- PDR -> PD -> MRA, R is NFSD
	public static void setArea(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetArea(context, armEntity);

		if (armEntity.testArea(null))
		{

			jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
			EMounting_restriction_area_armx armArea = armEntity.getArea(null);
			// MRA
			EMounting_restriction_area aimArea = armArea;
         // Exception, here means inverse is not specified and can't be used here
         // PDR -> PD -> MRA
         ANon_feature_shape_model anfsd = armArea.getElement_shape(null, context.domain);
         // Take first suitable
         jsdai.SRepresentation_schema.ERepresentation er;
         if(anfsd.getMemberCount() > 0){
            ENon_feature_shape_model enfsd = anfsd.getByIndex(1);
			//EA enfsd.createAimData(context);
            er = (jsdai.SRepresentation_schema.ERepresentation) enfsd.getTemp("AIM");//EA .getAimInstance();
         }
         // NFSD is not found - so create AIM based structure
         else{
               // PD -> MRA
            LangUtils.Attribute_and_value_structure[] pdStructure = {
               new LangUtils.Attribute_and_value_structure(
               CProperty_definition.attributeDefinition(null), aimArea)};
            EProperty_definition epd = (EProperty_definition)
               LangUtils.createInstanceIfNeeded(context,
                                                CProperty_definition.definition,
                                                pdStructure);
            if (!epd.testName(null))
               epd.setName(null, "");

            // PDR -> PD
            LangUtils.Attribute_and_value_structure[] pdrStructure = {
               new LangUtils.Attribute_and_value_structure(
               CProperty_definition_representation.attributeDefinition(null), epd)};
            EProperty_definition_representation epdr = (EProperty_definition_representation)
               LangUtils.createInstanceIfNeeded(context,
                                                CProperty_definition_representation.definition,
                                                pdrStructure);
            // R <- PDR
            if(epdr.testUsed_representation(null)){
               er = epdr.getUsed_representation(null);
            }
            else{
               er = CxAP210ARMUtilities.createRepresentation(context, null, "", false);
               if((!er.testItems(null))||(er.getItems(null).getMemberCount() == 0)){
	               ERepresentation_item eri = (ERepresentation_item)
	               		context.working_model.createEntityInstance(CRepresentation_item.definition);
	               eri.setName(null, "");
	               if(er.testItems(null)){
	            	   er.getItems(null).addUnordered(eri);
	               }else{
	            	   er.createItems(null).addUnordered(eri);
	               }
               }
               epdr.setUsed_representation(null, er);
            }
         }
         // RR
         ERepresentation_relationship err = (ERepresentation_relationship)
               context.working_model.createEntityInstance(CRepresentation_relationship.definition);
         err.setName(null, "component placement restriction assignment area");
         // R <- RR
         err.setRep_1(null, aimEntity);
         // RR -> R
         err.setRep_2(null, er);
		}
	}


	/**
	* Unsets/deletes data for area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  public static void unsetArea(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws
     SdaiException {
     jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
     ARepresentation_relationship relationships = new ARepresentation_relationship();
     CRepresentation_relationship.usedinRep_1(null, aimEntity, context.domain, relationships);
     for (int i = 1; i <= relationships.getMemberCount(); ) {
        ERepresentation_relationship temp = relationships.getByIndex(i);
        if ( (temp.testName(null)) && (temp.getName(null).equals("component placement restriction assignment area"))) {
           relationships.removeByIndex(i);
           temp.deleteApplicationInstance();
        }
        else {
           i++;
        }
     }

  }


	/**
	* Sets/creates data for volume attribute.
	*
	* <p>
	*  attribute_mapping volume_mounting_restriction_volume (volume
	* , (*PATH*), mounting_restriction_volume);
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition
	* 	{property_definition =>
	* 	requirements_property =>
	* 	grouped_requirements_property
	* 	{grouped_requirements_property <=
	* 	group
	* 	group.name = `item restricted requirements property'}}
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = shape_definition
	* 	shape_definition
	* 	shape_definition = shape_aspect
	* 	shape_aspect =>
	* 	mounting_restriction_volume
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping volume_mounting_restriction_volume (volume
	* , (*PATH*), mounting_restriction_volume);
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition
	* 	{property_definition =>
	* 	requirements_property =>
	* 	grouped_requirements_property
	* 	{grouped_requirements_property <=
	* 	group
	* 	group.name = `item restricted requirements property'}}
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = shape_definition
	* 	shape_definition
	* 	shape_definition = shape_aspect
	* 	shape_aspect =>
	* 	mounting_restriction_volume
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping volume_mounting_restriction_volume (volume
	* , (*PATH*), mounting_restriction_volume);
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition
	* 	{property_definition =>
	* 	requirements_property =>
	* 	grouped_requirements_property
	* 	{grouped_requirements_property <=
	* 	group
	* 	group.name = `item restricted requirements property'}}
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = shape_definition
	* 	shape_definition
	* 	shape_definition = shape_aspect
	* 	shape_aspect =>
	* 	mounting_restriction_volume
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setVolume(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetVolume(context, armEntity);

		if (armEntity.testVolume(null))
		{
			throw new SdaiException(SdaiException.FN_NAVL, "Set function is not implemented for this attribute");
		}
	}


	/**
	* Unsets/deletes data for volume attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetVolume(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
	}


	/**
	* Sets/creates data for requirement attribute.
	*
	* <p>
	*  attribute_mapping requirement_item_restricted_requirement_occurrence (requirement
	* , (*PATH*), item_restricted_requirement_occurrence);
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition =>
	* 	requirements_property =>
	* 	grouped_requirements_property
	* 	{[grouped_requirements_property <=
	* 	group
	* 	group.name = `item restricted requirements property']}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping requirement_item_restricted_requirement_occurrence (requirement
	* , (*PATH*), item_restricted_requirement_occurrence);
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition =>
	* 	requirements_property =>
	* 	grouped_requirements_property
	* 	{[grouped_requirements_property <=
	* 	group
	* 	group.name = `item restricted requirements property']}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping requirement_item_restricted_requirement_occurrence (requirement
	* , (*PATH*), item_restricted_requirement_occurrence);
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition =>
	* 	requirements_property =>
	* 	grouped_requirements_property
	* 	{[grouped_requirements_property <=
	* 	group
	* 	group.name = `item restricted requirements property']}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// R <- PDR -> GRP
	public static void setRequirement(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetRequirement(context, armEntity);

		if (armEntity.testRequirement(null))
		{
			jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
			EItem_restricted_requirement_armx armRequirement = armEntity.getRequirement(null);
			// GRP
			// R <- PDR -> GRP
			EProperty_definition_representation epdr = (EProperty_definition_representation)
			   context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			// R <- PDR
			epdr.setUsed_representation(null, aimEntity);
            LangUtils.Attribute_and_value_structure[] pdStructure = {
            		new LangUtils.Attribute_and_value_structure(
                    CProperty_definition.attributeDefinition(null), armRequirement)};
            EProperty_definition epd = (EProperty_definition)
                    LangUtils.createInstanceIfNeeded(context,
                                                     CProperty_definition.definition,
                                                     pdStructure);
            if(!epd.testName(null))
            	epd.setName(null, "");
			
			// PDR -> GRP
			epdr.setDefinition(null, epd);
		}
	}


	/**
	* Unsets/deletes data for requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
		// R <- PDR -> GRP , remove all PDRs
	public static void unsetRequirement(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdr);
		SdaiIterator iter = apdr.createIterator();
		while(iter.next()){
			EProperty_definition_representation epdr = apdr.getCurrentMember(iter);
			EEntity definition = epdr.getDefinition(null);
			if(definition instanceof EProperty_definition){
				EProperty_definition epd = (EProperty_definition)definition;
				if(epd.getDefinition(null) instanceof EItem_restricted_requirement){
					epdr.deleteApplicationInstance();
				}
			}
		}
	}


	/**
	* Sets/creates data for components_permitted attribute.
	*
	* <p>
	*  attribute_mapping components_permitted (components_permitted
	* , descriptive_representation_item);
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `components permitted'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `true')
	* 	(descriptive_representation_item.description = `false')}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping components_permitted (components_permitted
	* , descriptive_representation_item);
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `components permitted'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `true')
	* 	(descriptive_representation_item.description = `false')}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping components_permitted (components_permitted
	* , descriptive_representation_item);
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `components permitted'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `true')
	* 	(descriptive_representation_item.description = `false')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// R -> DRI
	public static void setComponents_permitted(SdaiContext context, CxComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetComponents_permitted(context, armEntity);

		if (armEntity.testComponents_permitted(null))
		{

			String keyword = "components permitted";
			boolean armComponents_permitted = armEntity.getComponents_permitted(null);
		   // DRI
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.class);
			edri.setName(null, keyword);
			if(armComponents_permitted)
				edri.setDescription(null, "true");
			else
				edri.setDescription(null, "false");
			// R -> DRI
			ARepresentation_item items;
			if(armEntity.testItems2(null))
				items = armEntity.getItems2(null);
			else
				items = armEntity.createItems(null);
			items.addUnordered(edri);
		}
	}


	/**
	* Unsets/deletes data for components_permitted attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// R -> DRI
	public static void unsetComponents_permitted(SdaiContext context, CxComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		String keyword = "components permitted";
		if(armEntity.testItems2(null)){
			ARepresentation_item items = armEntity.getItems2(null);
			for(int i=1;i<=items.getMemberCount();){
				ERepresentation_item item = items.getByIndex(i);
				if((item instanceof EDescriptive_representation_item)&&
				   (item.testName(null))&&(item.getName(null).equals(keyword))){
						items.removeByIndex(i);
						item.deleteApplicationInstance();
				}
			}
		}
	}


	/**
	* Sets/creates data for maximum_mounting_clearance attribute.
	*
	* <p>
	*  attribute_mapping maximum_mounting_clearance_datum_based_length_measure (maximum_mounting_clearance
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	representation
	* 	{representation.name = `maximum mounting clearance'}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping maximum_mounting_clearance_datum_based_length_measure (maximum_mounting_clearance
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	representation
	* 	{representation.name = `maximum mounting clearance'}
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping maximum_mounting_clearance_datum_based_length_measure (maximum_mounting_clearance
	* , (*PATH*), datum_based_length_measure);
	* 	representation <-
	* 	representation_relationship.rep_1
	* 	representation_relationship
	* 	representation_relationship.rep_2 ->
	* 	representation
	* 	representation
	* 	{representation.name = `maximum mounting clearance'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMounting_clearance(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetMounting_clearance(context, armEntity);

		if (armEntity.testMounting_clearance(null))
		{
			ETolerance_characteristic characteristic = armEntity.getMounting_clearance(null);

			ERepresentation_relationship rep_rel = (ERepresentation_relationship)
				context.working_model.createEntityInstance(CRepresentation_relationship.definition);
			rep_rel.setRep_1(null, armEntity);
			rep_rel.setRep_2(null, characteristic);
			rep_rel.setName(null, "mounting clearance");
		}
	}


	/**
	* Unsets/deletes data for mounting_clearance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMounting_clearance(SdaiContext context, EComponent_placement_restriction_assignment armEntity) throws SdaiException
	{
		jsdai.SRepresentation_schema.ERepresentation aimEntity = armEntity;
	     ARepresentation_relationship relationships = new ARepresentation_relationship();
	     CRepresentation_relationship.usedinRep_1(null, aimEntity, context.domain, relationships);
	     for (int i = 1; i <= relationships.getMemberCount(); ) {
	        ERepresentation_relationship temp = relationships.getByIndex(i);
	        if ( (temp.testName(null)) && (temp.getName(null).equals("mounting clearance"))) {
	           relationships.removeByIndex(i);
	           temp.deleteApplicationInstance();
	        }
	        else {
	           i++;
	        }
	     }
	}

}
