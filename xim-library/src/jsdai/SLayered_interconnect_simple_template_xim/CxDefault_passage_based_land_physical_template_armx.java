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

package jsdai.SLayered_interconnect_simple_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFabrication_technology_xim.EPassage_technology_armx;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SLayered_interconnect_simple_template_mim.CDefault_passage_based_land_physical_template;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxDefault_passage_based_land_physical_template_armx extends CDefault_passage_based_land_physical_template_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition
	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a2);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (EEntity)a2;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a2$;
	}
	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a0$;
	}
	
	// END OF Property_definition
	
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a5);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a5$;
	}

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDefault_passage_based_land_physical_template.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		
		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		setId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

      // setOf_stratum_technology(context, this);
      
      // minimum_fabrication_allowance
		// Removed per 2507, comment #2		
		// setMinimum_fabrication_allowance(context, this);
      
      // of_passage_technology
		setOf_passage_technology(context, this);
      
      // of_stratum_extent
		// setOf_stratum_extent(context, this);
      
      // minimum_annular_ring
		// Removed per 2507, comment #2		
		// setMinimum_annular_ring(context, this);
      
      
		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
      // unsetOf_stratum_technology(null);
		// Removed per 2507, comment #2
		// unsetMinimum_fabrication_allowance(null);
		unsetOf_passage_technology(null);
