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
import java.util.List;

import jsdai.SDocument_and_version_identification_xim.CDocument_version;
import jsdai.SDocument_and_version_identification_xim.EDocument_armx;
import jsdai.SDocument_and_version_identification_xim.EDocument_version;
import jsdai.SDocument_assignment_xim.ADocument_assignment;
import jsdai.SDocument_assignment_xim.CDocument_assignment;
import jsdai.SDocument_assignment_xim.EDocument_assignment;
import jsdai.SDocument_definition_xim.EDocument_definition;
import jsdai.SDocument_schema.ADocument;
import jsdai.SDocument_schema.EDocument;
import jsdai.SPart_and_version_identification_xim.CPart_version;
import jsdai.SPart_and_version_identification_xim.EPart;
import jsdai.SPart_and_version_identification_xim.EPart_version;
import jsdai.SPart_view_definition_xim.EPart_view_definition;
import jsdai.SProduct_breakdown_xim.CBreakdown_version;
import jsdai.SProduct_breakdown_xim.EBreakdown;
import jsdai.SProduct_breakdown_xim.EBreakdown_element;
import jsdai.SProduct_breakdown_xim.EBreakdown_element_definition;
import jsdai.SProduct_breakdown_xim.EBreakdown_version;
import jsdai.SProduct_definition_schema.CProduct_definition;
import jsdai.SProduct_definition_schema.CProduct_definition_with_associated_documents;
import jsdai.SProduct_definition_schema.EProduct;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition_formation;
import jsdai.SProduct_definition_schema.EProduct_definition_with_associated_documents;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author evita
 * 
 *  Part_view_definition mapping requires the following constraint:
 *  product_definition  {product_definition.frame_of_reference -> product_definition_context
 *			   product_definition_context <= application_context_element
 *		   application_context_element.name = 'part definition'}
 *
 *
 *  Some of Catia v.5 files missed required constraint, so mapping (upgrade from 
 *  product_definition to Part_view_definition) is performed manually.
 *    
 *  Document_definition and Breakdown_element_definition mapping in missing contraint
 *  case is covered as well.
 *  
 *  Test file: jsdai\Step21\EDAG\Catia v.5 2006-11\daru.stp
 */
public class DefinitionsRepair {

	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		List productDefinitionList = getDefinitions(models);
		for (Iterator it = productDefinitionList.iterator(); it.hasNext(); ) {
			EProduct_definition productDefinition = (EProduct_definition) it.next();
			repairDocuments(productDefinition, models, importer);
			repairType(productDefinition, importer);
		}
	}

	private static void repairDocuments(EProduct_definition productDefinition,
			ASdaiModel domain, Importer importer) throws SdaiException {

		if (!(productDefinition instanceof EProduct_definition_with_associated_documents)) {
			return;
		}
		
		EProduct_definition_with_associated_documents pdwss = (EProduct_definition_with_associated_documents)
			productDefinition;
		if (!pdwss.testDocumentation_ids(null)) {
			return;
		}
		
		AEntity assignedDocuments = getAssignedDocuments(pdwss, domain, importer);
		SdaiModel model = pdwss.findEntityInstanceSdaiModel();
		
		ADocument documents = pdwss.getDocumentation_ids(null);
		for (SdaiIterator i = documents.createIterator(); i.next();) {
			EDocument doc = documents.getCurrentMember(i);
			if (!assignedDocuments.isMember(doc)) {
				EDocument_assignment da = (EDocument_assignment) model.createEntityInstance(
					CDocument_assignment.definition);
				da.setAssigned_document_x(null, doc);
				da.createIs_assigned_to(null).addUnordered(pdwss);
				da.setRole_x(null, "");
				importer.logMessage(" Created new instance "+da);
			}
		}
	}
	
	private static AEntity getAssignedDocuments(EProduct_definition productDefinition,
		ASdaiModel domain, Importer importer) throws SdaiException {
		
		AEntity assignedDocuments = new AEntity();
		
		ADocument_assignment aDa = new ADocument_assignment();
		CDocument_assignment.usedinIs_assigned_to(null, productDefinition, domain, aDa);
		for (SdaiIterator i = aDa.createIterator(); i.next();) {
			EDocument_assignment eDa = aDa.getCurrentMember(i);
			if (eDa.testAssigned_document_x(null)) {
				EEntity eDoc = eDa.getAssigned_document_x(null);
				if (!assignedDocuments.isMember(eDoc)) {
					assignedDocuments.addUnordered(eDoc);
					importer.logMessage(" Added "+eDoc+" to assigned documents ");
				}
			}
		}
		
		return assignedDocuments;
	}

	private static void repairType(EProduct_definition productDefinition, Importer importer)
		throws SdaiException {

		if (!productDefinition.testFormation(null)) {
			return;
		}

		EProduct_definition_formation version = productDefinition.getFormation(null);
		if (!version.testOf_product(null)) {
			return;
		}

		EProduct product = version.getOf_product(null);
		if (product instanceof EPart) {
			productDefinition.findEntityInstanceSdaiModel().substituteInstance(productDefinition, EPart_view_definition.class);
			if (!(version instanceof EPart_version)) {
				String message = " Changed "+version;
				EEntity instance = version.findEntityInstanceSdaiModel().substituteInstance(version,
					CPart_version.definition);
				importer.logMessage(message+" to "+instance);
			}
		} else if (product instanceof EDocument_armx) {
			productDefinition.findEntityInstanceSdaiModel().substituteInstance(productDefinition, EDocument_definition.class);
			if (!(version instanceof EDocument_version)) {
				String message = " Changed "+version;
				EEntity instance = version.findEntityInstanceSdaiModel().substituteInstance(version,
					CDocument_version.definition);
				importer.logMessage(message+" to "+instance);
			}
		} else if (product instanceof EBreakdown || product instanceof EBreakdown_element) {
			productDefinition.findEntityInstanceSdaiModel().substituteInstance(productDefinition, EBreakdown_element_definition.class);
			if (!(version instanceof EBreakdown_version)) {
				String message = " Changed "+version;
				EEntity instance = version.findEntityInstanceSdaiModel().substituteInstance(version,
					CBreakdown_version.definition);
				importer.logMessage(message+" to "+instance);
			}
		}
	}

	private static List getDefinitions(ASdaiModel models)
		throws SdaiException {
		
		// perhaps we should get all non-XIM definitions

		AEntity productDefinitions = models.getExactInstances(CProduct_definition.class);
		// walkaround because getExactInstances is living aggregate and not ok in this case
		ArrayList productDefinitionList = new ArrayList();
		for (SdaiIterator it = productDefinitions.createIterator(); it.next(); ) {
			productDefinitionList.add((EProduct_definition) productDefinitions.getCurrentMemberEntity(it));
		}
		productDefinitions = models.getExactInstances(CProduct_definition_with_associated_documents.class);
		for (SdaiIterator it = productDefinitions.createIterator(); it.next(); ) {
			productDefinitionList.add((EProduct_definition) productDefinitions.getCurrentMemberEntity(it));
		}
		return productDefinitionList;
	}
}
