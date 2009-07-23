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
import jsdai.SClassification_assignment_mim.*;
import jsdai.SManagement_resources_schema.*;

public class CxClassification_association extends CClassification_association implements EMappedXIMEntity
{

	// From Classification_assignment
	// attribute (current explicit or supertype explicit) : role, base type: entity classification_role
/*	public static int usedinRole(EClassification_assignment type, EClassification_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRole(EClassification_assignment type) throws SdaiException {
		return test_instance(a1);
	}
	public EClassification_role getRole(EClassification_assignment type) throws SdaiException {
		return (EClassification_role)get_instance(a1);
	}*/
	public void setRole(EClassification_assignment type, EClassification_role value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetRole(EClassification_assignment type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EClassification_assignment type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF from Classification_assignment
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CApplied_classification_assignment.definition);

		setMappingConstraints(context, this);

		// Definitional
		setDefinitional(context, this);
		
		// clean ARM
		// version_id
		unsetDefinitional(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// version_id
		unsetDefinitional(context, this);
		
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
	public static void setMappingConstraints(SdaiContext context, EClassification_association armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EClassification_association armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for attribute version_id.
	*
	* <p>
	* attribute_mapping definitional(definitional, (classification_role.name)(classification_role.name)(classification_role.name));
		(
		applied_classification_assignment <= classification_assignment
		classification_assignment.role -> classification_role
		{classification_role.name = 'definitional'}		
					)(
		applied_classification_assignment <= classification_assignment
		classification_assignment.role -> classification_role
		{classification_role.name = 'non-definitional'}		
					)(
		applied_classification_assignment <= classification_assignment
		classification_assignment.role -> classification_role
		{classification_role.name = ''}		
			)
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// C <- AIA.
	public static void setDefinitional(SdaiContext context, EClassification_association armEntity) throws SdaiException
	{
		unsetDefinitional(context, armEntity);
		if(armEntity.testDefinitional(null)){
			int definitional = armEntity.getDefinitional(null);
			String value;
			if(ELogical.TRUE == definitional){
				value = "definitional";
			}else if(ELogical.FALSE == definitional){
				value = "non-definitional";
			}else{
				value = "";
			}
			// Role
			LangUtils.Attribute_and_value_structure[] roleStructure = {
         		new LangUtils.Attribute_and_value_structure(CClassification_role.attributeName(null), value)
			};
			EClassification_role role = (EClassification_role)
         	LangUtils.createInstanceIfNeeded(context, CClassification_role.definition, roleStructure);
			
			armEntity.setRole(null, role);
		}
	}

	public static void unsetDefinitional(SdaiContext context, EClassification_association armEntity) throws SdaiException
	{
		armEntity.unsetRole(null);
	}

}
