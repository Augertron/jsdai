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

// Java class implementing entity implicit_declaration$rule_declaration

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CImplicit_declaration$rule_declaration extends CEntity implements EImplicit_declaration, ERule_declaration {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CImplicit_declaration$rule_declaration.class,
      SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // parent - non-java inheritance - ENTITY generic_schema_definition
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // definition - non-java inheritance - SELECT declaration_type
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// parent_schema: protected Object  - derived - non-java inheritance -  ENTITY schema_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected String a2; // alias_name - non-java inheritance - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // parent - explicit - non-java inheritance
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // definition - explicit - non-java inheritance
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // parent_schema - derived - non-java inheritance
  protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
  // alias_name - explicit - non-java inheritance
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected String a2;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
		if (a1 == old) {
			a1 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1597=EXPLICIT_ATTRIBUTE('parent',#1595,0,#1682,$,.F.);
  //<01> generating methods for consolidated attribute:  parent
  //<01-2> supertype, non-java inheritance
  //<01-2-0> explicit - generateExplicitSupertypeNoJavaInheritanceMethodsX()
  // attribute (current explicit or supertype explicit) : parent, base type: entity generic_schema_definition
  public static int usedinParent(EDeclaration type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testParent(EDeclaration type) throws SdaiException {
    return test_instance(a0);
  }

  public EGeneric_schema_definition getParent(EDeclaration type) throws SdaiException {
    return (EGeneric_schema_definition) get_instance(a0);
  }

  public void setParent(EDeclaration type, EGeneric_schema_definition value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetParent(EDeclaration type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeParent(EDeclaration type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1598=EXPLICIT_ATTRIBUTE('definition',#1595,1,#1512,$,.F.);
  //<01> generating methods for consolidated attribute:  definition
  //<01-2> supertype, non-java inheritance
  //<01-2-0> explicit - generateExplicitSupertypeNoJavaInheritanceMethodsX()
  // -2- methods for SELECT attribute: definition
  public static int usedinDefinition(EDeclaration type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testDefinition(EDeclaration type) throws SdaiException {
    return test_instance(a1);
  }

  public EEntity getDefinition(EDeclaration type) throws SdaiException { // case 1
    return get_instance_select(a1);
  }

  public void setDefinition(EDeclaration type, EEntity value) throws SdaiException { // case 1
    a1 = set_instance(a1, value);
  }

  public void unsetDefinition(EDeclaration type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeDefinition(EDeclaration type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1599=DERIVED_ATTRIBUTE('parent_schema',#1595,0,#1793,$);
  //<01> generating methods for consolidated attribute:  parent_schema
  //<01-2> supertype, non-java inheritance
  //<01-2-1> derived
  //<01-2-1-2> NOT explicit-to-derived - generateDerivedSupertypeNoJavaInheritanceMethodsX()
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
    return (ESchema_definition) getParent_schema((EDeclaration) null, _context).getInstance();
  }

  public static jsdai.dictionary.EAttribute attributeParent_schema(EDeclaration type) throws SdaiException {
    return d0$;
  }

  //going through all the attributes: #1703=EXPLICIT_ATTRIBUTE('alias_name',#1701,0,#1522,$,.T.);
  //<01> generating methods for consolidated attribute:  alias_name
  //<01-2> supertype, non-java inheritance
  //<01-2-0> explicit - generateExplicitSupertypeNoJavaInheritanceMethodsX()
  /// methods for attribute: alias_name, base type: STRING
  public boolean testAlias_name(EInterfaced_declaration type) throws SdaiException {
    return test_string(a2);
  }

  public String getAlias_name(EInterfaced_declaration type) throws SdaiException {
    return get_string(a2);
  }

  public void setAlias_name(EInterfaced_declaration type, String value) throws SdaiException {
    a2 = set_string(value);
  }

  public void unsetAlias_name(EInterfaced_declaration type) throws SdaiException {
    a2 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeAlias_name(EInterfaced_declaration type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[2].getString(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      a2 = null;
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[2].getString(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: declaration
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		// partial entity: implicit_declaration
		// partial entity: interfaced_declaration
		av.entityValues[2].setString(0, a2);
		// partial entity: rule_declaration
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: declaration
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setInstance(1, a1);
    // partial entity: implicit_declaration
    // partial entity: interfaced_declaration
    av.entityValues[2].setString(0, a2);
    // partial entity: rule_declaration
  }
}
