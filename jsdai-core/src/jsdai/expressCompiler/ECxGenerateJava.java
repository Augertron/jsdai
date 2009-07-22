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

// %modified: 1016210367351 %

/*
Here is the idea:
Is it important to pass something while going down?
When going up in the recursion in the tree, after the final primitive operand node was reached,
the information about the type of the node is taken in the JavaClass instance.
When the upper operational node is reached, it stores the type and the operand.
Then again going down for the next operand, and up, bringing it and its type.
At the operational node, when all the operands and their types are present, the corresponding java code is generaded and passed to the
JavaClass instance together with the resulting type to bring them up as operand information to a possible next operational node higher-up in 
the tree.
So, each leaf node knows its type.
Eeach intermediate operational node stores the types of all its operands which are provided by the returning traversing on the way up from the
operands.
Each operational node also stores the so far generated java fragments, corresponding to its operands.
When all the operand strings and their types are retrieved, then the decision is taken what to generate in that operational node,
in which way to combine the operand-java string fragments,  as operands or as arguments of a special method perhaps, depends on their types.
The resulting java string and the resulting operand type are passed to the JavaCode instance on the way up.
The question is, is using of strings for each operand and combining them at the operational node enough, or something else should be
passed up in addition/instead.
At this point I think it is enough.
So, in JavaCode, we have one attribute for type, and possible additional attributes for type specialization,
and one string attribute for already generated java code fragment corresponding to the operand which may itself be already generated
java code for an expression.
In each multi-operand operational nodes,
an aggregate attribute for strings with operands,
and an aggregate attribute for their corresponding types,
with possible additional (aggregate) attributes for type specialization is stored.
Later they are used to make the resulting java string and resulting type, they are passed up.
About the type and its specializations.
If an integer is used for type attribute,
then additional EEntity may be used for general entity or for ECartesian_point, when the integer attribute indicates an entity type.
Also if an integer attribute indicates an aggregate, then 
Aggregate attribute is  used to store either a general aggregate or ACartesian_point or A_integer.
Alternative - to use a general enough type to store the type, probably Object.
 */
package jsdai.expressCompiler;

import java.util.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;


public class ECxGenerateJava implements Compiler2Visitor {
  // static boolean print2string_activated = false;
  // private int indent = 0;
  // private int depth = 0; // do we need it? Maybe the stack size tells us the same.
  static boolean do_debug = false;
  static String select_two_dollars = null;
  static boolean flag_deep_debug = false;
  static boolean flag_split_debug = false;
  static boolean flag_print_active_nodes = true; // false
  //-------------------------------------------------------------------------------------------------------------------
  static final int SELECT_ENTITIES_ONLY = 0;
  static final int SELECT_MIXED = 1;
  static final int SELECT_DEFINED_TYPES_ONLY = 2;
  static final int AA_JAVA_EXPLICIT = 1;
  static final int AA_JAVA_EXPLICIT_REDECLARING = 2;
  static final int AA_SUPERTYPE_EXPLICIT = 3;
  static final int AA_SUPERTYPE_EXPLICIT_REDECLARING = 4;
  static final int AA_CURRENT_EXPLICIT = 5;
  static final int AA_CURRENT_EXPLICIT_REDECLARING = 6;
  static final int AA_JAVA_DERIVED = 7;
  static final int AA_JAVA_DERIVED_REDECLARING = 8;
  static final int AA_SUPERTYPE_DERIVED = 9;
  static final int AA_SUPERTYPE_DERIVED_REDECLARING = 10;
  static final int AA_CURRENT_DERIVED = 11;
  static final int AA_CURRENT_DERIVED_REDECLARING = 12;
  static final int AA_JAVA_INVERSE = 13;
  static final int AA_JAVA_INVERSE_REDECLARING = 14;
  static final int AA_SUPERTYPE_INVERSE = 15;
  static final int AA_SUPERTYPE_INVERSE_REDECLARING = 16;
  static final int AA_CURRENT_INVERSE = 17;
  static final int AA_CURRENT_INVERSE_REDECLARING = 18;
  static final int AA_JAVA_EXPLICIT_TO_DERIVED = 19;
  static final int AA_SUPERTYPE_EXPLICIT_TO_DERIVED = 20;
  static final int AA_CURRENT_EXPLICIT_TO_DERIVED = 21; // I think this case is hardly possible.
	
	static String global_function_name = "";
	static String global_procedure_name = "";
  static int global_return_number;

	ECxGenerateJava(boolean flag_debug, boolean flag_deep_debug, boolean flag_no_print_active_nodes, boolean flag_split_debug, Object data){
		this.do_debug = flag_debug;
		this.flag_deep_debug = flag_deep_debug;
		this.flag_split_debug = flag_split_debug;
//		this.flag_split_debug = true;
//		this.flag_print_active_nodes = !flag_no_print_active_nodes;
//		this.flag_print_active_nodes = false;
		this.flag_print_active_nodes = false;

		JavaClass jc = (JavaClass) data;
//		jc.flag_print_active_nodes = flag_print_active_nodes;
		jc.flag_print_active_nodes = false;
//		jc.flag_print_active_nodes = false;
		jc.flag_deep_debug = flag_deep_debug;		
	}

  private String indentString(int indent) {
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < indent; ++i) {
      sb.append("\t");
    }

    return sb.toString();
  }

  void debugPrint(Object data, String msg) {
    if (do_debug) {
      if (data != null) {
        JavaClass jc = (JavaClass) data;

        if (jc.active) {
          System.out.println("DP> " + msg + "- type: " + jc.type_of_operand + ", operand: " + 
                             jc.generated_java);
        }
      }
    }
  }

  void printActive(String msg) {
    if (flag_print_active_nodes) {
      System.out.println(msg);
    }
  }

  void printDDebug(String msg) {
    if (flag_deep_debug) {
      System.out.println("[ECxGen][DeepDebug]>" +msg);
    }
  }

  public Object visit(SimpleNode node, Object data) throws SdaiException {
    //    System.out.println(indentString() + node +
    //             ": acceptor not unimplemented in subclass?");
    JavaClass jc = (JavaClass) data;
    jc.indent++;
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_AllSchemas node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;
    jc.indent++;
    printActive("XP Active: AllSchemas");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_Declaration node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

//    if (!jc.active) {
//      return data;
//    }

    jc.indent++;
    printActive("XP Active: Declaration");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }


  public Object visit(X_DerivedAttr node, Object data)
               throws SdaiException {
    // this prints the whole derived attribute directly from tokens as it is in original express (spaces and newlines not re-created)
    JavaClass jc = (JavaClass) data;
    String indent_str;


// System.out.println("XIXIX 1: " + jc.entity + ", 2: " + node.attribute);

// jc.pw.println("// XIXIX 1: " + jc.entity + ", 2: " + node.attribute);

/*

	if (jc.entity instanceof EDerived_attribute) {
		if (((EDerived_attribute)jc.entity).getName(null).equalsIgnoreCase("shaped_product")) {
			System.out.println("<X_DerivedAttr-1>: jc.entity: " + jc.entity + ", jc.ed: " + jc.ed); 
		}
		if (node.attribute.getName(null).equalsIgnoreCase("shaped_product")) {
			System.out.println("<X_DerivedAttr-2>: node.attribute: " + node.attribute + ", jc.entity: " + jc.entity + ", jc.ed: " + jc.ed); 
		}
	} else {
		if (node.attribute.getName(null).equalsIgnoreCase("shaped_product")) {
			System.out.println("<X_DerivedAttr-3>: node.attribute: " + node.attribute + ", jc.entity: " + jc.entity + ", jc.ed: " + jc.ed); 
		}
	
	}
	
*/	
	
//	if (jattr.getName(null).equalsIgnoreCase("shaped_product")) {
//			System.out.println("<DERIVED> attr: " + attr + ", schema: " + attr_schema + ", _ed: " + _ed + ", model: " + model); 
//	}


    if (jc.entity == node.attribute) {
//jc.pw.println("// XIXIX 1-2: jc.entity = node.attribute");
//jc.pw.println("// XP X_DerivedAttr, attribute found: " + node.attribute.getName(null) + ", entity: " + jc.ed.getName(null));
      jc.active = true;

      //         jc.generated_java = "\t\t\t\t";
    } else {
//jc.pw.println("// XIXIX 1-3: jc.entity NOT = node.attribute");
      //         EEntity_definition ped = ((EDerived_attribute)jc.entity).getParent_entity(null);
      // need to check if ped is supertype of the current entity jc.ed, if yes, then proceed into the expression
      // for cases of java inheritance, the expression generation part is not invoked at all, so supertype means artificial inheritance.
      //         if (ped.getClass().isAssignableFrom(jc.ed.getClass())) {
      //            jc.active = true;
      //         } else {
     return data;

      //         }
    }

    jc.indent++;

    /*
    SimpleNode snode = node;
    String str = "";
    Token current = snode.first_token;   
    for (;;) {
       str += current.image;
       current = current.next;
       if (current == snode.last_token) {
          if (current != snode.first_token) {
             str += current.image;
          }
          break;
       }
    }
    System.out.println(indentString() + node + " >>>EXPRESS>>> " + str);
     */
    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;

        // jc.generated_java = "";
        // jc.print_string = "";
      }


      // jc.generated_java = "";
      jc.print_tabs = indentString(jc.indent);
    }

    jc.flag_alloc_type = true;
    jc.alloc_type_depth = jc.indent;
    jc.alloc_type = node.getStaticTypeFieldName(jc);

    if (jc.active) {
      printActive("XP Active: DerivedAttr");
    }

    jc.in_derive_definition = true;
    data = node.childrenAccept(this, data);
    jc.in_derive_definition = false;

    jc.flag_alloc_type = false;

    if (node.print2string_activated) {
      jc.print2string = false;
      indent_str = indentString(jc.indent + 1);

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          jc.pw.println(indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          jc.pw.println(indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      node.getForwardedJavaFromChildren();
      jc.pw.println(node.forwarded_java);
      node.forwarded_java = "";
      jc.pw.println(jc.print_string);
      jc.print_string = "";
      node.print2string_activated = false;

      // jc.saved_str = "";
    }

    if (jc.entity == node.attribute) {
      jc.active = false;
      printDDebug("Node.attribute: " + node.attribute);

			String return_str;
			String return_type_str = node.getStaticTypeFieldName(jc);

jc.pw.println("//###-01 jc.generated_java: " + jc.generated_java);

			// trying to check if it is an entity constructor
			
			boolean is_entity_constructor = false;
			String constructor_static_parameter_type_str = "";
			String constructor_tmp = "";
			if (node.children != null) {
				if (node.children.length == 1) {
					if (node.children[0] instanceof X_Expression) {
						if (((X_Expression)node.children[0]).children != null) {
							if (((X_Expression)node.children[0]).children.length == 1) { // may not be true with complex expressions
								if (((X_Expression)node.children[0]).children[0] instanceof X_EntityConstructor) {
									is_entity_constructor = true;
//									constructor_static_parameter_type_str = ((X_EntityConstructor)((X_Expression)node.children[0]).children[0]).getStaticTypeFieldName(jc);									
									constructor_static_parameter_type_str = node.getStaticTypeFieldName(jc);
									constructor_tmp = "\t\treturn (Value.alloc(" + constructor_static_parameter_type_str + ").set(_context, ";
								} else 
								if (((X_Expression)node.children[0]).children[0] instanceof X_MultiplicationLikeOp) {
							 		// possible entity constructor as well
							 		// in this case, it is only ok to take the type from the first constructor, 
							 		// if the consructors are in suitable order and do not define complex entities, etc.
							 		// better to get the type from the attribute type instead
							 		// also, what if  we have  constant || constructor() - it will work only if constructor is first
							 		// perhaps we can implement this approach with set() for all derived attribute returns, 
							 		// not only the ones with entity constructors
									if (((X_MultiplicationLikeOp)((X_Expression)node.children[0]).children[0]).children.length > 0) { 
										if (((X_MultiplicationLikeOp)((X_Expression)node.children[0]).children[0]).children[0] instanceof X_EntityConstructor) {
											is_entity_constructor = true;
											constructor_static_parameter_type_str = node.getStaticTypeFieldName(jc);
											constructor_tmp = "\t\treturn (Value.alloc(" + constructor_static_parameter_type_str + ").set(_context, ";
										}
									}
  						 	}	
							} 
						}
					}
				}
			}

			if (jc.generated_java.equals("Value.alloc(ExpressTypes.GENERIC_TYPE).unset()")) {

//				jc.pw.println("XOXOXO-BEBBBBBBEEEEEE");

					jc.pw.println("\t\t\t\treturn(Value.alloc(" + return_type_str + ").set(_context, Value.alloc(ExpressTypes.GENERIC_TYPE).unset()));");				
		/*
		instead of:

		return (Value.alloc(ExpressTypes.GENERIC_TYPE).unset());
    generate:

		return (Value.alloc(jsdai.SRepresentation_schema.SRepresentation_schema._st_compound_item_definition).set(_context, Value.alloc(ExpressTypes.GENERIC_TYPE).unset()));
			
		*/
				
		  } else
			if (is_entity_constructor) {
        jc.pw.println(constructor_tmp + jc.generated_java + "));");
		  } else	
      if (jc.generated_java.charAt(0) == '.') {
        jc.pw.println("\t\t\t\treturn (Value.alloc()" + jc.generated_java + ");");
      } else {
        jc.pw.println("\t\t\t\treturn (" + jc.generated_java + ");");
      }

// for constants is needed:
// instead:
//				return (new jsdai.lang.Value(CAaa.definition));
// need: 
// return (Value.alloc(jsdai.STest_simple.CAaa.definition).set(_context, new jsdai.lang.Value(CAaa.definition)));





/*

      if (jc.generated_java.charAt(0) == '.') {
//      	return_str = "\t\t\t\treturn (Value.alloc()" + jc.generated_java + ");";

//      	return_str = "\t\t\t\treturn (Value.alloc()" + jc.generated_java + ");";
      	return_str = "\t\t\t\treturn (Value.alloc(" + return_type_str + ").set(Value.alloc()" + jc.generated_java + "));";
      } else {
//      	return_str = "\t\t\t\treturn (" + jc.generated_java + ");";
      	return_str = "\t\t\t\treturn (Value.alloc(" + return_type_str + ").set(_context, " + jc.generated_java + "));";
      }
      jc.pw.println(return_str);

*/

      jc.generated_java = "";
    } else {
      //         EEntity_definition ped = ((EDerived_attribute)jc.entity).getParent_entity(null);
      //         if (ped.getClass().isAssignableFrom(jc.ed.getClass())) {
      //            jc.active = false;
      //            jc.pw.println("\t\t\t\t" + jc.generated_java);
      //            jc.generated_java = "";
      //         }
    }

    jc.indent--;

    return data;
  }

  // introduced for incremental 
  public Object visit(X_Expression node, Object data)  throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: Expression");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;
    printActive("XP Active Ending: Expression");


if (jc.expression_fragment) {

 //   if (node.print2string_activated) {
 //     jc.print2string = false;
      String indent_str = indentString(jc.indent + 1);

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          jc.pw.println(indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          jc.pw.println(indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }


/*
      node.getForwardedJavaFromChildren();
      jc.pw.println(node.forwarded_java);
      node.forwarded_java = "";
      jc.pw.println(jc.print_string);
      jc.print_string = "";
      node.print2string_activated = false;
*/


//      node.getForwardedJavaFromChildren();
      jc.pw.println(node.forwarded_java);
//      node.forwarded_java = "";
      jc.pw.println(jc.print_string);
      jc.print_string = "";
//      node.print2string_activated = false;
	

// the above forwarding stuff should be printed always, not only when indent = 0? No examples so far
	if (jc.indent == 0) {

		if (jc.entity instanceof EWhere_rule) {
			// attempting to do this in JavaBackend, in generateJavaExpressionForEntityRulesInc() method
			String tmp_str = "\t\treturn (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, ";

			jc.pw.print(tmp_str);
			jc.pw.print(jc.generated_java);
			jc.pw.println(").getLogical());\n\t}");
		} else {

      if (jc.generated_java.charAt(0) == '.') {
        jc.pw.println("\t\t\t\treturn (Value.alloc()" + jc.generated_java + ");");
      } else {
        jc.pw.println("\t\t\t\treturn (" + jc.generated_java + ");");
      }

		}

	}			


}
//System.out.println("<>EXPRESSION<>: " + jc.generated_java);

    return data;


  }
   
  /*
  public Object visit(X_SimpleExpression node, Object data)  throws SdaiException {
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  public Object visit(X_RelOpExtended node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: RelOpExtended");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;
    printActive("XP Active Ending: RelOpExtended");

    return data;
  }

  public Object visit(X_RelOp node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: RelOp");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;
    printActive("XP Active Ending: RelOp");

    return data;
  }

  /*
  public Object visit(X_Term node, Object data)  throws SdaiException {
    System.out.println(indentString() + node);
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  public Object visit(X_AddLikeOp node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_AddLikeOp before children");

    int nr_of_operands = node.jjtGetNumChildren() - 1;


    //      if (jc.active) {
    //            jc.generated_java += "(";
    //      }
    printActive("XP Active: AddLikeOp");
    data = node.childrenAccept(this, data);
    debugPrint(data, "X_AddLikeOp after children");

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();


    //      if (jc.active) {
    //            jc.generated_java += ")";
    //      }
    jc.indent--;

    return data;
  }

  /*
  public Object visit(X_Factor node, Object data)  throws SdaiException {
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  public Object visit(X_MultiplicationLikeOp node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_MultiplicationLikeOp before children");
    printActive("XP Active: MultiplicationLikeOp");
//System.out.println("RRX - in MultiplicationLikeOp, jc: " + jc);
    data = node.childrenAccept(this, data);
    debugPrint(data, "X_MultiplicationLikeOp after children");

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_EnumerationRef node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_EnumerationRef before children");
    printActive("XP Active: EnumerationRef");

    if (jc.in_aggregate_initializer) {
//      jc.generated_java += ("" + node.value);
      jc.generated_java += ("\"" + node.name + "\"");
    } else {
//      jc.generated_java = "Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, " + node.value + ")";

//      jc.generated_java = "Value.alloc(ExpressTypes.STRING_TYPE).set(_context, \"" + node.name + "\")";


				// not working, a specific enumeration not known in reference, should have perhaps Express_types.ENUMERATION_TYPE
      jc.generated_java = "Value.alloc(" + node.getStaticTypeString(jc) + ").setEnum(_context, \"" + node.name + "\")";

    }

    data = node.childrenAccept(this, data);
    debugPrint(data, "X_EnumerationRef after children");

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  /*
  public Object visit(X_SimpleFactor node, Object data)  throws SdaiException {
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  /*
  public Object visit(X_Primary node, Object data)  throws SdaiException {
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  public Object visit(X_ParameterRef node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String parameter_name = node.name;

    if ((jc.in_function) || (jc.in_procedure)) {
      parameter_name = "_nonvar_" + parameter_name;
//      parameter_name = "_nonvar__e_" + parameter_name;
    }

    EEntity parameter_type = null;
    if (node.parameter != null) {
    	if (node.parameter.testParameter_type(null)) {
    		parameter_type = node.parameter.getParameter_type(null);
			} else {
				System.out.println("Expressions> ERROR parameter type null: " + node.parameter);
			}
		} else {
			System.out.println("Expressions> ERROR parameter null: " + node);
		}		
    if (jc.active) {
      jc.generated_java = parameter_name;

      if (parameter_type instanceof EInteger_type) {
        jc.type_of_operand = 2;
      } else if (parameter_type instanceof EReal_type) {
        jc.type_of_operand = 4;
      } else if (parameter_type instanceof ENumber_type) {
        jc.type_of_operand = 4;
      } else {
        jc.type_of_operand = 0;
      }
    }

    if (jc.active) {
      printActive("XP Active: ParameterRef");
    }

    data = node.childrenAccept(this, data);

// System.out.println("OOO---" + jc.generated_java);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_TargetParameterRef node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String parameter_name = node.name;

    if (jc.active) {
      jc.generated_java = parameter_name;
    }

    if (jc.active) {
      printActive("XP Active: TargetParameterRef");
    }

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_SourceParameterRef node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String parameter_name = node.name;

    if (jc.active) {
      jc.generated_java = parameter_name;
    }

    if (jc.active) {
      printActive("XP Active: SourceParameterRef");
    }

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_VariableRef node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String variable_name = "_no_name_";

    if (node == null) {
      printDDebug("RR0001: damn, node null");
    } else {
      if (node.variable == null) {
        printDDebug("RR0002: damn, variable null: " + node.name);
      } else {
        //        printDDebug("RR0003: varieble not null: " + node.variable);
      }
    }

    if (node.variable != null) {
//      variable_name = node.variable.getScope_id() + node.name;
			variable_name = node.constructVariableReference();
//      variable_name = "_e_" + node.variable.getScope_id() + node.name;
    	
    
    }

    //      EEntity variable_type = node.variable.getType().getParameter_type(null);
    EParameter par_type = null;
    EEntity variable_type = null;

    if (jc.active) {
      if (node.variable != null) {
        par_type = node.variable.getType();

        if (par_type.testParameter_type(null)) {
          variable_type = par_type.getParameter_type(null);
        } else {
          // let's leave it null, but it must be an error
          printDDebug("XP - VariableRef: parameter_type is UNSET: " + variable_name);
        }
      } else {
        printDDebug("XP - VariableRef: variable is UNSET: " + variable_name);
      }

      jc.generated_java = variable_name;

      if (variable_type instanceof EInteger_type) {
        jc.type_of_operand = 2;
      } else if (variable_type instanceof EReal_type) {
        jc.type_of_operand = 4;
      } else if (variable_type instanceof ENumber_type) {
        jc.type_of_operand = 4;
      } else {
        jc.type_of_operand = 0;
      }
    }

    if (jc.active) {
      printActive("XP Active: VariableRef");
    }

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;
    printActive("XP Active Ending: VariableRef");

    return data;
  }

  public Object visit(X_Literal node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }


    // if (jc.indexing) {
    // return data;
    // }
    jc.indent++;

    String value = "";

    if (node.type == JavaClass.T_INTEGER) {
      if (jc.in_aggregate_initializer) {
        value += node.int_value;
      } else {
        value += ("Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, " + node.int_value + ")");
      }
    } else if (node.type == JavaClass.T_BINARY) {
      if (jc.in_aggregate_initializer) {
        value += node.binary_value;
      } else {
        value += ("Value.alloc(ExpressTypes.BINARY_TYPE).set(_context, " + node.binary_value + ")");
      }
    } else if (node.type == JavaClass.T_LOGICAL) {
      switch (node.logical_value) {
      case 3:

        if (jc.in_aggregate_initializer) {
          value += "false /* unknown */";
        } else {
          value += "Value.alloc(ExpressTypes.LOGICAL_TYPE).setLB(_context, 3)";
        }

        break;

      case 1:

        if (jc.in_aggregate_initializer) {
          value += "false";
        } else {
          if (jc.return_type instanceof ELogical_type) {
            value += "Value.alloc(ExpressTypes.LOGICAL_TYPE).setLB(_context, 1)";
          } else if ((jc.pd  != null) && (jc.pd.getParameter_type(null) instanceof ELogical_type))  {
            value += "Value.alloc(ExpressTypes.LOGICAL_TYPE).setLB(_context, 1)";
          } else {
            value += "Value.alloc(ExpressTypes.BOOLEAN_TYPE).setLB(_context, 1)";
          }
        }

        break;

      case 2:
        if (jc.in_aggregate_initializer) {
          value += "true";
        } else {
          if (jc.return_type instanceof ELogical_type) {
            value += "Value.alloc(ExpressTypes.LOGICAL_TYPE).setLB(_context, 2)";
          } else if ((jc.pd  != null) && (jc.pd.getParameter_type(null) instanceof ELogical_type))  {
            value += "Value.alloc(ExpressTypes.LOGICAL_TYPE).setLB(_context, 2)";
          } else 
            value += "Value.alloc(ExpressTypes.BOOLEAN_TYPE).setLB(_context, 2)";
        }

        break;

      default:

        if (jc.in_aggregate_initializer) {
          value += "false /* default */";
        } else {
          if (jc.return_type instanceof ELogical_type) {
            value += "Value.alloc(ExpressTypes.LOGICAL_TYPE).setLB(_context, 0)";
          } else if ((jc.pd  != null) && (jc.pd.getParameter_type(null) instanceof ELogical_type))  {
            value += "Value.alloc(ExpressTypes.LOGICAL_TYPE).setLB(_context, 0)";
          } else {
            value += "Value.alloc(ExpressTypes.BOOLEAN_TYPE).setLB(_context, 0)";
          }
        }

        break;
      }
    } else if (node.type == JavaClass.T_REAL) {
      if (jc.in_aggregate_initializer) {
        value += node.double_value;
      } else {
        value += ("Value.alloc(ExpressTypes.REAL_TYPE).set(_context, " + node.double_value + ")");
      }
    } else if (node.type == JavaClass.T_STRING) {
      if (jc.in_aggregate_initializer) {
				if (node.original_schema_name != null) {
					// to see what is needed here
        	value += node.string_value;
				} else {
        	value += node.string_value;
      	}
      } else {
				if (node.original_schema_name != null) {
	        value += ("Value.alloc(ExpressTypes.STRING_TYPE).set(_context, " + node.string_value + ", " +  node.original_schema_name + ")");
				} else {
	        value += ("Value.alloc(ExpressTypes.STRING_TYPE).set(_context, " + node.string_value + ")");
  			}
      }
    }

    if (jc.active) {
      jc.generated_java = "" + value;
      jc.type_of_operand = node.type;
    }

    //    System.out.println(indentString() + node + value);
    if (jc.active) {
      printActive("XP Active: Literal");
    }

    data = node.childrenAccept(this, data);

