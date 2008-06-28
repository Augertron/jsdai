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

// Java class implementing entity sub_supertype_constraint

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CSub_supertype_constraint extends CEntity implements ESub_supertype_constraint {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSub_supertype_constraint.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // generic_supertype - current entity - ENTITY entity_or_view_definition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected AEntity_definition a2; // total_cover - current entity - SET OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected Object a3; // constraint - current entity - ENTITY subtype_expression
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected int a4; // abstract_supertype - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
	// super_type: protected Object  - derived - current -  ENTITY entity_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
		changeReferencesAggregate(a2, old, newer);
		if (a3 == old) {
			a3 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(ESub_supertype_constraint type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ESub_supertype_constraint type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(ESub_supertype_constraint type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ESub_supertype_constraint type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ESub_supertype_constraint type) throws SdaiException {
		return a0$;
	}

	// attribute: generic_supertype, base type: entity entity_or_view_definition
	public static int usedinGeneric_supertype(ESub_supertype_constraint type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testGeneric_supertype(ESub_supertype_constraint type) throws SdaiException {
		return test_instance(a1);
	}
	public EEntity_or_view_definition getGeneric_supertype(ESub_supertype_constraint type) throws SdaiException {
		a1 = get_instance(a1);
		return (EEntity_or_view_definition)a1;
	}
	public void setGeneric_supertype(ESub_supertype_constraint type, EEntity_or_view_definition value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetGeneric_supertype(ESub_supertype_constraint type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeGeneric_supertype(ESub_supertype_constraint type) throws SdaiException {
		return a1$;
	}

	// methods for attribute: total_cover, base type: SET OF ENTITY
	public static int usedinTotal_cover(ESub_supertype_constraint type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testTotal_cover(ESub_supertype_constraint type) throws SdaiException {
		return test_aggregate(a2);
	}
	public AEntity_definition getTotal_cover(ESub_supertype_constraint type) throws SdaiException {
		return (AEntity_definition)get_aggregate(a2);
	}
	public AEntity_definition createTotal_cover(ESub_supertype_constraint type) throws SdaiException {
		a2 = (AEntity_definition)create_aggregate_class(a2, a2$,  AEntity_definition.class, 0);
		return a2;
	}
	public void unsetTotal_cover(ESub_supertype_constraint type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeTotal_cover(ESub_supertype_constraint type) throws SdaiException {
		return a2$;
	}

	// attribute: constraint, base type: entity subtype_expression
	public static int usedinConstraint(ESub_supertype_constraint type, ESubtype_expression instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testConstraint(ESub_supertype_constraint type) throws SdaiException {
		return test_instance(a3);
	}
	public ESubtype_expression getConstraint(ESub_supertype_constraint type) throws SdaiException {
		a3 = get_instance(a3);
		return (ESubtype_expression)a3;
	}
	public void setConstraint(ESub_supertype_constraint type, ESubtype_expression value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetConstraint(ESub_supertype_constraint type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeConstraint(ESub_supertype_constraint type) throws SdaiException {
		return a3$;
	}

	/// methods for attribute: abstract_supertype, base type: BOOLEAN
	public boolean testAbstract_supertype(ESub_supertype_constraint type) throws SdaiException {
		return test_boolean(a4);
	}
	public boolean getAbstract_supertype(ESub_supertype_constraint type) throws SdaiException {
		return get_boolean(a4);
	}
	public void setAbstract_supertype(ESub_supertype_constraint type, boolean value) throws SdaiException {
		a4 = set_boolean(value);
	}
	public void unsetAbstract_supertype(ESub_supertype_constraint type) throws SdaiException {
		a4 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeAbstract_supertype(ESub_supertype_constraint type) throws SdaiException {
		return a4$;
	}

	// derived attribute: super_type, base type: entity entity_definition
	public boolean testSuper_type(ESub_supertype_constraint type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getSuper_type(ESub_supertype_constraint type, SdaiContext _context) throws SdaiException {



				return (jsdai.SExtended_dictionary_schema.FGet_entity_definition.run(_context, Value.alloc(jsdai.SExtended_dictionary_schema.CEntity_or_view_definition.definition).set(_context, get(a1$))));
	}
	public EEntity_definition getSuper_type(ESub_supertype_constraint type) throws SdaiException {
		SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
			return (EEntity_definition) getSuper_type(null, _context).getInstance();
	}
	public static jsdai.dictionary.EAttribute attributeSuper_type(ESub_supertype_constraint type) throws SdaiException {
		return d0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a3 = unset_instance(a3);
			a4 = 0;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = (AEntity_definition)av.entityValues[0].getInstanceAggregate(2, a2$, this);
		a3 = av.entityValues[0].getInstance(3, this, a3$);
		a4 = av.entityValues[0].getBoolean(4);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: sub_supertype_constraint
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstanceAggregate(2, a2);
		av.entityValues[0].setInstance(3, a3);
		av.entityValues[0].setBoolean(4, a4);
	}
}
