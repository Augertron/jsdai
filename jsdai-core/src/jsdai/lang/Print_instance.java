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
import java.io.*;

class Print_instance {

	SdaiRepository repo2file;
	int row_length;
	Writer_error_table error_table;



	Print_instance(Writer_error_table err_table) {
		error_table = err_table;
	}


	private void write_byte(byte sym, boolean first, DataOutputStream output_stream)
			throws java.io.IOException {
		int next_row;
		if (!first) {
			output_stream.write(PhFileReader.COMMA_b);
			row_length++;
		}
		if (row_length >= PhFileWriter.ROW_LENGTH_LIMIT) {
			next_row = 1;
		} else {
			next_row = 0;
		}
		switch (next_row) {
			case 0:
				output_stream.write(sym);
				row_length ++;
				break;
			case 1:
				output_stream.write(PhFileWriter.RETURN);
				output_stream.write(PhFileWriter.INDENT);
				output_stream.write(sym);
				row_length = PhFileWriter.INDENT_LENGTH + 1;
				break;
		}
	}

	private void write_byte_array(byte [] string, boolean first,
			DataOutputStream output_stream) throws java.io.IOException {
		int next_row;
		if (!first) {
			output_stream.write(PhFileReader.COMMA_b);
			row_length++;
		}
		if (row_length >= PhFileWriter.ROW_LENGTH_LIMIT) {
			next_row = 1;
		} else {
			next_row = 0;
		}
		switch (next_row) {
			case 0:
				output_stream.write(string);
				row_length += string.length;
				break;
			case 1:
				output_stream.write(PhFileWriter.RETURN);
				output_stream.write(PhFileWriter.INDENT);
				output_stream.write(string);
				row_length = string.length + PhFileWriter.INDENT_LENGTH;
				break;
		}
	}

	private void write_byte_array(boolean first, DataOutputStream output_stream)
			throws java.io.IOException {
		int next_row;
		if (!first) {
			output_stream.write(PhFileReader.COMMA_b);
			row_length++;
		}
		if (row_length >= PhFileWriter.ROW_LENGTH_LIMIT) {
			next_row = 1;
		} else {
			next_row = 0;
		}
		StaticFields staticFields = StaticFields.get();
		switch (next_row) {
			case 0:
				output_stream.write(staticFields.string, 0, staticFields.string_length);
				row_length += staticFields.string_length;
				break;
			case 1:
				output_stream.write(PhFileWriter.RETURN);
				output_stream.write(PhFileWriter.INDENT);
				output_stream.write(staticFields.string, 0, staticFields.string_length);
				row_length = staticFields.string_length + PhFileWriter.INDENT_LENGTH;
				break;
		}
	}

	private void write_byte_array(boolean first, byte sym, DataOutputStream output_stream)
			throws java.io.IOException {
		int i;
		int next_row;
		int count_of_apostrophe = 0;
		int ptr;
		if (!first) {
			output_stream.write(PhFileReader.COMMA_b);
			row_length++;
		}
		if (row_length >= PhFileWriter.ROW_LENGTH_LIMIT) {
			next_row = 1;
		} else {
			next_row = 0;
		}
		StaticFields staticFields = StaticFields.get();
		switch (next_row) {
			case 0:
				output_stream.write(sym);
				if (sym == PhFileReader.APOSTROPHE) {
					ptr = 0;
					for (i = 0; i < staticFields.string_length; i++) {
						output_stream.write(staticFields.string[i]);
						if (staticFields.string[i] == PhFileReader.APOSTROPHE && ptr != 2) {
							output_stream.write(PhFileReader.APOSTROPHE);
							count_of_apostrophe++;
							ptr = 0;
						} else {
							if (ptr == 2) {
								ptr = 0;
							} else if (ptr == 1) {
								if (staticFields.string[i] == PhFileReader.BACKSLASH) {
									ptr = 2;
								} else {
									ptr = 0;
								}
							} else if (staticFields.string[i] == PhFileReader.CAPITAL_S) {
								ptr = 1;
							}
						}
					}
				} else {
					output_stream.write(staticFields.string, 0, staticFields.string_length);
				}
				output_stream.write(sym);
				row_length += staticFields.string_length + count_of_apostrophe + 2;
				break;
			case 1:
				output_stream.write(PhFileWriter.RETURN);
				output_stream.write(PhFileWriter.INDENT);
				output_stream.write(sym);
				if (sym == PhFileReader.APOSTROPHE) {
					for (i = 0; i < staticFields.string_length; i++) {
						output_stream.write(staticFields.string[i]);
						if (staticFields.string[i] == PhFileReader.APOSTROPHE) {
							output_stream.write(PhFileReader.APOSTROPHE);
							count_of_apostrophe++;
						}
					}
				} else {
					output_stream.write(staticFields.string, 0, staticFields.string_length);
				}
				output_stream.write(sym);
				row_length = staticFields.string_length + PhFileWriter.INDENT_LENGTH + count_of_apostrophe + 2;
				break;
		}
	}

