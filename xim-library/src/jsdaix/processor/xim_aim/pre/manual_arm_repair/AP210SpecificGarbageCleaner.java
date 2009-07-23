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

import jsdai.SDimension_tolerance_xim.ETolerance_range;
import jsdai.SLayered_interconnect_complex_template_xim.ETemplate_location_in_structured_template_transform;
import jsdai.SPart_template_shape_with_parameters_xim.EPart_template_shape_model;
import jsdai.SPhysical_unit_shape_with_parameters_xim.EPhysical_unit_shape_model;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SProduct_property_representation_schema.EShape_representation;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SShape_property_assignment_xim.CShape_description_association;
import jsdai.SShape_property_assignment_xim.EItem_shape;
import jsdai.SShape_property_assignment_xim.EShape_description_association;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author Giedrius
 * 
 *	Removed ARM types after checking more strickt usage constraints in AP210 schema.
 *  So far I was searching for particular patterns - so that I know that ARM type
 *  is already participating in some attribute mapping, so it is garbage and has to be removed
 */
public class AP210SpecificGarbageCleaner {
	/**
	 * 
	 * @param model
	 * @throws SdaiException
	 */
	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		//1) Check Shape_representations. Logic - 
		// if we recognize it as part of some known mapping pattern - delete it.
//		long time = System.currentTimeMillis();
		int count = 0;
		AEntity instancesToRemove = models.getInstances(CShape_representation.definition);
		for(int index=1; index<=instancesToRemove.getMemberCount();){
			EShape_representation instanceToRemove = (EShape_representation)
				instancesToRemove.getByIndexEntity(index);
			// we want to skip Tolerance_range, because it has items
			// derived to ? and thus we would get an exception
			// on testItems (see bug #2313 for test case)
			boolean isToleranceRange = instanceToRemove instanceof ETolerance_range;
			// a) Template_location_in_structured_template_transform.reference_location
			if(!isToleranceRange && instanceToRemove.testItems(null)) {
				ARepresentation_item items = instanceToRemove.getItems(null);
				if(items.getMemberCount() == 1){
					if(items.getByIndex(1) instanceof ETemplate_location_in_structured_template_transform){
						importer.logMessage(" Deleting "+instanceToRemove);
						instanceToRemove.deleteApplicationInstance();
						count++;
						continue;
					}
				}
			}
			index++;
		}		
		// System.out.println(" AP210 Garbage A "+(System.currentTimeMillis()-time)/1000+" seconds. Deleted "+count);
		//2) Check Shape_description_associations. Logic - 
		// if referred representation is PUPS,.... than it already have a link to its definition, so SDA is redundant
		instancesToRemove = models.getInstances(CShape_description_association.definition);
		for(int index=1; index<=instancesToRemove.getMemberCount();){
			EShape_description_association instanceToRemove = (EShape_description_association)
				instancesToRemove.getByIndexEntity(index);
			// a) If we already unset one of mandatory attributes in some of the cleaners - this entity is garbage and we can delete it
			if((!instanceToRemove.testRepresented_characteristic(null))||
			  (!instanceToRemove.testRepresentation(null))){
				importer.logMessage(" Deleting "+instanceToRemove);
				instanceToRemove.deleteApplicationInstance();
				continue;
			}
			// b) Further check for particular patterns
			ERepresentation er = instanceToRemove.getRepresentation(null);
			EEntity characteristic = instanceToRemove.getRepresented_characteristic(null);
			if(!(characteristic instanceof EItem_shape)){
				index++;
				continue;
			}
			EItem_shape eis = (EItem_shape)characteristic;
			EEntity describedElement = eis.getDescribed_element(null);
			// Case a) Physical_unit_shape_model -> PVD
			if(er instanceof EPhysical_unit_shape_model){
				EPhysical_unit_shape_model epusm = (EPhysical_unit_shape_model)er;
				if(epusm.testShape_characterized_definition(null)){
					// Now we are 100% sure that SDA is redundant
					if(epusm.getShape_characterized_definition(null) == describedElement){
						importer.logMessage(" Deleting "+instanceToRemove);
						instanceToRemove.deleteApplicationInstance();
						continue;
					}
				}
			}
			// Case b) Physical_unit_shape_model -> PVD
			if(er instanceof EPart_template_shape_model){
				EPart_template_shape_model eptsm = (EPart_template_shape_model)er;
				if(eptsm.testShape_characterized_definition(null)){
					// Now we are 100% sure that SDA is redundant
					if(eptsm.getShape_characterized_definition(null).isMember(describedElement)){
						importer.logMessage(" Deleting "+instanceToRemove);
						instanceToRemove.deleteApplicationInstance();
						continue;
					}
				}
			}
			index++;
		}
		// System.out.println(" AP210 Garbage B "+(System.currentTimeMillis()-time)/1000+" seconds");
	}

}
