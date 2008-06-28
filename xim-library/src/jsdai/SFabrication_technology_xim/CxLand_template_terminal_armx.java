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

package jsdai.SFabrication_technology_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFabrication_technology_mim.CLand_template_terminal;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SProduct_property_definition_schema.*;

public class CxLand_template_terminal_armx extends CLand_template_terminal_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	// ENDOF From CShape_aspect.java


	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLand_template_terminal.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
      // connection_zone_category 
		setConnection_zone_category(context, this);
		
		// Clean ARM
		// Connection_area
		unsetConnection_area(null);
		
      // connection_zone_category 
		unsetConnection_zone_category(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
	      // connection_zone_category 
			unsetConnection_zone_category(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  land_template_terminal &lt;=
	 *  shape_aspect
	 *  {[shape_aspect
	 *  (shape_aspect.description = 'interface terminal')
	 *  (shape_aspect.description = 'join terminal')]
	 *  [shape_aspect
	 *  shape_aspect.of_shape -&gt;
	 *  product_definition_shape &lt;=
	 *  property_definition
	 *  property_definition.definition -&gt;
	 *  characterized_definition
	 *  characterized_definition = characterized_product_definition
	 *  characterized_product_definition
	 *  characterized_product_definition = product_definition
	 *  product_definition
	 *  [product_definition
	 *  product_definition.formation -&gt;
	 *  product_definition_formation
	 *  product_definition_formation.of_product -&gt;
	 *  product &lt;-
	 *  product_related_product_category.products[i]
	 *  product_related_product_category &lt;=
	 *  product_category
	 *  product_category.name = 'template model']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'template definition']]}
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
			ELand_template_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_feature.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ELand_template_terminal_armx armEntity) throws SdaiException {
		CxShape_feature.unsetMappingConstraints(context, armEntity);
	}


   //********** "land_template_terminal" attributes

 /**
  * Sets/creates data for Connection_area attribute.
  *
  */
 public static void setConnection_area(SdaiContext context, ELand_template_terminal_armx armEntity) throws
    SdaiException {
	CxShape_feature.setConnection_area(context, armEntity);
 }

 /**
  * Unsets/deletes data for Connection_area attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetConnection_area(SdaiContext context, ELand_template_terminal_armx armEntity) throws
    SdaiException {
	CxShape_feature.unsetConnection_area(context, armEntity);
 }

 /**
  * Sets/creates data for connection_zone_category attribute.
  *
  * <p>
  *  attribute_mapping connection_zone_category (connection_zone_category
  * , shape_aspect_relationship.description);
  * 	land_template_terminal <=
  * 	shape_aspect <-
  * 	shape_aspect_relationship.relating_shape_aspect
  * 	{shape_aspect_relationship
  * 	shape_aspect_relationship.name = `terminal connection zone'}
  * 	shape_aspect_relationship
  * 	shape_aspect_relationship.description
  * 	{(shape_aspect_relationship.description = `edge curve')
  * 	(shape_aspect_relationship.description = `edge point')
  * 	(shape_aspect_relationship.description =  `surface area')
  * 	(shape_aspect_relationship.description = `surface point')}
  *  end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setConnection_zone_category(SdaiContext context, ELand_template_terminal_armx armEntity) throws
    SdaiException {
    if (armEntity.testConnection_zone_category(null)) {
       //unset old values
       unsetConnection_zone_category(context, armEntity);

       A_enumeration category = armEntity.getConnection_zone_category(null);
       // All connection_zones must be of the same cateogries - only this is implicitly allowed by the model
       AShape_aspect_relationship relationships = new AShape_aspect_relationship();
       CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
       if(relationships.getMemberCount() == 0)
       	throw new SdaiException(SdaiException.EI_NVLD, armEntity+" is violating land_template_terminal WR1 ");
       for (int i = 1; i <= relationships.getMemberCount(); i++) {
          EShape_aspect_relationship sar = relationships.getByIndex(i);
          if ( (sar.testName(null)) && (sar.getName(null).equals("terminal connection zone"))) {
          	String valueToSet = ELand_template_terminal_class.toString(category.getByIndexInt(i)).toLowerCase().replace('_', ' ');
          	valueToSet = valueToSet.substring(0, valueToSet.length()-6);
             sar.setDescription(null, valueToSet);
          }
       }
       
       // Workarround for the problem - when there's no connection_zones
       // SEDS-739
       // It is made OPTIONAL - no need to support case, when there's no connection_zones
/*         if (relationships.getMemberCount() == 0) {
          EShape_aspect_relationship relationship = (EShape_aspect_relationship)
             context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
          relationship.setName(null, "terminal connection zone");
          relationship.setDescription(null,
                                      ELand_template_terminal_class.toString(category).toLowerCase().replace('_', ' '));
          relationship.setRelated_shape_aspect(null, aimEntity);
          relationship.setRelating_shape_aspect(null, aimEntity);
       }*/
    }
 }

 /**
  * Unsets/deletes data for connection_zone_category attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetConnection_zone_category(SdaiContext context, ELand_template_terminal_armx armEntity) throws
    SdaiException {
    if (armEntity.testConnection_zone_category(null)) {
       // All connection_zones must be of the same cateogries - only this is implicitly allowed by the model
       AShape_aspect_relationship relationships = new AShape_aspect_relationship();
       CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
       for (int i = 1; i <= relationships.getMemberCount(); ) {
          EShape_aspect_relationship sar = relationships.getByIndex(i);
          if ( (sar.testName(null)) && (sar.getName(null).equals("terminal connection zone"))) {
             sar.unsetDescription(null);
             if (sar.getRelated_shape_aspect(null) == armEntity) {
                relationships.removeByIndex(i);
                sar.deleteApplicationInstance();
             }
          }
          i++;
       }
    }
 }
	
}