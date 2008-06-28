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

// Java class implementing entity reference_from_specification_as

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CReference_from_specification_as extends CReference_from_specification implements EReference_from_specification_as {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CReference_from_specification_as.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// foreign_schema: protected Object a0;   foreign_schema - java inheritance - ENTITY generic_schema_definition
	// current_schema: protected Object a1;   current_schema - java inheritance - ENTITY generic_schema_definition
	// items: protected AInterfaced_declaration a2;   items - java inheritance - SET OF ENTITY
	protected String a3; // alias_name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: foreign_schema, base type: entity generic_schema_definition
	public static int usedinForeign_schema(EInterface_specification type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	// attribute: current_schema, base type: entity generic_schema_definition
	public static int usedinCurrent_schema(EInterface_specification type, EGeneric_schema_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	// methods for attribute: items, base type: SET OF ENTITY
	public static int usedinItems(EInterface_specification type, EInterfaced_declaration instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	/// methods for attribute: alias_name, base type: STRING
	public boolean testAlias_name(EReference_from_specification_as type) throws SdaiException {
		return test_string(a3);
	}
	public String getAlias_name(EReference_from_specification_as type) throws SdaiException {
		return get_string(a3);
	}
	public void setAlias_name(EReference_from_specification_as type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetAlias_name(EReference_from_specification_as type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeAlias_name(EReference_from_specification_as type) throws SdaiException {
		return a3$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a3 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = (AInterfaced_declaration)av.entityValues[0].getInstanceAggregate(2, a2$, this);
		a3 = av.entityValues[2].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: interface_specification
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstanceAggregate(2, a2);
		// partial entity: reference_from_specification
		// partial entity: reference_from_specification_as
		av.entityValues[2].setString(0, a3);
	}
}
