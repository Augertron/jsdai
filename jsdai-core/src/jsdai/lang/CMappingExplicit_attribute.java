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

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EDerived_attribute;
import jsdai.dictionary.EExplicit_attribute;
import jsdai.lang.MappingContext.CachedInstances;
import jsdai.lang.MappingContext.ImmutableArrayMap;
import jsdai.lang.MappingContext.ImmutableArraySet;
import jsdai.mapping.EAggregate_member_constraint;
import jsdai.mapping.EBoolean_constraint;
import jsdai.mapping.EEnumeration_constraint;
import jsdai.mapping.EInteger_constraint;
import jsdai.mapping.ELogical_constraint;
import jsdai.mapping.EReal_constraint;
import jsdai.mapping.ESelect_constraint;
import jsdai.mapping.EString_constraint;
import jsdai.util.LangUtils;

/**
 *
 * This class is a superclass of CExplicit_attribute class in package jsdai.dictionary.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class CMappingExplicit_attribute extends AttributeDefinition
	implements MappingConstraintPath, MappingConstraintMatcher {

	protected CMappingExplicit_attribute() { }

	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EAttribute selfInterface = (EAttribute)this;
		mappingContext.attributeMapUsersForward(selfInterface, instances);
	}

	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EAttribute selfInterface = (EAttribute)this;
		mappingContext.attributeMapUsersBackward(selfInterface, instances);
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			return generalFindForward((EAttribute)this, mappingContext,
					instances.dup(), false, decCacheUseCnt);
		}
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			return generalFindBackward((EAttribute)this, mappingContext,
					instances.dup(), decCacheUseCnt);
		}
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			return generalFindForward((EAttribute)this, mappingContext,
					instances.dup(), true, decCacheUseCnt);
		}
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
    	return null;
    }

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			return generalFindBackward((EAttribute)this, mappingContext,
					instances.dup(), decCacheUseCnt);
		}
	}

	static MatcherInstances generalFindForward(EAttribute attribute, MappingContext mappingContext,
			MatcherInstances instances, boolean inPath, boolean decCacheUseCnt) throws SdaiException {
		MatcherInstances outInstances;
		boolean dontTestPath =
			instances.attributePathTop < 0
			|| (instances.attributePathTop == 0
				&& instances.isAttributeMemberAt(0)
				&& instances.getAttributeMemberAt(0) < 0);
		Map instanceMap = instances.getInstanceMap();
		ASdaiModel workingModels = mappingContext.context.working_modelAggr;
		if(instanceMap.equals(workingModels.getInstances())) {
			AEntity instanceAgg = workingModels.getInstances(attribute.getParent_entity(null));
			outInstances =
				new MatcherInstances(new AEntityAttrMap(attribute, dontTestPath ? null : instances.dup(),
														instanceAgg, inPath), instances.status);
			if(!inPath) {
				Map valueMap = new HashMap();
				MatcherInstances constraintUserInstances = instances.dup(false);
				findConstraintUsers(mappingContext, constraintUserInstances, attribute,
									constraintUserInstances, attribute, valueMap);
				if(!valueMap.isEmpty()) {
					SdaiIterator instanceIter = instanceAgg.createIterator();
					while(instanceIter.next()) {
						EEntity instance = instanceAgg.getCurrentMemberEntity(instanceIter);
						if(((CEntity) instance).testAttributeFast(
								attribute, mappingContext.selectPath) > 0) {
							Object value = instance.get_object(attribute);
							testValueInMap(mappingContext, instance, value, value, valueMap);
						}
					}
				}
			}
		} else {
			Map outInstanceMap = new HashMap();
			Iterator instanceIter = instanceMap.entrySet().iterator();
			AEntity aggValues = new AEntity();
			if(dontTestPath) {
				while(instanceIter.hasNext()) {
					Map.Entry entry = (Map.Entry)instanceIter.next();
					EEntity instance = (EEntity)entry.getKey();
					Object origValue = entry.getValue();
					instance.findEntityInstanceUsedin(attribute, workingModels, aggValues);
					SdaiIterator aggValueIter = aggValues.createIterator();
					while(aggValueIter.next()) {
						EEntity aggValue = aggValues.getCurrentMemberEntity(aggValueIter);
						putToMatcherInstancesMap(outInstanceMap, aggValue, origValue);
					}
					aggValues.clear();
				}
			} else {
				while(instanceIter.hasNext()) {
					Map.Entry entry = (Map.Entry)instanceIter.next();
					EEntity instance = (EEntity)entry.getKey();
					Object origValue = entry.getValue();
					instance.findEntityInstanceUsedin(attribute, workingModels, aggValues);
					SdaiIterator aggValueIter = aggValues.createIterator();
					while(aggValueIter.next()) {
						EEntity aggValue = aggValues.getCurrentMemberEntity(aggValueIter);
						aggValue.testAttribute(attribute, mappingContext.selectPath);
						Object pathValue =
							instances.followAttributePath(aggValue.get_object(attribute),
									instances.attributePathTop, mappingContext.selectPath);
						if(pathValue instanceof Collection) {
							if(((Collection)pathValue).contains(instance)) {
								putToMatcherInstancesMap(outInstanceMap, aggValue, origValue);
							}
						} else {
							if(pathValue == instance) {
								putToMatcherInstancesMap(outInstanceMap, aggValue, origValue);
							}
						}
					}
					aggValues.clear();
				}
			}
			convertValueSets(outInstanceMap);
			outInstances =
				new MatcherInstances(new ImmutableArrayMap(outInstanceMap), instances.status);
		}
		CachedInstances attrCacheInstances = mappingContext.getCacheInstances(attribute);
		if(attrCacheInstances == null || attrCacheInstances.remainingGetCount > 0) {
			mappingContext.setCacheInstances(attribute, instances, outInstances, decCacheUseCnt);
		}
		return outInstances;
	}

	static MatcherInstances generalFindBackward(EAttribute attribute, MappingContext mappingContext,
			MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		Map instanceMap =
			instances.extractType(mappingContext, attribute.getParent_entity(null)).getInstanceMap();
		Map outInstanceMap = new HashMap();
		Iterator instanceIter = instanceMap.entrySet().iterator();
		while(instanceIter.hasNext()) {
			Map.Entry entry = (Map.Entry)instanceIter.next();
			EEntity instance = (EEntity)entry.getKey();
			if(((CEntity)instance).testAttributeFast(attribute, mappingContext.selectPath) > 0) {
				Object value = instance.get_object(attribute);
				if(instances.attributePathTop >= 0) {
					value = instances.followAttributePath(value,
							instances.attributePathTop, mappingContext.selectPath);
					if(value == null) {
						continue;
					}
					if(value instanceof Collection) {
						Iterator valueIter = ((Collection)value).iterator();
						Object origValue = entry.getValue();
						while(valueIter.hasNext()) {
							putToMatcherInstancesMap(outInstanceMap, valueIter.next(), origValue);
						}
						continue;
					}
				}
				if(value instanceof EEntity) {
					putToMatcherInstancesMap(outInstanceMap, value, entry.getValue());
				} else if(value instanceof Aggregate) { // FIXME: Should aggregate support be enhanced?
					Aggregate aggValues = (Aggregate)value;
					Object origValue = entry.getValue();
					SdaiIterator aggValueIter = aggValues.createIterator();
					while(aggValueIter.next()) {
						Object aggValue = aggValues.getCurrentMemberObject(aggValueIter);
						if(aggValue instanceof EEntity) {
							putToMatcherInstancesMap(outInstanceMap, aggValue, origValue);
						}
					}
				} else {
					throw new SdaiException(SdaiException.SY_ERR, "Unexpected value: " + value +
											" class: " + value.getClass());
				}
			}
		}
		convertValueSets(outInstanceMap);
		MatcherInstances outInstances =
			new MatcherInstances(new ImmutableArrayMap(outInstanceMap), instances.status);
		mappingContext.setCacheInstances(attribute, instances, outInstances, decCacheUseCnt);
		return outInstances;
	}

	private static void findConstraintUsers(MappingContext mappingContext, MatcherInstances instances,
											EAttribute attribute, MatcherInstances constraintInstances,
											EEntity parentConstraint,
											Map valueMap) throws SdaiException {
		AEntity constraintUsers = new AEntity();
		EDefined_type definedType = null;
		parentConstraint.findEntityInstanceUsers(mappingContext.context.mappingDomain, constraintUsers);
		SdaiIterator constraintUserIter = constraintUsers.createIterator();
		while(constraintUserIter.next()) {
			EEntity constraintUser = constraintUsers.getCurrentMemberEntity(constraintUserIter);
			Object value = null;
			if(constraintUser instanceof EString_constraint) {
				EString_constraint constraint = (EString_constraint)constraintUser;
				value = constraint.getConstraint_value(null);
			} else if(constraintUser instanceof EEnumeration_constraint) {
				EEnumeration_constraint constraint = (EEnumeration_constraint)constraintUser;
				if(definedType == null) {
					definedType = (EDefined_type)((EExplicit_attribute)attribute).getDomain(null);
				}
				String stringValue = constraint.getConstraint_value(null);
				value =
					new Integer(LangUtils.convertEnumerationStringToInt(stringValue, definedType));
			} else if(constraintUser instanceof EInteger_constraint) {
				EInteger_constraint constraint = (EInteger_constraint)constraintUser;
				value = new Integer(constraint.getConstraint_value(null));
			} else if(constraintUser instanceof ELogical_constraint) {
				ELogical_constraint constraint = (ELogical_constraint)constraintUser;
				value = new Integer(constraint.getConstraint_value(null));
			} else if(constraintUser instanceof EReal_constraint) {
				EReal_constraint constraint = (EReal_constraint)constraintUser;
				value = new Double(constraint.getConstraint_value(null));
			} else if(constraintUser instanceof EBoolean_constraint) {
				EBoolean_constraint constraint = (EBoolean_constraint)constraintUser;
				value = Boolean.valueOf(constraint.getConstraint_value(null));
			} else if(constraintUser instanceof EAggregate_member_constraint) {
				EAggregate_member_constraint constraint = (EAggregate_member_constraint)constraintUser;
				MatcherInstances amcInstances = constraintInstances.dup();
				amcInstances.attributePathReversePush(constraint.testMember(null) ?
													  constraint.getMember(null) : -1);
				findConstraintUsers(mappingContext, instances, attribute, amcInstances,
									constraint, valueMap);
			} else if(constraintUser instanceof ESelect_constraint) {
				ESelect_constraint constraint = (ESelect_constraint)constraintUser;
				MatcherInstances scInstances = constraintInstances.dup();
				scInstances.attributePathReversePush(constraint.getData_type(null));
				findConstraintUsers(mappingContext, instances, attribute, scInstances,
									constraint, valueMap);
			}
			if(value != null) {
				// FIXME: Derived attribute workaround
				if(attribute instanceof EDerived_attribute) {
					ASdaiModel workingModels = mappingContext.context.working_modelAggr;
					AEntity instanceAgg = workingModels.getInstances(attribute.getParent_entity(null));
					mappingContext.setUserCacheInstances(attribute, constraintUser, instances,
							constraintInstances.dup(new AEntityMap(instanceAgg, false),
									constraintInstances.status));
				} else {
					MatcherInstances cuInstances =
						constraintInstances.dup(new HashMap(), constraintInstances.status);
					mappingContext.setUserCacheInstances(attribute, constraintUser, instances, cuInstances);
					Object prevInstances = valueMap.put(value, cuInstances);
					if(prevInstances instanceof List) {
						List valueList = (List)prevInstances;
						valueList.add(cuInstances);
						valueMap.put(value, valueList);
					} else if(prevInstances != null) {
						List valueList = new ArrayList();
						valueList.add(prevInstances);
						valueList.add(cuInstances);
						valueMap.put(value, valueList);
					}
				}
			}
		}
	}

	private static void testValueInMap(MappingContext mappingContext, EEntity instance, Object value,
									   Object attrValue, Map valueMap) throws SdaiException {
		if(value instanceof Aggregate) {
			Aggregate valueAggregate = (Aggregate)value;
			SdaiIterator valueAggIter = valueAggregate.createIterator();
			while(valueAggIter.next()) {
				if(valueAggregate.testCurrentMember(valueAggIter, mappingContext.selectPath) >= 0) {
					testValueInMap(mappingContext, instance,
								   valueAggregate.getCurrentMemberObject(valueAggIter),
								   attrValue, valueMap);
				}
			}
		} else {
			Object valueInstances = valueMap.get(value);
			if(valueInstances instanceof List) {
				Iterator valueInstIter = ((List)valueInstances).iterator();
				while(valueInstIter.hasNext()) {
					addMatcherInstances(mappingContext, instance, attrValue,
										(MatcherInstances)valueInstIter.next());
				}
			} else if(valueInstances != null) {
				addMatcherInstances(mappingContext, instance, attrValue,
									(MatcherInstances)valueInstances);
			}
		}
	}

	private static void addMatcherInstances(MappingContext mappingContext, EEntity instance, Object value,
											MatcherInstances constraintInstances) throws SdaiException {
		if(constraintInstances.attributePathTop >= 0
		   && (constraintInstances.attributePathTop != 0
			   || !constraintInstances.isAttributeMemberAt(0)
			   || constraintInstances.getAttributeMemberAt(0) > 0)) {
			if(constraintInstances.followAttributePath(value,
					constraintInstances.attributePathTop, mappingContext.selectPath) != null) {
				constraintInstances.instances.put(instance, NON_PATH_VALUE);
			}
		} else {
			constraintInstances.instances.put(instance, NON_PATH_VALUE);
		}
	}

	private static void putToMatcherInstancesMap(Map instancesMap, Object key, Object value) {
		if(key instanceof AEntity) {
			throw new ClassCastException(key.toString());
		}
		Object oldValue = instancesMap.get(key);
		if(oldValue != null && oldValue != NON_PATH_VALUE) {
			if(value != NON_PATH_VALUE) {
				if(oldValue instanceof HashSet) {
					Set oldValueSet = (Set)oldValue;
					if(value instanceof Collection) {
						oldValueSet.addAll((Collection)value);
					} else {
						oldValueSet.add(value);
					}
				} else {
					if(oldValue instanceof Collection) {
						Set newValueSet = new HashSet((Collection)oldValue);
						if(value instanceof Collection) {
							newValueSet.addAll((Collection)value);
						} else {
							newValueSet.add(value);
						}
						instancesMap.put(key, newValueSet);
					} else if(value instanceof Collection) {
						Set newValueSet = new HashSet((Collection)value);
						newValueSet.add(oldValue);
						instancesMap.put(key, newValueSet);
					} else if(!oldValue.equals(value)) {
						Set newValueSet = new HashSet();
						newValueSet.add(value);
						newValueSet.add(oldValue);
						instancesMap.put(key, newValueSet);
					}
				}
			}
		} else {
			instancesMap.put(key, value);
		}
	}

	private static void convertValueSets(Map outInstanceMap) {
		for (Iterator i = outInstanceMap.entrySet().iterator(); i.hasNext();) {
			Entry entry = (Entry)i.next();
			Object value = entry.getValue();
			if(value instanceof HashSet) {
				entry.setValue(new ImmutableArraySet((Set)value));
			}
		}
	}

	static final class AEntityAttrSet extends AEntitySet {
		private EAttribute attribute;
		private MatcherInstances matcherInstances;
		private int size;
		private EDefined_type[] selectPath;

		AEntityAttrSet(EAttribute attribute, MatcherInstances matcherInstances, AEntity instances) {
			super(instances);
			this.attribute = attribute;
			this.matcherInstances = matcherInstances;
			size = -1;
			selectPath = new EDefined_type[20];
		}

		public Iterator iterator() {
			return new AEntityAttrIterator();
		}

		public int size() {
			try {
				if(size < 0) {
					size = 0;
					SdaiIterator instanceIter = instances.createIterator();
					while(instanceIter.next()) {
						EEntity instance = instances.getCurrentMemberEntity(instanceIter);
						if(isMatchingInstance(instance) != null) {
							size++;
						}
					}
				}
				return size;
			} catch(SdaiException e) {
				IllegalStateException wrapper = new IllegalStateException();
				wrapper.initCause(e);
				throw wrapper;
			}
		}

		public boolean contains(Object key) {
			if(key instanceof EEntity) {
				try {
					EEntity instance = (EEntity)key;
					return instances.isMember(instance) && isMatchingInstance(instance) != null;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			} else {
				return false;
			}
		}

		protected Object getEqualsObject() {
			return this;
		}

		public boolean equals(Object obj) {
			if(obj instanceof AEntityAttrSet) {
				AEntityAttrSet other = (AEntityAttrSet)obj;
				return instances.equals(other.instances) &&
					attribute == other.attribute &&
					matcherInstances.equalsAttributePath(other.matcherInstances);
			} else {
				return false;
			}
		}

		public int hashCode() {
			return instances.hashCode() ^ attribute.hashCode();
		}

		private Object isMatchingInstance(EEntity instance) throws SdaiException {
			if(((CEntity)instance).testAttributeFast(attribute, selectPath) > 0) {
				Object pathValue = instance.get_object(attribute);
				if(matcherInstances != null) {
					if(matcherInstances.followAttributePath(pathValue,
							matcherInstances.attributePathTop, selectPath) != null) {
						return pathValue;
					}
				}
				return pathValue;
			}
			return null;
		}

		class AEntityAttrIterator extends AEntityIterator {
			EEntity currInstance;

			AEntityAttrIterator() {
			}

			// Implementation of java.util.Iterator

			/**
			 * Describe <code>next</code> method here.
			 *
			 * @return an <code>Object</code> value
			 */
			public Object next() {
				if(hasNextResult < 0) {
					hasNext();
				}
				hasNextResult = -1;
				if(currInstance != null) {
					return currInstance;
				} else {
					throw new IllegalStateException();
				}
			}

			/**
			 * Describe <code>hasNext</code> method here.
			 *
			 * @return a <code>boolean</code> value
			 */
			public boolean hasNext() {
				try {
					if(hasNextResult < 0) {
						boolean isNext;
						while((isNext = iterator.next())) {
							currInstance = instances.getCurrentMemberEntity(iterator);
							if(isMatchingInstance(currInstance) != null) {
								break;
							}
							currInstance = null;
						}
						hasNextResult = isNext ? 1 : 0;
					}
					return hasNextResult > 0;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			}

		}
	}

	static final class AEntityAttrMap extends AEntityMap {
		private EAttribute attribute;
		private MatcherInstances matcherInstances;
		private int size;
		private EDefined_type[] selectPath;

		AEntityAttrMap(EAttribute attribute, MatcherInstances matcherInstances,
				AEntity instances, boolean inPath) {
			super(instances, inPath);
			this.attribute = attribute;
			this.matcherInstances = matcherInstances;
			size = -1;
			selectPath = new EDefined_type[20];
		}

		public int size() {
			try {
				if(size < 0) {
					size = 0;
					SdaiIterator instanceIter = instances.createIterator();
					while(instanceIter.next()) {
						EEntity instance = instances.getCurrentMemberEntity(instanceIter);
						if(isMatchingInstance(instance) != null) {
							size++;
						}
					}
				}
				return size;
			} catch(SdaiException e) {
				IllegalStateException wrapper = new IllegalStateException();
				wrapper.initCause(e);
				throw wrapper;
			}
		}

		public boolean containsKey(Object key) {
			if(key instanceof EEntity) {
				try {
					EEntity instance = (EEntity)key;
					return instances.isMember(instance) && isMatchingInstance(instance) != null;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			} else {
				return false;
			}
		}

		public Object get(Object key) {
			if(key instanceof EEntity) {
				try {
					EEntity instance = (EEntity)key;
					Object value = instances.isMember(instance) ? isMatchingInstance(instance) : null;
					if(!inPath && value != null) {
						value = NON_PATH_VALUE;
					} else if(value instanceof Aggregate) {
						value = new AEntityAttrEntry(instance, value);
					}
					return value;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			} else {
				return null;
			}
		}

		protected Object getEqualsObject() {
			return this;
		}

		public boolean equals(Object obj) {
			if(obj instanceof AEntityAttrMap) {
				AEntityAttrMap other = (AEntityAttrMap)obj;
				return instances.equals(other.instances) &&
					attribute == other.attribute &&
					((matcherInstances == null && other.matcherInstances == null)
							|| (matcherInstances != null && other.matcherInstances != null
									&& matcherInstances.equalsAttributePath(other.matcherInstances)));
			} else {
				return false;
			}
		}

		private Object isMatchingInstance(EEntity instance) throws SdaiException {
			if(((CEntity)instance).testAttributeFast(attribute, selectPath) > 0) {
				Object pathValue = instance.get_object(attribute);
				if(matcherInstances != null) {
					if(matcherInstances.followAttributePath(pathValue,
							matcherInstances.attributePathTop, selectPath) != null) {
						return pathValue;
					}
				}
				return pathValue;
			}
			return null;
		}

		public int hashCode() {
			return instances.hashCode() ^ attribute.hashCode();
		}

		protected Set newEntrySet() {
			return new AbstractSet() {
					public Iterator iterator() {
						return new AEntityAttrIterator();
					}

					public int size() {
						return AEntityAttrMap.this.size();
					}

				};
		}

		class AEntityAttrIterator extends AEntityIterator {
			Entry currEntry;

			protected AEntityAttrIterator() {
			}

			// Implementation of java.util.Iterator

			/**
			 * Describe <code>next</code> method here.
			 *
			 * @return an <code>Object</code> value
			 */
			public Object next() {
				if(hasNextResult < 0) {
					hasNext();
				}
				hasNextResult = -1;
				if(currEntry != null) {
					return currEntry;
				} else {
					throw new IllegalStateException();
				}
			}

			/**
			 * Describe <code>hasNext</code> method here.
			 *
			 * @return a <code>boolean</code> value
			 */
			public boolean hasNext() {
				try {
					if(hasNextResult < 0) {
						EEntity currInstance = null;
						Object pathValue = null;
						boolean isNext;
						while((isNext = iterator.next())) {
							currInstance = instances.getCurrentMemberEntity(iterator);
							pathValue = isMatchingInstance(currInstance);
							if(pathValue != null) {
								break;
							}
						}
						hasNextResult = isNext ? 1 : 0;
						if(isNext) {
							currEntry = new AEntityAttrEntry(currInstance,
									inPath ? pathValue : NON_PATH_VALUE);
						} else {
							currEntry = null;
						}
					}
					return hasNextResult > 0;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			}

		}

		protected class AEntityAttrEntry extends AbstractCollection implements Entry {
			Object keyInstance;
			Object value;
			int size;

			AEntityAttrEntry(Object keyInstance, Object value) {
				this.keyInstance = keyInstance;
				this.value = value;
				size = -1;
			}

			// Implementation of Map.Entry

			public Object getKey() {
				return keyInstance;
			}

			public Object getValue() {
				if(value instanceof Aggregate) {
					return this;
				} else {
					return value;
				}
			}

			public Object setValue(Object value) {
				throw new UnsupportedOperationException();
			}

			public boolean equals(Object obj) {
				if(this == obj) {
					return true;
				}
				if(obj instanceof Entry) {
					Entry other = (Entry)obj;
					return keyInstance.equals(other.getKey()) &&
						(getValue() == null ?
						 other.getValue() == null : getValue().equals(other.getValue()));
				} else if(obj instanceof Set) {
					return super.equals(obj);
				} else {
					return false;
				}
			}

			public int hashCode() {
				if(value instanceof Aggregate) {
					return super.hashCode();
				} else {
					return (keyInstance == null ? 0 : keyInstance.hashCode()) ^
						(value == null ? 0 : value.hashCode());
				}
			}

			// Implementation of value Collection

			public Iterator iterator() {
				if(value instanceof Aggregate) {
					try {
						return new ValueIterator();
					} catch(SdaiException e) {
						IllegalStateException wrapper = new IllegalStateException();
						wrapper.initCause(e);
						throw wrapper;
					}
				} else {
					throw new IllegalStateException();
				}
			}

			// Terribly ineffective but should be rarely called
			public int size() {
				if(size < 0) {
					Iterator iter = iterator();
					size = 0;
					while(iter.hasNext()) {
						iter.next();
						size++;
					}
				}
				return size;
			}

			class ValueIterator implements Iterator {
				protected int sdaiIterIdx;
				protected Aggregate[] aggregates;
				protected SdaiIterator[] sdaiIterators;
				protected int hasNextResult;
				protected Object currValue;
				protected EDefined_type[] selectPath;

				protected ValueIterator() throws SdaiException {
					aggregates = new Aggregate[10]; // Max. iterator nesting level
					sdaiIterators = new SdaiIterator[10]; // Max. iterator nesting level
					aggregates[0] = ((Aggregate)value);
					CEntity aggOwningInstance = null;
					SdaiModel origOwningModel = null;
					if(aggregates[0] instanceof CAggregate) {
						CAggregate cAgg = (CAggregate) aggregates[0];
						aggOwningInstance = cAgg.getOwningInstance();
						origOwningModel = aggOwningInstance.owning_model;
					}
					try {
						if(aggOwningInstance != null && origOwningModel == null) {
							aggOwningInstance.owning_model = ((CEntity)attribute).owning_model;
						}
						sdaiIterators[0] = aggregates[0].createIterator();
					} finally {
						if(aggOwningInstance != null) {
							aggOwningInstance.owning_model = origOwningModel;
						}
					}
					sdaiIterIdx = 0;
					hasNextResult = -1;
					currValue = null;
					selectPath = new EDefined_type[20];
				}

				public Object next() {
					if(hasNextResult < 0) {
						hasNext();
					}
					hasNextResult = -1;
					if(currValue != null) {
						return currValue;
					} else {
						throw new IllegalStateException();
					}
				}

				public boolean hasNext() {
					try {
						while(hasNextResult < 0) {
							CEntity aggOwningInstance = null;
							SdaiModel origOwningModel = null;
							if(aggregates[sdaiIterIdx] instanceof CAggregate) {
								CAggregate cAgg = (CAggregate) aggregates[sdaiIterIdx];
								aggOwningInstance = cAgg.getOwningInstance();
								origOwningModel = aggOwningInstance.owning_model;
							}
							try {
								if(aggOwningInstance != null && origOwningModel == null) {
									aggOwningInstance.owning_model = ((CEntity)attribute).owning_model;
								}
								while(sdaiIterators[sdaiIterIdx].next() && hasNextResult < 0) {
									if(aggregates[sdaiIterIdx].testCurrentMember(sdaiIterators[sdaiIterIdx],
																			selectPath) >= 0) {
										currValue = aggregates[sdaiIterIdx]
											.getCurrentMemberObject(sdaiIterators[sdaiIterIdx]);
										hasNextResult = 1;
									}
								}
							} finally {
								if(aggOwningInstance != null) {
									aggOwningInstance.owning_model = origOwningModel;
								}
								aggOwningInstance = null;
								origOwningModel = null;
							}
							if(hasNextResult == 1) {
								if(currValue instanceof Aggregate) {
									aggregates[++sdaiIterIdx] = ((Aggregate)currValue);
									if(aggregates[sdaiIterIdx] instanceof CAggregate) {
										CAggregate cAgg = (CAggregate) aggregates[sdaiIterIdx];
										aggOwningInstance = cAgg.getOwningInstance();
										origOwningModel = aggOwningInstance.owning_model;
									}
									try {
										if(aggOwningInstance != null && origOwningModel == null) {
											aggOwningInstance.owning_model = ((CEntity)attribute).owning_model;
										}
										sdaiIterators[sdaiIterIdx] = aggregates[sdaiIterIdx].createIterator();
									} finally {
										if(aggOwningInstance != null) {
											aggOwningInstance.owning_model = origOwningModel;
										}
										aggOwningInstance = null;
										origOwningModel = null;
									}
									hasNextResult = -1;
								}
							} else if(sdaiIterIdx > 0) {
								--sdaiIterIdx;
							} else {
								currValue = null;
								hasNextResult = 0;
							}
						}
						return hasNextResult > 0;
					} catch(SdaiException e) {
						IllegalStateException wrapper = new IllegalStateException();
						wrapper.initCause(e);
						throw wrapper;
					}
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			}
		}
	}


} // CMappingExplicit_attribute
