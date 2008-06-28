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
import java.io.InputStream;

class Scanner {
	SdaiSession mySession;
	boolean begin_or_end_expected;
	boolean file_population_comments;
	private final int BUFFER_SIZE = 512;
	private final int COMMENT_DEF_LENGTH = 128;
	private InputStream instream;
	private int buffer_index;
	private int buffer_read;
	private byte buffer[];
	private int line;
	private int column;
	private int index;
	private double real_for_power;
	private byte s;

	private final static int keyword_length[] =
		{4,8,6,6,5,12,16};
	private int keyword_flag[] = new int[7];
	private final int num_keywords_without_minus = 5;
	private byte digits[];
	private final int DIGITS_COUNT = 124;



	Scanner (InputStream instr, SdaiSession session) throws SdaiException, java.io.IOException {
		instream = instr;
		mySession = session;
		buffer = new byte[BUFFER_SIZE];
		digits = new byte[DIGITS_COUNT];
		initialize();

		keyword_flag[0] = PhFileReader.DATA;
		keyword_flag[1] = PhFileReader.ENDSCOPE;
		keyword_flag[2] = PhFileReader.ENDSEC;
		keyword_flag[3] = PhFileReader.HEADER;
		keyword_flag[4] = PhFileReader.SCOPE;
		keyword_flag[5] = PhFileReader.ISO_BEGIN;
		keyword_flag[6] = PhFileReader.ISO_END;
	}



	void attach_stream(InputStream instr, SdaiSession session)	throws SdaiException, java.io.IOException {
		instream = instr;
		mySession = session;
		initialize();
	}


