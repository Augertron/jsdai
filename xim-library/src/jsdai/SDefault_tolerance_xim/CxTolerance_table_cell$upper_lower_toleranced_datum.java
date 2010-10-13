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

import jsdai.SDefault_tolerance_mim.CDefault_tolerance_table_cell;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ECompound_representation_item;
import jsdai.SRepresentation_schema.EList_representation_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ESet_representation_item;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxTolerance_table_cell$upper_lower_toleranced_datum extends CTolerance_table_cell$upper_lower_toleranced_datum implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;
	
	// Taken from CRepresentation_item
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_item type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(ERepresentation_item type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(ERepresentation_item type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(ERepresentation_item type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_item type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF taken from CRepresentation_item
	
	// Taken from CRepresentation_item
	//going through all the attributes: #5629499534229272=EXPLICIT_ATTRIBUTE('name',#5629499534229270,0,#5629499534229350,$,.F.);
	//<01> generating methods for consolidated attribute:  name
	//<01-1> supertype, java inheritance
	//<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
	//going through all the attributes: #5629499534229229=EXPLICIT_ATTRIBUTE('item_element',#5629499534229227,0,#5629499534229211,$,.F.);
	//<01> generating methods for consolidated attribute:  item_element
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// -2- methods for SELECT attribute: item_element
/*	public static int usedinItem_element(ECompound_representation_item type, ERepresentation_item instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public int testItem_element(ECompound_representation_item type) throws SdaiException {
		return test_select(a0, a0$$);
	}

	public ARepresentation_item getItem_element(ECompound_representation_item type, EList_representation_item node1) throws SdaiException { // case 2
		return (ARepresentation_item)get_aggregate_select(a0, a0$$, 2);
	}*/
	public ARepresentation_item getItem_element(ECompound_representation_item type, ESet_representation_item node1) throws SdaiException { // case 3
		return (ARepresentation_item)get_aggregate_select(a0, a0$$, 3);
	}

	public ARepresentation_item createItem_element(ECompound_representation_item type, EList_representation_item node1) throws SdaiException { // case 2
		a0 = create_aggregate_class(a0, a0$, ARepresentation_item.class, a0$$ = 2);
		return (ARepresentation_item)a0;
	}
	public ARepresentation_item createItem_element(ECompound_representation_item type, ESet_representation_item node1) throws SdaiException { // case 3
		a0 = create_aggregate_class(a0, a0$, ARepresentation_item.class, a0$$ = 3);
		return (ARepresentation_item)a0;
	}

	public void unsetItem_element(ECompound_representation_item type) throws SdaiException {
		a0 = unset_select(a0);
		a0$$ = 0;
	}

	public static jsdai.dictionary.EAttribute attributeItem_element(ECompound_representation_item type) throws SdaiException {
		return a0$;
	}

	// ENDOF taken from CRepresentation_item
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		setTemp("AIM", CDefault_tolerance_table_cell.definition);

		setMappingConstraints(context, this);
		
		// cell_value : cell_entry_select;
		setCell_value(context, this);
		
		// description : STRING;
		setDescription(context, this);
		
		// limit : limit_select;
		setLimit(context, this);
		
		// upper_limit : measure_representation_item;
		setUpper_tolerance_value(context, this);
		
		// lower_limit : measure_representation_item;
		setLower_tolerance_value(context, this);
		
		// upper_limit : measure_representation_item;
		unsetUpper_tolerance_value(null);
		
		// lower_limit : measure_representation_item;
		unsetLower_tolerance_value(null);
		
		// cell_value : cell_entry_select;
		unsetCell_value(null);
		
		// description : STRING;
		unsetDescription(null);
		
		// limit : limit_select;
		unsetLimit(null);		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// cell_value : cell_entry_select;
		unsetCell_value(context, this);
		
		// description : STRING;
		unsetDescription(context, this);
		
		// limit : limit_select;
		unsetLimit(context, this);		
		
		// upper_limit : measure_representation_item;
		unsetUpper_tolerance_value(context, this);
		
		// lower_limit : measure_representation_item;
		unsetLower_tolerance_value(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		default_tolerance_table_cell <= 
		compound_representation_item
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, CTolerance_table_cell$upper_lower_toleranced_datum armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		
		CxTolerance_table_cell.setMappingConstraints(context, armEntity);
		CxUpper_lower_toleranced_datum.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, CTolerance_table_cell$upper_lower_toleranced_datum armEntity) throws SdaiException {
		CxTolerance_table_cell.unsetMappingConstraints(context, armEntity);
		CxUpper_lower_toleranced_datum.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping of cell_value attribute.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setCell_value(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		CxTolerance_table_cell.setCell_value(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetCell_value(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		CxTolerance_table_cell.unsetCell_value(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping of limit attribute.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setLimit(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		CxTolerance_table_cell.setLimit(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLimit(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		CxTolerance_table_cell.unsetLimit(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping of description attribute.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setDescription(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		CxTolerance_table_cell.setDescription(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetDescription(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		CxTolerance_table_cell.unsetDescription(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping of upper_limit attribute.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setUpper_tolerance_value(SdaiContext context, EUpper_lower_toleranced_datum armEntity) throws SdaiException {
		CxUpper_lower_toleranced_datum.setUpper_tolerance_value(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetUpper_tolerance_value(SdaiContext context, EUpper_lower_toleranced_datum armEntity) throws SdaiException {
		CxUpper_lower_toleranced_datum.unsetUpper_tolerance_value(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping of lower_limit attribute.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setLower_tolerance_value(SdaiContext context, EUpper_lower_toleranced_datum armEntity) throws SdaiException {
		CxUpper_lower_toleranced_datum.setLower_tolerance_value(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLower_tolerance_value(SdaiContext context, EUpper_lower_toleranced_datum armEntity) throws SdaiException {
		CxUpper_lower_toleranced_datum.unsetLower_tolerance_value(context, armEntity);
	}
	
}