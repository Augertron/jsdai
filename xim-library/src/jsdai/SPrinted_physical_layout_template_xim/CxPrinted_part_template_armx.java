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
import jsdai.util.LangUtils;
import jsdai.SFunctional_usage_view_xim.*;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SPart_template_xim.*;
import jsdai.SPrinted_physical_layout_template_mim.CPrinted_part_template;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.CxItem_shape;
import jsdai.SShape_property_assignment_xim.EItem_shape;

public class CxPrinted_part_template_armx extends CPrinted_part_template_armx implements EMappedXIMEntity
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

		setTemp("AIM", CPrinted_part_template.definition);

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

//      setOf_stratum_technology(context, this);
//      setMaterial_stack(context, this);
//      setPrinted_part_template_stack(context, this);
      setImplemented_function(context, this);
		
		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);

//      unsetOf_stratum_technology(null);
//      unsetMaterial_stack(null);
//      unsetPrinted_part_template_stack(null);
      unsetImplemented_function(null);
      throw new SdaiException(SdaiException.SY_ERR, " This Cx class needs to be reviewed");
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

//      unsetOf_stratum_technology(context, this);
//      unsetMaterial_stack(context, this);
//      unsetPrinted_part_template_stack(context, this);
      unsetImplemented_function(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  part_template_definition &lt;=
	*  {product_definition
	*  (product_definition.description = 'printed part template')
	*  (product_definition.description = 'printed connector template')}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPrinted_part_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxTemplate_definition.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPrinted_part_template_armx armEntity) throws SdaiException
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
 		CxTemplate_definition.unsetPhysical_characteristic(context, armEntity); 		
 	}
    
    
	/**
	* Sets/creates data for of_stratum_technology attribute.
	*
	* <p>
	*  attribute_mapping of_stratum_technology_stratum_technology (of_stratum_technology
	* , (*PATH*), stratum_technology);
	* 	part_template_definition &lt;=
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'of stratum technology'}
	*  shape_aspect_relationship.relating_shape_aspect -&gt;
	*  shape_aspect
	*  shape_aspect.of_shape -&gt;
	*  product_definition_shape &lt;=
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition = characterized_object
	*  characterized_object =&gt;
	*  stratum_technology	
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* 	
 	// PTD <- PDS <- SA <- SAR 
	public static void setOf_stratum_technology(SdaiContext context, EPrinted_part_template armEntity) throws SdaiException
	{
		//unset old values
		unsetOf_stratum_technology(context, armEntity);

		if (armEntity.testOf_stratum_technology(null))
		{

			AStratum_technology_armx armOf_stratum_technologies = armEntity.getOf_stratum_technology(null);
			SdaiIterator iter = armOf_stratum_technologies.createIterator();
			while(iter.next()){
				EStratum_technology_armx armOf_stratum_technology = armOf_stratum_technologies.getCurrentMember(iter);

            LangUtils.Attribute_and_value_structure[] saS = {new LangUtils.Attribute_and_value_structure(
                  CShape_aspect.attributeOf_shape(null),armEntity)
            };
            EShape_aspect armEntitySA = (EShape_aspect)
              LangUtils.createInstanceIfNeeded(context,CShape_aspect.definition,saS);
				
				EShape_aspect technologySA = CxAP210ARMUtilities.createPDS_SA_structureIfNeeded(context, armOf_stratum_technology);
				
            jsdai.SProduct_property_definition_schema.EShape_aspect_relationship sar =
                (jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)
                context.working_model.createEntityInstance(jsdai.SProduct_property_definition_schema.CShape_aspect_relationship.definition);
            sar.setName(null,"of stratum technology");
            sar.setRelated_shape_aspect(null,armEntitySA);
            sar.setRelating_shape_aspect(null,technologySA);
			}
		}
	}

*/
	/**
	* Unsets/deletes data for of_stratum_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* 	
	public static void unsetOf_stratum_technology(SdaiContext context, EPrinted_part_template armEntity) throws SdaiException
    {
       if (armEntity.testOf_stratum_technology(null)){
        AShape_aspect asa = new AShape_aspect();
        CShape_aspect.usedinOf_shape(null, armEntity, context.domain, asa);
        SdaiIterator iter2 = asa.createIterator();
        while(iter2.next()){
        	EShape_aspect esa = asa.getCurrentMember(iter2);
            AShape_aspect_relationship aSar = new AShape_aspect_relationship();
            EShape_aspect_relationship sar = null;
            CShape_aspect_relationship.usedinRelated_shape_aspect(null,esa,context.domain,aSar);
            String ost = "of stratum technology";
            String name = null;
            if (aSar.getMemberCount()>=1)
            {
                for (int i=1;i<=aSar.getMemberCount();i++)
                {
                    sar = aSar.getByIndex(i);
                    if (sar.testName(null))
                    {
                        name = sar.getName(null);
                        if (ost.equals(name))
                        {
                            sar.deleteApplicationInstance();
                        }
                    }
                }
            }
        }
       }
    }


*/
	/**
	* Sets/creates data for material_stack attribute.
	*
	* <p>
	*  attribute_mapping material_stack_stratum_technology_link (material_stack
	* , (*PATH*), stratum_technology_link);
	* 	part_template_definition <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_relationship.related_property_definition
	* 	property_definition_relationship
	* 	{property_definition_relationship
	* 	property_definition_relationship.name = `material stack'}
	* 	property_definition_relationship.relating_property_definition ->
	* 	property_definition =>
	* 	stratum_technology_link
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 	/*
	public static void setMaterial_stack(SdaiContext context, EPrinted_part_template armEntity) throws SdaiException
	{
		//unset old values
		unsetMaterial_stack(context, armEntity);

		if (armEntity.testMaterial_stack(null))
		{

			AStratum_technology_link_armx aArmStratum_technology = armEntity.getMaterial_stack(null);
	        EStratum_technology_link_armx armStratum_technology = null;
            for (int i=1;i<=aArmStratum_technology.getMemberCount();i++)
            {
                //PDR->PD
                jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship pdr =
                    (jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship)
                    context.working_model.createEntityInstance(jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship.class);
                pdr.setName(null,"material stack");
                pdr.setRelated_property_definition(null,armEntity);
                armStratum_technology = aArmStratum_technology.getByIndex(i);
                pdr.setRelating_property_definition(null, armStratum_technology);
            }
        }
    }
*/

	/**
	* Unsets/deletes data for material_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* 	
	public static void unsetMaterial_stack(SdaiContext context, EPrinted_part_template armEntity) throws SdaiException
	{
        if (armEntity.testMaterial_stack(null))
        {
            //PDR->PD
            jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship aPdr =
                new jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship();
            jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship pdr = null;
            jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, aPdr);
            String ms = "material stack";
            String name = null;
            if (aPdr.getMemberCount()>=1)
            {
                for (int i=1;i<=aPdr.getMemberCount();i++)
                {
                    pdr = aPdr.getByIndex(i);
                    if (pdr.testName(null))
                    {
                        name = pdr.getName(null);
                        if (ms.equals(name))
                        {
                            pdr.deleteApplicationInstance();
                        }
                    }
                }
            }
        }

	}
*/

	/**
	* Sets/creates data for printed_part_template_stack attribute.
	*
	* <p>
	*  attribute_mapping printed_part_template_stack_printed_part_template_link (printed_part_template_stack
	* , (*PATH*), printed_part_template_link);
	*  product_definition 
	*  characterized_product_definition = product_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_definition &lt;-
	*  property_definition.definition
	*  property_definition &lt;-
	*  property_definition_relationship.related_property_definition
	*  property_definition_relationship
	*  {property_definition_relationship
	*  property_definition_relationship.name = 'printed part template stack'}
	*  property_definition_relationship.relating_property_definition -&gt;
	*  property_definition =&gt;
	*  stratum_technology_link =&gt;
	*  printed_part_template_link
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PD <- PD <- PDR ->PPTL
/*
	public static void setPrinted_part_template_stack(SdaiContext context, EPrinted_part_template armEntity) throws SdaiException
	{
		//unset old values
		unsetPrinted_part_template_stack(context, armEntity);

		if (armEntity.testPrinted_part_template_stack(null))
		{
			APrinted_part_template_link_armx aArmPrinted_part_template_stack = armEntity.getPrinted_part_template_stack(null);
         EPrinted_part_template_link_armx armPptl = null;
         for (int i=1;i<=aArmPrinted_part_template_stack.getMemberCount();i++){
         	EProperty_definition_relationship pdr =
                    (EProperty_definition_relationship)
                    context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
                pdr.setName(null,"printed part template stack");
                pdr.setRelated_property_definition(null,armEntity);

                armPptl = aArmPrinted_part_template_stack.getByIndex(i);

                //CPrinted_part_template_link$product_definition_shape pptlAndPds = null;
                // Very questionable ???

                pdr.setRelating_property_definition(null,armPptl);
            }
		}

	}
*/

	/**
	* Unsets/deletes data for printed_part_template_stack attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* 	
	public static void unsetPrinted_part_template_stack(SdaiContext context, EPrinted_part_template armEntity) throws SdaiException{
		if (armEntity.testPrinted_part_template_stack(null)){
        AProperty_definition_relationship apdr = new AProperty_definition_relationship();
        CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
        SdaiIterator iter2 = apdr.createIterator();
        while(iter2.next()){
        	EProperty_definition_relationship epdr = apdr.getCurrentMember(iter2);
        	if((epdr.testName(null))&&(epdr.getName(null).equals("printed part template stack"))){
        		epdr.deleteApplicationInstance();
            }
     		}
		}
    }
*/

	/**
	* Sets/creates data for implemented_function attribute.
	*
	* <p>
	*  attribute_mapping implemented_function_functional_unit_usage_view (implemented_function
	* , (*PATH*), functional_unit_usage_view);
	*  part_template_definition &lt;=
	*  product_definition  &lt;-
	*  product_definition_relationship.related_product_definition
	*  product_definition_relationship
	*  {product_definition_relationship.name = 'implemented function'}
	*  product_definition_relationship.relating_product_definition -&gt;
	*  product_definition
	*  {product_definition
	*  product_definition.frame_of_reference -&gt;
	*  product_definition_context &lt;=
	*  application_context_element
	*  application_context_element.name = 'functional design usage'}
	*  product_definition =&gt; 
	*  functional_unit	
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PTD <- PDR -> PD -> FU
	public static void setImplemented_function(SdaiContext context, EPrinted_part_template_armx armEntity) throws SdaiException
	{
      //unset old values
      unsetImplemented_function(context, armEntity);
		if (armEntity.testImplemented_function(null)){
			EFunctional_unit_usage_view armImplemented_function = armEntity.getImplemented_function(null);
         // PD -> FU
         LangUtils.Attribute_and_value_structure[] propdS = {new LangUtils.Attribute_and_value_structure(
               jsdai.SProduct_property_definition_schema.CProperty_definition.attributeDefinition(null),
					armImplemented_function)};
         jsdai.SProduct_property_definition_schema.EProperty_definition pdFU =
           (jsdai.SProduct_property_definition_schema.EProperty_definition)
           LangUtils.createInstanceIfNeeded(context,jsdai.SProduct_property_definition_schema.CProperty_definition.definition,propdS);
         if(!pdFU.testName(null))
         	pdFU.setName(null, "");
         //PDR 
         EProperty_definition_relationship pdr = (EProperty_definition_relationship)
         	context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
         pdr.setName(null,"implemented function");
         pdr.setRelated_property_definition(null, armEntity);
         pdr.setRelating_property_definition(null,pdFU);
		}
	}


	/**
	* Unsets/deletes data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplemented_function(SdaiContext context, EPrinted_part_template_armx armEntity) throws SdaiException
	{
      AProperty_definition_relationship aPdr = new AProperty_definition_relationship();
      CProperty_definition_relationship.usedinRelated_property_definition(
      		null, armEntity, context.domain, aPdr);
      String imf = "implemented function";
      for (int i=1;i<=aPdr.getMemberCount();i++){
      	EProperty_definition_relationship pdr = aPdr.getByIndex(i);
         if (pdr.testName(null)){
         	if (pdr.getName(null).equals(imf)){
         		pdr.deleteApplicationInstance();
         	}
         }
      }
	}
	
}
