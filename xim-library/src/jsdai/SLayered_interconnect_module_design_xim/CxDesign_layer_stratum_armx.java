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

package jsdai.SLayered_interconnect_module_design_xim;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $$
* $ $
*/

import jsdai.SPart_view_definition_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SShape_property_assignment_xim.CxItem_shape;
import jsdai.SShape_property_assignment_xim.EItem_shape;
import jsdai.SLayered_interconnect_module_design_mim.CDesign_layer_stratum;
import jsdai.lang.*;
import jsdai.libutil.*;

public class CxDesign_layer_stratum_armx extends CDesign_layer_stratum_armx implements EMappedXIMEntity
{

	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a6$;
	}
	// ENDOF From CProperty_definition.java
	
	// Taken from Product_definition
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
	
	// ENDOF Taken from Product_definition
	
	// From STRATUM - property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a8);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a8 = get_instance_select(a8);
		return (EEntity)a8;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a8 = set_instanceX(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
	}
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDesign_layer_stratum.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "Part_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string
		// setAdditional_characterization(context, this);

		//additional_context - this is DERIVED to some magic string
		// setAdditional_contexts(context, this);

		// Id_x
		setId_x(context, this);
		
		//********** "stratum" attributes

		//of_technology
		setDefinition_x(context, this);
		
		// design_layer_technology
		setPrimary_design_layer_stratum(context, this);

		// Clean ARM - this is DERIVED to some magic string
		// unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetDefinition_x(null);
		unsetPrimary_design_layer_stratum(null);

		// Id_x
		unsetId_x(null);
		
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "Part_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string
		// unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		// Id_x
		unsetId_x(context, this);

		//********** "stratum" attributes

		//of_technology
		unsetDefinition_x(context, this);
		
		// Primary_design_layer_stratum
		unsetPrimary_design_layer_stratum(context, this);
		
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	stratum <=
	* 	product_definition
	* 	{product_definition
	* 	(product_definition.name = `design layer')
	* 	(product_definition.name = `documentation layer')
	* 	(product_definition.name = `generic stratum')}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		CxStratum_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		CxStratum_armx.unsetMappingConstraints(context, armEntity);
	}
	
	//********** "managed_design_object" attributes

	//********** "ee_product_definition" attributes
	/**
	* Unsets/deletes data for product_definition_id attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAdditional_contexts(SdaiContext context, EPart_view_definition armEntity) throws SdaiException
	{
		CxPart_view_definition.unsetAdditional_contexts(context, armEntity);
	}

   /**
    * Sets/creates data for id_x attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
   	CxItem_shape.setId_x(context, armEntity);
   }

 /**
  * Unsets/deletes data for id_x attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
 	CxItem_shape.unsetId_x(context, armEntity);
 }
	

	// Stratum

	/**
	* Sets/creates data for definition_x attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDefinition_x(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		CxStratum_armx.setDefinition_x(context, armEntity);		
	}


	/**
	* Unsets/deletes data for definition_x attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDefinition_x(SdaiContext context, EStratum_armx armEntity) throws SdaiException
	{
		CxStratum_armx.unsetDefinition_x(context, armEntity);		
	}
	
	//********** "design_layer_stratum" attributes

	/**
	* Sets/creates data for primary_design_layer_stratum attribute.
	*
	* <p>
	*  attribute_mapping primary_design_layer_stratum (primary_design_layer_stratum
	* , product_definition.description);
	* 	stratum <=
	* 	product_definition
	* 	product_definition.description
	* 	{(product_definition.description = `primary design layer stratum')
	* 	(product_definition.description = `non primary design layer stratum')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPrimary_design_layer_stratum(SdaiContext context, EDesign_layer_stratum_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetPrimary_design_layer_stratum(context, armEntity);

		if (armEntity.testPrimary_design_layer_stratum(null))
		{

			boolean armPrimary_design_layer_stratum = armEntity.getPrimary_design_layer_stratum(null);

			if (armPrimary_design_layer_stratum) {
				armEntity.setDescription((EProduct_definition)null, "primary design layer stratum");
			} else {
				armEntity.setDescription((EProduct_definition)null, "non primary design layer stratum");
			}
		}
	}


	/**
	* Unsets/deletes data for primary_design_layer_stratum attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPrimary_design_layer_stratum(SdaiContext context, EDesign_layer_stratum_armx armEntity) throws SdaiException
	{
		armEntity.unsetDescription((EProduct_definition)null);
	}	
	
}
