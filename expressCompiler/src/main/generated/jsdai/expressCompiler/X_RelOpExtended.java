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

// %modified: 1016210368166 %

/* Generated By:JJTree: Do not edit this line. X_RelOpExtended.java */
package jsdai.expressCompiler;

import java.util.*;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

/*
1 <    - numeric/logical/string/binary/enumeration vale less than
2 >    - numeric/logical/string/binary/enumeration value greater than
3 <=   - numeric/logical/string/binary/enumeration value less than or equal
4 >=   - numeric/logical/string/binary/enumeration value greater than or equal
5 <>   - numeric/logical/string/binary/enumeration/aggregate/entity value not equal
6 =    - numeric/logical/string/binary/enumeration/aggregate/entity value equal
7 :<>: - numeric/logical/string/binary/enumeration/aggregate/entity instance not equal
8 :=:  - numeric/logical/string/binary/enumeration/aggregate/entity instance equal
9 IN   - membership operator -  an_element IN an_aggregate
10 LIKE - string match operator - str_a LIKE str_b
for numeric/logical/string/binary/enumeration value and instance comparison is identical
for entities, value equal is when each attribute value is equal, instance equal is when it is the same instance
for aggregates, value equal is when  members are value equal, instance equal is when  members are the same instances
(especially, of entities or aggregates).

BAG can be equal to SET, both value or instance equal


 */
public class X_RelOpExtended extends ExpressionNode {
  // public class X_RelOpExtended extends SimpleNode {
  int op_count = 0;
  ArrayList operations; //  int[] operations;
  ArrayList operands; //  String[] operands;
  ArrayList operand_types; //  int[] operand_types;
  ArrayList exact_operand_types; // EEntity[] exact_operand_types;

  public X_RelOpExtended(int id) {
    super(id);

    operations = new ArrayList(); // operations = new int[20];
    operands = new ArrayList(); // operands = new String[20]; 
    operand_types = new ArrayList(); // operand_types = new int[20];
    exact_operand_types = new ArrayList(); // exact_operand_types = new EEntity[20];

    op_count = 0;
  }

  public X_RelOpExtended(Compiler2 p, int id) {
    super(p, id);

    operations = new ArrayList(); // operations = new int[20];
    operands = new ArrayList(); // operands = new String[20];
    operand_types = new ArrayList(); // operand_types = new int[20];
    exact_operand_types = new ArrayList(); // exact_operand_types = new EEntity[20];

    op_count = 0;
  }

  /**
   * Accept the visitor.
   **/
  public Object jjtAccept(Compiler2Visitor visitor, Object data)
      throws SdaiException {
    return visitor.visit(this, data);
  }

