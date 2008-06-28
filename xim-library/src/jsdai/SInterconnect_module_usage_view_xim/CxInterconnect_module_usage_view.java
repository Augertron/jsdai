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

package jsdai.SInterconnect_module_usage_view_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SExtended_geometric_tolerance_xim.ERestraint_condition;
import jsdai.SFunctional_usage_view_xim.EFunctional_unit_usage_view;
import jsdai.SPhysical_unit_usage_view_mim.*;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxInterconnect_module_usage_view
		extends
		CInterconnect_module_usage_view implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition
	
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

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}

	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a8);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a8 = get_instance_select(a8);
		return (EEntity)a8;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a8 = set_instance(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
	}
	
	// Taken from Physical_unit - Property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a6$;
	}
	// ENDOF Taken from Physical_unit - Property_definition
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPhysical_unit.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			// From Item_shape
			setId_x(context, this);

			// SETTING DERIVED
			// setDefinition(null, this);

			// Interconnect_module_usage_view
	      setThickness_over_metal_requirement(context, this);
	      setThickness_over_dielectric_requirement(context, this);
	      setMeasurement_condition(context, this);
	      setLocated_thickness_requirement(context, this);
	      setImplemented_function(context, this);
			
			
			// Clean ARM specific attributes - this is DERIVED to some magic string
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			unsetId_x(null);

	      unsetThickness_over_metal_requirement(null);
	      unsetThickness_over_dielectric_requirement(null);
	      unsetMeasurement_condition(null);
	      unsetLocated_thickness_requirement(null);
	      unsetImplemented_function(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization - this is DERIVED to some magic string
			// unsetAdditional_characterization(context, this);

			//id_x
			unsetId_x(context, this);
			
			// unsetDefinition(null);

			// Interconnect_module_usage_view
	      unsetThickness_over_metal_requirement(context, this);
	      unsetThickness_over_dielectric_requirement(context, this);
	      unsetMeasurement_condition(context, this);
	      unsetLocated_thickness_requirement(context, this);
	      unsetImplemented_function(context, this);

			// this.deleteApplicationInstance();
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; physical_unit &lt;=
	 *  product_definition
	 *  {product_definition
	 *  product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'physical design usage'}
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
			EInterconnect_module_usage_view armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_usage_view.setMappingConstraints(context, armEntity);

		armEntity.setName(null, "interconnect module");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EInterconnect_module_usage_view armEntity) throws SdaiException {
		CxPart_usage_view.unsetMappingConstraints(context, armEntity);

		armEntity.unsetName(null);
	}

	//********** "design_discipline_item_definition" attributes
	public static void setId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		//unset old values
		CxItem_shape.setId_x(context, armEntity);
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);	
	}


	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * <p>
	 * attribute_mapping additional_context_application_context
	 * (additional_context , $PATH, application_context); product_definition <-
	 * product_definition_context_association.definition
	 * product_definition_context_association
	 * {product_definition_context_association.role -> (* Modified by Audronis
	 * Gustas *) product_definition_context_role
	 * product_definition_context_role.name = 'additional context'}
	 * product_definition_context_association.frame_of_reference ->
	 * product_definition_context end_attribute_mapping;
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
	}

	//********** "interconnect_module_usage_view" attributes

	/**
	* Sets/creates data for thickness_over_metal_requirement attribute.
	*
	* <p>
	*  attribute_mapping minimum_thickness_over_metal_requirement_length_data_element (minimum_thickness_over_metal_requirement
	* , (*PATH*), length_data_element);
	* 	(physical_unit <=)
	* 	(externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	(library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition
	* 	{property_definition
	* 	property_definition.name = `interconnect module usage view physical characteristics'}
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `minimum thickness over metal requirement'}
	* 	representation_item =>
	* 	measure_representation_item <=
	* 	measure_with_unit =>
	* 	length_measure_with_unit
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setThickness_over_metal_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		//unset old values
		unsetThickness_over_metal_requirement(context, armEntity);

		if (armEntity.testThickness_over_metal_requirement(null))
		{
	      ETolerance_characteristic characteristic = armEntity.getThickness_over_metal_requirement(null);
	      
	      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "thickness over metal requirement", characteristic);
		}
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetThickness_over_metal_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "thickness over metal requirement");
	}


	/**
	* Sets/creates data for thickness_over_dielectric_requirement attribute.
	*
	* <p>
	*  attribute_mapping thickness_over_dielectric_requirement_length_data_element (minimum_thickness_over_dielectric_requirement
	* , (*PATH*), length_data_element);
	* 	(physical_unit <=)
	* 	(externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	(library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition
	* 	{property_definition
	* 	property_definition.name = `interconnect module usage view physical characteristics'}
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `minimum thickness over dielectric requirement'}
	* 	representation_item =>
	* 	measure_representation_item <=
	* 	measure_with_unit =>
	* 	length_measure_with_unit
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setThickness_over_dielectric_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		//unset old values
		unsetThickness_over_dielectric_requirement(context, armEntity);

		if (armEntity.testThickness_over_dielectric_requirement(null))
		{
	      ETolerance_characteristic characteristic = armEntity.getThickness_over_dielectric_requirement(null);
	      
	      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "thickness over dielectric requirement", characteristic);
		}
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_dielectric_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetThickness_over_dielectric_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "thickness over dielectric requirement");
	}


	/**
	* Sets/creates data for measurement_condition attribute.
	*
	* <p>
	*  attribute_mapping measurement_condition_restraint_condition (measurement_condition
	* , (*PATH*), restraint_condition);
	* 	(physical_unit <=)
	* 	(externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	(library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition
	* 	{property_definition
	* 	(property_definition.description = `restraint')
	* 	(property_definition.description = `tolerance specific restraint')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMeasurement_condition(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		//unset old values
		unsetMeasurement_condition(context, armEntity);

		if (armEntity.testMeasurement_condition(null))
		{

			ERestraint_condition armMeasurement_condition = armEntity.getMeasurement_condition(null);

			armMeasurement_condition.setDefinition(null, armEntity);
		}
	}


	/**
	* Unsets/deletes data for measurement_condition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMeasurement_condition(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);

			if (property_definition.testDescription(null)
			   && (property_definition.getDescription(null).equals("restraint") || property_definition.getDescription(null).equals("tolerance specific restraint")))
			{
				property_definition.unsetDefinition(null);
			}
		}
	}


	/**
	* Sets/creates data for located_thickness_requirement attribute.
	*
	* <p>
	*  attribute_mapping located_thickness_requirement_located_interconnect_module_thickness_requirement (located_thickness_requirement
	* , (*PATH*), located_interconnect_module_thickness_requirement);
	* 	(physical_unit <=)
	* 	(externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	(library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition =>
	* 	product_definition_shape <-
	* 	shape_aspect.of_shape
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	{property_definition
	* 	[property_definition.name = `located interconnect module thickness']}
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocated_thickness_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		//unset old values
		unsetLocated_thickness_requirement(context, armEntity);

		if (armEntity.testLocated_thickness_requirement(null))
		{

			ALocated_interconnect_module_thickness_requirement armLocated_thickness_requirements = 
				armEntity.getLocated_thickness_requirement(null);
			SdaiIterator iter = armLocated_thickness_requirements.createIterator();
			while(iter.next()){	
				ELocated_interconnect_module_thickness_requirement armThickness = armLocated_thickness_requirements.getCurrentMember(iter);	
			
				//property_definition_shape
			   LangUtils.Attribute_and_value_structure[] propertyDefintionShapeStructure =
														 {new LangUtils.Attribute_and_value_structure(
															  CProduct_definition_shape.attributeDefinition(null), armEntity),
														};
			   EProduct_definition_shape product_definition_shape = (EProduct_definition_shape)
																		 LangUtils.createInstanceIfNeeded(context, CProduct_definition_shape.definition, propertyDefintionShapeStructure);
				if (!product_definition_shape.testName(null)) {
					product_definition_shape.setName(null, "");
				}
	
				//shape_aspect
				EShape_aspect shape_aspect = null;
				AShape_aspect aShape_aspect = new AShape_aspect();
				CShape_aspect.usedinOf_shape(null, product_definition_shape, context.domain, aShape_aspect);
				if (aShape_aspect.getMemberCount() > 0) {
					shape_aspect = aShape_aspect.getByIndex(1);
				} else {
					shape_aspect = (EShape_aspect) context.working_model.createEntityInstance(EShape_aspect.class);
					shape_aspect.setOf_shape(null, product_definition_shape);
				}
	
				if (!shape_aspect.testName(null)) {
					shape_aspect.setName(null, "");
				}
	
				if (!shape_aspect.testProduct_definitional(null)) {
					shape_aspect.setProduct_definitional(null, ELogical.UNKNOWN);
				}
	
				//property_definition
				EProperty_definition property_definition = (EProperty_definition) context.working_model.createEntityInstance(EProperty_definition.class);
				property_definition.setName(null, "located interconnect module thickness");
				property_definition.setDefinition(null, shape_aspect);
	
				armThickness.setDefinition(null, property_definition);
			}
		}
	}


	/**
	* Unsets/deletes data for located_thickness_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocated_thickness_requirement(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		//Product_definition_shape
		EProduct_definition_shape product_definition_shape = null;
		AProduct_definition_shape aProduct_definition_shape = new AProduct_definition_shape();
		CProduct_definition_shape.usedinDefinition(null, armEntity, context.domain, aProduct_definition_shape);
		for (int i = 1; i <= aProduct_definition_shape.getMemberCount(); i++) {
			product_definition_shape = aProduct_definition_shape.getByIndex(i);

			//shape_aspect
			EShape_aspect shape_aspect = null;
			AShape_aspect aShape_aspect = new AShape_aspect();
			CShape_aspect.usedinOf_shape(null, product_definition_shape, context.domain, aShape_aspect);
			for (int j = 1; j <= aShape_aspect.getMemberCount(); j++) {
				shape_aspect = aShape_aspect.getByIndex(j);

				//property_definition
				EProperty_definition property_definition = null;
				AProperty_definition aProperty_definition = new AProperty_definition();
				CProperty_definition.usedinDefinition(null, shape_aspect, context.domain, aProperty_definition);
				for (int k = 1; k <= aProperty_definition.getMemberCount(); k++) {
					property_definition = aProperty_definition.getByIndex(k);
					if (property_definition.testName(null) && property_definition.getName(null).equals("located interconnect module thickness")) {
						property_definition.deleteApplicationInstance();
					}
				}

			}
		}
	}


	/**
	* Sets/creates data for implemented_function attribute.
	*
	* <p>
	*  attribute_mapping implemented_function_functional_unit_usage_view (implemented_function
	* , (*PATH*), functional_unit_usage_view);
	* 	(physical_unit <=)
	* 	(externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	(library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=
	* 	physical_unit <=)
	* 	product_definition <-
	* 	product_definition_relationship.related_product_definition
	* 	product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `implemented function'}
	* 	product_definition_relationship.relating_product_definition ->
	* 	{product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `functional design usage'}
	* 	product_definition =>
	* 	(functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit =>
	* 	library_defined_functional_unit)
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setImplemented_function(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{
		//unset old values
		unsetImplemented_function(context, armEntity);

		if (armEntity.testImplemented_function(null))
		{
			EFunctional_unit_usage_view armImplemented_function = armEntity.getImplemented_function(null);

			//product_definition_relationship
			jsdai.SProduct_definition_schema.EProduct_definition_relationship relationship = (jsdai.SProduct_definition_schema.EProduct_definition_relationship) context.working_model.createEntityInstance(jsdai.SProduct_definition_schema.CProduct_definition_relationship.definition);
			relationship.setId(null, "");
			relationship.setName(null, "implemented function");
			relationship.setRelated_product_definition(null, armEntity);
			relationship.setRelating_product_definition(null, armImplemented_function);
		}
	}


	/**
	* Unsets/deletes data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplemented_function(SdaiContext context, EInterconnect_module_usage_view armEntity) throws SdaiException
	{

		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
		EProduct_definition_relationship relationship = null;
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);
		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);

			if (relationship.testName(null) && relationship.getName(null).equals("implemented function")) {
				relationship.deleteApplicationInstance();
			}
		}
	}


}