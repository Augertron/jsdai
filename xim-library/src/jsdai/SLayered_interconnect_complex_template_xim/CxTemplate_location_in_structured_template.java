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

package jsdai.SLayered_interconnect_complex_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_structure_schema.*;

public class CxTemplate_location_in_structured_template extends CTemplate_location_in_structured_template implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Product_definition_relationship
	/// methods for attribute: id, base type: STRING
/*	
	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a0$;
	}

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

	// ENDOF Taken from Product_definition_relationship

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAssembly_component_usage.definition);

		setMappingConstraints(context, this);
		
		// Placement_status
		setPlacement_status(context, this);
		
		unsetPlacement_status(null);
		
//		setReference_feature(context, this);
		
//		unsetReference_feature(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// Placement_status
		unsetPlacement_status(context, this);
		
//		unsetReference_feature(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{assembly_component_usage &lt;=
	*  product_definition_usage &lt;=
	*  product_definition_relationship
	*  product_definition_relationship.name = 'part template location in padstack definition'}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		armEntity.setName(null, "tlist");
		// AIM GAPs, UR of Product_definition_usage
//		if(!armEntity.testId(null)){
//			if(armEntity.testReference_designation(null)){
//				armEntity.setId(null, armEntity.getReference_designation(null));
//			}else{
				armEntity.setId(null, armEntity.getReference_designation(null));
//			}
	}

	public static void unsetMappingConstraints(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
		attribute_mapping placement_status(placement_status, product_definition_relationship.description);
			assembly_component_usage <=
			product_definition_usage <=
			product_definition_relationship
			(product_definition_relationship.description = 'is fixed')
			(product_definition_relationship.description = 'must be moved in design')
			(product_definition_relationship.description = 'may be moved in design')
			(product_definition_relationship.description = 'is unknown')
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPlacement_status(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		unsetPlacement_status(context, armEntity);
		if(armEntity.testPlacement_status(null)){
			int status = armEntity.getPlacement_status(null);
			armEntity.setDescription(null, ETemplate_location_placement_status.toString(status).replace('_', ' ').toLowerCase());
		}
	}

	public static void unsetPlacement_status(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		// Can't unset as it is mandaotry in MIM
		armEntity.unsetDescription(null);
	}
	
}
