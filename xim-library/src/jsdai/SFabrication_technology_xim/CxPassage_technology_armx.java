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
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SFabrication_technology_mim.CPassage_technology;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxPassage_technology_armx extends CPassage_technology_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a0$;
	}
	// ENDOF From CProperty_definition.java

	// FROM Characterized_object
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(ECharacterized_object type) throws SdaiException {
		return test_string(a4);
	}
	public String getDescription(ECharacterized_object type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(ECharacterized_object type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(ECharacterized_object type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(ECharacterized_object type) throws SdaiException {
		return a4$;
	}
	// ENDOF FROM Characterized_object	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPassage_technology.definition);

		setMappingConstraints(context, this);
		
		// as_finished_inter_stratum_extent 		
		// setAs_finished_inter_stratum_extent(context, this);
		
		// EX Parameters
      // plated_passage
		setPlated_passage(context, this);
      
		// as_finished_passage_extent
		setAs_finished_passage_extent(context, this);
		
      // as_finished_deposition_thickness
		setAs_finished_deposition_thickness(context, this);
		
		// maximum_aspect_ratio
		setMaximum_aspect_ratio(context, this);
		
		// passage_terminus_condition : OPTIONAL ft_terminus_condition;
		setPassage_terminus_condition(context, this);
		
		// Clean ARM
		// as_finished_inter_stratum_extent 		
		// unsetAs_finished_inter_stratum_extent(null);

      // plated_passage
		unsetPlated_passage(null);
      
		// as_finished_passage_extent
		unsetAs_finished_passage_extent(null);
		
      // as_finished_deposition_thickness
		unsetAs_finished_deposition_thickness(null);
		
		// maximum_aspect_ratio
		unsetMaximum_aspect_ratio(null);
		
		// passage_terminus_condition : OPTIONAL ft_terminus_condition;
		unsetPassage_terminus_condition(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// as_finished_inter_stratum_extent 		
		// unsetAs_finished_inter_stratum_extent(context, this);
		
      // plated_passage
		unsetPlated_passage(context, this);
      
		// as_finished_passage_extent
		unsetAs_finished_passage_extent(context, this);
		
      // as_finished_deposition_thickness
		unsetAs_finished_deposition_thickness(context, this);
		
		// maximum_aspect_ratio
		unsetMaximum_aspect_ratio(context, this);
		
		// passage_terminus_condition : OPTIONAL ft_terminus_condition;
		unsetPassage_terminus_condition(context, this);

		cleanAIM_stuff(context, this);

	}
	
   public static void cleanAIM_stuff(SdaiContext context, EPassage_technology_armx armEntity) throws
   SdaiException {
	   // SPECIFICS - remove all users, since they will become invalid anyway
	   // 1) PD
	   AProperty_definition users = new AProperty_definition();
	   CProperty_definition.usedinDefinition(null, armEntity, context.domain, users);
	   for (int i = 1; i <= users.getMemberCount(); ) {
	      EProperty_definition user = users.getByIndex(i);
	      AProperty_definition_representation users2 = new AProperty_definition_representation();
	      CProperty_definition_representation.usedinDefinition(null, user,
	                                                           context.domain, users2);
	      for (int j = 1; j <= users2.getMemberCount(); ) {
	         EProperty_definition_representation user2 = users2.getByIndex(j);
	         // stuff needed for deletion of representation
	         ERepresentation er = user2.getUsed_representation(null);
	         users2.removeByIndex(j);
	         if ( (!er.testItems(null)) || (er.getItems(null).getMemberCount() == 0)) {
	            AEntity users3 = new AEntity();
	            er.findEntityInstanceUsers(context.domain, users3);
	            // Representation is used only from this passage_technology,
	            // which is going to be removed.
	            if (users3.getMemberCount() == 1) {
	               er.deleteApplicationInstance();
	            }
	         }
	         // delete PDR
	         user2.deleteApplicationInstance();
	      }
	      users.removeByIndex(i);
	      user.deleteApplicationInstance();
	   }
	   // 2) SARs and all remaining ones
	   AEntity allUsers = new AEntity();
	   armEntity.findEntityInstanceUsers(context.domain, allUsers);
	   for (int i = 1; i <= allUsers.getMemberCount(); ) {
	      EEntity user = allUsers.getByIndexEntity(i);
	      // only this we can remove safely
	      if (user instanceof EShape_aspect_relationship) {
	         allUsers.removeByIndex(i);
	         user.deleteApplicationInstance();
	      }
	      else {
	         i++;
	      }
	   }
   }
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	passage_technology &lt;=
	*  shape_aspect
	*  {shape_aspect
	*  shape_aspect.of_shape -&gt;
	*  product_definition_shape &lt;=
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition = product_definition
	*  product_definition
	*  [product_definition
	*  product_definition.name = 'interconnect module']}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
//		CxItem_shape.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException
	{
//		CxItem_shape.unsetMappingConstraints(context, armEntity);
	}

   //********** "passage_technology" attributes

   /**
    * Sets/creates data for as_finished_inter_stratum_extent attribute.
    *
    * <p>
    *  (inter_stratum_extent as as_finished_inter_stratum_extent);
    *
    * 	passage_technology <=
    * 	shape_aspect <-
    * 	shape_aspect_relationship.relating_shape_aspect
    * 	shape_aspect_relationship
    * 	{shape_aspect_relationship
    * 	shape_aspect_relationship.name = `as finished inter stratum extent'}
    * 	shape_aspect_relationship.related_shape_aspect ->
    * 	shape_aspect
    * 	shape_aspect.of_shape ->
    * 	product_definition_shape <=
    * 	property_definition
    * 	{property_definition
    * 	property_definition.description = `finished stratum extent'}
    * 	property_definition.definition ->
    * 	characterized_definition
    * 	characterized_definition = characterized_product_definition
    * 	characterized_product_definition
    * 	characterized_product_definition = product_definition_relationship
    * 	product_definition_relationship
    * 	{product_definition_relationship
    * 	product_definition_relationship.name = `inter stratum extent'}
    *  end_attribute_mapping;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   // PT <- SAR -> | SA -> PDS -> PDR (specific | reusable)
/* Removed attribute	
   public static void setAs_finished_inter_stratum_extent(SdaiContext context, EPassage_technology_armx armEntity) throws
      SdaiException {
      //unset old values
      unsetAs_finished_inter_stratum_extent(context, armEntity);

      if (armEntity.testAs_finished_inter_stratum_extent(null)) {
      	throw new SdaiException(SdaiException.FN_NAVL, "Need to rewrite function "+armEntity);
         // EXTENT itself
      }
   }
*/
 /**
  * Unsets/deletes data for as_finished_inter_stratum_extent attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
/* Removed attribute   
 public static void unsetAs_finished_inter_stratum_extent(SdaiContext context, EPassage_technology_armx armEntity) throws
    SdaiException {

    EShape_aspect_relationship shape_aspect_relationship = null;
    AShape_aspect_relationship
       results = new AShape_aspect_relationship();
    CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, results);
    for (int i = 1; i <= results.getMemberCount();) {
       shape_aspect_relationship = (EShape_aspect_relationship)
          results.getByIndexObject(i);
       if (shape_aspect_relationship.testName(null)) {
          // This checking is really sufficient
          if (shape_aspect_relationship.getName(null).equals("as finished inter stratum extent")) {
             shape_aspect_relationship.deleteApplicationInstance();
             results.removeByIndex(i);
             continue;
             // break;
          }
       }
       i++;
    }
 }
*/	
 /**
  * Sets/creates data for of_technology attribute.
  *
  * <p>
            <aa attribute="of_technology" assertion_to="Passage_technology_parameters">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">passage_technology &lt;=
						shape_aspect
						shape_definition = shape_aspect
						shape_definition
						characterized_definition = shape_definition
						characterized_definition &lt;-
						property_definition.definition
						property_definition &lt;-
						property_definition_representation.definition
						property_definition_representation
						property_definition_representation.used_representation -&gt;
						representation
						{representation
						representation.name = 'physical characteristics representation'}
					</refpath>
            </aa>			
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // PT <- PD <- PDR -> R
 /* Merged into one entity again
 public static void setOf_technology(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
    //unset old values
    unsetOf_technology(context, armEntity);

    if (armEntity.testOf_technology(null)) {

       EPassage_technology_parameters parameters = armEntity.getOf_technology(null);

         LangUtils.Attribute_and_value_structure[] structure = {
            new LangUtils.Attribute_and_value_structure(
              CProperty_definition.attributeDefinition(null), armEntity)};
         EProperty_definition epd = (EProperty_definition)LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, structure);
         if(!epd.testName(null))
         	epd.setName(null, "");

         EProperty_definition_representation epdr = (EProperty_definition_representation)
               context.working_model.createEntityInstance(CProperty_definition_representation.definition);
         epdr.setDefinition(null, epd);
         epdr.setUsed_representation(null, parameters);

    }
 }
*/
 /**
  * Unsets/deletes data for plated_passage attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
/* Merged into one entity again  
 public static void unsetOf_technology(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
 	AProperty_definition apd = new AProperty_definition();
 	CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
 	if(apd.getMemberCount() == 0)
 		return;
 	SdaiIterator iterPD = apd.createIterator();
 	while(iterPD.next()){
 		EProperty_definition epd = apd.getCurrentMember(iterPD);
 	 	AProperty_definition_representation apdr = new AProperty_definition_representation();
 	 	CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
 	 	SdaiIterator iterPDR = apdr.createIterator();
 	 	while(iterPDR.next()){
 	 		EProperty_definition_representation epdr = apdr.getCurrentMember(iterPDR);
 	 		if(epdr.getUsed_representation(null) instanceof EPassage_technology_parameters){
 	 			epdr.deleteApplicationInstance();
 	 		}
 	 	} 		
 	}
 }
*/
 
 //********** EX "passage_technology_parameters" attributes

 /**
  * Sets/creates data for plated_passage attribute.
  *
  * <p>
  *  attribute_mapping plated_passage (plated_passage
  * , descriptive_representation_item);
  * 	representation
  * 	{representation
  * 	representation.name = `physical characteristics representation'}
  * 	representation.items[i] ->
  * 	{representation_item
  * 	representation_item.name = `plated passage'}
  * 	representation_item =>
  * 	descriptive_representation_item
  * 	{descriptive_representation_item
  * 	(descriptive_representation_item.description = `true')
  * 	(descriptive_representation_item.description = `false')}
  *  end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // PT <- PD <- PDR -> R -> DRI
 public static void setPlated_passage(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
    //unset old values
    unsetPlated_passage(context, armEntity);

    if (armEntity.testPlated_passage(null)) {

       boolean armPlated_passage = armEntity.getPlated_passage(null);
       
       LangUtils.Attribute_and_value_structure[] propertyStructure = new LangUtils.Attribute_and_value_structure[]
				{
					new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeName(null), "plated passage"),
					new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), armEntity)
				};
       EProperty_definition property_definition  = (EProperty_definition) LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, propertyStructure);
       
       if (armPlated_passage) {
       	property_definition.setDescription(null, "true");
       }
       else {
       	property_definition.setDescription(null, "false");
       }
    }
 }

 /**
  * Unsets/deletes data for plated_passage attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetPlated_passage(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
    String keyword = "plated passage";
    AProperty_definition apd = new AProperty_definition();
    CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
    SdaiIterator iter = apd.createIterator();
    while(iter.next()){
    	EProperty_definition epd = apd.getCurrentMember(iter);
		// Safety check for derived attributes
/*		if(((CEntity)epd).testAttributeFast(CProperty_definition.attributeName(null), null) < 0){
			continue;
		}*/
    	
    	if((epd.testName(null))&&(epd.getName(null).equals(keyword)))
    		epd.deleteApplicationInstance();
    }
 }

 /**
  * Sets/creates data for as_finished_passage_extent attribute.
  *
  * <p>
  *  attribute_mapping as_finished_passage_extent_length_data_element (maximum_as_finished_passage_extent
  * , (*PATH*), length_data_element);
  * 	representation
  * 	{representation
  * 	representation.name = `physical characteristics representation'}
  * 	representation.items[i] ->
  * 	{representation_item
  * 	representation_item.name = `maximum as finished passage extent'}
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
 // PT <- PD <- PDR -> R -> MRI+LMWU
 public static void setAs_finished_passage_extent(SdaiContext context, EPassage_technology_armx armEntity) throws
    SdaiException {
    //unset old values
    unsetAs_finished_passage_extent(context, armEntity);

    if (armEntity.testAs_finished_passage_extent(null)) {
    	EEntity ee = armEntity.getAs_finished_passage_extent(null);
    	if(!(ee instanceof ERepresentation_item)){
    		SdaiSession.println("wrong type of attribute for Passage_technology_armx.as_finished_passage_extent"+ee);
    		return;
    	}
      ERepresentation_item characteristic = (ERepresentation_item)ee;
      ARepresentation ar = new ARepresentation();
      CRepresentation.usedinItems(null, characteristic, context.domain, ar);
      ERepresentation er;
      if(ar.getMemberCount() > 0){
    	  er = ar.getByIndex(1);
      }else{
    	  er = CxAP210ARMUtilities.createRepresentation(context, "", false);
    	  er.createItems(null).addUnordered(characteristic);
      }
      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "as finished passage extent", er);
    }
 }

 /**
  * Unsets/deletes data for as_finished_passage_extent attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetAs_finished_passage_extent(SdaiContext context, EPassage_technology_armx armEntity) throws
    SdaiException {
 	CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "as finished passage extent");
 }

 /**
  * Sets/creates data for as_finished_deposition_thickness attribute.
  *
  * <p>
  *  attribute_mapping as_finished_deposition_thickness_length_data_element (maximum_as_finished_deposition_thickness
  * , (*PATH*), length_data_element);
  * 	representation
  * 	{representation
  * 	representation.name = `physical characteristics representation'}
  * 	representation.items[i] ->
  * 	{representation_item
  * 	representation_item.name = `maximum as finished deposition thickness'}
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
 public static void setAs_finished_deposition_thickness(SdaiContext context, EPassage_technology_armx armEntity) throws
    SdaiException {
 	unsetAs_finished_deposition_thickness(context, armEntity);
 	if(armEntity.testAs_finished_deposition_thickness(null)){
 		ETolerance_characteristic characteristic = armEntity.getAs_finished_deposition_thickness(null);
   
 		CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "as finished deposition thickness", characteristic);
 	}
 }

 /**
  * Unsets/deletes data for maximum_as_finished_deposition_thickness attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetAs_finished_deposition_thickness(SdaiContext context, EPassage_technology_armx armEntity) throws
    SdaiException {
 	CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "as finished deposition thickness");
 }

 /**
  * Sets/creates data for maximum_aspect_ratio attribute.
  *
  * <p>
  *  attribute_mapping maximum_aspect_ratio (maximum_aspect_ratio
  * , ratio_measure_with_unit);
  * 	passage_technology <=
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
  * 	{representation
  * 	representation.name = `physical characteristics representation'}
  * 	representation.items[i] ->
  * 	{representation_item
  * 	representation_item.name = `maximum aspect ratio'}
  * 	representation_item =>
  * 	measure_representation_item <=
  * 	measure_with_unit =>
  * 	ratio_measure_with_unit
  *  end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // UNTESTED - no data
 public static void setMaximum_aspect_ratio(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
    //unset old values
    unsetMaximum_aspect_ratio(context, armEntity);

    if (armEntity.testMaximum_aspect_ratio(null)) {
    	throw new SdaiException(SdaiException.FN_NAVL, "This set method is not implemented yet ");
/*
       double armMaximum_aspect_ratio = armEntity.getMaximum_aspect_ratio(null);

       jsdai.SMixed_complex_types.CMeasure_representation_item$ratio_measure_with_unit ratioAIM =
          (jsdai.SMixed_complex_types.CMeasure_representation_item$ratio_measure_with_unit)
          context.working_model.createEntityInstance(jsdai.SMixed_complex_types.
                                                     CMeasure_representation_item$ratio_measure_with_unit.definition);
       ratioAIM.setName(null, "maximum aspect ratio");
       ratioAIM.setValue_component(null, armMaximum_aspect_ratio, (jsdai.SMeasure_schema.ERatio_measure)null);
       // Some lovely AIM stuff
       jsdai.SMeasure_schema.EDimensional_exponents dimensions =
          (jsdai.SMeasure_schema.EDimensional_exponents)
          context.working_model.createEntityInstance(jsdai.SMeasure_schema.CDimensional_exponents.class);
       dimensions.setAmount_of_substance_exponent(null, 0);
       dimensions.setElectric_current_exponent(null, 0);
       dimensions.setLength_exponent(null, 0);
       dimensions.setLuminous_intensity_exponent(null, 0);
       dimensions.setMass_exponent(null, 0);
       dimensions.setThermodynamic_temperature_exponent(null, 0);
       dimensions.setTime_exponent(null, 0);

       jsdai.SMeasure_schema.CContext_dependent_unit$ratio_unit unit =
          (jsdai.SMeasure_schema.CContext_dependent_unit$ratio_unit)
          context.working_model.createEntityInstance(jsdai.SMeasure_schema.CContext_dependent_unit$ratio_unit.class);
       unit.setDimensions(null, dimensions);
       unit.setName(null, "ratio");
       ratioAIM.setUnit_component(null, unit);
       
       ARepresentation_item items;
       if(armEntity.testItems(null))
       	items = armEntity.getItems(null);
       else
       	items = armEntity.createItems(null);
       items.addUnordered(ratioAIM);
*/       
    }
 }

 /**
  * Unsets/deletes data for maximum_aspect_ratio attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // Delete instance, since in ARM it stands for double, which can't exist by its own
 public static void unsetMaximum_aspect_ratio(SdaiContext context, EPassage_technology_armx armEntity) throws
    SdaiException {
 	 if (armEntity.testMaximum_aspect_ratio(null)) {
 	 	throw new SdaiException(SdaiException.FN_NAVL, "This unset method is not implemented yet ");
 	 }
/* 	 
    String keyword = "maximum aspect ratio";
    if ( (armEntity != null) && (armEntity.testItems(null))) {
       ARepresentation_item items = armEntity.getItems(null);
       for (int i = 1; i <= items.getMemberCount(); i++) {
          ERepresentation_item item = items.getByIndex(i);
          if ( (item instanceof jsdai.SMeasure_schema.CContext_dependent_unit$ratio_unit) &&
              (item.testName(null)) &&
              (item.getName(null).equals(keyword))) {
             item.deleteApplicationInstance();
          }
       }
    }*/
 }
	
 /**
  * Sets/creates data for specification attribute.
  *
  * <p>
  *  attribute_mapping specification_process_specification (specification
  * , (*PATH*), process_specification);
            <aa attribute="specification" assertion_to="Process_specification">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve"representation
						document_reference_item = representation
						document_reference_item &lt;-
						applied_document_reference.items[i]
						applied_document_reference
						applied_document_reference &lt;=
						document_reference
						document_reference.assigned_document -&gt;
						document &lt;-
						document_product_association.relating_document
						{document_product_association.name = 'equivalence'}
						document_product_association.related_product -&gt;
						product_or_formation_or_definition = product_definition
						product_definition
						{product_definition.name = 'process specification'}				
				</refpath>
            </aa>
  *  end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 // R <- ADR -> EES
 /* made derived
 public static void setSpecification(SdaiContext context, EPassage_technology_parameters armEntity) throws SdaiException {
    //unset old values
    //unsetSpecification(context, armEntity);

    if (armEntity.testSpecification(null)) {

       EProcess_specification armSpecification = armEntity.getSpecification(null);
       CxAP210ARMUtilities.assignDocument_definition(context, armSpecification, armEntity);
    }
 }
*/
 /**
  * Unsets/deletes data for specification attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 /* made derived
 public static void unsetSpecification(SdaiContext context, EPassage_technology_parameters armEntity) throws SdaiException {
 	CxAP210ARMUtilities.unAssignDocument_definition(context, armEntity, "process specification");
 }
*/ 
 
 /**
  * Sets/creates data for passage_terminus_condition attribute.
  *
  * <p>
	attribute_mapping passage_terminus_condition(passage_terminus_condition, $PATH);
		passage_technology <=
		characterized_object
		characterized_definition = characterized_object
		characterized_definition <-
		property_definition.definition
		property_definition
		{property_definition.name = 'passage terminus condition'}
		{(property_definition.description = 'bilateral complete removal')
		(property_definition.description = 'bilateral bond')
		(property_definition.description = 'unilateral bond')}
				
	end_attribute_mapping;
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setPassage_terminus_condition(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
    //unset old values
    unsetPassage_terminus_condition(context, armEntity);

    if (armEntity.testPassage_terminus_condition(null)) {

       int armTerminus_condition = armEntity.getPassage_terminus_condition(null);
       
       LangUtils.Attribute_and_value_structure[] propertyStructure = new LangUtils.Attribute_and_value_structure[]
			{
				new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeName(null), "passage terminus condition"),
				new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), armEntity)
			};
       EProperty_definition property_definition  = (EProperty_definition) LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, propertyStructure);
       String value = EFt_terminus_condition.toString(armTerminus_condition).toLowerCase().replace('_', ' ');
       property_definition.setDescription(null, value);
    }
 }

 /**
  * Unsets/deletes data for plated_passage attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetPassage_terminus_condition(SdaiContext context, EPassage_technology_armx armEntity) throws SdaiException {
    String keyword = "passage terminus condition";
    AProperty_definition apd = new AProperty_definition();
    CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
    SdaiIterator iter = apd.createIterator();
    while(iter.next()){
    	EProperty_definition epd = apd.getCurrentMember(iter);
		// Safety check for derived attributes
/*		if(((CEntity)epd).testAttributeFast(CProperty_definition.attributeName(null), null) < 0){
			continue;
		}*/
    	
    	if((epd.testName(null))&&(epd.getName(null).equals(keyword)))
    		epd.deleteApplicationInstance();
    }
 }
 
	
}
