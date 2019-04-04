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

// Java interface for entity generic_attribute_mapping

package jsdai.SMapping_schema;

import jsdai.lang.*;

public interface EGeneric_attribute_mapping extends EEntity {

  // generateExplicitAttributeMethodDeclarations: 1
  // attribute:parent_entity, base type: entity entity_mapping
  public boolean testParent_entity(EGeneric_attribute_mapping type) throws SdaiException;

  public EEntity_mapping getParent_entity(EGeneric_attribute_mapping type) throws SdaiException;

  public void setParent_entity(EGeneric_attribute_mapping type, EEntity_mapping value) throws SdaiException;

  public void unsetParent_entity(EGeneric_attribute_mapping type) throws SdaiException;

  // generateExplicitAttributeMethodDeclarations: 1
  // attribute:source, base type: entity attribute
  public boolean testSource(EGeneric_attribute_mapping type) throws SdaiException;

  public jsdai.SExtended_dictionary_schema.EAttribute getSource(EGeneric_attribute_mapping type) throws SdaiException;

  public void setSource(EGeneric_attribute_mapping type, jsdai.SExtended_dictionary_schema.EAttribute value) throws SdaiException;

  public void unsetSource(EGeneric_attribute_mapping type) throws SdaiException;

  // generateExplicitAttributeMethodDeclarations: 1
  // constants and methods for SELECT attribute: constraints
  boolean testConstraints(EGeneric_attribute_mapping type) throws SdaiException;

  EEntity getConstraints(EGeneric_attribute_mapping type) throws SdaiException; // case 1

  void setConstraints(EGeneric_attribute_mapping type, EEntity value) throws SdaiException; // case 1

  void unsetConstraints(EGeneric_attribute_mapping type) throws SdaiException;

  // generateExplicitAttributeMethodDeclarations: 1
  // methods for attribute: data_type, base type: LIST OF ENTITY
  public boolean testData_type(EGeneric_attribute_mapping type) throws SdaiException;

  public jsdai.SExtended_dictionary_schema.ANamed_type getData_type(EGeneric_attribute_mapping type) throws SdaiException;

  public jsdai.SExtended_dictionary_schema.ANamed_type createData_type(EGeneric_attribute_mapping type) throws SdaiException;

  public void unsetData_type(EGeneric_attribute_mapping type) throws SdaiException;

  // generateExplicitAttributeMethodDeclarations: 1
  /// methods for attribute:strong, base type: BOOLEAN
  public boolean testStrong(EGeneric_attribute_mapping type) throws SdaiException;

  public boolean getStrong(EGeneric_attribute_mapping type) throws SdaiException;

  public void setStrong(EGeneric_attribute_mapping type, boolean value) throws SdaiException;

  public void unsetStrong(EGeneric_attribute_mapping type) throws SdaiException;

}
