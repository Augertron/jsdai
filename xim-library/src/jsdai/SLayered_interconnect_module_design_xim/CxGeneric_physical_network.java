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
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SFunctional_assignment_to_part_xim.EPart_connected_terminals_definition_armx;
import jsdai.SLayered_interconnect_module_design_mim.CPhysical_network;
import jsdai.SProduct_property_definition_schema.*;

public class CxGeneric_physical_network extends CGeneric_physical_network implements EMappedXIMEntity{

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

		setTemp("AIM", CPhysical_network.definition);

		setMappingConstraints(context, this);
		
		// prior_associated_definition
		setPrior_associated_definition(context, this);
		
      // connectivity_requirement_element
		setConnectivity_requirement_element(context, this);
		
		setUnrouted_connectivity_requirement_element(context, this);
		
		setReference_connected_terminals(context, this);
		
		// clean ARM
		// prior_associated_definition
		unsetPrior_associated_definition(null);
		
      // connectivity_requirement_element
		unsetConnectivity_requirement_element(null);
	
		unsetUnrouted_connectivity_requirement_element(null);
		unsetReference_connected_terminals(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// prior_associated_definition
			unsetPrior_associated_definition(context, this);
			
	      // connectivity_requirement_element
			unsetConnectivity_requirement_element(context, this);
			
			unsetUnrouted_connectivity_requirement_element(context, this);
			unsetReference_connected_terminals(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  physical_network &lt;= 
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
			EGeneric_physical_network armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		// AIM gaps
//		if(!armEntity.testProduct_definitional(null))
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EGeneric_physical_network armEntity) throws SdaiException {
	}

	//********** "physical_network" attributes

	/**
	* Sets/creates data for connectivity_requirement_element attribute.
	*
	* <p>
	*  (laminate_component_join_terminal as connectivity_requirement_element);
	*
	* 	physical_network &lt;=
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.relating_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'connectivity requirement'}
	*  shape_aspect_relationship.related_shape_aspect -&gt;
	*  {shape_aspect
	*  (shape_aspect.description = 'component termination passage join terminal')
	*  (shape_aspect.description = 'land join terminal')
	*  (shape_aspect.description = 'non functional land join terminal')
	*  (shape_aspect.description = 'printed component join terminal')}
	*  shape_aspect =&gt;
	*  component_feature =&gt;
	*  laminate_component_feature =&gt;
	*  laminate_component_join_terminal
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PN <- SAR -> CT
	public static void setConnectivity_requirement_element(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
	{
		//unset old values
		unsetConnectivity_requirement_element(context, armEntity);

		if (armEntity.testConnectivity_requirement_element(null))
		{
			ALaminate_component_join_terminal_armx aConnectivity_requirement_element = armEntity.getConnectivity_requirement_element(null);
			for (int i = 1; i <= aConnectivity_requirement_element.getMemberCount(); i++){
				ELaminate_component_join_terminal_armx armConnectivity_requirement_element = aConnectivity_requirement_element.getByIndex(i);

				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);

				sar.setName(null, "connectivity requirement");
				sar.setRelated_shape_aspect(null, armConnectivity_requirement_element);
				sar.setRelating_shape_aspect(null, armEntity);
			}
		}
	}


	/**
	* Unsets/deletes data for connectivity_requirement_element attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnectivity_requirement_element(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
	{
		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
		for(int i=1;i<=relationships.getMemberCount();i++){
			EShape_aspect_relationship relationship = relationships.getByIndex(i);
			if((relationship.testName(null))&&(relationship.getName(null).equals("connectivity requirement")))
				relationship.deleteApplicationInstance();
		}
	}

   /**
    * Sets/creates data for prior_associated_definition attribute.
    *
    * <p>
    *     attribute_mapping prior_associated_definition_interconnect_module (prior_associated_definition
    * , (*PATH*), interconnect_module);
    *  physical_network &lt;=
    *  shape_aspect &lt;-
    *  shape_aspect_relationship.related_shape_aspect
    *  {shape_aspect_relationship
    *  shape_aspect_relationship.name = 'prior associated definition'}
    *  shape_aspect_relationship.relating_shape_aspect -&gt;
    *  shape_aspect.of_shape -&gt;
    *  product_definition_shape &lt;=
    *  property_definition
    *  property_definition.definition -&gt;
    *  characterized_definition
    *  characterized_definition = characterized_product_definition
    *  characterized_product_definition
    *  characterized_product_definition = product_definition
    *  {product_definition
    *  [product_definition.name = 'interconnect module']
    *  [product_definition.frame_of_reference -&gt;
    *  product_definition_context &lt;=
    *  application_context_element
    *  application_context_element.name = 'physical design']}
    *  product_definition =&gt;
    *  physical_unit 
    * end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   // PN <- SAR -> PDS -> PU
    public static void setPrior_associated_definition(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
    {
       //unset old values
       unsetPrior_associated_definition(context, armEntity);

       if (armEntity.testPrior_associated_definition(null))
       {
          AInterconnect_module aim = armEntity.getPrior_associated_definition(null);
          for(int i=1;i<=aim.getMemberCount();i++){
	          EInterconnect_module eim = aim.getByIndex(i);
	          // SA
	          LangUtils.Attribute_and_value_structure[] saStructure = {
	             new LangUtils.Attribute_and_value_structure(
	             CShape_aspect.attributeOf_shape(null),
	             eim)
	          };
	          EShape_aspect esa = (EShape_aspect)
	             LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition, saStructure);
	          if (!esa.testName(null)) {
	             esa.setName(null, "");
	          }
	          if (!esa.testProduct_definitional(null)) {
	             esa.setProduct_definitional(null, ELogical.UNKNOWN);
	          }

	          // SAR
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				sar.setRelated_shape_aspect(null, armEntity);
				sar.setRelating_shape_aspect(null, esa);
				sar.setName(null, "prior associated definition");
	          
          }
       }
    }

    /**
    * Unsets/deletes data for Prior_associated_definition attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
    public static void unsetPrior_associated_definition(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
    {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1;i<=asar.getMemberCount();i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if(esar.getName(null).equals("prior associated definition")){
				esar.deleteApplicationInstance();
			}
		}
    }

 	/**
 	* Sets/creates data for Unrouted_connectivity_requirement_element attribute.
 	*
 	* <p>
 	*
 	* 	physical_network &lt;=
 	*  shape_aspect &lt;-
 	*  shape_aspect_relationship.relating_shape_aspect
 	*  shape_aspect_relationship
 	*  {shape_aspect_relationship
 	*  shape_aspect_relationship.name = 'unrouted connectivity requirement'}
 	*  shape_aspect_relationship.related_shape_aspect -&gt;
 	*  shape_aspect
 	*  {shape_aspect.name = 'unrouted join'}
 	*  shape_aspect =&gt;
 	*  join_shape_aspect
 	*  end_attribute_mapping;
 	* </p>
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	// PN <- SAR -> JSA
 	public static void setUnrouted_connectivity_requirement_element(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
 	{
 		//unset old values
 		unsetUnrouted_connectivity_requirement_element(context, armEntity);

 		if (armEntity.testUnrouted_connectivity_requirement_element(null))
 		{
 			AUnrouted_join_relationship aInterconnect_module_network_topology_element = armEntity.getUnrouted_connectivity_requirement_element(null);

 			for (int i = 1; i <= aInterconnect_module_network_topology_element.getMemberCount(); i++){
 				EJoin_relationship armInterconnect_module_network_topology_element = aInterconnect_module_network_topology_element.getByIndex(i);

 				EShape_aspect_relationship
 						sar = (EShape_aspect_relationship)
 								context.working_model.createEntityInstance(CShape_aspect_relationship.definition);

 				sar.setName(null, "unrouted connectivity requirement");
 				sar.setRelating_shape_aspect(null, armEntity);
 				sar.setRelated_shape_aspect(null, armInterconnect_module_network_topology_element);
 			}
 		}
 	}


 	/**
 	* Unsets/deletes data for interconnect_module_network_topology_element attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetUnrouted_connectivity_requirement_element(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
 	{
 		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
 		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
 		for(int i=1;i<=relationships.getMemberCount();i++){
 			EShape_aspect_relationship relationship = relationships.getByIndex(i);
 			if((relationship.testName(null))&&(relationship.getName(null).equals("unrouted connectivity requirement")))
 				relationship.deleteApplicationInstance();
 		}
 	}    
	
 	/**
 	* Sets/creates data for reference_connected_terminals attribute.
 	*
 	* <p>
 	*  attribute_mapping reference_connected_terminals_part_connected_terminals_definition (reference_connected_terminals
 	* , (*PATH*), part_connected_terminals_definition);
 	* 	physical_network &lt;=
 	*  shape_aspect &lt;-
 	*  shape_aspect_relationship.related_shape_aspect
 	*  shape_aspect_relationship
 	*  {shape_aspect_relationship
 	*  shape_aspect_relationship.name = 'reference connected terminals'}
 	*  shape_aspect_relationship.relating_shape_aspect -&gt;
 	*  shape_aspect =&gt;
 	*  part_connected_terminals_definition
 	*  end_attribute_mapping;
 	* </p>
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void setReference_connected_terminals(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
 	{
 		//unset old values
 		unsetReference_connected_terminals(context, armEntity);

 		if (armEntity.testReference_connected_terminals(null))
 		{
 			EPart_connected_terminals_definition_armx armReference_connected_terminals = 
 			 armEntity.getReference_connected_terminals(null);

 			EShape_aspect_relationship
 					sar = (EShape_aspect_relationship)
 							context.working_model.createEntityInstance(CShape_aspect_relationship.class);

 			sar.setName(null, "reference connected terminals");
 			sar.setRelated_shape_aspect(null, armEntity);
 			sar.setRelating_shape_aspect(null, armReference_connected_terminals);
 		}
 	}


 	/**
 	* Unsets/deletes data for reference_connected_terminals attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetReference_connected_terminals(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
 	{
  		AShape_aspect_relationship asar = new AShape_aspect_relationship();
  		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
  		for(int i=1;i<=asar.getMemberCount();i++){
  			EShape_aspect_relationship esar = asar.getByIndex(i);
  			if(esar.getName(null).equals("reference connected terminals")){
  				esar.deleteApplicationInstance();
  			}
  		}
 	}
 	
	
}