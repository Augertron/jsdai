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

// Java class implementing entity aggregate_size_constraint

package jsdai.mapping;
import jsdai.lang.*;

public class CAggregate_size_constraint extends CEntity implements EAggregate_size_constraint {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAggregate_size_constraint.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected int a0; // size - current entity - INTEGER
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // attribute - current entity - SELECT aggregate_member_constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: size, base type: INTEGER
	public boolean testSize(EAggregate_size_constraint type) throws SdaiException {
		return test_integer(a0);
	}
	public int getSize(EAggregate_size_constraint type) throws SdaiException {
		return get_integer(a0);
	}
	public void setSize(EAggregate_size_constraint type, int value) throws SdaiException {
		a0 = set_integer(value);
	}
	public void unsetSize(EAggregate_size_constraint type) throws SdaiException {
		a0 = unset_integer();
	}
	public static jsdai.dictionary.EAttribute attributeSize(EAggregate_size_constraint type) throws SdaiException {
		return a0$;
	}

	// methods for SELECT attribute: attribute
	public static int usedinAttribute(EAggregate_size_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testAttribute(EAggregate_size_constraint type) throws SdaiException {
		return test_instance(a1);
	}

	public jsdai.lang.EEntity getAttribute(EAggregate_size_constraint type) throws jsdai.lang.SdaiException { // case 1
		a1 = get_instance_select(a1);
		return (jsdai.lang.EEntity)a1;
	}

	public void setAttribute(EAggregate_size_constraint type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a1 = set_instance(a1, value);
	}

	public void unsetAttribute(EAggregate_size_constraint type) throws SdaiException {
		a1 = unset_instance(a1);
	}

	public static jsdai.dictionary.EAttribute attributeAttribute(EAggregate_size_constraint type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = Integer.MIN_VALUE;
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[0].getInteger(0);
		a1 = av.entityValues[0].getInstance(1, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: aggregate_size_constraint
		av.entityValues[0].setInteger(0, a0);
		av.entityValues[0].setInstance(1, a1);
		// partial entity: constraint
		// partial entity: constraint_attribute
	}
}
