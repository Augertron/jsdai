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

// Java class implementing entity generic_attribute_mapping

package jsdai.mapping;

import jsdai.lang.*;

public class CGeneric_attribute_mapping extends CEntity implements EGeneric_attribute_mapping {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CGeneric_attribute_mapping.class, SMapping.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // parent_entity - current entity - ENTITY entity_mapping
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // source - current entity - ENTITY attribute
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // constraints - current entity - SELECT constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected jsdai.dictionary.ANamed_type a3; // data_type - current entity - LIST OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected int a4; // strong - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // parent_entity - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // source - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // constraints - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;
  // data_type - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected jsdai.dictionary.ANamed_type a3;
  // strong - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  protected int a4;

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
		if (a2 == old) {
			a2 = newer;
		}
		changeReferencesAggregate(a3, old, newer);
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
    if (a1 == old) {
      a1 = newer;
    }
    if (a2 == old) {
      a2 = newer;
    }
    changeReferencesAggregate(a3, old, newer);
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #605=EXPLICIT_ATTRIBUTE('parent_entity',#602,0,#417,$,.F.);
  //<01> generating methods for consolidated attribute:  parent_entity
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : parent_entity, base type: entity entity_mapping
  public static int usedinParent_entity(EGeneric_attribute_mapping type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
    return test_instance(a0);
  }

  public EEntity_mapping getParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
    return (EEntity_mapping) get_instance(a0);
  }

  public void setParent_entity(EGeneric_attribute_mapping type, EEntity_mapping value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #607=EXPLICIT_ATTRIBUTE('source',#602,1,#74,$,.F.);
  //<01> generating methods for consolidated attribute:  source
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : source, base type: entity attribute
  public static int usedinSource(EGeneric_attribute_mapping type, jsdai.dictionary.EAttribute instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testSource(EGeneric_attribute_mapping type) throws SdaiException {
    return test_instance(a1);
  }

  public jsdai.dictionary.EAttribute getSource(EGeneric_attribute_mapping type) throws SdaiException {
    return (jsdai.dictionary.EAttribute) get_instance(a1);
  }

  public void setSource(EGeneric_attribute_mapping type, jsdai.dictionary.EAttribute value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetSource(EGeneric_attribute_mapping type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeSource(EGeneric_attribute_mapping type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #609=EXPLICIT_ATTRIBUTE('constraints',#602,2,#459,$,.T.);
  //<01> generating methods for consolidated attribute:  constraints
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: constraints
  public static int usedinConstraints(EGeneric_attribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testConstraints(EGeneric_attribute_mapping type) throws SdaiException {
    return test_instance(a2);
  }

  public EEntity getConstraints(EGeneric_attribute_mapping type) throws SdaiException { // case 1
    return get_instance_select(a2);
  }

  public void setConstraints(EGeneric_attribute_mapping type, EEntity value) throws SdaiException { // case 1
    a2 = set_instance(a2, value);
  }

  public void unsetConstraints(EGeneric_attribute_mapping type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeConstraints(EGeneric_attribute_mapping type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #611=EXPLICIT_ATTRIBUTE('data_type',#602,3,#882,$,.T.);
  //<01> generating methods for consolidated attribute:  data_type
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: data_type, base type: LIST OF ENTITY
  public static int usedinData_type(EGeneric_attribute_mapping type, jsdai.dictionary.ENamed_type instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a3$, domain, result);
  }

  public boolean testData_type(EGeneric_attribute_mapping type) throws SdaiException {
    return test_aggregate(a3);
  }

  public jsdai.dictionary.ANamed_type getData_type(EGeneric_attribute_mapping type) throws SdaiException {
    return (jsdai.dictionary.ANamed_type) get_aggregate(a3);
  }

  public jsdai.dictionary.ANamed_type createData_type(EGeneric_attribute_mapping type) throws SdaiException {
    a3 = (jsdai.dictionary.ANamed_type) create_aggregate_class(a3, a3$, jsdai.dictionary.ANamed_type.class, 0);
    return a3;
  }

  public void unsetData_type(EGeneric_attribute_mapping type) throws SdaiException {
    unset_aggregate(a3);
    a3 = null;
  }

  public static jsdai.dictionary.EAttribute attributeData_type(EGeneric_attribute_mapping type) throws SdaiException {
    return a3$;
  }

  //going through all the attributes: #613=EXPLICIT_ATTRIBUTE('strong',#602,4,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  strong
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: strong, base type: BOOLEAN
  public boolean testStrong(EGeneric_attribute_mapping type) throws SdaiException {
    return test_boolean(a4);
  }

  public boolean getStrong(EGeneric_attribute_mapping type) throws SdaiException {
    return get_boolean(a4);
  }

  public void setStrong(EGeneric_attribute_mapping type, boolean value) throws SdaiException {
    a4 = set_boolean(value);
  }

  public void unsetStrong(EGeneric_attribute_mapping type) throws SdaiException {
    a4 = unset_boolean();
  }

  public static jsdai.dictionary.EAttribute attributeStrong(EGeneric_attribute_mapping type) throws SdaiException {
    return a4$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			a4 = 0;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInstance(2, this, a2$);
		a3 = (jsdai.dictionary.ANamed_type)av.entityValues[0].getInstanceAggregate(3, a3$, this);
		a4 = av.entityValues[0].getBoolean(4);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
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
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[0].getInstance(2, this, a2$);
    a3 = (jsdai.dictionary.ANamed_type) av.entityValues[0].getInstanceAggregate(3, a3$, this);
    a4 = av.entityValues[0].getBoolean(4);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: generic_attribute_mapping
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
		av.entityValues[0].setInstanceAggregate(3, a3);
		av.entityValues[0].setBoolean(4, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: generic_attribute_mapping
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstance(2, a2);
    av.entityValues[0].setInstanceAggregate(3, a3);
    av.entityValues[0].setBoolean(4, a4);
  }
}
