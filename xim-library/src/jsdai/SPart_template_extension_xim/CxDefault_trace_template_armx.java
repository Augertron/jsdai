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

package jsdai.SPart_template_extension_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SFabrication_technology_xim.EStratum_technology_armx;
import jsdai.SPart_template_extension_mim.CDefault_trace_template;
import jsdai.SPhysical_layout_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxDefault_trace_template_armx extends CDefault_trace_template_armx implements EMappedXIMEntity
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

		setTemp("AIM", CDefault_trace_template.definition);

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

		// trace_style 
		setTrace_style(context, this);
		
		setOf_stratum_technology(context, this);
		
		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);

		// trace_style 
		unsetTrace_style(null);
		
		unsetOf_stratum_technology(null);		
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
		
		// trace_style 
		unsetTrace_style(context, this);
	
		unsetOf_stratum_technology(context, this);		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  part_template_definition &lt;=
	*  {product_definition
	*  (product_definition.description = 'trace template')
	*  (product_definition.description = 'default trace template')}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EDefault_trace_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxTrace_template_armx.setMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EDefault_trace_template_armx armEntity) throws SdaiException
	{
		CxTrace_template_armx.unsetMappingConstraints(context, armEntity);
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
 	



	//********** "trace_template" attributes

	/**
	* Sets/creates data for trace_style attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTrace_style(SdaiContext context, ETrace_template_armx armEntity) throws SdaiException
	{
		CxTrace_template_armx.setTrace_style(context, armEntity);
	}


	/**
	* Unsets/deletes data for trace_style attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTrace_style(SdaiContext context, ETrace_template_armx armEntity) throws SdaiException
	{
		CxTrace_template_armx.unsetTrace_style(context, armEntity);
	}


	//********** "default_trace_template" attributes

	/**
	* Sets/creates data for of_stratum_technology attribute.
	*
	* <p>
	*  attribute_mapping of_stratum_technology_stratum_technology (of_stratum_technology
	* , (*PATH*), stratum_technology);
	* 	part_template_definition &lt;=
	*  product_definition 
	*  characterized_product_definition = product_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_definition &lt;-
	*  property_definition.definition
	*  property_definition =&gt;
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
	*  characterized_object
	*  {characterized_object.description = 'design layer'}
	*  characterized_object =&gt;
	*  stratum_technology
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PTD <- SA <- SAR -> SA -> PDS -> ST
	public static void setOf_stratum_technology(SdaiContext context, EDefault_trace_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testOf_stratum_technology(null))
		{

			EStratum_technology_armx armSt = armEntity.getOf_stratum_technology(null);
	        //unset old values
            unsetOf_stratum_technology(context, armEntity);
            // PTD <- SA
            LangUtils.Attribute_and_value_structure[] saS = {new LangUtils.Attribute_and_value_structure(
                    CShape_aspect.attributeOf_shape(null),armEntity)
            };
            EShape_aspect esa = (EShape_aspect)LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition,saS);
            if(!esa.testName(null))
            	esa.setName(null, "");
            if(!esa.testProduct_definitional(null))
            	esa.setProduct_definitional(null, ELogical.UNKNOWN);
            // PDS -> ST
/*            LangUtils.Attribute_and_value_structure[] pdsS = {new LangUtils.Attribute_and_value_structure(
                    CProduct_definition_shape.attributeDefinition(null),armSt)
            };
            EProduct_definition_shape epds = (EProduct_definition_shape)LangUtils.createInstanceIfNeeded(context, CProduct_definition_shape.definition,pdsS);
            if(!epds.testName(null))
            	epds.setName(null, "");*/
            // PDS -> ST
            LangUtils.Attribute_and_value_structure[] saSST = {new LangUtils.Attribute_and_value_structure(
                    CShape_aspect.attributeOf_shape(null),armSt)
            };
            EShape_aspect esaST = (EShape_aspect)LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition,saSST);
            if(!esaST.testName(null))
            	esaST.setName(null, "");
            if(!esaST.testProduct_definitional(null))
            	esaST.setProduct_definitional(null, ELogical.UNKNOWN);
            // SAR
            EShape_aspect_relationship esar = (EShape_aspect_relationship)context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
            esar.setName(null, "of stratum technology");
            esar.setRelated_shape_aspect(null, esa);
            esar.setRelating_shape_aspect(null, esaST);
            
		}
	}


	/**
	* Unsets/deletes data for of_stratum_technology attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetOf_stratum_technology(SdaiContext context, EDefault_trace_template_armx armEntity) throws SdaiException
	{
      if (armEntity.testOf_stratum_technology(null)){
        // SA
        AShape_aspect asa = new AShape_aspect();
        EShape_aspect sa = null;
        CShape_aspect.usedinOf_shape(null, armEntity ,context.domain, asa);
        SdaiIterator iterSA = asa.createIterator();
        while(iterSA.next()){
        		sa = asa.getCurrentMember(iterSA);
            AShape_aspect_relationship asar = new AShape_aspect_relationship();
            CShape_aspect_relationship.usedinRelated_shape_aspect(null, sa, context.domain, asar);
            EShape_aspect_relationship sar = null;
            SdaiIterator iterSAR = asar.createIterator();
            while(iterSAR.next()){
            	sar = asar.getCurrentMember(iterSAR);
            	if(sar.getName(null).equals("of stratum technology"))
            		sar.deleteApplicationInstance();
            }
        }
      }
	}
}
