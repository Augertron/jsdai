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

package jsdai.libutil;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jsdai.dictionary.AEntity_or_view_definition;
import jsdai.dictionary.ANamed_type;
import jsdai.dictionary.CDefined_type;
import jsdai.dictionary.EAggregation_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EBag_type;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EEnumeration_type;
import jsdai.dictionary.EExplicit_attribute;
import jsdai.dictionary.EInteger_type;
import jsdai.dictionary.ENamed_type;
import jsdai.dictionary.EReal_type;
import jsdai.dictionary.ESelect_type;
import jsdai.dictionary.ESet_type;
import jsdai.dictionary.ESimple_type;
import jsdai.dictionary.EString_type;
import jsdai.lang.AEntity;
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
import jsdai.lang.JsdaiLangAccessor;
import jsdai.lang.MappingContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiSession;
import jsdai.lang.SelectType;
import jsdai.mapping.AGeneric_attribute_mapping;
import jsdai.mapping.CGeneric_attribute_mapping;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.EGeneric_attribute_mapping;
import jsdai.util.LangUtils;

/**
 * This class provides functionality similar to <code>CMappedARMEntity</code>
 * for XIM mapped entity instance creation. It contains static methods that
 * are to be called within <code>MappingPopulationCreator</code>.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @see jsdai.lang.CMappedARMEntity
 * @see jsdai.lang.MappingPopulationCreator
 */
public final class CMappedXIMEntity extends JsdaiLangAccessor {

	private static final int BUILD_FLAG_FIND_NOT_NULL = 0;
	private static final int BUILD_FLAG_FIND_NULL = 1;

	private static final Object CPLX_TYPE_TEMP = new Object();

	/**
	 * This is a class with static only methods
	 */
	private CMappedXIMEntity() { }

	/**
	 * @deprecated Use {@link #buildMappedInstance(MappingContext, EEntity,
	 *             EEntity_mapping, Collection)} instead.
	 */
	public static EEntity buildMappedInstance(MappingContext mappingContext, EEntity mimInstance,
			EEntity_mapping entityMapping) throws SdaiException {
		return buildMappedInstance(mappingContext, mimInstance, entityMapping, null);
	}

