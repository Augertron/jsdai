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
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SCharacteristic_xim.*;
import jsdai.SFeature_and_connection_zone_mim.EConnection_zone_interface_plane_relationship;
import jsdai.SFeature_and_connection_zone_xim.AConnection_zone;
import jsdai.SFeature_and_connection_zone_xim.EConnection_zone;
import jsdai.SGroup_mim.*;
import jsdai.SGroup_schema.EGroup;
import jsdai.SPackage_mim.CPackage_terminal_template_definition;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxPackage_terminal_template_definition_armx extends CPackage_terminal_template_definition_armx implements EMappedXIMEntity{

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EGroup type) throws SdaiException {
		return test_string(a2);
	}
	public String getName(EGroup type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setName(EGroup type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(EGroup type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EGroup type) throws SdaiException {
		return a3$;
	}

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPackage_terminal_template_definition.definition);

		setMappingConstraints(context, this);
		
		// external_connection_zone
		setExternal_connection_zone(context, this);
		
      // internal_connection_zone
		setInternal_connection_zone(context, this);
		
      // seating_plane_intersection
		setSeating_plane_intersection(context, this);
		
		// terminal_characteristic
		setTerminal_characteristic(context, this);
		
      // terminal_diametrical_extent
		setTerminal_diametrical_extent(context, this);
		
      // seating_plane_zone
		setSeating_plane_zone(context, this);
		
		setLead_form(context, this);
		
		// setFeature_model(context, this);
		
		// Clean ARM
		// external_connection_zone
		unsetExternal_connection_zone(null);
		
      // internal_connection_zone
		unsetInternal_connection_zone(null);
		
      // seating_plane_intersection
		unsetSeating_plane_intersection(null);
		
		// terminal_characteristic
		unsetTerminal_characteristic(null);
		
      // terminal_diametrical_extent
		unsetTerminal_diametrical_extent(null);
		
      // seating_plane_zone
		unsetSeating_plane_zone(null);
		
		unsetLead_form(null);
		// unsetFeature_model(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// external_connection_zone
			unsetExternal_connection_zone(context, this);
			
	      // internal_connection_zone
			unsetInternal_connection_zone(context, this);
			
	      // seating_plane_intersection
			unsetSeating_plane_intersection(context, this);
			
			// terminal_characteristic
			unsetTerminal_characteristic(context, this);
			
	      // terminal_diametrical_extent
			unsetTerminal_diametrical_extent(context, this);
			
	      // seating_plane_zone
			unsetSeating_plane_zone(context, this);
			
			unsetLead_form(context, this);
			// unsetFeature_model(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  package_terminal &lt;=
	 *  shape_aspect
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_feature_template_definition_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
		CxPart_feature_template_definition_armx.unsetMappingConstraints(context, armEntity);
	}

