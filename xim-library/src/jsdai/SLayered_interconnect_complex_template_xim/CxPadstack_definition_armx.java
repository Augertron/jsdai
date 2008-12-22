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
import jsdai.util.LangUtils;
import jsdai.SLayered_interconnect_complex_template_mim.*;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SShape_property_assignment_xim.*;

public class CxPadstack_definition_armx extends CPadstack_definition_armx implements EMappedXIMEntity
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

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
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

		setTemp("AIM", CPadstack_definition.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		
		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
		setId_x(context, this);

		// Kind of AIM gap
		setName(null, "");
		
		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);
		
		// setReference_implementation(context, this);
		
		setPadstack_location(context, this);
		
		setLocation(context, this);

		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
		unsetLocation(null);		
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

		//unsetReference_implementation(context, this);
		
		unsetPadstack_location(context, this);
		
		unsetLocation(context, this);		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	footprint_definition &lt;= 
	* 	part_template_definition &lt;=
	* 	product_definition
	* 	{[product_definition
	* 	product_definition.formation -&gt;
	* 	product_definition_formation
	* 	product_definition_formation.of_product -&gt;
	* 	product &lt;-
	* 	product_related_product_category.products[i]
	* 	product_related_product_category &lt;=
	* 	product_category
	* 	product_category.name = 'template model'] (* Comes from supertype *)
	* 	[product_definition
	* 	product_definition.frame_of_reference -&gt;
	* 	product_definition_context &lt;=
	* 	application_context_element
	* 	application_context_element.name = 'layout design usage']} issue 5 in stepmod	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPadstack_definition_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxTemplate_definition.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPadstack_definition_armx armEntity) throws SdaiException
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

 	/**
 	* Sets/creates data for location attribute.
 	*
	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/

 	public static void setLocation(SdaiContext context, EMulti_stratum_structured_template_armx armEntity) throws SdaiException
 	{
 		CxMulti_stratum_structured_template_armx.setLocation(context, armEntity); 		
 	}


 	/**
 	* Unsets/deletes data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetLocation(SdaiContext context, EMulti_stratum_structured_template_armx armEntity) throws SdaiException
   {
 		CxMulti_stratum_structured_template_armx.unsetLocation(context, armEntity); 		
 	}
 	
	//********** "padstack_definition" attributes

	/**
	* Sets/creates data for reference_implementation attribute.
	*
	* <p>
	*  attribute_mapping reference_implementation_reference_packaged_part_interconnect_implementation (reference_implementation
	* , (*PATH*), reference_packaged_part_interconnect_implementation);
	* 	padstack_definition <=
	* 	product_definition <-
	* 	product_definition_relationship.related_product_definition
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `reference implementation'}
	* 	product_definition_relationship.relating_product_definition ->
	* 	product_definition =>
	* 	physical_unit =>
	* 	reference_packaged_part_interconnect_implementation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
 	// REMOVED
       // PD <- PDR -> RPPII
/* 	
      public static void setReference_implementation(SdaiContext context,
                                             EPadstack_definition_armx armEntity) throws
         SdaiException {
         //unset old values
         unsetReference_implementation(context, armEntity);

         if (armEntity.testReference_implementation(null)) {
            // RPPII
            EReference_packaged_part_interconnect_implementation armRPPII = armEntity.getReference_implementation(null);
           // PDR
           EProduct_definition_relationship epdr = (EProduct_definition_relationship)
              context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
           epdr.setName(null, "reference implementation");
           epdr.setId(null, "");
           epdr.setRelated_product_definition(null, armEntity);
           epdr.setRelating_product_definition(null, armRPPII);
         }
      }
*/
     /**
      * Unsets/deletes data for Reference_implementation attribute.
      *
      * @param context SdaiContext.
      * @param armEntity arm entity.
      * @throws SdaiException
      */
/* REMOVED
     public static void unsetReference_implementation(SdaiContext context, EPadstack_definition_armx armEntity) throws SdaiException {
     
           AProduct_definition_relationship apdr = new AProduct_definition_relationship();
           CProduct_definition_relationship.usedinRelated_product_definition(
              null, armEntity, context.domain, apdr);
           for (int i = 1; i <= apdr.getMemberCount(); i++) {
              EProduct_definition_relationship epdr = apdr.getByIndex(i);
              if(epdr.getName(null).equals("reference implementation"))
                 epdr.deleteApplicationInstance();
           }
     }
*/
 	
 	
   /**
	* Sets/creates data for padstack_location attribute.
	*
	* <p>
	* attribute_mapping padstack_location (padstack_location);
	*  padstack_definition <= 
	*  product_definition_shape <= 
	*  property_definition <- 
	*  property_definition_representation.definition 
	*  property_definition_representation 
	*  property_definition_representation.used_representation -> 
	*  representation 
	*  {representation.name = `padstack parametric data'} 
	*  representation.items[i] -> 
	*  {representation_item 
	*  representation_item.name = `padstack location'} 
	*  representation_item => 
	*  descriptive_representation_item 
	*  {descriptive_representation_item 
	*  (descriptive_representation_item.description = `top') 
	*  (descriptive_representation_item.description = `bottom') 
	*  (descriptive_representation_item.description = `symmetrical')} 
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
   // PD <- PD <- PDR -> R -> DRI
   public static void setPadstack_location(SdaiContext context, EPadstack_definition_armx armEntity) throws SdaiException {
	  //unset old values
	  unsetPadstack_location(context, armEntity);

	  if (armEntity.testLocation(null)) {
		 int location = armEntity.getLocation(null);
		 String locationValue = ETemplate_arrangement.toString(location).toLowerCase();
       String keyword2 = "dri_placement_location_"+locationValue;
       Object temp = CxAP210ARMUtilities.mostlyUsedInstances.get(keyword2);
       jsdai.SQualified_measure_schema.EDescriptive_representation_item edri;
       if(temp == null){
       	LangUtils.Attribute_and_value_structure[] structure = {
			   new LangUtils.Attribute_and_value_structure(
			   	jsdai.SQualified_measure_schema.CDescriptive_representation_item.attributeName(null), 
			   	"padstack location"),
			   new LangUtils.Attribute_and_value_structure(
			   	jsdai.SQualified_measure_schema.CDescriptive_representation_item.attributeDescription(null), 
					locationValue)
			   };
		   edri = 
			   (jsdai.SQualified_measure_schema.EDescriptive_representation_item)
		   LangUtils.createInstanceIfNeeded(context, jsdai.SQualified_measure_schema.CDescriptive_representation_item.definition, structure);
		   CxAP210ARMUtilities.mostlyUsedInstances.put(keyword2, edri);  
       }
       else{
       	edri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item)temp;
       }
		 ERepresentation representation = CxAP210ARMUtilities.findRepresentationShort(context, armEntity, "padstack parametric data", null);

		 if (!representation.testItems(null)) {
			representation.createItems(null);
		 }
		 representation.getItems(null).addUnordered(edri);
	  }
   }

   /**
	* Unsets/deletes data for padstack_location attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
   public static void unsetPadstack_location(SdaiContext context,
											   EPadstack_definition armEntity) throws
	  SdaiException {
	  CxAP210ARMUtilities.clearRepresentationItems(context, armEntity,
												   null, "padstack parametric data",
												   "padstack location");
   }
    
//********** "template_definition" attributes
}
