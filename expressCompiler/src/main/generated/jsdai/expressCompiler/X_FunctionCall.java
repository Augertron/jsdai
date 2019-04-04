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

// %modified: 1016210367799 %



/*


instead of :

jsdai.SNested_f.FBbb.run(...)

need to generate:

(new jsdai.SNested_f.FAaa$Bbb()).run(...)


*/



/* Generated By:JJTree: Do not edit this line. X_FunctionCall.java */
package jsdai.expressCompiler;

import java.util.*;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

public class X_FunctionCall extends ExpressionNode {
  // public class X_FunctionCall extends SimpleNode {
  boolean is_builtin = true;
  int built_in_id;
  int argument_count;
  int actual_count;
  EFunction_definition fd;
  EEntity scope;
  Vector arguments; // probably not needed - vector from the parser
  Vector actual_parameters; // strings with argument expressions from the parse tree

  boolean outer_sizeof;
  boolean sizeof_is_warning;
  boolean sizeof_is_error;
  int sizeof_value;

  public X_FunctionCall(int id) {
    super(id);
  }

  public X_FunctionCall(Compiler2 p, int id) {
    super(p, id);
  }

  String getName(JavaClass jc) throws SdaiException {

    String name = "NAME_NOT_KNOWN";

    //      String built_in_package = "Express.";
    //    String built_in_package = "Value.alloc(ExpressTypes.GENERIC_TYPE).";
    String built_in_package = "Value.alloc(ExpressTypes.";

    switch (built_in_id) {
      case 1:

        //        name = built_in_package + "abs(";
        name = built_in_package + "NUMBER_TYPE).abs(";

        break;

      case 2:

        //        name = built_in_package + "aCos(";
        name = built_in_package + "REAL_TYPE).aCos(";

        break;

      case 3:

        //        name = built_in_package + "aSin(";
        name = built_in_package + "REAL_TYPE).aSin(";

        break;

      case 4:

        //        name = built_in_package + "aTan(";
        name = built_in_package + "REAL_TYPE).aTan(";

        break;

      case 5:

        //        name = built_in_package + "bLength(";
        name = built_in_package + "INTEGER_TYPE).bLength(";

        break;

      case 6:

        //        name = built_in_package + "cos(";
        name = built_in_package + "REAL_TYPE).cos(";

        break;

      case 7:

        //        name = built_in_package + "exists(";
        name = built_in_package + "BOOLEAN_TYPE).exists(";

        break;

      case 8:

        //        name = built_in_package + "exp(";
        name = built_in_package + "REAL_TYPE).exp(";

        break;

      case 9:

        //        name = built_in_package + "format(";
        name = built_in_package + "STRING_TYPE).format(";

        break;

      case 10:

        //        name = built_in_package + "hiBound(";
        name = built_in_package + "INTEGER_TYPE).hiBound(_context, ";

        break;

      case 11:

        //        name = built_in_package + "hiIndex(";
        name = built_in_package + "INTEGER_TYPE).hiIndex(_context, ";

        break;

      case 12:

        //        name = built_in_package + "length(";
        name = built_in_package + "INTEGER_TYPE).length(";

        break;

      case 13:

        //        name = built_in_package + "loBound(";
        name = built_in_package + "INTEGER_TYPE).loBound(";

        break;

      case 14:

        //        name = built_in_package + "loIndex(";
        name = built_in_package + "INTEGER_TYPE).loIndex(";

        break;

      case 15:

        //        name = built_in_package + "log(";
        name = built_in_package + "REAL_TYPE).log(";

        break;

      case 16:

        //        name = built_in_package + "log2(";
        name = built_in_package + "REAL_TYPE).log2(";

        break;

      case 17:

        //        name = built_in_package + "log10(";
        name = built_in_package + "REAL_TYPE).log10(";

        break;

      case 18:

        //        name = built_in_package + "NVL(";
        // here the return type and parameters  - the same label
        name = built_in_package + "GENERIC_TYPE).NVL(_context, ";

        break;

      case 19:

        //        name = built_in_package + "odd(";
        name = built_in_package + "LOGICAL_TYPE).odd(";

        break;

      case 20:

        //        name = built_in_package + "rolesOf(";
        name = built_in_package + "SET_STRING_TYPE).rolesOf(";

        break;

      case 21:

        //        name = built_in_package + "sin(";
        name = built_in_package + "REAL_TYPE).sin(";

        break;

      case 22:

        //            name = built_in_package + "Sizeof.";
        //        name = built_in_package + "sizeOf(";

        if ((outer_sizeof) && (sizeof_is_error)) {
          name = built_in_package + "INTEGER_TYPE).sizeOfExt0(_context, ";
        }
        else if ((outer_sizeof) && (sizeof_is_warning)) {
          name = built_in_package + "INTEGER_TYPE).sizeOfExt(_context, ";
        }
        else {
          name = built_in_package + "INTEGER_TYPE).sizeOf(_context, ";
        }
        break;

      case 23:

        //        name = built_in_package + "sqrt(";
        name = built_in_package + "REAL_TYPE).sqrt(";

        break;

      case 24:

        //        name = built_in_package + "tan(";
        name = built_in_package + "REAL_TYPE).tan(";

        break;

      case 25:

        //        name = "typeOfV(";
        // SET OF STRING return type
        // why without prefix at all? Is in deliberately done?
        // YES, it is done deliberately, this function doesn't take an argument
//      name = "typeOfV(_context, ";
        name = "typeOfV(_context";

        //        name = built_in_package + "SET_STRING_TYPE).typeOfV(";
        break;

      case 26:

        //        name = built_in_package + "usedIn(";
        name = built_in_package + "BAG_GENERIC_TYPE).usedIn(";

        break;

      case 27:

        //        name = built_in_package + "value(";
        name = built_in_package + "NUMBER_TYPE).value(";

        break;

      case 28:

        //        name = built_in_package + "value_in(";
        name = built_in_package + "LOGICAL_TYPE).value_in(_context, ";

        break;

      case 29:

        //        name = built_in_package + "value_unique(";
        name = built_in_package + "LOGICAL_TYPE).value_unique(_context, ";

        break;

      case 30: // Express-X only built-in function EXTENT

        name = built_in_package + "SET_GENERIC_TYPE).extent(_context, ";

        break;

      case 31: // adding pseudo-built-in bag_to_set

//					Tas metodas galetu butu pvz. toks:
//					public Value bagToSet(SdaiContext context, Value bag) throws SdaiException {
//					Cia 'this' butu geidziamoji aibe

        name = built_in_package + "SET_GENERIC_TYPE).bagToSet(_context, ";

        break;

      case -1: // user defined

        if (fd == null) {
          // error
          //          name = built_in_package + "NullError.";
          name = "NullError.";

          break;
        }

        is_builtin = false;

//      String the_name = fd.getName(null);
//      String a_name = "F" + the_name.substring(0, 1).toUpperCase() + the_name.substring(1).toLowerCase() + ".";

        String a_name = constructFunctionClassName(fd);
        name = constructFunctionSchemaClass(jc, a_name);
			/*
			
				  not so simple,
				  if the function is top level -  (),
				  if inner -  (this)
				  if inner invoking another inner on the same level - (parent)
					
					below is the implemenation for outer function.
					Question, how to recognize the 3rd case?
					we have the function to be invoked, how do we know from which level it is invoked,
					from the parent level or from a sibling level?
					if there is no simple way to know, I may add a field to jc for fd of the function which declaration is being processed,
					or better - for its node. Perhaps already there, check
					Yes, we already have it - 			jc.keynode = node;

					
			*/
        String constructor_argument = "";

        EEntity pfd = Support.getParentFunctionProcedureRuleDefinition(fd);
//System.out.println("==============================="); 
//System.out.println("fd: " + fd); 
//System.out.println("pfd: " + pfd); 
        if (pfd != null) {
          //  inner function, may also search for inner_declaration, if needed
          // here we have either parent or this.
//				EFunction_definition ifd = ((X_FunctionDecl)jc.keynode).fd;
//				EFunction_definition ifd = null;
//System.out.println("ifd: " + ifd); 
//System.out.println("ifd parent: " + Support.getParentFunctionDefinition(ifd)); 
          if (pfd == scope) {
            constructor_argument = "this";
          }
          else if (scope instanceof EAlgorithm_definition) {
            if (Support.getParentFunctionProcedureRuleDefinition((EAlgorithm_definition) scope) == pfd) {
              constructor_argument = "parent";
            }
            else {
//					constructor_argument = "weird";
            }
          }
          else {
          }

        }

        name = "(new " + name + "(" + constructor_argument + ")).run(";

        break;

      default:
        name = "_fDefault_error";

        break; // error
    }

    return name;
  }

