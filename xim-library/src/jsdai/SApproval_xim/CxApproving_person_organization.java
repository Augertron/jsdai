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

import jsdai.SApproval_schema.*;
import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;

public class CxApproving_person_organization extends CApproving_person_organization implements EMappedXIMEntity{

	// From EApproval_person_organization
	// attribute (current explicit or supertype explicit) : role, base type: entity approval_role
/*	public static int usedinRole(EApproval_person_organization type, EApproval_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}*/
	private boolean testRole2(EApproval_person_organization type) throws SdaiException {
		return test_instance(a2);
	}
	private EApproval_role getRole2(EApproval_person_organization type) throws SdaiException {
		return (EApproval_role)get_instance(a2);
	}
	public void setRole(EApproval_person_organization type, EApproval_role value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetRole(EApproval_person_organization type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EApproval_person_organization type) throws SdaiException {
		return a2$;
	}
	// ENDOF From EApproval_person_organization
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		
		setTemp("AIM", CApproval_person_organization.definition);

		setMappingConstraints(context, this);

		//********** "approval" attributes
		//role_x
		setRole_x(context, this);
		
		// approval_date
		setApproval_date(context, this);

		// Clean ARM
		unsetRole_x(null);
		unsetApproval_date(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		//********** "approval" attributes
		//Role_x
		unsetRole_x(context, this);

		// approval_date
		unsetApproval_date(context, this);
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
			EApproving_person_organization armEntity) throws SdaiException {
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
			EApproving_person_organization armEntity) throws SdaiException {
	}

	//********** "Approving_person_organization" attributes

	/**
	 * Sets/creates data for role_x attribute.
	 * 
	 * <p>
	 * attribute_mapping role_x (role_x, $PATH, STRING); 
	 *  approval_person_organization.role -> approval_role
	 *  approval_role.role
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
	public static void setRole_x(SdaiContext context, CxApproving_person_organization armEntity)
			throws SdaiException {
		//unset old values
		unsetRole_x(context, armEntity);
		
		if (armEntity.testRole_x(null)) {
		
			LangUtils.Attribute_and_value_structure[] arStructure = {new LangUtils.Attribute_and_value_structure(
					CApproval_role
							.attributeRole(null), armEntity.getRole_x(null))};
			EApproval_role role = (EApproval_role) LangUtils
					.createInstanceIfNeeded(
							context,
							CApproval_role.definition,
							arStructure);
			armEntity.setRole(null, role);
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
			CxApproving_person_organization armEntity) throws SdaiException {
		if (armEntity.testRole2(null)) {
			EApproval_role role = armEntity.getRole2(null);
			armEntity.unsetRole(null);
			LangUtils.deleteInstanceIfUnused(context.domain, role);
		}
	}

	/**
	 * Sets/creates data for approval_date attribute.
	 * 
	 * <p>
	 *  (date_and_time_item = approval_person_organization
	 *  date_and_time_item &lt;-
	 *  applied_date_and_time_assignment.items[i]
	 *  applied_date_and_time_assignment &lt;=
	 *  date_and_time_assignment
	 *  {date_and_time_assignment.role -&gt;
	 *  date_time_role
	 *  date_time_role.name = 'sign off'})
	 * 
	 *  (date_item = approval_person_organization
	 *  date_item &lt;- applied_date_assignment.items[i]
	 *  applied_date_assignment &lt;= date_assignment
	 *  {date_assignment.role -&gt; date_role
	 *  date_role.name = 'sign off'}
	 *  {date_assignment.assigned_date -&gt; date
	 *  date =&gt; calendar_date})
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// APO <- ADATA {-> DTR}
	// APO <- ADA {-> DR} {-> CD}
	public static void setApproval_date(SdaiContext context, CxApproving_person_organization armEntity)
			throws SdaiException {
		//unset old values
		unsetApproval_date(context, armEntity);
		
		if (armEntity.testRole_x(null)) {
		
			LangUtils.Attribute_and_value_structure[] arStructure = {new LangUtils.Attribute_and_value_structure(
					CApproval_role
							.attributeRole(null), armEntity.getRole_x(null))};
			EApproval_role role = (EApproval_role) LangUtils
					.createInstanceIfNeeded(
							context,
							CApproval_role.definition,
							arStructure);
			armEntity.setRole(null, role);
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
	public static void unsetApproval_date(SdaiContext context,
			CxApproving_person_organization armEntity) throws SdaiException {
		if (armEntity.testRole2(null)) {
			EApproval_role role = armEntity.getRole2(null);
			armEntity.unsetRole(null);
			LangUtils.deleteInstanceIfUnused(context.domain, role);
		}
	}	
	
}