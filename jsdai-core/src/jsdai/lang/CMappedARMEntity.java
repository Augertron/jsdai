/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
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

package jsdai.lang;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import jsdai.dictionary.AAttribute;
import jsdai.dictionary.ADefined_type;
import jsdai.dictionary.AEntity_definition;
import jsdai.dictionary.AExplicit_attribute;
import jsdai.dictionary.ANamed_type;
import jsdai.dictionary.CExplicit_attribute;
import jsdai.dictionary.EAggregation_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EBag_type;
import jsdai.dictionary.EData_type;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EDerived_attribute;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EEnumeration_type;
import jsdai.dictionary.EExplicit_attribute;
import jsdai.dictionary.EInteger_type;
import jsdai.dictionary.EList_type;
import jsdai.dictionary.ENamed_type;
import jsdai.dictionary.EReal_type;
import jsdai.dictionary.ESelect_type;
import jsdai.dictionary.ESet_type;
import jsdai.dictionary.ESimple_type;
import jsdai.dictionary.EString_type;
import jsdai.lang.SdaiException;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.AGeneric_attribute_mapping;
import jsdai.mapping.CEntity_mapping;
import jsdai.mapping.CGeneric_attribute_mapping;
import jsdai.mapping.EAggregate_member_constraint;
import jsdai.mapping.EAnd_constraint_relationship;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EAttribute_mapping_boolean_value;
import jsdai.mapping.EAttribute_mapping_enumeration_value;
import jsdai.mapping.EAttribute_mapping_value;
import jsdai.mapping.EAttribute_value_constraint;
import jsdai.mapping.EEnd_of_path_constraint;
import jsdai.mapping.EEntity_constraint;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.EEnumeration_constraint;
import jsdai.mapping.EGeneric_attribute_mapping;
import jsdai.mapping.EInteger_constraint;
import jsdai.mapping.EInverse_attribute_constraint;
import jsdai.mapping.ELogical_constraint;
import jsdai.mapping.EOr_constraint_relationship;
import jsdai.mapping.EPath_constraint;
import jsdai.mapping.ESelect_constraint;
import jsdai.mapping.EString_constraint;
import jsdai.util.LangUtils;

/*
	Class that ARM entities derive and has additional functionality for AIM instances creation out of ARM->AIM mapping data
*/

