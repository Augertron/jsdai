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

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.MappingConstraintMatcher.MappingMatcherInstances;
import jsdai.lang.MappingConstraintMatcher.MatcherInstances;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EAttribute_mapping_value;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.SMapping;

import java.util.List;

/**
 *
 * This class is mapping context for AIM2ARM conversion method <code>convertMapping</code>.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @see SdaiSession#convertMapping
 * @since 3.6.0
 */

public final class MappingContext {
	private static final MappingPopulationCreator EMPTY_CREATOR = new MappingPopulationCreator() {
			public void createSourceInstance(EEntity_mapping type,
											 Collection targetInstances) throws SdaiException {
			}

			public void setSourceAttributeValues(EEntity_mapping type, EEntity targetInstance,
												 Map sourceValues) throws SdaiException {
			}

			public Set preprocessTargetInstances(EEntity_mapping type) throws SdaiException {
				return null;
			}

		};

	SdaiContext context;
	MappingPopulationCreator creator;
	boolean interleavedCreation;
	final EDefined_type[] selectPath;
	boolean preprocessInstances;
	boolean collectingStrongAttributes;
	final Map mappedInstances;
	final Map matcherInstancesCache;
	final List mappingList;
	private final ASdaiModel mappingSchemaModels;

	MatcherInstances[] allInstanceArray;


	/**
	 * Creates a new mapping context with provided mapping population creator.
	 *
	 * @param context context of the operations as <code>SdaiContext</code>.
	 *                The following fields of the context have to be set:
	 *                <code>domain</code>, <code>mappingDomain</code>.
	 * @param creator the mapping population creator to be used during AIM2ARM conversion
	 * @exception SdaiException if underlying JSDAI exception occurs (since JSDAI 4.1.2)
	 * @see MappingPopulationCreator
	 */
	public MappingContext(SdaiContext context,MappingPopulationCreator creator)
	throws SdaiException {
		this.context = context;
		this.creator = creator;
		interleavedCreation = true;
		selectPath = new EDefined_type[20];
		allInstanceArray = new MatcherInstances[6];
		preprocessInstances = false;
		collectingStrongAttributes = false;
		mappedInstances = new WeakHashMap();
		matcherInstancesCache = new HashMap();
		mappingList = new ArrayList();
		mappingSchemaModels = new ASdaiModel();
		ESchema_definition mappingSchema =
			SdaiSession.getSession().getSchemaDefinition(SMapping.class);
		for(SdaiIterator i = context.mappingDomain.createIterator(); i.next(); ) {
			SdaiModel model = context.mappingDomain.getCurrentMember(i);
			if(model.getUnderlyingSchema() == mappingSchema) {
				mappingSchemaModels.addByIndex(1, model);
			}
		}
	}

	/**
	 * Creates a new mapping context with empty mapping population creator.
	 *
	 * @param context context of the operations as <code>SdaiContext</code>.
	 *                The following fields of the context have to be set:
	 *                <code>domain</code>, <code>mappingDomain</code>.
	 * @exception SdaiException if underlying JSDAI exception occurs (since JSDAI 4.1.2)
	 */
	public MappingContext(SdaiContext context) throws SdaiException {
		this(context, EMPTY_CREATOR);
	}

	/**
	 * Returns <code>SdaiContext</code> associated with this <code>MappingContext</code>.
	 *
	 * @return the <code>SdaiContext</code>
	 * @since 4.0.0
	 */
	public final SdaiContext getContext() {
		return context;
	}

	/**
	 * Sets interleaved creation flag for this <code>MappingContext</code>.
	 * Interleaved creation defines the order in which <code>MappingPopulationCreator</code>
	 * methods are called. If interleved creation is <code>true</code> (which is the default
	 * setting) then call to <code>MappingPopulationCreator.createSourceInstance</code>
	 * is immediately followed by calls to <code>MappingPopulationCreator.setSourceAttributeValues</code>
	 * for all instances in targetInstances collection. Otherwise if interleved creation is
	 * <code>false</code> then calls to for all instances to 
	 * <code>MappingPopulationCreator.createSourceInstance</code> are followed by calls to 
	 * <code>MappingPopulationCreator.setSourceAttributeValues</code> for all instances.
	 *
	 * @param interleavedCreation the interleaved creation flag value
	 * @see #getInterleavedCreation
	 * @see MappingPopulationCreator#createSourceInstance
	 * @see MappingPopulationCreator#setSourceAttributeValues
	 * @see SdaiSession#convertMapping
	 * @since 4.0.0
	 */
	public void setInterleavedCreation(boolean interleavedCreation) {
		this.interleavedCreation = interleavedCreation;
	}

