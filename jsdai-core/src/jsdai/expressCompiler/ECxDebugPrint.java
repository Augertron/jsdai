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

// %modified: 1016210367483 %

/**
 *
 * Copyright (c) 1996-1997 Sun Microsystems, Inc.
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.
 *
 */

/* This is an example of how the Visitor pattern might be used to
implement the dumping code that comes with SimpleNode.  It's a bit
long-winded, but it does illustrate a couple of the main points.
1) the visitor can maintain state between the nodes that it visits
(for example the current indentation level).
2) if you don't implement a jjtAccept() method for a subclass of
SimpleNode, then SimpleNode's acceptor will get called.
3) the utility method childrenAccept() can be useful when
implementing preorder or postorder tree walks.
Err, that's it. */
package jsdai.expressCompiler;

public class ECxDebugPrint
  implements Compiler2Visitor {
  private int indent = 0;

  private String indentString() {
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < indent; ++i) {
      sb.append(" ");
    }

    return sb.toString();
  }

  public Object visit(SimpleNode node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node + ": acceptor not unimplemented in subclass?");
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AllSchemas node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Declaration node, Object data)
               throws jsdai.lang.SdaiException {
//    String entity_name = node.entity_definition.getName(null);
//    System.out.println(indentString() + node + " >> " + entity_name);
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_DerivedAttr node, Object data)
               throws jsdai.lang.SdaiException {

    // this prints the whole derived attribute directly from tokens as it is in original express (spaces and newlines not re-created)
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
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Expression node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_SimpleExpression node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_RelOpExtended node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_RelOp node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Term node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AddLikeOp node, Object data)
               throws jsdai.lang.SdaiException {
    int nr_of_operands = node.jjtGetNumChildren() - 1;
    String operations = " >> Operations: ";

    for (int i = 0; i < nr_of_operands; i++) {
//      switch (node.operations[i]) {
      switch (((Integer)node.operations.get(i)).intValue()) {
        case 1:
          operations += "+ ";
          break;

        case 2:
          operations += "- ";
          break;

        default:
          operations += "other ";
          break;
      }
    }

    System.out.println(indentString() + node + operations);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Factor node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_MultiplicationLikeOp node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_SimpleFactor node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Primary node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Literal node, Object data)
               throws jsdai.lang.SdaiException {
    String value = " >> ";

    if (node.type == 2) {
      value += node.int_value;
    }

    System.out.println(indentString() + node + value);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_QualifiableFactor node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AttributeRef node, Object data)
               throws jsdai.lang.SdaiException {
    if (!node.already_processed) {
			JavaClass jc = (JavaClass) data;
      node.process(jc);
    }

    String attr_name;

    if (node.attribute != null) {
      attr_name = node.attribute.getName(null);
    } else {
      attr_name = "attribute_is_null";
    }

    String attr_name_method = "get" + attr_name.substring(0, 1).toUpperCase() + attr_name.substring(1).toLowerCase() + "(null)";
    System.out.println(indentString() + node + " >> " + attr_name_method);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_EntityConstructor node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_EntityConstructorParameter node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_EntityDecl node, Object data)
               throws jsdai.lang.SdaiException {
    String entity_name = node.entity_definition.getName(null);
    System.out.println(indentString() + node + " >> " + entity_name);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_TypeDecl node, Object data)
               throws jsdai.lang.SdaiException {
    String type_name = node.dt.getName(null);
    System.out.println(indentString() + node + " >> " + type_name);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_MapDecl node, Object data)
               throws jsdai.lang.SdaiException {

    String map_name = node.map_definition.getName(null);
    System.out.println(indentString() + node + " >> " + map_name);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_DependentMapDecl node, Object data)
               throws jsdai.lang.SdaiException {

    String map_name = node.definition.getName(null);
    System.out.println(indentString() + node + " >> " + map_name);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_SchemaDecl node, Object data)
               throws jsdai.lang.SdaiException {
    String schema_name = node.schema_name;
    System.out.println(indentString() + node + " >> " + schema_name);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Interval node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IntervalHigh node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IntervalItem node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IntervalLow node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IntervalOp node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_QueryExpression node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AlgorithmHead node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AliasStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AssignmentStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CaseStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CaseSelector node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CaseAction node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CaseLabel node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CaseOtherwise node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CompoundStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ConstantDecl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ConstantBody node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ConstantRef node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_EscapeStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_FunctionDecl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ListOfStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_FunctionHead node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IfStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_LocalDecl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_LocalVariable node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_NullStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ProcedureCallStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ProcedureDecl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ProcedureHead node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_RepeatStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ReturnStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_SkipStmt node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_EnumerationRef node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ParameterRef node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_TargetParameterRef node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;
    return data;
  }

  public Object visit(X_SourceParameterRef node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_VariableRef node, Object data)
               throws jsdai.lang.SdaiException {
//    System.out.println(indentString() + node);
    System.out.println(indentString() + node + " - " + node.name);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Population node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IncrementControl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_WhileControl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_UntilControl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_FunctionCall node, Object data)
               throws jsdai.lang.SdaiException {
    if (node.outer_sizeof) {
    	System.out.println(indentString() + node + " - Outer SIZEOF");
    } else {
    	System.out.println(indentString() + node);
  	}
  	++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IfCondition node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Parameter node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AggregateInitializer node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Element node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AggregateSource node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_QueryLogicalExpression node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_RuleDecl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_WhereClause node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_DomainRule node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_DomainRuleLogicalExpression node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_PowerOp node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_UnaryOp node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_BuiltInConstant node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_Index node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IndexQualifier node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_GroupQualifier node, Object data)
               throws jsdai.lang.SdaiException {

    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_AttributeQualifier node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IdentifiedByClause node, Object data)
               throws jsdai.lang.SdaiException {

    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IdParameter node, Object data)
               throws jsdai.lang.SdaiException {

    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_MapAttributeDeclaration node, Object data)
               throws jsdai.lang.SdaiException {

    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_PopulationDependentBound node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_UnrecognizedReference node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_BindingHeader node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_DepMapPartition node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_SubtypeBindingHeader node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CaseExpr node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_CaseExprAction node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }
/*
  public Object visit(X_ForExpr node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }
*/
  public Object visit(X_ForloopExpr node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ForeachExpr node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_ForwardPathQualifier node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_BackwardPathQualifier node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_EntityInstantiationLoop node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_InstantiationForeachControl node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }

  public Object visit(X_IfExpr node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }


  public Object visit(X_MapCall node, Object data)
               throws jsdai.lang.SdaiException {
    System.out.println(indentString() + node);
    ++indent;
    data = node.childrenAccept(this, data);
    --indent;

    return data;
  }


} /*end*/
