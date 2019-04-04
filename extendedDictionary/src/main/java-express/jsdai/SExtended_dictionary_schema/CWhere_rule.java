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

// Java class implementing entity where_rule

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CWhere_rule extends CEntity implements EWhere_rule {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CWhere_rule.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected String a0; // label - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // parent_item - current entity - SELECT type_or_rule
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected int a2; // order - current entity - INTEGER
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // label - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected String a0;
  // parent_item - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // order - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected int a2;

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

  //going through all the attributes: #1885=EXPLICIT_ATTRIBUTE('label',#1883,0,#1522,$,.T.);
  //<01> generating methods for consolidated attribute:  label
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: label, base type: STRING
  public boolean testLabel(EWhere_rule type) throws SdaiException {
    return test_string(a0);
  }

  public String getLabel(EWhere_rule type) throws SdaiException {
    return get_string(a0);
  }

  public void setLabel(EWhere_rule type, String value) throws SdaiException {
    a0 = set_string(value);
  }

  public void unsetLabel(EWhere_rule type) throws SdaiException {
    a0 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeLabel(EWhere_rule type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1886=EXPLICIT_ATTRIBUTE('parent_item',#1883,1,#1532,$,.F.);
  //<01> generating methods for consolidated attribute:  parent_item
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: parent_item
  public static int usedinParent_item(EWhere_rule type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testParent_item(EWhere_rule type) throws SdaiException {
    return test_instance(a1);
  }

  public EEntity getParent_item(EWhere_rule type) throws SdaiException { // case 1
    return get_instance_select(a1);
  }

  public void setParent_item(EWhere_rule type, EEntity value) throws SdaiException { // case 1
    a1 = set_instance(a1, value);
  }

  public void unsetParent_item(EWhere_rule type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeParent_item(EWhere_rule type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1887=EXPLICIT_ATTRIBUTE('order',#1883,2,#2,$,.F.);
  //<01> generating methods for consolidated attribute:  order
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: order, base type: INTEGER
  public boolean testOrder(EWhere_rule type) throws SdaiException {
    return test_integer(a2);
  }

  public int getOrder(EWhere_rule type) throws SdaiException {
    return get_integer(a2);
  }

  public void setOrder(EWhere_rule type, int value) throws SdaiException {
    a2 = set_integer(value);
  }

  public void unsetOrder(EWhere_rule type) throws SdaiException {
    a2 = unset_integer();
  }

  public static jsdai.dictionary.EAttribute attributeOrder(EWhere_rule type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = Integer.MIN_VALUE;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInteger(2);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      a2 = Integer.MIN_VALUE;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[0].getInteger(2);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: where_rule
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInteger(2, a2);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: where_rule
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInteger(2, a2);
  }
}
