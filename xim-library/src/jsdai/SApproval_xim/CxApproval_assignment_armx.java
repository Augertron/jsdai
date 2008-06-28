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

package jsdai.SApproval_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version 
 */

import jsdai.SApproval_mim.*;
import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;

public class CxApproval_assignment_armx extends CApproval_assignment_armx implements EMappedXIMEntity{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		
		setTemp("AIM", CApplied_approval_assignment.definition);

		setMappingConstraints(context, this);

		//********** "approval" attributes
		//role_x
		setRole_x(context, this);

		// Clean ARM
		unsetRole_x(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		//********** "approval" attributes
		//Role_x
		unsetRole_x(context, this);

	}


	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 * {applied_approval_assignment <= approval_assignment}
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
			EApproval_assignment_armx armEntity) throws SdaiException {
		//unset old data
		unsetMappingConstraints(context, armEntity);

		//mapping constraints satisfied on creation of is_applied_to attribute
		// mapping
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EApproval_assignment_armx armEntity) throws SdaiException {
	}

	//********** "approval_assignment" attributes

	/**
	 * Sets/creates data for role_x attribute.
	 * 
	 * <p>
	 * attribute_mapping role_x (role_x, $PATH, STRING); 
	 *  applied_approval_assignment <= approval_assignment
	 *  approval_assignment = role_select
	 *  role_select <- role_association.item_with_role
	 *  role_association.role -> object_role
	 *  object_role.name 
	 * end_attribute_mapping;
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setRole_x(SdaiContext context, EApproval_assignment_armx armEntity)
			throws SdaiException {
		//unset old values
		unsetRole_x(context, armEntity);

		if (armEntity.testRole_x(null)) {
			LangUtils.Attribute_and_value_structure[] orStructure = {new LangUtils.Attribute_and_value_structure(
					jsdai.SBasic_attribute_schema.CObject_role
							.attributeName(null), armEntity.getRole_x(null))};
			jsdai.SBasic_attribute_schema.EObject_role role = (jsdai.SBasic_attribute_schema.EObject_role) LangUtils
					.createInstanceIfNeeded(
							context,
							jsdai.SBasic_attribute_schema.CObject_role.definition,
							orStructure);
			
			CxAp214ArmUtilities.setDerivedRole(context, armEntity, role);
		}
	}

	/**
	 * Unsets/deletes data for planned_date attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetRole_x(SdaiContext context,
			EApproval_assignment_armx armEntity) throws SdaiException {
			jsdai.SBasic_attribute_schema.EObject_role role = CxAp214ArmUtilities
					.getDerivedRole(context, armEntity);
			if (role != null && role.testName(null)) {
				CxAp214ArmUtilities.unsetDerivedRole(context,
						armEntity);
			}
	}
}