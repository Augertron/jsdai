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

import jsdai.SAic_associative_draughting_elements.EDraughting_model;
import jsdai.SAic_mechanical_design_geometric_presentation.EMechanical_design_geometric_presentation_representation;
import jsdai.SAic_mechanical_design_shaded_presentation.EMechanical_design_shaded_presentation_representation;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;
import jsdai.SConstruction_geometry_mim.AConstructive_geometry_representation_relationship;
import jsdai.SConstruction_geometry_mim.CConstructive_geometry_representation_relationship;
import jsdai.SConstruction_geometry_mim.EConstructive_geometry_representation_relationship;
import jsdai.SContextual_shape_positioning_xim.EGeometric_relationship_with_placement_transformation;
import jsdai.SGeneric_product_occurrence_xim.EDefinition_based_product_occurrence;
import jsdai.SLayered_interconnect_complex_template_xim.EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_representation_schema.AContext_dependent_shape_representation;
import jsdai.SProduct_property_representation_schema.AShape_definition_representation;
import jsdai.SProduct_property_representation_schema.CContext_dependent_shape_representation;
import jsdai.SProduct_property_representation_schema.CShape_definition_representation;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SProduct_property_representation_schema.CShape_representation_relationship;
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
		processRRBetweenDM(models, importer);
		processCGRR(models, importer);
		preossSRAndProductStructure(models, importer);
		removeAuxillaryMappingInstances(models, importer);

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

	private static void removeAuxillaryMappingInstances(ASdaiModel models, Importer importer)throws SdaiException {
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			ARepresentation_relationship arr = (ARepresentation_relationship) model.getExactInstances(CRepresentation_relationship.definition);
			ArrayList names = new ArrayList();
			names.add("general tolerance definition");
			for (SdaiIterator j = arr.createIterator(); j.next();) {
				ERepresentation_relationship err = arr.getCurrentMember(j);
				if(err.testName(null)){
					String name = err.getName(null);
					if(names.contains(name)){
						err.deleteApplicationInstance();
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

	/**
	 * This class should implement bug http://bugzilla.lksoft.lt/show_bug.cgi?id=3283.
	 */
	public static void processCGRR(ASdaiModel models, Importer importer)throws SdaiException {
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AConstructive_geometry_representation_relationship acgrr = (AConstructive_geometry_representation_relationship) model.getInstances(CConstructive_geometry_representation_relationship.definition);
			for (SdaiIterator j = acgrr.createIterator(); j.next();) {
				EConstructive_geometry_representation_relationship ecgrr = acgrr.getCurrentMember(j);
				ERepresentation er = ecgrr.getRep_2(null);
				ARepresentation_relationship arr = new ARepresentation_relationship();
				CRepresentation_relationship.usedinRep_2(null, er, models, arr);
				for (SdaiIterator k = arr.createIterator(); k.next();) {
					ERepresentation_relationship err = arr.getCurrentMember(k);
					if(err != ecgrr){
						if((err.getInstanceType() == CRepresentation_relationship.definition)||
						   (err.getInstanceType() == CShape_representation_relationship.definition)){
							if(err.getRep_1(null) == ecgrr.getRep_1(null)){
								importer.logMessage(" Deleting "+err+", which is duplicate of "+ecgrr);
								err.deleteApplicationInstance();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This class should implement bug http://bugzilla.lksoft.lt/show_bug.cgi?id=3394.
	 */
	public static void preossSRAndProductStructure(ASdaiModel models, Importer importer)throws SdaiException {
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AShape_definition_representation asdr = (AShape_definition_representation) model.getInstances(CShape_definition_representation.definition);
			top: for (SdaiIterator j = asdr.createIterator(); j.next();) {
				EShape_definition_representation esdr = asdr.getCurrentMember(j);
				if(esdr.testUsed_representation(null)){
					ERepresentation er = esdr.getUsed_representation(null);
					// our product structure points to something more specific than shape_representation
					if((!er.isInstanceOf(CShape_representation.class))&&(!er.isInstanceOf(CRepresentation.class))){
						ARepresentation ar = new ARepresentation();
						CRepresentation.usedinContext_of_items(null, er.getContext_of_items(null), models, ar);

						ARepresentation_relationship arr1 = new ARepresentation_relationship();
						CRepresentation_relationship.usedinRep_1(null, er, models, arr1);
						for(int c=1,cm=ar.getMemberCount(); c<=cm; c++){
							for(int r=1,rm=arr1.getMemberCount(); r<=rm; r++){
								ERepresentation_relationship err = arr1.getByIndex(r);
								ERepresentation erTemp = ar.getByIndex(c);
								if(err instanceof EPhysical_unit_keepout_shape_allocation_to_stratum_stack_armx){
									continue;
								}
								if(err.testRep_2(null)){
									if(err.getRep_2(null) == erTemp){
										// finally found it
										if(erTemp.isInstanceOf(CShape_representation.class)){
											esdr.setUsed_representation(null, erTemp);
											importer.logMessage(" Changed value for attribute Used_representation for entity "+esdr);
											continue top;
										}
									}
								}
							}
						}
						ARepresentation_relationship arr2 = new ARepresentation_relationship();
						CRepresentation_relationship.usedinRep_2(null, er, models, arr2);
						for(int c=1,cm=ar.getMemberCount(); c<=cm; c++){
							for(int r=1,rm=arr2.getMemberCount(); r<=rm; r++){
								ERepresentation_relationship err = arr2.getByIndex(r);
								ERepresentation erTemp = ar.getByIndex(c);
								if(err.getRep_1(null) == erTemp){
									// finally found it
									if(erTemp.isInstanceOf(EShape_representation.class)){
										esdr.setUsed_representation(null, erTemp);
										importer.logMessage(" Changed value for attribute Used_representation for entity "+esdr);
										continue top;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method should implement bug https://intranet.lksoft.net/bugzilla/show_bug.cgi?id=4032.
	 * Delete representation_relationships between draughting models
	 */
	public static void processRRBetweenDM(ASdaiModel models, Importer importer)throws SdaiException {
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			ARepresentation_relationship arr = (ARepresentation_relationship) model.getExactInstances(CRepresentation_relationship.definition);
			for (SdaiIterator j = arr.createIterator(); j.next();) {
				ERepresentation_relationship err = arr.getCurrentMember(j);
				if(!err.testRep_1(null)){
					continue;
				}
				if(!err.testRep_2(null)){
					continue;
				}
				ERepresentation er1 = err.getRep_1(null);
				ERepresentation er2 = err.getRep_2(null);
				if((er1 instanceof EDraughting_model)||
					(er1 instanceof EMechanical_design_geometric_presentation_representation)||
					(er1 instanceof EMechanical_design_shaded_presentation_representation)){
					if((er2 instanceof EDraughting_model)||
						(er2 instanceof EMechanical_design_geometric_presentation_representation)||
						(er2 instanceof EMechanical_design_shaded_presentation_representation)){
						importer.logMessage(" Deleting "+err+", as incorrect link between 2 styling models ");
						err.deleteApplicationInstance();
					}					
				}
			}
		}
	}
	
}
