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
import jsdai.SLayered_2d_shape_xim.CxPlanar_projected_shape_model;
import jsdai.SPhysical_unit_shape_with_parameters_xim.CxPhysical_unit_shape_model;
import jsdai.SPhysical_unit_shape_with_parameters_xim.EPhysical_unit_shape_model;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_relationship;

public class CxPhysical_unit_planar_shape_model extends CPhysical_unit_planar_shape_model implements EMappedXIMEntity
{

	// FROM CRepresentation.java
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
		setMass_property_quality(context, this);
		// PUPS
      setShape_distance_from_seating_plane(context, this);
		setShape_location_with_respect_to_seating_plane(context, this);
		setShape_purpose(context, this);
		
		
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
	* <p>
	* mapping_constraints;
	*  shape_representation &lt;=
	*  representation
	*  {[representation.name = 'planar projected shape']
	*  [representation.description = 'pups']
	*  [representation &lt;-
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
	*  physical_unit]}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// All the constraints are captured by setting attributes
	public static void setMappingConstraints(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
   	CxPlanar_projected_shape_model.setMappingConstraints(context, armEntity);
   	CxPhysical_unit_shape_model.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "pupsm");
	}

	public static void unsetMappingConstraints(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException
	{
   	CxPlanar_projected_shape_model.unsetMappingConstraints(context, armEntity);
   	CxPhysical_unit_shape_model.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.unsetDerviedDescription(context, armEntity);
	}



   /**
    * physical_unit_planar_shape.shape_characterized_physical_unit: LIST [1:2] OF UNIQUE physical_unit;
    * @param context
    * @param armEntity
    * @throws SdaiException
    */
   // SR <- PDR -> PropD -> PU
   public static void setShape_characterized_definition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.setShape_characterized_definition(context, armEntity);
   }

   public static void unsetShape_characterized_definition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.unsetShape_characterized_definition(context, armEntity);
   }

   /**
    * physical_unit_planar_shape.centroid_location: OPTIONAL cartesian_point;
    * @param context
    * @param armEntity
    * @throws SdaiException
    */
   // SR <- RR -> R -> DRI
   public static void setShape_material_condition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.setShape_material_condition(context, armEntity);
   }

   // SR <- RR -> R -> DRI - just remove DRI
   public static void unsetShape_material_condition(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.unsetShape_material_condition(context, armEntity);
   }

   /**
    * physical_unit_planar_shape.shape_environment: environment;
    * @param context
    * @param armEntity
    * @throws SdaiException
    */
   // SR <- RR -> R -> DRI
   public static void setShape_environment(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.setShape_environment(context, armEntity);
   }

   // SR <- RR -> R -> DRI - just remove DRI
   public static void unsetShape_environment(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.unsetShape_environment(context, armEntity);
   }

   /**
    * physical_unit_planar_shape.shape_material_condition: material_condition;
    * @param context
    * @param armEntity
    * @throws SdaiException
    */
   // R -> CP
   public static void setCentroid_location(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.setCentroid_location(context, armEntity);
   }

   // R -> CP
   public static void unsetCentroid_location(SdaiContext context, EPhysical_unit_shape_model armEntity) throws
      SdaiException {
   	CxPhysical_unit_shape_model.unsetCentroid_location(context, armEntity);
   }

	/**
	 * physical_unit_planar_shape.shape_extent: shape_extent;
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */

	// SR <- RR -> R -> DRI
	public static void setShape_extent(SdaiContext context,
																	   EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		CxPhysical_unit_shape_model.setShape_extent(context, armEntity);
	}

	// SR <- RR -> R -> DRI - just remove DRI
	public static void unsetShape_extent(SdaiContext context,
																		 EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		CxPhysical_unit_shape_model.unsetShape_extent(context, armEntity);
	}

	/**
	 * physical_unit_planar_shape.mass_property_quality: mass_property_quality;
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */

	// SR <- RR -> R -> DRI
	public static void setMass_property_quality(SdaiContext context,
																	   EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		CxPhysical_unit_shape_model.setMass_property_quality(context, armEntity);	
	}

	// SR <- RR -> R -> DRI - just remove DRI
	public static void unsetMass_property_quality(SdaiContext context,
																		 EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		CxPhysical_unit_shape_model.unsetMass_property_quality(context, armEntity);
	}

	/**
	 * physical_unit_planar_shape.mass_property_quality: mass_property_quality;
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */

	// SR <- RR -> R -> DRI
	public static void setShape_approximation_level(SdaiContext context,
																	   EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		CxPhysical_unit_shape_model.setShape_approximation_level(context, armEntity);		
	}

	// SR <- RR -> R -> DRI - just remove DRI
	public static void unsetShape_approximation_level(SdaiContext context,
																		 EPhysical_unit_shape_model armEntity) throws
	   SdaiException {
		CxPhysical_unit_shape_model.unsetShape_approximation_level(context, armEntity);
	}
	
	// Physical_unit_planar_shape
   // New since WD26
   /**
    * physical_unit_planar_shape.shape_distance_from_seating_plane: OPTIONAL length_data_element;
    * @param context
    * @param armEntity
    * @throws SdaiException
    * According the mapping:
     shape_representation <=
     representation <-
     representation_relationship.rep_1
     representation_relationship
     representation_relationship.rep_2 ->
     representation
     {representation.name = `shape location'}
     representation.items[i] ->
     representation_item
     {representation_item.name = `shape distance from seating plane'}
     representation_item =>
     measure_representation_item <=
     measure_with_unit =>
     length_measure_with_unit
    */
   public static void setShape_distance_from_seating_plane(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws
      SdaiException {
		if(armEntity.testShape_distance_from_seating_plane(null)){
	         ERepresentation aimEntity = armEntity.getShape_distance_from_seating_plane(null);
	         ERepresentation_relationship err = (ERepresentation_relationship)
	         	context.working_model.createEntityInstance(CRepresentation_relationship.definition);
	         err.setRep_1(null, armEntity);
	         err.setRep_2(null, aimEntity);
	         err.setName(null, "shape distance from seating plane");
		}
   }

   /**
       * Unsets/deletes data for physical_unit_planar_shape.shape_distance_from_seating_plane: OPTIONAL length_data_element;
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetShape_distance_from_seating_plane(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws
      SdaiException {
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
    * physical_unit_planar_shape.shape_purpose: purpose;
    * @param context
    * @param armEntity
    * @throws SdaiException
    * According mapping
    * 	shape_representation <=
    *    representation <-
    *    representation_relationship.rep_1
    *    representation_relationship
    *    representation_relationship.rep_2 ->
    *    representation
    *    {representation.name = `shape location'}
    *    representation.items[i] ->
    *    representation_item
    *    {(representation_item.name = `above')
    *     (representation_item.name = `congruent')
    *     (representation_item.name = `below')}
    *     representation_item =>
    *     descriptive_representation_item
    */

   // SR <- RR -> R -> DRI
   public static void setShape_location_with_respect_to_seating_plane(SdaiContext context,
                                                                      EPhysical_unit_planar_shape_model armEntity) throws
      SdaiException {
      String[] list = {
         "above",
         "congruent",
         "below"};
      String keyword = "shape location";
      CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
                             CPhysical_unit_planar_shape_model.attributeShape_location_with_respect_to_seating_plane(null),
                             keyword, list);
   }

   // SR <- RR -> R -> DRI - just remove DRI
   public static void unsetShape_location_with_respect_to_seating_plane(SdaiContext context,
                                                                        EPhysical_unit_planar_shape_model armEntity) throws
      SdaiException {
      String[] list = {
         "above",
         "congruent",
         "below"};
      String keyword = "shape location";
      CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
   }

   /**
    * physical_unit_planar_shape.shape_purpose: purpose;
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
    *    {representation.name = `predefined shape purpose'}
    *    representation.items[i] ->
    *    representation_item
    *    {(representation_item.name = `design')
    *    (representation_item.name = `design profile')
    *    (representation_item.name = `design profile above seating plane')
    *    (representation_item.name = `design profile below seating plane')
    *    (representation_item.name = `electromagnetic compatibility analysis')
    *    (representation_item.name = `non feature shape representation')
    *    (representation_item.name = `shock analysis')
    *    (representation_item.name = `thermal analysis')
    *    (representation_item.name = `vibration analysis')}
    *    representation_item =>
    *    descriptive_representation_item
    */
   // SR <- RR -> R -> DRI
   public static void setShape_purpose(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws SdaiException {
      String[] list = {
         "analysis input",
         "analysis output",
         "assembly symbol",
         "shock analysis input",
         "shock analysis output",
         "design",
         "design profile",
         "vibration analysis input",
         "vibration analysis output",
         "electromagnetic compatibility analysis input",
         "electromagnetic compatibility analysis output",
         "thermal analysis input",
         "thermal analysis output",
         "physical extent"
      };
      String keyword = "predefined shape purpose";
      if (armEntity.testShape_purpose(null)) { // INT
         CxPhysical_unit_shape_model.setRepresentation_item(context, armEntity,
                                CPhysical_unit_planar_shape_model.attributeShape_purpose(null),
                                keyword, list);
      }
      // ELSE NOT IMPLEMENTED the case for EXTERNAL_DEFINITION
   }

   // SR <- RR -> R -> DRI - just remove DRI
   public static void unsetShape_purpose(SdaiContext context, EPhysical_unit_planar_shape_model armEntity) throws
      SdaiException {
      String[] list = {
         "analysis input",
         "analysis output",
         "assembly symbol",
         "shock analysis input",
         "shock analysis output",
         "design",
         "design profile",
         "vibration analysis input",
         "vibration analysis output",
         "electromagnetic compatibility analysis input",
         "electromagnetic compatibility analysis output",
         "thermal analysis input",
         "thermal analysis output",
         "physical extent"};
      String keyword = "predefined shape purpose";
      CxPhysical_unit_shape_model.unsetRepresentation_item(context, armEntity, keyword, list);
   }
   
   
}
