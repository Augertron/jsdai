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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.lang.ASdaiModel;
import jsdai.lang.Aggregate;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

public class AddShortNames {
  static String short_name_directory = "SHORT_NAMES";
  static boolean flag_verbose = false;
  static boolean flag_debug = false;
  static final int TK_START_LINE = 0;
  static final int TK_LONG = 1;
  static final int TK_SHORT = 2;
  static final int TK_COMMA = 3;

  static void printVerbose(String msg) {
    if (flag_verbose) {
      System.out.println("Express Compiler> " + msg);
    }
  }

  static void printDebug(String msg) {
    if (flag_debug) {
      System.out.println("EC DEBUG> " + msg);
    }
  }

  void runMe(SdaiRepository repo, boolean f_verbose, boolean f_debug, String short_name_dir, String short_name_file) throws SdaiException {
    flag_verbose = f_verbose;
    flag_debug = f_debug;

    if (short_name_dir != null) {
      short_name_directory = short_name_dir;
    }
    else {
      short_name_directory = "SHORT_NAMES";
    }

    System.out.println("Processing Express Short Names started");

    ASdaiModel models = repo.getModels();
    SdaiIterator iter_model = models.createIterator();

    Vector long_names = new Vector();
    Vector short_names = new Vector();
    if (short_name_file != null) {
      ReadShortNameFile(short_name_file, long_names, short_names);
    }
    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);

      if (model.getMode() == SdaiModel.NO_ACCESS) {
        model.startReadOnlyAccess();
      }

      if (model.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      if (!model.getName().equalsIgnoreCase("MIXED_COMPLEX_TYPES_DICTIONARY_DATA")) {
        addModelShortNames(model, short_name_file, long_names, short_names);
      }
    }
    System.out.println("Processing Express Short Names ended");
  }

  static void addModelShortNames(SdaiModel model, String short_name_file1, Vector long_names1, Vector short_names1) throws SdaiException {
    String model_name = model.getName();

    if (model_name.length() > 15) {
      String part_model_name = model_name.substring(0, 15);

      if (part_model_name.equalsIgnoreCase("_DOCUMENTATION_")) {
        return;
      }
    }

    if (model_name.length() > 13) {
      String part_model_name = model_name.substring(0, 13);

      if (part_model_name.equalsIgnoreCase("_EXPRESSIONS_")) {
        return;
      }
    }

    if (model_name.length() > 9) {
      String part_model_name = model_name.substring(0, 9);

      if (part_model_name.equalsIgnoreCase("_EXPRESS_")) {
        return;
      }
    }

    if (model_name.length() > 6) {
      String part_model_name = model_name.substring(0, 6);

      if (part_model_name.equalsIgnoreCase("_JAVA_")) {
        return;
      }
    }

    if (short_name_file1 == null) {
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String short_name_file2 = short_name_directory + File.separator + schema_name.toUpperCase() + ".SN";
      Vector long_names2 = new Vector();
      Vector short_names2 = new Vector();

      if (ReadShortNameFile(short_name_file2, long_names2, short_names2)) {
        short_names1 = short_names2;
        long_names1 = long_names2;
      }
    }
    if ((long_names1 != null) && (short_names1 != null)) {
      addShortNames2Model(model, short_names1, long_names1);
    }
  }

  static boolean ReadShortNameFile(String short_name_file, Vector long_names, Vector short_names) {
    try {
      FileInputStream ins = new FileInputStream(short_name_file);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.ordinaryChar(',');
      st.commentChar('#');

      int status = TK_START_LINE;

      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();

        if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_WORD)) {
          long_names.addElement(st.sval);
          status = TK_LONG;
        }
        else if ((status == TK_LONG) && (st.ttype == ',')) {
          status = TK_COMMA;
        }
        else if ((status == TK_COMMA) && (st.ttype == StreamTokenizer.TT_WORD)) {
          short_names.add(st.sval);
          status = TK_SHORT;
        }
        else if (((status == TK_SHORT) && (st.ttype == StreamTokenizer.TT_EOL)) || (st.ttype == StreamTokenizer.TT_EOF)) {
          // current complex entity reading completed. Now, generate it.
          if (st.ttype == StreamTokenizer.TT_EOF) {
            status = TK_START_LINE;

            break;
          }
          else if (st.ttype == StreamTokenizer.TT_EOL) {
            // ok, next complex entity
            status = TK_START_LINE;
          }
        }
        else if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_EOL)) {
        }
        else {
          System.out.println("ERROR in input file, line: " + st.lineno());

          break;
        }
      }
      System.out.println("Short names file: " + short_name_file + "; no. of short names: " + long_names.size());
      return true;
    } // try
    catch (IOException e) {
      System.out.println("Short names file: " + short_name_file + " not found.");

      return false;
    }

  }

  static void addShortNames2Model(SdaiModel model, Vector short_names, Vector long_names) throws SdaiException {
    Aggregate ia = model.getEntityExtentInstances(EEntity_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EEntity_definition inst = (EEntity_definition) ia.getCurrentMemberObject(iter_inst);
      String entity_name = inst.getName(null).toUpperCase();

      for (int i = 0; i < long_names.size(); i++) {
        String current_name = (String) long_names.elementAt(i);

        if (entity_name.equalsIgnoreCase(current_name)) {
          // System.out.println("#_SN_# short names - long name: " + current_name + ", index: " + i);
          String short_name = (String) short_names.elementAt(i);
          inst.setShort_name(null, short_name);
          printVerbose(entity_name + " - " + short_name + ", model: " + model.getName());

          break;
        }
      }
    }
  }
}
