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

package jsdai.SMixed_complex_types;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SItem_definition_structure_xim.CxAssembled_part_association;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_structure_schema.CNext_assembly_usage_occurrence;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;

public class CxAssembled_part_association$next_assembly_usage_occurrence extends CAssembled_part_association$next_assembly_usage_occurrence implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a5);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF taken from PDR

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CNext_assembly_usage_occurrence.definition);

		setMappingConstraints(context, this);

		// CxNext_assembly_usage_occurrence_relationship_armx.processAssemblyTrick(context, this);
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
	* 	 product_definition_relationship
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CAssembled_part_association$next_assembly_usage_occurrence armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxAssembled_part_association.setMappingConstraints(context, armEntity);
		// CxNext_assembly_usage_occurrence_relationship_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CAssembled_part_association$next_assembly_usage_occurrence armEntity) throws SdaiException
	{
		CxAssembled_part_association.setMappingConstraints(context, armEntity);
		// CxNext_assembly_usage_occurrence_relationship_armx.unsetMappingConstraints(context, armEntity);
	}
	
	
	//********** "managed_design_object" attributes

}