// System.out.println("//##### literal: " + jc.generated_java);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

//System.out.println("<>LITERAL<>: " + jc.generated_java);

    return data;
  }

  /*
  public Object visit(X_QualifiableFactor node, Object data)  throws SdaiException {
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  String constructTypeSchemaPrefix(JavaClass jc) throws SdaiException {
    String type_schema_class = "jsdai." + constructSchemaClassNamePrefix(jc);

    return type_schema_class;
  }

  String constructSchemaClassNamePrefix(JavaClass jc) throws SdaiException {
    SdaiModel current_model = jc.model;
    String model_name = current_model.getName();
    String schema_name;

    if (model_name.length() > 16) {
      String part_model_name = model_name.substring(model_name.length() - 16);

      if (part_model_name.equalsIgnoreCase("_DICTIONARY_DATA")) {
        schema_name = model_name.substring(0, model_name.length() - 16);
      } else {
        schema_name = model_name;
      }
    } else {
      schema_name = model_name;
    }

    String type_schema_class = "S" + schema_name.substring(0, 1).toUpperCase() + 
                               schema_name.substring(1).toLowerCase() + ".";

    return type_schema_class;
  }

  // node.ed.findEntityInstanceSdaiModel().getName();
  String constructTypeSchemaPrefixFromModel(String model_name)
                                     throws SdaiException {
    String schema_name = null;

    if (model_name.length() > 16) {
      String part_model_name = model_name.substring(model_name.length() - 16);

      if (part_model_name.equalsIgnoreCase("_DICTIONARY_DATA")) {
        schema_name = model_name.substring(0, model_name.length() - 16);
      } else {
        schema_name = model_name;
      }
    }

    String schema_package = "jsdai.S" + schema_name.substring(0, 1).toUpperCase() + 
                            schema_name.substring(1).toLowerCase();

    return schema_package;
  }

  String constructSchemaClassNamePrefixFromModel(String model_name)
                                          throws SdaiException {
    String schema_name = null;

    if (model_name.length() > 16) {
      String part_model_name = model_name.substring(model_name.length() - 16);

      if (part_model_name.equalsIgnoreCase("_DICTIONARY_DATA")) {
        schema_name = model_name.substring(0, model_name.length() - 16);
      } else {
        schema_name = model_name;
      }
    }

    String schema_package = "S" + schema_name.substring(0, 1).toUpperCase() + 
                            schema_name.substring(1).toLowerCase();

    return schema_package;
  }

  String getStaticAttrNameOld(EAttribute attribute, JavaClass jc)
                    throws SdaiException {
System.out.println("><01>< calculating attribute internal name: " + attribute);
    // have to calculate the internal attribute number
    // also, it would be good to add entity and/or package information when needed.
    String result = null;
    String static_attribute_name = null;
//    String attribute_name = attribute.getName(null);
    String attribute_name = getOriginalAttributeName(attribute);


    //    ESchema_definition the_sd = jc.sd;
    EGeneric_schema_definition the_sd = jc.sd;
    EEntity_definition the_ed = jc.ed;
    SdaiModel the_model = jc.model;

    // EEntity_definition parent_entity = attribute.getParent_entity(null);
    EEntity_definition parent_entity = (EEntity_definition) attribute.getParent_entity(null);
    SdaiModel parent_model = parent_entity.findEntityInstanceSdaiModel();
    String parent_model_name = parent_model.getName();

    // if parent_entity != the_ed, then entity prefix is needed.
    // if parent_model != the_model, then schema prefix is needed.
    String schema_prefix = "";
    String entity_prefix = "";

    if (parent_entity != the_ed) {
      String the_entity_name = parent_entity.getName(null).replace('+', '$');
      entity_prefix = "C" + the_entity_name.substring(0, 1).toUpperCase() + 
                      the_entity_name.substring(1).toLowerCase() + ".";
    }

//    jsdai.dictionary.EGeneric_schema_definition the_parent_schema = parent_model.getUnderlyingSchema();
    jsdai.dictionary.ESchema_definition the_parent_schema = parent_model.getUnderlyingSchema();
    String the_schema_name = the_parent_schema.getName(null);

    if (parent_model != the_model) {
      //      schema_prefix = "jsdai.S" + the_schema_name.substring(0,1).toUpperCase() + the_schema_name.substring(1).toLowerCase() + ".";
      schema_prefix = constructTypeSchemaPrefixFromModel(parent_model_name) + ".";
    } else {
      // lets do the same for now
      //         schema_prefix = "jsdai.S" + the_schema_name.substring(0,1).toUpperCase() + the_schema_name.substring(1).toLowerCase() + ".";
      schema_prefix = constructTypeSchemaPrefix(jc);
    }

    Vector all_attributes = new Vector();
    Vector all_flags = new Vector();
    HashSet entities = new HashSet();
    boolean inherited_branch = true;
    boolean is_complex = parent_entity.getComplex(null);

    if (is_complex) {
      jc.java_backend_inst.collectComplexAttributes(parent_entity, all_attributes, all_flags);
    } else {
      jc.java_backend_inst.collectAttributes(parent_entity, parent_entity, all_attributes, 
                                             all_flags, entities, inherited_branch);
    }

    processAttributes(parent_entity, all_attributes, all_flags);

    // loop to find the correct attribute and count the number
    int internal = 0;
    int count_derived = 0;
    int count_inverse = 0;

    printDDebug("parent entity: " + parent_entity + " all_attributes count: " + 
                       all_attributes.size());

    for (int sj = 0; sj < all_attributes.size(); sj++) {
      EAttribute attr0 = (EAttribute) all_attributes.elementAt(sj);
//      String attr_name = attr0.getName(null);
      String attr_name = getOriginalAttributeName(attr0);
      if (attr0.testOrder(null)) {
      	printDDebug("all attributes: " + sj + ", name: " + attr_name + ", order: " + 
        	                 attr0.getOrder(null));
			} else {
      	printDDebug("all attributes: " + sj + ", name: " + attr_name + ", order: UNSET");
			}

      int attribute_flag = (int) ((Integer) all_flags.elementAt(sj)).intValue();
// System.out.println("XYX-00 flag: " + attribute_flag + ", attr0: " + attr0);       
      String attribute_field = null;

      if (attribute_flag == AA_CURRENT_EXPLICIT) {
        attribute_field = " a" + internal++ + "; // " + attr_name + " - current entity -";

        //           pw.println("\t + attribute_object + " a" + sj + " // " + attr_name + " - current entity");
      } else if (attribute_flag == AA_JAVA_EXPLICIT) {
        attribute_field = " a" + internal++ + ";   " + attr_name + " - java inheritance -";

        //           pw.println("\t//  + attribute_object + " a" + sj + "   " + attr_name + " - java inheritance");
      } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT) {
        attribute_field = " a" + internal++ + "; // " + attr_name + " - non-java inheritance -";

        //           pw.println("\t + attribute_object + " a" + sj + " // " + attr_name + " - non-java inheritance");
      } else if (attribute_flag == AA_CURRENT_EXPLICIT_TO_DERIVED) {
        //            internal++;
        attribute_field = " a" + internal++ + 
                          " - explicit redeclared as derived - current entity - ";
        count_derived++;
      } else if (attribute_flag == AA_JAVA_EXPLICIT_TO_DERIVED) {
        //            internal++;
        attribute_field = " a" + internal++ + 
                          " -  explicit redeclared as derived - java inheritance - ";
        count_derived++;
      } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_TO_DERIVED) {
        //            internal++;
        attribute_field = " a" + internal++ + 
                          " - explicit redeclared as derived - non-java inheritance - ";
        count_derived++;
      } else if (attribute_flag == AA_CURRENT_EXPLICIT_REDECLARING) {
        printDDebug("current explicit redeclaring");
        attribute_field = "  - explicit redeclaring - current - ";
      } else if (attribute_flag == AA_JAVA_EXPLICIT_REDECLARING) {
        //            printDDebug("explicit redeclaring");
        attribute_field = "  - explicit redeclaring - java inheritance - ";
      } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_REDECLARING) {
        //            printDDebug("explicit redeclaring");
        attribute_field = "  - explicit redeclaring - non-java inheritance - ";
      } else if (attribute_flag == AA_JAVA_DERIVED) {
        attribute_field = "  - derived - java inheritance - ";
        count_derived++;
      } else if (attribute_flag == AA_JAVA_DERIVED_REDECLARING) {
        attribute_field = "  - derived redeclaring - java inheritance - ";
      } else if (attribute_flag == AA_SUPERTYPE_DERIVED) {
        attribute_field = "  - derived - non-java inheritance - ";
        count_derived++;
      } else if (attribute_flag == AA_SUPERTYPE_DERIVED_REDECLARING) {
        attribute_field = "  - derived redeclaring - non-java inheritance - ";
      } else if (attribute_flag == AA_CURRENT_DERIVED) {
        attribute_field = "  - derived - current - ";
        count_derived++;
        printDDebug("==Derived: " + count_derived + ", name: " + attribute_name);
      } else if (attribute_flag == AA_CURRENT_DERIVED_REDECLARING) {
        attribute_field = "  - derived redeclaring - current - ";
      } else if (attribute_flag == AA_JAVA_INVERSE) {
        attribute_field = "  - inverse - java inheritance - ";
        count_inverse++;
      } else if (attribute_flag == AA_JAVA_INVERSE_REDECLARING) {
        attribute_field = "  - inverse redeclaring - java inheritance - ";
      } else if (attribute_flag == AA_SUPERTYPE_INVERSE) {
        attribute_field = "  - inverse - non-java inheritance - ";
        count_inverse++;
      } else if (attribute_flag == AA_SUPERTYPE_INVERSE_REDECLARING) {
        attribute_field = "  - inverse redeclaring - non-java inheritance - ";
      } else if (attribute_flag == AA_CURRENT_INVERSE) {
        attribute_field = "  - inverse - current - ";
        count_inverse++;
      } else if (attribute_flag == AA_CURRENT_INVERSE_REDECLARING) {
        attribute_field = "  - inverse redeclaring - current - ";
      } else {
        attribute_field = "// " + attr_name + " - unknown";
      }

      select_two_dollars = null;

      // String attribute_object = getAttributeObjectString(attr0, attribute_field, sj, internal-1, pw);
      if (attribute_flag == AA_JAVA_EXPLICIT) {
        internal--;

        //         System.out.println("attr0: " +attr0 + "  attribute: " + attribute);
        //         System.out.println("attribute_flag_explicit_java: " +AA_JAVA_EXPLICIT);
        static_attribute_name = "a" + internal + "$";
// System.out.println("XYX-01: " + static_attribute_name);

        internal++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if ((attribute_flag == AA_SUPERTYPE_EXPLICIT) || 
                     (attribute_flag == AA_CURRENT_EXPLICIT)) {
        // pw.println("\t" + attribute_object);
        internal--;

        // pw.println("\tprotected static final jsdai.dictionary.CExplicit_attribute a" + internal + "$ = CEntity.initExplicitAttribute(definition, " + internal + ");");
// System.out.println("XYX-02: " + attr0 + ", attribute: " + attribute + ", flag: " + attribute_flag);
        if (attr0 == attribute) {
          static_attribute_name = "a" + internal + "$";
// System.out.println("XYX-03: " + static_attribute_name);

          break;
        } else
        if (attribute instanceof EExplicit_attribute) {
        	if (((EExplicit_attribute)attribute).testRedeclaring(null)) {
						// alternative is not to use the internal field
          	if (attr0 == ((EExplicit_attribute)attribute).getRedeclaring(null)) {
							// and what if chained redeclaring?
          		static_attribute_name = "a" + internal + "$";
          		break;
        		}
        	}
				}
        internal++;

        if (select_two_dollars != null) {
          // pw.println("\t" + select_two_dollars);
        }
      } else if (attribute_flag == AA_CURRENT_EXPLICIT_TO_DERIVED) {
        // pw.println("\t// " + attribute_object);
        count_derived--;

        if (attr0 == attribute) {
          static_attribute_name = "d" + count_derived + "$";

          break;
        }

        count_derived++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_JAVA_EXPLICIT_TO_DERIVED) {
        // pw.println("\t// " + attribute_object);
        count_derived--;

        if (attr0 == attribute) {
          static_attribute_name = "d" + count_derived + "$";

          break;
        }

        count_derived++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_TO_DERIVED) {
        // pw.println("\t// " + attribute_object);
        count_derived--;

        if (attr0 == attribute) {
          static_attribute_name = "d" + count_derived + "$";

          break;
        }

        count_derived++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_CURRENT_EXPLICIT_REDECLARING) {
// System.out.println("XYX-10: " + internal);
        //            System.out.println("explicit redeclaring 2");
        // pw.println("\t// " + attribute_object);
        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_JAVA_EXPLICIT_REDECLARING) {
// System.out.println("XYX-10: " + internal);
        //            System.out.println("explicit redeclaring 2");
        // pw.println("\t// " + attribute_object);
        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_REDECLARING) {
// System.out.println("XYX-10: " + internal);
        //            System.out.println("explicit redeclaring 2");
        // pw.println("\t// " + attribute_object);
        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_JAVA_DERIVED) {
        // pw.println("\t// " + attribute_object);
        count_derived--;

        if (attr0 == attribute) {
          static_attribute_name = "d" + count_derived + "$";

          break;
        }

        count_derived++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_JAVA_DERIVED_REDECLARING) {
        // pw.println("\t// " + attribute_object);
        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_SUPERTYPE_DERIVED) {
        // pw.println("\t// " + attribute_object);
        count_derived--;

        if (attr0 == attribute) {
          static_attribute_name = "d" + count_derived + "$";

          break;
        }

        count_derived++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_SUPERTYPE_DERIVED_REDECLARING) {
        // pw.println("\t// " + attribute_object);
        count_derived--;

        if (attr0 == attribute) {
          static_attribute_name = "d" + count_derived + "$";

          break;
        }

        count_derived++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_CURRENT_DERIVED) {
        // pw.println("\t// " + attribute_object);
        count_derived--;

        if (attr0 == attribute) {
          static_attribute_name = "d" + count_derived + "$";

          break;
        }

        count_derived++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_CURRENT_DERIVED_REDECLARING) {
        // pw.println("\t// " + attribute_object);
        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_JAVA_INVERSE) {
        // pw.println("\t// " + attribute_object);
        count_inverse--;

        if (attr0 == attribute) {
          static_attribute_name = "i" + count_inverse + "$";

          break;
        }

        count_inverse++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_JAVA_INVERSE_REDECLARING) {
        // pw.println("\t// " + attribute_object);
        count_inverse--;

        if (attr0 == attribute) {
          static_attribute_name = "i" + count_inverse + "$";

          break;
        }

        count_inverse++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_SUPERTYPE_INVERSE) {
        // pw.println("\t// " + attribute_object);
        count_inverse--;

        if (attr0 == attribute) {
          static_attribute_name = "i" + count_inverse + "$";

          break;
        }

        count_inverse++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_SUPERTYPE_INVERSE_REDECLARING) {
        // pw.println("\t// " + attribute_object);
        count_inverse--;

        if (attr0 == attribute) {
          static_attribute_name = "i" + count_inverse + "$";

          break;
        }

        count_inverse++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_CURRENT_INVERSE) {
        // pw.println("\t// " + attribute_object);
        count_inverse--;

        if (attr0 == attribute) {
          static_attribute_name = "i" + count_inverse + "$";

          break;
        }

        count_inverse++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else if (attribute_flag == AA_CURRENT_INVERSE_REDECLARING) {
        // pw.println("\t// " + attribute_object);
        count_inverse--;

        if (attr0 == attribute) {
          static_attribute_name = "i" + count_inverse + "$";

          break;
        }

        count_inverse++;

        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      } else {
        // pw.println("\t// " + attribute_object);
        if (select_two_dollars != null) {
          // pw.println("\t// " + select_two_dollars);
        }
      }
System.out.println("><03>< calculating attribute internal name - attribute_flag: " + attribute_flag);

    }

    // end of the loop to find the correct attribute and count the number
    //      result = schema_prefix + entity_prefix + static_attribute_name;
    if (entity_prefix.equalsIgnoreCase("")) {
      result = static_attribute_name;
    } else {
      result = schema_prefix + entity_prefix + "attribute" + 
               attribute_name.substring(0, 1).toUpperCase() + 
               attribute_name.substring(1).toLowerCase() + "(null)";
    }
System.out.println("><02>< calculating attribute internal name - result: " + result);

    return result;
  }

  /*
    String getStaticAttrName(EAttribute attribute, JavaClass jc)
                      throws SdaiException {
  
  
  
      // have to calculate the internal attribute number
      // also, it would be good to add entity and/or package information when needed.
      String result = null;
      String static_attribute_name = null;
      ESchema_definition the_sd = jc.sd;
      EEntity_definition the_ed = jc.ed;
      SdaiModel the_model = jc.model;
      EEntity_definition parent_entity = attribute.getParent_entity(
            null);
      SdaiModel parent_model = parent_entity.findEntityInstanceSdaiModel();
      String parent_model_name = parent_model.getName();
  
      // if parent_entity != the_ed, then entity prefix is needed.
      // if parent_model != the_model, then schema prefix is needed.
      String schema_prefix = "";
      String entity_prefix = "";
  
      if (parent_entity != the_ed) {
        String the_entity_name = parent_entity.getName(null);
        entity_prefix = "C" + the_entity_name.substring(0, 1).toUpperCase() + the_entity_name.substring(1).toLowerCase() + ".";
      }
  
      jsdai.dictionary.ESchema_definition the_parent_schema = parent_model.getUnderlyingSchema();
      String the_schema_name = the_parent_schema.getName(null);
  
      if (parent_model != the_model) {
  
          //      schema_prefix = "jsdai.S" + the_schema_name.substring(0,1).toUpperCase() + the_schema_name.substring(1).toLowerCase() + ".";
          schema_prefix = constructTypeSchemaPrefixFromModel(parent_model_name) + ".";
      } else {
  
        // lets do the same for now
        //         schema_prefix = "jsdai.S" + the_schema_name.substring(0,1).toUpperCase() + the_schema_name.substring(1).toLowerCase() + ".";
        schema_prefix = constructTypeSchemaPrefix(jc);
      }
  
      Vector all_attributes = new Vector();
      Vector all_flags = new Vector();
      HashSet entities = new HashSet();
      boolean inherited_branch = true;
      boolean is_complex = parent_entity.getComplex(null);
  
      if (is_complex) {
        jc.java_backend_inst.collectComplexAttributes(parent_entity, all_attributes, all_flags);
      } else {
        jc.java_backend_inst.collectAttributes(parent_entity, parent_entity, all_attributes, 
                                               all_flags, entities, inherited_branch);
      }
  
      processAttributes(parent_entity, all_attributes, all_flags);
  
      // loop to find the correct attribute and count the number
      int internal = 0;
      int count_derived = 0;
  
      for (int sj = 0; sj < all_attributes.size(); sj++) {
        EAttribute attr0 = ( EAttribute )all_attributes.elementAt(sj);
        String attr_name = attr0.getName(null);
        System.out.println("all attributes: " + sj + ", name: " + attr_name + ", order: " + attr0.getOrder(
                                                                                                  null));
  
        int attribute_flag = ( int )(( Integer )all_flags.elementAt(sj)).intValue();
        String attribute_field = null;
  
        if (attribute_flag == AA_CURRENT_EXPLICIT) {
          attribute_field = " a" + internal++ + "; // " + attr_name + " - current entity -";
  
          //           pw.println("\t + attribute_object + " a" + sj + " // " + attr_name + " - current entity");
        } else if (attribute_flag == AA_JAVA_EXPLICIT) {
          attribute_field = " a" + internal++ + ";   " + attr_name + " - java inheritance -";
  
          //           pw.println("\t//  + attribute_object + " a" + sj + "   " + attr_name + " - java inheritance");
        } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT) {
          attribute_field = " a" + internal++ + "; // " + attr_name + " - non-java inheritance -";
  
          //           pw.println("\t + attribute_object + " a" + sj + " // " + attr_name + " - non-java inheritance");
        } else if (attribute_flag == AA_CURRENT_EXPLICIT_TO_DERIVED) {
  
          //            internal++;
          attribute_field = " a" + internal++ + " - explicit redeclared as derived - current entity - ";
          count_derived++;
        } else if (attribute_flag == AA_JAVA_EXPLICIT_TO_DERIVED) {
  
          //            internal++;
          attribute_field = " a" + internal++ + " -  explicit redeclared as derived - java inheritance - ";
          count_derived++;
        } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_TO_DERIVED) {
  
          //            internal++;
          attribute_field = " a" + internal++ + " - explicit redeclared as derived - non-java inheritance - ";
          count_derived++;
        } else if (attribute_flag == AA_CURRENT_EXPLICIT_REDECLARING) {
  
          //            System.out.println("explicit redeclaring");
          attribute_field = "  - explicit redeclaring - current - ";
        } else if (attribute_flag == AA_JAVA_EXPLICIT_REDECLARING) {
  
          //            System.out.println("explicit redeclaring");
          attribute_field = "  - explicit redeclaring - java inheritance - ";
        } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_REDECLARING) {
  
          //            System.out.println("explicit redeclaring");
          attribute_field = "  - explicit redeclaring - non-java inheritance - ";
        } else if (attribute_flag == AA_JAVA_DERIVED) {
          attribute_field = "  - derived - java inheritance - ";
          count_derived++;
        } else if (attribute_flag == AA_JAVA_DERIVED_REDECLARING) {
          attribute_field = "  - derived redeclaring - java inheritance - ";
        } else if (attribute_flag == AA_SUPERTYPE_DERIVED) {
          attribute_field = "  - derived - non-java inheritance - ";
          count_derived++;
        } else if (attribute_flag == AA_SUPERTYPE_DERIVED_REDECLARING) {
          attribute_field = "  - derived redeclaring - non-java inheritance - ";
        } else if (attribute_flag == AA_CURRENT_DERIVED) {
          attribute_field = "  - derived - current - ";
          count_derived++;
          System.out.println("==Derived: " + count_derived + ", name: " + attribute.getName(null));
        } else if (attribute_flag == AA_CURRENT_DERIVED_REDECLARING) {
          attribute_field = "  - derived redeclaring - current - ";
        } else if (attribute_flag == AA_JAVA_INVERSE) {
          attribute_field = "  - inverse - java inheritance - ";
        } else if (attribute_flag == AA_JAVA_INVERSE_REDECLARING) {
          attribute_field = "  - inverse redeclaring - java inheritance - ";
        } else if (attribute_flag == AA_SUPERTYPE_INVERSE) {
          attribute_field = "  - inverse - non-java inheritance - ";
        } else if (attribute_flag == AA_SUPERTYPE_INVERSE_REDECLARING) {
          attribute_field = "  - inverse redeclaring - non-java inheritance - ";
        } else if (attribute_flag == AA_CURRENT_INVERSE) {
          attribute_field = "  - inverse - current - ";
        } else if (attribute_flag == AA_CURRENT_INVERSE_REDECLARING) {
          attribute_field = "  - inverse redeclaring - current - ";
        } else {
          attribute_field = "// " + attr_name + " - unknown";
        }
  
        select_two_dollars = null;
  
        // String attribute_object = getAttributeObjectString(attr0, attribute_field, sj, internal-1, pw);
        if (attribute_flag == AA_JAVA_EXPLICIT) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if ((attribute_flag == AA_SUPERTYPE_EXPLICIT) || (attribute_flag == AA_CURRENT_EXPLICIT)) {
  
          // pw.println("\t" + attribute_object);
          internal--;
  
          // pw.println("\tprotected static final jsdai.dictionary.CExplicit_attribute a" + internal + "$ = CEntity.initExplicitAttribute(definition, " + internal + ");");
          if (attr0 == attribute) {
            static_attribute_name = "a" + internal + "$";
            break;
          }
  
          internal++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t" + select_two_dollars);
          }
        } else if (attribute_flag == AA_CURRENT_EXPLICIT_TO_DERIVED) {
  
          // pw.println("\t// " + attribute_object);
          count_derived--;
  
          if (attr0 == attribute) {
            static_attribute_name = "d" + count_derived + "$";
            break;
          }
  
          count_derived++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_JAVA_EXPLICIT_TO_DERIVED) {
  
          // pw.println("\t// " + attribute_object);
          count_derived--;
  
          if (attr0 == attribute) {
            static_attribute_name = "d" + count_derived + "$";
            break;
          }
  
          count_derived++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_TO_DERIVED) {
  
          // pw.println("\t// " + attribute_object);
          count_derived--;
  
          if (attr0 == attribute) {
            static_attribute_name = "d" + count_derived + "$";
            break;
          }
  
          count_derived++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_CURRENT_EXPLICIT_REDECLARING) {
  
          //            System.out.println("explicit redeclaring 2");
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_JAVA_EXPLICIT_REDECLARING) {
  
          //            System.out.println("explicit redeclaring 2");
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT_REDECLARING) {
  
          //            System.out.println("explicit redeclaring 2");
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_JAVA_DERIVED) {
  
          // pw.println("\t// " + attribute_object);
          count_derived--;
  
          if (attr0 == attribute) {
            static_attribute_name = "d" + count_derived + "$";
            break;
          }
  
          count_derived++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_JAVA_DERIVED_REDECLARING) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_SUPERTYPE_DERIVED) {
  
          // pw.println("\t// " + attribute_object);
          count_derived--;
  
          if (attr0 == attribute) {
            static_attribute_name = "d" + count_derived + "$";
            break;
          }
  
          count_derived++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_SUPERTYPE_DERIVED_REDECLARING) {
  
          // pw.println("\t// " + attribute_object);
          count_derived--;
  
          if (attr0 == attribute) {
            static_attribute_name = "d" + count_derived + "$";
            break;
          }
  
          count_derived++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_CURRENT_DERIVED) {
  
          // pw.println("\t// " + attribute_object);
          count_derived--;
  
          if (attr0 == attribute) {
            static_attribute_name = "d" + count_derived + "$";
            break;
          }
  
          count_derived++;
  
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_CURRENT_DERIVED_REDECLARING) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_JAVA_INVERSE) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_JAVA_INVERSE_REDECLARING) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_SUPERTYPE_INVERSE) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_SUPERTYPE_INVERSE_REDECLARING) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_CURRENT_INVERSE) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else if (attribute_flag == AA_CURRENT_INVERSE_REDECLARING) {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        } else {
  
          // pw.println("\t// " + attribute_object);
          if (select_two_dollars != null) {
  
            // pw.println("\t// " + select_two_dollars);
          }
        }
      }
  
      // end of the loop to find the correct attribute and count the number
      //      result = schema_prefix + entity_prefix + static_attribute_name;
      if (entity_prefix.equalsIgnoreCase("")) {
        result = static_attribute_name;
      } else {
        result = schema_prefix + entity_prefix + static_attribute_name;
      }
  
      return result;
    }
  
  */
  public Object visit(X_AttributeRef node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

//    jc.pw.println("<<<IN AttributeRef-visit>>>>>>> last_left_qualifier : " + jc.last_left_qualifier);

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String attr_name = "";
    String attr_name_method_body = "";
    String attr_name_method_part1 = "";
    String attr_name_method_set = "";
    String attr_name_method_part2 = "";
    String attr_name_method = "";
    String var_name = "";
    String static_attr_name = "";

    //    System.out.println(indentString() + node + " >> " + attr_name_method);
    if (jc.active) {
      printActive("XP Active: AttributeRef");
    }

//    jc.pw.println("<<<IN AttributeRef-visit-before-childrenAccept >>>>>>> last_left_qualifier : " + jc.last_left_qualifier);

    data = node.childrenAccept(this, data);

//    jc.pw.println("<<<IN AttributeRef-visit-after-childrenAccept >>>>>>> last_left_qualifier : " + jc.last_left_qualifier);

// jc.pw.println("###<3> node attribute: " + node.attribute);


    if (node.attribute != null) {
//    jc.pw.println("<<<IN AttributeRef-visit-after-childrenAccept-node.attribute NOT NULL >>>>>>> last_left_qualifier : " + jc.last_left_qualifier);
//      attr_name = node.attribute.getName(null);
      attr_name = getOriginalAttributeName(node.attribute);
      attr_name_method_body = attr_name.substring(0, 1).toUpperCase() + 
                              attr_name.substring(1).toLowerCase() + "(null)";

      //      System.out.println("XP: Attribute reference: attribute is :" +node.attribute +"  attr_name: " +attr_name );
      // attr_name_method_part1 = "test" + attr_name_method_body + "?" +"get" + attr_name_method_body + ":";
      //       testA1(null) ? getA1(null) : null
      //       var1.testA1(null) ? var1.getA1(null) : null
      //         (()var1).testA1(null) ? (()var1).getA1(null) : null
      //         var_name includes also the cast, if needed

// jc.pw.println("<### node.reference ###>: " + node.reference);

      if (node.reference != null) {
        if (node.reference instanceof EParameter) {
          if (((EParameter) node.reference).testName(null)) {
            var_name = ((EParameter) node.reference).getName(null);
          }
        } else if (node.reference instanceof ECtVariable) {
          var_name = ((ECtVariable) node.reference).name;
        }

        var_name += ".";
      } else {
//        jc.generated_java = ".getAttribute(\"" + node.name + "\")";
// System.out.println("XYX-03");
        jc.generated_java = ".getAttribute(\"" + node.name + "\", _context)";

        //					jc.pw.println(".getAttribute0(\"" +node.name +")\"");
      }

      //			System.out.println("Reference: " +node.reference);
// System.out.println("node: " + node + ", name: " + node.name + ", attribute: " + node.attribute);
 
// jc.pw.println("###<0> node reference: " + node.reference);
// jc.pw.println("###<1> last_left_qualifier: " + jc.last_left_qualifier);

 
      if (node.reference == null) {
        static_attr_name = getStaticAttrName(node.attribute, jc);
// System.out.println("static attribute name: " + static_attr_name);


        //       attr_name_method_part1 = var_name + "test" + attr_name_method_body + "?" + var_name + "get" + attr_name_method_body + ":";
        //       attr_name_method_part1 = var_name + "get(" + static_attr_name + ")";
        attr_name_method_part1 = var_name + "Value.alloc(" + node.getStaticTypeFieldName(jc) + 
                                 ").set(_context, get(" + static_attr_name + "))";
        attr_name_method_set = var_name + "set" + attr_name_method_body;
// jc.pw.println("###<0> reference NULL #############");
      } else {
        static_attr_name = getStaticAttrName(node.attribute, jc);
// jc.pw.println("###<1> static attribute name: " + static_attr_name);

// System.out.println("static attribute name: " + static_attr_name);

        //       attr_name_method_part1 = var_name + "test" + attr_name_method_body + "?" + var_name + "get" + attr_name_method_body + ":";
        //       attr_name_method_part1 = var_name + "get(" + static_attr_name + ")";
        if (jc.last_left_qualifier) {
          attr_name_method_part1 = ".setAttribute((jsdai.dictionary.EExplicit_attribute)" + 
                                   static_attr_name + ", ";
//jc.pw.println("###<2> last_left_qualifier - attr_name_method_part1 " + attr_name_method_part1);

          // continue setting value in X_AssignmentStmt() or X_MapAttributeDeclaration()
        } else {
//          attr_name_method_part1 = ".getAttribute(" + static_attr_name + ")";
					String static_attr_name2 = null;
					if (jc.query_logical_expression > 0) {
// System.out.println("XYX-01: " + static_attr_name);

//						static_attr_name2 = "\"" + node.attribute.getName(null).toLowerCase() + "\"";
						static_attr_name2 = "\"" + getOriginalAttributeName(node.attribute).toLowerCase() + "\"";
// jc.pw.println("###<4>  QUERY #########");
					} else {
// System.out.println("XYX-02: " + static_attr_name + ", reference: " + node.reference);
						if (node.reference instanceof EDefined_type) {
							if (((EDefined_type)node.reference).testDomain(null)) {
								if (((EDefined_type)node.reference).getDomain(null) instanceof ESelect_type) {
//									static_attr_name2 = "\"" + node.attribute.getName(null).toLowerCase() + "\"";
									static_attr_name2 = "\"" + getOriginalAttributeName(node.attribute).toLowerCase() + "\"";
								} else {
									// should not happen? perhaps can print an error message
//									static_attr_name2 = "\"" + node.attribute.getName(null).toLowerCase() + "\"";
									static_attr_name2 = "\"" + getOriginalAttributeName(node.attribute).toLowerCase() + "\"";
								}
							} else {
								// should not happen
//								static_attr_name2 = "\"" + node.attribute.getName(null).toLowerCase() + "\"";
								static_attr_name2 = "\"" + getOriginalAttributeName(node.attribute).toLowerCase() + "\"";
							}
						} else {
							static_attr_name2 = static_attr_name;
						}
					}
//          attr_name_method_part1 = ".getAttribute(" + static_attr_name + ", _context)";
// System.out.println("XYX-04: " + static_attr_name);
          attr_name_method_part1 = ".getAttribute(" + static_attr_name2 + ", _context)";
// jc.pw.println("###<3> NOT last_left_qualifier - attr_name_method_part1 " + attr_name_method_part1);
          attr_name_method_set = var_name + "set" + attr_name_method_body;
        }
      }

      if (jc.active) {
        if (!node.already_processed) {
          node.process(jc);
        }

        switch (node.type) {
        case JavaClass.T_INTEGER:
          attr_name_method_part2 = "Integer.MIN_VALUE";

          break;

        case JavaClass.T_REAL:
          attr_name_method_part2 = "Double.NaN";

          break;

        case JavaClass.T_BOOLEAN:
        case JavaClass.T_LOGICAL:
        case JavaClass.T_ENUMERATION:
          attr_name_method_part2 = "0";

          break;

        case JavaClass.T_STRING:
        case JavaClass.T_BINARY:
        case JavaClass.T_ENTITY:
        case JavaClass.T_AGGREGATE:
        case JavaClass.T_ARRAY:
        case JavaClass.T_BAG:
        case JavaClass.T_LIST:
        case JavaClass.T_SET:
        default:
          attr_name_method_part2 = "null";

          break;
        }

        if (node.left_side) {
          printDDebug("XP: AttributeRef - left side");


          //            attr_name_method_set = attr_name_method_part1 + attr_name_method_part2;
          attr_name_method_set = attr_name_method_part1;
        } else {
          printDDebug("XP: AttributeRef - right side");


          //            attr_name_method = attr_name_method_part1 + attr_name_method_part2;
          attr_name_method = attr_name_method_part1;
        }

        jc.generated_java = attr_name_method;
        jc.type_of_operand = node.type;
        jc.type_of_aggregate = node.at;
        jc.type_of_entity = node.ed;
        jc.pw.println();

        //        jc.pw.println("// attr_name_method: " + jc.generated_java + " //");
        //        jc.pw.println("// generated_java: " + jc.generated_java + " //");
      }
    } else { // if node != null

//    jc.pw.println("<<<IN AttributeRef-visit-after-childrenAccept-node.attribute IS NULL !!!! >>>>>>> last_left_qualifier : " + jc.last_left_qualifier);
//    jc.pw.println("<<<IN AttributeRef-visit-after-childrenAccept-node.attribute IS NULL !!!! name: " + node.name + " >>>>>>> last_left_qualifier : " + jc.last_left_qualifier);

      //XXX
//      jc.generated_java = ".getAttribute(\"" + node.name + "\")";
// System.out.println("XYX-05: " + static_attr_name);
      jc.generated_java = ".getAttribute(\"" + node.name + "\", _context)";


      //				jc.pw.println(".getAttribute(\"" +node.name +"\"");
      printDDebug("XP: Attribute reference: attribute is NULL");
    }


    //    jc.pw.println("// generated_java after: " + jc.generated_java + " //");
    // jc.generated_java = attr_name_method;
    jc.indent--;

    return data;
  }

  public Object visit(X_EntityConstructor node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;

    if (!jc.print2string) {
      jc.print2string = true;
      node.print2string_activated = true;
    }

    jc.print_tabs = indentString(jc.indent);

    printActive("XP Active:  EntityConstructor");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    indent_str = indentString(jc.indent + 1);

    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    if (node.java_contains_statements) {
      for (int i = 0; i < node.variable_declarations.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
      }

      for (int i = 0; i < node.statements.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
      }

      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
    }

    if (!jc.print2string) {
			if (jc.in_local_variable) {
	     		jc.local_initializations_str += node.forwarded_java + "\n";
  	   		jc.local_initializations_str += node.generated_java + "\n";
  			} else {
	      	jc.pw.println(node.forwarded_java);
  	    	jc.pw.println(node.generated_java);
    		}
      node.forwarded_java = "";
      node.generated_java = "";
    }

    jc.indent--;

    return data;
  }




  public Object visit(X_EntityConstructorParameter node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;

    if (!jc.print2string) {
      jc.print2string = true;
      node.print2string_activated = true;
    }

    jc.print_tabs = indentString(jc.indent);

    printActive("XP Active:  EntityConstructorParameter");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    indent_str = indentString(jc.indent + 1);

    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    if (node.java_contains_statements) {
      for (int i = 0; i < node.variable_declarations.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
      }

      for (int i = 0; i < node.statements.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
      }

      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
    }

    if (!jc.print2string) {
			if (jc.in_local_variable) {
	    	jc.local_initializations_str += node.forwarded_java + "\n";
  	  	jc.local_initializations_str += node.generated_java + "\n";
  		} else {
		    jc.pw.println(node.forwarded_java);
    	  jc.pw.println(node.generated_java);
      }
      node.forwarded_java = "";
      node.generated_java = "";
    }

    jc.indent--;

    return data;
  }

  public Object visit(X_EntityDecl node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;


// only for debugging
/*
	if (jc.entity instanceof EDerived_attribute) {
		if (((EDerived_attribute)jc.entity).getName(null).equalsIgnoreCase("shaped_product")) {
//			System.out.println("<X_EntityDecl-1>: jc.entity: " + jc.entity + ", jc.ed: " + jc.ed + ", node.entity_definition: " + node.entity_definition + ", jc.sd: " + jc.sd); 
//			jc.pw.println("<X_EntityDecl-1>: jc.entity: " + jc.entity + ", jc.ed: " + jc.ed + ", node.entity_definition: " + node.entity_definition + ", jc.sd: " + jc.sd); 
		}
		if (node.entity_definition.getName(null).equalsIgnoreCase("Contextual_item_shape")) {
//			System.out.println("<X_EntityDecl-2>: node.entity_definition: " + node.entity_definition + ", jc.entity: " + jc.entity + ", jc.ed: " + jc.ed); 
//			jc.pw.println("<X_EntityDecl-2>: node.entity_definition: " + node.entity_definition + ", jc.entity: " + jc.entity + ", jc.ed: " + jc.ed); 
		}
	} else {
		if (node.entity_definition.getName(null).equalsIgnoreCase("Contextual_item_shape")) {
//			System.out.println("<X_EntityDecl-3>: node.entity_definition: " + node.entity_definition + ", jc.entity: " + jc.entity + ", jc.ed: " + jc.ed); 
//			jc.pw.println("<X_EntityDecl-3>: node.entity_definition: " + node.entity_definition + ", jc.entity: " + jc.entity + ", jc.ed: " + jc.ed); 
		}
	
	}

*/

    // this approach ignores the possibility of nested entity declarations
    if (jc.entity == null) {
      return data;
    }

// System.out.println("XIXIX 0: " + jc.ed + ", 2: " + node.entity_definition);
    if (jc.ed != node.entity_definition) {
      // check if artificial inheritance
      if (jc.entity instanceof EDerived_attribute) {
        EEntity_definition ped = (EEntity_definition) ((EDerived_attribute) jc.entity).getParent_entity(
                                       null);

        if (ped.getClass().isAssignableFrom(jc.ed.getClass())) {
          // here we need to simulate artificial inheritance. Maybe nothing is needed.
        } else {
//          return data;
        }
      } else {
//        return data;
      }

      //      return data;
    }

    jc.indent++;

    //    String entity_name = node.entity_definition.getName(null);
    if (jc.active) {
      printActive("XP Active: EntityDecl");
    }

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_TypeDecl node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

//    if (jc.entity == null) {
//      return data;
//    }


    jc.indent++;

    //    String entity_name = node.entity_definition.getName(null);
    if (jc.active) {
      printActive("XP Active: TypeDecl");
    }

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }


  public Object visit(X_MapDecl node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (jc.entity == node.map_definition) {
      jc.active = true;
    } else {
      return data;
    }


    //    String entity_name = node.entity_definition.getName(null);
    jc.indent++;

    if (jc.active) {
      printActive("XP Active: MapDecl");
    }

    data = node.childrenAccept(this, data);

    if (jc.entity == node.map_definition) {
      jc.active = false;
      jc.generated_java = "";
    }

    jc.indent--;

    return data;
  }


  public Object visit(X_DependentMapDecl node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (jc.entity == node.definition) {
      jc.active = true;
    } else {
      return data;
    }


    //    String entity_name = node.entity_definition.getName(null);
    jc.indent++;

    if (jc.active) {
      printActive("XP Active: DependentMapDecl");
    }

    data = node.childrenAccept(this, data);

    if (jc.entity == node.definition) {
      jc.active = false;
      jc.generated_java = "";
    }

    jc.indent--;

    return data;
  }




  public Object visit(X_Interval node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: Interval");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_IntervalLow node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: IntervalLow");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_IntervalHigh node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: IntervalHigh");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_IntervalOp node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: IntervalOp");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_IntervalItem node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: IntervalItem");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_QueryExpression node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: QueryExpression");

		//		if(jc.in_local_variable && (node.parameter_type != null))
		if(jc.in_local_variable)
			jc.pd = null; // set to null, otherwise in this place aggregate with wrong type could be constructed

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public static ESchema_definition findSchema(SdaiModel model) throws SdaiException {
    if (model.getMode() == 0) {
      model.startReadOnlyAccess();
    }
    Aggregate instances = model.getInstances(ESchema_definition.class);
    SdaiIterator it = instances.createIterator();
    if (it.next()) {
      return (ESchema_definition)instances.getCurrentMemberObject(it);
    } else {
      return null;
    }
  }

  public Object visit(X_SchemaDecl node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;


/*
if (jc.entity != null ) {
	ESchema_definition sched = findSchema(jc.entity.findEntityInstanceSdaiModel());
	jc.pw.println("// XIXIX sd: " + jc.sd + ", node-sd: " + node.schema_definition + ", entity-sd: " + sched);
} else {
	jc.pw.println("// XIXIX 11: " + jc.sd + ", 20: " + node.schema_definition);
}
*/

// System.out.println("XIXIX 10: " + jc.sd + ", 20: " + node.schema_definition);


// debugging only

/*
	if (jc.entity instanceof EDerived_attribute) {
//		if (((EDerived_attribute)jc.entity).getName(null).equalsIgnoreCase("shaped_product")) {
		if (((EDerived_attribute)jc.entity).getName(null).equalsIgnoreCase("item_id")) {
//			System.out.println("<X_SchemaDecl-1>: jc.entity: " + jc.entity + ", jc.ed: " + jc.ed + ", node.schema_definition: " + node.schema_definition + ", jc.sd: " + jc.sd); 
			jc.pw.println("// <X_SchemaDecl-1>: jc.entity: " + jc.entity + ", jc.ed: " + jc.ed + ", node.schema_definition: " + node.schema_definition + ", jc.sd: " + jc.sd); 
		}
	}
*/


/*
	if (jc.entity instanceof EDerived_attribute) {
//		if (jc.entity.findEntityInstanceSdaiModel().getUnderlyingSchema() != node.schema_definition) {
		if (jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() != node.schema_definition) {
			return data;
		}
	}
*/

/*
    if (jc.sd != node.schema_definition) {
//			jc.pw.println("// jc.sd != node.schema_definition - previously disabled, but needed in some multi-schema cases"); 
//			jc.pw.println("// jc.sd != node.schema_definition - skipping"); 
//      return data;
    } else {
//			jc.pw.println("// jc.sd = node.schema_definition - YES, IT IS EQUAL"); 
    }
*/

//		if (jc.entity.findEntityInstanceSdaiModel().getUnderlyingSchema() != node.schema_definition) {


// debugging
/*
	  if (jc == null) {
	  	System.out.println("jc = null");
	  } else {
	  	if (jc.entity == null) {
		  	System.out.println("jc.entity = null");
  			System.out.println("jc.entity: " + jc.entity + ", jc.ed: " + jc.ed + ", node.schema_definition: " + node.schema_definition + ", jc.sd: " + jc.sd); 
	  	} else {
		  	if (jc.entity.findEntityInstanceSdaiModel() == null) {
			  	System.out.println("jc.entity.findEntityInstanceSdaiModel() = null, entity: " + jc.entity);
	  		} else {
			  	if (jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() == null) {
				  	System.out.println("jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() = null, model: " + jc.entity.findEntityInstanceSdaiModel());
	  			}
	  		}
	  	}
	  }
*/



// 4 errors
/*
  	if (jc.entity != null) {
			if( 
				(jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() != node.schema_definition) 
				&&
				(jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() != jc.sd) 
				
				) {
				return data;
			}
		}
*/


// 4 errors
/*
	if (jc.entity instanceof EDerived_attribute) {
		if( 
			(jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() != node.schema_definition) 
			&&
			(jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() != jc.sd) 
			) {
				return data;
			}
	} else {
    if (jc.sd != node.schema_definition) {
      return data;
    } else {
    }
	}
*/


// 2 errors
/*
	if (jc.entity instanceof EDerived_attribute) {
		if( 
			(jc.entity.findEntityInstanceSdaiModel().getDefinedSchema() != node.schema_definition) 
			&&
			(jc.sd != node.schema_definition) 
			) {
				return data;
			}
	} else {
    if (jc.sd != node.schema_definition) {
      return data;
    } else {
    }
	}
*/

// let's try a partial optimization: if derived attribute - go to each, otherwise - as the original implementatino
// that may be needed also for explicit redeclared attributes, better approach would be any redeclared attribute
    if (jc.sd != node.schema_definition) {
//			jc.pw.println("// jc.sd != node.schema_definition, "); 
			if (jc.entity != null) {
//					jc.pw.println("// jc.entity: " + jc.entity); 
//				 	ESchema_definition sched = (ESchema_definition)jc.entity.findEntityInstanceSdaiModel().getDefinedSchema();

//				 	ESchema_definition sched = findSchema(jc.entity.findEntityInstanceSdaiModel());
//					if (sched != node.schema_definition) {
					if (findSchema(jc.entity.findEntityInstanceSdaiModel()) != node.schema_definition) {
//						jc.pw.println("// jc.entity schema_definition != node.schema_definition - RETURN: " + sched); 
		      	return data;
					} else {
//						jc.pw.println("// jc.entity schema_definition = node.schema_definition - continue: " + sched); 
					}
	   	} else { // jc.entity = null
//				jc.pw.println("// jc.entity == null - RETURN"); 
    		return data;
    	}
    } else {
//			jc.pw.println("// jc.sd = node.schema_definition - YES, IT IS EQUAL - continue"); 
    }




    jc.indent++;

    String schema_name = node.schema_name;
    printDDebug("SCHEMA DECLARATION : " + node.schema_name);
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_AlgorithmHead node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: AlgorithmHead");
	
	/*
		if (!(jc.in_function || jc.in_procedure)) {
			if (!jc.declare_rule.equals("")) {
				jc.pw.println(jc.declare_rule);
				jc.declare_rule = "";
			}
		}
*/

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_AliasStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: AliasStmt");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_CaseStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";
    String tmp_str2 = "";
    String tmp_str1 = "";

    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;
        ((X_CaseStmt) node).selector = "";

        // jc.generated_java = "";
        // jc.print_string = "";
      }


      // jc.generated_java = "";
      jc.print_tabs = indentString(jc.indent);
    }

    if (jc.active) {
      printActive("XP Active: CaseStmt");
    }

    data = node.childrenAccept(this, data);

    if (jc.active) {
      indent_str = indentString(jc.indent + 1);


      //       // cannot use switch because any expression is allowed in express
      //       switch (selector)
      //       {
      //          case  1:
      //          case  2: 
      //          default: // OTHERWISE
      //       }
      //       // using if - else if - else instead:
      //       if (selector == 1) {
      //       } else 
      //       if (selector == 2) {
      //       } else { // OTHERWISE
      //       }
      //       if (case_selector == case_label1) {
      //       } else
      //       if (case_selector == case_label2) {
      //       } else {
      //       }
      //       // one case may have more than one case label:
      //       if ((case_selector == case_label1) || (case_selector == case_label2)) {
      //       } else
      //       if (case_selector == case_label3) {
      //       } else {
      //       }
      //         jc.pw.println("nr of actions = " + node.number_of_actions);
      // tmp_str = jc.print_string;
      // jc.pw.println("SELECTOR: " + node.selector);
      // first_time = true;
      tmp_str = "";

      for (int i = 0; i < node.number_of_actions; i++) {
        tmp_str2 += (String) node.actions.elementAt(i);
        tmp_str += ("\n" + indentString(jc.indent + 1) + tmp_str2);


        // jc.pw.println(tmp_str2);
        //            tmp_str += "\n" + indentString(jc.indent+2) + node.statements.elementAt(i);
        tmp_str += (indentString(jc.indent + 2) + node.statements.elementAt(i));

        // jc.pw.println(node.statements.elementAt(i));
        if ((i == node.number_of_actions - 1) && (!node.otherwise_present)) {
          tmp_str += ("\n" + indentString(jc.indent + 1) + "}");

          // jc.pw.println("}");
        } else {
          tmp_str += ("\n" + indentString(jc.indent + 1) + "} else");

          // jc.pw.println("} else");
        }

        tmp_str2 = "";
      }

      if (node.otherwise_present) {
        //            tmp_str += " {" + "\n" + indentString(jc.indent+2) + node.otherwise;
        tmp_str += (" {" + indentString(jc.indent + 2) + node.otherwise);
        tmp_str += ("\n" + indentString(jc.indent + 1) + "}");

        // jc.pw.println("{" + node.otherwise);
        // jc.pw.println("}");
      }


      //         tmp_str += node.
      //         tmp_str += "\n" + indentString(jc.indent+1) + "if (" + node.condition_exp + ") {";
      //         tmp_str += indent_str + node.if_statements;
      //         if (node.else_count > 0) {
      //            tmp_str += "\n" + indent_str + " } else {";
      //            tmp_str += indent_str + node.else_statements;
      //         }
      // tmp_str += "\n" + indentString(jc.indent+1) + "}";
      //      jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      node.generated_java += tmp_str;

      if (node.print2string_activated) {
        jc.print2string = false;
				// WARN: dosnt exist in V3
//         jc.pw.println(jc.print_string);
//         jc.print_string = "";
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      if (!jc.print2string) {
	//			if (true) { // check if in function or procedure, otherwise - the old way
				if (jc.in_function || jc.in_procedure) {
        	jc.statements_str += node.forwarded_java + "\n";
        	jc.statements_str += node.generated_java + "\n";
      	} else { 
        	jc.pw.println(node.forwarded_java);
        	jc.pw.println(node.generated_java);
        }
        node.forwarded_java = "";
        node.generated_java = "";
      } else {
        node.generated_java = node.forwarded_java + node.generated_java;
        node.forwarded_java = "";
      }

      /*
          if (node.print2string_activated) {
            jc.print2string = false;
            jc.pw.println(node.generated_java);
            node.generated_java = "";
            node.print2string_activated = false;
          }
      */
    }

    jc.indent--;

    return data;
  }




  public Object visit(X_CaseSelector node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: CaseSelector");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_CaseAction node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: CaseAction");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }


  public Object visit(X_BindingHeader node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

		jc.active = true;

    jc.indent++;
    printActive("XP Active: BindingHeader");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

//		jc.active = false;
    return data;
  }

  public Object visit(X_DepMapPartition node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

		jc.active = true;

    jc.indent++;
    printActive("XP Active: DepMapPartition");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

//		jc.active = false;
    return data;
  }

  public Object visit(X_SubtypeBindingHeader node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

		jc.active = true;

    jc.indent++;
    printActive("XP Active: SubtypeBindingHeader");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

//		jc.active = false;
    return data;
  }


  public Object visit(X_CaseLabel node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: CaseLabel");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_CaseOtherwise node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: CaseOtherwise");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_CompoundStmt node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;
    String indent_str = "";
    String tmp_str = "";

    if (!jc.active) {
      return data;
    }

    if (!jc.print2string) {
      jc.print2string = true;
      node.print2string_activated = true;
    }

    jc.indent++;


    //    if (jc.active) {
    //      tmp_str = "\t{";
    //    }
    printActive("XP Active: CompoundStmt");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    indent_str = indentString(jc.indent + 1);

    node.opening_java += ("\n" + indent_str + "{ // BEGIN");
    node.generated_java += ("\n" + indent_str + "} // END");

    //      jc.print_string += jc.generated_java + "\t}";
    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    if (node.java_contains_statements) {
      for (int i = 0; i < node.variable_declarations.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
      }

      for (int i = 0; i < node.statements.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
      }

      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
    }


    if (!jc.print2string) {
    	// if (true) { // if in function or procedure, otherwise - the old way
			if (jc.in_function || jc.in_procedure) {
        	jc.statements_str += node.opening_java + "\n";
        	jc.statements_str += node.forwarded_java + "\n";
        	jc.statements_str += node.generated_java + "\n";
    	} else {
      	jc.pw.println(node.opening_java);
      	jc.pw.println(node.forwarded_java);
      	jc.pw.println(node.generated_java);
			}
      node.generated_java = "";
    } else {
      //        jc.generated_java = jc.print_before_first_string + jc.print_first_string + jc.print_string;
      //        jc.print_string = jc.print_before_first_string + jc.print_first_string + jc.print_string;
      node.generated_java = node.opening_java + node.forwarded_java + node.generated_java;
    }

    node.opening_java = "";
    node.forwarded_java = "";


    //      jc.print_before_first_string = "";
    //      jc.print_first_string = "";
    // jc.active = false;
    // jc.generated_java = "";
    jc.indent--;

    return data;
  }



  public Object visit(X_ConstantDecl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;


//System.out.println("CCCCCCC- Constant: 

    if (!jc.active) {
    	// return data;
    }
    if (!(jc.entity instanceof EConstant_definition)) {
 				if (!node.inner_constant) {
 					return data;
 				} else {
			    if (!jc.active) {
			    	return data;
    			} else {
//System.out.println("<CONSTANT-01> node: " + node + ", schema: " +  jc.sd.getName(null));
    			}
 				}
    } else {
	    if (!jc.active) {
//  	  	return data;
    	}
//System.out.println("<CONSTANT-02> node: " + node + ", schema: " + jc.sd.getName(null) + ", entity: " + jc.entity + ", ed: " + jc.ed + ", inner: " + jc.inner_constant + ", model: " + jc.model + ", flag: " + jc.flag_value);
    }
    jc.indent++;
    printActive("XP Active: ConstantDecl");
    jc.in_constant_definition = true;

		// let's accumulate this stuff for all constants (done in X_ConstantBody)
		jc.constant_static_field_str = "";
		jc.constant_method_str = "";
		jc.inner_constant = node.inner_constant;

    data = node.childrenAccept(this, data);
    jc.in_constant_definition = false;

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_ConstantBody node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;
    String indent_str;

    if (!jc.inner_constant) {
    	if (jc.entity != node.cd) {
	      return data;
    	}
		}
    jc.active = true;

    if (!jc.print2string) {
      jc.print2string = true;
      node.print2string_activated = true;
    }

    jc.indent++;
    printActive("XP Active: ConstantBody");

    String tmp_str;

    String name = "c" + node.name.substring(0, 1).toUpperCase() + 
                  node.name.substring(1).toLowerCase();
    String field_name = "_CONSTANT_" + node.name.toUpperCase();

    String static_parameter_type_str = node.getStaticTypeString(jc);
    String field = "\tstatic Value " + field_name + " = null;";
    String field_inner = "\tValue " + field_name + " = null;";
    String signature = "\tpublic static Value " + name + 
                       "(SdaiContext _context) throws SdaiException {";
    String signature_inner = "\tpublic Value " + name + 
                       "(SdaiContext _context) throws SdaiException {";

		if (jc.inner_constant) {
//			jc.constant_static_field_str += field + "\n";
//			jc.constant_method_str += signature + "\n";
			jc.constant_static_field_str += field_inner + "\n";
			jc.constant_method_str += signature_inner + "\n";
		} else {
    	jc.pw.println(field);
    	jc.pw.println(signature);
		}
    indent_str = indentString(jc.indent + 1);


    //    tmp_str = "\t\treturn (Value.alloc(" + static_parameter_type_str + ").set(_context, ";
    tmp_str = "\t\tif (" + field_name + " == null) { \n\t\t\t" + field_name + " = Value.alloc(" + 
              static_parameter_type_str;

    jc.flag_alloc_type = true;
    jc.alloc_type_depth = jc.indent;
    jc.alloc_type = node.getStaticTypeString(jc);

    data = node.childrenAccept(this, data);

    jc.flag_alloc_type = false;

    if (jc.generated_java.equals("_JUST_CREATE_")) {
      jc.generated_java = "";
      tmp_str += ").create(";
    } else {
      tmp_str += ").set(_context, ";
    }

    tmp_str += (jc.generated_java + ");\n\t\t}\n\t\treturn " + field_name + ";\n\t}");


    //      jc.print_string = tmp_str;
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    node.generated_java += tmp_str;

    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    if (node.java_contains_statements) {
      for (int i = 0; i < node.variable_declarations.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
      }

      for (int i = 0; i < node.statements.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
      }

      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
    }

    if (!jc.print2string) {

			
		if (jc.inner_constant) {
			jc.constant_method_str += node.forwarded_java + "\n";
			jc.constant_method_str += node.generated_java + "\n";
		} else {
      jc.pw.println(node.forwarded_java);
      jc.pw.println(node.generated_java);
    }
     node.forwarded_java = "";
     node.generated_java = "";
   }

    if (!jc.inner_constant) {
	    jc.active = false;
    	jc.generated_java = "";
		}
    jc.indent--;

    return data;
  }

  public Object visit(X_ConstantRef node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    String model_name = node.cd.findEntityInstanceSdaiModel().getName();

    // String the_name = schema_package + ".C" + entity_name.substring(0, 1).toUpperCase() + entity_name.substring(1).toLowerCase() + ".class";
    
    String name = "_no_name";
    
    	if (node.depth > 0) {
    		name = node.constructConstantReference();
    	} else {
     		name = constructTypeSchemaPrefixFromModel(model_name) + "." + 
	                    constructSchemaClassNamePrefixFromModel(model_name) + 
	                    ".c" + node.name.substring(0, 1).toUpperCase() + node.name.substring(1).toLowerCase();

			}
    jc.indent++;
    printActive("XP Active: ConstantRef");
    data = node.childrenAccept(this, data);
    jc.generated_java = name + "(_context)";
    jc.indent--;

    return data;
  }

  public Object visit(X_EscapeStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";

    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;
      }
      jc.print_tabs = indentString(jc.indent);
    }


    printActive("XP Active: EscapeStmt");
    data = node.childrenAccept(this, data);

    if (jc.active) {
      indent_str = indentString(jc.indent + 1);
      tmp_str += ("\n" + indentString(jc.indent + 1) + "break;");


      //      jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      node.generated_java += tmp_str;

      //     jc.print_string += tmp_str;
      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      if (!jc.print2string) {
	//    	if (true) {  // if in function or procedure, not necessary inner, if not - print the old way (if other cases even possible?)
				if (jc.in_function || jc.in_procedure) {
	     		jc.statements_str += node.opening_java + "\n";
	     		jc.statements_str += node.forwarded_java + "\n";
  	   		jc.statements_str += node.generated_java + "\n";
  			} else {
        	jc.pw.println(node.opening_java);
        	jc.pw.println(node.forwarded_java);
        	jc.pw.println(node.generated_java);
        }
        jc.print_before_first_string = "";
        jc.print_first_string = "";
        jc.print_string = "";
      } else {
        node.generated_java = node.forwarded_java + node.generated_java;
        node.forwarded_java = "";
      }


	}


    jc.indent--;

    return data;
  }






  public Object visit(X_FunctionDecl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;
    String signature = "";
    String type;
    String name;
    String parameters;

//System.out.println("");
//System.out.println("jc.entity: " + jc.entity);
//System.out.println("jc.ed: " + jc.ed);

    if (jc.entity == null) {
//      return data;
    }

    if (jc.ed != null) {
//      return data;
    }

    if (jc.entity != node.fd) {
//      return data;
    }

    jc.indent++;
//    jc.statements_str = "";

    if (jc.entity == node.fd) {
    	jc.inner = false;
      jc.active = true;
      jc.in_function = true;
			jc.keynode = node;
// System.out.println("#@# starting outer function, setting keynode to: " + node.fd.getName(null)); 
      jc.return_type = node.fd.getReturn_type(null);

      String original_name = node.fd.getName(null);
      name = "f" + original_name.substring(0, 1).toUpperCase() + 
             original_name.substring(1).toLowerCase();
      type = node.getType(jc);
      parameters = node.getParameters();


      //         signature = "\tpublic " + type + " " + name + "(" + parameters + ") throws SdaiException {";
      //old - not separate files         signature = "\tpublic static Value " + name + "(" + parameters + ") throws SdaiException {";
      global_function_name = node.fd.getName(null);
      global_return_number = 1;
      if (flag_split_debug) {
//     signature = "\tpublic static Value run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting, parameters: \"" + parameters + ");";
             	signature = "\tpublic Value run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting\");";
    	} else {
      	signature = "\tpublic Value run(" + parameters + ") throws SdaiException {";
      }
    } else
//    if (Support.getParentFunctionDefinition(node.fd) != null) {
    if (Support.getParentFunctionProcedureRuleDefinition(node.fd) != null) {
    	//  inner function, may also search for inner_declaration, if needed
      	jc.inner = true;


      jc.active = true;
      jc.in_function = true;
			jc.keynode = node;
// System.out.println("#@# starting inner function, setting keynode to: " + node.fd.getName(null)); 

      jc.return_type = node.fd.getReturn_type(null);

      String original_name = node.fd.getName(null);
      name = "f" + original_name.substring(0, 1).toUpperCase() + 
             original_name.substring(1).toLowerCase();
      type = node.getType(jc);
      parameters = node.getParameters();


      //         signature = "\tpublic " + type + " " + name + "(" + parameters + ") throws SdaiException {";
      //old - not separate files         signature = "\tpublic static Value " + name + "(" + parameters + ") throws SdaiException {";
      global_function_name = node.fd.getName(null);
      global_return_number = 1;



      if (flag_split_debug) {
//      	signature = "\tpublic static Value run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting, parameters: \"" + parameters + ");";
      	signature = "\tValue run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting\");";
    	} else {
      	signature = "\tValue run(" + parameters + ") throws SdaiException {";
      }
    } else {
    	return data;
    }

    if (jc.active) {
      if (node.fd != null) {
        printActive("XP Active: FunctionDecl: " + node.fd.getName(null) + ", ID: " + 
                           node.id);

        for (int jak = 0; jak < node.children.length; jak++) {
          //          System.out.println("Child: " + (( SimpleNode )node.children[jak]).id);
        }
      } else {
        printActive("XP Active: FunctionDecl is NULL");
      }
    }

    data = node.childrenAccept(this, data);

    // this may prevent inner functions to be printed


//    if (jc.entity == node.fd) {
    if (jc.active) {

			// moved here from above, so that inner functions are printed first
      // not so easy to do - stuff inside the function is printed directly in childrenAccept, need to postpone
      String f_name = null;
      String public_str = "";

      String class_name;
      String parent_class_name = null;

//      EFunction_definition parent_f = Support.getParentFunctionDefinition(node.fd);
      EEntity parent_f = Support.getParentFunctionProcedureRuleDefinition(node.fd);
			if (parent_f == null) {
				public_str = "public ";
			}
	
      if (node.fd != null) {
      	f_name = node.fd.getName(null);
    	} else {
    		f_name = "function_definition_null";
    	}
      if (parent_f == null) {
      	class_name = "F" + f_name.substring(0, 1).toUpperCase() + f_name.substring(1).toLowerCase();
	    } else {
	    	// construct the name of the inner function class
// System.out.println("#@#@# constructing name for inner function class: " + node.fd.getName(null)); 

	    	class_name = node.constructFunctionClassName();
	  		parent_class_name = node.constructFunctionProcedureRuleClassName(parent_f);
// System.out.println("#@#@# constructed names - class: " + class_name + ", parent class: " + parent_class_name); 
	    }
	    jc.pw.println("");
      jc.pw.println(public_str + "class " + class_name + " {\n	");

			if (parent_f != null) {
				jc.pw.println("\t" + parent_class_name + " parent;\n");
			}

/*
33333333333 only if inner, FAaa - outer name

  FAaa parent;

3333333333333333333
*/

			if (!jc.constant_static_field_str.equals("")) {
				jc.pw.println(jc.constant_static_field_str);
				jc.constant_static_field_str = "";
			}


      node.makeNonVar(jc);
			jc.pw.println(jc.parameter_declarations_str);
	    jc.parameter_declarations_str = "";		
			jc.pw.println(jc.local_declarations_str);
			jc.local_declarations_str = "";

			if (parent_f != null) {
				
				jc.pw.println("\t" + class_name + "(" + parent_class_name + " parent) {");
	      jc.pw.println("\t\tthis.parent = parent;");
  	    jc.pw.println("\t}\n");

			}


			if (!jc.constant_method_str.equals("")) {
				jc.pw.println(jc.constant_method_str);
				jc.constant_method_str = "";
			}


/*
3333333333333333333 constructor - only if inner, FBbb - my own name, FAaa - my parent name
  ...
  FBbb(FAaa parent) {
    this.parent = parent;
  }


333333333333333
*/
      jc.pw.println(signature);

			// generate accumulated stuff here, perhaps
	
			jc.pw.println(jc.parameter_initializations_str);
	    jc.parameter_initializations_str = "";		
			jc.pw.println(jc.local_initializations_str);
			jc.local_initializations_str = "";
      jc.pw.println(jc.statements_str);
      jc.statements_str = "";
			jc.pw.println(jc.return_str);
			jc.return_str = "";
      // node.restoreNonVar(jc);
      //         jc.pw.println("\t\t\t\t" + jc.generated_java);
//      jc.pw.println("\t\treturn null; // if return is missing in express");
      jc.pw.println("\t\treturn Value.alloc(ExpressTypes.GENERIC_TYPE).unset(); // if return is missing in express - add printing of error message?");
      jc.pw.println("\t}");
      jc.pw.println("");
      jc.pw.println("}");

			if (parent_f == null) { // the outer function, has no function parent
      	jc.inner = false;
      	jc.active = false;
		    jc.in_function = false;
		    jc.keynode = null;
// System.out.println("#@# ending outer function, keynode to NULL: " + node.fd.getName(null)); 
			} else {
				if (parent_f instanceof EFunction_definition) {
					jc.in_function = true;
					jc.in_procedure = false;
				} else 
				if (parent_f instanceof EProcedure_definition) {
					jc.in_function = false;
					jc.in_procedure = true;
				} else 
				if (parent_f instanceof EGlobal_rule) {
					jc.in_function = false;
					jc.in_procedure = false;
				}
				jc.keynode = node;
// System.out.println("#@# ending inner function, setting keynode to (itself - cannot be right?): " + node.fd.getName(null) + ", parent: " + parent_f); 
			}

      jc.generated_java = "";
    }

    jc.indent--;
    jc.variable_ids = null;

    return data;
  }

  public Object visit_prev(X_FunctionDecl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;
    String signature;
    String type;
    String name;
    String parameters;

//System.out.println("");
//System.out.println("jc.entity: " + jc.entity);
//System.out.println("jc.ed: " + jc.ed);

    if (jc.entity == null) {
      return data;
    }

    if (jc.ed != null) {
      return data;
    }

    if (jc.entity != node.fd) {
      return data;
    }

    jc.indent++;

    if (jc.entity == node.fd) {
      jc.active = true;
      jc.in_function = true;
			jc.keynode = node;
      jc.return_type = node.fd.getReturn_type(null);

      String original_name = node.fd.getName(null);
      name = "f" + original_name.substring(0, 1).toUpperCase() + 
             original_name.substring(1).toLowerCase();
      type = node.getType(jc);
      parameters = node.getParameters();


      //         signature = "\tpublic " + type + " " + name + "(" + parameters + ") throws SdaiException {";
      //old - not separate files         signature = "\tpublic static Value " + name + "(" + parameters + ") throws SdaiException {";
      global_function_name = node.fd.getName(null);
      global_return_number = 1;
      if (flag_split_debug) {
//      	signature = "\tpublic static Value run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting, parameters: \"" + parameters + ");";
      	signature = "\tpublic static Value run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting\");";
    	} else {
      	signature = "\tpublic static Value run(" + parameters + ") throws SdaiException {";
      }
      jc.pw.println(signature);
      node.makeNonVar(jc);
    }

    if (jc.active) {
      if (node.fd != null) {
        printActive("XP Active: FunctionDecl: " + node.fd.getName(null) + ", ID: " + 
                           node.id);

        for (int jak = 0; jak < node.children.length; jak++) {
          //          System.out.println("Child: " + (( SimpleNode )node.children[jak]).id);
        }
      } else {
        printActive("XP Active: FunctionDecl is NULL");
      }
    }

    data = node.childrenAccept(this, data);

    if (jc.entity == node.fd) {
      jc.active = false;


      // node.restoreNonVar(jc);
      //         jc.pw.println("\t\t\t\t" + jc.generated_java);
      jc.pw.println("\t\treturn null;");
      jc.pw.println("\t}");
      jc.generated_java = "";
    }

    jc.indent--;
    jc.in_function = false;
    jc.variable_ids = null;

    return data;
  }


  public Object visit(X_ListOfStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
	    printActive("XP Active: ListOfStmt");
    data = node.childrenAccept(this, data);
    printActive("XP Active OUT: ListOfStmt");

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_FunctionHead node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: FunctionHead");
    data = node.childrenAccept(this, data);
    jc.indent--;

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_IfStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";

    // boolean print2string_activated = false;
    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;

        // jc.generated_java = "";
        // jc.print_string = "";
      }


      // jc.generated_java = "";
      jc.print_tabs = indentString(jc.indent);
    }

    //      if (jc.active) {
    //         if (!jc.print2string) {
    //            jc.print2string = true;
    //           jc.print2string_activated = true;
    //        }
    //        jc.generated_java = "";
    //         jc.print_tabs = "\t\t\t";
    //      }
    if (jc.active) {
      printActive("XP Active: IfStmt");
    }

    data = node.childrenAccept(this, data);

    if (jc.active) {
      indent_str = indentString(jc.indent + 1);
      tmp_str += ("\n" + indentString(jc.indent + 1) + "if (" + node.condition_exp + ".getLogical() == 2) {");


      // tmp_str += indentString(jc.indent+1) + "if (" + node.condition_exp + ") {";
      //         tmp_str += "\n" + indentString(jc.indent+1) + "if (" + jc.generated_java + ") {";
      //         jc.pw.println(node.if_statements);
      tmp_str += (indent_str + node.if_statements);

      if (node.else_count > 0) {
        tmp_str += ("\n" + indent_str + "} else {");


        //            tmp_str += "\n\t" + indent_str + node.else_statements;
        tmp_str += (indent_str + node.else_statements);
      }

      tmp_str += ("\n" + indentString(jc.indent + 1) + "}");


      //      jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      node.generated_java += tmp_str;

      //     jc.print_string += tmp_str;
      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      if (!jc.print2string) {
	//			if (true) {  //  check if in function, procedure, otherwise - the old way
				if (jc.in_function || jc.in_procedure) {
        	jc.statements_str += node.opening_java + "\n";
        	jc.statements_str += node.forwarded_java + "\n";
        	jc.statements_str += node.generated_java + "\n";
				} else {
        	jc.pw.println(node.opening_java);
        	jc.pw.println(node.forwarded_java);
        	jc.pw.println(node.generated_java);
      	}
        jc.print_before_first_string = "";
        jc.print_first_string = "";
        jc.print_string = "";
      } else {
        node.generated_java = node.forwarded_java + node.generated_java;
        node.forwarded_java = "";
      }
    }

    jc.indent--;
    printActive("XP Active Ending: IfStmt");

    return data;
  }


  // currently not made for nested
  public Object visit(X_LocalDecl node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }
	
