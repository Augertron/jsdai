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

package jsdai.SNetwork_functional_design_view_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFunctional_usage_view_xim.CxFunctional_unit_definition;
import jsdai.SFunctional_usage_view_mim.CFunctional_unit;
import jsdai.SFunctional_usage_view_xim.EFunctional_unit_usage_view;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.CxItem_shape;
import jsdai.SShape_property_assignment_xim.EItem_shape;

public class CxFunctional_unit_network_definition extends CFunctional_unit_network_definition implements EMappedXIMEntity{

	EEntity getAimInstance(){
		return this;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

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

	// Product_view_definition
	/// methods for attribute: name_x, base type: STRING
/*	public boolean testName_x(EProduct_view_definition type) throws SdaiException {
		return test_string(a4);
	}
	public String getName_x(EProduct_view_definition type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setName_x(EProduct_view_definition type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetName_x(EProduct_view_definition type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName_x(EProduct_view_definition type) throws SdaiException {
		return a4$;
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

		setTemp("AIM", CFunctional_unit.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization
			setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);
			
			// usage_view 
			setUsage_view(context, this);

			
			setId_x(context, this);
			// clean ARM
			//additional_characterization
			unsetAdditional_characterization(null);

			//additional_context
			unsetAdditional_contexts(null);

			// usage_view 
			unsetUsage_view(null);
			
			unsetId_x(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization
			unsetAdditional_characterization(context, this);
			
			// usage_view 
			unsetUsage_view(context, this);
			
			unsetId_x(context, this);			
			// this.deleteApplicationInstance();
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
	 *  functional_unit &lt;=
	 *  product_definition
	 *  {product_definition
	 *  product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'functional network design'}
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EFunctional_unit_network_definition armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxFunctional_unit_definition.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.assignPart_definition_type(context, armEntity, "functional network design");		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EFunctional_unit_network_definition armEntity) throws SdaiException {
		CxFunctional_unit_definition.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.deassignPart_definition_type(context, armEntity, "functional network design");
	}

	//********** "design_discipline_item_definition" attributes

	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * <p>
	 * attribute_mapping additional_context_application_context
	 * (additional_context , $PATH, application_context); product_definition <-
	 * product_definition_context_association.definition
	 * product_definition_context_association
	 * {product_definition_context_association.role -> (* Modified by Audronis
	 * Gustas *) product_definition_context_role
	 * product_definition_context_role.name = 'additional context'}
	 * product_definition_context_association.frame_of_reference ->
	 * product_definition_context end_attribute_mapping;
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
	}

	/**
	 * Sets/creates data for name attribute.
	 * 
	 * <p>
	 * attribute_mapping name (name , product_definition.description);
	 * end_attribute_mapping;
	 * </p>
	 * <p>
	 * attribute_mapping name_multi_language_string (name ,
	 * ([product_definition.description] [$PATH]) ($PATH),
	 * multi_language_string); product_definition (attribute_language_item =
	 * product_definition attribute_language_item <-
	 * attribute_language_assignment.items[i] attribute_language_assignment
	 * {attribute_language_assignment <= attribute_classification_assignment
	 * attribute_classification_assignment.attribute_name = 'description'})
	 * (multi_language_attribute_item = product_definition
	 * multi_language_attribute_item <-
	 * multi_language_attribute_assignment.items[i]
	 * multi_language_attribute_assignment {multi_language_attribute_assignment <=
	 * attribute_value_assignment attribute_value_assignment.attribute_name =
	 * 'description'}) end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
	}



	//********** "functional_unit_network_definition" attributes

	/**
	* Sets/creates data for usage_view attribute.
	*
	* <p>
	*  attribute_mapping usage_view_functional_unit_usage_view (usage_view
	* , (*PATH*), functional_unit_usage_view);
	* 	functional_unit <=
	* 	product_definition <-
	* 	product_definition_relationship.related_product_definition
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `design usage'}
	* 	product_definition_relationship
	* 	product_definition_relationship.relating_product_definition ->
	* 	{product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `functional design usage'}
	* 	product_definition =>
	* 	(functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit =>
	* 	library_defined_functional_unit)
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setUsage_view(SdaiContext context, EFunctional_unit_network_definition armEntity) throws SdaiException
	{
		//unset old values
		unsetUsage_view(context, armEntity);

		if (armEntity.testUsage_view(null))
		{

			EFunctional_unit_usage_view armUsage_view = armEntity.getUsage_view(null);

			//product_definition_relationship
			EProduct_definition_relationship relationship = (EProduct_definition_relationship) context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
			relationship.setId(null, "");
			relationship.setName(null, "design usage");
			relationship.setRelated_product_definition(null, armEntity);
			relationship.setRelating_product_definition(null, armUsage_view);
		}
	}


	/**
	* Unsets/deletes data for usage_view attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetUsage_view(SdaiContext context, EFunctional_unit_network_definition armEntity) throws SdaiException
	{
		//product_definition_relationship
//		EProduct_definition_relationship relationship = (EProduct_definition_relationship) context.working_model.createEntityInstance(EProduct_definition_relationship.class);
		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);
		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			EProduct_definition_relationship relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("design usage")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
	

////Item_shape	
	public static void setId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.setId_x(context, armEntity);
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);
	}
	
}