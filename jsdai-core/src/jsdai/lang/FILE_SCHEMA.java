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

class FILE_SCHEMA
{
	A_string schema_identifiers;
	A_string schema_identifiers_short;
	A_string schema_identifiers_temp;
	String schema;

	static final byte LBRACE = (byte)'{';


	FILE_SCHEMA(SdaiRepository repo) throws SdaiException {
		schema_identifiers = new A_string(SdaiSession.listTypeSpecialU, repo);
		schema_identifiers_short = new A_string(SdaiSession.listTypeSpecialU, repo);
		schema = null;
	}



	private int set_schema_identifiers(Value value) throws SdaiException { 
		if (value.tag != PhFileReader.EMBEDDED_LIST) {
			return -1;
		}
		if (value.integer < 1) {
			return 0;
		}
		int found = 0;
		for (int i = 0; i < value.integer; i++) {
			Value value_in_list = value.nested_values[i];
			if (value_in_list.tag == PhFileReader.STRING) {
				int count = 0;
				for (int j = 0; j < value_in_list.string.length(); j++) {
					char sym = value_in_list.string.charAt(j);
					if (sym == PhFileWriter.SPACE2 || sym == LBRACE) {
						break;
					}
					count++;
				}
				schema_identifiers.addByIndexPrivate(found + 1, value_in_list.string.toUpperCase());
				schema_identifiers_short.addByIndexPrivate(found + 1, 
					value_in_list.string.substring(0, count).toUpperCase());
				found++;
			} else {
				return -3;
			}
		}
		return found;
	}


	int write(EntityValue inst) throws SdaiException {
		if (inst.count < 1) {
			return PhFileReader.TOO_LESS_VALUES;
		}
		int res = set_schema_identifiers(inst.values[0]);
		if (res == -1) {
			return PhFileReader.INCORRECT_VALUE;
		} else if (res == -3) {
			return PhFileReader.IMPROPER_LIST_ITEM;
		} else if (res == 0) {
			return PhFileReader.SCHEMA_NOT_PROVIDED;
		}
		return 0;
	}


	void print() throws SdaiException {
		SdaiSession.println("*****Attributes of an instance of the entity " +
				"FILE_SCHEMA*****");
		SdaiSession.println("");
		SdaiSession.println("   schema identifiers");
		SdaiIterator it = schema_identifiers.createIterator();
		while (it.next()) {
			SdaiSession.println(schema_identifiers.getCurrentMember(it));
		}
		SdaiSession.println("");
	}

}

