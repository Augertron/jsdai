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

// Java class implementing entity generic_schema_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CGeneric_schema_definition extends CEntity implements EGeneric_schema_definition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CGeneric_schema_definition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected String a1; // identification - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

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
	}
}
