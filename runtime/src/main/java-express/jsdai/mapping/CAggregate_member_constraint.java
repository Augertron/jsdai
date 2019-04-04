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

// Java class implementing entity aggregate_member_constraint

package jsdai.mapping;

import jsdai.lang.*;

public class CAggregate_member_constraint extends CMappingAggregate_member_constraint implements EAggregate_member_constraint {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAggregate_member_constraint.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected int a0; // member - current entity - INTEGER
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // attribute - current entity - SELECT aggregate_member_constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // member - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected int a0;
  // attribute - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #474=EXPLICIT_ATTRIBUTE('member',#471,0,#2,$,.T.);
  //<01> generating methods for consolidated attribute:  member
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: member, base type: INTEGER
  public boolean testMember(EAggregate_member_constraint type) throws SdaiException {
    return test_integer(a0);
  }

  public int getMember(EAggregate_member_constraint type) throws SdaiException {
    return get_integer(a0);
  }

  public void setMember(EAggregate_member_constraint type, int value) throws SdaiException {
    a0 = set_integer(value);
  }

  public void unsetMember(EAggregate_member_constraint type) throws SdaiException {
    a0 = unset_integer();
  }

  public static jsdai.dictionary.EAttribute attributeMember(EAggregate_member_constraint type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #476=EXPLICIT_ATTRIBUTE('attribute',#471,1,#450,$,.F.);
  //<01> generating methods for consolidated attribute:  attribute
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: attribute
  public static int usedinAttribute(EAggregate_member_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testAttribute(EAggregate_member_constraint type) throws SdaiException {
    return test_instance(a1);
  }

  public EEntity getAttribute(EAggregate_member_constraint type) throws SdaiException { // case 1
    return get_instance_select(a1);
  }

  public void setAttribute(EAggregate_member_constraint type, EEntity value) throws SdaiException { // case 1
    a1 = set_instance(a1, value);
  }

  public void unsetAttribute(EAggregate_member_constraint type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeAttribute(EAggregate_member_constraint type) throws SdaiException {
    return a1$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = Integer.MIN_VALUE;
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[0].getInteger(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = Integer.MIN_VALUE;
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[0].getInteger(0);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: aggregate_member_constraint
		av.entityValues[0].setInteger(0, a0);
		av.entityValues[0].setInstance(1, a1);
		// partial entity: constraint
		// partial entity: constraint_attribute
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: aggregate_member_constraint
    av.entityValues[0].setInteger(0, a0);
    av.entityValues[0].setInstance(1, a1);
    // partial entity: constraint
    // partial entity: constraint_attribute
  }
}
