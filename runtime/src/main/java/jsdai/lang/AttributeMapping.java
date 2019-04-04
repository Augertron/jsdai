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
 * This class is part of attribute mapping implementation. It is intended
 * for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public class AttributeMapping extends EntityMapping {
// 	AttributeMapping(ASdaiModel target, ASdaiModel mapping) throws SdaiException {
// 		super(target, mapping, null);
// 	}

  /**
   * Creates a new <code>AttributeMapping</code>.
   *
   * @param session          <code>SdaiSession</code> to attach to
   * @param target           The target data domain
   * @param mapping          The mapping domain
   * @param touchedInstances The output parameter: the instances touched by
   *                         mapping operations
   * @throws SdaiException if an error occurs during the operation
   *                       or in underlying JSDAI operations
   */
  public AttributeMapping(SdaiSession session, ASdaiModel target, ASdaiModel mapping, java.util.Set touchedInstances) throws SdaiException {
    super();
    initialize(session, target, mapping, touchedInstances);
  }

}