/*	
		if (!(jc.in_function || jc.in_procedure)) {
			if (!jc.declare_rule.equals("")) {
				jc.pw.println(jc.declare_rule);
				jc.declare_rule = "";
			}
		}
*/

    jc.indent++;

    // moving after data
    if (jc.active) {
//      jc.pw.println("\t\t// Declarations of local variables");
    }

    jc.variable_ids = node.local_variable_ids;
    printActive("XP Active: LocalDecl");

    if (jc.active) {
      jc.local_declarations_str = "\t// declaration of local variables\n";
      jc.local_initializations_str = "\t\t// initialization of local variables\n";
    }


    //    jc.init_rule = new Vector();
    data = node.childrenAccept(this, data);


    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    if (jc.active) {
//      jc.pw.println("\t\t// End of declarations of local variables\n");
      jc.local_declarations_str += "\t// end of declaration of local variables\n\n";
      jc.local_initializations_str += "\t\t// end of initialization of local variables\n\n";
    }

    jc.indent--;

    return data;
  }

  public Object visit(X_AssignmentStmt node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    String indent_str;
    jc.indent++;

    String tmp_str = "";
    Object general_reference;
    String left_side = "notImplemented";

    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;
      }

      jc.print_tabs = indentString(jc.indent);
      jc.generated_java = "";
      general_reference = node.general_reference;

      if (general_reference instanceof ECtVariable) {
        ECtVariable ec_var = (ECtVariable) general_reference;
        String var_name = "_e_" + ec_var.getName();
        left_side = var_name;
      } else if (general_reference instanceof EParameter) {
        EParameter ec_par = (EParameter) general_reference;
        String par_name = "_e_" + ec_par.getName(null);

        if ((jc.in_function) || (jc.in_procedure)) {
          left_side = "_nonvar_" + par_name;
        } else {
          // it is not so simple, procedure has not  only VAR, maybe better is to restore VAR back
          left_side = par_name;
        }
      }

      jc.flag_non_temporary_value_instance = true;
      jc.value_instance = left_side + ".";
      jc.assignment_depth = jc.indent;
      printDDebug("XC assignment ECxGenerateJava indent: " + jc.indent + ", left: " + 
                         jc.value_instance);
    }

    if (jc.active) {
      printActive("XP Active: AssignmentStmt: " + node.id + ", parent: " + 
                         ((SimpleNode) node.parent).id);
    }
		
		jc.left_side_indexing = 0;

    data = node.childrenAccept(this, data);


