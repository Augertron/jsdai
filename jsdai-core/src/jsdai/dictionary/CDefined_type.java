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

// Java class implementing entity defined_type

package jsdai.dictionary;
import jsdai.lang.*;

public class CDefined_type extends DefinedType implements EDefined_type {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected String a1; // short_name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	// where_rules: protected Object  - inverse - non-java inheritance -  ENTITY where_rule
	protected static jsdai.dictionary.CInverse_attribute i0$;
	protected Object a2; // domain - current entity - SELECT underlying_type
	protected static jsdai.dictionary.CExplicit_attribute a2$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a2 == old) {
			a2 = newer;
		}
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

	// methods for SELECT attribute: domain
	public static int usedinDomain(EDefined_type type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testDomain(EDefined_type type) throws SdaiException {
		return test_instance(a2);
	}

	public jsdai.lang.EEntity getDomain(EDefined_type type) throws jsdai.lang.SdaiException { // case 1
//		a2 = get_instance_select(a2);
//		return (jsdai.lang.EEntity)a2;
		return (EEntity)a2;
	}

	public void setDomain(EDefined_type type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetDomain(EDefined_type type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeDomain(EDefined_type type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a2 = unset_instance(a2);
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a2 = av.entityValues[1].getInstance(0, this);
		a1 = av.entityValues[2].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: defined_type
		av.entityValues[1].setInstance(0, a2);
		// partial entity: named_type
		av.entityValues[2].setString(0, a1);
	}
}
