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

// %modified: 1016210368804 %

/* Generated By:JJTree: Do not edit this line. X_Interval.java */
package jsdai.expressCompiler;

import java.util.*;

public class X_Interval
    extends ExpressionNode {

  int operator1 = 0;
  int operator2 = 0;
  String str_interval = "";
  String str_interval_low;
  String str_interval_op1;
  String str_interval_item;
  String str_interval_op2;
  String str_interval_high;
  String str_tmp;

  // public class X_Interval extends SimpleNode {
  public X_Interval(int id) {
    super(id);
  }

  public X_Interval(Compiler2 p, int id) {
    super(p, id);
  }

  /**
   * Accept the visitor.
   **/
  public Object jjtAccept(Compiler2Visitor visitor, Object data)
      throws jsdai.lang.SdaiException {
    return visitor.visit(this, data);
  }

  public Object childrenAccept(Compiler2Visitor visitor, Object data)
      throws jsdai.lang.SdaiException {
    JavaClass jc = (JavaClass) data;

    if (children != null) {

      // for contained statements
      variable_names = new Vector();
      variable_declarations = new Vector();
      statements = new Vector();
      initializing_code = new Vector();

      for (int i = 0; i < children.length; ++i) {
        children[i].jjtAccept(visitor, data);

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

            //                     actual_parameters.addElement(jc.generated_java);

            //  { IntervalLow IntervalOp IntervalItem IntervalOp IntervalHigh }
            // {0.0 <= red <= 1.0};
            // ((0.0 <= red) AND (red <= 1.0));

            //   (red >= 0) && (red <= 1)
            //   (item op1 low) && (item op2 high)

            if (jc.print_string.length() == 0) {
              str_tmp = jc.generated_java;
            }
            else {
              str_tmp = jc.print_string;
            }

            switch (i) {
              case 0:
                str_interval_low = str_tmp;
//									System.out.println("III low: " + str_interval_low + ", node: " + children[i]);
                break;
              case 1:
                if (children[i] instanceof X_IntervalOp) {
                  operator1 = ((X_IntervalOp) children[i]).operator;
                }
                else {
                  // error
                  operator1 = -2;
                }
                if (operator1 == 1) {
                  str_interval_op1 = "Value.alloc(ExpressTypes.LOGICAL_TYPE).less";
                }
                else if (operator1 == 2) {
                  str_interval_op1 = "Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal";
                }
                else {
                  str_interval_op1 = "_UNKNOWN_INTERVAL_OPERATOR_";
                }
//									System.out.println("III op1: " + operator1 + ", node: " + children[i]);
                break;
              case 2:
                str_interval_item = str_tmp;
//									System.out.println("III item: " + str_interval_item + ", node: " + children[i]);
                break;
              case 3:
                if (children[i] instanceof X_IntervalOp) {
                  operator2 = ((X_IntervalOp) children[i]).operator;
                }
                else {
                  // error
                  operator2 = -2;
                }
                if (operator2 == 1) {
                  str_interval_op2 = "Value.alloc(ExpressTypes.LOGICAL_TYPE).less";
                }
                else if (operator2 == 2) {
                  str_interval_op2 = "Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal";
                }
                else {
                  str_interval_op2 = "_UNKNOWN_INTERVAL_OPERATOR_";
                }
//									System.out.println("III op2: " + operator2 + ", node: " + children[i]);
                break;
              case 4:
                str_interval_high = str_tmp;
//									System.out.println("III high: " + str_interval_high + ", node: " + children[i]);
                break;
              default:
//									System.out.println("III error, i: " + i + ", " + children[i]);
                break;
            } // switch

          }
        }
      } // for children

      if (jc != null) {
        if (jc.active) {

          //   (item op1 low) && (item op2 high)
//						str_interval = "(" + str_interval_item + str_interval_op1 + str_interval_low + ") && (" + str_interval_item + str_interval_op2 + str_interval_high + ")";
//						System.out.println("III result: " + str_interval);

/*
				_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).AND(
					_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).lequal(
						_context, Value.alloc(ExpressTypes.REAL_TYPE).set(_context, 0.0), 
						Value.alloc(ExpressTypes.REAL_TYPE).set(_context, get(a0$))
					), Value.alloc(ExpressTypes.LOGICAL_TYPE).less(
						_context, Value.alloc(ExpressTypes.REAL_TYPE).set(
							_context, get(a0$)
						), Value.alloc(ExpressTypes.REAL_TYPE).set(_context, 1.0)
					)
				)
*/

// Value.alloc(ExpressTypes.REAL_TYPE).set(_context, 0.0), node: IntervalLow
// III item: Value.alloc(ExpressTypes.REAL_TYPE).set(_context, get(a2$)), node: IntervalItem      
// III high: Value.alloc(ExpressTypes.REAL_TYPE).set(_context, 1.0), node: IntervalHigh

          str_interval = "Value.alloc(ExpressTypes.LOGICAL_TYPE).AND(" +
              "_context, " + str_interval_op1 + "(" +
              "_context, " + str_interval_low + ", " +
              str_interval_item + "), " +
              str_interval_op2 + "(" +
              "_context, " + str_interval_item + ", " +
              str_interval_high + "))";

          jc.generated_java = str_interval;

//						index_chain = jc.generated_java;
          // jc.generated_java = jc.generated_java + ".indexing(" + index[0] + ", " +index[1] + ")";
        } // jc.active

      } // jc != null

    }

    return data;
  }

}