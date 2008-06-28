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

// Java class implementing entity inner_declaration

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CInner_declaration extends CDeclaration implements EInner_declaration {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CInner_declaration.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// parent: protected Object a0;   parent - java inheritance - ENTITY generic_schema_definition
	// definition: protected Object a1;   definition - java inheritance - SELECT declaration_type
	// parent_schema: protected Object  - derived - java inheritance -  ENTITY schema_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected Object a2; // scope - current entity - SELECT declaration_scope_type
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a2 == old) {
			a2 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: parent, base type: entity generic_schema_definition
	public static int usedinParent(EDeclaration type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	// methods for SELECT attribute: definition
	public static int usedinDefinition(EDeclaration type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	// methods for SELECT attribute: scope
	public static int usedinScope(EInner_declaration type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testScope(EInner_declaration type) throws SdaiException {
		return test_instance(a2);
	}

	public EEntity getScope(EInner_declaration type) throws SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (EEntity)a2;
	}

	public void setScope(EInner_declaration type, EEntity value) throws SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetScope(EInner_declaration type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeScope(EInner_declaration type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[1].getInstance(0, this, a2$);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: declaration
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		// partial entity: inner_declaration
		av.entityValues[1].setInstance(0, a2);
	}
}
