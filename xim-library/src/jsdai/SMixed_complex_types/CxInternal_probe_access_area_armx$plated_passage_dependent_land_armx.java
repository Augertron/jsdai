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

package jsdai.SMixed_complex_types;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLand_xim.CxLand_armx;
import jsdai.SLand_xim.CxPlated_passage_dependent_land_armx;
import jsdai.SLand_xim.ELand_armx;
import jsdai.SLand_xim.EPlated_passage_dependent_land_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxInternal_probe_access_area_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxProbe_access_area_armx;
import jsdai.SLayered_interconnect_module_design_xim.CxStratum_feature_template_component_armx;
import jsdai.SLayered_interconnect_module_design_xim.EInternal_probe_access_area_armx;
import jsdai.SLayered_interconnect_module_design_xim.EProbe_access_area_armx;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxInternal_probe_access_area_armx$plated_passage_dependent_land_armx
		extends
			CInternal_probe_access_area_armx$plated_passage_dependent_land_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from PDR
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a14$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a14);
	} 
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a14);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a14 = set_instance(a14, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a14 = unset_instance(a14);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a14$;
	}
	// ENDOF taken from PDR

	// Product_view_definition

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a8);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a8);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a8 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a8 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a8$;
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
		return d12$;
	}

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a9);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a9 = get_instance(a9);
		return (EProduct_definition_formation)a9;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a9 = set_instance(a9, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a9$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a10$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a10);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a10 = get_instance(a10);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a3;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a10 = set_instance(a10, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a10 = unset_instance(a10);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a10$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a18);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a18);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a18 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a18 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a18$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a20$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a20);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a20 = get_instance_select(a20);
		return (EEntity)a20;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a20 = set_instance(a20, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a20 = unset_instance(a20);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a20$;
	}
	
	// ENDOF From CProperty_definition.java
	
	// Taken from Shape_element
	/// methods for attribute: id_x, base type: STRING
