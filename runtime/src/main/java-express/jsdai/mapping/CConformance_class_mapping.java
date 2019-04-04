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

// Java class implementing entity conformance_class_mapping

package jsdai.mapping;

import jsdai.lang.*;

public class CConformance_class_mapping extends CSchema_mapping implements EConformance_class_mapping {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CConformance_class_mapping.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	// source: protected Object a0;   source - java inheritance - ENTITY schema_definition
	// target: protected Object a1;   target - java inheritance - ENTITY schema_definition
	// uofs: protected AUof_mapping a2;   uofs - java inheritance - SET OF ENTITY
	// id: protected String a3;   id - java inheritance - STRING
	// components: protected ASchema_mapping a4;   components - java inheritance - SET OF ENTITY
*/

  /*----------------------------- Attributes (new version) -----------*/

  // source - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // target - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // uofs - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected AUof_mapping a2;
  // id - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  // protected String a3;
  // components - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  // protected ASchema_mapping a4;

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

  //going through all the attributes: #433=EXPLICIT_ATTRIBUTE('source',#430,0,#304,$,.F.);
  //<01> generating methods for consolidated attribute:  source
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): source, base type: entity schema_definition
  public static int usedinSource(ESchema_mapping type, jsdai.dictionary.ESchema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #435=EXPLICIT_ATTRIBUTE('target',#430,1,#304,$,.F.);
  //<01> generating methods for consolidated attribute:  target
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): target, base type: entity schema_definition
  public static int usedinTarget(ESchema_mapping type, jsdai.dictionary.ESchema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #437=EXPLICIT_ATTRIBUTE('uofs',#430,2,#877,$,.F.);
  //<01> generating methods for consolidated attribute:  uofs
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: uofs, base type: SET OF ENTITY
  public static int usedinUofs(ESchema_mapping type, EUof_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  //going through all the attributes: #439=EXPLICIT_ATTRIBUTE('id',#430,3,#7,$,.F.);
  //<01> generating methods for consolidated attribute:  id
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #441=EXPLICIT_ATTRIBUTE('components',#430,4,#878,$,.T.);
  //<01> generating methods for consolidated attribute:  components
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: components, base type: SET OF ENTITY
  public static int usedinComponents(ESchema_mapping type, ESchema_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a4$, domain, result);
  }

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a3 = null;
			if (a4 instanceof CAggregate)
				a4.unsetAll();
			a4 = null;
			return;
		}
		a0 = av.entityValues[1].getInstance(0, this, a0$);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
		a2 = (AUof_mapping)av.entityValues[1].getInstanceAggregate(2, a2$, this);
		a3 = av.entityValues[1].getString(3);
		a4 = (ASchema_mapping)av.entityValues[1].getInstanceAggregate(4, a4$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      if (a2 instanceof CAggregate) {
        a2.unsetAll();
      }
      a2 = null;
      a3 = null;
      if (a4 instanceof CAggregate) {
        a4.unsetAll();
      }
      a4 = null;
      return;
    }
    a0 = av.entityValues[1].getInstance(0, this, a0$);
    a1 = av.entityValues[1].getInstance(1, this, a1$);
    a2 = (AUof_mapping) av.entityValues[1].getInstanceAggregate(2, a2$, this);
    a3 = av.entityValues[1].getString(3);
    a4 = (ASchema_mapping) av.entityValues[1].getInstanceAggregate(4, a4$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: conformance_class_mapping
		// partial entity: schema_mapping
		av.entityValues[1].setInstance(0, a0);
		av.entityValues[1].setInstance(1, a1);
		av.entityValues[1].setInstanceAggregate(2, a2);
		av.entityValues[1].setString(3, a3);
		av.entityValues[1].setInstanceAggregate(4, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: conformance_class_mapping
    // partial entity: schema_mapping
    av.entityValues[1].setInstance(0, a0);
    av.entityValues[1].setInstance(1, a1);
    av.entityValues[1].setInstanceAggregate(2, a2);
    av.entityValues[1].setString(3, a3);
    av.entityValues[1].setInstanceAggregate(4, a4);
  }
}
