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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import jsdai.dictionary.AEntity_definition;
import jsdai.dictionary.CEntity_definition;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.MappingContext.ImmutableArrayMap;
import jsdai.lang.MappingContext.ImmutableArraySet;
import jsdai.mapping.AAttribute_mapping;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.AGeneric_attribute_mapping;
import jsdai.mapping.CAttribute_mapping;
import jsdai.mapping.CEntity_mapping;
import jsdai.mapping.CGeneric_attribute_mapping;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.EGeneric_attribute_mapping;



/**
 * This class is a superclass of CEntity_mapping class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author  Vaidas Narg�las
 * @version $Revision$
 */
public abstract class CEntityMappingBase extends CEntity implements MappingConstraintMatcher {

	boolean strongAttributesCollected;
	/** List of strong attributes, this list stores instances of EGeneric_attribute_mapping */
	HashMap strongAttributes;

	Map attMappingsByAtt;
	List armSubtypeLevels;
	int armSubtypeLevelSize;

	/** Protected to disable creation of instances of this class */
    protected CEntityMappingBase() {
		super();
		strongAttributesCollected = false;
		strongAttributes = null;
		attMappingsByAtt = null;
		armSubtypeLevels = null;
		armSubtypeLevelSize = 0;
    }

	void collectStrongAttributes() throws SdaiException {
		if(!strongAttributesCollected) {
			AGeneric_attribute_mapping attributeMappings = new AGeneric_attribute_mapping();
			CGeneric_attribute_mapping.usedinParent_entity(null,
				(EEntity_mapping)this, null, attributeMappings);
			SdaiIterator attributeMappingIter = attributeMappings.createIterator();
			while(attributeMappingIter.next()) {
				EGeneric_attribute_mapping attributeMapping =
					attributeMappings.getCurrentMember(attributeMappingIter);
				if(attributeMapping.getStrong(null)) {
					if(strongAttributes == null) strongAttributes = new HashMap();
					String attributeName = attributeMapping.getSource(null).getName(null);
					LinkedList oneAttributeList = (LinkedList)strongAttributes.get(attributeName);
					if(oneAttributeList == null) {
						oneAttributeList = new LinkedList();
						strongAttributes.put(attributeName, oneAttributeList);
					}
					oneAttributeList.add(attributeMapping);
				}
			}

			strongAttributesCollected = true;
		}
	}

	/**
	 * Makes the list of subtype lists containing <code>SubtypeNode</code> objects.
	 *
	 * @param mappingDomain an <code>ASdaiModel</code> value
	 * @exception SdaiException if an error occurs
	 */
	void findSubtypeMappings(ASdaiModel mappingDomain) throws SdaiException {
		if(armSubtypeLevels == null) {
			EEntity_mapping selfInterface = (EEntity_mapping)this;

			armSubtypeLevels = new ArrayList();
			ListIterator armSubtypeLevelsIter = armSubtypeLevels.listIterator();
			armSubtypeLevelSize = findSubtypeMappings(selfInterface.getSource(null),
													  mappingDomain, armSubtypeLevelsIter,
													  null, selfInterface.getTarget(null), 0);
		}
	}

