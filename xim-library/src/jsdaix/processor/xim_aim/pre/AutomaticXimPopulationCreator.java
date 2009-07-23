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

package jsdaix.processor.xim_aim.pre;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jsdai.SApplication_context_schema.CProduct_definition_context;
import jsdai.SPhysical_unit_design_view_mim.ENext_assembly_usage_occurrence_relationship;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.CRepresentation_context;
import jsdai.dictionary.ANamed_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EExplicit_attribute;
import jsdai.lang.EEntity;
import jsdai.lang.JsdaiLangAccessor;
import jsdai.lang.MappingContext;
import jsdai.lang.MappingPopulationCreator;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.CMappedXIMEntity;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.EGeneric_attribute_mapping;

/**
 * @author evita
 * Automatic engine used for MIM schema upgrade to XIM based on mapping operations
 */
public class AutomaticXimPopulationCreator extends JsdaiLangAccessor implements MappingPopulationCreator {
	private MappingContext mappingContext;
	private Collection deferredInstances = new ArrayList();

	// To store previous attribute mapping for debugging
	private EGeneric_attribute_mapping prevMapping = null;
	int totalSourceInstances = 0;
	int totalAttributeValueInstances = 0;
	int attributeValuesCatchCount = 0;
	private Map specialTypeInstanceMap; // This map is used to collect fake maps for further process according types
	private final static EEntity_definition[] specialTypes = {CRepresentation.definition,
		CDescriptive_representation_item.definition,
		CProduct_definition_context.definition,
		CRepresentation_context.definition};

	private Importer importer;
	
	public AutomaticXimPopulationCreator(SdaiContext context, Importer importer) throws SdaiException {
		mappingContext = new MappingContext(context, this);
		mappingContext.setInterleavedCreation(false);
		specialTypeInstanceMap = new HashMap();
		this.importer = importer;
	}

	public void createSourceInstance(EEntity_mapping type,
									 Collection targetInstances) throws SdaiException {
		EEntity_definition source = type.getSource(null);
		EEntity_definition target = (EEntity_definition)type.getTarget(null);
		totalSourceInstances += targetInstances.size();
		// Collect faked mapping which needs further processing
		for(int t=0; t< getSpecialTypes().length; t++){
			if((getSpecialTypes()[t] == target)&&(getSpecialTypes()[t] == source)){
				getSpecialTypeInstanceMap().put(target, targetInstances);
			}
		}
		for(Iterator i = targetInstances.iterator(); i.hasNext(); ) {
			EEntity targetInstance = (EEntity)i.next();
			try {
				EEntity sourceInstance =
					CMappedXIMEntity.buildMappedInstance(mappingContext, targetInstance, type,
							deferredInstances, targetInstance instanceof ENext_assembly_usage_occurrence_relationship);
			} catch(SdaiException e) {
				String eString = e.getMessage(); //toString();
				importer.errorMessage(" Problem while creating instance "+eString+" "+target+" vs "+targetInstance+" "+e.getErrorBase() );
				e.printStackTrace();
			}
		}
	}