  /**
   * Accept the visitor.
   **/
  public Object childrenAccept(Compiler2Visitor visitor, Object data)
      throws SdaiException {
    JavaClass jc = (JavaClass) data;
    printActive("XP childrenAccept starting - RelOpExtended: " + id + ", parent: " +
        ((SimpleNode) parent).id, jc);

//		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>");
//    System.out.println("XP childrenAccept starting - RelOpExtended: " + id + ", parent: " + ((SimpleNode) parent).id);
//		System.out.println("\top_count: " + op_count);
//		System.out.println("\toperations, size: " + operations.size() + ", operations: " + operations);
//		System.out.println("\toperands, size: " + operands.size() + ", operands: " + operands);
//		System.out.println("<<       >>");

    String op_type_str = "default";
    String operator_str = "DefaultRelOperator";
    String cast_1 = "";
    String cast_2 = "";
    int operand_1_type;
    int operand_2_type;
    int result_type = 0;
    EEntity exact_result_type = null;
    String str = null;

    if (jc.flag_value) {
      if (children != null) {
        // additional java code forwarded, if present
        variable_names = new Vector();
        variable_declarations = new Vector();
        statements = new Vector();
        initializing_code = new Vector();

        op_count = 0;
// System.out.println("<X-0>: " + children.length);
        for (int i = 0; i < children.length; ++i) {
          children[i].jjtAccept(visitor, data);

          //					System.out.println("child: " +children[i]);
          if (jc != null) {
            if (jc.active) {
              if (((SimpleNode) children[i]).java_contains_statements) {
                java_contains_statements = true;

                // variable_declaration += "\n" + ((SimpleNode)children[i]).variable_declaration;
                for (int j = 0; j < ((SimpleNode) children[i]).variable_names.size(); j++) {
                  variable_names.add(((SimpleNode) children[i]).variable_names.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).variable_declarations.size(); j++) {
                  variable_declarations.add(((SimpleNode) children[i]).variable_declarations.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).statements.size(); j++) {
                  statements.add(((SimpleNode) children[i]).statements.elementAt(j));
                }

                // initializing_code += "\n" + ((SimpleNode)children[i]).initializing_code;
                for (int j = 0; j < ((SimpleNode) children[i]).initializing_code.size(); j++) {
                  initializing_code.add(((SimpleNode) children[i]).initializing_code.elementAt(j));
                }
              } // if contains statements

              //							System.out.println("op count:" +op_count + " gen: " +jc.generated_java);
              if (children[i] instanceof X_IndexQualifier) {
                str = (String) operands.get(op_count - 1) + jc.generated_java;
                operands.set(op_count - 1, str);
                // operands[op_count - 1] += jc.generated_java;
              }
              else if (children[i] instanceof X_AttributeQualifier) {
                str = (String) operands.get(op_count - 1) + jc.generated_java;
                operands.set(op_count - 1, str);
                // operands[op_count - 1] += jc.generated_java;
              }
              else if (children[i] instanceof X_GroupQualifier) {
                str = (String) operands.get(op_count - 1) + jc.generated_java;
                operands.set(op_count - 1, str);
// System.out.println("<X>: " + children[i] + ", i: " + i);
                // operands[op_count - 1] += jc.generated_java;
              }
              else {
                if (op_count >= operands.size()) {
                  operands.add(jc.generated_java);
                }
                else {
                  // probably not needed
                  operands.set(op_count, jc.generated_java);
                }
                // operands[op_count] = jc.generated_java;
                op_count++;
              }

              //                     if (java_contains_statements) {
              //                        operands[i] = (String)initializing_code.elementAt(initializing_code.size()-1);
              //                     } else {
              //                        operands[i] = jc.generated_java;
              //                     }
//              operands[i] = jc.generated_java;

// what is this?  isn't it handled already above, selectively when not qualifier?
//              operands.set(i, jc.generated_java);

//              operand_types[i] = jc.type_of_operand;

//              operand_types.set(i, new Integer(jc.type_of_operand));

              if (jc.type_of_operand >= JavaClass.T_AGGREGATE) {
//                exact_operand_types[i] = jc.type_of_aggregate;
//                exact_operand_types.set(i, jc.type_of_aggregate);
              }

              jc.generated_java = "";
            } // jc.active
          } // jc not null
        } // for - loop through children
      } // if children - mayb better enclose everything?

      if (jc != null) {
        if (jc.active) {
          // let's say here we analyze the types of operands, and if numeric, then:
          //               jc.generated_java = "(";
//          jc.generated_java = operands[0];
          jc.generated_java = (String) operands.get(0);

          // operand_1_type = operand_types[0];
          for (int i = 0; i < (op_count - 1); ++i) {
            // operand_2_type = operand_types[i+1];
            // here we could have a general operand compatibility testing method invoked.
            // instead currently I invoke more specific methods in other places, they return not boolean, but the resulting type in dictionary.
            // System.out.println("###_#- Operand 1: " + operand_1_type + ", operand 2: " + operand_2_type);

//            switch (operations[i]) {
            switch (((Integer) operations.get(i)).intValue()) {
              case 1:

                // operator_str = "<";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).less";

                break;

              case 2:

                // operator_str = ">";
                // operator_str = "isGreater";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).greater";

                break;

              case 3:

                // operator_str = "<=";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal";

                break;

              case 4:

                // operator_str = ">=";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).gequal";

                break;

              case 5:

                // operator_str = "<>";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).nequal";

                break;

              case 6:

                // operator_str = "=";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).equal";

                break;

              case 7:

                // operator_str = ":<>:";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).instanceNotEqual";

                break;

              case 8:

                // operator_str = ":=:";
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).instanceEqual";

                break;

              case 9:

                // operator_str = "IN";
                // the right side type must be an aggregate, the left side type - an elemento of a compatible type
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).IN";

                break;

              case 10:

                // operator_str = "LIKE";
                // both operand must be strings
                operator_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).LIKE";

                break;

              default:
                System.out.println("XP - RelOPExtended: operator is unknown: " + (String) operations.get(i));
                operator_str = "DefaultRelOperator";

                break;
            }

            // -- new ending:
            //                  jc.generated_java = op_type_str + operator_str + "(" + cast_1 + jc.generated_java + ", " + cast_2 + operands[i+1] + ")";
            //                jc.generated_java = op_type_str + operator_str + "(" + jc.generated_java + ", " + operands[i+1] + ")";
            String value_instance_string;
            String alloc_type_str;

            //                if ((jc.flag_alloc_type) && ((jc.alloc_type_depth + 1) == jc.indent) && (i == children.length-2)) {
            if ((jc.flag_alloc_type) && ((jc.alloc_type_depth + 1) == jc.indent) &&
                (i == op_count - 2)) {
              alloc_type_str = jc.alloc_type;
              jc.flag_alloc_type = false;
              jc.alloc_type = "";
            }
            else {
              alloc_type_str = "ExpressTypes.LOGICAL_TYPE";
            }

            //                if (j.flag_non_temporary_value_instance) {
            // System.out.println("XC assignment AddLikeOp indent: " + jc.indent + ", left: " + jc.value_instance);
            //                if ((jc.flag_non_temporary_value_instance) && ((jc.assignment_depth + 1) == jc.indent) && (i == children.length-2)) {
            if ((jc.flag_non_temporary_value_instance) &&
                ((jc.assignment_depth + 1) == jc.indent) && (i == op_count - 2)) {
              value_instance_string = jc.value_instance;
              jc.flag_non_temporary_value_instance = false;
              jc.value_instance = "";
            }
            else {
              value_instance_string = "Value.alloc(" + alloc_type_str + ").";
            }

            value_instance_string = ""; // relational methods are static in Value - NOT any more

            //  jc.generated_java = value_instance_string + operator_str + "(" + jc.generated_java + ", " + operands[i + 1] + ") == 2";
            // FIXME: exact_operand_types may be obsolete
            // how to get other parameter's type ? 
            // maybe implementation in X_AggregateInitializer would be more optimal?
            if (jc.generated_java.equals("_JUST_CREATE_")) { // aggregate initializer

              //							jc.generated_java = "Value.alloc(" +exact_operand_types[i+1] +").create()";
              jc.generated_java = "Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create()";
            }

            ;

//            if (operands[i + 1].equals("_JUST_CREATE_")) { // aggregate initializer
            if (((String) operands.get(i + 1)).equals("_JUST_CREATE_")) { // aggregate initializer

              //							operands[i+1] = "Value.alloc(" +exact_operand_types[i] +").create()";
//              operands[i + 1] = "Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create()";
              operands.set(i + 1, "Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create()");
            }

            ;
//RR-SdaiContext - if all operators
//            jc.generated_java = value_instance_string + operator_str + "(_context, " + jc.generated_java + ", " + operands[i + 1] + ")";
            jc.generated_java = value_instance_string + operator_str + "(_context, " + jc.generated_java + ", " + (String) operands.get(i + 1) + ")";

            // 						if(operations[i] == 9){ //IN
            // 								jc.generated_java = operator_str + "(" + jc.generated_java + ", " +value_instance_string +"Value.alloc(ExpressTypes.LIST_GENERIC_TYPE).set(_context, "+ operands[i + 1] +")" + ")";
            // //						}else if(operations[i] == 6){//equal
            // 						}else{
            // 								jc.generated_java = value_instance_string + operator_str + "(" + jc.generated_java + ", " + operands[i + 1] + ")";
            // 						}
            //                jc.generated_java = value_instance_string + operator_str + "(" + jc.generated_java + ", " + operands[i+1] + ")";
          } // for children
        }
      }
    }
    else { // old stuff
    }

    if (jc != null) {
      if (jc.active) {
        jc.type_of_operand = result_type;
      }
    }

    // } // if not value
    return data;
  }

  // old ending
  //                   op_type_str = "";
  //                   // op_type_str = "integers";
  ////                  jc.generated_java = op_type_str + operator_str + "(" + cast_1 + jc.generated_java + ", " + cast_2 + operands[i+1] + ")";
  //                jc.generated_java = op_type_str + operator_str + "(" + jc.generated_java + ", " + operands[i+1] + ") == 2";
  //                  operand_1_type = result_type;
  //                  exact_operand_types[i] = exact_result_type;
  //               }
  ////               jc.generated_java += ")";
  //            }
  //         }
  //      }
  //   if (jc != null) {
  //         if (jc.active) {
  //            jc.type_of_operand = result_type;
  //      }
  //    }
  //
  //    }
  //   System.out.println("######### INSIDE RelOpExtended childrenAccept - end ###############");
  //   return data;
  //  }
}
