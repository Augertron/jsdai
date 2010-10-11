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

import java.util.Map;
import java.util.Set;

import jsdai.lang.MappingContext.CachedInstances;
import jsdai.lang.MappingContext.ImmutableArraySet;
import jsdai.mapping.CAttribute_mapping_value;
import jsdai.mapping.EAttribute_mapping_value;

/**
 *
 * This class is a superclass of CAttribute_mapping_value subclasses in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @since 3.6.0
 */

public abstract class CMappingAttribute_mapping_value
	extends CAttribute_mapping_value implements MappingConstraintMatcher {

	protected CMappingAttribute_mapping_value() { }

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
			EAttribute_mapping_value selfInterface = (EAttribute_mapping_value)this;

			if(selfInterface.testConstraints(null)) {
				CachedInstances cacheInstances = mappingContext.getCacheInstances(this);
				outInstances =
					((MappingConstraintMatcher)selfInterface.getConstraints(null))
					.findForward(mappingContext, instances, cacheInstances == null);
			} else {
				outInstances = mappingContext.newAllInstances(MatcherInstances.STATUS_FORWARD);
			}
			Map outInstanceMap = outInstances.getInstanceMap();
			Set outInstanceMapKeys = outInstanceMap.keySet();
			if(outInstanceMap instanceof AEntityMap) {
				outInstanceMapKeys = new ImmutableArraySet(outInstanceMapKeys);
			}
			outInstances =
				outInstances.dup(new FixedValueMap(outInstanceMapKeys, getMappedValueObject()),
								 outInstances.status);
			mappingContext.setPersistentCacheInstances(this, instances, outInstances);
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
		return findForward(mappingContext, instances.dup(null, MatcherInstances.STATUS_FORWARD),
				false);
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

	/**
     * @since 4.1.0
     */
    public EEntity[] findDependentInstances() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
				"This constraint can not be called as part of path forward references: " +
				this);
    }

	protected abstract Object getMappedValueObject() throws SdaiException;

} // CMappingAttribute_mapping_value
