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

package jsdai.SPart_feature_grouping_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SMixed_complex_types.CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SPart_feature_grouping_mim.*;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxPart_rectangular_array_feature extends CPart_rectangular_array_feature implements EMappedXIMEntity{

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

		setTemp("AIM", CRectangular_composite_array_shape_aspect.definition);

		setMappingConstraints(context, this);
		
		// Part_feature
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);

		// element
		setElement(context, this);
		
      // Initial_row_positive_y_displacement
		setInitial_row_positive_y_displacement(context, this);
		
      // Terminus_row_positive_y_displacement
		setTerminus_row_positive_y_displacement(context, this);
		
      // location_uncertainty 		
		setLocation_uncertainty(context, this);
		
		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);

		// element
		unsetElement(null);
		
      // Initial_row_positive_y_displacement
		unsetInitial_row_positive_y_displacement(null);
		
      // Terminus_row_positive_y_displacement
		unsetTerminus_row_positive_y_displacement(null);
		
      // location_uncertainty 		
		unsetLocation_uncertainty(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);

			// element
			unsetElement(context, this);
			
	      // Initial_row_positive_y_displacement
			unsetInitial_row_positive_y_displacement(context, this);
			
	      // Terminus_row_positive_y_displacement
			unsetTerminus_row_positive_y_displacement(context, this);
			
	      // location_uncertainty 		
			unsetLocation_uncertainty(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  linear_composite_array_shape_aspect &lt;=
	 *  composite_array_shape_aspect &lt;=
	 *  composite_shape_aspect &lt;=
	 *  shape_aspect
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
			EPart_rectangular_array_feature armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_array_feature.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPart_rectangular_array_feature armEntity) throws SdaiException {
		CxPart_array_feature.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for material_state_change.
	 * 
	 * <p>
	 *  shape_aspect &lt;-
	 *  shape_aspect_relationship.related_shape_aspect
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'precedent feature'}
	 *  shape_aspect_relationship.relating_shape_aspect -&gt;
	 *  shape_aspect
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
////////////////////////////////////////////////////////////////////
	/**
	* Sets/creates data for material_state_change attribute.
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setMaterial_state_change(context, armEntity);
	}


	/**
	* Unsets/deletes data for material_state_change attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetMaterial_state_change(context, armEntity);		
	}


	/**
	* Sets/creates data for precedent_feature attribute.
	*
	*  attribute_mapping precedent_feature_part_feature (precedent_feature
	* , (*PATH*), part_feature);
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.setPrecedent_feature(context, armEntity);
	}


	/**
	* Unsets/deletes data for precedent_feature attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		CxPart_feature.unsetPrecedent_feature(context, armEntity);		
	}

	//********** "part_linear_array_feature" attributes

	/**
	* Sets/creates data for element attribute.
	*
	* <p>
	*  attribute_mapping element_part_feature_placement_link (element
	* , (*PATH*), part_feature_placement_link);
	* 	linear_composite_array_shape_aspect <=
	* 	composite_array_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `element'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	composite_array_shape_aspect_link
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setElement(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
		//unset old values
		unsetElement(context, armEntity);

		if (armEntity.testElement(null))
		{

			APart_linear_array_feature_link aArmElement = armEntity.getElement(null);

			EPart_linear_array_feature_link armElement = null;

			for (int i = 1; i <= aArmElement.getMemberCount(); i++) {
				armElement = aArmElement.getByIndex(i);

				//shape_aspect_relationship
				EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
				shape_aspect_relationship.setName(null, "element");
				shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
				shape_aspect_relationship.setRelating_shape_aspect(null, armElement);
			}
		}
	}


	/**
	* Unsets/deletes data for element attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetElement(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("element")) {
				relationship.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for terminus_node_positive_x_displacement attribute.
	*
	* <p>
	*  attribute_mapping terminus_node_positive_x_displacement_length_data_element (terminus_node_positive_x_displacement
	* , (*PATH*), length_data_element);
	* 	linear_composite_array_shape_aspect <=
	* 	composite_array_shape_aspect <=
	* 	composite_shape_aspect <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition
	* 	{property_definition
	* 	property_definition.name = `linear array composite properties'}
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	{[representation
	* 	representation.name = `linear array properties']
	* 	[representation
	* 	representation.context_of_items ->
	* 	representation_context
	* 	[representation_context
	* 	representation_context =>
	* 	global_unit_assigned_context]
	* 	[representation_context
	* 	representation_context =>
	* 	global_uncertainty_assigned_context]
	* 	[representation_context
	* 	representation_context =>
	* 	parametric_representation_context]]
	* 	[representation
	* 	representation.context_of_items ->
	* 	representation_context
	* 	representation_context.context_type
	* 	representation_context.context_type = `array parametric context']}
	* 	representation.items[i] ->
	* 	representation_item =>
	* 	{representation_item
	* 	representation_item.name = `terminus element positive x displacement'}
	* 	measure_representation_item <=
	* 	measure_with_unit
	* 	{measure_with_unit =>
	* 	length_measure_with_unit}
	* 	measure_with_unit.value_component
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTerminus_row_positive_y_displacement(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
		//unset old values
		unsetTerminus_row_positive_y_displacement(context, armEntity);

		if (armEntity.testTerminus_row_positive_y_displacement(null))
		{

			jsdai.SMeasure_schema.ELength_measure_with_unit armDisplacement = armEntity.getTerminus_row_positive_y_displacement(null);

			//EA armTerminus_node_positive_x_displacement.createAimData(context);
			if ( !(armDisplacement instanceof EMeasure_representation_item)) {
				armDisplacement = (CLength_measure_with_unit$measure_representation_item) 
					context.working_model.substituteInstance(armDisplacement, CLength_measure_with_unit$measure_representation_item.definition);
				((ERepresentation_item)(armDisplacement)).setName(null, "");				
				//EA armTerminus_node_positive_x_displacement.setAimInstance(aimDisplacement);
			}

			//property_definition
			EProperty_definition property_definition = CxAP210ARMUtilities.createProperty_definition(context, null, "rectangular array placement group properties", null, armEntity, true);

			//property_definition_representation
		   LangUtils.Attribute_and_value_structure[] pdrStructure =
													 {
														new LangUtils.Attribute_and_value_structure(CProperty_definition_representation.attributeDefinition(null), property_definition)
													};
		   EProperty_definition_representation property_definition_representation = (EProperty_definition_representation)
																LangUtils.createInstanceIfNeeded(
																						context,
																						CProperty_definition_representation.definition,
																						pdrStructure);

			//representation
			ERepresentation representation = null;
			if (property_definition_representation.testUsed_representation(null)) {
				representation = property_definition_representation.getUsed_representation(null);
			} else {
				representation = (ERepresentation) context.working_model.createEntityInstance(jsdai.SRepresentation_schema.ERepresentation.class);
				representation.setName(null, "rectangular array properties");
				property_definition_representation.setUsed_representation(null, representation);

			   //ERepresentation_context
			   if (!representation.testContext_of_items(null)) {
					LangUtils.Attribute_and_value_structure[] rcStructure =
														 {
															new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
															new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "array parametric context")
														};

					CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context representation_context = (CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context) LangUtils.createInstanceIfNeeded(
																						context,
																						CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context.definition,
																						rcStructure);
					representation.setContext_of_items(null, representation_context);

					//uncertainty_measure_with_unit
					if (!representation_context.testUnits(null)) {
						representation_context.createUnits(null);

						if (((EMeasure_representation_item) armDisplacement).testUnit_component(null)) {
						   representation_context.getUnits(null).addUnordered((armDisplacement).getUnit_component(null));
						}
					}

				}
			}

			if (!representation.testItems(null)) {
				representation.createItems(null);
			}
			((ERepresentation_item) armDisplacement).setName(null, "terminus row positive y displacement");
			representation.getItems(null).addUnordered(armDisplacement);

		}
	}


	/**
	* Unsets/deletes data for terminus_node_positive_x_displacement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTerminus_row_positive_y_displacement(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
	//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);
			if (property_definition.testName(null) && property_definition.getName(null).equals("rectangular array placement group properties")) {

				//property_definition_representation
				EProperty_definition_representation property_definition_representation = null;
				AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
				for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
					property_definition_representation = aProperty_definition_representation.getByIndex(j);
					//representation
					if (property_definition_representation.testUsed_representation(null)) {
						ERepresentation representation = property_definition_representation.getUsed_representation(null);
						if (representation.testName(null) && representation.getName(null).equals("rectangular array properties")) {
							if (representation.testItems(null)) {
								ARepresentation_item aItem = representation.getItems(null);
								ERepresentation_item item = null;

								int k = 1;
								while (k <= aItem.getMemberCount()) {
									item = aItem.getByIndex(k);
									if (item.testName(null) && item.getName(null).equals("terminus row positive y displacement")) {
										aItem.removeUnordered(item);
									} else {
										k++;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	* Sets/creates data for Initial_node_positive_x_displacement attribute.
	*
	* <p>
	*  attribute_mapping Initial_node_positive_x_displacement_length_data_element (Initial_node_positive_x_displacement
	* , (*PATH*), length_data_element);
	* 	linear_composite_array_shape_aspect &lt;=
	*  composite_array_shape_aspect &lt;= 
	*  composite_shape_aspect &lt;=
	*  shape_aspect
	*  shape_definition = shape_aspect
	*  characterized_definition = shape_definition
	*  characterized_definition &lt;-
	*  property_definition.definition
	*  property_definition
	*  {property_definition
	*  property_definition.name = 'linear array composite properties'}
	*  property_definition &lt;-
	*  property_definition_representation.definition
	*  property_definition_representation.used_representation -&gt;
	*  representation
	*  {[representation
	*  representation.name = 'linear array properties']
	*  [representation
	*  representation.context_of_items -&gt;
	*  representation_context
	*  [representation_context
	*  representation_context =&gt;
	*  global_unit_assigned_context]
	*  [representation_context
	*  representation_context =&gt;
	*  global_uncertainty_assigned_context]
	*  [representation_context
	*  representation_context =&gt;
	*  parametric_representation_context]]
	*  [representation
	*  representation.context_of_items -&gt;
	*  representation_context
	*  representation_context.context_type
	*  representation_context.context_type = 'array parametric context']}
	*  representation.items[i] -&gt;
	*  representation_item 
	*  {representation_item.name = 'initial element positive x displacement'}
	*  representation_item =&gt;
	*  measure_representation_item &lt;=
	*  measure_with_unit
	*  {measure_with_unit =&gt; 
	*  length_measure_with_unit}
	*  measure_with_unit.value_component
	* 
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setInitial_row_positive_y_displacement(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
		//unset old values
		unsetInitial_row_positive_y_displacement(context, armEntity);

		if (armEntity.testInitial_row_positive_y_displacement(null))
		{

			jsdai.SMeasure_schema.ELength_measure_with_unit armDisplacement = armEntity.getInitial_row_positive_y_displacement(null);

			//EA armTerminus_node_positive_x_displacement.createAimData(context);
			if ( !(armDisplacement instanceof EMeasure_representation_item)) {
				armDisplacement = (CLength_measure_with_unit$measure_representation_item) 
					context.working_model.substituteInstance(armDisplacement, CLength_measure_with_unit$measure_representation_item.definition);
				((ERepresentation_item)(armDisplacement)).setName(null, "");				
				//EA armTerminus_node_positive_x_displacement.setAimInstance(aimDisplacement);
			}

			//property_definition
			EProperty_definition property_definition = CxAP210ARMUtilities.createProperty_definition(context, null, "rectangular array placement group properties", null, armEntity, true);

			//property_definition_representation
		   LangUtils.Attribute_and_value_structure[] pdrStructure =
													 {
														new LangUtils.Attribute_and_value_structure(CProperty_definition_representation.attributeDefinition(null), property_definition)
													};
		   EProperty_definition_representation property_definition_representation = (EProperty_definition_representation)
																LangUtils.createInstanceIfNeeded(
																						context,
																						CProperty_definition_representation.definition,
																						pdrStructure);

			//representation
			ERepresentation representation = null;
			if (property_definition_representation.testUsed_representation(null)) {
				representation = property_definition_representation.getUsed_representation(null);
			} else {
				representation = (ERepresentation) context.working_model.createEntityInstance(jsdai.SRepresentation_schema.ERepresentation.class);
				representation.setName(null, "rectangular array properties");
				property_definition_representation.setUsed_representation(null, representation);

			   //ERepresentation_context
			   if (!representation.testContext_of_items(null)) {
					LangUtils.Attribute_and_value_structure[] rcStructure =
														 {
															new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
															new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "array parametric context")
														};

					CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context representation_context = (CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context) LangUtils.createInstanceIfNeeded(
																						context,
																						CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context.definition,
																						rcStructure);
					representation.setContext_of_items(null, representation_context);

					//uncertainty_measure_with_unit
					if (!representation_context.testUnits(null)) {
						representation_context.createUnits(null);

						if (((EMeasure_representation_item) armDisplacement).testUnit_component(null)) {
						   representation_context.getUnits(null).addUnordered((armDisplacement).getUnit_component(null));
						}
					}
// this is done via location_uncertainty now
/*					if (!representation_context.testUncertainty(null)) {
						representation_context.createUncertainty(null);

						EUncertainty_measure_with_unit uncertainty_measure_with_unit = (EUncertainty_measure_with_unit) context.working_model.createEntityInstance(EUncertainty_measure_with_unit.class);
						uncertainty_measure_with_unit.setName(null, "");
						String value = CxAP210ARMUtilities.getMeasureValue(armDisplacement);
						CxAP210ARMUtilities.setMeasureValue(uncertainty_measure_with_unit, value, ((EMeasure_representation_item) armDisplacement).testValue_component(null));

						if (((EMeasure_representation_item) armDisplacement).testUnit_component(null)) {
						   uncertainty_measure_with_unit.setUnit_component(null, ((EMeasure_representation_item) armDisplacement).getUnit_component(null));
						}

						representation_context.getUncertainty(null).addUnordered(uncertainty_measure_with_unit);
					}*/
				}
			}

			if (!representation.testItems(null)) {
				representation.createItems(null);
			}
			((ERepresentation_item) armDisplacement).setName(null, "initial row positive y displacement");
			representation.getItems(null).addUnordered(armDisplacement);

		}
	}


	/**
	* Unsets/deletes data for Initial_node_positive_x_displacement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetInitial_row_positive_y_displacement(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
	//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);
			if (property_definition.testName(null) && property_definition.getName(null).equals("rectangular array placement group properties")) {

				//property_definition_representation
				EProperty_definition_representation property_definition_representation = null;
				AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
				for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
					property_definition_representation = aProperty_definition_representation.getByIndex(j);
					//representation
					if (property_definition_representation.testUsed_representation(null)) {
						ERepresentation representation = property_definition_representation.getUsed_representation(null);
						if (representation.testName(null) && representation.getName(null).equals("rectangular array properties")) {
							if (representation.testItems(null)) {
								ARepresentation_item aItem = representation.getItems(null);
								ERepresentation_item item = null;

								int k = 1;
								while (k <= aItem.getMemberCount()) {
									item = aItem.getByIndex(k);
									if (item.testName(null) && item.getName(null).equals("initial row positive y displacement")) {
										aItem.removeUnordered(item);
									} else {
										k++;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	* Sets/creates data for Initial_node_positive_x_displacement attribute.
	*
	* <p>
	*  attribute_mapping Location_uncertainty (Location_uncertainty
	* , (*PATH*), length_data_element);
	* 	linear_composite_array_shape_aspect &lt;=
	*  composite_array_shape_aspect &lt;=
	*  composite_shape_aspect &lt;=
	*  shape_aspect
	*  shape_definition = shape_aspect
	*  characterized_definition = shape_definition
	*  characterized_definition &lt;-
	*  property_definition.definition
	*  property_definition
	*  {property_definition
	*  property_definition.name = 'linear array composite properties'}
	*  property_definition &lt;-
	*  property_definition_representation.definition
	*  property_definition_representation.used_representation -&gt;
	*  representation
	*  {representation
	*  representation.name = 'linear array properties'}
	*  representation.context_of_items -&gt;
	*  representation_context
	*  representation_context =&gt;
	*  global_uncertainty_assigned_context
	*  global_uncertainty_assigned_context.uncertainty[i] -&gt;
	*  uncertainty_measure_with_unit &lt;=
	*  measure_with_unit =&gt;
	*  length_measure_with_unit	 
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocation_uncertainty(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
		//unset old values
		unsetLocation_uncertainty(context, armEntity);

		if (armEntity.testLocation_uncertainty(null))
		{

			jsdai.SMeasure_schema.ELength_measure_with_unit armDisplacement = armEntity.getLocation_uncertainty(null);

			//EA armTerminus_node_positive_x_displacement.createAimData(context);
			if ( !(armDisplacement instanceof EMeasure_representation_item)) {
				armDisplacement = (CLength_measure_with_unit$measure_representation_item) 
					context.working_model.substituteInstance(armDisplacement, CLength_measure_with_unit$measure_representation_item.definition);
				((ERepresentation_item)(armDisplacement)).setName(null, "");				
				//EA armTerminus_node_positive_x_displacement.setAimInstance(aimDisplacement);
			}

			//property_definition
			EProperty_definition property_definition = CxAP210ARMUtilities.createProperty_definition(context, null, "rectangular array placement group properties", null, armEntity, true);

			//property_definition_representation
		   LangUtils.Attribute_and_value_structure[] pdrStructure =
													 {
														new LangUtils.Attribute_and_value_structure(CProperty_definition_representation.attributeDefinition(null), property_definition)
													};
		   EProperty_definition_representation property_definition_representation = (EProperty_definition_representation)
																LangUtils.createInstanceIfNeeded(
																						context,
																						CProperty_definition_representation.definition,
																						pdrStructure);

			//representation
			ERepresentation representation = null;
			if (property_definition_representation.testUsed_representation(null)) {
				representation = property_definition_representation.getUsed_representation(null);
			} else {
				representation = (ERepresentation) context.working_model.createEntityInstance(jsdai.SRepresentation_schema.ERepresentation.class);
				representation.setName(null, "rectangular array properties");
				property_definition_representation.setUsed_representation(null, representation);

			   //ERepresentation_context
			   if (!representation.testContext_of_items(null)) {
					LangUtils.Attribute_and_value_structure[] rcStructure =
														 {
															new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
															new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "array parametric context")
														};

					CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context representation_context = (CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context) LangUtils.createInstanceIfNeeded(
																						context,
																						CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context.definition,
																						rcStructure);
					representation.setContext_of_items(null, representation_context);

					//uncertainty_measure_with_unit
					if (!representation_context.testUnits(null)) {
						representation_context.createUnits(null);

						if (((EMeasure_representation_item) armDisplacement).testUnit_component(null)) {
						   representation_context.getUnits(null).addUnordered((armDisplacement).getUnit_component(null));
						}
					}

					if (!representation_context.testUncertainty(null)) 
						representation_context.createUncertainty(null);
					representation_context.getUncertainty(null).addUnordered(armDisplacement);
				}
			}

		}
	}


	/**
	* Unsets/deletes data for Location_uncertainty attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation_uncertainty(SdaiContext context, EPart_rectangular_array_feature armEntity) throws SdaiException
	{
	//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);
			if (property_definition.testName(null) && property_definition.getName(null).equals("rectangular array placement group properties")) {

				//property_definition_representation
				EProperty_definition_representation property_definition_representation = null;
				AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
				for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
					property_definition_representation = aProperty_definition_representation.getByIndex(j);
					//representation
					if (property_definition_representation.testUsed_representation(null)) {
						ERepresentation representation = property_definition_representation.getUsed_representation(null);
						if (representation.testName(null) && representation.getName(null).equals("rectangular array properties")) {
							ERepresentation_context rc = representation.getContext_of_items(null);
							if(rc instanceof EGlobal_uncertainty_assigned_context){
								EGlobal_uncertainty_assigned_context eguac = (EGlobal_uncertainty_assigned_context)rc;
								eguac.unsetUncertainty(null);
							}
						}
					}
				}
			}
		}
	}
	

}