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

class FILE_POPULATION {

	String governing_schema;
	String determination_method;
	A_string governed_sections;

	String name;
	String final_name;
	String change_date;
	String validation_date;
	int validation_result;
	int validation_level;
	A_string description;
	A_string author;
	A_string organization;
	String preprocessor_version;
	String originating_system;
	String authorization;
	String language;
	A_string context_identifiers;
	A_string included_schema_instances = null;

	SdaiRepository owning_repo;


	FILE_POPULATION(SdaiRepository repo) throws SdaiException {
		owning_repo = repo;
	}



	private int set_governed_sections(Value value) throws SdaiException { 
		if (value.tag != PhFileReader.EMBEDDED_LIST) {
			return -1;
		}
		int found = 0;
		for (int i = 0; i < value.integer; i++) {
			Value value_in_set = value.nested_values[i];
			if (value_in_set.tag == PhFileReader.STRING) {
				governed_sections.addUnorderedPrivate(value_in_set.string);
				found++;
			} else {
				return -3;
			}
		}
		return found;
	}


	int write(EntityValue inst) throws SdaiException {
		if (inst.count < 3) {
			return PhFileReader.TOO_LESS_VALUES;
		}
		if (inst.values[0].tag == PhFileReader.STRING) {
			governing_schema = inst.values[0].string;
		} else {
			return PhFileReader.INCORRECT_VALUE;
		}
		if (inst.values[1].tag == PhFileReader.STRING) {
			determination_method = inst.values[1].string;
		} else {
			return PhFileReader.INCORRECT_VALUE;
		}
		if (inst.values[2].tag == PhFileReader.MISSING) {
			return 0;
		}
		governed_sections = new A_string(SdaiSession.setTypeSpecialNonEmpty, owning_repo);
		int res = set_governed_sections(inst.values[2]);
		if (res == -1) {
			return PhFileReader.INCORRECT_VALUE;
		} else if (res==-3) {
			return PhFileReader.IMPROPER_LIST_ITEM;
		} else if (res==0) {
//			return PhFileReader.LIST_CANNOT_BE_EMPTY;
		}
		return 0;
	}


	void print() throws SdaiException {
		SdaiSession.println("*****Attributes of an instance of the entity " +
				"FILE_POPULATION*****");
		SdaiSession.println("");
		SdaiSession.println("   governing_schema");
		SdaiSession.println(governing_schema);
		SdaiSession.println("   determination_method");
		SdaiSession.println(determination_method);
		SdaiSession.println("   governed_sections");
		SdaiIterator it = governed_sections.createIterator();
		while (it.next()) {
			SdaiSession.println(governed_sections.getCurrentMember(it));
		}
		SdaiSession.println("");
	}



}
