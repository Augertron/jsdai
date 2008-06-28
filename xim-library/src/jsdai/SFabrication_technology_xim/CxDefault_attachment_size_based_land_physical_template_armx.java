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
import jsdai.SFabrication_technology_mim.*;
import jsdai.SFeature_and_connection_zone_xim.*;
import jsdai.SPhysical_layout_template_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxDefault_attachment_size_based_land_physical_template_armx extends CDefault_attachment_size_based_land_physical_template_armx implements EMappedXIMEntity
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
*/
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
	
	// From CShape_aspect.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect type) throws SdaiException {
		return test_string(a12);
	}
	public String getName(EShape_aspect type) throws SdaiException {
		return get_string(a12);
	}*/
	public void setName(EShape_aspect type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetName(EShape_aspect type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
		return a11$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a15);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a15);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a14 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a14 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a14$;
	}
	
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a14$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a14);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		return (EProduct_definition_shape)get_instance(a14);
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a13 = set_instance(a13, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a13 = unset_instance(a13);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a13$;
	}
	// ENDOF From CShape_aspect.java
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDefault_attachment_size_based_land_physical_template.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		
		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		setId_x(context, this);
		// It is derived
		// setId_x(context, (EShape_element)this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

      // setOf_stratum_technology(context, this);
      
      // minimum_attachment_region_size
      setMinimum_attachment_region_size(context, this);
		
      // maximum_attachment_region_size       
		// setMaximum_attachment_region_size(context, this);
		
		// Clean ARM specific attributes
		unsetId_x((EItem_shape)null);
        // It is derived
		// unsetId_x((EShape_element)null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
        // unsetOf_stratum_technology(null);

      // unsetMinimum_attachment_region_size(null);
	// unsetMaximum_attachment_region_size(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		unsetId_x(context, this);
        // It is derived
		// unsetId_x(context, (EShape_element)this);

		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

      // unsetOf_stratum_technology(context, this);

      // minimum_attachment_region_size
      unsetMinimum_attachment_region_size(context, this);
		
      // maximum_attachment_region_size       
		// unsetMaximum_attachment_region_size(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  (land_physical_template &lt;=
	*  part_template_definition &lt;=
	*  {product_definition
	*  product_definition.description = 'default attachment size based'})	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EDefault_attachment_size_based_land_physical_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
		armEntity.setOf_shape(null, armEntity);
		
		// no more constraints as it is ABS and only subtypes have more magic strings
		CxLand_physical_template_armx.setMappingConstraints(context, armEntity);
		CxShape_feature.setMappingConstraints(context, armEntity);
		armEntity.setDescription((EProduct_definition)null, "default attachment size based");
//		if(!armEntity.testName((EShape_aspect)null)){
			armEntity.setName((EShape_aspect)null, "");
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EDefault_attachment_size_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxLand_physical_template_armx.unsetMappingConstraints(context, armEntity);
		CxShape_feature.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((EProduct_definition)null);
	}	
	//********** "managed_design_object" attributes

    /**
     * Sets/creates data for Id_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
/*	 It is derived	
    public static void setId_x(SdaiContext context, EShape_element armEntity) throws SdaiException {
       CxShape_element.setId_x(context, armEntity);
    }
*/
  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
/*	 It is derived	
    public static void unsetId_x(SdaiContext context, EShape_element armEntity) throws SdaiException {
      CxShape_element.unsetId_x(context, armEntity);
   }
*/	
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
	//********** "default_attachment_size_based_land_physical_template" attributes

	/**
	* Sets/creates data for minimum_attachment_region_size attribute.
	*
	* <p>
	*  attribute_mapping minimum_attachment_region_size_connection_zone (minimum_attachment_region_size
	* , (*PATH*), connection_zone);
	* 	land_physical_template <=
	* 	part_template_definition <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `minimum attachment region size'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	shape_aspect.description = `connection zone'}
	* 	shape_aspect
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMinimum_attachment_region_size(SdaiContext context, EDefault_attachment_size_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxShape_feature.setConnection_area(context, armEntity);
	}


	/**
	* Unsets/deletes data for minimum_attachment_region_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMinimum_attachment_region_size(SdaiContext context, EDefault_attachment_size_based_land_physical_template_armx armEntity) throws SdaiException
	{
		CxShape_feature.unsetConnection_area(context, armEntity);
    }


	/**
	* Sets/creates data for maximum_attachment_region_size attribute.
	*
	* <p>
	*  attribute_mapping maximum_attachment_region_size_connection_zone (maximum_attachment_region_size
	* , (*PATH*), connection_zone);
	* 	land_physical_template <=
	* 	part_template_definition <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `maximum attachment region size'}
	* 	shape_aspect_relationship
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	shape_aspect.description = `connection zone'}
	* 	shape_aspect
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/* Vanished according SEDSZILA issue #2344	
	public static void setMaximum_attachment_region_size(SdaiContext context, EDefault_attachment_size_based_land_physical_template_armx armEntity) throws SdaiException
	{
        if (armEntity.testMaximum_attachment_region_size(null))
		{

			EConnection_zone armCz = armEntity.getMaximum_attachment_region_size(null);
         // PTD <- SA
			LangUtils.Attribute_and_value_structure[] saStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CShape_aspect.attributeOf_shape(null),
					armEntity)};

			EShape_aspect esa = (EShape_aspect) LangUtils
					.createInstanceIfNeeded(context,
							CShape_aspect.definition,
							saStructure);
         if(!esa.testName(null))
         	esa.setName(null, "");
         if(!esa.testProduct_definitional(null))
         	esa.setProduct_definitional(null, ELogical.UNKNOWN);
         
         //SAR
         jsdai.SProduct_property_definition_schema.EShape_aspect_relationship sar =
             (jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)
                 context.working_model.createEntityInstance(jsdai.SProduct_property_definition_schema.CShape_aspect_relationship.class);
         sar.setName(null,"maximum attachment region size");
         sar.setRelating_shape_aspect(null,esa);
         sar.setRelated_shape_aspect(null,armCz);
		}
	}
*/

	/**
	* Unsets/deletes data for maximum_attachment_region_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Vanished according SEDSZILA issue #2344		
	public static void unsetMaximum_attachment_region_size(SdaiContext context, EDefault_attachment_size_based_land_physical_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testMinimum_attachment_region_size(null))
        {
     		AShape_aspect aSa = new AShape_aspect();
     		CShape_aspect.usedinOf_shape(null, armEntity, context.domain, aSa);
     		for(int j=1;j<=aSa.getMemberCount();j++){
            AShape_aspect_relationship aSar = new AShape_aspect_relationship();
            EShape_aspect_relationship sar = null;
            CShape_aspect_relationship.usedinRelating_shape_aspect(null,aSa.getByIndex(j),context.domain,aSar);
            for(int i=1;i<=aSar.getMemberCount();)
            {
                sar = aSar.getByIndex(i);
                if (sar.testName(null) && sar.getName(null).equals("maximum attachment region size"))
                {
                   aSar.removeByIndex(i);
                   sar.deleteApplicationInstance();
                   continue;
                }
                i++;
            }
     		}
        }
	}*/

	
}
