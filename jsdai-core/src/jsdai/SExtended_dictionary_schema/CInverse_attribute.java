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

// Java class implementing entity inverse_attribute

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CInverse_attribute extends CAttribute implements EInverse_attribute {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CInverse_attribute.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// parent: protected Object a1;   parent - java inheritance - ENTITY entity_or_view_definition
	// order: protected int a2;   order - java inheritance - INTEGER
	// parent_entity: protected Object  - derived - java inheritance -  ENTITY entity_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected Object a3; // domain - current entity - ENTITY entity_definition
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected Object a4; // redeclaring - current entity - ENTITY inverse_attribute
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
	protected Object a5; // inverted_attr - current entity - ENTITY explicit_attribute
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
	protected Object a6; // min_cardinality - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a6$ = CEntity.initExplicitAttribute(definition, 6);
	protected Object a7; // max_cardinality - current entity - ENTITY bound
	protected static final jsdai.dictionary.CExplicit_attribute a7$ = CEntity.initExplicitAttribute(definition, 7);
	protected int a8; // duplicates - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a8$ = CEntity.initExplicitAttribute(definition, 8);

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
		if (a5 == old) {
			a5 = newer;
		}
		if (a6 == old) {
			a6 = newer;
		}
		if (a7 == old) {
			a7 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: parent, base type: entity entity_or_view_definition
	public static int usedinParent(EAttribute type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	// attribute: domain, base type: entity entity_definition
	public static int usedinDomain(EInverse_attribute type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testDomain(EInverse_attribute type) throws SdaiException {
		return test_instance(a3);
	}
	public EEntity_definition getDomain(EInverse_attribute type) throws SdaiException {
		a3 = get_instance(a3);
		return (EEntity_definition)a3;
	}
	public void setDomain(EInverse_attribute type, EEntity_definition value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetDomain(EInverse_attribute type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeDomain(EInverse_attribute type) throws SdaiException {
		return a3$;
	}

	// attribute: redeclaring, base type: entity inverse_attribute
	public static int usedinRedeclaring(EInverse_attribute type, EInverse_attribute instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testRedeclaring(EInverse_attribute type) throws SdaiException {
		return test_instance(a4);
	}
	public EInverse_attribute getRedeclaring(EInverse_attribute type) throws SdaiException {
		a4 = get_instance(a4);
		return (EInverse_attribute)a4;
	}
	public void setRedeclaring(EInverse_attribute type, EInverse_attribute value) throws SdaiException {
		a4 = set_instance(a4, value);
	}
	public void unsetRedeclaring(EInverse_attribute type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributeRedeclaring(EInverse_attribute type) throws SdaiException {
		return a4$;
	}

	// attribute: inverted_attr, base type: entity explicit_attribute
	public static int usedinInverted_attr(EInverse_attribute type, EExplicit_attribute instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a5$, domain, result);
	}
	public boolean testInverted_attr(EInverse_attribute type) throws SdaiException {
		return test_instance(a5);
	}
	public EExplicit_attribute getInverted_attr(EInverse_attribute type) throws SdaiException {
		a5 = get_instance(a5);
		return (EExplicit_attribute)a5;
	}
	public void setInverted_attr(EInverse_attribute type, EExplicit_attribute value) throws SdaiException {
		a5 = set_instance(a5, value);
	}
	public void unsetInverted_attr(EInverse_attribute type) throws SdaiException {
		a5 = unset_instance(a5);
	}
	public static jsdai.dictionary.EAttribute attributeInverted_attr(EInverse_attribute type) throws SdaiException {
		return a5$;
	}

	// attribute: min_cardinality, base type: entity bound
	public static int usedinMin_cardinality(EInverse_attribute type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a6$, domain, result);
	}
	public boolean testMin_cardinality(EInverse_attribute type) throws SdaiException {
		return test_instance(a6);
	}
	public EBound getMin_cardinality(EInverse_attribute type) throws SdaiException {
		a6 = get_instance(a6);
		return (EBound)a6;
	}
	public void setMin_cardinality(EInverse_attribute type, EBound value) throws SdaiException {
		a6 = set_instance(a6, value);
	}
	public void unsetMin_cardinality(EInverse_attribute type) throws SdaiException {
		a6 = unset_instance(a6);
	}
	public static jsdai.dictionary.EAttribute attributeMin_cardinality(EInverse_attribute type) throws SdaiException {
		return a6$;
	}

	// attribute: max_cardinality, base type: entity bound
	public static int usedinMax_cardinality(EInverse_attribute type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a7$, domain, result);
	}
	public boolean testMax_cardinality(EInverse_attribute type) throws SdaiException {
		return test_instance(a7);
	}
	public EBound getMax_cardinality(EInverse_attribute type) throws SdaiException {
		a7 = get_instance(a7);
		return (EBound)a7;
	}
	public void setMax_cardinality(EInverse_attribute type, EBound value) throws SdaiException {
		a7 = set_instance(a7, value);
	}
	public void unsetMax_cardinality(EInverse_attribute type) throws SdaiException {
		a7 = unset_instance(a7);
	}
	public static jsdai.dictionary.EAttribute attributeMax_cardinality(EInverse_attribute type) throws SdaiException {
		return a7$;
	}

	/// methods for attribute: duplicates, base type: BOOLEAN
	public boolean testDuplicates(EInverse_attribute type) throws SdaiException {
		return test_boolean(a8);
	}
	public boolean getDuplicates(EInverse_attribute type) throws SdaiException {
		return get_boolean(a8);
	}
	public void setDuplicates(EInverse_attribute type, boolean value) throws SdaiException {
		a8 = set_boolean(value);
	}
	public void unsetDuplicates(EInverse_attribute type) throws SdaiException {
		a8 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeDuplicates(EInverse_attribute type) throws SdaiException {
		return a8$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = Integer.MIN_VALUE;
			a3 = unset_instance(a3);
			a4 = unset_instance(a4);
			a5 = unset_instance(a5);
			a6 = unset_instance(a6);
			a7 = unset_instance(a7);
			a8 = 0;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInteger(2);
		a3 = av.entityValues[1].getInstance(0, this, a3$);
		a4 = av.entityValues[1].getInstance(1, this, a4$);
		a5 = av.entityValues[1].getInstance(2, this, a5$);
		a6 = av.entityValues[1].getInstance(3, this, a6$);
		a7 = av.entityValues[1].getInstance(4, this, a7$);
		a8 = av.entityValues[1].getBoolean(5);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInteger(2, a2);
		// partial entity: inverse_attribute
		av.entityValues[1].setInstance(0, a3);
		av.entityValues[1].setInstance(1, a4);
		av.entityValues[1].setInstance(2, a5);
		av.entityValues[1].setInstance(3, a6);
		av.entityValues[1].setInstance(4, a7);
		av.entityValues[1].setBoolean(5, a8);
	}
}
