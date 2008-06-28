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

// Java class implementing entity real_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CReal_type extends CSimple_type implements EReal_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CReal_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	protected Object a1; // precision - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

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

	// attribute: precision, base type: entity bound
	public static int usedinPrecision(EReal_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testPrecision(EReal_type type) throws SdaiException {
		return test_instance(a1);
	}
	public EBound getPrecision(EReal_type type) throws SdaiException {
		a1 = get_instance(a1);
		return (EBound)a1;
	}
	public void setPrecision(EReal_type type, EBound value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetPrecision(EReal_type type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributePrecision(EReal_type type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getInstance(0, this, a1$);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: real_type
		av.entityValues[1].setInstance(0, a1);
		// partial entity: simple_type
	}
}
