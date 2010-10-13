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

package jsdai.SAnalytical_model_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SInformation_product_xim.CxInformation_product;
import jsdai.SProduct_definition_schema.*;

public class CxAnalytical_model_armx extends CAnalytical_model_armx implements EMappedXIMEntity
{

	// Taken from CProduct
	// methods for attribute: frame_of_reference, base type: SET OF ENTITY
/*	public static int usedinFrame_of_reference(EProduct type, jsdai.SApplication_context_schema.EProduct_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct type) throws SdaiException {
		return test_aggregate(a3);
	}
	public jsdai.SApplication_context_schema.AProduct_context getFrame_of_reference(EProduct type) throws SdaiException {
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
	// ENDOF taken from CProduct
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct.definition);

		setMappingConstraints(context, this);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*     <refpath>product
	*  product
	*  {product &lt;-
	*  product_related_product_category.products
	*  product_related_product_category &lt;=
	*  product_category
	*  product_category.name='analytical model'}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EAnalytical_model_armx armEntity) throws SdaiException
	{
		CxInformation_product.setMappingConstraints(context, armEntity);
		
		String name = "analytical model";
      LangUtils.Attribute_and_value_structure[] pcStructure = {
            new LangUtils.Attribute_and_value_structure(
            CProduct_related_product_category.attributeName(null), name)
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

	public static void unsetMappingConstraints(SdaiContext context, EAnalytical_model_armx armEntity) throws SdaiException
	{
		CxInformation_product.unsetMappingConstraints(context, armEntity);
		
		String name = "analytical model";

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
