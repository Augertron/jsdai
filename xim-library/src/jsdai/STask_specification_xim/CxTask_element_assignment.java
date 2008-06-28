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
import jsdai.util.LangUtils;
import jsdai.SActivity_method_assignment_mim.CApplied_action_method_assignment;
import jsdai.SManagement_resources_schema.CAction_method_role;
import jsdai.SManagement_resources_schema.EAction_method_role;

public class CxTask_element_assignment extends CTask_element_assignment implements EMappedXIMEntity{


	// Taken from Organizational_project_assignment
	// ENDOF taken from Organizational_project_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_action_method_assignment.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
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
			ETask_element_assignment armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		/* Strange mapping constraints placed in attribute mapping 
		attribute_mapping assigned_task_element(assigned_task_element, $PATH, Task_element);
		applied_action_method_assignment 
		applied_action_method_assignment <= action_method_assignment
		action_method_assignment
		        {action_method_assignment.role -> action_method_role
				action_method_role.name = 'Task element assignment'
				}
		action_method_assignment.assigned_action_method -> action_method
		=> task_element
		end_attribute_mapping;
		*/
		if(armEntity.testRole(null)){
			EAction_method_role role = armEntity.getRole(null);
			// Additional stuff requested by Eva in order to support old implementations from EDGAG
			if(role.testName(null)){
				if(!role.testDescription(null)){
					role.setDescription(null, role.getName(null));
				}
			}
			role.setName(null, "Task element assignment");
		}else{
			LangUtils.Attribute_and_value_structure[] roleStructure = {new LangUtils.Attribute_and_value_structure(
				CAction_method_role.attributeName(null), "Task element assignment")};
			EAction_method_role role = (EAction_method_role)
				LangUtils.createInstanceIfNeeded(context, CAction_method_role.definition,
				roleStructure);
			armEntity.setRole(null, role);
		}
		
		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ETask_element_assignment armEntity) throws SdaiException {
	}

	
}