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

// Java class implementing entity procedure_definition

package jsdai.dictionary;
import jsdai.lang.*;

public class CProcedure_definition extends CEntity implements EProcedure_definition {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected AParameter a1; // parameters - non-java inheritance - LIST OF ENTITY
	protected static jsdai.dictionary.CExplicit_attribute a1$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a1, old, newer);
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
		a1 = (AParameter)av.entityValues[0].getInstanceAggregate(1, a1$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: algorithm_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstanceAggregate(1, a1);
		// partial entity: procedure_definition
	}
}
