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

import jsdai.SApplication_context_schema.CApplication_context;
import jsdai.SGeometry_schema.CGeometric_representation_context;
import jsdai.SManagement_resources_schema.CClassification_role;
import jsdai.SPerson_organization_schema.COrganization_role;
import jsdai.SPerson_organization_schema.CPerson_and_organization_role;
import jsdai.SProduct_property_definition_schema.CCharacterized_object;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SRepresentation_schema.CRepresentation_map;
import jsdai.SSecurity_classification_schema.CSecurity_classification_level;
import jsdai.SShape_property_assignment_xim.CContextual_item_shape;
import jsdai.SShape_property_assignment_xim.CItem_shape;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.util.LangUtils;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author Giedrius
 * 
 *	Removed AIM instances of provided types, which should not be in XIM file (like shape_aspect_relationship, property_definition etc.)
 */
public class AIMGarbageCleaner {
	/**
	 * 
	 * @param model
	 * @throws SdaiException
	 */
//	private static int countOfDeletions = 0; // TEMP - DEBUG
//	private static int rmUsersCount = 0;
	
	public static void run(ASdaiModel models, EEntity_definition[] typesToRemove, Importer importer) throws SdaiException {
		// long time = System.currentTimeMillis();		
		// System.out.println(" Remove "+typesToRemove.length);
		for (int t=0,count=typesToRemove.length; t<count; t++) {
			EEntity_definition typeToRemove = typesToRemove[t];
			deleteExactInstances(models, typeToRemove, importer);
		}
		// Special treatment for shape_representation
		AEntity instancesToRemove = models.getExactInstances(CShape_representation.definition);
		int index = 1;
		while(instancesToRemove.getMemberCount() > index-1){
			EEntity instanceToRemove = instancesToRemove.getByIndexEntity(index);
			boolean deleted = LangUtils.deleteInstanceIfUsageCountLessThan(models, instanceToRemove, 1);
			if(!deleted){
				index++;
			}
		}
		// Special treatment for shape_representation, classification_role
		// removeUnusedEntities(models, CApplied_classification_assignment.definition);
		// removeUnusedEntities(models, CShape_representation.definition);
		removeUnusedEntities(models, CClassification_role.definition, importer);
		removeUnusedEntities(models, CGeometric_representation_context.definition, importer);
		removeUnusedEntities(models, CApplication_context.definition, importer);
		removeUnusedEntities(models, CItem_shape.definition, importer);
		removeUnusedEntities(models, COrganization_role.definition, importer);
		removeUnusedEntities(models, CCharacterized_object.definition, importer);
		removeUnusedEntities(models, CContextual_item_shape.definition, importer);
		removeUnusedEntities(models, CPerson_and_organization_role.definition, importer);
		removeUnusedEntities(models, CSecurity_classification_level.definition, importer);
		// System.out.println(" Cleaning AIM F "+(System.currentTimeMillis()-time)/1000+" seconds");
		removeUnusedEntities(models, CRepresentation_map.definition, importer);
		// System.out.println(" Cleaning AIM G "+(System.currentTimeMillis()-time)/1000+" seconds");
		// Special treatment for Classification_role
		
	}

	/**
	 * @param schema
	 * @throws SdaiException
	 */
	private static void removeUnusedEntities(ASdaiModel models, EEntity_definition typeToRemove, Importer importer) throws SdaiException {
		AEntity instancesToRemove = models.getExactInstances(typeToRemove);
		// System.out.println(" instance count "+instancesToRemove.getMemberCount());
		int index = 1;
		while(instancesToRemove.getMemberCount() > index-1){
			EEntity instanceToRemove = instancesToRemove.getByIndexEntity(index);
			String message = "Deleted unused instance "+instanceToRemove;
			boolean deleted = LangUtils.deleteInstanceIfUsageCountLessThan(models, instanceToRemove, 1);
			if(!deleted){
				index++;
			}else{
				importer.logMessage(message);
			}
		}
	}

	/**
	 * @param models
	 * @param typeToRemove
	 * @throws SdaiException
	 */
	private static void deleteExactInstances(ASdaiModel models, EEntity_definition typeToRemove, Importer importer) throws SdaiException {
		AEntity instancesToRemove = models.getExactInstances(typeToRemove);
		int count = instancesToRemove.getMemberCount();
		// countOfDeletions += count;
		
		// System.out.println(typeToRemove.getName(null)+" remove "+instancesToRemove.getMemberCount());
		while(count > 0){
			EEntity instanceToRemove = instancesToRemove.getByIndexEntity(count--);
			importer.logMessage(" Deleting instance "+instanceToRemove);
			instanceToRemove.deleteApplicationInstance();
		}
		// System.out.println(" Deleting "+typeToRemove.getName(null)+" takes "+(System.currentTimeMillis()-time)+" ms "+count2);
		
	}
}
