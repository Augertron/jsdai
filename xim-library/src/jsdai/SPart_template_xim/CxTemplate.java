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

package jsdai.SPart_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SApplication_context_schema.*;
import jsdai.SProduct_definition_schema.*;

public class CxTemplate extends CTemplate implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// From property_definition
	// methods for attribute: frame_of_reference, base type: SET OF ENTITY
/*	public static int usedinFrame_of_reference(EProduct type, jsdai.SApplication_context_schema.EProduct_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}*/
	public boolean testFrame_of_reference2(EProduct type) throws SdaiException {
		return test_aggregate(a3);
	}
/*	public jsdai.SApplication_context_schema.AProduct_context getFrame_of_reference(EProduct type) throws SdaiException {
		return (jsdai.SApplication_context_schema.AProduct_context)get_aggregate(a3);
	}*/
	public jsdai.SApplication_context_schema.AProduct_context createFrame_of_reference(EProduct type) throws SdaiException {
		a3 = (jsdai.SApplication_context_schema.AProduct_context)create_aggregate_class(a3, a3$,  jsdai.SApplication_context_schema.AProduct_context.class, 0);
		return a3;
	}
	public void unsetFrame_of_reference(EProduct type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct type) throws SdaiException {
		return a3$;
	}

	
	// END OF Property_definition
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct.definition);

		setMappingConstraints(context, this);
		
		fillAIMGaps(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		unfillAIMGaps(this);
		
	}
	
	
	public void fillAIMGaps(SdaiContext context, CxTemplate armEntity)throws SdaiException{
		if(!armEntity.testFrame_of_reference2(null)){
	      // Kind of AIM GAP
	      LangUtils.Attribute_and_value_structure[] pcxStructure = {
	            new LangUtils.Attribute_and_value_structure(
	            CProduct_context.attributeName(null), "template model")
	         };
	      
	         EProduct_context epc = (EProduct_context)
					LangUtils.createInstanceIfNeeded(context,
	         		CProduct_context.definition, pcxStructure);

	         // Fill AIM Gap
	         if(!epc.testFrame_of_reference(null)){
	        	 EApplication_context apc = CxAP210ARMUtilities.createApplication_context(context, "", true);	        	 
	        	 epc.setFrame_of_reference(null, apc);
	         }
	         if(!epc.testDiscipline_type(null)){
	        	 epc.setDiscipline_type(null, "");
	         }
	         
	      armEntity.createFrame_of_reference(null).addUnordered(epc);
		}		
	}
	
	public void unfillAIMGaps(CxTemplate armEntity)throws SdaiException{
		armEntity.unsetFrame_of_reference(null);
	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  product
	*  {product <-
	* 	product_related_product_category.products[i] 
	* 	product_related_product_category <= 
	* 	product_category 
	* 	product_category.name = 'template model'}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ETemplate armEntity) throws SdaiException
	{

      LangUtils.Attribute_and_value_structure[] pcStructure = {
            new LangUtils.Attribute_and_value_structure(
            CProduct_related_product_category.attributeName(null), "template model")
         };
      
         EProduct_related_product_category eprpc = (EProduct_related_product_category)
				LangUtils.createInstanceIfNeeded(context,
         		CProduct_related_product_category.definition, pcStructure);
      if(!eprpc.testProducts(null))
      	eprpc.createProducts(null).addUnordered(armEntity);
      else{
      	AProduct products = eprpc.getProducts(null);
      	if(!products.isMember(armEntity))
      		products.addUnordered(armEntity);
      }
		
	}

	public static void unsetMappingConstraints(SdaiContext context, ETemplate armEntity) throws SdaiException
	{
		String name = "template model";
		
		AProduct_related_product_category categories = new AProduct_related_product_category();
		CProduct_related_product_category.usedinProducts(null, armEntity, context.domain, categories);
		
		SdaiIterator iter = categories.createIterator();
		while(iter.next()){
			EProduct_related_product_category category = categories.getCurrentMember(iter);
			if(category.getName(null).equals(name))
				category.getProducts(null).removeUnordered(armEntity);
			if(category.getProducts(null).getMemberCount() == 0)
				category.deleteApplicationInstance();
		}
         
	}	
}