	void write_byte_array_simple(boolean first, byte [] source, int source_ln, 
			byte sym, boolean next_row, DataOutputStream output_stream) throws java.io.IOException {
		if (!first) {
			output_stream.write(PhFileReader.COMMA_b);
		}
		if (next_row) {
			output_stream.write(PhFileWriter.RETURN);
			output_stream.write(PhFileWriter.INDENT);
		}
		if (sym != PhFileReader.SPACE) {
			output_stream.write(sym);
		}
		if (sym == PhFileReader.APOSTROPHE) {
			for (int i = 0; i < source_ln; i++) {
				output_stream.write(source[i]);
				if (source[i] == PhFileReader.APOSTROPHE) {
					output_stream.write(PhFileReader.APOSTROPHE);
				}
			}
		} else {
			output_stream.write(source, 0, source_ln);
		}
		if (sym != PhFileReader.SPACE) {
			output_stream.write(sym);
		}
	}

	int write_byte_array(boolean first, DataOutputStream output_stream, String str, int row_ln)
			throws java.io.IOException {
		row_length = row_ln;
		fromString(str);
		write_byte_array(first, PhFileReader.APOSTROPHE, output_stream);
		return row_length;
	}

	void write_byte_array_simple(boolean first, byte [] source, 
			byte sym, boolean next_row, DataOutputStream output_stream) throws java.io.IOException {
		write_byte_array_simple(first, source, source.length, sym, next_row, output_stream);
	}

	void write_byte_array_simple(boolean first, String str, byte sym, boolean next_row, 
			DataOutputStream output_stream) throws java.io.IOException {
		fromString(str);
		StaticFields staticFields = StaticFields.get();
		write_byte_array_simple(first, staticFields.string, staticFields.string_length, sym, next_row, output_stream);
	}

	int write_byte_array(boolean first, DataOutputStream output_stream, byte [] str, int row_ln)
			throws java.io.IOException {
		row_length = row_ln;
		write_byte_array(str, first, output_stream);
		return row_length;
	}

