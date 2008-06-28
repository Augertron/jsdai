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

package jsdai.SModel_parameter_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SModel_parameter_mim.CProduct_specific_parameter_value_assignment;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;

public class CxProduct_specific_parameter_value_assignment_armx
		extends
		CProduct_specific_parameter_value_assignment_armx implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From Characterized_object
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ECharacterized_object type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ECharacterized_object type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ECharacterized_object type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ECharacterized_object type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ECharacterized_object type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(ECharacterized_object type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(ECharacterized_object type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(ECharacterized_object type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(ECharacterized_object type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(ECharacterized_object type) throws SdaiException {
		return a1$;
	}
	// ENDOF From Characterized_object

	// Product_related_product_category
	// methods for attribute: products, base type: SET OF ENTITY
/*	public static int usedinProducts(EProduct_related_product_category type, EProduct instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testProducts(EProduct_related_product_category type) throws SdaiException {
		return test_aggregate(a4);
	}
	public AProduct getProducts(EProduct_related_product_category type) throws SdaiException {
		return (AProduct)get_aggregate(a4);
	}*/
	public AProduct createProducts(EProduct_related_product_category type) throws SdaiException {
		a4 = (AProduct)create_aggregate_class(a4, a4$,  AProduct.class, 0);
		return a4;
	}
	public void unsetProducts(EProduct_related_product_category type) throws SdaiException {
		unset_aggregate(a4);
		a4 = null;
	}
	public static jsdai.dictionary.EAttribute attributeProducts(EProduct_related_product_category type) throws SdaiException {
		return a4$;
	}	
	// ENDOF Product_related_product_category
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_specific_parameter_value_assignment.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			// Assigned_parameter
			setAssigned_parameter(context, this);

			// Of_product
			setOf_product(context, this);

			
			// Clean ARM specific attributes - this is DERIVED to some magic string

			// Assigned_parameter
			unsetAssigned_parameter(null);

			// Of_product
			unsetOf_product(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			// Assigned_parameter
			unsetAssigned_parameter(context, this);

			// Of_product
			unsetOf_product(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
		product_specific_parameter_value_assignment <=
		[characterized_object]
		[product_related_product_category]
	   end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EProduct_specific_parameter_value_assignment_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EProduct_specific_parameter_value_assignment_armx armEntity) throws SdaiException {
	}


	//********** "interconnect_module_usage_view" attributes

	/**
	* Sets/creates data for thickness_over_metal_requirement attribute.
	*
	* <p>
	*  attribute_mapping assigned_parameter(assigned_parameter, $PATH, Parameter_assignment_armx);
			product_specific_parameter_value_assignment <=
			characterized_object
			characterized_definition = characterized_object
			characterized_definition
			characterized_definition <-
			property_definition.definition
			property_definition
			{property_definition.description = 'assigned parameter'}
			property_definition <-
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation ->
			representation =>
			parameter_assignment_representation
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PSPVA <- PD <-PDR->PAR
	public static void setAssigned_parameter(SdaiContext context, EProduct_specific_parameter_value_assignment_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetAssigned_parameter(context, armEntity);

		if (armEntity.testAssigned_parameter(null))
		{
	      EParameter_assignment_armx epa = armEntity.getAssigned_parameter(null);
	      // PD
	      EProperty_definition epd = (EProperty_definition)
	      	context.working_model.createEntityInstance(CProperty_definition.definition);
	      epd.setName(null, "");
	      epd.setDescription(null, "assigned parameter");
	      epd.setDefinition(null, armEntity);
	      // PDR
	      EProperty_definition_representation epdr = (EProperty_definition_representation)
	      	context.working_model.createEntityInstance(CProperty_definition_representation.definition); 
	      epdr.setUsed_representation(null, epa);
	      epdr.setDefinition(null, epd);
		}
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssigned_parameter(SdaiContext context, EProduct_specific_parameter_value_assignment_armx armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterAPD = apd.createIterator();
		while(iterAPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterAPD);
			if((epd.testDescription(null))&&(epd.getDescription(null).equals("assigned parameter"))){
				AProperty_definition_representation apdr = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
				SdaiIterator iterAPDR = apdr.createIterator();
				while(iterAPDR.next()){
					EProperty_definition_representation epdr = apdr.getCurrentMember(iterAPDR);
					epdr.deleteApplicationInstance();
				}				
				epd.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for of_product attribute.
	*
	* <p>
	*  attribute_mapping of_product(of_product, $PATH, Product);
			product_specific_parameter_value_assignment <=
			product_related_product_category
			product_related_product_category.products[i] ->
			product
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setOf_product(SdaiContext context, EProduct_specific_parameter_value_assignment_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetOf_product(context, armEntity);

		if (armEntity.testOf_product(null))
		{
	      EProduct product = armEntity.getOf_product(null);
	      armEntity.createProducts(null).addUnordered(product);
		}
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_dielectric_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetOf_product(SdaiContext context, EProduct_specific_parameter_value_assignment_armx armEntity) throws SdaiException
	{
		armEntity.unsetProducts(null);
	}

}