	private static int findSubtypeMappings(EEntity_definition armEntity, ASdaiModel mappingDomain,
										   ListIterator subtypeLevelsIter, SubtypeNode parentNode,
										   EEntity aimEntity, int nextNodeIndex) throws SdaiException {
		AEntity_definition armSubtypes = new AEntity_definition();
		CEntity_definition.usedinSupertypes(null, armEntity, mappingDomain, armSubtypes);
		if(armSubtypes.getMemberCount() == 0) {
			CEntity_definition.usedinSupertypes(null, armEntity, null, armSubtypes);
		}
		if(armSubtypes.getMemberCount() > 0) {
			List levelSubtypeNodes;
			if(subtypeLevelsIter.hasNext()) {
				levelSubtypeNodes = (List)subtypeLevelsIter.next();
			} else {
				levelSubtypeNodes = new ArrayList();
				subtypeLevelsIter.add(levelSubtypeNodes);
			}
			SdaiIterator armSubtypeIter = armSubtypes.createIterator();
			while(armSubtypeIter.next()) {
				EEntity_definition armSubtype = armSubtypes.getCurrentMember(armSubtypeIter);

				SubtypeNode newNode = new SubtypeNode(parentNode, nextNodeIndex++);
				levelSubtypeNodes.add(newNode);
				nextNodeIndex = findSubtypeMappings(armSubtype, mappingDomain, subtypeLevelsIter,
													newNode, aimEntity, nextNodeIndex);

				if(armSubtype.getInstantiable(null)) {
					AEntity_mapping oneSubtypeMappings = new AEntity_mapping();
					CEntity_mapping.usedinSource(null, armSubtype, mappingDomain, oneSubtypeMappings);
					SdaiIterator oneSubtypeMappingIter = oneSubtypeMappings.createIterator();
					while(oneSubtypeMappingIter.next()) {
						EEntity_mapping oneSubtypeMapping =
							oneSubtypeMappings.getCurrentMember(oneSubtypeMappingIter);
						if(isMappingEqual(oneSubtypeMapping.getTarget(null), aimEntity)) {
							newNode.mappings.add(oneSubtypeMapping);
						}
					}
				}
			}
			subtypeLevelsIter.previous();
		}
		return nextNodeIndex;
	}

