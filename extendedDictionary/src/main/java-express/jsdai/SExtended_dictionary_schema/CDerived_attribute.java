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

// Java class implementing entity derived_attribute

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CDerived_attribute extends CAttribute implements EDerived_attribute {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDerived_attribute.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// parent: protected Object a1;   parent - java inheritance - ENTITY entity_or_view_definition
	// order: protected int a2;   order - java inheritance - INTEGER
	// parent_entity: protected Object  - derived - java inheritance -  ENTITY entity_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected Object a3; // domain - current entity - SELECT base_type
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected Object a4; // redeclaring - current entity - SELECT explicit_or_derived
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // parent - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // order - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected int a2;
  // parent_entity - derived - java inheritance
  // protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
  // domain - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected Object a3;
  // redeclaring - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  protected Object a4;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a3 == old) {
			a3 = newer;
		}
		if (a4 == old) {
			a4 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    if (a3 == old) {
      a3 = newer;
    }
    if (a4 == old) {
      a4 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1565=EXPLICIT_ATTRIBUTE('name',#1563,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1566=EXPLICIT_ATTRIBUTE('parent',#1563,1,#1644,$,.F.);
  //<01> generating methods for consolidated attribute:  parent
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): parent, base type: entity entity_or_view_definition
  public static int usedinParent(EAttribute type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1567=EXPLICIT_ATTRIBUTE('order',#1563,2,#2,$,.T.);
  //<01> generating methods for consolidated attribute:  order
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1568=DERIVED_ATTRIBUTE('parent_entity',#1563,0,#1633,$);
  //<01> generating methods for consolidated attribute:  parent_entity
  //<01-1> supertype, java inheritance
  //<01-1-1> derived
  //<01-1-1-2> NOT explicit-to-derived - generateDerivedSupertypeJavaInheritedMethodsX
  // derived attribute (current derived or supertype derived): parent_entity, base type: entity entity_definition
  public boolean testParent_entity(EAttribute type) throws SdaiException {
    throw new SdaiException(SdaiException.FN_NAVL);
  }

  public Value getParent_entity(EAttribute type, SdaiContext _context) throws SdaiException {

//###-01 jc.generated_java: (new jsdai.SExtended_dictionary_schema.FGet_entity_definition()).run(_context, Value.alloc(jsdai.SExtended_dictionary_schema.CEntity_or_view_definition.definition).set(_context, get(jsdai.SExtended_dictionary_schema.CAttribute.attributeParent(null))))
    return ((new jsdai.SExtended_dictionary_schema.FGet_entity_definition()).run(_context,
        Value.alloc(jsdai.SExtended_dictionary_schema.CEntity_or_view_definition.definition)
            .set(_context, get(jsdai.SExtended_dictionary_schema.CAttribute.attributeParent(null)))));
  }

  public EEntity_definition getParent_entity(EAttribute type) throws SdaiException {
    SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
    return (EEntity_definition) getParent_entity(null, _context).getInstance();
  }

  public static jsdai.dictionary.EAttribute attributeParent_entity(EAttribute type) throws SdaiException {
    return d0$;
  }

  //going through all the attributes: #1618=EXPLICIT_ATTRIBUTE('domain',#1616,0,#1506,$,.F.);
  //<01> generating methods for consolidated attribute:  domain
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: domain
  public static int usedinDomain(EDerived_attribute type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a3$, domain, result);
  }

  public boolean testDomain(EDerived_attribute type) throws SdaiException {
    return test_instance(a3);
  }

  public EEntity getDomain(EDerived_attribute type) throws SdaiException { // case 1
    return get_instance_select(a3);
  }

  public void setDomain(EDerived_attribute type, EEntity value) throws SdaiException { // case 1
    a3 = set_instance(a3, value);
  }

  public void unsetDomain(EDerived_attribute type) throws SdaiException {
    a3 = unset_instance(a3);
  }

  public static jsdai.dictionary.EAttribute attributeDomain(EDerived_attribute type) throws SdaiException {
    return a3$;
  }

  //going through all the attributes: #1619=EXPLICIT_ATTRIBUTE('redeclaring',#1616,1,#1520,$,.T.);
  //<01> generating methods for consolidated attribute:  redeclaring
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: redeclaring
  public static int usedinRedeclaring(EDerived_attribute type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a4$, domain, result);
  }

  public boolean testRedeclaring(EDerived_attribute type) throws SdaiException {
    return test_instance(a4);
  }

  public EEntity getRedeclaring(EDerived_attribute type) throws SdaiException { // case 1
    return get_instance_select(a4);
  }

  public void setRedeclaring(EDerived_attribute type, EEntity value) throws SdaiException { // case 1
    a4 = set_instance(a4, value);
  }

  public void unsetRedeclaring(EDerived_attribute type) throws SdaiException {
    a4 = unset_instance(a4);
  }

  public static jsdai.dictionary.EAttribute attributeRedeclaring(EDerived_attribute type) throws SdaiException {
    return a4$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = Integer.MIN_VALUE;
			a3 = unset_instance(a3);
			a4 = unset_instance(a4);
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInteger(2);
		a3 = av.entityValues[1].getInstance(0, this, a3$);
		a4 = av.entityValues[1].getInstance(1, this, a4$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      a2 = Integer.MIN_VALUE;
      a3 = unset_instance(a3);
      a4 = unset_instance(a4);
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[0].getInteger(2);
    a3 = av.entityValues[1].getInstance(0, this, a3$);
    a4 = av.entityValues[1].getInstance(1, this, a4$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInteger(2, a2);
		// partial entity: derived_attribute
		av.entityValues[1].setInstance(0, a3);
		av.entityValues[1].setInstance(1, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: attribute
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInteger(2, a2);
    // partial entity: derived_attribute
    av.entityValues[1].setInstance(0, a3);
    av.entityValues[1].setInstance(1, a4);
  }
}