	/**
	 * Creates or returns already created mapped entity 
	 * instance using mapping context's mapped instance association.
	 * Mapped instance is created by substituting provided <code>mimInstance</code>
	 * to mapped entity type.
	 *
	 * @param mappingContext the mapping context for the operation
	 * @param mimInstance target MIM instance to build mapped XIM instance for
	 * @param entityMapping XIM entity mapping
	 * @param deferredInstances output collection to which deferred instances are added
	 * @return mapped XIM entity instance
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public static EEntity buildMappedInstance(MappingContext mappingContext, EEntity mimInstance,
			EEntity_mapping entityMapping, Collection deferredInstances) throws SdaiException {
		EEntity_definition ximType = entityMapping.getSource(null);
		EEntity ximMappedInstance = mappingContext.getMappedInstance(mimInstance);
		if(ximMappedInstance == null && !ximType.isSubtypeOf(mimInstance.getInstanceType())) {
			ximMappedInstance = mimInstance;
		}
		if(ximMappedInstance != null) {
			if(ximMappedInstance.isInstanceOf(ximType) || ximMappedInstance.isKindOf(ximType)) {
				return ximMappedInstance;
			} else {
				// Attempts to find a complex entity
				Collection complexNames = new TreeSet();
				Collection complexTypes;
				Object[] cplxTypeTemp = (Object[]) mimInstance.getTemp(CPLX_TYPE_TEMP);
				if(cplxTypeTemp != null) {
					complexTypes = (Collection) cplxTypeTemp[0];
					for (Iterator i = complexTypes.iterator(); i.hasNext();) {
						EEntity_definition complexLeaf = (EEntity_definition) i.next();
						complexNames.add(complexLeaf.getName(null));
					}
				} else {
					complexTypes = new HashSet();
					EEntity_definition oldXimType = ximMappedInstance.getInstanceType();
					if(oldXimType.getComplex(null)) {
						AEntity_or_view_definition complexLeaves = oldXimType.getGeneric_supertypes(null);
						for(SdaiIterator i = complexLeaves.createIterator(); i.next(); ) {
							EEntity_definition complexLeaf =
								(EEntity_definition) complexLeaves.getCurrentMember(i);
							complexNames.add(complexLeaf.getName(null));
							complexTypes.add(complexLeaf);
						}
					} else {
						complexNames.add(oldXimType.getName(null));
						complexTypes.add(oldXimType);
					}
				}
				if(ximType.getComplex(null)) {
					AEntity_or_view_definition complexLeaves = ximType.getGeneric_supertypes(null);
					COMPLEX_LEAVES: for(SdaiIterator i = complexLeaves.createIterator(); i.next(); ) {
						EEntity_definition complexLeaf =
							(EEntity_definition) complexLeaves.getCurrentMember(i);
						for (Iterator j = complexTypes.iterator(); j.hasNext();) {
							EEntity_definition othXimType = (EEntity_definition) j.next();
							if(othXimType.isSubtypeOf(complexLeaf)) {
								continue COMPLEX_LEAVES;
							} else if(complexLeaf.isSubtypeOf(othXimType)) {
								complexNames.remove(othXimType.getName(null));
								j.remove();
							}
						}
						complexNames.add(complexLeaf.getName(null));
						complexTypes.add(complexLeaf);
					}
				} else {
					boolean addNewType = true;
					for (Iterator i = complexTypes.iterator(); i.hasNext();) {
						EEntity_definition othXimType = (EEntity_definition) i.next();
						if(othXimType.isSubtypeOf(ximType)) {
							addNewType = false;
							break;
						} else if(ximType.isSubtypeOf(othXimType)) {
							complexNames.remove(othXimType.getName(null));
							i.remove();
						}
					}
					if(addNewType) {
						complexNames.add(ximType.getName(null));
						complexTypes.add(ximType);
					}
				}
				try {
					ximMappedInstance =
						substituteToComplexType(mappingContext, ximMappedInstance,
								complexNames, cplxTypeTemp);
					unsetMappedAttributes(mappingContext, ximMappedInstance, entityMapping);
					mimInstance.setTemp(CPLX_TYPE_TEMP, null);
				} catch (SdaiException e) {
					if(e.getErrorId() == SdaiException.ED_NDEF) {
						Object[] newCplxTypeTemp;
						if(cplxTypeTemp != null) {
							newCplxTypeTemp = new Object[cplxTypeTemp.length + 1];
							System.arraycopy(cplxTypeTemp, 0, newCplxTypeTemp, 0, cplxTypeTemp.length);
							newCplxTypeTemp[newCplxTypeTemp.length - 1] = entityMapping;
						} else {
							newCplxTypeTemp = new Object[] { complexTypes, entityMapping};
						}
						mimInstance.setTemp(CPLX_TYPE_TEMP, newCplxTypeTemp);
						if(deferredInstances != null) {
							deferredInstances.add(mimInstance);
						}
					} else {
						throw e;
					}
				}
				mappingContext.putMappedInstace(mimInstance, ximMappedInstance);
				return ximMappedInstance;
			}
		} else if(mimInstance.isInstanceOf(ximType) || mimInstance.isKindOf(ximType)) {
			mappingContext.putMappedInstace(mimInstance, mimInstance);
			return mimInstance;
		} else {
			SdaiModel workingModel = mappingContext.getContext().working_model;
			if(workingModel == null) {
				workingModel = mimInstance.findEntityInstanceSdaiModel();
			}
			ximMappedInstance = workingModel.substituteInstance(mimInstance, ximType);
			mappingContext.putMappedInstace(mimInstance, ximMappedInstance);
			unsetMappedAttributes(mappingContext, ximMappedInstance, entityMapping);
			return ximMappedInstance;
		}
	}

	public static void buildDeferredMappedInstances(MappingContext mappingContext,
			Collection deferredInstances) throws SdaiException {
		boolean logAssigned = false;
		PrintWriter log = null;//SdaiSession.getLogWriter();
		for (Iterator i = deferredInstances.iterator(); i.hasNext();) {
			EEntity mimInstance = (EEntity) i.next();
			Object[] cplxTypeTemp = (Object[]) mimInstance.getTemp(CPLX_TYPE_TEMP);
			if(cplxTypeTemp != null) {
				EEntity currInstance = mappingContext.getMappedInstance(mimInstance);
				if(currInstance == null) {
					currInstance = mimInstance;
				}
				if(!logAssigned) {
					// Workaround SdaiSession.getLogWriterSession() not forwarding to global writer
					log = currInstance.findEntityInstanceSdaiModel().getRepository().getSession().getLogWriterSession();
					if(log == null) {
						log = SdaiSession.getLogWriter();
					}
					logAssigned = true;
				}
				EEntity_definition mimType = currInstance.getInstanceType();
				Collection mimTypes = new HashSet();
				if(mimType.getComplex(null)) {
					AEntity_or_view_definition complexLeaves = mimType.getGeneric_supertypes(null);
					for(SdaiIterator j = complexLeaves.createIterator(); j.next(); ) {
						EEntity_definition complexLeaf =
							(EEntity_definition) complexLeaves.getCurrentMember(j);
						mimTypes.add(complexLeaf);
					}
				} else {
					mimTypes.add(mimType);
				}
				Collection complexTypes = (Collection) cplxTypeTemp[0];
				Collection complexNames = new TreeSet();
				for(Iterator j = complexTypes.iterator(); j.hasNext(); ) {
					EEntity_definition ximType = (EEntity_definition) j.next();
					complexNames.add(ximType.getName(null));
				}

				try {
					EEntity ximMappedInstance =
						substituteToComplexType(mappingContext, currInstance,
								complexNames, cplxTypeTemp);
					mappingContext.putMappedInstace(mimInstance, ximMappedInstance);
					if(log != null) {
						log.print("buildMappedInstance downgraded ");
						printComplexTypes(log, complexNames);
						log.println(" to " + ximMappedInstance);
					}
				} catch (SdaiException e) {
					int errorId = e.getErrorId();
					if(errorId == SdaiException.ED_NDEF || errorId == SdaiException.ED_NVLD) {
						if(log != null) {
							log.print("buildMappedInstance failed for " +
									currInstance.getPersistentLabel() + "=" + currInstance.getClass().getName() +
							" as ");
							printComplexTypes(log, complexNames);
							log.println();
						}
					} else {
						throw e;
					}
				}
			}
		}
	}

	/**
	 * Assigns (sets) value of mapped attribute.
	 *
	 * @param ximInstance mapped XIM instance to set attribute value for
	 * @param mappingContext the mapping context for the operation
	 * @param attribute The attribute to assign value to
	 * @param attrValue The attribute value. This can be one of:<ul>
	 *                  <li>object corresponding simple type;</li>
	 *                  <li><code>EEntity</code> for entity instances;</li>
	 *                  <li>either <code>AEntity</code> or {@link java.util.Collection}
	 *                  for aggregates</li></ul>
	 *                  <code>EEntity</code> instances have to be target (MIM) instances
	 * @param genAttMapping The <code>EGeneric_attribute_mapping</code> which provides
	 *                      type information for the value
	 * @return true is value was assigned to the attribute and false otherwise.
	 * @exception SdaiException if an error occurs during attribute assignment
	 *                          from AIM data or in underlying JSDAI operations
	 */
	public static boolean assignMappedValue(EEntity ximInstance, MappingContext mappingContext, EExplicit_attribute attribute,
											Object attrValue, EGeneric_attribute_mapping genAttMapping) throws SdaiException {
		return assignMappedValue(ximInstance, mappingContext, attribute, attribute.getDomain(null), attrValue,
								null, -1, genAttMapping, null);
	}

