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

// Java class implementing entity view_partition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CView_partition extends CMap_or_view_partition implements EView_partition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CView_partition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// parent: protected Object a0;   parent - java inheritance - SELECT map_or_view_definition_select
	// name: protected String a1;   name - java inheritance - STRING
	// source_parameters: protected Object  - inverse - java inheritance -  ENTITY source_parameter
	// attribute: protected Object  - inverse - current -  ENTITY view_partition_attribute
	protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for SELECT attribute: parent
	public static int usedinParent(EMap_or_view_partition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	// Inverse attribute - attribute : SET[0:-2147483648] OF view_partition_attribute FOR related_partition
	public AView_partition_attribute getAttribute(EView_partition type, ASdaiModel domain) throws SdaiException {
		AView_partition_attribute result = (AView_partition_attribute)get_inverse_aggregate(i1$);
		CView_partition_attribute.usedinRelated_partition(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeAttribute(EView_partition type) throws SdaiException {
		return i1$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getString(1);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: map_or_view_partition
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setString(1, a1);
		// partial entity: view_partition
	}
}
