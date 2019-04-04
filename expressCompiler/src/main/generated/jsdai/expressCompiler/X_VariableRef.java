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

// %modified: 1016210368679 %

/* Generated By:JJTree: Do not edit this line. X_VariableRef.java */
package jsdai.expressCompiler;

public class X_VariableRef
    extends ExpressionNode {

  // public class X_VariableRef extends SimpleNode {
  String name;
  ECtVariable variable;
  jsdai.lang.EEntity scope;
  int depth;

  public X_VariableRef(int id) {
    super(id);
  }

  public X_VariableRef(Compiler2 p, int id) {
    super(p, id);
  }

  /**
   * Accept the visitor.
   **/
  public Object jjtAccept(Compiler2Visitor visitor, Object data)
      throws jsdai.lang.SdaiException {
    return visitor.visit(this, data);
  }

  String constructVariableReference() {
    String var_name = variable.getScope_id() + name;

    for (int i = variable.depth; i < depth; i++) {
      var_name = "parent." + var_name;
    }

    return var_name;
  }

  public void dump(String prefix) {

    if (name != null) {
      System.out.println(toString(prefix) + " - " + name + " (" + variable.key + "), reference depth: " + depth + ", declaration depth: " + variable.depth);
    }
    else if (variable != null) {
      System.out.println(toString(prefix) + " - " + variable.getName() + " (" + variable.key + ")");
    }
    else {
      System.out.println(toString(prefix) + " - NULL");
    }

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