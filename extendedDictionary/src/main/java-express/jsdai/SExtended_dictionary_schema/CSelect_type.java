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

// Java class implementing entity select_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CSelect_type extends CData_type implements ESelect_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSelect_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	protected ANamed_type a1; // local_selections - current entity - SET OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// selections: protected ANamed_type  - derived - current -  SET OF ENTITY
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // local_selections - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected ANamed_type a1;
  // selections - derived - current entity
  protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		changeReferencesAggregate(a1, old, newer);
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    changeReferencesAggregate(a1, old, newer);
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1589=EXPLICIT_ATTRIBUTE('name',#1587,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1811=EXPLICIT_ATTRIBUTE('local_selections',#1809,0,#2077,$,.T.);
  //<01> generating methods for consolidated attribute:  local_selections
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: local_selections, base type: SET OF ENTITY
  public static int usedinLocal_selections(ESelect_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testLocal_selections(ESelect_type type) throws SdaiException {
    return test_aggregate(a1);
  }

  public ANamed_type getLocal_selections(ESelect_type type) throws SdaiException {
    return (ANamed_type) get_aggregate(a1);
  }

  public ANamed_type createLocal_selections(ESelect_type type) throws SdaiException {
    a1 = (ANamed_type) create_aggregate_class(a1, a1$, ANamed_type.class, 0);
    return a1;
  }

  public void unsetLocal_selections(ESelect_type type) throws SdaiException {
    unset_aggregate(a1);
    a1 = null;
  }

  public static jsdai.dictionary.EAttribute attributeLocal_selections(ESelect_type type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1812=DERIVED_ATTRIBUTE('selections',#1809,0,#2078,$);
  //<01> generating methods for consolidated attribute:  selections
  //<01-0> current entity
  //<01-0-1> derived attribute
  //<01-0-1-1> NOT explicit-to-derived - generateDerivedCurrentEntityMethodsX()
  // derived attribute: selections, base type: entity named_type
  public boolean testSelections(ESelect_type type) throws SdaiException {
    throw new SdaiException(SdaiException.FN_NAVL);
  }

  public ANamed_type getSelections(ESelect_type type) throws SdaiException {
    SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
    return (ANamed_type) getSelections((ESelect_type) null, _context).getInstanceAggregate(this);
  }

  public Value getSelections(ESelect_type type, SdaiContext _context) throws SdaiException {

//###-01 jc.generated_java: Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(a1$))
    return (Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(a1$)));
  }

  public static jsdai.dictionary.EAttribute attributeSelections(ESelect_type type) throws SdaiException {
    return d0$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = (ANamed_type)av.entityValues[1].getInstanceAggregate(0, a1$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = (ANamed_type) av.entityValues[1].getInstanceAggregate(0, a1$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: select_type
		av.entityValues[1].setInstanceAggregate(0, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type
    av.entityValues[0].setString(0, a0);
    // partial entity: select_type
    av.entityValues[1].setInstanceAggregate(0, a1);
  }

  /*---------------------- methods to validate WHERE rules --------------------*/

  public int rSelect_typeWr1(SdaiContext _context) throws SdaiException {

    return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).OR(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE)
        .greater(_context, Value.alloc(ExpressTypes.INTEGER_TYPE).sizeOf(_context, Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(a1$))),
            Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 0)), Value.alloc(ExpressTypes.LOGICAL_TYPE)
        .IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_SELECT_TYPE", "EXTENDED_DICTIONARY_SCHEMA"),
            Value.alloc(jsdai.SExtended_dictionary_schema.CSelect_type.definition).set(_context, this).typeOfV(_context)))).getLogical());
  }
}
