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

// Java class implementing entity target_parameter

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CTarget_parameter extends CEntity implements ETarget_parameter {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CTarget_parameter.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // parent - current entity - ENTITY map_definition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // extent - current entity - ENTITY entity_definition
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected int a3; // order - current entity - INTEGER
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected String a0;
  // parent - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // extent - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;
  // order - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected int a3;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
		if (a2 == old) {
			a2 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a1 == old) {
      a1 = newer;
    }
    if (a2 == old) {
      a2 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1841=EXPLICIT_ATTRIBUTE('name',#1839,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: name, base type: STRING
  public boolean testName(ETarget_parameter type) throws SdaiException {
    return test_string(a0);
  }

  public String getName(ETarget_parameter type) throws SdaiException {
    return get_string(a0);
  }

  public void setName(ETarget_parameter type, String value) throws SdaiException {
    a0 = set_string(value);
  }

  public void unsetName(ETarget_parameter type) throws SdaiException {
    a0 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeName(ETarget_parameter type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1842=EXPLICIT_ATTRIBUTE('parent',#1839,1,#1728,$,.F.);
  //<01> generating methods for consolidated attribute:  parent
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : parent, base type: entity map_definition
  public static int usedinParent(ETarget_parameter type, EMap_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testParent(ETarget_parameter type) throws SdaiException {
    return test_instance(a1);
  }

  public EMap_definition getParent(ETarget_parameter type) throws SdaiException {
    return (EMap_definition) get_instance(a1);
  }

  public void setParent(ETarget_parameter type, EMap_definition value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetParent(ETarget_parameter type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeParent(ETarget_parameter type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1843=EXPLICIT_ATTRIBUTE('extent',#1839,2,#1633,$,.F.);
  //<01> generating methods for consolidated attribute:  extent
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : extent, base type: entity entity_definition
  public static int usedinExtent(ETarget_parameter type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testExtent(ETarget_parameter type) throws SdaiException {
    return test_instance(a2);
  }

  public EEntity_definition getExtent(ETarget_parameter type) throws SdaiException {
    return (EEntity_definition) get_instance(a2);
  }

  public void setExtent(ETarget_parameter type, EEntity_definition value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetExtent(ETarget_parameter type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeExtent(ETarget_parameter type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1844=EXPLICIT_ATTRIBUTE('order',#1839,3,#2,$,.F.);
  //<01> generating methods for consolidated attribute:  order
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: order, base type: INTEGER
  public boolean testOrder(ETarget_parameter type) throws SdaiException {
    return test_integer(a3);
  }

  public int getOrder(ETarget_parameter type) throws SdaiException {
    return get_integer(a3);
  }

  public void setOrder(ETarget_parameter type, int value) throws SdaiException {
    a3 = set_integer(value);
  }

  public void unsetOrder(ETarget_parameter type) throws SdaiException {
    a3 = unset_integer();
  }

  public static jsdai.dictionary.EAttribute attributeOrder(ETarget_parameter type) throws SdaiException {
    return a3$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			a3 = Integer.MIN_VALUE;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInstance(2, this, a2$);
		a3 = av.entityValues[0].getInteger(3);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      a2 = unset_instance(a2);
      a3 = Integer.MIN_VALUE;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[0].getInstance(2, this, a2$);
    a3 = av.entityValues[0].getInteger(3);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: target_parameter
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
		av.entityValues[0].setInteger(3, a3);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: target_parameter
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstance(2, a2);
    av.entityValues[0].setInteger(3, a3);
  }
}
