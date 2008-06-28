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

/*
	for additional support of incremantal compilation,
	the following features implemented:
	- running the complex generator without the list
	  in that case, the complex entities are only generated when all the leaves are interfaced into the incremmentally compiled schema
	- additional checks, so that both a new definition from a complex list and declaration induced by interfaced leaves are no longer possible
  - creating mixed_complex_types model on-demand (but not when it is just missing - to avoid complications with multiple jars)	  

*/


/*
USAGE OF MODEL SET DIRECTIVES:
[ ALL ]
[ NONE ]
[ RESTORE ]
[ - ap214_arm ]
[ + ap212_arm ]
*/
package jsdai.expressCompiler;

import java.io.*;
import java.util.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;


public class AddComplex {

	static SdaiRepository srepository;
  static boolean flag_output = false;
  static final int TK_START_LINE = 0;
  static final int TK_NAME = 1;
  static final int TK_DOLLAR = 2;
  static final int TK_DOT = 3;
  static final int TK_COLON = 4;
  static final int TK_SCHEMA = 5;
  static final int TK_PACKAGE = 6;
  static final int TK_SECTION = 7;
  static final int TK_SKIP = 8;
  static final int TK_SKIP_START = 9;
  static final int TK_SKIP_END = 10;
  static final int TK_SECTION_DONE = 11;
  static final int TK_SET_OPEN = 12;
  static final int TK_SET_CLOSE = 13;
  static final int TK_SET_WORD = 14;
  static final int TK_SET_INCLUDE = 15;
  static final int TK_SET_EXCLUDE = 16;
  static final int SKIP_NO = 0;
  static final int SKIP_YES = 1;
  static final int SKIP_START = 2;
  static final int SKIP_END = 3;
  static final int LD_LOCAL = 1;
  static final int LD_USED = 2;
  static final int LD_REFERENCED = 3;
  static final int LD_IMPLICIT = 4;
  static String section = null;

  static boolean flag_mixed_created = false;


  //  static FileOutputStream rrput;
  //  static OutputStreamWriter osw;
  //	static PrintWriter pw;
  static RandomAccessFile pw;

  //  static FileOutputStream rrput;
  //  static OutputStreamWriter osw;
  //	static PrintWriter pw;
  static RandomAccessFile pw2;
  static SdaiModel mixed_model = null;
  static Vector original_active_models = null;
  static boolean mixed_model_written_to = false;
  static Vector current_active_models = null;
  static SdaiTransaction transaction;
  static boolean flag_verbose = false;
  static boolean flag_debug = false;

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

  //  public static final void main(String args[]) throws SdaiException { // throws IOException
  boolean runMe(SdaiRepository repo, SdaiTransaction trans, String complex_file, boolean f_verbose, 
             boolean f_debug, boolean f_directory) throws SdaiException {

    flag_mixed_created = false;
    flag_verbose = f_verbose;
    flag_debug = f_debug;
    flag_output = f_directory;

		srepository = repo;
		
    if (flag_output) {
      String cx_dir_name = "COMPLEX";
      File cx_dir = new File(cx_dir_name);
      cx_dir.mkdir();
    }

    transaction = trans;


    //		String complex_file = "complex.txt";
    System.out.println("Complex generator is running");

    ASdaiModel models = repo.getModels();
    original_active_models = getActiveModels(models);

    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);
// System.out.println("current model name: " + model.getName());

      if (model.getMode() == SdaiModel.NO_ACCESS) {
        model.startReadOnlyAccess();
      }

