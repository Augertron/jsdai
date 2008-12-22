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
* @version $ $
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SAnalytical_model_xim.*;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SPart_template_xim.*;
import jsdai.SPrinted_physical_layout_template_mim.CPrinted_part_cross_section_template;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxPrinted_part_cross_section_template_armx extends CPrinted_part_cross_section_template_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition
	// From property_definition
	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a2);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (EEntity)a2;
	}

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
	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}
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
	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a5);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a5);
	}
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
	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPrinted_part_cross_section_template.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

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

      // horizontal_material_link
      setHorizontal_material_link(context, this);
		
      // vertical_material_link
      setVertical_material_link(context, this);
		
      // transmission_line_model
      setTransmission_line_model(context, this);
		
      // maximum_transmission_line_characteristic
      setMaximum_transmission_line_characteristic(context, this);
		
      // minimum_transmission_line_characteristic
      setMinimum_transmission_line_characteristic(context, this);
		
      // width
      setWidth(context, this);
		
      // unit_length
      setUnit_length(context, this);
		
		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);

      unsetHorizontal_material_link(null);
      unsetVertical_material_link(null);
      unsetTransmission_line_model(null);
      unsetMaximum_transmission_line_characteristic(null);
      unsetMinimum_transmission_line_characteristic(null);
      unsetWidth(null);
      unsetUnit_length(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		// SETTING DERIVED
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		unsetId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

      // horizontal_material_link
      unsetHorizontal_material_link(context, this);
		
      // vertical_material_link
      unsetVertical_material_link(context, this);
		
      // transmission_line_model
      unsetTransmission_line_model(context, this);
		
      // maximum_transmission_line_characteristic
      unsetMaximum_transmission_line_characteristic(context, this);
		
      // minimum_transmission_line_characteristic
      unsetMinimum_transmission_line_characteristic(context, this);
		
      // width
      unsetWidth(context, this);
		
      // unit_length
      unsetUnit_length(context, this);
		
	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	* mapping_constraints;
	*  part_template_definition &lt;=
	*  {product_definition
	*  product_definition.description = 'printed part cross section template'}	
	* end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException{
		CxTemplate_definition.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException{
		CxTemplate_definition.setMappingConstraints(context, armEntity);
	}

	//********** "managed_design_object" attributes


	//********** "shape_element" attributes

	/**
	* Sets/creates data for name attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException
	{
		CxItem_shape.setId_x(context, armEntity);
	}


	/**
	* Unsets/deletes data for name attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException
	{
		CxItem_shape.unsetId_x(context, armEntity);
	}

	/**
	* Sets/creates data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);
	}


	/**
	* Unsets/deletes data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
	}

	/**
	* Sets/creates data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);
	}


	/**
	* Unsets/deletes data for Additional_characterization attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
	}
	


	//********** "physical_feature_or_part_template" attributes

	//********** "template_definition" attributes



	/**
	* Sets/creates data for physical_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
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

	//********** "printed_part_cross_section_template" attributes

	/**
	* Sets/creates data for horizontal_material_link attribute.
	*
	* <p>
	*  attribute_mapping horizontal_material_link_printed_part_template_material_link (horizontal_material_link
	* , (*PATH*), printed_part_template_material_link);
	*  part_template_definition &lt;=
	*  product_definition_shape &lt;- 
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship 
	*  shape_aspect_relationship.name = 'horizontal material link'} 
	*  shape_aspect_relationship.relating_shape_aspect -&gt; 
	*  shape_aspect =&gt;
	*  printed_part_template_material_link	
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setHorizontal_material_link(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testHorizontal_material_link(null))
		{
         //unset old values
         unsetHorizontal_material_link(context, armEntity);
			
			APrinted_part_template_material_link_armx aArmpptml = armEntity.getHorizontal_material_link(null);
         EPrinted_part_template_material_link_armx pptml = null;
         // PTD <- SA
         LangUtils.Attribute_and_value_structure[] saS =
				{new LangUtils.Attribute_and_value_structure(
						CShape_aspect.attributeOf_shape(null),
						armEntity)
			};
         EShape_aspect esa = (EShape_aspect)
             LangUtils.createInstanceIfNeeded(context,CShape_aspect.definition,saS);
         if(!esa.testName(null))
         	esa.setName(null, "");
         if(!esa.testProduct_definitional(null))
         	esa.setProduct_definitional(null, ELogical.UNKNOWN);
         
         for (int i=1;i<=aArmpptml.getMemberCount();i++){
            jsdai.SProduct_property_definition_schema.EShape_aspect_relationship sar =
                (jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)
                 context.working_model.createEntityInstance(jsdai.SProduct_property_definition_schema.CShape_aspect_relationship.class);
            sar.setName(null,"horizontal material link");
            pptml = aArmpptml.getByIndex(i);

            sar.setRelated_shape_aspect(null, esa);
            sar.setRelating_shape_aspect(null, pptml);
         }
		}
	}


	/**
	* Unsets/deletes data for horizontal_material_link attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetHorizontal_material_link(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
        if (armEntity.testHorizontal_material_link(null))
        {
        		AShape_aspect aSa = new AShape_aspect();
        		CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aSa);
        		for (int j=1;j<=aSa.getMemberCount();j++){
        			EShape_aspect sa = aSa.getByIndex(j);  
	            AShape_aspect_relationship aSar = new AShape_aspect_relationship();
	            EShape_aspect_relationship sar = null;
	
	            CShape_aspect_relationship.usedinRelated_shape_aspect(null, sa, context.domain, aSa);
	            String link = "horizontal material link";
	            String name = null;
	            if (aSar.getMemberCount()>=1)
	            {
	                for (int i=1;i<=aSar.getMemberCount();i++)
	                {
	                    sar = aSar.getByIndex(i);
	                    if (sar.testName(null))
	                    {
	                        name = sar.getName(null);
	                        if (link.equals(name))
	                        {
	                            sar.deleteApplicationInstance();
	                        }
	                    }
	                }
	            }
        		}
        }

    }


	/**
	* Sets/creates data for vertical_material_link attribute.
	*
	* <p>
	*  attribute_mapping vertical_material_link_printed_part_template_material_link (vertical_material_link
	* , (*PATH*), printed_part_template_material_link);
	*  part_template_definition &lt;=
	*  product_definition_shape &lt;- 
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship 
	*  shape_aspect_relationship.name = 'vertical material link'} 
	*  shape_aspect_relationship.relating_shape_aspect -&gt; 
	*  shape_aspect =&gt;
	*  printed_part_template_material_link		*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setVertical_material_link(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testVertical_material_link(null))
		{
         //unset old values
         unsetVertical_material_link(context, armEntity);

         APrinted_part_template_material_link_armx aArmpptml = armEntity.getVertical_material_link(null);
         EPrinted_part_template_material_link_armx pptml = null;

         // PTD <- SA
         LangUtils.Attribute_and_value_structure[] saS =
				{new LangUtils.Attribute_and_value_structure(
					CShape_aspect.attributeOf_shape(null),
					armEntity)
				};
         EShape_aspect esa = (EShape_aspect)
            LangUtils.createInstanceIfNeeded(context,CShape_aspect.definition,saS);
        
         for (int i=1;i<=aArmpptml.getMemberCount();i++){
            jsdai.SProduct_property_definition_schema.EShape_aspect_relationship sar =
                (jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)
                 context.working_model.createEntityInstance(jsdai.SProduct_property_definition_schema.CShape_aspect_relationship.class);
            sar.setName(null,"vertical material link");
            pptml = aArmpptml.getByIndex(i);

            sar.setRelated_shape_aspect(null,esa);
            sar.setRelating_shape_aspect(null,pptml);
        }
		}
	}


	/**
	* Unsets/deletes data for vertical_material_link attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetVertical_material_link(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testVertical_material_link(null)){
     		AShape_aspect aSa = new AShape_aspect();
     		CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aSa);
     		for (int j=1;j<=aSa.getMemberCount();j++){
     			EShape_aspect sa = aSa.getByIndex(j);  

            AShape_aspect_relationship aSar = new AShape_aspect_relationship();
            EShape_aspect_relationship sar = null;

            CShape_aspect_relationship.usedinRelated_shape_aspect(null, sa, context.domain, aSar);
            String link = "vertical material link";
            String name = null;

            if (aSar.getMemberCount()>=1)
            {
                for (int i=1;i<=aSar.getMemberCount();i++)
                {
                    sar = aSar.getByIndex(i);
                    if (sar.testName(null))
                    {
                        name = sar.getName(null);
                        if (link.equals(name))
                        {
                            sar.deleteApplicationInstance();
                        }
                    }
                }
            }
        }
		}
    }


	/**
	* Sets/creates data for transmission_line_model attribute.
	*
	* <p>
	*  attribute_mapping transmission_line_model_analytical_model (transmission_line_model
	* , (*PATH*), analytical_model);
	* 	part_template_definition <=
	* 	product_definition_shape <=
	* 	property_definition <-
	* 	property_definition_rel.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation => 
	* 	analytical_model ??? <-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition
	*  characterized_product_definition = product_definition
	*  product_definition =&gt;
	*  analytical_model_definition</refpath>
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTransmission_line_model(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
        if (armEntity.testTransmission_line_model(null))
		{
            EAnalytical_model_definition_armx armAnalytical_model = armEntity.getTransmission_line_model(null);

            //unset old values
            unsetTransmission_line_model(context, armEntity);

            // PD -> AMD
            LangUtils.Attribute_and_value_structure[] pdS =
				{new LangUtils.Attribute_and_value_structure(
					CProperty_definition.attributeDefinition(null),
					armAnalytical_model)
				};
            EProperty_definition pd = (EProperty_definition)
                LangUtils.createInstanceIfNeeded(context,CProperty_definition.definition,pdS);
            if(!pd.testName(null))
            	pd.setName(null, "");
            // PDR
            EProperty_definition_relationship pdr = (EProperty_definition_relationship) context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
            //set attributes
            pdr.setRelated_property_definition(null, armEntity);
            pdr.setRelated_property_definition(null, pd);
        }
	}


	/**
	* Unsets/deletes data for transmission_line_model attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTransmission_line_model(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testTransmission_line_model(null))
        {
            AProperty_definition_relationship apdr =  new AProperty_definition_relationship();
            EProperty_definition_relationship pdr = null;

            CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
            //geting other side - analytical model

            if (apdr.getMemberCount()==0){
                return;
            }
            else
            {
                for (int i=1;i<=apdr.getMemberCount();i++)
                {
                    pdr =  apdr.getByIndex(i);
                    if(pdr.getRelating_property_definition(null) instanceof EAnalytical_model_definition_armx)
                    	pdr.deleteApplicationInstance();
                }
            }
        }
	}


	/**
	* Sets/creates data for maximum_transmission_line_characteristic attribute.
	*
	* <p>
	*  attribute_mapping maximum_transmission_line_characteristic_analytical_representation (maximum_transmission_line_characteristic
	* , (*PATH*), analytical_representation);
	* 	part_template_definition <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	{property_definition
	* 	property_definition.name = `maximum transmission line characteristic'}
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation =>
	* 	analytical_representation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_transmission_line_characteristic(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaximum_transmission_line_characteristic(context, armEntity);

        if (armEntity.testMaximum_transmission_line_characteristic(null))
		{
            AAnalytical_model_application aAr = armEntity.getMaximum_transmission_line_characteristic(null);
            EAnalytical_model_application ar = null;
            for (int i=1;i<=aAr.getMemberCount();i++)
            {
                EProperty_definition_representation pdr = (EProperty_definition_representation)
                    context.working_model.createEntityInstance(CProperty_definition_representation.definition);

                pdr.setDefinition(null,armEntity);

                ar = aAr.getByIndex(i);

                pdr.setUsed_representation(null,ar);
                CxAP210ARMUtilities.setProperty_definition_representationName(context, pdr, "maximum transmission line characteristic");
            }
        }
	}


	/**
	* Unsets/deletes data for maximum_transmission_line_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_transmission_line_characteristic(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
        if (armEntity.testMaximum_transmission_line_characteristic(null))
        {
            String mtlc = "maximum transmission line characteristic";

            AProperty_definition_representation aPdr = new AProperty_definition_representation();
            EProperty_definition_representation pdr = null;

            CProperty_definition_representation.usedinDefinition(null,armEntity,context.domain,aPdr);
            if (aPdr.getMemberCount()>=1)
            {
                for (int j=1;j<=aPdr.getMemberCount();j++)
                {
                  pdr = aPdr.getByIndex(j);
               	if((pdr.testName(null))&&(pdr.getName(null).equals(mtlc)))
                    pdr.deleteApplicationInstance();
                }
            }
        }
	}


	/**
	* Sets/creates data for minimum_transmission_line_characteristic attribute.
	*
	* <p>
	*  attribute_mapping minimum_transmission_line_characteristic_analytical_representation (minimum_transmission_line_characteristic
	* , (*PATH*), analytical_representation);
	* 	part_template_definition <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	{property_definition
	* 	property_definition.name = `minimum transmission line characteristic'}
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation =>
	* 	analytical_representation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMinimum_transmission_line_characteristic(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMinimum_transmission_line_characteristic(context, armEntity);

        if (armEntity.testMinimum_transmission_line_characteristic(null)){
         AAnalytical_model_application aAr = armEntity.getMaximum_transmission_line_characteristic(null);
         EAnalytical_model_application ar = null;
         for (int i=1;i<=aAr.getMemberCount();i++)
         {
             EProperty_definition_representation pdr = (EProperty_definition_representation)
                 context.working_model.createEntityInstance(CProperty_definition_representation.definition);

             pdr.setDefinition(null,armEntity);

             ar = aAr.getByIndex(i);

             pdr.setUsed_representation(null,ar);
             CxAP210ARMUtilities.setProperty_definition_representationName(context, pdr, "minimum transmission line characteristic");
         }
        }
	}


	/**
	* Unsets/deletes data for minimum_transmission_line_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMinimum_transmission_line_characteristic(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testMinimum_transmission_line_characteristic(null))
        {
            String mtlc = "minimum transmission line characteristic";
            AProperty_definition_representation aPdr = new AProperty_definition_representation();
            EProperty_definition_representation pdr = null;

            CProperty_definition_representation.usedinDefinition(null,armEntity,context.domain,aPdr);
            if (aPdr.getMemberCount()>=1)
            {
                for (int j=1;j<=aPdr.getMemberCount();j++)
                {
                  pdr = aPdr.getByIndex(j);
               	if((pdr.testName(null))&&(pdr.getName(null).equals(mtlc)))
                    pdr.deleteApplicationInstance();
                }
            }
        }
	}


	/**
	* Sets/creates data for width attribute.
	*
	* <p>
	*  attribute_mapping width_length_data_element (maximum_width
	* , (*PATH*), length_data_element);
	* 	part_template_definition <=
	*  product_definition_shape <=
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `maximum width'}
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
	public static void setWidth(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		unsetWidth(context, armEntity);
		if (armEntity.testWidth(null))
		{
	      ETolerance_characteristic characteristic = armEntity.getWidth(null);
	      
	      CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "width", characteristic);
		}
	}


	/**
	* Unsets/deletes data for maximum_width attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetWidth(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "width");		
	}


	/**
	* Sets/creates data for unit_length attribute.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setUnit_length(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testUnit_length(null)){
			throw new SdaiException(SdaiException.FN_NAVL, " TODO- need to implement this method for "+armEntity);
		}
	}


	/**
	* Unsets/deletes data for Unit_length attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetUnit_length(SdaiContext context, EPrinted_part_cross_section_template_armx armEntity) throws SdaiException
	{
        if (armEntity.testUnit_length(null))
        {
			throw new SdaiException(SdaiException.FN_NAVL, " TODO- need to implement this method for "+armEntity);
        }
	}
	
}


