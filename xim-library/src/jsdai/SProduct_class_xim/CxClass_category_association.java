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
import jsdai.SBasic_attribute_schema.ARole_association;
import jsdai.SBasic_attribute_schema.CObject_role;
import jsdai.SBasic_attribute_schema.CRole_association;
import jsdai.SBasic_attribute_schema.EObject_role;
import jsdai.SBasic_attribute_schema.ERole_association;
import jsdai.SProduct_class_mim.ACategory_usage_item;
import jsdai.SProduct_class_mim.CProduct_concept_feature_category_usage;
import jsdai.SProduct_class_mim.EProduct_concept_feature_category_usage;

public class CxClass_category_association extends CClass_category_association implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from CProduct_concept_feature_category_usage
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EProduct_concept_feature_category_usage type, EProduct_class instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testItems(EProduct_concept_feature_category_usage type) throws SdaiException {
		return test_aggregate(a1);
	}
	public ACategory_usage_item getItems(EProduct_concept_feature_category_usage type) throws SdaiException {
		if (a1 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a1;
	}*/
	public ACategory_usage_item createItems(EProduct_concept_feature_category_usage type) throws SdaiException {
		a1 = (ACategory_usage_item)create_aggregate_class(a1, a1$, ACategory_usage_item.class, 0);
		return a1;
	}
	public void unsetItems(EProduct_concept_feature_category_usage type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EProduct_concept_feature_category_usage type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF Taken from CProduct_concept_feature_category_usage
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_concept_feature_category_usage.definition);

	    // associated_product_class : Product_class_armx;
		setAssociated_product_class(context, this);
		
	    // mandatory : BOOLEAN;
		setMandatory(context, this);
		
		unsetAssociated_product_class(null);
		unsetMandatory(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

	    // associated_product_class : Product_class_armx;
		unsetAssociated_product_class(context, this);
		
	    // mandatory : BOOLEAN;
		unsetMandatory(context, this);
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
	public static void setMappingConstraints(SdaiContext context, EClass_category_association armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EClass_category_association armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for attribute version_id.
	*
	* <p>
	attribute_mapping associated_product_class(associated_product_class, $PATH, Product_class_armx);
		product_concept_feature_category_usage.items[1] -> category_usage_item 
		category_usage_item = product_class 
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAssociated_product_class(SdaiContext context, EClass_category_association armEntity) throws SdaiException
	{
		unsetAssociated_product_class(context, armEntity);
	       if(armEntity.testAssociated_product_class(null)){
	    	   EProduct_class_armx epc = armEntity.getAssociated_product_class(null);
	    	   armEntity.createItems(null).addUnordered(epc);
	       }
	}

	public static void unsetAssociated_product_class(SdaiContext context, EClass_category_association armEntity) throws SdaiException
	{
		armEntity.unsetItems(null);
	}

	/**
	* Sets/creates data for attribute version_id.
	*
	* <p>
	attribute_mapping mandatory(mandatory, object_role.name);
		product_concept_feature_category_usage <= 
		group_assignment group_assignment = role_select role_select <- 
		role_association.item_with_role 
		role_association.role -> object_role 
		{(object_role.name = 'mandatory category usage') 
		(object_role.name = 'optional category usage')} 
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PCFCU <- RA -> OR
	public static void setMandatory(SdaiContext context, EClass_category_association armEntity) throws SdaiException
	{
		unsetMandatory(context, armEntity);
	       if(armEntity.testMandatory(null)){
	    	   boolean isMandatory = armEntity.getMandatory(null);
	    	   String value;
	    	   if(isMandatory){
	    		   value = "mandatory category usage";
	    	   }else{
	    		   value = "optional category usage";
	    	   }
	    	   // Find role
				LangUtils.Attribute_and_value_structure[] orStructure =
				{new LangUtils.Attribute_and_value_structure(
					CObject_role.attributeName(null),
					value)
				};
				EObject_role eor = (EObject_role)
					LangUtils.createInstanceIfNeeded(context, CObject_role.definition, orStructure);
				// RA
				ERole_association era = (ERole_association)
					context.working_model.createEntityInstance(CRole_association.definition);
				era.setRole(null, eor);
				era.setItem_with_role(null, armEntity);
	       }
	}

	public static void unsetMandatory(SdaiContext context, EClass_category_association armEntity) throws SdaiException
	{
		ARole_association ara = new ARole_association();
    	CRole_association.usedinItem_with_role(null, armEntity, context.domain, ara);
    	for(int i=1,count=ara.getMemberCount(); i<= count; i++){
    		ERole_association era = ara.getByIndex(i);
    		if(!era.testRole(null)){
    			continue;
    		}
    		EObject_role eor = era.getRole(null);
    		if(eor.testName(null)){
    			if((eor.getName(null).equals("mandatory category usage"))||
    				(eor.getName(null).equals("optional category usage"))){
    				era.deleteApplicationInstance();
    			}
    		}
    	}
	}
	
}
