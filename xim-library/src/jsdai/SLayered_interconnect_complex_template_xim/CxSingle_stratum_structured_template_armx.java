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
import jsdai.SLayered_interconnect_complex_template_mim.CSingle_stratum_structured_template;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxSingle_stratum_structured_template_armx extends CSingle_stratum_structured_template_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;
	// Taken from Single_stratum_template_armx
	// methods for attribute: of_stratum_technology, base type: SET OF ENTITY
/*	public static int usedinOf_stratum_technology(ESingle_stratum_template_armx type, EStratum_technology_armx instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a11$, domain, result);
	}
	public boolean testOf_stratum_technology(ESingle_stratum_template_armx type) throws SdaiException {
		return test_aggregate(a11);
	}
	public AStratum_technology_armx getOf_stratum_technology(ESingle_stratum_template_armx type) throws SdaiException {
		return (AStratum_technology_armx)get_aggregate(a11);
	}
	public AStratum_technology_armx createOf_stratum_technology(ESingle_stratum_template_armx type) throws SdaiException {
		a11 = (AStratum_technology_armx)create_aggregate_class(a11, a11$,  AStratum_technology_armx.class, 0);
		return a11;
	}
	public void unsetOf_stratum_technology(ESingle_stratum_template_armx type) throws SdaiException {
		unset_aggregate(a11);
		a11 = null;
	}
	public static jsdai.dictionary.EAttribute attributeOf_stratum_technology(ESingle_stratum_template_armx type) throws SdaiException {
		return a11$;
	}*/
	// ENDOF Taken from Single_stratum_template_armx
	
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
		a2 = set_instanceX(a2, value);
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

		setTemp("AIM", CSingle_stratum_structured_template.definition);

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
		
		// Clean ARM specific attributes
		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
		// unsetOf_stratum_technology(null);
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
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ESingle_stratum_structured_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxStructured_template_armx.setMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, ESingle_stratum_structured_template_armx armEntity) throws SdaiException
	{
		CxStructured_template_armx.unsetMappingConstraints(context, armEntity);
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

    // Template_definition, originally developed in Part_template_definition
 	/**
 	* Sets/creates data for of_stratum_technology attribute.
 	*
 	*attribute_mapping of_stratum_technology(of_stratum_technology, $PATH, Stratum_technology_armx);
		single_stratum_template <=
		part_template_definition <=
		product_definition_shape <-
		shape_aspect.of_shape
		shape_aspect <-
		shape_aspect_relationship.related_shape_aspect
		shape_aspect_relationship
		{shape_aspect_relationship
		shape_aspect_relationship.name = 'of stratum technology'}
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect
		shape_aspect.of_shape ->
		product_definition_shape <=
		property_definition
		property_definition.definition ->
		characterized_definition = characterized_object
		characterized_object =>
		stratum_technology
		end_attribute_mapping;
	end_entity_mapping;
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
    // SST <- SA <- SAR -> SA -> PDS -> ST
/* 	public static void setOf_stratum_technology(SdaiContext context, ESingle_stratum_structured_template_armx armEntity) throws SdaiException
 	{
 		unsetOf_stratum_technology(context, armEntity);
 		if(armEntity.testOf_stratum_technology(null)){
 			AStratum_technology_armx ast = armEntity.getOf_stratum_technology(null);
 			// SST <- SA
			LangUtils.Attribute_and_value_structure[] esa1Structure =
			{new LangUtils.Attribute_and_value_structure(
				CShape_aspect.attributeOf_shape(null),
				armEntity)};
			EShape_aspect esa1 = (EShape_aspect)
				LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition, esa1Structure);
 			if(!esa1.testName(null)){
 				esa1.setName(null, "");
 			}
 			if(!esa1.testProduct_definitional(null)){
 				esa1.setProduct_definitional(null, ELogical.UNKNOWN);
 			}
			
 			SdaiIterator iter = ast.createIterator();
 			while(iter.next()){
 				EStratum_technology_armx est = ast.getCurrentMember(iter); 
	 			// PDS -> ST
	 			// SA -> PDS
				LangUtils.Attribute_and_value_structure[] esa2Structure =
				{new LangUtils.Attribute_and_value_structure(
					CShape_aspect.attributeOf_shape(null),
					est)};
				EShape_aspect esa2 = (EShape_aspect)
					LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition, esa2Structure);
	 			if(!esa2.testName(null)){
	 				esa2.setName(null, "");
	 			}
	 			if(!esa2.testProduct_definitional(null)){
	 				esa2.setProduct_definitional(null, ELogical.UNKNOWN);
	 			}
				// SAR
	 			EShape_aspect_relationship esar = (EShape_aspect_relationship)
	 				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
	 			esar.setRelated_shape_aspect(null, esa1);
	 			esar.setRelating_shape_aspect(null, esa2);
	 			esar.setName(null, "of stratum technology");
 			} 			
 		}
 	}
*/

 	/**
 	* Unsets/deletes data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
/* 	public static void unsetOf_stratum_technology(SdaiContext context, ESingle_stratum_structured_template_armx armEntity) throws SdaiException
   {
 		AShape_aspect asa = new AShape_aspect();
 		CShape_aspect.usedinOf_shape(null, armEntity, context.domain, asa);
 		SdaiIterator iterSA = asa.createIterator();
 		while(iterSA.next()){
 			EShape_aspect esa = asa.getCurrentMember(iterSA);
 			AShape_aspect_relationship asar = new AShape_aspect_relationship();
 	 		CShape_aspect_relationship.usedinRelated_shape_aspect(null, esa, context.domain, asar);
 	 		SdaiIterator iterSAR = asar.createIterator();
 	 		while(iterSAR.next()){
 	 			EShape_aspect_relationship esar = asar.getCurrentMember(iterSAR);
 	 			if((esar.testName(null))&&(esar.getName(null).equals("of stratum technology"))){
 	 				esar.deleteApplicationInstance();
 	 			}
 	 		}
 		}
 	}
*/ 	
 	
 	
}
