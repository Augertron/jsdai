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

// Java class implementing entity attribute_mapping

package jsdai.mapping;
import jsdai.lang.*;

public class CAttribute_mapping extends CEntity implements EAttribute_mapping {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAttribute_mapping.class, SMapping.ss);

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
	protected AAttribute_mapping_path_select a5; // path - current entity - LIST OF SELECT
	protected static final jsdai.dictionary.CExplicit_attribute a5$ = CEntity.initExplicitAttribute(definition, 5);
	protected Object a6; // domain - current entity - SELECT attribute_mapping_domain_select
	protected static final jsdai.dictionary.CExplicit_attribute a6$ = CEntity.initExplicitAttribute(definition, 6);
	// target: protected Object  - derived - current -  SELECT base_type
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

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
		changeReferencesAggregate(a5, old, newer);
		if (a6 == old) {
			a6 = newer;
		}
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

	// methods for attribute: path, base type: LIST OF SELECT
	public static int usedinPath(EAttribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a5$, domain, result);
	}
	public boolean testPath(EAttribute_mapping type) throws SdaiException {
		return test_aggregate(a5);
	}
	public AAttribute_mapping_path_select getPath(EAttribute_mapping type) throws SdaiException {
		if (a5 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a5;
	}
	public AAttribute_mapping_path_select createPath(EAttribute_mapping type) throws SdaiException {
		a5 = (AAttribute_mapping_path_select)create_aggregate_class(a5, a5$, AAttribute_mapping_path_select.class, 0);
		return a5;
	}
	public void unsetPath(EAttribute_mapping type) throws SdaiException {
		unset_aggregate(a5);
		a5 = null;
	}
	public static jsdai.dictionary.EAttribute attributePath(EAttribute_mapping type) throws SdaiException {
		return a5$;
	}

	// methods for SELECT attribute: domain
	public static int usedinDomain(EAttribute_mapping type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a6$, domain, result);
	}
	public boolean testDomain(EAttribute_mapping type) throws SdaiException {
		return test_instance(a6);
	}

	public jsdai.lang.EEntity getDomain(EAttribute_mapping type) throws jsdai.lang.SdaiException { // case 1
		a6 = get_instance_select(a6);
		return (jsdai.lang.EEntity)a6;
	}

	public void setDomain(EAttribute_mapping type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a6 = set_instance(a6, value);
	}

	public void unsetDomain(EAttribute_mapping type) throws SdaiException {
		a6 = unset_instance(a6);
	}

	public static jsdai.dictionary.EAttribute attributeDomain(EAttribute_mapping type) throws SdaiException {
		return a6$;
	}

	// methods for derived SELECT attribute: target
	public boolean testTarget(EAttribute_mapping type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}

	public jsdai.lang.EEntity getTarget(EAttribute_mapping type) throws jsdai.lang.SdaiException { // case 1
			throw new jsdai.lang.SdaiException(jsdai.lang.SdaiException.FN_NAVL);
	}

	public static jsdai.dictionary.EAttribute attributeTarget(EAttribute_mapping type) throws SdaiException {
		return d0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			if (a5 instanceof CAggregate)
				a5.unsetAll();
			a5 = null;
			a6 = unset_instance(a6);
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			if (a3 instanceof CAggregate)
				a3.unsetAll();
			a3 = null;
			a4 = 0;
			return;
		}
		a5 = (AAttribute_mapping_path_select)av.entityValues[0].getInstanceAggregate(0, a5$, this);
		a6 = av.entityValues[0].getInstance(1, this);
		a0 = av.entityValues[1].getInstance(0, this);
		a1 = av.entityValues[1].getInstance(1, this);
		a2 = av.entityValues[1].getInstance(2, this);
		a3 = (jsdai.dictionary.ANamed_type)av.entityValues[1].getInstanceAggregate(3, a3$, this);
		a4 = av.entityValues[1].getBoolean(4);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute_mapping
		av.entityValues[0].setInstanceAggregate(0, a5);
		av.entityValues[0].setInstance(1, a6);
		// partial entity: generic_attribute_mapping
		av.entityValues[1].setInstance(0, a0);
		av.entityValues[1].setInstance(1, a1);
		av.entityValues[1].setInstance(2, a2);
		av.entityValues[1].setInstanceAggregate(3, a3);
		av.entityValues[1].setBoolean(4, a4);
	}
}
