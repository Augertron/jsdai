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

// Java class implementing entity enumeration_constraint

package jsdai.mapping;

import jsdai.lang.*;

public class CEnumeration_constraint extends CAttribute_value_constraint implements EEnumeration_constraint {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CEnumeration_constraint.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	// attribute: protected Object a0;   attribute - java inheritance - SELECT attribute_value_constraint_select
	protected String a1; // constraint_value - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // attribute - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // constraint_value - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected String a1;

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

  //going through all the attributes: #484=EXPLICIT_ATTRIBUTE('attribute',#481,0,#456,$,.F.);
  //<01> generating methods for consolidated attribute:  attribute
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: attribute
  public static int usedinAttribute(EAttribute_value_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #517=EXPLICIT_ATTRIBUTE('constraint_value',#514,0,#33,$,.F.);
  //<01> generating methods for consolidated attribute:  constraint_value
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: constraint_value, base type: STRING
  public boolean testConstraint_value(EEnumeration_constraint type) throws SdaiException {
    return test_string(a1);
  }

  public String getConstraint_value(EEnumeration_constraint type) throws SdaiException {
    return get_string(a1);
  }

  public void setConstraint_value(EEnumeration_constraint type, String value) throws SdaiException {
    a1 = set_string(value);
  }

  public void unsetConstraint_value(EEnumeration_constraint type) throws SdaiException {
    a1 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeConstraint_value(EEnumeration_constraint type) throws SdaiException {
    return a1$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[3].getString(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[3].getString(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_value_constraint
		av.entityValues[0].setInstance(0, a0);
		// partial entity: constraint
		// partial entity: constraint_attribute
		// partial entity: enumeration_constraint
		av.entityValues[3].setString(0, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: attribute_value_constraint
    av.entityValues[0].setInstance(0, a0);
    // partial entity: constraint
    // partial entity: constraint_attribute
    // partial entity: enumeration_constraint
    av.entityValues[3].setString(0, a1);
  }
}
