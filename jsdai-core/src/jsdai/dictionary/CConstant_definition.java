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

// Java class implementing entity constant_definition

package jsdai.dictionary;
import jsdai.lang.*;

public class CConstant_definition extends CEntity implements EConstant_definition {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - current entity - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected Object a1; // domain - current entity - SELECT base_type
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected Object a2; // constant_value - current entity - SELECT base_type
	protected static jsdai.dictionary.CExplicit_attribute a2$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
		if (a2 == old) {
			a2 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EConstant_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EConstant_definition type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EConstant_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EConstant_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EConstant_definition type) throws SdaiException {
		return a0$;
	}

	// methods for SELECT attribute: domain
	public static int usedinDomain(EConstant_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testDomain(EConstant_definition type) throws SdaiException {
		return test_instance(a1);
	}

	public jsdai.lang.EEntity getDomain(EConstant_definition type) throws jsdai.lang.SdaiException { // case 1
		a1 = get_instance_select(a1);
		return (jsdai.lang.EEntity)a1;
	}

	public void setDomain(EConstant_definition type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a1 = set_instance(a1, value);
	}

	public void unsetDomain(EConstant_definition type) throws SdaiException {
		a1 = unset_instance(a1);
	}

	public static jsdai.dictionary.EAttribute attributeDomain(EConstant_definition type) throws SdaiException {
		return a1$;
	}

	// methods for SELECT attribute: constant_value
	public static int usedinConstant_value(EConstant_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testConstant_value(EConstant_definition type) throws SdaiException {
		return test_instance(a2);
	}

	public jsdai.lang.EEntity getConstant_value(EConstant_definition type) throws jsdai.lang.SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (jsdai.lang.EEntity)a2;
	}

	public void setConstant_value(EConstant_definition type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetConstant_value(EConstant_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeConstant_value(EConstant_definition type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this);
		a2 = av.entityValues[0].getInstance(2, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: constant_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
	}
}
