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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jsdai.dictionary.ADefined_type;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.MappingContext.ImmutableArrayMap;
import jsdai.lang.MappingContext.ImmutableArraySet;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This interface defines the AIM2ARM conversion behavior. It is meant for
 * mapping operations internal use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @since 3.6.0
 */

public interface MappingConstraintMatcher {

	/**
	 * The special static value to be used in matcher instances map when the constraint is
	 * used not in a path. It is meant for mapping operations internal use.
	 */
	public static final Object NON_PATH_VALUE = new Object() { public String toString() { return "NON_PATH_VALUE"; } };

	/**
	 * Finds instances when the constraint is used not in a path for a forward direction.
	 * It is meant for mapping operations internal use.
	 *
	 * @param mappingContext The context of this operation
	 * @param instances The input instances to get the result with
	 * @param decCacheUseCnt true if cache use count should be decreased
	 * @return The resulting output instances
	 * @exception SdaiException if an error occurs during method execution or
	 *                          in underlying JSDAI operations
     * @since 4.1.0
	 */
	public MatcherInstances findForward(MappingContext mappingContext,
			MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException;

	/**
	 * Finds instances when the constraint is used not in a path for a backward direction.
	 * It is meant for mapping operations internal use.
	 *
	 * @param mappingContext The context of this operation
	 * @param instances The input instances to get the result with
	 * @param decCacheUseCnt true if cache use count should be decreased
	 * @return The resulting output instances
	 * @exception SdaiException if an error occurs during method execution or
	 *                          in underlying JSDAI operations
     * @since 4.1.0
	 */
	public MatcherInstances findBackward(MappingContext mappingContext,
			MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException;

	/**
	 * Finds instances when the constraint is used in a path for a forward direction.
	 * It is meant for mapping operations internal use.
	 *
	 * @param mappingContext The context of this operation
	 * @param instances The input instances to get the result with
	 * @param decCacheUseCnt true if cache use count should be decreased
	 * @return The resulting output instances
	 * @exception SdaiException if an error occurs during method execution or
	 *                          in underlying JSDAI operations
     * @since 4.1.0
	 */
	public MatcherInstances findPathForward(MappingContext mappingContext,
			MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException;

	/**
	 * Finds dependent instances when the constraint is used in a path
	 * for a forward direction. It is meant for mapping operations internal use.
	 *
	 * @return The dependent instances
	 * @exception SdaiException if an error occurs during method execution or
	 *                          in underlying JSDAI operations
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException;

	/**
	 * Finds instances when the constraint is used in a path for a backward direction.
	 * It is meant for mapping operations internal use.
	 *
	 * @param mappingContext The context of this operation
	 * @param instances The input instances to get the result with
	 * @param decCacheUseCnt true if cache use count should be decreased
	 * @return The resulting output instances
	 * @exception SdaiException if an error occurs during method execution or
	 *                          in underlying JSDAI operations
     * @since 4.1.0
	 */
	public MatcherInstances findPathBackward(MappingContext mappingContext,
			MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException;

	/**
	 * Identifies matcher instances used in attribute and entity mappings.
	 * This interface is meant for mapping operations internal use.
	 *
     * @since 3.6.0
	 */
	static interface MappingMatcherInstances {
		/**
		 * Returns matcher instances.
		 * It is meant for mapping operations internal use.
		 * @return the matcher instances
		 */
		public MatcherInstances getMatcherInstances();

		/**
		 * Returns concrete matcher instances.
		 * It is meant for mapping operations internal use.
		 * @return the concrete matcher instances
		 */
		public MatcherInstances getConcreteInstances();
	}

	/**
	 * This class defines entity instances for use in <code>MappingConstraintMatcher</code>
	 * It is meant for mapping operations internal use.
	 *
     * @since 3.6.0
	 */
	static class MatcherInstances implements MappingMatcherInstances {
		protected final static int STATUS_NON_PATH_MASK = 3;
		protected final static int STATUS_PATH_MASK = 12;
		protected final static int STATUS_FORWARD = 1;
		protected final static int STATUS_BACKWARD = 2;
		protected final static int STATUS_PATH_FORWARD = 4;
		protected final static int STATUS_PATH_BACKWARD = 8;

		protected final int status;
		protected final Map instances;

		int[] members;
		ADefined_type[] dataTypes;
		int attributePathTop;

		MatcherInstances(Map instances, int status) {
			this.instances = instances;
			this.status = status;
			members = null;
			dataTypes = null;
			attributePathTop = -1;
		}

		MatcherInstances(AEntity instances, int status) {
			this(new AEntityMap(instances, (status & STATUS_PATH_MASK) != 0), status);
		}

		protected MatcherInstances dup(Map instances, int status, boolean dupAttributePath) {
			MatcherInstances newMatcherInstances =
				new MatcherInstances(instances != null ? instances : this.instances, status);
			if(dupAttributePath && attributePathTop >= 0) {
				newMatcherInstances.attributePathTop = attributePathTop;
				newMatcherInstances.initPath();
				System.arraycopy(dataTypes, 0, newMatcherInstances.dataTypes, 0, attributePathTop + 1);
				System.arraycopy(members, 0, newMatcherInstances.members, 0, attributePathTop + 1);
			}
			return newMatcherInstances;
		}

		protected final MatcherInstances dup(Map instances, int status) {
			return dup(instances, status, true);
		}

		protected final MatcherInstances dup() {
			return dup(null, status, true);
		}

		protected final MatcherInstances dup(boolean dupAttributePath) {
			return dup(null, status, dupAttributePath);
		}

		protected MatcherInstances intersect(MatcherInstances other) throws SdaiException {
			if(other.prefersMaster()) {
				return other.doIntersect(this);
			} else {
				return doIntersect(other);
			}
		}

		protected MatcherInstances doIntersect(MatcherInstances other) throws SdaiException {
			List newKeys = new ArrayList();
			List newValue = new ArrayList();
			Map otherInstances = other.getInstanceMap();
			int newStatus;
			if((status & STATUS_NON_PATH_MASK) != 0 && (other.status & STATUS_NON_PATH_MASK) != 0) {
				for(Iterator i = otherInstances.keySet().iterator(); i.hasNext(); ) {
					Object o = i.next();
					if(instances.containsKey(o)) {
						newKeys.add(o);
						newValue.add(NON_PATH_VALUE);
					}
				}
				newStatus = status;
			} else {
				newStatus = (status & STATUS_PATH_MASK) != 0 ? status : other.status;
				for(Iterator i = otherInstances.entrySet().iterator(); i.hasNext(); ) {
					Map.Entry entry = (Map.Entry)i.next();
					Object key = entry.getKey();
					Object otherValue = instances.get(key);
					if(otherValue != null) {
						Object value = entry.getValue();
						if(value == NON_PATH_VALUE) {
							value = otherValue;
						} else if(otherValue != NON_PATH_VALUE) {
							if(value instanceof Collection) {
								Collection valueCollection = (Collection)value;
								if(otherValue instanceof Collection) {
									Set newValueSet = new HashSet(valueCollection);
									newValueSet.addAll((Collection)otherValue);
									value = new ImmutableArraySet(newValueSet);
								} else if(otherValue != NON_PATH_VALUE
										&& !valueCollection.contains(otherValue)) {
									Object[] newValueArray = valueCollection.toArray(
											new Object[valueCollection.size() + 1]);
									newValueArray[newValueArray.length - 1] = otherValue;
									value = new ImmutableArraySet(newValueArray);
								}
							} else if(otherValue instanceof Collection) {
								Collection otherValueCollection = (Collection)otherValue;
								if(otherValueCollection.contains(value)) {
									value = otherValueCollection;
								} else {
									Object[] newValueArray = otherValueCollection.toArray(
											new Object[otherValueCollection.size() + 1]);
									newValueArray[newValueArray.length - 1] = value;
									value = new ImmutableArraySet(newValueArray);
								}
							} else if(!value.equals(otherValue)) {
								Object[] newValueArray = { value, otherValue };
								value = new ImmutableArraySet(newValueArray);
							}
						}
						newKeys.add(key);
						newValue.add(value);
					}
				}
			}
			Map newInstances = new ImmutableArrayMap(newKeys, newValue);
			return new MatcherInstances(newInstances, newStatus);
		}

		protected MatcherInstances union(MatcherInstances other) throws SdaiException {
			if(other.prefersMaster()) {
				return other.doUnion(this);
			} else {
				return doUnion(other);
			}
		}

		protected MatcherInstances doUnion(MatcherInstances other) throws SdaiException {
			Map newInstances = new HashMap(instances);
			int newStatus;
			if((status & STATUS_NON_PATH_MASK) != 0 && (other.status & STATUS_NON_PATH_MASK) != 0) {
				newInstances.putAll(other.getInstanceMap());
				newStatus = status;
			} else {
				newStatus = (status & STATUS_PATH_MASK) != 0 ? status : other.status;
				for(Iterator i = other.getInstanceMap().entrySet().iterator(); i.hasNext(); ) {
					Map.Entry entry = (Map.Entry)i.next();
					Object key = entry.getKey();
					Object otherValue = newInstances.get(key);
					if(otherValue != null) {
						Object value = entry.getValue();
						if(value == NON_PATH_VALUE) {
							value = otherValue;
						} else if(otherValue != NON_PATH_VALUE) {
							if(value instanceof Collection) {
								Collection valueCollection = (Collection)value;
								if(otherValue instanceof Collection) {
									Set newValueSet = new HashSet((Collection)value);
									newValueSet.addAll((Collection)otherValue);
									value = new ImmutableArraySet(newValueSet);
								} else if(!valueCollection.contains(otherValue)) {
									Object[] newValueArray = valueCollection.toArray(
											new Object[valueCollection.size() + 1]);
									newValueArray[newValueArray.length - 1] = otherValue;
									value = new ImmutableArraySet(newValueArray);
								}
							} else if(otherValue instanceof Collection) {
								Collection otherValueCollection = (Collection)otherValue;
								if(otherValueCollection.contains(value)) {
									value = otherValueCollection;
								} else {
									Object[] newValueArray = otherValueCollection.toArray(
											new Object[otherValueCollection.size() + 1]);
									newValueArray[newValueArray.length - 1] = value;
									value = new ImmutableArraySet(newValueArray);
								}
							} else if(!value.equals(otherValue)) {
								Object[] newValueArray = { value, otherValue };
								value = new ImmutableArraySet(newValueArray);
							}
						}
						newInstances.put(key, value);
					} else {
						newInstances.put(key, entry.getValue());
					}
				}
			}
			return new MatcherInstances(new ImmutableArrayMap(newInstances), newStatus);
		}

		protected MatcherInstances difference(MatcherInstances other) throws SdaiException {
			Map otherInstances = other.getInstanceMap();
			if(otherInstances.isEmpty() || instances.isEmpty()) {
				return this;
			} else {
				List newKeys = null;
				List newValue = null;
				int sameCount = 0;
				for(Iterator i = instances.entrySet().iterator(); i.hasNext(); ) {
					Map.Entry entry = (Map.Entry)i.next();
					Object key = entry.getKey();
					if(otherInstances.containsKey(key)) {
						newKeys = new ArrayList();
						newValue = new ArrayList();
						// Add previously possitively matched entries
						for(i = instances.entrySet().iterator(); --sameCount >= 0; ) {
							entry = (Map.Entry)i.next();
							newKeys.add(entry.getKey());
							newValue.add(entry.getValue());
						}
						// Skip entry that was just failed the check in outer loop
						i.next();
						// Go through the rest of entries
						while(i.hasNext()) {
							entry = (Map.Entry)i.next();
							key = entry.getKey();
							if(!otherInstances.containsKey(key)) {
								newKeys.add(key);
								newValue.add(entry.getValue());
							}
						}
						break;
					}
					sameCount++;
				}
				if(newKeys != null) {
					Map newInstances = new ImmutableArrayMap(newKeys, newValue);
					return new MatcherInstances(newInstances, status);
				} else {
					return this;
				}
			}
		}

		protected MatcherInstances intersectValues(MatcherInstances other) throws SdaiException {
			List newKeys = new ArrayList();
			List newValue = new ArrayList();
			if(other != null) {
				Collection otherValues = other.getInstanceMap().values();
				if(!otherValues.isEmpty()) {
					Set otherValueSet = new HashSet();
					for(Iterator i = otherValues.iterator(); i.hasNext(); ) {
						Object otherValue = i.next();
						if(otherValue instanceof Collection) {
							otherValueSet.addAll((Collection)otherValue);
						} else {
							otherValueSet.add(otherValue);
						}
					}
					for(Iterator i = getInstanceMap().entrySet().iterator(); i.hasNext(); ) {
						Map.Entry entry = (Map.Entry)i.next();
						Object key = entry.getKey();
						if(otherValueSet.contains(key)) {
							newKeys.add(key);
							newValue.add(entry.getValue());
						}
					}
				}
			}
			Map newInstances = new ImmutableArrayMap(newKeys, newValue);
			return new MatcherInstances(newInstances, status);
		}

		protected MatcherInstances extractType(MappingContext mappingContext,
											   EEntity_definition type) throws SdaiException {
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			Map instances = getInstanceMap();
			if(instances.equals(workingModels.getInstances())) {
				if(type.getComplex(null)) {
					// Special processing for complex types
					EEntity_definition supertype =
						(EEntity_definition) type.getGeneric_supertypes(null).getByIndexEntity(1);
					AEntity supertypeInstances = workingModels.getInstances(supertype);
					int matchCount = 0;
					for(SdaiIterator i = supertypeInstances.createIterator(); i.next(); ) {
						EEntity instance = supertypeInstances.getCurrentMemberEntity(i);
						if(!instance.isKindOf(type)) {
							// Create new map if needed only
							List newKeys = new ArrayList();
							List newValue = new ArrayList();
							SdaiIterator j = supertypeInstances.createIterator();
							for(int k = 0; k < matchCount && j.next(); k++) {
								instance = supertypeInstances.getCurrentMemberEntity(j);
								newKeys.add(instance);
								newValue.add((status & STATUS_PATH_MASK) != 0 ?
										instance : NON_PATH_VALUE);
							}
							while(j.next()) {
								instance = supertypeInstances.getCurrentMemberEntity(j);
								if(instance.isKindOf(type)) {
									newKeys.add(instance);
									newValue.add((status & STATUS_PATH_MASK) != 0 ?
											instance : NON_PATH_VALUE);
								}
							}
							Map newInstances = new ImmutableArrayMap(newKeys, newValue);
							return new MatcherInstances(newInstances, status);
						}
						matchCount++;
					}
					return new MatcherInstances(new AEntityMap(supertypeInstances,
							(status & STATUS_PATH_MASK) != 0), status);
				} else {
					return new MatcherInstances(new AEntityMap(workingModels.getInstances(type),
							(status & STATUS_PATH_MASK) != 0), status);
				}
			} else {
				Iterator inInstInter = instances.entrySet().iterator();
				int sameCount = 0;
				while(inInstInter.hasNext()) {
					Map.Entry entry = (Map.Entry)inInstInter.next();
					EEntity instance = (EEntity)entry.getKey();
					if(!instance.isKindOf(type)) {
						//Avoiding new instance set creation failed. This should happen rarely
						List newKeys = new ArrayList();
						List newValue = new ArrayList();
						inInstInter = instances.entrySet().iterator();
						for(int i = 0 ; i < sameCount; i++) {
							entry = (Map.Entry)inInstInter.next();
							newKeys.add(entry.getKey());
							newValue.add(entry.getValue());
						}
						while(inInstInter.hasNext()) {
							entry = (Map.Entry)inInstInter.next();
							instance = (EEntity)entry.getKey();
							if(instance.isKindOf(type)) {
								newKeys.add(instance);
								newValue.add(entry.getValue());
							}
						}
						Map newInstances = new ImmutableArrayMap(newKeys, newValue);
						return new MatcherInstances(newInstances, status);
					}
					sameCount++;
				}
				return this;
			}
		}

		protected MatcherInstances extractExactType(MappingContext mappingContext,
													EEntity_definition type) throws SdaiException {
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			Map instances = getInstanceMap();
			if(instances.equals(workingModels.getInstances())) {
				return new MatcherInstances(new AEntityMap(workingModels.getExactInstances(type), (status & STATUS_PATH_MASK) != 0),
											status);
			} else {
				Iterator inInstInter = instances.entrySet().iterator();
				int sameCount = 0;
				while(inInstInter.hasNext()) {
					Map.Entry entry = (Map.Entry)inInstInter.next();
					EEntity instance = (EEntity)entry.getKey();
					if(!instance.isInstanceOf(type)) {
						//Avoiding new instance set creation failed. This should happen rarely
						List newKeys = new ArrayList();
						List newValue = new ArrayList();
						inInstInter = instances.entrySet().iterator();
						for(int i = 0 ; i < sameCount; i++) {
							entry = (Map.Entry)inInstInter.next();
							newKeys.add(entry.getKey());
							newValue.add(entry.getValue());
						}
						while(inInstInter.hasNext()) {
							entry = (Map.Entry)inInstInter.next();
							instance = (EEntity)entry.getKey();
							if(instance.isInstanceOf(type)) {
								newKeys.add(instance);
								newValue.add(entry.getValue());
							}
						}
						Map newInstances = new ImmutableArrayMap(newKeys, newValue);
						return new MatcherInstances(newInstances, status);
					}
					sameCount++;
				}
				return this;
			}
		}

		protected Map getInstanceMap() throws SdaiException {
			return instances;
		}

		protected boolean isEmpty() throws SdaiException {
			return instances.isEmpty();
		}

		protected boolean prefersMaster() {
			return (instances instanceof AEntityMap);
		}

		protected boolean equalsAttributePath(MatcherInstances other) {
			if(attributePathTop == other.attributePathTop) {
				for(int i = 0; i <= attributePathTop; i++) {
					if(dataTypes[i] != other.dataTypes[i]
					   || (dataTypes[i] != null && members[i] != other.members[i])) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Compares this matcher instances to other matcher instances.
		 *
		 * @param obj an object to compare
		 * @return true if object to compare to is <code>MatcherInstances</code> and
		 *         contains the same instances and the same auxiliary information.
		 */
		public boolean equals(Object obj) {
			if(obj instanceof MatcherInstances) {
				MatcherInstances other = (MatcherInstances)obj;
				return new EqualsBuilder()
					.append(getClass(), other.getClass())
					.append(instances, other.instances)
					.append(status, other.status)
					.isEquals() &&
					equalsAttributePath(other);
			} else {
				return false;
			}
		}

		/**
		 * Returns the hash code value.
		 *
		 * @return the result of expression <code>instances.hashCode() ^ status ^ attributePathTop</code>
		 */
		public int hashCode() {
			return instances.hashCode() ^ status ^ attributePathTop;
		}

		// Path operations
		private void initPath() {
			if(members == null) {
				members = new int[30];
				dataTypes = new ADefined_type[30];
// 				attributePathTop = -1;
			}
		}

		final void attributePathClear() {
			attributePathTop = -1;
		}

		final void attributePathPush(int member) {
			initPath();
			members[++attributePathTop] = member;
			dataTypes[attributePathTop] = null;
		}

		final void attributePathPush(ADefined_type dataType) {
			initPath();
			dataTypes[++attributePathTop] = dataType;
		}

		final void attributePathReversePush(int member) {
			initPath();
			System.arraycopy(dataTypes, 0, dataTypes, 1, ++attributePathTop);
			System.arraycopy(members, 0, members, 1, attributePathTop);
			members[0] = member;
			dataTypes[0] = null;
		}

		final void attributePathReversePush(ADefined_type dataType) {
			initPath();
			System.arraycopy(dataTypes, 0, dataTypes, 1, ++attributePathTop);
			System.arraycopy(members, 0, members, 1, attributePathTop);
			dataTypes[0] = dataType;
		}

		final void attributePathPop() {
			if(attributePathTop >= 0) {
				dataTypes[attributePathTop--] = null;
			}
		}

		final boolean isAttributeMemberAt(int position) {
			return dataTypes[position] == null;
		}

		final int getAttributeMemberAt(int position) {
			return members[position];
		}

		final ADefined_type getAttributeDataTypeAt(int position) {
			return dataTypes[position];
		}

		/**
		 * Extra requirement calling this method:
		 * selectPath field has to be filled in making a call like this:<br/>
		 * <code>instance.testAttribute(attribute, selectPath);</code>
		 * @param pathValue original attribute value obtained by
		 * <code>instance.get_object(attribute);</code>
		 * @param pathPosition path walking starting position
		 * which should always be 0 except when called recursively
		 * @return resulting attribute value as <code>Object</code>
		 * @exception SdaiException if an error occurs
		 */
		final Object followAttributePath(Object pathValue, int pathPosition,
										 EDefined_type[] selectPath) throws SdaiException {
			for(; pathPosition >= 0; pathPosition--) {
				if(isAttributeMemberAt(pathPosition)) {
					int member = getAttributeMemberAt(pathPosition);
					if(!(pathValue instanceof Aggregate)) {
						pathValue = null;
						break;
					}
					Aggregate pathAggregate = (Aggregate)pathValue;
					if(member < 0) {
						List pathValues = new ArrayList();
						SdaiIterator pathAggIter = pathAggregate.createIterator();
						while(pathAggIter.next()) {
							pathAggregate.testCurrentMember(pathAggIter, selectPath);
							pathValue =
								followAttributePath(pathAggregate.getCurrentMemberObject(pathAggIter),
													pathPosition - 1, selectPath);
							if(pathValue instanceof EEntity) {
								pathValues.add(pathValue);
							}
						}
						pathValue = new ImmutableArraySet(pathValues);
						break;
					} else {
						if(pathAggregate.getMemberCount() < member) {
							pathValue = null;
							break;
						}
						pathAggregate.testByIndex(member, selectPath);
						pathValue = pathAggregate.getByIndexObject(member);
					}
				} else {
					ADefined_type dataType = getAttributeDataTypeAt(pathPosition);
					SdaiIterator dataTypeIter = dataType.createIterator();
					int selPathIdx = 0;
					for(; selectPath[selPathIdx] != null && dataTypeIter.next();
						selPathIdx++) {
						EDefined_type dataTypeElem = dataType.getCurrentMember(dataTypeIter);
						if(dataTypeElem != selectPath[selPathIdx]) {
							break;
						}
					}
					if(selectPath[selPathIdx] != null || selPathIdx != dataType.getMemberCount()) {
						pathValue = null;
						break;
					}
				}
			}
			return pathValue;
		}

		public MatcherInstances getMatcherInstances() {
			return this;
		}

		public MatcherInstances getConcreteInstances() {
			return null;
		}

	}

	/**
	 * This class holds pre-processed instances for attribute and entity mappings.
	 * It is meant for mapping operations internal use.
	 *
     * @since 3.6.0
	 */
	static class MappingPreInstances implements MappingMatcherInstances {
		private MatcherInstances instances;
		private MatcherInstances concreteInstances;

		MappingPreInstances(MatcherInstances instances, MatcherInstances concreteInstances) {
			this.instances = instances;
			this.concreteInstances = concreteInstances;
		}

		public MatcherInstances getMatcherInstances() {
			return instances;
		}

		public MatcherInstances getConcreteInstances() {
			return concreteInstances;
		}
	}

	/**
	 * This is <code>Set</code> interface wrapper for <code>AEntity</code> aggregate.
	 * It is meant for mapping operations internal use.
	 *
     * @since 3.6.0
	 */
	static class AEntitySet extends AbstractSet {
		protected AEntity instances;

		AEntitySet(AEntity instances) {
			this.instances = instances;
		}

		public Iterator iterator() {
			return new AEntityIterator();
		}

		public int size() {
			try {
				return instances.getMemberCount();
			} catch(SdaiException e) {
				IllegalStateException wrapper = new IllegalStateException();
				wrapper.initCause(e);
				throw wrapper;
			}
		}

		public boolean contains(Object key) {
			if(key instanceof EEntity) {
				try {
					return instances.isMember((EEntity)key);
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
			return instances;
		}

		public boolean equals(Object obj) {
			if(obj instanceof AEntitySet) {
				obj = ((AEntitySet)obj).getEqualsObject();
			}
			return instances.equals(obj);
		}

		public int hashCode() {
			return instances.hashCode();
		}

		protected class AEntityIterator implements Iterator {
			protected SdaiIterator iterator;
			protected int hasNextResult;

			AEntityIterator() {
				try {
					iterator = instances.createIterator();
					hasNextResult = -1;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			}

			// Implementation of java.util.Iterator

			/**
			 * Describe <code>next</code> method here.
			 *
			 * @return an <code>Object</code> value
			 */
			public Object next() {
				try {
					if(hasNextResult < 0) {
						iterator.next();
					}
					hasNextResult = -1;
					return instances.getCurrentMemberEntity(iterator);
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
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
						hasNextResult = iterator.next() ? 1 : 0;
					}
					return hasNextResult > 0;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			}

			/**
			 * Describe <code>remove</code> method here.
			 *
			 */
			public void remove() {
				throw new UnsupportedOperationException();
			}

		}
	}

	/**
	 * This is <code>Map</code> interface wrapper for <code>AEntity</code> aggregate.
	 * It is meant for mapping operations internal use.
	 *
     * @since 3.6.0
	 */
	static class AEntityMap extends AbstractMap {
		protected AEntity instances;
		private Set entrySet;
		protected boolean inPath;

		AEntityMap(AEntity instances, boolean inPath) {
			this.instances = instances;
			this.inPath = inPath;
			entrySet = newEntrySet();
		}

		public Set entrySet() {
			return entrySet;
		}

		public int size() {
			try {
				return instances.getMemberCount();
			} catch(SdaiException e) {
				IllegalStateException wrapper = new IllegalStateException();
				wrapper.initCause(e);
				throw wrapper;
			}
		}

		public boolean containsKey(Object key) {
			if(key instanceof EEntity) {
				try {
					return instances.isMember((EEntity)key);
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
					return instances.isMember((EEntity)key) ? (inPath ? key : NON_PATH_VALUE) : null;
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
			return instances;
		}

		public boolean equals(Object obj) {
			if(obj instanceof AEntityMap) {
				obj = ((AEntityMap)obj).getEqualsObject();
			}
			return instances.equals(obj);
		}

		public int hashCode() {
			return instances.hashCode();
		}

		protected Set newEntrySet() {
			return new AbstractSet() {
					public Iterator iterator() {
						return new AEntityIterator();
					}

					public int size() {
						return AEntityMap.this.size();
					}

				};
		}

		protected class AEntityIterator implements Iterator {
			protected SdaiIterator iterator;
			protected int hasNextResult;

			AEntityIterator() {
				try {
					iterator = instances.createIterator();
					hasNextResult = -1;
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			}

			// Implementation of java.util.Iterator
			public Object next() {
				try {
					if(hasNextResult < 0) {
						iterator.next();
					}
					hasNextResult = -1;
					return inPath
						? (Object)new AEntityEntryInPath(instances.getCurrentMemberEntity(iterator))
						: (Object)new AEntityEntryNotInPath(instances.getCurrentMemberEntity(iterator));
				} catch(SdaiException e) {
					IllegalStateException wrapper = new IllegalStateException();
					wrapper.initCause(e);
					throw wrapper;
				}
			}

			public boolean hasNext() {
				try {
					if(hasNextResult < 0) {
						hasNextResult = iterator.next() ? 1 : 0;
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

		protected static abstract class AEntityEntryBase implements Entry {
			Object keyInstance;

			AEntityEntryBase(Object keyInstance) {
				this.keyInstance = keyInstance;
			}

			public Object getKey() {
				return keyInstance;
			}

			public Object setValue(Object value) {
				throw new UnsupportedOperationException();
			}

			public int hashCode() {
				return keyInstance == null ? 0 : keyInstance.hashCode();
			}
		}

		protected static class AEntityEntryInPath extends AEntityEntryBase {

			AEntityEntryInPath(Object keyInstance) {
				super(keyInstance);
			}

			public Object getValue() {
				return keyInstance;
			}

			public boolean equals(Object obj) {
				if(obj instanceof Entry) {
					Entry other = (Entry)obj;
					return keyInstance.equals(other.getKey()) && keyInstance.equals(other.getValue());
				} else {
					return false;
				}
			}

		}

		protected static class AEntityEntryNotInPath extends AEntityEntryBase {

			AEntityEntryNotInPath(Object keyInstance) {
				super(keyInstance);
			}

			public Object getValue() {
				return NON_PATH_VALUE;
			}

			public boolean equals(Object obj) {
				if(obj instanceof Entry) {
					Entry other = (Entry)obj;
					return keyInstance.equals(other.getKey()) && other.getValue() == NON_PATH_VALUE;
				} else {
					return false;
				}
			}

		}
	}

	/**
	 * This is <code>Map</code> interface wrapper for <code>Set</code> as keys
	 * and fixed value as values.
	 * It is meant for mapping operations internal use.
	 *
	 */
	static class FixedValueMap implements Map {
		final Set origKeySet;
		final Object fixedValue;

		FixedValueMap(Set origKeySet, Object fixedValue) {
			this.origKeySet = origKeySet;
			this.fixedValue = fixedValue;
		}

		public int size() {
			return origKeySet.size();
		}

		public boolean isEmpty() {
			return origKeySet.isEmpty();
		}

		public boolean containsKey(Object key) {
			return origKeySet.contains(key);
		}

		public boolean containsValue(Object value) {
			return fixedValue == null ? value == null : fixedValue.equals(value);
		}

		public Object get(Object key) {
			return origKeySet.contains(key) ? fixedValue : null;
		}

		public Object put(Object key, Object value) {
			throw new UnsupportedOperationException();
		}

		public Object remove(Object key) {
			throw new UnsupportedOperationException();
		}

		public void putAll(Map t) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public Set keySet() {
			return origKeySet;
		}

		public Collection values() {
			return Collections.nCopies(origKeySet.size(), fixedValue);
		}

		public Set entrySet() {
			return new FixedValueEntrySet(origKeySet, fixedValue);
		}

		public boolean equals(Object o) {
			if(o == this)
				return true;

			if(!(o instanceof Map))
				return false;

			Map t = (Map)o;
			if (t.size() != origKeySet.size())
				return false;

			Iterator i = origKeySet.iterator();
			while(i.hasNext()) {
				Object key = i.next();
				if(fixedValue == null) {
					if(!(t.containsKey(key) && t.get(key) == null)) {
						return false;
					}
				} else {
					if(!fixedValue.equals(t.get(key))) {
						return false;
					}
				}
			}
			return true;
		}

		public int hashCode() {
			int h = 0;
			Iterator i = origKeySet.iterator();
			int valueHashCode = fixedValue.hashCode();
			while(i.hasNext()) {
				int keyHashCode = i.next().hashCode();
				h += keyHashCode ^ valueHashCode;
			}
			return h;
		}

		public String toString() {
			return "Value: " + fixedValue + ", " + origKeySet.toString();
		}

		static private class FixedValueEntrySet extends AbstractSet {
			final Set origKeySet;
			final Object fixedValue;

			FixedValueEntrySet(Set origKeySet, Object fixedValue) {
				this.origKeySet = origKeySet;
				this.fixedValue = fixedValue;
			}

			public int size() {
				return origKeySet.size();
			}

			public boolean isEmpty() {
				return origKeySet.isEmpty();
			}

			public boolean contains(Object key) {
				return key instanceof Entry ? origKeySet.contains(((Entry)key).getKey()) : false;
			}

			public Iterator iterator() {
				return new FixedValueEntryIter(origKeySet.iterator(), fixedValue);
			}

		}

		static private class FixedValueEntryIter implements Iterator {
			final Iterator origIterator;
			final Object fixedValue;

			FixedValueEntryIter(Iterator origIterator, Object fixedValue) {
				this.origIterator = origIterator;
				this.fixedValue = fixedValue;
			}

			public boolean hasNext() {
				return origIterator.hasNext();
			}

			public Object next() {
				return new FixedValueEntry(origIterator.next(), fixedValue);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		}

		static private class FixedValueEntry implements Entry {
			final Object origKey;
			final Object fixedValue;

			FixedValueEntry(Object origKey, Object fixedValue) {
				this.origKey = origKey;
				this.fixedValue = fixedValue;
			}

			public Object getKey() {
				return origKey;
			}

			public Object getValue() {
				return fixedValue;
			}

			public Object setValue(Object value) {
				throw new UnsupportedOperationException();
			}

			public boolean equals(Object o) {
				if(!(o instanceof Entry)) {
					return false;
				}

				Entry e = (Entry)o;
				return
					(origKey == null ? e.getKey() == null : origKey.equals(e.getKey())) &&
					(fixedValue == null ? e.getValue() == null : fixedValue.equals(e.getValue()));
			}

			public int hashCode() {
				int keyHashCode = origKey == null ? 0 : origKey.hashCode();
				return keyHashCode ^ fixedValue.hashCode();
			}

			public String toString() {
				return "Value: " + fixedValue + ", " + origKey.toString();
			}
		}
	}

} // MappingConstraintMatcher
