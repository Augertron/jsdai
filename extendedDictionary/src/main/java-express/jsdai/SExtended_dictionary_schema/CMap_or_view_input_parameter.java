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

// Java class implementing entity map_or_view_input_parameter

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CMap_or_view_input_parameter extends CEntity implements EMap_or_view_input_parameter {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CMap_or_view_input_parameter.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // parent - current entity - ENTITY map_or_view_partition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // extent - current entity - ENTITY data_type
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

  //going through all the attributes: #1736=EXPLICIT_ATTRIBUTE('name',#1734,0,#1522,$,.T.);
  //<01> generating methods for consolidated attribute:  name
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: name, base type: STRING
  public boolean testName(EMap_or_view_input_parameter type) throws SdaiException {
    return test_string(a0);
  }

  public String getName(EMap_or_view_input_parameter type) throws SdaiException {
    return get_string(a0);
  }

  public void setName(EMap_or_view_input_parameter type, String value) throws SdaiException {
    a0 = set_string(value);
  }

  public void unsetName(EMap_or_view_input_parameter type) throws SdaiException {
    a0 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeName(EMap_or_view_input_parameter type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1737=EXPLICIT_ATTRIBUTE('parent',#1734,1,#1740,$,.F.);
  //<01> generating methods for consolidated attribute:  parent
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : parent, base type: entity map_or_view_partition
  public static int usedinParent(EMap_or_view_input_parameter type, EMap_or_view_partition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testParent(EMap_or_view_input_parameter type) throws SdaiException {
    return test_instance(a1);
  }

  public EMap_or_view_partition getParent(EMap_or_view_input_parameter type) throws SdaiException {
    return (EMap_or_view_partition) get_instance(a1);
  }

  public void setParent(EMap_or_view_input_parameter type, EMap_or_view_partition value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetParent(EMap_or_view_input_parameter type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeParent(EMap_or_view_input_parameter type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1738=EXPLICIT_ATTRIBUTE('extent',#1734,2,#1587,$,.T.);
  //<01> generating methods for consolidated attribute:  extent
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : extent, base type: entity data_type
  public static int usedinExtent(EMap_or_view_input_parameter type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testExtent(EMap_or_view_input_parameter type) throws SdaiException {
    return test_instance(a2);
  }

  public EData_type getExtent(EMap_or_view_input_parameter type) throws SdaiException {
    return (EData_type) get_instance(a2);
  }

  public void setExtent(EMap_or_view_input_parameter type, EData_type value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetExtent(EMap_or_view_input_parameter type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeExtent(EMap_or_view_input_parameter type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1739=EXPLICIT_ATTRIBUTE('order',#1734,3,#2,$,.F.);
  //<01> generating methods for consolidated attribute:  order
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: order, base type: INTEGER
  public boolean testOrder(EMap_or_view_input_parameter type) throws SdaiException {
    return test_integer(a3);
  }

  public int getOrder(EMap_or_view_input_parameter type) throws SdaiException {
    return get_integer(a3);
  }

  public void setOrder(EMap_or_view_input_parameter type, int value) throws SdaiException {
    a3 = set_integer(value);
  }

  public void unsetOrder(EMap_or_view_input_parameter type) throws SdaiException {
    a3 = unset_integer();
  }

  public static jsdai.dictionary.EAttribute attributeOrder(EMap_or_view_input_parameter type) throws SdaiException {
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
		// partial entity: map_or_view_input_parameter
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
		av.entityValues[0].setInteger(3, a3);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: map_or_view_input_parameter
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstance(2, a2);
    av.entityValues[0].setInteger(3, a3);
  }
}
