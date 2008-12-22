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

package jsdai.SPrinted_physical_layout_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFabrication_technology_xim.*;
import jsdai.SMeasure_schema.*;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SPart_template_xim.*;
import jsdai.SPrinted_physical_layout_template_mim.CNon_conductive_cross_section_template;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxNon_conductive_cross_section_template_armx extends CNon_conductive_cross_section_template_armx implements EMappedXIMEntity
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

		setTemp("AIM", CNon_conductive_cross_section_template.definition);

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

      setTemplate_technology(context, this);
      
      setNominal_width(context, this);

		
		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
		
      unsetTemplate_technology(null);
      
      unsetNominal_width(null);
		
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

      unsetTemplate_technology(context, this);
      
      unsetNominal_width(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  part_template_definition &lt;=
	*  {product_definition
	*  product_definition.description = 'material removal feature template'} 
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxTemplate_definition.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
	{
		CxTemplate_definition.unsetMappingConstraints(context, armEntity);
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
 		CxTemplate_definition.setPhysical_characteristic(context, armEntity); 		
 	}

	//********** "non_conductive_cross_section_template" attributes

	/**
	* Sets/creates data for template_technology attribute.
	*
	*
	*  attribute_mapping template_technology_stratum_technology (template_technology
	* , (*PATH*), stratum_technology);
	* 	part_template_definition <=
	* 	product_definition_shape <=
	* 	property_definition <-
	* 	property_definition_relationship.related_property_definition
	* 	property_definition_relationship
	* 	{property_definition_relationship
	* 	property_definition_relationship.name = `technology usage'}
	* 	property_definition_relationship.relating_property_definition ->
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_object
	* 	characterized_object =>
	* 	stratum_technology
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTemplate_technology(SdaiContext context, ENon_conductive_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testTemplate_technology(null))
		{

            AStratum_technology_armx armStratum_technologies = armEntity.getTemplate_technology(null);

            //unset old values
            unsetTemplate_technology(context, armEntity);

            jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship property_definition_relationship = 
            	(jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship) 
            	context.working_model.createEntityInstance(jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship.definition);
            for(int i=1;i<=armStratum_technologies.getMemberCount();i++){
                //seting name and one side
                property_definition_relationship.setName(null,"technology usage");
                property_definition_relationship.setRelated_property_definition(null,armEntity);
                property_definition_relationship.setDescription(null,"");
            	
            	EStratum_technology_armx armStratum_technology = armStratum_technologies.getByIndex(i); 
            	property_definition_relationship.setRelating_property_definition(null,armStratum_technology);
	            //going from other side
	
/*	            jsdai.SProduct_property_definition_schema.AProperty_definition aProperty_definition = new jsdai.SProduct_property_definition_schema.AProperty_definition();
	            jsdai.SProduct_property_definition_schema.EProperty_definition property_definition = null;
	            jsdai.SProduct_property_definition_schema.CProperty_definition.usedinDefinition(null,armStratum_technology,context.domain,aProperty_definition);
	
	            if (aProperty_definition.getMemberCount()==0)
	            {
	                property_definition =  (jsdai.SProduct_property_definition_schema.EProperty_definition) context.working_model.createEntityInstance(jsdai.SProduct_property_definition_schema.CProperty_definition.class);
	                property_definition.setDefinition(null,armStratum_technology);
	            }
	            else
	            {
	               property_definition = aProperty_definition.getByIndex(1);
	            }*/
            }
		}
	}


	/**
	* Unsets/deletes data for template_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTemplate_technology(SdaiContext context, ENon_conductive_cross_section_template_armx armEntity) throws SdaiException
	{
        //jsdai.SAp210_arm_extended.EStratum_technology armStratum_technology = armEntity.getTemplate_technology(null);

        jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship aProperty_definition_relationship = new jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship();
        jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship property_definition_relationship = null;

        jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain,aProperty_definition_relationship);
        String name = null;
        String tu = "technology usage";

        if (aProperty_definition_relationship.getMemberCount()==0)
        {
            return;
        }
        else
        {
            for (int i=1;i<=aProperty_definition_relationship.getMemberCount();i++)
            {
                property_definition_relationship = aProperty_definition_relationship.getByIndex(i);
                if  (property_definition_relationship.testName(null))
                {
                    name = property_definition_relationship.getName(null);
                    if (name.equals(tu))
                    {
                        property_definition_relationship.deleteApplicationInstance();
                    }
                }
            }
        }


	}


	/**
	* Sets/creates data for nominal_width attribute.
	*
	*
	*  attribute_mapping nominal_width_length_data_element (nominal_width
	* , (*PATH*), length_data_element);
	* 	part_template_definition &lt;=
	*  product_definition_shape &lt;=
	*  property_definition &lt;-
	*  property_definition_representation.definition
	*  property_definition_representation
	*  property_definition_representation.used_representation -&gt;
	*  representation
	*  representation.items[i] -&gt;
	*  {representation_item
	*  representation_item.name = 'nominal width'}
	*  representation_item =&gt;
	*  measure_representation_item &lt;=
	*  measure_with_unit =&gt;
	*  length_measure_with_unit
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setNominal_width(SdaiContext context, ENon_conductive_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testNominal_width(null))
		{

			ELength_measure_with_unit armLde = armEntity.getNominal_width(null);

         //unset old values
         unsetNominal_width(context, armEntity);

        jsdai.SProduct_property_representation_schema.EProperty_definition_representation property_definition_representation = (jsdai.SProduct_property_representation_schema.EProperty_definition_representation) context.working_model.createEntityInstance(jsdai.SProduct_property_representation_schema.CProperty_definition_representation.definition);
        property_definition_representation.setDefinition(null, armEntity);
        CLength_measure_with_unit$measure_representation_item lmwu;
        if(!(armLde instanceof CLength_measure_with_unit$measure_representation_item)){
	        lmwu = (CLength_measure_with_unit$measure_representation_item)
	               context.working_model.substituteInstance(armLde, CLength_measure_with_unit$measure_representation_item.definition);
	        lmwu.setName(null,"nominal width");
        }
        else{
        	lmwu = (CLength_measure_with_unit$measure_representation_item)armLde;
        }
		//EA armLde.setAimInstance(lmwu);
        jsdai.SRepresentation_schema.ARepresentation aR = new jsdai.SRepresentation_schema.ARepresentation();
        jsdai.SRepresentation_schema.ERepresentation r = null;
        jsdai.SRepresentation_schema.CRepresentation.usedinItems(null,lmwu,context.domain,aR);

        if (aR.getMemberCount()==0){
            r = (jsdai.SRepresentation_schema.ERepresentation) context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
            r.createItems(null).addUnordered(lmwu);
        }
        else
            r = aR.getByIndex(1);

        property_definition_representation.setUsed_representation(null,r);
		}
	}


	/**
	* Unsets/deletes data for nominal_width attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetNominal_width(SdaiContext context, ENon_conductive_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testNominal_width(null))
        {
            jsdai.SProduct_property_representation_schema.AProperty_definition_representation aProperty_definition_representation = new jsdai.SProduct_property_representation_schema.AProperty_definition_representation();
            jsdai.SProduct_property_representation_schema.EProperty_definition_representation property_definition_representation = null;
            jsdai.SProduct_property_representation_schema.CProperty_definition_representation.usedinDefinition(null,armEntity,context.domain,aProperty_definition_representation);

            if (aProperty_definition_representation.getMemberCount()==0)
            {
                return;
            }
            else
            {
                property_definition_representation = aProperty_definition_representation.getByIndex(1);
                property_definition_representation.deleteApplicationInstance();
            }
        }
	}
 	

}
