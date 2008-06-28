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

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CEntity_definition extends CEntity_or_view_definition implements EEntity_definition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CEntity_definition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// short_name: protected String a1;   short_name - java inheritance - STRING
	// where_rules: protected Object  - inverse - java inheritance -  ENTITY where_rule
	// generic_supertypes: protected AEntity_or_view_definition a2;   generic_supertypes - java inheritance - LIST OF ENTITY
	protected int a3; // complex - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected int a4; // instantiable - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
	protected int a5; // independent - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
	protected int a6; // abstract_entity - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a6$ = CEntity.initExplicitAttribute(definition, 6);
	protected int a7; // connotational_subtype - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a7$ = CEntity.initExplicitAttribute(definition, 7);
	// supertypes: protected AEntity_definition  - derived - current -  LIST OF ENTITY
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	// attributes: protected Object  - inverse - current -  ENTITY attribute
	protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);
	// uniqueness_rules: protected Object  - inverse - current -  ENTITY uniqueness_rule
	protected static final jsdai.dictionary.CInverse_attribute i2$ = CEntity.initInverseAttribute(definition, 2);
	// global_rules: protected Object  - inverse - current -  ENTITY global_rule
	protected static final jsdai.dictionary.CInverse_attribute i3$ = CEntity.initInverseAttribute(definition, 3);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: generic_supertypes, base type: LIST OF ENTITY
	public static int usedinGeneric_supertypes(EEntity_or_view_definition type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	/// methods for attribute: complex, base type: BOOLEAN
	public boolean testComplex(EEntity_definition type) throws SdaiException {
		return test_boolean(a3);
	}
	public boolean getComplex(EEntity_definition type) throws SdaiException {
		return get_boolean(a3);
	}
	public void setComplex(EEntity_definition type, boolean value) throws SdaiException {
		a3 = set_boolean(value);
	}
	public void unsetComplex(EEntity_definition type) throws SdaiException {
		a3 = unset_boolean();
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
	public boolean testSupertypes(EEntity_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public AEntity_definition getSupertypes(EEntity_definition type) throws SdaiException {
		SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
			return (AEntity_definition) getSupertypes((EEntity_definition)null, _context).getInstanceAggregate(this);
	}
	public Value getSupertypes(EEntity_definition type, SdaiContext _context) throws SdaiException {



				return (jsdai.SExtended_dictionary_schema.FGet_list_of_entity_definition.run(_context, Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_list_0_unique_entity_or_view_definition).set(_context, get(jsdai.SExtended_dictionary_schema.CEntity_or_view_definition.attributeGeneric_supertypes(null)))));
	}
	public static jsdai.dictionary.EAttribute attributeSupertypes(EEntity_definition type) throws SdaiException {
		return d0$;
	}

	// Inverse attribute - attributes : SET[0:-2147483648] OF attribute FOR parent
	public AAttribute getAttributes(EEntity_definition type, ASdaiModel domain) throws SdaiException {
		AAttribute result = (AAttribute)get_inverse_aggregate(i1$);
		CAttribute.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeAttributes(EEntity_definition type) throws SdaiException {
		return i1$;
	}

	// Inverse attribute - uniqueness_rules : SET[0:-2147483648] OF uniqueness_rule FOR parent_entity
	public AUniqueness_rule getUniqueness_rules(EEntity_definition type, ASdaiModel domain) throws SdaiException {
		AUniqueness_rule result = (AUniqueness_rule)get_inverse_aggregate(i2$);
		CUniqueness_rule.usedinParent_entity(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeUniqueness_rules(EEntity_definition type) throws SdaiException {
		return i2$;
	}

	// Inverse attribute - global_rules : SET[0:-2147483648] OF global_rule FOR entities
	public AGlobal_rule getGlobal_rules(EEntity_definition type, ASdaiModel domain) throws SdaiException {
		AGlobal_rule result = (AGlobal_rule)get_inverse_aggregate(i3$);
		CGlobal_rule.usedinEntities(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeGlobal_rules(EEntity_definition type) throws SdaiException {
		return i3$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a3 = 0;
			a4 = 0;
			a5 = 0;
			a6 = 0;
			a7 = 0;
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a3 = av.entityValues[1].getBoolean(0);
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
