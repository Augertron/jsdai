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

// Java class implementing entity type_constraint

package jsdai.mapping;

import jsdai.lang.*;

public class CType_constraint extends CMappingType_constraint implements EType_constraint {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CType_constraint.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // domain - current entity - ENTITY entity_definition
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // constraints - current entity - SELECT constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // domain - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // constraints - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
		if (a1 == old) {
			a1 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #668=EXPLICIT_ATTRIBUTE('domain',#665,0,#144,$,.F.);
  //<01> generating methods for consolidated attribute:  domain
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : domain, base type: entity entity_definition
  public static int usedinDomain(EType_constraint type, jsdai.dictionary.EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testDomain(EType_constraint type) throws SdaiException {
    return test_instance(a0);
  }

  public jsdai.dictionary.EEntity_definition getDomain(EType_constraint type) throws SdaiException {
    return (jsdai.dictionary.EEntity_definition) get_instance(a0);
  }

  public void setDomain(EType_constraint type, jsdai.dictionary.EEntity_definition value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetDomain(EType_constraint type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeDomain(EType_constraint type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #670=EXPLICIT_ATTRIBUTE('constraints',#665,1,#459,$,.T.);
  //<01> generating methods for consolidated attribute:  constraints
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: constraints
  public static int usedinConstraints(EType_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testConstraints(EType_constraint type) throws SdaiException {
    return test_instance(a1);
  }

  public EEntity getConstraints(EType_constraint type) throws SdaiException { // case 1
    return get_instance_select(a1);
  }

  public void setConstraints(EType_constraint type, EEntity value) throws SdaiException { // case 1
    a1 = set_instance(a1, value);
  }

  public void unsetConstraints(EType_constraint type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeConstraints(EType_constraint type) throws SdaiException {
    return a1$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[1].getInstance(0, this, a0$);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[1].getInstance(0, this, a0$);
    a1 = av.entityValues[1].getInstance(1, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: constraint
		// partial entity: type_constraint
		av.entityValues[1].setInstance(0, a0);
		av.entityValues[1].setInstance(1, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: constraint
    // partial entity: type_constraint
    av.entityValues[1].setInstance(0, a0);
    av.entityValues[1].setInstance(1, a1);
  }
}
