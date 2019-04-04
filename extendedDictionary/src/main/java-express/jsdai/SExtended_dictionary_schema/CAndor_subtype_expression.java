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

// Java class implementing entity andor_subtype_expression

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CAndor_subtype_expression extends CSubtype_expression implements EAndor_subtype_expression {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAndor_subtype_expression.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// generic_operands: protected AEntity_or_view_or_subtype_expression a0;   generic_operands - java inheritance - SET OF SELECT
	// operands: protected AEntity_or_subtype_expression  - derived - java inheritance -  SET OF SELECT
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // generic_operands - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected AEntity_or_view_or_subtype_expression a0;
  // operands - derived - java inheritance
  // protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

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

  //going through all the attributes: #1837=EXPLICIT_ATTRIBUTE('generic_operands',#1835,0,#2080,$,.F.);
  //<01> generating methods for consolidated attribute:  generic_operands
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: generic_operands, base type: SET OF SELECT
  public static int usedinGeneric_operands(ESubtype_expression type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #1838=DERIVED_ATTRIBUTE('operands',#1835,0,#2081,$);
  //<01> generating methods for consolidated attribute:  operands
  //<01-1> supertype, java inheritance
  //<01-1-1> derived
  //<01-1-1-2> NOT explicit-to-derived - generateDerivedSupertypeJavaInheritedMethodsX
  // methods for attribute: operands, base type: SET OF SELECT
  public boolean testOperands(ESubtype_expression type) throws SdaiException {
    throw new SdaiException(SdaiException.FN_NAVL);
  }

  public AEntity_or_subtype_expression getOperands(ESubtype_expression type) throws SdaiException {
    SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
    return (AEntity_or_subtype_expression) getOperands((ESubtype_expression) null, _context).getInstanceAggregate(this);
  }

  public Value getOperands(ESubtype_expression type, SdaiContext _context) throws SdaiException {

//###-01 jc.generated_java: (new jsdai.SExtended_dictionary_schema.FGet_operands()).run(_context, Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(jsdai.SExtended_dictionary_schema.CSubtype_expression.attributeGeneric_operands(null))))
    return ((new jsdai.SExtended_dictionary_schema.FGet_operands()).run(_context,
        Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, get(jsdai.SExtended_dictionary_schema.CSubtype_expression.attributeGeneric_operands(null)))));
  }

  public static jsdai.dictionary.EAttribute attributeOperands(ESubtype_expression type) throws SdaiException {
    return d0$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			if (a0 instanceof CAggregate)
				a0.unsetAll();
			a0 = null;
			return;
		}
		a0 = (AEntity_or_view_or_subtype_expression)av.entityValues[1].getInstanceAggregate(0, a0$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      if (a0 instanceof CAggregate) {
        a0.unsetAll();
      }
      a0 = null;
      return;
    }
    a0 = (AEntity_or_view_or_subtype_expression) av.entityValues[1].getInstanceAggregate(0, a0$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: andor_subtype_expression
		// partial entity: subtype_expression
		av.entityValues[1].setInstanceAggregate(0, a0);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: andor_subtype_expression
    // partial entity: subtype_expression
    av.entityValues[1].setInstanceAggregate(0, a0);
  }
}