// jc.pw.println("//###-02 right_str: " + node.right_str);

    if (jc.active) {
      printDDebug("XP OUT: AssignmentStmt: " + node.id + ", parent: " + 
                         ((SimpleNode) node.parent).id);
    }

    if (jc.active) {
      indent_str = indentString(jc.indent + 1);

      //         if (jc.print_string.length() == 0) {
      //            tmp_str = "\n" + indent_str + left_side + " = " + jc.generated_java + ";";
      //            jc.print_string = "";
      //            jc.generated_java = "";
      //         } else {
      //            tmp_str = "\n" + indent_str + left_side + " = " + jc.print_string + ";";
      //            jc.print_string = "";
      //            jc.generated_java = "";
      //         }
      //         jc.flag_non_temporary_value_instance = true;
      //         jc.value_instance = left_side;
      if (!jc.flag_non_temporary_value_instance) {
        //  left_side = ""; // there are unclear cases where to generate whole expression - 
        // in opper level or in X_Classes. Ex. PowerOp - it can be in assignment and alone.
        if (jc.print_string.length() == 0) {
          printDDebug("XP OUT: test exponent call " + jc.generated_java);


          //          tmp_str = "\n" + indent_str +jc.generated_java + ";";
          tmp_str = "\n" + indent_str + node.right_str + ";";

          //          jc.print_string = "";
          //          jc.generated_java = "";
        } else {
          tmp_str = "\n" + indent_str + jc.print_string + ";";

          //          jc.print_string = "";
          //          jc.generated_java = "";
        }
      } else {
        if (jc.print_string.length() == 0) {
          if (node.right_str.equals("_JUST_CREATE_")) {
            tmp_str = "\n" + indent_str + left_side + ".create();";
          } else if (node.left_str.length() > 0) {
            //							System.out.println("WE'RA HERE, left_str: " +node.left_str);
            if (node.last_left) {
              // whatis this?
              tmp_str = "\n" + indent_str + left_side + node.left_str + node.right_str + ");";
            } else {
	        		if (jc.left_side_indexing > 0) {
  	          	tmp_str = "\n" + indent_str + left_side + node.left_str  + node.right_str + ");";
    	    		} else {
      	        tmp_str = "\n" + indent_str + left_side + node.left_str + ".set(_context, " + node.right_str + ");";
        			}	
            }
          } else {
        		if (jc.left_side_indexing > 0) {
            	tmp_str = "\n" + indent_str + left_side + node.right_str + ");";
        		} else {
            	tmp_str = "\n" + indent_str + left_side + ".set(_context, " + node.right_str + ");";
						}
            //							tmp_str = "\n" + indent_str + left_side +node.right_str + ");";
          }

          //          jc.print_string = "";
          //          jc.generated_java = "";
        } else {
          if (node.right_str.equals("_JUST_CREATE_")) {
            tmp_str = "\n" + indent_str + left_side + ".create();";
          } else {
        
        		if (jc.left_side_indexing > 0) {
            	tmp_str = "\n" + indent_str + left_side + node.right_str + ");";
        		} else {
            	tmp_str = "\n" + indent_str + left_side + ".set(_context, " + node.right_str + ");";
						}
            //tmp_str = "\n" + indent_str + left_side +node.right_str + ");";
          }

          //               tmp_str = "\n" + indent_str + left_side + ".set(_context, " + jc.print_string + ");";
          //          jc.print_string = "";
          //          jc.generated_java = "";
        }
      }

			// left_side_indexing


      ////            jc.pw.println("\n" + indent_str + left_side + " = " + jc.generated_java + ";");
      //            jc.pw.println(indent_str + left_side + " = " + jc.generated_java + ";");
      //             jc.print_string = "";
      //            // jc.print_tabs = "";
      // jc.pw.println("// HEHE: " + node.right_str);
      //      jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();


      //			jc.print_string += tmp_str;
      node.generated_java += tmp_str;

      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      if (!jc.print2string) {
//  when not inner, also the same thing, it seems, because otherwise the statements are generated before class - the class genertion was changed, you know. 
//        if (jc.inner) {
  //      if (true) {  // may need a check allowing only in functions and procedures, otherwise - the old way
				if (jc.in_function || jc.in_procedure) {
        	jc.statements_str += node.opening_java + "\n";
        	jc.statements_str += node.forwarded_java + "\n";
        	jc.statements_str += node.generated_java + "\n";
        } else {
	        jc.pw.println(node.opening_java);
        	jc.pw.println(node.forwarded_java);
        	jc.pw.println(node.generated_java);
        }
        node.opening_java = "";
        node.forwarded_java = "";
        node.generated_java = "";
      } else {
        node.generated_java = node.forwarded_java + node.generated_java;
        node.forwarded_java = "";
      }
			// added to fix the NOT issue
      jc.flag_non_temporary_value_instance = false;
    }

    jc.indent--;

    jc.left_side_indexing = 0;
    return data;
  }

  /*
  public Object visit(X_AssignmentStmt node, Object data)  throws SdaiException {
     JavaClass jc = (JavaClass)data;
     String indent_str;
     jc.indent++;
     Object general_reference;
     String left_side = "not_supported";
     if (jc.active) {
        jc.generated_java = "";
        general_reference = node.general_reference;
        if (general_reference instanceof ECtVariable) {
           ECtVariable ec_var = (ECtVariable)general_reference;
           String var_name = ec_var.getName();
           left_side = var_name;
        } else
        if (general_reference instanceof EParameter) {
           EParameter ec_par = (EParameter)general_reference;
           String par_name = ec_par.getName(null);
           left_side = par_name;
        }
     }   
   data = node.childrenAccept(this, data);
     if (jc.active) {
        indent_str = indentString(jc.indent+1);
        if (jc.print2string) {
           // jc.print_tabs = "\t\t\t";
           jc.print_string = "\n" + indent_str + left_side + " = " + jc.generated_java + ";";
        } else {
  //            jc.pw.println("\n" + indent_str + left_side + " = " + jc.generated_java + ";");
              jc.pw.println(indent_str + left_side + " = " + jc.generated_java + ";");
              jc.print_string = "";
              // jc.print_tabs = "";
           }
        }
        jc.indent--;
      return data;
    }
   */

  public Object visit(X_LocalVariable node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;


		jc.in_local_variable = true; // -boom-


    data = node.childrenAccept(this, data);

// moving everything here from above data


    String parameter_type_str = "/* unknown local variable type */ EEntity";
    String static_parameter_type_str = "";
    String id = "";
		//    EEntity parameter_type = null;

    if (jc.active) {

// why? - generates emty variable initializations when literal,
// but perhaps something else will be wrong? Or Antanas did it?
//      jc.generated_java = "";


      // parameter_type_str = node.getType(jc);
      
     	static_parameter_type_str = node.getStaticTypeString(jc);
			
      for (int i = 0; i < (node.variable_ids.size() - 1); i++) {
        id = (String) node.variable_ids.elementAt(i);


				if (jc.in_function || jc.in_procedure) {


        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + ";");
//        jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");
//System.out.println("<>LOCAL_VARIABLE<> 01");
        	jc.local_declarations_str += "\tValue " + id + ";\n";
        	jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ");\n";
      	} else {
        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + ";");
//        jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");
//System.out.println("<>LOCAL_VARIABLE<> 01");


//        jc.pw.println("\t\tValue " + id + ";");
        jc.declare_rule += "\t\tValue " + id + ";\n";


//        	jc.local_declarations_str += "\tValue " + id + ";\n";

        	jc.init_rule.addElement(id + " = Value.alloc(" + static_parameter_type_str + ");\n");
//        	jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ");\n";
      	}
      }

      id = (String) node.variable_ids.elementAt(node.variable_ids.size() - 1);
    }

    if (jc.active) {
      printActive("XP Active: LocalVariable");
    }

    if (!jc.print2string) {
      jc.print2string = true;
      node.print2string_activated = true;
    }

    jc.flag_alloc_type = true;
    jc.alloc_type_depth = jc.indent;
    jc.alloc_type = node.getStaticTypeString(jc);

		// a fiew lines to handle type recognition in AggregateInitializer if base_type is set
		jc.in_local_variable = true;
		if(node.parameter_type != null) {
			jc.pd = node.parameter_type;
			printDDebug("pd type, in local: " +jc.pd.getParameter_type(null));
		}


    jc.flag_alloc_type = false;

		if(jc.pd != null) jc.pd = null;
		jc.in_local_variable = false;

    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

