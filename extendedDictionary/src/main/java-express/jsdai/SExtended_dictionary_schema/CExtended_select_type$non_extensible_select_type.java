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

// Java class implementing entity extended_select_type$non_extensible_select_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CExtended_select_type$non_extensible_select_type extends CEntity implements EExtended_select_type, ENon_extensible_select_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExtended_select_type$non_extensible_select_type.class,
      SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected String a0; // name - non-java inheritance - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // is_based_on - non-java inheritance - ENTITY defined_type
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected ANamed_type a2; // local_selections - non-java inheritance - SET OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	// selections: protected ANamed_type  - derived - non-java inheritance -  SET OF ENTITY
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - non-java inheritance
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected String a0;
  // is_based_on - explicit - non-java inheritance
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // local_selections - explicit - non-java inheritance
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected ANamed_type a2;
  // selections - derived - non-java inheritance
  protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
		changeReferencesAggregate(a2, old, newer);
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a1 == old) {
      a1 = newer;
    }
    changeReferencesAggregate(a2, old, newer);
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1589=EXPLICIT_ATTRIBUTE('name',#1587,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-2> supertype, non-java inheritance
  //<01-2-0> explicit - generateExplicitSupertypeNoJavaInheritanceMethodsX()
  /// methods for attribute: name, base type: STRING
  public boolean testName(EData_type type) throws SdaiException {
    return test_string(a0);
  }

  public String getName(EData_type type) throws SdaiException {
    return get_string(a0);
  }

  public void setName(EData_type type, String value) throws SdaiException {
    a0 = set_string(value);
  }

  public void unsetName(EData_type type) throws SdaiException {
    a0 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeName(EData_type type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1665=EXPLICIT_ATTRIBUTE('is_based_on',#1663,0,#1600,$,.F.);
  //<01> generating methods for consolidated attribute:  is_based_on
  //<01-2> supertype, non-java inheritance
  //<01-2-0> explicit - generateExplicitSupertypeNoJavaInheritanceMethodsX()
  // attribute (current explicit or supertype explicit) : is_based_on, base type: entity defined_type
  public static int usedinIs_based_on(EExtended_select_type type, EDefined_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testIs_based_on(EExtended_select_type type) throws SdaiException {
    return test_instance(a1);
  }

  public EDefined_type getIs_based_on(EExtended_select_type type) throws SdaiException {
    return (EDefined_type) get_instance(a1);
  }

  public void setIs_based_on(EExtended_select_type type, EDefined_type value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetIs_based_on(EExtended_select_type type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeIs_based_on(EExtended_select_type type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1754=EXPLICIT_ATTRIBUTE('local_selections',#1752,$,#2064,#1811,.F.);
  //<02> NOT generating methods for NOT consolidated attribute:  local_selections
  //going through all the attributes: #1811=EXPLICIT_ATTRIBUTE('local_selections',#1809,0,#2077,$,.T.);
  //<01> generating methods for consolidated attribute:  local_selections
  //<01-2> supertype, non-java inheritance
  //<01-2-0> explicit - generateExplicitSupertypeNoJavaInheritanceMethodsX()
  // methods for attribute: local_selections, base type: SET OF ENTITY
  public static int usedinLocal_selections(ESelect_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testLocal_selections(ESelect_type type) throws SdaiException {
    return test_aggregate(a2);
  }

  public ANamed_type getLocal_selections(ESelect_type type) throws SdaiException {
    return (ANamed_type) get_aggregate(a2);
  }

  public ANamed_type createLocal_selections(ESelect_type type) throws SdaiException {
    a2 = (ANamed_type) create_aggregate_class(a2, a2$, ANamed_type.class, 0);
    return a2;
  }

  public void unsetLocal_selections(ESelect_type type) throws SdaiException {
    unset_aggregate(a2);
    a2 = null;
  }

  public static jsdai.dictionary.EAttribute attributeLocal_selections(ESelect_type type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1812=DERIVED_ATTRIBUTE('selections',#1809,0,#2078,$);
  //<01> generating methods for consolidated attribute:  selections
  //<01-2> supertype, non-java inheritance
  //<01-2-1> derived
  //<01-2-1-2> NOT explicit-to-derived - generateDerivedSupertypeNoJavaInheritanceMethodsX()
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


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getInstance(0, this, a1$);
		a2 = (ANamed_type)av.entityValues[3].getInstanceAggregate(0, a2$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      if (a2 instanceof CAggregate) {
        a2.unsetAll();
      }
      a2 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[1].getInstance(0, this, a1$);
    a2 = (ANamed_type) av.entityValues[3].getInstanceAggregate(0, a2$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: extended_select_type
		av.entityValues[1].setInstance(0, a1);
		// partial entity: non_extensible_select_type
		// partial entity: select_type
		av.entityValues[3].setInstanceAggregate(0, a2);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type
    av.entityValues[0].setString(0, a0);
    // partial entity: extended_select_type
    av.entityValues[1].setInstance(0, a1);
    // partial entity: non_extensible_select_type
    // partial entity: select_type
    av.entityValues[3].setInstanceAggregate(0, a2);
  }

  /*---------------------- methods to validate WHERE rules --------------------*/

  public int rExtended_select_typeWr1(SdaiContext _context) throws SdaiException {

    return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE)
        .IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_SELECT_TYPE", "EXTENDED_DICTIONARY_SCHEMA"),
            Value.alloc(jsdai.SExtended_dictionary_schema.CDefined_type.definition)
                .set(_context, get(jsdai.SExtended_dictionary_schema.CExtended_select_type.attributeIs_based_on(null)))
                .getAttribute(jsdai.SExtended_dictionary_schema.CDefined_type.attributeDomain(null), _context).typeOfV(_context))).getLogical());
  }

  public int rSelect_typeWr1(SdaiContext _context) throws SdaiException {

    return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).OR(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE)
        .greater(_context, Value.alloc(ExpressTypes.INTEGER_TYPE).sizeOf(_context,
            Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(jsdai.SExtended_dictionary_schema.CSelect_type.attributeLocal_selections(null)))),
            Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 0)), Value.alloc(ExpressTypes.LOGICAL_TYPE)
        .IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_SELECT_TYPE", "EXTENDED_DICTIONARY_SCHEMA"),
            Value.alloc(jsdai.SExtended_dictionary_schema.CExtended_select_type$non_extensible_select_type.definition).set(_context, this).typeOfV(_context))))
        .getLogical());
  }
}
