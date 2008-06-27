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

// Java class implementing entity attribute_mapping_logical_value

package jsdai.mapping;
import jsdai.lang.*;

public class CAttribute_mapping_logical_value extends CEntity implements EAttribute_mapping_logical_value {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAttribute_mapping_logical_value.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected Object a0; // parent_entity - non-java inheritance - ENTITY entity_mapping
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // source - non-java inheritance - ENTITY attribute
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // constraints - non-java inheritance - SELECT constraint_select
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected jsdai.dictionary.ANamed_type a3; // data_type - non-java inheritance - LIST OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	protected int a4; // strong - non-java inheritance - BOOLEAN
	protected static final jsdai.dictionary.CExplicit_attribute a4$ = CEntity.initExplicitAttribute(definition, 4);
	protected int a5; // mapped_value - current entity - LOGICAL
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);

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

	// attribute: parent_entity, base type: entity entity_mapping
	public static int usedinParent_entity(EGeneric_attribute_mapping type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
		return test_instance(a0);
	}
	public EEntity_mapping getParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
		a0 = get_instance(a0);
		return (EEntity_mapping)a0;
	}
	public void setParent_entity(EGeneric_attribute_mapping type, EEntity_mapping value) throws SdaiException {
		a0 = set_instance(a0, value);
	}
	public void unsetParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeParent_entity(EGeneric_attribute_mapping type) throws SdaiException {
		return a0$;
	}

	// attribute: source, base type: entity attribute
	public static int usedinSource(EGeneric_attribute_mapping type, jsdai.dictionary.EAttribute instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testSource(EGeneric_attribute_mapping type) throws SdaiException {
		return test_instance(a1);
	}
	public jsdai.dictionary.EAttribute getSource(EGeneric_attribute_mapping type) throws SdaiException {
		a1 = get_instance(a1);
		return (jsdai.dictionary.EAttribute)a1;
	}
	public void setSource(EGeneric_attribute_mapping type, jsdai.dictionary.EAttribute value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetSource(EGeneric_attribute_mapping type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeSource(EGeneric_attribute_mapping type) throws SdaiException {
		return a1$;
	}

	// methods for SELECT attribute: constraints
	public static int usedinConstraints(EGeneric_attribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testConstraints(EGeneric_attribute_mapping type) throws SdaiException {
		return test_instance(a2);
	}

	public jsdai.lang.EEntity getConstraints(EGeneric_attribute_mapping type) throws jsdai.lang.SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (jsdai.lang.EEntity)a2;
	}

	public void setConstraints(EGeneric_attribute_mapping type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetConstraints(EGeneric_attribute_mapping type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeConstraints(EGeneric_attribute_mapping type) throws SdaiException {
		return a2$;
	}

	// methods for attribute: data_type, base type: LIST OF ENTITY
	public static int usedinData_type(EGeneric_attribute_mapping type, jsdai.dictionary.ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testData_type(EGeneric_attribute_mapping type) throws SdaiException {
		return test_aggregate(a3);
	}
	public jsdai.dictionary.ANamed_type getData_type(EGeneric_attribute_mapping type) throws SdaiException {
		return (jsdai.dictionary.ANamed_type)get_aggregate(a3);
	}
	public jsdai.dictionary.ANamed_type createData_type(EGeneric_attribute_mapping type) throws SdaiException {
		a3 = (jsdai.dictionary.ANamed_type)create_aggregate_class(a3, a3$,  jsdai.dictionary.ANamed_type.class, 0);
		return a3;
	}
	public void unsetData_type(EGeneric_attribute_mapping type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeData_type(EGeneric_attribute_mapping type) throws SdaiException {
		return a3$;
	}

	/// methods for attribute: strong, base type: BOOLEAN
	public boolean testStrong(EGeneric_attribute_mapping type) throws SdaiException {
		return test_boolean(a4);
	}
	public boolean getStrong(EGeneric_attribute_mapping type) throws SdaiException {
		return get_boolean(a4);
	}
	public void setStrong(EGeneric_attribute_mapping type, boolean value) throws SdaiException {
		a4 = set_boolean(value);
	}
	public void unsetStrong(EGeneric_attribute_mapping type) throws SdaiException {
		a4 = unset_boolean();
	}
	public static jsdai.dictionary.EAttribute attributeStrong(EGeneric_attribute_mapping type) throws SdaiException {
		return a4$;
	}

	/// methods for attribute: mapped_value, base type: LOGICAL
	public boolean testMapped_value(EAttribute_mapping_logical_value type) throws SdaiException {
		return test_logical(a5);
	}
	public int getMapped_value(EAttribute_mapping_logical_value type) throws SdaiException {
		return get_logical(a5);
	}
	public void setMapped_value(EAttribute_mapping_logical_value type, int value) throws SdaiException {
		a5 = set_logical(value);
	}
	public void unsetMapped_value(EAttribute_mapping_logical_value type) throws SdaiException {
		a5 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeMapped_value(EAttribute_mapping_logical_value type) throws SdaiException {
		return a5$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a5 = 0;
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			a4 = 0;
			return;
		}
		a5 = av.entityValues[0].getLogical(0);
		a0 = av.entityValues[2].getInstance(0, this);
		a1 = av.entityValues[2].getInstance(1, this);
		a2 = av.entityValues[2].getInstance(2, this);
		a3 = (jsdai.dictionary.ANamed_type)av.entityValues[2].getInstanceAggregate(3, a3$, this);
		a4 = av.entityValues[2].getBoolean(4);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_mapping_logical_value
		av.entityValues[0].setLogical(0, a5);
		// partial entity: attribute_mapping_value
		// partial entity: generic_attribute_mapping
		av.entityValues[2].setInstance(0, a0);
		av.entityValues[2].setInstance(1, a1);
		av.entityValues[2].setInstance(2, a2);
		av.entityValues[2].setInstanceAggregate(3, a3);
		av.entityValues[2].setBoolean(4, a4);
	}
}
