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

package jsdai.SBare_die_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SBare_die_mim.CBare_die_template_terminal;
import jsdai.SFeature_and_connection_zone_mim.EConnection_zone_interface_plane_relationship;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxBare_die_template_terminal_armx extends CBare_die_template_terminal_armx implements EMappedXIMEntity{


	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a7);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a7);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a7 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a7 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a7$;
	}
	// ENDOF From CShape_aspect.java
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CBare_die_template_terminal.definition);

		setMappingConstraints(context, this);
		
		// external_connection_area
		setConnection_area(context, this);
		
      // seating_plane_zone		
		setSeating_plane_zone(context, this);
		
		// Clean ARM

		// external_connection_area
		unsetConnection_area(null);
		
      // seating_plane_zone		
		unsetSeating_plane_zone(null);
		
		// unsetFeature_model(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// external_connection_area
			unsetConnection_area(context, this);
			
	      // seating_plane_zone		
			unsetSeating_plane_zone(context, this);
			
			// unsetFeature_model(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  Minimally_defined_bare_die_terminal
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
			EBare_die_template_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxShape_feature.setMappingConstraints(context, armEntity);
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
			EBare_die_template_terminal_armx armEntity) throws SdaiException {
		CxPart_feature_template_definition_armx.unsetMappingConstraints(context, armEntity);
		CxShape_feature.setMappingConstraints(context, armEntity);
	}



	
////////////////////////////////////////////////////////////////////
	//********** "bare_die_terminal" attributes

	/**
	* Sets/creates data for external_connection_area attribute.
	*
	* <p>
	*  attribute_mapping external_connection_area_connection_zone (external_connection_area
	* , (*PATH*), connection_zone);
	* 	bare_die_terminal <=
	* 	minimally_defined_bare_die_terminal <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `external connection area'}
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
	public static void setConnection_area(SdaiContext context, EBare_die_template_terminal_armx armEntity) throws SdaiException
	{
		CxShape_feature.setConnection_area(context, armEntity);
	}


	/**
	* Unsets/deletes data for external_connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EBare_die_template_terminal_armx armEntity) throws SdaiException
	{
		CxShape_feature.unsetConnection_area(context, armEntity);
	}


	/**
	* Sets/creates data for seating_plane_zone attribute.
	*
	* <p>
	*  attribute_mapping seating_plane_zone_connection_zone_interface_plane_relationship (seating_plane_zone
	* , (*PATH*), connection_zone_interface_plane_relationship);
	* 	bare_die_terminal <=
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
	public static void setSeating_plane_zone(SdaiContext context, EBare_die_template_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetSeating_plane_zone(context, armEntity);

		if (armEntity.testSeating_plane_zone(null))
		{
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
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
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
	public static void unsetSeating_plane_zone(SdaiContext context, EBare_die_template_terminal_armx armEntity) throws SdaiException
	{
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
	
	
}