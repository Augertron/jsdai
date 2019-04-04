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

// Java class implementing aggregate of  sub_supertype_mapping of level 1

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class ASub_supertype_mapping extends AEntity {
  public ESub_supertype_mapping getByIndex(int index) throws SdaiException {
    return (ESub_supertype_mapping) getByIndexEntity(index);
  }

  public ESub_supertype_mapping getCurrentMember(SdaiIterator iter) throws SdaiException {
    return (ESub_supertype_mapping) getCurrentMemberObject(iter);
  }
}