/* */

/*

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }
*/

/* */

    if (!jc.print2string) {
			if (jc.in_function || jc.in_procedure) {

//        jc.pw.println(node.opening_java);
//        jc.pw.println(node.forwarded_java);
//        jc.pw.println(node.generated_java);
				jc.local_initializations_str += node.opening_java;
				jc.local_initializations_str += node.forwarded_java;
				jc.local_initializations_str += node.generated_java;
      } else {
//      if (jc.entity instanceof EGlobal_rule) {
        jc.init_rule.addElement(node.opening_java);
        jc.init_rule.addElement(node.forwarded_java);
        jc.init_rule.addElement(node.generated_java);
      }
 
      node.opening_java = "";
      node.forwarded_java = "";
      node.generated_java = "";

      //jc.print_before_first_string = "";
      //jc.print_first_string = "";
      //jc.print_string = "";
    }

    if (jc.active) {
      if (node.expression_present) {
        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + jc.generated_java + ";");
        if (node.java_contains_statements) {
          //            jc.pw.println("\t\t" + node.variable_declarations.elementAt(0));
          for (int i = 0; i < node.variable_declarations.size(); i++) {
						if (jc.in_function || jc.in_procedure) {
//              jc.pw.println("\t\t" + node.variable_declarations.elementAt(i));
//System.out.println("<>LOCAL_VARIABLE<> 02");
              jc.local_initializations_str += "\t\t" + node.variable_declarations.elementAt(i) + "\n";
            } else {
//            if (jc.entity instanceof EGlobal_rule) {
              jc.init_rule.addElement(node.variable_declarations.elementAt(i));
            }
          }

          for (int i = 0; i < node.statements.size(); i++) {
						if (jc.in_function || jc.in_procedure) {
//              jc.pw.println("\t\t" + node.statements.elementAt(i));
//System.out.println("<>LOCAL_VARIABLE<> 03");
              jc.local_initializations_str += "\t\t" + node.statements.elementAt(i) + "\n";
            } else {
//            if (jc.entity instanceof EGlobal_rule) {
              jc.init_rule.addElement(node.statements.elementAt(i));
            }
          }

          node.variable_declarations = null;
          node.statements = null;
          node.java_contains_statements = false;
        }

        // may not be needed here, alternatively, the return value of initializing method should be assigned to the local variable.
        // jc.pw.println("\t\t" + node.initializing_code.elementAt(0));
        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + node.variable_declaration + ";");
        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + node.variable_names.elementAt(0) + ";");
        if (node.initializing_code.size() > 0) {
// System.out.println("ECx-Local-03, jc.generated_java: " + jc.generated_java);
          //                  jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + node.initializing_code.elementAt(0) + ";");
					if (jc.in_function || jc.in_procedure) {
            // OPTIMIZATION: need to optimize generated code for function calls
//            jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ").set(_context, " + jc.generated_java + ");");
            jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 05");
            jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ").set(_context, " + jc.generated_java + ");\n";
//                          ").set(_context, " + node.initializing_code.elementAt(0) + ");");
          } else {
//          if (jc.entity instanceof EGlobal_rule) {
//            jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str +  ");");

//            jc.pw.println("\t\tValue " + id + ";");
		        jc.declare_rule += "\t\tValue " + id + ";\n";

//            jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 04");


              jc.init_rule.addElement(id + " = Value.alloc(" + static_parameter_type_str +  ");");
//            jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ");\n";
//            jc.init_rule.addElement(id + ".set(_context, " + node.initializing_code.elementAt(0) + ");");
              jc.init_rule.addElement(id + ".set(_context, " + jc.generated_java + ");");
          }
        } else {
          //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = null; /* initializing code - 0 elements */");
          //           System.out.println("XP: LocalVariable - expression present, contains java, but initializing code has 0 elements: " + parameter_type_str + " " + id + ", type: " + parameter_type);
          if (jc.generated_java.equals("_JUST_CREATE_")) {
// System.out.println("ECx-Local-02, jc.generated_java: " + jc.generated_java);
						if (jc.in_function || jc.in_procedure) {
//              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ").create();");
              jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 07");
              jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ").create();\n";
            } else {
//            if (jc.entity instanceof EGlobal_rule) {
//              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");

//              jc.pw.println("\t\tValue " + id + ";");
			        jc.declare_rule += "\t\tValue " + id + ";\n";


//              jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 06");
              jc.init_rule.addElement(id + " = Value.alloc(" + static_parameter_type_str + ");\n");
//              jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ");\n";
              jc.init_rule.addElement(id + ".create();");
            }
          } else {

// System.out.println("ECx-Local-01, jc.generated_java: " + jc.generated_java);
						if (jc.in_function || jc.in_procedure) {
//              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ").set(_context, " + jc.generated_java + ");");
              jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 09");
              jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ").set(_context, " + jc.generated_java + ");\n";
            } else {
//            if (jc.entity instanceof EGlobal_rule) {
//              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");

//              jc.pw.println("\t\tValue " + id + ";");
			        jc.declare_rule += "\t\tValue " + id + ";\n";


//              jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 08");
              jc.init_rule.addElement(id + " = Value.alloc(" + static_parameter_type_str + ");\n");
//              jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ");\n";
              jc.init_rule.addElement(id + ".set(_context, " + jc.generated_java + ");");
            }
          }

          ;
        }
      } else { // contains java statements
				if (jc.in_function || jc.in_procedure) {

//	        jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");
    	    jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 10");
	        jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ");\n";
				} else {

//	        jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");

//	        jc.pw.println("\t\tValue " + id + ";");
	        jc.declare_rule += "\t\tValue " + id + ";\n";


//    	    jc.local_declarations_str += "\tValue " + id + ";\n";
//System.out.println("<>LOCAL_VARIABLE<> 10");
	        jc.init_rule.addElement(id + " = Value.alloc(" + static_parameter_type_str + ");\n");
//	        jc.local_initializations_str += "\t\t" + id + " = Value.alloc(" + static_parameter_type_str + ");\n";
				}
      }
    }

    jc.indent--;

