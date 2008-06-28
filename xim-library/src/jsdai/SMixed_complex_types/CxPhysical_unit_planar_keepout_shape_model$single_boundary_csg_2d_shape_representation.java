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

package jsdai.SMixed_complex_types;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConstructive_solid_geometry_2d_mim.CSingle_boundary_csg_2d_shape_representation;
import jsdai.SNon_feature_shape_element_xim.*;
import jsdai.SPhysical_unit_2d_shape_xim.*;
import jsdai.SPhysical_unit_shape_with_parameters_xim.*;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxPhysical_unit_planar_keepout_shape_model$single_boundary_csg_2d_shape_representation extends CPhysical_unit_planar_keepout_shape_model$single_boundary_csg_2d_shape_representation implements EMappedXIMEntity
{

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a15);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a15);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a15 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a15 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a15$;
	}

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CSingle_boundary_csg_2d_shape_representation.definition);

		setMappingConstraints(context, this);

		// Non_feature_shape_definition
		//  associated_element
		setAssociated_element(context, this);
      //  model_shape
		setModel_shape(context, this);
		
		// Physical_unit_keepout_shape
      //  constrained_design_object_category
		setConstrained_design_object_category(context, this);
		
      //  shape_characterized_physical_unit
		setShape_characterized_definition(context, this);
		
      //  shape_material_condition
		setShape_material_condition(context, this);
		
      //  shape_purpose
		setShape_purpose(context, this);
		
      //  shape_environment
		setShape_environment(context, this);
		
      //  shape_extent
		setShape_extent(context, this);
		
      //  shape_approximation_level
		setShape_approximation_level(context, this);
		
      //  centroid_location
		setCentroid_location(context, this);
		
      //  shape_distance_from_seating_plane
		setShape_distance_from_seating_plane(context, this);
		
      //  shape_location_with_respect_to_seating_plane
		setShape_location_with_respect_to_seating_plane(context, this);
		
		setComponent_application(context, this);		
		// clean ARM
		// Non_feature_shape_definition
		//  associated_element
		unsetAssociated_element(null);
      //  model_shape
		unsetModel_shape(null);
		
		// Physical_unit_keepout_shape
      //  constrained_design_object_category
		unsetConstrained_design_object_category(null);
		
      //  shape_characterized_physical_unit
		unsetShape_characterized_definition(null);
		
      //  shape_material_condition
		unsetShape_material_condition(null);
		
      //  shape_purpose
		unsetShape_purpose(null);
		
      //  shape_environment
		unsetShape_environment(null);
		
      //  shape_extent
		unsetShape_extent(null);
		
      //  shape_approximation_level
		unsetShape_approximation_level(null);
		
      //  centroid_location
		unsetCentroid_location(null);
		
      //  shape_distance_from_seating_plane
		unsetShape_distance_from_seating_plane(null);
		
      //  shape_location_with_respect_to_seating_plane
		unsetShape_location_with_respect_to_seating_plane(null);
		
		unsetComponent_application(null);		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// Non_feature_shape_definition
		//  associated_element
		unsetAssociated_element(context, this);
      //  model_shape
		unsetModel_shape(context, this);
		
		// Physical_unit_keepout_shape
      //  constrained_design_object_category
		unsetConstrained_design_object_category(context, this);
		
      //  shape_characterized_physical_unit
		unsetShape_characterized_definition(context, this);
		
      //  shape_material_condition
		unsetShape_material_condition(context, this);
		
      //  shape_purpose
		unsetShape_purpose(context, this);
		
      //  shape_environment
		unsetShape_environment(context, this);
		
      //  shape_extent
		unsetShape_extent(context, this);
		
      //  shape_approximation_level
		unsetShape_approximation_level(context, this);
		
      //  centroid_location
		unsetCentroid_location(context, this);
		
      //  shape_distance_from_seating_plane
		unsetShape_distance_from_seating_plane(context, this);
		
      //  shape_location_with_respect_to_seating_plane
		unsetShape_location_with_respect_to_seating_plane(context, this);
		
		unsetComponent_application(context, this);		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CPhysical_unit_planar_keepout_shape_model$single_boundary_csg_2d_shape_representation armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPhysical_unit_planar_keepout_shape_model.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CPhysical_unit_planar_keepout_shape_model$single_boundary_csg_2d_shape_representation armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_keepout_shape_model.unsetMappingConstraints(context, armEntity);
	}

	// Non_feature_shape_definition
	//  associated_element
	public static void setAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException
	{
		CxNon_feature_shape_model.setAssociated_element(context, armEntity);
	}

	public static void unsetAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException
	{
		CxNon_feature_shape_model.unsetAssociated_element(context, armEntity);
	}
	

   //  model_shape
	public static void setModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException
	{
		CxNon_feature_shape_model.setModel_shape(context, armEntity);
	}
	
	public static void unsetModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException
	{
		CxNon_feature_shape_model.unsetModel_shape(context, armEntity);
	}
	

	// Physical_unit_keepout_shape
   //  constrained_design_object_category
	public static void setConstrained_design_object_category(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setConstrained_design_object_category(context, armEntity);
	}
	
	public static void unsetConstrained_design_object_category(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetConstrained_design_object_category(context, armEntity);
	}
	
   //  shape_characterized_physical_unit
	public static void setShape_characterized_definition(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setShape_characterized_definition(context, armEntity);
	}
	
	public static void unsetShape_characterized_definition(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetShape_characterized_definition(context, armEntity);
	}
	
   //  shape_material_condition
	public static void setShape_material_condition(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setShape_material_condition(context, armEntity);
	}
	
	public static void unsetShape_material_condition(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetShape_material_condition(context, armEntity);
	}
	
   //  shape_purpose
	public static void setShape_purpose(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setShape_purpose(context, armEntity);
	}
	
	public static void unsetShape_purpose(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetShape_purpose(context, armEntity);
	}
	
   //  shape_environment
	public static void setShape_environment(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setShape_environment(context, armEntity);
	}
	
	public static void unsetShape_environment(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetShape_environment(context, armEntity);
	}
	
   //  shape_extent
	public static void setShape_extent(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setShape_extent(context, armEntity);
	}
	
	public static void unsetShape_extent(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetShape_extent(context, armEntity);
	}
	
   //  shape_approximation_level
	public static void setShape_approximation_level(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setShape_approximation_level(context, armEntity);
	}
	
	public static void unsetShape_approximation_level(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetShape_approximation_level(context, armEntity);
	}
	
   //  centroid_location
	public static void setCentroid_location(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.setCentroid_location(context, armEntity);
	}
	
	public static void unsetCentroid_location(SdaiContext context, EPhysical_unit_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_keepout_shape_model.unsetCentroid_location(context, armEntity);
	}
	
	// Physical_unit_planar_keepout_shape
   //  shape_distance_from_seating_plane
	public static void setShape_distance_from_seating_plane(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_keepout_shape_model.setShape_distance_from_seating_plane(context, armEntity);
	}
	
	public static void unsetShape_distance_from_seating_plane(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_keepout_shape_model.unsetShape_distance_from_seating_plane(context, armEntity);
	}
	
   //  shape_location_with_respect_to_seating_plane
	public static void setShape_location_with_respect_to_seating_plane(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_keepout_shape_model.setShape_location_with_respect_to_seating_plane(context, armEntity);
	}
	
	public static void unsetShape_location_with_respect_to_seating_plane(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_keepout_shape_model.unsetShape_location_with_respect_to_seating_plane(context, armEntity);
	}
	
	   //  component_application
	public static void setComponent_application(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_keepout_shape_model.setComponent_application(context, armEntity);
	}
	
	public static void unsetComponent_application(SdaiContext context, EPhysical_unit_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_keepout_shape_model.unsetComponent_application(context, armEntity);
	}
	
}
