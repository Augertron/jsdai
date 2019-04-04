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

// Java class implementing entity data_type_declaration

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CData_type_declaration extends CDeclaration implements EData_type_declaration {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CData_type_declaration.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// parent: protected Object a0;   parent - java inheritance - ENTITY generic_schema_definition
	// definition: protected Object a1;   definition - java inheritance - SELECT declaration_type
	// parent_schema: protected Object  - derived - java inheritance -  ENTITY schema_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // parent - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // definition - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // parent_schema - derived - java inheritance
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

  //going through all the attributes: #1597=EXPLICIT_ATTRIBUTE('parent',#1595,0,#1682,$,.F.);
  //<01> generating methods for consolidated attribute:  parent
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): parent, base type: entity generic_schema_definition
  public static int usedinParent(EDeclaration type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #1598=EXPLICIT_ATTRIBUTE('definition',#1595,1,#1512,$,.F.);
  //<01> generating methods for consolidated attribute:  definition
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: definition
  public static int usedinDefinition(EDeclaration type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1599=DERIVED_ATTRIBUTE('parent_schema',#1595,0,#1793,$);
  //<01> generating methods for consolidated attribute:  parent_schema
  //<01-1> supertype, java inheritance
  //<01-1-1> derived
  //<01-1-1-2> NOT explicit-to-derived - generateDerivedSupertypeJavaInheritedMethodsX
  // derived attribute (current derived or supertype derived): parent_schema, base type: entity schema_definition
  public boolean testParent_schema(EDeclaration type) throws SdaiException {
    throw new SdaiException(SdaiException.FN_NAVL);
  }

  public Value getParent_schema(EDeclaration type, SdaiContext _context) throws SdaiException {

//###-01 jc.generated_java: (new jsdai.SExtended_dictionary_schema.FGet_schema_definition()).run(_context, Value.alloc(jsdai.SExtended_dictionary_schema.CGeneric_schema_definition.definition).set(_context, get(jsdai.SExtended_dictionary_schema.CDeclaration.attributeParent(null))))
    return ((new jsdai.SExtended_dictionary_schema.FGet_schema_definition()).run(_context,
        Value.alloc(jsdai.SExtended_dictionary_schema.CGeneric_schema_definition.definition)
            .set(_context, get(jsdai.SExtended_dictionary_schema.CDeclaration.attributeParent(null)))));
  }

  public ESchema_definition getParent_schema(EDeclaration type) throws SdaiException {
    SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
    return (ESchema_definition) getParent_schema(null, _context).getInstance();
  }

  public static jsdai.dictionary.EAttribute attributeParent_schema(EDeclaration type) throws SdaiException {
    return d0$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[1].getInstance(0, this, a0$);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[1].getInstance(0, this, a0$);
    a1 = av.entityValues[1].getInstance(1, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type_declaration
		// partial entity: declaration
		av.entityValues[1].setInstance(0, a0);
		av.entityValues[1].setInstance(1, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type_declaration
    // partial entity: declaration
    av.entityValues[1].setInstance(0, a0);
    av.entityValues[1].setInstance(1, a1);
  }
}
