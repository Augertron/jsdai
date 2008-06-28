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

// Java class implementing entity identified_by_parameter

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CIdentified_by_parameter extends CMap_or_view_input_parameter implements EIdentified_by_parameter {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CIdentified_by_parameter.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// parent: protected Object a1;   parent - java inheritance - ENTITY map_or_view_partition
	// extent: protected Object a2;   extent - java inheritance - ENTITY data_type
	// order: protected int a3;   order - java inheritance - INTEGER

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: parent, base type: entity map_or_view_partition
	public static int usedinParent(EMap_or_view_input_parameter type, EMap_or_view_partition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	// attribute: extent, base type: entity data_type
	public static int usedinExtent(EMap_or_view_input_parameter type, EData_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}

	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			a3 = Integer.MIN_VALUE;
			return;
		}
		a0 = av.entityValues[1].getString(0);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
		a2 = av.entityValues[1].getInstance(2, this, a2$);
		a3 = av.entityValues[1].getInteger(3);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: identified_by_parameter
		// partial entity: map_or_view_input_parameter
		av.entityValues[1].setString(0, a0);
		av.entityValues[1].setInstance(1, a1);
		av.entityValues[1].setInstance(2, a2);
		av.entityValues[1].setInteger(3, a3);
	}
}
