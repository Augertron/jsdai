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

package jsdai.lang; import jsdai.dictionary.*;

class PartialEntityName {
	byte[] entity_name;
	int length_of_entity_name;
	CEntityDefinition def;
	int index;
	boolean mark;

	static final int ENTITY_NAME_LENGTH = 128;

	PartialEntityName () {
		entity_name = new byte[ENTITY_NAME_LENGTH];
	}


	void enlarge_entity_name(int demand, byte[] str) {
		int new_length = entity_name.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_entity_name[] = new byte[new_length];
		System.arraycopy(str, 0, new_entity_name, 0, demand);
		entity_name = new_entity_name;
		length_of_entity_name = demand;
	}

}
