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

import java.util.*;

public class ExpressionNode extends SimpleNode {
	

	// to SimpleNode
	/*
	static int uid = 0;
	boolean java_contains_statements;
	String variable_declaration;
	Vector statements;
	String generated_code;
	int current_uid; // I doubt if I'1l need it
	*/
	
  public ExpressionNode(int id) {
    super(id);
    // java_contains_statements = false;
  }

  public ExpressionNode(Compiler2 p, int id) {
    super(p, id);
    // java_contains_statements = false;
  }

	

  /** Accept the visitor. **/
	/*
  public Object childrenAccept(Compiler2Visitor visitor, Object data)  throws jsdai.lang.SdaiException {
		JavaClass jc = (JavaClass)data;
    if (children != null) {
      variable_declaration = "";
      statements = new Vector(); 
      generated_code = "";     
      for (int i = 0; i < children.length; ++i) {
        children[i].jjtAccept(visitor, data);
				if (jc != null) {
					if (jc.active) {
						if (children[i] instanceof ExpressionNode) {
							if (((ExpressionNode)children[i]).java_contains_statements) {
								java_contains_statements = true;
								variable_declaration += "\n" + ((ExpressionNode)children[i]).variable_declaration;
								statements.add(((ExpressionNode)children[i]).statements);
								generated_code += "\n" + ((ExpressionNode)children[i]).generated_code;
							} // if contains statements
						} // if ExpressionNode
					} // if active
				}	 // if jc not null
      } // loop through all the children
    } // if has children
    return data;
  }
	*/
	
   /** Accept the visitor. **/
//   public Object jjtAccept(Compiler2Visitor visitor, Object data)  throws jsdai.lang.SdaiException {
//     return visitor.visit(this, data);
//   }
}