	/**
	 * Returns interleaved creation flag for this <code>MappingContext</code>.
	 * Read {@link #setInterleavedCreation} for description of interleaved creation flag.
	 *
	 * @return the interleaved creation flag value
	 * @see #setInterleavedCreation
	 * @since 4.0.0
	 */
	public boolean getInterleavedCreation() {
		return interleavedCreation;
	}

	/**
	 * Creates an association between <code>targetInstances</code> and 
	 * <code>sourceInstance</code>. Later <code>sourceInstance</code>
	 * can be retrieved by providing <code>targetInstances</code> using
	 * <code>getMappedInstance</code> method. Notice that associations are kept
	 * only during <code>convertMapping</code> call and they are cleaned up afterward.
	 *
	 * @param targetInstance the target instance value
	 * @param sourceInstance the source instance value
	 * @return previous value of source instance association if such existed on <code>null</code> otherwise
	 * @see #getMappedInstance
	 * @since 4.0.0
	 */
	public EEntity putMappedInstace(EEntity targetInstance, EEntity sourceInstance) {
		return (EEntity)mappedInstances.put(targetInstance, sourceInstance);
	}

	/**
	 * Returns <code>sourceInstance</code> associated with this <code>targetInstance</code>.
	 * Read {@link #putMappedInstace} for more detailed description.
	 *
	 * @param targetInstance the target instance value
	 * @return the associated source instance if such exists of <code>null</code> otherwise
	 * @since 4.0.0
	 */
	public EEntity getMappedInstance(EEntity targetInstance) {
		return (EEntity)mappedInstances.get(targetInstance);
	}

	MatcherInstances getCacheInstances(EEntity selfInstance,
			MatcherInstances inInstances, boolean decCacheUseCnt) throws SdaiException {
		CachedInstances cachedInstances =
			(CachedInstances) matcherInstancesCache.get(selfInstance);
		if(cachedInstances != null) {
			MatcherInstances outInstances =
				(MatcherInstances)cachedInstances.outInstanceMap
					.get(inInstances);
			if(outInstances != null && decCacheUseCnt) {
				updateCacheGetCount(cachedInstances, 1);
			}
			return outInstances;
		} else {
			return null;
		}
	}

	CachedInstances getCacheInstances(EEntity selfInstance) {
		return (CachedInstances) matcherInstancesCache.get(selfInstance);
	}

	MappingMatcherInstances getCacheMappingInstances(EEntity selfInstance,
			MatcherInstances inInstances) {
		CachedInstances cachedInstances =
			(CachedInstances) matcherInstancesCache.get(selfInstance);
		if(cachedInstances != null) {
			return (MappingMatcherInstances)cachedInstances.outInstanceMap.get(
					inInstances);
		} else {
			return null;
		}
	}

	CachedInstances setCacheInstances(EEntity selfInstance,
			MatcherInstances inInstances, MatcherInstances outInstances,
			boolean decCacheUseCnt) throws SdaiException {
		return setCacheInstances(selfInstance, inInstances, outInstances,
				decCacheUseCnt, null, 1, CACHED_INSTANCES_TYPE_HELPER);
	}

	CachedInstances setCacheInstances(EEntity selfInstance,
			MatcherInstances inInstances, MatcherInstances outInstances,
			boolean decCacheUseCnt, EEntity[] dependentSelfInstances) throws SdaiException {
		CachedInstances cachedInstances =
			setCacheInstances(selfInstance, inInstances, outInstances, decCacheUseCnt,
					dependentSelfInstances, 1, CACHED_INSTANCES_TYPE_HELPER);
		return cachedInstances;
	}

