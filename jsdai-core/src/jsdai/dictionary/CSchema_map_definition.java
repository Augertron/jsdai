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

// Java class implementing entity schema_map_definition

package jsdai.dictionary;
import jsdai.lang.*;

public class CSchema_map_definition extends CEntity implements ESchema_map_definition {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected String a1; // identification - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	// view_declarations: protected Object  - inverse - current -  ENTITY view_declaration
	protected static jsdai.dictionary.CInverse_attribute i0$;
	// map_declarations: protected Object  - inverse - current -  ENTITY map_declaration
	protected static jsdai.dictionary.CInverse_attribute i1$;
	// source_schema_specifications: protected Object  - inverse - current -  ENTITY reference_from_specification_as_source
	protected static jsdai.dictionary.CInverse_attribute i2$;
	// target_schema_specifications: protected Object  - inverse - current -  ENTITY reference_from_specification_as_target
	protected static jsdai.dictionary.CInverse_attribute i3$;

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

	// Inverse attribute - view_declarations : SET[0:-2147483648] OF view_declaration FOR parent
	public AView_declaration getView_declarations(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
		AView_declaration result = new AView_declaration();
		CView_declaration.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeView_declarations(ESchema_map_definition type) throws SdaiException {
		return i0$;
	}

	// Inverse attribute - map_declarations : SET[0:-2147483648] OF map_declaration FOR parent
	public AMap_declaration getMap_declarations(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
		AMap_declaration result = new AMap_declaration();
		CMap_declaration.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeMap_declarations(ESchema_map_definition type) throws SdaiException {
		return i1$;
	}

	// Inverse attribute - source_schema_specifications : SET[1:-2147483648] OF reference_from_specification_as_source FOR current_schema
	public AReference_from_specification_as_source getSource_schema_specifications(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
		AReference_from_specification_as_source result = new AReference_from_specification_as_source();
		CReference_from_specification_as_source.usedinCurrent_schema(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeSource_schema_specifications(ESchema_map_definition type) throws SdaiException {
		return i2$;
	}

	// Inverse attribute - target_schema_specifications : SET[1:-2147483648] OF reference_from_specification_as_target FOR current_schema
	public AReference_from_specification_as_target getTarget_schema_specifications(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
		AReference_from_specification_as_target result = new AReference_from_specification_as_target();
		CReference_from_specification_as_target.usedinCurrent_schema(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeTarget_schema_specifications(ESchema_map_definition type) throws SdaiException {
		return i3$;
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
		// partial entity: schema_map_definition
	}
}