	void initialize()	throws SdaiException, java.io.IOException {
		if (instream == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.RD_SCAN + 
				(String)mySession.getReader().error_table.messages.get(new Integer(PhFileReader.BAD_INPUT_STREAM));
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		buffer_read = 0;
		buffer_index = -1;
		line = 0;
		column = 0;
		begin_or_end_expected = true;
		file_population_comments = false;
		advance();
	}


	private void advance ()	throws java.io.IOException {
		buffer_index++;
		if (buffer_index < buffer_read) {
			column++;
			s = buffer[buffer_index];
		} else {
			buffer_index = 0;
			buffer_read = instream.read(buffer,0,buffer.length);
			if (buffer_read == -1) {
				s = (byte)PhFileReader.EOF;
			} else {
				column++;
				s = buffer[buffer_index];
			}
		}
	}


	private void skip_blanks() throws java.io.IOException
	{
		if (s == PhFileReader.EOF) {
			return;
		}
		while (s == PhFileReader.SPACE || s == (byte)'\n' || s == (byte)'\r' ||
						s == (byte)'\t' || s == (byte)'\b' || s == (byte)'\f' || s == 0) {
			if (s == (byte)'\n') {
				line++;
				column=0;
			} else if (s == 0) {
				printWarningToLogo(mySession);
			}
			advance();
			if (s == PhFileReader.EOF) {
				return;
			}
		}
	}


	private boolean check_keyword_containing_minus(Token tok, int keyword_index)
	{
		int i;
		boolean coincide;

		if (index+1 != keyword_length[keyword_index]) {
			return false;
		}
		for (i = 0, coincide = true; i <= index; i++) {
			if (tok.string[i] != PhFileReader.keywords[keyword_index][i]) {
				coincide = false;
				break;
			}
		}
		return coincide;
	}


	private void ident_internal(Token tok) throws java.io.IOException
	{
		index++;
		if (index >= tok.string.length) {
			tok.enlarge();
		}
		tok.string[index] = s;
		advance();
		while ('A' <= s && s <= 'Z' || '0' <= s && s <= '9' || s == PhFileReader.UNDERSCORE) {
			index++;
			if (index >= tok.string.length) {
				tok.enlarge();
			}
			tok.string[index] = s;
			advance();
		}
	}


	private void ident(Token tok) throws java.io.IOException
	{
		int i, j;
		int find;
		boolean coincide;

		tok.line = line;
		tok.column = column;
		tok.string[0] = s;
		index = 0;
		advance();
		while ('A' <= s && s <= 'Z' || '0' <= s && s <= '9' || s == PhFileReader.UNDERSCORE) {
			index++;
			if (index >= tok.string.length) {
				tok.enlarge();
			}
			tok.string[index] = s;
			advance();
		}
		for (i = 0, find = -1; i < num_keywords_without_minus; i++) {
			if (index+1 != keyword_length[i]) {
				continue;
			}
			for (j = 0, coincide = true; j <= index; j++) {
				if (tok.string[j] != PhFileReader.keywords[i][j]) {
					coincide = false;
					break;
				}
			}
			if (coincide) {
				find=i;
				break;
			}
		}
		if (find >= 0) {
			tok.type = keyword_flag[find];
			return;
		}
		tok.length = index+1;
		if (s == PhFileReader.MINUS && begin_or_end_expected){
			ident_internal(tok);
			if (s == PhFileReader.MINUS) {
				ident_internal(tok);
				if ( check_keyword_containing_minus(tok, 5) ) {
					tok.type = keyword_flag[5];
					return;
				}
				if (s == PhFileReader.MINUS) {
					ident_internal(tok);
					if ( check_keyword_containing_minus(tok, 6) ) {
						tok.type = keyword_flag[6];
						return;
					}
				}
			}
		}
		tok.type = PhFileReader.ENTITY_NAME;
	}


	private void ident_user_defined(Token tok) throws java.io.IOException
	{
		tok.line = line;
		tok.column = column;
		tok.string[0] = PhFileReader.EXCLAMATION_MARK;
		advance();
		if ('A' <= s && s <= 'Z' || s == PhFileReader.UNDERSCORE) {
			tok.string[1] = s;
			index = 1;
			advance();
			while ('A' <= s && s <= 'Z' || '0' <= s && s <= '9' || s == PhFileReader.UNDERSCORE) {
				index++;
				if (index >= tok.string.length) {
					tok.enlarge();
				}
				tok.string[index] = s;
				advance();
			}
			tok.type = PhFileReader.USER_DEFINED_KEYWORD;
			tok.length = index + 1;
		} else {
			tok.line = line;
			tok.column = column;
			tok.type = PhFileReader.ERROR;
			tok.integer = PhFileReader.INVALID_USER_DEFINED_ENTITY_NAME;
		}
	}


	private void ident_in_enumeration(Token tok) throws java.io.IOException
	{
		tok.line = line;
		tok.column = column;
		advance();
		if ('A' <= s && s <= 'Z' || s == PhFileReader.UNDERSCORE) {
			tok.string[0] = s;
			index = 0;
			advance();
			while ('A' <= s && s <= 'Z' || '0' <= s && s <= '9' || s == PhFileReader.UNDERSCORE) {
				index++;
				if (index >= tok.string.length) {
					tok.enlarge();
				}
				tok.string[index] = s;
				advance();
			}
			if (s == PhFileReader.DOT) {
				tok.type = PhFileReader.ENUM;
				tok.length = index + 1;
				advance();
			} else {
				tok.line = line;
				tok.column = column;
				tok.type = PhFileReader.ERROR;
				tok.integer = PhFileReader.UNTERMINATED_ENUMERATION;
			}
		} else {
			tok.line = line;
			tok.column = column;
			tok.type = PhFileReader.ERROR;
			tok.integer = PhFileReader.INCORRECT_ENUMERATION;
		}
	}


/*	private void number(Token tok) throws java.io.IOException {
		int converted_to_real = 0;
		int exponent, number_of_digits;
		long result;
		long limit, threshold, digit;
		double real_result = 0, fraction;
		boolean negative;

		tok.line = line;
		tok.column = column;

		// Checking of negativity and taking the limits
		if (s == PhFileReader.MINUS) {
					negative = true;
					limit = Long.MIN_VALUE;
					advance();
		} else {
			negative = false;
			limit = -Long.MAX_VALUE;
			if (s == PhFileReader.PLUS) {
				advance();
			}
		}
		threshold = limit/10;

		// Processing of digits
		if ('0' <= s && s <= '9') {
			result = -Character.digit( (char)s, 10 );
			advance();
			while ('0' <= s && s <= '9') {
				switch (converted_to_real) {
						case 0:
								digit = Character.digit( (char)s, 10 );
								if (result < threshold) {
									converted_to_real = 1;
									real_result = result;
									real_result *= 10;
									real_result -= digit;
								} else {
									result *= 10;
									if (result < limit + digit) {
										converted_to_real = 1;
										real_result = result;
										real_result -= digit;
									} else {
										result -= digit;
									}
								}
								break;
						case 1:
								real_result *= 10;
								real_result -= Character.digit( (char)s, 10 );
								break;
				}
				advance();
			}
		} else {
			tok.type = PhFileReader.ERROR;
			tok.integer = PhFileReader.ONLY_SIGN_IS_SPECIFIED;
			return;
		}
  //	Integer or integer part of a real is stored using the variables:
  //	result or real_result, negative and converted_to_real

		if (s != PhFileReader.DOT) {     // integer number
			if (converted_to_real == 1) {
				tok.type = PhFileReader.ERROR;
				tok.integer = PhFileReader.INTEGER_NUMBER_IS_TOO_LARGE;
				return;
			}
			tok.type = PhFileReader.INTEGER;
			if (negative) {
				tok.integer = (int)result;
			} else {
				tok.integer = (int)-result;
			}
			return;
		}
									//  real number
		advance();
		if (converted_to_real == 0) {
			if (negative) {
				real_result = result;
			} else {
				real_result = -result;
			}
		} else if (!negative) {
			real_result = -real_result;
		}
		fraction = 0.;
		number_of_digits = 0;
		while ('0' <= s && s <= '9') {
			fraction *= 10;
			fraction += Character.digit( (char)s, 10 );
			number_of_digits++;
			advance();
		}
		exponent = 0;
		if (s == 'E') {
			boolean negative_exp;
			advance();
			if (s == PhFileReader.MINUS) {
				negative_exp = true;
				advance();
			} else {
				negative_exp = false;
				if (s == PhFileReader.PLUS) {
					advance();
				}
			}
			if ('0' <= s && s <= '9') {
				while ('0' <= s && s <= '9') {
					exponent *= 10;
					exponent += Character.digit( (char)s, 10 );
					advance();
				}
			} else {
				tok.line = line;
				tok.column = column;
				tok.type = PhFileReader.ERROR;
				tok.integer = PhFileReader.BAD_REAL_LITERAL;
				return;
			}
			if (negative_exp) {
				exponent = -exponent;
			}
		}
		if (exponent > 0) {
			power10(exponent);
			real_result *= real_for_power;
		} else if (exponent < 0) {
			power10(-exponent);
			real_result /= real_for_power;
		}
		exponent -= number_of_digits;
		if (exponent > 0) {
			power10(exponent);
			fraction *= real_for_power;
		} else if (exponent < 0) {
			power10(-exponent);
			fraction /= real_for_power;
		}
		tok.type = PhFileReader.REAL;
		if (negative) {
			tok.real = real_result - fraction;
		} else {
			tok.real = real_result + fraction;
		}
	}*/


	private void number(Token tok) throws java.io.IOException {
		int d_count = 0;
		int start = 1;
		long limit, threshold;
		boolean negative;
		boolean converted_to_real = false;

		tok.line = line;
		tok.column = column;
		/* Checking of negativity and taking the limits  */
		if (s == PhFileReader.MINUS) {
			negative = true;
			start = 2;
			limit = Long.MIN_VALUE;
			digits[d_count++] = s;
			advance();
		} else {
			negative = false;
			limit = -Long.MAX_VALUE;
			if (s == PhFileReader.PLUS) {
				start = 2;
				digits[d_count++] = s;
				advance();
			}
		}
		threshold = limit/10;

		/* Processing of digits  */
		if ('0' <= s && s <= '9') {
			while ('0' <= s && s <= '9') {
				if (d_count >= digits.length) {
					enlarge_digits();
				}
				digits[d_count++] = s;
				advance();
			}
		} else {
			tok.type = PhFileReader.ERROR;
			tok.integer = PhFileReader.ONLY_SIGN_IS_SPECIFIED;
			return;
		}
		/* integer number */
		if (s != PhFileReader.DOT) {
			long result = -Character.digit( (char)digits[start-1], 10 );
			for (int i = start; i < d_count; i++) {
				long digit = Character.digit( (char)digits[i], 10 );
				if (result < threshold) {
					converted_to_real = true;
					break;
				} else {
					result *= 10;
					if (result < limit + digit) {
						converted_to_real = true;
						break;
					} else {
						result -= digit;
					}
				}
			}
			if (converted_to_real) {
				tok.type = PhFileReader.ERROR;
				tok.integer = PhFileReader.INTEGER_NUMBER_IS_TOO_LARGE;
				return;
			}
			tok.type = PhFileReader.INTEGER;
			if (negative) {
				tok.integer = (int)result;
			} else {
				tok.integer = (int)-result;
			}
			return;
		}
		/*  real number  */
		if (d_count >= digits.length) {
			enlarge_digits();
		}
		digits[d_count++] = s;
		advance();
		while ('0' <= s && s <= '9') {
			if (d_count >= digits.length) {
				enlarge_digits();
			}
			digits[d_count++] = s;
			advance();
		}
		if (s == 'E') {
			if (d_count >= digits.length - 1) {
				enlarge_digits();
			}
			digits[d_count++] = s;
			advance();
			if (s == PhFileReader.MINUS || s == PhFileReader.PLUS) {
				digits[d_count++] = s;
				advance();
			}
			if ('0' <= s && s <= '9') {
				while ('0' <= s && s <= '9') {
					if (d_count >= digits.length) {
						enlarge_digits();
					}
					digits[d_count++] = s;
					advance();
				}
			} else {
				tok.line = line;
				tok.column = column;
				tok.type = PhFileReader.ERROR;
				tok.integer = PhFileReader.BAD_REAL_LITERAL;
				return;
			}
		}
		tok.type = PhFileReader.REAL;
		tok.real = Double.parseDouble(new String(digits, 0, d_count));
	}


	private void get_string(Token tok) throws java.io.IOException {
		int ptr = 0;
		tok.line = line;
		tok.column = column;
		tok.integer = 0;
		index = -1;
		advance();
		while(true) {
			if (s == PhFileReader.EOF) {
				tok.line = line;
				tok.column = column;
				tok.type = PhFileReader.ERROR;
				tok.integer = PhFileReader.UNTERMINATED_STRING;
				return;
			}
			if (s == (byte)'\n') {
				line++;
				column=0;
			} else if (s == PhFileReader.APOSTROPHE) {
				if (ptr == 2) {
					index++;
					if (index >= tok.string.length) {
						tok.enlarge();
					}
					tok.string[index] = s;
					ptr = 0;
				} else {
					advance();
					if (s != PhFileReader.APOSTROPHE) {
						tok.type = PhFileReader.STRING;
						tok.length = index + 1;
						return;
					} else {
						index++;
						if (index >= tok.string.length) {
							tok.enlarge();
						}
						tok.string[index] = s;
					}
				}
			} else {
				index++;
				if (index >= tok.string.length) {
					tok.enlarge();
				}
				tok.string[index] = s;
				if (s == PhFileReader.BACKSLASH) {
					tok.integer = 1;
				}
				if (ptr == 2) {
					ptr = 0;
				} else if (ptr == 1) {
					if (s == PhFileReader.BACKSLASH) {
						ptr = 2;
					} else {
						ptr = 0;
					}
				} else if (s == PhFileReader.CAPITAL_S) {
					ptr = 1;
				}
			}
			advance();
		}
	}


	private void get_binary(Token tok) throws java.io.IOException
	{
		tok.line = line;
		tok.column = column;
		index = -1;
		advance();
		while(true) {
			if (s == PhFileReader.EOF) {
				tok.line = line;
				tok.column = column;
				tok.type = PhFileReader.ERROR;
				tok.integer = PhFileReader.UNTERMINATED_BINARY;
				return;
			}
			if (s == (byte)'\n') {
				line++;
				column = 0;
			} else if (s == PhFileReader.QUOTATION_MARK) {
				tok.type = PhFileReader.BINARY;
				tok.length = index + 1;
				advance();
				return;
			} else {
				index++;
				if (index >= tok.string.length) {
					tok.enlarge();
				}
				tok.string[index] = s;
			}
			advance();
		}
	}


	private void check_logical(Token tok) throws java.io.IOException
	{
		ident_in_enumeration(tok);
		if (tok.type == PhFileReader.ERROR) {
			return;
		}
		if (tok.length == 1) {
			tok.type = PhFileReader.LOGICAL;
			switch (tok.string[0]) {
					case 'T':
							tok.integer = 1;
							break;
					case 'F':
							tok.integer = 0;
							break;
					case 'U':
							tok.integer = 2;
							break;
					default:break;
			}
		}
	}


	private void get_instance_name(Token tok) throws java.io.IOException
	{
		int zero = 0;
		tok.line = line;
		tok.column = column;
		long result = 0;
		advance();
		while ('0' <= s && s <= '9') {
			result *= 10;
			result += Character.digit( (char)s, 10 );
			if (s > '0') {
				zero++;
			}
			advance();
		}
		if (result == 0) {
//			tok.type = PhFileReader.ERROR;
//			tok.integer = PhFileReader.ZERO_INSTANCE_IDENTIFIER;
			tok.type = PhFileReader.INSTANCE_NAME;
			tok.integer = (int)result;
		} else if (zero == 0) {
			tok.type = PhFileReader.ERROR;
			tok.integer = PhFileReader.INCORRECT_INSTANCE_IDENTIFIER;
		} else {
			tok.type = PhFileReader.INSTANCE_NAME;
			tok.integer = (int)result;
		}
	}


	private int comment() throws java.io.IOException {
		int mark = -1;
		int repeat = 1;

		while (true) {
			if (repeat == 1) {
				advance();
			}
			if (s == (byte)'\n') {
				line++;
				column = 0;
			}
			repeat = 1;
			if (s == PhFileReader.EOF) {
				return -1;
			} else if (s == PhFileReader.ASTERISK) {
				advance();
				if (s == PhFileReader.SLASH_b) {
					return 0;
				} else {
					repeat = 0;
				}
			} else if (s == PhFileReader.SLASH_b) {
				advance();
				if (s == PhFileReader.ASTERISK) {
					mark = comment();
					if (mark < 0) {
						return -1;
					}
				}	else {
					repeat = 0;
				}
			}
		}
	}


	private int give_comment(Token tok) throws java.io.IOException {
		int count = 0;
		int repeat = 1;
		
		tok.integer = 0;
		while (true) {
			if (repeat == 1) {
				advance();
			}
			repeat = 1;
			if (s == (byte)'\n') {
				line++;
				column = 0;
			} else if (s == PhFileReader.EOF) {
				return -1;
			} else if (s == PhFileReader.ASTERISK) {
				advance();
				if (s == PhFileReader.SLASH_b) {
					tok.length = count;
					return 0;
				} else {
					if (count + 2 > tok.string.length) {
						tok.enlarge();
					}
					tok.string[count++] = PhFileReader.ASTERISK;
					tok.string[count++] = s;
					repeat = 0;
				}
			} else if (s == PhFileReader.SLASH_b) {
				advance();
				if (s == PhFileReader.ASTERISK) {
					int mark = comment();
					tok.integer = -1;
					if (mark < 0) {
						return -1;
					}
				}	else {
					if (count + 2 > tok.string.length) {
						tok.enlarge();
					}
					tok.string[count++] = PhFileReader.SLASH_b;
					tok.string[count++] = s;
					repeat = 0;
				}
			} else {
				if (count + 1 > tok.string.length) {
					tok.enlarge();
				}
				tok.string[count++] = s;
			}
		}
	}


	private void power10(int degree)
	{
		real_for_power = 1.;
		for (int i = 0; i < degree; i++) {
			real_for_power *= 10.;
		}
	}


	void get_token(Token tok) throws java.io.IOException
	{
		int find;
			/* Blanks are skipped */
		skip_blanks();

			/*  If and switch */
		if ('A' <= s && s <= 'Z' || s == PhFileReader.UNDERSCORE) {
			ident(tok);
		} else if ('0' <= s && s <= '9' || s == PhFileReader.MINUS || s == PhFileReader.PLUS) {
			number(tok);
		} else switch (s) {
						case PhFileReader.EOF:
							tok.type = PhFileReader.EOF;
							tok.line = line;
							tok.column = column;
//							SdaiSession.println("SCANNER>         End of file reached. Lines: " + line);
							break;
						case PhFileReader.EXCLAMATION_MARK:
							ident_user_defined(tok);
							break;
						case PhFileReader.APOSTROPHE:
							get_string(tok);
							break;
						case PhFileReader.QUOTATION_MARK:
							get_binary(tok);
							break;
						case PhFileReader.DOT:
							check_logical(tok);
							break;
						case PhFileReader.SPECIAL:
							get_instance_name(tok);
							break;
						case PhFileReader.LEFT_PARENTHESIS:
							tok.type = PhFileReader.LPAREN;
							tok.line = line;
							tok.column = column;
							advance();
							break;
						case PhFileReader.ASTERISK:
							advance();
							if (s == PhFileReader.SLASH_b) {
								tok.line = line;
								tok.column = column;
								tok.type = PhFileReader.ERROR;
								tok.integer = PhFileReader.UNMATCHED_CLOSE_COMMENT;
							} else {
								tok.line = line;
								tok.column = column - 1;
								tok.type = PhFileReader.REDEFINE;
							}
							break;
						case PhFileReader.COMMA_b:
							tok.type = PhFileReader.COMMA;
							tok.line = line;
							tok.column = column;
							advance();
							break;
						case PhFileReader.EQUAL:
							tok.type = PhFileReader.EQUALS;
							tok.line = line;
							tok.column = column;
							advance();
							break;
						case PhFileReader.DOLLAR_SIGN:
							tok.type = PhFileReader.MISSING;
							tok.line = line;
							tok.column = column;
							advance();
							break;
						case PhFileReader.RIGHT_PARENTHESIS:
							tok.type = PhFileReader.RPAREN;
							tok.line = line;
							tok.column = column;
							advance();
							break;
						case PhFileReader.SEMICOLON_b:
							tok.type = PhFileReader.SEMICOLON;
							tok.line = line;
							tok.column = column;
							advance();
							break;
						case PhFileReader.SLASH_b:
							advance();
							if (s == PhFileReader.ASTERISK) {
								if (file_population_comments) {
									find = give_comment(tok);
								} else {
									find = comment();
								}
								if (find < 0) {
									tok.line = line;
									tok.column = column;
									tok.type = PhFileReader.ERROR;
									tok.integer = PhFileReader.UNTERMINATED_COMMENT;
								} else {
									advance();
									if (file_population_comments && tok.integer >= 0) {
										tok.type = PhFileReader.COMMENT;
										tok.line = line;
										tok.column = column;
									} else {
										get_token(tok);
									}
								}
							} else {
								tok.type = PhFileReader.SLASH;
								tok.line = line;
								tok.column = column - 1;
							}
							break;
						case PhFileReader.CONJUNCTION:
							advance();
							ident(tok);
							if (tok.type == PhFileReader.SCOPE) {
								tok.column--;
							} else {
								tok.type = PhFileReader.ERROR;
								tok.integer = PhFileReader.UNEXPECTED_CHARACTER;
							}
							break;
						default:
							tok.line = line;
							tok.column = column;
							tok.type = PhFileReader.ERROR;
							if ('a' <= s && s <= 'z') {
								tok.integer = PhFileReader.ILLEGAL_LOWERCASE;
							} else if (s == '%' || s == '@' || s == '^' || s == '~') {
								tok.integer = PhFileReader.UNEXPECTED_CHARACTER;
							} else {
								tok.integer = PhFileReader.UNMATCHED_INPUT;
							}
		}  /*  End of switch  */
//System.out.println("token in scanner="+tok.type);
	}  /*  End of get_token()  */


	final private void enlarge_digits() {
		int new_length = digits.length * 2;
		byte new_digits[] = new byte[new_length];
		System.arraycopy(digits, 0, new_digits, 0, digits.length);
		digits = new_digits;
	}


	private void printWarningToLogo(SdaiSession session) {
		int incr_line = line + 1;
		String base = AdditionalMessages.RD_WCHA + 
			SdaiSession.line_separator + "   Line: " + incr_line + 
			SdaiSession.line_separator + "   Column: " + column;
		try {
			if (session != null && session.logWriterSession != null) {
				session.printlnSession(base);
			} else {
				SdaiSession.println(base);
			}
		} catch (SdaiException ex){
		}
	}


}
