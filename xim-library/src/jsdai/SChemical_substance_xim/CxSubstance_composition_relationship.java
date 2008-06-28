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

import jsdai.SMaterial_property_definition_schema.CProduct_material_composition_relationship;
import jsdai.SMaterial_property_definition_schema.EProduct_material_composition_relationship;
import jsdai.SMeasure_schema.EMeasure_with_unit;
import jsdai.SProduct_definition_schema.*;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;

public class CxSubstance_composition_relationship extends CSubstance_composition_relationship implements EMappedXIMEntity
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

	// Taken from CProduct_material_composition_relationship	
	// methods for attribute: constituent_amount, base type: SET OF ENTITY
/*	public static int usedinConstituent_amount(EProduct_material_composition_relationship type, jsdai.SMeasure_schema.EMeasure_with_unit instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a6$, domain, result);
	}
	public boolean testConstituent_amount(EProduct_material_composition_relationship type) throws SdaiException {
		return test_aggregate(a6);
	}
	public jsdai.SMeasure_schema.AMeasure_with_unit getConstituent_amount(EProduct_material_composition_relationship type) throws SdaiException {
		return (jsdai.SMeasure_schema.AMeasure_with_unit)get_aggregate(a6);
	}*/
	public jsdai.SMeasure_schema.AMeasure_with_unit createConstituent_amount(EProduct_material_composition_relationship type) throws SdaiException {
		a6 = (jsdai.SMeasure_schema.AMeasure_with_unit)create_aggregate_class(a6, a6$,  jsdai.SMeasure_schema.AMeasure_with_unit.class, 0);
		return a6;
	}
	public void unsetConstituent_amount(EProduct_material_composition_relationship type) throws SdaiException {
		unset_aggregate(a6);
		a6 = null;
	}
	public static jsdai.dictionary.EAttribute attributeConstituent_amount(EProduct_material_composition_relationship type) throws SdaiException {
		return a6$;
	}

	//<01> generating methods for consolidated attribute:  composition_basis
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: composition_basis, base type: STRING
/*	public boolean testComposition_basis(EProduct_material_composition_relationship type) throws SdaiException {
		return test_string(a7);
	}
	public String getComposition_basis(EProduct_material_composition_relationship type) throws SdaiException {
		return get_string(a7);
	}*/
	public void setComposition_basis(EProduct_material_composition_relationship type, String value) throws SdaiException {
		a7 = set_string(value);
	}
	public void unsetComposition_basis(EProduct_material_composition_relationship type) throws SdaiException {
		a7 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeComposition_basis(EProduct_material_composition_relationship type) throws SdaiException {
		return a7$;
	}

	// ENDOF Taken from CProduct_material_composition_relationship
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CProduct_material_composition_relationship.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		// composition_basis_x : substance_composition_basis;
		setComposition_basis_x(context, this);
		
        // amount : measure_with_unit;
		setAmount(context, this);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// composition_basis_x : substance_composition_basis;
		unsetComposition_basis_x(context, this);
		
        // amount : measure_with_unit;
		unsetAmount(context, this);
		
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
	public static void setMappingConstraints(SdaiContext context, ESubstance_composition_relationship armEntity) throws SdaiException
	{
		armEntity.setName(null, "assembly material composition");
		// AIM GAP
		// armEntity.setId(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, ESubstance_composition_relationship armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
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
