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
import jsdai.mapping.CConstraint_relationship;
import jsdai.mapping.EPath_constraint;

/**
 *
 * This class is a superclass of CPath_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CMappingPath_constraint extends CConstraint_relationship
	implements MappingConstraintPath, MappingConstraintMatcher {

	protected CMappingPath_constraint() { }
	
	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		Object ids[] = new Object[] { this, instances };
		Object value = mappingContext.findMappedUsersCache.getValueByIds(ids);
		if (value != null) {
			instances.clear();
			instances.addAll((Set)value);
		} else {
			ids[1] = ((HashSet)instances).clone();
			EPath_constraint selfInterface = (EPath_constraint)this;
		
			((MappingConstraintPath)selfInterface.getElement2(null))
				.mapUsersForward(mappingContext, instances);
			((MappingConstraintPath)selfInterface.getElement1(null))
				.mapUsersForward(mappingContext, instances);
			mappingContext.findMappedUsersCache.setValueByIds(ids, ((HashSet)instances).clone());
		}
	}

	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"Path constraint can not be called as part of backward references");
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
			EPath_constraint selfInterface = (EPath_constraint)this;
			outInstances = ((MappingConstraintMatcher)selfInterface.getElement2(null))
				.findForward(mappingContext, instances, false);
			outInstances = ((MappingConstraintMatcher)selfInterface.getElement1(null))
				.findForward(mappingContext, outInstances, false);
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
			EPath_constraint selfInterface = (EPath_constraint)this;
			outInstances = ((MappingConstraintMatcher)selfInterface.getElement2(null))
				.findPathForward(mappingContext, instances, false);
			outInstances = ((MappingConstraintMatcher)selfInterface.getElement1(null))
				.findPathForward(mappingContext, outInstances, false);
			mappingContext.setCacheInstances(this, instances, outInstances, decCacheUseCnt,
					new EEntity[] { selfInterface.getElement1(null),
					selfInterface.getElement2(null) });
			return outInstances;
		}
	}

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		EPath_constraint selfInterface = (EPath_constraint)this;
    	return new EEntity[] { selfInterface.getElement1(null),
    			selfInterface.getElement2(null) };
    }

	/**
     * @since 4.1.0
     */
    public MatcherInstances findPathBackward(MappingContext mappingContext,
    		MatcherInstances instances, boolean decCacheUseCnt) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
								"This constraint can not be called as part of path backward references");
	}

} // CMappingPath_constraint
