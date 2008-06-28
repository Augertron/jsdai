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
import jsdai.util.LangUtils;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxNon_feature_shape_model extends CNon_feature_shape_model implements EMappedXIMEntity
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
	* 	{[{shape_representation &lt;=
	* 	representation &lt;-
	* 	property_definition_representation.used_representation
	* 	property_definition_representation
	* 	property_definition_representation.definition -&gt;
	* 	property_definition
	* 	property_definition.definition -&gt;
	* 	characterized_definition
	* 	characterized_definition = shape_definition
	* 	shape_definition
	* 	shape_definition = shape_aspect
	* 	shape_aspect
	* 	{shape_aspect
	* 	shape_aspect.product_definitional = .FALSE.}}]
	* 	[shape_representation &lt;=
	* 	representation
	* 	(representation.id ='nfsd')
	* 	(representation.id ='vlnfsd')]}	
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
		CxAP210ARMUtilities.setRepresentationId(context, armEntity, "nfsm");
	}

	public static void unsetMappingConstraints(SdaiContext context, ENon_feature_shape_model armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.setRepresentationId(context, armEntity, "");
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
      //unset old values
      unsetAssociated_element(context, armEntity);

      if (armEntity.testAssociated_element(null)) {

         ENon_feature_shape_element armAssociated_element = armEntity.getAssociated_element(null);
         // SA
         // PD -> SA
         LangUtils.Attribute_and_value_structure[] pdStructure = {
            new LangUtils.Attribute_and_value_structure(
            CProperty_definition.attributeDefinition(null),
				armAssociated_element)
         };
         EProperty_definition epd = (EProperty_definition)
            LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
         if (!epd.testName(null)) {
            epd.setName(null, "");
            // R <- PDR -> PD
         }
         LangUtils.Attribute_and_value_structure[] pdrStructure = {
            new LangUtils.Attribute_and_value_structure(
            CProperty_definition_representation.attributeDefinition(null),
            epd),
            new LangUtils.Attribute_and_value_structure(
            CProperty_definition_representation.attributeUsed_representation(null),
            armEntity)
         };
         //EProperty_definition_representation epdr = (EProperty_definition_representation)
            LangUtils.createInstanceIfNeeded(context, CProperty_definition_representation.definition, pdrStructure);
      }
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
     AProperty_definition_representation relationships = new AProperty_definition_representation();
     CProperty_definition_representation.usedinUsed_representation(null,
                                                                   armEntity, context.domain, relationships);
     for (int i = 1; i <= relationships.getMemberCount();i++ ) {
        EProperty_definition_representation relationship = relationships.getByIndex(i);
        EEntity definition = relationship.getDefinition(null);
        if(definition instanceof EProperty_definition){
        	EProperty_definition epd = (EProperty_definition)definition;
        	EEntity definition2 = epd.getDefinition(null);
        	if(definition2 instanceof ENon_feature_shape_element){
            // I hope it is not too severe
            relationship.deleteApplicationInstance();
        	}
        }
     }
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
     //unset old values
     unsetModel_shape(context, armEntity);

     if (armEntity.testModel_shape(null)) {
        EShape_representation armModel_shape = armEntity.getModel_shape(null);
//EA         armModel_shape.createAimData(context);

        LangUtils.Attribute_and_value_structure[] relationshipStructure = {
           new LangUtils.Attribute_and_value_structure(
           CRepresentation_relationship.attributeRep_1(null),
           armModel_shape),
           new LangUtils.Attribute_and_value_structure(
           CRepresentation_relationship.attributeRep_2(null),
           armEntity),
           new LangUtils.Attribute_and_value_structure(
           CRepresentation_relationship.attributeName(null),
           "model shape")
        };
        //ERepresentation_relationship relationship = (ERepresentation_relationship)
           LangUtils.createInstanceIfNeeded(context,
                                            CRepresentation_relationship.definition, relationshipStructure);

     }
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
     ARepresentation_relationship relationships = new ARepresentation_relationship();
     CRepresentation_relationship.usedinRep_2(null, armEntity, context.domain, relationships);
     for (int i = 1; i <= relationships.getMemberCount(); ) {
        ERepresentation_relationship temp = relationships.getByIndex(i);
        if ( (temp.testName(null)) && (temp.getName(null).equals("model shape"))) {
           relationships.removeByIndex(i);
           temp.deleteApplicationInstance();
        }
        else {
           i++;
        }
     }
  }
	

	
}
