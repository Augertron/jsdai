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

package jsdai.expressCompiler;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;


public class UnknownObject {
	
	EEntity  scope;
	String name;
	String express_file;
	EGeneric_schema_definition schd;
	public UnknownObject(String name, EEntity scope, EGeneric_schema_definition schd, String express_file) {
		this.name = name;
		this.scope = scope;
		this.express_file = express_file;
		this.schd = schd;
	}
}