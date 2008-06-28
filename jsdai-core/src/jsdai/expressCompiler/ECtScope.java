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


/*

scopes

express



Item               Scope    Identifier
alias statement      �           �1
attribute            -           � 
constant             -           � 
enumeration          -           � 
entity               �           � 
function             �           � 
parameter            -           � 
procedure            �           � 
query expression     �           �1
repeat statement     �           �1,2
rule                 �           �3
rule label           -           � 
schema               �           � 
subtype constraint   �           � 
type                 �           � 
type label           -           � 
variable             -           �
-------------------------------------------------------------------------------------------------------
NOTE 1 The identifier is an implicitly declared variable within the defined scope of the declaration.
NOTE 2 The variable is only implicitly declared when an increment control is specified.
NOTE 3 An implicit variable declaration is made for all entities which are constrained by the rule.


express-x

Item                     Scope     Identifier
Instantiation Loop         �            �1
For Expression             �            �1
Dependent map, Map         �            �
Dependent view, View       �            �
Source Parameter           -            �
Target Parameter           -            �
Path Expression            �            �
----------------------------------------------------------------------------------------------
NOTE1 - the identifier is an implicitly declared variable within the scope of the declaration.








*/

public class ECtScope  {

	// null, if nested query or similar stuff
	EEntity parent_active_scope;
	EEntity current_active_scope;

	// may be for extensions only, null, if parent_active_scope is not null
	// or may be instantiated to back-up all scopes, we'll see
	ECtScope parent;

	// may be calculated, or may be kept up-to-date in this special field
	String current_scope_string;
	String parent_scope_string;
	String id;
	String id2; // for the case of two implicit variables in the same scope (Express-X)

	// not clear now, why it is called parent_key and not current_key, at least for scope extensions
	String parent_key = null;
  String parent_key2 = null; // for the case of two implicit variables in the same scope (Express-X)
	String current_key;

	public ECtScope(EEntity parent_active_scope, EEntity current_active_scope, ECtScope parent_scope, String scope_string, String id, String key) {
		this.parent_active_scope = parent_active_scope;
		this.current_active_scope = current_active_scope;
//	System.out.println("IN SCOPE - current_active_scope: " + current_active_scope + ", parent_active_scope: " + parent_active_scope);
		parent = parent_scope;
		current_scope_string = "";
		parent_scope_string = scope_string; 
		this.id = id;
		id2 = null;
		parent_key = key;
		parent_key2 = null;
//System.out.println(">>>>>> CREATING NEW scope instance: \n\tparent_active_scope: " + parent_active_scope + "\n\tcurrent_active_scope: " + current_active_scope +
//									"\n\tparent_scope: " + parent_scope + "\n\tscope_string: " + scope_string + "\n\tid: " + id + "\n\tkey: " + key + "\n<<<<<<<<<<");
	}

	// methods to calculate string prefices etc. could be here.

	String getCurrent_scope_string() {
		return current_scope_string;
	}
	String getParent_scope_string() {
		return "";
//		return parent_scope_string;
	}
	EEntity getParent_active_scope() {
		return parent_active_scope;
	}
	EEntity getCurrent_active_scope() {
		return current_active_scope;
	}
	ECtScope getParent() {
		if (parent != null) {
//			System.out.println("###### TO PREVIOUS SCOPE: " + parent.toString());
		} else {
			// may not be an error, if in schema scope, therefore, no parent above schema (perhaps better to implement a super-root scope)
//			if (!(current_active_scope instanceof ESchema_definition)) {
			if (!((current_active_scope instanceof ESchema_definition) || (current_active_scope instanceof ESchema_map_definition))) {
				System.out.println("###### TO PREVIOUS SCOPE: parent is NULL, current: " + current_active_scope);
			}
		}
		return parent;
	}
	String getId() {
		return id;
	}
	String getId2() {
		return id;
	}
	String getParentKey() {
		return parent_key;
	}
	String getParentKey2() {
		return parent_key2;
	}
	void set2ndId(String an_id) {
		id2 = an_id;
	}
	void set2ndKey(String a_key) {
		parent_key2 = a_key;
	}

	String getKey() {
		return current_key;
	}


	public String toString() {
//		if (true) return "";
		String result = "\n------ PRINTING scope: ------";
		result += "\nparent_active_scope: " + parent_active_scope;
		result += "\ncurrent_active_scope: " + current_active_scope;
		result += "\ncurrent_scope_string (not expected): " + current_scope_string;
		result += "\nparent_scope_string: " + parent_scope_string;
		result += "\nid: " + id;
		result += "\nparent_key: " + parent_key;
		result += "\ncurrent_key (not expected): " + current_key;
		if (parent != null) {
			result += "\nparent - NOT NULL: ";
//			result += "parent:\n" + parent + "\n>>> end of parent <<<";
		} else {
			result += "\nparent - NULL";
		}
		if (id2 != null) {
			result += "\nid2: " + id2;
		}
		if (parent_key2 != null) {
			result += "\nparent_key2: " + parent_key2;
		}
		result += "\n----------------------------------\n";		
		return result;
	}

}