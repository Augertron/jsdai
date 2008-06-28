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

package jsdai.SLayered_interconnect_module_2d_design_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SConstructive_solid_geometry_2d_mim.CCsg_2d_shape_representation;
import jsdai.SLayered_interconnect_module_design_xim.EStratum_feature_armx;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;

public class CxStratum_feature_planar_shape_model extends CStratum_feature_planar_shape_model implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CCsg_2d_shape_representation.definition);

		setMappingConstraints(context, this);

		// Shape_characterized_component 
		setShape_characterized_component (context, this);
		
		// clean ARM
		// shape_characterized_part_template
		unsetShape_characterized_component (null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// shape_characterized_part_template
		unsetShape_characterized_component (context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	*  representation				
	*  [representation.id = 'fsd']
	*  [representation.name = 'planar projected shape']
	*  [representation.description = 'sfps']}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_feature_planar_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
//		CxPlanar_projected_shape_model.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "sfps");
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_feature_planar_shape_model armEntity) throws SdaiException
	{
//		CxPlanar_projected_shape_model.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "");
	}	
	
	//********** "stratum_feature_planar_shape" attributes

	/**
	* Sets/creates data for Shape_characterized_component attribute.
	*
	* <p>
	*  attribute_mapping Shape_characterized_component_stratum_feature (Shape_characterized_component
	* , (*PATH*), stratum_feature);
	* 	shape_representation <=
	* 	representation <-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition ->
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = shape_definition
	* 	shape_definition
	* 	shape_definition = shape_aspect
	* 	shape_aspect =>
	* 	stratum_feature
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- PDR -> PD -> SF
	public static void setShape_characterized_component(SdaiContext context, EStratum_feature_planar_shape_model armEntity) throws SdaiException
	{
		//unset old values
		unsetShape_characterized_component(context, armEntity);

      if (armEntity.testShape_characterized_component(null)){
         EStratum_feature_armx armShape_characterized_component = armEntity.getShape_characterized_component(null);
         // PD -> SF
         LangUtils.Attribute_and_value_structure[] pdStructure =
            {new LangUtils.Attribute_and_value_structure(
               CProperty_definition.attributeDefinition(null), armShape_characterized_component)};
         EProperty_definition epd = (CProperty_definition)
            LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
         if(!epd.testName(null))
            epd.setName(null, "");
         // PDR

         //  PropD <- PDR -> PD
         EProperty_definition_representation epdr = (EProperty_definition_representation)
                                                 context.working_model.createEntityInstance(CProperty_definition_representation.definition);
         // SR <- PDR
         epdr.setUsed_representation(null, armEntity);
         // PDR -> PD
         epdr.setDefinition(null, epd);
		}
	}


	/**
	* Unsets/deletes data for Shape_characterized_component attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // SR <- PDR -> PD -> SF. Strategy - remove all PDRs of this kind
	public static void unsetShape_characterized_component(SdaiContext context, EStratum_feature_planar_shape_model armEntity) throws SdaiException
	{
      // PD <- PDS
      AProperty_definition_representation apdr = new AProperty_definition_representation();
      CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdr);
      for(int i=1;i<=apdr.getMemberCount();){
         EProperty_definition_representation epdr = apdr.getByIndex(i);
         if((epdr.testDefinition(null))&&(epdr.getDefinition(null) instanceof EProperty_definition)){
            EProperty_definition epd = (EProperty_definition)epdr.getDefinition(null);
            if((epd.testDefinition(null))&&(epd.getDefinition(null) instanceof EStratum_feature_armx)){
               apdr.removeByIndex(i);
               epdr.deleteApplicationInstance();
            }
            else
               i++;
         }
         else
            i++;
      }
	}
	
}