/**
 * A common supertype of all ARM entity type implementing classes that support
 * mapping operations. It is part of JSDAI implementation and is for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public abstract class CMappedARMEntity extends CEntity implements EMappedARMEntity {
	// constants identifying which createAimData() method is called: overridden or from this superclass
	protected static final int CREATED_IN_SUBCLASS = 0;
	protected static final int CREATED_IN_GENERIC_WAY= 1;

	private static final int BUILD_FLAG_BUILD = 0;
	private static final int BUILD_FLAG_FIND_NOT_NULL = 1;
	private static final int BUILD_FLAG_FIND_NULL = 2;

	static private final Object TEMP_KEY_ARM_SIDE = new Object();
	CMappedARMEntity nextArm = null;

	static private String attributeStateStrings[] = 
		new String[] { "ATTRIBUTES_UNKNOWN", "ATTRIBUTES_UNMODIFIED", "ATTRIBUTES_MODIFIED" };

	private int attributeState = ATTRIBUTES_MODIFIED;

	// AIM instance that ARM entity maps to.
	protected EEntity aimInstance = null;

	// entity mapping that is found and used for creating AIM data
	protected EEntity_mapping entityMapping = null;
	protected int creationType = CREATED_IN_SUBCLASS; // 0 - subclass's createAimData(), 1 - CMappedARMEntity.createAimData()

	// temporary variable that is set when creating AIM data, for not passing through parameter list over and over
	SdaiContext contextTmp;

	// AIM instance's type, that is resolved from entity mapping's constraint path.
	protected EEntity_definition mappingTarget = null;

	protected Map attrMappingsMap = null;

	// ICM (class that find and replaces instances' attributes with "reuses") instance, that is common
	// for all entity_mapping path instances.
	static protected ThreadLocal icmThreadLocal = new ThreadLocal();

	// indicator if instance is an ICM owner and should perform its store() action.
	boolean icmOwner = false;

	protected CMappedARMEntity() { }

	protected final InstanceCreationManager getICM() throws SdaiException {
		InstanceCreationManager icm = (InstanceCreationManager) icmThreadLocal.get();
		if (icm == null) {
			icm = new InstanceCreationManager(this);
			setICM(icm);
			getICM().clear();
			icmOwner = true;
		}

		return icm;
	}

	protected final void setICM(InstanceCreationManager icm) {
		icmThreadLocal.set(icm);
	}

    public void setMappingTarget(EEntity_definition edef) throws SdaiException {
		if(Implementation.mappingSupport) {
			mappingTarget = edef;
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
    }

	public void unsetMappingTarget() throws SdaiException {
		if(Implementation.mappingSupport) {
			// unset mappingTarget to find entity_mapping automatically
			mappingTarget = null;
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	protected void resolveMapping(SdaiContext context) throws SdaiException {
		if (mappingTarget != null) {
			AEntity_mapping mappings = new AEntity_mapping();
			int mappingsCount = findARMEntityMappings(context, mappings, EEntity.MOST_SPECIFC_ENTITY);

			SdaiIterator it = mappings.createIterator();
			while (it.next()) {
				EEntity_mapping mapping = mappings.getCurrentMember(it);
				EEntity mappingTargetEntity = mapping.getTarget(null);

				if (mappingTargetEntity instanceof EEntity_definition) {
					EEntity_definition mt = (EEntity_definition) mappingTargetEntity;
					if (mt.equals(mappingTarget)) {
						entityMapping = mapping;
						return;
					}
				}
			}
			throw new SdaiException(SdaiException.ED_NVLD, "Mapping wasn't found for this target: " + mappingTarget + ", ARMEntity=" + this);
		}
	}

	AEntity_definition parseComplexType() throws SdaiException {
		AEntity_definition res = new AEntity_definition();
		Class ownClass = this.getClass();
		while (true) {
			String fullName = ownClass.getName();
			int dotPos = fullName.lastIndexOf('.');
			String name = fullName.substring(dotPos + 1, fullName.length());

			if (!name.startsWith("Cx")) {
				break;
			}
			ownClass = ownClass.getSuperclass();
		}
		Class[] ifaces = ownClass.getInterfaces();

		for (int i = 0; i < ifaces.length; i++) {
			if (EMappedARMEntity.class.isAssignableFrom(ifaces[i]) &&
			   (ifaces[i] != EMappedARMEntity.class)) {
				String ifaceName = ifaces[i].getName();
				int dotPos = ifaceName.lastIndexOf('.');
				String packageName = ifaceName.substring(0, dotPos);
				String className = packageName + ".C" + ifaceName.substring(dotPos + 2, ifaceName.length());
				try {
					Class armClass =
						Class.forName(className, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
					CMappedARMEntity instance = (CMappedARMEntity) armClass.newInstance();
					EEntity_definition nextEdef = instance.getInstanceType();
					res.addUnordered(nextEdef);
				} catch	(Exception e) {
				}
			}
		}

//		parseComplexTypeRecursion(model, edef.getName(null), res);
		return res;
	}



	public EEntity_definition getMappingTarget(SdaiContext context) throws SdaiException {
		if(Implementation.mappingSupport) {
			// if mapping or entity mapping target is not calculated yes - do it.
			if ((mappingTarget == null) || (entityMapping == null)) {
				AEntity_mapping mappings = new AEntity_mapping();
				CEntity entityType = (CEntity) getInstanceType();
				// finds ARM instance's suitable entity mapping from context's mapping data
				entityMapping = ARM2AIMMappingManager.getAIMEntityMapping(context, this);
				EEntity target = entityMapping.getTarget(null);
				mappingTarget = (EEntity_definition) target;
			}
			return mappingTarget;
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public int getAttributeState() {
		return attributeState;
	}

	/**
     * @since 3.6.0
     */
    public void setAttributeState(int attributeState) {
		this.attributeState = attributeState;
	}

	protected boolean getModified() {
		return attributeState == ATTRIBUTES_MODIFIED;
	}

	protected void setModified(boolean bModified) throws SdaiException {
		if(attributeState == ATTRIBUTES_UNKNOWN) {
			throw new SdaiException(SdaiException.SY_ERR, "Attribute values are unknown");
		}
		attributeState = bModified ? ATTRIBUTES_MODIFIED : ATTRIBUTES_UNMODIFIED;
	}

	public boolean applyMappedAttributes(SdaiContext context) throws SdaiException {
		if(Implementation.mappingSupport) {
			if(aimInstance == null) {
				throw new SdaiException(SdaiException.SY_ERR, "No target instance assigned");
			}
			switch(attributeState) {
			case ATTRIBUTES_MODIFIED:
				throw new SdaiException(SdaiException.SY_ERR, "Attribute values have been modified");
			case ATTRIBUTES_UNMODIFIED:
				return false;
			}
			attributeState = ATTRIBUTES_UNMODIFIED;
			ObjectMapping objectMapping = Mapping.getEntityMapping(owning_model.repository.session,
																   context.domain, context.mappingDomain);
			EEntity_definition armEntity = getInstanceType();
			EExplicit_attribute[] entityAttr = ((CEntityDefinition)armEntity).attributes;
			Field[] entityAttrFields = ((CEntityDefinition)armEntity).attributeFields;
			for(int i = entityAttr.length - 1; i >= 0 ; i--) {
				if(entityAttrFields[i] == null) continue;
				EEntity domain = entityAttr[i].getDomain(null);
				AGeneric_attribute_mapping attMappings = new AGeneric_attribute_mapping();
				CGeneric_attribute_mapping.usedinSource(null, entityAttr[i], 
														context.mappingDomain, attMappings);
				SdaiIterator attMapIter = attMappings.createIterator();
				while(attMapIter.next()) {
					EGeneric_attribute_mapping attMapping = attMappings.getCurrentMember(attMapIter);
					Object attrValue = objectMapping.getMappedAttribute(aimInstance, attMapping, 0, true);
					if(assignMappedValue(context, entityAttr[i], domain, attrValue,
										 null, -1, attMapping, null)) {
						break;
					}
				}
			}
			attributeState = ATTRIBUTES_UNMODIFIED;
			return true;
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/**
     * @since 3.6.0
     */
    public boolean assignMappedValue(SdaiContext context, EExplicit_attribute attribute, Object attrValue,
									 EGeneric_attribute_mapping genAttMapping) throws SdaiException {
		if(Implementation.mappingSupport) {
			return assignMappedValue(context, attribute, attribute.getDomain(null), attrValue,
									 null, -1, genAttMapping, null);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/**
     * @since 3.6.0
     */
    public boolean assignMappedValue(SdaiContext context, EExplicit_attribute attribute, Object attrValue,
									 ANamed_type dataType) throws SdaiException {
		if(Implementation.mappingSupport) {
			return assignMappedValue(context, attribute, attribute.getDomain(null), attrValue,
									 null, -1, null, dataType);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	private boolean assignMappedValue(SdaiContext context, Object attrOrAggr, EEntity domain, 
									  Object attrValue, EDefined_type selectTypes[], int selectPos,
									  EGeneric_attribute_mapping genAttMapping,
									  ANamed_type dataType) throws SdaiException {
		if(domain instanceof ESimple_type) {
			if(attrValue == null) {
				return false;
			}
			//FIXME: Enhance AIM - ARM attribute type matching
			if(domain instanceof EInteger_type && attrValue instanceof Double) {
				attrValue = new Integer(((Double)attrValue).intValue());
			} else if(domain instanceof EString_type && !(attrValue instanceof String)) {
				attrValue = attrValue.toString();
			}
			try {
				assignAttrOrAggr(attrOrAggr, attrValue, selectTypes);
				return true;
			} catch(SdaiException e) {
				SdaiException wrapper = 
					new SdaiException(e.getErrorId(), e, "Exception for instance: " + this +
									  "\nattribute: " + attrOrAggr + " value: " + attrValue +
									  " value class: " + attrValue.getClass() +
									  " selectTypes: " + (selectTypes != null ? selectTypes[0] : null) +
									  " mapping: " + genAttMapping + "\n");
				wrapper.initCause(e);
				throw wrapper;
			}
		} else if(domain instanceof EAggregation_type) {
			Aggregate aggregate = createAttrOrAggr(attrOrAggr, selectTypes, attrValue == null);
			if(aggregate != null) {
				EAggregation_type aggregationType = (EAggregation_type)domain;
				assignMappedValue(context, aggregate, aggregationType.getElement_type(null), 
								  attrValue, selectTypes, selectPos, genAttMapping, dataType);
			}
			return false;
		} else if(domain instanceof EDefined_type) {
			EEntity newDomain = domain;
			do {
				newDomain = ((EDefined_type)newDomain).getDomain(null);
			} while(newDomain instanceof EDefined_type);
			if(selectTypes != null && newDomain instanceof ESelect_type) {
				ESelect_type selectDomain = (ESelect_type)newDomain;
				ANamed_type selections = selectDomain.getSelections(null);
				SdaiIterator selectionIter = selections.createIterator();
				while(selectionIter.next()) {
					ENamed_type selection = selections.getCurrentMember(selectionIter);
					if(selection instanceof EDefined_type
					   && assignMappedValue(context, attrOrAggr, selection, attrValue,
											selectTypes, 0, genAttMapping, dataType)) {
						return true;
					}
				}
				return false;
			} else if(selectTypes == null || selectTypes[selectPos] == domain) {
				return assignMappedValue(context, attrOrAggr, newDomain, attrValue,
										 selectTypes, selectPos + 1, genAttMapping, dataType);
			} else {
				return false;
			}
		} else if(domain instanceof EEntity_definition) {
			if(attrValue instanceof EEntity) {
				EEntity attrInstance =
					findOrBuildMappedInstance(context, (EEntity)attrValue,
											  (EEntity_definition)domain, (genAttMapping != null) ?
											  BUILD_FLAG_BUILD : BUILD_FLAG_FIND_NOT_NULL);
				if(attrInstance != null) {
					try {
						assignAttrOrAggr(attrOrAggr, attrInstance, null);
					} catch(SdaiException e) {
						SdaiException wrapper = 
							new SdaiException(e.getErrorId(), e, "Exception for instance: " + this +
											  "\nattribute: " + attrOrAggr + " value: " + attrValue +
											  " value class: " + attrValue.getClass() +
											  " attrInstance: " + attrInstance + " mapping: " +
											  genAttMapping + "\n");
						wrapper.initCause(e);
						throw wrapper;
					}
					return true;
				}
			} else if(attrValue instanceof AEntity) {
				EEntity_definition domainDefinition = (EEntity_definition)domain;
				AEntity aggregate = (AEntity)attrValue;
				SdaiIterator aggregateIter = aggregate.createIterator();
				while(aggregateIter.next()) {
					EEntity aggrInst = aggregate.getCurrentMemberEntity(aggregateIter);
					EEntity attrInstance = 
						findOrBuildMappedInstance(context, aggrInst,
												  domainDefinition, (genAttMapping != null) ?
												  BUILD_FLAG_BUILD : BUILD_FLAG_FIND_NOT_NULL);
					if(attrInstance != null) {
						assignAttrOrAggr(attrOrAggr, attrInstance, null);
					}
				}
				return true;
			} else if(attrValue instanceof Collection) {
				EEntity_definition domainDefinition = (EEntity_definition)domain;
				Iterator collectionIter = ((Collection)attrValue).iterator();
				while(collectionIter.hasNext()) {
					Object value = collectionIter.next();
					if(value instanceof EEntity) {
						EEntity attrInstance = 
							findOrBuildMappedInstance(context, (EEntity)value, domainDefinition,
													  (genAttMapping != null) ?
													  BUILD_FLAG_BUILD : BUILD_FLAG_FIND_NOT_NULL);
						if(attrInstance != null) {
							assignAttrOrAggr(attrOrAggr, attrInstance, null);
						}
					}
				}
				return true;
			}
		} else if(domain instanceof EEnumeration_type) {
			if(attrValue instanceof String) {
				Object origValue = attrValue;
				attrValue =
					new Integer(LangUtils.convertEnumerationStringToInt((String)attrValue,
																		(EEnumeration_type)domain));
				try {
					assignAttrOrAggr(attrOrAggr,
									 attrValue, selectTypes);
				} catch(SdaiException e) {
					SdaiException wrapper = 
					new SdaiException(e.getErrorId(), e, "Exception for instance: " + this +
									  "\nattribute: " + attrOrAggr + "origValue: " + origValue +
									  " value: " + attrValue + " domain: " + domain +
									  " selectTypes: " + selectTypes + " mapping: " +
									  genAttMapping + "\n");
					wrapper.initCause(e);
					throw wrapper;
				}
				return true;
			} else {
				//FIXME: implement enumeration support
				throw new SdaiException(SdaiException.FN_NAVL, 
										"No support for EEnumeration_type attributes yet: " + 
										this + "\n" + genAttMapping + "\n" + domain + "\n" + attrValue);
			}
		} else if(domain instanceof ESelect_type) {
			if(attrValue == null) {
				return false;
			}
			if(attrValue instanceof EEntity || attrValue instanceof AEntity
			   || attrValue instanceof Collection) {
				EAttribute_mapping attrMapping = (EAttribute_mapping)genAttMapping;
				EEntity domainEntity;
				if(attrMapping != null && attrMapping.testDomain(null)
				   && (domainEntity = attrMapping.getDomain(null)) instanceof EEntity_mapping) {
					EEntity_mapping domainMapping = (EEntity_mapping)domainEntity;
					AEntity_mapping baseMappings = new AEntity_mapping();
					baseMappings.addByIndex(1, domainMapping);
					if(attrValue instanceof EEntity) {
						EEntity instance = (EEntity)attrValue;
						AEntity_mapping mostSpecificMappings = 
							instance.findMostSpecificMappings(context.domain, context.mappingDomain,
														  baseMappings, EEntity.NO_RESTRICTIONS);
						//FIXME: What if more than one mapping is returned in mostSpecificMappings?
						if(mostSpecificMappings.getMemberCount() > 0) {
							EEntity attrInstance = 
								Mapping.buildMappedInstance(instance, context, 
															mostSpecificMappings.getByIndex(1));
							if(attrInstance != null) {
								assignAttrOrAggr(attrOrAggr, attrInstance, null);
								return true;
							}
						}
					} else if(attrValue instanceof AEntity) {
						AEntity aggregate = (AEntity)attrValue;
						SdaiIterator aggregateIter = aggregate.createIterator();
						boolean aggResult = false;
						while(aggregateIter.next()) {
							EEntity aggrInst = aggregate.getCurrentMemberEntity(aggregateIter);
							AEntity_mapping mostSpecificMappings = 
								aggrInst.findMostSpecificMappings(context.domain, context.mappingDomain,
																  baseMappings, EEntity.NO_RESTRICTIONS);
							//FIXME: What if more than one mapping is returned in mostSpecificMappings?
							if(mostSpecificMappings.getMemberCount() > 0) {
								EEntity attrInstance = 
									Mapping.buildMappedInstance(aggrInst, context, 
																mostSpecificMappings.getByIndex(1));
								if(attrInstance != null) {
									assignAttrOrAggr(attrOrAggr, attrInstance, null);
									aggResult = true;
								}
							}
						}
						return aggResult;
					} else if(attrValue instanceof Collection) {
						Iterator collectionIter = ((Collection)attrValue).iterator();
						boolean aggResult = false;
						while(collectionIter.hasNext()) {
							Object value = collectionIter.next();
							if(value instanceof EEntity) {
								EEntity aggrInst = (EEntity)value;
								AEntity_mapping mostSpecificMappings = 
									aggrInst.findMostSpecificMappings(context.domain,
																	  context.mappingDomain,
																	  baseMappings,
																	  EEntity.NO_RESTRICTIONS);
								//FIXME: What if more than one mapping is returned in mostSpecificMappings?
								if(mostSpecificMappings.getMemberCount() > 0) {
									EEntity attrInstance = 
										Mapping.buildMappedInstance(aggrInst, context, 
																	mostSpecificMappings.getByIndex(1));
									if(attrInstance != null) {
										assignAttrOrAggr(attrOrAggr, attrInstance, null);
										aggResult = true;
									}
								}
							}
						}
						return aggResult;
					}
				} else {
					ESelect_type selectDomain = (ESelect_type)domain;
					if(attrValue instanceof EEntity) {
						if(!assignGeneralEntitySelect(context, attrOrAggr, selectDomain,
													  (EEntity)attrValue, genAttMapping != null)) {
							//FIXME: implement the case when entity domain as entity_mapping isn't given
							throw new SdaiException(SdaiException.FN_NAVL, 
													"No support for selects yet (1): " + 
													this + "\nattrOrAggr: " + attrOrAggr +
													"\nselectDomain: " + selectDomain +
													"\nattrValue: " + attrValue);
						}
						return true;
					} else if(attrValue instanceof AEntity) {
						AEntity aggregate = (AEntity)attrValue;
						SdaiIterator aggregateIter = aggregate.createIterator();
						boolean aggResult = false;
						while(aggregateIter.next()) {
							EEntity aggrInst = aggregate.getCurrentMemberEntity(aggregateIter);
							aggResult |= assignGeneralEntitySelect(context, attrOrAggr, selectDomain,
																   aggrInst, genAttMapping != null);
						}
						return aggResult;
					} else {
						Iterator collectionIter = ((Collection)attrValue).iterator();
						boolean aggResult = false;
						while(collectionIter.hasNext()) {
							Object value = collectionIter.next();
							if(value instanceof EEntity) {
								//FIXME: We don't support mixed aggregates (instances+simple values)
								aggResult |= assignGeneralEntitySelect(context, attrOrAggr, selectDomain,
																	   (EEntity)value,
																	   genAttMapping != null);
							}
						}
						return aggResult;
					}
				}
			} else {
				if(genAttMapping != null) {
					dataType = genAttMapping.testData_type(null) ?
						genAttMapping.getData_type(null) : null;
				}
				if(dataType == null) {
					SelectType domainType = (SelectType)domain;
					EDefined_type[] select = null;
					for(int i = 0; i < domainType.count; i++) {
						if(domainType.paths[i].length == 1) {
							EDefined_type selectOnePath = domainType.paths[i][0];
							EEntity selectDomain = selectOnePath.getDomain(null);
							if((attrValue instanceof String
								&& selectDomain instanceof EString_type)
							   || (attrValue instanceof Integer
								   && selectDomain instanceof EInteger_type)
							   || (attrValue instanceof Double
								   && selectDomain instanceof EReal_type)) {
								select = domainType.paths[i];
								break;
							}
						}
					}
					if(select != null) {
						assignAttrOrAggr(attrOrAggr, attrValue, select);
						return true;
					} else {
						String attrValueString;
						if(attrValue instanceof Object[]) {
							Object attrValueArray[] = (Object[])attrValue;
							attrValueString = "[ ";
							for(int i = 0; i < attrValueArray.length; i++) {
								attrValueString += attrValueArray[i] + " ";
							}
							attrValueString += "]";
						} else {
							attrValueString = String.valueOf(attrValue);
						}
						throw new SdaiException(SdaiException.SY_ERR, 
												"No suitable select path found: " + this + "\n" +
												genAttMapping + "\n" + domain + "\n" + attrValueString);
					}
				} else {
					ESelect_type selectDomain = (ESelect_type)domain;
					EDefined_type[] select = new EDefined_type[dataType.getMemberCount()];
					int selectIdx = 0;
					SdaiIterator dataTypeIter = dataType.createIterator();
					while(dataTypeIter.next()) {
						ENamed_type dataTypeElem = dataType.getCurrentMember(dataTypeIter);
						if(dataTypeElem instanceof EDefined_type) {
							select[selectIdx++] = (EDefined_type)dataTypeElem;
						}
					}
					
					ANamed_type selections = selectDomain.getSelections(null);
					SdaiIterator selectionIter = selections.createIterator();
					while(selectionIter.next()) {
						ENamed_type selection = selections.getCurrentMember(selectionIter);
						if(selection instanceof EDefined_type
						   && assignMappedValue(context, attrOrAggr, selection, attrValue,
												select, 0, genAttMapping, dataType)) {
							return true;
						}
					}
					return false;
					//FIXME: implement selects of defined type when mapping.data_type is given
// 					throw new SdaiException(SdaiException.FN_NAVL, 
// 											"No support for selects yet (2): " + 
// 											this + "\n" + genAttMapping + "\n" + domain);
				}
			}
		}
		return false;
	}

	private void assignAttrOrAggr(Object attrOrAggr, Object attrValue,
								  EDefined_type[] select) throws SdaiException {
		if(attrOrAggr instanceof EAttribute) {
			set((EAttribute)attrOrAggr, attrValue, select);
		} else if(attrOrAggr instanceof Aggregate) {
			Aggregate aggr = (Aggregate)attrOrAggr;
			EAggregation_type aggregationType = aggr.getAggregationType();
			if(aggregationType instanceof ESet_type || aggregationType instanceof EBag_type) {
				aggr.addUnordered(attrValue, select);
			} else {
				aggr.addByIndex(aggr.getMemberCount() + 1, attrValue, select);
			}
		}
	}

	private Aggregate createAttrOrAggr(Object attrOrAggr, EDefined_type[] selectTypes,
									   boolean mandatoryOnly) throws SdaiException {
		if(attrOrAggr instanceof EAttribute) {
			EAttribute attribute = (EAttribute)attrOrAggr;
			if(mandatoryOnly 
			   && (!(attribute instanceof EExplicit_attribute) 
				   || ((EExplicit_attribute)attribute).getOptional_flag(null))) {
				return null;
			}
			EDefined_type tempSelect[] = new EDefined_type[20];
			if(testAttribute(attribute, tempSelect) == 0) {
				return createAggregate(attribute, selectTypes);
			} else {
				return (Aggregate)get_object(attribute);
			}
		} else if(attrOrAggr instanceof Aggregate) {
			if(mandatoryOnly) {
				return null;
			}
			Aggregate aggr = (Aggregate)attrOrAggr;
			EAggregation_type parentAggrType = aggr.getAggregationType();
			if(parentAggrType instanceof ESet_type || parentAggrType instanceof EBag_type) {
				return aggr.createAggregateUnordered(selectTypes);
			} else {
				return aggr.createAggregateByIndex(aggr.getMemberCount() + 1, selectTypes);
			}
		}
		throw new SdaiException(SdaiException.SY_ERR, 
								"FATAL! Nothing known about this attrOrAggr type: " + attrOrAggr);
	}

	private boolean assignGeneralEntitySelect(SdaiContext context, Object attrOrAggr, 
											  ESelect_type selectDomain, EEntity attrValue,
											  boolean build) throws SdaiException {
		ANamed_type selections = selectDomain.getSelections(null);
		SdaiIterator selectionIter = selections.createIterator();
		int entDefCount = 0;
		while(selectionIter.next()) {
			ENamed_type selection = selections.getCurrentMember(selectionIter);
			if(selection instanceof EEntity_definition) {
				entDefCount++;
				EEntity attrInstance =
					findOrBuildMappedInstance(context, (EEntity)attrValue,
											  (EEntity_definition)selection, build ?
											  BUILD_FLAG_BUILD : BUILD_FLAG_FIND_NULL);
				if(attrInstance != null) {
					assignAttrOrAggr(attrOrAggr, attrInstance, null);
					return true;
				}
			} else if(selection instanceof EDefined_type) {
				EEntity definedTypeDomain = ((EDefined_type)selection).getDomain(null);
				if(definedTypeDomain instanceof ESelect_type) {
					if(assignGeneralEntitySelect(context, attrOrAggr, (ESelect_type)definedTypeDomain,
												 attrValue, build)) {
						return true;
					}
				}
			}

		}
		// If only one entity is in the select (as with undefined_object)
		if(entDefCount == 1) {
			selectionIter.beginning();
			while(selectionIter.next()) {
				ENamed_type selection = selections.getCurrentMember(selectionIter);
				if(selection instanceof EEntity_definition) {
					EMappedARMEntity attrInstance = 
						(EMappedARMEntity)context.mappedWorkingModel
						.createEntityInstance((EEntity_definition)selection);
					attrInstance.setAimInstance((EEntity)attrValue);
					((CMappedARMEntity)attrInstance).setAttributeState(ATTRIBUTES_UNKNOWN);
					assignAttrOrAggr(attrOrAggr, attrInstance, null);
					return true;
				}
			}
		}
		return false;
	}

	static private EEntity findOrBuildMappedInstance(SdaiContext context, EEntity instance,
													 EEntity_definition entityDefinition,
													 int buildFlag) throws SdaiException {
		EEntity armInstance = (buildFlag == BUILD_FLAG_BUILD) ? 
			instance.buildMappedInstance(context, entityDefinition) :
			instance.findLinkedMappedInstance(entityDefinition);
		if(buildFlag == BUILD_FLAG_FIND_NOT_NULL && armInstance == null) {
			throw new SdaiException(SdaiException.SY_ERR, "Can not find mapped instance: " + instance +
									"\nentity definition: " + entityDefinition);
		} else {
			return armInstance;
		}
	}

	public void createAimData(SdaiContext context) throws SdaiException {
/*		if ((this instanceof jsdai.SAp210_arm.EInterconnect_module_edge_surface) ||
			(this instanceof jsdai.SAp210_arm.EInterconnect_module_component_surface_feature) ||
			(this instanceof jsdai.SAp210_arm.EInterconnect_module_primary_surface) ||
			(this instanceof jsdai.SAp210_arm.EInterconnect_module_secondary_surface)){
			return;
		}
*/
		if(Implementation.mappingSupport) {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
				creationType = CREATED_IN_GENERIC_WAY;

				// 1, 2 STEPS
				boolean wasCreated = (aimInstance != null);
				unsetMappingTarget();
				commonForAIMInstanceCreation(context);

				// insert into ICM entry whose attributes will be updated for to use the "reuses"
				if (!wasCreated) {
					getICM().mainInstances.add(aimInstance);
				}

				setSimpleAttributes(context);
			}
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		if(Implementation.mappingSupport) {
			throw new SdaiException(SdaiException.EX_NSUP, 
									"removeAimData is not implemented in generic level");
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public EEntity getAimInstance() {
		return aimInstance;
	}

	public void setAimInstance(EEntity aimInstance) {
		if(aimInstance != null) {
			this.aimInstance = aimInstance;
			addNextArmInstance();
		} else {
			if(this.aimInstance != null) {
				removeNextArmInstance();
			}
			this.aimInstance = null;
		}
	}

	private void addNextArmInstance() {
		if(aimInstance != null) {
			CMappedARMEntity firstArm = 
				(CMappedARMEntity)((CEntity)aimInstance).getTemp(TEMP_KEY_ARM_SIDE);
			if(firstArm == null) {
				((CEntity)aimInstance).setTemp(TEMP_KEY_ARM_SIDE, this);
			} else {
				while(firstArm.nextArm != null) firstArm = firstArm.nextArm;
				firstArm.nextArm = this;
			}
		}
		nextArm = null;
	}

	private void removeNextArmInstance() {
		CMappedARMEntity firstArm = (CMappedARMEntity)((CEntity)aimInstance).getTemp(TEMP_KEY_ARM_SIDE);
		if(firstArm != null) {
			CMappedARMEntity prevArm = null;
			while(firstArm != null && firstArm != this) {
				prevArm = firstArm;
				firstArm = firstArm.nextArm;
			}
			if(firstArm != null) {
				if(prevArm != null) {
					prevArm.nextArm = nextArm;
				} else {
					((CEntity)aimInstance).setTemp(TEMP_KEY_ARM_SIDE, nextArm);
				}
			}
		}
		nextArm = null;
	}

	static CMappedARMEntity getFirstArmInstance(EEntity aimInstance) {
		return (CMappedARMEntity)aimInstance.getTemp(TEMP_KEY_ARM_SIDE);
	}

	/**
     * @since 3.6.0
     */
    public EMappedARMEntity getNextArmInstance() {
		return nextArm;
	}

	static void removeArmInstances(EEntity aimInstance) throws SdaiException {
		CMappedARMEntity firstArm = (CMappedARMEntity)aimInstance.getTemp(TEMP_KEY_ARM_SIDE);
		while(firstArm != null) {
			CMappedARMEntity nextFirstArm = firstArm.nextArm;
			firstArm.aimInstance = null;
			firstArm.nextArm = null;
			firstArm = nextFirstArm;
		}
		aimInstance.setTemp(TEMP_KEY_ARM_SIDE, null);
	}

	static EMappedARMEntity findLinkedMappedInstance(EEntity aimInstance, 
													 EEntity_definition mappedInstanceType) 
	throws SdaiException {
		CMappedARMEntity firstArm = (CMappedARMEntity)aimInstance.getTemp(TEMP_KEY_ARM_SIDE);
		while(firstArm != null) {
			if(mappedInstanceType == null) {
				return firstArm;
			}
			EEntity_definition firstArmType = firstArm.getInstanceType();
			if(firstArmType == mappedInstanceType
			   || ((CEntityDefinition)firstArmType).isSubtypeOf(mappedInstanceType)) {
				return firstArm;
			}
			firstArm = firstArm.nextArm;
		}
		return null;
	}

	protected int getCreationType() {
		return creationType;
	}

	// returns only explicit attributes
	protected AExplicit_attribute getSimpleAttributes() throws SdaiException {
		EEntity_definition inst_type = getInstanceType();
		AExplicit_attribute attributes = inst_type.getExplicit_attributes(null);
		return attributes;
	}

	// method for creating aggregate_member_constraint's target object:
	// parent (aggregate's owner) - if AMC is met after inverse_attribute_constraint or
	// child (aggregate's entry) - if AMC is found directly in mapping path
	EEntity createAggregateParentOrChild(EAggregate_member_constraint constraint, boolean createParent) throws SdaiException {
		EEntity subConstraint = constraint.getAttribute(null);

		if (subConstraint instanceof EExplicit_attribute) {
			EExplicit_attribute attribute = (EExplicit_attribute) subConstraint;

			if (createParent) {
				EEntity_definition parentEntity = attribute.getParent_entity(null);
				EEntity parent = getICM().createEntityInstance(parentEntity);
				return parent;
			} else {
                EEntity domain = attribute.getDomain(null);
				if (domain instanceof EAggregation_type) {
					EAggregation_type aggr_type = (EAggregation_type) domain;
					EData_type element_type = aggr_type.getElement_type(null);
					if (element_type instanceof EEntity_definition) {
						EEntity_definition childEntity = (EEntity_definition) element_type;
						EEntity child = getICM().createEntityInstance(childEntity);
						return child;
					}
				}
			}
		}
		return null;
	}

	// generic method for creating and assigning parentInstance's attribute (that is not yet set).
	// NOTICE: Newly created value's attributes aren't set.
	protected EEntity createAttributeInstance(EEntity parentInstance, EExplicit_attribute attribute, EEntity_definition edef) throws SdaiException {
		EEntity domain = attribute.getDomain(null);
		if (edef == null) {
			if (domain instanceof EEntity_definition) {
				edef = (EEntity_definition) domain;
			} else {
				return parentInstance;
			}
		}

		EEntity aimSubAttribute = getICM().createEntityInstance(edef);
		if (domain instanceof EAggregation_type) {
			addAggregateItemSimple(parentInstance, attribute, aimSubAttribute, true, 1);
		} else {
			parentInstance.set(attribute, aimSubAttribute, null);
		}

		return aimSubAttribute;
	}

	/**
	 * Adds items to an aggregate.
	 * This method is intended for internal JSDAI use.
	 *
	 * @param parentInstance The entity instance
	 * @param attribute The attribute
	 * @param targetValue The value to add
	 * @param useIndexMask If true then <code>index</code> parameter is used as an index
	 * @param index The index of the member to add.
	 *              Is used only when <code>useIndexMask</code> is true.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void addAggregateItemSimple(EEntity parentInstance, EExplicit_attribute attribute, EEntity targetValue, boolean useIndexMask, int index) throws SdaiException {
		Aggregate aggregateValue = null;
		if (parentInstance.testAttribute(attribute, null) != 0) {
			aggregateValue = (AEntity) parentInstance.get_object(attribute);
		} else {
			aggregateValue = (AEntity) getICM().createAggregate(parentInstance, attribute, null);
		}

		EAggregation_type aggr_type = aggregateValue.getAggregationType();
		boolean addByIndex = useIndexMask && (aggr_type instanceof EList_type);
		if (addByIndex) {
			aggregateValue.addByIndex(index, targetValue, null);
		} else {
			aggregateValue.addUnordered(targetValue, null);
		}
	}

	// method for adding aggregate's target (item) and create aggregate if it doesn't exist
	// finalAimInstance - item to be added, parentInstance - aggregate's owner
	protected void addAggregateItem(EEntity finalAimInstance, EEntity parentInstance, EAggregate_member_constraint amc) throws SdaiException {
		EEntity aggregateAttribute = amc.getAttribute(null);

		if (aggregateAttribute instanceof EExplicit_attribute) {
			EExplicit_attribute attribute = (EExplicit_attribute) aggregateAttribute;
			boolean byIndex = amc.testMember(null);
			int index = 0;
			if (byIndex) {
				index = amc.getMember(null);
			}
			addAggregateItemSimple(parentInstance, attribute, finalAimInstance, byIndex, index);
		} else {
			throw new SdaiException(SdaiException.EI_NVLD, " This type of entity not supported for addAggregateItem()");
		}
	}

	// method for going through inverse_attribute_constraint
	// returns the INVERSE's owner, that will be used for creating AIM instances tree (out of mapping path) next
	protected EEntity createInverseAttribute(EEntity finalAimInstance, EEntity parentInstance, EInverse_attribute_constraint constraints) throws SdaiException {
		EEntity subConstraint = constraints.getInverted_attribute(null);

		// find the inverse owner (aggregate or attribute)
		if (subConstraint instanceof EAggregate_member_constraint) {
			EAggregate_member_constraint amc = (EAggregate_member_constraint) subConstraint;

			if (parentInstance == null) {
				// if not found -> create attribute's owner
				parentInstance = createAggregateParentOrChild(amc, true);
			}

			if (parentInstance == null) {
				return null;
			}

			// set the inverse point to AIM instance passed through parameters
			addAggregateItem(finalAimInstance, parentInstance, amc);
			return parentInstance;
		} else if (subConstraint instanceof EExplicit_attribute) {
			EExplicit_attribute attr = (EExplicit_attribute) subConstraint;

			if (parentInstance == null) {
				// if not found -> create
				EEntity_definition edef = attr.getParent_entity(null);
				parentInstance = getICM().createEntityInstance(edef);
			}

			// set inverse attribute point to AIM instance
			if (attr.getDomain(null) instanceof EAggregation_type) {
//				Aggregate aggr = (AEntity) getICM().createAggregate(parentInstance, attr, null);
//				aggr.addUnordered(finalAimInstance, null);
				addAggregateItemSimple(parentInstance, attr, finalAimInstance, true, 1);
			} else {
				parentInstance.set(attr, finalAimInstance, null);
			}
			return parentInstance;
		} else {
			throw new SdaiException(SdaiException.EI_NVLD, " This type of entity not supported for createInverseAttribute()");
		}
	}

	// method for going through entity_constraint
	// returns next entity for which next mapping path constraints will be applied
	EEntity goThroughEntityConstraint(EEntity finalAimInstance, EEntity_constraint ec, EEntity generatedInstance, boolean useGeneratedInstance) throws SdaiException {
		EEntity attributeSelect = ec.getAttribute(null);

		EExplicit_attribute attribute = null;
		EEntity_definition domain = ec.getDomain(null);

		// check if we already have the instance created and its type matches entity_constraint
		if (useGeneratedInstance && (generatedInstance != null)) {
			if (!generatedInstance.isKindOf(domain)) {
				throw new SdaiException(SdaiException.VA_NVLD, "Wrong type AIM instance created: instance=" + generatedInstance + ", attribute=" + attribute);
			}

			EDefined_type[] types = new EDefined_type[30];

			// check/set the instance attribute have the same values as described in entity_constraint
			if (attributeSelect instanceof EExplicit_attribute) {
				attribute = (EExplicit_attribute) attributeSelect;
				if (finalAimInstance.testAttribute(attribute, types) != 0) {
					Object obj = finalAimInstance.get_object(attribute);
					if (obj != generatedInstance) {
						throw new SdaiException(SdaiException.VA_NVLD, "Instance values should be equal:" +
												obj + ", " + generatedInstance);
					}
				} else {
					finalAimInstance.set(attribute,  generatedInstance, types);
				}
			}
			return generatedInstance;
		}

		if (attributeSelect instanceof EExplicit_attribute) {
			attribute = (EExplicit_attribute) attributeSelect;
			if (finalAimInstance.testAttribute(attribute, null) != 0) {
			    // if attribute is set, then check its type
				Object value = finalAimInstance.get_object(attribute);
				EEntity attributeObject = null;
				if (value instanceof EEntity) {
					attributeObject = (EEntity) value;
				} else if (value instanceof AEntity) {
					AEntity aggr = (AEntity) value;
					if (aggr.getMemberCount() != 1) {
						throw new SdaiException(SdaiException.EI_NVLD, "Only one entity could be in this aggregate. " + aggr);
					}
					attributeObject = aggr.getByIndexEntity(1);
				} else {
					throw new SdaiException(SdaiException.EI_NVLD, "This value is unsupported: " + value);
				}

				if (!attributeObject.isKindOf(domain)) {
					throw new SdaiException(SdaiException.VA_NVLD, "Wrong type AIM instance created: instance=" + finalAimInstance + ", attribute=" + attribute);
				}
				return (EEntity) attributeObject;
			} else {
				// if attribute is not set -> create it.
				EEntity aimSubAttribute = createAttributeInstance(finalAimInstance, attribute, domain);
				return aimSubAttribute;
			}
		} else if (attributeSelect instanceof EInverse_attribute_constraint) {
			// create Inverse's owner, its attribute and set the value to finalAimInstance
			EEntity subInstance = getICM().createEntityInstance(domain);
			createInverseAttribute(finalAimInstance, subInstance, (EInverse_attribute_constraint) attributeSelect);
			return subInstance;
		} else if (attributeSelect instanceof EAggregate_member_constraint) {
			EAggregate_member_constraint amc = (EAggregate_member_constraint) attributeSelect;
			// create aggregate and add finalAimInstance to its item list
			EEntity aggregateItem = getICM().createEntityInstance(domain);
			addAggregateItem(aggregateItem, finalAimInstance, amc);
			return aggregateItem;
		} else {
			throw new SdaiException(SdaiException.ED_NVLD, "This type of attribute is not supported yet." +
									attributeSelect);
			// return null;
		}
	}

	// method for creating AIM data out of constraints not belonging to main mapping path
	EEntity makeConstraintStep(EEntity finalAimInstance, EEntity constraints, EEntity generatedInstance, boolean useGeneratedInstance) throws SdaiException {
		if (constraints instanceof EExplicit_attribute) {
			EExplicit_attribute attribute = (EExplicit_attribute) constraints;
			if (finalAimInstance.testAttribute(attribute, null) != 0) {
				Object attributeObject = finalAimInstance.get_object(attribute);
				return (EEntity) attributeObject;
			} else {
				EEntity aimSubAttribute = createAttributeInstance(finalAimInstance, attribute, null);
				return aimSubAttribute;
			}
		} else if (constraints instanceof EEntity_constraint) {
			return goThroughEntityConstraint(finalAimInstance, (EEntity_constraint) constraints, generatedInstance, useGeneratedInstance);
		} else if (constraints instanceof EInverse_attribute_constraint) {
			EInverse_attribute_constraint iac = (EInverse_attribute_constraint) constraints;
			EEntity inverseAttribute = createInverseAttribute(finalAimInstance, null, iac);
			return inverseAttribute;
		} else if (constraints instanceof EPath_constraint) {
			EPath_constraint pc = (EPath_constraint) constraints;
			EEntity element1 = pc.getElement1(null);
			EEntity element2 = pc.getElement2(null);
			finalAimInstance = makeConstraintStep(finalAimInstance, element1, generatedInstance, useGeneratedInstance);

    		if (finalAimInstance == null) {
				throw new SdaiException(SdaiException.VA_NVLD, "makeConstraintStep() returned null on constraint: " +
										element1);
			}

			finalAimInstance = makeConstraintStep(finalAimInstance, element2, generatedInstance, useGeneratedInstance);
			return finalAimInstance;
		} else if (constraints instanceof EAttribute_value_constraint) {
			EAttribute_value_constraint avc = (EAttribute_value_constraint) constraints;
			setAttributeValue(finalAimInstance, avc);
			return finalAimInstance;
		} else if (constraints instanceof EAggregate_member_constraint) {
			EAggregate_member_constraint amc = (EAggregate_member_constraint) constraints;
			EEntity aggregate_child = createAggregateParentOrChild(amc, false);
			addAggregateItem(aggregate_child, finalAimInstance, amc);
			return aggregate_child;
		} else if (constraints instanceof EOr_constraint_relationship) {
			EOr_constraint_relationship ocr = (EOr_constraint_relationship) constraints;
			EEntity element2 = ocr.getElement2(null);
//			System.out.println("Warning: Or_relationship_constraint was met - using element2 branch: " + ocr);
			return makeConstraintStep(finalAimInstance, element2, generatedInstance, useGeneratedInstance);
		} else if (constraints instanceof EAnd_constraint_relationship) {
			EAnd_constraint_relationship acr = (EAnd_constraint_relationship) constraints;
			EEntity element1 = acr.getElement1(null);
			EEntity element2 = acr.getElement2(null);
			makeConstraintStep(finalAimInstance, element1, generatedInstance, useGeneratedInstance);
			makeConstraintStep(finalAimInstance, element2, generatedInstance, useGeneratedInstance);

			return finalAimInstance;
		} else {
			throw new SdaiException(SdaiException.EI_NVLD,
									" This type of constraint is not implemented in makeConstraintStep(): " +
									constraints.toString());
		}
	}

	protected void setAttributeValue(EEntity finalAimInstance, EAttribute_value_constraint avc) throws SdaiException {
		EEntity attr = avc.getAttribute(null);

		if (attr instanceof EExplicit_attribute) {
			EExplicit_attribute expl_attr = (EExplicit_attribute) attr;
			EEntity attrInstance = createAttributeInstance(finalAimInstance, expl_attr, null);

			if (avc instanceof EString_constraint) {
				EString_constraint strc = (EString_constraint) avc;
				String value = strc.getConstraint_value(null);
				attrInstance.set(expl_attr, value, null);
			} else if (avc instanceof ELogical_constraint) {
				ELogical_constraint lc = (ELogical_constraint) avc;
				int value = lc.getConstraint_value(null);
				attrInstance.set(expl_attr, value, null);
			} else if (avc instanceof EInteger_constraint) {
				EInteger_constraint ic = (EInteger_constraint) avc;
				int value = ic.getConstraint_value(null);
				attrInstance.set(expl_attr, value, null);
			} else if (avc instanceof EEnumeration_constraint) {
				EEnumeration_constraint enumc = (EEnumeration_constraint) avc;
				EDefined_type dt = (EDefined_type)expl_attr.getDomain(null);
				EEnumeration_type enumDomain = (EEnumeration_type)dt.getDomain(null); // bug. This will not work for selects.

				String value = enumc.getConstraint_value(null);

				int convertedValue = LangUtils.convertEnumerationStringToInt(value, enumDomain);
				attrInstance.set(expl_attr, new Integer(convertedValue), null);
			} else {
				throw new SdaiException(SdaiException.ED_NVLD, "This type of attribute_value_constraints isn't implemented yet: " +
										avc.toString());
			}
		} else if (attr instanceof EDerived_attribute) {
			System.out.println("WARNING: derived attribute was encountered in attribute_value_constraint: " + avc);
		} else {
			throw new SdaiException(SdaiException.ED_NVLD,
									"This kind of attribute isn't supported in setAttributeValue(): " + attr.toString());
		}

	}

	protected boolean useGeneratedInstance(EEntity constraints) throws SdaiException {
		EEntity subConstraint = null;
		if (constraints instanceof EAnd_constraint_relationship) {
			EAnd_constraint_relationship acr = (EAnd_constraint_relationship) constraints;
			subConstraint = acr.getElement1(null);
		} else if (constraints instanceof EPath_constraint) {
			EPath_constraint pc = (EPath_constraint) constraints;
			subConstraint = pc.getElement2(null);
		}

		if ((subConstraint instanceof EPath_constraint) ||
		   (subConstraint instanceof EAnd_constraint_relationship)) {
			return false;
		}

		return true;
	}


	private void createAttributeFromPath(EEntity finalAimInstance, EAttribute armAttribute, EEntity constraints, EEntity_definition targetDefinition, EEntity generatedAimInstance, boolean useGeneratedInstance, EDefined_type[] types) throws SdaiException {
		if (constraints instanceof EExplicit_attribute) {
			EExplicit_attribute aimAttribute = (EExplicit_attribute) constraints;

			if (types == null) {
				types = getDefinedTypes(aimAttribute);
			}

			setSimpleAttribute(finalAimInstance, aimAttribute, armAttribute, contextTmp, types);
		} else if (constraints instanceof EAggregate_member_constraint) {
			EEntity subConstraints = ((EAggregate_member_constraint)constraints).getAttribute(null);
			createAttributeFromPath(finalAimInstance, armAttribute, subConstraints, targetDefinition, generatedAimInstance, useGeneratedInstance, types);
		} else if (constraints instanceof EPath_constraint) {
			EPath_constraint pc = (EPath_constraint) constraints;
			EEntity element1 = pc.getElement1(null);
			EEntity element2 = pc.getElement2(null);
			finalAimInstance = makeConstraintStep(finalAimInstance, element1, generatedAimInstance, useGeneratedInstance);

			if (finalAimInstance == null) {
				throw new SdaiException(SdaiException.VA_NVLD, "makeConstraintStep() returned null on constraint: " +
										element1);
			}

			createAttributeFromPath(finalAimInstance, armAttribute, element2, targetDefinition, generatedAimInstance, useGeneratedInstance(pc), types);
		} else if (constraints instanceof EAnd_constraint_relationship) {
			EAnd_constraint_relationship acr = (EAnd_constraint_relationship) constraints;
			makeConstraintStep(finalAimInstance, acr.getElement2(null), generatedAimInstance, useGeneratedInstance);
			createAttributeFromPath(finalAimInstance, armAttribute, acr.getElement1(null), targetDefinition, generatedAimInstance, useGeneratedInstance(acr), types);
		} else if (constraints instanceof EEnd_of_path_constraint) {
			EEnd_of_path_constraint eopc = (EEnd_of_path_constraint) constraints;
			createAttributeFromPath(finalAimInstance, armAttribute, eopc.getConstraints(null), targetDefinition, generatedAimInstance, true, types);
		} else if (constraints instanceof EAttribute_value_constraint) {
			EAttribute_value_constraint avc = (EAttribute_value_constraint) constraints;
			setAttributeValue(finalAimInstance, avc);
		} else if (constraints instanceof EEntity_constraint) {
			goThroughEntityConstraint(finalAimInstance, (EEntity_constraint) constraints, generatedAimInstance, useGeneratedInstance);
		} else if (constraints instanceof EInverse_attribute_constraint) {
			EInverse_attribute_constraint iac = (EInverse_attribute_constraint) constraints;
			createInverseAttribute(finalAimInstance, null, iac);
		} else if (constraints instanceof EOr_constraint_relationship) {
            EOr_constraint_relationship ocr = (EOr_constraint_relationship) constraints;
			EEntity element1 = ocr.getElement1(null);
			createAttributeFromPath(finalAimInstance, armAttribute, element1, targetDefinition, generatedAimInstance, useGeneratedInstance, types);
		} else if (constraints instanceof ESelect_constraint) {
			ESelect_constraint sc = (ESelect_constraint) constraints;
			ADefined_type aType = sc.getData_type(null);
			EEntity attr = sc.getAttribute(null);

			int count = aType.getMemberCount();
			types = new EDefined_type[count];
			for (int i = 0; i < count; i++) {
				types[i] = aType.getByIndex(i + 1);
			}
		    createAttributeFromPath(finalAimInstance, armAttribute, attr, targetDefinition, generatedAimInstance, useGeneratedInstance(attr), types);
		} else {
			throw new SdaiException(SdaiException.ED_NVLD,
									"This type of constraint not supported for createAttributeFromPath(): " +
									constraints.toString());
		}
	}

	protected static AGeneric_attribute_mapping getSimpleMappings(EEntity_mapping em, SdaiContext context) throws SdaiException {
		AGeneric_attribute_mapping agam = new AGeneric_attribute_mapping();
		AGeneric_attribute_mapping rv = new AGeneric_attribute_mapping();
		CGeneric_attribute_mapping.usedinParent_entity(null, em, context.mappingDomain, agam);
		SdaiIterator i = agam.createIterator();
		while (i.next()) {
			EGeneric_attribute_mapping gam = agam.getCurrentMember(i);
			// find if this is simple mapping
			EEntity constraints = gam.getConstraints(null);
			if (constraints instanceof EExplicit_attribute) {
				 rv.addUnordered(gam);
			}
		}
		return rv;
	}

	EEntity getFinalAIMdomain(EEntity constraints) throws SdaiException {
		if (constraints instanceof EPath_constraint) {
			EPath_constraint pc = (EPath_constraint) constraints;
			EEntity branch = pc.getElement2(null);
			if (branch instanceof EEnd_of_path_constraint) {
				branch = pc.getElement1(null);
			}

			return getFinalAIMdomain(branch);
		} else if (constraints instanceof EAnd_constraint_relationship) {
			EAnd_constraint_relationship acr = (EAnd_constraint_relationship) constraints;
			return getFinalAIMdomain(acr.getElement1(null));
		} else if (constraints instanceof EExplicit_attribute) {
			EExplicit_attribute attribute = (EExplicit_attribute) constraints;
			return attribute.getDomain(null);
		} else if (constraints instanceof EAggregate_member_constraint) {
			EAggregate_member_constraint amc = (EAggregate_member_constraint) constraints;
			EEntity attribute = amc.getAttribute(null);
			EEntity entity = getFinalAIMdomain(attribute);
			if (entity != null) {
				if (entity instanceof ESet_type) {
					ESet_type setType = (ESet_type) entity;
					entity = setType.getElement_type(null);
				}
			}
			return entity;
		} else if (constraints instanceof EEntity_constraint) {
			EEntity_constraint ec = (EEntity_constraint) constraints;
			return ec.getDomain(null);
		}

//		throw new SdaiException(SdaiException.ED_NVLD, " This type of entity isn't checked in getFinalARMdefintion.");
		return null;
	}

	protected boolean matchesAttributeMappingValue(EAttribute_mapping_value amv) throws SdaiException {
		EAttribute attr = amv.getSource(null);

		if (!(attr instanceof EExplicit_attribute)) {
			throw new SdaiException(SdaiException.ED_NVLD,
									"This type of attribute_mapping_value source is not implemented in matchesAttributeMappingValue(): " +
									attr);
		}

		EExplicit_attribute expl_attr = (EExplicit_attribute) attr;

		// if enumeration is unset
		if (testAttribute(expl_attr, null) == 0) {
			return false;
		}

        if (amv instanceof EAttribute_mapping_enumeration_value) {
            EAttribute_mapping_enumeration_value amev = (EAttribute_mapping_enumeration_value) amv;
			String mapping_value = amev.getMapped_value(null);
			Integer instance_value = (Integer) get_object(expl_attr);
			int int_value = instance_value.intValue();

			EDefined_type dt = (EDefined_type)expl_attr.getDomain(null);
			EEnumeration_type enumDomain = (EEnumeration_type)dt.getDomain(null); // bug. This will not work for selects.
			String convertedValue = LangUtils.convertEnumerationIntToString(int_value, enumDomain);

			if (mapping_value.compareTo(convertedValue) == 0) {
				return true;
			}
		} else if (amv instanceof EAttribute_mapping_boolean_value) {
			EAttribute_mapping_boolean_value ambv = (EAttribute_mapping_boolean_value) amv;
			boolean mappingValue = ambv.getMapped_value(null);
//			Boolean instance_value = (Boolean) get_object(expl_attr);
			boolean boolean_value = get_boolean(expl_attr);

			if (boolean_value == mappingValue) {
				return true;
			}
		} else {
			throw new SdaiException(SdaiException.ED_NVLD,
					  " This type of attribute_mapping_value is not implemented in matchesAttributeMappingValue()." +
					  amv);
		}
		return false;
	}

	// searches setXXX in the ARM instances "Cx" class, if not found searches up in hierarchy level "Cx" classes
	// returns null if haven't found
	protected Method findAttributeSetter(Class entityClass, EAttribute attribute) throws SdaiException {
		try {
			if (!(attribute instanceof EExplicit_attribute)) {
				return null;
			}
			EExplicit_attribute expl_attr = (EExplicit_attribute) attribute;
			EEntity_definition parentEntity = expl_attr.getParent_entity(null);
			Class attributeDomain = ((CEntityDefinition)parentEntity).getEntityClass();
			String attributeDomainClassName = attributeDomain.getName();
			int dotPos = attributeDomainClassName.lastIndexOf('.');
			String packageName = attributeDomainClassName.substring(0, dotPos);
			String className = attributeDomainClassName.substring(dotPos + 1);
			if (className.startsWith("C")) {
				attributeDomain =
					Class.forName(packageName + ".E" + className.substring(1), true,
							SdaiClassLoaderProvider.getDefault().getClassLoader());
			}

			String entityClassName = entityClass.getName();
			dotPos = entityClassName.lastIndexOf('.');
			packageName = entityClassName.substring(0, dotPos);
			className = entityClassName.substring(dotPos + 3);
			int pos;
			do {
				pos = className.lastIndexOf('$');
				String subClass = className;
				if (pos >= 0) {
					String tmp = className.substring(pos + 1, className.length());
					String firstLetter = tmp.substring(0, 1);
					subClass = firstLetter.toUpperCase() + tmp.substring(1, tmp.length());
					tmp = className.substring(0, pos);
					className = tmp;
				}

				Method method = findAttributeSetter(Class.forName(packageName + ".C" + subClass), attribute.getName(null), attributeDomain);
				if (method != null) {
					return method;
				}
			} while (pos >= 0);
		} catch (Exception e) {
		}

		return null;
	}

	protected Method findAttributeSetter(Class entityClass, String attributeName, Class attributeDomain) {
        Method method = null;
		try {
			String entityClassName = entityClass.getName();
			int dotPos = entityClassName.lastIndexOf('.');
			String packageName = entityClassName.substring(0, dotPos);

			String cxClassName = packageName + ".Cx" + entityClassName.substring(dotPos + 2);
			Class cxClass =
				Class.forName(cxClassName, true, SdaiClassLoaderProvider.getDefault().getClassLoader());

			String methodName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);

			Class[] paramTypes = new Class[2];
			paramTypes[0] = SdaiContext.class;
			paramTypes[1] = attributeDomain;
			method = cxClass.getMethod(methodName, paramTypes);
		} catch (Exception e) {
			if (!((e instanceof NoSuchMethodException) || (e instanceof ClassNotFoundException))) {
				e.printStackTrace();
				return null;
			}

			Class superClass = entityClass.getSuperclass();
			if (superClass != null) {
				method = findAttributeSetter(superClass, attributeName, attributeDomain);
			}
		}

		return method;
	}

	protected void getDefinedTypesRecursion(EEntity domain, ADefined_type types) throws SdaiException {
		if (domain instanceof EDefined_type) {
			EEntity subDomain = ((EDefined_type) domain).getDomain(null);
			if (subDomain instanceof ESelect_type) {
				ANamed_type selections = ((ESelect_type) subDomain).getSelections(null);
				ENamed_type selection = selections.getByIndex(1);
				if (selection instanceof EDefined_type) {
					int count = types.getMemberCount();
					types.addByIndex(count + 1, selection);
					getDefinedTypesRecursion(((EDefined_type) selection).getDomain(null), types);
				}
			}
		}
	}

	protected EDefined_type[] getDefinedTypes(EExplicit_attribute attr) throws SdaiException {
		ADefined_type types = new ADefined_type();
		getDefinedTypesRecursion(attr.getDomain(null), types);
		int count = types.getMemberCount();
		EDefined_type[] rv = new EDefined_type[count];
		for (int i = 0; i < count; i++) {
			rv[i] = types.getByIndex(i + 1);
		}
		return rv;
	}

	protected void fillAttributeMappingMap(AGeneric_attribute_mapping agam) throws SdaiException {
		attrMappingsMap = new HashMap();
		SdaiIterator it = agam.createIterator();
		while (it.next()) {
			EGeneric_attribute_mapping gam = agam.getCurrentMember(it);

			EAttribute armAttribute = gam.getSource(null);
			if (armAttribute instanceof EDerived_attribute) {
				System.out.println("WARNING: derived attribute mapping found: " + gam);
				continue;
			}

			if (attrMappingsMap.containsKey(armAttribute)) {
				continue;
			}

			if (gam instanceof EAttribute_mapping_value) {
				EAttribute_mapping_value amv = (EAttribute_mapping_value) gam;
				if (!matchesAttributeMappingValue(amv)) {
					continue;
				}
			}

			// do nothing if attribute is not set
			EDefined_type[] types = new EDefined_type[30];
			if (testAttribute(armAttribute, types) == 0) {
				continue;
			}

			EEntity constraints = gam.getConstraints(null);
			EEntity finalMappingDomain = getFinalAIMdomain(constraints);
			Object obj = get_object(armAttribute);

			boolean isUseful = false;
			if (obj instanceof EEntity) {
				EMappedARMEntity armInstance = (EMappedARMEntity) obj;
				EEntity_definition instanceDomain = armInstance.getMappingTarget(contextTmp);

				if ((instanceDomain == null) && (finalMappingDomain != null)) {
					throw new SdaiException(SdaiException.ED_NVLD, " AIM instance domain should be set for ARM instance: " + armInstance);
				}

				if (finalMappingDomain instanceof EEntity_definition) {
					if (instanceDomain.isSubtypeOf((EEntity_definition) finalMappingDomain)) {
						isUseful = true;
					}
				}
			} else if (obj instanceof AEntity) {
				AEntity ae = (AEntity) obj;
				SdaiIterator valueIt = ae.createIterator();

				boolean matchesOne = false;
				while (valueIt.next()) {
					EMappedARMEntity value = (EMappedARMEntity) ae.getCurrentMemberEntity(valueIt);
					EEntity_definition instanceDomain = value.getMappingTarget(contextTmp);

					if (instanceDomain.isSubtypeOf((EEntity_definition) finalMappingDomain)) {
						matchesOne = true;
					} else if (matchesOne) {
						throw new SdaiException(SdaiException.EI_NVLD, " Some aggregate values do match mapping, some - not.");
					} else {
						break;
					}
				}
				isUseful = true;
			} else {
				isUseful = true;
			}

			if (isUseful) {
				attrMappingsMap.put(armAttribute, gam);
			}
		}
	}

	public EGeneric_attribute_mapping getAttributeMapping(EExplicit_attribute attr) throws SdaiException {
		if(Implementation.mappingSupport) {
			return (EGeneric_attribute_mapping) attrMappingsMap.get(attr);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	protected void setSimpleAttributes(SdaiContext context) throws SdaiException {
		contextTmp = context;

		// get all simple attributes with mappings and set them
		EEntity_mapping em = entityMapping;
		AGeneric_attribute_mapping agam = new AGeneric_attribute_mapping();
		CGeneric_attribute_mapping.usedinParent_entity(null, em, contextTmp.mappingDomain, agam);
		fillAttributeMappingMap(agam);
        Map settersMap = new HashMap();
		Map attrSet = new HashMap();

		SdaiIterator it = agam.createIterator();
		while (it.next()) {
			EGeneric_attribute_mapping gam = agam.getCurrentMember(it);
			EAttribute armAttribute = (EAttribute) gam.getSource(null);
			String attrName = armAttribute.getName(null);

			Method attributeSetter = findAttributeSetter(getClass(), armAttribute);
			if (attributeSetter != null) {
				if (!settersMap.containsKey(attrName)) {
					settersMap.put(attrName, attributeSetter);
					attrSet.put(attrName, null);
				} else {
                    Method otherSetter = (Method) settersMap.get(attrName);
					Class[] paramTypes = otherSetter.getParameterTypes();
					Class usedType = paramTypes[1];
					paramTypes = attributeSetter.getParameterTypes();
					Class newType = paramTypes[1];
					// more specific type found
					if (newType.isAssignableFrom(usedType)) {
						settersMap.remove(attrName);
						settersMap.put(attrName, attributeSetter);
					}
				}
				continue;
			} else if (settersMap.containsKey(attrName)) {
				continue;
			}


			if (attrSet.containsKey(attrName)) {
				continue;
			}
			attrSet.put(attrName, null);

			gam = (EGeneric_attribute_mapping) attrMappingsMap.get(armAttribute);
			if (gam == null) {
				continue;
			}

			EEntity target = gam.getConstraints(null);
			// unset the old value
//			unsetMappedAttribute(contextTmp, gam);

			if ((armAttribute instanceof EExplicit_attribute) &&
			   (target instanceof EExplicit_attribute)) {
				EExplicit_attribute targetAttr = (EExplicit_attribute) target;
				setSimpleAttribute(targetAttr, armAttribute, contextTmp, getDefinedTypes(targetAttr));
				continue;
			}

/*			EEntity_definition attributeDef = mappingTarget;
			EEntity constraints = gam.getConstraints(null);

			boolean doCreate = true;
			Object obj = get_object(armAttribute);

			if (obj instanceof EEntity) {
				EMappedARMEntity armInstance = (EMappedARMEntity) obj;
//					EEntity_definition instanceDomain = armInstance.getMappingTarget(contextTmp);
				armInstance.createAimData(contextTmp);
				EEntity generatedInstance = armInstance.getAimInstance();
				createAttributeFromPath(aimInstance, (EAttribute) armAttribute, constraints, mappingTarget, generatedInstance, false, null);
				continue;
			} else if (obj instanceof AEntity) {
				AEntity ae = (AEntity) obj;
				SdaiIterator valueIt = ae.createIterator();

				while (valueIt.next()) {
					EMappedARMEntity value = (EMappedARMEntity) ae.getCurrentMemberEntity(valueIt);
					createAttributeFromPath(aimInstance, (EAttribute) armAttribute, constraints, mappingTarget, null, false, null);
				}
			} else { // simple type
				createAttributeFromPath(aimInstance, (EAttribute) armAttribute, constraints, mappingTarget, null, false, null);
			}*/
		}

		Set keySet = settersMap.keySet();
		Object[] keys = keySet.toArray();

		for (int i = 0; i < keys.length; i++) {
			String attrName = (String) keys[i];
			Method attributeSetter = (Method) settersMap.get(attrName);
			Object[] params = new Object[2];
			params[0] = contextTmp;
			params[1] = this;
			try {
				attributeSetter.invoke(null, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

//		if (em.testConstraints(null)) {
//		    EEntity entity_constraints = em.getConstraints(null);
//			makeConstraintStep(aimInstance, entity_constraints, null, false);
//		}
//
//		if (icmOwner) {
//			getICM().store();
//			setICM(null);
//		}
	}

	// Copy of this method is taken from Validation class
	protected static String listAttributes(AAttribute aat, String message) throws SdaiException {
		String result = new String();
		for(int i = 1; i <= aat.getMemberCount(); i++){
			EAttribute eat = (EAttribute)aat.getByIndex(i);
			result += " \t attribute \"" + eat.getName(null) + "\" " + message;
		}
		return result;
	}

///////////////
// General stuff which is common for all createAIMdata methods
///////////////
	protected EEntity_definition commonForAIMInstanceCreation(SdaiContext context) throws SdaiException{
		// find the ARM and AIM classes to map to
		resolveMapping(context);

		EEntity_definition aimDefinition = getMappingTarget(context);
		// 2) Take or create AIM instance
		if(aimInstance == null) {
		   AAttribute aat1 = new AAttribute();
			// 2a) If not all mandatory attributes are set - throw exception
			if(!validateRequiredExplicitAttributesAssigned(aat1))
			   throw new SdaiException(SdaiException.VA_NVLD, listAttributes(aat1, "value not set"));
		    // 2b) Create AIM instance
		    setAimInstance(context.working_model.createEntityInstance(aimDefinition));
		    // 2c) Link ARM and AIM instances
		}

		return aimDefinition;
	}

	// Unsetting all attributes of ARM instance - this is needed in order to avoid setting
	// AIM data corresponding those attributes next time
	protected void unsetAllAttributes() throws SdaiException {
		// We need to collect all attributes (also from supertypes) first
		EEntity_definition type = getInstanceType();
		ArrayList list = new ArrayList();
		LangUtils.findSupertypes(type, list);
		// Unset direct attributes
		AExplicit_attribute attributes = type.getExplicit_attributes(null);
		for(int i=1; i <= attributes.getMemberCount(); i++){
		   unsetAttributeValue(attributes.getByIndex(i));
		}
		// Unset attributes from all supertypes
		for(int j=0; j < list.size(); j++){
			EEntity_definition temp = (EEntity_definition)list.get(j);
			attributes = temp.getExplicit_attributes(null);
			for(int i=1; i <= attributes.getMemberCount(); i++){
				unsetAttributeValue(attributes.getByIndex(i));
			}
		}
	}

	void unsetAttributeRecursion(EEntity finalAimInstance, SdaiContext context, EEntity constraints) throws SdaiException {
		if (constraints instanceof EExplicit_attribute) {
			EExplicit_attribute ac = (EExplicit_attribute) constraints;
			EEntity domain = ac.getDomain(null);
			if (domain instanceof EEntity_definition) {
				if (finalAimInstance.testAttribute(ac, null) != 0) {
					EEntity obj = (EEntity) finalAimInstance.get_object(ac);
					getICM().deleteInstance(obj);
				}
			} else if (domain instanceof EAggregation_type) {
				// remove information from aggregate
			} else { // simple value
				finalAimInstance.unsetAttributeValue(ac);
			}
		} else if (constraints instanceof EPath_constraint) {
            EPath_constraint pc = (EPath_constraint) constraints;
			EEntity nextInstance = makeConstraintStep(finalAimInstance, pc.getElement1(null), null, false);
			unsetAttributeRecursion(nextInstance, context, pc.getElement2(null));
			AEntity users = new AEntity();
			nextInstance.findEntityInstanceUsers(context.domain, users);
			if ((users.getMemberCount() == 0) ||
				((users.getMemberCount() == 1) && (users.isMember(finalAimInstance)))) {
				getICM().deleteInstance(nextInstance);
			}
		} else {
			System.out.println("Constraint not supported in unset method. " + constraints);
		}
	}

    private void unsetMappedAttribute(SdaiContext context, EGeneric_attribute_mapping attribute) throws SdaiException {
		ObjectMapping objectMapping = Mapping.getEntityMapping(owning_model.repository.session,
															   context.domain, context.mappingDomain);
		Object mappedInstance = 
			objectMapping.getMappedAttribute(aimInstance, attribute, EEntity.NO_RESTRICTIONS, false);
		if (mappedInstance != null) {
			unsetAttributeRecursion(aimInstance, context, attribute.getConstraints(null));
		}
	}

	protected void setSimpleAttributes(EEntity_definition exactAIMType, EEntity armInstance, SdaiContext context, EAttribute[] attributePairs) throws SdaiException {
		setSimpleAttributes(exactAIMType, null, armInstance, context, attributePairs);
	}

	// This method sets AIM attributes form ARM, when mapping is simple
	// and contains no path (1:1 mapping).
	// String[] attributePairs should contain even number of elements, like this:
	// AIM attribute No.1, ARM attribute No.1, AIM attribute No.2, ARM attribute No.2 ...
	// String exactAIMType is needed in order to avoid name conflicts
	// It is likely we would need to do the same on ARM later
	protected void setSimpleAttributes(EEntity_definition exactAIMType, EEntity attributesInstance, EEntity armInstance, SdaiContext context, EAttribute[] attributePairs) throws SdaiException {
		LangUtils.setSimpleAttributes(this, exactAIMType, attributesInstance, (EMappedARMEntity) armInstance, context, attributePairs);
	}

	protected void setSimpleAttribute(EAttribute aimAttribute, EAttribute armAttribute, SdaiContext context) throws SdaiException {
		LangUtils.setSimpleAttribute(this, aimInstance, aimAttribute, armAttribute, context);
	}

	protected void setSimpleAttribute(EAttribute aimAttribute, EAttribute armAttribute, SdaiContext context, EDefined_type[] types) throws SdaiException {
		LangUtils.setSimpleAttribute(this, aimInstance, aimAttribute, armAttribute, context, types);
	}

	protected void setSimpleAttribute(EEntity attributesInstance, EAttribute aimAttribute, EAttribute armAttribute, SdaiContext context) throws SdaiException {
		setSimpleAttribute(attributesInstance, aimAttribute, armAttribute, context, null);
	}

	protected void setSimpleAttribute(EEntity attributesInstance, EAttribute aimAttribute, EAttribute armAttribute, SdaiContext context, EDefined_type[] types) throws SdaiException {
		LangUtils.setSimpleAttribute(this, attributesInstance, aimAttribute, armAttribute, context, types);
	}

	protected int findARMEntityMappings(SdaiContext context, AEntity_mapping mappings, EEntity_definition edef, int mode) throws SdaiException {
		CEntity_mapping.usedinSource(null, edef, context.mappingDomain, mappings);
		return mappings.getMemberCount();
	}

	public int findARMEntityMappings(SdaiContext context, AEntity_mapping mappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			EEntity_definition armEntity = getInstanceType();
			return findARMEntityMappings(context, mappings, armEntity, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/**
		Notifies all listeners in the aggregate <code>listenrList</code> that
		this entity instance was modified.
		Also, sets the field 'modified' of the owning model with the value 'true'.
	*/
	void modified() throws SdaiException {
		if(!owning_model.deleting) {
			if(attributeState == ATTRIBUTES_UNKNOWN) {
				throw new SdaiException(SdaiException.SY_ERR, "Attribute values are unknown");
			}

			attributeState = ATTRIBUTES_MODIFIED;
		}
		super.modified();
	}

	/**
	 * Describe <code>deletedObject</code> method here.
	 *
	 * @exception SdaiException if an error occurs
	 */
	void deletedObject() throws SdaiException {
		setAimInstance(null);
		super.deletedObject();
	}
	
	/**
	 * Returns all attributes of the entity including supertype attributes.
	 * This method is intended for internal JSDAI use.
	 *
	 * @param edef The entity type definition
	 * @return <code>AAttribute</code> containing all attribute definitions
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public static AAttribute getAllAttributes(EEntity_definition edef) throws SdaiException {
		AAttribute attrs = new AAttribute();
		getAllAttributesRecursion(edef, attrs);
		return attrs;
	}

	protected static void getAllAttributesRecursion(EEntity_definition edef, AAttribute attrs) throws SdaiException {
		AAttribute moreAttrs = edef.getAttributes(null, null);
		int i;
		for (i = 0; i < moreAttrs.getMemberCount(); i++) {
			attrs.addUnordered(moreAttrs.getByIndex(i + 1));
		}

		AEntity_definition supertypes = edef.getSupertypes(null);
		for (i = 0; i < supertypes.getMemberCount(); i++) {
			getAllAttributesRecursion(supertypes.getByIndex(i + 1), attrs);
		}
	}

// 	public String toString() {
// 		return super.toString() + "[" + attributeStateStrings[attributeState] + "];";
// 	}
	
	protected class InstanceCreationManager {
		LinkedList instancesList = null;
		LinkedList mainInstances = null;
		SdaiModel tmpModel = null;
		ASdaiModel tmpModelA = null;
		int instancesCount = 0;
		EMappedARMEntity armOwner = null;
		Map instanceIndexMap = null;

		private InstanceCreationManager() throws Exception {
			throw new Exception("Empty InstanceCreationManager constructor shouldn't be used.");
		}

		public InstanceCreationManager(EMappedARMEntity _armOwner) {
			armOwner = _armOwner;
		}

		protected SdaiModel getTmpModel() throws SdaiException {
			if (tmpModel == null) {
				SdaiModel instanceModel = findEntityInstanceSdaiModel();
				SdaiRepository repository = instanceModel.getRepository();

				tmpModel = repository.findSdaiModel("temporaryAIMmodel");
				if (tmpModel != null) {
					tmpModel.deleteSdaiModel();
				}
				tmpModel = repository.createSdaiModel("temporaryAIMmodel", contextTmp.working_model.getUnderlyingSchema());
				tmpModel.startReadWriteAccess();

				tmpModelA = new ASdaiModel();
				tmpModelA.addByIndex(1, tmpModel, null);
			}

			return tmpModel;
		}

		protected void clear() throws SdaiException {
			instanceIndexMap = new HashMap();
			instancesList = new LinkedList();
			mainInstances = new LinkedList();
			instancesCount = 0;

			if (tmpModel != null) {
				tmpModel.deleteSdaiModel();
				tmpModel = null;
				tmpModelA = null;
			}
		}

		boolean testInstancesEquality(EEntity entity1, EEntity entity2) throws SdaiException {
			EEntity_definition edef = entity1.getInstanceType();
			if ((entity2.isInstanceOf(edef)) && (entity2 != entity1)) {
//				System.out.print(entity1 + " ? " + entity2);
				// check the attributes
				boolean equals = true;
				AAttribute attrs = getAllAttributes(edef);
				SdaiIterator it = attrs.createIterator();

				while (it.next()) {
					EAttribute attr = attrs.getCurrentMember(it);
					if (attr instanceof EExplicit_attribute) {
						CExplicit_attribute expl_attr = (CExplicit_attribute) attr;
						if (((CEntityDefinition)edef).checkIfDerived(expl_attr)) {
							continue;
						}

						int entity2AttrTestRes = entity2.testAttribute(attr, null);
						int entity1AttrTestRes = entity1.testAttribute(attr, null);
						if (entity2AttrTestRes != 0) {
							Object entity2AttrValue = entity2.get_object(attr);
							if (entity1AttrTestRes == 0) {
								equals = false;
								break;
							}
							Object entity1AttrValue = entity1.get_object(attr);

							if (entity1AttrValue instanceof AEntity) {
								if (!testAggregatesEquality((AEntity) entity1AttrValue, (AEntity) entity2AttrValue)) {
									equals = false;
									break;
								}
							} else if (!entity1AttrValue.equals(entity2AttrValue)) {
								equals = false;
								break;
							}
						} else if (entity1AttrTestRes != 0) {
							equals = false;
							break;
						}
					}
				}

//				System.out.println(": " + equals);
				return equals;
			}

			return false;
		}

		boolean testAggregatesEquality(AEntity entity1, AEntity entity2) throws SdaiException {
			if (entity1.getMemberCount() != entity2.getMemberCount()) {
				return false;
			}

			for (int i = 0; i < entity1.getMemberCount(); i++) {
				EEntity subEntity1 = entity1.getByIndexEntity(i + 1);

				if (subEntity1 instanceof AEntity) {
					// find matching subaggregate
					boolean found = false;
					for (int j = 0; j < entity2.getMemberCount(); j++) {
						EEntity subEntity2 = entity2.getByIndexEntity(j + 1);
						if (subEntity2 instanceof AEntity) {
							found = testAggregatesEquality((AEntity) subEntity1, (AEntity) subEntity2);
							if (found) {
								break;
							}
						}
					}

					if (!found) {
						return false;
					}
				} else {
					if (!entity2.isMember(subEntity1)) {
						return false;
					}
				}
			}

			return true;
		}

		protected EEntity findLocally(EEntity instance) throws SdaiException {
			for (int i = 0; i < instancesCount; i++) {
				EEntity obj = (EEntity) instancesList.get(i);
				if (testInstancesEquality(instance, obj)) {
					return obj;
				}
			}

			return null;
		}

		protected EEntity findGlobally(EEntity instance) throws SdaiException {
			EEntity rv = findLocally(instance);
			if (rv == null) {
				EEntity_definition edef = instance.getInstanceType();
				AEntity instances = contextTmp.working_model.getInstances(edef);

				SdaiIterator it = instances.createIterator();
				while (it.next()) {
					EEntity persistentInstance = instances.getCurrentMemberEntity(it);
					if (testInstancesEquality(instance, persistentInstance)) {
						rv = persistentInstance;
						break;
					}
				}
			}

			return rv;
		}

		protected void deleteInstance(EEntity instance) throws SdaiException {
			int index = instancesList.indexOf(instance);
			if (index >= 0) {
				instancesList.remove(index);
				instancesCount--;
			}
			instance.deleteApplicationInstance();
		}

		protected EEntity createEntityInstance(EEntity_definition edef) throws SdaiException {
			EEntity newInstance = null;
			try {
/*				Class entityClass = ((CEntityDefinition)edef).getEntityClass();
				newInstance = (EEntity) entityClass.newInstance();*/
				if (edef.getInstantiable(null)) {
					newInstance = getTmpModel().createEntityInstance(edef);
					instancesList.add(newInstance);
					instancesCount++;
				} else {
					System.out.println("Couldn't create AIM instance (abstract): " + edef);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (newInstance == null) {
				System.out.println("newInstance = null.");
			}
			return newInstance;
		}

		protected Aggregate createAggregate(EEntity parentInstance, EAttribute attribute, EDefined_type select[]) {
			Aggregate newAggregate = null;
			try {
				newAggregate = parentInstance.createAggregate(attribute, select);
			} catch (Exception e) {
			}

			return newAggregate;
		}

		protected void storeAggregate(EEntity persistentObject, AEntity value, EAttribute attr,
									  LinkedList instancesList, LinkedList persistentInstances) throws SdaiException {
			Aggregate aggr = persistentObject.createAggregate(attr, null);

			for (int i = 0; i < value.getMemberCount(); i++) {
				EEntity entity = value.getByIndexEntity(i + 1);
				if (instancesList.contains(entity)) {
					int index = ((Integer)instanceIndexMap.get(entity)).intValue();
					aggr.addUnordered(persistentInstances.get(index), null);
				} else {
					aggr.addUnordered(entity,  null);
				}
			}
		}

		protected void replaceEntity(EEntity from, EEntity to, LinkedList instances, LinkedList persistentInstances) throws SdaiException {
			int index = instances.indexOf(from);

			if (index == -1) {
				return;
			}

			instances.remove(index);
			persistentInstances.remove(index);
			instancesCount--;

			AAttribute roles = new AAttribute();
			from.findInstanceRoles(tmpModelA, roles);

			SdaiIterator attrIt = roles.createIterator();
			while (attrIt.next()) {
				EAttribute attr = roles.getCurrentMember(attrIt);
				AEntity usedIns = new AEntity();
				from.findEntityInstanceUsedin(attr, tmpModelA, usedIns);

				SdaiIterator usedInIt = usedIns.createIterator();
				while (usedInIt.next()) {
					EEntity usedInEntity = usedIns.getCurrentMemberEntity(usedInIt);

					Object value = usedInEntity.get_object(attr);
					if (value instanceof Aggregate) {
						Aggregate aggr = (Aggregate) value;
						SdaiIterator it = aggr.createIterator();
						while (it.next()) {
							Object subValue = aggr.getCurrentMemberObject(it);
							if (subValue.equals(from)) {
								aggr.setCurrentMember(it, to, null);
							}
						}
					} else {
						usedInEntity.set(attr, to, null);
					}
					EEntity mirrorObj = findReuse(usedInEntity);

					if (mirrorObj != null) {
						replaceEntity(usedInEntity, mirrorObj, instances, persistentInstances);
					}
				}
			}
		}

		protected EEntity findReuse(EEntity obj) throws SdaiException {
			EEntity_definition edef = obj.getInstanceType();
			int reuseFlag = ARM2AIMMappingManager.getAimEntityReuseFlag(armOwner, edef);

			EEntity mirrorObj = null;
			if (reuseFlag == ARM2AIMMappingManager.REUSE_GLOBALLY) {
				mirrorObj = findGlobally(obj);
			} else if (reuseFlag == ARM2AIMMappingManager.REUSE_LOCALLY) {
				mirrorObj = findLocally(obj);
			}

			return mirrorObj;
		}

		protected void replaceAttributes(EEntity instance, EEntity persistentInstance, LinkedList instances, LinkedList persistentInstances) throws SdaiException {
			// go through all attributes and inverses
			// if they refer to the instances that aren't persistent
			// make them point to a newly created ones
			AAttribute attrs = getAllAttributes(instance.getInstanceType());
			SdaiIterator it = attrs.createIterator();

			while (it.next()) {
				EAttribute attr = attrs.getCurrentMember(it);

				if (attr instanceof EExplicit_attribute) {
					if (instance.testAttribute(attr, null) != 0) {
						Object value = instance.get_object(attr);

						if (value != null) {
							if (instanceIndexMap.containsKey(value)) {
								int index = ((Integer)instanceIndexMap.get(value)).intValue();
								persistentInstance.set(attr, persistentInstances.get(index), null);
							} else if (persistentInstance.testAttribute(attr, null) == 0) {
								if (value instanceof AEntity) { // aggregate
									storeAggregate(persistentInstance, (AEntity) value, attr, instances, persistentInstances);
								} else {
									persistentInstance.set(attr, value, null);
								}
							}
						}
					}
				}
			}
		}

		protected void store() {
			try {
				LinkedList persistentInstances = new LinkedList();

				int i;
				for (i = 0; i < instancesList.size(); i++) {
					persistentInstances.add(null);
				}

				for (i = 0; i < instancesList.size(); i++) {
					EEntity obj = (EEntity) instancesList.get(i);
					EEntity_definition edef = obj.getInstanceType();

					EEntity mirrorObj = findReuse(obj);
					if (mirrorObj != null) {
						replaceEntity(obj, mirrorObj, instancesList, persistentInstances);
					}
				}

				for (i = 0; i < instancesList.size(); i++) {
					instanceIndexMap.put(instancesList.get(i), new Integer(i));
				}

				LinkedList newEntitiesIndexes = new LinkedList();
				for (i = 0; i < instancesList.size(); i++) {
					EEntity obj = (EEntity) instancesList.get(i);
					EEntity persistentObj = (EEntity) persistentInstances.get(i);

					if (persistentObj == null) {
						persistentObj = contextTmp.working_model.createEntityInstance(obj.getInstanceType());
						persistentInstances.set(i, persistentObj);
						newEntitiesIndexes.add(new Integer(i));
					}
				}

				for (i = 0; i < newEntitiesIndexes.size(); i++) {
					int entityIndex = ((Integer)newEntitiesIndexes.get(i)).intValue();
					EEntity obj = (EEntity) instancesList.get(entityIndex);
					EEntity persistentObj = (EEntity) persistentInstances.get(entityIndex);
					replaceAttributes(obj, persistentObj, instancesList, persistentInstances);
				}
				for (i = 0; i < mainInstances.size(); i++) {
					EEntity entity = (EEntity) mainInstances.get(i);
					replaceAttributes(entity, entity,  instancesList, persistentInstances);
				}

				clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
