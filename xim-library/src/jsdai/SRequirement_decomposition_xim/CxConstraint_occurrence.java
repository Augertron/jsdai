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
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SPresentation_appearance_schema.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduction_rule_xim.EComplex_clause_armx;
import jsdai.SRepresentation_schema.*;
import jsdai.SRequirement_decomposition_mim.CPredefined_requirement_view_definition;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxConstraint_occurrence extends CConstraint_occurrence implements EMappedXIMEntity
{

	
	// Taken from property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProduct_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProduct_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProduct_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
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

	public static jsdai.dictionary.EAttribute attributeDefinition(EProduct_definition type) throws SdaiException {
		return a2$;
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

	      // Clean ARM
	      // unsetAssociated_definition(null);
	      unsetRequired_analytical_representation(null);
	      unsetRequired_functional_specification(null);
	      // unsetRequired_part(null);
	      // removed during modularization
	      // unsetRequired_coordinated_characteristic(null);
	      unsetRequired_characteristic(null);
	      unsetLogical_relation(null);

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
		(product_definition.id = 'constraint')
		((product_definition.description = 'shape and product definition based constraint')
		(product_definition.description = 'product definition based constraint'))}
	
	end_mapping_constraints;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EConstraint_occurrence armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "product definition based constraint");
		armEntity.setId(null, "constraint");
	}

	public static void unsetMappingConstraints(SdaiContext context, EConstraint_occurrence armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
		armEntity.unsetId(null);
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
	attribute_mapping logical_relation(logical_relation, $PATH, Complex_clause_armx);
		predefined_requirement_view_definition <=
		product_definition
		characterized_product_definition = product_definition
		characterized_product_definition
		characterized_definition = characterized_product_definition
		characterized_definition <-
		property_definition.definition
		{property_definition.name = 'logical relation'}
		property_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation.items[i] ->
		representation_item =>
		compound_representation_item =>
		complex_clause
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PRVD <- PD <- PDR -> R -> CC
	public static void setLogical_relation(SdaiContext context, EConstraint_occurrence armEntity) throws SdaiException
	{
		unsetLogical_relation(context, armEntity);
		if(armEntity.testLogical_relation(null)){
			EComplex_clause_armx clause = armEntity.getLogical_relation(null);
			//1) R -> CC
			ARepresentation representations = new ARepresentation();
			CRepresentation.usedinItems(null, clause, context.domain, representations);
			SdaiIterator iter = representations.createIterator();
			ERepresentation suitableRepresentation = null;
			top: while(iter.next()){
				ERepresentation rep = representations.getCurrentMember(iter);
				ARepresentation_item items = rep.getItems(null);
				int count = items.getMemberCount();
				// Make sure the path is unambigous
				if(count == 1){
					suitableRepresentation = rep;
					break;
				}
				for(int i=1; i<=count; i++){
					ERepresentation_item item = items.getByIndex(i);
					// rep makes path ambigous - skip it
					if((item instanceof EFill_area_style_hatching)||
						(item instanceof EExternally_defined_hatch_style)||
						(item instanceof EFill_area_style_tiles)||
						(item instanceof EExternally_defined_tile_style)){
						if(item != clause){
							continue top;
						}
					}
				}
				suitableRepresentation = rep;
				break;
			}
			if(suitableRepresentation == null){
				suitableRepresentation = CxAP210ARMUtilities.createRepresentation(context, "", false);
				suitableRepresentation.getItems(null).addUnordered(clause);
			}
			// PD
            EProperty_definition epd = (EProperty_definition)
            	context.working_model.createEntityInstance(CProperty_definition.definition);
            epd.setDefinition(null, armEntity);
            epd.setName(null, "logical relation");
			// PDR
            EProperty_definition_representation epdr = (EProperty_definition_representation)
            	context.working_model.createEntityInstance(CProperty_definition_representation.definition);
            epdr.setDefinition(null, epd);
            epdr.setUsed_representation(null, suitableRepresentation);
		}
	}

	public static void unsetLogical_relation(SdaiContext context, EConstraint_occurrence armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator apdIter = apd.createIterator();
		while(apdIter.next()){
			EProperty_definition epd = apd.getCurrentMember(apdIter);
			if(!(epd.testName(null))||(!epd.getName(null).equals("logical relation"))){
				continue;
			}
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apdr);
			SdaiIterator apdrIter = apdr.createIterator();
			while(apdrIter.next()){
				EProperty_definition_relationship epdr = apdr.getCurrentMember(apdrIter);
				epdr.deleteApplicationInstance();
			}	
			epd.deleteApplicationInstance();
		}
	}
	
	
	
}
