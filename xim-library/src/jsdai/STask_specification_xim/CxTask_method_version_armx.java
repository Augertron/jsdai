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

package jsdai.STask_specification_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SAction_schema.AAction_method_relationship;
import jsdai.SAction_schema.CAction_method_relationship;
import jsdai.SAction_schema.EAction_method_relationship;
import jsdai.STask_specification_mim.CTask_method_version;
import jsdai.STask_specification_mim.ETask_element;

public class CxTask_method_version_armx extends CTask_method_version_armx implements EMappedXIMEntity{


	// Taken from Organizational_project_assignment
	// ENDOF taken from Organizational_project_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CTask_method_version.definition);

		setMappingConstraints(context, this);

		setOf_task_method(context, this);			
		setContent(context, this);
		
		unsetOf_task_method(null);
		unsetContent(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetOf_task_method(context, this);			
			unsetContent(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
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
			ETask_method_version_armx armEntity) throws SdaiException {
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
			ETask_method_version_armx armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for attribute Of_task_method.
	 * 
	 * <p>
		attribute_mapping of_task_method(of_task_method, $PATH, Task_method);
			task_method_version <=
						action_method <-
			action_method_relationship.relating_method
			action_method_relationship
			{action_method_relationship.description='of task method'}
			action_method_relationship.related_method ->
			action_method
		end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// TM <- AMR -> TO
	public static void setOf_task_method(SdaiContext context,
			ETask_method_version_armx armEntity) throws SdaiException {
		unsetOf_task_method(context, armEntity);
		
		if(armEntity.testOf_task_method(null)){
			ETask_method_armx method = armEntity.getOf_task_method(null);
			EAction_method_relationship eamr = (EAction_method_relationship)
				context.working_model.createEntityInstance(CAction_method_relationship.definition);
			eamr.setRelated_method(null, method);
			eamr.setRelating_method(null, armEntity);
			eamr.setName(null, "");
			eamr.setDescription(null, "of task method");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetOf_task_method(SdaiContext context,
			ETask_method_version_armx armEntity) throws SdaiException {
		AAction_method_relationship aamr = new AAction_method_relationship();
		CAction_method_relationship.usedinRelated_method(null, armEntity, context.domain, aamr);
		for(int i=1;i <= aamr.getMemberCount();i++){
			EAction_method_relationship eamr = aamr.getByIndex(i);
			if(eamr.testDescription(null)){
				if(eamr.getDescription(null).equals("of task method")){
					eamr.deleteApplicationInstance();
				}
			}
		}
	}
	
	/**
	 * Sets/creates data for attribute Of_task_method.
	 * 
	 * <p>
		attribute_mapping content(content, $PATH, Task_element);
			task_method_version <=
			action_method <-
			action_method_relationship.relating_method
			action_method_relationship
			{action_method_relationship.description='method'}
			action_method_relationship.related_method ->
			action_method
		end_attribute_mapping;

	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// TM <- AMR -> TO
	public static void setContent(SdaiContext context,
			ETask_method_version_armx armEntity) throws SdaiException {
		unsetContent(context, armEntity);
		
		if(armEntity.testContent(null)){
			ETask_element element = armEntity.getContent(null);
			EAction_method_relationship eamr = (EAction_method_relationship)
				context.working_model.createEntityInstance(CAction_method_relationship.definition);
			eamr.setRelated_method(null, element);
			eamr.setRelating_method(null, armEntity);
			eamr.setName(null, "");
			eamr.setDescription(null, "method");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetContent(SdaiContext context,
			ETask_method_version_armx armEntity) throws SdaiException {
		AAction_method_relationship aamr = new AAction_method_relationship();
		CAction_method_relationship.usedinRelated_method(null, armEntity, context.domain, aamr);
		for(int i=1;i <= aamr.getMemberCount();i++){
			EAction_method_relationship eamr = aamr.getByIndex(i);
			if(eamr.testDescription(null)){
				if(eamr.getDescription(null).equals("method")){
					eamr.deleteApplicationInstance();
				}
			}
		}
	}
	
}