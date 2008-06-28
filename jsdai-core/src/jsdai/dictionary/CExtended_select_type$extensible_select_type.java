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

// Java class implementing entity extended_select_type$extensible_select_type

package jsdai.dictionary;
import jsdai.lang.*;

public class CExtended_select_type$extensible_select_type extends SelectType implements EExtended_select_type, EExtensible_select_type {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected Object a1; // is_based_on - non-java inheritance - ENTITY defined_type
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected ANamed_type a2; // local_selections - non-java inheritance - SET OF ENTITY
	protected static jsdai.dictionary.CExplicit_attribute a2$;
	// selections: protected ANamed_type  - derived - non-java inheritance -  SET OF ENTITY
	protected static jsdai.dictionary.CDerived_attribute d0$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
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
		a0 = set_string(value);
	}
	public void unsetName(EData_type type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EData_type type) throws SdaiException {
		return a0$;
	}

	// attribute: is_based_on, base type: entity defined_type
	public static int usedinIs_based_on(EExtended_select_type type, EDefined_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testIs_based_on(EExtended_select_type type) throws SdaiException {
		return test_instance(a1);
	}
	public EDefined_type getIs_based_on(EExtended_select_type type) throws SdaiException {
		a1 = get_instance(a1);
		return (EDefined_type)a1;
	}
	public void setIs_based_on(EExtended_select_type type, EDefined_type value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetIs_based_on(EExtended_select_type type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeIs_based_on(EExtended_select_type type) throws SdaiException {
		return a1$;
	}

	// methods for attribute: local_selections, base type: SET OF ENTITY
	public static int usedinLocal_selections(ESelect_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testLocal_selections(ESelect_type type) throws SdaiException {
		return test_aggregate(a2);
	}
	public ANamed_type getLocal_selections(ESelect_type type) throws SdaiException {
		return (ANamed_type)get_aggregate(a2);
	}
	public ANamed_type createLocal_selections(ESelect_type type) throws SdaiException {
		a2 = (ANamed_type)create_aggregate_class(a2, a2$,  ANamed_type.class, 0);
		return a2;
	}
	public void unsetLocal_selections(ESelect_type type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeLocal_selections(ESelect_type type) throws SdaiException {
		return a2$;
	}

	// derived attribute: selections, base type: entity named_type
	public boolean testSelections(ESelect_type type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public ANamed_type getSelections(ESelect_type type, SdaiContext context) throws SdaiException {
		return getSelectionsExtensible(a2$, context);
	}
	public ANamed_type getSelections(ESelect_type type) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, em_wrong_method_used);
	}
	public static jsdai.dictionary.EAttribute attributeSelections(ESelect_type type) throws SdaiException {
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
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getInstance(0, this);
		a2 = (ANamed_type)av.entityValues[3].getInstanceAggregate(0, a2$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: extended_select_type
		av.entityValues[1].setInstance(0, a1);
		// partial entity: extensible_select_type
		// partial entity: select_type
		av.entityValues[3].setInstanceAggregate(0, a2);
	}
}
