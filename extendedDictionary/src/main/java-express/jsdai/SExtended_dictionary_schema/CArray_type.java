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

// Java class implementing entity array_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CArray_type extends CAggregation_type implements EArray_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CArray_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// element_type: protected Object a1;   element_type - java inheritance - ENTITY data_type
	protected Object a2; // lower_index - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected Object a3; // upper_index - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected int a4; // unique_flag - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
	protected int a5; // optional_flag - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // element_type - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // lower_index - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;
  // upper_index - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected Object a3;
  // unique_flag - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  protected int a4;
  // optional_flag - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
  protected int a5;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a2 == old) {
			a2 = newer;
		}
		if (a3 == old) {
			a3 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    if (a2 == old) {
      a2 = newer;
    }
    if (a3 == old) {
      a3 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1589=EXPLICIT_ATTRIBUTE('name',#1587,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1542=EXPLICIT_ATTRIBUTE('element_type',#1540,0,#1587,$,.F.);
  //<01> generating methods for consolidated attribute:  element_type
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): element_type, base type: entity data_type
  public static int usedinElement_type(EAggregation_type type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1559=EXPLICIT_ATTRIBUTE('lower_index',#1557,0,#1577,$,.T.);
  //<01> generating methods for consolidated attribute:  lower_index
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : lower_index, base type: entity bound
  public static int usedinLower_index(EArray_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testLower_index(EArray_type type) throws SdaiException {
    return test_instance(a2);
  }

  public EBound getLower_index(EArray_type type) throws SdaiException {
    return (EBound) get_instance(a2);
  }

  public void setLower_index(EArray_type type, EBound value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetLower_index(EArray_type type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeLower_index(EArray_type type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1560=EXPLICIT_ATTRIBUTE('upper_index',#1557,1,#1577,$,.T.);
  //<01> generating methods for consolidated attribute:  upper_index
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : upper_index, base type: entity bound
  public static int usedinUpper_index(EArray_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a3$, domain, result);
  }

  public boolean testUpper_index(EArray_type type) throws SdaiException {
    return test_instance(a3);
  }

  public EBound getUpper_index(EArray_type type) throws SdaiException {
    return (EBound) get_instance(a3);
  }

  public void setUpper_index(EArray_type type, EBound value) throws SdaiException {
    a3 = set_instance(a3, value);
  }

  public void unsetUpper_index(EArray_type type) throws SdaiException {
    a3 = unset_instance(a3);
  }

  public static jsdai.dictionary.EAttribute attributeUpper_index(EArray_type type) throws SdaiException {
    return a3$;
  }

  //going through all the attributes: #1561=EXPLICIT_ATTRIBUTE('unique_flag',#1557,2,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  unique_flag
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: unique_flag, base type: BOOLEAN
  public boolean testUnique_flag(EArray_type type) throws SdaiException {
    return test_boolean(a4);
  }

  public boolean getUnique_flag(EArray_type type) throws SdaiException {
    return get_boolean(a4);
  }

  public void setUnique_flag(EArray_type type, boolean value) throws SdaiException {
    a4 = set_boolean(value);
  }

  public void unsetUnique_flag(EArray_type type) throws SdaiException {
    a4 = unset_boolean();
  }

  public static jsdai.dictionary.EAttribute attributeUnique_flag(EArray_type type) throws SdaiException {
    return a4$;
  }

  //going through all the attributes: #1562=EXPLICIT_ATTRIBUTE('optional_flag',#1557,3,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  optional_flag
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: optional_flag, base type: BOOLEAN
  public boolean testOptional_flag(EArray_type type) throws SdaiException {
    return test_boolean(a5);
  }

  public boolean getOptional_flag(EArray_type type) throws SdaiException {
    return get_boolean(a5);
  }

  public void setOptional_flag(EArray_type type, boolean value) throws SdaiException {
    a5 = set_boolean(value);
  }

  public void unsetOptional_flag(EArray_type type) throws SdaiException {
    a5 = unset_boolean();
  }

  public static jsdai.dictionary.EAttribute attributeOptional_flag(EArray_type type) throws SdaiException {
    return a5$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			a3 = unset_instance(a3);
			a4 = 0;
			a5 = 0;
			a0 = null;
			return;
		}
		a1 = av.entityValues[0].getInstance(0, this, a1$);
		a2 = av.entityValues[1].getInstance(0, this, a2$);
		a3 = av.entityValues[1].getInstance(1, this, a3$);
		a4 = av.entityValues[1].getBoolean(2);
		a5 = av.entityValues[1].getBoolean(3);
		a0 = av.entityValues[2].getString(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a1 = unset_instance(a1);
      a2 = unset_instance(a2);
      a3 = unset_instance(a3);
      a4 = 0;
      a5 = 0;
      a0 = null;
      return;
    }
    a1 = av.entityValues[0].getInstance(0, this, a1$);
    a2 = av.entityValues[1].getInstance(0, this, a2$);
    a3 = av.entityValues[1].getInstance(1, this, a3$);
    a4 = av.entityValues[1].getBoolean(2);
    a5 = av.entityValues[1].getBoolean(3);
    a0 = av.entityValues[2].getString(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: aggregation_type
		av.entityValues[0].setInstance(0, a1);
		// partial entity: array_type
		av.entityValues[1].setInstance(0, a2);
		av.entityValues[1].setInstance(1, a3);
		av.entityValues[1].setBoolean(2, a4);
		av.entityValues[1].setBoolean(3, a5);
		// partial entity: data_type
		av.entityValues[2].setString(0, a0);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: aggregation_type
    av.entityValues[0].setInstance(0, a1);
    // partial entity: array_type
    av.entityValues[1].setInstance(0, a2);
    av.entityValues[1].setInstance(1, a3);
    av.entityValues[1].setBoolean(2, a4);
    av.entityValues[1].setBoolean(3, a5);
    // partial entity: data_type
    av.entityValues[2].setString(0, a0);
  }
}
