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

package jsdai.SRequirement_decomposition_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SRequirement_decomposition_mim.*;
import jsdai.SRequirement_view_definition_xim.CxRequirement_view_definition;
import jsdai.SAnalytical_model_mim.*;
import jsdai.SAnalytical_model_xim.*;
import jsdai.SFunctional_specification_mim.*;

/**
* @author Giedrius Liutkus, Valdas Zigas
* @version $Revision$
*/

public class CxPredefined_requirement_view_definition_armx extends CPredefined_requirement_view_definition_armx implements EMappedXIMEntity
{

	// Taken from property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProduct_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProduct_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProduct_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a2$;
	}

	// END OF taken from property_definition
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPredefined_requirement_view_definition.definition);

		setMappingConstraints(context, this);

		//********** "Requirement_definition_property" attributes

		//required_analytical_representation
		setRequired_analytical_representation(context, this);

		//order improtant set of requirement_specification must be before required_specification
		//requirement_specification - made derived
		// setRequirement_specification(context, this);

		//required_specification
      // Removed since WD34
		// setRequired_specification(context, this);

		//required_part
		// setRequired_part(context, this);

		//required_coordinated_characteristic - removed during modularization
		// setRequired_coordinated_characteristic(context, this);


		//required_characteristic
		setRequired_characteristic(context, this);

		//required_material - INVERSE
		// setRequired_material(context, this);

		// required_functional_specification
		setRequired_functional_specification(context, this);

      // life_cycle_context                                                : life_cycle_context_type;
      // setLife_cycle_context(context, this);
      
      // domain_context                                                    : domain_context_type;
      // setDomain_context(context, this);
		
		// made derived
      // setReference_clause(context, this);
	
      // Clean ARM
      unsetRequired_analytical_representation(null);
      unsetRequired_functional_specification(null);
      // unsetRequired_part(null);
      unsetRequired_characteristic(null);

      // life_cycle_context                                                : life_cycle_context_type;
      // unsetLife_cycle_context(null);
      
      // domain_context                                                    : domain_context_type;
      // unsetDomain_context(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "Requirement_definition_property" attributes

		//required_analytical_representation
		unsetRequired_analytical_representation(context, this);

		//order improtant set of requirement_specification must be before required_specification
		//requirement_specification - made derived
		// unsetRequirement_specification(context, this);

		//required_specification
      // Removed since WD34
		// setRequired_specification(context, this);

		//required_part
		// unsetRequired_part(context, this);

		//required_coordinated_characteristic - removed during modularization
		// unsetRequired_coordinated_characteristic(context, this);


		//required_characteristic
		unsetRequired_characteristic(context, this);

		//required_material - INVERSE
		// unsetRequired_material(context, this);

		// required_functional_specification
		unsetRequired_functional_specification(context, this);
		
		// made derived 
		// unsetReference_clause(context, this);

      // life_cycle_context                                                : life_cycle_context_type;
      // unsetLife_cycle_context(context, this);
      
      // domain_context                                                    : domain_context_type;
      // unsetDomain_context(context, this);
		
	}

