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

package jsdai.SExternal_item_identification_assignment_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.dictionary.EDefined_type;
import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SExternal_item_identification_assignment_mim.*;
import jsdai.SExternal_reference_schema.CExternal_source;
import jsdai.SExternal_reference_schema.EExternal_source;
import jsdai.SManagement_resources_schema.*;

public class CxExternal_source_identification extends CExternal_source_identification implements EMappedXIMEntity
{

	// Taken from applied_external_identification_assignment
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EApplied_external_identification_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testItems(EApplied_external_identification_assignment type) throws SdaiException {
		return test_aggregate(a3);
	}
	public AExternal_identification_item getItems(EApplied_external_identification_assignment type) throws SdaiException {
		if (a3 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a3;
	}*/
	public AExternal_identification_item createItems(EApplied_external_identification_assignment type) throws SdaiException {
		a3 = (AExternal_identification_item)create_aggregate_class(a3, a3$, AExternal_identification_item.class, 0);
		return a3;
	}
	public void unsetItems(EApplied_external_identification_assignment type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EApplied_external_identification_assignment type) throws SdaiException {
		return a3$;
	}
	// ENDOF Taken from applied_external_identification_assignment
	
	// Taken from identification_assignment
	// attribute (current explicit or supertype explicit) : role, base type: entity identification_role
/*	public static int usedinRole(EIdentification_assignment type, EIdentification_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}*/
	public EIdentification_role getRole2(EIdentification_assignment type) throws SdaiException {
		a1 = get_instance(a1);
		return (EIdentification_role)a1;
	}
	private boolean testRole2(EIdentification_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public void setRole(EIdentification_assignment type, EIdentification_role value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetRole(EIdentification_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EIdentification_assignment type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from identification_assignment

	// Taken from external_identification_assignment
	// attribute (current explicit or supertype explicit) : source, base type: entity external_source
/*	public static int usedinSource(EExternal_identification_assignment type, jsdai.SExternal_reference_schema.EExternal_source instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testSource(EExternal_identification_assignment type) throws SdaiException {
		return test_instance(a2);
	}
	public jsdai.SExternal_reference_schema.EExternal_source getSource(EExternal_identification_assignment type) throws SdaiException {
		a2 = get_instance(a2);
		return (jsdai.SExternal_reference_schema.EExternal_source)a2;
	}*/
	public void setSource(EExternal_identification_assignment type, jsdai.SExternal_reference_schema.EExternal_source value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetSource(EExternal_identification_assignment type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeSource(EExternal_identification_assignment type) throws SdaiException {
		return a2$;
	}
	// ENDOF Taken from external_identification_assignment	

	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_external_identification_assignment.definition);

		setMappingConstraints(context, this);

      // source_id
		setSource_id(context, this);
      
		// source_type
		setSource_type(context, this);
		
      // description
		setDescription(context, this);
		
		// item		
		setItem(context, this);
		
		// clean ARM
      // source_id
		unsetSource_id(null);
      
		// source_type
		unsetSource_type(null);
		
      // description
		unsetDescription(null);
		
		// item		
		unsetItem(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

      // source_id
		unsetSource_id(context, this);
      
		// source_type
		unsetSource_type(context, this);
		
      // description
		unsetDescription(context, this);
		
		// item		
		unsetItem(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	applied_external_identification_assignment
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for attribute Source_id.
	*
	* <p>
	* mapping_constraints;
	*  <aa attribute="source_id">
	*  <aimelt>external_source.source_id</aimelt>
	*  	<source>ISO 10303-41</source>
	*  		<refpath>applied_external_identification_assignment &lt;= 
	* 				external_identification_assignment
	* 				external_identification_assignment.source -&gt; external_source
	* 				{external_source.source_id -&gt; source_item
	* 				source_item = identifier}
	* 			</refpath>
	* 		</aa>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// C <- AIA.
	public static void setSource_id(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		unsetSource_id(context, armEntity);
		if(armEntity.testSource_id(null)){
			String externalID = armEntity.getSource_id(null);
			// ES
			EDefined_type type = context.schema.getDefinedType("identifier");
			
         LangUtils.Attribute_and_value_structure[] esStructure = {
         		new LangUtils.Attribute_and_value_structure(
         				CExternal_source.attributeSource_id(null), externalID, type)
         };
         EExternal_source ees = (EExternal_source)
         	LangUtils.createInstanceIfNeeded(context, CExternal_source.definition, esStructure);
         armEntity.setSource(null, ees);
		}
	}

	public static void unsetSource_id(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		armEntity.unsetSource(null);
	}

	/**
	* Sets/creates data for attribute Source_type.
	*
	* <p>
	* mapping_constraints;
	*  <aa attribute="source_type">
	*  <aimelt>identification_role.name</aimelt>
	*  <source>ISO 10303-41</source>
	*  	<refpath>applied_external_identification_assignment &lt;= 
	* 		external_identification_assignment &lt;=
	* 		identification_assignment	
	* 		identification_assignment.role -&gt;
	* 		identification_role
	* 		identification_role.name
	* 		</refpath>
	* 	</aa>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// C <- AIA.
	public static void setSource_type(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		unsetSource_id(context, armEntity);
		if(armEntity.testSource_id(null)){
			String externalID = armEntity.getSource_id(null);
			// IR
         LangUtils.Attribute_and_value_structure[] irStructure = {
         		new LangUtils.Attribute_and_value_structure(
         				CIdentification_role.attributeName(null), externalID)
         };
         EIdentification_role eir = (EIdentification_role)
         	LangUtils.createInstanceIfNeeded(context, CIdentification_role.definition, irStructure);
         armEntity.setRole(null, eir);
		}
	}

	public static void unsetSource_type(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		armEntity.unsetRole(null);
	}
	
	/**
	* Sets/creates data for attribute Description.
	*
	* <p>
	* mapping_constraints;
	*  <aa attribute="source_type">
	*  <aimelt>identification_role.name</aimelt>
	*  <source>ISO 10303-41</source>
	*  	<refpath>applied_external_identification_assignment &lt;= 
	* 		external_identification_assignment &lt;=
	* 		identification_assignment	
	* 		identification_assignment.role -&gt;
	* 		identification_role
	* 		identification_role.name
	* 		</refpath>
	* 	</aa>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// C <- AIA.
	public static void setDescription(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		unsetDescription(context, armEntity);
		if(armEntity.testDescription(null)){
			String externalID = armEntity.getDescription(null);
			boolean test = false;
			EIdentification_role eir = null;
			if(armEntity instanceof CxExternal_source_identification){
				CxExternal_source_identification esi = (CxExternal_source_identification)armEntity;
				test = ((CxExternal_source_identification)(armEntity)).testRole2(null);
				if(test){
					eir = (EIdentification_role)esi.getRole2(null);  
				}
			}
			if(eir == null)
				throw new SdaiException(SdaiException.AI_NSET, " It is impossible to set description without source_type set ");
         
         // Need to search for another Identification_role
         if((eir.testDescription(null))&&(!eir.getDescription(null).equals(externalID))){
				AEntity result = new AEntity();
				eir.findEntityInstanceUsers(context.domain, result);
				// we can reuse and overwrite description
				if(result.getMemberCount() == 1){
					eir.setDescription(null, externalID);
					return;
				}
				// Need to search/populate another ID
				// IR
	         LangUtils.Attribute_and_value_structure[] irStructure = {
	         		new LangUtils.Attribute_and_value_structure(
	         				CIdentification_role.attributeName(null), armEntity.getSource_type(null)),
	         		new LangUtils.Attribute_and_value_structure(
	         				CIdentification_role.attributeDescription(null), externalID),
								
	         };
	         eir = (EIdentification_role)
	         	LangUtils.createInstanceIfNeeded(context, CExternal_source.definition, irStructure);
	         armEntity.setRole(null, eir);
         }
         else{
         	eir.setDescription(null, externalID);
         }
		}
	}

	public static void unsetDescription(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		boolean test = false;
		if(armEntity instanceof CxExternal_source_identification){
			CxExternal_source_identification esi = (CxExternal_source_identification)armEntity;
			test = esi.testRole2(null);
			EIdentification_role eir = esi.getRole2(null);
			if(eir != null){
				eir.unsetDescription(null);
			}
		}
		// TODO - support other subtypes when they will be implemented in Cx classes (document_location_identification (DOCUMENT_DEFINITION_XIM), file_location_identification (FILE_IDENTIFICATION_XIM))
		else{
			armEntity.findEntityInstanceSdaiModel().getRepository().getSession().printlnSession("Not supported subtype of External_source_identification "+armEntity);
		}
	}

	/**
	* Sets/creates data for attribute Description.
	*
	* <p>
	* mapping_constraints;
	* 	<aa attribute="item" assertion_to="external_identification_item">
	* 		<aimelt>PATH</aimelt>
	* 		<refpath>applied_external_identification_assignment.items[i]-&gt; external_identification_item 
	* 		</refpath>
	* 	</aa>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// C <- AIA.
	public static void setItem(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		unsetItem(context, armEntity);
		if(armEntity.testItem(null)){
			EEntity item = armEntity.getItem(null);
			armEntity.createItems(null).addUnordered(item);
		}
	}

	public static void unsetItem(SdaiContext context, EExternal_source_identification armEntity) throws SdaiException
	{
		armEntity.unsetItems(null);
	}
	
}
