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

/* Generated By:JJTree: Do not edit this line. X_ForloopExpr.java */

package jsdai.expressCompiler;

import java.util.*;
import java.io.*;

import jsdai.lang.*;

public class X_ForloopExpr extends SimpleNode {

  boolean outer;

  String all_str;
  String bound1_str;
  String bound2_str;
  String increment_str;
  String while_str;
  String until_str;
  String stmt_str;
  String step_str;
  String end_str;

  String variable_uid;

  public X_ForloopExpr(int id) {
    super(id);
    bound1_str = "";
    bound2_str = "";
    increment_str = "";
    while_str = "";
    until_str = "";
    step_str = "";
    end_str = "";
    outer = false;
  }

  public X_ForloopExpr(Compiler2 p, int id) {
    super(p, id);
    bound1_str = "";
    bound2_str = "";
    increment_str = "";
    while_str = "";
    until_str = "";
    step_str = "";
    end_str = "";
    outer = false;
  }

  /**
   * Accept the visitor.
   **/
  public Object jjtAccept(Compiler2Visitor visitor, Object data) throws jsdai.lang.SdaiException {
    return visitor.visit(this, data);
  }

  public Object childrenAccept(Compiler2Visitor visitor, Object data) throws jsdai.lang.SdaiException {

    JavaClass jc = (JavaClass) data;
    boolean first_time = true;

    if (children != null) {

      // for contained statements
      variable_names = new Vector();
      variable_declarations = new Vector();
      statements = new Vector();
      initializing_code = new Vector();
      variable_names_tmp = new Vector();
      variable_declarations_tmp = new Vector();
      statements_tmp = new Vector();
      initializing_code_tmp = new Vector();

      stmt_str = "";
      increment_str = ";;";

      boolean increment_present = false;
      boolean while_present = false;
      boolean until_present = false;

      String return_generated_java_2 = "";

      for (int i = 0; i < children.length; ++i) {
        children[i].jjtAccept(visitor, data);

        if (jc != null) {
          if (jc.active) {
            if (children[i] instanceof X_IncrementControl) {

              increment_present = true;

              if (((SimpleNode) children[i]).java_contains_statements) {
                java_contains_statements = true;
                printDDebug("XPOORR B1: " + java_contains_statements, jc);

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
              }

              // integer case
              //                     increment_str = jc.java_str2 + ".getInteger();" + jc.java_str3 + ".getInteger();" + jc.java_str4;
              // Value case

							/*
									ok, making some changes:
									java_str2 - remains as it was
									java_str3 - the condition has to be generated here, 
									   java_str3 now will return only the bound2 expression to be assigned to a special variable before the loop
									   and to be used as for second parameter part
									java_str4 - has to be generated here, it will return now only the increment expression to be assigned to a spec variable
									before the loop and to be used  as for second and third parameter part, something like that:
									
									Value var_unique_id_end = java_str3;
									Value var_unique_id_step = java_str4;
									for (int j = start_expression; var_unique_id_step > 0 ? j <= var_unique_id_end : j >= var_unique_id_end; j += var_unique_id_step) {
									}
									
									  
							*/

              variable_uid = ((X_IncrementControl) children[i]).getVariable_uid();
              boolean step_present = ((X_IncrementControl) children[i]).increment_present;
              String end_name = "_end" + (++uid);
              String step_name = "_step" + (uid);  // let's use the same UID
              end_str = "Value " + end_name + " = Value.alloc(ExpressTypes.NUMBER_TYPE).set(" + jc.java_str3 + ");";
              if (jc.java_str4.equals("")) {
                step_str = "Value " + step_name + " = Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1);";
              }
              else {
                step_str = "Value " + step_name + " = Value.alloc(ExpressTypes.NUMBER_TYPE).set(" + jc.java_str4 + ");";
              }

//              jc.java_str3 = "Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal(_context, " + variable_uid + ", " + jc.generated_java + ").getLogical() == 2";
//                jc.java_str4 = variable_uid + ".inc(" + jc.generated_java + ")";

              // we can treat some integer literal cases as if step is not present, if we know that the step is > 0, for examlpe, when it is = 1

              if (step_str.endsWith(" = Value.alloc(ExpressTypes.NUMBER_TYPE).set(Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1));")) {
                step_present = false;
              }
              if (step_str.endsWith(" = Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 1);")) {
                step_present = false;
              }

              if (step_present) {
                increment_str = jc.java_str2 + ";" +

                    "Value.alloc(ExpressTypes.LOGICAL_TYPE).greater(_context, " + step_name
                    + ", Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 0)).getLogical() == 2 ? " // step_name > 0 ?
                    + "Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal(_context, " + variable_uid + ", " + end_name + ").getLogical() == 2 : "
                    // j <= end_name :
                    + "Value.alloc(ExpressTypes.LOGICAL_TYPE).gequal(_context, " + variable_uid + ", " + end_name + ").getLogical() == 2;"
                    // j >= end_name;
                    + variable_uid + ".inc(" + step_name + ")";
              }
              else {
                // when step is not present, assume 1, it is non-negative, and old style code perhaps could be used.
//               increment_str = jc.java_str2 + ";" + jc.java_str3 + ";" + jc.java_str4;  // old stuff
                increment_str = jc.java_str2 + ";" +

                    "Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal(_context, " + variable_uid + ", " + end_name + ").getLogical() == 2;" +
                    variable_uid + ".inc(" + step_name + ")";

              }
              jc.java_str2 = "";
              jc.java_str3 = "";
              jc.java_str4 = "";
            }
            else if (children[i] instanceof X_WhileControl) {

              while_present = true;

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
              }

              while_str = "if (!(" + jc.generated_java + ".getLogical() == 2)) break;";
            }
            else if (children[i] instanceof X_UntilControl) {

              until_present = true;

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
              }

              until_str = "if (" + jc.generated_java + ".getLogical() == 2) break;";

            }
            else if (children[i] instanceof X_Expression) {
              //  RETURN expression
              return_generated_java_2 = jc.generated_java;

              stmt_str += ((SimpleNode) children[i]).forwarded_java;

              if (((SimpleNode) children[i]).java_contains_statements) {

                printDDebug("XPOORR B1B repeat - statements: " + java_contains_statements, jc);
                for (int j = 0; j < ((SimpleNode) children[i]).variable_names.size(); j++) {
                  variable_names_tmp.add(((SimpleNode) children[i]).variable_names.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).variable_declarations.size(); j++) {
                  variable_declarations_tmp.add(((SimpleNode) children[i]).variable_declarations.elementAt(j));
                  stmt_str += "\n" + jc.print_tabs + (String) ((SimpleNode) children[i]).variable_declarations.elementAt(j);
                }

                for (int j = 0; j < ((SimpleNode) children[i]).statements.size(); j++) {
                  statements_tmp.add(((SimpleNode) children[i]).statements.elementAt(j));
                  stmt_str += "\n" + jc.print_tabs + (String) ((SimpleNode) children[i]).statements.elementAt(j);
                }

                for (int j = 0; j < ((SimpleNode) children[i]).initializing_code.size(); j++) {
                  initializing_code_tmp.add(((SimpleNode) children[i]).initializing_code.elementAt(j));
                }
              } // if contains statements

              if (first_time) {

                //                        stmt_str += jc.print_tabs + jc.print_string;
//                stmt_str += jc.print_string;
                stmt_str += ((SimpleNode) children[i]).generated_java;
                ((SimpleNode) children[i]).generated_java = "";
//                jc.print_string = "";
                first_time = false;
              }
              else {
                stmt_str += jc.print_tabs + ((SimpleNode) children[i]).generated_java;
//                stmt_str += jc.print_tabs + jc.print_string;
                ((SimpleNode) children[i]).generated_java = "";
//                jc.print_string = "";
              }

            }
            else {
              // should NEVER occur
              printDDebug("XPOORR B1A repeat - statements: " + children[i], jc);
              stmt_str += ((SimpleNode) children[i]).forwarded_java;
              printDDebug("XPOORR B1AA repeat - statements: " + ((SimpleNode) children[i]).forwarded_java, jc);
              ((SimpleNode) children[i]).forwarded_java = "";

              if (((SimpleNode) children[i]).java_contains_statements) {

                printDDebug("XPOORR B1B repeat - statements: " + java_contains_statements, jc);
                for (int j = 0; j < ((SimpleNode) children[i]).variable_names.size(); j++) {
                  variable_names_tmp.add(((SimpleNode) children[i]).variable_names.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).variable_declarations.size(); j++) {
                  variable_declarations_tmp.add(((SimpleNode) children[i]).variable_declarations.elementAt(j));
                  stmt_str += "\n" + jc.print_tabs + (String) ((SimpleNode) children[i]).variable_declarations.elementAt(j);
                }

                for (int j = 0; j < ((SimpleNode) children[i]).statements.size(); j++) {
                  statements_tmp.add(((SimpleNode) children[i]).statements.elementAt(j));
                  stmt_str += "\n" + jc.print_tabs + (String) ((SimpleNode) children[i]).statements.elementAt(j);
                }

                for (int j = 0; j < ((SimpleNode) children[i]).initializing_code.size(); j++) {
                  initializing_code_tmp.add(((SimpleNode) children[i]).initializing_code.elementAt(j));
                }
              } // if contains statements
              // stmt_str += jc.generated_java + ";\n";
              if (first_time) {

                //                        stmt_str += jc.print_tabs + jc.print_string;
//                stmt_str += jc.print_string;
                stmt_str += ((SimpleNode) children[i]).generated_java;
                ((SimpleNode) children[i]).generated_java = "";
//                jc.print_string = "";
                first_time = false;
              }
              else {
                stmt_str += jc.print_tabs + ((SimpleNode) children[i]).generated_java;
//                stmt_str += jc.print_tabs + jc.print_string;
                ((SimpleNode) children[i]).generated_java = "";
//                jc.print_string = "";
              }
            }
          }
        }
      } // for - i children

