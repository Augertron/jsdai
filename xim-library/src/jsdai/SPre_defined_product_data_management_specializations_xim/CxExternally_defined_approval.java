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
import jsdai.SApproval_schema.CApproval;
import jsdai.SApproval_schema.EApproval;
import jsdai.SApproval_xim.CxApproval_armx;
import jsdai.SApproval_xim.EApproval_armx;

public class CxExternally_defined_approval extends CExternally_defined_approval implements EMappedXIMEntity{

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApproval.definition);

		setMappingConstraints(context, this);

		//planned_date
		setPlanned_date(context, this);

		//actual_date
		setActual_date(context, this);
			
		// Clean ARM
		unsetPlanned_date(null);
		unsetActual_date(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			//planned_date
			unsetPlanned_date(context, this);

			//actual_date
			unsetActual_date(context, this);
			
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
			EExternally_defined_approval armEntity) throws SdaiException {
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
			EExternally_defined_approval armEntity) throws SdaiException {
	}

	//********** "approval" attributes

	/**
	 * Sets/creates data for planned_date attribute.
	 * 
	 * <p>
	 * attribute_mapping planned_date_date_time (planned_date , $PATH, date_time); 
	 *  approval <- 
	 * approval_date_time.dated_approval
	 * approval_date_time 
	 * {approval_date_time.role -> 
	 * object_role
	 * object_role.name = 'planned'} 
	 * approval_date_time.date_time ->
	 * date_time_select 
	 * (date_time_select = date date => calendar_date)
	 * (date_time_select = date_and_time date_and_time) 
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
	public static void setPlanned_date(SdaiContext context, EApproval_armx armEntity)
			throws SdaiException {
		CxApproval_armx.setPlanned_date(context, armEntity);
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
	public static void unsetPlanned_date(SdaiContext context,
			EApproval_armx armEntity) throws SdaiException {
		CxApproval_armx.unsetPlanned_date(context, armEntity);		
	}

	/**
	 * Sets/creates data for actual_date attribute.
	 * 
	 * <p>
	 * attribute_mapping actual_date_date_time (actual_date , $PATH, date_time);
	 * approval <- approval_date_time.dated_approval approval_date_time
	 * {approval_date_time.role -> object_role object_role.name = 'actual'}
	 * approval_date_time.date_time -> date_time_select (date_time_select = date
	 * date => calendar_date) (date_time_select = date_and_time date_and_time)
	 * end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setActual_date(SdaiContext context, EApproval_armx armEntity)
			throws SdaiException {
		CxApproval_armx.setActual_date(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for actual_date attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetActual_date(SdaiContext context,
			EApproval armEntity) throws SdaiException {
		CxApproval_armx.unsetActual_date(context, armEntity);
	}
	
}