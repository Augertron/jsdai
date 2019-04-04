/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2013, LKSoftWare GmbH, Germany
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

package jsdai.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.A_string;
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;
import jsdai.util.SimpleOperations;

/**
 * Application to generate a multi-frame HTML documentation for one or several EXPRESS schemas
 * out of an Express Dictionary file (*.exd), produced by the JSDAI ExpressCompiler.
 * The result is similar to a normal API references generated with Java Doc.
 * An HTML file is created for each Express schema, constant, entity, defined type,
 * function, procedure and subtype constraint.
 * Optionally these pages also contain the corresponding Java functionality
 * for the JSDAI early binding.
 */
public class ExpressDoc {

  public static interface StrOp {
    public void op(StringBuffer result) throws SdaiException;
  }

  HashSet select_loops;
  HashSet used_types;

  static boolean flag_debug = false;
  static SdaiSession globalSession = null;
  static SdaiRepository globalRepo = null;
  ArrayList globalAttributeTypes = null;
  HashSet already_done = null;
  PrintWriter pw;
  FileWriter fw;
  File f;
  static String baseDir = "";
  String schemaName = "";
  ESchema_definition schema = null;
  ASdaiModel schemas = new ASdaiModel();
  SdaiModel docModel = null;
  TreeSet index = new TreeSet(new SorterForEntities());

  static boolean flag_include_implicit = false;
  static boolean flag_print_M_message = false;
  static String global_letters = null;

  static boolean flag_only_cyclic = false;
  static boolean flag_help = false;
  static boolean bottom_level = false;
  static boolean flag_go_to_extensible_root = true;
  static boolean flag_non_incremental = false;

  HashMap hm_iso_numbers;
  HashMap hm_iso_ids;
  HashMap hm_part_names;

  Vector aggregates = new Vector();
  Vector deep_agg = new Vector();
  boolean doNotGenerateJava = false;
  String complex_title = "Index"; // default value if "complex_index" option not used
  static boolean flag_generate_summary = false;
  static boolean flag_original_expressions = true;
  static String section_title = null;
  static String iso_file = "iso_db";

  boolean askForSchema = false;
  boolean printISO_DBpath = false;

  static Vector include_models = new Vector();
  static Vector exclude_models = new Vector();

  HashMap hmUsedEntities = new HashMap(2048);
  HashMap hmExpressModelSet = new HashMap(128);
  ASdaiModel asmExpressDomain = new ASdaiModel();
  Vector vAlgorithmDefinition = new Vector();
  HashMap hm_extensible_select_users = new HashMap();
  static long start_time, finish_time, elapsed_time;

  void printDebug(String message) {
    if (flag_debug) {
      System.out.println(message);
    }
  }

  public static final void main(String args[]) throws SdaiException {
    try {
      System.out.println("");
      System.out.println("JSDAI(TM) Express Doc,        Copyright (C) 1998-2015 LKSoftWare GmbH");
      System.out.println("---------------------------------------------------------------------");

      start_time = System.currentTimeMillis();

      ExpressDoc ge = new ExpressDoc();
      SdaiSession session = SdaiSession.openSession();
      SdaiTransaction trans = session.startTransactionReadWriteAccess();
      String title = "EXPRESS Schemas", location = "";
      for (int i = 0; i < args.length; i++) {

        if (args[i].equalsIgnoreCase("-non_incremental")) {
          flag_non_incremental = true;
        }
        else
          // this switch is currently disabled, default changed to true;
          if (args[i].equalsIgnoreCase("-original_expressions")) {
            flag_original_expressions = true;
          }
          else if (args[i].equalsIgnoreCase("-formatted_expressions")) {
            flag_original_expressions = false;
          }
          else if (args[i].equalsIgnoreCase("-include_implicit")) {
            flag_include_implicit = true;
          }
          else if (args[i].equalsIgnoreCase("-print_mixed")) {
            flag_print_M_message = true;
          }
          else if (args[i].equalsIgnoreCase("-generate_summary")) {
            flag_generate_summary = true;
          }
          else if (args[i].equalsIgnoreCase("-location")) {
            i++;
            location = args[i];
          }
          else if (args[i].equalsIgnoreCase("-iso_db_path")) {
            i++;
            iso_file = args[i];
          }
          else if (args[i].equalsIgnoreCase("-output")) {
            i++;
            baseDir = args[i];
          }
          else if (args[i].equalsIgnoreCase("-title")) {
            i++;
            title = args[i];
            section_title = args[i];
          }
          else if (args[i].equalsIgnoreCase("-nojava")) {
            ge.doNotGenerateJava = true;
          }
          else if (args[i].equalsIgnoreCase("-complex_index")) {
            i++;
            ge.complex_title = args[i];
          }
          else if (args[i].equalsIgnoreCase("-complex_schema")) {
            // this is now always on 2013-08-18
          }
          else if (args[i].equalsIgnoreCase("-include")) {
            i++;
            while ((i < args.length) && (args[i].charAt(0) != '-')) {
              include_models.add(args[i]);
              i++;
            }
            i--;
          }
          else if (args[i].equalsIgnoreCase("-exclude")) {
            i++;
            while ((i < args.length) && (args[i].charAt(0) != '-')) {
              exclude_models.add(args[i]);
              i++;
            }
            i--;
          }
          else if (args[i].equalsIgnoreCase("-include_list")) {
            i++;
            if (i < args.length) {
              if (args[i].substring(0, 1).equals("-")) {
                System.out.println("A file name must follow the " + args[i - 1] + " switch");
                return;
              }
            }
            else {
              System.out.println("A file name must follow the " + args[i - 1] + " switch");
              return;
            }
            includeModelsFromListInFile(args[i]);
          }
          else if (args[i].equalsIgnoreCase("-exclude_list")) {
            i++;
            if (i < args.length) {
              if (args[i].substring(0, 1).equals("-")) {
                System.out.println("A file name must follow the " + args[i - 1] + " switch");
                return;
              }
            }
            else {
              System.out.println("A file name must follow the " + args[i - 1] + " switch");
              return;
            }
            excludeModelsFromListInFile(args[i]);
          }
          else if (args[i].equalsIgnoreCase("-add_file")) {
            i++;
            File toadd = new File(args[i]);
            if (toadd.exists()) {
              copyFile(args[i], baseDir + File.separator + toadd.getName());
              i++;
              insertLinkToFile(toadd.getName(), args[i]);
            }
            else {
              System.out.println("Warning: File " + args[i] + " was not foud.");
              i++;
            }
          }
          else if (args[i].equalsIgnoreCase("-confirm")) {
            ge.askForSchema = true;
          }
          else if (args[i].equalsIgnoreCase("-iso_db")) {
            ge.printISO_DBpath = true;
          }
      }
      if (location.equals("") || baseDir.equals("")) {
        System.out.println("Wrong parameters specified!");
        System.out.println("Program usage: java ExpressDoc options");
        System.out.println("Options:");
        System.out.println("-location xxx      : Path to an input file/repository with Express Dictionary Data (*.exd)");
        System.out.println("-output xxx        : Specifies path to output directory");
        System.out.println("-title xxx         : Title for group of schemas");
        System.out.println("-nojava            : If specified Java part of documentation will not be generated");
        System.out.println("-complex_index xxx : Generates common index for all schemas. Separate view for every first letter");
        System.out.println("-include xxx {xxx} : Which schemas will be included in documentation");
        System.out.println("-exclude xxx {xxx} : Which schemas will be excluded from documentation");
        System.out.println("-add_file xxx yyy  : Adds existing file to documentation. File is copied to root of documentation and link to it is added");
        System.out.println("-confirm           : User is asked to confirm request to generate documentation on each schema");
        return;
      }
      if (flag_non_incremental) {
        // delete everything in directory: -output = ge.baseDir
        emptyDirectory(baseDir);
      }
      // after emptyDirectory() we need to create the directory itself again, but also better create it always if it does not exist already, so that there is no requirement to provide an existing directory.
      File outdir = new File(baseDir);
      if (!outdir.exists()) {
        outdir.mkdir();
      }

      if (flag_generate_summary) {
        generateSummaryFile();
      }
      globalSession = session;
      ge.printSchemas(SimpleOperations.linkRepositoryOrName("ExpressDoc", location), title, trans);

      session.closeSession();
      finish_time = System.currentTimeMillis();
      elapsed_time = finish_time - start_time;
      System.out.println("Express Doc ended, duration: " + elapsed_time + " ms");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      SdaiSession runningSession = SdaiSession.getSession();
      if (runningSession != null) {
        if (runningSession.testActiveTransaction()) {
          runningSession.getActiveTransaction().abort();
        }
        runningSession.closeSession();
      }
    }
  }

