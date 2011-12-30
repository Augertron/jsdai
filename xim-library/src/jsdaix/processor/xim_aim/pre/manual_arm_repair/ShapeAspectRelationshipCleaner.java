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
import java.util.List;

import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.SShape_aspect_definition_schema.EComposite_shape_aspect;
import jsdai.SShape_aspect_definition_schema.EDatum;
import jsdai.SShape_aspect_definition_schema.EDatum_feature;
import jsdai.SShape_aspect_definition_schema.EDatum_target;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author Giedrius
 *
 *	Cleans garbage from items list (those entities, which are there mainly because of mapping and not as items for geometry
 */
public class ShapeAspectRelationshipCleaner {

	// List of magic strings, which means SAR is just serving as linking instance for attribute mapping and should be deleted
	private static List removableMagicStrings = new ArrayList();


	/**
	 *
	 * @param repo
	 * @throws SdaiException
	 */
	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		removableMagicStrings.add("model coordinate system");
		removableMagicStrings.add("affected plane association");
		removableMagicStrings.add("composing");
		// This may be expanded in the future
		AEntity instancesToClean = models.getExactInstances(CShape_aspect_relationship.definition);
		for(int index=1; index <= instancesToClean.getMemberCount();){
			EShape_aspect_relationship instanceToClean = (EShape_aspect_relationship)instancesToClean.getByIndexEntity(index);
			if(instanceToClean.testName(null)){
				String name = instanceToClean.getName(null);
				if(removableMagicStrings.contains(name)){
					deleteWithMessage(importer, instanceToClean);
					continue;
				}
			}
			if(instanceToClean.testRelating_shape_aspect(null)){
				EShape_aspect relating = instanceToClean.getRelating_shape_aspect(null);
				if(relating instanceof EComposite_shape_aspect){
					deleteWithMessage(importer, instanceToClean);
					continue;
				}
				EShape_aspect related = instanceToClean.getRelated_shape_aspect(null);
				if(related instanceof EDatum){
					if((relating instanceof EDatum_feature)||(relating instanceof EDatum_target)){
						deleteWithMessage(importer, instanceToClean);
						continue;
					}
				}
			}
			index++;
		}
	}


	private static void deleteWithMessage(Importer importer,
			EShape_aspect_relationship instanceToClean) throws SdaiException {
		importer.logMessage(" Deleting "+instanceToClean);
		instanceToClean.deleteApplicationInstance();
	}

}
