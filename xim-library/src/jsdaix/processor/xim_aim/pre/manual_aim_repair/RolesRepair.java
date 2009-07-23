/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2009, LKSoftWare GmbH, Germany
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

package jsdaix.processor.xim_aim.pre.manual_aim_repair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jsdai.SPerson_organization_assignment_mim.AApplied_organization_assignment;
import jsdai.SPerson_organization_assignment_mim.AApplied_person_and_organization_assignment;
import jsdai.SPerson_organization_assignment_mim.CApplied_organization_assignment;
import jsdai.SPerson_organization_assignment_mim.CApplied_person_and_organization_assignment;
import jsdai.SPerson_organization_assignment_mim.EApplied_organization_assignment;
import jsdai.SPerson_organization_assignment_mim.EApplied_person_and_organization_assignment;
import jsdai.SPerson_organization_schema.EOrganization_role;
import jsdai.SPerson_organization_schema.EPerson_and_organization_role;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * This class tries to fix different roles (for example applied_person_and_organization_assignments),
 * so that if they are similar to pre defined ones, then they are substituted
 * to them in order to satisfy mapping.
 * <p>For example all pre defined roles should be in lower case, but there are
 * test cases, where they are in upper case. Or there should always be spaces
 * separating words in role, but some test cases have underscores instead.
 */
public class RolesRepair {
	
	/**
	 * Pre_defined_organization_or_person_in_organization_assignment_types
	 */
	public static final Map ORGANIZATION_ROLES;
	
	static {
		Map roles = new HashMap();
		roles.put("author", "author");
		roles.put("classification_officer", "classification officer");
		roles.put("creator", "creator");
		roles.put("custodian", "custodian");
		roles.put("customer", "customer");
		roles.put("design_supplier", "design supplier");
		roles.put("editor", "editor");
		roles.put("id_owner", "id owner");
		roles.put("location", "location");
		roles.put("manufacturer", "manufacturer");
		roles.put("owner", "owner");
		roles.put("read_access", "read access");
		roles.put("supplier", "supplier");
		roles.put("wholesaler", "wholesaler");
		roles.put("write_access", "write access");
		roles.put("organization_in_contract", "organization in contract");
		
		ORGANIZATION_ROLES = Collections.unmodifiableMap(roles);
	}

	private RolesRepair() { }
	
	public static void run(ASdaiModel domain, Importer importer)
		throws SdaiException {
		
		repairApaoaRoles(domain, importer);
		repairAoaRoles(domain, importer);
	}

	private static void repairApaoaRoles(ASdaiModel domain, Importer importer)
		throws SdaiException {

		// we travetrse all Applied_person_and_organization_assignments and not
		// Person_and_organization_roles here, because some Person_and_organization_roles
		// might be used not by Applied_person_and_organization_assignments, but other/unknown AOs

		AApplied_person_and_organization_assignment aApaoa = (AApplied_person_and_organization_assignment)
			domain.getInstances(CApplied_person_and_organization_assignment.definition);
		for (SdaiIterator i = aApaoa.createIterator(); i.next();) {
			EApplied_person_and_organization_assignment eApaoa = aApaoa.getCurrentMember(i);
			if (eApaoa.testRole(null)) {
				EPerson_and_organization_role eRole = eApaoa.getRole(null);
				if (eRole.testName(null)) {
					String role = eRole.getName(null);
					String correctRole = getCorrectRole(ORGANIZATION_ROLES, role);
					if (correctRole != null) {
						eRole.setName(null, correctRole);
						importer.logMessage("Fixing role name for "+eRole);
					}
				}
			}
		}
	}

	private static void repairAoaRoles(ASdaiModel domain, Importer importer)
		throws SdaiException {

		// we travetrse all Applied_organization_assignments and not
		// Organization_roles here, because some Organization_roles
		// might be used not by Applied_organization_assignments, but other/unknown AOs

		AApplied_organization_assignment aAoa = (AApplied_organization_assignment)
			domain.getInstances(CApplied_organization_assignment.definition);
		for (SdaiIterator i = aAoa.createIterator(); i.next();) {
			EApplied_organization_assignment eAoa = aAoa.getCurrentMember(i);
			if (eAoa.testRole(null)) {
				EOrganization_role eRole = eAoa.getRole(null);
				if (eRole.testName(null)) {
					String role = eRole.getName(null);
					String correctRole = getCorrectRole(ORGANIZATION_ROLES, role);
					if (correctRole != null) {
						eRole.setName(null, correctRole);
						importer.logMessage("Fixing role name for "+eRole);
					}
				}
			}
		}
	}
	
	private static String getCorrectRole(Map roles, String role) {
		for (Iterator i = roles.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			if (role.equalsIgnoreCase((String) entry.getKey())) {
				return (String) entry.getValue();
			}
		}

		return null;
	}
}
