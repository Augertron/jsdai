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


import jsdai.dictionary.EEntity_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.SDocument_assignment_mim.AApplied_document_reference;
import jsdai.SDocument_assignment_mim.CApplied_document_reference;
import jsdai.SFabrication_technology_xim.APassage_technology_armx;
import jsdai.SFabrication_technology_xim.CPassage_technology_armx;
import jsdai.SMeasure_schema.CLength_measure_with_unit;
import jsdai.SMeasure_schema.CMass_measure_with_unit;
import jsdai.SMeasure_schema.CMeasure_with_unit;
import jsdai.SMeasure_schema.CVolume_measure_with_unit;
import jsdai.SMixed_complex_types.*;
import jsdai.SPart_template_xim.ATemplate_definition;
import jsdai.SPart_template_xim.CTemplate_definition;
import jsdai.SQualified_measure_schema.CMeasure_representation_item;
import jsdai.SRepresentation_schema.AGlobal_uncertainty_assigned_context;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.AUncertainty_assigned_representation;
import jsdai.SRepresentation_schema.CGlobal_uncertainty_assigned_context;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.CUncertainty_assigned_representation;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.EUncertainty_measure_with_unit;
import jsdai.SRequirement_decomposition_xim.APredefined_requirement_view_definition_armx;
import jsdai.SRequirement_decomposition_xim.CPredefined_requirement_view_definition_armx;
import jsdai.SMeasure_schema.CFrequency_measure_with_unit;
import jsdai.SMeasure_schema.CPower_measure_with_unit;

/**
 * @author Giedrius
 * 
 *	Removed AIM instances of provided types, which should not be in XIM file (like shape_aspect_relationship, property_definition etc.)
 */
public class MRIDowngrader {
	/**
	 * 
	 * @param models
	 * @throws SdaiException
	 */
	public static void run(ASdaiModel models) throws SdaiException {
		// This may be expanded in the future
		// 1) LMWU+MRI
		downgradeMRI(models, CLength_measure_with_unit$measure_representation_item.definition,
				CLength_measure_with_unit.definition);
		// 2) MMWU+MRI
		downgradeMRI(models, CMass_measure_with_unit$measure_representation_item.definition,
				CMass_measure_with_unit.definition);
		// 3) LMWU+UMWU
		AEntity instancesToRename = models.getExactInstances(CLength_measure_with_unit$uncertainty_measure_with_unit.definition);
		for(int index=1; index <= instancesToRename.getMemberCount();){
			EUncertainty_measure_with_unit instanceToRename = (EUncertainty_measure_with_unit)instancesToRename.getByIndexEntity(index);
			AGlobal_uncertainty_assigned_context users1 = new AGlobal_uncertainty_assigned_context();
			CGlobal_uncertainty_assigned_context.usedinUncertainty(null, instanceToRename, null, users1);
			if(users1.getMemberCount() > 0){
				index++;
				continue;
			}
			AUncertainty_assigned_representation users2 = new AUncertainty_assigned_representation();
			CUncertainty_assigned_representation.usedinUncertainty(null, instanceToRename, null, users2);
			if(users2.getMemberCount() > 0){
				index++;
				continue;
			}
			instanceToRename.findEntityInstanceSdaiModel().substituteInstance(instanceToRename, CLength_measure_with_unit.definition);
		}
		// 4) MRI
		downgradeMRI(models, CMeasure_representation_item.definition,
				CMeasure_with_unit.definition);
		// 5) FMWU+MRI
		downgradeMRI(models, CFrequency_measure_with_unit$measure_representation_item.definition,
				CFrequency_measure_with_unit.definition);
		// 6) PMWU+MRI
		downgradeMRI(models, CMeasure_representation_item$power_measure_with_unit.definition,
				CPower_measure_with_unit.definition);
		// 7) VMWU+MRI
		downgradeMRI(models, CMeasure_representation_item$volume_measure_with_unit.definition,
				CVolume_measure_with_unit.definition);
		
	}

	/**
	 * @param models
	 * @throws SdaiException
	 */
	private static void downgradeMRI(ASdaiModel models, EEntity_definition oldType,
			EEntity_definition newType) throws SdaiException {
		AEntity instancesToRename = models.getExactInstances(oldType);
		for(int index=1; index <= instancesToRename.getMemberCount();){
			ERepresentation_item instanceToRename = (ERepresentation_item)instancesToRename.getByIndexEntity(index);
			ARepresentation representations = new ARepresentation();
			CRepresentation.usedinItems(null, instanceToRename, null, representations);
			if(representations.getMemberCount() > 0){
				index++;
				continue;
			}
			APredefined_requirement_view_definition_armx requirements = new APredefined_requirement_view_definition_armx();
			CPredefined_requirement_view_definition_armx.usedinRequired_characteristic(null, instanceToRename, null, requirements);
			if(requirements.getMemberCount() > 0){
				index++;
				continue;
			}
			ATemplate_definition atd = new ATemplate_definition(); 			
			CTemplate_definition.usedinPhysical_characteristic(null, instanceToRename, null, atd);
			if(atd.getMemberCount() > 0){
				index++;
				continue;
			}
			AApplied_document_reference adr = new AApplied_document_reference(); 			
			CApplied_document_reference.usedinItems(null, instanceToRename, null, adr);
			if(adr.getMemberCount() > 0){
				index++;
				continue;
			}
			
			APassage_technology_armx apt = new APassage_technology_armx();
			CPassage_technology_armx.usedinAs_finished_passage_extent(null, instanceToRename, null, apt);
			if(apt.getMemberCount() > 0){
				index++;
				continue;
			}
			// System.err.println(" Downgrading "+instanceToRename);
			instanceToRename.findEntityInstanceSdaiModel().substituteInstance(instanceToRename, newType);
		}
	}
}
