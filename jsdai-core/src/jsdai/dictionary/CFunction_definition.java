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

// Java class implementing entity function_definition

package jsdai.dictionary;
import jsdai.lang.*;

public class CFunction_definition extends CEntity implements EFunction_definition {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected AParameter a1; // parameters - non-java inheritance - LIST OF ENTITY
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected Object a2; // return_type - current entity - ENTITY data_type
	protected static jsdai.dictionary.CExplicit_attribute a2$;
	protected String a3; // return_type_label - current entity - STRING
	protected static jsdai.dictionary.CExplicit_attribute a3$;
	protected A_string a4; // return_type_labels - current entity - LIST OF STRING
	protected static jsdai.dictionary.CExplicit_attribute a4$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a1, old, newer);
		if (a2 == old) {
			a2 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EAlgorithm_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EAlgorithm_definition type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EAlgorithm_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EAlgorithm_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EAlgorithm_definition type) throws SdaiException {
		return a0$;
	}

	// methods for attribute: parameters, base type: LIST OF ENTITY
	public static int usedinParameters(EAlgorithm_definition type, EParameter instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testParameters(EAlgorithm_definition type) throws SdaiException {
		return test_aggregate(a1);
	}
	public AParameter getParameters(EAlgorithm_definition type) throws SdaiException {
		return (AParameter)get_aggregate(a1);
	}
	public AParameter createParameters(EAlgorithm_definition type) throws SdaiException {
		a1 = (AParameter)create_aggregate_class(a1, a1$,  AParameter.class, 0);
		return a1;
	}
	public void unsetParameters(EAlgorithm_definition type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeParameters(EAlgorithm_definition type) throws SdaiException {
		return a1$;
	}

	// attribute: return_type, base type: entity data_type
	public static int usedinReturn_type(EFunction_definition type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testReturn_type(EFunction_definition type) throws SdaiException {
		return test_instance(a2);
	}
	public EData_type getReturn_type(EFunction_definition type) throws SdaiException {
		a2 = get_instance(a2);
		return (EData_type)a2;
	}
	public void setReturn_type(EFunction_definition type, EData_type value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetReturn_type(EFunction_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeReturn_type(EFunction_definition type) throws SdaiException {
		return a2$;
	}

	/// methods for attribute: return_type_label, base type: STRING
	public boolean testReturn_type_label(EFunction_definition type) throws SdaiException {
		return test_string(a3);
	}
	public String getReturn_type_label(EFunction_definition type) throws SdaiException {
		return get_string(a3);
	}
	public void setReturn_type_label(EFunction_definition type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetReturn_type_label(EFunction_definition type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeReturn_type_label(EFunction_definition type) throws SdaiException {
		return a3$;
	}

	// methods for attribute: return_type_labels, base type: LIST OF STRING
	public boolean testReturn_type_labels(EFunction_definition type) throws SdaiException {
		return test_aggregate(a4);
	}
	public A_string getReturn_type_labels(EFunction_definition type) throws SdaiException {
		return (A_string)get_aggregate(a4);
	}
	public A_string createReturn_type_labels(EFunction_definition type) throws SdaiException {
		a4 = create_aggregate_string(a4, a4$, 0);
		return a4;
	}
	public void unsetReturn_type_labels(EFunction_definition type) throws SdaiException {
		unset_aggregate(a4);
		a4 = null;
	}
	public static jsdai.dictionary.EAttribute attributeReturn_type_labels(EFunction_definition type) throws SdaiException {
		return a4$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			a2 = unset_instance(a2);
			a3 = null;
			if (a4 instanceof CAggregate)
				a4.unsetAll();
			a4 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = (AParameter)av.entityValues[0].getInstanceAggregate(1, a1$, this);
		a2 = av.entityValues[1].getInstance(0, this);
		a3 = av.entityValues[1].getString(1);
		a4 = av.entityValues[1].getStringAggregate(2, a4$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: algorithm_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstanceAggregate(1, a1);
		// partial entity: function_definition
		av.entityValues[1].setInstance(0, a2);
		av.entityValues[1].setString(1, a3);
		av.entityValues[1].setStringAggregate(2, a4);
	}
}
