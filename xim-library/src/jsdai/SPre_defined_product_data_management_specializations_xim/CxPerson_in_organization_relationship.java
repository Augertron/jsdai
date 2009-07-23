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
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SManagement_resources_schema.EPerson_and_organization_assignment;
import jsdai.SPerson_organization_assignment_mim.*;
import jsdai.SPerson_organization_schema.CPerson_and_organization_role;
import jsdai.SPerson_organization_schema.EPerson_and_organization_role;

public class CxPerson_in_organization_relationship extends CPerson_in_organization_relationship implements EMappedXIMEntity{


	// Taken from CPerson_and_organization_assignment
	// attribute (current explicit or supertype explicit) : role, base type: entity person_and_organization_role
/*	public static int usedinRole(EPerson_and_organization_assignment type, jsdai.SPerson_organization_schema.EPerson_and_organization_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRole(EPerson_and_organization_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public jsdai.SPerson_organization_schema.EPerson_and_organization_role getRole(EPerson_and_organization_assignment type) throws SdaiException {
		a1 = get_instance(a1);
		return (jsdai.SPerson_organization_schema.EPerson_and_organization_role)a1;
	}*/
	public void setRole(EPerson_and_organization_assignment type, jsdai.SPerson_organization_schema.EPerson_and_organization_role value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetRole(EPerson_and_organization_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EPerson_and_organization_assignment type) throws SdaiException {
		return a1$;
	}
	// ENDOF taken from CPerson_and_organization_assignment

	// Taken from CApplied_person_and_organization_assignment	
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EApplied_person_and_organization_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testItems(EApplied_person_and_organization_assignment type) throws SdaiException {
		return test_aggregate(a2);
	}
	public APerson_and_organization_item getItems(EApplied_person_and_organization_assignment type) throws SdaiException {
		if (a2 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a2;
	}*/
	public APerson_and_organization_item createItems(EApplied_person_and_organization_assignment type) throws SdaiException {
		a2 = (APerson_and_organization_item)create_aggregate_class(a2, a2$, APerson_and_organization_item.class, 0);
		return a2;
	}
	public void unsetItems(EApplied_person_and_organization_assignment type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EApplied_person_and_organization_assignment type) throws SdaiException {
		return a2$;
	}
	// ENDOF taken from CApplied_person_and_organization_assignment	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_person_and_organization_assignment.definition);

		setMappingConstraints(context, this);

		// relation_type
		setRelation_type(context, this);
		
		// description 
		setDescription(context, this);
		
		// clean ARM
		// relation_type
		unsetRelation_type(null);
		
		// description 
		unsetDescription(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// relation_type
			unsetRelation_type(context, this);
			
			// description 
			unsetDescription(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * 	applied_person_and_organization_assignment &lt;=
	 * 	person_and_organization_assignment 		
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EPerson_in_organization_relationship armEntity) throws SdaiException {
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
			EPerson_in_organization_relationship armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for Relation_type attribute.
	 * 
	 * <p>
	 * <aa attribute="relation_type" assertion_to="person_and_organization_role.name">
      	<aimelt xml:space="preserve">PATH</aimelt>
         	<refpath xml:space="preserve">
         		applied_person_and_organization_assignment &lt;=
         		person_and_organization_assignment
         		person_and_organization_assignment.role -&gt;
         		person_and_organization_role
         		person_and_organization_role.name
             </refpath>
      </aa>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setRelation_type(SdaiContext context,
			EPerson_in_organization_relationship armEntity) throws SdaiException {
		unsetRelation_type(context, armEntity);
		if(armEntity.testRelation_type(null)){
			String relation = armEntity.getRelation_type(null);
			// Role
			LangUtils.Attribute_and_value_structure[] roleStructure = {new LangUtils.Attribute_and_value_structure(
					CPerson_and_organization_role.attributeName(null), relation)};
				EPerson_and_organization_role role = (EPerson_and_organization_role)
					LangUtils.createInstanceIfNeeded(context, CPerson_and_organization_role.definition,
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
	public static void unsetRelation_type(SdaiContext context,
			EPerson_in_organization_relationship armEntity) throws SdaiException {
		armEntity.unsetRole(null);
	}

	/**
	 * Sets/creates data for Description attribute.
	 * 
	 * <p>
	 * <aa attribute="description" assertion_to="person_and_organization_role.description">
      	<aimelt xml:space="preserve">PATH</aimelt>
         	<refpath xml:space="preserve">
         		applied_person_and_organization_assignment &lt;=
         		person_and_organization_assignment
         		person_and_organization_assignment.role -&gt;
         		person_and_organization_role
         		person_and_organization_role.description
            </refpath>
      </aa>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setDescription(SdaiContext context,
			EPerson_in_organization_relationship armEntity) throws SdaiException {
		unsetDescription(context, armEntity);
		if(armEntity.testDescription(null)){
			String description = armEntity.getDescription(null);
			// Role must be set at relation_type
			EPerson_and_organization_role role = armEntity.getRole(null);
			CxAP210ARMUtilities.setDerviedDescription(context, role, description);
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
			EPerson_in_organization_relationship armEntity) throws SdaiException {
		if(armEntity.testRole(null)){
			EPerson_and_organization_role role = armEntity.getRole(null);
			CxAP210ARMUtilities.unsetDerviedDescription(context, role);
		}
	}
	
	
}