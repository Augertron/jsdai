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

// Java class implementing entity schema_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CSchema_definition extends CGeneric_schema_definition implements ESchema_definition {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSchema_definition.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// identification: protected String a1;   identification - java inheritance - STRING
	// entity_declarations: protected Object  - inverse - current -  ENTITY entity_declaration
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
	// type_declarations: protected Object  - inverse - current -  ENTITY type_declaration
	protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);
	// rule_declarations: protected Object  - inverse - current -  ENTITY rule_declaration
	protected static final jsdai.dictionary.CInverse_attribute i2$ = CEntity.initInverseAttribute(definition, 2);
	// algorithm_declarations: protected Object  - inverse - current -  ENTITY algorithm_declaration
	protected static final jsdai.dictionary.CInverse_attribute i3$ = CEntity.initInverseAttribute(definition, 3);
	// external_schemas: protected Object  - inverse - current -  ENTITY external_schema
	protected static final jsdai.dictionary.CInverse_attribute i4$ = CEntity.initInverseAttribute(definition, 4);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // identification - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected String a1;
  // entity_declarations - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
  // type_declarations - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);
  // rule_declarations - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i2$ = CEntity.initInverseAttribute(definition, 2);
  // algorithm_declarations - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i3$ = CEntity.initInverseAttribute(definition, 3);
  // external_schemas - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i4$ = CEntity.initInverseAttribute(definition, 4);

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

  //going through all the attributes: #1684=EXPLICIT_ATTRIBUTE('name',#1682,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1685=EXPLICIT_ATTRIBUTE('identification',#1682,1,#1524,$,.T.);
  //<01> generating methods for consolidated attribute:  identification
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1795=INVERSE_ATTRIBUTE('entity_declarations',#1793,0,#1630,$,#1597,#2227,$,.F.);
  //<01> generating methods for consolidated attribute:  entity_declarations
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - entity_declarations : SET[0:-2147483648] OF entity_declaration FOR parent
  public AEntity_declaration getEntity_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
    AEntity_declaration result = (AEntity_declaration) get_inverse_aggregate(i0$);
    CEntity_declaration.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeEntity_declarations(ESchema_definition type) throws SdaiException {
    return i0$;
  }

  //going through all the attributes: #1796=INVERSE_ATTRIBUTE('type_declarations',#1793,1,#1845,$,#1597,#2229,$,.F.);
  //<01> generating methods for consolidated attribute:  type_declarations
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - type_declarations : SET[0:-2147483648] OF type_declaration FOR parent
  public AType_declaration getType_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
    AType_declaration result = (AType_declaration) get_inverse_aggregate(i1$);
    CType_declaration.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeType_declarations(ESchema_definition type) throws SdaiException {
    return i1$;
  }

  //going through all the attributes: #1797=INVERSE_ATTRIBUTE('rule_declarations',#1793,2,#1791,$,#1597,#2231,$,.F.);
  //<01> generating methods for consolidated attribute:  rule_declarations
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - rule_declarations : SET[0:-2147483648] OF rule_declaration FOR parent
  public ARule_declaration getRule_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
    ARule_declaration result = (ARule_declaration) get_inverse_aggregate(i2$);
    CRule_declaration.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeRule_declarations(ESchema_definition type) throws SdaiException {
    return i2$;
  }

  //going through all the attributes: #1798=INVERSE_ATTRIBUTE('algorithm_declarations',#1793,3,#1543,$,#1597,#2233,$,.F.);
  //<01> generating methods for consolidated attribute:  algorithm_declarations
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - algorithm_declarations : SET[0:-2147483648] OF algorithm_declaration FOR parent
  public AAlgorithm_declaration getAlgorithm_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
    AAlgorithm_declaration result = (AAlgorithm_declaration) get_inverse_aggregate(i3$);
    CAlgorithm_declaration.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeAlgorithm_declarations(ESchema_definition type) throws SdaiException {
    return i3$;
  }

  //going through all the attributes: #1799=INVERSE_ATTRIBUTE('external_schemas',#1793,4,#1670,$,#1673,#2235,$,.F.);
  //<01> generating methods for consolidated attribute:  external_schemas
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - external_schemas : SET[1:-2147483648] OF external_schema FOR native_schema
  public AExternal_schema getExternal_schemas(ESchema_definition type, ASdaiModel domain) throws SdaiException {
    AExternal_schema result = (AExternal_schema) get_inverse_aggregate(i4$);
    CExternal_schema.usedinNative_schema(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeExternal_schemas(ESchema_definition type) throws SdaiException {
    return i4$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getString(1);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[0].getString(1);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: generic_schema_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setString(1, a1);
		// partial entity: schema_definition
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: generic_schema_definition
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setString(1, a1);
    // partial entity: schema_definition
  }
}
