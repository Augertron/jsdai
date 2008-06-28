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

import java.io.*;
import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;


public class ECtVariable implements Serializable {
  String name;
  String scope_id;
  String key;
  int depth;
  EParameter type;
  EEntity scope;
	boolean is_implicit;
	int implicit_type; // 1 - query_expression, 2 - repeat statement increment control, 3 - alias statement

	public ECtVariable(String name1, EParameter type1, int depth1, String key1, EEntity scope1) {
    name = name1;
    type = type1;
		depth = depth1;
		key = key1;
		scope = scope1;
    scope_id = "";
    is_implicit = false;
    implicit_type = 0;
  }

	public ECtVariable(String name1, EParameter type1, int depth1, String key1, EEntity scope1, boolean is_implicit, int implicit_type,  String scope_id1) {
    name = name1;
    type = type1;
		depth = depth1;
		key = key1;
		scope = scope1;
    scope_id = "";
    this.is_implicit = is_implicit;
    this.implicit_type = 1;
  	scope_id = scope_id1;
  }

  public ECtVariable(String name1, EParameter type1) {
    name = name1;
    type = type1;
    scope_id = "";
  }

  public ECtVariable(String name1, EParameter type1, String scope_id1) {
    name = name1;
    type = type1;
    scope_id = scope_id1;
  }

  void setName(String new_name) {
    name = new_name;
  }

  String getName() {
    return name;
  }

  void setScope_id(String new_scope_id) {
    scope_id = new_scope_id;
  }

  public boolean isSetScope_id() {
    if ((scope_id != null) && (scope_id != "")) {
      return true;
    } else {
      return false;
    }
  }

  String getScope_id() {
    return scope_id;
  }

  EParameter getType() {
    return type;
  }

  void setType(EParameter a_type) {
    type = a_type;
  }

	public String toString() {
		return name;
	}

}
