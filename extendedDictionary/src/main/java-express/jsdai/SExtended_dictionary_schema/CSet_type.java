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

// Java class implementing entity set_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CSet_type extends CVariable_size_aggregation_type implements ESet_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSet_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// element_type: protected Object a1;   element_type - java inheritance - ENTITY data_type
	// lower_bound: protected Object a2;   lower_bound - java inheritance - ENTITY bound
	// upper_bound: protected Object a3;   upper_bound - java inheritance - ENTITY bound
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // element_type - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // lower_bound - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected Object a2;
  // upper_bound - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  // protected Object a3;

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

  //going through all the attributes: #1860=EXPLICIT_ATTRIBUTE('lower_bound',#1858,0,#1577,$,.T.);
  //<01> generating methods for consolidated attribute:  lower_bound
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): lower_bound, base type: entity bound
  public static int usedinLower_bound(EVariable_size_aggregation_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  //going through all the attributes: #1861=EXPLICIT_ATTRIBUTE('upper_bound',#1858,1,#1577,$,.T.);
  //<01> generating methods for consolidated attribute:  upper_bound
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): upper_bound, base type: entity bound
  public static int usedinUpper_bound(EVariable_size_aggregation_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a3$, domain, result);
  }

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a1 = unset_instance(a1);
			a0 = null;
			a2 = unset_instance(a2);
			a3 = unset_instance(a3);
			return;
		}
		a1 = av.entityValues[0].getInstance(0, this, a1$);
		a0 = av.entityValues[1].getString(0);
		a2 = av.entityValues[3].getInstance(0, this, a2$);
		a3 = av.entityValues[3].getInstance(1, this, a3$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a1 = unset_instance(a1);
      a0 = null;
      a2 = unset_instance(a2);
      a3 = unset_instance(a3);
      return;
    }
    a1 = av.entityValues[0].getInstance(0, this, a1$);
    a0 = av.entityValues[1].getString(0);
    a2 = av.entityValues[3].getInstance(0, this, a2$);
    a3 = av.entityValues[3].getInstance(1, this, a3$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: aggregation_type
		av.entityValues[0].setInstance(0, a1);
		// partial entity: data_type
		av.entityValues[1].setString(0, a0);
		// partial entity: set_type
		// partial entity: variable_size_aggregation_type
		av.entityValues[3].setInstance(0, a2);
		av.entityValues[3].setInstance(1, a3);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: aggregation_type
    av.entityValues[0].setInstance(0, a1);
    // partial entity: data_type
    av.entityValues[1].setString(0, a0);
    // partial entity: set_type
    // partial entity: variable_size_aggregation_type
    av.entityValues[3].setInstance(0, a2);
    av.entityValues[3].setInstance(1, a3);
  }
}
