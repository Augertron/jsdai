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

package jsdai.SContextual_shape_positioning_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxGeometric_placement_operation extends CGeometric_placement_operation implements EMappedXIMEntity, RepresentationMapAttributesImplementer
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Mapped_item
	// attribute (current explicit or supertype explicit) : mapping_source, base type: entity representation_map
/*	public static int usedinMapping_source(EMapped_item type, ERepresentation_map instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}*/
	public boolean testMapping_source2(EMapped_item type) throws SdaiException {
		return test_instance(a1);
	}
	public ERepresentation_map getMapping_source2(EMapped_item type) throws SdaiException {
		a1 = get_instance(a1);
		return (ERepresentation_map)a1;
	}
	public void setMapping_source(EMapped_item type, ERepresentation_map value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetMapping_source(EMapped_item type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeMapping_source(EMapped_item type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from Mapped_item

	// Taken from Representation_item
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_item type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation_item type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation_item type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation_item type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_item type) throws SdaiException {
		return a0$;
	}
	// ENDOF Taken from Representation_item
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMapped_item.definition);

		setMappingConstraints(context, this);

      // source 		
		setSource(context, this);
		
      // template_definition 		
		setTemplate_definition(context, this);
		
		// clean ARM
      // source 		
		unsetSource(null);
		
      // associated_shape_definition 		
		unsetTemplate_definition(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      // source 		
		unsetSource(context, this);
		
      // associated_shape_definition 		
		unsetTemplate_definition(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{mapped_item &lt;=
	*  representation_item
	*  representation_item.name = 'ucur'}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EGeometric_placement_operation armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EGeometric_placement_operation armEntity) throws SdaiException
	{
	}


   //********** "Geometric_placement_operation" attributes


  /**
   * Because of the mapping limitations - it is not possible to have different
   * unset metthods for attributes: associating_non_feature_shape_usage,
   * associating_usage_shape and associated_usage. So we have only one "unsetter".
   * I'm not sure if this is longer true - we have different unsetters - so comment out it
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // MI <- R ..., Remove ALL users of Mapped ITEM
  public static void unsetUsedin_representations(SdaiContext context, EGeometric_placement_operation armEntity) throws
     SdaiException {
     jsdai.SRepresentation_schema.ARepresentation representations = new jsdai.SRepresentation_schema.ARepresentation();
     jsdai.SRepresentation_schema.CRepresentation.usedinItems(null, armEntity,
                                                              context.domain, representations);
     for (int i = 1; i <= representations.getMemberCount(); i++) {
        jsdai.SRepresentation_schema.ERepresentation representation =
           representations.getByIndex(i);
        representation.getItems(null).removeUnordered(armEntity);
     }
  }

  /**
   * Sets/creates data for template_definition attribute.
   *
   *
   *  attribute_mapping template_definition (template_definition
   * , (*PATH*), geometric_model);
   * 	mapped_item
   * 	mapped_item.mapping_source ->
   * 	representation_map
   * 	representation_map.mapped_representation ->
   * 	representation =>
   * 	shape_representation
   *  end_attribute_mapping;
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // MI -> RM -> SR
  public static void setTemplate_definition(SdaiContext context,
                                                            EGeometric_placement_operation armEntity) throws
     SdaiException {
     // No need for unset - overwrite strategy is used
     // unsetAssociated_feature_shape_definition(context, armEntity);

     if (armEntity.testTemplate_definition(null)) {
        EShape_representation armShape = (EShape_representation)armEntity.getTemplate_definition(null);
        // GET SR in AIM
		//EA armAssociated_feature_shape_definition.createAimData(context);
        // MI -> RM
                
        LangUtils.Attribute_and_value_structure[] rmStructure =
        {new LangUtils.Attribute_and_value_structure(
         CRepresentation_map.attributeMapped_representation(null),
			armShape)
        };
        ERepresentation_map map = (ERepresentation_map)
        LangUtils.createInstanceIfNeeded(context, CRepresentation_map.definition, rmStructure);
        
        armEntity.setMapping_source(null, map);
        if (map.testMapping_origin(null))
        	return;
        // AIM gaps
        if (armShape.testItems(null)) {
           ARepresentation_item items = armShape.getItems(null);
           for (int i = 1; i <= items.getMemberCount(); i++) {
              if (items.getByIndex(i)instanceof jsdai.SGeometry_schema.EPlacement) {
                 map.setMapping_origin(null, items.getByIndex(i));
                 break;
              }
           }
        }
     }
  }

  /**
   * Unsets/deletes data for associated_feature_shape_definition attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // MI - RM -> SR, break here
  public static void unsetTemplate_definition(SdaiContext context,
      EGeometric_placement_operation armEntity) throws
     SdaiException {
	 RepresentationMapAttributesImplementer temp = (RepresentationMapAttributesImplementer)armEntity;
     if (temp.testMapping_source2(null)) {
        ERepresentation_map map = temp.getMapping_source2(null);
        armEntity.unsetMapping_source(null);
        if (CxAP210ARMUtilities.countEntityUsers(context, map) == 0) {
           map.deleteApplicationInstance();
        }
     }
  }

  public static void setSource(SdaiContext context, EGeometric_placement_operation armEntity)throws SdaiException{
  	unsetSource(context, armEntity);
  	
  	if(armEntity.testSource(null)){
  		RepresentationMapAttributesImplementer temp = (RepresentationMapAttributesImplementer)armEntity;
  		// Safety check
	  	if(!temp.testMapping_source2(null))
	  		CxGeometric_placement_operation.setTemplate_definition(context, armEntity);
	  	ERepresentation_map map = temp.getMapping_source2(null);
	  	ERepresentation_item origin = (ERepresentation_item)armEntity.getSource(null);
/*	
	      jsdai.SGeometry_schema.EAxis2_placement_2d origin = (jsdai.SGeometry_schema.EAxis2_placement_2d)
	         context.working_model.createEntityInstance(jsdai.SGeometry_schema.CAxis2_placement_2d.class);
	      origin.setName(null, "origin");
	      // Placement
	      jsdai.SGeometry_schema.ECartesian_point location = (jsdai.SGeometry_schema.ECartesian_point)
	         context.working_model.createEntityInstance(jsdai.SGeometry_schema.CCartesian_point.class);
	      A_double coords = location.createCoordinates(null);
	      coords.addByIndex(1, 0); // X
	      coords.addByIndex(2, 0); // Y 
	      location.setName(null, "");
	      // AXIS -> Placement
	      origin.setLocation(null, location);*/
	      // RM -> AXIS
	      map.setMapping_origin(null, origin);
	      // SR -> AXIS
         EShape_representation armShape = (EShape_representation)armEntity.getTemplate_definition(null);
	      ARepresentation_item items;
	      if (armShape.testItems(null)) {
	         items = armShape.getItems(null);
	      }
	      else {
	         items = armShape.createItems(null);
	      }
	      if(!items.isMember(origin)){
	    	  items.addUnordered(origin);
	      }
  	}
  }
  
  public static void unsetSource(SdaiContext context, EGeometric_placement_operation armEntity)throws SdaiException{
	  RepresentationMapAttributesImplementer temp = (RepresentationMapAttributesImplementer)armEntity;
	  	if(temp.testMapping_source2(null)){
	  		ERepresentation_map map = temp.getMapping_source2(null);
	  		map.unsetMapping_origin(null);
	  	}
  }
  
}