	CachedInstances setUserCacheInstances(EEntity selfInstance, EEntity userInstance,
			MatcherInstances inInstances, MatcherInstances outInstances) throws SdaiException {
		CachedInstances cachedOrigInstances =
			(CachedInstances) matcherInstancesCache.get(userInstance);
		if(cachedOrigInstances != null && cachedOrigInstances.remainingGetCount == 0) {
			return cachedOrigInstances;
		}
		UserCachedInstances cachedInstances =
			(UserCachedInstances)setCacheInstances(userInstance, inInstances, outInstances,
					true, null, 0, USER_CACHED_INSTANCES_TYPE_HELPER);
		cachedInstances.selfInstance = selfInstance;
		decCacheInstancesUseCnt(selfInstance, true);
		return cachedInstances;
	}

	void decCacheInstancesUseCnt(EEntity selfInstance,
			boolean decCacheUseCnt) throws SdaiException {
		CachedInstances cachedInstances =
			(CachedInstances) matcherInstancesCache.get(selfInstance);
		if(cachedInstances != null) {
			updateCacheGetCount(cachedInstances, decCacheUseCnt ? 1 : 0);
		} else {
			cachedInstances =
				setCacheInstances(selfInstance, null, null, decCacheUseCnt,
						null, 1, CACHED_INSTANCES_TYPE_HELPER);
			decCacheUseCnt = cachedInstances.remainingGetCount == 0;
			EEntity[] dependentSelfInstances =
				((MappingConstraintMatcher)selfInstance).findDependentInstances();
			if(dependentSelfInstances != null) {
				for (int i = 0; i < dependentSelfInstances.length; i++) {
					decCacheInstancesUseCnt(dependentSelfInstances[i], decCacheUseCnt);
				}
				if(!decCacheUseCnt) {
					cachedInstances.dependentSelfInstances = dependentSelfInstances;
				}
			}
		}
	}

	CachedInstances setPersistentCacheInstances(EEntity selfInstance,
			MatcherInstances inInstances,
			MappingMatcherInstances outInstances) throws SdaiException {
		return setCacheInstances(selfInstance, inInstances, outInstances,
				false, null, -1, CACHED_INSTANCES_TYPE_HELPER);
	}

	private CachedInstances setCacheInstances(EEntity selfInstance,
			MatcherInstances inInstances, Object outInstances,
			boolean decCacheUseCnt, EEntity[] dependentSelfInstances,
			int type, CachedInstancesTypeHelper typeHelper) throws SdaiException {
		CachedInstances cachedInstances =
			(CachedInstances) matcherInstancesCache.get(selfInstance);
		if(cachedInstances == null) {
			if(type >= 0) {
				int mappingConstraintsCnt = calcConstraintUserCount(selfInstance, type == 0);
				cachedInstances = typeHelper.newInstance(mappingConstraintsCnt);
			} else {
				cachedInstances = typeHelper.newInstance(-1);
			}
			matcherInstancesCache.put(selfInstance, cachedInstances);
		} else {
			CachedInstances typedCachedInstances = typeHelper.asType(cachedInstances);
			if(typedCachedInstances != cachedInstances) {
				cachedInstances = typedCachedInstances;
				matcherInstancesCache.put(selfInstance, cachedInstances);
			}
			if(cachedInstances.remainingGetCount == 0) {
				int mappingConstraintsCnt = calcConstraintUserCount(selfInstance, type == 0);
				cachedInstances.remainingGetCount = mappingConstraintsCnt;
				System.out.println(selfInstance + " c:" + mappingConstraintsCnt + " readded");
			}
		}
		cachedInstances.dependentSelfInstances = dependentSelfInstances;
		if(!decCacheUseCnt || updateCacheGetCount(cachedInstances, type)) {
			if(inInstances != null) {
				cachedInstances.outInstanceMap.put(inInstances, outInstances);
			}
		}
		return cachedInstances;
	}

