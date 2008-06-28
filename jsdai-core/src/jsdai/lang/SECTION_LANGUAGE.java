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

class SECTION_LANGUAGE
{
	String section;
	String default_language;

	SECTION_LANGUAGE() {
	}

	SECTION_LANGUAGE(String provided_section, String provided_default_language) {
		section = provided_section;
		default_language = provided_default_language;
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
		if (inst.values[1].tag != PhFileReader.STRING) {
			return PhFileReader.INCORRECT_VALUE;
		}
		default_language = inst.values[1].string;
		return 0;
	}


	void print() throws SdaiException {
		SdaiSession.println("*****Attributes of an instance of the entity " +
				"SECTION_LANGUAGE*****");
		SdaiSession.println("");
		SdaiSession.println("   section");
		SdaiSession.println(section);
		SdaiSession.println("   default_language");
		SdaiSession.println(default_language);
		SdaiSession.println("");
	}



}
