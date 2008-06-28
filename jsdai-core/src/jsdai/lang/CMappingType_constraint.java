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
import jsdai.dictionary.EEntity_definition;
import jsdai.mapping.CConstraint;
import jsdai.mapping.EExact_type_constraint;
import jsdai.mapping.EType_constraint;

/**
 *
 * This class is a superclass of CType_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingType_constraint extends CConstraint
	implements MappingConstraintPath, MappingConstraintMatcher {
	
	protected CMappingType_constraint() { }

	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EType_constraint selfInterface = (EType_constraint)this;
		
		if(selfInterface.testConstraints(null)) {
			((MappingConstraintPath)selfInterface.getConstraints(null))
				.mapUsersForward(mappingContext, instances);
		}
		EEntity_definition domain = selfInterface.getDomain(null);
		//FIXME: test type of instances in the Set
	}

	//FIXME: maybe throw an exception since for me this combination makes no sense
	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EType_constraint selfInterface = (EType_constraint)this;
		
		if(selfInterface.testConstraints(null)) {
			((MappingConstraintPath)selfInterface.getConstraints(null))
				.mapUsersBackward(mappingContext, instances);
		}
		EEntity_definition domain = selfInterface.getDomain(null);
		//FIXME: test type of instances in the Set
	}

	private MatcherInstances generalFind(MappingContext mappingContext, MatcherInstances instances,
			boolean decCacheUseCnt, boolean path) throws SdaiException {
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			EType_constraint selfInterface = (EType_constraint)this;

			EEntity[] dependentSelfInstances = null;
			if(selfInterface.testConstraints(null)) {
				EEntity constraints = selfInterface.getConstraints(null);
				MappingConstraintMatcher childMatcher = (MappingConstraintMatcher)constraints;
				instances = path ?
						childMatcher.findPathForward(mappingContext, instances, false) :
							childMatcher.findForward(mappingContext, instances, false);
				dependentSelfInstances = new EEntity[] { constraints };
			}
			EEntity_definition domain = selfInterface.getDomain(null);
			outInstances = this instanceof EExact_type_constraint ?
				instances.extractExactType(mappingContext, domain) :
				instances.extractType(mappingContext, domain);
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					dependentSelfInstances);
			return outInstances;
		}
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		return generalFind(mappingContext, instances, decCacheUseCnt, false);
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
		return generalFind(mappingContext, instances, decCacheUseCnt, true);
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		EType_constraint selfInterface = (EType_constraint)this;
		return selfInterface.testConstraints(null) ?
				new EEntity[] { selfInterface.getConstraints(null) } : null;
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

} // CMappingType_constraint
