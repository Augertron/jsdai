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

// Java class implementing entity select_constraint

package jsdai.mapping;
import jsdai.lang.*;

public class CSelect_constraint extends CEntity implements ESelect_constraint {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSelect_constraint.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected jsdai.dictionary.ADefined_type a0; // data_type - current entity - LIST OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // attribute - current entity - SELECT select_constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a0, old, newer);
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: data_type, base type: LIST OF ENTITY
	public static int usedinData_type(ESelect_constraint type, jsdai.dictionary.EDefined_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testData_type(ESelect_constraint type) throws SdaiException {
		return test_aggregate(a0);
	}
	public jsdai.dictionary.ADefined_type getData_type(ESelect_constraint type) throws SdaiException {
		return (jsdai.dictionary.ADefined_type)get_aggregate(a0);
	}
	public jsdai.dictionary.ADefined_type createData_type(ESelect_constraint type) throws SdaiException {
		a0 = (jsdai.dictionary.ADefined_type)create_aggregate_class(a0, a0$,  jsdai.dictionary.ADefined_type.class, 0);
		return a0;
	}
	public void unsetData_type(ESelect_constraint type) throws SdaiException {
		unset_aggregate(a0);
		a0 = null;
	}
	public static jsdai.dictionary.EAttribute attributeData_type(ESelect_constraint type) throws SdaiException {
		return a0$;
	}

	// methods for SELECT attribute: attribute
	public static int usedinAttribute(ESelect_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testAttribute(ESelect_constraint type) throws SdaiException {
		return test_instance(a1);
	}

	public jsdai.lang.EEntity getAttribute(ESelect_constraint type) throws jsdai.lang.SdaiException { // case 1
		a1 = get_instance_select(a1);
		return (jsdai.lang.EEntity)a1;
	}

	public void setAttribute(ESelect_constraint type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a1 = set_instance(a1, value);
	}

	public void unsetAttribute(ESelect_constraint type) throws SdaiException {
		a1 = unset_instance(a1);
	}

	public static jsdai.dictionary.EAttribute attributeAttribute(ESelect_constraint type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			if (a0 instanceof CAggregate)
				a0.unsetAll();
			a0 = null;
			a1 = unset_instance(a1);
			return;
		}
		a0 = (jsdai.dictionary.ADefined_type)av.entityValues[2].getInstanceAggregate(0, a0$, this);
		a1 = av.entityValues[2].getInstance(1, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: constraint
		// partial entity: constraint_attribute
		// partial entity: select_constraint
		av.entityValues[2].setInstanceAggregate(0, a0);
		av.entityValues[2].setInstance(1, a1);
	}
}
