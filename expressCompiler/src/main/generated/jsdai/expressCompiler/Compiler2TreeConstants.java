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

/* Generated By:JJTree: Do not edit this line. D:\work\lksoft\git\jsdai-core\expressCompiler\src\main\generated\jsdai\expressCompiler\Compiler2TreeConstants.java */

package jsdai.expressCompiler;

public interface Compiler2TreeConstants {
  public int JJTVOID = 0;
  public int JJTCASESELECTOR = 1;
  public int JJTDOMAINRULELOGICALEXPRESSION = 2;
  public int JJTLISTOFSTMT = 3;
  public int JJTIFCONDITION = 4;
  public int JJTQUERYLOGICALEXPRESSION = 5;
  public int JJTATTRIBUTEREF = 6;
  public int JJTCONSTANTREF = 7;
  public int JJTENUMERATIONREF = 8;
  public int JJTPARAMETERREF = 9;
  public int JJTVARIABLEREF = 10;
  public int JJTAGGREGATEINITIALIZER = 11;
  public int JJTAGGREGATESOURCE = 12;
  public int JJTALGORITHMHEAD = 13;
  public int JJTALIASSTMT = 14;
  public int JJTASSIGNMENTSTMT = 15;
  public int JJTATTRIBUTEQUALIFIER = 16;
  public int JJTBACKWARDPATHQUALIFIER = 17;
  public int JJTBINDINGHEADER = 18;
  public int JJTPOPULATIONDEPENDENTBOUND = 19;
  public int JJTBUILTINCONSTANT = 20;
  public int JJTCASEACTION = 21;
  public int JJTCASEEXPR = 22;
  public int JJTCASEEXPRACTION = 23;
  public int JJTCASELABEL = 24;
  public int JJTCASESTMT = 25;
  public int JJTCASEOTHERWISE = 26;
  public int JJTCOMPOUNDSTMT = 27;
  public int JJTCONSTANTBODY = 28;
  public int JJTCONSTANTDECL = 29;
  public int JJTDECLARATION = 30;
  public int JJTDEPENDENTMAPDECL = 31;
  public int JJTDEPMAPPARTITION = 32;
  public int JJTDERIVEDATTR = 33;
  public int JJTDOMAINRULE = 34;
  public int JJTELEMENT = 35;
  public int JJTENTITYCONSTRUCTOR = 36;
  public int JJTENTITYCONSTRUCTORPARAMETER = 37;
  public int JJTENTITYDECL = 38;
  public int JJTENTITYINSTANTIATIONLOOP = 39;
  public int JJTESCAPESTMT = 40;
  public int JJTEXPRESSION = 41;
  public int JJTRELOPEXTENDED = 42;
  public int JJTPOWEROP = 43;
  public int JJTFOREACHEXPR = 44;
  public int JJTFORLOOPEXPR = 45;
  public int JJTFORWARDPATHQUALIFIER = 46;
  public int JJTFUNCTIONCALL = 47;
  public int JJTFUNCTIONDECL = 48;
  public int JJTFUNCTIONHEAD = 49;
  public int JJTGROUPQUALIFIER = 50;
  public int JJTIDENTIFIEDBYCLAUSE = 51;
  public int JJTIDPARAMETER = 52;
  public int JJTIFEXPR = 53;
  public int JJTIFSTMT = 54;
  public int JJTINCREMENTCONTROL = 55;
  public int JJTINDEX = 56;
  public int JJTINDEXQUALIFIER = 57;
  public int JJTINSTANTIATIONFOREACHCONTROL = 58;
  public int JJTINTERVAL = 59;
  public int JJTINTERVALHIGH = 60;
  public int JJTINTERVALITEM = 61;
  public int JJTINTERVALLOW = 62;
  public int JJTINTERVALOP = 63;
  public int JJTLITERAL = 64;
  public int JJTLOCALDECL = 65;
  public int JJTLOCALVARIABLE = 66;
  public int JJTMAPATTRIBUTEDECLARATION = 67;
  public int JJTMAPCALL = 68;
  public int JJTMAPDECL = 69;
  public int JJTNULLSTMT = 70;
  public int JJTPARAMETER = 71;
  public int JJTPOPULATION = 72;
  public int JJTPROCEDURECALLSTMT = 73;
  public int JJTPROCEDUREDECL = 74;
  public int JJTPROCEDUREHEAD = 75;
  public int JJTUNRECOGNIZEDREFERENCE = 76;
  public int JJTQUERYEXPRESSION = 77;
  public int JJTREPEATSTMT = 78;
  public int JJTRETURNSTMT = 79;
  public int JJTRULEDECL = 80;
  public int JJTALLSCHEMAS = 81;
  public int JJTSCHEMADECL = 82;
  public int JJTADDLIKEOP = 83;
  public int JJTUNARYOP = 84;
  public int JJTSKIPSTMT = 85;
  public int JJTSUBTYPEBINDINGHEADER = 86;
  public int JJTTARGETPARAMETERREF = 87;
  public int JJTSOURCEPARAMETERREF = 88;
  public int JJTMULTIPLICATIONLIKEOP = 89;
  public int JJTTYPEDECL = 90;
  public int JJTUNTILCONTROL = 91;
  public int JJTWHERECLAUSE = 92;
  public int JJTWHILECONTROL = 93;

