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

package jsdai.dictionary;
import jsdai.lang.*;

public class CSchema_definition extends SchemaDefinition implements ESchema_definition {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected String a1; // identification - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	// entity_declarations: protected Object  - inverse - current -  ENTITY entity_declaration
	protected static jsdai.dictionary.CInverse_attribute i0$;
	// type_declarations: protected Object  - inverse - current -  ENTITY type_declaration
	protected static jsdai.dictionary.CInverse_attribute i1$;
	// rule_declarations: protected Object  - inverse - current -  ENTITY rule_declaration
	protected static jsdai.dictionary.CInverse_attribute i2$;
	// algorithm_declarations: protected Object  - inverse - current -  ENTITY algorithm_declaration
	protected static jsdai.dictionary.CInverse_attribute i3$;
	// external_schemas: protected Object  - inverse - current -  ENTITY external_schema
	protected static jsdai.dictionary.CInverse_attribute i4$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EGeneric_schema_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EGeneric_schema_definition type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EGeneric_schema_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EGeneric_schema_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EGeneric_schema_definition type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: name, base type: STRING
	public boolean testName(ESchema_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ESchema_definition type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(ESchema_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ESchema_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ESchema_definition type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: identification, base type: STRING
	public boolean testIdentification(EGeneric_schema_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getIdentification(EGeneric_schema_definition type) throws SdaiException {
		return get_string(a1);
	}
	public void setIdentification(EGeneric_schema_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetIdentification(EGeneric_schema_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeIdentification(EGeneric_schema_definition type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: identification, base type: STRING
	public boolean testIdentification(ESchema_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getIdentification(ESchema_definition type) throws SdaiException {
		return get_string(a1);
	}
	public void setIdentification(ESchema_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetIdentification(ESchema_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeIdentification(ESchema_definition type) throws SdaiException {
		return a1$;
	}

	// Inverse attribute - entity_declarations : SET[0:-2147483648] OF entity_declaration FOR parent
	public AEntity_declaration getEntity_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
		AEntity_declaration result = new AEntity_declaration();
		CEntity_declaration.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeEntity_declarations(ESchema_definition type) throws SdaiException {
		return i0$;
	}

	// Inverse attribute - type_declarations : SET[0:-2147483648] OF type_declaration FOR parent
	public AType_declaration getType_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
		AType_declaration result = new AType_declaration();
		CType_declaration.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeType_declarations(ESchema_definition type) throws SdaiException {
		return i1$;
	}

	// Inverse attribute - rule_declarations : SET[0:-2147483648] OF rule_declaration FOR parent
	public ARule_declaration getRule_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
		ARule_declaration result = new ARule_declaration();
		CRule_declaration.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeRule_declarations(ESchema_definition type) throws SdaiException {
		return i2$;
	}

	// Inverse attribute - algorithm_declarations : SET[0:-2147483648] OF algorithm_declaration FOR parent
	public AAlgorithm_declaration getAlgorithm_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException {
		AAlgorithm_declaration result = new AAlgorithm_declaration();
		CAlgorithm_declaration.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeAlgorithm_declarations(ESchema_definition type) throws SdaiException {
		return i3$;
	}

	// Inverse attribute - external_schemas : SET[1:-2147483648] OF external_schema FOR native_schema
	public AExternal_schema getExternal_schemas(ESchema_definition type, ASdaiModel domain) throws SdaiException {
		AExternal_schema result = new AExternal_schema();
		CExternal_schema.usedinNative_schema(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeExternal_schemas(ESchema_definition type) throws SdaiException {
		return i4$;
	}


	/*---------------------- setAll() --------------------*/

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

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: generic_schema_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setString(1, a1);
		// partial entity: schema_definition
	}
}
