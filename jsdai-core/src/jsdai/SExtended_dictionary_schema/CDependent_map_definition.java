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

// Java class implementing entity dependent_map_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CDependent_map_definition extends CMap_definition implements EDependent_map_definition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CDependent_map_definition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// super_type: protected Object a1;   super_type - java inheritance - ENTITY map_definition
	// partitions: protected Object  - inverse - java inheritance -  ENTITY map_partition
	// target_parameters: protected Object  - inverse - java inheritance -  ENTITY target_parameter

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: super_type, base type: entity map_definition
	public static int usedinSuper_type(EMap_definition type, EMap_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}

	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			return;
		}
		a0 = av.entityValues[1].getString(0);
		a1 = av.entityValues[1].getInstance(1, this, a1$);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: dependent_map_definition
		// partial entity: map_definition
		av.entityValues[1].setString(0, a0);
		av.entityValues[1].setInstance(1, a1);
	}
}
