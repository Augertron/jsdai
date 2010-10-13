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

package jsdai.SComponent_grouping_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SComponent_grouping_mim.*;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SMeasure_schema.*;
import jsdai.SMixed_complex_types.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxRectangular_array_placement_group_component_armx
		extends
			CRectangular_array_placement_group_component_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from PDR
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a10 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a10 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a10$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a11$;
	}
	
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a13$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a13);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a13);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a13 = set_instanceX(a13, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a13 = unset_instance(a13);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a13$;
	}
	// ENDOF taken from PDR

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

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_formation)a2;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a2$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a3);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = get_instance(a3);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a3;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a3 = set_instanceX(a3, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a3$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a7);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a7);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a7 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a7 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a7$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a9);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a9 = get_instance_select(a9);
		return (EEntity)a9;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a9 = set_instanceX(a9, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a9$;
	}
	
	// ENDOF From CProperty_definition.java
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CRectangular_array_placement_group_component.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
//			setId_x(context, this);
			
			//id - goes directly into AIM
			
			//additional_characterization
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setDerived_from(context, this);
			
	      // element
			setElement(context, this);
	      
			// initial_row_positive_y_displacement
			setInitial_row_positive_y_displacement(context, this);
	      
			// terminus_row_positive_y_displacement
			setTerminus_row_positive_y_displacement(context, this);
			
	      // location_uncertainty
			setLocation_uncertainty(context, this);

			// Clean ARM specific attributes
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
//			unsetId_x(null);

			unsetElement(null);
			unsetInitial_row_positive_y_displacement(null);
			unsetTerminus_row_positive_y_displacement(null);
			unsetLocation_uncertainty(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// Kind of AIM gap
			// unsetDefinition(null);

			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization
			// unsetAdditional_characterization(context, this);
			
			unsetDerived_from(context, this);
			
//			unsetId_x(context, this);			

	      // element
			unsetElement(context, this);
	      
			// Initial_row_positive_y_displacement
			unsetInitial_row_positive_y_displacement(context, this);
	      
			// terminus_row_positive_y_displacement
			unsetTerminus_row_positive_y_displacement(context, this);
			
	      // location_uncertainty
			unsetLocation_uncertainty(context, this);
		
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  rectangular_array_placement_group_component_definition &lt;=
	 *  array_placement_group_component_definition &lt;=
	 *  assembly_group_component_definition &lt;=
	 *  assembly_component &lt;=
	 *  component_definition &lt;=
	 *  product_definition
	 *  {product_definition &lt;-
	 *  product_definition_relationship.relating_product_definition
	 *  {product_definition_relationship 
	 *  product_definition_relationship.name = 'group component'}
	 *  product_definition_relationship
	 *  product_definition_relationship.related_product_definition -&gt;
	 *  product_definition =&gt; 
	 *  component_definition =&gt;
	 *  assembly_component}
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
			ERectangular_array_placement_group_component_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxAssembly_group_component_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ERectangular_array_placement_group_component_armx armEntity) throws SdaiException {
		CxAssembly_group_component_armx.unsetMappingConstraints(context, armEntity);
	}

	//********** "design_discipline_item_definition" attributes

	/**
	 * Sets/creates data for additional_context attribute.
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

	/**
	 * Sets/creates data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);		
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
	public static void unsetAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);		
	}

// Product_occurrence attributes	
	
	/**
	 * Sets/creates data for derived_from attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PDU -> PD
	public static void setDerived_from(SdaiContext context,
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		CxDefinition_based_product_occurrence.setDerived_from(context, armEntity);
	}

	/**
	 * Unsets/deletes data for Quantity_criterion attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetDerived_from(SdaiContext context,
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		CxDefinition_based_product_occurrence.unsetDerived_from(context, armEntity);		
	}
	
///// Item_shape
	/**
	 * Sets/creates data for id_x attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		CxItem_shape.setId_x(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes data for Quantity_criterion attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);		
	}
*/
	//********** "Rectangular_array_placement_group_component" attributes

	/**
	* Sets/creates data for element attribute.
	*
	* <p>
	*  attribute_mapping element_assembly_component_placement_link (element
	* , (*PATH*), assembly_component_placement_link);
	* 	rectangular_array_placement_group_component_definition &lt;=
	*  array_placement_group_component_definition &lt;=
	*  assembly_group_component_definition &lt;=
	*  assembly_component &lt;=
	*  component_definition &lt;=
	*  product_definition &lt;-
	*  product_definition_relationship.relating_product_definition
	*  {product_definition_relationship
	*  product_definition_relationship.name = 'element'}
	*  product_definition_relationship.related_product_definition -&gt;
	*  product_definition =&gt;
	*  linear_array_component_definition_link
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setElement(SdaiContext context, ERectangular_array_placement_group_component_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetElement(context, armEntity);

		if (armEntity.testElement(null))
		{

			ALinear_array_placement_group_component_link aArmElement = armEntity.getElement(null);

			ELinear_array_placement_group_component_link armElement = null;
			for (int i = 1; i <= aArmElement.getMemberCount(); i++) {
				armElement = aArmElement.getByIndex(i);

				jsdai.SProduct_definition_schema.EProduct_definition_relationship product_definition_relationship =
					(jsdai.SProduct_definition_schema.EProduct_definition_relationship) context.working_model.
                  createEntityInstance(jsdai.SProduct_definition_schema.CProduct_definition_relationship.definition);
            product_definition_relationship.setName(null, "element");
            product_definition_relationship.setRelated_product_definition(null, armElement);
            product_definition_relationship.setRelating_product_definition(null, armEntity);
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
	public static void unsetElement(SdaiContext context, ERectangular_array_placement_group_component armEntity) throws SdaiException
	{
     AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
     CProduct_definition_relationship.usedinRelating_product_definition(null, armEntity, context.domain, aRelationship);
     EProduct_definition_relationship relationship = null;

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
	*  rectangular_array_placement_group_component_definition &lt;=
	*  array_placement_group_component_definition &lt;= 
	*  assembly_group_component_definition &lt;=
	*  assembly_component &lt;=
	*  component_definition &lt;=
	*  product_definition
	*  characterized_product_definition = product_definition
	*  characterized_product_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_definition &lt;-
	*  property_definition.definition
	*  property_definition &lt;-
	*  {property_definition
	*  property_definition.name = 'rectangular array placement group properties'}
	*  property_definition_representation.definition
	*  property_definition_representation.used_representation -&gt;
	*  representation
	*  {[representation
	*  representation.name = 'rectangular array properties']
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
	*  {representation_item.name = 'terminus row positive y displacement'}
	*  representation_item =&gt;
	*  measure_representation_item &lt;=
	*  measure_with_unit
	*  {measure_with_unit =&gt;
	*  length_measure_with_unit}
	*  measure_with_unit.value_component
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTerminus_row_positive_y_displacement(SdaiContext context, ERectangular_array_placement_group_component_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetTerminus_row_positive_y_displacement(context, armEntity);

		if (armEntity.testTerminus_row_positive_y_displacement(null))
		{

			ELength_measure_with_unit armTerminus_row_positive_y_displacement = armEntity.getTerminus_row_positive_y_displacement(null);
			//EA armTerminus_node_positive_x_displacement.createAimData(context);
			ERepresentation_item aimDisplacement;
			if ( !(armTerminus_row_positive_y_displacement instanceof EMeasure_representation_item)) {
				aimDisplacement = (ERepresentation_item)context.working_model.substituteInstance(armTerminus_row_positive_y_displacement, CLength_measure_with_unit$measure_representation_item.definition);
				aimDisplacement.setName(null, "");				
				//EA armTerminus_node_positive_x_displacement.setAimInstance(aimDisplacement);
			}
			else
				aimDisplacement = (ERepresentation_item)armTerminus_row_positive_y_displacement;				

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
/*					if (!representation_context.testUnits(null)) {
						representation_context.createUnits(null);

						if (((EMeasure_representation_item) aimDisplacement).testUnit_component(null)) {
						   representation_context.getUnits(null).addUnordered(((EMeasure_representation_item) aimDisplacement).getUnit_component(null));
						}
					}

					if (!representation_context.testUncertainty(null)) {
						representation_context.createUncertainty(null);

						EUncertainty_measure_with_unit uncertainty_measure_with_unit = (EUncertainty_measure_with_unit) context.working_model.createEntityInstance(EUncertainty_measure_with_unit.class);
						uncertainty_measure_with_unit.setName(null, "");
						String value = CxAP210ARMUtilities.getMeasureValue((EMeasure_representation_item) aimDisplacement);
						CxAP210ARMUtilities.setMeasureValue(uncertainty_measure_with_unit, value, ((EMeasure_representation_item) aimDisplacement).testValue_component(null));

						if (((EMeasure_representation_item) aimDisplacement).testUnit_component(null)) {
						   uncertainty_measure_with_unit.setUnit_component(null, ((EMeasure_representation_item) aimDisplacement).getUnit_component(null));
						}

						representation_context.getUncertainty(null).addUnordered(uncertainty_measure_with_unit);
					}*/
				}
			}

			if (!representation.testItems(null)) {
				representation.createItems(null);
			}
			aimDisplacement.setName(null, "terminus row positive y displacement");
			representation.getItems(null).addUnordered(aimDisplacement);
		}
	}


	/**
	* Unsets/deletes data for terminus_node_positive_x_displacement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTerminus_row_positive_y_displacement(SdaiContext context, ERectangular_array_placement_group_component armEntity) throws SdaiException
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

/////////////////////////
// New since WDxxx
   /**
   * Sets/creates data for Initial_row_positive_y_displacement attribute.
   *
   * <p>
   *  attribute_mapping terminus_node_positive_x_displacement_length_data_element (Initial_node_positive_x_displacement
   * , (*PATH*), length_data_element);
   * 	rectangular_array_placement_group_component_definition &lt;=
   *  array_placement_group_component_definition &lt;=
   *  assembly_group_component_definition &lt;=
   *  assembly_component &lt;=
   *  component_definition &lt;=
   *  product_definition
   *  characterized_product_definition = product_definition
   *  characterized_product_definition
   *  characterized_definition = characterized_product_definition
   *  characterized_definition &lt;-
   *  property_definition.definition
   *  property_definition &lt;-
   *  {property_definition
   *  property_definition.name = 'rectangular array placement group properties'}
   *  property_definition_representation.definition
   *  property_definition_representation.used_representation -&gt;
   *  representation
   *  {[representation
   *  representation.name = 'rectangular array properties']
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
   *  {representation_item.name = 'initial row positive y displacement'}
   *  representation_item =&gt;
   *  measure_representation_item &lt;= 
   *  measure_with_unit
   *  {measure_with_unit =&gt;
   *  length_measure_with_unit}
   *  measure_with_unit.value_component
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   public static void setInitial_row_positive_y_displacement(SdaiContext context, ERectangular_array_placement_group_component_armx armEntity) throws SdaiException
   {
      //unset old values
      unsetInitial_row_positive_y_displacement(context, armEntity);

      if (armEntity.testInitial_row_positive_y_displacement(null))
      {

         jsdai.SMeasure_schema.ELength_measure_with_unit armInitial_node_positive_x_displacement = armEntity.getInitial_row_positive_y_displacement(null);
		//EA armInitial_node_positive_x_displacement.createAimData(context);
         ERepresentation_item aimDisplacement;
         if ( !(armInitial_node_positive_x_displacement instanceof EMeasure_representation_item)) {
            aimDisplacement = (ERepresentation_item)context.working_model.substituteInstance(armInitial_node_positive_x_displacement, CLength_measure_with_unit$measure_representation_item.definition);
            aimDisplacement.setName(null, "");            
			//EA armInitial_node_positive_x_displacement.setAimInstance(aimDisplacement);
         }
         else
         	aimDisplacement = (ERepresentation_item)armInitial_node_positive_x_displacement;

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
/*               if (!representation_context.testUnits(null)) {
                  representation_context.createUnits(null);

                  if (((EMeasure_representation_item) aimDisplacement).testUnit_component(null)) {
                     representation_context.getUnits(null).addUnordered(((EMeasure_representation_item) aimDisplacement).getUnit_component(null));
                  }
               }

               if (!representation_context.testUncertainty(null)) {
                  representation_context.createUncertainty(null);

                  EUncertainty_measure_with_unit uncertainty_measure_with_unit = (EUncertainty_measure_with_unit) context.working_model.createEntityInstance(EUncertainty_measure_with_unit.class);
                  uncertainty_measure_with_unit.setName(null, "");
                  String value = CxAP210ARMUtilities.getMeasureValue((EMeasure_representation_item) aimDisplacement);
                  CxAP210ARMUtilities.setMeasureValue(uncertainty_measure_with_unit, value, ((EMeasure_representation_item) aimDisplacement).testValue_component(null));

                  if (((EMeasure_representation_item) aimDisplacement).testUnit_component(null)) {
                     uncertainty_measure_with_unit.setUnit_component(null, ((EMeasure_representation_item) aimDisplacement).getUnit_component(null));
                  }
                  representation_context.getUncertainty(null).addUnordered(uncertainty_measure_with_unit);
               }*/
            }
         }

         if (!representation.testItems(null)) {
            representation.createItems(null);
         }
         aimDisplacement.setName(null, "initial row positive y displacement");
         representation.getItems(null).addUnordered(aimDisplacement);
      }
   }


   /**
   * Unsets/deletes data for Initial_row_positive_y_displacement attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   public static void unsetInitial_row_positive_y_displacement(SdaiContext context, ERectangular_array_placement_group_component armEntity) throws SdaiException
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

/////////////////////////
// New since Modularization
   /**
   * Sets/creates data for location_uncertainty attribute.
   *
   * <p>
   *  attribute_mapping location_uncertainty (location_uncertainty
   * , (*PATH*), length_data_element);
   * 	rectangular_array_placement_group_component_definition &lt;=
   *  array_placement_group_component_definition &lt;=
   *  assembly_group_component_definition &lt;=
   *  assembly_component &lt;=
   *  component_definition &lt;=
   *  product_definition
   *  characterized_product_definition = product_definition
   *  characterized_product_definition
   *  characterized_definition = characterized_product_definition
   *  characterized_definition &lt;-
   *  property_definition.definition
   *  property_definition &lt;-
   *  {property_definition
   *  property_definition.name = 'rectangular array placement group properties'}
   *  property_definition_representation.definition
   *  property_definition_representation.used_representation -&gt;
   *  representation
   *  {representation
   *  representation.name = 'rectangular array properties'}
   *  representation.context_of_items -&gt;
   *  representation_context
   *  representation_context =&gt;
   *  global_uncertainty_assigned_context
   *  global_uncertainty_assigned_context.uncertainty[i] -&gt;
   *  uncertainty_measure_with_unit &lt;=
   *  measure_with_unit =&gt;
   *  length_measure_with_unit
   * end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   // LAPGCD <- PD <- PDR -> R -> GUAC -> LMWU
   public static void setLocation_uncertainty(SdaiContext context, ERectangular_array_placement_group_component_armx armEntity) throws SdaiException
   {
      //unset old values
      unsetLocation_uncertainty(context, armEntity);

      if (armEntity.testLocation_uncertainty(null))
      {
         jsdai.SMeasure_schema.ELength_measure_with_unit armUncertainty = armEntity.getLocation_uncertainty(null);
   		//EA armInitial_node_positive_x_displacement.createAimData(context);
            ERepresentation_item aimDisplacement;
            if ( !(armUncertainty instanceof EMeasure_representation_item)) {
               aimDisplacement = (ERepresentation_item)context.working_model.substituteInstance(armUncertainty, CLength_measure_with_unit$measure_representation_item.definition);
               aimDisplacement.setName(null, "");               
   			//EA armInitial_node_positive_x_displacement.setAimInstance(aimDisplacement);
            }
            else
            	aimDisplacement = (ERepresentation_item)armUncertainty;

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
//                                                new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "array parametric context")
                                             };

                  CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context representation_context = (CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context) LangUtils.createInstanceIfNeeded(
                                                                     context,
                                                                     CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context.definition,
                                                                     rcStructure);
                  representation.setContext_of_items(null, representation_context);

                  //uncertainty_measure_with_unit
                  if (!representation_context.testUnits(null)) {
                     representation_context.createUnits(null);

                     if (((EMeasure_representation_item) aimDisplacement).testUnit_component(null)) {
                        representation_context.getUnits(null).addUnordered(((EMeasure_representation_item) aimDisplacement).getUnit_component(null));
                     }
                  }

                  if (!representation_context.testUncertainty(null)) {
                     representation_context.createUncertainty(null);

                     EUncertainty_measure_with_unit uncertainty_measure_with_unit = (EUncertainty_measure_with_unit) context.working_model.createEntityInstance(EUncertainty_measure_with_unit.class);
                     uncertainty_measure_with_unit.setName(null, "");
                     String value = CxAP210ARMUtilities.getMeasureValue((EMeasure_representation_item) aimDisplacement);
                     CxAP210ARMUtilities.setMeasureValue(uncertainty_measure_with_unit, value, ((EMeasure_representation_item) aimDisplacement).testValue_component(null));

                     if (((EMeasure_representation_item) aimDisplacement).testUnit_component(null)) {
                        uncertainty_measure_with_unit.setUnit_component(null, ((EMeasure_representation_item) aimDisplacement).getUnit_component(null));
                     }

                     representation_context.getUncertainty(null).addUnordered(uncertainty_measure_with_unit);
                  }
               }
            }

            if (!representation.testItems(null)) {
               representation.createItems(null);
            }
            representation.getItems(null).addUnordered(aimDisplacement);

      }
   }


   /**
   * Unsets/deletes data for Location_uncertainty attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   public static void unsetLocation_uncertainty(SdaiContext context, ERectangular_array_placement_group_component armEntity) throws SdaiException
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
                  	ERepresentation_context repContext = representation.getContext_of_items(null);
                  	if(repContext instanceof EGlobal_uncertainty_assigned_context){
                  		EGlobal_uncertainty_assigned_context uncertaintyContext = (EGlobal_uncertainty_assigned_context)repContext;
                  		uncertaintyContext.unsetUncertainty(null);
                  	}
                  }
               }
            }
         }
      }
   }


	
}