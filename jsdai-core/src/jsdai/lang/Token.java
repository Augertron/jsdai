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

package jsdai.lang; import jsdai.dictionary.*;


class Token
{
	int    type;
	int    integer;
	double real;
	byte[] string;
	int    length;
	int    line;
	int    column;

	static final int LENGTH_OF_STRING_TOKEN = 128;


	Token() {
		string = new byte[LENGTH_OF_STRING_TOKEN];
	}



	void enlarge() {
		int new_length = string.length * 2;
		byte new_string[] = new byte[new_length];
		System.arraycopy(string, 0, new_string, 0, string.length);
		string = new_string;
	}


	void PrintToken() {
		int i;
		String str_type;

		switch (type) {
				case PhFileReader.EOF:
					str_type = "eof";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.ERROR:
					str_type = "error";
					System.out.println("SCANNER>  " + str_type + " error_index=" + integer +
						" line=" + line + " column=" + column);
					break;
				case PhFileReader.BINARY:
					str_type = "BINARY";
					System.out.print("SCANNER>  " + str_type + " value=");
					for (i = 0; i < length; i++) {
						System.out.print( (char)string[i]);
					}
					System.out.println(" line=" + line + " column=" + column);
					break;
				case PhFileReader.COMMA:
					str_type = "COMMA";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.DATA:
					str_type = "DATA";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.ENDSCOPE:
					str_type = "ENDSCOPE";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.ENDSEC:
					str_type = "ENDSEC";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.ISO_END:
					str_type = "END_ISO_10303_21";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.ENTITY_NAME:
					str_type = "ENTITY_NAME";
					System.out.print("SCANNER>  " + str_type + " name=");
					for (i = 0; i < length; i++) {
						System.out.print( (char)string[i]);
					}
					System.out.println(" line=" + line + " column=" + column);
					break;
				case PhFileReader.ENUM:
					str_type = "ENUM";
					System.out.print("SCANNER>  " + str_type + " value=");
					for (i = 0; i < length; i++) {
						System.out.print( (char)string[i]);
					}
					System.out.println(" line=" + line + " column=" + column);
					break;
				case PhFileReader.EQUALS:
					str_type = "EQUALS";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.HEADER:
					str_type = "HEADER";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.INSTANCE_NAME:
					str_type = "INSTANCE_NAME";
					System.out.println("SCANNER>  " + str_type + " instance=" + integer +
						" line=" + line + " column=" + column);
					break;
				case PhFileReader.INTEGER:
					str_type = "INTEGER";
					System.out.println("SCANNER>  " + str_type + " value=" + integer +
						" line=" + line + " column=" + column);
					break;
				case PhFileReader.ISO_BEGIN:
					str_type = "ISO_10303_21";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.LPAREN:
					str_type = "LEFT_PAREN";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.LOGICAL:
					str_type = "LOGICAL";
					System.out.println("SCANNER>  " + str_type + " value=" + integer +
						" line=" + line + " column=" + column);
					break;
				case PhFileReader.MISSING:
					str_type = "MISSING";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.REAL:
					str_type = "REAL";
					System.out.println("SCANNER>  " + str_type + " value=" + real +
						" line=" + line + " column=" + column);
					break;
				case PhFileReader.REDEFINE:
					str_type = "REDEFINE";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.RPAREN:
					str_type = "RIGHT_PAREN";
					System.out.println("SCANNER>  " + str_type +" line=" + line + " column=" + column);
					break;
				case PhFileReader.SCOPE:
					str_type = "SCOPE";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.SEMICOLON:
					str_type = "SEMICOLON";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.SLASH:
					str_type = "SLASH";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
				case PhFileReader.STRING:
					str_type = "STRING";
					System.out.print("SCANNER>  " + str_type + " value=");
					for (i = 0; i < length; i++) {
						System.out.print( (char)string[i]);
					}
					System.out.println(" line=" + line + " column=" + column);
					break;
				case PhFileReader.USER_DEFINED_KEYWORD:
					str_type = "USER_DEFINED_KEYWORD";
					System.out.print("SCANNER>  " + str_type + " name=");
					for (i = 0; i < length; i++) {
						System.out.print( (char)string[i]);
					}
					System.out.println(" line=" + line + " column=" + column);
					break;
				default: str_type = "Unknown token";
					System.out.println("SCANNER>  " + str_type + " line=" + line + " column=" + column);
					break;
		}
	}
}

