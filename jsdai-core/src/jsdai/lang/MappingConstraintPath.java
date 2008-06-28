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

/**
 * This interface defines the mapping operations behaviour. It is meant for
 * mapping operations internal use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public interface MappingConstraintPath {

	/**
	 * Makes a step in resolving the result of {@link EEntity#findMappedUsers}
	 * result in a forward direction.
	 *
	 * @param mappingContext <code>ObjectMapping</code> as this operation context
	 * @param instances input and output instance set to work with
	 * @exception SdaiException if an error occurs
	 */
	public void mapUsersForward(ObjectMapping mappingContext, Set instances) throws SdaiException;

	/**
	 * Makes a step in resolving the result of {@link EEntity#findMappedUsers}
	 * result in a backward direction.
	 *
	 * @param mappingContext <code>ObjectMapping</code> as this operation context
	 * @param instances input and output instance set to work with
	 * @exception SdaiException if an error occurs
	 */
	public void mapUsersBackward(ObjectMapping mappingContext, Set instances) throws SdaiException;
	
} // MappingConstraintPath