	/**
	 * Assigns (sets) value of mapped attribute.
	 *
	 * @param ximInstance mapped XIM instance to set attribute value for
	 * @param mappingContext the mapping context for the operation
	 * @param attribute The attribute to assign value to
	 * @param attrValue The attribute value. This can be one of:<ul>
	 *                  <li>object corresponding simple type;</li>
	 *                  <li><code>EEntity</code> for entity instances;</li>
	 *                  <li>either <code>AEntity</code> or {@link java.util.Collection}
	 *                  for aggregates</li></ul>
	 *                  <code>EEntity</code> instances have to be target (AIM) instances
	 * @param dataType The data type aggregate which provides select path information.
	 *                 This parameter has to be not null only when the attribute domain is select
	 *                 and attribute value is of simple type of aggregate of simple types
	 * @return true is value was assigned to the attribute and false otherwise
	 * @exception SdaiException if an error occurs during attribute assignment
	 *                          from AIM data or in underlying JSDAI operations
	 */
	public static boolean assignMappedValue(EEntity ximInstance, MappingContext mappingContext, EExplicit_attribute attribute,
											Object attrValue, ANamed_type dataType) throws SdaiException {
		return assignMappedValue(ximInstance, mappingContext, attribute, attribute.getDomain(null), attrValue,
								 null, -1, null, dataType);
	}

