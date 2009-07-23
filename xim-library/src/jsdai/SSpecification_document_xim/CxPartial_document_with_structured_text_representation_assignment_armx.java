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

package jsdai.SSpecification_document_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SDocument_assignment_mim.*;
import jsdai.SDocument_assignment_xim.CxPartial_document_assignment;
import jsdai.SDocument_assignment_xim.EPartial_document_assignment;
import jsdai.SManagement_resources_schema.EDocument_usage_constraint_assignment;
import jsdai.SManagement_resources_schema.EDocument_usage_role;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_representation_schema.AItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.CItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.EItem_identified_representation_usage;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SSpecification_document_mim.CPartial_document_with_structured_text_representation_assignment;

/**
* @author Giedrius Liutkus
* @version $Revision$
*/

public class CxPartial_document_with_structured_text_representation_assignment_armx extends CPartial_document_with_structured_text_representation_assignment_armx implements EMappedXIMEntity
{

	
	// Taken from Document_usage_constraint_assignment
	// attribute (current explicit or supertype explicit) : assigned_document_usage, base type: entity document_usage_constraint
/*	public static int usedinAssigned_document_usage(EDocument_usage_constraint_assignment type, jsdai.SDocument_schema.EDocument_usage_constraint instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testAssigned_document_usage(EDocument_usage_constraint_assignment type) throws SdaiException {
		return test_instance(a0);
	}
	public jsdai.SDocument_schema.EDocument_usage_constraint getAssigned_document_usage(EDocument_usage_constraint_assignment type) throws SdaiException {
		return (jsdai.SDocument_schema.EDocument_usage_constraint)get_instance(a0);
	}*/
	public void setAssigned_document_usage(EDocument_usage_constraint_assignment type, jsdai.SDocument_schema.EDocument_usage_constraint value) throws SdaiException {
		a0 = set_instanceX(a0, value);
	}
	public void unsetAssigned_document_usage(EDocument_usage_constraint_assignment type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeAssigned_document_usage(EDocument_usage_constraint_assignment type) throws SdaiException {
		return a0$;
	}
	
	// attribute (current explicit or supertype explicit) : role, base type: entity document_usage_role
/*	public static int usedinRole(EDocument_usage_constraint_assignment type, EDocument_usage_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRole(EDocument_usage_constraint_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public EDocument_usage_role getRole(EDocument_usage_constraint_assignment type) throws SdaiException {
		return (EDocument_usage_role)get_instance(a1);
	}*/
	public void setRole(EDocument_usage_constraint_assignment type, EDocument_usage_role value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetRole(EDocument_usage_constraint_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EDocument_usage_constraint_assignment type) throws SdaiException {
		return a1$;
	}
	// END OF taken from Document_usage_constraint_assignment
   
   // Taken from Applied_document_usage_constraint_assignment
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EApplied_document_usage_constraint_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testItems(EApplied_document_usage_constraint_assignment type) throws SdaiException {
		return test_aggregate(a2);
	}
	public ADocument_reference_item getItems(EApplied_document_usage_constraint_assignment type) throws SdaiException {
		if (a2 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a2;
	}*/
	public ADocument_reference_item createItems(EApplied_document_usage_constraint_assignment type) throws SdaiException {
		a2 = (ADocument_reference_item)create_aggregate_class(a2, a2$, ADocument_reference_item.class, 0);
		return a2;
	}
	public void unsetItems(EApplied_document_usage_constraint_assignment type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EApplied_document_usage_constraint_assignment type) throws SdaiException {
		return a2$;
	}
   // END OF taken from Applied_document_usage_constraint_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CPartial_document_with_structured_text_representation_assignment.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

	        // assigned_document : assigned_document_select;
	        // document_portion : STRING
			setAssigned_document_portion(context, this);

			// role_x : STRING;
			setRole_x(context, this);

			// is_assigned_to : documented_element_select;
			setIs_assigned_to(context, this);
			
			// structured_portion
			setStructured_portion(context, this);
			
			// Clean ARM
            // assigned_document : assigned_document_select;
			unsetAssigned_document(null);

			// role_x : STRING;
			unsetRole_x(null);

			// is_assigned_to : documented_element_select;
			unsetIs_assigned_to(null);
			
	        // document_portion : STRING
			unsetDocument_portion(null);
			
			unsetStructured_portion(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

        // assigned_document : assigned_document_select;
        // document_portion : STRING
		unsetAssigned_document_portion(context, this);

		// role_x : STRING;
		unsetRole_x(context, this);

		// is_assigned_to : documented_element_select;
		unsetIs_assigned_to(context, this);
		
		// structured_portion
		unsetStructured_portion(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	* mapping_constraints;
		applied_document_usage_constraint_assignment <=
		document_usage_constraint_assignment 
	* end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPartial_document_with_structured_text_representation_assignment_armx armEntity) throws SdaiException
	{
		CxPartial_document_assignment.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPartial_document_with_structured_text_representation_assignment_armx armEntity) throws SdaiException
	{
		CxPartial_document_assignment.unsetMappingConstraints(context, armEntity);
	}
	
	/**
	* Sets/creates data for Assigned_document_armx attribute.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// Method doing 2 jobs at a time - setting assigned_document and document_portion 
	public static void setAssigned_document_portion(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		CxPartial_document_assignment.setAssigned_document_portion(context, armEntity);
	}


	/**
	* Unsets/deletes data for assigned_document attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssigned_document_portion(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		CxPartial_document_assignment.unsetAssigned_document_portion(context, armEntity);
	}

	/**
	* Sets/creates data for role_x attribute.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRole_x(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		CxPartial_document_assignment.setRole_x(context, armEntity);		
	}


	/**
	* Unsets/deletes data for role_x attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> xxx, delete AGAs
	public static void unsetRole_x(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		CxPartial_document_assignment.setRole_x(context, armEntity);
	}
	
	/**
	* Sets/creates data for is_assigned_to attribute.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setIs_assigned_to(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		CxPartial_document_assignment.setIs_assigned_to(context, armEntity);
	}


	/**
	* Unsets/deletes data for Is_assigned_to attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetIs_assigned_to(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		CxPartial_document_assignment.unsetIs_assigned_to(context, armEntity);
	}

	/**
	* Sets/creates data for is_assigned_to attribute.
	*
	* <p>
	attribute_mapping structured_portion(structured_portion, $PATH, string_representation_item_select);
		partial_document_with_structured_text_representation_assignment <=
		characterized_object
		characterized_definition = characterized_object
		characterized_definition <-
		property_definition.definition
		property_definition
		represented_definition = property_definition
		represented_definition <-
		item_identified_representation_usage.definition
		item_identified_representation_usage
		item_identified_representation_usage.identified_item ->
		representation_item
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PDWSTRA <- PD <- IIRU -> RI
	public static void setStructured_portion(SdaiContext context, EPartial_document_with_structured_text_representation_assignment_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetStructured_portion(context, armEntity);
		if (armEntity.testStructured_portion(null))
		{
			ERepresentation_item ee = (ERepresentation_item)armEntity.getStructured_portion(null);
			// <- PD
			LangUtils.Attribute_and_value_structure[] epdStructure =
			{new LangUtils.Attribute_and_value_structure(
				CProperty_definition.attributeDefinition(null),
				armEntity)
			};
			EProperty_definition epd = (EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, epdStructure);
			if(!epd.testName(null)){
				epd.setName(null, "");
			}
			// IIRU
			EItem_identified_representation_usage eiiru = (EItem_identified_representation_usage)
				context.working_model.createEntityInstance(CItem_identified_representation_usage.definition);
			eiiru.setDefinition(null, epd);
			eiiru.setIdentified_item(null, ee);
			eiiru.setName(null, "");
			// Filling AIM gap - find representation
			ARepresentation ar = new ARepresentation();
			CRepresentation.usedinItems(null, ee, context.domain, ar);
			// Exception here mean XIM data violates WR1 of rep_item
			if(ar.getMemberCount() == 0){
				ERepresentation er = CxAP210ARMUtilities.createRepresentation(context, "", true);
				ARepresentation_item items;
				if(er.testItems(null)){
					items = er.getItems(null);
				}else{
					items = er.createItems(null);
				}
				items.addUnordered(ee);
			}else{
				eiiru.setUsed_representation(null, ar.getByIndex(1));
			}
		}
	}


	/**
	* Unsets/deletes data for Is_assigned_to attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStructured_portion(SdaiContext context, EPartial_document_with_structured_text_representation_assignment_armx armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterAPD = apd.createIterator();
		while(iterAPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterAPD);
			AItem_identified_representation_usage aiiru = new AItem_identified_representation_usage();
			CItem_identified_representation_usage.usedinDefinition(null, epd, context.domain, aiiru);
			SdaiIterator iterIIRU = aiiru.createIterator();
			while(iterIIRU.next()){
				EItem_identified_representation_usage eiiru = aiiru.getCurrentMember(iterIIRU);
				eiiru.deleteApplicationInstance();
			}			
		}
	}
	
}
