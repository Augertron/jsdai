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

// Java class implementing entity function_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CFunction_definition extends CAlgorithm_definition implements EFunction_definition {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CFunction_definition.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// parameters: protected AParameter a1;   parameters - java inheritance - LIST OF ENTITY
	protected Object a2; // return_type - current entity - ENTITY data_type
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected String a3; // return_type_label - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected A_string a4; // return_type_labels - current entity - LIST OF STRING
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // parameters - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected AParameter a1;
  // return_type - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;
  // return_type_label - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  protected String a3;
  // return_type_labels - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
  protected A_string a4;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a2 == old) {
			a2 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
    if (a2 == old) {
      a2 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1547=EXPLICIT_ATTRIBUTE('name',#1545,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1548=EXPLICIT_ATTRIBUTE('parameters',#1545,1,#2045,$,.F.);
  //<01> generating methods for consolidated attribute:  parameters
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: parameters, base type: LIST OF ENTITY
  public static int usedinParameters(EAlgorithm_definition type, EParameter instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1679=EXPLICIT_ATTRIBUTE('return_type',#1677,0,#1587,$,.F.);
  //<01> generating methods for consolidated attribute:  return_type
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : return_type, base type: entity data_type
  public static int usedinReturn_type(EFunction_definition type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testReturn_type(EFunction_definition type) throws SdaiException {
    return test_instance(a2);
  }

  public EData_type getReturn_type(EFunction_definition type) throws SdaiException {
    return (EData_type) get_instance(a2);
  }

  public void setReturn_type(EFunction_definition type, EData_type value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetReturn_type(EFunction_definition type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeReturn_type(EFunction_definition type) throws SdaiException {
    return a2$;
  }

  //going through all the attributes: #1680=EXPLICIT_ATTRIBUTE('return_type_label',#1677,1,#1522,$,.T.);
  //<01> generating methods for consolidated attribute:  return_type_label
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: return_type_label, base type: STRING
  public boolean testReturn_type_label(EFunction_definition type) throws SdaiException {
    return test_string(a3);
  }

  public String getReturn_type_label(EFunction_definition type) throws SdaiException {
    return get_string(a3);
  }

  public void setReturn_type_label(EFunction_definition type, String value) throws SdaiException {
    a3 = set_string(value);
  }

  public void unsetReturn_type_label(EFunction_definition type) throws SdaiException {
    a3 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeReturn_type_label(EFunction_definition type) throws SdaiException {
    return a3$;
  }

  //going through all the attributes: #1681=EXPLICIT_ATTRIBUTE('return_type_labels',#1677,2,#2056,$,.T.);
  //<01> generating methods for consolidated attribute:  return_type_labels
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: return_type_labels, base type: LIST OF STRING
  public boolean testReturn_type_labels(EFunction_definition type) throws SdaiException {
    return test_aggregate(a4);
  }

  public A_string getReturn_type_labels(EFunction_definition type) throws SdaiException {
    return (A_string) get_aggregate(a4);
  }

  public A_string createReturn_type_labels(EFunction_definition type) throws SdaiException {
    a4 = create_aggregate_string(a4, a4$, 0);
    return a4;
  }

  public void unsetReturn_type_labels(EFunction_definition type) throws SdaiException {
    unset_aggregate(a4);
    a4 = null;
  }

  public static jsdai.dictionary.EAttribute attributeReturn_type_labels(EFunction_definition type) throws SdaiException {
    return a4$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			a2 = unset_instance(a2);
			a3 = null;
			if (a4 instanceof CAggregate)
				a4.unsetAll();
			a4 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = (AParameter)av.entityValues[0].getInstanceAggregate(1, a1$, this);
		a2 = av.entityValues[1].getInstance(0, this, a2$);
		a3 = av.entityValues[1].getString(1);
		a4 = av.entityValues[1].getStringAggregate(2, a4$, this);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      a2 = unset_instance(a2);
      a3 = null;
      if (a4 instanceof CAggregate) {
        a4.unsetAll();
      }
      a4 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = (AParameter) av.entityValues[0].getInstanceAggregate(1, a1$, this);
    a2 = av.entityValues[1].getInstance(0, this, a2$);
    a3 = av.entityValues[1].getString(1);
    a4 = av.entityValues[1].getStringAggregate(2, a4$, this);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: algorithm_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstanceAggregate(1, a1);
		// partial entity: function_definition
		av.entityValues[1].setInstance(0, a2);
		av.entityValues[1].setString(1, a3);
		av.entityValues[1].setStringAggregate(2, a4);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: algorithm_definition
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstanceAggregate(1, a1);
    // partial entity: function_definition
    av.entityValues[1].setInstance(0, a2);
    av.entityValues[1].setString(1, a3);
    av.entityValues[1].setStringAggregate(2, a4);
  }
}
