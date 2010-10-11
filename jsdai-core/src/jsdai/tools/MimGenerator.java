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

/* Copyright (C) 2004 LKSoftWare GmbH */

package jsdai.tools;

import java.util.*;
import java.io.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.util.*;


public class MimGenerator {


	static String mim_express_file = "mim_schemas.exp";
	static boolean flag_help = false;
	static final String DICT_MOD_SUFIX = "_DICTIONARY_DATA";
	static HashMap hm_iso_numbers = null;
	static HashMap hm_iso_ids = null;
	static HashMap hm_part_names = null;
	static String iso_file = "iso_db";

	public static final void main(String args[]) throws SdaiException, java.io.IOException {

			String location = "ExpressCompilerRepo";

			System.out.println("");
			System.out.println("JSDAI(TM) MIM Generator,   Copyright (C) 2004 LKSoftWare GmbH");
			System.out.println("---------------------------------------------------------------------------");
		
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-help")) flag_help = true;
				if (args[i].equalsIgnoreCase("-?")) flag_help = true;
				if (args[i].equalsIgnoreCase("-location")) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A repository path or name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A repository path or name must follow the " + args[i-1] + " switch");
						return;
					}
					location = args[i];
				}
				if (args[i].equalsIgnoreCase("-output")) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A path or name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A path or name must follow the " + args[i-1] + " switch");
						return;
					}
					mim_express_file = args[i];
				}
			}

			if (flag_help) {
				System.out.println("USAGE:\n");
				System.out.println("java jsdai.tools.MimGenerator -location name|path]");
				System.out.println("\ncommand line switches:\n");
				System.out.println("-location name|path");
				System.out.println("\tthe name of or the path to the repository to be checked");
				System.out.println("\t\tdefault: ExpressCompilerRepo, location specified in jsdai.properties");
				System.out.println("-output name|path");
				System.out.println("\tthe name of or the path of the express file to be generated");
				System.out.println("\t\tdefault: mim_schemas.exp in the current directory");
				return;
			}
	
			MimGenerator mg = new MimGenerator();
			SdaiSession session = SdaiSession.openSession();
			SdaiTransaction trans = session.startTransactionReadOnlyAccess();
      mg.run(SimpleOperations.linkRepositoryOrName("ExpressCompilerRepo", location));
      session.closeSession();
			
	
	}
	
	
	
	public static void generateMimExpress(SchemaInstance sch_inst, String file_name) throws SdaiException {

		File fiso = new File(iso_file);
		if (fiso.exists()) {
			// readIsoNumbersOfSchemas(iso_file);
			readIsoIdsAndPartNamesOfSchemas(iso_file);
		}
		SdaiRepository repo = sch_inst.getRepository();
//		SdaiModel work = repo.createSdaiModel("working", SExtended_dictionary_schema.class);
		SdaiSession session = repo.getSession();
		String searched_name = sch_inst.getName().toUpperCase() + DICT_MOD_SUFIX;
		ASdaiModel assoc_mods = sch_inst.getAssociatedModels();
		SdaiIterator iter_assoc = assoc_mods.createIterator();
		SdaiModel model = null;
		while (iter_assoc.next()) {
			SdaiModel mod = (SdaiModel)assoc_mods.getCurrentMember(iter_assoc);
			if (mod.getName().equals(searched_name)) {
				model = mod;
				break;
			}
		}
		if (model != null) {
			if (model.getMode() == SdaiModel.NO_ACCESS) {
				model.startReadOnlyAccess();
			}
			MimGenerator mg = new MimGenerator();
//				mg.printModelSchema(model, session, repo, work, file_name);
				mg.printModelSchema(model, file_name);
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
//		work.deleteSdaiModel();
	}

//	private void printModelSchema(SdaiModel model, SdaiSession session, SdaiRepository repo, SdaiModel work, String file_name) throws SdaiException {
	private void printModelSchema(SdaiModel model, String file_name) throws SdaiException {

	/* let's try to read the first line of the header from the file */
	
	String line = null;
	String first_header_line = null;
	String ident = "$" + "I" + "d" + ":";

  try {

		File aFile = new File(file_name); 
		BufferedReader input = new BufferedReader(new FileReader(aFile));

		for (;;) {
			line = input.readLine();
			if (line == null) break;
			if (line.trim().startsWith(ident)) { // found what we need
				first_header_line = line;
				break;
			}
		}
		input.close();
	} catch (Exception ex) {
//      ex.printStackTrace();
		// do nothing here, if the file does not exist or does not have this line, use my constructed first line
  }
	

// System.out.println("model: " + model + ", file: " + file_name);
    PrintWriter pw = getPrintWriter(file_name);
 //System.out.println("pw: " + pw);
		ESchema_definition sd = getSchema_definitionFromModel(model);
		String name = sd.getName(null);
		name = name.substring(0, name.length()-4) + "_mim";

		// print header info such as this:

		String str_date_and_time = get_time(); 
		String name_with_spaces = name.substring(0, name.length()-4).replace('_', ' ');
		String iso_number = "";
		if (hm_iso_ids != null) {
			iso_number = (String)hm_iso_ids.get(sd.getName(null).toLowerCase());
			if (iso_number == null) {
				iso_number = "ISO/CD-TS 10303-xxxx";
			}
		}
		
		String version_nr = "1.2";
//		String author_name = "budreckytej";
		String author_name = "nobody";
		

		if (first_header_line == null) {
			first_header_line =  "   " + ident + " mim.exp,v " + version_nr + " " + str_date_and_time + " " + author_name + " Exp " + "$"; 
		}

		pw.println("(*");
		pw.println(first_header_line);
		pw.println("   ISO TC184/SC4/WG12 N - " + iso_number + " " + name_with_spaces + " - EXPRESS MIM");

		pw.println("*)\n\n");

		pw.println("SCHEMA " + name + ";");


		AInterface_specification specifications = (AInterface_specification)model.getInstances(CInterface_specification.class);
    EInterface_specification[] specifications_set = new EInterface_specification[specifications.getMemberCount()];
		SdaiIterator iterator = specifications.createIterator();
		int iSSetCount = 0;
		while (iterator.next()) {
      EInterface_specification specification = (EInterface_specification) specifications.getCurrentMemberObject(iterator);
      specifications_set[iSSetCount]=specification;
      iSSetCount++;
		}
		Arrays.sort(specifications_set, new SpecSorterBySchema());
//    Arrays.sort(specifications_set, SpecSorterBySchema());

    for (int i=0; i<iSSetCount; i++) { 
	    EInterface_specification specification = (EInterface_specification)specifications_set[i];
      ESchema_definition interfaced_schema = (ESchema_definition)specification.getForeign_schema(null);
			name = interfaced_schema.getName(null);
			name = name.substring(0, name.length()-4) + "_mim";
			String iso_name = (String)hm_iso_ids.get(interfaced_schema.getName(null).toLowerCase());
			if (iso_name == null) {
				iso_name = "";
			} else {
				iso_name = "\t-- " + iso_name;
			}
			if (specification instanceof EUse_from_specification) {
				pw.println("\tUSE FROM " + name + ";" + iso_name);
			} else {
				pw.println("\tREFERENCE FROM " + name + ";" + iso_name);
			}
		}
		pw.println("END_SCHEMA;\n");
    pw.flush();
    pw.close();
		
	}
		

	void run(SdaiRepository repo) throws SdaiException, java.io.IOException {
		repo.openRepository();
//		repo.exportClearTextEncoding("_KUKU3.pf");
		System.out.println();

    PrintWriter pw = getPrintWriter(mim_express_file);

		ASdaiModel models = repo.getModels();
		SdaiIterator iter = models.createIterator();
    int count = 0;
		HashSet interfaced_schemas = new HashSet();
    
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			String name = model.getName();
			if (name.endsWith("_ARM_DICTIONARY_DATA")) {
	      if (model.getMode() == SdaiModel.NO_ACCESS) {
        	model.startReadOnlyAccess();
	      }
				// may not need it
				ESchema_definition sd = getSchema_definitionFromModel(model);
				name = sd.getName(null);
				name = name.substring(0, name.length()-4) + "_mim";
				pw.println("SCHEMA " + name + ";");
				printSchema(model, pw);
				pw.println("END_SCHEMA;\n");
			}
		} 
	
    pw.flush();
    pw.close();
		System.out.println("Mim Generator finished");
	
	}

	void printSchema(SdaiModel model, PrintWriter pw) throws SdaiException {
			
		AInterface_specification specifications = (AInterface_specification)model.getInstances(CInterface_specification.class);
    EInterface_specification[] specifications_set = new EInterface_specification[specifications.getMemberCount()];
		SdaiIterator iterator = specifications.createIterator();
		int iSSetCount = 0;
		while (iterator.next()) {
      EInterface_specification specification = (EInterface_specification) specifications.getCurrentMemberObject(iterator);
      specifications_set[iSSetCount]=specification;
      iSSetCount++;
		}
    Arrays.sort(specifications_set, new SpecSorterBySchema());

    for (int i=0; i<iSSetCount; i++) { 
	    EInterface_specification specification = (EInterface_specification)specifications_set[i];
      ESchema_definition interfaced_schema = (ESchema_definition)specification.getForeign_schema(null);
			String name = interfaced_schema.getName(null);
			name = name.substring(0, name.length()-4) + "_mim";
			if (specification instanceof EUse_from_specification) {
				pw.println("\tUSE FROM " + name + ";");
			} else {
				pw.println("\tREFERENCE FROM " + name + ";");
			}
		}
		
	}

	void printSchema_not_sorted(SdaiModel model, PrintWriter pw) throws SdaiException {
			
		AInterface_specification specifications = (AInterface_specification)model.getInstances(CInterface_specification.class);
		SdaiIterator iterator = specifications.createIterator();
		while (iterator.next()) {
      EInterface_specification specification = (EInterface_specification) specifications.getCurrentMemberObject(iterator);
      ESchema_definition interfaced_schema = (ESchema_definition)specification.getForeign_schema(null);
			String name = interfaced_schema.getName(null);
			name = name.substring(0, name.length()-4) + "_mim";
			if (specification instanceof EUse_from_specification) {
				pw.println("\tUSE FROM " + name + ";");
			} else {
				pw.println("\tREFERENCE FROM " + name + ";");
			}
		}
		
	}
	
  	ESchema_definition getSchema_definitionFromModel(SdaiModel sm)
                                                   throws SdaiException {
    if (sm == null) return null;
    Aggregate ia = sm.getEntityExtentInstances(ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    if (iter_inst.next()) {
      ESchema_definition inst = (ESchema_definition) ia.getCurrentMemberObject(iter_inst);

      return inst;
    }

    return null;
  }

  PrintWriter getPrintWriter(String file_name) { 
//		if (output_directory != null) {
//			file_name = output_directory + File.separator + file_name;
//		}
try {
    FileOutputStream fos = new FileOutputStream(file_name);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);
    return pw;
	} catch (java.io.IOException exc) {
				exc.printStackTrace(System.err);
				return null;
	}

 }


  String get_time() {
    GregorianCalendar cal = new GregorianCalendar();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    boolean month_one_digit;

    if (month < 10) {
      month_one_digit = true;
    } else {
      month_one_digit = false;
    }

    int day = cal.get(Calendar.DAY_OF_MONTH);
    boolean day_one_digit;

    if (day < 10) {
      day_one_digit = true;
    } else {
      day_one_digit = false;
    }

    int hour = cal.get(Calendar.HOUR_OF_DAY);
    boolean hour_one_digit;

    if (hour < 10) {
      hour_one_digit = true;
    } else {
      hour_one_digit = false;
    }

    int minute = cal.get(Calendar.MINUTE);
    boolean minute_one_digit;

    if (minute < 10) {
      minute_one_digit = true;
    } else {
      minute_one_digit = false;
    }

    int second = cal.get(Calendar.SECOND);
    boolean second_one_digit;

    if (second < 10) {
      second_one_digit = true;
    } else {
      second_one_digit = false;
    }

    String time_stamp = year + "/";

    if (month_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + month + "/";

    if (day_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + day + " ";

    if (hour_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + hour + ":";

    if (minute_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + minute + ":";

    if (second_one_digit) {
      time_stamp = time_stamp + "0";
    }

    return time_stamp + second;
  }


	static void readIsoNumbersOfSchemas(String iso_file_name) {
		
		// removing unnecessary caching, just in case
		// if (hm_iso_numbers != null) return;

  	final int TK_START_LINE = 0;
  	final int TK_LONG = 1;
  	final int TK_SHORT = 2;
  	final int TK_COMMA = 3;

		hm_iso_numbers = new HashMap();

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
//System.out.println("<<>> schema: " + current_schema_name + ", number: " +  current_schema_iso_number);       	
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
          System.out.println("ERROR in input file, line: " + st.lineno());

          break;
        }
      }

    } // try
    catch (IOException e) {
			// no schema - no iso numbers
      return;
   }
  }


//	public static boolean readIsoIdsAndPartNamesOfSchemas(String iso_file_name, MessageConsoleStream constream) {
	static boolean readIsoIdsAndPartNamesOfSchemas(String iso_file_name) {


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

/*		
		if (hm_iso_ids == null) {
			hm_iso_ids = new HashMap();
			hm_part_names = new HashMap();
		} else {
			return true;
		}
*/

		hm_iso_ids = new HashMap();
		hm_part_names = new HashMap();

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
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a comma - IGNORED");
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a number: " + st.nval + " - IGNORED");
						} else {
							// what can it be? impossible?
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with not sure what: " + st.toString() + "  - IGNORED");
						}
					
					}
				} else
				if (status == TK_SCHEMA_NAME) {
					if (st.ttype == ',') {
						status = TK_COMMA_1;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// seems that there is nothing after the schema name - ignore, skip it, but print a warning
							System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " contains only the schema name - IGNORED");
							status = TK_START;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
							System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name - IGNORED");
							status = TK_START; // not really needed
							current_schema_name = null; // not really needed
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
							break;
					} else {
						// it is not even possible - if schema name is not followed by comma, then what can it be?
						if (st.ttype == StreamTokenizer.TT_WORD) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.sval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.nval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by not sure what: " + st.toString() + " - IGNORED");
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
						System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " contains only the schema name and a comma - IGNORED");
						status = TK_START;
						current_schema_name = null;
						current_schema_iso_id = null;  // not really needed
						current_schema_part_name = null; // not really needed
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						// incomplete line, no iso_id nor part_name, print a warning
						System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name and a comma - IGNORED");
						status = TK_START; // not really needed
						current_schema_name = null; // not really needed
						current_schema_iso_id = null;  // not really needed
						current_schema_part_name = null; // not really needed
						break;
					} else {
						// this should not be even possible
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by " + st.nval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by not sure what: " + st.toString() + " - IGNORED");
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
							System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
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
							System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
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
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.sval + " - assumed part_name absent");
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.nval + " - assumed part_name absent");
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by not sure what: " + st.toString() + " - assumed part_name absent");
						}
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
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
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
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
								System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (5)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name is ABSENT ");
						} else {
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
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
								System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (6)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						} else {
							
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - both iso_id and part_name are absent, the line is ignored");
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
								System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (7)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						} else {
							
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
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
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by " + st.nval + " - assumed part_name absent");
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by not sure what: " + st.toString() + " - assumed part_name absent");
						}
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (8)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name is ABSENT ");
						} else {
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
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
						System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: " + st.sval);
					} else
					if (st.ttype == StreamTokenizer.TT_NUMBER) {
						System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: " + st.nval);
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - something completely wrong with this line, ignored, current token: EOL");
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: EOF");
						break;
					} else
					if (st.ttype == ',') {
						System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: comma");
					}
				}
				 
			} // while
      return true;
		} // try
		catch (IOException e) {
			// no schema - no iso numbers
			//System.out.println("ISO_DB WARNING! - file not found: " + iso_file_name);
			hm_iso_ids = null;
			hm_part_names = null;
		
			return false;
	 }
	}


	class SpecSorterBySchema implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
		    EInterface_specification e1 = (EInterface_specification)o1;
		    EInterface_specification e2 = (EInterface_specification)o2;

		    String e1_schemaName = e1.getForeign_schema(null).getName(null);
		    String e2_schemaName = e2.getForeign_schema(null).getName(null);
			

		    return e1_schemaName.compareToIgnoreCase(e2_schemaName);
			}
			catch (SdaiException exc) {
				exc.printStackTrace(System.err);
				return 0;
		  }
	  }

	}
}
