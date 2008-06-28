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

// Java class implementing entity string_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CString_type extends CSimple_type implements EString_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CString_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	protected Object a1; // width - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected int a2; // fixed_width - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: width, base type: entity bound
	public static int usedinWidth(EString_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testWidth(EString_type type) throws SdaiException {
		return test_instance(a1);
	}
	public EBound getWidth(EString_type type) throws SdaiException {
		a1 = get_instance(a1);
		return (EBound)a1;
	}
	public void setWidth(EString_type type, EBound value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetWidth(EString_type type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeWidth(EString_type type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: fixed_width, base type: BOOLEAN
	public boolean testFixed_width(EString_type type) throws SdaiException {
		return test_boolean(a2);
	}
	public boolean getFixed_width(EString_type type) throws SdaiException {
		return get_boolean(a2);
	}
	public void setFixed_width(EString_type type, boolean value) throws SdaiException {
		a2 = set_boolean(value);
	}
	public void unsetFixed_width(EString_type type) throws SdaiException {
		a2 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeFixed_width(EString_type type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = 0;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[2].getInstance(0, this, a1$);
		a2 = av.entityValues[2].getBoolean(1);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: simple_type
		// partial entity: string_type
		av.entityValues[2].setInstance(0, a1);
		av.entityValues[2].setBoolean(1, a2);
	}
}
