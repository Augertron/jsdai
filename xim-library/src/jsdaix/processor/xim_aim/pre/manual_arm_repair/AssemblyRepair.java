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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jsdai.SAssembly_structure_xim.AProduct_occurrence_definition_relationship_armx;
import jsdai.SAssembly_structure_xim.CNext_assembly_usage_occurrence_relationship_armx;
import jsdai.SAssembly_structure_xim.CProduct_occurrence_definition_relationship_armx;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;
import jsdai.SAssembly_structure_xim.EProduct_occurrence_definition_relationship_armx;
import jsdai.SGeneric_product_occurrence_xim.EProduct_occurrence;
import jsdai.SItem_definition_structure_xim.CAssembly_definition_armx;
import jsdai.SItem_definition_structure_xim.EAssembly_definition_armx;
import jsdai.SLand_mim.ELand_with_join_terminal;
import jsdai.SLayered_interconnect_module_design_mim.EStructured_layout_component_sub_assembly_relationship_with_component;
import jsdai.SLayered_interconnect_module_design_xim.EStructured_layout_component_sub_assembly_relationship_armx;
import jsdai.SPart_occurrence_xim.CDefinition_based_part_occurrence;
import jsdai.SPart_occurrence_xim.EDefinition_based_part_occurrence;
import jsdai.SPart_view_definition_xim.CPart_view_definition;
import jsdai.SPart_view_definition_xim.EPart_view_definition;
import jsdai.SPhysical_unit_design_view_xim.AAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.CAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_structure_schema.ANext_assembly_usage_occurrence;
import jsdai.SProduct_structure_schema.AProduct_definition_occurrence_relationship;
import jsdai.SProduct_structure_schema.CNext_assembly_usage_occurrence;
import jsdai.SProduct_structure_schema.CProduct_definition_occurrence_relationship;
import jsdai.SProduct_structure_schema.ENext_assembly_usage_occurrence;
import jsdai.SProduct_view_definition_xim.EProduct_view_definition;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.SMixed_complex_types.*;

/**
 * @author evita
 */
public class AssemblyRepair {
	
	public static void run(ASdaiModel models) throws SdaiException {
		changeStructure(models);
		fixAssemblies(models);
		fixNauo(models);
		makeSlcsarComplexes(models);
	}

	private static void makeSlcsarComplexes(ASdaiModel models) throws SdaiException {
		AAssembly_component_armx aAc = (AAssembly_component_armx)
			models.getInstances(CAssembly_component_armx.definition);
		Collection acList = new ArrayList();
		for (SdaiIterator it = aAc.createIterator(); it.next();) {
			acList.add(aAc.getCurrentMember(it));
		}
		for (Iterator i = acList.iterator(); i.hasNext();) {
			EAssembly_component_armx eAc = (EAssembly_component_armx)i.next();
			AProduct_occurrence_definition_relationship_armx apodr = new AProduct_occurrence_definition_relationship_armx();
			CProduct_occurrence_definition_relationship_armx.usedinRelated_view(null, eAc, models, apodr);
			EStructured_layout_component_sub_assembly_relationship_armx eslcsar = null;
			for (SdaiIterator itS = apodr.createIterator(); itS.next();) {
				EProduct_occurrence_definition_relationship_armx epodr = apodr.getCurrentMember(itS);
				if(epodr instanceof EStructured_layout_component_sub_assembly_relationship_armx){
					// second user - so we can't make complex and skip it
					if(eslcsar != null){
						eslcsar = null;
						break;
					}
					eslcsar = (EStructured_layout_component_sub_assembly_relationship_armx)epodr;
				}
			}
			if(eslcsar != null){
				// create a complex:
				// 1) form a list of leaves
				String substring = eAc.getInstanceType().getName(null);
				List leaves = new ArrayList();
				// we have a complex to parse
				if(substring.indexOf('+') > 0){
					StringTokenizer st = new StringTokenizer(substring, "+", false);
					while(st.hasMoreElements()){
						leaves.add(st.nextElement());
					}
				}else{
					leaves.add(substring);
				}
				leaves.add("structured_layout_component_sub_assembly_relationship_with_component_xim");
				// 2) sort leaves
				Collections.sort(leaves);
				// 3) try to make a complex
				String complexName = new String();
				for(int j=0, count=leaves.size();j<count;j++){
					complexName += leaves.get(j)+"+";
				}
				complexName = complexName.substring(0, complexName.length()-1);
				// System.err.println(" Complex "+complexName+" vs "+eAc.getInstanceType()+" "+eAc);
				SdaiModel model = eAc.findEntityInstanceSdaiModel();
				// TEMPORARY WORKAROUND
				if(eAc instanceof ELand_with_join_terminal){
					ELand_with_join_terminal elwjt = (ELand_with_join_terminal)eAc;
					elwjt.unsetOf_shape(null);
				}
				EEntity_definition complextEntityDefinition =
					model.getUnderlyingSchema().getEntityDefinition(complexName);
				EStructured_layout_component_sub_assembly_relationship_armx
					newInstance = (EStructured_layout_component_sub_assembly_relationship_armx)
					model.substituteInstance(eAc, complextEntityDefinition);
				// System.err.println(" New "+newInstance);
				// 4) copy attributes
				// (RT) relating_view: structured_layout_component_armx;
				if(eslcsar.testRelating_view(null)){
					newInstance.setRelating_view(null, eslcsar.getRelating_view(null));
				}
				// first_location: template_location_in_structured_template;
				if(eslcsar.testFirst_location(null)){
					newInstance.setFirst_location(null, eslcsar.getFirst_location(null));
				}
		        // (OPT) second_location: template_location_in_structured_template;
				if(eslcsar.testSecond_location(null)){
					newInstance.setSecond_location(null, eslcsar.getSecond_location(null));
				}
		        // (OPT) overriding_shape: part_template_shape_model;
				if(eslcsar.testOverriding_shape(null)){
					newInstance.setOverriding_shape(null, eslcsar.getOverriding_shape(null));
				}
		        // design_specific_placement: BOOLEAN;
				if(eslcsar.testDesign_specific_placement(null)){
					newInstance.setDesign_specific_placement(null, eslcsar.getDesign_specific_placement(null));
				}
				// (OPT) reference_designator: identifier;
				if(eslcsar.testReference_designator(null)){
					newInstance.setReference_designator(null, eslcsar.getReference_designator(null));
				}
				eslcsar.deleteApplicationInstance();
			}
		}		
	}
	
