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

// Java class implementing entity type_constraint

package jsdai.mapping;
import jsdai.lang.*;

public class CType_constraint extends CEntity implements EType_constraint {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CType_constraint.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected Object a0; // domain - current entity - ENTITY entity_definition
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // constraints - current entity - SELECT constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: domain, base type: entity entity_definition
	public static int usedinDomain(EType_constraint type, jsdai.dictionary.EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testDomain(EType_constraint type) throws SdaiException {
		return test_instance(a0);
	}
	public jsdai.dictionary.EEntity_definition getDomain(EType_constraint type) throws SdaiException {
		a0 = get_instance(a0);
		return (jsdai.dictionary.EEntity_definition)a0;
	}
	public void setDomain(EType_constraint type, jsdai.dictionary.EEntity_definition value) throws SdaiException {
		a0 = set_instance(a0, value);
	}
	public void unsetDomain(EType_constraint type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeDomain(EType_constraint type) throws SdaiException {
		return a0$;
	}

	// methods for SELECT attribute: constraints
	public static int usedinConstraints(EType_constraint type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testConstraints(EType_constraint type) throws SdaiException {
		return test_instance(a1);
	}

	public jsdai.lang.EEntity getConstraints(EType_constraint type) throws jsdai.lang.SdaiException { // case 1
		a1 = get_instance_select(a1);
		return (jsdai.lang.EEntity)a1;
	}

	public void setConstraints(EType_constraint type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a1 = set_instance(a1, value);
	}

	public void unsetConstraints(EType_constraint type) throws SdaiException {
		a1 = unset_instance(a1);
	}

	public static jsdai.dictionary.EAttribute attributeConstraints(EType_constraint type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[1].getInstance(0, this);
		a1 = av.entityValues[1].getInstance(1, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: constraint
		// partial entity: type_constraint
		av.entityValues[1].setInstance(0, a0);
		av.entityValues[1].setInstance(1, a1);
	}
}