      if (model.getName().equalsIgnoreCase("MIXED_COMPLEX_TYPES_DICTIONARY_DATA")) {
        mixed_model = model;
      }
    }
    // if complex_file == null, then only declarations to support incremental compilation are generated
    // null option should be invoked always by default when the complex generator is not invoked explicitly.
    // to switch the complex generator off altogether, a special command line switch is used -complex_off or -co
    if (complex_file != null) {
    	readComplex(models, complex_file);
		}
		createDeclarationsOfOldComplexEntitiesInNewIncrementalModels(repo);
    restoreActiveModels(original_active_models, models);
    System.out.println("Complex generator ended");
  
  	return flag_mixed_created;
  }

  static void readComplex(ASdaiModel models, String complex_file)
                   throws SdaiException {
    // String complex_file = args[0];
    String out_file = "complex";
    String part_name = null;
    String part_schema = null;
    String current_name = null;
    String schema_name = null;

    try {
      FileInputStream ins = new FileInputStream(complex_file);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.ordinaryChar('+');
      st.ordinaryChar('-');
      st.ordinaryChar('.');
      st.ordinaryChar(':');
      st.ordinaryChar('@');
      st.ordinaryChar('^');
      st.ordinaryChar('<');
      st.ordinaryChar('>');
      st.ordinaryChar('[');
      st.ordinaryChar(']');
      st.commentChar('#');

      int status = TK_START_LINE;
      int skip_status = SKIP_NO;
      TreeSet complex_entity = new TreeSet();
      Vector complex_parts = new Vector();
      Vector complex_schema = new Vector();

      // 		  rrput = new FileOutputStream("COMPLEX" + File.separator + out_file + ".exp");
      //      osw = new OutputStreamWriter(rrput);
      // 		  pw = new PrintWriter(osw);
      //			pw = new RandomAccessFile("COMPLEX" + File.separator + out_file + ".exp", "rw");
      //			pw.seek(pw.length());
      //      pw.writeBytes("SCHEMA " + out_file + ";\n");
      //    while (st.nextToken() != StreamTokenizer.TT_EOF)
      int line_nr = 1;

      for (;;) {
        //			while (st.ttype != StreamTokenizer.TT_EOF) {

// System.out.println("before - status: " + status + ", token type: " + st.ttype + ", token value: " + st.sval);
        st.nextToken();
// System.out.println("after - status: " + status + ", token type: " + st.ttype + ", token value: " + st.sval);

        if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_EOF)) {
          printDebug("END OF FILE at START OF LINE");

          break;
        } else if ((status == TK_START_LINE) && (st.ttype == '[')) {
          status = TK_SET_OPEN;
        } else if ((status == TK_SET_OPEN) && (st.ttype == '+')) {
          status = TK_SET_INCLUDE;
        } else if ((status == TK_SET_OPEN) && (st.ttype == '-')) {
          status = TK_SET_EXCLUDE;
        } else if ((status == TK_SET_OPEN) && (st.ttype == StreamTokenizer.TT_WORD)) {
          String value = st.sval;

          if (value.equalsIgnoreCase("ALL")) {
            includeAllModels(models);
          } else if (value.equalsIgnoreCase("NONE")) {
            excludeAllModels(models);
          } else if (value.equalsIgnoreCase("RESTORE")) {
            restoreActiveModels(original_active_models, models);
          } else {
            // ignore other keywords
          }

          status = TK_SET_WORD;
        } else if ((status == TK_SET_INCLUDE) && (st.ttype == StreamTokenizer.TT_WORD)) {
          includeModel(st.sval, models);
          status = TK_SET_WORD;
        } else if ((status == TK_SET_EXCLUDE) && (st.ttype == StreamTokenizer.TT_WORD)) {
          excludeModel(st.sval, models);
          status = TK_SET_WORD;
        } else if ((status == TK_SET_WORD) && (st.ttype == ']')) {
          status = TK_SET_CLOSE;
        } else if ((status == TK_START_LINE) && (st.ttype == '@')) {
          status = TK_SECTION;
          section = null;

          if (skip_status == SKIP_YES) {
            skip_status = SKIP_NO;
          }
        } else if ((status == TK_START_LINE) && (st.ttype == '^')) {
          status = TK_SKIP;

          if (skip_status == SKIP_NO) {
            skip_status = SKIP_YES;
          }
        } else if ((status == TK_START_LINE) && (st.ttype == '<')) {
          status = TK_SKIP;

          if (skip_status == SKIP_NO) {
            skip_status = SKIP_START;
          }
        } else if ((status == TK_START_LINE) && (st.ttype == '>')) {
          status = TK_SKIP;

          if (skip_status == SKIP_START) {
            skip_status = SKIP_NO;
          }
        } else if ((status == TK_SECTION) && (st.ttype == StreamTokenizer.TT_WORD)) {
          section = st.sval;
          status = TK_SECTION_DONE;

//        } else if ((((status == TK_SET_CLOSE) || (status == TK_SECTION) || 
//                           (status == TK_SECTION_DONE) || (status == TK_SKIP)) && 
//                         (st.ttype == StreamTokenizer.TT_EOL)) || 
//                       (st.ttype == StreamTokenizer.TT_EOF)) {

        } else if (((status == TK_SET_CLOSE) || (status == TK_SECTION) || 
                           (status == TK_SECTION_DONE) || (status == TK_SKIP)) && 
                         (st.ttype == StreamTokenizer.TT_EOL)) {
//          if (st.ttype == StreamTokenizer.TT_EOF) {
//            printDebug("END OF FILE at END OF LINE: " + status);
//            status = TK_START_LINE;

//            break;
//          } else if (st.ttype == StreamTokenizer.TT_EOL) {
            status = TK_START_LINE;
            complex_entity = new TreeSet();
            complex_parts = new Vector();
            complex_schema = new Vector();
            part_name = null;
            part_schema = null;
            current_name = null;
            schema_name = null;
 //         }
        } else if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_name = st.sval;
          part_schema = null;
          schema_name = null;
          status = TK_NAME;
        } else if ((status == TK_DOLLAR) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_name = st.sval;
          part_schema = null;
          status = TK_NAME;
        } else if ((status == TK_NAME) && (st.ttype == ':')) {
          schema_name = current_name;
          status = TK_COLON;
        } else if ((status == TK_NAME) && (st.ttype == '.')) {
          part_schema = current_name;
          status = TK_DOT;
        } else if ((status == TK_COLON) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_name = st.sval;
          status = TK_NAME;
        } else if ((status == TK_DOT) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_name = st.sval;
          status = TK_NAME;
        } else if ((status == TK_NAME) && (st.ttype == '+')) {
          part_name = current_name;
          status = TK_DOLLAR;
          complex_parts.addElement(part_name.toUpperCase());
          complex_entity.add(part_name.toUpperCase());
          complex_schema.add(part_schema);
        } else if (((status == TK_NAME) && (st.ttype == StreamTokenizer.TT_EOL)) || (st.ttype == StreamTokenizer.TT_EOF)) {
//        } else if ((status == TK_NAME) && ((st.ttype == StreamTokenizer.TT_EOL) || (st.ttype == StreamTokenizer.TT_EOF))) {
          // current complex entity reading completed. Now, generate it.
          part_name = current_name;


          //          status = TK_DOLLAR;

          /*
          if (complex_parts == null) {
            System.out.println("complex_parts = null");
          }
          if (part_name == null) {
            System.out.println("part_name = null");
          } else {
            System.out.println("part_name = " + part_name);
          }
          */
          complex_parts.addElement(part_name.toUpperCase());
          complex_entity.add(part_name.toUpperCase());
          complex_schema.add(part_schema);

          if (skip_status == SKIP_NO) {
            GenerateComplexEntity(complex_entity, complex_parts, complex_schema, schema_name, 
                                  models);
          }

          //					list("-- complex entity --", complex_entity);
          if (st.ttype == StreamTokenizer.TT_EOF) {
            printDebug("END OF FILE at END OF LINE: status");
            status = TK_START_LINE;

            break;
          } else if (st.ttype == StreamTokenizer.TT_EOL) {
            // ok, next complex entity
            status = TK_START_LINE;
            complex_entity = new TreeSet();
            complex_parts = new Vector();
            complex_schema = new Vector();
            part_name = null;
            part_schema = null;
            current_name = null;
            schema_name = null;
          }
        } else if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_EOL)) {
        } else {
          System.out.println("Express Compiler> Complex list: ERROR in input file, line: " + 
                             st.lineno());

          break;
        }
      }

      //			pw.writeBytes("\n");
      //			pw.writeBytes("END_SCHEMA;\n");
      //		  pw.flush();
      //  		pw.close();
    } // try

    //  catch (FileNotFoundException e) 
    catch (IOException e) {
      System.out.println("Complex file " + complex_file + " not found.");

      return;
    }
  }

  static void readComplex_old(ASdaiModel models, String complex_file)
                       throws SdaiException {
    // String complex_file = args[0];
    String out_file = "complex";
    String part_name;

    try {
      FileInputStream ins = new FileInputStream(complex_file);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.ordinaryChar('$');
      st.commentChar('#');

      int status = TK_START_LINE;
      TreeSet complex_entity = new TreeSet();

      // 		  rrput = new FileOutputStream("COMPLEX" + File.separator + out_file + ".exp");
      //      osw = new OutputStreamWriter(rrput);
      // 		  pw = new PrintWriter(osw);
      //			pw = new RandomAccessFile("COMPLEX" + File.separator + out_file + ".exp", "rw");
      //			pw.seek(pw.length());
      //      pw.writeBytes("SCHEMA " + out_file + ";\n");
      //    while (st.nextToken() != StreamTokenizer.TT_EOF)
      int line_nr = 1;

      //      for(;;) {
      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();

        if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_WORD)) {
          part_name = st.sval;
          complex_entity.add(part_name.toUpperCase());
          status = TK_NAME;
        } else if ((status == TK_NAME) && (st.ttype == '$')) {
          status = TK_DOLLAR;
        } else if ((status == TK_DOLLAR) && (st.ttype == StreamTokenizer.TT_WORD)) {
          part_name = st.sval;
          complex_entity.add(part_name.toUpperCase());
          status = TK_NAME;
        } else if (((status == TK_NAME) && (st.ttype == StreamTokenizer.TT_EOL)) || 
                       (st.ttype == StreamTokenizer.TT_EOF)) {
          // current complex entity reading completed. Now, generate it.
          //					GenerateComplexEntity(complex_entity, models);
          //					list("-- complex entity --", complex_entity);
          if (st.ttype == StreamTokenizer.TT_EOF) {
            status = TK_START_LINE;

            break;
          } else if (st.ttype == StreamTokenizer.TT_EOL) {
            // ok, next complex entity
            status = TK_START_LINE;
            complex_entity = new TreeSet();
          }
        } else if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_EOL)) {
        } else {
          System.out.println("Express Compiler> Short names: ERROR in input file, line: " + 
                             st.lineno());

          break;
        }
      }

      //			pw.writeBytes("\n");
      //			pw.writeBytes("END_SCHEMA;\n");
      //		  pw.flush();
      //  		pw.close();
    } // try

    //  catch (FileNotFoundException e) 
    catch (IOException e) {
      System.out.println("Complex file " + complex_file + " not found.");

      return;
    }
  }

  static void list(String header, TreeSet sorted) {
    if (sorted.size() < 1) {
      return;
    }


    //		System.out.println("list set ------- " + header);
    //		System.out.println("--- " + header + " ---");
    System.out.println(header);

    Iterator iter = sorted.iterator();

    while (iter.hasNext()) {
      System.out.println("- " + (String) iter.next());
    }

    System.out.println(".");
  }

  static void GenerateComplexEntity(TreeSet leaves, Vector also_leaves, Vector schemas, 
                                    String owner_schema_name, ASdaiModel models)
                             throws SdaiException, IOException {
    if (leaves.size() < 1) {
      return;
    }

    Iterator iter = leaves.iterator();
    HashSet leaves_ed = new HashSet();
    boolean all_found = true;

    while (iter.hasNext()) {
      String leaf = (String) iter.next();
      EEntity_definition ed = findEntity_definitionInAllModels(leaf, also_leaves, schemas, models);

      if (ed == null) {
        list("WARNING! Entity definition not found: " + leaf + 
             ", this complex entity will not be created:", leaves);
        all_found = false;
      } else {
        leaves_ed.add(ed);

        // System.out.println("Entity definition found: " + leaf + " - " + ed.getName(null));
      }
    }

    if (!all_found) {
      return;
    }


    // what if complex entity also contains some supertypes, not only the leaves? Let's check and remove them.
    PruneSupertypes(leaves_ed, leaves, also_leaves, schemas);

    if (leaves_ed.size() < 2) {
      list("WARNING! This complex entity has less than 2 leaves and will not be created:", leaves);

      return;
    }

    if (!noAbstractLeaves(leaves_ed)) {
      list("WARNING! There is at least one ABSTRACT leaf	, the entity will not be created:", leaves);

      return;
    }

    /* // Instead of too restrictive requirement for all the leaves to have a common root supertype, they have to be in the same sub-supertype graph.
            
          // testing if all the leaves have a common root supertype, otherwise, such a complex may be currently illegal
          boolean has_root = findCommonRoot(leaves_ed);
          if (!has_root) {
            list("WARNING! The leaves of this complex entity do not have the same root supertype, the entity will not be created:", leaves);
            return;
          }
    */
    boolean the_same_graph = areLeavesInTheSameSubSuperGraph(leaves_ed, models);

    if (!the_same_graph) {
      list("WARNING! Not all the leaves of this complex entity are in the same subtype-supertype graph, the entity will not be created:", 
           leaves);

      return;
    }

    SdaiModel complex_model = getComplexModel(leaves_ed, owner_schema_name, models, leaves);

    if (complex_model == null) {
      // do not generate for wrong schema.
      return;
    }

    String model_name = complex_model.getName();
    String schema_name = model_name.substring(0, model_name.length() - 16).toLowerCase();
    String schema_file = "C$$" + schema_name.toUpperCase();

    if (flag_output) {
      pw = new RandomAccessFile("COMPLEX" + File.separator + schema_file + ".EXP", "rw");

      if (pw.length() == 0) {
        pw.writeBytes("SCHEMA " + schema_file + ";\n");
        pw.writeBytes("END_SCHEMA;\n");
      }

      pw.seek(pw.length() - 12);
      pw.writeBytes("\n");

      pw.writeBytes("\n");
      pw.writeBytes("\t(*\n");
      pw.writeBytes("\t  " + schema_name + "\n");
      pw.writeBytes("\t Available in: \n");
    }

    HashSet parts_ed = getAllParts(leaves_ed);
    EEntity_definition ed = generateEntityDefinition(complex_model, parts_ed, leaves, leaves_ed);

    if (ed == null) {
    } else {
      generateEntityDeclarations(ed, models, parts_ed, leaves_ed);

      if (flag_output) {
        pw.writeBytes("\t*)\n");
      }

      generateExpressEntity(pw, parts_ed, leaves_ed);

      //		  pw.flush();
    }

    if (flag_output) {
      pw.close();
    }
  }

  // two changes: #1 and #2 to make supertypes attribute to hold only leaves - new approach.
  // also change - name of complex entity now has "+" instead of "$"
  static EEntity_definition generateEntityDefinition(SdaiModel model, HashSet parts, 
                                                     TreeSet leave_names, HashSet leaves)
                                              throws SdaiException {
    // create entity definition in model, don't forget set complex flag, supertypes maybe better in alphabetic order, LIST afterall.
    // attributes:
    // name : express_id; - from leave_names in that order, connected by $ (actually should be +, but I'm not sure if Gintaras supports).
    // where_rules : LIST [0:?] OF where_rule; - no, but not optional, so empty LIST
    // short_name : OPTIONAL STRING; -- not for complex entities, I don't think they exist?
    // supertypes : LIST [0:?] OF UNIQUE entity_definition; from HashSet parts, but maybe better sort alphabeticly.
    // complex : BOOLEAN; - true
    // instantiable : BOOLEAN; - true, not abstract
    // independent : BOOLEAN; - strictly speaking, if there are implicitly, or REFERNCE FROM interfaced leaves, then false, otherwise true.
    // - but check. Anyway, we so far ignore this attribute
    // explicit_attributes : LIST [0:?] OF explicit_attribute; - no explicit attributes, empty list (not optional attribute)

    ESchema_definition sd = getSchema_definitionFromModel(model);
    EEntity_declaration edc;

    // it would be possible to create local declaration here too, instead of there		
    String entity_name = null;
    String entity_class_name = null;
    Iterator iter = leave_names.iterator();

    while (iter.hasNext()) {
      String leaf = (String) iter.next();

      if (entity_name == null) {
        entity_name = leaf;
      } else {
        entity_name += ("+" + leaf); // later will need "+" instead of "$"
        entity_class_name += ("$" + leaf); // will not need it, I think
      }
    }
		
		

    EEntity_definition end = findEntity_definitionInModel(entity_name, model);

    if (end != null) {
      //       String model0_name = model.getName();
      //       String schema0_name = model0_name.substring(0, model0_name.length() - 16).toLowerCase();
      //       System.out.println("WARNING: complex entity " + entity_name + " is already in " + schema0_name);
      //       return null;
      // Existing complex entity definitions are reused now.
      // I guess it's okay when we have incremental compilation. V.N.
      System.out.println("WARNING: complex entity " + entity_name.toLowerCase() + " already exists in the model of " + getSchema_definitionFromModel(model).getName(null) + " schema, not created");
      return end;
    }

		if (model.getMode() != jsdai.lang.SdaiModel.READ_WRITE) {
			// System.out.println("Attempt to create a complex entity in models of previously compiled schemas, the complex entity created in the mixed complex types model:");
			System.out.println(entity_name);
			// return null;
			createMixedModel();
			model = mixed_model;
	    end = findEntity_definitionInModel(entity_name, model);
			if (end != null) {
	      System.out.println("WARNING: complex entity " + entity_name.toLowerCase() + " already exists in the model of " + getSchema_definitionFromModel(model).getName(null) + " schema, not created");
				return end;
			}
		}

		if (model == mixed_model) {
  		mixed_model_written_to = true;
			
		}
    EEntity_definition ed = (EEntity_definition) model.createEntityInstance(
                                  CEntity_definition.class);
    edc = (EEntity_declaration) model.createEntityInstance(
                CEntity_declaration$local_declaration.class);
    edc.setParent(null, sd);
		// attribute parent_schema now is derived instead of explicit
		// if (sd instanceof ESchema_definition) {
	    // edc.setParent_schema(null, sd);
  	// }
    edc.setDefinition(null, (ENamed_type) ed);
    ed.setName(null, entity_name.toLowerCase());
    printDebug("complex entity: " + entity_name.toLowerCase());
    ed.setInstantiable(null, true); // true if not ABSTRACT
		ed.setAbstract_entity(null, false);
		ed.setConnotational_subtype(null, false);


    boolean independent;

    if (model == mixed_model) {
      independent = false;
    } else {
      independent = true;
    }

    ed.setIndependent(null, independent);
    ed.setComplex(null, true);

    // - removed, because where_rules now is inverse instead of direct.
    // ed.createWhere_rules(null);
    // ed.createExplicit_attributes(null);
    //pre-X		AEntity_definition supertypes = ed.createSupertypes(null); 
    AEntity_or_view_definition generic_supertypes = ed.createGeneric_supertypes(null);
    AEntity_definition supertypes = null;
   	// attribute supertypes now is derived instead of explicit
    // if (ed instanceof jsdai.SExtended_dictionary_schema.EEntity_definition) {
	    // supertypes  = ((jsdai.SExtended_dictionary_schema.EEntity_definition)ed).createSupertypes(null);		
		// }
    //#1		HashSet	tmp_parts	=	new	HashSet(parts);
    HashSet tmp_parts = new HashSet(leaves); // if supertypes contain leaves only - new style
    TreeSet sorted_parts = new TreeSet();
    Iterator outer = tmp_parts.iterator();

    while (outer.hasNext()) {
      String part0 = ((EEntity_definition) outer.next()).getName(null);
      sorted_parts.add(part0.toUpperCase());
    }

    outer = sorted_parts.iterator();

    int index = 1; // because LIST
    int index_g = 1;

    while (outer.hasNext()) {
      String supertype_name = (String) outer.next();

      //#2			Iterator inner = parts.iterator();
      Iterator inner = leaves.iterator(); // if supertypes contain leaves only - new style

      while (inner.hasNext()) {
        EEntity_definition supertype = (EEntity_definition) inner.next();
        String sup_name = supertype.getName(null);

        if (supertype_name.equalsIgnoreCase(sup_name)) {
          //					System.out.println("supertype: " + sup_name);
          generic_supertypes.addByIndex(index_g++, supertype);
          if (supertypes != null) {
          	supertypes.addByIndex(index++, supertype);
					}
          if (model == mixed_model) {
            // check if declaration for this supertype is in mixed_complex_types model, and if not, generate it
            Aggregate ia = model.getEntityExtentInstances(EEntity_declaration.class);
            SdaiIterator iter_inst = ia.createIterator();
            boolean declaration_found = false;

            while (iter_inst.next()) {
              EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
              EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);

              if (inst == supertype) {
                declaration_found = true;

                break;
              }
            }

            if (!declaration_found) {
              edc = (EEntity_declaration) model.createEntityInstance(
                          CEntity_declaration$used_declaration.class);
              edc.setParent(null, sd);
							// parent_schema now is derived instead of explicit
							// if (sd instanceof ESchema_definition) {
	              // edc.setParent_schema(null, sd);
  						// }
              edc.setDefinition(null, (ENamed_type) supertype);
            }
          }

          break;
        }
      }
    }

    return ed;
  }

  static void generateEntityDeclarations(EEntity_definition ed, ASdaiModel models, HashSet parts_ed, 
                                         HashSet leaves)
                                  throws SdaiException, IOException {
    // go through all models in a loop.
    // for each model, check if all leaves are in that model, if yes - create declaraiton.
    // inside model, go in a loop through leaves. For each leaf run a loop of all declarations, check if that leaf has a declaration.
    // if at least one leaf does not have declaration, no need to continue, complex declaration will not be created in that model, 
    // move to the next model
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);

      if (model.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }


      boolean complex_exists = false;
      Aggregate iaxx = model.getEntityExtentInstances(EEntity_declaration.class);
      SdaiIterator iter_instxx = iaxx.createIterator();
        //boolean leaf_found_xx = false;

