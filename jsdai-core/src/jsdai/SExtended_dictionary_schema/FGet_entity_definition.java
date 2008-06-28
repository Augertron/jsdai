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

// Java class implementing EXPRESS function get_entity_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class FGet_entity_definition {
	public static Value run(SdaiContext _context, Value _e_parent) throws SdaiException {
		Value _nonvar__e_parent = Value.alloc(jsdai.SExtended_dictionary_schema.CEntity_or_view_definition.definition).set(_e_parent);
		// Declarations of local variables



		Value _e_parent_entity = Value.alloc(jsdai.SExtended_dictionary_schema.CEntity_definition.definition);
		// End of declarations of local variables




					if (Value.alloc(ExpressTypes.LOGICAL_TYPE).IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.ENTITY_DEFINITION", "EXTENDED_DICTIONARY_SCHEMA"), _nonvar__e_parent.typeOfV(_context)).getLogical() == 2) {					
						_e_parent_entity.set(_context, _nonvar__e_parent);
					}



					if (true) return Value.alloc(jsdai.SExtended_dictionary_schema.CEntity_definition.definition).set(_context, _e_parent_entity).check(_context, jsdai.SExtended_dictionary_schema.CEntity_definition.definition);
		return null;
	}

}