	public static void fixNauo(ASdaiModel models) throws SdaiException {
		Collection nauoList = new ArrayList();
		ANext_assembly_usage_occurrence aNauo = (ANext_assembly_usage_occurrence)
			models.getInstances(ENext_assembly_usage_occurrence.class);
		for (SdaiIterator it = aNauo.createIterator(); it.next();) {
			nauoList.add(aNauo.getCurrentMember(it));
		}

		for (Iterator i = nauoList.iterator(); i.hasNext();) {
			ENext_assembly_usage_occurrence eNauo = (ENext_assembly_usage_occurrence) i.next();
			if (!(eNauo instanceof EStructured_layout_component_sub_assembly_relationship_with_component)) {
				final EProduct_definition eRelatingView = eNauo.testRelating_product_definition(null)
					? eNauo.getRelating_product_definition(null) : null;
				final EProduct_definition eRelatedView = eNauo.testRelated_product_definition(null)
					? eNauo.getRelated_product_definition(null) : null;
				final String refDes = eNauo.testReference_designator(null)
					? eNauo.getReference_designator(null) : null;

				ENext_assembly_usage_occurrence_relationship_armx eNauoRel;
				final SdaiModel model = eNauo.findEntityInstanceSdaiModel();
				try {
					EEntity_definition type = CNext_assembly_usage_occurrence_relationship_armx.definition;
					if(eRelatingView instanceof EAssembly_definition_armx){
						type = CAssembled_part_association$next_assembly_usage_occurrence_relationship_armx.definition;
					}
					eNauoRel = (ENext_assembly_usage_occurrence_relationship_armx)
						model.substituteInstance(eNauo, type);
				} catch (SdaiException e) {
					model.getRepository().getSession().printlnSession("Failed to substitute " + eNauo + " bacause of " + e.getMessage());

					// this is fall-back code if substitution fails.
					// it can fail because of some users of old entity
					// can not be transfered to new entity. So as a result
					// we shall drop all users.
					eNauo.deleteApplicationInstance();
					eNauoRel = (ENext_assembly_usage_occurrence_relationship_armx)
						model.createEntityInstance(CNext_assembly_usage_occurrence_relationship_armx.definition);
				}
				if (eRelatingView instanceof EProduct_view_definition) {
					eNauoRel.setRelating_view(null, (EProduct_view_definition) eRelatingView);
				}
				if (eRelatedView instanceof EProduct_occurrence) {
					eNauoRel.setRelated_view(null, (EProduct_occurrence) eRelatedView);
				}
				if (refDes != null) {
					eNauoRel.setReference_designator(null, refDes);
				}
			}
		}
	}


