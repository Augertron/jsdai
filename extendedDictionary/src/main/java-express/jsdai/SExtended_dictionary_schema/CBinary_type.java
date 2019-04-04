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

// Java class implementing entity binary_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CBinary_type extends CSimple_type implements EBinary_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CBinary_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	protected Object a1; // width - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected int a2; // fixed_width - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // width - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // fixed_width - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected int a2;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a1 == old) {
			a1 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1589=EXPLICIT_ATTRIBUTE('name',#1587,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1573=EXPLICIT_ATTRIBUTE('width',#1571,0,#1577,$,.T.);
  //<01> generating methods for consolidated attribute:  width
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : width, base type: entity bound
  public static int usedinWidth(EBinary_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testWidth(EBinary_type type) throws SdaiException {
    return test_instance(a1);
  }

  public EBound getWidth(EBinary_type type) throws SdaiException {
    return (EBound) get_instance(a1);
  }

  public void setWidth(EBinary_type type, EBound value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetWidth(EBinary_type type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeWidth(EBinary_type type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1574=EXPLICIT_ATTRIBUTE('fixed_width',#1571,1,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  fixed_width
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: fixed_width, base type: BOOLEAN
  public boolean testFixed_width(EBinary_type type) throws SdaiException {
    return test_boolean(a2);
  }

  public boolean getFixed_width(EBinary_type type) throws SdaiException {
    return get_boolean(a2);
  }

  public void setFixed_width(EBinary_type type, boolean value) throws SdaiException {
    a2 = set_boolean(value);
  }

  public void unsetFixed_width(EBinary_type type) throws SdaiException {
    a2 = unset_boolean();
  }

  public static jsdai.dictionary.EAttribute attributeFixed_width(EBinary_type type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a1 = unset_instance(a1);
			a2 = 0;
			a0 = null;
			return;
		}
		a1 = av.entityValues[0].getInstance(0, this, a1$);
		a2 = av.entityValues[0].getBoolean(1);
		a0 = av.entityValues[1].getString(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a1 = unset_instance(a1);
      a2 = 0;
      a0 = null;
      return;
    }
    a1 = av.entityValues[0].getInstance(0, this, a1$);
    a2 = av.entityValues[0].getBoolean(1);
    a0 = av.entityValues[1].getString(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: binary_type
		av.entityValues[0].setInstance(0, a1);
		av.entityValues[0].setBoolean(1, a2);
		// partial entity: data_type
		av.entityValues[1].setString(0, a0);
		// partial entity: simple_type
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: binary_type
    av.entityValues[0].setInstance(0, a1);
    av.entityValues[0].setBoolean(1, a2);
    // partial entity: data_type
    av.entityValues[1].setString(0, a0);
    // partial entity: simple_type
  }
}
