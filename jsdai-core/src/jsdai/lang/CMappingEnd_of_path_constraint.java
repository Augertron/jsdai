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

import java.util.Iterator;
import java.util.Set;

import jsdai.lang.MappingContext.CachedInstances;
import jsdai.mapping.CConstraint;
import jsdai.mapping.EEnd_of_path_constraint;

/**
 *
 * This class is a superclass of CEnd_of_path_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingEnd_of_path_constraint extends CConstraint
	implements MappingConstraintPath, MappingConstraintMatcher {
	
	protected CMappingEnd_of_path_constraint() { }

	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EEnd_of_path_constraint selfInterface = (EEnd_of_path_constraint)this;
		
		EEntity constraint = selfInterface.getConstraints(null);
		Iterator instanceIter = instances.iterator();
		while(instanceIter.hasNext()) {
			EEntity instance = (EEntity)instanceIter.next();
			if(!mappingContext.testMappingPath(instance, constraint)) {
				instanceIter.remove();
			}
		}
	}

	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"End of path constraint can not be called as part of backward references");
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"This constraint can not be called as part of forward references");
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
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			EEnd_of_path_constraint selfInterface = (EEnd_of_path_constraint)this;
			EEntity constraints = selfInterface.getConstraints(null);
			MatcherInstances allInInstances =
				mappingContext.newAllInstances(MatcherInstances.STATUS_FORWARD);
			CachedInstances cachedInstances = mappingContext.getCacheInstances(selfInterface);
			MatcherInstances constraintsOutInstances = null;
			boolean foundConstraintsOutInstances = false;
			if(cachedInstances != null) {
				constraintsOutInstances =
					cachedInstances.getChildCacheInstances(constraints, allInInstances);
			}
			if(constraintsOutInstances == null) {
				constraintsOutInstances = ((MappingConstraintMatcher)constraints)
					.findForward(mappingContext, allInInstances, true);
				foundConstraintsOutInstances = true;
			}
			outInstances = instances.intersect(constraintsOutInstances);
			cachedInstances =
				mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt);
			if(foundConstraintsOutInstances) {
				cachedInstances.addChildCacheInstances(constraints,
						allInInstances, constraintsOutInstances);
			}
			return outInstances;
		}
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
    	EEnd_of_path_constraint selfInterface = (EEnd_of_path_constraint)this;
    	return new EEntity[] { selfInterface.getConstraints(null) };
    }

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"This constraint can not be called as part of path backward references");
	}

} // CMappingEnd_of_path_constraint
