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
import jsdai.SManagement_resources_schema.CAttribute_value_role;
import jsdai.SManagement_resources_schema.CClassification_role;
import jsdai.SManagement_resources_schema.EAttribute_value_assignment;
import jsdai.SManagement_resources_schema.EAttribute_value_role;
import jsdai.SManagement_resources_schema.EClassification_role;
import jsdai.SMulti_linguism_mim.AAttribute_language_assignment;
import jsdai.SMulti_linguism_mim.AAttribute_language_item;
import jsdai.SMulti_linguism_mim.AMulti_language_attribute_item;
import jsdai.SMulti_linguism_mim.CAttribute_language_assignment;
import jsdai.SMulti_linguism_mim.CMulti_language_attribute_assignment;
import jsdai.SMulti_linguism_mim.EAttribute_language_assignment;
import jsdai.SMulti_linguism_mim.ELanguage;
import jsdai.SMulti_linguism_mim.EMulti_language_attribute_assignment;

public class CxAttribute_translation_assignment
		extends
		CAttribute_translation_assignment implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from CAttribute_value_assignment
	// SELF\attribute_value_assignment.role : attribute_value_role :=  ? ;
	//<01> generating methods for consolidated attribute:  role
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : role, base type: entity attribute_value_role
/*	public static int usedinRole(EAttribute_value_assignment type, EAttribute_value_role instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testRole(EAttribute_value_assignment type) throws SdaiException {
		return test_instance(a2);
	}
	public EAttribute_value_role getRole(EAttribute_value_assignment type) throws SdaiException {
		return (EAttribute_value_role)get_instance(a2);
	}*/
	public void setRole(EAttribute_value_assignment type, EAttribute_value_role value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetRole(EAttribute_value_assignment type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeRole(EAttribute_value_assignment type) throws SdaiException {
		return a2$;
	}
	// ENDOF Taken from CAttribute_value_assignment
	
	// Taken from CMulti_language_attribute_assignment
	// SELF\multi_language_attribute_assignment.items : SET [1:?] OF representation_item :=  ? ;
	//<01> generating methods for consolidated attribute:  items
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EMulti_language_attribute_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testItems(EMulti_language_attribute_assignment type) throws SdaiException {
		return test_aggregate(a3);
	}
	public AMulti_language_attribute_item getItems(EMulti_language_attribute_assignment type) throws SdaiException {
		if (a3 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a3;
	}*/
	public AMulti_language_attribute_item createItems(EMulti_language_attribute_assignment type) throws SdaiException {
		a3 = (AMulti_language_attribute_item)create_aggregate_class(a3, a3$, AMulti_language_attribute_item.class, 0);
		return a3;
	}
	public void unsetItems(EMulti_language_attribute_assignment type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EMulti_language_attribute_assignment type) throws SdaiException {
		return a3$;
	}
	
	// ENDOF Taken from CMulti_language_attribute_assignment
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMulti_language_attribute_assignment.definition);

			setMappingConstraints(context, this);

			// considered_instance : attribute_language_item;
			setConsidered_instance(context, this);

			// translation_language_x : language;
			setTranslation_language_x(context, this);

			
			// Clean ARM specific attributes - this is DERIVED to some magic string

			// considered_instance : attribute_language_item;
			unsetConsidered_instance(null);

			// translation_language_x : language;
			unsetTranslation_language_x(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			// considered_instance : attribute_language_item;
			unsetConsidered_instance(context, this);

			// translation_language_x : language;
			unsetTranslation_language_x(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
			#1: multi_language_attribute_assignment <= attribute_value_assignment
			{attribute_value_assignment.role -> attribute_value_role
			attribute_value_role.name = 'alternate language'}
			#2: {attribute_value_assignment = attribute_language_item 
			attribute_language_item <- attribute_language_assignment.items[i]
			attribute_language_assignment <= attribute_classification_assignment
			{attribute_classification_assignment.role -> classification_role
			classification_role.name = 'translated'}
			{attribute_classification_assignment.attribute_name = 'attribute_value'}}
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
			EAttribute_translation_assignment armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		// AVR
	    LangUtils.Attribute_and_value_structure[] avrStructure = {
		  new LangUtils.Attribute_and_value_structure(CAttribute_value_role.attributeName(null), 
		  	"alternate language")};
	    EAttribute_value_role evr = (EAttribute_value_role)
		  LangUtils.createInstanceIfNeeded(context, CAttribute_value_role.definition, avrStructure);
		armEntity.setRole(null, evr);
		// CR
	    LangUtils.Attribute_and_value_structure[] crStructure = {
	  		  new LangUtils.Attribute_and_value_structure(CClassification_role.attributeName(null), 
	  		  	"translated")};
	    EClassification_role cr = (EClassification_role)
	  		  LangUtils.createInstanceIfNeeded(context, CClassification_role.definition, crStructure);
		// ALA
	    LangUtils.Attribute_and_value_structure[] alaStructure = {
	  		  new LangUtils.Attribute_and_value_structure(CAttribute_language_assignment.attributeAttribute_name(null), 
	  		  	"attribute_value"),
	  		  new LangUtils.Attribute_and_value_structure(CAttribute_language_assignment.attributeRole(null), 
		  		cr)};
	    EAttribute_language_assignment ala = (EAttribute_language_assignment)
	  		  LangUtils.createInstanceIfNeeded(context, CAttribute_language_assignment.definition, alaStructure);
	    AAttribute_language_item items;
	    if(ala.testItems(null)){
	    	items = ala.getItems(null);
	    }else{
	    	items = ala.createItems(null);
	    }
	    if(!items.isMember(armEntity)){
	    	items.addUnordered(armEntity);
	    }
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EAttribute_translation_assignment armEntity) throws SdaiException {
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
	public static void setConsidered_instance(SdaiContext context, EAttribute_translation_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetConsidered_instance(context, armEntity);

		if (armEntity.testConsidered_instance(null))
		{
	      EEntity eci = armEntity.getConsidered_instance(null);
	      armEntity.createItems(null).addUnordered(eci);
		}
	}


	/**
	* Unsets/deletes data for considered_instance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConsidered_instance(SdaiContext context, EAttribute_translation_assignment armEntity) throws SdaiException
	{
		armEntity.unsetItems(null);
	}


	/**
	* Sets/creates data for of_product attribute.
	*
	* <p>
		attribute_mapping translation_language_x(translation_language_x, $PATH, Language);
			multi_language_attribute_assignment <= attribute_value_assignment
			attribute_value_assignment = attribute_language_item 
			attribute_language_item <- attribute_language_assignment.items[i]
			attribute_language_assignment <= attribute_classification_assignment
			attribute_classification_assignment.assigned_class -> group
			group => language
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// MLAA <- ALA -> L
	public static void setTranslation_language_x(SdaiContext context, EAttribute_translation_assignment armEntity) throws SdaiException
	{
		//unset old values
		unsetTranslation_language_x(context, armEntity);
		if (armEntity.testTranslation_language_x(null)){
	      ELanguage language = armEntity.getTranslation_language_x(null);
	      // Idea - reuse ALA from mapping constraints - therefore assumption - it always must be present
	      AAttribute_language_assignment ala = new AAttribute_language_assignment();
	      CAttribute_language_assignment.usedinItems(null, armEntity, context.domain, ala);
	      SdaiIterator iter = ala.createIterator();
	      boolean isSet = false;
	      while(iter.next()){
	    	  EAttribute_language_assignment ela = ala.getCurrentMember(iter);
	    	  if(!ela.testAssigned_class(null)){
	    		  ela.setAssigned_class(null, language);
	    		  isSet = true;
	    		  break;
	    	  }
	      }
	      // Typically we should never enter here
	      if(!isSet){
		      EAttribute_language_assignment eala = (EAttribute_language_assignment)
		      	context.working_model.createEntityInstance(CAttribute_language_assignment.definition);
		      eala.createItems(null).addUnordered(armEntity);
		      eala.setAssigned_class(null, language);
		      eala.setAttribute_name(null, "");
		      // CR
			  LangUtils.Attribute_and_value_structure[] crStructure = {
			  	new LangUtils.Attribute_and_value_structure(CClassification_role.attributeName(null), 
			  		  	"translated")};
			  EClassification_role cr = (EClassification_role)
			  	  LangUtils.createInstanceIfNeeded(context, CClassification_role.definition, crStructure);
		      eala.setRole(null, cr);
	      }
		}
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_dielectric_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetTranslation_language_x(SdaiContext context, EAttribute_translation_assignment armEntity) throws SdaiException
	{
	      AAttribute_language_assignment ala = new AAttribute_language_assignment();
	      CAttribute_language_assignment.usedinItems(null, armEntity, context.domain, ala);
	      SdaiIterator iter = ala.createIterator();
	      while(iter.next()){
	    	  EAttribute_language_assignment ela = ala.getCurrentMember(iter);
	    	  if(ela.testAssigned_class(null)){
	    		  if(ela.getAssigned_class(null) instanceof ELanguage){
	    			  ela.unsetAssigned_class(null);
	    		  }
	    	  }
	      }
	}

}