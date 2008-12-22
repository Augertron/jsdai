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

package jsdai.SPackage_xim;

/**
 * @author Giedrius Liutkus
 * @version $$
 * $ $
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
//import jsdai.dictionary.*;
import jsdai.SPackage_mim.*;
import jsdai.SPackage_xim.CPackage_armx;
import jsdai.SPhysical_unit_usage_view_mim.*;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
//import jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPrimary_reference_terminal;
//import jsdai.SElectronic_assembly_interconnect_and_packaging_design.CPrimary_reference_terminal;
//import jsdai.SElectronic_assembly_interconnect_and_packaging_design.APrimary_reference_terminal;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SMixed_complex_types.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxPackage_armx
   extends CPackage_armx implements EMappedXIMEntity{
	
	// TODO - need to redo some attributes where mapping paths are now different (via unique property_definition) 

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition

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
/*	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}*/

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
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
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a8 = set_instance(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
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

      //body_clearance_below_seating_plane
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

      //maximum_body_clearance_above_seating_plane
      unsetBody_clearance_above_seating_plane(context, this);

      //maximum_body_clearance_below_seating_plane
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
		
	}
	
	
   /**
    * Sets/creates data for mapping constraints.
    *
    * <p>
    *  mapping_constraints;
    * 	package <=
    *  physical_unit <=
    *  product_definition 
    *  {product_definition
    *  [product_definition.formation ->
    *  product_definition_formation
    *  product_definition_formation.of_product ->
    *  product <-
    *  product_related_product_category.products[i]
    *  product_related_product_category <=
    *  product_category
    *  product_category.name = 'package']
    *  [product_definition.frame_of_reference ->
    *  product_definition_context <=
    *  application_context_element
    *  application_context_element.name = 'physical design usage']}
    *  end_mapping_constraints;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMappingConstraints(SdaiContext context, EPackage_armx armEntity) throws SdaiException {
   	unsetMappingConstraints(context, armEntity);
      CxPart_usage_view.setMappingConstraints(context, armEntity);

      // Changing category in order to avoid not allowed complex in ARM
      // PACKAGE and Interconnect_module_usage_view
      // System.err.println(" VERSION "+armEntity);
      EProduct product = armEntity.getFormation(null).getOf_product(null);
      AProduct_related_product_category categories = new AProduct_related_product_category();
      CProduct_related_product_category.usedinProducts(null, product, context.domain, categories);
      boolean found = false;
      for (int i = 1; i <= categories.getMemberCount(); i++) {
         EProduct_related_product_category temp = categories.getByIndex(i);
         if (temp.getName(null).equals("interconnect module")) {
            temp.setName(null, "package");
            found = true;
         }
      }
      if(!found){
         LangUtils.Attribute_and_value_structure[] pcStructure = {
               new LangUtils.Attribute_and_value_structure(
               CProduct_related_product_category.attributeName(null), "package")
            };
         
            EProduct_related_product_category eprpc = (EProduct_related_product_category)
   				LangUtils.createInstanceIfNeeded(context,
            		CProduct_related_product_category.definition, pcStructure);
   		
         if(!eprpc.testProducts(null))
         	eprpc.createProducts(null).addUnordered(product);
         else{
         	AProduct products = eprpc.getProducts(null);
         	if(!products.isMember(product))
         		products.addUnordered(product);
         }
      }
   }

   public static void unsetMappingConstraints(SdaiContext context, EPackage_armx armEntity) throws SdaiException {
      CxPart_usage_view.unsetMappingConstraints(context, armEntity);

      if(!armEntity.testDefined_version(null))
			return;
		EProduct_definition_formation version = armEntity.getDefined_version(null);
		if(!version.testOf_product(null))
			return;
		EProduct product = version.getOf_product(null);
		
		AProduct_related_product_category categories = new AProduct_related_product_category();
		CProduct_related_product_category.usedinProducts(null, product, context.domain, categories);
		
		SdaiIterator iter = categories.createIterator();
		while(iter.next()){
			EProduct_related_product_category category = categories.getCurrentMember(iter);
			if(category.getName(null).equals("package"))
				category.getProducts(null).removeUnordered(product);
			if(category.getProducts(null).getMemberCount() == 0)
				category.deleteApplicationInstance();
		}
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
      * Sets/creates data for case_style attribute.
      *
      * <p>
      *  attribute_mapping case_style_registered_style (case_style
      * , (*PATH*), registered_style);
      * 	(package <=)
      * 	(externally_defined_package <=
      * 	externally_defined_physical_unit <=)
      * 	(library_defined_package <=
      * 	library_defined_physical_unit <=
      * 	externally_defined_physical_unit <=)
      * 	physical_unit <=
      * 	product_definition
      * 	characterized_product_definition = product_definition
      * 	characterized_product_definition
      * 	characterized_definition = characterized_product_definition
      * 	characterized_definition <-
      * 	property_definition.definition
      * 	property_definition <-
      * 	property_definition_representation.definition
      * 	property_definition_representation
      * 	property_definition_representation.used_representation ->
      * 	{representation
      * 	representation.name = `registered case style'}
      * 	representation
      *  end_attribute_mapping;
      * </p>
      * @param context SdaiContext.
      * @param armEntity arm entity.
      * @throws SdaiException
      */