//System.out.println("<>LOCAL_VARIABLE<>: " + jc.generated_java);
//System.out.println("<>LOCAL_VARIABLE<>INIT: " + jc.local_initializations_str);

    return data;
  }


  // the current implementation does not support nested functions, etc.
  public Object visit_old(X_LocalVariable node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String parameter_type_str = "/* unknown local variable type */ EEntity";
    String static_parameter_type_str = "";
    String id = "";
		//    EEntity parameter_type = null;

    if (jc.active) {
      jc.generated_java = "";


      // parameter_type_str = node.getType(jc);
      
     	static_parameter_type_str = node.getStaticTypeString(jc);
			
      for (int i = 0; i < (node.variable_ids.size() - 1); i++) {
        id = (String) node.variable_ids.elementAt(i);


        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + ";");
        jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");
      }

      id = (String) node.variable_ids.elementAt(node.variable_ids.size() - 1);
    }

    if (jc.active) {
      printActive("XP Active: LocalVariable");
    }

    if (!jc.print2string) {
      jc.print2string = true;
      node.print2string_activated = true;
    }

    jc.flag_alloc_type = true;
    jc.alloc_type_depth = jc.indent;
    jc.alloc_type = node.getStaticTypeString(jc);

		// a fiew lines to handle type recognition in AggregateInitializer if base_type is set
		jc.in_local_variable = true;
		if(node.parameter_type != null) {
			jc.pd = node.parameter_type;
			printDDebug("pd type, in local: " +jc.pd.getParameter_type(null));
		}

    data = node.childrenAccept(this, data);

    jc.flag_alloc_type = false;

		if(jc.pd != null) jc.pd = null;
		jc.in_local_variable = false;

    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

/* */

/*

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }
*/

/* */

    if (!jc.print2string) {
      if (jc.entity instanceof EGlobal_rule) {
        jc.init_rule.addElement(node.opening_java);
        jc.init_rule.addElement(node.forwarded_java);
        jc.init_rule.addElement(node.generated_java);
      } else {
        jc.pw.println(node.opening_java);
        jc.pw.println(node.forwarded_java);
        jc.pw.println(node.generated_java);
      }

      node.opening_java = "";
      node.forwarded_java = "";
      node.generated_java = "";

      //jc.print_before_first_string = "";
      //jc.print_first_string = "";
      //jc.print_string = "";
    }

    if (jc.active) {
      if (node.expression_present) {
        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + jc.generated_java + ";");
        if (node.java_contains_statements) {
          //            jc.pw.println("\t\t" + node.variable_declarations.elementAt(0));
          for (int i = 0; i < node.variable_declarations.size(); i++) {
            if (jc.entity instanceof EGlobal_rule) {
              jc.init_rule.addElement(node.variable_declarations.elementAt(i));
            } else {
              jc.pw.println("\t\t" + node.variable_declarations.elementAt(i));
            }
          }

          for (int i = 0; i < node.statements.size(); i++) {
            if (jc.entity instanceof EGlobal_rule) {
              jc.init_rule.addElement(node.statements.elementAt(i));
            } else {
              jc.pw.println("\t\t" + node.statements.elementAt(i));
            }
          }

          node.variable_declarations = null;
          node.statements = null;
          node.java_contains_statements = false;
        }

        // may not be needed here, alternatively, the return value of initializing method should be assigned to the local variable.
        // jc.pw.println("\t\t" + node.initializing_code.elementAt(0));
        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + node.variable_declaration + ";");
        //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + node.variable_names.elementAt(0) + ";");
        if (node.initializing_code.size() > 0) {
// System.out.println("ECx-Local-03, jc.generated_java: " + jc.generated_java);
          //                  jc.pw.println("\t\t" + parameter_type_str + " " + id + " = " + node.initializing_code.elementAt(0) + ";");
          if (jc.entity instanceof EGlobal_rule) {
            jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + 
                          ");");
//            jc.init_rule.addElement(id + ".set(_context, " + node.initializing_code.elementAt(0) + ");");
              jc.init_rule.addElement(id + ".set(_context, " + jc.generated_java + ");");
          } else {
            // OPTIMIZATION: need to optimize generated code for function calls
            jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + 
//                          ").set(_context, " + node.initializing_code.elementAt(0) + ");");
                            ").set(_context, " + jc.generated_java + ");");
          }
        } else {
          //            jc.pw.println("\t\t" + parameter_type_str + " " + id + " = null; /* initializing code - 0 elements */");
          //           System.out.println("XP: LocalVariable - expression present, contains java, but initializing code has 0 elements: " + parameter_type_str + " " + id + ", type: " + parameter_type);
          if (jc.generated_java.equals("_JUST_CREATE_")) {
// System.out.println("ECx-Local-02, jc.generated_java: " + jc.generated_java);
            if (jc.entity instanceof EGlobal_rule) {
              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + 
                            ");");
              jc.init_rule.addElement(id + ".create();");
            } else {
              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + 
                            ").create();");
            }
          } else {

// System.out.println("ECx-Local-01, jc.generated_java: " + jc.generated_java);
            if (jc.entity instanceof EGlobal_rule) {
              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + 
                            ");");
              jc.init_rule.addElement(id + ".set(_context, " + jc.generated_java + ");");
            } else {
              jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + 
                            ").set(_context, " + jc.generated_java + ");");
            }
          }

          ;
        }
      } else { // contains java statements
        jc.pw.println("\t\tValue " + id + " = Value.alloc(" + static_parameter_type_str + ");");
      }
    }

    jc.indent--;

    return data;
  }

  public Object visit(X_NullStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";

    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;
      }
      jc.print_tabs = indentString(jc.indent);
    }

    printActive("XP Active: NullStmt");
    data = node.childrenAccept(this, data);

    if (jc.active) {
      indent_str = indentString(jc.indent + 1);
      tmp_str += ("\n" + indentString(jc.indent + 1) + ";");


      //      jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      node.generated_java += tmp_str;

      //     jc.print_string += tmp_str;
      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      if (!jc.print2string) {
	//    	if (true) {  // if in function or procedure, not necessary inner, if not - print the old way (if other cases even possible?)
				if (jc.in_function || jc.in_procedure) {
	     		jc.statements_str += node.opening_java + "\n";
	     		jc.statements_str += node.forwarded_java + "\n";
  	   		jc.statements_str += node.generated_java + "\n";
  			} else {
	        jc.pw.println(node.opening_java);
  	      jc.pw.println(node.forwarded_java);
    	    jc.pw.println(node.generated_java);
      	}
        jc.print_before_first_string = "";
        jc.print_first_string = "";
        jc.print_string = "";
      } else {
        node.generated_java = node.forwarded_java + node.generated_java;
        node.forwarded_java = "";
      }


	}

    jc.indent--;

    return data;
  }


  public Object visit(X_ProcedureCallStmt node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";
    String tmp_argument;
    boolean first_time;

    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;
      }

      jc.print_tabs = indentString(jc.indent);
    }

    if (jc.active) {
      printActive("XP Active: ProcedureCall");
    }

    jc.prd = node.pd;
    data = node.childrenAccept(this, data);
    jc.prd = null;

    if (jc.active) {
      indent_str = indentString(jc.indent - 1);

      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
          printDDebug("XPOORR AAA 1 in procedure_call: " + 
                             node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
          printDDebug("XPOORR AAA 2 in procedure_call: " + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      printDDebug("XPOORR AAA 3 in procedure_call: " + node.forwarded_java);

      if (!jc.print2string) {
//         if (jc.inner) {
  //       if (true) { // if in function or procedure, otherwise - the old way
				if (jc.in_function || jc.in_procedure) {
        	
        	jc.statements_str += jc.generated_java + "\n";
				 } else {
        	jc.pw.println(jc.generated_java); // change me: zolia
				}

        //       	jc.pw.println(node.forwarded_java);
        //       	jc.pw.println(node.generated_java);
        node.forwarded_java = "";
        node.generated_java = "";
      }
    }

    jc.indent--;

    return data;
  }

  public Object visit(X_ProcedureDecl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;
    String signature = "";
//    String type;
    String name;
    String parameters;

    if (jc.entity == null) {
//      return data;
    }

    if (jc.ed != null) {
//      return data;
    }

    if (jc.entity != node.pd) {
//      return data;
    }

    jc.indent++;


    if (jc.entity == node.pd) {
    	jc.inner = false;
      jc.active = true;
      jc.in_procedure = true;
			jc.keynode = node;
// System.out.println("#@# starting outer procedure, setting keynode to: " + node.pd.getName(null)); 
      // jc.return_type = node.fd.getReturn_type(null);

      String original_name = node.pd.getName(null);
      name = "p" + original_name.substring(0, 1).toUpperCase() + 
             original_name.substring(1).toLowerCase();
//      type = node.getType(jc);
      parameters = node.getParameters();


      //         signature = "\tpublic " + type + " " + name + "(" + parameters + ") throws SdaiException {";
      //old - not separate files         signature = "\tpublic static Value " + name + "(" + parameters + ") throws SdaiException {";
      global_procedure_name = node.pd.getName(null);
      // global_return_number = 1;
      if (flag_split_debug) {
//      	signature = "\tpublic static Value run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting, parameters: \"" + parameters + ");";
      	signature = "\tpublic void run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_procedure_name + " starting\");";
    	} else {
      	signature = "\tpublic void run(" + parameters + ") throws SdaiException {";
      }
//      jc.pw.println(signature);
//      node.makeNonVar(jc);
		} else
//    if (Support.getParentFunctionDefinition(node.fd) != null) {
    if (Support.getParentFunctionProcedureRuleDefinition(node.pd) != null) {

    	//  inner function, may also search for inner_declaration, if needed
      jc.inner = true;


      jc.active = true;
      jc.in_procedure = true;
			jc.keynode = node;
// System.out.println("#@# starting inner procedure, setting keynode to: " + node.pd.getName(null)); 

//      jc.return_type = node.fd.getReturn_type(null);


      String original_name = node.pd.getName(null);
      name = "p" + original_name.substring(0, 1).toUpperCase() + 
             original_name.substring(1).toLowerCase();
//      type = node.getType(jc);
      parameters = node.getParameters();

      //         signature = "\tpublic " + type + " " + name + "(" + parameters + ") throws SdaiException {";
      //old - not separate files         signature = "\tpublic static Value " + name + "(" + parameters + ") throws SdaiException {";
      global_procedure_name = node.pd.getName(null);
      // global_return_number = 1;
      if (flag_split_debug) {
//      	signature = "\tpublic static Value run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_function_name + " starting, parameters: \"" + parameters + ");";
      	signature = "\tpublic void run(" + parameters + ") throws SdaiException {\n\t\tSystem.out.println(\"" + global_procedure_name + " starting\");";
    	} else {
      	signature = "\tpublic void run(" + parameters + ") throws SdaiException {";
      }
		}

    if (jc.active) {
      if (node.pd != null) {
        printActive("XP Active: ProcedureDecl: " + node.pd.getName(null) + ", ID: " + 
                           node.id);

        for (int jak = 0; jak < node.children.length; jak++) {
          //          System.out.println("Child: " + (( SimpleNode )node.children[jak]).id);
        }
      } else {
        printActive("XP Active: ProcedureDecl is NULL");
      }
    }

//    printActive("XP Active: ProcedureDecl");
    data = node.childrenAccept(this, data);

//    if (jc.entity == node.pd) {
//      jc.active = false;
    if (jc.active) {

      String p_name = null;
      String public_str = "";

      String class_name;
      String parent_class_name = null;

      EEntity parent_f = Support.getParentFunctionProcedureRuleDefinition(node.pd);
			if (parent_f == null) {
				public_str = "public ";
			}
	
      if (node.pd != null) {
      	p_name = node.pd.getName(null);
    	} else {
    		p_name = "procedure_definition_null";
    	}
      if (parent_f == null) {
      	class_name = "P" + p_name.substring(0, 1).toUpperCase() + p_name.substring(1).toLowerCase();
	    } else {
	    	// construct the name of the inner function class
	    	class_name = node.constructProcedureClassName();
	  		parent_class_name = node.constructFunctionProcedureRuleClassName(parent_f);
	    }
	    jc.pw.println("");
      jc.pw.println(public_str + "class " + class_name + " {\n	");

			if (parent_f != null) {
				jc.pw.println("\t" + parent_class_name + " parent;\n");
			}

			if (!jc.constant_static_field_str.equals("")) {
				jc.pw.println(jc.constant_static_field_str);
				jc.constant_static_field_str = "";
			}

      node.makeNonVar(jc);
			jc.pw.println(jc.parameter_declarations_str);
	    jc.parameter_declarations_str = "";		
			jc.pw.println(jc.local_declarations_str);
			jc.local_declarations_str = "";

			if (parent_f != null) {
				
				jc.pw.println("\t" + class_name + "(" + parent_class_name + " parent) {");
	      jc.pw.println("\t\tthis.parent = parent;");
  	    jc.pw.println("\t}\n");

			}

			if (!jc.constant_method_str.equals("")) {
				jc.pw.println(jc.constant_method_str);
				jc.constant_method_str = "";
			}

      jc.pw.println(signature);

			// generate accumulated stuff here, perhaps
	
			jc.pw.println(jc.parameter_initializations_str);
	    jc.parameter_initializations_str = "";		
			jc.pw.println(jc.local_initializations_str);
			jc.local_initializations_str = "";
      jc.pw.println(jc.statements_str);
      jc.statements_str = "";
      
      node.makeVar(jc);
      jc.pw.println(jc.var_parameter_post_initializations_str);
      jc.var_parameter_post_initializations_str = "";
      jc.pw.println("");
      jc.pw.println("\t}");
      jc.pw.println("}");

			if (parent_f == null) { // the outer procedure, has no  parent
      	jc.inner = false;
      	jc.active = false;
		    jc.in_procedure = false;
		    jc.keynode = null;
// System.out.println("#@# ending inner procedure, setting keynode to NULL: " + node.pd.getName(null)); 
			} else {
				if (parent_f instanceof EFunction_definition) {
					jc.in_function = true;
					jc.in_procedure = false;
				} else 
				if (parent_f instanceof EProcedure_definition) {
					jc.in_function = false;
					jc.in_procedure = true;
				} else 
				if (parent_f instanceof EGlobal_rule) {
					jc.in_function = false;
					jc.in_procedure = false;
				}
				jc.keynode = node;
// System.out.println("#@# ending inner procedure, setting keynode to (itself - cannot be right?): " + node.pd.getName(null) + ", parent: " + parent_f); 
			}

      jc.generated_java = "";
    }

    jc.indent--;
    jc.variable_ids = null;

    return data;
  }

  public Object visit(X_ProcedureHead node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: ProcedureHead");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_RepeatStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";

    // boolean print2string_activated = false;
    if (!jc.print2string) {
      jc.print2string = true;
      node.print2string_activated = true;

      // jc.generated_java = "";
      // jc.print_string = "";
    }


    // jc.generated_java = "";
    jc.print_tabs = indentString(jc.indent);

    printActive("XP Active: RepeatStmt");

    data = node.childrenAccept(this, data);


    // jc.pw.println("\t\t// for (" + node.increment_str + ") {" );
    indent_str = indentString(jc.indent + 1);


    // tmp_str = indentString(jc.indent+1);
    tmp_str += ("\n" + indentString(jc.indent + 1) + node.end_str);
    tmp_str += ("\n" + indentString(jc.indent + 1) + node.step_str);
    tmp_str += ("\n" + indentString(jc.indent + 1) + "for (" + node.increment_str + ") {");

    // tmp_str += indentString(jc.indent+1) + "for (" + node.increment_str + ") {";
    if (node.while_str.length() > 0) {
      //            jc.pw.println("\t\t\t" + node.while_str );
      tmp_str += ("\n\t" + indent_str + node.while_str);
    }


    //         jc.pw.println(node.stmt_str );
    // tmp_str += "\n" + indent_str + node.stmt_str;
    tmp_str += (indent_str + node.stmt_str);
    node.stmt_str = "";

    if (node.until_str.length() > 0) {
      //            jc.pw.println("\t\t\t" + node.until_str );
      tmp_str += ("\n\t" + indent_str + node.until_str);
    }


    //         jc.pw.println("\t\t}" );
    tmp_str += ("\n" + indentString(jc.indent + 1) + "} // for - REPEAT");


    //         jc.pw.println("// tmp: " + tmp_str);
    //      jc.print_string = tmp_str;
    printDDebug("XPOORR 1: " + node.forwarded_java);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    printDDebug("XPOORR 2: " + node.forwarded_java);

    node.getGeneratedJavaFromChildren();

    node.generated_java += tmp_str;

    //      jc.print_string += tmp_str;
    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    if (node.java_contains_statements) {
      for (int i = 0; i < node.variable_declarations.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        printDDebug("XPOORR A3: " + node.variable_declarations.elementAt(i));
      }

      for (int i = 0; i < node.statements.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        printDDebug("XPOORR A4: " + node.statements.elementAt(i));
      }

      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
    }

    printDDebug("XPOORR 3: " + node.forwarded_java);

    if (!jc.print2string) {

//    	if (true) {  // if in function or procedure, not necessary inner, if not - print the old way (if other cases even possible?)
			if (jc.in_function || jc.in_procedure) {
     		jc.statements_str += node.forwarded_java + "\n";
     		jc.statements_str += node.generated_java + "\n";

			} else {
  	    jc.pw.println(node.forwarded_java);
    	  jc.pw.println(node.generated_java);
			}
      node.forwarded_java = "";
      node.generated_java = "";
    } else {
      node.generated_java = node.forwarded_java + node.generated_java;
      node.forwarded_java = "";
    }





    // jc.saved_str = "";
    //      }
    // jc.print_string = "";
    //jc.print_tabs = "";
    jc.indent--;

    return data;
  }

  public Object visit(X_ReturnStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";

    // boolean print2string_activated = false;
    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;

        // jc.generated_java = "";
        // jc.print_string = "";
      }


      // jc.generated_java = "";
      jc.print_tabs = indentString(jc.indent);
    }

    //      if (jc.active) {
    //         jc.generated_java = "";
    //         jc.pw.println("\t\treturn ");
    //      }
    if (jc.active) {
      printActive("XP Active: ReturnStmt");
    }

