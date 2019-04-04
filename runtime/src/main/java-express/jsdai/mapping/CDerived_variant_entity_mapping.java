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

// Java class implementing entity derived_variant_entity_mapping

package jsdai.mapping;

import jsdai.lang.*;

public class CDerived_variant_entity_mapping extends CEntity_mapping_relationship implements EDerived_variant_entity_mapping {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDerived_variant_entity_mapping.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	// relating: protected Object a0;   relating - java inheritance - ENTITY entity_mapping
	// related: protected Object a1;   related - java inheritance - ENTITY entity_mapping
	// constraints: protected Object a2;   constraints - java inheritance - SELECT constraint_select
	// path: protected AAttribute_mapping_path_select a3;   path - java inheritance - LIST OF SELECT
*/

  /*----------------------------- Attributes (new version) -----------*/

  // relating - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // related - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // constraints - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected Object a2;
  // path - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  // protected AAttribute_mapping_path_select a3;

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

  //going through all the attributes: #651=EXPLICIT_ATTRIBUTE('relating',#648,0,#417,$,.F.);
  //<01> generating methods for consolidated attribute:  relating
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): relating, base type: entity entity_mapping
  public static int usedinRelating(EEntity_mapping_relationship type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #653=EXPLICIT_ATTRIBUTE('related',#648,1,#417,$,.F.);
  //<01> generating methods for consolidated attribute:  related
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): related, base type: entity entity_mapping
  public static int usedinRelated(EEntity_mapping_relationship type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #655=EXPLICIT_ATTRIBUTE('constraints',#648,2,#459,$,.T.);
  //<01> generating methods for consolidated attribute:  constraints
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: constraints
  public static int usedinConstraints(EEntity_mapping_relationship type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  //going through all the attributes: #657=EXPLICIT_ATTRIBUTE('path',#648,3,#883,$,.T.);
  //<01> generating methods for consolidated attribute:  path
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: path, base type: LIST OF SELECT
  public static int usedinPath(EEntity_mapping_relationship type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a3$, domain, result);
  }

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			return;
		}
		a0 = av.entityValues[1].getInstance(0, this, a0$);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
		a2 = av.entityValues[1].getInstance(2, this, a2$);
		a3 = (AAttribute_mapping_path_select)av.entityValues[1].getInstanceAggregate(3, a3$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      a2 = unset_instance(a2);
      if (a3 instanceof CAggregate) {
        a3.unsetAll();
      }
      a3 = null;
      return;
    }
    a0 = av.entityValues[1].getInstance(0, this, a0$);
    a1 = av.entityValues[1].getInstance(1, this, a1$);
    a2 = av.entityValues[1].getInstance(2, this, a2$);
    a3 = (AAttribute_mapping_path_select) av.entityValues[1].getInstanceAggregate(3, a3$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: derived_variant_entity_mapping
		// partial entity: entity_mapping_relationship
		av.entityValues[1].setInstance(0, a0);
		av.entityValues[1].setInstance(1, a1);
		av.entityValues[1].setInstance(2, a2);
		av.entityValues[1].setInstanceAggregate(3, a3);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: derived_variant_entity_mapping
    // partial entity: entity_mapping_relationship
    av.entityValues[1].setInstance(0, a0);
    av.entityValues[1].setInstance(1, a1);
    av.entityValues[1].setInstance(2, a2);
    av.entityValues[1].setInstanceAggregate(3, a3);
  }
}
