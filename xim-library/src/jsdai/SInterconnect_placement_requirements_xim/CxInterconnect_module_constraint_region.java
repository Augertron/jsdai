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

package jsdai.SInterconnect_placement_requirements_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SBasic_attribute_schema.CObject_role;
import jsdai.SBasic_attribute_schema.CRole_association;
import jsdai.SBasic_attribute_schema.EObject_role;
import jsdai.SGroup_mim.AApplied_group_assignment;
import jsdai.SGroup_mim.AGroupable_item;
import jsdai.SGroup_mim.CApplied_group_assignment;
import jsdai.SGroup_mim.EApplied_group_assignment;
import jsdai.SInterconnect_placement_requirements_mim.CInterconnect_module_design_object_category;
import jsdai.SInterconnect_placement_requirements_mim.EInterconnect_module_design_object_category;
import jsdai.SLayered_interconnect_module_design_xim.*;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SNon_feature_shape_element_mim.CGroup_shape_aspect;
import jsdai.SNon_feature_shape_element_xim.CxNon_feature_shape_element;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.*;

public class CxInterconnect_module_constraint_region extends CInterconnect_module_constraint_region implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a1$;
	}
/*	
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_shape)a2;
	}
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a2$;
	}
*/	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CGroup_shape_aspect.definition);

		setMappingConstraints(context, this);

//		setScope(context, this);
		
		// keepout
		setKeepout(context, this);
		
      // non_conformant_interconnect_module_design_object
		setNon_conformant_interconnect_module_design_object(context, this);
		
      // design_specific_purpose
		setDesign_specific_purpose(context, this);
		
      // associated_stratum_extent
		setAssociated_stratum_extent(context, this);
		
      // constrained_design_object_category 		
		setConstrained_design_object_category(context, this);
		
		// Clean ARM
//		unsetScope(null);
		
		// keepout
		unsetKeepout(null);
		
      // non_conformant_interconnect_module_design_object
		unsetNon_conformant_interconnect_module_design_object(null);
		
      // design_specific_purpose
		unsetDesign_specific_purpose(null);
