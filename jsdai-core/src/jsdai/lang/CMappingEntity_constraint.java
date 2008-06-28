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
import jsdai.mapping.CConstraint_attribute;
import jsdai.mapping.EEntity_constraint;
import jsdai.mapping.EExact_entity_constraint;

/**
 *
 * This class is a superclass of CEntity_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingEntity_constraint extends CConstraint_attribute
	implements MappingConstraintPath, MappingConstraintMatcher {

	protected CMappingEntity_constraint() { }

	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EEntity_constraint selfInterface = (EEntity_constraint)this;
		
		((MappingConstraintPath)selfInterface.getAttribute(null))
			.mapUsersForward(mappingContext, instances);
		EEntity_definition domain = selfInterface.getDomain(null);
		//FIXME: test type of instances in the Set
	}

	//FIXME: maybe throw an exception since for me this combination makes no sense
	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		EEntity_constraint selfInterface = (EEntity_constraint)this;
		
		((MappingConstraintPath)selfInterface.getAttribute(null))
			.mapUsersBackward(mappingContext, instances);
		EEntity_definition domain = selfInterface.getDomain(null);
		//FIXME: test type of instances in the Set
	}

	private MatcherInstances generalFind(MappingContext mappingContext, MatcherInstances instances,
			boolean decCacheUseCnt, int direction) throws SdaiException {
		MatcherInstances outInstances =
			mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
		if(outInstances != null) {
			return outInstances;
		} else {
			EEntity_constraint selfInterface = (EEntity_constraint)this;

			EEntity_definition domain = selfInterface.getDomain(null);
			outInstances = this instanceof EExact_entity_constraint ?
				instances.extractExactType(mappingContext, domain) :
				instances.extractType(mappingContext, domain);
			MappingConstraintMatcher childMatcher = 
				(MappingConstraintMatcher)selfInterface.getAttribute(null);
			switch(direction) {
			case 0:
				outInstances = childMatcher.findForward(mappingContext,
						outInstances, false);
				break;
			case 1:
				outInstances = childMatcher.findBackward(mappingContext,
						outInstances, false);
				break;
			case 2:
				outInstances = childMatcher.findPathForward(mappingContext,
						outInstances, false);
				break;
			case 3:
				outInstances = childMatcher.findPathBackward(mappingContext,
						outInstances, false);
				break;
			}
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { (EEntity)childMatcher });
			return outInstances;
		}
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		return generalFind(mappingContext, instances, decCacheUseCnt, 0);
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		return generalFind(mappingContext, instances, decCacheUseCnt, 1);
	}

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathForward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		return generalFind(mappingContext, instances, decCacheUseCnt, 2);
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		EEntity_constraint selfInterface = (EEntity_constraint)this;
		return new EEntity[] { selfInterface.getAttribute(null) };
    }

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		return generalFind(mappingContext, instances, decCacheUseCnt, 3);
	}

} // CMappingEntity_constraint
