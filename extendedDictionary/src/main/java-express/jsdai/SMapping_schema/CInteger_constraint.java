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

// Java class implementing entity integer_constraint

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class CInteger_constraint extends CAttribute_value_constraint implements EInteger_constraint {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CInteger_constraint.class, SMapping_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// attribute: protected Object a0;   attribute - java inheritance - SELECT attribute_value_constraint_select
	protected int a1; // constraint_value - current entity - INTEGER
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // attribute - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // constraint_value - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected int a1;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1309=EXPLICIT_ATTRIBUTE('attribute',#1306,0,#1281,$,.F.);
  //<01> generating methods for consolidated attribute:  attribute
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: attribute
  public static int usedinAttribute(EAttribute_value_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #1353=EXPLICIT_ATTRIBUTE('constraint_value',#1350,0,#2,$,.F.);
  //<01> generating methods for consolidated attribute:  constraint_value
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: constraint_value, base type: INTEGER
  public boolean testConstraint_value(EInteger_constraint type) throws SdaiException {
    return test_integer(a1);
  }

  public int getConstraint_value(EInteger_constraint type) throws SdaiException {
    return get_integer(a1);
  }

  public void setConstraint_value(EInteger_constraint type, int value) throws SdaiException {
    a1 = set_integer(value);
  }

  public void unsetConstraint_value(EInteger_constraint type) throws SdaiException {
    a1 = unset_integer();
  }

  public static jsdai.dictionary.EAttribute attributeConstraint_value(EInteger_constraint type) throws SdaiException {
    return a1$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = Integer.MIN_VALUE;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[3].getInteger(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = Integer.MIN_VALUE;
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[3].getInteger(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_value_constraint
		av.entityValues[0].setInstance(0, a0);
		// partial entity: constraint
		// partial entity: constraint_attribute
		// partial entity: integer_constraint
		av.entityValues[3].setInteger(0, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: attribute_value_constraint
    av.entityValues[0].setInstance(0, a0);
    // partial entity: constraint
    // partial entity: constraint_attribute
    // partial entity: integer_constraint
    av.entityValues[3].setInteger(0, a1);
  }
}
