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

// %modified: 1016210368920 %

/* Generated By:JJTree: Do not edit this line. X_AddLikeOp.java */
package jsdai.expressCompiler;

import java.util.*;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

/*
"+"   value = 1 - public void addOrUnion(Value val1, Value val2)
   "-"   value = 2 - public void substractOrDifference(Value val1, Value val2)
   <OR>  value = 3 - public void OR(Value val1, Value val2)
   <XOR> value = 4 - public void XOR(Value val1, Value val2)
 */
public class X_AddLikeOp extends ExpressionNode {
  //public class X_AddLikeOp extends SimpleNode {
  int op_count = 0;
  ArrayList operations; //  int[] operations;
  ArrayList operands; //  String[] operands;
  ArrayList operand_types; //  int[] operand_types;
  ArrayList exact_operand_types; // EEntity[] exact_operand_types;

  public X_AddLikeOp(int id) {
    super(id);

    operations = new ArrayList(); // operations = new int[20];
    operands = new ArrayList(); // operands = new String[20]; // we can know the exact number of operands, if not at this moment, then it may be done not in constructor.
    operand_types = new ArrayList(); // operand_types = new int[20];
    exact_operand_types = new ArrayList(); // exact_operand_types = new EEntity[20];
    op_count = 0;
  }

  public X_AddLikeOp(Compiler2 p, int id) {
    super(p, id);
    operations = new ArrayList(); // operations = new int[20];
    operands = new ArrayList(); // operands = new String[20];
    operand_types = new ArrayList(); // operand_types = new int[20];
    exact_operand_types = new ArrayList(); // exact_operand_types = new EEntity[20];
    op_count = 0;
  }