// removed since WD28
/*     public static void setCase_style(SdaiContext context, EPackage armEntity) throws SdaiException {
        //unset old values
        unsetCase_style(context, armEntity);

        if (armEntity.testCase_style(null)) {
           jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
              SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();
           jsdai.SAp210_arm_extended.ARegistered_style aArmCase_style = (jsdai.SAp210_arm_extended.ARegistered_style) armEntity.
              getCase_style(null);

           //property definition must be created during creation of Ee_product_definition.context_description
           EProperty_definition property_definition = null;
           AProperty_definition aProperty_definition = new AProperty_definition();
           CProperty_definition.usedinDefinition(null, aimEntity, context.domain, aProperty_definition);
           if (aProperty_definition.getMemberCount() > 0) {
              property_definition = aProperty_definition.getByIndex(1);
           }
           else {
              property_definition = jsdai.SAp210_arm.CxAP210ARMUtilities.createProperty_definition(context, null, "", null, aimEntity, true);
           }

           EMappedARMEntity armCase_style = null;
           jsdai.SRepresentation_schema.ERepresentation aimCase_style = null;
           for (int i = 1; i <= aArmCase_style.getMemberCount(); i++) {
              armCase_style = (EMappedARMEntity) aArmCase_style.getByIndexEntity(i);
              armCase_style.createAimData(context);
              aimCase_style = (jsdai.SRepresentation_schema.ERepresentation) armCase_style.getAimInstance();

              //EProperty_definition_representation
              EProperty_definition_representation property_definition_representation = (
                 EProperty_definition_representation) context.working_model.createEntityInstance(
                 EProperty_definition_representation.class);
              property_definition_representation.setDefinition(null, property_definition);
              property_definition_representation.setUsed_representation(null, aimCase_style);
              aimCase_style.setName(null, "registered case style");
           }
        }
     }
*/
   /**
    * Unsets/deletes data for case_style attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
// removed since WD28
/*   public static void unsetCase_style(SdaiContext context, EPackage armEntity) throws SdaiException {
      jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
         SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();

      //property_definition
      EProperty_definition property_definition = null;
      AProperty_definition aProperty_definition = new AProperty_definition();
      CProperty_definition.usedinDefinition(null, aimEntity, context.domain, aProperty_definition);
      for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
         property_definition = aProperty_definition.getByIndex(i);
         //property_definition_representation
         AProperty_definition_representation aPdr = new AProperty_definition_representation();
         CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aPdr);
         EProperty_definition_representation ePdr = null;
         jsdai.SRepresentation_schema.ERepresentation representation = null;
         for (int j = 1; j <= aPdr.getMemberCount(); j++) {
            ePdr = aPdr.getByIndex(j);
            if (!ePdr.testUsed_representation(null)) {
               continue;
            }
            representation = ePdr.getUsed_representation(null);
            if (representation.testName(null) && representation.getName(null).equals("registered case style")) {
               ePdr.deleteApplicationInstance();
            }
         }
      }
   }
*/
// Removed since WD26
   /**
    * Sets/creates data for package_seating_plane attribute.
    *
    * <p>
    *  attribute_mapping package_seating_plane_seating_plane (package_seating_plane
    * , (*PATH*), seating_plane);
    * (package <=)
    * (externally_defined_package <=
    * externally_defined_physical_unit <=)
    * (library_defined_package <=
    * library_defined_physical_unit <=
    * externally_defined_physical_unit <=)
    * physical_unit <=
    * product_definition
    * characterized_product_definition = product_definition
    * characterized_product_definition
    * characterized_definition = characterized_product_definition
    * characterized_definition <-
    * property_definition.definition
    * property_definition =>
    * product_definition_shape <-
    * shape_aspect.of_shape
    * shape_aspect <-
    * shape_aspect_relationship.related_shape_aspect
    * {shape_aspect_relationship
    * shape_aspect_relationship.name = `package seating plane'}
    * shape_aspect_relationship.relating_shape_aspect ->
    * shape_aspect =>
    * seating_plane
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   // Removed since WD26
   // PU <- PDS <- SA <- SAR -> SP
   /* public static void setPackage_seating_plane(SdaiContext context, EPackage armEntity) throws SdaiException {
      //unset old values
      unsetPackage_seating_plane(context, armEntity);

      if (armEntity.testPackage_seating_plane(null)) {

         jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
            SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();
         jsdai.SAp210_arm_extended.ESeating_plane armPackage_seating_plane = (jsdai.SAp210_arm_extended.ESeating_plane) armEntity.
            getPackage_seating_plane(null);
         armPackage_seating_plane.createAimData(context);
         jsdai.SElectronic_assembly_interconnect_and_packaging_design.ESeating_plane aimPackage_seating_plane =
            (jsdai.SElectronic_assembly_interconnect_and_packaging_design.ESeating_plane)
            armPackage_seating_plane.getAimInstance();

         //property definition must be created during creation of Ee_product_definition.context_description
         // Changes after WD24
         // EProperty_definition property_definition = null;
         AProperty_definition aProperty_definition = new AProperty_definition();
         CProperty_definition.usedinDefinition(null, aimEntity, context.domain, aProperty_definition);
         EProduct_definition_shape product_definition_shape = null;
         EProperty_definition property_definition = null;

         for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
            property_definition = aProperty_definition.getByIndex(i);
            if (property_definition instanceof EProduct_definition_shape) {
               product_definition_shape = (EProduct_definition_shape) property_definition;
               break;
            }
         }

         if (product_definition_shape == null) {
            if (property_definition == null) {
               product_definition_shape = (EProduct_definition_shape) jsdai.SAp210_arm.CxAP210ARMUtilities.createProperty_definition(
                  context, CProduct_definition_shape.definition, "", null, aimEntity, true);
            }
            else {
               product_definition_shape = (EProduct_definition_shape) context.working_model.substituteInstance(
                  property_definition, EProduct_definition_shape.class);
            }
         }
         // Changed since WD24
         //primary_reference_terminal
         //			LangUtils.Attribute_and_value_structure[] saStructure = {
         //    new LangUtils.Attribute_and_value_structure(CShape_aspect.attributeOf_shape(null), product_definition_shape)
         //    };
         //   EPrimary_reference_terminal primary_reference_terminal = (EPrimary_reference_terminal)
         //       LangUtils.createInstanceIfNeeded(context, CPrimary_reference_terminal.definition, saStructure);
         //   if (!primary_reference_terminal.testName(null)) {
         //      primary_reference_terminal.setName(null, "");
         //   }
         //   if (!primary_reference_terminal.testProduct_definitional(null)) {
         //      primary_reference_terminal.setProduct_definitional(null, ELogical.UNKNOWN);
         //   }

         // Intermmidiate shape_aspect
         AShape_aspect aShape_aspect = new AShape_aspect();
         CShape_aspect.usedinOf_shape(null, product_definition_shape, context.domain, aShape_aspect);
         EShape_aspect shape_aspect = null;

         if (aShape_aspect.getMemberCount() > 0) {
            shape_aspect = aShape_aspect.getByIndex(1);
         }
         else {
            throw new SdaiException(SdaiException.ED_NVLD, " Package doesn't have a single terminal " + armEntity);
         }

         //shape_aspect_relationship
         EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.
            createEntityInstance(EShape_aspect_relationship.class);
         shape_aspect_relationship.setRelated_shape_aspect(null, shape_aspect);
         shape_aspect_relationship.setRelating_shape_aspect(null, aimPackage_seating_plane);
         shape_aspect_relationship.setName(null, "package seating plane");
         // AIM gap
         aimPackage_seating_plane.setOf_shape(null, product_definition_shape);
      }
   }
*/
   /**
    * Unsets/deletes data for package_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   /* public static void unsetPackage_seating_plane(SdaiContext context, EPackage armEntity) throws SdaiException {
      jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
         SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();

      //product_definition_shape
      EProduct_definition_shape product_definition_shape = null;
      AProduct_definition_shape aProduct_definition_shape = new AProduct_definition_shape();
      CProduct_definition_shape.usedinDefinition(null, aimEntity, context.domain, aProduct_definition_shape);

      for (int i = 1; i <= aProduct_definition_shape.getMemberCount(); i++) {
         product_definition_shape = aProduct_definition_shape.getByIndex(i);

         //Intermidiate
         EShape_aspect esa = null;
         AShape_aspect asa = new AShape_aspect();
         CShape_aspect.usedinOf_shape(null, product_definition_shape, context.domain, asa);
         for (int j = 1; j <= asa.getMemberCount(); j++) {
            esa = asa.getByIndex(j);

            //shape_aspect_relationship
            EShape_aspect_relationship shape_aspect_relationship = null;
            AShape_aspect_relationship aShape_aspect_relationship = new AShape_aspect_relationship();
            CShape_aspect_relationship.usedinRelated_shape_aspect(null, esa, context.domain, aShape_aspect_relationship);
            for (int k = 1; k <= aShape_aspect_relationship.getMemberCount(); ) {
               shape_aspect_relationship = aShape_aspect_relationship.getByIndex(k);
               if (shape_aspect_relationship.testName(null) &&
                   shape_aspect_relationship.getName(null).equals("package seating plane")) {
                  aShape_aspect_relationship.removeByIndex(k);
                  shape_aspect_relationship.deleteApplicationInstance();
               }
               else {
                  k++;
               }
            }
         }
      }
   }
*/
   /**
    * Sets/creates data for least_material_condition_centroid_location attribute.
    *
    * <p>
    *  attribute_mapping least_material_condition_centroid_location_cartesian_point (least_material_condition_centroid_location
    * , (*PATH*), cartesian_point);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	{representation
    * 	representation.name = `material property data'}
    * 	representation
    * 	representation.items[i] ->
    * 	representation_item =>
    * 	{cartesian_point
    * 	cartesian_point.name = `least material condition centroid location'}
    * 	cartesian_point
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   // Removed since WD37
/*   public static void setLeast_material_condition_centroid_location(SdaiContext context, EPackage armEntity) throws
      SdaiException {
      //unset old values
      unsetLeast_material_condition_centroid_location(context, armEntity);

      if (armEntity.testLeast_material_condition_centroid_location(null)) {

         jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
            SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();
         jsdai.SGeometry_schema.ECartesian_point armLeast_material_condition_centroid_location = (jsdai.SGeometry_schema.ECartesian_point) armEntity.getLeast_material_condition_centroid_location(null);
		//EA armLeast_material_condition_centroid_location.createAimData(context);
         jsdai.SGeometry_schema.ECartesian_point aimLocation = (jsdai.SGeometry_schema.ECartesian_point)
            armLeast_material_condition_centroid_location.getTemp("AIM");//EA .getAimInstance();

         jsdai.SRepresentation_schema.ERepresentation representation = jsdai.SAp210_arm.CxAP210ARMUtilities.findRepresentation(context,
            aimEntity, "material property data");
         aimLocation.setName(null, "least material condition centroid location");
         representation.getItems(null).addUnordered(aimLocation);
      }
   }
*/
   /**
    * Unsets/deletes data for least_material_condition_centroid_location attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
	// Removed since WD37
/*   public static void unsetLeast_material_condition_centroid_location(SdaiContext context, EPackage armEntity) throws
      SdaiException {
      jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
         SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();
      jsdai.SAp210_arm.CxAP210ARMUtilities.clearRepresentationItems(context, aimEntity, null, "material property data",
         "least material condition centroid location");
   }
*/
   /**
    * Sets/creates data for maximum_material_condition_centroid_location attribute.
    *
    * <p>
    *  attribute_mapping maximum_material_condition_centroid_location_cartesian_point (maximum_material_condition_centroid_location
    * , (*PATH*), cartesian_point);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	{representation
    * 	representation.name = `material property data'}
    * 	representation
    * 	representation.items[i] ->
    * 	representation_item =>
    * 	{cartesian_point
    * 	cartesian_point.name = `maximum material condition centroid location'}
    * 	cartesian_point
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
	// Removed since WD37
/*   public static void setMaximum_material_condition_centroid_location(SdaiContext context, EPackage armEntity) throws
      SdaiException {
      //unset old values
      unsetMaximum_material_condition_centroid_location(context, armEntity);

      if (armEntity.testMaximum_material_condition_centroid_location(null)) {

         jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
            SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();
         jsdai.SGeometry_schema.ECartesian_point armMaximum_material_condition_centroid_location = (jsdai.SGeometry_schema.
            ECartesian_point) armEntity.getMaximum_material_condition_centroid_location(null);
		//EA armMaximum_material_condition_centroid_location.createAimData(context);
         jsdai.SGeometry_schema.ECartesian_point aimLocation = (jsdai.SGeometry_schema.ECartesian_point)
            armMaximum_material_condition_centroid_location.getTemp("AIM");//EA .getAimInstance();

         jsdai.SRepresentation_schema.ERepresentation representation = jsdai.SAp210_arm.CxAP210ARMUtilities.findRepresentation(context,
            aimEntity, "material property data");
         aimLocation.setName(null, "maximum material condition centroid location");
         representation.getItems(null).addUnordered(aimLocation);
      }
   }
*/
   /**
    * Unsets/deletes data for maximum_material_condition_centroid_location attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
	// Removed since WD37
/*   public static void unsetMaximum_material_condition_centroid_location(SdaiContext context, EPackage armEntity) throws
      SdaiException {
      jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit aimEntity = (jsdai.
         SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit) armEntity.getAimInstance();
      jsdai.SAp210_arm.CxAP210ARMUtilities.clearRepresentationItems(context, aimEntity, null, "material property data",
         "maximum material condition centroid location");
   }
*/
   /**
    * Sets/creates data for maximum_body_height_above_seating_plane attribute.
    *
    * <p>
    *  attribute_mapping maximum_body_height_above_seating_plane_length_data_element (maximum_body_height_above_seating_plane
    * , (*PATH*), length_data_element);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	representation
    * 	{representation.name = `package mounting data'}
    * 	representation.items[i] ->
    * 	representation_item
    * 	{representation_item.name = `maximum body height above seating plane'}
    * 	representation_item =>
    * 	measure_representation_item <=
    * 	measure_with_unit =>
    * 	length_measure_with_unit
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   // P <- PD <- PDR -> R -> RI
   public static void setMaximum_body_height_above_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetMaximum_body_height_above_seating_plane(context, armEntity);

      if (armEntity.testMaximum_body_height_above_seating_plane(null)) {
         jsdai.SMeasure_schema.ELength_measure_with_unit item = armEntity.getMaximum_body_height_above_seating_plane(null);
         setRepresentation_characteristic(context, armEntity, "maximum body height above seating plane", item);
      }
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
	   unsetRepresentation_characteristic(context, armEntity, "maximum body height above seating plane");
   }

   /**
    * Sets/creates data for maximum_body_height_below_seating_plane attribute.
    *
    * <p>
    *  attribute_mapping maximum_body_height_below_seating_plane_length_data_element (maximum_body_height_below_seating_plane
    * , (*PATH*), length_data_element);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	representation
    * 	{representation.name = `package mounting data'}
    * 	representation.items[i] ->
    * 	representation_item
    * 	{representation_item.name = `maximum body height below seating plane'}
    * 	representation_item =>
    * 	measure_representation_item <=
    * 	measure_with_unit =>
    * 	length_measure_with_unit
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMaximum_body_height_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetMaximum_body_height_below_seating_plane(context, armEntity);

      if (armEntity.testMaximum_body_height_below_seating_plane(null)) {
         jsdai.SMeasure_schema.ELength_measure_with_unit item = armEntity.getMaximum_body_height_below_seating_plane(null);
         setRepresentation_characteristic(context, armEntity, "maximum body height below seating plane", item);
      }
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
	   unsetRepresentation_characteristic(context, armEntity, "maximum body height below seating plane");
   }

   /**
    * Sets/creates data for maximum_body_clearance_above_seating_plane attribute.
    *
    * <p>
    *  attribute_mapping maximum_body_clearance_above_seating_plane_length_data_element (maximum_body_clearance_above_seating_plane
    * , (*PATH*), length_data_element);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	representation
    * 	{representation.name = `package mounting data'}
    * 	representation.items[i] ->
    * 	representation_item
    * 	{representation_item.name = `maximum body clearance above seating plane'}
    * 	representation_item =>
    * 	measure_representation_item <=
    * 	measure_with_unit =>
    * 	length_measure_with_unit
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setBody_clearance_above_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetBody_clearance_above_seating_plane(context, armEntity);

      if (armEntity.testBody_clearance_above_seating_plane(null)) {

         ETolerance_characteristic item = armEntity.getBody_clearance_above_seating_plane(null);
         setRepresentation_characteristic(context, armEntity, "body clearance above seating plane", item);
      }
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
	   unsetRepresentation_characteristic(context, armEntity, "body clearance above seating plane");
   }

   /**
    * Sets/creates data for maximum_body_clearance_below_seating_plane attribute.
    *
    * <p>
    *  attribute_mapping maximum_body_clearance_below_seating_plane_length_data_element (maximum_body_clearance_below_seating_plane
    * , (*PATH*), length_data_element);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	representation
    * 	{representation.name = `package mounting data'}
    * 	representation.items[i] ->
    * 	representation_item
    * 	{representation_item.name = `maximum body clearance below seating plane'}
    * 	representation_item =>
    * 	measure_representation_item <=
    * 	measure_with_unit =>
    * 	length_measure_with_unit
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setBody_clearance_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetBody_clearance_below_seating_plane(context, armEntity);

      if (armEntity.testBody_clearance_below_seating_plane(null)) {
         ETolerance_characteristic item = armEntity.getBody_clearance_below_seating_plane(null);
         setRepresentation_characteristic(context, armEntity, "body clearance below seating plane", item);
      }
   }

   /**
    * Unsets/deletes data for maximum_body_clearance_below_seating_plane attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetBody_clearance_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
	   unsetRepresentation_characteristic(context, armEntity, "body clearance below seating plane");
   }


   /**
    * Sets/creates data for maximum_lead_length_below_seating_plane attribute.
    *
    * <p>
    *  attribute_mapping maximum_lead_length_below_seating_plane_length_data_element (maximum_lead_length_below_seating_plane
    * , (*PATH*), length_data_element);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	representation
    * 	{representation.name = `package mounting data'}
    * 	representation.items[i] ->
    * 	representation_item
    * 	{representation_item.name = `maximum lead length below seating plane'}
    * 	representation_item =>
    * 	measure_representation_item <=
    * 	measure_with_unit =>
    * 	length_measure_with_unit
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMaximum_lead_length_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetMaximum_lead_length_below_seating_plane(context, armEntity);

      if (armEntity.testMaximum_lead_length_below_seating_plane(null)) {
         jsdai.SMeasure_schema.ELength_measure_with_unit item = armEntity.getMaximum_lead_length_below_seating_plane(null);
         setRepresentation_characteristic(context, armEntity, "maximum lead length below seating plane", item);
      }
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
	   unsetRepresentation_characteristic(context, armEntity, "maximum lead length below seating plane");
   }

   /**
    * Sets/creates data for least_lead_length_below_seating_plane attribute.
    *
    * <p>
       *  attribute_mapping least_lead_length_below_seating_plane_length_data_element (least_lead_length_below_seating_plane
    * , (*PATH*), length_data_element);
    * 	(package <=)
    * 	(externally_defined_package <=
    * 	externally_defined_physical_unit <=)
    * 	(library_defined_package <=
    * 	library_defined_physical_unit <=
    * 	externally_defined_physical_unit <=)
    * 	physical_unit <=
    * 	product_definition
    * 	characterized_product_definition = product_definition
    * 	characterized_product_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_definition <-
    * 	property_definition.definition
    * 	property_definition <-
    * 	property_definition_representation.definition
    * 	property_definition_representation
    * 	property_definition_representation.used_representation ->
    * 	representation
    * 	{representation.name = `package mounting data'}
    * 	representation.items[i] ->
    * 	representation_item
    * 	{representation_item.name = `least lead length below seating plane'}
    * 	representation_item =>
    * 	measure_representation_item <=
    * 	measure_with_unit =>
    * 	length_measure_with_unit
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setLeast_lead_length_below_seating_plane(SdaiContext context, EPackage_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetLeast_lead_length_below_seating_plane(context, armEntity);

      if (armEntity.testLeast_lead_length_below_seating_plane(null)) {
         jsdai.SMeasure_schema.ELength_measure_with_unit item = armEntity.getLeast_lead_length_below_seating_plane(null);
   		 setRepresentation_characteristic(context, armEntity, "least lead length below seating plane", item);
      }
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
	   unsetRepresentation_characteristic(context, armEntity, "least lead length below seating plane");
   }

   // New since WDxx
   public static void setNominal_mounting_lead_pitch(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	//unset old values
   	unsetNominal_mounting_lead_pitch(context, armEntity);

   	if (armEntity.testNominal_mounting_lead_pitch(null)) {
   		jsdai.SMeasure_schema.ELength_measure_with_unit item = armEntity.getNominal_mounting_lead_pitch(null);
   		setRepresentation_characteristic(context, armEntity, "nominal mounting lead pitch", item);
   	}
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
	   unsetRepresentation_characteristic(context, armEntity, "nominal mounting lead pitch");
   }

   public static void setNominal_mounting_lead_span(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	//unset old values
   	unsetNominal_mounting_lead_span(context, armEntity);

   	if (armEntity.testNominal_mounting_lead_span(null)) {
   		jsdai.SMeasure_schema.ELength_measure_with_unit item = armEntity.getNominal_mounting_lead_span(null);
   		setRepresentation_characteristic(context, armEntity, "nominal mounting lead span", item);
   	}
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
	   unsetRepresentation_characteristic(context, armEntity, "nominal mounting lead span");
   }

   public static void setMaximum_seating_plane_installation_offset(SdaiContext context, EPackage_armx armEntity) throws
	SdaiException {
   	//unset old values
   	unsetMaximum_seating_plane_installation_offset(context, armEntity);

   	if (armEntity.testMaximum_seating_plane_installation_offset(null)) {
   		jsdai.SMeasure_schema.ELength_measure_with_unit item = armEntity.getMaximum_seating_plane_installation_offset(null);
   		setRepresentation_characteristic(context, armEntity, "maximum seating plane installation offset", item);
   	}
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
	unsetRepresentation_characteristic(context, armEntity, "maximum seating plane installation offset");
   }

   public static void setRepresentation_characteristic(SdaiContext context, EProduct_definition armEntity, String epdName, EEntity item)throws SdaiException{
	   EProperty_definition epd = (EProperty_definition)
	   		context.working_model.createEntityInstance(CProperty_definition.definition);
	   epd.setName(null, epdName);
	   epd.setDefinition(null, armEntity);
	   EProperty_definition_representation epdr = (EProperty_definition_representation)
  			context.working_model.createEntityInstance(CProperty_definition_representation.definition);
	   epdr.setDefinition(null, epd);
	   ERepresentation er;
	   if(item instanceof ERepresentation){
		   er = (ERepresentation)item;
	   }else{
		   if(!(item instanceof ERepresentation_item)){
			   jsdai.SMeasure_schema.ELength_measure_with_unit lmwu = (jsdai.SMeasure_schema.ELength_measure_with_unit)item;
			   item = context.working_model.substituteInstance(lmwu, CLength_measure_with_unit$measure_representation_item.definition);
			   ((ERepresentation_item)(item)).setName(null, "");			   
		   }
		   ARepresentation ar = new ARepresentation();
		   CRepresentation.usedinItems(null, (ERepresentation_item)item, context.domain, ar);
		   
		   if(ar.getMemberCount() == 0){
			   er = CxAP210ARMUtilities.createRepresentation(context, CRepresentation.definition, "", false);
			   ARepresentation_item items;
			   if(er.testItems(null)){
				   items = er.getItems(null);
			   }else{
				   items = er.createItems(null);
			   }
			   items.addUnordered(item);
		   }else{
			   er = ar.getByIndex(1);
		   }
	   }
	   epdr.setUsed_representation(null, er);
   }

   public static void unsetRepresentation_characteristic(SdaiContext context, EProduct_definition armEntity, String epdName)throws SdaiException{
	   AProperty_definition apd = new AProperty_definition();
	   CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
	   SdaiIterator iterAPD = apd.createIterator();
	   while(iterAPD.next()){
		   EProperty_definition epd = apd.getCurrentMember(iterAPD);
		   if((epd.testName(null))&&(epd.getName(null).equals(epdName))){
			   AProperty_definition_representation apdr = new AProperty_definition_representation();
			   CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
			   SdaiIterator iterAPDR = apdr.createIterator();
			   while(iterAPDR.next()){
				   EProperty_definition_representation epdr = apdr.getCurrentMember(iterAPDR);
				   epdr.deleteApplicationInstance();
			   }			   
			   epd.deleteApplicationInstance();
		   }
	   }
   }
   
///////////////////////////////////////
// New since WD26
   /**
     * Sets/creates data for mounting_technology attribute.
     *
     * <p>
      attribute_mapping mounting_technology (mounting_technology
     , descriptive_representation_item.description);
        (package <=)
        (externally_defined_package <=
        externally_defined_physical_unit <=)
        (library_defined_package <=
        library_defined_physical_unit <=
        externally_defined_physical_unit <=)
        physical_unit <=
        product_definition
        characterized_product_definition = product_definition
        characterized_product_definition
        characterized_definition = characterized_product_definition
        characterized_definition <-
        property_definition.definition
        property_definition <-
        property_definition_representation.definition
        property_definition_representation
        property_definition_representation.used_representation ->
        {representation
        representation.name = `package mounting data'}
        representation
        representation.items[i] ->
        representation_item =>
        {representation_item
        representation_item.name = `mounting technology'}
        descriptive_representation_item
        {descriptive_representation_item
        (descriptive_representation_item.description = `surface mount')
        (descriptive_representation_item.description = `through hole')}
        descriptive_representation_item.description
      end_attribute_mapping;
     * </p>
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    // PU <- PropD <- PDR -> R -> DRI
    public static void setMounting_technology(SdaiContext context, EPackage_armx armEntity) throws
       SdaiException {
       //unset old values
       unsetMounting_technology(context, armEntity);

       if (armEntity.testMounting_technology(null)) {
          int armMounting_technology = armEntity.getMounting_technology(null);
          EProperty_definition epd = (EProperty_definition)
          	context.working_model.createEntityInstance(CProperty_definition.definition);
          epd.setName(null, "mounting technology");
          epd.setDescription(null, EMounting_technology_type.toString(armMounting_technology).toLowerCase().replace('_', ' '));
          epd.setDefinition(null, armEntity);
       }
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
       EPhysical_unit aimEntity = armEntity;
      AProperty_definition aProperty_definition = new AProperty_definition();
      CProperty_definition.usedinDefinition(null, aimEntity, context.domain, aProperty_definition);
      for(int i=1;i<=aProperty_definition.getMemberCount();i++){
         EProperty_definition property_definition = aProperty_definition.getByIndex(i);
         if (property_definition.getName(null).equals("mounting technology")) {
        	 property_definition.deleteApplicationInstance();
         }
      }
    }

}
