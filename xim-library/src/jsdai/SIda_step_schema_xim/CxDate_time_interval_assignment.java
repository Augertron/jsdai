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

package jsdai.SIda_step_schema_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SDate_time_schema.*;
import jsdai.SManagement_resources_schema.ETime_interval_assignment;
import jsdai.STime_interval_assignment_mim.CApplied_time_interval_assignment;

public class CxDate_time_interval_assignment extends CDate_time_interval_assignment implements EMappedXIMEntity{


	// Taken from CTime_interval_assignment
	// attribute (current explicit or supertype explicit) : role, base type: entity time_interval_role
/*	public static int usedinRole(ETime_interval_assignment type, jsdai.SDate_time_schema.ETime_interval_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRole(ETime_interval_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public jsdai.SDate_time_schema.ETime_interval_role getRole(ETime_interval_assignment type) throws SdaiException {
		a1 = get_instance(a1);
		return (jsdai.SDate_time_schema.ETime_interval_role)a1;
	}*/
	public void setRole(ETime_interval_assignment type, jsdai.SDate_time_schema.ETime_interval_role value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetRole(ETime_interval_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(ETime_interval_assignment type) throws SdaiException {
		return a1$;
	}
	// ENDOF taken from CTime_interval_assignment	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_time_interval_assignment.definition);

		setMappingConstraints(context, this);

		// role_x
		setRole_x(context, this);
		
		// description 
		setDescription(context, this);
		
		// clean ARM
		// role_x
		unsetRole_x(null);
		
		// description 
		unsetDescription(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// role_x
			unsetRole_x(context, this);
			
			// description 
			unsetDescription(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * 	applied_time_interval_assignment <=
	 * 	time_interval_assignment
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EDate_time_interval_assignment armEntity) throws SdaiException {
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
			EDate_time_interval_assignment armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for Role_x attribute.
	 * 
	 * <p>
	 * attribute_mapping role (role
	 * , time_interval_role.name);
	 		applied_time_interval_assignment <=
	 		time_interval_assignment
	 		time_interval_assignment.role ->
	 		time_interval_role
	 		time_interval_role.name
      end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setRole_x(SdaiContext context,
			EDate_time_interval_assignment armEntity) throws SdaiException {
		unsetRole_x(context, armEntity);
		if(armEntity.testRole_x(null)){
			String relation = armEntity.getRole_x(null);
			// Role
			LangUtils.Attribute_and_value_structure[] roleStructure = {new LangUtils.Attribute_and_value_structure(
					CTime_interval_role.attributeName(null), relation)};
				ETime_interval_role role = (ETime_interval_role)
					LangUtils.createInstanceIfNeeded(context, CTime_interval_role.definition,
							roleStructure);
			armEntity.setRole(null, role);
		}

	}

	/**
	 * Unsets/deletes mapping for Relation_type attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetRole_x(SdaiContext context,
			EDate_time_interval_assignment armEntity) throws SdaiException {
		armEntity.unsetRole(null);
	}

	/**
	 * Sets/creates data for Description attribute.
	 * 
	 * <p>
	 * attribute_mapping description (description
	 * , time_interval_role.description);
	 		applied_time_interval_assignment <=
	 		time_interval_assignment
	 		time_interval_assignment.role ->
	 		time_interval_role
	 		time_interval_role.description
      end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setDescription(SdaiContext context,
			EDate_time_interval_assignment armEntity) throws SdaiException {
		unsetDescription(context, armEntity);
		int result = armEntity.testDescription(null);
		if(result == 0)
			return;
		if (result == EDate_time_interval_assignment.sDescriptionDefault_language_string) {
			String armDescription = armEntity.getDescription(null,
					(EDefault_language_string) null);
			ETime_interval_role role = armEntity.getRole(null);
			CxAP210ARMUtilities.setDerviedDescription(context, role, armDescription);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, "Multi language strings are not supported for this type yet "+armEntity);
/*			String multi_language_string = armEntity.getDescription(null);
			CxAp214ArmUtilities.createMultiLanguageAttribute(context,
					armEntity, multi_language_string, "purpose");*/
		}
	}

	/**
	 * Unsets/deletes mapping for Description attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetDescription(SdaiContext context,
			EDate_time_interval_assignment armEntity) throws SdaiException {
		if(armEntity.testRole(null)){
			ETime_interval_role role = armEntity.getRole(null);
			CxAP210ARMUtilities.unsetDerviedDescription(context, role);
		}
		// Not supported yet
		// CxAp214ArmUtilities.removeMultiLanguageAttribute(context, armEntity, "purpose");

	}
	
	
}