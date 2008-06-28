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

// %modified: 1016210367035 %
package jsdai.expressCompiler;

import java.io.*;
import java.util.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;


public class JavaClass {
  public static int uid = 0;
  static boolean print2string = false;
  static boolean print2string_activated = false;

  // stores the type of operand until an operation node takes it on the way up.
  // at that point the operation node writes here the type of operand to pass further up
  // it is saved in the operation node until that time.
  // if int type_of_operand indicates an entity or an aggregate, then a more specific type can be stored in the next attributes.
  // correspond to literal types
  final static int T_BINARY = 1;
  final static int T_INTEGER = 2;
  final static int T_LOGICAL = 3;
  final static int T_REAL = 4;
  final static int T_STRING = 5;

  // no such literals
  final static int T_NUMBER = 4; // using the same as for REAL, at least for now.
  final static int T_BOOLEAN = 6;
  final static int T_ENUMERATION = 7;
  final static int T_ENTITY = 8;
  final static int T_AGGREGATE = 10;

  // specific aggregates are known, but not used
  final static int T_ARRAY = 11;
  final static int T_BAG = 12;
  final static int T_LIST = 13;
  final static int T_SET = 14;
  Vector init_rule;

  // this stack is supposed to contain data types from which the static string to be supllied in alloc()/init() can be calculated
  // for next alloc. If the stack is empty, then alloc is empty too. If stack contains null, then alloc is empty too.
  // to support nested functions, a stack is needed here
  Vector pdb;
  EEntity return_type;

	ArrayList all_attributes;

	SimpleNode keynode;


	boolean foreach = false;
	Vector foreach_where = null;
	Stack foreach_stack = null;
	
	
	String x_target_type;
	boolean in_entity_instantiation_loop = false;
	boolean in_entity_instantiation_loop_first = false;
	boolean single_map_call = false;
	ETarget_parameter target = null;
	ETarget_parameter current_target = null;

	String declare_rule	= "";
	int query_logical_expression;
	boolean expression_fragment;
  boolean indexing = false;
  boolean in_function = false;
  boolean in_procedure = false;
  boolean in_return = false;
	boolean in_local_variable = false;
  int current_function_parameter = 0;
  boolean in_aggregate_initializer = false;
  boolean domain_rule = false;
  boolean last_left_qualifier = false;
  HashSet variable_ids = null;
  boolean first_pass = false;
  boolean second_pass = false;
  boolean identified_by = false;
  int assignment_depth;
  int alloc_type_depth;
  int target_par_count;

	int ind;

	HashSet attribute_mappings = null;


  // Stack alloc_type_stack;
  JavaBackend java_backend_inst;
  boolean flag_alloc_type = false;
  String alloc_type;
  boolean flag_non_temporary_value_instance = false;
  boolean in_derive_definition = false;
  boolean in_constant_definition = false;
  String value_instance = "";
  boolean flag_value;

  //  ESchema_definition sd = null;
  EGeneric_schema_definition sd = null;
  EEntity_definition ed = null;
  EFunction_definition fd = null;
  EProcedure_definition prd = null;
	EParameter pd = null;
	ASource_parameter asp = null; // source parameters of current map partition
  EMap_or_view_partition partition = null;
  SdaiModel model;
  int indent = 0;
  Stack java_stack; // removed static
  EEntity entity;
  PrintWriter pw;
  boolean active;
	boolean flag_print_active_nodes = true;
	boolean flag_deep_debug = false;
  int[] add_operations;
  String generated_java = null;
  String saved_str = "";
  String java_str2 = "";
  String java_str3 = "";
  String java_str4 = "";
  String java_str5 = "";
  String java_str6 = "";
  String print_string = "";
  String print_first_string = "";
  String print_before_first_string = "";
  String print_tabs = "";
  int type_of_operand;
  EEntity_definition type_of_entity;
  int aggregate_type;
  EAggregation_type type_of_aggregate; // can be specific aggregate or generic.
  int add_pointer;
  int add_depth;

  boolean inner;
	String local_declarations_str = "";
	String local_initializations_str = "";
	String statements_str = "";
  String return_str = "";
  String parameter_declarations_str = "";
  String parameter_initializations_str = "";
	String var_parameter_post_initializations_str = "";
	String constant_static_field_str = "";
	String constant_method_str = "";
	boolean inner_constant = false;

	String iterator_uid = "";
	String index_uid = "";

	int left_side_indexing = 0;


	int secondary = 0;
	JavaClass parent = null;
	Object reference = null;

