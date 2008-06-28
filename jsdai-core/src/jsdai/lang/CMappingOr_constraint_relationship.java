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

import jsdai.mapping.CInstance_constraint;
import jsdai.mapping.EInstance_constraint;

/**
 *
 * This class is a superclass of COr_constraint_relationship class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @since 3.6.0
 */

public class CMappingOr_constraint_relationship extends CInstance_constraint
	implements MappingConstraintMatcher {

	protected CMappingOr_constraint_relationship() { }
	
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
			EInstance_constraint selfInterface = (EInstance_constraint)this;
			outInstances = ((MappingConstraintMatcher)selfInterface.getElement1(null))
				.findForward(mappingContext, instances, false);
			outInstances =
				outInstances.union(((MappingConstraintMatcher)selfInterface.getElement2(null))
									   .findForward(mappingContext, instances, false));
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { selfInterface.getElement1(null),
					selfInterface.getElement2(null) });
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
		EInstance_constraint selfInterface = (EInstance_constraint)this;
    	return new EEntity[] { selfInterface.getElement1(null),
				selfInterface.getElement2(null) };
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

} // CMappingOr_constraint_relationship
