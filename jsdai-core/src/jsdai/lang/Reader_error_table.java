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

package jsdai.lang;

import jsdai.dictionary.*;
import java.util.*;

class Reader_error_table {

	Hashtable messages;

	Reader_error_table() throws SdaiException {

		Object val;
		messages = new Hashtable();
		

					/*  Scanning errors  */
		val = messages.put(new Integer(PhFileReader.ONLY_SIGN_IS_SPECIFIED),
			"only sign is specified.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.INTEGER_NUMBER_IS_TOO_LARGE),
			"integer number is too large to fit into long.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.BAD_REAL_LITERAL),
			"real literal without cyphers in an exponent.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.UNTERMINATED_STRING),
			"unterminated string.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.UNTERMINATED_BINARY),
			"unterminated binary.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.UNTERMINATED_ENUMERATION),
			"unterminated enumeration item.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.INCORRECT_ENUMERATION),
			"incorrect enumeration item.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.INVALID_USER_DEFINED_ENTITY_NAME),
			"invalid user-defined entity name.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.ZERO_INSTANCE_IDENTIFIER),
			"zero instance identifier.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.INCORRECT_INSTANCE_IDENTIFIER),
			"incorrect instance identifier.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.UNMATCHED_CLOSE_COMMENT),
			"unmatched close of a comment.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.UNTERMINATED_COMMENT),
			"comment not terminated.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.ILLEGAL_LOWERCASE),
			"lowercase is not allowed.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.UNEXPECTED_CHARACTER),
			"unexpected character.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.UNMATCHED_INPUT),
			"unmatched input.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.BAD_INPUT_STREAM),
			"bad input stream initializer.");
		if (val != null) {
			messages_exception();
		}



					/*  Parsing errors  */
		val = messages.put(new Integer(PhFileReader.UNEXPECTED_ENTITY),
			"unexpected header entity.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.INCORRECT_VALUE),
			"value is incompatible with attribute type.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.TOO_LESS_VALUES),
			"insufficient number of values.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.IMPROPER_LIST_ITEM),
			"list contains an improper item.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.LIST_CANNOT_BE_EMPTY),
			"list cannot be empty.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.SCHEMA_NOT_PROVIDED),
			"no express schema is provided.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.WRONG_TOKEN),
			"invalid token.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.ENTITY_EXPECTED),
			"entity name or right parenthesis is expected.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.WRONG_VALUE_IN_HEADER),
			"value in header is redefined or is entity instance identifier.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.TYPED_PARAMETER_IN_HEADER),
			"typed parameter value in user-defined header section entity.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.INVALID_STRING_IN_DSECTION),
			"schema name in data section is inconsistent with the header.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.TOO_MANY_VALUES_IN_DSECTION),
			"parameter list in data section contains too many values.");
		if (val != null) {
			messages_exception();
		}



					/* Errors in Reader actions */
		val = messages.put(new Integer(PhFileReader.DICTIONARY_MODEL_NOT_FOUND),
			"dictionary model not found.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.DUPLICATE_INSTANCE),
			"duplicate instance identifier.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.ILLEGAL_ACCESS),
			"class, method or field is not accessible.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.CLASS_NOT_FOUND),
			"class not found.");
		if (val != null) {
			messages_exception();
		}

		val = messages.put(new Integer(PhFileReader.INVALID_SECTION_NAME),
			"invalid section name in a header's entity.");
		if (val != null) {
			messages_exception();
		}

	}


	final private void messages_exception() throws SdaiException {
		String base = SdaiSession.line_separator + AdditionalMessages.RD_ERR + 
			"unable to initialize table of messages.";
		throw new SdaiException(SdaiException.SY_ERR, base);
	}


}
