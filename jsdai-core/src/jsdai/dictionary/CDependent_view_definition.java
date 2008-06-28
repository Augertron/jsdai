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

package jsdai.dictionary;
import jsdai.lang.*;

public class CDependent_view_definition extends CEntity implements EDependent_view_definition {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected String a1; // short_name - non-java inheritance - STRING
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	// where_rules: protected Object  - inverse - non-java inheritance -  ENTITY where_rule
	protected static jsdai.dictionary.CInverse_attribute i0$;
	protected AEntity_or_view_definition a2; // generic_supertypes - non-java inheritance - LIST OF ENTITY
	protected static jsdai.dictionary.CExplicit_attribute a2$;
	// partitions: protected Object  - inverse - non-java inheritance -  ENTITY view_partition
	protected static jsdai.dictionary.CInverse_attribute i1$;
	protected Object a3; // domain - current entity - SELECT base_type
	protected static jsdai.dictionary.CExplicit_attribute a3$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a2, old, newer);
		if (a3 == old) {
			a3 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EData_type type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EData_type type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EData_type type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EData_type type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EData_type type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: short_name, base type: STRING
	public boolean testShort_name(ENamed_type type) throws SdaiException {
		return test_string(a1);
	}
	public String getShort_name(ENamed_type type) throws SdaiException {
		return get_string(a1);
	}
	public void setShort_name(ENamed_type type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetShort_name(ENamed_type type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeShort_name(ENamed_type type) throws SdaiException {
		return a1$;
	}

	// Inverse attribute - where_rules : SET[0:-2147483648] OF where_rule FOR parent_item
	public AWhere_rule getWhere_rules(ENamed_type type, ASdaiModel domain) throws SdaiException {
		AWhere_rule result = new AWhere_rule();
		CWhere_rule.usedinParent_item(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeWhere_rules(ENamed_type type) throws SdaiException {
		return i0$;
	}

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

	// Inverse attribute - partitions : SET[1:-2147483648] OF view_partition FOR parent
	public AView_partition getPartitions(EView_definition type, ASdaiModel domain) throws SdaiException {
		AView_partition result = new AView_partition();
		CView_partition.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributePartitions(EView_definition type) throws SdaiException {
		return i1$;
	}

	// methods for SELECT attribute: domain
	public static int usedinDomain(EDependent_view_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testDomain(EDependent_view_definition type) throws SdaiException {
		return test_instance(a3);
	}

	public jsdai.lang.EEntity getDomain(EDependent_view_definition type) throws jsdai.lang.SdaiException { // case 1
		a3 = get_instance_select(a3);
		return (jsdai.lang.EEntity)a3;
	}

	public void setDomain(EDependent_view_definition type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
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
		a3 = av.entityValues[1].getInstance(0, this);
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
