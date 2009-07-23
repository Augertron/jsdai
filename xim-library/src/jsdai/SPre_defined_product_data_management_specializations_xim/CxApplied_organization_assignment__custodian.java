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
import jsdai.SManagement_resources_schema.EOrganization_assignment;
import jsdai.SPerson_organization_assignment_mim.CApplied_organization_assignment;
import jsdai.SPerson_organization_schema.*;

public class CxApplied_organization_assignment__custodian extends CApplied_organization_assignment__custodian implements EMappedXIMEntity{


	// Taken from Organization_assignment
	// attribute (current explicit or supertype explicit) : role, base type: entity organization_role
/*	public static int usedinRole(EOrganization_assignment type, jsdai.SPerson_organization_schema.EOrganization_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRole(EOrganization_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public jsdai.SPerson_organization_schema.EOrganization_role getRole(EOrganization_assignment type) throws SdaiException {
		a1 = get_instance(a1);
		return (jsdai.SPerson_organization_schema.EOrganization_role)a1;
	}*/
	public void setRole(EOrganization_assignment type, jsdai.SPerson_organization_schema.EOrganization_role value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetRole(EOrganization_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EOrganization_assignment type) throws SdaiException {
		return a1$;
	}

	// ENDOF taken from Organization_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_organization_assignment.definition);

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
			EApplied_organization_assignment__custodian armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPre_defined_organization_or_person_in_organization_assignment__aoa.setMappingConstraints(context, armEntity);
		// Role
		LangUtils.Attribute_and_value_structure[] roleStructure = {new LangUtils.Attribute_and_value_structure(
				COrganization_role.attributeName(null), "custodian")};
			EOrganization_role role = (EOrganization_role)
				LangUtils.createInstanceIfNeeded(context, COrganization_role.definition,
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
			EApplied_organization_assignment__custodian armEntity) throws SdaiException {
		CxPre_defined_organization_or_person_in_organization_assignment__aoa.unsetMappingConstraints(context, armEntity);
		armEntity.unsetRole(null);
	}

	
}