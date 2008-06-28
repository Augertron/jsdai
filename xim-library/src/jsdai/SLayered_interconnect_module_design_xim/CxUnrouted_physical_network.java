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
import jsdai.SLayered_interconnect_module_design_mim.CPhysical_network;
import jsdai.SProduct_property_definition_schema.*;

public class CxUnrouted_physical_network extends CUnrouted_physical_network implements EMappedXIMEntity{

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
		
		
		// unrouted_connectivity_requirement_element
		setUnrouted_connectivity_requirement_element(context, this);
		
		// clean ARM
		// prior_associated_definition
		unsetPrior_associated_definition(null);
		
      // connectivity_requirement_element
		unsetConnectivity_requirement_element(null);

		// reference_connected_terminals
		unsetReference_connected_terminals(null);
		
		// unrouted_connectivity_requirement_element
		unsetUnrouted_connectivity_requirement_element(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// prior_associated_definition
			unsetPrior_associated_definition(context, this);
			
	      // connectivity_requirement_element
			unsetConnectivity_requirement_element(context, this);
		
			// reference_connected_terminals
			unsetReference_connected_terminals(context, this);
			
			// unrouted_connectivity_requirement_element
			unsetUnrouted_connectivity_requirement_element(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  mapping_constraints;
		physical_network <= 
		shape_aspect
		{shape_aspect.description = 'unrouted physical network'}
		end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EUnrouted_physical_network armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxGeneric_physical_network.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "unrouted physical network");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EUnrouted_physical_network armEntity) throws SdaiException {
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
    	
}