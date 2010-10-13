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

package jsdai.SPart_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGroup_mim.CApplied_group_assignment;
import jsdai.SPart_template_mim.CPart_template_definition;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.CxProduct_view_definition;
import jsdai.SProduct_view_definition_xim.EProduct_view_definition;

public class CxTemplate_definition extends CTemplate_definition implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition
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

	// END OF Property_definition
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPart_template_definition.definition);

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

		// Clean ARM specific attributes
//		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);
		
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

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  part_template_definition <=
	*  product_definition
	*  {product_definition.frame_of_reference ->
	*  product_definition_context <=
	*  application_context_element
	*  application_context_element.name = 'template definition'}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
//		CxItem_shape.setMappingConstraints(context, armEntity);
		CxProduct_view_definition.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.assignPart_definition_type(context, armEntity, "template definition");
		
		// Important AIM gap
		armEntity.setDefinition(null, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
	{
//		CxItem_shape.unsetMappingConstraints(context, armEntity);
		CxProduct_view_definition.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.deassignPart_definition_type(context, armEntity, "template definition");
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
 	*
 	*  attribute_mapping physical_characteristic_characteristic (physical_characteristic
 	* , (*PATH*), characteristic);
 	* 	part_template_definition &lt;=
 	*  product_definition_shape &lt;=
 	*  property_definition &lt;-
 	*  property_definition_representation.definition
 	*  property_definition_representation
 	*  property_definition_representation.used_representation -&gt;
 	*  representation
 	*  representation.items[i] -&gt;
 	*  representation_item
 	*  {representation_item
 	*  groupable_item = representation_item
 	*  groupable_item &lt;-
 	*  applied_group_assignment.items[i]
 	*  applied_group_assignment &lt;=
 	*  group_assignment
 	*  group_assignment.assigned_group -&gt;
 	*  group =&gt;
 	*  characteristic_type}
 	* end_attribute_mapping;
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
    // PTD <- PD <- PDR -> R -> RI <- AGA -> CT
 	public static void setPhysical_characteristic(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
 	{
 		//unset old values
 		unsetPhysical_characteristic(context, armEntity);

      if (armEntity.testPhysical_characteristic(null)){

 			ACorrelated_or_independent aArmPhysical_characteristic = armEntity.getPhysical_characteristic(null);
 			boolean firstPath = false;

         // PD <- PDR
         jsdai.SProduct_property_representation_schema.AProperty_definition_representation aProperty_definition_representation = new jsdai.SProduct_property_representation_schema.AProperty_definition_representation();
         jsdai.SProduct_property_representation_schema.EProperty_definition_representation property_definition_representation = null;
         jsdai.SProduct_property_representation_schema.CProperty_definition_representation.usedinDefinition(null,armEntity,context.domain,aProperty_definition_representation);
         if (aProperty_definition_representation.getMemberCount()==0){
         	property_definition_representation = (jsdai.SProduct_property_representation_schema.EProperty_definition_representation) context.working_model.createEntityInstance(jsdai.SProduct_property_representation_schema.CProperty_definition_representation.class);
            property_definition_representation.setDefinition(null, armEntity);
         }
         // PDR -> R
         jsdai.SRepresentation_schema.ERepresentation representation;
         if (property_definition_representation.testUsed_representation(null))
         	representation = property_definition_representation.getUsed_representation(null);
         else{
         	representation = (jsdai.SRepresentation_schema.ERepresentation) context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
            property_definition_representation.setUsed_representation(null,representation);
         }
         if (!representation.testItems(null))
         	representation.createItems(null);
         EMappedXIMEntity armCharacteristic = null;
         if (firstPath){
         	for (int ii = 1; ii <= aArmPhysical_characteristic.getMemberCount(); ii++){
         		armCharacteristic = (EMappedXIMEntity) aArmPhysical_characteristic.getByIndexEntity(ii);
               armCharacteristic.createAimData(context);
               representation.getItems(null).addUnordered(armCharacteristic);
         	}
         }
         //uses different mapping path
         else{
         	for (int ii = 1; ii <= aArmPhysical_characteristic.getMemberCount(); ii++){
         		armCharacteristic = (EMappedXIMEntity) aArmPhysical_characteristic.getByIndexEntity(ii);
               armCharacteristic.createAimData(context);
               representation.getItems(null).addUnordered(armCharacteristic);
         	}
         }
 		}
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
         if (armEntity.testPhysical_characteristic(null))
         {
             //how with different mapping paths ?
             jsdai.SProduct_property_representation_schema.AProperty_definition_representation aPdr = new jsdai.SProduct_property_representation_schema.AProperty_definition_representation();
             jsdai.SProduct_property_representation_schema.CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, aPdr);
             jsdai.SProduct_property_representation_schema.EProperty_definition_representation ePdr = aPdr.getByIndex(1);

             jsdai.SRepresentation_schema.ERepresentation representation = ePdr.getUsed_representation(null);
             if (!representation.testItems(null)) {
                 return;
             }

             AEntity items = representation.getItems(null);
             AEntity assignments = new AEntity();
             EEntity item = null;
             int i = 1;
             while (i <= items.getMemberCount())
             {
                 item = items.getByIndexEntity(i);
                 assignments.clear();
                 CApplied_group_assignment.usedinItems(null, item, context.domain, assignments);
                 // TODO - NEED review
                 if (assignments.getMemberCount() > 0 ) //|| item instanceof ECoordinated_representation_item)
                     items.removeUnordered(item);
                 else
                     i++;
             }
         }
 	}
    
    
//********** "template_definition" attributes
}