  public String[] jjtNodeName = {
      "void",
      "CaseSelector",
      "DomainRuleLogicalExpression",
      "ListOfStmt",
      "IfCondition",
      "QueryLogicalExpression",
      "AttributeRef",
      "ConstantRef",
      "EnumerationRef",
      "ParameterRef",
      "VariableRef",
      "AggregateInitializer",
      "AggregateSource",
      "AlgorithmHead",
      "AliasStmt",
      "AssignmentStmt",
      "AttributeQualifier",
      "BackwardPathQualifier",
      "BindingHeader",
      "PopulationDependentBound",
      "BuiltInConstant",
      "CaseAction",
      "CaseExpr",
      "CaseExprAction",
      "CaseLabel",
      "CaseStmt",
      "CaseOtherwise",
      "CompoundStmt",
      "ConstantBody",
      "ConstantDecl",
      "Declaration",
      "DependentMapDecl",
      "DepMapPartition",
      "DerivedAttr",
      "DomainRule",
      "Element",
      "EntityConstructor",
      "EntityConstructorParameter",
      "EntityDecl",
      "EntityInstantiationLoop",
      "EscapeStmt",
      "Expression",
      "RelOpExtended",
      "PowerOp",
      "ForeachExpr",
      "ForloopExpr",
      "ForwardPathQualifier",
      "FunctionCall",
      "FunctionDecl",
      "FunctionHead",
      "GroupQualifier",
      "IdentifiedByClause",
      "IdParameter",
      "IfExpr",
      "IfStmt",
      "IncrementControl",
      "Index",
      "IndexQualifier",
      "InstantiationForeachControl",
      "Interval",
      "IntervalHigh",
      "IntervalItem",
      "IntervalLow",
      "IntervalOp",
      "Literal",
      "LocalDecl",
      "LocalVariable",
      "MapAttributeDeclaration",
      "MapCall",
      "MapDecl",
      "NullStmt",
      "Parameter",
      "Population",
      "ProcedureCallStmt",
      "ProcedureDecl",
      "ProcedureHead",
      "UnrecognizedReference",
      "QueryExpression",
      "RepeatStmt",
      "ReturnStmt",
      "RuleDecl",
      "AllSchemas",
      "SchemaDecl",
      "AddLikeOp",
      "UnaryOp",
      "SkipStmt",
      "SubtypeBindingHeader",
      "TargetParameterRef",
      "SourceParameterRef",
      "MultiplicationLikeOp",
      "TypeDecl",
      "UntilControl",
      "WhereClause",
      "WhileControl",
  };
}