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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import jsdai.SActivity_mim.CApplied_action_assignment;
import jsdai.SApplication_context_schema.CProduct_context;
import jsdai.SApplication_context_schema.CProduct_definition_context;
import jsdai.SApproval_mim.CApplied_approval_assignment;
import jsdai.SCertification_mim.CApplied_certification_assignment;
import jsdai.SContract_mim.CApplied_contract_assignment;
import jsdai.SDate_time_assignment_mim.CApplied_date_and_time_assignment;
import jsdai.SDocument_assignment_mim.CApplied_document_reference;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CCc_design_approval;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CCc_design_certification;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CCc_design_contract;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CCc_design_date_and_time_assignment;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CCc_design_person_and_organization_assignment;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CCc_design_security_classification;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CCc_design_specification_reference;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CChange;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CChange_request;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CDesign_context;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CDesign_make_from_relationship;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CMechanical_context;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CStart_request;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CStart_work;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.CSupplied_part_relationship;
import jsdai.SPerson_organization_assignment_mim.CApplied_person_and_organization_assignment;
import jsdai.SProduct_definition_schema.CProduct_definition_formation;
import jsdai.SProduct_definition_schema.CProduct_definition_formation_with_specified_source;
import jsdai.SProduct_definition_schema.CProduct_definition_relationship;
import jsdai.SSecurity_classification_mim.CApplied_security_classification_assignment;
import jsdai.SWork_request_mim.CApplied_action_request_assignment;
import jsdai.dictionary.AAttribute;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EInverse_attribute;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.Aggregate;
import jsdai.lang.CAggregate;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiSession;

/**
 * @author evita
 * Class is used for initial aim entities fix during import procedure. It substitutes all old-style 
 * entities with current ones (cc_xxx -> applied_xxx_assignment)
 * 
 * After AIM entities fix can be run mapping engine.
 * 
 * Test file: Step21\AP203\allied_203.stp
 * 
 */
public class AP203SpecificRepair {
	
	private AP203SpecificRepair() { }

	/**
	 * Map of entity definitions that should not be supported
	 * in XIM and thus need to be substituted into some other types.
	 * See bug #2044 for details
	 */
	private static final Map ENTITIES_DICTIONARY;

	static {
		// use linked hash map here, as it is important to substitute entities
		// in particular order by type (see bug #2310 for case where order matters)
		Map entitiesDictionary = new LinkedHashMap();
		entitiesDictionary.put(CCc_design_approval.definition, CApplied_approval_assignment.definition);
		entitiesDictionary.put(CCc_design_certification.definition,CApplied_certification_assignment.definition);
		entitiesDictionary.put(CCc_design_contract.definition,CApplied_contract_assignment.definition);
		entitiesDictionary.put(CCc_design_date_and_time_assignment.definition,CApplied_date_and_time_assignment.definition);
		entitiesDictionary.put(CCc_design_person_and_organization_assignment.definition,CApplied_person_and_organization_assignment.definition);
		entitiesDictionary.put(CCc_design_security_classification.definition,CApplied_security_classification_assignment.definition);
		entitiesDictionary.put(CCc_design_specification_reference.definition,CApplied_document_reference.definition);
		entitiesDictionary.put(CChange.definition, CApplied_action_assignment.definition);
		entitiesDictionary.put(CChange_request.definition, CApplied_action_request_assignment.definition);
		entitiesDictionary.put(CDesign_context.definition, CProduct_definition_context.definition);
		entitiesDictionary.put(CDesign_make_from_relationship.definition, CProduct_definition_relationship.definition);
		entitiesDictionary.put(CMechanical_context.definition, CProduct_context.definition);
		entitiesDictionary.put(CStart_request.definition, CApplied_action_request_assignment.definition);
		entitiesDictionary.put(CStart_work.definition, CApplied_action_assignment.definition);
		entitiesDictionary.put(CSupplied_part_relationship.definition, CProduct_definition_relationship.definition);
		entitiesDictionary.put(CProduct_definition_formation_with_specified_source.definition, CProduct_definition_formation.definition);
		
		ENTITIES_DICTIONARY = Collections.unmodifiableMap(entitiesDictionary);
	}
	