	public void setSourceAttributeValues(EEntity_mapping type, EEntity targetInstance,
										 Map sourceValues) throws SdaiException {
		if(deferredInstances != null) {
			CMappedXIMEntity.buildDeferredMappedInstances(mappingContext, deferredInstances);
			deferredInstances = null;
		}
		totalAttributeValueInstances++;
		EEntity ximInstance = null;
		EEntity_definition ximType = type.getSource(null);
		Set allowedAttributes = new HashSet();
		for(Iterator i = sourceValues.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry)i.next();
			EGeneric_attribute_mapping attributeMapping =
				(EGeneric_attribute_mapping)entry.getKey();
			if(ximInstance == null) {
				ximInstance = mappingContext.getMappedInstance(targetInstance);
				if(ximInstance == null && targetInstance.isKindOf(ximType)) {
					ximInstance = targetInstance;
				}
				if(ximInstance == null) {
					return;
				}

				EEntity_definition concreteXimType = ximInstance.getInstanceType();
				EExplicit_attribute[] explicitAttributes = getEntityExplicitAttributes(concreteXimType);
				Field[] explicitAttributeFields = getEntityAttributeFields(concreteXimType);
				for (int j = 0; j < explicitAttributes.length; j++) {
					if(explicitAttributeFields[j] != null) {
						allowedAttributes.add(explicitAttributes[j]);
					}
				}
			}
			Object attrValue = entry.getValue();
			EAttribute attribute = attributeMapping.getSource(null);
			if(attribute instanceof EExplicit_attribute) {
				EExplicit_attribute baseAttribute = (EExplicit_attribute) attribute;
                while(baseAttribute.testRedeclaring(null)) {
                    baseAttribute = baseAttribute.getRedeclaring(null);
                }
                boolean process = allowedAttributes.contains(baseAttribute);
				if(process) {
					EExplicit_attribute explicitAttribute = (EExplicit_attribute)attribute;
					while(explicitAttribute.testRedeclaring(null)) {
						explicitAttribute = explicitAttribute.getRedeclaring(null);
					}
					try {
						CMappedXIMEntity.assignMappedValue(ximInstance, mappingContext, explicitAttribute, attrValue,
														   (attributeMapping.testData_type(null) ? 
															attributeMapping.getData_type(null) :
															(ANamed_type)null));
					} catch(SdaiException e) {
						String eString = e.getMessage();
						if(eString.startsWith("SY_ERR - Underlying system error. ")) {
							eString = eString.substring("SY_ERR - Underlying system error. ".length());
						}
						if(eString.indexOf("Can not find mapped instance") >= 0) {
							eString = eString.substring(0, eString.indexOf('='));
						}
						attributeValuesCatchCount++;
						importer.errorMessage(" Problem while setting attribute "+ximInstance+" "+explicitAttribute+" "+eString+" "+e.getErrorBase());
						e.printStackTrace();
					}
				}
			} else {
				if(prevMapping != attributeMapping) {
					// System.out.println("Unsupported attribute: " + attribute);
					prevMapping = attributeMapping;
				}
			}
		}
	}

	public Set preprocessTargetInstances(EEntity_mapping type) throws SdaiException {
		// String armType = type.getSource(null).getName(null);
		
//		if (// property_definition
//				armType.equals("classification_attribute") 
////				|| armType.equals("item_shape")
////				|| armType.equals("contextual_item_shape")
////				|| armType.equals("product_definition_shape")
////				
////				//property_definition_representation
//				|| armType.equals("surface_condition_association")
////				|| armType.equals("shape_placement_association")
////				|| armType.equals("shape_description_association")
////				
////				// representation
//				|| armType.equals("treatment_result")
////				|| armType.equals("external_geometric_model")
////				|| armType.equals("shape_representation")
////				
////				// measure_representation_item
////				|| armType.equals("regional_coordinate")
//				|| armType.equals("surface_texture_parameter__measure_representation_item")) {
//			return new HashSet();
//		}
		
		// This is a workaroud for NEXT_ASSEMBLY_USAGE_OCCURRENCE which should remain not mapped
		// for assembly structure. But unfortunately it mappes into the following entities, so
		// mapping should be fixed to avoid that. 
		// Test file - STEP-Files\AP214\as1-tc-214_3D.stp (from instalaltion)
//		if (armType.equals("breakdown_node_relationship") 
//				|| armType.equals("part_function_association")
//				|| armType.equals("part_occurrence_relationship")
//				|| armType.equals("design_constraint_relationship")) {
//			return new HashSet();
//		}
		return null;
	}

	/**
	 * @return Returns the mappingContext.
	 */
	public MappingContext getMappingContext() {
		return mappingContext;
	}

	public Map getSpecialTypeInstanceMap() {
		return specialTypeInstanceMap;
	}

	public static EEntity_definition[] getSpecialTypes() {
		return specialTypes;
	}
}