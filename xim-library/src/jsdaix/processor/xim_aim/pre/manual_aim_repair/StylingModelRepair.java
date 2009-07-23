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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jsdai.SAic_associative_draughting_elements.ADraughting_model;
import jsdai.SAic_associative_draughting_elements.CDraughting_model;
import jsdai.SAic_associative_draughting_elements.EDraughting_model;
import jsdai.SAic_mechanical_design_geometric_presentation.AMechanical_design_geometric_presentation_representation;
import jsdai.SAic_mechanical_design_geometric_presentation.CMechanical_design_geometric_presentation_representation;
import jsdai.SAic_mechanical_design_geometric_presentation.EMechanical_design_geometric_presentation_representation;
import jsdai.SAic_mechanical_design_shaded_presentation.AMechanical_design_shaded_presentation_representation;
import jsdai.SAic_mechanical_design_shaded_presentation.CMechanical_design_shaded_presentation_representation;
import jsdai.SAic_mechanical_design_shaded_presentation.EMechanical_design_shaded_presentation_representation;
import jsdai.SDraughting_element_schema.AAnnotation_plane;
import jsdai.SDraughting_element_schema.ADraughting_callout;
import jsdai.SDraughting_element_schema.CAnnotation_plane;
import jsdai.SDraughting_element_schema.CDraughting_callout;
import jsdai.SDraughting_element_schema.EAnnotation_plane;
import jsdai.SDraughting_element_schema.EDraughting_callout;
import jsdai.SPresentation_appearance_schema.APresentation_style_select;
import jsdai.SPresentation_appearance_schema.AStyled_item;
import jsdai.SPresentation_appearance_schema.CPresentation_style_assignment;
import jsdai.SPresentation_appearance_schema.CStyled_item;
import jsdai.SPresentation_appearance_schema.ENull_style;
import jsdai.SPresentation_appearance_schema.EPresentation_style_assignment;
import jsdai.SPresentation_appearance_schema.EStyled_item;
import jsdai.SPresentation_definition_schema.AAnnotation_occurrence;
import jsdai.SPresentation_definition_schema.AText_literal;
import jsdai.SPresentation_definition_schema.CAnnotation_occurrence;
import jsdai.SPresentation_definition_schema.CText_literal;
import jsdai.SPresentation_definition_schema.EAnnotation_occurrence;
import jsdai.SPresentation_definition_schema.EText_literal;
import jsdai.SProduct_property_representation_schema.EShape_representation;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.EMapped_item;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ERepresentation_map;
import jsdai.SRepresentation_schema.ERepresentation_relationship;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author Giedrius Liutkus
 * Various bug fixes for CAX-IF files on styling - mainly draughting_models, etc.
 *
 */
