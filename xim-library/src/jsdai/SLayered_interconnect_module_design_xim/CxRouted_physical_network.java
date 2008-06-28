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
import jsdai.SLayered_interconnect_module_design_mim.CPhysical_network;
import jsdai.SProduct_property_definition_schema.*;

public class CxRouted_physical_network extends CRouted_physical_network implements EMappedXIMEntity{

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
		
		// reference_connected_terminals
		setReference_connected_terminals(context, this);
		
		// routed_connectivity_requirement_element
		setRouted_connectivity_requirement_element(context, this);
		
		// unrouted_connectivity_requirement_element
		setUnrouted_connectivity_requirement_element(context, this);
		
		// probe 
		setProbe(context, this);
		
		// clean ARM
		// prior_associated_definition
		unsetPrior_associated_definition(null);
		
      // connectivity_requirement_element
		unsetConnectivity_requirement_element(null);

		// reference_connected_terminals
		unsetReference_connected_terminals(null);
		
		// routed_connectivity_requirement_element
		unsetRouted_connectivity_requirement_element(null);
		
		// unrouted_connectivity_requirement_element
		unsetUnrouted_connectivity_requirement_element(null);
		
		// probe 
		unsetProbe(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// prior_associated_definition
			unsetPrior_associated_definition(context, this);
			
	      // connectivity_requirement_element
			unsetConnectivity_requirement_element(context, this);
		
			// reference_connected_terminals
			unsetReference_connected_terminals(context, this);
			
			// routed_connectivity_requirement_element
			unsetRouted_connectivity_requirement_element(context, this);
			
			// unrouted_connectivity_requirement_element
			unsetUnrouted_connectivity_requirement_element(context, this);
			
			// probe 
			unsetProbe(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  physical_network &lt;= 
	 *  shape_aspect
	 *  {shape_aspect.description = 'routed physical network'}
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
			ERouted_physical_network armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxGeneric_physical_network.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "routed physical network");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ERouted_physical_network armEntity) throws SdaiException {
		CxGeneric_physical_network.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}

	//********** "physical_network" attributes

