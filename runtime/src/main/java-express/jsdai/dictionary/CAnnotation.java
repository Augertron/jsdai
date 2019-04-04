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

// Java class implementing entity annotation

package jsdai.dictionary;

import jsdai.lang.*;

public class CAnnotation extends CEntity implements EAnnotation {
  static jsdai.dictionary.CEntity_definition definition;

  /*----------------------------- Attributes -----------*/

  protected Object a0; // target - current entity - SELECT documentation_object
  protected static jsdai.dictionary.CExplicit_attribute a0$;
  protected A_string a1; // values - current entity - LIST OF STRING
  protected static jsdai.dictionary.CExplicit_attribute a1$;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/

  // methods for SELECT attribute: target
  public static int usedinTarget(EAnnotation type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testTarget(EAnnotation type) throws SdaiException {
    return test_instance(a0);
  }

  public jsdai.lang.EEntity getTarget(EAnnotation type) throws jsdai.lang.SdaiException { // case 1
    a0 = get_instance_select(a0);
    return (jsdai.lang.EEntity) a0;
  }

  public void setTarget(EAnnotation type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
    a0 = set_instance(a0, value);
  }

  public void unsetTarget(EAnnotation type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeTarget(EAnnotation type) throws SdaiException {
    return a0$;
  }

  // methods for attribute: values, base type: LIST OF STRING
  public boolean testValues(EAnnotation type) throws SdaiException {
    return test_aggregate(a1);
  }

  public A_string getValues(EAnnotation type) throws SdaiException {
    return (A_string) get_aggregate(a1);
  }

  public A_string createValues(EAnnotation type) throws SdaiException {
    a1 = create_aggregate_string(a1, a1$, 0);
    return a1;
  }

  public void unsetValues(EAnnotation type) throws SdaiException {
    unset_aggregate(a1);
    a1 = null;
  }

  public static jsdai.dictionary.EAttribute attributeValues(EAnnotation type) throws SdaiException {
    return a1$;
  }


  /*---------------------- setAll() --------------------*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this);
    a1 = av.entityValues[0].getStringAggregate(1, a1$, this);
  }

  /*---------------------- getAll() --------------------*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: annotation
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setStringAggregate(1, a1);
  }
}