//    printActive("XP Active: ReturnStmt type before accept: " + jc.return_type);

    jc.in_return = true;
    data = node.childrenAccept(this, data);
    jc.in_return = false;

    if (jc.active) {
      //indent_str = indentString(jc.indent + 1);
      indent_str = "\t\t";

			if (jc.generated_java.equals("Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create()")) {
				jc.generated_java = "Value.alloc(" + node.getStaticTypeString(jc) + ").create()";
			}
// System.out.println("XRTX-02" + node.getStaticTypeString(jc));

      // can we get type more easily?
      if (jc.print_string.length() == 0) {
//        printActive("XP Active: ReturnStmt type after accept: " + jc.return_type);


        // not sure if this type "check" makes sence
        if (flag_split_debug) {
        	tmp_str = "\n" + indent_str + "if (true) {\n\t" + indent_str + "Value rv = Value.alloc(" + node.getStaticTypeString(jc) + 
                  ").set(_context, " + jc.generated_java + ");\n\t" + indent_str + "System.out.println(\""+ global_function_name + " " + global_return_number++ + " return: \" + rv);\n\t" + indent_str + "return rv.check(_context, " + node.getStaticTypeString(jc) + 
                  ");\n" + indent_str + "};";
        } else {
        	tmp_str = "\n" + indent_str + "if (true) return " + "Value.alloc(" + node.getStaticTypeString(jc) + 
                  ").set(_context, " + jc.generated_java + ").check(_context, " + node.getStaticTypeString(jc) + 
                  ");";
				}
        // tmp_str = "\n" + indent_str + "return Value.alloc()." + jc.generated_java + ";";
        //        jc.print_string = "";
        //        jc.generated_java = "";
      } else {
        //            tmp_str = "\n" + indent_str + "return " + jc.print_string + ";";
        if (flag_split_debug) {
        	tmp_str = "\n" + indent_str + "if (true) {\n\t" + indent_str + "Value rv = Value.alloc(" + node.getStaticTypeString(jc) + 
                  ").set(_context, " + jc.generated_java + ");\n\t" + indent_str + "System.out.println(\""+ global_function_name + " " + global_return_number++ + " return: \" + rv);\n\t" + indent_str + "return rv.check(_context, " + node.getStaticTypeString(jc) + 
                  ");\n" + indent_str + "};";
        } else {
        	tmp_str = "\n" + indent_str + "if(true) return " + "Value.alloc(" + node.getStaticTypeString(jc) + 
                  ").set(_context, " + jc.generated_java + ").check(_context, " + node.getStaticTypeString(jc) + 
                  ");";

				}
        //   tmp_str = "\n" + indent_str + "return Value.alloc()." + jc.generated_java + ";";
        //        jc.print_string = "";
        //        jc.generated_java = "";
      }


      //      if (jc.active) {
      //
      //         jc.pw.println("\t\t\t" + jc.generated_java + ";");
      //      }
      //      jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

  		// support for RETURN in procedures 
      if (node.jjtGetNumChildren() == 0) {
      	node.generated_java += "\n" + indent_str + "if(true) { ";
      	node.generated_java +=  "\n" + indent_str + "\t" + ((X_ProcedureDecl)jc.keynode).makeVarStr(jc, indent_str);
      	node.generated_java +=  " \n"  + indent_str + "\t return;";
      	node.generated_java +=  " \n"  + indent_str + "}";
      } else {
	      node.generated_java += tmp_str;
      }

      jc.generated_java = "";

      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      if (!jc.print2string) {
//        if (jc.inner) {
  //      if (true) { // check if in function, procedure (because it return statement, it is save to asume - in function, though)
				if (jc.in_function || jc.in_procedure) {
	        jc.return_str += node.opening_java + "\n";
  	      jc.return_str += node.forwarded_java + "\n";
    	    jc.return_str += node.generated_java + "\n";
      	} else {
	        jc.pw.println(node.opening_java);
  	      jc.pw.println(node.forwarded_java);
    	    jc.pw.println(node.generated_java);
      	}
        node.opening_java = "";
        node.forwarded_java = "";
        node.generated_java = "";
      }
    }

    jc.indent--;

    return data;
  }

  public Object visit(X_SkipStmt node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";

    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;
      }
      jc.print_tabs = indentString(jc.indent);
    }

    printActive("XP Active: SkipStmt");
    data = node.childrenAccept(this, data);

    if (jc.active) {
      indent_str = indentString(jc.indent + 1);
      tmp_str += ("\n" + indentString(jc.indent + 1) + "continue;");


      //      jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      node.generated_java += tmp_str;

      //     jc.print_string += tmp_str;
      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      if (!jc.print2string) {
	//    	if (true) {  // if in function or procedure, not necessary inner, if not - print the old way (if other cases even possible?)
				if (jc.in_function || jc.in_procedure) {
	     		jc.statements_str += node.opening_java + "\n";
	     		jc.statements_str += node.forwarded_java + "\n";
  	   		jc.statements_str += node.generated_java + "\n";
  			} else {
	        jc.pw.println(node.opening_java);
  	      jc.pw.println(node.forwarded_java);
    	    jc.pw.println(node.generated_java);
      	}
        jc.print_before_first_string = "";
        jc.print_first_string = "";
        jc.print_string = "";
      } else {
        node.generated_java = node.forwarded_java + node.generated_java;
        node.forwarded_java = "";
      }


	}


    jc.indent--;

    return data;
  }

  public Object visit(X_Population node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: Population");

    String model_name = node.ed.findEntityInstanceSdaiModel().getName();
    String schema_package = constructTypeSchemaPrefixFromModel(model_name);
    String entity_name = node.ed.getName(null).replace('+', '$');

    String the_name = schema_package + ".C" + entity_name.substring(0, 1).toUpperCase() + 
                      entity_name.substring(1).toLowerCase() + ".class";

    //  to check if alloc() without aggregate type is ok.
    // if not, how to put there a specific aggregate -unclear. a general aggregate possible, no problem.	

//     String static_type_attribute_name = "_st_population_" + node.ed.getName(null).toLowerCase();

//     jc.generated_java = "Value.alloc(" + constructSchemaClassNamePrefix(jc) + 
//                         static_type_attribute_name + ").set(_context.domain.getInstances(" + 
//                         the_name + "))";


//    jc.generated_java = "Value.alloc(ExpressTypes.SET_GENERIC_TYPE).set(_context, _context.domain.getInstances(" + the_name + "))";
    jc.generated_java = "Value.alloc(ExpressTypes.SET_GENERIC_TYPE).setInstancesAggregate(_context, _context.domain.getInstances(" + the_name + "))";

    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_IncrementControl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;


    //      if (jc.active) {
    //         jc.generated_java = "";
    //      }
    printActive("XP Active: IncrementalControl");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();


    //      if (jc.active) {
    ////         jc.pw.println("\t\tfor (" + jc.bound1_str + ";" + jc.bound2_str + ";" + jc.increment_str + ") {a + ";");
    //      }
    jc.indent--;

    return data;
  }

  public Object visit(X_WhileControl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;


    //      if (jc.active) {
    //         jc.generated_java = "";
    //      }
    printActive("XP Active: WhileControl");
    data = node.childrenAccept(this, data);


    //      if (jc.active) {
    //         jc.pw.println("\t\t" + jc.generated_java + ";");
    //      }
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_UntilControl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: UntilControl");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_MapCall node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: MapCall");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

/*
      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

*/

    jc.indent--;

    return data;
  }

  public Object visit(X_FunctionCall node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    String indent_str;
    String tmp_str = "";
    String tmp_argument;
    boolean first_time;

    // boolean print2string_activated = false;
    if (jc.active) {
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;

        // jc.generated_java = "";
        // jc.print_string = "";
      }


      // jc.generated_java = "";
      jc.print_tabs = indentString(jc.indent);
    }

    if (jc.active) {
      printActive("XP Active: FunctionCall");
    }

    jc.fd = node.fd;
    printDDebug("activated before children: " + node.print2string_activated);
    data = node.childrenAccept(this, data);
    jc.fd = null;

    if (jc.active) {
      //      indent_str = indentString(jc.indent);
      indent_str = indentString(jc.indent - 1);


      // function call, consists of function name and actual parameters - arguments
      // not parameters from the dictionary!!!
      // new line not needed

      /*
      tmp_str += node.getName() + "(";   
      first_time = true;   
      for (int i = 0; i < node.actual_count; i++) {
         tmp_argument = (String)node.actual_parameters.elementAt(i);
         if (first_time) {
            tmp_str += tmp_argument;
            first_time = false;
         } else {
            tmp_str += ", " + tmp_argument;
         }
      }
      tmp_str += ")";
       */

      //         jc.print_string = tmp_str;
      // jc.print_string = jc.generated_java; // - disrupts everything that was accumulated
      printDDebug("activated after children: " + node.print2string_activated);

      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      if (node.print2string_activated) {
        jc.print2string = false;
        node.print2string_activated = false;
      }

      if (node.java_contains_statements) {
        for (int i = 0; i < node.variable_declarations.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
          printDDebug("XPOORR AAA 1 in function_call: " + 
                             node.variable_declarations.elementAt(i));
        }

        for (int i = 0; i < node.statements.size(); i++) {
          node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
          printDDebug("XPOORR AAA 2 in function_call: " + node.statements.elementAt(i));
        }

        node.java_contains_statements = false;
        node.variable_declarations = null;
        node.statements = null;
      }

      printDDebug("XPOORR AAA 3 in function_call: " + node.forwarded_java);

      if (!jc.print2string) {
				if (jc.in_local_variable) {
					if (jc.in_function || jc.in_procedure) {
//		    		jc.local_initializations_str += node.forwarded_java + "\n";
// 		  		jc.local_initializations_str += node.generated_java + "\n";
  				} else {
  					// global rule
//		        jc.init_rule.addElement(node.forwarded_java);
//    		    jc.init_rule.addElement(node.generated_java);
  				}

//        node.forwarded_java = "";
//        node.generated_java = "";


  			} else { 
        	// why do we print here directly, and why it is still ok?
        	jc.pw.println(node.forwarded_java);
        	jc.pw.println(node.generated_java);

        node.forwarded_java = "";
        node.generated_java = "";

        }
      }

      // jc.saved_str = "";
      //?      }
      //jc.print_string = "";
      //jc.print_tabs = "";
    }

    jc.indent--;

    return data;
  }



  
  public Object visit(X_IfCondition node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: IfCondition");
    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;
    printActive("XP Active Ending: IfCondition");

    return data;
  }

  public Object visit(X_Parameter node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: Parameter");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_AggregateInitializer node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    String tmp_str;
    String tmp_str2;
    jc.indent++;

    if (jc.active) {
      // can an agggregate_initializer be inside another aggregate_initializer?
      // if so, it probably makes no difference
      //         jc.in_aggregate_initializer = true;
      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;

        // jc.generated_java = "";
        // jc.print_string = "";
      }


      // jc.generated_java = "";
      jc.print_tabs = indentString(jc.indent);
    }

    if (jc.active) {
      printActive("XP Active: AggregateInitializer: " + node.been_here + ", ID: " + 
                         node.id + ", parent: " + ((SimpleNode) node.parent).id);
    }

    data = node.childrenAccept(this, data);
    node.been_here = "Visited already";

    if (jc.active) {
      printDDebug("XP OUT: AggregateInitializer: " + node.been_here + ", ID: " + node.id + 
                         ", parent: " + ((SimpleNode) node.parent).id);
    }

    if (jc.active) {
      //         indent_str = indentString(jc.indent+1);
      tmp_str = "";

      //            tmp_str += "\nA_string elements = new A_string()";
      for (int i = 0; i < node.elements.size(); i++) {
        //               tmp_str += "\nelements.addByIndex(elements.getMemberCount()+1, " + (String)node.elements.elementAt(i) + ";";
        //               tmp_str += "\nelements.addUnordered(" + (String)node.elements.elementAt(i) + ";";
      }


      //            tmp_str += "\ninitializeAggregate(elements);";
      //jc.pw.println(tmp_str);
      //         tmp_str += node.getName() + "(";
      //         jc.print_string = tmp_str;
      node.getOpeningJavaFromChildren();
      node.getForwardedJavaFromChildren();
      node.getGeneratedJavaFromChildren();

      if (node.print2string_activated) {
        jc.print2string = false;
//        if (jc.in_local_variable) {
//        	jc.local_initializations_str += node.generated_java;
//        } else {
        	jc.pw.println(node.generated_java);
 //       }
        node.generated_java = "";
        node.print2string_activated = false;

        // jc.saved_str = "";
      }

      // jc.print_string = "";
      //jc.print_tabs = "";
    }


    //      jc.in_aggregate_initializer = false;
    jc.indent--;

    return data;
  }



  public Object visit(X_Element node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: Element");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_AggregateSource node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: AggregateSource");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_QueryLogicalExpression node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    printDDebug("X_QueryLogicalExpression before: " + jc.generated_java);

    jc.indent++;
    printActive("XP Active: QueryLogicalExpression");
		// inside query logical_expression attribute references will be done by name, because implicit variable is too generic
		// perhaps it is possible to recognize the implicit variable specificly, if so, the references by name could be limited to only to it.
		jc.query_logical_expression++;
    data = node.childrenAccept(this, data);
		jc.query_logical_expression--;
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
//    node.getForwardedStuffFromChildren();
	  node.getGeneratedJavaFromChildren();
    jc.indent--;

    printDDebug("X_QueryLogicalExpression after: " + 
                       (((JavaClass) data).generated_java += ".getLogical() == 2"));

    return data;
  }

  public Object visit(X_RuleDecl node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;
    String signature;
    String type;
    String name;
    String parameters;

    if (jc.entity == null) {
      return data;
    }

    if (jc.ed != null) {
      return data;
    }

    // Dublicate code? (in X_DomainRule?)
    if (jc.entity == node.global_rule) {
      jc.active = true;

      String original_name = node.global_rule.getName(null);
      name = "r" + original_name.substring(0, 1).toUpperCase() + 
             original_name.substring(1).toLowerCase();
			

      // type = node.getType();
      parameters = "SdaiContext _context";
      signature = "\tpublic int " + name + "(" + parameters + ") throws SdaiException {";

      //      jc.pw.println(signature);

//      String original_name = node.global_rule.getName(null);
			String class_name =  "R" + original_name.substring(0, 1).toUpperCase() + original_name.substring(1).toLowerCase();
			jc.declare_rule = "public class " + class_name + " {\n";
      jc.declare_rule += "\tboolean _already_initialized = false;\n";
//      jc.pw.println("public class " + class_name + " {");
//			jc.pw.println("\tboolean _already_initialized = false;");


    }

    if (jc.active) {
      jc.indent++;

      if (node.global_rule != null) {
        printActive("XP Active: RuleDecl: " + node.global_rule.getName(null));
      } else {
        printActive("XP Active: RuleDecl is NULL");
      }
    }

    jc.init_rule = new Vector();



    data = node.childrenAccept(this, data);



    if (jc.entity == node.global_rule) {
      jc.active = false;


      //      jc.pw.println("\t}");
      jc.generated_java = "";

		// added after discovering problems in following functions
      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
      node.opening_java = "";
      node.forwarded_java = "";
      node.generated_java = "";
			jc.init_rule = null;

    }

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;
    jc.variable_ids = null;
		jc.declare_rule = "";

    return data;
    

    
  }

  public Object visit(X_DomainRule node, Object data) throws SdaiException {
    // this prints the whole derived attribute directly from tokens as it is in original express (spaces and newlines not re-created)
    JavaClass jc = (JavaClass) data;
    String indent_str;
    String tmp_str = "";

    boolean from_global_rule = false;
    boolean do_not_skip = false;
		boolean where_rule_in_defined_type = false;
		boolean where_rule_in_entity = false;
		EDefined_type dt = null;
		EEntity_definition ed = null;

    if (node.global_rule) {
      X_RuleDecl grand_parent = (X_RuleDecl) node.jjtGetParent().jjtGetParent();

      if (jc.entity == grand_parent.global_rule) {
        from_global_rule = true;
        do_not_skip = true;
      }
    } else {
      if (jc.entity == node.where_rule) {
        do_not_skip = true;
      
      	EEntity dt_candidate = node.where_rule.getParent_item(null);
      	if (dt_candidate instanceof EDefined_type) {
      		dt = (EDefined_type)dt_candidate;
      		where_rule_in_defined_type = true;
      	} else 
      	if (dt_candidate instanceof EEntity_definition) {
      		ed = (EEntity_definition)dt_candidate;
      		where_rule_in_entity = true;
      	}
      }
    }

    if (node.map_definition) {
				do_not_skip = true;
		}
	
		if (jc.foreach) {
			do_not_skip = true;
		}
		
		// do_not_skip = true; // TMP to print debugging results - later - to make it true for FOR EACH expressions in Express-X

    if (do_not_skip) {
      jc.active = true; // if global rule, already should be active.

      if (!jc.print2string) {
        jc.print2string = true;
        node.print2string_activated = true;
      }

      //         jc.generated_java = "\t\t\t\t";
      //      if (!node.global_rule) {

//      if (!node.map_definition) {
      if (!(node.map_definition || jc.foreach)) {
        String name;
        String parameters;
        String signature;
        boolean label_present = node.where_rule.testLabel(null);
				

				String original_name;
        if (label_present) {
        	original_name = node.where_rule.getLabel(null);
    		}
    		else {
    			original_name = "WR1";
    		} 	
    			
    			if (where_rule_in_defined_type) {
						String dt_name = dt.getName(null);

          	name = "r" + dt_name.substring(0, 1).toUpperCase() + dt_name.substring(1).toLowerCase() + 
          					original_name.substring(0, 1).toUpperCase() + original_name.substring(1).toLowerCase();

    			} else
    			if (where_rule_in_entity) {
						String entity_name = ed.getName(null);

          	name = "r" + entity_name.substring(0, 1).toUpperCase() + entity_name.substring(1).toLowerCase() + 
          					original_name.substring(0, 1).toUpperCase() + original_name.substring(1).toLowerCase();



    			} else {
    
          	name = "r" + original_name.substring(0, 1).toUpperCase() + 
            	     original_name.substring(1).toLowerCase();
        	}



        // type = node.getType();
   			if (where_rule_in_defined_type) {
//	        String self_type = node.constructSelfType(dt, jc);
	        String self_type = "Value";
 	        parameters = "SdaiContext _context, " + self_type + " self";
				} else {
	        parameters = "SdaiContext _context";
				}

        if (from_global_rule) {
          signature = "\tpublic int " + name + "(" + parameters + 
                      ") throws SdaiException {\n\t\tinit(_context);";
        } else {
   				if (where_rule_in_defined_type) {
          	signature = "\tpublic static int " + name + "(" + parameters + ") throws SdaiException {\n\t";
					} else {
          	signature = "\tpublic int " + name + "(" + parameters + ") throws SdaiException {\n\t";
					}
        }

        jc.pw.println(signature);
      } // not map_definition
    } else {
      return data;
    }

    jc.indent++;

    indent_str = indentString(jc.indent + 1);

    jc.print_string = "";
    //FIXME?    jc.print_string = "";
    if (jc.active) {
      printActive("XP Active: DomainRule");
    }

    jc.domain_rule = true;

//		if(node.map_definition)
		if(node.map_definition || jc.foreach)
				tmp_str = "Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, ";
		else
				tmp_str = "\t\treturn (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, ";

    data = node.childrenAccept(this, data);

    jc.domain_rule = false;


    // 		if (jc.entity == node.where_rule || node.global_rule) {
    tmp_str += jc.generated_java;


    // 		}
//		if(node.map_definition)
		if(node.map_definition || jc.foreach)
			tmp_str += ").getAsBoolean()";
		else
			tmp_str += ").getLogical());\n\t}";
			//    tmp_str += ").getLogical());\n\t}";


    //    if ((jc.entity == node.where_rule || node.global_rule)) {
    if (!jc.foreach) {
    	jc.active = false;
		}

    //      jc.pw.println("\n\t\t);\n\t}");
    //      jc.print_string = tmp_str;
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    node.generated_java += tmp_str;

// System.out.println("<DOMAIN RULE> : " + tmp_str);

		if (jc.foreach) {
			jc.foreach_where.addElement(node.generated_java);
			node.generated_java = "";
		}

    jc.generated_java = "";

    if (node.print2string_activated) {
      jc.print2string = false;
      node.print2string_activated = false;
    }

    if (node.java_contains_statements) {
      for (int i = 0; i < node.variable_declarations.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
      }

      for (int i = 0; i < node.statements.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
      }

      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
    }

    if (!jc.print2string) {
      jc.pw.println(node.opening_java);
      jc.pw.println(node.forwarded_java);
      jc.pw.println(node.generated_java);
      node.opening_java = "";
      node.forwarded_java = "";
      node.generated_java = "";
    }

    jc.indent--;

    return data;
  }

  /* 
  public Object visit(X_Expression node, Object data)  throws SdaiException {
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  /*
  public Object visit(X_SimpleExpression node, Object data)  throws SdaiException {
    data = node.childrenAccept(this, data);
    return data;
  }
   */
  public Object visit(X_WhereClause node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;


    // if (!jc.active) {
    // return data;
    // }
    jc.indent++;

/*
		if (!(jc.in_function || jc.in_procedure)) {
			if (!jc.declare_rule.equals("")) {
				jc.pw.println(jc.declare_rule);
				jc.declare_rule = "";
			}
		}
*/

    // printActive("XP Active: QueryLogicalExpression");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_DomainRuleLogicalExpression node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: DomainRuleLogicalExpression");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_PowerOp node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_PowerOp before children");
    printActive("XP Active: PowerOp");
    data = node.childrenAccept(this, data);
    debugPrint(data, "X_PowerOp after children");

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_UnaryOp node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_UnaryOp before children");
    printActive("XP Active: UnaryOp");
    data = node.childrenAccept(this, data);
    debugPrint(data, "X_UnaryOp after children");

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_AttributeQualifier node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_AttributeQualifier before children");
    printActive("XP Active: AttributeQualifier");

    //FIXME?
    String original = jc.generated_java;
    data = node.childrenAccept(this, data);
// System.out.println("XYX-1: " + original + ", " + jc.generated_java);
    jc.generated_java = original + jc.generated_java;
//    System.out.println("XYX-1 after attribute qualifier: " + jc.generated_java);
    debugPrint(data, "X_AttributeQualifier after children");
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_GroupQualifier node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_GroupQualifier before children");
    printActive("XP Active: GroupQualifier");

    String original = jc.generated_java;
    String entity_name = node.ed.getName(null).replace('+', '$');
    String model_name = node.ed.findEntityInstanceSdaiModel().getName();

    data = node.childrenAccept(this, data);

		// changing the class in the case of explicit attribute redeclared as explicit
		// in that case the original (not redeclared attribute) has the internal field and its entity class has to be used
		// otherwise lang has problems with the method groupReference()
		
		
		
		Node my_parent = node.jjtGetParent();
		boolean next_last = false;
		for (int i = 0; i < my_parent.jjtGetNumChildren(); i++) {
			Node child = my_parent.jjtGetChild(i);
			if (next_last) {
				if (child instanceof X_AttributeQualifier) {
					Node attr_node = child.jjtGetChild(0);
					if (attr_node instanceof X_AttributeRef) {
						// get the original non-redeclaring attribute
						if (((X_AttributeRef)attr_node).attribute instanceof EExplicit_attribute) {
							EExplicit_attribute attr = (EExplicit_attribute)((X_AttributeRef)attr_node).attribute;
							
							while (attr.testRedeclaring(null)) {
								// redeclaring
								EExplicit_attribute attr2 = attr.getRedeclaring(null);
								attr = attr2;
							}
							if (attr != ((X_AttributeRef)attr_node).attribute) {
								// attribute changed, need to update schema and entity
								entity_name = ((EEntity_definition)attr.getParent(null)).getName(null).replace('+', '$');
								model_name = ((EEntity_definition)attr.getParent(null)).findEntityInstanceSdaiModel().getName();
							}
						}			
					} else {
						// something wrong
					}
					break;
				} else {
					// something wrong
				}
				break;
			} 
			if (child == node) {
				next_last = true;
			}
		}
		
   

    jc.generated_java = original + ".groupReference(_context, " + 
                        constructTypeSchemaPrefixFromModel(model_name) + ".C" + 
                        entity_name.substring(0, 1).toUpperCase() + 
                        entity_name.substring(1).toLowerCase() + ".class)";


    debugPrint(data, "X_GroupQualifier after children");
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }

  public Object visit(X_IdentifiedByClause node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;


    //     if (!jc.active) {
    //       return data;
    //     }
    jc.indent++;
    debugPrint(data, "X_IdentifiedByClause before children");
    printActive("XP Active: IdentifiedByClause");

    data = node.childrenAccept(this, data);

    debugPrint(data, "X_IdentifiedByClause after children");
    jc.indent--;

    return data;
  }

  public Object visit(X_IdParameter node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_IdParameter before children");
    printActive("XP Active: IdParameter");

    data = node.childrenAccept(this, data);

    debugPrint(data, "X_IdParameter after children");
    jc.indent--;

    jc.active = false;

    return data;
  }

  public Object visit(X_MapAttributeDeclaration node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_MapAttributeDeclaration before children");
    printActive("XP Active: MapAttributeDeclaration");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_MapAttributeDeclaration after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }


  public Object visit(X_CaseExpr node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_CaseExpr before children");
    printActive("XP Active: X_CaseExpr");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_CaseExpr after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }


  public Object visit(X_CaseExprAction node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_CaseExprAction before children");
    printActive("XP Active: X_CaseExprAction");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_CaseExprAction after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }

