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

// Java class implementing entity documentation

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CDocumentation extends CAnnotation implements EDocumentation {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDocumentation.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// target: protected Object a0;   target - java inheritance - SELECT documentation_object
	// values: protected A_string a1;   values - java inheritance - LIST OF STRING

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for SELECT attribute: target
	public static int usedinTarget(EAnnotation type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}

	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getStringAggregate(1, a1$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: annotation
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setStringAggregate(1, a1);
		// partial entity: documentation
	}
}
