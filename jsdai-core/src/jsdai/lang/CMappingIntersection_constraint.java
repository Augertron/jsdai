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

import java.util.HashSet;
import java.util.Set;
import jsdai.mapping.AConstraint_select;
import jsdai.mapping.CConstraint;
import jsdai.mapping.EIntersection_constraint;

/**
 *
 * This class is a superclass of CIntersection_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingIntersection_constraint extends CConstraint
	implements MappingConstraintPath, MappingConstraintMatcher {

	protected CMappingIntersection_constraint() { }
	
	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EIntersection_constraint selfInterface = (EIntersection_constraint)this;
		
		AConstraint_select constraints = selfInterface.getSubpaths(null);
		SdaiIterator constraintIter = constraints.createIterator();
		Set outputInstances = null;
		while(constraintIter.next()) {
			EEntity constraint = constraints.getCurrentMember(constraintIter);
			Set constraintInstances = (Set)((HashSet)instances).clone();
			((MappingConstraintPath)constraint)
				.mapUsersForward(mappingContext, constraintInstances);
			if(outputInstances == null) {
				outputInstances = constraintInstances;
			} else {
				outputInstances.retainAll(constraintInstances);
			}
		}
		instances.clear();
		instances.addAll(outputInstances);
	}

	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"Path constraint can not be called as part of backward references: " +
								this);
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
			EIntersection_constraint selfInterface = (EIntersection_constraint)this;
			AConstraint_select constraints = selfInterface.getSubpaths(null);
			EEntity[] dependentSelfInstances =
				constraints.getMemberCount() > 0 ?
						new EEntity[constraints.getMemberCount()] : null;
			int dependentSelfInstCnt = 0;
			SdaiIterator constraintIter = constraints.createIterator();
			while(constraintIter.next()) {
				 EEntity constraint = constraints.getCurrentMember(constraintIter);
				if(outInstances != null) {
					outInstances =
						outInstances.intersect(
								((MappingConstraintMatcher)constraint).findPathForward(
										mappingContext,
										instances.dup(null,
												MatcherInstances
												.STATUS_PATH_FORWARD), false));
				} else {
					outInstances =
						((MappingConstraintMatcher)constraint).findPathForward(mappingContext,
								instances.dup(null,
										MatcherInstances
										.STATUS_PATH_FORWARD), false);
				}
				dependentSelfInstances[dependentSelfInstCnt++] = constraint;
			}
			mappingContext.setCacheInstances(this, instances,
					outInstances, decCacheUseCnt, dependentSelfInstances);
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
		return findForward(mappingContext, instances, false);
	}

    /**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		EIntersection_constraint selfInterface = (EIntersection_constraint)this;
		AConstraint_select constraints = selfInterface.getSubpaths(null);
		EEntity[] dependentSelfInstances =
			constraints.getMemberCount() > 0 ?
					new EEntity[constraints.getMemberCount()] : null;
		int dependentSelfInstCnt = 0;
		SdaiIterator constraintIter = constraints.createIterator();
		while(constraintIter.next()) {
			 EEntity constraint = constraints.getCurrentMember(constraintIter);
			dependentSelfInstances[dependentSelfInstCnt++] = constraint;
		}
		return dependentSelfInstances;
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

} // CMappingIntersection_constraint
