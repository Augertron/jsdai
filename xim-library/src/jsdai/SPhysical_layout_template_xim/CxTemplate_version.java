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

package jsdai.SPhysical_layout_template_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_definition_schema.*;

public class CxTemplate_version extends CTemplate_version implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_definition_formation.definition);

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
	public static void setMappingConstraints(SdaiContext context, ETemplate_version armEntity) throws SdaiException
	{
		// Constraints must be set at product/template level anyway, so do nothing here.
/*      LangUtils.Attribute_and_value_structure[] pcStructure = {
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
      }*/
		
	}

	public static void unsetMappingConstraints(SdaiContext context, ETemplate_version armEntity) throws SdaiException
	{
		//	Constraints must be set at product/template level anyway, so do nothing here.
/*      LangUtils.Attribute_and_value_structure[] pcStructure = {
            new LangUtils.Attribute_and_value_structure(
            		CProduct_related_product_category.attributeName(null), "template model")
         };
      
         EProduct_related_product_category eprpc = (EProduct_related_product_category)
				LangUtils.createInstanceIfNeeded(context,
         		CProduct_related_product_category.definition, pcStructure);
         if(eprpc.testProducts(null)){
         	AProduct products = eprpc.getProducts(null);
         	if(!products.isMember(armEntity))
         		products.removeUnordered(armEntity);
         }*/
         
	}	
}
