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

// Java class implementing EXPRESS function get_list_of_entity_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class FGet_list_of_entity_definition {

  // declaration of non-VAR parameters
  Value _nonvar__e_generic_supertypes;
  // end of declaration of non-VAR parameters

  // declaration of local variables
  Value _e_supertypes;
  // end of declaration of local variables

  public Value run(SdaiContext _context, Value _e_generic_supertypes) throws SdaiException {

    // initialization of non-VAR parameters
    _nonvar__e_generic_supertypes = Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generallist_0_entity_or_view_definition)
        .set(_e_generic_supertypes);
    // end of initialization of non-VAR parameters

    // initialization of local variables
    _e_supertypes = Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generallist_0_entity_definition).create();
    // end of initialization of local variables

    Value _end2 = Value.alloc(ExpressTypes.NUMBER_TYPE).set(Value.alloc(ExpressTypes.INTEGER_TYPE).sizeOf(_context, _nonvar__e_generic_supertypes));
    Value _step2 = Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1);
    for (Value _implicit_2_i = Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1);
         Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal(_context, _implicit_2_i, _end2).getLogical() == 2; _implicit_2_i.inc(_step2)) {
      if (Value.alloc(ExpressTypes.LOGICAL_TYPE)
          .IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.ENTITY_DEFINITION", "EXTENDED_DICTIONARY_SCHEMA"),
              _nonvar__e_generic_supertypes.indexing(_context, _implicit_2_i, null).typeOfV(_context)).getLogical() == 2) {
        _e_supertypes.set(_context,
            Value.alloc().addOrUnionOrConcatenate(_context, _e_supertypes, _nonvar__e_generic_supertypes.indexing(_context, _implicit_2_i, null)));
      }
    } // for - REPEAT

    if (true) {
      return Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generallist_0_entity_definition).set(_context, _e_supertypes)
          .check(_context, jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_generallist_0_entity_definition);
    }

    return Value.alloc(ExpressTypes.GENERIC_TYPE).unset(); // if return is missing in express - add printing of error message?
  }

}
