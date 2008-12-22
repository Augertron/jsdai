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

package jsdai.SPhysical_unit_2d_shape_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_2d_shape_xim.*;
import jsdai.SNon_feature_shape_element_xim.*;
import jsdai.SPhysical_unit_shape_with_parameters_xim.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_relationship;

public class CxPhysical_unit_planar_keepout_shape_model extends CPhysical_unit_planar_keepout_shape_model implements EMappedXIMEntity
{

	// Taken from CRepresentation.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a0$;
	}

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

		setMappingConstraints(context, this);
      setShape_characterized_definition(context, this);
		setShape_material_condition(context, this);      
		setCentroid_location(context, this);      
		setShape_environment(context, this);      
		setShape_extent(context, this);      
		setShape_approximation_level(context, this);
		setShape_purpose(context, this);
		setConstrained_design_object_category(context, this);
      setAssociated_element(context, this);
      setModel_shape(context, this);
      setSide(context, this);
		// PUPKS
      setShape_distance_from_seating_plane(context, this);
		setShape_location_with_respect_to_seating_plane(context, this);
		setComponent_application(context, this);
		
		
      unsetShape_characterized_definition(null);
      unsetShape_material_condition(null);      
      unsetCentroid_location(null);      
      unsetShape_environment(null);      
      unsetShape_extent(null);      
      unsetShape_approximation_level(null);
      unsetShape_purpose(null);
      unsetConstrained_design_object_category(null);

      unsetShape_distance_from_seating_plane(null);
		unsetShape_location_with_respect_to_seating_plane(null);
      
      unsetAssociated_element(null);
      unsetModel_shape(null);
      unsetComponent_application(null);
      unsetSide(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      unsetShape_characterized_definition(context, this);
      unsetShape_material_condition(context, this);      
      unsetCentroid_location(context, this);      
      unsetShape_environment(context, this);      
      unsetShape_extent(context, this);      
      unsetShape_approximation_level(context, this);
      unsetShape_purpose(context, this);
      unsetConstrained_design_object_category(context, this);

      unsetShape_distance_from_seating_plane(context, this);
		unsetShape_location_with_respect_to_seating_plane(context, this);
		
      unsetAssociated_element(context, null);
      unsetModel_shape(context, null);
      unsetComponent_application(context, this);
      unsetSide(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	shape_representation &lt;=
	* 	representation
	* 	{[representation.id = 'nfsd']
	* 	[representation.name = 'pupks']
	* 	[(representation.description = 'pupks')]
	* 	[representation &lt;-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition -&gt;
	* 	property_definition
	* 	property_definition.definition -&gt;
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition =&gt;
	* 	physical_unit]}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// All the constraints are captured by setting attributes.
	// Some magic strings will come only at subtypes as this is ABSTRACT entity.
	public static void setMappingConstraints(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPlanar_shape_model.setMappingConstraints(context, armEntity);
		CxPhysical_unit_keepout_shape_model.setMappingConstraints(context, armEntity);
		CxUsage_view_level_non_feature_shape_model.setMappingConstraints(context, armEntity);

		armEntity.setName(null, "pupksm");
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "pupksm");
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPlanar_shape_model.unsetMappingConstraints(context, armEntity);
		CxPhysical_unit_keepout_shape_model.unsetMappingConstraints(context, armEntity);
		CxUsage_view_level_non_feature_shape_model.unsetMappingConstraints(context, armEntity);

      armEntity.unsetName(null);
		CxAP210ARMUtilities.unsetDerviedDescription(context, armEntity);
	}



   //********** "physical_unit_keepout_shape" attributes


  /**
   * Sets/creates data for constrained_design_object_category attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- PDR -> PD -> KDOC
  public static void setConstrained_design_object_category(SdaiContext context,
     EPhysical_unit_keepout_shape_model armEntity) throws SdaiException {
  		CxPhysical_unit_keepout_shape_model.setConstrained_design_object_category(context, armEntity);
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
     EPhysical_unit_keepout_shape_model armEntity) throws SdaiException {
  	CxPhysical_unit_keepout_shape_model.unsetConstrained_design_object_category(context, armEntity);
  }

  /**
   * Sets/creates data for shape_characterized_physical_unit attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- PDR -> PropD -> PU
  public static void setShape_characterized_definition(SdaiContext context,
     EPhysical_unit_keepout_shape_model armEntity) throws SdaiException {
  	CxPhysical_unit_keepout_shape_model.setShape_characterized_definition(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for shape_characterized_physical_unit attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_characterized_definition(SdaiContext context,
     EPhysical_unit_keepout_shape_model armEntity) throws SdaiException {
  		CxPhysical_unit_keepout_shape_model.unsetShape_characterized_definition(context, armEntity);  	
  }


  /**
   * Sets/creates data for shape_location_with_respect_to_seating_plane attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_location_with_respect_to_seating_plane(SdaiContext context,
     EPhysical_unit_keepout_shape_model armEntity) throws SdaiException {
  		CxPhysical_unit_keepout_shape_model.setShape_location_with_respect_to_seating_plane(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for shape_location_with_respect_to_seating_plane attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_location_with_respect_to_seating_plane(SdaiContext context,
     EPhysical_unit_keepout_shape_model armEntity) throws SdaiException {
  		CxPhysical_unit_keepout_shape_model.unsetShape_location_with_respect_to_seating_plane(context, armEntity);
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
  public static void setShape_material_condition(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.setShape_material_condition(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for shape_material_condition attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_material_condition(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.unsetShape_material_condition(context, armEntity);  	
  }

  /**
   * Sets/creates data for centroid_location attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setCentroid_location(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.setCentroid_location(context, armEntity);
  }

  /**
   * Unsets/deletes data for centroid_location attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetCentroid_location(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.unsetCentroid_location(context, armEntity);
  }

  /**
   * Sets/creates data for shape_environment attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_environment(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.setShape_environment(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for shape_environment attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_environment(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.unsetShape_environment(context, armEntity);  	
  }

  /**
   * Sets/creates data for shape_purpose attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_purpose(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.setShape_purpose(context, armEntity);
  }

  /**
   * Unsets/deletes data for shape_purpose attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_purpose(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPhysical_unit_keepout_shape_model.unsetShape_purpose(context, armEntity);  	
  }

  /**
	* Sets/creates data for shape_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  public static void setShape_extent(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
	  SdaiException {
  		CxPhysical_unit_keepout_shape_model.setShape_extent(context, armEntity);  	
  }

  /**
	* Unsets/deletes data for shape_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  public static void unsetShape_extent(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
	  SdaiException {
  	CxPhysical_unit_keepout_shape_model.unsetShape_extent(context, armEntity);  	
  }

  /**
	* Sets/creates data for shape_approximation_level attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  public static void setShape_approximation_level(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
	  SdaiException {
  		CxPhysical_unit_keepout_shape_model.setShape_approximation_level(context, armEntity);  	
  }

  /**
	* Unsets/deletes data for shape_approximation_level attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  public static void unsetShape_approximation_level(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws
	  SdaiException {
  		CxPhysical_unit_keepout_shape_model.unsetShape_approximation_level(context, armEntity);  	
  }

  // NFSD

  /**
	* Sets/creates data for Associated_element attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 public static void setAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws
	  SdaiException {
 	CxNon_feature_shape_model.setAssociated_element(context, armEntity);  	
 }

 /**
	* Unsets/deletes data for shape_approximation_level attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 public static void unsetAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws
	  SdaiException {
 	CxNon_feature_shape_model.setAssociated_element(context, armEntity);  	
 }

 /**
	* Sets/creates data for Associated_element attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
public static void setModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws
	  SdaiException {
	CxNon_feature_shape_model.setModel_shape(context, armEntity);  	
}

/**
	* Unsets/deletes data for Model_shape attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
public static void unsetModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws
	  SdaiException {
	CxNon_feature_shape_model.setModel_shape(context, armEntity);  	
}
 
  
  /**
   * Sets/creates data for shape_distance_from_seating_plane attribute.
   *
   * <p>
     attribute_mapping shape_distance_from_seating_plane(shape_distance_from_seating_plane, $PATH, Length_tolerance_characteristic);
		shape_representation <=
		representation <-
		representation_relationship.rep_1
		representation_relationship
		{representation_relationship
		representation_relationship.name = 'shape distance from seating plane'}
		representation_relationship.rep_2 ->
		representation
     end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_distance_from_seating_plane(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
     //unset old values
//     unsetShape_distance_from_seating_plane(context, armEntity);
     if (armEntity.testShape_distance_from_seating_plane(null)) {
         ERepresentation aimEntity = armEntity.getShape_distance_from_seating_plane(null);
         ERepresentation_relationship err = (ERepresentation_relationship)
         	context.working_model.createEntityInstance(CRepresentation_relationship.definition);
         err.setRep_1(null, armEntity);
         err.setRep_2(null, aimEntity);
         err.setName(null, "shape distance from seating plane");
      }
  }

  /**
   * Unsets/deletes data for shape_distance_from_seating_plane attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_distance_from_seating_plane(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
	  ARepresentation_relationship arr = new ARepresentation_relationship();
	  CRepresentation_relationship.usedinRep_1(null, armEntity, context.domain, arr);
	  SdaiIterator iter = arr.createIterator();
	  while(iter.next()){
		  ERepresentation_relationship err = arr.getCurrentMember(iter);
		  if((err.testName(null))&&(err.getName(null).equals("shape distance from seating plane"))){
			  err.deleteApplicationInstance();
		  }
	  }
  }

  /**
   * Sets/creates data for shape_location_with_respect_to_seating_plane attribute.
   *
   * <p>
   *  attribute_mapping shape_location_with_respect_to_seating_plane (shape_location_with_respect_to_seating_plane
   * , descriptive_representation_item);
   * 	shape_representation <=
   * 	representation <-
   * 	representation_relationship.rep_1
   * 	representation_relationship
   * 	representation_relationship.rep_2 ->
   * 	representation
   * 	{representation.name = `shape location'}
   * 	representation.items[i] ->
   * 	representation_item
   * 	{(representation_item.name = `above')
   * 	(representation_item.name = `congruent')
   * 	(representation_item.name = `below')}
   * 	representation_item =>
   * 	descriptive_representation_item
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_location_with_respect_to_seating_plane(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
     //unset is done inside this method
     String[] list = {
        "above",
        "congruent",
        "below"};
     String keyword = "shape location";
     CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
        CPhysical_unit_planar_keepout_shape_model.attributeShape_location_with_respect_to_seating_plane(null),
        keyword, list);
  }

  /**
   * Unsets/deletes data for shape_location_with_respect_to_seating_plane attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_location_with_respect_to_seating_plane(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
     String[] list = {
        "above",
        "congruent",
        "below"};
     String keyword = "shape location";
     CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
  }


  /**
   * Sets/creates data for shape_location_with_respect_to_seating_plane attribute.
   *
   * <p>
		attribute_mapping component_application(component_application, descriptive_representation_item);
			shape_representation <=
			representation <-
			representation_relationship.rep_1 
			representation_relationship 
			representation_relationship.rep_2 -> 
			representation 
			{representation.name = 'component application'}
			representation.items[i] ->
			representation_item
			{(representation_item.name = 'compliant components permitted')
			(representation_item.name = 'no components permitted')}
			representation_item =>
			descriptive_representation_item

		end_attribute_mapping;

   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setComponent_application(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
     //unset is done inside this method
     String[] list = {
        "compliant components permitted",
        "no components permitted"};
     String keyword = "component application";
     CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
        CPhysical_unit_planar_keepout_shape_model.attributeComponent_application(null),
        keyword, list);
  }

  /**
   * Unsets/deletes data for shape_location_with_respect_to_seating_plane attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetComponent_application(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
     String[] list = {
        "compliant components permitted",
        "no components permitted"};
     String keyword = "component application";
     CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
  }
  
  /**
   * Sets/creates data for side attribute.
   *
   * <p>
	attribute_mapping side(side, descriptive_representation_item);
		shape_representation <=
		representation <-
		representation_relationship.rep_1 
		representation_relationship 
		representation_relationship.rep_2 -> 
		representation 
		{representation.name = 'side'}
		representation.items[i] ->
		representation_item
		{(representation_item.name = 'same side')
		(representation_item.name = 'opposite side')
		(representation_item.name = 'both sides')}
		representation_item =>
		descriptive_representation_item
	end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setSide(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
     //unset is done inside this method
     String[] list = {
    	        "same side",
    	        "opposite side",
    	        "both sides"};
     String keyword = "side";
     CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
        CPhysical_unit_planar_keepout_shape_model.attributeSide(null),
        keyword, list);
  }

  /**
   * Unsets/deletes data for side attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetSide(SdaiContext context,
     EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException {
     String[] list = {
 	        "same side",
	        "opposite side",
	        "both sides"};
     String keyword = "side";
     CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
  }
  
  
	
}