	/**
	* Sets/creates data for connectivity_requirement_element attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PN <- SAR -> CT
	public static void setConnectivity_requirement_element(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
	{
		CxGeneric_physical_network.setConnectivity_requirement_element(context, armEntity);		
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
		CxGeneric_physical_network.unsetConnectivity_requirement_element(context, armEntity);		
	}

   /**
    * Sets/creates data for prior_associated_definition attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   // PN <- SAR -> PDS -> PU
    public static void setPrior_associated_definition(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
    {
    	CxGeneric_physical_network.setPrior_associated_definition(context, armEntity);    	
    }

	/**
	* Unsets/deletes data for Reference_connected_terminals attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetReference_connected_terminals(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
	{
		CxGeneric_physical_network.unsetReference_connected_terminals(context, armEntity);		
	}

   /**
    * Sets/creates data for Reference_connected_terminals attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
    public static void setReference_connected_terminals(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
    {
    	CxGeneric_physical_network.setReference_connected_terminals(context, armEntity);    	
    }

	/**
	* Unsets/deletes data for Unrouted_connectivity_requirement_element attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetUnrouted_connectivity_requirement_element(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
	{
		CxGeneric_physical_network.unsetUnrouted_connectivity_requirement_element(context, armEntity);		
	}

   /**
    * Sets/creates data for Unrouted_connectivity_requirement_element attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
    public static void setUnrouted_connectivity_requirement_element(SdaiContext context, EGeneric_physical_network armEntity) throws SdaiException
    {
    	CxGeneric_physical_network.setUnrouted_connectivity_requirement_element(context, armEntity);    	
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
    	CxGeneric_physical_network.unsetPrior_associated_definition(context, armEntity);    	
    }

 	//********** "physical_network" attributes


 	/**
 	* Sets/creates data for Routed_connectivity_requirement_element attribute.
 	*
 	* <p>
 	*
 	* 	physical_network &lt;=
 	*  shape_aspect &lt;-
 	*  shape_aspect_relationship.relating_shape_aspect
 	*  shape_aspect_relationship
 	*  {shape_aspect_relationship
 	*  shape_aspect_relationship.name = 'network topology'}
 	*  shape_aspect_relationship.related_shape_aspect -&gt;
 	*  {shape_aspect
 	*  (shape_aspect.name = 'constrained intra layer join')
 	*  (shape_aspect.name = 'inter stratum join')
 	*  (shape_aspect.name = 'intra stratum join')}
 	*  shape_aspect =&gt;
 	*  join_shape_aspect
 	*  end_attribute_mapping;
 	* </p>
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	// PN <- SAR -> JSA
 	public static void setRouted_connectivity_requirement_element(SdaiContext context, ERouted_physical_network armEntity) throws SdaiException
 	{
 		//unset old values
 		unsetRouted_connectivity_requirement_element(context, armEntity);

 		if (armEntity.testRouted_connectivity_requirement_element(null))
 		{
 			ARouted_join_relationship aInterconnect_module_network_topology_element = armEntity.getRouted_connectivity_requirement_element(null);

 			for (int i = 1; i <= aInterconnect_module_network_topology_element.getMemberCount(); i++){
 				EJoin_relationship armInterconnect_module_network_topology_element = aInterconnect_module_network_topology_element.getByIndex(i);

 				EShape_aspect_relationship
 						sar = (EShape_aspect_relationship)
 								context.working_model.createEntityInstance(CShape_aspect_relationship.definition);

 				sar.setName(null, "network topology");
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
 	public static void unsetRouted_connectivity_requirement_element(SdaiContext context, ERouted_physical_network armEntity) throws SdaiException
 	{
 		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
 		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
 		for(int i=1;i<=relationships.getMemberCount();i++){
 			EShape_aspect_relationship relationship = relationships.getByIndex(i);
 			if((relationship.testName(null))&&(relationship.getName(null).equals("network topology")))
 				relationship.deleteApplicationInstance();
 		}
 	}

     /**
      * Sets/creates data for probe attribute.
      *
      * <p>
      * attribute_mapping probe_probe_access_area (probe
      *  , (*PATH*), probe_access_area);
      *  physical_network &lt;=
      *  shape_aspect &lt;-
      *  shape_aspect_relationship.relating_shape_aspect
      *  shape_aspect_relationship
      *  {shape_aspect_relationship
      *  shape_aspect_relationship.name = 'probe'}
      *  shape_aspect_relationship.related_shape_aspect -&gt;
      *  shape_aspect 
      *  shape_aspect.of_shape -&gt; 
      *  product_definition_shape =&gt;
      *  assembly_component =&gt;	
      *  probe_access_area
      * end_attribute_mapping;
      * </p>
      * @param context SdaiContext.
      * @param armEntity arm entity.
      * @throws SdaiException
      */

      public static void setProbe(SdaiContext context, ERouted_physical_network armEntity) throws SdaiException
      {
         //unset old values
         unsetProbe(context, armEntity);

         if (armEntity.testProbe(null))
         {
            AProbe_access_area_armx probes = armEntity.getProbe(null);
            for(int i=1;i<=probes.getMemberCount();i++){
           		EProbe_access_area_armx probe = probes.getByIndex(i);
           		// SA 
           		LangUtils.Attribute_and_value_structure[] saS = {new LangUtils.Attribute_and_value_structure(
                    CShape_aspect.attributeOf_shape(null),probe)
           		};
           		EShape_aspect armEntitySA = (EShape_aspect)
                	LangUtils.createInstanceIfNeeded(context,CShape_aspect.definition,saS);
           		if(!armEntitySA.testName(null))
           			armEntitySA.setName(null, "");
           		if(!armEntitySA.testProduct_definitional(null))
           			armEntitySA.setProduct_definitional(null, ELogical.UNKNOWN);
	            		
	  	          // SAR
	  				EShape_aspect_relationship sar = (EShape_aspect_relationship)
	  					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
	  				sar.setRelated_shape_aspect(null, armEntitySA);
	  				sar.setRelating_shape_aspect(null, armEntity);
	  				sar.setName(null, "probe");
  	          
            }
         }
      }

  /**
  * Unsets/deletes data for current_associated_definition attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
  public static void unsetProbe(SdaiContext context, ERouted_physical_network armEntity) throws SdaiException
  {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1;i<=asar.getMemberCount();i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if(esar.getName(null).equals("probe")){
				esar.deleteApplicationInstance();
			}
		}
  }
	
	
}