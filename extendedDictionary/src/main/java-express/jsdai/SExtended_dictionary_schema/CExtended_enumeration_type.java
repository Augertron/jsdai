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

// Java class implementing entity extended_enumeration_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CExtended_enumeration_type extends CEnumeration_type implements EExtended_enumeration_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExtended_enumeration_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// local_elements: protected A_string a1;   local_elements - java inheritance - LIST OF STRING
	// elements: protected A_string  - derived - java inheritance -  LIST OF STRING
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected Object a2; // is_based_on - current entity - ENTITY defined_type
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // local_elements - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected A_string a1;
  // elements - derived - java inheritance
  // protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
  // is_based_on - explicit - current entity
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
  //going through all the attributes: #1651=EXPLICIT_ATTRIBUTE('local_elements',#1649,0,#2053,$,.T.);
  //<01> generating methods for consolidated attribute:  local_elements
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1652=DERIVED_ATTRIBUTE('elements',#1649,0,#2054,$);
  //<01> generating methods for consolidated attribute:  elements
  //<01-1> supertype, java inheritance
  //<01-1-1> derived
  //<01-1-1-2> NOT explicit-to-derived - generateDerivedSupertypeJavaInheritedMethodsX
  // methods for attribute: elements, base type: LIST OF STRING
  public boolean testElements(EEnumeration_type type) throws SdaiException {
    throw new SdaiException(SdaiException.FN_NAVL);
  }

  public A_string getElements(EEnumeration_type type) throws SdaiException {
    SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
    return getElements((EEnumeration_type) null, _context).getStringAggregate(this);
  }

  public Value getElements(EEnumeration_type type, SdaiContext _context) throws SdaiException {

//###-01 jc.generated_java: Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_list_0_unique_express_id).set(_context, get(jsdai.SExtended_dictionary_schema.CEnumeration_type.attributeLocal_elements(null)))
    return (Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_list_0_unique_express_id)
        .set(_context, get(jsdai.SExtended_dictionary_schema.CEnumeration_type.attributeLocal_elements(null))));
  }

  public static jsdai.dictionary.EAttribute attributeElements(EEnumeration_type type) throws SdaiException {
    return d0$;
  }

  //going through all the attributes: #1662=EXPLICIT_ATTRIBUTE('is_based_on',#1660,0,#1600,$,.F.);
  //<01> generating methods for consolidated attribute:  is_based_on
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : is_based_on, base type: entity defined_type
  public static int usedinIs_based_on(EExtended_enumeration_type type, EDefined_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testIs_based_on(EExtended_enumeration_type type) throws SdaiException {
    return test_instance(a2);
  }

  public EDefined_type getIs_based_on(EExtended_enumeration_type type) throws SdaiException {
    return (EDefined_type) get_instance(a2);
  }

  public void setIs_based_on(EExtended_enumeration_type type, EDefined_type value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetIs_based_on(EExtended_enumeration_type type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeIs_based_on(EExtended_enumeration_type type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			a2 = unset_instance(a2);
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getStringAggregate(0, a1$, this);
		a2 = av.entityValues[2].getInstance(0, this, a2$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      a2 = unset_instance(a2);
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[1].getStringAggregate(0, a1$, this);
    a2 = av.entityValues[2].getInstance(0, this, a2$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: enumeration_type
		av.entityValues[1].setStringAggregate(0, a1);
		// partial entity: extended_enumeration_type
		av.entityValues[2].setInstance(0, a2);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type
    av.entityValues[0].setString(0, a0);
    // partial entity: enumeration_type
    av.entityValues[1].setStringAggregate(0, a1);
    // partial entity: extended_enumeration_type
    av.entityValues[2].setInstance(0, a2);
  }

  /*---------------------- methods to validate WHERE rules --------------------*/

  public int rExtended_enumeration_typeWr1(SdaiContext _context) throws SdaiException {

    return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE)
        .IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_ENUMERATION_TYPE", "EXTENDED_DICTIONARY_SCHEMA"),
            Value.alloc(jsdai.SExtended_dictionary_schema.CDefined_type.definition).set(_context, get(a2$))
                .getAttribute(jsdai.SExtended_dictionary_schema.CDefined_type.attributeDomain(null), _context).typeOfV(_context))).getLogical());
  }
}
