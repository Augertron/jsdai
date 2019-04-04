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

// Java class implementing entity dependent_map_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CDependent_map_definition extends CMap_definition implements EDependent_map_definition {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDependent_map_definition.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// super_type: protected Object a1;   super_type - java inheritance - ENTITY map_definition
	// partitions: protected Object  - inverse - java inheritance -  ENTITY map_partition
	// target_parameters: protected Object  - inverse - java inheritance -  ENTITY target_parameter
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // super_type - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected Object a1;
  // partitions - inverse - java inheritance
  // protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
  // target_parameters - inverse - java inheritance
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

  //going through all the attributes: #1730=EXPLICIT_ATTRIBUTE('name',#1728,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1731=EXPLICIT_ATTRIBUTE('super_type',#1728,1,#1728,$,.F.);
  //<01> generating methods for consolidated attribute:  super_type
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  // attribute (java explicit): super_type, base type: entity map_definition
  public static int usedinSuper_type(EMap_definition type, EMap_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }
  //going through all the attributes: #1732=INVERSE_ATTRIBUTE('partitions',#1728,0,#1745,$,#1747,#2207,$,.F.);
  //<01> generating methods for consolidated attribute:  partitions
  //<01-1> supertype, java inheritance
  //<01-1-2> inverse - generateInverseSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1733=INVERSE_ATTRIBUTE('target_parameters',#1728,1,#1839,$,#1842,#2209,$,.F.);
  //<01> generating methods for consolidated attribute:  target_parameters
  //<01-1> supertype, java inheritance
  //<01-1-2> inverse - generateInverseSupertypeJavaInheritedMethodsX()

  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[1].getString(0);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[1].getString(0);
    a1 = av.entityValues[1].getInstance(1, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: dependent_map_definition
		// partial entity: map_definition
		av.entityValues[1].setString(0, a0);
		av.entityValues[1].setInstance(1, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: dependent_map_definition
    // partial entity: map_definition
    av.entityValues[1].setString(0, a0);
    av.entityValues[1].setInstance(1, a1);
  }
}
