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

// Java class implementing entity documentation_with_element

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CDocumentation_with_element extends CDocumentation implements EDocumentation_with_element {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDocumentation_with_element.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// target: protected Object a0;   target - java inheritance - SELECT documentation_object
	// values: protected A_string a1;   values - java inheritance - LIST OF STRING
	protected String a2; // element - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // target - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // values - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected A_string a1;
  // element - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected String a2;

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

  //going through all the attributes: #1555=EXPLICIT_ATTRIBUTE('target',#1553,0,#1514,$,.F.);
  //<01> generating methods for consolidated attribute:  target
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: target
  public static int usedinTarget(EAnnotation type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #1556=EXPLICIT_ATTRIBUTE('values',#1553,1,#2046,$,.F.);
  //<01> generating methods for consolidated attribute:  values
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1624=EXPLICIT_ATTRIBUTE('element',#1622,0,#7,$,.F.);
  //<01> generating methods for consolidated attribute:  element
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: element, base type: STRING
  public boolean testElement(EDocumentation_with_element type) throws SdaiException {
    return test_string(a2);
  }

  public String getElement(EDocumentation_with_element type) throws SdaiException {
    return get_string(a2);
  }

  public void setElement(EDocumentation_with_element type, String value) throws SdaiException {
    a2 = set_string(value);
  }

  public void unsetElement(EDocumentation_with_element type) throws SdaiException {
    a2 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeElement(EDocumentation_with_element type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			a2 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getStringAggregate(1, a1$, this);
		a2 = av.entityValues[2].getString(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      a2 = null;
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getStringAggregate(1, a1$, this);
    a2 = av.entityValues[2].getString(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: annotation
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setStringAggregate(1, a1);
		// partial entity: documentation
		// partial entity: documentation_with_element
		av.entityValues[2].setString(0, a2);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: annotation
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setStringAggregate(1, a1);
    // partial entity: documentation
    // partial entity: documentation_with_element
    av.entityValues[2].setString(0, a2);
  }
}