	private int calcConstraintUserCount(EEntity selfInstance,
			boolean recursive) throws SdaiException {
		AEntity mappingConstraints = new AEntity();
		selfInstance.findEntityInstanceUsers(mappingSchemaModels, mappingConstraints);
		int mappingConstraintsCnt = 0;
		Collection attributeMappings = new ArrayList();
		for(SdaiIterator i = mappingConstraints.createIterator(); i.next(); ) {
			EEntity mappingConstraint = mappingConstraints.getCurrentMemberEntity(i);
			if(mappingConstraint instanceof EAttribute_mapping) {
				EAttribute_mapping attributeMapping =
					(EAttribute_mapping) mappingConstraint;
				if(attributeMapping.testConstraints(null)
						&& attributeMapping.getConstraints(null) == selfInstance
						&& !attributeMappings.contains(attributeMapping) ) {
					attributeMappings.add(attributeMapping);
				} else {
					mappingConstraintsCnt--;
				}
			} else if(recursive
					&& !(mappingConstraint instanceof EEntity_mapping)
					&& !(mappingConstraint instanceof EAttribute_mapping_value)) {
				CachedInstances cachedUserInstances =
					(CachedInstances) matcherInstancesCache.get(mappingConstraint);
				if(cachedUserInstances == null) {
					cachedUserInstances =
						setCacheInstances(mappingConstraint, null, null, true, null,
								0, CACHED_INSTANCES_TYPE_HELPER);
					cachedUserInstances.dependentSelfInstances =
						((MappingConstraintMatcher)mappingConstraint)
						.findDependentInstances();
				}
				if(cachedUserInstances.remainingGetCount == 0) {
					mappingConstraintsCnt--;
				}
			}
		}
		attributeMappings.clear();
		mappingConstraintsCnt =
			mappingConstraints.getMemberCount() + mappingConstraintsCnt;
		return mappingConstraintsCnt;
	}
	
	private boolean updateCacheGetCount(CachedInstances cachedInstances,
			int type) throws SdaiException {
		if(cachedInstances.remainingGetCount >= 0) {
			if(type > 0 && cachedInstances.remainingGetCount > 0) {
				cachedInstances.remainingGetCount--;
			}
			if(cachedInstances.remainingGetCount == 0) {
				cachedInstances.outInstanceMap.clear();
				if(cachedInstances.dependentSelfInstances != null) {
					for (int i = 0; i < cachedInstances.dependentSelfInstances.length; i++) {
						decCacheInstancesUseCnt(cachedInstances.dependentSelfInstances[i],
								true);
					}
				}
				if(cachedInstances instanceof UserCachedInstances) {
					EEntity selfInstance = ((UserCachedInstances)cachedInstances).selfInstance;
					if(selfInstance != null) {
						CachedInstances cachedSelfInstances =
							(CachedInstances) matcherInstancesCache.get(selfInstance);
						if(cachedSelfInstances != null
								&& cachedSelfInstances.remainingGetCount > 0) {
							cachedSelfInstances.outInstanceMap.clear();
						}
					}
				}
				return false;
			}
		}
		return true;
	}
	MatcherInstances newAllInstances(int status) throws SdaiException {
		MatcherInstances allInstances = allInstanceArray[status];
		if(allInstances != null) {
			return allInstances;
		} else {
			ASdaiModel workingModels = context.working_modelAggr;
			allInstances =
				new MatcherInstances(workingModels.getInstances(), status);
			allInstanceArray[status] = allInstances;
			return allInstances;
		}
	}

	void clearCaches() {
		mappedInstances.clear();
		matcherInstancesCache.clear();
		mappingList.clear();
	}

	static class CachedInstances {
		int remainingGetCount;
		final Map/*<MatcherInstances, Object>*/ outInstanceMap;
		EEntity[] dependentSelfInstances;
		Map /*<List<EEntity, MatcherInstances>, MatcherInstances>*/ childrenCachedInstances;

		CachedInstances(int initialGetCount) {
			remainingGetCount = initialGetCount;
			outInstanceMap = new HashMap();
		}

		protected CachedInstances(int remainingGetCount,
				Map/*<MatcherInstances, Object>*/ outInstanceMap,
				EEntity[] dependentSelfInstances,
				Map /*<List<EEntity, MatcherInstances>, MatcherInstances>*/
				childrenCachedInstances) {
			this.remainingGetCount = remainingGetCount;
			this.outInstanceMap = outInstanceMap;
			this.dependentSelfInstances = dependentSelfInstances;
			this.childrenCachedInstances = childrenCachedInstances;
		}

		void addChildCacheInstances(EEntity childInstance,
			MatcherInstances childInInstances,
			MatcherInstances childOutInstances) {
			if(childrenCachedInstances == null) {
				childrenCachedInstances = new HashMap();
			}
			childrenCachedInstances.put(
					Arrays.asList(new Object[] { childInstance, childInInstances}),
					childOutInstances);
		}

