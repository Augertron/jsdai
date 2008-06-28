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

class SECTION_CONTEXT
{
	String section;
	A_string context_identifiers;

	SECTION_CONTEXT(SdaiRepository repo) throws SdaiException {
		context_identifiers = new A_string(SdaiSession.listTypeSpecial, repo);
	}

	SECTION_CONTEXT(String provided_section, A_string provided_context_identifiers) throws SdaiException {
		section = provided_section;
		context_identifiers = provided_context_identifiers;
		context_identifiers.myType.shift = SdaiSession.PRIVATE_AGGR;
	}



	private int set_context_identifiers(Value value) throws SdaiException { 
		if (value.tag != PhFileReader.EMBEDDED_LIST) {
			return -1;
		}
		int found = 0;
		for (int i = 0; i < value.integer; i++) {
			Value value_in_list = value.nested_values[i];
			if (value_in_list.tag == PhFileReader.STRING) {
				context_identifiers.addByIndexPrivate(found + 1, value_in_list.string);
				found++;
			} else {
				return -3;
			}
		}
		return found;
	}


	int write(EntityValue inst) throws SdaiException {
		if (inst.count < 2) {
			return PhFileReader.TOO_LESS_VALUES;
		}
		if (inst.values[0].tag == PhFileReader.STRING) {
			section = inst.values[0].string;
		} else if (inst.values[0].tag != PhFileReader.MISSING) {
			return PhFileReader.INCORRECT_VALUE;
		}
		int res = set_context_identifiers(inst.values[1]);
		if (res == -1) {
			return PhFileReader.INCORRECT_VALUE;
		} else if (res==-3) {
			return PhFileReader.IMPROPER_LIST_ITEM;
		} else if (res==0) {
			return PhFileReader.LIST_CANNOT_BE_EMPTY;
		}
		return 0;
	}


	void print() throws SdaiException {
		SdaiSession.println("*****Attributes of an instance of the entity " +
				"SECTION_CONTEXT*****");
		SdaiSession.println("");
		SdaiSession.println("   section");
		SdaiSession.println(section);
		SdaiSession.println("   context_identifiers");
		SdaiIterator it = context_identifiers.createIterator();
		while (it.next()) {
			SdaiSession.println(context_identifiers.getCurrentMember(it));
		}
		SdaiSession.println("");
	}



}
