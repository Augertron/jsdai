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

package jsdai.SRequirement_assignment_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGroup_schema.EGroup;
import jsdai.SManagement_resources_schema.*;
import jsdai.SProduct_definition_schema.AProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SRequirement_assignment_mim.*;
import jsdai.SRequirement_view_definition_xim.ERequirement_view_definition;

public class CxRequirement_assignment_armx extends CRequirement_assignment_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Requirement_assignment - group
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SGroup_schema.EGroup type) throws SdaiException {
		return test_string(a2);
	}
	public String getName(jsdai.SGroup_schema.EGroup type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setName(jsdai.SGroup_schema.EGroup type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetName(jsdai.SGroup_schema.EGroup type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SGroup_schema.EGroup type) throws SdaiException {
		return a2$;
	}
	
	// ENDOF Taken from Requirement_assignment - group
	
	// Taken from Characterized_object	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ECharacterized_object type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ECharacterized_object type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ECharacterized_object type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ECharacterized_object type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ECharacterized_object type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(ECharacterized_object type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(ECharacterized_object type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(ECharacterized_object type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(ECharacterized_object type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(ECharacterized_object type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from Characterized_object
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CRequirement_assignment.definition);

		setMappingConstraints(context, this);
		
		// assigned_requirement
		setAssigned_requirement(context, this);
		
      // assigned_to 
		setAssigned_to(context, this);
		
		// id_x
		setId_x(context, this);
		
		// Clean ARM
		// assigned_requirement
		unsetAssigned_requirement(null);
		
      // assigned_to 
		unsetAssigned_to(null);

		// id_x
		unsetId_x(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// assigned_requirement
		unsetAssigned_requirement(context, this);
		
      // assigned_to 
		unsetAssigned_to(context, this);
		
		// id_x
		unsetId_x(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	requirement_assignment &lt;=
	*  [group]
	*  [characterized_object]
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gaps
//		if(!armEntity.testName((ECharacterized_object)null))
			armEntity.setName((ECharacterized_object)null, "RA");

//		if(!armEntity.testName((EGroup)null))
			armEntity.setName((EGroup)null, "");
		
	}

	public static void unsetMappingConstraints(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
	}
	
	// Requirement_assignment_armx attributes
	
	/* Sets/creates data for Assigned_requirement attribute.
	*
	* <p>
			<aa attribute="assigned_requirement" assertion_to="Requirement_view_definition">
				<aimelt>PATH</aimelt>
				<refpath>
					requirement_assignment &lt;-
					assigned_requirement.assigned_group
					assigned_requirement
					assigned_requirement.items -&gt;
					product_definition
				</refpath>
			</aa>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 
	public static void setAssigned_requirement(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetAssigned_requirement(context, armEntity);

		if (armEntity.testAssigned_requirement(null))
		{
			ERequirement_view_definition requirement = armEntity.getAssigned_requirement(null);
			EAssigned_requirement ear = (EAssigned_requirement)context.working_model.createEntityInstance(CAssigned_requirement.definition);
			ear.setAssigned_group(null, armEntity);
			ear.createItems(null).addUnordered(requirement);
		}
	}


	/**
	* Unsets/deletes data for sub_assembly_reference_designation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssigned_requirement(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
		
      AGroup_assignment aga = new AGroup_assignment();
      CGroup_assignment.usedinAssigned_group(null, armEntity, context.domain, aga);
      SdaiIterator iter = aga.createIterator();
      while(iter.next()){
      	EGroup_assignment ega = aga.getCurrentMember(iter);
      	if(ega instanceof EAssigned_requirement){
         	EAssigned_requirement ear = (EAssigned_requirement)ega;
         	if(!ear.testItems(null))
         		continue;
         	AProduct_definition items = ear.getItems(null);
         	if((items.getMemberCount() >=1 )&&(items.getByIndex(1) instanceof ERequirement_view_definition)){
         		ear.deleteApplicationInstance();
         	}
      		
      	}
      }
	}
	
	/* Sets/creates data for Assigned_to attribute.
	*
	* <p>
			<aa attribute="assigned_to" assertion_to="requirement_assignment_item">
				<aimelt>PATH</aimelt>
				<refpath>
					requirement_assignment &lt;-
					requirement_assigned_object.assigned_group
					requirement_assigned_object
					requirement_assigned_object.items -&gt;
					requirement_assigned_item
				</refpath>
			</aa>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 
	public static void setAssigned_to(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetAssigned_to(context, armEntity);

		if (armEntity.testAssigned_to(null))
		{
			EEntity item = armEntity.getAssigned_to(null);
			ERequirement_assigned_object erao = (ERequirement_assigned_object)context.working_model.createEntityInstance(CRequirement_assigned_object.definition);
			erao.setAssigned_group(null, armEntity);
			erao.createItems(null).addUnordered(item);
		}
	}


	/**
	* Unsets/deletes data for sub_assembly_reference_designation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssigned_to(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
      AGroup_assignment aga = new AGroup_assignment();
      CGroup_assignment.usedinAssigned_group(null, armEntity, context.domain, aga);
      SdaiIterator iter = aga.createIterator();
      while(iter.next()){
      	EGroup_assignment ega = aga.getCurrentMember(iter);
      	if(ega instanceof ERequirement_assigned_object){
      		ega.deleteApplicationInstance();
      	}
      }
	}

	/* Sets/creates data for Assigned_requirement attribute.
	*
	* <p>
			<aa attribute="assigned_requirement" assertion_to="Requirement_view_definition">
				<aimelt>PATH</aimelt>
				<refpath>
					requirement_assignment &lt;-
					assigned_requirement.assigned_group
					assigned_requirement
					assigned_requirement.items -&gt;
					product_definition
				</refpath>
			</aa>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 
	public static void setId_x(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetId_x(context, armEntity);

		if (armEntity.testId_x(null))
		{
			String id = armEntity.getId_x(null);
			CxAP210ARMUtilities.setGroupId(context, armEntity, id);
		}
	}


	/**
	* Unsets/deletes data for sub_assembly_reference_designation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetId_x(SdaiContext context, ERequirement_assignment_armx armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetGroupId(context, armEntity);
	}
	
}
