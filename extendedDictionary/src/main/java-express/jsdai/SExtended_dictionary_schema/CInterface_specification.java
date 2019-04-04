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

// Java class implementing entity interface_specification

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CInterface_specification extends CEntity implements EInterface_specification {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CInterface_specification.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // foreign_schema - current entity - ENTITY generic_schema_definition
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // current_schema - current entity - ENTITY generic_schema_definition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected AInterfaced_declaration a2; // items - current entity - SET OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // foreign_schema - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // current_schema - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // items - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected AInterfaced_declaration a2;

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
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1706=EXPLICIT_ATTRIBUTE('foreign_schema',#1704,0,#1682,$,.F.);
  //<01> generating methods for consolidated attribute:  foreign_schema
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : foreign_schema, base type: entity generic_schema_definition
  public static int usedinForeign_schema(EInterface_specification type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testForeign_schema(EInterface_specification type) throws SdaiException {
    return test_instance(a0);
  }

  public EGeneric_schema_definition getForeign_schema(EInterface_specification type) throws SdaiException {
    return (EGeneric_schema_definition) get_instance(a0);
  }

  public void setForeign_schema(EInterface_specification type, EGeneric_schema_definition value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetForeign_schema(EInterface_specification type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeForeign_schema(EInterface_specification type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1707=EXPLICIT_ATTRIBUTE('current_schema',#1704,1,#1682,$,.F.);
  //<01> generating methods for consolidated attribute:  current_schema
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : current_schema, base type: entity generic_schema_definition
  public static int usedinCurrent_schema(EInterface_specification type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testCurrent_schema(EInterface_specification type) throws SdaiException {
    return test_instance(a1);
  }

  public EGeneric_schema_definition getCurrent_schema(EInterface_specification type) throws SdaiException {
    return (EGeneric_schema_definition) get_instance(a1);
  }

  public void setCurrent_schema(EInterface_specification type, EGeneric_schema_definition value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetCurrent_schema(EInterface_specification type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeCurrent_schema(EInterface_specification type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1708=EXPLICIT_ATTRIBUTE('items',#1704,2,#2059,$,.T.);
  //<01> generating methods for consolidated attribute:  items
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: items, base type: SET OF ENTITY
  public static int usedinItems(EInterface_specification type, EInterfaced_declaration instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testItems(EInterface_specification type) throws SdaiException {
    return test_aggregate(a2);
  }

  public AInterfaced_declaration getItems(EInterface_specification type) throws SdaiException {
    return (AInterfaced_declaration) get_aggregate(a2);
  }

  public AInterfaced_declaration createItems(EInterface_specification type) throws SdaiException {
    a2 = (AInterfaced_declaration) create_aggregate_class(a2, a2$, AInterfaced_declaration.class, 0);
    return a2;
  }

  public void unsetItems(EInterface_specification type) throws SdaiException {
    unset_aggregate(a2);
    a2 = null;
  }

  public static jsdai.dictionary.EAttribute attributeItems(EInterface_specification type) throws SdaiException {
    return a2$;
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
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = (AInterfaced_declaration)av.entityValues[0].getInstanceAggregate(2, a2$, this);
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
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = (AInterfaced_declaration) av.entityValues[0].getInstanceAggregate(2, a2$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: interface_specification
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstanceAggregate(2, a2);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: interface_specification
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstanceAggregate(2, a2);
  }
}
