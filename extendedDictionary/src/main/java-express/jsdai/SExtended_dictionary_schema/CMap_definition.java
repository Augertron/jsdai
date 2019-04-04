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

// Java class implementing entity map_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CMap_definition extends CEntity implements EMap_definition {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CMap_definition.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // super_type - current entity - ENTITY map_definition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// partitions: protected Object  - inverse - current -  ENTITY map_partition
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
	// target_parameters: protected Object  - inverse - current -  ENTITY target_parameter
	protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected String a0;
  // super_type - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // partitions - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
  // target_parameters - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1730=EXPLICIT_ATTRIBUTE('name',#1728,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
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

  //going through all the attributes: #1731=EXPLICIT_ATTRIBUTE('super_type',#1728,1,#1728,$,.F.);
  //<01> generating methods for consolidated attribute:  super_type
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : super_type, base type: entity map_definition
  public static int usedinSuper_type(EMap_definition type, EMap_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testSuper_type(EMap_definition type) throws SdaiException {
    return test_instance(a1);
  }

  public EMap_definition getSuper_type(EMap_definition type) throws SdaiException {
    return (EMap_definition) get_instance(a1);
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

  //going through all the attributes: #1732=INVERSE_ATTRIBUTE('partitions',#1728,0,#1745,$,#1747,#2207,$,.F.);
  //<01> generating methods for consolidated attribute:  partitions
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - partitions : SET[1:-2147483648] OF map_partition FOR parent
  public AMap_partition getPartitions(EMap_definition type, ASdaiModel domain) throws SdaiException {
    AMap_partition result = (AMap_partition) get_inverse_aggregate(i0$);
    CMap_partition.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributePartitions(EMap_definition type) throws SdaiException {
    return i0$;
  }

  //going through all the attributes: #1733=INVERSE_ATTRIBUTE('target_parameters',#1728,1,#1839,$,#1842,#2209,$,.F.);
  //<01> generating methods for consolidated attribute:  target_parameters
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - target_parameters : SET[0:-2147483648] OF target_parameter FOR parent
  public ATarget_parameter getTarget_parameters(EMap_definition type, ASdaiModel domain) throws SdaiException {
    ATarget_parameter result = (ATarget_parameter) get_inverse_aggregate(i1$);
    CTarget_parameter.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeTarget_parameters(EMap_definition type) throws SdaiException {
    return i1$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: map_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: map_definition
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstance(1, a1);
  }
}
