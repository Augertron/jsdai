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

package jsdai.SRequirement_view_definition_relationship_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_definition_schema.*;

public class CxRequirement_collection_relationship extends CRequirement_collection_relationship implements EMappedXIMEntity {

	// From CProduct_definition_relationship

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a2);
	}
	public String getDescription(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setDescription(EProduct_definition_relationship type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetDescription(EProduct_definition_relationship type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition_relationship type) throws SdaiException {
		return a2$;
	}
	// ENDOF From CProduct_definition_relationship	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_definition_relationship.definition);

		setMappingConstraints(context, this);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
	}


	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 *mapping_constraints;
			product_definition_relationship
			{[product_definition_relationship
			product_definition_relationship.description='requirement collection relationship']
			[product_definition_relationship
			product_definition_relationship.relating_product_definition ->
			product_definition
			product_definition.formation ->
			product_definition_formation
			product_definition_formation.of_product ->
			product <- 
			product_related_product_category.products[i]
			product_related_product_category <=
			product_category 
			product_category.name='requirement']}
	end_mapping_constraints;
	 *
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			CRequirement_collection_relationship armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "requirement collection relationship");
		// AIM gap
		if(!armEntity.testName(null)){
			armEntity.setName(null, "");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CRequirement_collection_relationship armEntity) throws SdaiException {
		armEntity.unsetDescription(null);
	}

}