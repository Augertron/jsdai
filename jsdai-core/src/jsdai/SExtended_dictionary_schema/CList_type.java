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

// Java class implementing entity list_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CList_type extends CVariable_size_aggregation_type implements EList_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CList_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// element_type: protected Object a1;   element_type - java inheritance - ENTITY data_type
	// lower_bound: protected Object a2;   lower_bound - java inheritance - ENTITY bound
	// upper_bound: protected Object a3;   upper_bound - java inheritance - ENTITY bound
	protected int a4; // unique_flag - current entity - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: element_type, base type: entity data_type
	public static int usedinElement_type(EAggregation_type type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	// attribute: lower_bound, base type: entity bound
	public static int usedinLower_bound(EVariable_size_aggregation_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	// attribute: upper_bound, base type: entity bound
	public static int usedinUpper_bound(EVariable_size_aggregation_type type, EBound instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	/// methods for attribute: unique_flag, base type: BOOLEAN
	public boolean testUnique_flag(EList_type type) throws SdaiException {
		return test_boolean(a4);
	}
	public boolean getUnique_flag(EList_type type) throws SdaiException {
		return get_boolean(a4);
	}
	public void setUnique_flag(EList_type type, boolean value) throws SdaiException {
		a4 = set_boolean(value);
	}
	public void unsetUnique_flag(EList_type type) throws SdaiException {
		a4 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeUnique_flag(EList_type type) throws SdaiException {
		return a4$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a1 = unset_instance(a1);
			a0 = null;
			a4 = 0;
			a2 = unset_instance(a2);
			a3 = unset_instance(a3);
			return;
		}
		a1 = av.entityValues[0].getInstance(0, this, a1$);
		a0 = av.entityValues[1].getString(0);
		a4 = av.entityValues[2].getBoolean(0);
		a2 = av.entityValues[3].getInstance(0, this, a2$);
		a3 = av.entityValues[3].getInstance(1, this, a3$);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: aggregation_type
		av.entityValues[0].setInstance(0, a1);
		// partial entity: data_type
		av.entityValues[1].setString(0, a0);
		// partial entity: list_type
		av.entityValues[2].setBoolean(0, a4);
		// partial entity: variable_size_aggregation_type
		av.entityValues[3].setInstance(0, a2);
		av.entityValues[3].setInstance(1, a3);
	}
}
