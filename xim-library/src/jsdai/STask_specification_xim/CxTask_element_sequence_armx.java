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
import jsdai.SMethod_definition_schema.CSequential_method;
import jsdai.SMethod_definition_schema.ESequential_method;
import jsdai.STask_element_mim.ATask_element;
import jsdai.STask_element_mim.CTask_element_sequence;
import jsdai.STask_element_mim.ETask_element;

public class CxTask_element_sequence_armx extends CTask_element_sequence_armx implements EMappedXIMEntity{


	// Taken from Organizational_project_assignment
	// ENDOF taken from Organizational_project_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CTask_element_sequence.definition);

		setMappingConstraints(context, this);

		setElements(context, this);
		
		unsetElements(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetElements(context, this);			
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
			ETask_element_sequence_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxStructured_task_element.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ETask_element_sequence_armx armEntity) throws SdaiException {
		CxStructured_task_element.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * attribute_mapping elements(elements, $PATH, Task_element);
			task_element_sequence <=
			task_element <= 
			action_method <-
			[(action_method_relationship.relating_method
			action_method_relationship  =>
			serial_action_method  =>
			sequential_method <=
			serial_action_method  <=
			action_method_relationship
			action_method_relationship.related_method ->
			action_method)*]
		end_attribute_mapping;

	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// TES <- SM -> AM
	public static void setElements(SdaiContext context,
			ETask_element_sequence_armx armEntity) throws SdaiException {
		unsetElements(context, armEntity);
		if(armEntity.testElements(null)){
			ATask_element elements = armEntity.getElements(null);
			SdaiIterator iter = elements.createIterator();
			while(iter.next()){
				ETask_element element = elements.getCurrentMember(iter);
				ESequential_method esm = (ESequential_method)
					context.working_model.createEntityInstance(CSequential_method.definition);
				esm.setRelated_method(null, element);
				esm.setRelating_method(null, armEntity);
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
	public static void unsetElements(SdaiContext context,
			ETask_element_sequence_armx armEntity) throws SdaiException {
		AAction_method_relationship aamr = new AAction_method_relationship();
		CAction_method_relationship.usedinRelating_method(null, armEntity, context.domain, aamr);
		for(int i=1;i <= aamr.getMemberCount();i++){
			EAction_method_relationship eamr = aamr.getByIndex(i);
			if(eamr instanceof ESequential_method){
				if((eamr.testRelating_method(null))&&
						(eamr.getRelating_method(null) instanceof ETask_element)){
					eamr.deleteApplicationInstance();
				}
			}
		}
	}
	
	
}