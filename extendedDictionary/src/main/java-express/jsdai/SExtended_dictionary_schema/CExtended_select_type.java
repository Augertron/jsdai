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

// Java class implementing entity extended_select_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CExtended_select_type extends CSelect_type implements EExtended_select_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExtended_select_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// local_selections: protected ANamed_type a1;   local_selections - java inheritance - SET OF ENTITY
	// selections: protected ANamed_type  - derived - java inheritance -  SET OF ENTITY
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected Object a2; // is_based_on - current entity - ENTITY defined_type
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // local_selections - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected ANamed_type a1;
  // selections - derived - java inheritance
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
  //going through all the attributes: #1811=EXPLICIT_ATTRIBUTE('local_selections',#1809,0,#2077,$,.T.);
  //<01> generating methods for consolidated attribute:  local_selections
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: local_selections, base type: SET OF ENTITY
  public static int usedinLocal_selections(ESelect_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1812=DERIVED_ATTRIBUTE('selections',#1809,0,#2078,$);
  //<01> generating methods for consolidated attribute:  selections
  //<01-1> supertype, java inheritance
  //<01-1-1> derived
  //<01-1-1-2> NOT explicit-to-derived - generateDerivedSupertypeJavaInheritedMethodsX
  // derived attribute: selections, base type: entity named_type
  public boolean testSelections(ESelect_type type) throws SdaiException {
    throw new SdaiException(SdaiException.FN_NAVL);
  }

  public ANamed_type getSelections(ESelect_type type) throws SdaiException {
    SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
    return (ANamed_type) getSelections((ESelect_type) null, _context).getInstanceAggregate(this);
  }

  public Value getSelections(ESelect_type type, SdaiContext _context) throws SdaiException {

//###-01 jc.generated_java: Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(jsdai.SExtended_dictionary_schema.CSelect_type.attributeLocal_selections(null)))
    return (Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(jsdai.SExtended_dictionary_schema.CSelect_type.attributeLocal_selections(null))));
  }

  public static jsdai.dictionary.EAttribute attributeSelections(ESelect_type type) throws SdaiException {
    return d0$;
  }

  //going through all the attributes: #1665=EXPLICIT_ATTRIBUTE('is_based_on',#1663,0,#1600,$,.F.);
  //<01> generating methods for consolidated attribute:  is_based_on
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : is_based_on, base type: entity defined_type
  public static int usedinIs_based_on(EExtended_select_type type, EDefined_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testIs_based_on(EExtended_select_type type) throws SdaiException {
    return test_instance(a2);
  }

  public EDefined_type getIs_based_on(EExtended_select_type type) throws SdaiException {
    return (EDefined_type) get_instance(a2);
  }

  public void setIs_based_on(EExtended_select_type type, EDefined_type value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetIs_based_on(EExtended_select_type type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeIs_based_on(EExtended_select_type type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a2 = unset_instance(a2);
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a2 = av.entityValues[1].getInstance(0, this, a2$);
		a1 = (ANamed_type)av.entityValues[2].getInstanceAggregate(0, a1$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a2 = unset_instance(a2);
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a2 = av.entityValues[1].getInstance(0, this, a2$);
    a1 = (ANamed_type) av.entityValues[2].getInstanceAggregate(0, a1$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: extended_select_type
		av.entityValues[1].setInstance(0, a2);
		// partial entity: select_type
		av.entityValues[2].setInstanceAggregate(0, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type
    av.entityValues[0].setString(0, a0);
    // partial entity: extended_select_type
    av.entityValues[1].setInstance(0, a2);
    // partial entity: select_type
    av.entityValues[2].setInstanceAggregate(0, a1);
  }

  /*---------------------- methods to validate WHERE rules --------------------*/

  public int rExtended_select_typeWr1(SdaiContext _context) throws SdaiException {

    return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE)
        .IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_SELECT_TYPE", "EXTENDED_DICTIONARY_SCHEMA"),
            Value.alloc(jsdai.SExtended_dictionary_schema.CDefined_type.definition).set(_context, get(a2$))
                .getAttribute(jsdai.SExtended_dictionary_schema.CDefined_type.attributeDomain(null), _context).typeOfV(_context))).getLogical());
  }
}
