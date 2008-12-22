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

package jsdai.SPhysical_unit_shape_with_parameters_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.dictionary.*;
import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SAltered_package_xim.EPredefined_geometric_status;
import jsdai.SGeometry_schema.ECartesian_point;
import jsdai.SPart_view_definition_xim.EPart_view_definition;
import jsdai.SPhysical_unit_2d_shape_xim.*;
import jsdai.SPhysical_unit_3d_shape_xim.*;
import jsdai.SPhysical_unit_usage_view_mim.EPhysical_unit;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_parameters_xim.*;

public class CxPhysical_unit_shape_model extends CPhysical_unit_shape_model implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

      setShape_characterized_definition(context, this);
		setShape_material_condition(context, this);      
		setCentroid_location(context, this);      
		setShape_environment(context, this);      
		setShape_extent(context, this);      
		setShape_approximation_level(context, this);
		setMass_property_quality(context, this);
		
      unsetShape_characterized_definition(null);
      unsetShape_material_condition(null);      
      unsetCentroid_location(null);      
      unsetShape_environment(null);      
      unsetShape_extent(null);      
      unsetShape_approximation_level(null);
      unsetMass_property_quality(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      unsetShape_characterized_definition(context, this);
      unsetShape_material_condition(context, this);      
      unsetCentroid_location(context, this);      
      unsetShape_environment(context, this);      
      unsetShape_extent(context, this);      
      unsetShape_approximation_level(context, this);
      unsetMass_property_quality(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	shape_representation &lt;=
	* 	representation
	* 	{representation &lt;-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition -&gt;	
	*  property_definition	
	* 	property_definition.definition -&gt;
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition =&gt;
	* 	physical_unit}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// All the constraints are captured by setting attributes
	public static void setMappingConstraints(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException
	{
	}



   /**
    * physical_unit_planar_shape.shape_characterized_physical_unit: LIST [1:2] OF UNIQUE physical_unit;
    * @param context
    * @param armEntity
    * @throws SdaiException
    * According the mapping:
    *    shape_representation <=
    *    representation <-
    *    property_definition_representation.used_representation
    *    property_definition_representation
    *    property_definition_representation.definition ->
    *    property_definition
    *    property_definition.definition ->
    *    characterized_definition
    *    characterized_definition = characterized_product_definition
    *    characterized_product_definition
    *    characterized_product_definition = product_definition
    *    {product_definition
    *    product_definition.frame_of_reference ->
    *    product_definition_context <=
    *    application_context_element
    *    (application_context_element.name = `physical design')
    *    (application_context_element.name = `physical design usage')}
    *    product_definition =>
    *    (physical_unit)
    *    (physical_unit =>
    *    externally_defined_physical_unit)
    *    (physical_unit =>
    *    externally_defined_physical_unit =>
    *    library_defined_physical_unit)
    */
   // SR <- PDR -> PropD -> PU
   public static void setShape_characterized_definition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
      unsetShape_characterized_definition(context, armEntity);
//			System.out.println(" in setShape_characterized_definition " + armEntity);
      if (armEntity.testShape_characterized_definition(null)) {
         SdaiModel modelAIM = context.working_model;
         EPart_view_definition armUnit = armEntity.getShape_characterized_definition(null);

         EShape_definition_representation epdr = (EShape_definition_representation) modelAIM.createEntityInstance(
            CShape_definition_representation.definition);
         epdr.setUsed_representation(null, armEntity);
         epdr.setDefinition(null, armUnit);
         //System.err.println(epdr+" Creating PDR for "+aimEntity);
      }
   }

   public static void unsetShape_characterized_definition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
      //EPhysical_unit epu = armEntity.getShape_characterized_physical_unit(null);
      //EProduct_definition epd = (EProduct_definition) epu.getAimInstance();
      AProperty_definition_representation apdr = new AProperty_definition_representation();
      CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdr);
      for (int i = 1; i <= apdr.getMemberCount();i++) {
         EProperty_definition_representation epdr = apdr.getByIndex(i);
         if((!epdr.testDefinition(null))||(!(epdr.getDefinition(null) instanceof EPhysical_unit))){
            continue;
         }
         epdr.deleteApplicationInstance();
      }
   }

   /**
    * physical_unit_planar_shape.centroid_location: OPTIONAL cartesian_point;
    * @param context
    * @param armEntity
    * @throws SdaiException
    * According the mapping:
    *    shape_representation <=
    *    representation <-
    *    representation_relationship.rep_1
    *    representation_relationship
    *    representation_relationship.rep_2 ->
    *    representation
    *    {representation.name = `shape material condition'}
    *    representation.items[i] ->
    *    representation_item
    *    {(representation_item.name = `maximum material condition')
    *    (representation_item.name = `minimum material condition')
    *    (representation_item.name = `nominal material condition')}
    *    representation_item =>
    *    descriptive_representation_item
    */
   // SR <- RR -> R -> DRI
   public static void setShape_material_condition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
      String[] list = {
         "maximum material condition",
         "minimum material condition",
         "nominal material condition"};
      String keyword = "shape material condition";
      setRepresentation_item(context, armEntity,
                             CPhysical_unit_shape_model.attributeShape_material_condition(null),
                             keyword, list);
   }

   // SR <- RR -> R -> DRI - just remove DRI
   public static void unsetShape_material_condition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
      String[] list = {
         "maximum material condition",
         "minimum material condition",
         "nominal material condition"};
      String keyword = "shape material condition";
      unsetRepresentation_item(context, armEntity, keyword, list);
   }

   /**
    * physical_unit_planar_shape.shape_environment: environment;
    * @param context
    * @param armEntity
    * @throws SdaiException
    * According the mapping:
    * shape_representation <=
    *    representation <-
    *    representation_relationship.rep_1
    *    representation_relationship
    *    representation_relationship.rep_2 ->
    *    representation
    *    {representation.name = `shape environment'}
    *    representation.items[i] ->
    *    representation_item
    *    {(representation_item.name = `manufacturing')
    *		(representation_item.name = `end user application')}
    *    representation_item =>
    *    descriptive_representation_item
    */
   // SR <- RR -> R -> DRI
   public static void setShape_environment(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
      String[] list = {
         "manufacturing",
         "end user application"};
      String keyword = "shape environment";
      setRepresentation_item(context, armEntity,
                             CPhysical_unit_shape_model.attributeShape_environment(null),
                             keyword, list);
   }

   // SR <- RR -> R -> DRI - just remove DRI
   public static void unsetShape_environment(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
      String[] list = {
         "manufacturing",
         "end user application"};
      String keyword = "shape environment";
      unsetRepresentation_item(context, armEntity, keyword, list);
   }

   /**
    * physical_unit_planar_shape.shape_material_condition: material_condition;
    * @param context
    * @param armEntity
    * @throws SdaiException
    * According the mapping:
    *    	shape_representation
    *       shape_representation <=
    *       representation
    *       representation.items[i] ->
    *       representation_item
    *       representation_item =>
    *       geometric_representation_item =>
    *       point =>
    *       cartesian_point
    */
   // R -> CP
   public static void setCentroid_location(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	unsetCentroid_location(context, armEntity);
      if (armEntity.testCentroid_location(null)) {
         ECartesian_point armLocation = armEntity.getCentroid_location(null);
//EA         armLocation.createAimData(context);
         // R -> CP
         ARepresentation_item items;
         if (armEntity.testItems(null)) {
            items = armEntity.getItems(null);
         }
         else {
            items = armEntity.createItems(null);
         }
         items.addUnordered(armLocation);
      }
   }

   // R -> CP
   public static void unsetCentroid_location(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
      if (armEntity.testCentroid_location(null)) {
         if (armEntity.testItems(null)) {
            ARepresentation_item items = armEntity.getItems(null);
            for (int i = 1; i <= items.getMemberCount(); ) {
               ERepresentation_item item = items.getByIndex(i);
               if (item instanceof jsdai.SGeometry_schema.ECartesian_point) {

                  // We can't delete, since it has meaning from ARM
                  items.removeUnordered(item);
               }
            }
         }

      }
   }

   // generic methods suitable for few attribut mappings at once
   // SR <- RR -> R -> DRI
   public static void setRepresentation_item(SdaiContext context, EShape_representation armEntity,
                                             EAttribute attribute, String keyword, String[] values) throws
      SdaiException {
      // unset old values
      unsetRepresentation_item(context, armEntity, keyword, values);
      EDefined_type[] types = new EDefined_type[2]; // As needed for shape_purpose
      if (armEntity.testAttribute(attribute, types) == 2) { // INT
         int armValue = armEntity.get_int(attribute);
         // Search for suitable Representation, if it exists
         jsdai.SRepresentation_schema.ERepresentation suitableRepresentation = null;
         ARepresentation_relationship relationships = new ARepresentation_relationship();
         CRepresentation_relationship.usedinRep_1(null, armEntity, context.domain, relationships);
         for (int i = 1; i <= relationships.getMemberCount(); i++) {
            jsdai.SRepresentation_schema.ERepresentation representation =
               relationships.getByIndex(i).getRep_2(null);
            if(!representation.getInstanceType().getName(null).equals("representation")){
            	continue;
            }
            if ( (representation.testName(null)) &&
                (representation.getName(null).equals(keyword))) {
               suitableRepresentation = representation;
               break;
            }
         }
         if (suitableRepresentation == null) {
            // R
            suitableRepresentation = (jsdai.SRepresentation_schema.ERepresentation)
               context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
            suitableRepresentation.setName(null, keyword);
            // Context
            ERepresentation_context repContext = 
               CxAP210ARMUtilities.createRepresentation_context(context,
                                                                          "", "", true);
            suitableRepresentation.setContext_of_items(null, repContext);
            // RR
            ERepresentation_relationship relationship = (ERepresentation_relationship)
               context.working_model.createEntityInstance(CRepresentation_relationship.class);
            relationship.setName(null, "");
            relationship.setRep_1(null, armEntity);
            relationship.setRep_2(null, suitableRepresentation);
         }
         // DRI
         EDescriptive_representation_item item = (EDescriptive_representation_item)
            context.working_model.createEntityInstance(CDescriptive_representation_item.class);
         if (attribute.getName(null).equals("shape_material_condition")) {
            item.setName(null, EMaterial_condition.toString(armValue).toLowerCase().replace('_', ' '));
         }
         else if (attribute.getName(null).equals("shape_environment")) {
            item.setName(null, EApplication_environment.toString(armValue).toLowerCase().replace('_', ' '));
         }
         else if (attribute.getName(null).equals("shape_location_with_respect_to_seating_plane")) {
            item.setName(null, EPu2ds_shape_location.toString(armValue).toLowerCase().replace('_', ' '));
            // For Package_planar_shape
         }
         else if (attribute.getName(null).equals("modified_terminal_separation")) {
            item.setName(null, ELogical.toString(armValue).toLowerCase());
            // For Package_planar_shape
         }
         else if (attribute.getName(null).equals("of_geometric_status")) {
            item.setName(null, EPredefined_geometric_status.toString(armValue).toLowerCase().replace('_', ' '));
         }
         else if (attribute.getName(null).equals("shape_purpose")) {
            if((attribute.getParent_entity(null).getName(null).equals("physical_unit_planar_shape_model"))||
            	(attribute.getParent_entity(null).getName(null).equals("part_template_planar_shape_model"))){
               item.setName(null, EPredefined_planar_purpose.toString(armValue).toLowerCase().replace('_', ' '));
            }
// Questionable - I think this should go away and only generic purpose should be supported            
//            else if (attribute.getParent_entity(null).getName(null).equals("physical_unit_planar_keepout_shape")) {
//               item.setName(null, EPredefined_keepout_purpose.toString(armValue).toLowerCase().replace('_', ' '));
//            }
//            else if (attribute.getParent_entity(null).getName(null).equals("physical_unit_3d_shape")) {
//            	item.setName(null, EPredefined_3d_purpose.toString(armValue).toLowerCase().replace('_', ' '));
//            }
            else if((attribute.getParent_entity(null).getName(null).equals("physical_unit_keepout_shape_model"))||
            		(attribute.getParent_entity(null).getName(null).equals("part_template_keepout_shape_model"))){
               item.setName(null, EPredefined_keepout_purpose.toString(armValue).toLowerCase().replace('_', ' '));
            }
            else {
               throw new SdaiException(SdaiException.FN_NAVL,
                                       " This function is not available for the attribute of type " +
                                       attribute.getParent_entity(null));
            }
         }
			else if (attribute.getName(null).equals("shape_extent")) {
			   item.setName(null, EShape_extent.toString(armValue).toLowerCase().replace('_', ' '));
			}
			else if (attribute.getName(null).equals("shape_approximation_level")) {
		   	item.setName(null, EShape_approximation_level.toString(armValue).toLowerCase().replace('_', ' '));
			}
			else if (attribute.getName(null).equals("shape_classification")) {
		   	item.setName(null, EShape_class.toString(armValue).toLowerCase().replace('_', ' '));
			}
			else if (attribute.getName(null).equals("mass_property_quality")) {
				String value = EMass_property_quality.toString(armValue).toLowerCase().replace('_', ' ');
				item.setName(null, value);
			}
            else if (attribute.getName(null).equals("component_application")) {
	            item.setName(null, EPu2ds_component_application.toString(armValue).toLowerCase().replace('_', ' '));
	        }
            else if (attribute.getName(null).equals("side")) {
	            item.setName(null, EPu2ds_shape_side.toString(armValue).toLowerCase().replace('_', ' '));
	        }
         	else
				throw new SdaiException(SdaiException.FN_NAVL, "Unsupported case - attribute: "+attribute.getName(null)+" for "+armEntity);
         item.setDescription(null, "");
         // R -> DRI
         ARepresentation_item items;
         if (suitableRepresentation.testItems(null)) {
            items = suitableRepresentation.getItems(null);
         }
         else {
            items = suitableRepresentation.createItems(null);
         }
         items.addUnordered(item);
      }

   }

   // SR <- RR -> R -> DRI - just remove DRI
   public static void unsetRepresentation_item(SdaiContext context, EShape_representation armEntity, String keyword,
                                               String[] list) throws SdaiException {
//      int result = MPhysical_unit_planar_shape.testShape_material_condition(context.domain, aimEntity);
//      if (result != 0) {
         ARepresentation_relationship relationships = new ARepresentation_relationship();
         CRepresentation_relationship.usedinRep_1(null, armEntity, context.domain, relationships);
         for (int i = 1; i <= relationships.getMemberCount(); i++) {
            jsdai.SRepresentation_schema.ERepresentation representation =
               relationships.getByIndex(i).getRep_2(null);
            if(!representation.getInstanceType().getName(null).equals("representation")){
            	continue;
            }
            if ( (representation.testName(null)) &&
                (representation.getName(null).equals(keyword))) {
               if (representation.testItems(null)) {
                  ARepresentation_item items = representation.getItems(null);
                  for (int j = 1; j <= items.getMemberCount(); ) {
                     ERepresentation_item item = items.getByIndex(j);
                     if ( (item instanceof EDescriptive_representation_item) &&
                         (item.testName(null)) &&
                         (CxAP210ARMUtilities.isStringInList(list, item.getName(null)))) {
                        item.deleteApplicationInstance();
                     }
                     else {
                        j++;
                     }
                  }
               }
            }
         }
//      }
   }


	/**
	 * physical_unit_planar_shape.shape_extent: shape_extent;
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 * According mapping
	 * attribute_mapping shape_extent (shape_extent
	 * , descriptive_representation_item);
	 *  shape_representation <= 
	 *  representation <- 
	 *  representation_relationship.rep_1 
	 *  representation_relationship 
	 *  representation_relationship.rep_2 -> 
	 *  representation 
	 *  {representation.name = `shape extent'} 
	 *  representation.items[i] -> 
	 *  representation_item 
	 *  {(representation_item.name = `envelope') 
	 *  (representation_item.name = `over body') 
	 *  (representation_item.name = `over lands') 
	 *  (representation_item.name = `over breakout')} 
	 *  representation_item => 
	 *  descriptive_representation_item 
	 * end_attribute_mapping;
	 */

	// SR <- RR -> R -> DRI
	public static void setShape_extent(SdaiContext context,
																	   EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
	   String[] list = {
		  "envelope",
		  "over body",
		  "over lands",
			"over breakout"};
	   String keyword = "shape extent";
	   setRepresentation_item(context, armEntity,
							  CPhysical_unit_planar_shape_model.attributeShape_extent(null),
							  keyword, list);
	}

	// SR <- RR -> R -> DRI - just remove DRI
	public static void unsetShape_extent(SdaiContext context,
																		 EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		String[] list = {
		   "envelope",
		   "over body",
		   "over lands",
			 "over breakout"};
	   String keyword = "shape extent";
	   unsetRepresentation_item(context, armEntity, keyword, list);
	}

	/**
	 * physical_unit_planar_shape.mass_property_quality: mass_property_quality;
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 * According mapping
	 * attribute_mapping mass_property_quality (mass_property_quality
	 * , descriptive_representation_item);
	 *  shape_representation <= 
	 *  representation <- 
	 *  representation_relationship.rep_1 
	 *  representation_relationship 
	 *  representation_relationship.rep_2 -> 
	 *  representation 
	 *  {representation.name = `mass property quality'} 
	 *  representation.items[i] -> 
	 *  representation_item 
	 *  {(representation_item.name = `high') 
	 *  (representation_item.name = `medium') 
	 *  (representation_item.name = `low') 
	 *  (representation_item.name = `unknown')} 
	 *  representation_item => 
	 *  descriptive_representation_item 
	 * end_attribute_mapping;
	 */

	// SR <- RR -> R -> DRI
	public static void setMass_property_quality(SdaiContext context,
																	   EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
	   String[] list = {
		  "high",
		  "medium",
		  "low",
			"is unknown"};
	   String keyword = "mass property quality";
	   setRepresentation_item(context, armEntity,
							  CPhysical_unit_planar_shape_model.attributeMass_property_quality(null),
							  keyword, list);
	}

	// SR <- RR -> R -> DRI - just remove DRI
	public static void unsetMass_property_quality(SdaiContext context,
																		 EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		String[] list = {
		   "high",
		   "medium",
		   "low",
			 "is unknown"};
		String keyword = "mass property quality";
	   unsetRepresentation_item(context, armEntity, keyword, list);
	}

	/**
	 * physical_unit_planar_shape.mass_property_quality: mass_property_quality;
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 * According mapping
	 * attribute_mapping shape_approximation_level (shape_approximation_level
	 * , descriptive_representation_item);
	 *  shape_representation <= 
	 *  representation <- 
	 *  representation_relationship.rep_1 
	 *  representation_relationship 
	 *  representation_relationship.rep_2 -> 
	 *  representation 
	 *  {representation.name = `shape approximation level'} 
	 *  representation.items[i] -> 
	 *  representation_item 
	 *  {(representation_item.name = `coarse') 
	 *  (representation_item.name = `detailed') 
	 *  (representation_item.name = `is unknown')} 
	 *  representation_item => 
	 *  descriptive_representation_item 
	 * end_attribute_mapping;
	 */

	// SR <- RR -> R -> DRI
	public static void setShape_approximation_level(SdaiContext context,
																	   EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
	   String[] list = {
		  "coarse",
		  "detailed",
		  "is unknown"};
	   String keyword = "shape approximation level";
	   setRepresentation_item(context, armEntity,
							  CPhysical_unit_planar_shape_model.attributeShape_approximation_level(null),
							  keyword, list);
	}

	// SR <- RR -> R -> DRI - just remove DRI
	public static void unsetShape_approximation_level(SdaiContext context,
																		 EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		String[] list = {
		   "coarse",
		   "detailed",
		   "is unknown"};
		String keyword = "shape approximation level";
	   unsetRepresentation_item(context, armEntity, keyword, list);
	}
	
}
