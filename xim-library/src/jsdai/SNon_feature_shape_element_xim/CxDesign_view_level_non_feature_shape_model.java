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

package jsdai.SNon_feature_shape_element_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_property_representation_schema.*;

public class CxDesign_view_level_non_feature_shape_model extends CDesign_view_level_non_feature_shape_model implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

      setAssociated_element(context, this);
      setModel_shape(context, this);

      unsetAssociated_element(null);
      unsetModel_shape(null);
      
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      unsetAssociated_element(context, this);
      unsetModel_shape(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  shape_representation &lt;=
	*  representation
	*  {representation.id = 'vlnfsd'}
	*  representation &lt;-
	*  representation_relationship.rep_2
	*  representation_relationship
	*  {representation_relationship
	*  representation_relationship.name = 'model shape'}
	*  representation_relationship.rep_1 -&gt;
	*  representation =&gt;
	*  shape_representation
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA will be set via NFSE
	// All the constraints are captured by setting attributes.
	// Some magic strings will come only at subtypes as this is ABSTRACT entity.
	public static void setMappingConstraints(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxNon_feature_shape_model.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationId(context, armEntity, "dvlnfsm");
	}

	public static void unsetMappingConstraints(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException
	{
		CxNon_feature_shape_model.unsetMappingConstraints(context, armEntity);

	}



   //********** "NFSD" attributes

   /**
    * Sets/creates data for associated_element attribute.
    *
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   // It is likely the mapping is: SR <- PDR -> PD -> SA
   public static void setAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws
      SdaiException {
   	CxNon_feature_shape_model.setAssociated_element(context, armEntity);   	
   }

  /**
   * Unsets/deletes data for associated_element attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // It is likely the mapping is: SR .- PDR -> PD -> SA, remove here
  //  I hope strategy is not too strict
  public static void unsetAssociated_element(SdaiContext context, ENon_feature_shape_model armEntity) throws
     SdaiException {
  		CxNon_feature_shape_model.unsetAssociated_element(context, armEntity);
  }

  /**
   * Sets/creates data for model_shape attribute.
   *
   *
   *  attribute_mapping model_shape_feature_shape_definition (model_shape
   * , (*PATH*), feature_shape_definition);
   * 	shape_representation <=
   * 	representation <-
   * 	representation_relationship.rep_2
   * 	representation_relationship
   * 	{representation_relationship
   * 	representation_relationship.name = `model shape'}
   * 	representation_relationship.rep_1 ->
   * 	representation =>
   * 	shape_representation
   *  end_attribute_mapping;
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- RR -> SR
  public static void setModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException {
  		CxNon_feature_shape_model.setModel_shape(context, armEntity);  	
  }

  /**
   * Unsets/deletes data for model_shape attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // SR <- RR -> SR - remove all RRs
  public static void unsetModel_shape(SdaiContext context, ENon_feature_shape_model armEntity) throws
     SdaiException {
  		CxNon_feature_shape_model.unsetModel_shape(context, armEntity);  	
  }
	

	
}
