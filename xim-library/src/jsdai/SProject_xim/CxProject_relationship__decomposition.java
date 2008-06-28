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
import jsdai.SPerson_organization_schema.COrganizational_project_relationship;
import jsdai.SPerson_organization_schema.EOrganizational_project_relationship;

public class CxProject_relationship__decomposition extends CProject_relationship__decomposition implements EMappedXIMEntity{


	// Taken from CProject_relationship
	/// methods for attribute: relation_type, base type: STRING
/*	public boolean testRelation_type(EProject_relationship type) throws SdaiException {
		return testName((jsdai.SPerson_organization_schema.EOrganizational_project_relationship)null);
	}
	public String getRelation_type(EProject_relationship type) throws SdaiException {
		return getName((jsdai.SPerson_organization_schema.EOrganizational_project_relationship)null);
	}*/
	public void setRelation_type(EProject_relationship type, String value) throws SdaiException {
		setName((jsdai.SPerson_organization_schema.EOrganizational_project_relationship)null, value);
	}
	public void unsetRelation_type(EProject_relationship type) throws SdaiException {
		unsetName((jsdai.SPerson_organization_schema.EOrganizational_project_relationship)null);
	}
	public static jsdai.dictionary.EAttribute attributeRelation_type(EProject_relationship type) throws SdaiException {
		return attributeName((jsdai.SPerson_organization_schema.EOrganizational_project_relationship)null);
	}
	// ENDOF Taken from CProject_relationship	

	// Taken from COrganizational_project_relationship	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EOrganizational_project_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EOrganizational_project_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EOrganizational_project_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EOrganizational_project_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EOrganizational_project_relationship type) throws SdaiException {
		return a0$;
	}
	// ENDOF Taken from COrganizational_project_relationship
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", COrganizational_project_relationship.definition);

		setMappingConstraints(context, this);

		// clean ARM
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
			EProject_relationship__decomposition armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxProject_relationship.setMappingConstraints(context, armEntity);
		armEntity.setRelation_type(null, "decomposition");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EProject_relationship__decomposition armEntity) throws SdaiException {
		CxProject_relationship.unsetMappingConstraints(context, armEntity);
		armEntity.unsetRelation_type(null);
	}

	
}