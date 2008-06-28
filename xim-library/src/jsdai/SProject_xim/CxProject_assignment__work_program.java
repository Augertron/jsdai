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

package jsdai.SProject_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SManagement_resources_schema.*;
import jsdai.SProject_mim.CApplied_organizational_project_assignment;

public class CxProject_assignment__work_program extends CProject_assignment__work_program implements EMappedXIMEntity{


	// Taken from Organizational_project_assignment
	// attribute (current explicit or supertype explicit) : role, base type: entity organizational_project_role
/*	public static int usedinRole(EOrganizational_project_assignment type, EOrganizational_project_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRole(EOrganizational_project_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public EOrganizational_project_role getRole(EOrganizational_project_assignment type) throws SdaiException {
		a1 = get_instance(a1);
		return (EOrganizational_project_role)a1;
	}*/
	public void setRole(EOrganizational_project_assignment type, EOrganizational_project_role value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetRole(EOrganizational_project_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EOrganizational_project_assignment type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF taken from Organizational_project_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_organizational_project_assignment.definition);

		setMappingConstraints(context, this);

		setRole_x(context, this);
		
		// clean ARM
//		unsetRole_x(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetRole_x(context, this);
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
			EProject_assignment armEntity) throws SdaiException {
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
			EProject_assignment armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for mapping scope.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setRole_x(SdaiContext context,
			EProject_assignment armEntity) throws SdaiException {
		String rolex = "work program";
		unsetRole_x(context, armEntity);
			// Object_role 
         LangUtils.Attribute_and_value_structure[] orStructure =
			{new LangUtils.Attribute_and_value_structure(
					COrganizational_project_role.attributeName(null),
              	rolex)
			};
			EOrganizational_project_role eopr = (EOrganizational_project_role)
				LangUtils.createInstanceIfNeeded(context, COrganizational_project_role.definition, orStructure);
			armEntity.setRole(null, eopr);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetRole_x(SdaiContext context,
			EProject_assignment armEntity) throws SdaiException {
		armEntity.unsetRole(null);
	}
	
}