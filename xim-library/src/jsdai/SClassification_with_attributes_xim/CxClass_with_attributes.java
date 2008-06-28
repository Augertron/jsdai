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

package jsdai.SClassification_with_attributes_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SClass_xim.CxClass_armx;
import jsdai.SClass_xim.EClass_armx;
import jsdai.SClassification_assignment_mim.*;
import jsdai.SClassification_with_attributes_mim.CCharacterized_class;
import jsdai.SClassification_with_attributes_mim.EClass_system;
import jsdai.SIdentification_assignment_mim.*;
import jsdai.SManagement_resources_schema.*;

public class CxClass_with_attributes extends CClass_with_attributes implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CCharacterized_class.definition);

		setMappingConstraints(context, this);

		// version_id
		setVersion_id(context, this);
		
		// used_classification_system
		setUsed_classification_system(context, this);
		
		setId_x(context, this);
		
		// clean ARM
		// version_id
		unsetVersion_id(null);
		
		// used_classification_system
		unsetUsed_classification_system(null);
	
		unsetId_x(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// version_id
		unsetVersion_id(context, this);
		
		// used_classification_system
		unsetUsed_classification_system(context, this);
		
		unsetId_x(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	(characterized_class)
	*  (/SUBTYPE(External_class_with_attributes)/)
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EClass_with_attributes armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EClass_with_attributes armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for attribute version_id.
	*
	* <p>
	* mapping_constraints;
	* 	<aa attribute="version_id">
		<aimelt>identification_assignment.assigned_id</aimelt>
			<source>ISO 10303-1021</source>
				<refpath>
					class = identification_item
					identification_item &lt;- applied_identification_assignment.items[i]
					applied_identification_assignment &lt;= identification_assignment
					{identification_assignment.role -&gt; identification_role
					identification_role.name = 'version'}
					identification_assignment.assigned_id
			    </refpath>
			</aa>
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// C <- AIA.
	public static void setVersion_id(SdaiContext context, EClass_with_attributes armEntity) throws SdaiException
	{
		unsetVersion_id(context, armEntity);
		if(armEntity.testVersion_id(null)){
			String versionID = armEntity.getVersion_id(null);
			// Role
         LangUtils.Attribute_and_value_structure[] irStructure = {
         		new LangUtils.Attribute_and_value_structure(CIdentification_role.attributeName(null), "version")
         };
         EIdentification_role role = (EIdentification_role)
         	LangUtils.createInstanceIfNeeded(context, CIdentification_role.definition, irStructure);
			// AIA
         LangUtils.Attribute_and_value_structure[] aiaStructure = {
         		new LangUtils.Attribute_and_value_structure(CApplied_identification_assignment.attributeRole(null), role),
					new LangUtils.Attribute_and_value_structure(CApplied_identification_assignment.attributeAssigned_id(null), versionID)
         };
         EApplied_identification_assignment eaia = (EApplied_identification_assignment)
         	LangUtils.createInstanceIfNeeded(context, CApplied_identification_assignment.definition, aiaStructure);
         AIdentification_item aii;
         if(eaia.testItems(null)){
         	aii = eaia.getItems(null);
         }
         else
         	aii = eaia.createItems(null);
         if(!aii.isMember(armEntity))
         	aii.addUnordered(armEntity);
		}
	}

	public static void unsetVersion_id(SdaiContext context, EClass_with_attributes armEntity) throws SdaiException
	{
		AApplied_identification_assignment aaia = new AApplied_identification_assignment();
		CApplied_identification_assignment.usedinItems(null, armEntity, context.domain, aaia);
		SdaiIterator iter = aaia.createIterator();
		while(iter.next()){
			EApplied_identification_assignment eaia = aaia.getCurrentMember(iter);
			AIdentification_item aii = eaia.getItems(null);
			aii.removeUnordered(armEntity);
			if(aii.getMemberCount() == 0)
				eaia.deleteApplicationInstance();
		}
	}

	/**
	* Sets/creates data for attribute version_id.
	*
	* <p>
	* mapping_constraints;
			<aa attribute="used_classification_system" assertion_to="Classification_system">
				<aimelt>PATH</aimelt>
				<refpath>
					characterized_class &lt;= class
					classification_item = class
					classification_item &lt;- applied_classification_assignment.items[i]
					applied_classification_assignment &lt;= classification_assignment
					{classification_assignment.role -&gt; classification_role
					classification_role.name = 'class system membership'}
					classification_assignment.assigned_class -&gt; group
					group =&gt; class_system
				</refpath>
			</aa>
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// C <- AIA -> CS
	public static void setUsed_classification_system(SdaiContext context, EClass_with_attributes armEntity) throws SdaiException
	{
		unsetVersion_id(context, armEntity);
		if(armEntity.testUsed_classification_system(null)){
			EClass_system ecs = armEntity.getUsed_classification_system(null);
			// Role
         LangUtils.Attribute_and_value_structure[] csStructure = {
         		new LangUtils.Attribute_and_value_structure(CClassification_role.attributeName(null), "class system membership")
         };
         EClassification_role role = (EClassification_role)
         	LangUtils.createInstanceIfNeeded(context, CClassification_role.definition, csStructure);
			// ACA
         LangUtils.Attribute_and_value_structure[] aiaStructure = {
         		new LangUtils.Attribute_and_value_structure(CApplied_classification_assignment.attributeRole(null), role),
					new LangUtils.Attribute_and_value_structure(CApplied_classification_assignment.attributeAssigned_class(null), ecs)
         };
         EApplied_classification_assignment eaca = (EApplied_classification_assignment)
         	LangUtils.createInstanceIfNeeded(context, CApplied_classification_assignment.definition, aiaStructure);
         AClassification_item aci;
         if(eaca.testItems(null)){
         	aci = eaca.getItems(null);
         }
         else
         	aci = eaca.createItems(null);
         if(!aci.isMember(armEntity))
         	aci.addUnordered(armEntity);
		}
	}

	public static void unsetUsed_classification_system(SdaiContext context, EClass_with_attributes armEntity) throws SdaiException
	{
		AApplied_classification_assignment aaca = new AApplied_classification_assignment();
		CApplied_identification_assignment.usedinItems(null, armEntity, context.domain, aaca);
		SdaiIterator iter = aaca.createIterator();
		while(iter.next()){
			EApplied_classification_assignment eaca = aaca.getCurrentMember(iter);
			AClassification_item aci = eaca.getItems(null);
			aci.removeUnordered(armEntity);
			if(aci.getMemberCount() == 0)
				eaca.deleteApplicationInstance();
		}
	}

// Class_armx
	public static void setId_x(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
		CxClass_armx.setId_x(context, armEntity);
	}

	public static void unsetId_x(SdaiContext context, EClass_armx armEntity) throws SdaiException
	{
		CxClass_armx.unsetId_x(context, armEntity);
	}

	
	
}