////////////////////////////////////////////////////////////////////
	
   //********** "package_terminal" attributes

   /**
    * Sets/creates data for external_connection_zone attribute.
    *
    * <p>
    *  attribute_mapping external_connection_zone_connection_zone (external_connection_zone
    * , (*PATH*), connection_zone);
    * 	package_terminal <=
    * 	shape_aspect <-
    * 	shape_aspect_relationship.relating_shape_aspect
    * 	{shape_aspect_relationship
    * 	shape_aspect_relationship.name = `external connection zone'}
    * 	shape_aspect_relationship
    * 	shape_aspect_relationship.related_shape_aspect ->
    * 	shape_aspect
    * 	{shape_aspect
    * 	shape_aspect.description = `connection zone'}
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setExternal_connection_zone(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetExternal_connection_zone(context, armEntity);

      if (armEntity.testExternal_connection_zone(null)) {

         AConnection_zone aArmExternal_connection_zone = armEntity.getExternal_connection_zone(null);
         EConnection_zone armExternal_connection_zone = null;

         for (int i = 1; i <= aArmExternal_connection_zone.getMemberCount(); i++) {
            armExternal_connection_zone = aArmExternal_connection_zone.getByIndex(i);

            // PDS
            LangUtils.Attribute_and_value_structure[] pdStructure = {
               new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), 
               		armEntity),
            };

            EProperty_definition property_definition = (EProperty_definition) LangUtils.createInstanceIfNeeded(
               context,
               CProduct_definition_shape.definition,
               pdStructure);

            if (!property_definition.testName(null)) {
               property_definition.setName(null, "");
            }
            // SA
            LangUtils.Attribute_and_value_structure[] saStructure = {
               new LangUtils.Attribute_and_value_structure(CShape_aspect.attributeOf_shape(null), 
               		armEntity)
            };

            EShape_aspect esa = (EShape_aspect) LangUtils.createInstanceIfNeeded(
               context,
               CShape_aspect.definition,
               saStructure);
            if (!esa.testName(null)) {
               esa.setName(null, "");
            }
            if (!esa.testProduct_definitional(null)) {
            		esa.setProduct_definitional(null, ELogical.UNKNOWN);
            }

            //shape_aspect_relationship
            EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.
               createEntityInstance(CShape_aspect_relationship.definition);
            shape_aspect_relationship.setName(null, "external connection zone");
            shape_aspect_relationship.setRelating_shape_aspect(null, esa);
            shape_aspect_relationship.setRelated_shape_aspect(null, armExternal_connection_zone);
         }
      }
   }

  /**
   * Unsets/deletes data for external_connection_zone attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetExternal_connection_zone(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws
     SdaiException {
	   //property_definition
	   EProperty_definition property_definition = null;
	   AProperty_definition aProperty_definition = new AProperty_definition();
	   CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
	   for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
	      property_definition = aProperty_definition.getByIndex(i);
	      if(property_definition instanceof EProduct_definition_shape){
	         AShape_aspect asa = new AShape_aspect();
	         CShape_aspect.usedinOf_shape(null, (EProduct_definition_shape)property_definition, context.domain, asa);
	   	   for (int j = 1; j <= asa.getMemberCount(); j++) {
	   	      EShape_aspect esa = asa.getByIndex(j);
	   	      AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
	   	      CShape_aspect_relationship.usedinRelating_shape_aspect(null, esa, context.domain, aRelationship);
	   	      EShape_aspect_relationship relationship = null;
	   	      for (int k = 1; k <= aRelationship.getMemberCount(); k++) {
	   	         relationship = aRelationship.getByIndex(k);
	   	         if (relationship.testName(null) && relationship.getName(null).equals("external connection zone")) {
	   	            relationship.deleteApplicationInstance();
	   	         }
	   	      }
	   	      
	   	   }	      	
	      }
	   }
  }

  /**
   * Sets/creates data for internal_connection_zone attribute.
   *
   * <p>
   *  attribute_mapping internal_connection_zone_connection_zone (internal_connection_zone
   * , (*PATH*), connection_zone);
   * 	package_terminal <=
   * 	shape_aspect <-
   * 	shape_aspect_relationship.relating_shape_aspect
   * 	{shape_aspect_relationship
   * 	shape_aspect_relationship.name = `internal connection zone'}
   * 	shape_aspect_relationship
   * 	shape_aspect_relationship.related_shape_aspect ->
   * 	shape_aspect
   * 	{shape_aspect
   * 	shape_aspect.description = `connection zone'}
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setInternal_connection_zone(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws
     SdaiException {
     //unset old values
     unsetInternal_connection_zone(context, armEntity);

     if (armEntity.testInternal_connection_zone(null)) {

     		AConnection_zone_in_part_feature_template_definition aArmInternal_connection_zone = armEntity.getInternal_connection_zone(null);
     		EConnection_zone_in_part_feature_template_definition armInternal_connection_zone = null;

        for (int i = 1; i <= aArmInternal_connection_zone.getMemberCount(); i++) {
           armInternal_connection_zone = aArmInternal_connection_zone.getByIndex(i);

           // PDS
           LangUtils.Attribute_and_value_structure[] pdStructure = {
              new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), 
              		armEntity),
           };

           EProperty_definition property_definition = (EProperty_definition) LangUtils.createInstanceIfNeeded(
              context,
              CProduct_definition_shape.definition,
              pdStructure);

           if (!property_definition.testName(null)) {
              property_definition.setName(null, "");
           }
           // SA
           LangUtils.Attribute_and_value_structure[] saStructure = {
              new LangUtils.Attribute_and_value_structure(CShape_aspect.attributeOf_shape(null), 
              		armEntity)
           };

           EShape_aspect esa = (EShape_aspect) LangUtils.createInstanceIfNeeded(
              context,
              CShape_aspect.definition,
              saStructure);
           if (!esa.testName(null)) {
              esa.setName(null, "");
           }
           if (!esa.testProduct_definitional(null)) {
           		esa.setProduct_definitional(null, ELogical.UNKNOWN);
           }
           
           //shape_aspect_relationship
           EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.
              createEntityInstance(CShape_aspect_relationship.definition);
           shape_aspect_relationship.setName(null, "internal connection zone");
           shape_aspect_relationship.setRelating_shape_aspect(null, esa);
           shape_aspect_relationship.setRelated_shape_aspect(null, armInternal_connection_zone);
        }
     }
  }

  /**
   * Unsets/deletes data for internal_connection_zone attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetInternal_connection_zone(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws
     SdaiException {
   //property_definition
   EProperty_definition property_definition = null;
   AProperty_definition aProperty_definition = new AProperty_definition();
   CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
   for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
      property_definition = aProperty_definition.getByIndex(i);
      if(property_definition instanceof EProduct_definition_shape){
         AShape_aspect asa = new AShape_aspect();
         CShape_aspect.usedinOf_shape(null, (EProduct_definition_shape)property_definition, context.domain, asa);
   	   for (int j = 1; j <= asa.getMemberCount(); j++) {
   	      EShape_aspect esa = asa.getByIndex(j);
   	      AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
   	      CShape_aspect_relationship.usedinRelating_shape_aspect(null, esa, context.domain, aRelationship);
   	      EShape_aspect_relationship relationship = null;
   	      for (int k = 1; k <= aRelationship.getMemberCount(); k++) {
   	         relationship = aRelationship.getByIndex(k);
   	         if (relationship.testName(null) && relationship.getName(null).equals("internal connection zone")) {
   	            relationship.deleteApplicationInstance();
   	         }
   	      }
   	      
   	   }	      	
      }
   }
  }

  /**
   * Sets/creates data for terminal_characteristic attribute.
   *
   * <p>
   *  attribute_mapping terminal_characteristic_characteristic (terminal_characteristic
   * , (*PATH*), characteristic);
   * 	package_terminal <=
   * 	shape_aspect
   * 	shape_definition = shape_aspect
   * 	shape_definition
   * 	characterized_definition = shape_definition
   * 	characterized_definition <-
   * 	property_definition.definition
   * 	property_definition <-
   * 	property_definition_representation.definition
   * 	property_definition_representation
   * 	property_definition_representation.used_representation ->
   * 	representation
   * 	representation.items[i] ->
   * 	representation_item
   * 	{representation_item
   * 	group_assigned_item = representation_item
   * 	group_assigned_item <-
   * 	applied_group_assignment.items[i]
   * 	applied_group_assignment <=
   * 	group_assignment
   * 	group_assignment.assigned_group ->
   * 	group =>
   * 	characteristic_type}
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setTerminal_characteristic(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
     //unset old values
     unsetTerminal_characteristic(context, armEntity);

     if (armEntity.testTerminal_characteristic(null)) {

        ACharacteristic aArmTerminal_characteristic = armEntity.getTerminal_characteristic(null);
        //property_definition
        LangUtils.Attribute_and_value_structure[] pdStructure = {
           new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), armEntity),
        };

        EProperty_definition property_definition = (EProperty_definition) LangUtils.createInstanceIfNeeded(
           context,
           CProperty_definition.definition,
           pdStructure);

        if (!property_definition.testName(null)) {
           property_definition.setName(null, "");
        }

        //property_definition_representation
        LangUtils.Attribute_and_value_structure[] pdrStructure = {
           new LangUtils.Attribute_and_value_structure(CProperty_definition_representation.attributeDefinition(null),
                                                       property_definition)
        };
        EProperty_definition_representation property_definition_representation = (EProperty_definition_representation)
           LangUtils.createInstanceIfNeeded(
           context,
           CProperty_definition_representation.definition,
           pdrStructure);

        //representation
        ERepresentation representation = null;
        if (property_definition_representation.testUsed_representation(null)) {
           representation = property_definition_representation.getUsed_representation(null);
        }
        else {
           representation = CxAP210ARMUtilities.createRepresentation(context, "", false);
           property_definition_representation.setUsed_representation(null, representation);
        }

        if (!representation.testItems(null)) {
           representation.createItems(null);
        }
        ERepresentation_item armTerminal_characteristic;
        for (int i = 1; i <= aArmTerminal_characteristic.getMemberCount(); i++) {
           armTerminal_characteristic = (ERepresentation_item)aArmTerminal_characteristic.getByIndex(i);
           armTerminal_characteristic.setName(null, "terminal characteristic");
           representation.getItems(null).addUnordered(armTerminal_characteristic);
        }
     }
  }

  /**
   * Unsets/deletes data for terminal_characteristic attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetTerminal_characteristic(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws
     SdaiException {
     //property_definition
     EProperty_definition property_definition = null;
     AProperty_definition aProperty_definition = new AProperty_definition();
     CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
     for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
        property_definition = aProperty_definition.getByIndex(i);

        //property_definition_representation
        EProperty_definition_representation property_definition_representation = null;
        AProperty_definition_representation aProperty_definition_representation = new
           AProperty_definition_representation();
        CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain,
                                                             aProperty_definition_representation);
        l1: for (int j = 1; j <= aProperty_definition_representation.getMemberCount();) {
           property_definition_representation = aProperty_definition_representation.getByIndex(j);
           //representation
           if (property_definition_representation.testUsed_representation(null)) {
              ERepresentation representation = property_definition_representation.getUsed_representation(null);
              if (representation.testItems(null)) {
                 ARepresentation_item aItem = representation.getItems(null);
                 ERepresentation_item item = null;

                 int k = 1;
                 while (k <= aItem.getMemberCount()) {
                    item = aItem.getByIndex(k);
                    AApplied_group_assignment aApplied_group_assignment = new AApplied_group_assignment();
                    CApplied_group_assignment.usedinItems(null, item, context.domain, aApplied_group_assignment);

                    if ((aApplied_group_assignment.getMemberCount() > 0)&&
						  (item.getName(null).equals("terminal characteristic"))){
                       aItem.removeUnordered(item);
                    }
                    else {
                       k++;
                    }
                 }
                 if((!representation.testItems(null))||(representation.getItems(null).getMemberCount() == 0)){
                    AEntity temp = new AEntity();
                    representation.findEntityInstanceUsers(context.domain, temp);
                    // if the only user is PDR, we reached this R from - then remove it
                    if(temp.getMemberCount() == 1){
                       representation.deleteApplicationInstance();
                       aProperty_definition_representation.removeByIndex(j);
                       property_definition_representation.deleteApplicationInstance();
                       continue l1;
                    }
                 }
              }
           }
           j++;
        }
     }
  }


  /**
   * Sets/creates data for terminal_diametrical_extent attribute.
   *
   * <p>
   *  attribute_mapping minimum_terminal_diametrical_extent_length_data_element (minimum_terminal_diametrical_extent
   * , (*PATH*), length_data_element);
   * 	package_terminal <=
   * 	shape_aspect
   * 	shape_definition = shape_aspect
   * 	shape_definition
   * 	characterized_definition = shape_definition
   * 	characterized_definition <-
   * 	property_definition.definition
   * 	property_definition <-
   * 	property_definition_representation.definition
   * 	property_definition_representation
   * 	property_definition_representation.used_representation ->
   * 	representation
   * 	representation.items[i] ->
   * 	{representation_item
   * 	representation_item.name = `minimum terminal diametrical extent'}
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
  public static void setTerminal_diametrical_extent(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws
     SdaiException {
     //unset old values
     unsetTerminal_diametrical_extent(context, armEntity);

     if (armEntity.testTerminal_diametrical_extent(null)) {
      ETolerance_characteristic characteristic = armEntity.getTerminal_diametrical_extent(null);
      
      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "terminal diametrical extent", characteristic);
     }
  }

  /**
   * Unsets/deletes data for terminal_diametrical_extent attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetTerminal_diametrical_extent(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws
     SdaiException {
  		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "terminal diametrical extent");
  }

  /**
   * Sets/creates data for seating_plane_zone attribute.
   *
   * <p>
   *  attribute_mapping seating_plane_zone_connection_zone_interface_plane_relationship (seating_plane_zone
   * , (*PATH*), connection_zone_interface_plane_relationship);
   * 	package_terminal <=
   * 	shape_aspect <-
   * 	shape_aspect_relationship.related_shape_aspect
   * 	{shape_aspect_relationship
   * 	shape_aspect_relationship.name = `seating plane zone'}
   * 	shape_aspect_relationship
   * 	shape_aspect_relationship.relating_shape_aspect ->
   * 	shape_aspect =>
   * 	connection_zone_interface_plane_relationship
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setSeating_plane_zone(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
     //unset old values
     unsetSeating_plane_zone(context, armEntity);

     if (armEntity.testSeating_plane_zone(null)) {

        EConnection_zone_interface_plane_relationship armSeating_plane_zone = armEntity.getSeating_plane_zone(null);

        // PDS
        LangUtils.Attribute_and_value_structure[] pdStructure = {
           new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), 
           		armEntity),
        };

        EProperty_definition property_definition = (EProperty_definition) LangUtils.createInstanceIfNeeded(
           context,
           CProduct_definition_shape.definition,
           pdStructure);

        if (!property_definition.testName(null)) {
           property_definition.setName(null, "");
        }
        // SA
        LangUtils.Attribute_and_value_structure[] saStructure = {
           new LangUtils.Attribute_and_value_structure(CShape_aspect.attributeOf_shape(null), 
           		armEntity)
        };

        EShape_aspect esa = (EShape_aspect) LangUtils.createInstanceIfNeeded(
           context,
           CShape_aspect.definition,
           saStructure);
        if (!esa.testName(null)) {
           esa.setName(null, "");
        }
        if (!esa.testProduct_definitional(null)) {
        		esa.setProduct_definitional(null, ELogical.UNKNOWN);
        }

        //shape_aspect_relationship
        EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.
           createEntityInstance(EShape_aspect_relationship.class);
        shape_aspect_relationship.setName(null, "seating plane zone");
        shape_aspect_relationship.setRelated_shape_aspect(null, esa);
        shape_aspect_relationship.setRelating_shape_aspect(null, armSeating_plane_zone);
     }
  }

  /**
   * Unsets/deletes data for seating_plane_zone attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetSeating_plane_zone(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
   //property_definition
   EProperty_definition property_definition = null;
   AProperty_definition aProperty_definition = new AProperty_definition();
   CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
   for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
      property_definition = aProperty_definition.getByIndex(i);
      if(property_definition instanceof EProduct_definition_shape){
         AShape_aspect asa = new AShape_aspect();
         CShape_aspect.usedinOf_shape(null, (EProduct_definition_shape)property_definition, context.domain, asa);
   	   for (int j = 1; j <= asa.getMemberCount(); j++) {
   	      EShape_aspect esa = asa.getByIndex(j);
   	      AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
   	      CShape_aspect_relationship.usedinRelating_shape_aspect(null, esa, context.domain, aRelationship);
   	      EShape_aspect_relationship relationship = null;
   	      for (int k = 1; k <= aRelationship.getMemberCount(); k++) {
   	         relationship = aRelationship.getByIndex(k);
   	         if (relationship.testName(null) && relationship.getName(null).equals("seating plane zone")) {
   	            relationship.deleteApplicationInstance();
   	         }
   	      }
   	      
   	   }	      	
      }
   }
  }

///////////////////////////////////////
//New since WD26
///////////////////////////////////////
  /**
   * Sets/creates data for seating_plane_intersection attribute.
   *
   * <p>
   *  attribute_mapping seating_plane_zone_connection_zone_interface_plane_relationship (seating_plane_zone
   * , (*PATH*), connection_zone_interface_plane_relationship);
			package_terminal_template_definition <=
			part_feature_template_definition <=
			feature_definition <=
			characterized_object
			characterized_definition = characterized_object
			characterized_definition <-
			property_definition.definition
			{property_definition.name = 'seating plane intersection'}
			{(property_definition.description = 'surface intersection')
			(property_definition.description = 'through intersection')
			(property_definition.description = 'does not intersect')}
			property_definition.description
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // PT <- PD 
  public static void setSeating_plane_intersection(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
     //unset old values
     unsetSeating_plane_intersection(context, armEntity);

     if (armEntity.testSeating_plane_intersection(null)) {

        int armSeating_plane_intersection = armEntity.getSeating_plane_intersection(null);
        String keyword = ESeating_plane_intersection_type.toString(armSeating_plane_intersection).toLowerCase().replace('_', ' ');
        EProperty_definition epd = (EProperty_definition)context.working_model.createEntityInstance(CProperty_definition.definition);
        epd.setName(null, "seating plane intersection");
        epd.setDescription(null, keyword);
        epd.setDefinition(null, armEntity);
     }
  }

  /**
   * Unsets/deletes data for seating_plane_intersection attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  	public static void unsetSeating_plane_intersection(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
  		AProperty_definition apd = new AProperty_definition();
  		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
  		for(int i=1;i<=apd.getMemberCount();i++){
  			EProperty_definition epd = apd.getByIndex(i);
  			if((epd.testName(null))&&(epd.getName(null).equals("seating plane intersection")))
  				epd.deleteApplicationInstance();
     }
  }

  	
    /**
     * Sets/creates data for lead_form attribute.
     *
     * <p>
     *  attribute_mapping lead_form(lead_form, $PATH, Predefined_lead_form);
		  	package_terminal_template_definition <=
		  	part_feature_template_definition <=
		  	shape_feature_definition <=
		  	characterized_object
		  	characterized_definition = characterized_object
		  	characterized_definition <-
		  	property_definition.definition
		  	property_definition
		  	{property_definition.name = 'predefined lead form'}
		  	{(property_definition.description = 'gull wing')
		  	(property_definition.description = 'j lead')
		  	(property_definition.description = 'reversed j lead')
		  	(property_definition.description = 'integral terminal')
		  	(property_definition.description = 'ball')
		  	(property_definition.description = 'undefined')
		  	(property_definition.description = 'straight')}
		  	property_definition.description
  		end_attribute_mapping;
     * </p>
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    // PT <- PD 
    public static void setLead_form(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
       //unset old values
       unsetLead_form(context, armEntity);

       if (armEntity.testLead_form(null)) {

          int armSeating_plane_intersection = armEntity.getLead_form(null);
          String keyword = ESeating_plane_intersection_type.toString(armSeating_plane_intersection).toLowerCase().replace('_', ' ');
          EProperty_definition epd = (EProperty_definition)context.working_model.createEntityInstance(CProperty_definition.definition);
          // kind of workarround
          if(keyword.equals("unset")){
        	  keyword = "undefined";
          }
          epd.setName(null, "predefined lead form");
          epd.setDescription(null, keyword);
          epd.setDefinition(null, armEntity);
       }
    }

    /**
     * Unsets/deletes data for seating_plane_intersection attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void unsetLead_form(SdaiContext context, EPackage_terminal_template_definition_armx armEntity) throws SdaiException {
    	AProperty_definition apd = new AProperty_definition();
    	CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
    	for(int i=1;i<=apd.getMemberCount();i++){
    		EProperty_definition epd = apd.getByIndex(i);
    		if((epd.testName(null))&&(epd.getName(null).equals("predefined lead form")))
    			epd.deleteApplicationInstance();
    	}
    }
	
}