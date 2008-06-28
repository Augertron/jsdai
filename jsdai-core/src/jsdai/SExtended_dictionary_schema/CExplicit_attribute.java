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

// Java class implementing entity explicit_attribute

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CExplicit_attribute extends CAttribute implements EExplicit_attribute {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExplicit_attribute.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// parent: protected Object a1;   parent - java inheritance - ENTITY entity_or_view_definition
	// order: protected int a2;   order - java inheritance - INTEGER
	// parent_entity: protected Object  - derived - java inheritance -  ENTITY entity_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected Object a3; // domain - current entity - SELECT base_type
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected Object a4; // redeclaring - current entity - ENTITY explicit_attribute
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
	protected int a5; // optional_flag - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a3 == old) {
			a3 = newer;
		}
		if (a4 == old) {
			a4 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: parent, base type: entity entity_or_view_definition
	public static int usedinParent(EAttribute type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	// methods for SELECT attribute: domain
	public static int usedinDomain(EExplicit_attribute type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testDomain(EExplicit_attribute type) throws SdaiException {
		return test_instance(a3);
	}

	public EEntity getDomain(EExplicit_attribute type) throws SdaiException { // case 1
		a3 = get_instance_select(a3);
		return (EEntity)a3;
	}

	public void setDomain(EExplicit_attribute type, EEntity value) throws SdaiException { // case 1
		a3 = set_instance(a3, value);
	}

	public void unsetDomain(EExplicit_attribute type) throws SdaiException {
		a3 = unset_instance(a3);
	}

	public static jsdai.dictionary.EAttribute attributeDomain(EExplicit_attribute type) throws SdaiException {
		return a3$;
	}

	// attribute: redeclaring, base type: entity explicit_attribute
	public static int usedinRedeclaring(EExplicit_attribute type, EExplicit_attribute instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testRedeclaring(EExplicit_attribute type) throws SdaiException {
		return test_instance(a4);
	}
	public EExplicit_attribute getRedeclaring(EExplicit_attribute type) throws SdaiException {
		a4 = get_instance(a4);
		return (EExplicit_attribute)a4;
	}
	public void setRedeclaring(EExplicit_attribute type, EExplicit_attribute value) throws SdaiException {
		a4 = set_instance(a4, value);
	}
	public void unsetRedeclaring(EExplicit_attribute type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributeRedeclaring(EExplicit_attribute type) throws SdaiException {
		return a4$;
	}

	/// methods for attribute: optional_flag, base type: BOOLEAN
	public boolean testOptional_flag(EExplicit_attribute type) throws SdaiException {
		return test_boolean(a5);
	}
	public boolean getOptional_flag(EExplicit_attribute type) throws SdaiException {
		return get_boolean(a5);
	}
	public void setOptional_flag(EExplicit_attribute type, boolean value) throws SdaiException {
		a5 = set_boolean(value);
	}
	public void unsetOptional_flag(EExplicit_attribute type) throws SdaiException {
		a5 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeOptional_flag(EExplicit_attribute type) throws SdaiException {
		return a5$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = Integer.MIN_VALUE;
			a3 = unset_instance(a3);
			a4 = unset_instance(a4);
			a5 = 0;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInteger(2);
		a3 = av.entityValues[1].getInstance(0, this, a3$);
		a4 = av.entityValues[1].getInstance(1, this, a4$);
		a5 = av.entityValues[1].getBoolean(2);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInteger(2, a2);
		// partial entity: explicit_attribute
		av.entityValues[1].setInstance(0, a3);
		av.entityValues[1].setInstance(1, a4);
		av.entityValues[1].setBoolean(2, a5);
	}
}
