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

// Java class implementing entity and_constraint_relationship

package jsdai.mapping;

import jsdai.lang.*;

public class CAnd_constraint_relationship extends CMappingAnd_constraint_relationship implements EAnd_constraint_relationship {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAnd_constraint_relationship.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	// element2: protected Object a0;   element2 - java inheritance - SELECT constraint_select
	// element1: protected Object a1;   element1 - java inheritance - SELECT constraint_select
*/

  /*----------------------------- Attributes (new version) -----------*/

  // element2 - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // element1 - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;

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

  //going through all the attributes: #505=EXPLICIT_ATTRIBUTE('element2',#502,0,#459,$,.F.);
  //<01> generating methods for consolidated attribute:  element2
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: element2
  public static int usedinElement2(EConstraint_relationship type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #546=EXPLICIT_ATTRIBUTE('element1',#543,0,#459,$,.F.);
  //<01> generating methods for consolidated attribute:  element1
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: element1
  public static int usedinElement1(EInstance_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[2].getInstance(0, this, a0$);
		a1 = av.entityValues[3].getInstance(0, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[2].getInstance(0, this, a0$);
    a1 = av.entityValues[3].getInstance(0, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: and_constraint_relationship
		// partial entity: constraint
		// partial entity: constraint_relationship
		av.entityValues[2].setInstance(0, a0);
		// partial entity: instance_constraint
		av.entityValues[3].setInstance(0, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: and_constraint_relationship
    // partial entity: constraint
    // partial entity: constraint_relationship
    av.entityValues[2].setInstance(0, a0);
    // partial entity: instance_constraint
    av.entityValues[3].setInstance(0, a1);
  }
}
