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

// Java class implementing entity exact_type_constraint

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class CExact_type_constraint extends CType_constraint implements EExact_type_constraint {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExact_type_constraint.class, SMapping_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// domain: protected Object a0;   domain - java inheritance - ENTITY entity_definition
	// constraints: protected Object a1;   constraints - java inheritance - SELECT constraint_select
*/

  /*----------------------------- Attributes (new version) -----------*/

  // domain - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // constraints - explicit - java inheritance
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

  //going through all the attributes: #1493=EXPLICIT_ATTRIBUTE('domain',#1490,0,#1633,$,.F.);
  //<01> generating methods for consolidated attribute:  domain
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): domain, base type: entity entity_definition
  public static int usedinDomain(EType_constraint type, jsdai.SExtended_dictionary_schema.EEntity_definition instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #1495=EXPLICIT_ATTRIBUTE('constraints',#1490,1,#1284,$,.T.);
  //<01> generating methods for consolidated attribute:  constraints
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: constraints
  public static int usedinConstraints(EType_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
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
		a1 = av.entityValues[2].getInstance(1, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[2].getInstance(0, this, a0$);
    a1 = av.entityValues[2].getInstance(1, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: constraint
		// partial entity: exact_type_constraint
		// partial entity: type_constraint
		av.entityValues[2].setInstance(0, a0);
		av.entityValues[2].setInstance(1, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: constraint
    // partial entity: exact_type_constraint
    // partial entity: type_constraint
    av.entityValues[2].setInstance(0, a0);
    av.entityValues[2].setInstance(1, a1);
  }
}
