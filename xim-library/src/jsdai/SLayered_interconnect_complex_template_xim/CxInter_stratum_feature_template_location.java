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
import jsdai.SProduct_structure_schema.CAssembly_component_usage;

public class CxInter_stratum_feature_template_location extends CInter_stratum_feature_template_location implements EMappedXIMEntity
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
		
		setPlacement_status(context, this);
		
		unsetPlacement_status(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetPlacement_status(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{[stratum_specific_part_template_location_in_padstack_definition &lt;=
	*  assembly_component_usage &lt;=
	*  product_definition_usage &lt;=
	*  product_definition_relationship
	*  product_definition_relationship.name = 'part template location in padstack definition']
	*  [stratum_specific_part_template_location_in_padstack_definition &lt;=
	*  property_definition]}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EInter_stratum_feature_template_location armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxTemplate_location_in_structured_template.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EInter_stratum_feature_template_location armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.unsetMappingConstraints(context, armEntity);
	}

	public static void setPlacement_status(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.setPlacement_status(context, armEntity);
	}

	public static void unsetPlacement_status(SdaiContext context, ETemplate_location_in_structured_template armEntity) throws SdaiException
	{
		CxTemplate_location_in_structured_template.unsetPlacement_status(context, armEntity);
	}
	
}
