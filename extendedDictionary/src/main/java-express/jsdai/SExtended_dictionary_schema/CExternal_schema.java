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

// Java class implementing entity external_schema

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CExternal_schema extends CEntity implements EExternal_schema {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExternal_schema.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // definition - current entity - ENTITY schema_definition
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // native_schema - current entity - ENTITY schema_definition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// for_types: protected Object  - inverse - current -  ENTITY domain_equivalent_type
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // definition - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // native_schema - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // for_types - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);

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
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    if (a0 == old) {
      a0 = newer;
    }
    if (a1 == old) {
      a1 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1672=EXPLICIT_ATTRIBUTE('definition',#1670,0,#1793,$,.F.);
  //<01> generating methods for consolidated attribute:  definition
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : definition, base type: entity schema_definition
  public static int usedinDefinition(EExternal_schema type, ESchema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testDefinition(EExternal_schema type) throws SdaiException {
    return test_instance(a0);
  }

  public ESchema_definition getDefinition(EExternal_schema type) throws SdaiException {
    return (ESchema_definition) get_instance(a0);
  }

  public void setDefinition(EExternal_schema type, ESchema_definition value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetDefinition(EExternal_schema type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeDefinition(EExternal_schema type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1673=EXPLICIT_ATTRIBUTE('native_schema',#1670,1,#1793,$,.F.);
  //<01> generating methods for consolidated attribute:  native_schema
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : native_schema, base type: entity schema_definition
  public static int usedinNative_schema(EExternal_schema type, ESchema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testNative_schema(EExternal_schema type) throws SdaiException {
    return test_instance(a1);
  }

  public ESchema_definition getNative_schema(EExternal_schema type) throws SdaiException {
    return (ESchema_definition) get_instance(a1);
  }

  public void setNative_schema(EExternal_schema type, ESchema_definition value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetNative_schema(EExternal_schema type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeNative_schema(EExternal_schema type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1674=INVERSE_ATTRIBUTE('for_types',#1670,0,#1625,$,#1629,#2195,$,.F.);
  //<01> generating methods for consolidated attribute:  for_types
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - for_types : SET[1:-2147483648] OF domain_equivalent_type FOR owner
  public ADomain_equivalent_type getFor_types(EExternal_schema type, ASdaiModel domain) throws SdaiException {
    ADomain_equivalent_type result = (ADomain_equivalent_type) get_inverse_aggregate(i0$);
    CDomain_equivalent_type.usedinOwner(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeFor_types(EExternal_schema type) throws SdaiException {
    return i0$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: external_schema
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: external_schema
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setInstance(1, a1);
  }
}
