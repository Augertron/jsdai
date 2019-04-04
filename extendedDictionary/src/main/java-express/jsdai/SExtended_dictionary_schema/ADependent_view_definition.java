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

// Java class implementing aggregate of  dependent_view_definition of level 1

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class ADependent_view_definition extends AEntity {
  public EDependent_view_definition getByIndex(int index) throws SdaiException {
    return (EDependent_view_definition) getByIndexEntity(index);
  }

  public EDependent_view_definition getCurrentMember(SdaiIterator iter) throws SdaiException {
    return (EDependent_view_definition) getCurrentMemberObject(iter);
  }
}