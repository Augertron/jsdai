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

package jsdai.dictionary;
import jsdai.lang.*;

public class CExplicit_attribute extends CMappingExplicit_attribute implements EExplicit_attribute {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected Object a1; // parent - non-java inheritance - ENTITY entity_or_view_definition
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected int a2; // order - non-java inheritance - INTEGER
	protected static jsdai.dictionary.CExplicit_attribute a2$;
	// parent_entity: protected Object  - derived - non-java inheritance -  ENTITY entity_definition
	protected static jsdai.dictionary.CDerived_attribute d0$;
	protected Object a3; // domain - current entity - SELECT base_type
	protected static jsdai.dictionary.CExplicit_attribute a3$;
	protected Object a4; // redeclaring - current entity - ENTITY explicit_attribute
	protected static jsdai.dictionary.CExplicit_attribute a4$;
	protected int a5; // optional_flag - current entity - BOOLEAN
	protected static jsdai.dictionary.CExplicit_attribute a5$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
		if (a3 == old) {
			a3 = newer;
		}
		if (a4 == old) {
			a4 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EAttribute type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EAttribute type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EAttribute type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EAttribute type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EAttribute type) throws SdaiException {
		return a0$;
	}

	// attribute: parent, base type: entity entity_or_view_definition
	public static int usedinParent(EAttribute type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testParent(EAttribute type) throws SdaiException {
		return test_instance(a1);
	}
	public EEntity_or_view_definition getParent(EAttribute type) throws SdaiException {
		a1 = get_instance(a1);
		return (EEntity_or_view_definition)a1;
	}
	public void setParent(EAttribute type, EEntity_or_view_definition value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetParent(EAttribute type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeParent(EAttribute type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: order, base type: INTEGER
	public boolean testOrder(EAttribute type) throws SdaiException {
		return test_integer(a2);
	}
	public int getOrder(EAttribute type) throws SdaiException {
		return get_integer(a2);
	}
	public void setOrder(EAttribute type, int value) throws SdaiException {
		a2 = set_integer(value);
	}
	public void unsetOrder(EAttribute type) throws SdaiException {
		a2 = unset_integer();
	}
	public static jsdai.dictionary.EAttribute attributeOrder(EAttribute type) throws SdaiException {
		return a2$;
	}

	// derived attribute: parent_entity, base type: entity entity_definition
	public static int usedinParent_entity(EAttribute type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testParent_entity(EAttribute type) throws SdaiException {
		return testParent_entityInternal((EEntity_or_view_definition)a1);
	}
	public EEntity_definition getParent_entity(EAttribute type) throws SdaiException {
		return getParent_entityInternal((EEntity_or_view_definition)a1);
	}
	public static jsdai.dictionary.EAttribute attributeParent_entity(EAttribute type) throws SdaiException {
		return d0$;
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
		a1 = av.entityValues[0].getInstance(1, this);
		a2 = av.entityValues[0].getInteger(2);
		a3 = av.entityValues[1].getInstance(0, this);
		a4 = av.entityValues[1].getInstance(1, this);
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