public class StylingModelRepair {

	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		changeStylingModelType(models, importer);
		mappedItemRepair(models, importer);
	}

	public static void runAfterMapping(ASdaiModel models, Importer importer) throws SdaiException {
		// It is important this methods falls after mappedItemRepair
		annotationStuffCleaningRepair(models, importer);
	}


	private static void changeStylingModelType(ASdaiModel models, Importer importer)throws SdaiException {
		AMechanical_design_geometric_presentation_representation items =
			(AMechanical_design_geometric_presentation_representation) models.getExactInstances(CMechanical_design_geometric_presentation_representation.definition);
		for (SdaiIterator it = items.createIterator(); it.next(); ) {
			EMechanical_design_geometric_presentation_representation item = items.getCurrentMember(it);
			EEntity instance = item.findEntityInstanceSdaiModel().substituteInstance(item, CDraughting_model.definition);
			importer.logMessage("Changed Mechanical_design_geometric_presentation_representation to "+instance);
		}

		AMechanical_design_shaded_presentation_representation items2 =
			(AMechanical_design_shaded_presentation_representation) models.getExactInstances(CMechanical_design_shaded_presentation_representation.definition);
		for (SdaiIterator it = items2.createIterator(); it.next(); ) {
			EMechanical_design_shaded_presentation_representation item = items2.getCurrentMember(it);
			EEntity instance = item.findEntityInstanceSdaiModel().substituteInstance(item, CDraughting_model.definition);
			importer.logMessage("Changed Mechanical_design_shaded_presentation_representation to "+instance);
		}

	}

	// Remove Annotation_occurrences, callouts and place them into associated DMs
	private static void annotationStuffCleaningRepair(ASdaiModel models, Importer importer)throws SdaiException {
		// Draughting_callouts
		ADraughting_callout adc = (ADraughting_callout) models.getInstances(CDraughting_callout.definition);
		for (SdaiIterator it = adc.createIterator(); it.next(); ) {
			EDraughting_callout item = adc.getCurrentMember(it);
			cleanUpStyledItem(item, models, importer);
			cleanCallouts(item, models, importer);
		}

		// Annotation_plane
		AAnnotation_plane aap = (AAnnotation_plane) models.getInstances(CAnnotation_plane.definition);
		for (SdaiIterator it = aap.createIterator(); it.next(); ) {
			EAnnotation_plane item = aap.getCurrentMember(it);
			cleanUpStyledItem(item, models, importer);
		}

		// Annotation_occurrences
		AAnnotation_occurrence items = (AAnnotation_occurrence) models.getExactInstances(CAnnotation_occurrence.definition);
		for (SdaiIterator it = items.createIterator(); it.next(); ) {
			EAnnotation_occurrence item = items.getCurrentMember(it);
			cleanUpStyledItem(item, models, importer);
		}
		// Text_literal
		AText_literal ati = (AText_literal) models.getInstances(CText_literal.definition);
		for (SdaiIterator it = ati.createIterator(); it.next(); ) {
			EText_literal item = ati.getCurrentMember(it);
			cleanUpItem(item, models, importer);
		}
	}

	// Known problem - that callouts are directly placed in Shape_representations.
	// do no allow this at all
	private static void cleanCallouts(EDraughting_callout item, ASdaiModel models, Importer importer)throws SdaiException{
		// Here we will support only case when same items are in few reps, than remove it from SR
		ARepresentation reps = new ARepresentation();
		CRepresentation.usedinItems(null, item, models, reps);
		for(int i=1;i<=reps.getMemberCount();i++){
			ERepresentation er = reps.getByIndex(i);
			if(er instanceof EShape_representation){
				er.getItems(null).removeUnordered(item);
				importer.errorMessage(" Removing callout from shape_rep "+item+" from "+er);
			}
		}
	}

	private static void cleanUpItem(ERepresentation_item item, ASdaiModel models, Importer importer)throws SdaiException{
		// Here we will support only case when same items are in few reps, than remove it from SR
		ARepresentation reps = new ARepresentation();
		CRepresentation.usedinItems(null, item, models, reps);
		AStyled_item asi = new AStyled_item();
		CStyled_item.usedinItem(null, item, models, asi);
		// only one or less users - nothing to do
		if(reps.getMemberCount()+asi.getMemberCount() <=1){
			return;
		}
		boolean stillHasUser=asi.getMemberCount()>0;
		for(int i=1;i<=reps.getMemberCount();i++){
			ERepresentation er = reps.getByIndex(i);
			if((stillHasUser)||(i<reps.getMemberCount())){
				er.getItems(null).removeUnordered(item);
				importer.errorMessage(" Removing invalid item "+item+" from "+er);
			}
		}
	}

	private static void cleanUpStyledItem(ERepresentation_item item, ASdaiModel models, Importer importer)throws SdaiException{
		// Here we will support only case when same items are in few reps, than remove it from SR
		ARepresentation reps = new ARepresentation();
		CRepresentation.usedinItems(null, item, models, reps);
		// Indirect reps have even higher priority than regular ones
		Set indirectReps = collectAllIndirectReps(item, models);
		if(reps.getMemberCount()+indirectReps.size() <=1){
			return;
		}

		boolean stillHasShape=false;
		Set dms = new HashSet();
		for(int i=1;i<=reps.getMemberCount();i++){
			ERepresentation er = reps.getByIndex(i);
			if(er instanceof EDraughting_model){
				stillHasShape=true;
				dms.add(er);
			}else{
				if((stillHasShape)||(i<reps.getMemberCount())){
					er.getItems(null).removeUnordered(item);
					importer.errorMessage(" Removing duplicate item "+item+" from "+er);
				}
			}
		}
		dms.addAll(indirectReps);
		// Determine parents
		HashMap parent2Child = new HashMap();
		for(Iterator it1 = dms.iterator(); it1.hasNext();){
			EDraughting_model dm1 = (EDraughting_model)it1.next();
			Set child = (Set)parent2Child.get(dm1);
			if(child == null){
				child = new HashSet();
				parent2Child.put(dm1, child);
			}
			for(Iterator it2 = dms.iterator(); it2.hasNext();){
				EDraughting_model dm2 = (EDraughting_model)it2.next();
				if(dm1 == dm2){
					continue;
				}
				if(isParent(dm1, dm2, item, models)){
					child.add(dm2);
				}
			}
		}
		for(Iterator it1 = dms.iterator(); it1.hasNext();){
			EDraughting_model dm1 = (EDraughting_model)it1.next();
			Set child = (Set)parent2Child.get(dm1);
			if(child.size() > 0){
				removeItemFromModelIfNeeded(item, dm1, indirectReps, importer, models);
				// We have the same item in few DMs, so keep it only in one
				if(child.size() > 1){
					boolean oneKept=false;
					for(Iterator it2 = child.iterator(); it2.hasNext();){
						EDraughting_model dm2 = (EDraughting_model)it2.next();
						if(oneKept){
							removeItemFromModelIfNeeded(item, dm2, indirectReps, importer, models);
						}else{
							oneKept = true;
						}
					}
				}
			}
		}
/*
		// if still we have the same occurrence in few dms - need to check if they are linked with rep_rel in addition
		List<EDraughting_model> dmsToSkip = new ArrayList<EDraughting_model>();
		top: for(EDraughting_model dm1 : dms){
			for(EDraughting_model dm2 : dms){
				if(dm1 == dm2){
					continue;
				}
				if(dmsToSkip.contains(dm2)){
					continue;
				}
				Set<EDraughting_model> child = parent2Child.get(dm1);
				if(child.contains(dm2)){
					System.err.println(dm1+" in ? "+item);
					dm1.getItems(null).removeUnordered(item);
					dmsToSkip.add(dm1);
					continue top;
				}
			}
		}*/
		// Now likely most complex case - http://bugzilla.lksoft.lt/show_bug.cgi?id=3095#c3
		// When Annotation_occurrence is placed in few children of the same parent
	}

	private static Set collectAllIndirectReps(ERepresentation_item item, ASdaiModel models)throws SdaiException {
		Set result = new HashSet();
		// Go up and also check paths via callouts and planes
		ADraughting_callout adc = new ADraughting_callout();
		CDraughting_callout.usedinContents(null, item, models, adc);
		for(int i=1,n=adc.getMemberCount(); i<=n; i++){
			EDraughting_callout edc = adc.getByIndex(i);
			ARepresentation ar = new ARepresentation();
			CRepresentation.usedinItems(null, edc, models, ar);
			for(int j=1,r=ar.getMemberCount(); j<=r; j++){
				ERepresentation er = ar.getByIndex(j);
				if(er instanceof EDraughting_model){
					result.add(er);
				}
			}
		}
		// planes
		AAnnotation_plane aap = new AAnnotation_plane();
		CAnnotation_plane.usedinElements(null, item, models, aap);
		for(int i=1,n=aap.getMemberCount(); i<=n; i++){
			EAnnotation_plane eap = aap.getByIndex(i);
			ARepresentation ar = new ARepresentation();
			CRepresentation.usedinItems(null, eap, models, ar);
			for(int j=1,r=ar.getMemberCount(); j<=r; j++){
				ERepresentation er = ar.getByIndex(j);
				if(er instanceof EDraughting_model){
					result.add(er);
				}
			}
		}
		return result;
	}

	private static void removeItemFromModelIfNeeded(ERepresentation_item item,
			EDraughting_model dm1, Set indirectReps, Importer importer, ASdaiModel domain) throws SdaiException {
		ARepresentation_item items = dm1.getItems(null);
		if(items.isMember(item)){
			items.removeUnordered(item);
			importer.errorMessage(" Removing duplicate item "+item+" from "+dm1);
		}else{
			// Remove from callout or annotation_plane
			// TODO - implement this case when example available
			if(indirectReps.contains(dm1)){
				boolean actionDone = false;
				// Case 1 - via annotation_plane
				AAnnotation_plane aap = new AAnnotation_plane();
				CAnnotation_plane.usedinElements(null, item, domain, aap);
				for(int i=1,count=aap.getMemberCount(); i<=count; i++){
					EAnnotation_plane eap = aap.getByIndex(i);
					if(items.isMember(eap)){
						eap.getElements(null).removeUnordered(item);
						actionDone = true;
						importer.errorMessage(" Removing duplicate item "+item+" from "+eap);
					}
					if(eap.getElements(null).getMemberCount() == 0){
						eap.deleteApplicationInstance();
						importer.logMessage(" Deleting "+eap+" which no longer have any elements ");
					}
				}
				// Case 2 - via draughting_callout
				ADraughting_callout adc = new ADraughting_callout();
				CDraughting_callout.usedinContents(null, item, domain, adc);
				for(int i=1,count=adc.getMemberCount(); i<=count; i++){
					EDraughting_callout edc = adc.getByIndex(i);
					if(items.isMember(edc)){
						edc.getContents(null).removeUnordered(item);
						actionDone = true;
						importer.errorMessage(" Removing duplicate item "+item+" from "+edc);
					}
					if(edc.getContents(null).getMemberCount() == 0){
						edc.deleteApplicationInstance();
						importer.logMessage(" Deleting "+edc+" which no longer have any elements ");
					}
				}
				if(!actionDone){
					importer.errorMessage(" Unimplemented case of having annotion_element repeated in complex structures "+item+" indirectly in "+dm1);
				}
			}
		}
	}

	// We check for simple pattern here - DM <- RR -> DM, but we may need to extend it for more complex patterns
	private static boolean isParent(EDraughting_model dm1, EDraughting_model dm2, ERepresentation_item item, ASdaiModel domain)throws SdaiException {
		ARepresentation_relationship arr = new ARepresentation_relationship();
		CRepresentation_relationship.usedinRep_2(null, dm1, domain, arr);
		for(int i=1,n=arr.getMemberCount(); i<=n; i++){
			ERepresentation_relationship err = arr.getByIndex(i);
			if(err.testRep_1(null)){
				if(err.getRep_1(null) == dm2){
					return true;
				}
			}
		}

		return false;
	}

	// Change pattern DM -> MI -> RM -> SR to DM -> RC <- SR and DM -> SI -> MI -> RM -> SR.
	// Delete MI/RM if otherwise unused.
	// Possible problems - if DM have few MIs. currently this is unchecked
	private static void mappedItemRepair(ASdaiModel models, Importer importer)throws SdaiException {
		ADraughting_model adm = (ADraughting_model) models.getExactInstances(CDraughting_model.definition);
		for (SdaiIterator it = adm.createIterator(); it.next(); ) {
			EDraughting_model edm = adm.getCurrentMember(it);
			if(edm.testItems(null)){
				ARepresentation_item items = edm.getItems(null);
				for(int i=1;i<=items.getMemberCount();){
					ERepresentation_item item = items.getByIndex(i);
					if(item instanceof EMapped_item){
						EMapped_item emi = (EMapped_item)item;
						if(emi.testMapping_source(null)){
							ERepresentation_map erm = emi.getMapping_source(null);
							if(erm.testMapped_representation(null)){
								ERepresentation er = erm.getMapped_representation(null);
								if(er.testContext_of_items(null)){
									ERepresentation_context erc = edm.getContext_of_items(null);
									edm.setContext_of_items(null, er.getContext_of_items(null));
									items.removeUnordered(emi);
									// New approach - add styled_item in between
									SdaiModel model = emi.findEntityInstanceSdaiModel();
									// SI
									EStyled_item esi = (EStyled_item)model.createEntityInstance(CStyled_item.definition);
									esi.setItem(null, emi);
									items.addUnordered(esi);
									// PSA
									EPresentation_style_assignment epsa = (EPresentation_style_assignment)model.createEntityInstance(CPresentation_style_assignment.definition);
									APresentation_style_select apss = epsa.createStyles(null);
									apss.addUnordered(ENull_style.NULL, (ENull_style)null);
									esi.createStyles(null).addUnordered(epsa);
									importer.errorMessage(" Change DM -> MI -> RM -> SR to DM -> SI -> MI -> RM -> SR. Added new SI "+esi);
/*									importer.logMessage(" Removing Mapped_Item and related stuff "+emi+" from "+edm);
									LangUtils.deleteInstanceIfUnused(models, emi);
									LangUtils.deleteInstanceIfUnused(models, erm);
									LangUtils.deleteInstanceIfUnused(models, erc);*/
									continue;
								}
							}
						}
					}
					i++;
				}
			}
		}
	}
}
