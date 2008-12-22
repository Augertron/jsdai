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

package jsdai.SLayered_interconnect_complex_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_2d_shape_xim.CxPlanar_shape_model;
import jsdai.SNon_feature_shape_element_xim.*;
import jsdai.SPart_template_shape_with_parameters_xim.*;
import jsdai.SProduct_property_representation_schema.*;

public class CxPart_template_planar_keepout_shape_model extends CPart_template_planar_keepout_shape_model implements EMappedXIMEntity
{

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
		setShape_purpose(context, this);
		setConstrained_design_object_category(context, this);
      setAssociated_element(context, this);
      setModel_shape(context, this);
		
		
      unsetShape_characterized_definition(null);
      unsetShape_material_condition(null);      
      unsetCentroid_location(null);      
      unsetShape_environment(null);      
      unsetShape_purpose(null);
      unsetConstrained_design_object_category(null);
		
      unsetAssociated_element(null);
      unsetModel_shape(null);
      
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      unsetShape_characterized_definition(context, this);
		unsetShape_material_condition(context, this);      
		unsetCentroid_location(context, this);      
		unsetShape_environment(context, this);      
		unsetShape_purpose(context, this);
		unsetConstrained_design_object_category(context, this);

      unsetAssociated_element(context, this);
      unsetModel_shape(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	*  representation				
	*  [representation.id = 'nfsd']
	*  [representation.name = 'planar projected shape']
	*  [representation.description = 'ptpks']
	*  [representation &lt;-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  property_definition =&gt;
	*  product_definition_shape =&gt;
	*  part_template_definition]}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// All the constraints are captured by setting attributes.
	// Some magic strings will come only at subtypes as this is ABSTRACT entity.
	public static void setMappingConstraints(SdaiContext context, EPart_template_planar_keepout_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPlanar_shape_model.setMappingConstraints(context, armEntity);
		CxNon_feature_shape_model.setMappingConstraints(context, armEntity);
		CxPart_template_keepout_shape_model.setMappingConstraints(context, armEntity);
		
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "ptpksm");
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart_template_planar_keepout_shape_model armEntity) throws SdaiException
	{
		CxPlanar_shape_model.unsetMappingConstraints(context, armEntity);
		CxNon_feature_shape_model.unsetMappingConstraints(context, armEntity);
		CxPart_template_keepout_shape_model.unsetMappingConstraints(context, armEntity);
		
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "");
	}



   //********** "part_template_keepout_shape" attributes


  /**
   * Sets/creates data for constrained_design_object_category attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- PDR -> PD -> KDOC
  public static void setConstrained_design_object_category(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
  		CxPart_template_keepout_shape_model.setConstrained_design_object_category(context, armEntity);
  }

  /**
   * Unsets/deletes data for constrained_design_object_category attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // remove all PDRs of that kind and KDOC if unused, SR <- PDR -> KDOC
  public static void unsetConstrained_design_object_category(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
  	CxPart_template_keepout_shape_model.unsetConstrained_design_object_category(context, armEntity);
  }

  /**
   * Sets/creates data for shape_characterized_physical_unit attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- PDR -> PropD -> PU
  public static void setShape_characterized_definition(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
  		CxPart_template_keepout_shape_model.setShape_characterized_definition(context, armEntity);
  }

  /**
   * Unsets/deletes data for shape_characterized_physical_unit attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_characterized_definition(SdaiContext context,
     EPart_template_keepout_shape_model armEntity) throws SdaiException {
  		CxPart_template_keepout_shape_model.unsetShape_characterized_definition(context, armEntity);  	
  }


  /**
   * Sets/creates data for shape_material_condition attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_material_condition(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPart_template_keepout_shape_model.setShape_material_condition(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for shape_material_condition attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_material_condition(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPart_template_keepout_shape_model.unsetShape_material_condition(context, armEntity);  	
  }

  /**
   * Sets/creates data for centroid_location attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setCentroid_location(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPart_template_keepout_shape_model.setCentroid_location(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for centroid_location attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetCentroid_location(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  	CxPart_template_keepout_shape_model.unsetCentroid_location(context, armEntity);  	
  }

  /**
   * Sets/creates data for shape_environment attribute.
   *
   * <p>
   *  attribute_mapping shape_environment (shape_environment
   * , descriptive_representation_item);
   * 	shape_representation <=
   * 	representation <-
   * 	representation_relationship.rep_1
   * 	representation_relationship
   * 	representation_relationship.rep_2 ->
   * 	representation
   * 	{representation.name = `shape environment'}
   * 	representation.items[i] ->
   * 	representation_item
   * 	{(representation_item.name = `manufacturing')
   * 	(representation_item.name = `end user application')}
   * 	representation_item =>
   * 	descriptive_representation_item
   *  end_attribute_mapping;
   * </p>
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_environment(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPart_template_keepout_shape_model.setShape_environment(context, armEntity);
  }

  /**
   * Unsets/deletes data for shape_environment attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_environment(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPart_template_keepout_shape_model.unsetShape_environment(context, armEntity);  	
  }

  /**
   * Sets/creates data for shape_purpose attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setShape_purpose(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  	CxPart_template_keepout_shape_model.setShape_purpose(context, armEntity);
  }

  /**
   * Unsets/deletes data for shape_purpose attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetShape_purpose(SdaiContext context, EPart_template_keepout_shape_model armEntity) throws
     SdaiException {
  		CxPart_template_keepout_shape_model.unsetShape_purpose(context, armEntity);
  }

  // Non_feature_shape_definition
  /**
   * Sets/creates data for associated_element attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws
     SdaiException {
  		CxNon_feature_shape_model.setAssociated_element(context, armEntity);
  }

  /**
   * Unsets/deletes data for shape_purpose attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws
     SdaiException {
  		CxNon_feature_shape_model.unsetAssociated_element(context, armEntity);
  }

  /**
   * Sets/creates data for associated_element attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws
     SdaiException {
  		CxNon_feature_shape_model.setModel_shape(context, armEntity);
  }

  /**
   * Unsets/deletes data for shape_purpose attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws
     SdaiException {
  		CxNon_feature_shape_model.unsetModel_shape(context, armEntity);
  }
  
}
