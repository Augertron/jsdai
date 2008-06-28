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

class $HEADER_USER_DEFINED_ENTITY {

	String name;
	private Object [] attributes;
	private final int ATTRIBUTES_ARRAY_SIZE = 16;
	EntityValue stored_instance;



	$HEADER_USER_DEFINED_ENTITY() throws SdaiException {
		attributes = new Object[ATTRIBUTES_ARRAY_SIZE];
	}



	int write(EntityValue inst) throws SdaiException {
		int i;

		stored_instance = new EntityValue(inst.owning_session);
		stored_instance.def = inst.def;
		if (stored_instance.values == null || stored_instance.values.length < inst.count) {
			stored_instance.values = new Value[inst.count];
		}
		for (i = 0; i < inst.count; i++) {
			if (stored_instance.values[i] == null) {
				stored_instance.values[i] = new Value();
			}
			stored_instance.values[i] = search_for_write(inst.values[i]);
		}
		stored_instance.count = inst.count;

		int attr_count = 0;
		for (i = 0; i < inst.count; i++) {
			attr_count = search_for_write_in_attr(attributes, attr_count, inst.values[i]);
			if (attr_count < 0) {
				return PhFileReader.TYPED_PARAMETER_IN_HEADER;
			}
		}
		attributes[0] = new Integer(attr_count);
		return 0;
	}


	byte [] get_name() {
		byte [] bytes = new byte[name.length()];
		for (int l = 0; l < name.length(); l++) {
			bytes[l] = (byte)name.charAt(l);
		}
		return bytes;
	}


	String getName() {
		return name;
	}


	Object [] getAttributes() {
		return attributes;
	}


	EntityValue read() {
		return stored_instance;
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


	private int search_for_write_in_attr(Object [] attributes, int attr_count, Value val) throws SdaiException {
		if (attr_count >= attributes.length - 1) {
			int new_length = attributes.length * 2;
			Object [] new_array = new Object[new_length];
			System.arraycopy(attributes, 0, new_array, 0, attributes.length);
			attributes = new_array;
		}
		switch(val.tag) {
			case PhFileReader.MISSING:
				attributes[++attr_count] = "$SOMETHING MISSING";
				break;
			case PhFileReader.REDEFINE:
				attributes[++attr_count] = "$ERROR";
				break;
			case PhFileReader.INTEGER:
				attributes[++attr_count] = new Integer(val.integer);
				break;
			case PhFileReader.REAL:
				attributes[++attr_count] = new Double(val.real);
				break;
			case PhFileReader.LOGICAL:
				attributes[++attr_count] = new Integer(val.integer);
				break;
			case PhFileReader.ENUM:
				attributes[++attr_count] = val.string;
				break;
			case PhFileReader.STRING:
				attributes[++attr_count] = val.string;
				break;
			case PhFileReader.BINARY:
				attributes[++attr_count] = val.reference;
				break;
			case PhFileReader.TYPED_PARAMETER:
				attributes[++attr_count] = "$ERROR";
				return -1;
			case PhFileReader.ENTITY_REFERENCE:
				attributes[++attr_count] = "$ERROR";
				break;
			case PhFileReader.EMBEDDED_LIST:
				Object [] attributes_next = new Object[ATTRIBUTES_ARRAY_SIZE];
				int attr_count_next = 0;
				for (int i = 0; i < val.integer; i++) {
					attr_count_next = search_for_write_in_attr(attributes_next, attr_count_next, val.nested_values[i]);
					if (attr_count_next < 0) {
						return PhFileReader.TYPED_PARAMETER_IN_HEADER;
					}
				}
				attributes_next[0] = new Integer(attr_count_next);
				attributes[++attr_count] = attributes_next;
				break;
		}
		return attr_count;
	}

}