  JavaClass(PrintWriter pw1, EEntity ee, EGeneric_schema_definition __sd, EEntity_definition __ed, 
            SdaiModel current_model, boolean f_value, JavaBackend java_backend_instance) {
		
		left_side_indexing = 0;
		x_target_type = "";
		current_target = null;
		target = null;
		single_map_call = false;
		foreach_stack = new Stack();
		attribute_mappings = null;
    secondary = 0;
    parent = null;
    reference = null;

    flag_value = f_value;
    pw = pw1;
    entity = ee;
    inner = false;
    return_str = "";
    statements_str = "";
    local_declarations_str = "";
    local_initializations_str = "";
  	parameter_declarations_str = "";
  	parameter_initializations_str = "";
  	var_parameter_post_initializations_str = "";
		constant_static_field_str = "";
		constant_method_str = "";
    sd = __sd;
    ed = __ed;
    active = false;
    print2string = false;
    print2string_activated = false;
		expression_fragment = false;
    java_stack = new Stack();
		query_logical_expression = 0;
		inner_constant = false;
		declare_rule = "";

    // alloc_type_stack = new Stack();
    indent = 0;
    model = current_model;
    java_backend_inst = java_backend_instance;
    in_aggregate_initializer = false;
    in_function = false;
    in_procedure = false;
    indexing = false;
    pdb = new Vector();
    target_par_count = 0;
    iterator_uid = "";
    index_uid = "";
  }


  JavaClass(PrintWriter pw1, EEntity ee, EGeneric_schema_definition __sd, EEntity_definition __ed, 
            SdaiModel current_model, boolean f_value, JavaBackend java_backend_instance, ArrayList global_all_attributes_all) {

		left_side_indexing = 0;
		x_target_type = "";
		current_target = null;
		target = null;
		single_map_call = false;
		foreach_stack = new Stack();
		attribute_mappings = null;
    secondary = 0;
    parent = null;
    reference = null;

    all_attributes = global_all_attributes_all;
    flag_value = f_value;
    pw = pw1;
    entity = ee;
    inner = false;
    return_str = "";
    statements_str = "";
    local_declarations_str = "";
    local_initializations_str = "";
  	parameter_declarations_str = "";
  	parameter_initializations_str = "";
  	var_parameter_post_initializations_str = "";
		constant_static_field_str = "";
		constant_method_str = "";
    sd = __sd;
    ed = __ed;
    active = false;
    print2string = false;
    print2string_activated = false;
		expression_fragment = false;
    java_stack = new Stack();
		query_logical_expression = 0;
		inner_constant = false;
		declare_rule = "";

    // alloc_type_stack = new Stack();
    indent = 0;
    model = current_model;
    java_backend_inst = java_backend_instance;
    in_aggregate_initializer = false;
    in_function = false;
    in_procedure = false;
    indexing = false;
    pdb = new Vector();
    target_par_count = 0;
    iterator_uid = "";
    index_uid = "";
  }


  JavaClass(PrintWriter pw1, EEntity ee, EGeneric_schema_definition __sd, EEntity_definition __ed, 
            SdaiModel current_model, boolean f_value, JavaBackend java_backend_instance, ArrayList global_all_attributes_all, int secondary_flag, JavaClass parent_traversing, Object reference_object) {
    
		left_side_indexing = 0;
		x_target_type = "";
		current_target = null;
		target = null;
		single_map_call = false;
		foreach_stack = new Stack();
		attribute_mappings = null;
    secondary = secondary_flag;
    parent = parent_traversing;
    reference = reference_object;

    all_attributes = global_all_attributes_all;
    flag_value = f_value;
    pw = pw1;
    entity = ee;
    inner = false;
    return_str = "";
    statements_str = "";
    local_declarations_str = "";
    local_initializations_str = "";
  	parameter_declarations_str = "";
  	parameter_initializations_str = "";
  	var_parameter_post_initializations_str = "";
		constant_static_field_str = "";
		constant_method_str = "";
    sd = __sd;
    ed = __ed;
    active = false;
    print2string = false;
    print2string_activated = false;
		expression_fragment = false;
    java_stack = new Stack();
		query_logical_expression = 0;
		inner_constant = false;
		declare_rule = "";

    // alloc_type_stack = new Stack();
    indent = 0;
    model = current_model;
    java_backend_inst = java_backend_instance;
    in_aggregate_initializer = false;
    in_function = false;
    in_procedure = false;
    indexing = false;
    pdb = new Vector();
    target_par_count = 0;
    iterator_uid = "";
    index_uid = "";
  }



  //   getAllocType() throws SdaiException {
  //   }
}
