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

// Java class implementing entity reference_from_specification

package jsdai.dictionary;
import jsdai.lang.*;

public class CReference_from_specification extends CEntity implements EReference_from_specification {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected Object a0; // foreign_schema - non-java inheritance - ENTITY generic_schema_definition
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected Object a1; // current_schema - non-java inheritance - ENTITY generic_schema_definition
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected AInterfaced_declaration a2; // items - non-java inheritance - SET OF ENTITY
	protected static jsdai.dictionary.CExplicit_attribute a2$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
		if (a1 == old) {
			a1 = newer;
		}
		changeReferencesAggregate(a2, old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: foreign_schema, base type: entity generic_schema_definition
	public static int usedinForeign_schema(EInterface_specification type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testForeign_schema(EInterface_specification type) throws SdaiException {
		return test_instance(a0);
	}
	public EGeneric_schema_definition getForeign_schema(EInterface_specification type) throws SdaiException {
		a0 = get_instance(a0);
		return (EGeneric_schema_definition)a0;
	}
	public void setForeign_schema(EInterface_specification type, EGeneric_schema_definition value) throws SdaiException {
		a0 = set_instance(a0, value);
	}
	public void unsetForeign_schema(EInterface_specification type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeForeign_schema(EInterface_specification type) throws SdaiException {
		return a0$;
	}

	// attribute: current_schema, base type: entity generic_schema_definition
	public static int usedinCurrent_schema(EInterface_specification type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testCurrent_schema(EInterface_specification type) throws SdaiException {
		return test_instance(a1);
	}
	public EGeneric_schema_definition getCurrent_schema(EInterface_specification type) throws SdaiException {
		a1 = get_instance(a1);
		return (EGeneric_schema_definition)a1;
	}
	public void setCurrent_schema(EInterface_specification type, EGeneric_schema_definition value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetCurrent_schema(EInterface_specification type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeCurrent_schema(EInterface_specification type) throws SdaiException {
		return a1$;
	}

	// methods for attribute: items, base type: SET OF ENTITY
	public static int usedinItems(EInterface_specification type, EInterfaced_declaration instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testItems(EInterface_specification type) throws SdaiException {
		return test_aggregate(a2);
	}
	public AInterfaced_declaration getItems(EInterface_specification type) throws SdaiException {
		return (AInterfaced_declaration)get_aggregate(a2);
	}
	public AInterfaced_declaration createItems(EInterface_specification type) throws SdaiException {
		a2 = (AInterfaced_declaration)create_aggregate_class(a2, a2$,  AInterfaced_declaration.class, 0);
		return a2;
	}
	public void unsetItems(EInterface_specification type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EInterface_specification type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this);
		a1 = av.entityValues[0].getInstance(1, this);
		a2 = (AInterfaced_declaration)av.entityValues[0].getInstanceAggregate(2, a2$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: interface_specification
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstanceAggregate(2, a2);
		// partial entity: reference_from_specification
	}
}
