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

package jsdai.SLayered_interconnect_module_design_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SLayered_interconnect_module_design_mim.CProbe_access_area;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxProbe_access_area_armx
		extends
			CProbe_access_area_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from PDR
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a11$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a12 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a12 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a12$;
	}
	
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
		a2 = set_instance(a2, value);
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
		a3 = set_instance(a3, value);
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
		a9 = set_instance(a9, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a9$;
	}
	// ENDOF From CProperty_definition.java

	// Taken from Shape_element
	/// methods for attribute: id_x, base type: STRING
/*	public boolean testId_x(EShape_element type) throws SdaiException {
		return test_string(a20);
	}
	public String getId_x(EShape_element type) throws SdaiException {
		return get_string(a20);
	}*/
	public void setId_x(EShape_element type, String value) throws SdaiException {
		a20 = set_string(value);
	}
	public void unsetId_x(EShape_element type) throws SdaiException {
		a20 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId_x(EShape_element type) throws SdaiException {
		return a20$;
	}
	// ENDOF taken from Shape_element
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect type) throws SdaiException {
		return test_string(a16);
	}
	public String getName(EShape_aspect type) throws SdaiException {
		return get_string(a16);
	}*/
	public void setName(EShape_aspect type, String value) throws SdaiException {
		a16 = set_string(value);
	}
	public void unsetName(EShape_aspect type) throws SdaiException {
		a16 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
		return a16$;
	}
	
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a18$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a18);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		return (EProduct_definition_shape)get_instance(a18);
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a18 = set_instance(a18, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a18 = unset_instance(a18);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a18$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a19);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a19);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a19 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a19 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a19$;
	}
	// ENDOF From CShape_aspect.java
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProbe_access_area.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
			setId_x(context, this);
			// It is derived
			// setId_x(context, (EShape_element)this);
			
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
			
			// Clean ARM specific attributes - this is derived in laminate_component
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
			unsetId_x((EItem_shape)null);
			// It is derived
			// unsetId_x((EShape_element)null);

			// probed_layout_item
			unsetProbed_layout_item(null);
			
         // Connection_area
			unsetConnection_area(null);
			
			// stratum_feature_material_stackup
         unsetStratum_feature_material_stackup(null); 			
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
			
			unsetId_x(context, this);			
			// It is derived
			// unsetId_x(context, (EShape_element)this);

			// probed_layout_item
			unsetProbed_layout_item(context, this);
			
         // Connection_area
			unsetConnection_area(context, this);
			
			// stratum_feature_material_stackup
         unsetStratum_feature_material_stackup(context, this); 			
			
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  probe_access_area &lt;=
	 *  assembly_component &lt;=	
	 *  component_definition &lt;=
	 *  product_definition
	 *  {product_definition
	 *  product_definition.name = 'laminate component'}
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
			EProbe_access_area_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		// AIM gap
		armEntity.setOf_shape(null, armEntity);
		CxLaminate_component_armx.setMappingConstraints(context, armEntity);
		CxShape_feature.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EProbe_access_area_armx armEntity) throws SdaiException {
		CxLaminate_component_armx.unsetMappingConstraints(context, armEntity);
		CxShape_feature.unsetMappingConstraints(context, armEntity);
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
/*	 It is derived */
	public static void setId_x(SdaiContext context,
			EProbe_access_area_armx armEntity) throws SdaiException {
		// setting shape_element id_x
		if(armEntity.testId_x((EItem_shape)null)){
			String id = armEntity.getId_x((EItem_shape)null);
			CxAP210ARMUtilities.setId(context, armEntity, id);
		}
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
/*	 It is derived	*/
	public static void unsetId_x(SdaiContext context,
			EProbe_access_area_armx armEntity) throws SdaiException {
		CxShape_element.unsetId_x(context, armEntity);		
	}

	
	//********** "probe_access_area" attributes

	/**
	* Sets/creates data for probed_layout_item attribute.
	*
	* <p>
	*  attribute_mapping probed_layout_item_stratum_feature (probed_layout_item
	* , (*PATH*), stratum_feature);
	* 	probe_access_area &lt;=
	*  assembly_component &lt;=	 
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.relating_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'probed layout item'}
	*  shape_aspect_relationship.related_shape_aspect -&gt;	
	*  shape_aspect =&gt;
	*  stratum_feature
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setProbed_layout_item(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetProbed_layout_item(context, armEntity);

		if (armEntity.testProbed_layout_item(null))
		{

			EStratum_feature_armx armProbed_layout_item = (EStratum_feature_armx)armEntity.getProbed_layout_item(null);
			// SA
         LangUtils.Attribute_and_value_structure[] saStructure =
            {new LangUtils.Attribute_and_value_structure(
            		CShape_aspect.attributeOf_shape(null),
						armEntity)};
         EShape_aspect
            esa = (EShape_aspect)
            LangUtils.createInstanceIfNeeded(context,
                                             CShape_aspect.definition,
                                             saStructure);
			

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) 
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				shape_aspect_relationship.setName(null, "probed layout item");
				shape_aspect_relationship.setDescription(null, "");
				shape_aspect_relationship.setRelated_shape_aspect(null, armProbed_layout_item);
				shape_aspect_relationship.setRelating_shape_aspect(null, esa);
		}
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
		EShape_aspect esa = null;
		AShape_aspect asa = new AShape_aspect();
		CShape_aspect.usedinOf_shape(null, armEntity, context.domain, asa);

		for (int j = 1; j <= asa.getMemberCount(); j++) {
			esa = asa.getByIndex(j);

			EShape_aspect_relationship relationship = null;
			AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
			CShape_aspect_relationship.usedinRelating_shape_aspect(null, esa, context.domain, aRelationship);
	
			for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
				relationship = aRelationship.getByIndex(i);
				if (relationship.testName(null) && relationship.getName(null).equals("probed layout item")) {
					relationship.deleteApplicationInstance();
				}
			}
		}
	}


	/**
	* Sets/creates data for Connection_area attribute.
	*
	*/
	public static void setConnection_area(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		CxShape_feature.setConnection_area(context, armEntity);
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
		CxShape_feature.unsetConnection_area(context, armEntity);
	}

	/**
	* Sets/creates data for probed_layout_item attribute.
	*
	* <p>
	*  <aa attribute="stratum_feature_material_stackup" assertion_to="Multi_layer_stratum_feature">
	*  <aimelt xml:space="preserve">PATH</aimelt>
	*  <refpath xml:space="preserve">probe_access_area &lt;
	*  assembly_component &lt;=	
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.relating_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'stratum feature material stackup'}
	*  shape_aspect_relationship.related_shape_aspect -&gt;
	*  shape_aspect =&gt;
	*  stratum_feature =&gt;
	*  multi_layer_stratum_feature
	*  </refpath>	
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_feature_material_stackup(SdaiContext context, EProbe_access_area_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetStratum_feature_material_stackup(context, armEntity);

		if (armEntity.testStratum_feature_material_stackup(null))
		{

			EStratum_feature_based_derived_pattern_armx materialStackup = armEntity.getStratum_feature_material_stackup(null);
			// PD
			LangUtils.Attribute_and_value_structure[] pdStructure =
            {new LangUtils.Attribute_and_value_structure(
            		CProperty_definition.attributeDefinition(null),
            		materialStackup)};
			EProperty_definition epd = (EProperty_definition)
            	LangUtils.createInstanceIfNeeded(context,
                                             CProperty_definition.definition,
                                             pdStructure);
			if(!epd.testName(null)){
				epd.setName(null, "");
			}
			// property_definition_relationship
			EProperty_definition_relationship epdr = (EProperty_definition_relationship) 
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setName(null, "stratum feature material stackup");
			epdr.setDescription(null, "");
			epdr.setRelated_property_definition(null, armEntity);
			epdr.setRelating_property_definition(null, epd);
		}
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
		EProperty_definition_relationship epdr = null;
		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);

		for (int j = 1; j <= apdr.getMemberCount(); j++) {
			epdr = apdr.getByIndex(j);
			if (epdr.testName(null) && epdr.getName(null).equals("stratum feature material stackup")) {
				epdr.deleteApplicationInstance();
			}
		}
	}
	

}