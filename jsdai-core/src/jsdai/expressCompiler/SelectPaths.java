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

// %modified: 1016210368078 %
package jsdai.expressCompiler;
import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;

import java.io.*;
import java.util.*;


public class SelectPaths {
  static final int XT_ENTITY = 1;
  static final int XT_ENUMERATION = 2;
  static final int XT_ARRAY = 3;
  static final int XT_BAG = 4;
  static final int XT_LIST = 5;
  static final int XT_SET = 6;
  static final int XT_INTEGER = 7;
  static final int XT_NUMBER = 8;
  static final int XT_REAL = 9;
  static final int XT_STRING = 10;
  static final int XT_LOGICAL = 11;
  static final int XT_BOOLEAN = 12;
  static final int XT_BINARY = 13;
  static final int XT_AGGREGATE = 14;
  static final int AB_NOT_AGGREGATE = 0;
  static final int AB_INSTANCE = 1;
  static final int AB_INTEGER = 2;
  static final int AB_ENUMERATION = 3;
  static final int AB_BOOLEAN = 4;
  static final int AB_DOUBLE = 5;
  static final int AB_STRING = 6;
  static final int AB_BINARY = 7;
  static final int AB_SELECT = 8;

  static boolean replace_object_by_entity = true;

  //  static final int AB_ENTITY_SELECT =  9;
  //  static final int AB_DT_SELECT     = 10;
  //  static final int AB_MIXED_SELECT  = 11;
  //  static final int AB_SELECT_ERROR  = 12;
  ESelect_type owner;
  int select_type;
  Vector paths;
  Vector path_strings;
  Vector value_strings;
  Vector aggregate_bases;

  //  Vector nodes;
  //  Vector nodeS;
  //  Vector return_symbols;
  //  Vector return_types;
  Vector express_types;

  //  Vector express_names;
  //  Vector selects;
  //  Vector select_packages;
  //  Vector numbers;
  //  int array_nr[][];
  //  String values[];
  //  String schemas[];
  SelectPaths() {
    select_type = Integer.MIN_VALUE;
    paths = new Vector();
    path_strings = new Vector();
    value_strings = new Vector();
    aggregate_bases = new Vector();

    //    nodes     = new Vector();
    //    nodeS     = new Vector();
    //    return_symbols = new Vector();
    //    return_types = new Vector();
    express_types = new Vector();

    //    express_names = new Vector();
    //    selects = new Vector();
    //    select_packages = new Vector();
    //    numbers = new Vector();
  }

  void removeIdentical() {
    int nr_of_paths = paths.size();

    for (int fi = 0; fi < nr_of_paths - 1; fi++) {
      Vector master = ( Vector )paths.elementAt(fi);

      for (int fj = fi + 1; fj < nr_of_paths; fj++) {
        Vector slave = ( Vector )paths.elementAt(fj);

        if (master.size() == slave.size()) { // possibility of identical
          boolean identical = true;

          for (int fk = 0; fk < master.size(); fk++) {
            Object o1;
            Object o2;
            o1 = master.elementAt(fk);
            o2 = slave.elementAt(fk);

            if (o1 != o2) {
              identical = false;
              break;
            }
          }

          if (identical == true) { // remove slave
            paths.removeElementAt(fj);
            path_strings.removeElementAt(fj);
            value_strings.removeElementAt(fj);
            express_types.removeElementAt(fj);
            aggregate_bases.removeElementAt(fj);

            //            express_names.remove(fj);
            //            return_symbols.remove(fj);
            nr_of_paths--;
            fj--; // ? - how it works?
          }
        }
      }
    }
  }

  void generateConstants(PrintWriter pw, String name)
                  throws SdaiException {
    int i = 0;
    int i2 = 2;
    String el_str;

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      switch (express_type) {
        case XT_ENTITY:
          el_str = " entity";
          break;

        case XT_ENUMERATION:
          el_str = " enumeration";
          break;

        case XT_ARRAY:
          el_str = " array";
          break;

        case XT_BAG:
          el_str = " bag";
          break;

        case XT_LIST:
          el_str = " list";
          break;

        case XT_SET:
          el_str = " set";
          break;

        case XT_INTEGER:
          el_str = " integer";
          break;

        case XT_NUMBER:
          el_str = " number";
          break;

        case XT_REAL:
          el_str = " real";
          break;

        case XT_STRING:
          el_str = " string";
          break;

        case XT_LOGICAL:
          el_str = " logical";
          break;

        case XT_BOOLEAN:
          el_str = " boolean";
          break;

        case XT_BINARY:
          el_str = " binary";
          break;

        case XT_AGGREGATE:
          el_str = " aggregate";
          break;

        default:
          el_str = " unknown";
          break;
      }

      Vector path = ( Vector )paths.elementAt(si);
      String str_path = "\tint s" + normalize(name);
      int node_count = 1;

//case 11      if ((express_type == XT_ENTITY) && (path.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //         pw.println("// direct Entity: " + el_str);
      } else {
        for (int sj = 0; sj < path.size(); sj++) {

          //          String str_node = (String)((EDefined_type)path.elementAt(sj)).getName(null);
          String str_node = ( String )(( ENamed_type )path.elementAt(sj)).getName(
                null);
          str_path += normalize(str_node);
        }

        //        pw.println(str_path + " = " + (i2++) + "; // " + el_str);
        pw.println(str_path + " = " + (i2++) + ";");

        //      }
      }