  String constructFunctionClassName(EFunction_definition fd4) throws SdaiException {
    String result = "";
    EAlgorithm_definition fd2 = null;
    EEntity fd3 = null;
    String f_name = null;

    if (fd4 == null) {
      result = "_NULL_function_definition_";
      return result;
    }

    fd2 = (EAlgorithm_definition) fd4;

    result = fd2.getName(null);
    result = result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();

    for (; ; ) {
      if (fd2 == null) {
        fd3 = null;
      }
      else {
        fd3 = Support.getParentFunctionProcedureRuleDefinition(fd2);
      }
      if (fd3 == null) {
//				result = "P" + result;  -- why P here?
        result = "F" + result;
        break;
      }
      else {
        // still inner function, add names
        if (fd3 instanceof EAlgorithm_definition) {
          f_name = ((EAlgorithm_definition) fd3).getName(null);
          fd2 = (EAlgorithm_definition) fd3;
        }
        else if (fd3 instanceof EGlobal_rule) {
          f_name = ((EGlobal_rule) fd3).getName(null);
          fd2 = null;
        }
        else {
          // should not happen
          fd2 = null;
        }
        result = f_name.substring(0, 1).toUpperCase() + f_name.substring(1).toLowerCase() + "$" + result;
      }
    }

    return result;

  }

