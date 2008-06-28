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

package jsdai.SProduct_class_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SBasic_attribute_schema.CObject_role;
import jsdai.SBasic_attribute_schema.CRole_association;
import jsdai.SBasic_attribute_schema.EObject_role;
import jsdai.SGroup_mim.AApplied_group_assignment;
import jsdai.SGroup_mim.AGroupable_item;
import jsdai.SGroup_mim.CApplied_group_assignment;
import jsdai.SGroup_mim.EApplied_group_assignment;
import jsdai.SIdentification_assignment_mim.AApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.AIdentification_item;
import jsdai.SIdentification_assignment_mim.CApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.EApplied_identification_assignment;
import jsdai.SManagement_resources_schema.CIdentification_role;
import jsdai.SManagement_resources_schema.EIdentification_role;
import jsdai.SProduct_concept_schema.CProduct_concept_feature;

public class CxSpecification extends CSpecification implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_concept_feature.definition);

		setMappingConstraints(context, this);
		// version_id : OPTIONAL STRING;
		setVersion_id(context, this);
		
	    // category : Specification_category;
		setCategory(context, this);
		
		// version_id : OPTIONAL STRING;
		unsetVersion_id(null);
		
	    // category : Specification_category;
		unsetCategory(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// version_id : OPTIONAL STRING;
		unsetVersion_id(context, this);
		
	    // category : Specification_category;
		unsetCategory(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ESpecification armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, ESpecification armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for attribute version_id.
	*
	* <p>
	* attribute_mapping version_id(version_id, identification_assignment.assigned_id);
		 id_for_class = product_concept_feature 
		 id_for_class <* identification_item  
		 identification_item <- applied_identification_assignment.items[i] 
		 applied_identification_assignment <= identification_assignment 
		 {identification_assignment.role -> identification_role 
		 identification_role.name = 'version'} 
		 identification_assignment.assigned_id
	  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setVersion_id(SdaiContext context, ESpecification armEntity) throws SdaiException
	{
		unsetVersion_id(context, armEntity);
	       if(armEntity.testVersion_id(null)){
	    	   String value = armEntity.getVersion_id(null);
	    	   // Find role
				LangUtils.Attribute_and_value_structure[] irStructure =
				{new LangUtils.Attribute_and_value_structure(
					CIdentification_role.attributeName(null),
					"version")
				};
				EIdentification_role eir = (EIdentification_role)
					LangUtils.createInstanceIfNeeded(context, CIdentification_role.definition, irStructure);
				// AIA
				EApplied_identification_assignment eaia = (EApplied_identification_assignment)
					context.working_model.createEntityInstance(CApplied_identification_assignment.definition);
				eaia.setRole(null, eir);
				eaia.createItems(null).addUnordered(armEntity);
				eaia.setAssigned_id(null, value);
	       }
	}

	public static void unsetVersion_id(SdaiContext context, ESpecification armEntity) throws SdaiException
	{
    	AApplied_identification_assignment aaia = new AApplied_identification_assignment();
    	CApplied_identification_assignment.usedinItems(null, armEntity, context.domain, aaia);
    	for(int i=1,count=aaia.getMemberCount(); i<= count; i++){
    		EApplied_identification_assignment eaia = aaia.getByIndex(i);
    		AIdentification_item items = eaia.getItems(null);
    		items.removeUnordered(armEntity);
    		if(items.getMemberCount() == 0){
    			eaia.deleteApplicationInstance();
    		}
    	}
	}

	/**
	* Sets/creates data for attribute version_id.
	*
	* <p>
		attribute_mapping category(category, $PATH, Specification_category);
			 specification_for_category = product_concept_feature 
			 specification_for_category <* groupable_item  
			 groupable_item <- applied_group_assignment.items[i] 
			 applied_group_assignment <= group_assignment 
			 {group_assignment = role_select 
			 role_select <- role_association.item_with_role 
			 role_association.role -> object_role 
			 object_role.name = 'specification category member'} 
			 group_assignment.assigned_group -> 
			 group => product_concept_feature_category 
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PCF <- AGA {<- RA->OR} -> PCFC
	public static void setCategory(SdaiContext context, ESpecification armEntity) throws SdaiException
	{
		unsetCategory(context, armEntity);
	       if(armEntity.testCategory(null)){
	    	   ESpecification_category esc = armEntity.getCategory(null);
	    	   // Find role
				LangUtils.Attribute_and_value_structure[] orStructure =
				{new LangUtils.Attribute_and_value_structure(
					CObject_role.attributeName(null),
					"specification category member")
				};
				EObject_role eor = (EObject_role)
					LangUtils.createInstanceIfNeeded(context, CObject_role.definition, orStructure);
				// AGA
				LangUtils.Attribute_and_value_structure[] agaStructure =
				{new LangUtils.Attribute_and_value_structure(
					CApplied_group_assignment.attributeAssigned_group(null),
					esc)
				};
				EApplied_group_assignment eaga = (EApplied_group_assignment)
					LangUtils.createInstanceIfNeeded(context, CApplied_group_assignment.definition, agaStructure);
				// RA
				LangUtils.Attribute_and_value_structure[] raStructure =
				{new LangUtils.Attribute_and_value_structure(
					CRole_association.attributeItem_with_role(null),
					eaga),
				 new LangUtils.Attribute_and_value_structure(
					CRole_association.attributeRole(null),
					eor)					
				};
				LangUtils.createInstanceIfNeeded(context, CRole_association.definition, raStructure);
				// Finally wrap up with AGA
				AGroupable_item items;
				if(eaga.testItems(null)){
					items = eaga.getItems(null);
				}else{
					items = eaga.createItems(null);
				}
				items.addUnordered(armEntity);
	       }
	}

	public static void unsetCategory(SdaiContext context, ESpecification armEntity) throws SdaiException
	{
    	AApplied_group_assignment aaga = new AApplied_group_assignment();
    	CApplied_group_assignment.usedinItems(null, armEntity, context.domain, aaga);
    	for(int i=1,count=aaga.getMemberCount(); i<= count; i++){
    		EApplied_group_assignment eaga = aaga.getByIndex(i);
    		AGroupable_item items = eaga.getItems(null);
    		items.removeUnordered(armEntity);
    		if(items.getMemberCount() == 0){
    			eaga.deleteApplicationInstance();
    		}
    	}
	}
	
}