	/**
	 *	Runs the custom translation from source instances to target instances (custom mapping, fixed creation etc.)
	 *@param repo	Source repository
	 *@return Aggregate of translated instances
	 */
	public static void run(ASdaiModel models)
		throws SdaiException {
		
		for (Iterator i = ENTITIES_DICTIONARY.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			EEntity_definition eDef = (EEntity_definition) entry.getKey();
			EEntity_definition eSubstDef = (EEntity_definition) entry.getValue();

			AEntity ccEntities = models.getInstances(eDef);

			// the ccEntities is a live aggregate and since we want to
			// substitute its members, we need to copy all members to
			// different collection first
			
			Collection entities = new ArrayList();
			for (SdaiIterator j = ccEntities.createIterator(); j.next(); ) {
				entities.add(ccEntities.getCurrentMemberEntity(j));
			}
			
			for (Iterator j = entities.iterator(); j.hasNext();) {
				EEntity ccEntity = (EEntity) j.next();

				// gather old-type entity attributes - only direct ones, not from supertypes
				HashMap ccEntityAttributes = collectEntityAttributes(ccEntity);
				
				// fix entity type
				EEntity newEntity = ccEntity.findEntityInstanceSdaiModel().substituteInstance(ccEntity, eSubstDef);
				
				// restore direct (not from supertype) attributes value which were lost
				setEntityAttributes(newEntity, ccEntityAttributes);
			}
		}
	}
	
	/**
	 * Collects entitity attributes values and places into HashMap as following:
	 * String - entity name : 
	 * 		Object - attribute value (if attribute value was aggregate all aggregate elements are included to ArrayList)  
	 * 
	 * @param originalInstance
	 * @return
	 * @throws SdaiException
	 */
	private static HashMap collectEntityAttributes(EEntity originalInstance) throws SdaiException {
		HashMap attributes = new HashMap();
		AAttribute entityAttributes = originalInstance.getInstanceType().getAttributes(null, originalInstance.findEntityInstanceSdaiModel().getRepository().getSession().getSystemRepository().getModels());
		for (SdaiIterator itAttributes = entityAttributes.createIterator(); itAttributes.next(); ) {
			EAttribute entityAttribute = entityAttributes.getCurrentMember(itAttributes);
			if (!(entityAttribute instanceof EInverse_attribute) && originalInstance.testAttribute(entityAttribute, new EDefined_type[3]) > 0){
				Object attributeValue = originalInstance.get_object(entityAttribute);
				Object value = null;
				if (attributeValue instanceof CAggregate) {
					value = new ArrayList();
					CAggregate valueAggregateOriginal = (CAggregate) attributeValue;
					
					for (SdaiIterator itValAgg = valueAggregateOriginal.createIterator(); itValAgg.next(); ) {
						Object aggregateValue = valueAggregateOriginal.getCurrentMemberObject(itValAgg);
						if (aggregateValue instanceof EEntity) {
							EEntity aggregateValueEntity = (EEntity)aggregateValue;
							((ArrayList)value).add(aggregateValueEntity);
						}
					}
				} else {
					value = attributeValue;
				}
				attributes.put(entityAttribute.getName(null), value);
			}
		}
		return attributes;
	}
	
	private static void setEntityAttributes(EEntity entity, HashMap attributesValues) throws SdaiException {
		final SdaiSession session = entity.findEntityInstanceSdaiModel().getRepository().getSession();
		final AAttribute entityAttributes = entity.getInstanceType().getAttributes(null, entity.findEntityInstanceSdaiModel().getRepository().getSession().getSystemRepository().getModels());

		for (SdaiIterator itAttributes = entityAttributes.createIterator(); itAttributes.next(); ) {
			EAttribute entityAttribute =  entityAttributes.getCurrentMember(itAttributes);
			if (attributesValues.containsKey(entityAttribute.getName(null))) {
				Object value = attributesValues.get(entityAttribute.getName(null));
				try {
					if (value instanceof ArrayList){
						ArrayList aggregateValues = (ArrayList) value;
						Aggregate aggregateToSet = entity.createAggregate(entityAttribute, null);
						boolean hasError = false;
						for (Iterator it = aggregateValues.iterator(); it.hasNext(); ) {
							Object next = it.next();
							try {
								aggregateToSet.addUnordered(next, null);
							} catch (SdaiException e) {
								if (!hasError) {
									printError(session, entity, entityAttribute);
									// we do not want to print same error message more
									// than once
									hasError = true;
								}
							}
						}
					} else {
						entity.set(entityAttribute, value, null);
					}
				} catch (SdaiException e) {
					printError(session, entity, entityAttribute);
				}
			}
		}
	}

	private static void printError(SdaiSession session, EEntity entity, EAttribute attr)
		throws SdaiException {

		session.printlnSession("Error: Attribute \"" + attr.getName(null)
			+ "\" of entity instance " + entity.getPersistentLabel() + " is set to invalid value.");
	}
}
