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

/**
 * This class is part of entity mapping implementation. It is intended
 * for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public class EntityMapping extends ObjectMapping {

   EntityMapping() { }

//    EntityMapping(ASdaiModel data, ASdaiModel mapping) throws SdaiException {
//       super(data, mapping, null);
//    }

	/**
	 * Creates a new <code>EntityMapping</code>.
	 *
	 * @param session <code>SdaiSession</code> to attach to
	 * @param data The target data domain
	 * @param mapping The mapping domain
	 * @param touchedInstances The output parameter: the instances touched by
	 *                         mapping operations
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public EntityMapping(SdaiSession session, ASdaiModel data, ASdaiModel mapping, 
						java.util.Set touchedInstances) throws SdaiException {
	   super();
	   initialize(session, data, mapping, touchedInstances);
   }

}

