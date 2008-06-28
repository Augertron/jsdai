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

// Java class implementing entity aggregation_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CAggregation_type extends CData_type implements EAggregation_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAggregation_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	protected Object a1; // element_type - current entity - ENTITY data_type
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: element_type, base type: entity data_type
	public static int usedinElement_type(EAggregation_type type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testElement_type(EAggregation_type type) throws SdaiException {
		return test_instance(a1);
	}
	public EData_type getElement_type(EAggregation_type type) throws SdaiException {
		a1 = get_instance(a1);
		return (EData_type)a1;
	}
	public void setElement_type(EAggregation_type type, EData_type value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetElement_type(EAggregation_type type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeElement_type(EAggregation_type type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a1 = unset_instance(a1);
			a0 = null;
			return;
		}
		a1 = av.entityValues[0].getInstance(0, this, a1$);
		a0 = av.entityValues[1].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: aggregation_type
		av.entityValues[0].setInstance(0, a1);
		// partial entity: data_type
		av.entityValues[1].setString(0, a0);
	}
}
