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

// Java class implementing entity entity_mapping

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class CEntity_mapping extends CEntity implements EEntity_mapping {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CEntity_mapping.class, SMapping_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // source - current entity - ENTITY entity_definition
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // target - current entity - SELECT entity_or_attribute
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // constraints - current entity - SELECT constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected int a3; // entry_point - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected int a4; // strong_users - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // source - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // target - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // constraints - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;
  // entry_point - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected int a3;
  // strong_users - explicit - current entity
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
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1245=EXPLICIT_ATTRIBUTE('source',#1242,0,#1633,$,.F.);
  //<01> generating methods for consolidated attribute:  source
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : source, base type: entity entity_definition
  public static int usedinSource(EEntity_mapping type, jsdai.SExtended_dictionary_schema.EEntity_definition instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testSource(EEntity_mapping type) throws SdaiException {
    return test_instance(a0);
  }

  public jsdai.SExtended_dictionary_schema.EEntity_definition getSource(EEntity_mapping type) throws SdaiException {
    return (jsdai.SExtended_dictionary_schema.EEntity_definition) get_instance(a0);
  }

  public void setSource(EEntity_mapping type, jsdai.SExtended_dictionary_schema.EEntity_definition value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetSource(EEntity_mapping type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeSource(EEntity_mapping type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1247=EXPLICIT_ATTRIBUTE('target',#1242,1,#1237,$,.F.);
  //<01> generating methods for consolidated attribute:  target
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: target
  public static int usedinTarget(EEntity_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testTarget(EEntity_mapping type) throws SdaiException {
    return test_instance(a1);
  }

  public EEntity getTarget(EEntity_mapping type) throws SdaiException { // case 1
    return get_instance_select(a1);
  }

  public void setTarget(EEntity_mapping type, EEntity value) throws SdaiException { // case 1
    a1 = set_instance(a1, value);
  }

  public void unsetTarget(EEntity_mapping type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeTarget(EEntity_mapping type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1249=EXPLICIT_ATTRIBUTE('constraints',#1242,2,#1284,$,.T.);
  //<01> generating methods for consolidated attribute:  constraints
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // -2- methods for SELECT attribute: constraints
  public static int usedinConstraints(EEntity_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testConstraints(EEntity_mapping type) throws SdaiException {
    return test_instance(a2);
  }

  public EEntity getConstraints(EEntity_mapping type) throws SdaiException { // case 1
    return get_instance_select(a2);
  }

  public void setConstraints(EEntity_mapping type, EEntity value) throws SdaiException { // case 1
    a2 = set_instance(a2, value);
  }

  public void unsetConstraints(EEntity_mapping type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeConstraints(EEntity_mapping type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1251=EXPLICIT_ATTRIBUTE('entry_point',#1242,3,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  entry_point
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: entry_point, base type: BOOLEAN
  public boolean testEntry_point(EEntity_mapping type) throws SdaiException {
    return test_boolean(a3);
  }

  public boolean getEntry_point(EEntity_mapping type) throws SdaiException {
    return get_boolean(a3);
  }

  public void setEntry_point(EEntity_mapping type, boolean value) throws SdaiException {
    a3 = set_boolean(value);
  }

  public void unsetEntry_point(EEntity_mapping type) throws SdaiException {
    a3 = unset_boolean();
  }

  public static jsdai.dictionary.EAttribute attributeEntry_point(EEntity_mapping type) throws SdaiException {
    return a3$;
  }

  //going through all the attributes: #1253=EXPLICIT_ATTRIBUTE('strong_users',#1242,4,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  strong_users
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: strong_users, base type: BOOLEAN
  public boolean testStrong_users(EEntity_mapping type) throws SdaiException {
    return test_boolean(a4);
  }

  public boolean getStrong_users(EEntity_mapping type) throws SdaiException {
    return get_boolean(a4);
  }

  public void setStrong_users(EEntity_mapping type, boolean value) throws SdaiException {
    a4 = set_boolean(value);
  }

  public void unsetStrong_users(EEntity_mapping type) throws SdaiException {
    a4 = unset_boolean();
  }

  public static jsdai.dictionary.EAttribute attributeStrong_users(EEntity_mapping type) throws SdaiException {
    return a4$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			a3 = 0;
			a4 = 0;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInstance(2, this, a2$);
		a3 = av.entityValues[0].getBoolean(3);
		a4 = av.entityValues[0].getBoolean(4);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      a2 = unset_instance(a2);
      a3 = 0;
      a4 = 0;
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[0].getInstance(2, this, a2$);
    a3 = av.entityValues[0].getBoolean(3);
    a4 = av.entityValues[0].getBoolean(4);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: entity_mapping
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
		av.entityValues[0].setBoolean(3, a3);
		av.entityValues[0].setBoolean(4, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: entity_mapping
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstance(2, a2);
    av.entityValues[0].setBoolean(3, a3);
    av.entityValues[0].setBoolean(4, a4);
  }
}