	void print_instance(ComplexEntityValue entity_values, byte [] name, int name_length, 
			DataOutputStream output_stream, int row_length_initial)
			throws java.io.IOException, SdaiException {
		boolean first;

		output_stream.write(name, 0, name_length);
		output_stream.write(PhFileReader.LEFT_PARENTHESIS);
		row_length = row_length_initial + name_length + 1;
		first = true;
		CEntity_definition def = entity_values.def;
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			int index = def.externalMappingIndexing[i];
			EntityValue pval = entity_values.entityValues[index];
			for (int j = 0; j < pval.count; j++) {
				Value val = pval.values[j];
				if (explore_value(first, val, output_stream, null)) {
					first = false;
				}
			}
		}
		output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
		output_stream.write(PhFileReader.SEMICOLON_b);
		output_stream.write(PhFileWriter.RETURN);
	}

	private void print_instance(EntityValue pval, boolean short_names, 
			SchemaData sch_data, DataOutputStream output_stream)
			throws java.io.IOException, SdaiException {
		boolean first;
		int index = sch_data.find_entity(0, sch_data.bNames.length - 1, 
//			pval.def.getNameUpperCase());
			(CEntityDefinition)pval.def);

		if (index < 0 || index >= sch_data.bNames.length) {
			String base = SdaiSession.line_separator + AdditionalMessages.WR_OUT + 
				SdaiSession.line_separator + "  " + pval.def.name + "  " +
				(String)error_table.messages.get(new Integer(PhFileWriter.ENTITY_NOT_FOUND_FOR_WRITER));
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		byte [] name;
		int name_length;
		if (short_names) {
			int index_short = sch_data.toShort[index];
			if (index_short >= 0) {
				name =  sch_data.bShortNames[index_short];
				name_length = sch_data.bShortNames[index_short].length;
			} else {
				name =  sch_data.bNames[index];
				name_length = sch_data.bNames[index].length;
			}
		} else {
			name =  sch_data.bNames[index];
			name_length = sch_data.bNames[index].length;
		}
		if (row_length + name_length + 2 >= PhFileWriter.ROW_LENGTH_LIMIT) {
			output_stream.write(PhFileWriter.RETURN);
			output_stream.write(PhFileWriter.INDENT);
			row_length = PhFileWriter.INDENT_LENGTH;
		}
		output_stream.write(name, 0, name_length);
		output_stream.write(PhFileReader.LEFT_PARENTHESIS);
		row_length += name_length + 1;
		first = true;
		for (int i = 0; i < pval.count; i++) {
			Value val = pval.values[i];
			if (explore_value(first, val, output_stream, null)) {
				first = false;
			}
		}
		output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
	}

	void print_instance(ComplexEntityValue entity_values, boolean short_names, 
			SchemaData sch_data, DataOutputStream output_stream, int row_length_initial)
			throws java.io.IOException, SdaiException {
		output_stream.write(PhFileReader.LEFT_PARENTHESIS);
		row_length = row_length_initial + 1;
		for (int i = 0; i < entity_values.def.noOfPartialEntityTypes; i++) {
			EntityValue pval = entity_values.entityValues[i];
			if (pval == null) break;
			// The MIXED_COMPLEX model does no longer provide the
			// information for all entity data types. So SchemaData needs to be updated
			SchemaData sd = pval.def.owning_model.schemaData;
			print_instance(pval, short_names, sd, output_stream);
		}
		output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
		output_stream.write(PhFileReader.SEMICOLON_b);
		output_stream.write(PhFileWriter.RETURN);
	}

	void print_instance(EntityValue pval, DataOutputStream output_stream,
			int row_length_initial, boolean in_header)
			throws java.io.IOException, SdaiException {
		output_stream.write(PhFileReader.LEFT_PARENTHESIS);
		row_length = row_length_initial + 1;
		boolean first = true;
		for (int i = 0; i < pval.count; i++) {
			Value val = pval.values[i];
			explore_value_for_header(first, val, output_stream, in_header);
			first = false;
		}
		output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
		output_stream.write(PhFileReader.SEMICOLON_b);
		output_stream.write(PhFileWriter.RETURN);
	}

	private boolean explore_value(boolean first, Value val,
			DataOutputStream output_stream, Object aggr)
			throws java.io.IOException, SdaiException {
		Value value_next;
		boolean first_next;

//System.out.println(" Print_instance   tag: " + val.tag);
		switch (val.tag) {
			case PhFileReader.MISSING:
				write_byte(PhFileReader.DOLLAR_SIGN, first, output_stream);
				return true;
			case PhFileReader.REDEFINE:
				write_byte(PhFileReader.ASTERISK, first, output_stream);
				return true;
			case PhFileReader.INTEGER:
				take_integer_value(val.integer);
				write_byte_array(first, output_stream);
				return true;
			case PhFileReader.REAL:
				String str = Double.toString(val.real);
				fromStringBasicLatin(str);
				write_byte_array(first, output_stream);
				return true;
			case PhFileReader.BOOLEAN:
				if (val.integer == 0) {
					write_byte_array(PhFileWriter.LOG_FALSE, first, output_stream);
				} else if (val.integer == 1) {
					write_byte_array(PhFileWriter.LOG_TRUE, first, output_stream);
				}
				return true;
			case PhFileReader.LOGICAL:
				if (val.integer == 0) {
					write_byte_array(PhFileWriter.LOG_FALSE, first, output_stream);
				} else if (val.integer == 1) {
					write_byte_array(PhFileWriter.LOG_TRUE, first, output_stream);
				} else {
					write_byte_array(PhFileWriter.LOG_UNKNOWN, first, output_stream);
				}
				return true;
			case PhFileReader.ENUM:
				fromStringBasicLatin(val.string);
				write_byte_array(first, PhFileReader.DOT, output_stream);
				return true;
			case PhFileReader.STRING:
				fromString(val.string);
				write_byte_array(first, PhFileReader.APOSTROPHE, output_stream);
				return true;
			case PhFileReader.BINARY:
				convertBinary((Binary)val.reference);
				write_byte_array(first, PhFileReader.QUOTATION_MARK, output_stream);
				return true;
			case PhFileReader.TYPED_PARAMETER:
				fromStringBasicLatin(val.string);
				write_byte_array(first, output_stream);
				output_stream.write(PhFileReader.LEFT_PARENTHESIS);
				row_length++;
				value_next = val.nested_values[0];
				first_next = true;
				boolean result = explore_value(first_next, value_next, output_stream, null);
				output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
				row_length++;
				return true;
			case PhFileReader.ENTITY_REFERENCE:
				long ident;
				if (val.reference instanceof SdaiModel.Connector) {
					SdaiModel modelIn = ((SdaiModel.Connector)val.reference).resolveModelIn();
					if(modelIn != null) {
						if (modelIn.repository == repo2file) {
							ident = ((SdaiModel.Connector)val.reference).instance_identifier;
						} else {
							if (aggr == null) {
								write_byte(PhFileReader.DOLLAR_SIGN, first, output_stream);
								return true;
							} else {
								if (((CAggregate)aggr).myType.express_type == DataType.ARRAY) {
									write_byte(PhFileReader.DOLLAR_SIGN, first, output_stream);
									return true;
								} else {
									return false;
								}
							}
						}
					} else {
						write_byte(PhFileReader.DOLLAR_SIGN, first, output_stream);
						return true;
					}
				} else {
					CEntity ref_inst = (CEntity)val.reference;
					if (ref_inst.owning_model == null) {
						throw new SdaiException(SdaiException.EI_NVLD, ref_inst,
							"Instance #" + ref_inst.instance_identifier +
							" of type " + ref_inst.getInstanceType().getName(null) );
					}
					if (ref_inst.owning_model.repository == repo2file) {
						ident = ref_inst.instance_identifier;
					} else {
						if (aggr == null) {
							write_byte(PhFileReader.DOLLAR_SIGN, first, output_stream);
							return true;
						} else {
							if (((CAggregate)aggr).myType.express_type == DataType.ARRAY) {
								write_byte(PhFileReader.DOLLAR_SIGN, first, output_stream);
								return true;
							} else {
								return false;
							}
						}
					}
				}
				take_instance_id(ident);
				write_byte_array(first, output_stream);
				return true;
			case PhFileReader.EMBEDDED_LIST:
				if (row_length >= PhFileWriter.ROW_LENGTH_LIMIT-2) {
					if (!first) {
						output_stream.write(PhFileReader.COMMA_b);
					}
					output_stream.write(PhFileWriter.RETURN);
					output_stream.write(PhFileWriter.INDENT);
					row_length = PhFileWriter.INDENT_LENGTH;
				} else if (!first) {
					output_stream.write(PhFileReader.COMMA_b);
					row_length++;
				}
				output_stream.write(PhFileReader.LEFT_PARENTHESIS);
				row_length++;
				first_next = true;
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					result = explore_value(first_next, value_next, output_stream, val.reference);
					if (result) {
						first_next = false;
					}
				}
				output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
				row_length++;
				return true;
			}
			return false;
	}

	private void explore_value_for_header(boolean first, Value val,
			DataOutputStream output_stream, boolean in_header)
			throws java.io.IOException, SdaiException {
		Value value_next;
		boolean first_next;

		switch (val.tag) {
			case PhFileReader.MISSING:
				write_byte(PhFileReader.DOLLAR_SIGN, first, output_stream);
				break;
			case PhFileReader.REDEFINE:
				if (in_header) {
					String base = SdaiSession.line_separator + AdditionalMessages.WR_OUT + 
					SdaiSession.line_separator + "     " +
					(String)error_table.messages.get(new Integer(PhFileWriter.REDEFINE_IN_HEADER));
					throw new SdaiException(SdaiException.SY_ERR, base);
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.WR_OUT + 
					SdaiSession.line_separator + "     " +
(String)error_table.messages.get(new Integer(PhFileWriter.REDEFINE_IN_USER_DEFINED));
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			case PhFileReader.INTEGER:
				take_integer_value(val.integer);
				write_byte_array(first, output_stream);
				break;
			case PhFileReader.REAL:
				String str = Double.toString(val.real);
				fromStringBasicLatin(str);
				write_byte_array(first, output_stream);
				break;
			case PhFileReader.BOOLEAN:
				if (val.integer == 0) {
					write_byte_array(PhFileWriter.LOG_FALSE, first, output_stream);
				} else if (val.integer == 1) {
					write_byte_array(PhFileWriter.LOG_TRUE, first, output_stream);
				}
				break;
			case PhFileReader.LOGICAL:
				if (val.integer == 0) {
					write_byte_array(PhFileWriter.LOG_FALSE, first, output_stream);
				} else if (val.integer == 1) {
					write_byte_array(PhFileWriter.LOG_TRUE, first, output_stream);
				} else {
					write_byte_array(PhFileWriter.LOG_UNKNOWN, first, output_stream);
				}
				break;
			case PhFileReader.ENUM:
				fromStringBasicLatin(val.string);
				write_byte_array(first, PhFileReader.DOT, output_stream);
				break;
			case PhFileReader.STRING:
				fromString(val.string);
				write_byte_array(first, PhFileReader.APOSTROPHE, output_stream);
				break;
			case PhFileReader.BINARY:
				convertBinary((Binary)val.reference);
				write_byte_array(first, PhFileReader.QUOTATION_MARK, output_stream);
				break;
			case PhFileReader.TYPED_PARAMETER:
				if (in_header) {
					String base = SdaiSession.line_separator + AdditionalMessages.WR_OUT + 
					SdaiSession.line_separator + "     " +
					(String)error_table.messages.get(new Integer(PhFileWriter.TYPED_PAR_IN_HEADER));
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				fromStringBasicLatin(val.string);
				write_byte_array(first, output_stream);
				output_stream.write(PhFileReader.LEFT_PARENTHESIS);
				row_length++;
				value_next = val.nested_values[0];
				first_next = true;
				explore_value_for_header(first_next, value_next, output_stream, in_header);
				output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
				row_length++;
				break;
			case PhFileReader.ENTITY_REFERENCE:
				if (in_header) {
					String base = SdaiSession.line_separator + AdditionalMessages.WR_OUT + 
					SdaiSession.line_separator + "     " +
					(String)error_table.messages.get(new Integer(PhFileWriter.REFERENCE_IN_HEADER));
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				take_instance_id(((CEntity)val.reference).instance_identifier);
				write_byte_array(first, output_stream);
				break;
			case PhFileReader.EMBEDDED_LIST:
				if (row_length >= PhFileWriter.ROW_LENGTH_LIMIT-2) {
					if (!first) {
						output_stream.write(PhFileReader.COMMA_b);
					}
					output_stream.write(PhFileWriter.RETURN);
					output_stream.write(PhFileWriter.INDENT);
					row_length = PhFileWriter.INDENT_LENGTH;
				} else if (!first) {
					output_stream.write(PhFileReader.COMMA_b);
					row_length++;
				}
				output_stream.write(PhFileReader.LEFT_PARENTHESIS);
				row_length++;
				first_next = true;
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					explore_value_for_header(first_next, value_next, output_stream, in_header);
					first_next = false;
				}
				output_stream.write(PhFileReader.RIGHT_PARENTHESIS);
				row_length++;
				break;
		}
	}

	private void take_instance_id(long lo) {
		int digit_index = -1;
		long next_number;
		StaticFields staticFields = StaticFields.get();
		while (lo != 0) {
			next_number = lo / 10;
			staticFields.string[++digit_index] = PhFileWriter.DIGITS[(int)(lo - next_number * 10)];
			lo = next_number;
		}
		staticFields.string[++digit_index] = PhFileReader.SPECIAL;
		for (int i = 0; i <= (digit_index - 1) / 2; i++) {
			byte sym = staticFields.string[i];
			staticFields.string[i] = staticFields.string[digit_index - i];
			staticFields.string[digit_index - i] = sym;
		}
		staticFields.string_length = digit_index + 1;
	}

	private void take_integer_value(int integ) throws SdaiException {
		boolean neg;
		byte sym;
		int number, next_number;
		if (integ == Integer.MIN_VALUE) {
			String base = SdaiSession.line_separator + AdditionalMessages.WR_OUT + 
				SdaiSession.line_separator + "     " +
				(String)error_table.messages.get(new Integer(PhFileWriter.INTERNAL_ERROR_WR));
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (integ < 0) {
			neg = true;
			number = -integ;
		} else {
			neg = false;
			number = integ;
		}
		StaticFields staticFields = StaticFields.get();
		if (integ == 0) {
			staticFields.string[0] = PhFileWriter.DIGITS[0];
			staticFields.string_length = 1;
			return;
		}
		int digit_index = -1;
		while (number != 0) {
			next_number = number / 10;
			digit_index++;
			staticFields.string[digit_index] = PhFileWriter.DIGITS[(int)(number - next_number * 10)];
			number = next_number;
		}
		if (neg) {
			staticFields.string[++digit_index] = PhFileReader.MINUS;
		}
		for (int i = 0; i <= (digit_index - 1) / 2; i++) {
			sym = staticFields.string[i];
			staticFields.string[i] = staticFields.string[digit_index - i];
			staticFields.string[digit_index - i] = sym;
		}
		staticFields.string_length = digit_index + 1;
	}

	void fromStringBasicLatin(String str) {
		StaticFields staticFields = StaticFields.get();
		if (staticFields.string.length < str.length()) {
			int len = staticFields.string.length;
			while (len < str.length()) {
				len *= 2;
			}
			staticFields.string = new byte[len];
		}
   	for (int l = 0; l < str.length(); l++) {
			staticFields.string[l] = (byte)str.charAt(l);
		}
		staticFields.string_length = str.length();
	}

	void fromString(String str) {
		int count = 0;
		boolean iso10646 = false;
		boolean non_basic = false;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.string.length < str.length()) {
			enlarge_string(staticFields, str.length());
		}
		for (int i = 0; i < str.length(); i++) {
			char sym = str.charAt(i);
			int range;
			int numb;
			int code;
			int res;
			int j;
			if (sym < 128) {
				range = 1;
			} else if (sym < 256) {
				range = 2;
			} else {
				range = 3;
			}
			if (range < 3 && iso10646) {
				if (staticFields.string.length < count + 4) {
					enlarge_string(staticFields, count + 4);
				}
				staticFields.string[count] = PhFileReader.BACKSLASH;
				staticFields.string[count + 1] = PhFileReader.CAPITAL_X;
				staticFields.string[count + 2] = PhFileReader.ZERO;
				staticFields.string[count + 3] = PhFileReader.BACKSLASH;
				count += 4;
				iso10646 = false;
			}
			switch (range) {
				case 1:
					if (sym < 32) {
						if (staticFields.string.length < count + 5) {
							enlarge_string(staticFields, count + 5);
						}
						staticFields.string[count] = PhFileReader.BACKSLASH;
						staticFields.string[count + 1] = PhFileReader.CAPITAL_X;
						staticFields.string[count + 2] = PhFileReader.BACKSLASH;
						count += 3;
						if (sym >= 16) {
							staticFields.string[count] = (byte)49;
							numb = sym - 16;
						} else {
							staticFields.string[count] = (byte)48;
							numb = sym;
						}
						count++;
						if (numb <= 9) {
							code = numb + (int)'0';
						} else {
							code = numb + (int)'A' - 10;
						}
						staticFields.string[count] = (byte)code;
						count++;
						non_basic = true;
						break;
					}
//					if (non_basic && string.length < count + 2) {
					if (staticFields.string.length < count + 2) {
						enlarge_string(staticFields, count + 2);
					}
					staticFields.string[count] = (byte)sym;
					count++;
					if (sym == PhFileReader.BACKSLASH) {
						staticFields.string[count++] = PhFileReader.BACKSLASH;
					}
					break;
				case 2:
					if (sym < 160 || sym == 255) {
						if (staticFields.string.length < count + 5) {
							enlarge_string(staticFields, count + 5);
						}
						staticFields.string[count] = PhFileReader.BACKSLASH;
						staticFields.string[count + 1] = PhFileReader.CAPITAL_X;
						staticFields.string[count + 2] = PhFileReader.BACKSLASH;
						count += 3;
						numb = sym;
						for (j = 0; j < 2; j++) {
							if (j > 0) {
								res = numb;
							} else {
								res = numb / 16;
							}
							if (res <= 9) {
								code = res + (int)'0';
							} else {
								code = res + (int)'A' - 10;
							}
							staticFields.string[count] = (byte)code;
							count++;
							if (j > 0) {
								break;
							}
							numb -= (res * 16);
						}
						non_basic = true;
						break;
					}
					if (staticFields.string.length < count + 4) {
						enlarge_string(staticFields, count + 4);
					}
					staticFields.string[count] = PhFileReader.BACKSLASH;
					staticFields.string[count + 1] = PhFileReader.CAPITAL_S;
					staticFields.string[count + 2] = PhFileReader.BACKSLASH;
					staticFields.string[count + 3] = (byte)((int)sym - 128);
					count += 4;
					non_basic = true;
					break;
				case 3:
					if (!iso10646) {
						if (staticFields.string.length < count + 8) {
							enlarge_string(staticFields, count + 8);
						}
						staticFields.string[count] = PhFileReader.BACKSLASH;
						staticFields.string[count + 1] = PhFileReader.CAPITAL_X;
						staticFields.string[count + 2] = PhFileReader.TWO;
						staticFields.string[count + 3] = PhFileReader.BACKSLASH;
						count += 4;
						iso10646 = true;
					} else {
						if (staticFields.string.length < count + 4) {
							enlarge_string(staticFields, count + 4);
						}
					}
					numb = sym;
					int divisor = 4096;
					for (j = 0; j < 4; j++) {
						res = numb / divisor;
						if (res <= 9) {
							code = res + (int)'0';
						} else {
							code = res + (int)'A' - 10;
						}
						staticFields.string[count] = (byte)code;
						count++;
						numb -= (res * divisor);
						divisor /= 16;
					}
					non_basic = true;
					break;
			}
		}
		if (iso10646) {
			if (staticFields.string.length < count + 4) {
				enlarge_string(staticFields, count + 4);
			}
			staticFields.string[count] = PhFileReader.BACKSLASH;
			staticFields.string[count + 1] = PhFileReader.CAPITAL_X;
			staticFields.string[count + 2] = PhFileReader.ZERO;
			staticFields.string[count + 3] = PhFileReader.BACKSLASH;
			count += 4;
		}
		staticFields.string_length = count;
	}

	void convertBinary(Binary value) throws SdaiException {
		if (value == null) {
			return;
		}
		int ln = value.value.length * 2 + 1;
		if (value.unused > 3) {
			ln--;
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.string.length < ln) {
			int len = staticFields.string.length * 2;
			if (len < ln) {
				len = ln;
			}
			staticFields.string = new byte[len];
		}
		value.toByteArray(staticFields.string);
		staticFields.string_length = ln;
	}

	private static void enlarge_string(StaticFields staticFields, int demand) {
		int new_length = staticFields.string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_string[] = new byte[new_length];
		System.arraycopy(staticFields.string, 0, new_string, 0, staticFields.string.length);
		staticFields.string = new_string;
	}

}
