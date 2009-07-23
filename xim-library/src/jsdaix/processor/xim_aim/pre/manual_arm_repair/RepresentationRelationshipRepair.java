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

import jsdai.SAic_associative_draughting_elements.EDraughting_model;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;
import jsdai.SContextual_shape_positioning_xim.EGeometric_relationship_with_placement_transformation;
import jsdai.SGeneric_product_occurrence_xim.EDefinition_based_product_occurrence;
import jsdai.SLayered_interconnect_complex_template_xim.EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_representation_schema.AContext_dependent_shape_representation;
import jsdai.SProduct_property_representation_schema.AShape_definition_representation;
import jsdai.SProduct_property_representation_schema.CContext_dependent_shape_representation;
import jsdai.SProduct_property_representation_schema.CShape_definition_representation;
import jsdai.SProduct_property_representation_schema.EContext_dependent_shape_representation;
import jsdai.SProduct_property_representation_schema.EShape_definition_representation;
import jsdai.SProduct_property_representation_schema.EShape_representation;
import jsdai.SProduct_property_representation_schema.EShape_representation_relationship;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_relationship;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * This class should implement bug http://bugzilla.lksoft.lt/show_bug.cgi?id=2931.
 */
public final class RepresentationRelationshipRepair {
	
	private RepresentationRelationshipRepair() { }
	
	public static void run(ASdaiModel models, Importer importer)
		throws SdaiException {
		
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			ARepresentation_relationship arr = (ARepresentation_relationship) model.getInstances(CRepresentation_relationship.definition);
			for (SdaiIterator j = arr.createIterator(); j.next();) {
				ERepresentation_relationship err = arr.getCurrentMember(j);
				if(err instanceof EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx){
					continue;
				}
				if((!err.testRep_1(null))||(!err.testRep_2(null))){
					continue;
				}
				if(err instanceof EShape_representation_relationship){
					ENext_assembly_usage_occurrence_relationship_armx enauor = getPdr((EShape_representation_relationship)err);
					// Check if rep_1 is pointing to component
					ERepresentation rep1 = err.getRep_1(null);
					ERepresentation rep2 = err.getRep_2(null);
					EProduct_definition componentFromShape = getPDFromShapeStructure(err, rep1);
					EProduct_definition assemblyFromShape = getPDFromShapeStructure(err, rep2);
					// Need to go more dangerous route as Assembly structure not available
					if(enauor == null){
						// When rep1 is pure shape_rep and attached to structure, while rep2 not 
						if((componentFromShape != null)&&(assemblyFromShape == null)){
							swap(err, rep1, rep2, importer);
						}
						continue;
					}
					
					EProduct_definition component = null; 
					EProduct_definition assembly = null;
					if(enauor.testRelated_view(null)){
						component = enauor.getRelated_view(null);
						if(component instanceof EDefinition_based_product_occurrence){
							EDefinition_based_product_occurrence edbpo = (EDefinition_based_product_occurrence)
								component;
							if(edbpo.testDerived_from(null)){
								component = edbpo.getDerived_from(null);
							}
						}
					}
					if(enauor.testRelating_view(null)){
						assembly = enauor.getRelating_view(null);
					}
					if((componentFromShape == component)&&(assemblyFromShape == assembly)){
						// fine match - do nothing
					}else if((componentFromShape == assembly)&&(assemblyFromShape == component)){
						// need to swap rep_1 and rep_2
						swap(err, rep1, rep2, importer);
					}else{
						importer.errorMessage(" Unsupported case for componentFromShape "+componentFromShape+" vs "+component+" assemblyFromShape "+assemblyFromShape+" vs "+assembly);
					}
				}else{
					ERepresentation er1 = err.getRep_1(null);
					ERepresentation er2 = err.getRep_2(null);
					// Special case for Draughting_models.
					// Logic - if Draughting_model is sharing context with Shape_representation
					// and it is linked via rep_rel with another DM, which is not sharing context
					// with any SR - the former DM is treated as 'parent' and the latter as 'child'
					
					if((er1 instanceof EDraughting_model)&&(er2 instanceof EDraughting_model)){
						boolean isFirstSharing = isSharingContextWithSR(models, er1.getContext_of_items(null));
						boolean isSecondSharing = isSharingContextWithSR(models, er2.getContext_of_items(null));
						if((isFirstSharing)&&(!isSecondSharing)){
							swap(err, er1, er2, importer);
						}
					}
				}
			}
		}
	}

	private static boolean isSharingContextWithSR(ASdaiModel models,
			ERepresentation_context erc) throws SdaiException {
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinContext_of_items(null, erc, models, ar);
		for(int r=1,n=ar.getMemberCount(); r<=n; r++){
			if(ar.getByIndex(r) instanceof EShape_representation){
				return true;
			}
		}
		return false;
	}

	private static void swap(ERepresentation_relationship err,
			ERepresentation rep1, ERepresentation rep2, Importer importer) throws SdaiException {
		importer.logMessage(" Swaping rep_1 and rep_2 for "+err);
		err.setRep_1(null, rep2);
		err.setRep_2(null, rep1);
		if(err instanceof EGeometric_relationship_with_placement_transformation){
			EGeometric_relationship_with_placement_transformation egrwpt = (EGeometric_relationship_with_placement_transformation)
				err;
			if((egrwpt.testSource(null))&&(egrwpt.testTarget(null))){
				EEntity source = egrwpt.getSource(null);
				EEntity target = egrwpt.getTarget(null);
				egrwpt.setSource(null, target);
				egrwpt.setTarget(null, source);
			}
		}
	}

	private static EProduct_definition getPDFromShapeStructure(ERepresentation_relationship err, ERepresentation rep)
			throws SdaiException {
		AShape_definition_representation aSdr = new AShape_definition_representation();
		CShape_definition_representation.usedinUsed_representation(null, rep, null, aSdr);
		if(aSdr.getMemberCount() > 0){
			EShape_definition_representation eSdr = aSdr.getByIndex(1);
			if(eSdr.testDefinition(null)){
				EEntity ee = eSdr.getDefinition(null);
				if(ee instanceof EProduct_definition_shape){
					EProduct_definition_shape epds = (EProduct_definition_shape)ee;
					EEntity component = epds.getDefinition(null);
					if(component instanceof EProduct_definition){
						return (EProduct_definition)component;
					}
				}
			}
		}
		return null;
	}
	
	private static ENext_assembly_usage_occurrence_relationship_armx getPdr(EShape_representation_relationship eSrr)
	throws SdaiException {
		AContext_dependent_shape_representation aCdsr = new AContext_dependent_shape_representation();
		CContext_dependent_shape_representation.usedinRepresentation_relation(null, eSrr, null, aCdsr);
		for (SdaiIterator i = aCdsr.createIterator(); i.next();) {
			EContext_dependent_shape_representation eCdsr = aCdsr.getCurrentMember(i);
			if (!eCdsr.testRepresented_product_relation(null)) {
				continue;
			}
			EProduct_definition_shape ePds = eCdsr.getRepresented_product_relation(null);
			if (!ePds.testDefinition(null)) {
				continue;
			}
			ENext_assembly_usage_occurrence_relationship_armx ePdr = (ENext_assembly_usage_occurrence_relationship_armx) ePds.getDefinition(null);
			if(ePdr.testRelating_view(null)){
				if (ePdr.testRelated_view(null)) {
					return ePdr;
				}
			}
		}
		return null;
	}
}
