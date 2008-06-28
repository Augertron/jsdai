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

import java.util.Set;
import jsdai.mapping.CConstraint;
import jsdai.mapping.EInverse_attribute_constraint;

/**
 *
 * This class is a superclass of CInverse_attribute_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingInverse_attribute_constraint extends CConstraint
	implements MappingConstraintPath, MappingConstraintMatcher {

	protected CMappingInverse_attribute_constraint() { }
	
	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EInverse_attribute_constraint selfInterface = (EInverse_attribute_constraint)this;
		
		((MappingConstraintPath)selfInterface.getInverted_attribute(null))
			.mapUsersBackward(mappingContext, instances);
	}

	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"Inverse constraint can not be inside of another inverse constraint: " +
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
			EInverse_attribute_constraint selfInterface = (EInverse_attribute_constraint)this;
		
			outInstances =
				((MappingConstraintMatcher)selfInterface.getInverted_attribute(null))
				.findBackward(mappingContext,
						instances.dup(null, MatcherInstances.STATUS_BACKWARD), false);

			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] {selfInterface.getInverted_attribute(null) });
			return outInstances;
		}
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"Inverse constraint can not be inside of another inverse constraint: " +
								this);
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
			EInverse_attribute_constraint selfInterface = (EInverse_attribute_constraint)this;
		
			outInstances =
				((MappingConstraintMatcher)selfInterface.getInverted_attribute(null))
				.findPathBackward(mappingContext,
						instances.dup(null, MatcherInstances.STATUS_PATH_BACKWARD), false);

			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] {selfInterface.getInverted_attribute(null) });
			return outInstances;
		}
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		EInverse_attribute_constraint selfInterface = (EInverse_attribute_constraint)this;
		return new EEntity[] {selfInterface.getInverted_attribute(null) };
    }

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"Inverse constraint can not be inside of another inverse constraint: " +
								this);
	}

} // CMappingInverse_attribute_constraint
