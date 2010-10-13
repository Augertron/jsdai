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

import jsdai.SDimension_tolerance_xim.AAssociated_shape_element;
import jsdai.SDimension_tolerance_xim.ALocation_dimension;
import jsdai.SDimension_tolerance_xim.ASize_dimension;
import jsdai.SDimension_tolerance_xim.CAssociated_shape_element;
import jsdai.SDimension_tolerance_xim.CLocation_dimension;
import jsdai.SDimension_tolerance_xim.CSize_dimension;
import jsdai.SDimension_tolerance_xim.EAssociated_shape_element;
import jsdai.SDimension_tolerance_xim.ELocation_dimension;
import jsdai.SDimension_tolerance_xim.ESize_dimension;
import jsdai.SExternal_reference_schema.AExternal_source;
import jsdai.SExternal_reference_schema.CExternal_source;
import jsdai.SExternal_reference_schema.EExternal_source;
import jsdai.SGeometry_schema.EGeometric_representation_item;
import jsdai.SPart_template_xim.ETemplate_definition;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EPart_design_view;
import jsdai.SPhysical_unit_usage_view_xim.EPart_usage_view;
import jsdai.SProduct_property_definition_schema.CShape_aspect;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_representation_schema.AItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.AShape_definition_representation;
import jsdai.SProduct_property_representation_schema.CItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.CShape_definition_representation;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SProduct_property_representation_schema.EItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.EShape_definition_representation;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.FUsing_representations;
import jsdai.SShape_property_assignment_mim.CGeometric_item_specific_usage;
import jsdai.SShape_property_assignment_mim.EGeometric_item_specific_usage;
import jsdai.SSupport_resource_schema.EIdentifier;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.ASdaiModel;
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
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

	private static final String ASE_NAME = "associated_shape_element";
	
	public static void run(SdaiContext context, ASdaiModel models, Importer importer)throws SdaiException {
		unset2False(models, importer);
		createGISUFromOldPattern(context, models, importer);
		repairAssociatedShapeElement(models, importer);
		cleanupExternally_defined_size_dimension(models, importer);
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
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AShape_definition_representation asdr = (AShape_definition_representation)
				model.getInstances(CShape_definition_representation.definition);
			for (SdaiIterator j = asdr.createIterator(); j.next();) {
				EShape_definition_representation esdr = asdr.getCurrentMember(j);
				if(!esdr.testUsed_representation(null)){
					continue;
				}
				ERepresentation er = esdr.getUsed_representation(null);
				ARepresentation_item items = er.getItems(null);
				// if we do not have exaclty one item to point to - we treat as pattern #1 (old) is not matched here
//				if(items.getMemberCount() != 1){
//					continue;
//				}
				if(!esdr.testDefinition(null)){
					continue;
				}
				EEntity ee = esdr.getDefinition(null);
				if(ee instanceof EProperty_definition){
					EProperty_definition epd = (EProperty_definition)ee;
					if(epd instanceof EAssembly_component_armx){
						continue;
					}
					if(epd instanceof EPart_usage_view){
						continue;
					}
					if(epd instanceof EPart_design_view){
						continue;
					}
					if(epd instanceof ETemplate_definition){
						continue;
					}
					if(!epd.testDefinition(null)){
						continue;
					}
					ee = epd.getDefinition(null);
					if(ee instanceof EShape_aspect){
						EShape_aspect esa = (EShape_aspect)ee;
						// Have to get the right context
						EProduct_definition_shape epds = esa.getOf_shape(null);
						AShape_definition_representation asdr2 = new AShape_definition_representation();
						CShape_definition_representation.usedinDefinition(null, epds, models, asdr2);
						ARepresentation ar = new ARepresentation();
						for (SdaiIterator s = asdr2.createIterator(); s.next();) {
							EShape_definition_representation esdr2 = asdr2.getCurrentMember(s);
							ERepresentation_context erc = esdr2.getUsed_representation(null).getContext_of_items(null);
							CRepresentation.usedinContext_of_items(null, erc, models, ar);
						}						
						for (SdaiIterator k = items.createIterator(); k.next();) {
							ERepresentation_item item = items.getCurrentMember(k);
							if(!(item instanceof EGeometric_representation_item)){
								continue;
							}
							// Usually Representation contains just this one item.
							// But for IDA-STEP to work properly we have to point to real A-BREP
							// or so having complete shape
							FUsing_representations fur = new FUsing_representations();
							Value value = Value.alloc(item.getInstanceType());
							value.set(context, item);
							value = fur.run(context, value);
							Aggregate agg = value.getInstanceAggregate();
							ERepresentation_context erc = er.getContext_of_items(null);
							ERepresentation erTop = er;
							for (SdaiIterator r = agg.createIterator(); r.next();) {
								ERepresentation ee1 = (ERepresentation)agg.getCurrentMemberObject(r);
								if(ar.isMember(ee1)){
									EEntity_definition type = ee1.getInstanceType();
									if((ee1 != er)&&(ee1.getContext_of_items(null) == erc)&&
										(type != CRepresentation.definition)&&(type != CShape_representation.definition)){
										erTop = (ERepresentation)ee1;
										break;
									}
								}
							}
							
							// Now we can create GISU
							EGeometric_item_specific_usage egisu = null;
							// if possible have to make associated_shape_element
							EEntity_definition esaType = esa.getInstanceType();
							if((esaType == CShape_aspect.definition)){
								egisu = (EGeometric_item_specific_usage)
									model.substituteInstance(esa, CAssociated_shape_element.definition);
							// Here we have to basically do nothing	
							}else if((esaType == CAssociated_shape_element.definition)){
								egisu = (EAssociated_shape_element)esa;
							}else{
								// try to make a complex
								String complex;
								String esaName = esaType.getName(null);
								if(esaName.compareTo(ASE_NAME) > 0){
									complex = ASE_NAME+"+"+esaName;
								}else{
									complex = esaName+"+"+ASE_NAME;
								}
								try {
									EEntity_definition complexType = 
										model.getUnderlyingSchema().getEntityDefinition(complex);
									egisu = (EGeometric_item_specific_usage)
										model.substituteInstance(esa, complexType);
								} catch (SdaiException e) {
									importer.errorMessage("Missing complex "+complex);
								}
							}
							//backup
							if(egisu == null){
								egisu = (EGeometric_item_specific_usage)
									model.createEntityInstance(CGeometric_item_specific_usage.definition);
								egisu.setDefinition(null, esa);
							}
//							egisu.setDefinition(null, esa);

							egisu.setIdentified_item(null, item);
							egisu.setUsed_representation(null, erTop);
							
							egisu.setName(null, epd.getName(null));
						}
						// Clean up
						importer.logMessage(" Deleting "+esdr);
						esdr.deleteApplicationInstance();
						LangUtils.deleteInstanceIfUnused(models, epd);
						LangUtils.deleteInstanceIfUnused(models, er);
					}
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
}
