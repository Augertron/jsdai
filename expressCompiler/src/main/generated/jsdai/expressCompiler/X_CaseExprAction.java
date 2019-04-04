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

/* Generated By:JJTree: Do not edit this line. X_CaseExprAction.java */

package jsdai.expressCompiler;

import java.util.*;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;

public class X_CaseExprAction extends SimpleNode {

  Vector label_expressions = null;

  public X_CaseExprAction(int id) {
    super(id);
  }

  public X_CaseExprAction(Compiler2 p, int id) {
    super(p, id);
  }

  /**
   * Accept the visitor.
   **/
  public Object jjtAccept(Compiler2Visitor visitor, Object data) throws jsdai.lang.SdaiException {
    return visitor.visit(this, data);
  }

  public Object childrenAccept(Compiler2Visitor visitor, Object data) throws SdaiException {

// 	 System.out.println("---<CaseExprAction> ------------");

    JavaClass jc = (JavaClass) data;

    if (children != null) {

      // these initializations are from SimpleNode, perhaps needed or not
      variable_names = new Vector();
      variable_declarations = new Vector();
      statements = new Vector();
      initializing_code = new Vector();

      label_expressions = new Vector();

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

//							System.out.println("<CaseExprAction> child: " +  children[i] + ", jc.generated_java: " + jc.generated_java);

							/*

									 the children nodes are as follows, for example:
									
									'one', 'two', 'three' : 'first';

									CaseLabel    - jc.generated_java : Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "one")
									CaseLabel    - jc.generated_java : Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "two")  
									CaseLabel    - jc.generated_java : Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "three")
									expression   - jc.generated_java : Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "first")
		
									need to return:
									
									because in the generated java expression that is needed to be returned also the expression for case selector from the parent node is needed
									(possibly multiple times), perhaps we do nothing here, except that we put the expressions for CaseLabel nodes into a Vector
									and let the parent node to Handle the whole thing
							
							*/

            if (children[i] instanceof X_CaseLabel) {
              label_expressions.addElement(jc.generated_java);
              jc.generated_java = "";
            }

          } // if jc.active

        } //  if jc not null

      } // loop through all the children

    } // if children not null

    return data;
  }

}
