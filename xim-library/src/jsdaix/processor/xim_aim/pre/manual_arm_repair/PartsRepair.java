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

package jsdaix.processor.xim_aim.pre.manual_arm_repair;

import java.util.ArrayList;
import java.util.Iterator;

import jsdai.SPart_and_version_identification_xim.CPart;
import jsdai.SPart_and_version_identification_xim.EPart;
import jsdai.SProduct_identification_xim.CProduct_armx;
import jsdai.SProduct_identification_xim.EProduct_armx;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author evita
 * 
 * All product entitity instances with product_related_product_categories named:
 * - part
 * - tool 
 * - raw material
 * - document
 *  
 *  Are mapped into - tool, part, raw_material or document by the automating mapping engine (MIM->XIM)
 *  
 *  All other product entity instances are left not mapped, and while part in ARM is abstract, product is 
 *  substituted with product_armx (mapping is extended).
 *   
 *  The purpose of this class is to substitute all these left product_armx entity instances with part.
 * 
 * Test file: Step21\LPKF\HSG-IMAT\PadTrack05.stp
 */
public class PartsRepair {
	
	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		AEntity products = models.getExactInstances(CProduct_armx.class);
		ArrayList productList = new ArrayList(); 
		for (SdaiIterator it = products.createIterator(); it.next(); ) {
			//	walkaround because getExactInstances is living aggregate and not ok in this case
			productList.add(products.getCurrentMemberEntity(it));
		}
		
		for (Iterator it = productList.iterator(); it.hasNext(); ) {
			EProduct_armx product = (EProduct_armx) it.next();
			String message = " Changed "+product;
			EEntity instance = product.findEntityInstanceSdaiModel().substituteInstance(product, CPart.definition);
			importer.logMessage(message+" to "+instance);
		}
	}
}