/*
  public Object visit(X_ForExpr node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_ForExpr before children");
    printActive("XP Active: X_ForExpr");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_ForExpr after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }

*/

  public Object visit(X_ForloopExpr node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_ForloopExpr before children");
    printActive("XP Active: X_ForloopExpr");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_ForloopExpr after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }

  public Object visit(X_ForeachExpr node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;
		boolean prev_foreach = jc.foreach;

    jc.active = true;

		jc.foreach = true;
		jc.foreach_where = new Vector();
    jc.indent++;
    debugPrint(data, "X_ForeachExpr before children");
    printActive("XP Active: X_ForeachExpr");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_ForeachExpr after children");
    jc.indent--;

//    jc.foreach = false;
//			jc.foreach_stack.push(jc.foreach_where);
			jc.foreach = prev_foreach;

//    jc.active = false;

    return data;
  }



  public Object visit(X_EntityInstantiationLoop node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_EntityInstantiationLoop before children");
    printActive("XP Active: X_EntityInstantiationLoop");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_EntityInstantiationLoop after children");
    jc.indent--;


    return data;
  }

  public Object visit(X_InstantiationForeachControl node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_InstantiationForeachControl before children");
    printActive("XP Active: X_InstantiationForeachControl");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_InstantiationForeachControl after children");
    jc.indent--;


    return data;
  }


  public Object visit(X_ForwardPathQualifier node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_ForwardPathQualifier before children");
    printActive("XP Active: X_ForwardPathQualifier");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_ForwardPathQualifier after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }


  public Object visit(X_BackwardPathQualifier node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_BackwardPathQualifier before children");
    printActive("XP Active: X_BackwardPathQualifier");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_BackwardPathQualifier after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }


  public Object visit(X_IfExpr node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    jc.active = true;

    jc.indent++;
    debugPrint(data, "X_IfExpr before children");
    printActive("XP Active: X_IfExpr");

    data = node.childrenAccept(this, data);

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    debugPrint(data, "X_IfExpr after children");
    jc.indent--;

//    jc.active = false;

    return data;
  }


  public Object visit(X_Index node, Object data) throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;

    debugPrint(data, "X_Index before children");
    printActive("XP Active: Index");

    data = node.childrenAccept(this, data);
    debugPrint(data, "X_Index after children");
    printActive("XP Active Ending: Index");
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    jc.indent--;

    return data;
  }

  public Object visit(X_IndexQualifier node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }


    // numeric index values are calculated during parsing, therefore we can skip all the nodes inside
    jc.indexing = true;
    jc.indent++;
    debugPrint(data, "X_IndexQualifier before children");
    printActive("XP Active: IndexQualifier");

    String original = jc.generated_java;


    //String original = jc.print_string; // !!!!! - original is stored in X_AssignmentStmt.java ?
    //    System.out.println("BEFORE INDEX: " +original);
    data = node.childrenAccept(this, data);

// no, it is the index itself
// original = jc.generated_java;

    jc.generated_java = original + ".indexing(" + node.index[0] + ", " + node.index[1] + ")";

		if (node.left_side) {
//	    jc.generated_java = original + ".replaceRange(" + node.index[0] + ", " + node.index[1] + ")";
	    
	    if (node.children.length > 1) { // for now we allow left-side substring only with two indices, one index means an aggregate
	    	jc.generated_java = original + ".replaceRange(" + node.index[0] + ", " + node.index[1] + ", ";
	    	jc.left_side_indexing++;
	  	} else {
	  	  jc.generated_java = original + ".indexing(" + node.index[0] + ", " + node.index[1] + ")";
	  	}
		} else {
  	  jc.generated_java = original + ".indexing(" + node.index[0] + ", " + node.index[1] + ")";
		}


    debugPrint(data, "X_IndexQualifier after children");
    jc.indexing = false;
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;
    printActive("XP Active Ending: IndexQualifier");

    return data;
  }

  public Object visit(X_BuiltInConstant node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    debugPrint(data, "X_BuiltInConstant before children");
    printActive("XP Active: BuiltInConstant: " + node);

    if (node.type == 1) { // E
      jc.generated_java = "Value.alloc(ExpressTypes.REAL_TYPE).set(_context, 2.718281828459)";
    } else if (node.type == 2) { // PI
      jc.generated_java = "Value.alloc(ExpressTypes.REAL_TYPE).set(_context, 3.1415926)";
    } else if (node.type == 3) { // SELF
      printActive("XP Active: BuiltInConstant - type 3");

      if (jc.ed instanceof EEntity_definition) {
        String entity_name = ((EEntity_definition) jc.ed).getName(null).replace('+', '$');
        String norm_entity_name = entity_name.substring(0, 1).toUpperCase() + 
                                  entity_name.substring(1).toLowerCase();

        //        	String self_type = "jsdai." + norm_entity_name + ".definition";
        String self_type = "jsdai." + constructSchemaClassNamePrefix(jc) + "C" + 
                           norm_entity_name + ".definition";
        jc.generated_java = "Value.alloc(" + self_type + ").set(_context, this)";
      } else {
      	if (jc.entity instanceof EWhere_rule) {
					EEntity parent_item = ((EWhere_rule)jc.entity).getParent_item(null);
      		if (parent_item instanceof EDefined_type) {
      			// cannot use this, use additional parameter, let's call it "self" because this name will not occur, it is reserved in express
						String self_type = node.getStaticTypeString((EDefined_type)parent_item, jc);
//	        	jc.generated_java = "Value.alloc(" + self_type + ").set(_context, self)";
	        	jc.generated_java = "self";
      		} else {
	        	printActive("XP Active: BuiltInConstant - type 3, NOT entity: " + jc.ed);
  	    		jc.generated_java = "_NOT_ENTITY_OR_TYPE_";
      		}
      	} else {
        	printActive("XP Active: BuiltInConstant - type 3, NOT entity: " + jc.ed);
      		jc.generated_java = "_NOT_ENTITY_OR_TYPE_";
      	}
      }
    } else if (node.type == 4) { // ?

      // here we have a problem: how to know what is the type of ? - different things have to be generated
      // indeterminate constant is supposed to be compatible with all data types.
      // we probably need a special adaptable data type to supported in Value.
      jc.generated_java = "Value.alloc(ExpressTypes.GENERIC_TYPE).unset()";
    } else { // error

      // part 11 lists also the following built-in constants:
      // FALSE
      // TRUE
      // UNKNOWN
      // However, the formal grammar does not have them. They are literals anyway.
      // probably a literal has a built-in constant value, 
      // but it does not need to be reflected in the grammar
      printActive("XP Active: BuiltInConstant - UNKNOWN type");
    }

    data = node.childrenAccept(this, data);
    debugPrint(data, "X_BuiltInConstant after children");
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    printActive("XP Active ENDING: BuiltInConstant: " + node);

    return data;
  }

  public Object visit(X_PopulationDependentBound node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

// jc.pw.println("PDB-01");
    if (node.parent instanceof X_EntityDecl) {
// jc.pw.println("PDB-02");
      if (jc.ed != ((X_EntityDecl) node.parent).entity_definition) {
// jc.pw.println("PDB-03");
        return data;
      }
    }
// jc.pw.println("PDB-04");

    // rejects if the pdb is in a defined_type, while started from entity
    // this is because defined_type does not have a node, perhaps the node will be needed
    if (jc.ed != null) {
// jc.pw.println("PDB-05");
      if (node.parent instanceof X_SchemaDecl) {
// jc.pw.println("PDB-06");
        return data;
      }
    }
// jc.pw.println("PDB-07");

    if ((jc.ed == null) && (jc.entity != null)) {
// jc.pw.println("PDB-08");
      if (node.parent instanceof X_SchemaDecl) {
// jc.pw.println("PDB-09");
        return data;
      }
    }
    
// jc.pw.println("PDB-10");

    jc.active = true;

    jc.indent++;

    debugPrint(data, "X_PobulationDependentBound before children");
    printActive("XP Active: PopulationDependentBound");

    printDDebug("XAXA pdb: " + node.pdb + ", entity: " + node.entity);


    		String method_signature = "public Value " + node.pdb.getMethod_name(null) + "(SdaiContext _context) throws SdaiException {\n\t\treturn (";
    //			public Value calculatePDB0(SdaiContext context) throws SdaiException {
    //		return Value.alloc(ExpressTypes.INTEGER_TYPE).set(25);
    data = node.childrenAccept(this, data);
    debugPrint(data, "X_PopulationDependentBound after children");
    printActive("XP Active Ending: PopulationDependentBound");

    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();

    String indent_str = "\t\t";

    if (node.java_contains_statements) {
      for (int i = 0; i < node.variable_declarations.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.variable_declarations.elementAt(i));
      }

      for (int i = 0; i < node.statements.size(); i++) {
        node.forwarded_java += ("\n" + indent_str + node.statements.elementAt(i));
      }

      node.java_contains_statements = false;
      node.variable_declarations = null;
      node.statements = null;
    }

// jc.pw.println("PDB-11");

    if (jc.entity == node.pdb) {
// jc.pw.println("PDB-12");
      if (jc.ed instanceof EEntity_definition) {
// jc.pw.println("PDB-13");
        // explicit attribute - can print directly, because the same file and location in the file is OK.
        // this whole part vas commented out - start -
        			jc.pdb.addElement(method_signature + 	"\t\t" + jc.generated_java + "\n\t);");
        			jc.pw.println(method_signature);
//        			jc.pw.println("\t\t" + jc.generated_java + "\n\t);");
        			jc.pw.println("\t\t" + jc.generated_java + "\n\t);\n\t}");
        			jc.generated_java = "";
//        				jc.pw.println("2: " + jc.print_string);
//              	jc.pw.println("3: " + node.forwarded_java);
//              	jc.pw.println("4: " + node.generated_java);
        	      node.forwarded_java = "";
          	    node.generated_java = "";
//             } else {
//               node.generated_java = node.forwarded_java + node.generated_java;
//              node.forwarded_java = "";
//             }
        // this whole part vas commented out - end -
      }
	
      jc.generated_java = "";
    } else {
// jc.pw.println("PDB-14");
      jc.pdb.addElement("\tpublic Value " + node.pdb.getMethod_name(null) + 
                        "(SdaiContext _context) throws SdaiException {" + node.forwarded_java + 
                        "\n\t\treturn (\n\t\t\t" + jc.generated_java + "\n\t\t);\n\t}");
      jc.generated_java = "";
    }
// jc.pw.println("PDB-15");

    jc.indent--;

    if (jc.entity == node.pdb) {
// jc.pw.println("PDB-16");
      jc.active = false;
    }
// jc.pw.println("PDB-17");

    return data;
  }


  public Object visit(X_UnrecognizedReference node, Object data)
               throws SdaiException {
    JavaClass jc = (JavaClass) data;

    if (!jc.active) {
      return data;
    }

    jc.indent++;
    printActive("XP Active: UnrecognizedReference");
    data = node.childrenAccept(this, data);
    node.getOpeningJavaFromChildren();
    node.getForwardedJavaFromChildren();
    node.getGeneratedJavaFromChildren();
    jc.indent--;

    return data;
  }



	String getOriginalAttributeName(EAttribute attr) throws SdaiException {	
		
		EAttribute attr2 = attr;
		

		for (;;) {
	
			if (attr2 instanceof EExplicit_attribute) {
				if (((EExplicit_attribute)attr2).testRedeclaring(null)) {
					attr2 = ((EExplicit_attribute)attr2).getRedeclaring(null);
				} else {
					return attr2.getName(null);
				}			
			} else
			if (attr2 instanceof EDerived_attribute) {
				if (((EDerived_attribute)attr2).testRedeclaring(null)) {
					attr2 = (EAttribute)((EDerived_attribute)attr2).getRedeclaring(null);
				} else {
					return attr2.getName(null);
				}			
			} else
			if (attr2 instanceof EInverse_attribute) {
				if (((EInverse_attribute)attr2).testRedeclaring(null)) {
					attr2 = ((EInverse_attribute)attr2).getRedeclaring(null);
				} else {
					return attr2.getName(null);
				}			
			} else {
			// internal error 
				return "_WRONG_ATTRIBUTE_";
			}
		}
	}

  void processAttributes(EEntity_definition ed, Vector all_attributes, Vector all_flags)
                  throws SdaiException {
    //      Check each attribute if it is redeclared later.  Especially
    //      important cases when explicit is redeclared as derived.
    //      Although the internal number remains reserved, the field is
    //      not present.  However, in the case of java inheritance, we can
    //      do nothing.  Let's have the field as well, but need to know if
    //      the attribute is later redeclared as derived
    for (int sj = 0; sj < all_attributes.size(); sj++) {
      EAttribute a1 = (EAttribute) all_attributes.elementAt(sj);
      int attribute_flag = (int) ((Integer) all_flags.elementAt(sj)).intValue();

      if ((attribute_flag == AA_JAVA_EXPLICIT) || (attribute_flag == AA_SUPERTYPE_EXPLICIT) || 
              (attribute_flag == AA_CURRENT_EXPLICIT)) {
        for (int sjj = 0; sjj < all_attributes.size(); sjj++) {
          if (sj == sjj) {
            continue;
          }

          int attribute_flag2 = (int) ((Integer) all_flags.elementAt(sjj)).intValue();

          if ((attribute_flag2 == AA_JAVA_DERIVED_REDECLARING) || 
                  (attribute_flag2 == AA_CURRENT_DERIVED_REDECLARING) || 
                  (attribute_flag2 == AA_SUPERTYPE_DERIVED_REDECLARING)) {
            EAttribute a2 = (EAttribute) all_attributes.elementAt(sjj);
            EEntity a3 = ((EDerived_attribute) a2).getRedeclaring(null);

            if (a1 == a3) {
              // printDebug("CHANGING FLAG: " + a1.getName(null));
              // changing flag for a1 - explicit later redeclared as derived
              if (attribute_flag == AA_JAVA_EXPLICIT) {
                attribute_flag = AA_JAVA_EXPLICIT_TO_DERIVED;
              } else if (attribute_flag == AA_SUPERTYPE_EXPLICIT) {
                attribute_flag = AA_SUPERTYPE_EXPLICIT_TO_DERIVED;
              } else if (attribute_flag == AA_CURRENT_EXPLICIT) {
                attribute_flag = AA_CURRENT_EXPLICIT_TO_DERIVED;
              }

              all_flags.set(sj, new Integer(attribute_flag));
            }
          }
        } // for sjj
      } // if explicit
    } // for sj
  } // method



/*

		Vector global_all_attributes ; // old version
		Vector global_all_flags;       // old version
		ArrayList global_all_attributes_all; 
		ArrayList global_all_flags_all;
		Vector global_all_attributes_ext;
		Vector global_all_flags_ext;


*/


  String getStaticAttrName(EAttribute attribute, JavaClass jc)
                    throws SdaiException {
 // System.out.println("><01>< calculating attribute internal name: " + attribute);
    // have to calculate the internal attribute number
    // also, it would be good to add entity and/or package information when needed.
    String result = null;
    String static_attribute_name = null;
    String attribute_name = attribute.getName(null);
//    String attribute_name = getOriginalAttributeName(attribute);


    //    ESchema_definition the_sd = jc.sd;
    EGeneric_schema_definition the_sd = jc.sd;
    EEntity_definition the_ed = jc.ed;
    SdaiModel the_model = jc.model;

    // EEntity_definition parent_entity = attribute.getParent_entity(null);
    EEntity_definition parent_entity = (EEntity_definition) attribute.getParent_entity(null);
    SdaiModel parent_model = parent_entity.findEntityInstanceSdaiModel();
    String parent_model_name = parent_model.getName();

    // if parent_entity != the_ed, then entity prefix is needed.
    // if parent_model != the_model, then schema prefix is needed.
    String schema_prefix = "";
    String entity_prefix = "";

//System.out.println("<getStaticAttrName> attribute: " + attribute +
//									 "\the ed: " + the_ed +
//									 "\tparent_entity: " + parent_entity);

    if (parent_entity != the_ed) {
      String the_entity_name = parent_entity.getName(null).replace('+', '$');
      entity_prefix = "C" + the_entity_name.substring(0, 1).toUpperCase() + 
                      the_entity_name.substring(1).toLowerCase() + ".";
    }

//    jsdai.dictionary.EGeneric_schema_definition the_parent_schema = parent_model.getUnderlyingSchema();
    jsdai.dictionary.ESchema_definition the_parent_schema = parent_model.getUnderlyingSchema();
    String the_schema_name = the_parent_schema.getName(null);

    if (parent_model != the_model) {
      //      schema_prefix = "jsdai.S" + the_schema_name.substring(0,1).toUpperCase() + the_schema_name.substring(1).toLowerCase() + ".";
      schema_prefix = constructTypeSchemaPrefixFromModel(parent_model_name) + ".";
    } else {
      // lets do the same for now
      //         schema_prefix = "jsdai.S" + the_schema_name.substring(0,1).toUpperCase() + the_schema_name.substring(1).toLowerCase() + ".";
      schema_prefix = constructTypeSchemaPrefix(jc);
    }

// ok, add new stuff here:
	
	if (jc.all_attributes != null) { // may be invoked not from inside entities only

    for (int i = 0; i < jc.all_attributes.size(); i++) {
      TheAttribute tattr = (TheAttribute) jc.all_attributes.get(i);
 // System.out.println("><04>< global tatr: " + tattr);

			/*
				newest implementation
				we are interested in consolidated only,
				if consolidated, check if it is our attribute, if not,
				check if our attribute is anywhere in the list of redeclared_by.
				if either of those is true,
				take the appropriate consolidated_index, corresponding to the type of the attribute
	
			*/

			if (tattr.consolidated) {
				boolean found_attribute = false;
				if (tattr.attr == attribute) {
					found_attribute = true;
				} else {
      		found_attribute = searchInRedeclared(tattr, attribute);
     
				
				}
				if (found_attribute) {
					if (attribute instanceof EExplicit_attribute) {
					  static_attribute_name = "a" + tattr.consolidated_explicit_index + "$";
					} else
					if (attribute instanceof EDerived_attribute) {
					  static_attribute_name = "d" + tattr.consolidated_derived_index + "$";
					} else
					if (attribute instanceof EInverse_attribute) {
					  static_attribute_name = "i" + tattr.consolidated_inverse_index + "$";
					}
				}
			}
// older implementation
/*
			if (tattr.attr == attribute) {
				switch (tattr.type) {
					case 0: //explicit
					  static_attribute_name = "a" + tattr.explicit_index + "$";
						break;
					case 1: // derived
					  static_attribute_name = "d" + tattr.derived_index + "$";
					  break;
					case 2: // inverse  
					  static_attribute_name = "i" + tattr.inverse_index + "$";
						break;
				}
				break;
			}
*/


		}	// for
		
	} // if not null



    if (entity_prefix.equalsIgnoreCase("")) {
      result = static_attribute_name;
    } else {
      result = schema_prefix + entity_prefix + "attribute" + 
               attribute_name.substring(0, 1).toUpperCase() + 
               attribute_name.substring(1).toLowerCase() + "(null)";
    }
// System.out.println("><02>< calculating attribute internal name - result: " + result);

    return result;
  }


	/*
			 recursive search:
			 search in all redeclared_by,
			 and for each redeclared_by, search in its redeclared_by as well
	*/
	boolean searchInRedeclared(TheAttribute tattr, EAttribute attribute) {

		boolean result = false;
		
		if (tattr.redeclared_by != null) {
   		for (int j = 0; j < tattr.redeclared_by.size(); j++) {
				TheAttribute tattr3 = (TheAttribute)tattr.redeclared_by.get(j);
				if (tattr3.attr == attribute) {
				  return true;
				}	
				result = searchInRedeclared(tattr3, attribute);
				if (result) {
					return true;
				}
			}
		}
		return result;
	}					



/*

1.
><01>< calculating attribute internal name: #1593=EXPLICIT_ATTRIBUTE('location',#1591,0,#1564,$,.F.);
old:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CMulti_stratum_structured_template_armx.attributeLocation(null)
new:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CMulti_stratum_structured_template_armx.attributeLocation(null)


2.
><01>< calculating attribute internal name: #1598=DERIVED_ATTRIBUTE('padstacks',#1596,1,#1618,$);
old:
><02>< calculating attribute internal name - result: d1$
new:
><02>< calculating attribute internal name - result: null


3.
><01>< calculating attribute internal name: #1588=INVERSE_ATTRIBUTE('templates',#1586,0,#1607,$,#1615,#1624,$,.F.);
old:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CStructured_template_armx.attributeTemplates(null)
new:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CStructured_template_armx.attributeTemplates(null)

4.
><01>< calculating attribute internal name: #1588=INVERSE_ATTRIBUTE('templates',#1586,0,#1607,$,#1615,#1624,$,.F.);
old:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CStructured_template_armx.attributeTemplates(null)
new:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CStructured_template_armx.attributeTemplates(null)

5.
><01>< calculating attribute internal name: #1593=EXPLICIT_ATTRIBUTE('location',#1591,0,#1564,$,.F.);
old:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CMulti_stratum_structured_template_armx.attributeLocation(null)
new:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CMulti_stratum_structured_template_armx.attributeLocation(null)

6.
><01>< calculating attribute internal name: #1614=DERIVED_ATTRIBUTE('location',#1596,0,#1564,#1593);
old:
><02>< calculating attribute internal name - result: null
new:
><02>< calculating attribute internal name - result: null

7.
><01>< calculating attribute internal name: #1593=EXPLICIT_ATTRIBUTE('location',#1591,0,#1564,$,.F.);
old:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CMulti_stratum_structured_template_armx.attributeLocation(null)
new:
><02>< calculating attribute internal name - result: jsdai.SDebug_01.CMulti_stratum_structured_template_armx.attributeLocation(null)


*/




}/*end*/
