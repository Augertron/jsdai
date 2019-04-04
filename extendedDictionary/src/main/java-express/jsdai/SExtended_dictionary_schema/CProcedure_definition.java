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

// Java class implementing entity procedure_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CProcedure_definition extends CAlgorithm_definition implements EProcedure_definition {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CProcedure_definition.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// parameters: protected AParameter a1;   parameters - java inheritance - LIST OF ENTITY
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // parameters - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected AParameter a1;

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

  //going through all the attributes: #1547=EXPLICIT_ATTRIBUTE('name',#1545,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1548=EXPLICIT_ATTRIBUTE('parameters',#1545,1,#2045,$,.F.);
  //<01> generating methods for consolidated attribute:  parameters
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: parameters, base type: LIST OF ENTITY
  public static int usedinParameters(EAlgorithm_definition type, EParameter instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = (AParameter)av.entityValues[0].getInstanceAggregate(1, a1$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = (AParameter) av.entityValues[0].getInstanceAggregate(1, a1$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: algorithm_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstanceAggregate(1, a1);
		// partial entity: procedure_definition
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: algorithm_definition
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstanceAggregate(1, a1);
    // partial entity: procedure_definition
  }
}
