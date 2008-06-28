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

package jsdai.SLayered_interconnect_module_with_printed_component_design_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SLayered_interconnect_module_with_printed_component_design_mim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxMulti_stratum_printed_component_armx$printed_connector_component_armx
		extends
			CMulti_stratum_printed_component_armx$printed_connector_component_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from PDR
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a9);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a9);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a9 = set_instance(a9, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a9 = unset_instance(a9);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a9$;
	}
	// ENDOF taken from PDR

	// Product_view_definition
	
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a3);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a3$;
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
		a4 = get_instance(a4);
		return (EProduct_definition_formation)a4;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a4 = set_instance(a4, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a4$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a5$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a5);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a5 = get_instance(a5);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a5;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a5 = set_instance(a5, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a5 = unset_instance(a5);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a5$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a13);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a13);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a13 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a13 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a13$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a15$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a15);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a15 = get_instance_select(a15);
		return (EEntity)a15;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a15 = set_instance(a15, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a15 = unset_instance(a15);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a15$;
	}
	
	// ENDOF From CProperty_definition.java
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMulti_stratum_printed_component$printed_connector_component.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
			setId_x(context, this);
			
			//id - goes directly into AIM
			
			//additional_characterization
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setDerived_from(context, this);
			
			// stratum_feature_implementation
			// setStratum_feature_implementation(context, this);
			
			// footprint_implementation
			// setFootprint_implementation(context, this);
			
			
			// Clean ARM specific attributes
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);

			unsetDerived_from(null);
			unsetId_x(null);

			// unsetStratum_feature_implementation(null);
			// unsetPrinted_component_stack(null);
			// unsetRequired_material_stack(null);
			// unsetFootprint_implementation(null);
			
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
			
			unsetId_x(context, this);			

			// stratum_feature_implementation
			// unsetStratum_feature_implementation(context, this);
			
			// footprint_implementation
			// unsetFootprint_implementation(context, this);

	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
	 *  printed_component &lt;=
	 *  assembly_component &lt;=
	 *  component_definition &lt;=
	 *  product_definition
	 *  {product_definition
	 *  [product_definition.id = 'printed connector component']
	 *  [product_definition.description = 'printed component']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'layout occurrence']} 
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
			CMulti_stratum_printed_component_armx$printed_connector_component_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxMulti_stratum_printed_component_armx.setMappingConstraints(context, armEntity);
		CxPrinted_connector_component_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CMulti_stratum_printed_component_armx$printed_connector_component_armx armEntity) throws SdaiException {
		CxMulti_stratum_printed_component_armx.unsetMappingConstraints(context, armEntity);
		CxPrinted_connector_component_armx.unsetMappingConstraints(context, armEntity);
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
	public static void setId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.setId_x(context, armEntity);
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
	public static void unsetId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);		
	}
	
	//********** "printed_component" attributes

	/**
	* Sets/creates data for stratum_feature_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* removed	
	public static void setStratum_feature_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.setStratum_feature_implementation(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for stratum_feature_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Removed	
	public static void unsetStratum_feature_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.unsetStratum_feature_implementation(context, armEntity);		
	}
*/

	/**
	* Sets/creates data for printed_component_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Moved to another subtype	
	public static void setPrinted_component_stack(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.setPrinted_component_stack(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for printed_component_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Moved to another subtype	
	public static void unsetPrinted_component_stack(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.unsetPrinted_component_stack(context, armEntity);		
	}
*/

	/**
	* Sets/creates data for required_material_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Moved to another subtype	
	public static void setRequired_material_stack(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.setRequired_material_stack(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for required_material_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Moved to another subtype	
	public static void unsetRequired_material_stack(SdaiContext context, EPrinted_component armEntity) throws SdaiException
	{
		CxPrinted_component_armx.unsetRequired_material_stack(context, armEntity);		
	}
*/
	/**
	* Sets/creates data for footprint_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed
	public static void setFootprint_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.setFootprint_implementation(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for footprint_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Removed	
	public static void unsetFootprint_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.unsetFootprint_implementation(context, armEntity);		
	}
*/

}