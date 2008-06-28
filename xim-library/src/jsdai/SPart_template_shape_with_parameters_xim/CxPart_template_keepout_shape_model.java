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

package jsdai.SPart_template_shape_with_parameters_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SPhysical_layout_template_mim.EPart_template_definition;
import jsdai.SPhysical_layout_template_xim.ETemplate_definition;
import jsdai.SPhysical_unit_shape_with_parameters_xim.CxPhysical_unit_shape_model;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SShape_parameters_mim.*;
import jsdai.SShape_parameters_xim.*;

public class CxPart_template_keepout_shape_model extends CPart_template_keepout_shape_model implements EMappedXIMEntity
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
		setShape_purpose(context, this);
		setConstrained_design_object_category(context, this);
		
      unsetShape_characterized_definition(null);
      unsetShape_material_condition(null);      
      unsetCentroid_location(null);      
      unsetShape_environment(null);      
      unsetShape_purpose(null);
      unsetConstrained_design_object_category(null);
		
      
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      unsetShape_characterized_definition(context, this);
		unsetShape_material_condition(context, this);      
		unsetCentroid_location(context, this);      
		unsetShape_environment(context, this);      
		unsetShape_purpose(context, this);
		unsetConstrained_design_object_category(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	*  representation &lt;-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  property_definition =&gt;
	*  product_definition_shape =&gt;
	*  part_template_definition}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// All the constraints are captured by setting attributes.
	// Some magic strings will come only at subtypes as this is ABSTRACT entity.
	public static void setMappingConstraints(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws SdaiException
	{
	}



   //********** "part_template_keepout_shape" attributes


  /**
   * Sets/creates data for constrained_design_object_category attribute.
   *
   * <p>
   *  attribute_mapping constrained_design_object_category (constrained_design_object_category
   * , keepout_design_object_category);
   * 	shape_representation &lt;=
   *  representation &lt;-
   *  property_definition_representation.used_representation\
   *  property_definition_representation
   *  property_definition_representation.definition -&gt;
   *  property_definition
   *  property_definition.definition -&gt;
   *  characterized_definition
   *  characterized_definition = characterized_object
   *  characterized_object =&gt;
   *  {characterized_object
   *  (characterized_object.description = 'assembly module assembly component category')
   *  (characterized_object.description = 'component feature category')
   *  (characterized_object.description = 'assembly ee material category')
   *  (characterized_object.description = 'interconnect ee material category')
   *  (characterized_object.description = 'interconnect module assembly component category')
   *  (characterized_object.description = 'via category')
   *  (characterized_object.description = 'inter stratum feature category')
   *  (characterized_object.description = 'cutout category')
   *  (characterized_object.description = 'fill area category')
   *  (characterized_object.description = 'laminate component category')
   *  (characterized_object.description = 'stratum feature category')}
   *  keepout_design_object_category
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- PDR -> PD -> KDOC
  public static void setConstrained_design_object_category(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
     //unset old values
     unsetConstrained_design_object_category(context, armEntity);

     if (armEntity.testConstrained_design_object_category(null)) {
        // Get value
        int armConstrained_design_object_category = armEntity.getConstrained_design_object_category(null);
        String description;
        if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "ASSEMBLY_MODULE_ASSEMBLY_COMPONENT")) {
           description = "assembly module assembly component category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "ASSEMBLY_MODULE_COMPONENT_FEATURE")) {
           description = "component feature category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "ASSEMBLY_EE_MATERIAL")) {
           description = "assembly ee material category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_EE_MATERIAL")) {
           description = "interconnect ee material category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_MODULE_ASSEMBLY_COMPONENT")) {
           description = "interconnect module assembly component category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_MODULE_VIA")) {
           description = "via category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_MODULE_INTER_STRATUM_FEATURE")) {
           description = "inter stratum feature category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_MODULE_CUTOUT")) {
           description = "cutout category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_MODULE_FILL_AREA")) {
           description = "fill area category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_MODULE_LAMINATE_COMPONENT")) {
           description = "laminate component category";
        }
        else if (EKeepout_product_design_object_category.toString(armConstrained_design_object_category).equals(
           "INTERCONNECT_MODULE_STRATUM_FEATURE")) {
           description = "stratum feature category";
        }
        else {
           throw new SdaiException(SdaiException.VA_NVLD,
                                   EKeepout_product_design_object_category.toString(armConstrained_design_object_category) +
                                   " Keepout category is not supported ");
        }

        EProperty_definition_representation epdr = (EProperty_definition_representation)
           context.working_model.createEntityInstance(CProperty_definition_representation.definition);
        // SR <- PDR
        epdr.setUsed_representation(null, armEntity);
        // KDOC
        LangUtils.Attribute_and_value_structure[] structure = {
           new LangUtils.Attribute_and_value_structure(
           CKeepout_design_object_category.attributeDescription(null), description)};
        EKeepout_design_object_category ekdoc = (EKeepout_design_object_category)
           LangUtils.createInstanceIfNeeded(context, CKeepout_design_object_category.definition, structure);
        if (!ekdoc.testName(null)) {
           ekdoc.setName(null, "");
           // PD -> KDOC
        }
        LangUtils.Attribute_and_value_structure[] pdStructure = {
           new LangUtils.Attribute_and_value_structure(
           CProperty_definition.attributeDefinition(null), ekdoc)};
        EProperty_definition epd = (CProperty_definition)
           LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
        if (!epd.testName(null)) {
           epd.setName(null, "");
           // PDR -> PD
        }
        epdr.setDefinition(null, epd);
     }
  }

  /**
   * Unsets/deletes data for constrained_design_object_category attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // remove all PDRs of that kind and KDOC if unused, SR <- PDR -> KDOC
  public static void unsetConstrained_design_object_category(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
     AProperty_definition_representation apdr = new AProperty_definition_representation();
     CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdr);
     for (int i = 1; i <= apdr.getMemberCount(); ) {
        EProperty_definition_representation epdr = apdr.getByIndex(i);
        if ( (epdr.testDefinition(null)) && (epdr.getDefinition(null)instanceof EKeepout_design_object_category)) {
           EKeepout_design_object_category ekdoc = (EKeepout_design_object_category) epdr.getDefinition(null);
           apdr.removeByIndex(i);
           epdr.deleteApplicationInstance();
           LangUtils.deleteInstanceIfUnusedRecursive(context.domain, ekdoc);
        }
        else {
           i++;
        }
     }
  }

  /**
   * Sets/creates data for shape_characterized_physical_unit attribute.
   *
   * <p>
   *  attribute_mapping shape_characterized_physical_unit_physical_unit (shape_characterized_physical_unit
   * , (*PATH*), physical_unit);
   * 	shape_representation &lt;=
   *  representation &lt;-
   *  property_definition_representation.used_representation
   *  property_definition_representation
   *  property_definition_representation.definition -&gt;
   *  property_definition =&gt;
   *  product_definition_shape =&gt;
   *  part_template_definition
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- PDR -> PropD -> PU
  public static void setShape_characterized_definition(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
     //unset old values
     unsetShape_characterized_definition(context, armEntity);

     if (armEntity.testShape_characterized_definition(null)) {
        SdaiModel modelAIM = context.working_model;
        ETemplate_definition armUnit = armEntity.getShape_characterized_definition(null);
        EShape_definition_representation epdr = (EShape_definition_representation) modelAIM.createEntityInstance(
           CShape_definition_representation.definition);
        epdr.setUsed_representation(null, armEntity);
        epdr.setDefinition(null, armUnit);
     }
  }

  /**
   * Unsets/deletes data for shape_characterized_physical_unit attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_characterized_definition(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
     //?? EPhysical_unit epu = armEntity.getShape_characterized_physical_unit(null);
     //?? EProduct_definition epd = (EProduct_definition)epu.getAimInstance();
     AProperty_definition_representation apdr = new AProperty_definition_representation();
     CProperty_definition_representation.usedinUsed_representation(null, armEntity, context.domain, apdr);
     for (int i = 1; i <= apdr.getMemberCount(); ) {
        EProperty_definition_representation epdr = apdr.getByIndex(i);
        if ( (epdr.testDefinition(null)) && (epdr.getDefinition(null)instanceof EPart_template_definition)) {
              apdr.removeByIndex(i);
              epdr.deleteApplicationInstance();
              continue;
        }
        i++;
     }
  }


  /**
   * Sets/creates data for shape_material_condition attribute.
   *
   * <p>
   *  attribute_mapping shape_material_condition (shape_material_condition
   * , descriptive_representation_item);
   * 	shape_representation <=
   * 	representation <-
   * 	representation_relationship.rep_1
   * 	representation_relationship
   * 	representation_relationship.rep_2 ->
   * 	representation
   * 	{representation.name = `shape material condition'}
   * 	representation.items[i] ->
   * 	representation_item
   * 	{(representation_item.name = `maximum material condition')
   * 	(representation_item.name = `minimum material condition')
   * 	(representation_item.name = `nominal material condition')}
   * 	representation_item =>
   * 	descriptive_representation_item
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_material_condition(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
     String[] list = {
        "maximum material condition",
        "minimum material condition",
        "nominal material condition"};
     String keyword = "shape material condition";
     CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
        CPart_template_keepout_shape_model.attributeShape_material_condition(null),
        keyword, list);
  }

  /**
   * Unsets/deletes data for shape_material_condition attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_material_condition(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
     String[] list = {
        "maximum material condition",
        "minimum material condition",
        "nominal material condition"};
     String keyword = "shape material condition";
     CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
  }

  /**
   * Sets/creates data for centroid_location attribute.
   *
   * <p>
   *  attribute_mapping centroid_location_cartesian_point (centroid_location
   * , (*PATH*), cartesian_point);
   * 	shape_representation
   * 	shape_representation <=
   * 	representation
   * 	representation.items[i] ->
   * 	representation_item
   * 	representation_item =>
   * 	geometric_representation_item =>
   * 	point =>
   * 	cartesian_point
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setCentroid_location(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
     //unset old values
//     unsetCentroid_location(context, armEntity);

     if (armEntity.testCentroid_location(null)) {
			throw new SdaiException(SdaiException.FN_NAVL," This setter is not implemented yet");
/*         jsdai.SProduct_property_representation_schema.EShape_representation aimEntity = (jsdai.
           SProduct_property_representation_schema.EShape_representation) armEntity.getTemp("AIM");
        jsdai.SGeometry_schema.ECartesian_point armCentroid_location = (jsdai.SGeometry_schema.ECartesian_point) armEntity.
           getCentroid_location(null);

*/      }
  }

  /**
   * Unsets/deletes data for centroid_location attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetCentroid_location(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL," This unsetter is not implemented yet");
//     jsdai.SProduct_property_representation_schema.EShape_representation aimEntity = (jsdai.
//        SProduct_property_representation_schema.EShape_representation) armEntity.getTemp("AIM");
  }

  /**
   * Sets/creates data for shape_environment attribute.
   *
   * <p>
   *  attribute_mapping shape_environment (shape_environment
   * , descriptive_representation_item);
   * 	shape_representation <=
   * 	representation <-
   * 	representation_relationship.rep_1
   * 	representation_relationship
   * 	representation_relationship.rep_2 ->
   * 	representation
   * 	{representation.name = `shape environment'}
   * 	representation.items[i] ->
   * 	representation_item
   * 	{(representation_item.name = `manufacturing')
   * 	(representation_item.name = `end user application')}
   * 	representation_item =>
   * 	descriptive_representation_item
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_environment(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
     String[] list = {
        "manufacturing",
        "end user application"};
     String keyword = "shape environment";
     CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
        CPart_template_keepout_shape_model.attributeShape_environment(null),
        keyword, list);
  }

  /**
   * Unsets/deletes data for shape_environment attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_environment(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
     String[] list = {
        "manufacturing",
        "end user application"};
     String keyword = "shape environment";
     CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
  }

  /**
   * Sets/creates data for shape_purpose attribute.
   *
   * <p>
   *  attribute_mapping shape_purpose_predefined_planar_purpose (shape_purpose
   * , descriptive_representation_item, predefined_planar_purpose);
   * 	shape_representation <=
   * 	representation <-
   * 	representation_relationship.rep_1
   * 	representation_relationship
   * 	representation_relationship.rep_2 ->
   * 	representation
   * 	{representation.name = `predefined keepout shape purpose'}
   * 	representation.items[i] ->
   * 	representation_item
   * 	{(representation_item.name = `thermal')
   * 	(representation_item.name = `generic clearance')
   * 	(representation_item.name = `shock')
   * 	(representation_item.name = `vibration')
   * 	(representation_item.name = `electromagnetic compatibility')}
   * 	representation_item =>
   * 	descriptive_representation_item
   *  end_attribute_mapping;
   *  attribute_mapping shape_purpose_external_definition (shape_purpose
   * , (*PATH*), external_definition);
   * 	shape_representation <=
   * 	representation <-
   * 	representation_relationship.rep_1
   * 	representation_relationship
   * 	representation_relationship.rep_2 ->
   * 	representation
   * 	{representation.name = `externally defined shape purpose'}
   * 	representation.items[i] ->
   * 	representation_item =>
   * 	externally_defined_representation_item =>
   * 	external_definition
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_purpose(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
     String[] list = {
        "thermal",
        "generic clearance",
        "shock",
        "vibration",
        "electromagnetic compatibility"};
     String keyword = "predefined keepout shape purpose";
     if (armEntity.testShape_purpose(null)) { // BOOLEAN
        CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
           CPart_template_keepout_shape_model.attributeShape_purpose(null),
           keyword, list);
     }
     // ELSE NOT IMPLEMENTED the case for EXTERNAL_DEFINITION
  }

  /**
   * Unsets/deletes data for shape_purpose attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_purpose(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
     String[] list = {
        "thermal",
        "generic clearance",
        "shock",
        "vibration",
        "electromagnetic compatibility"};
     String keyword = "predefined keepout shape purpose";
     CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
  }

	
}
