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

package jsdai.SPart_feature_location_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SContextual_shape_positioning_xim.*;
import jsdai.SPart_feature_location_mim.CUsage_concept_usage_relationship;
import jsdai.SProduct_property_representation_schema.EItem_identified_representation_usage;
import jsdai.SRepresentation_schema.*;

public class CxUsage_concept_usage_relationship_armx extends CUsage_concept_usage_relationship_armx implements EMappedXIMEntity, RepresentationMapAttributesImplementer
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Item_identified_representation_usage
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EItem_identified_representation_usage type) throws SdaiException {
		return test_string(a5);
	}
	public String getName(EItem_identified_representation_usage type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setName(EItem_identified_representation_usage type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetName(EItem_identified_representation_usage type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EItem_identified_representation_usage type) throws SdaiException {
		return a5$;
	}
	// ENDOF Taken from Item_identified_representation_usage	
	
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

		setTemp("AIM", CUsage_concept_usage_relationship.definition);

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
	public static void setMappingConstraints(SdaiContext context, EUsage_concept_usage_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxGeometric_placement_operation.setMappingConstraints(context, armEntity);
		// AIM gaps
//		if(!armEntity.testName((EItem_identified_representation_usage)null)){
			armEntity.setName((EItem_identified_representation_usage)null, "");
//		}
//		if(!armEntity.testName((ERepresentation_item)null)){
			armEntity.setName((ERepresentation_item)null, "");
//		}
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EUsage_concept_usage_relationship_armx armEntity) throws SdaiException
	{
		CxGeometric_placement_operation.unsetMappingConstraints(context, armEntity);
	}


   //********** "Geometric_placement_operation" attributes


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
	public static void setTemplate_definition(SdaiContext context, EGeometric_placement_operation armEntity) throws SdaiException
	{
		CxGeometric_placement_operation.setTemplate_definition(context, armEntity);
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
  	CxGeometric_placement_operation.unsetTemplate_definition(context, armEntity);  	
  }

  public static void setSource(SdaiContext context, EGeometric_placement_operation armEntity)throws SdaiException{
  	CxGeometric_placement_operation.setSource(context, armEntity);  	
  }
  
  public static void unsetSource(SdaiContext context, EGeometric_placement_operation armEntity)throws SdaiException{
  	CxGeometric_placement_operation.unsetSource(context, armEntity);  	
  }

///////////////////////////////////////

  // MI -> RM -> R
/* Now it is the same as template_definition  
  public static void setAssociated_shape_definition(SdaiContext context, EUsage_concept_usage_relationship_armx armEntity) throws
     SdaiException {
	  unsetAssociated_shape_definition(context, armEntity);
     if (armEntity.testAssociated_shape_definition(null)) {
        EShape_representation armShape = armEntity.getAssociated_shape_definition(null);
	  	if(!armEntity.testMapping_source(null))
	  		CxGeometric_placement_operation.setTemplate_definition(context, armEntity);
	  	ERepresentation_map map = armEntity.getMapping_source(null);
	  	map.setMapped_representation(null, armShape);
     }
  }

  public static void unsetAssociated_shape_definition(SdaiContext context, EUsage_concept_usage_relationship_armx armEntity) throws
     SdaiException {
	  	if(armEntity.testMapping_source(null)){
	  		ERepresentation_map map = armEntity.getMapping_source(null);
	  		map.unsetMapped_representation(null);
	  	}
  }
*/  
}
