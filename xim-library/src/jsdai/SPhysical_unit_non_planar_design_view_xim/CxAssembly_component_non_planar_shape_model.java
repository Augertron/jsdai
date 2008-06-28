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

package jsdai.SPhysical_unit_non_planar_design_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SPhysical_unit_design_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_property_representation_schema.CShape_representation;

public class CxAssembly_component_non_planar_shape_model extends CAssembly_component_non_planar_shape_model implements EMappedXIMEntity
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
		
		unsetShape_characterized_component(context, this);
		
		// Clean ARM
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
	* 	manifold_subsurface_shape_representation &lt;=
	*  shape_representation &lt;=
	*  representation
	*  {representation
	*  [representation.id = 'acnps']
	*  [representation.name = 'manifold subsurface']
	*  [representation.description = 'acnps']}
	*  representation &lt;-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition
	*  characterized_product_definition = product_definition
	*  product_definition =&gt;
	*  component_definition =&gt;
	*  assembly_component	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EAssembly_component_non_planar_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "manifold subsurface");
		CxAP210ARMUtilities.setRepresentationId(context, armEntity, "acnps");
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "acnps");
	}

	public static void unsetMappingConstraints(SdaiContext context, EAssembly_component_non_planar_shape_model armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
		CxAP210ARMUtilities.setRepresentationId(context, armEntity, "");
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "");
	}

	//**********assembly_component_3d_shape attributes
	/**
	* Sets/creates data for shape_characterized_component attribute.
	* @param context SdaiContext.
	* @param armEntity arm entity.
	*
	*  attribute_mapping shape_characterized_component_assembly_component (shape_characterized_component
	* , (*PATH*), assembly_component);
	* 	shape_representation <=
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	({property_definition =>
	* 	product_definition_shape}
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition =>
	* 	component_definition)
	* 	
	*  end_attribute_mapping;
	*
	*/
  // SR <- PDR -> PD -> CD (or CSA)
	public static void setShape_characterized_component(SdaiContext context, EAssembly_component_non_planar_shape_model armEntity) throws SdaiException {
		if (armEntity.testShape_characterized_component(null)) {
			unsetShape_characterized_component(context, armEntity);
			
			//SdaiModel modelAIM = context.working_model;
			AAssembly_component_armx armShape_characterized_component = armEntity.getShape_characterized_component(null);
         SdaiIterator iterator = armShape_characterized_component.createIterator();
         AEntity users2 = new AEntity();
         armEntity.findEntityInstanceUsers(context.domain, users2);
         AProperty_definition_representation apdrs = new AProperty_definition_representation();
         CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdrs);
         AProperty_definition property_definitions = new AProperty_definition();
         SdaiIterator apdrIterator = apdrs.createIterator();
         while(apdrIterator.next()){
            EProperty_definition_representation temp = apdrs.getCurrentMember(apdrIterator);
            EEntity ee = temp.getDefinition(null);
            if(ee instanceof EProperty_definition){
               property_definitions.addUnordered(ee);
            }
         }
         SdaiIterator apdIterator = property_definitions.createIterator();
			while(iterator.next()){
				// Getting AIM assembly component
				EAssembly_component_armx armComponent = armShape_characterized_component.getCurrentMember(iterator);
				// jsdai.SElectronic_assembly_interconnect_and_packaging_design.EComponent_definition aimComponent;
				EProperty_definition property_definition;
		      LangUtils.Attribute_and_value_structure[] pdsStructure =
						{new LangUtils.Attribute_and_value_structure(CProduct_definition_shape.attributeDefinition(null), armComponent)};
			      property_definition = (EProduct_definition_shape)LangUtils.createInstanceIfNeeded(
							context, CProduct_definition_shape.definition, pdsStructure);
				if(!property_definition.testName(null))
					property_definition.setName(null, "");
				// Setting link AC3DS -> AC in AIM
				// EProduct_definition_shape epd = (EProduct_definition_shape)modelAIM.createEntityInstance(CProduct_definition_shape.class);
				// epd.setName(null, "");
//					System.out.println(i+"-th unit = "+aimUnits.getByIndexEntity(i));
				// epd.setDefinition(null, aimComponent);
				// REUSE
            // This is optimization in order to speed up usedin

//              System.err.println(" AC3DS "+armEntity+" "+users1.getMemberCount()+" "+users2.getMemberCount()+" "+armShape_characterized_component.getMemberCount());
              boolean epdNotFound = true;
              apdIterator.beginning();
              while(apdIterator.next()){
                 EProperty_definition temp = property_definitions.getCurrentMember(apdIterator);
                 if(temp == property_definition){
                    epdNotFound = false;
                    break;
                 }
              }
              if(epdNotFound){
                 EProperty_definition_representation epdr = (EProperty_definition_representation)
                    context.working_model.createEntityInstance(CProperty_definition_representation.definition);
                 epdr.setUsed_representation(null, armEntity);
                 epdr.setDefinition(null, property_definition);
              }
				// System.err.println(armComponent+" PDR "+epdr);
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
	public static void unsetShape_characterized_component(SdaiContext context, EAssembly_component_non_planar_shape_model armEntity) throws SdaiException {
      AProperty_definition_representation apdrs = new AProperty_definition_representation();
      CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdrs);
		SdaiIterator iter = apdrs.createIterator();
		while(iter.next()){
			EProperty_definition_representation epdr = apdrs.getCurrentMember(iter);
			EEntity ee = epdr.getDefinition(null);
			if(ee instanceof EProperty_definition){
				EProperty_definition epd = (EProperty_definition)ee;
				if(epd.getDefinition(null) instanceof EAssembly_component_armx){
					epdr.deleteApplicationInstance();
				}
			}
		}
	}	
	
}
