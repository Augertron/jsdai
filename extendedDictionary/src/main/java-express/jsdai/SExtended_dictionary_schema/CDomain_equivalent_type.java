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

// Java class implementing entity domain_equivalent_type

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CDomain_equivalent_type extends CEntity implements EDomain_equivalent_type {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDomain_equivalent_type.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected Object a0; // external_type - current entity - ENTITY named_type
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // native_type - current entity - ENTITY named_type
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // owner - current entity - ENTITY external_schema
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // external_type - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected Object a0;
  // native_type - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected Object a1;
  // owner - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;

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
		if (a2 == old) {
			a2 = newer;
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
    if (a2 == old) {
      a2 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1627=EXPLICIT_ATTRIBUTE('external_type',#1625,0,#1748,$,.F.);
  //<01> generating methods for consolidated attribute:  external_type
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : external_type, base type: entity named_type
  public static int usedinExternal_type(EDomain_equivalent_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a0$, domain, result);
  }

  public boolean testExternal_type(EDomain_equivalent_type type) throws SdaiException {
    return test_instance(a0);
  }

  public ENamed_type getExternal_type(EDomain_equivalent_type type) throws SdaiException {
    return (ENamed_type) get_instance(a0);
  }

  public void setExternal_type(EDomain_equivalent_type type, ENamed_type value) throws SdaiException {
    a0 = set_instance(a0, value);
  }

  public void unsetExternal_type(EDomain_equivalent_type type) throws SdaiException {
    a0 = unset_instance(a0);
  }

  public static jsdai.dictionary.EAttribute attributeExternal_type(EDomain_equivalent_type type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1628=EXPLICIT_ATTRIBUTE('native_type',#1625,1,#1748,$,.F.);
  //<01> generating methods for consolidated attribute:  native_type
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : native_type, base type: entity named_type
  public static int usedinNative_type(EDomain_equivalent_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testNative_type(EDomain_equivalent_type type) throws SdaiException {
    return test_instance(a1);
  }

  public ENamed_type getNative_type(EDomain_equivalent_type type) throws SdaiException {
    return (ENamed_type) get_instance(a1);
  }

  public void setNative_type(EDomain_equivalent_type type, ENamed_type value) throws SdaiException {
    a1 = set_instance(a1, value);
  }

  public void unsetNative_type(EDomain_equivalent_type type) throws SdaiException {
    a1 = unset_instance(a1);
  }

  public static jsdai.dictionary.EAttribute attributeNative_type(EDomain_equivalent_type type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1629=EXPLICIT_ATTRIBUTE('owner',#1625,2,#1670,$,.F.);
  //<01> generating methods for consolidated attribute:  owner
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : owner, base type: entity external_schema
  public static int usedinOwner(EDomain_equivalent_type type, EExternal_schema instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testOwner(EDomain_equivalent_type type) throws SdaiException {
    return test_instance(a2);
  }

  public EExternal_schema getOwner(EDomain_equivalent_type type) throws SdaiException {
    return (EExternal_schema) get_instance(a2);
  }

  public void setOwner(EDomain_equivalent_type type, EExternal_schema value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetOwner(EDomain_equivalent_type type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeOwner(EDomain_equivalent_type type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInstance(2, this, a2$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = unset_instance(a0);
      a1 = unset_instance(a1);
      a2 = unset_instance(a2);
      return;
    }
    a0 = av.entityValues[0].getInstance(0, this, a0$);
    a1 = av.entityValues[0].getInstance(1, this, a1$);
    a2 = av.entityValues[0].getInstance(2, this, a2$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: domain_equivalent_type
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: domain_equivalent_type
    av.entityValues[0].setInstance(0, a0);
    av.entityValues[0].setInstance(1, a1);
    av.entityValues[0].setInstance(2, a2);
  }
}
