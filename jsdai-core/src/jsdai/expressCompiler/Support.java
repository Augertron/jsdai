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
import java.io.InputStream;
import java.net.URL;
import java.util.regex.*;
import java.util.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;


/*

using HashMap for entity declaration in each schema - key is the name of entity,
can be found in any schema, local or interfaced

to find this HashMap, the global HashMap is used for HashMaps of each schema

HashMap hm_entity_diclarations
HashMap hm_current_entity_declarations

*/


public class Support {
	
	static Compiler2 x_parser;
	static boolean expression_instances = false;
	static boolean flag_type_tracking = false;
	static X_AllSchemas x_tree = null;
  static Vector compiled_models = null;
	static String model_file_name = null;
	static  String express_file = null;
	static Vector express_files = null;
	static Vector global_excluded = null;

	static Matcher specialClassSuffixMatcher = null;

	static EGeneric_schema_definition sd;
	static String output_dir = null;

	static HashMap hm_variables;
	static HashMap hm_current_variables;
	static HashMap hm_parameters;
	static HashMap hm_current_parameters;
	static HashMap hm_entity_declarations;
	static HashMap hm_current_entity_declarations;
	static HashMap hm_type_declarations;
	static HashMap hm_current_type_declarations;
	static HashMap hm_function_declarations;
	static HashMap hm_current_function_declarations;
	static HashMap hm_procedure_declarations;
	static HashMap hm_current_procedure_declarations;
	static HashMap hm_rule_declarations;
	static HashMap hm_current_rule_declarations;
	static HashMap hm_constant_declarations;
	static HashMap hm_current_constant_declarations;
	static HashMap hm_subtype_constraint_declarations;
	static HashMap hm_current_subtype_constraint_declarations;
	static HashMap hm_attributes;
	static EEntity_definition global_supertype_entity;


	/*
		using those to contain Hashsets for each interfaced schema, for detecting duplicate interfacing DIRECTLY into the same schemma
		part 11 does not prohibit duplicate interfac (? to check more thoroughly) but perhaps it is better to warn the user (not sure about error, though)

		There are the following possible cases, where to use ERROR and where WARNING, and where nothing?

		1. a whole schema is interfaced with REFERENCE FROM and then (or perhaps in any order) separate entities and/or types with USE FROM
		NOTHING ?  perfectly legitimate reasons - want to override only for some named types
 
	  2. a whole schema is interfaced both with REFERENCE FROM and with USE FROM
 		NOTHING ? legitimate reasons - if one wants all named types to be interfaced with USE FROM but also wants all functions, procedures, constants  

	  3. a whole schema is interfaced with USE FROM and then separate constants, functions, procedures with REFERENCE FROM  
		NOTHING - perfectly legitimate

	  4. a whole schema is interfaced with REFERENCE FROM and then separate items ALSO with REFERENCE FROM
		WARNING?

	  5. a whole schema is interfaced with USE FROM and then separate entities and/or types  with REFERENCE FROM  
		WARNING?

	  6. a whole schema is interfaced with USE FROM and then separate entities and/or types also with USE FROM  
		WARNING?

		7. a separate entity or types is interfaced BOTH with REFERENCE FROM and with USE FROM (even if in the overriding sequence: REFERENCE FROM first, USE FROM last - still, the 1st of them not needed, and which one was not intended?)
		WARNING?

    8. a separate item is interfaced more than once with REFERENCE FROM
		WARNING?

    9. a separate entity or type is interfaced more than once with USE FROM
		WARNING?

   10. more than one REFERENCE FROM declaration used for separate items
  	NOTHING? - there was a warning request

	 11. more than one USE FROM declaration used for separate entities and/or types
  	NOTHING? - there was a warning request

	 12. Items ather than entities or types included in USE FROM
	 ERROR 

	 13. Items other than the allowed ones included in REFERENCE FROM  (explicitly excluded: schemas, global rules; explicitly included: types, entities, constants, functions, procedures; so what about subtype constraints ???)
	 ERROR

    NOTICE:
		10. & 11. are more controversial, in my opinion.
    do the really require a warning? 
    It could be: 
    - such a coding style; 
    - or some item(s) emphasized this way for better readability;
    - or emphasized because they are (exceptionally) given alias names with AS, again for better readability or not to forget;
    - or because they are added only temporarily;

    also, USE FROM - only entities and types
    REFERENCE FROM - entities, types + constants, functions, procedures
    not interfaced at all - rules, schemas, 


    NOTICE 2:  there might be an error in part 11 edition 2:  !!!!!!!!!!!!!!!!!!!!!!!!!
    
    11 says:
    
    "...the reference specification applies to all declarations except rules and schemas"

    but 11.2 says:
    
	   A reference specification enables the following EXPRESS items, declared in a foreign schema, to be visible in the current schema:

		— Constant;
		— Entity;
		— Function;
		— Procedure;
		— Type.
    
		THERE IS A CONTRADICTION BETWEEN THOSE TWO CLAUSES:  - Subtype constraints.
		According to 11 they are interfaced (not explicitly excluded), according to 11.2 - they are not (not explicitly included).   


    
	*/

	static HashMap hm_used_froms;
	static HashMap hm_referenced_froms; 
	static HashSet hm_used_froms_all;
	static HashSet hm_referenced_froms_all; 

	static boolean global_flag_for_expression_inside = false;

	static String global_express_dir_name = null;
	static String global_entity_ref = null;
	static String global_entity_name = null;
	static String global_schema_name = null;
	static String hm_attribute_key = null;
	static boolean flag_no_special_reference_from = true;
	static boolean flag_implicit_expressions = false;
	static boolean flag_relative_exclude = false;
	static boolean flag_stepmod = false;
	static boolean flag_arm = false;
	static boolean flag_mim = false;
  static boolean no_express_amendment = false;
  static boolean flag_serialize = false;
  static boolean express_x = false;	
  static int parser_pass;
  static boolean first_parsed_file;
  static String global_name1_global;
  static int global_kind1_global;
  static String global_name2_global;
  static int global_kind2_global;
  static String global_name3_global;
  static int global_kind3_global;
	static boolean flag_replace_schema = false; 
  static boolean flag_xt_declarations = false; // if true, declarations are generated for standard express types, local and implicit data_type_declarations
  static boolean flag_xt_sdai = true; // if true, standard express types are generated in sdai_dictionary_schema instead of in extended_dictionary_schema
  static boolean flag_xt_init_sdai = true; // if true, standard express types are generated in sdai_dictionary_schema instead of in extended_dictionary_schema
  static boolean flag_stack = false;
  static boolean flag_scope = false;
  static boolean flag_scope_stack = false;
  static boolean flag_verbose0 = false;
  static boolean flag_verbose = false;
  static boolean flag_debug = false;
  static boolean flag_error_debug = false;
  static boolean flag_deep_debug = false;
	static boolean flag_debug_split_return = false;
	static boolean flag_no_print_active_nodes = false;
  static boolean flag_oc = true;
	static boolean flag_more_errors = false;
  static boolean flag_complex = false;
  static boolean flag_complex_off = false;
  static boolean flag_implicit_select = false;
	static boolean flag_original_expressions = false;
	static boolean flag_formatted_1 = false; // possibly will not be used at this time
	static int flag_format_level = 1;
	static boolean flag_really_original_expressions = true;
  static boolean flag_eof = false;
	
	static boolean flag_hard_supertype_error = true;


  static Stack scope_stack = new Stack();
  static Vector current_scope;
  static int variable_uid = 0;
  static Stack variable_id_stack = new Stack();
  static Stack argument_stack = new Stack();
  static Stack type_stack = new Stack();
  static Stack expression_stack = new Stack();
  static EEntity active_scope = null;
	static ECtScope active_scope_extension = null;
  static String active_scope_string = null;
  static Vector used_vectors = new Vector();
  static Vector referenced_vectors = new Vector();
  static Vector model_vector = new Vector();
  static Integer FLAG_USED = new Integer(1);
  static Integer FLAG_REFERENCED = new Integer(2);
  static Integer FLAG_IMPLICIT = new Integer(0);
  static Integer FLAG_KEEP_OUT = new Integer(-1);
  static SdaiSession session = null;
  static SdaiRepository repository;
  static SdaiRepository sysrepository;
  static jsdai.dictionary.ESchema_definition dic_sd;
  static SdaiModel model = null;
  static SdaiModel expression_model = null;
  static EInteger_type _st_integer;
  static EReal_type _st_real;
  static ENumber_type _st_number;
  static ELogical_type _st_logical;
  static EBoolean_type _st_boolean;
  static EString_type _st_string;
  static EBinary_type _st_binary;
  static EData_type _st_generic;

  // static EEntity_definition _st_entity;
  static EData_type _st_entity;
  static EDeclaration _std_entity;
  static EList_type _st_list_string;
  static EList_type _st_list_generic;
  static ESet_type _st_set_generic;
  static ESet_type _st_set_string;
  static EBag_type _st_bag_generic;
  static EAggregation_type _st_aggregate_generic;
  static ESchema_definition _st_schema;
  static SdaiModel _st_model;
  static EDeclaration _std_integer;
  static EDeclaration _std_real;
  static EDeclaration _std_number;
  static EDeclaration _std_logical;
  static EDeclaration _std_boolean;
  static EDeclaration _std_string;
  static EDeclaration _std_binary;
  static EDeclaration _std_generic;
  static EDeclaration _std_list_string;
  static EDeclaration _std_list_generic;
  static EDeclaration _std_set_generic;
  static EDeclaration _std_set_string;
  static EDeclaration _std_bag_generic;
  static EDeclaration _std_aggregate_generic;
  static int the_depth;
	static int schema_depth;
	static HashSet hs_redeclared_attribute_originals;

	static int global_sizeof_restriction_value;
  static boolean global_is_spec_error_sizeof;
	static boolean global_is_spec_warning_sizeof;
	static boolean global_is_outer_sizeof;



/*
	  String literals are saved with double quotes at both ends in the string itself:
	  that is not  geometry_schema but "geometry_schema"
	  So, possible combinations are as follows:
	  a) "geometry_schema.cartesian_point"
	  b) "geometry_schema" + ".cartesian_point" 
	  c) "geometry_schema." + "cartesian_point"
	  d) "geometry_schema" + "." + "cartesian_point"
	  e) variable + ".cartesian_point"
	  f) or perhaps variable + "cartesian_point" where variable includes  the dot
	  
	  So, what is needed to be done:
	  
	  1. "geometry_schema" to replace by "*" - covers b), d) and e) 
	  2. "geometry_schema." to replace by "*." - covers a), c), f)
	  3. "geometry_schema.cartesian_point" to replace by "*.cartesian_point" - the same as 2.
	  
	  "kuku"
	  012345
	  length = 6
	  substring(1,5)
	  
	  So, if dot is not present, then use substring(1, length-1)

	  if replacement is needed,
	  then construct the result in the folowing way:
	  "*"  - discuss this with Gintaras perhaps? 
	  
	  
	  "kuku.bum"
	  0123456789
	            
	  dot index = 5
	  so use substring (1,5)          
	            
	            
	  if dot is present, then use substring(1,dot_index)
		if replacement is needed,
		construct the result in the following way:
		"* + substring(dot_index)
	  
	  often in express happen the following strings:
	  ".cartesian_point"
	  So, if dot is with index 1, no need to check.
		dot in position 0 should not happen, unless it is an encoded string literal (in our current implementation)
 
*/

	static String getOriginalExpressionString(Token start_token, Token end_token) {
		// the file - in global variable express_file
		String result = "";
		if (start_token == null || end_token == null) {
//		if (start_token == end_token) {
			return "";
		}
		int start_line = start_token.beginLine;
		int start_column = start_token.beginColumn;
		int end_line = end_token.endLine;
		int end_column = end_token.endColumn;
  	int start_column0 = start_column;
  	int end_column0 = end_column;
  
  	FileReader fileReader = null;
 		try 
 		{ 
 			fileReader = new FileReader(express_file); 
 		} catch (FileNotFoundException e) {
			return "";
    }
		BufferedReader bufferedReader = new BufferedReader(fileReader); 
		
		String s; 

// System.out.println("========= file: " + express_file);
// System.out.println("start_line: " + start_line + ", start_culumn: " + start_column + ", end_line: " + end_line + ", end_column: " + end_column);
	
		if (start_column < 1) start_column0 = 0;
		if (end_column < 1) end_column0 = 0;
		
		for (int i = 1; i <= end_line; i++) {
			if (i < start_line) {
				readLine(bufferedReader);
			} else {
				s = readLine(bufferedReader);
//System.out.println("line: " + i + ", length: " + s.length());
				if (s != null) {
//System.out.println("corrected start column: " + start_column0);
					if ((i == start_line) && (start_line == end_line)) {
//System.out.println("== single-line expression == start line: " + start_line + ", start column: " + start_column + ", end column: " + end_column);
						start_column0 = getTABCorrectedColumn(s, start_column);
//System.out.println("recalculated start column: " + start_column0);
						if (start_column0 < 1) {
							start_column0 = 1;
 //System.out.println("========= file: " + express_file);
 //System.out.println("start_line: " + start_line + ", start_culumn: " + start_column + ", end_line: " + end_line + ", end_column: " + end_column);
 //System.out.println("line: " + i + ", length: " + s.length());
						}
						end_column0 = getTABCorrectedColumn(s, end_column);
//System.out.println("recalculated end column: " + end_column0);
						if (end_column0 < 0)  {
							end_column0 = 0;
// System.out.println("========= file: " + express_file);
// System.out.println("start_line: " + start_line + ", start_culumn: " + start_column + ", end_line: " + end_line + ", end_column: " + end_column);
// System.out.println("line: " + i + ", length: " + s.length());
						}
//System.out.println("corrected end column: " + end_column0);
						if (end_column0 > s.length()) end_column0 = s.length();
//System.out.println("<1> string: " + s);
						s = s.substring(start_column0-1, end_column0);
//System.out.println("<1> start: " + (start_column0-1));
//System.out.println("<1> end: " + end_column0);
//System.out.println("<1> substring: " + s);
					} else
					if (i == start_line) {
						start_column0 = getTABCorrectedColumn(s, start_column);
						if (start_column0 < 1) {
							start_column0 = 1;
 //System.out.println("========= file: " + express_file);
 //System.out.println("start_line: " + start_line + ", start_culumn: " + start_column + ", end_line: " + end_line + ", end_column: " + end_column);
 //System.out.println("line: " + i + ", length: " + s.length());
						}
//System.out.println("<2> string: " + s);
						s = s.substring(start_column0-1);
//System.out.println("<2> start: " + (start_column0-1));
//System.out.println("<2> substring: " + s);
					} else
					if (i == end_line) {
						end_column0 = getTABCorrectedColumn(s, end_column);
						if (end_column0 < 0)  {
							end_column0 = 0;
// System.out.println("end < 0 ========= file: " + express_file);
// System.out.println("start_line: " + start_line + ", start_culumn: " + start_column + ", end_line: " + end_line + ", end_column: " + end_column);
// System.out.println("line: " + i + ", length: " + s.length());
						}
//System.out.println("corrected end column: " + end_column0);
						if (end_column0 > s.length()) end_column0 = s.length();
//System.out.println("end column before " + s);
//System.out.println("<3> string: " + s);
						s = s.substring(0, end_column0);
//System.out.println("<3> start: " + 0);
//System.out.println("<3> end: " + end_column0);
//System.out.println("<3> substring: " + s);

//System.out.println("end column after " + s);
					}
					result += s;
					if (i < end_line) {
						result += "\n";
					}
				}
			}
		} 
		close(bufferedReader);
// System.out.println(">>>>>> final expression: \n" + result);
		return result;
	} 
	
	static int getTABCorrectedColumn(String s, int column) {

/*  
		   we have to find all the tabs from the beginning of the line to the column one-by-one
		   and correct the column value, accordingly, taking into account the exact position of the tab, as it may have different values

		   if tab = 8
		   01234567890
		   ||||||||1
       ||||||| 2
       ||||||  3
       |||||   4
       ||||    5
       |||     6
       ||      7
       |       8

      so, 

      0 - 8
      1 - 7
      2 - 6
      3 - 5
      4 - 4
      5 - 3
      6 - 2
      7 - 1
      8 - 8
      9 - 7
     10 - 6
     11 - 5

and so on

	x
 	*	


          1         2         3
0123456789012345678901234567890
				          
	X
a	x
ab	x
abc	x
abcd	x
abcde	x
abcdef	x
abcdefg	x
abcdefgh	x
abcdefghi	x
			x - 3 tabs
a			* - 3 tabs
ab			* - 3 tabs
a	bc	def	* - 3 tabs
a	bc		* - 3 tabs
			


in other words, we need to decrease the column number in accordance to tab value (8 for now) and tab stop value

tab stops
if tab is 8
0, 8, 16, 24, etc. =  8xn

if we find a tab in position  0: its value is 8, we need 1, so decrease by 7  0x8 + 0, decrease by 7-0
if we find a tab in position  1: its value is 7, we need 1, so decrease by 6  0x8 + 1  decrease by 7-1
if we find a tab in position  2: its value is 6, we need 1, so decrease by 5  0x8 + 2  decrease by 7-2
if we find a tab in position  3: its value is 5, we need 1, so decrease by 4  0x8 + 3  decrease by 7-3
if we find a tab in position  4: its value is 4, we need 1, so decrease by 3  0x8 + 4  decrease by 7-4
if we find a tab in position  5: its value is 3, we need 1, so decrease by 2  0x8 + 5  decrease by 7-5
if we find a tab in position  6: its value is 2, we need 1, so decrease by 1  0x8 + 6  decrease by 7-6
if we find a tab in position  7: its value is 1, we need 1, so decrease by 0  0x8 + 7  decrease by 7-7
if we find a tab in position  8: its value is 8, we need 1, so decrease by 7  1x8 + 0  decrease by 7-0
if we find a tab in position  9: its value is 7, we need 1, so decrease by 6  1x8 + 1  decrease by 7-1
if we find a tab in position 10: its value is 6, we need 1, so decrease by 5  1x8 + 2  decraese by 7-2
if we find a tab in position 11: its value is 5, we need 1, so decrease by 4  1x8 + 3  decrease by 7-3 

 6/8 = 
 7/8 = 0, remainder 7, 0+7
 8/8 = 1, remainder 0, 8
 9/8 = 1, remainder 1, 8+1
10/8 = 1, remainder 2, 8+2
11/8 = 1, remainder 3, 8+3


so,  what we do?

check in the loop each character of the string from the beginning,
if tab found,  devide the position of that character by 8, take the remainder, and decrease the column value by (7-remainder)

however, what is the possition of tab character? - it is the expanded possition!!
so we need to have also the expanded possition counter


also, don't have to check any further than the column number, because any futhre tabs should not be corrected,
but which number, the original one or the corrected one?
the real phisical string has 1 char per tab, the original column number is too high

\t\txxxxxx\t\txxxxxx\t\taaaaaaa
        xxxxxx       xxxxxxxx       aaaaaa
                        | - original number points here,
if applied, would take too much of the string                        



another solution would be to expant tabs - all tabs in the string


  */


//System.out.println("@@@@@@@ recalculating column: " + column + " for string: " + s);

		int result = column;
		byte [] bytes = s.getBytes();
	
		for (int i = 0, j = 0, k = 0; i < s.length(); i++) {
//			if (j >= column) break;
			if (i >= result) break; // > or >= ? hardly tab can be at this position, so perhaps not important - because tab cannot be at token.start, no such tokens 
			if (bytes[i] == '\t') {
//System.out.println("tab found - i: " + i + ", j: " + j + ", k: " + k);
//				int correction = 7 - j%8;
				int correction = 7 - k%8;
//System.out.println("correction = 7 - remainder of " + j + " devided by 8 = " + correction);
//System.out.println("correction = 7 - remainder of " + k + " devided by 8 = " + correction);
				result -= correction;
				j += correction + 1;
//System.out.println("corrected column: " + result);
//System.out.println("corrected j: " + j);
//System.out.println("correction occured, i: " + i + ", result: " + result);
				k += correction + 1;
			} else {
				k++;
			}
			
		}
	
    if (result < 0) {
			System.out.println("WARNING - ORIGINAL EXPRESSIONS, correcting column: " + column + ", corrected: " + result + ", for string: " + s); 
    	result = 0;
  	}        
		return result;
	}


	
	static String readLine(BufferedReader bufferedReader) {
   try 
   {
   		return bufferedReader.readLine( );
   } catch(IOException e) {
   }
   return null;		
	}


	static void close(BufferedReader bufferedReader) {
    try {
        bufferedReader.close( );
    }
    catch(IOException e) { }
	}

	static String replace_schema_name(String input) throws SdaiException {

		if (input.charAt(0) != '\"') {
			// all our non-encoded string literals start with java double quote
			return input;
		}
		String possible_schema_name = null; 
		int index = input.indexOf('.');
		if (index == 0) {
 System.out.println("ERROR (internal): literal representation of string literal in java without opening double quote: " + input);
	    	
			return input;
		} 
		if (index == 1) {
			// this may be the entity part such as '.cartesian_point'
			return input;
		} 
		if (index > 1) {
			// possible schema prefix
			possible_schema_name = input.substring(1,index);
		} else {
			// possible schema name alone
			possible_schema_name = input.substring(1,input.length()-1);
		}
		
// 		String possible_model_name = possible_schema_name.toUpperCase() + "_DICTIONARY_DATA";
 		// removing toUpperCase() results in recognizing as schema prefices only strings with all uppercase letters
 		String possible_model_name = possible_schema_name + "_DICTIONARY_DATA";


		SdaiModel possible_model = repository.findSdaiModel(possible_model_name);	
//		SdaiModel possible_model = findModel2(possible_schema_name);
    if (possible_model != null) {
			if (index > 1) {
	
	// requested by Gintaras: to return "*" instead of "*."

//			if (input.substring(index).equals(".\"")) {
//				return "\"*\"";

//				return "SdaiSession.asterisk";
//			} else {

	//System.out.println("2: " + input.substring(index));
	
				return "\"*" + input.substring(index);
//				return "SdaiSession.asterisk+\"" + input.substring(index);
//			}
			} else {
//				return "\"*\"";
				return "SdaiSession.asterisk";
    	}
    } else {
    	return input;
    }
		
		
	}


	static String get_original_schema_name(String input) throws SdaiException {

		if (input.charAt(0) != '\"') {
			// all our non-encoded string literals start with java double quote
			return null;
		}
		String possible_schema_name = null; 
		int index = input.indexOf('.');
		if (index == 0) {
 System.out.println("ERROR (internal): literal representation of string literal in java without opening double quote: " + input);
	    	
			return null;
		} 
		if (index == 1) {
			// this may be the entity part such as '.cartesian_point'
			return null;
		} 
		if (index > 1) {
			// possible schema prefix
			possible_schema_name = input.substring(1,index);
		} else {
			// possible schema name alone
			possible_schema_name = input.substring(1,input.length()-1);
		}
		
// 		String possible_model_name = possible_schema_name.toUpperCase() + "_DICTIONARY_DATA";
// removing toUpperCase() results in recognizing as schema prefices only strings with all uppercase letters
 		String possible_model_name = possible_schema_name + "_DICTIONARY_DATA";


		SdaiModel possible_model = repository.findSdaiModel(possible_model_name);	
//		SdaiModel possible_model = findModel2(possible_schema_name);
    if (possible_model != null) {
			if (index > 1) {
//				return "\"*" + input.substring(index);
				if (flag_oc) {
					return "\"" + possible_schema_name + "\"";
				} else {
//					return "\"" + possible_schema_name.toUpperCase() + "\"";
					return "\"" + possible_schema_name + "\"";
				}
			} else {
//				return "\"*\"";
			if (flag_oc) {
				return "\"" + possible_schema_name + "\"";
			} else {
//				return "\"" + possible_schema_name.toUpperCase() + "\"";
				return "\"" + possible_schema_name + "\"";
			}
//				return "\"" + possible_schema_name.toUpperCase() + "\"";
    	}
    } else {
    	return null;
    }
		
		
	}


  static SdaiModel  findModel2(String schema_name) throws SdaiException {
  
// System.out.println("in findModel - schema name: " + schema_name);  
	  String name_searched = schema_name.toUpperCase() + "_DICTIONARY_DATA";
	  ASdaiModel models  = repository.getModels();
		SdaiIterator iter_models = models.createIterator();
		while (iter_models.next()) {
			SdaiModel sm1 = models.getCurrentMember(iter_models);
			String model_name = sm1.getName();
//System.out.println("in findModel - current: " + model_name + ", searching for: " + name_searched);  

// printDDebug("findModel - searching: " + name_searched + ", current: " + model_name + ", nr of models: " + models.getMemberCount());
			if (model_name.equalsIgnoreCase(name_searched)) {
				return sm1;
			}
		}
		return null;
  }


	static X_AllSchemas makeOneRoot(Vector all_trees) {
		X_AllSchemas result = null;
		X_AllSchemas current;
		if (all_trees == null) return result;
		if (all_trees.size() < 1) return result;
		result = (X_AllSchemas)all_trees.elementAt(0);
		if (result == null) return result;
		if (all_trees.size() < 2) return result;
		int main_children_count = result.jjtGetNumChildren();
		for (int i = 1; i < all_trees.size(); i++) {
			current = (X_AllSchemas)all_trees.elementAt(i);
			if (current != null) {
				int children_count = current.jjtGetNumChildren();
				for (int j = 0; j < children_count; j++) {
					Node a_child = current.jjtGetChild(j);
					result.jjtAddChild(a_child, main_children_count++);
				}
			}
		} 
		all_trees.clear();
		all_trees = null;
		return result;
	}


	static void printErrorNoMore(String msg) throws SdaiException {
		String schema_name = "ERROR in file " + express_file + ": \n";
		if (sd != null) {
			if (flag_oc) {
				schema_name += sd.getName(null) + ": ";
			} else {
				schema_name += sd.getName(null).toLowerCase() + ": ";
			}
		}
		if (express_files.size() > 1) {
			System.out.println(schema_name + msg);
		} else {
			System.out.println("ERROR: " + msg);
		}
	}


	static void printErrorMsg5(String msg, Token token, boolean print_line) throws SdaiException {
 		printErrorMsg(msg, token, print_line);
      
	}

	static void printErrorMsgX(String msg, Token token, boolean print_line) throws SdaiException {
		if (flag_more_errors) {
			printErrorMsg(msg, token, print_line);
		}
	}

	static void printErrorMsg(String msg, Token token, boolean print_line) throws SdaiException {
		String schema_name = "ERROR in file " + express_file + ": \n";
		if (sd != null) {
			if (flag_oc) {
				schema_name += sd.getName(null) + ": ";
			} else {
				schema_name += sd.getName(null).toLowerCase() + ": ";
			}
		}

		int line = -1;
		int column = -1;
		if (token != null) {
			line = token.beginLine;
			column = token.beginColumn;
		} else {
			if (print_line) {
				Token t = Compiler2.getToken(0);
				line = t.beginLine;
				column = t.beginColumn;
			}
		}
		
		// perhaps we can provide the last line of the file, or better, of the schema, if the  line is not provided?
		// or perhaps it can be done in the error parser. See later
		String line_str = "";
		
		if (line >= 0) {
			line_str = " line: " + line + ", column: " + column + ". ";
		}

		if (express_files.size() > 1) {
			System.out.println(schema_name + line_str + msg);
		} else {
			System.out.println("ERROR: " + line_str + msg);
		}
	}

	// leave ERROR at this time, perhaps we will change to SOFT ERROR or something, but then, more work in Eclipse to parse it will be needed, so perhaps - not
	static void printSoftErrorMsg(String msg, Token token, boolean print_line) throws SdaiException {
		String schema_name = "ERROR in file " + express_file + ": \n";
		if (sd != null) {
			if (flag_oc) {
				schema_name += sd.getName(null) + ": ";
			} else {
				schema_name += sd.getName(null).toLowerCase() + ": ";
			}
		}

		int line = -1;
		int column = -1;
		if (token != null) {
			line = token.beginLine;
			column = token.beginColumn;
		} else {
			if (print_line) {
				Token t = Compiler2.getToken(0);
				line = t.beginLine;
				column = t.beginColumn;
			}
		}
		
		// perhaps we can provide the last line of the file, or better, of the schema, if the  line is not provided?
		// or perhaps it can be done in the error parser. See later
		String line_str = "";
		
		if (line >= 0) {
			line_str = " line: " + line + ", column: " + column + ". ";
		}

		if (express_files.size() > 1) {
			System.out.println(schema_name + line_str + msg);
		} else {
			System.out.println("ERROR: " + line_str + msg);
		}
	}


	static void printSoftErrorMsg_alt(String msg, Token token, boolean print_line) throws SdaiException {
		String schema_name = "SOFT ERROR in file " + express_file + ": \n";
		if (sd != null) {
			if (flag_oc) {
				schema_name += sd.getName(null) + ": ";
			} else {
				schema_name += sd.getName(null).toLowerCase() + ": ";
			}
		}

		int line = -1;
		int column = -1;
		if (token != null) {
			line = token.beginLine;
			column = token.beginColumn;
		} else {
			if (print_line) {
				Token t = Compiler2.getToken(0);
				line = t.beginLine;
				column = t.beginColumn;
			}
		}
		
		// perhaps we can provide the last line of the file, or better, of the schema, if the  line is not provided?
		// or perhaps it can be done in the error parser. See later
		String line_str = "";
		
		if (line >= 0) {
			line_str = " line: " + line + ", column: " + column + ". ";
		}

		if (express_files.size() > 1) {
			System.out.println(schema_name + line_str + msg);
		} else {
			System.out.println("SOFT ERROR: " + line_str + msg);
		}
	}


	static void printErrorMsgW(String msg, Token token, boolean print_line) throws SdaiException {
		String schema_name = "WARNING in file " + express_file + ": \n";
		if (sd != null) {
			if (flag_oc) {
				schema_name += sd.getName(null) + ": ";
			} else {
				schema_name += sd.getName(null).toLowerCase() + ": ";
			}
		}

		int line = -1;
		int column = -1;
		if (token != null) {
			line = token.beginLine;
			column = token.beginColumn;
		} else {
			if (print_line) {
				Token t = Compiler2.getToken(0);
				line = t.beginLine;
				column = t.beginColumn;
			}
		}
		
		// perhaps we can provide the last line of the file, or better, of the schema, if the  line is not provided?
		// or perhaps it can be done in the error parser. See later
		String line_str = "";
		
		if (line >= 0) {
			line_str = " line: " + line + ", column: " + column + ". ";
		}

		if (express_files.size() > 1) {
			System.out.println(schema_name + line_str + msg);
		} else {
			System.out.println("WARNING: " + line_str + msg);
		}
	}





	static void printWarningNoMore(String msg) throws SdaiException {
		String schema_name = "WARNING! ";
		if (sd != null) {
			if (flag_oc) {
				schema_name += sd.getName(null) + ": ";
			} else {
				schema_name += sd.getName(null).toLowerCase() + ": ";
			}
		}
		System.out.println(schema_name + msg);
	}

	static void printOK(String msg) {
		System.out.println("" + msg);
	}




	static void printWarningMsg(String msg, Token token, boolean print_line) throws SdaiException {
		String schema_name = "WARNING in file " + express_file + ": \n";
		if (sd != null) {
			if (flag_oc) {
				schema_name += sd.getName(null) + ": ";
			} else {
				schema_name += sd.getName(null).toLowerCase() + ": ";
			}
		}

		int line = -1;
		int column = -1;
		if (token != null) {
			line = token.beginLine;
			column = token.beginColumn;
		} else {
			if (print_line) {
				Token t = Compiler2.getToken(0);
				line = t.beginLine;
				column = t.beginColumn;
			}
		}
		
		// perhaps we can provide the last line of the file, or better, of the schema, if the  line is not provided?
		// or perhaps it can be done in the error parser. See later
		String line_str = "";
		
		if (line >= 0) {
			line_str = " line: " + line + ", column: " + column + ". ";
		}

		if (express_files.size() > 1) {
			if (print_line) {
				System.out.println(schema_name + line_str + msg);
			} else {
//				System.out.println("WARNING: " + line_str + msg);
				System.out.println(schema_name + msg);
			}
		} else {
			if (print_line) {
				System.out.println("WARNING: " + line_str + msg);
			} else {
				System.out.println("WARNING: " + msg);
			}
		}
	}




  static void printVerbose0(String msg) {
    if (flag_verbose0 || flag_verbose) {
      System.out.println("" + msg);
    }
  }

  static void printVerbose(String msg) {
    if (flag_verbose) {
      System.out.println("" + msg);
    }
  }


  static void printEDebug(String msg) {
    if (flag_error_debug) {
      System.out.println("ERROR (debug): " + msg);
    }
  }

  static void printDebug(String msg) {
    if (flag_debug) {
      System.out.println("EC DEBUG> " + msg);
    }
  }

  static void printDDebug(String msg) {
    if (flag_deep_debug) {
      System.out.println("EC DEEP DEBUG> " + msg);
    }
  }

  static void printScopeStack(String msg) {
    if (flag_scope_stack) {
      System.out.println("EC SCOPE STACK @ " + scope_stack.size() + " > " + msg);
    }
  }

  static void printStack(String msg) {
    if (flag_stack) {
      System.out.println("EC STACK @ " + argument_stack.size() + " > " + msg);
    }
  }

  static void printXStack(String msg) {
    if (flag_stack) {
      System.out.println("X STACK @ " + expression_stack.size() + " > " + msg);
    }
  }
  static void printTStack(String msg) {
    if (flag_stack) {
      System.out.println("T STACK @ " + type_stack.size() + " > " + msg);
    }
  }

  static void printScope(String msg) {
    if (flag_scope) {
      ;
    }

    //			System.out.println("EC STACK @ " + argument_stack.size() + " > " + msg);
  }

  /*
    
  In SdaiRepository:
     public synchronized ASchemaInstance getSchemas() throws SdaiException
     public synchronized SchemaInstance createSchemaInstance(String name, ESchema_definition schema)
		  In ASchemaInstance:
   getCurrentMember()
     
  In SchemaInstance:
     public String getName() throws SdaiException {
     public ASdaiModel getAssociatedModels() throws SdaiException {
     public String getNativeSchemaString() throws SdaiException { -- the name of underlying schema definition.
     public ESchema_definition getNativeSchema() throws SdaiException {
     public void delete() throws SdaiException {
     public void addSdaiModel(SdaiModel model) throws SdaiException {
     public void removeSdaiModel(SdaiModel model) throws SdaiException { 
  So,
        updateSchemaInstances(repository, repository_name, express_file);
           
     The ExpressCompilerRepo must be updated and also, if -express was used, the specific SchemaInstance with that name
     ExpressCompilerRepo - all sdai models removed and added from the repository again
     specific - all the models removed from that SchemaInstance and added only those with ReadWrite access.
           
           
           
        deleteRelatedModels(repository, express_file);
     the SchemaInstance with the name of the current express_file searched.
     if found,
     loop through all its models, and the coresponding models removed from the repository.
           
           

   */

	static void initializeHashMaps(SdaiRepository repo)
                              throws SdaiException {
    if (repo == null) {
      //System.out.println("XC: initializeHashMaps: repository is NULL");

      return;
    }
	
// System.out.println(" Starting initializing HashMaps ");
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() == SdaiModel.NO_ACCESS) {
        model2.startReadOnlyAccess();
      }
// System.out.println(" model: " + model2.getName());
		
			Object o = hm_entity_declarations.get(model2);
			if (o == null) {
				hm_current_entity_declarations = new HashMap();
				AEntity_declaration edcs = (AEntity_declaration)model2.getEntityExtentInstances(EEntity_declaration.class);
        SdaiIterator iter_inst = edcs.createIterator();
		    while (iter_inst.next()) {
	        EEntity_declaration edc = (EEntity_declaration) edcs.getCurrentMemberObject(iter_inst);
					if (edc.testDefinition(null)) { // for corrupt date because of -replace_schemas
						String entity_name = ((EEntity_definition)edc.getDefinition(null)).getName(null).toLowerCase();
// if (model2.getName().equalsIgnoreCase("qualified_measure_schema_dictionary_data")) {
//	 System.out.println("entity name: " + entity_name);					
// }
						hm_current_entity_declarations.put(entity_name, edc);
					}
				}
				hm_entity_declarations.put(model2, hm_current_entity_declarations);
	
				EEntity_definition ed = findEntity_definition("uncertainty_qualifier", null);
// System.out.println(" uncertainty_qualifier: " + ed);				
			} else {
// System.out.println(" HasMap already exists");				
	
			} 


			o = hm_type_declarations.get(model2);
			if (o == null) {
				hm_current_type_declarations = new HashMap();
				AType_declaration tdcs = (AType_declaration)model2.getEntityExtentInstances(EType_declaration.class);
        SdaiIterator iter_inst = tdcs.createIterator();
		    while (iter_inst.next()) {
	        EType_declaration tdc = (EType_declaration) tdcs.getCurrentMemberObject(iter_inst);
					String type_name = ((EDefined_type)tdc.getDefinition(null)).getName(null).toLowerCase();
					hm_current_type_declarations.put(type_name, tdc);
				}
				hm_type_declarations.put(model2, hm_current_type_declarations);
			} 

			o = hm_function_declarations.get(model2);
			if (o == null) {
				hm_current_function_declarations = new HashMap();
				AFunction_declaration fdcs = (AFunction_declaration)model2.getEntityExtentInstances(EFunction_declaration.class);
        SdaiIterator iter_inst = fdcs.createIterator();
		    while (iter_inst.next()) {
	        EFunction_declaration fdc = (EFunction_declaration) fdcs.getCurrentMemberObject(iter_inst);
					String function_name = ((EFunction_definition)fdc.getDefinition(null)).getName(null).toLowerCase();
					hm_current_function_declarations.put(function_name, fdc);
				}
				hm_function_declarations.put(model2, hm_current_function_declarations);
			} 

			// procedure
			o = hm_procedure_declarations.get(model2);
			if (o == null) {
				hm_current_procedure_declarations = new HashMap();
				AProcedure_declaration fdcs = (AProcedure_declaration)model2.getEntityExtentInstances(EProcedure_declaration.class);
        SdaiIterator iter_inst = fdcs.createIterator();
		    while (iter_inst.next()) {
	        EProcedure_declaration fdc = (EProcedure_declaration) fdcs.getCurrentMemberObject(iter_inst);
					String procedure_name = ((EProcedure_definition)fdc.getDefinition(null)).getName(null).toLowerCase();
					hm_current_procedure_declarations.put(procedure_name, fdc);
				}
				hm_procedure_declarations.put(model2, hm_current_procedure_declarations);
			} 

			// constant
			o = hm_constant_declarations.get(model2);
			if (o == null) {
				hm_current_constant_declarations = new HashMap();
				AConstant_declaration fdcs = (AConstant_declaration)model2.getEntityExtentInstances(EConstant_declaration.class);
        SdaiIterator iter_inst = fdcs.createIterator();
		    while (iter_inst.next()) {
	        EConstant_declaration fdc = (EConstant_declaration) fdcs.getCurrentMemberObject(iter_inst);
					String constant_name = ((EConstant_definition)fdc.getDefinition(null)).getName(null).toLowerCase();
					hm_current_constant_declarations.put(constant_name, fdc);
				}
				hm_constant_declarations.put(model2, hm_current_constant_declarations);
			} 

			// rule
			o = hm_rule_declarations.get(model2);
			if (o == null) {
				hm_current_rule_declarations = new HashMap();
				ARule_declaration fdcs = (ARule_declaration)model2.getEntityExtentInstances(ERule_declaration.class);
        SdaiIterator iter_inst = fdcs.createIterator();
		    while (iter_inst.next()) {
	        ERule_declaration fdc = (ERule_declaration) fdcs.getCurrentMemberObject(iter_inst);
					String rule_name = ((EGlobal_rule)fdc.getDefinition(null)).getName(null).toLowerCase();
					hm_current_rule_declarations.put(rule_name, fdc);
				}
				hm_rule_declarations.put(model2, hm_current_rule_declarations);
			} 


			// subtype_constraint
			o = hm_subtype_constraint_declarations.get(model2);
			if (o == null) {
				hm_current_subtype_constraint_declarations = new HashMap();
				ASubtype_constraint_declaration fdcs = (ASubtype_constraint_declaration)model2.getEntityExtentInstances(ESubtype_constraint_declaration.class);
        SdaiIterator iter_inst = fdcs.createIterator();
		    while (iter_inst.next()) {
	        ESubtype_constraint_declaration fdc = (ESubtype_constraint_declaration) fdcs.getCurrentMemberObject(iter_inst);
					String subtype_constraint_name = ((ESub_supertype_constraint)fdc.getDefinition(null)).getName(null).toLowerCase();
					hm_current_subtype_constraint_declarations.put(subtype_constraint_name, fdc);
				}
				hm_subtype_constraint_declarations.put(model2, hm_current_subtype_constraint_declarations);
			} 
			// parameters
			o = hm_parameters.get(model2);
			if (o == null) {
				hm_current_parameters = new HashMap();
				AParameter fdcs = (AParameter)model2.getEntityExtentInstances(EParameter.class);
        SdaiIterator iter_inst = fdcs.createIterator();
		    while (iter_inst.next()) {
	        EParameter fdc = (EParameter) fdcs.getCurrentMemberObject(iter_inst);
//					String parameter_name = ((EParameter)fdc.getName(null).toLowerCase();
					// TODO problem 1 have to add function prefices for scope
					// 		should be possible to do - 1. find the function/procedure that uses this parameter
					//                               2. find the declaration of this function/procedure
					//                               3. construct prefices according to the declaration, if it is inner -more prefices, etc.
					// problem 2 - what about variables, they are not preserved
					String parameter_name = constructPrefixedParameterName(fdc);
					hm_current_parameters.put(parameter_name, fdc);
				}
				hm_parameters.put(model2, hm_current_parameters);
			} 
	
		}
	}

	static String constructPrefixedParameterName(EParameter par) throws SdaiException {
		String result = null;
	
		// find the function or the procedure to which this parameter belongs
		AAlgorithm_definition fds = new AAlgorithm_definition();
		CAlgorithm_definition.usedinParameters(null, par, null, fds);
		SdaiIterator iter = fds.createIterator();
		// hopefully, only one
		while (iter.next()) {
			EAlgorithm_definition adf = (EAlgorithm_definition)fds.getCurrentMemberObject(iter);

			if (flag_oc) { // is it needed?
				result = constructFunctionProcedurePrefix(adf) + par.getName(null).toLowerCase();
//2009				result = constructFunctionProcedurePrefix(adf) + par.getName(null);
			} else {
				result = constructFunctionProcedurePrefix(adf) + par.getName(null).toLowerCase();
			}

/*
			AAlgorithm_declaration dclrs = new AAlgorithm_declaration();
			CAlgorithm_declaration.usedinDefinition(null, adf, null, dclrs);
			SdaiIterator iter2 = dclrs.createIterator();
			// hopefully, only one
			while (iter2.next()) {
				EAlgorithm_declaration dclr = (EAlgorithm_declaration)dclrs.getCurrentMemberObject(iter2);
				EAlgorithm_definition adf2 = (EAlgorithm_definition)dclr.getDefinition(null);
				result = constructFunctionProdecurePrefix(adf2) + par.getName(null).toLowerCase();
			}
*/		
		}	
	
		return result;
	}





	static String constructGlobalRulePrefix (EGlobal_rule gr) throws SdaiException {

		String result = null;
		if (flag_oc) {
			result = gr.getName(null).toLowerCase() + "$";
//2009			result = gr.getName(null) + "$";
		} else {
			result = gr.getName(null).toLowerCase() + "$";
		}
		return result;
	
	}

	static String constructEntityPrefix (EEntity_definition ed) throws SdaiException {

		String result = null;
		if (flag_oc) {
			result = ed.getName(null).toLowerCase() + "$";
//2009			result = ed.getName(null) + "$";
		} else {
			result = ed.getName(null).toLowerCase() + "$";
		}
		return result;
	
	}



	static String constructMapDefinitionPrefix (EMap_definition md) throws SdaiException {

		String result = null;
		if (flag_oc) {
			result = md.getName(null).toLowerCase() + "$";
//2009			result = md.getName(null) + "$";
		} else {
			result = md.getName(null).toLowerCase() + "$";
		}
		return result;
	
	}


	// need to add support for global rules, that is,  going to the outer level we can encounter a global rule instead of function


	static String constructConstantPrefix (EConstant_definition fd4) throws SdaiException {
		String result = "";

		EEntity fd3 = getParentFunctionProcedureRuleDefinitionX(fd4);
// System.out.println("<CONST-STUFF-01> fd3: " + fd3);		
		if (fd3 instanceof EAlgorithm_definition) {
			result = constructFunctionProcedurePrefix((EAlgorithm_definition)fd3);
		} else
		if (fd3 instanceof EGlobal_rule) {
			result = constructGlobalRulePrefix ((EGlobal_rule)fd3);
		}

		return result;
	}
	
	static String constructFunctionProcedurePrefix (EAlgorithm_definition fd4) throws SdaiException {
	  String result = "";
		EAlgorithm_definition fd2 = null;
		EEntity fd3 = null;
		String f_name = null;
		
		fd2 = fd4;
		
		if (fd4 == null) {
//			result = "_NULL_algorithm_definition_";
			return result;
		}
		
//		result = fd2.getName(null);
//		result = result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();
		
		for (;;) {
//			fd3 = getParentFunctionDefinition(fd2);
			if (fd2 == null) {
				fd3 = null;
			} else {
				fd3 = getParentFunctionProcedureRuleDefinition(fd2);
			}
			if (fd3 == null) {
				f_name = fd4.getName(null);
				if (flag_oc) {
					result += f_name.toLowerCase() + "$";
//2009					result += f_name + "$";
				} else {
					result += f_name.toLowerCase() + "$";
				}
				break;
			} else {
				// still inner function, add names
				if (fd3 instanceof EAlgorithm_definition) {
					f_name = ((EAlgorithm_definition)fd3).getName(null);
					fd2 = (EAlgorithm_definition)fd3;
				} else
				if (fd3 instanceof EGlobal_rule) {
					f_name = ((EGlobal_rule)fd3).getName(null);
					fd2 = null;
				}
				if (flag_oc) {
					result = f_name.toLowerCase() + "$" + result;
//2009					result = f_name + "$" + result;
				} else {
					result = f_name.toLowerCase() + "$" + result;
				}
			}
		}
	
		return result;	
	
	
	}


	static String constructFunctionProcedureNoRulePrefix (EAlgorithm_definition fd4) throws SdaiException {
	  String result = "";
		EAlgorithm_definition fd2 = null;
		EAlgorithm_definition fd3 = null;
		String f_name = null;
		
		fd2 = fd4;
		
		if (fd2 == null) {
//			result = "_NULL_algorithm_definition_";
			return result;
		}
		
//		result = fd2.getName(null);
//		result = result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();
		
		for (;;) {
//			fd3 = getParentFunctionDefinition(fd2);
			fd3 = getParentFunctionProcedureDefinition(fd2);
			if (fd3 == null) {
				f_name = fd4.getName(null);
				if (flag_oc) {
					result += f_name.toLowerCase() + "$";
//2009					result += f_name + "$";
			 	} else {
					result += f_name.toLowerCase() + "$";
			 	}	
				break;
			} else {
				// still inner function, add names
				f_name = fd3.getName(null);
				if (flag_oc) {
					result = f_name.toLowerCase() + "$" + result;
//2009					result = f_name + "$" + result;
				} else {
					result = f_name.toLowerCase() + "$" + result;
				}
				fd2 = fd3;
			}
		}
	
		return result;	
	
	
	}


	static String constructVariableParameterKey(String id) throws SdaiException {
		if (active_scope instanceof EAlgorithm_definition) {
			if (flag_oc) {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
//2009				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id;
			} else {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
			}
		} else
		if (active_scope instanceof EGlobal_rule) {
      // is this opposite thing intentional? toLowerCase() could have been in NOT oc
			if (flag_oc) {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
			} else {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
//2009				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id;
			}
		} else 
		if (active_scope instanceof EEntity_definition) {
			if (flag_oc) {
				return constructEntityPrefix((EEntity_definition)active_scope) + id.toLowerCase();
//2009				return constructEntityPrefix((EEntity_definition)active_scope) + id;
			} else {
				return constructEntityPrefix((EEntity_definition)active_scope) + id.toLowerCase();
			}
		}	else {
			return null;
		}
	}


	/*
			 here we have an implicit variable in a scope other than represented by active_scope, such as query_expression,
			 which may be nested themselves, so each such query_expression (or its implicit variable has an unique id assignet to it
	*/


	static String constructImplicitVariableKey(String id, int uid) throws SdaiException {
		String key_prefix = "";

		String result = null;
		if (flag_oc) {
			result = id.toLowerCase();
//2009			result = id;
		} else {
			result = id.toLowerCase();
		}



//System.out.println("CONSTRUCTING IMPLICIT KEY: id: " + id + ", uid: " + uid); 		



		ECtScope a_scope = active_scope_extension;
//System.out.println("CONSTRUCTING IMPLICIT KEY, first a_cope: " + a_scope); 		
		
		for (;;) {
		
				
			if (a_scope.current_active_scope == null) {
				key_prefix = a_scope.id + "$" +  key_prefix;
//System.out.println("current_active_scope == NULL, adding id: " + key_prefix); 		
			} else {
//				if (active_scope == null) {
//				if (active_scope instanceof ESchema_definition) {

//				if (a_scope.current_active_scope instanceof ESchema_definition) {
				if ((a_scope.current_active_scope instanceof ESchema_definition) || (a_scope.current_active_scope instanceof ESchema_map_definition)) {

//					result = key_prefix + "$" + result;
					result = key_prefix + result;
//System.out.println("CONSTRUCTING IMPLICIT KEY fineshed: " + result); 		
//2009					return result;
					return result.toLowerCase();
				} else
				if (a_scope.current_active_scope instanceof EEntity_definition) {
					key_prefix =  ((EEntity_definition)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
				} else
				if (a_scope.current_active_scope instanceof EDefined_type) {
					key_prefix =  ((EDefined_type)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
				} else
				if (a_scope.current_active_scope instanceof EAlgorithm_definition) {
					key_prefix =  ((EAlgorithm_definition)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
				} else
				if (a_scope.current_active_scope instanceof EGlobal_rule) {
					key_prefix =  ((EGlobal_rule)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
				} else
				if (a_scope.current_active_scope instanceof EMap_definition) {
// System.out.println("current_active_scope - map_definition");
					
					key_prefix =  ((EMap_definition)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
				}
			}
//System.out.println("CONSTRUCTING IMPLICIT KEY, prefix: " + key_prefix + ", current active_scope: " + a_scope.current_active_scope); 		
//System.out.println("CONSTRUCTING IMPLICIT KEY, prefix: " + key_prefix + ", current active_scope: " + a_scope.current_active_scope + ", a_scope: " + a_scope); 		
			a_scope = a_scope.getParent();
// System.out.println("CONSTRUCTING IMPLICIT KEY, next a_cope: " + a_scope); 		
//System.out.println("CONSTRUCTING IMPLICIT KEY, next inst current active scope:  " + a_scope.current_active_scope); 		
		}
		
	
	}



	static String constructImplicitScopePrefix() throws SdaiException {
		String key_prefix = "";
//		String result = id.toLowerCase();

//  System.out.println(">01< CONSTRUCTING IMPLICIT SCOPE PREFIX"); 		
		

		ECtScope a_scope = active_scope_extension;

// System.out.println("In constructImplicitScopePrefix - initial scope: " + a_scope);

		
		for (;;) {
			
			if (a_scope != null) {
			
			if (a_scope.current_active_scope == null) {
 // System.out.println(">03< CONSTRUCTING IMPLICIT SCOPE PREFIX: " + a_scope.id); 		
				key_prefix = a_scope.id + "$" + key_prefix;
			} else {
				if (a_scope.current_active_scope instanceof ESchema_definition) {
//				if (active_scope instanceof ESchema_definition) {
//				if (active_scope == null) {
 // System.out.println(">02< CONSTRUCTING IMPLICIT SCOPE PREFIX FINESHED: " + key_prefix); 		
//					return key_prefix + "$";
//2009					return key_prefix;
					return key_prefix.toLowerCase();
				} else
				if (a_scope.current_active_scope instanceof EEntity_definition) {
					key_prefix =  ((EEntity_definition)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
 // System.out.println(">04< CONSTRUCTING IMPLICIT SCOPE PREFIX - entity: " + key_prefix); 		
				} else
				if (a_scope.current_active_scope instanceof EDefined_type) {
					key_prefix =  ((EDefined_type)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
// System.out.println(">05< CONSTRUCTING IMPLICIT SCOPE PREFIX - defined type: " + key_prefix); 		
				} else
				if (a_scope.current_active_scope instanceof EAlgorithm_definition) {
					key_prefix =  ((EAlgorithm_definition)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
 //System.out.println(">06< CONSTRUCTING IMPLICIT SCOPE PREFIX - algorithm: " + key_prefix); 		
				} else
				if (a_scope.current_active_scope instanceof EGlobal_rule) {
					key_prefix =  ((EGlobal_rule)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
// System.out.println(">07< CONSTRUCTING IMPLICIT SCOPE PREFIX - global_rule: " + key_prefix); 		
				} else
				if (a_scope.current_active_scope instanceof EMap_definition) {
					key_prefix =  ((EMap_definition)a_scope.current_active_scope).getName(null) + "$" + key_prefix;
// System.out.println(">07< CONSTRUCTING IMPLICIT SCOPE PREFIX - global_rule: " + key_prefix); 		
				}
			}
			a_scope = a_scope.getParent();
// System.out.println(">08< CONSTRUCTING IMPLICIT SCOPE PREFIX - parent: " + a_scope); 		
// System.out.println("In constructImplicitScopePrefix - next scope: " + a_scope);
		
		} else {
// System.out.println("In constructImplicitScopePrefix - scope parent null!!!");
// System.out.println(">09< CONSTRUCTING IMPLICIT SCOPE PREFIX - parent NULL: " + key_prefix); 		
//2009			return key_prefix + "$";
			return key_prefix.toLowerCase() + "$";
		}
		
		}
		
	
	}


	static String constructConstantKey(String id) throws SdaiException {
		
		//  I think that for referencing purposes id always has to be case insensitive, even if its original case is preserved
		// I should remove all those if )flag_oc) here
		id = id.toLowerCase();
		
		if (active_scope instanceof EAlgorithm_definition) {
			if (flag_oc) {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
//2009				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id;
			} else {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
			}
		} else 
		if (active_scope instanceof EGlobal_rule) {
			if (flag_oc) {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
//2009				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id;
			} else {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
			}
		}	else {
			if (flag_oc) {
				return id.toLowerCase();
//2009				return id;
			} else {
				return id.toLowerCase();
			}
		}
	}

	static String constructFunctionKey(String id) throws SdaiException {
		if (active_scope instanceof EAlgorithm_definition) {
			if (flag_oc) {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
//2009				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id;
			} else {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
			}
		} else 
		if (active_scope instanceof EGlobal_rule) {
			if (flag_oc) {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
//2009				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id;
			} else {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
			}
		}	else {
			if (flag_oc) {
				return id.toLowerCase();
//2009				return id;
			} else {
				return id.toLowerCase();
			}
		}
	}
	static String constructProcedureKey(String id) throws SdaiException {
		if (active_scope instanceof EAlgorithm_definition) {
			if (flag_oc) {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
//2009				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id;
			} else {
				return constructFunctionProcedurePrefix((EAlgorithm_definition)active_scope) + id.toLowerCase();
			}
		} else 
		if (active_scope instanceof EGlobal_rule) {
			if (flag_oc) {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
//2009				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id;
			} else {
				return constructGlobalRulePrefix((EGlobal_rule)active_scope) + id.toLowerCase();
			}
		}	else {
			if (flag_oc) {
				return id.toLowerCase();
//2009				return id;
			} else {
				return id.toLowerCase();
			}
		}
	}



	static ECtVariable findVariableX(String name) throws jsdai.lang.SdaiException {

    // not clear why resolving of variable names is case-sensitive even when -oc switch is used, especially that it is not working correctly
    // let's try this
    // no, it is not a solution
		//2009
		name = name.toLowerCase();


//  System.out.println("X-01-X IN findVariableX: " + name); 		


/*
if (name.equalsIgnoreCase("prd")) {

	System.out.println("<X-02-X in findVariableX, name: " + name + ", scope: " + active_scope + ", hm: " + hm_current_variables);
	if (active_scope_extension != null) {
		System.out.println("<X-03-X in findVariableX, active_scope_extension NOT NULL: " + active_scope_extension);
	} else {
		System.out.println("<X-04-X in findVariableX, active_scope_extension: NULL ");
	}
}
*/

//if (active_scope != null) {
//	System.out.println("<X-05-X in findVariableX, active_scope: " + active_scope);
//} else {
//	System.out.println("<X-06-X in findVariableX, active_scope is NULL");
//}


		if (!((active_scope instanceof EAlgorithm_definition) || (active_scope instanceof EGlobal_rule))) {
// System.out.println("<in findVariableX, NOT found, active_scope is NOT function, procedure or global rule");

		// may be also entity, type, or null, if query expression or repeat statement, or alias statement
//			return null;
		}
	
		String var_prefix = "";
		boolean done = false;
//		if ((active_scope != null) && (active_scope_extension.current_active_scope == null)) {
		if (active_scope != null) {
			if (active_scope_extension != null) {
//	System.out.println("<X-06-X-01 in findVariableX, active_scope_extension is not NULL: " + active_scope_extension);
				if (active_scope_extension.current_active_scope == null) {
					// implicit stuff
//	System.out.println("<X-06-X-02 in findVariableX, current_active_scope is  NULL - implicit stuff");
					var_prefix = constructImplicitScopePrefix();
//	System.out.println("<X-06-X-03 in findVariableX, constructed var_prefix: " + var_prefix);
					done = true;
				} else {
					// may not be an implicit variable
//	System.out.println("<X-06-X-04 in findVariableX, may not be an implicit variable, current active scope not NULL: " + active_scope_extension.current_active_scope);
				}
			} else {
				// may not be a variable
//	System.out.println("<X-06-X-05 in findVariableX, active_scope_extension - NULL: may not be a variable ");
			}
			
			if (!done) {
				if (active_scope instanceof EAlgorithm_definition) {
					var_prefix = constructFunctionProcedurePrefix ((EAlgorithm_definition)active_scope);
//	System.out.println("<X-06-X-06 in findVariableX, new constructed var_prefix: " + var_prefix);
				} else
				if (active_scope instanceof EGlobal_rule) {
			  	var_prefix = constructGlobalRulePrefix((EGlobal_rule)active_scope);
//	System.out.println("<X-06-X-07 in findVariableX, new constructed var_prefix: " + var_prefix);
				} else 
				if (active_scope instanceof EMap_definition) {
			  	var_prefix = constructMapDefinitionPrefix((EMap_definition)active_scope);
				} else {
//	System.out.println("<X-06-X-07-3 in findVariableX - ELSE - , new constructed var_prefix: " + var_prefix);
				}
			} else {
//	System.out.println("<X-06-X-07-3 in findVariableX - ELSE - , new constructed var_prefix: " + var_prefix);
			} 
			
		} // active_scope != null
		
		String pure_name = null;
		if (flag_oc) {
			pure_name = name.toLowerCase();
//2009			pure_name = name;
		} else {
			pure_name = name.toLowerCase();
		}

// System.out.println("<X-070-X in findVariableX, initial prefix: " + var_prefix + ", name: " + pure_name);
			
		
		Set key_set = hm_current_variables.keySet();

// if (name.equalsIgnoreCase("IsDifferent")) {
//	System.out.println("<X-06-X-08 in findVariableX, key set: " + key_set);
//}		
		
		for (;;) {
			String var_name = var_prefix + pure_name;
// if (name.equalsIgnoreCase("IsDifferent")) {
//  System.out.println("<X-07-X in findVariableX, key: " + var_name);
//}
			if (hm_current_variables.containsKey(var_name)) {
		 		ECtVariable var = (ECtVariable)hm_current_variables.get(var_name);
//System.out.println("<in findVariableX, found: " + var);
//System.out.println("IN findVariableX -found: " + var); 		
				return var;
			} else {
				// try the outer function/procedure
				int index = var_prefix.lastIndexOf('$', var_prefix.length()-2);
				if (index > 0) {
					var_prefix = var_prefix.substring(0,index+1);
				} else {
					// no more outer functions, not found
//System.out.println("<in findVariableX, NOT found");
//System.out.println("IN findVariableX -NOT found"); 		
					return null;
				}
			}
		}
		
//		return null;
 }            


	static ECtVariable findVariableY(String name) throws jsdai.lang.SdaiException {

//if (name.equalsIgnoreCase("pdr_bag")) {
//System.out.println("<in findVariableX, name: " + name + ", scope: " + active_scope + ", hm: " + hm_current_variables);
//}
if (active_scope != null) {
//System.out.println("<in findVariableX, active_scope: " + active_scope);
} else {
//System.out.println("<in findVariableX, active_scope is NULL");
}


		if (!((active_scope instanceof EAlgorithm_definition) || (active_scope instanceof EGlobal_rule))) {
//System.out.println("<in findVariableX, NOT found, active_scope is NULL");
			return null;
		}
	
		String var_prefix = "";
		if (active_scope instanceof EAlgorithm_definition) {
			var_prefix = constructFunctionProcedurePrefix ((EAlgorithm_definition)active_scope);
		} else
		if (active_scope instanceof EGlobal_rule) {
		  var_prefix = constructGlobalRulePrefix((EGlobal_rule)active_scope);

		} 
		
		String pure_name = null;
		if (flag_oc) {
			pure_name = name.toLowerCase();
//2009			pure_name = name;
		} else {	
			pure_name = name.toLowerCase();
		}
		
		for (;;) {
			String var_name = var_prefix + pure_name;
//if (name.equalsIgnoreCase("pdr_bag")) {
//System.out.println("<in findVariableX, key: " + var_name);
//}
			if (hm_current_variables.containsKey(var_name)) {
		 		ECtVariable var = (ECtVariable)hm_current_variables.get(var_name);
//System.out.println("<in findVariableX, found: " + var);
				return var;
			} else {
				// try the outer function/procedure
				int index = var_prefix.lastIndexOf('$', var_prefix.length()-2);
				if (index > 0) {
					var_prefix = var_prefix.substring(0,index+1);
				} else {
					// no more outer functions, not found
//System.out.println("<in findVariableX, NOT found");
					return null;
				}
			}
		}
		
//		return null;
 }            



/*

<in findVariableX, key: aaa$bbb$ccc$b1
<in findVariableX, key: aaa$bbbb1
<in findVariableX, key: aaab1


aaa$bbb$ccc$b1 -> aaa$bbbb1

aaa$bbbb1 - should be aaa$bbb$b1

aaa$bbbb1 -> aaab1

*/


	static boolean isDomainRule() {

    int token_kind;
    String token_image;
    token_kind = Compiler2.getToken(1).kind;
    token_image = Compiler2.getToken(1).image;
//    System.out.println("<isDomainRule> token kind: " + token_kind + ", image: " + token_image);


		// hopefully, these are all the possible cases when NOT domain rule:
		
		if (token_kind == Compiler2Constants.END_ENTITY) return false;
		else if (token_kind == Compiler2Constants.END_RULE) return false;
		else if (token_kind == Compiler2Constants.END_TYPE) return false;
		else if (token_kind == Compiler2Constants.FOR) return false;
		else if (token_kind == Compiler2Constants.IDENTIFIED_BY) return false;
		else if (token_kind == Compiler2Constants.ORDERED_BY) return false;
		else if (token_kind == Compiler2Constants.RETURN) return false;
		else if (token_kind == Compiler2Constants.SELECT) return false;
		return true;

	}



	static boolean isDomainRule_old() {
		// FOR expression can be only in maps

//    try {

//System.out.println("active_scope: " + active_scope);
		if (!(active_scope instanceof EMap_definition)) {
			return true;
		}
    int token_kind;
    String token_image;
    token_kind = Compiler2.getToken(1).kind;
    token_image = Compiler2.getToken(1).image;
    //System.out.println("<isDomainRule> token kind: " + token_kind + ", image: " + token_image);
//		if (token_kind == 108) { // FOR - very dangerous, this number may change
		if (token_image.equalsIgnoreCase("FOR")) {
//System.out.println("found FOR");
			return false;
		}
		return true;

/*
    } catch (SdaiException e) {
      System.out.println("SdaiException in isDomainRule: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }
*/		
	}
	

  static boolean isVariableRefX() {
    Object found = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    try {
//      found = findInterpretedId(token_image);
//System.out.println("<about to find var, 06, name: " + token_image); 
      found = findVariableX(token_image);
//System.out.println("XXX isVariableRef, found: " + found);

//      if (found instanceof ECtVariable) {
      if (found != null) {
        printDebug("QF lookaheads -  selected variable_ref");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isVariableRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }



  static void initializeExpressTypes(SdaiRepository repo)
                              throws SdaiException {

//System.out.println("O-Init-O");
    if (repo == null) {
      //System.out.println("XC: initializeExpressTypes: repository is NULL");

      return;
    }

    ASdaiModel models2 = repo.getModels();
//System.out.println("O-Init-O - models: " + models2.getMemberCount() + ", repo: " + repo);
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() == SdaiModel.NO_ACCESS) {
        model2.startReadOnlyAccess();
      }

      String model_name = model2.getName();
//System.out.println("O-Init-O - current model: " + model_name);

      if (flag_xt_init_sdai) {
        if (model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) {
          Aggregate ia = model2.getEntityExtentInstances(EData_type.class);
          SdaiIterator iter_inst = ia.createIterator();
//System.out.println("O-Init-O - model found: " + model2);

          while (iter_inst.next()) {
            EData_type inst_dt = (EData_type) ia.getCurrentMemberObject(iter_inst);

            if (!(inst_dt.testName(null))) {
              // in the future, no types should be without names.
              //System.out.println("XP: Data Type without name: " + inst_dt);

              continue;
            }

            String data_type_name = inst_dt.getName(null);

            if ((inst_dt instanceof EInteger_type) && (data_type_name.equalsIgnoreCase("_INTEGER"))) {
              _st_integer = (EInteger_type) inst_dt;
            } else if ((inst_dt instanceof EReal_type) && (data_type_name.equalsIgnoreCase("_REAL"))) {
              _st_real = (EReal_type) inst_dt;
            } else if ((inst_dt instanceof ENumber_type) && (data_type_name.equalsIgnoreCase("_NUMBER"))) {
              _st_number = (ENumber_type) inst_dt;
            } else if ((inst_dt instanceof ELogical_type) && 
                           (data_type_name.equalsIgnoreCase("_LOGICAL"))) {
              _st_logical = (ELogical_type) inst_dt;
            } else if ((inst_dt instanceof EBoolean_type) && 
                           (data_type_name.equalsIgnoreCase("_BOOLEAN"))) {
              _st_boolean = (EBoolean_type) inst_dt;
//System.out.println("O-Init-O - boolean: " + _st_boolean);
            } else if ((inst_dt instanceof EString_type) && (data_type_name.equalsIgnoreCase("_STRING"))) {
              _st_string = (EString_type) inst_dt;
            } else if ((inst_dt instanceof EBinary_type) && (data_type_name.equalsIgnoreCase("_BINARY"))) {
              _st_binary = (EBinary_type) inst_dt;
            } else if ((inst_dt instanceof EList_type) && 
                           (data_type_name.equalsIgnoreCase("_LIST_STRING"))) {
              _st_list_string = (EList_type) inst_dt;
            } else
            //                  _st_entity = (EEntity_definition)inst_dt;
            if (data_type_name.equalsIgnoreCase("_ENTITY")) {
              _st_entity = inst_dt;
            } else if (data_type_name.equalsIgnoreCase("_GENERIC")) {
              _st_generic = inst_dt;
            } else if ((inst_dt instanceof EList_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_LIST_GENERIC"))) {
            "_GENERALLIST_0_GENERIC"))) {
              _st_list_generic = (EList_type) inst_dt;
            } else if ((inst_dt instanceof ESet_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_SET_GENERIC"))) {
            "_GENERALSET_0_GENERIC"))) {
              _st_set_generic = (ESet_type) inst_dt;
            } else if ((inst_dt instanceof ESet_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_SET_STRING"))) {
            "_GENERALSET_0_STRING"))) {
              _st_set_string = (ESet_type) inst_dt;
            } else if ((inst_dt instanceof EBag_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_BAG_GENERIC"))) {
            "_GENERALBAG_0_GENERIC"))) {
              _st_bag_generic = (EBag_type) inst_dt;
            } else if ((inst_dt instanceof EAggregation_type) && 
                           (data_type_name.equalsIgnoreCase("_AGGREGATE_GENERIC"))) {
              _st_aggregate_generic = (EAggregation_type) inst_dt;
            }
          }

          break;
        }
      } else {
        if (model_name.equalsIgnoreCase("EXTENDED_DICTIONARY_SCHEMA_DICTIONARY_DATA")) {
          Aggregate ia = model2.getEntityExtentInstances(EData_type.class);
          SdaiIterator iter_inst = ia.createIterator();

          while (iter_inst.next()) {
            EData_type inst_dt = (EData_type) ia.getCurrentMemberObject(iter_inst);

            if (!(inst_dt.testName(null))) {
              // in the future, no types should be without names.
              //System.out.println("XP: Data Type without name: " + inst_dt);

              continue;
            }

            String data_type_name = inst_dt.getName(null);

            if ((inst_dt instanceof EInteger_type) && (data_type_name.equalsIgnoreCase("_INTEGER"))) {
              _st_integer = (EInteger_type) inst_dt;
            } else if ((inst_dt instanceof EReal_type) && (data_type_name.equalsIgnoreCase("_REAL"))) {
              _st_real = (EReal_type) inst_dt;
            } else if ((inst_dt instanceof ENumber_type) && (data_type_name.equalsIgnoreCase("_NUMBER"))) {
              _st_number = (ENumber_type) inst_dt;
            } else if ((inst_dt instanceof ELogical_type) && 
                           (data_type_name.equalsIgnoreCase("_LOGICAL"))) {
              _st_logical = (ELogical_type) inst_dt;
            } else if ((inst_dt instanceof EBoolean_type) && 
                           (data_type_name.equalsIgnoreCase("_BOOLEAN"))) {
              _st_boolean = (EBoolean_type) inst_dt;
            } else if ((inst_dt instanceof EString_type) && (data_type_name.equalsIgnoreCase("_STRING"))) {
              _st_string = (EString_type) inst_dt;
            } else if ((inst_dt instanceof EBinary_type) && (data_type_name.equalsIgnoreCase("_BINARY"))) {
              _st_binary = (EBinary_type) inst_dt;
            } else if ((inst_dt instanceof EList_type) && 
                           (data_type_name.equalsIgnoreCase("_LIST_STRING"))) {
              _st_list_string = (EList_type) inst_dt;
            } else
            //                  _st_entity = (EEntity_definition)inst_dt;
            if (data_type_name.equalsIgnoreCase("_ENTITY")) {
              _st_entity = inst_dt;
            } else if (data_type_name.equalsIgnoreCase("_GENERIC")) {
              _st_generic = inst_dt;
            } else if ((inst_dt instanceof EList_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_LIST_GENERIC"))) {
            "_GENERALLIST_0_GENERIC"))) {
              _st_list_generic = (EList_type) inst_dt;
            } else if ((inst_dt instanceof ESet_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_SET_GENERIC"))) {
            "_GENERALSET_0_GENERIC"))) {
              _st_set_generic = (ESet_type) inst_dt;
            } else if ((inst_dt instanceof ESet_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_SET_STRING"))) {
            "_GENERALSET_0_STRING"))) {
              _st_set_string = (ESet_type) inst_dt;
            } else if ((inst_dt instanceof EBag_type) && 
                           (data_type_name.equalsIgnoreCase(
            //                  "_BAG_GENERIC"))) {
            "_GENERALBAG_0_GENERIC"))) {
              _st_bag_generic = (EBag_type) inst_dt;
            } else if ((inst_dt instanceof EAggregation_type) && 
                           (data_type_name.equalsIgnoreCase("_AGGREGATE_GENERIC"))) {
              _st_aggregate_generic = (EAggregation_type) inst_dt;
            }
          }

          break;
        }
      }
    }
  }

  static boolean findIfParameter(String name) throws SdaiException {
    // boolean findIfVariable(String name) throws SdaiException {
    EParameter result = null;
    String its_name = null;

    for (int i = 0; i < current_scope.size(); i++) {
      Object something = current_scope.elementAt(i);

      if (something instanceof EParameter) {
        its_name = ((EParameter) something).getName(null);

        // System.out.println("#_# current name: " + its_name + ", name: " + name);
        if (its_name.equalsIgnoreCase(name)) {
          // result = (ECtVariable)something;
          // return result;
          return true;

          // break;
        }
      } else {
        // System.out.println("#_# not variable: " + something);
      }
    }

    // get super-scopes
    // System.out.println("#_# Scope stack size: " + scope_stack.size());

		for (int j = scope_stack.size()-1; j >= 0; j--) {
//    for (int j = 0; j < scope_stack.size(); j++) {
      Vector the_scope = (Vector) scope_stack.elementAt(j);

      // System.out.println("#_# Scope stack index: " + j);
      for (int i = 0; i < the_scope.size(); i++) {
        Object something = the_scope.elementAt(i);

        if (something instanceof EParameter) {
          its_name = ((EParameter) something).getName(null);

          // System.out.println("#_# current name: " + its_name + ", name: " + name);
          if (its_name.equalsIgnoreCase(name)) {
            // result = (ECtVariable)something;
            // return result;
            return true;

            // break;
          }
        } else {
          // System.out.println("#_# not variable: " + something);
        }
      }
    }

    // return result;
    return false;
  }

	/*
			here, fd is the inner function, we need the function definition of its parent outer function
			so, find the inner_declaration of fd, and its scope attribute will point to the outer function
	
	*/
	static EFunction_definition getParentFunctionDefinition(EFunction_definition fd) throws SdaiException {

      Aggregate ia = model.getEntityExtentInstances(EInner_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
        EEntity inst = (EEntity) dec.getDefinition(null);
				if (inst == fd) {
					return (EFunction_definition)((EInner_declaration)dec).getScope(null);
				}
			}
			return null;
	}

	static EProcedure_definition getParentProcedureDefinition(EProcedure_definition fd) throws SdaiException {

      Aggregate ia = model.getEntityExtentInstances(EInner_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
        EEntity inst = (EEntity) dec.getDefinition(null);
				if (inst == fd) {
					return (EProcedure_definition)((EInner_declaration)dec).getScope(null);
				}
			}
			return null;
	}




	static EAlgorithm_definition getParentFunctionProcedureDefinition(EAlgorithm_definition fd) throws SdaiException {
			
			if (fd == null) {
				return null;
			}
			SdaiModel modelx = fd.findEntityInstanceSdaiModel(); 
      Aggregate ia = modelx.getEntityExtentInstances(EInner_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
        if (!(dec instanceof EAlgorithm_declaration)) continue;
        EEntity inst = (EEntity) dec.getDefinition(null);
				if (inst == fd) {
// System.out.println("parent function delraration:  " + dec); 
					return (EAlgorithm_definition)((EInner_declaration)dec).getScope(null);
				}
			}
			return null;
	}

	static EEntity getParentFunctionProcedureRuleDefinition(EAlgorithm_definition fd) throws SdaiException {
			
			if (fd == null) {
				return null;
			}
			SdaiModel modelx = fd.findEntityInstanceSdaiModel(); 
      Aggregate ia = modelx.getEntityExtentInstances(EInner_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
        if (!(dec instanceof EAlgorithm_declaration)) continue;
// System.out.println("candidate declaration:  " + dec); 
        EEntity inst = (EEntity) dec.getDefinition(null);
				if (inst == fd) {
// System.out.println("parent function declaration:  " + dec); 
					return ((EInner_declaration)dec).getScope(null);
				}
			}
			return null;
	}

	static EEntity getParentFunctionProcedureRuleDefinitionX(EEntity fd) throws SdaiException {

// System.out.println("<CONST-STUFF-02> fd: " + fd);			
			if (fd == null) {
				return null;
			}
			SdaiModel modelx = fd.findEntityInstanceSdaiModel(); 
      Aggregate ia = modelx.getEntityExtentInstances(EInner_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
// System.out.println("<CONST-STUFF-03> dec: " + dec);			
//        if (!(dec instanceof EAlgorithm_declaration)) continue;
        if (!(dec instanceof EConstant_declaration)) continue;
// System.out.println("candidate declaration:  " + dec); 
        EEntity inst = (EEntity) dec.getDefinition(null);
// System.out.println("<CONST-STUFF-04> inst: " + inst);			
				if (inst == fd) {
// System.out.println("parent function declaration:  " + dec); 
					return ((EInner_declaration)dec).getScope(null);
				}
			}
			return null;
	}






  static boolean findIfVariable(String name) {
    // boolean findIfVariable(String name) throws SdaiException {
    ECtVariable result = null;
    String its_name = null;

    // only partially implemented so far, other types must be supported, and also not only current_scope, but also from the scope stack.
    for (int i = 0; i < current_scope.size(); i++) {
      Object something = current_scope.elementAt(i);

      if (something instanceof ECtVariable) {
        its_name = ((ECtVariable) something).getName();

        // System.out.println("#_# current name: " + its_name + ", name: " + name);
        if (its_name.equalsIgnoreCase(name)) {
          // result = (ECtVariable)something;
          // return result;
          return true;

          // break;
        }
      } else {
        // System.out.println("#_# not variable: " + something);
      }
    }

    // get super-scopes
    // System.out.println("#_# Scope stack size: " + scope_stack.size());

		for (int j = scope_stack.size()-1; j >= 0; j--) {
//    for (int j = 0; j < scope_stack.size(); j++) {
      Vector the_scope = (Vector) scope_stack.elementAt(j);

      // System.out.println("#_# Scope stack index: " + j);
      for (int i = 0; i < the_scope.size(); i++) {
        Object something = the_scope.elementAt(i);

        if (something instanceof ECtVariable) {
          its_name = ((ECtVariable) something).getName();

          // System.out.println("#_# current name: " + its_name + ", name: " + name);
          if (its_name.equalsIgnoreCase(name)) {
            // result = (ECtVariable)something;
            // return result;
            return true;

            // break;
          }
        } else {
          // System.out.println("#_# not variable: " + something);
        }
      }
    }

    // return result;
    return false;
  }

  static boolean isEntityConstructor() {


    // initialization of these global variables when done in SimpleFactor does not work correctly
    // for the cases when SimpleFactor non-terminals are nested
   
		if (flag_oc) {
		  global_name1_global = Compiler2.getToken(1).image;
		  global_name2_global = Compiler2.getToken(2).image;
		  global_name3_global = Compiler2.getToken(3).image;
		} else {
		  global_name1_global = Compiler2.getToken(1).image.toLowerCase();
		  global_name2_global = Compiler2.getToken(2).image.toLowerCase();
		  global_name3_global = Compiler2.getToken(3).image.toLowerCase();
		}
		global_kind1_global = Compiler2.getToken(1).kind;
		global_kind2_global = Compiler2.getToken(2).kind;
		global_kind3_global = Compiler2.getToken(3).kind;

    if (global_kind1_global != Compiler2Constants.SIMPLE_ID) {
			//System.out.println("EntityConstructor - false, no simple id");
      return false;
    }

    if (global_kind2_global != Compiler2Constants.LPAREN) {
			//System.out.println("EntityConstructor - false, no parenthese");
      return false;
    }

    String name = global_name1_global;

    //		if (parser_pass != 5)  return false;
    if (findIfVariable(name)) {
			//System.out.println("EntityConstructor - false: " + name);
      return false;
    }

    try {
      Aggregate ia = model.getEntityExtentInstances(EEntity_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
        EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);
        String instance_name = inst.getName(null);

        if (instance_name.equalsIgnoreCase(name)) { // found! return true

					//System.out.println("1 EntityConstructor - true: " + name);
          return true;
        } else if (dec instanceof EInterfaced_declaration) {
          if (((EInterfaced_declaration) dec).testAlias_name(null)) {
            instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

            if (instance_name.equalsIgnoreCase(name)) { // found! return true

							//System.out.println("2 EntityConstructor - true: " + name);
              return true;
            }
          }
        }
      } // while
			//System.out.println("3 EntityConstructor - false: " + name);
    } catch (SdaiException e) {
      System.out.println("SdaiException in isEntityReference, name: " + name + ", description: " + 
                         e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    return false;
  }

  static boolean isEntityReference() {

    // initialization of these global variables when done in SimpleFactor does not work correctly
    // for the cases when SimpleFactor non-terminals are nested
   
		if (flag_oc) {
		  global_name1_global = Compiler2.getToken(1).image;
		  global_name2_global = Compiler2.getToken(2).image;
		  global_name3_global = Compiler2.getToken(3).image;
		} else {
		  global_name1_global = Compiler2.getToken(1).image.toLowerCase();
		  global_name2_global = Compiler2.getToken(2).image.toLowerCase();
		  global_name3_global = Compiler2.getToken(3).image.toLowerCase();
		}
		global_kind1_global = Compiler2.getToken(1).kind;
		global_kind2_global = Compiler2.getToken(2).kind;
		global_kind3_global = Compiler2.getToken(3).kind;

    if (global_kind1_global != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    String name = global_name1_global;

    if (parser_pass != 5) {
      return false;
    }

    if (findIfVariable(name)) {
      return false;
    }

    try {
      Aggregate ia = model.getEntityExtentInstances(EEntity_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
        EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);
        String instance_name = inst.getName(null);

        if (instance_name.equalsIgnoreCase(name)) { // found! return true

          return true;
        } else if (dec instanceof EInterfaced_declaration) {
          if (((EInterfaced_declaration) dec).testAlias_name(null)) {
            instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

            if (instance_name.equalsIgnoreCase(name)) { // found! return true

              return true;
            }
          }
        }
      } // while
    } catch (SdaiException e) {
      System.out.println("SdaiException in isEntityReference, name: " + name + ", description: " + 
                         e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    return false;
  }

  static int whatsAhead() {
		//    System.out.println("XX IN whatsAhead method");

    //		global_kind1_global = Compiler2.getToken(1).kind;
    //	  global_name1_global = Compiler2.getToken(1).image;
    //		global_kind2_global = Compiler2.getToken(2).kind;
    //	  global_name2_global = Compiler2.getToken(2).image;
    //		global_kind3_global = Compiler2.getToken(3).kind;
    //	  global_name3_global = Compiler2.getToken(3).image;
    int token_kind;
    String token_image;

    for (int i = 1;; i++) {
      token_kind = Compiler2.getToken(i).kind;
      token_image = Compiler2.getToken(i).image;
//      System.out.println("RR##XX token kind: " + token_kind + ", image: " + token_image);

      if (token_kind != Compiler2Constants.SIMPLE_ID) {
      } else {
      }

      break;
    }

    return 1;
  }

/*
  static boolean isDomainRule() {
    int token_kind;
    String token_image;
      token_kind = Compiler2.getToken(1).kind;
      token_image = Compiler2.getToken(1).image;
      System.out.println("<isDomainRule> token kind: " + token_kind + ", image: " + token_image);

		return true;
	}
*/

	static String checkFunctionParameterArgumentCompatibility(EFunction_definition fd, int built_in_id, Vector arguments, Vector argument_types)   throws SdaiException {
		String result = null;
		int parameter_count;
		int argument_count;
		
		if (arguments.size() != argument_types.size()) {
			// internal error - we were not able to check, so leave true.
			System.out.println("Express Compiler INTERNAL ERROR 01 while checking argument-parameter compatibility: Function: " + fd + ", built-in: " + built_in_id + ", arguments: " + arguments + ", argument types: " + argument_types);
			return null;
		}
		
		// if built_in_id = -1 and fd non-null, then not a built in function
		switch (built_in_id) {
				case -1: // not a built in function
					// as declared:  
					// 		parameters : LIST[0:?] OF parameter;
					// 				parameter_type : data_type;
					AParameter parameters = null;
					if (fd.testParameters(null)) {
						parameters = fd.getParameters(null);
					}
//System.out.println("parameters: " + parameters);
					parameter_count = parameters.getMemberCount();
					argument_count = argument_types.size();
//System.out.println("parameter count: " + parameter_count);					
//System.out.println("argument count: " + argument_count);					
					if (argument_count != parameter_count) {
						// must report an error in express, but how? Perhaps return int and report by its type
						// System.out.println("The number of arguments is not the same as declared number of parameters in function " + fd.getName(null));
						// return false;  // perhaps return some int number to signal to the parser that a specific error message has to be issued
						return ("The number of arguments is not the same as declared number of parameters in function " + fd.getName(null));
					}
					//System.out.println("One parameter: " + a_parameter);
					for (int i = 0; i < argument_types.size(); i++) {
						EParameter a_parameter = parameters.getByIndex(i+1);
						EData_type parameter_type = null;
						if (a_parameter.testParameter_type(null)) {
							parameter_type = a_parameter.getParameter_type(null);
						} else { // something wrong, so we cannot check
							System.out.println("EXPRESS COMPILER INTERNAL ERROR - Type Tracking 02");
							return null;
						}					
						//System.out.println("One parameter: " + parameter_type);
						Object an_argument_type = argument_types.get(i);
						//System.out.println("One argument: " + an_argument_type);
						if ((parameter_type instanceof EEntity_definition) && (an_argument_type instanceof EEntity_definition)) {
							 // check this simple case when both the parameter and argument type are entities
							 // if parameter and argument entities have no commont supertype
							 boolean is_subtype = isSubtype((EEntity_definition)an_argument_type, (EEntity_definition)parameter_type);
							 if (is_subtype) {
							 	return null;
							 }
							 is_subtype = isSubtype((EEntity_definition)parameter_type, (EEntity_definition)an_argument_type);
							 if (is_subtype) {
							 	// can be checked only at runtime, so better to allow
							 	return null;
							 } else {
							 	 // not the same entity
							 	 // argument is not a subtype of parameter
							 	 // and even parameter is not a subtype of argument
							 	 // do we need to allow some over cases where entities are in the same tree? 
								 //System.out.println("Wrong argument type " + an_argument_type + " in function " + fd.getName(null) + " call, expected: " + parameter_type);
								 //return false;
								 return ("Wrong argument type " + ((EEntity_definition)an_argument_type).getName(null) + " in function call of function " + fd.getName(null) + ", expected: " + ((EEntity_definition)parameter_type).getName(null));
							 }
						} else
//						if ((parameter_type instanceof _st_integer) && (an_argument_type instanceof EEntity_definition)) {
						if ((parameter_type instanceof EInteger_type) && (an_argument_type instanceof EEntity_definition)) {
								 return ("Wrong argument type " + ((EEntity_definition)an_argument_type).getName(null) + " in function call of function " + fd.getName(null) + ", expected: INTEGER");
						}
					} // for
		
				case  0: // unknown built in function, internal error
					break;
				default: // must be an internal error.
					break;
		}

		return result;
	}


	static boolean isRedeclared_attrCompatible(EAttribute redeclaring, EAttribute redeclared)  throws SdaiException {
		
		/*
		 at least start making some checks
		 if the type is entity, see that redeclared is the same or subtype entity
		getDomain - base_type for explicit and derived
		getDomain - entity_definition for inverse
	  
	  TYPE base_type = SELECT
		(
			simple_type,
	 		aggregation_type,
	 		named_type,
	 		parameter);
		END_TYPE;
		
		explicit -> explicit
		explicit -> derived
		derived -> derived
		inverse -> inverse




### part 11, 9.2.3.4 #####


ï¿½ the data type of the attribute may be changed to a specialization of the original data type
(see 9.2.7);
EXAMPLE 1 A number data type attribute may be changed to an integer data type or to a
real data type.
ï¿½ if the original data type of the attribute is a defined data type based on a select, the type
may be changed to either another select, which identifies a subset or specialization of items
from the original select list, or a specialization of one of the items from the original select
list;
ï¿½ an optional attribute in the supertype may be changed to a mandatory attribute in the


a) The data type in the redeclaration shall be the same as, or a specialization of, the data
type of the attribute declared in the supertype. The rules for specialization in 9.2.7 apply.



#### 9.2.7 Specialization ###

A specialization is a more constrained form of an original declaration. The following are the
defined specializations:
ï¿½ a subtype entity is a specialization of any of its supertypes;
ï¿½ an entity is a specialization of a generic entity;
ï¿½ an extensible generic entity select is a specialization of a generic entity;
ï¿½ a select data type that contains only entity data types is a specialization of a generic entity;
ï¿½ the aggregation data types are specializations of aggregate;
ï¿½ a select of a, b, c is a specialization of a select of d, e, f if a, b, c are specializations of
d, e, f;
ï¿½ a select of a, b, c is a specialization of a supertype if a, b, c are subtypes of the supertype;
ï¿½ integer and real are both specializations of number;
ï¿½ integer is a specialization of real;
ï¿½ boolean is a specialization of logical;
ï¿½ LIST OF UNIQUE item is a specialization of LIST OF item;
ï¿½ ARRAY OF UNIQUE item is a specialization of ARRAY OF item;
ï¿½ ARRAY OF item is a specialization of ARRAY OF OPTIONAL item;
ï¿½ SET OF item is a specialization of BAG OF item;
ï¿½ letting AGG stand for one of array, bag, list or set then AGG OF item is a specialization
of AGG OF original provided that item is a specialization of original;
ï¿½ letting AGG stand for one of bag, list or set then AGG [b:t] is a specialization of AGG [l:u]
provided that b <= t and l <= b <= u and l <= t <= u;
ï¿½ letting BSR stand for one of the data types binary, string or real then BSR (length) is
a specialization of BSR;
ï¿½ BSR (short) is a specialization of BSR (long) provided that short is less than long;
ï¿½ a binary which uses the keyword fixed is a specialization of variable length binary;
ï¿½ a string which uses the keyword fixed is a specialization of variable length string;
ï¿½ a constructed data type that is based on an extensible constructed data type is a specialization
of that extensible constructed data type;
ï¿½ a defined data type is a specialization of the underlying data type used to declare the defined
data type.


comparing two selects:

1. if the 1st is extensible and the 2nd is based_on it - ok

2. if the 1st is select, the 2nd may be:
   a) select that contains subset of the elements from 1. (does it include extensible selects with explicitly undefined subsets?)
   b) select that contains specializations of elements from 1
   c) a mixture of the two above, I think
   d) non-select - one element from 1 orits specialization
   
2a if the 1st is extensible select, the 2nd - select based_on the 1st   

3. when 2nd is select and yet the 1st is not:
	 a) 1st is generic entity - 2nd - select that contains only entities
	 b) 1st is generic entity - 2nd - extensible generic entity select
	 c) 1st - an entity, 2nd- select that contains subtypes of 1st.
	 
4. what if 1st/2nd are extensible selects?	 
		
		
So how to deal with them?


1.   1st is select, 2nd - non-select
			a) if select is extensible, calculate selection list elements taking into account based_on types with elements interfaced into the schema
			b) if not extensible, get the elements, skipping/simplifying a)
			c) if 2nd is an entity - check if the list of 1st contains it, or its (also indirect) supertype
         if 2nd is an aggregate of entity - see if the list of 1st contains it (as defined type) or an aggregate of supertypes 
	
2.  1st entity, 2nd - select
    see if all selection list elements are the entity from 1 or its subtypes.

3.  1st select - 2nd - select
    if extensible, calculated lists for the current schema
    see if each entity element of 2nd is also element of 1st, or has supertype entity in 1st, or has an element - select in 1st which
    has it or its supertype in its list, and so on.
    If true for all elements of 2nd - then ok, otherwise - no.     

		
Obvious incompatibles:
1st aggregate  - 2nd entity
1st entity     - 2nd aggregate
1st defined type non-select  - 2nd entity
1st entity - 2nd defined type non-select		
		
		
		*/

	 if (parser_pass != 5) {
	 	 return true;
	 }
	 
		if (redeclared == null) {
			printWarningMsg("" + " - redeclared attribute NULL when checking domain compatibility  - may be an INTERNAL error (1)", null, true);
	 		return true;
		}
		if (redeclaring == null) {
			printWarningMsg("" + " - redeclaring attribute NULL when checking domain compatibility  - may be an INTERNAL error (1)", null, true);
	 		return true;
		}
	

		EEntity redeclaring_domain = null;
		EEntity redeclared_domain  = null;
		EEntity original_redeclaring_domain = null;
		
		if ((redeclaring instanceof EExplicit_attribute) && (!(redeclared instanceof EExplicit_attribute))) {
			printErrorMsg("" + redeclaring.getName(null) + " - attempt to redeclare a non-explicit attribute as explicit, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		}
		if ((redeclaring instanceof EInverse_attribute) && (!(redeclared instanceof EInverse_attribute))) {
			printErrorMsg("" + redeclaring.getName(null) + " - attempt to redeclare a non-inverse attribute as inverse, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		}
		if ((redeclaring instanceof EDerived_attribute) && (redeclared instanceof EInverse_attribute)) {
			printErrorMsg("" + redeclaring.getName(null) + " - attempt to redeclare an inverse attribute as derived, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		}
		
		if ((redeclaring instanceof EExplicit_attribute) || (redeclaring instanceof EDerived_attribute)) {
		
			if (redeclaring instanceof EExplicit_attribute) {
				if (((EExplicit_attribute)redeclaring).testDomain(null)) {
					redeclaring_domain = ((EExplicit_attribute)redeclaring).getDomain(null);
				}
			} else
			if (redeclaring instanceof EDerived_attribute) {
				if (((EDerived_attribute)redeclaring).testDomain(null)) {
					redeclaring_domain = ((EDerived_attribute)redeclaring).getDomain(null);
				}
			}
			if (redeclared instanceof EExplicit_attribute) {
				if (((EExplicit_attribute)redeclared).testDomain(null)) {
					redeclared_domain = ((EExplicit_attribute)redeclared).getDomain(null);
				}
			} else
			if (redeclared instanceof EDerived_attribute) {
				if (((EDerived_attribute)redeclared).testDomain(null)) {
					redeclared_domain = ((EDerived_attribute)redeclared).getDomain(null);
				}
			}
		} else 
		if (redeclaring instanceof EInverse_attribute) {
			if (((EInverse_attribute)redeclaring).testDomain(null)) {
				redeclaring_domain = ((EInverse_attribute)redeclaring).getDomain(null);
			}			
			if (((EInverse_attribute)redeclared).testDomain(null)) {
				redeclared_domain = ((EInverse_attribute)redeclared).getDomain(null);
			}			
		}

		// let's get to the underlying types of (possibly nested) defined types

		EEntity domain = null;
		EEntity domain2 = null;
		boolean done_something = false;
		
		original_redeclaring_domain = redeclaring_domain;


		if (redeclaring_domain != null) {
			domain = redeclaring_domain;
			done_something = true;
			while (done_something) {	
				done_something = false;
				if (domain instanceof EDefined_type) {
					if (((EDefined_type)domain).testDomain(null)) {
						domain2 = ((EDefined_type)domain).getDomain(null);
						done_something = true;
						if (domain == domain2) {
							// ERROR - this kind of error in express has to be caught, but not here, hopefully, because here only part of such errors can be detected
							// temporrary message, if/when done elsewhere - remove it.
							printErrorMsg("" + ((EDefined_type)domain).getName(null) + " - defined type has itself as its underlying type", null, true);
						}
						domain = domain2;
					}
				}
			}
			redeclaring_domain = domain;
		} else {
			if (parser_pass < 5) {
				printWarningMsg("" + redeclaring.getName(null) + " - the domain of the attribute unresolved - may be an INTERNAL error (1)", null, true);
			}
			return true;
		}
		if (redeclared_domain != null) {
			domain = redeclared_domain;
			done_something = true;
			while (done_something) {	
				done_something = false;
				if (domain instanceof EDefined_type) {
					if (((EDefined_type)domain).testDomain(null)) {
						domain2 = ((EDefined_type)domain).getDomain(null);
						done_something = true;
						if (domain == domain2) {
							// ERROR - this kind of error in express has to be caught, but not here, hopefully, because here only part of such errors can be detected
							// temporrary message, if/when done elsewhere - remove it.
							printErrorMsg("" + ((EDefined_type)domain).getName(null) + " - defined type has itself as its underlying type", null, true);
						}
						domain = domain2;
						if (domain == original_redeclaring_domain) {
							// wrong direction of specialization
//tmp							printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, wrong direction of specialization: " + redeclaring.getParent(null).getName(null), null, true);
//tmp							return false;
						}
					}
				}
			}
			redeclared_domain = domain;
		} else {
			if (parser_pass < 5) {
				printWarningMsg("" + redeclared.getName(null) + " - the domain of the attribute unresolved - may be an INTERNAL error (2)", null, true);
			}
			return true;
		}
  
		if (redeclaring_domain == redeclared_domain) {
			return true;
		}

		if ((redeclaring_domain instanceof EEntity_definition) && (redeclared_domain instanceof EEntity_definition)) {
			HashSet processed = new HashSet();
			boolean result = recurseSupertypes((EEntity_definition)redeclaring_domain, (EEntity_definition)redeclared_domain, (EEntity_definition)redeclaring_domain, processed);
			if (!result) {
				printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, both are entities, redeclaring is not the same nor a subtype of redeclared, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			}
			return result;
		} else 
		if ((redeclaring_domain instanceof EAggregation_type) && 
			(!((redeclared_domain instanceof EAggregation_type) || (redeclared_domain instanceof ESelect_type)))) {
			// redeclared select - redeclaring aggregate may be when this aggregate is in selection list
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, an aggregate is redeclaring a non-aggregate, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else 

		// this version allowed select, but part 11 does not seem to allow it.
//		if ((!((redeclaring_domain instanceof EAggregation_type) || (redeclaring_domain instanceof ESelect_type))) && 
		if ((!(redeclaring_domain instanceof EAggregation_type)) && 
			(redeclared_domain instanceof EAggregation_type)) {
			// redeclared aggregate - redeclaring select is a dubious case, not sure if allowed, even if the select holds the same aggregate and/or aggregates with specialized elements
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-aggregate is redeclaring an aggregate, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else 
		if ((redeclared_domain instanceof ENumber_type) && ((redeclaring_domain instanceof EInteger_type) || (redeclaring_domain instanceof EReal_type))) {
			// perhaps this check partially duplicates the one farther below
			return true;
		} else 
		if ((redeclared_domain instanceof EAggregation_type) && (redeclaring_domain instanceof EAggregation_type)) {
			// to do later
			// compare element types, a lot of cases, not only entities
			// but for now, just add a temporary fix for entities directly
			EEntity redeclared_element_domain = null;
			EEntity redeclaring_element_domain = null;
			if (((EAggregation_type)redeclared_domain).testElement_type(null)) {
		  		redeclared_element_domain = ((EAggregation_type)redeclared_domain).getElement_type(null);
			}
			if (((EAggregation_type)redeclaring_domain).testElement_type(null)) {
		  		redeclaring_element_domain = ((EAggregation_type)redeclaring_domain).getElement_type(null);
			}
			if (redeclared_element_domain == null) {
				printWarningMsg("" + redeclared.getName(null) + " - the domain of the attribute is an aggregate with element type unresolved - may be an INTERNAL error (1)", null, true);
				return true;
			}
			if (redeclaring_element_domain == null) {
				printWarningMsg("" + redeclaring.getName(null) + " - the domain of the attribute is an aggregate with element type unresolved - may be an INTERNAL error (2)", null, true);
				return true;
			}
			if (redeclared_element_domain == redeclaring_element_domain) {
				return true;
			}
			if ((redeclared_element_domain instanceof EEntity_definition) && (redeclaring_element_domain instanceof EEntity_definition)) {
				HashSet processed = new HashSet();
				boolean result = recurseSupertypes((EEntity_definition)redeclaring_element_domain, (EEntity_definition)redeclared_element_domain, (EEntity_definition)redeclaring_element_domain, processed);
				if (!result) {
					printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, both are aggregates of entities, redeclaring element is not the same nor a subtype of redeclared, in entity: " + redeclaring.getParent(null).getName(null), null, true);
				}
				return result;
			} else {
				// to implement
				return true;
			}
		} else
		if ((redeclared_domain instanceof ESelect_type) || (redeclaring_domain instanceof ESelect_type)) {
			// to do later
			return true;
		} else
		if ((redeclared_domain instanceof EInteger_type) && (!(redeclaring_domain instanceof EInteger_type))) {	
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-INTEGER is redeclaring an INTEGER, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof EReal_type) && (!((redeclaring_domain instanceof EReal_type) || (redeclaring_domain instanceof EInteger_type)))) {	
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-REAL is redeclaring a REAL, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof EString_type) && (!(redeclaring_domain instanceof EString_type))) {	
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-STRING is redeclaring a STRING, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof EBinary_type) && (!(redeclaring_domain instanceof EBinary_type))) {	
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-BINARY is redeclaring a BINARY, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof EBoolean_type) && (!(redeclaring_domain instanceof EBoolean_type))) {	
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-BOOLEAN is redeclaring a BOOLEAN, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof ELogical_type) && (!((redeclaring_domain instanceof ELogical_type) || (redeclaring_domain instanceof EBoolean_type)))) {	
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-LOGICAL and non-BOOLEAN is redeclaring a LOGICAL, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof ENumber_type) && (!((redeclaring_domain instanceof ENumber_type) || (redeclaring_domain instanceof EReal_type) || (redeclaring_domain instanceof EInteger_type)))) { 
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-NUMBER, non-REAL, and non-INTEGER is redeclaring a NUMBER, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof EEntity_definition) && (!((redeclaring_domain instanceof EEntity_definition) || (redeclaring_domain instanceof ESelect_type)))) {
			// may be duplicating other checks, but so what
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-entity and non-select is redeclaring an entity, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else
		if ((redeclared_domain instanceof EAggregation_type) && (!((redeclaring_domain instanceof EAggregation_type) || (redeclaring_domain instanceof ESelect_type)))) {
			// may be duplicating, besides, select case is dubious
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-aggregate and non-select is redeclaring an aggregate, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
    } else
    if ((redeclared_domain instanceof EEnumeration_type) && (!(redeclaring_domain instanceof EEnumeration_type))) {
			// it is possible to redeclare extensible enumeration with based_on enumeration, and any enumeration by itself
			printErrorMsg("" + redeclaring.getName(null) + " - incompatible types of redeclaring and redeclared attributes, a non-enumeration is redeclaring an enumeration, in entity: " + redeclaring.getParent(null).getName(null), null, true);
			return false;
		} else {	


      			
 			// to do later, when no cases left unchecked, then perhaps false can be returned
			return true;
		} 	
	
	
	}


  static boolean isSpecifiedEntityOfRedeclaredAttributeIsSupertype(EEntity_definition current, EEntity_definition specified) throws SdaiException {
		HashSet processed = new HashSet();
		return recurseSupertypes(current, specified, current, processed);
  }


	static boolean isInverted_attrCompatible(EEntity_definition ed, EExplicit_attribute ar) throws SdaiException {
// System.out.println("_-<=>-_ 0: entity: " + ed.getName(null).toLowerCase() + ", inverted attribute: " + ar.getName(null).toLowerCase() + ", pass: " + parser_pass);	
		EEntity domain = null;
		EEntity domain2 = null;
		if (!(ar.testDomain(null))) {
			// perhaps too early to test
// System.out.println("_-<=>-_ 1:  domain unset");	
			return true; 
		}
		domain = ar.getDomain(null);
// System.out.println("_-<=>-_ 2:  domain: " + domain);	

		boolean done_something = true;
		while (done_something) {	
// System.out.println("_-<=>-_ 3: in the loop");	
			done_something = false;
			if (domain instanceof EDefined_type) {
				if (((EDefined_type)domain).testDomain(null)) {
					domain2 = ((EDefined_type)domain).getDomain(null);
					done_something = true;
// System.out.println("_-<=>-_ 4: in the loop - defined_type, domain2: " + domain2);	
				} else {
					// too early?
// System.out.println("_-<=>-_ 1-2:  domain unset");	
					return true;
				}
			} else
			if (domain instanceof EAggregation_type) {
	  		if (((EAggregation_type)domain).testElement_type(null)) {
		  		domain2 = ((EAggregation_type)domain).getElement_type(null);
					done_something = true;
// System.out.println("_-<=>-_ 5: in the loop - aggregate, domain2: " + domain2);	
				} else {
					// too early?
// System.out.println("_-<=>-_ 1-3:  domain unset");	
					return true;
				}	
			}
			if (done_something) {
				domain = domain2;
// System.out.println("_-<=>-_ 6: in the loop - done something: " + domain);	
			}
		} // while
		

// System.out.println("_-<=>-_ 7: after the loop, domain: " + domain);	
		if (domain instanceof EExtensible_select_type) {
			return true;
		}

		if (domain instanceof ESelect_type) {
// System.out.println("_-<=>-_ 8: select");	
			// if (((ESelect_type)domain).testSelections(null)) {
				ANamed_type selections = getSelections((ESelect_type)domain);
				for (int i = 1; i < selections.getMemberCount() + 1; i++) {
					ENamed_type element = (ENamed_type)selections.getByIndexEntity(i);
// System.out.println("_-<=>-_ 9: going through selections, element: " + element);	

					boolean result = isInverted_attrCompatibleRecurse(element, ed, ar);									
					if (result) {
						return true;
					}					
				} 
				// gone through all the select elements, and still none is compatible
// System.out.println("_-<=>-_ 13: none of select elements suitable");	
				return false;	
			// } else {
				// too early?
// System.out.println("_-<=>-_ 1-4:  domain unset");	
				// return true;
			// }
		} else 
		if (domain instanceof EEntity_definition) {
			EEntity_definition ed2 = (EEntity_definition)domain;
	  	if (ed == ed2) {
// System.out.println("_-<=>-_ 14: exact entity");	
	  		return true;
			}
// System.out.println("_-<=>-_ 15: entity, checking supertypes - current: " + ed.getName(null) + ", inverted type: " + ed2.getName(null));	
			HashSet processed = new HashSet();
			return recurseSupertypes(ed, ed2, ed, processed);
		} else {
			// cannot warn about everything
// System.out.println("_-<=>-_ 16 something else, domain: " + domain);	
  		return true;
		}
	}


	static boolean isInverted_attrCompatibleRecurse(EEntity domain, EEntity_definition ed, EExplicit_attribute ar) throws SdaiException {
// System.out.println("_-<=>-_ 0: entity: " + ed.getName(null).toLowerCase() + ", inverted attribute: " + ar.getName(null).toLowerCase() + ", pass: " + parser_pass);	
		// EEntity domain = null;
		EEntity domain2 = null;

		boolean done_something = true;
		while (done_something) {	
// System.out.println("_-<=>-_ 3: in the loop");	
			done_something = false;
			if (domain instanceof EDefined_type) {
				if (((EDefined_type)domain).testDomain(null)) {
					domain2 = ((EDefined_type)domain).getDomain(null);
					done_something = true;
// System.out.println("_-<=>-_ 4: in the loop - defined_type, domain2: " + domain2);	
				} else {
					// too early?
// System.out.println("_-<=>-_ 1-2:  domain unset");	
					return true;
				}
			} else
			if (domain instanceof EAggregation_type) {
	  		if (((EAggregation_type)domain).testElement_type(null)) {
		  		domain2 = ((EAggregation_type)domain).getElement_type(null);
					done_something = true;
// System.out.println("_-<=>-_ 5: in the loop - aggregate, domain2: " + domain2);	
				} else {
					// too early?
// System.out.println("_-<=>-_ 1-3:  domain unset");	
					return true;
				}	
			}
			if (done_something) {
				domain = domain2;
// System.out.println("_-<=>-_ 6: in the loop - done something: " + domain);	
			}
		} // while
		

// System.out.println("_-<=>-_ 7: after the loop, domain: " + domain);	
		if (domain instanceof EExtensible_select_type) {
			return true;
		}

		if (domain instanceof ESelect_type) {
// System.out.println("_-<=>-_ 8: select");	
			// if (((ESelect_type)domain).testSelections(null)) {
				ANamed_type selections = getSelections((ESelect_type)domain);
				for (int i = 1; i < selections.getMemberCount() + 1; i++) {
					ENamed_type element = (ENamed_type)selections.getByIndexEntity(i);
// System.out.println("_-<=>-_ 9: going through selections, element: " + element);	

					boolean result = isInverted_attrCompatibleRecurse(element, ed, ar);									
					if (result) {
						return true;
					}					
				} 
				// gone through all the select elements, and still none is compatible
// System.out.println("_-<=>-_ 13: none of select elements suitable");	
				return false;	
			// } else {
				// too early?
// System.out.println("_-<=>-_ 1-4:  domain unset");	
				// return true;
			// }
		} else 
		if (domain instanceof EEntity_definition) {
			EEntity_definition ed2 = (EEntity_definition)domain;
	  	if (ed == ed2) {
// System.out.println("_-<=>-_ 14: exact entity");	
	  		return true;
			}
// System.out.println("_-<=>-_ 15: entity, checking supertypes - current: " + ed.getName(null) + ", inverted type: " + ed2.getName(null));	
			HashSet processed = new HashSet();
			return recurseSupertypes(ed, ed2, ed, processed);
		} else {
			// cannot warn about everything
// System.out.println("_-<=>-_ 16 something else, domain: " + domain);	
  		return true;
		}
	}



	static boolean isInverted_attrCompatible_prev(EEntity_definition ed, EExplicit_attribute ar) throws SdaiException {
// System.out.println("_-<=>-_ 0: entity: " + ed.getName(null).toLowerCase() + ", inverted attribute: " + ar.getName(null).toLowerCase() + ", pass: " + parser_pass);	
		EEntity domain = null;
		EEntity domain2 = null;
		if (!(ar.testDomain(null))) {
			// perhaps too early to test
// System.out.println("_-<=>-_ 1:  domain unset");	
			return true; 
		}
		domain = ar.getDomain(null);
// System.out.println("_-<=>-_ 2:  domain: " + domain);	

		boolean done_something = true;
		while (done_something) {	
// System.out.println("_-<=>-_ 3: in the loop");	
			done_something = false;
			if (domain instanceof EDefined_type) {
				if (((EDefined_type)domain).testDomain(null)) {
					domain2 = ((EDefined_type)domain).getDomain(null);
					done_something = true;
// System.out.println("_-<=>-_ 4: in the loop - defined_type, domain2: " + domain2);	
				} else {
					// too early?
// System.out.println("_-<=>-_ 1-2:  domain unset");	
					return true;
				}
			} else
			if (domain instanceof EAggregation_type) {
	  		if (((EAggregation_type)domain).testElement_type(null)) {
		  		domain2 = ((EAggregation_type)domain).getElement_type(null);
					done_something = true;
// System.out.println("_-<=>-_ 5: in the loop - aggregate, domain2: " + domain2);	
				} else {
					// too early?
// System.out.println("_-<=>-_ 1-3:  domain unset");	
					return true;
				}	
			}
			if (done_something) {
				domain = domain2;
// System.out.println("_-<=>-_ 6: in the loop - done something: " + domain);	
			}
		} // while
		

// System.out.println("_-<=>-_ 7: after the loop, domain: " + domain);	
		if (domain instanceof EExtensible_select_type) {
			return true;
		}

		if (domain instanceof ESelect_type) {
// System.out.println("_-<=>-_ 8: select");	
			// if (((ESelect_type)domain).testSelections(null)) {
				ANamed_type selections = getSelections((ESelect_type)domain);
				for (int i = 1; i < selections.getMemberCount() + 1; i++) {
					ENamed_type element = (ENamed_type)selections.getByIndexEntity(i);
// System.out.println("_-<=>-_ 9: going through selections, element: " + element);	
					if (element instanceof EEntity_definition) {
						EEntity_definition ed2 = (EEntity_definition)element;
				  	if (ed == ed2) {
// System.out.println("_-<=>-_ 10: select element exact entity");	
				  		return true;
	  				}
// System.out.println("_-<=>-_ 11: select element entity, checking supertypes - current: " + ed.getName(null) + ", inverted type: " + ed2.getName(null));	
						HashSet processed = new HashSet();
						boolean result = recurseSupertypes(ed, ed2, ed, processed);
						if (result) {
// System.out.println("_-<=>-_ 12: select element entity, supertype found");	
							return true;
						}
					}
				} 
				// gone through all the select elements, and still none is compatible
// System.out.println("_-<=>-_ 13: none of select elements suitable");	
				return false;	
			// } else {
				// too early?
// System.out.println("_-<=>-_ 1-4:  domain unset");	
				// return true;
			// }
		} else 
		if (domain instanceof EEntity_definition) {
			EEntity_definition ed2 = (EEntity_definition)domain;
	  	if (ed == ed2) {
// System.out.println("_-<=>-_ 14: exact entity");	
	  		return true;
			}
// System.out.println("_-<=>-_ 15: entity, checking supertypes - current: " + ed.getName(null) + ", inverted type: " + ed2.getName(null));	
			HashSet processed = new HashSet();
			return recurseSupertypes(ed, ed2, ed, processed);
		} else {
			// cannot warn about everything
// System.out.println("_-<=>-_ 16 something else, domain: " + domain);	
  		return true;
		}
	}
  

	static boolean isInverted_attrCompatible_first(EEntity_definition ed, EExplicit_attribute ar) throws SdaiException {
		// ed is current entity,  ar type ed2 should be the same or its supertype 
//System.out.println("_-<=>-_ 0: entity: " + ed.getName(null) + ", inverted attribute: " + ar.getName(null) + ", pass: " + parser_pass);	
		EEntity domain = null;
		if (!(ar.testDomain(null))) {
			// perhaps too early to test
//System.out.println("_-<=>-_ 1:  domain unset");	
			return true; 
		}
		domain = ar.getDomain(null);
	  if (domain instanceof EEntity_definition) {
	  	EEntity_definition ed2 = (EEntity_definition)domain;
	  	if (ed == ed2) {
//System.out.println("_-<=>-_ 2: correct entity");	
	  		return true;
	  	}
//System.out.println("_-<=>-_ 3: entity - check if supertype - current: " + ed.getName(null) + ", attribute type: " + ed2.getName(null));	
			HashSet processed = new HashSet();
			return recurseSupertypes(ed, ed2, ed, processed);
	  } else {
//System.out.println("_-<=>-_ 4: not entity");	
	  	// perhaps an aggregate, get to the bottom of it
	  	if (domain instanceof EAggregation_type) {
//System.out.println("_-<=>-_ 5: aggregate");	
	  		EAggregation_type adomain = (EAggregation_type)domain;
	  		if (!(adomain.testElement_type(null))) {
	  			// perhaps too early
//System.out.println("_-<=>-_ 6: aggregate element unset");	
	  			return true;
	  		}
				EEntity element_type = adomain.getElement_type(null);
				if (element_type instanceof EEntity_definition) {
//System.out.println("_-<=>-_ 7: aggregate element = entity");	
			  	EEntity_definition ed2 = (EEntity_definition)element_type;
		  		if (ed == ed2) {
//System.out.println("_-<=>-_ 8: aggregate element - corret entity");	
		  			return true;
	  			}
//System.out.println("_-<=>-_ 9: aggregate element - entity - check if supertype");	
					HashSet processed = new HashSet();
					return recurseSupertypes(ed, ed2, ed, processed);
				} else {
//System.out.println("_-<=>-_ 10: aggregate element not entity: " + element_type);	
					return false;
				}
	  	} else {
//System.out.println("_-<=>-_ 11: domain - not aggregate: " + domain);	
	  	}
//System.out.println("_-<=>-_ 12: domain - false at the end");	
	  	return false;
	  }

	} 


	static boolean checkCyclicSupertypes(EEntity_definition ed, EEntity_definition super_ed) throws SdaiException  {
		// returns true if ok, false, if a cyclic supertype found

						HashSet processed = new HashSet();
						boolean result = recurseSupertypes2(super_ed, null, ed, processed);
						if (result) {
							return false;
						} else {
							return true;
						}


	}

	static boolean recurseSupertypes2(EEntity_definition current_entity, EEntity_definition supertype_entity, EEntity_definition start_entity, HashSet processed) throws SdaiException {
		
		boolean result = false;

		if (!(processed.add(current_entity))) {
			// repeated inheritance, already done
			return false;
		}
	
//		AEntity_definition supertypes = current_entity.getSupertypes(null);
		AEntity_definition supertypes = (AEntity_definition)current_entity.getSupertypes(null);
  	SdaiIterator isuper = supertypes.createIterator();

  	while (isuper.next()) {
  		EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(isuper);
//			pw.println("\t\t-- recursing supertypes: from " + arm_entity.getName(null) + " to " + supertype.getName(null));

			// NOTE - noticed later - should it be recurseSupertypes2 ??? !!!
//			result = recurseSupertypes(supertype, supertype_entity, start_entity, processed);
			result = recurseSupertypes2(supertype, supertype_entity, start_entity, processed);
			if (result) {
				return true;
			}
		}
		

// System.out.println("<>>  1: current: " + current_entity.getName(null) + ", supertype: " + supertype_entity.getName(null));	
		
		if (current_entity == start_entity) { 
			return true;
		}
	
	
		return false;	
	
	} // end of function



	static boolean recurseSupertypes(EEntity_definition current_entity, EEntity_definition supertype_entity, EEntity_definition start_entity, HashSet processed) throws SdaiException {
		
		boolean result = false;

		if (!(processed.add(current_entity))) {
			// repeated inheritance, already done
			return false;
		}
	
//		AEntity_definition supertypes = current_entity.getSupertypes(null);
		AEntity_definition supertypes = (AEntity_definition)current_entity.getSupertypes(null);
  	SdaiIterator isuper = supertypes.createIterator();

  	while (isuper.next()) {
  		EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(isuper);
//			pw.println("\t\t-- recursing supertypes: from " + arm_entity.getName(null) + " to " + supertype.getName(null));
			result = recurseSupertypes(supertype, supertype_entity, start_entity, processed);
			if (result) {
				return true;
			}
		}
		

// System.out.println("<>>  1: current: " + current_entity.getName(null) + ", supertype: " + supertype_entity.getName(null));	
		
		if (current_entity == supertype_entity) { 
			return true;
		}
	
	
		return false;	
	
	} // end of function


	static EEntity_definition moreThanOneAttributeWithTheSameNameRenamed(String attribute_name, EEntity_definition ed) throws SdaiException {

	  SdaiModel this_model = ed.findEntityInstanceSdaiModel();
    Aggregate ia = this_model.getEntityExtentInstances(EAttribute.class);
    SdaiIterator iter_inst = ia.createIterator();
		
    while (iter_inst.next()) {
      EAttribute attr = (EAttribute) ia.getCurrentMemberObject(iter_inst);
			if (attr.testParent(null)) {
				if (attr.getParent(null) == ed) {
					if (attr.testName(null)) {
						if (attr.getName(null).equalsIgnoreCase(attribute_name)) {
							return ed;
						}
					}
				}
			}
		}
		HashSet processed = new HashSet();
	  EEntity_definition result = attributeWithTheSameNameInSupertypes(attribute_name, ed, true, ed, processed);	
		return result;
	}

	static EEntity_definition moreThanOneAttributeWithTheSameName(String attribute_name, EEntity_definition ed) throws SdaiException {

	  SdaiModel this_model = ed.findEntityInstanceSdaiModel();
    Aggregate ia = this_model.getEntityExtentInstances(EAttribute.class);
    SdaiIterator iter_inst = ia.createIterator();
		boolean already_used = false;
		
    while (iter_inst.next()) {
      EAttribute attr = (EAttribute) ia.getCurrentMemberObject(iter_inst);
			if (attr.testParent(null)) {
				if (attr.getParent(null) == ed) {
					if (attr.testName(null)) {
						if (attr.getName(null).equalsIgnoreCase(attribute_name)) {
							if (already_used) {
								return ed;
							} else {
								already_used = true;
							}
						}
					}
				}
			}
		}
		HashSet processed = new HashSet();
	  EEntity_definition result = attributeWithTheSameNameInSupertypes(attribute_name, ed, already_used, ed, processed);	
		return result;
	}

	static EEntity_definition attributeWithTheSameNameInSupertypes(String attribute_name, EEntity_definition ed, boolean already_used, EEntity_definition original, HashSet processed) throws SdaiException {
		if (!already_used) {
			// we asume that more than one attribute in supertypes should have been detected already when processing those attributes
			return null;
		}
		if (!(processed.add(ed))) {
			// repeated inheritance, already done
			return null;
		}
		
		if (ed != original) {
			// if ed == original, then it is already checked and exactly one attribute with this name found

	    SdaiModel this_model = ed.findEntityInstanceSdaiModel();
	    Aggregate ia = this_model.getEntityExtentInstances(EAttribute.class);
  	  SdaiIterator iter_inst = ia.createIterator();
			// boolean already_used = false;
		
	    while (iter_inst.next()) {
  	    EAttribute attr = (EAttribute) ia.getCurrentMemberObject(iter_inst);
				if (attr.testParent(null)) {
					if (attr.getParent(null) == ed) {
						if (attr.testName(null)) {
							if (attr.getName(null).equalsIgnoreCase(attribute_name)) {
								return ed;
							}
						}
					}
				}
			}
		} 
	
		AEntity_definition supertypes = (AEntity_definition)ed.getSupertypes(null);
  	SdaiIterator isuper = supertypes.createIterator();

  	while (isuper.next()) {
  		EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(isuper);


			EEntity_definition result = attributeWithTheSameNameInSupertypes(attribute_name, supertype, already_used, original, processed);	
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	static EAttribute getOriginalAttributeForRedeclaring(EAttribute attr) throws SdaiException {
		EAttribute redeclared = null;
		EAttribute redeclaring = null;
	
		redeclaring = attr;


		for (;;) {
			if (redeclaring instanceof EExplicit_attribute) {
				if (((EExplicit_attribute)redeclaring).testRedeclaring(null)) {
					redeclared = ((EExplicit_attribute)redeclaring).getRedeclaring(null);
					redeclaring = redeclared;
				} else {
					break;
				}
  
			} else 
			if (redeclaring instanceof EDerived_attribute) {
				if (((EDerived_attribute)redeclaring).testRedeclaring(null)) {
					redeclared = (EAttribute)((EDerived_attribute)redeclaring).getRedeclaring(null);
					redeclaring = redeclared;
				} else {
					break;
				}
			} else
			if (redeclaring instanceof EInverse_attribute) {

				if (((EInverse_attribute)redeclaring).testRedeclaring(null)) {
					redeclared = ((EInverse_attribute)redeclaring).getRedeclaring(null);
					redeclaring = redeclared;
				} else {
					break;
				}
			} else { // internal error, to report?
				break;
			}

		}
		return redeclared;
	}


  static jsdai.lang.SdaiModel  getDocumentationModel() throws jsdai.lang.SdaiException
  {

		jsdai.lang.SdaiModel model_doc = null;

		String name_searched = "_DOCUMENTATION_" + getSchema_definitionFromModel(model).getName(null).toUpperCase();
	  jsdai.lang.ASdaiModel models  = repository.getModels();
		jsdai.lang.SdaiIterator iter_models = models.createIterator();
		while (iter_models.next()) {
			jsdai.lang.SdaiModel sm1 = models.getCurrentMember(iter_models);
			String model_name = sm1.getName();
// printDDebug("findModel - searching: " + name_searched + ", current: " + model_name + ", nr of models: " + models.getMemberCount());
			if (model_name.equalsIgnoreCase(name_searched)) {
				return sm1;
			}
		}
		// model not found, create
// listModels();
// System.out.println("Creating model: : " + name_searched);

		model_doc = repository.createSdaiModel(name_searched, jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		model_doc.startReadWriteAccess();
		return model_doc;
  }

	static void createTagDocumentationEntity(jsdai.lang.EEntity target, String description) throws jsdai.lang.SdaiException {
		if (parser_pass != 5) return;

		if (description != null) {
			jsdai.lang.SdaiModel sm = getDocumentationModel(); // finds or creates if not yet exists (or maybe better to create together with the main model always?
			jsdai.SExtended_dictionary_schema.EDocumentation doc = (jsdai.SExtended_dictionary_schema.EDocumentation)sm.createEntityInstance(jsdai.SExtended_dictionary_schema.CDocumentation.class);

			// New dictionary
			jsdai.lang.A_string values = doc.createValues(null);
			values.addByIndex(1, description);


			// All this for jsdai 1.1, for jsdai 1.2 just one set method
			if (target instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)target);
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.EConstant_definition) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EConstant_definition)target);
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.ENamed_type) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.ENamed_type)target);
			} else
//                      if (target instanceof jsdai.SExtended_dictionary_schema.EDerived_attribute) {
//                              doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EDerived_attribute)target);
//printDDebug("Derived Attribute target:" + ((jsdai.SExtended_dictionary_schema.EDerived_attribute)target).getName(null));
//                      } else
			if (target instanceof jsdai.SExtended_dictionary_schema.EAttribute) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EAttribute)target);
// printDDebug("Attribute target:" + ((jsdai.SExtended_dictionary_schema.EAttribute)target).getName(null));
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.EFunction_definition) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EFunction_definition)target);
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.EProcedure_definition) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EProcedure_definition)target);
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.EGlobal_rule) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EGlobal_rule)target);
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.EWhere_rule) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EWhere_rule)target);
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.EParameter) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.EParameter)target);
			} else
			if (target instanceof jsdai.SExtended_dictionary_schema.ESub_supertype_constraint) {
				doc.setTarget(null, (jsdai.SExtended_dictionary_schema.ESub_supertype_constraint)target);
			}
		}
	}


  static EEntity_definition findEntity_definition_not_optimal(String name, ESchema_definition optional_schema)
                                    throws SdaiException {

//System.out.println("X__X 100-1 in non-optimal - name: " + name + ", schema: " + optional_schema);

    Aggregate ia = model.getEntityExtentInstances(EEntity_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
      EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            return (EEntity_definition) dec.getDefinition(null);
          }
        }
      }
    }

    return null;
  }

		/* 
		
		remark_tag = ’"’ remark_ref { ’.’ remark_ref } ’"’ .
		remark_ref = 
								 attribute_ref | 
									constant_ref | 
										entity_ref | 
							 enumeration_ref | 
							 		function_ref | 
							 	 parameter_ref | 
							 	 procedure_ref | 
							 	rule_label_ref | 
										 	rule_ref | 
										schema_ref | 
				subtype_constraint_ref | 
								type_label_ref | 
											type_ref | 
									variable_ref 
		
		
		
			tag may be simple or compound: tag1.tag2.tag3
			Unfortunately, currently there is no method to resolve such a general reference,
			even special keys used to resolve variables, attributes, etc, require special form and are apliicable only to some references
			So we have to:
			- either attempt to resove the tag by a try-and-error method
			- or to attempt to construct the key of correct format, still, by guessing what tag parts may be
			- or to make a method that compares such a compound key directly as full or sub-key with the existing keys, by converting them to the format of the tag
			  which may not be enough.
			
			BTW, part 11 seems not to support hierarchical tags, for example:
			
			(*"entity_a" this is entity entity_a
				("attribute_a_a" this is attribute attribute_a_a
				*)
			*)	
			
			because the attribute tag is an inner tag, and the outer tag is an entity tag, the inner attribute tag could be interpreted as 
			"entity_a.attribute_a_a"
			but it seems that part11 requires it to be written as:

			(*"entity_a" this is entity entity_a
				("entity_a.attribute_a_a" this is attribute attribute_a_a
				*)
			*)	
			
			In other words, part 11 does not take advantage of the nested structure of the remark, it could just as well be written:

			(*"entity_a" this is entity entity_a
			*)	
			("entity_a.attribute_a_a" this is attribute attribute_a_a of entity entity_a
			*)
			
		  At least part11 explicitly says nothing about the tags of inner remarks being further qualified by the tags of their outer remarks.
		  So perhaps we should allow that, it is a logical thing to do, otherwise why to have nested tagged remarks at all.
		
		  In other words, we could allow all 3 ways to tag an attribute:
		  
		  1.

			("entity_a.attribute_a_a" this is attribute attribute_a_a of entity entity_a
			*)
		
			2.

			(*"entity_a" this is entity entity_a
				("entity_a.attribute_a_a" this is attribute attribute_a_a
				*)
			*)	

			3.

			(*"entity_a" this is entity entity_a
				("attribute_a_a" this is attribute attribute_a_a
				*)
			*)	
		
      In a short form express, also schema prefix may be needed in the tag, and could be handled in any of the above ways, just as the entity prefix.
		
		  Another issue:  
		  part 11 says that tags should be resolved according to visibility rules of of 10.2
		  
		  However, part 11 does not say anything about the remark tag being affected by the location of the tagged remark itsef.
		  For example:
		  
		  ENTITY aaaa;
		  	bbbb : STRING;
		  END_ENTITY;
		  (*"aaaa.bbbb" this is an attribute of entity aaaa *) 	
		  
		  but if the remark itself is located inside the scope of entity aaaa, perhaps it would be enough for the tag to be resolved to have only the attribute part?
		  
		  ENTITY aaaa;
		  	bbbb : STRING;
		  	(*"bbbb" this is an attribute of entity aaaa *) 	
		  END_ENTITY;
		  
		  or

		  ENTITY aaaa;
		  	(*"bbbb" this is an attribute of entity aaaa *) 	
		  	bbbb : STRING;
		  END_ENTITY;

      By the way, as to tagged tail remarks, why not also this:

		  ENTITY aaaa;
		  	bbbb : STRING; --"bbbb" this is an attribute of entity aaaa
		  END_ENTITY;
		  		  
		  If we do not allow this, do we also need to add schema prefix for each tag if the remark itself is inside the schema anyway? - The same thing.
		  So, because explicitly this is not clearly said in part 11, perhaps we should be helpful and allow omission of such unnecessary prefices.

		  We could show some flexibility, robustness and user-friendliness and try to resolve all such cases:
		  1. try adding the tag of the outer remark as a prefix to tags of the inner remarks
		  2. look in which scope the remark itself is placed and do not require the corresponding prefix (prefices) to be present.
		  3. If such (unnecessary?) prefix or prefices already is/are explicitly present in the tag, that is ok too.
		  
		  
		  Notice that if a tag cannot be resolved, it is not an error in express, just that comment is considered to be not tagged.
		  Still, perhaps it would be a good idea to print a warning, because if someone tried to write a tag, probably a tag was intended and a mistake was made in it.
		
		------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			OK, let's try to do this:

      - Let's take also the stack with tags and try using them as prefixes - only for resolving tags for multi-line remarks, no need for tail remarks
        Furthermore, the tags in the stack should already be resolved, so perhaps better to use the already resolved tags - depends on the implemenation,
        if the implementatios is hashtable/key based, then perhaps not.
      - Let's try to find out what exactly we are currently parsing (location of the remark) and try using that information prefix tag - like			
			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

       10.1 Scope rules:
       
Item                Scope  Identifier
_________________________________________________________
alias statement       •        •1
attribute                      • 
constant                       • 
enumeration                    • 
entity                •        • 
function              •        • 
parameter                      • 
procedure             •        • 
query expression      •        •1
repeat statement      •        •1,2
rule                  •        •3
rule label                     • 
schema                •        • 
subtype constraint    •        • 
type                  •        • 
type label                     • 
variable                       •
_________________________________________________________
NOTE 1  The identifier is an implicitly declared variable
				within the defined scope of the declaration.
NOTE 2  The variable is only implicitly declared when an
				increment control is specified.
NOTE 3  An implicit variable declaration is made for all
				entities which are constrained by the rule.       
_________________________________________________________


			10.2 Visibility rules:

________________________________________________________________________________________________________
a)  An identifier is visible in the scope in which it is declared. This scope is called the local
		scope of the identifier.

b)  If an identifier is visible in a particular scope, it is also visible in all scopes defined within
		that scope, subject to rule (d).

c)  An identifier is not visible in any scope outside its local scope, subject to rule (f).

d)  When an identifier i visible in a scope P is re-declared in some inner scope Q enclosed
		within P, then:
		— If the i declared in P refers to a named data type or a type label and the i declared in
			Q does not refere to a named data type or a type label, then both the i declared in P
			and the i declared in Q are visible in Q.
		— Otherwise; only the i declared in scope Q is visible in Q and any scopes declared
			within Q. The i declared in scope P is visible in P and in any inner scopes which do
			not re-declare i.

e)  The built-in constants, functions, procedures and types of EXPRESS are considered to be
		declared in an imaginary universal scope. All schemas are nested within this scope. The
		identifiers which refer to the built-in constants, functions, procedures, types of EXPRESS,
		and schemas are visible in all scopes defined by EXPRESS.

      EXPRESS data types: 
				simple data types:			NUMBER | REAL | INTEGER | LOGICAL | BOOLEAN | STRING | BINARY 
      	aggregation data types: ARRAY | BAG | LIST | SET 
        named data types:       ENTITY | TYPE
        constructed data types: ENUMERATION | SELECT
        generalized data types: GENERIC | AGGREGATE | GENERIC ENTITY
      
			built-in constants:   CONST_E | PI | SELF | ’?’ 
	
			built-in functions:   ABS | ACOS | ASIN | ATAN | BLENGTH | COS | EXISTS | EXP | 
														FORMAT | HIBOUND | HIINDEX | LENGTH | LOBOUND | LOINDEX | 
														LOG | LOG2 | LOG10 | NVL | ODD | ROLESOF | SIN | SIZEOF | 
														SQRT | TAN | TYPEOF | USEDIN | VALUE | VALUE_IN | VALUE_UNIQUE 

			built-in procedure:   INSERT | REMOVE 

f)  Enumeration item identifiers declared within the scope of a defined data type are visible
		wherever the defined data type is visible, unless this outer scope contains a declaration of
		the same identifier for some other item.
		
		NOTE  If the next outer scope contains a declaration of the same identifier, the enumeration items
					are still accessible, but have to be prefixed by the defined data type identifier (see 12.7.2).

g)  Declarations in one schema are made visible to items in another schema by the interface
		specification (see clause 11).
________________________________________________________________________________________________________

			
			
		
		remark_tag = ’"’ remark_ref { ’.’ remark_ref } ’"’ .
		remark_ref = 
								 attribute_ref |  +
									constant_ref |  +
										entity_ref |  +
							 enumeration_ref |  +
							 		function_ref |  +
							 	 parameter_ref |  +
							 	 procedure_ref |  +
							 	rule_label_ref |  +
										 	rule_ref |  +
										schema_ref |  +
				subtype_constraint_ref |  +
								type_label_ref |  +
											type_ref |  +
									variable_ref    +


+		attribute in: entity and its subtypes
+		constant   in: function, procedure, rule, schema
+		enumeration item in: where the defining type of that enumeration is visible, unless outer scope contains the same identifier (then prefixed)
+		entity   in: function, procedure, rule, schema
+	  function in: function, procedure, rule, schema
+		procedure in: function, procedure, rule, schema
+		rule in: schema
+		rule label in: entity, rule, type
+		schema in: everywhere
+		subtype constraint in: schema
+		type: function, procedure, rule, schema
+		type label in: entity, function, procedure
+		variable in: function, procedure, rule
+		parameter in: function, procedure



(function_ref | procedure_ref | rule_ref |schema_ref) + entity_ref + attribute_ref		
             (function_ref | procedure_ref | rule_ref |schema_ref) + constant_ref
             (function_ref | procedure_ref | rule_ref |schema_ref) + entity_ref
             (function_ref | procedure_ref | rule_ref |schema_ref) + enumeration_ref
             (function_ref | procedure_ref | rule_ref |schema_ref) + function_ref
             (function_ref | procedure_ref | rule_ref |schema_ref) + procedure_ref		                                        
                                                        schema_ref + rule_ref
                                (entity_ref | rule_ref | type_ref) + rule_label_ref
                                                        schema_ref + subtype_constraint_ref
             (function_ref | procedure_ref | rule_ref |schema_ref) + type_ref
                                   (entity | function | procedure) + type_label_ref
                                     (function | procedure | rule) + variable_ref
                                            (function | procedure) + parameter_ref
                                                                     schema_ref



(function_ref | procedure_ref | rule_ref |schema_ref)  means one of:

schema_ref
schema_ref + rule_ref
schema_ref + {function_ref | procedure_ref} 
schema_ref + {function_ref | procedure_ref}  + rule_ref


algorithm_head present in functions, procedures and rules,
algorithm_head contains declaration, 

algorithm_head = { declaration } [ constant_decl ] [ local_decl ]


and declaration contain: 

declaration = entity_decl | function_decl | procedure_decl |
subtype_constraint_decl | type_decl .


so functions and procedures may be nested,

rule may contain functions and/or procedures but itself cannot be inner

entity may be declared inside procedure function or rule

etc
		
		
+  static EEntity active_scope = null;
+	static ECtScope active_scope_extension = null;

+  static Vector current_scope;

+	static String global_entity_ref = null;
+	static String global_entity_name = null;
+	static String global_schema_name = null;
+	static String hm_attribute_key = null;
		

+  static Stack scope_stack = new Stack();
+  static Vector current_scope;
		
+	static HashMap hm_variables;
+	static HashMap hm_current_variables;
+	static HashMap hm_parameters;
+	static HashMap hm_current_parameters;
+	static HashMap hm_entity_declarations;
+	static HashMap hm_current_entity_declarations;
+	static HashMap hm_type_declarations;
+	static HashMap hm_current_type_declarations;
+	static HashMap hm_function_declarations;
+	static HashMap hm_current_function_declarations;
+	static HashMap hm_procedure_declarations;
+	static HashMap hm_current_procedure_declarations;
+	static HashMap hm_rule_declarations;
+	static HashMap hm_current_rule_declarations;
+	static HashMap hm_constant_declarations;
+	static HashMap hm_current_constant_declarations;
+	static HashMap hm_subtype_constraint_declarations;
+	static HashMap hm_current_subtype_constraint_declarations;
	static HashMap hm_attributes;

+  static String global_name1_global;
  static int global_kind1_global;
+  static String global_name2_global;
  static int global_kind2_global;
+  static String global_name3_global;
  static int global_kind3_global;

  static int variable_uid = 0;
  static Stack variable_id_stack = new Stack();
  static Stack argument_stack = new Stack();
  static Stack type_stack = new Stack();
  static Stack expression_stack = new Stack();
+  static EEntity active_scope = null;
+	static ECtScope active_scope_extension = null;
+  static String active_scope_string = null;
  static Vector used_vectors = new Vector();
  static Vector referenced_vectors = new Vector();
  static Vector model_vector = new Vector();


		
		
		*/	

  // this is primarily for tail remarks, no tag stack is used
	static EEntity resolveTag(String tag_name) throws SdaiException {
		EEntity result_tag = null;
/*
		System.out.println("<RESOLVING-TAIL-TAG-01> tag name: " + tag_name);
		System.out.println("<RESOLVING-TAIL-TAG-02> active scope: " + active_scope);
		System.out.println("<RESOLVING-TAIL-TAG-03> active scope extension: " + active_scope_extension);
		System.out.println("<RESOLVING-TAIL-TAG-03B> active scope string: " + active_scope_string);
		System.out.println("<RESOLVING-TAIL-TAG-04> current scope: " + current_scope);
		System.out.println("<RESOLVING-TAIL-TAG-05> global_entity_ref: " + global_entity_ref);
		System.out.println("<RESOLVING-TAIL-TAG-06> global_entity_name: " + global_entity_name);
		System.out.println("<RESOLVING-TAIL-TAG-07> global_schema_name: " + global_schema_name);
		System.out.println("<RESOLVING-TAIL-TAG-08> hm_attribute_key: " + hm_attribute_key);
		System.out.println("<RESOLVING-TAIL-TAG-09> scope_stack: " + scope_stack);
		System.out.println("<RESOLVING-TAIL-TAG-10> schema_definition: " + sd);
		System.out.println("<RESOLVING-TAIL-TAG-11> model: " + model);
		System.out.println("<RESOLVING-TAIL-TAG-12> global_name1_global: " + global_name1_global);
		System.out.println("<RESOLVING-TAIL-TAG-13> global_name2_global: " + global_name2_global);
		System.out.println("<RESOLVING-TAIL-TAG-14> global_name3_global: " + global_name3_global);
		System.out.println("<RESOLVING-TAIL-TAG-15> hm_variables: " + hm_variables);
		System.out.println("<RESOLVING-TAIL-TAG-16> hm_current_variables: " + hm_current_variables);
		System.out.println("<RESOLVING-TAIL-TAG-17> hm_parameters: " + hm_parameters);
		System.out.println("<RESOLVING-TAIL-TAG-18> hm_current_parameters: " + hm_current_parameters);
		System.out.println("<RESOLVING-TAIL-TAG-19> hm_entity_declarations: " + hm_entity_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-20> hm_current_entity_declarations: " + hm_current_entity_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-21> hm_type_declarations: " + hm_type_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-22> hm_current_type_declarations: " + hm_current_type_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-23> hm_function_declarations: " + hm_function_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-24> hm_current_function_declarations: " + hm_current_function_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-25> hm_procedure_declarations: " + hm_procedure_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-26> hm_current_procedure_declarations: " + hm_current_procedure_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-27> hm_rule_declarations: " + hm_rule_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-28> hm_current_rule_declarations: " + hm_current_rule_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-29> hm_constant_declarations: " + hm_constant_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-30> hm_current_constant_declarations: " + hm_current_constant_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-31> hm_subtype_constraint_declarations: " + hm_subtype_constraint_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-32> hm_current_subtype_constraint_declarations: " + hm_current_subtype_constraint_declarations);
		System.out.println("<RESOLVING-TAIL-TAG-33> hm_attributes: " + hm_attributes);
*/
		//System.out.println("<RESOLVING-TAIL-TAG-34> : " + );
		//System.out.println("<RESOLVING-TAIL-TAG-35> : " + );
		//System.out.println("<RESOLVING-TAIL-TAG-36> : " + );
		//System.out.println("<RESOLVING-TAIL-TAG-37> : " + );
		//System.out.println("<RESOLVING-TAIL-TAG-38> : " + );
		//System.out.println("<RESOLVING-TAIL-TAG-39> : " + );
		//System.out.println("<RESOLVING-TAIL-TAG-40> : " + );

    /*
     	let's see if we have any prefixes and get them:
     
     	prefix1.prefix2.prefix3.tag
    
    	put them in a stack, perhaps:
    	
    	
    	prefix1
    	prefix2
    	prefix3
    	tag
    	
    	so that the wider scope is resolved first, then narrow down
    	the other way around - may not resolve at all or get multiple/wrong objects
   
    */

//System.out.println("The whole tag: " + tag_name);
		Stack prefix_stack = new Stack();
		int last_index = tag_name.length()-1;
		int index = last_index;
		String current_prefix = null;
		while (index > 0) {
			index = tag_name.lastIndexOf('.', last_index); 
//System.out.println("index: " + index + ", last_index: " + last_index);
			if (index > 0) {
				current_prefix = tag_name.substring(index+1,last_index+1);
//System.out.println("CURRENT PREFIX: " + current_prefix);
				prefix_stack.push(current_prefix);
				last_index = index-1;
			} else {
				current_prefix = tag_name.substring(0,last_index+1);
//System.out.println("CURRENT PREFIX 2: " + current_prefix);
				prefix_stack.push(current_prefix);
				break;
			}
		}
//System.out.println("prefix stack: " + prefix_stack);


		// let's add here some quick simple cases
		// if there is only one tag, no prefixes:
		if (prefix_stack.size() == 1) {
			// just try to resolve everything, probably starting from inside (because if the same ID is redeclared, the outers are hidden, the inner - visible
			// so also look what is the scope (if we even need to look what is the scope, perhaps some findAttribute() method does it itself, if I implemented such a method, that is
			result_tag = resolveSingleTag((String)prefix_stack.pop());
//System.out.println("RESOLVING SINGLE TAG: " + result_tag);
			return result_tag;
		} else
		if (prefix_stack.size() == 2) {
			// check for situations such as entity.attribute
			result_tag = resolveDoubleTag((String)prefix_stack.pop(), (String)prefix_stack.pop());
			return result_tag;
		}


		// Ok, so now we have the tag split to component parts and in the stack
		// if the tag contains no prefixes, still one item in the stack - the tag itself
		// let's try resolving this item
		
		EEntity subtag_object = null;
		EEntity outer_subtag_object = null;
		String current_subtag = null;
		while (!prefix_stack.empty()) {
			current_subtag = (String)prefix_stack.pop();	
			subtag_object = resolveSubtag(current_subtag, outer_subtag_object);
			if (subtag_object != null) {
				outer_subtag_object = subtag_object;
			} else {
				// failed to resolve, so further resolving makes no point - 
				// perhaps not enough prefixes were present, try taking into account the scope of the location of the remark itself
				subtag_object = resolveSubtagTryHarder(current_subtag, outer_subtag_object);	
				if (subtag_object != null) {
					outer_subtag_object = subtag_object;
				} else {
					// we give up - the tag not resolved
					// actually, a third possibility exists - 
					// some prefixes were present, but only relativelly inner ones 
					// start from location scope, then go to prefixes
					// or perhaps it is a good idea first to eliminate duplications between prefixes and scopes, and add scopes as prefixes, etc.
					return null;
				}
			}
		}
		

		if (active_scope == null) { 
			// possibly a schema level - or between schemas
		} else 
		if (active_scope instanceof EEntity_definition) {
		} else {
		}


		return outer_subtag_object;
	} 

/*
	static ESchema_definition findSchema_definition(String tag_name) throws SdaiException {
	}
*/

	static EDefined_type findDefined_typeT(String tag_name) throws SdaiException {
		EDefined_type tdf = null;
		if (hm_current_type_declarations != null ) {
			EType_declaration tdc = (EType_declaration)hm_current_type_declarations.get(tag_name.toLowerCase());
			if (tdc != null) {
				tdf = (EDefined_type)tdc.getDefinition(null);
			}	
		} else {
			return findType_definition_non_optimalT(tag_name);
		}
		return tdf;
	}


	static EGeneric_schema_definition findSchema_definitionT(String name) throws jsdai.lang.SdaiException {
		
//System.out.println("<SCHEMA>: " + name);		
		
		SdaiModel  a_model = findModel2(name);

//System.out.println("<SCHEMA> model: " + a_model);		

		if (a_model == null) return null;
		if(a_model.getMode() == jsdai.lang.SdaiModel.NO_ACCESS) {
			a_model.startReadOnlyAccess();
		}

		Aggregate ia = a_model.getEntityExtentInstances(EGeneric_schema_definition.class);
//		printDebug("findGeneric_schema_definition count: " + ia.getMemberCount() + ", name: " + name);
		jsdai.lang.SdaiIterator iter_inst = ia.createIterator();
		while (iter_inst.next()) {
			EGeneric_schema_definition inst = (EGeneric_schema_definition)ia.getCurrentMemberObject(iter_inst);
			String instance_name = inst.getName(null);
//printDDebug("findInterpretedId2 - in loop - current name: " + instance_name);

			if (instance_name.equalsIgnoreCase(name)) { // found! return it
				return inst;
			}
		}
		return null;
	}

/*
	static EEntity findEnumeration_type_element(String tag_name) throws SdaiException {
	}
*/
	static EAttribute findAttribute(String tag_name) throws SdaiException {
		if (active_scope instanceof EEntity_definition) {
		// ok, we can search for attributes of that entity - not using hashmap and keys, not taking into account support for RENAMED attributes (TODO later)
			EEntity_definition ed = (EEntity_definition)active_scope;
	    SdaiModel mdl = ed.findEntityInstanceSdaiModel();
  	  Aggregate ia = mdl.getEntityExtentInstances(EAttribute.class);
    	SdaiIterator iter_inst = ia.createIterator();

	    while (iter_inst.next()) {
  	    EAttribute inst = (EAttribute) ia.getCurrentMemberObject(iter_inst);
    	  String instance_name = inst.getName(null);

      	if (instance_name.equalsIgnoreCase(tag_name)) {
      		EEntity_definition ed2 = (EEntity_definition)inst.getParent_entity(null);
					if (ed2 == ed) {
						// ok, found it
						return inst;
					}
				}
			} // while

	    AEntity_or_view_definition aed = ed.getGeneric_supertypes(null);
//System.out.println("supertypes: " + aed);
  	  SdaiIterator iter_super = aed.createIterator();

    	while (iter_super.next()) {
      	EEntity_definition ed1 = (EEntity_definition) aed.getCurrentMemberObject(iter_super);
	      EAttribute at = findAttribute_2_tags(ed1, tag_name);
  	    if (at != null) {
    	    return at;
      	}
			} // while		

		} else {
		// hm, leave unresolved
			return null;
		}
		return null;
	}

	static EAttribute findAttribute_2_tags(EEntity_definition ed, String tag_name) throws SdaiException {
		// ok, we can search for attributes of that entity - not using hashmap and keys, not taking into account support for RENAMED attributes (TODO later)
	    SdaiModel mdl = ed.findEntityInstanceSdaiModel();
  	  Aggregate ia = mdl.getEntityExtentInstances(EAttribute.class);
    	SdaiIterator iter_inst = ia.createIterator();

	    while (iter_inst.next()) {
  	    EAttribute inst = (EAttribute) ia.getCurrentMemberObject(iter_inst);
    	  String instance_name = inst.getName(null);

      	if (instance_name.equalsIgnoreCase(tag_name)) {
      		EEntity_definition ed2 = (EEntity_definition)inst.getParent_entity(null);
					if (ed2 == ed) {
						// ok, found it
						return inst;
					}
				}
			} // while

	    AEntity_or_view_definition aed = ed.getGeneric_supertypes(null);
  	  SdaiIterator iter_super = aed.createIterator();

    	while (iter_super.next()) {
      	EEntity_definition ed1 = (EEntity_definition) aed.getCurrentMemberObject(iter_super);
				// I think there was a bug - in this recursion, wrong method was used
//	      EAttribute at = findAttribute(tag_name);
	      EAttribute at = findAttribute_2_tags(ed1, tag_name);
  	    if (at != null) {
    	    return at;
      	}
			} // while		

		return null;
	}

	static EAttribute findAttribute_everywhere(EEntity_definition ed, String tag_name) throws SdaiException {
	    // this may not be needed and somewhat duplicates the further implementation, but this result may be suitable for more applications of the parent method, for that reason included here for now
	    EAttribute attr = findAttribute_2_tags(ed, tag_name);
	    if (attr != null) {
	    	return attr;
	    } 
	    SdaiModel mdl = ed.findEntityInstanceSdaiModel();
  	  Aggregate ied = mdl.getEntityExtentInstances(EEntity_declaration.class);
    	SdaiIterator iter_ed = ied.createIterator();
	    while (iter_ed.next()) {
  	    EEntity_declaration edec = (EEntity_declaration) ied.getCurrentMemberObject(iter_ed);
				EEntity_definition edef = (EEntity_definition)edec.getDefinition(null);
			
				// check if entity edef is a subtype of our entity ed
				if (isSubtype(edef, ed)) {
						// if edef is subtype of ed, then check if edef or any of its supertypes has an attribute with the required name
						attr = findAttribute_2_tags(edef, tag_name);
						if (attr != null) {
							// this is a bit different, question is - is it ok for other applications of this method (its parent method) - to check and modify if needed
							return attr;
						}
				}
			} // while - through entity declarations
		return null;
	} // method findAttribute_everywhere

	static boolean isSubtype(EEntity_definition sub_candidate, EEntity_definition super_candidate) throws SdaiException {
			boolean result = false;
			if (sub_candidate == super_candidate) {
				// currently do this, later we'll see - depends on the implementation of parent methods
				return true;
			}
	    AEntity_or_view_definition aed = sub_candidate.getGeneric_supertypes(null);
  	  SdaiIterator iter_super = aed.createIterator();

    	while (iter_super.next()) {
      	EEntity_definition ed1 = (EEntity_definition) aed.getCurrentMemberObject(iter_super);
				if (ed1 == super_candidate) {
					return true;
				} else {
					result = isSubtype(ed1, super_candidate);
					if (result) {
						return true;
					}
				}
			} // while - through all the supertypes
		return result;
	} // method isSubtype


	// here, we will search for attributes also in subtypes and in other supertypes of subtypes
	static EAttribute findAttribute_everywhere2(EEntity_definition ed, String tag_name) throws SdaiException {
		// ok, we can search for attributes of that entity - not using hashmap and keys, not taking into account support for RENAMED attributes (TODO later)
	    SdaiModel mdl = ed.findEntityInstanceSdaiModel();
  	  Aggregate ia = mdl.getEntityExtentInstances(EAttribute.class);
    	SdaiIterator iter_inst = ia.createIterator();
			
			EEntity_definition ed2 = null;

	    while (iter_inst.next()) {
  	    EAttribute inst = (EAttribute) ia.getCurrentMemberObject(iter_inst);
    	  String instance_name = inst.getName(null);

      	if (instance_name.equalsIgnoreCase(tag_name)) {
      		ed2 = (EEntity_definition)inst.getParent_entity(null);
					if (ed2 == ed) {
						// ok, found it
						return inst;
					}
//-----------------------------------------------

			// and now let's try in subtypes

			if (ed2 != null) {
		    AEntity_or_view_definition aed2 = ed2.getGeneric_supertypes(null);
  		  SdaiIterator iter_super2 = aed2.createIterator();
    		while (iter_super2.next()) {
      		EEntity_definition ed21 = (EEntity_definition) aed2.getCurrentMemberObject(iter_super2);
	      	EAttribute at2 = findAttribute_2_tags(ed21, tag_name);
	  	    if (at2 != null) {
  	  	    return at2;
    	  	}
				} // while		
			}	


//--------------------------------------
				}
			} // while

	    AEntity_or_view_definition aed = ed.getGeneric_supertypes(null);
  	  SdaiIterator iter_super = aed.createIterator();

    	while (iter_super.next()) {
      	EEntity_definition ed1 = (EEntity_definition) aed.getCurrentMemberObject(iter_super);
	      // here, we are interested in supertypes only
	      EAttribute at = findAttribute_2_tags(ed1, tag_name);
  	    if (at != null) {
    	    return at;
      	}
			} // while		




		return null;
	}

// inverse attributes do not work here
	static EWhere_rule findWhere_rule_does_not_work(String tag_name) throws SdaiException {
//System.out.println("<WR> trying to find where rule: " + tag_name);
    // this does not help either
    ASdaiModel models2 = repository.getModels();
		AWhere_rule where_rules = null;
		if (active_scope instanceof ENamed_type) {
			where_rules = ((ENamed_type)active_scope).getWhere_rules(null, models2);
//System.out.println("<WR> NT - where_rules found: " + where_rules);
		} else 
		if (active_scope instanceof EGlobal_rule) {
			where_rules = ((EGlobal_rule)active_scope).getWhere_rules(null, models2);
//System.out.println("<WR> GR - where_rules found: " + where_rules);
		}
		if (where_rules == null) {
			return null;
		}
 	  SdaiIterator iter_wr = where_rules.createIterator();
   	while (iter_wr.next()) {
			EWhere_rule where_rule = (EWhere_rule)where_rules.getCurrentMemberObject(iter_wr);
			String a_label = null;
			if (where_rule.testLabel(null)) {
				a_label = where_rule.getLabel(null);
				if (tag_name.equalsIgnoreCase(a_label)) {
					return where_rule;
				}
			}
			
		}		
		return null;
	}

// does not work because label and parent_item are set in pass 5
	static EWhere_rule findWhere_rule(String tag_name) throws SdaiException {
//System.out.println("<WR> active_scope: " + active_scope);
		if ((active_scope instanceof ENamed_type) || (active_scope instanceof EGlobal_rule)) {
	    SdaiModel mdl = active_scope.findEntityInstanceSdaiModel();
  	  Aggregate where_rules = mdl.getEntityExtentInstances(EWhere_rule.class);
//System.out.println("<WR> where rules: " + where_rules);
    	SdaiIterator iter_wr = where_rules.createIterator();

	    while (iter_wr.next()) {
  	    EWhere_rule where_rule = (EWhere_rule) where_rules.getCurrentMemberObject(iter_wr);
				if (where_rule.testParent_item(null)) { // should be always set, but may be unset at certain stages while still during parsing
					if (where_rule.getParent_item(null) == active_scope) {
  	  	  	String wr_label = null;
						if (where_rule.testLabel(null)) {
							wr_label = where_rule.getLabel(null);
							if (tag_name.equalsIgnoreCase(wr_label)) {
								return where_rule;
							} // if the same label
						} // if has label
					} // if parent_item
				} // parent_item set
			} // while				
		} else {
			return null;
		}
		return null;
	}

	static EWhere_rule findNamed_type_where_rule(ENamed_type nt, String tag_name) throws SdaiException {
    SdaiModel mdl = nt.findEntityInstanceSdaiModel();
 	  Aggregate where_rules = mdl.getEntityExtentInstances(EWhere_rule.class);
   	SdaiIterator iter_wr = where_rules.createIterator();

    while (iter_wr.next()) {
 	    EWhere_rule where_rule = (EWhere_rule) where_rules.getCurrentMemberObject(iter_wr);
			if (where_rule.testParent_item(null)) { // should be always set, but may be unset at certain stages while still during parsing
				if (where_rule.getParent_item(null) == nt) {
  	 	  	String wr_label = null;
					if (where_rule.testLabel(null)) {
						wr_label = where_rule.getLabel(null);
						if (tag_name.equalsIgnoreCase(wr_label)) {
							return where_rule;
						} // if the same label
					} // if has label
				} // if parent_item
			} // parent_item set
		} // while				
	 return null;
	}

	static EWhere_rule findNamed_type_where_rule_not_working(ENamed_type nt, String tag_name) throws SdaiException {
		AWhere_rule where_rules = null;
		where_rules = nt.getWhere_rules(null, null);
		if (where_rules == null) {
			return null;
		}
 	  SdaiIterator iter_wr = where_rules.createIterator();
   	while (iter_wr.next()) {
			EWhere_rule where_rule = (EWhere_rule)where_rules.getCurrentMemberObject(iter_wr);
			String a_label = null;
			if (where_rule.testLabel(null)) {
				a_label = where_rule.getLabel(null);
				if (tag_name.equalsIgnoreCase(a_label)) {
					return where_rule;
				}
			}
			
		}		
		return null;
	}

	static EWhere_rule findGlobal_rule_where_rule(EGlobal_rule gr, String tag_name) throws SdaiException {
    SdaiModel mdl = gr.findEntityInstanceSdaiModel();
 	  Aggregate where_rules = mdl.getEntityExtentInstances(EWhere_rule.class);
   	SdaiIterator iter_wr = where_rules.createIterator();

    while (iter_wr.next()) {
 	    EWhere_rule where_rule = (EWhere_rule) where_rules.getCurrentMemberObject(iter_wr);
			if (where_rule.testParent_item(null)) { // should be always set, but may be unset at certain stages while still during parsing
				if (where_rule.getParent_item(null) == gr) {
   	  		String wr_label = null;
					if (where_rule.testLabel(null)) {
						wr_label = where_rule.getLabel(null);
						if (tag_name.equalsIgnoreCase(wr_label)) {
							return where_rule;
						} // if the same label
					} // if has label
				} // if parent_item
			} // parent_item set
		} // while				
	 return null;
	}

	static EWhere_rule findGlobal_rule_where_rule_not_working(EGlobal_rule gr, String tag_name) throws SdaiException {
		AWhere_rule where_rules = null;
		where_rules = gr.getWhere_rules(null, null);
		if (where_rules == null) {
			return null;
		}
 	  SdaiIterator iter_wr = where_rules.createIterator();
   	while (iter_wr.next()) {
			EWhere_rule where_rule = (EWhere_rule)where_rules.getCurrentMemberObject(iter_wr);
			String a_label = null;
			if (where_rule.testLabel(null)) {
				a_label = where_rule.getLabel(null);
				if (tag_name.equalsIgnoreCase(a_label)) {
					return where_rule;
				}
			}
			
		}		
		return null;
	}


  static EDefined_type findType_definition_non_optimalT(String name)
                                        throws SdaiException {
    Aggregate ia = model.getEntityExtentInstances(EType_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);

//      EDefined_type inst = (EDefined_type)dec.getDefinition(null, (ENamed_type)null);
      EDefined_type inst = (EDefined_type)dec.getDefinition(null);
      //EFunction_definition inst = (EFunction_definition) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            return (EDefined_type)dec.getDefinition(null);
//            return (EDefined_type)dec.getDefinition(null, (ENamed_type)null);
            //return (EFunction_definition) dec.getDefinition(null);
          }
        }
      }
    }

    return null;
  }

	static EParameter findParameter_for_single_tag(String tag_name) throws jsdai.lang.SdaiException {

		AParameter parameters = null;
		if (active_scope instanceof jsdai.SExtended_dictionary_schema.EAlgorithm_definition) {
			parameters = ((jsdai.SExtended_dictionary_schema.EAlgorithm_definition)active_scope).getParameters(null);
//System.out.println("parameters: " + parameters);
		} else {
			return null;
		}
		
		jsdai.lang.SdaiIterator iter = parameters.createIterator();
		while (iter.next()) {
			jsdai.SExtended_dictionary_schema.EParameter inst = (jsdai.SExtended_dictionary_schema.EParameter)parameters.getCurrentMemberObject(iter);
			String its_name = inst.getName(null);
			if (its_name.equalsIgnoreCase(tag_name)) { // found! return it
				return inst;
			}
		}
//		System.out.println("@#NULL find parameter null, name: " + name + ", active_scope: " + active_scope + ", pass: " + parser_pass);
		return null;
	}

	static EParameter findParameter_for_double_tag(EAlgorithm_definition tag1_object, String tag2_name) throws jsdai.lang.SdaiException {

//System.out.println("tag1: " + tag1_object + ", tag2: " + tag2_name);

		AParameter parameters = null;

	
		parameters = ((jsdai.SExtended_dictionary_schema.EAlgorithm_definition)tag1_object).getParameters(null);
//System.out.println("its parameters: " + parameters);
		
		jsdai.lang.SdaiIterator iter = parameters.createIterator();
		while (iter.next()) {
			jsdai.SExtended_dictionary_schema.EParameter inst = (jsdai.SExtended_dictionary_schema.EParameter)parameters.getCurrentMemberObject(iter);
			String its_name = inst.getName(null);
			if (its_name.equalsIgnoreCase(tag2_name)) { // found! return it
				return inst;
			}
		}
//		System.out.println("@#NULL find parameter null, name: " + name + ", active_scope: " + active_scope + ", pass: " + parser_pass);
		return null;
	}




	static EEntity resolveDoubleTag(String tag1_name, String tag2_name) throws SdaiException {
		EEntity tag2_object = null;
		EEntity tag1_object = resolveSingleTag(tag1_name);
		if (tag1_object == null) {
			return null;
		}
		// check for some pairs, such as entity-attribute
//System.out.println("tag1 object: " + tag1_object);
		if (tag1_object instanceof EEntity_definition) {
			tag2_object = findAttribute_2_tags((EEntity_definition)tag1_object, tag2_name);
			if (tag2_object == null) {
				tag2_object = findNamed_type_where_rule((ENamed_type)tag1_object, tag2_name);
			}
		} else 
		if (tag1_object instanceof EDefined_type) {
			tag2_object = findNamed_type_where_rule((ENamed_type)tag1_object, tag2_name);
		} else
		if (tag1_object instanceof EGlobal_rule) {
			tag2_object = findGlobal_rule_where_rule((EGlobal_rule)tag1_object, tag2_name);
		} else
		if (tag1_object instanceof EAlgorithm_definition) {
//System.out.println("<<<<<HERE>>>>>");
			tag2_object = findParameter_for_double_tag((EAlgorithm_definition)tag1_object, tag2_name);
		}
		
		return tag2_object;
	}

	// this version returns first non-null without checking for possible name conflicts
	static EEntity resolveSingleTag(String tag_name) throws SdaiException {
  	EEntity tag_object = null;
		Object tag_object2 = null;
		Object tag_object3 = null;
		Object tag_object4 = null;
		Object tag_object5 = null;

// we need also: schema, type (defined_type), enumeration, enumeration item (can do when have type), where rule labels, type labels. And attributes!!!


		// entity - already used separately
    tag_object = findAttribute(tag_name);
    if (tag_object != null) return tag_object;
    tag_object = findWhere_rule(tag_name);
    if (tag_object != null) return tag_object;
		tag_object = findEntity_definition(tag_name, (jsdai.SExtended_dictionary_schema.ESchema_definition)null);
    if (tag_object != null) return tag_object;
		tag_object = findDefined_typeT(tag_name);
    if (tag_object != null) return tag_object;
//    tag_object = findEnumeration_type_element(tag_name);
//    if (tag_object != null) return tag_object;
		tag_object = findParameter_for_single_tag(tag_name); // tinka par ir var
    if (tag_object != null) return tag_object;
		tag_object2 = findVariableX(tag_name);
		if (tag_object2 instanceof ECtVariable) {
			tag_object = ((ECtVariable)tag_object2).getType();
		}		
    if (tag_object != null) return tag_object;
    tag_object = findFunction_definition(tag_name);
//    tag_object = findFunction_definitionX(tag_name);
    if (tag_object != null) return tag_object;
		tag_object = findProcedure_definition(tag_name);
//		tag_object = findProcedure_definitionX(tag_name);
    if (tag_object != null) return tag_object;
		tag_object = findGlobal_rule(tag_name);
    if (tag_object != null) return tag_object;
		tag_object = findSubtype_constraint(tag_name);
    if (tag_object != null) return tag_object;
		tag_object = findConstant_definition(tag_name);
		//tag_object = findConstant_definitionX(tag_name);
    if (tag_object != null) return tag_object;
		tag_object = findSchema_definitionT(tag_name);
    if (tag_object != null) return tag_object;
//		tag_object3 = findInterpretedId(tag_name);  // tinka par 
//		tag_object4 = findVariableX(tag_name); // ECtVariable - tinka var
//		tag_object5 = findVariableY(tag_name); // ECtVariable - tinka var
//System.out.println("2: " + tag_object2); 		
//System.out.println("3: " + tag_object3); 		
//System.out.println("4: " + tag_object4); 		
//System.out.println("5: " + tag_object5); 		

// attribute - no readily suitable method found:

/*
  static EAttribute findAttribute(String attribute_name3, String attribute_name2, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key)
                    throws SdaiException {

  static EAttribute findAttribute(String attribute_name, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key)
                    throws SdaiException {

  static EAttribute findAttribute(String attribute_name3, String attribute_name2, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key)
                    throws SdaiException {

  static EAttribute findAttribute(String attribute_name, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key)
                    throws SdaiException {
*/


  	return tag_object;
  }

	static EEntity resolveSubtag(String tag_name, EEntity outer_tag) throws SdaiException {
		EEntity result_object = null;
		if (outer_tag == null) {
			// the first one, or prefixes not present
			// try the outer possible scope first - schema
//			result_object = findModel2(tag_name);	
			if (result_object != null) {
				// hm, what if a schema has the same name as an entity in it, does our compiler allows this?
				// yes, it does - you may have the schema, the entity and the attribute all with the same name.
				// or schema -> function -> inner entity -> attribute - all with the same name - OK
				// but an outer function and an inner function cannot have the same name - why, is it a bug? scopes are different
				//return result_object;
			}
		} else {
			// cannot be schema, because at least one outer tag present
		}
//System.out.println("resulting tag object: " + result_object);		
		return result_object;
	}

	static EEntity resolveSubtagTryHarder(String tag_name, EEntity outer_tag) throws SdaiException {
		return null;
	}
	 

	// this is for multi-line remarks only
	static EEntity resolveTag(String tag_name,  Stack tag_stack) throws SdaiException {
		return null;
	} 


  static EEntity_definition findEntity_definition(String name, ESchema_definition optional_schema)
                                    throws SdaiException {
 //System.out.println("X___X 01 in findEntity_definition, entity name: " + name + ", pass: " + parser_pass);
 //System.out.println("hashed declarations: " + hm_current_entity_declarations);
		EEntity_definition edf = null;
		if (hm_current_entity_declarations != null) {
//System.out.println("X___X 02 in findEntity_definition, hm_current_entity_declarations not NULL: " + hm_current_entity_declarations);
			EEntity_declaration edc = (EEntity_declaration)hm_current_entity_declarations.get(name.toLowerCase());
//System.out.println("X___X 03 in findEntity_definition, declaration: " + edc + ", key: " + name.toLowerCase());
			if (edc != null) {
				edf = (EEntity_definition)edc.getDefinition(null);
			} else {
				/*
				System.out.println("DOES IT CONTAIN THE KEY: " + hm_current_entity_declarations.containsKey(name.toLowerCase()));
				Set all_keys = hm_current_entity_declarations.keySet();
				System.out.println("ALL KEYS: " + all_keys);
				System.out.println("does it contain our name: " + all_keys.contains(name.toLowerCase()));
				//if (all_keys.contains(name.toLowerCase())) {
					Iterator it = all_keys.iterator();
					if (it.hasNext()) {
						String the_key = (String)it.next();
						System.out.println("EXTRACTED KEY: " + the_key + ", length: " + the_key.length());
						edc = (EEntity_declaration)hm_current_entity_declarations.get(the_key);
						System.out.println("2nd ATTEMPT: " + edc);
					}
				//}
			*/
			}
		} else {
// System.out.println("X___X 04 in findEntity_definition, declaration, going to non-optimal search");
			return findEntity_definition_not_optimal(name, optional_schema);
		}
		return edf;
	}

  static EMap_or_view_partition findMap_or_view_partition(String name, EEntity parent)
                              throws SdaiException {
    // get parent model and search for partitions only in that model
    SdaiModel the_model = parent.findEntityInstanceSdaiModel();
    Aggregate ia = the_model.getEntityExtentInstances(EMap_or_view_partition.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EMap_or_view_partition partition = (EMap_or_view_partition)ia.getCurrentMemberObject(iter_inst);
      String instance_name = partition.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return partition;
      }
    }

    return null;
  }

  static EMap_definition findMap_definition(String name, ESchema_map_definition optional_schema)
                              throws SdaiException {
//  System.out.println("in findMap_definition: " + name);

    Aggregate ia = model.getEntityExtentInstances(EMap_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
      EMap_definition inst = (EMap_definition) dec.getDefinition(null);

 // System.out.println("current map_definition: " + inst);

      String instance_name = inst.getName(null);

// System.out.println("comparing instance name: " + instance_name + " and " + name);


      if (instance_name.equalsIgnoreCase(name)) { // found! return it

// System.out.println("yes, found, returning: " + inst);

// System.out.println("found: " + inst);
        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            return (EMap_definition) dec.getDefinition(null);
          }
        }
      }
    }

// System.out.println("NOT found");
    return null;
  }

  static EFunction_definition findFunction_definition(String name) throws SdaiException {
		EFunction_definition fdf = null;
		if (hm_current_function_declarations != null ) {
			EFunction_declaration fdc = (EFunction_declaration)hm_current_function_declarations.get(name.toLowerCase());
			if (fdc != null) {
				fdf = (EFunction_definition)fdc.getDefinition(null);
			}	
		} else {
			return findFunction_definition_non_optimal(name);
		}
		return fdf;
	}


  static EGlobal_rule findGlobal_rule(String name) throws SdaiException {
		EGlobal_rule fdf = null;
		if (hm_current_rule_declarations != null ) {
			ERule_declaration fdc = (ERule_declaration)hm_current_rule_declarations.get(name.toLowerCase());
			if (fdc != null) {
				fdf = (EGlobal_rule)fdc.getDefinition(null);
			}	
		} else {
			return null;
			// return findFunction_definition_non_optimal(name);
		}
		return fdf;
	}

	
	
  static EFunction_definition findFunction_definitionX(String name) throws SdaiException {


//System.out.println("<in findFunction_definitionX, searching for " + name + " in scope: " + active_scope + " in HashMap: " + hm_current_function_declarations);


	
		String pure_name = null;
		if (flag_oc) {
			pure_name = name;
		} else {
			pure_name = name.toLowerCase();
		}
		String const_prefix = "";
		
		if (active_scope instanceof EAlgorithm_definition) {
			const_prefix = constructFunctionProcedurePrefix ((EAlgorithm_definition)active_scope);
		} else
		if (active_scope instanceof EGlobal_rule) {
			const_prefix = constructGlobalRulePrefix ((EGlobal_rule)active_scope);
		}
		
		for (;;) {
			String const_name = const_prefix + pure_name;
			const_name = const_name.toLowerCase();
//System.out.println("<in findFunction_definitionX, key: " + const_name);
			if (hm_current_function_declarations.containsKey(const_name)) {
		 		EFunction_declaration cd = (EFunction_declaration)hm_current_function_declarations.get(const_name);
//System.out.println("<in findFunction_definitionX, found: " + cd);
				return (EFunction_definition)cd.getDefinition(null);
			} else 
			if (const_prefix.length() == 0) {
//System.out.println("<in findFunction_definitionX, NOT found");
				return null;
			} else {
				// try the outer function/procedure or schema scope
				int index = const_prefix.lastIndexOf('$', const_prefix.length()-2);
				if (index > 0) {
					const_prefix = const_prefix.substring(0,index+1);
				} else {
					// no more outer functions, not found
//System.out.println("<in findFunction_definitionX, trying schema scope");
					// but may be in the schema scope
					const_prefix = "";
				}
			}
		}

}
	

  static EProcedure_definition findProcedure_definitionX(String name) throws SdaiException {


// System.out.println("<in findProcedure_definitionX, searching for " + name + " in scope: " + active_scope + " in HashMap: " + hm_current_procedure_declarations);


	
		String pure_name = null;
		if (flag_oc) {
			pure_name = name;
		} else {
			pure_name = name.toLowerCase();
		}
		
		String const_prefix = "";
		
		if (active_scope instanceof EAlgorithm_definition) {
			const_prefix = constructFunctionProcedurePrefix ((EAlgorithm_definition)active_scope);
		} else
		if (active_scope instanceof EGlobal_rule) {
			const_prefix = constructGlobalRulePrefix ((EGlobal_rule)active_scope);
		}
		
		for (;;) {
			String const_name = const_prefix + pure_name;
			const_name = const_name.toLowerCase();
			
// System.out.println("<in findProcedure_definitionX, key: " + const_name);
			if (hm_current_procedure_declarations.containsKey(const_name)) {
		 		EProcedure_declaration cd = (EProcedure_declaration)hm_current_procedure_declarations.get(const_name);
// System.out.println("<in findProcedure_definitionX, found: " + cd);
				return (EProcedure_definition)cd.getDefinition(null);
			} else 
			if (const_prefix.length() == 0) {
// System.out.println("<in findProcedure_definitionX, NOT found");
				return null;
			} else {
				// try the outer function/procedure or schema scope
				int index = const_prefix.lastIndexOf('$', const_prefix.length()-2);
				if (index > 0) {
					const_prefix = const_prefix.substring(0,index+1);
				} else {
					// no more outer functions, not found
// System.out.println("<in findProcedure_definitionX, trying schema scope");
					// but may be in the schema scope
					const_prefix = "";
				}
			}
		}

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
  static EFunction_definition findFunction_definition_non_optimal(String name)
                                        throws SdaiException {
    Aggregate ia = model.getEntityExtentInstances(EFunction_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);

      //                      EDefined_type inst = (EDefined_type)dec.getDefinition(null, (ENamed_type)null);
      EFunction_definition inst = (EFunction_definition) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            //                                              return (EDefined_type)dec.getDefinition(null, (ENamed_type)null);
            return (EFunction_definition) dec.getDefinition(null);
          }
        }
      }
    }

    return null;
  }

  static EProcedure_definition findProcedure_definition(String name)
                                        throws SdaiException {
    Aggregate ia = model.getEntityExtentInstances(EProcedure_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);

      //                      EDefined_type inst = (EDefined_type)dec.getDefinition(null, (ENamed_type)null);
      EProcedure_definition inst = (EProcedure_definition) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            //                                              return (EDefined_type)dec.getDefinition(null, (ENamed_type)null);
            return (EProcedure_definition) dec.getDefinition(null);
          }
        }
      }
    }

    return null;
  }


  static EView_attribute findView_attribute(String attribute_name, EView_definition vd)
                              throws SdaiException {
    if (vd == null) {
      printDDebug("XC: In findView_attribute, parameter vd = null, view attribute name: " + 
                         attribute_name + ", pass: " + parser_pass);

      return null;
    }

    SdaiModel mdl = vd.findEntityInstanceSdaiModel();
    Aggregate ia = mdl.getEntityExtentInstances(EView_attribute.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EView_attribute inst = (EView_attribute) ia.getCurrentMemberObject(iter_inst);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(attribute_name)) {
//        EView_definition vd2 = (EView_definition) inst.getParent_entity(null);
        EView_definition vd2 = (EView_definition) inst.getParent(null);

        if (vd2 == vd) {
          return (EView_attribute) inst;
        }
      }
    }

//    AEntity_or_view_definition aed = vd.getSupertypes(null);
    AEntity_or_view_definition aed = vd.getGeneric_supertypes(null);
    SdaiIterator iter_super = aed.createIterator();

    while (iter_super.next()) {
      EView_definition vd1 = (EView_definition) aed.getCurrentMemberObject(iter_super);
      EView_attribute at = findView_attribute(attribute_name, vd1);

      if (at != null) {
        return at;
      }
    }

    return null;
  }

  static ESub_supertype_constraint findSubtype_constraint(String name)
                                        throws SdaiException {
    Aggregate ia = model.getEntityExtentInstances(ESubtype_constraint_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
      ESub_supertype_constraint inst = (ESub_supertype_constraint) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            return (ESub_supertype_constraint) dec.getDefinition(null);
          }
        }
      }
    }

    return null;
  }


  static EConstant_definition findConstant_definitionX(String name) throws SdaiException {


// System.out.println("<in findConstant_definitionX, searching for " + name + " in scope: " + active_scope + " in HashMap: " + hm_current_constant_declarations);


	
		String pure_name = null;
		if (flag_oc) {
			//pure_name = name;
			// for referencing purposes the name still has to be case insensitive
			pure_name = name.toLowerCase();
		} else {
			pure_name = name.toLowerCase();
		}
		String const_prefix = "";
		
		if (active_scope instanceof EAlgorithm_definition) {
			const_prefix = constructFunctionProcedurePrefix ((EAlgorithm_definition)active_scope);
		} else
		if (active_scope instanceof EGlobal_rule) {
			const_prefix = constructGlobalRulePrefix ((EGlobal_rule)active_scope);
		} else 
		if (active_scope instanceof EConstant_definition) {
			const_prefix = constructConstantPrefix ((EConstant_definition)active_scope);
		}
		
		for (;;) {
			String const_name = const_prefix + pure_name;
			const_name = const_name.toLowerCase();
// System.out.println("<in findConstant_definitionX, key: " + const_name);
			if (hm_current_constant_declarations.containsKey(const_name)) {
		 		EConstant_declaration cd = (EConstant_declaration)hm_current_constant_declarations.get(const_name);
// System.out.println("<in findConstant_definitionX, found: " + cd);
				return (EConstant_definition)cd.getDefinition(null);
			} else 
			if (const_prefix.length() == 0) {
// System.out.println("<in findConstant_definitionX, NOT found");
				return null;
			} else {
				// try the outer function/procedure or schema scope
				int index = const_prefix.lastIndexOf('$', const_prefix.length()-2);
				if (index > 0) {
					const_prefix = const_prefix.substring(0,index+1);
				} else {
					// no more outer functions, not found
// System.out.println("<in findConstant_definitionX, trying schema scope");
					// but may be in the schema scope
					const_prefix = "";
				}
			}
		}

}

  static int getConstantDepth(String name) throws SdaiException {


// System.out.println("<in getConstantDepth, name: " + name + " in scope: " + active_scope);
//System.out.println("<in getConstantDepth, extension: "  + active_scope_extension);
//if (active_scope_extension != null) {
//	System.out.println("<in getConstantDepth, extension parent: "  + active_scope_extension.getParent());
//}


		String pure_name = null;
		if (flag_oc) {
			pure_name = name;
		} else {
			pure_name = name.toLowerCase();
		}
		
		String const_prefix = "";
		String const_prefix0 = "";
		
		if (active_scope instanceof EAlgorithm_definition) {
			const_prefix0 = constructFunctionProcedurePrefix ((EAlgorithm_definition)active_scope);
			const_prefix = const_prefix0;
		} else
		if (active_scope instanceof EGlobal_rule) {
			const_prefix0 = constructGlobalRulePrefix ((EGlobal_rule)active_scope);
			const_prefix = const_prefix0;
		}
		// adding some more stuff, because inner constant references inside other inner constants are not generated correctly
		else
		if (active_scope instanceof EConstant_definition) {
			// check if it in a function or in global rule or not
			if (active_scope_extension != null) {

				if (active_scope_extension.parent_active_scope instanceof EAlgorithm_definition) {
					const_prefix0 = constructFunctionProcedurePrefix ((EAlgorithm_definition)active_scope_extension.parent_active_scope);
					const_prefix = const_prefix0;
				} else
				if (active_scope_extension.parent_active_scope instanceof EGlobal_rule) {
					const_prefix0 = constructGlobalRulePrefix ((EGlobal_rule)active_scope_extension.parent_active_scope);
					const_prefix = const_prefix0;
				}

			}
		}
		
		
		for (;;) {
			String const_name = const_prefix + pure_name;
			const_name = const_name.toLowerCase();
//System.out.println("<in findConstant_definitionX, key: " + const_name);
			if (hm_current_constant_declarations.containsKey(const_name)) {
		 		EConstant_declaration cd = (EConstant_declaration)hm_current_constant_declarations.get(const_name);
//System.out.println("<in findConstant_definitionX, found: " + cd);

				// return the depth, 0 - if reference to a schema constant, 1 - if current function, 2 - if parent, 3 - if parent.parent, etc.
				if (const_prefix.equals("")) {
// System.out.println("<in getConstantDepth, result = 0, declared: " + const_prefix + ", reference from: " + const_prefix0);
					return 0;
				} else 
				if (const_prefix.equals(const_prefix0)) {
// System.out.println("<in getConstantDepth, result = 1, declared: " + const_prefix + ", reference from: " + const_prefix0);
					return 1;
				} else {
					
					/*
					
							 aaa$bbb$ccc$ - prefix of constant decl, where found
							 aaa$bbb#ccc$ - first prefix, calculated for the location of reference,
							 if the same, then  return 1
							 
							 aaa$bbb$      where declared   - const_prefix
							 aaa$bbb$ccc$  where referenced - const_prefix0
							 -- return 2  = 3 - 2 = 1 + 1 = 2
							 
							 
							 aaa$
							 aaa$bbb$ccc$  3 - 1 = 2 + 1 = 3
							 -- return 3
					
					
					*/

					int result = countDollars(const_prefix0) - countDollars(const_prefix) + 1;
//System.out.println("<in getConstantDepth, result: " + result + ", declared: " + const_prefix + ", reference from: " + const_prefix0);
				
					return result;
				
				}
				
//				return (EConstant_definition)cd.getDefinition(null);
			} else 
			if (const_prefix.length() == 0) {
//System.out.println("<in findConstant_definitionX, NOT found");
//System.out.println("<in getConstantDepth, NOT FOUND, result = -1, declared: " + const_prefix + ", reference from: " + const_prefix0);
				return -1;
			} else {
				// try the outer function/procedure or schema scope
				int index = const_prefix.lastIndexOf('$', const_prefix.length()-2);
				if (index > 0) {
					const_prefix = const_prefix.substring(0,index+1);
				} else {
					// no more outer functions, not found
//System.out.println("<in findConstant_definitionX, trying schema scope");
					// but may be in the schema scope
					const_prefix = "";
				}
			}
		}



/*

    Aggregate ia = model.getEntityExtentInstances(EConstant_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
      EConstant_definition inst = (EConstant_definition) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            return (EConstant_definition) dec.getDefinition(null);
          }
        }
      }
    }

    return null;

*/


}


		static int countDollars(String str) {
			
			int index = 0;
			int count = 0;
			
			while ((index >= 0) && (index < str.length()-1)) {
				index = str.indexOf('$', index+1);
				count++;
			}
			return count;
		}








  static EConstant_definition findConstant_definition(String name) throws SdaiException {
    Aggregate ia = model.getEntityExtentInstances(EConstant_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
      EConstant_definition inst = (EConstant_definition) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            return (EConstant_definition) dec.getDefinition(null);
          }
        }
      }
    }

    return null;
  }


// other findAttribute



  static EAttribute findAttribute(String attribute_name3, String attribute_name2, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key)
                    throws SdaiException {

		if (hm_attributes != null) {
			if (attr_key != null) {
				Object attr_from_hm_object = hm_attributes.get(attr_key);			
				if (attr_from_hm_object != null) {
					if (attr_from_hm_object instanceof EAttribute) {
// System.out.println("<= hm_attribute @ work, key: " + attr_key + ", attribute: " + attr_from_hm_object);
						return (EAttribute)attr_from_hm_object;
					}
				}
			}
		}
	
		
		String attribute_name;
		if (attribute_name2 != null) {
			attribute_name = attribute_name2;
		} else {
			attribute_name = attribute_name3;
		}
 
// 	if ((attribute_name3.equalsIgnoreCase("associated_component")) || (attribute_name2.equalsIgnoreCase("associated_component"))) {
 
//	 	System.out.println("### In findAttribute - name3: " + attribute_name3);
// 		System.out.println("### In findAttribute - name2: " + attribute_name2);
// 		System.out.println("### In findAttribute - name: " + attribute_name + ", ed: " + ed + ", edx: " + edx);
//	}
		boolean debug_print = false;
		
    if ((ed == null) && (parser_pass == 5)) {
			if (edx!= null) {
				ed = edx;
			}
//      System.out.println("XC: In findAttribute, parameter ed = null, attribute name: " + 
//                         attribute_name + ", type: " + attr_type + ", pass: " + parser_pass + ", edx: " + edx + ", active scope: " + active_scope);
			else {
	      printDDebug("XC: In findAttribute, parameter ed = null, attribute name: " + 
                         attribute_name + ", type: " + attr_type + ", pass: " + parser_pass);
      	return null;
    	}
    }
		if (ed == null) {
			if (edx != null) {
//	      System.out.println("XC: IGNORE - In findAttribute, parameter ed = null, attribute name: " + 
//                         attribute_name + ", type: " + attr_type + ", pass: " + parser_pass + ", edx: " + edx + ", active scope: " + active_scope);
			}
			return null;
		}
		
		if (active_scope instanceof EFunction_definition) {
			String f_name = ((EFunction_definition)active_scope).getName(null);
//			System.out.println("@@@### for function: " + f_name);
		}

//		System.out.println("@@@@@@@ findAttribute, name: " + attribute_name + ", ed: " + ed + ", type: " + attr_type + ", pass: " + parser_pass);

//		if ((ed.getName(null).equalsIgnoreCase("application_context_element")) && (attribute_name.equalsIgnoreCase("name")) && (parser_pass == 5)){
//			debug_print = true;
//			System.out.println("@@@@@@@ findAttribute in pass 5 STARTING ed: application_context_element, attribute: name");
//		}

    SdaiModel mdl = ed.findEntityInstanceSdaiModel();

    //              Aggregate ia = model.getEntityExtentInstances(EAttribute.class);
    Aggregate ia = mdl.getEntityExtentInstances(EAttribute.class);
		if (debug_print) {
//			System.out.println("##### in findAttribute - attribute number: " + ia.getMemberCount() + ", attribute_name: " + attribute_name + ", entity name: " + ed.getName(null) );
    }	
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EAttribute inst = (EAttribute) ia.getCurrentMemberObject(iter_inst);
      String instance_name = inst.getName(null);
			String instance_name_new = null;

    	Object mark_obj = inst.getTemp();
    	if (mark_obj instanceof String) {
    		instance_name_new = (String) mark_obj;
    	}


			if (debug_print) {
//				System.out.println("##### in findAttribute - current name: " + instance_name);
//				System.out.println("##### in findAttribute: " + attribute_name + " - current name: " + instance_name);
      }
      if (instance_name.equalsIgnoreCase(attribute_name)) {
        // pre-X 				EEntity_definition ed2 = inst.getParent_entity(null);
        // only Express
        // incompatible with X, is it on purpose?
        EEntity_definition ed2 = (EEntity_definition) inst.getParent_entity(null);
				if (debug_print) {
//					System.out.println("##### in findAttribute - is correct entity - ed: " + ed + ", ed2: " + ed2);
        }
        if (ed2 == ed) {
// System.out.println(">>>4 found, type: " + attr_type + ", attribute: " + inst);
          if ((attr_type == 0) && (inst instanceof EExplicit_attribute)) {
          	return (EAttribute) inst;
        	} else
          if ((attr_type == 1) && ((inst instanceof EDerived_attribute) || (inst instanceof EExplicit_attribute))) {
          	return (EAttribute) inst;
        	} else
          if ((attr_type == 2) && (inst instanceof EInverse_attribute)) {
          	return (EAttribute) inst;
   	    	} else 
   	    	if ((attr_type != 0) && (attr_type != 1) && (attr_type != 2)) {
						// what is this?
 	        	return (EAttribute) inst;
        	}
        }
      } 
      if (instance_name_new != null) {
      	if (instance_name_new.equalsIgnoreCase(attribute_name)) {
        	// pre-X 				EEntity_definition ed2 = inst.getParent_entity(null);
        	// only Express
        	// incompatible with X, is it on purpose?
        	EEntity_definition ed2 = (EEntity_definition) inst.getParent_entity(null);
					if (debug_print) {
	//					System.out.println("##### in findAttribute - is correct entity - ed: " + ed + ", ed2: " + ed2);
  	      }
    	    if (ed2 == ed) {
// System.out.println(">>>3 found, type: " + attr_type + ", attribute: " + inst);
	          if ((attr_type == 0) && (inst instanceof EExplicit_attribute)) {
  	        	return (EAttribute) inst;
    	    	} else
	          if ((attr_type == 1) && ((inst instanceof EDerived_attribute) || (inst instanceof EExplicit_attribute))) {
  //    	    if ((attr_type == 1) && (inst instanceof EDerived_attribute)) {
        	  	return (EAttribute) inst;
        		} else
	          if ((attr_type == 2) && (inst instanceof EInverse_attribute)) {
  	        	return (EAttribute) inst;
    	    	} else 
    	    	if ((attr_type != 0) && (attr_type != 1) && (attr_type != 2)) {
							// what is this?
  	        	return (EAttribute) inst;
    	    	}
        	}
      	} 
      }
      
    }





    // pre-X		AEntity_definition aed = ed.getSupertypes(null);
//    AEntity_or_view_definition aed = ed.getSupertypes(null);
    AEntity_or_view_definition aed = ed.getGeneric_supertypes(null);

//    System.out.println("##### in findAttribute - going to supertypes,  supertype number: " + aed.getMemberCount() + ", attribute_name: " + attribute_name + ", entity name: " + ed.getName(null) );
//		if (debug_print) {
//	    System.out.println("##### in findAttribute - supertype number: " + aed.getMemberCount() + ", attribute_name: " + attribute_name + ", entity name: " + ed.getName(null) );
// 	}
    SdaiIterator iter_super = aed.createIterator();

    while (iter_super.next()) {
      EEntity_definition ed1 = (EEntity_definition) aed.getCurrentMemberObject(iter_super);
//			if (debug_print) {
//				System.out.println("invoking findAttribute for supertype: " + ed1);
//    	}
      EAttribute at = findAttribute(attribute_name, ed1, attr_type, ed, attr_key);
      if (at != null) {
        return at;
      }
    }

// 	if ((attribute_name3.equalsIgnoreCase("associated_component")) || (attribute_name2.equalsIgnoreCase("associated_component"))) {
//	 System.out.println("in foundAttribute - returning null");
//	}
    return null;
  }


// end
/*
  static EAttribute findAttribute(String attribute_name, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key, Object reference) throws SdaiException {
		EAttribute result = null;
		result = findAttribute(attribute_name, ed, attr_type, edx, attr_key);
		if (result == null) {
			// may want to attempt to resolve this attribute again, this time, using the reference object
			System.out.println("in findAttribute extended version, default result is null,  reference: " + reference);
			if ((reference instanceof EParameter) || (reference instanceof ECtVariable)) {
				Object parameter_type = null;
				if (reference instanceof EParameter) {
					parameter_type = ((EParameter)reference).getParameter_type(null);
				} else
				if (reference instanceof ECtVariable) {
					parameter_type = ((ECtVariable)reference).getType().getParameter_type(null);
				} 
				if (parameter_type instanceof EDefined_type) {
					Object underlying_type = ((EDefined_type)parameter_type).getDomain(null);
					if (underlying_type instanceof ESelect_type) {
						// here perhaps we can try to resolve the attribute if it is a select type with a single element of entity type (may also be a nested or extensible select, never mind, important that there is only one final element
						// however, because we actually resolve attributes, we cannot resolve if there are several entity elements and each entity has (or perhaps even its supertype has?) the attribute with this name
						// for such cases we need to extend to accept not only resolved attributes, but also matches by the name only for selects.
					
						// perhaps we should limit ourselves with non-extensible selects for now, with non-extensible selects we could detect errors with 100% certainty.
						if (underlying_type instanceof ENon_extensible_select_type) {
							// we are interested in two cases: 
							// 1. a single entity select
							// 2. a select where none of the elements is an entity with an attribute with this name
							// especially for selects perhaps we should allow the attribute to be in a supertype
							// we should allow nested selects for both cases
							// a special case - a nested multi-element select which resolves into a single element single entity select
							// in othe words - the single entity select is a special case of the same - if we do not find an attribute in any entity element  when selects are only non-extensible
							// however, from the implementation point of view it is different: we return the attribute directly, and now we will have to return just a fact if an attribute with this name was found or not.
							// the single entity select also may be nested and have more than one elements as long as they all lead to the same final entity leaf, as mentioned above.
							// at this point, move this case to the more general implementation (without returning direct references), and leave the simplest case here (which often occurs)

    						EEntity an_ss;
    						EEntity an_ss_super;
							  boolean name_found = false;
							
							  ENon_extensible_select_type nest = (ENon_extensible_select_type)underlying_type;
							  ANamed_type ant = nest.getLocal_selections(null);
						    if (ant.getMemberCount() == 1) {
							    SdaiIterator iant1 = ant.createIterator();
							    while (iant1.next()) {
      							an_ss = (ENamed_type) ant.getCurrentMemberObject(iant1);
									  if (an_ss instanceof EEntity_definition) {
											result = findAttribute_2_tags((EEntity_definition)an_ss, attribute_name)									  }
											return result;
									}						    	
						    }
						    
						    SdaiIterator iant = ant.createIterator();
    						//all_selects.add(st);
						    while (iant.next()) {
      						an_ss = (ENamed_type) ant.getCurrentMemberObject(iant);
						      while (an_ss instanceof EDefined_type) {
        					EEntity domain = ((EDefined_type) an_ss).getDomain(null);
        					an_ss_super = an_ss;
        					an_ss = domain;
      					}
				      	if (an_ss instanceof ESelect_type) {
				      		if (an_ss instanceof ENon_extensible_select_type) {
				      		} else {
				      			// if extensible - better not to report an error at this time.
				      			return null;
				      		}
				      	} else if (an_ss instanceof EEntity_definition) {
				      		// check if it has an attribute with this name, if not, check also if an entity in a supertype chain has this attribute - if it has, better not to report an error either
				      	} else if (an_ss instanceof EAggregation_type) {
									// not interested at this time
								} else {
									// not interested
								}      					
							}
					
					}
				System.out.println("variable-or-parameter: " + underlying_type);  // #1670=NON_EXTENSIBLE_SELECT_TYPE('_SELECT_my_select',(#1665));
				}
			}
		}
		return result;
	}
*/

  static EAttribute findAttribute(String attribute_name, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key, Object reference) throws SdaiException {
		EAttribute result = null;
		result = findAttribute(attribute_name, ed, attr_type, edx, attr_key);
		if (result == null) {
			// may want to attempt to resolve this attribute again, this time, using the reference object
//			System.out.println("in findAttribute extended version, default result is null,  reference: " + reference);
//System.out.println("FA-01");
			if ((reference instanceof EParameter) || (reference instanceof ECtVariable)) {
//System.out.println("FA-02");
				Object parameter_type = null;
				if (reference instanceof EParameter) {
					//System.out.println("FA-02-B test: " + ((EParameter)reference).testParameter_type(null));
					if (((EParameter)reference).testParameter_type(null)) {
						parameter_type = ((EParameter)reference).getParameter_type(null);
					} else {
						// print a warning - may be too many, for debugging only
						return null;
					}
				} else
				if (reference instanceof ECtVariable) {
					// this occurs sometimse - to investigate further
					//System.out.println("FA-02-C test: " + ((ECtVariable)reference).getType().testParameter_type(null));
					if (((ECtVariable)reference).getType().testParameter_type(null)) {
						parameter_type = ((ECtVariable)reference).getType().getParameter_type(null);
					} else {
						// print a warning - there actually ARE too many, for debugging only
						//RR-TO-DO(got with semanticstep)
						//System.out.println("attribute parameter_type not set in a variable: " + reference + ", attribute: " + attribute_name + ", ed: " + ed + ", edx: " + edx + ", key: " + attr_key);						
						return null;
					}
				} 
				if (parameter_type instanceof EDefined_type) {
//System.out.println("FA-03");
					Object underlying_type = ((EDefined_type)parameter_type).getDomain(null);
					if (underlying_type instanceof ESelect_type) {
//System.out.println("FA-04");
	
//						if (underlying_type instanceof ENon_extensible_select_type) {
						if (true) {  // no longer needed to differentiate
//System.out.println("FA-05");
							// it is safe to deal with non_extensible selects
							// let's handle a single entity select first (it occurs often)
							EEntity an_ss = null;
//							ENon_extensible_select_type nest = (ENon_extensible_select_type)underlying_type;
							//ESelect_type nest = (ESelect_type)underlying_type;
							//ANamed_type ant = nest.getLocal_selections(null);
							ANamed_type ant = getAllSelections((ESelect_type)underlying_type);

							if (true) {
//System.out.println("FA-06");
							// if (ant.getMemberCount() == 1) {
								// we could extend the implementation to handle multiple elements - nested selects if they resolve to the same single entity, this implementation ignores nested selects for now
								SdaiIterator iant1 = ant.createIterator();
					    	boolean error_found = true;
								while (iant1.next()) {
									an_ss = (ENamed_type) ant.getCurrentMemberObject(iant1);
									if (an_ss instanceof EEntity_definition) {
										//result = findAttribute_2_tags((EEntity_definition)an_ss, attribute_name);
										result = findAttribute_everywhere((EEntity_definition)an_ss, attribute_name);
										if (result != null) {
											error_found = false;
											result = null;
											break;
										}		
										// if result == null - then report an error : but check also where else this method is invoked from, may we always report an error here?
										
/*	
										if (result == null) {
											String err_reference_name = "";
											if (reference instanceof jsdai.SExtended_dictionary_schema.EParameter) {
												err_reference_name = ((jsdai.SExtended_dictionary_schema.EParameter)reference).getName(null);
											} else
											if (reference instanceof ECtVariable) {
												err_reference_name = ((ECtVariable)reference).getName();
											}
											//error_count++;
//											printErrorMsg(err_reference_name + "." + attribute_name + " - " + err_reference_name + " type is single entity SELECT, but attribute \"" + attribute_name + "\" not found in entity " + ((EEntity_definition)an_ss).getName(null), null, true);
											printWarningMsg(err_reference_name + "." + attribute_name + " - " + err_reference_name + " type is SELECT, but attribute \"" + attribute_name + "\" may not be present in entity " + ((EEntity_definition)an_ss).getName(null), null, true);
// reports:  ERROR:  line: 26, column: 21. var2.weights - var2 type is single entity SELECT, but attribute "weights" not found in entity Rational_b_spline_surface
										} // result = null, so reporting an error
										
System.out.println("== SINGLE ENTITY SELECT, resolving attribute: " + result);										
										return result;
*/									
									} // if element is entity
								}	// through select elements, 	
					    	// if not found - print warning
					    	if (error_found) {
									String err_reference_name = "";
									if (reference instanceof jsdai.SExtended_dictionary_schema.EParameter) {
										err_reference_name = ((jsdai.SExtended_dictionary_schema.EParameter)reference).getName(null);
									} else
									if (reference instanceof ECtVariable) {
										err_reference_name = ((ECtVariable)reference).getName();
									}
//									printErrorMsg(err_reference_name + "." + attribute_name + " - " + err_reference_name + " type is non-extensible SELECT, but attribute \"" + attribute_name + "\" not found in any elements - entities", null, true);
									printWarningMsg(err_reference_name + "." + attribute_name + " - " + err_reference_name + " type is SELECT, but attribute \"" + attribute_name + "\" may not be present in any elements - entities", null, true);
					    	} // error found
					    
					    } else { // if NOT only one element - this is currently disabled
//System.out.println("FA-07");
					    	// if this non-extensible select contains more than one element, let's attempt the general solution
					    	// get each select (possibly nested select) leaf, check for each leaf, if at least one leaf found with such an attribute, then do not report an error
					    	HashSet entity_leaves = new HashSet();
					    	boolean outcome = getAllSelectEntityLeaves((ESelect_type)underlying_type, (ESelect_type)underlying_type, entity_leaves);
					    	if (!outcome) {
					    		return null;
					    	}
					    	// here we will go through all the entity leaves and check if at least one of them (or its supertype) has an attribute with this name
					    	boolean error_found = true;
					    	Iterator it1 = entity_leaves.iterator();
					    	while (it1.hasNext()) {
					    		EEntity_definition current_entity = (EEntity_definition)it1.next();
									result = findAttribute_2_tags(current_entity, attribute_name);
					    		if (result != null) {
					    			error_found = false;
					    			break;
					    		}
					    	} // while - through all the entities
					    	if (error_found) {
									String err_reference_name = "";
									if (reference instanceof jsdai.SExtended_dictionary_schema.EParameter) {
										err_reference_name = ((jsdai.SExtended_dictionary_schema.EParameter)reference).getName(null);
									} else
									if (reference instanceof ECtVariable) {
										err_reference_name = ((ECtVariable)reference).getName();
									}
//									printErrorMsg(err_reference_name + "." + attribute_name + " - " + err_reference_name + " type is non-extensible SELECT, but attribute \"" + attribute_name + "\" not found in any elements - entities", null, true);
									printWarningMsg(err_reference_name + "." + attribute_name + " - " + err_reference_name + " type is SELECT, but attribute \"" + attribute_name + "\" may not be present in any elements - entities", null, true);
					    	} // error found
								// error or not, still do not return anything
					    	return null;
					    }
						} else {  // non_extensible select
//System.out.println("FA-08");
							// extensible_select type - here catching references to non-existing attributes is more problematic
							System.out.println("INTERNAL WARNING FA6-04: underlying_type is an extensible select type: " + underlying_type);
							ANamed_type all_sels = getAllSelections((ESelect_type)underlying_type);
							System.out.println("selections: " + all_sels);
						}
					} else { // unferlying_type = select type
						// unferlying_type is not a select type - interesting why it was not resolved before
						System.out.println("INTERNAL WARNING FA6-03: underlying_type is not a select type: " + underlying_type);
					}
				} else { // parameter_type = defined type
//System.out.println("FA-09");
					// parameter_type not defined type 
					// perhaps try again, with a bit different method - however if we find attributes in subtypes and other supertypes of subtypes, it may be too much for other application of the underlying method findAttribute()
					if (parameter_type instanceof EEntity_definition) {
//System.out.println("FA-10");
							result = findAttribute_everywhere((EEntity_definition)parameter_type, attribute_name);
							if (result == null) {
//System.out.println("FA-11");
									String err_reference_name = "";
									if (reference instanceof jsdai.SExtended_dictionary_schema.EParameter) {
										err_reference_name = ((jsdai.SExtended_dictionary_schema.EParameter)reference).getName(null);
									} else
									if (reference instanceof ECtVariable) {
										err_reference_name = ((ECtVariable)reference).getName();
									}
									printWarningMsg(err_reference_name + "." + attribute_name + " - " + " attribute \"" + attribute_name + "\" may not be present in entity " + err_reference_name + " or its subtypes or supertypes unless a new suitable complex entity is created", null, true);
							} else {
								result = null;
							}
						

					} else {
						// System.out.println("INTERNAL WARNING FA6-02: parameter_type is not a defined type nor entity: " + parameter_type);
						/*
						
							actually found, while compiling stepmod arm
								#9=DATA_TYPE('_GENERIC');
						
						*/
					
					}
				}
			} else {  // reference is Parameter or variable
				// reference not Parameter or variable - to implement if needed
				// System.out.println("INTERNAL WARNING FA6-01: reference is not parameter nor variable: " + reference);
				/*
					actually found, while compiling stepmod arm
					
					defined_type
					entity_definition
					function_definition
					global_rule
					
					
				
				
				*/
			
			}
			
		} // result == null
		return result;
	} // method
  
	// do we need to support aggregates inside selects here?
	// for the test cases I am working so far - not, but perhaps for the more general application - yes?
	static boolean getSelectEntityLeaves(ENon_extensible_select_type start, ENon_extensible_select_type current, HashSet entity_leaves) throws SdaiException {
		boolean outcome = false;
		ANamed_type ant = current.getLocal_selections(null);
		EEntity element = null;
		SdaiIterator iant1 = ant.createIterator();
		while (iant1.next()) {
			element = (ENamed_type) ant.getCurrentMemberObject(iant1);
			
			while (element instanceof EDefined_type) {
				EEntity domain = ((EDefined_type)element).getDomain(null);
				/*
				if (domain instanceof ESelect_type) {
					// anything here?
					// perhaps this (could have done this outside the loop)  - perhaps a more elegant solution to do it outside the loop
					if (domain != instance of ENon_extensible_select_type) {
						return false;
					}
				} else {
					// or here?
				}
				*/
    		element = domain;
			}
			// here we have element resolved to its underlying type, of which we are interested in
			// entity_definition - mostly, aggregate - if we are going to go into them, not now, perhaps later, 
			// and, of course select - for recursion, but only non_extensible_select, if extensible - we consider it is unsafe to issue errors about attributes
			// other types - of no interest to us
			if (element instanceof EEntity_definition) {
				// add this entity to the hashset if not already there
				entity_leaves.add(element);
			} else 
			if (element instanceof ESelect_type) {
					if (element instanceof ENon_extensible_select_type) {
						outcome = getSelectEntityLeaves(start, (ENon_extensible_select_type) element, entity_leaves);
						if (!outcome) {
							return false;
						}
					} else {
						return false;
					}
				
			} else {
				// not interested for now, later might add aggregates here as well
			}				
		
		} // while - through all elements
		return true;
	} // method

	static boolean getAllSelectEntityLeaves(ESelect_type start, ESelect_type current, HashSet entity_leaves) throws SdaiException {
		boolean outcome = false;
		ANamed_type ant = current.getLocal_selections(null);
		EEntity element = null;
		SdaiIterator iant1 = ant.createIterator();
		while (iant1.next()) {
			element = (ENamed_type) ant.getCurrentMemberObject(iant1);
			
			while (element instanceof EDefined_type) {
				EEntity domain = ((EDefined_type)element).getDomain(null);
				/*
				if (domain instanceof ESelect_type) {
					// anything here?
					// perhaps this (could have done this outside the loop)  - perhaps a more elegant solution to do it outside the loop
					if (domain != instance of ENon_extensible_select_type) {
						return false;
					}
				} else {
					// or here?
				}
				*/
    		element = domain;
			}
			// here we have element resolved to its underlying type, of which we are interested in
			// entity_definition - mostly, aggregate - if we are going to go into them, not now, perhaps later, 
			// and, of course select - for recursion, but only non_extensible_select, if extensible - we consider it is unsafe to issue errors about attributes
			// other types - of no interest to us
			if (element instanceof EEntity_definition) {
				// add this entity to the hashset if not already there
				entity_leaves.add(element);
			} else 
			if (element instanceof ESelect_type) {
					if (true) {
					// if (element instanceof ENon_extensible_select_type) {
						outcome = getAllSelectEntityLeaves(start, (ESelect_type) element, entity_leaves);
						if (!outcome) {
							return false;
						}
					} else {
						return false;
					}
				
			} else {
				// not interested for now, later might add aggregates here as well
			}				
		
		} // while - through all elements
		return true;
	} // method


  
  static EAttribute findAttribute(String attribute_name, EEntity_definition ed, int attr_type, EEntity_definition edx, String attr_key)
                    throws SdaiException {

		int attr_type_original;
		boolean to_debug = false;

//		if (parser_pass == 5) {
//			if (attr_type == 0) attr_type = -40; // debugging switching on in pass 5 for all explicit attributes 
//		}

		attr_type_original = attr_type;
		if (attr_type == -40) {
			attr_type = 0;
			to_debug = true;
			//System.out.println("to debug explicit in findAttribute - attribute_name: " + attribute_name + ", ed: " + ed + ", attr_type: " + attr_type + ", edx: " + edx + ", attr_key: " + attr_key);


//System.out.println("attribute: " + attribute_name);
//System.out.println("hm key: " + attr_key);
//System.out.println("===== hm_attributes ============");
//System.out.println("===== hm_attributes: " + hm_attributes );
//Set keyset = hm_attributes.keySet();
//Iterator keyset_iterator = keyset.iterator();
//System.out.println("\n==============================================\n");
//while (keyset_iterator.hasNext()) {
//	String a_key = (String)keyset_iterator.next();
//System.out.println("a hashmap key: " + a_key + ", value: " + hm_attributes.get(a_key));
//}


		}


//System.out.println("in findAttribute - name: " + attribute_name + ", ed: " + ed + ", edx: " + edx + ", key: " + attr_key + ", type: " + attr_type); 

if (false) {
//if (true) {
//if (attribute_name.equals("secondary")) {
//System.out.println("===== hm_attributes START ============: attribute: " + attribute_name + ", ed: " + ed + ", edx: " + edx + ", key: " + attr_key);
Set keyset = hm_attributes.keySet();
Iterator keyset_iterator = keyset.iterator();
	while (keyset_iterator.hasNext()) {
		String a_key = (String)keyset_iterator.next();
		//System.out.println("a hashmap key: " + a_key + ", value: " + hm_attributes.get(a_key));
	}
//System.out.println("\n============================================== hm_attributes END ====\n");
}


		if (hm_attributes != null) {
			if (attr_key != null) {
				Object attr_from_hm_object = hm_attributes.get(attr_key);			
				if (attr_from_hm_object != null) {
					if (attr_from_hm_object instanceof EAttribute) {
// System.out.println("<= hm_attribute @ work, key: " + attr_key + ", attribute: " + attr_from_hm_object);
						return (EAttribute)attr_from_hm_object;
					}
				}
			}
		}
		if (to_debug) {
			//System.out.println("debugging findAttribute - not in hm_attributes");
		}


// 	if (attribute_name.equalsIgnoreCase("associated_component")) {

// 		System.out.println("In findAttribute - name: " + attribute_name + ", ed: " + ed + ", edx: " + edx);
//	}

		boolean debug_print = false;
		
    if ((ed == null) && (parser_pass == 5)) {
			if (edx!= null) {
				ed = edx;
			}
//      System.out.println("XC: In findAttribute, parameter ed = null, attribute name: " + 
 //                        attribute_name + ", type: " + attr_type + ", pass: " + parser_pass + ", edx: " + edx + ", active scope: " + active_scope);
			else {
	      printDDebug("XC: In findAttribute, parameter ed = null, attribute name: " + 
                         attribute_name + ", type: " + attr_type + ", pass: " + parser_pass);
      	return null;
    	}
    }
//if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 1");
		if (ed == null) {
//if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 2");
			if (edx != null) {
//if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 3");
//	      System.out.println("XC: IGNORE - In findAttribute, parameter ed = null, attribute name: " + 
//                         attribute_name + ", type: " + attr_type + ", pass: " + parser_pass + ", edx: " + edx + ", active scope: " + active_scope);
			}
			return null;
		}
//if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 4");

		if (to_debug) {
			//System.out.println("debugging findAttribute - active_scope: " + active_scope);
		}

		
		if (active_scope instanceof EFunction_definition) {
			String f_name = ((EFunction_definition)active_scope).getName(null);
//			System.out.println("@@@### for function: " + f_name);
		}

//		System.out.println("@@@@@@@ findAttribute, name: " + attribute_name + ", ed: " + ed + ", type: " + attr_type + ", pass: " + parser_pass);

//		if ((ed.getName(null).equalsIgnoreCase("application_context_element")) && (attribute_name.equalsIgnoreCase("name")) && (parser_pass == 5)){
//			debug_print = true;
//			System.out.println("@@@@@@@ findAttribute in pass 5 STARTING ed: application_context_element, attribute: name");
//		}
//if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 5");

		EEntity_definition edd = null;
		if (active_scope instanceof EEntity_definition) {
//if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 6");
			edd = (EEntity_definition)active_scope;
// System.out.println(">>> edd: " + edd);
// System.out.println(">>> ed : " + ed);
// System.out.println(">>> edx: " + edx);

		}
// System.out.println("In findAttribute - name: " + attribute_name);
// System.out.println(">>> ed : " + ed);
// System.out.println(">>> edx: " + edx);
// System.out.println(">>> edd: " + edd);
		// better search in the current entity
		if (edd != null) {
//			ed = edd;
		}


    SdaiModel mdl = ed.findEntityInstanceSdaiModel();
//System.out.println("<M> attribute: " + attribute_name + ", entity: " + ed + ", model: " + mdl);

    //              Aggregate ia = model.getEntityExtentInstances(EAttribute.class);
    Aggregate ia = mdl.getEntityExtentInstances(EAttribute.class);
		if (debug_print) {
//			System.out.println("##### in findAttribute - attribute number: " + ia.getMemberCount() + ", attribute_name: " + attribute_name + ", entity name: " + ed.getName(null) );
    }	
    SdaiIterator iter_inst = ia.createIterator();
//if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 7");

    while (iter_inst.next()) {
      EAttribute inst = (EAttribute) ia.getCurrentMemberObject(iter_inst);
// if (attribute_name.equalsIgnoreCase("defined_part_feature")) System.out.println("<findAttribute> 8");
      String instance_name = inst.getName(null);
			String instance_name_new = null;

    	Object mark_obj = inst.getTemp();
    	if (mark_obj instanceof String) {
    		instance_name_new = (String) mark_obj;

//if (attribute_name.equalsIgnoreCase("defined_part_feature")) {
//	System.out.println("<findAttribute> 8a - instance_name_new: " + instance_name_new + ", instance_name: " + instance_name + ", attribute name: " + attribute_name + "ed: " + ed + ", parent: " + inst.getParent_entity(null));
//}	
//if ((instance_name_new.equalsIgnoreCase("defined_part_feature")) && (attribute_name.equalsIgnoreCase("defined_part_feature")))	System.out.println("<findAttribute> 8a - instance_name_new: " + instance_name_new + ", instance_name: " + instance_name + ", attribute name: " + attribute_name);
    	} else {
    		instance_name_new = null;
    	}
//if ((parser_pass == 4) && (inst.getParent_entity(null).getName(null).equalsIgnoreCase("component_feature_to_physical_usage_view_assignment"))) {
//if ((parser_pass == 4) && (inst.getParent_entity(null).getName(null).equalsIgnoreCase(""))) {
//	System.out.println("<findAttribute> 8b - instance_name_new: " + instance_name_new + ", instance_name: " + instance_name + ", attribute name: " + attribute_name + "ed: " + ed + ", parent: " + inst.getParent_entity(null));
//}

			if (to_debug) {
				if (ed == inst.getParent_entity(null)) {
					//System.out.println("current attribute - new name: " + instance_name_new + ", attribute: " + inst + ", entity: " + ed);
				}
			}

			if (debug_print) {
//				System.out.println("##### in findAttribute - current name: " + instance_name);
//				System.out.println("##### in findAttribute: " + attribute_name + " - current name: " + instance_name);
      }
      if (instance_name.equalsIgnoreCase(attribute_name)) {
        // pre-X 				EEntity_definition ed2 = inst.getParent_entity(null);
        // only Express
        // incompatible with X, is it on purpose?
        EEntity_definition ed2 = (EEntity_definition) inst.getParent_entity(null);
				if (debug_print) {
//					System.out.println("##### in findAttribute - is correct entity - ed: " + ed + ", ed2: " + ed2);
        }
        if (ed2 == ed) {
// System.out.println(">>> found, type: " + attr_type + ", attribute: " + inst);

          if ((attr_type == 0) && (inst instanceof EExplicit_attribute)) {
          	return (EAttribute) inst;
        	} else
          if ((attr_type == 1) && ((inst instanceof EDerived_attribute) || (inst instanceof EExplicit_attribute))) {
//          if ((attr_type == 1) && (inst instanceof EDerived_attribute)) {
          	return (EAttribute) inst;
        	} else
          if ((attr_type == 2) && (inst instanceof EInverse_attribute)) {
          	return (EAttribute) inst;
   	    	} else 
   	    	if ((attr_type != 0) && (attr_type != 1) && (attr_type != 2)) {
						// what is this?
 	        	return (EAttribute) inst;
        	}


//          return (EAttribute) inst;
        }
      } 
//if ((instance_name_new != null) && (attribute_name.equalsIgnoreCase("defined_part_feature")) &&(instance_name_new.equalsIgnoreCase("defined_part_feature")))	System.out.println("<findAttribute> 9");
      if (instance_name_new != null) {
      	if (instance_name_new.equalsIgnoreCase(attribute_name)) {
        	// pre-X 				EEntity_definition ed2 = inst.getParent_entity(null);
        	// only Express
        	// incompatible with X, is it on purpose?
        	EEntity_definition ed2 = (EEntity_definition) inst.getParent_entity(null);
					if (debug_print) {
	//					System.out.println("##### in findAttribute - is correct entity - ed: " + ed + ", ed2: " + ed2);
  	      }
//if ((instance_name_new != null) && (attribute_name.equalsIgnoreCase("defined_part_feature")) && (instance_name_new.equalsIgnoreCase("defined_part_feature")))	System.out.println("##### in findAttribute - is correct entity - ed: " + ed + ", ed2: " + ed2);
    	    if (ed2 == ed) {
//if ((instance_name_new != null) && (attribute_name.equalsIgnoreCase("defined_part_feature")) && (instance_name_new.equalsIgnoreCase("defined_part_feature")))	System.out.println("##### in findAttribute - found: " + inst);
// System.out.println(">>> found : " + inst);
// System.out.println(">>>2 found, type: " + attr_type + ", attribute: " + inst);

	          if ((attr_type == 0) && (inst instanceof EExplicit_attribute)) {
//if ((instance_name_new != null) && (attribute_name.equalsIgnoreCase("defined_part_feature")) && (instance_name_new.equalsIgnoreCase("defined_part_feature")))	System.out.println("##### in findAttribute - returning 1: " + inst);
  	        	return (EAttribute) inst;
    	    	} else
	          if ((attr_type == 1) && ((inst instanceof EDerived_attribute) || (inst instanceof EExplicit_attribute))) {
//      	    if ((attr_type == 1) && (inst instanceof EDerived_attribute)) {
//if ((instance_name_new != null) &&  (attribute_name.equalsIgnoreCase("defined_part_feature")) && (instance_name_new.equalsIgnoreCase("defined_part_feature")))	System.out.println("##### in findAttribute - returning 2: " + inst);
        	  	return (EAttribute) inst;
        		} else
	          if ((attr_type == 2) && (inst instanceof EInverse_attribute)) {
//if ((instance_name_new != null) && (attribute_name.equalsIgnoreCase("defined_part_feature")) && (instance_name_new.equalsIgnoreCase("defined_part_feature")))	System.out.println("##### in findAttribute - returning 3: " + inst);
  	        	return (EAttribute) inst;
    	    	} else 
    	    	if ((attr_type != 0) && (attr_type != 1) && (attr_type != 2)) {
							// what is this?
//if ((instance_name_new != null) && (attribute_name.equalsIgnoreCase("defined_part_feature")) && (instance_name_new.equalsIgnoreCase("defined_part_feature")))	System.out.println("##### in findAttribute - returning 4: " + inst);
  	        	return (EAttribute) inst;
    	    	}


//      	    return (EAttribute) inst;
        	} // if correct entity
      	} // name = new name in obj
      } // new name in obj != null
      
    } // while - through attributes

    // pre-X		AEntity_definition aed = ed.getSupertypes(null);
//    AEntity_or_view_definition aed = ed.getSupertypes(null);
    AEntity_or_view_definition aed = ed.getGeneric_supertypes(null);

//    System.out.println("##### in findAttribute - going to supertypes,  supertype number: " + aed.getMemberCount() + ", attribute_name: " + attribute_name + ", entity name: " + ed.getName(null) );
//		if (debug_print) {
//	    System.out.println("##### in findAttribute - supertype number: " + aed.getMemberCount() + ", attribute_name: " + attribute_name + ", entity name: " + ed.getName(null) );
// 	}
    SdaiIterator iter_super = aed.createIterator();

    while (iter_super.next()) {
      EEntity_definition ed1 = (EEntity_definition) aed.getCurrentMemberObject(iter_super);
//			if (debug_print) {
//				System.out.println("invoking findAttribute for supertype: " + ed1);
//    	}
//System.out.println("findAttribute - entity: " + ed1.getName(null) + ", attribute: " + attribute_name + ", pass: " + parser_pass);
      EAttribute at = findAttribute(attribute_name, ed1, attr_type, ed, attr_key);
      if (at != null) {
// System.out.println(">>> found in supertype : " + at);
        return at;
      }
    }

//if (attribute_name.equalsIgnoreCase("associated_component")) {
 //System.out.println(">>> NOT found");
//}
    return null;
  }

  static ESource_parameter findSource_parameter(String name) throws SdaiException {
    EEntity vmd = null;
    EEntity supertype = null;




//    System.out.println("<##>in finfdSource_parameter: " + name + ", active_scope: " + active_scope);

    if ((active_scope instanceof EView_definition) || (active_scope instanceof EMap_definition)) {
      vmd = (EEntity) active_scope;
    } else {
//      System.out.println("findSource_parameter: " + name + ", something wrong here active scope is not map or view: " + active_scope);

// System.out.println("NOT found");
      return null;
    }

    Aggregate ia = model.getEntityExtentInstances(ESource_parameter.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      ESource_parameter inst = (ESource_parameter) ia.getCurrentMemberObject(iter_inst);
      String its_name = inst.getName(null);
//    System.out.println("findSource_parameter: " + its_name);

      if (its_name.equalsIgnoreCase(name)) { // found! return it

        // a risky solution, in pass 1 it is not possible to check in supertypes, 
        // because supertypes are not yet set. So accepting anywhere
        // only pass 5 is really important, the same risky solution is ok in other passes if ok in pass 1
//        System.out.println("findSource_parameter - match: " + name);

        if (parser_pass < 5) {
// System.out.println("found: " + inst);
//          return inst;
        }

        EMap_or_view_partition parent_partition = inst.getParent(null);
        EEntity parent = parent_partition.getParent(null);

        if (parent == vmd) {
				// in map calls, a different - called - map, but the names of source parameters in maps must be different
				// otherwise may be problems, wrong source parameter may be found
				// to look what can be done

//				if (true) {
// System.out.println("found: " + inst);
          return inst;
        } else {
          // perhaps a subtype
//          System.out.println("findSource_parameter - supertypes");

          AEntity supertypes = null;

          if (vmd instanceof EMap_definition) {
            EMap_definition md = (EMap_definition) vmd;
          
          	for (;;) {
							if (md.testSuper_type(null)) {
								md = md.getSuper_type(null);
          			if (parent == md) {
          				return inst;
          			}
          		} else {
          			break;
          		}
          	}
//            supertype = md.getSuper_type(null);
//            System.out.println("findSource_parameter - nr of supertypes: " + supertypes.getMemberCount());

//            if (parent == supertype) {
//              return inst;
//            }



          } else if (vmd instanceof EView_definition) {
          	// fixed map supertypes so that chains are allowed, TODO for view supertype chains as well
          	
            EView_definition vd = (EView_definition) vmd;
//            supertypes = vd.getSupertypes(null);
            supertypes = vd.getGeneric_supertypes(null);

            SdaiIterator iter_super = supertypes.createIterator();

            while (iter_super.next()) {
              supertype = (EEntity) supertypes.getCurrentMemberObject(iter_super);

              if (parent == supertype) {
                return inst;
              }
            }
          }
        }
      }
    }
// System.out.println("NOT found");

    return null;
  }

  // 
  static ETarget_parameter findTarget_parameter(String name) throws SdaiException {
    EEntity md = null;

// System.out.println("<findTarget_parameter-01> name: " + name);


		String map_name = null;

    if (active_scope instanceof EMap_definition) {
      md = (EEntity) active_scope;
      map_name = ((EMap_definition)md).getName(null);
    } else {
      // System.out.println("findTarget_parameter: " + name + ", something wrong here active scope is not map: " + active_scope);

      return null;
    }

		// we have to handle the case when target parameter is a prefix in map call:
		//   target_parameter_id@map_cal_name

    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;
    int token_kind2 = Compiler2.getToken(2).kind;
    String token_image2 = Compiler2.getToken(2).image;



    if ((token_kind == Compiler2Constants.OP_AT) && (token_kind2 == Compiler2Constants.SIMPLE_ID)) {
    	// this target parameter is a prefix for map call,
    	// we need to search for it in that map
    	map_name = token_image2;
    }


// System.out.println("### token(1) - kind: " + token_kind + ", image: " + token_image);
// System.out.println("### token(2) - kind: " + token_kind2 + ", image: " + token_image2);
// System.out.println("### token(3) - kind: " + token_kind3+ ", image: " + token_image3);

    Aggregate ia = model.getEntityExtentInstances(ETarget_parameter.class);
    SdaiIterator iter_inst = ia.createIterator();

// System.out.println("<findTarget_parameter-02> target parameters: " + ia);

    while (iter_inst.next()) {
      ETarget_parameter inst = (ETarget_parameter) ia.getCurrentMemberObject(iter_inst);
      String its_name = inst.getName(null);

      if (its_name.equalsIgnoreCase(name)) { // found! return it

        EMap_definition parent = inst.getParent(null);
				String parent_name = parent.getName(null);

// we will compare map names instead, we have either the current map mane or the called map name if the target parameter is prefix
//        if (parent == md) {
        if (parent_name.equalsIgnoreCase(map_name)) {
          return inst;
        } else {
        	// have to allow, because  of map calls: target_parameter@map_definition()
        	// on the other hand, it is dangerous to allow it, if the same target parameter name is used in various maps
        	// the express-x syntax is very bad for implementation: target parameter reference BEFORE its map reference
// System.out.println("<findTarget_parameter-03> target parameter - different map: " + inst);
   
          // no, this issue already solved
          // return inst;
        }
      }
    }

    return null;
  }

  // no longer needed, the problem solved, but may stay in use, no difference
  static ETarget_parameter findTarget_parameterStrict(String name) throws SdaiException {
    EEntity md = null;

// System.out.println("<findTarget_parameter-01> name: " + name);

    if (active_scope instanceof EMap_definition) {
      md = (EEntity) active_scope;
    } else {
      System.out.println("findTarget_parameter: " + name + ", something wrong here active scope is not map: " + active_scope);

      return null;
    }

    Aggregate ia = model.getEntityExtentInstances(ETarget_parameter.class);
    SdaiIterator iter_inst = ia.createIterator();

// System.out.println("<findTarget_parameter-02> target parameters: " + ia);

    while (iter_inst.next()) {
      ETarget_parameter inst = (ETarget_parameter) ia.getCurrentMemberObject(iter_inst);
      String its_name = inst.getName(null);

      if (its_name.equalsIgnoreCase(name)) { // found! return it

        EMap_definition parent = inst.getParent(null);

        if (parent == md) {
          return inst;
        }
      }
    }

    return null;
  }

  static Object findInterpretedIdX(String name) throws SdaiException {
    Object result = null;
    String its_name = null;

//if (name.equalsIgnoreCase("result")) {
//	System.out.println("<about to find var, 07, name: " + name); 
//}
		Object var_attempt = findVariableX(name);
		if (var_attempt != null) {
			return var_attempt;
		}
//if (parser_pass == 5) System.out.println("#_#  searching for: " + name);

    // only partially implemented so far, other types must be supported, and also not only current_scope, but also from the scope stack.
    // System.out.println("#_#_XX current_scope size: " + current_scope.size() + ", name to search: " + name);
    for (int i = 0; i < current_scope.size(); i++) {
      Object something = current_scope.elementAt(i);
// if (parser_pass == 5)
// System.out.println("isPARAMETER-2, current scope: " + something);

      //System.out.println("#_#_# Current scope: " + something + ", name to search: " + name);
      if (something instanceof ECtVariable) {
        its_name = ((ECtVariable) something).getName();
// if (parser_pass == 5)
// System.out.println("isPARAMETER-2, current scope - variable, name: " + its_name);

        // System.out.println("#_# Variable - current name: " + its_name + ", name: " + name);
        if (its_name.equalsIgnoreCase(name)) {
          return something;
        }
      } else if (something instanceof EParameter) {
        its_name = ((EParameter) something).getName(null);
// if (parser_pass == 5)
// System.out.println("isPARAMETER-2, current scope - parameter, name: " + its_name);

        // System.out.println("#_# Parameter - current name: " + its_name + ", name: " + name);
        if (its_name.equalsIgnoreCase(name)) {
          return something;
        }
      }
    }

//if (parser_pass == 5) System.out.println("#_# Scope stack size: " + scope_stack.size());
    for (int j = 0; j < scope_stack.size(); j++) {
      Vector the_scope = (Vector) scope_stack.elementAt(j);

//if (parser_pass == 5) System.out.println("#_#, the " + j + " scope: " + the_scope);

//System.out.println("#_# Scope stack index: " + j);
      for (int i = 0; i < the_scope.size(); i++) {
        Object something = the_scope.elementAt(i);

//if (parser_pass == 5) System.out.println("#_#_# scope element nr: " + i + ", scope element: " + something);
        if (something instanceof ECtVariable) {
          its_name = ((ECtVariable) something).getName();
// if (parser_pass == 5)
// System.out.println("isPARAMETER-3, the scope - variable, name: " + its_name);

          // System.out.println("#_# current name: " + its_name + ", name: " + name);
          if (its_name.equalsIgnoreCase(name)) {
            result = (ECtVariable) something;

            return result;

            // break;
          }
        } else if (something instanceof EParameter) {
          its_name = ((EParameter) something).getName(null);
// if (parser_pass == 5)
// System.out.println("isPARAMETER-3, the scope - parameter, name: " + its_name);

          // System.out.println("#_# Parameter - current name: " + its_name + ", name: " + name);
          if (its_name.equalsIgnoreCase(name)) {
            return something;
          }
        }
      }
    }

    return null;
  }


  static Object findInterpretedId(String name) throws SdaiException {
    Object result = null;
    String its_name = null;

//if (parser_pass == 5) System.out.println("#_#  searching for: " + name);

    // only partially implemented so far, other types must be supported, and also not only current_scope, but also from the scope stack.
    // System.out.println("#_#_XX current_scope size: " + current_scope.size() + ", name to search: " + name);
    for (int i = 0; i < current_scope.size(); i++) {
      Object something = current_scope.elementAt(i);
// if (parser_pass == 5)
// System.out.println("isPARAMETER-2, current scope: " + something);

      //System.out.println("#_#_# Current scope: " + something + ", name to search: " + name);
      if (something instanceof ECtVariable) {
        its_name = ((ECtVariable) something).getName();
// if (parser_pass == 5)
// System.out.println("isPARAMETER-2, current scope - variable, name: " + its_name);

        // System.out.println("#_# Variable - current name: " + its_name + ", name: " + name);
        if (its_name.equalsIgnoreCase(name)) {
          return something;
        }
      } else if (something instanceof EParameter) {
        its_name = ((EParameter) something).getName(null);
// if (parser_pass == 5)
// System.out.println("isPARAMETER-2, current scope - parameter, name: " + its_name);

        // System.out.println("#_# Parameter - current name: " + its_name + ", name: " + name);
        if (its_name.equalsIgnoreCase(name)) {
          return something;
        }
      }
    }

//if (parser_pass == 5) System.out.println("#_# Scope stack size: " + scope_stack.size());
    for (int j = 0; j < scope_stack.size(); j++) {
      Vector the_scope = (Vector) scope_stack.elementAt(j);

//if (parser_pass == 5) System.out.println("#_#, the " + j + " scope: " + the_scope);

//System.out.println("#_# Scope stack index: " + j);
      for (int i = 0; i < the_scope.size(); i++) {
        Object something = the_scope.elementAt(i);

//if (parser_pass == 5) System.out.println("#_#_# scope element nr: " + i + ", scope element: " + something);
        if (something instanceof ECtVariable) {
          its_name = ((ECtVariable) something).getName();
// if (parser_pass == 5)
// System.out.println("isPARAMETER-3, the scope - variable, name: " + its_name);

          // System.out.println("#_# current name: " + its_name + ", name: " + name);
          if (its_name.equalsIgnoreCase(name)) {
            result = (ECtVariable) something;

            return result;

            // break;
          }
        } else if (something instanceof EParameter) {
          its_name = ((EParameter) something).getName(null);
// if (parser_pass == 5)
// System.out.println("isPARAMETER-3, the scope - parameter, name: " + its_name);

          // System.out.println("#_# Parameter - current name: " + its_name + ", name: " + name);
          if (its_name.equalsIgnoreCase(name)) {
            return something;
          }
        }
      }
    }

    return null;
  }

  //  boolean isAttributeRef(jsdai.expressCompiler.Compiler2 the_parser, EEntity_definition ed) {
  static boolean isAttributeRef() {
  	String attr_key = null;
    EEntity_definition ed = null;
    EAttribute attr = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    try {
      if (active_scope instanceof EEntity_definition) {
		
				// adding to handle variables in local rules etc
        Object found = findVariableX(token_image);
        if (found != null) {
        	if (!(found instanceof EAttribute)) {
        		return false;
        	}
        }

        ed = (EEntity_definition) active_scope;
        attr = findAttribute(token_image, ed, -1, null, attr_key);

        if (attr != null) {
          printDebug("QF lookaheads -  selected attribute_ref");

          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isAttributeRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isMapCall() {
		if (!express_x) {
			return false;
		}

		EMap_definition md = null;
		
	// if then obviously map call <SIMPLE_ID> <X_AT_X> 
	  int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;
	  int token_kind2 = Compiler2.getToken(2).kind;
    String token_image2 = Compiler2.getToken(2).image;

//System.out.println("XXX - is map call? - image (1): " + token_image + ", image (2): " + token_image2);
    
    if ((token_kind == Compiler2Constants.SIMPLE_ID) && (token_kind2 == Compiler2Constants.OP_AT)) {

//System.out.println("XXX - prefixed map call detected");
    	return true;
    } 
    else {
//System.out.println("XXX - NOT prefixed map call");
    }
   
  // if not, more difficult to tell
  // MapCall(name) 



    try {
//      con = findConstant_definition(token_image);
      md = findMap_definition(token_image, null);

      if (md != null) {
        printDebug("QF lookaheads -  selected map_call");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isMapCall, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

		
//		return false		;
  }


  static boolean isMapCall_other() {
		if (!express_x) {
			return false;
		}
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    printDebug("QF lookaheads -  selected (left) map_call");

    //		try {
    // check for the sequence of optional target_parameter reference followed by @ first.
    // [	TargetParameterRef  '@' ] MapCall() 
    // 	MapCall(): MapRef() [ PartitionQualification() ] "(" ExpressionOrWild() ( "," ExpressionOrWild())* ")"
    // NOTE: perhaps MapReference() should be here, not MapRef() - in analogy to ViewReference() in ViewCall? - error in part 14?
    //		} catch (SdaiException e) {
    //			System.out.println("SdaiException in isMapCall, name: " + token_image + ", description: " + e.getErrorDescription(e.getErrorId()));
    //			e.printStackTrace();
    //			return false;
    //		}
    return false;
  }


  static boolean isGeneralOrMapCall() {
		if (!express_x) {
			return false;
		}

		EMap_definition md = null;
		
	// if then obviously map call <SIMPLE_ID> <X_AT_X> 
	  int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;
	  int token_kind2 = Compiler2.getToken(2).kind;
    String token_image2 = Compiler2.getToken(2).image;
    if ((token_kind == Compiler2Constants.SIMPLE_ID) && (token_kind2 == Compiler2Constants.OP_AT)) {
    	return true;
    }
  
  // if not, more difficult to tell
  // MapCall(name) 



    try {
//      con = findConstant_definition(token_image);
      md = findMap_definition(token_image, null);

      if (md != null) {
        printDebug("QF lookaheads -  selected map_call");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isGeneralOrMapCall, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

		
//		return false		;
  }

  static boolean isConstantRef() {
    EConstant_definition con = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    try {
//      con = findConstant_definition(token_image);
      con = findConstant_definitionX(token_image);

      if (con != null) {
        printDebug("QF lookaheads -  selected constant_ref");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isConstantRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isFunctionCall() {
    EFunction_definition fun = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if ((token_kind == Compiler2Constants.ABS) || (token_kind == Compiler2Constants.ACOS) || 
            (token_kind == Compiler2Constants.ASIN) || (token_kind == Compiler2Constants.ATAN) || 
            (token_kind == Compiler2Constants.BLENGTH) || 
            (token_kind == Compiler2Constants.COS) || (token_kind == Compiler2Constants.EXISTS) || 
            (token_kind == Compiler2Constants.EXTENT) || (token_kind == Compiler2Constants.EXP) || 
            (token_kind == Compiler2Constants.FORMAT) || 
            (token_kind == Compiler2Constants.HIBOUND) || 
            (token_kind == Compiler2Constants.HIINDEX) || 
            (token_kind == Compiler2Constants.LENGTH) || 
            (token_kind == Compiler2Constants.LOBOUND) || 
            (token_kind == Compiler2Constants.LOINDEX) || 
            (token_kind == Compiler2Constants.LOG) || (token_kind == Compiler2Constants.LOG2) || 
            (token_kind == Compiler2Constants.LOG10) || (token_kind == Compiler2Constants.NVL) || 
            (token_kind == Compiler2Constants.ODD) || 
            (token_kind == Compiler2Constants.ROLESOF) || 
            (token_kind == Compiler2Constants.SIN) || (token_kind == Compiler2Constants.SIZEOF) || 
            (token_kind == Compiler2Constants.SQRT) || (token_kind == Compiler2Constants.TAN) || 
            (token_kind == Compiler2Constants.TYPEOF) || 
            (token_kind == Compiler2Constants.USEDIN) || 
            (token_kind == Compiler2Constants.VALUE) || 
            (token_kind == Compiler2Constants.VALUE_IN) || 
            (token_kind == Compiler2Constants.VALUE_UNIQUE)) {
      return true;
    }

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    try {
//      fun = findFunction_definition(token_image);
      fun = findFunction_definitionX(token_image);

      if (fun != null) {
        printDebug("QF lookaheads -  selected function_call");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isFunctionCall, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isPopulation() {
    EEntity_definition ed = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    int token_kind2 = Compiler2.getToken(2).kind;

    if (token_kind2 == Compiler2Constants.LPAREN) {
			//System.out.println("isPopulation: seems like EntityConstructor or function call");
      return false;
    }

    try {
      ed = findEntity_definition(token_image, null);

      if (ed != null) {
        printDebug("QF lookaheads -  selected population");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isPopulation, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isViewAttributeRef() {
		if (!express_x) {
			return false;
		}
    EView_definition vd = null;
    EView_attribute view_attr = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    try {
      if (active_scope instanceof EView_definition) {
        vd = (EView_definition) active_scope;
        view_attr = findView_attribute(token_image, vd);

        if (view_attr != null) {
          printDebug("QF lookaheads -  selected view_attribute_ref");

          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isViewAttributeRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isViewCall() {
		if (!express_x) {
			return false;
		}
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    printDebug("QF lookaheads -  NOT selected view_call");

    /*
        try {
        //   ViewReference() [ PartitionQualification() ] "(" [ ExpressionOrWild() ( "," ExpressionOrWild() )* ] ")"
        //  ViewReference: [ (SchemaMapRef() | SchemaViewRef() ) "." ] ViewRef()
        schema = findGeneralSchema(token_image);
        if (schema == null) {
            
        }
        } catch (SdaiException e) {
          System.out.println("SdaiException in isViewCall, name: " + token_image + ", description: " + e.getErrorDescription(e.getErrorId()));
          e.printStackTrace();
          return false;
        }
    */
    return false;
  }

  static boolean isParameterRef() {
    Object found = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
// if (parser_pass == 5)
// System.out.println("isPARAMETER: - not simple id: " + token_kind);

      return false;
    }

    try {
      found = findInterpretedId(token_image);

// if (parser_pass == 5)
// System.out.println("isPARAMETER: - found: " + found + ", token: " + token_image);

      if (found instanceof EParameter) {
        // check here if the next token is @, if so, then it is a map_call?
        // no, stupid express x grammar, parameter_ref does not include target_prameter_ref, the grammar is wrong.
        // so, no need to check for @
        printDebug("QF lookaheads -  selected parameter_ref");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isParameterRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isSourceParameterRef() {
		if (!express_x) {
			return false;
		}
    Object found = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    try {
//      System.out.println("IN isSourceParameterRef");
      found = findSource_parameter(token_image);

      if (found != null) {
        printDebug("QF lookaheads -  selected source_parameter_ref");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isSourceParameterRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isTargetParameterRef() {
		if (!express_x) {
			return false;
		}
    Object found = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    int token_kind2 = Compiler2.getToken(2).kind;

    if (token_kind2 == Compiler2Constants.OP_AT) {
      // this means that it is not a possible target_parameter_ref but a map_call 
      return false;
    }

    try {
      found = findTarget_parameter(token_image);

      if (found != null) {
        printDebug("QF lookaheads -  selected target_parameter_ref");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isTargetParameterRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }

  static boolean isVariableRef() {
    Object found = null;
    int token_kind = Compiler2.getToken(1).kind;
    String token_image = Compiler2.getToken(1).image;

    if (token_kind != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    try {
      found = findInterpretedId(token_image);
			//      System.out.println("XXX isVariableRef, found: " + found);

      if (found instanceof ECtVariable) {
        printDebug("QF lookaheads -  selected variable_ref");

        return true;
      } else {
        return false;
      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isVariableRef, name: " + token_image + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    // return false;
  }




  static EDefined_type  findEnumerationType(jsdai.SExtended_dictionary_schema.EDefined_type dt, String name) throws jsdai.lang.SdaiException {
	// if dt == null, then enumeration elements are hopefully unique, still have to find the enumeration
		if (dt != null) {
			return dt;
		}
		// match an enumeration type to the name

    if (parser_pass != 5) {
      return null;
    }

		Aggregate ia = model.getEntityExtentInstances(EType_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
       //non-SF        EDefined_type inst_dt = ( EDefined_type )ia.getCurrentMemberObject(iter_inst);
      EType_declaration dec_dt = (EType_declaration) ia.getCurrentMemberObject(iter_inst);
      EDefined_type inst_dt = (EDefined_type) dec_dt.getDefinition(null);

      if (inst_dt.testDomain(null)) {
      	EEntity underlying_type = inst_dt.getDomain(null);
	      if (underlying_type instanceof EEnumeration_type) {
        	EEnumeration_type the_enum = (EEnumeration_type) underlying_type;
          Vector elements_enum = getElementsV(the_enum);
					Iterator iter_enum = elements_enum.iterator();
					while (iter_enum.hasNext()) {
						String current_enum = (String)iter_enum.next();
            if (current_enum.equalsIgnoreCase(name)) {
        			return inst_dt;
        		}
        	}
				}

			}			

		}

  	return null;
  }


	static int findEnumerationValue(jsdai.SExtended_dictionary_schema.EDefined_type dt, String name) throws jsdai.lang.SdaiException {
		// here we assume that dt != null
		if ((dt == null) && (parser_pass == 5)){
			// System.out.println("Enumeration type is null in pass 5: " + name);
			return -1;
		}

    if (parser_pass != 5) {
      return 0;
    }


    if (dt.testDomain(null)) {
    	EEntity underlying_type = dt.getDomain(null);
	    if (underlying_type instanceof EEnumeration_type) {
       	EEnumeration_type the_enum = (EEnumeration_type) underlying_type;
        Vector elements_enum = getElementsV(the_enum);
				Iterator iter_enum = elements_enum.iterator();
				int count = 1;
				while (iter_enum.hasNext()) {
					String current_enum = (String)iter_enum.next();
          if (current_enum.equalsIgnoreCase(name)) {
        		return count;
        	}
      		count++;
        }
			} else {
				// System.out.println(dt.getName(null) + " is not an enumeration, searching for element: " + name);
			}

		} else {
			// System.out.println(" domain of defined type " + dt.getName(null) + " is unset, searching for enumeration element: " + name);
		} 			

		// System.out.println("Element " + name + " not found in enumeration " + dt.getName(null));
  	return 0;
  }


//--------------------------------------

  static boolean isEnumerationReference() {
//        System.out.println("-EN------- entering isEnumerationReference ------------- ");

    // initialization of these global variables when done in SimpleFactor does not work correctly
    // for the cases when SimpleFactor non-terminals are nested
   
		if (flag_oc) {
		  global_name1_global = Compiler2.getToken(1).image;
		  global_name2_global = Compiler2.getToken(2).image;
		  global_name3_global = Compiler2.getToken(3).image;
		} else {
		  global_name1_global = Compiler2.getToken(1).image.toLowerCase();
		  global_name2_global = Compiler2.getToken(2).image.toLowerCase();
		  global_name3_global = Compiler2.getToken(3).image.toLowerCase();
		}
		global_kind1_global = Compiler2.getToken(1).kind;
		global_kind2_global = Compiler2.getToken(2).kind;
		global_kind3_global = Compiler2.getToken(3).kind;

    //      if (Compiler2.getToken(1).kind != Compiler2Constants.SIMPLE_ID) {
    if (global_kind1_global != Compiler2Constants.SIMPLE_ID) {
      // System.out.println("-EN------- not SIMPLE_ID");
      return false;
    }


    String name = global_name1_global;

// System.out.println("active scope: "	+ active_scope);	
//System.out.println("name(name1): "	+ name + ", name2: " + global_name2_global + ", name3: " + global_name3_global);	


//System.out.println("tokens now: " + Compiler2.getToken(0).image + " " + Compiler2.getToken(1).image + " " + Compiler2.getToken(2).image + " " + Compiler2.getToken(3).image + " " + Compiler2.getToken(4).image + " " + Compiler2.getToken(5).image + " " + Compiler2.getToken(6).image + " " + Compiler2.getToken(7).image + " " + Compiler2.getToken(8).image);
 
//System.out.println("tokens again: " + Compiler2.getToken(0).image + " " + Compiler2.getToken(1).image + " " + Compiler2.getToken(2).image + " " + Compiler2.getToken(3).image + " " + Compiler2.getToken(4).image + " " + Compiler2.getToken(5).image + " " + Compiler2.getToken(6).image + " " + Compiler2.getToken(7).image + " " + Compiler2.getToken(8).image);

    if (parser_pass != 5) {
      //System.out.println("-EN------- not 5 pass");
      return false;
    }

    if (findIfVariable(name)) {
      //			System.out.println("-EN------- variable in pass 5: " + name);
      return false;
    }

    try {
      if (findIfParameter(name)) {
        //			System.out.println("-EN------- parameter in pass 5: " + name);
        return false;
      }

			// also see if it is an attribute first
			if (active_scope instanceof EEntity_definition) {
				
				AAttribute aa = ((EEntity_definition)active_scope).getAttributes(null, null);
				SdaiIterator iaa = aa.createIterator();
				while (iaa.next()) {
					EAttribute an_a = (EAttribute)aa.getCurrentMemberObject(iaa);  
					String attr_name = an_a.getName(null);
// System.out.println("entity: " + ((EEntity_definition)active_scope).getName(null) + ", attribute: " + attr_name);					
							
				if (attr_name.equalsIgnoreCase(name)) {
 // System.out.println("match found: " + name);
					if (global_kind2_global == Compiler2Constants.DOT) {
// System.out.println("need to see what is after the dot");
            if (global_kind3_global == Compiler2Constants.SIMPLE_ID) {
// System.out.println("which is: " + global_name3_global);
								// here, we could check, if attribute "name" is of entity type, and if so, if global_name3_global is its attribute
						} else {
							// not necessary correct here
// System.out.println("incorrect case: ID+DOT followed not by ID - return false ");
							return false;
						}
						// this is a temporary solution here, I think
// System.out.println("temporary solution  ID+DOT end reached - return false");
						return false;
					} else {
// System.out.println("Not enumeration reference, but attribute - return false");
						return false;
					}
// unreacheable
// System.out.println("ActiveScope - entity, matching attribute found, but went through successfully");
				}

				}
// System.out.println("ActiveScope - entity, went through successfully, will continue");
			} else {
// System.out.println("ActiveScope - NOT entity, so will continue");
			}


      boolean is_type = false;

      //    System.out.println("##IER## inEnumerationReference: " + name);
      // check if name is type, if so, is it enumeration type, if so, get "." and another name and then:
      // if yes or no, check if it is a name of an anumeration element, if yes, return true
      Aggregate ia = model.getEntityExtentInstances(
      //non-SF            EDefined_type.class);
      EType_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        //non-SF        EDefined_type inst_dt = ( EDefined_type )ia.getCurrentMemberObject(iter_inst);
        EType_declaration dec_dt = (EType_declaration) ia.getCurrentMemberObject(iter_inst);
        EDefined_type inst_dt = (EDefined_type) dec_dt.getDefinition(null);

        if (!(inst_dt.testDomain(null))) {
          String dt_name2;

          if (inst_dt.testName(null)) {
            dt_name2 = inst_dt.getName(null);
          } else {
            dt_name2 = "_UNSET_";
          }

          //				  System.out.println("##IER##  In isEnumerationReference dt domain NULL: " + dt_name2);
          continue;
        }

        EEntity underlying_type = inst_dt.getDomain(null);

        if (underlying_type instanceof EEnumeration_type) {
          String dt_name = inst_dt.getName(null);

// System.out.println("testing if enumeration type: " + dt_name + ", comparing to: " + name);
          if (dt_name.equalsIgnoreCase(name)) {
            is_type = true;
//System.out.println("found enumeration type: " + dt_name);

            if (global_kind2_global != Compiler2Constants.DOT) {
              // cannot be enumeration_type.enumeration_element, but the 1st is enumeration type,
              // so - error, unles an enumeration element may have the same name as some enumeration_type.
              //              System.out.println("##IER## (ERROR?) in isEnumerationRef - enumeration type ID not followed by DOT: " + dt_name);
              
              // continue;
				
              return false;
            }

            if (global_kind3_global != Compiler2Constants.SIMPLE_ID) {
              // although we have enumeration type followed by ., still no simple id - error
              //              System.out.println("##IER## (ERROR?) in isEnumerationRef - enumeration type ID followed by DOT not follewed by simple ID: " + dt_name);
              continue;

              // return false;
            }

            // first simple id is enumeration type, now we will search for the element in this enumeration only.
            String name2 = global_name3_global;
            EEnumeration_type the_enum = (EEnumeration_type) underlying_type;
//            A_string elements_enum = the_enum.getElements(null);
//            A_string elements_enum = the_enum.getLocal_elements(null);
//            A_string elements_enum = getElements(the_enum);
            Vector elements_enum = getElementsV(the_enum);
//            SdaiIterator iter_enum = elements_enum.createIterator();
						Iterator iter_enum = elements_enum.iterator();
						
//            while (iter_enum.next()) {
//              String current_enum = elements_enum.getCurrentMember(iter_enum);

							while (iter_enum.hasNext()) {
								String current_enum = (String)iter_enum.next();
              //              System.out.println("##IER##  inEnumerationReference - type + element: " + current_enum + ", " + name2);
              if (current_enum.equalsIgnoreCase(name2)) {
                //                System.out.println("##IER##  inEnumerationReference - element found - TRUE");
                return true;
              }
            }
          } else {
            // if name is not enumeration type name, perhaps directly element name
            if (global_kind2_global == Compiler2Constants.DOT) {
              //              System.out.println("##IER## (ERROR?) in isEnumerationRef - possible enumeration element ID followed by DOT: " + dt_name);
              continue;

              // return false;
            }

            EEnumeration_type the_enum = (EEnumeration_type) underlying_type;
//            A_string elements_enum = the_enum.getElements(null);
//            A_string elements_enum = the_enum.getLocal_elements(null);
//            A_string elements_enum = getElements(the_enum);
            Vector elements_enum = getElementsV(the_enum);
//            SdaiIterator iter_enum = elements_enum.createIterator();
						Iterator iter_enum = elements_enum.iterator();

//            while (iter_enum.next()) {
//              String current_enum = elements_enum.getCurrentMember(iter_enum);
							while (iter_enum.hasNext()) {
								String current_enum = (String)iter_enum.next();

              //              System.out.println("##IER##  inEnumerationReference - element only: " + current_enum + ", " + name);
              if (current_enum.equalsIgnoreCase(name)) {
                //                System.out.println("##IER##  inEnumerationReference - element found - TRUE");
                return true;
              }
            }
          }
        } // if defined type domain - enumeration type
      } // while defined types

      if (is_type) {
        // although enumeration type found, no matching element found.
//System.out.println("<1>: active_scope: " + active_scope);

//        System.out.println(
//              "ERROR in isEnumerationRef - enumeration type found followed by DOT and element ID, but matching element not found: " + 
//              name);

//haha


//System.out.println("tokens: " + Compiler2.getToken(0).image+ Compiler2.getToken(1).image + Compiler2.getToken(2).image + Compiler2.getToken(3).image + Compiler2.getToken(4).image + Compiler2.getToken(5).image + Compiler2.getToken(6).image + Compiler2.getToken(7).image + Compiler2.getToken(8).image);

							printErrorMsg("" + name + " - enumeration type found followed by DOT and element ID, but matching element not found (1)", null, true);


      }
    } catch (SdaiException e) {
      System.out.println("SdaiException in isEnumerationReference, name: " + name + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    return false;
  }


	 boolean isEnumerationReference_V35() {
    // System.out.println("-------- entering isEnumerationReference ------------- ");
    //		if (Compiler2.getToken(1).kind != Compiler2Constants.SIMPLE_ID) {
   	String attr_key = null;
    if (global_kind1_global != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    String name = global_name1_global;

// why? expressions have to be calculated in pass 5 only!!!!!!!

//    if (parser_pass != 4) {
    if (parser_pass != 5) {
      return false;
    }

		// if in entity and attribute name, then false

    if (findIfVariable(name)) {
      return false;
    }

    boolean is_type = false;

    //  System.out.println("####### inEnumerationReference: " + name);
    // check if name is type, if so, is it enumeration type, if so, get "." and another name and then: 
    // if yes or no, check if it is a name of an anumeration element, if yes, return true
    try {

      if (active_scope instanceof EEntity_definition) {
        EEntity_definition ed = (EEntity_definition) active_scope;
        EAttribute attr = findAttribute(name, ed, -1, null, attr_key);

        if (attr != null) {
          printDebug("isEnumerationRef - attribute_ref, not enumeration: " + name + ", entity: " + ed.getName(null));
        
          return false;
        } 
			}
      Aggregate ia = model.getEntityExtentInstances(EDefined_type.class);
      SdaiIterator iter_inst = ia.createIterator();
	
      while (iter_inst.next()) {
        EDefined_type inst_dt = (EDefined_type) ia.getCurrentMemberObject(iter_inst);

        if (!(inst_dt.testDomain(null))) {
          String dt_name2;

          if (inst_dt.testName(null)) {
            dt_name2 = inst_dt.getName(null);
          } else {
            dt_name2 = "_UNSET_";
          }

					//          System.out.println("## In isEnumerationReference dt domain NULL: " + dt_name2);

          continue;
        }

        EEntity underlying_type = inst_dt.getDomain(null);

        if (underlying_type instanceof EEnumeration_type) {
          String dt_name = inst_dt.getName(null);

          if (dt_name.equalsIgnoreCase(name)) {
            is_type = true;

            if (global_kind2_global != Compiler2Constants.DOT) {
              // cannot be enumeration_type.enumeration_element, but the 1st is enumeration type,
              // so - error, unles an enumeration element may have the same name as some enumeration_type.
              System.out.println(
                    "ERROR in isEnumerationRef - enumeration type ID not followed by DOT: " + 
                    dt_name);

              return false;
            }

            if (global_kind3_global != Compiler2Constants.SIMPLE_ID) {
              // although we have enumeration type followed by ., still no simple id - error
              System.out.println(
                    "ERROR in isEnumerationRef - enumeration type ID followed by DOT not follewed by simple ID: " + 
                    dt_name);

              return false;
            }

            // first simple id is enumeration type, now we will search for the element in this enumeration only.
            String name2 = global_name3_global;
            EEnumeration_type the_enum = (EEnumeration_type) underlying_type;
//            A_string elements_enum = the_enum.getElements(null);
//            A_string elements_enum = the_enum.getLocal_elements(null);
//            A_string elements_enum = getElements(the_enum);
            Vector elements_enum = getElementsV(the_enum);
//            SdaiIterator iter_enum = elements_enum.createIterator();
						Iterator iter_enum = elements_enum.iterator();
						
//            while (iter_enum.next()) {
//              String current_enum = elements_enum.getCurrentMember(iter_enum);
							while (iter_enum.hasNext()) {
								String current_enum = (String)iter_enum.next();

              // System.out.println("####### inEnumerationReference - type + element: " + current_enum + ", " + name);
              if (current_enum.equalsIgnoreCase(name2)) {
                return true;
              }
            }
          } // if enumeration type has correct name
        } // if defined type domain - enumeration type
      } // while defined types

      if (is_type) {
        // although enumeration type found, no matching element found.

//        System.out.println(
//              "ERROR in isEnumerationRef - enumeration type found followed by DOT and element ID, but matching element not found: " + 
//              name);

//haha

// System.out.println("tokens: " + Compiler2.getToken(0).image+ Compiler2.getToken(1).image + Compiler2.getToken(2).image + Compiler2.getToken(3).image + Compiler2.getToken(4).image + Compiler2.getToken(5).image + Compiler2.getToken(6).image + Compiler2.getToken(7).image + Compiler2.getToken(8).image);

							printErrorMsg("" + name + " - enumeration type found followed by DOT and element ID, but matching element not found (2)", null, true);

        return false;
      }


      // enumeration type not found
      ia = model.getEntityExtentInstances(EEnumeration_type.class);
      iter_inst = ia.createIterator();
      name = global_name1_global;

      while (iter_inst.next()) {
        EEnumeration_type inst = (EEnumeration_type) ia.getCurrentMemberObject(iter_inst);
//        A_string elements = inst.getElements(null);
//        A_string elements = inst.getLocal_elements(null);
//        A_string elements = getElements(inst);
        Vector elements = getElementsV(inst);
//        SdaiIterator iter_str = elements.createIterator();
				Iterator iter_str = elements.iterator();
				
//        while (iter_str.next()) {
//          String current = elements.getCurrentMember(iter_str);
				while (iter_str.hasNext()) {
					String current = (String)iter_str.next();

          // System.out.println("####### inEnumerationReference - current: " + current + ", " + name);
          if (current.equalsIgnoreCase(name)) {
            return true;
          }
        }
      } // while enumeration type - searching in all enumeration types
    } catch (SdaiException e) {
      System.out.println("SdaiException in isEnumerationReference, name: " + name + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    return false;
  }

  boolean alt_isEnumerationReference() {
    // System.out.println("-------- entering isEnumerationReference ------------- ");
    //		if (Compiler2.getToken(1).kind != Compiler2Constants.SIMPLE_ID) {
    if (global_kind1_global != Compiler2Constants.SIMPLE_ID) {
      return false;
    }

    String name = global_name1_global;

    // System.out.println("####### inEnumerationReference: " + name);
    // check if name is type, if so, is it enumeration type, if so, get "." and another name and then: 
    // if yes or no, check if it is a name of an anumeration element, if yes, return true
    try {
      Aggregate ia = model.getEntityExtentInstances(EEnumeration_type.class);
      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EEnumeration_type inst = (EEnumeration_type) ia.getCurrentMemberObject(iter_inst);
//        A_string elements = inst.getElements(null);
//        A_string elements = inst.getLocal_elements(null);
//        A_string elements = getElements(inst);
        Vector elements = getElementsV(inst);
//        SdaiIterator iter_str = elements.createIterator();
        Iterator iter_str = elements.iterator();

//        while (iter_str.next()) {
//          String current = elements.getCurrentMember(iter_str);
        while (iter_str.hasNext()) {
          String current = (String)iter_str.next();

          // System.out.println("####### inEnumerationReference - current: " + current + ", " + name);
          if (current.equalsIgnoreCase(name)) {
            return true;
          }
        }
      }
    } catch (SdaiException e) {
      //			System.out.println("SdaiException in isEnumerationReference, name: " + name);
      System.out.println("SdaiException in isEnumerationReference, name: " + name + 
                         ", description: " + e.getErrorDescription(e.getErrorId()));
      e.printStackTrace();

      return false;
    }

    return false;
  }

  static jsdai.dictionary.ESchema_definition getSchema_definitionFromModel2(SdaiModel sm)
    throws SdaiException {
    Aggregate ia = sm.getEntityExtentInstances(jsdai.dictionary.ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      jsdai.dictionary.ESchema_definition inst = (jsdai.dictionary.ESchema_definition) ia.getCurrentMemberObject(
                                                       iter_inst);

      return inst;
    }

    return null;
  }

  static void updateSchemaInstances(jsdai.dictionary.ESchema_definition sds, SdaiRepository repo, 
                                    String repository_name, String express_file, 
                                    boolean instance_flag, String instance_name)
                             throws SdaiException {
    String current_name = null;

    if (express_file != null) {
      int id = express_file.lastIndexOf('.');
      current_name = express_file.substring(0, id) + "_INSTANCE";
      id = current_name.lastIndexOf(File.separator);

      if (id != -1) {
        current_name = current_name.substring(id + 1, current_name.length());
      }

      current_name = current_name.toUpperCase();

      // System.out.println("#####__#### current file name: " + current_name);	
    }

    if (instance_flag) {
      current_name = instance_name;
    }

    //		jsdai.dictionary.ESchema_definition sds = null;
    //		ESchema_definition sds = null;
    ASdaiModel models2 = repo.getModels();
    SdaiModel model2;
    SdaiIterator iter_model;

    /*
        SdaiIterator iter_model = models2.createIterator();
        while (iter_model.next()) {
          model2 = models2.getCurrentMember(iter_model);
          String model_name = model2.getName();
          if (model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) {
            sds = getSchema_definitionFromModel2(model2);
          }
        }
    */
    ASchemaInstance sis = repo.getSchemas();
    SdaiIterator iter_sis = sis.createIterator();
    SchemaInstance si;
    SchemaInstance si1;
    SchemaInstance si2 = null;
    SchemaInstance si3 = null;
	
    while (iter_sis.next()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      // System.out.println("#### Schema instance name: " + si_name);
      if (si_name.equalsIgnoreCase(repository_name + "_INSTANCE")) {
        si.delete();

        break;
      }
    }

    iter_sis = sis.createIterator();

    while (iter_sis.next()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      // System.out.println("Current schema instance: " + si_name);
      if (si_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_INSTANCE")) {
        si.delete();

        break;
      }
    }

    if (express_file != null) {
      iter_sis = sis.createIterator();

      while (iter_sis.next()) {
        si = sis.getCurrentMember(iter_sis);

        String si_name = si.getName();

        if (si_name.equalsIgnoreCase(current_name)) {
          si.delete();

          break;
        }
      }
    }


    //------- for schema instances of all schemas -------
    // if  the schema_instance name does not contain _INSTANCE at the end - delete
    iter_sis = sis.createIterator();

    // iter_sis.end();
    while (iter_sis.next()) {
      ;
    }

    while (iter_sis.previous()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      // System.out.println("Current schema instance: " + si_name);
      if (si_name.length() < 10) {
        si.delete();
      } else {
        String si_name_end = si_name.substring(si_name.length() - 9);

        if (!(si_name_end.equalsIgnoreCase("_INSTANCE"))) {
          si.delete();
        }
      }
    }


    //----------------------------------------------------
    si1 = repo.createSchemaInstance("SDAI_DICTIONARY_SCHEMA_INSTANCE", sds);

    // si = repo.createSchemaInstance(repository_name + "_INSTANCE", sds);
    if (current_name != null) {
      if (!(current_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_INSTANCE"))) {
        if (!(current_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_INSTANCE"))) {
          si2 = repo.createSchemaInstance(current_name, sds);
        }
      }
    }

    iter_model = models2.createIterator();

    while (iter_model.next()) {
      model2 = models2.getCurrentMember(iter_model);

      String model_name = model2.getName();

      // System.out.println("Current HAHA model: " + model_name);
      String part_model_name;

			if (skipModel(model_name)) {
				continue;
			}

      // System.out.println("Current OIOI model: " + model_name);
      // original case is required for instance names by Lothar, but I don't have original case.
      String current_model_schema_name = model_name.substring(0, model_name.length() - 16)
                                                   .toLowerCase();
      si3 = repo.createSchemaInstance(current_model_schema_name, sds);


      // si.addSdaiModel(model2);
      si1.addSdaiModel(model2);

      if (current_name != null) {
        if (!(current_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_INSTANCE"))) {
          if (!(current_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_INSTANCE"))) {
            if (model2.getMode() == SdaiModel.READ_WRITE) {
              si2.addSdaiModel(model2);
            }
          }
        }
      }

      // ------- adding models to each per-schema schema_instances
      Aggregate declarations = model2.getEntityExtentInstances(EDeclaration.class);
      SdaiIterator iter_d = declarations.createIterator();
      TreeSet sorter_eliminator = new TreeSet(new SorterForModels());
      sorter_eliminator.add(model2); // the current model should be here too, just to make sure, although almost impossible that there are no local declarations

      while (iter_d.next()) {
        EDeclaration a_declaration = (EDeclaration) declarations.getCurrentMemberObject(iter_d);
// System.out.println("declaration: " + a_declaration + ", model: " + model2);
        if (a_declaration.testDefinition(null)) { // for corrupt data because of -replace_schemas
        	EEntity declaration_definition = (EEntity) a_declaration.getDefinition(null);
        	SdaiModel owner = declaration_definition.findEntityInstanceSdaiModel();


        // do we need to check for duplicates? - associated_models is a set, so it should take care of it itself
        // do we need to sort them and add in alphabetic order - for set the order is indeterminate, but in reality it exists
        // maybe I put them to a sorting TreeSet first, to ensure that only one instance is present, and that they are sorted.
        	sorter_eliminator.add(owner);
      	}
      }

      for (Iterator i = sorter_eliminator.iterator(); i.hasNext();) {
        SdaiModel current_model = (SdaiModel) i.next();
        si3.addSdaiModel(current_model);
      }
    } // through all the models

    //		SdaiIterator iter_sis2 = sis.createIterator();
    //	  while (iter_sis2.next()) {
    //			si = sis.getCurrentMember(iter_sis2);
    //			String si_name = si.getName();
    // 			System.out.println("Current schema instance: " + si_name);
    //		}
  }

  static void deleteSchemaInstances(jsdai.dictionary.ESchema_definition sds, SdaiRepository repo, 
                                    String repository_name, String express_file, 
                                    boolean instance_flag, String instance_name, 
                                    boolean annex_a_flag, boolean keep_flag, Vector keep_names, 
                                    boolean keep_all_flag)
                             throws SdaiException {
    ASdaiModel models2 = repo.getModels();
    SdaiModel model2;
    SdaiIterator iter_model;
    ASchemaInstance sis = repo.getSchemas();
    SdaiIterator iter_sis;
    SchemaInstance si;
    SchemaInstance si1;
    SchemaInstance si2 = null;
    SchemaInstance si3 = null;

    iter_sis = sis.createIterator();

    // iter_sis.end();
    while (iter_sis.next()) {
      ;
    }

    while (iter_sis.previous()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      // System.out.println("Current schema instance: " + si_name);
      if (si_name.equalsIgnoreCase(instance_name)) {
        if (instance_flag) {
          continue; // do not delete if switch -instance my_instance
        }
      }

      if (keep_flag) {
        boolean not_delete = false;

        for (int j = 0; j < keep_names.size(); j++) {
          String keep_name = (String) keep_names.elementAt(j);

          if (si_name.equalsIgnoreCase(keep_name)) {
            not_delete = true;

            continue; // do not delete if switch -instance my_instance
          }
        }

        if (not_delete) {
          // System.out.println("## NOT DELETE: " + si_name);				
          continue;
        }
      }

      if (si_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_INSTANCE")) {
        if (annex_a_flag) {
          continue; // do not delete if switch -annex_a_instance
        } else {
          si.delete();

          continue;
        }
      }

      if (!keep_all_flag) {
        if (si_name.length() < 10) { // cannot containt "_INSTANCE" - do not delete, unless it is the instance_name.

          continue;
        } else {
          String si_name_end = si_name.substring(si_name.length() - 9);

          if (si_name_end.equalsIgnoreCase("_INSTANCE")) {
            // System.out.println("## Deleting schema instance: " + si_name);						
            si.delete();
          }
        }
      }
    }
  }

  static void updateSchemaInstances(jsdai.dictionary.ESchema_definition sds, SdaiRepository repo, 
                                    String repository_name, String express_file)
                             throws SdaiException {
    String current_name = null;

    if (express_file != null) {
      int id = express_file.lastIndexOf('.');
      current_name = express_file.substring(0, id) + "_INSTANCE";
    }

    //		jsdai.dictionary.ESchema_definition sds = null;
    //		ESchema_definition sds = null;
    ASdaiModel models2 = repo.getModels();
    SdaiModel model2;
    SdaiIterator iter_model;

    /*
        SdaiIterator iter_model = models2.createIterator();
        while (iter_model.next()) {
          model2 = models2.getCurrentMember(iter_model);
          String model_name = model2.getName();
          if (model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) {
            sds = getSchema_definitionFromModel2(model2);
          }
        }
    */
    ASchemaInstance sis = repo.getSchemas();
    SdaiIterator iter_sis = sis.createIterator();
    SchemaInstance si;
    SchemaInstance si1;
    SchemaInstance si2 = null;
    SchemaInstance si3 = null;

    while (iter_sis.next()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      // System.out.println("#### Schema instance name: " + si_name);
      if (si_name.equalsIgnoreCase(repository_name + "_INSTANCE")) {
        si.delete();

        break;
      }
    }

    iter_sis = sis.createIterator();

    while (iter_sis.next()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      // System.out.println("Current schema instance: " + si_name);
      if (si_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_INSTANCE")) {
        si.delete();

        break;
      }
    }

    if (express_file != null) {
      iter_sis = sis.createIterator();

      while (iter_sis.next()) {
        si = sis.getCurrentMember(iter_sis);

        String si_name = si.getName();

        if (si_name.equalsIgnoreCase(current_name)) {
          si.delete();

          break;
        }
      }
    }


    //------- for schema instances of all schemas -------
    // if  the schema_instance name does not contain _INSTANCE at the end - delete
    iter_sis = sis.createIterator();

    // iter_sis.end();
    while (iter_sis.next()) {
      ;
    }

    while (iter_sis.previous()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      // System.out.println("Current schema instance: " + si_name);
      if (si_name.length() < 10) {
        si.delete();
      } else {
        String si_name_end = si_name.substring(si_name.length() - 9);

        if (!(si_name_end.equalsIgnoreCase("_INSTANCE"))) {
          si.delete();
        }
      }
    }


    //----------------------------------------------------
    si1 = repo.createSchemaInstance("SDAI_DICTIONARY_SCHEMA_INSTANCE", sds);
    si = repo.createSchemaInstance(repository_name + "_INSTANCE", sds);

    if (current_name != null) {
      si2 = repo.createSchemaInstance(current_name, sds);
    }

    iter_model = models2.createIterator();

    while (iter_model.next()) {
      model2 = models2.getCurrentMember(iter_model);

      String model_name = model2.getName();

      if (current_name != null) {
        if (model2.getMode() == SdaiModel.READ_WRITE) {
          si2.addSdaiModel(model2);
        }
      }

      // System.out.println("Current HAHA model: " + model_name);
      String part_model_name;

			if (skipModel(model_name)) {
				continue;
			}

/*
      if (model_name.length() > 15) {
        part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          // System.out.println("Eliminating documentation model: " + model_name);
          continue;
        }
      }

      if (model_name.length() > 13) {
        part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          // System.out.println("Eliminating expressions model: " + model_name);
          continue;
        }
      }

      if (model_name.length() > 9) {
        part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          // System.out.println("Eliminating express model: " + model_name);
          continue;
        }
      }

      if (model_name.length() > 6) {
        part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          // System.out.println("Eliminating java model: " + model_name);
          continue;
        }
      }

      if (model_name.length() > 13) {
        part_model_name = model_name.substring(model_name.length() - 13);

        if (part_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
          // System.out.println("Eliminating mapping_data model: " + model_name);
          continue;
        }
      }

      if (model_name.length() > 16) {
        part_model_name = model_name.substring(model_name.length() - 16);

        //			System.out.println("Part model name: " + part_model_name);
        if (!part_model_name.equalsIgnoreCase("_DICTIONARY_DATA")) {
          //			System.out.println("Eliminating not dictionary data model: " + model_name + ", part: " + part_model_name);
          continue;
        }
      }

      if (model_name.length() <= 16) {
        // System.out.println("Eliminating short name model: " + model_name);
        continue;
      }

*/

      // System.out.println("Current OIOI model: " + model_name);
      // original case is required for instance names by Lothar, but I don't have original case.
      String current_model_schema_name = model_name.substring(0, model_name.length() - 16)
                                                   .toLowerCase();
      si3 = repo.createSchemaInstance(current_model_schema_name, sds);
      si.addSdaiModel(model2);
      si1.addSdaiModel(model2);

      if (current_name != null) {
        if (model2.getMode() == SdaiModel.READ_WRITE) {
          //					si2.addSdaiModel(model2);
        }
      }

      // ------- adding models to each per-schema schema_instances
      Aggregate declarations = model2.getEntityExtentInstances(EDeclaration.class);
      SdaiIterator iter_d = declarations.createIterator();
      TreeSet sorter_eliminator = new TreeSet(new SorterForModels());
      sorter_eliminator.add(model2); // the current model should be here too, just to make sure, althogh almost impossible that there are no local declarations

      while (iter_d.next()) {
        EDeclaration a_declaration = (EDeclaration) declarations.getCurrentMemberObject(iter_d);
        EEntity declaration_definition = (EEntity) a_declaration.getDefinition(null);
        SdaiModel owner = declaration_definition.findEntityInstanceSdaiModel();


        // do we need to check for duplicates? - associated_models is a set, so it should take care of it itself
        // do we need to sort them and add in alphabetic order - for set the order is indeterminate, but in reality it exists
        // maybe I put them to a sorting TreeSet first, to ensure that only one instance is present, and that they are sorted.
        sorter_eliminator.add(owner);
      }

      for (Iterator i = sorter_eliminator.iterator(); i.hasNext();) {
        SdaiModel current_model = (SdaiModel) i.next();
        si3.addSdaiModel(current_model);
      }
    } // through all the models

    //		SdaiIterator iter_sis2 = sis.createIterator();
    //	  while (iter_sis2.next()) {
    //			si = sis.getCurrentMember(iter_sis2);
    //			String si_name = si.getName();
    // 			System.out.println("Current schema instance: " + si_name);
    //		}
  }

  // from version 2.4
  static void deleteRelatedModels(SdaiRepository repo, String express_file, boolean instance_flag, 
                                  String instance_name, boolean keep_flag, Vector keep_names)
                           throws SdaiException {
    // int id = express_file.lastIndexOf('.');
    // String current_name = express_file.substring(0, id) + "_INSTANCE";		
    String current_name = null;

    if (express_file != null) {
      int id = express_file.lastIndexOf('.');
      current_name = express_file.substring(0, id) + "_INSTANCE";
      id = current_name.lastIndexOf(File.separator);

      if (id != -1) {
        current_name = current_name.substring(id + 1, current_name.length());
      }

      current_name = current_name.toUpperCase();

      // System.out.println("#####__#### current file name: " + current_name);	
    }

    if (instance_flag) {
      current_name = instance_name;
    } else if (keep_flag) {
      current_name = (String) keep_names.elementAt(0);

      //  System.out.println("## current_name: " + current_name);		
    }

    ASchemaInstance sis = repo.getSchemas();
    SdaiIterator iter_sis = sis.createIterator();
    SchemaInstance si;

    while (iter_sis.next()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      if (si_name.equalsIgnoreCase(current_name)) {
        // System.out.println("### instance found: " + si_name);
        // delete models here.
        for (;;) {
          ASdaiModel models2 = si.getAssociatedModels();

          if (models2.getMemberCount() <= 0) {
            // System.out.println("#### 0 models");		
            break;
          }

          SdaiIterator iter_model = models2.createIterator();

          while (iter_model.next()) {
            SdaiModel model2 = models2.getCurrentMember(iter_model);


            // System.out.println("#### Removing model from schema instance: " + model2.getName());
            // System.out.println("#### removing model: " + model2.getName());
            si.removeSdaiModel(model2);
            model2.deleteSdaiModel();
          }
        }
      }
    }
  }

  static void deleteRelatedModels(SdaiRepository repo, String express_file)
                           throws SdaiException {
    int id = express_file.lastIndexOf('.');
    String current_name = express_file.substring(0, id) + "_INSTANCE";
    ASchemaInstance sis = repo.getSchemas();
    SdaiIterator iter_sis = sis.createIterator();
    SchemaInstance si;

    while (iter_sis.next()) {
      si = sis.getCurrentMember(iter_sis);

      String si_name = si.getName();

      if (si_name.equalsIgnoreCase(current_name)) {
        // delete models here.
        for (;;) {
          ASdaiModel models2 = si.getAssociatedModels();

          if (models2.getMemberCount() <= 0) {
            break;
          }

          SdaiIterator iter_model = models2.createIterator();

          while (iter_model.next()) {
            SdaiModel model2 = models2.getCurrentMember(iter_model);


            // System.out.println("#### Removing model from schema instance: " + model2.getName());
            si.removeSdaiModel(model2);
            model2.deleteSdaiModel();
          }
        }
      }
    }
  }


  static void removeDuplicateDataTypes(SdaiRepository repo) throws SdaiException {
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();
    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }
//			removeDuplicateDataTypesInModel(model2);
			removeDuplicateDataTypesInModel2(model2);
		}
  }

  static void removeDuplicateDataTypesInModel(SdaiModel model2) throws SdaiException {
    Aggregate ia = model2.getInstances(EData_type.class);
//    Aggregate ia = model2.getEntityExtentInstances(EData_type.class);
    SdaiIterator iter_outer = ia.createIterator();

    while (iter_outer.next()) {
      EData_type inst_outer = (EData_type) ia.getCurrentMemberObject(iter_outer);
      String outer_name = inst_outer.getName(null);
	    SdaiIterator iter_inner = ia.createIterator();
	    while (iter_inner.next()) {
  	    EData_type inst_inner = (EData_type) ia.getCurrentMemberObject(iter_inner);
    	  if (inst_inner == inst_outer) {
    	  	continue;
    	  }
    	  String inner_name = inst_inner.getName(null);
    	  if (inner_name.equalsIgnoreCase(outer_name)) {
// System.out.println("--XX-- deleting duplicate data_type instance: " + inst_inner);	
// printUsers("move to", inst_outer);		
// printUsers("move from",inst_inner);		
	AEntity rrr = new AEntity();
	inst_outer.findEntityInstanceUsers(null, rrr);
 					inst_outer.moveUsersFrom(inst_inner);
	SdaiIterator it_users = rrr.createIterator();
	while (it_users.next()) {
		EEntity usr = rrr.getCurrentMemberEntity(it_users);
//		System.out.println("vanished user: " + usr);
	}
//printUsers("remains", inst_outer);		
// printUsers("to delete",inst_inner);		
    	  	inst_inner.deleteApplicationInstance();
    	  }
			}
		}
	}






  static void removeDuplicateDataTypesInModel2(SdaiModel model2) throws SdaiException {
//System.out.println("DELETING instances of data types with duplicate names");	
		clearModelMarks(model2);
    Aggregate ia = model2.getInstances(EData_type.class);
//    Aggregate ia = model2.getEntityExtentInstances(EData_type.class);
    SdaiIterator iter_outer = ia.createIterator();

    while (iter_outer.next()) {
      EData_type inst_outer = (EData_type) ia.getCurrentMemberObject(iter_outer);
      if (inst_outer.getTemp() != null) continue;
      String outer_name = inst_outer.getName(null);
	    SdaiIterator iter_inner = ia.createIterator();
	    while (iter_inner.next()) {
  	    EData_type inst_inner = (EData_type) ia.getCurrentMemberObject(iter_inner);
	      if (inst_inner.getTemp() != null) continue;
    	  if (inst_inner == inst_outer) {
    	  	continue;
    	  }
    	  String inner_name = inst_inner.getName(null);
    	  if (inner_name.equalsIgnoreCase(outer_name)) {
 //System.out.println("--XX-- marking for deleting duplicate data_type instance: " + inst_inner);	
// printUsers("move to", inst_outer);		
// printUsers("move from",inst_inner);		
					AEntity rrr = new AEntity();
					inst_outer.findEntityInstanceUsers(null, rrr);
 					inst_outer.moveUsersFrom(inst_inner);
					SdaiIterator it_users = rrr.createIterator();
					while (it_users.next()) {
						EEntity usr = rrr.getCurrentMemberEntity(it_users);
// 		System.out.println("vanished user: " + usr);
					}
// printUsers("remains", inst_outer);		
// printUsers("to delete",inst_inner);		
//    	  	inst_inner.deleteApplicationInstance();
					inst_inner.setTemp(inst_inner);
    		}
			} // inner
		} // outer

		
    ArrayList to_delete = new ArrayList();
//		EData_type to_delete = new EData_type();
		iter_outer.beginning();
//		iter_outer.end();
    while (iter_outer.next()) {
//    while (iter_outer.previous()) {
      EData_type inst_outer = (EData_type) ia.getCurrentMemberObject(iter_outer);
      if (inst_outer.getTemp() != null) {
 // System.out.println("--XX-- actually deleting duplicate data_type instance: " + inst_outer);	
//				to_delete.addUnordered(inst_outer);
					to_delete.add(inst_outer);
//    		inst_outer.deleteApplicationInstance();
//    		iter_outer.remove();
      }
		}

/*
		SdaiIterator iter_delete = to_delete.createIterator();
		while (iter_delete.next()) {
      EData_type instxxx = (EData_type) to_delete.getCurrentMemberObject(iter_delete);
  System.out.println("--XX-- actually deleting duplicate data_type instance: " + instxxx);	
   		instxxx.deleteApplicationInstance();
		}
*/

		Iterator iter_del = to_delete.iterator();
		while (iter_del.hasNext()) {
			EEntity instxxx = (EEntity) iter_del.next();
//  System.out.println("--XX-- actually deleting duplicate data_type instance: " + instxxx);	
   		instxxx.deleteApplicationInstance();
		}

	}






  static void removeDuplicateDataTypesInModel2_old(SdaiModel model2) throws SdaiException {
		clearModelMarks(model2);
    Aggregate ia = model2.getInstances(EData_type.class);
//    Aggregate ia = model2.getEntityExtentInstances(EData_type.class);
    SdaiIterator iter_outer = ia.createIterator();

    while (iter_outer.next()) {
      EData_type inst_outer = (EData_type) ia.getCurrentMemberObject(iter_outer);
      if (inst_outer.getTemp() != null) continue;
      String outer_name = inst_outer.getName(null);
	    SdaiIterator iter_inner = ia.createIterator();
	    while (iter_inner.next()) {
  	    EData_type inst_inner = (EData_type) ia.getCurrentMemberObject(iter_inner);
	      if (inst_inner.getTemp() != null) continue;
    	  if (inst_inner == inst_outer) {
    	  	continue;
    	  }
    	  String inner_name = inst_inner.getName(null);
    	  if (inner_name.equalsIgnoreCase(outer_name)) {
// System.out.println("--XX-- deleting duplicate data_type instance: " + inst_inner);	
// printUsers("move to", inst_outer);		
// printUsers("move from",inst_inner);		
	AEntity rrr = new AEntity();
	inst_outer.findEntityInstanceUsers(null, rrr);
 					inst_outer.moveUsersFrom(inst_inner);
	SdaiIterator it_users = rrr.createIterator();
	while (it_users.next()) {
		EEntity usr = rrr.getCurrentMemberEntity(it_users);
// 		System.out.println("vanished user: " + usr);
	}
// printUsers("remains", inst_outer);		
// printUsers("to delete",inst_inner);		
//    	  	inst_inner.deleteApplicationInstance();
						inst_inner.setTemp(inst_inner);
    	  }
			}
		}
		iter_outer.beginning();
    while (iter_outer.next()) {
      EData_type inst_outer = (EData_type) ia.getCurrentMemberObject(iter_outer);
      if (inst_outer.getTemp() != null) {
    		inst_outer.deleteApplicationInstance();
      }
		}
	}




static void printUsers(String msg, EEntity inst) throws SdaiException {

	System.out.println(msg + " " + inst + " users:");
	AEntity rrr = new AEntity();
	inst.findEntityInstanceUsers(null, rrr);
	SdaiIterator it_users = rrr.createIterator();
	while (it_users.next()) {
		EEntity usr = rrr.getCurrentMemberEntity(it_users);
		System.out.println("user: " + usr);
	}

}


	static void moveReferencesInModel(SdaiModel model2, EData_type to_this, EData_type from_this) throws SdaiException {
		
	}

  static void generateJarBatch(SdaiRepository repo, String jar_add, String jar_file, 
                               String jar_create, String bat_file)
                        throws java.io.IOException, SdaiException {
    // let's have the name of the file = the name of the express file
    FileOutputStream fos = new FileOutputStream(bat_file);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();
    boolean jar_created;
    String jar_string;

    if (jar_create == null) {
      jar_created = true;
      jar_string = jar_add;
    } else {
      jar_created = false;
      jar_string = jar_create;
    }

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      String model_name = model2.getName();

      if ((model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) || 
              (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
        continue;
      }

			if (skipModelM(model_name)) {
				continue;
			}

/*
      if (model_name.length() > 15) {
        String part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          continue;
        }
      }

      if (model_name.length() > 13) {
        String part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          continue;
        }
      }

      if (model_name.length() > 9) {
        String part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          continue;
        }
      }

      if (model_name.length() > 6) {
        String part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          continue;
        }
      }

*/

      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String end_model_name = model_name.substring(model_name.length() - 13);
      String schema_directory;

      if (end_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
        schema_directory = "M" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      } else {
        schema_directory = "S" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      }

      String directory_string1 = "jsdai" + File.separator + schema_directory + File.separator + 
                                 "m*";
      String directory_string2 = "jsdai" + File.separator + schema_directory + File.separator + 
                                 "*.class";
      pw.println(jar_string + " " + jar_file + " " + directory_string1);

      if (!jar_created) {
        jar_created = true;
        jar_string = jar_add;
      }

      pw.println(jar_string + " " + jar_file + " " + directory_string2);
    }

    pw.flush();
    pw.close();
  }

  static void generateZipBatch(SdaiRepository repo, String zip_string, String zip_file, 
                               String bat_file) throws java.io.IOException, SdaiException {
    // let's have the name of the file = the name of the express file
    FileOutputStream fos = new FileOutputStream(bat_file);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      String model_name = model2.getName();

	      if ((model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) || 
              (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
        continue;
      }

			if (skipModelM(model_name)) {
				continue;
			}

/*
      if (model_name.length() > 15) {
        String part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          continue;
        }
      }

      if (model_name.length() > 13) {
        String part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          continue;
        }
      }

      if (model_name.length() > 9) {
        String part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          continue;
        }
      }

      if (model_name.length() > 6) {
        String part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          continue;
        }
      }

*/
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String end_model_name = model_name.substring(model_name.length() - 13);
      String schema_directory;

      if (end_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
        schema_directory = "M" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      } else {
        schema_directory = "S" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      }

      String directory_string1 = "jsdai" + File.separator + schema_directory + File.separator + 
                                 "*.java";
      String directory_string2 = "jsdai" + File.separator + schema_directory + File.separator + 
                                 "m*";
      pw.println(zip_string + " " + zip_file + " " + directory_string1);
      pw.println(zip_string + " " + zip_file + " " + directory_string2);
    }

    pw.flush();
    pw.close();
  }

  static void generateModelFile() throws java.io.IOException {
		if (output_dir != null) {
			model_file_name = output_dir + File.separator + model_file_name;
		}
    FileOutputStream fos = new FileOutputStream(model_file_name);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

		// go through the vector compiled_models
		
		Iterator iter = compiled_models.iterator();
		while(iter.hasNext()) {
			String compiled_model = (String)iter.next();
      pw.println(compiled_model);
		}

    pw.flush();
    pw.close();
  }

  static void generateCompileBatch(SdaiRepository repo, String compile_string, String bat_file, 
                                   String log_file) throws java.io.IOException, SdaiException {
    // let's have the name of the file = the name of the express file
    FileOutputStream fos = new FileOutputStream(bat_file);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    boolean first_time = true;
    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      String model_name = model2.getName();

      if ((model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) || 
              (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
        continue;
      }
//      if (!model_name.endsWith("_DICTIONARY_DATA")) {
//      	continue;
//      }
      
			if (skipModelM(model_name)) {
				continue;
			}
      
/*
      if (model_name.length() > 15) {
        String part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          continue;
        }
      }

      if (model_name.length() > 13) {
        String end_model_name = model_name.substring(model_name.length() - 13);

        if (end_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
          continue;
        }
      }

      if (model_name.length() > 9) {
        String part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          continue;
        }
      }

      if (model_name.length() > 6) {
        String part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          continue;
        }
      }
*/
  
      //     	pw.println("### END: " + end_model_name);
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String schema_directory = "S" + schema_name.substring(0, 1).toUpperCase() + 
                                schema_name.substring(1).toLowerCase();
      String directory_string = "jsdai" + File.separator + schema_directory + File.separator + 
                                "*.java";
			if (first_time) {
				first_time = false;

      	if (log_file == null) {
        	pw.println(compile_string + " " + directory_string);
      	} else {
        	pw.println(compile_string + " " + directory_string + " >" + log_file + " 2>" + log_file + "_err");
      	}
    		
  		} else {
      	if (log_file == null) {
        	pw.println(compile_string + " " + directory_string);
      	} else {
        	pw.println(compile_string + " " + directory_string + " >>" + log_file + " 2>>" + log_file + "_err");
      	}
  		}
    }

    pw.flush();
    pw.close();
  }

  static void copyFile_old(File inputFile, File outputFile) {
    try {
      FileInputStream fis = new FileInputStream(inputFile);
      FileOutputStream fos = new FileOutputStream(outputFile);
      int c;

      while ((c = fis.read()) != -1) {
        fos.write(c);
      }

  	    fis.close();
      fos.close();
    } catch (FileNotFoundException e) {
      System.out.println("Inserting binaries failed: " + e);
    } catch (IOException e) {
      System.out.println("Inserting binaries failed: " + e);
    }
  }

  static void copyFile(URL inputURL, File outputFile) {
    try {
      InputStream is = inputURL.openStream();
      FileOutputStream fos = new FileOutputStream(outputFile);
      int c;

	  byte[] buffer = new byte[8 * 1024];
	  int count = 0;
	  do {
		  fos.write(buffer, 0, count);
		  count = is.read(buffer, 0, buffer.length);
	  } while (count != -1);

      is.close();
      fos.close();
    } catch (FileNotFoundException e) {
      System.out.println("Inserting binaries failed: " + e);
    } catch (IOException e) {
      System.out.println("Inserting binaries failed: " + e);
    }
  }

  static void insertBinaries_old(SdaiRepository repo, X_AllSchemas x_data, boolean to_serialize) throws SdaiException, IOException {
//  static void insertBinaries(SdaiRepository repo) throws SdaiException {
    // go through all models, and, if the model is ReadWrite, renameTo() it into the corresponding java package directory.
    int str_length;
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      String model_name = model2.getName();

      if ((model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) || 
              (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
        ;

        // continue;
      }

			if (skipModelM(model_name)) {
				continue;
			}

/*
      if (model_name.length() > 15) {
        String part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          continue;
        }
      }

      if (model_name.length() > 13) {
        String part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          continue;
        }
      }

      if (model_name.length() > 9) {
        String part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          continue;
        }
      }

      if (model_name.length() > 6) {
        String part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          continue;
        }
      }
*/
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String end_model_name = model_name.substring(model_name.length() - 13);
      String schema_directory;

      if (end_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
        schema_directory = "M" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      } else if (model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) {
        schema_directory = "dictionary";
      } else if (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA")) {
        schema_directory = "mapping";
      } else {
        schema_directory = "S" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      }

      String source_path = repo.getLocation();

      //			String source_path = "REPOSITORIES" + File.separator + "ExpressCompilerRepo";
      String destination_path = "jsdai" + File.separator + schema_directory;

      String model_id = model2.getId();
      File source = new File(source_path, model_id);

      //			File destination = new File(destination_path, model_id);
      File destination = new File(destination_path, model_name);

      if (source.canRead()) {
        printDebug("CAN READ SOURCE: " + source_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT READ SOURCE: " + source_path + File.separator + model_name);
      }

      if (source.canWrite()) {
        printDebug("CAN WRITE SOURCE: " + source_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT WRITE SOURCE: " + source_path + File.separator + model_name);
      }

      if (destination.canRead()) {
        printDebug("CAN READ DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT READ DESTINATION: " + destination_path + File.separator + model_name);
      }

      if (destination.canWrite()) {
        printDebug("CAN WRITE DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT WRITE DESTINATION: " + destination_path + File.separator + 
                   model_name);
      }

      copyFile_old(source, destination);

			// flag_serialize = false;
		
      flag_serialize = false;
    	if (flag_serialize) {
    		if (x_data.children != null) {
      		for (int i = 0; i < x_data.children.length; ++i) {
        		Node child = x_data.children[i];
        		if (child instanceof X_SchemaDecl) {
							if (schema_name.equalsIgnoreCase(((X_SchemaDecl)child).schema_name)) {
								// serialize etc.
				    	  String x_name = schema_name.toUpperCase() + "_EXPRESSION_DATA";
//    						FileOutputStream x_ostream = new FileOutputStream(destination_path + File.separator + x_name);
    						FileOutputStream x_ostream = new FileOutputStream(source_path + File.separator + x_name);
    						ObjectOutputStream p = new ObjectOutputStream(x_ostream);
    						p.writeObject(child);
						    p.flush();
    						x_ostream.close();
							}        	
        		}
      		} // for
    		}
    	}
    }
  }

	static void normalizeIDs(SdaiRepository repo) throws SdaiException {
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();
    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      // 1.
      // normalise schema names - to all UPPERCASE
			// ENTITY generic_schema_definition
			// name : express_id;
      Aggregate a_sd = model2.getEntityExtentInstances(EGeneric_schema_definition.class);
      SdaiIterator iter_sd = a_sd.createIterator();
    	while (iter_sd.next()) {
	      EGeneric_schema_definition sd_inst = (EGeneric_schema_definition) a_sd.getCurrentMemberObject(iter_sd);
  	    String sd_name = sd_inst.getName(null);
    		sd_inst.setName(null, sd_name.toUpperCase());
      }

			// 2.
		  // normalize named types names - changed to data_type names
      // perhaps better just all data types:
      // ENTITY data_type
			// name : express_id;
      Aggregate a_dt = model2.getEntityExtentInstances(EData_type.class);
      SdaiIterator iter_dt = a_dt.createIterator();
    	while (iter_dt.next()) {
	      EData_type dt_inst = (EData_type) a_dt.getCurrentMemberObject(iter_dt);
  	    String dt_name = dt_inst.getName(null);
    		dt_inst.setName(null, dt_name.toLowerCase());
      }
		
    	// 3.
    	// normalize attribute names
      Aggregate a_attr = model2.getEntityExtentInstances(EAttribute.class);
      SdaiIterator iter_attr = a_attr.createIterator();
    	while (iter_attr.next()) {
	      EAttribute attr_inst = (EAttribute) a_attr.getCurrentMemberObject(iter_attr);
  	    String attr_name = attr_inst.getName(null);
    		attr_inst.setName(null, attr_name.toLowerCase());
      }

			// 4.
      // functions, procedures
      // ENTITY Algorithm_definition
      // name : express_id; 
      Aggregate a_ad = model2.getEntityExtentInstances(EAlgorithm_definition.class);
      SdaiIterator iter_ad = a_ad.createIterator();
    	while (iter_ad.next()) {
	      EAlgorithm_definition ad_inst = (EAlgorithm_definition) a_ad.getCurrentMemberObject(iter_ad);
  	    String ad_name = ad_inst.getName(null);
    		ad_inst.setName(null, ad_name.toLowerCase());
      }
      
			// 5.
      // global rule
			// ENTITY Global_rule;
			// name : express_id;
      Aggregate a_gr = model2.getEntityExtentInstances(EGlobal_rule.class);
      SdaiIterator iter_gr = a_gr.createIterator();
    	while (iter_gr.next()) {
	      EGlobal_rule gr_inst = (EGlobal_rule) a_gr.getCurrentMemberObject(iter_gr);
  	    String gr_name = gr_inst.getName(null);
    		gr_inst.setName(null, gr_name.toLowerCase());
      }

			// 6.
      // uniqueness rule
			// ENTITY Uniqueness_rule;
			// label : OPTIONAL express_id;
      Aggregate a_ur = model2.getEntityExtentInstances(EUniqueness_rule.class);
      SdaiIterator iter_ur = a_ur.createIterator();
    	while (iter_ur.next()) {
	      EUniqueness_rule ur_inst = (EUniqueness_rule) a_ur.getCurrentMemberObject(iter_ur);
  	    // String ur_name = ur_inst.getLabel(null);
    		if (ur_inst.testLabel(null)) { // it is OPTIONAL
    			ur_inst.setLabel(null, ur_inst.getLabel(null).toLowerCase());
				}
      }

      // 7.
      // constants
      // ENTITY Constant_definition;
			// name : express_id;
      Aggregate a_cd = model2.getEntityExtentInstances(EConstant_definition.class);
      SdaiIterator iter_cd = a_cd.createIterator();
    	while (iter_cd.next()) {
	      EConstant_definition cd_inst = (EConstant_definition) a_cd.getCurrentMemberObject(iter_cd);
  	    String cd_name = cd_inst.getName(null);
    		cd_inst.setName(null, cd_name.toLowerCase());
      }

			// 8.
      // supertype constraint
			// ENTITY Sub_supertype_constraint;
      // name : OPTIONAL express_id;
      Aggregate a_sc = model2.getEntityExtentInstances(ESub_supertype_constraint.class);
      SdaiIterator iter_sc = a_sc.createIterator();
    	while (iter_sc.next()) {
	      ESub_supertype_constraint sc_inst = (ESub_supertype_constraint) a_sc.getCurrentMemberObject(iter_sc);
  	    // String sc_name = sc_inst.getName(null);
    		if (sc_inst.testName(null)) { // it is OPTIONAL
    			sc_inst.setName(null, sc_inst.getName(null).toLowerCase());
      	}
      }

      // 9.
      // enumeration IDs:
			// ENTITY Enumeration_type 
      //	local_elements : OPTIONAL LIST [0:?] OF UNIQUE express_id; 
      Aggregate a_et = model2.getEntityExtentInstances(EEnumeration_type.class);
      SdaiIterator iter_et = a_et.createIterator();
    	while (iter_et.next()) {
	      EEnumeration_type et_inst = (EEnumeration_type) a_et.getCurrentMemberObject(iter_et);
				// further implementation different - go through the whole LIST, set each element
      	if (et_inst.testLocal_elements(null)) { // it is OPTIONAL
      		A_string local_elements = et_inst.getLocal_elements(null);
      		SdaiIterator iter_e = local_elements.createIterator();
      		while (iter_e.next()) {
      			String local_element = (String)local_elements.getCurrentMember(iter_e);
      			local_elements.setCurrentMember(iter_e, local_element.toLowerCase());
      		}
      	}
      }

      // 10.
      // labels of where rules
      // ENTITY Where_rule;
			// label : OPTIONAL express_id;
      Aggregate a_wr = model2.getEntityExtentInstances(EWhere_rule.class);
      SdaiIterator iter_wr = a_wr.createIterator();
    	while (iter_wr.next()) {
	      EWhere_rule wr_inst = (EWhere_rule) a_wr.getCurrentMemberObject(iter_wr);
  	    // String _name = _inst.getName(null);
    		if (wr_inst.testLabel(null)) { // it is OPTIONAL
    			wr_inst.setLabel(null, wr_inst.getLabel(null).toLowerCase());
      	}
      }
      	
      // 11-12.
      // function return type
			// ENTITY Function_definition 
  		// return_type_label : OPTIONAL express_id;
			// return_type_labels : OPTIONAL LIST [1:?] OF express_id;
      Aggregate a_rt = model2.getEntityExtentInstances(EFunction_definition.class);
      SdaiIterator iter_rt = a_rt.createIterator();
    	while (iter_rt.next()) {
	      EFunction_definition rt_inst = (EFunction_definition) a_rt.getCurrentMemberObject(iter_rt);
  	    //String _name = _inst.getName(null);
    		if (rt_inst.testReturn_type_label(null)) { // it is OPTIONAL
    			rt_inst.setReturn_type_label(null, rt_inst.getReturn_type_label(null).toLowerCase());
    		}
      	if (rt_inst.testReturn_type_labels(null)) { // it is OPTIONAL
      		A_string return_type_labels = rt_inst.getReturn_type_labels(null);
      		SdaiIterator iter_e = return_type_labels.createIterator();
      		while (iter_e.next()) {
      			String return_type_label = (String)return_type_labels.getCurrentMember(iter_e);
      			return_type_labels.setCurrentMember(iter_e, return_type_label.toLowerCase());
      		}
      	}
      }
      
      // 13.
      // alias in interfaced declarations
			// ENTITY Interfaced_declaration
			// alias_name : OPTIONAL express_id;
      Aggregate a_id = model2.getEntityExtentInstances(EInterfaced_declaration.class);
      SdaiIterator iter_id = a_id.createIterator();
    	while (iter_id.next()) {
	      EInterfaced_declaration id_inst = (EInterfaced_declaration) a_id.getCurrentMemberObject(iter_id);
  	    // String _name = _inst.getName(null);
    		if (id_inst.testAlias_name(null)) { // it is OPTIONAL
    			id_inst.setAlias_name(null, id_inst.getAlias_name(null).toLowerCase());
      	}
      }
      
      // 14-15
      // parameter
			// ENTITY Parameter;
			// name : express_id;
			// type_labels: OPTIONAL LIST [1:?] OF express_id;
      Aggregate a_p = model2.getEntityExtentInstances(EParameter.class);
      SdaiIterator iter_p = a_p.createIterator();
    	while (iter_p.next()) {
	      EParameter p_inst = (EParameter) a_p.getCurrentMemberObject(iter_p);
  	    String p_name = p_inst.getName(null);
    		p_inst.setName(null, p_name.toLowerCase());
      	if (p_inst.testType_labels(null)) { // it is OPTIONAL
      		A_string type_labels = p_inst.getType_labels(null);
      		SdaiIterator iter_e = type_labels.createIterator();
      		while (iter_e.next()) {
      			String type_label = (String)type_labels.getCurrentMember(iter_e);
      			type_labels.setCurrentMember(iter_e, type_label.toLowerCase());
      		}
      	}
      }

      // alias in reference from specification - is it express-x only? 
      // NOTE - there are other things that are express-x only, not included here, at least for now
			// ENTITY reference_from_specification_as 
			// alias_name : OPTIONAL express_id; 
      

		} // end while
	}

  static void insertBinaries(SdaiRepository repo, X_AllSchemas x_data, boolean to_serialize) throws SdaiException, IOException {
//  static void insertBinaries(SdaiRepository repo) throws SdaiException {
    // go through all models, and, if the model is ReadWrite, renameTo() it into the corresponding java package directory.
    int str_length;
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      String model_name = model2.getName();

      if ((model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) || 
              (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
        ;

        // continue;
      }

			if (skipModelM(model_name)) {
				continue;
			}

/*
      if (model_name.length() > 15) {
        String part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          continue;
        }
      }

      if (model_name.length() > 13) {
        String part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          continue;
        }
      }

      if (model_name.length() > 9) {
        String part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          continue;
        }
      }

      if (model_name.length() > 6) {
        String part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          continue;
        }
      }
*/
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String end_model_name = model_name.substring(model_name.length() - 13);
      String schema_directory;

      if (end_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
        schema_directory = "M" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      } else if (model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) {
        schema_directory = "dictionary";
      } else if (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA")) {
        schema_directory = "mapping";
      } else {
        schema_directory = "S" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      }

      String source_path = repo.getLocation();

      //			String source_path = "REPOSITORIES" + File.separator + "ExpressCompilerRepo";
      String destination_path = "jsdai" + File.separator + schema_directory;

 			if (output_dir != null) {
				destination_path = output_dir + File.separator + destination_path;
			}

//       String model_id = model2.getId();
//       File source = new File(source_path, model_id);

      //			File destination = new File(destination_path, model_id);
      File destination = new File(destination_path, model_name);

//       if (source.canRead()) {
//         printDebug("CAN READ SOURCE: " + source_path + File.separator + model_name);
//       } else {
//         printDebug("CAN NOT READ SOURCE: " + source_path + File.separator + model_name);
//       }

//       if (source.canWrite()) {
//         printDebug("CAN WRITE SOURCE: " + source_path + File.separator + model_name);
//       } else {
//         printDebug("CAN NOT WRITE SOURCE: " + source_path + File.separator + model_name);
//       }

      if (destination.canRead()) {
        printDebug("CAN READ DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT READ DESTINATION: " + destination_path + File.separator + model_name);
      }

      if (destination.canWrite()) {
        printDebug("CAN WRITE DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT WRITE DESTINATION: " + destination_path + File.separator + 
                   model_name);
      }

      copyFile(model2.getLocationURL(), destination);
//System.out.println("<<>><<>>model location: " + model2.getLocationURL());
			// flag_serialize = false;
		
       flag_serialize = false;
    	if (flag_serialize) {
    		if (x_data.children != null) {
      		for (int i = 0; i < x_data.children.length; ++i) {
        		Node child = x_data.children[i];
        		if (child instanceof X_SchemaDecl) {
							if (schema_name.equalsIgnoreCase(((X_SchemaDecl)child).schema_name)) {
								// serialize etc.
				    	  String x_name = schema_name.toUpperCase() + "_EXPRESSION_DATA";
//    						FileOutputStream x_ostream = new FileOutputStream(destination_path + File.separator + x_name);
    						FileOutputStream x_ostream = new FileOutputStream(source_path + File.separator + x_name);
    						ObjectOutputStream p = new ObjectOutputStream(x_ostream);
    						p.writeObject(child);
						    p.flush();
    						x_ostream.close();
							}        	
        		}
      		} // for
    		}
    	}
    }
  }

//  static void insertAllBinaries(SdaiRepository repo, X_AllSchemas x_data, boolean to_serialize) throws SdaiException {
  static void insertAllBinaries_old(SdaiRepository repo) throws SdaiException {
    // go through all models, and, if the model is ReadWrite, renameTo() it into the corresponding java package directory.
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() == SdaiModel.NO_ACCESS) {
        model2.startReadWriteAccess();
      } else if (model2.getMode() == SdaiModel.READ_ONLY) {
        model2.promoteSdaiModelToRW();
      }

      String model_name = model2.getName();

      if ((model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) || 
              (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
        continue;
      }

			if (skipModelM(model_name)) {
				continue;
			}

/*
      if (model_name.length() > 15) {
        String part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          continue;
        }
      }

      if (model_name.length() > 13) {
        String part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          continue;
        }
      }

      if (model_name.length() > 9) {
        String part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          continue;
        }
      }

      if (model_name.length() > 6) {
        String part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          continue;
        }
      }

*/
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String end_model_name = model_name.substring(model_name.length() - 13);
      String schema_directory;

      if (end_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
        schema_directory = "M" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      } else {
        schema_directory = "S" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      }

      String source_path = "REPOSITORIES" + File.separator + "ExpressCompilerRepo";
      String destination_path = "jsdai" + File.separator + schema_directory;
      String model_id = model2.getId();
      File source = new File(source_path, model_id);

      //			File destination = new File(destination_path, model_id);
      File destination = new File(destination_path, model_name);

      if (source.canRead()) {
        printDebug("CAN READ SOURCE: " + source_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT READ SOURCE: " + source_path + File.separator + model_name);
      }

      if (source.canWrite()) {
        printDebug("CAN WRITE SOURCE: " + source_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT WRITE SOURCE: " + source_path + File.separator + model_name);
      }

      if (destination.canRead()) {
        printDebug("CAN READ DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT READ DESTINATION: " + destination_path + File.separator + model_name);
      }

      if (destination.canWrite()) {
        printDebug("CAN WRITE DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT WRITE DESTINATION: " + destination_path + File.separator + 
                   model_name);
      }

      copyFile_old(source, destination);
    }
  }

  static void insertAllBinaries(SdaiRepository repo) throws SdaiException {
    // go through all models, and, if the model is ReadWrite, renameTo() it into the corresponding java package directory.
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() == SdaiModel.NO_ACCESS) {
        model2.startReadWriteAccess();
      } else if (model2.getMode() == SdaiModel.READ_ONLY) {
        model2.promoteSdaiModelToRW();
      }

      String model_name = model2.getName();

      if ((model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) || 
              (model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
        continue;
      }

			if (skipModelM(model_name)) {
				continue;
			}

/*
      if (model_name.length() > 15) {
        String part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          continue;
        }
      }

      if (model_name.length() > 13) {
        String part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          continue;
        }
      }

      if (model_name.length() > 9) {
        String part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          continue;
        }
      }

      if (model_name.length() > 6) {
        String part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          continue;
        }
      }

*/
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String end_model_name = model_name.substring(model_name.length() - 13);
      String schema_directory;

      if (end_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
        schema_directory = "M" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      } else {
        schema_directory = "S" + schema_name.substring(0, 1).toUpperCase() + 
                           schema_name.substring(1).toLowerCase();
      }

      String source_path = "REPOSITORIES" + File.separator + "ExpressCompilerRepo";
      String destination_path = "jsdai" + File.separator + schema_directory;

 			if (output_dir != null) {
				destination_path = output_dir + File.separator + destination_path;
			}
//       String model_id = model2.getId();
//       File source = new File(source_path, model_id);

      //			File destination = new File(destination_path, model_id);
      File destination = new File(destination_path, model_name);

//       if (source.canRead()) {
//         printDebug("CAN READ SOURCE: " + source_path + File.separator + model_name);
//       } else {
//         printDebug("CAN NOT READ SOURCE: " + source_path + File.separator + model_name);
//       }

//       if (source.canWrite()) {
//         printDebug("CAN WRITE SOURCE: " + source_path + File.separator + model_name);
//       } else {
//         printDebug("CAN NOT WRITE SOURCE: " + source_path + File.separator + model_name);
//       }

      if (destination.canRead()) {
        printDebug("CAN READ DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT READ DESTINATION: " + destination_path + File.separator + model_name);
      }

      if (destination.canWrite()) {
        printDebug("CAN WRITE DESTINATION: " + destination_path + File.separator + model_name);
      } else {
        printDebug("CAN NOT WRITE DESTINATION: " + destination_path + File.separator + 
                   model_name);
      }

      copyFile(model2.getLocationURL(), destination);
    }
  }

  static void deleteAllModels(SdaiRepository repo) throws SdaiException {
    for (;;) {
      ASdaiModel models2 = repo.getModels();

      if (models2.getMemberCount() <= 2) {
        break;
      }

      printDebug("DELETE MODELS: the number of models: " + models2.getMemberCount());

      SdaiIterator iter_model = models2.createIterator();

      while (iter_model.next()) {
        SdaiModel model2 = models2.getCurrentMember(iter_model);
        printDebug("DELETING MODEL: " + model2.getName());

        String model_name = model2.getName();

        if ((!model_name.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) && 
                (!model_name.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA"))) {
          model2.deleteSdaiModel();
        }

        //			if (model2.getMode() == SdaiModel.NO_ACCESS) {
        //				model2.startReadWriteAccess();
        //			} else
        //			if (model2.getMode() == SdaiModel.READ_ONLY) {
        //				model2.promoteSdaiModelToRW();
        //			}
      }
    }
  }

  static void deleteAllModels_old2(SdaiRepository repo)
                            throws SdaiException {
    ASdaiModel models2 = repo.getModels();
    printDebug("DELETE MODELS: the number of models: " + models2.getMemberCount());

    int count = models2.getMemberCount();

    for (int i = 1; i <= count; i++) {
      SdaiModel model2 = models2.getByIndex(i);
      printDebug("DELETING MODEL: " + model2.getName());


      //			models2.removeByIndex(i);
      model2.deleteSdaiModel();

      //			if (model2.getMode() == SdaiModel.NO_ACCESS) {
      //				model2.startReadWriteAccess();
      //			} else
      //			if (model2.getMode() == SdaiModel.READ_ONLY) {
      //				model2.promoteSdaiModelToRW();
      //			}
    }
  }

  static void deleteAllModels_old(SdaiRepository repo)
                           throws SdaiException {
    ASdaiModel models2 = repo.getModels();
    printDebug("DELETE MODELS: the number of models: " + models2.getMemberCount());

    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);
      printDebug("DELETING MODEL: " + model2.getName());
      model2.deleteSdaiModel();

      //			if (model2.getMode() == SdaiModel.NO_ACCESS) {
      //				model2.startReadWriteAccess();
      //			} else
      //			if (model2.getMode() == SdaiModel.READ_ONLY) {
      //				model2.promoteSdaiModelToRW();
      //			}
    }
  }

  static void setAllReadWrite(SdaiRepository repo) throws SdaiException {
    ASdaiModel models2 = repo.getModels();
    SdaiIterator iter_model = models2.createIterator();

    while (iter_model.next()) {
      SdaiModel model2 = models2.getCurrentMember(iter_model);

      if (model2.getMode() == SdaiModel.NO_ACCESS) {
        model2.startReadWriteAccess();
      } else if (model2.getMode() == SdaiModel.READ_ONLY) {
        model2.promoteSdaiModelToRW();
      }
    }
  }

  static void deleteInterfacedDeclarations() throws SdaiException {
    SdaiModel sm = null;
    String name_searched = "MIXED_COMPLEX_TYPES_DICTIONARY_DATA";
    ASdaiModel models = repository.getModels();
    SdaiIterator iter_models = models.createIterator();

    while (iter_models.next()) {
      SdaiModel sm1 = models.getCurrentMember(iter_models);
      String model_name = sm1.getName();

      // System.out.println("findModel - searching: " + name_searched + ", current: " + model_name + ", nr of models: " + models.getMemberCount());
      if (model_name.equalsIgnoreCase(name_searched)) {
        sm = sm1;
      }
    }

    // Remove all interfaced_declarations from the model
    if (sm == null) {
      printDebug("MIXED_COMLPEX_TYPES dictionary model not found");

      //			System.out.println("MIXED_COMLPEX_TYPES dictionary model not found");
      return;
    }

    printDebug("DEBUG DELETE INTERFACED DECLARATIONS. MODEL: " + sm.getName());

    for (;;) {
      Aggregate ia = sm.getEntityExtentInstances(EInterfaced_declaration.class);

      if (ia.getMemberCount() <= 0) {
        break;
      }

      SdaiIterator iter_inst = ia.createIterator();

      while (iter_inst.next()) {
        EInterfaced_declaration inst = (EInterfaced_declaration) ia.getCurrentMemberObject(
                                             iter_inst);
        printDebug("DEBUG DELETE INTERFACED DECLARATIONS: " + 
                   ((EEntity_definition) ((EDeclaration) inst).getDefinition(null)).getName(null));

        // probably for incremental, perhaps need to check if it does not interfece with handling of schema instances.
        if (sm.getMode() == SdaiModel.READ_ONLY) {
          sm.promoteSdaiModelToRW();
        }

        inst.deleteApplicationInstance();

        //		iter_inst.remove();
      }
    }

    return;
  }

  static ESchema_definition getSchema_definitionFromModel(SdaiModel sm)
                                                   throws SdaiException {
    if (sm == null) return null;
    Aggregate ia = sm.getEntityExtentInstances(ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      ESchema_definition inst = (ESchema_definition) ia.getCurrentMemberObject(iter_inst);

      return inst;
    }

    return null;
  }

  static EGeneric_schema_definition getGeneric_schema_definitionFromModel(SdaiModel sm)
    throws SdaiException {
// System.out.println("suspicious model: " + sm);

//    Aggregate ia = sm.getEntityExtentInstances(EGeneric_schema_definition.class);
    Aggregate ia = sm.getInstances(EGeneric_schema_definition.class);

    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EGeneric_schema_definition inst = (EGeneric_schema_definition) ia.getCurrentMemberObject(
                                              iter_inst);

      return inst;
    }

    return null;
  }

  static ESchema_definition getSchema_definitionFromModel_does_not_works_for_empty_schemas(SdaiModel sm)
    throws SdaiException {
    Aggregate ia = sm.getEntityExtentInstances(ELocal_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      ELocal_declaration inst = (ELocal_declaration) ia.getCurrentMemberObject(iter_inst);

      //			ESchema_definition sdf = (ESchema_definition)inst.getParent_schema(null);
      // this approach does not work for X, but do we need it for X?
      // pre-X			ESchema_definition sdf = inst.getParent_schema(null);
      // only Express
      
      ESchema_definition sdf = null;
      if (inst.testParent_schema(null)) {
      	sdf = (ESchema_definition) inst.getParent_schema(null);
      }
      return sdf;
    }

    return null;
  }

  static void chainUseFroms() throws SdaiException {
    int i;
    int j;
    Vector current;
    SdaiModel smd;
    SdaiModel target;


    // System.out.println("models: " + model_vector.size() + ", used: " + used_vectors.size() + ", referenced: " + referenced_vectors.size());
    for (i = 0; i < used_vectors.size(); i++) {
      unsetAllMarks();
      current = (Vector) used_vectors.elementAt(i);


      // System.out.println("CHAINED USE FROM - scanning all used schemas: " + getSchema_definitionFromModel((SdaiModel)model_vector.elementAt(i)).getName(null) + ", nr of USE FROMs: " + current.size());
      for (j = 0; j < current.size(); j++) {
        smd = (SdaiModel) current.elementAt(j);


        // System.out.println("CHAINED USE FROM - before the first goDeeper: " + getSchema_definitionFromModel(smd).getName(null));
        goDeeper(smd, smd);
        target = (SdaiModel) model_vector.elementAt(i);
        hm_current_entity_declarations = (HashMap)hm_entity_declarations.get(target);
        hm_current_type_declarations = (HashMap)hm_type_declarations.get(target);
        hm_current_function_declarations = (HashMap)hm_function_declarations.get(target);
        hm_current_procedure_declarations = (HashMap)hm_procedure_declarations.get(target);
        hm_current_constant_declarations = (HashMap)hm_constant_declarations.get(target);
        hm_current_rule_declarations = (HashMap)hm_rule_declarations.get(target);
        hm_current_subtype_constraint_declarations = (HashMap)hm_subtype_constraint_declarations.get(target);
//System.out.println("smd: " + smd + ", target: " + target);
        duplicateDeclarations(smd, target, smd);
      }
    }

	
    for (i = 0; i < referenced_vectors.size(); i++) {
      unsetAllMarks();
      current = (Vector) referenced_vectors.elementAt(i);

      // System.out.println("CHAINED USE FROM - scanning all referenced schemas: " + getSchema_definitionFromModel((SdaiModel)model_vector.elementAt(i)).getName(null) + ", nr of USE FROMs: " + current.size());
      for (j = 0; j < current.size(); j++) {
        smd = (SdaiModel) current.elementAt(j);


        // System.out.println("CHAINED USE FROM - before the first goDeeper: " + getSchema_definitionFromModel(smd).getName(null));
        goDeeper(smd, smd);
        target = (SdaiModel) model_vector.elementAt(i);
        hm_current_entity_declarations = (HashMap)hm_entity_declarations.get(target);
        hm_current_type_declarations = (HashMap)hm_type_declarations.get(target);
        hm_current_function_declarations = (HashMap)hm_function_declarations.get(target);
        hm_current_procedure_declarations = (HashMap)hm_procedure_declarations.get(target);
        hm_current_constant_declarations = (HashMap)hm_constant_declarations.get(target);
        hm_current_rule_declarations = (HashMap)hm_rule_declarations.get(target);
        hm_current_subtype_constraint_declarations = (HashMap)hm_subtype_constraint_declarations.get(target);
        duplicateReferencedDeclarations(smd, target, smd);
      }
    }


  }

  static void goDeeper(SdaiModel smd, SdaiModel start) throws SdaiException {
    if (isMarkSet(smd)) {
      // System.out.println("CHAINED USE FROM - schema rejected - already done: " + getSchema_definitionFromModel(smd).getName(null));
      return;
    }

    setMark(smd);

    int index = model_vector.indexOf(smd);

    //for (int ik = 0; ik < model_vector.size(); ik++) {
    // System.out.println("UUaU model vector element: " + ik + ", name: " + ((SdaiModel)model_vector.elementAt(ik)).getName());
    //}
    //System.out.println("UUaU index: " + index + " of " + smd.getName());	
    //System.out.println("UUaU used vectors: " + used_vectors.size());
    //for (int ik1 = 0; ik1 < used_vectors.size(); ik1++) {
    //		Vector current1 = (Vector)used_vectors.elementAt(ik1);
    //System.out.println("UUaU vector:  " + ik1 + ", elements: " + current1.size());
    //		for (int ik2 = 0; ik2 < current1.size(); ik2++) {
    //      System.out.println("UUaU vector: " + ik1 + ", element: " + ((SdaiModel)current1.elementAt(ik2)).getName());
    //		}
    //}	
    
    
//    System.out.println("XXXOOOXXX index: " + index + " of model: " + smd);

    if (index < 0) {
    	// incremental compilation, this schema has been pre-compiled earlier, used_vector does not exist.
    	// Question: do we need it? If we do, we are out of luck. 
    	// Currently there is no information about interfaced whole schemas in the dictionary.
    	// We should not need it, because chained USE FROMs have been already calculated for pre-compiled schemas at the time they were compiled
  		// BTW, from version 3.5, sdai_dictionary_schema now can include that data.
    	return;
    }
    
    Vector current = (Vector) used_vectors.elementAt(index);

    for (int j = 0; j < current.size(); j++) {
      SdaiModel smd_new = (SdaiModel) current.elementAt(j);


      // System.out.println("CHAINED USE FROM - before the next goDeeper - current: " + getSchema_definitionFromModel(smd).getName(null) + ", new: " + getSchema_definitionFromModel(smd_new).getName(null));
      goDeeper(smd_new, start);


      // System.out.println("CHAINED USE FROM - after goDeeper - previous: " + getSchema_definitionFromModel(smd_new).getName(null) + ", current: " + getSchema_definitionFromModel(smd).getName(null));
      // duplicate used declarations here - take from smd_new and make them in smd
      hm_current_entity_declarations = (HashMap)hm_entity_declarations.get(smd);
      hm_current_type_declarations = (HashMap)hm_type_declarations.get(smd);

      hm_current_function_declarations = (HashMap)hm_function_declarations.get(smd);
      hm_current_procedure_declarations = (HashMap)hm_procedure_declarations.get(smd);
      hm_current_constant_declarations = (HashMap)hm_constant_declarations.get(smd);
      hm_current_rule_declarations = (HashMap)hm_rule_declarations.get(smd);
      hm_current_subtype_constraint_declarations = (HashMap)hm_subtype_constraint_declarations.get(smd);

      duplicateDeclarations(smd_new, smd, start);
    }

    //		setMark(smd);
  }

  static void duplicateDeclarations(SdaiModel source, SdaiModel target, SdaiModel start)
                             throws SdaiException {
    boolean skip_it;
    Aggregate ia;
    Aggregate iat;
    SdaiIterator iter_inst;
    SdaiIterator iter_instt;

		String source_schema_name = source.getName().toLowerCase();
		source_schema_name = source_schema_name.substring(0, source_schema_name.length()-16);
		String target_schema_name = target.getName().toLowerCase();
		target_schema_name = target_schema_name.substring(0, target_schema_name.length()-16);
		String original_source_schema_name = "";
		String original_source_schema_name2 = "";

		
    ia = source.getEntityExtentInstances(EUsed_declaration.class);
    iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EUsed_declaration inst = (EUsed_declaration) ia.getCurrentMemberObject(iter_inst);
      ENamed_type nt = (ENamed_type) inst.getDefinition(null);
			String nt_type_name = null;
	    String nt_name = nt.getName(null).toLowerCase();
			if (nt instanceof EEntity_definition) {
				nt_type_name = "entity";
			} else {
				nt_type_name = "type";
			}


      


        if (nt instanceof EDefined_type) {
				
          // will have to use seprate or combined hashmap for other types, such as functions, procedures, constants
          if (hm_current_entity_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but an entity from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_function_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_function_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a function from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_procedure_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_procedure_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a procedure from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_constant_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_constant_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a constant from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_rule_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_rule_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a global rule from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_subtype_constraint_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_subtype_constraint_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a subtype_constraint from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
					if (!(hm_current_type_declarations.containsKey(nt_name))) {
	          EDeclaration tdc = (EDeclaration) target.createEntityInstance(CType_declaration$used_declaration.class);
  	        tdc.setParent(null, getGeneric_schema_definitionFromModel(target));
    	      tdc.setDefinition(null, nt);
						hm_current_type_declarations.put(nt_name, tdc);
					} else {
						// check if the same defined type, if not - print warning
						// if the same, check if referenced declaration, if so - remove it and create used instead
							EType_declaration tdc2 = (EType_declaration)hm_current_type_declarations.get(nt_name);
							if (tdc2 != null) { // cannot be null anyway
								EDefined_type dt2 = (EDefined_type)tdc2.getDefinition(null);
	               if (dt2 == nt) {
                  	// the same
                  	if (tdc2 instanceof EReferenced_declaration) {
											 // replace it with used declaration
											Object o = hm_current_type_declarations.remove(nt_name);
											if (o != tdc2) {
												// internal error, more than one declaration with the same key
												System.out.println("INTERNAL ERROR #4 in the generator of chained use froms: " + nt_name + ", 1: " + o + ", 2: " + tdc2);
											}
					            tdc2.deleteApplicationInstance();
						          EDeclaration tdc = (EDeclaration) target.createEntityInstance(CType_declaration$used_declaration.class);
  	      					  tdc.setParent(null, getGeneric_schema_definitionFromModel(target));
    	      					tdc.setDefinition(null, nt);
											hm_current_type_declarations.put(nt_name, tdc);
                  	}
                 } else {
				  	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  			  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
				  	        original_source_schema_name2 = dt2.findEntityInstanceSdaiModel().getName().toLowerCase();
	  			  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
			        			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but another defined type from " + original_source_schema_name2 + " already exists with this name", null, false); 
                  }
							} else {
								System.out.println("INTERNAL ERROR #3 in the generator of chained use froms: " + nt_name);
							}  // if not null
					}

        } else if (nt instanceof EEntity_definition) {

          // will have to use seprate or combined hashmap for other types, such as functions, procedures, constants
					if (hm_current_type_declarations.containsKey(nt_name)) {
//RR	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_type_declarations.get(nt_name);
//System.out.println("<entity definition> key: " + nt_name + ", declaration: " + tmpdc);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
  	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
      			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a defined type from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else

          if (hm_current_function_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_function_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a function from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_procedure_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_procedure_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a procedure from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_constant_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_constant_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a constant from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_rule_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_rule_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a global rule from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_subtype_constraint_declarations.containsKey(nt_name)) {
//	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_subtype_constraint_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a subtype_constraint from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else


          if (!(hm_current_entity_declarations.containsKey(nt_name))) {
	          EDeclaration edc = (EDeclaration) target.createEntityInstance(CEntity_declaration$used_declaration.class);
  	        edc.setParent(null, getGeneric_schema_definitionFromModel(target));
    	      edc.setDefinition(null, nt);
          	hm_current_entity_declarations.put(nt_name, edc);
					} else {
						// check if the same entity, if not - print warning
						// if the same, check if referenced declaration, if so - remove it and create used instead
							EEntity_declaration edc2 = (EEntity_declaration)hm_current_entity_declarations.get(nt_name);
							if (edc2 != null) { // cannot be null anyway
									EEntity_definition edf2 = (EEntity_definition)edc2.getDefinition(null);
                  if (edf2 == nt) {
                  	// the same
                  	if (edc2 instanceof EReferenced_declaration) {
											 // replace it with used declaration
											Object o = hm_current_entity_declarations.remove(nt_name);
											if (o != edc2) {
												// internal error, more than one declaration with the same key
												System.out.println("INTERNAL ERROR #1 in the generator of chained use froms: " + nt_name + ", 1: " + o + ", 2: " + edc2);
											}
					            edc2.deleteApplicationInstance();

						          EDeclaration edc = (EDeclaration) target.createEntityInstance(CEntity_declaration$used_declaration.class);
  	      					  edc.setParent(null, getGeneric_schema_definitionFromModel(target));
    	      					edc.setDefinition(null, nt);
					          	hm_current_entity_declarations.put(nt_name, edc);
                  	
                  	}
                  } else {
					  	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
		  			  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
					  	        original_source_schema_name2 = edf2.findEntityInstanceSdaiModel().getName().toLowerCase();
		  			  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
				        			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but another entity from " + original_source_schema_name2 + " already exists with this name", null, false); 
                  }
							} else {
								System.out.println("INTERNAL ERROR #2 in the generator of chained use froms: " + nt_name);
							}  // if not null
					}

        } // if entity_definition
    }
  }

  static void duplicateDeclarations_slow_and_bad(SdaiModel source, SdaiModel target, SdaiModel start)
                             throws SdaiException {
    boolean skip_it;
    Aggregate ia;
    Aggregate iat;
    SdaiIterator iter_inst;
    SdaiIterator iter_instt;

		String source_schema_name = source.getName().toLowerCase();
		source_schema_name = source_schema_name.substring(0, source_schema_name.length()-16);
		String target_schema_name = target.getName().toLowerCase();
		target_schema_name = target_schema_name.substring(0, target_schema_name.length()-16);
		String original_source_schema_name = "";


		// does not help
//		if (source == target) return;
//		if (target == start) return;
		
		
    ia = source.getEntityExtentInstances(EUsed_declaration.class);
    iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EUsed_declaration inst = (EUsed_declaration) ia.getCurrentMemberObject(iter_inst);
      ENamed_type nt = (ENamed_type) inst.getDefinition(null);
			String nt_type_name = null;
			if (nt instanceof EEntity_definition) {
				nt_type_name = "entity";
			} else {
				nt_type_name = "type";
			}

			// check for local_declaration

      iat = target.getEntityExtentInstances(ELocal_declaration.class);
      iter_instt = iat.createIterator();
      skip_it = false;

      // System.out.println("Candidate for duplication: " + nt.getName(null));
      while (iter_instt.next()) {
        ELocal_declaration instt = (ELocal_declaration) iat.getCurrentMemberObject(iter_instt);
        EEntity ntt = (EEntity) instt.getDefinition(null);
				String ntt_name = null;
	      String ntt_type_name = "";
        // System.out.println("Current already in target: " + ntt.getName(null));
        if (nt == ntt) { // skip this one

          // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
          skip_it = true;

          // break;
        } else {
        	// let's see if there are name conflicts
        	ntt_name = null;
        	if (ntt instanceof ENamed_type) {
        		ntt_name = ((ENamed_type)ntt).getName(null);
        		if (ntt instanceof EEntity_definition) {
        			ntt_type_name = "entity";
        		} else {
        			ntt_type_name = "type";
        		}
        	} else
        	if (ntt instanceof EAlgorithm_definition) {
        		ntt_name = ((EAlgorithm_definition)ntt).getName(null);
        		if (ntt instanceof EFunction_definition) {
        			ntt_type_name = "function";
        		} else {
        			ntt_type_name = "procedure";
        		}
        	} else
        	if (ntt instanceof EConstant_definition) {
        		ntt_name = ((EConstant_definition)ntt).getName(null);
        		ntt_type_name = "constant";
        	} else
        	if (ntt instanceof EGlobal_rule) {
        		ntt_name = ((EGlobal_rule)ntt).getName(null);
       			ntt_type_name = "global rule";
        	} else
        	if (ntt instanceof ESub_supertype_constraint) {
        		if (((ESub_supertype_constraint)ntt).testName(null)) {
	        		ntt_name = ((ESub_supertype_constraint)ntt).getName(null);
        			ntt_type_name = "subtype_constraint";
        		}
        	}
        	if (ntt_name != null) {
        		if (ntt_name.equalsIgnoreCase(nt.getName(null))) {
        			// a name conflict
        			skip_it = true;
		          original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
		          original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
        			printWarningMsg("" + ntt_name + " - attempting to interface " + nt_type_name + " from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a local " + ntt_type_name + " already exists with this name", null, false); 
        		}
        	}	
        }
      } // while - loop through local declarations

      if (skip_it) continue;

//			if (!skip_it) {

	      // check if a used_declaration of nt already exists - if so, do nothing. And also referenced declaration - if so, change to used declaration
  	    iat = target.getEntityExtentInstances(EUsed_declaration.class);
    	  iter_instt = iat.createIterator();
      	skip_it = false;

	      // System.out.println("Candidate for duplication: " + nt.getName(null));
  	    while (iter_instt.next()) {
    	    EUsed_declaration instt = (EUsed_declaration) iat.getCurrentMemberObject(iter_instt);
      	  ENamed_type ntt = (ENamed_type) instt.getDefinition(null);
					String ntt_name = null;
		      String ntt_type_name = "";
	
  	      // System.out.println("Current already in target: " + ntt.getName(null));
    	    if (nt == ntt) { // skip this one

      	    // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
        	  skip_it = true;
	
  	    //    break;
        	} else {
        	// let's see if there are name conflicts
	        	ntt_name = null;
  	      	if (ntt instanceof ENamed_type) {
    	    		ntt_name = ((ENamed_type)ntt).getName(null);
      	  		if (ntt instanceof EEntity_definition) {
        				ntt_type_name = "entity";
        			} else {
	        			ntt_type_name = "type";
  	      		}
    	    	} else
      	  	if (ntt instanceof EAlgorithm_definition) {
	        		ntt_name = ((EAlgorithm_definition)ntt).getName(null);
  	      		if (ntt instanceof EFunction_definition) {
    	    			ntt_type_name = "function";
      	  		} else {
        				ntt_type_name = "procedure";
        			}
	        	} else
  	      	if (ntt instanceof EConstant_definition) {
    	    		ntt_name = ((EConstant_definition)ntt).getName(null);
      	  		ntt_type_name = "constant";
        		} else
	        	if (ntt instanceof EGlobal_rule) {
  	      		ntt_name = ((EGlobal_rule)ntt).getName(null);
    	   			ntt_type_name = "global rule";
      	  	} else
        		if (ntt instanceof ESub_supertype_constraint) {
	        		if (((ESub_supertype_constraint)ntt).testName(null)) {
		        		ntt_name = ((ESub_supertype_constraint)ntt).getName(null);
    	    			ntt_type_name = "subtype_constraint";
      	  		}
        		}
	        	if (ntt_name != null) {
  	      		if (ntt_name.equalsIgnoreCase(nt.getName(null))) {
    	    			// a name conflict
      	  			skip_it = true;
		    	      original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
			          original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
  	      			printWarningMsg("" + ntt_name + " - attempting to interface " + nt_type_name + " from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a used " + ntt_type_name + " already exists with this name", null, false); 
    	    		}
      	  	}	
					}

      	} // while through used
			
//			} // if not skip because of local conflict
      
      if (skip_it) continue;
      
//      if (!skip_it) { // if not skipping because

        // checking, if perhaps  a referenced_declaration exists, it should be replaced by used_declaration.
        iat = target.getEntityExtentInstances(EReferenced_declaration.class);
        iter_instt = iat.createIterator();

        // System.out.println("Candidate for duplication: " + nt.getName(null));
        skip_it = false;
        while (iter_instt.next()) {
          EReferenced_declaration instt = (EReferenced_declaration) iat.getCurrentMemberObject(
                                                iter_instt);

          //					ENamed_type ntt = (ENamed_type)instt.getDefinition(null);									
          EEntity ntt = (EEntity) instt.getDefinition(null);
					String ntt_name = null;
		      String ntt_type_name = "";

          // System.out.println("Current already in target: " + ntt.getName(null));
          if (nt == ntt) { // skip this one
							
				    if (nt instanceof EDefined_type) {
							if (hm_current_type_declarations.containsValue(instt)) {
								Object o = hm_current_type_declarations.remove(nt.getName(null).toLowerCase());
								if (o != instt) {
									// internal error, more than one declaration
									hm_current_type_declarations.put(nt.getName(null).toLowerCase(),o);
//					System.out.println("In duplicateDeclarations - more than one type declaration in hash map found: " + nt.getName(null).toLowerCase());
									printDebug("In duplicateDeclarations - more than one type declaration in hash map found");
								}
							} else {
//					System.out.println("no type declaration found in HashMap: " + nt.getName(null).toLowerCase());
							}
						} else 
						if (nt instanceof EEntity_definition) {
							if (hm_current_entity_declarations.containsValue(instt)) {
// System.out.println("__XX__ 02 removing from hm_current_entity_declarations: " +  nt.getName(null).toLowerCase());
								Object o = hm_current_entity_declarations.remove(nt.getName(null).toLowerCase());
								if (o != instt) {
									// internal error, more than one declaration
									hm_current_entity_declarations.put(nt.getName(null).toLowerCase(),o);
//					System.out.println("In duplicateDeclarations - more than one entity declaration in hash map found: " + nt.getName(null).toLowerCase());
									printDebug("In duplicateDeclarations - more than one entity declaration in hash map found");
								}
							} else {
//					System.out.println("no entity declaration found in HashMap: " + nt.getName(null).toLowerCase());
							} 
						} else {
//					System.out.println("unknown type of named type: " + nt);
						}
            
            // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
            instt.deleteApplicationInstance();

            //System.out.println("CHAINED REMOVED: " + ntt.getName(null));
            // break; - maybe there are more than one referenced declaration, let's correct it by removing all of them.
          } else {
          	// check for name conflicts, perhaps a referenced declaration exists for something different with the same name
	        	ntt_name = null;
  	      	if (ntt instanceof ENamed_type) {
    	    		ntt_name = ((ENamed_type)ntt).getName(null);
      	  		if (ntt instanceof EEntity_definition) {
        				ntt_type_name = "entity";
        			} else {
        				ntt_type_name = "type";
	        		}
  	      	} else
    	    	if (ntt instanceof EAlgorithm_definition) {
      	  		ntt_name = ((EAlgorithm_definition)ntt).getName(null);
        			if (ntt instanceof EFunction_definition) {
        				ntt_type_name = "function";
        			} else {
	        			ntt_type_name = "procedure";
  	      		}
    	    	} else
      	  	if (ntt instanceof EConstant_definition) {
        			ntt_name = ((EConstant_definition)ntt).getName(null);
        			ntt_type_name = "constant";
	        	} else
  	      	if (ntt instanceof EGlobal_rule) {
    	    		ntt_name = ((EGlobal_rule)ntt).getName(null);
      	 			ntt_type_name = "global rule";
        		} else
        		if (ntt instanceof ESub_supertype_constraint) {
	        		if (((ESub_supertype_constraint)ntt).testName(null)) {
		        		ntt_name = ((ESub_supertype_constraint)ntt).getName(null);
    	    			ntt_type_name = "subtype_constraint";
      	  		}
        		}
	        	if (ntt_name != null) {
  	      		if (ntt_name.equalsIgnoreCase(nt.getName(null))) {
    	    			// a name conflict
      	  			skip_it = true;
		    	      original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
			          original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
  	      			printWarningMsg("" + ntt_name + " - attempting to interface " + nt_type_name + " from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs but a referenced " + ntt_type_name + " already exists with this name", null, false); 
    	    		}
	        	}	
          } // checking if the same name already used
        } // while  - a loop through all referenced declarations


		 if (skip_it) continue;

//      if (!skip_it) { // create an instance of used declaration

		

        if (nt instanceof EDefined_type) {
          //					EType_declaration$used_declaration tdc = (EType_declaration$used_declaration)target.createEntityInstance(CType_declaration$used_declaration.class);
          EDeclaration tdc = (EDeclaration) target.createEntityInstance(
                                   CType_declaration$used_declaration.class);


          // pre-X				tdc.setParent_schema(null, getSchema_definitionFromModel(target));
//          tdc.setParent_schema(null, getGeneric_schema_definitionFromModel(target));
          tdc.setParent(null, getGeneric_schema_definitionFromModel(target));
          // parent_schema now is derived instead of explicit
          // if (tdc.testParent(null)) {
          	// if (tdc.getParent(null) instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
          		// tdc.setParent_schema(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)tdc.getParent(null));
          	// }
          // }
          tdc.setDefinition(null, nt);
					if (!(hm_current_type_declarations.containsKey(nt.getName(null).toLowerCase()))) {
						hm_current_type_declarations.put(nt.getName(null).toLowerCase(), tdc);
					}
          // System.out.println("CHAINED USE FROM - defined type: " + nt.getName(null) + ", from schema: " + getSchema_definitionFromModel(source).getName(null) + ", to schema: " + getSchema_definitionFromModel(target).getName(null));
        } else if (nt instanceof EEntity_definition) {
          //					EEntity_declaration$used_declaration edc = (EEntity_declaration$used_declaration)target.createEntityInstance(CEntity_declaration$used_declaration.class);
          EDeclaration edc = (EDeclaration) target.createEntityInstance(
                                   CEntity_declaration$used_declaration.class);


          // pre-X				edc.setParent_schema(null, getSchema_definitionFromModel(target));
          edc.setParent(null, getGeneric_schema_definitionFromModel(target));
          // parent_schema now is derived instead of explicit
          // if (edc.testParent(null)) {
          	// if (edc.getParent(null) instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
          		// edc.setParent_schema(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)edc.getParent(null));
          	// }
          // }
          edc.setDefinition(null, nt);
          if (!(hm_current_entity_declarations.containsKey(nt.getName(null).toLowerCase()))) {
          	hm_current_entity_declarations.put(nt.getName(null).toLowerCase(), edc);
					}

          // System.out.println("CHAINED USE FROM - entity definition: " + nt.getName(null) + ", from schema: " + getSchema_definitionFromModel(source).getName(null) + ", to schema: " + getSchema_definitionFromModel(target).getName(null));
        } // if entity_definition
      
//    	} // if not skip for conflict with referenced
      
//      } // if not skip for conflict with used
    }
  }



  static void duplicateReferencedDeclarations(SdaiModel source, SdaiModel target, SdaiModel start)
                                       throws SdaiException {


		String source_schema_name = source.getName().toLowerCase();
		source_schema_name = source_schema_name.substring(0, source_schema_name.length()-16);
		String target_schema_name = target.getName().toLowerCase();
		target_schema_name = target_schema_name.substring(0, target_schema_name.length()-16);
		String original_source_schema_name = "";
		String original_source_schema_name2 = "";

    boolean skip_it;
    Aggregate ia;
    Aggregate iat;
    SdaiIterator iter_inst;
    SdaiIterator iter_instt;
    ia = source.getEntityExtentInstances(EUsed_declaration.class);
    iter_inst = ia.createIterator();


    while (iter_inst.next()) {
      EUsed_declaration inst = (EUsed_declaration) ia.getCurrentMemberObject(iter_inst);
      ENamed_type nt = (ENamed_type) inst.getDefinition(null);
			String nt_type_name = null;
			String nt_name = nt.getName(null).toLowerCase();
			if (nt instanceof EEntity_definition) {
				nt_type_name = "entity";
			} else {
				nt_type_name = "type";
			}


      if (nt instanceof EDefined_type) {

          // will have to use seprate or combined hashmap for other types, such as functions, procedures, constants
          if (hm_current_entity_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but an entity from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else

          if (hm_current_function_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a function from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_procedure_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a procedure from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_constant_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a constant from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_rule_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a global rule from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_subtype_constraint_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a subtype_constraint from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else


					if (!(hm_current_type_declarations.containsKey(nt_name))) {
            EDeclaration tdc = (EDeclaration) target.createEntityInstance(CReferenced_declaration$type_declaration.class);
            tdc.setParent(null, getGeneric_schema_definitionFromModel(target));
            tdc.setDefinition(null, nt);
          	hm_current_type_declarations.put(nt.getName(null).toLowerCase(), tdc);
					} else {
						// check if the same defined type, if not - print warning
							EType_declaration tdc2 = (EType_declaration)hm_current_type_declarations.get(nt_name);
							if (tdc2 != null) { // cannot be null anyway
								EDefined_type dt2 = (EDefined_type)tdc2.getDefinition(null);
	              if (dt2 == nt) {
                  	// the same
								} else {
				  	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  			  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
				  	        original_source_schema_name2 = dt2.findEntityInstanceSdaiModel().getName().toLowerCase();
	  			  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
			        			printWarningMsg("" + nt_name + " - attempting to interface a defined type from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but another defined type from " + original_source_schema_name2 + " already exists with this name", null, false); 
								}
						 } else {
								System.out.println("INTERNAL ERROR #5 in the generator of chained use froms: " + nt_name);
						 }
					}
	
      } else if (nt instanceof EEntity_definition) {

          // will have to use seprate or combined hashmap for other types, such as functions, procedures, constants
					if (hm_current_type_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
  	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
      			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a defined type from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else

          if (hm_current_function_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a function from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_procedure_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a procedure from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_constant_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a constant from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_rule_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a global rule from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else
          if (hm_current_subtype_constraint_declarations.containsKey(nt_name)) {
	 	   			EDeclaration tmpdc = (EDeclaration)hm_current_entity_declarations.get(nt_name);
	 	   			EEntity  tmpinst = (EEntity)tmpdc.getDefinition(null);
  	        original_source_schema_name2 = tmpinst.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
	 	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
	  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
	     			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a subtype_constraint from " + original_source_schema_name2 + " already exists with this name", null, false); 
					} else


          if (!(hm_current_entity_declarations.containsKey(nt_name))) {
            EDeclaration edc = (EDeclaration) target.createEntityInstance(CEntity_declaration$referenced_declaration.class);
            edc.setParent(null, getGeneric_schema_definitionFromModel(target));
            edc.setDefinition(null, nt);
          	hm_current_entity_declarations.put(nt.getName(null).toLowerCase(), edc);
					} else {
						// check if the same entity, if not - print warning
						EEntity_declaration edc2 = (EEntity_declaration)hm_current_entity_declarations.get(nt_name);
						if (edc2 != null) { // cannot be null anyway
							EEntity_definition edf2 = (EEntity_definition)edc2.getDefinition(null);
              if (edf2 == nt) {
  	           	// the same
		          } else {
			  	        original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
  			  	      original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
			  	        original_source_schema_name2 = edf2.findEntityInstanceSdaiModel().getName().toLowerCase();
  			  	      original_source_schema_name2 = original_source_schema_name2.substring(0, original_source_schema_name2.length()-16);
		        			printWarningMsg("" + nt_name + " - attempting to interface an entity from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but another entity from " + original_source_schema_name2 + " already exists with this name", null, false); 
		          }			
						} else {
							System.out.println("INTERNAL ERROR #6 in the generator of chained use froms: " + nt_name);
						}
					}

       } // entity

    } // while
  }



  static void duplicateReferencedDeclarations_slow_bad(SdaiModel source, SdaiModel target, SdaiModel start)
                                       throws SdaiException {


		String source_schema_name = source.getName().toLowerCase();
		source_schema_name = source_schema_name.substring(0, source_schema_name.length()-16);
		String target_schema_name = target.getName().toLowerCase();
		target_schema_name = target_schema_name.substring(0, target_schema_name.length()-16);
		String original_source_schema_name = "";

    boolean skip_it;
    Aggregate ia;
    Aggregate iat;
    SdaiIterator iter_inst;
    SdaiIterator iter_instt;
    ia = source.getEntityExtentInstances(EUsed_declaration.class);
    iter_inst = ia.createIterator();

//		if (target == start) return;

    while (iter_inst.next()) {
      EUsed_declaration inst = (EUsed_declaration) ia.getCurrentMemberObject(iter_inst);
      ENamed_type nt = (ENamed_type) inst.getDefinition(null);
			String nt_type_name = null;
			if (nt instanceof EEntity_definition) {
				nt_type_name = "entity";
			} else {
				nt_type_name = "type";
			}

// check for local
      iat = target.getEntityExtentInstances(ELocal_declaration.class);
      iter_instt = iat.createIterator();
      skip_it = false;

      // System.out.println("Candidate for duplication: " + nt.getName(null));
      while (iter_instt.next()) {
        ELocal_declaration instt = (ELocal_declaration) iat.getCurrentMemberObject(iter_instt);
        EEntity ntt = (EEntity) instt.getDefinition(null);
				String ntt_name = null;
	      String ntt_type_name = "";

        // System.out.println("Current already in target: " + ntt.getName(null));
        if (nt == ntt) { // skip this one

          // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
          skip_it = true;

          // break;
        } else {

        	// let's see if there are name conflicts
        	ntt_name = null;
        	if (ntt instanceof ENamed_type) {
        		ntt_name = ((ENamed_type)ntt).getName(null);
        		if (ntt instanceof EEntity_definition) {
        			ntt_type_name = "entity";
        		} else {
        			ntt_type_name = "type";
        		}
        	} else
        	if (ntt instanceof EAlgorithm_definition) {
        		ntt_name = ((EAlgorithm_definition)ntt).getName(null);
        		if (ntt instanceof EFunction_definition) {
        			ntt_type_name = "function";
        		} else {
        			ntt_type_name = "procedure";
        		}
        	} else
        	if (ntt instanceof EConstant_definition) {
        		ntt_name = ((EConstant_definition)ntt).getName(null);
        		ntt_type_name = "constant";
        	} else
        	if (ntt instanceof EGlobal_rule) {
        		ntt_name = ((EGlobal_rule)ntt).getName(null);
       			ntt_type_name = "global rule";
        	} else
        	if (ntt instanceof ESub_supertype_constraint) {
        		if (((ESub_supertype_constraint)ntt).testName(null)) {
	        		ntt_name = ((ESub_supertype_constraint)ntt).getName(null);
        			ntt_type_name = "subtype_constraint";
        		}
        	}
        	if (ntt_name != null) {
        		if (ntt_name.equalsIgnoreCase(nt.getName(null))) {
        			// a name conflict
        			skip_it = true;
		          original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
		          original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
        			printWarningMsg("" + ntt_name + " - attempting to interface " + nt_type_name + " from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a local " + ntt_type_name + " already exists with this name", null, false); 
        		}
        	}	

        }
      }

			if (skip_it) continue;

//			if (!skip_it) {
	      // check if a used_declaration of nt already exists - if so, do nothing. And also referenced declaration - if so, change to used declaration
  	    iat = target.getEntityExtentInstances(EUsed_declaration.class);
    	  iter_instt = iat.createIterator();
      	// skip_it = false;

	      // System.out.println("Candidate for duplication: " + nt.getName(null));
  	    while (iter_instt.next()) {
    	    EUsed_declaration instt = (EUsed_declaration) iat.getCurrentMemberObject(iter_instt);
      	  ENamed_type ntt = (ENamed_type) instt.getDefinition(null);
					String ntt_name = null;
		      String ntt_type_name = "";

	        // System.out.println("Current already in target: " + ntt.getName(null));
  	      if (nt == ntt) { // skip this one
	
  	        // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
    	      skip_it = true;

      	    // break;
        	} else {

        	// let's see if there are name conflicts
	        	ntt_name = null;
  	      	if (ntt instanceof ENamed_type) {
    	    		ntt_name = ((ENamed_type)ntt).getName(null);
      	  		if (ntt instanceof EEntity_definition) {
        				ntt_type_name = "entity";
        			} else {
	        			ntt_type_name = "type";
  	      		}
    	    	} else
      	  	if (ntt instanceof EAlgorithm_definition) {
	        		ntt_name = ((EAlgorithm_definition)ntt).getName(null);
  	      		if (ntt instanceof EFunction_definition) {
    	    			ntt_type_name = "function";
      	  		} else {
        				ntt_type_name = "procedure";
        			}
	        	} else
  	      	if (ntt instanceof EConstant_definition) {
    	    		ntt_name = ((EConstant_definition)ntt).getName(null);
      	  		ntt_type_name = "constant";
        		} else
	        	if (ntt instanceof EGlobal_rule) {
  	      		ntt_name = ((EGlobal_rule)ntt).getName(null);
    	   			ntt_type_name = "global rule";
      	  	} else
        		if (ntt instanceof ESub_supertype_constraint) {
	        		if (((ESub_supertype_constraint)ntt).testName(null)) {
		        		ntt_name = ((ESub_supertype_constraint)ntt).getName(null);
    	    			ntt_type_name = "subtype_constraint";
      	  		}
        		}
	        	if (ntt_name != null) {
  	      		if (ntt_name.equalsIgnoreCase(nt.getName(null))) {
    	    			// a name conflict
      	  			skip_it = true;
		    	      original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
			          original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
  	      			printWarningMsg("" + ntt_name + " - attempting to interface " + nt_type_name + " from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a used " + ntt_type_name + " already exists with this name", null, false); 
    	    		}
      	  	}	

        	}
	      }

//			} // if not skip

		  if (skip_it) continue;

//      if (!skip_it) {
        iat = target.getEntityExtentInstances(EReferenced_declaration.class);
        iter_instt = iat.createIterator();
        skip_it = false;

        while (iter_instt.next()) {
          EReferenced_declaration instt = (EReferenced_declaration) iat.getCurrentMemberObject(
                                                iter_instt);

          //					ENamed_type ntt = (ENamed_type)instt.getDefinition(null);									
          EEntity ntt = (EEntity) instt.getDefinition(null);
					String ntt_name = null;
		      String ntt_type_name = "";

          // System.out.println("Current already in target: " + ntt.getName(null));
          if (nt == ntt) { // skip this one

            // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
            skip_it = true;

  //          break;
          } else {

          	// check for name conflicts, perhaps a referenced declaration exists for something different with the same name
	        	ntt_name = null;
  	      	if (ntt instanceof ENamed_type) {
    	    		ntt_name = ((ENamed_type)ntt).getName(null);
      	  		if (ntt instanceof EEntity_definition) {
        				ntt_type_name = "entity";
        			} else {
        				ntt_type_name = "type";
	        		}
  	      	} else
    	    	if (ntt instanceof EAlgorithm_definition) {
      	  		ntt_name = ((EAlgorithm_definition)ntt).getName(null);
        			if (ntt instanceof EFunction_definition) {
        				ntt_type_name = "function";
        			} else {
	        			ntt_type_name = "procedure";
  	      		}
    	    	} else
      	  	if (ntt instanceof EConstant_definition) {
        			ntt_name = ((EConstant_definition)ntt).getName(null);
        			ntt_type_name = "constant";
	        	} else
  	      	if (ntt instanceof EGlobal_rule) {
    	    		ntt_name = ((EGlobal_rule)ntt).getName(null);
      	 			ntt_type_name = "global rule";
        		} else
        		if (ntt instanceof ESub_supertype_constraint) {
	        		if (((ESub_supertype_constraint)ntt).testName(null)) {
		        		ntt_name = ((ESub_supertype_constraint)ntt).getName(null);
    	    			ntt_type_name = "subtype_constraint";
      	  		}
        		}
	        	if (ntt_name != null) {
  	      		if (ntt_name.equalsIgnoreCase(nt.getName(null))) {
    	    			// a name conflict
      	  			skip_it = true;
		    	      original_source_schema_name = nt.findEntityInstanceSdaiModel().getName().toLowerCase();
			          original_source_schema_name = original_source_schema_name.substring(0, original_source_schema_name.length()-16);
  	      			printWarningMsg("" + ntt_name + " - attempting to interface " + nt_type_name + " from " + original_source_schema_name + " through " + source_schema_name + " into " + target_schema_name + " via chained USE FROMs + REFERENCE FROM but a referenced " + ntt_type_name + " already exists with this name", null, false); 
    	    		}
	        	}	

          }
        }

				if (skip_it) continue;
//        if (!skip_it) { // create an instance of used declaration

          if (nt instanceof EDefined_type) {
            //						EReferenced_declaration$type_declaration tdc = (EReferenced_declaration$type_declaration)target.createEntityInstance(CReferenced_declaration$type_declaration.class);
            EDeclaration tdc = (EDeclaration) target.createEntityInstance(
                                     CReferenced_declaration$type_declaration.class);


            // pre-X						tdc.setParent_schema(null, getSchema_definitionFromModel(target));
            tdc.setParent(null, getGeneric_schema_definitionFromModel(target));
	          // parent_schema is now derived instead of explicit
	          // if (tdc.testParent(null)) {
  	        	// if (tdc.getParent(null) instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
    	      		// tdc.setParent_schema(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)tdc.getParent(null));
      	    	// }
        	  // }
            tdc.setDefinition(null, nt);
	          if (!(hm_current_type_declarations.containsKey(nt.getName(null).toLowerCase()))) {
	          	hm_current_type_declarations.put(nt.getName(null).toLowerCase(), tdc);
						}	
	
            // System.out.println("CHAINED REFERENCE FROM - defined type: " + nt.getName(null) + ", from schema: " + getSchema_definitionFromModel(source).getName(null) + ", to schema: " + getSchema_definitionFromModel(target).getName(null));
          } else if (nt instanceof EEntity_definition) {
            //						EEntity_declaration$referenced_declaration edc = (EEntity_declaration$referenced_declaration)target.createEntityInstance(CEntity_declaration$referenced_declaration.class);
            EDeclaration edc = (EDeclaration) target.createEntityInstance(
                                     CEntity_declaration$referenced_declaration.class);


            // pre-X						edc.setParent_schema(null, getSchema_definitionFromModel(target));
            edc.setParent(null, getGeneric_schema_definitionFromModel(target));
	          // parent_schema is now derived instead of explicit
	          // if (edc.testParent(null)) {
  	        	// if (edc.getParent(null) instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
    	      		// edc.setParent_schema(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)edc.getParent(null));
      	    	// }
        	  // }
            edc.setDefinition(null, nt);
	          if (!(hm_current_entity_declarations.containsKey(nt.getName(null).toLowerCase()))) {
	          	hm_current_entity_declarations.put(nt.getName(null).toLowerCase(), edc);
						}

            // System.out.println("CHAINED REFERENCE FROM - entity definition: " + nt.getName(null) + ", from schema: " + getSchema_definitionFromModel(source).getName(null) + ", to schema: " + getSchema_definitionFromModel(target).getName(null));
          }
//        } // if not skip it
//      } // if not skip it
    }
  }


  static void unsetAllMarks() throws SdaiException {

    for (int i = 0; i < model_vector.size(); i++) {
      SdaiModel mm = (SdaiModel) model_vector.elementAt(i);
      EGeneric_schema_definition schdef = getGeneric_schema_definitionFromModel(mm);
      schdef.setTemp(null);
    }
  }

  static void unsetAllMarks_pre_X() throws SdaiException {
    for (int i = 0; i < model_vector.size(); i++) {
      SdaiModel mm = (SdaiModel) model_vector.elementAt(i);
      ESchema_definition schdef = getSchema_definitionFromModel(mm);
      schdef.setTemp(null);
    }
  }

  static void setMark(SdaiModel smd) throws SdaiException {
    EGeneric_schema_definition schdef = getGeneric_schema_definitionFromModel(smd);
    schdef.setTemp(FLAG_USED);
  }

  static void setMark_pre_X(SdaiModel smd) throws SdaiException {
    ESchema_definition schdef = getSchema_definitionFromModel(smd);
    schdef.setTemp(FLAG_USED);
  }

  static boolean isMarkSet(SdaiModel smd) throws SdaiException {
    EGeneric_schema_definition schdef = getGeneric_schema_definitionFromModel(smd);
    Object mark_obj = schdef.getTemp();

    if (mark_obj != null) {
      return true;
    } else {
      return false;
    }
  }

  static boolean isMarkSet_pre_X(SdaiModel smd) throws SdaiException {
    ESchema_definition schdef = getSchema_definitionFromModel(smd);
    Object mark_obj = schdef.getTemp();

    if (mark_obj != null) {
      return true;
    } else {
      return false;
    }
  }

  static void generateImplicitDeclarations(X_AllSchemas x_tree0, Compiler2 parser0) throws SdaiException {
  	x_tree = x_tree0;
  	x_parser = parser0;
    ASdaiModel models = repository.getModels();
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel sml = models.getCurrentMember(iter_model);

      // models which where already present before are opened in read-only mode
      // no implicit items where generated for them
      String model_name = sml.getName();

//      String part_model_name = model_name.substring(0, 13);

//      if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
//        continue;
//      }

			if (skipModel(model_name)) {
				continue;
			}
      

      if (sml.getMode() == SdaiModel.READ_WRITE) {
//        System.out.println("## SETTING IMPLICIT - schema: " + getSchema_definitionFromModel(sml).getName(null) + " - Claring all marks ");
        // I assume here that all direct references in current model to other models must be explicit, only chained references in other models may be implicit.
        clearAllMarks(); // clear all marks in all models


//        System.out.println("############### SETTING IMPLICIT - schema: " + getSchema_definitionFromModel(sml).getName(null) + " - Setting explicit marks ");
        setExplicitMarks(sml); // set explicit marks from model sml to all other models


//        System.out.println("############### SETTING IMPLICIT - schema: " + getSchema_definitionFromModel(sml).getName(null) + " - Setting keep out marks ");
        setKeepOutMarks(sml); // set marks on all named types in this model to prevent the wave spreading from other models back to this model


//        System.out.println("############### SETTING IMPLICIT - schema: " + getSchema_definitionFromModel(sml).getName(null) + " - Setting implicit marks ");
        setImplicitMarks(sml); // wave - set implicit marks in all other models, except sml


//        System.out.println("############### SETTING IMPLICIT - schema: " + getSchema_definitionFromModel(sml).getName(null) + " - Generating implicit declarations ");
        generateImplicitModelDeclarations(sml); // generate implicit declarations in model sml from all other models
      }
    }
  }

  static void clearAllMarks() throws SdaiException {
    ASdaiModel models = repository.getModels();
    SdaiIterator iter_models = models.createIterator();

    while (iter_models.next()) {
      SdaiModel sml = models.getCurrentMember(iter_models);
      String model_name = sml.getName();

//      String part_model_name = model_name.substring(0, 13);

//      if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
//        continue;
//      }

			if (skipModel(model_name)) {
				continue;
			}      
      

      if (sml.getMode() == SdaiModel.NO_ACCESS) {
        sml.startReadOnlyAccess();
      }

      clearModelMarks(sml);
    }
  }

  static void clearModelMarks(SdaiModel sm) throws SdaiException {
    Aggregate ia = sm.getInstances();
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EEntity inst = (EEntity) ia.getCurrentMemberObject(iter_inst);
      inst.setTemp(null); // unset mark here
    }
  }

  static void setExplicitMarks(SdaiModel sm) throws SdaiException {
    Aggregate ia;
    SdaiIterator iter_inst;
    ia = sm.getEntityExtentInstances(EUsed_declaration.class);
    iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EUsed_declaration inst = (EUsed_declaration) ia.getCurrentMemberObject(iter_inst);

      // only named types can be USED FROM
      //			ENamed_type nt = inst.getDefinition(null, (ENamed_type)null);									
      ENamed_type nt = (ENamed_type) inst.getDefinition(null);


      // System.out.println("## Setting USED mark: " + nt.getName(null));
      nt.setTemp(FLAG_USED);
    }

    ia = sm.getEntityExtentInstances(EReferenced_declaration.class);
    iter_inst = ia.createIterator(); // attachIterator() ?

    while (iter_inst.next()) {
      EReferenced_declaration inst = (EReferenced_declaration) ia.getCurrentMemberObject(iter_inst);

      // may be not only named types
      if (inst.testDefinition(null)) { // named_type

        //	       ENamed_type nt = inst.getDefinition(null, (ENamed_type)null);									
        EEntity def = (EEntity) inst.getDefinition(null);


        //	       ENamed_type nt = (ENamed_type)inst.getDefinition(null);									
        // System.out.println("## Setting REFERENCED mark: " + nt.getName(null));
        //				nt.setTemp(FLAG_REFERENCED); 
        def.setTemp(FLAG_REFERENCED);
      }
    }
  }

  static void setKeepOutMarks(SdaiModel sm) throws SdaiException {
    Aggregate ia = sm.getInstances();
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EEntity inst = (EEntity) ia.getCurrentMemberObject(iter_inst);
      inst.setTemp(FLAG_KEEP_OUT); // set mark 
    }
  }

  /*
      Aggregate ia = sm.getEntityExtentInstances(ENamed_type.class);
      SdaiIterator iter_inst = ia.createIterator();
      while (iter_inst.next()) {
        ENamed_type inst = (ENamed_type)ia.getCurrentMemberObject(iter_inst);
  // System.out.println("## Setting Keep Out mark: " + inst.getName(null));
        inst.setTemp(FLAG_KEEP_OUT); // set mark 
      }
    }
    
  */
  static void setImplicitMarks(SdaiModel smx) throws SdaiException {
    ASdaiModel models = repository.getModels();
    SdaiIterator iter_models = models.createIterator();

    while (iter_models.next()) {
      SdaiModel sml = models.getCurrentMember(iter_models);
      String model_name = sml.getName();

//      String part_model_name = model_name.substring(0, 13);

//      if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
//        continue;
//      }

			if (skipModel(model_name)) {
				continue;
			}      


      if (sml != smx) {
        setImplicitModelMarks(sml); // implicit wave may cross model boundaries, it is just the initial model
      }
    }
  }

  static void setImplicitModelMarks(SdaiModel sm) throws SdaiException {
    Aggregate ia = sm.getInstances();
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EEntity inst = (EEntity) ia.getCurrentMemberObject(iter_inst);
      Object mark_obj = inst.getTemp();

      if (mark_obj != null) {
        // System.out.println("## SETTING IMPLICIT - Initial source OK: " + inst.getName(null) + ", mark: " + ((Integer)mark_obj).intValue());
        if (((Integer) mark_obj).intValue() > 0) {
          //					spreadWave(inst);
          // System.out.println("## Setting IMPLICIT - Initial source OK: " + inst.getName(null) + ", mark: " + ((Integer)mark_obj).intValue());
          startWave(inst);
        } else {
          // System.out.println("## Setting IMPLICIT - Initial source REJECTED: " + inst.getName(null) + ", mark: " + ((Integer)mark_obj).intValue());
        }
      } else {
        // System.out.println("## Setting IMPLICIT - NOT Initial source - Mark NULL: " + inst.getName(null));
      }
    }
  }

  static void setImplicitModelMarks_old(SdaiModel sm) throws SdaiException {
    Aggregate ia = sm.getEntityExtentInstances(ENamed_type.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      ENamed_type inst = (ENamed_type) ia.getCurrentMemberObject(iter_inst);
      Object mark_obj = inst.getTemp();

      if (mark_obj != null) {
        // System.out.println("## SETTING IMPLICIT - Initial source OK: " + inst.getName(null) + ", mark: " + ((Integer)mark_obj).intValue());
        if (((Integer) mark_obj).intValue() > 0) {
          //					spreadWave(inst);
          // System.out.println("## Setting IMPLICIT - Initial source OK: " + inst.getName(null) + ", mark: " + ((Integer)mark_obj).intValue());
          startWave(inst);
        } else {
          // System.out.println("## Setting IMPLICIT - Initial source REJECTED: " + inst.getName(null) + ", mark: " + ((Integer)mark_obj).intValue());
        }
      } else {
        // System.out.println("## Setting IMPLICIT - NOT Initial source - Mark NULL: " + inst.getName(null));
      }
    }
  }

  static void waveAggr(Aggregate agg) throws SdaiException {
//System.out.println(">02<: " + agg);
    SdaiIterator it = agg.createIterator();

    while (it.next()) {
      waveInst((EEntity) agg.getCurrentMemberObject(it));
    }
  }


/*

	 constants --------------------------------:
		 	- defined types
	 		- entities
		 	- constants
		 	- functions
	 
	 defined types ---------------------------:
	 
		 	- any defined data types used in the declaration of the interfaced type, including the extensible
				defined data types that this interfaced type may extend using the based on keyword, but
				excluding any of the selectable items if the interfaced type is a select type, also excluding
				those selectable items of select types that this interfaced type may be based on;
				RR: - if select type is interfaced explicitly, select elements are not interfaced implicitly (switch -is overrides that)
	
	 		- constants
	
	 		- functions
	
	 		- any defined data types represented by a select data type whose selection list contains the interfaced defined data type.
        RR: - if select element is explicitly interfaced, select type containing that element is interfaced implicitly (!!!) - not implemented? 	

	entities -------------------------------------:	

			- supertype entities
			  NOTE: subtypes are not implicitly interfaced even if they appear in a SUPERTYPE expression
			
			- all rules referring to the interfaced entity data type and zero or more other entity data
				types, all of which are either explicitly or implicitly interfaced in the current schema
				RR: does that mean that other entities in the rule must be interfaced implicitly? 
						Or rather that the rule is not interfaced implicitly if other entities are not already interfaced?
						The paragraph about interfacing of rules does not say that entities in the header must be interfaced implicitly,
						so the second is more likely? RULES should be pruned, at least when making long forms, I think, but what about short forms?
						Not to interface the rule, if some entities in the header are not interfaced at least implicitly?
						
			- all subtype constraints for the interfaced entity data type
			
			- any constants, defined data types, entity data types or functions used in the declaration of
				attributes of the interfaced entity data type;						

			- any constants, defined data types, entity data types or functions used within the domain
				rules of the interfaced entity data type;

			- any defined data types represented by a select data type which specifies the interfaced
				entity data type in its selection list.
				RR: if select element is this entity, then the select type with this entity in its select list is interfaced implicitly (!!!)

			NOTE: Subtype/supertype graphs may be pruned as a result of only following the SUBTYPE OF links
						when collecting the implicit interfaces of an interfaced entity data type. 


		functions ---------------------------------------------:
		
				- any defined data types or entity data types used in the declaration of parameters for the
					interfaced function;

				- any defined data types or entity data types used in the declaration of the returned type for
					the interfaced function;
  
  			- any defined data types or entity data types used in the declaration of local variables within
					the interfaced function;
					
				- any constants, functions or procedures used within the interfaced function
				
		procedures ----------------------------------------------:
		
				- any defined data types or entity data types used in the declaration of parameters for the
					interfaced procedure;					

				- any defined data types or entity data types used in the declaration of local variables within
					the interfaced procedure;

				- any constants, functions or procedures used within the interfaced procedure.
				
		global rules ----------------------------------------------:
		
				- any defined data types or entity data types used in the declaration of local variables within
					the interfaced rule;		

				- any constants, functions, or procedures used within the interfaced rule

		subtype constraints ----------------------------------------:
		
				- nothing is implicitly interfaced
				
*/



  static void startWave(EEntity inst) throws SdaiException {
//System.out.println(">0-0< - inst: " + inst);
    if (inst instanceof EEntity_definition) {
//System.out.println(">01-0< - entity: " + inst);
      EEntity_definition e = (EEntity_definition) inst;
      waveAggr(e.getSupertypes(null));
      waveAggr(e.getAttributes(null, null));



// inverse attribute getWhere_rules() seems to be working now, so this is no longer needed
/*
      //					waveAgg(e.getGlobal_rules(null, null));
System.out.println(">01<: " + inst);
			SdaiModel mdl = inst.findEntityInstanceSdaiModel();
			ASdaiModel amdl = new ASdaiModel();
			amdl.addByIndex(1,mdl);
//			AWhere_rule awr = e.getWhere_rules(null, amdl);
			AWhere_rule awr = e.getWhere_rules(null, null);
System.out.println(">01-A<: " + awr);
			AWhere_rule awr2 = (AWhere_rule)mdl.getInstances(EWhere_rule.class);
System.out.println(">01-B<: " + awr2);
			SdaiIterator awr_iter = awr2.createIterator();
			while (awr_iter.next()) {
				EWhere_rule wr =  awr2.getCurrentMember(awr_iter);
				EEntity parent_item = wr.getParent_item(null);
				if (parent_item == inst) {
System.out.println(">01-C<: " + wr);
		      waveInst(wr);
				}
			}
*/

    	waveAggr(e.getWhere_rules(null, null));
    } else if (inst instanceof EDefined_type) {
//System.out.println(">02-0< - defined type: " + inst);
      EDefined_type d = (EDefined_type) inst;
      waveInst(d.getDomain(null));

      waveAggr(d.getWhere_rules(null, null));
    } else if (inst instanceof EGlobal_rule) {
//System.out.println(">03-0< - global rule: " + inst);
      //					EGlobal_rule e = (EGlobal_rule) inst;
      //					waveAggr(e.getEntities(null));
      // skip where_rules	
      exploreExpression(inst);
    } else if (inst instanceof EConstant_definition) {
	      EConstant_definition c = (EConstant_definition) inst;
				waveInst(c.getDomain(null));
    } else if (inst instanceof EFunction_definition) {
      EFunction_definition f = (EFunction_definition) inst;
  		waveAggr(f.getParameters(null));
  		waveInst(f.getReturn_type(null));
      exploreExpression(inst);
  
 
    } else if (inst instanceof EProcedure_definition) {
      EProcedure_definition p = (EProcedure_definition) inst;
  		waveAggr(p.getParameters(null));
      exploreExpression(inst);
    } else {
      printDebug("Unknown wave source");
    }
  }

  static void waveInst(EEntity inst) throws SdaiException {
//System.out.println(">0-1< - inst: " + inst);
// System.out.println("--implicit- in waveInst: " + inst);
    if (inst.getTemp() == null) {
      // if (inst instanceof ENamed_type)
      //	System.out.println("## Setting IMPLICIT mark: " + ((ENamed_type)inst).getName(null));
      // else
      //	System.out.println("## Setting IMPLICIT mark - NOT named type: " + inst);
      inst.setTemp(FLAG_IMPLICIT);
// System.out.println("--implicit- in waveInst - set implicit mark: " + inst);

      // I think this approach may generate too many implicit marks on unnecessary items, but, so what?
      if (inst instanceof EEntity_definition) {
//System.out.println(">01< - entity: " + inst);
        EEntity_definition e = (EEntity_definition) inst;
        waveAggr(e.getSupertypes(null));
        waveAggr(e.getAttributes(null, null));

        //					waveAgg(e.getGlobal_rules(null, null));
        waveAggr(e.getWhere_rules(null, null));
      } else if (inst instanceof EDefined_type) {
//System.out.println(">01< - defined type: " + inst);
        EDefined_type d = (EDefined_type) inst;
 				if (d.testDomain(null)) {
	        waveInst(d.getDomain(null));
 				} else {
					 System.out.println("UNSET domain  of a defined type: " + d.getName(null));
 				}
        waveInst(d.getDomain(null));

	      waveAggr(d.getWhere_rules(null, null));
      } else if (inst instanceof EExplicit_attribute) {
        EExplicit_attribute e = (EExplicit_attribute) inst;


// System.out.println("attribute: " + e);
// System.out.println("attribute name: " + e.getName(null) + ", entity name: " + e.getParent_entity(null).getName(null));
 				if (e.testDomain(null)) {
	        waveInst(e.getDomain(null));
 				} else {
					 System.out.println("UNSET domain  of an explicit attribute - attribute name: " + e.getName(null) + ", entity name: " + e.getParent_entity(null).getName(null));
 				}
      } else if (inst instanceof EDerived_attribute) {
        EDerived_attribute e = (EDerived_attribute) inst;
 				if (e.testDomain(null)) {
	        waveInst(e.getDomain(null));
 				} else {
					 System.out.println("UNSET domain  of a derived attribute - attribute name: " + e.getName(null) + ", entity name: " + e.getParent_entity(null).getName(null));
 				}
        // waveInst(e.getDomain(null));
      } else if (inst instanceof EInverse_attribute) {
        EInverse_attribute e = (EInverse_attribute) inst;
 				if (e.testDomain(null)) {
	        waveInst(e.getDomain(null));
 				} else {
					 System.out.println("UNSET domain  of an inverse attribute - attribute name: " + e.getName(null) + ", entity name: " + e.getParent_entity(null).getName(null));
 				}
//        waveInst(e.getDomain(null));
      } else if (inst instanceof EGlobal_rule) {
        //					EGlobal_rule e = (EGlobal_rule) inst;
        //					waveAggr(e.getEntities(null));
        // skip where_rules	
        exploreExpression(inst);
    } else if (inst instanceof EAggregation_type) {
        EAggregation_type e = (EAggregation_type) inst;
// System.out.println("aggregate in wave: " + e);
        waveInst(e.getElement_type(null));

        //			} else if (inst instanceof E) {
      } else if (inst instanceof ESelect_type) {
        // new stuff for extensible



        if (inst instanceof EExtended_select_type) {
        	EExtended_select_type e = (EExtended_select_type)inst;
//        	waveInst(e.getIs_based_on(null));
// System.out.println("--implicit- Extended Select: " + inst);
//     			EExtensible_select_type x = e.getIs_based_on(null);
     			EExtensible_select_type x = (EExtensible_select_type)e.getIs_based_on(null).getDomain(null);
     			// find the defined_type for x first, the wave should be  spread from the defined_type
	 				AEntity result = new AEntity();
	 				ASdaiModel domain = new ASdaiModel();
          domain.addByIndex(1, x.findEntityInstanceSdaiModel(), null);	 				
	        CDefined_type.usedinDomain(null, x, domain, result);
// System.out.println("--inverse result count: " + result.getMemberCount());
	        EDefined_type dt = (EDefined_type)result.getByIndexEntity(1);
// System.out.println("--inverse result: " + dt);
     			
//        	waveInst(e.getIs_based_on(null));
// System.out.println("--implicit- Extended Select - spreading: " + x);
        	waveInst(dt);

        }
        

       
        if (flag_implicit_select) {
          ESelect_type e = (ESelect_type) inst;
//          waveAggr(e.getSelections(null));
//          waveAggr(e.getLocal_selections(null));
          waveAggr(getSelections(e));
        }
      } else if (inst instanceof EEnumeration_type) {
// System.out.println("--implicit- Enumeration: " + inst);
        // new stuf for extensible
        if (inst instanceof EExtended_enumeration_type) {
// System.out.println("--implicit- Extended Enumeration: " + inst);
        	EExtended_enumeration_type e = (EExtended_enumeration_type)inst;
//     			EExtensible_enumeration_type x = e.getIs_based_on(null);
     			EExtensible_enumeration_type x = (EExtensible_enumeration_type)e.getIs_based_on(null).getDomain(null);
     			// find the defined_type for x first, the wave should be  spread from the defined_type
	 				AEntity result = new AEntity();
	 				ASdaiModel domain = new ASdaiModel();
          domain.addByIndex(1, x.findEntityInstanceSdaiModel(), null);	 				
	        CDefined_type.usedinDomain(null, x, domain, result);
// System.out.println("--inverse result count: " + result.getMemberCount());
	        EDefined_type dt = (EDefined_type)result.getByIndexEntity(1);
// System.out.println("--inverse result: " + dt);
     			
//        	waveInst(e.getIs_based_on(null));
// System.out.println("--implicit- Extended Enumeration - spreading: " + x);
        	waveInst(dt);
        }
      } else if (inst instanceof EWhere_rule) {
//System.out.println(">03< - where rule: " + inst);
      	// have to traverse the parse tree if it is available, if not - create it by parsing the corresponding string fragment
      	exploreExpression(inst);
      } else if (inst instanceof EConstant_definition) {
	      EConstant_definition c = (EConstant_definition) inst;
				waveInst(c.getDomain(null));
      } else if (inst instanceof EFunction_definition) {
	      EFunction_definition f = (EFunction_definition) inst;
  			waveAggr(f.getParameters(null));
  			waveInst(f.getReturn_type(null));
      	exploreExpression(inst);
      } else if (inst instanceof EProcedure_definition) {
	      EProcedure_definition p = (EProcedure_definition) inst;
  			waveAggr(p.getParameters(null));
	      exploreExpression(inst);
	  	} else if (inst instanceof EParameter) {
	      EParameter p = (EParameter) inst;
				waveInst(p.getParameter_type(null));
      } else {
        printDebug("In waveInst, unknown wave source");
      }

      // nothing to do for
      //   simple_type
      //   enumeration_type
    } // if not marked
    else {
      Object tmp_mark = inst.getTemp();
      Integer int_mark = (Integer) tmp_mark;
      int int_int = int_mark.intValue();

      // if (inst instanceof ENamed_type)
      //	System.out.println("## Wave encountered an obstacle - mark NOT NULL: " + int_int + ", name: " + ((ENamed_type)inst).getName(null));
      // else
      //	System.out.println("## Wave encountered an obstacle  - mark NOT NULL - NOT named type: "  + int_int + ", name: " + inst);
    }
  }


	static void exploreExpression(EEntity inst) throws SdaiException {
		if (!expression_instances) {
//      return;
    }
//System.out.println("flag_implicit_expressions: " + flag_implicit_expressions);
    if (!flag_implicit_expressions) {
//System.out.println("DO NOT POCEEDING with expressions");
    	return;
    }
//System.out.println("POCEEDING Exploring expressions");
//System.out.println(">04< - IN where rule: " + inst);
    SdaiModel inst_model = inst.findEntityInstanceSdaiModel();
		ESchema_definition inst_schema = (ESchema_definition)getSchema_definitionFromModel(inst_model);
		if (inst_model.getMode() == SdaiModel.READ_WRITE) {
			// parse tree is available
//System.out.println(">04-A< ");
			traverseParseTree(inst, inst_model, inst_schema);
		} else {


			// this is not a good solution, see if in pass 5 all cases where instances are created could be replaced by search first, etc.

      if (inst_model.getMode() == SdaiModel.NO_ACCESS) {
        inst_model.startReadWriteAccess();
      } else if (inst_model.getMode() == SdaiModel.READ_ONLY) {
        inst_model.promoteSdaiModelToRW();
      }


			// incremental - parse tree fragment is not available, need to make it by parsing the corresponding string 




      if (inst_model.getMode() == SdaiModel.NO_ACCESS) {
				inst_model.startReadOnlyAccess();
  		}
			String str = null;
			String name_searched = "_EXPRESS_" + inst_schema.getName(null).toUpperCase();
	  	ASdaiModel models  = repository.getModels();
			SdaiIterator iter_models = models.createIterator();
			boolean all_done = false;
			while (iter_models.next()) {
				SdaiModel sm1 = models.getCurrentMember(iter_models);
				String model_name = sm1.getName();
// System.out.println("findModel - searching: " + name_searched + ", current: " + model_name + ", nr of models: " + models.getMemberCount());
				if (model_name.equalsIgnoreCase(name_searched)) {
									// now find the express_code instance for the attribute
									
					Aggregate ia = sm1.getEntityExtentInstances(EExpress_code.class);
					SdaiIterator iter_inst = ia.createIterator();
					while (iter_inst.next()) {
						EExpress_code xc = (EExpress_code)ia.getCurrentMemberObject(iter_inst);
						EEntity target = xc.getTarget(null);
						if (target == inst) {
							// found our attribute, get the string now
							A_string values = xc.getValues(null);
							str = values.getByIndex(1);										
							all_done = true;
							break;
						}	
					}
				}
				if (all_done) {
					break;
				}
		
		
			} // while models
	
			if (str != null) {
// System.out.println("XOX string fragment: " + str);
								// String str = "2 + 3";
//								X_Expression x_e = Compiler2.runParserDerivedExpression(str, model, attr_schema, _ed, (jsdai.SExtended_dictionary_schema.EDerived_attribute)attr, parser);
//				X_Expression x_e = Compiler2.runParserDerivedExpression(str, attr_model, attr_schema, _ed, (jsdai.SExtended_dictionary_schema.EDerived_attribute)attr, parser);

	if (inst instanceof EWhere_rule) {
			
//System.out.println("IMPLICIT 1 - parsing string fragment");
				
					X_Expression x_e = Compiler2.runParserEntityRuleExpression(str, inst_model, inst_schema, (EEntity_definition)((EWhere_rule)inst).getParent_item(null), (EWhere_rule)inst, x_parser);

						 		// x_e.dump("Derived fragment NODE: ");
// pw.println("// before generateJavaExpressionInc");

	    	traverseParseTreeInc(inst, inst_model, inst_schema, x_e);
//	            	generateJavaExpressionInc(pw, attr, attr_schema, _ed, attr_model, x_e);
	
	}	
			}
	
	
		
	
		}
	}

	static void traverseParseTree(EEntity inst, SdaiModel inst_model, ESchema_definition inst_schema) throws SdaiException {
		if (!expression_instances) {
//      return;
    }

//   JavaClass(PrintWriter pw1, EEntity ee, EGeneric_schema_definition __sd, EEntity_definition __ed, 
//            SdaiModel current_model, boolean f_value, JavaBackend java_backend_instance) {

//System.out.println(">05<:  " + inst);
 		EEntity_definition __ed = null;
    JavaClass jc = new JavaClass(null, inst, inst_schema, __ed, inst_model, true, null);
//    Compiler2Visitor v = new ECxImplicitExpressions(flag_debug, flag_deep_debug, flag_no_print_active_nodes, flag_split_debug, jc);
    Compiler2Visitor v = new ECxImplicitExpressions(false, false, false, false, jc);


    x_tree.jjtAccept(v, jc);


	}
	static void traverseParseTreeInc(EEntity inst, SdaiModel inst_model, ESchema_definition inst_schema, X_Expression x) throws SdaiException {
		if (!expression_instances) {
//      return;
    }

//   JavaClass(PrintWriter pw1, EEntity ee, EGeneric_schema_definition __sd, EEntity_definition __ed, 
//            SdaiModel current_model, boolean f_value, JavaBackend java_backend_instance) {

//System.out.println("IMPLICIT 2 - parsing string fragment");
 		EEntity_definition __ed = null;
    JavaClass jc = new JavaClass(null, inst, inst_schema, __ed, inst_model, true, null);
//    Compiler2Visitor v = new ECxImplicitExpressions(flag_debug, flag_deep_debug, flag_no_print_active_nodes, flag_split_debug, jc);
    jc.active = true;
		jc.expression_fragment = true;
    Compiler2Visitor v = new ECxImplicitExpressions(false, false, false, false, jc);


    x.jjtAccept(v, jc);






	}
	
  static void spreadWave(ENamed_type inst) throws SdaiException {
    if (inst instanceof EDefined_type) {
      exploreUnderlyingType((EDefined_type) inst);
    } else if (inst instanceof EEntity_definition) {
      exploreSupertypes((EEntity_definition) inst);


      //				exploreSubtypes((EEntity_definition)inst);
      exploreAttributeBaseTypes((EEntity_definition) inst);
    } else {
      // should not happen
    }
  }

  static void exploreSupertypes(EEntity_definition ed)
                         throws SdaiException {
    // pre-X		AEntity_definition supertypes = ed.getSupertypes(null);
//    AEntity_or_view_definition supertypes = ed.getSupertypes(null);
    AEntity_or_view_definition supertypes = ed.getGeneric_supertypes(null);
    SdaiIterator iter_supertypes = supertypes.createIterator();

    while (iter_supertypes.next()) {
      // pre-X			EEntity_definition super_ed = supertypes.getCurrentMember(iter_supertypes);
      // only Express
      EEntity_definition super_ed = (EEntity_definition) supertypes.getCurrentMember(
                                          iter_supertypes);
      Object mark = super_ed.getTemp();

      if (mark == null) {
        super_ed.setTemp(FLAG_IMPLICIT);
        spreadWave(super_ed);
      }
    }
  }

  static void exploreSubtypes(EEntity_definition ed) {
  }

  static void exploreAttributeBaseTypes(EEntity_definition ed)
                                 throws SdaiException {
    AAttribute aa = ed.getAttributes(null, null);
    SdaiIterator iter_attributes = aa.createIterator();

    while (iter_attributes.next()) {
      EAttribute attr = aa.getCurrentMember(iter_attributes);

      // System.out.println("Attribute: " + attr.getName(null) + " of entity: " + ed.getName(null));
    }
  }

  static void exploreUnderlyingType(EDefined_type dt) throws SdaiException {
    //		switch (dt.testDomain(null)) {
    //			case sDomainSimple_type:
    //				break;
    //		}
  }

  static void generateImplicitModelDeclarations(SdaiModel sm_in)
                                         throws SdaiException {
    ASdaiModel models = repository.getModels();
    SdaiIterator iter_models = models.createIterator();

    while (iter_models.next()) {
      SdaiModel sm_from = models.getCurrentMember(iter_models);
      String model_name = sm_from.getName();

//      String part_model_name = model_name.substring(0, 13);

//      if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
//        continue;
//      }

			if (skipModel(model_name)) {
				continue;
			}      

      generateImplicitDeclarationsInModelFromModel(sm_in, sm_from);
    }
  }

  static void generateImplicitDeclarationsInModelFromModel(SdaiModel sm_in, SdaiModel sm_from)
                                                    throws SdaiException {
    ENamed_type ntt = null;
    Aggregate ia;
    Aggregate iat;
    SdaiIterator iter_inst;
    SdaiIterator itert;
    Object mark;
		String candidate_name = null;
		String current_name = null;
		String first_type = null;
    boolean skip_it;

    // pre-X	ESchema_definition schd = getSchema_definitionFromModel(sm_in);
    // pre-X	ESchema_definition schd_from = getSchema_definitionFromModel(sm_from);
    EGeneric_schema_definition schd = getGeneric_schema_definitionFromModel(sm_in);
    EGeneric_schema_definition schd_from = getGeneric_schema_definitionFromModel(sm_from);


    //		ESchema_definition schd = (ESchema_definition)sm_in.getDefined_schema();
    ia = sm_from.getEntityExtentInstances(EDefined_type.class);
    iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDefined_type inst = (EDefined_type) ia.getCurrentMemberObject(iter_inst);
      mark = inst.getTemp();

      if (mark == FLAG_IMPLICIT) {

				candidate_name = inst.getName(null);

        // add here debugging printout if the declaration is already here
        iat = sm_in.getEntityExtentInstances(EDeclaration.class);
        itert = iat.createIterator();
        skip_it = false;

        // System.out.println("Candidate for duplication: " + nt.getName(null));
        boolean is_the_same = false;
        while (itert.next()) {
          EDeclaration instt = (EDeclaration) iat.getCurrentMemberObject(itert);
          EEntity dct = (EEntity) instt.getDefinition(null);

          // System.out.println("Current already in target: " + ntt.getName(null));
          is_the_same = false;
          if (inst == dct) {
          	is_the_same = true;
            ntt = (ENamed_type) dct;

            if (instt instanceof ELocal_declaration) {
              // System.out.println("#RR#TL implicit declaration is the same as local - internal error: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EUsed_declaration) {
              // System.out.println("#RR#TU implicit declaration is the same as a used one: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EReferenced_declaration) {
              // System.out.println("#RR#TR implicit declaration is the same as a referenced one: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EImplicit_declaration) {
              // System.out.println("#RR#TI implicit declaration is the same as another implicit: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            }

            // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
            skip_it = true;

            //					break;
          } else { 
						current_name = null;
						first_type = "type";

						if (dct instanceof EDefined_type) {
							current_name = ((EDefined_type)dct).getName(null);						
  						first_type = "a different defined type";
						} else
						if (dct instanceof EEntity_definition) {
							current_name = ((EEntity_definition)dct).getName(null);						
							first_type = "an entity";
						} else
						if (dct instanceof EFunction_definition) {
							current_name = ((EFunction_definition)dct).getName(null);						
							first_type = "a function";
						} else
						if (dct instanceof EProcedure_definition) {
							current_name = ((EProcedure_definition)dct).getName(null);						
							first_type = "a procedure";
						} else
						if (dct instanceof EConstant_definition) {
							current_name = ((EConstant_definition)dct).getName(null);						
							first_type = "a constant";
						} else
						if (dct instanceof EGlobal_rule) {
							current_name = ((EGlobal_rule)dct).getName(null);						
							first_type = "a global rule";
						}	else {
							first_type = "a type";
						}					
						if (current_name != null) {
							if (candidate_name.equalsIgnoreCase(current_name)) {
								skip_it = true;
								if (!is_the_same) { // this check is not needed at all 
//								printWarning(current_name + ": attempting to interface implicitly this type, but its name is already in use in schema " + schd.getName(null));
//								printWarningMsg("" + current_name + " - attempting to interface implicitly this type, but its name is already in use in schema " + schd.getName(null), null, true);

									SdaiModel inst_model = inst.findEntityInstanceSdaiModel();
									ESchema_definition inst_sch = getSchema_definitionFromModel(inst_model);
									String inst_schema = inst_sch.getName(null).toLowerCase();
									System.out.println("WARNING: " + current_name + " - attempting to interface implicitly this defined type from " + inst_schema +  ", but its name is already used in schema " + schd.getName(null).toLowerCase() + " for " + first_type);
								} // not the same instance
							} // the same name
						} // current declaration is of something that has name
        	} // not the same instance
        } // loop through all the declarations in the model

        if (!skip_it) {
          // System.out.println("#RR#OK-TI implicit type declaration - first time: " + inst.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
	        
	        EDeclaration tdc = (EDeclaration) sm_in.createEntityInstance(CImplicit_declaration$type_declaration.class);
  	      tdc.setParent(null, schd);
    	   	// parent_schema is now derived instead of explicit
    	   	// if (schd instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
      	 		// tdc.setParent_schema(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)schd);
       		// }
        	tdc.setDefinition(null, (ENamed_type) inst);
        }
      }
    }

    ia = sm_from.getEntityExtentInstances(EEntity_definition.class);
    iter_inst = ia.createIterator(); // is attachIterator() still available?

    while (iter_inst.next()) {
      EEntity_definition inst = (EEntity_definition) ia.getCurrentMemberObject(iter_inst);
      mark = inst.getTemp();

      if (mark == FLAG_IMPLICIT) {

				candidate_name = inst.getName(null);

        iat = sm_in.getEntityExtentInstances(EDeclaration.class);
        itert = iat.createIterator();
        skip_it = false;

        // System.out.println("Candidate for duplication: " + nt.getName(null));
        boolean is_the_same = false;
        while (itert.next()) {
          EDeclaration instt = (EDeclaration) iat.getCurrentMemberObject(itert);
          EEntity dct = (EEntity) instt.getDefinition(null);

          //					ENamed_type ntt = null;
          // System.out.println("Current already in target: " + ntt.getName(null));
          is_the_same = false;
          if (inst == dct) {
          	is_the_same = true;
            ntt = (ENamed_type) dct;

            if (instt instanceof ELocal_declaration) {
              // System.out.println("#RR#EL implicit declaration is the same as local - internal error: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EUsed_declaration) {
              // System.out.println("#RR#EU implicit declaration is the same as a used one: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EReferenced_declaration) {
              // System.out.println("#RR#ER implicit declaration is the same as a referenced one: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EImplicit_declaration) {
              // System.out.println("#RR#EI implicit declaration is the same as another implicit: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            }

            // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
            skip_it = true;
            //					break;
          } else { 
						current_name = null;
						first_type = "type";

						if (dct instanceof EDefined_type) {
							current_name = ((EDefined_type)dct).getName(null);						
  						first_type = "a defined type";
						} else
						if (dct instanceof EEntity_definition) {
							current_name = ((EEntity_definition)dct).getName(null);						
							first_type = "a different entity";
						} else
						if (dct instanceof EFunction_definition) {
							current_name = ((EFunction_definition)dct).getName(null);						
							first_type = "a function";
						} else
						if (dct instanceof EProcedure_definition) {
							current_name = ((EProcedure_definition)dct).getName(null);						
							first_type = "a procedure";
						} else
						if (dct instanceof EConstant_definition) {
							current_name = ((EConstant_definition)dct).getName(null);						
							first_type = "a constant";
						} else
						if (dct instanceof EGlobal_rule) {
							current_name = ((EGlobal_rule)dct).getName(null);						
							first_type = "a global rule";
						}	else {
							first_type = "a type";
						}					
						if (current_name != null) {
							if (candidate_name.equalsIgnoreCase(current_name)) {
								skip_it = true;
								if (!is_the_same) { // this check is not needed at all 
//								printWarning(current_name + ": attempting to interface implicitly this type, but its name is already in use in schema " + schd.getName(null));
//								printWarningMsg("" + current_name + " - attempting to interface implicitly this type, but its name is already in use in schema " + schd.getName(null), null, true);

									SdaiModel inst_model = inst.findEntityInstanceSdaiModel();
									ESchema_definition inst_sch = getSchema_definitionFromModel(inst_model);
									String inst_schema = inst_sch.getName(null).toLowerCase();
									System.out.println("WARNING: " + current_name + " - attempting to interface implicitly this entity from " + inst_schema +  ", but its name is already used in schema " + schd.getName(null).toLowerCase() + " for " + first_type);
								} // not the same instance
							} // the same name
						} // current declaration is of something that has name
        	} // not the same instance

/*88888888888
					if (dct instanceof ENamed_type) {
						current_name = ((ENamed_type)dct).getName(null);						
						if (candidate_name.equalsIgnoreCase(current_name)) {
							skip_it = true;
							if (!is_the_same) {
//								printWarning(current_name + ": attempting to interface implicitly this entity, but its name is already in use in schema " + schd.getName(null));
								printWarningMsg("" + current_name + " - attempting to interface implicitly this entity, but its name is already in use in schema " + schd.getName(null), null, false);
							}
						}
					}
888888888888 */
        }




        if (!skip_it) {
          // System.out.println("#RR#OK-EI implicit entity declaration - first time: " + inst.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
	        EDeclaration edc = (EDeclaration) sm_in.createEntityInstance(CEntity_declaration$implicit_declaration.class);
    	    edc.setParent(null, schd);
      	 	// parent_schema now is derived instead of explicit
      	 	// if (schd instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
       			// edc.setParent_schema(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)schd);
     	  	// }
      	  edc.setDefinition(null, (ENamed_type) inst);
        }
      }
    }

    ia = sm_from.getEntityExtentInstances(EFunction_definition.class);
    iter_inst = ia.createIterator(); // is attachIterator() still available?

    while (iter_inst.next()) {
      EFunction_definition inst = (EFunction_definition) ia.getCurrentMemberObject(iter_inst);
      mark = inst.getTemp();

      if (mark == FLAG_IMPLICIT) {

				candidate_name = inst.getName(null);

        iat = sm_in.getEntityExtentInstances(EDeclaration.class);
        itert = iat.createIterator();
        skip_it = false;

        // System.out.println("Candidate for duplication: " + nt.getName(null));
       	boolean is_the_same = false;
        while (itert.next()) {
          EDeclaration instt = (EDeclaration) iat.getCurrentMemberObject(itert);
          EEntity dct = (EEntity) instt.getDefinition(null);

          //					ENamed_type ntt = null;
          // System.out.println("Current already in target: " + ntt.getName(null));
          is_the_same = false;
          if (inst == dct) {
          	is_the_same = true;
//            ntt = (ENamed_type) dct;

            if (instt instanceof ELocal_declaration) {
              // System.out.println("#RR#EL implicit declaration is the same as local - internal error: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EUsed_declaration) {
              // System.out.println("#RR#EU implicit declaration is the same as a used one: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EReferenced_declaration) {
              // System.out.println("#RR#ER implicit declaration is the same as a referenced one: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            } else 
            if (instt instanceof EImplicit_declaration) {
              // System.out.println("#RR#EI implicit declaration is the same as another implicit: " + ntt.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
            }


            // System.out.println("This one must be skipped: " + ntt.getName(null) + " - " + ntt.getName(null));
            skip_it = true;

            //					break;
          } else { 
						current_name = null;
						first_type = "type";

						if (dct instanceof EDefined_type) {
							current_name = ((EDefined_type)dct).getName(null);						
  						first_type = "a defined type";
						} else
						if (dct instanceof EEntity_definition) {
							current_name = ((EEntity_definition)dct).getName(null);						
							first_type = "an entity";
						} else
						if (dct instanceof EFunction_definition) {
							current_name = ((EFunction_definition)dct).getName(null);						
							first_type = "a different function";
						} else
						if (dct instanceof EProcedure_definition) {
							current_name = ((EProcedure_definition)dct).getName(null);						
							first_type = "a procedure";
						} else
						if (dct instanceof EConstant_definition) {
							current_name = ((EConstant_definition)dct).getName(null);						
							first_type = "a constant";
						} else
						if (dct instanceof EGlobal_rule) {
							current_name = ((EGlobal_rule)dct).getName(null);						
							first_type = "a global rule";
						}	else {
							first_type = "a type";
						}					
						if (current_name != null) {
							if (candidate_name.equalsIgnoreCase(current_name)) {
								skip_it = true;
								if (!is_the_same) { // this check is not needed at all 
//								printWarning(current_name + ": attempting to interface implicitly this type, but its name is already in use in schema " + schd.getName(null));
//								printWarningMsg("" + current_name + " - attempting to interface implicitly this type, but its name is already in use in schema " + schd.getName(null), null, true);

									SdaiModel inst_model = inst.findEntityInstanceSdaiModel();
									ESchema_definition inst_sch = getSchema_definitionFromModel(inst_model);
									String inst_schema = inst_sch.getName(null).toLowerCase();
									System.out.println("WARNING: " + current_name + " - attempting to interface implicitly this function from " + inst_schema +  ", but its name is already used in schema " + schd.getName(null).toLowerCase() + " for " + first_type);
								} // not the same instance
							} // the same name
						} // current declaration is of something that has name
        	} // not the same instance

/*
					if (dct instanceof ENamed_type) {
						current_name = ((ENamed_type)dct).getName(null);						
						if (candidate_name.equalsIgnoreCase(current_name)) {
							skip_it = true;
							if (!is_the_same) {
//								printWarning(current_name + ": attempting to interface implicitly this function, but its name is already in use in schema " + schd.getName(null));
								printWarningMsg("" + current_name + " - attempting to interface implicitly this function, but its name is already in use in schema " + schd.getName(null), null, false);
							}
						}
					}

*/
					// perhaps also check the names of function_definitions
        }

        if (!skip_it) {
          // System.out.println("#RR#OK-EI implicit entity declaration - first time: " + inst.getName(null) + ", from: " + schd_from.getName(null) + ", to: " + schd.getName(null));
        	EDeclaration edc = (EDeclaration) sm_in.createEntityInstance(CFunction_declaration$implicit_declaration.class);
        	edc.setParent(null, schd);
					// parent_schema is now derived instead of explicit 
      	 	// if (schd instanceof jsdai.SExtended_dictionary_schema.ESchema_definition) {
    	   		// edc.setParent_schema(null, (jsdai.SExtended_dictionary_schema.ESchema_definition)schd);
  	     	// }
	        edc.setDefinition(null, (EFunction_definition) inst);
        }
      }
    }
  }


/* 

 here we will attemt to collect all the selections for an extensible select from later extended selects as well as from previous selects if this extensible select is at the same time also an extended select,
 and also local selections, of course

*/

	static ANamed_type getAllSelections(ESelect_type st) throws SdaiException {
		// ANamed_type l_selections = null;
		ANamed_type selections = null;
		if (st.testLocal_selections(null)) {
			selections = st.getLocal_selections(null);
		}
		if (st instanceof EExtended_select_type) {
			selections = addSelectionsFromExtensible((EExtended_select_type)st, selections);
			return selections;
		}
		if (st instanceof EExtensible_select_type) {
			// add all selections from later extended
			selections = addSelectionsFromLaterExtended((EExtensible_select_type)st, selections);
			return selections;
		}
		return selections;
	}

	static ANamed_type getSelections(ESelect_type st) throws SdaiException {
		ANamed_type l_selections = null;
		ANamed_type selections = null;
		if (st.testLocal_selections(null)) {
			l_selections = st.getLocal_selections(null);
		}
		if (st instanceof EExtended_select_type) {
			selections = addSelectionsFromExtensible((EExtended_select_type)st, l_selections);
			return selections;
		}
// System.out.println("XAM: number of selections: " + selections.getMemberCount());
		return l_selections;
	}

	static ANamed_type addSelectionsFromLaterExtended(EExtensible_select_type st, ANamed_type current_selections) throws SdaiException {
		// go through all the type declarations and check if the type is select and then if it is extended and if so, if it directly or indirectly extends st.

	    SdaiModel mdl = st.findEntityInstanceSdaiModel();
  	  Aggregate itd = mdl.getEntityExtentInstances(EType_declaration.class);
    	SdaiIterator iter_td = itd.createIterator();
	    while (iter_td.next()) {
  	    EType_declaration td = (EType_declaration) itd.getCurrentMemberObject(iter_td);
				EDefined_type tdef = (EDefined_type)td.getDefinition(null);
				Object ut = tdef.getDomain(null); 
				if (ut instanceof ESelect_type) {
					if (ut == st) { // found our select - can add selections
					} else {
						if (ut instanceof EExtended_select_type) {
							// check if directly or inderectly extends st
							boolean if_extends = extendsOrNot((EExtended_select_type)ut, st);
							if (if_extends) {
								// add selections
								ANamed_type selections = ((ESelect_type)ut).getLocal_selections(null);

								if (selections != null) {
									if (selections.getMemberCount() > 0) {
										if (current_selections == null) {
											current_selections = new ANamed_type();
										}	
		
										for (int i = 1; i < selections.getMemberCount() + 1; i++) {
											ENamed_type element = (ENamed_type)selections.getByIndexEntity(i);
											if (!(current_selections.isMember(element))) {
												current_selections.addUnordered(element);
											}
										} // for
									} // if > 0
								} // if != null


			
							}
						}
					}
				} else {
					// not interested
				}
			} // while - through all the type declarations
			return current_selections;
	}

	static boolean extendsOrNot(EExtended_select_type current, EExtensible_select_type required) throws SdaiException {
		EDefined_type dt = current.getIs_based_on(null);
		Object ut = dt.getDomain(null); 
		if (ut instanceof ESelect_type) {
			if (ut == required) {
				return true;
			} else {
				if (ut instanceof EExtended_select_type) {
					boolean if_extends = extendsOrNot((EExtended_select_type)ut, required);
					if (if_extends) {
						return true;
					}
				} // extended
			} // not required
		} // select type
		
		return false;
	}

   
  
  

	static ANamed_type addSelectionsFromExtensible(EExtended_select_type st, ANamed_type current_selections) throws SdaiException {
		ANamed_type l_selections = null;
		ANamed_type selections = null;
//		ESelect_type prior = st.getIs_based_on(null);
// System.out.println("PROBLEM with based_on: " + st);
		ESelect_type prior = (ESelect_type)st.getIs_based_on(null).getDomain(null);
		if (prior.testLocal_selections(null)) {
			l_selections = prior.getLocal_selections(null);
			selections = new ANamed_type();
			for (int i = 1; i < l_selections.getMemberCount() + 1; i++) {
				ENamed_type element = (ENamed_type)l_selections.getByIndexEntity(i);
				selections.addUnordered(element);
			}
		}
		if (current_selections != null) {
			if (current_selections.getMemberCount() > 0) {
				if (selections == null) {
					selections = new ANamed_type();
				}	
		
		// selections = selections + current_selections, in that order, perhaps check for duplicates because it is SET
// System.out.println("XXRR: number of selections: " + current_selections.getMemberCount());
				for (int i = 1; i < current_selections.getMemberCount() + 1; i++) {
// System.out.println("Index: " + i + " - type: " + st);
					ENamed_type element = (ENamed_type)current_selections.getByIndexEntity(i);
					if (!(selections.isMember(element))) {
// System.out.println("XXRR Adding element from extensible: " + element);				
						selections.addUnordered(element);
					}
				}
			}
		}
		if (prior instanceof EExtended_select_type) {
// System.out.println("PROBLEM with based_on - prior: " + prior);
			selections = addSelectionsFromExtensible((EExtended_select_type)prior, selections);
		}
		return selections;
	}


  static Vector A_string2Vector(A_string input)  throws SdaiException {
		Vector result;
		if (input == null) {
			return null;
		}
		result = new Vector();
		for (int i = 1; i < input.getMemberCount() + 1; i++) {
			String element = (String)input.getByIndex(i);
			result.addElement(element);
		}
		return result;
	}	

	static Vector getElementsV(EEnumeration_type et) throws SdaiException {
		A_string l_elements = null;
		Vector vl_elements = null;
		Vector elements = null;
		if (et.testLocal_elements(null)) {
			l_elements = et.getLocal_elements(null);
			vl_elements = A_string2Vector(l_elements);
		}
		if (et instanceof EExtended_enumeration_type) {
			elements = addElementsFromExtensibleV((EExtended_enumeration_type)et, vl_elements);
			return elements;
		}
		return vl_elements;
	}

	static Vector addElementsFromExtensibleV(EExtended_enumeration_type et, Vector current_elements) throws SdaiException {
		A_string l_elements = null;
//		Vector vl_elements = null;
		Vector elements = null;
//		EEnumeration_type prior = et.getIs_based_on(null);
		EEnumeration_type prior = (EEnumeration_type)et.getIs_based_on(null).getDomain(null);
		if (prior.testLocal_elements(null)) {
			l_elements = prior.getLocal_elements(null);
//			vl_elements = A_string2Vector(l_elements);
			elements = A_string2Vector(l_elements);
		}
		if (current_elements != null) {
			if (current_elements.size() > 0) {
				if (elements == null) {
					elements = new Vector();
				}	
		
				for (int i = 0; i < current_elements.size(); i++) {
					String element = (String)current_elements.elementAt(i);
					if (!(elements.contains(element))) {
						elements.addElement(element);
					}
				}
			}
		}
		if (prior instanceof EExtended_enumeration_type) {
			elements = addElementsFromExtensibleV((EExtended_enumeration_type)prior, elements);
		}
		return elements;
	}

/*

	static A_string getElements(EEnumeration_type et) throws SdaiException {
		A_string l_elements = null;
		A_string elements = null;
		if (et.testLocal_elements(null)) {
			l_elements = et.getLocal_elements(null);
// System.out.println("XAME 01: local nr: " + l_elements.getMemberCount() + " -: " + l_elements);			
		}
		if (et instanceof EExtended_enumeration_type) {
			elements = addElementsFromExtensible((EExtended_enumeration_type)et, l_elements);
			return elements;
		}
// System.out.println("XAME: number of elements: " + elements.getMemberCount());
		return l_elements;
	}
	
	static A_string addElementsFromExtensible(EExtended_enumeration_type et, A_string current_elements) throws SdaiException {
		A_string l_elements = null;
		A_string elements = null;
		EEnumeration_type prior = et.getIs_based_on(null);
		if (prior.testLocal_elements(null)) {
			l_elements = prior.getLocal_elements(null);
			elements = new A_string();
			for (int i = 1; i < l_elements.getMemberCount() + 1; i++) {
				String element = (String)l_elements.getByIndex(i);
				elements.addUnordered(element);
			}
		}
		if (current_elements != null) {
			if (current_elements.getMemberCount() > 0) {
				if (elements == null) {
					elements = new A_string();
				}	
		
		// elements = elements + current_elements, in that order, perhaps check for duplicates because it is SET
// System.out.println("XXRRE: number of elements: " + current_elements.getMemberCount());
				for (int i = 1; i < current_elements.getMemberCount() + 1; i++) {
// System.out.println("Index: " + i + " - type: " + et);
					String element = (String)current_elements.getByIndex(i);
					if (!(elements.isMember(element))) {
// System.out.println("XXRRE Adding element from extensible: " + element);				
						elements.addUnordered(element);
					}
				}
			}
		}
		if (prior instanceof EExtended_enumeration_type) {
			elements = addElementsFromExtensible((EExtended_enumeration_type)prior, elements);
		}
		return elements;
	}


*/

		static boolean skipModel(String model_name) {
			String part_model_name;
			
	    if (model_name.length() > 15) {
        part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          // System.out.println("Eliminating documentation model: " + model_name);
          return true;
        }
      }

      if (model_name.length() > 13) {
        part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          // System.out.println("Eliminating expressions model: " + model_name);
          return true;
        }
      }

      if (model_name.length() > 9) {
        part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          // System.out.println("Eliminating express model: " + model_name);
          return true;
        }
      }

      if (model_name.length() > 6) {
        part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          // System.out.println("Eliminating java model: " + model_name);
          return true;
        }
      }

      if (model_name.length() > 13) {
        part_model_name = model_name.substring(model_name.length() - 13);

        if (part_model_name.equalsIgnoreCase("_MAPPING_DATA")) {
          // System.out.println("Eliminating mapping_data model: " + model_name);
          return true;
        }
      }

			// why do we even need all the above 

      if (model_name.length() > 16) {
        part_model_name = model_name.substring(model_name.length() - 16);

        // System.out.println("Part model name: " + part_model_name);
        if (!(part_model_name.equalsIgnoreCase("_DICTIONARY_DATA"))) {
          //			System.out.println("Eliminating not dictionary data model: " + model_name + ", part: " + part_model_name);
          return true;
        }
      }

      if (model_name.length() <= 16) {
        // System.out.println("Eliminating short name model: " + model_name);
        return true;
      }

		  return false;
		}

		static boolean skipModelM(String model_name) {
			String part_model_name;
			
	    if (model_name.length() > 15) {
        part_model_name = model_name.substring(0, 15);

        if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
          // System.out.println("Eliminating documentation model: " + model_name);
          return true;
        }
      }

      if (model_name.length() > 13) {
        part_model_name = model_name.substring(0, 13);

        if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
          // System.out.println("Eliminating expressions model: " + model_name);
          return true;
        }
      }

      if (model_name.length() > 9) {
        part_model_name = model_name.substring(0, 9);

        if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
          // System.out.println("Eliminating express model: " + model_name);
          return true;
        }
      }

      if (model_name.length() > 6) {
        part_model_name = model_name.substring(0, 6);

        if (part_model_name.equalsIgnoreCase("_JAVA_")) {
          // System.out.println("Eliminating java model: " + model_name);
          return true;
        }
      }

			// why do we even need all the above 

      if (model_name.length() > 16) {
        part_model_name = model_name.substring(model_name.length() - 16);

        // System.out.println("Part model name: " + part_model_name);
        if (!(part_model_name.equalsIgnoreCase("_DICTIONARY_DATA"))) {
          //			System.out.println("Eliminating not dictionary data model: " + model_name + ", part: " + part_model_name);
          return true;
        }
      }

			// allow _MAPPING_DATA
      if (model_name.length() <= 13) {
        // System.out.println("Eliminating short name model: " + model_name);
        return true;
      }

		  return false;
		}

	static void addExpressFiles(Vector express_files, String file_name) {

    try {
      FileReader fr = new FileReader(file_name);
			BufferedReader br =  new BufferedReader(fr);
			for (;;) {
				String current_line = br.readLine();
				if (current_line == null) break;
				current_line = current_line.trim();
				if (current_line.equalsIgnoreCase("")) continue;
				if (current_line.startsWith("#")) continue;
				if (!current_line.endsWith(".exp")) continue;
				express_files.addElement(current_line.replace('/', File.separatorChar).replace('\\', File.separatorChar));
//				express_files.addElement(st.sval.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				
			}

		} catch (FileNotFoundException e) {
      System.out.println("file " + file_name + " not found.");
//      System.out.println("file " + file_name + " caused exception:");
//      System.out.println("" + e.toString());
			
    } catch (IOException e) {
//      System.out.println("file " + file_name + " not found.");
      System.out.println("file " + file_name + " caused exception:");
//      System.out.println("" + e.toString());
			
    }

	}

	static void addExpressFiles_old(Vector express_files, String file_name) {

    try {
      FileInputStream ins = new FileInputStream(file_name);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.wordChars('.', '.');
      st.wordChars(':', ':');
      st.wordChars('/', '/');
      st.wordChars('\\', '\\');
      st.commentChar('#');

//      for (;;) {
      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();
				if (st.ttype == StreamTokenizer.TT_WORD) {
					express_files.addElement(st.sval.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				}		
			}
    } // try

    //  catch (FileNotFoundException e) 
    catch (IOException e) {
      System.out.println("file " + file_name + " not found.");
			
      return;
    }


	}

	static Vector excludeExpressFiles(String file_name) {
		Vector express_files = new Vector();

    try {
      FileReader fr = new FileReader(file_name);
			BufferedReader br =  new BufferedReader(fr);
			for (;;) {
				String current_line = br.readLine();
				if (current_line == null) break;
				current_line = current_line.trim();
				if (current_line.equalsIgnoreCase("")) continue;
				if (current_line.startsWith("#")) continue;
				String dir_path_str = global_express_dir_name;
				if (dir_path_str != null) {
					if (!(dir_path_str.endsWith("/") || dir_path_str.endsWith("\\"))) {
						dir_path_str += File.separator;
					}
					if (flag_relative_exclude) {
						current_line = dir_path_str + current_line;
					}
				}
				express_files.addElement(current_line.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				
			}
    	return express_files;

		} catch (FileNotFoundException e) {
      System.out.println("file " + file_name + " not found.");
//      System.out.println("file " + file_name + " caused exception:");
//      System.out.println("" + e.toString());
			
      return express_files;
    } catch (IOException e) {
//      System.out.println("file " + file_name + " not found.");
      System.out.println("file " + file_name + " caused exception:");
//      System.out.println("" + e.toString());
			
      return express_files;
    }

	}


	static Vector excludeExpressFilesOK(String file_name) {
		Vector express_files = new Vector();

    try {
      FileReader fr = new FileReader(file_name);
			BufferedReader br =  new BufferedReader(fr);
			for (;;) {
				String current_line = br.readLine();
				if (current_line == null) break;
				current_line = current_line.trim();
				if (current_line.equalsIgnoreCase("")) continue;
				if (current_line.startsWith("#")) continue;
				express_files.addElement(current_line.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				
			}
    	return express_files;

		} catch (FileNotFoundException e) {
      System.out.println("file " + file_name + " not found.");
//      System.out.println("file " + file_name + " caused exception:");
//      System.out.println("" + e.toString());
			
      return express_files;
    } catch (IOException e) {
//      System.out.println("file " + file_name + " not found.");
      System.out.println("file " + file_name + " caused exception:");
//      System.out.println("" + e.toString());
			
      return express_files;
    }

	}



	
	static Vector excludeExpressFiles_old(String file_name) {
		Vector express_files = new Vector();

    try {
      FileInputStream ins = new FileInputStream(file_name);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.wordChars('.', '.');
      st.wordChars(':', ':');
      st.wordChars('/', '/');
      st.wordChars('\\', '\\');
      st.commentChar('#');

//      for (;;) {
      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();
				if (st.ttype == StreamTokenizer.TT_WORD) {
					express_files.addElement(st.sval.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				}		
			}
    	return express_files;
    } // try

    //  catch (FileNotFoundException e) 
    catch (IOException e) {
      System.out.println("file " + file_name + " not found.");
			
      return null;
    }


	}


	// this version without recursion
	static void addExpressDirectory(Vector express_files, String directory_path0, String excluded_name) {
		String directory_path = directory_path0.replace('/', File.separatorChar).replace('\\', File.separatorChar); 
		File dir = new File(directory_path);
		String [] files;
		ExpressFilenameFilter ef = new ExpressFilenameFilter();
		if (!(dir.isDirectory())) {
			return;
		}
		files = dir.list(ef);
		Vector excluded = excludeExpressFiles(directory_path + File.separator + excluded_name);
		for (int i = 0; i < files.length; i++) {
			// not needed here, only files returned without path
			// files[i] = files[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
			
			boolean must_exclude = false;
			if (excluded != null) { 
				Iterator iter = excluded.iterator();
				while (iter.hasNext()) {
					String current = (String)iter.next();
// System.out.println("EXEX - current: " + current + ", file: " + files[i]);
					if (current.equalsIgnoreCase(files[i])) {
						must_exclude = true;
						break;
					}
				}
			} // excluded not NULL			
			if (!must_exclude) {
				express_files.addElement(directory_path + File.separator + files[i]);
			}
		}
	}

	// this version with recursion
	static void addExpressDirectoryRecursively(Vector express_files, String directory_path0, Vector excluded, String excluded_name, boolean no_more) {
		String directory_path = directory_path0.replace('/', File.separatorChar).replace('\\', File.separatorChar); 
		File dir = new File(directory_path);
		String [] files;
		ExpressFilenameFilterForRecursive ef = new ExpressFilenameFilterForRecursive();
		if (!(dir.isDirectory())) {
			return;
		}
		files = dir.list(ef);
		if (excluded == null) {
			excluded = excludeExpressFiles(directory_path + File.separator + excluded_name);
		}
		for (int i = 0; i < files.length; i++) {
			// no paths here
			// files[i] = files[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
			String a_file_path = directory_path + File.separator + files[i];
			File a_file = new File(a_file_path);

			if (a_file.isDirectory()) {

				boolean must_exclude = false;
				if (excluded != null) {
					Iterator iter = excluded.iterator();
					while (iter.hasNext()) {
						String current = (String)iter.next();
						// current = current.replace('/', File.separatorChar).replace('\\', File.separatorChar);
						if (current.equalsIgnoreCase(files[i])) {
							must_exclude = true;
							break;
						}
					}
				} // if excluded not NULL
		
				if (flag_stepmod) {
					// if we are in stepmod, we go into data only
					// perhaps not needed, because better to specify the initial directory path ending with data rather than ending with stepmod
					if (directory_path.endsWith("stepmod")) {
						if (!(a_file_path.endsWith("data"))) {
							must_exclude = true;
						}
					} else
					if (directory_path.endsWith("data")) {
						if (flag_arm) {
							// if we are in data, we go into modules only
							if (!(a_file_path.endsWith("modules"))) {
								must_exclude = true;
							}
						} else {
							// if not arm, then go into modules and into resources
							if ((!(a_file_path.endsWith("modules"))) && (!(a_file_path.endsWith("resources")))) {
								must_exclude = true;
							}
						}
					}
				}
				
				if (no_more) {
					must_exclude = true;
				}
				if (!must_exclude) {
					if (a_file_path.endsWith("modules")) {
						no_more = true;
					}
					addExpressDirectoryRecursively(express_files, a_file_path, excluded, excluded_name, no_more); 
				}
			} else {

				boolean must_exclude = false;
				if (excluded != null) {
					Iterator iter = excluded.iterator();
					while (iter.hasNext()) {
						String current = (String)iter.next();
// System.out.println("EXEX - current: " + current + ", file: " + files[i]);
						if (current.equalsIgnoreCase(files[i])) {
							must_exclude = true;
							break;
						}
					}		
				} // if excluded is not NULL
				if (flag_arm) {
					if (flag_stepmod) {
						if (!(files[i].equalsIgnoreCase("arm.exp"))) {
							must_exclude = true;
						}
					} else {
						if (!(files[i].endsWith("arm.exp"))) {
							must_exclude = true;
						}
					}
				} else
				if (flag_mim) {
					if (flag_stepmod) {
						if (no_more) { // this means that we are in the next directory inside modules and not in resources
							if (!(files[i].equalsIgnoreCase("mim.exp"))) {
								must_exclude = true;
							}
						}
					} else {
						// could it even be else? let's treat the same for now
						if (no_more) { // this means that we are in the next directory inside modules and not in resources
							if (!(files[i].equalsIgnoreCase("mim.exp"))) {
								must_exclude = true;
							}
						}
					}
				}
				if (!must_exclude) {
						express_files.addElement(directory_path + File.separator + files[i]);
				}
			}
		}
	}



  // checks if select contains non-entities, at least one non-entity is violation
  static boolean isEntitySelect(ESelect_type st) throws SdaiException {

//System.out.println("isEntitySelect: " + st);
    HashSet all_selects = new HashSet();
    EEntity an_ss;
    EEntity an_ss_super;
    boolean status = true;
//    ANamed_type ant = st.getSelections(null);
//    ANamed_type ant = st.getLocal_selections(null);
    ANamed_type ant = getSelections(st);
    SdaiIterator iant = ant.createIterator();
    all_selects.add(st);

    while (iant.next()) {
      an_ss = (ENamed_type) ant.getCurrentMemberObject(iant);

      // printDDebug("##### " +((ENamed_type)an_ss).getName(null));
      while (an_ss instanceof EDefined_type) {
        EEntity domain = ((EDefined_type) an_ss).getDomain(null);
        an_ss_super = an_ss;
        an_ss = domain;
      }

      if (an_ss instanceof ESelect_type) {



        if (all_selects.add(an_ss)) {
          status = isEntitySelect((ESelect_type) an_ss);

          if (!status) {
//System.out.println("return 1: " + status);
            return status;
          }
        }


      } else if (an_ss instanceof EEntity_definition) {
				 // nothing changed, just in case
				 status = true;
        //            return status;
      } else if (an_ss instanceof EAggregation_type) {
        EEntity ass = an_ss;
        EAggregation_type aas = (EAggregation_type) an_ss;
        an_ss = aas.getElement_type(null);

        for (;;) {
          boolean done_something = false;

          if (an_ss instanceof EDefined_type) {
            ass = an_ss;
            an_ss = ((EDefined_type) an_ss).getDomain(null);
            done_something = true;
          } else if (an_ss instanceof EAggregation_type) {
            ass = an_ss;
            an_ss = ((EAggregation_type) an_ss).getElement_type(null);
            done_something = true;
          }

          if (!done_something) {
            break;
          }
        }

        if (an_ss instanceof ESelect_type) {



          if (all_selects.add(an_ss)) {
            status = isEntitySelect((ESelect_type) an_ss);

            if (!status) {
//System.out.println("return 2: " + status);
              return status;
            }
          }


        } else if (an_ss instanceof EEntity_definition) {
					// nothing
					status = true;

          //            return status;
        } else {
	        status = false;
  	      return status;
        }
      } else {
        status = false;
        return status;
      }
    }
//System.out.println("return 3: " + status);

    return status;
  }


	static boolean isDTDomainValid(EDefined_type dt, EEntity at) {
		return true;
	}



/**
	The alphabet of ISO 8859-1.
*/
	static char [] iso8859_1 = {
		0x00A0,	//	NO-BREAK SPACE
		0x00A1,	//	INVERTED EXCLAMATION MARK
		0x00A2,	//	CENT SIGN
		0x00A3,	//	POUND SIGN
		0x00A4,	//	CURRENCY SIGN
		0x00A5,	//	YEN SIGN
		0x00A6,	//	BROKEN BAR
		0x00A7,	//	SECTION SIGN
		0x00A8,	//	DIAERESIS
		0x00A9,	//	COPYRIGHT SIGN
		0x00AA,	//	FEMININE ORDINAL INDICATOR
		0x00AB,	//	LEFT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x00AC,	//	NOT SIGN
		0x00AD,	//	SOFT HYPHEN
		0x00AE,	//	REGISTERED SIGN
		0x00AF,	//	MACRON
		0x00B0,	//	DEGREE SIGN
		0x00B1,	//	PLUS-MINUS SIGN
		0x00B2,	//	SUPERSCRIPT TWO
		0x00B3,	//	SUPERSCRIPT THREE
		0x00B4,	//	ACUTE ACCENT
		0x00B5,	//	MICRO SIGN
		0x00B6,	//	PILCROW SIGN
		0x00B7,	//	MIDDLE DOT
		0x00B8,	//	CEDILLA
		0x00B9,	//	SUPERSCRIPT ONE
		0x00BA,	//	MASCULINE ORDINAL INDICATOR
		0x00BB,	//	RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x00BC,	//	VULGAR FRACTION ONE QUARTER
		0x00BD,	//	VULGAR FRACTION ONE HALF
		0x00BE,	//	VULGAR FRACTION THREE QUARTERS
		0x00BF,	//	INVERTED QUESTION MARK
		0x00C0,	//	LATIN CAPITAL LETTER A WITH GRAVE
		0x00C1,	//	LATIN CAPITAL LETTER A WITH ACUTE
		0x00C2,	//	LATIN CAPITAL LETTER A WITH CIRCUMFLEX
		0x00C3,	//	LATIN CAPITAL LETTER A WITH TILDE
		0x00C4,	//	LATIN CAPITAL LETTER A WITH DIAERESIS
		0x00C5,	//	LATIN CAPITAL LETTER A WITH RING ABOVE
		0x00C6,	//	LATIN CAPITAL LETTER AE
		0x00C7,	//	LATIN CAPITAL LETTER C WITH CEDILLA
		0x00C8,	//	LATIN CAPITAL LETTER E WITH GRAVE
		0x00C9,	//	LATIN CAPITAL LETTER E WITH ACUTE
		0x00CA,	//	LATIN CAPITAL LETTER E WITH CIRCUMFLEX
		0x00CB,	//	LATIN CAPITAL LETTER E WITH DIAERESIS
		0x00CC,	//	LATIN CAPITAL LETTER I WITH GRAVE
		0x00CD,	//	LATIN CAPITAL LETTER I WITH ACUTE
		0x00CE,	//	LATIN CAPITAL LETTER I WITH CIRCUMFLEX
		0x00CF,	//	LATIN CAPITAL LETTER I WITH DIAERESIS
		0x00D0,	//	LATIN CAPITAL LETTER ETH (Icelandic)
		0x00D1,	//	LATIN CAPITAL LETTER N WITH TILDE
		0x00D2,	//	LATIN CAPITAL LETTER O WITH GRAVE
		0x00D3,	//	LATIN CAPITAL LETTER O WITH ACUTE
		0x00D4,	//	LATIN CAPITAL LETTER O WITH CIRCUMFLEX
		0x00D5,	//	LATIN CAPITAL LETTER O WITH TILDE
		0x00D6,	//	LATIN CAPITAL LETTER O WITH DIAERESIS
		0x00D7,	//	MULTIPLICATION SIGN
		0x00D8,	//	LATIN CAPITAL LETTER O WITH STROKE
		0x00D9,	//	LATIN CAPITAL LETTER U WITH GRAVE
		0x00DA,	//	LATIN CAPITAL LETTER U WITH ACUTE
		0x00DB,	//	LATIN CAPITAL LETTER U WITH CIRCUMFLEX
		0x00DC,	//	LATIN CAPITAL LETTER U WITH DIAERESIS
		0x00DD,	//	LATIN CAPITAL LETTER Y WITH ACUTE
		0x00DE,	//	LATIN CAPITAL LETTER THORN (Icelandic)
		0x00DF,	//	LATIN SMALL LETTER SHARP S (German)
		0x00E0,	//	LATIN SMALL LETTER A WITH GRAVE
		0x00E1,	//	LATIN SMALL LETTER A WITH ACUTE
		0x00E2,	//	LATIN SMALL LETTER A WITH CIRCUMFLEX
		0x00E3,	//	LATIN SMALL LETTER A WITH TILDE
		0x00E4,	//	LATIN SMALL LETTER A WITH DIAERESIS
		0x00E5,	//	LATIN SMALL LETTER A WITH RING ABOVE
		0x00E6,	//	LATIN SMALL LETTER AE
		0x00E7,	//	LATIN SMALL LETTER C WITH CEDILLA
		0x00E8,	//	LATIN SMALL LETTER E WITH GRAVE
		0x00E9,	//	LATIN SMALL LETTER E WITH ACUTE
		0x00EA,	//	LATIN SMALL LETTER E WITH CIRCUMFLEX
		0x00EB,	//	LATIN SMALL LETTER E WITH DIAERESIS
		0x00EC,	//	LATIN SMALL LETTER I WITH GRAVE
		0x00ED,	//	LATIN SMALL LETTER I WITH ACUTE
		0x00EE,	//	LATIN SMALL LETTER I WITH CIRCUMFLEX
		0x00EF,	//	LATIN SMALL LETTER I WITH DIAERESIS
		0x00F0,	//	LATIN SMALL LETTER ETH (Icelandic)
		0x00F1,	//	LATIN SMALL LETTER N WITH TILDE
		0x00F2,	//	LATIN SMALL LETTER O WITH GRAVE
		0x00F3,	//	LATIN SMALL LETTER O WITH ACUTE
		0x00F4,	//	LATIN SMALL LETTER O WITH CIRCUMFLEX
		0x00F5,	//	LATIN SMALL LETTER O WITH TILDE
		0x00F6,	//	LATIN SMALL LETTER O WITH DIAERESIS
		0x00F7,	//	DIVISION SIGN
		0x00F8,	//	LATIN SMALL LETTER O WITH STROKE
		0x00F9,	//	LATIN SMALL LETTER U WITH GRAVE
		0x00FA,	//	LATIN SMALL LETTER U WITH ACUTE
		0x00FB,	//	LATIN SMALL LETTER U WITH CIRCUMFLEX
		0x00FC,	//	LATIN SMALL LETTER U WITH DIAERESIS
		0x00FD,	//	LATIN SMALL LETTER Y WITH ACUTE
		0x00FE,	//	LATIN SMALL LETTER THORN (Icelandic)
		0x00FF	//	LATIN SMALL LETTER Y WITH DIAERESIS
	};

/**
	The alphabet of ISO 8859-2.
*/
	static char [] iso8859_2 = {
		0x00A0,	//	NO-BREAK SPACE
		0x0104,	//	LATIN CAPITAL LETTER A WITH OGONEK
		0x02D8,	//	BREVE
		0x0141,	//	LATIN CAPITAL LETTER L WITH STROKE
		0x00A4,	//	CURRENCY SIGN
		0x013D,	//	LATIN CAPITAL LETTER L WITH CARON
		0x015A,	//	LATIN CAPITAL LETTER S WITH ACUTE
		0x00A7,	//	SECTION SIGN
		0x00A8,	//	DIAERESIS
		0x0160,	//	LATIN CAPITAL LETTER S WITH CARON
		0x015E,	//	LATIN CAPITAL LETTER S WITH CEDILLA
		0x0164,	//	LATIN CAPITAL LETTER T WITH CARON
		0x0179,	//	LATIN CAPITAL LETTER Z WITH ACUTE
		0x00AD,	//	SOFT HYPHEN
		0x017D,	//	LATIN CAPITAL LETTER Z WITH CARON
		0x017B,	//	LATIN CAPITAL LETTER Z WITH DOT ABOVE
		0x00B0,	//	DEGREE SIGN
		0x0105,	//	LATIN SMALL LETTER A WITH OGONEK
		0x02DB,	//	OGONEK
		0x0142,	//	LATIN SMALL LETTER L WITH STROKE
		0x00B4,	//	ACUTE ACCENT
		0x013E,	//	LATIN SMALL LETTER L WITH CARON
		0x015B,	//	LATIN SMALL LETTER S WITH ACUTE
		0x02C7,	//	CARON
		0x00B8,	//	CEDILLA
		0x0161,	//	LATIN SMALL LETTER S WITH CARON
		0x015F,	//	LATIN SMALL LETTER S WITH CEDILLA
		0x0165,	//	LATIN SMALL LETTER T WITH CARON
		0x017A,	//	LATIN SMALL LETTER Z WITH ACUTE
		0x02DD,	//	DOUBLE ACUTE ACCENT
		0x017E,	//	LATIN SMALL LETTER Z WITH CARON
		0x017C,	//	LATIN SMALL LETTER Z WITH DOT ABOVE
		0x0154,	//	LATIN CAPITAL LETTER R WITH ACUTE
		0x00C1,	//	LATIN CAPITAL LETTER A WITH ACUTE
		0x00C2,	//	LATIN CAPITAL LETTER A WITH CIRCUMFLEX
		0x0102,	//	LATIN CAPITAL LETTER A WITH BREVE
		0x00C4,	//	LATIN CAPITAL LETTER A WITH DIAERESIS
		0x0139,	//	LATIN CAPITAL LETTER L WITH ACUTE
		0x0106,	//	LATIN CAPITAL LETTER C WITH ACUTE
		0x00C7,	//	LATIN CAPITAL LETTER C WITH CEDILLA
		0x010C,	//	LATIN CAPITAL LETTER C WITH CARON
		0x00C9,	//	LATIN CAPITAL LETTER E WITH ACUTE
		0x0118,	//	LATIN CAPITAL LETTER E WITH OGONEK
		0x00CB,	//	LATIN CAPITAL LETTER E WITH DIAERESIS
		0x011A,	//	LATIN CAPITAL LETTER E WITH CARON
		0x00CD,	//	LATIN CAPITAL LETTER I WITH ACUTE
		0x00CE,	//	LATIN CAPITAL LETTER I WITH CIRCUMFLEX
		0x010E,	//	LATIN CAPITAL LETTER D WITH CARON
		0x0110,	//	LATIN CAPITAL LETTER D WITH STROKE
		0x0143,	//	LATIN CAPITAL LETTER N WITH ACUTE
		0x0147,	//	LATIN CAPITAL LETTER N WITH CARON
		0x00D3,	//	LATIN CAPITAL LETTER O WITH ACUTE
		0x00D4,	//	LATIN CAPITAL LETTER O WITH CIRCUMFLEX
		0x0150,	//	LATIN CAPITAL LETTER O WITH DOUBLE ACUTE
		0x00D6,	//	LATIN CAPITAL LETTER O WITH DIAERESIS
		0x00D7,	//	MULTIPLICATION SIGN
		0x0158,	//	LATIN CAPITAL LETTER R WITH CARON
		0x016E,	//	LATIN CAPITAL LETTER U WITH RING ABOVE
		0x00DA,	//	LATIN CAPITAL LETTER U WITH ACUTE
		0x0170,	//	LATIN CAPITAL LETTER U WITH DOUBLE ACUTE
		0x00DC,	//	LATIN CAPITAL LETTER U WITH DIAERESIS
		0x00DD,	//	LATIN CAPITAL LETTER Y WITH ACUTE
		0x0162,	//	LATIN CAPITAL LETTER T WITH CEDILLA
		0x00DF,	//	LATIN SMALL LETTER SHARP S
		0x0155,	//	LATIN SMALL LETTER R WITH ACUTE
		0x00E1,	//	LATIN SMALL LETTER A WITH ACUTE
		0x00E2,	//	LATIN SMALL LETTER A WITH CIRCUMFLEX
		0x0103,	//	LATIN SMALL LETTER A WITH BREVE
		0x00E4,	//	LATIN SMALL LETTER A WITH DIAERESIS
		0x013A,	//	LATIN SMALL LETTER L WITH ACUTE
		0x0107,	//	LATIN SMALL LETTER C WITH ACUTE
		0x00E7,	//	LATIN SMALL LETTER C WITH CEDILLA
		0x010D,	//	LATIN SMALL LETTER C WITH CARON
		0x00E9,	//	LATIN SMALL LETTER E WITH ACUTE
		0x0119,	//	LATIN SMALL LETTER E WITH OGONEK
		0x00EB,	//	LATIN SMALL LETTER E WITH DIAERESIS
		0x011B,	//	LATIN SMALL LETTER E WITH CARON
		0x00ED,	//	LATIN SMALL LETTER I WITH ACUTE
		0x00EE,	//	LATIN SMALL LETTER I WITH CIRCUMFLEX
		0x010F,	//	LATIN SMALL LETTER D WITH CARON
		0x0111,	//	LATIN SMALL LETTER D WITH STROKE
		0x0144,	//	LATIN SMALL LETTER N WITH ACUTE
		0x0148,	//	LATIN SMALL LETTER N WITH CARON
		0x00F3,	//	LATIN SMALL LETTER O WITH ACUTE
		0x00F4,	//	LATIN SMALL LETTER O WITH CIRCUMFLEX
		0x0151,	//	LATIN SMALL LETTER O WITH DOUBLE ACUTE
		0x00F6,	//	LATIN SMALL LETTER O WITH DIAERESIS
		0x00F7,	//	DIVISION SIGN
		0x0159,	//	LATIN SMALL LETTER R WITH CARON
		0x016F,	//	LATIN SMALL LETTER U WITH RING ABOVE
		0x00FA,	//	LATIN SMALL LETTER U WITH ACUTE
		0x0171,	//	LATIN SMALL LETTER U WITH DOUBLE ACUTE
		0x00FC,	//	LATIN SMALL LETTER U WITH DIAERESIS
		0x00FD,	//	LATIN SMALL LETTER Y WITH ACUTE
		0x0163,	//	LATIN SMALL LETTER T WITH CEDILLA
		0x02D9	//	DOT ABOVE
	};

/**
	The alphabet of ISO 8859-3.
*/
	static char [] iso8859_3 = {
		0x00A0,	//	NO-BREAK SPACE
		0x0126,	//	LATIN CAPITAL LETTER H WITH STROKE
		0x02D8,	//	BREVE
		0x00A3,	//	POUND SIGN
		0x00A4,	//	CURRENCY SIGN
		0x0000,	//	DOES NOT EXIST
		0x0124,	//	LATIN CAPITAL LETTER H WITH CIRCUMFLEX
		0x00A7,	//	SECTION SIGN
		0x00A8,	//	DIAERESIS
		0x0130,	//	LATIN CAPITAL LETTER I WITH DOT ABOVE
		0x015E,	//	LATIN CAPITAL LETTER S WITH CEDILLA
		0x011E,	//	LATIN CAPITAL LETTER G WITH BREVE
		0x0134,	//	LATIN CAPITAL LETTER J WITH CIRCUMFLEX
		0x00AD,	//	SOFT HYPHEN
		0x0000,	//	DOES NOT EXIST
		0x017B,	//	LATIN CAPITAL LETTER Z WITH DOT ABOVE
		0x00B0,	//	DEGREE SIGN
		0x0127,	//	LATIN SMALL LETTER H WITH STROKE
		0x00B2,	//	SUPERSCRIPT TWO
		0x00B3,	//	SUPERSCRIPT THREE
		0x00B4,	//	ACUTE ACCENT
		0x00B5,	//	MICRO SIGN
		0x0125,	//	LATIN SMALL LETTER H WITH CIRCUMFLEX
		0x00B7,	//	MIDDLE DOT
		0x00B8,	//	CEDILLA
		0x0131,	//	LATIN SMALL LETTER DOTLESS I
		0x015F,	//	LATIN SMALL LETTER S WITH CEDILLA
		0x011F,	//	LATIN SMALL LETTER G WITH BREVE
		0x0135,	//	LATIN SMALL LETTER J WITH CIRCUMFLEX
		0x00BD,	//	VULGAR FRACTION ONE HALF
		0x0000,	//	DOES NOT EXIST
		0x017C,	//	LATIN SMALL LETTER Z WITH DOT ABOVE
		0x00C0,	//	LATIN CAPITAL LETTER A WITH GRAVE
		0x00C1,	//	LATIN CAPITAL LETTER A WITH ACUTE
		0x00C2,	//	LATIN CAPITAL LETTER A WITH CIRCUMFLEX
		0x0000,	//	DOES NOT EXIST
		0x00C4,	//	LATIN CAPITAL LETTER A WITH DIAERESIS
		0x010A,	//	LATIN CAPITAL LETTER C WITH DOT ABOVE
		0x0108,	//	LATIN CAPITAL LETTER C WITH CIRCUMFLEX
		0x00C7,	//	LATIN CAPITAL LETTER C WITH CEDILLA
		0x00C8,	//	LATIN CAPITAL LETTER E WITH GRAVE
		0x00C9,	//	LATIN CAPITAL LETTER E WITH ACUTE
		0x00CA,	//	LATIN CAPITAL LETTER E WITH CIRCUMFLEX
		0x00CB,	//	LATIN CAPITAL LETTER E WITH DIAERESIS
		0x00CC,	//	LATIN CAPITAL LETTER I WITH GRAVE
		0x00CD,	//	LATIN CAPITAL LETTER I WITH ACUTE
		0x00CE,	//	LATIN CAPITAL LETTER I WITH CIRCUMFLEX
		0x00CF,	//	LATIN CAPITAL LETTER I WITH DIAERESIS
		0x0000,	//	DOES NOT EXIST
		0x00D1,	//	LATIN CAPITAL LETTER N WITH TILDE
		0x00D2,	//	LATIN CAPITAL LETTER O WITH GRAVE
		0x00D3,	//	LATIN CAPITAL LETTER O WITH ACUTE
		0x00D4,	//	LATIN CAPITAL LETTER O WITH CIRCUMFLEX
		0x0120,	//	LATIN CAPITAL LETTER G WITH DOT ABOVE
		0x00D6,	//	LATIN CAPITAL LETTER O WITH DIAERESIS
		0x00D7,	//	MULTIPLICATION SIGN
		0x011C,	//	LATIN CAPITAL LETTER G WITH CIRCUMFLEX
		0x00D9,	//	LATIN CAPITAL LETTER U WITH GRAVE
		0x00DA,	//	LATIN CAPITAL LETTER U WITH ACUTE
		0x00DB,	//	LATIN CAPITAL LETTER U WITH CIRCUMFLEX
		0x00DC,	//	LATIN CAPITAL LETTER U WITH DIAERESIS
		0x016C,	//	LATIN CAPITAL LETTER U WITH BREVE
		0x015C,	//	LATIN CAPITAL LETTER S WITH CIRCUMFLEX
		0x00DF,	//	LATIN SMALL LETTER SHARP S
		0x00E0,	//	LATIN SMALL LETTER A WITH GRAVE
		0x00E1,	//	LATIN SMALL LETTER A WITH ACUTE
		0x00E2,	//	LATIN SMALL LETTER A WITH CIRCUMFLEX
		0x0000,	//	DOES NOT EXIST
		0x00E4,	//	LATIN SMALL LETTER A WITH DIAERESIS
		0x010B,	//	LATIN SMALL LETTER C WITH DOT ABOVE
		0x0109,	//	LATIN SMALL LETTER C WITH CIRCUMFLEX
		0x00E7,	//	LATIN SMALL LETTER C WITH CEDILLA
		0x00E8,	//	LATIN SMALL LETTER E WITH GRAVE
		0x00E9,	//	LATIN SMALL LETTER E WITH ACUTE
		0x00EA,	//	LATIN SMALL LETTER E WITH CIRCUMFLEX
		0x00EB,	//	LATIN SMALL LETTER E WITH DIAERESIS
		0x00EC,	//	LATIN SMALL LETTER I WITH GRAVE
		0x00ED,	//	LATIN SMALL LETTER I WITH ACUTE
		0x00EE,	//	LATIN SMALL LETTER I WITH CIRCUMFLEX
		0x00EF,	//	LATIN SMALL LETTER I WITH DIAERESIS
		0x0000,	//	DOES NOT EXIST
		0x00F1,	//	LATIN SMALL LETTER N WITH TILDE
		0x00F2,	//	LATIN SMALL LETTER O WITH GRAVE
		0x00F3,	//	LATIN SMALL LETTER O WITH ACUTE
		0x00F4,	//	LATIN SMALL LETTER O WITH CIRCUMFLEX
		0x0121,	//	LATIN SMALL LETTER G WITH DOT ABOVE
		0x00F6,	//	LATIN SMALL LETTER O WITH DIAERESIS
		0x00F7,	//	DIVISION SIGN
		0x011D,	//	LATIN SMALL LETTER G WITH CIRCUMFLEX
		0x00F9,	//	LATIN SMALL LETTER U WITH GRAVE
		0x00FA,	//	LATIN SMALL LETTER U WITH ACUTE
		0x00FB,	//	LATIN SMALL LETTER U WITH CIRCUMFLEX
		0x00FC,	//	LATIN SMALL LETTER U WITH DIAERESIS
		0x016D,	//	LATIN SMALL LETTER U WITH BREVE
		0x015D,	//	LATIN SMALL LETTER S WITH CIRCUMFLEX
		0x02D9	//	DOT ABOVE
	};

/**
	The alphabet of ISO 8859-4.
*/
	static char [] iso8859_4 = {
		0x00A0,	//	NO-BREAK SPACE
		0x0104,	//	LATIN CAPITAL LETTER A WITH OGONEK
		0x0138,	//	LATIN SMALL LETTER KRA
		0x0156,	//	LATIN CAPITAL LETTER R WITH CEDILLA
		0x00A4,	//	CURRENCY SIGN
		0x0128,	//	LATIN CAPITAL LETTER I WITH TILDE
		0x013B,	//	LATIN CAPITAL LETTER L WITH CEDILLA
		0x00A7,	//	SECTION SIGN
		0x00A8,	//	DIAERESIS
		0x0160,	//	LATIN CAPITAL LETTER S WITH CARON
		0x0112,	//	LATIN CAPITAL LETTER E WITH MACRON
		0x0122,	//	LATIN CAPITAL LETTER G WITH CEDILLA
		0x0166,	//	LATIN CAPITAL LETTER T WITH STROKE
		0x00AD,	//	SOFT HYPHEN
		0x017D,	//	LATIN CAPITAL LETTER Z WITH CARON
		0x00AF,	//	MACRON
		0x00B0,	//	DEGREE SIGN
		0x0105,	//	LATIN SMALL LETTER A WITH OGONEK
		0x02DB,	//	OGONEK
		0x0157,	//	LATIN SMALL LETTER R WITH CEDILLA
		0x00B4,	//	ACUTE ACCENT
		0x0129,	//	LATIN SMALL LETTER I WITH TILDE
		0x013C,	//	LATIN SMALL LETTER L WITH CEDILLA
		0x02C7,	//	CARON
		0x00B8,	//	CEDILLA
		0x0161,	//	LATIN SMALL LETTER S WITH CARON
		0x0113,	//	LATIN SMALL LETTER E WITH MACRON
		0x0123,	//	LATIN SMALL LETTER G WITH CEDILLA
		0x0167,	//	LATIN SMALL LETTER T WITH STROKE
		0x014A,	//	LATIN CAPITAL LETTER ENG
		0x017E,	//	LATIN SMALL LETTER Z WITH CARON
		0x014B,	//	LATIN SMALL LETTER ENG
		0x0100,	//	LATIN CAPITAL LETTER A WITH MACRON
		0x00C1,	//	LATIN CAPITAL LETTER A WITH ACUTE
		0x00C2,	//	LATIN CAPITAL LETTER A WITH CIRCUMFLEX
		0x00C3,	//	LATIN CAPITAL LETTER A WITH TILDE
		0x00C4,	//	LATIN CAPITAL LETTER A WITH DIAERESIS
		0x00C5,	//	LATIN CAPITAL LETTER A WITH RING ABOVE
		0x00C6,	//	LATIN CAPITAL LETTER AE
		0x012E,	//	LATIN CAPITAL LETTER I WITH OGONEK
		0x010C,	//	LATIN CAPITAL LETTER C WITH CARON
		0x00C9,	//	LATIN CAPITAL LETTER E WITH ACUTE
		0x0118,	//	LATIN CAPITAL LETTER E WITH OGONEK
		0x00CB,	//	LATIN CAPITAL LETTER E WITH DIAERESIS
		0x0116,	//	LATIN CAPITAL LETTER E WITH DOT ABOVE
		0x00CD,	//	LATIN CAPITAL LETTER I WITH ACUTE
		0x00CE,	//	LATIN CAPITAL LETTER I WITH CIRCUMFLEX
		0x012A,	//	LATIN CAPITAL LETTER I WITH MACRON
		0x0110,	//	LATIN CAPITAL LETTER D WITH STROKE
		0x0145,	//	LATIN CAPITAL LETTER N WITH CEDILLA
		0x014C,	//	LATIN CAPITAL LETTER O WITH MACRON
		0x0136,	//	LATIN CAPITAL LETTER K WITH CEDILLA
		0x00D4,	//	LATIN CAPITAL LETTER O WITH CIRCUMFLEX
		0x00D5,	//	LATIN CAPITAL LETTER O WITH TILDE
		0x00D6,	//	LATIN CAPITAL LETTER O WITH DIAERESIS
		0x00D7,	//	MULTIPLICATION SIGN
		0x00D8,	//	LATIN CAPITAL LETTER O WITH STROKE
		0x0172,	//	LATIN CAPITAL LETTER U WITH OGONEK
		0x00DA,	//	LATIN CAPITAL LETTER U WITH ACUTE
		0x00DB,	//	LATIN CAPITAL LETTER U WITH CIRCUMFLEX
		0x00DC,	//	LATIN CAPITAL LETTER U WITH DIAERESIS
		0x0168,	//	LATIN CAPITAL LETTER U WITH TILDE
		0x016A,	//	LATIN CAPITAL LETTER U WITH MACRON
		0x00DF,	//	LATIN SMALL LETTER SHARP S
		0x0101,	//	LATIN SMALL LETTER A WITH MACRON
		0x00E1,	//	LATIN SMALL LETTER A WITH ACUTE
		0x00E2,	//	LATIN SMALL LETTER A WITH CIRCUMFLEX
		0x00E3,	//	LATIN SMALL LETTER A WITH TILDE
		0x00E4,	//	LATIN SMALL LETTER A WITH DIAERESIS
		0x00E5,	//	LATIN SMALL LETTER A WITH RING ABOVE
		0x00E6,	//	LATIN SMALL LETTER AE
		0x012F,	//	LATIN SMALL LETTER I WITH OGONEK
		0x010D,	//	LATIN SMALL LETTER C WITH CARON
		0x00E9,	//	LATIN SMALL LETTER E WITH ACUTE
		0x0119,	//	LATIN SMALL LETTER E WITH OGONEK
		0x00EB,	//	LATIN SMALL LETTER E WITH DIAERESIS
		0x0117,	//	LATIN SMALL LETTER E WITH DOT ABOVE
		0x00ED,	//	LATIN SMALL LETTER I WITH ACUTE
		0x00EE,	//	LATIN SMALL LETTER I WITH CIRCUMFLEX
		0x012B,	//	LATIN SMALL LETTER I WITH MACRON
		0x0111,	//	LATIN SMALL LETTER D WITH STROKE
		0x0146,	//	LATIN SMALL LETTER N WITH CEDILLA
		0x014D,	//	LATIN SMALL LETTER O WITH MACRON
		0x0137,	//	LATIN SMALL LETTER K WITH CEDILLA
		0x00F4,	//	LATIN SMALL LETTER O WITH CIRCUMFLEX
		0x00F5,	//	LATIN SMALL LETTER O WITH TILDE
		0x00F6,	//	LATIN SMALL LETTER O WITH DIAERESIS
		0x00F7,	//	DIVISION SIGN
		0x00F8,	//	LATIN SMALL LETTER O WITH STROKE
		0x0173,	//	LATIN SMALL LETTER U WITH OGONEK
		0x00FA,	//	LATIN SMALL LETTER U WITH ACUTE
		0x00FB,	//	LATIN SMALL LETTER U WITH CIRCUMFLEX
		0x00FC,	//	LATIN SMALL LETTER U WITH DIAERESIS
		0x0169,	//	LATIN SMALL LETTER U WITH TILDE
		0x016B,	//	LATIN SMALL LETTER U WITH MACRON
		0x02D9,	//	DOT ABOVE
	};

/**
	The alphabet of ISO 8859-5.
*/
	static char [] iso8859_5 = {
		0x00A0,	//	NO-BREAK SPACE
		0x0401,	//	CYRILLIC CAPITAL LETTER IO
		0x0402,	//	CYRILLIC CAPITAL LETTER DJE
		0x0403,	//	CYRILLIC CAPITAL LETTER GJE
		0x0404,	//	CYRILLIC CAPITAL LETTER UKRAINIAN IE
		0x0405,	//	CYRILLIC CAPITAL LETTER DZE
		0x0406,	//	CYRILLIC CAPITAL LETTER BYELORUSSIAN-UKRAINIAN I
		0x0407,	//	CYRILLIC CAPITAL LETTER YI
		0x0408,	//	CYRILLIC CAPITAL LETTER JE
		0x0409,	//	CYRILLIC CAPITAL LETTER LJE
		0x040A,	//	CYRILLIC CAPITAL LETTER NJE
		0x040B,	//	CYRILLIC CAPITAL LETTER TSHE
		0x040C,	//	CYRILLIC CAPITAL LETTER KJE
		0x00AD,	//	SOFT HYPHEN
		0x040E,	//	CYRILLIC CAPITAL LETTER SHORT U
		0x040F,	//	CYRILLIC CAPITAL LETTER DZHE
		0x0410,	//	CYRILLIC CAPITAL LETTER A
		0x0411,	//	CYRILLIC CAPITAL LETTER BE
		0x0412,	//	CYRILLIC CAPITAL LETTER VE
		0x0413,	//	CYRILLIC CAPITAL LETTER GHE
		0x0414,	//	CYRILLIC CAPITAL LETTER DE
		0x0415,	//	CYRILLIC CAPITAL LETTER IE
		0x0416,	//	CYRILLIC CAPITAL LETTER ZHE
		0x0417,	//	CYRILLIC CAPITAL LETTER ZE
		0x0418,	//	CYRILLIC CAPITAL LETTER I
		0x0419,	//	CYRILLIC CAPITAL LETTER SHORT I
		0x041A,	//	CYRILLIC CAPITAL LETTER KA
		0x041B,	//	CYRILLIC CAPITAL LETTER EL
		0x041C,	//	CYRILLIC CAPITAL LETTER EM
		0x041D,	//	CYRILLIC CAPITAL LETTER EN
		0x041E,	//	CYRILLIC CAPITAL LETTER O
		0x041F,	//	CYRILLIC CAPITAL LETTER PE
		0x0420,	//	CYRILLIC CAPITAL LETTER ER
		0x0421,	//	CYRILLIC CAPITAL LETTER ES
		0x0422,	//	CYRILLIC CAPITAL LETTER TE
		0x0423,	//	CYRILLIC CAPITAL LETTER U
		0x0424,	//	CYRILLIC CAPITAL LETTER EF
		0x0425,	//	CYRILLIC CAPITAL LETTER HA
		0x0426,	//	CYRILLIC CAPITAL LETTER TSE
		0x0427,	//	CYRILLIC CAPITAL LETTER CHE
		0x0428,	//	CYRILLIC CAPITAL LETTER SHA
		0x0429,	//	CYRILLIC CAPITAL LETTER SHCHA
		0x042A,	//	CYRILLIC CAPITAL LETTER HARD SIGN
		0x042B,	//	CYRILLIC CAPITAL LETTER YERU
		0x042C,	//	CYRILLIC CAPITAL LETTER SOFT SIGN
		0x042D,	//	CYRILLIC CAPITAL LETTER E
		0x042E,	//	CYRILLIC CAPITAL LETTER YU
		0x042F,	//	CYRILLIC CAPITAL LETTER YA
		0x0430,	//	CYRILLIC SMALL LETTER A
		0x0431,	//	CYRILLIC SMALL LETTER BE
		0x0432,	//	CYRILLIC SMALL LETTER VE
		0x0433,	//	CYRILLIC SMALL LETTER GHE
		0x0434,	//	CYRILLIC SMALL LETTER DE
		0x0435,	//	CYRILLIC SMALL LETTER IE
		0x0436,	//	CYRILLIC SMALL LETTER ZHE
		0x0437,	//	CYRILLIC SMALL LETTER ZE
		0x0438,	//	CYRILLIC SMALL LETTER I
		0x0439,	//	CYRILLIC SMALL LETTER SHORT I
		0x043A,	//	CYRILLIC SMALL LETTER KA
		0x043B,	//	CYRILLIC SMALL LETTER EL
		0x043C,	//	CYRILLIC SMALL LETTER EM
		0x043D,	//	CYRILLIC SMALL LETTER EN
		0x043E,	//	CYRILLIC SMALL LETTER O
		0x043F,	//	CYRILLIC SMALL LETTER PE
		0x0440,	//	CYRILLIC SMALL LETTER ER
		0x0441,	//	CYRILLIC SMALL LETTER ES
		0x0442,	//	CYRILLIC SMALL LETTER TE
		0x0443,	//	CYRILLIC SMALL LETTER U
		0x0444,	//	CYRILLIC SMALL LETTER EF
		0x0445,	//	CYRILLIC SMALL LETTER HA
		0x0446,	//	CYRILLIC SMALL LETTER TSE
		0x0447,	//	CYRILLIC SMALL LETTER CHE
		0x0448,	//	CYRILLIC SMALL LETTER SHA
		0x0449,	//	CYRILLIC SMALL LETTER SHCHA
		0x044A,	//	CYRILLIC SMALL LETTER HARD SIGN
		0x044B,	//	CYRILLIC SMALL LETTER YERU
		0x044C,	//	CYRILLIC SMALL LETTER SOFT SIGN
		0x044D,	//	CYRILLIC SMALL LETTER E
		0x044E,	//	CYRILLIC SMALL LETTER YU
		0x044F,	//	CYRILLIC SMALL LETTER YA
		0x2116,	//	NUMERO SIGN
		0x0451,	//	CYRILLIC SMALL LETTER IO
		0x0452,	//	CYRILLIC SMALL LETTER DJE
		0x0453,	//	CYRILLIC SMALL LETTER GJE
		0x0454,	//	CYRILLIC SMALL LETTER UKRAINIAN IE
		0x0455,	//	CYRILLIC SMALL LETTER DZE
		0x0456,	//	CYRILLIC SMALL LETTER BYELORUSSIAN-UKRAINIAN I
		0x0457,	//	CYRILLIC SMALL LETTER YI
		0x0458,	//	CYRILLIC SMALL LETTER JE
		0x0459,	//	CYRILLIC SMALL LETTER LJE
		0x045A,	//	CYRILLIC SMALL LETTER NJE
		0x045B,	//	CYRILLIC SMALL LETTER TSHE
		0x045C,	//	CYRILLIC SMALL LETTER KJE
		0x00A7,	//	SECTION SIGN
		0x045E,	//	CYRILLIC SMALL LETTER SHORT U
		0x045F	//	CYRILLIC SMALL LETTER DZHE
	};

/**
	The alphabet of ISO 8859-6.
*/
	static char [] iso8859_6 = {
		0x00A0,	//	NO-BREAK SPACE
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x00A4,	//	CURRENCY SIGN
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x060C,	//	ARABIC COMMA
		0x00AD,	//	SOFT HYPHEN
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x061B,	//	ARABIC SEMICOLON
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x061F,	//	ARABIC QUESTION MARK
		0x0000,	//	DOES NOT EXIST
		0x0621,	//	ARABIC LETTER HAMZA
		0x0622,	//	ARABIC LETTER ALEF WITH MADDA ABOVE
		0x0623,	//	ARABIC LETTER ALEF WITH HAMZA ABOVE
		0x0624,	//	ARABIC LETTER WAW WITH HAMZA ABOVE
		0x0625,	//	ARABIC LETTER ALEF WITH HAMZA BELOW
		0x0626,	//	ARABIC LETTER YEH WITH HAMZA ABOVE
		0x0627,	//	ARABIC LETTER ALEF
		0x0628,	//	ARABIC LETTER BEH
		0x0629,	//	ARABIC LETTER TEH MARBUTA
		0x062A,	//	ARABIC LETTER TEH
		0x062B,	//	ARABIC LETTER THEH
		0x062C,	//	ARABIC LETTER JEEM
		0x062D,	//	ARABIC LETTER HAH
		0x062E,	//	ARABIC LETTER KHAH
		0x062F,	//	ARABIC LETTER DAL
		0x0630,	//	ARABIC LETTER THAL
		0x0631,	//	ARABIC LETTER REH
		0x0632,	//	ARABIC LETTER ZAIN
		0x0633,	//	ARABIC LETTER SEEN
		0x0634,	//	ARABIC LETTER SHEEN
		0x0635,	//	ARABIC LETTER SAD
		0x0636,	//	ARABIC LETTER DAD
		0x0637,	//	ARABIC LETTER TAH
		0x0638,	//	ARABIC LETTER ZAH
		0x0639,	//	ARABIC LETTER AIN
		0x063A,	//	ARABIC LETTER GHAIN
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0640,	//	ARABIC TATWEEL
		0x0641,	//	ARABIC LETTER FEH
		0x0642,	//	ARABIC LETTER QAF
		0x0643,	//	ARABIC LETTER KAF
		0x0644,	//	ARABIC LETTER LAM
		0x0645,	//	ARABIC LETTER MEEM
		0x0646,	//	ARABIC LETTER NOON
		0x0647,	//	ARABIC LETTER HEH
		0x0648,	//	ARABIC LETTER WAW
		0x0649,	//	ARABIC LETTER ALEF MAKSURA
		0x064A,	//	ARABIC LETTER YEH
		0x064B,	//	ARABIC FATHATAN
		0x064C,	//	ARABIC DAMMATAN
		0x064D,	//	ARABIC KASRATAN
		0x064E,	//	ARABIC FATHA
		0x064F,	//	ARABIC DAMMA
		0x0650,	//	ARABIC KASRA
		0x0651,	//	ARABIC SHADDA
		0x0652,	//	ARABIC SUKUN
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000	//	DOES NOT EXIST
	};

/**
	The alphabet of ISO 8859-7.
*/
	static char [] iso8859_7 = {
		0x00A0,	//	NO-BREAK SPACE
		0x2018,	//	LEFT SINGLE QUOTATION MARK
		0x2019,	//	RIGHT SINGLE QUOTATION MARK
		0x00A3,	//	POUND SIGN
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x00A6,	//	BROKEN BAR
		0x00A7,	//	SECTION SIGN
		0x00A8,	//	DIAERESIS
		0x00A9,	//	COPYRIGHT SIGN
		0x0000,	//	DOES NOT EXIST
		0x00AB,	//	LEFT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x00AC,	//	NOT SIGN
		0x00AD,	//	SOFT HYPHEN
		0x0000,	//	DOES NOT EXIST
		0x2015,	//	HORIZONTAL BAR
		0x00B0,	//	DEGREE SIGN
		0x00B1,	//	PLUS-MINUS SIGN
		0x00B2,	//	SUPERSCRIPT TWO
		0x00B3,	//	SUPERSCRIPT THREE
		0x0384,	//	GREEK TONOS
		0x0385,	//	GREEK DIALYTIKA TONOS
		0x0386,	//	GREEK CAPITAL LETTER ALPHA WITH TONOS
		0x00B7,	//	MIDDLE DOT
		0x0388,	//	GREEK CAPITAL LETTER EPSILON WITH TONOS
		0x0389,	//	GREEK CAPITAL LETTER ETA WITH TONOS
		0x038A,	//	GREEK CAPITAL LETTER IOTA WITH TONOS
		0x00BB,	//	RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x038C,	//	GREEK CAPITAL LETTER OMICRON WITH TONOS
		0x00BD,	//	VULGAR FRACTION ONE HALF
		0x038E,	//	GREEK CAPITAL LETTER UPSILON WITH TONOS
		0x038F,	//	GREEK CAPITAL LETTER OMEGA WITH TONOS
		0x0390,	//	GREEK SMALL LETTER IOTA WITH DIALYTIKA AND TONOS
		0x0391,	//	GREEK CAPITAL LETTER ALPHA
		0x0392,	//	GREEK CAPITAL LETTER BETA
		0x0393,	//	GREEK CAPITAL LETTER GAMMA
		0x0394,	//	GREEK CAPITAL LETTER DELTA
		0x0395,	//	GREEK CAPITAL LETTER EPSILON
		0x0396,	//	GREEK CAPITAL LETTER ZETA
		0x0397,	//	GREEK CAPITAL LETTER ETA
		0x0398,	//	GREEK CAPITAL LETTER THETA
		0x0399,	//	GREEK CAPITAL LETTER IOTA
		0x039A,	//	GREEK CAPITAL LETTER KAPPA
		0x039B,	//	GREEK CAPITAL LETTER LAMDA
		0x039C,	//	GREEK CAPITAL LETTER MU
		0x039D,	//	GREEK CAPITAL LETTER NU
		0x039E,	//	GREEK CAPITAL LETTER XI
		0x039F,	//	GREEK CAPITAL LETTER OMICRON
		0x03A0,	//	GREEK CAPITAL LETTER PI
		0x03A1,	//	GREEK CAPITAL LETTER RHO
		0x0000,	//	DOES NOT EXIST
		0x03A3,	//	GREEK CAPITAL LETTER SIGMA
		0x03A4,	//	GREEK CAPITAL LETTER TAU
		0x03A5,	//	GREEK CAPITAL LETTER UPSILON
		0x03A6,	//	GREEK CAPITAL LETTER PHI
		0x03A7,	//	GREEK CAPITAL LETTER CHI
		0x03A8,	//	GREEK CAPITAL LETTER PSI
		0x03A9,	//	GREEK CAPITAL LETTER OMEGA
		0x03AA,	//	GREEK CAPITAL LETTER IOTA WITH DIALYTIKA
		0x03AB,	//	GREEK CAPITAL LETTER UPSILON WITH DIALYTIKA
		0x03AC,	//	GREEK SMALL LETTER ALPHA WITH TONOS
		0x03AD,	//	GREEK SMALL LETTER EPSILON WITH TONOS
		0x03AE,	//	GREEK SMALL LETTER ETA WITH TONOS
		0x03AF,	//	GREEK SMALL LETTER IOTA WITH TONOS
		0x03B0,	//	GREEK SMALL LETTER UPSILON WITH DIALYTIKA AND TONOS
		0x03B1,	//	GREEK SMALL LETTER ALPHA
		0x03B2,	//	GREEK SMALL LETTER BETA
		0x03B3,	//	GREEK SMALL LETTER GAMMA
		0x03B4,	//	GREEK SMALL LETTER DELTA
		0x03B5,	//	GREEK SMALL LETTER EPSILON
		0x03B6,	//	GREEK SMALL LETTER ZETA
		0x03B7,	//	GREEK SMALL LETTER ETA
		0x03B8,	//	GREEK SMALL LETTER THETA
		0x03B9,	//	GREEK SMALL LETTER IOTA
		0x03BA,	//	GREEK SMALL LETTER KAPPA
		0x03BB,	//	GREEK SMALL LETTER LAMDA
		0x03BC,	//	GREEK SMALL LETTER MU
		0x03BD,	//	GREEK SMALL LETTER NU
		0x03BE,	//	GREEK SMALL LETTER XI
		0x03BF,	//	GREEK SMALL LETTER OMICRON
		0x03C0,	//	GREEK SMALL LETTER PI
		0x03C1,	//	GREEK SMALL LETTER RHO
		0x03C2,	//	GREEK SMALL LETTER FINAL SIGMA
		0x03C3,	//	GREEK SMALL LETTER SIGMA
		0x03C4,	//	GREEK SMALL LETTER TAU
		0x03C5,	//	GREEK SMALL LETTER UPSILON
		0x03C6,	//	GREEK SMALL LETTER PHI
		0x03C7,	//	GREEK SMALL LETTER CHI
		0x03C8,	//	GREEK SMALL LETTER PSI
		0x03C9,	//	GREEK SMALL LETTER OMEGA
		0x03CA,	//	GREEK SMALL LETTER IOTA WITH DIALYTIKA
		0x03CB,	//	GREEK SMALL LETTER UPSILON WITH DIALYTIKA
		0x03CC,	//	GREEK SMALL LETTER OMICRON WITH TONOS
		0x03CD,	//	GREEK SMALL LETTER UPSILON WITH TONOS
		0x03CE,	//	GREEK SMALL LETTER OMEGA WITH TONOS
		0x0000	//	DOES NOT EXIST
	};

/**
	The alphabet of ISO 8859-8.
*/
	static char [] iso8859_8 = {
		0x00A0,	//	NO-BREAK SPACE
		0x0000,	//	DOES NOT EXIST
		0x00A2,	//	CENT SIGN
		0x00A3,	//	POUND SIGN
		0x00A4,	//	CURRENCY SIGN
		0x00A5,	//	YEN SIGN
		0x00A6,	//	BROKEN BAR
		0x00A7,	//	SECTION SIGN
		0x00A8,	//	DIAERESIS
		0x00A9,	//	COPYRIGHT SIGN
		0x00D7,	//	MULTIPLICATION SIGN
		0x00AB,	//	LEFT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x00AC,	//	NOT SIGN
		0x00AD,	//	SOFT HYPHEN
		0x00AE,	//	REGISTERED SIGN
		0x203E,	//	OVERLINE
		0x00B0,	//	DEGREE SIGN
		0x00B1,	//	PLUS-MINUS SIGN
		0x00B2,	//	SUPERSCRIPT TWO
		0x00B3,	//	SUPERSCRIPT THREE
		0x00B4,	//	ACUTE ACCENT
		0x00B5,	//	MICRO SIGN
		0x00B6,	//	PILCROW SIGN
		0x00B7,	//	MIDDLE DOT
		0x00B8,	//	CEDILLA
		0x00B9,	//	SUPERSCRIPT ONE
		0x00F7,	//	DIVISION SIGN
		0x00BB,	//	RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x00BC,	//	VULGAR FRACTION ONE QUARTER
		0x00BD,	//	VULGAR FRACTION ONE HALF
		0x00BE,	//	VULGAR FRACTION THREE QUARTERS
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x2017,	//	DOUBLE LOW LINE
		0x05D0,	//	HEBREW LETTER ALEF
		0x05D1,	//	HEBREW LETTER BET
		0x05D2,	//	HEBREW LETTER GIMEL
		0x05D3,	//	HEBREW LETTER DALET
		0x05D4,	//	HEBREW LETTER HE
		0x05D5,	//	HEBREW LETTER VAV
		0x05D6,	//	HEBREW LETTER ZAYIN
		0x05D7,	//	HEBREW LETTER HET
		0x05D8,	//	HEBREW LETTER TET
		0x05D9,	//	HEBREW LETTER YOD
		0x05DA,	//	HEBREW LETTER FINAL KAF
		0x05DB,	//	HEBREW LETTER KAF
		0x05DC,	//	HEBREW LETTER LAMED
		0x05DD,	//	HEBREW LETTER FINAL MEM
		0x05DE,	//	HEBREW LETTER MEM
		0x05DF,	//	HEBREW LETTER FINAL NUN
		0x05E0,	//	HEBREW LETTER NUN
		0x05E1,	//	HEBREW LETTER SAMEKH
		0x05E2,	//	HEBREW LETTER AYIN
		0x05E3,	//	HEBREW LETTER FINAL PE
		0x05E4,	//	HEBREW LETTER PE
		0x05E5,	//	HEBREW LETTER FINAL TSADI
		0x05E6,	//	HEBREW LETTER TSADI
		0x05E7,	//	HEBREW LETTER QOF
		0x05E8,	//	HEBREW LETTER RESH
		0x05E9,	//	HEBREW LETTER SHIN
		0x05EA,	//	HEBREW LETTER TAV
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000,	//	DOES NOT EXIST
		0x0000	//	DOES NOT EXIST
	};

/**
	The alphabet of ISO 8859-9.
*/
	static char [] iso8859_9 = {
		0x00A0,	//	NO-BREAK SPACE
		0x00A1,	//	INVERTED EXCLAMATION MARK
		0x00A2,	//	CENT SIGN
		0x00A3,	//	POUND SIGN
		0x00A4,	//	CURRENCY SIGN
		0x00A5,	//	YEN SIGN
		0x00A6,	//	BROKEN BAR
		0x00A7,	//	SECTION SIGN
		0x00A8,	//	DIAERESIS
		0x00A9,	//	COPYRIGHT SIGN
		0x00AA,	//	FEMININE ORDINAL INDICATOR
		0x00AB,	//	LEFT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x00AC,	//	NOT SIGN
		0x00AD,	//	SOFT HYPHEN
		0x00AE,	//	REGISTERED SIGN
		0x00AF,	//	MACRON
		0x00B0,	//	DEGREE SIGN
		0x00B1,	//	PLUS-MINUS SIGN
		0x00B2,	//	SUPERSCRIPT TWO
		0x00B3,	//	SUPERSCRIPT THREE
		0x00B4,	//	ACUTE ACCENT
		0x00B5,	//	MICRO SIGN
		0x00B6,	//	PILCROW SIGN
		0x00B7,	//	MIDDLE DOT
		0x00B8,	//	CEDILLA
		0x00B9,	//	SUPERSCRIPT ONE
		0x00BA,	//	MASCULINE ORDINAL INDICATOR
		0x00BB,	//	RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK
		0x00BC,	//	VULGAR FRACTION ONE QUARTER
		0x00BD,	//	VULGAR FRACTION ONE HALF
		0x00BE,	//	VULGAR FRACTION THREE QUARTERS
		0x00BF,	//	INVERTED QUESTION MARK
		0x00C0,	//	LATIN CAPITAL LETTER A WITH GRAVE
		0x00C1,	//	LATIN CAPITAL LETTER A WITH ACUTE
		0x00C2,	//	LATIN CAPITAL LETTER A WITH CIRCUMFLEX
		0x00C3,	//	LATIN CAPITAL LETTER A WITH TILDE
		0x00C4,	//	LATIN CAPITAL LETTER A WITH DIAERESIS
		0x00C5,	//	LATIN CAPITAL LETTER A WITH RING ABOVE
		0x00C6,	//	LATIN CAPITAL LETTER AE
		0x00C7,	//	LATIN CAPITAL LETTER C WITH CEDILLA
		0x00C8,	//	LATIN CAPITAL LETTER E WITH GRAVE
		0x00C9,	//	LATIN CAPITAL LETTER E WITH ACUTE
		0x00CA,	//	LATIN CAPITAL LETTER E WITH CIRCUMFLEX
		0x00CB,	//	LATIN CAPITAL LETTER E WITH DIAERESIS
		0x00CC,	//	LATIN CAPITAL LETTER I WITH GRAVE
		0x00CD,	//	LATIN CAPITAL LETTER I WITH ACUTE
		0x00CE,	//	LATIN CAPITAL LETTER I WITH CIRCUMFLEX
		0x00CF,	//	LATIN CAPITAL LETTER I WITH DIAERESIS
		0x011E,	//	LATIN CAPITAL LETTER G WITH BREVE
		0x00D1,	//	LATIN CAPITAL LETTER N WITH TILDE
		0x00D2,	//	LATIN CAPITAL LETTER O WITH GRAVE
		0x00D3,	//	LATIN CAPITAL LETTER O WITH ACUTE
		0x00D4,	//	LATIN CAPITAL LETTER O WITH CIRCUMFLEX
		0x00D5,	//	LATIN CAPITAL LETTER O WITH TILDE
		0x00D6,	//	LATIN CAPITAL LETTER O WITH DIAERESIS
		0x00D7,	//	MULTIPLICATION SIGN
		0x00D8,	//	LATIN CAPITAL LETTER O WITH STROKE
		0x00D9,	//	LATIN CAPITAL LETTER U WITH GRAVE
		0x00DA,	//	LATIN CAPITAL LETTER U WITH ACUTE
		0x00DB,	//	LATIN CAPITAL LETTER U WITH CIRCUMFLEX
		0x00DC,	//	LATIN CAPITAL LETTER U WITH DIAERESIS
		0x0130,	//	LATIN CAPITAL LETTER I WITH DOT ABOVE
		0x015E,	//	LATIN CAPITAL LETTER S WITH CEDILLA
		0x00DF,	//	LATIN SMALL LETTER SHARP S
		0x00E0,	//	LATIN SMALL LETTER A WITH GRAVE
		0x00E1,	//	LATIN SMALL LETTER A WITH ACUTE
		0x00E2,	//	LATIN SMALL LETTER A WITH CIRCUMFLEX
		0x00E3,	//	LATIN SMALL LETTER A WITH TILDE
		0x00E4,	//	LATIN SMALL LETTER A WITH DIAERESIS
		0x00E5,	//	LATIN SMALL LETTER A WITH RING ABOVE
		0x00E6,	//	LATIN SMALL LETTER AE
		0x00E7,	//	LATIN SMALL LETTER C WITH CEDILLA
		0x00E8,	//	LATIN SMALL LETTER E WITH GRAVE
		0x00E9,	//	LATIN SMALL LETTER E WITH ACUTE
		0x00EA,	//	LATIN SMALL LETTER E WITH CIRCUMFLEX
		0x00EB,	//	LATIN SMALL LETTER E WITH DIAERESIS
		0x00EC,	//	LATIN SMALL LETTER I WITH GRAVE
		0x00ED,	//	LATIN SMALL LETTER I WITH ACUTE
		0x00EE,	//	LATIN SMALL LETTER I WITH CIRCUMFLEX
		0x00EF,	//	LATIN SMALL LETTER I WITH DIAERESIS
		0x011F,	//	LATIN SMALL LETTER G WITH BREVE
		0x00F1,	//	LATIN SMALL LETTER N WITH TILDE
		0x00F2,	//	LATIN SMALL LETTER O WITH GRAVE
		0x00F3,	//	LATIN SMALL LETTER O WITH ACUTE
		0x00F4,	//	LATIN SMALL LETTER O WITH CIRCUMFLEX
		0x00F5,	//	LATIN SMALL LETTER O WITH TILDE
		0x00F6,	//	LATIN SMALL LETTER O WITH DIAERESIS
		0x00F7,	//	DIVISION SIGN
		0x00F8,	//	LATIN SMALL LETTER O WITH STROKE
		0x00F9,	//	LATIN SMALL LETTER U WITH GRAVE
		0x00FA,	//	LATIN SMALL LETTER U WITH ACUTE
		0x00FB,	//	LATIN SMALL LETTER U WITH CIRCUMFLEX
		0x00FC,	//	LATIN SMALL LETTER U WITH DIAERESIS
		0x0131,	//	LATIN SMALL LETTER DOTLESS I
		0x015F,	//	LATIN SMALL LETTER S WITH CEDILLA
		0x00FF	//	LATIN SMALL LETTER Y WITH DIAERESIS
	};

/**
	The alphabet of ISO 8859.
*/
	private static char[] [] iso8859 = {
		iso8859_1,
		iso8859_2,
		iso8859_3,
		iso8859_4,
		iso8859_5,
		iso8859_6,
		iso8859_7,
		iso8859_8,
		iso8859_9
	};



/**
	Returns the string described by the current token assuming that 
	to encode it the character sets of ISO 8859 and/or ISO 10646 might be used.
*/

	private static char [] chars;
	private static final int NUMBER_OF_CHARACTERS_IN_STRING      = 128;
	static final String nullString = "";

	static final byte G_BACKSLASH = (byte)'\\';
	static final byte CAPITAL_S = (byte)'S';
	static final byte CAPITAL_P = (byte)'P';
	static final byte CAPITAL_X = (byte)'X';
	static final byte ZERO      = (byte)'0';
	static final byte TWO       = (byte)'2';
	static final byte FOUR      = (byte)'4';



	static String analyse_string(Token string_token) throws SdaiException  {

		String hexString;
		String result = "";

		int i;
		int k = 0;
		int count = 0;
		int to_string = -1;
		int code;
		int mult;
		int numb = 0;
		byte bt;
		boolean iso10646 = false;
		String base, current_str;
		// StaticFields staticFields;
		
		GintaroToken token = new GintaroToken(string_token);

		if (chars == null) {
			chars = new char[NUMBER_OF_CHARACTERS_IN_STRING];
		}

		
		if (token.length > chars.length) {
			int new_length = chars.length * 2;
			if (new_length < token.length) {
				new_length = token.length;
			}
			chars = new char[new_length];
		}
//current_str = new String(token.string, 0, token.length);
//System.out.println("PhFileReader   ******   current_str: " + current_str);
		if (token.integer == 0) {
			if (token.length > 0) {
				return new String(token.string, 0, token.length);
			} else {
				return nullString;
			}
		}
//System.out.println("----- We are at least here -------");
		while (k < token.length) {
								iso10646 = true;
								while (true) {
									if (k >= token.length) {
										break;
									}
									mult = 1;
									bt = token.string[k];
									if (!((bt >= '0' && bt <= '9') || (bt >= 'A' && bt <= 'F'))) {
										break;
									}
									code = 0;
									for (i = 0; i < 8; i++) {
                    
										//if (i < 4) continue; // <new>
										/*
										else if (i == 4) {
											chars[count++] = (char)'\\';
											chars[count++] = (char)'u';
										}
										*/
										bt = token.string[k + 7 - i];
										if (bt >= '0' && bt <= '9') {
											numb = (int)bt - (int)'0';
										} else if (bt >= 'A' && bt <= 'F') {
											numb = (int)bt - (int)'A' + 10;
										} else {
//											string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k + 7 - i));
											// string_warning(active_session, AdditionalMessages.RD_WRIS, 
											//	new String(token.string, 0, k + 8 - i), token, k + 8 - i);
//										  System.out.println("STRING ENCODING - WARNING 12 - RD_WRIS");
											numb = 0;
										}
										code += (mult * numb);
										mult *= 16;
									}
									hexString = Integer.toHexString((char)code);
									if (hexString.length() == 4) {
//										chars[count++] = (char)'\\'; // <new>
//										chars[count++] = (char)'u';  // <new>
											result += '\\';
											result += 'u';
											result += hexString;
									} else
									if (hexString.length() == 3) {
											result += '\\';
											result += 'u';
											result += '0';
											result += hexString;
									} else
									if (hexString.length() == 2) {
											result += '\\';
											result += 'u';
											result += '0';
											result += '0';
											result += hexString;
									} else {
											// should not happen !!!
										  result += (char)code;
									}
									// System.out.println("hex value: " + hexString);
									chars[count] = (char)code;
									count++;
									k += 8;
								}
	} // while
		if (count > 0) {

//			return new String(chars, 0, count);
		} else {
//			return nullString;
		}
		return result;
	}


	static String analyse_string_rr_1(Token string_token) throws SdaiException  {

		int i;
		int k = 0;
		int count = 0;
		int to_string = -1;
		int code;
		int mult;
		int numb = 0;
		byte bt;
		boolean iso10646 = false;
		String base, current_str;
		// StaticFields staticFields;
		
		GintaroToken token = new GintaroToken(string_token);

		if (chars == null) {
			chars = new char[NUMBER_OF_CHARACTERS_IN_STRING];
		}

		
		if (token.length > chars.length) {
			int new_length = chars.length * 2;
			if (new_length < token.length) {
				new_length = token.length;
			}
			chars = new char[new_length];
		}
//current_str = new String(token.string, 0, token.length);
//System.out.println("PhFileReader   ******   current_str: " + current_str);
		if (token.integer == 0) {
			if (token.length > 0) {
				return new String(token.string, 0, token.length);
			} else {
				return nullString;
			}
		}
//System.out.println("----- We are at least here -------");
		while (k < token.length) {
								iso10646 = true;
								while (true) {
									if (k >= token.length) {
										break;
									}
									mult = 1;
									bt = token.string[k];
									if (!((bt >= '0' && bt <= '9') || (bt >= 'A' && bt <= 'F'))) {
										break;
									}
									code = 0;
									for (i = 0; i < 8; i++) {
										bt = token.string[k + 7 - i];
										if (bt >= '0' && bt <= '9') {
											numb = (int)bt - (int)'0';
										} else if (bt >= 'A' && bt <= 'F') {
											numb = (int)bt - (int)'A' + 10;
										} else {
//											string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k + 7 - i));
											// string_warning(active_session, AdditionalMessages.RD_WRIS, 
											//	new String(token.string, 0, k + 8 - i), token, k + 8 - i);
										  // System.out.println("STRING ENCODING - WARNING 12 - RD_WRIS");
											numb = 0;
										}
										code += (mult * numb);
										mult *= 16;
									}
									chars[count] = (char)code;
									count++;
									k += 8;
								}
	} // while
		if (count > 0) {
			return new String(chars, 0, count);
		} else {
			return nullString;
		}
	}

	static String analyse_string_original(Token string_token) throws SdaiException  {
		int i;
		int k = 0;
		int count = 0;
		int to_string = -1;
		int code;
		int mult;
		int numb = 0;
		byte bt;
		boolean iso10646 = false;
		String base, current_str;
		// StaticFields staticFields;
		
		GintaroToken token = new GintaroToken(string_token);

		if (chars == null) {
			chars = new char[NUMBER_OF_CHARACTERS_IN_STRING];
		}

		
		if (token.length > chars.length) {
			int new_length = chars.length * 2;
			if (new_length < token.length) {
				new_length = token.length;
			}
			chars = new char[new_length];
		}
//current_str = new String(token.string, 0, token.length);
//System.out.println("PhFileReader   ******   current_str: " + current_str);
		if (token.integer == 0) {
			if (token.length > 0) {
				return new String(token.string, 0, token.length);
			} else {
				return nullString;
			}
		}
// System.out.println("----- We are at least here -------");
		while (k < token.length) {
			if (token.string[k] == G_BACKSLASH) {
				k++;
				if (token.string[k] == G_BACKSLASH) {
					chars[count] = (char)token.string[k];
					count++;
					k++;
					continue;
				}
				switch (token.string[k]) {
					case CAPITAL_S:
						k++;
						if (token.string[k] != G_BACKSLASH) {
//							string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
							// string_warning(active_session, AdditionalMessages.RD_WRIS, new String(token.string, 0, k + 1), token, k + 1);
							//System.out.println("STRING ENCODING - WARNING 01 - RD_WRIS");
						} else {
							k++;
						}
						code = (int)token.string[k];
						if (to_string >= 0) {
							code = iso8859[to_string][code - 32];
							if (code <= 0) {
//								string_exception(AdditionalMessages.RD_INCH, new String(token.string, 0, k));
								// string_warning(active_session, AdditionalMessages.RD_INCS, new String(token.string, 0, k + 1), token, k + 1);
							  //System.out.println("STRING ENCODING - WARNING 02 - RD_INCS");
								code = iso8859[to_string][0];
							}
						} else {
							code += 128;
						}
						chars[count] = (char)code;
						count++;
						k++;
						break;
					case CAPITAL_P:
						k++;
						if (token.string[k] < 'A' || token.string[k] > 'I') {
//							string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
							// string_warning(active_session, AdditionalMessages.RD_WRIS, new String(token.string, 0, k + 1), token, k + 1);
							//System.out.println("STRING ENCODING - WARNING 03 - RD_WRIS");
							to_string = 0;
						} else {
							to_string = (int)token.string[k] - (int)'A';
						}
						k++;
						if (token.string[k] != G_BACKSLASH) {
//							string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
							// string_warning(active_session, AdditionalMessages.RD_WRIS, new String(token.string, 0, k + 1), token, k + 1);
							//System.out.println("STRING ENCODING - WARNING 04 - RD_WRIS");
						} else {
							k++;
						}
						break;
					case CAPITAL_X:
						k++;
						switch (token.string[k]) {
							case G_BACKSLASH:
								code = 0;
								mult = 1;
								for (i = 0; i < 2; i++) {
									bt = token.string[k + 2 - i];
									if (bt >= '0' && bt <= '9') {
										numb = (int)bt - (int)'0';
									} else if (bt >= 'A' && bt <= 'F') {
										numb = (int)bt - (int)'A' + 10;
									} else {
//										string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k + 2 - i));
										// string_warning(active_session, AdditionalMessages.RD_WRIS, 
										// new String(token.string, 0, k + 3 - i), token, k + 3 - i);
										//System.out.println("STRING ENCODING - WARNING 05 - RD_WRIS");
										numb = 0;
									}
									code += (mult * numb);
									mult *= 16;
								}
								chars[count] = (char)code;
								count++;
								k += 3;
								break;
							case ZERO:
								k++;
								if (token.string[k] != G_BACKSLASH) {
//									string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
									// string_warning(active_session, AdditionalMessages.RD_WRIS, 
									//	new String(token.string, 0, k + 1), token, k + 1);
									//System.out.println("STRING ENCODING - WARNING 06 - RD_WRIS");
							} else {
									k++;
								}
								if (iso10646) {
									iso10646 = false;
								} else {
//									string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
									// string_warning(active_session, AdditionalMessages.RD_WRIS, 
									//	new String(token.string, 0, k), token, k);
									//System.out.println("STRING ENCODING - WARNING 07 - RD_WRIS");
								}
								break;
							case TWO:
								k++;
								if (token.string[k] != G_BACKSLASH) {
//									string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
									// string_warning(active_session, AdditionalMessages.RD_WRIS, 
									//	new String(token.string, 0, k + 1), token, k + 1);
										//System.out.println("STRING ENCODING - WARNING 08 - RD_WRIS");
								} else {
									k++;
								}
								iso10646 = true;
								while (true) {
									if (k >= token.length) {
										break;
									}
									mult = 1;
									bt = token.string[k];
									if (!((bt >= '0' && bt <= '9') || (bt >= 'A' && bt <= 'F'))) {
										break;
									}
									code = 0;
									for (i = 0; i < 4; i++) {
										bt = token.string[k + 3 - i];
										if (bt >= '0' && bt <= '9') {
											numb = (int)bt - (int)'0';
										} else if (bt >= 'A' && bt <= 'F') {
											numb = (int)bt - (int)'A' + 10;
										} else {
//											string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k + 3 - i));
											// string_warning(active_session, AdditionalMessages.RD_WRIS, 
											//	new String(token.string, 0, k + 4 - i), token, k + 4 - i);
										 //System.out.println("STRING ENCODING - WARNING 09 - RD_WRIS");
											numb = 0;
										}
										code += (mult * numb);
										mult *= 16;
									}
									chars[count] = (char)code;
									count++;
									k += 4;
								}
								if (k >= token.length || token.string[k] != G_BACKSLASH) {
									iso10646 = false;
									// staticFields = StaticFields.get();
									// EntityValue.printWarningToLogo(active_session, AdditionalMessages.RD_CDEM, staticFields.current_instance_identifier);
										//System.out.println("STRING ENCODING - WARNING 10 - RD_CDEM");
								}
								break;
							case FOUR:
								k++;
								if (token.string[k] != G_BACKSLASH) {
//									string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
									// string_warning(active_session, AdditionalMessages.RD_WRIS, 
									//	new String(token.string, 0, k + 1), token, k + 1);
										//System.out.println("STRING ENCODING - WARNING 11 - RD_WRIS");
								} else {
									k++;
								}
								iso10646 = true;
								while (true) {
									if (k >= token.length) {
										break;
									}
									mult = 1;
									bt = token.string[k];
									if (!((bt >= '0' && bt <= '9') || (bt >= 'A' && bt <= 'F'))) {
										break;
									}
									code = 0;
									for (i = 0; i < 8; i++) {
										bt = token.string[k + 7 - i];
										if (bt >= '0' && bt <= '9') {
											numb = (int)bt - (int)'0';
										} else if (bt >= 'A' && bt <= 'F') {
											numb = (int)bt - (int)'A' + 10;
										} else {
//											string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k + 7 - i));
											// string_warning(active_session, AdditionalMessages.RD_WRIS, 
											//	new String(token.string, 0, k + 8 - i), token, k + 8 - i);
										  //System.out.println("STRING ENCODING - WARNING 12 - RD_WRIS");
											numb = 0;
										}
										code += (mult * numb);
										mult *= 16;
									}
									chars[count] = (char)code;
									count++;
									k += 8;
								}
								if (k >= token.length || token.string[k] != G_BACKSLASH) {
									iso10646 = false;
									// staticFields = StaticFields.get();
									// EntityValue.printWarningToLogo(active_session, AdditionalMessages.RD_CDEM, staticFields.current_instance_identifier);
									//System.out.println("STRING ENCODING - WARNING 13 - RD_CDEM");
								}
								break;
							default:
//								string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k));
								// string_warning(active_session, AdditionalMessages.RD_WRIS, 
								// 	new String(token.string, 0, k + 1), token, k + 1);
								//System.out.println("STRING ENCODING - WARNING 14 - RD_WRIS");
								code = 0;
								mult = 1;
								for (i = 0; i < 2; i++) {
									bt = token.string[k + 1 - i];
									if (bt >= '0' && bt <= '9') {
										numb = (int)bt - (int)'0';
									} else if (bt >= 'A' && bt <= 'F') {
										numb = (int)bt - (int)'A' + 10;
									} else {
										// string_warning(active_session, AdditionalMessages.RD_WRIS, 
										//	new String(token.string, 0, k + 2 - i), token, k + 2 - i);
										//System.out.println("STRING ENCODING - WARNING 15 - RD_WRIS");
										numb = 0;
									}
									code += (mult * numb);
									mult *= 16;
								}
								chars[count] = (char)code;
								count++;
								k += 2;
						}
						break;
					default:
						// string_warning(active_session, AdditionalMessages.RD_WRIS, new String(token.string, 0, k + 1), token, k + 1);
						//System.out.println("STRING ENCODING - WARNING 16 - RD_WRIS");
//						string_exception(AdditionalMessages.RD_WRST, new String(token.string, 0, k + 1), token);
						chars[count] = (char)G_BACKSLASH;
						count++;
						continue;
				}
			} else {
				chars[count] = (char)token.string[k];
				count++;
				k++;
			}
		}
		if (count > 0) {
			return new String(chars, 0, count);
		} else {
			return nullString;
		}
	}



}



class SorterForModels implements Comparator {
  public int compare(Object o1, Object o2) {
    try {
      SdaiModel m1 = (SdaiModel) o1;
      SdaiModel m2 = (SdaiModel) o2;
      String n1 = m1.getName();
      String n2 = m2.getName();

      return n1.compareToIgnoreCase(n2);
    } catch (SdaiException exc) {
      exc.printStackTrace(System.err);

      return 0;
    }
  }
}


class ExpressFilenameFilter implements FilenameFilter {
	ExpressFilenameFilter() {
	}
	public boolean accept(File dir, String name) {
	  File file = new File(dir,name);
  	if (file.isDirectory()) {
  		return false;
  	} else 
  	if((file.isFile()) && (name.toLowerCase().endsWith(".exp"))) {
  		return true;
  	} else {
  		return false;
  	}
	} 
}

class ExpressFilenameFilterForRecursive implements FilenameFilter {
	ExpressFilenameFilterForRecursive() {
	}
	public boolean accept(File dir, String name) {
	  File file = new File(dir,name);
  	if (file.isDirectory()) {
  		return true;
  	} else 
  	if((file.isFile()) && (name.toLowerCase().endsWith(".exp"))) {
  		return true;
  	} else {
  		return false;
  	}
	} 
}

class GintaroToken {
	int    type;
	int    integer;
	double real;
	byte[] string;
	int    length;
	int    line;
	int    column;

	static final int LENGTH_OF_STRING_TOKEN = 128;


	GintaroToken (Token my_token) {
//		string = new byte[LENGTH_OF_STRING_TOKEN];
//			string = my_token.image.getBytes();
			string = my_token.image.substring(1,my_token.image.length()-1).getBytes();
			length = my_token.image.length()-2;
			integer = 1; // seems that if integer == 0, then return string constructed directly without any re-encoding
			type = 1; // not used here
	}



	void enlarge() {
		int new_length = string.length * 2;
		byte new_string[] = new byte[new_length];
		System.arraycopy(string, 0, new_string, 0, string.length);
		string = new_string;
	}

}