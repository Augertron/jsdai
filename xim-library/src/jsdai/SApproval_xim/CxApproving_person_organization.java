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
import jsdai.SDate_time_assignment_mim.AApplied_date_and_time_assignment;
import jsdai.SDate_time_assignment_mim.AApplied_date_assignment;
import jsdai.SDate_time_assignment_mim.ADate_and_time_item;
import jsdai.SDate_time_assignment_mim.ADate_item;
import jsdai.SDate_time_assignment_mim.CApplied_date_and_time_assignment;
import jsdai.SDate_time_assignment_mim.CApplied_date_assignment;
import jsdai.SDate_time_assignment_mim.EApplied_date_and_time_assignment;
import jsdai.SDate_time_assignment_mim.EApplied_date_assignment;
import jsdai.SDate_time_schema.CDate_role;
import jsdai.SDate_time_schema.CDate_time_role;
import jsdai.SDate_time_schema.ECalendar_date;
import jsdai.SDate_time_schema.EDate_and_time;
import jsdai.SDate_time_schema.EDate_role;
import jsdai.SDate_time_schema.EDate_time_role;
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
	 *  date_time_role.name = 'sign off'}
	 *  date_and_time_assignment.assigned_date_and_time ->
		date_and_time)
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
		
		if (armEntity.testApproval_date(null)) {
			String name = "sign off";
			EEntity approval_date = armEntity.getApproval_date(null);
			if(approval_date instanceof ECalendar_date){
				// date_role
				LangUtils.Attribute_and_value_structure[] drStructure = {new LangUtils.Attribute_and_value_structure(
						CDate_role.attributeName(null), name)};
				EDate_role role = (EDate_role) LangUtils
					.createInstanceIfNeeded(
						context,
						CDate_role.definition,
						drStructure);
				// applied_date_assignment
				LangUtils.Attribute_and_value_structure[] adaStructure = {
					new LangUtils.Attribute_and_value_structure(
						CApplied_date_assignment.attributeAssigned_date(null), approval_date),
					new LangUtils.Attribute_and_value_structure(
						CApplied_date_assignment.attributeRole(null), role),
				};
				EApplied_date_assignment eada = (EApplied_date_assignment) LangUtils
					.createInstanceIfNeeded(
						context,
						CApplied_date_assignment.definition,
						adaStructure);
				ADate_item items;
				if(eada.testItems(null)){
					items = eada.getItems(null);
				}else{
					items = eada.createItems(null);
				}
				if(!items.isMember(armEntity)){
					items.addUnordered(armEntity);
				}
			}else if(approval_date instanceof EDate_and_time){
				// date_role
				LangUtils.Attribute_and_value_structure[] datrStructure = {new LangUtils.Attribute_and_value_structure(
						CDate_time_role.attributeName(null), name)};
				EDate_time_role role = (EDate_time_role) LangUtils
					.createInstanceIfNeeded(
						context,
						CDate_time_role.definition,
						datrStructure);
				// applied_date_and_time_assignment
				LangUtils.Attribute_and_value_structure[] adaStructure = {
					new LangUtils.Attribute_and_value_structure(
						CApplied_date_and_time_assignment.attributeAssigned_date_and_time(null), approval_date),
					new LangUtils.Attribute_and_value_structure(
						CApplied_date_and_time_assignment.attributeRole(null), role),
				};
				EApplied_date_and_time_assignment eadata = (EApplied_date_and_time_assignment) LangUtils
					.createInstanceIfNeeded(
						context,
						CApplied_date_and_time_assignment.definition,
						adaStructure);
				ADate_and_time_item items;
				if(eadata.testItems(null)){
					items = eadata.getItems(null);
				}else{
					items = eadata.createItems(null);
				}
				if(!items.isMember(armEntity)){
					items.addUnordered(armEntity);
				}
			}else{
				System.err.println("Unsupported type of date for CApproving_person_organization.approval_date "+armEntity);
			}
			
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
		AApplied_date_and_time_assignment adata = new AApplied_date_and_time_assignment();
		CApplied_date_and_time_assignment.usedinItems(null, armEntity, context.domain, adata);
		for(int i=1,count=adata.getMemberCount(); i<=count; i++){
			EApplied_date_and_time_assignment edata = adata.getByIndex(i);
			ADate_and_time_item items = edata.getItems(null);
			items.removeUnordered(armEntity);
			if(items.getMemberCount() == 0){
				edata.deleteApplicationInstance();
			}
		}
		
		AApplied_date_assignment ada = new AApplied_date_assignment();
		CApplied_date_assignment.usedinItems(null, armEntity, context.domain, ada);
		for(int i=1,count=ada.getMemberCount(); i<=count; i++){
			EApplied_date_assignment eda = ada.getByIndex(i);
			ADate_item items = eda.getItems(null);
			items.removeUnordered(armEntity);
			if(items.getMemberCount() == 0){
				eda.deleteApplicationInstance();
			}
		}
		
	}	
	
}