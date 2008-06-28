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

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SAssembly_component_placement_requirements_mim.CItem_restricted_requirement_occurrence;
import jsdai.SGroup_mim.*;
import jsdai.SGroup_schema.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SRequirement_decomposition_xim.*;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxItem_restricted_requirement_occurrence_armx extends CItem_restricted_requirement_occurrence_armx implements EMappedXIMEntity
{

	
	// Taken from property_definition

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
	
	// Taken from CGroup
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EGroup type) throws SdaiException {
		return test_string(a11);
	}
	public String getDescription(EGroup type) throws SdaiException {
		return get_string(a11);
	}*/
	public void setDescription(EGroup type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetDescription(EGroup type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EGroup type) throws SdaiException {
		return a11$;
	}

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EGroup type) throws SdaiException {
		return test_string(a10);
	}
	public String getName(EGroup type) throws SdaiException {
		return get_string(a10);
	}*/
	public void setName(EGroup type, String value) throws SdaiException {
		a10 = set_string(value);
	}
	public void unsetName(EGroup type) throws SdaiException {
		a10 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EGroup type) throws SdaiException {
		return a10$;
	}
	
	// ENDOF taken from CGroup	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CItem_restricted_requirement_occurrence.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

			//required_analytical_representation
			setRequired_analytical_representation(context, this);

			setRequired_functional_specification(context, this);
			
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

	      //********** "Item_restricted_requirement_occurrence" attributes
	      // basis
	      setBasis(context, this);

	      // Clean ARM
	      // unsetAssociated_definition(null);
	      unsetRequired_analytical_representation(null);
	      unsetRequired_functional_specification(null);
	      // unsetRequired_part(null);
	      unsetRequired_characteristic(null);

	      unsetBasis(null);
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

		unsetRequired_functional_specification(context, this);
		//required_material - INVERSE
		// setRequired_material(context, this);

      //********** "Item_restricted_requirement_occurrence" attributes
      // basis
      unsetBasis(context, this);

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
	public static void setMappingConstraints(SdaiContext context, EItem_restricted_requirement_occurrence_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setMappingConstraints(context, armEntity);
		armEntity.setName((EGroup)null, "item restricted requirements property");
	}

	public static void unsetMappingConstraints(SdaiContext context, EItem_restricted_requirement_occurrence_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName((EGroup)null);

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
	* Sets/creates data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_functional_specification(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_functional_specification(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_functional_specification(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_functional_specification(context, armEntity);
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
	* Sets/creates data for required_part attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed
	public static void setRequired_part(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_part(context, armEntity);
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
		CxPredefined_requirement_view_definition_armx.unsetRequired_part(context, armEntity);
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

	//********** "item_restricted_requirement_occurrence" attributes

	/**
	* Sets/creates data for basis attribute.
	*
	* <p>
	*  attribute_mapping basis_part_feature (basis
	* , (*PATH*), part_feature);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = shape_aspect
	* 	shape_aspect
	* 	{([shape_aspect =>
	* 	composite_shape_aspect]
	* 	[shape_aspect
	* 	shape_aspect.description = `part group feature'])
	* 	(shape_aspect.description = `part generic feature')
	* 	(shape_aspect.description = `polarity indication feature')
	* 	(shape_aspect.description = `interconnect module edge segment surface')
	* 	(shape_aspect.description = `interconnect module edge surface')
	* 	(shape_aspect.description = `interconnect module primary surface')
	* 	(shape_aspect.description = `interconnect module secondary surface')
	* 	(shape_aspect.description = `interconnect module surface feature')
	* 	(shape_aspect =>
	* 	primary_orientation_feature)
	* 	(shape_aspect =>
	* 	secondary_orientation_feature)
	* 	(shape_aspect =>
	* 	package_body)
	* 	(shape_aspect =>
	* 	part_tooling_feature)
	* 	(shape_aspect =>
	* 	thermal_feature)
	* 	(shape_aspect =>
	* 	part_mounting_feature)
	* 	(shape_aspect =>
	* 	package_terminal)
	* 	(shape_aspect =>
	* 	assembly_module_terminal)
	* 	(shape_aspect =>
	* 	interconnect_module_terminal)
	* 	(shape_aspect =>
	* 	minimally_defined_bare_die_terminal)
	* 	(shape_aspect =>
	* 	packaged_part_terminal)
	* 	(shape_aspect =>
	* 	package_body_surface)}
	*  end_attribute_mapping;
	*  attribute_mapping basis_component_feature_external_reference (basis
	* , (*PATH*), component_feature_external_reference);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = representation_item
	* 	{representation_item
	* 	representation_item.name = `component feature external reference'}
	* 	representation_item =>
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	*  attribute_mapping basis_component_feature (basis
	* , (*PATH*), component_feature);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	(group_assigned_item = component_terminal)
	* 	(group_assigned_item = component_interface_terminal)
	* 	(group_assigned_item = laminate_component_interface_terminal)
	* 	(group_assigned_item = component_feature)
	*  end_attribute_mapping;
	*  attribute_mapping basis_ee_requirement_occurrence (basis
	* , (*PATH*), ee_requirement_occurrence);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = requirements_property
	* 	requirements_property
	*  end_attribute_mapping;
	*  attribute_mapping basis_component_external_reference (basis
	* , (*PATH*), component_external_reference);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = representation
	* 	{representation
	* 	representation.name = `component external reference'}
	* 	representation
	*  end_attribute_mapping;
	*  attribute_mapping basis_assembly_component (basis
	* , (*PATH*), assembly_component);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	(group_assigned_item = component_definition)
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping basis_component_feature_external_reference (basis
	* , (*PATH*), component_feature_external_reference);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = representation_item
	* 	{representation_item
	* 	representation_item.name = `component feature external reference'}
	* 	representation_item =>
	* 	descriptive_representation_item
	*  end_attribute_mapping;
	*  attribute_mapping basis_assembly_component (basis
	* , (*PATH*), assembly_component);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	(group_assigned_item = component_definition)
	* 	(group_assigned_item = component_shape_aspect)
	*  end_attribute_mapping;
	*  attribute_mapping basis_ee_requirement_occurrence (basis
	* , (*PATH*), ee_requirement_occurrence);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = requirements_property
	* 	requirements_property
	*  end_attribute_mapping;
	*  attribute_mapping basis_part_feature (basis
	* , (*PATH*), part_feature);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = shape_aspect
	* 	shape_aspect
	* 	{([shape_aspect =>
	* 	composite_shape_aspect]
	* 	[shape_aspect
	* 	shape_aspect.description = `part group feature'])
	* 	(shape_aspect.description = `part generic feature')
	* 	(shape_aspect.description = `polarity indication feature')
	* 	(shape_aspect.description = `interconnect module edge segment surface')
	* 	(shape_aspect.description = `interconnect module edge surface')
	* 	(shape_aspect.description = `interconnect module primary surface')
	* 	(shape_aspect.description = `interconnect module secondary surface')
	* 	(shape_aspect.description = `interconnect module surface feature')
	* 	(shape_aspect =>
	* 	primary_orientation_feature)
	* 	(shape_aspect =>
	* 	secondary_orientation_feature)
	* 	(shape_aspect =>
	* 	package_body)
	* 	(shape_aspect =>
	* 	part_tooling_feature)
	* 	(shape_aspect =>
	* 	thermal_feature)
	* 	(shape_aspect =>
	* 	part_mounting_feature)
	* 	(shape_aspect =>
	* 	package_terminal)
	* 	(shape_aspect =>
	* 	assembly_module_terminal)
	* 	(shape_aspect =>
	* 	interconnect_module_terminal)
	* 	(shape_aspect =>
	* 	minimally_defined_bare_die_terminal)
	* 	(shape_aspect =>
	* 	packaged_part_terminal)
	* 	(shape_aspect =>
	* 	package_body_surface)}
	*  end_attribute_mapping;
	*  attribute_mapping basis_component_external_reference (basis
	* , (*PATH*), component_external_reference);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = representation
	* 	{representation
	* 	representation.name = `component external reference'}
	* 	representation
	*  end_attribute_mapping;
	*  attribute_mapping basis_inter_stratum_feature (basis
	* , (*PATH*), inter_stratum_feature);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = inter_stratum_feature
	* 	inter_stratum_feature
	* 	{inter_stratum_feature <=
	* 	component_shape_aspect <=
	* 	shape_aspect
	* 	[(shape_aspect.description = `bonded conductive base blind via')
	* 	(shape_aspect.description = `non conductive base blind via')
	* 	(shape_aspect.description = `plated conductive base blind via')
	* 	(shape_aspect.description = `interfacial connection')
	* 	(shape_aspect.description = `buried via')
	* 	(shape_aspect.description = `component termination passage')
	* 	(shape_aspect.description = `plated cutout')
	* 	(shape_aspect.description = `plated cutout edge segment')
	* 	(shape_aspect.description = `plated interconnect module edge segment')
	* 	(shape_aspect.description = `unsupported passage')
	* 	(shape_aspect.description = `cutout')
	* 	(shape_aspect.description = `physical connectivity interrupting cutout')
	* 	(shape_aspect.description = `dielectric material passage')
	* 	(shape_aspect.description = `cutout edge segment')
	* 	(shape_aspect.description = `interconnect module edge segment')
	* 	(shape_aspect.description = `interconnect module edge')]
	* 	[shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	[product_definition
	* 	product_definition.formation ->
	* 	product_definition_formation
	* 	product_definition_formation.of_product ->
	* 	product <-
	* 	product_related_product_category.products[i]
	* 	product_related_product_category <=
	* 	product_category
	* 	product_category.name = `interconnect module']
	* 	[product_definition =>
	* 	component_definition]]}
	*  end_attribute_mapping;
	*  attribute_mapping basis_stratum_feature (basis
	* , (*PATH*), stratum_feature);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = stratum_feature
	* 	stratum_feature
	* 	{stratum_feature <=
	* 	shape_aspect}
	*  end_attribute_mapping;
	*  attribute_mapping basis_component_feature (basis
	* , (*PATH*), component_feature);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	(group_assigned_item = component_terminal)
	* 	(group_assigned_item = component_interface_terminal)
	* 	(group_assigned_item = laminate_component_interface_terminal)
	* 	(group_assigned_item = component_feature)
	*  end_attribute_mapping;
	*  attribute_mapping basis_stratum_surface (basis
	* , (*PATH*), stratum_surface);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = stratum_surface
	* 	stratum_surface
	*  end_attribute_mapping;
	*  attribute_mapping basis_stratum (basis
	* , (*PATH*), stratum);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	group_assigned_item = stratum
	* 	stratum
	* 	{stratum <=
	* 	product_definition}
	*  end_attribute_mapping;
	*  attribute_mapping basis_laminate_component (basis
	* , (*PATH*), laminate_component);
	* 	grouped_requirements_property <=
	* 	group <-
	* 	group_assignment.assigned_group
	* 	group_assignment =>
	* 	applied_group_assignment
	* 	applied_group_assignment.items[i] ->
	* 	group_assigned_item
	* 	(group_assigned_item = component_definition
	* 	{component_definition <=
	* 	product_definition
	* 	product_definition.description = `laminate component'})
	* 	(group_assigned_item = component_shape_aspect)
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> xxx
	public static void setBasis(SdaiContext context, EItem_restricted_requirement_occurrence_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetBasis(context, armEntity);

		if (armEntity.testBasis(null))
		{

			EEntity armBasis = armEntity.getBasis(null);
			// xxx
			// GRP <- AGA
			LangUtils.Attribute_and_value_structure[] agaStructure =
				{new LangUtils.Attribute_and_value_structure(
					CApplied_group_assignment.attributeAssigned_group(null),
					armEntity)
				};
			EApplied_group_assignment assignment = (EApplied_group_assignment)
				LangUtils.createInstanceIfNeeded(context, CApplied_group_assignment.definition, agaStructure);
			// AGA -> xxx
		   AGroupable_item items;
			if(assignment.testItems(null))
				items = assignment.getItems(null);
			else
				items = assignment.createItems(null);
		   items.addUnordered(armBasis);
		}
	}


	/**
	* Unsets/deletes data for basis attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> xxx, delete AGAs
	public static void unsetBasis(SdaiContext context, EItem_restricted_requirement_occurrence_armx armEntity) throws SdaiException
	{
		// xxx
		AApplied_group_assignment assignments = new AApplied_group_assignment();
		CApplied_group_assignment.usedinAssigned_group(null, armEntity, context.domain, assignments);
		for(int i=1;i<=assignments.getMemberCount();){
			EApplied_group_assignment assignment = assignments.getByIndex(i);
			assignments.removeByIndex(i);
			assignment.deleteApplicationInstance();
		}
	}
	

}