  // better to put such methods in one place
  String constructFunctionONLYClassName(EFunction_definition fd4) throws SdaiException {
    String result = "";
    EFunction_definition fd2 = null;
    EFunction_definition fd3 = null;
    String f_name = null;

    fd2 = fd4;

    if (fd2 == null) {
      result = "_NULL_function_definition_";
      return result;
    }

    result = fd2.getName(null);
    result = result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();

    for (; ; ) {
      fd3 = Support.getParentFunctionDefinition(fd2);
      if (fd3 == null) {
        result = "F" + result;
        break;
      }
      else {
        // still inner function, add names
        f_name = fd3.getName(null);
        result = f_name.substring(0, 1).toUpperCase() + f_name.substring(1).toLowerCase() + "$" + result;
        fd2 = fd3;
      }
    }

    return result;

  }

  /**
   * Accept the visitor.
   **/
  public Object jjtAccept(Compiler2Visitor visitor, Object data)
      throws SdaiException {
    return visitor.visit(this, data);
  }

  public Object childrenAccept(Compiler2Visitor visitor, Object data)
      throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (true) { // if the function has no parameters, then children = null, so this case also should be included
//    if (children != null) {
      // for contained statements
      variable_names = new Vector();
      variable_declarations = new Vector();
      statements = new Vector();
      initializing_code = new Vector();
      actual_count = 0;
      actual_parameters = new Vector();

      if (children != null) {
        for (int i = 0; i < children.length; ++i) {
          if (children[i] instanceof X_Parameter) {
            jc.current_function_parameter = actual_count + 1;
          }

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

              if (children[i] instanceof X_Parameter) {
                //                     actual_parameters.addElement(jc.generated_java);
                if (jc.print_string.length() == 0) {
                  actual_parameters.addElement(jc.generated_java);
                }
                else {
                  actual_parameters.addElement(jc.print_string);
                }

                jc.current_function_parameter = actual_count;
                actual_count++;
              }
              else {
                // must be an error
              }
            }
          }
        } // for children
      } // if has children
      if (jc != null) {
        if (jc.active) {
          // old - in one file               String tmp_str = constructFunctionSchemaClass(jc) + getName() + "(";
          //          is_builtin = true;
          String tmp_str = getName(jc);

          //                String express_parameter = "new Express(null, null, null)";
          String express_parameter = "_context";

          if (!is_builtin) {
            tmp_str += express_parameter;
          }

          boolean first_time = false;

          if (is_builtin) {
            first_time = true;
          }

          if (children != null) {

            for (int i = 0; i < actual_count; i++) {
              String tmp_argument = (String) actual_parameters.elementAt(i);

              if (first_time) {
//              if (tmp_str.equals("typeOfV(_context, ")) { // 
                if (tmp_str.equals("typeOfV(_context")) { //
                  tmp_str = tmp_argument + "." + tmp_str;
                  first_time = false;
                }
                else {
                  tmp_str += tmp_argument;
                  first_time = false;
                }
              }
              else {
                tmp_str += (", " + tmp_argument);
              }
            }
          } // if has children

          tmp_str += ")";
          jc.generated_java = tmp_str;
          printDDebug("### X_FunctionCall - generated java: " + jc.generated_java, jc);

          switch (built_in_id) {
            case 1:
              break;

            case 2:
              break;

            case 3:
              break;

            case 4:
              break;

            case 5:
              break;

            case 6:
              break;

            case 7:
              break;

            case 8:
              break;

            case 9:
              break;

            case 10:
              break;

            case 11:
              break;

            case 12:
              break;

            case 13:
              break;

            case 14:
              break;

            case 15:
              break;

            case 16:
              break;

            case 17:
              break;

            case 18:
              break;

            case 19:
              break;

            case 20:
              break;

            case 21:
              break;

            case 22:
              break;

            case 23:
              break;

            case 24:
              break;

            case 25: // TYPEOF
              jc.type_of_operand = JavaClass.T_AGGREGATE;
              printDDebug("### X_FunctionCall - TYPEOF return type: " + jc.type_of_operand, jc);

              break;

            case 26:
              break;

            case 27:
              break;

            case 28:
              break;

            case 29:
              break;

            case 30: // extent
              break;

            case -1: // user defined

              if (fd == null) {
                // error
                break;
              }

              // here goes the type of user-defined function
              break;

            default:
              break; // error
          }
        } // jc.active
      } // jc != null
