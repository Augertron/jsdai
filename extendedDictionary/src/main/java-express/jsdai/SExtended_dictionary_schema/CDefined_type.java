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

// Java class implementing entity defined_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CDefined_type extends CNamed_type implements EDefined_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDefined_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// short_name: protected String a1;   short_name - java inheritance - STRING
	// where_rules: protected Object  - inverse - java inheritance -  ENTITY where_rule
	protected Object a2; // domain - current entity - SELECT underlying_type
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // short_name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected String a1;
  // where_rules - inverse - java inheritance
  // protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
  // domain - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a2 == old) {
			a2 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    if (a2 == old) {
      a2 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1589=EXPLICIT_ATTRIBUTE('name',#1587,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1750=EXPLICIT_ATTRIBUTE('short_name',#1748,0,#7,$,.T.);
  //<01> generating methods for consolidated attribute:  short_name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1751=INVERSE_ATTRIBUTE('where_rules',#1748,0,#1883,$,#1886,#2219,$,.F.);
  //<01> generating methods for consolidated attribute:  where_rules
  //<01-1> supertype, java inheritance
  //<01-1-2> inverse - generateInverseSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1602=EXPLICIT_ATTRIBUTE('domain',#1600,0,#1534,$,.F.);
  //<01> generating methods for consolidated attribute:  domain
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: domain
  public static int usedinDomain(EDefined_type type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testDomain(EDefined_type type) throws SdaiException {
    return test_instance(a2);
  }

  public EEntity getDomain(EDefined_type type) throws SdaiException { // case 1
    return get_instance_select(a2);
  }

  public void setDomain(EDefined_type type, EEntity value) throws SdaiException { // case 1
    a2 = set_instance(a2, value);
  }

  public void unsetDomain(EDefined_type type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeDomain(EDefined_type type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a2 = unset_instance(a2);
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a2 = av.entityValues[1].getInstance(0, this, a2$);
		a1 = av.entityValues[2].getString(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a2 = unset_instance(a2);
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a2 = av.entityValues[1].getInstance(0, this, a2$);
    a1 = av.entityValues[2].getString(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: defined_type
		av.entityValues[1].setInstance(0, a2);
		// partial entity: named_type
		av.entityValues[2].setString(0, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type
    av.entityValues[0].setString(0, a0);
    // partial entity: defined_type
    av.entityValues[1].setInstance(0, a2);
    // partial entity: named_type
    av.entityValues[2].setString(0, a1);
  }
}
