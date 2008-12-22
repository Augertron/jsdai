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

package jsdai.SMulti_linguism_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SManagement_resources_schema.CClassification_role;
import jsdai.SManagement_resources_schema.EAttribute_classification_assignment;
import jsdai.SManagement_resources_schema.EClassification_role;
import jsdai.SMulti_linguism_mim.AAttribute_language_item;
import jsdai.SMulti_linguism_mim.CAttribute_language_assignment;
import jsdai.SMulti_linguism_mim.EAttribute_language_assignment;

public class CxLanguage_indication extends CLanguage_indication implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from CAttribute_classification_assignment
	//<01> generating methods for consolidated attribute:  role
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : role, base type: entity classification_role
/*	public static int usedinRole(EAttribute_classification_assignment type, EClassification_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testRole(EAttribute_classification_assignment type) throws SdaiException {
		return test_instance(a2);
	}
	public EClassification_role getRole(EAttribute_classification_assignment type) throws SdaiException {
		return (EClassification_role)get_instance(a2);
	}*/
	public void setRole(EAttribute_classification_assignment type, EClassification_role value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetRole(EAttribute_classification_assignment type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EAttribute_classification_assignment type) throws SdaiException {
		return a2$;
	}
	// ENDOF Taken from CAttribute_classification_assignment
	
	// Taken from CAttribute_language_assignment
	//<01> generating methods for consolidated attribute:  items
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EAttribute_language_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testItems(EAttribute_language_assignment type) throws SdaiException {
		return test_aggregate(a3);
	}
	public AAttribute_language_item getItems(EAttribute_language_assignment type) throws SdaiException {
		if (a3 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a3;
	}*/
	public AAttribute_language_item createItems(EAttribute_language_assignment type) throws SdaiException {
		a3 = (AAttribute_language_item)create_aggregate_class(a3, a3$, AAttribute_language_item.class, 0);
		return a3;
	}
	public void unsetItems(EAttribute_language_assignment type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EAttribute_language_assignment type) throws SdaiException {
		return a3$;
	}
	// ENDOF Taken from CAttribute_language_assignment
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAttribute_language_assignment.definition);

			setMappingConstraints(context, this);

			// considered_instance : attribute_language_item;
			setConsidered_instance(context, this);

			
			// Clean ARM specific attributes - this is DERIVED to some magic string

			// considered_instance : attribute_language_item;
			unsetConsidered_instance(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			// considered_instance : attribute_language_item;
			unsetConsidered_instance(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
			attribute_language_assignment <= attribute_classification_assignment
			{attribute_classification_assignment.role ->
			classification_role
			classification_role.name = 'primary'}
	   end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			ELanguage_indication armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		// CR
	    LangUtils.Attribute_and_value_structure[] crStructure = {
	  		  new LangUtils.Attribute_and_value_structure(CClassification_role.attributeName(null), 
	  		  	"primary")};
	    EClassification_role cr = (EClassification_role)
	  		  LangUtils.createInstanceIfNeeded(context, CClassification_role.definition, crStructure);
	    armEntity.setRole(null, cr);
/*		// ALA
	    LangUtils.Attribute_and_value_structure[] alaStructure = {
	  		  new LangUtils.Attribute_and_value_structure(CAttribute_language_assignment.attributeRole(null), 
		  		cr)};
	    EAttribute_language_assignment ala = (EAttribute_language_assignment)
	  		  LangUtils.createInstanceIfNeeded(context, CAttribute_language_assignment.definition, alaStructure);
	    if(!ala.testAttribute_name(null)){
	    	ala.setAttribute_name(null, "");
	    }
	    AAttribute_language_item items;
	    if(ala.testItems(null)){
	    	items = ala.getItems(null);
	    }else{
	    	items = ala.createItems(null);
	    }
	    if(!items.isMember(armEntity)){
	    	items.addUnordered(armEntity);
	    }*/
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ELanguage_indication armEntity) throws SdaiException {
	}


	//********** "interconnect_module_usage_view" attributes

	/**
	* Sets/creates data for considered_instance attribute.
	*
	* <p>
		attribute_mapping considered_instance(considered_instance, $PATH, attribute_language_item);
			multi_language_attribute_assignment.items[i] -> multi_language_attribute_item
		end_attribute_mapping;
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PSPVA <- PD <-PDR->PAR
	public static void setConsidered_instance(SdaiContext context, ELanguage_indication armEntity) throws SdaiException
	{
		//unset old values
		unsetConsidered_instance(context, armEntity);

		if (armEntity.testConsidered_instance(null))
		{
	      EEntity eci = armEntity.getConsidered_instance(null);
	      try {
			armEntity.createItems(null).addUnordered(eci);
	      } catch (SdaiException e) {
			System.err.println("Problem with setting language_indication.considered_instance "+armEntity+" -> "+eci);
			e.printStackTrace();
		}
		}
	}


	/**
	* Unsets/deletes data for considered_instance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConsidered_instance(SdaiContext context, ELanguage_indication armEntity) throws SdaiException
	{
		armEntity.unsetItems(null);
	}

}