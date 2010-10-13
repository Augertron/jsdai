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

package jsdai.SDefault_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.CMeasure_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ERepresentation_relationship;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxPlus_minus_toleranced_datum extends CPlus_minus_toleranced_datum implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	
	// Taken from CRepresentation_item
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_item type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation_item type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation_item type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation_item type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_item type) throws SdaiException {
		return a0$;
	}
	
	// ENDOF taken from CRepresentation_item
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		setTemp("AIM", CMeasure_representation_item.definition);

		setMappingConstraints(context, this);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		{measure_representation_item <= 
		representation_item 
		representation_item.name = 'plus minus tolerance value'}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EPlus_minus_toleranced_datum armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "plus minus tolerance value");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, EPlus_minus_toleranced_datum armEntity) throws SdaiException {
		armEntity.unsetName(null);
	}
	
	/**
	 * Sets/creates data for table_definition attribute.
	 * 
	 * <p>
	attribute_mapping table_definition(table_definition, $PATH, general_tolerance_table_select);
		representation <- 
		representation_relationship.rep_2 
		representation_relationship
		{representation_relationship.name = 'general tolerance definition'} 
		representation_relationship.rep_1 -> 
		representation
	end_attribute_mapping;
	attribute_mapping table_definition(table_definition, $PATH, General_tolerance_table);
		representation representation <- 
		representation_relationship.rep_2 
		representation_relationship
		{representation_relationship.name = 'general tolerance definition'} 
		representation_relationship.rep_1 -> 
		representation => 
		default_tolerance_table
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setTable_definition(SdaiContext context, EGeneral_tolerances armEntity) throws SdaiException {
		unsetTable_definition(context, armEntity);
		if(armEntity.testTable_definition(null)){
			ERepresentation er = (ERepresentation)armEntity.getTable_definition(null);
			ERepresentation_relationship err = (ERepresentation_relationship)
				context.working_model.createEntityInstance(CRepresentation_relationship.definition);
			err.setRep_2(null, armEntity);
			err.setName(null, "general tolerance definition");
			err.setRep_1(null, er);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetTable_definition(SdaiContext context, EGeneral_tolerances armEntity) throws SdaiException {
		ARepresentation_relationship arr = new ARepresentation_relationship();
		CRepresentation_relationship.usedinRep_2(null, armEntity, context.domain, arr);
		for(int i=1,count=arr.getMemberCount(); i<=count; i++){
			ERepresentation_relationship err = arr.getByIndex(i);
			if((err.testName(null))&&(err.getName(null).equals("general tolerance definition"))){
				err.deleteApplicationInstance();
			}
		}
	}

	/**
	 * Sets/creates data for tolerance_class attribute.
	 * 
	 * <p>
	attribute_mapping tolerance_class(tolerance_class, descriptive_representation_item.description);
		representation 
		representation.items[i] -> 
		representation_item =>
		{representation_item.name = 'tolerance class'} 
		descriptive_representation_item 
		descriptive_representation_item.description
		end_attribute_mapping;
	end_entity_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setTolerance_class(SdaiContext context, EGeneral_tolerances armEntity) throws SdaiException {
		unsetTolerance_class(context, armEntity);
		if(armEntity.testTolerance_class(null)){
			String value = armEntity.getTolerance_class(null);
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, "tolerance class");
			edri.setDescription(null, value);
		}
	}

	/**
	 * Unsets/deletes mapping data for attribute tolerance_class.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetTolerance_class(SdaiContext context, EGeneral_tolerances armEntity) throws SdaiException {
		armEntity.unsetItems(null);
	}
	
}