  public void printDebugMe() {
    System.out.println("<AddLikeOp> ===========================================================");
    System.out.println("<AddLikeOp> op_count: " + op_count);
    for (int i = 0; i < operations.size(); i++) {
      System.out.println("<AddLikeOp> operations[" + i + "]: " + operations.get(i));
    }
    for (int i = 0; i < operands.size(); i++) {
      System.out.println("<AddLikeOp> operands[" + i + "]: " + operands.get(i));
    }
    for (int i = 0; i < operand_types.size(); i++) {
      System.out.println("<AddLikeOp> operand_types[" + i + "]: " + operand_types.get(i));
    }
    for (int i = 0; i < operand_types.size(); i++) {
      System.out.println("<AddLikeOp> exact_operand_types[" + i + "]: " + exact_operand_types.get(i));
    }
    System.out.println("<AddLikeOp> -----------------------------------------------------------");

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
    printActive("XP childrenAccept starting - AddLikeOp: " + id + ", parent: " +
        ((SimpleNode) parent).id, jc);

    String op_type_str = "default";
    String operator_str = "DefaultAddOperator";
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

              printActive("XP childrenAccept  - AddLikeOp - operand nr: " + i +
                  ", value: " + jc.generated_java, jc);
//              operand_types.set(i, new Integer(jc.type_of_operand));
              // operand_types[i] = jc.type_of_operand; // may be no longer needed

              if (jc.type_of_operand >= JavaClass.T_AGGREGATE) { // may be no longer needed
//                exact_operand_types.set(i, jc.type_of_aggregate);
                // exact_operand_types[i] = jc.type_of_aggregate; // may be no longer needed
              }

              jc.generated_java = "";
            } // jc active
          } // jc not null
        } // for - loop through children
      } // if children

      if (jc != null) {
        if (jc.active) {
          // let's say here we analyze the types of operands, and if numeric, then:
          //               jc.generated_java = "(";

          if (operands.size() > 0) { // protection against exception (temporary measure? internal error?)
            jc.generated_java = (String) operands.get(0);
          }
          // jc.generated_java = operands[0];

          // operand_1_type = operand_types[0]; // probably not needed
          for (int i = 0; i < (op_count - 1); ++i) {
            // operand_2_type = operand_types[i+1]; // probably not needed
            // System.out.println("###_#- Operand 1: " + operand_1_type + ", operand 2: " + operand_2_type);

            switch (((Integer) operations.get(i)).intValue()) {
//            switch (operations[i]) {
              case 1:
                operator_str = "addOrUnionOrConcatenate";

                break;

              case 2:
                operator_str = "substractOrDifference";

                break;

              case 3:
                operator_str = "OR";

                break;

              case 4:
                operator_str = "XOR";

                break;

              default:
                // System.out.println("XP - AddLikeOP - default operator 2");
                operator_str = "addLikeDefault";

                break;
            }

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
              // OR/XOR only for logical operators?

            }
            else if ((((Integer) operations.get(i)).intValue() == 3) || (((Integer) operations.get(i)).intValue() == 4)) {
//            } else if ((operations[i] == 3) || (operations[i] == 4)) {
              alloc_type_str = "ExpressTypes.LOGICAL_TYPE";
            }
            else {
              alloc_type_str = "";
            }

            //                if (j.flag_non_temporary_value_instance) {
            // System.out.println("XC assignment AddLikeOp indent: " + jc.indent + ", left: " + jc.value_instance);
            // System.out.println("RRRRXC assignment AddLikeOp indent: " + jc.indent + ", depth: " + jc.assignment_depth + ", children: " + children.length + ", op count: "+ op_count + ", nontemp: " + jc.flag_non_temporary_value_instance );
            // System.out.println("RRRRXC2: " + jc.generated_java);
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

            if (jc.generated_java.equals("_JUST_CREATE_")) { // aggregate initializer

              jc.generated_java = "Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create()";
            }

            ;

//            if (operands[i + 1].equals("_JUST_CREATE_")) { // aggregate initializer
            if (((String) operands.get(i + 1)).equals("_JUST_CREATE_")) { // aggregate initializer

//              operands[i + 1] = "Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create()";
              operands.set(i + 1, "Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create()");
            }

            ;

//RR-SdaiContext - assuming that XOR and OR also will have _context, if not - to change the implementation
//RR-SdaiContext   easy - to move ( to the operator string itself

//            jc.generated_java = value_instance_string + operator_str + "(_context, " + jc.generated_java + ", " + operands[i + 1] + ")";
            jc.generated_java = value_instance_string + operator_str + "(_context, " + jc.generated_java + ", " + (String) operands.get(i + 1) + ")";
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
    printActive("XP childrenAccept ending - AddLikeOp: " + id + ", parent: " +
        ((SimpleNode) parent).id, jc);

    return data;
  }

  /*
  public Object childrenAccept(Compiler2Visitor visitor, Object data)  throws SdaiException {
     JavaClass jc = (JavaClass)data;
     if (children != null) {
        for (int i = 0; i < children.length; ++i) {
           children[i].jjtAccept(visitor, data);
           if (jc != null) {
              if (jc.active) {
                 operands[i] = jc.generated_java;   
                 jc.generated_java = "";
              }
           }   
        }
        if (jc != null) {
           if (jc.active) {
              // let's say here we analyze the types of operands, and if numeric, then:
              jc.generated_java = "(";
              for (int i = 0; i < children.length; ++i) {
                 jc.generated_java += operands[i];
                 switch (operations[i]) {
                    case 1:
                       jc.generated_java += "+";
                       break;
                    case 2:
                       jc.generated_java += "-";
                       break;
                    default: 
                       break;
                 }
              }
              jc.generated_java += ")";
           }
        }   
     }
   return data;
  }
   */
  EEntity testBagBag(EBag_type op1, EBag_type op2) throws SdaiException {
    // if element types of op1 and op2 aggregates are compatible, then return the result member type - supertype?
    EEntity el_type1 = op1.getElement_type(null);
    EEntity el_type2 = op2.getElement_type(null);
    EEntity el_type12;
    EEntity el_type22;
    Class class1 = el_type1.getClass();
    Class class2 = el_type2.getClass();
    Class class12 = null;
    Class class22 = null;
    EEntity an_ss;
    EEntity ass;
    an_ss = el_type1;

    for (; ; ) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        ass = an_ss;
        an_ss = ((EDefined_type) an_ss).getDomain(null);
        done_something = true;
      }
      else if (an_ss instanceof EAggregation_type) {
        // aggr_prefices += "a";
        // aggregate_depth++;
        // System.out.println("## IN AGGREGATE");
        ass = an_ss;

        if (ass instanceof EArray_type) {
          // System.out.println("## IN ARRAY");
          // not compatible
          return null;
        }
        else if (ass instanceof EBag_type) {
          // System.out.println("## IN BAG");
        }
        else if (ass instanceof ESet_type) {
          // System.out.println("## IN SET");
        }
        else if (ass instanceof EList_type) {
          // System.out.println("## IN LIST");
          // not compatible
          return null;
        }

        an_ss = ((EAggregation_type) an_ss).getElement_type(null);
        done_something = true;
      }

      if (!done_something) {
        break;
      }
    }

    el_type12 = an_ss;
    an_ss = el_type2;

    for (; ; ) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        ass = an_ss;
        an_ss = ((EDefined_type) an_ss).getDomain(null);
        done_something = true;
      }
      else if (an_ss instanceof EAggregation_type) {
        // aggr_prefices += "a";
        // aggregate_depth++;
        ass = an_ss;

        if (ass instanceof EArray_type) {
          // not compatible
          return null;
        }
        else if (ass instanceof EBag_type) {
        }
        else if (ass instanceof ESet_type) {
        }
        else if (ass instanceof EList_type) {
          // not compatible
          return null;
        }

        an_ss = ((EAggregation_type) an_ss).getElement_type(null);
        done_something = true;
      }

      if (!done_something) {
        break;
      }
    }

    el_type22 = an_ss;
    class12 = el_type12.getClass();
    class22 = el_type22.getClass();

    // System.out.println("## Class12: " + class12.getName() + ", Class22: " + class22.getName());
    if (el_type12 == el_type22) {
      return op1;
    }
    else if (class12.isAssignableFrom(class22)) {
      return op1;
    }
    else if (class22.isAssignableFrom(class12)) {
      return op2;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof ENumber_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof ENumber_type)) {
      return op1;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof EReal_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof EReal_type)) {
      return op1;
    }

    if ((el_type12 instanceof EBoolean_type) && (el_type22 instanceof ELogical_type)) {
      return op2;
    }

    if ((el_type22 instanceof EBoolean_type) && (el_type12 instanceof ELogical_type)) {
      return op1;
    }

    if (el_type1 == el_type2) {
      return op1;
    }
    else if (class1.isAssignableFrom(class2)) {
      return op1;
    }
    else if (class2.isAssignableFrom(class1)) {
      return op2;
    }

    return null;
  }

  EEntity testBagSet(EBag_type op1, ESet_type op2) throws SdaiException {
    // if element types of op1 and op2 aggregates are compatible, then return the result member type - supertype?
    EEntity el_type1 = op1.getElement_type(null);
    EEntity el_type2 = op2.getElement_type(null);
    EEntity el_type12;
    EEntity el_type22;
    Class class1 = el_type1.getClass();
    Class class2 = el_type2.getClass();
    Class class12 = null;
    Class class22 = null;
    EEntity an_ss1;
    EEntity an_ss2;
    EEntity ass1;
    EEntity ass2;
    boolean make_shift;
    boolean shift_made;
    an_ss1 = el_type1;
    an_ss2 = el_type2;
    shift_made = false;
    make_shift = false;

    for (; ; ) {
      boolean done_something = false;

      while (an_ss1 instanceof EDefined_type) {
        ass1 = an_ss1;
        an_ss1 = ((EDefined_type) an_ss1).getDomain(null);

        // done_something = true;
      }

      while (an_ss2 instanceof EDefined_type) {
        ass2 = an_ss2;
        an_ss2 = ((EDefined_type) an_ss2).getDomain(null);

        // done_something = true;
      }

      // bag of ... - set of ... Here,  set may be an element of the bag, in the case: bag of set of integer - set of integer.
      // a more complicated case: bag of set of bag of set of integer - set of bag of set of integer
      if (an_ss1 instanceof EAggregation_type) {
        // op1 is the bag, op2 may be element, so if op1 is aggregate, op2 may be aggregate or not.
        // if compatible - ok.
        // if not compatible - try if maybe element, get element type for op1, if already done that once - then not compatible.
        if (!(an_ss2 instanceof EAggregation_type)) {
          // not aggregation type - let's try if maybe element
          if (!shift_made) {
            an_ss1 = ((EAggregation_type) an_ss1).getElement_type(null);
            make_shift = false;
            shift_made = true;
            done_something = true;
          }
          else {
            return null;
          }
        }
        else {
          // op2 is aggregate
          if (((an_ss1 instanceof EBag_type) && (an_ss2 instanceof EList_type)) ||
              ((an_ss1 instanceof EBag_type) && (an_ss2 instanceof EArray_type)) ||
              ((an_ss1 instanceof ESet_type) && (an_ss2 instanceof EList_type)) ||
              ((an_ss1 instanceof ESet_type) && (an_ss2 instanceof EArray_type)) ||
              ((an_ss1 instanceof EList_type) && (an_ss2 instanceof EArray_type)) ||
              ((an_ss1 instanceof EList_type) && (an_ss2 instanceof EBag_type)) ||
              ((an_ss1 instanceof EList_type) && (an_ss2 instanceof ESet_type)) ||
              ((an_ss1 instanceof EArray_type) && (an_ss2 instanceof EBag_type)) ||
              ((an_ss1 instanceof EArray_type) && (an_ss2 instanceof EList_type)) ||
              ((an_ss1 instanceof EArray_type) && (an_ss2 instanceof ESet_type))) {
            if (!shift_made) {
              make_shift = true;
            }
            else {
              return null;
            }
          }

          if ((an_ss1 instanceof EArray_type) && (an_ss2 instanceof EArray_type)) {
            // check if bounds are identical (or maybe population-dependent?) If bound are not identical - make_shift.
          }

          an_ss1 = ((EAggregation_type) an_ss1).getElement_type(null);

          if (!make_shift) {
            an_ss2 = ((EAggregation_type) an_ss2).getElement_type(null);
          }
          else {
            shift_made = true;
            make_shift = false;
          }

          done_something = true;
        }
      }
      else if (an_ss2 instanceof EAggregation_type) {
        return null;
      }

      if (!done_something) {
        break;
      }
    } // for

    el_type12 = an_ss1;
    el_type22 = an_ss2;
    class12 = el_type12.getClass();
    class22 = el_type22.getClass();

    // System.out.println("## Class12: " + class12.getName() + ", Class22: " + class22.getName());
    if (el_type12 == el_type22) {
      return op1;
    }
    else if (class12.isAssignableFrom(class22)) {
      return op1;
    }
    else if (class22.isAssignableFrom(class12)) {
      return op2;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof ENumber_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof ENumber_type)) {
      return op1;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof EReal_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof EReal_type)) {
      return op1;
    }

    if ((el_type12 instanceof EBoolean_type) && (el_type22 instanceof ELogical_type)) {
      return op2;
    }

    if ((el_type22 instanceof EBoolean_type) && (el_type12 instanceof ELogical_type)) {
      return op1;
    }

    if (el_type1 == el_type2) {
      return op1;
    }
    else if (class1.isAssignableFrom(class2)) {
      return op1;
    }
    else if (class2.isAssignableFrom(class1)) {
      return op2;
    }

    return null;

    //      }
    // what if there element types are defined type chains, etc, pointing to the same type.
    // or defined types having the same underlying type
    // or defined types which underlying types are subtype-supertype
    // or defined types which underlying types are - look above - maybe some more complications are possible.
    // another thing - if the type of element is aggregate again, either directly, or after a defined type chain,
    // it is important to look deaper to see what is the type of that aggregate, to see if they are compatible.
    // not very clear is the type of element aggregate itself, for example, for operation with sets, does
    // set of set of cartesian_point is compatible with set of array of cartesian_point? - if we look at the final types of elements, then yes,
    // but that may be wrong.
  }

  EEntity testBagSet_old(EBag_type op1, ESet_type op2)
      throws SdaiException {
    // if element types of op1 and op2 aggregates are compatible, then return the result member type - supertype?
    EEntity el_type1 = op1.getElement_type(null);
    EEntity el_type2 = op2.getElement_type(null);
    EEntity el_type12;
    EEntity el_type22;
    Class class1 = el_type1.getClass();
    Class class2 = el_type2.getClass();
    Class class12 = null;
    Class class22 = null;
    EEntity an_ss;
    EEntity ass;
    an_ss = el_type1;

    for (; ; ) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        ass = an_ss;
        an_ss = ((EDefined_type) an_ss).getDomain(null);
        done_something = true;
      }
      else if (an_ss instanceof EAggregation_type) {
        // aggr_prefices += "a";
        // aggregate_depth++;
        // System.out.println("## IN AGGREGATE");
        ass = an_ss;

        if (ass instanceof EArray_type) {
          // System.out.println("## IN ARRAY");
          // not compatible
          return null;
        }
        else if (ass instanceof EBag_type) {
          // System.out.println("## IN BAG");
        }
        else if (ass instanceof ESet_type) {
          // System.out.println("## IN SET");
        }
        else if (ass instanceof EList_type) {
          // System.out.println("## IN LIST");
          // not compatible
          return null;
        }

        an_ss = ((EAggregation_type) an_ss).getElement_type(null);
        done_something = true;
      }

      if (!done_something) {
        break;
      }
    }

    el_type12 = an_ss;
    an_ss = el_type2;

    for (; ; ) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        ass = an_ss;
        an_ss = ((EDefined_type) an_ss).getDomain(null);
        done_something = true;
      }
      else if (an_ss instanceof EAggregation_type) {
        // aggr_prefices += "a";
        // aggregate_depth++;
        ass = an_ss;

        if (ass instanceof EArray_type) {
          // not compatible
          return null;
        }
        else if (ass instanceof EBag_type) {
        }
        else if (ass instanceof ESet_type) {
        }
        else if (ass instanceof EList_type) {
          // not compatible
          return null;
        }

        an_ss = ((EAggregation_type) an_ss).getElement_type(null);
        done_something = true;
      }

      if (!done_something) {
        break;
      }
    }

    el_type22 = an_ss;
    class12 = el_type12.getClass();
    class22 = el_type22.getClass();

    // System.out.println("## Class12: " + class12.getName() + ", Class22: " + class22.getName());
    if (el_type12 == el_type22) {
      return op1;
    }
    else if (class12.isAssignableFrom(class22)) {
      return op1;
    }
    else if (class22.isAssignableFrom(class12)) {
      return op2;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof ENumber_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof ENumber_type)) {
      return op1;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof EReal_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof EReal_type)) {
      return op1;
    }

    if ((el_type12 instanceof EBoolean_type) && (el_type22 instanceof ELogical_type)) {
      return op2;
    }

    if ((el_type22 instanceof EBoolean_type) && (el_type12 instanceof ELogical_type)) {
      return op1;
    }

    if (el_type1 == el_type2) {
      return op1;
    }
    else if (class1.isAssignableFrom(class2)) {
      return op1;
    }
    else if (class2.isAssignableFrom(class1)) {
      return op2;
    }

    return null;

    //      }
    // what if there element types are defined type chains, etc, pointing to the same type.
    // or defined types having the same underlying type
    // or defined types which underlying types are subtype-supertype
    // or defined types which underlying types are - look above - maybe some more complications are possible.
    // another thing - if the type of element is aggregate again, either directly, or after a defined type chain,
    // it is important to look deaper to see what is the type of that aggregate, to see if they are compatible.
    // not very clear is the type of element aggregate itself, for example, for operation with sets, does
    // set of set of cartesian_point is compatible with set of array of cartesian_point? - if we look at the final types of elements, then yes,
    // but that may be wrong.
  }

  EEntity testBagList(EBag_type op1, EList_type op2) throws SdaiException {
    // if element types of op1 and op2 aggregates are compatible, then return the result member type - supertype?
    EEntity el_type1 = op1.getElement_type(null);
    EEntity el_type2 = op2.getElement_type(null);
    EEntity el_type12;
    EEntity el_type22;
    Class class1 = el_type1.getClass();
    Class class2 = el_type2.getClass();
    Class class12 = null;
    Class class22 = null;
    EEntity an_ss;
    EEntity ass;
    an_ss = el_type1;

    for (; ; ) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        ass = an_ss;
        an_ss = ((EDefined_type) an_ss).getDomain(null);
        done_something = true;
      }
      else if (an_ss instanceof EAggregation_type) {
        // aggr_prefices += "a";
        // aggregate_depth++;
        // System.out.println("## IN AGGREGATE");
        ass = an_ss;

        if (ass instanceof EArray_type) {
          // System.out.println("## IN ARRAY");
          // not compatible
          return null;
        }
        else if (ass instanceof EBag_type) {
          // System.out.println("## IN BAG");
        }
        else if (ass instanceof ESet_type) {
          // System.out.println("## IN SET");
        }
        else if (ass instanceof EList_type) {
          // System.out.println("## IN LIST");
          // not compatible
          return null;
        }

        an_ss = ((EAggregation_type) an_ss).getElement_type(null);
        done_something = true;
      }

      if (!done_something) {
        break;
      }
    }

    el_type12 = an_ss;
    an_ss = el_type2;

    for (; ; ) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        ass = an_ss;
        an_ss = ((EDefined_type) an_ss).getDomain(null);
        done_something = true;
      }
      else if (an_ss instanceof EAggregation_type) {
        // aggr_prefices += "a";
        // aggregate_depth++;
        ass = an_ss;

        if (ass instanceof EArray_type) {
          // not compatible
          return null;
        }
        else if (ass instanceof EBag_type) {
        }
        else if (ass instanceof ESet_type) {
        }
        else if (ass instanceof EList_type) {
          // not compatible
          return null;
        }

        an_ss = ((EAggregation_type) an_ss).getElement_type(null);
        done_something = true;
      }

      if (!done_something) {
        break;
      }
    }

    el_type22 = an_ss;
    class12 = el_type12.getClass();
    class22 = el_type22.getClass();

    // System.out.println("## Class12: " + class12.getName() + ", Class22: " + class22.getName());
    if (el_type12 == el_type22) {
      return op1;
    }
    else if (class12.isAssignableFrom(class22)) {
      return op1;
    }
    else if (class22.isAssignableFrom(class12)) {
      return op2;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof ENumber_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof ENumber_type)) {
      return op1;
    }

    if ((el_type12 instanceof EInteger_type) && (el_type22 instanceof EReal_type)) {
      return op2;
    }

    if ((el_type22 instanceof EInteger_type) && (el_type12 instanceof EReal_type)) {
      return op1;
    }

    if ((el_type12 instanceof EBoolean_type) && (el_type22 instanceof ELogical_type)) {
      return op2;
    }

    if ((el_type22 instanceof EBoolean_type) && (el_type12 instanceof ELogical_type)) {
      return op1;
    }

    if (el_type1 == el_type2) {
      return op1;
    }
    else if (class1.isAssignableFrom(class2)) {
      return op1;
    }
    else if (class2.isAssignableFrom(class1)) {
      return op2;
    }

    return null;

    //      }
    // what if there element types are defined type chains, etc, pointing to the same type.
    // or defined types having the same underlying type
    // or defined types which underlying types are subtype-supertype
    // or defined types which underlying types are - look above - maybe some more complications are possible.
    // another thing - if the type of element is aggregate again, either directly, or after a defined type chain,
    // it is important to look deaper to see what is the type of that aggregate, to see if they are compatible.
    // not very clear is the type of element aggregate itself, for example, for operation with sets, does
    // set of set of cartesian_point is compatible with set of array of cartesian_point? - if we look at the final types of elements, then yes,
    // but that may be wrong.
  }
}