//    } else { // if children = null;
//			// this case is possible when the function has no parameters/
//			// it was missed, should have been included directly instead
    }

// System.out.println("in X_FunctionCall - returning - jc.generated_java: " + jc.generated_java);

    return data;
  }

  // at this point it is not known where the function is invoked from, if from the same schema class,
  // no need for the class prefix, but it is not known
  String constructFunctionSchemaClass(JavaClass jc, String function_name)
      throws SdaiException {
    // very exprensive call!!!
    SdaiModel current_model = fd.findEntityInstanceSdaiModel();

    //SdaiModel current_model = jc.model;
    String model_name = current_model.getName();

    //		System.out.println("function_name: " + function_name +"model_name: " + model_name);
    String schema_name;

    if (model_name.length() > 16) {
      String part_model_name = model_name.substring(model_name.length() - 16);

      if (part_model_name.equalsIgnoreCase("_DICTIONARY_DATA")) {
        schema_name = model_name.substring(0, model_name.length() - 16);
      }
      else {
        schema_name = model_name;
      }
    }
    else {
      schema_name = model_name;
    }

    //     String type_schema_class = "jsdai.S" + schema_name.substring(0,1).toUpperCase() + schema_name.substring(1).toLowerCase() + ".class";
    String type_schema_class = "jsdai.S" + schema_name.substring(0, 1).toUpperCase() +
        schema_name.substring(1).toLowerCase() + "." + function_name;

    return type_schema_class;
  }

  String constructFunctionSchemaClass_old(JavaClass jc)
      throws SdaiException {
    SdaiModel current_model = jc.model;
    String model_name = current_model.getName();
    String schema_name;

    if (model_name.length() > 16) {
      String part_model_name = model_name.substring(model_name.length() - 16);

      if (part_model_name.equalsIgnoreCase("_DICTIONARY_DATA")) {
        schema_name = model_name.substring(0, model_name.length() - 16);
      }
      else {
        schema_name = model_name;
      }
    }
    else {
      schema_name = model_name;
    }

    //     String type_schema_class = "jsdai.S" + schema_name.substring(0,1).toUpperCase() + schema_name.substring(1).toLowerCase() + ".class";
    String type_schema_class = "jsdai.S" + schema_name.substring(0, 1).toUpperCase() +
        schema_name.substring(1).toLowerCase() + ".S" +
        schema_name.substring(0, 1).toUpperCase() +
        schema_name.substring(1).toLowerCase() + ".";

    return type_schema_class;
  }

  String getName_old() throws SdaiException {
    String name = "NAME_NOT_KNOWN";

    switch (built_in_id) {
      case 1:
        name = "Abs";

        break;

      case 2:
        name = "Acos";

        break;

      case 3:
        name = "Asin";

        break;

      case 4:
        name = "Atan";

        break;

      case 5:
        name = "Blength";

        break;

      case 6:
        name = "Cos";

        break;

      case 7:
        name = "Exists";

        break;

      case 8:
        name = "Exp";

        break;

      case 9:
        name = "Format";

        break;

      case 10:
        name = "Hibound";

        break;

      case 11:
        name = "Hiindex";

        break;

      case 12:
        name = "Length";

        break;

      case 13:
        name = "Lobound";

        break;

      case 14:
        name = "Loindex";

        break;

      case 15:
        name = "Log";

        break;

      case 16:
        name = "Log2";

        break;

      case 17:
        name = "Log10";

        break;

      case 18:
        name = "Nvl";

        break;

      case 19:
        name = "Odd";

        break;

      case 20:
        name = "Rolesof";

        break;

      case 21:
        name = "Sin";

        break;

      case 22:
        name = "Sizeof";

        break;

      case 23:
        name = "Sqrt";

        break;

      case 24:
        name = "Tan";

        break;

      case 25:
        name = "Typeof";

        break;

      case 26:
        name = "Usedin";

        break;

      case 27:
        name = "Value";

        break;

      case 28:
        name = "Value_in";

        break;

      case 29:
        name = "Value_unique";

        break;

      case 30:
//			name = "Bag_to_set";
        name = "Extent";
        break;

      case -1: // user defined

        if (fd == null) {
          // error
          name = "_fFd_null_error";

          break;
        }

        String the_name = fd.getName(null);
        name = "f" + the_name.substring(0, 1).toUpperCase() + the_name.substring(1).toLowerCase();

        break;

      default:
        name = "_fDefault_error";

        break; // error
    }

    return name;
  }

  public void dump(String prefix) {

    String hey = "";

    if (outer_sizeof) {
      hey = "Outer SIZEOF";
    }

    System.out.println(toString(prefix) + " - " + hey);

    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode) children[i];

        if (n != null) {
          n.dump(prefix + " ");
        }
      }
    }
  }

}