  public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args) throws SdaiException {
    Properties jsdaiProperties = new Properties();
    jsdaiProperties.setProperty("repositories", sdaireposDirectory);
    SdaiSession.setSessionProperties(jsdaiProperties);
    return new Runnable() {
      public void run() {
        try {
          main(args);
        }
        catch (SdaiException e) {
          e.printStackTrace();
        }
      }
    };
  }

  // added by RR

  /**
   * convert all express keywords to uppercase
   * remove unnecessary spaces, such as:
   * before and after dot
   * before comma, before semicolon, etc.
   * perhaps some other formatting can be added
   * <p>
   * EXPRESS keywords:
   * <p>
   * ABSTRACT
   * AGGREGATE
   * ALIAS
   * ARRAY
   * AS
   * BAG
   * BASED_ON
   * BEGIN
   * BINARY
   * BOOLEAN
   * BY
   * CASE
   * CONSTANT
   * DERIVE
   * ELSE
   * END
   * END_ALIAS
   * END_CASE
   * END_CONSTANT
   * END_ENTITY
   * END_FUNCTION
   * END_IF
   * END_LOCAL
   * END_PROCEDURE
   * END_REPEAT
   * END_RULE
   * END_SCHEMA
   * END_SUBTYPE_CONSTRAINT
   * END_TYPE
   * ENTITY
   * ENUMERATION
   * ESCAPE
   * EXTENSIBLE
   * FIXED
   * FOR
   * FROM
   * FUNCTION
   * GENERIC
   * GENERIC_ENTITY
   * IF
   * INTEGER
   * INVERSE
   * LIST
   * LOCAL
   * LOGICAL
   * NUMBER
   * OF
   * ONEOF
   * OPTIONAL
   * OTHERWISE
   * PROCEDURE
   * QUERY
   * REAL
   * RENAMED
   * REFERENCE
   * REPEAT
   * RETURN
   * RULE
   * SCHEMA
   * SELECT
   * SET
   * SKIP
   * STRING
   * SUBTYPE
   * SUBTYPE_CONSTRAINT
   * SUPERTYPE
   * THEN
   * TO
   * TOTAL_OVER
   * TYPE
   * UNIQUE
   * UNTIL
   * USE
   * VAR
   * WHERE
   * WHILE
   * WITH
   * <p>
   * EXPRESS operators:
   * <p>
   * AND
   * ANDOR
   * DIV
   * IN
   * LIKE
   * MOD
   * NOT
   * OR
   * XOR
   * <p>
   * EXPRESS built-in constants:
   * <p>
   * SELF
   * CONST_E
   * PI
   * FALSE
   * TRUE
   * UNKNOWN
   * <p>
   * EXPRESS built-in functions:
   * <p>
   * ABS
   * ACOS
   * ASIN
   * ATAN
   * BLENGTH
   * COS
   * EXISTS
   * EXP
   * FORMAT
   * HIBOUND
   * HIINDEX
   * LENGTH
   * LOBOUND
   * LOG
   * LOG2
   * LOG10
   * LOINDEX
   * NVL
   * ODD
   * ROLESOF
   * SIN
   * SIZEOF
   * SQRT
   * TAN
   * TYPEOF
   * USEDIN
   * VALUE
   * VALUE_IN
   * VALUE_UNIQUE
   * <p>
   * EXPRESS built-in procedures:
   * <p>
   * INSERT
   * REMOVE
   */
  String format(String input) {

    String result;

    // convert keywords to uppercase, input is always normalized to lowercase, so mixed-case keywords can not happen
    // however, we have to begin from the longest strings so as to avoid converting substrings only and getting mixed-case words that are not later detected

    result = replaceAll2(input, "end_subtype_constraint", "END_SUBTYPE_CONSTRAINT");

    result = replaceAll2(result, "subtype_constraint", "SUBTYPE_CONSTRAINT");

    result = replaceAll2(result, "generic_entity", "GENERIC_ENTITY");

    result = replaceAll2(result, "end_procedure", "END_PROCEDURE");

    result = replaceAll2(result, "end_constant", "END_CONSTANT");
    result = replaceAll2(result, "end_function", "END_FUNCTION");
    result = replaceAll2(result, "value_unique", "VALUE_UNIQUE");

    result = replaceAll2(result, "enumeration", "ENUMERATION");

    result = replaceAll2(result, "end_entity", "END_ENTITY");
    result = replaceAll2(result, "end_repeat", "END_REPEAT");
    result = replaceAll2(result, "end_schema", "END_SCHEMA");
    result = replaceAll2(result, "extensible", "EXTENSIBLE");
    result = replaceAll2(result, "total_over", "TOTAL_OVER");

    result = replaceAll2(result, "aggregate", "AGGREGATE");
    result = replaceAll2(result, "end_alias", "END_ALIAS");
    result = replaceAll2(result, "end_local", "END_LOCAL");
    result = replaceAll2(result, "otherwise", "OTHERWISE");
    result = replaceAll2(result, "procedure", "PROCEDURE");
    result = replaceAll2(result, "reference", "REFERENCE");
    result = replaceAll2(result, "supertype", "SUPERTYPE");

    result = replaceAll2(result, "abstract", "ABSTRACT");
    result = replaceAll2(result, "based_on", "BASED_ON");
    result = replaceAll2(result, "constant", "CONSTANT");
    result = replaceAll2(result, "end_case", "END_CASE");
    result = replaceAll2(result, "end_rule", "END_RULE");
    result = replaceAll2(result, "end_type", "END_TYPE");
    result = replaceAll2(result, "function", "FUNCTION");
    result = replaceAll2(result, "optional", "OPTIONAL");
    result = replaceAll2(result, "value_in", "VALUE_IN");

    result = replaceAll2(result, "blength", "BLENGTH");
    result = replaceAll2(result, "boolean", "BOOLEAN");
    result = replaceAll2(result, "const_e", "CONST_E");
    result = replaceAll2(result, "generic", "GENERIC");
    result = replaceAll2(result, "hibound", "HIBOUND");
    result = replaceAll2(result, "hiindex", "HIINDEX");
    result = replaceAll2(result, "integer", "INTEGER");
    result = replaceAll2(result, "inverse", "INVERSE");
    result = replaceAll2(result, "lobound", "LOBOUND");
    result = replaceAll2(result, "logical", "LOGICAL");
    result = replaceAll2(result, "loindex", "LOINDEX");
    result = replaceAll2(result, "renamed", "RENAMED");
    result = replaceAll2(result, "rolesof", "ROLESOF");
    result = replaceAll2(result, "subtype", "SUBTYPE");
    result = replaceAll2(result, "unknown", "UNKNOWN");

    result = replaceAll2(result, "binary", "BINARY");
    result = replaceAll2(result, "derive", "DERIVE");
    result = replaceAll2(result, "end_if", "END_IF");
    result = replaceAll2(result, "entity", "ENTITY");
    result = replaceAll2(result, "escape", "ESCAPE");
    result = replaceAll2(result, "exists", "EXISTS");
    result = replaceAll2(result, "format", "FORMAT");
    result = replaceAll2(result, "insert", "INSERT");
    result = replaceAll2(result, "length", "LENGTH");
    result = replaceAll2(result, "number", "NUMBER");
    result = replaceAll2(result, "remove", "REMOVE");
    result = replaceAll2(result, "repeat", "REPEAT");
    result = replaceAll2(result, "return", "RETURN");
    result = replaceAll2(result, "schema", "SCHEMA");
    result = replaceAll2(result, "select", "SELECT");
    result = replaceAll2(result, "sizeof", "SIZEOF");
    result = replaceAll2(result, "string", "STRING");
    result = replaceAll2(result, "unique", "UNIQUE");
    result = replaceAll2(result, "typeof", "TYPEOF");
    result = replaceAll2(result, "usedin", "USEDIN");

    result = replaceAll2(result, "alias", "ALIAS");
    result = replaceAll2(result, "andor", "ANDOR");
    result = replaceAll2(result, "array", "ARRAY");
    result = replaceAll2(result, "begin", "BEGIN");
    result = replaceAll2(result, "false", "FALSE");
    result = replaceAll2(result, "fixed", "FIXED");
    result = replaceAll2(result, "local ", "LOCAL ");
    result = replaceAll2(result, "local\n", "LOCAL\n");
    result = replaceAll2(result, "local\t", "LOCAL\t");
    result = replaceAll2(result, "local\r", "LOCAL\r");
    result = replaceAll2(result, "log10", "LOG10");
    result = replaceAll2(result, "oneof", "ONEOF");
    result = replaceAll2(result, "query", "QUERY");
    result = replaceAll2(result, "until", "UNTIL");
    result = replaceAll2(result, "value", "VALUE");
    result = replaceAll2(result, "where", "WHERE");
    result = replaceAll2(result, "while", "WHILE");

    result = replaceAll2(result, "acos", "ACOS");
    result = replaceAll2(result, "asin", "ASIN");
    result = replaceAll2(result, "atan", "ATAN");
    result = replaceAll2(result, "case", "CASE");
    result = replaceAll2(result, "else", "ELSE");
    result = replaceAll2(result, "from", "FROM");
    result = replaceAll2(result, "like", "LIKE");
    result = replaceAll2(result, "list", "LIST");
    result = replaceAll2(result, "log2", "LOG2");
    result = replaceAll2(result, "real", "REAL");
    result = replaceAll2(result, "rule", "RULE");
    result = replaceAll2(result, "self", "SELF");
    result = replaceAll2(result, "skip", "SKIP");
    result = replaceAll2(result, "sqrt", "SQRT");
    result = replaceAll2(result, "then", "THEN");
    result = replaceAll2(result, "true", "TRUE");
    result = replaceAll2(result, "type", "TYPE");
    result = replaceAll2(result, "with", "WITH");

    result = replaceAll2(result, "abs", "ABS");
    result = replaceAll2(result, "and", "AND");
    result = replaceAll2(result, "bag", "BAG");
    result = replaceAll2(result, "cos", "COS");
    result = replaceAll2(result, "div", "DIV");
    result = replaceAll2(result, "end", "END");
    result = replaceAll2(result, "exp", "EXP");
    result = replaceAll2(result, "for", "FOR");
    result = replaceAll2(result, "log", "LOG");
    result = replaceAll2(result, "mod", "MOD");
    result = replaceAll2(result, "not", "NOT");
    result = replaceAll2(result, "nvl", "NVL");
    result = replaceAll2(result, "odd", "ODD");
    result = replaceAll2(result, "set", "SET");
    result = replaceAll2(result, "sin", "SIN");
    result = replaceAll2(result, "tan", "TAN");
    result = replaceAll2(result, "use", "USE");
    result = replaceAll2(result, "xor", "XOR");
    result = replaceAll2(result, "var", "VAR");

    result = replaceAll2(result, "as", "AS");
    result = replaceAll2(result, "by", "BY");
    result = replaceAll2(result, "if", "IF");
    result = replaceAll2(result, "in", "IN");
    result = replaceAll2(result, "of", "OF");
    result = replaceAll2(result, "or", "OR");
    result = replaceAll2(result, "pi", "PI");
    result = replaceAll2(result, "to", "TO");

    if (flag_original_expressions) {
      // however, needed special symbols to replace spaces, \n and \t
      result = replaceAll(result, " ", "&nbsp;");
      result = replaceAll(result, "\n", "<BR>");
      result = replaceAll(result, "\t", "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ");
    }
    else {
      result = replaceAll(result, " \\ ", "\\");
      result = replaceAll(result, " . ", ".");
      result = replaceAll(result, ". ", ".");
      result = replaceAll(result, " .", ".");
      result = replaceAll(result, " ; ", "; ");
      result = replaceAll(result, " , ", ", ");
      result = replaceAll(result, " ( ", "(");
      result = replaceAll(result, " (", "(");
      result = replaceAll(result, "( ", "(");
      result = replaceAll(result, " ) ", ")");
      result = replaceAll(result, " )", ")");
      result = replaceAll(result, ") ", ")");
    }
    return result;
  }

  // added by RR - stupid method, and stupid implementation
  AExplicit_attribute getExplicit_attributes(EEntity_definition ed) throws SdaiException {
    AExplicit_attribute axa = new AExplicit_attribute();

    CompareAttributes ce = new CompareAttributes();
    TreeSet axas = new TreeSet(ce);

    ASdaiModel domain = null;
    AAttribute aa = ed.getAttributes(null, domain);
    SdaiIterator iaa = aa.createIterator();
    while (iaa.next()) {
      EAttribute an_a = (EAttribute) aa.getCurrentMemberObject(iaa);
      if (an_a instanceof EExplicit_attribute) {
        EEntity_definition ped = (EEntity_definition) an_a.getParent(null);
        if (ped == ed) {
          if (an_a.testOrder(null)) {
            axas.add(an_a);
          }
        }
      }
    }
    Iterator iter = axas.iterator();
    while (iter.hasNext()) {
      EExplicit_attribute xa = (EExplicit_attribute) iter.next();
      axa.addByIndex(axa.getMemberCount() + 1, xa);
    }
    return axa;
  }

  // RR still stupid - includes also redeclared, and returns them in the original ordor, as in express (instead of sorted)
  static AExplicit_attribute getAllExplicit_attributes(EEntity_definition ed) throws SdaiException {
    AExplicit_attribute axa = new AExplicit_attribute();

    ArrayList axas = new ArrayList();

    ASdaiModel domain = null;
    AAttribute aa = ed.getAttributes(null, domain);
    SdaiIterator iaa = aa.createIterator();
    while (iaa.next()) {
      EAttribute an_a = (EAttribute) aa.getCurrentMemberObject(iaa);
      if (an_a instanceof EExplicit_attribute) {
        EEntity_definition ped = (EEntity_definition) an_a.getParent(null);
        if (ped == ed) {
          axas.add(an_a);
        }
      }
    }

    for (int i = axas.size() - 1; i >= 0; i--) {
      EExplicit_attribute xa = (EExplicit_attribute) axas.get(i);
      axa.addByIndex(axa.getMemberCount() + 1, xa);
    }
    return axa;
  }

  static ADerived_attribute getAllDerived_attributes(EEntity_definition ed) throws SdaiException {
    ADerived_attribute axa = new ADerived_attribute();

    ArrayList axas = new ArrayList();

    ASdaiModel domain = null;
    AAttribute aa = ed.getAttributes(null, domain);
    SdaiIterator iaa = aa.createIterator();
    while (iaa.next()) {
      EAttribute an_a = (EAttribute) aa.getCurrentMemberObject(iaa);
      if (an_a instanceof EDerived_attribute) {
        EEntity_definition ped = (EEntity_definition) an_a.getParent(null);
        if (ped == ed) {
          axas.add(an_a);
        }
      }
    }

    for (int i = axas.size() - 1; i >= 0; i--) {
      EDerived_attribute xa = (EDerived_attribute) axas.get(i);
      axa.addByIndex(axa.getMemberCount() + 1, xa);
    }
    return axa;
  }

  static AInverse_attribute getAllInverse_attributes(EEntity_definition ed) throws SdaiException {
    AInverse_attribute axa = new AInverse_attribute();

    ArrayList axas = new ArrayList();

    ASdaiModel domain = null;
    AAttribute aa = ed.getAttributes(null, domain);
    SdaiIterator iaa = aa.createIterator();
    while (iaa.next()) {
      EAttribute an_a = (EAttribute) aa.getCurrentMemberObject(iaa);
      if (an_a instanceof EInverse_attribute) {
        EEntity_definition ped = (EEntity_definition) an_a.getParent(null);
        if (ped == ed) {
          axas.add(an_a);
        }
      }
    }

    for (int i = axas.size() - 1; i >= 0; i--) {
      EInverse_attribute xa = (EInverse_attribute) axas.get(i);
      axa.addByIndex(axa.getMemberCount() + 1, xa);
    }
    return axa;
  }

  /**
   * Print all known schemas
   *
   * @param repo
   * @param schemas_title
   * @param trans
   * @throws SdaiException
   * @throws IOException
   */
  private void printSchemas(SdaiRepository repo, String schemas_title, SdaiTransaction trans) throws SdaiException, IOException {

    globalRepo = repo;
    repo.openRepository();

    File fiso = new File(iso_file);
    if (fiso.exists()) {
      if (printISO_DBpath) {
        System.out.println("iso_db file path: " + fiso.getAbsolutePath());
      }
      readIsoIdsAndPartNamesOfSchemas(iso_file);
    }
    else {
      if (printISO_DBpath) {
        System.out.println("iso_db file path - file does not exist: " + fiso.getAbsolutePath());
      }
    }

    File fros = new File(baseDir + File.separator + "overview-summary.html");
    if (!fros.exists()) {
      createOverviewSummary(baseDir + File.separator + "overview-summary.html", repo);
    }
    File fraf = new File(baseDir + File.separator + "allclasses-frame.html");
    if (!fraf.exists()) {
      createAllClasses(baseDir + File.separator + "allclasses-frame.html");
    }
    File frp = new File(baseDir + File.separator + "packages.html");
    if (!frp.exists()) {
      createPackages(baseDir + File.separator + "packages.html");
    }

    f = new File(baseDir + File.separator + "index.html");
    if (!f.exists()) {
      f.createNewFile();
      fw = new FileWriter(f);
      pw = new PrintWriter(fw, true);
      pw.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"\"http://www.w3.org/TR/REC-html40/loose.dtd>");
      pw.println("<HTML>");
      pw.println("<HEAD>");
      pw.println("<TITLE>");
      pw.println("JSDAI documentation");
      pw.println("</TITLE>");
      pw.println("</HEAD>");
      pw.println("<FRAMESET cols=\"20%,80%\">");
      pw.println("<FRAMESET rows=\"30%,70%\">");
      pw.println("<FRAME src=\"overview-frame.html\" name=\"packageListFrame\">");
      pw.println("<FRAME src=\"allclasses-frame.html\" name=\"packageFrame\">");
      pw.println("</FRAMESET>");
      pw.println("<FRAME src=\"overview-summary.html\" name=\"classFrame\">");
      pw.println("</FRAMESET>");
      pw.println("</HTML>");
      pw.close();
    }
    f = new File(baseDir + File.separator + "jsdai");
    if (!f.exists()) {
      f.mkdir();
    }
    schemas = new ASdaiModel();
    ASdaiModel models = repo.getModels();

    String justIndex = printH3(schemas_title);
    SdaiIterator iter = models.createIterator();
    TreeSet modelSet = new TreeSet(new SorterForModels());

    while (iter.next()) {
      SdaiModel model = models.getCurrentMember(iter);
      String sCurrModelName = model.getName();
// not enough to remove here
//			if (sCurrModelName.equals("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA")) continue;
//			if (sCurrModelName.equals("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA")) continue;
//			if (sCurrModelName.equals("MIXED_COMPLEX_TYPES_DICTIONARY_DATA")) continue;

      //if (findSchema(model) == null) { //--VV--
      if (sCurrModelName.indexOf("_DICTIONARY_DATA") < 0) {
        if (sCurrModelName.indexOf("_EXPRESS") >= 0) {
          hmExpressModelSet.put(sCurrModelName.toUpperCase(), model);
        }
      }
      else {
        if (((include_models.size() == 0) || haveVector(include_models, model.getName())) && (!haveVector(exclude_models, model.getName()))) {
          modelSet.add(model);
        }
      }
    }
    Iterator modelIter = modelSet.iterator();
    while (modelIter.hasNext()) {
      SdaiModel model = (SdaiModel) modelIter.next();
      if (model.getMode() == 0) {
        model.startReadOnlyAccess();
      }
      schemas.addByIndex(1, model);
    }

    modelIter = modelSet.iterator();
    int modelNum = 0;
    while (modelIter.hasNext()) {
      SdaiModel model = (SdaiModel) modelIter.next();
      if (model.getName().charAt(0) != '_') {
        docModel = findDocModel(models, model);
        if (docModel != null) {
          if (docModel.getMode() == 0) {
            docModel.startReadOnlyAccess();
          }
        }
        if (model.getMode() == 0) {
          model.startReadOnlyAccess();
        }
        ESchema_definition schema = findSchema(model); //model.getDefined_schema(); //So is in dictionary data
        this.schema = schema;
        asmExpressDomain.clear();
        if (hmExpressModelSet.containsKey(("_EXPRESS_" + schema.getName(null)).toUpperCase())) {
          SdaiModel smExpressModel = (SdaiModel) hmExpressModelSet.get(("_EXPRESS_" + schema.getName(null)).toUpperCase());
          if (smExpressModel.getMode() == 0) {
            smExpressModel.startReadOnlyAccess();
          }
          asmExpressDomain.addUnordered(smExpressModel, null); //--VV--
        }

        //if (schema.getName(null).compareTo("REPRESENTATION_SCHEMA")==0) {
        //if (schema.getName(null).compareTo("GEOMETRY_SCHEMA")==0) {
        //if (schema.getName(null).compareTo("AUTOMOTIVE_DESIGN")==0) {
        if (askForSchema) {
          System.out.print(String.valueOf(++modelNum) + " Generate documentation for: " + schema.getName(null) + "? (y/n):");
          System.out.println("");
          BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
          String s = in.readLine();
          if (s.length() >= 1) {
            if (s.charAt(0) == 'y') {
              printSchema(repo, schema);
              justIndex += "<NOBR><FONT ID=\"FrameItemFont\"><A HREF=\"jsdai/" + correctSchemaNameS(schema)
                  + "/package-frame.html\" TARGET=\"packageFrame\">" + correctSchemaName(schema) + "</A></FONT>  "
                  + printIsoNumber(schema.getName(null)) + "</NOBR>";
              justIndex += "<BR>\n";
            }
          }
        }
        else {
          printSchema(repo, schema);
          justIndex += "<NOBR><FONT ID=\"FrameItemFont\"><A HREF=\"jsdai/" + correctSchemaNameS(schema)
              + "/package-frame.html\" TARGET=\"packageFrame\">" + correctSchemaName(schema) + "</A></FONT>  "
              + printIsoNumber(schema.getName(null)) + "</NOBR>";
          justIndex += "<BR>\n";
        }
        //}
      }
    }

    // Adding additional aggregates to defined types and entities
    for (int i = 0; i < aggregates.size(); i++) {
      String partAggr = "";
      ENamed_type nt = (ENamed_type) aggregates.elementAt(i);
      if (nt instanceof EDefined_type) {
        EDefined_type dt = (EDefined_type) nt;
        EEntity domain = dt.getDomain(null);
        if (!doNotGenerateJava) {
          if (domain instanceof ESelect_type) {
            /*
             *
             * the ExpressDoc implementation seems not to recognize the difference between pure entity select and
             * mixed select types
             *
             * here is what is done in the expressCompiler:
             *
             * if ((select_type == 0) || (ut instanceof EEntity_select_type)) { // includes only entities
             * pw.println("public class " + class_name + " extends AEntitySelect {");
             * } else { // may include non-entities
             * pw.println("public class " + class_name + " extends CAggregate {");
             * }
             *
             * the calculating of the select_type itself is something that has to be implementing in ExpressDoc first
             */

            select_loops = new HashSet(); // not really needed
            int select_type = Integer.MIN_VALUE;
            int count = 1;
            int[] indeces = new int[500];
            int depth = 0;
            int depth_count = 0;
            boolean with_type = false;
            Vector current_nodes = new Vector();
            Vector current_node_strings = new Vector();
            select_type = calculateSelectPaths(count, current_nodes, current_node_strings, indeces, depth, depth_count, (ESelect_type) domain,
                with_type);

            if ((select_type == 0) || (domain instanceof EEntity_select_type)) { // includes only entities
              partAggr += printH3("public class A" + getUpper(dt.getName(null)) + " extends "
                  + printHRef("AEntitySelect", "../lang/AEntitySelect.html"));
              partAggr += printAggregateForSelect((ESelect_type) domain, new MyInteger(2), new Vector());
            }
            else {
              partAggr += printH3("public class A" + getUpper(dt.getName(null)) + " extends "
                  + printHRef("CAggregate", "../lang/CAggregate.html"));
              partAggr += printAggregateForSelect((ESelect_type) domain, new MyInteger(2), new Vector());
            }

          }
          else {
            partAggr += printH3("public class A" + getUpper(nt.getName(null)) + " extends " + printHRef("AEntity", "../lang/AEntity.html"));
            partAggr += printBreak();
          }
        }
      }
      else if (nt instanceof EEntity_definition) {
        EEntity_definition ed = (EEntity_definition) nt;
        Integer dd = (Integer) deep_agg.elementAt(i);
        String sufix = "a";
        for (int d = 1; d < dd.intValue(); d++) {
          if (!doNotGenerateJava) {
            partAggr += printH3(printName(
                "public class " + getUpper(sufix + "a") + getUpper(ed.getName(null)) + " extends "
                    + printHRef("AEntity", "../lang/AEntity.html"), sufix + getUpper(ed.getName(null))));
            partAggr += printTab("public " + getUpper(sufix) + getUpper(ed.getName(null)) + " getByIndex(int index)");
            partAggr += printTab("public " + getUpper(sufix) + getUpper(ed.getName(null)) + " getCurrentMember("
                + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " it)");
            partAggr += printBreak();
          }
          sufix += "a";
        }
      }

      ESchema_definition sd = findSchemaForEntity(nt);
      f = new File(correctSchemaNameFile(sd) + File.separator + getUpper(nt.getName(null)) + ".html");

      int size = (int) f.length();
      FileReader reader = new FileReader(f);
      char[] all = new char[size];
      reader.read(all, 0, size);
      reader.close();

      FileWriter writer = new FileWriter(f);
      int k = 0;
      int find = 0;
      while ((find != 2) && (k < size)) {
        if ((all[k] == '<') && (all[k + 1] == '/') && (all[k + 2] == 'B') && (all[k + 3] == 'O')) {
          find++;
          if (find == 1) {
            writer.write(all[k]);
            k++;
          }
        }
        else {
          writer.write(all[k]);
          k++;
        }
      }
      for (int j = 0; j < partAggr.length(); j++) {
        writer.write(partAggr.charAt(j));
      }
      while (k < size) {
        writer.write(all[k]);
        k++;
      }
      writer.close();
    }

    // adding new packages in the list
    global_letters = printComplexIndex(index, complex_title);

    File fr = new File(baseDir + File.separator + "overview-frame.html");
    if (!fr.exists()) {
      createOverviewFrame(baseDir + File.separator + "overview-frame.html");
    }
    int size = (int) fr.length();
    FileReader reader = new FileReader(fr);
    char[] all = new char[size];
    reader.read(all, 0, size);
    reader.close();

    FileWriter writer = new FileWriter(fr);
    int k = 0;
    int find = 0;
    while ((find != 2) && (k < size)) {
      if ((all[k] == '<') && (all[k + 1] == '/') && (all[k + 2] == 'T') && (all[k + 3] == 'D')) {
        find++;
        if (find == 1) {
          writer.write(all[k]);
          k++;
        }
      }
      else {
        writer.write(all[k]);
        k++;
      }
    }
    for (int j = 0; j < justIndex.length(); j++) {
      writer.write(justIndex.charAt(j));
    }
    while (k < size) {
      writer.write(all[k]);
      k++;
    }
    writer.close();

    trans.endTransactionAccessAbort();
    repo.closeRepository();
    repo.unlinkRepository();
  }

  private boolean haveVector(Vector v, Object o) {
    for (int i = 0; i < v.size(); i++) {
      if (v.elementAt(i).equals(o)) {
        return true;
      }
    }
    return false;
  }

  static protected void copyFile(String source, String target) {
    try {
      File destFile = new File(target);
      File fromFileFile = new File(source);
      if (!destFile.exists()) {
        destFile.createNewFile();
      }
      FileOutputStream destFileStream = new FileOutputStream(destFile.getAbsolutePath());
      FileInputStream in = new FileInputStream(fromFileFile);
      byte[] buffer = new byte[8 * 1024];
      int count = 0;
      do {
        destFileStream.write(buffer, 0, count);
        count = in.read(buffer, 0, buffer.length);
      } while (count != -1);
      in.close();
      destFileStream.close();
    }
    catch (IOException ioe) {
//			String msg = "Failed to concatenate " + fromFile + " to "
//			    + destFile.getAbsolutePath() + " due to " + ioe.getMessage();
//				throw new BuildException(msg, ioe, location);
    }
  }

  static private void insertLinkToFile(String link, String name) throws IOException {
    String stuff = "";
    stuff += "<TR>\n";
    stuff += "<TD NOWRAP><FONT CLASS=\"FrameItemFont\"><A HREF=\"" + link + "\" TARGET=\"classFrame\">" + name + "</A></FONT>\n";

    File fr = new File(baseDir + File.separator + "overview-frame.html");

    if (!fr.exists()) {
      fr = createOverviewFrame(baseDir + File.separator + "overview-frame.html");
    }

    int size = (int) fr.length();
    FileReader reader = new FileReader(fr);
    char[] all = new char[size];
    reader.read(all, 0, size);
    reader.close();

    FileWriter writer = new FileWriter(fr);
    int k = 0;
    int find = 0;
    while ((find != 1) && (k < size)) {
      if ((all[k] == '<') && (all[k + 1] == 'P') && (all[k + 2] == '>')) {
        find++;
      }
      else {
        writer.write(all[k]);
        k++;
      }
    }
    for (int j = 0; j < stuff.length(); j++) {
      writer.write(stuff.charAt(j));
    }
    while (k < size) {
      writer.write(all[k]);
      k++;
    }
    writer.close();
  }

  /**
   * prints one schema with package list and files for every entity and defined type
   *
   * @param repo
   * @param schema
   * @throws SdaiException
   * @throws IOException
   */
  private void printSchema(SdaiRepository repo, ESchema_definition schema) throws SdaiException, IOException {
    schemaName = schema.getName(null);
    SdaiModel model = schema.findEntityInstanceSdaiModel();
    f = new File(correctSchemaNameFile(schemaName));
    f.mkdir();
    PrintWriter partIndex = null;
    try {
      //-- Algorithms declarations --
      AAlgorithm_declaration algorithms = schema.getAlgorithm_declarations(null, null);
      SdaiIterator iter_a = algorithms.createIterator();
      TreeSet afunctions = new TreeSet(new SorterForEntities());
      TreeSet ref_afunctions = new TreeSet(new SorterForEntities());
      TreeSet aprocedures = new TreeSet(new SorterForEntities());
      TreeSet ref_aprocedures = new TreeSet(new SorterForEntities());
      while (iter_a.next()) {
        EAlgorithm_declaration declaration = algorithms.getCurrentMember(iter_a);
        vAlgorithmDefinition.add(declaration.getDefinition(null));
        if (declaration instanceof ELocal_declaration) {
          if (declaration instanceof EFunction_declaration) {
            afunctions.add(declaration.getDefinition(null));
          }
          if (declaration instanceof EProcedure_declaration) {
            aprocedures.add(declaration.getDefinition(null));
          }
        }
        else {
          if (declaration instanceof EFunction_declaration) {
            ref_afunctions.add(declaration.getDefinition(null));
          }
          if (declaration instanceof EProcedure_declaration) {
            ref_aprocedures.add(declaration.getDefinition(null));
          }
        }
      }
      //-- Type declarations --
      AType_declaration types = schema.getType_declarations(null, null);
      SdaiIterator iter1 = types.createIterator();
      TreeSet atypes = new TreeSet(new SorterForEntities());
      TreeSet ref_atypes = new TreeSet(new SorterForEntities());
      while (iter1.next()) {
        EDeclaration declaration = types.getCurrentMember(iter1);
        if (declaration instanceof ELocal_declaration) {
          atypes.add(declaration.getDefinition(null));
        }
        else {
          ref_atypes.add(declaration.getDefinition(null));
        }
      }
      if (types.getMemberCount() > 0) {
        Iterator atypesIter = atypes.iterator();
        while (atypesIter.hasNext()) {
          EDefined_type type = (EDefined_type) atypesIter.next();
          printDefinedType(type, false);
        }
      }
      AEntity_declaration entities = schema.getEntity_declarations(null, null);
      SdaiIterator iter2 = entities.createIterator();
      TreeSet aentity = new TreeSet(new SorterForEntities());
      TreeSet acomplex = new TreeSet(new SorterForEntities());
      TreeSet ref_aentity = new TreeSet(new SorterForEntities());
      while (iter2.next()) {
        EEntity_declaration decl = entities.getCurrentMember(iter2);
        if (decl instanceof ELocal_declaration) {
          EEntity_definition entity = (EEntity_definition) decl.getDefinition(null);
          if (entity.getComplex(null)) {
            acomplex.add(entity);
          }
          else {
            aentity.add(entity);
          }
        }
        else {
          ref_aentity.add(decl.getDefinition(null));
        }
      }
      if (aentity.size() > 0) {
        for (Iterator i = aentity.iterator(); i.hasNext(); ) {
          EEntity_definition entity = (EEntity_definition) i.next();
          printEntity(entity, schema);
        }
      }
      if (acomplex.size() > 0) {
        for (Iterator i = acomplex.iterator(); i.hasNext(); ) {
          EEntity_definition entity = (EEntity_definition) i.next();
          printEntity(entity, schema);
          //System.out.println(entity);
        }
      }
      // --- Global rules ---
      ARule_declaration rules = schema.getRule_declarations(null, null);
      SdaiIterator iter_r = rules.createIterator();
      TreeSet arules = new TreeSet(new SorterForEntities());
      TreeSet ref_arules = new TreeSet(new SorterForEntities());
      while (iter_r.next()) {
        ERule_declaration declaration = rules.getCurrentMember(iter_r);
        if (declaration instanceof ELocal_declaration) {
          arules.add(declaration.getDefinition(null));
        }
        else {
          ref_arules.add(declaration.getDefinition(null));
        }
      }
      if (arules.size() > 0) {
        Iterator arulesIter = arules.iterator();
        while (arulesIter.hasNext()) {
          EGlobal_rule rule = (EGlobal_rule) arulesIter.next();
          printGlobalRule(rule, schema);
        }
      }

      // --- Subtype Constraints ---
      ASubtype_constraint_declaration constraints = (ASubtype_constraint_declaration) model
          .getEntityExtentInstances(ESubtype_constraint_declaration.class);
      SdaiIterator iter_sc = constraints.createIterator();
      TreeSet aconstraints = new TreeSet(new SorterForEntities());
      TreeSet ref_aconstraints = new TreeSet(new SorterForEntities());
      while (iter_sc.next()) {
        ESubtype_constraint_declaration declaration = constraints.getCurrentMember(iter_sc);
        // only stand-alone subtype constraints have declarations, yes? if not, additionally check the name from definition
        if (declaration instanceof ELocal_declaration) {
          aconstraints.add(declaration.getDefinition(null));
        }
        else {
          ref_aconstraints.add(declaration.getDefinition(null));
        }
      }
      if (aconstraints.size() > 0) {
        Iterator aconstraintsIter = aconstraints.iterator();
        while (aconstraintsIter.hasNext()) {
          ESub_supertype_constraint constraint = (ESub_supertype_constraint) aconstraintsIter.next();
          printSubtypeConstraint(constraint, schema);
        }
      }

      // --- Algorithms ---
      if (afunctions.size() > 0) {
        Iterator afunctionsIter = afunctions.iterator();
        while (afunctionsIter.hasNext()) {
          EFunction_definition function = (EFunction_definition) afunctionsIter.next();
          printAlgorithm(function, schema);
        }
      }
      if (aprocedures.size() > 0) {
        Iterator aproceduresIter = aprocedures.iterator();
        while (aproceduresIter.hasNext()) {
          EProcedure_definition procedure = (EProcedure_definition) aproceduresIter.next();
          printAlgorithm(procedure, schema);
        }
      }
      // --- Constants ---
      AConstant_declaration constants = (AConstant_declaration) model.getInstances(EConstant_declaration.class);
      SdaiIterator iter_c = constants.createIterator();
      TreeSet aconstants = new TreeSet(new SorterForEntities());
      TreeSet ref_aconstants = new TreeSet(new SorterForEntities());
      while (iter_c.next()) {
        EConstant_declaration declaration = constants.getCurrentMember(iter_c);
        if (declaration instanceof ELocal_declaration) {
          aconstants.add(declaration.getDefinition(null));
        }
        else {
          ref_aconstants.add(declaration.getDefinition(null));
        }
      }
      if (aconstants.size() > 0) {
        Iterator aconstantsIter = aconstants.iterator();
        while (aconstantsIter.hasNext()) {
          EConstant_definition constant = (EConstant_definition) aconstantsIter.next();
          printConstant(constant, schema);
        }
      }

      TreeSet complex_types = new TreeSet(new SorterForEntities());
      complex_types.addAll(aconstants);
      complex_types.addAll(atypes);
      complex_types.addAll(aentity);
      complex_types.addAll(arules);
      complex_types.addAll(afunctions);
      complex_types.addAll(aprocedures);
      complex_types.addAll(aconstraints);
      f = new File(correctSchemaNameFile(schemaName) + File.separator + "package-frame.html");
      f.createNewFile();
      fw = new FileWriter(f);
      partIndex = new PrintWriter(new BufferedWriter(fw));
      partIndex.print(printHtmlHead(schema.getName(null)));
      partIndex.print(println(printHRefandTarget("<I>" + correctSchemaName(schemaName) + "</I>", "package-summary.html", "classFrame")));
      if ((ref_aconstants.size() > 0) || (ref_atypes.size() > 0) || (ref_aentity.size() > 0) || (ref_arules.size() > 0) || (ref_afunctions.size() > 0)
          || (ref_aprocedures.size() > 0)) {
        partIndex.print(print("short  "));
        partIndex.print(printHRefandTarget("long", "package-frame-long.html", "packageFrame"));
        partIndex.print(println());
      }
      else {
        partIndex.print(println());
      }
      printLeftLowerIndex(partIndex, complex_types, model);
      partIndex.print(printHtmlTail());
      partIndex.close();
      partIndex = null;
      if ((ref_aconstants.size() > 0) || (ref_atypes.size() > 0) || (ref_aentity.size() > 0) || (ref_arules.size() > 0) || (ref_afunctions.size() > 0)
          || (ref_aprocedures.size() > 0)) {
        f = new File(correctSchemaNameFile(schemaName) + File.separator + "package-frame-long.html");
        f.createNewFile();
        fw = new FileWriter(f);
        partIndex = new PrintWriter(new BufferedWriter(fw));
        partIndex.print(printHtmlHead(schema.getName(null)));
        partIndex.print(println(printHRefandTarget("<I>" + correctSchemaName(schemaName) + "</I>", "package-summary.html", "classFrame")));
        partIndex.print(printHRefandTarget("short", "package-frame.html", "packageFrame") + "  ");
        partIndex.print(print("long"));
        partIndex.print(println());
        complex_types.addAll(ref_aconstants);
        complex_types.addAll(ref_atypes);
        complex_types.addAll(ref_aentity);
        complex_types.addAll(ref_arules);
        complex_types.addAll(ref_afunctions);
        complex_types.addAll(ref_aprocedures);
        complex_types.addAll(ref_aconstraints);
        printLeftLowerIndex(partIndex, complex_types, model);
        partIndex.print(printHtmlTail());
        partIndex.close();
        partIndex = null;
      }

      index.addAll(aconstants);
      index.addAll(atypes);
      index.addAll(aentity);
      //index.addAll(acomplex);
      index.addAll(arules);
      index.addAll(afunctions);
      index.addAll(aprocedures);
      index.addAll(aconstraints);

      printSchemaSummary(repo, schema);
      vAlgorithmDefinition.clear();
    }
    finally {
      if (partIndex != null) {
        partIndex.close();
      }
    }
  }

  private void printLeftLowerIndex(PrintWriter partLetter, TreeSet index, SdaiModel model) throws SdaiException, IOException {
    boolean simple_index = index.size() < 200;
    String letters = "";
    letters += "<FONT size=\"-1\">\n";
    //Generating index(A,B,C...)
    String s = "";
    for (Iterator j = index.iterator(); j.hasNext(); ) {
      EEntity type = (EEntity) j.next();
      String sName = getDictionaryEntityName(type);
      if (!s.equalsIgnoreCase(sName.substring(0, 1))) {
        s = sName.substring(0, 1);
        letters += printHRef(s.toUpperCase(), "#" + s.toUpperCase()) + " ";
      }
    }
    letters += "<FONT>\n";

    String part = "";
    partLetter.print((simple_index) ? "" : letters);
    partLetter.print(printTableHeaderRR());
    s = "";
    for (Iterator j = index.iterator(); j.hasNext(); ) {
      EEntity type = (EEntity) j.next();
      if (type instanceof EConstant_definition || type instanceof ENamed_type || type instanceof EGlobal_rule || type instanceof EAlgorithm_definition
          || type instanceof ESub_supertype_constraint) {
        String sTypeName = getDictionaryEntityName(type);
        String prefix = "<TR><TD WIDTH=3>";
        prefix += getDictionaryEntityTypeRR(type, model);
        prefix += "</TD><TD>";
        if (!s.equalsIgnoreCase(sTypeName.substring(0, 1))) {
          if (!s.equals("")) {
            partLetter.print(part);
            part = "";
          }
          s = sTypeName.substring(0, 1);
          part += (simple_index) ? "" : "<TR><TD WIDTH=3>" + (println(printName("<B>" + s.toUpperCase() + "</B>", s.toUpperCase()))) + "</TD></TR>";
        }
        part += ((findSchemaForEntity(type) == schema) ? prefix : prefix.toLowerCase()) + " ";
        part += printHRefandTarget(printCapitalCase(getComplexName(sTypeName)), getSchemaNameIfDiffer(type, schema)
            + getComplexName(getUpper(sTypeName)) + ".html", "classFrame");
        part += "</TD></TR>";
      }
    }
    if (!s.equals("")) {
      partLetter.print(part);
    }
    partLetter.print(printTableTailRR());
  }

  /**
   * prints package summary for a single schema in a file called package-summary.html
   *
   * @param repo
   * @param schema
   * @throws SdaiException
   * @throws IOException
   */
  private void printSchemaSummary(SdaiRepository repo, final ESchema_definition schema) throws SdaiException, IOException {
    f = new File(correctSchemaNameFile(schemaName) + File.separator + "package-summary.html");
    f.createNewFile();
    fw = new FileWriter(f);
    PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
    try {
      StringBuffer partSummary = new StringBuffer();
      printHtmlHead(partSummary, schema.getName(null));
      SdaiIterator it;
      printNavBar(partSummary);
      printH3ISO(partSummary, schema.getName(null));
      printTab(partSummary, new StrOp() {
        public void op(StringBuffer result) throws SdaiException {
          result.append("identification:").append(schema.testIdentification(null) ? schema.getIdentification(null) : " - ");
        }
      });
      partSummary.append(printBreak());
      String doc = findDocFor(schema);
      if (!doc.equals("")) {
        partSummary.append(doc).append(printBreak());
      }
      writeToAndClean(partSummary, pw);
      printInterfacedDeclarations(partSummary, schema);
      writeToAndClean(partSummary, pw);
      SdaiModel model = schema.findEntityInstanceSdaiModel();
      ALocal_declaration local_result = (ALocal_declaration) model.getInstances(CLocal_declaration.class);

      it = local_result.createIterator();
      StringBuffer entity = new StringBuffer();
      StringBuffer complex = new StringBuffer();
      StringBuffer defined = new StringBuffer();
      StringBuffer function = new StringBuffer();
      StringBuffer procedure = new StringBuffer();
      StringBuffer constant = new StringBuffer();
      StringBuffer global = new StringBuffer();
      StringBuffer subconstraint = new StringBuffer();

      DeclarationTabOp declarationTabOp = new DeclarationTabOp();
      TreeSet entities = new TreeSet(new SorterForEntities());
      while (it.next()) {
        EEntity type = local_result.getCurrentMember(it).getDefinition(null);
        entities.add(type);
      }
      for (Iterator i = entities.iterator(); i.hasNext(); ) {
        EEntity type = (EEntity) i.next();
        if (type instanceof EAlgorithm_definition) {
          if (type instanceof EFunction_definition) {
            EFunction_definition fd = (EFunction_definition) type;
            declarationTabOp.name = fd.getName(null);
            printTab(function, declarationTabOp);
          }
          if (type instanceof EProcedure_definition) {
            EProcedure_definition pd = (EProcedure_definition) type;
            declarationTabOp.name = pd.getName(null);
            printTab(procedure, declarationTabOp);
          }
        }
        else if (type instanceof ENamed_type) {
          if (type instanceof EEntity_definition) {
            EEntity_definition en = (EEntity_definition) type;
            if (en.getComplex(null)) {
              declarationTabOp.name = en.getName(null);
              printTab(complex, declarationTabOp);
            }
            else {
              declarationTabOp.name = en.getName(null);
              printTab(entity, declarationTabOp);
            }
          }
          else {
            declarationTabOp.name = ((EDefined_type) type).getName(null);
            printTab(defined, declarationTabOp);
          }
        }
        else if (type instanceof EConstant_definition) {
          declarationTabOp.name = ((EConstant_definition) type).getName(null);
          printTab(constant, declarationTabOp);
        }
        else if (type instanceof EGlobal_rule) {
          EGlobal_rule gr = (EGlobal_rule) type;
          AEntity_definition global_entities = gr.getEntities(null);
          SdaiIterator global_entities_it = global_entities.createIterator();
          global.append(printUnclosedTab());
          declarationTabOp.name = gr.getName(null);
          declarationTabOp.op(global);
          global.append('(');
          boolean first = true;
          while (global_entities_it.next()) {
            if (first) {
              first = false;
            }
            else {
              global.append(", ");
            }
            EEntity gentity = global_entities.getCurrentMember(global_entities_it);
            printHRef(global, getDictionaryEntityNameOp(gentity), getSchemaRefDicOp(gentity, schema));
          }
          global.append(')');
          global.append(println());
        }
        else if (type instanceof ESub_supertype_constraint) {
          ESub_supertype_constraint ssc = (ESub_supertype_constraint) type;
          if (ssc.testName(null)) {
            EEntity_or_view_definition subtype_constraint_parent_entity = ssc.getGeneric_supertype(null);
            subconstraint.append(printUnclosedTab());
            declarationTabOp.name = ssc.getName(null);
            declarationTabOp.op(subconstraint);
            subconstraint.append('(');
            printHRef(subconstraint, getDictionaryEntityNameOp(subtype_constraint_parent_entity),
                getSchemaRefDicOp(subtype_constraint_parent_entity, schema));
            subconstraint.append(')');
            subconstraint.append(println());
          }
        }
      }
      if (constant.length() > 0) {
        printH4(partSummary, "Constants");
        writeToAndClean(partSummary, pw);
        writeToAndClean(constant, pw);
        pw.print(printBreak());
      }
      if (defined.length() > 0) {
        printH4(partSummary, "Defined types");
        writeToAndClean(partSummary, pw);
        writeToAndClean(defined, pw);
        pw.print(printBreak());
      }
      if (entity.length() > 0) {
        printH4(partSummary, "Entities");
        writeToAndClean(partSummary, pw);
        writeToAndClean(entity, pw);
        pw.print(printBreak());
      }
      if (complex.length() > 0) {
        printH4(partSummary, "Complex entities");
        writeToAndClean(partSummary, pw);
        writeToAndClean(complex, pw);
        pw.print(printBreak());
      }
      if (function.length() > 0) {
        printH4(partSummary, "Functions");
        writeToAndClean(partSummary, pw);
        writeToAndClean(function, pw);
        pw.print(printBreak());
      }
      if (procedure.length() > 0) {
        printH4(partSummary, "Procedures");
        writeToAndClean(partSummary, pw);
        writeToAndClean(procedure, pw);
        pw.print(printBreak());
      }
      if (global.length() > 0) {
        printH4(partSummary, "Global rules");
        writeToAndClean(partSummary, pw);
        writeToAndClean(global, pw);
        pw.print(printBreak());
      }
      if (subconstraint.length() > 0) {
        printH4(partSummary, "Subtype Constraints");
        writeToAndClean(partSummary, pw);
        writeToAndClean(subconstraint, pw);
        pw.print(printBreak());
      }
      if (printInterfacingSchemas(pw, printH4("Interfacing Schemas"), repo, schema)) {
        pw.print(printBreak());
      }

      if (schemaHasExtensibleTypes(schema)) {
        pw.print(printBold(printHRefandTarget("Extensible types", "../" + correctSchemaNameS(schema) + "/package-extensible-types.html", "classFrame")));
        pw.print(printBreak());
        createExtensibleTypePage(schema);
      }

      pw.println(printHtmlTail());
    }
    finally {
      pw.close();
    }
  }

  private static void writeToAndClean(StringBuffer buffer, Writer writer) throws IOException {
    char chars[] = new char[4096];
    int end = buffer.length();
    int localEnd;
    for (int start = 0; start < end; start = localEnd) {
      localEnd = start + chars.length;
      if (localEnd > end) {
        localEnd = end;
      }
      buffer.getChars(start, localEnd, chars, 0);
      writer.write(chars, 0, localEnd - start);
    }
    buffer.setLength(0);
  }

  private static class DeclarationTabOp implements StrOp {
    private String name;
    private final DecNameOp decNameOp = new DecNameOp();
    private final DecRefOp decRefOp = new DecRefOp();

    public void op(StringBuffer result) throws SdaiException {
      printHRef(result, decNameOp, decRefOp);
    }

    private class DecNameOp implements StrOp {
      public void op(StringBuffer result) throws SdaiException {
        printCapitalCase(result, getComplexNameOp(name));
      }
    }

    private class DecRefOp implements StrOp {
      public void op(StringBuffer result) throws SdaiException {
        getUpper(result, getComplexNameOp(name));
        result.append(".html");
      }
    }
  }

  void createExtensibleTypePage(ESchema_definition schema) throws SdaiException, IOException {
    f = new File(correctSchemaNameFile(schema) + File.separator + "package-extensible-types.html");
    f.createNewFile();
    fw = new FileWriter(f);
    PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
    try {
      pw.print(printHtmlHead(schema.getName(null)));

      /*
       *
       * let's have here only root extensible types
       * even if the type is not interfaced explicitly, it is interfaced implicitly, if any based_on type is interfaced.
       *
       * So, only extensible types that are not extended
       * Add elements into the list of such an extensible type in the following way:
       * - take the elements that are already in the selection list of the root type, if any, but only those that have
       * declarations in the current schema
       * - for all types that are based on the root type (and chained recursively) that have declarations in the current
       * schema add their elements,
       * but only those that have declarations in the current schema.
       */

      TreeSet roots = new TreeSet(new SorterForEntities());
      HashMap roots_with_elements = new HashMap();
      SdaiModel a_model = schema.findEntityInstanceSdaiModel();
      Set definitionSet = new HashSet();
      Map extSelBasedOnMap = new HashMap();
      Map extEnumBasedOnMap = new HashMap();
      createDeclarationSetsAndMap(a_model, definitionSet, extSelBasedOnMap, extEnumBasedOnMap);
      ADeclaration declarations = (ADeclaration) a_model.getInstances(EDeclaration.class);
      SdaiIterator iter = declarations.createIterator();
      while (iter.next()) {
        EDeclaration declaration = declarations.getCurrentMember(iter);
        EEntity definition = declaration.getDefinition(null);
        if (definition instanceof EDefined_type) {
          EEntity domain = ((EDefined_type) definition).getDomain(null);
          if ((domain instanceof EExtensible_select_type) && (!(domain instanceof EExtended_select_type))) {
            // a root extensible select type - that is the type for which we will calculate all the selection list elements for the current schema
            // we need to sort these root types alphabetically somehow.
            // Let's just put them into a tree set, and in parallel - put the TreeSet with elemenets into a hash map with the key - this root type.
            roots.add(definition);
            // 1.add the elements from the selection list of the type, if they have declarations in the current schema
            // 2 find all the types based on this select type that have declarations in the current schema, and for each of them, recursively repeat 1 & 2
            // we can put all this into a recursive method
            TreeSet elements = new TreeSet(new SorterForEntities());
            collectSelectElementsRecursively((EDefined_type) definition, (ESelect_type) domain, definitionSet, a_model, extSelBasedOnMap, elements);
            TreeSet root_elements = new TreeSet(elements);
            roots_with_elements.put(definition, root_elements);
            // for testing, just print elements
            /*
             * Iterator iter_e = elements.iterator();
             * while (iter_e.hasNext()) {
             * ENamed_type element = (ENamed_type)iter_e.next();
             * strExtensibleTypes += printTab(element.getName(null));
             * }
             */
          }
          else if ((domain instanceof EExtensible_enumeration_type) && (!(domain instanceof EExtended_enumeration_type))) {
            roots.add(definition);
            TreeSet eelements = new TreeSet();
            collectEnumerationElementsRecursively((EDefined_type) definition, (EEnumeration_type) domain, a_model, extEnumBasedOnMap, eelements);
            TreeSet root_eelements = new TreeSet(eelements);
            roots_with_elements.put(definition, root_eelements);
          }
        }
      }

      String schema_name = schema.getName(null);
      // ok, so now let's print the types
      pw.print(printH3("Extensible select and enumeration types"));
      pw.print(printH4("and their elements in schema " + schema_name + " taking into account BASED_ON types <BR>"));

      Iterator iter_root = roots.iterator();
      while (iter_root.hasNext()) {
        EDefined_type dt = (EDefined_type) iter_root.next();
        String str_type = "<I>";
        EEntity domain = dt.getDomain(null);
        if (domain instanceof ESelect_type) {
          if (domain instanceof EExtensible_select_type) {
            str_type += "EXTENSIBLE ";
          }
          if (domain instanceof EEntity_select_type) {
            str_type += "GENERIC_ENTITY ";
          }
          str_type += "SELECT";
        }
        else if (domain instanceof EEnumeration_type) {
          if (domain instanceof EExtensible_enumeration_type) {
            str_type += "EXTENSIBLE ";
          }
          str_type += "ENUMERATION";
        }
        pw.print("<BR> &nbsp;"
            + printHRef(getDictionaryEntityName(dt), getSchemaNameIfDiffer(dt, schema) + getUpper(getDictionaryEntityName(dt)) + ".html") + " - "
            + str_type + "<BR>");
        TreeSet dt_elements = (TreeSet) roots_with_elements.get(dt);
        if (dt_elements != null) {
          Iterator iter_e = dt_elements.iterator();
          while (iter_e.hasNext()) {
            Object element = iter_e.next();
            if (element instanceof ENamed_type) {
              pw.print(printTab(printHRef(getDictionaryEntityName((ENamed_type) element), getSchemaNameIfDiffer((ENamed_type) element, schema)
                  + getUpper(getDictionaryEntityName((ENamed_type) element)) + ".html")));
            }
            else if (element instanceof String) {
              // enumeration element
              pw.print(printTab((String) element));
            }
          }
        }
        str_type += "</I>";
      }

      pw.print(printHtmlTail());
    }
    finally {
      pw.close();
    }
  }

  static void createDeclarationSetsAndMap(SdaiModel a_model, final Set definitionSet, final Map extSelBasedOnMap, final Map extEnumBasedOnMap)
      throws SdaiException {
    ADeclaration declarations = (ADeclaration) a_model.getInstances(EDeclaration.class);
    SdaiIterator iter = declarations.createIterator();
    while (iter.next()) {
      EDeclaration declaration = declarations.getCurrentMember(iter);
      EEntity definition = declaration.getDefinition(null);
      definitionSet.add(definition);
      if (definition instanceof EDefined_type) {
        EEntity domain = ((EDefined_type) definition).getDomain(null);
        if (domain instanceof EExtended_select_type) {
          EDefined_type based_on = ((EExtended_select_type) domain).getIs_based_on(null);
          Collection definitions = (Collection) extSelBasedOnMap.get(based_on);
          if (definitions == null) {
            definitions = new ArrayList();
            extSelBasedOnMap.put(based_on, definitions);
          }
          definitions.add(definition);
        }
        else if (domain instanceof EExtended_enumeration_type) {
          EDefined_type based_on = ((EExtended_enumeration_type) domain).getIs_based_on(null);
          extEnumBasedOnMap.put(based_on, definition);
        }
      }
    }
  }

  static void collectSelectElementsRecursively(EDefined_type dt, ESelect_type sel, Set definitionSet, SdaiModel a_model, Map basedOnMap, TreeSet elements)
      throws SdaiException {
    addElementsFromSelectionList(sel, definitionSet, a_model, elements);

    Collection definitions = (Collection) basedOnMap.get(dt);
    if (definitions != null) {
      for (Iterator i = definitions.iterator(); i.hasNext(); ) {
        EDefined_type definition = (EDefined_type) i.next();
        EEntity domain = definition.getDomain(null);
        collectSelectElementsRecursively(definition, (ESelect_type) domain, definitionSet, a_model, basedOnMap, elements);
      }
    }
  }

  static void collectEnumerationElementsRecursively(EDefined_type dt, EEnumeration_type enumType, SdaiModel a_model, Map basedOnMap, TreeSet elements)
      throws SdaiException {
    addEnumerationElements(enumType, a_model, elements);

    EDefined_type definition = (EDefined_type) basedOnMap.get(dt);
    if (definition != null) {
      EEntity domain = definition.getDomain(null);
      collectEnumerationElementsRecursively(definition, (EEnumeration_type) domain, a_model, basedOnMap, elements);
    }
  }

  static void addEnumerationElements(EEnumeration_type enumType, SdaiModel a_model, TreeSet elements) throws SdaiException {
    A_string local_elements = null;
    if (enumType.testLocal_elements(null)) {
      local_elements = enumType.getLocal_elements(null);
      SdaiIterator iter_le = local_elements.createIterator();
      while (iter_le.next()) {
        String local_element = local_elements.getCurrentMember(iter_le);
        // enumeration elements do not have declarations, nothing to check
        elements.add(local_element);

      }
    }
  }

  static void addElementsFromSelectionList(ESelect_type sel, Set definitionSet, SdaiModel a_model, TreeSet elements) throws SdaiException {
    ANamed_type local_elements = null;
    if (sel.testLocal_selections(null)) {
      local_elements = sel.getLocal_selections(null);
      SdaiIterator iter_le = local_elements.createIterator();
      while (iter_le.next()) {
        ENamed_type local_element = local_elements.getCurrentMember(iter_le);
        // see if this local_element has a declaration in the current schema, if so, add to elements
        if (definitionSet.contains(local_element)) {
          elements.add(local_element);
        }

      }
    }
  }

  boolean schemaHasExtensibleTypes(ESchema_definition schema) throws SdaiException {
    SdaiModel a_model = schema.findEntityInstanceSdaiModel();
    ADeclaration declarations = (ADeclaration) a_model.getInstances(EDeclaration.class);
    SdaiIterator iter = declarations.createIterator();
    while (iter.next()) {
      EDeclaration declaration = declarations.getCurrentMember(iter);
      EEntity definition = declaration.getDefinition(null);
      if (definition instanceof EDefined_type) {
        EEntity domain = ((EDefined_type) definition).getDomain(null);
        if ((domain instanceof EExtensible_select_type) || (domain instanceof EExtensible_enumeration_type)) {
          return true;
        }
      }
    }
    return false;
  }

  String printIsoNumber(String schema_name) {
    String iso_number = "";
    if (hm_iso_ids != null) {
      iso_number = (String) hm_iso_ids.get(schema_name.toLowerCase());
      if (iso_number == null) {
        iso_number = "";
      }
    }
    if (!iso_number.equals("")) {
      return "<I>" + iso_number + "</I>";
    }
    return "";
  }

  void printIsoNumber(StringBuffer result, String schema_name) {
    String iso_number = "";
    if (hm_iso_ids != null) {
      iso_number = (String) hm_iso_ids.get(schema_name.toLowerCase());
      if (iso_number == null) {
        iso_number = "";
      }
    }
    if (!iso_number.equals("")) {
      result.append("<I>").append(iso_number).append("</I>");
    }
  }

  private StrOp printIsoNumberOp(final String schema_name) {
    return new StrOp() {
      public void op(StringBuffer result) {
        printIsoNumber(result, schema_name);
      }
    };
  }

  String printIsoNumberComment(String schema_name) {
    String iso_number = "";
    if (hm_iso_ids != null) {
      iso_number = (String) hm_iso_ids.get(schema_name.toLowerCase());
      if (iso_number == null) {
        iso_number = "";
      }
    }
    if (!iso_number.equals("")) {
      return printUnclosedTab("-- <I>" + iso_number + "</I>");
    }
    return "";
  }

  void printIsoNumberComment(StringBuffer result, String schema_name) {
    String iso_number = "";
    if (hm_iso_ids != null) {
      iso_number = (String) hm_iso_ids.get(schema_name.toLowerCase());
      if (iso_number == null) {
        iso_number = "";
      }
    }
    if (!iso_number.equals("")) {
      result.append(printUnclosedTab()).append("-- <I>").append(iso_number).append("</I>");
    }
  }

  /**
   * prints the list of schemas in alphabetic order into which this schema is partially or completely directly or indirectly
   * interfaced
   * with letters indicating if it is used, referenced or indirectly interfaced, and with iso number comments.
   * To find if a schema is directly interfaced we can only through interface_specifications
   * To find if it is chain use fromed perhaps better just by checking declarations (or by recursively analyzing
   * interface_specifications).
   * However, if a schema is not directly interfaced but still has used declarations in another schema, then it is chain used.
   * If a schema is not directly interfaced but still has referenced declarations in another schema, then it is chain used until
   * the last
   * schema and then referenced.
   * <p>
   * We could have the following information:
   * 1. if a schema is directly explicitly interfaced or not: used, referenced or both
   * 2. if a schema is explicitly indirectly used, referenced or both
   * The above two could be listed simultaneously, or have the most/lest important intefacing listed only:
   * if something is interfaced both directly and indirectly, direct (or indirect) takes precedent
   * if something is used and something is referenced, used (or referenced) takes precedent.
   * So, for example u could mean that at least something is directly used (but something else may be directly referenced or
   * used/referenced indirectly)
   * 3. if something from a schema is implicitly interfaced, more difficult to tell directly or indirectly, perhaps not
   * important if directly
   * <p>
   * perhaps better to use two letter-system:
   * du - at least something from the schema is directly used in that schema (something else might be directly referenced,
   * indirectly interfaced, etc.)
   * dr - at least something from the schema is directly referenced in that schema, but nothing is directly used
   * iu - at least something from the schema is indirectly used in that schema, but nothing is interfaced explicitly directly
   * ir - at least something from the schema is indirectly referenced in that schema, but nothing is interfaced directly and
   * nothing is used indirectly
   * i - at lesat something from the schema is implicitly interfaced in that schema, but nothing explicitly, directly or
   * indirectly
   * <p>
   * Here it is interesting what is more important to indicate - if something is directly referenced but something else is
   * indirectly used,
   * what takes the precedent - directly or used - still has independently instantiable items interfaced, even if indirectly.
   * <p>
   * perhaps we could use multiple columns to indicate both
   * <p>
   * perhaps we could use upper and lower case letters:
   * <p>
   * U - used directly
   * u - used indirectly
   * R - refereced direcly
   * r - referenced indirecly
   * then we could have (up to) three column indicators
   * uRi
   * U
   * we could mark implicit only when no others are available:
   * uR
   * U
   * r
   * i
   * <p>
   * --------------------
   * <p>
   * implementation:
   * 1. have a set of schemas that directly use the schema
   * 2. have a separate set of schemas that directly reference the schema
   * 3. have a set of schemas that use the schema, remove occurring also in the list 1 = only indirectly used
   * 4. have a set of schemas that reference the schema, remove occurring also in the list2 = only indirectly referenced
   * 5. have a set of schemas that have implicit declarations from the schema
   * from the above 5 lists make a merged list and the indicator
   */
  private boolean printInterfacingSchemas(PrintWriter writer, String header, SdaiRepository repo, ESchema_definition schema) throws SdaiException {
    boolean first = true;
    HashSet directly_using = new HashSet();
    HashSet directly_referencing = new HashSet();
    HashSet using = new HashSet();
    HashSet referencing = new HashSet();
    HashSet only_indirectly_using = new HashSet();
    HashSet only_indirectly_referencing = new HashSet();
    HashSet implicitly_interfacing = new HashSet();

    ASdaiModel models = repo.getModels();
    SdaiIterator iter = models.createIterator();
    while (iter.next()) {
      SdaiModel model = models.getCurrentMember(iter);
      String model_name = model.getName();
      if (model_name.endsWith("_DICTIONARY_DATA")) {
        if (((include_models.size() == 0) || haveVector(include_models, model_name)) && (!haveVector(exclude_models, model_name))) {
          // for this model, check what can be added to the five sets
          exploreInterfacingForModel(model, schema, directly_using, directly_referencing, using, referencing, implicitly_interfacing);
        }
      }
    }
    using.removeAll(directly_using);
    only_indirectly_using = using;
    referencing.removeAll(directly_referencing);
    only_indirectly_referencing = referencing;

    /*
     * perhaps we can implement several different behaviours controlled by switches
     * - include implicit/not include implicit
     *
     * U - using directly, never mind if referencing
     * R - referencing directly, not using, directly or indirectly
     * u - using indirectly, not referencing directly
     * r - referencing indirectly, not using, directly or indirectly
     * M - Mixed interfacing, referencing directly, but using indirectly - still with the power of independent instantiation
     * B - both, referencing directly but using indirectly, the same as M
     * I - the same as above M or B, for "Interfacing"
     * i - interfacing implicitly only - include with a special
     */

    String interfacing_code_U = "U";
    String interfacing_code_R = "R";
    String interfacing_code_u = "u";
    String interfacing_code_r = "r";
//		String interfacing_code_M = "M";
    String interfacing_code_i = "i";

    // combine all interfacing schemas into this HashMap so that it can be sorted and printed afterwards
    HashMap all_interfacing = new HashMap();

    // U
    Iterator iter_du = directly_using.iterator();
    while (iter_du.hasNext()) {
      Object current = iter_du.next();
      Object o = all_interfacing.put(current, interfacing_code_U);
      if (o != null) {
        System.out.println("internal ERROR #1 (U) in printInterfacingSchemas: " + o);
      }
    }

    // u & M
    Iterator iter_oiu = only_indirectly_using.iterator();
    while (iter_oiu.hasNext()) {
      // only if not referencing directly
      Object current = iter_oiu.next();
      // M is currently disabled, u is used instead
      Object o = all_interfacing.put(current, interfacing_code_u);
      if (o != null) {
        System.out.println("internal ERROR #2 (u) in printInterfacingSchemas: " + o);
      }
      if (flag_print_M_message) {
        if (!directly_referencing.contains(current)) {
          //				all_interfacing.put(current, interfacing_code_u);
        }
        else {
          //				all_interfacing.put(current, interfacing_code_M);
          System.out.println("Mixed interfacing occured, schema " + schema.getName(null) + " interfaced into "
              + findSchema((SdaiModel) current).getName(null));
        }
      }
    }

    // R
    Iterator iter_dr = directly_referencing.iterator();
    while (iter_dr.hasNext()) {
      // only if not using, directly or indirectly
      Object current = iter_dr.next();
      if (directly_using.contains(current)) {
        continue;
      }
      if (only_indirectly_using.contains(current)) {
        continue;
      }
      Object o = all_interfacing.put(current, interfacing_code_R);
      if (o != null) {
        System.out.println("internal ERROR #3 (R) in printInterfacingSchemas: " + o);
      }
    }

    // r
    Iterator iter_oir = only_indirectly_referencing.iterator();
    while (iter_oir.hasNext()) {
      // only if not using, directly or indirectly
      Object current = iter_oir.next();
      if (directly_using.contains(current)) {
        continue;
      }
      if (only_indirectly_using.contains(current)) {
        continue;
      }
      Object o = all_interfacing.put(current, interfacing_code_r);
      if (o != null) {
        System.out.println("internal ERROR #4 (r) in printInterfacingSchemas: " + o);
      }
    }

    Iterator iter_i = implicitly_interfacing.iterator();
    while (iter_i.hasNext()) {
      Object current = iter_i.next();
      if (directly_using.contains(current)) {
        continue;
      }
      if (only_indirectly_using.contains(current)) {
        continue;
      }
      if (directly_referencing.contains(current)) {
        continue;
      }
      if (only_indirectly_referencing.contains(current)) {
        continue;
      }
      Object o = all_interfacing.put(current, interfacing_code_i);
      if (o != null) {
        System.out.println("internal ERROR #5 (i) in printInterfacingSchemas: " + o);
      }
    }

    // ok, now sort them by schema names
    SdaiModel[] all_models = new SdaiModel[all_interfacing.size()];
    Set keys = all_interfacing.keySet();
    Iterator iter_keys = keys.iterator();
    int model_count = 0;
    while (iter_keys.hasNext()) {
      SdaiModel current = (SdaiModel) iter_keys.next();
      all_models[model_count] = current;
      model_count++;
    }
    Arrays.sort(all_models, new SorterForModels());

    for (int i = 0; i < model_count; i++) {
      SdaiModel current = all_models[i];
      String interfacing_code = (String) all_interfacing.get(current);
      ESchema_definition current_schema = findSchema(current);
      String schema_name = current_schema.getName(null);
      // now we have the code and the model, we need also the iso number for the schema

      String interfacing_schema_str = printUnclosedTab()
          + "<tt>"
          + interfacing_code
          + " </tt>"
          + printBold(printHRefandTarget(correctSchemaName(schema_name), "../" + correctSchemaNameS(schema_name) + "/package-summary.html",
          "classFrame")) + "&nbsp; " + printIsoNumber(schema_name) + "<BR>";
      if (first) {
        writer.print(header);
        first = false;
      }
      writer.print(interfacing_schema_str);
    }

    return !first;

  }

  private void exploreInterfacingForModel(SdaiModel interfacing_model, ESchema_definition interfaced_schema, HashSet directly_using,
      HashSet directly_referencing, HashSet using, HashSet referencing, HashSet implicitly_interfacing) throws SdaiException {
    // for directly_used and directly_referenced, go through all the interface_specifications
    SdaiModel interfaced_model = interfaced_schema.findEntityInstanceSdaiModel();
    AInterface_specification specifications = (AInterface_specification) interfacing_model.getInstances(CInterface_specification.class);
    SdaiIterator specifications_it = specifications.createIterator();
    while (specifications_it.next()) {
      EInterface_specification specification = (EInterface_specification) specifications.getCurrentMemberObject(specifications_it);
      ESchema_definition referenced_schema = (ESchema_definition) specification.getForeign_schema(null);
      if (referenced_schema == interfaced_schema) {
        if (specification instanceof EUse_from_specification) {
          // can make it schema, if needed
          directly_using.add(interfacing_model);
        }
        else if (specification instanceof EReference_from_specification) {
          // can make it schema, if needed
          directly_referencing.add(interfacing_model);
        }
        else {
          // internal error
          System.out.println("Internal ERROR #1 in exploreInterfacingForModel - interfacing model: " + interfacing_model + ", interfaced schema: "
              + interfaced_schema + ", interface specification: " + specification);
        }
      }
      else {
        // we need to check for wrapper schemas - using recursion - only use_from_specification with unset optional items attribute are interesting to us
        HashSet detect_loop = new HashSet(); // needed to prevent stack overflow - some schemas are interfaced in loops
        if (isAWrapper(interfaced_schema, specification, detect_loop)) {
          using.add(interfacing_model);
        }
      }
    }
    // for others - go through declarations
    ADeclaration declarations = (ADeclaration) interfacing_model.getInstances(CDeclaration.class);
    SdaiIterator declarations_it = declarations.createIterator();
    while (declarations_it.next()) {
      EDeclaration declaration = (EDeclaration) declarations.getCurrentMemberObject(declarations_it);
      EEntity definition = declaration.getDefinition(null);
      SdaiModel definition_model = definition.findEntityInstanceSdaiModel();
      if (definition_model == interfaced_model) {
        if (declaration instanceof EUsed_declaration) {
          using.add(interfacing_model);
        }
        else if (declaration instanceof EReferenced_declaration) {
          referencing.add(interfacing_model);
        }
        else if (declaration instanceof EImplicit_declaration) {
          implicitly_interfacing.add(interfacing_model);
        }
      }
    }

  }

  private boolean isAWrapper(ESchema_definition interfaced_schema, EInterface_specification specification, HashSet detect_loop) {
    if (!(specification instanceof EUse_from_specification)) {
      return false;
    }
    if (specification.testItems(null)) {
      return false;
    }
    ESchema_definition referenced_schema = (ESchema_definition) specification.getForeign_schema(null);
    if (referenced_schema == interfaced_schema) {
      return true;
    }
    if (!detect_loop.add(referenced_schema.getName(null))) {
      // loop detected
      return false;
    }
    SdaiModel next_model = referenced_schema.findEntityInstanceSdaiModel();
    AInterface_specification next_specifications = (AInterface_specification) next_model.getInstances(CInterface_specification.class);
    SdaiIterator specifications_it = next_specifications.createIterator();
    while (specifications_it.next()) {
      EInterface_specification next_specification = (EInterface_specification) next_specifications.getCurrentMemberObject(specifications_it);
      boolean result = isAWrapper(interfaced_schema, next_specification, detect_loop);
      if (result) {
        return true;
      }
    }
    return false;
  }

  private StringBuffer printInterfacedDeclarations(StringBuffer result, ESchema_definition schema) throws SdaiException {

    // RR - let's have only direct interfacing first, chained use froms discarded,
    // the specification information reflects the original use froms and reference froms from express
    // so, if there are several use froms from the same schema - we will have a line fro each one of them here.
    // we also would like to preserve the original order but it may not be possible

    int resultStart = result.length();
    SdaiModel model = schema.findEntityInstanceSdaiModel();
    AInterface_specification specifications = (AInterface_specification) model.getInstances(CInterface_specification.class);
    EInterface_specification[] specifications_set = new EInterface_specification[specifications.getMemberCount()];
    SdaiIterator specifications_it = specifications.createIterator();
    int iSSetCount = 0;

    while (specifications_it.next()) {
      EInterface_specification specification = (EInterface_specification) specifications.getCurrentMemberObject(specifications_it);
      specifications_set[iSSetCount] = specification;
      iSSetCount++;
    }
    Arrays.sort(specifications_set, new SpecSorterBySchema());

    for (int i = 0; i < iSSetCount; i++) {
      EInterface_specification specification = specifications_set[i];
      if (specification instanceof EUse_from_specification) {
        result.append("USE FROM ");
      }
      else if (specification instanceof EReference_from_specification) {
        result.append("REFERENCE FROM ");
      }
      String referenced_schema = specification.getForeign_schema(null).getName(null);
      printHRef(result, getStringOp(referenced_schema), getStringOp("../" + correctSchemaNameS(referenced_schema) + "/package-summary.html"));

      AInterfaced_declaration items = null;

      if (specification.testItems(null)) {
        items = specification.getItems(null);
      }
      if (items != null) {
        result.append(" (");
        printIsoNumberComment(result, referenced_schema);
        SdaiIterator items_it = items.createIterator();
        EDeclaration[] declarations_set = new EDeclaration[items.getMemberCount()];
        int iDSetCount = 0;
        while (items_it.next()) {
          EDeclaration item = (EDeclaration) items.getCurrentMemberObject(items_it);
          declarations_set[iDSetCount] = item;
          iDSetCount++;
        }
        Arrays.sort(declarations_set, new SorterByDefinition());

        boolean first_time = true;
        IterfacedDeclStringOp iterfacedDeclStringOp = new IterfacedDeclStringOp();
        IterfacedDeclRefOp iterfacedDeclRefOp = new IterfacedDeclRefOp();

        for (int j = 0; j < iDSetCount; j++) {
          if (first_time) {
            first_time = false;
            result.append(println()).append(printUnclosedTab());
          }
          else {
            result.append(",").append(println()).append(printUnclosedTab());
          }

          EInterfaced_declaration item = (EInterfaced_declaration) declarations_set[j];
          iterfacedDeclStringOp.declaration = item;
          iterfacedDeclRefOp.declaration = item;

          printHRef(result, iterfacedDeclStringOp, iterfacedDeclRefOp);

          /*
           * EEntity definition = item.getDefinition(null);
           * String item_name = null;
           * if (definition instanceof ENamed_type) {
           * item_name = ((ENamed_type)definition).getName(null);
           * } else
           * if (definition instanceof EAlgorithm_definition) {
           * item_name = ((EAlgorithm_definition)definition).getName(null);
           * } else
           * if (definition instanceof EGlobal_rule) {
           * item_name = ((EGlobal_rule)definition).getName(null);
           * }
           */

        }
        if (!first_time) {
          result.append(println());
        }

        result.append(");");
      }
      else {
        result.append(";");
        printIsoNumberComment(result, referenced_schema);
      }
      result.append(println());
    }
    if (resultStart != result.length()) {
      result.append(printBreak());
    }

    AInterfaced_declaration declarations = new AInterfaced_declaration();

    CInterfaced_declaration.usedinParent(null, schema, null, declarations);
    EDeclaration[] declarations_set = new EDeclaration[declarations.getMemberCount()];
    int iDSetCount = 0;
    SdaiIterator declarations_it = declarations.createIterator();

    while (declarations_it.next()) {
      declarations_set[iDSetCount] = (EDeclaration) declarations.getCurrentMemberObject(declarations_it);
      iDSetCount++;
    }
    Arrays.sort(declarations_set, new SorterBySchema());

    boolean first = true;
    boolean any = true;
    String actualSchemaName = "";

    IterfacedDeclStringOp iterfacedDeclStringOp = new IterfacedDeclStringOp();
    IterfacedDeclRefOp iterfacedDeclRefOp = new IterfacedDeclRefOp();
    for (int i = 0; i < iDSetCount; i++) {
      EInterfaced_declaration declaration = (EInterfaced_declaration) declarations_set[i];
      String newSchemaName = findSchemaForEntity(declaration.getDefinition(null)).getName(null);
      if (!newSchemaName.equals(actualSchemaName)) {
        if (!first) {
          result.append(println());
        }
        actualSchemaName = newSchemaName;
        printBold(result, schemaHRefandTargetOp(actualSchemaName));
        printTab(result, printIsoNumberOp(actualSchemaName));
        first = true;
        any = true;
      }
      if (any) {
        any = false;
      }
      else {
        result.append(",").append(println());
      }

      result.append(printUnclosedTab()).append("<tt>").append(getDictionaryEntityTypeRR2(declaration)).append(" </tt>");
      iterfacedDeclStringOp.declaration = declaration;
      iterfacedDeclRefOp.declaration = declaration;
      printHRef(result, iterfacedDeclStringOp, iterfacedDeclRefOp);

      first = any;
    }
    if (!first) {
      result.append(println());
    }

    result.append(printBreak());
    return result;
  }

  private static class IterfacedDeclStringOp implements StrOp {
    private EInterfaced_declaration declaration;

    public void op(StringBuffer result) throws SdaiException {
      if (declaration.testAlias_name(null)) {
        result.append(declaration.getAlias_name(null));
      }
      else {
        getComplexName(result, getDictionaryEntityNameOp(declaration.getDefinition(null)));
      }
    }
  }

  private static class IterfacedDeclRefOp implements StrOp {
    private EInterfaced_declaration declaration;
    private final SchNameOp schNameOp = new SchNameOp();

    public void op(StringBuffer result) throws SdaiException {
      schNameOp.definition = declaration.getDefinition(null);
      result.append("../S");
      getUpper(result, correctSchemaNameOp(schNameOp));
      result.append("/");
      getUpper(result, getComplexNameOp(getDictionaryEntityNameOp(schNameOp.definition)));
      result.append(".html");
    }

    private static class SchNameOp implements StrOp {
      private EEntity definition;

      public void op(StringBuffer result) throws SdaiException {
        result.append(findSchemaForEntity(definition).getName(null));
      }
    }
  }

  private String printComplexIndex(TreeSet index, String complex_title) throws SdaiException, IOException {
//		String title = "Merged AIM, IR, AIC, PLIB express schema index";
    String title = "Index";
    String fileName = "";
    int i = 0;
    f = new File(baseDir + File.separator + "Express-frame0");
    while (f.exists()) {
      i++;
      f = new File(baseDir + File.separator + "Express-frame" + String.valueOf(i));
    }
    String indexNameURL = f.getName() + "/index.html";
    String indexName = f.getName() + File.separator + "index.html";
    fileName = f.getName();
    f.mkdir();
    String letters = "";
    String s = "";
    letters += "<PRE>\n";
    letters += printHRef("All", "All.html") + " ";

    for (Iterator j = index.iterator(); j.hasNext(); ) {
      EEntity type = (EEntity) j.next();
      String sName = getDictionaryEntityName(type);
      if (!s.equalsIgnoreCase(sName.substring(0, 1))) {
        s = sName.substring(0, 1);
        letters += printHRef(s.toUpperCase(), s.toUpperCase() + ".html") + " ";
      }
    }

    letters += "</PRE>\n";
    s = "";
    f = new File(baseDir + File.separator + fileName + File.separator + "All" + ".html");
    fw = new FileWriter(f);
    PrintWriter allLetters = new PrintWriter(new BufferedWriter(fw));
    try {
      allLetters.print(printHtmlHead(title));
      allLetters.print(printTableHeader());
      allLetters.print(printH3(title));
      allLetters.print(letters);
      PrintWriter part = null;
      //String part = letters;
      try {
        for (Iterator j = index.iterator(); j.hasNext(); ) {
          EEntity type = (EEntity) j.next();
          String sTypeLetter = getDictionaryEntityType(type);
          String sTypeName = getDictionaryEntityName(type);

          if (!s.equalsIgnoreCase(sTypeName.substring(0, 1))) {
            if (part != null) {
              part.print(printTableTail());
              part.print(printHtmlTail());
              part.close();
            }
            s = sTypeName.substring(0, 1);
            f = new File(baseDir + File.separator + fileName + File.separator + s.toUpperCase() + ".html");
            fw = new FileWriter(f);
            part = new PrintWriter(new BufferedWriter(fw));
            part.print(printHtmlHead(title));
            part.print(printTableHeader());
            part.print(printH3(title));
            part.print(letters);
            part.print("<DT>" + s.toUpperCase() + "<BR>");
          }
          if (getComplexName(sTypeName).lastIndexOf("$") == (-1)) {

            String part_1 = printUnclosedTab("<TT>"
                + sTypeLetter
                + "</TT> "
                + printHRef(printCapitalCase(getComplexName(sTypeName)), "../jsdai/" + correctSchemaNameS(findSchemaForEntity(type)) + "/"
                + getUpper(getComplexName(sTypeName)) + ".html"));
            String part_2 = " (" + correctSchemaName(findSchemaForEntity(type)) + ")" + "<BR>\n";
            allLetters.print(part_1);
            allLetters.print(part_2);
            assert part != null;
            part.print(part_1);
            part.print(part_2);
          }
        }
      }
      finally {
        if (part != null) {
          part.print(printTableTail());
          part.print(printHtmlTail());
          part.close();
        }
      }
      f = new File(baseDir + File.separator + indexName);
      fw = new FileWriter(f);
      PrintWriter partIndex = new PrintWriter(new BufferedWriter(fw));
      try {
        partIndex.print(printHtmlHead(title));
        partIndex.print(printTableHeader());
        partIndex.print(printH3(title));
        partIndex.print(letters);
        partIndex.print(printTableTail());
        partIndex.print(printHtmlTail());
      }
      finally {
        partIndex.close();
      }
      insertLinkToFile(indexNameURL, complex_title);

      allLetters.print(printTableTail());
      allLetters.print(printHtmlTail());
    }
    finally {
      allLetters.close();
    }

    return letters;
  }

  /**
   * prints defined_type in one file with two parts: Express, Interface
   *
   * @param type
   * @param haveAggregate
   * @throws SdaiException
   * @throws IOException
   */
  private void printDefinedType(EDefined_type type, boolean haveAggregate) throws SdaiException, IOException {
    String schemaName = findSchemaForEntity(type).getName(null);
    f = new File(baseDir + File.separator + "jsdai" + File.separator + correctSchemaNameS(schemaName));
    if (!f.exists()) {
      f.mkdir();
    }
    f = new File(baseDir + File.separator + "jsdai" + File.separator + correctSchemaNameS(schemaName) + File.separator + getUpper(type.getName(null))
        + ".html");
    f.createNewFile();
    fw = new FileWriter(f);
    pw = new PrintWriter(fw, true);
    String partExpress = "";
    String partComments = "";
    String partInterface = "";
    String partAggregate = "";
    String partBasedOnSelects = "";
    String partBasedOnEnumerations = "";
    String partSelectWithItems = "";
    String partUsers = "";

    pw.print(printHtmlHead(printCapitalCase(type.getName(null))));
    pw.print(printNavBar());

    //partComments
    String doc = findDocFor(type);
    if (!doc.equals("")) {
      partComments += doc;
      partComments += printBreak();
    }

    //part Express
    if (doNotGenerateJava) {
      partExpress += printHRef(printH3ISO(correctSchemaName(schemaName)), "package-summary.html");
    }
    else {
      partExpress += printHRef(printH3(correctSchemaName(schemaName) + " (jsdai." + correctSchemaNameS(schemaName) + ")"), "package-summary.html");
    }

    partExpress += printBreak();
    partExpress += printTableHeader();
    partExpress += print("TYPE " + printBold(type.getName(null)) + "&nbsp;= ");
    EEntity domain = type.getDomain(null);
    if (domain instanceof ESelect_type || domain instanceof EEnumeration_type) {
      partExpress += printType(domain);
    }
    else {
      partExpress += printType(domain) + ";";
      partExpress += println();
    }
    partExpress += printWhere_rules(type); //--VV--030617--Added where rules for defined type--
    partExpress += println("END_TYPE; -- " + type.getName(null));
    partExpress += printTableTail();
    partExpress += printBreak();
    if (domain instanceof EEnumeration_type) {
      partInterface += printTableHeader();
      partInterface += printH3(printName("public final class E" + getUpper(type.getName(null)), "E" + getUpper(type.getName(null))));
      Vector ant = getElements((EEnumeration_type) domain);
      String s = "";
      boolean first = true;
      int i = 0;
      for (i = 0; i < ant.size(); i++) {
        partInterface += printTab("static final int " + ((String) ant.elementAt(i)).toUpperCase() + " = " + String.valueOf(i + 1));
        if (first) {
          s += "\"" + ((String) ant.elementAt(i)).toUpperCase() + "\"";
          first = false;
        }
        else {
          s += ", \"" + ((String) ant.elementAt(i)).toUpperCase() + "\"";
        }
      }
      partInterface += printTab("static String[] " + printBold("values") + " = {" + s + "}");
      partInterface += printTab("static int " + printBold("toInt") + "(String str)");
      partInterface += printTab("static String " + printBold("toString") + "(int value)");
      partInterface += printTableTail();
      partInterface += printBreak();
      if (domain instanceof EExtensible_enumeration_type) {
        partBasedOnEnumerations += printBasedOnEnumerations(type, domain, schemas);
      }
    }
    else if (!(domain instanceof ESelect_type)) {
      partInterface += printTableHeader();
      partInterface += printH3(printName("public interface E" + getUpper(type.getName(null)), "E" + getUpper(type.getName(null))));
      partInterface += printTableTail();
      partInterface += printBreak();
    }
    else {
      partBasedOnSelects += printBasedOnSelects(type, domain, schemas);
    }
    if (haveAggregate) {
      partAggregate += printTableHeader();
      partAggregate += printH3("public class A" + getUpper(type.getName(null)) + " implements " + printHRef("Aggregate", "../lang/Aggregate.html"));
      partAggregate += printAggregateForSelect((ESelect_type) domain, new MyInteger(2), new Vector());
      partAggregate += printTableTail();
      partAggregate += printBreak();
    }
    partUsers += printEntityUsers(type, schemas);
//		partSubtypes += printKnownSubtypes(type, schemas);
//		partRules += printGlobalRules(type, schemas);
    pw.print(partExpress);
    pw.print(partComments);
    pw.print(partBasedOnEnumerations);
    pw.print(partSelectWithItems);
    pw.print(partBasedOnSelects);
    pw.print(partUsers);
//		pw.print(partSubtypes);
//		pw.print(partRules);
    if (!doNotGenerateJava) {
      pw.print(partInterface);
      pw.print(partAggregate);
    }
    pw.print(printHtmlTail());
    pw.close();
  }

  private String printAggregateForSelect(ESelect_type st, MyInteger i, Vector nodes) throws SdaiException {
    String result = "";
    ANamed_type ant = st.getSelections(null);
    SdaiIterator it_ant = ant.createIterator();
    if ((countEntityInsideSelect(st) > 0)) {
      if ((nodes.size() > 0) && (nodes.elementAt(0) != null)) {
        result += printTab("case <b>s" + printNodes(nodes) + "</b>:"); //--VV--
      }
      else {
        result += printTab("case <b>1</b>:"); //--VV--
      }
      String valueType = printHRef("EEntity", "../lang/EEntity.html");
      String nodess = "";
      for (int k = 0; k < nodes.size(); k++) {
        ENamed_type named = (ENamed_type) nodes.elementAt(k);
        Vector temp = new Vector();
        temp.add(named);
        nodess += ", " + printJavaType(named, "", temp, FIRST_TYPE, 1) + " node" + String.valueOf(k + 1);
      }
      result += printTab("&nbsp; &nbsp; boolean " + printBold("isMember") + "(" + valueType + " value" + nodess + ")");
      result += printTab("&nbsp; &nbsp; " + valueType + printBold(" getByIndex") + "(int index" + nodess + ")");
      result += printTab("&nbsp; &nbsp; " + valueType + printBold(" getCurrentMember") + "(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html")
          + " iter" + nodess + ")");
      result += printTab("&nbsp; &nbsp; void " + printBold("setByIndex") + "(int index, " + valueType + " value" + nodess + ")");
      result += printTab("&nbsp; &nbsp; void " + printBold("setCurrentMember") + "(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter, "
          + valueType + " value" + nodess + ")");
      result += printTab("&nbsp; &nbsp; void " + printBold("addBefore") + "(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter, "
          + valueType + " value" + nodess + ")");
      result += printTab("&nbsp; &nbsp; void " + printBold("addAfter") + "(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter, "
          + valueType + " value" + nodess + ")");
      result += printTab("&nbsp; &nbsp; void " + printBold("addUnordered") + "(" + valueType + " value" + nodess + ")");
      result += printTab("&nbsp; &nbsp; void " + printBold("addByIndex") + "(int index, " + valueType + " value" + nodess + ")");
      result += printTab("&nbsp; &nbsp; void " + printBold("removeUnordered") + "(" + valueType + " value" + nodess + ")");

      i.integer++;
    }
    while (it_ant.next()) {
      Vector pathNodes = new Vector(nodes);
      ENamed_type nt = ant.getCurrentMember(it_ant);
      if (nt instanceof EDefined_type) {
        EDefined_type dt = (EDefined_type) nt;
        EEntity domain = dt.getDomain(null);
        if (!(domain instanceof ESelect_type)) {
          pathNodes.add(dt);
        }
        while (domain instanceof EDefined_type) {
          domain = ((EDefined_type) domain).getDomain(null);
        }
        if (domain instanceof ESelect_type) {
          result += printAggregateForSelect((ESelect_type) domain, i, pathNodes);
        }
        else {
          String nodess = "";
          for (int k = 0; k < pathNodes.size(); k++) {
            ENamed_type named = (ENamed_type) pathNodes.elementAt(k);
            Vector temp = new Vector();
            temp.add(named);
            nodess += ", " + printJavaType(named, "", temp, FIRST_TYPE, 1) + " node" + String.valueOf(k + 1);
          }
          result += printTab("case <b>s" + printNodes(pathNodes) + "</b>:"); //--VV--
          String valueType = printJavaType(dt, "", pathNodes, BASE_TYPE, 1);
          if (isAggregateInside(dt, pathNodes)) {
            result += printTab("&nbsp; &nbsp; boolean " + printBold("isMember") + "(" + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" getByIndex") + "(int index" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" getCurrentMember") + "("
                + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" createAggregateByIndex") + "(int index" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" createAggregateCurrentMember") + "("
                + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" createAggregateBefore") + "("
                + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" createAggregateAfter") + "("
                + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" createAggregateUnordered") + "(" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" createAggregateByIndex") + "(int index" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("removeUnordered") + "(" + nodess + ")");
          }
          else {
            result += printTab("&nbsp; &nbsp; boolean " + printBold("isMember") + "(" + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" getByIndex") + "(int index" + nodess + ")");
            result += printTab("&nbsp; &nbsp; " + valueType + printBold(" getCurrentMember") + "("
                + printHRef("SdaiIterator", "../lang/SdaiIterator.html") + " iter" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("setByIndex") + "(int index, " + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("setCurrentMember") + "(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html")
                + " iter, " + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("addBefore") + "(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html")
                + " iter, " + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("addAfter") + "(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html")
                + " iter, " + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("addUnordered") + "(" + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("addByIndex") + "(int index, " + valueType + " value" + nodess + ")");
            result += printTab("&nbsp; &nbsp; void " + printBold("removeUnordered") + "(" + valueType + " value" + nodess + ")");
          }
          i.integer++;
        }
      }
    }
    return result;
  }

  /**
   * prints entity with parts: Express, Comments, Partial, Subtypes, Iterface, Class, Aggregate
   *
   * @param entity
   * @param schema
   * @throws SdaiException
   * @throws IOException
   */
  private void printEntity(EEntity_definition entity, ESchema_definition schema) throws SdaiException, IOException {
    f = new File(correctSchemaNameFile(schemaName) + File.separator + getUpper(getComplexName(entity.getName(null))) + ".html");
    f.createNewFile();
    fw = new FileWriter(f);
    pw = new PrintWriter(fw, true);
    String partExpress = "", partComments = "", partPartial = "", partInterface = "", partClass = "", partAggregate = "", partSubtypes = "", partConstraints = "", partHeader = "", partRules = "", partUsers = "";
    pw.print(printHtmlHead(printCapitalCase(entity.getName(null))));
    partHeader += printNavBar();

    //Header
    if (doNotGenerateJava) {
      partHeader += printHRef(printH3ISO(correctSchemaName(schemaName)), "package-summary.html");
    }
    else {
      partHeader += printHRef(printH3(correctSchemaName(schemaName) + " (jsdai." + correctSchemaNameS(schemaName) + ")"), "package-summary.html");
    }
    partHeader += printBreak();

    //Express for entity
    partExpress += printTableHeader();
    partExpress += printExpressForEntity(entity);

    // Comments for entity
    partComments += printDocumentationForEntityDefinition(entity);

    // Partial entity data types
    partPartial += printTableHeader();
    partPartial += printH3("Entity data types and their attributes");
    if (entity.getComplex(null)) {
      partPartial += printPartialComplex(entity);
    }
    else {
      partPartial += printPartial(entity, null);
    }

    // Subtypes for entity
    partSubtypes += printSubtypes(entity, schema);

    partConstraints += printConstraints(entity, schema);

    //Interface for entity
    partInterface += printTableHeader();
    partInterface += printInterfaceForEntity(entity);

    // Class for entity
    partClass += printTableHeader();
    partClass += printClassForEntity(entity);

    // Aggregate for entity
    partAggregate += printTableHeader();
    partAggregate += printAggregateForEntity(entity);

    // loop through the attributes
    AExplicit_attribute attributes = getAllExplicit_attributes(entity);
    SdaiIterator attributes_it = attributes.createIterator();
    while (attributes_it.next()) {
      EExplicit_attribute attribute = attributes.getCurrentMember(attributes_it);

      // comments for attribute
      // seems that only for non-redeclaring is needed
      if (!attribute.testRedeclaring(null)) {
        partComments += printDocumentationForAttribute(attribute);
      }

      // Express of attribute
      partExpress += printExpressForAttribute(attribute);

      // Interface for attribute
      // seems that only for non-redeclaring is needed
      if (!attribute.testRedeclaring(null)) {
        partInterface += printInterfaceForAttribute(attribute);
        //class for attribute - NOTE - it is only if non-redeclaring for explicit, but for all attributes if derived !!! ???
        partClass += printClassForAttribute(attribute);
      }
      collectAggregates(attribute.getDomain(null), 0);
    }

    //RR implementing handling of derived attributes separately, and with support for the original order of attributes

    ADerived_attribute d_attributes = getAllDerived_attributes(entity);
    if (d_attributes.getMemberCount() > 0) {
      partExpress += println("DERIVE");
      SdaiIterator d_attributes_it = d_attributes.createIterator();
      while (d_attributes_it.next()) {
        EDerived_attribute d_attribute = d_attributes.getCurrentMember(d_attributes_it);
        collectAggregates(d_attribute.getDomain(null), 0);
        partComments += printDocumentationForAttribute(d_attribute);
        partExpress += printExpressForAttribute(d_attribute);
        if (!d_attribute.testRedeclaring(null)) {
          partInterface += printH4(printName(d_attribute.getName(null), d_attribute.getName(null)) + "(derived attribute)");
          partInterface += printTab(printMethodGet(d_attribute, null));
          partInterface += printTab(printMethodTest(d_attribute, false));
        }
        partClass += printClassForAttribute(d_attribute);
      }
    }

//RR implementing handling of inverse attributes separately, and with support for the original order of attributes

    AInverse_attribute i_attributes = getAllInverse_attributes(entity);
    if (i_attributes.getMemberCount() > 0) {
      partExpress += print("INVERSE<BR>");
      SdaiIterator i_attributes_it = i_attributes.createIterator();
      while (i_attributes_it.next()) {
        EInverse_attribute i_attribute = i_attributes.getCurrentMember(i_attributes_it);
        collectAggregates(i_attribute.getDomain(null), 0);
        EEntity_definition domain = i_attribute.getDomain(null);
        partComments += printDocumentationForAttribute(i_attribute);

        //express for inverse attribute
        String temp_inverse = "";
        String i_attribute_name = i_attribute.getName(null);
        if (testRedeclaring(i_attribute)) {
          String current_name = i_attribute.getName(null);
          String previous_name = getRedeclaring(i_attribute).getName(null);

          temp_inverse += "SELF\\" + getRedeclaring(i_attribute).getParent(null).getName(null) + ".";

          if (current_name.equalsIgnoreCase(previous_name)) {
            // not renamed, we are not interested if renamed even earlier
            temp_inverse += i_attribute_name + " : ";
          }
          else {
            // renamed
            temp_inverse += previous_name;
            temp_inverse += " RENAMED " + current_name + " : ";
          }
        }
        else {
          temp_inverse += i_attribute_name + " : ";
        }

        if (i_attribute.testMin_cardinality(null)) {
          temp_inverse += ((i_attribute.getDuplicates(null)) ? "BAG [" : "SET [") + printBound(i_attribute.getMin_cardinality(null)) + ":"
              + printBound((i_attribute.testMax_cardinality(null)) ? i_attribute.getMax_cardinality(null) : null) + "] OF ";
        }
        temp_inverse += printType(domain)
            + " FOR "
            + printHRef(i_attribute.getInverted_attr(null).getName(null), getSchemaNameIfDiffer(domain, schema) + getUpper(domain.getName(null))
            + ".html") + ";";
        partExpress += printTab(temp_inverse);

        partClass += printTab(printMethodAttribute(i_attribute));
        if (!i_attribute.testRedeclaring(null)) {
          partInterface += printH4(printName(i_attribute.getName(null), i_attribute.getName(null)) + " (inverse attribute)");
          partInterface += printTab(printJavaType(domain, "A", null, BASE_TYPE, 1) + printBold(" get") + getUpper(i_attribute.getName(null)) + "(E"
              + getUpper(entity.getName(null)) + " type, " + printHRef("ASdaiModel", "../lang/ASdaiModel.html") + " domain)");
        }
      }
    }

    partExpress += printUniqueness_rules(entity);
    partExpress += printWhere_rules(entity);
    partExpress += println("END_ENTITY; -- " + entity.getName(null));
    if (entity.testShort_name(null)) {
      partExpress += printBreak();
      partExpress += println("Express Short Name = " + entity.getShort_name(null));
    }
    partExpress += printTableTail();
    partExpress += printBreak();
    if (!partComments.equals("")) {
      partComments += printBreak();
    }
    partPartial += printTableTail();
    partPartial += printBreak();
    partInterface += printTableTail();
    partInterface += printBreak();
    partClass += printTableTail();
    partClass += printBreak();
    partAggregate += printTableTail();
    partAggregate += printBreak();
    pw.print(partHeader);
    if (!entity.getComplex(null)) {
      pw.print(partExpress);
      partUsers += printEntityUsers(entity, schemas);
      partRules += printGlobalRules(entity, schemas);
    }
    else {
      String partComplexExpress = "";
      partComplexExpress += println("Multi leaf complex entity data type");
      String devidedComplex = "";
      String names[] = devideComplexName(entity.getName(null));
      boolean first = true;
      for (int i = 0; i < names.length; i++) {
        String tmp = "";
        if (first) {
          first = false;
        }
        else {
          tmp = "+";
        }
        devidedComplex += tmp + printHRef(names[i], getSchemaNameIfDiffer(findInPartial(entity, names[i]), schema) + getUpper(names[i]) + ".html");
      }
      partComplexExpress += printTab(printBold(devidedComplex));
      partComplexExpress += printBreak();
      pw.print(partComplexExpress);
    }
    pw.print(partComments);
    pw.print(partPartial);
    pw.print(partSubtypes);
    pw.print(partConstraints);
    pw.print(partUsers);
    pw.print(partRules);
    if (!doNotGenerateJava) {
      if (!entity.getComplex(null)) {
        pw.print(partInterface);
        pw.print(partClass);
        pw.print(partAggregate);
      }
      else {
        pw.print(partClass);
      }
    }
    pw.print(printHtmlTail());
    pw.close();
  }

  private String printExpressForEntity(EEntity_definition definition) throws SdaiException {
    String result = "";
    boolean haveSubOrSup = false;
    boolean hasSupertypes = false;
    boolean hasSubtypes = false;

    // subtype
    AEntity supertypes = definition.getGeneric_supertypes(null);
    if (supertypes.getMemberCount() > 0) {
      haveSubOrSup = true;
      hasSupertypes = true;
      SdaiIterator iter1 = supertypes.createIterator();
      boolean first = true;
      String partSub = "";
      while (iter1.next()) {
        EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(iter1);
        if (first) {
          first = false;
        }
        else {
          partSub += ", ";
        }
        partSub += printHRef(supertype.getName(null), getSchemaNameIfDiffer(supertype, schema) + getUpper(supertype.getName(null)) + ".html");
      }
      // have to test for backward compatibility, at least temporarily, because sdai_dictionary_schema is old, etc.
      if (definition.testConnotational_subtype(null)) {

        if (definition.getConnotational_subtype(null)) {
          result += printTab("CONNOTATIONAL SUBTYPE OF (" + partSub + ");");
        }
        else {
          result += printTab("SUBTYPE OF (" + partSub + ");");
        }
      }
      else {
        result += printTab("SUBTYPE OF (" + partSub + ");");
      }
    }

    // supertype
    ASub_supertype_constraint subtypes = new ASub_supertype_constraint();

    CSub_supertype_constraint.usedinGeneric_supertype(null, definition, schemas, subtypes);
    String partSuper = "";
    if (subtypes.getMemberCount() > 0) {
      SdaiIterator it_subtypes = subtypes.createIterator();
      while (it_subtypes.next()) {
        if (subtypes.getCurrentMember(it_subtypes).testConstraint(null)) {
          if (subtypes.getCurrentMember(it_subtypes).testName(null)) {
            // this is a stand-alone subtype_constraint, should not be included into the entity itself
          }
          else {
            partSuper += printSubtypeConstraint(subtypes.getCurrentMember(it_subtypes).getConstraint(null));
            hasSubtypes = true;
            haveSubOrSup = true;
          }
        }
      }
    }

    /*
     * possible cases:
     * 1) has subtype_constraint and supertypes, generate SUPERTYPE OF () SUBTYPE OF ();
     * 2) has subtype_constraint but no supertypes, generate SUPERTYPE OF ();
     * 3) has no subtype_constraint but has supertypes, generate SUBTYPE OF ();
     * 4) has no subtype_constraint and no supertype_constraint, generate nothing
     */
    String temp_result = "";
    if (hasSubtypes) {
      if (!(definition.getInstantiable(null))) {
        temp_result += "ABSTRACT ";
      }
      temp_result += "SUPERTYPE OF (" + partSuper + ")";
      if (!hasSupertypes) {
        temp_result += ";";
      }
      result = printTab(temp_result) + result;
    }
    else {
      // may be abstract supertype
      if (!definition.getInstantiable(null)) {
        if (definition.testAbstract_entity(null)) { // mandatory but early implementations left it unset in the dictionary
          if (definition.getAbstract_entity(null)) {
            temp_result = "ABSTRACT";
          }
          else {
            temp_result = "ABSTRACT SUPERTYPE";
          }
        }
        else {
          temp_result = "ABSTRACT SUPERTYPE";
        }
        if (!hasSupertypes) {
          temp_result += ";";
        }
        result = printTab(temp_result) + result;
      }
    }
    result = println("ENTITY " + printBold(definition.getName(null)) + ((haveSubOrSup) ? "" : ";")) + result;
    return result;
  }

  private String printSubtypeConstraint(ESubtype_expression expression) throws SdaiException {
    String result = "";
    if (expression.getGeneric_operands(null).getMemberCount() > 1) {
      if (expression instanceof EOneof_subtype_expression) {
        result += "ONEOF (";
        result += printOperands(expression, ", ");
        result += ")";
      }
      else if (expression instanceof EAndor_subtype_expression) {
        result += "(";
        result += printOperands(expression, " ANDOR ");
        result += ")";
      }
      else if (expression instanceof EAnd_subtype_expression) {
        result += "(";
        result += printOperands(expression, " AND ");
        result += ")";
      }
    }
    else {
      result += printOperands(expression, "");
    }
    return result;
  }

  private String printOperands(ESubtype_expression expression, String separator) throws SdaiException {
    String result = "";
    AEntity oper = expression.getGeneric_operands(null);
    SdaiIterator it_oper = oper.createIterator();
    boolean first = true;
    while (it_oper.next()) {
      if (first) {
        first = false;
      }
      else {
        result += separator;
      }
      EEntity select = oper.getCurrentMemberEntity(it_oper);
      if (select instanceof EEntity_definition) {
        EEntity_definition entity = (EEntity_definition) select;
        result += printHRef(entity.getName(null), getSchemaNameIfDiffer(entity, schema) + getUpper(entity.getName(null)) + ".html");
      }
      else if (select instanceof ESubtype_expression) {
        result += printSubtypeConstraint((ESubtype_expression) select);
      }
    }
    return result;
  }

  static private String replaceAll(String text, String original, String replacement) {
    int curr_pos = 0;
    int find_pos = 0;
    StringBuffer sb = new StringBuffer();

    while (find_pos >= 0) {
      find_pos = text.indexOf(original, curr_pos);
      if (find_pos >= 0) {
        sb.append(text.substring(curr_pos, find_pos)).append(replacement);
        curr_pos = find_pos + original.length();
      }
      else {
        sb.append(text.substring(curr_pos));
      }
    }
    return sb.toString();
  }

  static private String replaceAll2(String text, String original, String replacement) {
    String original2 = " " + original + " ";
    String replacement2 = " " + replacement + " ";
    int curr_pos = 0;
    int find_pos = 0;
    StringBuffer sb = new StringBuffer();
    String text_l = new String(text);
    String true_original = "";
    text_l = text_l.toLowerCase();

    while (find_pos >= 0) {
      find_pos = text_l.indexOf(original2, curr_pos);
      if (find_pos >= 0) {
        true_original = text.substring(find_pos, find_pos + original2.length());
        if (found_not_in_string(find_pos, text)) {
          sb.append(text.substring(curr_pos, find_pos)).append(replacement2);
          curr_pos = find_pos + replacement2.length() - 1;
        }
        else {
          sb.append(text.substring(curr_pos, find_pos)).append(true_original);
          curr_pos = find_pos + original2.length() - 1;
        }
      }
      else {
        if (curr_pos > 0) {
          sb.append(text.substring(curr_pos + 1));
        }
        else {
          sb.append(text.substring(curr_pos));
        }
      }
    }
    return sb.toString();
  }

  static private boolean found_not_in_string(int match_pos, String text) {
    int curr_pos = 0;
    int find_pos = 0;
    int quote_count = 0;
    if (match_pos < 0) {
      return false;
    }
    for (; ; ) {
      find_pos = text.indexOf("\'", curr_pos);
      if ((find_pos >= 0) && (find_pos < match_pos)) {
        quote_count++;
        curr_pos = find_pos + 1;
      }
      else {
        break;
      }
    }
    if ((quote_count % 2) == 0) {
      return true;
    }
    else {
      return false;
    }
  }

  private String printExpressForAttribute(EAttribute attribute) throws SdaiException {
    String result = "";
    String attribute_name = attribute.getName(null);
    EEntity domain = getAttrType(attribute);
    if (testRedeclaring(attribute)) {
      result += "SELF\\" + getRedeclaring(attribute).getParent(null).getName(null) + ".";
      // see if it is renamed
      String current_name = attribute.getName(null);
      String previous_name = getRedeclaring(attribute).getName(null);
      if (current_name.equalsIgnoreCase(previous_name)) {
        // not renamed, we are not interested if renamed even earlier
        result += attribute_name + " : ";
      }
      else {
        // renamed
        result += previous_name;
        result += " RENAMED " + current_name + " : ";
      }
    }
    else {
      result += attribute_name + " : ";
    }

    if (attribute instanceof EExplicit_attribute) {
      EExplicit_attribute ea = (EExplicit_attribute) attribute;
      result += (ea.getOptional_flag(null) ? "OPTIONAL " : "");
    }
    result += printType(domain);
    if (attribute instanceof EDerived_attribute) {
      EDerived_attribute da = (EDerived_attribute) attribute;
      AEntity aeUsers = new AEntity();
      da.findEntityInstanceUsers(asmExpressDomain, aeUsers);
      SdaiIterator iter_users = aeUsers.createIterator();
      result += " := ";
      while (iter_users.next()) {
        EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
        if (eUser instanceof EExpress_code) {
          EExpress_code ec = (EExpress_code) eUser;
          A_string asECValues = ec.getValues(null);
          SdaiIterator iter_ecv = asECValues.createIterator();
          while (iter_ecv.next()) {
            String sEC = format(asECValues.getCurrentMember(iter_ecv));
            for (Iterator iter_ad = vAlgorithmDefinition.iterator(); iter_ad.hasNext(); ) {
              EAlgorithm_definition ad = (EAlgorithm_definition) iter_ad.next();
              sEC = replaceAll(sEC, ad.getName(null) + " (",
                  printHRef(ad.getName(null), getSchemaNameIfDiffer(ad, schema) + getUpper(ad.getName(null)) + ".html") + " (");
            }
            result += sEC;
          }
        }
      }
    }
    result += ";";
    return printTab(result);
  }

  private String printDocumentationForEntity(EEntity entity) throws SdaiException {
    return findDocFor(entity);
  }

  private String printDocumentationForEntityDefinition(EEntity_definition definition) throws SdaiException {
    return findDocFor(definition);
  }

  private String printDocumentationForAttribute(EAttribute attribute) throws SdaiException {
    String result = findDocFor(attribute);
    if (!result.equals("")) {
      return printTab(printBold(attribute.getName(null) + ": ") + result);
    }
    return "";
  }

  private String printInterfaceForEntity(EEntity_definition definition) throws SdaiException {
    String result = "";
    result += printName("public interface E" + getUpper(definition.getName(null)) + " extends ", "E" + getUpper(definition.getName(null)));
    AEntity supertypes = definition.getGeneric_supertypes(null);
    if (supertypes.getMemberCount() > 0) {
      SdaiIterator iter1 = supertypes.createIterator();
      String tempInter = "";
      boolean first = true;
      while (iter1.next()) {
        EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(iter1);
        if (first) {
          first = false;
        }
        else {
          tempInter = ", ";
        }
        result += tempInter
            + printHRef("E" + getUpper(supertype.getName(null)), getSchemaNameIfDiffer(supertype, schema) + getUpper(supertype.getName(null))
            + ".html" + "#E" + getUpper(supertype.getName(null)));
      }
    }
    else {
      result += printHRef("EEntity", "../lang/EEntity.html#EEntity");
    }
    return printH3(result);
  }

  private String printInterfaceForAttribute(EAttribute attribute) throws SdaiException {
    String result = "";
    EEntity domain = getAttrType(attribute);
    result += printH4(printName(attribute.getName(null), attribute.getName(null)) + "(explicit attribute)");
    ESelect_type st = isSelectInside(domain);
    if (st == null) {
      result += printTab(printMethodTest(attribute, false));
      result += printTab(printMethodGet(attribute, null));
      result += printTab(printMethodSet(attribute, null));
      result += printTab(printMethodUnset(attribute));
    }
    else {
      Vector nodes = new Vector();
      MyInteger i = new MyInteger(2);
      result += printTab(printMethodTest(attribute, true));
      result += printInterfaceForAttributeSelect(attribute, st, i, nodes);
      result += printTab(printMethodUnset(attribute));
    }
    return result;
  }

  private String printInterfaceForAttributeSelect(EAttribute attribute, ESelect_type st, MyInteger i, Vector nodes) throws SdaiException {
    String result = "";
    ANamed_type ant = st.getSelections(null);
    SdaiIterator it_ant = ant.createIterator();
    if ((countEntityInsideSelect(st) > 0)) {// && (nodes.size() > 0)
      Vector pathNodes = new Vector(nodes);
      pathNodes.addElement(null);
      if ((pathNodes.size() > 0) && (pathNodes.elementAt(0) != null)) {
        result += printTab("case <b>s" + getUpper(attribute.getName(null)) + printNodes(pathNodes) + "</b>"); //--VV--
      }
      else {
        result += printTab("case <b>1</b>"); //--VV--
      }
      result += printTab("&nbsp; &nbsp; " + printMethodGet(attribute, pathNodes));
      result += printTab("&nbsp; &nbsp; " + printMethodSet(attribute, pathNodes));
      i.integer++;
    }
    while (it_ant.next()) {
      Vector pathNodes = new Vector(nodes);
      ENamed_type nt = ant.getCurrentMember(it_ant);
      if (nt instanceof EDefined_type) {
        EDefined_type dt = (EDefined_type) nt;
        EEntity domain = dt.getDomain(null);
        if (!(domain instanceof ESelect_type)) {
          pathNodes.add(dt);
        }
        while (domain instanceof EDefined_type) {
          domain = ((EDefined_type) domain).getDomain(null);
        }
        if (domain instanceof ESelect_type) {
          result += printInterfaceForAttributeSelect(attribute, (ESelect_type) domain, i, pathNodes);
        }
        else {
          result += printTab("case <b>s" + getUpper(attribute.getName(null)) + printNodes(pathNodes) + ":</b>"); // --VV--
          result += printTab("&nbsp; &nbsp; " + printMethodGet(attribute, pathNodes));
          result += printTab("&nbsp; &nbsp; " + printMethodSet(attribute, pathNodes));
          i.integer++;
        }
      }
    }
    return result;
  }

  private int countEntityInsideSelect(ESelect_type st) throws SdaiException {
    int count = 0;
    ANamed_type selects = st.getSelections(null);
    SdaiIterator it_selects = selects.createIterator();
    while (it_selects.next()) {
      ENamed_type type = selects.getCurrentMember(it_selects);
      if (type instanceof EEntity_definition) {
        count++;
      }
    }
    return count;
  }

  private String printNodes(Vector nodes) throws SdaiException {
    String result = "";
    if (nodes == null) {
      return "";
    }
    for (int j = 0; j < nodes.size(); j++) {
      EDefined_type node = (EDefined_type) nodes.elementAt(j);
      result += getUpper((node != null) ? node.getName(null) : "Entity");
    }
    return result;
  }

  private String printClassForEntity(EEntity_definition definition) throws SdaiException {
    String result = "public class C" + getUpper(getComplexName(definition.getName(null))) + " ";
    result += "extends ";
    if (!definition.getComplex(null)) {

      AEntity supertypes = definition.getGeneric_supertypes(null);
      if (supertypes.getMemberCount() > 0) {
        SdaiIterator iter1 = supertypes.createIterator();
        while (iter1.next()) {
          EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(iter1);
          result += printHRef("C" + getUpper(supertype.getName(null)), getSchemaNameIfDiffer(supertype, schema) + getUpper(supertype.getName(null))
              + ".html" + "#C" + getUpper(supertype.getName(null)));
          break;
        }
      }
      else {
        result += printHRef("CEntity", "../lang/CEntity.html#CEntity");
      }

      result += " implements E" + getUpper(definition.getName(null));
    }
    else {
      result += printHRef("CEntity", "../lang/CEntity.html#CEntity");
      result += " implements ";
      AEntity supertypes = definition.getGeneric_supertypes(null);
      if (supertypes.getMemberCount() > 0) {
        SdaiIterator iter1 = supertypes.createIterator();
        String temp = "";
        boolean first = true;
        while (iter1.next()) {
          EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(iter1);
          if (first) {
            first = false;
          }
          else {
            temp = ", ";
          }
          result += temp
              + printHRef("E" + getUpper(supertype.getName(null)), getSchemaNameIfDiffer(supertype, schema) + getUpper(supertype.getName(null))
              + ".html" + "#E" + getUpper(supertype.getName(null)));
        }
      }
    }
    return printH3(result);
  }

  private String printClassForAttribute(EAttribute attribute) throws SdaiException {
    String result = "";
    result += printTab(printMethodAttribute(attribute));
    String temp = printMethodUsedin(attribute);
    result += (temp.equals("")) ? "" : printTab(temp);
    return result;
  }

  private String printAggregateForEntity(EEntity_definition definition) throws SdaiException {
    String result = "";
    result += printH3(printName("public class A" + getUpper(definition.getName(null)) + " extends " + printHRef("AEntity", "../lang/AEntity.html"), "E"
        + getUpper(definition.getName(null))));
    result += printTab("public E" + getUpper(definition.getName(null)) + " getByIndex(int index)");
    result += printTab("public E" + getUpper(definition.getName(null)) + " getCurrentMember(" + printHRef("SdaiIterator", "../lang/SdaiIterator.html")
        + " it)");
    return result;
  }

  private String printMethodGet(EAttribute attribute, Vector nodes) throws SdaiException {
    String result = "";
    EEntity_definition definition = (EEntity_definition) attribute.getParent(null);
    EEntity domain = getAttrType(attribute);
    result += printJavaType(domain, "", nodes, BASE_TYPE, 1) + printBold(" get") + getUpper(attribute.getName(null)) + "(E"
        + getUpper(definition.getName(null)) + " type";
    if (nodes != null) {
      for (int i = 0; i < nodes.size(); i++) {
        ENamed_type named = (ENamed_type) nodes.elementAt(i);
        if (named != null) {
          Vector temp = new Vector();
          temp.add(named);
          result += ", " + printJavaType(named, "", temp, FIRST_TYPE, 1) + " node" + String.valueOf(i + 1);
        }
      }
    }
    result += ")";
    return result;
  }

  private String printMethodSet(EAttribute attribute, Vector nodes) throws SdaiException {
    String result = "";
    String nodess = "";
    EEntity_definition definition = (EEntity_definition) attribute.getParent(null);
    EEntity domain = getAttrType(attribute);
    if (nodes != null) {
      for (int i = 0; i < nodes.size(); i++) {
        ENamed_type named = (ENamed_type) nodes.elementAt(i);
        Vector temp = new Vector();
        temp.add(named);
        nodess += ", " + printJavaType(named, "", temp, FIRST_TYPE, 1) + " node" + String.valueOf(i + 1);
      }
    }
    if (isAggregateInside(domain, nodes)) {
      result += printJavaType(domain, "", nodes, BASE_TYPE, 1) + printBold(" create") + getUpper(attribute.getName(null)) + "(E"
          + getUpper(definition.getName(null)) + " type";
    }
    else {
      result += "void " + printBold("set") + getUpper(attribute.getName(null)) + "(E" + getUpper(definition.getName(null)) + " type, "
          + printJavaType(domain, "", nodes, BASE_TYPE, 1) + " value";
    }
    result += nodess + ")";
    return result;
  }

  private String printMethodTest(EAttribute attribute, boolean return_type) throws SdaiException {
    EEntity_definition definition = (EEntity_definition) attribute.getParent(null);
    return ((return_type) ? "int " : "boolean ") + printBold("test") + getUpper(attribute.getName(null)) + "(E" + getUpper(definition.getName(null))
        + " type)";
  }

  private String printMethodUnset(EAttribute attribute) throws SdaiException {
    EEntity_definition definition = (EEntity_definition) attribute.getParent(null);
    return "void " + printBold("unset") + getUpper(attribute.getName(null)) + "(E" + getUpper(definition.getName(null)) + " type)";
  }

  private String printMethodUsedin(EAttribute attribute) throws SdaiException {
    String result = "";
    EEntity_definition definition = (EEntity_definition) attribute.getParent(null);
    EEntity domain = getAttrType(attribute);
    result += "static  int " + printBold("usedin") + getUpper(attribute.getName(null)) + "(E" + getUpper(definition.getName(null)) + " type, ";
    while ((domain instanceof EAggregation_type) || (domain instanceof EDefined_type)) {
      if (domain instanceof EAggregation_type) {
        domain = ((EAggregation_type) domain).getElement_type(null);
      }
      else if (domain instanceof EDefined_type) {
        domain = ((EDefined_type) domain).getDomain(null);
      }
    }
    if (domain instanceof ESelect_type) {
      result += printHRef("EEntity", "../lang/EEntity.html");
    }
    else if (domain instanceof EEntity_definition) {
      result += printJavaType(domain, "", null, BASE_TYPE, 1);
    }
    else {
      return "";
    }
    result += " instance, " + printHRef("ASdaiModel", "../lang/ASdaiModel.html") + " domain, A" + getUpper(definition.getName(null)) + " result)";
    return result;
  }

  private String printMethodAttribute(EAttribute attribute) throws SdaiException {
    EEntity_definition definition = (EEntity_definition) attribute.getParent(null);
    return "static EAttribute " + printBold("attribute") + getUpper(attribute.getName(null)) + "(E" + getUpper(definition.getName(null)) + " type)";
  }

  /**
   * printing uniqueness rules
   *
   * @param entity
   * @return
   * @throws SdaiException
   */
  private String printUniqueness_rules(EEntity_definition entity) throws SdaiException {
    String result = "";
    AUniqueness_rule uniques = entity.getUniqueness_rules(null, null);
    if (uniques.getMemberCount() > 0) {
      result += println("UNIQUE");
      SdaiIterator uniques_it = uniques.createIterator();
      while (uniques_it.next()) {
        EUniqueness_rule unique = uniques.getCurrentMember(uniques_it);
        String temp = ((unique.testLabel(null)) ? unique.getLabel(null) + " : " : "");
        AAttribute attributes = unique.getAttributes(null);
        SdaiIterator attributes_it = attributes.createIterator();
        boolean first = true;
        while (attributes_it.next()) {
          EAttribute attribute = attributes.getCurrentMember(attributes_it);
          if (first) {
            first = false;
          }
          else {
            temp += ", ";
          }
          EEntity_definition attr_parent = attribute.getParent_entity(null);
          if (attr_parent != entity) {
            temp += "SELF\\"
                + print(printHRef(attr_parent.getName(null), getSchemaNameIfDiffer(attr_parent, schema) + getUpper(attr_parent.getName(null))
                + ".html")) + "." + attribute.getName(null);
          }
          else {
            temp += attribute.getName(null);
          }
        }
        result += printTab(temp + ";");
      }
      //result += println("");
    }
    return result;
  }

  /**
   * printing where rules
   *
   * @param entity
   * @return
   * @throws SdaiException
   */
  private String printWhere_rules(EEntity entity) throws SdaiException {
    String result = "";

    // RR - adding the second null parameter - domain. Have to investigate, null may be wrong.
    // the first impression - current model is OK, where rules of this entity only.
    if (entity instanceof ENamed_type || entity instanceof EGlobal_rule) {
      AWhere_rule wheres = new AWhere_rule();
      if (entity instanceof ENamed_type) {
        ENamed_type nt = (ENamed_type) entity;
        wheres = nt.getWhere_rules(null, null);
      }
      if (entity instanceof EGlobal_rule) {
        EGlobal_rule gr = (EGlobal_rule) entity;
        wheres = gr.getWhere_rules(null, null);
      }
      if (wheres.getMemberCount() > 0) {
        result += println("WHERE");

        // print in the order defined by order attribute, perhaps sort first

        SortWhereRules swr = new SortWhereRules();
        TreeSet sorted_wrules = new TreeSet(swr);
        SdaiIterator wheres_it = wheres.createIterator();
        while (wheres_it.next()) {
          EWhere_rule whererule = wheres.getCurrentMember(wheres_it);
          sorted_wrules.add(whererule);
        }

        Iterator iter_swr = sorted_wrules.iterator();
        while (iter_swr.hasNext()) {
          EWhere_rule where = (EWhere_rule) iter_swr.next();
          result += "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " + (where.testLabel(null) ? where.getLabel(null) + ": " : "");
          AEntity aeUsers = new AEntity();
          where.findEntityInstanceUsers(asmExpressDomain, aeUsers);
          SdaiIterator iter_users = aeUsers.createIterator();
          while (iter_users.next()) {
            EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
            if (eUser instanceof EExpress_code) {
              EExpress_code ec = (EExpress_code) eUser;
              A_string asECValues = ec.getValues(null);
              SdaiIterator iter_ecv = asECValues.createIterator();
              while (iter_ecv.next()) {
                result += format(asECValues.getCurrentMember(iter_ecv));
              }
            }
          }
          result += ";<BR>";
        }
      }
    }
    return result;
  }

  /**
   * prints whatever express type, which exist in dictionary
   *
   * @param type
   * @return
   * @throws SdaiException
   */
  private String printType(EEntity type) throws SdaiException {
    String partType = "";
    if (type instanceof EAggregation_type) {
      EAggregation_type at = (EAggregation_type) type;
      boolean fUnique = false;
      boolean fOptional = false;
      EBound bound1 = null;
      EBound bound2 = null;
      if (type instanceof EVariable_size_aggregation_type) {
        EVariable_size_aggregation_type vt = (EVariable_size_aggregation_type) type;
        bound1 = vt.getLower_bound(null);
        if (vt.testUpper_bound(null)) {
          bound2 = vt.getUpper_bound(null);
        }
        if (type instanceof ESet_type) {
          partType += print("SET [");
        }
        else if (type instanceof EBag_type) {
          partType += print("BAG [");
        }
        else if (type instanceof EList_type) {
          EList_type lt = (EList_type) type;
          fUnique = lt.getUnique_flag(null);
          partType += print("LIST [");
        }
      }
      else if (type instanceof EArray_type) {
        EArray_type rt = (EArray_type) type;
        fUnique = rt.getUnique_flag(null);
        fOptional = rt.getOptional_flag(null);
        bound1 = rt.getLower_index(null);
        if (rt.testUpper_index(null)) {
          bound2 = rt.getUpper_index(null);
        }
        partType += print("ARRAY [");
      }
      else { // --VV--
        partType += print("AGGREGATE");
      }
      if (bound1 != null || bound2 != null) {
        partType += printBound(bound1);
        partType += print(":");
        partType += printBound(bound2);
        partType += print("]");
      }
      partType += print(" OF ");
      if (fOptional) {
        partType += print(" OPTIONAL ");
      }
      if (fUnique) {
        partType += print(" UNIQUE ");
      }
      partType += printType(at.getElement_type(null));
    }
    else if (type instanceof ENamed_type) {
      partType += print(printHRef(((ENamed_type) type).getName(null), getSchemaNameIfDiffer(type, schema) + getUpper(((ENamed_type) type).getName(null))
          + ".html"));
    }
    else if (type instanceof ENumber_type) {
      partType += print("NUMBER");
    }
    else if (type instanceof EInteger_type) {
      partType += print("INTEGER");
    }
    else if (type instanceof EReal_type) {
      partType += print("REAL");
    }
    else if (type instanceof EBoolean_type) {
      partType += print("BOOLEAN");
    }
    else if (type instanceof ELogical_type) {
      partType += print("LOGICAL");
    }
    else if (type instanceof EBinary_type) {
      partType += print("BINARY");
    }
    else if (type instanceof EString_type) {
      partType += print("STRING");
    }
    else if (type instanceof ESelect_type) {

      // RR: perhaps here also add support for extensible select types
      if (type instanceof EExtensible_select_type) {
        partType += print("EXTENSIBLE ");
      }
      if (type instanceof EEntity_select_type) {
        partType += print("GENERIC_ENTITY ");
      }
      partType += print("SELECT ");
      if (type instanceof EExtended_select_type) {
        // probably need to generate reference, not just the name?
        EDefined_type def = ((EExtended_select_type) type).getIs_based_on(null);
        partType += print("BASED_ON ");
        if (def != null) {
          partType += print(printHRef(def.getName(null), getSchemaNameIfDiffer(def, schema) + getUpper(def.getName(null)) + ".html"));
        }
        else {
          // report error, perhaps
        }
      }
      if (((ESelect_type) type).testLocal_selections(null)) {
        ANamed_type nts = ((ESelect_type) type).getLocal_selections(null);
        if (nts.getMemberCount() > 0) {
          if (type instanceof EExtended_select_type) {
            partType += print(" WITH ");
          }
          partType += println("(");
          ANamed_type ant = ((ESelect_type) type).getSelections(null);
          SdaiIterator it = ant.createIterator();
          if (it.next()) {
            boolean fGoOn = true;
            while (fGoOn) {
              String temp = "";
              ENamed_type nt = ant.getCurrentMember(it);
              temp += print(printHRef(nt.getName(null), getSchemaNameIfDiffer(nt, schema) + getUpper(nt.getName(null)) + ".html"));
              if (it.next()) {
                temp += print(",");
              }
              else {
                //temp += print(")");
                temp += print(");");
                fGoOn = false;
              }
              partType += printTab(temp);
            }
          }
          else {
            //partType += print(")");
            partType += print(");");
          }

        }
        else {
          partType += println(";");
        }

      }
      else {
        partType += println(";");
      }
    }
//####################################################################333
    else if (type instanceof EEnumeration_type) {
      // RR: add support for extensible enumeration types?

      if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
        partType += print("EXTENSIBLE ENUMERATION BASED_ON ");
        EDefined_type based_on = ((EExtended_enumeration_type) type).getIs_based_on(null);
        partType += print(printHRef(based_on.getName(null), getSchemaNameIfDiffer(based_on, schema) + getUpper(based_on.getName(null)) + ".html"));
      }
      else if (type instanceof EExtensible_enumeration_type) {
        partType += print("EXTENSIBLE ENUMERATION");
      }
      else if (type instanceof EExtended_enumeration_type) {
        partType += print("ENUMERATION BASED_ON ");
        EDefined_type based_on = ((EExtended_enumeration_type) type).getIs_based_on(null);
        partType += print(printHRef(based_on.getName(null), getSchemaNameIfDiffer(based_on, schema) + getUpper(based_on.getName(null)) + ".html"));
      }
      else { // just a regular enumeration
        partType += print("ENUMERATION OF (");
      }

      A_string ant = ((EEnumeration_type) type).getElements(null);
      int iDim = ant.getMemberCount();
      if (iDim >= 1) {

        if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
          partType += print(" WITH (");
        }
        else if (type instanceof EExtensible_enumeration_type) {
          partType += print(" OF (");
        }
        else if (type instanceof EExtended_enumeration_type) {
          partType += print(" WITH (");
        }
        else {
        }

        partType += println();
        int i = 1;
        while (i <= iDim) {
          String temp = "";
          temp += print(ant.getByIndex(i));
          if (i < iDim) {
            temp += print(",");
          }
          else {
            temp += print(" );");
          }
          i++;
          partType += printTab(temp);
        }
      }
      else {
        if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
          partType += print(";\n");
        }
        else if (type instanceof EExtensible_enumeration_type) {
          partType += print(";\n"); // for regular enumeration with have println to handle new line
        }
        else if (type instanceof EExtended_enumeration_type) {
          partType += print(";\n");
        }
        else {
          partType += print(" );");
        }
      }
    }
    else {
      partType += print("GENERIC");
    }
    return partType;
  }

  /**
   * print boud of aggregation type
   *
   * @param bound
   * @return
   * @throws SdaiException
   */
  private String printBound(EBound bound) throws SdaiException {
    String partBound = "";
    if (bound == null) {
      partBound += print("?");
    }
    else if (bound instanceof EInteger_bound) {
      partBound += print(String.valueOf(((EInteger_bound) bound).getBound_value(null)));
    }
    else {
      String partBound2 = null;
      AEntity aeUsers = new AEntity();
      bound.findEntityInstanceUsers(asmExpressDomain, aeUsers);
      SdaiIterator iter_users = aeUsers.createIterator();
      while (iter_users.next()) {
        EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
        if (eUser instanceof EExpress_code) {
          EExpress_code ec = (EExpress_code) eUser;
          A_string asECValues = ec.getValues(null);
          SdaiIterator iter_ecv = asECValues.createIterator();
          while (iter_ecv.next()) {
            String expr = asECValues.getCurrentMember(iter_ecv);
            partBound2 = print(format(expr));
            break;
          }
        }
      }
      if (partBound2 == null) {
        partBound += print("??");
      }
      else {
        partBound += partBound2;
      }

    }
    return partBound;
  }

  private void printSubtypeConstraint(ESub_supertype_constraint constraint, ESchema_definition schema) throws SdaiException, IOException {
    f = new File(correctSchemaNameFile(schemaName) + File.separator + getUpper(getComplexName(constraint.getName(null))) + ".html");
    f.createNewFile();
    fw = new FileWriter(f);
    pw = new PrintWriter(fw, true);
    String partHeader = "", partExpress = "", partClass = "", partComments = "";
    pw.print(printHtmlHead(printCapitalCase(constraint.getName(null))));
    partHeader += printNavBar();

    //Header
    partHeader += printHRef(printH3(correctSchemaName(schemaName) + " (jsdai." + correctSchemaNameS(schemaName) + ")"), "package-summary.html");
    partHeader += printBreak();

    //Express for subtype constraint
    partExpress += printTableHeader();
    partExpress += printExpressForSubtypeConstraint(constraint);
    partExpress += printTableTail();
    partExpress += printBreak();

    //Comments for subtype_constraint
    partComments += printDocumentationForEntity(constraint);
    if (!partComments.equals("")) {
      partComments += printBreak();
    }

    pw.print(partHeader);
    pw.print(partExpress);
    pw.print(partClass);
    pw.print(partComments);
    pw.print(printHtmlTail());
    pw.close();
  }

  private String printExpressForSubtypeConstraint(ESub_supertype_constraint constraint) throws SdaiException {
    String result = "";
    String parent_entity = "";
    EEntity_definition ed = (EEntity_definition) constraint.getGeneric_supertype(null);
    parent_entity += printHRef(ed.getName(null), getSchemaNameIfDiffer(ed, schema) + getUpper(ed.getName(null)) + ".html");
    result = println("SUBTYPE_CONSTRAINT " + printBold(constraint.getName(null)) + "&nbsp;FOR " + parent_entity + ";");

    // here we can reuse the same method that prints subtype constraints in entities
    if (constraint.testAbstract_supertype(null)) { // it is mandatory, but just in case
      if (constraint.getAbstract_supertype(null)) {
        result += printTab("ABSTRACT SUPERTYPE;");
      }
    }

    ESubtype_expression expression = null;
    if (constraint.testConstraint(null)) {
      expression = constraint.getConstraint(null);
    }

    if (expression != null) {
      String temp_result = printSubtypeConstraint(expression);
      temp_result += println(";");
      result += printUnclosedTab(temp_result);
    }
    result += println("END_SUBTYPE_CONSTRAINT; -- " + constraint.getName(null));
    return result;
  }

  /**
   * prints global rule
   *
   * @param rule
   * @param schema
   * @throws SdaiException
   * @throws IOException
   */
  private void printGlobalRule(EGlobal_rule rule, ESchema_definition schema) throws SdaiException, IOException {
    f = new File(correctSchemaNameFile(schemaName) + File.separator + getUpper(getComplexName(rule.getName(null))) + ".html");
    f.createNewFile();
    fw = new FileWriter(f);
    pw = new PrintWriter(fw, true);
    String partHeader = "", partExpress = "", partClass = "", partComments = "";
    pw.print(printHtmlHead(printCapitalCase(rule.getName(null))));
    partHeader += printNavBar();

    // Header
    partHeader += printHRef(printH3(correctSchemaName(schemaName) + " (jsdai." + correctSchemaNameS(schemaName) + ")"), "package-summary.html");
    partHeader += printBreak();

    // Express for global rule
    partExpress += printTableHeader();
    partExpress += printExpressForGlobalRule(rule);
    partExpress += printTableTail();
    partExpress += printBreak();

    // Function for constant
    partClass += printTableHeader();
    partClass += printClassForGlobalRule(rule);
    partClass += printTableTail();
    partClass += printBreak();

    // Comments for entity
    partComments += printDocumentationForEntity(rule);
    if (!partComments.equals("")) {
      partComments += printBreak();
    }

    pw.print(partHeader);
    pw.print(partExpress);
    pw.print(partClass);
    pw.print(partComments);
    pw.print(printHtmlTail());
    pw.close();
  }

  private String printExpressForGlobalRule(EGlobal_rule rule) throws SdaiException {
    String result = "";
    String entity_ref_list = "";
    AEntity_definition aed = rule.getEntities(null);

    SdaiIterator iter_aed = aed.createIterator();
    while (iter_aed.next()) {
      EEntity_definition ed = aed.getCurrentMember(iter_aed);
      if (!entity_ref_list.equals("")) {
        entity_ref_list += ", ";
      }
      entity_ref_list += printHRef(ed.getName(null), getSchemaNameIfDiffer(ed, schema) + getUpper(ed.getName(null)) + ".html");
    }

    result = println("RULE " + printBold(rule.getName(null)) + "&nbsp;FOR (" + entity_ref_list + ");");

    AEntity aeUsers = new AEntity();
    rule.findEntityInstanceUsers(asmExpressDomain, aeUsers);
    SdaiIterator iter_users = aeUsers.createIterator();
    while (iter_users.next()) {
      EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
      if (eUser instanceof EExpress_code) {
        EExpress_code ec = (EExpress_code) eUser;
        A_string asECValues = ec.getValues(null);
        SdaiIterator iter_ecv = asECValues.createIterator();
        while (iter_ecv.next()) {

          String sEC = format(asECValues.getCurrentMember(iter_ecv));
          // some formatting, but better formating would be nice, perhaps to implement parsing of the string

          if (!flag_original_expressions) {
            sEC = replaceAll(sEC, "LOCAL", "LOCAL<BR>\n");
            sEC = replaceAll(sEC, ";", ";<BR>\n");
          }
          result += println(sEC);
        }
      }
    }
    result += printWhere_rules(rule);
    result += println("END_RULE; -- " + rule.getName(null));
    return result;
  }

  private String printClassForGlobalRule(EGlobal_rule rule) throws SdaiException {
    String result = "";
    result += println(printBold("public class R" + getUpper(rule.getName(null))));

    AWhere_rule awr = rule.getWhere_rules(null, null);

    SdaiIterator iter_awr = awr.createIterator();
    while (iter_awr.next()) {
      EWhere_rule wr = awr.getCurrentMember(iter_awr);
      result += print(printTab("public int r" + getUpper(wr.testLabel(null) ? wr.getLabel(null) : "") + "(SdaiContext _context)"));
    }
    result += print(printTab("public int run(SdaiContext _context, A_string violations)"));
    return result;
  }

  /**
   * prints algorithm
   *
   * @param algorithm
   * @param schema
   * @throws SdaiException
   * @throws IOException
   */
  private void printAlgorithm(EAlgorithm_definition algorithm, ESchema_definition schema) throws SdaiException, IOException {
    f = new File(correctSchemaNameFile(schemaName) + File.separator + getUpper(getComplexName(algorithm.getName(null))) + ".html");
    f.createNewFile();
    fw = new FileWriter(f);
    pw = new PrintWriter(fw, true);
    String partHeader = "", partExpress = "", partClass = "", partComments = "";
    pw.print(printHtmlHead(printCapitalCase(algorithm.getName(null))));
    partHeader += printNavBar();

    // Header
    partHeader += printHRef(printH3(correctSchemaName(schemaName) + " (jsdai." + correctSchemaNameS(schemaName) + ")"), "package-summary.html");
    partHeader += printBreak();

    // Express for algorithm
    partExpress += printTableHeader();
    partExpress += printExpressForAlgorithm(algorithm);
    partExpress += printTableTail();
    partExpress += printBreak();

    // Function for constant
    partClass += printTableHeader();
    partClass += printClassForAlgorithm(algorithm);
    partClass += printTableTail();
    partClass += printBreak();

    // Comments for entity
    partComments += printDocumentationForEntity(algorithm);
    if (!partComments.equals("")) {
      partComments += printBreak();
    }

    pw.print(partHeader);
    pw.print(partExpress);
    pw.print(partClass);
    pw.print(partComments);
    pw.print(printHtmlTail());
    pw.close();
  }

  private String printExpressForAlgorithm(EAlgorithm_definition algorithm) throws SdaiException {
    String result = "";
    String parameter_list = "";
    String algorithm_body = "";
    if (algorithm.testParameters(null)) {
      AParameter ap = algorithm.getParameters(null);
      SdaiIterator iter_ap = ap.createIterator();
      while (iter_ap.next()) {
        EParameter p = ap.getCurrentMember(iter_ap);
        if (p.testName(null)) {
          if (!parameter_list.equals("")) {
            //parameter_list += ", ";
            parameter_list += "; ";
          }
          if (p.testVar_type(null) && p.getVar_type(null)) {
            parameter_list += "VAR ";
          }
          parameter_list += p.getName(null);
          if (p.testParameter_type(null)) {
            parameter_list += " : " + printType(p.getParameter_type(null));
            if (p.testType_labels(null)) {
              parameter_list += ":";
              A_string atl = p.getType_labels(null);
              SdaiIterator iter_atl = atl.createIterator();
              String stl = "";
              while (iter_atl.next()) {
                if (!stl.equals("")) {
                  //stl += ",";
                  stl += ";";
                }
                stl += atl.getCurrentMember(iter_atl);
              }
              parameter_list += stl;
            }
          }
        }
      }
    }

    AEntity aeUsers = new AEntity();
    algorithm.findEntityInstanceUsers(asmExpressDomain, aeUsers);
    SdaiIterator iter_users = aeUsers.createIterator();
    while (iter_users.next()) {
      EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
      if (eUser instanceof EExpress_code) {
        EExpress_code ec = (EExpress_code) eUser;
        A_string asECValues = ec.getValues(null);
        SdaiIterator iter_ecv = asECValues.createIterator();
        while (iter_ecv.next()) {
          String sEC = format(asECValues.getCurrentMember(iter_ecv));
          if (!flag_original_expressions) {
            sEC = replaceAll(sEC, ";", ";<BR>\n");
            sEC = replaceAll(sEC, "end_local", "END_LOCAL");
            sEC = replaceAll(sEC, "local ", "LOCAL<BR>\n");
            sEC = replaceAll(sEC, "local\t", "LOCAL<BR>\n");
            sEC = replaceAll(sEC, "local\n", "LOCAL<BR>\n");
            sEC = replaceAll(sEC, "local\r", "LOCAL<BR>\n");
            sEC = replaceAll(replaceAll(sEC, "end ;", "END ;"), "begin", "BEGIN<BR>\n");
            sEC = replaceAll(sEC, "return", "RETURN");
          }

          for (Iterator iter_ad = vAlgorithmDefinition.iterator(); iter_ad.hasNext(); ) {
            EAlgorithm_definition ad = (EAlgorithm_definition) iter_ad.next();
            sEC = replaceAll(sEC, ad.getName(null) + " (",
                printHRef(ad.getName(null), getSchemaNameIfDiffer(ad, schema) + getUpper(ad.getName(null)) + ".html") + " (");
          }
          algorithm_body += sEC;
        }
      }
    }

    if (algorithm instanceof EFunction_definition) {
      EFunction_definition function = (EFunction_definition) algorithm;
      result = println("FUNCTION " + printBold(function.getName(null)));
      result += "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; (" + parameter_list + ")";
      if (function.testReturn_type(null)) {
        result += " : " + printType(function.getReturn_type(null));
        if (function.testReturn_type_label(null)) {
          result += ":" + function.getReturn_type_label(null);
        }
      }
      result += ";<BR>";
      result += print("<P style=\"margin-left: 40px; margin-top: 0px; margin-bottom: 0px\">" + algorithm_body + "</P>");
      result += println("END_FUNCTION; -- " + function.getName(null));
    }
    if (algorithm instanceof EProcedure_definition) {
      EProcedure_definition procedure = (EProcedure_definition) algorithm;
      result = println("PROCEDURE " + printBold(procedure.getName(null)));
      result += printTab("(" + parameter_list + ");");
      result += println(algorithm_body);
      result += println("END_PROCEDURE; -- " + procedure.getName(null));
    }
    return result;
  }

  private String printClassForAlgorithm(EAlgorithm_definition algorithm) throws SdaiException {
    String result = "";
    String parameter_list = "";

    if (algorithm.testParameters(null)) {
      AParameter ap = algorithm.getParameters(null);
      SdaiIterator iter_ap = ap.createIterator();
      while (iter_ap.next()) {
        EParameter p = ap.getCurrentMember(iter_ap);
        if (p.testName(null)) {
          parameter_list += ", Value " + p.getName(null);
        }
      }
    }
    result += println(printBold("public class F" + getUpper(algorithm.getName(null))));
    result += printTab("public static Value run(SdaiContext _context" + parameter_list + ")");

    return result;
  }

  /**
   * prints constant
   *
   * @param constant
   * @param schema
   * @throws SdaiException
   * @throws IOException
   */
  private void printConstant(EConstant_definition constant, ESchema_definition schema) throws SdaiException, IOException {
    f = new File(correctSchemaNameFile(schemaName) + File.separator + getUpper(getComplexName(constant.getName(null))) + ".html");
    f.createNewFile();
    fw = new FileWriter(f);
    pw = new PrintWriter(fw, true);
    String partHeader = "", partExpress = "", partClass = "", partComments = "";
    pw.print(printHtmlHead(printCapitalCase(constant.getName(null))));
    partHeader += printNavBar();

    // Header
    partHeader += printHRef(printH3(correctSchemaName(schemaName) + " (jsdai." + correctSchemaNameS(schemaName) + ")"), "package-summary.html");
    partHeader += printBreak();

    // Express for constant
    partExpress += printTableHeader();
    partExpress += printExpressForConstant(constant);
    partExpress += printTableTail();
    partExpress += printBreak();

    // Function for constant
    partClass += printTableHeader();
    partClass += println(printBold("public class " + correctSchemaNameS(schemaName)));
    partClass += printTab("public static Value c" + getUpper(constant.getName(null)) + "(SdaiContext _context)");
    partClass += printTableTail();
    partClass += printBreak();

    // Comments for entity
    partComments += printDocumentationForEntity(constant);
    if (!partComments.equals("")) {
      partComments += printBreak();
    }

    pw.print(partHeader);
    pw.print(partExpress);
    pw.print(partClass);
    pw.print(partComments);
    pw.print(printHtmlTail());
    pw.close();
  }

  private String printExpressForConstant(EConstant_definition constant) throws SdaiException {
    String result = "";
    String constant_body = "";

    result = println("CONSTANT ");
    result += "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " + printBold(constant.getName(null));
    if (constant.testDomain(null)) {
      result += print(" : " + printType(constant.getDomain(null)));
    }
    AEntity aeUsers = new AEntity();
    constant.findEntityInstanceUsers(asmExpressDomain, aeUsers);
    SdaiIterator iter_users = aeUsers.createIterator();
    while (iter_users.next()) {
      EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
      if (eUser instanceof EExpress_code) {
        EExpress_code ec = (EExpress_code) eUser;
        A_string asECValues = ec.getValues(null);
        SdaiIterator iter_ecv = asECValues.createIterator();
        while (iter_ecv.next()) {
          constant_body += format(asECValues.getCurrentMember(iter_ecv));
        }
      }
    }
    if (!constant_body.equals("")) {
      result += print(" := " + constant_body);
    }
    result += println(";");
    result += println("END_CONSTANT; -- " + constant.getName(null));

    return result;
  }

  private boolean testRedeclaring(EAttribute attribute) throws SdaiException {
    if (attribute instanceof EExplicit_attribute) {
      return ((EExplicit_attribute) attribute).testRedeclaring(null);
    }
    else if (attribute instanceof EDerived_attribute) {
      return ((EDerived_attribute) attribute).testRedeclaring(null);
    }
    else if (attribute instanceof EInverse_attribute) {
      return ((EInverse_attribute) attribute).testRedeclaring(null);
    }
    return false;
  }

  private EAttribute getRedeclaring(EAttribute attribute) throws SdaiException {
    if (attribute instanceof EExplicit_attribute) {
      return ((EExplicit_attribute) attribute).getRedeclaring(null);
    }
    else if (attribute instanceof EDerived_attribute) {
      return (EAttribute) ((EDerived_attribute) attribute).getRedeclaring(null);
    }
    else if (attribute instanceof EInverse_attribute) {
      return ((EInverse_attribute) attribute).getRedeclaring(null);
    }
    return null;
  }

  /**
   * Return domain of attribute.
   */
  private EEntity getAttrType(EAttribute attribute) throws SdaiException {
    EEntity domain = null;
    if (attribute instanceof EExplicit_attribute) {
      domain = ((EExplicit_attribute) attribute).getDomain(null);
    }
    else if (attribute instanceof EDerived_attribute) {
      domain = ((EDerived_attribute) attribute).getDomain(null);
    }
    else if (attribute instanceof EInverse_attribute) {
      domain = ((EInverse_attribute) attribute).getDomain(null);
    }
    return domain;
  }

  private String printConstraints(EEntity_definition entity, ESchema_definition schema) throws SdaiException {
    String partConstraints = "";
    ASub_supertype_constraint constraints = new ASub_supertype_constraint();
    CSub_supertype_constraint.usedinGeneric_supertype(null, entity, schemas, constraints);
    SdaiIterator constraints_it = constraints.createIterator();
    TreeSet sorter = new TreeSet(new SorterForEntities());
    while (constraints_it.next()) {
      ESub_supertype_constraint currConstraint = constraints.getCurrentMember(constraints_it);
      if (currConstraint.testName(null)) {
        sorter.add(currConstraint);
      }
    }
    boolean first = true;
    for (Iterator i = sorter.iterator(); i.hasNext(); ) {
      ESub_supertype_constraint currConstraint = (ESub_supertype_constraint) i.next();
      if (first) {
        first = false;
        partConstraints += printH3("Known subtype constraints");
        partConstraints += "<TABLE>";
      }
      partConstraints += "<TR><TD NOWRAP>";
      partConstraints += printUnclosedTab(printHRef(currConstraint.getName(null),
          getSchemaNameIfDiffer(currConstraint, schema) + getUpper(currConstraint.getName(null)) + ".html"));
      partConstraints += " (" + correctSchemaName(findSchemaForEntity(currConstraint)) + ")<BR>";
      partConstraints += "</TD></TR>\n";
    }
    if (!first) {
      partConstraints += "</TABLE>";
      partConstraints += printBreak();
    }
    return partConstraints;
  }

  /**
   * Print all known express subtypes of entity which now are renamed to "Complex entities"
   *
   * @param entity
   * @param schema
   * @return
   * @throws SdaiException
   */
  private String printSubtypes(EEntity_definition entity, ESchema_definition schema) throws SdaiException {
    String partSubtypes = "";
    AEntity_definition subtypes = new AEntity_definition();
    CEntity_definition.usedinGeneric_supertypes(null, entity, schemas, subtypes);
    SdaiIterator subtypes_it = subtypes.createIterator();
    TreeSet sorter = new TreeSet(new SorterForEntities());
    while (subtypes_it.next()) {
      EEntity_definition currEntity = subtypes.getCurrentMember(subtypes_it);
      sorter.add(currEntity);
    }
    boolean first = true;
    for (Iterator i = sorter.iterator(); i.hasNext(); ) {
      EEntity_definition currEntity = (EEntity_definition) i.next();
      if (first) {
        first = false;
        if (getComplexName(currEntity.getName(null)).lastIndexOf("$") == (-1)) {
          partSubtypes += printH3("Known subtypes");
        }
        else {
          partSubtypes += printH3("Complex entities");
        }
        partSubtypes += "<TABLE>";
      }
      partSubtypes += "<TR><TD NOWRAP>";
      partSubtypes += printUnclosedTab(printHRef(getComplexName(currEntity.getName(null)), getSchemaNameIfDiffer(currEntity, schema)
          + getUpper(getComplexName(currEntity.getName(null))) + ".html"));
      partSubtypes += " (" + correctSchemaName(findSchemaForEntity(currEntity)) + ")<BR>";
      partSubtypes += "</TD></TR>\n";
    }
    if (!first) {
      partSubtypes += "</TABLE>";
      partSubtypes += printBreak();
    }
    return partSubtypes;
  }

  /**
   * print partial entity data types
   *
   * @param entity
   * @param otemp
   * @return
   * @throws SdaiException
   */
  private String printPartial(EEntity_definition entity, Object otemp) throws SdaiException {
    if (otemp == null) {
      otemp = entity;
    }
    String partPartial = "";
    AEntity supertypes = entity.getGeneric_supertypes(null);
    SdaiIterator it_super = supertypes.createIterator();
    while (it_super.next()) {
      // print supertype partials
      partPartial += printPartial((EEntity_definition) supertypes.getCurrentMemberObject(it_super), otemp);
    }
    if (entity.getTemp() != otemp) {
      // print partial entity
      partPartial += println(printHRef(getComplexName(entity.getName(null)),
          getSchemaNameIfDiffer(entity, schema) + getUpper(getComplexName(entity.getName(null))) + ".html"));
      partPartial += printPartialAttributes(entity, otemp);
      entity.setTemp(otemp);
    }
    return partPartial;
  }

  private String printPartialAttributes(EEntity_definition entity, Object otemp) throws SdaiException {
    String partPartial = "";
    AAttribute attributes = entity.getAttributes(null, null);
    if (attributes.getMemberCount() == 0) {
      partPartial += printTab("-");
    }
    else {
      // printing of explicit attributes in "Entity data types and their attributes" section
      AExplicit_attribute explicit = getAllExplicit_attributes(entity);
      SdaiIterator it_e = explicit.createIterator();
      while (it_e.next()) {
        EExplicit_attribute attribute = explicit.getCurrentMember(it_e);
        boolean later_redeclared = isLaterRedeclaredInScope(attribute, otemp);
        String temp = "";
        String original_name = "";
        if (attribute.testRedeclaring(null)) {
          temp += "(RT) ";
          String old_name = attribute.getRedeclaring(null).getName(null);
          if (!old_name.equalsIgnoreCase(attribute.getName(null))) {
            original_name = old_name + "->";
          }
        }
        temp += ((attribute.getOptional_flag(null)) ? "(OPT) " : "");

        if (later_redeclared) {
          temp += print(printItalic(original_name + attribute.getName(null)) + ": ");
        }
        else {
          temp += print(original_name + attribute.getName(null) + ": ");
        }
        temp += printType(getAttrType(attribute)) + ";";
        partPartial += printTab(temp);
      }

      ADerived_attribute derived = getAllDerived_attributes(entity);
      SdaiIterator it_d = derived.createIterator();
      while (it_d.next()) {
        EDerived_attribute attribute = derived.getCurrentMember(it_d);
        boolean later_redeclared = isLaterRedeclaredInScopeDerived(attribute, otemp);
        String temp = "";
        String original_name = "";
        if (attribute.testRedeclaring(null)) {
          temp += "(RT) ";
          String old_name = ((EAttribute) attribute.getRedeclaring(null)).getName(null);
          if (!old_name.equalsIgnoreCase(attribute.getName(null))) {
            original_name = old_name + "->";
          }
        }
        temp += "(DER) ";

        if (later_redeclared) {
          temp += print(printItalic(original_name + attribute.getName(null)) + ": ");
        }
        else {
          temp += print(original_name + attribute.getName(null) + ": ");
        }
        temp += printType(getAttrType(attribute)) + ";";
        partPartial += printTab(temp);
      }

      AInverse_attribute inverse = getAllInverse_attributes(entity);
      SdaiIterator it_i = inverse.createIterator();
      while (it_i.next()) {
        EInverse_attribute attribute = inverse.getCurrentMember(it_i);
        boolean later_redeclared = isLaterRedeclaredInScopeInverse(attribute, otemp);
        String temp = "";
        String original_name = "";
        if (attribute.testRedeclaring(null)) {
          temp += "(RT) ";
          String old_name = attribute.getRedeclaring(null).getName(null);
          if (!old_name.equalsIgnoreCase(attribute.getName(null))) {
            original_name = old_name + "->";
          }
        }
        temp += "(INV) ";

        if (later_redeclared) {
          temp += print(printItalic(original_name + attribute.getName(null)) + ": ");
        }
        else {
          temp += print(original_name + attribute.getName(null) + ": ");
        }

        if (attribute.testMin_cardinality(null)) {
          temp += ((attribute.getDuplicates(null)) ? "BAG [" : "SET [") + printBound(attribute.getMin_cardinality(null)) + ":"
              + printBound((attribute.testMax_cardinality(null)) ? attribute.getMax_cardinality(null) : null) + "] OF ";
        }
        EEntity_definition domain = attribute.getDomain(null);
        temp += printType(domain)
            + " FOR "
            + printHRef(attribute.getInverted_attr(null).getName(null), getSchemaNameIfDiffer(domain, schema) + getUpper(domain.getName(null))
            + ".html") + ";";

        partPartial += printTab(temp);
      }
    }
    return partPartial;
  }

  private boolean isLaterRedeclaredInScope(EExplicit_attribute xa, Object otemp) throws SdaiException {

    EEntity_definition scope = null;
    AAttribute attributes = new AAttribute();

    CExplicit_attribute.usedinRedeclaring(null, xa, schemas, attributes);
    CDerived_attribute.usedinRedeclaring(null, xa, schemas, attributes);

    if (attributes.getMemberCount() > 0) {
      if (otemp instanceof EEntity_definition) {
        scope = (EEntity_definition) otemp;
      }
      else {
        return true;
      }
      SdaiIterator iter = attributes.createIterator();
      while (iter.next()) {
        EAttribute attribute = (EAttribute) attributes.getCurrentMemberObject(iter);
        EEntity_definition attribute_ed = (EEntity_definition) attribute.getParent(null);
        HashSet processed = new HashSet();
        boolean result = recurseSupertypes(scope, attribute_ed, scope, processed);
        if (result) {
          return true;
        }
      }
      return false;
    }
    else {
      return false;
    }
  }

  private boolean isLaterRedeclaredInScopeDerived(EDerived_attribute xa, Object otemp) throws SdaiException {

    EEntity_definition scope = null;
    ADerived_attribute attributes = new ADerived_attribute();

    CDerived_attribute.usedinRedeclaring(null, xa, schemas, attributes);

    if (attributes.getMemberCount() > 0) {
      if (otemp instanceof EEntity_definition) {
        scope = (EEntity_definition) otemp;
      }
      else {
        return true;
      }
      SdaiIterator iter = attributes.createIterator();
      while (iter.next()) {
        EDerived_attribute attribute = (EDerived_attribute) attributes.getCurrentMemberObject(iter);
        // see if this redeclaring attribute is in the scope - is it in the current entity (scope) or in its supertypes
        EEntity_definition attribute_ed = (EEntity_definition) attribute.getParent(null);
        HashSet processed = new HashSet();
        boolean result = recurseSupertypes(scope, attribute_ed, scope, processed);
        if (result) {
          return true;
        }
      }
      return false;
    }
    else {
      return false;
    }
  }

  private boolean isLaterRedeclaredInScopeInverse(EInverse_attribute xa, Object otemp) throws SdaiException {
    EEntity_definition scope = null;
    AInverse_attribute attributes = new AInverse_attribute();

    CInverse_attribute.usedinRedeclaring(null, xa, schemas, attributes);

    if (attributes.getMemberCount() > 0) {
      if (otemp instanceof EEntity_definition) {
        scope = (EEntity_definition) otemp;
      }
      else {
        return true;
      }
      SdaiIterator iter = attributes.createIterator();
      while (iter.next()) {
        EInverse_attribute attribute = (EInverse_attribute) attributes.getCurrentMemberObject(iter);
        // see if this redeclaring attribute is in the scope - is it in the current entity (scope) or in its supertypes
        EEntity_definition attribute_ed = (EEntity_definition) attribute.getParent(null);
        HashSet processed = new HashSet();
        boolean result = recurseSupertypes(scope, attribute_ed, scope, processed);
        if (result) {
          return true;
        }
      }
      return false;
    }
    else {
      return false;
    }
  }

  /*
   * a - original attribute
   * b subtype of a - redeclaring attribute
   * c subtype of b - c = scope
   * if so, return true
   */

  static boolean recurseSupertypes(EEntity_definition current_entity, EEntity_definition target_entity, EEntity_definition start_entity, HashSet processed)
      throws SdaiException {

    boolean result = false;

    if (!(processed.add(current_entity))) {
      // repeated inheritance, already done
      return false;
    }
    AEntity supertypes = current_entity.getGeneric_supertypes(null);

    SdaiIterator isuper = supertypes.createIterator();

    while (isuper.next()) {
      EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(isuper);
      result = recurseSupertypes(supertype, target_entity, start_entity, processed);
      if (result) {
        return true;
      }
    }
    if (current_entity == target_entity) {
      return true;
    }

    return false;

  } // end of function

  /**
   * print partials for complex entity
   *
   * @param entity
   * @return
   * @throws SdaiException
   */
  private String printPartialComplex(EEntity_definition entity) throws SdaiException {
    String partPartial = "";
    TreeSet defs = new TreeSet(new SorterForEntities());
    findPartial(defs, entity);
    for (Iterator i = defs.iterator(); i.hasNext(); ) {
      EEntity_definition def = (EEntity_definition) i.next();
      if (def.getTemp() != entity) {
        // print partial entity
        partPartial += println(printHRef(getUpper(def.getName(null)), getSchemaNameIfDiffer(def, schema) + getUpper(def.getName(null)) + ".html"));
        // print partial attributes
        partPartial += printPartialAttributes(def, null);
        def.setTemp(entity);
      }
    }
    return partPartial;
  }

  /**
   * fills partials for complex entity
   */
  private void findPartial(TreeSet set, EEntity_definition entity) throws SdaiException {
    AEntity supertypes = entity.getGeneric_supertypes(null);
    SdaiIterator it = supertypes.createIterator();
    while (it.next()) {
      EEntity_definition def = (EEntity_definition) supertypes.getCurrentMemberObject(it);
      findPartial(set, def);
    }
    if (!entity.getComplex(null)) {
      set.add(entity);
    }
  }

  private EEntity findInPartial(EEntity_definition entity, String name) throws SdaiException {
    TreeSet set = new TreeSet(new SorterForEntities());
    findPartial(set, entity);
    for (Iterator i = set.iterator(); i.hasNext(); ) {
      EEntity_definition e = (EEntity_definition) i.next();
      if (e.getName(null).equalsIgnoreCase(name)) {
        return e;
      }
    }
    return entity;
  }

  private ESelect_type isSelectInside(EEntity type) throws SdaiException {
    if (type instanceof ESelect_type) {
      return (ESelect_type) type;
    }
    else if (type instanceof EDefined_type) {
      EDefined_type dt = (EDefined_type) type;
      return isSelectInside(dt.getDomain(null));
    }
    return null;
  }

  private boolean isAggregateInside(EEntity domain, Vector nodes) throws SdaiException {
    while (domain instanceof EDefined_type) {
      domain = ((EDefined_type) domain).getDomain(null);
    }
    if (domain instanceof EAggregation_type) {
      return true;
    }
    if ((domain instanceof ESelect_type) && (nodes != null)) {
      return isAggregateInsideSelect((ESelect_type) domain, nodes, 0);
    }
    return false;
  }

  private boolean isAggregateInsideSelect(ESelect_type st, Vector nodes, int i) throws SdaiException {
    if (1 >= nodes.size()) {
      return false;
    }
    ANamed_type selections = st.getSelections(null);
    SdaiIterator selections_it = selections.createIterator();
    while (selections_it.next()) {
      EEntity type = selections.getCurrentMember(selections_it);
      ENamed_type t = (ENamed_type) nodes.elementAt(i);
      if (type == t) {
        i++;
        while (type instanceof EDefined_type) {
          type = ((EDefined_type) type).getDomain(null);
        }
        if (type instanceof ESelect_type) {
          return isAggregateInsideSelect((ESelect_type) type, nodes, i);
        }
        else if (type instanceof EAggregation_type) {
          return true;
        }
        else {
          return false;
        }
      }
    }
    return false;
  }

  final int NO_SELECT = 0;
  final int FIRST_TYPE = 1;
  final int BASE_TYPE = 2;

  private String printJavaType(EEntity type, String sufix, Vector nodeses, int selectStatus, int deep) throws SdaiException {
    Vector nodes = null;
    if (nodeses != null) {
      nodes = new Vector(nodeses);
    }
    String partJavaType = "";
    if (type == null) {
      partJavaType += printHRef("EEntity", "../lang/EEntity.html");
    }
    else if (type instanceof EAggregation_type) {
      EAggregation_type at = (EAggregation_type) type;
      EEntity domain = at.getElement_type(null);
      sufix += (sufix.length() > 0) ? "a" : "A";
      partJavaType += printJavaType(domain, sufix, nodes, (selectStatus == BASE_TYPE) ? NO_SELECT : selectStatus, deep + 1);
    }
    else if (type instanceof EEntity_definition) {
      EEntity_definition definition = (EEntity_definition) type;
      if (sufix.length() > 0) {
        if (sufix.charAt(sufix.length() - 1) != 'A' && sufix.charAt(sufix.length() - 1) != 'a') {
          sufix = "E";
        }
      }
      else {
        sufix = "E";
      }
      partJavaType += printHRef(sufix + getUpper(definition.getName(null)), getSchemaNameIfDiffer(type, schema) + getUpper(definition.getName(null))
          + ".html");
    }
    else if (type instanceof EDefined_type) {
      EDefined_type defined = (EDefined_type) type;
      EEntity def_domain = defined.getDomain(null);
      if ((selectStatus == FIRST_TYPE) || ((def_domain instanceof ESelect_type) && (selectStatus == NO_SELECT))) {
        if (sufix.length() > 0) {
          if (sufix.charAt(sufix.length() - 1) != 'A' && sufix.charAt(sufix.length() - 1) != 'a') {
            sufix = "E";
          }
        }
        else {
          sufix = "E";
        }
        partJavaType += printHRef(getUpper(sufix) + getUpper(defined.getName(null)),
            getSchemaNameIfDiffer(defined, schema) + getUpper(defined.getName(null)) + ".html");
      }
      else {
        partJavaType += printJavaType(defined.getDomain(null), sufix, nodes, selectStatus, deep);
      }
    }
    else if (type instanceof ESimple_type) {
      boolean isJsdaiClass = false;
      if (sufix.length() > 0) {
        if (sufix.charAt(sufix.length() - 1) == 'A' && sufix.charAt(sufix.length() - 1) != 'a') {
          sufix += '_';
        }
        isJsdaiClass = true;
      }
      partJavaType += getUpper(sufix);
      if (type instanceof ENumber_type) {
        partJavaType += "double";
      }
      else if (type instanceof EInteger_type) {
        partJavaType += "integer";
      }
      else if (type instanceof EReal_type) {
        partJavaType += "double";
      }
      else if (type instanceof EBoolean_type) {
        partJavaType += "boolean";
      }
      else if (type instanceof ELogical_type) {
        partJavaType += "integer";
      }
      else if (type instanceof EBinary_type) {
        partJavaType += "Binary";
      }
      else if (type instanceof EString_type) {
        partJavaType += "String";
      }
      if (isJsdaiClass) {
        partJavaType = printHRef(partJavaType, "../lang/" + partJavaType + ".html");
      }
    }
    else if (type instanceof ESelect_type) {
      if (nodes == null) {
        if (sufix.length() > 0) {
          if (sufix.charAt(sufix.length() - 1) != 'A' && sufix.charAt(sufix.length() - 1) != 'a') {
            sufix = "E";
          }
        }
        else {
          sufix = "E";
        }
        partJavaType += printHRef(sufix + "Entity", "../lang/" + sufix + "Entity" + ".html");
      }
      else {
        ANamed_type ant = ((ESelect_type) type).getSelections(null);
        SdaiIterator it = ant.createIterator();
        if (nodes.size() > 0) {
          while (it.next()) {
            ENamed_type n = ant.getCurrentMember(it);
            if (nodes.elementAt(0) == n) {
              nodes.remove(0);
              partJavaType += printJavaType(n, sufix, nodes, selectStatus, deep);
              return partJavaType;
            }
          }
          EEntity nulas = (EEntity) nodes.elementAt(0);
          nodes.remove(0);
          partJavaType += printJavaType(nulas, sufix, nodes, selectStatus, deep);
          return partJavaType;
        }
      }
    }
    else if (type instanceof EEnumeration_type) {
      if ((sufix.length() > 0) && (sufix.charAt(sufix.length() - 1) == 'A' && sufix.charAt(sufix.length() - 1) != 'a')) {
        partJavaType += getUpper(sufix) + "_enumeration";
      }
      else {
        partJavaType += "int";
      }
    }
    else {
      partJavaType += "???";
    }
    return partJavaType;
  }

  /**
   * Return name of dictionary entity.
   */
  private static String getDictionaryEntityName(EEntity entity) throws SdaiException {
    String result = "";
    if (entity instanceof ENamed_type) {
      result = ((ENamed_type) entity).getName(null);
    }
    else if (entity instanceof ESchema_definition) {
      result = ((ESchema_definition) entity).getName(null);
    }
    else if (entity instanceof EAttribute) {
      result = ((EAttribute) entity).getName(null);
    }
    else if (entity instanceof EWhere_rule) {
      result = ((EWhere_rule) entity).getLabel(null);
    }
    else if (entity instanceof EGlobal_rule) {
      result = ((EGlobal_rule) entity).getName(null);
    }
    else if (entity instanceof EAlgorithm_definition) { //--VV--
      result = ((EAlgorithm_definition) entity).getName(null); //--VV--
    }
    else if (entity instanceof EConstant_definition) { //--VV--
      result = ((EConstant_definition) entity).getName(null); //--VV--
    }
    else if (entity instanceof ESub_supertype_constraint) {
      result = ((ESub_supertype_constraint) entity).getName(null);
    }

    return result;
  }

  private static StrOp getDictionaryEntityNameOp(final EEntity entity) {
    return new StrOp() {
      public void op(StringBuffer result) throws SdaiException {
        if (entity instanceof ENamed_type) {
          result.append(((ENamed_type) entity).getName(null));
        }
        else if (entity instanceof ESchema_definition) {
          result.append(((ESchema_definition) entity).getName(null));
        }
        else if (entity instanceof EAttribute) {
          result.append(((EAttribute) entity).getName(null));
        }
        else if (entity instanceof EWhere_rule) {
          result.append(((EWhere_rule) entity).getLabel(null));
        }
        else if (entity instanceof EGlobal_rule) {
          result.append(((EGlobal_rule) entity).getName(null));
        }
        else if (entity instanceof EAlgorithm_definition) { //--VV--
          result.append(((EAlgorithm_definition) entity).getName(null)); //--VV--
        }
        else if (entity instanceof EConstant_definition) { //--VV--
          result.append(((EConstant_definition) entity).getName(null)); //--VV--
        }
        else if (entity instanceof ESub_supertype_constraint) {
          result.append(((ESub_supertype_constraint) entity).getName(null));
        }
      }
    };
  }

  /**
   * Returns type letter of dictionary entity.
   * Used in indexes to indicate entity type.
   *
   * @param entity
   * @return
   * @throws SdaiException
   */
  private String getDictionaryEntityType(EEntity entity) throws SdaiException {
    String result = "";
    if (entity instanceof EEntity_definition) {
      EEntity_definition ed = (EEntity_definition) entity;
      if (ed.getComplex(null)) {
        result = "c";
      }
      else {
        result = "e";
      }
    }
    else if (entity instanceof EDefined_type) {
      result = "d";
    }
    else if (entity instanceof EGlobal_rule) {
      result = "r";
    }
    else if (entity instanceof EFunction_definition) {
      result = "f";
    }
    else if (entity instanceof EProcedure_definition) {
      result = "p";
    }
    else if (entity instanceof EConstant_definition) {
      result = "k";
    }
    else if (entity instanceof ESub_supertype_constraint) {
      result = "s";
    }

    return result;
  }

  private String getDictionaryEntityTypeRR2(EDeclaration declaration) throws SdaiException {
    String result = "";

    EEntity entity = declaration.getDefinition(null);

    if (entity instanceof EEntity_definition) {
      EEntity_definition ed = (EEntity_definition) entity;
      if (ed.getComplex(null)) {
        result = "c";
      }
      else {
        result = "e";
      }
    }
    else if (entity instanceof EDefined_type) {
      result = "d";
    }
    else if (entity instanceof EGlobal_rule) {
      result = "r";
    }
    else if (entity instanceof EFunction_definition) {
      result = "f";
    }
    else if (entity instanceof EProcedure_definition) {
      result = "p";
    }
    else if (entity instanceof EConstant_definition) {
      result = "k";
    }
    else if (entity instanceof ESub_supertype_constraint) {
      result = "s";
    }

    if (declaration instanceof EUsed_declaration) {
      result += "u";
    }
    else if (declaration instanceof EReferenced_declaration) {
      result += "r";
    }
    else if (declaration instanceof EImplicit_declaration) {
      result += "i";
    }

    return result;
  }

  private String getDictionaryEntityTypeRR(EEntity entity, SdaiModel model) throws SdaiException {
    String result = "";
    if (entity instanceof EEntity_definition) {
      EEntity_definition ed = (EEntity_definition) entity;
      if (ed.getComplex(null)) {
        result = "c";
      }
      else {
        result = "e";
      }
    }
    else if (entity instanceof EDefined_type) {
      result = "d";
    }
    else if (entity instanceof EGlobal_rule) {
      result = "r";
    }
    else if (entity instanceof EFunction_definition) {
      result = "f";
    }
    else if (entity instanceof EProcedure_definition) {
      result = "p";
    }
    else if (entity instanceof EConstant_definition) {
      result = "k";
    }
    else if (entity instanceof ESub_supertype_constraint) {
      result = "s";
    }

    // add the second letter if not local declaration:
    // although there should not be more than one declaration for the same type,
    // in the case there are, the priorities are: local, used, referenced, implicit
    Aggregate declarations = model.getInstances(EDeclaration.class);
    SdaiIterator it = declarations.createIterator();
    String second_letter = "";
    while (it.next()) {
      EDeclaration declaration = (EDeclaration) declarations.getCurrentMemberObject(it);
      EEntity definition = declaration.getDefinition(null);
      if (definition != entity) {
        continue;
      }
      if (declaration instanceof ELocal_declaration) {
        second_letter = "L";
      }
      else if (declaration instanceof EUsed_declaration) {
        if (!second_letter.equals("L")) {
          second_letter = "u";
        }
      }
      else if (declaration instanceof EReferenced_declaration) {
        if (second_letter.equals("i") || second_letter.equals("")) {
          second_letter = "r";
        }
      }
      else if (declaration instanceof EImplicit_declaration) {
        if (!second_letter.equals("")) {
          second_letter = "i";
        }

      }
    }
    if (second_letter.equals("L")) {
      second_letter = "";
    }
    result += second_letter;

    return result;
  }

  private static String getSchemaNameIfDiffer(EEntity entity, ESchema_definition schema) throws SdaiException {
    String result = "";
    ESchema_definition e_schema = findSchemaForEntity(entity);
    if (e_schema != schema) {
      result = "../" + correctSchemaNameS(e_schema) + "/";
    }
    return result;
  }

  private static StringBuffer getSchemaNameIfDiffer(StringBuffer result, final EEntity entity, final ESchema_definition schema) throws SdaiException {
    ESchema_definition e_schema = findSchemaForEntity(entity);
    if (e_schema != schema) {
      result.append("../");
      result.append(correctSchemaNameS(e_schema.getName(null)));
      result.append("/");
    }
    return result;
  }

  public static ESchema_definition findSchema(SdaiModel model) throws SdaiException {
    if (model.getMode() == 0) {
      model.startReadOnlyAccess();
    }
    Aggregate instances = model.getInstances(ESchema_definition.class);
    SdaiIterator it = instances.createIterator();
    if (it.next()) {
      return (ESchema_definition) instances.getCurrentMemberObject(it);
    }
    else {
      return null;
    }
  }

  /**
   * Finds this entity schema to which it belongs.
   */
  static public ESchema_definition findSchemaForEntity(EEntity entity) throws SdaiException {
    return (ESchema_definition) entity.findEntityInstanceSdaiModel().getInstances(ESchema_definition.class).getByIndexEntity(1);
  }

  /**
   * Finds documentation model for model.
   */
  private SdaiModel findDocModel(ASdaiModel models, SdaiModel mod) throws SdaiException {
    SdaiModel result = null;
    SdaiIterator it = models.createIterator();
    while (it.next()) {
      if (models.getCurrentMember(it).getName().equals("_DOCUMENTATION_" + mod.getName().substring(0, mod.getName().lastIndexOf("_DICTIONARY_DATA")))) {
        result = models.getCurrentMember(it);
        return result;
      }
    }
    return result;
  }

  private String findDocFor(EEntity entity) throws SdaiException {
    if (docModel != null) {
      Aggregate entities = docModel.getInstances();
      SdaiIterator it = entities.createIterator();
      while (it.next()) {
        EDocumentation doc = (EDocumentation) entities.getCurrentMemberObject(it);
        if (doc.getTarget(null) == entity) {
          // return doc.getDescription(null);
          // instead of STRING description now we have LIST OF STRING values.
          boolean values_present = doc.testValues(null);
          if (values_present) {
            A_string value_strings = doc.getValues(null);
            int number_of_value_strings = value_strings.getMemberCount();
            if (number_of_value_strings > 0) {
              String a_value_string = value_strings.getByIndex(1);
              return a_value_string;
            }
          }
        }
      }
    }
    return "";
  }

  static public String getUpper(String s) {
    return (s.length() > 0) ? s.substring(0, 1).toUpperCase() + s.substring(1, s.length()).toLowerCase() : "";
  }

  static public StringBuffer getUpper(StringBuffer result, String s) {
    if (s.length() > 0) {
      result.append(Character.toUpperCase(s.charAt(0)));
      int sLength = s.length();
      for (int i = 1; i < sLength; i++) {
        result.append(Character.toLowerCase(s.charAt(i)));
      }
    }
    return result;
  }

  static public StringBuffer getUpper(StringBuffer result, StrOp s) throws SdaiException {
    int start = result.length();
    s.op(result);
    int end = result.length();
    if (end > start) {
      result.setCharAt(start, Character.toUpperCase(result.charAt(start)));
      for (int i = start + 1; i < end; i++) {
        result.setCharAt(i, Character.toLowerCase(result.charAt(i)));
      }
    }
    return result;
  }

  private String printHtmlHead(String s) {
    return "<HTML>\n<HEAD>\n\t<TITLE>" + s + "</TITLE>\n</HEAD>\n<SCRIPT>\nfunction change_title(){\nparent.document.title=\"" + s
        + "\";\n}\n</SCRIPT>\n<BODY onload=\"change_title();\">\n";
  }

  private void printHtmlHead(StringBuffer result, String s) {
    result.append("<HTML>\n<HEAD>\n\t<TITLE>").append(s).append("</TITLE>\n</HEAD>\n<SCRIPT>\nfunction change_title(){\nparent.document.title=\"")
        .append(s).append("\";\n}\n</SCRIPT>\n<BODY onload=\"change_title();\">\n");
  }

  private String printHtmlTail() {
    return "</BODY>\n</HTML>\n";
  }

  private static String println(String s) {
    return s + "<BR>\n";
  }

  private static String println() {
    return "<BR>\n";
  }

  private static StringBuffer println(StringBuffer result) {
    result.append("<BR>\n");
    return result;
  }

  private static String printBold(String s) {
    return "<B>" + s + "</B>";
  }

  private static void printBold(StringBuffer result, String s) throws SdaiException {
    result.append(printBold(s));
  }

  private String printItalic(String s) {
    return "<I>" + s + "</I>";
  }

  private String print(String s) {
    return s;
  }

  private String printBreak() {
    return "<HR>\n";
  }

  private String printH3(String s) {
    return "<H4>\n" + s + "</H4>\n";
  }

  private StringBuffer printH3(StringBuffer result, String s) {
    result.append("<H4>\n").append(s).append("</H4>\n");
    return result;
  }

  private String printH3ISO(String s) {
    return "<H4>\n" + s + printUnclosedTab() + printIsoNumber(s) + "</H4>\n";
  }

  private void printH3ISO(StringBuffer result, String s) {
    result.append("<H4>\n").append(s).append(printUnclosedTab()).append(printIsoNumber(s)).append("</H4>\n");
  }

  private static String printH4(String s) {
    return "<H4><I>\n" + s + "</I></H4>\n";
  }

  private static void printH4(StringBuffer result, String s) {
    result.append("<H4><I>\n").append(s).append("</I></H4>\n");
  }

  static public String printHRef(String s, String ref) {
    return "<A HREF=" + ref + ">" + s + "</A>";
  }

  public static StringBuffer printHRef(StringBuffer result, final StrOp s, final StrOp ref) throws SdaiException {
    result.append("<A HREF=");
    ref.op(result);
    result.append(">");
    s.op(result);
    result.append("</A>");
    return result;
  }

  private String printUnclosedTab(String s) {
    return "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " + s;
  }

  private static String printUnclosedTab() {
    return "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ";
  }

  private static String printTab(String s) {
    return "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " + s + "<BR>\n";
  }

  private static StringBuffer printTab(StringBuffer result, String s) {
    result.append("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ").append(s).append("<BR>\n");
    return result;
  }

  private static StringBuffer printTab(StringBuffer result, StrOp s) throws SdaiException {
    result.append("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ");
    s.op(result);
    result.append("<BR>\n");
    return result;
  }

  private static String printName(String s, String name) {
    return "<A NAME=\"" + name + "\">" + s + "</A>\n";
  }

  private static String printHRefandTarget(String schemaName, String schemaURL, String target) {
    return "<A HREF=" + schemaURL + " TARGET=" + target + ">" + schemaName + "</A>\n";
  }

  private String schemaHRefandTargetOp(String s) {
    return printHRefandTarget(correctSchemaName(s), "../" + correctSchemaNameS(s) + "/package-summary.html", "classFrame");
  }

  private String printTableHeader() {
    return "<TABLE BORDER=\"0\" WIDTH=\"100%\"><TR><TD NOWRAP>\n";
  }

  private String printTableHeaderRR() {
    return "<TABLE BORDER=\"0\">\n";
  }

  private String printTableTail() {
    return "</TD></TR></TABLE>\n";
  }

  private String printTableTailRR() {
    return "</TABLE>\n";
  }

  private static String printCapitalCase(String s) {
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }

  private static void printCapitalCase(StringBuffer result, StrOp s) throws SdaiException {
    int start = result.length();
    s.op(result);
    if (result.length() > start) {
      result.setCharAt(start, Character.toUpperCase(result.charAt(start)));
    }
  }

  class MyBoolean {
    public boolean bool;

    public MyBoolean(boolean bool) {
      this.bool = bool;
    }
  }

  class MyInteger {
    public int integer;

    public MyInteger(int integer) {
      this.integer = integer;
    }
  }

  private void collectAggregates(EEntity type, int deep) throws SdaiException {
    if (type instanceof EAggregation_type) {
      EAggregation_type at = (EAggregation_type) type;
      deep++;
      collectAggregates(at.getElement_type(null), deep);
    }
    else if (type instanceof EEntity_definition) {
      addToArrayColector((ENamed_type) type, deep);
    }
    else if (type instanceof EDefined_type) {
      EDefined_type dt = (EDefined_type) type;
      EEntity domain = dt.getDomain(null);
      if (domain instanceof EAggregation_type) {
        collectAggregates(domain, deep);
      }
      else if (domain instanceof ESelect_type) {
        addToArrayColector((ENamed_type) type, deep);
        if (aggregates.indexOf(type) == -1) {
          ESelect_type st = (ESelect_type) domain;
          ANamed_type dts = st.getSelections(null);
          SdaiIterator it_dts = dts.createIterator();
          while (it_dts.next()) {
            collectAggregates(dts.getCurrentMember(it_dts), 0);
          }
        }
      }
      else if (domain instanceof EEnumeration_type) {
        addToArrayColector((ENamed_type) type, deep);
      }
      else if (domain instanceof EDefined_type) {
        collectAggregates(domain, deep);
      }
    }
  }

  private void addToArrayColector(ENamed_type type, int deep) throws SdaiException {
    boolean find = false;
    for (int i = 0; i < aggregates.size(); i++) {
      ENamed_type nt = (ENamed_type) aggregates.elementAt(i);
      if (nt == type) {
        find = true;
        Integer deep_value = (Integer) deep_agg.elementAt(i);
        if (deep_value.intValue() < deep) {
          deep_agg.remove(i);
          deep_agg.add(i, new Integer(deep));
        }
      }
    }
    if (!find) {
      if (deep > 0) {
        aggregates.add(type);
        deep_agg.add(new Integer(deep));
      }
    }
  }

  private String printBasedOnEnumerations(EEntity entity, EEntity type_domain, ASdaiModel domain) throws SdaiException {
    String result = "";
    if (type_domain instanceof EExtensible_enumeration_type) {
      result += printTableHeader();
      result += printH3("Extensions");
      result += printBasedOnEnumerationsX(entity, type_domain, domain);
      result += printTableTail();
      result += printBreak();
    }
    return result;
  }

  private String printBasedOnEnumerationsX(EEntity entity, EEntity type_domain, ASdaiModel domain) throws SdaiException {
    String result = "";
    TreeSet types = new TreeSet(new SorterForEntities());
    AExtended_enumeration_type eets = new AExtended_enumeration_type();
    SdaiIterator it_eets;
    CExtended_enumeration_type.usedinIs_based_on(null, (EDefined_type) entity, domain, eets);
    it_eets = eets.createIterator();
    while (it_eets.next()) {
      EExtended_enumeration_type eet = (EExtended_enumeration_type) eets.getCurrentMemberObject(it_eets);
      // eet is enumeration_type, we need the defined_type with this underlying type
      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, eet, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        types.add(def);
      }
    }
    Iterator typesIter = types.iterator();
    while (typesIter.hasNext()) {
      EDefined_type dt = (EDefined_type) typesIter.next();

      String tmp = "";

      tmp += printUnclosedTab(printHRef(dt.getName(null), getSchemaNameIfDiffer(dt, schema) + getUpper(getDictionaryEntityName(dt)) + ".html"));
      tmp += " (" + printHRef(correctSchemaName(findSchemaForEntity(dt).getName(null)) + ")", getSchemaNameIfDiffer(dt, schema) + "package-summary.html")
          + "<BR>\n";
      result += print(tmp);
    }
    if (result.equals("")) {
      result += print(printTab("-"));
    }
    return result;
  }

  private String printBasedOnSelects(EEntity entity, EEntity type_domain, ASdaiModel domain) throws SdaiException {
    String result = "";
    if (type_domain instanceof EExtensible_select_type) {
      result += printTableHeader();
      result += printH3("Extensions");
      result += printBasedOnSelectsX(entity, type_domain, domain);
      result += printTableTail();
      result += printBreak();
    }
    return result;
  }

  private String printBasedOnSelectsX(EEntity entity, EEntity type_domain, ASdaiModel domain) throws SdaiException {
    String result = "";
    String tab_items_string = printUnclosedTab() + printUnclosedTab();
    String tab_type_string = printUnclosedTab();

    TreeSet types = new TreeSet(new SorterForEntities());

    AExtended_select_type ests = new AExtended_select_type();
    SdaiIterator it_ests;
    CExtended_select_type.usedinIs_based_on(null, (EDefined_type) entity, domain, ests);
    it_ests = ests.createIterator();
    while (it_ests.next()) {
      EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
      // est is select_type, we need the defined_type with this underlying type

      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, est, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        types.add(def);
      }

    }

    Iterator typesIter = types.iterator();
    while (typesIter.hasNext()) {
      EDefined_type dt = (EDefined_type) typesIter.next();
      EEntity ut = dt.getDomain(null);

      String tmp = "";
      String tmp2 = "";

      tmp += tab_type_string + printHRef(dt.getName(null), getSchemaNameIfDiffer(dt, schema) + getUpper(getDictionaryEntityName(dt)) + ".html");

      tmp += " (" + printHRef(correctSchemaName(findSchemaForEntity(dt)) + ")", getSchemaNameIfDiffer(dt, schema) + "package-summary.html") + "<BR>\n";

      tmp += printSelectItems((ESelect_type) dt.getDomain(null), type_domain, domain, tab_items_string);
      if (ut instanceof EExtensible_select_type) {
        tmp2 = printBasedOnSelectsY(dt, type_domain, domain, tab_items_string, tab_type_string);
        tmp += tmp2;
      }

      result += print(tmp);
    }

    if (result.equals("")) {
      result += print(printTab("-"));
    }
    return result;
  }

  private String printBasedOnSelectsY(EEntity entity, EEntity type_domain, ASdaiModel domain, String tab_items_string, String tab_type_string)
      throws SdaiException {
    String result = "";
    String tab_type_string2 = tab_type_string + printUnclosedTab();
    String tab_items_string2 = tab_items_string + printUnclosedTab();

    TreeSet types = new TreeSet(new SorterForEntities());

    AExtended_select_type ests = new AExtended_select_type();
    SdaiIterator it_ests;
    CExtended_select_type.usedinIs_based_on(null, (EDefined_type) entity, domain, ests);
    it_ests = ests.createIterator();
    while (it_ests.next()) {
      EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
      // est is select_type, we need the defined_type with this underlying type
      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, est, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        types.add(def);
      }
    }

    Iterator typesIter = types.iterator();
    while (typesIter.hasNext()) {
      EDefined_type dt = (EDefined_type) typesIter.next();
      EEntity ut = dt.getDomain(null);

      String tmp = "";
      String tmp2 = "";

      tmp += tab_type_string2 + printHRef(dt.getName(null), getSchemaNameIfDiffer(dt, schema) + getUpper(getDictionaryEntityName(dt)) + ".html");
      tmp += " (" + printHRef(correctSchemaName(findSchemaForEntity(dt)) + ")", getSchemaNameIfDiffer(dt, schema) + "package-summary.html") + "<BR>\n";
      tmp += printSelectItems((ESelect_type) dt.getDomain(null), type_domain, domain, tab_items_string2);
      if (ut instanceof EExtensible_select_type) {
        tmp2 = printBasedOnSelectsY(dt, type_domain, domain, tab_items_string2, tab_type_string2);
        tmp += tmp2;
      }
      result += print(tmp);
    }
    return result;
  }

  private String printSelectItems(ESelect_type type, EEntity type_domain, ASdaiModel domain, String tab_string) throws SdaiException {
    String result = "";
    TreeSet items = new TreeSet(new SorterForEntities());

    if (type.testLocal_selections(null)) {
      ANamed_type nts = type.getLocal_selections(null);
      if (nts.getMemberCount() > 0) {
        // let's sort them and print in the aplphabetic order
        SdaiIterator it = nts.createIterator();
        while (it.next()) {
          ENamed_type ntitem = (ENamed_type) nts.getCurrentMemberObject(it);
          items.add(ntitem);
        }
        Iterator itemIter = items.iterator();
        while (itemIter.hasNext()) {
          ENamed_type nt = (ENamed_type) itemIter.next();

          String tmp = "";
          tmp += printHRef(nt.getName(null), getSchemaNameIfDiffer(nt, schema) + getUpper(getDictionaryEntityName(nt)) + ".html");
          result += tab_string + printUnclosedTab() + tmp + "<BR>";

        }
      }
    }
    if (result.equals("")) {
      result = tab_string + printUnclosedTab() + "-<BR>";
    }
    return result;
  }

  private String printGlobalRules(EEntity entity, ASdaiModel domain) throws SdaiException {
    String result = "";
    result += printTableHeader();
    result += printH3("Global rules for this entity data type");
    result += printGlobalRulesX(entity, domain);
    result += printTableTail();
    result += printBreak();
    return result;
  }

  private String printGlobalRulesX(EEntity entity, ASdaiModel domain) throws SdaiException {
    String result = "";
    TreeSet rules = new TreeSet(new SorterForEntities());
    AGlobal_rule grs = new AGlobal_rule();
    SdaiIterator it_grs;
    CGlobal_rule.usedinEntities(null, (EEntity_definition) entity, domain, grs);
    it_grs = grs.createIterator();
    while (it_grs.next()) {
      EGlobal_rule gr = (EGlobal_rule) grs.getCurrentMemberObject(it_grs);
      rules.add(gr);
    }

    Iterator rulesIter = rules.iterator();
    while (rulesIter.hasNext()) {
      EGlobal_rule rule = (EGlobal_rule) rulesIter.next();

      String tmp = "";

      tmp += printUnclosedTab(printHRef(rule.getName(null), getSchemaNameIfDiffer(rule, schema) + getUpper(getDictionaryEntityName(rule)) + ".html"));
      tmp += " (" + correctSchemaName(findSchemaForEntity(rule)) + ")" + "<BR>\n";
      result += print(tmp);
    }

    if (result.equals("")) {
      result += print(printTab("-"));
    }
    return result;
  }

  private String printEntityUsers(EEntity entity, ASdaiModel domain) throws SdaiException {
    StringBuffer result = new StringBuffer();
    result.append(printTableHeader());
    if (entity instanceof EEntity_definition) {
      if (((EEntity_definition) entity).getName(null).equalsIgnoreCase("Action_status")) {
        flag_debug = true;
      }
      else {
        flag_debug = false;
      }
      printH3(result, "Users: by entity attributes");
      used_types = new HashSet(); // not really needed
      printEntityUsersAttributesX(result, entity, domain, true);
      // for the implementation of printEntityUsersIdleTypes we might want to collect the information in PrintEntityUsersAttributesX to eliminate types that are used
      printH3(result, "Users: by defined types, not used by any entity attribute");
      printEntityUsersIdleTypes(result, entity, domain, true, true,
          true); // two last booleans - include non-extensible selects, include aggregates, is outer_loop - may be needed or not
    }
    else {
      // for non-entities - old implementation so far, not sure if needed at all
      printH3(result, "Users");
      printEntityUsersXX(result, entity, domain);
    }
    result.append(printTableTail());
    result.append(printBreak());
    return result.toString();
  }

  /*
   *
   * new implementation for printing entity users:
   * because we are only interested in users - attributes, and not in types that are not used for any entity attributes,
   * we can calculate the users in the opposite way, at the same time optimizing the process - processing the users only once:
   * - let's go through all the attributes of all entities, and for each attribute lets find its type:
   * if the type is not entity, we are not interested, if it is - we are.
   * The type may contain more than one entity if it is a select.
   * Let's do like this:
   *
   * we may or may not use a special class for processed results,
   * and the aggregate: do we need a HashMap? probably not, because we will go in a loop through all the attributes anyway,
   * probably,
   * if we use the current entity as a key, then we should..
   *
   * Or we can produce the following result when processing the attributes:
   * HashMap, where the key is an entity, and the object is something that contains all the attributes - users
   * However, producing such a structure will require calculations compareable to ones needed to print the users themselves, so,
   * perhaps no need,
   * or we can add this stage later, if needed.
   *
   * So, for now, let's have an ArrayList of objects with attributes: entity, attribute, HashSet of attribute entity types,
   * is it really a HashSet? We may also want to have the full path through the chain of selects, etc., what if more than one
   * path exists,
   * do we want then not HashSet?
   *
   *
   * Then, when we are actually printing the users,
   * for the entity, we go through the processed attributes and print whose which list that entity as one of its types
   */

  private StringBuffer printEntityUsersIdleTypes(StringBuffer result, EEntity entity, ASdaiModel domain, boolean include_non_extensible_selects,
      boolean include_aggregates, boolean is_outer) throws SdaiException {
    boolean is_extensible_select = false;
    if (entity instanceof EEntity_definition) {
      EEntity_definition definition = (EEntity_definition) entity;
      printHRef(result, getComplexNameOp(definition.getName(null)), getSchemaRefOp(definition, schema));
      println(result);
    }

    if (entity instanceof EDefined_type) {
      EEntity ut = ((EDefined_type) entity).getDomain(null);
      if (ut instanceof EExtensible_select_type) {
        is_extensible_select = true;
      }
    }

    int resultStart = result.length();
    EEntity[] sortedSet;
    int iSortedSetCount = 0;

    // -- Only one tree set is created per entity.
    // using generation and copying of generated ones. HashMap for storage.
    String sCurrentEntityPL = entity.getPersistentLabel();
    if (hmUsedEntities.containsKey(sCurrentEntityPL)) {
      sortedSet = (EEntity[]) hmUsedEntities.get(sCurrentEntityPL);
      iSortedSetCount = sortedSet.length;
    }
    else {
      AEntity users = new AEntity();
      AEntity final_users = new AEntity();
      entity.findEntityInstanceUsers(domain, users);
      SdaiIterator it_users = users.createIterator();
      while (it_users.next()) {
        EEntity user = users.getCurrentMemberEntity(it_users);
        // what about non-explicit attributes?
        if (user instanceof EExplicit_attribute) {
        }
        else if (user instanceof EDerived_attribute) {
        }
        else if (user instanceof EAggregation_type) {

          AAttribute attrs = new AAttribute();
          // what about non-explicit attributes?
          CExplicit_attribute.usedinDomain(null, user, domain, attrs);
          // add also defined types here ? (later)
          ADefined_type defs = new ADefined_type();
          SdaiIterator it_defs;
          CDefined_type.usedinDomain(null, user, domain, defs);
          it_defs = defs.createIterator();
          while (it_defs.next()) {
            EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
            if (!used_types.contains(def)) {
              final_users.addUnordered(def);
            }
          }
        }
        else if (user instanceof ESelect_type) {
          ADefined_type defs = new ADefined_type();
          SdaiIterator it_defs;
          CDefined_type.usedinDomain(null, user, domain, defs);
          it_defs = defs.createIterator();
          while (it_defs.next()) {
            EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);

            // if entity is extensible select type and def is its based_on type, then do not add it
            if ((user instanceof EExtended_select_type) && (is_extensible_select)) {
              if (((EExtended_select_type) user).getIs_based_on(null) != entity) {
                if (!used_types.contains(def)) {
                  final_users.addUnordered(def);
                }
              }
            }
            else {
              if (!used_types.contains(def)) {
                final_users.addUnordered(def);
              }
            }
          }
        }
      } // while
      sortedSet = new EEntity[final_users.getMemberCount()];
      SdaiIterator it_final_users = final_users.createIterator();
      while (it_final_users.next()) {
        sortedSet[iSortedSetCount] = final_users.getCurrentMemberEntity(it_final_users);
        iSortedSetCount++;
      }
      Arrays.sort(sortedSet, new SorterForUsers());
      hmUsedEntities.put(sCurrentEntityPL, sortedSet);
    }
    for (int i = 0; i < iSortedSetCount; i++) {
      EEntity user = sortedSet[i];
      if (user instanceof EExplicit_attribute) {
      }
      else if (user instanceof EDerived_attribute) {
      }
      else if (user instanceof EAggregation_type) {
        // do nothing, resolved to explicit attributes (or defined types)
      }
      else if (user instanceof ESelect_type) {
        // do nothing, resolved to defined_types
      }
      else if (user instanceof EDefined_type) {
        EDefined_type def = (EDefined_type) user;
        result.append(printUnclosedTab());
        printHRef(result, getStringOp(def.getName(null)), getSchemaRefDicOp(def, schema));
        result.append("<BR>\n");
        // new stuff - if select type, also print the users of it and the users of all the related extensible types with indentation
        EEntity dtdomain = def.getDomain(null);
        if (dtdomain instanceof ESelect_type) {
          printExtensibleSelectTypesX(result, (ESelect_type) dtdomain, def, domain);
        }
      }
    }
    if (resultStart == result.length()) {
      printTab(result, "-");
    }
    return result;
  }

  private StringBuffer printEntityUsersAttributesX(StringBuffer result, EEntity entity, ASdaiModel domain, boolean is_outer) throws SdaiException {
    if (entity instanceof EEntity_definition) {
      EEntity_definition definition = (EEntity_definition) entity;
      AEntity supertypes = definition.getGeneric_supertypes(null);
      SdaiIterator it_super = supertypes.createIterator();
      while (it_super.next()) {
        EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(it_super);
        printEntityUsersAttributesX(result, supertype, domain, false);
      }
      printHRef(result, getComplexNameOp(definition.getName(null)), getSchemaRefOp(definition, schema));
      println(result);
    }

    if (globalAttributeTypes == null) {
      SdaiModel work = globalRepo.createSdaiModel("working", SExtended_dictionary_schema.class);
      jsdai.dictionary.ESchema_definition a_schema = work.getUnderlyingSchema();
      SdaiContext _context = new SdaiContext(a_schema, domain, work);
      globalSession.setSdaiContext(_context);
      globalAttributeTypes = calculateAttributeTypesX(domain);
    }
    Iterator it_attr = globalAttributeTypes.iterator();
    ArrayList our_set = new ArrayList();
    while (it_attr.hasNext()) {
      AttributeClass attrc = (AttributeClass) it_attr.next();
      if (attrc.the_entity == entity) {
        our_set.add(attrc);
      } //if
    } // while
    AttributeClass[] sortedSet = new AttributeClass[our_set.size()];
    Iterator it_attr2 = our_set.iterator();
    int iSortedSetCount = 0;
    while (it_attr2.hasNext()) {
      AttributeClass attrc2 = (AttributeClass) it_attr2.next();
      sortedSet[iSortedSetCount] = attrc2;
      iSortedSetCount++;
    }
    Arrays.sort(sortedSet, new SorterForUsersX());
    String previous_entity_attribute_name = "";
    for (int i = 0; i < iSortedSetCount; i++) {
      AttributeClass user = sortedSet[i];
      String entity_attribute_name = user.the_parent.getName(null) + "." + user.the_attribute.getName(null);
      if (entity_attribute_name.equalsIgnoreCase(previous_entity_attribute_name)) {
        /*
         * we don't want the same attribute present several times:
         *
         * ccc.attr_3
         * sel1
         * sel2
         * ccc.attr_3
         * sel1
         * sel3
         *
         * just taking the first one:
         *
         * ccc.attr_3
         * sel1
         * sel2
         *
         *
         * altarnative - to implement:
         *
         * ccc.attr_3
         * sel1
         * sel2
         * sel3
         *
         *
         * or
         *
         * ccc.attr_3
         * sel1
         * sel2, sel3
         *
         * not sure if it is worth doing it, the attribute user is correctly displayed anyway
         */
        continue;
      }
      previous_entity_attribute_name = entity_attribute_name;

      result.append(printUnclosedTab());
      printHRef(result, getEntityAttrNameOp(user.the_parent, user.the_attribute), getSchemaRefDicOp(user.the_parent, schema));
      result.append("<BR>\n");

      if (user.dt != null) {

        // needed for the subsequent implementation of empty types
        used_types.add(user.dt);
        // not direct entity, but something else
        if (user.named_type != null) {
          NamedTypeClass ntc = user.named_type;
          if (ntc.previous != null) {
            TypeClass tc = ntc.previous;
            ArrayList type_chain = new ArrayList();
            for (; ; ) {
              if (tc == null) {
                break;
              }
              StringBuffer current_name = new StringBuffer();
              printHRef(current_name, getStringOp(tc.current_type.getName(null) + "<KUKU-1>"), getSchemaRefDicOp(tc.current_type, schema));
              type_chain.add(current_name);

              if (tc.previous != null) {
                // extensible select etc.
                tc = tc.previous;
              }
              else if (tc.initial != null) {
                // nested select, etc.
                tc = tc.initial.previous;
              }
              else {
                if (tc.previous_type != null) {
                  // the last one for the opposite direction, i.e, the 1st one
                  current_name = new StringBuffer();
                  printHRef(current_name, getStringOp(tc.previous_type.getName(null) + "<KUKU-2>"),
                      getSchemaRefDicOp(tc.previous_type, schema));
                  type_chain.add(current_name);
                }
                break;
              }
            }
            for (int k = type_chain.size() - 1, m = 0; k >= 0; k--, m++) {
              result.append(printUnclosedTab());
              for (int l = 0; l <= m; l++) {
                result.append(printUnclosedTab());
              }
              result.append(type_chain.get(k));
              result.append("<BR>\n");
            }
          }
        }
      }

    }
    return result;
  }

  ArrayList calculateAttributeTypesX(ASdaiModel domain) throws SdaiException {
    ArrayList result = new ArrayList();
    AAttribute attributes = (AAttribute) domain.getInstances(CAttribute.class);
    SdaiIterator it_attributes = attributes.createIterator();
    while (it_attributes.next()) {
      EEntity attribute_domain = null;
      EAttribute attribute = (EAttribute) attributes.getCurrentMemberObject(it_attributes);
      if (attribute instanceof EInverse_attribute) {
        // not interested in inverse attributes
        continue;
      }
      else if (attribute instanceof EExplicit_attribute) {
        attribute_domain = ((EExplicit_attribute) attribute).getDomain(null);
      }
      else if (attribute instanceof EDerived_attribute) {
        attribute_domain = ((EDerived_attribute) attribute).getDomain(null);
      }
      else {
        // internal error
      }
      if (attribute_domain != null) {
        // ok, lets get to the entities
        HashSet processed_ = new HashSet();
        processAttributeDomainX(result, attribute_domain, attribute, domain, null, processed_);
      }
    }
    return result;
  }

  ArrayList calculateAttributeTypes(ASdaiModel domain) throws SdaiException {
    ArrayList result = new ArrayList();
    AAttribute attributes = (AAttribute) domain.getInstances(CAttribute.class);
    SdaiIterator it_attributes = attributes.createIterator();
    while (it_attributes.next()) {
      EEntity attribute_domain = null;
      EAttribute attribute = (EAttribute) attributes.getCurrentMemberObject(it_attributes);
      if (attribute instanceof EInverse_attribute) {
        // not interested in inverse attributes
        continue;
      }
      else if (attribute instanceof EExplicit_attribute) {
        attribute_domain = ((EExplicit_attribute) attribute).getDomain(null);
      }
      else if (attribute instanceof EDerived_attribute) {
        attribute_domain = ((EDerived_attribute) attribute).getDomain(null);
      }
      else {
        // internal error
      }
      if (attribute_domain != null) {
        // ok, lets get to the entities
        processAttributeDomain(result, attribute_domain, attribute, domain);
      }
    }
    return result;
  }

  void processAttributeDomainX(ArrayList result, EEntity attribute_domain, EAttribute attribute, ASdaiModel domain, NamedTypeClass ntc, HashSet processed_)
      throws SdaiException {
    if (attribute_domain instanceof EEntity_definition) {
      result.add(new AttributeClass(attribute, (EEntity_definition) attribute_domain));
    }
    else if (attribute_domain instanceof EDefined_type) {
      // defined types may include select types, aggregates, etc.
      EEntity dt_domain = ((EDefined_type) attribute_domain).getDomain(null);
      if (dt_domain instanceof ESelect_type) {
        processAttributeDomainSelectTypeX(attribute, (ESelect_type) dt_domain, (EDefined_type) attribute_domain, domain, result, null, processed_);

      }
      else if (dt_domain instanceof EDefined_type) {
        // just a chain of defined types, have to get the inner one
        // this TypeClass may not be ok
        //TypeClass tc = new TypeClass((EDefined_type)dt_domain,(EDefined_type)attribute_domain, ntc.previous, ntc);
        // perhaps just put null instead of tc
        //NamedTypeClass ntc2 = new NamedTypeClass((ENamed_type)dt_domain, deft, tc, ntc);
        processAttributeDomainX(result, dt_domain, attribute, domain, null, processed_);

      }
      else if (dt_domain instanceof EAggregation_type) {
        // a defined type with underlying aggregate - aggregates are interesting, if their elements are entities, directly or not
        processAttributeDomainAggregationTypeX(attribute, (EAggregation_type) dt_domain, (EDefined_type) attribute_domain, domain, result, null,
            processed_);
        // TODO
      }
      else {
        // perhaps simple types wrapped  in defined types, enumerations, etc., no interest to us, not users
      }

    }
    else if (attribute_domain instanceof EAggregation_type) {
      processAttributeDomainAggregationTypeX(attribute, (EAggregation_type) attribute_domain, null, domain, result, null, processed_);
      // TODO
    }
    else {
      // also remaining non-entity types with no interest to us, such as simple types
    }
  }

  void processAttributeDomainAggregationTypeX(EAttribute attribute, EAggregation_type at, EDefined_type dt, ASdaiModel domain, ArrayList result,
      NamedTypeClass ntc, HashSet processed_) throws SdaiException {
    EEntity aggr_domain = at.getElement_type(null);
    if (aggr_domain instanceof EEntity_definition) {
      result.add(new AttributeClass(attribute, (EEntity_definition) aggr_domain, dt, ntc));
    }
    else if (aggr_domain instanceof EDefined_type) {
      // may be nested defined types or select
      NamedTypeClass ntc_d = null;
      processAttributeDomainX(result, aggr_domain, attribute, domain, ntc_d, processed_);
    }
    else if (aggr_domain instanceof EAggregation_type) {
      // nested aggregate
      NamedTypeClass ntc_a = null;
      processAttributeDomainAggregationTypeX(attribute, (EAggregation_type) aggr_domain, null, domain, result, ntc_a, processed_);
    }
    else {
      // probably not interesting to us
    }

  }

  void processAttributeDomain(ArrayList result, EEntity attribute_domain, EAttribute attribute, ASdaiModel domain) throws SdaiException {
    if (attribute_domain instanceof EEntity_definition) {
      result.add(new AttributeClass(attribute, (EEntity_definition) attribute_domain));
    }
    else if (attribute_domain instanceof EDefined_type) {
      // defined types may include select types, aggregates, etc.
      EEntity dt_domain = ((EDefined_type) attribute_domain).getDomain(null);
      if (dt_domain instanceof ESelect_type) {
        processAttributeDomainSelectType(attribute, (ESelect_type) dt_domain, (EDefined_type) attribute_domain, domain, result);
      }
    }
    else {
      // will have to add direct aggregates here,
      // also remaining non-enitity types with no interest to us
    }
  }

  void processAttributeDomainSelectTypeX(EAttribute attribute, ESelect_type st, EDefined_type dt, ASdaiModel domain, ArrayList result, NamedTypeClass ntc2,
      HashSet processed_) throws SdaiException {
    if (st == null) {
      return;
    }
    else if (!processed_.add(st)) {
      return;
    }

    // the outer one has NamedTypeClass = null
    HashSet elements = getAllSelectionsX(st, dt, domain, ntc2);
    Iterator it_e = elements.iterator();
    while (it_e.hasNext()) {
      NamedTypeClass ntc = (NamedTypeClass) it_e.next();
      ntc.from = ntc2;
      ENamed_type element = ntc.current;
      if (element instanceof EEntity_definition) {
        result.add(new AttributeClass(attribute, (EEntity_definition) element, dt, ntc));
      }
      else if (element instanceof EDefined_type) {
        EEntity element_domain = ((EDefined_type) element).getDomain(null);
        if (element_domain instanceof ESelect_type) {
          processAttributeDomainSelectTypeX(attribute, (ESelect_type) element_domain, (EDefined_type) element, domain, result, ntc, processed_);
        }
        else if (element_domain instanceof EAggregation_type) {
          processAttributeDomainAggregationTypeX(attribute, (EAggregation_type) element_domain, (EDefined_type) element, domain, result, ntc,
              processed_);
        }
        else if (element_domain instanceof EDefined_type) {
          // just a chain of defined types
          processAttributeDomainX(result, element_domain, attribute, domain, ntc, processed_);
        }
        else {
          // may not be interesting to us
        }
      }
      else {
        // internal error, can't be anything else
      }
    }
  }

  void processAttributeDomainSelectType(EAttribute attribute, ESelect_type st, EDefined_type dt, ASdaiModel domain, ArrayList result) throws SdaiException {

    HashSet elements = getAllSelections(st, dt, domain);
    Iterator it_e = elements.iterator();
    while (it_e.hasNext()) {
      ENamed_type element = (ENamed_type) it_e.next();
      if (element instanceof EEntity_definition) {
        result.add(new AttributeClass(attribute, (EEntity_definition) element, dt));
      }
      else if (element instanceof ESelect_type) {
        processAttributeDomainSelectType(attribute, st, dt, domain, result);
      }
      else {
      }
    }
  }

  /**
   * getSelections - seems not to work with extended dictionary schema, so artificial local implementation instead
   *
   * @param st
   * @param dt
   * @param domain
   * @return
   * @throws SdaiException
   */
  static ANamed_type getSelections(ESelect_type st, EDefined_type dt, ASdaiModel domain) throws SdaiException {
    ANamed_type selections = null;
    if (st.testLocal_selections(null)) {
      selections = st.getLocal_selections(null);
    }
    if (st instanceof EExtended_select_type) {
      // this adds only in backward direction, from the select types that are extended by this type
      selections = addSelectionsFromExtensible((EExtended_select_type) st, selections);
    }
    if (st instanceof EExtensible_select_type) {
      // what about future types that further extend this type?
//			selections = addSelectionsFromExtensions((EExtensible_select_type)st, dt, selections, domain);
    }

    return selections;

  }

  static ANamed_type addSelectionsFromExtensible(EExtended_select_type st, ANamed_type current_selections) throws SdaiException {
    ANamed_type l_selections = null;
    ANamed_type selections = null;
    ESelect_type prior = (ESelect_type) st.getIs_based_on(null).getDomain(null);
    if (prior.testLocal_selections(null)) {
      l_selections = prior.getLocal_selections(null);
      selections = new ANamed_type();
      for (int i = 1; i < l_selections.getMemberCount() + 1; i++) {
        ENamed_type element = (ENamed_type) l_selections.getByIndexEntity(i);
        selections.addUnordered(element);
      }
    }
    if (current_selections != null) {
      if (current_selections.getMemberCount() > 0) {
        if (selections == null) {
          selections = new ANamed_type();
        }
        for (int i = 1; i < current_selections.getMemberCount() + 1; i++) {
          ENamed_type element = (ENamed_type) current_selections.getByIndexEntity(i);
          if (!(selections.isMember(element))) {
            selections.addUnordered(element);
          }
        }
      }
    }
    if (prior instanceof EExtended_select_type) {
      selections = addSelectionsFromExtensible((EExtended_select_type) prior, selections);
    }
    return selections;
  }

  //################################ for extensible enumerations
  // made from the corresponding implementation for extensible select types
  // getEnumerationElements - seems not to work with extended dictionary schema, so artificial local implementation instead
  Vector getElements(EEnumeration_type st) throws SdaiException {
    Vector l_selections = null;
    Vector selections = null;
    if (st.testLocal_elements(null)) {
      l_selections = getLocal_elements(st);
    }
    if (st instanceof EExtended_enumeration_type) {
      selections = addElementsFromExtensible((EExtended_enumeration_type) st, l_selections);
      return selections;
    }
    return l_selections;
  }

  static Vector addElementsFromExtensible(EExtended_enumeration_type st, Vector current_selections) throws SdaiException {
    Vector l_selections = null;
    Vector selections = null;
    EEnumeration_type prior = (EEnumeration_type) st.getIs_based_on(null).getDomain(null);
    if (prior.testLocal_elements(null)) {
      l_selections = getLocal_elements(prior);
      selections = new Vector();
      for (int i = 0; i < l_selections.size(); i++) {
        String element = (String) l_selections.elementAt(i);
        selections.addElement(element);
      }
    }
    if (current_selections != null) {
      if (current_selections.size() > 0) {
        if (selections == null) {
          selections = new Vector();
        }
        for (int i = 0; i < current_selections.size(); i++) {
          String element = (String) current_selections.elementAt(i);
          if (!(selections.contains(element))) {
            selections.addElement(element);
          }
        }
      }
    }
    if (prior instanceof EExtended_enumeration_type) {
      selections = addElementsFromExtensible((EExtended_enumeration_type) prior, selections);
    }
    return selections;
  }

  // for extensible enumerations
  static Vector getLocal_elements(EEnumeration_type et) throws SdaiException {
    Vector result = new Vector();
    A_string elements = et.getLocal_elements(null);
    for (int i = 1; i < elements.getMemberCount() + 1; i++) {
      String element = elements.getByIndex(i);
      result.addElement(element);
    }
    return result;
  }

  private class AttributeClass {
    EEntity_definition the_entity = null;
    EAttribute the_attribute = null;
    EEntity_definition the_parent = null;
    EDefined_type dt = null;
    HashSet entity_types = null;
    // Object the_path = null;
    NamedTypeClass named_type = null;
    int if_aggregate = 0; //  1 - aggregate, 2 - array, 3 - bag, 4 - list, 5 - set

    AttributeClass(EAttribute attribute, EEntity_definition entity_type) throws SdaiException {
      the_attribute = attribute;
      the_entity = entity_type;
      entity_types = new HashSet();
      entity_types.add(entity_type);
      the_parent = (EEntity_definition) attribute.getParent(null);
      named_type = null;
      if_aggregate = 0;
    }

    AttributeClass(EAttribute attribute, EEntity_definition entity_type, EDefined_type def_type) throws SdaiException {
      the_attribute = attribute;
      the_entity = entity_type;
      entity_types = new HashSet();
      entity_types.add(entity_type);
      the_parent = (EEntity_definition) attribute.getParent(null);
      dt = def_type;
      named_type = null;
      if_aggregate = 0;
    }

    AttributeClass(EAttribute attribute, EEntity_definition entity_type, EDefined_type def_type, NamedTypeClass ntc) throws SdaiException {
      the_attribute = attribute;
      the_entity = entity_type;
      entity_types = new HashSet();
      entity_types.add(entity_type);
      the_parent = (EEntity_definition) attribute.getParent(null);
      dt = def_type;
      named_type = ntc;
      if_aggregate = 0;
    }

    public String toString() {
      String result = "\n\t--- printing AttributeClass: " + this.hashCode() + "\n";
      try {
        if (the_entity != null) {
          result += "\t\tthe_entity: " + the_entity.getName(null) + "\n";
        }
        else {
          result += "\t\tthe_entity is NULL\n";
        }
        if (the_attribute != null) {
          result += "\t\tthe_attribute: " + the_attribute.getName(null) + "\n";
        }
        else {
          result += "\t\tthe_attribute is NULL\n";
        }
        if (the_parent != null) {
          result += "\t\tthe_parent: " + the_parent.getName(null) + "\n";
        }
        else {
          result += "\t\tthe_parent is NULL\n";
        }
        if (dt != null) {
          result += "\t\tdt: " + dt.getName(null) + "\n";
        }
        else {
          result += "\t\tdt is NULL\n";
        }
        if (entity_types != null) {
          result += "\t\tentity_types size: " + entity_types.size() + "\n";
        }
        else {
          result += "\t\tentity_types is NULL\n";
        }
        if (named_type != null) {
          result += "\t\tnamed_type: " + named_type + "\n";
        }
        else {
          result += "\t\tnamed_type is NULL\n";
        }
        switch (if_aggregate) {
          case 0: // not an aggregate
            result += "\t\tnot an aggregate\n";
            break;
          case 1: // aggregate
            result += "\t\tan aggregate\n";
            break;
          case 2: // array
            result += "\t\tan array\n";
            break;
          case 3: // bag
            result += "\t\ta bag\n";
            break;
          case 4: // list
            result += "\t\ta list\n";
            break;
          case 5: // set
            result += "\t\ta set\n";
            break;
          default:
            result += "\t\tnot an aggregate\n";
            break;
        }
      }
      catch (SdaiException e) {
        result += "\t\tSdaiException occurred\n";
      }
      return result;
    }

  }

  boolean getUsersY(EEntity entity, ASdaiModel domain, HashSet users2, HashSet visited) throws SdaiException {

    if (!(visited.add(entity))) {
      // already been here
      return false;
    }

    boolean is_extensible_select = false;
    int size = users2.size();

    if (entity instanceof EEntity_definition) {
      EEntity_definition definition = (EEntity_definition) entity;
      AEntity supertypes = definition.getGeneric_supertypes(null);
      SdaiIterator it_super = supertypes.createIterator();
      while (it_super.next()) {
        EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(it_super);
        getUsersY(supertype, domain, users2, visited);
      }
    }
    // this is never true, you have to start with defined_type
    if (entity instanceof EDefined_type) {
      EEntity ut = ((EDefined_type) entity).getDomain(null);
      if (ut instanceof EExtended_select_type) {
        EDefined_type st = ((EExtended_select_type) ut).getIs_based_on(null);
        getUsersY(st, domain, users2, visited);
      }
      if (ut instanceof EExtensible_select_type) {
        is_extensible_select = true;
      }
    }
    AEntity users = new AEntity();
    entity.findEntityInstanceUsers(domain, users);

    SdaiIterator it_users = users.createIterator();
    while (it_users.next()) {
      EEntity user = users.getCurrentMemberEntity(it_users);
      // what about non-explicit attributes?
      if (user instanceof EExplicit_attribute) {
        users2.add(user);
        getUsersY(user, domain, users2, visited);
      }
      else if (user instanceof EAggregation_type) {

        AAttribute attrs = new AAttribute();
        SdaiIterator it_attrs;
        // what about non-explicit attributes?
        CExplicit_attribute.usedinDomain(null, user, domain, attrs);
        it_attrs = attrs.createIterator();
        while (it_attrs.next()) {
          EAttribute attribute = (EAttribute) attrs.getCurrentMemberObject(it_attrs);
          users2.add(attribute);
          getUsersY(attribute, domain, users2, visited);
        }
        // add also defined types here ? (later)
        ADefined_type defs = new ADefined_type();
        SdaiIterator it_defs;
        CDefined_type.usedinDomain(null, user, domain, defs);
        it_defs = defs.createIterator();
        while (it_defs.next()) {
          EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
          users2.add(def);
          getUsersY(def, domain, users2, visited);
        }
      }
      else if (user instanceof ESelect_type) {
        ADefined_type defs = new ADefined_type();
        SdaiIterator it_defs;
        CDefined_type.usedinDomain(null, user, domain, defs);
        it_defs = defs.createIterator();
        while (it_defs.next()) {
          EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);

          // if entity is extensible select type and def is its based_on type, then do not add it
          if ((user instanceof EExtended_select_type) && (is_extensible_select)) {
            if (((EExtended_select_type) user).getIs_based_on(null) != entity) {
              users2.add(def);
            }
            getUsersY(def, domain, users2, visited);
          }
          else {
            users2.add(def);
            getUsersY(def, domain, users2, visited);
          }
        }
      }
    } // while

    if (users2.size() > size) {
      return true;
    }
    else {
      return false;
    }
  }

  private void getSelectExtensionsRecursiveX(EDefined_type dt, HashSet types, ASdaiModel domain, TypeClass tc) throws SdaiException {

    AExtended_select_type ests = new AExtended_select_type();
    SdaiIterator it_ests;
    CExtended_select_type.usedinIs_based_on(null, dt, domain, ests);
    it_ests = ests.createIterator();
    while (it_ests.next()) {
      EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
      // est is select_type, we need the defined_type with this underlying type

      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, est, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        TypeClass tc2 = new TypeClass(def2, dt, tc);
        types.add(tc2);
        getSelectExtensionsRecursiveX(def2, types, domain, tc2);

      }
    }
  }

  private class TypeClass {
    EDefined_type current_type = null;
    EDefined_type previous_type = null;
    TypeClass previous = null;
    NamedTypeClass initial = null;

    TypeClass(EDefined_type current_type, EDefined_type previous_type, TypeClass previous) {
      this.current_type = current_type;
      this.previous_type = previous_type;
      this.previous = previous;
      initial = null;
    }

    TypeClass(EDefined_type current_type, EDefined_type previous_type, TypeClass previous, NamedTypeClass initial) {
      this.current_type = current_type;
      this.previous_type = previous_type;
      this.previous = previous;
      this.initial = initial;
    }

    public String toString() {
      String result = "\n\t\t\t--------- printing TypeClass: " + this.hashCode() + "\n";
      try {
        if (current_type != null) {
          result += "\t\t\t\tcurrent_type: " + current_type.getName(null) + "\n";
        }
        else {
          result += "\t\t\t\tcurrent_type is NULL\n";
        }
        if (previous_type != null) {
          result += "\t\t\t\tprevious_type: " + previous_type.getName(null) + "\n";
        }
        else {
          result += "\t\t\t\tprevious_type is NULL\n";
        }
        if (previous != null) {
          result += "\t\t\t\t-DEEPER- previous: " + previous + "\n";
        }
        else {
          result += "\t\t\t\tprevious is NULL\n";
        }
        if (initial != null) {
          result += "\t\t\t\t--DEEPER STILL-- initial: " + initial + "\n";
        }
        else {
          result += "\t\t\t\tinitial is NULL\n";
        }
      }
      catch (SdaiException e) {
        result += "\t\t\t\tSdaiException occurred\n";
      }
      return result;
    }
  }

  private class NamedTypeClass {
    ENamed_type current = null;
    EDefined_type previous_type = null;
    TypeClass previous = null;
    NamedTypeClass from = null;
    NamedTypeClass owner = null;

    NamedTypeClass(ENamed_type current, EDefined_type previous_type, TypeClass previous, NamedTypeClass owner) {
      this.current = current;
      this.previous_type = previous_type;
      this.previous = previous;
      from = null;
      this.owner = owner;
    }

    public String toString() {
      String result = "\n\t\t------ printing NamedTypeClass: " + this.hashCode() + "\n";
      try {
        if (current != null) {
          result += "\t\t\tcurrent: " + current.getName(null) + "\n";
        }
        else {
          result += "\t\t\tcurrent is NULL\n";
        }
        if (previous_type != null) {
          result += "\t\t\tprevious_type: " + previous_type.getName(null) + "\n";
        }
        else {
          result += "\t\t\tprevious_type is NULL\n";
        }
        if (previous != null) {
          result += "\t\t\t--TC-- previous: " + previous + "\n";
        }
        else {
          result += "\t\t\tprevious is NULL\n";
        }
        if (from != null) {
          result += "\t\t\t-NTC- from: " + from + "\n";
        }
        else {
          result += "\t\t\tfrom is NULL\n";
        }
        if (owner != null) {
          result += "\t\t\t-NTC- owner: " + owner + "\n";
        }
        else {
          result += "\t\t\towner is NULL\n";
        }
      }
      catch (SdaiException e) {
        result += "\t\t\tSdaiException occurred\n";
      }

      return result;
    }
  }

  private void getSelectExtensionsRecursive(EDefined_type dt, HashSet types, ASdaiModel domain) throws SdaiException {

    AExtended_select_type ests = new AExtended_select_type();
    SdaiIterator it_ests;
    CExtended_select_type.usedinIs_based_on(null, dt, domain, ests);
    it_ests = ests.createIterator();
    while (it_ests.next()) {
      EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
      // est is select_type, we need the defined_type with this underlying type
      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, est, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        types.add(def2);
        getSelectExtensionsRecursive(def2, types, domain);

      }
    }
  }

  private void getSelectExtensionsRecursiveNew(EDefined_type dt, HashSet types, ASdaiModel domain, HashMap non_final_users, ESelect_type userx)
      throws SdaiException {

    AExtended_select_type ests = new AExtended_select_type();
    SdaiIterator it_ests;
    CExtended_select_type.usedinIs_based_on(null, dt, domain, ests);
    it_ests = ests.createIterator();
    while (it_ests.next()) {
      EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
      // est is select_type, we need the defined_type with this underlying type
      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, est, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        types.add(def2);
        getSelectExtensionsRecursiveNew(def2, types, domain, non_final_users, userx);

      }
    }
  }

  void addSelectUsersXYZ(ArrayList final_users, ESelect_type user, EDefined_type def, ASdaiModel domain) throws SdaiException {
    /*
     * we are interested here to add the final users - attributes
     *
     * first, we want to find all the related select types:
     *
     * if b extends a, and c extends b, and d extends c, and e also extends c,
     * a<-b<-c<-d
     * <-e
     * and our type is c,
     * we go back and find b and a, and then go up and find also d and e,
     * a user of d or e is also a user of c,
     * but also the user of b and a is an user of c . Or is it? Previous implementation suggests it.
     * I would be inclined to take the users of c, d, and e, but not a and b.
     *
     * ok, let's make it flexible - to go back to the root extensible type or not
     */

    HashSet types = new HashSet();
    ESelect_type sel_type = user;
    EDefined_type dt = def;

    if (flag_go_to_extensible_root) {
      for (; ; ) {
        if (sel_type instanceof EExtended_select_type) {
          dt = ((EExtended_select_type) sel_type).getIs_based_on(null);
          sel_type = (ESelect_type) dt.getDomain(null);
        }
        else {
          break;
        }
      } // for
    }

    if (sel_type instanceof EExtensible_select_type) {
      // see if the users already calculated and cached
      // TODO

//**************** start calculations

      // ok, so now, find all the based_on types recursively, and then find their users,
      // now let's go forward
      AExtended_select_type ests = new AExtended_select_type();
      SdaiIterator it_ests;
      CExtended_select_type.usedinIs_based_on(null, dt, domain, ests);
      it_ests = ests.createIterator();
      while (it_ests.next()) {
        EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
        // est is select_type, we need the defined_type with this underlying type
        ADefined_type defs = new ADefined_type();
        SdaiIterator it_defs;
        CDefined_type.usedinDomain(null, est, domain, defs);
        it_defs = defs.createIterator();
        while (it_defs.next()) {
          EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
          types.add(def2);
          getSelectExtensionsRecursive(def2, types, domain);
        }
      } // while
      // ok, we have all the selects in HashSet types now,
      // let's calculate users for each of them, and then sort everything

      Iterator types_iter = types.iterator();
      while (types_iter.hasNext()) {
        EDefined_type deft = (EDefined_type) types_iter.next();

        // not yet calculated, not found in cache
        AEntity users = new AEntity();
        deft.findEntityInstanceUsers(domain, users);
        // RR a fix: including indirect users as well, then non-final users will be ignored

        SdaiIterator it_users = users.createIterator();
        while (it_users.next()) {
          EEntity user2 = users.getCurrentMemberEntity(it_users);
          if (user2 instanceof EExplicit_attribute) {
            final_users.add(null);
          }
          else if (user2 instanceof EAggregation_type) {

            AAttribute attrs = new AAttribute();
            SdaiIterator it_attrs;
            CExplicit_attribute.usedinDomain(null, user2, domain, attrs);
            it_attrs = attrs.createIterator();
            while (it_attrs.next()) {
              final_users.add(null);
            }
            // add also defined types here ? (later)
            ADefined_type defs = new ADefined_type();
            SdaiIterator it_defs;
            CDefined_type.usedinDomain(null, user2, domain, defs);
            it_defs = defs.createIterator();
            while (it_defs.next()) {
              final_users.add(null); // what is this
            }
          }
          else if (user2 instanceof ESelect_type) {
            ADefined_type defs = new ADefined_type();
            SdaiIterator it_defs;
            CDefined_type.usedinDomain(null, user2, domain, defs);
            it_defs = defs.createIterator();
            while (it_defs.next()) {
              final_users.add(null);
            }
          }
        } // while
      } //while - through types

//**************** end calculations

    }
    else { // not extensible select type
      // probably the initial select type which is not extended nor extensible
      // previously not implemented and treated as internal error,
      // but previously always went back to the root,
      // if it is chosen to continue from the current type instead, it may be non-extensible
      // in such a case, no recursion is needed to find all the extensions, just proceed with this one type
      System.out.println("WARNING: not yet supported - select type not extensible");

      // adding something, see what happens:

      ADefined_type defsns = new ADefined_type();
      SdaiIterator it_defsns;
      CDefined_type.usedinDomain(null, sel_type, domain, defsns);
      it_defsns = defsns.createIterator();
      while (it_defsns.next()) {
        final_users.add(null);
      }

    }
  }

  void addSelectUsersNew(HashMap non_final_users, ESelect_type user, EDefined_type def, ASdaiModel domain, HashSet already_done) throws SdaiException {

    if (!already_done.add(user)) {
      return;
    }

    /*
     * we are interested here to add the final users - attributes
     *
     * first, we want to find all the related select types:
     *
     * if b extends a, and c extends b, and d extends c, and e also extends c,
     * a<-b<-c<-d
     * <-e
     * and our type is c,
     * we go back and find b and a, and then go up and find also d and e,
     * a user of d or e is also a user of c,
     * but also the user of b and a is a user of c . Or is it? Previous implementation suggests it.
     * I would be inclined to take the users of c, d, and e, but not a and b.
     *
     * ok, let's make it flexible - to go back to the root extensible type or not
     */

    HashSet types = new HashSet();
    ESelect_type sel_type = user;
    EDefined_type dt = def;

    if (flag_go_to_extensible_root) {
      for (; ; ) {
        if (sel_type instanceof EExtended_select_type) {
          dt = ((EExtended_select_type) sel_type).getIs_based_on(null);
          sel_type = (ESelect_type) dt.getDomain(null);
        }
        else {
          break;
        }
      } // for
    }

    if (sel_type instanceof EExtensible_select_type) {
      // see if the users already calculated and cached
      // TODO
//**************** start calculations

      // ok, so now, find all the based_on types recursively, and then find their users,
      // now let's go forward
      AExtended_select_type ests = new AExtended_select_type();
      SdaiIterator it_ests;
      CExtended_select_type.usedinIs_based_on(null, dt, domain, ests);
      it_ests = ests.createIterator();
      while (it_ests.next()) {
        EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
        // est is select_type, we need the defined_type with this underlying type
        ADefined_type defs = new ADefined_type();
        SdaiIterator it_defs;
        CDefined_type.usedinDomain(null, est, domain, defs);
        it_defs = defs.createIterator();
        while (it_defs.next()) {
          EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
          types.add(def2);

          getSelectExtensionsRecursiveNew(def2, types, domain, non_final_users, user);
        }
      } // while
      // ok, we have all the selects in HashSet types now,
      // let's calculate users for each of them, and then sort everything

      Iterator types_iter = types.iterator();
      while (types_iter.hasNext()) {

        EDefined_type deft = (EDefined_type) types_iter.next();

        // not yet calculated, not found in cache
        AEntity users = new AEntity();
        deft.findEntityInstanceUsers(domain, users);
        // RR a fix: including indirect users as well, then non-final users will be ignored

        SdaiIterator it_users = users.createIterator();
        while (it_users.next()) {
          EEntity user2 = users.getCurrentMemberEntity(it_users);
          if (user2 instanceof EExplicit_attribute) {
            if (!(non_final_users.containsKey(user2))) {
              non_final_users.put(user2, null);
            }
          }
          else if (user2 instanceof EAggregation_type) {

            AAttribute attrs = new AAttribute();
            SdaiIterator it_attrs;
            CExplicit_attribute.usedinDomain(null, user2, domain, attrs);
            it_attrs = attrs.createIterator();
            while (it_attrs.next()) {
              EAttribute attribute = (EAttribute) attrs.getCurrentMemberObject(it_attrs);
              if (!(non_final_users.containsKey(attribute))) {
                non_final_users.put(attribute, null);
              }
            }
            // add also defined types here ? (later)
            ADefined_type defs = new ADefined_type();
            SdaiIterator it_defs;
            CDefined_type.usedinDomain(null, user2, domain, defs);
            it_defs = defs.createIterator();
            while (it_defs.next()) {
              EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
              if (!(non_final_users.containsKey(def2))) {
                non_final_users.put(def2, null); // what is this
              }
            }
          }
          else if (user2 instanceof ESelect_type) {
            if (user2 != user) {

              ADefined_type defs = new ADefined_type();
              SdaiIterator it_defs;
              CDefined_type.usedinDomain(null, user2, domain, defs);
              it_defs = defs.createIterator();
              while (it_defs.next()) {
                EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
                if (!(non_final_users.containsKey(def2))) {
                  non_final_users.put(def2, null);
                }
              }
            } // not user
          }
          else if (user2 instanceof EDefined_type) {
            if (((EDefined_type) user2).getDomain(null) != null) {
              if (!(non_final_users.containsKey(user2))) {
                non_final_users.put(user2, null);
              }
            }
          } // not user
        } // while
      } //while - through types

//**************** end calculations

    }
    else { // not extensible select type
      // probably the initial select type which is not extended nor extensible
      // previously not implemented and treated as internal error,
      // but previously always went back to the root,
      // if it is chosen to continue from the current type instead, it may be non-extensible
      // in such a case, no recursion is needed to find all the extensions, just proceed with this one type
      System.out.println("WARNING: not yet supported - select type not extensible");

      // adding something, see what happens:

      if (sel_type != user) {

        ADefined_type defsns = new ADefined_type();
        SdaiIterator it_defsns;
        CDefined_type.usedinDomain(null, sel_type, domain, defsns);
        it_defsns = defsns.createIterator();
        while (it_defsns.next()) {
          EDefined_type def2ns = (EDefined_type) defsns.getCurrentMemberObject(it_defsns);
          if (!(non_final_users.containsKey(def2ns))) {
            non_final_users.put(def2ns, null);
          }
        }

      }
    }
  }

  // this here moved from the main method printing users
  // we want to find users recursively until no more users
  // create chains, hopefully ending with final users (explicit or derived attributes)

  void findChildrenUsers(EEntity user, ASdaiModel domain, HashMap non_final_users) throws SdaiException {

    if (user instanceof EAggregation_type) {

      AAttribute attrs = new AAttribute();
      SdaiIterator it_attrs;
      // what about non-explicit attributes? Add also derived?
      CExplicit_attribute.usedinDomain(null, user, domain, attrs);
      it_attrs = attrs.createIterator();
      while (it_attrs.next()) {
        EAttribute attribute = (EAttribute) attrs.getCurrentMemberObject(it_attrs);
        if (!(non_final_users.containsKey(attribute))) {
          non_final_users.put(attribute, null);
        }
      }

      // adding also derived
      attrs = new AAttribute();
      CDerived_attribute.usedinDomain(null, user, domain, attrs);
      it_attrs = attrs.createIterator();
      while (it_attrs.next()) {
        EAttribute attribute = (EAttribute) attrs.getCurrentMemberObject(it_attrs);
        if (!(non_final_users.containsKey(attribute))) {
          non_final_users.put(attribute, null);
        }
      }

      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, user, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        if (!(non_final_users.containsKey(def))) {
          non_final_users.put(def, null);
        }
      }
    }
    else if (user instanceof ESelect_type) {
      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, user, domain, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        if (already_done == null) {
          already_done = new HashSet();
        }
        addSelectUsersNew(non_final_users, (ESelect_type) user, def, domain, already_done);
      }
    }
    else if (user instanceof EDefined_type) {
      // could it happen?

      AEntity users = new AEntity();
      user.findEntityInstanceUsers(domain, users);

      SdaiIterator it_users = users.createIterator();
      while (it_users.next()) {
        EEntity user2 = users.getCurrentMemberEntity(it_users);

        if (user2 instanceof EExplicit_attribute) {
          if (!(non_final_users.containsKey(user2))) {
            non_final_users.put(user2, null);
          }
        }
        else if (user2 instanceof EDerived_attribute) {
          if (!(non_final_users.containsKey(user2))) {
            non_final_users.put(user2, null);
          }
        }
        else {
          if (!(non_final_users.containsKey(user2))) {
            non_final_users.put(user2, null);
          }
        }
      } // while users
    }
  }

  private StringBuffer printEntityUsersXX(StringBuffer result, EEntity entity, ASdaiModel domain) throws SdaiException {
    boolean is_extensible_select = false;
    if (entity instanceof EEntity_definition) {
      EEntity_definition definition = (EEntity_definition) entity;
      AEntity supertypes = definition.getGeneric_supertypes(null);
      SdaiIterator it_super = supertypes.createIterator();
      while (it_super.next()) {
        EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(it_super);
        printEntityUsersXX(result, supertype, domain);
      }
      printHRef(result, getComplexNameOp(definition.getName(null)), getSchemaRefOp(definition, schema));
      println(result);
    }

    if (entity instanceof EDefined_type) {
      EEntity ut = ((EDefined_type) entity).getDomain(null);
      if (ut instanceof EExtensible_select_type) {
        is_extensible_select = true;
      }
    }

    int resultStart = result.length();
    EEntity[] sortedSet;
    int iSortedSetCount = 0;
    // -- Only one tree set is created per entity.
    // using generation and copying of generated ones. HashMap for storage.
    String sCurrentEntityPL = entity.getPersistentLabel();
    if (hmUsedEntities.containsKey(sCurrentEntityPL)) {
      sortedSet = (EEntity[]) hmUsedEntities.get(sCurrentEntityPL);
      iSortedSetCount = sortedSet.length;
    }
    else {
      AEntity users = new AEntity();
      AEntity final_users = new AEntity();
      entity.findEntityInstanceUsers(domain, users);
// RR a fix: including indirect users as well, then non-final users will be ignored

      SdaiIterator it_users = users.createIterator();
      while (it_users.next()) {
        EEntity user = users.getCurrentMemberEntity(it_users);
        // what about non-explicit attributes?
        if (user instanceof EExplicit_attribute) {
          final_users.addUnordered(user);
        }
        else if (user instanceof EDerived_attribute) {
          final_users.addUnordered(user);
        }
        else if (user instanceof EAggregation_type) {

          AAttribute attrs = new AAttribute();
          SdaiIterator it_attrs;
          // what about non-explicit attributes?
          CExplicit_attribute.usedinDomain(null, user, domain, attrs);
          it_attrs = attrs.createIterator();
          while (it_attrs.next()) {
            EAttribute attribute = (EAttribute) attrs.getCurrentMemberObject(it_attrs);
            final_users.addUnordered(attribute);
          }
          // add also defined types here ? (later)
          ADefined_type defs = new ADefined_type();
          SdaiIterator it_defs;
          CDefined_type.usedinDomain(null, user, domain, defs);
          it_defs = defs.createIterator();
          while (it_defs.next()) {
            EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
            final_users.addUnordered(def);
          }
        }
        else if (user instanceof ESelect_type) {
          ADefined_type defs = new ADefined_type();
          SdaiIterator it_defs;
          CDefined_type.usedinDomain(null, user, domain, defs);
          it_defs = defs.createIterator();
          while (it_defs.next()) {
            EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);

            // if entity is extensible select type and def is its based_on type, then do not add it
            if ((user instanceof EExtended_select_type) && (is_extensible_select)) {
              if (((EExtended_select_type) user).getIs_based_on(null) != entity) {
                final_users.addUnordered(def);
              }
            }
            else {
              final_users.addUnordered(def);
            }
          }
        }
      } // while
      sortedSet = new EEntity[final_users.getMemberCount()];
      SdaiIterator it_final_users = final_users.createIterator();
      while (it_final_users.next()) {
        sortedSet[iSortedSetCount] = final_users.getCurrentMemberEntity(it_final_users);
        iSortedSetCount++;
      }
      Arrays.sort(sortedSet, new SorterForUsers());
      hmUsedEntities.put(sCurrentEntityPL, sortedSet);
    }
    for (int i = 0; i < iSortedSetCount; i++) {
      EEntity user = sortedSet[i];
      if (user instanceof EExplicit_attribute) {
        EAttribute attribute = (EAttribute) user;
        EEntity_definition parent = (EEntity_definition) attribute.getParent(null);
        if (parent != entity) {
          result.append(printUnclosedTab());
          printHRef(result, getEntityAttrNameOp(parent, attribute), getSchemaRefDicOp(parent, schema));
          result.append("<BR>\n");
        }
      }
      else if (user instanceof EDerived_attribute) {
        EAttribute attribute = (EAttribute) user;
        EEntity_definition parent = (EEntity_definition) attribute.getParent(null);
        if (parent != entity) {
          result.append(printUnclosedTab());
          printHRef(result, getEntityAttrNameOp(parent, attribute), getSchemaRefDicOp(parent, schema));
          result.append("<BR>\n");
        }
      }
      else if (user instanceof EAggregation_type) {
        // do nothing, resolved to explicit attributes (or defined types)
      }
      else if (user instanceof ESelect_type) {
        // do nothing, resolved to defined_types
      }
      else if (user instanceof EDefined_type) {
        EDefined_type def = (EDefined_type) user;
        result.append(printUnclosedTab());
        printHRef(result, getStringOp(def.getName(null)), getSchemaRefDicOp(def, schema));
        result.append("<BR>\n");
        // new stuff - if select type, also print the users of it and the users of all the related extensible types with indentation
        EEntity dtdomain = def.getDomain(null);
        if (dtdomain instanceof ESelect_type) {
          printExtensibleSelectTypesX(result, (ESelect_type) dtdomain, def, domain);
        }
      }
    }
    if (resultStart == result.length()) {
      printTab(result, "-");
    }
    return result;
  }

  private static StrOp getSchemaRefOp(final EEntity_definition definition, final ESchema_definition schema) {
    return new StrOp() {
      public void op(StringBuffer result) throws SdaiException {
        getSchemaNameIfDiffer(result, definition, schema);
        getUpper(result, getComplexNameOp(definition.getName(null)));
        result.append(".html");
      }
    };
  }

  private static StrOp getSchemaRefDicOp(final EEntity entity, final ESchema_definition schema) {
    return new StrOp() {
      public void op(StringBuffer result) throws SdaiException {
        getSchemaNameIfDiffer(result, entity, schema);
        getUpper(result, getDictionaryEntityNameOp(entity));
        result.append(".html");
      }
    };
  }

  private static StrOp getEntityAttrNameOp(final EEntity_definition definition, final EAttribute attribute) {
    return new StrOp() {
      public void op(StringBuffer result) throws SdaiException {
        result.append(definition.getName(null));
        result.append(".");
        result.append(attribute.getName(null));
      }
    };
  }

  HashSet getAllSelectionsX(ESelect_type st, EDefined_type dt, ASdaiModel domain, NamedTypeClass ntc) throws SdaiException {
    HashSet types = new HashSet();
    ESelect_type sel_type = st;
    EDefined_type def_type = dt;
    HashSet all_local_selections = new HashSet();
    HashSet all_local_selections2 = new HashSet();
    TypeClass tc = null;

    // get all the types in the chain backwards that are extended by this type
    for (; ; ) {
      if (sel_type instanceof EExtended_select_type) {
        def_type = ((EExtended_select_type) sel_type).getIs_based_on(null);
        TypeClass tc2;
        if (def_type == dt) {
          // hm this will never happen
          tc2 = new TypeClass(def_type, null, null, ntc);
          types.add(tc2);
        }
        else {
          tc2 = new TypeClass(def_type, dt, tc, ntc);
        }
        types.add(tc2);
        tc = tc2;
        sel_type = (ESelect_type) def_type.getDomain(null);
      }
      else {
        break;
      }
    } // for
    TypeClass tc3 = new TypeClass(dt, null, null, ntc);
    types.add(tc3);
    // now let's get all the select types that extend the current type in the domain of schemas
    if (st instanceof EExtensible_select_type) {
      getSelectExtensionsRecursiveX(dt, types, domain, tc3);
    }
    // ok, hopefully we have here the chain of extensible types that are extended by the current type,
    // also the current type, and all the types that are extending the current type in the domain of schemas
    // other types branching out back in the chain of the extended types should not be included

    // now we can just add local selections off all these types to get all the selections
    // let's use a HashSet as well, just in case there are duplications
    Iterator types_iter = types.iterator();
    while (types_iter.hasNext()) {
      TypeClass tc4 = (TypeClass) types_iter.next();
      EDefined_type deft = tc4.current_type;
      EEntity dt_domain = deft.getDomain(null);
      if (dt_domain instanceof ESelect_type) { // of course it is!
        ANamed_type local_selections = ((ESelect_type) dt_domain).getLocal_selections(null);
        SdaiIterator it_ls = local_selections.createIterator();
        while (it_ls.next()) {
          ENamed_type nt = (ENamed_type) local_selections.getCurrentMemberObject(it_ls);
          // do we want to use this for a check or just add all?
          if (all_local_selections2.add(nt)) {
            NamedTypeClass ntc2 = new NamedTypeClass(nt, deft, tc4, ntc);
            all_local_selections.add(ntc2);
          }
        }
      }
    }
    // we could now transform the result into ANamed_type to match the direct method of select type if it were working correctly,
    // but not doing it for now
    return all_local_selections;
  }

  HashSet getAllSelections(ESelect_type st, EDefined_type dt, ASdaiModel domain) throws SdaiException {
    HashSet types = new HashSet();
    ESelect_type sel_type = st;
    EDefined_type def_type = dt;
    HashSet all_local_selections = new HashSet();

    // get all the types in the chain backwards that are extended by this type
    for (; ; ) {
      if (sel_type instanceof EExtended_select_type) {
        def_type = ((EExtended_select_type) sel_type).getIs_based_on(null);
        types.add(def_type);
        sel_type = (ESelect_type) def_type.getDomain(null);
      }
      else {
        break;
      }
    }
    // let's also add the current type as well
    types.add(dt);
    // now let's get all the select types that extend the current type in the domain of schemas
    if (st instanceof EExtensible_select_type) {
      getSelectExtensionsRecursive(dt, types, domain);
    }
    // ok, hopefully we have here the chain of extensible types that are extended by the current type,
    // also the current type, and all the types that are extending the current type in the domain of schemas
    // other types branching out back in the chain of the extended types should not be included

    // now we can just add local selections off all these types to get all the selections
    // let's use a HashSet as well, just in case there are duplications
    Iterator types_iter = types.iterator();
    while (types_iter.hasNext()) {
      EDefined_type deft = (EDefined_type) types_iter.next();
      EEntity dt_domain = deft.getDomain(null);
      if (dt_domain instanceof ESelect_type) { // of course it is!
        ANamed_type local_selections = ((ESelect_type) dt_domain).getLocal_selections(null);
        SdaiIterator it_ls = local_selections.createIterator();
        while (it_ls.next()) {
          ENamed_type nt = (ENamed_type) local_selections.getCurrentMemberObject(it_ls);
          all_local_selections.add(nt);
        }
      }
    }
    // we could now transform the result into ANamed_type to match the direct method of select type if it were working correctly,
    // but not doing it for now
    return all_local_selections;
  }

  private StringBuffer printExtensibleSelectTypesX(StringBuffer result, ESelect_type user, EDefined_type def, ASdaiModel domain) throws SdaiException {
    // get all select types: go back to the first extensible, and then get all  the types that are based on it, and on those, etc.
    HashSet types = new HashSet();
    ESelect_type sel_type = user;
    EDefined_type dt = def;

    // may add here or not, should be no difference
    types.add(def);

    // let's go back
    for (; ; ) {
      if (sel_type instanceof EExtended_select_type) {
        dt = ((EExtended_select_type) sel_type).getIs_based_on(null);
        // may add here or not, should be no difference - there is a difference for the root type at least
        types.add(dt);
        sel_type = (ESelect_type) dt.getDomain(null);
      }
      else {
        // no longer extended, but must be extensible, if was extended

        // ok, so this is our root extensible type, check if its users are already calculated, if not, do it.
        EEntity[] sortedSet2;
        int iSortedSetCount2 = 0;
        HashSet final_users2 = new HashSet();

        String key = sel_type.getPersistentLabel();
        if (hm_extensible_select_users.containsKey(key)) {
          sortedSet2 = (EEntity[]) hm_extensible_select_users.get(key);
          iSortedSetCount2 = sortedSet2.length;
        }
        else {
          if (sel_type instanceof EExtensible_select_type) {
            // ok, so now, find all the based_on types recursively, and then find their users,
            // and then sort them
            AExtended_select_type ests = new AExtended_select_type();
            SdaiIterator it_ests;
            CExtended_select_type.usedinIs_based_on(null, dt, domain, ests);
            it_ests = ests.createIterator();
            while (it_ests.next()) {
              EExtended_select_type est = (EExtended_select_type) ests.getCurrentMemberObject(it_ests);
              // est is select_type, we need the defined_type with this underlying type

              ADefined_type defs = new ADefined_type();
              SdaiIterator it_defs;
              CDefined_type.usedinDomain(null, est, domain, defs);
              it_defs = defs.createIterator();
              while (it_defs.next()) {
                EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
                types.add(def2);
                if (est instanceof EExtensible_select_type) {
                  getSelectExtensionsRecursive(def2, types, domain);
                }
              }
            } // while
            // ok, we have all the selects in HashSet types now,
            // let's calculate users for each of them, and then sort everything (and put in HashMap)

          } // extensible

          Iterator types_iter = types.iterator();
          while (types_iter.hasNext()) {
            EDefined_type deft = (EDefined_type) types_iter.next();
            AEntity users = new AEntity();
            deft.findEntityInstanceUsers(domain, users);
// RR a fix: including indirect users as well, then non-final users will be ignored

            SdaiIterator it_users = users.createIterator();
            while (it_users.next()) {
              EEntity user2 = users.getCurrentMemberEntity(it_users);
              // what about non-explicit attributes?
              if (user2 instanceof EExplicit_attribute) {
                final_users2.add(user2);
              }
              else if (user2 instanceof EAggregation_type) {

                AAttribute attrs = new AAttribute();
                SdaiIterator it_attrs;
                // what about non-explicit attributes?
                CExplicit_attribute.usedinDomain(null, user2, domain, attrs);
                it_attrs = attrs.createIterator();
                while (it_attrs.next()) {
                  EAttribute attribute = (EAttribute) attrs.getCurrentMemberObject(it_attrs);
                  final_users2.add(attribute);
                }
                // add also defined types here ? (later)
                ADefined_type defs = new ADefined_type();
                SdaiIterator it_defs;
                CDefined_type.usedinDomain(null, user2, domain, defs);
                it_defs = defs.createIterator();
                while (it_defs.next()) {
                  EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
                  final_users2.add(def2);
                }
              }
              else if (user2 instanceof ESelect_type) {
                ADefined_type defs = new ADefined_type();
                SdaiIterator it_defs;
                CDefined_type.usedinDomain(null, user2, domain, defs);
                it_defs = defs.createIterator();
                while (it_defs.next()) {
                  EDefined_type def2 = (EDefined_type) defs.getCurrentMemberObject(it_defs);
                  if (!types.contains(def2)) {
                    final_users2.add(def2);
                  }
                }
              }
            } // while - users of the type
          } // while -- through all the types
          sortedSet2 = new EEntity[final_users2.size()];
          Iterator it_final_users = final_users2.iterator();
          while (it_final_users.hasNext()) {
            sortedSet2[iSortedSetCount2] = (EEntity) it_final_users.next();
            iSortedSetCount2++;
          }
          Arrays.sort(sortedSet2, new SorterForUsers());
          hm_extensible_select_users.put(key, sortedSet2);
        }

        // probably in wrong place, we may need to accumulate together users of all select chain members together and sort
        // this is inside the loop for each separately
        for (int i = 0; i < iSortedSetCount2; i++) {
          EEntity user3 = sortedSet2[i];
          if (user3 instanceof EExplicit_attribute) {
            EAttribute attribute = (EAttribute) user3;
            EEntity_definition parent = (EEntity_definition) attribute.getParent(null);

            // of cours != defined type, besides, now everything is together and which deft it was is no longer known
            result.append(printUnclosedTab());
            result.append(printUnclosedTab());
            printHRef(result, getEntityAttrNameOp(parent, attribute), getSchemaRefDicOp(parent, schema));
            result.append("<BR>\n");
          }
          else if (user3 instanceof EAggregation_type) {
            // do nothing, resolved to explicit attributes (or defined types)
          }
          else if (user3 instanceof ESelect_type) {
            // do nothing, resolved to defined_types
          }
          else if (user3 instanceof EDefined_type) {
            EDefined_type def4 = (EDefined_type) user3;
            result.append(printUnclosedTab());
            result.append(printUnclosedTab());
            printHRef(result, getStringOp(def4.getName(null)), getSchemaRefDicOp(def4, schema));
            result.append("<BR>\n");
          }
        } // for

        break;
      }
    } // for
    return result;
  }

  private String getUserName(EEntity user) throws SdaiException {
    if (user instanceof EAttribute) {
      EAttribute attribute = (EAttribute) user;
      EEntity_definition parent = (EEntity_definition) attribute.getParent(null);
      return parent.getName(null) + "." + attribute.getName(null);
    }
    else if (user instanceof EAggregation_type) {
      // not really needed -- RR
      AAttribute attrs = new AAttribute();
      SdaiIterator it_attrs;
      CExplicit_attribute.usedinDomain(null, user, schemas, attrs);
      it_attrs = attrs.createIterator();
      if (it_attrs.next()) {
        EAttribute attribute = (EAttribute) attrs.getCurrentMemberObject(it_attrs);
        EEntity_definition parent = (EEntity_definition) attribute.getParent(null);
        return parent.getName(null) + "." + attribute.getName(null);
      }
    }
    else if (user instanceof ESelect_type) {
      // not really needed - RR
      ADefined_type defs = new ADefined_type();
      SdaiIterator it_defs;
      CDefined_type.usedinDomain(null, user, schemas, defs);
      it_defs = defs.createIterator();
      while (it_defs.next()) {
        EDefined_type def = (EDefined_type) defs.getCurrentMemberObject(it_defs);
        return def.getName(null);
      }
    }
    else if (user instanceof EDefined_type) {
      return ((EDefined_type) user).getName(null);
    }

    return "";
  }

  private static String correctSchemaName(String name) {
    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    if (name.equalsIgnoreCase("ssdai_dictionary_schema")) {
      name = "dictionary";
    }
    else if (name.equalsIgnoreCase("ssdai_mapping_schema")) {
      name = "mapping";
    }
    else if (name.equalsIgnoreCase("sdai_dictionary_schema")) {
      name = "SDAI_DICTIONARY_SCHEMA";
    }
    else if (name.equalsIgnoreCase("sdai_mapping_schema")) {
      name = "SDAI_MAPPING_SCHEMA";
    }
    return name;
  }

  private static String correctSchemaName(ESchema_definition sd) {
    return correctSchemaName(sd.getName(null));
  }

  private static String correctSchemaNameS(String name) {
    return "S" + correctSchemaName(name);
  }

  private static String correctSchemaNameS(ESchema_definition sd) {
    return correctSchemaNameS(sd.getName(null));
  }

  private static String correctSchemaNameFile(String name) {
    return baseDir + File.separator + "jsdai" + File.separator + correctSchemaNameS(name);
  }

  private static String correctSchemaNameFile(ESchema_definition sd) throws SdaiException {
    return correctSchemaNameFile(sd.getName(null));
  }

  private static StringBuffer correctSchemaName(StringBuffer result, final StrOp name) throws SdaiException {
    int start = result.length();
    name.op(result);
    int end = result.length();
    sdaiSchemaNameMatcher.reset(result.subSequence(start, end));
    if (sdaiSchemaNameMatcher.matches()) {
      result.setLength(start);
      if (sdaiSchemaNameMatcher.start(1) >= 0) {
        result.append("dictionary");
      }
      else if (sdaiSchemaNameMatcher.start(2) >= 0) {
        result.append("mapping");
      }
      else if (sdaiSchemaNameMatcher.start(3) >= 0) {
        result.append("SDAI_DICTIONARY_SCHEMA");
      }
      else if (sdaiSchemaNameMatcher.start(4) >= 0) {
        result.append("SDAI_MAPPING_SCHEMA");
      }
    }
    return result;
  }

  private static StrOp correctSchemaNameOp(final StrOp name) {
    return new StrOp() {
      public void op(StringBuffer result) throws SdaiException {
        correctSchemaName(result, name);
      }
    };
  }

  private static Matcher sdaiSchemaNameMatcher = Pattern.compile(
      "(ssdai_dictionary_schema)|" + "(ssdai_mapping_schema)|" + "(sdai_dictionary_schema)|" + "(sdai_mapping_schema)", Pattern.CASE_INSENSITIVE)
      .matcher("");

  private static StringBuffer replace(StringBuffer result, int start, int end, char from, char to) {
    for (int i = start; i < end; i++) {
      char ch = result.charAt(i);
      if (ch == from) {
        result.setCharAt(i, to);
      }
    }
    return result;
  }

  private static String getComplexName(String name) {
    return name.replace('+', '$');
  }

  private static void getComplexName(StringBuffer result, StrOp name) throws SdaiException {
    int start = result.length();
    name.op(result);
    replace(result, start, result.length(), '+', '$');
  }

  private static StrOp getComplexNameOp(final String name) {
    return new StrOp() {
      public void op(StringBuffer result) {
        int start = result.length();
        result.append(name);
        replace(result, start, result.length(), '+', '$');
      }
    };
  }

  private static StrOp getComplexNameOp(final StrOp name) {
    return new StrOp() {
      public void op(StringBuffer result) throws SdaiException {
        getComplexName(result, name);
      }
    };
  }

  private static StrOp getStringOp(final String s) {
    return new StrOp() {
      public void op(StringBuffer result) {
        result.append(s);
      }
    };
  }

  private static String printNavBar() {
    String result = "";
    result += "<A HREF=../../overview-summary.html>Overview</A>\n";
    if (flag_generate_summary) {
      result += "<A HREF=../../express-summary.html>Schemas</A>\n";
    }
    result += "<A HREF=../../Express-frame0/index.html>Index</A>\n";
    result += "<HR>\n";
    return result;
  }

  private static void printNavBar(StringBuffer result) {
    result.append("<A HREF=../../overview-summary.html>Overview</A>\n");
    if (flag_generate_summary) {
      result.append("<A HREF=../../express-summary.html>Schemas</A>\n");
    }
    result.append("<A HREF=../../Express-frame0/index.html>Index</A>\n" + "<HR>\n");
  }

  private String[] devideComplexName(String name) {
    Vector result = new Vector();
    while (name.indexOf("+") != -1) {
      result.add(name.substring(0, name.indexOf("+")));
      name = name.substring(name.indexOf("+") + 1, name.length());
    }
    result.add(name);
    return (String[]) result.toArray(new String[0]);
  }

  private static void generateSummaryFile() throws java.io.IOException {
    String model_file_name = "express-summary.html";
    if (section_title != null) {
      if (!section_title.equals("")) {
        model_file_name = section_title + ".html";
      }
    }
    FileOutputStream fos = new FileOutputStream(baseDir + File.separator + model_file_name);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    pw.println("<HTML>");
    pw.println("<HEAD>");
    pw.println("<TITLE>List of express schemas.</TITLE>");
    pw.println("</HEAD>");
    pw.println("<BODY>");

    pw.println("<H3>");
    pw.println(section_title + ":");
    pw.println("</H3>");

    // go through the vector compiled_models
    Iterator iter = include_models.iterator();
    while (iter.hasNext()) {
      String model_name = (String) iter.next();
      String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
      String package_name = correctSchemaNameS(schema_name);
      String schema_line = "<A HREF=\"jsdai/" + package_name + "/package-summary.html\" TARGET=\"classFrame\">" + schema_name + "</A><BR>";
      pw.println(schema_line);
    }

    pw.println("<P>");
    pw.println("</BODY>");
    pw.println("</HTML>");

    pw.flush();
    pw.close();
  }

  private static void includeModelsFromListInFile(String file_name) {
    try {
      FileInputStream ins = new FileInputStream(file_name);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.wordChars('.', '.');
      st.wordChars(':', ':');
      st.wordChars('/', '/');
      st.wordChars('\\', '\\');
      st.commentChar('#');

      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();
        if (st.ttype == StreamTokenizer.TT_WORD) {
          include_models.addElement(st.sval.replace('/', File.separatorChar).replace('\\', File.separatorChar));
        }
      }
    }
    catch (IOException e) {
      System.out.println("the file " + file_name + " with the models to include not found.");
      return;
    }
  }

  private static void excludeModelsFromListInFile(String file_name) {
    try {
      FileInputStream ins = new FileInputStream(file_name);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.wordChars('.', '.');
      st.wordChars(':', ':');
      st.wordChars('/', '/');
      st.wordChars('\\', '\\');
      st.commentChar('#');

      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();
        if (st.ttype == StreamTokenizer.TT_WORD) {
          exclude_models.addElement(st.sval.replace('/', File.separatorChar).replace('\\', File.separatorChar));
        }
      }
    }
    catch (IOException e) {
      System.out.println("the file " + file_name + " with the models to exclude not found.");
      return;
    }

  }

  static void emptyDirectory(String dir_str) {
    File dir = new File(dir_str);
    deleteDir(dir);
  }

  private static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }

    // The directory is now empty so delete it
    return dir.delete();
  }

  static File createOverviewFrame(String path) {
    FileOutputStream fos1 = null;
    try {
      fos1 = new FileOutputStream(path);
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    OutputStreamWriter osw1 = new OutputStreamWriter(fos1);
    PrintWriter pw1 = new PrintWriter(osw1);
    pw1.println("<HTML>");
    pw1.println("<HEAD>");
    pw1.println("<TITLE>overview frame</TITLE>");
    pw1.println("</HEAD>");
    pw1.println("<BODY BGCOLOR=\"white\">");

    pw1.println("</BODY>");
    pw1.println("</HTML>");

    pw1.flush();
    pw1.close();

    File fr = new File(path);
    return fr;

  }

  void createOverviewSummary(String path, SdaiRepository repo) throws SdaiException {
    FileOutputStream fos1 = null;
    try {
      fos1 = new FileOutputStream(path);
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    OutputStreamWriter osw1 = new OutputStreamWriter(fos1);
    PrintWriter pw1 = new PrintWriter(osw1);
    pw1.println("<HTML>");
    pw1.println("<HEAD>");
    pw1.println("<TITLE>overview frame - top schemas</TITLE>");
    pw1.println("</HEAD>");
    pw1.println("<BODY BGCOLOR=\"white\">");
    if (flag_generate_summary) {
      pw1.print("<A HREF=express-summary.html>Schemas</A>\n");
    }
    pw1.print("<A HREF=Express-frame0/index.html>Index</A>\n");
    pw1.println("<HR>\n");

    /* top level schemas: */
    String top_level_schemas = getTopLevelSchemas(repo);
    String copy_right_line = "Copyright (C) 2013 LKSoftWare GmbH";
    String generated_by = "Generated by <A HREF=http://www.jsdai.net>JSDAI</A>";

    String date_line = get_time();

    pw1.println(top_level_schemas);
    pw1.println("<HR>\n");
    pw1.println(generated_by + " on " + date_line);
    pw1.println("<HR>\n");
    pw1.println(copy_right_line);
    pw1.println("</BODY>");
    pw1.println("</HTML>");

    pw1.flush();
    pw1.close();
  }

  String get_time() {
    GregorianCalendar cal = new GregorianCalendar();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    boolean month_one_digit;

    if (month < 10) {
      month_one_digit = true;
    }
    else {
      month_one_digit = false;
    }

    int day = cal.get(Calendar.DAY_OF_MONTH);
    boolean day_one_digit;
    if (day < 10) {
      day_one_digit = true;
    }
    else {
      day_one_digit = false;
    }

    String time_stamp = year + "-";

    if (month_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + month + "-";

    if (day_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + day;

    return time_stamp;
  }

  String getTopLevelSchemas(SdaiRepository repo) throws SdaiException {
    String result = "";
    TreeSet sorted_top = new TreeSet(new SorterForSchemas());

    ASdaiModel models = repo.getModels();
    SdaiIterator iter = models.createIterator();
    HashSet interfaced_schemas = new HashSet();

    while (iter.next()) {
      SdaiModel model = models.getCurrentMember(iter);
      String name = model.getName();
      if ((name.endsWith("_DICTIONARY_DATA")) && (!name.startsWith("SDAI_"))) {
        if (model.getMode() == SdaiModel.NO_ACCESS) {
          model.startReadOnlyAccess();
        }
        ESchema_definition sd = getSchema_definitionFromModel(model);
        bottom_level = false;
        boolean is_cyclic = CyclicTest(sd, sd.getName(null), true, interfaced_schemas);
        if (is_cyclic) {
          // System.out.println("");
        }
        else {
          if (!flag_only_cyclic) {
            if (bottom_level) {
//					 		System.out.println(" - TOP");
////					 		System.out.println(" - BOTTOM"); // schema that is not interfacing from any other shemas
            }
            else {
////					 		System.out.println(" - OK");
            }
          }
        }
      }
    }

    // new cycle through the models to print the list of top-level schemas
    result += "<H4>\nTop level schemas:</H4><BR>\n";
    iter.beginning();
    int top_count = 0;
    while (iter.next()) {
      SdaiModel model = models.getCurrentMember(iter);
      String name = model.getName();
      if ((name.endsWith("_DICTIONARY_DATA")) && (!name.startsWith("SDAI_")) && (!name.equalsIgnoreCase("MAPPING_SCHEMA_DICTIONARY_DATA"))
          && (!name.equalsIgnoreCase("EXTENDED_DICTIONARY_SCHEMA_DICTIONARY_DATA"))) {
        ESchema_definition sd = getSchema_definitionFromModel(model);
        if (!interfaced_schemas.contains(sd) && !sd.getName(null).equalsIgnoreCase("mixed_complex_types")) {
          // top level schema, not interfaced by any other schemas
          // better sort here
//	      	result += "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " + sd.getName(null) + "<BR>";
          sorted_top.add(sd);
          top_count++;
        }
      }
    }

    Iterator iter_top = sorted_top.iterator();
    while (iter_top.hasNext()) {
      ESchema_definition sd1 = (ESchema_definition) iter_top.next();
      String schema_name = sd1.getName(null);
      result += printUnclosedTab()
          + printHRefandTarget(correctSchemaName(schema_name), "jsdai/" + correctSchemaNameS(schema_name) + "/package-frame.html", "packageFrame")
          + "&nbsp; " + printIsoNumber(schema_name) + "<BR>";

    }

    if (top_count == 0) {
//	   	System.out.println("\nNo top level schemas found\n");
    }
    else if (top_count == 1) {
//	   	System.out.println("\nOne top level schema found\n");
    }
    else {
//	   	System.out.println("\n" + top_count + " top level schemas found\n");
    }

    return result;
  }

  boolean CyclicTest(ESchema_definition asd, String noas, boolean first, HashSet interfaced_schemas) throws SdaiException {
    if (!first && asd.getName(null).equals(noas)) {
      // a cyclic case found
      if (flag_only_cyclic) {
//			 	System.out.println("" + noas + " - cyclic, interfaced");
      }
      else {
//				System.out.println(" - cyclic, interfaced" );
      }
      return true;
    }
    else if (noas.equals(asd.getTemp())) {
      // we already were here, skip this branch
    }
    else {
      // first time visiting
      asd.setTemp(noas);
      if (!first) {
        interfaced_schemas.add(asd);
      }
      // loop over all interfaced schemas
      AInterface_specification specifications = (AInterface_specification) asd.findEntityInstanceSdaiModel().getInstances(EInterface_specification.class);
      if (first && specifications.getMemberCount() < 1) {
        bottom_level = true;
      }
      SdaiIterator iter = specifications.createIterator();
      while (iter.next()) {
        EInterface_specification specification = (EInterface_specification) specifications.getCurrentMemberObject(iter);
        ESchema_definition sd = (ESchema_definition) specification.getForeign_schema(null);
        if (CyclicTest(sd, noas, false, interfaced_schemas)) {
          return true;
        }
      }
    }
    return false;
  }

  ESchema_definition getSchema_definitionFromModel(SdaiModel sm) throws SdaiException {
    if (sm == null) {
      return null;
    }
    Aggregate ia = sm.getEntityExtentInstances(ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    if (iter_inst.next()) {
      ESchema_definition inst = (ESchema_definition) ia.getCurrentMemberObject(iter_inst);

      return inst;
    }

    return null;
  }

  static void createAllClasses(String path) {
    FileOutputStream fos1 = null;
    try {
      fos1 = new FileOutputStream(path);
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    OutputStreamWriter osw1 = new OutputStreamWriter(fos1);
    PrintWriter pw1 = new PrintWriter(osw1);
    pw1.println("<HTML>");
    pw1.println("<HEAD>");
    pw1.println("<TITLE>All Classes</TITLE>");
    pw1.println("</HEAD>");
    pw1.println("<BODY BGCOLOR=\"white\">");

    pw1.println("</BODY>");
    pw1.println("</HTML>");

    pw1.flush();
    pw1.close();
  }

  static void createPackages(String path) {
    FileOutputStream fos1 = null;
    try {
      fos1 = new FileOutputStream(path);
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    OutputStreamWriter osw1 = new OutputStreamWriter(fos1);
    PrintWriter pw1 = new PrintWriter(osw1);
    pw1.println("<HTML>");
    pw1.println("<HEAD>");
    pw1.println("<TITLE>Packages</TITLE>");
    pw1.println("</HEAD>");
    pw1.println("<BODY BGCOLOR=\"white\">");
    pw1.println("</BODY>");
    pw1.println("</HTML>");

    pw1.flush();
    pw1.close();
  }

  /*
   *
   * The new implementation takes three first tokens separated by commas:
   * schema_name, iso_id, part_name
   *
   * It is very robust and flexible and accepts anything without breaking and extracts maximum of useful information from it.
   * However, occasionally it prints warnings, if it appears that the author of document_reference.txt did not intended what was
   * encountered.
   *
   * If the first token is not present (no schema name) - the line is skipped, but a warning is printed:
   * , iso_id, part_name
   * , iso_id
   * ,, part_name
   * etc
   *
   * If the first token is present but both iso_id and part_name are absent, then, again, such a line is skipped and a warning
   * is printed.:
   * schema_name
   * schema_name,
   * schema_name,,
   * schema_name,,,
   * etc.
   *
   * If one of iso_id or part_name is present and the other one is absent, then it is considered a correct line, no warning.
   * schema_name, iso_id
   * schema_name,, part_name
   *
   * There are three ways for the last token to end: 1) EOF, 2) EOL, 3) another comma - because comma separates tokens
   * So there may be more commas after the last useful token, or anything really after another comma.
   * That allows us to use commas for comments - see below.
   *
   * Here are the allowed characters.
   * the character range is as follows: 0-31 - control symbols, I decided not to allow them, some of them may cause unintended
   * consequences
   * 32-255 - allowed, except comma (we use it as a separator) and possibly except the comment character.
   *
   * The comment character, if defined, makes everything from it (put anythere in the line) to the end of the line a comment.
   * Therefore it is not possible to use it as a line comment character only at the begining of a line, but as a regular allowed
   * character elsewhere.
   * So we should choose for the line comment character such a character that is not needed in iso_id or part_name.
   * My previous implementation used # as the comment character. For backward compatibility perhaps we should use it as well.
   * On the other hand, if it is needed in iso_id, etc, we may choose a different comment character.
   *
   * There is another possibility - not to have a comment character at all.
   * Comments may be marked by the same separator character comma.
   *
   * You can write a comment after the 3rd comma, because after the part_name anything is accepted and ignored, and the
   * part_name ends with EOL or with a comma:
   *
   * presentation_appearance_schema, ISO 10303-46, visual_presentation, ========= IMPORTANT LINE HERE ===========
   *
   * or, if part_name is not present, still after the 3rd comma:
   *
   * presentation_appearance_schema, ISO 10303-46,, ========= IMPORTANT LINE HERE ===========
   *
   * or, if part_name is present, but iso_id is not present (not sure if it makes sense, but it is allowed):
   *
   * presentation_appearance_schema,, visual_presentation, ========= IMPORTANT LINE HERE ===========
   *
   * of course, once the 3rd comma is encountered, there may be more commas, because anything is allowed.
   * It does not matter if the whole comment is one skipped toked, or multiple skipped tokens.
   *
   * presentation_appearance_schema, ISO 10303-46,, ========= IMPORTANT == , also important, ,,,,,,,,,, even more
   * important,,,,,,,
   *
   *
   *
   * Also the whole line may be marked as a comment by using a comma:
   *
   * ,>>>>>>>>>>>>>>>>>>>> the really important stuff starts here <<<<<<<<<<<<<<<<<<
   *
   * When a comma is encountered before the schema name, then such a line is ignored and skipped, because you can do nothing
   * without a schema name.
   * However, I also print a warning, assuming that the author of document_reference.txt intended something else.
   * If we want to use a line begining with a comma as a comment line, I will not print that warning, that is all.
   *
   *
   * The use of commas for comments is a side-effect of the implementation, I just made it flexible to be able to handle without
   * breaking any garbage thrown at it.
   * And as long as the name of the schema and at least one of the two - iso_id and part_name - are present, no warnings are
   * printed.
   *
   *
   *
   *
   *
   *
   * 1) valid when both iso_id and part name are present:
   *
   * presentation_appearance_schema, ISO 10303-46, visual_presentation
   *
   * also valid, flexible handling of garbage:
   *
   * presentation_appearance_schema, ISO 10303-46, visual_presentation,
   * presentation_appearance_schema, ISO 10303-46, visual_presentation, some other garbage ignored - ignored
   * presentation_appearance_schema, ISO 10303-46, visual_presentation, some other garbage ignored - ignored, and maybe more
   * garbage etc
   * presentation_appearance_schema, ISO 10303-46, visual_presentation, , and maybe only more garbage etc
   *
   * and so on
   *
   * BTW, it allows to write end-of-line comments, because everything after the part_name is ignored until EOL, but don't forget
   * to use comma after part_name,
   * otherwise your comment will become a part of the part name.
   *
   * presentation_appearance_schema, ISO 10303-46, visual_presentation, this allows to generate imgfile.content
   * module="visual_presentation"
   * presentation_appearance_schema, ISO 10303-46, visual_presentation, ========= IMPORTANT LINE HERE ===========
   *
   *
   * 2) iso_id is present, part name is absent
   *
   * presentation_appearance_schema, ISO 10303-46
   *
   * also valid:
   *
   * presentation_appearance_schema, ISO 10303-46,
   * presentation_appearance_schema, ISO 10303-46, , >>>>>>> THIS IS A COMMENT <<<<<<<<<<<<<
   * presentation_appearance_schema, ISO 10303-46, , , , , , >>>>>>> THIS IS ALSO A COMMENT <<<<<<<<<<<<< , , , --- comment
   * continues ----
   *
   * 3) iso_id is absent, part name is present
   *
   * presentation_appearance_schema, , visual_presentation
   *
   * also valid:
   *
   * presentation_appearance_schema, , visual_presentation,
   * presentation_appearance_schema, , visual_presentation,,,,,,,, so yes, we skipped the iso_id here, see, comments may contain
   * also commas!
   *
   *
   * COMMENTS:
   *
   * We already saw that if there is a comma after part name (even if part name itself is absent) is ignored and therefore may
   * be used as comment.
   * That is the end-of-line-comment.
   *
   * A single line comment officially begins with #:
   *
   * # this is a comment line
   *
   * But because this implementation handles garbage flexibly and avoids breaking with errors, the following lines are also
   * ignored and have no effect:
   *
   * presentation_appearance_schema
   * ,presentation_appearance_schema, ISO 10303-46, visual_presentation
   *
   * The first one contains only the schema name, and therefore iso_id and part name are not specified, as if this line were not
   * present at all
   * The second one contains a comma at the very beginning, therefore the schema part is absent and it is not possible to
   * associate the following tokens with a schema,
   * so it is also ignored.
   *
   * However, both those cases may occur unintentionally when the author of the document_reference.txt thinks that everything is
   * ok, therefore, warning messages are printed.
   * Therefore don't use those cases as single line comments, use #..... instead.
   *
   * Also, if several lines contain the same schema name, only the first one is taken (or the last one?)
   * And a warning is printed, because perhaps the author intended to write the name of a different schema
   */

  boolean readIsoIdsAndPartNamesOfSchemas(String iso_file_name) {
    final int TK_START = 0;
    final int TK_SCHEMA_NAME = 1;
    final int TK_COMMA_1 = 2;
    final int TK_ISO_ID = 3;
    final int TK_COMMA_2 = 4;
    // final int TK_PART_NAME         = 5;
    // final int TK_COMMA_OTHER       = 6;
    // final int TK_WORD_OTHER        = 7;
    final int TK_SKIPPING_THE_LINE = 5;

    hm_iso_ids = new HashMap();
    hm_part_names = new HashMap();

    try {

      FileInputStream ins = new FileInputStream(iso_file_name);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);

      st.eolIsSignificant(true);
      st.slashSlashComments(true);
      st.slashStarComments(false);

      st.wordChars(32, 34);
      st.commentChar('#'); // 35
      st.wordChars(36, 43);
      st.ordinaryChar(','); // 44
      st.wordChars(45, 255);

      int status = TK_START;

      String current_schema_name = null;
      String current_schema_iso_id = null;
      String current_schema_part_name = null;
      Object previous = null;

      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();

        if (status == TK_START) {
          if (st.ttype == StreamTokenizer.TT_WORD) {
            current_schema_name = st.sval.trim().toLowerCase();
            status = TK_SCHEMA_NAME;
          }
          else if (st.ttype == StreamTokenizer.TT_EOL) {
            // an empty line - ignore, skip it.
            // do absolutely nothing, status remains the same (TK_START) for the next line
          }
          else if (st.ttype == StreamTokenizer.TT_EOF) {
            break;
          }
          else {
            // something else (probably a comma) at the begining of the line is so wrong that the line is ignored and skipped, just like a comment line #......
            // but a warning is printed
            status = TK_SKIPPING_THE_LINE;
            if (st.ttype == ',') {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a comma - IGNORED");
            }
            else if (st.ttype == StreamTokenizer.TT_NUMBER) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a number: " + st.nval + " - IGNORED");
            }
            else {
              // what can it be? impossible?
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with not sure what: " + st.toString() + "  - IGNORED");
            }

          }
        }
        else if (status == TK_SCHEMA_NAME) {
          if (st.ttype == ',') {
            status = TK_COMMA_1;
          }
          else if (st.ttype == StreamTokenizer.TT_EOL) {
            // seems that there is nothing after the schema name - ignore, skip it, but print a warning
            System.out.println("ISO_DB WARNING! line: " + (st.lineno() - 1) + " contains only the schema name - IGNORED");
            status = TK_START;
            current_schema_name = null;
            current_schema_iso_id = null; // not really needed
          }
          else if (st.ttype == StreamTokenizer.TT_EOF) {
            System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name - IGNORED");
            status = TK_START; // not really needed
            current_schema_name = null; // not really needed
            current_schema_iso_id = null; // not really needed
            break;
          }
          else {
            // it is not even possible - if schema name is not followed by comma, then what can it be?
            if (st.ttype == StreamTokenizer.TT_WORD) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.sval + " - IGNORED");
              status = TK_SKIPPING_THE_LINE;
              current_schema_name = null;
              current_schema_iso_id = null; // not really needed
            }
            else if (st.ttype == StreamTokenizer.TT_NUMBER) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.nval + " - IGNORED");
              status = TK_SKIPPING_THE_LINE;
              current_schema_name = null;
              current_schema_iso_id = null; // not really needed
            }
            else {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by not sure what: " + st.toString()
                  + " - IGNORED");
              status = TK_SKIPPING_THE_LINE;
              current_schema_iso_id = null; // not really needed
              current_schema_name = null;
            }
          }
        }
        else if (status == TK_COMMA_1) {
          if (st.ttype == StreamTokenizer.TT_WORD) {
            current_schema_iso_id = st.sval.trim();
            status = TK_ISO_ID;
          }
          else if (st.ttype == ',') {
            // the iso_id is absent, but perhaps part_name is present
            status = TK_COMMA_2;
          }
          else if (st.ttype == StreamTokenizer.TT_EOL) {
            // incomplete line, no iso_id nor part_name, print a warning
            System.out.println("ISO_DB WARNING! line: " + (st.lineno() - 1) + " contains only the schema name and a comma - IGNORED");
            status = TK_START;
            current_schema_name = null;
            current_schema_iso_id = null; // not really needed
          }
          else if (st.ttype == StreamTokenizer.TT_EOF) {
            // incomplete line, no iso_id nor part_name, print a warning
            System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name and a comma - IGNORED");
            status = TK_START; // not really needed
            current_schema_name = null; // not really needed
            current_schema_iso_id = null; // not really needed
            break;
          }
          else {
            // this should not be even possible
            if (st.ttype == StreamTokenizer.TT_NUMBER) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by " + st.nval + " - IGNORED");
              status = TK_SKIPPING_THE_LINE;
              current_schema_name = null;
              current_schema_iso_id = null; // not really needed
            }
            else {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by not sure what: "
                  + st.toString() + " - IGNORED");
              status = TK_SKIPPING_THE_LINE;
              current_schema_name = null;
              current_schema_iso_id = null; // not really needed
            }
          }
        }
        else if (status == TK_ISO_ID) {
          if (st.ttype == ',') {
            status = TK_COMMA_2;
          }
          else if (st.ttype == StreamTokenizer.TT_EOL) {
            // iso_id present, part_name absent, ok, add it
            previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
            if (previous != null) {
              System.out.println("ISO_DB WARNING! line: " + (st.lineno() - 1) + " - a duplicate line for schema: " + current_schema_name
                  + ", previous values replaced");
              previous = null;
            }
            hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
            current_schema_name = null;
            current_schema_iso_id = null;
            status = TK_START;
          }
          else if (st.ttype == StreamTokenizer.TT_EOF) {
            // iso_id present, part_name absent, ok, add it
            previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
            if (previous != null) {
              System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name
                  + ", previous values replaced");
              previous = null;
            }
            hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
            current_schema_name = null; // not really needed
            current_schema_iso_id = null; // not really needed
            status = TK_START; // not really needed
            break;
          }
          else {
            // something seriosly wrong here, should be not possible, but we can store the line assuming that part_name is absent, but print a warning
            if (st.ttype == StreamTokenizer.TT_WORD) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.sval + " - assumed part_name absent");
            }
            else if (st.ttype == StreamTokenizer.TT_NUMBER) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.nval + " - assumed part_name absent");
            }
            else {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by not sure what: " + st.toString()
                  + " - assumed part_name absent");
            }
            previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
            if (previous != null) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name
                  + ", previous values replaced");
              previous = null;
            }
            hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
            current_schema_name = null;
            current_schema_iso_id = null;
            status = TK_SKIPPING_THE_LINE;
          }
        }
        else if (status == TK_COMMA_2) {
          if (st.ttype == StreamTokenizer.TT_WORD) {
            current_schema_part_name = st.sval.trim();
            // we have now the complete info, can add now.
            if (current_schema_iso_id == null) {
              // iso_id is absent, part_name - present
              current_schema_iso_id = "";
            }
            previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
            if (previous != null) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name
                  + ", previous values replaced");
              previous = null;
            }
            hm_part_names.put(current_schema_name, current_schema_part_name); // because we are doing it in pairs, no need to check the result again
            current_schema_name = null;
            current_schema_iso_id = null;
            current_schema_part_name = null;
            // status = TK_PART_NAME;
            status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_PART_NAME as well
          }
          else if (st.ttype == ',') {
            // the part_name is absent, but perhaps the iso_id part is present
            if (current_schema_iso_id != null) {
              // add this line
              previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
              if (previous != null) {
                System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name
                    + ", previous values replaced");
                previous = null;
              }
              hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
            }
            else {
              // both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
            }
            current_schema_name = null;
            current_schema_iso_id = null;
            // status = TK_COMMA_3;
            status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_COMMA_3 as well
          }
          else if (st.ttype == StreamTokenizer.TT_EOL) {
            // the part_name is absent, but perhaps the iso_id part is present
            if (current_schema_iso_id != null) {
              // add this line
              previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
              if (previous != null) {
                System.out.println("ISO_DB WARNING! line: " + (st.lineno() - 1) + " - a duplicate line for schema: " + current_schema_name
                    + ", previous values replaced");
                previous = null;
              }
              hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
            }
            else {

              // both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
              System.out.println("ISO_DB WARNING! line: " + (st.lineno() - 1) + " - both iso_id and part_name are absent, the line is ignored");
            }
            current_schema_name = null;
            current_schema_iso_id = null;
            status = TK_START;
          }
          else if (st.ttype == StreamTokenizer.TT_EOF) {
            // the part_name is absent, but perhaps the iso_id part is present
            if (current_schema_iso_id != null) {
              // add this line
              previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
              if (previous != null) {
                System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name
                    + ", previous values replaced");
                previous = null;
              }
              hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
            }
            else {

              // both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
              System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
            }
            current_schema_name = null; // not really needed
            current_schema_iso_id = null; // not really needed
            status = TK_START; // not really needed
            break;
          }
          else {
            // this should not happen, but just in case
            // whatever happened, part_name is absent, add the line
            if (st.ttype == StreamTokenizer.TT_NUMBER) {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by " + st.nval + " - assumed part_name absent");
            }
            else {
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by not sure what: " + st.toString()
                  + " - assumed part_name absent");
            }
            if (current_schema_iso_id != null) {
              // add this line
              previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
              if (previous != null) {
                System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name
                    + ", previous values replaced");
                previous = null;
              }
              hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
            }
            else {
              // both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
              System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
            }
            current_schema_name = null;
            current_schema_iso_id = null;
            status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_COMMA_3 as well
          }
        }
        else if (status == TK_SKIPPING_THE_LINE) {
          if (st.ttype == StreamTokenizer.TT_EOL) {
            status = TK_START;
            current_schema_name = null; // should not be needed
            current_schema_iso_id = null; // should not be needed
          }
          else if (st.ttype == StreamTokenizer.TT_EOF) {
            status = TK_START; // not really needed
            current_schema_name = null; // should not be needed
            current_schema_iso_id = null; // should not be needed
            break;
          }
        }
        else {
          status = TK_START;
          current_schema_name = null;
          current_schema_iso_id = null;
          if (st.ttype == StreamTokenizer.TT_WORD) {
            System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: "
                + st.sval);
          }
          else if (st.ttype == StreamTokenizer.TT_NUMBER) {
            System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: "
                + st.nval);
          }
          else if (st.ttype == StreamTokenizer.TT_EOL) {
            System.out.println("ISO_DB WARNING! line: " + (st.lineno() - 1)
                + " - something completely wrong with this line, ignored, current token: EOL");
          }
          else if (st.ttype == StreamTokenizer.TT_EOF) {
            System.out.println("ISO_DB WARNING! (last) line: " + st.lineno()
                + " - something completely wrong with this line, ignored, current token: EOF");
            break;
          }
          else if (st.ttype == ',') {
            System.out.println("ISO_DB WARNING! line: " + st.lineno()
                + " - something completely wrong with this line, ignored, current token: comma");
          }
        }

      } // while
      return true;
    }
    catch (IOException e) {
      // no schema - no iso numbers
      //System.out.println("ISO_DB WARNING! - file not found: " + iso_file_name);
      hm_iso_ids = null;
      hm_part_names = null;
      return false;
    }
  }

  // we can eliminate SelectPath usage altogether, we only need the select_type value, nothing else - or do we?

  int calculateSelectPaths(int count, Vector current_nodes, Vector current_node_strings, int[] indeces, int depth, int depth_count, ESelect_type st,
      boolean with_type) throws SdaiException {

    if (count == 1) { // let's say that this is 1st time (count seems to be not used for anything - until now)
      select_loops = new HashSet();
    }
    count++;
    if (!select_loops.add(st)) {
      // we already were in this select type
      System.out.println("WARNING (02): select type loop detected" + st);
      // not sure what to return: 0-entity, 1-type, 2-mixed, if there is a loop, still any of these may be true
      // but a non-entity perhaps was detected in the 1st iteration through that type, so will not add a worse case that may not be there
      return 0;
    }

    boolean type_present = false;
    int is_pure = -1;
    int returned_pure = -1;
    boolean purity_type_present = false;
    boolean purity_entity_present = false;
    indeces[depth] = depth_count; //  indeces not used, it seems.
    depth++;
    depth_count = 0;

    if (st instanceof EExtensible_select_type) {
      purity_entity_present = true;
    }

    // here goes actions on the way inside if any
    EEntity an_ss;
    ANamed_type ant = getSelections(st);
    SdaiIterator iant = ant.createIterator();

    while (iant.next()) {
      for (int k = depth_count; k > 0; k--) {
        current_nodes.removeElementAt(current_nodes.size() - 1);
        current_node_strings.removeElementAt(current_node_strings.size() - 1);
      }

      depth_count = 0;
      an_ss = (ENamed_type) ant.getCurrentMemberObject(iant);

      while (an_ss instanceof EDefined_type) {
        EEntity domain = ((EDefined_type) an_ss).getDomain(null);

        if (!(domain instanceof ESelect_type)) {
          if (!type_present) {
            current_nodes.addElement(an_ss);
            current_node_strings.addElement(getEEntityPackage(an_ss));
            depth_count++;
            depth++;
            type_present = true;
            with_type = true;
          }
          else {
            // guess nothing here.
          }
        }
        an_ss = domain;
      }

      type_present = false;

      if (an_ss instanceof ESelect_type) {
        int ret_pur = calculateSelectPaths(count, current_nodes, current_node_strings, indeces, depth, depth_count, (ESelect_type) an_ss, with_type);

        if (returned_pure < 0) {
          returned_pure = ret_pur;
        }
        else if (returned_pure != ret_pur) {
          returned_pure = 2;
        }
      }
      else {
        if (an_ss instanceof EEntity_definition) {

          if (with_type) {
            purity_type_present = true;

          }
          else {
            purity_entity_present = true;
          }
        }
        else {
          purity_type_present = true;
        }

        with_type = false;

        if (an_ss instanceof EInteger_type) {
        }
        else if (an_ss instanceof ENumber_type) {
        }
        else if (an_ss instanceof EReal_type) {
        }
        else if (an_ss instanceof EString_type) {
        }
        else if (an_ss instanceof ELogical_type) {
        }
        else if (an_ss instanceof EBoolean_type) {
        }
        else if (an_ss instanceof EBinary_type) {
        }
        else if (an_ss instanceof EEnumeration_type) {
        }
        else if (an_ss instanceof EAggregation_type) {
          EEntity ass = an_ss;
          EAggregation_type aas = (EAggregation_type) an_ss;
          an_ss = aas.getElement_type(null);

          int aggregate_depth = 1;

          for (; ; ) {
            boolean done_something = false;

            if (an_ss instanceof EDefined_type) {
              ass = an_ss;
              an_ss = ((EDefined_type) an_ss).getDomain(null);
              done_something = true;
            }
            else if (an_ss instanceof EAggregation_type) {
              aggregate_depth++;
              ass = an_ss;
              an_ss = ((EAggregation_type) an_ss).getElement_type(null);
              done_something = true;
            }

            if (!done_something) {
              break;
            }
          }

          // these two lines used to store necessary depth - which aggregate classes need to be generated
          //           int the_aggr_depth = aggr_prefices.length() + 1;
          //           if (an_ss.aggr_depth < the_aggr_depth) an_ss.aggr_depth = the_aggr_depth;
          if (an_ss instanceof ESelect_type) {
            updateAggregateDepth((ENamed_type) ass, aggregate_depth);
          }
          else if (an_ss instanceof EEntity_definition) {
            // in EC1 there is the same lines as in the non-aggregate entities - adding to current_nodes, depth_count, etc - probably a bug.
            updateAggregateDepth((ENamed_type) an_ss, aggregate_depth);
          }
          else if (an_ss instanceof EEnumeration_type) {
          }
          else if (an_ss instanceof EInteger_type) {
          }
          else if (an_ss instanceof ENumber_type) {
          }
          else if (an_ss instanceof EReal_type) {
          }
          else if (an_ss instanceof EString_type) {
          }
          else if (an_ss instanceof ELogical_type) {
          }
          else if (an_ss instanceof EBoolean_type) {
          }
          else if (an_ss instanceof EBinary_type) {
          }
        }

      } // not select
    } // while - iterates through all select elements

    if (depth_count > 0) {
      for (int k = depth_count; k > 0; k--) {
        current_nodes.removeElementAt(current_nodes.size() - 1);
        current_node_strings.removeElementAt(current_node_strings.size() - 1);
      }

      depth_count = 0;
    }

    // here goe actions from the inside out if any - returning when there are no deeper selects
    // entity = 0
    // dt     = 1
    // mixed  = 2
    if (returned_pure == 2) {
      is_pure = 2;
    }
    else if (returned_pure == 1) {
      if (is_pure == 1) {
        is_pure = 1;
      }
      else if (is_pure == 2) {
        is_pure = 2;
      }
      else if (is_pure == 0) {
        is_pure = 2;
      }
      else if (is_pure == -1) {
        is_pure = 1;
      }
    }
    else if (returned_pure == 0) {
      if (is_pure == 1) {
        is_pure = 2;
      }
      else if (is_pure == 2) {
        is_pure = 2;
      }
      else if (is_pure == 0) {
        is_pure = 0;
      }
      else if (is_pure == -1) {
        is_pure = 0;
      }
    }
    else {
    }

    if (is_pure == 2) {
      return 2;
    }
    else if (is_pure == 0) {
      if (purity_type_present) {
        return 2;
      }
      else {
        return 0;
      }
    }
    else if (is_pure == 1) {
      if (purity_entity_present) {
        return 2;
      }
      else {
        return 1;
      }
    }
    else if (is_pure == -1) {
      if (purity_entity_present) {
        if (purity_type_present) {
          return 2;
        }
        else {
          return 0;
        }
      }
      else {
        if (purity_type_present) {
          return 1;
        }
        else {
          return -1;
        }
      }
    }
    else {
      return -2;
    }
  }

  void updateAggregateDepth(ENamed_type nt, int aggregate_depth) throws SdaiException {
    SdaiModel mdl = nt.findEntityInstanceSdaiModel();
    if (mdl.getMode() != SdaiModel.READ_WRITE) {
      // this is an incremental case, we will generate nothing
      // however, there might be a problem if higher depth is needed
      return;
    }

    Object temp_obj = nt.getTemp();

    Integer temp_int = (Integer) temp_obj;
    int current_depth = temp_int.intValue();

    if (current_depth < aggregate_depth) {
      nt.setTemp(new Integer(aggregate_depth));
    }
  }

  ANamed_type getSelections(ESelect_type st) throws SdaiException {
    ANamed_type l_selections = null;
    ANamed_type selections = null;
    if (st.testLocal_selections(null)) {
      l_selections = st.getLocal_selections(null);
    }
    if (st instanceof EExtended_select_type) {
      selections = addSelectionsFromExtensible((EExtended_select_type) st, l_selections);
      return selections;
    }
    return l_selections;
  }

  String getEEntityPackage(EEntity ee) throws SdaiException {
    String a_package = "";
    String a_name = "";

    if (ee instanceof ENamed_type) {
      a_name = ((ENamed_type) ee).getName(null);
      a_name = "E" + a_name.substring(0, 1).toUpperCase() + a_name.substring(1).toLowerCase();
    }
    return a_package + a_name;
  }

  class SorterForUsersX implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        AttributeClass e1 = (AttributeClass) o1;
        AttributeClass e2 = (AttributeClass) o2;
        String s1 = e1.the_parent.getName(null) + "." + e1.the_attribute.getName(null);
        String s2 = e2.the_parent.getName(null) + "." + e2.the_attribute.getName(null);
        return s1.compareToIgnoreCase(s2);
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SorterForUsers implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        EEntity e1 = (EEntity) o1;
        EEntity e2 = (EEntity) o2;
        return getUserName(e1).compareToIgnoreCase(getUserName(e2));
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SorterForModels implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        SdaiModel m1 = (SdaiModel) o1;
        SdaiModel m2 = (SdaiModel) o2;
        return findSchema(m1).getName(null).compareToIgnoreCase(findSchema(m2).getName(null));
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SorterForSchemas implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        ESchema_definition sd1 = (ESchema_definition) o1;
        ESchema_definition sd2 = (ESchema_definition) o2;
        return sd1.getName(null).compareToIgnoreCase(sd2.getName(null));
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SorterForEntities_original implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        EEntity e1 = (EEntity) o1;
        EEntity e2 = (EEntity) o2;
        return getDictionaryEntityName(e1).compareToIgnoreCase(getDictionaryEntityName(e2));
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SorterForEntities_not_sorting_schemas implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        EEntity e1 = (EEntity) o1;
        EEntity e2 = (EEntity) o2;
        int result = getDictionaryEntityName(e1).compareToIgnoreCase(getDictionaryEntityName(e2));
        if (result == 0) {
          result = -1;
        }
        return result;
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SorterForEntities implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        EEntity e1 = (EEntity) o1;
        EEntity e2 = (EEntity) o2;

        int result = getDictionaryEntityName(e1).compareToIgnoreCase(getDictionaryEntityName(e2));
        if (result == 0) {
          String e1_schemaName = findSchemaForEntity(e1).getName(null);
          String e2_schemaName = findSchemaForEntity(e2).getName(null);
          result = e1_schemaName.compareToIgnoreCase(e2_schemaName);
        }
        return result;
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SpecSorterBySchema implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        EInterface_specification e1 = (EInterface_specification) o1;
        EInterface_specification e2 = (EInterface_specification) o2;

        String e1_schemaName = e1.getForeign_schema(null).getName(null);
        String e2_schemaName = e2.getForeign_schema(null).getName(null);

        /*
         * if the same schema, try to compare names of the items, if present
         * but first we have to sort items and compare the names of the first items after sorting
         */
        String e1_name = getFirstSortedItemName(e1);

        String e2_name = getFirstSortedItemName(e2);

        int schemaNameCompare = e1_schemaName.compareToIgnoreCase(e2_schemaName);
        return schemaNameCompare != 0 ? schemaNameCompare : e1_name.compareToIgnoreCase(e2_name);
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }

    String getFirstSortedItemName(EInterface_specification specification) {
      try {

        AInterfaced_declaration items = null;

        if (specification.testItems(null)) {
          items = specification.getItems(null);
        }
        if (items != null) {
          EDeclaration[] declarations_set = new EDeclaration[items.getMemberCount()];
          SdaiIterator items_it = items.createIterator();
          int iDSetCount = 0;

          if (items.getMemberCount() > 1) {
            while (items_it.next()) {
              EDeclaration item = (EDeclaration) items.getCurrentMemberObject(items_it);
              declarations_set[iDSetCount] = item;
              iDSetCount++;
            }

            Arrays.sort(declarations_set, new SorterByDefinition());
            return getDictionaryEntityName(declarations_set[0].getDefinition(null));
          }
          else if (items.getMemberCount() == 1) {
            if (items_it.next()) {
              return getDictionaryEntityName((EDeclaration) items.getCurrentMemberObject(items_it));
            }
            else {
              return "";
            }
          }
          else {
            return "";
          }

        }
        else {
          return "";
        }

      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return "";
      }

    }
  }

  class SorterBySchema implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        EDeclaration e1 = (EDeclaration) o1;
        EDeclaration e2 = (EDeclaration) o2;

        String e1_schemaName = findSchemaForEntity(e1.getDefinition(null)).getName(null);
        String e1_name = getDictionaryEntityName(e1.getDefinition(null));

        String e2_schemaName = findSchemaForEntity(e2.getDefinition(null)).getName(null);
        String e2_name = getDictionaryEntityName(e2.getDefinition(null));

        int schemaNameCompare = e1_schemaName.compareToIgnoreCase(e2_schemaName);
        return schemaNameCompare != 0 ? schemaNameCompare : e1_name.compareToIgnoreCase(e2_name);
      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class SorterByDefinition implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        EDeclaration e1 = (EDeclaration) o1;
        EDeclaration e2 = (EDeclaration) o2;

        String e1_name = getDictionaryEntityName(e1.getDefinition(null));

        String e2_name = getDictionaryEntityName(e2.getDefinition(null));

        return e1_name.compareTo(e2_name);

      }
      catch (SdaiException exc) {
        exc.printStackTrace(System.err);
        return 0;
      }
    }
  }

  class CompareAttributes implements Comparator {
    public int compare(Object a1, Object a2) {
      Integer order1 = null, order2 = null;
      try {
        order1 = new Integer(
            ((jsdai.SExtended_dictionary_schema.EAttribute) a1).testOrder(null) ? ((jsdai.SExtended_dictionary_schema.EAttribute) a1)
                .getOrder(null) : Integer.MAX_VALUE);
        order2 = new Integer(
            ((jsdai.SExtended_dictionary_schema.EAttribute) a2).testOrder(null) ? ((jsdai.SExtended_dictionary_schema.EAttribute) a2)
                .getOrder(null) : Integer.MAX_VALUE);
      }
      catch (jsdai.lang.SdaiException ex) {
        if (order1 == null) {
          order1 = new Integer(Integer.MAX_VALUE);
        }
        if (order2 == null) {
          order2 = new Integer(Integer.MAX_VALUE);
        }
      }
      return order1.compareTo(order2);
    }

    public boolean equals(Object obj) {
      return false;
    }

    public int hashCode() {
      return super.hashCode();
    }
  }

  class SortWhereRules implements Comparator {
    public int compare(Object a1, Object a2) {
      Integer order1 = null, order2 = null;
      order1 = new Integer(((jsdai.SExtended_dictionary_schema.EWhere_rule) a1).getOrder(null));
      order2 = new Integer(((jsdai.SExtended_dictionary_schema.EWhere_rule) a2).getOrder(null));
      return order1.compareTo(order2);
    }

    public boolean equals(Object obj) {
      return false;
    }

    public int hashCode() {
      return super.hashCode();
    }
  }

}
