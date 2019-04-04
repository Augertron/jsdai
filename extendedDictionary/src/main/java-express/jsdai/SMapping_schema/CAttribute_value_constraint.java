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

// Java class implementing entity attribute_value_constraint

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class CAttribute_value_constraint extends CConstraint_attribute implements EAttribute_value_constraint {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAttribute_value_constraint.class, SMapping_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // attribute - current entity - SELECT attribute_value_constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // attribute - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1309=EXPLICIT_ATTRIBUTE('attribute',#1306,0,#1281,$,.F.);
  //<01> generating methods for consolidated attribute:  attribute
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: attribute
  public static int usedinAttribute(EAttribute_value_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testAttribute(EAttribute_value_constraint type) throws SdaiException {
    return test_instance(a0);
  }

  public EEntity getAttribute(EAttribute_value_constraint type) throws SdaiException { // case 1
    return get_instance_select(a0);
  }

  public void setAttribute(EAttribute_value_constraint type, EEntity value) throws SdaiException { // case 1
    a0 = set_instance(a0, value);
  }

  public void unsetAttribute(EAttribute_value_constraint type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeAttribute(EAttribute_value_constraint type) throws SdaiException {
    return a0$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_value_constraint
		av.entityValues[0].setInstance(0, a0);
		// partial entity: constraint
		// partial entity: constraint_attribute
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: attribute_value_constraint
    av.entityValues[0].setInstance(0, a0);
    // partial entity: constraint
    // partial entity: constraint_attribute
  }
}
