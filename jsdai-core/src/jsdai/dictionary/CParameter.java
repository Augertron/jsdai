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

// Java class implementing entity parameter

package jsdai.dictionary;
import jsdai.lang.*;

public class CParameter extends CEntity implements EParameter {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - current entity - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected Object a1; // parameter_type - current entity - ENTITY data_type
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected int a2; // var_type - current entity - BOOLEAN
	protected static jsdai.dictionary.CExplicit_attribute a2$;
	protected A_string a3; // type_labels - current entity - LIST OF STRING
	protected static jsdai.dictionary.CExplicit_attribute a3$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EParameter type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EParameter type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EParameter type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EParameter type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EParameter type) throws SdaiException {
		return a0$;
	}

	// attribute: parameter_type, base type: entity data_type
	public static int usedinParameter_type(EParameter type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testParameter_type(EParameter type) throws SdaiException {
		return test_instance(a1);
	}
	public EData_type getParameter_type(EParameter type) throws SdaiException {
		a1 = get_instance(a1);
		return (EData_type)a1;
	}
	public void setParameter_type(EParameter type, EData_type value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetParameter_type(EParameter type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeParameter_type(EParameter type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: var_type, base type: BOOLEAN
	public boolean testVar_type(EParameter type) throws SdaiException {
		return test_boolean(a2);
	}
	public boolean getVar_type(EParameter type) throws SdaiException {
		return get_boolean(a2);
	}
	public void setVar_type(EParameter type, boolean value) throws SdaiException {
		a2 = set_boolean(value);
	}
	public void unsetVar_type(EParameter type) throws SdaiException {
		a2 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeVar_type(EParameter type) throws SdaiException {
		return a2$;
	}

	// methods for attribute: type_labels, base type: LIST OF STRING
	public boolean testType_labels(EParameter type) throws SdaiException {
		return test_aggregate(a3);
	}
	public A_string getType_labels(EParameter type) throws SdaiException {
		return (A_string)get_aggregate(a3);
	}
	public A_string createType_labels(EParameter type) throws SdaiException {
		a3 = create_aggregate_string(a3, a3$, 0);
		return a3;
	}
	public void unsetType_labels(EParameter type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeType_labels(EParameter type) throws SdaiException {
		return a3$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = 0;
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this);
		a2 = av.entityValues[0].getBoolean(2);
		a3 = av.entityValues[0].getStringAggregate(3, a3$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: parameter
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setBoolean(2, a2);
		av.entityValues[0].setStringAggregate(3, a3);
	}
}
