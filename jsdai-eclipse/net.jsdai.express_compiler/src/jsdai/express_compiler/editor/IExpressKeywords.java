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

package jsdai.express_compiler.editor;

public interface IExpressKeywords {


// keywords, perhaps I will remove types and make them a separate category
String[] keywords = { 
 	"ABSTRACT", 
//	"AGGREGATE", 
	"ALIAS", 
//	"ARRAY",
	"AS", 
//	"BAG", 
	"BASED_ON", 
	"BEGIN",
//	"BINARY", 
//	"BOOLEAN", 
	"BY", 
	"CASE",
	"CONSTANT", 
	"DERIVE", 
	"ELSE", 
	"END",
	"END_ALIAS", 
	"END_CASE", 
	"END_CONSTANT", 
	"END_ENTITY",
	"END_FUNCTION", 
	"END_IF", 
	"END_LOCAL", 
	"END_PROCEDURE",
	"END_REPEAT", 
	"END_RULE", 
	"END_SCHEMA", 
	"END_SUBTYPE_CONSTRAINT",
	"END_TYPE", 
	"ENTITY", 
	"ENUMERATION", 
	"ESCAPE",
	"EXTENSIBLE", 
	"FIXED", 
	"FOR", 
	"FROM",
	"FUNCTION", 
//	"GENERIC", 
//	"GENERIC_ENTITY", 
	"IF",
//	"INTEGER", 
	"INVERSE", 
//	"LIST", 
	"LOCAL",
//	"LOGICAL", 
//	"NUMBER", 
	"OF", 
	"ONEOF",
	"OPTIONAL", 
	"OTHERWISE", 
	"PROCEDURE", 
	"QUERY",
//	"REAL", 
	"RENAMED", 
	"REFERENCE", 
	"REPEAT",
	"RETURN", 
	"RULE", 
	"SCHEMA", 
	"SELECT",
//	"SET", 
	"SKIP", 
//	"STRING", 
	"SUBTYPE",
	"SUBTYPE_CONSTRAINT", 
	"SUPERTYPE", 
	"THEN", 
	"TO",
	"TOTAL_OVER", 
	"TYPE", 
	"UNIQUE", 
	"UNTIL",
	"USE", 
	"VAR", 
	"WHERE", 
	"WHILE",
	"WITH"
};

// types, a subset of keywords, made artificially by me, Express does not define types separately
String[] types = { 
	"BINARY", 
	"BAG", 
	"ARRAY",
	"AGGREGATE", 
	"BOOLEAN", 
	"GENERIC", 
	"GENERIC_ENTITY", 
	"INTEGER", 
	"LIST", 
	"LOGICAL", 
	"NUMBER", 
	"REAL", 
	"SET", 
	"STRING"
};

// operators
String[] operators = {
	"AND", 
	"ANDOR", 
	"DIV", 
	"IN",
	"LIKE", 
	"MOD", 
	"NOT", 
	"OR",
	"XOR"
};

// ------------- BUILT-IN CONSTANTS--------
String[] builtin_constants = {
//? 
 "SELF", 
	"CONST_E", 
	"PI",
	"FALSE", 
	"TRUE", 
	"UNKNOWN",
};

//---------------- BUILT-IN FUNCTIONS

String[] builtin_functions = {
	"ABS", 
	"ACOS", 
	"ASIN", 
	"ATAN",
	"BLENGTH", 
	"COS", 
	"EXISTS", 
	"EXP",
	"FORMAT", 
	"HIBOUND", 
	"HIINDEX", 
	"LENGTH",
	"LOBOUND", 
	"LOG", 
	"LOG2", 
	"LOG10",
	"LOINDEX", 
	"NVL", 
	"ODD", 
	"ROLESOF",
	"SIN", 
	"SIZEOF", 
	"SQRT", 
	"TAN",
	"TYPEOF", 
	"USEDIN", 
	"VALUE", 
	"VALUE_IN",
	"VALUE_UNIQUE",
};
//------------- BUILT-IN PROCEDURES ------------
String[] builtin_procedures = {
	"INSERT", 
	"REMOVE"
};

}


