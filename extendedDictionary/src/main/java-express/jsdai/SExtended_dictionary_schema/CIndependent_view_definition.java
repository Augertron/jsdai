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

// Java class implementing entity independent_view_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CIndependent_view_definition extends CView_definition implements EIndependent_view_definition {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CIndependent_view_definition.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// short_name: protected String a1;   short_name - java inheritance - STRING
	// where_rules: protected Object  - inverse - java inheritance -  ENTITY where_rule
	// generic_supertypes: protected AEntity_or_view_definition a2;   generic_supertypes - java inheritance - LIST OF ENTITY
	// partitions: protected Object  - inverse - java inheritance -  ENTITY view_partition
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // short_name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected String a1;
  // where_rules - inverse - java inheritance
  // protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
  // generic_supertypes - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  // protected AEntity_or_view_definition a2;
  // partitions - inverse - java inheritance
  // protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);

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

  //going through all the attributes: #1589=EXPLICIT_ATTRIBUTE('name',#1587,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1750=EXPLICIT_ATTRIBUTE('short_name',#1748,0,#7,$,.T.);
  //<01> generating methods for consolidated attribute:  short_name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1751=INVERSE_ATTRIBUTE('where_rules',#1748,0,#1883,$,#1886,#2219,$,.F.);
  //<01> generating methods for consolidated attribute:  where_rules
  //<01-1> supertype, java inheritance
  //<01-1-2> inverse - generateInverseSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1646=EXPLICIT_ATTRIBUTE('generic_supertypes',#1644,0,#2052,$,.F.);
  //<01> generating methods for consolidated attribute:  generic_supertypes
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // methods for attribute: generic_supertypes, base type: LIST OF ENTITY
  public static int usedinGeneric_supertypes(EEntity_or_view_definition type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result)
      throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }
  //going through all the attributes: #1873=EXPLICIT_ATTRIBUTE('generic_supertypes',#1871,$,#2084,#1646,.F.);
  //<02> NOT generating methods for NOT consolidated attribute:  generic_supertypes
  //going through all the attributes: #1874=INVERSE_ATTRIBUTE('partitions',#1871,0,#1875,$,#1877,#2271,$,.F.);
  //<01> generating methods for consolidated attribute:  partitions
  //<01-1> supertype, java inheritance
  //<01-1-2> inverse - generateInverseSupertypeJavaInheritedMethodsX()

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a2 = (AEntity_or_view_definition)av.entityValues[1].getInstanceAggregate(0, a2$, this);
		a1 = av.entityValues[3].getString(0);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      if (a2 instanceof CAggregate) {
        a2.unsetAll();
      }
      a2 = null;
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a2 = (AEntity_or_view_definition) av.entityValues[1].getInstanceAggregate(0, a2$, this);
    a1 = av.entityValues[3].getString(0);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: entity_or_view_definition
		av.entityValues[1].setInstanceAggregate(0, a2);
		// partial entity: independent_view_definition
		// partial entity: named_type
		av.entityValues[3].setString(0, a1);
		// partial entity: view_definition
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: data_type
    av.entityValues[0].setString(0, a0);
    // partial entity: entity_or_view_definition
    av.entityValues[1].setInstanceAggregate(0, a2);
    // partial entity: independent_view_definition
    // partial entity: named_type
    av.entityValues[3].setString(0, a1);
    // partial entity: view_definition
  }
}