		MatcherInstances getChildCacheInstances(EEntity childInstance,
				MatcherInstances childInInstances) {
			return childrenCachedInstances != null ?
					(MatcherInstances) childrenCachedInstances.get(
							Arrays.asList(new Object[] { childInstance, childInInstances}))
							: null;
		}
	}

	static class UserCachedInstances extends CachedInstances {

		EEntity selfInstance;

		UserCachedInstances(int initialGetCount) {
			super(initialGetCount);
		}

		UserCachedInstances(int remainingGetCount,
				Map/*<MatcherInstances, Object>*/ outInstanceMap,
				EEntity[] dependentSelfInstances,
				Map /*<List<EEntity, MatcherInstances>, MatcherInstances>*/
				childrenCachedInstances) {
			super(remainingGetCount, outInstanceMap,
					dependentSelfInstances, childrenCachedInstances);
		}
		
	}

	private static final CachedInstancesTypeHelper CACHED_INSTANCES_TYPE_HELPER =
		new CachedInstancesTypeHelper();

	private static class CachedInstancesTypeHelper {
		CachedInstances newInstance(int initialGetCount) {
			return new CachedInstances(initialGetCount);
		}

		public CachedInstances asType(CachedInstances cachedInstances) {
			return cachedInstances;
		}
	}

	private static final UserCachedInstancesTypeHelper USER_CACHED_INSTANCES_TYPE_HELPER =
		new UserCachedInstancesTypeHelper();

	private static class UserCachedInstancesTypeHelper extends CachedInstancesTypeHelper {
		CachedInstances newInstance(int initialGetCount) {
			return new UserCachedInstances(initialGetCount);
		}
		
		public CachedInstances asType(CachedInstances cachedInstances) {
			if(cachedInstances instanceof UserCachedInstances) {
				return cachedInstances;
			} else {
				return new UserCachedInstances(
						cachedInstances.remainingGetCount,
						cachedInstances.outInstanceMap,
						cachedInstances.dependentSelfInstances,
						cachedInstances.childrenCachedInstances);
			}
		}
	}

	static class ImmutableArraySet extends ArraySet {

		public ImmutableArraySet(Collection c) {
			super(sortArray(c.toArray()));
		}

		public ImmutableArraySet(Object[] a) {
			super(sortArray(a));
		}

		public boolean contains(Object o) {
			int foundIdx = Arrays.binarySearch(array, o, HASH_CODE_COMPARATOR);
			return foundIdx >= 0 ? findEqObject(foundIdx, o) : false;
		}

		private static Object[] sortArray(Object[] array) {
			Arrays.sort(array, HASH_CODE_COMPARATOR);
			return array;
		}
	}

	static class ImmutableArrayMap extends AbstractMap {
		private static final Comparator ENTRY_HASH_CODE_COMPARATOR =
			new Comparator() {

			public int compare(Object o1, Object o2) {
				Entry e1 = (Entry)o1;
				Entry e2 = (Entry)o2;
				return e1.getKey().hashCode() - e2.getKey().hashCode();
			}

		};

		private Object[] keys;
		private Object[] values;
		private boolean keysHashCodeSet;
		private int keysHashCode;
		private boolean cachedHashCodeSet = false;
		private int cachedHashCode;

		public ImmutableArrayMap(List inKeys, List inValues) {
			int size = inKeys.size();
			if(inValues.size() < size) {
				throw new IllegalArgumentException();
			}
			keysHashCode = 0;
			long[] entryIndices = new long[size];
			for (int i = 0; i < entryIndices.length; i++) {
				int keyHashCode = inKeys.get(i).hashCode();
				entryIndices[i] = ((long)keyHashCode << 32) | i;
				keysHashCode ^= keyHashCode;
			}
			keysHashCodeSet = true;
			Arrays.sort(entryIndices);
			keys = new Object[size];
			values = new Object[size];
			for (int i = 0; i < entryIndices.length; i++) {
				int idx = (int)(entryIndices[i] & 0x7FFFFFFF);
				keys[i] = inKeys.get(idx);
				values[i] = inValues.get(idx);
			}
		}

