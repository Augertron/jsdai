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

// Java class implementing entity entity_definition

package jsdai.dictionary;
import jsdai.lang.*;

public class CEntity_definition extends CEntityDefinition implements EEntity_definition {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // duplicated as CEntityDefinition.name   // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected String a1; // short_name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	// where_rules: protected Object  - inverse - non-java inheritance -  ENTITY where_rule
	protected static jsdai.dictionary.CInverse_attribute i0$;
	protected AEntity_or_view_definition a2; // generic_supertypes - non-java inheritance - LIST OF ENTITY
	protected static jsdai.dictionary.CExplicit_attribute a2$;
	protected int a3; // duplicated as CEntityDefinition.complex   // complex - current entity - BOOLEAN
	protected static jsdai.dictionary.CExplicit_attribute a3$;
	protected int a4; // instantiable - current entity - BOOLEAN
	protected static jsdai.dictionary.CExplicit_attribute a4$;
	protected int a5; // independent - current entity - BOOLEAN
	protected static jsdai.dictionary.CExplicit_attribute a5$;
	protected int a6; // abstract_entity - current entity - BOOLEAN
	protected static jsdai.dictionary.CExplicit_attribute a6$;
	protected int a7; // connotational_subtype - current entity - BOOLEAN
	protected static jsdai.dictionary.CExplicit_attribute a7$;
	// supertypes: protected AEntity_definition  - derived - current -  LIST OF ENTITY
	protected static jsdai.dictionary.CDerived_attribute d0$;
	protected AEntity_definition supertypes;
	// attributes: protected Object  - inverse - current -  ENTITY attribute
	protected static jsdai.dictionary.CInverse_attribute i1$;
	// uniqueness_rules: protected Object  - inverse - current -  ENTITY uniqueness_rule
	protected static jsdai.dictionary.CInverse_attribute i2$;
	// global_rules: protected Object  - inverse - current -  ENTITY global_rule
	protected static jsdai.dictionary.CInverse_attribute i3$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a2, old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EData_type type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EData_type type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EData_type type, String value) throws SdaiException {
		name = a0 = set_string(value);
	}
	public void unsetName(EData_type type) throws SdaiException {
		name = a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EData_type type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: short_name, base type: STRING
	public boolean testShort_name(ENamed_type type) throws SdaiException {
		return test_string(a1);
	}
	public String getShort_name(ENamed_type type) throws SdaiException {
		return get_string(a1);
	}
	public void setShort_name(ENamed_type type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetShort_name(ENamed_type type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeShort_name(ENamed_type type) throws SdaiException {
		return a1$;
	}

	// Inverse attribute - where_rules : SET[0:-2147483648] OF where_rule FOR parent_item
	public AWhere_rule getWhere_rules(ENamed_type type, ASdaiModel domain) throws SdaiException {
		AWhere_rule result = new AWhere_rule();
		CWhere_rule.usedinParent_item(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeWhere_rules(ENamed_type type) throws SdaiException {
		return i0$;
	}

	// methods for attribute: generic_supertypes, base type: LIST OF ENTITY
	public static int usedinGeneric_supertypes(EEntity_or_view_definition type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		return test_aggregate(a2);
	}
	public AEntity_or_view_definition getGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		return (AEntity_or_view_definition)get_aggregate(a2);
	}
	public AEntity_or_view_definition createGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		a2 = (AEntity_or_view_definition)create_aggregate_class(a2, a2$,  AEntity_or_view_definition.class, 0);
		return a2;
	}
	public void unsetGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		return a2$;
	}

	/// methods for attribute: complex, base type: BOOLEAN
	public boolean testComplex(EEntity_definition type) throws SdaiException {
		return test_boolean(a3);
	}
	public boolean getComplex(EEntity_definition type) throws SdaiException {
		return get_boolean(a3);
	}
	public void setComplex(EEntity_definition type, boolean value) throws SdaiException {
		complex = a3 = set_boolean(value);
	}
	public void unsetComplex(EEntity_definition type) throws SdaiException {
		complex = a3 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeComplex(EEntity_definition type) throws SdaiException {
		return a3$;
	}

	/// methods for attribute: instantiable, base type: BOOLEAN
	public boolean testInstantiable(EEntity_definition type) throws SdaiException {
		return test_boolean(a4);
	}
	public boolean getInstantiable(EEntity_definition type) throws SdaiException {
		return get_boolean(a4);
	}
	public void setInstantiable(EEntity_definition type, boolean value) throws SdaiException {
		a4 = set_boolean(value);
	}
	public void unsetInstantiable(EEntity_definition type) throws SdaiException {
		a4 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeInstantiable(EEntity_definition type) throws SdaiException {
		return a4$;
	}

	/// methods for attribute: independent, base type: BOOLEAN
	public boolean testIndependent(EEntity_definition type) throws SdaiException {
		return test_boolean(a5);
	}
	public boolean getIndependent(EEntity_definition type) throws SdaiException {
		return get_boolean(a5);
	}
	public void setIndependent(EEntity_definition type, boolean value) throws SdaiException {
		a5 = set_boolean(value);
	}
	public void unsetIndependent(EEntity_definition type) throws SdaiException {
		a5 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeIndependent(EEntity_definition type) throws SdaiException {
		return a5$;
	}

	/// methods for attribute: abstract_entity, base type: BOOLEAN
	public boolean testAbstract_entity(EEntity_definition type) throws SdaiException {
		return test_boolean(a6);
	}
	public boolean getAbstract_entity(EEntity_definition type) throws SdaiException {
		return get_boolean(a6);
	}
	public void setAbstract_entity(EEntity_definition type, boolean value) throws SdaiException {
		a6 = set_boolean(value);
	}
	public void unsetAbstract_entity(EEntity_definition type) throws SdaiException {
		a6 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeAbstract_entity(EEntity_definition type) throws SdaiException {
		return a6$;
	}

	/// methods for attribute: connotational_subtype, base type: BOOLEAN
	public boolean testConnotational_subtype(EEntity_definition type) throws SdaiException {
		return test_boolean(a7);
	}
	public boolean getConnotational_subtype(EEntity_definition type) throws SdaiException {
		return get_boolean(a7);
	}
	public void setConnotational_subtype(EEntity_definition type, boolean value) throws SdaiException {
		a7 = set_boolean(value);
	}
	public void unsetConnotational_subtype(EEntity_definition type) throws SdaiException {
		a7 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeConnotational_subtype(EEntity_definition type) throws SdaiException {
		return a7$;
	}

	// derived attribute: supertypes, base type: entity entity_definition
	public static int usedinSupertypes(EEntity_definition type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testSupertypes(EEntity_definition type) throws SdaiException {
		return test_aggregate(a2);
	}
	public AEntity_definition getSupertypes(EEntity_definition type) throws SdaiException {
		return
			supertypes != null
			? supertypes
			: (supertypes = getSupertypesInternal((AEntity_or_view_definition)a2));
	}
	public static jsdai.dictionary.EAttribute attributeSupertypes(EEntity_definition type) throws SdaiException {
		return d0$;
	}

	// Inverse attribute - attributes : SET[0:-2147483648] OF attribute FOR parent
	public AAttribute getAttributes(EEntity_definition type, ASdaiModel domain) throws SdaiException {
		AAttribute result = new AAttribute();
		CAttribute.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeAttributes(EEntity_definition type) throws SdaiException {
		return i1$;
	}

	// Inverse attribute - uniqueness_rules : SET[0:-2147483648] OF uniqueness_rule FOR parent_entity
	public AUniqueness_rule getUniqueness_rules(EEntity_definition type, ASdaiModel domain) throws SdaiException {
		AUniqueness_rule result = new AUniqueness_rule();
		CUniqueness_rule.usedinParent_entity(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeUniqueness_rules(EEntity_definition type) throws SdaiException {
		return i2$;
	}

	// Inverse attribute - global_rules : SET[0:-2147483648] OF global_rule FOR entities
	public AGlobal_rule getGlobal_rules(EEntity_definition type, ASdaiModel domain) throws SdaiException {
		AGlobal_rule result = new AGlobal_rule();
		CGlobal_rule.usedinEntities(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeGlobal_rules(EEntity_definition type) throws SdaiException {
		return i3$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			name = a0 = null;
			complex = a3 = 0;
			a4 = 0;
			a5 = 0;
			a6 = 0;
			a7 = 0;
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			supertypes = null;
			a1 = null;
			return;
		}
		name = a0 = av.entityValues[0].getString(0);
		complex = a3 = av.entityValues[1].getBoolean(0);
		a4 = av.entityValues[1].getBoolean(1);
		a5 = av.entityValues[1].getBoolean(2);
		a6 = av.entityValues[1].getBoolean(3);
		a7 = av.entityValues[1].getBoolean(4);
		a2 = (AEntity_or_view_definition)av.entityValues[2].getInstanceAggregate(0, a2$, this);
		a1 = av.entityValues[3].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: entity_definition
		av.entityValues[1].setBoolean(0, a3);
		av.entityValues[1].setBoolean(1, a4);
		av.entityValues[1].setBoolean(2, a5);
		av.entityValues[1].setBoolean(3, a6);
		av.entityValues[1].setBoolean(4, a7);
		// partial entity: entity_or_view_definition
		av.entityValues[2].setInstanceAggregate(0, a2);
		// partial entity: named_type
		av.entityValues[3].setString(0, a1);
	}

}
