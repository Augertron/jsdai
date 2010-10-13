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

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SCharacteristic_xim.ELength_tolerance_characteristic;
import jsdai.SFabrication_technology_mim.CStratum_sub_stack;
import jsdai.SFabrication_technology_xim.AStratum_technology_occurrence_link_armx;
import jsdai.SFabrication_technology_xim.EStratum_technology_occurrence_link_armx;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;

public class CxStratum_sub_stack_armx extends CStratum_sub_stack_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	
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
		return test_string(a4);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a4$;
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

		setTemp("AIM", CStratum_sub_stack.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		
		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
//		setId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

	    // stack_thickness : OPTIONAL length_tolerance_characteristic;
		setStack_thickness(context, this);
		
        // stratum_technology_sequence : OPTIONAL SET [1:?] OF stratum_technology_occurrence_link_armx;
		setStratum_technology_sequence(context, this);
		
        // associated_stackup : design_stack_model_armx;
		setAssociated_stackup(context, this);
		
		// Clean ARM specific attributes
//		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);

	    // stack_thickness : OPTIONAL length_tolerance_characteristic;
		unsetStack_thickness(null);
		
        // stratum_technology_sequence : OPTIONAL SET [1:?] OF stratum_technology_occurrence_link_armx;
		unsetStratum_technology_sequence(null);
		
        // associated_stackup : design_stack_model_armx;
		unsetAssociated_stackup(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
//		unsetId_x(context, this);

		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);
	
	    // stack_thickness : OPTIONAL length_tolerance_characteristic;
		unsetStack_thickness(context, this);
		
        // stratum_technology_sequence : OPTIONAL SET [1:?] OF stratum_technology_occurrence_link_armx;
		unsetStratum_technology_sequence(context, this);
		
        // associated_stackup : design_stack_model_armx;
		unsetAssociated_stackup(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_sub_stack_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxTemplate_definition.setMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_sub_stack_armx armEntity) throws SdaiException
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
/* Removed from XIM - see bug #3610	
    public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
       CxItem_shape.setId_x(context, armEntity);
    }
*/
  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
/* Removed from XIM - see bug #3610
    public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
      CxItem_shape.unsetId_x(context, armEntity);
   }
*/
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
 	* Sets/creates data for stratum_technology_sequence attribute.
 	*
	attribute_mapping stratum_technology_sequence(stratum_technology_sequence, $PATH, Stratum_technology_occurrence_link_armx);
		stratum_sub_stack <=                
		part_template_definition <=
		product_definition_shape <=
		property_definition <-
		property_definition_relationship.related_property_definition
		{property_definition_relationship
		property_definition_relationship.name = 'stratum technology sequence'}
		property_definition_relationship.relating_property_definition ->
		property_definition =>
		stratum_technology_occurrence_relationship =>
		stratum_technology_occurrence_link
	end_attribute_mapping;
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void setStratum_technology_sequence(SdaiContext context, EStratum_sub_stack_armx armEntity) throws SdaiException
 	{
 		unsetStratum_technology_sequence(context, armEntity);
 		if(armEntity.testStratum_technology_sequence(null)){
 			AStratum_technology_occurrence_link_armx links = armEntity.getStratum_technology_sequence(null);
 			for(int i=1,count=links.getMemberCount(); i<=count; i++){
 				EStratum_technology_occurrence_link_armx link = links.getByIndex(i); 
 				EProperty_definition_relationship epdr = (EProperty_definition_relationship)
 		      		context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
 				epdr.setRelated_property_definition(null, armEntity);
 				epdr.setName(null, "stratum technology sequence");
 				epdr.setRelating_property_definition(null, link);
 			}	
 		}
 		
 	}


 	/**
 	* Unsets/deletes data for stratum_technology_sequence attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetStratum_technology_sequence(SdaiContext context, ETemplate_definition armEntity) throws SdaiException{
 		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
        CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
        for(int j=1,countPDR=apdr.getMemberCount(); j<=countPDR;j++){
        	EProperty_definition_relationship epdr = apdr.getByIndex(j);
       	 	if((epdr.testName(null))&&(epdr.getName(null).equals("stratum technology sequence"))){
       	 		epdr.deleteApplicationInstance();
       	 	}
        }
 	}

 	/**
 	* Sets/creates data for associated_stackup attribute.
 	*
	attribute_mapping associated_stackup(associated_stackup, $PATH, Design_stack_model_armx);
		stratum_sub_stack <=                
		part_template_definition <=
		product_definition_shape <=
		property_definition <-
		property_definition_relationship.related_property_definition
		{property_definition_relationship
		property_definition_relationship.name = 'associated stackup'}
		property_definition_relationship.relating_property_definition ->
		property_definition =>
		product_definition_shape =>
		part_template_definition =>
		stratum_stack_model =>
		design_stack_model
	end_attribute_mapping;
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void setAssociated_stackup(SdaiContext context, EStratum_sub_stack_armx armEntity) throws SdaiException
 	{
 		unsetAssociated_stackup(context, armEntity);
 		if(armEntity.testAssociated_stackup(null)){
 			EDesign_stack_model_armx edsma = armEntity.getAssociated_stackup(null);
			EProperty_definition_relationship epdr = (EProperty_definition_relationship)
	      		context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setRelated_property_definition(null, armEntity);
			epdr.setName(null, "associated stackup");
			epdr.setRelating_property_definition(null, edsma);
 		}
 	}


 	/**
 	* Unsets/deletes data for associated_stackup attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetAssociated_stackup(SdaiContext context, ETemplate_definition armEntity) throws SdaiException{
 		AProperty_definition_relationship apdr = new AProperty_definition_relationship();
        CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, apdr);
        for(int j=1,countPDR=apdr.getMemberCount(); j<=countPDR;j++){
        	EProperty_definition_relationship epdr = apdr.getByIndex(j);
       	 	if((epdr.testName(null))&&(epdr.getName(null).equals("associated stackup"))){
       	 		epdr.deleteApplicationInstance();
       	 	}
        }
 	}

 	/**
 	* Sets/creates data for stack_thickness attribute.
 	*
	attribute_mapping stack_thickness(stack_thickness, $PATH, Length_tolerance_characteristic);
		stratum_sub_stack <=                
		part_template_definition <=
		product_definition
		characterized_product_definition = product_definition
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		property_definition
		{property_definition.name = 'stack thickness'}
		property_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation
	end_attribute_mapping;
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void setStack_thickness(SdaiContext context, EStratum_sub_stack_armx armEntity) throws SdaiException
 	{
 		unsetStack_thickness(context, armEntity);
 		if(armEntity.testStack_thickness(null)){
 			ELength_tolerance_characteristic characteristic = armEntity.getStack_thickness(null);
 			
 			CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "stack thickness", characteristic);
 		}
 	}


 	/**
 	* Unsets/deletes data for stack_thickness attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetStack_thickness(SdaiContext context, ETemplate_definition armEntity) throws SdaiException{
 		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "stack thickness"); 		
 	}
 	
}
