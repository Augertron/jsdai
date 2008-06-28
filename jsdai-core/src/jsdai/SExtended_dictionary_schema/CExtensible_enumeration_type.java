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

// Java class implementing entity extensible_enumeration_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CExtensible_enumeration_type extends CEnumeration_type implements EExtensible_enumeration_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExtensible_enumeration_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// local_elements: protected A_string a1;   local_elements - java inheritance - LIST OF STRING
	// elements: protected A_string  - derived - java inheritance -  LIST OF STRING
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getStringAggregate(0, a1$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: enumeration_type
		av.entityValues[1].setStringAggregate(0, a1);
		// partial entity: extensible_enumeration_type
	}
}
