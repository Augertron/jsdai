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

// Java class implementing entity dependent_map_definition

package jsdai.dictionary;

import jsdai.lang.*;

public class CDependent_map_definition extends CEntity implements EDependent_map_definition {
  static jsdai.dictionary.CEntity_definition definition;

  /*----------------------------- Attributes -----------*/

  protected String a0; // name - non-java inheritance - STRING
  protected static jsdai.dictionary.CExplicit_attribute a0$;
  protected Object a1; // super_type - non-java inheritance - ENTITY map_definition
  protected static jsdai.dictionary.CExplicit_attribute a1$;
  // partitions: protected Object  - inverse - non-java inheritance -  ENTITY map_partition
  protected static jsdai.dictionary.CInverse_attribute i0$;
  // target_parameters: protected Object  - inverse - non-java inheritance -  ENTITY target_parameter
  protected static jsdai.dictionary.CInverse_attribute i1$;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/

  /// methods for attribute: name, base type: STRING
  public boolean testName(EMap_definition type) throws SdaiException {
    return test_string(a0);
  }

  public String getName(EMap_definition type) throws SdaiException {
    return get_string(a0);
  }

  public void setName(EMap_definition type, String value) throws SdaiException {
    a0 = set_string(value);
  }

  public void unsetName(EMap_definition type) throws SdaiException {
    a0 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeName(EMap_definition type) throws SdaiException {
    return a0$;
  }

  // attribute: super_type, base type: entity map_definition
  public static int usedinSuper_type(EMap_definition type, EMap_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testSuper_type(EMap_definition type) throws SdaiException {
    return test_instance(a1);
  }

  public EMap_definition getSuper_type(EMap_definition type) throws SdaiException {
    a1 = get_instance(a1);
    return (EMap_definition) a1;
  }

  public void setSuper_type(EMap_definition type, EMap_definition value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetSuper_type(EMap_definition type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeSuper_type(EMap_definition type) throws SdaiException {
    return a1$;
  }

  // Inverse attribute - partitions : SET[1:-2147483648] OF map_partition FOR parent
  public AMap_partition getPartitions(EMap_definition type, ASdaiModel domain) throws SdaiException {
    AMap_partition result = new AMap_partition();
    CMap_partition.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributePartitions(EMap_definition type) throws SdaiException {
    return i0$;
  }

  // Inverse attribute - target_parameters : SET[0:-2147483648] OF target_parameter FOR parent
  public ATarget_parameter getTarget_parameters(EMap_definition type, ASdaiModel domain) throws SdaiException {
    ATarget_parameter result = new ATarget_parameter();
    CTarget_parameter.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeTarget_parameters(EMap_definition type) throws SdaiException {
    return i1$;
  }


  /*---------------------- setAll() --------------------*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[1].getString(0);
    a1 = av.entityValues[1].getInstance(1, this);
  }

  /*---------------------- getAll() --------------------*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: dependent_map_definition
    // partial entity: map_definition
    av.entityValues[1].setString(0, a0);
    av.entityValues[1].setInstance(1, a1);
  }
}