//		unsetOf_stratum_extent(null);
		// Removed per 2507, comment #2		
		// unsetMinimum_annular_ring(null);
      
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		unsetId_x(context, this);

		//	********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

      // unsetOf_stratum_technology(context, this);

      // minimum_fabrication_allowance
		// Removed per 2507, comment #2		
		// unsetMinimum_fabrication_allowance(context, this);
      
      // of_passage_technology
		unsetOf_passage_technology(context, this);
      
      // of_stratum_extent
		//unsetOf_stratum_extent(context, this);
      
      // minimum_annular_ring
		// Removed per 2507, comment #2		
		// unsetMinimum_annular_ring(context, this);
      
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  land_physical_template &lt;=
	*  part_template_definition &lt;=
	*  {product_definition
	*  (product_definition.description = 'default via based')
	*  (product_definition.description = 'default plated passage based')
	*  (product_definition.description = 'default attachment size and via based')
	*  (product_definition.description = 'default unsupported passage based')
	*  (product_definition.description = 'default component termination passage based')
	*  (product_definition.description = 'default attachment size and component termination passage based')}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// no more constraints as it is ABS and only subtypes have more magic strings
		CxLand_physical_template_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxLand_physical_template_armx.unsetMappingConstraints(context, armEntity);
	}	
	//********** "managed_design_object" attributes

	//********** "item_shape" attributes
    /**
     * Sets/creates data for Id_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
       CxItem_shape.setId_x(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
      CxItem_shape.unsetId_x(context, armEntity);
   }

 	//********** "product_view_definition" attributes
    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_characterization(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
   }

    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_contexts(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
   }

    // Template_definition, originally developed in Part_template_definition
 	/**
 	* Sets/creates data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
    // PTD <- PD <- PDR -> R -> RI <- AGA -> CT
 	public static void setPhysical_characteristic(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
 	{
 		CxTemplate_definition.setPhysical_characteristic(context, armEntity);
 	}


 	/**
 	* Unsets/deletes data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetPhysical_characteristic(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
   {
 		CxTemplate_definition.unsetPhysical_characteristic(context, armEntity); 		
 	}
    
    
	/**
	* Sets/creates data for of_stratum_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 	// PTD <- SA <- SAR 
/*	public static void setOf_stratum_technology(SdaiContext context, EStratum_feature_template_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_armx.setOf_stratum_technology(context, armEntity);		
	}
*/

	/**
	* Unsets/deletes data for of_stratum_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/*	public static void unsetOf_stratum_technology(SdaiContext context, EStratum_feature_template_armx armEntity) throws SdaiException
    {
		CxStratum_feature_template_armx.unsetOf_stratum_technology(context, armEntity);		
    }
*/
	//********** "default_passage_based_land_physical_template" attributes

	/**
	* Sets/creates data for minimum_fabrication_allowance attribute.
	*
	* <p>
	*  attribute_mapping minimum_fabrication_allowance_length_data_element (minimum_fabrication_allowance
	* , (*PATH*), length_data_element);
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `minimum fabrication allowance'}
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
 	/* Removed per 2507, comment #2 	
	public static void setMinimum_fabrication_allowance(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
        if (armEntity.testMinimum_fabrication_allowance(null))
		{
			ELength_measure_with_unit armLde = armEntity.getMinimum_fabrication_allowance(null);

            //unset old values
            unsetMinimum_fabrication_allowance(context, armEntity);

			//EA armLde.createAimData(context);
            CLength_measure_with_unit$measure_representation_item newInstance;
            if(!(armLde instanceof jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item)){
//            	System.err.println(" Substituting ... "+instance);
               newInstance = (CLength_measure_with_unit$measure_representation_item)
                    context.working_model.substituteInstance(armLde, jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item.class);
               if(!newInstance.testName(null)){
            	   newInstance.setName(null, "");
               }
				//EA armLde.setAimInstance(newInstance);
            }
            else{
                newInstance = (CLength_measure_with_unit$measure_representation_item)armLde;
            }
            setRepresentation_item(context, armEntity, newInstance, "minimum fabrication allowance");
		}

	}
*/

	/**
	* Unsets/deletes data for minimum_fabrication_allowance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 	/* Removed per 2507, comment #2 	
	public static void unsetMinimum_fabrication_allowance(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator apdIter = apd.createIterator();
		String name = "minimum fabrication allowance";
		while(apdIter.next()){
			EProperty_definition epd = apd.getCurrentMember(apdIter);
			if((epd.testName(null))&&(epd.getName(null).equals(name))){
				AProperty_definition_representation apdr = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apd);
				SdaiIterator apdrIter = apdr.createIterator();
				while(apdrIter.next()){
					EProperty_definition_representation epdr = apdr.getCurrentMember(apdrIter);
					epdr.deleteApplicationInstance();
				}
				epd.deleteApplicationInstance();
			}
		}
	}
*/

	/**
	* Sets/creates data for of_passage_technology attribute.
	*
	* <p>
	*  attribute_mapping of_passage_technology_passage_technology (of_passage_technology
	* , (*PATH*), passage_technology);
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `technology usage'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	passage_technology
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setOf_passage_technology(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testOf_passage_technology(null))
		{

			EPassage_technology_armx armPt = armEntity.getOf_passage_technology(null);
            //unset old values
            unsetOf_passage_technology(context, armEntity);
   			// PD -> PT
/*   			LangUtils.Attribute_and_value_structure[] pdStructure = {
   				new LangUtils.Attribute_and_value_structure(
            	CProperty_definition.attributeDefinition(null), armPt)
   			};
   			EProperty_definition epd = (EProperty_definition) LangUtils
   				.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
			// Safety check for derived attributes
//			if(((CEntity)epd).testAttributeFast(CProperty_definition.attributeName(null), null) >= 0){
				if(!epd.testName(null))
					epd.setName(null, "");
//			}
*/   			
   			// PDR
            EProperty_definition_relationship epdr = 
            	(EProperty_definition_relationship) 
   				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);

            String isft = "technology usage";
   				// According SEDS 527 - do nothing more
            epdr.setName(null,isft);

            //setting related
            epdr.setRelated_property_definition(null, armEntity);

            //setting relating
            epdr.setRelating_property_definition(null, armPt);
            
            epdr.setDescription(null,"");
		}
	}


	/**
	* Unsets/deletes data for of_passage_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetOf_passage_technology(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
	     AProperty_definition_relationship apdr = new AProperty_definition_relationship();
	      CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
	      if(apdr.getMemberCount() == 0)
	      	return;
	      SdaiIterator iter = apdr.createIterator();
	      String isft = "technology usage";
	      while(iter.next()){
	      	EProperty_definition_relationship epdr = apdr.getCurrentMember(iter);
	         	if((epdr.testName(null))&&(epdr.getName(null).equals(isft)))
	         		epdr.deleteApplicationInstance();
	      }
	}


	/**
	* Sets/creates data for of_stratum_extent attribute.
	*
	* <p>
	*  attribute_mapping of_stratum_extent_inter_stratum_extent (of_stratum_extent
	* , (*PATH*), inter_stratum_extent);
	* 	property_definition <-
	* 	property_definition_relationship.related_property_definition
	* 	{property_definition_relationship
	* 	property_definition_relationship.name = `of stratum extent'}
	* 	property_definition_relationship.relating_property_definition ->
	* 	property_definition.definition ->
	* 	characterized_definition = characterized_product_definition
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
/* Removed during modularization	
	public static void setOf_stratum_extent(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testOf_stratum_extent(null))
		{
			throw new SdaiException(SdaiException.FN_NAVL, " Need to implement this function a new "+armEntity);
			
			EInter_stratum_extent armIse = armEntity.getOf_stratum_extent(null);
            //unset old values
            unsetOf_stratum_extent(context, armEntity);
            EProperty_definition_relationship pdr = (EProperty_definition_relationship)
                context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
            pdr.setName(null,"of stratum extent");
				pdr.setDescription(null, "");
            //PDR->PD
            pdr.setRelated_property_definition(null,armEntity);
            //PD->PDR2
            LangUtils.Attribute_and_value_structure[] pdS2 = {new LangUtils.Attribute_and_value_structure(
                    jsdai.SProduct_property_definition_schema.CProperty_definition.attributeDefinition(null),armIse)
            };
            jsdai.SProduct_property_definition_schema.EProperty_definition pd2 = (jsdai.SProduct_property_definition_schema.EProperty_definition)
                LangUtils.createInstanceIfNeeded(context,jsdai.SProduct_property_definition_schema.CProperty_definition.definition,pdS2);
            //PDR->PD
            pdr.setRelating_property_definition(null,pd2);
		}
	}
*/

	/**
	* Unsets/deletes data for of_stratum_extent attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed during modularization		
	public static void unsetOf_stratum_extent(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
        if (armEntity.testOf_stratum_extent(null))
       {
            AProperty_definition_relationship aPdr = new AProperty_definition_relationship();
            EProperty_definition_relationship pdr = null;
            CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, aPdr);
            SdaiIterator iter = aPdr.createIterator();
            while(iter.next()){
                pdr = aPdr.getCurrentMember(iter);
                if(pdr.getName(null).equals("of stratum extent"))
                	pdr.deleteApplicationInstance();
            }
        }
	}
*/

	/**
	* Sets/creates data for minimum_annular_ring attribute.
	*
	* <p>
	*  attribute_mapping minimum_annular_ring_length_data_element (minimum_annular_ring
	* , (*PATH*), length_data_element);
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
	* 	representation_item.name = `minimum annular ring'}
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
	/* Removed per 2507, comment #2
	public static void setMinimum_annular_ring(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testMinimum_annular_ring(null))
		{
			ELength_measure_with_unit armLde = armEntity.getMinimum_annular_ring(null);

            //unset old values
            unsetMinimum_annular_ring(context, armEntity);
			//EA armLde.createAimData(context);
            CLength_measure_with_unit$measure_representation_item newInstance;
            if(!(armLde instanceof jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item)){
               newInstance = (CLength_measure_with_unit$measure_representation_item)
                    context.working_model.substituteInstance(armLde, jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item.class);
				//EA armLde.setAimInstance(newInstance);
               if(!newInstance.testName(null)){
            	   newInstance.setName(null, "");
               }
            }
            else{
                newInstance = (CLength_measure_with_unit$measure_representation_item)armLde;
            }
            setRepresentation_item(context, armEntity, newInstance, "minimum annular ring");
		}
	}
*/

	/**
	* Unsets/deletes data for minimum_annular_ring attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed per 2507, comment #2	
	public static void unsetMinimum_annular_ring(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator apdIter = apd.createIterator();
		String name = "minimum annular ring";
		while(apdIter.next()){
			EProperty_definition epd = apd.getCurrentMember(apdIter);
			if((epd.testName(null))&&(epd.getName(null).equals(name))){
				AProperty_definition_representation apdr = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apd);
				SdaiIterator apdrIter = apdr.createIterator();
				while(apdrIter.next()){
					EProperty_definition_representation epdr = apdr.getCurrentMember(apdrIter);
					epdr.deleteApplicationInstance();
				}
				epd.deleteApplicationInstance();
			}
		}
	}*/
