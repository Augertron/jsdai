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

// Java class implementing entity intersection_constraint

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class CIntersection_constraint extends CConstraint implements EIntersection_constraint {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CIntersection_constraint.class, SMapping_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected AConstraint_select a0; // subpaths - current entity - SET OF SELECT
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // subpaths - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected AConstraint_select a0;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a0, old, newer);
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    changeReferencesAggregate(a0, old, newer);
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1503=EXPLICIT_ATTRIBUTE('subpaths',#1500,0,#2044,$,.F.);
  //<01> generating methods for consolidated attribute:  subpaths
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: subpaths, base type: SET OF SELECT
  public static int usedinSubpaths(EIntersection_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testSubpaths(EIntersection_constraint type) throws SdaiException {
    return test_aggregate(a0);
  }

  public AConstraint_select getSubpaths(EIntersection_constraint type) throws SdaiException {
    if (a0 == null) {
      throw new SdaiException(SdaiException.VA_NSET);
    }
    return a0;
  }

  public AConstraint_select createSubpaths(EIntersection_constraint type) throws SdaiException {
    a0 = (AConstraint_select) create_aggregate_class(a0, a0$, AConstraint_select.class, 0);
    return a0;
  }

  public void unsetSubpaths(EIntersection_constraint type) throws SdaiException {
    unset_aggregate(a0);
    a0 = null;
  }

  public static jsdai.dictionary.EAttribute attributeSubpaths(EIntersection_constraint type) throws SdaiException {
    return a0$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			if (a0 instanceof CAggregate)
				a0.unsetAll();
			a0 = null;
			return;
		}
		a0 = (AConstraint_select)av.entityValues[1].getInstanceAggregate(0, a0$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      if (a0 instanceof CAggregate) {
        a0.unsetAll();
      }
      a0 = null;
      return;
    }
    a0 = (AConstraint_select) av.entityValues[1].getInstanceAggregate(0, a0$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: constraint
		// partial entity: intersection_constraint
		av.entityValues[1].setInstanceAggregate(0, a0);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: constraint
    // partial entity: intersection_constraint
    av.entityValues[1].setInstanceAggregate(0, a0);
  }
}