/*		if(testAssociated_stratum_extent(null)){
			AInter_stratum_extent extents = getAssociated_stratum_extent(null);
			System.err.println(this+" REMOVE "+extents.getByIndex(1));
			extents.removeByIndex(1);
		}
		else{
			System.err.println(this+" ALREADY UNSET ");
		}*/
      // associated_stratum_extent
		unsetAssociated_stratum_extent(null);
		
      // constrained_design_object_category 		
		unsetConstrained_design_object_category(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

//			unsetScope(context, this);
			
			// keepout
			unsetKeepout(context, this);
			
	      // non_conformant_interconnect_module_design_object
			unsetNon_conformant_interconnect_module_design_object(context, this);
			
	      // design_specific_purpose
			unsetDesign_specific_purpose(context, this);
			
	      // associated_stratum_extent
			unsetAssociated_stratum_extent(context, this);
			
	      // constrained_design_object_category 		
			unsetConstrained_design_object_category(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  group_shape_aspect &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'interconnect module constraint region'}
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
			EInterconnect_module_constraint_region armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxNon_feature_shape_element.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "interconnect module constraint region");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EInterconnect_module_constraint_region armEntity) throws SdaiException {
		CxNon_feature_shape_element.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}

	/**
	 * Sets/creates data for mapping scope.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/*	
	public static void setScope(SdaiContext context,
			ENon_feature_shape_element armEntity) throws SdaiException {
		CxNon_feature_shape_element.setScope(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/*	
	public static void unsetScope(SdaiContext context,
			ENon_feature_shape_element armEntity) throws SdaiException {
		CxNon_feature_shape_element.unsetScope(context, armEntity);
	}
*/	
	//********** "interconnect_module_constraint_region" attributes

	/**
	* Sets/creates data for keepout attribute.
	*
	*
	*  attribute_mapping keepout (keepout
	* , descriptive_representation_item);
	* 	group_shape_aspect <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `keepout'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `true')
	* 	(descriptive_representation_item.description = `false')}
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GDA <- PD <- PDR -> R -> DRI
	public static void setKeepout(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
		if (armEntity.testKeepout(null))
		{

			boolean armKeepout = armEntity.getKeepout(null);

			// No need for unset, since it is just simple boolean - we can overwrite
		   jsdai.SRepresentation_schema.ARepresentation reps = CxAP210ARMUtilities.getAllRepresentationsOfShapeAspect(armEntity, context, null);
			String keyword = "keepout";
			boolean isRepresentationCreated = false;
			String setValue = "true";
			if(!armKeepout)
				setValue = "false";
			jsdai.SRepresentation_schema.ERepresentation suitableRepresentation = null;
			for(int i=1;i<=reps.getMemberCount();i++){
				jsdai.SRepresentation_schema.ERepresentation temp = reps.getByIndex(i);
				// Exact types only - since all subtypes are intended for geometry - do not "touch" them
				if(temp.getInstanceType() == jsdai.SRepresentation_schema.CRepresentation.definition){
					suitableRepresentation = temp;
				   if(temp.testItems(null)){
						jsdai.SRepresentation_schema.ARepresentation_item items = temp.getItems(null);
						for(int j=1;j<=items.getMemberCount();j++){
							jsdai.SRepresentation_schema.ERepresentation_item item = items.getByIndex(j);
							if((item instanceof EDescriptive_representation_item)&&
								(item.getName(null).equals(keyword))){
								((EDescriptive_representation_item)(item)).setDescription(null, setValue);
								return;
							}
						}
				   }
				}
			}
		   if(suitableRepresentation == null){
			   suitableRepresentation = (jsdai.SRepresentation_schema.ERepresentation)
					context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
				// Some AIM stuff
				suitableRepresentation.setName(null, "");
				// Context
				jsdai.SRepresentation_schema.ERepresentation_context repContext = 
               CxAP210ARMUtilities.createRepresentation_context(context,
                                                                          "", "", true);
				suitableRepresentation.setContext_of_items(null, repContext);
				isRepresentationCreated = true;
		   }
			// Value
			EDescriptive_representation_item keepoutAIM = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			keepoutAIM.setName(null, keyword);
			keepoutAIM.setDescription(null, setValue);
			// Put into items list
			jsdai.SRepresentation_schema.ARepresentation_item items;
			if(!suitableRepresentation.testItems(null))
				items = suitableRepresentation.createItems(null);
			else
				items = suitableRepresentation.getItems(null);
			items.addUnordered(keepoutAIM);
		   if(isRepresentationCreated){
				// Create a new structure
				// PD
				EProperty_definition propDef = (EProperty_definition)
					context.working_model.createEntityInstance(CProperty_definition.definition);
				propDef.setDefinition(null, armEntity);
				propDef.setName(null, "");
				// Representation
				EProperty_definition_representation propDefRep = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.class);
				propDefRep.setDefinition(null, propDef);
				propDefRep.setUsed_representation(null, suitableRepresentation);
		   }

		}
	}


	/**
	* Unsets/deletes data for keepout attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetKeepout(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
		String keyword = "keepout";
		CxAP210ARMUtilities.unsetRepresentationItemFromShapeAspect(armEntity, context, null, keyword);
	}


	/**
	* Sets/creates data for non_conformant_interconnect_module_design_object attribute.
	*
	*
	*  attribute_mapping non_conformant_interconnect_module_design_object_cutout (non_conformant_interconnect_module_design_object
	* , (*PATH*), cutout);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	shape_aspect.description = `cutout'}
	* 	shape_aspect =>
	* 	component_shape_aspect =>
	* 	inter_stratum_feature
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_stratum_feature (non_conformant_interconnect_module_design_object
	* , (*PATH*), stratum_feature);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	stratum_feature
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_laminate_component (non_conformant_interconnect_module_design_object
	* , (*PATH*), laminate_component);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	(shape_aspect =>
	* 	component_shape_aspect)
	* 	(shape_aspect
	* 	shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition =>
	* 	{product_definition.description = `laminate component'}
	* 	component_definition)
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_conductor (non_conformant_interconnect_module_design_object
	* , (*PATH*), conductor);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	{shape_aspect
	* 	shape_aspect.description = `conductor'}
	* 	shape_aspect =>
	* 	stratum_feature
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_inter_stratum_feature (non_conformant_interconnect_module_design_object
	* , (*PATH*), inter_stratum_feature);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	component_shape_aspect =>
	* 	inter_stratum_feature
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_non_functional_land (non_conformant_interconnect_module_design_object
	* , (*PATH*), non_functional_land);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	[shape_aspect.description = `non functional land']
	* 	[shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	[product_definition =>
	* 	component_definition]
	* 	[product_definition
	* 	product_definition.formation ->
	* 	product_definition_formation
	* 	product_definition_formation.of_product ->
	* 	product <-
	* 	product_related_product_category.products[i]
	* 	product_related_product_category <=
	* 	product_category
	* 	product_category.name = `interconnect module']]}
	* 	shape_aspect =>
	* 	component_shape_aspect =>
	* 	land
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_land (non_conformant_interconnect_module_design_object
	* , (*PATH*), land);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	[product_definition =>
	* 	component_definition]
	* 	[product_definition
	* 	product_definition.formation ->
	* 	product_definition_formation
	* 	product_definition_formation.of_product ->
	* 	product <-
	* 	product_related_product_category.products[i]
	* 	product_related_product_category <=
	* 	product_category
	* 	product_category.name = `interconnect module']}
	* 	shape_aspect =>
	* 	component_shape_aspect =>
	* 	land
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_component_termination_passage (non_conformant_interconnect_module_design_object
	* , (*PATH*), component_termination_passage);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	{shape_aspect
	* 	shape_aspect.description = `component termination passage'}
	* 	shape_aspect =>
	* 	component_shape_aspect =>
	* 	inter_stratum_feature =>
	* 	plated_inter_stratum_feature =>
	* 	plated_passage
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_via (non_conformant_interconnect_module_design_object
	* , (*PATH*), via);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	(shape_aspect.description = `bonded conductive base blind via')
	* 	(shape_aspect.description = `buried via')
	* 	(shape_aspect.description = `interfacial connection')
	* 	(shape_aspect.description = `non conductive base blind via')
	* 	(shape_aspect.description = `plated conductive base blind via')}
	* 	shape_aspect =>
	* 	component_shape_aspect =>
	* 	inter_stratum_feature =>
	* 	plated_inter_stratum_feature =>
	* 	plated_passage
	*  end_attribute_mapping;
	*  attribute_mapping non_conformant_interconnect_module_design_object_conductive_filled_area (non_conformant_interconnect_module_design_object
	* , (*PATH*), conductive_filled_area);
	* 	group_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `constraint region violation'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	shape_aspect.description = `conductive filled area'}
	* 	shape_aspect =>
	* 	stratum_feature
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> SA
	// SA <- SAR -> SA -> PDS
	public static void setNon_conformant_interconnect_module_design_object(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
      //unset old values
      unsetNon_conformant_interconnect_module_design_object(context, armEntity);
		if (armEntity.testNon_conformant_interconnect_module_design_object(null))
		{
			AInterconnect_module_design_object_select armNon_conformant_interconnect_module_design_object = armEntity.getNon_conformant_interconnect_module_design_object(null);
			SdaiIterator iter = armNon_conformant_interconnect_module_design_object.createIterator();
			EEntity eimdo;
			EShape_aspect esa;
			while(iter.next()){
				eimdo = armNon_conformant_interconnect_module_design_object.getCurrentMember(iter);
				
				if(eimdo instanceof EProduct_definition_shape){
					EProduct_definition_shape epds = (EProduct_definition_shape)eimdo;
					
		         LangUtils.Attribute_and_value_structure[] saStructure =
					{new LangUtils.Attribute_and_value_structure(
		                 CShape_aspect.attributeOf_shape(null),
		                  epds)
					};
					esa = (EShape_aspect)
						LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, saStructure);
					if(!esa.testName(null))
						esa.setName(null, "");
					if(!esa.testProduct_definitional(null))
						esa.setProduct_definitional(null, ELogical.UNKNOWN);
				}
				else
					// Class cast exception means this is unsupported case and need a review
					esa = (EShape_aspect)eimdo;
				// SAR
				EShape_aspect_relationship esar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				esar.setRelating_shape_aspect(null, armEntity);
				esar.setRelated_shape_aspect(null, esa);
				esar.setName(null, "constraint region violation");
			}
		}
	}


	/**
	* Unsets/deletes data for non_conformant_interconnect_module_design_object attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetNon_conformant_interconnect_module_design_object(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, asar);
		SdaiIterator iter = asar.createIterator();
		while(iter.next()){
			EShape_aspect_relationship esar = asar.getCurrentMember(iter);
			if(esar.getName(null).equals("constraint region violation"))
				esar.deleteApplicationInstance();
		}
	}


	/**
	* Sets/creates data for design_specific_purpose attribute.
	*
	*
	*  attribute_mapping design_specific_purpose (design_specific_purpose
	* , descriptive_representation_item);
	* 	group_shape_aspect <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `design specific purpose'}
	* 	representation_item =>
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDesign_specific_purpose(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
		if (armEntity.testDesign_specific_purpose(null))
		{
			String armPurpose = armEntity.getDesign_specific_purpose(null);

			// No need for unset, since it is just simple String - we can overwrite
		   jsdai.SRepresentation_schema.ARepresentation reps = CxAP210ARMUtilities.getAllRepresentationsOfShapeAspect(armEntity, context, null);
			String keyword = "design specific purpose";
			boolean isRepresentationCreated = false;

			jsdai.SRepresentation_schema.ERepresentation suitableRepresentation = null;
			for(int i=1;i<=reps.getMemberCount();i++){
				jsdai.SRepresentation_schema.ERepresentation temp = reps.getByIndex(i);
				// Exact types only - since all subtypes are intended for geometry - do not "touch" them
				if(temp.getInstanceType() == jsdai.SRepresentation_schema.CRepresentation.definition){
					suitableRepresentation = temp;
				   if(temp.testItems(null)){
						jsdai.SRepresentation_schema.ARepresentation_item items = temp.getItems(null);
						for(int j=1;j<=items.getMemberCount();j++){
							jsdai.SRepresentation_schema.ERepresentation_item item = items.getByIndex(j);
							if((item instanceof EDescriptive_representation_item)&&
								(item.getName(null).equals(keyword))){
								((EDescriptive_representation_item)(item)).setDescription(null, armPurpose);
								return;
							}
						}
				   }
				}
			}
		   if(suitableRepresentation == null){
			   suitableRepresentation = (jsdai.SRepresentation_schema.ERepresentation)
					context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
				// Some AIM stuff
				suitableRepresentation.setName(null, "");
				// Context
				jsdai.SRepresentation_schema.ERepresentation_context repContext = 
               CxAP210ARMUtilities.createRepresentation_context(context,
                                                                          "", "", true);
				suitableRepresentation.setContext_of_items(null, repContext);
				isRepresentationCreated = true;
		   }
			// Value
			EDescriptive_representation_item keepoutAIM = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.class);
			keepoutAIM.setName(null, keyword);
			keepoutAIM.setDescription(null, armPurpose);
			// Put into items list
			jsdai.SRepresentation_schema.ARepresentation_item items;
			if(!suitableRepresentation.testItems(null))
				items = suitableRepresentation.createItems(null);
			else
				items = suitableRepresentation.getItems(null);
			items.addUnordered(keepoutAIM);
		   if(isRepresentationCreated){
				// Create a new structure
				// PD
				EProperty_definition propDef = (EProperty_definition)
					context.working_model.createEntityInstance(CProperty_definition.class);
				propDef.setDefinition(null, armEntity);
				propDef.setName(null, "");
				// Representation
				EProperty_definition_representation propDefRep = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.class);
				propDefRep.setDefinition(null, propDef);
				propDefRep.setUsed_representation(null, suitableRepresentation);
		   }

		}
	}


	/**
	* Unsets/deletes data for design_specific_purpose attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDesign_specific_purpose(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
		String keyword = "design specific purpose";
		CxAP210ARMUtilities.unsetRepresentationItemFromShapeAspect(armEntity, context, null, keyword);
	}


	/**
	* Sets/creates data for associated_stratum_extent attribute.
	*
	*
	*  attribute_mapping associated_stratum_extent_inter_stratum_extent (associated_stratum_extent
	* , (*PATH*), inter_stratum_extent);
      group_shape_aspect <=
      shape_aspect
      shape_definition = shape_aspect
      characterized_definition = shape_definition
      characterized_definition <-
      property_definition.definition
      property_definition <-
      property_definition_relationship.related_property_definition
      {property_definition_relationship
      property_definition_relationship.name = `associated stratum extent'}
      property_definition_relationship.relating_property_definition ->
      property_definition
      property_definition.definition ->
      characterized_definition
      characterized_definition = characterized_product_definition
      characterized_product_definition
      characterized_product_definition = product_definition_relationship
      product_definition_relationship
      {product_definition_relationship
      product_definition_relationship.name = `inter stratum extent'}
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
   // GSA <- PD <- PDR -> PD -> PDR
	public static void setAssociated_stratum_extent(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
      //unset old values
      unsetAssociated_stratum_extent(context, armEntity);
		if (armEntity.testAssociated_stratum_extent(null))
		{

			AInter_stratum_extent armAssociated_stratum_extent = armEntity.getAssociated_stratum_extent(null);
         // GSA <- PD
         LangUtils.Attribute_and_value_structure[] pdStructure =
         {new LangUtils.Attribute_and_value_structure(
                 CProperty_definition.attributeDefinition(null),
                  armEntity)
         };
         EProperty_definition epd1 = (EProperty_definition)
            LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
         if(!epd1.testName(null))
            epd1.setName(null, "");
         for(int i=1;i<=armAssociated_stratum_extent.getMemberCount();i++){
            EInter_stratum_extent armExtent = armAssociated_stratum_extent.getByIndex(i);
            // PD -> PDR
            LangUtils.Attribute_and_value_structure[] pd2Structure =
            {new LangUtils.Attribute_and_value_structure(
                    CProperty_definition.attributeDefinition(null),
                     armExtent)
            };
            EProperty_definition epd2 = (EProperty_definition)
               LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pd2Structure);
            if(!epd2.testName(null))
               epd2.setName(null, "");
            // PDR
            EProperty_definition_relationship epdr2 = (EProperty_definition_relationship)
               context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
            epdr2.setDescription(null, "");
            epdr2.setName(null, "associated stratum extent");
            epdr2.setRelated_property_definition(null, epd1);
            epdr2.setRelating_property_definition(null, epd2);
         }
		}
	}


	/**
	* Unsets/deletes data for associated_stratum_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssociated_stratum_extent(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
		
      AProperty_definition apd = new AProperty_definition();
      CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
      for(int i=1;i<=apd.getMemberCount();i++){
         EProperty_definition epd = apd.getByIndex(i);
         AProperty_definition_relationship apdr = new AProperty_definition_relationship();
         CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apdr);
         for(int j=1;j<=apdr.getMemberCount();){
            EProperty_definition_relationship epdr = apdr.getByIndex(j);
            if((epdr.testName(null))&&(epdr.getName(null).equals("associated stratum extent"))){
               apdr.removeByIndex(j);
               epdr.deleteApplicationInstance();
            }
            else
               j++;
         }
      }
	}


	/**
	* Sets/creates data for constrained_design_object_category attribute.
	*
	*
	attribute_mapping constrained_design_object_category(constrained_design_object_category, interconnect_module_design_object_category);
		group_shape_aspect <=
		shape_aspect
		shape_definition = shape_aspect
		characterized_definition = shape_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.relating_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'constrained object'}
		property_definition_relationship.related_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_object
		characterized_object
		{characterized_object
		[(characterized_object.description = 'assembly component category')
		(characterized_object.description = 'cutout category')
		(characterized_object.description = 'embedded physical component terminal category')
		(characterized_object.description = 'fill area category')
		(characterized_object.description = 'inter stratum feature category')
		(characterized_object.description = 'stratum feature category')
		(characterized_object.description = 'via category')]
		[characterized_object =>
		interconnect_module_design_object_category]}

	end_attribute_mapping;
	
	attribute_mapping constrained_design_object_category(constrained_design_object_category, group);
		group_shape_aspect
		groupable_item = group_shape_aspect 
		groupable_item <-
		applied_group_assignment.items[i]
		applied_group_assignment <=
		group_assignment 
		{group_assignment.role ->
		object_role
		object_role.name = 'constrained object'}
		group_assignment.assigned_group ->
		group 
	end_attribute_mapping;	
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GSA <- PD <- PDR -> PD -> IMDOC
	// GSA <- AGA -> G
	
	public static void setConstrained_design_object_category(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
      //unset old values
      unsetConstrained_design_object_category(context, armEntity);
		if (armEntity.testConstrained_design_object_category(null) == EInterconnect_module_constraint_region.sConstrained_design_object_categoryInterconnect_module_design_object_category_armx)
		{
			int armConstrained_design_object_category = armEntity.getConstrained_design_object_category(null, (EInterconnect_module_design_object_category_armx)null);
         // Find IMDOC
         String description;
         if(EInterconnect_module_design_object_category_armx.INTERCONNECT_MODULE_CUTOUT ==
            armConstrained_design_object_category)
            description = "cutout category";
         else if(EInterconnect_module_design_object_category_armx.INTERCONNECT_MODULE_FILL_AREA ==
            armConstrained_design_object_category)
            description = "fill area category";
         else if(EInterconnect_module_design_object_category_armx.INTERCONNECT_MODULE_INTER_STRATUM_FEATURE ==
            armConstrained_design_object_category)
            description = "inter stratum feature category";
         else if(EInterconnect_module_design_object_category_armx.INTERCONNECT_MODULE_STRATUM_FEATURE ==
            armConstrained_design_object_category)
            description = "stratum feature category";
         else if(EInterconnect_module_design_object_category_armx.INTERCONNECT_MODULE_VIA ==
            armConstrained_design_object_category)
            description = "via category";
         else
            throw new SdaiException(SdaiException.VA_NVLD, armConstrained_design_object_category +" is not supported for Interconnect_module_design_object_category ");
         LangUtils.Attribute_and_value_structure[] imdocStructure =
			{new LangUtils.Attribute_and_value_structure(
                 CInterconnect_module_design_object_category.attributeDescription(null),
                  description)
			};
			EInterconnect_module_design_object_category
            eimdoc = (EInterconnect_module_design_object_category)
				LangUtils.createInstanceIfNeeded(context, CInterconnect_module_design_object_category.definition, imdocStructure);
			if(!eimdoc.testName(null))
				eimdoc.setName(null, "");

			// GSA <- PropD
         LangUtils.Attribute_and_value_structure[] pdStructure =
			{new LangUtils.Attribute_and_value_structure(
                 CProperty_definition.attributeDefinition(null),
                  armEntity)
			};
			EProperty_definition epd1 = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
			if(!epd1.testName(null))
				epd1.setName(null, "");
			// PD -> IMDOC
         LangUtils.Attribute_and_value_structure[] pd2Structure =
			{new LangUtils.Attribute_and_value_structure(
                 CProperty_definition.attributeDefinition(null),
                  eimdoc)
			};
			EProperty_definition epd2 = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pd2Structure);
			if(!epd2.testName(null))
				epd2.setName(null, "");
         // PDR
         EProperty_definition_relationship epdr = (EProperty_definition_relationship)
            context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
         epdr.setDescription(null, "");
         epdr.setName(null, "constrained object");
         // PropD <- PDR
         epdr.setRelated_property_definition(null, epd2);
         // PDR -> RP
         epdr.setRelating_property_definition(null, epd1);

        // Group case 
        // GSA <- AGA -> G
		} else if (armEntity.testConstrained_design_object_category(null) != 0){
			EEntity group = armEntity.getConstrained_design_object_category(null);
			// OR
	         LangUtils.Attribute_and_value_structure[] orStructure =
				{new LangUtils.Attribute_and_value_structure(
	                 CObject_role.attributeName(null),
	                  "constrained object")
				};
				EObject_role eor = (EObject_role)
					LangUtils.createInstanceIfNeeded(context, CObject_role.definition, orStructure);
			// AGA
	         LangUtils.Attribute_and_value_structure[] agaStructure =
				{new LangUtils.Attribute_and_value_structure(
	    	         CApplied_group_assignment.attributeAssigned_group(null),
	    	         group)	                 
				};
	         EApplied_group_assignment eaga = (EApplied_group_assignment)
					LangUtils.createInstanceIfNeeded(context, CApplied_group_assignment.definition, agaStructure);
				// RA
				LangUtils.Attribute_and_value_structure[] raStructure =
				{new LangUtils.Attribute_and_value_structure(
					CRole_association.attributeItem_with_role(null),
					eaga),
				 new LangUtils.Attribute_and_value_structure(
					CRole_association.attributeRole(null),
					eor)					
				};
				LangUtils.createInstanceIfNeeded(context, CRole_association.definition, raStructure);
				// Finally wrap up with AGA
				AGroupable_item items;
				if(eaga.testItems(null)){
					items = eaga.getItems(null);
				}else{
					items = eaga.createItems(null);
				}
				items.addUnordered(armEntity);
		}
	}

	/**
	* Unsets/deletes data for constrained_design_object_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	
	public static void unsetConstrained_design_object_category(SdaiContext context, EInterconnect_module_constraint_region armEntity) throws SdaiException
	{
		// Cleanup first option 
      AProperty_definition apd = new AProperty_definition();
      CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
      for(int i=1;i<=apd.getMemberCount();i++){
         EProperty_definition epd = apd.getByIndex(i);
         AProperty_definition_relationship apdr = new AProperty_definition_relationship();
         CProperty_definition_relationship.usedinRelating_property_definition(null, epd, context.domain, apdr);
         for(int j=1;j<=apdr.getMemberCount();){
            EProperty_definition_relationship epdr = apdr.getByIndex(j);
            if((epdr.testName(null))&&
               (epdr.getName(null).equals("constrained object"))){
               apdr.removeByIndex(j);
               epdr.deleteApplicationInstance();
            }
            else
               j++;
         }
      }
		// Cleanup second option   
	  	AApplied_group_assignment aaga = new AApplied_group_assignment();
		CApplied_group_assignment.usedinItems(null, armEntity, context.domain, aaga);
		for(int i=1,count=aaga.getMemberCount(); i<= count; i++){
			EApplied_group_assignment eaga = aaga.getByIndex(i);
			AGroupable_item items = eaga.getItems(null);
			items.removeUnordered(armEntity);
			if(items.getMemberCount() == 0){
				eaga.deleteApplicationInstance();
			}
		}
	}
	
	
}