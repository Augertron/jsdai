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


import jsdai.lang.AEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiRepository;
import jsdai.SLayered_interconnect_complex_template_xim.ETemplate_location_in_structured_template_transform;
import jsdai.SModel_parameter_xim.EParameter_assignment_armx;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;

/**
 * @author Giedrius
 * 
 *	Cleans garbage from items list (those entities, which are there mainly because of mapping and not as items for geometry
 */
public class RepItemsInRepCleaner {
	/**
	 * 
	 * @param repo
	 * @throws SdaiException
	 */
	public static void run(SdaiRepository repo) throws SdaiException {
		// This may be expanded in the future
		AEntity instancesToClean = repo.getModels().getInstances(CRepresentation.definition);
		for(int index=1, countR=instancesToClean.getMemberCount(); index <= countR; index++){
			ERepresentation instanceToClean = (ERepresentation)instancesToClean.getByIndexEntity(index);
			// entity have items attribute derived
			if(instanceToClean instanceof EParameter_assignment_armx){
				continue;
			}
			if(instanceToClean.testItems(null)){
				ARepresentation_item items = instanceToClean.getItems(null);
				for(int i=1; i <= items.getMemberCount();){
					ERepresentation_item item = items.getByIndex(i);
					// Add more types here on demand
					if(item instanceof ETemplate_location_in_structured_template_transform){
						items.removeUnordered(item);
					}else{
						i++;
					}
				}
			}
			// Check Shape_representations. Logic - 
			// if we recognize it as part of some known mapping pattern - delete it.
			// Here pattern - Template_location_in_structured_template_transform.reference_location.
			if(instanceToClean.getItems(null).getMemberCount() == 0){
				instanceToClean.deleteApplicationInstance();
			}
		}
	}
}
