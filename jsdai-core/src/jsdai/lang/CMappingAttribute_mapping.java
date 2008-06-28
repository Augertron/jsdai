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

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jsdai.lang.MappingContext.CachedInstances;
import jsdai.mapping.ADerived_variant_entity_mapping;
import jsdai.mapping.CDerived_variant_entity_mapping;
import jsdai.mapping.CGeneric_attribute_mapping;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EDerived_variant_entity_mapping;
import jsdai.mapping.EEntity_mapping;

/**
 *
 * This class is a superclass of CAttribute_mapping class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingAttribute_mapping 
	extends CGeneric_attribute_mapping implements MappingConstraintPath, MappingConstraintMatcher {

	protected CMappingAttribute_mapping() { }
	
	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EAttribute_mapping selfInterface = (EAttribute_mapping)this;

		mappingContext.attributePathClear();
		
		if(selfInterface.testDomain(null)) {
			EEntity domainEntity = selfInterface.getDomain(null);
			if(domainEntity instanceof EEntity_mapping) {
				EEntity_mapping domain = (EEntity_mapping)domainEntity;
				ADerived_variant_entity_mapping derivedVariants = null;
				SdaiIterator derivedVariantIter = null;
				Set derivedInstances = null;
				Iterator instanceIter = instances.iterator();
				while(instanceIter.hasNext()) {
					EEntity instance = (EEntity)instanceIter.next();
					if(!mappingContext.testMappedEntity(instance, domain, mappingContext.defaultMode)) {
						instanceIter.remove();
						if(derivedVariants == null) {
							derivedVariants = new ADerived_variant_entity_mapping();
							CDerived_variant_entity_mapping.usedinRelating(null, domain, 
																		   mappingContext.mappingDomain, 
																		   derivedVariants);
							if(derivedVariants.getMemberCount() > 0) {
								derivedVariantIter = derivedVariants.createIterator();
								derivedInstances = new HashSet();
							}
						}
						if(derivedInstances != null) {
							derivedInstances.add(instance);
						}
					}
				}
				if(derivedInstances != null) {
					while(derivedVariantIter.next()) {
						EDerived_variant_entity_mapping derivedVariant =
							derivedVariants.getCurrentMember(derivedVariantIter);
						if(derivedVariant.testConstraints(null)) {
							Set oneDerivedInstances = (Set)((HashSet)derivedInstances).clone();
							((MappingConstraintPath)derivedVariant.getConstraints(null))
								.mapUsersForward(mappingContext, oneDerivedInstances);
							instances.addAll(oneDerivedInstances);
						} else {
							instances.addAll(derivedInstances);
						}
					}
				}
				if(instances.isEmpty()) {
					return;
				}
			}
		}

		if(selfInterface.testConstraints(null)) {
			((MappingConstraintPath)selfInterface.getConstraints(null))
				.mapUsersForward(mappingContext, instances);
		}

		EEntity_mapping entityMapping = selfInterface.getParent_entity(null);
		Iterator instanceIter = instances.iterator();
		while(instanceIter.hasNext()) {
			EEntity instance = (EEntity)instanceIter.next();
			if(!mappingContext.testMappedEntity(instance, entityMapping, mappingContext.defaultMode)) {
				instanceIter.remove();
			}
		}
	}

	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"Attribute mapping can not be called as part of backward references: " +
								this);
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"This constraint can not be called as part of forward references: " +
								this);
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"This constraint can not be called as part of backward references: " +
								this);
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		MappingMatcherInstances mappingInstances =
			mappingContext.getCacheMappingInstances(this, instances);
		if(mappingInstances != null) {
			MatcherInstances outInstances = mappingInstances.getMatcherInstances();
			if(mappingInstances != outInstances && !mappingContext.preprocessInstances) {
				mappingContext.setPersistentCacheInstances(this, instances, outInstances);

				EAttribute_mapping selfInterface = (EAttribute_mapping)this;
				EEntity_mapping domain = (EEntity_mapping)selfInterface.getDomain(null);
				((MappingConstraintMatcher)domain)
					.findForward(mappingContext,
							instances.dup(null, MatcherInstances.STATUS_FORWARD), false);
				ADerived_variant_entity_mapping derivedVariants =
					new ADerived_variant_entity_mapping();
				CDerived_variant_entity_mapping.usedinRelating(null, domain, 
															   mappingContext.context.mappingDomain, 
															   derivedVariants);
				SdaiIterator derivedVariantIter = derivedVariants.createIterator();
				while(derivedVariantIter.next()) {
					EDerived_variant_entity_mapping derivedVariant =
						derivedVariants.getCurrentMember(derivedVariantIter);
					((MappingConstraintMatcher)derivedVariant.getRelated(null))
						.findForward(mappingContext,
								instances.dup(null, MatcherInstances.STATUS_FORWARD), false);
				}
			}
			return outInstances;
		} else {
			MatcherInstances outInstances;
			EAttribute_mapping selfInterface = (EAttribute_mapping)this;
			boolean preprocessInstances = false;
			if(selfInterface.testDomain(null)) {
				EEntity domainEntity = selfInterface.getDomain(null);
				if(domainEntity instanceof EEntity_mapping) {
					EEntity_mapping domain = (EEntity_mapping)domainEntity;
					outInstances = 
						((MappingConstraintMatcher)domainEntity)
						.findForward(mappingContext,
								instances.dup(null, MatcherInstances.STATUS_FORWARD), true);
					outInstances = newPathForwardInstances(outInstances);
					ADerived_variant_entity_mapping derivedVariants =
						new ADerived_variant_entity_mapping();
					CDerived_variant_entity_mapping.usedinRelating(null, domain, 
																   mappingContext.context.mappingDomain, 
																   derivedVariants);
					SdaiIterator derivedVariantIter = derivedVariants.createIterator();
					while(derivedVariantIter.next()) {
						EDerived_variant_entity_mapping derivedVariant =
							derivedVariants.getCurrentMember(derivedVariantIter);
						MatcherInstances derivedVariantInstances = 
							((MappingConstraintMatcher)derivedVariant.getRelated(null))
							.findForward(mappingContext,
									instances.dup(null, MatcherInstances.STATUS_FORWARD), true);
						derivedVariantInstances = newPathForwardInstances(derivedVariantInstances);
						if(derivedVariant.testConstraints(null)) {
							derivedVariantInstances = 
								((MappingConstraintMatcher)derivedVariant.getConstraints(null))
								.findPathForward(mappingContext, derivedVariantInstances, false);
						}
						outInstances = outInstances.union(derivedVariantInstances);
					}
					preprocessInstances = mappingContext.preprocessInstances;
				} else {
					outInstances = instances;
				}
			} else {
				outInstances = instances; //.dup(null, MatcherInstances.STATUS_PATH_FORWARD);
			}
			CachedInstances cacheInstances = mappingContext.getCacheInstances(this);
			if(cacheInstances != null
					&& (mappingInstances =
						(MappingMatcherInstances) cacheInstances.outInstanceMap.get(
								instances)) != null) {
				outInstances = mappingInstances.getMatcherInstances();
			} else {
				if(selfInterface.testConstraints(null)) {
					outInstances =
						((MappingConstraintMatcher)selfInterface.getConstraints(null))
						.findPathForward(mappingContext, outInstances, cacheInstances == null);
				}
				Map outInstanceMap = outInstances.getInstanceMap();
				Iterator outInstanceIter = outInstanceMap.values().iterator();
				if(outInstanceIter.hasNext() && outInstanceIter.next() == NON_PATH_VALUE) {
					outInstances = outInstances.dup(new MirroredKeyMap(outInstanceMap), outInstances.status);
				}
				mappingContext.setPersistentCacheInstances(this, instances, (preprocessInstances) ?
						(MappingMatcherInstances)new MappingPreInstances(outInstances, null) :
							(MappingMatcherInstances)outInstances);
			}
			return outInstances;
		}
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
								"This constraint can not be called as part of path backward references: " +
								this);
	}

	static private MatcherInstances newPathForwardInstances(MatcherInstances instances)
		throws SdaiException {

		return instances.dup(new MirroredKeyMap(instances.getInstanceMap()),
							 MatcherInstances.STATUS_PATH_FORWARD);
	}

	static class MirroredKeyMap implements Map {
		final Set origMapKeys;

		MirroredKeyMap(Map origMap) {
			this.origMapKeys = origMap.keySet();
		}

		public int size() {
			return origMapKeys.size();
		}

		public boolean isEmpty() {
			return origMapKeys.isEmpty();
		}

		public boolean containsKey(Object key) {
			return origMapKeys.contains(key);
		}

		public boolean containsValue(Object value) {
			return origMapKeys.contains(value);
		}

		public Object get(Object key) {
			return origMapKeys.contains(key) ? key : null;
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
			return origMapKeys;
		}

		public Collection values() {
			return origMapKeys;
		}

		public Set entrySet() {
			return new MirroredEntrySet(origMapKeys);
		}

		public boolean equals(Object o) {
			if(o == this) {
				return true;
			}

			if(!(o instanceof Map)) {
// 				System.out.println("origMap: " + origMap.getClass() + " o: " + o.getClass() + " origMap.equals(o): " + origMap.equals(o));
// 				return origMap.equals(o);
				return false;
			}

			Map t = (Map)o;
			if (t.size() != origMapKeys.size())
				return false;

			Iterator i = origMapKeys.iterator();
			while(i.hasNext()) {
				Object key = i.next();
				if(key == null) {
					if(!(t.containsKey(null) && t.get(null) == null)) {
						return false;
					}
				} else {
					if(!key.equals(t.get(key))) {
						return false;
					}
				}
			}
			return true;
		}

		public int hashCode() {
			int h = 0;
			Iterator i = origMapKeys.iterator();
			while(i.hasNext()) {
				int keyHashCode = i.next().hashCode();
				h += keyHashCode ^ keyHashCode;
			}
			return h;
		}

		public String toString() {
			return "Mirror: " + origMapKeys.toString();
		}

		static class MirroredEntrySet extends AbstractSet {
			final Set origMapKeys;

			MirroredEntrySet(Set origMapKeys) {
				this.origMapKeys = origMapKeys;
			}

			public int size() {
				return origMapKeys.size();
			}

			public boolean isEmpty() {
				return origMapKeys.isEmpty();
			}

			public boolean contains(Object key) {
				if(key instanceof Entry) {
					Entry entry = (Entry)key;
					key = entry.getKey();
					Object value = entry.getValue();
					if(key == null) {
						if(value != null) {
							return false;
						}
					} else if(!key.equals(value)) {
						return false;
					}
					return origMapKeys.contains(key);
				} else {
					return false;
				}
			}

			public Iterator iterator() {
				return new MirroredEntryIter(origMapKeys.iterator());
			}

		}

		static class MirroredEntryIter implements Iterator {
			final Iterator origIterator;

			MirroredEntryIter(Iterator origIterator) {
				this.origIterator = origIterator;
			}

			public boolean hasNext() {
				return origIterator.hasNext();
			}

			public Object next() {
				return new MirroredEntry(origIterator.next());
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		
		static class MirroredEntry implements Entry {
			final Object origKey;

			MirroredEntry(Object origKey) {
				this.origKey = origKey;
			}

			public Object getKey() {
				return origKey;
			}

			public Object getValue() {
				return origKey;
			}

			public Object setValue(Object value) {
				throw new UnsupportedOperationException();
			}

			public boolean equals(Object o) {
				if(!(o instanceof Entry)) {
					return false;
				}

				Entry e = (Entry)o;
				return origKey == null ?
					e.getKey() == null && e.getValue() == null :
						origKey.equals(e.getKey()) && origKey.equals(e.getValue());
			}

			public int hashCode() {
				int keyHashCode = origKey == null ? 0 : origKey.hashCode();
				return keyHashCode ^ keyHashCode;
			}

			public String toString() {
				return "Mirror: " + origKey.toString();
			}
		}
	}

} // CMappingAttribute_mapping
