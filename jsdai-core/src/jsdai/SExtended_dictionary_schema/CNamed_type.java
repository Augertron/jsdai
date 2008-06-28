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

// Java class implementing entity named_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CNamed_type extends CData_type implements ENamed_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CNamed_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	protected String a1; // short_name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// where_rules: protected Object  - inverse - current -  ENTITY where_rule
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

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
		AWhere_rule result = (AWhere_rule)get_inverse_aggregate(i0$);
		CWhere_rule.usedinParent_item(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeWhere_rules(ENamed_type type) throws SdaiException {
		return i0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: named_type
		av.entityValues[1].setString(0, a1);
	}
}
