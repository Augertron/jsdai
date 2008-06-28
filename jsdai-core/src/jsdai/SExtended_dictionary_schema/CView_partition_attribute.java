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

// Java class implementing entity view_partition_attribute

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CView_partition_attribute extends CEntity implements EView_partition_attribute {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CView_partition_attribute.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	protected Object a0; // parent_view_attribute - current entity - ENTITY view_attribute
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // related_partition - current entity - ENTITY view_partition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

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
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: parent_view_attribute, base type: entity view_attribute
	public static int usedinParent_view_attribute(EView_partition_attribute type, EView_attribute instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testParent_view_attribute(EView_partition_attribute type) throws SdaiException {
		return test_instance(a0);
	}
	public EView_attribute getParent_view_attribute(EView_partition_attribute type) throws SdaiException {
		a0 = get_instance(a0);
		return (EView_attribute)a0;
	}
	public void setParent_view_attribute(EView_partition_attribute type, EView_attribute value) throws SdaiException {
		a0 = set_instance(a0, value);
	}
	public void unsetParent_view_attribute(EView_partition_attribute type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeParent_view_attribute(EView_partition_attribute type) throws SdaiException {
		return a0$;
	}

	// attribute: related_partition, base type: entity view_partition
	public static int usedinRelated_partition(EView_partition_attribute type, EView_partition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testRelated_partition(EView_partition_attribute type) throws SdaiException {
		return test_instance(a1);
	}
	public EView_partition getRelated_partition(EView_partition_attribute type) throws SdaiException {
		a1 = get_instance(a1);
		return (EView_partition)a1;
	}
	public void setRelated_partition(EView_partition_attribute type, EView_partition value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetRelated_partition(EView_partition_attribute type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeRelated_partition(EView_partition_attribute type) throws SdaiException {
		return a1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: view_partition_attribute
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
	}
}
