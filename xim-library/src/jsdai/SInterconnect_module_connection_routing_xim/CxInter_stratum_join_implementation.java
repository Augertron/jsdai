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

package jsdai.SInterconnect_module_connection_routing_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SLayered_interconnect_module_design_xim.*;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxInter_stratum_join_implementation
		extends
			CInter_stratum_join_implementation implements EMappedXIMEntity{
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// Subtype must set it
		// setTemp("AIM", Cxxx.definition);

			setMappingConstraints(context, this);
			
			// topological_requirement 
			setTopological_requirement(context, this);
			
			// Clean ARM specific attributes
			unsetTopological_requirement(null);			
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// topological_requirement 
			unsetTopological_requirement(context, this);  
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
	 *  (stratum_concept_relationship &lt;=
	 *  [shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'physical net supporting stratum feature conductive join'}]
	 *  [shape_aspect_relationship
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'stratum feature conductive join'}]) 
	 *  (plated_inter_stratum_feature &lt;=
	 *  inter_stratum_feature &lt;=
	 *  assembly_component &lt;=
	 *  component_definition &lt;=
	 *  product_definition
	 *  {product_definition
	 *  (product_definition.description = 'bonded conductive base blind via') 
	 *  (product_definition.description = 'non conductive base blind via') 
	 *  (product_definition.description = 'plated conductive base blind via') 
	 *  (product_definition.description = 'interfacial connection')
	 *  (product_definition.description = 'buried via')
	 *  (product_definition.description = 'component termination passage')
	 *  (product_definition.description = 'plated cutout')
	 *  (product_definition.description = 'plated cutout edge segment')
	 *  (product_definition.description = 'plated interconnect module edge segment')
	 *  (product_definition.description = 'plated interconnect module edge')})
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// Do nothing as it is abstract
	public static void setMappingConstraints(SdaiContext context,
			EInter_stratum_join_implementation armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		armEntity.setDescription((EShape_aspect)null, "physical net supporting stratum feature conductive join");
		// do nothing for another alternative
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EInter_stratum_join_implementation armEntity) throws SdaiException {
		armEntity.unsetDescription((EShape_aspect)null);
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

	//********** "inter_stratum_join_implementation" attributes

	/**
	* Sets/creates data for setTopological_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTopological_requirement(SdaiContext context, EInter_stratum_feature_armx armEntity) throws SdaiException
	{
		CxInter_stratum_join_implementation.setTopological_requirement(context, armEntity);		
	}


	/**
	* Unsets/deletes data for setTopological_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTopological_requirement(SdaiContext context, EInter_stratum_feature_armx armEntity) throws SdaiException
	{
		CxInter_stratum_join_implementation.unsetTopological_requirement(context, armEntity);
	}

	//********** "inter_stratum_join_implementation" attributes

	/**
	* Sets/creates data for topological_requirement attribute.
	*
	* <p>
	*  attribute_mapping topological_requirement_inter_stratum_join_relationship (topological_requirement
	* , (*PATH*), inter_stratum_join_relationship);
	* 	stratum_concept_relationship &lt;=
	*  shape_aspect)
	*  (plated_inter_stratum_feature &lt;=
	*  inter_stratum_feature &lt;=
	*  assembly_component &lt;=
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect)
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'join implementation'}
	*  shape_aspect_relationship.relating_shape_aspect -&gt;
	*  {shape_aspect
	*  shape_aspect.name = 'inter stratum join'}
	*  shape_aspect =&gt;
	*  join_shape_aspect
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTopological_requirement(SdaiContext context, EInter_stratum_join_implementation armEntity) throws SdaiException
	{
		//unset old values
		unsetTopological_requirement(context, armEntity);

		if (armEntity.testTopological_requirement(null))
		{

			EInter_stratum_join_relationship armTopological_requirement = armEntity.getTopological_requirement(null);

			EShape_aspect esa;
			if(armEntity instanceof EShape_aspect)
				esa = (EShape_aspect)armEntity;
			else{
			   LangUtils.Attribute_and_value_structure[] saStructure =
		   	{
		   		new LangUtils.Attribute_and_value_structure(CShape_aspect.attributeOf_shape(null), (EProduct_definition_shape)armEntity)
				};
			   esa = (EShape_aspect)LangUtils.createInstanceIfNeeded(
																						context,
																						CShape_aspect.definition,
																						saStructure);
				if(!esa.testName(null))
					esa.setName(null, "");
				if(!esa.testProduct_definitional(null))
					esa.setProduct_definitional(null, ELogical.UNKNOWN);
				
			}
	        //shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "join implementation");
			shape_aspect_relationship.setRelated_shape_aspect(null, esa);
			shape_aspect_relationship.setRelating_shape_aspect(null, armTopological_requirement);
		}
	}


	/**
	* Unsets/deletes data for topological_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTopological_requirement(SdaiContext context, EInter_stratum_join_implementation armEntity) throws SdaiException
	{
		AShape_aspect asa = new AShape_aspect();
		if(armEntity instanceof EShape_aspect)
			asa.addUnordered((EShape_aspect)armEntity);
		else{
			CShape_aspect.usedinOf_shape(null, (EProduct_definition_shape)armEntity, context.domain, asa);
		}
		for (int j = 1; j <= asa.getMemberCount(); j++) {
			EShape_aspect esa = asa.getByIndex(j);
		
			AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
			CShape_aspect_relationship.usedinRelated_shape_aspect(null, esa, context.domain, aRelationship);
			EShape_aspect_relationship relationship = null;
	
			for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
				relationship = aRelationship.getByIndex(i);
				if (relationship.testName(null) && relationship.getName(null).equals("join implementation")) {
					relationship.deleteApplicationInstance();
				}
			}
		}
	}
	
	
}