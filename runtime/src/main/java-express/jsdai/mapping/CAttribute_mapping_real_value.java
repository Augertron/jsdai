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

// Java class implementing entity attribute_mapping_real_value

package jsdai.mapping;

import jsdai.lang.*;

public class CAttribute_mapping_real_value extends CMappingAttribute_mapping_real_value implements EAttribute_mapping_real_value {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAttribute_mapping_real_value.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	// parent_entity: protected Object a0;   parent_entity - java inheritance - ENTITY entity_mapping
	// source: protected Object a1;   source - java inheritance - ENTITY attribute
	// constraints: protected Object a2;   constraints - java inheritance - SELECT constraint_select
	// data_type: protected jsdai.dictionary.ANamed_type a3;   data_type - java inheritance - LIST OF ENTITY
	// strong: protected int a4;   strong - java inheritance - BOOLEAN
	protected double a5; // mapped_value - current entity - REAL
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
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
  // protected jsdai.dictionary.ANamed_type a3;
  // strong - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  // protected int a4;
  // mapped_value - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
  protected double a5;

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

  //going through all the attributes: #605=EXPLICIT_ATTRIBUTE('parent_entity',#602,0,#417,$,.F.);
  //<01> generating methods for consolidated attribute:  parent_entity
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): parent_entity, base type: entity entity_mapping
  public static int usedinParent_entity(EGeneric_attribute_mapping type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  //going through all the attributes: #607=EXPLICIT_ATTRIBUTE('source',#602,1,#74,$,.F.);
  //<01> generating methods for consolidated attribute:  source
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): source, base type: entity attribute
  public static int usedinSource(EGeneric_attribute_mapping type, jsdai.dictionary.EAttribute instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #609=EXPLICIT_ATTRIBUTE('constraints',#602,2,#459,$,.T.);
  //<01> generating methods for consolidated attribute:  constraints
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // -1- methods for SELECT attribute: constraints
  public static int usedinConstraints(EGeneric_attribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  //going through all the attributes: #611=EXPLICIT_ATTRIBUTE('data_type',#602,3,#882,$,.T.);
  //<01> generating methods for consolidated attribute:  data_type
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: data_type, base type: LIST OF ENTITY
  public static int usedinData_type(EGeneric_attribute_mapping type, jsdai.dictionary.ENamed_type instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a3$, domain, result);
  }

  //going through all the attributes: #613=EXPLICIT_ATTRIBUTE('strong',#602,4,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  strong
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #638=EXPLICIT_ATTRIBUTE('mapped_value',#635,0,#3,$,.F.);
  //<01> generating methods for consolidated attribute:  mapped_value
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: mapped_value, base type: REAL
  public boolean testMapped_value(EAttribute_mapping_real_value type) throws SdaiException {
    return test_double(a5);
  }

  public double getMapped_value(EAttribute_mapping_real_value type) throws SdaiException {
    return get_double(a5);
  }

  public void setMapped_value(EAttribute_mapping_real_value type, double value) throws SdaiException {
    a5 = set_double(value);
  }

  public void unsetMapped_value(EAttribute_mapping_real_value type) throws SdaiException {
    a5 = unset_double();
  }

  public static jsdai.dictionary.EAttribute attributeMapped_value(EAttribute_mapping_real_value type) throws SdaiException {
    return a5$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a5 = Double.NaN;
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			a4 = 0;
			return;
		}
		a5 = av.entityValues[0].getDouble(0);
		a0 = av.entityValues[2].getInstance(0, this, a0$);
		a1 = av.entityValues[2].getInstance(1, this, a1$);
		a2 = av.entityValues[2].getInstance(2, this, a2$);
		a3 = (jsdai.dictionary.ANamed_type)av.entityValues[2].getInstanceAggregate(3, a3$, this);
		a4 = av.entityValues[2].getBoolean(4);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a5 = Double.NaN;
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
    a5 = av.entityValues[0].getDouble(0);
    a0 = av.entityValues[2].getInstance(0, this, a0$);
    a1 = av.entityValues[2].getInstance(1, this, a1$);
    a2 = av.entityValues[2].getInstance(2, this, a2$);
    a3 = (jsdai.dictionary.ANamed_type) av.entityValues[2].getInstanceAggregate(3, a3$, this);
    a4 = av.entityValues[2].getBoolean(4);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_mapping_real_value
		av.entityValues[0].setDouble(0, a5);
		// partial entity: attribute_mapping_value
		// partial entity: generic_attribute_mapping
		av.entityValues[2].setInstance(0, a0);
		av.entityValues[2].setInstance(1, a1);
		av.entityValues[2].setInstance(2, a2);
		av.entityValues[2].setInstanceAggregate(3, a3);
		av.entityValues[2].setBoolean(4, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: attribute_mapping_real_value
    av.entityValues[0].setDouble(0, a5);
    // partial entity: attribute_mapping_value
    // partial entity: generic_attribute_mapping
    av.entityValues[2].setInstance(0, a0);
    av.entityValues[2].setInstance(1, a1);
    av.entityValues[2].setInstance(2, a2);
    av.entityValues[2].setInstanceAggregate(3, a3);
    av.entityValues[2].setBoolean(4, a4);
  }
}
