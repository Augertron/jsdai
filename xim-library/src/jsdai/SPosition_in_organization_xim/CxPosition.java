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

package jsdai.SPosition_in_organization_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SManagement_resources_schema.CPosition_in_organization_role;
import jsdai.SManagement_resources_schema.EPosition_in_organization_role;
import jsdai.SPerson_organization_schema.CPosition_in_organization;
import jsdai.SPerson_organization_schema.EAddress;
import jsdai.SPerson_organization_schema.EOrganization;
import jsdai.SPerson_organization_schema.EOrganizational_project;
import jsdai.SPosition_in_organization_mim.AApplied_position_in_organization_assignment;
import jsdai.SPosition_in_organization_mim.APosition_in_organization_item;
import jsdai.SPosition_in_organization_mim.CApplied_position_in_organization_assignment;
import jsdai.SPosition_in_organization_mim.EApplied_position_in_organization_assignment;

public class CxPosition extends CPosition implements EMappedXIMEntity
{

	// Taken from CProduct
	// ENDOF taken from CProduct
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPosition_in_organization.definition);

		setMappingConstraints(context, this);

		setAddress(context, this);
		setPosition_context(context, this);

		unsetAddress(null);
		unsetPosition_context(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);

		unsetAddress(context, this);
		unsetPosition_context(context, this);
		
	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	product
	*  {product &lt;-
	*  product_related_product_category.products[i]
	*  product_related_product_category &lt;=
	*  product_category
	*  product_category.name='breakdown element'}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPosition armEntity) throws SdaiException
	{
	}

	public static void unsetMappingConstraints(SdaiContext context, EPosition armEntity) throws SdaiException
	{
	}	

	/**
	* Sets/creates data for Address.
	*
	* <p>
	attribute_mapping address(address, $PATH, Address);
		position_in_organization <-
		position_in_organization_assignment.assigned_position_in_organization
		position_in_organization_assignment =>
		applied_position_in_organization_assignment
		applied_position_in_organization_assignment.items[i] ->
		position_in_organization_item
		position_in_organization_item =
		address
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PIO <- APIOA -> A 
	public static void setAddress(SdaiContext context, EPosition armEntity) throws SdaiException
	{
		unsetAddress(context, armEntity);
		if(armEntity.testAddress(null)){
			EAddress address = armEntity.getAddress(null);
			LangUtils.Attribute_and_value_structure[] assignmentStructure = {new LangUtils.Attribute_and_value_structure(
					CApplied_position_in_organization_assignment.attributeAssigned_position_in_organization(null), armEntity)};
			EApplied_position_in_organization_assignment eapioa = (EApplied_position_in_organization_assignment)
				LangUtils.createInstanceIfNeeded(context, CApplied_position_in_organization_assignment.definition,
					assignmentStructure);

			APosition_in_organization_item items;
			if(eapioa.testItems(null)){
				items = eapioa.getItems(null);
			}else{
				items = eapioa.createItems(null);
			}
			fillGaps4APIOA(context, eapioa);			
			items.addUnordered(address);

		}
		
	}	

	public static void unsetAddress(SdaiContext context, EPosition armEntity) throws SdaiException
	{
		AApplied_position_in_organization_assignment aapioa = new AApplied_position_in_organization_assignment();
		CApplied_position_in_organization_assignment.usedinAssigned_position_in_organization(null, armEntity, context.domain, aapioa);
		for(int i=1;i <= aapioa.getMemberCount();){
			EApplied_position_in_organization_assignment eapioa = aapioa.getByIndex(i);
			if(eapioa.testItems(null)){
				APosition_in_organization_item items = eapioa.getItems(null);
				if((items.getMemberCount() == 1)&&(items.getByIndex(1) instanceof EAddress)){
					eapioa.deleteApplicationInstance();
					continue;
				}else{
					for(int j=1;j <= items.getMemberCount();){
						EEntity item = items.getByIndex(j);
						if(item instanceof EAddress){
							items.removeByIndex(j);
						}else{
							j++;
						}
					}
					if(items.getMemberCount() == 0){
						eapioa.deleteApplicationInstance();
						continue;
					}
				}
				i++;
			}
		}
		
	}	

	/**
	* Sets/creates data for Position_context.
	*
	* <p>
		attribute_mapping position_context(position_context, $PATH, position_context_item);
            position_in_organization <-
            position_in_organization_assignment.assigned_position_in_organization
            position_in_organization_assignment =>
            applied_position_in_organization_assignment
            applied_position_in_organization_assignment.items[i] ->
            position_in_organization_item
            position_in_organization_item 
		end_attribute_mapping;
		attribute_mapping position_context(position_context, $PATH, Organization);
			position_in_organization <-
			position_in_organization_assignment.assigned_position_in_organization
			position_in_organization_assignment =>
			applied_position_in_organization_assignment
			applied_position_in_organization_assignment.items[i] ->
			position_in_organization_item
			position_in_organization_item =
			organization
		end_attribute_mapping;
		attribute_mapping position_context(position_context, $PATH, Project);
			position_in_organization <-
			position_in_organization_assignment.assigned_position_in_organization
			position_in_organization_assignment =>
			applied_position_in_organization_assignment
			applied_position_in_organization_assignment.items[i] ->
			position_in_organization_item
			position_in_organization_item =
			organizational_project
		end_attribute_mapping;
		attribute_mapping position_context(position_context, $PATH, Position_group);
			position_in_organization <-
			position_in_organization_assignment.assigned_position_in_organization
			position_in_organization_assignment
			{position_in_organization_assignment.name='position in organization context'}
			position_in_organization_assignment =>
			applied_position_in_organization_assignment
			applied_position_in_organization_assignment.items[i] ->
			position_in_organization_item
			position_in_organization_item =
			group
			{group.description='position group'}
		end_attribute_mapping;	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PIO <- APIOA -> X 
	public static void setPosition_context(SdaiContext context, EPosition armEntity) throws SdaiException
	{
		unsetPosition_context(context, armEntity);
		if(armEntity.testPosition_context(null)){
			EEntity positionContext = armEntity.getPosition_context(null);
			
			LangUtils.Attribute_and_value_structure[] assignmentStructure = {new LangUtils.Attribute_and_value_structure(
					CApplied_position_in_organization_assignment.attributeAssigned_position_in_organization(null), armEntity)};
			EApplied_position_in_organization_assignment eapioa = (EApplied_position_in_organization_assignment)
				LangUtils.createInstanceIfNeeded(context, CApplied_position_in_organization_assignment.definition,
					assignmentStructure);

			APosition_in_organization_item items;
			if(eapioa.testItems(null)){
				items = eapioa.getItems(null);
			}else{
				items = eapioa.createItems(null);
			}
			if(positionContext instanceof EPosition_group){
				eapioa.setName(null, "position in organization context");
			}
			fillGaps4APIOA(context, eapioa);
			
			items.addUnordered(positionContext);
		}
		
	}

	/**
	 * @param context
	 * @param eapioa
	 * @throws SdaiException
	 */
	private static void fillGaps4APIOA(SdaiContext context, EApplied_position_in_organization_assignment eapioa) throws SdaiException {
		if(!eapioa.testId(null)){
			eapioa.setId(null, "");
		}
		if(!eapioa.testName(null)){
			eapioa.setName(null, "");
		}
		if(!eapioa.testRole(null)){
			LangUtils.Attribute_and_value_structure[] roleStructure = {
				new LangUtils.Attribute_and_value_structure(
					CPosition_in_organization_role.attributeId(null), ""),
				new LangUtils.Attribute_and_value_structure(
					CPosition_in_organization_role.attributeName(null), "")};
			EPosition_in_organization_role role = (EPosition_in_organization_role)
				LangUtils.createInstanceIfNeeded(context, CPosition_in_organization_role.definition,
					roleStructure);
			eapioa.setRole(null, role);
		}
	}	

	public static void unsetPosition_context(SdaiContext context, EPosition armEntity) throws SdaiException
	{
		AApplied_position_in_organization_assignment aapioa = new AApplied_position_in_organization_assignment();
		CApplied_position_in_organization_assignment.usedinAssigned_position_in_organization(null, armEntity, context.domain, aapioa);
		for(int i=1;i <= aapioa.getMemberCount();){
			EApplied_position_in_organization_assignment eapioa = aapioa.getByIndex(i);
			if(eapioa.testItems(null)){
				APosition_in_organization_item items = eapioa.getItems(null);
				if((items.getMemberCount() == 1)&&(items.getByIndex(1) instanceof EAddress)){
					eapioa.deleteApplicationInstance();
					continue;
				}else{
					for(int j=1;j <= items.getMemberCount();){
						EEntity item = items.getByIndex(j);
						if((item instanceof EAddress)||
							(item instanceof EPosition_group)||
							(item instanceof EOrganization)||
							(item instanceof EOrganizational_project)){
							items.removeByIndex(j);
						}else{
							j++;
						}
					}
					if(items.getMemberCount() == 0){
						eapioa.deleteApplicationInstance();
						continue;
					}
				}
				i++;
			}
		}
		
	}	
	
	
}
