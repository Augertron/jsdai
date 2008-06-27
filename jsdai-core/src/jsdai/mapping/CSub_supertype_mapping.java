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

// Java class implementing entity sub_supertype_mapping

package jsdai.mapping;
import jsdai.lang.*;

public class CSub_supertype_mapping extends CEntity implements ESub_supertype_mapping {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSub_supertype_mapping.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected Object a0; // relating - non-java inheritance - ENTITY entity_mapping
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // related - non-java inheritance - ENTITY entity_mapping
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // constraints - non-java inheritance - SELECT constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected AAttribute_mapping_path_select a3; // path - non-java inheritance - LIST OF SELECT
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);

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
		if (a2 == old) {
			a2 = newer;
		}
		changeReferencesAggregate(a3, old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: relating, base type: entity entity_mapping
	public static int usedinRelating(EEntity_mapping_relationship type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testRelating(EEntity_mapping_relationship type) throws SdaiException {
		return test_instance(a0);
	}
	public EEntity_mapping getRelating(EEntity_mapping_relationship type) throws SdaiException {
		a0 = get_instance(a0);
		return (EEntity_mapping)a0;
	}
	public void setRelating(EEntity_mapping_relationship type, EEntity_mapping value) throws SdaiException {
		a0 = set_instance(a0, value);
	}
	public void unsetRelating(EEntity_mapping_relationship type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeRelating(EEntity_mapping_relationship type) throws SdaiException {
		return a0$;
	}

	// attribute: related, base type: entity entity_mapping
	public static int usedinRelated(EEntity_mapping_relationship type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRelated(EEntity_mapping_relationship type) throws SdaiException {
		return test_instance(a1);
	}
	public EEntity_mapping getRelated(EEntity_mapping_relationship type) throws SdaiException {
		a1 = get_instance(a1);
		return (EEntity_mapping)a1;
	}
	public void setRelated(EEntity_mapping_relationship type, EEntity_mapping value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetRelated(EEntity_mapping_relationship type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRelated(EEntity_mapping_relationship type) throws SdaiException {
		return a1$;
	}

	// methods for SELECT attribute: constraints
	public static int usedinConstraints(EEntity_mapping_relationship type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testConstraints(EEntity_mapping_relationship type) throws SdaiException {
		return test_instance(a2);
	}

	public jsdai.lang.EEntity getConstraints(EEntity_mapping_relationship type) throws jsdai.lang.SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (jsdai.lang.EEntity)a2;
	}

	public void setConstraints(EEntity_mapping_relationship type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetConstraints(EEntity_mapping_relationship type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeConstraints(EEntity_mapping_relationship type) throws SdaiException {
		return a2$;
	}

	// methods for attribute: path, base type: LIST OF SELECT
	public static int usedinPath(EEntity_mapping_relationship type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testPath(EEntity_mapping_relationship type) throws SdaiException {
		return test_aggregate(a3);
	}
	public AAttribute_mapping_path_select getPath(EEntity_mapping_relationship type) throws SdaiException {
		if (a3 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a3;
	}
	public AAttribute_mapping_path_select createPath(EEntity_mapping_relationship type) throws SdaiException {
		a3 = (AAttribute_mapping_path_select)create_aggregate_class(a3, a3$, AAttribute_mapping_path_select.class, 0);
		return a3;
	}
	public void unsetPath(EEntity_mapping_relationship type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributePath(EEntity_mapping_relationship type) throws SdaiException {
		return a3$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this);
		a1 = av.entityValues[0].getInstance(1, this);
		a2 = av.entityValues[0].getInstance(2, this);
		a3 = (AAttribute_mapping_path_select)av.entityValues[0].getInstanceAggregate(3, a3$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: entity_mapping_relationship
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
		av.entityValues[0].setInstanceAggregate(3, a3);
		// partial entity: sub_supertype_mapping
	}
}
