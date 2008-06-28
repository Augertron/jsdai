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
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SDate_time_assignment_mim.*;
import jsdai.SDate_time_schema.*;
import jsdai.SEvent_assignment_mim.*;
import jsdai.SEvent_xim.EEvent;
import jsdai.SPerson_organization_schema.COrganizational_project;

public class CxProject extends CProject implements EMappedXIMEntity{


	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", COrganizational_project.definition);

		setMappingConstraints(context, this);

		// planned_start_date
		setPlanned_start_date(context, this);
		
      // planned_end_date
		setPlanned_end_date(context, this);
		
      // actual_start_date
		setActual_start_date(context, this);

      // actual_end_date 		
		setActual_end_date(context, this);
		
		// id_x
		setId_x(context, this);
		
		// clean ARM
		// planned_start_date
		unsetPlanned_start_date(null);
		
      // planned_end_date
		unsetPlanned_end_date(null);
		
      // actual_start_date
		unsetActual_start_date(null);

      // actual_end_date 		
		unsetActual_end_date(null);
		
		// id_x
		unsetId_x(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// planned_start_date
			unsetPlanned_start_date(context, this);
			
	      // planned_end_date
			unsetPlanned_end_date(context, this);
			
	      // actual_start_date
			unsetActual_start_date(context, this);

	      // actual_end_date 		
			unsetActual_end_date(context, this);
			
			// id_x
			unsetId_x(context, this);

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
			EProject armEntity) throws SdaiException {
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
			EProject armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for mapping planned_start_date.
	 * 
	 *      <aa attribute="planned_start_date" assertion_to="Calendar_date">
       <aimelt>PATH</aimelt>
		 <refpath>
		   organizational_project
			 date_item = organizational_project
			 date_item &lt;- applied_date_assignment.items[i]
			 applied_date_assignment &lt;=
			 date_assignment
			 {date_assignment.role -&gt; date_role
			  date_role.name = 'planned start'}
			 date_assignment.assigned_date -&gt; date
			 date =&gt; calendar_date
		 </refpath>
     </aa>
     <aa attribute="planned_start_date" assertion_to="Date_time">
       <aimelt>PATH</aimelt>
		 <refpath>
		   organizational_project
			 date_and_time_item = organizational_project
			 date_and_time_item &lt;- applied_date_and_time_assignment.items[i]
			 applied_date_and_time_assignment &lt;=
			 date_and_time_assignment
			 {date_and_time_assignment.role -&gt; date_time_role
			  date_time_role.name = 'planned start'}
			 date_and_time_assignment.assigned_date_and_time 
			 -&gt; date_and_time
		 </refpath>
		 </aa>
     <aa attribute="planned_start_date" assertion_to="Event">
       <aimelt>PATH</aimelt>
		 <refpath>
		   organizational_project
			 event_occurrence_item = organizational_project
			 event_occurrence_item &lt;- applied_event_occurrence_assignment.items[i]
			 applied_event_occurrence_assignment &lt;= event_occurrence_assignment
			 event_occurrence_assignment.role -&gt; event_occurrence_role
			 {event_occurrence_role.name='planned start'}
		 </refpath>
		 </aa>
     
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// OP <- ADA -> CD
	// OP <- ADATA -> DAT
	// OP <- AEOA -> EOR
	public static void setPlanned_start_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		unsetPlanned_start_date(context, armEntity);
		if(armEntity.testPlanned_start_date(null)){
			EEntity date = armEntity.getPlanned_start_date(null);
			String name = "planned start";
			setPlanned_xxx_date(context, armEntity, name, date);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetPlanned_start_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		String name = "planned start";
		unsetPlanned_xxx_date(context, armEntity, name);
	}

	// OP <- ADA -> CD
	// OP <- ADATA -> DAT
	// OP <- AEOA -> EOR
	public static void setPlanned_end_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		unsetPlanned_end_date(context, armEntity);
		if(armEntity.testPlanned_end_date(null)){
			EEntity date = armEntity.getPlanned_end_date(null);
			String name = "planned end";
			setPlanned_xxx_date(context, armEntity, name, date);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetPlanned_end_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		String name = "planned end";
		unsetPlanned_xxx_date(context, armEntity, name);
	}

	// OP <- ADA -> CD
	// OP <- ADATA -> DAT
	public static void setActual_start_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		unsetActual_start_date(context, armEntity);
		if(armEntity.testActual_start_date(null)){
			EEntity date = armEntity.getActual_start_date(null);
			String name = "actual start";
			setPlanned_xxx_date(context, armEntity, name, date);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetActual_start_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		String name = "actual start";
		unsetPlanned_xxx_date(context, armEntity, name);
	}

	// OP <- ADA -> CD
	// OP <- ADATA -> DAT
	public static void setActual_end_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		unsetActual_end_date(context, armEntity);
		if(armEntity.testActual_end_date(null)){
			EEntity date = armEntity.getActual_end_date(null);
			String name = "actual end";
			setPlanned_xxx_date(context, armEntity, name, date);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetActual_end_date(SdaiContext context,
			EProject armEntity) throws SdaiException {
		String name = "actual end";
		unsetPlanned_xxx_date(context, armEntity, name);
	}

	public static void setId_x(SdaiContext context,
			EProject armEntity) throws SdaiException {
		unsetId_x(context, armEntity);
		if(armEntity.testId_x(null)){
			String idx = armEntity.getId_x(null);
			CxAP210ARMUtilities.setId(context, armEntity, idx);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context,
			EProject armEntity) throws SdaiException {
		CxAP210ARMUtilities.unsetId(context, armEntity);
	}
	
	
	
	/**
	 * Sets/creates data for mapping planned_start_date.
	 * 
	 *      <aa attribute="planned_start_date" assertion_to="Calendar_date">
       <aimelt>PATH</aimelt>
		 <refpath>
		   organizational_project
			 date_item = organizational_project
			 date_item &lt;- applied_date_assignment.items[i]
			 applied_date_assignment &lt;=
			 date_assignment
			 {date_assignment.role -&gt; date_role
			  date_role.name = 'planned start'}
			 date_assignment.assigned_date -&gt; date
			 date =&gt; calendar_date
		 </refpath>
     </aa>
     <aa attribute="planned_start_date" assertion_to="Date_time">
       <aimelt>PATH</aimelt>
		 <refpath>
		   organizational_project
			 date_and_time_item = organizational_project
			 date_and_time_item &lt;- applied_date_and_time_assignment.items[i]
			 applied_date_and_time_assignment &lt;=
			 date_and_time_assignment
			 {date_and_time_assignment.role -&gt; date_time_role
			  date_time_role.name = 'planned start'}
			 date_and_time_assignment.assigned_date_and_time 
			 -&gt; date_and_time
		 </refpath>
		 </aa>
     <aa attribute="planned_start_date" assertion_to="Event">
       <aimelt>PATH</aimelt>
		 <refpath>
		   organizational_project
			 event_occurrence_item = organizational_project
			 event_occurrence_item &lt;- applied_event_occurrence_assignment.items[i]
			 applied_event_occurrence_assignment &lt;= event_occurrence_assignment
			 event_occurrence_assignment.role -&gt; event_occurrence_role
			 {event_occurrence_role.name='planned start'}
		 </refpath>
		 </aa>
     
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// OP <- ADA -> CD
	// OP <- ADATA -> DAT
	// OP <- AEOA -> EOR
	public static void setPlanned_xxx_date(SdaiContext context,
			EProject armEntity, String name, EEntity date) throws SdaiException {
			if(date instanceof ECalendar_date){
				ECalendar_date ecd = (ECalendar_date)date;
				// Role
	       	LangUtils.Attribute_and_value_structure[] structure = {
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CDate_role.attributeName(null), 
	   			   	name)
	   			   };
	       	EDate_role role = (EDate_role)  
	   		   LangUtils.createInstanceIfNeeded(context, CDate_role.definition, structure);
				// ADA
	       	LangUtils.Attribute_and_value_structure[] relStructure = {
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CApplied_date_assignment.attributeRole(null), 
	   			   	role),
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CApplied_date_assignment.attributeAssigned_date(null), 
	   			   	ecd)
	   			   };
	       	EApplied_date_assignment eada = (EApplied_date_assignment)  
	   		   LangUtils.createInstanceIfNeeded(context, CApplied_date_assignment.definition, relStructure);
	       	ADate_item items;
	       	if(eada.testItems(null))
	       		items = eada.getItems(null);
	       	else
	       		items = eada.createItems(null);
	       	if(!items.isMember(armEntity))
	       		items.addUnordered(armEntity);
			}
			else if(date instanceof EDate_and_time){
				EDate_and_time edat = (EDate_and_time)date;
				// Role
	       	LangUtils.Attribute_and_value_structure[] structure = {
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CDate_time_role.attributeName(null), 
	   			   	name)
	   			   };
	       	EDate_time_role role = (EDate_time_role)  
	   		   LangUtils.createInstanceIfNeeded(context, CDate_time_role.definition, structure);
				// ADA
	       	LangUtils.Attribute_and_value_structure[] relStructure = {
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CApplied_date_and_time_assignment.attributeRole(null), 
	   			   	role),
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CApplied_date_and_time_assignment.attributeAssigned_date_and_time(null), 
	   			   	edat)
	   			   };
	       	EApplied_date_and_time_assignment eadata = (EApplied_date_and_time_assignment)  
	   		   LangUtils.createInstanceIfNeeded(context, CApplied_date_and_time_assignment.definition, relStructure);
	       	ADate_and_time_item items;
	       	if(eadata.testItems(null))
	       		items = eadata.getItems(null);
	       	else
	       		items = eadata.createItems(null);
	       	if(!items.isMember(armEntity))
	       		items.addUnordered(armEntity);
			}
			else if(date instanceof EEvent){
				EEvent event = (EEvent)date;
				// Role
	       	LangUtils.Attribute_and_value_structure[] structure = {
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CEvent_occurrence_role.attributeName(null), 
	   			   	name)
	   			   };
	       	EEvent_occurrence_role role = (EEvent_occurrence_role)  
	   		   LangUtils.createInstanceIfNeeded(context, CEvent_occurrence_role.definition, structure);
				// ADA
	       	LangUtils.Attribute_and_value_structure[] relStructure = {
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CApplied_event_occurrence_assignment.attributeRole(null), 
	   			   	role),
	   			   new LangUtils.Attribute_and_value_structure(
	   			   	CApplied_event_occurrence_assignment.attributeAssigned_event_occurrence(null), 
	   			   	event)
	   			   };
	       	EApplied_event_occurrence_assignment eaeoa = (EApplied_event_occurrence_assignment)  
	   		   LangUtils.createInstanceIfNeeded(context, CApplied_event_occurrence_assignment.definition, relStructure);
	       	AEvent_occurrence_item items;
	       	if(eaeoa.testItems(null))
	       		items = eaeoa.getItems(null);
	       	else
	       		items = eaeoa.createItems(null);
	       	if(!items.isMember(armEntity))
	       		items.addUnordered(armEntity);
			}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetPlanned_xxx_date(SdaiContext context,
			EProject armEntity, String name) throws SdaiException {
		// Need to clean all 3 cases
		// 1) Calendar_date
		AApplied_date_assignment aada = new AApplied_date_assignment();
		CApplied_date_assignment.usedinItems(null, armEntity, context.domain, aada);
		SdaiIterator iterAADA = aada.createIterator();
		while(iterAADA.next()){
			EApplied_date_assignment eada = aada.getCurrentMember(iterAADA);
			if((eada.testRole(null))&&(eada.getRole(null).getName(null).equals(name)))
				eada.deleteApplicationInstance();
		}
		// 2) Date_and_time
		AApplied_date_and_time_assignment aadata = new AApplied_date_and_time_assignment();
		CApplied_date_and_time_assignment.usedinItems(null, armEntity, context.domain, aadata);
		SdaiIterator iterAADATA = aadata.createIterator();
		while(iterAADATA.next()){
			EApplied_date_and_time_assignment eadata = aadata.getCurrentMember(iterAADATA);
			if((eadata.testRole(null))&&(eadata.getRole(null).getName(null).equals(name)))
				eadata.deleteApplicationInstance();
		}
		// 3) Event
		AApplied_event_occurrence_assignment aaeoa = new AApplied_event_occurrence_assignment();
		CApplied_event_occurrence_assignment.usedinItems(null, armEntity, context.domain, aaeoa);
		SdaiIterator iterAAEOA = aaeoa.createIterator();
		while(iterAAEOA.next()){
			EApplied_event_occurrence_assignment eaeoa = aaeoa.getCurrentMember(iterAAEOA);
			if((eaeoa.testRole(null))&&(eaeoa.getRole(null).getName(null).equals(name)))
				eaeoa.deleteApplicationInstance();
		}
		
	}
	
}