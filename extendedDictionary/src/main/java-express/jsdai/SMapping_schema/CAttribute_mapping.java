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

// Java class implementing entity attribute_mapping

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class CAttribute_mapping extends CGeneric_attribute_mapping implements EAttribute_mapping {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAttribute_mapping.class, SMapping_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// parent_entity: protected Object a0;   parent_entity - java inheritance - ENTITY entity_mapping
	// source: protected Object a1;   source - java inheritance - ENTITY attribute
	// constraints: protected Object a2;   constraints - java inheritance - SELECT constraint_select
	// data_type: protected jsdai.SExtended_dictionary_schema.ANamed_type a3;   data_type - java inheritance - LIST OF ENTITY
	// strong: protected int a4;   strong - java inheritance - BOOLEAN
	protected AAttribute_mapping_path_select a5; // path - current entity - LIST OF SELECT
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
	protected Object a6; // domain - current entity - SELECT attribute_mapping_domain_select
	protected static final jsdai.dictionary.CExplicit_attribute a6$ = CEntity.initExplicitAttribute(definition, 6);
	// target: protected Object  - derived - current -  SELECT base_type
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // parent_entity - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected Object a0;
  // source - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // constraints - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected Object a2;
  // data_type - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  // protected jsdai.SExtended_dictionary_schema.ANamed_type a3;
  // strong - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  // protected int a4;
  // path - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
  protected AAttribute_mapping_path_select a5;
  // domain - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a6$ = CEntity.initExplicitAttribute(definition, 6);
  protected Object a6;
  // target - derived - current entity
  protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		changeReferencesAggregate(a5, old, newer);
		if (a6 == old) {
			a6 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    changeReferencesAggregate(a5, old, newer);
    if (a6 == old) {
      a6 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1430=EXPLICIT_ATTRIBUTE('parent_entity',#1427,0,#1242,$,.F.);
  //<01> generating methods for consolidated attribute:  parent_entity
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): parent_entity, base type: entity entity_mapping
  public static int usedinParent_entity(EGeneric_attribute_mapping type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #1432=EXPLICIT_ATTRIBUTE('source',#1427,1,#1563,$,.F.);
  //<01> generating methods for consolidated attribute:  source
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): source, base type: entity attribute
  public static int usedinSource(EGeneric_attribute_mapping type, jsdai.SExtended_dictionary_schema.EAttribute instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1434=EXPLICIT_ATTRIBUTE('constraints',#1427,2,#1284,$,.T.);
  //<01> generating methods for consolidated attribute:  constraints
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: constraints
  public static int usedinConstraints(EGeneric_attribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  //going through all the attributes: #1436=EXPLICIT_ATTRIBUTE('data_type',#1427,3,#2042,$,.T.);
  //<01> generating methods for consolidated attribute:  data_type
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: data_type, base type: LIST OF ENTITY
  public static int usedinData_type(EGeneric_attribute_mapping type, jsdai.SExtended_dictionary_schema.ENamed_type instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a3$, domain, result);
  }

  //going through all the attributes: #1438=EXPLICIT_ATTRIBUTE('strong',#1427,4,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  strong
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1422=EXPLICIT_ATTRIBUTE('path',#1419,0,#2041,$,.T.);
  //<01> generating methods for consolidated attribute:  path
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: path, base type: LIST OF SELECT
  public static int usedinPath(EAttribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a5$, domain, result);
  }

  public boolean testPath(EAttribute_mapping type) throws SdaiException {
    return test_aggregate(a5);
  }

  public AAttribute_mapping_path_select getPath(EAttribute_mapping type) throws SdaiException {
    if (a5 == null) {
      throw new SdaiException(SdaiException.VA_NSET);
    }
    return a5;
  }

  public AAttribute_mapping_path_select createPath(EAttribute_mapping type) throws SdaiException {
    a5 = (AAttribute_mapping_path_select) create_aggregate_class(a5, a5$, AAttribute_mapping_path_select.class, 0);
    return a5;
  }

  public void unsetPath(EAttribute_mapping type) throws SdaiException {
    unset_aggregate(a5);
    a5 = null;
  }

  public static jsdai.dictionary.EAttribute attributePath(EAttribute_mapping type) throws SdaiException {
    return a5$;
  }

  //going through all the attributes: #1424=EXPLICIT_ATTRIBUTE('domain',#1419,1,#1240,$,.T.);
  //<01> generating methods for consolidated attribute:  domain
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: domain
  public static int usedinDomain(EAttribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a6$, domain, result);
  }

  public boolean testDomain(EAttribute_mapping type) throws SdaiException {
    return test_instance(a6);
  }

  public EEntity getDomain(EAttribute_mapping type) throws SdaiException { // case 1
    return get_instance_select(a6);
  }

  public void setDomain(EAttribute_mapping type, EEntity value) throws SdaiException { // case 1
    a6 = set_instance(a6, value);
  }

  public void unsetDomain(EAttribute_mapping type) throws SdaiException {
    a6 = unset_instance(a6);
  }

  public static jsdai.dictionary.EAttribute attributeDomain(EAttribute_mapping type) throws SdaiException {
    return a6$;
  }

  //going through all the attributes: #1426=DERIVED_ATTRIBUTE('target',#1419,0,#1506,$);
  //<01> generating methods for consolidated attribute:  target
  //<01-0> current entity
  //<01-0-1> derived attribute
  //<01-0-1-1> NOT explicit-to-derived - generateDerivedCurrentEntityMethodsX()
  // methods for derived SELECT attribute: target
  public boolean testTarget(EAttribute_mapping type) throws SdaiException {
    throw new SdaiException(SdaiException.FN_NAVL);
  }

  public EEntity getTarget(EAttribute_mapping type) throws SdaiException { // case 1
    SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
    return (EEntity) getTarget(((EAttribute_mapping) null), _context).getInstance();
  }

  public Value getTarget(EAttribute_mapping type, SdaiContext _context) throws SdaiException {

//###-01 jc.generated_java: Value.alloc(jsdai.SMapping_schema.SMapping_schema._st_list_1_attribute_mapping_path_select).set(_context, get(a5$)).indexing(_context, Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1), null)
    return (Value.alloc(jsdai.SMapping_schema.SMapping_schema._st_list_1_attribute_mapping_path_select).set(_context, get(a5$))
        .indexing(_context, Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1), null));
  }

  public static jsdai.dictionary.EAttribute attributeTarget(EAttribute_mapping type) throws SdaiException {
    return d0$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			if (a5 instanceof CAggregate)
				a5.unsetAll();
			a5 = null;
			a6 = unset_instance(a6);
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			a4 = 0;
			return;
		}
		a5 = (AAttribute_mapping_path_select)av.entityValues[0].getInstanceAggregate(0, a5$, this);
		a6 = av.entityValues[0].getInstance(1, this, a6$);
		a0 = av.entityValues[1].getInstance(0, this, a0$);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
		a2 = av.entityValues[1].getInstance(2, this, a2$);
		a3 = (jsdai.SExtended_dictionary_schema.ANamed_type)av.entityValues[1].getInstanceAggregate(3, a3$, this);
		a4 = av.entityValues[1].getBoolean(4);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      if (a5 instanceof CAggregate) {
        a5.unsetAll();
      }
      a5 = null;
      a6 = unset_instance(a6);
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      a2 = unset_instance(a2);
      if (a3 instanceof CAggregate) {
        a3.unsetAll();
      }
      a3 = null;
      a4 = 0;
      return;
    }
    a5 = (AAttribute_mapping_path_select) av.entityValues[0].getInstanceAggregate(0, a5$, this);
    a6 = av.entityValues[0].getInstance(1, this, a6$);
    a0 = av.entityValues[1].getInstance(0, this, a0$);
    a1 = av.entityValues[1].getInstance(1, this, a1$);
    a2 = av.entityValues[1].getInstance(2, this, a2$);
    a3 = (jsdai.SExtended_dictionary_schema.ANamed_type) av.entityValues[1].getInstanceAggregate(3, a3$, this);
    a4 = av.entityValues[1].getBoolean(4);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_mapping
		av.entityValues[0].setInstanceAggregate(0, a5);
		av.entityValues[0].setInstance(1, a6);
		// partial entity: generic_attribute_mapping
		av.entityValues[1].setInstance(0, a0);
		av.entityValues[1].setInstance(1, a1);
		av.entityValues[1].setInstance(2, a2);
		av.entityValues[1].setInstanceAggregate(3, a3);
		av.entityValues[1].setBoolean(4, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: attribute_mapping
    av.entityValues[0].setInstanceAggregate(0, a5);
    av.entityValues[0].setInstance(1, a6);
    // partial entity: generic_attribute_mapping
    av.entityValues[1].setInstance(0, a0);
    av.entityValues[1].setInstance(1, a1);
    av.entityValues[1].setInstance(2, a2);
    av.entityValues[1].setInstanceAggregate(3, a3);
    av.entityValues[1].setBoolean(4, a4);
  }
}
