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

// Java class implementing EXPRESS function get_operands

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class FGet_operands {
	public static Value run(SdaiContext _context, Value _e_operandz) throws SdaiException {
		Value _nonvar__e_operandz = Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_view_or_subtype_expression).set(_e_operandz);
		// Declarations of local variables



		Value _e_operands = Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_subtype_expression).create();
		// End of declarations of local variables



					for (Value _implicit_9_i=Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1);Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal(_context, _implicit_9_i, Value.alloc(ExpressTypes.INTEGER_TYPE).sizeOf(_nonvar__e_operandz)).getLogical() == 2;_implicit_9_i.inc()) {					
						if (Value.alloc(ExpressTypes.LOGICAL_TYPE).NOT(Value.alloc(ExpressTypes.LOGICAL_TYPE).IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.VIEW_DEFINITION", "EXTENDED_DICTIONARY_SCHEMA"), _nonvar__e_operandz.indexing(_implicit_9_i, null).typeOfV(_context))).getLogical() == 2) {						
							_e_operands.set(_context, Value.alloc().addOrUnionOrConcatenate(_context, _e_operands, _nonvar__e_operandz.indexing(_implicit_9_i, null)));
						}
					} // for - REPEAT



					if (true) return Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_subtype_expression).set(_context, _e_operands).check(_context, jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_subtype_expression);
		return null;
	}

}
