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

// Java class implementing entity integer_bound

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CInteger_bound extends CBound implements EInteger_bound {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CInteger_bound.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// bound_value: protected int a0;   bound_value - java inheritance - INTEGER
*/

  /*----------------------------- Attributes (new version) -----------*/

  // bound_value - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected int a0;

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

  //going through all the attributes: #1579=EXPLICIT_ATTRIBUTE('bound_value',#1577,0,#2,$,.F.);
  //<01> generating methods for consolidated attribute:  bound_value
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = Integer.MIN_VALUE;
			return;
		}
		a0 = av.entityValues[0].getInteger(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = Integer.MIN_VALUE;
      return;
    }
    a0 = av.entityValues[0].getInteger(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: bound
		av.entityValues[0].setInteger(0, a0);
		// partial entity: integer_bound
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: bound
    av.entityValues[0].setInteger(0, a0);
    // partial entity: integer_bound
  }
}
