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
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SGroup_schema.EGroup;
import jsdai.SPackage_mim.CWire_terminal_template_definition;

public class CxWire_terminal_template_definition_armx extends CWire_terminal_template_definition_armx implements EMappedXIMEntity{


	public int attributeState = ATTRIBUTES_MODIFIED;	

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
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CWire_terminal_template_definition.definition);

		setMappingConstraints(context, this);
		
		// Part_feature
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
		
		// Wire_terminal_length
		setWire_terminal_length(context, this);
		
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

		// Wire_terminal_length
		unsetWire_terminal_length(null);

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
			
			// wire_terminal_length
			unsetWire_terminal_length(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  Wire_terminal &lt;=
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
			EWire_terminal_template_definition_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPackage_terminal_template_definition_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EWire_terminal_template_definition_armx armEntity) throws SdaiException {
		CxPackage_terminal_template_definition_armx.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for material_state_change.
	 * 
	 * <p>
	 *  shape_aspect &lt;-
	 *  shape_aspect_relationship.related_shape_aspect
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'precedent feature'}
	 *  shape_aspect_relationship.relating_shape_aspect -&gt;
	 *  shape_aspect
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
////////////////////////////////////////////////////////////////////
   //********** "package_terminal" attributes

   /**
    * Sets/creates data for external_connection_zone attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setExternal_connection_zone(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws
      SdaiException {
   	CxPackage_terminal_template_definition_armx.setExternal_connection_zone(context, armEntity);
   }

  /**
   * Unsets/deletes data for external_connection_zone attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetExternal_connection_zone(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws
     SdaiException {
  	CxPackage_terminal_template_definition_armx.setExternal_connection_zone(context, armEntity);  	
  }

  /**
   * Sets/creates data for internal_connection_zone attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setInternal_connection_zone(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws
     SdaiException {
  	CxPackage_terminal_template_definition_armx.setInternal_connection_zone(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for internal_connection_zone attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetInternal_connection_zone(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws
     SdaiException {
  	CxPackage_terminal_template_definition_armx.unsetInternal_connection_zone(context, armEntity);  	
  }

  /**
   * Sets/creates data for terminal_characteristic attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setTerminal_characteristic(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws SdaiException {
  	CxPackage_terminal_template_definition_armx.setTerminal_characteristic(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for terminal_characteristic attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetTerminal_characteristic(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws
     SdaiException {
  	CxPackage_terminal_template_definition_armx.unsetTerminal_characteristic(context, armEntity);  	
  }


  /**
   * Sets/creates data for terminal_diametrical_extent attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setTerminal_diametrical_extent(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws
     SdaiException {
  	CxPackage_terminal_template_definition_armx.setTerminal_diametrical_extent(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for terminal_diametrical_extent attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetTerminal_diametrical_extent(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws
     SdaiException {
  	CxPackage_terminal_template_definition_armx.unsetTerminal_diametrical_extent(context, armEntity);  	
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
  public static void setSeating_plane_zone(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws SdaiException {
  	CxPackage_terminal_template_definition_armx.setSeating_plane_zone(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for seating_plane_zone attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetSeating_plane_zone(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws SdaiException {
  	CxPackage_terminal_template_definition_armx.unsetSeating_plane_zone(context, armEntity);  	
  }

///////////////////////////////////////
//New since WD26
///////////////////////////////////////
  /**
   * Sets/creates data for seating_plane_intersection attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // PT <- PD <- PDR -> R -> DRI
  public static void setSeating_plane_intersection(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws SdaiException {
  	CxPackage_terminal_template_definition_armx.setSeating_plane_intersection(context, armEntity);  
  }

  /**
   * Unsets/deletes data for seating_plane_intersection attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // strategy - remove suitable DRIs only
  public static void unsetSeating_plane_intersection(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws SdaiException {
		CxPackage_terminal_template_definition_armx.unsetSeating_plane_intersection(context, armEntity);
  }


	//********** "wire_terminal" attributes

	/**
	* Sets/creates data for wire_terminal_length attribute.
	*
	* <p>
	*  attribute_mapping maximum_wire_terminal_length_length_data_element (maximum_wire_terminal_length
	* , (*PATH*), length_data_element);
	* 	wire_terminal <=
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
	* 	representation_item.name = `maximum wire terminal length'}
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
	public static void setWire_terminal_length(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetWire_terminal_length(context, armEntity);

		if (armEntity.testWire_terminal_length(null)){
	      ETolerance_characteristic characteristic = armEntity.getWire_terminal_length(null);
	      
	      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "wire terminal length", characteristic);
		}
	}


	/**
	* Unsets/deletes data for maximum_wire_terminal_length attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetWire_terminal_length(SdaiContext context, EWire_terminal_template_definition_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "wire terminal length");
	}
 
}