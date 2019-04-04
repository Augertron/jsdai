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

  // declaration of non-VAR parameters
  Value _nonvar__e_operandz;
  // end of declaration of non-VAR parameters

  // declaration of local variables
  Value _e_operands;
  // end of declaration of local variables

  public Value run(SdaiContext _context, Value _e_operandz) throws SdaiException {

    // initialization of non-VAR parameters
    _nonvar__e_operandz = Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_view_or_subtype_expression)
        .set(_e_operandz);
    // end of initialization of non-VAR parameters

    // initialization of local variables
    _e_operands = Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_subtype_expression).create();
    // end of initialization of local variables

    Value _end1 = Value.alloc(ExpressTypes.NUMBER_TYPE).set(Value.alloc(ExpressTypes.INTEGER_TYPE).sizeOf(_context, _nonvar__e_operandz));
    Value _step1 = Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1);
    for (Value _implicit_1_i = Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1);
         Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal(_context, _implicit_1_i, _end1).getLogical() == 2; _implicit_1_i.inc(_step1)) {
      if (Value.alloc(ExpressTypes.LOGICAL_TYPE).NOT(Value.alloc(ExpressTypes.LOGICAL_TYPE)
          .IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.VIEW_DEFINITION", "EXTENDED_DICTIONARY_SCHEMA"),
              _nonvar__e_operandz.indexing(_context, _implicit_1_i, null).typeOfV(_context))).getLogical() == 2) {
        _e_operands.set(_context, Value.alloc().addOrUnionOrConcatenate(_context, _e_operands, _nonvar__e_operandz.indexing(_context, _implicit_1_i, null)));
      }
    } // for - REPEAT

    if (true) {
      return Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_subtype_expression).set(_context, _e_operands)
          .check(_context, jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generalset_0_entity_or_subtype_expression);
    }

    return Value.alloc(ExpressTypes.GENERIC_TYPE).unset(); // if return is missing in express - add printing of error message?
  }

}
