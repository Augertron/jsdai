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

import jsdai.SExtended_measure_representation_mim.AValue_range;
import jsdai.SExtended_measure_representation_mim.CValue_range;
import jsdai.SExtended_measure_representation_mim.EValue_range;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ECompound_representation_item;
import jsdai.SRepresentation_schema.EList_representation_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ESet_representation_item;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author Giedrius Liutkus
 * bug #3093 - Value_range have list_representation_items, which is clear violation of its WR1
 *
 */
public class ValueRangeFixer {
	
	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		
		AValue_range productList = (AValue_range) models.getExactInstances(CValue_range.definition);
		for (SdaiIterator it = productList.createIterator(); it.next(); ) {
			EValue_range evr = productList.getCurrentMember(it);
			if(evr.testItem_element(null) == ECompound_representation_item.sItem_elementList_representation_item){
				ARepresentation_item items = evr.getItem_element(null, (EList_representation_item)null);
				importer.errorMessage(" Fix WR1 of Value_range for "+evr);
				evr.unsetItem_element(null);
				ARepresentation_item itemsNew = evr.createItem_element(null, (ESet_representation_item)null);
				for (SdaiIterator it2 = items.createIterator(); it2.next(); ) {
					ERepresentation_item item = items.getCurrentMember(it2);
					itemsNew.addUnordered(item);
				}
			}
		}
	}
}
