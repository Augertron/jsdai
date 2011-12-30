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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jsdai.SAssociative_draughting_elements_mim.ADraughting_model_item_association;
import jsdai.SAssociative_draughting_elements_mim.CDraughting_model_item_association;
import jsdai.SAssociative_draughting_elements_mim.EDraughting_model_item_association;
import jsdai.SDimension_tolerance_xim.AAssociated_shape_element;
import jsdai.SDimension_tolerance_xim.AChain_based_associated_shape_element;
import jsdai.SDimension_tolerance_xim.ALocation_dimension;
import jsdai.SDimension_tolerance_xim.ASize_dimension;
import jsdai.SDimension_tolerance_xim.CAssociated_shape_element;
import jsdai.SDimension_tolerance_xim.CChain_based_associated_shape_element;
import jsdai.SDimension_tolerance_xim.CLocation_dimension;
import jsdai.SDimension_tolerance_xim.CSize_dimension;
import jsdai.SDimension_tolerance_xim.EAssociated_shape_element;
import jsdai.SDimension_tolerance_xim.EChain_based_associated_shape_element;
import jsdai.SDimension_tolerance_xim.ELocation_dimension;
import jsdai.SDimension_tolerance_xim.ESize_dimension;
import jsdai.SDocument_and_version_identification_xim.EDocument_armx;
import jsdai.SDocument_assignment_xim.ADocument_assignment;
import jsdai.SDocument_assignment_xim.CDocument_assignment;
import jsdai.SDocument_assignment_xim.EDocument_assignment;
import jsdai.SExternal_reference_schema.AExternal_source;
import jsdai.SExternal_reference_schema.CExternal_source;
import jsdai.SExternal_reference_schema.EExternal_source;
import jsdai.SGeometric_tolerance_xim.AGeometric_tolerance_armx;
import jsdai.SGeometric_tolerance_xim.CGeometric_tolerance_armx;
import jsdai.SGeometric_tolerance_xim.EGeometric_tolerance_armx;
import jsdai.SGeometry_schema.AGeometric_representation_item;
import jsdai.SGeometry_schema.EGeometric_representation_item;
import jsdai.SPart_template_xim.ETemplate_definition;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EPart_design_view;
import jsdai.SPhysical_unit_usage_view_xim.EPart_usage_view;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_representation_schema.AItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.AShape_definition_representation;
import jsdai.SProduct_property_representation_schema.CItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.CShape_definition_representation;
import jsdai.SProduct_property_representation_schema.EItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.EShape_definition_representation;
import jsdai.SProduct_property_representation_schema.EShape_representation;
import jsdai.SProperty_assignment_xim.EApplied_independent_property;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.FUsing_representations;
import jsdai.SShape_aspect_definition_schema.EDatum_feature;
import jsdai.SShape_composition_xim.CComposite_shape_aspect_armx;
import jsdai.SShape_composition_xim.EComposite_shape_aspect_armx;
import jsdai.SShape_property_assignment_mim.CGeometric_item_specific_usage;
import jsdai.SShape_property_assignment_mim.EGeometric_item_specific_usage;
import jsdai.SShape_tolerance_schema.ATolerance_zone;
import jsdai.SShape_tolerance_schema.ATolerance_zone_target;
import jsdai.SShape_tolerance_schema.CTolerance_zone;
import jsdai.SShape_tolerance_schema.ETolerance_zone;
import jsdai.SSupport_resource_schema.EIdentifier;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.Value;
import jsdai.util.LangUtils;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * This class should contain GDT specific repairs (modules Geomtric_dimension and Tolerance_dimension).
 * 1) First repair for now is for cases when mapping does not distinguish between unset($) and .FALSE. values.
 * When those attributes are mandatory - just collect unset values and make them false.
 */
public final class GDTRepair {

	private GDTRepair() { }

	public static void run(SdaiContext context, ASdaiModel models, Importer importer)throws SdaiException {
		unset2False(models, importer);
		createGISUFromOldPattern(context, models, importer);
		repairAssociatedShapeElement(models, importer);
		repairChainBasedASE(models, importer);
		cleanupExternally_defined_size_dimension(models, importer);
		repairDMIA(models, importer);
		repairToleranceZone(models, importer);
	}

