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
import jsdai.SPerson_organization_mim.CPerson_and_organization_address;
import jsdai.SPerson_organization_schema.EOrganizational_address;

public class CxPerson_and_organization_address__visitor_address extends CPerson_and_organization_address__visitor_address implements EMappedXIMEntity{


	// Taken from Organizational_project_assignment
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EOrganizational_address type) throws SdaiException {
		return test_string(a13);
	}
	public String getDescription(EOrganizational_address type) throws SdaiException {
		return get_string(a13);
	}*/
	public void setDescription(EOrganizational_address type, String value) throws SdaiException {
		a13 = set_string(value);
	}
	public void unsetDescription(EOrganizational_address type) throws SdaiException {
		a13 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EOrganizational_address type) throws SdaiException {
		return a13$;
	}
	// ENDOF taken from Organizational_project_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPerson_and_organization_address.definition);

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
			EPerson_and_organization_address__visitor_address armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPre_defined_address_assignment__paoa.setMappingConstraints(context, armEntity);
		armEntity.setDescription((EOrganizational_address)null, "visitor address");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPerson_and_organization_address__visitor_address armEntity) throws SdaiException {
		CxPre_defined_address_assignment__paoa.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((EOrganizational_address)null);
	}

	
}