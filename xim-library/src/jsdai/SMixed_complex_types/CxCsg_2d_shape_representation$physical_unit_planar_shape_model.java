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
import jsdai.SConstructive_solid_geometry_2d_mim.CCsg_2d_shape_representation;
import jsdai.SPhysical_unit_2d_shape_xim.*;
import jsdai.SPhysical_unit_shape_with_parameters_xim.*;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxCsg_2d_shape_representation$physical_unit_planar_shape_model extends CCsg_2d_shape_representation$physical_unit_planar_shape_model implements EMappedXIMEntity
{

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a10);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a10);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a10 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a10 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a10$;
	}

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CCsg_2d_shape_representation.definition);

		setMappingConstraints(context, this);

      setShape_characterized_definition(context, this);
		setShape_material_condition(context, this);      
		setCentroid_location(context, this);      
		setShape_environment(context, this);      
		setShape_extent(context, this);      
		setShape_approximation_level(context, this);
		setMass_property_quality(context, this);
		// PUPS
      setShape_distance_from_seating_plane(context, this);
		setShape_location_with_respect_to_seating_plane(context, this);
		setShape_purpose(context, this);

      // clean ARM
		
      unsetShape_characterized_definition(null);
      unsetShape_material_condition(null);      
      unsetCentroid_location(null);      
      unsetShape_environment(null);      
      unsetShape_extent(null);      
      unsetShape_approximation_level(null);
      unsetMass_property_quality(null);

      unsetShape_distance_from_seating_plane(null);
		unsetShape_location_with_respect_to_seating_plane(null);
		unsetShape_purpose(null);

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
		// PUPS
      unsetShape_distance_from_seating_plane(context, this);
		unsetShape_location_with_respect_to_seating_plane(context, this);
		unsetShape_purpose(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CCsg_2d_shape_representation$physical_unit_planar_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPhysical_unit_planar_shape_model.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CCsg_2d_shape_representation$physical_unit_planar_shape_model armEntity) throws SdaiException
	{
		CxPhysical_unit_planar_shape_model.unsetMappingConstraints(context, armEntity);
	}


	// Physical_unit_shape
	public static void setShape_characterized_definition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.setShape_characterized_definition(context, armEntity);		
	}
	
	public static void unsetShape_characterized_definition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.unsetShape_characterized_definition(context, armEntity);		
	}

	public static void setShape_material_condition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.setShape_material_condition(context, armEntity);		
	}
	
	public static void unsetShape_material_condition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.unsetShape_material_condition(context, armEntity);		
	}

	
	public static void setCentroid_location(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.setCentroid_location(context, armEntity);		
	}

	public static void unsetCentroid_location(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.unsetCentroid_location(context, armEntity);		
	}

	public static void setShape_environment(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.setShape_environment(context, armEntity);		
	}

	public static void unsetShape_environment(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.unsetShape_environment(context, armEntity);		
	}
	

	public static void setShape_extent(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.setShape_extent(context, armEntity);		
	}
      
	public static void unsetShape_extent(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.unsetShape_extent(context, armEntity);		
	}


	public static void setShape_approximation_level(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.setShape_approximation_level(context, armEntity);		
	}

	public static void unsetShape_approximation_level(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.unsetShape_approximation_level(context, armEntity);		
	}

	public static void setMass_property_quality(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.setMass_property_quality(context, armEntity);		
	}

	public static void unsetMass_property_quality(SdaiContext context, EPhysical_unit_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_shape_model.unsetMass_property_quality(context, armEntity);		
	}

	// PUPS
	public static void setShape_distance_from_seating_plane(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_planar_shape_model.setShape_distance_from_seating_plane(context, armEntity);		
	}

	public static void unsetShape_distance_from_seating_plane(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_planar_shape_model.unsetShape_distance_from_seating_plane(context, armEntity);		
	}

	
	public static void setShape_location_with_respect_to_seating_plane(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_planar_shape_model.setShape_location_with_respect_to_seating_plane(context, armEntity);		
	}

	public static void unsetShape_location_with_respect_to_seating_plane(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_planar_shape_model.unsetShape_location_with_respect_to_seating_plane(context, armEntity);		
	}
	
	public static void setShape_purpose(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_planar_shape_model.setShape_purpose(context, armEntity);		
	}
	
	public static void unsetShape_purpose(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException {
		CxPhysical_unit_planar_shape_model.unsetShape_purpose(context, armEntity);		
	}
	
}