	public static void repairToleranceZone(ASdaiModel models, Importer importer)throws SdaiException{
		ATolerance_zone atz = (ATolerance_zone)models.getExactInstances(CTolerance_zone.definition);
		List zones2Delete = new ArrayList();
		for (SdaiIterator j = atz.createIterator(); j.next();) {
			ETolerance_zone etz = atz.getCurrentMember(j);
			ATolerance_zone_target atzt = etz.getDefining_tolerance(null);
			if(atzt.getMemberCount() == 1){
				EEntity ee = atzt.getByIndex(1);
				if(ee instanceof EGeometric_tolerance_armx){
					EGeometric_tolerance_armx egta = (EGeometric_tolerance_armx)ee;
					egta.moveUsersFrom(etz);
					zones2Delete.add(etz);
				}
			}
		}
		for(int i=0, count=zones2Delete.size(); i<count; i++){
			EEntity ee = (EEntity)zones2Delete.get(i);
			importer.logMessage(" Deleting "+ee);
			ee.deleteApplicationInstance();
		}
	}
	
	private static void repairDMIA(ASdaiModel models, Importer importer)throws SdaiException{
		ADraughting_model_item_association admia = (ADraughting_model_item_association)models.getInstances(CDraughting_model_item_association.definition);
		List edmias2Delete = new ArrayList();
		for (SdaiIterator j = admia.createIterator(); j.next();) {
			EDraughting_model_item_association edmia = admia.getCurrentMember(j);
			if(edmia.testDefinition(null)){
				EEntity ee = edmia.getDefinition(null);
				if(ee instanceof EShape_aspect){
					AGeometric_tolerance_armx agta = new AGeometric_tolerance_armx();
					CGeometric_tolerance_armx.usedinApplied_to(null, ee, models, agta);
					boolean changed = false;
					for (SdaiIterator i = agta.createIterator(); i.next();) {
						EDraughting_model_item_association edmia2 = (EDraughting_model_item_association)
							edmia.copyApplicationInstance(edmia.findEntityInstanceSdaiModel());
						edmia2.setDefinition(null, agta.getCurrentMemberEntity(i));
						changed = true;
						importer.logMessage(" Substituting "+edmia+" by "+edmia2);
					}
					if(changed){
						// Delete only if it is not datum feature as for datum feature we need to have that link
						if(!(ee instanceof EDatum_feature)){
							edmias2Delete.add(edmia);
						}
					}
				}
			}
		}
		for(int i=0, count=edmias2Delete.size(); i<count; i++){
			EEntity ee = (EEntity)edmias2Delete.get(i);
			ee.deleteApplicationInstance();
		}
	}

