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

package jsdai.SDocument_assignment_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SDocument_and_version_identification_xim.*;
import jsdai.SDocument_assignment_mim.*;
import jsdai.SDocument_definition_xim.*;
import jsdai.SDocument_schema.*;
import jsdai.SFile_identification_xim.EFile;
import jsdai.SManagement_resources_schema.CDocument_usage_role;
import jsdai.SManagement_resources_schema.EDocument_usage_constraint_assignment;
import jsdai.SManagement_resources_schema.EDocument_usage_role;
import jsdai.SProduct_definition_schema.EProduct;
import jsdai.SProduct_definition_schema.EProduct_definition_formation;

/**
* @author Giedrius Liutkus
* @version $Revision$
*/

public class CxPartial_document_assignment extends CPartial_document_assignment implements EMappedXIMEntity
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
		a0 = set_instance(a0, value);
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
		a1 = set_instance(a1, value);
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

			setTemp("AIM", CApplied_document_usage_constraint_assignment.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

	        // assigned_document : assigned_document_select;
	        // document_portion : STRING
			setAssigned_document_portion(context, this);

			// role_x : STRING;
			setRole_x(context, this);

			// is_assigned_to : documented_element_select;
			setIs_assigned_to(context, this);
			
			// Clean ARM
            // assigned_document : assigned_document_select;
			unsetAssigned_document(null);

			// role_x : STRING;
			unsetRole_x(null);

			// is_assigned_to : documented_element_select;
			unsetIs_assigned_to(null);
			
	        // document_portion : STRING
			unsetDocument_portion(null);
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
	public static void setMappingConstraints(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
	}

	public static void unsetMappingConstraints(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
	}
	
	/**
	* Sets/creates data for Assigned_document_armx attribute.
	*
	* <p>
	attribute_mapping assigned_document(assigned_document, ($PATH)($PATH), Document_armx);
		applied_document_usage_constraint_assignment <=
		document_usage_constraint_assignment
		document_usage_constraint_assignment.assigned_document_usage ->
		document_usage_constraint
		document_usage_constraint.source ->
		document <-
		{document.kind -> document_type
		document_type.product_data_type = 'configuration controlled document'}
		document_product_association.relating_document
		document_product_association
		{document_product_association =>
		document_product_equivalence}
		document_product_association.related_product ->
		product_or_formation_or_definition
		product_or_formation_or_definition = product
		{product <- product_related_product_category.products
		product_related_product_category <= product_category
		product_category.name='document'}
	end_attribute_mapping;

	attribute_mapping assigned_document(assigned_document, $PATH, Document_version);
		applied_document_usage_constraint_assignment <=
		document_usage_constraint_assignment
		document_usage_constraint_assignment.assigned_document_usage ->
		document_usage_constraint
		document_usage_constraint.source ->
		document <-
		{document.kind -> document_type
		document_type.product_data_type = 'configuration controlled document version'}
		document_product_association.relating_document
		document_product_association
		{document_product_association => document_product_equivalence}
		document_product_association.related_product ->
		product_or_formation_or_definition
		product_or_formation_or_definition = product_definition_formation
		{product_definition_formation.of_product -> product
		product <- product_related_product_category.products
		product_related_product_category <= product_category
		product_category.name='document'}
	end_attribute_mapping;

	attribute_mapping assigned_document(assigned_document, ($PATH)($PATH), Document_definition);
		applied_document_usage_constraint_assignment <=
		document_usage_constraint_assignment
		document_usage_constraint_assignment.assigned_document_usage ->
		document_usage_constraint
		document_usage_constraint.source ->
		document <-
		{document.kind -> document_type
		document_type.product_data_type = 'configuration controlled document definition'}
		document_product_association.relating_document
		document_product_association
		{document_product_association => document_product_equivalence}
		document_product_association.related_product ->
		product_or_formation_or_definition
		product_or_formation_or_definition = product_definition
		{product_definition.frame_of_reference ->
		product_definition_context <=
		application_context_element
		(application_context_element.name = 'physical document definition')
		(application_context_element.name = 'digital document definition')}
		{product_definition.formation -> product_definition_formation
		product_definition_formation.of_product -> product
		product <- product_related_product_category.products
		product_related_product_category <= product_category
		product_category.name='document'}
	end_attribute_mapping;

	attribute_mapping assigned_document(assigned_document, ($PATH)($PATH), File);
		applied_document_usage_constraint_assignment <=
		document_usage_constraint_assignment
		document_usage_constraint_assignment.assigned_document_usage ->
		document_usage_constraint
		document_usage_constraint.source ->
		document
		{document <- document_representation_type.represented_document
		(document_representation_type.name = 'digital')
		(document_representation_type.name = 'physical')}
		document => document_file 
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
    // ADUCA.adu -> DUC -> D
	// Method doing 2 jobs at a time - setting assigned_document and document_portion 
	public static void setAssigned_document_portion(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetAssigned_document_portion(context, armEntity);

		if (armEntity.testAssigned_document(null))
		{
			EEntity ee = armEntity.getAssigned_document(null);
			
			String name = null;
			String docID = null;
			EDocument document = null;
			if(ee instanceof EDocument_definition){
				name = "configuration controlled document definition";
				EDocument_definition edd = (EDocument_definition)ee;
				EProduct_definition_formation version = edd.getFormation(null);
				EProduct product = version.getOf_product(null);
				docID = new String();
				if(product.testId(null)){
					docID += product.getId(null);
				}
				if(version.testId(null)){
					docID += "-"+version.getId(null);
				}
				if(edd.testId(null)){
					docID += "-"+edd.getId(null);
				}
			}
			else if(ee instanceof EDocument_version){
				name = "configuration controlled document version";
				EProduct_definition_formation version = (EProduct_definition_formation)ee;
				EProduct product = version.getOf_product(null);
				docID = new String();
				if(product.testId(null)){
					docID += product.getId(null);
				}
				if(version.testId(null)){
					docID += "-"+version.getId(null);
				}
			}
			else if(ee instanceof EDocument_armx){
				name = "configuration controlled document";
				EDocument_armx product = (EDocument_armx)ee;
				docID = new String();
				if(product.testId(null)){
					docID += product.getId(null);
				}
			}
			else if(!(ee instanceof EFile)) 
				throw new SdaiException(SdaiException.EI_NVLD, " This kind of target for assignment is not supported "+ee);
			if(!(ee instanceof EFile)){			
				// Strategy - find any suitable kind of document_type, than create document
				// document_type
				LangUtils.Attribute_and_value_structure[] dtStructure =
					{new LangUtils.Attribute_and_value_structure(
						CDocument_type.attributeProduct_data_type(null),
						name)
					};
				EDocument_type type = (EDocument_type)
					LangUtils.createInstanceIfNeeded(context, CDocument_type.definition, dtStructure);
				// Document - it must be unique as definition of it - equivalence
				LangUtils.Attribute_and_value_structure[] dStructure =
				{new LangUtils.Attribute_and_value_structure(
					CDocument.attributeKind(null),
					type),
				new LangUtils.Attribute_and_value_structure(
					CDocument.attributeId(null),
					docID)				
				};
				document = (EDocument)
					LangUtils.createInstanceIfNeeded(context, CDocument.definition, dStructure);
				if(!document.testName(null))
					document.setName(null, "");
			}else{
				document = (EDocument)ee;
			}
			String portion = null;
			if(armEntity.testDocument_portion(null)){
				portion = armEntity.getDocument_portion(null);
			}
			// DUC
			EDocument_usage_constraint educ;
			if(portion == null){
				LangUtils.Attribute_and_value_structure[] ducStructure =
				{new LangUtils.Attribute_and_value_structure(
					CDocument_usage_constraint.attributeSource(null),
					document)
				};
				educ = (EDocument_usage_constraint)
					LangUtils.createInstanceIfNeeded(context, CDocument_usage_constraint.definition, ducStructure);
				if(!educ.testSubject_element(null))
					educ.setSubject_element(null, "");
				if(!educ.testSubject_element_value(null))
					educ.setSubject_element_value(null, "");
			}else{
				LangUtils.Attribute_and_value_structure[] ducStructure =
				{new LangUtils.Attribute_and_value_structure(
					CDocument_usage_constraint.attributeSource(null),
					document),
				 new LangUtils.Attribute_and_value_structure(
					CDocument_usage_constraint.attributeSubject_element(null),
					portion),
				 new LangUtils.Attribute_and_value_structure(
					CDocument_usage_constraint.attributeSubject_element_value(null),
					portion)					
				};
				educ = (EDocument_usage_constraint)
					LangUtils.createInstanceIfNeeded(context, CDocument_usage_constraint.definition, ducStructure);
			}
			
			armEntity.setAssigned_document_usage(null, educ);
		}
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
		// xxx
		armEntity.unsetAssigned_document_usage(null);
	}

	/**
	* Sets/creates data for role_x attribute.
	*
	* <p>
	attribute_mapping role_x(role_x, document_usage_role.name);
		applied_document_usage_constraint_assignment <=
		document_usage_constraint_assignment
		document_usage_constraint_assignment.role ->
		document_usage_role
		document_usage_role.name
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRole_x(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetRole_x(context, armEntity);

		if (armEntity.testRole_x(null))
		{

			String name = armEntity.getRole_x(null);
			LangUtils.Attribute_and_value_structure[] edurStructure =
				{new LangUtils.Attribute_and_value_structure(
					CDocument_usage_role.attributeName(null),
					name)
				};
			EDocument_usage_role edur = (EDocument_usage_role)
				LangUtils.createInstanceIfNeeded(context, CDocument_usage_role.definition, edurStructure);
			armEntity.setRole(null, edur);
		}
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
		armEntity.unsetRole(null);
	}
	
	/**
	* Sets/creates data for is_assigned_to attribute.
	*
	* <p>
	attribute_mapping is_assigned_to(is_assigned_to, ($PATH)($PATH), documented_element_select);
		applied_document_usage_constraint_assignment
		applied_document_usage_constraint_assignment.items[i] ->
		document_reference_item
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setIs_assigned_to(SdaiContext context, EPartial_document_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetIs_assigned_to(context, armEntity);

		if (armEntity.testIs_assigned_to(null))
		{
			EEntity ee = armEntity.getIs_assigned_to(null);
			armEntity.createItems(null).addUnordered(ee);
		}
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
		armEntity.unsetItems(null);
	}

}
