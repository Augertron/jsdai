/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2009, LKSoftWare GmbH, Germany
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

package jsdaix.processor.xim_aim.pre.manual_aim_repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jsdai.SProduct_definition_schema.AProduct;
import jsdai.SProduct_definition_schema.AProduct_category_relationship;
import jsdai.SProduct_definition_schema.AProduct_related_product_category;
import jsdai.SProduct_definition_schema.CProduct;
import jsdai.SProduct_definition_schema.CProduct_category_relationship;
import jsdai.SProduct_definition_schema.CProduct_related_product_category;
import jsdai.SProduct_definition_schema.EProduct;
import jsdai.SProduct_definition_schema.EProduct_category;
import jsdai.SProduct_definition_schema.EProduct_category_relationship;
import jsdai.SProduct_definition_schema.EProduct_related_product_category;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiRepository;

/**
 * @author evita
 *
 * The main idea is that product->part (and subtypes) mapping searches directly for related
 * product_category with name "part" (or "raw materia" and others specific names). But in some cases 
 * product category is not associated directly as:
 * 
 * 	product <- product_related_product_category.name="part"
 * 
 * but is associated indirectly throug product_category_relationship as:
 * 
 * 	product <- product_related_product_category <- product_category_relationship <- ... <- product category.name="part"
 * 
 * To solve the problem when mapping does not find deeper product_category, all super product_categories are collected
 * and assigned directly to the product.
 * 
 *   Test File: Step21\cax-if\round02j\ad\AS1-IN-203.stp
 *
 */
public class ProductCategoryFlattener {
	
	public static void run(ASdaiModel models) throws SdaiException {
		
		HashMap categoriesWithParents = new HashMap();
		
		AProduct productList = (AProduct) models.getExactInstances(CProduct.class);
		for (SdaiIterator it = productList.createIterator(); it.next(); ) {
			EProduct product = productList.getCurrentMember(it);
			AProduct_related_product_category categories = new AProduct_related_product_category();
			CProduct_related_product_category.usedinProducts(null, product, models, categories);
			for (SdaiIterator itCategory = categories.createIterator(); itCategory.next(); ) {
				EProduct_related_product_category category = categories.getCurrentMember(itCategory);
				ArrayList superCategories = gatherSuperProductCategories(category, models, categoriesWithParents); 
				for (Iterator itSC = superCategories.iterator(); itSC.hasNext(); ) {
					EProduct_category superCategory = (EProduct_category) itSC.next();
					if (!(superCategory instanceof EProduct_related_product_category)) {
						superCategory = (EProduct_category)	superCategory.findEntityInstanceSdaiModel().substituteInstance(
								superCategory, EProduct_related_product_category.class);
					}
					if (!((EProduct_related_product_category) superCategory).testProducts(null)) {
						((EProduct_related_product_category) superCategory).createProducts(null);
					}
					((EProduct_related_product_category) superCategory).getProducts(null).addUnordered(product); 
				}
			}
		}
	}
	
	/**
	 * 
	 * @param productCategory
	 * @param models
	 * @param categoriesWithParents
	 * @return
	 * @throws SdaiException
	 */
	private static ArrayList gatherSuperProductCategories(EProduct_category productCategory, ASdaiModel models, HashMap categoriesWithParents) throws SdaiException {
		ArrayList parents = new ArrayList(); 
		if (categoriesWithParents.containsKey(productCategory)) {
			return (ArrayList) categoriesWithParents.get(productCategory);
		}
		AProduct_category_relationship includedInCategories = new AProduct_category_relationship();
		CProduct_category_relationship.usedinSub_category(null, productCategory, models, includedInCategories);
		for (SdaiIterator it = includedInCategories.createIterator(); it.next(); ) {
			EProduct_category_relationship relationship = includedInCategories.getCurrentMember(it); 
			if (relationship.testCategory(null)) {
				EProduct_category superCategory = relationship.getCategory(null);
				parents.add(superCategory);
				parents.addAll(gatherSuperProductCategories(superCategory, models, categoriesWithParents));
			}
		}
		return parents;
	}
	
	
	
	
	
	
}
