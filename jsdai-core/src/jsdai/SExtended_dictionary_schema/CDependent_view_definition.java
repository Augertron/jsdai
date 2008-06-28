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

// Java class implementing entity dependent_view_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CDependent_view_definition extends CView_definition implements EDependent_view_definition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDependent_view_definition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// short_name: protected String a1;   short_name - java inheritance - STRING
	// where_rules: protected Object  - inverse - java inheritance -  ENTITY where_rule
	// generic_supertypes: protected AEntity_or_view_definition a2;   generic_supertypes - java inheritance - LIST OF ENTITY
	// partitions: protected Object  - inverse - java inheritance -  ENTITY view_partition
	protected Object a3; // domain - current entity - SELECT base_type
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
		if (a3 == old) {
			a3 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: generic_supertypes, base type: LIST OF ENTITY
	public static int usedinGeneric_supertypes(EEntity_or_view_definition type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	// methods for SELECT attribute: domain
	public static int usedinDomain(EDependent_view_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testDomain(EDependent_view_definition type) throws SdaiException {
		return test_instance(a3);
	}

	public EEntity getDomain(EDependent_view_definition type) throws SdaiException { // case 1
		a3 = get_instance_select(a3);
		return (EEntity)a3;
	}

	public void setDomain(EDependent_view_definition type, EEntity value) throws SdaiException { // case 1
		a3 = set_instance(a3, value);
	}

	public void unsetDomain(EDependent_view_definition type) throws SdaiException {
		a3 = unset_instance(a3);
	}

	public static jsdai.dictionary.EAttribute attributeDomain(EDependent_view_definition type) throws SdaiException {
		return a3$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a3 = unset_instance(a3);
			if (a2 instanceof CAggregate)
				a2.unsetAll();
			a2 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a3 = av.entityValues[1].getInstance(0, this, a3$);
		a2 = (AEntity_or_view_definition)av.entityValues[2].getInstanceAggregate(0, a2$, this);
		a1 = av.entityValues[3].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: dependent_view_definition
		av.entityValues[1].setInstance(0, a3);
		// partial entity: entity_or_view_definition
		av.entityValues[2].setInstanceAggregate(0, a2);
		// partial entity: named_type
		av.entityValues[3].setString(0, a1);
		// partial entity: view_definition
	}
}