	/**	The standard assembly structure:
		 *
		 *	assembly_definition -> next_assembly_usage_occurence -> part_view_defintion
		 *
		 *	Is replaced by the following structure:
		 *
		 *	assembly_definition -> next_assembly_usage_occurence -> definition_based_part_occurence -> part_view_defintion
		 *
		 *	Test file: H:\Step21\IDA-STEP\Duplo-Process.stp and all others containing assembly structure
		 */
	private static void changeStructure(ASdaiModel models) throws SdaiException {
		AEntity nextAssemblyList = models.getExactInstances(CNext_assembly_usage_occurrence.class);
		for (SdaiIterator it = nextAssemblyList.createIterator(); it.next(); ) {
			ENext_assembly_usage_occurrence nextAssembly = (ENext_assembly_usage_occurrence) nextAssemblyList.getCurrentMemberEntity(it);
			if (nextAssembly.testRelated_product_definition(null) && 
					nextAssembly.getRelated_product_definition(null) instanceof EPart_view_definition) {

				EPart_view_definition componentDefinition = (EPart_view_definition) 
					nextAssembly.getRelated_product_definition(null);
				// Check - maybe there is already a component available
				AProduct_definition_occurrence_relationship apdor = new AProduct_definition_occurrence_relationship(); 
				CProduct_definition_occurrence_relationship.usedinOccurrence_usage(null, 
					nextAssembly, models, apdor);
				EDefinition_based_part_occurrence defBasedPartOcc = null;
				if(apdor.getMemberCount() > 0){
					for(int i=1,count=apdor.getMemberCount();i<=count;i++){
						EProduct_definition epd = apdor.getByIndex(i).getOccurrence(null);
						if(epd instanceof EDefinition_based_part_occurrence){
							EDefinition_based_part_occurrence edbpo = (EDefinition_based_part_occurrence)epd;
							defBasedPartOcc = edbpo;
							defBasedPartOcc.setDerived_from(null, componentDefinition);
							// Not sure on this, but I think we need to delete PDOR, as otherwise it is violating some rules
							apdor.getByIndex(i).deleteApplicationInstance();								
						}
					}
				}
				if(defBasedPartOcc == null){
					defBasedPartOcc = (EDefinition_based_part_occurrence) nextAssembly.findEntityInstanceSdaiModel().createEntityInstance(CDefinition_based_part_occurrence.definition);
					defBasedPartOcc.setId(null, "");
					defBasedPartOcc.setDerived_from(null, componentDefinition);
				}
				
				nextAssembly.setRelated_product_definition(null, defBasedPartOcc);
			}
		}
	}
	
	/**
	 * All Part_view_definition entity instances, which have associated relating 
	 * ANext_assembly_usage_occurrence (are definitions with inner elements) are upgraded to 
	 * Assembly_definition_armx
	 * 
	 * Test file: H:\Step21\cax-if\round18j\r18j_b4.zip\r18j_b4\b4-c5-214.stp
	 * 
	 * @param models
	 * @throws SdaiException
	 */
	private static void fixAssemblies(ASdaiModel models) throws SdaiException {
		AEntity partViewDefinitions = models.getExactInstances(CPart_view_definition.class);
		ArrayList partViewDefinitionList = new ArrayList(); 
		for (SdaiIterator it = partViewDefinitions.createIterator(); it.next(); ) {
			//	walkaround because getExactInstances is living aggregate and not ok in this case
			partViewDefinitionList.add(partViewDefinitions.getCurrentMemberEntity(it));
		}
		for (Iterator it = partViewDefinitionList.iterator(); it.hasNext(); ) {
			EPart_view_definition partViewDefinition = (EPart_view_definition) it.next(); 
			ANext_assembly_usage_occurrence assemblyOccurences = new ANext_assembly_usage_occurrence();
			CNext_assembly_usage_occurrence.usedinRelating_product_definition(null, partViewDefinition, models, assemblyOccurences);
			
			if (assemblyOccurences.getMemberCount() > 0) {
				partViewDefinition.findEntityInstanceSdaiModel().substituteInstance(partViewDefinition, CAssembly_definition_armx.class);
			}
		}
	}
}
