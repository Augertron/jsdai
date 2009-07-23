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

package jsdai.SPre_defined_product_data_management_specializations_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SDate_time_assignment_mim.CApplied_date_and_time_assignment;
import jsdai.SDate_time_schema.*;
import jsdai.SManagement_resources_schema.EDate_and_time_assignment;

public class CxApplied_date_and_time_assignment__start_date extends CApplied_date_and_time_assignment__start_date implements EMappedXIMEntity{


	// Taken from Date_and_time_assignment
	// attribute (current explicit or supertype explicit) : role, base type: entity date_time_role
/*	public static int usedinRole(EDate_and_time_assignment type, jsdai.SDate_time_schema.EDate_time_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRole(EDate_and_time_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public jsdai.SDate_time_schema.EDate_time_role getRole(EDate_and_time_assignment type) throws SdaiException {
		a1 = get_instance(a1);
		return (jsdai.SDate_time_schema.EDate_time_role)a1;
	}*/
	public void setRole(EDate_and_time_assignment type, jsdai.SDate_time_schema.EDate_time_role value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetRole(EDate_and_time_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EDate_and_time_assignment type) throws SdaiException {
		return a1$;
	}

	// ENDOF taken from Date_and_time_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_date_and_time_assignment.definition);

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
			EApplied_date_and_time_assignment__start_date armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPre_defined_date_or_date_time_assignment__data.setMappingConstraints(context, armEntity);
		// Role
		LangUtils.Attribute_and_value_structure[] roleStructure = {new LangUtils.Attribute_and_value_structure(
				CDate_time_role.attributeName(null), "start date")};
			EDate_time_role role = (EDate_time_role)
				LangUtils.createInstanceIfNeeded(context, CDate_time_role.definition,
						roleStructure);
		armEntity.setRole(null, role);
		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EApplied_date_and_time_assignment__start_date armEntity) throws SdaiException {
		CxPre_defined_date_or_date_time_assignment__data.unsetMappingConstraints(context, armEntity);
		armEntity.unsetRole(null);
	}

	
}