//reuse
/*
    private static void setRepresentation_item(SdaiContext context, EDefault_passage_based_land_physical_template_armx armEntity,
            jsdai.SRepresentation_schema.ERepresentation_item item,
            String name)throws SdaiException{

    	ARepresentation representations = new ARepresentation();
    	CRepresentation.usedinItems(null, item, context.domain, representations);
    	jsdai.SRepresentation_schema.ERepresentation suitableRepresentation = null;
    	if(representations.getMemberCount() > 0){
    		suitableRepresentation = representations.getByIndex(1);
    	}else{
    		suitableRepresentation = (jsdai.SRepresentation_schema.ERepresentation)
            	context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.definition);
    		suitableRepresentation.setName(null, "");//representationName);
    		// Context
    		jsdai.SRepresentation_schema.ERepresentation_context representation_context =
    			CxAP210ARMUtilities.createRepresentation_context(context,
                                                                       "", "", true);
    		suitableRepresentation.setContext_of_items(null, representation_context);
    		suitableRepresentation.createItems(null).addUnordered(item);
    	}
    	// PD
        EProperty_definition propDef = (EProperty_definition)
    		context.working_model.createEntityInstance(CProperty_definition.definition);
        propDef.setDefinition(null, armEntity);
        propDef.setName(null, name);
    	// PDR
        EProperty_definition_representation propDefRep = (EProperty_definition_representation)
        	context.working_model.createEntityInstance(CProperty_definition_representation.class);
        propDefRep.setDefinition(null, propDef);
        propDefRep.setUsed_representation(null, suitableRepresentation);
    }
*/
        public static jsdai.SRepresentation_schema.ERepresentation getSuitable_representation(SdaiContext context, EProperty_definition armEntity)throws SdaiException{
            AProperty_definition_representation apdr = new AProperty_definition_representation();
            CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, apdr);
            for(int j=1;j<=apdr.getMemberCount();j++){
                if(apdr.getByIndex(j).testUsed_representation(null)){
                    ERepresentation er = apdr.getByIndex(j).getUsed_representation(null);
                    return er;
                }
            }
            return null;
        }


	
	
}
