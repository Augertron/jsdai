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

// Java class implementing entity data_type_declaration$implicit_declaration

package jsdai.dictionary;

import jsdai.lang.*;

public class CData_type_declaration$implicit_declaration extends CEntity implements EData_type_declaration, EImplicit_declaration {
  static jsdai.dictionary.CEntity_definition definition;

  /*----------------------------- Attributes -----------*/

  protected Object a0; // parent - non-java inheritance - ENTITY generic_schema_definition
  protected static jsdai.dictionary.CExplicit_attribute a0$;
  protected Object a1; // definition - non-java inheritance - SELECT declaration_type
  protected static jsdai.dictionary.CExplicit_attribute a1$;
  // parent_schema: protected Object  - derived - non-java inheritance -  ENTITY schema_definition
  protected static jsdai.dictionary.CDerived_attribute d0$;
  protected String a2; // alias_name - non-java inheritance - STRING
  protected static jsdai.dictionary.CExplicit_attribute a2$;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/

  // attribute: parent, base type: entity generic_schema_definition
  public static int usedinParent(EDeclaration type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testParent(EDeclaration type) throws SdaiException {
    return test_instance(a0);
  }

  public EGeneric_schema_definition getParent(EDeclaration type) throws SdaiException {
    a0 = get_instance(a0);
    return (EGeneric_schema_definition) a0;
  }

  public void setParent(EDeclaration type, EGeneric_schema_definition value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetParent(EDeclaration type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeParent(EDeclaration type) throws SdaiException {
    return a0$;
  }

  // methods for SELECT attribute: definition
  public static int usedinDefinition(EDeclaration type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testDefinition(EDeclaration type) throws SdaiException {
    return test_instance(a1);
  }

  public EEntity getDefinition(EDeclaration type) throws SdaiException { // case 1
    a1 = get_instance_select(a1);
    return (EEntity) a1;
  }

  public void setDefinition(EDeclaration type, EEntity value) throws SdaiException { // case 1
    a1 = set_instance(a1, value);
  }

  public void unsetDefinition(EDeclaration type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeDefinition(EDeclaration type) throws SdaiException {
    return a1$;
  }

  // derived attribute: parent_schema, base type: entity schema_definition
  public static int usedinParent_schema(EDeclaration type, ESchema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testParent_schema(EDeclaration type) throws SdaiException {
    return testParent_schemaInternal((EGeneric_schema_definition) a0);
  }

  public ESchema_definition getParent_schema(EDeclaration type) throws SdaiException {
    return getParent_schemaInternal((EGeneric_schema_definition) a0);
  }

  public static jsdai.dictionary.EAttribute attributeParent_schema(EDeclaration type) throws SdaiException {
    return d0$;
  }

  /// methods for attribute: alias_name, base type: STRING
  public boolean testAlias_name(EInterfaced_declaration type) throws SdaiException {
    return test_string(a2);
  }

  public String getAlias_name(EInterfaced_declaration type) throws SdaiException {
    return get_string(a2);
  }

  public void setAlias_name(EInterfaced_declaration type, String value) throws SdaiException {
    a2 = set_string(value);
  }

  public void unsetAlias_name(EInterfaced_declaration type) throws SdaiException {
    a2 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeAlias_name(EInterfaced_declaration type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      a2 = null;
      return;
    }
    a0 = av.entityValues[1].getInstance(0, this);
    a1 = av.entityValues[1].getInstance(1, this);
    a2 = av.entityValues[3].getString(0);
  }

  /*---------------------- getAll() --------------------*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type_declaration
    // partial entity: declaration
    av.entityValues[1].setInstance(0, a0);
    av.entityValues[1].setInstance(1, a1);
    // partial entity: implicit_declaration
    // partial entity: interfaced_declaration
    av.entityValues[3].setString(0, a2);
  }
}
