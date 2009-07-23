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


import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.SShape_aspect_definition_schema.CDatum;
import jsdai.SShape_aspect_definition_schema.CDatum_feature;
import jsdai.dictionary.EEntity_definition;
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
	
	// List of type which if SAR points to - those SARs have to be preserved
	private static EEntity_definition[] allowedTargets = {CDatum.definition, CDatum_feature.definition};  
	
	
	/**
	 * 
	 * @param repo
	 * @throws SdaiException
	 */
	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		// This may be expanded in the future
		AEntity instancesToClean = models.getExactInstances(CShape_aspect_relationship.definition);
		for(int index=1; index <= instancesToClean.getMemberCount();){
			EShape_aspect_relationship instanceToClean = (EShape_aspect_relationship)instancesToClean.getByIndexEntity(index);
			if(instanceToClean.testRelated_shape_aspect(null)){
				EShape_aspect ea = instanceToClean.getRelated_shape_aspect(null);
				if(isAllowedType(ea)){
					index++;
					continue;
				}
			}
			if(instanceToClean.testRelating_shape_aspect(null)){
				EShape_aspect ea = instanceToClean.getRelating_shape_aspect(null);
				if(isAllowedType(ea)){
					index++;
					continue;
				}
			}
			importer.logMessage(" Deleting "+instanceToClean);
			instanceToClean.deleteApplicationInstance();
		}
	}


	private static boolean isAllowedType(EShape_aspect ea)throws SdaiException {
		for(int i=0,n=allowedTargets.length; i<n; i++){
			if(ea.isInstanceOf(allowedTargets[i])){
				return true;
			}
		}
		return false;
	}
}
