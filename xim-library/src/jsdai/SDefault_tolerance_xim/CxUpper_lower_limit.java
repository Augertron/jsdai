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

import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CCompound_representation_item;
import jsdai.SRepresentation_schema.ECompound_representation_item;
import jsdai.SRepresentation_schema.EList_representation_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ESet_representation_item;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxUpper_lower_limit extends CUpper_lower_limit implements EMappedXIMEntity
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
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public int testItem_element(ECompound_representation_item type) throws SdaiException {
		return test_select(a1, a1$$);
	}

	public ARepresentation_item getItem_element(ECompound_representation_item type, EList_representation_item node1) throws SdaiException { // case 2
		return (ARepresentation_item)get_aggregate_select(a1, a1$$, 2);
	}*/
	public ARepresentation_item getItem_element(ECompound_representation_item type, ESet_representation_item node1) throws SdaiException { // case 3
		return (ARepresentation_item)get_aggregate_select(a1, a1$$, 3);
	}

	public ARepresentation_item createItem_element(ECompound_representation_item type, EList_representation_item node1) throws SdaiException { // case 2
		a1 = create_aggregate_class(a1, a1$, ARepresentation_item.class, a1$$ = 2);
		return (ARepresentation_item)a1;
	}
	public ARepresentation_item createItem_element(ECompound_representation_item type, ESet_representation_item node1) throws SdaiException { // case 3
		a1 = create_aggregate_class(a1, a1$, ARepresentation_item.class, a1$$ = 3);
		return (ARepresentation_item)a1;
	}

	public void unsetItem_element(ECompound_representation_item type) throws SdaiException {
		a1 = unset_select(a1);
		a1$$ = 0;
	}

	public static jsdai.dictionary.EAttribute attributeItem_element(ECompound_representation_item type) throws SdaiException {
		return a1$;
	}

	// ENDOF taken from CRepresentation_item
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		setTemp("AIM", CCompound_representation_item.definition);

		setMappingConstraints(context, this);
		
		// upper_limit : measure_representation_item;
		setUpper_limit(context, this);
		
		// lower_limit : measure_representation_item;
		setLower_limit(context, this);
		
		// upper_limit : measure_representation_item;
		unsetUpper_limit(null);
		
		// lower_limit : measure_representation_item;
		unsetLower_limit(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// upper_limit : measure_representation_item;
		unsetUpper_limit(context, this);
		
		// lower_limit : measure_representation_item;
		unsetLower_limit(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		compound_representation_item 
		compound_representation_item.item_element -> 
		compound_item_definition 
		compound_item_definition = set_representation_item 
		{[set_representation_item[i] -> representation_item 
		{representation_item 
		representation_item.name = 'lower limit'}
		representation_item => 
		measure_representation_item]}
		{[set_representation_item[i] -> representation_item 
		{representation_item 
		representation_item.name = 'upper limit'}
		representation_item => 
		measure_representation_item]}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EUpper_lower_limit armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "");
		armEntity.createItem_element(null, (ESet_representation_item)null);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, EUpper_lower_limit armEntity) throws SdaiException {
		armEntity.unsetItem_element(null);
	}

	/**
	 * Sets/creates data for mapping of cell_value attribute.
	 * 
	 * <p>
	attribute_mapping upper_limit(upper_limit, $PATH, measure_representation_item);
		compound_representation_item 
		compound_representation_item.item_element -> 
		compound_item_definition 
		compound_item_definition = set_representation_item 
		set_representation_item[i] -> representation_item 
		{representation_item 
		representation_item.name = 'upper limit'}
		representation_item => 
		measure_representation_item
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setUpper_limit(SdaiContext context, EUpper_lower_limit armEntity) throws SdaiException {
		unsetUpper_limit(context, armEntity);
		if(armEntity.testUpper_limit(null)){
			EMeasure_representation_item value = armEntity.getUpper_limit(null);
			ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
			items.addUnordered(value);
			value.setName(null, "upper limit");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetUpper_limit(SdaiContext context, EUpper_lower_limit armEntity) throws SdaiException {
		ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
		for(int i=1;i<=items.getMemberCount();i++){
			ERepresentation_item item = items.getByIndex(i);
			if((item.testName(null))&&(item.getName(null).equals("upper limit"))){
				item.deleteApplicationInstance();
			}
		}
	}

	/**
	 * Sets/creates data for mapping of cell_value attribute.
	 * 
	 * <p>
	attribute_mapping lower_limit(lower_limit, $PATH, measure_representation_item);
		compound_representation_item 
		compound_representation_item.item_element -> 
		compound_item_definition 
		compound_item_definition = set_representation_item 
		set_representation_item[i] -> representation_item 
		{representation_item 
		representation_item.name = 'lower limit'}
		representation_item => 
		measure_representation_item
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setLower_limit(SdaiContext context, EUpper_lower_limit armEntity) throws SdaiException {
		unsetLower_limit(context, armEntity);
		if(armEntity.testLower_limit(null)){
			EMeasure_representation_item value = armEntity.getLower_limit(null);
			ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
			items.addUnordered(value);
			value.setName(null, "lower limit");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLower_limit(SdaiContext context, EUpper_lower_limit armEntity) throws SdaiException {
		ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
		for(int i=1;i<=items.getMemberCount();i++){
			ERepresentation_item item = items.getByIndex(i);
			if((item.testName(null))&&(item.getName(null).equals("lower limit"))){
				item.deleteApplicationInstance();
			}
		}
	}
	
}