		public ImmutableArrayMap(Map map) {
			Entry[] entries =
				(Entry[])map.entrySet().toArray(new Entry[map.size()]);
			Arrays.sort(entries, ENTRY_HASH_CODE_COMPARATOR);
			keysHashCodeSet = false;
			keys = new Object[entries.length];
			values = new Object[entries.length];
			for (int i = 0; i < entries.length; i++) {
				Entry entry = entries[i];
				keys[i] = entry.getKey();
				values[i] = entry.getValue();
			}
		}

		public int size() {
			return keys.length;
		}

		public boolean isEmpty() {
			return keys.length == 0;
		}

		public Object get(Object key) {
			int foundIdx = Arrays.binarySearch(keys, key, HASH_CODE_COMPARATOR);
			if(foundIdx >= 0) {
				foundIdx = findEqKey(foundIdx, key);
			}
			return foundIdx >= 0 ? values[foundIdx] : null;
		}

		public boolean containsKey(Object key) {
			int foundIdx = Arrays.binarySearch(keys, key, HASH_CODE_COMPARATOR);
			return foundIdx >= 0 ? findEqKey(foundIdx, key) >= 0 : false;
		}

		public boolean containsValue(Object value) {
			for (int i = 0; i < values.length; i++) {
				if(values[i].equals(value)) {
					return true;
				}
			}
			return false;
		}
		
		public Set keySet() {
			return new KeySet(keys);
		}

		public Collection values() {
			return new ValueCollection(values);
		}

		public Set entrySet() {
			return new EntrySet();
		}

		public void putAll(Map t) {
			throw new UnsupportedOperationException();
		}

		public Object remove(Object key) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public int hashCode() {
			if(!cachedHashCodeSet) {
				if(keysHashCodeSet) {
					cachedHashCode = keysHashCode;
					for (int i = 0; i < values.length; i++) {
						cachedHashCode ^= values[i].hashCode();
					}
				} else {
					cachedHashCode = 0;
					for (int i = 0; i < keys.length; i++) {
						cachedHashCode ^= keys[i].hashCode() ^ values[i].hashCode();
					}
				}
				cachedHashCodeSet = true;
			}
			return cachedHashCode;
		}

		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}

			if (!(o instanceof Map)) {
				return false;
			}

			Map t = (Map) o;
			if (t.size() != size()) {
				return false;
			}

			for (int i = 0; i < keys.length; i++) {
				Object key = keys[i];
				Object value = values[i];
				if (value == null) {
					if (!(t.get(key)==null && t.containsKey(key))) {
						return false;
					}
				} else {
					if(!value.equals(t.get(key))) {
						return false;
					}
				}
			}