	static boolean isMappingEqual(EEntity entity, EEntity testEntity) throws SdaiException {
		if(entity == testEntity) {
			return true;
		}
		if(!(entity instanceof EEntity_definition && testEntity instanceof EEntity_definition)) {
			return false;
		}
 		EEntity_definition entityDefinition = (EEntity_definition)entity;
 		EEntity_definition testEntityDefinition = (EEntity_definition)testEntity;
 		if(testEntityDefinition.getComplex(null)) {
			AEntity leaves = testEntityDefinition.getGeneric_supertypes(null);
			SdaiIterator leafIterator = leaves.createIterator();
			while(leafIterator.next()) {
				EEntity_definition leaf = (EEntity_definition)leaves.getCurrentMemberObject(leafIterator);
				if(!isMappingEqualEntity(entityDefinition, leaf)) {
					return false;
				}
			}
			return true;

 		} else {
 			return isMappingEqualEntity(entityDefinition, testEntityDefinition);
 		}

	}
	private static boolean isMappingEqualEntity(EEntity_definition entityDefinition,
			EEntity_definition testEntityDefinition) throws SdaiException {
		if(entityDefinition == testEntityDefinition) {
			return true;
		}
		if(entityDefinition.testGeneric_supertypes(null)) {
			AEntity supertypes = entityDefinition.getGeneric_supertypes(null);
			SdaiIterator supertypesIter = supertypes.createIterator();
			while(supertypesIter.next()) {
				EEntity_definition supertype =
					(EEntity_definition) supertypes.getCurrentMemberObject(supertypesIter);
				if(isMappingEqualEntity(supertype, testEntityDefinition)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		try {
			MappingMatcherInstances mappingInstances =
				mappingContext.getCacheMappingInstances(this, instances);
			if(mappingInstances != null) {
				MatcherInstances outInstances = mappingInstances.getMatcherInstances();
				if(mappingInstances != outInstances && !mappingContext.preprocessInstances) {
					mappingContext.setPersistentCacheInstances(this, instances, outInstances);
					callAttrCreators(mappingContext, mappingInstances.getConcreteInstances());
				}
				return outInstances;
			} else {
				MatcherInstances outInstances;
				EEntity_mapping selfInterface = (EEntity_mapping)this;

				Set preprocessedInstances =
					mappingContext.creator.preprocessTargetInstances(selfInterface);
				if(preprocessedInstances != null) {
					Map preprocessedInstanceMap =
						new FixedValueMap(preprocessedInstances, NON_PATH_VALUE);
					outInstances =
						new MatcherInstances(preprocessedInstanceMap, MatcherInstances.STATUS_FORWARD);
					mappingContext.setPersistentCacheInstances(this, instances, outInstances);
					return outInstances;
				}

				EEntity_definition source = selfInterface.getSource(null);
				EEntity_definition target = (EEntity_definition)selfInterface.getTarget(null);

				ASdaiModel workingModels = mappingContext.context.working_modelAggr;
				MatcherInstances attributeStartInstances =
					mappingContext.newAllInstances(MatcherInstances.STATUS_PATH_FORWARD);
				MatcherInstances entityStartInstances =
					mappingContext.newAllInstances(MatcherInstances.STATUS_FORWARD);

				if(selfInterface.testConstraints(null)) {
					outInstances =
						((MappingConstraintMatcher)selfInterface.getConstraints(null))
						.findForward(mappingContext, entityStartInstances, true)
						.extractType(mappingContext, target);
					if(!instances.getInstanceMap().equals(workingModels.getInstances())) {
						outInstances = instances.intersect(outInstances);
					}
				} else {
					outInstances = instances.extractType(mappingContext, target);
				}

				//Temporary setting to block recursion (through strong attributes and users)
				mappingContext.setPersistentCacheInstances(this, instances, outInstances);

				if(!outInstances.isEmpty()) {
					collectStrongAttributes();
					if(strongAttributes != null) {
						mappingContext.collectingStrongAttributes = true;
						try {
							Iterator strongAttrIter = strongAttributes.values().iterator();
							while(strongAttrIter.hasNext()) {
								List oneAttributeList = (List)strongAttrIter.next();
								Iterator oneAttrIter = oneAttributeList.iterator();
								if(oneAttrIter.hasNext()) {
									MatcherInstances strongInstances =
										((MappingConstraintMatcher)oneAttrIter.next())
										.findPathForward(mappingContext,
												attributeStartInstances, true);
									while(oneAttrIter.hasNext()) {
										MatcherInstances attributeInstances =
											((MappingConstraintMatcher)oneAttrIter.next())
											.findPathForward(mappingContext,
													attributeStartInstances, true);
										strongInstances = strongInstances.union(attributeInstances);
									}
									outInstances = outInstances.intersect(strongInstances);
								}
							}
						} finally {
							mappingContext.collectingStrongAttributes = false;
						}
						mappingContext.setPersistentCacheInstances(this, instances, outInstances);
					}
				}

				if(selfInterface.getStrong_users(null)) {
					boolean oldPreprocessInstances = mappingContext.preprocessInstances;
					mappingContext.preprocessInstances = true;
					AAttribute_mapping userAttrMappings = new AAttribute_mapping();
					MatcherInstances strongUserInstances =
						findAllUsers(selfInterface, mappingContext, attributeStartInstances,
									 entityStartInstances, target, userAttrMappings, null);
					mappingContext.preprocessInstances = oldPreprocessInstances;
					if(!outInstances.isEmpty()) {
						outInstances = outInstances.intersectValues(strongUserInstances);
						mappingContext.setPersistentCacheInstances(this, instances, outInstances);
					}
				}

				if(!outInstances.isEmpty()) {
					MatcherInstances concreteInstances = outInstances;
					findSubtypeMappings(mappingContext.context.mappingDomain);
					Iterator armSubtypeLevelsIter = armSubtypeLevels.iterator();
					if(source.getInstantiable(null)) {
						boolean oldPreprocessInstances = mappingContext.preprocessInstances;
						mappingContext.preprocessInstances = true;
						while(armSubtypeLevelsIter.hasNext()) {
							List levelSubtypeNodes = (List)armSubtypeLevelsIter.next();
							Iterator levelSubtypeNodesIter = levelSubtypeNodes.iterator();
							while(levelSubtypeNodesIter.hasNext()) {
								SubtypeNode subtypeNode =  (SubtypeNode)levelSubtypeNodesIter.next();
								Iterator mappingsIter = subtypeNode.mappings.iterator();
								while(mappingsIter.hasNext()) {
									MatcherInstances subtypeInstances =
										((MappingConstraintMatcher)mappingsIter.next())
										.findForward(mappingContext, instances, false);
									concreteInstances = concreteInstances.difference(subtypeInstances);
								}
							}
						}
						mappingContext.preprocessInstances = oldPreprocessInstances;
						if(mappingContext.preprocessInstances) {
							mappingContext.setPersistentCacheInstances(this, instances,
									new MappingPreInstances(outInstances,
											concreteInstances));
						} else {
							callAttrCreators(mappingContext, concreteInstances);
						}
					} else {
						while(armSubtypeLevelsIter.hasNext()) {
							List levelSubtypeNodes = (List)armSubtypeLevelsIter.next();
							Iterator levelSubtypeNodesIter = levelSubtypeNodes.iterator();
							while(levelSubtypeNodesIter.hasNext()) {
								SubtypeNode subtypeNode =  (SubtypeNode)levelSubtypeNodesIter.next();
								Iterator mappingsIter = subtypeNode.mappings.iterator();
								while(mappingsIter.hasNext()) {
									((MappingConstraintMatcher)mappingsIter.next())
										.findForward(mappingContext, instances, false);
								}
							}
						}
						releaseAttrConstraints(mappingContext);
					}

				} else {
					releaseAttrConstraints(mappingContext);
				}

				return outInstances;
			}
		} catch(SdaiException e) {
			SdaiException wrapper =
				new SdaiException(e.getErrorId(), e, ((EEntity)this).getPersistentLabel());
			wrapper.initCause(e);
			throw wrapper;
		} catch(IllegalStateException  e) {
			Throwable cause = e.getCause();
			if(cause instanceof SdaiException) {
				SdaiException sdaiCause = (SdaiException)cause;
				throw (SdaiException)new SdaiException(sdaiCause.getErrorId(), sdaiCause,
													   ((EEntity)this).getPersistentLabel()).initCause(sdaiCause);
			} else {
				throw (SdaiException)new SdaiException(SdaiException.SY_ERR, e.toString(),
													   ((EEntity)this).getPersistentLabel()).initCause(e);
			}
		}
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
								"This constraint can not be called as part of backward references");
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
								"This constraint can not be called as part of path forward references");
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
				"This constraint can not be called as part of path forward references: " +
				this);
    }

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
				"This constraint can not be called as part of path backward references");
	}

    private static MatcherInstances
		findAllUsers(EEntity_mapping entityMapping, MappingContext mappingContext,
					 MatcherInstances attributeStartInstances, MatcherInstances entityStartInstances,
					 EEntity_definition target, AAttribute_mapping userAttrMappings,
					 MatcherInstances strongUserInstances) throws SdaiException {

		userAttrMappings.clear();
		CAttribute_mapping.usedinDomain(null, entityMapping,
										mappingContext.context.mappingDomain, userAttrMappings);
		SdaiIterator userAttrMappingIter = userAttrMappings.createIterator();
		while(userAttrMappingIter.next()) {
			EAttribute_mapping userAttrMapping =
				userAttrMappings.getCurrentMember(userAttrMappingIter);
			MatcherInstances entityInstances =
				((MappingConstraintMatcher)userAttrMapping.getParent_entity(null))
				.findForward(mappingContext, entityStartInstances, true);
			if(!entityInstances.instances.isEmpty()) {
				MatcherInstances attributeInstances =
					((MappingConstraintMatcher)userAttrMapping)
					.findPathForward(mappingContext, attributeStartInstances, true)
					.intersect(entityInstances);
				strongUserInstances = strongUserInstances == null ?
						attributeInstances : strongUserInstances.union(attributeInstances);
			}
		}
		AEntity_definition supertypes = entityMapping.getSource(null).getSupertypes(null);
		SdaiIterator supertypeIter = supertypes.createIterator();
		while(supertypeIter.next()) {
			EEntity_definition supertype = supertypes.getCurrentMember(supertypeIter);
			AEntity_mapping supertypeMappings = new AEntity_mapping();
			CEntity_mapping.usedinSource(null, supertype,
										 mappingContext.context.mappingDomain, supertypeMappings);
			SdaiIterator supertypeMappingIter = supertypeMappings.createIterator();
			while(supertypeMappingIter.next()) {
				EEntity_mapping supertypeMapping =
					supertypeMappings.getCurrentMember(supertypeMappingIter);
				EEntity supertypeTarget = supertypeMapping.getTarget(null);
				if(supertypeTarget == target || target.isSubtypeOf((CEntityDefinition)supertypeTarget)) {
					strongUserInstances =
						findAllUsers(supertypeMapping, mappingContext,
									 attributeStartInstances, entityStartInstances, target,
									 userAttrMappings, strongUserInstances);
				}
			}
		}
		return strongUserInstances;
	}

	private void callAttrCreators(MappingContext mappingContext,
								  MatcherInstances concreteInstances) throws SdaiException {
		if(!concreteInstances.isEmpty()) {
			EEntity_mapping selfInterface = (EEntity_mapping)this;
			Map targetInstanceMap = concreteInstances.getInstanceMap();
			Set targetInstances = targetInstanceMap.keySet();
			if(!mappingContext.interleavedCreation && targetInstanceMap instanceof AEntityMap) {
				targetInstances = new ImmutableArraySet(targetInstances);
			}
			MatcherInstances attributeStartInstances =
				mappingContext.newAllInstances(MatcherInstances.STATUS_PATH_FORWARD);
			// Map attribute values
			final List mappingList = new ArrayList();
			mappingList.add(selfInterface);
			mappingList.add(targetInstances);
			AEntity attributeMappings = new AEntity();
			CGeneric_attribute_mapping
				.usedinParent_entity(null, selfInterface,
									 mappingContext.context.mappingDomain, attributeMappings);
			if(attributeMappings.getMemberCount() > 0) {
				SdaiIterator attributeMappingIter = attributeMappings.createIterator();
				while(attributeMappingIter.next()) {
					Object attributeMapping =
						attributeMappings.getCurrentMemberObject(attributeMappingIter);
					MatcherInstances attributeInstances =
						((MappingConstraintMatcher)attributeMapping)
						.findPathForward(mappingContext, attributeStartInstances, true);
					mappingList.add(attributeMapping);
					Map attributeInstanceMap = attributeInstances.getInstanceMap();
					if(!mappingContext.interleavedCreation
							&& attributeInstanceMap instanceof AEntityMap) {
						attributeInstanceMap = new ImmutableArrayMap(attributeInstanceMap);
					}
					mappingList.add(attributeInstanceMap);
				}
			}
			mappingContext.mappingList.addAll(mappingList);
		} else {
			releaseAttrConstraints(mappingContext);
		}
	}

	private void releaseAttrConstraints(MappingContext mappingContext) throws SdaiException {
		EEntity_mapping selfInterface = (EEntity_mapping)this;
		AEntity attributeMappings = new AEntity();
		CGeneric_attribute_mapping.usedinParent_entity(null, selfInterface,
				mappingContext.context.mappingDomain, attributeMappings);
		for(SdaiIterator i = attributeMappings.createIterator(); i.next(); ) {
			EGeneric_attribute_mapping genAttributeMapping =
				(EGeneric_attribute_mapping)attributeMappings.getCurrentMemberEntity(i);
			boolean asStrongUser = false;
			if(genAttributeMapping instanceof EAttribute_mapping) {
				EAttribute_mapping attributeMapping = (EAttribute_mapping)genAttributeMapping;
				if(attributeMapping.testDomain(null)) {
					EEntity domain = attributeMapping.getDomain(null);
					if(domain instanceof EEntity_mapping) {
						asStrongUser = ((EEntity_mapping)domain).getStrong_users(null);
					}
				}
			}
			if(!asStrongUser && genAttributeMapping.testConstraints(null)
					&& mappingContext.getCacheInstances(genAttributeMapping) == null) {
				EEntity constraints = genAttributeMapping.getConstraints(null);
				mappingContext.decCacheInstancesUseCnt(constraints, true);
				mappingContext.setPersistentCacheInstances(genAttributeMapping, null, null);
			}
		}
	}

	/** Subtypes node class */
	static final class SubtypeNode {

		public final SubtypeNode parent;
		/** List of EEntity_mappings */
		public final List mappings;
		public final int index;

		private SubtypeNode(SubtypeNode parent, int index) {
			this.mappings = new ArrayList();
			this.parent = parent;
			this.index = index;
		}

		public String toString() {
			return "(parent: " + parent + ", mappings: " + mappings + ", index: " + index + ")@" +
				System.identityHashCode(this) + "\n";
		}
	}
}
