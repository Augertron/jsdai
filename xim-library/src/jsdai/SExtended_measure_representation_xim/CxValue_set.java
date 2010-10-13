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

package jsdai.SExtended_measure_representation_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SQualified_measure_schema.AMeasure_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CCompound_representation_item;
import jsdai.SRepresentation_schema.ECompound_representation_item;
import jsdai.SRepresentation_schema.EList_representation_item;
import jsdai.SRepresentation_schema.ESet_representation_item;

public class CxValue_set extends CValue_set implements EMappedXIMEntity{

	// Methods from Compound_representation_item
	// -2- methods for SELECT attribute: item_element
/*	public static int usedinItem_element(ECompound_representation_item type, ERepresentation_item instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public int testItem_element(ECompound_representation_item type) throws SdaiException {
		return test_select(a1, a1$$);
	}

	public ARepresentation_item getItem_element(ECompound_representation_item type, EList_representation_item node1) throws SdaiException { // case 2
		return (ARepresentation_item)get_aggregate_select(a1, a1$$, 2);
	}
	public ARepresentation_item getItem_element(ECompound_representation_item type, ESet_representation_item node1) throws SdaiException { // case 3
		return (ARepresentation_item)get_aggregate_select(a1, a1$$, 3);
	}
*/
	public ARepresentation_item createItem_element(ECompound_representation_item type, EList_representation_item node1) throws SdaiException { // case 2
		a1 = create_aggregate_class(a1, a1$, ARepresentation_item.class, a1$$ = 2);
		return (ARepresentation_item)a1;
	}
	public ARepresentation_item createItem_element(ECompound_representation_item type, ESet_representation_item node1) throws SdaiException { // case 3
		a1 = create_aggregate_class(a1, a1$, ARepresentation_item.class, a1$$ = 3);
		return (ARepresentation_item)a1;
	}

	public void unsetItem_element(ECompound_representation_item type) throws SdaiException {
		a1$$ = 0;
		a1 = unset_select(a1);
	}

	public static jsdai.dictionary.EAttribute attributeItem_element(ECompound_representation_item type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF Methods from ECompound_representation_item	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CCompound_representation_item.definition);

			setMappingConstraints(context, this);

			// values
			setValues(context, this);

			// Clean ARM specific attributes
			unsetValues(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//environment_characterization
			unsetValues(context, this);

	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EValue_set armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EValue_set armEntity) throws SdaiException {
	}


	/**
	 * Sets/creates data for environment_characterization attribute.
	 *	attribute_mapping environment_characterization(environment_characterization, $PATH, property_definition_representation);
	 		data_environment.elements -> property_definition_representation
	 		property_definition_representation.definition -> represented_definition 
			represented_definition = general_property 
		end_attribute_mapping;
		
		attribute_mapping environment_characterization(environment_characterization, $PATH, Property_representation);
	 		data_environment.elements -> property_definition_representation
	 		{property_definition_representation.definition -> represented_definition 
			represented_definition = property_definition} 
		end_attribute_mapping;
 	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setValues(SdaiContext context,
			EValue_set armEntity) throws SdaiException {
		unsetValues(context, armEntity);
		if(armEntity.testValues(null)){
			AMeasure_representation_item items = armEntity.getValues(null);
			SdaiIterator iter = items.createIterator();
			ARepresentation_item ari = armEntity.createItem_element(null, (ESet_representation_item)null);
			while(iter.next()){
				EEntity item = items.getCurrentMember(iter);
				ari.addUnordered(item);
			}
		}
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetValues(SdaiContext context,
			EValue_set armEntity) throws SdaiException {
		armEntity.unsetItem_element(null);		
	}

}