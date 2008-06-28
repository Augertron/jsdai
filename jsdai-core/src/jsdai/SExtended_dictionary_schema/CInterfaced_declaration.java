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

// Java class implementing entity interfaced_declaration

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CInterfaced_declaration extends CDeclaration implements EInterfaced_declaration {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CInterfaced_declaration.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// parent: protected Object a0;   parent - java inheritance - ENTITY generic_schema_definition
	// definition: protected Object a1;   definition - java inheritance - SELECT declaration_type
	// parent_schema: protected Object  - derived - java inheritance -  ENTITY schema_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected String a2; // alias_name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
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
	/// methods for attribute: alias_name, base type: STRING
	public boolean testAlias_name(EInterfaced_declaration type) throws SdaiException {
		return test_string(a2);
	}
	public String getAlias_name(EInterfaced_declaration type) throws SdaiException {
		return get_string(a2);
	}
	public void setAlias_name(EInterfaced_declaration type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetAlias_name(EInterfaced_declaration type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeAlias_name(EInterfaced_declaration type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[1].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: declaration
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		// partial entity: interfaced_declaration
		av.entityValues[1].setString(0, a2);
	}
}
