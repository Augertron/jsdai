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

import jsdai.mapping.CConstraint_attribute;
import jsdai.mapping.EAggregate_member_constraint;

/**
 *
 * This class is a superclass of CAggregate_member_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingAggregate_member_constraint extends CConstraint_attribute 
	implements MappingConstraintPath, MappingConstraintMatcher {

	protected CMappingAggregate_member_constraint() { }

	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EAggregate_member_constraint selfInterface = (EAggregate_member_constraint)this;
		
		mappingContext.attributePathPush(selfInterface.testMember(null) ? 
										 selfInterface.getMember(null) : -1);
		try {
			((MappingConstraintPath)selfInterface.getAttribute(null))
				.mapUsersForward(mappingContext, instances);
		} finally {
			mappingContext.attributePathPop();
		}
	}

	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EAggregate_member_constraint selfInterface = (EAggregate_member_constraint)this;
		
		mappingContext.attributePathPush(selfInterface.testMember(null) ? 
										 selfInterface.getMember(null) : -1);
		try {
			((MappingConstraintPath)selfInterface.getAttribute(null))
				.mapUsersBackward(mappingContext, instances);
		} finally {
			mappingContext.attributePathPop();
		}
	}

	/**
     * @since 3.6.0
     */
    public MatcherInstances findForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			EAggregate_member_constraint selfInterface = (EAggregate_member_constraint)this;
			instances.attributePathPush(selfInterface.testMember(null) ? 
					selfInterface.getMember(null) : -1);

			try {
				outInstances =
					((MappingConstraintMatcher)selfInterface.getAttribute(null))
					.findForward(mappingContext, instances, false);
			} finally {
				instances.attributePathPop();
			}
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { selfInterface.getAttribute(null) });
			return outInstances;
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
			EAggregate_member_constraint selfInterface = (EAggregate_member_constraint)this;
			instances.attributePathPush(selfInterface.testMember(null) ? 
					selfInterface.getMember(null) : -1);

			try {
				outInstances = 
					((MappingConstraintMatcher)selfInterface.getAttribute(null))
					.findBackward(mappingContext, instances, false);
			} finally {
				instances.attributePathPop();
			}
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { selfInterface.getAttribute(null) });
			return outInstances;
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
			EAggregate_member_constraint selfInterface = (EAggregate_member_constraint)this;
			instances.attributePathPush(selfInterface.testMember(null) ? 
					selfInterface.getMember(null) : -1);

			try {
				outInstances = 
					((MappingConstraintMatcher)selfInterface.getAttribute(null))
					.findPathForward(mappingContext, instances, false);
			} finally {
				instances.attributePathPop();
			}
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { selfInterface.getAttribute(null) });
			return outInstances;
		}
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		EAggregate_member_constraint selfInterface = (EAggregate_member_constraint)this;
    	return new EEntity[] { selfInterface.getAttribute(null) };
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
			EAggregate_member_constraint selfInterface = (EAggregate_member_constraint)this;
			instances.attributePathPush(selfInterface.testMember(null) ? 
					selfInterface.getMember(null) : -1);

			try {
				outInstances =
					((MappingConstraintMatcher)selfInterface.getAttribute(null))
					.findPathBackward(mappingContext, instances, false);
			} finally {
				instances.attributePathPop();
			}
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { selfInterface.getAttribute(null) });
			return outInstances;
		}
	}

} // CMappingAggregate_member_constraint
