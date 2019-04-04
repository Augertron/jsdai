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

// Java interface for entity inverse_attribute_constraint

package jsdai.SMapping_schema;

import jsdai.lang.*;

public interface EInverse_attribute_constraint extends EConstraint {

  // generateExplicitAttributeMethodDeclarations: 1
  // constants and methods for SELECT attribute: inverted_attribute
  boolean testInverted_attribute(EInverse_attribute_constraint type) throws SdaiException;

  EEntity getInverted_attribute(EInverse_attribute_constraint type) throws SdaiException; // case 1

  void setInverted_attribute(EInverse_attribute_constraint type, EEntity value) throws SdaiException; // case 1

  void unsetInverted_attribute(EInverse_attribute_constraint type) throws SdaiException;

}
