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

import jsdai.mapping.CConstraint_attribute;
import jsdai.mapping.EAttribute_value_constraint;

/**
 *
 * This class is a superclass of CAttribute_value_constraint class in package jsdai.mapping.
 * It holds additional functionality needed by mapping operations.
 * It is intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @since 3.6.0
 */

public class CMappingAttribute_value_constraint extends CConstraint_attribute
	implements MappingConstraintMatcher {

	protected CMappingAttribute_value_constraint() { }

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
			EAttribute_value_constraint selfInterface = (EAttribute_value_constraint)this;
			MappingConstraintMatcher attrMatcher =
				(MappingConstraintMatcher)selfInterface.getAttribute(null);
			attrMatcher.findForward(mappingContext, instances, false);
			outInstances = mappingContext.getCacheInstances(this, instances, decCacheUseCnt);
			if(outInstances == null) {
				throw new SdaiException(SdaiException.SY_ERR, "Unsupported constraint: " + this);
			}
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
		EAttribute_value_constraint selfInterface = (EAttribute_value_constraint)this;
		return new EEntity[] { selfInterface.getAttribute(null) };
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

} // CMappingAttribute_value_constraint
