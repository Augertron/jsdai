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

// Java class implementing entity view_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CView_definition extends CEntity_or_view_definition implements EView_definition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CView_definition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// short_name: protected String a1;   short_name - java inheritance - STRING
	// where_rules: protected Object  - inverse - java inheritance -  ENTITY where_rule
	// generic_supertypes: protected AEntity_or_view_definition a2;   generic_supertypes - java inheritance - LIST OF ENTITY
	// partitions: protected Object  - inverse - current -  ENTITY view_partition
	protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: generic_supertypes, base type: LIST OF ENTITY
	public static int usedinGeneric_supertypes(EEntity_or_view_definition type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	// Inverse attribute - partitions : SET[1:-2147483648] OF view_partition FOR parent
	public AView_partition getPartitions(EView_definition type, ASdaiModel domain) throws SdaiException {
		AView_partition result = (AView_partition)get_inverse_aggregate(i1$);
		CView_partition.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributePartitions(EView_definition type) throws SdaiException {
		return i1$;
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
		// partial entity: view_definition
	}
}
