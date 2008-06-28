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

package jsdai.SPhysical_unit_2d_design_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_2d_shape_xim.*;
import jsdai.SPhysical_unit_design_view_mim.EAssembly_component;
import jsdai.SPhysical_unit_design_view_xim.*;
import jsdai.SProduct_property_representation_schema.*;

public class CxAssembly_component_2d_shape_model extends CAssembly_component_2d_shape_model implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

		setMappingConstraints(context, this);

      setShape_characterized_component(context, this);
		
      unsetShape_characterized_component(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetShape_characterized_component(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	*  representation				
	*  [representation.id = 'ac2ds']
	*  [representation.name = 'planar projected shape']
	*  [representation &lt;-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  {property_definition =&gt;
	*  product_definition_shape}
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition
	*  characterized_product_definition = product_definition
	*  product_definition =&gt;
	*  component_definition =&gt;
	*  assembly_component]}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EAssembly_component_2d_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPlanar_projected_shape_model.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationId(context, armEntity, "ac2dsm");
	}

	public static void unsetMappingConstraints(SdaiContext context, EAssembly_component_2d_shape_model armEntity) throws SdaiException
	{
		CxPlanar_projected_shape_model.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationId(context, armEntity, "");
	}

	//**********assembly_component_2d_shape attributes
	/**
	* Sets/creates data for shape_characterized_component attribute.
	* @param context SdaiContext.
	* @param armEntity arm entity.
	*
	attribute_mapping shape_characterized_component(shape_characterized_component, $PATH, Assembly_component_armx);
		shape_representation <=
		representation <-
		property_definition_representation.used_representation
		property_definition_representation
		{property_definition_representation =>
		shape_definition_representation}
		property_definition_representation.definition ->
		property_definition =>
		product_definition_shape =>
		assembly_component

	end_attribute_mapping;
	*
	*/
  // SR <- PDR -> PD -> CD (or CSA)
	public static void setShape_characterized_component(SdaiContext context, EAssembly_component_2d_shape_model armEntity) throws SdaiException {
		if (armEntity.testShape_characterized_component(null)) {
			unsetShape_characterized_component(context, armEntity);
			
			//SdaiModel modelAIM = context.working_model;
			AAssembly_component_armx armShape_characterized_component = armEntity.getShape_characterized_component(null);
			
			SdaiIterator iterator = armShape_characterized_component.createIterator();
			while(iterator.next()){
				// Getting AIM assembly component
				EAssembly_component_armx armComponent = armShape_characterized_component.getCurrentMember(iterator);
                EShape_definition_representation epdr = (EShape_definition_representation)
                    context.working_model.createEntityInstance(CShape_definition_representation.definition);
                epdr.setUsed_representation(null, armEntity);
                epdr.setDefinition(null, armComponent);
			}
		}
	}
	
	/**
	* unSets/deletes data for shape_characterized_component attribute.
	* @param context SdaiContext.
	* @param armEntity arm entity.
	*
	*/
  // SR <- PDR -> PD -> CD (or CSA)
	public static void unsetShape_characterized_component(SdaiContext context, EAssembly_component_2d_shape_model armEntity) throws SdaiException {
      AProperty_definition_representation apdrs = new AProperty_definition_representation();
      CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdrs);
		SdaiIterator iter = apdrs.createIterator();
		while(iter.next()){
			EProperty_definition_representation epdr = apdrs.getCurrentMember(iter);
			EEntity ee = epdr.getDefinition(null);
			if(ee instanceof EAssembly_component){
				epdr.deleteApplicationInstance();
			}
		}
	}	
	
	
}
