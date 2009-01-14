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

import java.util.Arrays;
import java.util.List;

import jsdai.SProduct_definition_schema.AProduct;
import jsdai.SProduct_definition_schema.AProduct_related_product_category;
import jsdai.SProduct_definition_schema.CProduct;
import jsdai.SProduct_definition_schema.CProduct_related_product_category;
import jsdai.SProduct_definition_schema.EProduct;
import jsdai.SProduct_definition_schema.EProduct_related_product_category;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;

/**
 * This class contains utility method, that tries to repair
 * products defined in models with underlying AP203 AIM schema,
 * so that they could be mapped to parts in XIM.
 */
public final class Ap203ProductCategoryRepair {
	
	/**
	 * The name of AP203 AIM schema.
	 */
	private static final String AP203_AIM_SCHEMA_NAME = "config_control_design";
	
	/**
	 * The name of category, that identifies parts in the mapping.
	 */
	private static final String PART_CATEGORY_NAME = "part";

	/**
	 * The list of category names allowed for products in AP203
	 * according to document:
	 * ISO TC184/SC4/* WG4 N601 (P6-1)
	 * Date: 8 November 1994
	 * Part: 203
	 * Title: Configuration Controlled 3D Designs of Mechanical Parts and Assemblies
	 */
	private static final List AP203_PART_CATEGORIES = Arrays.asList(new String[] {
		"assembly", "detail", "customer_furnished_equipment",
		"inseparable_assembly", "cast",	"coined", "drawn",
		"extruded", "forged", "formed", "machined", "molded",
		"rolled", "sheared"
	});
	
	private Ap203ProductCategoryRepair() { }

	/**
	 * This method tries to repair products defined in models
	 * with underlying AP203 AIM schema, so that they could be
	 * mapped to parts in XIM.
	 * 
	 * <p> This is done by adding part category to products,
	 * that either have no categories at all, or do not have
	 * part category assigned and have at least one of AP203
	 * categories assigned.
	 * 
	 * @param models models that should be repaired
	 * @throws SdaiException
	 */
	public static void run(ASdaiModel models)
		throws SdaiException {
		
		EProduct_related_product_category ePartCategory = null;
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			if (model.getOriginalSchemaName().equalsIgnoreCase(AP203_AIM_SCHEMA_NAME)) {
				AProduct aProducts = (AProduct) model.getInstances(CProduct.definition);
				for (SdaiIterator j = aProducts.createIterator(); j.next();) {
					EProduct eProduct = aProducts.getCurrentMember(j);
					if (needsRepair(eProduct, models)) {
						if (ePartCategory == null) {
							// use lazy category creation in order
							// to avoid creation of unneeded category
							ePartCategory = createPartCategory(model);
						}
						
						ePartCategory.getProducts(null).addUnordered(eProduct);
					}
				}
			}
		}
	}
	
	private static EProduct_related_product_category createPartCategory(SdaiModel model)
		throws SdaiException {
		
		EProduct_related_product_category ePartCategory = (EProduct_related_product_category)
			model.createEntityInstance(CProduct_related_product_category.definition);
		ePartCategory.setName(null, PART_CATEGORY_NAME);
		ePartCategory.setDescription(null, "generated");
		ePartCategory.createProducts(null);
		
		return ePartCategory;
	}
	
	private static boolean needsRepair(EProduct eProduct, ASdaiModel domain)
		throws SdaiException {
		
		AProduct_related_product_category aCategories = new AProduct_related_product_category();
		CProduct_related_product_category.usedinProducts(null, eProduct, domain, aCategories);
		if (aCategories.getMemberCount() == 0) {
			return true;
		}

		boolean hasAp210PartCat = false;
		for (SdaiIterator i = aCategories.createIterator(); i.next();) {
			EProduct_related_product_category eCategory = aCategories.getCurrentMember(i);
			if (eCategory.testName(null)) {
				String name = eCategory.getName(null);
				// mapping is case sensitive so we need to
				// find part with exact same name as part category
				// name in order to verify that product does
				// not need repair
				if (name.equals(PART_CATEGORY_NAME)) {
					// product has part category attached, so nothing needs to be done
					return false;
				}

				// we make following check case insensitive, because
				// sometimes we get such data where categories are
				// in upper case and sometimes in lower case
				if (AP203_PART_CATEGORIES.contains(name.toLowerCase())) {
					// the name of this category is in the list of
					// part categories of ap203, so product is a part
					// and thus needs to have part category assigned
					hasAp210PartCat = true;
				}
			}
		}
		
		return hasAp210PartCat;
	}
}
