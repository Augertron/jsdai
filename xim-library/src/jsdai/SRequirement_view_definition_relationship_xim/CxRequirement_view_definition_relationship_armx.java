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
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SRequirement_view_definition_relationship_mim.CRequirement_view_definition_relationship;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;

public class CxRequirement_view_definition_relationship_armx extends CRequirement_view_definition_relationship_armx implements EMappedXIMEntity
{
	
	// Taken from CProduct_definition_relationship
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
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CRequirement_view_definition_relationship.definition);

		setMappingConstraints(context, this);

		setRelation_type(context, this);
		
		//********** "managed_design_object" attributes

		unsetRelation_type(null);
		
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetRelation_type(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	 product_definition_relationship
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ERequirement_view_definition_relationship_armx armEntity) throws SdaiException
	{
	}

	public static void unsetMappingConstraints(SdaiContext context, ERequirement_view_definition_relationship_armx armEntity) throws SdaiException
	{
	}

	
	/**
	* Sets/creates data for relation_type attribute.
	*
	* <p>
	*  attribute_mapping relation_type(relation_type, product_definition_relationship.name);
		requirement_view_definition_relationship <=
		product_definition_relationship
		{product_definition_relationship.name
		(product_definition_relationship.name = 'derived from operation')
		(product_definition_relationship.name = 'precedence')
		(product_definition_relationship.name = 'supplemental')}
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRelation_type(SdaiContext context, ERequirement_view_definition_relationship_armx armEntity) throws SdaiException
	{
		unsetRelation_type(context, armEntity);
		if(armEntity.testRelation_type(null)){
			int type = armEntity.getRelation_type(null);
			if(ERequirement_view_relationship_type.DERIVED_FROM != type){
				armEntity.setName(null, ERequirement_view_relationship_type.toString(type).toLowerCase().replace('_', ' '));
			}else{
				armEntity.setName(null, "derived from operation");
			}
			
		}
	}

	public static void unsetRelation_type(SdaiContext context, ERequirement_view_definition_relationship_armx armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
	
	//********** "managed_design_object" attributes

}