      // let's generate everything here

      String result_name = "_result" + (++uid);
      String variable_declaration = "Value " + result_name + " = Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create();";

      statements.addElement(variable_declaration);

//	    statements.addElement("\tfor (int " + for_index + " = 1; " + for_index + " <= " + str_in_expression + ".getMemberCount(); " + for_index + "++) {");



			/*
					increment/while/until are all optional and may be present one, two or all three at once
					although it may not be suitable for FOR loop expression not to have increment control at all
					
					First, let's implement the case when increment control is present, and only increment control
					
			*/

      String for_string_whole = "";

      String return_expression_whole = return_generated_java_2;

      if (increment_present) {

        statements.addElement(end_str);
        statements.addElement(step_str);
        for_string_whole = "for (" + increment_str + ") {";
        statements.addElement(for_string_whole);

//		      statements.addElement("\t\t\t" + result_name + ".set(_context, Value.alloc().addOrUnionOrConcatenate(_context, " + result_name + ", (" + return_expression_whole + ")));");

        if (outer) {
          statements.addElement("\t\t\t" + result_name + ".unionEnlarge((" + return_generated_java_2 + "), _context);");
        }
        else {
          statements.addElement(
              "\t\t\t" + result_name + ".set(_context, Value.alloc().addOrUnionOrConcatenate(_context, " + result_name + ", (" + return_expression_whole
                  + ")));");
        }

        statements.addElement("}");

      }

