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
 * @author Valdas Zigas, Giedrius Liutkus
 * @version 
 */

import jsdai.SApproval_schema.*;
import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;

public class CxApproval_armx extends CApproval_armx  implements EMappedXIMEntity{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		
		setTemp("AIM", jsdai.SApproval_schema.CApproval.definition);

		setMappingConstraints(context, this);

		//********** "approval" attributes
		//planned_date
		setPlanned_date(context, this);

		//actual_date
		setActual_date(context, this);
			
		// Clean ARM
		unsetPlanned_date(null);
		unsetActual_date(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		//********** "approval" attributes
		//planned_date
		unsetPlanned_date(context, this);

		//actual_date
		unsetActual_date(context, this);
	}

	//		************************************* EX-AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; {approval <- approval_assignment.assigned_approval
	 * approval_assignment => applied_approval_assignment}
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
			EApproval_armx armEntity) throws SdaiException {
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
			EApproval_armx armEntity) throws SdaiException {
	}

	//********** "approval" attributes

	/**
	 * Sets/creates data for planned_date attribute.
	 * 
	 * <p>
	 * attribute_mapping planned_date_date_time (planned_date , $PATH, date_time); 
	 *  approval <- 
	 * approval_date_time.dated_approval
	 * approval_date_time 
	 * {approval_date_time.role -> 
	 * object_role
	 * object_role.name = 'planned'} 
	 * approval_date_time.date_time ->
	 * date_time_select 
	 * (date_time_select = date date => calendar_date)
	 * (date_time_select = date_and_time date_and_time) 
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
	public static void setPlanned_date(SdaiContext context, EApproval_armx armEntity)
			throws SdaiException {
		//unset old values
		unsetPlanned_date(context, armEntity);

		if (armEntity.testPlanned_date(null)) {
			jsdai.SApproval_schema.EApproval aimEntity = armEntity;
			EEntity armPlanned_date = armEntity.getPlanned_date(null);
			EEntity aimPlanned_date = CxAp214ArmUtilities.getAimInstance(
					armPlanned_date, context);

			//approval_date_time
			jsdai.SApproval_schema.EApproval_date_time approval_date_time = (jsdai.SApproval_schema.EApproval_date_time) context.working_model
					.createEntityInstance(jsdai.SApproval_schema.EApproval_date_time.class);
			approval_date_time.setDated_approval(null, aimEntity);
			approval_date_time.setDate_time(null, aimPlanned_date);

			//object_role
			LangUtils.Attribute_and_value_structure[] orStructure = {new LangUtils.Attribute_and_value_structure(
					jsdai.SBasic_attribute_schema.CObject_role
							.attributeName(null), "planned"),};
			jsdai.SBasic_attribute_schema.EObject_role role = (jsdai.SBasic_attribute_schema.EObject_role) LangUtils
					.createInstanceIfNeeded(
							context,
							jsdai.SBasic_attribute_schema.CObject_role.definition,
							orStructure);

			CxAp214ArmUtilities.setDerivedRole(context, approval_date_time,
					role);
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
	public static void unsetPlanned_date(SdaiContext context,
			EApproval_armx armEntity) throws SdaiException {
		jsdai.SApproval_schema.EApproval aimEntity = armEntity;

		//approval_date_time
		jsdai.SApproval_schema.EApproval_date_time approval_date_time = null;
		jsdai.SApproval_schema.AApproval_date_time aApproval_date_time = new jsdai.SApproval_schema.AApproval_date_time();
		jsdai.SApproval_schema.CApproval_date_time.usedinDated_approval(null,
				aimEntity, context.domain, aApproval_date_time);
		for (int i = 1; i <= aApproval_date_time.getMemberCount(); i++) {
			approval_date_time = aApproval_date_time.getByIndex(i);

			jsdai.SBasic_attribute_schema.EObject_role role = CxAp214ArmUtilities
					.getDerivedRole(context, approval_date_time);
			if (role != null && role.testName(null)
					&& role.getName(null).equals("planned")) {
				CxAp214ArmUtilities.unsetDerivedRole(context,
						approval_date_time);
				approval_date_time.deleteApplicationInstance();
			}
		}
	}

	/**
	 * Sets/creates data for actual_date attribute.
	 * 
	 * <p>
	 * attribute_mapping actual_date_date_time (actual_date , $PATH, date_time);
	 * approval <- approval_date_time.dated_approval approval_date_time
	 * {approval_date_time.role -> object_role object_role.name = 'actual'}
	 * approval_date_time.date_time -> date_time_select (date_time_select = date
	 * date => calendar_date) (date_time_select = date_and_time date_and_time)
	 * end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setActual_date(SdaiContext context, EApproval_armx armEntity)
			throws SdaiException {
		//unset old values
		unsetActual_date(context, armEntity);

		if (armEntity.testActual_date(null)) {
			jsdai.SApproval_schema.EApproval aimEntity = armEntity;
			EEntity armActual_date = armEntity.getActual_date(null);
			EEntity aimActual_date = CxAp214ArmUtilities.getAimInstance(
					armActual_date, context);

			//approval_date_time
			jsdai.SApproval_schema.EApproval_date_time approval_date_time = (jsdai.SApproval_schema.EApproval_date_time) context.working_model
					.createEntityInstance(jsdai.SApproval_schema.EApproval_date_time.class);
			approval_date_time.setDated_approval(null, aimEntity);
			approval_date_time.setDate_time(null, aimActual_date);

			//object_role
			LangUtils.Attribute_and_value_structure[] orStructure = {new LangUtils.Attribute_and_value_structure(
					jsdai.SBasic_attribute_schema.CObject_role
							.attributeName(null), "actual"),};
			jsdai.SBasic_attribute_schema.EObject_role role = (jsdai.SBasic_attribute_schema.EObject_role) LangUtils
					.createInstanceIfNeeded(
							context,
							jsdai.SBasic_attribute_schema.CObject_role.definition,
							orStructure);

			CxAp214ArmUtilities.setDerivedRole(context, approval_date_time,
					role);
		}
	}

	/**
	 * Unsets/deletes data for actual_date attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetActual_date(SdaiContext context,
			EApproval armEntity) throws SdaiException {
		jsdai.SApproval_schema.EApproval aimEntity = armEntity;

		//approval_date_time
		jsdai.SApproval_schema.EApproval_date_time approval_date_time = null;
		jsdai.SApproval_schema.AApproval_date_time aApproval_date_time = new jsdai.SApproval_schema.AApproval_date_time();
		jsdai.SApproval_schema.CApproval_date_time.usedinDated_approval(null,
				aimEntity, context.domain, aApproval_date_time);
		for (int i = 1; i <= aApproval_date_time.getMemberCount(); i++) {
			approval_date_time = aApproval_date_time.getByIndex(i);

			jsdai.SBasic_attribute_schema.EObject_role role = CxAp214ArmUtilities
					.getDerivedRole(context, approval_date_time);
			if (role != null && role.testName(null)
					&& role.getName(null).equals("actual")) {
				CxAp214ArmUtilities.unsetDerivedRole(context,
						approval_date_time);
				approval_date_time.deleteApplicationInstance();
			}
		}
	}
}