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

package jsdai.SChemical_substance_xim;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SMeasure_schema.CDimensional_exponents;
import jsdai.SMeasure_schema.CMeasure_with_unit;
import jsdai.SMeasure_schema.CNamed_unit;
import jsdai.SMeasure_schema.CRatio_unit;
import jsdai.SMeasure_schema.EDimensional_exponents;
import jsdai.SMeasure_schema.EMeasure_with_unit;
import jsdai.SMeasure_schema.ERatio_unit;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_structure_schema.CMake_from_usage_option;
import jsdai.SProduct_structure_schema.EMake_from_usage_option;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxRaw_material_definition_relationship extends CRaw_material_definition_relationship implements EMappedXIMEntity
{
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from CProduct_definition_relationship

	// Taken from CMake_from_usage_option
	/// methods for attribute: ranking, base type: INTEGER
/*	public boolean testRanking(EMake_from_usage_option type) throws SdaiException {
		return test_integer(a5);
	}
	public int getRanking(EMake_from_usage_option type) throws SdaiException {
		return get_integer(a5);
	}*/
	public void setRanking(EMake_from_usage_option type, int value) throws SdaiException {
		a5 = set_integer(value);
	}
	public void unsetRanking(EMake_from_usage_option type) throws SdaiException {
		a5 = unset_integer();
	}
	public static jsdai.dictionary.EAttribute attributeRanking(EMake_from_usage_option type) throws SdaiException {
		return a5$;
	}

	//<01> generating methods for consolidated attribute:  ranking_rationale
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: ranking_rationale, base type: STRING
/*	public boolean testRanking_rationale(EMake_from_usage_option type) throws SdaiException {
		return test_string(a6);
	}
	public String getRanking_rationale(EMake_from_usage_option type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setRanking_rationale(EMake_from_usage_option type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetRanking_rationale(EMake_from_usage_option type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeRanking_rationale(EMake_from_usage_option type) throws SdaiException {
		return a6$;
	}

	//<01> generating methods for consolidated attribute:  quantity
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : quantity, base type: entity measure_with_unit
/*	public static int usedinQuantity(EMake_from_usage_option type, jsdai.SMeasure_schema.EMeasure_with_unit instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a7$, domain, result);
	}
	public boolean testQuantity(EMake_from_usage_option type) throws SdaiException {
		return test_instance(a7);
	}
	public jsdai.SMeasure_schema.EMeasure_with_unit getQuantity(EMake_from_usage_option type) throws SdaiException {
		return (jsdai.SMeasure_schema.EMeasure_with_unit)get_instance(a7);
	}*/
	public void setQuantity(EMake_from_usage_option type, jsdai.SMeasure_schema.EMeasure_with_unit value) throws SdaiException {
		a7 = set_instance(a7, value);
	}
	public void unsetQuantity(EMake_from_usage_option type) throws SdaiException {
		a7 = unset_instance(a7);
	}
	public static jsdai.dictionary.EAttribute attributeQuantity(EMake_from_usage_option type) throws SdaiException {
		return a7$;
	}	
	// ENDOF Taken from CMake_from_usage_option
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CMake_from_usage_option.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
		{product_material_composition_relationship <=
		product_definition_relationship
		product_definition_relationship.name = 'assembly material composition'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ERaw_material_definition_relationship armEntity) throws SdaiException
	{
/*        SELF\make_from_usage_option.ranking_rationale : text :=  ? ;
        SELF\make_from_usage_option.quantity : measure_with_unit :=  ? ;
        SELF\make_from_usage_option.ranking : INTEGER :=  ? ;
        SELF\product_definition_relationship.name : label :=  'raw material definition' ;
  */      
		armEntity.setName(null, "raw material definition");
		// AIM GAP
		armEntity.setRanking(null, 0);
		armEntity.setRanking_rationale(null, "");
		// DE
		LangUtils.Attribute_and_value_structure[] deStructure = {
				new LangUtils.Attribute_and_value_structure(
						CDimensional_exponents.attributeAmount_of_substance_exponent(null),
						new Double(0.0)),
				new LangUtils.Attribute_and_value_structure(
						CDimensional_exponents.attributeElectric_current_exponent(null),
						new Double(0.0)),
				new LangUtils.Attribute_and_value_structure(
						CDimensional_exponents.attributeLength_exponent(null),
						new Double(0.0)),				
				new LangUtils.Attribute_and_value_structure(
						CDimensional_exponents.attributeLuminous_intensity_exponent(null),
						new Double(0.0)),				
				new LangUtils.Attribute_and_value_structure(
						CDimensional_exponents.attributeMass_exponent(null),
						new Double(0.0)),				
				new LangUtils.Attribute_and_value_structure(
						CDimensional_exponents.attributeThermodynamic_temperature_exponent(null),
						new Double(0.0)),				
				new LangUtils.Attribute_and_value_structure(
						CDimensional_exponents.attributeTime_exponent(null),
						new Double(0.0))
				};
		EDimensional_exponents ede = (EDimensional_exponents) LangUtils
				.createInstanceIfNeeded(
						context,
						CDimensional_exponents.definition,
						deStructure);
		// RU
		LangUtils.Attribute_and_value_structure[] ruStructure = {
				new LangUtils.Attribute_and_value_structure(
						CNamed_unit.attributeDimensions(null),
						ede)
				};
		ERatio_unit eru = (ERatio_unit) LangUtils
				.createInstanceIfNeeded(
						context,
						CRatio_unit.definition,
						ruStructure);
		// MWU
		LangUtils.Attribute_and_value_structure[] mwuStructure = {
				new LangUtils.Attribute_and_value_structure(
						CMeasure_with_unit.attributeUnit_component(null),
						eru),
				new LangUtils.Attribute_and_value_structure(
						CMeasure_with_unit.attributeValue_component(null),
						new Double(1),
						context.working_model.getUnderlyingSchema().getDefinedType("parameter_value")
						)
						
				};
		EMeasure_with_unit emwu = (EMeasure_with_unit) LangUtils
				.createInstanceIfNeeded(
						context,
						CMeasure_with_unit.definition,
						mwuStructure);
		
		armEntity.setQuantity(null, emwu);
	}

	public static void unsetMappingConstraints(SdaiContext context, ERaw_material_definition_relationship armEntity) throws SdaiException
	{
		armEntity.unsetRanking(null);
		armEntity.unsetRanking_rationale(null);
		armEntity.unsetQuantity(null);
	}

	/**
	* Sets/creates data for Composition_basis_x attribute.
	*
	* <p>
	attribute_mapping composition_basis_x(composition_basis_x, product_definition_relationship.description);
		product_material_composition_relationship
		product_material_composition_relationship.composition_basis
		{(product_material_composition_relationship.composition_basis = 'volume')
		(product_material_composition_relationship.composition_basis = 'mass')
		(product_material_composition_relationship.composition_basis = 'moles')
		(product_material_composition_relationship.composition_basis = 'atoms')}
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setComposition_basis_x(SdaiContext context, ESubstance_composition_relationship armEntity) throws SdaiException
	{
		if(armEntity.testComposition_basis_x(null)){
			int composition_basis_x = armEntity.getComposition_basis_x(null);
			armEntity.setComposition_basis(null, ESubstance_composition_basis.toString(composition_basis_x).toLowerCase());
		}
	}

	public static void unsetComposition_basis_x(SdaiContext context, ESubstance_composition_relationship armEntity) throws SdaiException
	{
		armEntity.unsetComposition_basis(null);
	}

	/**
	* Sets/creates data for Amount attribute.
	*
	* <p>
	attribute_mapping amount(amount, $PATH, measure_with_unit);
		product_material_composition_relationship
		product_material_composition_relationship.constituent_amount ->
		measure_with_unit

	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAmount(SdaiContext context, ESubstance_composition_relationship armEntity) throws SdaiException
	{
		if(armEntity.testAmount(null)){
			EMeasure_with_unit amount = armEntity.getAmount(null);
			armEntity.createConstituent_amount(null).addUnordered(amount);
		}
	}

	public static void unsetAmount(SdaiContext context, ESubstance_composition_relationship armEntity) throws SdaiException
	{
		armEntity.unsetConstituent_amount(null);
	}
	
	
	//********** "managed_design_object" attributes

}