/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	requirements_property <=
	* 	property_definition
	* 	{property_definition
	* 	property_definition.definition ->
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition
	* 	{[product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	(application_context_element.name = `design requirement')
	* 	(application_context_element.name = `requirement')]
	* 	[product_definition
	* 	product_definition.name = `requirements model']}}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	//  All constraints are anyway set in "associated_definition"
	public static void setMappingConstraints(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxRequirement_view_definition.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxRequirement_view_definition.unsetMappingConstraints(context, armEntity);
	}
	
	//********** "managed_design_object" attributes

	//********** "requirement_definition_property" attributes


	/**
	* Sets/creates data for required_analytical_representation attribute.
	*
	* <p>
	*  attribute_mapping required_analytical_representation_analytical_representation (required_analytical_representation
	* , (*PATH*), analytical_representation);
			predefined_requirement_view_definition &lt;=
			product_definition
			characterized_product_definition = product_definition
			characterized_product_definition
			characterized_definition = characterized_product_definition
			characterized_definition &lt;-
			property_definition.definition
			{property_definition.name = 'required analytical representation'}
			property_definition &lt;-
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation -&gt;
			representation =&gt;
			analytical_representation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// required_analytical_representation: OPTIONAL SET [1:?] OF analytical_representation;
	// RP <- PDR -> AR
	public static void setRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRequired_analytical_representation(context, armEntity);

		if (armEntity.testRequired_analytical_representation(null))
		{

			AAnalytical_model_application aArmRequired_analytical_representation =  armEntity.getRequired_analytical_representation(null);
			EAnalytical_representation armRequired_analytical_representation = null;
			EProperty_definition property_definition = (EProperty_definition) context.working_model.createEntityInstance(CProperty_definition.definition);
			property_definition.setDefinition(null, armEntity);
			property_definition.setName(null, "required analytical representation");
			for (int i = 1; i <= aArmRequired_analytical_representation.getMemberCount(); i++) {
				armRequired_analytical_representation = aArmRequired_analytical_representation.getByIndex(i);
				//property_definition_representation
				EProperty_definition_representation property_definition_representation = (EProperty_definition_representation) context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				property_definition_representation.setDefinition(null, property_definition);
				property_definition_representation.setUsed_representation(null, armRequired_analytical_representation);
			}
		}
	}


	/**
	* Unsets/deletes data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int j = 1; j <= aProperty_definition.getMemberCount(); j++) {
			property_definition = aProperty_definition.getByIndex(j);
			if((property_definition.testName(null))&&(property_definition.getName(null).equals("required analytical representation"))){
				//property_definition_representation
				EProperty_definition_representation property_definition_representation = null;
				AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, aProperty_definition_representation);
				for (int i = 1; i <= aProperty_definition_representation.getMemberCount(); i++) {
					property_definition_representation = aProperty_definition_representation.getByIndex(i);
					property_definition_representation.deleteApplicationInstance();
				}
				property_definition.deleteApplicationInstance();
			}
		}

	}



	/**
	* Sets/creates data for required_part attribute.
	*
	*
	*  attribute_mapping required_part_ee_product (required_part
	* , (*PATH*), ee_product);
	* 	requirements_property <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	{[product_definition =>
	* 	requirement_definition]
	* 	[product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `requirement']}
	* 	product_definition <-
	* 	product_definition_relationship.relating_product_definition
	* 	product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `required parts'}
	* 	product_definition_relationship.related_product_definition ->
	* 	product_definition
	* 	product_definition.formation ->
	* 	product_definition_formation
	* 	product_definition_formation.of_product ->
	* 	product
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// required_part: OPTIONAL SET [1:?] OF ee_product;
	// RP -> RD <<- PDR ->> PD -> PDF -> P
	/* REMOVED
	public static void setRequired_part(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRequired_part(context, armEntity);

		if (armEntity.testRequired_part(null))
		{

			AProduct aArmRequired_part = armEntity.getRequired_part(null);
			EProduct armRequired_part = null;

			//property_definition.definition mus be set on creating of associated_definition: requirement_definition
			EProduct_definition product_definition = null;
			if (armEntity.testDefinition(null)) {
				product_definition = (EProduct_definition) armEntity.getDefinition(null);
			} else {
				throw new SdaiException(SdaiException.VA_NSET, "associated_definition must be created before required_part");
			}

			for (int i = 1; i <= aArmRequired_part.getMemberCount(); i++) {
				armRequired_part = aArmRequired_part.getByIndex(i);

				//product_definition_relationship
				EProduct_definition_relationship product_definition_relationship = (EProduct_definition_relationship) context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
				product_definition_relationship.setId(null, "");
				product_definition_relationship.setName(null, "required parts");
            product_definition_relationship.setRelating_product_definition(null, product_definition);
				//product_definition
				EProduct_definition product_definitionRelated = (EProduct_definition) context.working_model.createEntityInstance(EProduct_definition.class);
				product_definitionRelated.setId(null, "");
				product_definitionRelated.setFrame_of_reference(null, CxAP210ARMUtilities.createProduct_definition_context(context, "","electronic_assembly_interconnect_and_packaging_design", true));
				product_definition_relationship.setRelated_product_definition(null, product_definitionRelated);

				//product_definition.formation
				EProduct_definition_formation product_definition_formation = (EProduct_definition_formation) context.working_model.createEntityInstance(EProduct_definition_formation.class);
				product_definition_formation.setId(null, "");
				product_definition_formation.setOf_product(null, armRequired_part);
				product_definitionRelated.setFormation(null, product_definition_formation);
			}
		}
	}
*/

	/**
	* Unsets/deletes data for required_part attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Removed	
	public static void unsetRequired_part(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{

		//product_definition
		EProduct_definition product_definition = null;
		if (armEntity.testDefinition(null)) {
			product_definition = (EProduct_definition) armEntity.getDefinition(null);
		} else {
			return;
		}

		//product_definition_relationship
		EProduct_definition_relationship product_definition_relationship = null;
		AProduct_definition_relationship aProduct_definition_relationship = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelating_product_definition(null, product_definition, context.domain, aProduct_definition_relationship);
		for (int i = 1; i <= aProduct_definition_relationship.getMemberCount(); i++) {
			product_definition_relationship = aProduct_definition_relationship.getByIndex(i);

			//product_definition related
			if (product_definition_relationship.testName(null) && product_definition_relationship.getName(null).equals("required parts")) {
			   if (product_definition_relationship.testRelated_product_definition(null)) {
			      EProduct_definition product_definitionRelated = product_definition_relationship.getRelated_product_definition(null);

				  if (CxAP210ARMUtilities.countEntityUsers(context, product_definitionRelated) == 1) {
					  //product_definition_formation
					  if (product_definitionRelated.testFormation(null)) {
						 EProduct_definition_formation product_definition_formation = product_definitionRelated.getFormation(null);

						 if (CxAP210ARMUtilities.countEntityUsers(context, product_definition_formation) == 1) {
							product_definition_formation.deleteApplicationInstance();
						 }
					  }

					  //frame_of_reference
					  if (product_definitionRelated.testFrame_of_reference(null)) {
						 CxAP210ARMUtilities.removeInstanceIfNeeded(context, product_definitionRelated.getFrame_of_reference(null));
					  }

					  product_definitionRelated.deleteApplicationInstance();
				  }

			   }
			   product_definition_relationship.deleteApplicationInstance();
			}
		}
	}
*/
	/**
	* Sets/creates data for requirement_specification attribute.
	*
	*
	*  attribute_mapping requirement_specification_ee_specification (requirement_specification
	* , (*PATH*), ee_specification);
	* 	requirements_property
		document_reference_item = requirements_property
		document_reference_item &lt;-
		applied_document_reference.items[i]
		applied_document_reference
		applied_document_reference &lt;=
		document_reference
		document_reference.assigned_document -&gt;
		document &lt;-
		document_product_association.relating_document
		{document_product_association.name = 'equivalence'}
		document_product_association.related_product -&gt;
		product_or_formation_or_definition = product_definition
		product_definition
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// requirement_specification: SET [1:?] OF ee_specification;
	// RP <<- ADR ->> ES
	/* - made derived
	public static void setRequirement_specification(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRequirement_specification(context, armEntity);

		if (armEntity.testRequirement_specification(null))
		{
			ASpecification_definition_armx specifications = armEntity.getRequirement_specification(null);
			for(int i=1, count=specifications.getMemberCount(); i<=count; i++){
				ESpecification_definition_armx specification = specifications.getByIndex(i); 
				CxAP210ARMUtilities.assignDocument_definition(context, specification, armEntity);				
			}

		}
	}
*/

	/**
	* Unsets/deletes data for requirement_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// requirement_specification: SET [1:?] OF ee_specification;
	// RP <<- ADR ->> ES
	// Strategy - remove all ADRs links like this RP - ADR ->> ES
	// If ADR has no more elements - remove it too
	/* made derived
	public static void unsetRequirement_specification(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unAssignDocument_definition(context, armEntity, null);
	}
*/

	/**
	* Sets/creates data for required_characteristic attribute.
	*
	*
attribute_mapping required_characteristic(required_characteristic, $PATH, characteristic);
predefined_requirement_view_definition <=
product_definition
characterized_product_definition = product_definition
characterized_product_definition
characterized_definition = characterized_product_definition
characterized_definition <-
property_definition.definition
{property_definition.name = 'required characteristic'}
property_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation
representation.items[i] ->
representation_item
{representation_item
groupable_item = representation_item
groupable_item <-
applied_group_assignment.items[i]
applied_group_assignment <=
group_assignment
group_assignment.assigned_group ->
group =>
characteristic_type}

end_attribute_mapping;

attribute_mapping required_characteristic(required_characteristic, $PATH, descriptive_representation_item);
				predefined_requirement_view_definition <=
				product_definition
				characterized_product_definition = product_definition
				characterized_product_definition
				characterized_definition = characterized_product_definition
				characterized_definition <-
				property_definition.definition
				{property_definition.name = 'required characteristic'}
				property_definition <-
				property_definition_representation.definition
				property_definition_representation
				property_definition_representation.used_representation ->
				representation
				representation.items[i] ->
				representation_item =>
				descriptive_representation_item
				
end_attribute_mapping;

attribute_mapping required_characteristic(required_characteristic, $PATH, measure_representation_item);
					predefined_requirement_view_definition <=
					product_definition
					characterized_product_definition = product_definition
					characterized_product_definition
					characterized_definition = characterized_product_definition
					characterized_definition <-
					property_definition.definition
					{property_definition.name = 'required characteristic'}
					property_definition <-
					property_definition_representation.definition
					property_definition_representation
					property_definition_representation.used_representation ->
					representation
					representation.items[i] ->
					representation_item =>
					measure_representation_item
				
end_attribute_mapping;

attribute_mapping required_characteristic(required_characteristic, $PATH, Value_list);
				predefined_requirement_view_definition <=
				product_definition
				characterized_product_definition = product_definition
				characterized_product_definition
				characterized_definition = characterized_product_definition
				characterized_definition <-
				property_definition.definition
				{property_definition.name = 'required characteristic'}
				property_definition <-
				property_definition_representation.definition
				property_definition_representation
				property_definition_representation.used_representation ->
				representation
				representation.items[i] ->
				representation_item =>
				compound_representation_item
				
end_attribute_mapping;

attribute_mapping required_characteristic(required_characteristic, $PATH, Value_set);
				predefined_requirement_view_definition <=
				product_definition
				characterized_product_definition = product_definition
				characterized_product_definition
				characterized_definition = characterized_product_definition
				characterized_definition <-
				property_definition.definition
				{property_definition.name = 'required characteristic'}
				property_definition <-
				property_definition_representation.definition
				property_definition_representation
				property_definition_representation.used_representation ->
				representation
				representation.items[i] ->
				representation_item =>
				compound_representation_item
				
end_attribute_mapping;

attribute_mapping required_characteristic(required_characteristic, $PATH, Property_value_representation);
				predefined_requirement_view_definition <=
				product_definition
				characterized_product_definition = product_definition
				characterized_product_definition
				characterized_definition = characterized_product_definition
				characterized_definition <-
				property_definition.definition
				{property_definition.name = 'required characteristic'}
				property_definition <-
				property_definition_representation.definition
				property_definition_representation
				property_definition_representation.used_representation ->
				representation 
				
end_attribute_mapping;	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
    // (OPT) required_characteristic: characteristic_select;
	// PRVD <- PD <- PDR -> R (-> RI)
	public static void setRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRequired_characteristic(context, armEntity);

		if (armEntity.testRequired_characteristic(null))
		{

			EEntity ee = armEntity.getRequired_characteristic(null);
			// PRVD <- PD
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "required characteristic");
			
			if(ee instanceof ERepresentation){
				ERepresentation armRequired_coordinated_characteristic = (ERepresentation)ee;
				EProperty_definition_representation epdr = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				epdr.setUsed_representation(null, armRequired_coordinated_characteristic);
				epdr.setDefinition(null, epd);
			}
			else{
				ERepresentation_item armCharacteristic = (ERepresentation_item)ee;			
			   // AIM characteristic
				// GET AIM characteristics
				// ARepresentation_item aimCharacteristics = new ARepresentation_item();
	
			   jsdai.SRepresentation_schema.ARepresentation reps = CxAP210ARMUtilities.getAllRepresentationsOfCharacterizedDefinition(armEntity, context, null);
				jsdai.SRepresentation_schema.ERepresentation suitableRepresentation = null;
				top: for(int i=1;i<=reps.getMemberCount();i++){
					jsdai.SRepresentation_schema.ERepresentation temp = reps.getByIndex(i);
					// Exact types only - since all subtypes are intended for geometry - do not "touch" them
					if(temp.getInstanceType() == jsdai.SRepresentation_schema.CRepresentation.definition){
						suitableRepresentation = temp;
						break top;
					}
				}
			   if(suitableRepresentation == null){
				   suitableRepresentation = CxAP210ARMUtilities.createRepresentation(context, "", false);
				   ARepresentation_item items = suitableRepresentation.createItems(null);
				   items.addUnordered(armCharacteristic);
			   }
				// Create a new structure
				// PDR
				EProperty_definition_representation propDefRep = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.class);
				propDefRep.setDefinition(null, epd);
				propDefRep.setUsed_representation(null, suitableRepresentation);
			}
		}

	}


	/**
	* Unsets/deletes data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// RP <- PDR -> R - RI <- AGA -> CT
	// Remove this link
	public static void unsetRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		if (armEntity.testRequired_characteristic(null))
		{

			// ARepresentation reps = CxAP210ARMUtilities.getAllRepresentationsOfPropertyDefinition(armEntity, context, null);
			EProperty_definition property_definition = null;
			AProperty_definition aProperty_definition = new AProperty_definition();
			CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
			for (int j = 1; j <= aProperty_definition.getMemberCount(); j++) {
				property_definition = aProperty_definition.getByIndex(j);
				if((property_definition.testName(null))&&(property_definition.getName(null).equals("required characteristic"))){
					//property_definition_representation
					EProperty_definition_representation property_definition_representation = null;
					AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
					CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
					for (int i = 1; i <= aProperty_definition_representation.getMemberCount(); i++) {
						property_definition_representation = aProperty_definition_representation.getByIndex(i);
						property_definition_representation.deleteApplicationInstance();
					}
					property_definition.deleteApplicationInstance();
				}
			}
		}
	}

/* This attribute is INVERSE after modularization - need not to set it in Cx
	/
	* Sets/creates data for required_material attribute.
	*
	*
	*  attribute_mapping required_material_ee_material (required_material
	* , (*PATH*), ee_material);
	* 	requirements_property <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition
	* 	{product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `requirement'}
	* 	product_definition <-
	* 	product_definition_relationship.relating_product_definition
	* 	product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `required material'}
	* 	product_definition_relationship.related_product_definition ->
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	material_designation.definitions[i]
	* 	material_designation
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	
	// (Valdas) Created in follorwing path: Requirements_property -> requirement_definition <- material_designation

	// required_material: OPTIONAL ee_material;
	// RP -> PD (RD) <<- PDR ->> PD <- MD
	// OR
	// RP -> PD (RD) <- PDR -> PD <<- MD
	public static void setRequired_material(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRequired_material(context, armEntity);

		if (armEntity.testRequired_material(null))
		{

			EMaterial_designation armRequired_material = armEntity.getRequired_material(null);

			//property_definition.definition mus be set on creating of associated_definition: requirement_definition
			EProduct_definition product_definition = null;
			if (armEntity.testDefinition(null)) {
				product_definition = (EProduct_definition) armEntity.getDefinition(null);
			} else {
				throw new SdaiException(SdaiException.VA_NSET, "associated_definition must be created before required_part");
			}

			if (!armRequired_material.testDefinitions(null)) {
				armRequired_material.createDefinitions(null);
			}
			armRequired_material.getDefinitions(null).addUnordered(product_definition);
		}
	}
*/
	/* This attribute is INVERSE after modularization - need not to set it in Cx
	/
	* Unsets/deletes data for required_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	
	public static void unsetRequired_material(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		//product_definition
		EProduct_definition product_definition = null;
		if (armEntity.testDefinition(null)) {
			product_definition = (EProduct_definition) armEntity.getDefinition(null);
		} else {
			return;
		}

		//material_designation
		EMaterial_designation material_designation = null;
		AMaterial_designation aMaterial_designation = new AMaterial_designation();
		CMaterial_designation.usedinDefinitions(null, product_definition, context.domain, aMaterial_designation);
		for (int i = 1; i <= aMaterial_designation.getMemberCount(); i++) {
			material_designation = aMaterial_designation.getByIndex(i);
			ACharacterized_definition aDefinitions = material_designation.getDefinitions(null);
			while (aDefinitions.isMember(product_definition)) {
				aDefinitions.removeUnordered(product_definition);
			}
		}
	}
*/
// NEW	
   /**
   * Sets/creates data for required_functional_specification attribute.
   *
   *
   *  attribute_mapping required_functional_specification (required_functional_specification
   * );
		predefined_requirement_view_definition &lt;=
		product_definition
		characterized_product_definition = product_definition
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition &lt;-
		property_definition.definition
		{property_definition.name = 'required functional specification'}
		property_definition &lt;-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation -&gt;
		representation =&gt;
		functional_specification
   *  end_attribute_mapping;
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   // name: ee_name;
   // RP <- PDR -> FS
   public static void setRequired_functional_specification(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
   {
	   if (armEntity.testRequired_functional_specification(null))
	   {

			AFunctional_specification afsARM = armEntity.getRequired_functional_specification(null);
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "required functional specification");
			
			for(int i=1;i<=afsARM.getMemberCount();i++){
				EFunctional_specification efsARM = afsARM.getByIndex(i);
				
				EProperty_definition_representation epdr = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				epdr.setDefinition(null, epd);
				epdr.setUsed_representation(null, efsARM); 
			}
	   }
   }


   /**
   * Unsets/deletes data for name attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   public static void unsetRequired_functional_specification(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
   {
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int j = 1; j <= aProperty_definition.getMemberCount(); j++) {
			property_definition = aProperty_definition.getByIndex(j);
			if((property_definition.testName(null))&&(property_definition.getName(null).equals("required functional specification"))){
		   		AProperty_definition_representation apdr = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, apdr);
				for(int i=1;i<=apdr.getMemberCount();i++){
					EProperty_definition_representation epdr = apdr.getByIndex(i);
					epdr.deleteApplicationInstance(); 
				}
				property_definition.deleteApplicationInstance();
			}
		}
   }

   /**
    * Sets/creates data for required_functional_specification attribute.
    *
    *
    *  attribute_mapping required_functional_specification (required_functional_specification);
 	*  requirements_property
 	*  document_reference_item = requirements_property
 	*  document_reference_item &lt;-
 	*  applied_document_reference.items[i]
 	*  {applied_document_reference &lt;=
 	*  document_reference
 	*  document_reference.role -&gt;
 	*  object_role.name = 'text identifier in document'}
 	*  applied_document_reference 
    *  end_attribute_mapping;
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
    // Reference_clause: Document_definition;
    // RP <- ADR
   /* - made derived
    public static void setReference_clause(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
    {
 	   if (armEntity.testReference_clause(null))
 	   {
 	   	throw new SdaiException(SdaiException.FN_NAVL, "Setter is not available for this attribute");
 	   }
    }
*/

    /**
    * Unsets/deletes data for name attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
/* made derived   
    public static void unsetReference_clause(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
    {
 	   if (armEntity.testReference_clause(null))
 	   {
 	   	throw new SdaiException(SdaiException.FN_NAVL, "Setter is not available for this attribute");
 	   }
    }
*/
   
   /**
    * Sets/creates data for life_cycle_context attribute.
    *
    *
  	 attribute_mapping life_cycle_context(life_cycle_context, $PATH, life_cycle_context_type);
    	requirements_definition <=
    	product_definition					
    	product_definition.frame_of_reference ->
    	product_definition_context
    	product_definition_context.life_cycle_stage
    end_attribute_mapping;
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
    // name: ee_name;
    // RP <- PDR -> FS
   /* Removed
    public static void setLife_cycle_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
    {
    	unsetLife_cycle_context(context, armEntity);
 	   if (armEntity.testLife_cycle_context(null))
 	   {
 			int lifeCycleContext = armEntity.getLife_cycle_context(null);
 			String value = ELife_cycle_context_type.toString(lifeCycleContext);
 			
 			if(armEntity.testFrame_of_reference(null)){
 				EProduct_definition_context epdc = armEntity.getFrame_of_reference(null);
 				epdc.setLife_cycle_stage(null, value);
 			}
 			else{	
				LangUtils.Attribute_and_value_structure[] pdcStructure =
				{
					new LangUtils.Attribute_and_value_structure(
							CProduct_definition_context.attributeLife_cycle_stage(null), value)
				};
				EProduct_definition_context epdc = (EProduct_definition_context)
					LangUtils.createInstanceIfNeeded(context, CProduct_definition_context.definition, pdcStructure);
				armEntity.setFrame_of_reference(null, epdc);
 			}
 	   }
    }
*/

    /**
    * Unsets/deletes data for name attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
/* Removed   
    public static void unsetLife_cycle_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
    {
			if(armEntity.testFrame_of_reference(null)){
 				EProduct_definition_context epdc = armEntity.getFrame_of_reference(null);
 				epdc.unsetLife_cycle_stage(null);
 			}
    }
*/
    /**
     * Sets/creates data for domain_context attribute.
     *
     *
     attribute_mapping domain_context(domain_context, $PATH, domain_context_type);
     		requirements_property <=
     		product_definition
     		product_definition.frame_of_reference ->
     		product_definition_context <=
     		application_context_element
     		application_context_element.frame_of_reference ->
     		application_context
     		application_context.application
     end_attribute_mapping;
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
     // name: ee_name;
     // RP <- PDR -> FS
/* Removed   
     public static void setDomain_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
     {
  	   if (armEntity.testDomain_context(null))
  	   {
  			int domainContext = armEntity.getDomain_context(null);
  			String value = EDomain_context_type.toString(domainContext);
				LangUtils.Attribute_and_value_structure[] acStructure =
 				{
 					new LangUtils.Attribute_and_value_structure(
 							CApplication_context.attributeApplication(null), value)
 				};
 				EApplication_context eac = (EApplication_context)
 					LangUtils.createInstanceIfNeeded(context, CApplication_context.definition, acStructure);

  			
  			if(armEntity.testFrame_of_reference(null)){
  				EProduct_definition_context epdc = armEntity.getFrame_of_reference(null);
  				epdc.setFrame_of_reference(null, eac);
  			}
  			else{
				LangUtils.Attribute_and_value_structure[] pdcStructure =
				{
					new LangUtils.Attribute_and_value_structure(
							CProduct_definition_context.attributeFrame_of_reference(null), eac)
				};
				EProduct_definition_context epdc = (EProduct_definition_context)
					LangUtils.createInstanceIfNeeded(context, CProduct_definition_context.definition, pdcStructure);
				armEntity.setFrame_of_reference(null, epdc);
  			}
  	   }
     }
*/

     /**
     * Unsets/deletes data for name attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
/* Removed   
     public static void unsetDomain_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
     {
 			if(armEntity.testFrame_of_reference(null)){
  				EProduct_definition_context epdc = armEntity.getFrame_of_reference(null);
  				if(epdc.testFrame_of_reference(null)){
  					epdc.unsetFrame_of_reference(null);
  				}
  			}
     }
*/    
   
}