	private static boolean assignMappedValue(EEntity ximInstance, MappingContext mappingContext, Object attrOrAggr,
											 EEntity domain, Object attrValue, EDefined_type selectTypes[],
											 int selectPos, EGeneric_attribute_mapping genAttMapping,
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
				assignAttrOrAggr(ximInstance, attrOrAggr, attrValue, selectTypes);
				return true;
			} catch(SdaiException e) {
				throw (SdaiException)
					new SdaiException(e.getErrorId(), e, "Exception for instance: " + ximInstance +
									  "\nattribute: " + attrOrAggr + " value: " + attrValue +
									  " value class: " + attrValue.getClass() +
									  " selectTypes: " + (selectTypes != null ? selectTypes[0] : null) +
									  " mapping: " + genAttMapping + "\n").initCause(e);
			}
		} else if(domain instanceof EAggregation_type) {
			Aggregate aggregate = createAttrOrAggr(ximInstance, attrOrAggr, selectTypes, attrValue == null);
			if(aggregate != null) {
				EAggregation_type aggregationType = (EAggregation_type)domain;
				assignMappedValue(ximInstance, mappingContext, aggregate, aggregationType.getElement_type(null), 
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
					   && assignMappedValue(ximInstance, mappingContext, attrOrAggr, selection, attrValue,
											selectTypes, 0, genAttMapping, dataType)) {
						return true;
					}
				}
				return false;
			} else if(selectTypes == null || selectTypes[selectPos] == domain) {
				return assignMappedValue(ximInstance, mappingContext, attrOrAggr, newDomain, attrValue,
										 selectTypes, selectPos + 1, genAttMapping, dataType);
			} else {
				return false;
			}
		} else if(domain instanceof EEntity_definition) {
			if(attrValue instanceof EEntity) {
				EEntity attrInstance =
					findMappedInstance(mappingContext, (EEntity)attrValue,
									   (EEntity_definition)domain, (genAttMapping != null) ?
									   BUILD_FLAG_FIND_NULL : BUILD_FLAG_FIND_NOT_NULL);
				if(attrInstance != null) {
					try {
						assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
					} catch(SdaiException e) {
						throw (SdaiException)
							new SdaiException(e.getErrorId(), e, "Exception for instance: " + ximInstance +
											  "\nattribute: " + attrOrAggr + " value: " + attrValue +
											  " value class: " + attrValue.getClass() +
											  " attrInstance: " + attrInstance + " mapping: " +
											  genAttMapping + "\n").initCause(e);
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
						findMappedInstance(mappingContext, aggrInst,
										   domainDefinition, (genAttMapping != null) ?
										   BUILD_FLAG_FIND_NULL : BUILD_FLAG_FIND_NOT_NULL);
					if(attrInstance != null) {
						assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
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
							findMappedInstance(mappingContext, (EEntity)value, domainDefinition,
											   (genAttMapping != null) ?
											   BUILD_FLAG_FIND_NULL : BUILD_FLAG_FIND_NOT_NULL);
						if(attrInstance != null) {
							assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
						}
					}
				}
				return true;
			}
		} else if(domain instanceof EEnumeration_type) {
			if(attrValue instanceof String) {
				Object origValue = attrValue;
				attrValue =
					new Integer(LangUtils.convertEnumerationStringToInt(mappingContext.getContext(),
							(String)attrValue, (EEnumeration_type)domain));
				try {
					assignAttrOrAggr(ximInstance, attrOrAggr, attrValue, selectTypes);
				} catch(SdaiException e) {
					throw (SdaiException)
						new SdaiException(e.getErrorId(), e, "Exception for instance: " + ximInstance +
										  "\nattribute: " + attrOrAggr + "origValue: " + origValue +
										  " value: " + attrValue + " domain: " + domain +
										  " selectTypes: " + selectTypes + " mapping: " +
										  genAttMapping + "\n").initCause(e);
				}
				return true;
			} else {
				//FIXME: implement enumeration support
				throw new SdaiException(SdaiException.FN_NAVL, 
										"No support for EEnumeration_type attributes yet: " + 
										ximInstance + "\n" + genAttMapping + "\n" + domain + "\n" + attrValue);
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
				   && ((domainEntity = attrMapping.getDomain(null)) instanceof EEntity_mapping
					   || domainEntity instanceof EEntity_definition)) {
					EEntity_definition domainSourceDef;
					if(domainEntity instanceof EEntity_mapping) {
						EEntity_mapping domainMapping = (EEntity_mapping)domainEntity;
						domainSourceDef = domainMapping.getSource(null);
					} else {
						domainSourceDef = (EEntity_definition)domainEntity;
					}
					if(attrValue instanceof EEntity) {
						EEntity attrInstance = findMappedInstance(mappingContext, (EEntity)attrValue,
																  domainSourceDef, BUILD_FLAG_FIND_NULL);
						if(attrInstance != null) {
							assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
							return true;
						}
					} else if(attrValue instanceof AEntity) {
						AEntity aggregate = (AEntity)attrValue;
						SdaiIterator aggregateIter = aggregate.createIterator();
						boolean aggResult = false;
						while(aggregateIter.next()) {
							EEntity aggrInst = aggregate.getCurrentMemberEntity(aggregateIter);
							EEntity attrInstance = findMappedInstance(mappingContext, aggrInst,
																	  domainSourceDef, BUILD_FLAG_FIND_NULL);
							if(attrInstance != null) {
								assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
								aggResult = true;
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
								EEntity attrInstance = findMappedInstance(mappingContext, aggrInst,
																		  domainSourceDef, BUILD_FLAG_FIND_NULL);
								if(attrInstance != null) {
									assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
									aggResult = true;
								}
							}
						}
						return aggResult;
					}
				} else {
					ESelect_type selectDomain = (ESelect_type)domain;
					if(attrValue instanceof EEntity) {
						if(!assignGeneralEntitySelect(ximInstance, mappingContext, attrOrAggr,
													  selectDomain, (EEntity)attrValue)) {
							//FIXME: implement the case when entity domain as entity_mapping isn't given
							throw new SdaiException(SdaiException.FN_NAVL, 
													"No suitable value found for attribute of select type.\n" + 
													"Instance " + ximInstance +
													"\nAttribute or aggregate: " + attrOrAggr +
													" (select is " + selectDomain.getPersistentLabel() + ").\n" +
													"Reason: attribute value " + attrValue +
													" is not of expected type" + 
													(dataType != null ? ": " + 
															(dataType.getMemberCount() == 1 ?
																	dataType.getByIndexEntity(1).toString() 
																	: dataType.toString()): ""));
						}
						return true;
					} else if(attrValue instanceof AEntity) {
						AEntity aggregate = (AEntity)attrValue;
						SdaiIterator aggregateIter = aggregate.createIterator();
						boolean aggResult = false;
						while(aggregateIter.next()) {
							EEntity aggrInst = aggregate.getCurrentMemberEntity(aggregateIter);
							aggResult |= assignGeneralEntitySelect(ximInstance, mappingContext, attrOrAggr,
																   selectDomain, aggrInst);
						}
						return aggResult;
					} else {
						Iterator collectionIter = ((Collection)attrValue).iterator();
						boolean aggResult = false;
						while(collectionIter.hasNext()) {
							Object value = collectionIter.next();
							if(value instanceof EEntity) {
								//FIXME: We don't support mixed aggregates (instances+simple values)
								aggResult |= assignGeneralEntitySelect(ximInstance, mappingContext, attrOrAggr,
																	   selectDomain, (EEntity)value);
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
					int domainTypeCount = getSelectTypeCount(domainType);
					CDefined_type[][] domainTypePaths = getSelectTypePaths(domainType);
					for(int i = 0; i < domainTypeCount; i++) {
						if(domainTypePaths[i].length == 1) {
							EDefined_type selectOnePath = domainTypePaths[i][0];
							EEntity selectDomain = selectOnePath.getDomain(null);
							if((attrValue instanceof String
								&& selectDomain instanceof EString_type)
							   || (attrValue instanceof Integer
								   && selectDomain instanceof EInteger_type)
							   || (attrValue instanceof Double
								   && selectDomain instanceof EReal_type)) {
								select = domainTypePaths[i];
								break;
							}
						}
					}
					if(select != null) {
						assignAttrOrAggr(ximInstance, attrOrAggr, attrValue, select);
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
												"No suitable select path found: " + ximInstance + "\n" +
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
						   && assignMappedValue(ximInstance, mappingContext, attrOrAggr, selection, attrValue,
												select, 0, genAttMapping, dataType)) {
							return true;
						}
					}
					return false;
					//FIXME: implement selects of defined type when mapping.data_type is given
// 					throw new SdaiException(SdaiException.FN_NAVL, 
// 											"No support for selects yet (2): " + 
// 											ximInstance + "\n" + genAttMapping + "\n" + domain);
				}
			}
		}
		return false;
	}

	private static void assignAttrOrAggr(EEntity ximInstance, Object attrOrAggr, Object attrValue,
										 EDefined_type[] select) throws SdaiException {
		if(attrOrAggr instanceof EAttribute) {
			ximInstance.set((EAttribute)attrOrAggr, attrValue, select);
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

	private static Aggregate createAttrOrAggr(EEntity ximInstance, Object attrOrAggr, EDefined_type[] selectTypes,
											  boolean mandatoryOnly) throws SdaiException {
		if(attrOrAggr instanceof EAttribute) {
			EAttribute attribute = (EAttribute)attrOrAggr;
			if(mandatoryOnly 
			   && (!(attribute instanceof EExplicit_attribute) 
				   || ((EExplicit_attribute)attribute).getOptional_flag(null))) {
				return null;
			}
			EDefined_type tempSelect[] = new EDefined_type[20];
			if(ximInstance.testAttribute(attribute, tempSelect) == 0) {
				return ximInstance.createAggregate(attribute, selectTypes);
			} else {
				return (Aggregate)ximInstance.get_object(attribute);
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

	private static boolean assignGeneralEntitySelect(EEntity ximInstance, MappingContext mappingContext,
													 Object attrOrAggr,  ESelect_type selectDomain,
													 EEntity attrValue) throws SdaiException {
		ANamed_type selections = selectDomain.getSelections(null, mappingContext.getContext());
		SdaiIterator selectionIter = selections.createIterator();
		int entDefCount = 0;
		while(selectionIter.next()) {
			ENamed_type selection = selections.getCurrentMember(selectionIter);
			if(selection instanceof EEntity_definition) {
				entDefCount++;
				EEntity attrInstance =
					findMappedInstance(mappingContext, attrValue, (EEntity_definition)selection, BUILD_FLAG_FIND_NULL);
				if(attrInstance != null) {
					assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
					return true;
				}
			} else if(selection instanceof EDefined_type) {
				EEntity definedTypeDomain = ((EDefined_type)selection).getDomain(null);
				if(definedTypeDomain instanceof ESelect_type) {
					if(assignGeneralEntitySelect(ximInstance, mappingContext, attrOrAggr,
												 (ESelect_type)definedTypeDomain, attrValue)) {
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
					EEntity attrInstance =
						findMappedInstance(mappingContext, (EEntity)attrValue, (EEntity_definition)selection, BUILD_FLAG_FIND_NOT_NULL);
					assignAttrOrAggr(ximInstance, attrOrAggr, attrInstance, null);
					return true;
				}
			}
		}
		return false;
	}

	private static EEntity findMappedInstance(MappingContext mappingContext, EEntity mimInstance,
											  EEntity_definition entityDefinition,
											  int buildFlag) throws SdaiException {
		EEntity ximMappedInstance = mappingContext.getMappedInstance(mimInstance);
		if(ximMappedInstance != null) {
			if(ximMappedInstance.isInstanceOf(entityDefinition) || ximMappedInstance.isKindOf(entityDefinition)) {
				return ximMappedInstance;
			} else if(buildFlag == BUILD_FLAG_FIND_NULL) {
				return null;
			} else {
				throw new SdaiException(SdaiException.SY_ERR, "Attribute value " + ximMappedInstance.getPersistentLabel() +
										"=" + ximMappedInstance.getInstanceType().getName(null) +
										"(...) is not of type " + entityDefinition.getName(null) +
										"[" + entityDefinition.getPersistentLabel() + "]");
// 				throw new SdaiException(SdaiException.SY_ERR, "Found mapped instance: " + ximMappedInstance +
// 										" for " + mimInstance + " is of wrong entity type.\n" +
// 										"Requested entity definition: " + entityDefinition);
			}
		} else if(mimInstance.isInstanceOf(entityDefinition) || mimInstance.isKindOf(entityDefinition)) {
			return mimInstance;
		} else if(buildFlag == BUILD_FLAG_FIND_NULL) {
			return null;
		} else {
			throw new SdaiException(SdaiException.SY_ERR, "Can not find mapped instance: " + mimInstance +
									"\nentity definition: " + entityDefinition);
		}
	}

	private static EEntity substituteToComplexType(MappingContext mappingContext,
			EEntity inInstance, Collection complexNames,
			Object[] cplxTypeTemp) throws SdaiException {
		SdaiModel workingModel = inInstance.findEntityInstanceSdaiModel();

		StringBuffer complexNameBuffer = new StringBuffer();
		for(Iterator i = complexNames.iterator(); i.hasNext(); ) {
			complexNameBuffer.append(i.next()).append('+');
		}
		complexNameBuffer.setLength(complexNameBuffer.length() - 1);

		EEntity_definition complextEntityDefinition = 
			workingModel.getUnderlyingSchema().getEntityDefinition(
					complexNameBuffer.toString());
		EEntity outInstance =
			workingModel.substituteInstance(inInstance, complextEntityDefinition);
		if(cplxTypeTemp != null) {
			for (int i = 1; i < cplxTypeTemp.length; i++) {
				unsetMappedAttributes(mappingContext, outInstance,
						(EEntity_mapping) cplxTypeTemp[i]);
			}
		}
		return outInstance;
	}

	private static void unsetMappedAttributes(MappingContext mappingContext,
			EEntity ximInstance, EEntity_mapping entityMapping)
	throws SdaiException {
		AGeneric_attribute_mapping attributeMappings = new AGeneric_attribute_mapping();
		CGeneric_attribute_mapping.usedinParent_entity(null, entityMapping,
				mappingContext.getContext().mappingDomain, attributeMappings);
		EEntity_definition ximType = ximInstance.getInstanceType();
		Set allowedAttributes = new HashSet();
		EExplicit_attribute[] explicitAttributes = getEntityExplicitAttributes(ximType);
		Field[] explicitAttributeFields = getEntityAttributeFields(ximType);
		for (int i = 0; i < explicitAttributes.length; i++) {
			if(explicitAttributeFields[i] != null) {
				allowedAttributes.add(explicitAttributes[i]);
			}
		}
		for(SdaiIterator i = attributeMappings.createIterator(); i.next(); ) {
			EGeneric_attribute_mapping attributeMapping = attributeMappings.getCurrentMember(i);
			EAttribute ximAttribute = attributeMapping.getSource(null);
			if(ximAttribute instanceof EExplicit_attribute) {
				EExplicit_attribute baseAttribute = (EExplicit_attribute) ximAttribute;
				while(baseAttribute.testRedeclaring(null)) {
					baseAttribute = baseAttribute.getRedeclaring(null);
				}
				if(allowedAttributes.contains(baseAttribute)) {
					ximInstance.unsetAttributeValue(ximAttribute);
				}
			}
		}
	}

	private static void printComplexTypes(PrintWriter out, Collection complexNames) throws SdaiException {
		for(Iterator j = complexNames.iterator(); j.hasNext(); ) {
			String ximName = j.next().toString();
			out.print(j.hasNext() ? ximName + "+" : ximName);
		}
	}

}