	private static void cleanupExternally_defined_size_dimension(ASdaiModel models, Importer importer)throws SdaiException {
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AExternal_source aes = (AExternal_source)model.getInstances(CExternal_source.definition);
			for (SdaiIterator j = aes.createIterator(); j.next();) {
				EExternal_source ees = aes.getCurrentMember(j);
				if(ees.testSource_id(null) == EExternal_source.sSource_idIdentifier){
					if(ees.getSource_id(null, (EIdentifier)null).equals("external size dimension specification")){
						ees.deleteApplicationInstance();
					}
				}
			}
		}
	}

	// Go from old approach to new:
	// http://www.wikistep.org/index.php/Associativity_between_GD%26T_and_its_presentation
	private static void createGISUFromOldPattern(SdaiContext context, ASdaiModel models, Importer importer)throws SdaiException {
		AShape_definition_representation asdr = (AShape_definition_representation) models.getInstances(CShape_definition_representation.definition);
		for (Iterator i = aggregateToList(asdr).iterator(); i.hasNext();) {
			EShape_definition_representation esdr = (EShape_definition_representation) i.next();
			if(!esdr.testUsed_representation(null)){
				continue;
			}
			if(!esdr.testDefinition(null)){
				continue;
			}
			EEntity ee = esdr.getDefinition(null);
			if(!(ee instanceof EProperty_definition)){
				continue;
			}
			EProperty_definition epd = (EProperty_definition)ee;
			if(epd instanceof EAssembly_component_armx
					|| epd instanceof EPart_usage_view
					|| epd instanceof EPart_design_view
					|| epd instanceof ETemplate_definition){
				continue;
			}

			if(!epd.testDefinition(null)){
				continue;
			}
			ee = epd.getDefinition(null);
			if(!(ee instanceof EShape_aspect)){
				continue;
			}
			String gisuName = "";
			// More type where name is redeclared as derived maybe added here
			if(!(epd instanceof EApplied_independent_property)){
				if(epd.testName(null)){
					gisuName = epd.getName(null);
				}
			}
			ERepresentation er = esdr.getUsed_representation(null);
			createGISUFromOldPattern(importer, context, models, (EShape_aspect)ee, er,
					gisuName);

			// Clean up
			importer.logMessage(" Deleting "+esdr);
			esdr.deleteApplicationInstance();
			LangUtils.deleteInstanceIfUnused(models, epd);
			LangUtils.deleteInstanceIfUnused(models, er);
		}
	}

	private static void createGISUFromOldPattern(Importer importer, SdaiContext context, ASdaiModel models,
			EShape_aspect esa, ERepresentation er, String gisuName) throws SdaiException {

		TopRepresentationFitness fitness = new TopRepresentationFitness(models, esa);
		AGeometric_representation_item items = getGeometricItems(er);
		if (items.getMemberCount() == 1) {
			ERepresentation_item item = items.getByIndex(1);

			// Now we can create GISU. if possible have to make associated_shape_element
			SdaiModel model = esa.findEntityInstanceSdaiModel();
			EGeometric_item_specific_usage egisu;
			if (esa instanceof EAssociated_shape_element) {
				egisu = (EAssociated_shape_element) esa;
			} else {
				EEntity_definition complexType = getCompatibleType(model.getUnderlyingSchema(), new EEntity_definition[] {
					esa.getInstanceType(), CAssociated_shape_element.definition});
				if (complexType != null) {
					egisu = (EGeometric_item_specific_usage) model.substituteInstance(esa, complexType);
				} else {
					importer.logMessage("Missing complex type with Associated_shape_element for " + esa);
					//backup
					egisu = (EGeometric_item_specific_usage) model.createEntityInstance(CGeometric_item_specific_usage.definition);
					egisu.setDefinition(null, esa);
				}
			}

			ERepresentation eTop = getTopRepresentation(context, item, fitness);
			setupGisu(egisu, item, gisuName, eTop == null ? er : eTop);
		} else if (items.getMemberCount() > 1) {
			// create composite shape aspect as we have multiple geometric items related to single shape aspect
			SdaiModel model = esa.findEntityInstanceSdaiModel();
			EComposite_shape_aspect_armx eComposite;
			if (esa instanceof EComposite_shape_aspect_armx) {
				eComposite = (EComposite_shape_aspect_armx) esa;
			} else {
				EEntity_definition complexType = getCompatibleType(model.getUnderlyingSchema(), new EEntity_definition[] {
					esa.getInstanceType(), CComposite_shape_aspect_armx.definition});
				if (complexType != null) {
					eComposite = (EComposite_shape_aspect_armx) model.substituteInstance(esa, complexType);
				} else {
					eComposite = null;
					importer.logMessage("Missing complex type with Composite_shape_aspect_armx for " + esa);
				}
			}
			if (eComposite != null) {
				if (!eComposite.testElements(null)) {
					eComposite.createElements(null);
				}
				for (SdaiIterator j = items.createIterator(); j.next();) {
					EGeometric_representation_item item = items.getCurrentMember(j);
					EAssociated_shape_element eChild = (EAssociated_shape_element) model.createEntityInstance(CAssociated_shape_element.definition);
					eComposite.getElements(null).addUnordered(eChild);
					if (item.testName(null)) {
						eChild.setName((EShape_aspect) null, item.getName(null));
					} else {
						eChild.setName((EShape_aspect) null, "");
					}

					if (eComposite.testOf_shape(null)) {
						eChild.setOf_shape(null, eComposite.getOf_shape(null));
					}

					if (eComposite.testProduct_definitional(null)) {
						eChild.setProduct_definitional(null, eComposite.getProduct_definitional(null));
					} else {
						eChild.setProduct_definitional(null, ELogical.UNKNOWN);
					}

					ERepresentation eTop = getTopRepresentation(context, item, fitness);
					setupGisu(eChild, item, gisuName, eTop == null ? er : eTop);
				}
			} else {
				// as a backup create gisu to link shape aspect with each geometric item, although it is not
				// supported in IDA-STEP
				for (SdaiIterator j = items.createIterator(); j.next();) {
					EGeometric_representation_item item = items.getCurrentMember(j);
					EGeometric_item_specific_usage egisu = (EGeometric_item_specific_usage) model.createEntityInstance(CGeometric_item_specific_usage.definition);
					egisu.setDefinition(null, esa);
					ERepresentation eTop = getTopRepresentation(context, item, fitness);
					setupGisu(egisu, item, gisuName, eTop == null ? er : eTop);
				}
			}
		}
	}

	private static List aggregateToList(AEntity aggr) throws SdaiException {
		List result = new ArrayList();
		for (SdaiIterator i = aggr.createIterator(); i.next();) {
			result.add(aggr.getCurrentMemberEntity(i));
		}
		return result;
	}

	private static void setupGisu(EGeometric_item_specific_usage egisu, ERepresentation_item item, String name,
			ERepresentation eRep) throws SdaiException {

		egisu.setName(null, name);
		egisu.setIdentified_item(null, item);
		egisu.setUsed_representation(null, eRep);
	}

	private static AGeometric_representation_item getGeometricItems(ERepresentation er) throws SdaiException {
		AGeometric_representation_item aGri = new AGeometric_representation_item();
		if(!er.testItems(null)) {
			return aGri;
		}

		ARepresentation_item items = er.getItems(null);
		for (SdaiIterator k = items.createIterator(); k.next();) {
			ERepresentation_item item = items.getCurrentMember(k);
			if(item instanceof EGeometric_representation_item && !aGri.isMember(item)){
				aGri.addUnordered(item);
			}
		}
		return aGri;
	}

	private static ERepresentation getTopRepresentation(SdaiContext context, ERepresentation_item item,
			TopRepresentationFitness fitness) throws SdaiException {

		FUsing_representations fur = new FUsing_representations();
		Value value = Value.alloc(item.getInstanceType());
		value.set(context, item);
		value = fur.run(context, value);
		Aggregate agg = value.getInstanceAggregate();
		ERepresentation erTop = null;
		for (SdaiIterator r = agg.createIterator(); r.next();) {
			ERepresentation ee1 = (ERepresentation)agg.getCurrentMemberObject(r);
			if (fitness.fits(erTop, ee1)) {
				erTop = ee1;
			}
		}
		return erTop;
	}

	private static class TopRepresentationFitness {
		private final ARepresentation aMainRepresentations;
		private final ARepresentation aAllRepresentations;

		public TopRepresentationFitness(ASdaiModel domain, EShape_aspect eShapeAspect) throws SdaiException {
			aMainRepresentations = getMainRepresentations(domain, eShapeAspect);
			aAllRepresentations = getRepresentationsInContext(domain, aMainRepresentations);
		}

		public boolean fits(ERepresentation eOldFit, ERepresentation eCandidate) throws SdaiException {
			if (!aAllRepresentations.isMember(eCandidate)) {
				return false;
			}
			if (eOldFit == null) {
				return true;
			}

			if (aMainRepresentations.isMember(eOldFit)) {
				if (!(eOldFit instanceof EShape_representation)
						&& aMainRepresentations.isMember(eCandidate)
						&& (eCandidate instanceof EShape_representation)) {

					return true;
				}
			} else {
				if (aMainRepresentations.isMember(eCandidate)) {
					return true;
				}
				if (!(eOldFit instanceof EShape_representation) && (eCandidate instanceof EShape_representation)) {
					return true;
				}
			}
			return false;
		}

		private static ARepresentation getMainRepresentations(ASdaiModel models, EShape_aspect esa) throws SdaiException {
			ARepresentation ar = new ARepresentation();
			if (esa.testOf_shape(null)) {
				EProduct_definition_shape epds = esa.getOf_shape(null);
				AShape_definition_representation asdr2 = new AShape_definition_representation();
				CShape_definition_representation.usedinDefinition(null, epds, models, asdr2);
				for (SdaiIterator s = asdr2.createIterator(); s.next();) {
					EShape_definition_representation esdr2 = asdr2.getCurrentMember(s);
					if (esdr2.testUsed_representation(null) && !ar.isMember(esdr2)) {
						ar.addUnordered(esdr2.getUsed_representation(null));
					}
				}
			}
			return ar;
		}

		private static ARepresentation getRepresentationsInContext(ASdaiModel models, ARepresentation aRootRepresentations) throws SdaiException {
			ARepresentation ar = new ARepresentation();
			for (SdaiIterator i = aRootRepresentations.createIterator(); i.next();) {
				ERepresentation eRoot = aRootRepresentations.getCurrentMember(i);
				if (eRoot.testContext_of_items(null)) {
					CRepresentation.usedinContext_of_items(null, eRoot.getContext_of_items(null), models, ar);
				}
			}
			return ar;
		}
	}

	private static void repairChainBasedASE(ASdaiModel models, Importer importer)throws SdaiException {
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AChain_based_associated_shape_element aase = (AChain_based_associated_shape_element) model.getInstances(CChain_based_associated_shape_element.definition);
			for (SdaiIterator j = aase.createIterator(); j.next();) {
				EChain_based_associated_shape_element ease = aase.getCurrentMember(j);
				AItem_identified_representation_usage aiiru = new AItem_identified_representation_usage();
				CItem_identified_representation_usage.usedinDefinition(null, ease, models, aiiru);
				for(int k=1,count=aiiru.getMemberCount();k<=count;k++){
					EItem_identified_representation_usage eiiru = aiiru.getByIndex(k);
					if(eiiru.testName(null)){
						ease.setName((EItem_identified_representation_usage)null, eiiru.getName(null));
					}
					if(eiiru.testDescription(null)){
						ease.setDescription((EItem_identified_representation_usage)null, eiiru.getDescription((EItem_identified_representation_usage)null));
					}
					if(eiiru.testUsed_representation(null)){
						ease.setUsed_representation(null, eiiru.getUsed_representation(null));
					}
					if(eiiru.testIdentified_item(null)){
						ease.setIdentified_item(null, eiiru.getIdentified_item(null));
					}
					eiiru.deleteApplicationInstance();
					break;
				}
			}
		}
	}
	
	private static void repairAssociatedShapeElement(ASdaiModel models, Importer importer)throws SdaiException {
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AAssociated_shape_element aase = (AAssociated_shape_element) model.getInstances(CAssociated_shape_element.definition);
			for (SdaiIterator j = aase.createIterator(); j.next();) {
				EAssociated_shape_element ease = aase.getCurrentMember(j);
				AItem_identified_representation_usage aiiru = new AItem_identified_representation_usage();
				CItem_identified_representation_usage.usedinDefinition(null, ease, models, aiiru);
				for(int k=1,count=aiiru.getMemberCount();k<=count;k++){
					EItem_identified_representation_usage eiiru = aiiru.getByIndex(k);
					if(eiiru instanceof EGeometric_item_specific_usage){
						if(eiiru.testName(null)){
							ease.setName((EItem_identified_representation_usage)null, eiiru.getName(null));
						}
						if(eiiru.testDescription(null)){
							ease.setDescription((EItem_identified_representation_usage)null, eiiru.getDescription((EItem_identified_representation_usage)null));
						}
						if(eiiru.testUsed_representation(null)){
							ease.setUsed_representation(null, eiiru.getUsed_representation(null));
						}
						if(eiiru.testIdentified_item(null)){
							ease.setIdentified_item(null, eiiru.getIdentified_item(null));
						}
						eiiru.deleteApplicationInstance();
						break;
					}
				}
			}
		}
	}

	private static void unset2False(ASdaiModel models, Importer importer)throws SdaiException  {
		// 1) Location_dimension
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			ALocation_dimension ald = (ALocation_dimension) model.getInstances(CLocation_dimension.definition);
			for (SdaiIterator j = ald.createIterator(); j.next();) {
				ELocation_dimension eld = ald.getCurrentMember(j);
				if(eld.testRelated_shape_aspect(null)){
					EShape_aspect esa = eld.getRelated_shape_aspect(null);
					int value = getEnvelope_principle(models, esa);
					if(value == ELogical.TRUE){
						eld.setEnvelope_principle(null, true);
					}else if(value == ELogical.FALSE){
						eld.setEnvelope_principle(null, false);
					}
				}
				if(!eld.testDirected(null)){
					eld.setDirected(null, false);
				}
				if(!eld.testAuxiliary(null)){
					eld.setAuxiliary(null, false);
				}
				if(!eld.testTheoretical_exact(null)){
					eld.setTheoretical_exact(null, false);
				}
			}
		}
		// 1) Size_dimension
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			ASize_dimension asd = (ASize_dimension) model.getInstances(CSize_dimension.definition);
			for (SdaiIterator j = asd.createIterator(); j.next();) {
				ESize_dimension esd = asd.getCurrentMember(j);
				if(!esd.testEnvelope_principle(null)){
					EShape_aspect esa = esd.getApplies_to(null);
					int value = getEnvelope_principle(models, esa);
					if(value == ELogical.TRUE){
						esd.setEnvelope_principle(null, true);
					}else if(value == ELogical.FALSE){
						esd.setEnvelope_principle(null, false);
					}
				}
				if(!esd.testEnvelope_principle(null)){
					esd.setEnvelope_principle(null, false);
				}
				if(!esd.testAuxiliary(null)){
					esd.setAuxiliary(null, false);
				}
				if(!esd.testTheoretical_exact(null)){
					esd.setTheoretical_exact(null, false);
				}

			}
		}

	}

	/* Processing the way it is requested in https://intranet.lksoft.net/bugzilla/show_bug.cgi?id=3798 */
	private static int getEnvelope_principle(ASdaiModel models, EShape_aspect esa) throws SdaiException {
		if(esa.testOf_shape(null)){
			EProduct_definition_shape epds = esa.getOf_shape(null);
			if(epds.testDefinition(null)){
				EEntity ee = epds.getDefinition(null);
				if(ee instanceof EProduct_definition){
					EProduct_definition epd =(EProduct_definition)ee;
//							product_definition <-
//							Document_assignment.Is_assigned_to
//							Document_assignment
//							{Document_assignment.role_x = 'dimensioning standard'}
//							Document_assignment.Assigned_document_x ->
//							Document_armx
//							{([Document_armx.id = 'ASME Y14.5-2009']
//							[Document_armx.name = 'Dimensioning and Tolerancing'])
//							([Document_armx.id = 'ISO 1101:2004']
//							[Document_armx.name = 'Geometrical Product Specifications (GPS) — Geometrical tolerancing'])}
					ADocument_assignment ada = new ADocument_assignment();
					CDocument_assignment.usedinIs_assigned_to(null, epd, models, ada);
					for (SdaiIterator d = ada.createIterator(); d.next();) {
						EDocument_assignment eda = ada.getCurrentMember(d);
						if(eda.testRole_x(null)){
							if(eda.getRole_x(null).equals("dimensioning standard")){
								if(eda.testAssigned_document_x(null)){
									EDocument_armx ed = (EDocument_armx)eda.getAssigned_document_x(null);
									if(ed.testId(null)){
										String id = ed.getId(null);
										if((id.length() >= 4)&&(id.substring(0, 4).equals("ASME"))){
											return ELogical.FALSE;
										}else if((id.length() >= 4)&&(id.substring(0, 4).equals("ISO"))){
											return ELogical.TRUE;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return ELogical.UNKNOWN;
	}

	// ========================================================================================
	// following code is copied from repo_edit plug-in and should not be modified independently.
	// ideally it should be moved to jsdai_runtime.jar (SimpleOperations class?) and reused in both places
	// ========================================================================================

	/**
	 * Returns entity definition, that is compatible with all specified definitions in scope of specified schema.
	 * The returned entity definition is (sub)type of all specified entity definitions.
	 * <br>If there is no such entity definition in the specified schema, then <code>null</code> is returned.
	 *
	 * @since 1.0.1300
	 */
	public static EEntity_definition getCompatibleType(ESchema_definition schema,
			EEntity_definition[] types) throws SdaiException {

		EEntity_definition[] leafTypes = getLeafTypes(types);
		String complexName = getComplexTypeName(leafTypes);
		try {
			return schema.getEntityDefinition(complexName);
		} catch (SdaiException e) {
			// do nothing, because exception here means, that there is no such type
		}
		return null;
	}

	private static EEntity_definition[] getLeafTypes(EEntity_definition[] types) throws SdaiException {
		Set leafTypes = new HashSet();
		for (int i = 0, n = types.length; i < n; ++i) {
			boolean include = true;
			for (int j = 0; j < n; ++j) {
				if (types[i] != types[j] && types[j].isSubtypeOf(types[i])) {
					include = false;
					break;
				}
			}

			if (include) {
				leafTypes.add(types[i]);
			}
		}

		return (EEntity_definition[]) leafTypes.toArray(new EEntity_definition[leafTypes.size()]);
	}

	private static String getComplexTypeName(EEntity_definition[] types) throws SdaiException {
		String[] typeNames = getTypeNames(types);
		Arrays.sort(typeNames);
		return getComplexTypeName(typeNames);
	}

	private static String getComplexTypeName(String[] typeNames) {
		StringBuilder complexName = new StringBuilder();
		for (int i = 0, n = typeNames.length; i < n; ++i) {
			if (i > 0) {
				complexName.append('+');
			}
			complexName.append(typeNames[i]);
		}

		return complexName.toString();
	}

	private static String[] getTypeNames(EEntity_definition[] types) throws SdaiException {
		Set typeNamesSet = new HashSet();
		for (int i = 0, n = types.length; i < n; ++i) {
			EEntity_definition eType = types[i];
			typeNamesSet.addAll(Arrays.asList(eType.getName(null).split("[+]"))); //$NON-NLS-0$
		}
		return (String[]) typeNamesSet.toArray(new String[typeNamesSet.size()]);
	}
}
