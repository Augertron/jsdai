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
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_relationship;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;

public class CxGeneral_tolerances extends CGeneral_tolerances implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	
	/*----------- Taken 1:1 from Representation -----------*/
	/*
		public boolean testName(ERepresentation type) throws SdaiException {
			return test_string(a0);
		}
		public String getName(ERepresentation type) throws SdaiException {
			return get_string(a0);
		}*/
		public void setName(ERepresentation type, String value) throws SdaiException {
			a0 = set_string(value);
		}
		public void unsetName(ERepresentation type) throws SdaiException {
			a0 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
			return a0$;
		}

		// methods for attribute: items, base type: SET OF ENTITY
	/*	public static int usedinItems(ERepresentation type, ERepresentation_item instance, ASdaiModel domain, AEntity result) throws SdaiException {
			return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
		}
		private boolean testItems2(ERepresentation type) throws SdaiException {
			return test_aggregate(a1);
		}
		private ARepresentation_item getItems2(ERepresentation type) throws SdaiException {
			return (ARepresentation_item)get_aggregate(a1);
		}*/
		public ARepresentation_item createItems(ERepresentation type) throws SdaiException {
			a1 = (ARepresentation_item)create_aggregate_class(a1, a1$,  ARepresentation_item.class, 0);
			return a1;
		}
		public void unsetItems(ERepresentation type) throws SdaiException {
			unset_aggregate(a1);
			a1 = null;
		}
		public static jsdai.dictionary.EAttribute attributeItems(ERepresentation type) throws SdaiException {
			return a1$;
		}

	// attribute (current explicit or supertype explicit) : context_of_items, base type: entity representation_context
	/*	public static int usedinContext_of_items(ERepresentation type, ERepresentation_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
			return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
		}
		public boolean testContext_of_items(ERepresentation type) throws SdaiException {
			return test_instance(a2);
		}
		public ERepresentation_context getContext_of_items(ERepresentation type) throws SdaiException {
			a2 = get_instance(a2);
			return (ERepresentation_context)a2;
		}*/
		public void setContext_of_items(ERepresentation type, ERepresentation_context value) throws SdaiException {
			a2 = set_instanceX(a2, value);
		}
		public void unsetContext_of_items(ERepresentation type) throws SdaiException {
			a2 = unset_instance(a2);
		}
		public static jsdai.dictionary.EAttribute attributeContext_of_items(ERepresentation type) throws SdaiException {
			return a2$;
		}
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		setTemp("AIM", CRepresentation.definition);
		setMappingConstraints(context, this);

		// table_definition : general_tolerance_table_select;
		setTable_definition(context, this);
		
		// tolerance_class : STRING;
		setTolerance_class(context, this);
		
		// table_definition : general_tolerance_table_select;
		unsetTable_definition(null);
		
		// tolerance_class : STRING;
		unsetTolerance_class(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// table_definition : general_tolerance_table_select;
		unsetTable_definition(context, this);
		
		// tolerance_class : STRING;
		unsetTolerance_class(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		representation 
		{representation.name = 'default tolerances'}
		representation.context_of_items -> 
		representation_context 
		{representation_context.context_type = 'default setting'}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EGeneral_tolerances armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "default tolerances");
		ERepresentation_context erc = 
			CxAP210ARMUtilities.createRepresentation_context(context, "", "default setting", true);
		armEntity.setContext_of_items(null, erc);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, EGeneral_tolerances armEntity) throws SdaiException {
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
			armEntity.createItems(null).addUnordered(edri);
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