/*	public boolean testId_x(EShape_element type) throws SdaiException {
		return test_string(a25);
	}
	public String getId_x(EShape_element type) throws SdaiException {
		return get_string(a25);
	}*/
	public void setId_x(EShape_element type, String value) throws SdaiException {
		a25 = set_string(value);
	}
	public void unsetId_x(EShape_element type) throws SdaiException {
		a25 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId_x(EShape_element type) throws SdaiException {
		return a25$;
	}
	// ENDOF taken from Shape_element
	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a24);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a24);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a24 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a24 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a24$;
	}
	
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a23$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a23);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		return (EProduct_definition_shape)get_instance(a23);
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a23 = set_instance(a23, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a23 = unset_instance(a23);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a23$;
	}
	
	// ENDOF From CShape_aspect.java

	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CInternal_probe_access_area$plated_passage_dependent_land.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
			setId_x(context, (EItem_shape)this);
			setId_x(context, (EProbe_access_area_armx)this);
			
			//id - goes directly into AIM
			
			//additional_characterization - this is derived in laminate_component
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setDerived_from(context, this);
			
			// probed_layout_item
			setProbed_layout_item(context, this);
			
         // Connection_area
			setConnection_area(context, this);
			
			// stratum_feature_material_stackup
         setStratum_feature_material_stackup(context, this); 			
			
         // stratum_feature_implementation
         setStratum_feature_implementation(context, this);
		
			// stratum_feature_implementation
			setImplementation_or_resident_stratum(context, this);
			
			// alternate_land_definition  
			setAlternate_land_definition (context, this); 
			
			setReference_plated_passage(context, this);
			
			// Clean ARM specific attributes - this is derived in laminate_component
			// unsetAdditional_characterization(null);
			unsetImplementation_or_resident_stratum(null);

			// associated_layer_connection_point 
			unsetAlternate_land_definition(null); 
         
			unsetAdditional_contexts(null);
			
			unsetDerived_from(null);
			unsetId_x((EItem_shape)null);
			unsetId_x((EShape_element)null);

			// probed_layout_item
			unsetProbed_layout_item(null);
			
         // Connection_area
			unsetConnection_area(null);
			
			// stratum_feature_material_stackup
         unsetStratum_feature_material_stackup(null);
         
         // stratum_feature_implementation
         unsetStratum_feature_implementation(null);
         
         unsetReference_plated_passage(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// Kind of AIM gap
			// unsetDefinition(null);

			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization - this is derived in laminate_component
			// unsetAdditional_characterization(context, this);

			unsetDerived_from(context, this);
			
			unsetId_x(context, (EShape_element)this);
			unsetId_x(context, (EItem_shape)this);			

			// probed_layout_item
			unsetProbed_layout_item(context, this);
			
         // Connection_area
			unsetConnection_area(context, this);
			
			// stratum_feature_material_stackup
         unsetStratum_feature_material_stackup(context, this); 			
			
         // stratum_feature_implementation
         unsetStratum_feature_implementation(context, this);
         
			// stratum_feature_implementation
			unsetImplementation_or_resident_stratum(context, this);
			
			// alternate_land_definition  
			unsetAlternate_land_definition (context, this); 
         
			unsetReference_plated_passage(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  internal_probe_access_area &lt;=
	 *  probe_access_area &lt;=
	 *  assembly_component &lt;=	
	 *  component_definition &lt;=
	 *  product_definition
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
			CInternal_probe_access_area_armx$plated_passage_dependent_land_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxInternal_probe_access_area_armx.setMappingConstraints(context, armEntity);
		CxPlated_passage_dependent_land_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CInternal_probe_access_area_armx$plated_passage_dependent_land_armx armEntity) throws SdaiException {
		CxInternal_probe_access_area_armx.unsetMappingConstraints(context, armEntity);
		CxPlated_passage_dependent_land_armx.unsetMappingConstraints(context, armEntity);
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

	/**
	 * Sets/creates data for id_x attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setId_x(SdaiContext context,
			EProbe_access_area_armx armEntity) throws SdaiException {
		CxProbe_access_area_armx.setId_x(context, armEntity);
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
			EShape_element armEntity) throws SdaiException {
		CxShape_element.unsetId_x(context, armEntity);		
	}
	
	
	//********** "probe_access_area" attributes

	/**
	* Sets/creates data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setProbed_layout_item(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.setProbed_layout_item(context, armEntity);
	}


	/**
	* Unsets/deletes data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetProbed_layout_item(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.unsetProbed_layout_item(context, armEntity);
	}


	/**
	* Sets/creates data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.setConnection_area(context, armEntity);		
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.unsetConnection_area(context, armEntity);		
	}

	/**
	* Sets/creates data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_feature_material_stackup(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.setStratum_feature_material_stackup(context, armEntity);		
	}


	/**
	* Unsets/deletes data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStratum_feature_material_stackup(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxProbe_access_area_armx.unsetStratum_feature_material_stackup(context, armEntity);		
	}

	/**
	* Sets/creates data for probed_layout_item attribute.
	*
	* <p>
	*  <aa attribute="stratum_feature_implementation" assertion_to="Stratum_feature">
	*  <aimelt xml:space="preserve">PATH</aimelt>
	*  <refpath xml:space="preserve">probe_access_area &lt;
	*  probe_access_area &lt;=
	*  assembly_component &lt;=	
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.relating_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'stratum feature implementation'}
	*  shape_aspect_relationship.related_shape_aspect -&gt;
	*  shape_aspect =&gt; 
	*  stratum_feature
	*  </refpath>	
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_feature_implementation(SdaiContext context, EInternal_probe_access_area_armx armEntity) throws SdaiException
	{
		CxInternal_probe_access_area_armx.setStratum_feature_implementation(context, armEntity);
	}


	/**
	* Unsets/deletes data for probed_layout_item attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStratum_feature_implementation(SdaiContext context, EInternal_probe_access_area_armx armEntity) throws SdaiException
	{
		CxInternal_probe_access_area_armx.unsetStratum_feature_implementation(context, armEntity);
	}
	
	/**
	* Sets/creates data for stratum_feature_implementation attribute.
	*
	* <p>
	*  attribute_mapping stratum_feature_implementation_stratum_feature (stratum_feature_implementation
	* , (*PATH*), stratum_feature);
	* 	assembly_component &lt;=
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'stratum feature implementation'}
	*  shape_aspect_relationship.relating_shape_aspect -&gt;
	*  shape_aspect =&gt;
	*  stratum_feature
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setImplementation_or_resident_stratum(SdaiContext context, ELand_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.setImplementation_or_resident_stratum(context, armEntity);
	}


	/**
	* Unsets/deletes data for stratum_feature_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplementation_or_resident_stratum(SdaiContext context, ELand_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.unsetImplementation_or_resident_stratum(context, armEntity);
	}

   //********** "land" attributes

   /**
    * Sets/creates data for alternate_land_definition attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setAlternate_land_definition(SdaiContext context, ELand_armx armEntity) throws SdaiException {
	  CxLand_armx.setAlternate_land_definition(context, armEntity);
   }

  /**
   * Unsets/deletes data for alternate_land_definition attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetAlternate_land_definition(SdaiContext context, ELand_armx armEntity) throws SdaiException {
	  CxLand_armx.unsetAlternate_land_definition(context, armEntity);
  }

  /**
   * Sets/creates data for alternate_land_definition attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setReference_plated_passage(SdaiContext context, EPlated_passage_dependent_land_armx armEntity) throws SdaiException {
	  CxPlated_passage_dependent_land_armx.setReference_plated_passage(context, armEntity);
  }

 /**
  * Unsets/deletes data for alternate_land_definition attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetReference_plated_passage(SdaiContext context, EPlated_passage_dependent_land_armx armEntity) throws SdaiException {
	 CxPlated_passage_dependent_land_armx.unsetReference_plated_passage(context, armEntity);
 }
  

}