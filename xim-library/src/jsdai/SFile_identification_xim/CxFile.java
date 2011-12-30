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

package jsdai.SFile_identification_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SDocument_schema.*;
import jsdai.SFile_identification_mim.CDocument_file;
import jsdai.SFile_identification_xim.EFile;
import jsdai.SIdentification_assignment_mim.AApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.AIdentification_item;
import jsdai.SIdentification_assignment_mim.CApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.EApplied_identification_assignment;
import jsdai.SManagement_resources_schema.CIdentification_role;
import jsdai.SManagement_resources_schema.EIdentification_role;
import jsdai.SProduct_property_definition_schema.ECharacterized_object;

/**
* @author Giedrius Liutkus
* @version $Revision$
*/

public class CxFile extends CFile implements EMappedXIMEntity
{

	// Taken from CCharacterized_object
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ECharacterized_object type) throws SdaiException {
		return test_string(a4);
	}
	public String getName(ECharacterized_object type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setName(ECharacterized_object type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(ECharacterized_object type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ECharacterized_object type) throws SdaiException {
		return a3$;
	}

	//<01> generating methods for consolidated attribute:  description
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(ECharacterized_object type) throws SdaiException {
		return test_string(a5);
	}
	public String getDescription(ECharacterized_object type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setDescription(ECharacterized_object type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(ECharacterized_object type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(ECharacterized_object type) throws SdaiException {
		return a4$;
	}
	// END OF taken from CCharacterized_object

	// Taken from CDocument	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EDocument type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EDocument type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(EDocument type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EDocument type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EDocument type) throws SdaiException {
		return a0$;
	}

	//<01> generating methods for consolidated attribute:  description
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EDocument type) throws SdaiException {
		return test_string(a2);
	}
	public String getDescription(EDocument type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setDescription(EDocument type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EDocument type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EDocument type) throws SdaiException {
		return a1$;
	}

	//<01> generating methods for consolidated attribute:  kind
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : kind, base type: entity document_type
/*	public static int usedinKind(EDocument type, EDocument_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testKind(EDocument type) throws SdaiException {
		return test_instance(a3);
	}
	public EDocument_type getKind(EDocument type) throws SdaiException {
		return (EDocument_type)get_instance(a3);
	}*/
	public void setKind(EDocument type, EDocument_type value) throws SdaiException {
		a5 = set_instanceX(a5, value);
	}
	public void unsetKind(EDocument type) throws SdaiException {
		a5 = unset_instance(a5);
	}
	public static jsdai.dictionary.EAttribute attributeKind(EDocument type) throws SdaiException {
		return a5$;
	}
	// END OF taken from CDocument	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CDocument_file.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

			// Version
			setVersion(context, this);

			// contained_data_type
			setContained_data_type(context, this);

	      // Clean ARM
			// assigned_document_armx
			unsetVersion(null);

			// role_x
			unsetContained_data_type(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// Version
		unsetVersion(context, this);

		// contained_data_type
		unsetContained_data_type(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	* mapping_constraints;
		document_file <=
		[document
		{document <- document_representation_type.represented_document
		(document_representation_type.name = 'digital')
		(document_representation_type.name = 'physical')}]
		[characterized_object
		{characterized_object.name=''}] 
	* end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// Only first alternative is really supported
	public static void setMappingConstraints(SdaiContext context, EFile armEntity) throws SdaiException
	{
		((ECharacterized_object)(armEntity)).setName(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, EFile armEntity) throws SdaiException
	{
	}
	
	/**
	* Sets/creates data for Assigned_document_armx attribute.
	*
	* <p>
	attribute_mapping version(version, identification_assignment.assigned_id);
		document_file
		identification_item = document_file
		identification_item <-
		applied_identification_assignment.items[i] 
		applied_identification_assignment <=
		identification_assignment  
		{identification_assignment.role ->
		identification_role
		identification_role.name = 'version'}
		identification_assignment.assigned_id 
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// df <- aia
	public static void setVersion(SdaiContext context, EFile armEntity) throws SdaiException
	{
		//unset old values
		unsetVersion(context, armEntity);

		if (armEntity.testVersion(null))
		{

			String version = armEntity.getVersion(null);
			// role
			LangUtils.Attribute_and_value_structure[] roleStructure =
				{new LangUtils.Attribute_and_value_structure(
					CIdentification_role.attributeName(null),
					"version")
				};
			EIdentification_role role = (EIdentification_role)
				LangUtils.createInstanceIfNeeded(context, CIdentification_role.definition, roleStructure);
			// applied_identification_assignment
			LangUtils.Attribute_and_value_structure[] aiaStructure =
			{new LangUtils.Attribute_and_value_structure(
				CApplied_identification_assignment.attributeRole(null),
				role),
			new LangUtils.Attribute_and_value_structure(
				CApplied_identification_assignment.attributeAssigned_id(null),
				version)				
			};
			EApplied_identification_assignment eaia = (EApplied_identification_assignment)
				LangUtils.createInstanceIfNeeded(context, CApplied_identification_assignment.definition, aiaStructure);
			AIdentification_item items;
			if(eaia.testItems(null)){
				items = eaia.getItems(null);
			}else{
				items = eaia.createItems(null);
			}
			items.addUnordered(armEntity);
		}
	}


	/**
	* Unsets/deletes data for basis attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetVersion(SdaiContext context, EFile armEntity) throws SdaiException
	{
		// xxx
		AApplied_identification_assignment aaia = new AApplied_identification_assignment(); 
		CApplied_identification_assignment.usedinItems(null, armEntity, context.domain, aaia);
		for(int i=1; i<= aaia.getMemberCount(); i++){
			EApplied_identification_assignment eaia = aaia.getByIndex(i); 
			if(eaia.testRole(null)){
				EIdentification_role eir = eaia.getRole(null);
				if((eir.testName(null))&&(eir.getName(null).equals("version"))){
					AIdentification_item items = eaia.getItems(null);
					items.removeUnordered(armEntity);
					if(items.getMemberCount() == 0){
						eaia.deleteApplicationInstance();
						LangUtils.deleteInstanceIfUnused(context.domain, eir);
					}
				}
			}
		}
	}

	/**
	* Sets/creates data for Assigned_document_armx attribute.
	*
	* <p>
	attribute_mapping contained_data_type(contained_data_type,  document_type.product_data_type);
		document_file <= document
		document.kind -> document_type
		document_type.product_data_type
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// df <- aia
	public static void setContained_data_type(SdaiContext context, EFile armEntity) throws SdaiException
	{
		//unset old values
		unsetContained_data_type(context, armEntity);

		if (armEntity.testContained_data_type(null))
		{

			String value = armEntity.getContained_data_type(null);
			
			// type
			LangUtils.Attribute_and_value_structure[] typeStructure =
				{new LangUtils.Attribute_and_value_structure(
					CDocument_type.attributeProduct_data_type(null),
					value)
				};
			CDocument_type type = (CDocument_type)
				LangUtils.createInstanceIfNeeded(context, CDocument_type.definition, typeStructure);
			armEntity.setKind(null, type);
		}
	}


	/**
	* Unsets/deletes data for basis attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetContained_data_type(SdaiContext context, EFile armEntity) throws SdaiException
	{
		armEntity.unsetKind(null);
	}

}
