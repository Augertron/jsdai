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

// Java class implementing entity aggregate_target_parameter

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CAggregate_target_parameter extends CTarget_parameter implements EAggregate_target_parameter {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAggregate_target_parameter.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// parent: protected Object a1;   parent - java inheritance - ENTITY map_definition
	// extent: protected Object a2;   extent - java inheritance - ENTITY entity_definition
	// order: protected int a3;   order - java inheritance - INTEGER
	protected Object a4; // lower_bound - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
	protected Object a5; // upper_bound - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // parent - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // extent - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected Object a2;
  // order - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  // protected int a3;
  // lower_bound - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  protected Object a4;
  // upper_bound - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
  protected Object a5;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a4 == old) {
			a4 = newer;
		}
		if (a5 == old) {
			a5 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    if (a4 == old) {
      a4 = newer;
    }
    if (a5 == old) {
      a5 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1841=EXPLICIT_ATTRIBUTE('name',#1839,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1842=EXPLICIT_ATTRIBUTE('parent',#1839,1,#1728,$,.F.);
  //<01> generating methods for consolidated attribute:  parent
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): parent, base type: entity map_definition
  public static int usedinParent(ETarget_parameter type, EMap_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1843=EXPLICIT_ATTRIBUTE('extent',#1839,2,#1633,$,.F.);
  //<01> generating methods for consolidated attribute:  extent
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): extent, base type: entity entity_definition
  public static int usedinExtent(ETarget_parameter type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  //going through all the attributes: #1844=EXPLICIT_ATTRIBUTE('order',#1839,3,#2,$,.F.);
  //<01> generating methods for consolidated attribute:  order
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1538=EXPLICIT_ATTRIBUTE('lower_bound',#1536,0,#1577,$,.T.);
  //<01> generating methods for consolidated attribute:  lower_bound
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : lower_bound, base type: entity bound
  public static int usedinLower_bound(EAggregate_target_parameter type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a4$, domain, result);
  }

  public boolean testLower_bound(EAggregate_target_parameter type) throws SdaiException {
    return test_instance(a4);
  }

  public EBound getLower_bound(EAggregate_target_parameter type) throws SdaiException {
    return (EBound) get_instance(a4);
  }

  public void setLower_bound(EAggregate_target_parameter type, EBound value) throws SdaiException {
    a4 = set_instance(a4, value);
  }

  public void unsetLower_bound(EAggregate_target_parameter type) throws SdaiException {
    a4 = unset_instance(a4);
  }

  public static jsdai.dictionary.EAttribute attributeLower_bound(EAggregate_target_parameter type) throws SdaiException {
    return a4$;
  }

  //going through all the attributes: #1539=EXPLICIT_ATTRIBUTE('upper_bound',#1536,1,#1577,$,.T.);
  //<01> generating methods for consolidated attribute:  upper_bound
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : upper_bound, base type: entity bound
  public static int usedinUpper_bound(EAggregate_target_parameter type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a5$, domain, result);
  }

  public boolean testUpper_bound(EAggregate_target_parameter type) throws SdaiException {
    return test_instance(a5);
  }

  public EBound getUpper_bound(EAggregate_target_parameter type) throws SdaiException {
    return (EBound) get_instance(a5);
  }

  public void setUpper_bound(EAggregate_target_parameter type, EBound value) throws SdaiException {
    a5 = set_instance(a5, value);
  }

  public void unsetUpper_bound(EAggregate_target_parameter type) throws SdaiException {
    a5 = unset_instance(a5);
  }

  public static jsdai.dictionary.EAttribute attributeUpper_bound(EAggregate_target_parameter type) throws SdaiException {
    return a5$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a4 = unset_instance(a4);
			a5 = unset_instance(a5);
			a0 = null;
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			a3 = Integer.MIN_VALUE;
			return;
		}
		a4 = av.entityValues[0].getInstance(0, this, a4$);
		a5 = av.entityValues[0].getInstance(1, this, a5$);
		a0 = av.entityValues[1].getString(0);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
		a2 = av.entityValues[1].getInstance(2, this, a2$);
		a3 = av.entityValues[1].getInteger(3);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a4 = unset_instance(a4);
      a5 = unset_instance(a5);
      a0 = null;
      a1 = unset_instance(a1);
      a2 = unset_instance(a2);
      a3 = Integer.MIN_VALUE;
      return;
    }
    a4 = av.entityValues[0].getInstance(0, this, a4$);
    a5 = av.entityValues[0].getInstance(1, this, a5$);
    a0 = av.entityValues[1].getString(0);
    a1 = av.entityValues[1].getInstance(1, this, a1$);
    a2 = av.entityValues[1].getInstance(2, this, a2$);
    a3 = av.entityValues[1].getInteger(3);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: aggregate_target_parameter
		av.entityValues[0].setInstance(0, a4);
		av.entityValues[0].setInstance(1, a5);
		// partial entity: target_parameter
		av.entityValues[1].setString(0, a0);
		av.entityValues[1].setInstance(1, a1);
		av.entityValues[1].setInstance(2, a2);
		av.entityValues[1].setInteger(3, a3);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: aggregate_target_parameter
    av.entityValues[0].setInstance(0, a4);
    av.entityValues[0].setInstance(1, a5);
    // partial entity: target_parameter
    av.entityValues[1].setString(0, a0);
    av.entityValues[1].setInstance(1, a1);
    av.entityValues[1].setInstance(2, a2);
    av.entityValues[1].setInteger(3, a3);
  }
}
