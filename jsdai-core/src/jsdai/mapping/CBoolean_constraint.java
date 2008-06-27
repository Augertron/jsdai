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

// Java class implementing entity boolean_constraint

package jsdai.mapping;
import jsdai.lang.*;

public class CBoolean_constraint extends CEntity implements EBoolean_constraint {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CBoolean_constraint.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected Object a0; // attribute - non-java inheritance - SELECT attribute_value_constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected int a1; // constraint_value - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// methods for SELECT attribute: attribute
	public static int usedinAttribute(EAttribute_value_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testAttribute(EAttribute_value_constraint type) throws SdaiException {
		return test_instance(a0);
	}

	public jsdai.lang.EEntity getAttribute(EAttribute_value_constraint type) throws jsdai.lang.SdaiException { // case 1
		a0 = get_instance_select(a0);
		return (jsdai.lang.EEntity)a0;
	}

	public void setAttribute(EAttribute_value_constraint type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a0 = set_instance(a0, value);
	}

	public void unsetAttribute(EAttribute_value_constraint type) throws SdaiException {
		a0 = unset_instance(a0);
	}

	public static jsdai.dictionary.EAttribute attributeAttribute(EAttribute_value_constraint type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: constraint_value, base type: BOOLEAN
	public boolean testConstraint_value(EBoolean_constraint type) throws SdaiException {
		return test_boolean(a1);
	}
	public boolean getConstraint_value(EBoolean_constraint type) throws SdaiException {
		return get_boolean(a1);
	}
	public void setConstraint_value(EBoolean_constraint type, boolean value) throws SdaiException {
		a1 = set_boolean(value);
	}
	public void unsetConstraint_value(EBoolean_constraint type) throws SdaiException {
		a1 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeConstraint_value(EBoolean_constraint type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = 0;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this);
		a1 = av.entityValues[1].getBoolean(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_value_constraint
		av.entityValues[0].setInstance(0, a0);
		// partial entity: boolean_constraint
		av.entityValues[1].setBoolean(0, a1);
		// partial entity: constraint
		// partial entity: constraint_attribute
	}
}
