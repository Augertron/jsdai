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

package jsdai.SLocation_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.SLocation_mim.ALocation_representation_item;
import jsdai.SLocation_mim.CApplied_location_representation_assignment;
import jsdai.SLocation_mim.EApplied_location_representation_assignment;
import jsdai.lang.*;
import jsdai.libutil.*;

public class CxProduct_based_location_identification extends CProduct_based_location_identification implements EMappedXIMEntity
{

	// Taken from CApplied_location_representation_assignment
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EApplied_location_representation_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a5$, domain, result);
	}
	public boolean testItems(EApplied_location_representation_assignment type) throws SdaiException {
		return test_aggregate(a5);
	}
	public ALocation_representation_item getItems(EApplied_location_representation_assignment type) throws SdaiException {
		if (a5 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a5;
	}*/
	public ALocation_representation_item createItems(EApplied_location_representation_assignment type) throws SdaiException {
		a5 = (ALocation_representation_item)create_aggregate_class(a5, a5$, ALocation_representation_item.class, 0);
		return a5;
	}
	public void unsetItems(EApplied_location_representation_assignment type) throws SdaiException {
		unset_aggregate(a5);
		a5 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EApplied_location_representation_assignment type) throws SdaiException {
		return a5$;
	}
	
	// ENDOF taken from CApplied_location_representation_assignment
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_location_representation_assignment.definition);

		// referenced_product : product_based_location_representation;
		setReferenced_product(context, this);

		unsetReferenced_product(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// referenced_product : product_based_location_representation;
		unsetReferenced_product(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		applied_location_representation_assignment
		{applied_location_representation_assignment
		applied_location_representation_assignment.items[i] ->
		location_representation_item =
		(product)
		(product_definition_formation)}
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EProduct_based_location_identification armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EProduct_based_location_identification armEntity) throws SdaiException
	{
	}
	
	/**
	* Sets/creates data for alternative_location_representations attribute.
	*
	* <p>
	attribute_mapping referenced_product(referenced_product, $PATH, Product);
		applied_location_representation_assignment
		applied_location_representation_assignment.items[i] ->
		location_representation_item =
		product
	end_attribute_mapping;
	attribute_mapping referenced_product(referenced_product, $PATH, product_definition_formation);
		applied_location_representation_assignment
		applied_location_representation_assignment.items[i] ->
		location_representation_item =
		product_definition_formation
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setReferenced_product(SdaiContext context, EProduct_based_location_identification armEntity) throws SdaiException
	{
		unsetReferenced_product(context, armEntity);
		if(armEntity.testReferenced_product(null)){
			EEntity ee = armEntity.getReferenced_product(null);
			armEntity.createItems(null).addUnordered(ee);
		}
	}

	public static void unsetReferenced_product(SdaiContext context, EProduct_based_location_identification armEntity) throws SdaiException
	{
		armEntity.unsetItems(null);
	}
	
}
