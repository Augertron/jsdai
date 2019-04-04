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

// Java class implementing entity source_parameter

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CSource_parameter extends CMap_or_view_input_parameter implements ESource_parameter {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSource_parameter.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// parent: protected Object a1;   parent - java inheritance - ENTITY map_or_view_partition
	// extent: protected Object a2;   extent - java inheritance - ENTITY data_type
	// order: protected int a3;   order - java inheritance - INTEGER
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // parent - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // extent - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected Object a2;
  // order - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
  // protected int a3;

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

  //going through all the attributes: #1736=EXPLICIT_ATTRIBUTE('name',#1734,0,#1522,$,.T.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1737=EXPLICIT_ATTRIBUTE('parent',#1734,1,#1740,$,.F.);
  //<01> generating methods for consolidated attribute:  parent
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): parent, base type: entity map_or_view_partition
  public static int usedinParent(EMap_or_view_input_parameter type, EMap_or_view_partition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  //going through all the attributes: #1738=EXPLICIT_ATTRIBUTE('extent',#1734,2,#1587,$,.T.);
  //<01> generating methods for consolidated attribute:  extent
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): extent, base type: entity data_type
  public static int usedinExtent(EMap_or_view_input_parameter type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }
  //going through all the attributes: #1739=EXPLICIT_ATTRIBUTE('order',#1734,3,#2,$,.F.);
  //<01> generating methods for consolidated attribute:  order
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1819=EXPLICIT_ATTRIBUTE('name',#1817,$,#1522,#1736,.F.);
  //<02> NOT generating methods for NOT consolidated attribute:  name
  //going through all the attributes: #1820=EXPLICIT_ATTRIBUTE('extent',#1817,$,#1644,#1738,.F.);
  //<02> NOT generating methods for NOT consolidated attribute:  extent

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
		// partial entity: source_parameter
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: map_or_view_input_parameter
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstance(2, a2);
    av.entityValues[0].setInteger(3, a3);
    // partial entity: source_parameter
  }
}