//System.out.println("----------------------------------------------------");
//System.out.println("model: " + model);
//System.out.println("complex declaration: " + ed);
        while (iter_instxx.next()) {
          EDeclaration decxx = (EDeclaration) iaxx.getCurrentMemberObject(iter_instxx);
          EEntity_definition instxx = (EEntity_definition) decxx.getDefinition(null);
//System.out.println("current declaration: " + instxx);
					if (instxx == ed) {
						complex_exists = true;
//System.out.println("complex FOUND");
						break;
					}

			}
			// skip this model, it already has a declaration for this complex entity
			// and there is no possibility of promoting it to a stronger one, so no need to do anything
			if (complex_exists) {
//System.out.println("skipping the model: " + model);
				continue;
			}

      Iterator iter_leaf = leaves.iterator();
      boolean all_leaves = true;
      int lowest_denominator = LD_LOCAL;

      while (iter_leaf.hasNext()) {
        // already established, that a declaration of the complex entity itself already exists in this model
//        if (complex_exists) break;
        EEntity_definition leaf = (EEntity_definition) iter_leaf.next();

        // here go through all declarations, get attribute definition and compare to leaf
        Aggregate ia = model.getEntityExtentInstances(EEntity_declaration.class);
        SdaiIterator iter_inst = ia.createIterator();
        boolean leaf_found = false;

//System.out.println("----------------------------------------------------");
//System.out.println("complex declaration: " + ed);
        while (iter_inst.next()) {
          EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
          EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);


//System.out.println("current declaration: " + inst);

//					if (inst == ed) {
						// the complex entity itself already has a declaration in this model
						// it is not possible that this declaration could be promoted from referenced to used, etc., so no need to do anything further
						// it is a quick fix, perhaps it could be done in a more optimal way, not checking here in the inner loop multiple times
//						complex_exists = true;
//						break;
//					}

          if (inst == leaf) {
            leaf_found = true;

            // check the type of the leaf declaration, especially if interfaced - used, referenced, implicit.
            // will need to know what kind of declaration to generate.
            // what is the algoritm - if at least one implicit then implicit, or the opposite - at least one used, then used? Check.
            if (dec instanceof ELocal_declaration) {
            } else if (dec instanceof EUsed_declaration) {
              if (lowest_denominator == LD_LOCAL) {
                lowest_denominator = LD_USED;
              }
            } else if (dec instanceof EReferenced_declaration) {
              if (lowest_denominator != LD_IMPLICIT) {
                lowest_denominator = LD_REFERENCED;
              }
            } else if (dec instanceof EImplicit_declaration) {
              lowest_denominator = LD_IMPLICIT;
            }

            break;
          }
        }

        if (!leaf_found) {
          all_leaves = false;

          break;
        }
      }

      if (all_leaves) {
//      if (all_leaves && (!complex_exists)) {
        // create declaration here. There is one question - what kind of declaration.
        EEntity_declaration edc;
        ESchema_definition sd = getSchema_definitionFromModel(model);

        if (flag_output) {
          pw.writeBytes("\t\t  " + sd.getName(null).toLowerCase() + "\n");
        }

        if (model == ed.findEntityInstanceSdaiModel()) {
          // create local declaration
          // already created together with definition
          //					edc = (EEntity_declaration)model.createEntityInstance(CEntity_declaration$local_declaration.class);
          //					edc.setParent_schema(null, sd);
          //					edc.setDefinition(null, (ENamed_type)ed);
        } else {
          String model_name = model.getName();
          String schema_name = "C$$" + 
                               model_name.substring(0, model_name.length() - 16).toUpperCase();

          if (flag_output) {
            pw2 = new RandomAccessFile("COMPLEX" + File.separator + schema_name + ".EXP", "rw");

            if (pw2.length() == 0) {
              pw2.writeBytes("SCHEMA " + schema_name + ";\n");
              pw2.writeBytes("END_SCHEMA;\n");
            }

            pw2.seek(pw2.length() - 12);
            pw2.writeBytes("\n");
          }

          generateExpressEntity(pw2, parts_ed, leaves);

          if (flag_output) {
            pw2.close();
          }

          // create interfaced declaration - but what - used, referenced or implicit?
          switch (lowest_denominator) {
          case LD_LOCAL:

            // why second time???
            // already created together with definition
            //							edc = (EEntity_declaration)model.createEntityInstance(CEntity_declaration$local_declaration.class);
            //							edc.setParent_schema(null, sd);
            //							edc.setDefinition(null, (ENamed_type)ed);
            break;

          case LD_USED:
            edc = (EEntity_declaration) model.createEntityInstance(
                        CEntity_declaration$used_declaration.class);
       	    edc.setParent(null, sd);
						// parent_schema now is derived instead of explicit
						// if (sd instanceof ESchema_definition) {
        	    // edc.setParent_schema(null, sd);
          	// }
            edc.setDefinition(null, (ENamed_type) ed);

            break;

          case LD_REFERENCED:

            // for new lang, do not use interfaces of complex entities. Entity declaration, and even declaration is enough.
            //							EEntity_declaration$referenced_declaration edc = (EEntity_declaration$referenced_declaration)model.createEntityInstance(CEntity_declaration$referenced_declaration.class);
            //							edc = (EEntity_declaration$referenced_declaration)model.createEntityInstance(CEntity_declaration$referenced_declaration.class);
            edc = (EEntity_declaration) model.createEntityInstance(
                        CEntity_declaration$referenced_declaration.class);
            edc.setParent(null, sd);
						// parent_schema now is derived instead of explicit
						// if (sd instanceof ESchema_definition) {
	            // edc.setParent_schema(null, sd);
  					// }
            edc.setDefinition(null, (ENamed_type) ed);

            break;

          case LD_IMPLICIT:
            edc = (EEntity_declaration) model.createEntityInstance(
                        CEntity_declaration$implicit_declaration.class);
            edc.setParent(null, sd);
						// parent_schema now is derived instead of explicit
						// if (sd instanceof ESchema_definition) {
	            // edc.setParent_schema(null, sd);
  					// }
            edc.setDefinition(null, (ENamed_type) ed);

            break;

          default:
            System.out.println("Internal error while creating declarations");
          }
        }
      }
    }
  }

  static HashSet getAllParts(HashSet leaves) throws SdaiException {
    HashSet result = new HashSet();

    // recursion is needed, get supertypes, and supertypes for each supertype, etc. repeated inheritance is solved by using HashSet.
    Iterator iter_leaves = leaves.iterator();

    while (iter_leaves.hasNext()) {
      EEntity_definition leaf = (EEntity_definition) iter_leaves.next();


      // recursion goes here
      getLeafSupertypes(leaf, result);
      result.add(leaf);
    }

    return result;
  }

  static boolean noAbstractLeaves(HashSet leaves) throws SdaiException {
    Iterator iter_leaves = leaves.iterator();

    while (iter_leaves.hasNext()) {
      EEntity_definition leaf = (EEntity_definition) iter_leaves.next();

      if (!leaf.getInstantiable(null)) {
        return false;
      }
    }

    return true;
  }

  static void getLeafSupertypes(EEntity_definition leaf, HashSet result)
                         throws SdaiException {
    // pre-X		AEntity_definition supertypes = leaf.getSupertypes(null);
//    AEntity_or_view_definition supertypes = leaf.getSupertypes(null);
    AEntity_or_view_definition supertypes = leaf.getGeneric_supertypes(null);
    SdaiIterator iterSuper = supertypes.createIterator();

    while (iterSuper.next()) {
      EEntity_definition superEntity = (EEntity_definition) supertypes.getCurrentMember(iterSuper);
      getLeafSupertypes(superEntity, result);
      result.add(superEntity);
    }
  }

  static boolean findCommonRoot(HashSet leaves_ed) throws SdaiException {
    // have to check if all the leaves are in the same graph, even more,  if they have a common root supertype
    HashSet roots = null;
    Iterator iter_leaves = leaves_ed.iterator();

    while (iter_leaves.hasNext()) {
      EEntity_definition leaf = (EEntity_definition) iter_leaves.next();

      // recursion goes here
      HashSet result = new HashSet();
      getLeafSupertypes(leaf, result);

      if (roots == null) {
        roots = new HashSet(result);
      } else {
        roots.retainAll(result);
      }
    }

    if (roots.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  static void PruneSupertypes(HashSet leaves_ed, TreeSet leaves, Vector also_leaves, Vector schemas)
                       throws SdaiException {
    HashSet result = new HashSet();
    Iterator iter_leaves = leaves_ed.iterator();

    while (iter_leaves.hasNext()) {
      EEntity_definition leaf = (EEntity_definition) iter_leaves.next();


      // recursion goes here
      getLeafSupertypes(leaf, result);
    }

    // supertypes of all leaves are here now
    Iterator iter_super = result.iterator();

    while (iter_super.hasNext()) {
      EEntity_definition supertype = (EEntity_definition) iter_super.next();
      boolean had_supertype = leaves_ed.remove(supertype);

      if (had_supertype) {
        // print a warning, remove also from leaves, also_leaves and schemas
        String removed_name = supertype.getName(null).toUpperCase();
        list("WARNING: entity " + removed_name.toLowerCase() + 
             " is not a leaf, but a supertype, and was removed from this complex entity:", leaves);

        boolean name_removed = leaves.remove(removed_name);

        if (!name_removed) {
          System.out.println("INTERNAL ERROR 1 while pruning supertypes: " + removed_name);
        }

        int index = also_leaves.indexOf(removed_name);

        if (index == -1) {
          System.out.println("INTERNAL ERROR 2 while pruning supertypes: " + removed_name);
        }

        also_leaves.remove(index);
        schemas.remove(index);
      }
    }
  }

  static void generateExpressEntity(RandomAccessFile pw1, HashSet parts, HashSet leaves)
                             throws SdaiException, IOException {
    // the name of the complex entity consists of leaves, the supertype list - parts. Everything in alphabetic order
    TreeSet sorted = new TreeSet();
    HashSet tmp_parts = new HashSet(parts);
    TreeSet sorted_parts = new TreeSet();
    Iterator inner = tmp_parts.iterator();

    while (inner.hasNext()) {
      String part0 = ((EEntity_definition) inner.next()).getName(null);
      sorted_parts.add(part0.toUpperCase());
    }

    HashSet tmp_leaves = new HashSet(leaves);
    TreeSet sorted_leaves = new TreeSet();
    Iterator inner2 = tmp_leaves.iterator();

    while (inner2.hasNext()) {
      String part2 = ((EEntity_definition) inner2.next()).getName(null);
      sorted_leaves.add(part2.toUpperCase());
    }

    inner = sorted_leaves.iterator();

    String entity_name = null;

    while (inner.hasNext()) {
      String leaf = (String) inner.next();

      if (entity_name == null) {
        entity_name = leaf;
      } else {
        entity_name += ("$" + leaf);
      }
    }

    if (flag_output) {
      pw1.writeBytes("\tENTITY " + entity_name.toLowerCase() + "\n");
      pw1.writeBytes("\t\tSUBTYPE OF (\n");
    }


    // here go supertypes in the loop
    inner = sorted_parts.iterator();

    boolean first_time = true;

    while (inner.hasNext()) {
      String supertype = (String) inner.next();

      if (first_time) {
        if (flag_output) {
          pw1.writeBytes("\t\t\t" + supertype.toLowerCase()); // print
        }

        first_time = false;
      } else {
        if (flag_output) {
          pw1.writeBytes(",\n");
          pw1.writeBytes("\t\t\t" + supertype.toLowerCase()); // print
        }
      }
    }

    if (flag_output) {
      pw1.writeBytes("\n");
      pw1.writeBytes("\t\t);\n");
      pw1.writeBytes("\tEND_ENTITY;\n");
      pw1.writeBytes("\n");
      pw1.writeBytes("END_SCHEMA;\n");
    }
  }

  static boolean checkLeaves(SdaiModel model, HashSet leaves)
                      throws SdaiException {
    Iterator inner = leaves.iterator();

    while (inner.hasNext()) {
      EEntity_definition entity = (EEntity_definition) inner.next();

      // check if this leave entity has declaration in this model
      Aggregate ia = model.getEntityExtentInstances(EEntity_declaration.class);
      SdaiIterator iter_inst = ia.createIterator();
      boolean leave_found = false;

      while (iter_inst.next()) {
        EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
        EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);

        if (entity == inst) {
          // ok, it has declaration
          leave_found = true;

          break;
        }
      }

      if (!leave_found) {
        return false;
      }
    }

    return true;
  }

  static SdaiModel getComplexModel(HashSet leaves, String owner_schema_name, ASdaiModel models, 
                                   TreeSet leaves_str)
                            throws SdaiException {
    SdaiModel model = null;

    if (owner_schema_name != null) {
      model = getModelFromSchemaName(owner_schema_name, models);

      if (model == null) {
        System.out.println("Wrong specified complex entity schema name, model not found: " + 
                           owner_schema_name);

        return null;
      } else if (model.getMode() != SdaiModel.READ_WRITE) {
        System.out.println("The target complex entity schema is currently not accessible: " + 
                           owner_schema_name);

        return null;
      } else {
        // better check here if all leaves have declarations in that model.
        boolean rez = checkLeaves(model, leaves);

        if (rez) {
          return model;
        } else {
          list("WARNING! Wrong schema for this complex entity, not all its leaves have declarations here: " + 
               owner_schema_name, leaves_str);

          return null;
        }
      }
    }

    if (section != null) {
      model = getModelFromSchemaName(section, models);

      if (model == null) {
        System.out.println("Wrong section complex entity schema name, model not found: " + 
                           section);

        return null;
      } else if (model.getMode() != SdaiModel.READ_WRITE) {
        System.out.println(
              "The section schema model is currently not accessible to create complex entities: " + 
              section);

        return null;
      } else {
        // better check here if all leaves have declarations in that model.
        boolean rez = checkLeaves(model, leaves);

        if (rez) {
          return model;
        } else {
          list("WARNING! Wrong schema for this complex entity, not all its leaves have declarations here: " + 
               section, leaves_str);

          return null;
        }
      }
    }

    Iterator inner = leaves.iterator();

    while (inner.hasNext()) {
      EEntity_definition entity = (EEntity_definition) inner.next();
      SdaiModel current = entity.findEntityInstanceSdaiModel();

      if (current == null) {
        // System.out.println("current = NULL, entity: " + entity.getName(null));
      }

      if (model == null) {
        model = current;
      } else {
        if (model == current) {
          // the same, candidate for non-mixed schema
        } else {
          createMixedModel();
          // mixed_complex_types - get its model.
          // System.out.println("mixed model");
          // mixed_model is made read write to 
          // ensure incremetal compilation works reliably V.N.
          if (mixed_model.getMode() == SdaiModel.READ_ONLY) {
            mixed_model.promoteSdaiModelToRW();
          } else if (mixed_model.getMode() == SdaiModel.NO_ACCESS) {
            mixed_model.startReadWriteAccess();
          }

          return mixed_model;
        }
      }
    }

    // System.out.println("just model");
    return model;
  }

  static SdaiModel getModelFromSchemaName(String owner_schema_name, ASdaiModel models)
                                   throws SdaiException {
    String owner_model_name = owner_schema_name.toUpperCase() + "_DICTIONARY_DATA";
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);
      String model_name = model.getName();

      if (model_name.equalsIgnoreCase(owner_model_name)) {
        return model;
      }
    }

    return null;
  }

  static EEntity_definition findEntity_definitionInAllModels(String name, Vector leaves, 
                                                             Vector schemas, ASdaiModel models)
                                                      throws SdaiException {
    SdaiIterator iter_model = models.createIterator();
    int number_of_found = 0;
    TreeSet list_of_found = new TreeSet();
    EEntity_definition result = null;
    boolean schema_specified = false;
    String schema_name = null;

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);

      if (model.getMode() != SdaiModel.READ_WRITE) {
        continue;
      }

      EEntity_definition ed = findEntity_definitionInModel(name, model);

      if (ed != null) {
        if (ed != result) {
          number_of_found++;

          if (number_of_found > 1) {
            // here, try to use the optional schema name.
            // check if the name is present, if not - print "ambigous leaf name warning".
            // if the optional schema name is present, take the one that is in that schema and break, if no one - make another loop.
            schema_name = getOptionalSchemaName(name, leaves, schemas);

            if (schema_name == null) {
              schema_name = section;
            }

            if (schema_name == null) {
              //								System.out.println("WARNING! Ambiguous leaf name: " + name + ", model 1: " + result.findEntityInstanceSdaiModel().getName() + ", model 2: " + ed.findEntityInstanceSdaiModel().getName());
              String model_name1 = result.findEntityInstanceSdaiModel().getName();
              String schema_name1 = model_name1.substring(0, model_name1.length() - 16)
                                               .toLowerCase();
              String model_name2 = ed.findEntityInstanceSdaiModel().getName();
              String schema_name2 = model_name2.substring(0, model_name2.length() - 16)
                                               .toLowerCase();

              list_of_found.add(schema_name1);
              list_of_found.add(schema_name2);
            } else {
              schema_specified = true;
              printVerbose("Specified schema " + schema_name + 
                           " was assumed to resolve this ambiguous leaf entity: " + name);

              //	System.out.println("WARNING! SPECIFIED SCHEMA: : " + schema_name + ", entity: " + name + ", model 1: " + result.findEntityInstanceSdaiModel().getName() + ", model 2: " + ed.findEntityInstanceSdaiModel().getName());
              // get Sdai Model name for that schema name. 
              String model_name = schema_name.toUpperCase() + "_DICTIONARY_DATA";

              if (model_name.equalsIgnoreCase(result.findEntityInstanceSdaiModel().getName())) {
                return result;
              } else if (model_name.equalsIgnoreCase(ed.findEntityInstanceSdaiModel().getName())) {
                return ed;
              } else {
                // next loop, let's check other models.
              }
            }
          }

          result = ed;
        }
      }
    }

    if (number_of_found > 1) {
      if (schema_specified) {
      } else {
        list("WARNING! Ambiguous leaf name: " + name.toLowerCase() + " found in schemas:", 
             list_of_found);
      }
    }

    return result;
  }

  static String getOptionalSchemaName(String name, Vector leaves, Vector schemas) {
    // System.out.println("INSIDE getOptionalSchemaName, schema name: " + name);
    // find the leave with the name "name", get its index, and then get schema with the same index
    for (int i = 0; i < leaves.size(); i++) {
      String leaf = (String) leaves.elementAt(i);

      // System.out.println("INSIDE getOptionalSchemaName, leaf name: " + leaf);
      if (leaf.equalsIgnoreCase(name)) {
        String result = (String) schemas.elementAt(i);

        // System.out.println("INSIDE getOptionalSchemaName, result name: " + result);
        return result;
      }
    }

    return null;
  }

  static EEntity_definition findEntity_definitionInModel(String name, SdaiModel model)
                                                  throws SdaiException {
    Aggregate ia = model.getEntityExtentInstances(EEntity_declaration.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
      EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);
      String instance_name = inst.getName(null);

      if (instance_name.equalsIgnoreCase(name)) { // found! return it

        return inst;
      } else if (dec instanceof EInterfaced_declaration) {
        if (((EInterfaced_declaration) dec).testAlias_name(null)) {
          instance_name = ((EInterfaced_declaration) dec).getAlias_name(null);

          if (instance_name.equalsIgnoreCase(name)) { // found! return it

            return (EEntity_definition) dec.getDefinition(null);
          }
        }
      }
    }

    return null;
  }

  static ESchema_definition getSchema_definitionFromModel(SdaiModel sm)
                                                   throws SdaiException {
    Aggregate ia = sm.getEntityExtentInstances(ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      ESchema_definition inst = (ESchema_definition) ia.getCurrentMemberObject(iter_inst);

      return inst;
    }

    return null;
  }

  static void includeAllModels(ASdaiModel models) throws SdaiException {
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);
      setReadWriteAccess(model);
    }
  }

  static void excludeAllModels(ASdaiModel models) throws SdaiException {
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);
      setReadOnlyAccess(model);
    }
  }

  static void includeModel(String schema_name, ASdaiModel models)
                    throws SdaiException {
    SdaiModel my_model = getModelFromSchemaName(schema_name, models);
    setReadWriteAccess(my_model);
  }

  static void excludeModel(String schema_name, ASdaiModel models)
                    throws SdaiException {
    SdaiModel my_model = getModelFromSchemaName(schema_name, models);
    setReadOnlyAccess(my_model);
  }

  static Vector getActiveModels(ASdaiModel models) throws SdaiException {
    Vector active_models = new Vector();
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);

      if (model.getMode() == SdaiModel.READ_WRITE) {
        active_models.addElement(model);
      }
    }

    return active_models;
  }

  static void restoreActiveModels(Vector active_models, ASdaiModel models)
                           throws SdaiException {
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);


      if (active_models.contains(model)) {
        setReadWriteAccess(model);
      } else {
				if ((model == mixed_model) && (mixed_model_written_to)) {
	        setReadWriteAccess(model);
				} else {
        	setReadOnlyAccess(model);
      	}
      }
    }
  }

  static void setReadWriteAccess(SdaiModel model) throws SdaiException {
    if (model.getMode() == SdaiModel.READ_ONLY) {
      model.promoteSdaiModelToRW();
    } else if (model.getMode() == SdaiModel.NO_ACCESS) {
      model.startReadWriteAccess();
    } else if (model.getMode() == SdaiModel.READ_WRITE) {
    }
  }

  static void setReadOnlyAccess(SdaiModel model) throws SdaiException {
    if (model.getMode() == SdaiModel.READ_WRITE) {
      transaction.commit();
      model.reduceSdaiModelToRO();
    } else if (model.getMode() == SdaiModel.NO_ACCESS) {
      model.startReadOnlyAccess();
    } else if (model.getMode() == SdaiModel.READ_ONLY) {
    }
  }

  /***************** checking that all the leaves are in the same subtype-supertype graph  ********************/
  static boolean areLeavesInTheSameSubSuperGraph(HashSet leaves, ASdaiModel models)
                                          throws SdaiException {
    // 1. get all entities - leaves and their supertypes together in the pool
    //		HashSet initial_entity_pool = getAllParts(leaves);
    HashSet entityPool = getAllParts(leaves);

    /*
        // this is not the complete entity pool yet, all subtypes of all these types are also needed, if not yet included.      
        Iterator iter_pool = initial_entity_pool.iterator();
        ASdaiModel domain = getActiveJsdaiModels(models);
        HashSet entityPool = new HashSet(initial_entity_pool);
        while  (iter_pool.hasNext())  {
          EEntity_definition pool_entity  =  (EEntity_definition)iter_pool.next();  
          AEntity_definition subtypes = new AEntity_definition();
          findSubtypes(entityPool, pool_entity, domain, subtypes);
        }  
    */

    // 2. Group the entity pool into sub-sets, one for each sub-supertype graph
    // It is not necessary here to store all the subsets simultaneously, it is enough to count them.
    // If the count is > 1, then all the leaves are not in the same sub-super graph, 
    // because it is not possible that all the supertypes are in the same graph but their subtypes are not. 
    int number_of_graphs = 0;

    while (!entityPool.isEmpty()) {
      // take the first entity in the pool and start recursive traversing.
      EEntity_definition entity = (EEntity_definition) entityPool.iterator().next();
      HashSet entityGraph = new HashSet();
      traverse(entity, entityGraph, entityPool, null);
      number_of_graphs++;
    }

    if (number_of_graphs > 1) {
      return false;
    } else {
      return true;
    }
  }

  /*  
  This is a recursive method that finds subtypes for entity ed and returns them in subtypes
  These subtypes are also added to the resulting enityPool.
  Then, for each subtype this method is again invoked recursively and new subtype aggregate is returned (non-acumulative), etc.
      
  */
  static void findSubtypes(HashSet entityPool, EEntity_definition ed, ASdaiModel domain, 
                           AEntity_definition subtypes)
                    throws SdaiException {
    // attribute supertypes now is derived instead of explicit
    // CEntity_definition.usedinSupertypes(null, ed, domain, subtypes);
    CEntity_definition.usedinGeneric_supertypes(null, ed, domain, subtypes);

    SdaiIterator iterSub = subtypes.createIterator();

    while (iterSub.next()) {
      EEntity_definition subEntity = (EEntity_definition) subtypes.getCurrentMember(iterSub);
      entityPool.add(subEntity);

      AEntity_definition subtypes2 = new AEntity_definition();
      findSubtypes(entityPool, subEntity, domain, subtypes2);
    }
  }

  static ASdaiModel getActiveJsdaiModels(ASdaiModel models)
                                  throws SdaiException {
    ASdaiModel active_models = new ASdaiModel();
    SdaiIterator iter_model = models.createIterator();
    int i = 1;

    while (iter_model.next()) {
      SdaiModel model = models.getCurrentMember(iter_model);

      if (model.getMode() == SdaiModel.READ_WRITE) {
        //				active_models.addUnordered(model);
        active_models.addByIndex(i++, model, null);
      }
    }

    return active_models;
  }

  static void traverse(EEntity_definition entity, HashSet entityGraph, HashSet entityPool, 
                       ASchemaInstance domain) throws SdaiException {
    if (entityPool.contains(entity)) {
      // move entity from entityPool to entityGraph
      //			System.out.println("Entering traverse, moving from Pool to Graph: " + entity.getName(null));
      entityPool.remove(entity);
      entityGraph.add(entity);

      // going up in the sub/supertype graph
      // pre-X			AEntity_definition supertypes = entity.getSupertypes(null);
//      AEntity_or_view_definition supertypes = entity.getSupertypes(null);
      AEntity_or_view_definition supertypes = entity.getGeneric_supertypes(null);
      SdaiIterator iterSuper = supertypes.createIterator();

      while (iterSuper.next()) {
        EEntity_definition superEntity = (EEntity_definition) supertypes.getCurrentMember(iterSuper);
        traverse(superEntity, entityGraph, entityPool, domain);
      }

      // going down in the sub/supertype graph
      AEntity_definition subtypes = new AEntity_definition();


      //			CEntity_definition.usedinSupertypes(null, entity, domain, subtypes);
      useThis(entity, entityPool, subtypes);

      // System.out.println("Going down. Number of subtypes of entity: " + entity.getName(null) + " is " + subtypes.getMemberCount());
      SdaiIterator iterSub = subtypes.createIterator();

      while (iterSub.next()) {
        EEntity_definition subEntity = (EEntity_definition) subtypes.getCurrentMember(iterSub);
        traverse(subEntity, entityGraph, entityPool, domain);
      }
    }
  }

  static void useThis(EEntity_definition entity, HashSet entityPool, AEntity_definition subtypes)
               throws SdaiException {
    Iterator iter = entityPool.iterator();
    int i = 1;

    while (iter.hasNext()) {
      EEntity_definition pool_entity = (EEntity_definition) iter.next();

      // pre-X			AEntity_definition supertypes = pool_entity.getSupertypes(null);
//      AEntity_or_view_definition supertypes = pool_entity.getSupertypes(null);
      AEntity_or_view_definition supertypes = pool_entity.getGeneric_supertypes(null);
      SdaiIterator iterSuper = supertypes.createIterator();

      while (iterSuper.next()) {
        EEntity_definition superEntity = (EEntity_definition) supertypes.getCurrentMember(iterSuper);

        if (superEntity == entity) {
          subtypes.addByIndex(i++, pool_entity);
        }
      }
    }
  }


	static void createDeclarationsOfOldComplexEntitiesInNewIncrementalModels(SdaiRepository repo) throws SdaiException {
		// go through all entity definitions in all old models + in mixed_complex_types model (may be modified or not)
		// and create declarations in new incremental models if all leaves have declarations there, 
		// and the complex declaration is not yet present
	
		Vector new_models = getNewModels();
		Vector old_models = getOldModels(repo);
		Iterator old_iterator = old_models.iterator();
// System.out.println("number of old models: " + old_models.size());		
		while (old_iterator.hasNext()) {
// System.out.println("inside the loop");		
			SdaiModel old_model = (SdaiModel)old_iterator.next();
			AEntity_definition aedf = (AEntity_definition)old_model.getEntityExtentInstances(EEntity_definition.class);
// System.out.println("old model: " + old_model);
//System.out.println("aedf member count: " + aedf.getMemberCount());
			SdaiIterator iter_aedf = aedf.createIterator();
			while (iter_aedf.next()) {
				EEntity_definition edf = aedf.getCurrentMember(iter_aedf);
				if (edf.getComplex(null)) {
					HashSet leaves = getLeaves(edf);
						
					Iterator new_iterator = new_models.iterator();
					while (new_iterator.hasNext()) {
						SdaiModel new_model = (SdaiModel)new_iterator.next();
				
            // just in case, check if this declaration already exists in this model (should not happen)
						boolean declaration_exists = false;
       			Aggregate aedc = new_model.getEntityExtentInstances(EEntity_declaration.class);
       			SdaiIterator iter_aedc = aedc.createIterator();
       			while (iter_aedc.next()) {
         			EDeclaration a_dec = (EDeclaration) aedc.getCurrentMemberObject(iter_aedc);
        			EEntity_definition a_edf = (EEntity_definition) a_dec.getDefinition(null);
							if (a_edf == edf) {
								declaration_exists = true;
								break;
							}
							// also check if the name of the entity is the same, even if the definition is different
							// do not allow such cases as well
							if (a_edf.getName(null).equalsIgnoreCase(edf.getName(null))) {
								//System.out.println("Complex Genetaror: );
								declaration_exists = true;
								break;
							}
						}
						if (declaration_exists) {
							continue;
						}

	     			Iterator iter_leaf = leaves.iterator();
      			boolean all_leaves = true;
      			int lowest_denominator = LD_LOCAL;

      			while (iter_leaf.hasNext()) {
        			EEntity_definition leaf = (EEntity_definition) iter_leaf.next();

        			// here go through all declarations, get attribute definition and compare to leaf
        			Aggregate ia = new_model.getEntityExtentInstances(EEntity_declaration.class);
        			SdaiIterator iter_inst = ia.createIterator();
        			boolean leaf_found = false;

        			while (iter_inst.next()) {
          			EDeclaration dec = (EDeclaration) ia.getCurrentMemberObject(iter_inst);
          			EEntity_definition inst = (EEntity_definition) dec.getDefinition(null);

          			if (inst == leaf) {
            			leaf_found = true;

            			// check the type of the leaf declaration, especially if interfaced - used, referenced, implicit.
            			// will need to know what kind of declaration to generate.
            			// what is the algoritm - if at least one implicit then implicit, or the opposite - at least one used, then used? Check.
            			if (dec instanceof ELocal_declaration) {
            			} else if (dec instanceof EUsed_declaration) {
              			if (lowest_denominator == LD_LOCAL) {
                			lowest_denominator = LD_USED;
              			}
            			} else 
            			if (dec instanceof EReferenced_declaration) {
              			if (lowest_denominator != LD_IMPLICIT) {
                			lowest_denominator = LD_REFERENCED;
              			}
            			} else if (dec instanceof EImplicit_declaration) {
              			lowest_denominator = LD_IMPLICIT;
            			}

            			break;
          			}
        			} // go through all the declarations is the model

        			if (!leaf_found) {
          			all_leaves = false;
								break;
        			}
      			} // go through all the leaves

      			if (all_leaves) {
        			// create declaration here. There is one question - what kind of declaration.
        			EEntity_declaration edc;
        			ESchema_definition sd = getSchema_definitionFromModel(new_model);


        			// this is not a possible case
        			if (new_model == edf.findEntityInstanceSdaiModel()) {
          			// create local declaration
          			// already created together with definition
          			//					edc = (EEntity_declaration)model.createEntityInstance(CEntity_declaration$local_declaration.class);
          			//					edc.setParent_schema(null, sd);
          			//					edc.setDefinition(null, (ENamed_type)ed);
        			} else {

          			// create interfaced declaration - but what - used, referenced or implicit?
          			switch (lowest_denominator) {
          				case LD_LOCAL:

			            	// double impossible
			           		// already created together with definition
            				//							edc = (EEntity_declaration)model.createEntityInstance(CEntity_declaration$local_declaration.class);
            				//							edc.setParent_schema(null, sd);
            				//							edc.setDefinition(null, (ENamed_type)ed);
            				break;
          				case LD_USED:
            				edc = (EEntity_declaration) new_model.createEntityInstance(CEntity_declaration$used_declaration.class);
       	    				edc.setParent(null, sd);
										// parent_schema is now derived instead of explicit
										// if (sd instanceof ESchema_definition) {
        	    				// edc.setParent_schema(null, sd);
          					// }
            				edc.setDefinition(null, (ENamed_type) edf);
            				break;

          				case LD_REFERENCED:
			            	// for new lang, do not use interfaces of complex entities. Entity declaration, and even declaration is enough.
            				//							EEntity_declaration$referenced_declaration edc = (EEntity_declaration$referenced_declaration)model.createEntityInstance(CEntity_declaration$referenced_declaration.class);
            				//							edc = (EEntity_declaration$referenced_declaration)model.createEntityInstance(CEntity_declaration$referenced_declaration.class);
            				edc = (EEntity_declaration) new_model.createEntityInstance(CEntity_declaration$referenced_declaration.class);
            				edc.setParent(null, sd);
										// parent_schema is now derived instead of explicit
										// if (sd instanceof ESchema_definition) {
	            				// edc.setParent_schema(null, sd);
  									// }
            				edc.setDefinition(null, (ENamed_type) edf);
				            break;

          				case LD_IMPLICIT:
            				edc = (EEntity_declaration) new_model.createEntityInstance(CEntity_declaration$implicit_declaration.class);
            				edc.setParent(null, sd);
										// parent_schema is now derived instead of explicit
										// if (sd instanceof ESchema_definition) {
	            				// edc.setParent_schema(null, sd);
  									// }
            				edc.setDefinition(null, (ENamed_type) edf);
				            break;

          				default:
            				System.out.println("Internal error while creating declarations");
          			} // switch
        			} // not local - local impossible anyway
      			} // if all leaves have declarations in new_model
    			} // go through all new models
				} // if complex entity
			} // go through all entity definitions in old model
		} // go through all old models
	} // method ends

	static Vector getNewModels() throws SdaiException {
		Vector new_models = new Vector();
		
		Iterator iter = original_active_models.iterator();
		
		while (iter.hasNext()) {
			SdaiModel a_model = (SdaiModel)iter.next();
			if (a_model != mixed_model) {
				new_models.addElement(a_model);
			}
		}

		return new_models;
	}


	static Vector getOldModels(SdaiRepository repo) throws SdaiException {
		Vector old_models = new Vector();
    ASdaiModel the_models = repo.getModels();
    SdaiIterator iter_model = the_models.createIterator();
		boolean mixed_found = false;

    while (iter_model.next()) {
      SdaiModel a_model = the_models.getCurrentMember(iter_model);

      if (a_model.getMode() != SdaiModel.READ_WRITE) {
	      if (a_model.getMode() == SdaiModel.NO_ACCESS) {
  	      a_model.startReadOnlyAccess();
    	  }
        old_models.addElement(a_model);
      	if (a_model == mixed_model) {
      		mixed_found = true;
      	}
      }
    }
		if (!mixed_found) {
			if (mixed_model != null) {
				old_models.addElement(mixed_model);
			}
		}

		return old_models;
	}

	// probably currently it is save to assume that all the direct supertypes of the complex entity are the leaves we need
	// if not, change the implementation
	static HashSet getLeaves(EEntity_definition edf) throws SdaiException {
		HashSet leaves = new HashSet();

    AEntity_or_view_definition supertypes = edf.getGeneric_supertypes(null);
    SdaiIterator iter_super = supertypes.createIterator();

    while (iter_super.next()) {
			EEntity_definition super_entity = (EEntity_definition) supertypes.getCurrentMember(iter_super);
			leaves.add(super_entity);
		}
		return leaves;
	}

	static void createMixedModel() throws SdaiException {
		// create the model in the case that it is absent (i.e., it is needed, but the express schema was not provided explicitly)
		if (mixed_model != null) return;
		String mixed_model_name = "MIXED_COMPLEX_TYPES_DICTIONARY_DATA";
		String schema_name = "MIXED_COMPLEX_TYPES";
		mixed_model = srepository.createSdaiModel(mixed_model_name, jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		mixed_model.startReadWriteAccess();
		ESchema_definition sd = (jsdai.SExtended_dictionary_schema.ESchema_definition)mixed_model.createEntityInstance(jsdai.SExtended_dictionary_schema.CSchema_definition.class);
		sd.setName(null, schema_name.toUpperCase());
		flag_mixed_created = true;
	}

}

