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

// %modified: 1016210368332 %

/* Generated By:JJTree: Do not edit this line. X_DomainRule.java */
package jsdai.expressCompiler;

import java.util.*;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

public class X_DomainRule
    extends SimpleNode {
  EWhere_rule where_rule;
  boolean global_rule; // may not be needed, because if our global rule, then already active
  boolean map_definition;
  SdaiModel model;

  public X_DomainRule(int id) {
    super(id);
  }

  public X_DomainRule(Compiler2 p, int id) {
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

            printDDebug("### X_DomainRule: " + jc.generated_java, jc);
// 						jc.pw.println("/* In DomainRule: */");
            // suppose, that there are no complex domain rules in map_definition
            //						if(map_definition) jc.pw.print(jc.generated_java);

          } // jc active

        } // jc null

      } // for

    } // if children

    return data;
  } // childrenAccept method

  String constructSelfType(EDefined_type dt2, JavaClass jc) throws SdaiException {
    model = jc.model;

    EEntity bt = dt2.getDomain(null);
    String return_object = "_SOMETHING_WRONG_";

    if (bt instanceof ESimple_type) {
      ESimple_type st = (ESimple_type) bt;
      return_object = getSimpleTypeAttributeString(st);
    }
    else if (bt instanceof EAggregation_type) {
      EAggregation_type at = (EAggregation_type) bt;
      return_object = getAggregateAttributeString(at);
    }
    else if (bt instanceof EDefined_type) {
      EDefined_type dt = (EDefined_type) bt;
      return_object = getDefinedTypeAttributeString(dt);
    }
    else if (bt instanceof EEntity_definition) {
      EEntity_definition ed1 = (EEntity_definition) bt;
      String entity_name = ed1.getName(null);
      return_object = "Object";
    }
    else if (bt instanceof EParameter) {     // generalized attribute type - ammendment
      return_object = "Object";
    }
    else if (bt instanceof ESelect_type) {
      return_object = "Object";
    }
    else if (bt instanceof EEnumeration_type) {
      return_object = "int";
    }
    else {
    }

    return return_object;
  }

  String getSimpleTypeAttributeString(ESimple_type st) throws SdaiException {
    String return_object = "_WRONG_SIMPLE_";

    if (st instanceof EInteger_type) {
      return_object = "int";
    }
    else if (st instanceof ENumber_type) {
      return_object = "double";
    }
    else if (st instanceof EReal_type) {
      return_object = "double";
    }
    else if (st instanceof EBoolean_type) {
      return_object = "int";
    }
    else if (st instanceof ELogical_type) {
      return_object = "int";
    }
    else if (st instanceof EBinary_type) {
      return_object = "Binary";
    }
    else if (st instanceof EString_type) {
      return_object = "String";
    }

    return return_object;
  }

  String getAggregateAttributeString(EAggregation_type at) throws SdaiException {
    String return_object = "_WRONG_AGGREGATION_";

    int aggregate_depth = 1;
    EEntity an_ss;
    EEntity ass = at;
    String aggr_prefices = "";
    an_ss = at.getElement_type(null);

    for (; ; ) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        ass = an_ss;
        an_ss = ((EDefined_type) an_ss).getDomain(null);
        done_something = true;
      }
      else if (an_ss instanceof EAggregation_type) {
        aggr_prefices += "a";
        aggregate_depth++;
        ass = an_ss;

        an_ss = ((EAggregation_type) an_ss).getElement_type(null);
        done_something = true;
      }

      if (!done_something) {
        break;
      }
    }

    String aggr_prefix = "A" + aggr_prefices;

    if (an_ss instanceof ESelect_type) {
      return_object = "" + getAggregatePackage(aggr_prefix, ass);
    }
    else if (an_ss instanceof EEntity_definition) {
      return_object = "" + getAggregatePackage(aggr_prefix, an_ss);
    }
    else if (an_ss instanceof EEnumeration_type) {
      return_object = "" + aggr_prefix + "_enumeration";
    }
    else if (an_ss instanceof EInteger_type) {
      return_object = "" + aggr_prefix + "_integer";
    }
    else if (an_ss instanceof ENumber_type) {
      return_object = "" + aggr_prefix + "_double";
    }
    else if (an_ss instanceof EReal_type) {
      return_object = "" + aggr_prefix + "_double";
    }
    else if (an_ss instanceof EString_type) {
      return_object = "" + aggr_prefix + "_string";
    }
    else if (an_ss instanceof ELogical_type) {
      return_object = "" + aggr_prefix + "_enumeration";
    }
    else if (an_ss instanceof EBoolean_type) {
      return_object = "" + aggr_prefix + "_boolean";
    }
    else if (an_ss instanceof EBinary_type) {
      return_object = "" + aggr_prefix + "_binary";
    }
    else if (an_ss instanceof EData_type) {
      if (((EData_type) an_ss).getName(null).equalsIgnoreCase("_GENERIC")) {
        return_object = "CAggregate";
      }
      else if (((EData_type) an_ss).getName(null).equalsIgnoreCase("_ENTITY")) {
        return_object = "AEntity";
      }
    }

    return return_object;
  }

  String getDefinedTypeAttributeString(EDefined_type dt) throws SdaiException {
    String return_object = "";
    EEntity ut = dt.getDomain(null);

    if (ut instanceof ESimple_type) {
      return_object = getSimpleTypeAttributeString((ESimple_type) ut);
    }
    else if (ut instanceof EAggregation_type) {
      return_object = getAggregateAttributeString((EAggregation_type) ut);
    }
    else if (ut instanceof EDefined_type) {
      return_object = getDefinedTypeAttributeString((EDefined_type) ut);
    }
    else if (ut instanceof EEnumeration_type) {
      return_object = "int";
    }
    else if (ut instanceof ESelect_type) {
      return_object = "Object";
    }

    return return_object;
  }

  String getAggregatePackage(String aggr_prefix, EEntity ee)
      throws SdaiException {
    String a_package = "";
    String a_name = "";

    if (ee instanceof ENamed_type) {
      a_name = ((ENamed_type) ee).getName(null);
      a_name = a_name.substring(0, 1).toUpperCase() + a_name.substring(1).toLowerCase();
    }

    SdaiModel a_model = ee.findEntityInstanceSdaiModel();

    if (a_model != model) {
      String a_schema_name = getSchema_definitionFromModel(a_model).getName(null);

      if (a_schema_name.equalsIgnoreCase("Sdai_dictionary_schema")) {
        a_package = "jsdai.dictionary.";
      }
      else {
        a_package = "jsdai.S" + a_schema_name.substring(0, 1).toUpperCase() +
            a_schema_name.substring(1).toLowerCase() + ".";
      }
    }

    //      return a_package;
    return a_package + aggr_prefix + a_name;
  }

  static EGeneric_schema_definition getSchema_definitionFromModel(SdaiModel sm)
      throws SdaiException {
    Aggregate ia = sm.getEntityExtentInstances(EGeneric_schema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EGeneric_schema_definition inst = (EGeneric_schema_definition) ia.getCurrentMemberObject(
          iter_inst);

      return inst;
    }

    return null;
  }

}
