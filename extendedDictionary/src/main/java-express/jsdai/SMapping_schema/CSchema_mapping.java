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

// Java class implementing entity schema_mapping

package jsdai.SMapping_schema;

import jsdai.lang.*;

public class CSchema_mapping extends CEntity implements ESchema_mapping {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSchema_mapping.class, SMapping_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // source - current entity - ENTITY schema_definition
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // target - current entity - ENTITY schema_definition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected AUof_mapping a2; // uofs - current entity - SET OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected String a3; // id - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected ASchema_mapping a4; // components - current entity - SET OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // source - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // target - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // uofs - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected AUof_mapping a2;
  // id - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected String a3;
  // components - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  protected ASchema_mapping a4;

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
		changeReferencesAggregate(a2, old, newer);
		changeReferencesAggregate(a4, old, newer);
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
    if (a1 == old) {
      a1 = newer;
    }
    changeReferencesAggregate(a2, old, newer);
    changeReferencesAggregate(a4, old, newer);
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1258=EXPLICIT_ATTRIBUTE('source',#1255,0,#1793,$,.F.);
  //<01> generating methods for consolidated attribute:  source
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : source, base type: entity schema_definition
  public static int usedinSource(ESchema_mapping type, jsdai.SExtended_dictionary_schema.ESchema_definition instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testSource(ESchema_mapping type) throws SdaiException {
    return test_instance(a0);
  }

  public jsdai.SExtended_dictionary_schema.ESchema_definition getSource(ESchema_mapping type) throws SdaiException {
    return (jsdai.SExtended_dictionary_schema.ESchema_definition) get_instance(a0);
  }

  public void setSource(ESchema_mapping type, jsdai.SExtended_dictionary_schema.ESchema_definition value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetSource(ESchema_mapping type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeSource(ESchema_mapping type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1260=EXPLICIT_ATTRIBUTE('target',#1255,1,#1793,$,.F.);
  //<01> generating methods for consolidated attribute:  target
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : target, base type: entity schema_definition
  public static int usedinTarget(ESchema_mapping type, jsdai.SExtended_dictionary_schema.ESchema_definition instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testTarget(ESchema_mapping type) throws SdaiException {
    return test_instance(a1);
  }

  public jsdai.SExtended_dictionary_schema.ESchema_definition getTarget(ESchema_mapping type) throws SdaiException {
    return (jsdai.SExtended_dictionary_schema.ESchema_definition) get_instance(a1);
  }

  public void setTarget(ESchema_mapping type, jsdai.SExtended_dictionary_schema.ESchema_definition value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetTarget(ESchema_mapping type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeTarget(ESchema_mapping type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1262=EXPLICIT_ATTRIBUTE('uofs',#1255,2,#2037,$,.F.);
  //<01> generating methods for consolidated attribute:  uofs
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: uofs, base type: SET OF ENTITY
  public static int usedinUofs(ESchema_mapping type, EUof_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testUofs(ESchema_mapping type) throws SdaiException {
    return test_aggregate(a2);
  }

  public AUof_mapping getUofs(ESchema_mapping type) throws SdaiException {
    return (AUof_mapping) get_aggregate(a2);
  }

  public AUof_mapping createUofs(ESchema_mapping type) throws SdaiException {
    a2 = (AUof_mapping) create_aggregate_class(a2, a2$, AUof_mapping.class, 0);
    return a2;
  }

  public void unsetUofs(ESchema_mapping type) throws SdaiException {
    unset_aggregate(a2);
    a2 = null;
  }

  public static jsdai.dictionary.EAttribute attributeUofs(ESchema_mapping type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1264=EXPLICIT_ATTRIBUTE('id',#1255,3,#7,$,.F.);
  //<01> generating methods for consolidated attribute:  id
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: id, base type: STRING
  public boolean testId(ESchema_mapping type) throws SdaiException {
    return test_string(a3);
  }

  public String getId(ESchema_mapping type) throws SdaiException {
    return get_string(a3);
  }

  public void setId(ESchema_mapping type, String value) throws SdaiException {
    a3 = set_string(value);
  }

  public void unsetId(ESchema_mapping type) throws SdaiException {
    a3 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeId(ESchema_mapping type) throws SdaiException {
    return a3$;
  }

  //going through all the attributes: #1266=EXPLICIT_ATTRIBUTE('components',#1255,4,#2038,$,.T.);
  //<01> generating methods for consolidated attribute:  components
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: components, base type: SET OF ENTITY
  public static int usedinComponents(ESchema_mapping type, ESchema_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a4$, domain, result);
  }

  public boolean testComponents(ESchema_mapping type) throws SdaiException {
    return test_aggregate(a4);
  }

  public ASchema_mapping getComponents(ESchema_mapping type) throws SdaiException {
    return (ASchema_mapping) get_aggregate(a4);
  }

  public ASchema_mapping createComponents(ESchema_mapping type) throws SdaiException {
    a4 = (ASchema_mapping) create_aggregate_class(a4, a4$, ASchema_mapping.class, 0);
    return a4;
  }

  public void unsetComponents(ESchema_mapping type) throws SdaiException {
    unset_aggregate(a4);
    a4 = null;
  }

  public static jsdai.dictionary.EAttribute attributeComponents(ESchema_mapping type) throws SdaiException {
    return a4$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a3 = null;
			if (a4 instanceof CAggregate)
				a4.unsetAll();
			a4 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = (AUof_mapping)av.entityValues[0].getInstanceAggregate(2, a2$, this);
		a3 = av.entityValues[0].getString(3);
		a4 = (ASchema_mapping)av.entityValues[0].getInstanceAggregate(4, a4$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      if (a2 instanceof CAggregate) {
        a2.unsetAll();
      }
      a2 = null;
      a3 = null;
      if (a4 instanceof CAggregate) {
        a4.unsetAll();
      }
      a4 = null;
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = (AUof_mapping) av.entityValues[0].getInstanceAggregate(2, a2$, this);
    a3 = av.entityValues[0].getString(3);
    a4 = (ASchema_mapping) av.entityValues[0].getInstanceAggregate(4, a4$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: schema_mapping
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstanceAggregate(2, a2);
		av.entityValues[0].setString(3, a3);
		av.entityValues[0].setInstanceAggregate(4, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: schema_mapping
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstanceAggregate(2, a2);
    av.entityValues[0].setString(3, a3);
    av.entityValues[0].setInstanceAggregate(4, a4);
  }
}
