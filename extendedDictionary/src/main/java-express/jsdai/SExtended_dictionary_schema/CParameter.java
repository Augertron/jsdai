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

// Java class implementing entity parameter

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CParameter extends CEntity implements EParameter {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CParameter.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // parameter_type - current entity - ENTITY data_type
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected int a2; // var_type - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected A_string a3; // type_labels - current entity - LIST OF STRING
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected String a0;
  // parameter_type - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // var_type - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected int a2;
  // type_labels - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected A_string a3;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1761=EXPLICIT_ATTRIBUTE('name',#1759,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: name, base type: STRING
  public boolean testName(EParameter type) throws SdaiException {
    return test_string(a0);
  }

  public String getName(EParameter type) throws SdaiException {
    return get_string(a0);
  }

  public void setName(EParameter type, String value) throws SdaiException {
    a0 = set_string(value);
  }

  public void unsetName(EParameter type) throws SdaiException {
    a0 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeName(EParameter type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1762=EXPLICIT_ATTRIBUTE('parameter_type',#1759,1,#1587,$,.F.);
  //<01> generating methods for consolidated attribute:  parameter_type
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : parameter_type, base type: entity data_type
  public static int usedinParameter_type(EParameter type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testParameter_type(EParameter type) throws SdaiException {
    return test_instance(a1);
  }

  public EData_type getParameter_type(EParameter type) throws SdaiException {
    return (EData_type) get_instance(a1);
  }

  public void setParameter_type(EParameter type, EData_type value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetParameter_type(EParameter type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeParameter_type(EParameter type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1763=EXPLICIT_ATTRIBUTE('var_type',#1759,2,#6,$,.F.);
  //<01> generating methods for consolidated attribute:  var_type
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: var_type, base type: BOOLEAN
  public boolean testVar_type(EParameter type) throws SdaiException {
    return test_boolean(a2);
  }

  public boolean getVar_type(EParameter type) throws SdaiException {
    return get_boolean(a2);
  }

  public void setVar_type(EParameter type, boolean value) throws SdaiException {
    a2 = set_boolean(value);
  }

  public void unsetVar_type(EParameter type) throws SdaiException {
    a2 = unset_boolean();
  }

  public static jsdai.dictionary.EAttribute attributeVar_type(EParameter type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1764=EXPLICIT_ATTRIBUTE('type_labels',#1759,3,#2065,$,.T.);
  //<01> generating methods for consolidated attribute:  type_labels
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: type_labels, base type: LIST OF STRING
  public boolean testType_labels(EParameter type) throws SdaiException {
    return test_aggregate(a3);
  }

  public A_string getType_labels(EParameter type) throws SdaiException {
    return (A_string) get_aggregate(a3);
  }

  public A_string createType_labels(EParameter type) throws SdaiException {
    a3 = create_aggregate_string(a3, a3$, 0);
    return a3;
  }

  public void unsetType_labels(EParameter type) throws SdaiException {
    unset_aggregate(a3);
    a3 = null;
  }

  public static jsdai.dictionary.EAttribute attributeType_labels(EParameter type) throws SdaiException {
    return a3$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = 0;
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getBoolean(2);
		a3 = av.entityValues[0].getStringAggregate(3, a3$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      a2 = 0;
      if (a3 instanceof CAggregate) {
        a3.unsetAll();
      }
      a3 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[0].getBoolean(2);
    a3 = av.entityValues[0].getStringAggregate(3, a3$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: parameter
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setBoolean(2, a2);
		av.entityValues[0].setStringAggregate(3, a3);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: parameter
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setBoolean(2, a2);
    av.entityValues[0].setStringAggregate(3, a3);
  }
}
