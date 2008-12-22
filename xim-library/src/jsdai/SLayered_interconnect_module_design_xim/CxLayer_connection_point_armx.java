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
import jsdai.SGeometry_schema.ECartesian_point;
import jsdai.SLayered_interconnect_module_design_mim.CLayer_connection_point;
import jsdai.SPhysical_unit_2d_shape_xim.*;
import jsdai.SPhysical_unit_design_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxLayer_connection_point_armx extends CLayer_connection_point_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
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
	
	// ENDOF From CShape_aspect.java
	

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLayer_connection_point.definition);

		setMappingConstraints(context, this);
		
		// location_2d
		setLocation_2d(context, this);
		
      // location_3d
		setLocation_3d(context, this);
		
	
		// Clean ARM

		// location_2d
		unsetLocation_2d(null);
		
      // location_3d
		unsetLocation_3d(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// location_2d
			unsetLocation_2d(context, this);
			
	      // location_3d
			unsetLocation_3d(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; layer_connection_point &lt;=
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
			ELayer_connection_point_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		// if(!armEntity.testProduct_definitional(null))
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
			ELayer_connection_point_armx armEntity) throws SdaiException {
	}


	//********** "layer_connection_point" attributes

	/**
	* Sets/creates data for location_2d attribute.
	*
	*  (cartesian_point as location_2d);
	*
	* 	layer_connection_point <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	(###create) property_definition.definition
	* 	property_definition <-
	* 	(###create) property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	{[representation =>
	* 	(###create) shape_representation]
	* 	[representation
	* 	representation.name = `connection point location 2d']}
	*  representation 
	*  representation.items[i] -> 
	*  representation_item => 
	*  geometric_representation_item => 
	*  {geometric_representation_item 
	*  geometric_representation_item.dim = 2} 
	*  point => 
	*  cartesian_point 
	*   
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// LCP <- PD <- PDR -> R -> CP
	public static void setLocation_2d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetLocation_2d(context, armEntity);
		// System.err.println(" ELLCPoint.L2D before ... " + armEntity.findEntityInstanceSdaiModel().getRepository().getSessionIdentifier("#48").getTemp("AIM"));
		//no reuse - entire mapping path is created anew - what a hell !!!!
		if (armEntity.testLocation_2d(null))
		{
			ECartesian_point armLocation_2d = armEntity.getLocation_2d(null);

				EProperty_definition property_definition = (EProperty_definition)
					context.working_model.createEntityInstance(CProperty_definition.definition);
				property_definition.setName(null, "");
				property_definition.setDefinition(null, armEntity);

				EProperty_definition_representation
						property_definition_representation = (EProperty_definition_representation)
								context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				property_definition_representation.setDefinition(null, property_definition);

				EShape_representation
						shape_representation = (EShape_representation)
								context.working_model.createEntityInstance(CShape_representation.definition);

				property_definition_representation.setUsed_representation(null, shape_representation);

				shape_representation.setName(null, "connection point location 2d");

				ARepresentation_item
						items = shape_representation.createItems(null);
				items.addUnordered(armLocation_2d );
            // This context must reused from PCB
            //
//				System.err.println(" Getting STRATUM for "+armEntity);
            EDesign_layer_stratum_armx stratum = armEntity.getResident_design_layer_stratum(null);
            // cache it
            Object temp = stratum.getTemp("Assembly");
            AInterconnect_module_stratum_assembly_relationship imsar;
            if(temp == null){
	            imsar = stratum.getAssembly(null, null);
	            if(imsar.getMemberCount() != 1){
	               throw new SdaiException(SdaiException.AI_NVLD, " S <- IMSAR -> PCB must be 1:1 "+imsar.getMemberCount());
	            }
	            stratum.setTemp("Assembly", imsar);
            }
            else
            	imsar = (AInterconnect_module_stratum_assembly_relationship)temp;
            //System.err.println(" ELLCPoint.L2D Middle ... " + armEntity.findEntityInstanceSdaiModel().getRepository().getSessionIdentifier("#48").getTemp("AIM"));
            EInterconnect_module_design_view_armx pcb = imsar.getByIndex(1).getAssembly(null);
            EPhysical_unit_planar_shape_model pups = get_PUPS_with_specific_predefined_purpose(pcb, EPredefined_planar_purpose.DESIGN);
            if(pups == null){
               throw new SdaiException(SdaiException.EI_NVLD, " PCB must have PUPS with DESIGN purpose here "+pcb);
            }
            ERepresentation_context erc = pups.getContext_of_items(null);
            //EA ccs.createAimData(context);
				shape_representation.setContext_of_items(null, erc);

//				erc.setCoordinate_space_dimension(null, 2);
				//System.err.println(" ELLCPoint.L2D After ... " + armEntity.findEntityInstanceSdaiModel().getRepository().getSessionIdentifier("#48").getTemp("AIM"));
//			}
		}
	}


	 // Searches for the passed physical_unit for the specific PUPS (with specified purpose).
	 private static EPhysical_unit_planar_shape_model get_PUPS_with_specific_predefined_purpose(
	         EPart_design_view epu, int preDefinedPurpose) throws
	         SdaiException {
	     // Check for specific representation
	     if (epu != null) {
			  EPhysical_unit_planar_shape_model specificShapeARM = null;
			  Object shapesCandidates = epu.getTemp("PUPSs");
			  APhysical_unit_planar_shape_model shapes;
			  if(shapesCandidates == null){
			  		shapes = new APhysical_unit_planar_shape_model();
			  		CPhysical_unit_planar_shape_model.usedinShape_characterized_definition(null,
			  				epu, null, shapes);
			  		epu.setTemp("PUPSs", shapes);
			  }
			  else
			  		shapes = (APhysical_unit_planar_shape_model)shapesCandidates;
			  
			  for (int v = 1, n=shapes.getMemberCount(); v <= n; v++) {
				  if (shapes.getByIndex(v).getShape_purpose(null) ==
						preDefinedPurpose) {
					  specificShapeARM = (EPhysical_unit_planar_shape_model) shapes.
						  getByIndexEntity(v);
					  return specificShapeARM;
				  }
			  }
		  }
	     return null;
	 }

	
	
	/**
	* Unsets/deletes data for location_2d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation_2d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{

		if (armEntity.testLocation_2d(null)){
			AProperty_definition apd = new AProperty_definition(); 
			CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
			for(int k=1;k<=apd.getMemberCount();k++){
				EProperty_definition property_definition = apd.getByIndex(k);
				AProperty_definition_representation apdr = new AProperty_definition_representation(); 
				CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, apdr);
				for(int v=1;v<=apdr.getMemberCount();v++){				
					EProperty_definition_representation 
						property_definition_representation = apdr.getByIndex(v);
					ERepresentation representation = 
					property_definition_representation.getUsed_representation(null);
					if(!(representation.getName(null).equals("connection point location 2d")))
						continue;
					if (representation.testItems(null)){
						ARepresentation_item
						items = representation.getItems(null);
						if (items.getMemberCount() > 0){
							items.clear();
						}
						representation.unsetItems(null);
					}
					LangUtils.deleteInstanceIfUnused(context.domain, property_definition_representation);
					LangUtils.deleteInstanceIfUnused(context.domain, representation);
				}
			}
		}
	}

	/**
	* Sets/creates data for location_3d attribute.
	*
	*  (cartesian_point as location_3d)
	*
	* 	layer_connection_point <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	{[representation =>
	* 	shape_representation]
	* 	[representation
	* 	representation.name = `connection point location 3d']}
	* 	representation
	* 	representation.items[1] ->
	* 	representation_item =>
	* 	geometric_representation_item =>
	* 	{geometric_representation_item
	* 	<- representation.items
	* 	representation.context_of_items ->
	* 	representation_context =>
	* 	geometric_representation_context
	* 	geometric_representation_context.coordinate_space_dimension = 3}
	* 	point =>
	* 	cartesian_point
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocation_3d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{

		if (armEntity.testLocation_3d(null))
		{
			throw new SdaiException(SdaiException.FN_NAVL," setLocation_3d is not implemented ");
		}
	}


	/**
	* Unsets/deletes data for location_3d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation_3d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		if (armEntity.testLocation_3d(null)){
			throw new SdaiException(SdaiException.FN_NAVL," unsetLocation_3d is not implemented ");
		}
	}

	
}