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

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SInterconnect_placement_requirements_mim.CLayout_spacing_requirement_occurrence;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SRequirement_decomposition_xim.*;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxLayout_spacing_requirement_occurrence_armx extends CLayout_spacing_requirement_occurrence_armx implements EMappedXIMEntity
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

	public static jsdai.dictionary.EAttribute attributeDefinition(EProduct_definition type) throws SdaiException {
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

			setTemp("AIM", CLayout_spacing_requirement_occurrence.definition);

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

	      //********** "layout_spacing_requirement_occurrence" attributes
	      //reference_design_object_category
	      setDesign_object_category_1(context, this);

	      //dependent_design_object_category
	      setDesign_object_category_2(context, this);

	      // New since WD26
	      setOf_spacing_type(context, this);

	      // life_cycle_context                                                : life_cycle_context_type;
	      // setLife_cycle_context(context, this);
	      
	      // domain_context                                                    : domain_context_type;
	      // setDomain_context(context, this);

	      // Clean ARM
	      // unsetAssociated_definition(null);
	      unsetRequired_analytical_representation(null);
	      unsetRequired_functional_specification(null);
	      // unsetRequired_part(null);
	      unsetRequired_characteristic(null);

	      unsetDesign_object_category_1(null);
	      unsetDesign_object_category_2(null);
	      unsetOf_spacing_type(null);

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
		// setRequired_material(context, this);

      //********** "layout_spacing_requirement_occurrence" attributes
      //reference_design_object_category
      unsetDesign_object_category_1(context, this);

      //dependent_design_object_category
      unsetDesign_object_category_2(context, this);

      // New since WD26
      unsetOf_spacing_type(context, this);

      // life_cycle_context                                                : life_cycle_context_type;
      // unsetLife_cycle_context(context, this);
      
      // domain_context                                                    : domain_context_type;
      // unsetDomain_context(context, this);
      
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	*  mapping_constraints;
	* 	grouped_requirements_property <=
	* 	[group
	* 	{group
	* 	group.name = `layout spacing requirements property'}]
	* 	[requirements_property <=
	* 	property_definition]
	*  end_mapping_constraints;
	*
	*  mapping_constraints;
	* 	grouped_requirements_property <=
	* 	[group
	* 	{group
	* 	group.name = `layout spacing requirements property'}]
	* 	[requirements_property <=
	* 	property_definition]
	*  end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetMappingConstraints(context, armEntity);

	}
	
	
	//********** "managed_design_object" attributes

	//********** "Requirement_definition_property" attributes

	/**
	* Sets/creates data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_analytical_representation(context, armEntity);
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
		CxPredefined_requirement_view_definition_armx.unsetRequired_analytical_representation(context, armEntity);
	}


	/**
	* Sets/creates data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void setRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.setRequired_specification(context, armEntity);
	}
*/

	/**
	* Unsets/deletes data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void unsetRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.unsetRequired_specification(context, armEntity);
	}
*/

	/**
	* Sets/creates data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_characteristic(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_characteristic(context, armEntity);
	}

/* This is now inverse attribute
	/
	* Sets/creates data for required_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void setRequired_material(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_material(context, armEntity);
	}


	/
	* Unsets/deletes data for required_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void unsetRequired_material(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.unsetRequired_material(context, armEntity);
	}
*/
	//********** "layout_spacing_requirement_occurrence" attributes

	/**
	* Sets/creates data for reference_design_object_category attribute.
	*
	*
	*  attribute_mapping reference_design_object_category (reference_design_object_category
	* );
	* Changed PART
	grouped_requirements_property<=
	group <-
	group_assignment.assigned_group
	group_assignment =>
	applied_group_assignment
	applied_group_assignment.items[i] ->
	group_assigned_item
	group_assigned_item = design_object
	{design_object <=
	characterized_object
	characterized_object.description = `dependent design object category'}
	* 	characterized_object
	* 	characterized_object.name
	* 	{
	* 	(characterized_object.name = `stratum feature')
	* 	(characterized_object.name = `via')
	* 	(characterized_object.name = `fill area')
	* 	(characterized_object.name = `cutout')
	* 	(characterized_object.name = `component feature')
	* 	(characterized_object.name = `assembly component')
	* 	(characterized_object.name = `altered package')}
	*  end_attribute_mapping;
	*
	*  attribute_mapping reference_design_object_category (reference_design_object_category
	* );
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	groupable_item
	* 	groupable_item = requirements_property
	* 	requirements_property <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_object
	* 	{[characterized_object =>
	* 	design_object]
	* 	[characterized_object
	* 	characterized_object.description = `reference design object category']}
	* 	characterized_object
	* 	characterized_object.name
	* 	{(characterized_object.name = `inter stratum feature')
	* 	(characterized_object.name = `stratum feature')
	* 	(characterized_object.name = `via')
	* 	(characterized_object.name = `fill area')
	* 	(characterized_object.name = `cutout')
	* 	(characterized_object.name = `component feature')
	* 	(characterized_object.name = `assembly component')
	* 	(characterized_object.name = `altered package')}
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> DO, where DO should be created/deleted always
	public static void setDesign_object_category_2(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
	{
		if (armEntity.testDesign_object_category_2(null) != 0)
		{
			int armReference_design_object_category = armEntity.getDesign_object_category_2(null, (EPca_product_design_object_category)null);

			//unset old values
			unsetDesign_object_category_2(context, armEntity);
			// Create ALL a new
			// DO
			ECharacterized_object eco = (ECharacterized_object)
				context.working_model.createEntityInstance(CCharacterized_object.definition);
		   // In the future we may need smarter way to determine this string.
		   // But for now it is default value
		   // eco.setName((EGroup)null, "interconnect module");
		   // Changed since WD38
		   String categoryString = EPca_product_design_object_category.toString(armReference_design_object_category).toLowerCase().replace('_', ' ');
		   eco.setName((ECharacterized_object)null, categoryString);
		   // PD
		   EProperty_definition epd = (EProperty_definition)
		   	context.working_model.createEntityInstance(CProperty_definition.definition);
		   epd.setDefinition(null, eco);
		   epd.setName(null, "");
		   // <- PD
           LangUtils.Attribute_and_value_structure[] pdStructure = {
               new LangUtils.Attribute_and_value_structure(
               CProperty_definition.attributeDefinition(null), armEntity)};
           EProperty_definition epd2 = (EProperty_definition)
               LangUtils.createInstanceIfNeeded(context,
               CProperty_definition.definition,
               pdStructure);
           if (!epd2.testName(null))
        	   epd2.setName(null, "");
		   // PDR
           EProperty_definition_relationship epdr = (EProperty_definition_relationship)
           		context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
           epdr.setRelated_property_definition(null, epd);
           epdr.setRelating_property_definition(null, epd2);
           epdr.setName(null, "design object category 2");
           epdr.setDescription(null, "");
      }
   }


	/**
	* Unsets/deletes data for reference_design_object_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> DO, remove DO
	public static void unsetDesign_object_category_2(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
	{
		String keyword = "design object category 2";
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterPD = apd.createIterator();
		while(iterPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterPD);
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelating_property_definition(null, epd, context.domain, apdr);
			SdaiIterator iterPDR = apdr.createIterator();
			while(iterPDR.next()){
				EProperty_definition_relationship epdr = apdr.getCurrentMember(iterPDR);
				if((epdr.testName(null))&&(epdr.getName(null).equals(keyword))){
					epdr.deleteApplicationInstance();
				}
			}			
		}
	}


	/**
	* Sets/creates data for dependent_design_object_category attribute.
	*
	*
	*  attribute_mapping dependent_design_object_category (dependent_design_object_category
	* );
	// Changed PART
	attribute_mapping design_object_category_1(design_object_category_1, group);
		layout_spacing_requirement_occurrence
		groupable_item = assembly_spacing_requirement_occurrence 
		groupable_item <-
		applied_group_assignment.items[i]
		applied_group_assignment <=
		group_assignment 
		{group_assignment.role = 'design object category 1'}
		group_assignment.assigned_group ->
		group 
		{!{(group.name = 'assembly module')
		(group.name = 'interconnect module')}}
	end_attribute_mapping;
	* 	characterized_object
	* 	characterized_object.name
	* 	{
	* 	(characterized_object.name = `stratum feature')
	* 	(characterized_object.name = `via')
	* 	(characterized_object.name = `fill area')
	* 	(characterized_object.name = `cutout')
	* 	(characterized_object.name = `component feature')
	* 	(characterized_object.name = `assembly component')
	* 	(characterized_object.name = `altered package')}
	*  end_attribute_mapping;
	*
		layout_spacing_requirement_occurrence <=
		predefined_requirement_view_definition <=
		product_definition			
		characterized_product_definition = product_definition
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.relating_property_definition
		{property_definition_relationship.name = 'design object category 1'}
		property_definition_relationship.related_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_object
		characterized_object
		{characterized_object =>
		design_object <=
		group
		{(group.name = 'assembly module')
		(group.name = 'interconnect module')}}
		characterized_object.name
		{(characterized_object.name = 'assembly component category')
		(characterized_object.name = 'assembly module category')
		...
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// LSRO <- PD <- PDR -> PD -> DO
	public static void setDesign_object_category_1(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
	{
		if (armEntity.testDesign_object_category_1(null) != 0)
		{
			int armDependent_design_object_category = armEntity.getDesign_object_category_1(null, (EPca_product_design_object_category)null);

			//unset old values
			unsetDesign_object_category_1(context, armEntity);
			// Create ALL a new
			// DO
			ECharacterized_object eco = (ECharacterized_object)
				context.working_model.createEntityInstance(CCharacterized_object.definition);
		   // In the future we may need smarter way to determine this string.
		   // But for now it is default value
		   // eco.setName((EGroup)null, "interconnect module");
		   // Changed since WD38
		   String categoryString = EPca_product_design_object_category.toString(armDependent_design_object_category).toLowerCase().replace('_', ' ');
		   eco.setName((ECharacterized_object)null, categoryString);
		   // PD
		   EProperty_definition epd = (EProperty_definition)
		   	context.working_model.createEntityInstance(CProperty_definition.definition);
		   epd.setDefinition(null, eco);
		   epd.setName(null, "");
		   // <- PD
           LangUtils.Attribute_and_value_structure[] pdStructure = {
               new LangUtils.Attribute_and_value_structure(
               CProperty_definition.attributeDefinition(null), armEntity)};
           EProperty_definition epd2 = (EProperty_definition)
               LangUtils.createInstanceIfNeeded(context,
               CProperty_definition.definition,
               pdStructure);
           if (!epd2.testName(null))
        	   epd2.setName(null, "");
		   // PDR
           EProperty_definition_relationship epdr = (EProperty_definition_relationship)
           		context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
           epdr.setRelated_property_definition(null, epd);
           epdr.setRelating_property_definition(null, epd2);
           epdr.setName(null, "design object category 1");
           epdr.setDescription(null, "");
      }
   }


	/**
	* Unsets/deletes data for dependent_design_object_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDesign_object_category_1(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
	{
		String keyword = "design object category 1";
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterPD = apd.createIterator();
		while(iterPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterPD);
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelating_property_definition(null, epd, context.domain, apdr);
			SdaiIterator iterPDR = apdr.createIterator();
			while(iterPDR.next()){
				EProperty_definition_relationship epdr = apdr.getCurrentMember(iterPDR);
				if((epdr.testName(null))&&(epdr.getName(null).equals(keyword))){
					epdr.deleteApplicationInstance();
				}
			}			
		}
	}

// New since WD26
   /**
   * Sets/creates data for reference_design_object_category attribute.
	attribute_mapping of_spacing_type(of_spacing_type, $PATH);
		layout_spacing_requirement_occurrence <=
		predefined_requirement_view_definition <=
		product_definition			
		characterized_product_definition = product_definition
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition
		{property_definition.name = 'of spacing type'}
		property_definition.description
		{(property_definition.description = 'nearest boundary')
		(property_definition.description = 'centroid')
		(property_definition.description = 'furthest boundary')}

	end_attribute_mapping;   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
	// LSRO <- PD
   public static void setOf_spacing_type(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
   {
      //unset old values
      unsetOf_spacing_type(context, armEntity);

      if (armEntity.testOf_spacing_type(null))
      {
         int armOf_sacing_type = armEntity.getOf_spacing_type(null);
         EProperty_definition epd = (EProperty_definition)context.working_model.createEntityInstance(CProperty_definition.definition);
         epd.setDefinition(null, armEntity);
         epd.setName(null, "of spacing type");
         epd.setDescription(null, ESpacing_type.toString(armOf_sacing_type).replace('_', ' ').toLowerCase());
      }
   }


   /**
   * Unsets/deletes data for reference_design_object_category attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   public static void unsetOf_spacing_type(SdaiContext context, ELayout_spacing_requirement_occurrence_armx armEntity) throws SdaiException
   {
         AProperty_definition apd = new AProperty_definition();
         CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
         SdaiIterator iter = apd.createIterator();
         while(iter.next()){
        	 EProperty_definition epd = apd.getCurrentMember(iter);
        	 if((epd.testName(null))&&(epd.getName(null).equals("of spacing type"))){
        		 epd.deleteApplicationInstance();
        	 }
         }
   }


   
}