			return true;
		}

		protected int findEqKey(int foundIdx, Object key) {
			int keyHashCode = key.hashCode();
			int startIdx = foundIdx;
			do {
				if(keys[startIdx].equals(key)) {
					return startIdx;
				}
			} while(++startIdx < keys.length
					&& keys[startIdx].hashCode() == keyHashCode);
			startIdx = foundIdx;
			while(--startIdx >= 0
					&& keys[startIdx].hashCode() == keyHashCode) {
				if(keys[startIdx].equals(key)) {
					return startIdx;
				}
			}
			return -1;
		}

		private static class KeySet extends ArraySet {

			public KeySet(Object[] keys) {
				super(keys);
			}

			public boolean contains(Object o) {
				int foundIdx = Arrays.binarySearch(array, o, HASH_CODE_COMPARATOR);
				return foundIdx >= 0 ? findEqObject(foundIdx, o) : false;
			}

		}

		private class EntrySet extends ArraySet {

			public EntrySet() {
				super(keys);
			}

			public Iterator iterator() {
				return new SimpleEntryIterator();
			}

			public boolean contains(Object o) {
	            if (!(o instanceof Entry)) {
	            	return false;
	            }
	            Entry e = (Entry)o;
	            Object searchKey = e.getKey();
				int foundIdx = Arrays.binarySearch(keys, searchKey, HASH_CODE_COMPARATOR);
				return foundIdx >= 0 ? findEqObject(foundIdx, searchKey) : false;
			}

		}

		private static class ValueCollection extends AbstractCollection {

			private final Object[] values;

			ValueCollection(Object[] values) {
				this.values = values;
			}

			public boolean contains(Object value) {
				for (int i = 0; i < values.length; i++) {
					if(values[i].equals(value)) {
						return true;
					}
				}
				return false;
			}

			public Iterator iterator() {
				return new SetIterator(values);
			}

			public int size() {
				return values.length;
			}

			public boolean isEmpty() {
				return values.length == 0;
			}

			public boolean add(Object o) {
				throw new UnsupportedOperationException();
			}

			public boolean addAll(Collection t) {
				throw new UnsupportedOperationException();
			}

			public boolean remove(Object o) {
				throw new UnsupportedOperationException();
			}

			public boolean removeAll(Collection c) {
				throw new UnsupportedOperationException();
			}

			public boolean retainAll(Collection c) {
				throw new UnsupportedOperationException();
			}

			public void clear() {
				throw new UnsupportedOperationException();
			}
			
		}

		private class SimpleEntryIterator implements Iterator {

			private int nextIdx = 0;


			public Object next() {
				Entry e = new SimpleEntry(keys[nextIdx], values[nextIdx]);
				nextIdx++;
				return e;
			}
		
			public boolean hasNext() {
				return nextIdx < keys.length;
			}
		
			public void remove() {
				throw new UnsupportedOperationException();
			}

			
		}

		private static class SimpleEntry implements Entry {
			Object key;
			Object value;

			public SimpleEntry(Object key, Object value) {
				this.key   = key;
				this.value = value;
			}

			public SimpleEntry(Map.Entry e) {
				this.key   = e.getKey();
				this.value = e.getValue();
			}

			public Object getKey() {
				return key;
			}

			public Object getValue() {
				return value;
			}

			public Object setValue(Object value) {
				Object oldValue = this.value;
				this.value = value;
				return oldValue;
			}

			public boolean equals(Object o) {
				if (!(o instanceof Map.Entry)) {
					return false;
				}
				Map.Entry e = (Map.Entry)o;
				return eq(key, e.getKey()) &&  eq(value, e.getValue());
			}

			public int hashCode() {
				return (((key == null) ? 0 : key.hashCode()) ^
						((value == null) ? 0 : value.hashCode()));
			}

			public String toString() {
				return key + "=" + value;
			}

			private static boolean eq(Object o1, Object o2) {
				return (o1 == null ? o2 == null : o1.equals(o2));
			}
		}
	}

	private static final Comparator HASH_CODE_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			return o1.hashCode() - o2.hashCode();
		}

	};

	private static abstract class ArraySet extends AbstractSet {

		private boolean cachedHashCodeSet = false;
		private int cachedHashCode;
		protected final Object[] array;

		ArraySet(Object[] array) {
			this.array = array;
		}

		public Iterator iterator() {
			return new SetIterator(array);
		}

		public int size() {
			return array.length;
		}

		public boolean isEmpty() {
			return array.length == 0;
		}

		public boolean add(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection t) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public Object[] toArray() {
			Object[] result = new Object[array.length];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}

		public Object[] toArray(Object[] a) {
	        if (a.length < array.length) {
	            a = (Object[])Array.newInstance(
	            		a.getClass().getComponentType(), array.length);
	        }

			System.arraycopy(array, 0, a, 0, array.length);

	        if (a.length > array.length) {
	        	a[array.length] = null;
	        }

	        return a;
		}

		public int hashCode() {
			if(!cachedHashCodeSet) {
				cachedHashCode = super.hashCode();
				cachedHashCodeSet = true;
			}
			return cachedHashCode;
		}

		protected boolean findEqObject(int foundIdx, Object o) {
			int objHashCode = o.hashCode();
			int startIdx = foundIdx;
			do {
				if(array[startIdx].equals(o)) {
					return true;
				}
			} while(++startIdx < array.length
					&& array[startIdx].hashCode() == objHashCode);
			startIdx = foundIdx;
			while(--startIdx >= 0
					&& array[startIdx].hashCode() == objHashCode) {
				if(array[startIdx].equals(o)) {
					return true;
				}
			}
			return false;
		}

	}

	private static class SetIterator implements Iterator {

		private final Object[] array;
		private int nextIdx = 0;

		public SetIterator(Object[] array) {
			this.array = array;
			nextIdx = 0;
		}

		public Object next() {
			return array[nextIdx++];
		}
	
		public boolean hasNext() {
			return nextIdx < array.length;
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}

		
	}

} // MappingContext
