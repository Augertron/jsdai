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

package jsdai.SAltered_package_xim;

/**
 * @author Giedrius Liutkus
 * @version $$
 * $ $
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPackage_mim.*;
import jsdai.SPackage_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxAltered_package
   extends CAltered_package implements EMappedXIMEntity{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}

	// Taken from Physical_unit - Property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a6$;
	}
	// ENDOF Taken from Physical_unit - Property_definition

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}
	
	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a8);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a8 = get_instance_select(a8);
		return (EEntity)a8;
	}*/

	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a8 = set_instanceX(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
	}
	
	// Product_view_definition
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		// 		commonForAIMInstanceCreation(context);
      setTemp("AIM", CPackage.definition);

      setMappingConstraints(context, this);

      //********** "managed_design_object" attributes

       //********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

		
		setId_x(context, this);
      //********** "package" attributes
        //case_style
        // removed since WD28
      // setCase_style(context, this);

      //package_seating_plane
      // Removed since WD26
      // setPackage_seating_plane(context, this);

      //least_material_condition_centroid_location
		//	Removed since WD37
      // setLeast_material_condition_centroid_location(context, this);

      //maximum_material_condition_centroid_location
		//	Removed since WD37
      // setMaximum_material_condition_centroid_location(context, this);

      //maximum_body_height_above_seating_plane
      setMaximum_body_height_above_seating_plane(context, this);

      //maximum_body_height_below_seating_plane
      setMaximum_body_height_below_seating_plane(context, this);

      //body_clearance_above_seating_plane
      setBody_clearance_above_seating_plane(context, this);

      //maximum_body_clearance_below_seating_plane
      setBody_clearance_below_seating_plane(context, this);

      //maximum_lead_length_below_seating_plane
      setMaximum_lead_length_below_seating_plane(context, this);

      //least_lead_length_below_seating_plane
      setLeast_lead_length_below_seating_plane(context, this);

      //nominal_mounting_lead_pitch
      setNominal_mounting_lead_pitch(context, this);
      
      //nominal_mounting_lead_span
      setNominal_mounting_lead_span(context, this);
      
      // maximum_seating_plane_installation_offset
      setMaximum_seating_plane_installation_offset(context, this);
      
      //of_datum_reference_frame
		//	Removed since WD37
      // setOf_datum_reference_frame(context, this);

      //of_defining_datum_system_relationship
		//	Removed since WD37
      // setOf_defining_datum_system_relationship(context, this);

      //of_derived_datum_system_definining_relationship
		//	Removed since WD37
      // setOf_derived_datum_system_definining_relationship(context, this);

      // New since WD26
      setMounting_technology(context, this);

      // Altered_package attributes
      // of_geometric_status
      setOf_geometric_status(context, this);
      
      // modified_terminal_separation
		setModified_terminal_separation(context, this);
      
      // package_to_be_altered
		setPackage_to_be_altered(context, this);
      

      // CLEAN ARM
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetId_x(null);
		unsetMaximum_body_height_above_seating_plane(null);
      unsetMaximum_body_height_below_seating_plane(null);
      unsetBody_clearance_above_seating_plane(null);
      unsetBody_clearance_below_seating_plane(null);
      unsetMaximum_lead_length_below_seating_plane(null);
      unsetLeast_lead_length_below_seating_plane(null);
      unsetNominal_mounting_lead_pitch(null);
      unsetNominal_mounting_lead_span(null);
      unsetMaximum_seating_plane_installation_offset(null);
      unsetMounting_technology(null);

      unsetOf_geometric_status(null);
		unsetModified_terminal_separation(null);
		unsetPackage_to_be_altered(null);
      
   }

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
      unsetMappingConstraints(context, this);

      //********** "managed_design_object" attributes

       //********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		
		unsetId_x(context, this);
      //********** "package" attributes
        //case_style
        // removed since WD28
      // setCase_style(context, this);

      //package_seating_plane
      // Removed since WD26
      // setPackage_seating_plane(context, this);

      //least_material_condition_centroid_location
		//	Removed since WD37
      // setLeast_material_condition_centroid_location(context, this);

      //maximum_material_condition_centroid_location
		//	Removed since WD37
      // setMaximum_material_condition_centroid_location(context, this);

      //maximum_body_height_above_seating_plane
      unsetMaximum_body_height_above_seating_plane(context, this);

      //maximum_body_height_below_seating_plane
      unsetMaximum_body_height_below_seating_plane(context, this);

      //body_clearance_above_seating_plane
      unsetBody_clearance_above_seating_plane(context, this);

      //body_clearance_below_seating_plane
      unsetBody_clearance_below_seating_plane(context, this);

      //maximum_lead_length_below_seating_plane
      unsetMaximum_lead_length_below_seating_plane(context, this);

      //least_lead_length_below_seating_plane
      unsetLeast_lead_length_below_seating_plane(context, this);

      //nominal_mounting_lead_pitch
      unsetNominal_mounting_lead_pitch(context, this);
      
      //nominal_mounting_lead_span
      unsetNominal_mounting_lead_span(context, this);
      
      // maximum_seating_plane_installation_offset
      unsetMaximum_seating_plane_installation_offset(context, this);
      
      //of_datum_reference_frame
		//	Removed since WD37
      // setOf_datum_reference_frame(context, this);

      //of_defining_datum_system_relationship
		//	Removed since WD37
      // setOf_defining_datum_system_relationship(context, this);

      //of_derived_datum_system_definining_relationship
		//	Removed since WD37
      // setOf_derived_datum_system_definining_relationship(context, this);

      // New since WD26
      unsetMounting_technology(context, this);

      // Altered_package attributes
      // of_geometric_status
      unsetOf_geometric_status(context, this);
      
      // modified_terminal_separation
		unsetModified_terminal_separation(context, this);
      
      // package_to_be_altered
		unsetPackage_to_be_altered(context, this);
      
	}
	
	
   /**
    * Sets/creates data for mapping constraints.
    *
    * <p>
    *  mapping_constraints;
    *   package &lt;=
    *   physical_unit &lt;=
    *   product_definition
    *   {product_definition
    *   [product_definition.description = 'altered package']
    *   [product_definition.frame_of_reference -&gt;
    *   product_definition_context &lt;=
    *   application_context_element
    *   application_context_element.name = 'physical design usage']}
    *  end_mapping_constraints;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMappingConstraints(SdaiContext context, EAltered_package armEntity) throws SdaiException {
      CxPackage_armx.setMappingConstraints(context, armEntity);
      armEntity.setDescription((EProduct_definition)null, "altered package");
   }

   public static void unsetMappingConstraints(SdaiContext context, EAltered_package armEntity) throws SdaiException {
      CxPackage_armx.unsetMappingConstraints(context, armEntity);
      armEntity.unsetDescription((EProduct_definition)null);
   }
   
   
   //********** "managed_design_object" attributes
   /**
    * Sets/creates data for product_definition_approval attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws
      SdaiException {
      CxProduct_view_definition.setAdditional_characterization(context, armEntity);
   }

   /**
    * Unsets/deletes data for product_definition_approval attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws
      SdaiException {
      CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
   }

   /**
    * Sets/creates data for creation_date attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.setAdditional_contexts(context, armEntity);
   }

   /**
    * Unsets/deletes data for creation_date attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
   }

   /**
    * Sets/creates data for documentation attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
      CxItem_shape.setId_x(context, armEntity);
   }

   /**
    * Unsets/deletes data for documentation attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
   	CxItem_shape.unsetId_x(context, armEntity);
   }

   //********** "physical_unit_usage_view" attributes

    //********** "package" attributes

   /**
    * Sets/creates data for maximum_body_height_above_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMaximum_body_height_above_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
      //unset old values
   	CxPackage_armx.setMaximum_body_height_above_seating_plane(context, armEntity);
   }

   /**
    * Unsets/deletes data for maximum_body_height_above_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetMaximum_body_height_above_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.unsetMaximum_body_height_above_seating_plane(context, armEntity);
   }

   /**
    * Sets/creates data for maximum_body_height_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMaximum_body_height_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.setMaximum_body_height_below_seating_plane(context, armEntity);   	
   }

   /**
    * Unsets/deletes data for maximum_body_height_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetMaximum_body_height_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.unsetMaximum_body_height_below_seating_plane(context, armEntity);   
   }

   /**
    * Sets/creates data for body_clearance_above_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setBody_clearance_above_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.setBody_clearance_above_seating_plane(context, armEntity);   	
   }

   /**
    * Unsets/deletes data for maximum_body_clearance_above_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetBody_clearance_above_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.unsetBody_clearance_above_seating_plane(context, armEntity);
   }

   /**
    * Sets/creates data for body_clearance_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setBody_clearance_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.setBody_clearance_below_seating_plane(context, armEntity);   	
   }

   /**
    * Unsets/deletes data for body_clearance_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetBody_clearance_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.unsetBody_clearance_below_seating_plane(context, armEntity);
   }

   /**
    * Sets/creates data for maximum_lead_length_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMaximum_lead_length_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.setMaximum_lead_length_below_seating_plane(context, armEntity);   	
   }

   /**
    * Unsets/deletes data for maximum_lead_length_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetMaximum_lead_length_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.unsetMaximum_lead_length_below_seating_plane(context, armEntity);
   }

   /**
    * Sets/creates data for least_lead_length_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setLeast_lead_length_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.setLeast_lead_length_below_seating_plane(context, armEntity);
   }

   /**
    * Unsets/deletes data for least_lead_length_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetLeast_lead_length_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
   	CxPackage_armx.unsetLeast_lead_length_below_seating_plane(context, armEntity);   	
   }

   // New since WDxx
   public static void setNominal_mounting_lead_pitch(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	CxPackage_armx.setNominal_mounting_lead_pitch(context, armEntity);   	
   }

   /**
    * Unsets/deletes data for least_lead_length_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetNominal_mounting_lead_pitch(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	CxPackage_armx.unsetNominal_mounting_lead_pitch(context, armEntity);   	
   }

   public static void setNominal_mounting_lead_span(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	CxPackage_armx.setNominal_mounting_lead_span(context, armEntity);   	
   }

   /**
    * Unsets/deletes data for least_lead_length_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetNominal_mounting_lead_span(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	CxPackage_armx.unsetNominal_mounting_lead_span(context, armEntity);   	
   }

   public static void setMaximum_seating_plane_installation_offset(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	CxPackage_armx.setMaximum_seating_plane_installation_offset(context, armEntity);   	
   }

   /**
    * Unsets/deletes data for least_lead_length_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetMaximum_seating_plane_installation_offset(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	CxPackage_armx.unsetMaximum_seating_plane_installation_offset(context, armEntity);   	
   }

///////////////////////////////////////
// New since WD26
   /**
     * Sets/creates data for mounting_technology attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    // PU <- PropD <- PDR -> R -> DRI
    public static void setMounting_technology(SdaiContext context, EPackage_armx armEntity) throws
       SdaiException {
    	CxPackage_armx.setMounting_technology(context, armEntity);    	
    }

    /**
     * Unsets/deletes data for mounting_technology attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    // PU <- PropD <- PDR | -> R -> DRI (remove DRI)
    public static void unsetMounting_technology(SdaiContext context, EPackage_armx armEntity) throws
       SdaiException {
    	CxPackage_armx.unsetMounting_technology(context, armEntity);    	
    }

///////////////////////////////

 	//********** "altered_package" attributes

 	/**
 	* Sets/creates data for package_to_be_altered attribute.
 	*
 	* <p>
 	*  attribute_mapping package_to_be_altered_package (package_to_be_altered
 	* , (*PATH*), package);
 	* 	(package <=)
 	* 	(externally_defined_package <=
 	* 	externally_defined_physical_unit <=)
 	* 	(library_defined_package <=
 	* 	library_defined_physical_unit <=
 	* 	externally_defined_physical_unit <=)
 	* 	physical_unit <=
 	* 	product_definition <-
 	* 	product_definition_relationship.related_product_definition
 	* 	{product_definition_relationship
 	* 	product_definition_relationship.name = `package alteration'}
 	* 	product_definition_relationship
 	* 	product_definition_relationship.relating_product_definition ->
 	* 	{product_definition
 	* 	product_definition.frame_of_reference ->
 	* 	product_definition_context <=
 	* 	application_context_element
 	* 	application_context_element.name = `physical design usage'}
 	* 	product_definition =>
 	* 	physical_unit =>
 	* 	(package)
 	* 	(externally_defined_physical_unit =>
 	* 	externally_defined_package)
 	* 	(externally_defined_physical_unit =>
 	* 	library_defined_physical_unit =>
 	* 	library_defined_package)
 	*  end_attribute_mapping;
 	* </p>
 	* <p>
 	*  attribute_mapping package_to_be_altered_package (package_to_be_altered
 	* , (*PATH*), package);
 	* 	(package <=)
 	* 	(externally_defined_package <=
 	* 	externally_defined_physical_unit <=)
 	* 	(library_defined_package <=
 	* 	library_defined_physical_unit <=
 	* 	externally_defined_physical_unit <=)
 	* 	physical_unit <=
 	* 	product_definition <-
 	* 	product_definition_relationship.related_product_definition
 	* 	{product_definition_relationship
 	* 	product_definition_relationship.name = `package alteration'}
 	* 	product_definition_relationship
 	* 	product_definition_relationship.relating_product_definition ->
 	* 	{product_definition
 	* 	product_definition.frame_of_reference ->
 	* 	product_definition_context <=
 	* 	application_context_element
 	* 	application_context_element.name = `physical design usage'}
 	* 	product_definition =>
 	* 	physical_unit =>
 	* 	(package)
 	* 	(externally_defined_physical_unit =>
 	* 	externally_defined_package)
 	* 	(externally_defined_physical_unit =>
 	* 	library_defined_physical_unit =>
 	* 	library_defined_package)
 	*  end_attribute_mapping;
 	* </p>
 	* <p>
 	*  attribute_mapping package_to_be_altered_package (package_to_be_altered
 	* , (*PATH*), package);
 	* 	(package <=)
 	* 	(externally_defined_package <=
 	* 	externally_defined_physical_unit <=)
 	* 	(library_defined_package <=
 	* 	library_defined_physical_unit <=
 	* 	externally_defined_physical_unit <=)
 	* 	physical_unit <=
 	* 	product_definition <-
 	* 	product_definition_relationship.related_product_definition
 	* 	{product_definition_relationship
 	* 	product_definition_relationship.name = `package alteration'}
 	* 	product_definition_relationship
 	* 	product_definition_relationship.relating_product_definition ->
 	* 	{product_definition
 	* 	product_definition.frame_of_reference ->
 	* 	product_definition_context <=
 	* 	application_context_element
 	* 	application_context_element.name = `physical design usage'}
 	* 	product_definition =>
 	* 	physical_unit =>
 	* 	(package)
 	* 	(externally_defined_physical_unit =>
 	* 	externally_defined_package)
 	* 	(externally_defined_physical_unit =>
 	* 	library_defined_physical_unit =>
 	* 	library_defined_package)
 	*  end_attribute_mapping;
 	* </p>
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void setPackage_to_be_altered(SdaiContext context, EAltered_package armEntity) throws SdaiException
 	{
 		//unset old values
 		unsetPackage_to_be_altered(context, armEntity);

 		if (armEntity.testPackage_to_be_altered(null))
 		{

 			EPackage_armx armPackage_to_be_altered = armEntity.getPackage_to_be_altered(null);

 			//product_definition_relationship
 			jsdai.SProduct_definition_schema.EProduct_definition_relationship relationship = (jsdai.SProduct_definition_schema.EProduct_definition_relationship) context.working_model.createEntityInstance(jsdai.SProduct_definition_schema.EProduct_definition_relationship.class);
 			relationship.setId(null, "");
 			relationship.setName(null, "package alteration");
 			relationship.setRelated_product_definition(null, armEntity);
 			relationship.setRelating_product_definition(null, armPackage_to_be_altered);
 		}
 	}


 	/**
 	* Unsets/deletes data for package_to_be_altered attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetPackage_to_be_altered(SdaiContext context, EAltered_package armEntity) throws SdaiException
 	{
 		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
 		EProduct_definition_relationship relationship = null;
 		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);
 		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
 			relationship = aRelationship.getByIndex(i);

 			if (relationship.testName(null) && relationship.getName(null).equals("package alteration")) {
 				relationship.deleteApplicationInstance();
 			}
 		}
 	}

 	public static void setOf_geometric_status(SdaiContext context, EAltered_package armEntity) throws SdaiException
 	{
 		//unset old values
 		unsetOf_geometric_status(context, armEntity);

 		if (armEntity.testOf_geometric_status(null)){
 			throw new SdaiException(SdaiException.FN_NAVL, "Set function is not implemented for this attribute");
 		}
 	}

 	public static void unsetOf_geometric_status(SdaiContext context, EAltered_package armEntity) throws SdaiException
 	{
		if (armEntity.testOf_geometric_status(null)){
 			throw new SdaiException(SdaiException.FN_NAVL, "Unset function is not implemented for this attribute");
 		}
 	}

 	public static void setModified_terminal_separation(SdaiContext context, EAltered_package armEntity) throws SdaiException
 	{
 		//unset old values
 		unsetOf_geometric_status(context, armEntity);

 		if (armEntity.testModified_terminal_separation(null)){
 			throw new SdaiException(SdaiException.FN_NAVL, "Set function is not implemented for this attribute");
 		}
 	}

 	public static void unsetModified_terminal_separation(SdaiContext context, EAltered_package armEntity) throws SdaiException
 	{
		if (armEntity.testModified_terminal_separation(null)){
 			throw new SdaiException(SdaiException.FN_NAVL, "Unset function is not implemented for this attribute");
 		}
 	}
 	
    
}
