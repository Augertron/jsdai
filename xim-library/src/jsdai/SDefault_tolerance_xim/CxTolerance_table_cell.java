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
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ECompound_representation_item;
import jsdai.SRepresentation_schema.EList_representation_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ESet_representation_item;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxTolerance_table_cell extends CTolerance_table_cell implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	
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
		setTemp("AIM", CDefault_tolerance_table_cell.definition);

		setMappingConstraints(context, this);
		
		// cell_value : cell_entry_select;
		setCell_value(context, this);
		
		// description : STRING;
		setDescription(context, this);
		
		// limit : limit_select;
		setLimit(context, this);
		
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
	public static void setMappingConstraints(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		
		armEntity.createItem_element(null, (ESet_representation_item)null);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		armEntity.unsetItem_element(null);
	}

	/**
	 * Sets/creates data for mapping of cell_value attribute.
	 * 
	 * <p>
	attribute_mapping cell_value(cell_value, $PATH, Plus_minus_toleranced_datum);
		default_tolerance_table_cell <= 
		compound_representation_item 
		compound_representation_item.item_element -> 
		compound_item_definition 
		compound_item_definition = set_representation_item 
		set_representation_item[i] -> representation_item 
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
	public static void setCell_value(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		unsetCell_value(context, armEntity);
		if(armEntity.testCell_value(null)){
			EEntity value = armEntity.getCell_value(null);
			if(value instanceof EMeasure_representation_item){
				ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
				items.addUnordered(value);
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetCell_value(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
		for(int i=1;i<=items.getMemberCount();){
			ERepresentation_item item = items.getByIndex(i);
			if(item instanceof EPlus_minus_toleranced_datum){
				items.removeByIndex(i);
			}else{
				i++;
			}
		}
	}

	/**
	 * Sets/creates data for mapping of cell_value attribute.
	 * 
	 * <p>
	attribute_mapping limit(limit, $PATH, Significant_number_of_digits);
		default_tolerance_table_cell <= 
		compound_representation_item 
		compound_representation_item.item_element ->
		compound_item_definition 
		compound_item_definition = set_representation_item 
		set_representation_item[i] -> representation_item 
		representation_item =>
		measure_representation_item
	end_attribute_mapping;
	attribute_mapping limit(limit, $PATH, Upper_lower_limit);
		default_tolerance_table_cell <= 
		compound_representation_item 
		compound_representation_item.item_element -> 
		compound_item_definition 
		compound_item_definition = set_representation_item 
		set_representation_item[i] -> representation_item representation_item => 
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
	public static void setLimit(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		unsetLimit(context, armEntity);
		if(armEntity.testLimit(null)){
			EEntity value = armEntity.getLimit(null);
			if(value instanceof ERepresentation_item){
				ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
				items.addUnordered(value);
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLimit(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
		for(int i=1;i<=items.getMemberCount();){
			ERepresentation_item item = items.getByIndex(i);
			if((item instanceof ESignificant_number_of_digits)||
				(item instanceof EUpper_lower_limit)){
				items.removeByIndex(i);
			}else{
				i++;
			}
		}
	}

	/**
	 * Sets/creates data for mapping of cell_value attribute.
	 * 
	 * <p>
	attribute_mapping description(description, $PATH);
		default_tolerance_table_cell <= 
		compound_representation_item 
		compound_representation_item.item_element -> 
		compound_item_definition 
		compound_item_definition = set_representation_item 
		set_representation_item[i] -> representation_item 
		representation_item =>
		{representation_item.name = 'cell description'} 
		descriptive_representation_item
		descriptive_representation_item.description
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setDescription(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		unsetDescription(context, armEntity);
		if(armEntity.testDescription(null)){
			String value = armEntity.getDescription(null);
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, "cell description");
			edri.setDescription(null, value);
			ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
			items.addUnordered(edri);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetDescription(SdaiContext context, ETolerance_table_cell armEntity) throws SdaiException {
		ARepresentation_item items = armEntity.getItem_element(null, (ESet_representation_item)null);
		for(int i=1;i<=items.getMemberCount();i++){
			ERepresentation_item item = items.getByIndex(i);
			if(item instanceof EDescriptive_representation_item){
				item.deleteApplicationInstance();
			}
		}
	}
	
}