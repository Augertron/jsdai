
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

package jsdai.express_g.util.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.HashMap;

import org.eclipse.ui.console.MessageConsoleStream;


public class IsoDbTools {

	static HashMap hm_iso_numbers = null;

	static HashMap hm_iso_ids = null;
	static HashMap hm_part_names = null;
	static String last_iso_file_name = null;

	public static HashMap getIsoIds() {
		return hm_iso_ids;
	}  

	public static HashMap getPartNames() {
		return hm_part_names;
	}  
	
	public static void initIsoDb() {
		hm_iso_ids = null;
		hm_part_names = null;
		last_iso_file_name = null;
	}
	
	
	
	


	public static boolean readIsoIdsAndPartNamesOfSchemas(String iso_file_name, MessageConsoleStream constream) {


		final int TK_START             = 0;
		final int TK_SCHEMA_NAME       = 1;
		final int TK_COMMA_1           = 2;
		final int TK_ISO_ID            = 3;
		final int TK_COMMA_2           = 4;
		// final int TK_PART_NAME         = 5;
		// final int TK_COMMA_OTHER       = 6;
		// final int TK_WORD_OTHER        = 7;
		final int TK_SKIPPING_THE_LINE = 5;
		
		
		
		// hm_iso_ids = new HashMap();
		// hm_part_names = new HashMap();

	 // it was like this:  when a new exg is opened, the first next export after that reads the file again
	 // obviously, it is wrong, if several (more than one) exg files are opened simultaneously, and the export is done back and forth from different exg files
	
	 // so now we will remember the last used document_reference path (and therefore, its project), and if the project is different, then read the file again.
	 // also, perhaps we can remove the forcing of the new file reading when a new exg is opened, this is no longer necessary - but that is done in SdaiEditor, remove there (or not)

		if (hm_iso_ids == null) {
			hm_iso_ids = new HashMap();
			hm_part_names = new HashMap();
		} else {
        if (false) {
//			if (iso_file_name.equals(last_iso_file_name)) {
//System.out.println("document_reference - NOT READING");
				return true;
			} else {
				last_iso_file_name = iso_file_name;
				hm_iso_ids = new HashMap();
				hm_part_names = new HashMap();
			}
		}
//System.out.println("document_reference - READING");

		try {

			FileInputStream ins = new FileInputStream(iso_file_name);
			InputStreamReader isr = new InputStreamReader(ins);
			StreamTokenizer st = new StreamTokenizer(isr);

			st.eolIsSignificant(true);
			st.slashSlashComments(true);
			st.slashStarComments(false);

			// st.wordChars(0, 31); // these are Ctrl characters, leaving them alone
			// st.wordChars(32, 43);
			st.wordChars(32, 34);
			st.commentChar('#'); // 35
			//st.wordChars('#', '#');
			st.wordChars(36, 43);
			st.ordinaryChar(','); // 44
			st.wordChars(45, 255);

			int status = TK_START;
			
			String current_schema_name = null;
			String current_schema_iso_id = null;
			String current_schema_part_name = null;
			Object previous = null;

			while (st.ttype != StreamTokenizer.TT_EOF) {
				st.nextToken();

				if (status == TK_START) {
					if (st.ttype == StreamTokenizer.TT_WORD) {
						current_schema_name = st.sval.trim().toLowerCase();
						status = TK_SCHEMA_NAME;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// an empty line - ignore, skip it.
						// do absolutely nothing, status remains the same (TK_START) for the next line
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						break;
					} else {
						// something else (probably a comma) at the begining of the line is so wrong that the line is ignored and skipped, just like a comment line #......
						// but a warning is printed
						status = TK_SKIPPING_THE_LINE;
						if (st.ttype == ',') {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a comma - IGNORED");
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a number: " + st.nval + " - IGNORED");
						} else {
							// what can it be? impossible?
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " starts with not sure what: " + st.toString() + "  - IGNORED");
						}
					
					}
				} else
				if (status == TK_SCHEMA_NAME) {
					if (st.ttype == ',') {
						status = TK_COMMA_1;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// seems that there is nothing after the schema name - ignore, skip it, but print a warning
							constream.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " contains only the schema name - IGNORED");
							status = TK_START;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
							constream.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name - IGNORED");
							status = TK_START; // not really needed
							current_schema_name = null; // not really needed
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
							break;
					} else {
						// it is not even possible - if schema name is not followed by comma, then what can it be?
						if (st.ttype == StreamTokenizer.TT_WORD) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.sval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.nval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by not sure what: " + st.toString() + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
							current_schema_name = null;
						}
					}
				} else
				if (status == TK_COMMA_1) {
					if (st.ttype == StreamTokenizer.TT_WORD) {
						current_schema_iso_id = st.sval.trim();
						status = TK_ISO_ID;
					} else
					if (st.ttype == ',') {
						// the iso_id is absent, but perhaps part_name is present
						status = TK_COMMA_2;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// incomplete line, no iso_id nor part_name, print a warning
						constream.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " contains only the schema name and a comma - IGNORED");
						status = TK_START;
						current_schema_name = null;
						current_schema_iso_id = null;  // not really needed
						current_schema_part_name = null; // not really needed
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						// incomplete line, no iso_id nor part_name, print a warning
						constream.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name and a comma - IGNORED");
						status = TK_START; // not really needed
						current_schema_name = null; // not really needed
						current_schema_iso_id = null;  // not really needed
						current_schema_part_name = null; // not really needed
						break;
					} else {
						// this should not be even possible
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by " + st.nval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by not sure what: " + st.toString() + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						}
					}
				} else
				if (status == TK_ISO_ID) {
					if (st.ttype == ',') {
						status = TK_COMMA_2;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// iso_id present, part_name absent, ok, add it
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							constream.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (1) - schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: ABSENT");
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_START;
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						// iso_id present, part_name absent, ok, add it
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							constream.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (2)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: ABSENT");
						current_schema_name = null; // not really needed
						current_schema_iso_id = null; // not really needed
						current_schema_part_name = null; // not really needed
						status = TK_START; // not really needed
						break;
					} else {
						// something seriosly wrong here, should be not possible, but we can store the line assuming that part_name is absent, but print a warning
						if (st.ttype == StreamTokenizer.TT_WORD) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.sval + " - assumed part_name absent");
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.nval + " - assumed part_name absent");
						} else {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by not sure what: " + st.toString() + " - assumed part_name absent");
						}
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (3)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: ABSENT, the line ends BADLY");
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_SKIPPING_THE_LINE;
					}
				} else 
				if (status == TK_COMMA_2) {
					if (st.ttype == StreamTokenizer.TT_WORD) {
						current_schema_part_name = st.sval.trim();
						// we have now the complete info, can add now.
						if (current_schema_iso_id == null) {
							// iso_id is absent, part_name - present
							current_schema_iso_id = "";
						} 
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, current_schema_part_name); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (4)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						// status = TK_PART_NAME;
						status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_PART_NAME as well
					} else
					if (st.ttype == ',') {
						// the part_name is absent, but perhaps the iso_id part is present
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								constream.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (5)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name is ABSENT ");
						} else {
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						// status = TK_COMMA_3;
						status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_COMMA_3 as well
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// the part_name is absent, but perhaps the iso_id part is present
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								constream.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (6)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						} else {
							
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							constream.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_START;
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						// the part_name is absent, but perhaps the iso_id part is present
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								constream.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (7)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						} else {
							
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							constream.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null; // not really needed
						current_schema_iso_id = null; // not really needed
						current_schema_part_name = null; // not really needed
						status = TK_START; // not really needed
						break;
					} else {
						// this should not happen, but just in case
						// whatever happened, part_name is absent, add the line
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by " + st.nval + " - assumed part_name absent");
						} else {
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by not sure what: " + st.toString() + " - assumed part_name absent");
						}
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								constream.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (8)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name is ABSENT ");
						} else {
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							constream.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_COMMA_3 as well
					}
				} else
				if (status == TK_SKIPPING_THE_LINE) {
					if (st.ttype == StreamTokenizer.TT_EOL) {
						status = TK_START;
						current_schema_name = null; // should not be needed
						current_schema_iso_id = null;  // should not be needed
						current_schema_part_name = null; // should not be needed
					} else 
					if (st.ttype == StreamTokenizer.TT_EOF) {
						status = TK_START; // not really needed
						current_schema_name = null; // should not be needed
						current_schema_iso_id = null;  // should not be needed
						current_schema_part_name = null; // should not be needed
						break;
					} 
				} else {
					status = TK_START;
					current_schema_name = null; 
					current_schema_iso_id = null;  
					current_schema_part_name = null; 
					if (st.ttype == StreamTokenizer.TT_WORD) {
						constream.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: " + st.sval);
					} else
					if (st.ttype == StreamTokenizer.TT_NUMBER) {
						constream.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: " + st.nval);
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						constream.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - something completely wrong with this line, ignored, current token: EOL");
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						constream.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: EOF");
						break;
					} else
					if (st.ttype == ',') {
						constream.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: comma");
					}
				}
				 
			} // while
      return true;
		} // try
		catch (IOException e) {
			// no schema - no iso numbers
			constream.println("ISO_DB WARNING! - file not found: " + iso_file_name);
			hm_iso_ids = null;
			hm_part_names = null;
		
			return false;
	 }
	}



	public static void readIsoNumbersOfSchemas_old(String iso_file_name, MessageConsoleStream constream) {

		final int TK_START_LINE = 0;
		final int TK_LONG = 1;
		final int TK_SHORT = 2;
		final int TK_COMMA = 3;

		if (hm_iso_numbers == null) {
			hm_iso_numbers = new HashMap();
		} else {
			return;
		}
		try {
			FileInputStream ins = new FileInputStream(iso_file_name);
			InputStreamReader isr = new InputStreamReader(ins);
			StreamTokenizer st = new StreamTokenizer(isr);
			st.eolIsSignificant(true);
			st.wordChars('_', '_');
			st.wordChars(' ', ' ');
			st.wordChars('-', '-');
			st.wordChars('/', '/');
			st.wordChars('?', '?');
			st.ordinaryChar(',');
			st.commentChar('#');

			int status = TK_START_LINE;
			String current_schema_name = null;
			String current_schema_iso_number = null;

			while (st.ttype != StreamTokenizer.TT_EOF) {
				st.nextToken();
//System.out.println("<<>><> status: " + status + ", type: " + st.ttype + ", value: " + st.sval);
				if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_WORD)) {
					current_schema_name = st.sval.toLowerCase();
					status = TK_LONG;
				} else if ((status == TK_LONG) && (st.ttype == ',')) {
					status = TK_COMMA;
				} else if ((status == TK_COMMA) && (st.ttype == StreamTokenizer.TT_WORD)) {
					current_schema_iso_number = st.sval;
					status = TK_SHORT;
					hm_iso_numbers.put(current_schema_name, current_schema_iso_number);
//System.out.println("<<>> schema: " + current_schema_name + ", number: \"" +  current_schema_iso_number + "\"");        
				} else if (((status == TK_SHORT) && (st.ttype == StreamTokenizer.TT_EOL)) || 
											 (st.ttype == StreamTokenizer.TT_EOF)) {
					// current reading completed. Now, use it.
					if (st.ttype == StreamTokenizer.TT_EOF) {
						status = TK_START_LINE;

						break;
					} else if (st.ttype == StreamTokenizer.TT_EOL) {
						// ok, next complex entity
						status = TK_START_LINE;
					}
				} else if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_EOL)) {
				} else {
//          System.out.println("ERROR in input file, line: " + st.lineno());
					constream.println("ERROR in document_reference.txt file, line: " + st.lineno());

					break;
				}
			}

		} // try
		catch (IOException e) {
			// no schema - no iso numbers
			return;
	 }
	}


}