      i++;
    }

    //    pw.println("");
  }

  void generateDefinedTypeGetMethodDeclarations(ESelect_type st, 
                                                PrintWriter pw, String entity_name, String name, 
                                                String method_suffix)
                                         throws SdaiException {
    int i = 0;
    int i2 = 2;

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      if (express_type != XT_ENTITY) {
        Vector path_string = ( Vector )path_strings.elementAt(i);
        String return_type = ( String )value_strings.elementAt(i);
        int ii = 0;
        String str_path = "\t" + return_type + " get" + normalize(name) + "(" + entity_name + " type";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException; // case " + i2++);
      } //      if (express_type != XT_ENTITY)


      i++;
    } // for si

  } // method



  String normalize(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }

  void generateGetMethodDeclarations(ESelect_type st, 
                                     PrintWriter pw, String entity_name, String name, 
                                     String method_suffix, int select_type)
                              throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tEEntity get" + normalize(name) + "(" + entity_name + " type) throws SdaiException; // case 1");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = "\t" + return_type + " get" + normalize(name) + "(" + entity_name + " type";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException; // case " + i2++);

        //      } //      if (express_type != XT_ENTITY)
      }

      i++;
    } // for si

  } // method



  void generateSetMethodDeclarations(ESelect_type st, 
                                     PrintWriter pw, String entity_name, String name, 
                                     String method_suffix, int select_type)
                              throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tvoid set" + normalize(name) + "(" + entity_name + " type, EEntity value) throws SdaiException; // case 1");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);
      String str_path;

