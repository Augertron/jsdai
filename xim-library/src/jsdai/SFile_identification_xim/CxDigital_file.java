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
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SDocument_schema.*;
import jsdai.SFile_identification_mim.CDocument_file;
import jsdai.SFile_identification_xim.EFile;
import jsdai.SProduct_property_definition_schema.ECharacterized_object;

/**
* @author Giedrius Liutkus
* @version $Revision$
*/

public class CxDigital_file extends CDigital_file implements EMappedXIMEntity
{

	// Taken from CCharacterized_object
	/// methods for attribute: name, base type: STRING
	public boolean testName(ECharacterized_object type) throws SdaiException {
		return test_string(a4);
	}
	public String getName(ECharacterized_object type) throws SdaiException {
		return get_string(a4);
	}
	public void setName(ECharacterized_object type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetName(ECharacterized_object type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ECharacterized_object type) throws SdaiException {
		return a4$;
	}

	//<01> generating methods for consolidated attribute:  description
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: description, base type: STRING
	public boolean testDescription(ECharacterized_object type) throws SdaiException {
		return test_string(a5);
	}
	public String getDescription(ECharacterized_object type) throws SdaiException {
		return get_string(a5);
	}
	public void setDescription(ECharacterized_object type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetDescription(ECharacterized_object type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(ECharacterized_object type) throws SdaiException {
		return a5$;
	}
	// END OF taken from CCharacterized_object

	// Taken from CDocument	
	/// methods for attribute: name, base type: STRING
	public boolean testName(EDocument type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EDocument type) throws SdaiException {
		return get_string(a1);
	}
	public void setName(EDocument type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EDocument type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EDocument type) throws SdaiException {
		return a1$;
	}

	//<01> generating methods for consolidated attribute:  description
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	/// methods for attribute: description, base type: STRING
	public boolean testDescription(EDocument type) throws SdaiException {
		return test_string(a2);
	}
	public String getDescription(EDocument type) throws SdaiException {
		return get_string(a2);
	}
	public void setDescription(EDocument type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetDescription(EDocument type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EDocument type) throws SdaiException {
		return a2$;
	}

	//<01> generating methods for consolidated attribute:  kind
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : kind, base type: entity document_type
	public static int usedinKind(EDocument type, EDocument_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testKind(EDocument type) throws SdaiException {
		return test_instance(a3);
	}
	public EDocument_type getKind(EDocument type) throws SdaiException {
		return (EDocument_type)get_instance(a3);
	}
	public void setKind(EDocument type, EDocument_type value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetKind(EDocument type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeKind(EDocument type) throws SdaiException {
		return a3$;
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
		CxFile.setMappingConstraints(context, armEntity);
		EDocument_representation_type edrt = (EDocument_representation_type)
			context.working_model.createEntityInstance(CDocument_representation_type.definition);
		edrt.setName((EDocument_representation_type)null, "digital");
		edrt.setRepresented_document(null, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EFile armEntity) throws SdaiException
	{
		ADocument_representation_type adrt = new ADocument_representation_type();
		CDocument_representation_type.usedinRepresented_document(null, armEntity, context.domain, adrt);
		for(int i=1, count=adrt.getMemberCount(); i<= count; i++){
			adrt.getByIndex(i).deleteApplicationInstance();
		}
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
		CxFile.setVersion(context, armEntity);
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
		CxFile.unsetVersion(context, armEntity);		
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
		CxFile.setContained_data_type(context, armEntity);		
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
		CxFile.unsetContained_data_type(context, armEntity);
	}

}
