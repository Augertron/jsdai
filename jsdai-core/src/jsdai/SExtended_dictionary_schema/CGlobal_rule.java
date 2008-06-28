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

// Java class implementing entity global_rule

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CGlobal_rule extends CEntity implements EGlobal_rule {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CGlobal_rule.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected AEntity_definition a1; // entities - current entity - LIST OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// where_rules: protected Object  - inverse - current -  ENTITY where_rule
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a1, old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EGlobal_rule type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EGlobal_rule type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EGlobal_rule type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EGlobal_rule type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EGlobal_rule type) throws SdaiException {
		return a0$;
	}

	// methods for attribute: entities, base type: LIST OF ENTITY
	public static int usedinEntities(EGlobal_rule type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testEntities(EGlobal_rule type) throws SdaiException {
		return test_aggregate(a1);
	}
	public AEntity_definition getEntities(EGlobal_rule type) throws SdaiException {
		return (AEntity_definition)get_aggregate(a1);
	}
	public AEntity_definition createEntities(EGlobal_rule type) throws SdaiException {
		a1 = (AEntity_definition)create_aggregate_class(a1, a1$,  AEntity_definition.class, 0);
		return a1;
	}
	public void unsetEntities(EGlobal_rule type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeEntities(EGlobal_rule type) throws SdaiException {
		return a1$;
	}

	// Inverse attribute - where_rules : SET[1:-2147483648] OF where_rule FOR parent_item
	public AWhere_rule getWhere_rules(EGlobal_rule type, ASdaiModel domain) throws SdaiException {
		AWhere_rule result = (AWhere_rule)get_inverse_aggregate(i0$);
		CWhere_rule.usedinParent_item(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeWhere_rules(EGlobal_rule type) throws SdaiException {
		return i0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = (AEntity_definition)av.entityValues[0].getInstanceAggregate(1, a1$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: global_rule
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstanceAggregate(1, a1);
	}
}
