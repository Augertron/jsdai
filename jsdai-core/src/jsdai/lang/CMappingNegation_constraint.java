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
import java.util.List;
import java.util.Map;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.MappingContext.ImmutableArrayMap;
import jsdai.mapping.CConstraint;
import jsdai.mapping.ENegation_constraint;

/**
 *
 * This class is a superclass of CNegation_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @since 3.6.0
 */

public class CMappingNegation_constraint extends CConstraint
	implements MappingConstraintMatcher {

	protected CMappingNegation_constraint() { }

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
			ENegation_constraint selfInterface = (ENegation_constraint)this;
			outInstances = ((MappingConstraintMatcher)selfInterface.getConstraints(null))
				.findForward(mappingContext, instances, false);
			outInstances = new NegationInstances(mappingContext, outInstances);

			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { selfInterface.getConstraints(null) });
			return outInstances;
		}
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
		throw new SdaiException(SdaiException.FN_NAVL,
								"This constraint can not be called as part of path forward references: " +
								this);
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		ENegation_constraint selfInterface = (ENegation_constraint)this;
		return new EEntity[] { selfInterface.getConstraints(null) };
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

	static class NegationInstances extends MatcherInstances {
		final MappingContext mappingContext;
		private Map matchingInstances;

		NegationInstances(MappingContext mappingContext, MatcherInstances matcherInstances) {
			super(matcherInstances.instances, matcherInstances.status);
			this.mappingContext = mappingContext;
			matchingInstances = null;
		}

		private NegationInstances(MappingContext mappingContext, Map instances, int status) {
			super(instances, status);
			this.mappingContext = mappingContext;
			matchingInstances = null;
		}

		protected MatcherInstances dup(Map instances, int status, boolean dupAttributePath) {
			if(instances != null) {
				return super.dup(instances, status, dupAttributePath);
			}
			NegationInstances newMatcherInstances =
				new NegationInstances(mappingContext, this.instances, status);
			return newMatcherInstances;
		}

		protected MatcherInstances intersect(MatcherInstances other) throws SdaiException {
			// We give no chance for other to do intersect
			return doIntersect(other);
		}

		protected MatcherInstances doIntersect(MatcherInstances other) throws SdaiException {
			Map newInstances = new HashMap();
			boolean otherNegation = other instanceof NegationInstances;
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			if(!instances.equals(workingModels.getInstances()) &&
			   !(otherNegation && other.instances.equals(workingModels.getInstances()))) {

				if(otherNegation) {
					newInstances.putAll(instances);
					newInstances.putAll(other.instances);
					return new NegationInstances(mappingContext, newInstances, status);
				} else {
					Map otherInstances = other.getInstanceMap();
					for(Iterator i = otherInstances.entrySet().iterator(); i.hasNext(); ) {
						Map.Entry entry = (Map.Entry)i.next();
						Object key = entry.getKey();
						if(!instances.containsKey(key)) {
							newInstances.put(key, entry.getValue());
						}
					}
				}
			}
			return new MatcherInstances(new ImmutableArrayMap(newInstances),
										(status & STATUS_PATH_MASK) != 0 ? status : other.status);
		}

		protected MatcherInstances union(MatcherInstances other) throws SdaiException {
			// We give no chance for other to do union
			return doUnion(other);
		}

		protected MatcherInstances doUnion(MatcherInstances other) throws SdaiException {
			boolean otherNegation = other instanceof NegationInstances;
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			if(instances.equals(workingModels.getInstances())) {
				return other;
			} else if(otherNegation && other.instances.equals(workingModels.getInstances())) {
				return this;
			} else {
				List newKeys = new ArrayList();
				List newValue = new ArrayList();
				if(otherNegation) {
					for(Iterator i = other.instances.keySet().iterator(); i.hasNext(); ) {
						Object o = i.next();
						if(instances.containsKey(o)) {
							newKeys.add(o);
							newValue.add(NON_PATH_VALUE);
						}
					}
				} else {
					Map otherInstances = other.getInstanceMap();
					for(Iterator i = instances.entrySet().iterator(); i.hasNext(); ) {
						Map.Entry entry = (Map.Entry)i.next();
						Object key = entry.getKey();
						if(!otherInstances.containsKey(key)) {
							newKeys.add(key);
							newValue.add(entry.getValue());
						}
					}
				}
				Map newInstances = new ImmutableArrayMap(newKeys, newValue);
				return new NegationInstances(mappingContext, newInstances, status);
			}
		}

		protected MatcherInstances difference(MatcherInstances other) throws SdaiException {
			boolean otherNegation = other instanceof NegationInstances;
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			if(instances.equals(workingModels.getInstances())) {
				return this;
			} else if(otherNegation) {
				if(other.instances.equals(workingModels.getInstances())) {
					return this;
				} else {
					List newKeys = new ArrayList();
					List newValue = new ArrayList();
					for(Iterator i = other.instances.entrySet().iterator(); i.hasNext(); ) {
						Map.Entry entry = (Map.Entry)i.next();
						Object key = entry.getKey();
						if(!instances.containsKey(key)) {
							newKeys.add(key);
							newValue.add(entry.getValue());
						}
					}
					Map newInstances = new ImmutableArrayMap(newKeys, newValue);
					return new MatcherInstances(newInstances, status);
				}
			} else {
				Map otherInstances = other.getInstanceMap();
				if(otherInstances.isEmpty()) {
					return this;
				} else {
					Map newInstances = new HashMap(instances);
					newInstances.putAll(otherInstances);
					return new NegationInstances(mappingContext, newInstances, status);
				}
			}
		}

		protected void put(Object key, Object value) {
			throw new UnsupportedOperationException("NegationInstances.put method can not be called");
		}

		protected MatcherInstances extractType(MappingContext mappingContext,
											   EEntity_definition type) throws SdaiException {
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			MatcherInstances typeInstances =
				new MatcherInstances(workingModels.getInstances(), status).extractType(mappingContext, type);
			return doIntersect(typeInstances);
		}

		protected MatcherInstances extractExactType(MappingContext mappingContext,
													EEntity_definition type) throws SdaiException {
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			MatcherInstances typeInstances =
				new MatcherInstances(new AEntityMap(workingModels.getExactInstances(type), (status & STATUS_PATH_MASK) != 0), status);
			return doIntersect(typeInstances);
		}

		protected Map getInstanceMap() throws SdaiException {
			if(matchingInstances != null) {
				return matchingInstances;
			} else {
				// FIXME: possible optimization is to create custom Map class
				ASdaiModel workingModels = mappingContext.context.working_modelAggr;
				AEntity allInstances = workingModels.getInstances();
				if(instances.isEmpty()) {
					return new AEntityMap(allInstances, false);
				} else {
					List newKeys = new ArrayList();
					List newValue = new ArrayList();
					if(!instances.equals(allInstances)) {
						SdaiIterator instanceIter = allInstances.createIterator();
						while(instanceIter.next()) {
							EEntity instance =
								allInstances.getCurrentMemberEntity(instanceIter);
							if(!instances.containsKey(instance)) {
								newKeys.add(instance);
								newValue.add(NON_PATH_VALUE);
							}
						}
					}
					matchingInstances = new ImmutableArrayMap(newKeys, newValue);
				}
				return matchingInstances;
			}
		}

		protected boolean isEmpty() throws SdaiException {
			ASdaiModel workingModels = mappingContext.context.working_modelAggr;
			return instances.equals(workingModels.getInstances());
		}

		protected boolean prefersMaster() {
			return true;
		}

		public boolean equals(Object obj) {
			return obj instanceof NegationInstances ? super.equals(obj) : false;
		}

	}

} // CMappingNegation_constraint
