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

// Java class implementing entity entity_or_view_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CEntity_or_view_definition extends CNamed_type implements EEntity_or_view_definition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CEntity_or_view_definition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// short_name: protected String a1;   short_name - java inheritance - STRING
	// where_rules: protected Object  - inverse - java inheritance -  ENTITY where_rule
	protected AEntity_or_view_definition a2; // generic_supertypes - current entity - LIST OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		changeReferencesAggregate(a2, old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: generic_supertypes, base type: LIST OF ENTITY
	public static int usedinGeneric_supertypes(EEntity_or_view_definition type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		return test_aggregate(a2);
	}
	public AEntity_or_view_definition getGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		return (AEntity_or_view_definition)get_aggregate(a2);
	}
	public AEntity_or_view_definition createGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		a2 = (AEntity_or_view_definition)create_aggregate_class(a2, a2$,  AEntity_or_view_definition.class, 0);
		return a2;
	}
	public void unsetGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeGeneric_supertypes(EEntity_or_view_definition type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a2 = (AEntity_or_view_definition)av.entityValues[1].getInstanceAggregate(0, a2$, this);
		a1 = av.entityValues[2].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: entity_or_view_definition
		av.entityValues[1].setInstanceAggregate(0, a2);
		// partial entity: named_type
		av.entityValues[2].setString(0, a1);
	}
}
