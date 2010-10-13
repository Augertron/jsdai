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

package jsdai.SIntegral_shield_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SAssembly_module_design_xim.*;
import jsdai.SComponent_feature_xim.EComponent_feature_armx;
import jsdai.SLayered_interconnect_module_design_xim.*;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SPhysical_connectivity_definition_xim.EPhysical_connectivity_definition_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SIntegral_shield_mim.CRouted_shield;

public class CxRouted_shield_armx
		extends
			CRouted_shield_armx implements EMappedXIMEntity{

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

		setTemp("AIM", CRouted_shield.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
//			setId_x(context, this);
			
			//id - goes directly into AIM
			
			//additional_characterization
			setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setDerived_from(context, this);
			
			// stratum_feature_implementation
			// setStratum_feature_implementation(context, this);
			
			// footprint_implementation
			// setFootprint_implementation(context, this);
			
			// routed_centreline_shape
			setRouted_centreline_shape(context, this);
			
         // component_extent 			
			setComponent_extent(context, this);
			
			// setShielded_item
			setShielded_item(context, this);
			
			// Clean ARM specific attributes
			unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			unsetDerived_from(null);
//			unsetId_x(null);
			// unsetStratum_feature_implementation(null);
			// unsetFootprint_implementation(null);
			unsetRouted_centreline_shape(null);
			unsetComponent_extent(null);

			unsetShielded_item(null);
			
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
			unsetAdditional_characterization(context, this);
			
			unsetDerived_from(context, this);
			
//			unsetId_x(context, this);			

			// stratum_feature_implementation
			// unsetStratum_feature_implementation(context, this);
			
			// footprint_implementation
			// unsetFootprint_implementation(context, this);

			// routed_centreline_shape
			unsetRouted_centreline_shape(context, this);
			
         // component_extent 			
			unsetComponent_extent(context, this);

			// setShielded_item
			unsetShielded_item(context, this);
			
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  routed_shield &lt;=
	 *  routed_printed_component &lt;=
	 *  printed_component &lt;=				
	 *  assembly_component &lt;=
	 *  component_definition &lt;=
	 *  product_definition
	 *  {product_definition
	 *  product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'physical occurrence'}
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
			ERouted_shield_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxIntegral_shield_armx.setMappingConstraints(context, armEntity);
		// CxAP210ARMUtilities.assignPart_definition_type(context, armEntity, "physical occurrence");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ERouted_shield_armx armEntity) throws SdaiException {
		CxIntegral_shield_armx.unsetMappingConstraints(context, armEntity);
		// CxAP210ARMUtilities.deassignPart_definition_type(context, armEntity);
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
/* removed	
	public static void unsetStratum_feature_implementation(SdaiContext context, EPrinted_component_armx armEntity) throws SdaiException
	{
		CxPrinted_component_armx.unsetStratum_feature_implementation(context, armEntity);		
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
	//********** "routed_printed_component" attributes

	/**
	* Sets/creates data for routed_centreline_shape attribute.
	*
	* <p>
	*  attribute_mapping routed_centreline_shape_curve (routed_centreline_shape
	* , (*PATH*), curve);
	* 	routed_printed_component <=
	* 	printed_component <=
	* 	assembly_component <=
	* 	product_definition_shape <=
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation.items[i] ->
	* 	representation_item =>
	* 	geometric_representation_item =>
	* 	curve
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRouted_centreline_shape(SdaiContext context, ERouted_shield_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetRouted_centreline_shape(context, armEntity);

		if (armEntity.testRouted_centreline_shape(null))
		{

			jsdai.SGeometry_schema.ECurve armRouted_centreline_shape = armEntity.getRouted_centreline_shape(null);
			//EA armRouted_centreline_shape.createAimData(context);

			//property_definition
			EProperty_definition property_definition = CxAP210ARMUtilities.createProperty_definition(context, null, "", null, armEntity, true);

			//property_definition_representation
			LangUtils.Attribute_and_value_structure[] pdrStructure =
				{
					new LangUtils.Attribute_and_value_structure(CProperty_definition_representation.attributeDefinition(null), property_definition),
				};

			EProperty_definition_representation property_definition_representation = (EProperty_definition_representation) LangUtils.createInstanceIfNeeded(
																					context,
																					CProperty_definition_representation.definition,
																					pdrStructure);

			//representation
			ERepresentation representation = null;
			if (property_definition_representation.testUsed_representation(null)) {
				representation = property_definition_representation.getUsed_representation(null);
			} else {
				representation = CxAP210ARMUtilities.createRepresentation(context, "", false);
				property_definition_representation.setUsed_representation(null, representation);
			}
			if (!representation.testItems(null)) {
				representation.createItems(null);
			}
			representation.getItems(null).addUnordered(armRouted_centreline_shape);
		}
	}


	/**
	* Unsets/deletes data for routed_centreline_shape attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRouted_centreline_shape(SdaiContext context, ERouted_shield_armx armEntity) throws SdaiException
	{
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);

		//property_definition
		for (int k = 1; k <= aProperty_definition.getMemberCount(); k++) {
			property_definition = aProperty_definition.getByIndex(k);

			EProperty_definition_representation property_definition_representation = null;
			AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
			//property_definition_representation
			for (int l = 1; l <= aProperty_definition_representation.getMemberCount(); l++) {
				property_definition_representation = aProperty_definition_representation.getByIndex(l);
				if (property_definition_representation.testUsed_representation(null)) {
					ERepresentation representation = property_definition_representation.getUsed_representation(null);
					if (representation.testItems(null)) {
						ARepresentation_item aItem = representation.getItems(null);
						ERepresentation_item representation_item = null;
						int it = 1;
						while (it <= aItem.getMemberCount()) {
							representation_item = aItem.getByIndex(it);
							if (representation_item instanceof jsdai.SGeometry_schema.ECurve) {
								aItem.removeUnordered(representation_item);
							} else {
								it++;
							}
						}
					}
				}
			}

		}

	}


	/**
	* Sets/creates data for component_extent attribute.
	*
	* <p>
	*  attribute_mapping component_extent_inter_stratum_extent (component_extent
	* , (*PATH*), inter_stratum_extent);
	*  routed_printed_component &lt;=
	*  printed_component &lt;=
	*  assembly_component &lt;=
	*  product_definition_shape &lt;=
	*  property_definition &lt;-
	*  property_definition_relationship.related_property_definition
	*  {property_definition_relationship
	*  property_definition_relationship.name = 'component extent'}
	*  property_definition_relationship.relating_property_definition -&gt;
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition
	*  characterized_product_definition = product_definition_relationship\
	*  {product_definition_relationship
	*  product_definition_relationship.name = 'inter stratum extent'}
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setComponent_extent(SdaiContext context, ERouted_shield_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetComponent_extent(context, armEntity);

		if (armEntity.testComponent_extent(null))
		{

			EInter_stratum_extent armComponent_extent = armEntity.getComponent_extent(null);
			// Property_definition_relationship
			EProperty_definition_relationship relationship = (EProperty_definition_relationship) 
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			relationship.setName(null, "component extent");
			relationship.setRelated_property_definition(null, armEntity);
			

			// Property_definition
	      LangUtils.Attribute_and_value_structure[] pdStructure = {
	            new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), armComponent_extent)
	         };
	         EProperty_definition epd = (EProperty_definition)
	            LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
			if(!epd.testName(null))
				epd.setName(null, "");
	      
			relationship.setRelating_property_definition(null, armEntity);
			armComponent_extent.setName(null, "inter stratum extent");
		}
	}


	/**
	* Unsets/deletes data for component_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetComponent_extent(SdaiContext context, ERouted_shield_armx armEntity) throws SdaiException
	{
		EProperty_definition_relationship epdr = null;
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);

		//property_definition
		for (int k = 1; k <= apdr.getMemberCount(); k++) {
			epdr = apdr.getByIndex(k);

			if (epdr.testName(null) && epdr.getName(null).equals("component extent")){
				epdr.deleteApplicationInstance();
			}
		}
	}
	
// COPY taken from Integral_shield
	//********** "shield" attributes

	/**
	* Sets/creates data for shielded_item attribute.
	*
	*
	*  attribute_mapping shielded_item_component_feature (shielded_item
	* , (*PATH*), component_feature);
            <aa attribute="shielded_item" assertion_to="Physical_connectivity_definition_external_reference">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
product_definition_shape &lt;=
property_definition &lt;-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation -&gt;
representation
{representation representation.name = 'physical connectivity definition external reference'}
</refpath>
            </aa> 
            <aa attribute="shielded_item" assertion_to="Component_group_external_reference">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
product_definition_shape &lt;=
property_definition &lt;-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation -&gt;
representation
{representation representation.name = 'component group external reference'}
</refpath>
            </aa>  
            <aa attribute="shielded_item" assertion_to="Component_feature_external_reference">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
product_definition_shape &lt;=
property_definition &lt;-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation -&gt;
representation
representation.items[i]-&gt;
representation_item
{representation_item
representation_item.name = 'component feature external reference'}			
representation_item =&gt;
descriptive_representation_item
</refpath>
            </aa>
            <aa attribute="shielded_item" assertion_to="Component_external_reference">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
product_definition_shape &lt;=
property_definition &lt;-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation -&gt;
representation
{representation representation.name = 'component external reference'}
</refpath>
            </aa> 
            <aa attribute="shielded_item" assertion_to="Component_feature">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
product_definition_shape &lt;-
shape_aspect.of_shape
shape_aspect &lt;-
shape_aspect_relationship.related_shape_aspect
{shape_aspect_relationship
shape_aspect_relationship.name = 'shielded item'}
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect -&gt;
shape_aspect =&gt;
component_feature
</refpath>
            </aa>
            <aa attribute="shielded_item" assertion_to="Stratum_feature_template_component">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
component_definition &lt;=
product_definition
product_definition &lt;-
product_definition_relationship.related_product_definition
{product_definition_relationship
product_definition_relationship.name = 'shielded item'}
product_definition_relationship
product_definition_relationship.relating_product_definition -&gt;
product_definition =&gt;
component_definition =&gt;
assembly_component
</refpath>
            </aa>  
            <aa attribute="shielded_item" assertion_to="Generic_physical_network">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
product_definition_shape &lt;-
shape_aspect.of_shape
shape_aspect &lt;-
shape_aspect_relationship.related_shape_aspect
{shape_aspect_relationship
shape_aspect_relationship.name = 'shielded item'}
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect -&gt;
shape_aspect =&gt;
physical_network
</refpath>
            </aa>
            <aa attribute="shielded_item" assertion_to="Physical_connectivity_definition">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">integral_shield &lt;=
assembly_component &lt;=	
product_definition_shape &lt;-
shape_aspect.of_shape
shape_aspect &lt;-
shape_aspect_relationship.related_shape_aspect
{shape_aspect_relationship
shape_aspect_relationship.name = 'shielded item'}
shape_aspect_relationship
shape_aspect_relationship.relating_shape_aspect -&gt;
shape_aspect =&gt;
physical_connectivity_definition
</refpath>
            </aa>	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/

	public static void setShielded_item(SdaiContext context, ERouted_shield_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetShielded_item(context, armEntity);

		if (armEntity.testShielded_item(null))
		{
			ALaminate_shieldable_item aArmShielded_item = armEntity.getShielded_item(null);
			EEntity armShielded_item = null;

			for (int i = 1; i <= aArmShielded_item.getMemberCount(); i++) {
				armShielded_item = aArmShielded_item.getByIndex(i);
				//assembly_component
				// AC <- PDR -> AC (SFTC)
				if (armShielded_item instanceof EAssembly_component_armx) {
						CxAP210ARMUtilities.createProduct_definition_relationship(
															context,
															"shielded item",
															(EAssembly_component_armx) armShielded_item,
															armEntity, false);
				//component_feature
            // AC <- SA <- SAR -> CF/PN/PCD (CF/GPN/PCD)						
				} else if ((armShielded_item instanceof EComponent_feature_armx)||
						(armShielded_item instanceof EGeneric_physical_network)||
						(armShielded_item instanceof EPhysical_connectivity_definition_armx)){
				   LangUtils.Attribute_and_value_structure[] saStructure =
			   	{
			   		new LangUtils.Attribute_and_value_structure(CShape_aspect.attributeOf_shape(null), armShielded_item)
					};
				   EShape_aspect esa = (EShape_aspect)LangUtils.createInstanceIfNeeded(
																							context,
																							CShape_aspect.definition,
																							saStructure);
				   EShape_aspect_relationship esar = (EShape_aspect_relationship)
						context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				   esar.setName(null, "shielded item");
				   esar.setRelated_shape_aspect(null, esa);
				   esar.setRelating_shape_aspect(null, (EShape_aspect)armShielded_item);
				//physical_connectivity_definition
				// AC <- PDR -> R (PCDE, CGER, CER)  
				// AC <- PDR -> R -> DRI (CFER)
				} else if ((armShielded_item instanceof EComponent_external_reference)|| 
						(armShielded_item instanceof EComponent_group_external_reference)||
						(armShielded_item instanceof EPhysical_connectivity_definition_external_reference)||
						(armShielded_item instanceof EComponent_feature_external_reference)){
					ERepresentation er;
					if (armShielded_item instanceof EComponent_feature_external_reference){
						EComponent_feature_external_reference ecfer = (EComponent_feature_external_reference)armShielded_item;
						er = ecfer.getAssociated_component(null);
					}
					else
						er = (ERepresentation)armShielded_item;
					
					EProperty_definition_representation epdr = (EProperty_definition_representation)
						context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				   epdr.setDefinition(null, armEntity);
				   epdr.setUsed_representation(null, er);
				}
				else{
					throw new SdaiException(SdaiException.EI_NVLD, " This type of shield is not supported "+armShielded_item);
				}

			}
		}
	}

	/**
	* Unsets/deletes data for shielded_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/

	public static void unsetShielded_item(SdaiContext context, ERouted_shield_armx armEntity) throws SdaiException
	{
		//	 AC <- PDR -> AC (SFTC)
	   //product_definition_relationship
	   EProduct_definition_relationship relationship = null;
	   AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
	   CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);

	   for (int j = 1; j <= aRelationship.getMemberCount(); j++) {
		   relationship = aRelationship.getByIndex(j);
		   if (relationship.getName(null).equals("shielded item")) {
				relationship.deleteApplicationInstance();
		   }
	   }
	   //	 AC <- SA <- SAR -> CF/PN/PCD (CF/GPN/PCD)
	   AShape_aspect asa = new AShape_aspect();
	   CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aRelationship);
	   for (int i = 1; i <= asa.getMemberCount(); i++) {
	   	EShape_aspect esa = asa.getByIndex(i); 
		   EShape_aspect_relationship sar = null;
		   AShape_aspect_relationship asar = new AShape_aspect_relationship();
		   CShape_aspect_relationship.usedinRelated_shape_aspect(null, esa, context.domain, asar);
	
		   for (int j = 1; j <= asar.getMemberCount(); j++) {
			   sar = asar.getByIndex(j);
			   if (sar.getName(null).equals("shielded item")) {
					sar.deleteApplicationInstance();
			   }
		   }
	   }
		// AC <- PDR -> R (PCDE, CGER, CER)  
		// AC <- PDR -> R -> DRI (CFER)
	   AProperty_definition_representation apdr = new AProperty_definition_representation();
	   CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, aRelationship);
	   for (int i = 1; i <= apdr.getMemberCount(); i++) {
	   	EProperty_definition_representation epdr = apdr.getByIndex(i);
	   	ERepresentation er = epdr.getUsed_representation(null);
	   	if	((er instanceof EComponent_group_external_reference)||
			(er instanceof EPhysical_connectivity_definition_external_reference)||
			(er instanceof EComponent_feature_external_reference)){
	   		epdr.deleteApplicationInstance();
	   		continue;
	   	}
	   	if(!er.testItems(null))
	   		continue;
	   	ARepresentation_item items = er.getItems(null);
	   	for (int j = 1; j <= items.getMemberCount(); j++){
	   		ERepresentation_item item = items.getByIndex(j);
	   		if(item instanceof EComponent_external_reference)
	   			items.removeUnordered(item);
	   	}
	   }	   
	}


}