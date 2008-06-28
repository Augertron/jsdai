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

package jsdai.lang; 

import jsdai.dictionary.*;
import java.io.*;

class $USER_DEFINED_ENTITY extends CEntity
{
	String name;
	EntityValue stored_instance;



	EntityValue getStored_instance(EEntity_definition def) throws SdaiException {
		return stored_instance;
	}

	void setStored_instance(EntityValue inst,
		EEntity_definition def, EDefined_type select[]) throws SdaiException {
		stored_instance = inst;
	}

	int testStored_instance(EEntity_definition ed) throws SdaiException {
		if (stored_instance == null) {
			return 0;
		} else {
			return 1;
		}
	}

	void unsetStored_instance(EEntity_definition ed) throws SdaiException {
		stored_instance = null;
	}

	public EEntity_definition getInstanceType() throws SdaiException {
		return null;
	}

//	protected int getEntityExtentIndex() throws SdaiException {
//		SdaiModel mod = (SdaiModel)findEntityInstanceSdaiModel();
//		if (mod == null) {
//			return -1;
//		} else {
//			return mod.getFolders().getMemberCount() - 1;
//		}
//	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		// not implemented today
	}

	protected EEntity_definition getEntityDefinition() {
		return null;
	}

	protected void setAll(ComplexEntityValue entity_values) throws SdaiException {
		int i;

		EntityValue inst = entity_values.entityValues[0];
		stored_instance = new EntityValue(inst.owning_session);
		if (stored_instance.values == null || stored_instance.values.length < inst.count) {
			stored_instance.values = new Value[inst.count];
		}
		for (i = 0; i <inst.count; i++) {
			if (stored_instance.values[i] == null) {
				stored_instance.values[i] = new Value();
			}
			stored_instance.values[i] = search_for_write(inst.values[i]);
		}
		stored_instance.count = inst.count;
	}

	protected void getAll(ComplexEntityValue inst) throws SdaiException {
		if (inst.entityValues[0] == null) {
			inst.entityValues[0] = new EntityValue(stored_instance.owning_session);
		}
		inst.entityValues[0] = stored_instance;
//		inst.index = 1;
	}

	private Value search_for_write(Value val) throws SdaiException {
		Value cloned = new Value();
		Value val_next;

		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			cloned.tag = val.tag;
			cloned.integer = val.integer;
			cloned.length = val.length;
			val_next = search_for_write(val.nested_values[0]);
			cloned.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
			cloned.nested_values[0] = val_next;
		} else if (val.tag == PhFileReader.EMBEDDED_LIST) {
			cloned.tag = val.tag;
			cloned.integer = val.integer;
			cloned.length = val.length;
			if (val.integer <= SdaiSession.NUMBER_OF_VALUES) {
				cloned.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
			} else {
				cloned.nested_values = new Value[val.integer];
			}
			for (int i = 0; i < val.integer; i++) {
				val_next = search_for_write(val.nested_values[i]);
				cloned.nested_values[i] = val_next;
			}
		} else {
			cloned.tag = val.tag;
			cloned.integer = val.integer;
			cloned.real = val.real;
			cloned.string = val.string;
			cloned.length = val.length;
			cloned.reference = val.reference;
		}
		return cloned;
	}


	private void search_for_unset_references(Value val, CEntity deleted_entity) {
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			search_for_unset_references(val.nested_values[0], deleted_entity);
		} else if (val.tag == PhFileReader.ENTITY_REFERENCE) {
			if (val.reference == deleted_entity) val.reference = null;
		} else if (val.tag == PhFileReader.EMBEDDED_LIST) {
			for (int i = 0; i < val.integer; i++) {
				search_for_unset_references(val.nested_values[i], deleted_entity);
			}
		}
	}

	byte [] get_name() {
		byte [] bytes = new byte[name.length()];

		for (int l = 0; l < name.length(); l++) {
			bytes[l] = (byte)name.charAt(l);
		}
		return bytes;
	}


}