      java_contains_statements = true;

      if (jc != null) {
        if (jc.active) {
          jc.generated_java = result_name;
        }
      }
      generated_java = "";

    }

    return data;
  }

  public Object childrenAccept_abandoned(Compiler2Visitor visitor, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (children != null) {

      Vector increment_variable_names = new Vector();
      Vector increment_variable_declarations = new Vector();
      Vector increment_statements = new Vector();
      Vector increment_initializing_code = new Vector();
      Vector increment_forwarded_stuff = new Vector();

      Vector return_variable_names = new Vector();
      Vector return_variable_declarations = new Vector();
      Vector return_statements = new Vector();
      Vector return_initializing_code = new Vector();
      Vector return_forwarded_stuff = new Vector();

      String increment_forwarded_java = "";
      String increment_opening_java = "";
      String increment_generated_java = "";
      String increment_generated_java_2 = "";
      boolean increment_move_up_java_statements = false;
      boolean increment_print2string_activated = false;
      boolean increment_java_contains_statements = false;

      String return_forwarded_java = "";
      String return_opening_java = "";
      String return_generated_java = "";
      String return_generated_java_2 = "";
      boolean return_move_up_java_statements = false;
      boolean return_print2string_activated = false;
      boolean return_java_contains_statements = false;

      boolean increment_control_present = false;

      for (int i = 0; i < children.length; ++i) {
        children[i].jjtAccept(visitor, data);

        if (jc != null) {
          if (jc.active) {

            if (children[i] instanceof X_IncrementControl) {

              increment_control_present = true;

              if (((SimpleNode) children[i]).java_contains_statements) {
                increment_java_contains_statements = true;

                // variable_declaration += "\n" + ((SimpleNode)children[i]).variable_declaration;
                for (int j = 0; j < ((SimpleNode) children[i]).variable_names.size(); j++) {
                  increment_variable_names.add(((SimpleNode) children[i]).variable_names.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).variable_declarations.size(); j++) {
                  increment_variable_declarations.add(((SimpleNode) children[i]).variable_declarations.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).statements.size(); j++) {
                  increment_statements.add(((SimpleNode) children[i]).statements.elementAt(j));
                }

                // initializing_code += "\n" + ((SimpleNode)children[i]).initializing_code;
                for (int j = 0; j < ((SimpleNode) children[i]).initializing_code.size(); j++) {
                  increment_initializing_code.add(((SimpleNode) children[i]).initializing_code.elementAt(j));
                }

              } // if contains_statements

              increment_generated_java_2 = jc.generated_java;
              increment_generated_java = ((SimpleNode) children[i]).generated_java;
              increment_forwarded_java = ((SimpleNode) children[i]).forwarded_java;
              increment_opening_java = ((SimpleNode) children[i]).opening_java;
              increment_forwarded_stuff = ((SimpleNode) children[i]).forwarded_stuff;

            }
            else if (children[i] instanceof X_Expression) {
              // RETURN expression, at least should be

              if (((SimpleNode) children[i]).java_contains_statements) {
                return_java_contains_statements = true;

                // variable_declaration += "\n" + ((SimpleNode)children[i]).variable_declaration;
                for (int j = 0; j < ((SimpleNode) children[i]).variable_names.size(); j++) {
                  return_variable_names.add(((SimpleNode) children[i]).variable_names.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).variable_declarations.size(); j++) {
                  return_variable_declarations.add(((SimpleNode) children[i]).variable_declarations.elementAt(j));
                }

                for (int j = 0; j < ((SimpleNode) children[i]).statements.size(); j++) {
                  return_statements.add(((SimpleNode) children[i]).statements.elementAt(j));
                }

                // initializing_code += "\n" + ((SimpleNode)children[i]).initializing_code;
                for (int j = 0; j < ((SimpleNode) children[i]).initializing_code.size(); j++) {
                  return_initializing_code.add(((SimpleNode) children[i]).initializing_code.elementAt(j));
                }

              } // if contains_statements

              return_generated_java_2 = jc.generated_java;
              return_generated_java = ((SimpleNode) children[i]).generated_java;
              return_forwarded_java = ((SimpleNode) children[i]).forwarded_java;
              return_opening_java = ((SimpleNode) children[i]).opening_java;

              return_forwarded_stuff = ((SimpleNode) children[i]).forwarded_stuff;

            }
            else {
              // not all loop forms supported yet?

              System.out.println("<ForLoopExpr> TODO - child node: " + children[i]);

            }

          } // jc.active
        } // jc != null

      } // for - i - children

      System.out.println("<FOR loop NODE> increment generated_java_2 : " + increment_generated_java_2);
      System.out.println("<FOR loop NODE> RETURN generated_java_2 : " + return_generated_java_2);

      System.out.println("<FOR loop NODE> increment generated_java : " + increment_generated_java);
      System.out.println("<FOR loop NODE> RETURN generated_java : " + return_generated_java);

      System.out.println("<FOR loop NODE> increment forwarded_java : " + increment_forwarded_java);
      System.out.println("<FOR loop NODE> RETURN forwarded_java : " + return_forwarded_java);

      System.out.println("<FOR loop NODE> increment opening_java : " + increment_opening_java);
      System.out.println("<FOR loop NODE> RETURN opening_java : " + return_opening_java);

      System.out.println("<FOR loop NODE> increment forwarded_stuff : " + increment_forwarded_stuff);
      System.out.println("<FOR loop NODE> RETURN forwarded_stuff : " + return_forwarded_stuff);

      System.out.println("<FOR loop NODE> increment variable_names : " + increment_variable_names);
      System.out.println("<FOR loop NODE> RETURN variable_names : " + return_variable_names);

      System.out.println("<FOR loop NODE> increment variable_declarations : " + increment_variable_declarations);
      System.out.println("<FOR loop NODE> RETURN variable_declarations : " + return_variable_declarations);

      System.out.println("<FOR loop NODE> increment statements : " + increment_statements);
      System.out.println("<FOR loop NODE> RETURN statements : " + return_statements);

      System.out.println("<FOR loop NODE> increment initializing_code : " + increment_initializing_code);
      System.out.println("<FOR loop NODE> RETURN initializing_code : " + return_initializing_code);

			/*
			
								names := FOR i := 1 TO 10 RETURN all_names[i];

			
							<FOR loop NODE> increment generated_java_2 : Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 10)
							<FOR loop NODE> RETURN generated_java_2 : _e_all_names.indexing(_implicit_1_i, null)

			
			*/

    } // children NOT null

    return data;
  } // method
}