//case 11      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;

        if (express_type == XT_AGGREGATE)
          str_path = "\t" + return_type + " create" + normalize(name) + "(" + entity_name + " type";
        else
          str_path = "\tvoid set" + normalize(name) + "(" + entity_name + " type, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException; // case " + i2++);

        //      } //      if (express_type != XT_ENTITY)
      }

      i++;
    } // for si

  } // method



  void generateAggregateConstants(PrintWriter pw)
                           throws SdaiException {
    int i = 0;
    int i2 = 2;
    String el_str;

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      switch (express_type) {
        case XT_ENTITY:
          el_str = " entity";
          break;

        case XT_ENUMERATION:
          el_str = " enumeration";
          break;

        case XT_ARRAY:
          el_str = " array";
          break;

        case XT_BAG:
          el_str = " bag";
          break;

        case XT_LIST:
          el_str = " list";
          break;

        case XT_SET:
          el_str = " set";
          break;

        case XT_INTEGER:
          el_str = " integer";
          break;

        case XT_NUMBER:
          el_str = " number";
          break;

        case XT_REAL:
          el_str = " real";
          break;

        case XT_STRING:
          el_str = " string";
          break;

        case XT_LOGICAL:
          el_str = " logical";
          break;

        case XT_BOOLEAN:
          el_str = " boolean";
          break;

        case XT_BINARY:
          el_str = " binary";
          break;

        case XT_AGGREGATE:
          el_str = " aggregate";
          break;

        default:
          el_str = " unknown";
          break;
      }

      Vector path = ( Vector )paths.elementAt(si);

      //        String str_path = "\tint s" + normalize(name);
      String str_path = "\tpublic static final int s";
      int node_count = 1;

      if ((express_type == XT_ENTITY) && (path.size() == 0)) {

        //         pw.println("// direct Entity: " + el_str);
      } else {
        for (int sj = 0; sj < path.size(); sj++) {

          //          String str_node = (String)((EDefined_type)path.elementAt(sj)).getName(null);
          String str_node = ( String )(( ENamed_type )path.elementAt(sj)).getName(
                null);
          str_path += normalize(str_node);
        }

        //        pw.println(str_path + " = " + (i2++) + "; // " + el_str);
        pw.println(str_path + " = " + (i2++) + ";");

        //      }
      }

      i++;
    }

    //    pw.println("");
  }

  void generateAggregateMethods(PrintWriter pw)
                         throws SdaiException {
  }

  void generateTest(PrintWriter pw) {
    pw.println("\tpublic int testByIndex(int index) throws SdaiException {");
    pw.println("\t\treturn pTestByIndex(index);");
    pw.println("\t}");
    pw.println("");
    pw.println("\tpublic int testCurrentMember(SdaiIterator iter) throws SdaiException {");
    pw.println("\t\treturn pTestCurrentMember(iter);");
    pw.println("\t}");
    pw.println("");
  }

  //----------------isMember---------------------------------------------------------------------
  void generateIsMember(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 - present? - may be old-style implementation, though
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic boolean isMember(EEntity value) throws SdaiException { // case 1");
//        pw.println("\t\treturn super.isMember(value, null);");
//     pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = "\tpublic boolean isMember(" + return_type + " value";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);
        pw.println("\t\treturn pIsMember(value, " + i2++ + ");");
        pw.println("\t}");

        //      } //      if (express_type != XT_ENTITY)
      }

      i++;
    } // for si

  } // method


  void generateGetByIndex(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - begin (so, case 1 was not generated, but case 11 - was generated ?!
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic EEntity getByIndex(int index, EEntity node1) throws SdaiException { // case 1");
//        pw.println("\t\treturn (EEntity)getByIndexObject(index);");
//     pw.println("\t}");
//    }
    // was commented out - end 
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = "\tpublic " + return_type + " getByIndex(int index";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        switch (express_type) {
          case XT_INTEGER:
          case XT_LOGICAL:
          case XT_ENUMERATION:
            pw.println("\t\treturn pGetByIndexInt(index, " + i2++ + ");");
            break;

          case XT_NUMBER:
          case XT_REAL:
            pw.println("\t\treturn pGetByIndexDouble(index, " + i2++ + ");");
            break;

          case XT_BOOLEAN:
            pw.println("\t\treturn pGetByIndexBoolean(index, " + i2++ + ");");
            break;

          case XT_ENTITY: 
            if (replace_object_by_entity) {
	            pw.println("\t\treturn (" + return_type + ")pGetByIndexEntity(index, " + i2++ + ");");
            } else {
	            pw.println("\t\treturn (" + return_type + ")pGetByIndexObject(index, " + i2++ + ");");
            }
          default:

            // if entity, String, binary.
            // NEW: entity now moved separately in order to use an optimized method
            pw.println("\t\treturn (" + return_type + ")pGetByIndexObject(index, " + i2++ + ");");
            break;
        }

        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateGetCurrentMember(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 present? - the implementation may be out of date, though
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic EEntity getCurrentMember(SdaiIterator iter, EEntity node1) throws SdaiException { // case 1");
//        pw.println("\t\treturn (EEntity)getCurrentMemberObject(iter);");
//     pw.println("\t}");
//   }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = "\tpublic " + return_type + " getCurrentMember(SdaiIterator iter";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        switch (express_type) {
          case XT_INTEGER:
          case XT_LOGICAL:
          case XT_ENUMERATION:
            pw.println("\t\treturn pGetCurrentMemberInt(iter, " + i2++ + ");");
            break;

          case XT_NUMBER:
          case XT_REAL:
            pw.println("\t\treturn pGetCurrentMemberDouble(iter, " + i2++ + ");");
            break;

          case XT_BOOLEAN:
            pw.println("\t\treturn pGetCurrentMemberBoolean(iter, " + i2++ + ");");
            break;

          default:

            // if entity, String, binary.
            pw.println("\t\treturn (" + return_type + ")pGetCurrentMemberObject(iter, " + i2++ + ");");
            break;
        }

        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateSetByIndex(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 present??
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic void setByIndex(int index, EEntity value) throws SdaiException { // case 1");
//      pw.println("\t\tsuper.setByIndex(index, value, null);");
//     pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " createAggregateByIndex(int index";
        else
          str_path = "\tpublic void setByIndex(int index, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        if (express_type == XT_AGGREGATE)
          pw.println("\t\treturn (" + return_type + ")pCreateAggregateByIndex(index, " + i2++ + ");");
        else
          pw.println("\t\tpSetByIndex(index, value, " + i2++ + ");");

        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateSetCurrentMember(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 present
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic void setCurrentMember(SdaiIterator iter, EEntity value) throws SdaiException { // case 1");
//      pw.println("\t\tsuper.setCurrentMember(iter, value, null);");
//     pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " createAggregateAsCurrentMember(SdaiIterator iter";
        else
          str_path = "\tpublic void setCurrentMember(SdaiIterator iter, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        if (express_type == XT_AGGREGATE)
          pw.println("\t\treturn (" + return_type + ")pCreateAggregateAsCurrentMember(iter, " + i2++ + ");");
        else
          pw.println("\t\tpSetCurrentMember(iter, value, " + i2++ + ");");

        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateAddBefore(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 present ??
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic void addBefore(SdaiIterator iter, EEntity value) throws SdaiException { // case 1");
//       pw.println("\t\tsuper.addBefore(iter, value, null);");
//    pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " createAggregateBefore(SdaiIterator iter";
        else
          str_path = "\tpublic void addBefore(SdaiIterator iter, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        if (express_type == XT_AGGREGATE)
          pw.println("\t\treturn (" + return_type + ")pCreateAggregateBefore(iter, " + i2++ + ");");
        else
          pw.println("\t\tpAddBefore(iter, value, " + i2++ + ");");

        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateAddAfter(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 present??
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic void addAfter(SdaiIterator iter, EEntity value) throws SdaiException { // case 1");
//       pw.println("\t\tsuper.addAfter(iter, value, null);");
//    pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " createAggregateAfter(SdaiIterator iter";
        else
          str_path = "\tpublic void addAfter(SdaiIterator iter, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        if (express_type == XT_AGGREGATE)
          pw.println("\t\treturn (" + return_type + ")pCreateAggregateAfter(iter, " + i2++ + ");");
        else
          pw.println("\t\tpAddAfter(iter, value, " + i2++ + ");");

        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateAddUnordered(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 - present??
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic void addUnordered(EEntity value) throws SdaiException { // case 1");
//       pw.println("\t\tsuper.addUnordered(value, null);");
//    pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " createAggregateUnordered(";
        else
          str_path = "\tpublic void addUnordered(" + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);

          if ((sj == 0) && (express_type == XT_AGGREGATE))
            str_path += the_node + " node" + node_count++;
          else
            str_path += ", " + the_node + " node" + node_count++;

          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        if (express_type == XT_AGGREGATE)
          pw.println("\t\treturn (" + return_type + ")pCreateAggregateUnordered(" + i2++ + ");");
        else
          pw.println("\t\tpAddUnordered(value, " + i2++ + ");");

        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateRemoveUnordered(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 present      
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic void removeUnordered(EEntity value) throws SdaiException { // case 1");
//       pw.println("\t\tsuper.removeUnordered(value, null);");
//    pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path;
        str_path = "\tpublic void removeUnordered(" + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);
        pw.println("\t\tpRemoveUnordered(value, " + i2++ + ");");
        pw.println("\t}");
      }

      i++;
    } // for si

  }

  void generateAddByIndex(PrintWriter pw) {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);

    // was commented out - case 1 absent, case 11 present??
//    if ((select_type == 0) || (select_type == 2)){ // includes direct entities
//    pw.println("\tpublic void addByIndex(int index, EEntity value) throws SdaiException { // case 1");
//       pw.println("\t\tsuper.addByIndex(index, value, null);");
//    pw.println("\t}");
//    }
    // was commented out - end
    
    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " addAggregateByIndex(int index";
        else
          str_path = "\tpublic void addByIndex(int index, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        if (express_type == XT_AGGREGATE)
          pw.println("\t\treturn (" + return_type + ")pAddAggregateByIndex(index, " + i2++ + ");");
        else
          pw.println("\t\tpAddByIndex(index, value, " + i2++ + ");");

        pw.println("\t}");
      }

      i++;
    } // for si

  }



  // methods for select attributes in entity classes
  void generateGetMethods(ESelect_type st, PrintWriter pw, 
                          String entity_name, String name, String method_suffix, int select_type, 
                          String owning_entity_name, String attr_internal_name)
                   throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic EEntity get" + normalize(name) + "(" + owning_entity_name + " type) throws SdaiException { // case 1");
//O      pw.println("\t\t" + attr_internal_name + " = get_instance_select(" + attr_internal_name + ");");
//0      pw.println("\t\treturn (EEntity)" + attr_internal_name + ";");
      pw.println("\t\treturn get_instance_select(" + attr_internal_name + ");");
      pw.println("\t}");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = "\tpublic " + return_type + " get" + normalize(name) + "(" + owning_entity_name + " type";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        //      } //      if (express_type != XT_ENTITY)
        String at_name = attr_internal_name;

        switch (express_type) {
          case XT_ENTITY:
//O            pw.println("\t\t" + attr_internal_name + " = get_instance_select(" + attr_internal_name + ");");
//O            pw.println("\t\treturn (EEntity)" + attr_internal_name + ";");
			      pw.println("\t\treturn get_instance_select(" + attr_internal_name + ");");
            break;

          case XT_INTEGER:
            pw.println("\t\treturn get_integer_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_ENUMERATION:
            pw.println("\t\treturn get_enumeration_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_LOGICAL:
            pw.println("\t\treturn get_logical_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_BOOLEAN:
            pw.println("\t\treturn get_boolean_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_NUMBER:
          case XT_REAL:
            pw.println("\t\treturn get_double_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_STRING:
            pw.println("\t\treturn get_string_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_BINARY:
            pw.println("\t\treturn get_binary_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_AGGREGATE:
          case XT_ARRAY:
          case XT_BAG:
          case XT_LIST:
          case XT_SET:
            pw.println("\t\treturn (" + return_type + ")get_aggregate_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          default:
            pw.println("\t\treturn get_something_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;
        }

        i2++;
        pw.println("\t}");
      }

      i++;
    } // for si

  } // method





  // methods for select attributes in entity classes
  void generateGetMethodsRenamed(ESelect_type st, PrintWriter pw, 
                          String entity_name, String name, String method_suffix, int select_type, 
                          String owning_entity_name, String attr_internal_name,
                          String method_suffix_prev,
                          String owning_entity_name_prev)
                   throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic EEntity get" + normalize(name) + "(" + owning_entity_name + " type) throws SdaiException { // case 1");
//      pw.println("\t\t" + attr_internal_name + " = get_instance_select(" + attr_internal_name + ");");
//      pw.println("\t\treturn (EEntity)" + attr_internal_name + ";");
      pw.println("\t\treturn get" + method_suffix_prev + "((" + owning_entity_name_prev + ")null);");
      pw.println("\t}");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = "\tpublic " + return_type + " get" + normalize(name) + "(" + owning_entity_name + " type";
        String str_path2 = "\t\treturn get" + method_suffix_prev + "((" + owning_entity_name_prev + ")null";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          str_path2 += ", (" + the_node + ")null";
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);
        pw.println(str_path2 + ");");

        //      } //      if (express_type != XT_ENTITY)
        String at_name = attr_internal_name;

        switch (express_type) {
          case XT_ENTITY:
//            pw.println("\t\t" + attr_internal_name + " = get_instance_select(" + attr_internal_name + ");");
//            pw.println("\t\treturn (EEntity)" + attr_internal_name + ";");
            break;

          case XT_INTEGER:
//            pw.println("\t\treturn get_integer_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_ENUMERATION:
//            pw.println("\t\treturn get_enumeration_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_LOGICAL:
//            pw.println("\t\treturn get_logical_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_BOOLEAN:
//            pw.println("\t\treturn get_boolean_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_NUMBER:
          case XT_REAL:
//            pw.println("\t\treturn get_double_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_STRING:
//            pw.println("\t\treturn get_string_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_BINARY:
//            pw.println("\t\treturn get_binary_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          case XT_AGGREGATE:
          case XT_ARRAY:
          case XT_BAG:
          case XT_LIST:
          case XT_SET:
//            pw.println("\t\treturn (" + return_type + ")get_aggregate_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;

          default:
//            pw.println("\t\treturn get_something_select(" + attr_internal_name + ", " + attr_internal_name + "$$, " + i2 + ");");
            break;
        }

        i2++;
        pw.println("\t}");
      }

      i++;
    } // for si

  } // method








  void generateSetMethodsRenamed(ESelect_type st, PrintWriter pw, 
                          String entity_name, String name, String method_suffix, int select_type, 
                          String owning_entity_name, String attr_internal_name,
                          String method_suffix_prev,
                          String owning_entity_name_prev)
                   throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic void set" + normalize(name) + "(" + owning_entity_name + " type, EEntity value) throws SdaiException { // case 1");
//      pw.println("\t\t" + attr_internal_name + " = set_instance(" + attr_internal_name + ", value);");
      pw.println("\t\tset" + method_suffix_prev + "((" + owning_entity_name_prev + ")null, value);");
      pw.println("\t}");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);
      String str_path;
			String str_path2;
//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;

        if (express_type == XT_AGGREGATE) {
          str_path = "\tpublic " + return_type + " create" + normalize(name) + "(" + owning_entity_name + " type";
          str_path2 = "\t\treturn create" + method_suffix_prev + "((" + owning_entity_name_prev + ")null";
        } else {
          str_path = "\tpublic void set" + normalize(name) + "(" + owning_entity_name + " type, " + return_type + " value";
          str_path2 = "\t\tset" + method_suffix_prev + "((" + owning_entity_name_prev + ")null, value";
				}
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          str_path2 += ", (" + the_node + ")null";
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);
        pw.println(str_path2 + ");");
	

        //      } //      if (express_type != XT_ENTITY)
        String at_name = attr_internal_name;

        switch (express_type) {
          case XT_ENTITY:
//            pw.println("\t\t" + attr_internal_name + " = set_instance(" + attr_internal_name + ", value);");
            break;

          case XT_INTEGER:
//            pw.println("\t\t" + at_name + " = set_integer_select(value);");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_ENUMERATION:
//            pw.println("\t\t" + at_name + " = set_enumeration_select(value, " + at_name + "$);");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_LOGICAL:
//            pw.println("\t\t" + at_name + " = set_logical_select(value);");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_BOOLEAN:
//            pw.println("\t\t" + at_name + " = set_boolean_select(value);");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_NUMBER:
          case XT_REAL:
//            pw.println("\t\t" + at_name + " = set_double_select(value);");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_STRING:
//            pw.println("\t\t" + at_name + " = set_string(value);");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_BINARY:
//            pw.println("\t\t" + at_name + " = set_binary(value);");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_AGGREGATE:
          case XT_ARRAY:
          case XT_BAG:
          case XT_LIST:
          case XT_SET:
            int aggr_base = (( Integer )aggregate_bases.elementAt(i)).intValue();

            switch (aggr_base) {
              case AB_INSTANCE:

                //          pw.println("\t\t"+ at_name + " = (" + aggr_str + ")create_aggregate_instances("+ at_name + ", " + at_name + "$, " + aggr_str + ".class, 0);");
                //                pw.println("\t\t"+ at_name + " = create_aggregate_instances((AEntity)"+ at_name + ", " + at_name + "$, " +  return_package + return_type   + ".class, 0);");
                //                pw.println("\t\t"+ at_name + " = create_aggregate_class((AEntity)"+ at_name + ", " + at_name + "$, " +  return_type   + ".class, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\t" + at_name + " = create_aggregate_class(" + at_name + ", " + at_name + "$, " + return_type + ".class, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_INTEGER:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_integer("+ at_name + ", " + at_name + "$, 0);");
//                pw.println("\t\t" + at_name + " = create_aggregate_integer((A_integer)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_ENUMERATION:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_integer("+ at_name + ", " + at_name + "$, 0);");
//                pw.println("\t\t" + at_name + " = create_aggregate_enumeration((A_enumeration)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_BOOLEAN:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_integer("+ at_name + ", " + at_name + "$, 0);");
//                pw.println("\t\t" + at_name + " = create_aggregate_boolean((A_boolean)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_DOUBLE:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_double("+ at_name + ", " + at_name + "$, 0);");
//                pw.println("\t\t" + at_name + " = create_aggregate_double((A_double)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_STRING:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_string("+ at_name + ", " + at_name + "$, 0);");
//                pw.println("\t\t" + at_name + " = create_aggregate_string((A_string)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_BINARY:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_binary("+ at_name + ", " + at_name + "$, 0);");
//                pw.println("\t\t" + at_name + " = create_aggregate_binary((A_binary)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              //              case AB_NOT_AGGREGATE:
              //              case AB_ENTITY_SELECT:
              //              case AB_MIXED_SELECT:
              //              case AB_DT_SELECT:
              //              case AB_SELECT_ERROR: // in fact, it is an internal error.
              default:
//                pw.println("\t\t" + at_name + " = create_aggregate_select((AEntity)" + at_name + ", " + at_name + "$, " + return_type + ".class, " + at_name + "$$ = " + i2 + ", 0);");
//                pw.println("\t\t" + at_name + " = create_aggregate_select(" + at_name + ", " + at_name + "$, " + return_type + ".class, " + at_name + "$$ = " + i2 + ", 0);");
//                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;
            } // switch - aggregate type

            break;

          default:
//            pw.println("\t\t\treturn set_something_select(" + at_name + ", " + at_name + "$$, " + i2 + ");");
//            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;
        } // switch attribute type

        i2++;
        pw.println("\t}");
      }

      i++;
    } // for si

  } // method









  void generateSetMethods(ESelect_type st, PrintWriter pw, 
                          String entity_name, String name, String method_suffix, int select_type, 
                          String owning_entity_name, String attr_internal_name)
                   throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic void set" + normalize(name) + "(" + owning_entity_name + " type, EEntity value) throws SdaiException { // case 1");
      pw.println("\t\t" + attr_internal_name + " = set_instance(" + attr_internal_name + ", value);");
      pw.println("\t}");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);
      String str_path;

//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " create" + normalize(name) + "(" + owning_entity_name + " type";
        else
          str_path = "\tpublic void set" + normalize(name) + "(" + owning_entity_name + " type, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2);

        //      } //      if (express_type != XT_ENTITY)
        String at_name = attr_internal_name;

        switch (express_type) {
          case XT_ENTITY:
            pw.println("\t\t" + attr_internal_name + " = set_instance(" + attr_internal_name + ", value);");
            break;

          case XT_INTEGER:
            pw.println("\t\t" + at_name + " = set_integer_select(value);");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_ENUMERATION:
            pw.println("\t\t" + at_name + " = set_enumeration_select(value, " + at_name + "$);");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_LOGICAL:
            pw.println("\t\t" + at_name + " = set_logical_select(value);");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_BOOLEAN:
            pw.println("\t\t" + at_name + " = set_boolean_select(value);");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_NUMBER:
          case XT_REAL:
            pw.println("\t\t" + at_name + " = set_double_select(value);");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_STRING:
            pw.println("\t\t" + at_name + " = set_string(value);");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_BINARY:
            pw.println("\t\t" + at_name + " = set_binary(value);");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;

          case XT_AGGREGATE:
          case XT_ARRAY:
          case XT_BAG:
          case XT_LIST:
          case XT_SET:
            int aggr_base = (( Integer )aggregate_bases.elementAt(i)).intValue();

            switch (aggr_base) {
              case AB_INSTANCE:

                //          pw.println("\t\t"+ at_name + " = (" + aggr_str + ")create_aggregate_instances("+ at_name + ", " + at_name + "$, " + aggr_str + ".class, 0);");
                //                pw.println("\t\t"+ at_name + " = create_aggregate_instances((AEntity)"+ at_name + ", " + at_name + "$, " +  return_package + return_type   + ".class, 0);");
                //                pw.println("\t\t"+ at_name + " = create_aggregate_class((AEntity)"+ at_name + ", " + at_name + "$, " +  return_type   + ".class, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\t" + at_name + " = create_aggregate_class(" + at_name + ", " + at_name + "$, " + return_type + ".class, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_INTEGER:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_integer("+ at_name + ", " + at_name + "$, 0);");
                pw.println("\t\t" + at_name + " = create_aggregate_integer((A_integer)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_ENUMERATION:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_integer("+ at_name + ", " + at_name + "$, 0);");
                pw.println("\t\t" + at_name + " = create_aggregate_enumeration((A_enumeration)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_BOOLEAN:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_integer("+ at_name + ", " + at_name + "$, 0);");
                pw.println("\t\t" + at_name + " = create_aggregate_boolean((A_boolean)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_DOUBLE:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_double("+ at_name + ", " + at_name + "$, 0);");
                pw.println("\t\t" + at_name + " = create_aggregate_double((A_double)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_STRING:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_string("+ at_name + ", " + at_name + "$, 0);");
                pw.println("\t\t" + at_name + " = create_aggregate_string((A_string)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              case AB_BINARY:

                //          pw.println("\t\t"+ at_name + " = create_aggregate_binary("+ at_name + ", " + at_name + "$, 0);");
                pw.println("\t\t" + at_name + " = create_aggregate_binary((A_binary)" + at_name + ", " + at_name + "$, " + at_name + "$$ = " + i2 + ");");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;

              //              case AB_NOT_AGGREGATE:
              //              case AB_ENTITY_SELECT:
              //              case AB_MIXED_SELECT:
              //              case AB_DT_SELECT:
              //              case AB_SELECT_ERROR: // in fact, it is an internal error.
              default:
//                pw.println("\t\t" + at_name + " = create_aggregate_select((AEntity)" + at_name + ", " + at_name + "$, " + return_type + ".class, " + at_name + "$$ = " + i2 + ", 0);");
                pw.println("\t\t" + at_name + " = create_aggregate_select(" + at_name + ", " + at_name + "$, " + return_type + ".class, " + at_name + "$$ = " + i2 + ", 0);");
                pw.println("\t\treturn (" + return_type + ")" + at_name + ";");
                break;
            } // switch - aggregate type

            break;

          default:
            pw.println("\t\t\treturn set_something_select(" + at_name + ", " + at_name + "$$, " + i2 + ");");
            pw.println("\t\t" + at_name + "$$ = " + (i2) + ";");
            break;
        } // switch attribute type

        i2++;
        pw.println("\t}");
      }

      i++;
    } // for si

  } // method



  // derived attributes
  void generateGetDerivedInterfaces(ESelect_type st, 
                                    PrintWriter pw, String entity_name, String name, 
                                    String method_suffix, int select_type, 
                                    String owning_entity_name)
                             throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic EEntity get" + normalize(name) + "(" + owning_entity_name + " type) throws SdaiException; // case 1");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = null;
        if (express_type == XT_BOOLEAN) {
//					str_path = "\tpublic int get" + normalize(name) + "(" + owning_entity_name + " type";
					str_path = "\tpublic int get" + normalize(name) + "(" + owning_entity_name + " type, SdaiContext _context";
				} else {
//					str_path = "\tpublic " + return_type + " get" + normalize(name) + "(" + owning_entity_name + " type";
					str_path = "\tpublic " + return_type + " get" + normalize(name) + "(" + owning_entity_name + " type, SdaiContext _context";
 		    }
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException; // case " + i2++);
      }

      i++;
    } // for si

  } // method



  void generateGetDerivedMethods(EAttribute attr, 
                                 ESelect_type st, PrintWriter pw, 
                                 String entity_name, String name, String method_suffix, 
                                 int select_type, String owning_entity_name, 
                                 String attr_internal_name, A_string expression_java, 
                                 boolean flag_expressions, 
                                 EGeneric_schema_definition __sd, 
                                 EEntity_definition __ed, 
                                 SdaiModel a_model, JavaBackend jb)
                          throws SdaiException {
    int i = 0;
    int i2 = 2;
		boolean value_method_generated = false;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic EEntity get" + normalize(name) + "(" + owning_entity_name + " type) throws SdaiException { // case 1");

      //         if ((flag_expressions) & (expression_java != null)) {
      if (flag_expressions) {
//				pw.println("\t\t\tSdaiContext _context = SdaiSession.getSdaiContext();");
//      pw.println("\t\t\tSdaiContext _context = SdaiSession.getSdaiContext();");          
				pw.println("\t\tSdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();");
//        pw.println("\t\treturn (EEntity) get" +method_suffix +"(null, _context).getActualType();" );
//        pw.println("\t\treturn (EEntity) get" +method_suffix +"(null, _context).getInstance();" );
        pw.println("\t\treturn (EEntity) get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getInstance();" );
      } else {
        pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
      }
      pw.println("\t}");


/*
      pw.println("\tpublic EEntity get" + normalize(name) + "(" + owning_entity_name + " type, SdaiContext _context) throws SdaiException { // case 1");

      //         if ((flag_expressions) & (expression_java != null)) {
      if (flag_expressions) {
//				pw.println("\t\t\tSdaiContext _context = SdaiSession.getSdaiContext();");
//      pw.println("\t\t\tSdaiContext _context = SdaiSession.getSdaiContext();");          
//				pw.println("\t\tSdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();");
//        pw.println("\t\treturn (EEntity) get" +method_suffix +"(null, _context).getActualType();" );
        pw.println("\t\treturn (EEntity) get" +method_suffix +"(null, _context).getInstance();" );
      } else {
        pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
      }
      pw.println("\t}");
*/


			pw.println("\tpublic Value get" + method_suffix + "(" + owning_entity_name + " type, SdaiContext _context) throws " + "SdaiException {");
			if (flag_expressions) {
//					JavaBackend.generateJavaExpression(pw, attr, __sd,__ed, a_model);

//TODO temporarily replaced by exception, will have to look why it is not working, as well as why it is needed at all, seems may be not needed.
		        jb.generateJavaExpressionForDeriveMethodsInc(pw, attr, __sd,__ed, a_model);
//					pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL); // B");
			} else {
					pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
			}
			pw.println("\t}");
    	value_method_generated = true;
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

// pw.println("OXO index: " + si + ", return type: " + return_type);

//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = null;
  			String str_path_no_context = null;
        if (express_type == XT_BOOLEAN) {
// 2005-04-25 tmp removing sdai context
        	str_path = "\tpublic int get" + normalize(name) + "(" + owning_entity_name + " type, SdaiContext _context";
//RR-2007-01-30        	str_path_no_context = "\tpublic int get" + normalize(name) + "(" + owning_entity_name + " type";
        	str_path_no_context = "\tpublic boolean get" + normalize(name) + "(" + owning_entity_name + " type";
        } else {
// 2005-04-25 tmp removing sdai context
        	str_path = "\tpublic " + return_type + " get" + normalize(name) + "(" + owning_entity_name + " type, SdaiContext _context";
        	str_path_no_context = "\tpublic " + return_type + " get" + normalize(name) + "(" + owning_entity_name + " type";
        }
        int node_count = 1;

				String path_nodes = "";

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
//          str_path += ", " + the_node + " node" + node_count++;
          path_nodes += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj

				str_path += path_nodes;
				str_path_no_context += path_nodes;
				String throws_case = ") throws SdaiException { // case " + i2++;


				// a non-implemented temporary version for no-context case
        pw.println(str_path_no_context + throws_case);
        pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
        pw.println("\t}");
        											
        											
//        pw.println(str_path + ") throws SdaiException { // case " + i2++);
        pw.println(str_path + throws_case);

        //                  if ((flag_expressions) & (expression_java != null)) {
        if (flag_expressions) {
						//					pw.println("\t\t\tSdaiContext _context = new SdaiContext(this);");
//					pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getActualType();" );

//_____________________________________start


        switch (express_type) {
          case XT_ENTITY:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getInstance();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getInstance();" );
            break;

          case XT_INTEGER:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getInteger();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getInteger();" );
            break;

          case XT_ENUMERATION:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getInteger();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getInteger();" );
            break;

          case XT_LOGICAL:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getLogical();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getLogical();" );
            break;

          case XT_BOOLEAN:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getBoolean();" );
//						pw.println("\t\treturn (int) get" +method_suffix +"(null, _context).getBoolean();" );
						pw.println("\t\treturn (int) get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getBoolean();" );
            break;

          case XT_NUMBER:
          case XT_REAL:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getDouble();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getDouble();" );
            break;

          case XT_STRING:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getString();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getString();" );
            break;

          case XT_BINARY:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getBinary();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getBinary();" );
            break;

          case XT_AGGREGATE:
          case XT_ARRAY:
          case XT_BAG:
          case XT_LIST:
          case XT_SET:
            int aggr_base = (( Integer )aggregate_bases.elementAt(i)).intValue();

            switch (aggr_base) {
              case AB_INSTANCE:
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getInstanceAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getInstanceAggregate(this);" );
                break;

              case AB_INTEGER:
								// should count aggregate depth, may be needed for example, getInteger2Aggregate(this)
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getIntegerAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getIntegerAggregate(this);" );
                break;

              case AB_ENUMERATION:
								// guessing here, the corresponding non-select method is just generateJavaExpression() - very different
								// may be wrong there, better to test.
								// also the usual possible implementation extension for nested aggregates
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getIntegerAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getIntegerAggregate(this);" );
                break;

              case AB_BOOLEAN:
								// may be also needed getBoolean2Aggregate(this) etc - calculate depth
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getBooleanAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getBooleanAggregate(this);" );
                break;

              // AB_LOGICAL does not exist - why not?
              /*
              // added, is not included in other methods - why not?
              case AB_LOGICAL:
								// may be also needed getLogical2Aggregate(this) etc - calculate depth
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getLogicalAggregate(this);" );
                break;
							*/
							
              case AB_DOUBLE:
								// may be needed getDouble2Aggregate(this) etc. - calculate depth
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getDoubleAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getDoubleAggregate(this);" );
		            break;

              case AB_STRING:
								// may be needed also getString2Aggregate(this) etc - calculated depth
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getStringAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getStringAggregate(this);" );
		            break;

              case AB_BINARY:
								// may be needed also getBinary2Aggregate(this) - calculated depth
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getBinaryAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getBinaryAggregate(this);" );
		            break;

              //              case AB_NOT_AGGREGATE:
              //              case AB_ENTITY_SELECT:
              //              case AB_MIXED_SELECT:
              //              case AB_DT_SELECT:
              //              case AB_SELECT_ERROR: // in fact, it is an internal error.
              default:
//								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getInstanceAggregate(this);" );
								pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getInstanceAggregate(this);" );
                break;
            } // switch - aggregate type

            break;

          default:
//						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(null, _context).getInstance();" );
						pw.println("\t\treturn (" +return_type +") get" +method_suffix +"(((" + owning_entity_name + ")null), _context).getInstance();" );
            break;
        } // switch attribute type


//------------------------------------------end

        } else {
          pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
        }
        pw.println("\t}");

				if (!value_method_generated) {	
					pw.println("\tpublic Value get" + method_suffix + "(" + owning_entity_name + " type, SdaiContext _context) throws " + "SdaiException {");
					if (flag_expressions) {

// TOFIX - temp switched off
//						pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL); // A");
// trying to switch on again:
						pw.println("\t\t\t// case A - select of aggregates etc");
//						pw.println("\t\t\t// attr: " + attr + ", __sd: " + __sd + ", __ed: " + __ed + ", a_model: " + a_model);
						JavaBackend.generateJavaExpression(pw, attr, __sd,__ed, a_model);
					} else {
						pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
					}
					pw.println("\t}");
    			value_method_generated = true;
				}

      }

      i++;
    } // for si

  } // method



  // redeclared attributes
  void generateGetLaterRedeclaredMethods(ESelect_type st, 
                                         PrintWriter pw, String entity_name, String name, 
                                         String method_suffix, int select_type, 
                                         String owning_entity_name, String attr_internal_name)
                                  throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic EEntity get" + normalize(name) + "(" + owning_entity_name + " type) throws SdaiException { // case 1");
      pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
      pw.println("\t}");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);

//case11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;
        String str_path = "\tpublic " + return_type + " get" + normalize(name) + "(" + owning_entity_name + " type";
        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2++);
        pw.println("\t\t\tthrow new SdaiException(SdaiException.FN_NAVL);");
        pw.println("\t}");
      }

      i++;
    } // for si

  } // method



  void generateSetLaterRedeclaredMethods(ESelect_type st, 
                                         PrintWriter pw, String entity_name, String name, 
                                         String method_suffix, int select_type, 
                                         String owning_entity_name, String attr_internal_name)
                                  throws SdaiException {
    int i = 0;
    int i2 = 2;

    // pw.println("select_type: " + select_type);
    if ((select_type == 0) || (select_type == 2)) { // includes direct entities
      pw.println("\tpublic void set" + normalize(name) + "(" + owning_entity_name + " type, EEntity value) throws SdaiException { // case 1");
      pw.println("\t\t\tthrow new SdaiException(SdaiException.AT_NVLD);");
      pw.println("\t}");
    }

    for (int si = 0; si < paths.size(); si++) {
      int express_type = (( Integer )express_types.elementAt(i)).intValue();

      //      if (express_type != XT_ENTITY) {
      Vector path_string = ( Vector )path_strings.elementAt(i);
      String return_type = ( String )value_strings.elementAt(i);
      String str_path;

//case 11 - sort of      if ((express_type == XT_ENTITY) && (path_string.size() == 0)) {
      if (express_type == XT_ENTITY) {

        //           pw.println("// direct Entity: " + name);
      } else {
        int ii = 0;

        if (express_type == XT_AGGREGATE)
          str_path = "\tpublic " + return_type + " create" + normalize(name) + "(" + owning_entity_name + " type";
        else
          str_path = "\tpublic void set" + normalize(name) + "(" + owning_entity_name + " type, " + return_type + " value";

        int node_count = 1;

        for (int sj = 0; sj < path_string.size(); sj++) {
          String the_node = ( String )path_string.elementAt(ii);
          str_path += ", " + the_node + " node" + node_count++;
          ii++;
        } // for sj


        pw.println(str_path + ") throws SdaiException { // case " + i2++);

        //      } //      if (express_type != XT_ENTITY)
        pw.println("\t\t\tthrow new SdaiException(SdaiException.AT_NVLD);");
        pw.println("\t}");
      }

      i++;
    } // for si

  } // method


} // class
