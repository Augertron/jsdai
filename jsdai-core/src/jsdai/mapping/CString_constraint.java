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

// Java class implementing entity string_constraint

package jsdai.mapping;
import jsdai.lang.*;

public class CString_constraint extends CEntity implements EString_constraint {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CString_constraint.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected Object a0; // attribute - non-java inheritance - SELECT attribute_value_constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected String a1; // constraint_value - current entity - STRING
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

	/// methods for attribute: constraint_value, base type: STRING
	public boolean testConstraint_value(EString_constraint type) throws SdaiException {
		return test_string(a1);
	}
	public String getConstraint_value(EString_constraint type) throws SdaiException {
		return get_string(a1);
	}
	public void setConstraint_value(EString_constraint type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetConstraint_value(EString_constraint type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeConstraint_value(EString_constraint type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this);
		a1 = av.entityValues[3].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_value_constraint
		av.entityValues[0].setInstance(0, a0);
		// partial entity: constraint
		// partial entity: constraint_attribute
		// partial entity: string_constraint
		av.entityValues[3].setString(0, a1);
	}
}
