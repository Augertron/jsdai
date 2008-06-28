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

package jsdai.SRequirement_decomposition_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SProduct_definition_schema.AProduct_definition_relationship;
import jsdai.SProduct_definition_schema.CProduct_definition_relationship;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_view_definition_xim.EProduct_view_definition;
import jsdai.SRequirement_decomposition_mim.CPredefined_requirement_view_definition;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxView_based_constraint_occurrence extends CView_based_constraint_occurrence implements EMappedXIMEntity
{

	
	// Taken from property_definition
	/// methods for attribute: name, base type: STRING

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

	public static jsdai.dictionary.EAttribute attributeDefinition(EProduct_definition type) throws SdaiException {
		return a2$;
	}
	
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setId(EProduct_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition type) throws SdaiException {
		return a0$;
	}
	
	// END OF taken from property_definition
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CPredefined_requirement_view_definition.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

			//required_analytical_representation
			setRequired_analytical_representation(context, this);

			//order improtant set of requirement_specification must be before required_specification
			//requirement_specification - made derived
			// setRequirement_specification(context, this);

			//required_specification
	      // Removed since WD34
			// setRequired_specification(context, this);

			//required_part
			// setRequired_part(context, this);

			//required_coordinated_characteristic - removed during modularization
			// setRequired_coordinated_characteristic(context, this);


			//required_characteristic
			setRequired_characteristic(context, this);

	      // life_cycle_context                                                : life_cycle_context_type;
	      // setLife_cycle_context(context, this);
	      
	      // domain_context                                                    : domain_context_type;
	      // setDomain_context(context, this);
			
			//required_material - INVERSE
			// setRequired_material(context, this);

			// logical_relation : OPTIONAL complex_clause_armx;
			setLogical_relation(context, this);
			
			setConstraining_part(context, this);

	      // Clean ARM
	      // unsetAssociated_definition(null);
	      unsetRequired_analytical_representation(null);
	      unsetRequired_functional_specification(null);
	      // unsetRequired_part(null);
	      // removed during modularization
	      // unsetRequired_coordinated_characteristic(null);
	      unsetRequired_characteristic(null);
	      unsetLogical_relation(null);

	      unsetConstraining_part(null);
	      // life_cycle_context                                                : life_cycle_context_type;
	      // unsetLife_cycle_context(null);
	      
	      // domain_context                                                    : domain_context_type;
	      // unsetDomain_context(null);
	      
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "Requirement_definition_property" attributes

		//required_analytical_representation
		unsetRequired_analytical_representation(context, this);

		//order improtant set of requirement_specification must be before required_specification
		//requirement_specification - made derived
		// unsetRequirement_specification(context, this);

		//required_specification
      // Removed since WD34
		// setRequired_specification(context, this);

		//required_part
		// unsetRequired_part(context, this);

		//required_coordinated_characteristic - removed during modularization
		// unsetRequired_coordinated_characteristic(context, this);


		//required_characteristic
		unsetRequired_characteristic(context, this);

		unsetLogical_relation(context, this);
		
		unsetConstraining_part(context, this);
		//required_material - INVERSE
		// setRequired_material(context, this);

      // life_cycle_context                                                : life_cycle_context_type;
      // unsetLife_cycle_context(context, this);
      
      // domain_context                                                    : domain_context_type;
      // unsetDomain_context(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	mapping_constraints;
		predefined_requirement_view_definition <=
		product_definition
		{product_definition
		(product_definition.description = 'shape and product definition based constraint')
		(product_definition.description = 'product definition based constraint')}
	end_mapping_constraints;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EView_based_constraint_occurrence armEntity) throws SdaiException
	{
		CxConstraint_occurrence.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "product definition based constraint");
	}

	public static void unsetMappingConstraints(SdaiContext context, EView_based_constraint_occurrence armEntity) throws SdaiException
	{
		CxConstraint_occurrence.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}
	
	
	//********** "managed_design_object" attributes

	//********** "Requirement_definition_property" attributes

	/**
	* Sets/creates data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_analytical_representation(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_analytical_representation(context, armEntity);
	}


	/**
	* Sets/creates data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void setRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.setRequired_specification(context, armEntity);
	}
*/

	/**
	* Unsets/deletes data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void unsetRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.unsetRequired_specification(context, armEntity);
	}
*/

	/**
	* Sets/creates data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_characteristic(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_characteristic(context, armEntity);
	}

	/**
	* Sets/creates data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLogical_relation(SdaiContext context, EConstraint_occurrence armEntity) throws SdaiException
	{
		CxConstraint_occurrence.setLogical_relation(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLogical_relation(SdaiContext context, EConstraint_occurrence armEntity) throws SdaiException
	{
		CxConstraint_occurrence.unsetLogical_relation(context, armEntity);
	}
	
/* This is now inverse attribute
	/
	* Sets/creates data for required_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void setRequired_material(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_material(context, armEntity);
	}


	/
	* Unsets/deletes data for required_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void unsetRequired_material(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.unsetRequired_material(context, armEntity);
	}
*/

	/**
	* Sets/creates data for Life_cycle_context attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed attribute
	public static void setLife_cycle_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setLife_cycle_context(context, armEntity);
	}
*/

	/**
	* Unsets/deletes data for Life_cycle_context attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed attribute	
	public static void unsetLife_cycle_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetLife_cycle_context(context, armEntity);
	}
*/
	/**
	* Sets/creates data for Domain_context attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed attribute
	public static void setDomain_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setDomain_context(context, armEntity);
	}
*/

	/**
	* Unsets/deletes data for Domain_context attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* Removed attribute	
	public static void unsetDomain_context(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetDomain_context(context, armEntity);
	}
*/	

	/**
	* Sets/creates data for mapping constraints.
	*
	attribute_mapping constraining_part(constraining_part, $PATH, Product_view_definition);
		predefined_requirement_view_definition <=
		product_definition <-
		product_definition_relationship.related_product_definition
		{product_definition_relationship
		product_definition_relationship.name = 'constraining part'}
		product_definition_relationship.relating_product_definition ->
		product_definition
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PRVD <- PDR -> PD
	public static void setConstraining_part(SdaiContext context, EView_based_constraint_occurrence armEntity) throws SdaiException
	{
		unsetConstraining_part(context, armEntity);
		if(armEntity.testConstraining_part(null)){
			EProduct_view_definition part = armEntity.getConstraining_part(null);
			// PDR
            EProduct_definition_relationship epdr = (EProduct_definition_relationship)
            	context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
            epdr.setRelated_product_definition(null, armEntity);
            epdr.setRelating_product_definition(null, part);
            epdr.setName(null, "constraining part");
            epdr.setId(null, "");
		}
	}

	public static void unsetConstraining_part(SdaiContext context, EView_based_constraint_occurrence armEntity) throws SdaiException
	{
		AProduct_definition_relationship apdr = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, apdr);
		SdaiIterator apdrIter = apdr.createIterator();
		while(apdrIter.next()){
			EProduct_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
			if(!(epdr.testName(null))||(!epdr.getName(null).equals("constraining part"))){
				continue;
			}
			epdr.deleteApplicationInstance();
		}
	}
	
}
