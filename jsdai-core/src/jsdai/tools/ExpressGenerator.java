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

/**

USAGE:
			java jsdai.tools.ExpressGenerator [switches]

Command line switches:

-help
-?
	prints this text

-out path|file
-output path|file
	A directory path or a file name may be specified where to put 
	generated express file(s)
	If it is a file, it may also include a path, 
	but has to end with .exp
	A file may be specified only when long form is generated 
	into a single file
	Default location is the current directory, default name for long form file: 
		lf.exp
	Default name for long form when -inlude_arm switch is used: 
		arm_lf.exp
	Default name for long form when -exclude_arm switch is used:
		aim_lf.exp
	Default name for long form when -top_schema/-top swich is used: 
		schema_name.exp
	Deflaut name for long form when -schema switch is used:
		schema_name.exp
	Deflaut name for long form when -schema_instance switch is used:
		schema_name.exp
	
-location path|name
	The location of the repository with dictionary models - 
	the data for the generator.
	If not specified, the repository location is taken from jsdai.properties

-long
-long_form
	Tells the generator to generate long form 
	instead of default short form
	
-include_arm
	Includes only schemas with names ending with "_arm", 
	other schemas are ignored

-exclude_arm
	Includes all the schemas except those whose names end with "_arm"				

-include_list file_path/name
	Includes all the schemas that are listed in the specified file, 
	names of schemas or names of models, one-per-line.
	Other schemas are not included
	lines starting with # are commented out.

-exclude_list file_path|name
	Includes all the schemas, except those that are listed 
	in the specified file, names of schemas or names of models, one-per-line
	The list of included schemas may be further narrowed down 
	by other command line switches, such as -include_list, -top, etc.
	Lines starting with # are commented out.

-top_schema schema_name
-top schema_name
	Includes the specified schema together with all the schemas 
	that are interfaced into the specified schema or into schemas 
	that are interfaced into the specified schema, directly or indirectly.
	Other schemas are excluded by default, but additional schemas 
	may be included with other switches such as -include_list
	The specified schema does not have to be really a top schema, 
	any schema may be specified and an appropriate subset is taken.
	Higher up schemas are not included unless there is a cyclic dependency.

-schema schema_name
	Generates long form for the specified schema, including everything that the schema 
	interfaces explicitly or implicitly, directly or indirectly,
	but it is not merging of several schemas together, stuff from other schemas 
	may be included complete or incomplete.
	-long switch is implied, may be ommited
	-skip switch does nothing because this approach does not allow any conflicts
	Other switches specifying schemas have no (accumulative) effect with this switch.
	The name of the long form schema and of the file are derived from the name 
	of the specified schema unless overrideng by -out switch.

-schema_instance schema_name
	Includes all the schemas which models are associated models 
	of the schema instance of the specified schema.
	It may be the same subset of schemas as with -top switch 
	or a different subset, at least with current schema instances.
	This switch may produce wrong results when generating express, 
	use it with caution or don't use it at all.

-skip_conflicts
-skip
	This switch has effect only when -long or -long_form switch is used.
	If there are name conflicts when merging schemas into long form, 
	the default behaviour is to generate express 
	for all the subsequent occurences of the same name, 
	when this switch is used, express is generated for the first occurence only.
	The generator informs about conflicts by warning messages in both cases.

-print_schemas
-print
	Prints the list of schemas included with switches 
	-top/-top_schema or -schema_instance/-schema 
	into the subset for generating express

-format_expressions
-format
  Attempts to format the java output for express expressions such as in functions, rules or derived attribute expressions.
  Default is not to format - asuming that the author of the original express already has it formatted


*/

package jsdai.tools;

import java.util.*;
import java.io.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.util.*;

public class ExpressGenerator {

	static String express_path = null;
	static String express_long_file = "lf.exp";
	static String express_long_schema = "lf_schema";
	static String top_schema = null;
	static String specified_schema = null;
	static boolean flag_print_schemas = false;
	static int flag_printed_schema = 0;
	static boolean flag_do_not_allign_LOCAL = false;
	static boolean flag_help = false;
	static boolean flag_long_form = false;
	static boolean flag_include_arm = false;
	static boolean flag_exclude_arm = false; 
	static boolean flag_skip_conflicts = false;
	static boolean flag_format_expressions = false;
	static boolean flag_end_comment = false;
	static final String DICT_MOD_SUFIX = "_DICTIONARY_DATA";

	static HashMap hm_iso_numbers = null;
	static HashMap hm_iso_ids = null;
	static HashMap hm_part_names = null;

	static String iso_file = "iso_db";
	SdaiModel docModel = null;
	ASdaiModel schemas = new ASdaiModel();
	HashMap hm_all_names = new HashMap();
	static boolean flag_schema_specific_long = false;

	ASdaiModel asmExpressDomain = new ASdaiModel(); //--VV--
	Vector vAlgorithmDefinition = new Vector();

//  replacing Vector by Hashset so there is no trouble with duplications with multiple lists on command line, etc.
//	static Vector include_models = new Vector();
//	static Vector exclude_models = new Vector();
	static HashSet include_models = new HashSet();
	static HashSet exclude_models = new HashSet();
	static String current_schema = null;


	public static final void main(String args[]) throws SdaiException, java.io.IOException {


		String help_string = "USAGE:\n\tjava jsdai.tools.ExpressGenerator [switches]\n\nCommand line switches:\n\n-help\n-?\n\tprints this text\n\n-out path|file\n-output path|file\n\tA directory path or a file name may be specified where to put\n\tgenerated express file(s)\n\tIf it is a file, it may also include a path,\n\tbut has to end with .exp\n\tA file may be specified only when long form is generated\n\tinto a single file\n\tDefault location is the current directory, default name for long form file:\n\t\tlf.exp\n\tDefault name for long form when -inlude_arm switch is used:\n\t\tarm_lf.exp\n\tDefault name for long form when -exclude_arm switch is used:\n\t\taim_lf.exp\n\tDefault name for long form when -top_schema/-top swich is used:\n\t\tschema_name.exp\n\tDeflaut name for long form when -schema switch is used:\n\t\tschema_name.exp\n\tDeflaut name for long form when -schema_instance switch is used:\n\t\tschema_name.exp\n\n-repository_location\n-location path|name\n\tThe location of the repository with dictionary models -\n\tthe data for the generator.\n\tIf not specified, the repository location is taken from jsdai.properties.\n\n-long_form\n-long\n\tTells the generator to generate long form\n\tinstead of default short form\n\n-include_arm\n\tIncludes only schemas with names ending with \"_arm\",\n\tother schemas are ignored\n\n-exclude_arm\n\tIncludes all the schemas except those whose names end with \"_arm\"\n\n-include_list file_path|name\n\tIncludes all the schemas that are listed in the specified file,\n\tnames of schemas or names of models, one-per-line.\n\tOther schemas are not included\n\tlines starting with # are commented out.\n\n-exclude_list file_path|name\n\tIncludes all the schemas, except those that are listed\n\tin the specified file, names of schemas or names of models, one-per-line\n\tThe list of included schemas may be further narrowed down\n\tby other command line switches, such as -include_list, -top, etc.\n\tLines starting with # are commented out.\n\n-top_schema schema_name\n-top schema_name\n\tIncludes the specified schema together with all the schemas\n\tthat are interfaced into the specified schema or into schemas\n\tthat are interfaced into the specified schema, directly or indirectly.\n\tOther schemas are excluded by default, but additional schemas\n\tmay be included with other switches such as -include_list\n\tThe specified schema does not have to be really a top schema,\n\tany schema may be specified and an appropriate subset is taken.\n\tHigher up schemas are not included unless there is a cyclic dependency.\n\n-schema schema_name\n\tGenerates long form for the specified schema, including everything that the schema\n\tinterfaces explicitly or implicitly, directly or indirectly,\n\tbut it is not merging of several schemas together, stuff from other schemas\n\tmay be included complete or incomplete.\n\t-long switch is implied, may be ommited\n\t-skip switch does nothing because this approach does not allow any conflicts\n\tOther switches specifying schemas have no (accumulative) effect with this switch.\n\tThe name of the long form schema and of the file are derived from the name\n\tof the specified schema unless overrideng by -out switch.\n\n-schema_instance schema_name\n\tIncludes all the schemas which models are associated models\n\tof the schema instance of the specified schema.\n\tIt may be the same subset of schemas as with -top switch\n\tor a different subset, at least with current schema instances.\n\tThis switch may produce wrong results when generating express,\n\tuse it with caution or don't use it at all.\n\n-skip_conflicts\n-skip\n\tThis switch has effect only when -long or -long_form switch is used.\n\tIf there are name conflicts when merging schemas into long form,\n\tthe default behaviour is to generate express\n\tfor all the subsequent occurences of the same name,\n\twhen this switch is used, express is generated for the first occurence only.\n\tThe generator informs about conflicts by warning messages in both cases.\n\n-print_schemas\n-print\n\tPrints the list of schemas included with switches\n\t-top/-top_schema or -schema_instance/-schema\n\tinto the subset for generating express\n-format\n-format_expressions\n\tAttempts to format express inside expressions\n-end_comment\n\tAdds names as comments to END_ENTITY, END_FUNCTION, etc.\n\n";



			String location = "ExpressCompilerRepo";

			System.out.println("");
			System.out.println("JSDAI(TM) Express Generator,   Copyright (C) 2009 LKSoftWare GmbH");
			System.out.println("---------------------------------------------------------------------------");
		
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-help")) flag_help = true;
				if (args[i].equalsIgnoreCase("-?")) flag_help = true;
				if ((args[i].equalsIgnoreCase("-long_form")) || (args[i].equalsIgnoreCase("-long"))) flag_long_form = true;
				if ((args[i].equalsIgnoreCase("-print_schemas")) || (args[i].equalsIgnoreCase("-print"))){
					flag_print_schemas = true;
					flag_printed_schema = 0; 
				}	
				if (args[i].equalsIgnoreCase("-end_comment")) flag_end_comment = true;
				if (args[i].equalsIgnoreCase("-format_expressions")) flag_format_expressions = true;
				if (args[i].equalsIgnoreCase("-format")) flag_format_expressions = true;
				if (args[i].equalsIgnoreCase("-dont_allign_local")) flag_do_not_allign_LOCAL = true;
				if (args[i].equalsIgnoreCase("-include_arm")) flag_include_arm = true;
				if (args[i].equalsIgnoreCase("-exclude_arm")) flag_exclude_arm = true;
				if (args[i].equalsIgnoreCase("-skip_conflicts")) flag_skip_conflicts = true;
				if (args[i].equalsIgnoreCase("-skip")) flag_skip_conflicts = true;
				if (args[i].equalsIgnoreCase("-exclude_internal")) {
					excludeInternalDictionaryModels();
			  }
				if (args[i].equalsIgnoreCase("-iso_db_path")) {
					i++;
					iso_file = args[i];
				}
				if ((args[i].equalsIgnoreCase("-repository_location")) || (args[i].equalsIgnoreCase("-location"))){
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A repository path or name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A repository path or name must follow the " + args[i-1] + " switch");
						return;
					}
					location = args[i];
				}
				if ((args[i].equalsIgnoreCase("-output")) || (args[i].equalsIgnoreCase("-out"))){
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A path or name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A path or name must follow the " + args[i-1] + " switch");
						return;
					}
					express_path = args[i];
				}
				if (args[i].equalsIgnoreCase("-include_list")) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A file name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A file name must follow the " + args[i-1] + " switch");
						return;
					}
					includeModelsFromListInFile(args[i]);
				}
				if (args[i].equalsIgnoreCase("-exclude_list")) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A file name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A file name must follow the " + args[i-1] + " switch");
						return;
					}
					excludeModelsFromListInFile(args[i]);
				}
				if ((args[i].equalsIgnoreCase("-top_schema")) || (args[i].equalsIgnoreCase("-top"))) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A schema name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A schema name must follow the " + args[i-1] + " switch");
						return;
					}
					top_schema = args[i];
				}
				if (args[i].equalsIgnoreCase("-schema_instance")){
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A schema name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A schema name must follow the " + args[i-1] + " switch");
						return;
					}
					specified_schema = args[i];
				}
				if (args[i].equalsIgnoreCase("-schema")){
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A schema name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A schema name must follow the " + args[i-1] + " switch");
						return;
					}
					flag_schema_specific_long = true;
					specified_schema = args[i];
				}
			}

			if (flag_help) {

				System.out.println(help_string);
/*
				System.out.println("USAGE:\n");
				System.out.println("java jsdai.tools.ExpressGenerator -location name|path]");
				System.out.println("\ncommand line switches:\n");
				System.out.println("-location name|path");
				System.out.println("\tthe name of or the path to the repository to be checked");
				System.out.println("\t\tdefault: ExpressCompilerRepo, location specified in jsdai.properties");
				System.out.println("-output name|path");
				System.out.println("\tthe name of or the path of the express file to be generated");
				System.out.println("\t\tdefault: mim_schemas.exp in the current directory");
*/
				return;
			}
	
			ExpressGenerator xg = new ExpressGenerator();
			SdaiSession session = SdaiSession.openSession();
			SdaiTransaction trans = session.startTransactionReadOnlyAccess();
      if (flag_schema_specific_long) {
      	xg.runLong(SimpleOperations.linkRepositoryOrName("ExpressCompilerRepo", location));
      } else {	
      	xg.run(SimpleOperations.linkRepositoryOrName("ExpressCompilerRepo", location));
      }
      session.closeSession();
			
	
	}
	
  	public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args) throws SdaiException {
  		Properties jsdaiProperties = new Properties();
  		jsdaiProperties.setProperty("repositories", sdaireposDirectory);
  		SdaiSession.setSessionProperties(jsdaiProperties);
  		return new Runnable() {
  			public void run() {
  				try {
  	  				main(args);
  				} catch (SdaiException e1) {
  					e1.printStackTrace();
  				} catch (java.io.IOException e2) { 
  					e2.printStackTrace();
  				}
  			}
  		};
  	}

	
	void runLong(SdaiRepository repo) throws SdaiException, java.io.IOException {

		flag_long_form = true;
		File fiso = new File(iso_file);
		if (fiso.exists()) {
			//readIsoNumbersOfSchemas(iso_file);
			readIsoIdsAndPartNamesOfSchemas(iso_file);
		}

		express_long_file = specified_schema + ".exp";
		express_long_schema = specified_schema;
		if (express_path != null) {
			if (express_path.endsWith(".exp") || express_path.endsWith(".EXP")) {
				express_long_file = express_path;
				int index1 = express_path.lastIndexOf('/');
				int index2 = express_path.lastIndexOf('\\');
				int index = (index1 > index2) ? index1 : index2;
				if (index > 0) {
					express_long_schema = express_path.substring(index+1, express_path.length()-4);
				} else {
					express_long_schema = express_path.substring(0, express_path.length()-4);
				}
			} else {
				express_long_file = express_path + File.separator + express_long_file;
			}
		}
  	
		PrintWriter pw = getPrintWriter(express_long_file);
	
		repo.openRepository();
//		repo.exportClearTextEncoding("_KUKU3Jurga.pf");
		
		SchemaInstance si = null;
		ASchemaInstance asi = repo.getSchemas(); 
		SdaiIterator iter_si = asi.createIterator();
		while (iter_si.next()) {
			SchemaInstance current_si = asi.getCurrentMember(iter_si); 
			if (current_si.getName().equalsIgnoreCase(specified_schema)) {
				si = current_si;
				break;
			}
		}
		if (si == null) {
			System.out.println("Schema instance for the specified schema not found: " + specified_schema);
			return;
		}

		ASdaiModel domain = si.getAssociatedModels();
		
		// getInstances() method for SchemaInstance does not exist
		// getInstances() for associated model return 0 elements, because the models are in no access mode
		// seems like I have to make them read-only one-by-one, also, they all will be needed read-only anyway
		SdaiIterator iter_assoc = domain.createIterator();
		while (iter_assoc.next()) {
			SdaiModel mod = (SdaiModel)domain.getCurrentMember(iter_assoc);
			if (mod.getMode() == SdaiModel.NO_ACCESS) {
				mod.startReadOnlyAccess();
			}
			schemas.addByIndex(schemas.getMemberCount()+1, mod);
		}
		
 		ESchema_definition sd = null;
		// could have done this in the loop above, obviously
		ASchema_definition asd = (ASchema_definition)domain.getInstances(ESchema_definition.class); 
		SdaiIterator iter_sd = asd.createIterator();
		while (iter_sd.next()) {
			ESchema_definition current_sd = asd.getCurrentMember(iter_sd); 
			if (current_sd.getName(null).equalsIgnoreCase(specified_schema)) {
				sd = current_sd;
				break;
			}
		}
		if (sd == null) {
			System.out.println("Schema definition for the specified schema " + specified_schema + " not found in schema instance " + si);
			return;
		}
 
		pw.println("SCHEMA " + express_long_schema + ";");

		// constants
		AConstant_declaration constant_declarations = new AConstant_declaration();
		CConstant_declaration.usedinParent(null, sd, domain, constant_declarations);
    EConstant_definition [] constant_definitions = new EConstant_definition[constant_declarations.getMemberCount()];
		SdaiIterator iter_const = constant_declarations.createIterator();
		int constant_count = 0;
		while (iter_const.next()) {
	    EConstant_declaration constant_declaration = (EConstant_declaration)constant_declarations.getCurrentMemberObject(iter_const);
			constant_definitions[constant_count++] = (EConstant_definition)constant_declaration.getDefinition(null);       	
		}
    Arrays.sort(constant_definitions, new SorterForEntities());

		String str_constants = "";
    for (int i = 0; i < constant_count; i++) { 
	    EConstant_definition constant_definition = (EConstant_definition)constant_definitions[i];
			str_constants += printConstant(constant_definition, repo);
		}
		if (!str_constants.equals("")) {
	    pw.println("\nCONSTANT\n");
			pw.print(str_constants);
   	  pw.println("END_CONSTANT;\n");
		}

		// defined types
		AType_declaration type_declarations = new AType_declaration();
		CType_declaration.usedinParent(null, sd, domain, type_declarations);
    EDefined_type [] defined_types = new EDefined_type[type_declarations.getMemberCount()];
		SdaiIterator iter_type = type_declarations.createIterator();
		int type_count = 0;
		while (iter_type.next()) {
	    EType_declaration type_declaration = (EType_declaration)type_declarations.getCurrentMemberObject(iter_type);
			defined_types[type_count++] = (EDefined_type)type_declaration.getDefinition(null);       	
		}
    Arrays.sort(defined_types, new SorterForEntities());


		// move type handling from here down just before entity handling but where entity_definitions are already available,
		// so that they can be used for pruning select types
		

		// entities
		AEntity_declaration entity_declarations = new AEntity_declaration();
		CEntity_declaration.usedinParent(null, sd, domain, entity_declarations);
    EEntity_definition [] entity_definitions = new EEntity_definition[entity_declarations.getMemberCount()];
		SdaiIterator iter_entity = entity_declarations.createIterator();
		int entity_count = 0;
		while (iter_entity.next()) {
	    EEntity_declaration entity_declaration = (EEntity_declaration)entity_declarations.getCurrentMemberObject(iter_entity);
			entity_definitions[entity_count++] = (EEntity_definition)entity_declaration.getDefinition(null);       	
		}
    Arrays.sort(entity_definitions, new SorterForEntities());

		
		// moved here from above commented location, so that entity_definitions are already available when processing types
    for (int i = 0; i < type_count; i++) { 
	    EDefined_type defined_type = (EDefined_type)defined_types[i];
			printDefinedTypeLF(pw, defined_type, repo, entity_definitions, entity_count, defined_types, type_count);
		}

    for (int i = 0; i < entity_count; i++) { 
	    EEntity_definition entity_definition = (EEntity_definition)entity_definitions[i];
			// do we print complex entities? not now
			if (entity_definition.getComplex(null)) continue;
			printEntity(pw, entity_definition, repo, entity_definitions, entity_count); 
		}

 		// subtype_constraints
		ASubtype_constraint_declaration subtype_constraint_declarations = new ASubtype_constraint_declaration();
		CSubtype_constraint_declaration.usedinParent(null, sd, domain, subtype_constraint_declarations);
    ESub_supertype_constraint [] subtype_constraints = new ESub_supertype_constraint[subtype_constraint_declarations.getMemberCount()];
		SdaiIterator iter_sub = subtype_constraint_declarations.createIterator();
		int subtype_constraint_count = 0;
		while (iter_sub.next()) {
	    ESubtype_constraint_declaration subtype_constraint_declaration = (ESubtype_constraint_declaration)subtype_constraint_declarations.getCurrentMemberObject(iter_sub);
			// should we check if it has a name, before adding to the array?
			// no, only stand-alone subtype_constraints have declarations, so everything is ok
			subtype_constraints[subtype_constraint_count++] = (ESub_supertype_constraint)subtype_constraint_declaration.getDefinition(null);       	
		}
    Arrays.sort(subtype_constraints, new SorterForEntities());

    for (int i = 0; i < subtype_constraint_count; i++) { 
	    ESub_supertype_constraint subtype_constraint = (ESub_supertype_constraint)subtype_constraints[i];
			printSubtypeConstraintLF(pw, subtype_constraint, null, entity_definitions, entity_count); // schema_definition is not used in it 
		}

 		// global rules
		ARule_declaration rule_declarations = new ARule_declaration();
		CRule_declaration.usedinParent(null, sd, domain, rule_declarations);
    EGlobal_rule [] global_rules = new EGlobal_rule[rule_declarations.getMemberCount()];
		SdaiIterator iter_rule = rule_declarations.createIterator();
		int rule_count = 0;
		while (iter_rule.next()) {
	    ERule_declaration rule_declaration = (ERule_declaration)rule_declarations.getCurrentMemberObject(iter_rule);
			global_rules[rule_count++] = (EGlobal_rule)rule_declaration.getDefinition(null);       	
		}
    Arrays.sort(global_rules, new SorterForEntities());

    for (int i = 0; i < rule_count; i++) { 
	    EGlobal_rule global_rule = (EGlobal_rule)global_rules[i];
			printGlobalRuleLF(pw, global_rule, repo, entity_definitions, entity_count); 
		}


 		// functions 
		AFunction_declaration function_declarations = new AFunction_declaration();
		CFunction_declaration.usedinParent(null, sd, domain, function_declarations);
    EFunction_definition [] function_definitions = new EFunction_definition[function_declarations.getMemberCount()];
		SdaiIterator iter_function = function_declarations.createIterator();
		int function_count = 0;
		while (iter_function.next()) {
	    EFunction_declaration function_declaration = (EFunction_declaration)function_declarations.getCurrentMemberObject(iter_function);
			function_definitions[function_count++] = (EFunction_definition)function_declaration.getDefinition(null);       	
		}
    Arrays.sort(function_definitions, new SorterForEntities());

    for (int i = 0; i < function_count; i++) { 
	    EFunction_definition function_definition = (EFunction_definition)function_definitions[i];
			printFunction(pw, function_definition, repo); 
		}

 		// procedures
		AProcedure_declaration procedure_declarations = new AProcedure_declaration();
		CProcedure_declaration.usedinParent(null, sd, domain, procedure_declarations);
    EProcedure_definition [] procedure_definitions = new EProcedure_definition[procedure_declarations.getMemberCount()];
		SdaiIterator iter_procedure = procedure_declarations.createIterator();
		int procedure_count = 0;
		while (iter_procedure.next()) {
	    EProcedure_declaration procedure_declaration = (EProcedure_declaration)procedure_declarations.getCurrentMemberObject(iter_procedure);
			procedure_definitions[procedure_count++] = (EProcedure_definition)procedure_declaration.getDefinition(null);       	
		}
    Arrays.sort(procedure_definitions, new SorterForEntities());

    for (int i = 0; i < procedure_count; i++) { 
	    EProcedure_definition procedure_definition = (EProcedure_definition)procedure_definitions[i];
			printProcedure(pw, procedure_definition, repo); 
		}
 		

		// END_SCHEMA
		if (flag_end_comment) {
			pw.println("END_SCHEMA; --" + express_long_schema + "\n");
		} else {
			pw.println("END_SCHEMA; \n");
		}
    pw.flush();
 	  pw.close();
	}
	
	void run(SdaiRepository repo) throws SdaiException, java.io.IOException {

		File fiso = new File(iso_file);
		if (fiso.exists()) {
			// readIsoNumbersOfSchemas(iso_file);
			readIsoIdsAndPartNamesOfSchemas(iso_file);
		}

		repo.openRepository();
		repo.exportClearTextEncoding("_KUKU3Jurga.pf");
		System.out.println();

		if (top_schema != null) {
			if (!includeTopSchemaModels(repo)) {
				System.out.println("The specified top schema not found: " + top_schema);
				System.out.println("\nExpress Generator aborted");
				return;
			}
		}
		if (specified_schema != null) {
			if (!includeSchemaModels(repo)) {
				System.out.println("The specified schema not found: " + specified_schema);
				System.out.println("\nExpress Generator aborted");
				return;
			}
		}

		ASdaiModel models = repo.getModels();

		SdaiIterator iter = models.createIterator();
    int count = 0;
		HashSet interfaced_schemas = new HashSet();
    

		TreeSet modelSet = new TreeSet(new SorterForModels());
    asmExpressDomain.clear();
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			String name = model.getName();
			if (name.startsWith("_EXPRESS_")) {
	      if (model.getMode() == SdaiModel.NO_ACCESS) {
        	model.startReadOnlyAccess();
	      }
        asmExpressDomain.addUnordered(model, null);
			} else 
			if (name.endsWith("_DICTIONARY_DATA")) {
				if (flag_include_arm && (!name.endsWith("_ARM_DICTIONARY_DATA"))) continue;
				if (flag_exclude_arm && name.endsWith("_ARM_DICTIONARY_DATA")) continue;
	      if (model.getMode() == SdaiModel.NO_ACCESS) {
        	model.startReadOnlyAccess();
	      }

//				if (((include_models.size() == 0) || haveVector(include_models, name)) && (!haveVector(exclude_models, name))) {

				if ((include_models.isEmpty() || include_models.contains(name)) && (!exclude_models.contains(name))) {
				    modelSet.add(model);
						schemas.addByIndex(schemas.getMemberCount()+1, model);
				}
    	}
    }



    
    if (flag_long_form) {


			if (flag_include_arm) {
				express_long_file = "arm_lf.exp";
				express_long_schema = "arm_lf_schema";
			}
			if (flag_exclude_arm) {
				express_long_file = "aim_lf.exp";
				express_long_schema = "aim_lf_schema";
			}
			if (top_schema != null) {
				express_long_file = top_schema + ".exp";
				express_long_schema = top_schema;
			} else
			if (specified_schema != null) {
				express_long_file = specified_schema + ".exp";
				express_long_schema = specified_schema;
			}
			if (express_path != null) {
				if (express_path.endsWith(".exp") || express_path.endsWith(".EXP")) {
					express_long_file = express_path;
					int index1 = express_path.lastIndexOf('/');
					int index2 = express_path.lastIndexOf('\\');
					int index = (index1 > index2) ? index1 : index2;
					if (index > 0) {
						express_long_schema = express_path.substring(index+1, express_path.length()-4);
					} else {
						express_long_schema = express_path.substring(0, express_path.length()-4);
					}
				} else {
					express_long_file = express_path + File.separator + express_long_file;
				}
			}
			
  	
			PrintWriter pw = getPrintWriter(express_long_file);
			
			// will catch duplicated names here
  		hm_all_names = new HashMap();
  		

			pw.println("SCHEMA " + express_long_schema + ";");


  		// constants - will have to deal with start end as well - later
  		// CONSTANT

			Iterator modelIter = modelSet.iterator();
			String str_constants = "";
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				str_constants += printConstants(sd, model);
			}
			if (!str_constants.equals("")) {
		    pw.println("\nCONSTANT");
				pw.println(str_constants);
    	  pw.println("END_CONSTANT;\n");
			}
  		
  		// END_CONSTANT
  	
  		// types
			modelIter = modelSet.iterator();
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				printTypes(pw, sd, model);
			}
 		
  		// entities
			modelIter = modelSet.iterator();
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				printEntities(pw, sd, model);
			}

  		// subtype_constraints
			modelIter = modelSet.iterator();
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				printSubtypeConstraints(pw, sd, model);
			}
			
  		// global rules
			modelIter = modelSet.iterator();
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				printGlobalRules(pw, sd, model);
			}

  		// functions 
			modelIter = modelSet.iterator();
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				printFunctions(pw, sd, model);
			}
  		
  		// procedures
			modelIter = modelSet.iterator();
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				printProcedures(pw, sd, model);
			}
  	
  		// END_SCHEMA
			if (flag_end_comment) {
				pw.println("END_SCHEMA; --" + express_long_schema + "\n");
			} else {
				pw.println("END_SCHEMA; \n");
			}
  	
	    pw.flush();
  	  pw.close();
  	
  	} else {
  		
			Iterator modelIter = modelSet.iterator();
			while (modelIter.hasNext()) {
				SdaiModel model = (SdaiModel)modelIter.next();
				ESchema_definition sd = getSchema_definitionFromModel(model);
				current_schema = sd.getName(null);
				printModelSchema(model, express_path);
			}
		} 
		
	
//    pw.flush();
//    pw.close();
		System.out.println("\nExpress Generator finished");
	
	}

	public static void generateSchemaExpress(SchemaInstance sch_inst, String file_name) throws SdaiException {

		File fiso = new File(iso_file);
		if (fiso.exists()) {
			//readIsoNumbersOfSchemas(iso_file);
			readIsoIdsAndPartNamesOfSchemas(iso_file);
		}
		SdaiRepository repo = sch_inst.getRepository();
//		SdaiModel work = repo.createSdaiModel("working", SExtended_dictionary_schema.class);
		SdaiSession session = repo.getSession();
		String searched_name = sch_inst.getName().toUpperCase() + DICT_MOD_SUFIX;
		ASdaiModel assoc_mods = sch_inst.getAssociatedModels();
		SdaiIterator iter_assoc = assoc_mods.createIterator();
		SdaiModel model = null;
		while (iter_assoc.next()) {
			SdaiModel mod = (SdaiModel)assoc_mods.getCurrentMember(iter_assoc);
			if (mod.getName().equals(searched_name)) {
				model = mod;
				break;
			}
		}
		if (model != null) {
			if (model.getMode() == SdaiModel.NO_ACCESS) {
				model.startReadOnlyAccess();
			}
			ExpressGenerator xg = new ExpressGenerator();
//				mg.printModelSchema(model, session, repo, work, file_name);
				xg.printModelSchema(model, file_name);
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
//		work.deleteSdaiModel();
	}


	private void printModelSchema(SdaiModel model, String file_name) throws SdaiException {


		ESchema_definition sd = getSchema_definitionFromModel(model);
		String name = sd.getName(null);
		// add some logic later, if file_name not null, etc.
    String express_file_name = null;
    if (file_name.endsWith(".exp")) {
    	express_file_name = file_name;
    } else 
    if (file_name != null) {
    	express_file_name = file_name + File.separator + name + ".exp";
    } else {
    	express_file_name = name + ".exp";
    }
    PrintWriter pw = getPrintWriter(express_file_name);

		printFileHeader(pw, sd, model);
		printLanguageVersion(pw, sd, model);
		printSchemaHeader(pw, sd, model);
		printInterfacedSpecifications(pw, sd, model);
		String str_constants = printConstants(sd, model);
		if (!str_constants.equals("")) {
	  	pw.println("\nCONSTANT");
			pw.println(str_constants);
	    pw.println("END_CONSTANT;\n");
		}
		printTypes(pw, sd, model);
		printEntities(pw, sd, model);
		printSubtypeConstraints(pw, sd, model);
		printGlobalRules(pw, sd, model);
		printFunctions(pw, sd, model);
		printProcedures(pw, sd, model);

		if (flag_end_comment) {
			pw.println("END_SCHEMA; -- " + name + "\n");
		} else {
			pw.println("END_SCHEMA; \n");
		}
    pw.flush();
    pw.close();

	}


	void printFileHeader(PrintWriter pw, ESchema_definition sd, SdaiModel model) throws SdaiException {
		String line = null;
		String second_header_line = "";
		String iso_number = null;
		if (hm_iso_ids != null) {
			iso_number = (String)hm_iso_ids.get(sd.getName(null).toLowerCase());
			if (iso_number == null) {
				// iso_number = "ISO 10303-xxxx (not in the database)";
			}
		} else {
			//iso_number = "ISO 10303-xxxx (iso database is missing)";
		}
		if (iso_number != null) {
			second_header_line = "\t" + iso_number;
		}
		pw.println("(*");
		pw.println(second_header_line);
		pw.println("*)\n");
	}

	void printFileHeader_old(PrintWriter pw, ESchema_definition sd, SdaiModel model) throws SdaiException {
		String line = null;
		String first_header_line = null;
		String second_header_line = null;
		String ident = "$" + "Id: this is CVS ID tag, it will be replaced when commiting to CVS " + "$";
		String iso_number = null;
		if (hm_iso_ids != null) {
			iso_number = (String)hm_iso_ids.get(sd.getName(null).toLowerCase());
			if (iso_number == null) {
				iso_number = "ISO 10303-xxxx (not in the database)";
			}
		} else {
			iso_number = "ISO 10303-xxxx (iso database is missing)";
		}
		first_header_line = "   " + ident;
		second_header_line = "   " + iso_number;
		pw.println("(*");
		pw.println(first_header_line);
		pw.println(second_header_line);
		pw.println("*)\n");
	}


	void printLanguageVersion(PrintWriter pw, ESchema_definition sd, SdaiModel model) {
		// not supported, because not stored anywhere
	}

	void printSchemaHeader(PrintWriter pw, ESchema_definition sd, SdaiModel model) throws SdaiException {
		String schema_version_id = null;
		String name = sd.getName(null);
		if (sd.testIdentification(null)) {
			schema_version_id = sd.getIdentification(null);
		}
		if (schema_version_id != null) {
			pw.println("SCHEMA " + name + " " + schema_version_id + ";");
		} else {
			pw.println("SCHEMA " + name + ";");
		}
	}

	void printInterfacedSpecifications(PrintWriter pw, ESchema_definition sd, SdaiModel model) throws SdaiException {

		String result = "";

    // RR - let's have only direct interfacing first, chained use froms discarded, 
    // the specification information reflects the original use froms and reference froms from express
    // so, if there are several use froms from the same schema - we will have a line fro each one of them here.
    // we also would like to preserve the original order but it may not be possible

//		SdaiModel model = schema.findEntityInstanceSdaiModel();
		AInterface_specification specifications = (AInterface_specification)model.getInstances(CInterface_specification.class);
    EInterface_specification[] specifications_set = new EInterface_specification[specifications.getMemberCount()];
		SdaiIterator specifications_it = specifications.createIterator();
		int iSSetCount = 0;
		
		while (specifications_it.next()) {
	    EInterface_specification specification = (EInterface_specification)specifications.getCurrentMemberObject(specifications_it);
      specifications_set[iSSetCount]=specification;
      iSSetCount++;
		}
    Arrays.sort(specifications_set, new SpecSorterBySchema());

    for (int i=0; i<iSSetCount; i++) { 
	    EInterface_specification specification = (EInterface_specification)specifications_set[i];
			String current_str = "";
			if (specification instanceof EUse_from_specification) {
				current_str += "\nUSE FROM ";
			} else 
			if (specification instanceof EReference_from_specification) {
				current_str += "\nREFERENCE FROM ";
			}

//			String referenced_schema = specification.getForeign_schema(null).getName(null).toLowerCase();
			String referenced_schema = specification.getForeign_schema(null).getName(null);
//			current_str += printHRef(referenced_schema, "../S"+getUpper(correctSchemaName(referenced_schema)) + "/package-summary.html");
			current_str += referenced_schema;

			
			AInterfaced_declaration items = null;
		
			if (specification.testItems(null)) {
				items = specification.getItems(null);
			}
			if (items != null) {
				current_str += " (";
				current_str += printIsoNumberComment(referenced_schema);
				SdaiIterator items_it = items.createIterator();
		    EDeclaration[] declarations_set = new EDeclaration[items.getMemberCount()];
				int iDSetCount = 0;
  			while (items_it.next()) {
					EDeclaration item = (EDeclaration)items.getCurrentMemberObject(items_it);
		      declarations_set[iDSetCount]=item;
    		  iDSetCount++;
        }
		    Arrays.sort(declarations_set, new SorterByDefinition());


				
				boolean first_time = true;
				String item_str = "";


				for (int j = 0; j < iDSetCount; j++) {
					EInterfaced_declaration item = (EInterfaced_declaration)declarations_set[j];
					
//					String item_name =  "<tt>" + getDictionaryEntityTypeRR3(item) + " </tt>" + printHRef((item.testAlias_name(null))?item.getAlias_name(null):getComplexName(getDictionaryEntityName(item.getDefinition(null))),
//					String item_name =  printHRef((item.testAlias_name(null))?item.getAlias_name(null):getComplexName(getDictionaryEntityName(item.getDefinition(null))),"../S"+getUpper(correctSchemaName(findSchemaForEntity(item.getDefinition(null)).getName(null)))+"/"+getUpper(getComplexName(getDictionaryEntityName(item.getDefinition(null))))+".html");
					String item_name =  item.testAlias_name(null)?item.getAlias_name(null):getComplexName(getDictionaryEntityName(item.getDefinition(null)));

/*
					EEntity definition = item.getDefinition(null);
					String item_name = null;
					if (definition instanceof ENamed_type) {
						item_name = ((ENamed_type)definition).getName(null);
					} else
					if (definition instanceof EAlgorithm_definition) {
						item_name = ((EAlgorithm_definition)definition).getName(null);
					} else
					if (definition instanceof EGlobal_rule) {
						item_name = ((EGlobal_rule)definition).getName(null);
					}					
*/				
				
				if (first_time) {
					first_time = false;
					item_str += println("") + printUnclosedTab(item_name);
				} else {
//					used += ", "+temp;
					item_str += println(",") +printUnclosedTab(item_name);
				}
				
				
				}
				if (item_str != null) {
					current_str += println(item_str); 
				}
				 
				current_str += ");";
			} else {
				current_str += ";";
				current_str += printIsoNumberComment(referenced_schema);
			}
//			result += printTab(current_str);
			result += println(current_str);
		}
    if (!result.equals("")) {
			result += printBreak();
		}
		// RR - end of direct specifications -------------------------------------------------------------------------------------

		pw.println(result);
	}

	String printConstants(ESchema_definition schema, SdaiModel model) throws SdaiException {
		String str_constants = "";

    // --- Constants ---
    AConstant_declaration constants = (AConstant_declaration)model.getInstances(EConstant_declaration.class);
    SdaiIterator iter_c = constants.createIterator();
    TreeSet aconstants = new TreeSet(new SorterForEntities());
		TreeSet ref_aconstants = new TreeSet(new SorterForEntities());
    while (iter_c.next()) {
			EConstant_declaration declaration = (EConstant_declaration)constants.getCurrentMember(iter_c);
			if (declaration instanceof ELocal_declaration) {
      	aconstants.add((EEntity)declaration.getDefinition(null));
			} else {
      	ref_aconstants.add((EEntity)declaration.getDefinition(null));
			}
		}
    if (aconstants.size() > 0) {
			Iterator aconstantsIter = aconstants.iterator();
			while (aconstantsIter.hasNext()) {
				EConstant_definition constant = (EConstant_definition)aconstantsIter.next();
				if (flag_long_form) {
        	String name = constant.getName(null);
					if (!(hm_all_names.containsKey(name.toLowerCase()))) {
						hm_all_names.put(name.toLowerCase(), schema.getName(null));
					} else {
						String other_schema = (String)hm_all_names.get(name.toLowerCase());
						System.out.println("duplicate name " + name + " in schema " + schema.getName(null) + " (constant), and in schema: " + other_schema);
						if (flag_skip_conflicts) continue;
					}
				}        
        str_constants += printConstant(constant, schema);
			}
		}
		return str_constants;
	}
	
	void printTypes(PrintWriter pw, ESchema_definition schema, SdaiModel model) throws SdaiException {

    //-- Type declarations --
    AType_declaration types = schema.getType_declarations(null, null);
		SdaiIterator iter1 = types.createIterator();
		TreeSet atypes = new TreeSet(new SorterForEntities());
		TreeSet ref_atypes = new TreeSet(new SorterForEntities());
		while (iter1.next()) {
			EDeclaration declaration = (EDeclaration)types.getCurrentMember(iter1);
			if (declaration instanceof ELocal_declaration) {
		    atypes.add((EEntity)declaration.getDefinition(null));
			} else {
				// not needed
		    ref_atypes.add((EEntity)declaration.getDefinition(null));
			}
		}
    if (types.getMemberCount() > 0) {
			Iterator atypesIter = atypes.iterator();
			while (atypesIter.hasNext()) {
				EDefined_type type = (EDefined_type)atypesIter.next();
				if (flag_long_form) {
        	String name = type.getName(null);
					if (!(hm_all_names.containsKey(name.toLowerCase()))) {
						hm_all_names.put(name.toLowerCase(), schema.getName(null));
					} else {
						String other_schema = (String)hm_all_names.get(name.toLowerCase());
						System.out.println("duplicate name " + name + " in schema " + schema.getName(null) + " (type), and in schema: " + other_schema);
						if (flag_skip_conflicts) continue;
					}
				}        
				printDefinedType(pw, type, false);
			}
		}

	}

	void printEntities(PrintWriter pw, ESchema_definition schema, SdaiModel model) throws SdaiException {

		AEntity_declaration entities = schema.getEntity_declarations(null, null);
		SdaiIterator iter2 = entities.createIterator();
		// V.N.
// 		Vector aentity = new Vector();
// 		Vector acomplex = new Vector();
// 		Vector ref_aentity = new Vector();
		TreeSet aentity = new TreeSet(new SorterForEntities());
		TreeSet acomplex = new TreeSet(new SorterForEntities());
		TreeSet ref_aentity = new TreeSet(new SorterForEntities());
		while (iter2.next()) {
			EEntity_declaration decl = entities.getCurrentMember(iter2);
            if (decl instanceof ELocal_declaration) {
				EEntity_definition entity = (EEntity_definition)decl.getDefinition(null);
				if (entity.getComplex(null)) {
						// not needed
				    acomplex.add((EEntity)entity);
				} else {
				    aentity.add((EEntity)entity);
				}
			} else {
				// not needed
				    ref_aentity.add((EEntity)decl.getDefinition(null));
			}
		}
// 	    System.out.println("Schema start D: " + schemaName);
		if (aentity.size() > 0) {
			for (Iterator i = aentity.iterator(); i.hasNext(); ) {
				EEntity_definition entity = (EEntity_definition)i.next();
				if (flag_long_form) {
        	String name = entity.getName(null);
					if (!(hm_all_names.containsKey(name.toLowerCase()))) {
						hm_all_names.put(name.toLowerCase(), schema.getName(null));
					} else {
						String other_schema = (String)hm_all_names.get(name.toLowerCase());
						System.out.println("duplicate name " + name + " in schema " + schema.getName(null) + " (entity), and in schema: " + other_schema);
						if (flag_skip_conflicts) continue;
					}
				}        
				printEntity(pw, entity, schema);
			}
		}
/*
		// no express for complex entities, ok?
		if (acomplex.size() > 0) {
			partIndexTmp += printH3("Complex entities");
			for (Iterator i = acomplex.iterator(); i.hasNext(); ) {
				EEntity_definition entity = (EEntity_definition)i.next();
				printEntity(entity, schema);
                //System.out.println(entity);
				partIndexTmp += printHRefandTarget(getComplexName(entity.getName(null)), getUpper(getComplexName(entity.getName(null)))+".html", "classFrame");
				partIndexTmp += println();
			}
		}
*/

	}

	void printSubtypeConstraints(PrintWriter pw, ESchema_definition schema, SdaiModel model) throws SdaiException {

	  // --- Subtype Constraints ---
//System.out.println("<> in subtype constraints");
        ASubtype_constraint_declaration constraints = (ASubtype_constraint_declaration)model.getEntityExtentInstances(ESubtype_constraint_declaration.class);
        SdaiIterator iter_sc = constraints.createIterator();
        TreeSet aconstraints = new TreeSet(new SorterForEntities());
				TreeSet ref_aconstraints = new TreeSet(new SorterForEntities());
        while (iter_sc.next()) {
					ESubtype_constraint_declaration declaration = (ESubtype_constraint_declaration)constraints.getCurrentMember(iter_sc);
//System.out.println("<> in subtype constraints - current: " + declaration);
					// only stand-alone subtype constraints have declarations, yes? if not, additionally check the name from definition
					if (declaration instanceof ELocal_declaration) {
			    	aconstraints.add((EEntity)declaration.getDefinition(null));
					} else {
			    	ref_aconstraints.add((EEntity)declaration.getDefinition(null));
					}
				}
//System.out.println("<> in subtype constraints - size: " + aconstraints.size());
    		if (aconstraints.size() > 0) {
					Iterator aconstraintsIter = aconstraints.iterator();
					while (aconstraintsIter.hasNext()) {
						ESub_supertype_constraint constraint = (ESub_supertype_constraint)aconstraintsIter.next();

				if (flag_long_form) {
        	String name = constraint.getName(null);
					if (!(hm_all_names.containsKey(name.toLowerCase()))) {
						hm_all_names.put(name.toLowerCase(), schema.getName(null));
					} else {
						String other_schema = (String)hm_all_names.get(name.toLowerCase());
						System.out.println("duplicate name " + name + " in schema " + schema.getName(null) + " (subtype_constraint), and in schema: " + other_schema);
						if (flag_skip_conflicts) continue;
					}
				}        

          	printSubtypeConstraint(pw, constraint, schema);
					}
				}

	}

	void printGlobalRules(PrintWriter pw, ESchema_definition schema, SdaiModel model) throws SdaiException {


	  // --- Global rules ---
    ARule_declaration rules = schema.getRule_declarations(null, null);
    SdaiIterator iter_r = rules.createIterator();
    TreeSet arules = new TreeSet(new SorterForEntities());
		TreeSet ref_arules = new TreeSet(new SorterForEntities());
    while (iter_r.next()) {
			ERule_declaration declaration = (ERule_declaration)rules.getCurrentMember(iter_r);
			if (declaration instanceof ELocal_declaration) {
				arules.add((EEntity)declaration.getDefinition(null));
			} else {
				ref_arules.add((EEntity)declaration.getDefinition(null));
			}
		}
    if (arules.size() > 0) {
			Iterator arulesIter = arules.iterator();
			while (arulesIter.hasNext()) {
				EGlobal_rule rule = (EGlobal_rule)arulesIter.next();
				if (flag_long_form) {
        	String name = rule.getName(null);
					if (!(hm_all_names.containsKey(name.toLowerCase()))) {
						hm_all_names.put(name.toLowerCase(), schema.getName(null));
					} else {
						String other_schema = (String)hm_all_names.get(name.toLowerCase());
						System.out.println("duplicate name " + name + " in schema " + schema.getName(null) + " (global rule), and in schema: " + other_schema);
						if (flag_skip_conflicts) continue;
					}
				}        
        printGlobalRule(pw, rule, schema);
			}
		}


	}


	void printFunction(PrintWriter pw, EFunction_definition function, SdaiRepository repo) throws SdaiException {
    asmExpressDomain.clear();
    SdaiModel express_model = findExpressModel(repo, function);
    if (express_model != null) {
    	asmExpressDomain.addUnordered(express_model, null);
		}
		current_schema = function.findEntityInstanceSdaiModel().getName();
		current_schema = current_schema.substring(0, current_schema.lastIndexOf("_DICTIONARY_DATA"));
  	printAlgorithm(pw, function, null);
	}

	void printProcedure(PrintWriter pw, EProcedure_definition procedure, SdaiRepository repo) throws SdaiException {
    asmExpressDomain.clear();
    SdaiModel express_model = findExpressModel(repo, procedure);
    if (express_model != null) {
    	asmExpressDomain.addUnordered(express_model, null);
		}
		current_schema = procedure.findEntityInstanceSdaiModel().getName();
		current_schema = current_schema.substring(0, current_schema.lastIndexOf("_DICTIONARY_DATA"));
  	printAlgorithm(pw, procedure, null);
	}

	void printFunctions(PrintWriter pw, ESchema_definition schema, SdaiModel model) throws SdaiException {


		AAlgorithm_declaration algorithms = schema.getAlgorithm_declarations(null, null);
    SdaiIterator iter_a = algorithms.createIterator();
    TreeSet afunctions = new TreeSet(new SorterForEntities());
//		TreeSet ref_afunctions = new TreeSet(new SorterForEntities());
      while (iter_a.next()) {
				EAlgorithm_declaration declaration = (EAlgorithm_declaration)algorithms.getCurrentMember(iter_a);
        vAlgorithmDefinition.add((EAlgorithm_definition)declaration.getDefinition(null));
				if (declaration instanceof ELocal_declaration) {
        	if (declaration instanceof EFunction_declaration) {
			    	afunctions.add((EEntity)declaration.getDefinition(null));
          }
			} else {
      	if (declaration instanceof EFunction_declaration) {
//			        ref_afunctions.add((EEntity)declaration.getDefinition(null));
        }
			}
		}


    if (afunctions.size() > 0) {
			Iterator afunctionsIter = afunctions.iterator();
			while (afunctionsIter.hasNext()) {
				EFunction_definition function = (EFunction_definition)afunctionsIter.next();
				if (flag_long_form) {
        	String name = function.getName(null);
					if (!(hm_all_names.containsKey(name.toLowerCase()))) {
						hm_all_names.put(name.toLowerCase(), schema.getName(null));
					} else {
						String other_schema = (String)hm_all_names.get(name.toLowerCase());
						System.out.println("duplicate name " + name + " in schema " + schema.getName(null) + " (function), and in schema: " + other_schema);
						if (flag_skip_conflicts) continue;
					}
				}        
        printAlgorithm(pw, function, schema);
			}
		}

	}

	void printProcedures(PrintWriter pw, ESchema_definition schema, SdaiModel model) throws SdaiException {

    AAlgorithm_declaration algorithms = schema.getAlgorithm_declarations(null, null);
    SdaiIterator iter_a = algorithms.createIterator();
    TreeSet aprocedures = new TreeSet(new SorterForEntities());
		// TreeSet ref_aprocedures = new TreeSet(new SorterForEntities());
		while (iter_a.next()) {
			EAlgorithm_declaration declaration = (EAlgorithm_declaration)algorithms.getCurrentMember(iter_a);
      vAlgorithmDefinition.add((EAlgorithm_definition)declaration.getDefinition(null));
			if (declaration instanceof ELocal_declaration) {
      	if (declaration instanceof EProcedure_declaration) {
        	aprocedures.add((EEntity)declaration.getDefinition(null));
        }
			} else {
      	if (declaration instanceof EProcedure_declaration) {
        //	ref_aprocedures.add((EEntity)declaration.getDefinition(null));
        }
			}
		}


    if (aprocedures.size() > 0) {
			Iterator aproceduresIter = aprocedures.iterator();
			while (aproceduresIter.hasNext()) {
				EProcedure_definition procedure = (EProcedure_definition)aproceduresIter.next();
				if (flag_long_form) {
        	String name = procedure.getName(null);
					if (!(hm_all_names.containsKey(name.toLowerCase()))) {
						hm_all_names.put(name.toLowerCase(), schema.getName(null));
					} else {
						String other_schema = (String)hm_all_names.get(name.toLowerCase());
						System.out.println("duplicate name " + name + " in schema " + schema.getName(null) + " (procedure), and in schema: " + other_schema);
						if (flag_skip_conflicts) continue;
					}
				}        
        printAlgorithm(pw, procedure, schema);
			}
		}

	}



/*
	void printSchema(SdaiModel model, PrintWriter pw) throws SdaiException {
			
		AInterface_specification specifications = (AInterface_specification)model.getInstances(CInterface_specification.class);
    EInterface_specification[] specifications_set = new EInterface_specification[specifications.getMemberCount()];
		SdaiIterator iterator = specifications.createIterator();
		int iSSetCount = 0;
		while (iterator.next()) {
      EInterface_specification specification = (EInterface_specification) specifications.getCurrentMemberObject(iterator);
      specifications_set[iSSetCount]=specification;
      iSSetCount++;
		}
    Arrays.sort(specifications_set, new SpecSorterBySchema());

    for (int i=0; i<iSSetCount; i++) { 
	    EInterface_specification specification = (EInterface_specification)specifications_set[i];
      ESchema_definition interfaced_schema = (ESchema_definition)specification.getForeign_schema(null);
			String name = interfaced_schema.getName(null);
			name = name.substring(0, name.length()-4) + "_mim";
			if (specification instanceof EUse_from_specification) {
				pw.println("\tUSE FROM " + name + ";");
			} else {
				pw.println("\tREFERENCE FROM " + name + ";");
			}
		}
		
	}
*/

	//  methods for generating express entity


    //prints constant
	private String printConstant(EConstant_definition constant, ESchema_definition schema) throws SdaiException {
		String str_constant = "";
		String 	partHeader = "",
                partExpress = "",
                partClass = "",
                partComments = "";
    //Express for constant
		partExpress += printExpressForConstant(constant);
		partExpress += printBreak();
    //Function for constant
//		partComments += printDocumentationForEntity(constant);
		if (!partComments.equals("")) {
//			partComments += printBreak();
		}
        
//        pw.println(partExpress);
			str_constant = partExpress;
//		pw.print(partComments);
		return str_constant;
	}


    private String printConstant(EConstant_definition constant, SdaiRepository repo) throws SdaiException {
			String result = "";
      String constant_body = "";
        
//        result = println("CONSTANT ");
//        result += "\t" + printBold(constant.getName(null).toLowerCase());
        result += "\t" + printBold(constant.getName(null));
        if (constant.testDomain(null)) {
            result += print(" : "+printType(constant.getDomain(null)));
        }
        
				SdaiModel express_model = findExpressModel(repo, constant);
        AExpress_code codes = (AExpress_code)express_model.getInstances(EExpress_code.class);
        SdaiIterator iter_code = codes.createIterator();
        while (iter_code.next()) {
	        EExpress_code code = codes.getCurrentMember(iter_code);
					EEntity target = code.getTarget(null);
					if (target == constant) {
            A_string expressions = code.getValues(null);
            SdaiIterator iter_ecv = expressions.createIterator();
            while (iter_ecv.next()) {
	          	String expr = (String)expressions.getCurrentMember(iter_ecv);  
							if (flag_long_form) {
								current_schema = constant.findEntityInstanceSdaiModel().getName();
								current_schema = current_schema.substring(0, current_schema.lastIndexOf("_DICTIONARY_DATA"));
								expr = replaceAll(expr, current_schema.toUpperCase(),  express_long_schema.toUpperCase());
								expr = replaceAll(expr, current_schema.toLowerCase(),  express_long_schema.toLowerCase());
							}				
	            constant_body += format(expr);
            }
						break;
					}
				}         
        
        if (!constant_body.equals("")) {
            result += print(" := "+constant_body);
        }
        result += println(";\n");
//        result += println(";");
//        result += println("END_CONSTANT; -- " + constant.getName(null));
        
		return result;
	}

    private String printExpressForConstant(EConstant_definition constant) throws SdaiException {
		String result = "";
        String constant_body = "";
        
//        result = println("CONSTANT ");
//        result += "\t" + printBold(constant.getName(null).toLowerCase());
        result += "\t" + printBold(constant.getName(null));
        if (constant.testDomain(null)) {
            result += print(" : "+printType(constant.getDomain(null)));
        }
        AEntity aeUsers = new AEntity();
        constant.findEntityInstanceUsers(asmExpressDomain, aeUsers);
        SdaiIterator iter_users=aeUsers.createIterator();
        while (iter_users.next()) {
            EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
            if (eUser instanceof EExpress_code) {
                EExpress_code ec = (EExpress_code) eUser;
                A_string asECValues = ec.getValues(null);
                SdaiIterator iter_ecv=asECValues.createIterator();
                while (iter_ecv.next()) {
	           			String expr = (String)asECValues.getCurrentMember(iter_ecv);  
									if (flag_long_form) {
										expr = replaceAll(expr, current_schema.toUpperCase(),  express_long_schema.toUpperCase());
										expr = replaceAll(expr, current_schema.toLowerCase(),  express_long_schema.toLowerCase());
									}				
	                
	                if (flag_format_expressions) {
	                	constant_body += format(expr);
	                } else {
	                	constant_body += expr;
	              	}
                }
            }
        }
        if (!constant_body.equals("")) {
            result += print(" := "+constant_body);
        }
        result += println(";\n");
//        result += println("END_CONSTANT; -- " + constant.getName(null));
        
		return result;
	}




//	private void printDefinedType(PrintWriter pw, EDefined_type type, SdaiRepository repo) throws SdaiException {
	private void printDefinedTypeLF(PrintWriter pw, EDefined_type type, SdaiRepository repo, EEntity_definition [] entity_definitions, int entity_count, EDefined_type [] defined_types, int type_count) throws SdaiException {

    asmExpressDomain.clear();
    SdaiModel express_model = findExpressModel(repo, type);
    if (express_model != null) {
    	asmExpressDomain.addUnordered(express_model, null);
		}
		current_schema = type.findEntityInstanceSdaiModel().getName();
		current_schema = current_schema.substring(0, current_schema.lastIndexOf("_DICTIONARY_DATA"));
		printDefinedTypeLF(pw, type, false, entity_definitions, entity_count, defined_types, type_count);
	}

//prints defined type in one file with two parts: Express, Interface
	private void printDefinedTypeLF(PrintWriter pw, EDefined_type type, boolean haveAggregate, EEntity_definition [] entity_definitions, int entity_count, EDefined_type [] defined_types, int type_count) throws SdaiException {
		String schemaName = findSchemaForEntity(type).getName(null);
		String 	partExpress = "",
					partComments = "",
					partInterface = "",
					partAggregate = "",
					partBasedOnSelects = "",
					partUsers = "",
					partSubtypes = "",
					partRules = "";
					
//partComments
//		String doc = findDocFor(type);
//		if (!doc.equals("")) {
//			partComments += doc;
//			partComments += printBreak();
//		}
//part Express
//		partExpress += print("TYPE " + printBold(type.getName(null).toLowerCase()) + " = ");
		partExpress += print("TYPE " + printBold(type.getName(null)) + " = ");
        EEntity domain = type.getDomain(null);
        if (domain instanceof ESelect_type || domain instanceof EEnumeration_type) {
            partExpress += printTypeLF(domain, entity_definitions, entity_count, defined_types, type_count);
        }
        else {
            partExpress += printType(domain)+";";
            partExpress += println();
        }
        partExpress += printWhere_rules(type);  //--VV--030617--Added where rules for defined type--
//		partExpress += println("END_TYPE; -- " + type.getName(null).toLowerCase());
		if (flag_end_comment) {
			partExpress += println("END_TYPE; -- " + type.getName(null));
	  } else {
			partExpress += println("END_TYPE;");
	  }
//		partExpress += printBreak();

//part Interface for selected and Enumeration types
		if (domain instanceof EEnumeration_type) {
			A_string ant = ((EEnumeration_type) domain).getElements(null);
			String s = "";
			boolean first = true;
			int i = 0;
			for (i = 1; i <= ant.getMemberCount(); i++) {
				if (first) {
					s += "\""+ant.getByIndex(i).toUpperCase()+"\"";
					first = false;
				} else {
					s += ", \""+ant.getByIndex(i).toUpperCase()+"\"";
				}
			}
		}
		else if (!(domain instanceof ESelect_type)) {
		} else {
//			partBasedOnSelects += printBasedOnSelects(type, domain, schemas);
		}
		if (haveAggregate) {
		}
		pw.println(partExpress);
//		pw.print(partComments);
//		pw.print(partBasedOnSelects);
//		pw.print(partUsers);
//		pw.print(partSubtypes);
//		pw.print(partRules);
	}


//prints defined type in one file with two parts: Express, Interface
	private void printDefinedType(PrintWriter pw, EDefined_type type, boolean haveAggregate) throws SdaiException {
		String schemaName = findSchemaForEntity(type).getName(null);
		String 	partExpress = "",
					partComments = "",
					partInterface = "",
					partAggregate = "",
					partBasedOnSelects = "",
					partUsers = "",
					partSubtypes = "",
					partRules = "";
					
//partComments
//		String doc = findDocFor(type);
//		if (!doc.equals("")) {
//			partComments += doc;
//			partComments += printBreak();
//		}
//part Express
//		partExpress += print("TYPE " + printBold(type.getName(null).toLowerCase()) + " = ");
		partExpress += print("TYPE " + printBold(type.getName(null)) + " = ");
        EEntity domain = type.getDomain(null);
        if (domain instanceof ESelect_type || domain instanceof EEnumeration_type) {
            partExpress += printType(domain);
        }
        else {
            partExpress += printType(domain)+";";
            partExpress += println();
        }
        partExpress += printWhere_rules(type);  //--VV--030617--Added where rules for defined type--
//		partExpress += println("END_TYPE; -- " + type.getName(null).toLowerCase());
		if (flag_end_comment) {
			partExpress += println("END_TYPE; -- " + type.getName(null));
		} else {
			partExpress += println("END_TYPE;");
		}
//		partExpress += printBreak();

//part Interface for selected and Enumeration types
		if (domain instanceof EEnumeration_type) {
			A_string ant = ((EEnumeration_type) domain).getElements(null);
			String s = "";
			boolean first = true;
			int i = 0;
			for (i = 1; i <= ant.getMemberCount(); i++) {
				if (first) {
					s += "\""+ant.getByIndex(i).toUpperCase()+"\"";
					first = false;
				} else {
					s += ", \""+ant.getByIndex(i).toUpperCase()+"\"";
				}
			}
		}
		else if (!(domain instanceof ESelect_type)) {
		} else {
//			partBasedOnSelects += printBasedOnSelects(type, domain, schemas);
		}
		if (haveAggregate) {
		}
		pw.println(partExpress);
//		pw.print(partComments);
//		pw.print(partBasedOnSelects);
//		pw.print(partUsers);
//		pw.print(partSubtypes);
//		pw.print(partRules);
	}

	
	
	private void printEntity(PrintWriter pw, EEntity_definition entity, SdaiRepository repo, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException {
    asmExpressDomain.clear();
    SdaiModel express_model = findExpressModel(repo, entity);
    if (express_model != null) {
    	asmExpressDomain.addUnordered(express_model, null);
		}
		current_schema = entity.findEntityInstanceSdaiModel().getName();
		current_schema = current_schema.substring(0, current_schema.lastIndexOf("_DICTIONARY_DATA"));
		printEntityLF(pw, entity, (ESchema_definition)null, entity_definitions, entity_count);
	}
	

//prints entity with parts: Express, Comments, Partial, Subtypes, Iterface, Class, Aggregate
	private void printEntityLF(PrintWriter pw, EEntity_definition entity, ESchema_definition schema, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException {

// System.out.println("entity: " + entity + ", schema: " + schema);

		String 	partExpress = "",
					partComments = "",
					partPartial = "",
					partInterface = "",
					partClass = "",
					partAggregate = "",
					partSubtypes = "",
					partConstraints = "",
					partHeader = "",
					partRules = "",
					partUsers = "";
		partExpress += printExpressForEntityLF(entity, entity_definitions, entity_count);
//		partComments += printDocumentationForEntityDefinition(entity);

// go through the attributes
//RR		AExplicit_attribute attributes = entity.getExplicit_attributes(null);
		AExplicit_attribute attributes = getExplicit_attributes(entity);
		SdaiIterator attributes_it = attributes.createIterator();
		while (attributes_it.next()) {
			EExplicit_attribute attribute = attributes.getCurrentMember(attributes_it);
//commnets for attribute
//			partComments += printDocumentationForAttribute(attribute);
//express for attribute
			partExpress += printExpressForAttribute(attribute);
//			collectAggregates(attribute.getDomain(null), 0);
		}
//finding derived, inverse and explicit_redeclared attributes
		AAttribute all_attributes = entity.getAttributes(null, null);
		SdaiIterator iter_all = all_attributes.createIterator();
// 		Vector derives = new Vector();
// 		Vector inverses = new Vector();
// 		Vector redeclared = new Vector();
		TreeSet derives = new TreeSet(new SorterForAttributes());
		TreeSet inverses = new TreeSet(new SorterForAttributes());
		TreeSet redeclared = new TreeSet(new SorterForAttributes());
		while (iter_all.next()) {
			EAttribute attribute = all_attributes.getCurrentMember(iter_all);
			if (attribute instanceof EDerived_attribute) {
			    derives.add(attribute);
// 				addSorted(derives, attribute);
			} else if (attribute instanceof EInverse_attribute) {
			    inverses.add(attribute);
// 				addSorted(inverses, attribute);
			} else if (attribute instanceof EExplicit_attribute) {
				EExplicit_attribute ea = (EExplicit_attribute)attribute;
				if (ea.testRedeclaring(null)) {
				    redeclared.add(attribute);
// 					addSorted(redeclared, attribute);
				}
			}
		}
//printing for explicit redeclared attribtues
		for (Iterator i = redeclared.iterator(); i.hasNext(); ) {
		    Object element = i.next();
			partExpress += printExpressForAttribute((EAttribute)element);
//			collectAggregates(((EExplicit_attribute)element).getDomain(null), 0);
		}
//printing for derived attributes
		if (derives.size() > 0) {
			//partExpress += println();
			partExpress += println("DERIVE");
			for (Iterator i = derives.iterator(); i.hasNext(); ) {
				EDerived_attribute attribute = (EDerived_attribute)i.next();
//				collectAggregates(attribute.getDomain(null), 0);
//commnets for attribute
//				partComments += printDocumentationForAttribute(attribute);
//express for attribute
				partExpress += printExpressForAttribute(attribute);
			}
		}
//printing for inverse attributes
		if (inverses.size() > 0) {
			//partExpress += println();
//RR			partExpress += print("INVERSE");
			partExpress += print("INVERSE\n");
			for (Iterator i = inverses.iterator(); i.hasNext(); ) {
				EInverse_attribute attribute = (EInverse_attribute)i.next();
//				collectAggregates(attribute.getDomain(null), 0);
				EEntity inv_domain = attribute.getInverted_attr(null).getDomain(null);
				EEntity_definition domain = attribute.getDomain(null);
//commnets for attribute
//				partComments += printDocumentationForAttribute(attribute);
//express fro attribute
				String temp_inverse = ((testRedeclaring(attribute))?"SELF\\"
					+getRedeclaring(attribute).getParent(null).getName(null)+".":"")
//					+attribute.getName(null).toLowerCase()+": ";
					+attribute.getName(null)+": ";
				if (attribute.testMin_cardinality(null)) {
					temp_inverse += ((attribute.getDuplicates(null))?"BAG [":"SET [")
					+printBound(attribute.getMin_cardinality(null))+":"
					+printBound((attribute.testMax_cardinality(null))?attribute.getMax_cardinality(null):null)+"] OF ";
				}
				temp_inverse += printType(domain)+" FOR "+ attribute.getInverted_attr(null).getName(null) + ";";  // toLowerCase() removed
				partExpress += printTab(temp_inverse);
			}
		}
		//partExpress += println();
		partExpress += printUniqueness_rules(entity);
		partExpress += printWhere_rules(entity);
		if (flag_end_comment) {
			partExpress += println("END_ENTITY; -- " + entity.getName(null));
		} else {
			partExpress += println("END_ENTITY;");
		}
		partExpress += printBreak();
//		if (!partComments.equals("")) {
//			partComments += printBreak();
//		}

		if (!entity.getComplex(null)) {
//			pw.print(partExpress);
		} else {
//			String partComplexExpress = "";
//			partComplexExpress += println("Multi leaf complex entity data type");
//			String devidedComplex = "";
//			String names[] = devideComplexName(entity.getName(null));
//			boolean first = true;
//			for (int i = 0; i < names.length; i++) {
//				String tmp = "";
//				if (first)
//					first = false;
//				else
//					tmp = "+";
//				devidedComplex += tmp+printHRef(names[i], getSchemaNameIfDiffer(findInPartial(entity, names[i]), schema)+getUpper(names[i])+".html");
//			}
//			partComplexExpress += printTab(printBold(devidedComplex));
//			partComplexExpress += printBreak();
//			pw.print(partComplexExpress);
		}
//		pw.print(partComments);

//		pw.print(partPartial);
//		pw.print(partSubtypes);
//		pw.print(partConstraints);
//		pw.print(partUsers);
//		pw.print(partRules);

		pw.println(partExpress);
	}
	
	
//prints entity with parts: Express, Comments, Partial, Subtypes, Iterface, Class, Aggregate
	private void printEntity(PrintWriter pw, EEntity_definition entity, ESchema_definition schema) throws SdaiException {


		String 	partExpress = "",
					partComments = "",
					partPartial = "",
					partInterface = "",
					partClass = "",
					partAggregate = "",
					partSubtypes = "",
					partConstraints = "",
					partHeader = "",
					partRules = "",
					partUsers = "";
		partExpress += printExpressForEntity(entity);
//		partComments += printDocumentationForEntityDefinition(entity);

// go through the attributes
//RR		AExplicit_attribute attributes = entity.getExplicit_attributes(null);
		AExplicit_attribute attributes = getExplicit_attributes(entity);
		SdaiIterator attributes_it = attributes.createIterator();
		while (attributes_it.next()) {
			EExplicit_attribute attribute = attributes.getCurrentMember(attributes_it);
//commnets for attribute
//			partComments += printDocumentationForAttribute(attribute);
//express for attribute
			partExpress += printExpressForAttribute(attribute);
//			collectAggregates(attribute.getDomain(null), 0);
		}
//finding derived, inverse and explicit_redeclared attributes
		AAttribute all_attributes = entity.getAttributes(null, null);
		SdaiIterator iter_all = all_attributes.createIterator();
// 		Vector derives = new Vector();
// 		Vector inverses = new Vector();
// 		Vector redeclared = new Vector();
		TreeSet derives = new TreeSet(new SorterForAttributes());
		TreeSet inverses = new TreeSet(new SorterForAttributes());
		TreeSet redeclared = new TreeSet(new SorterForAttributes());
		while (iter_all.next()) {
			EAttribute attribute = all_attributes.getCurrentMember(iter_all);
			if (attribute instanceof EDerived_attribute) {
			    derives.add(attribute);
// 				addSorted(derives, attribute);
			} else if (attribute instanceof EInverse_attribute) {
			    inverses.add(attribute);
// 				addSorted(inverses, attribute);
			} else if (attribute instanceof EExplicit_attribute) {
				EExplicit_attribute ea = (EExplicit_attribute)attribute;
				if (ea.testRedeclaring(null)) {
				    redeclared.add(attribute);
// 					addSorted(redeclared, attribute);
				}
			}
		}
//printing for explicit redeclared attribtues
		for (Iterator i = redeclared.iterator(); i.hasNext(); ) {
		    Object element = i.next();
			partExpress += printExpressForAttribute((EAttribute)element);
//			collectAggregates(((EExplicit_attribute)element).getDomain(null), 0);
		}
//printing for derived attributes
		if (derives.size() > 0) {
			//partExpress += println();
			partExpress += println("DERIVE");
			for (Iterator i = derives.iterator(); i.hasNext(); ) {
				EDerived_attribute attribute = (EDerived_attribute)i.next();
//				collectAggregates(attribute.getDomain(null), 0);
//commnets for attribute
//				partComments += printDocumentationForAttribute(attribute);
//express for attribute
				partExpress += printExpressForAttribute(attribute);
			}
		}
//printing for inverse attributes
		if (inverses.size() > 0) {
			//partExpress += println();
//RR			partExpress += print("INVERSE");
			partExpress += print("INVERSE\n");
			for (Iterator i = inverses.iterator(); i.hasNext(); ) {
				EInverse_attribute attribute = (EInverse_attribute)i.next();
//				collectAggregates(attribute.getDomain(null), 0);
				EEntity inv_domain = attribute.getInverted_attr(null).getDomain(null);
				EEntity_definition domain = attribute.getDomain(null);
//commnets for attribute
//				partComments += printDocumentationForAttribute(attribute);
//express fro attribute
				String temp_inverse = ((testRedeclaring(attribute))?"SELF\\"
					+getRedeclaring(attribute).getParent(null).getName(null)+".":"")
//					+attribute.getName(null).toLowerCase()+": ";
					+attribute.getName(null)+": ";
				if (attribute.testMin_cardinality(null)) {
					temp_inverse += ((attribute.getDuplicates(null))?"BAG [":"SET [")
					+printBound(attribute.getMin_cardinality(null))+":"
					+printBound((attribute.testMax_cardinality(null))?attribute.getMax_cardinality(null):null)+"] OF ";
				}
				temp_inverse += printType(domain)+" FOR "+ attribute.getInverted_attr(null).getName(null) + ";";  // toLowerCase() removed
				partExpress += printTab(temp_inverse);
			}
		}
		//partExpress += println();
		partExpress += printUniqueness_rules(entity);
		partExpress += printWhere_rules(entity);
		if (flag_end_comment) {
			partExpress += println("END_ENTITY; -- " + entity.getName(null));
		} else {
			partExpress += println("END_ENTITY;");
		}		

		partExpress += printBreak();
//		if (!partComments.equals("")) {
//			partComments += printBreak();
//		}

		if (!entity.getComplex(null)) {
//			pw.print(partExpress);
		} else {
//			String partComplexExpress = "";
//			partComplexExpress += println("Multi leaf complex entity data type");
//			String devidedComplex = "";
//			String names[] = devideComplexName(entity.getName(null));
//			boolean first = true;
//			for (int i = 0; i < names.length; i++) {
//				String tmp = "";
//				if (first)
//					first = false;
//				else
//					tmp = "+";
//				devidedComplex += tmp+printHRef(names[i], getSchemaNameIfDiffer(findInPartial(entity, names[i]), schema)+getUpper(names[i])+".html");
//			}
//			partComplexExpress += printTab(printBold(devidedComplex));
//			partComplexExpress += printBreak();
//			pw.print(partComplexExpress);
		}
//		pw.print(partComments);

//		pw.print(partPartial);
//		pw.print(partSubtypes);
//		pw.print(partConstraints);
//		pw.print(partUsers);
//		pw.print(partRules);

		pw.println(partExpress);
	}


	// with pruning for long forms
	private String printExpressForEntityLF(EEntity_definition definition, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException {
		String result = "";
		boolean haveSubOrSup = false;
		boolean hasSupertypes = false;
		boolean hasSubtypes = false;
//subtype
		AEntity supertypes = definition.getGeneric_supertypes(null);
		if (supertypes.getMemberCount() > 0) {
			haveSubOrSup = true;
			hasSupertypes = true;
			SdaiIterator iter1 = supertypes.createIterator();
			int count = supertypes.getMemberCount();
			boolean first = true;
			String partSub = "";
			while (iter1.next()) {
				EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(iter1);
				if (first) {
					first = false;
				} else {
					partSub += ", ";
				}
//				partSub += printHRef(supertype.getName(null).toLowerCase(), getSchemaNameIfDiffer(supertype, schema)+getUpper(supertype.getName(null))+".html");
				partSub += supertype.getName(null); // toLowerCase() removed
			}
			result += printTab("SUBTYPE OF ("+partSub+");");
		}
/*		else {
			if (!definition.getInstantiable(null)) {
				result += printTab("SUPERTYPE");
			}
		}*/
//supertype
//		ASubtype_constraint subtypes = new ASubtype_constraint();
		ASub_supertype_constraint subtypes = new ASub_supertype_constraint();

// System.out.println("### before usedin - definition: " + definition + ", schemas: " + schemas);
//		CSubtype_constraint.usedinSuper_type(null, definition, schemas, subtypes);
		CSub_supertype_constraint.usedinGeneric_supertype(null, definition, schemas, subtypes);
		String partSuper = "";
		if (subtypes.getMemberCount() > 0) {
			SdaiIterator it_subtypes = subtypes.createIterator();
			while (it_subtypes.next()) {
        if(subtypes.getCurrentMember(it_subtypes).testConstraint(null)) {
	        if(subtypes.getCurrentMember(it_subtypes).testName(null)) {
	        	// this is a stand-alone subtype_constraint, should not be included into the entity itself
//	        	System.out.println("stand-alone subtype_constraint: " + subtypes.getCurrentMember(it_subtypes));
	        } else {
//	        	System.out.println("in-entity subtype_constraint: " + subtypes.getCurrentMember(it_subtypes));
						String partSuperX = printSubtypeConstraintLF(subtypes.getCurrentMember(it_subtypes).getConstraint(null), entity_definitions, entity_count, definition);
						if (!partSuperX.equals("")) {
							hasSubtypes = true;
							haveSubOrSup = true;
							partSuper += partSuperX;
						}
					}
				} else {
//System.out.println("XD: problem - constraint attribute unset: " +  subtypes.getCurrentMember(it_subtypes));
				}
			}
			


//			result = printTab((definition.getInstantiable(null)?"":"ABSTRACT ")+"SUPERTYPE OF ("+partSuper+")"+((haveSubOrSup)?"":";"))+result;
		} 


// RR
			/*
				possible cases:
			  1)  has subtype_constraint and supertypes, generate        SUPERTYPE OF () SUBTYPE OF ();
				2)  has subtype_constraint but no supertypes, generate     SUPERTYPE OF ();
				3)  has no subtype_constraint but has supertypes, generate SUBTYPE OF ();
				4)  has no subtype_constraint and no supertype_constraint, generate nothing
			*/

		
		
	  // result = printTab((definition.getInstantiable(null)?"":"ABSTRACT ")+"SUPERTYPE OF ("+partSuper+")"+((haveSubOrSup)?"":";"))+result;
		
		String temp_result = "";
		if (hasSubtypes) {
			haveSubOrSup = true;
			if (!(definition.getInstantiable(null))) {
				temp_result += "ABSTRACT ";
			}
			temp_result += "SUPERTYPE OF (" + partSuper + ")";
			if (!hasSupertypes) {
				temp_result += ";";
			}
			result = printTab(temp_result) + result;
		} else {
			// may be abstract supertype
			if (!definition.getInstantiable(null)) {
				if (definition.testAbstract_entity(null)) {  // mandatory but early implementations left it unset in the dictionary
					if (definition.getAbstract_entity(null)) {
						temp_result = "ABSTRACT";
					} else {
						temp_result = "ABSTRACT SUPERTYPE";
					}
				} else {
					temp_result = "ABSTRACT SUPERTYPE";
				}
//				temp_result = "ABSTRACT SUPERTYPE";
				haveSubOrSup = true;
				if (!hasSupertypes) {
					temp_result += ";";
				}
				result = printTab(temp_result) + result;
			}
		}
	
		
// end-RR		
		
		// added by RR begins - no longer needed, see above
//		else 
//		if (!definition.getInstantiable(null)) {
//			result = printTab("ABSTRACT SUPERTYPE;") + result;
//		}
		// added by RR ends
		
//		result = println("ENTITY " + printBold(definition.getName(null).toLowerCase())+((haveSubOrSup)?"":";"))+result;
		result = println("ENTITY " + printBold(definition.getName(null))+((haveSubOrSup)?"":";"))+result;
		return result;
	}

	private String printExpressForEntity(EEntity_definition definition) throws SdaiException {
		String result = "";
		boolean haveSubOrSup = false;
		boolean hasSupertypes = false;
		boolean hasSubtypes = false;
//subtype
		AEntity supertypes = definition.getGeneric_supertypes(null);
		if (supertypes.getMemberCount() > 0) {
			haveSubOrSup = true;
			hasSupertypes = true;
			SdaiIterator iter1 = supertypes.createIterator();
			int count = supertypes.getMemberCount();
			boolean first = true;
			String partSub = "";
			while (iter1.next()) {
				EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(iter1);
				if (first) {
					first = false;
				} else {
					partSub += ", ";
				}
//				partSub += printHRef(supertype.getName(null).toLowerCase(), getSchemaNameIfDiffer(supertype, schema)+getUpper(supertype.getName(null))+".html");
				partSub += supertype.getName(null); // toLowerCase() removed
			}
			result += printTab("SUBTYPE OF ("+partSub+");");
		}
/*		else {
			if (!definition.getInstantiable(null)) {
				result += printTab("SUPERTYPE");
			}
		}*/
//supertype
//		ASubtype_constraint subtypes = new ASubtype_constraint();
		ASub_supertype_constraint subtypes = new ASub_supertype_constraint();

// System.out.println("### before usedin - definition: " + definition + ", schemas: " + schemas);
//		CSubtype_constraint.usedinSuper_type(null, definition, schemas, subtypes);
		CSub_supertype_constraint.usedinGeneric_supertype(null, definition, schemas, subtypes);
		String partSuper = "";
		if (subtypes.getMemberCount() > 0) {
			SdaiIterator it_subtypes = subtypes.createIterator();
			while (it_subtypes.next()) {
        if(subtypes.getCurrentMember(it_subtypes).testConstraint(null)) {
	        if(subtypes.getCurrentMember(it_subtypes).testName(null)) {
	        	// this is a stand-alone subtype_constraint, should not be included into the entity itself
//	        	System.out.println("stand-alone subtype_constraint: " + subtypes.getCurrentMember(it_subtypes));
	        } else {
//	        	System.out.println("in-entity subtype_constraint: " + subtypes.getCurrentMember(it_subtypes));
						partSuper += printSubtypeConstraint(subtypes.getCurrentMember(it_subtypes).getConstraint(null));
						hasSubtypes = true;
						haveSubOrSup = true;
					}
				} else {
//System.out.println("XD: problem - constraint attribute unset: " +  subtypes.getCurrentMember(it_subtypes));
				}
			}
			


//			result = printTab((definition.getInstantiable(null)?"":"ABSTRACT ")+"SUPERTYPE OF ("+partSuper+")"+((haveSubOrSup)?"":";"))+result;
		} 


// RR
			/*
				possible cases:
			  1)  has subtype_constraint and supertypes, generate        SUPERTYPE OF () SUBTYPE OF ();
				2)  has subtype_constraint but no supertypes, generate     SUPERTYPE OF ();
				3)  has no subtype_constraint but has supertypes, generate SUBTYPE OF ();
				4)  has no subtype_constraint and no supertype_constraint, generate nothing
			*/

		
		
	  // result = printTab((definition.getInstantiable(null)?"":"ABSTRACT ")+"SUPERTYPE OF ("+partSuper+")"+((haveSubOrSup)?"":";"))+result;
		
		String temp_result = "";
		if (hasSubtypes) {
			haveSubOrSup = true;
			if (!(definition.getInstantiable(null))) {
				temp_result += "ABSTRACT ";
			}
			temp_result += "SUPERTYPE OF (" + partSuper + ")";
			if (!hasSupertypes) {
				temp_result += ";";
			}
			result = printTab(temp_result) + result;
		} else {
			// may be abstract supertype
			if (!definition.getInstantiable(null)) {
				if (definition.testAbstract_entity(null)) {  // mandatory but early implementations left it unset in the dictionary
					if (definition.getAbstract_entity(null)) {
						temp_result = "ABSTRACT";
					} else {
						temp_result = "ABSTRACT SUPERTYPE";
					}
				} else {
					temp_result = "ABSTRACT SUPERTYPE";
				}
		
		
//				temp_result = "ABSTRACT SUPERTYPE";
				haveSubOrSup = true;
				if (!hasSupertypes) {
					temp_result += ";";
				}
				result = printTab(temp_result) + result;
			}
		}
	
		
// end-RR		
		
		// added by RR begins - no longer needed, see above
//		else 
//		if (!definition.getInstantiable(null)) {
//			result = printTab("ABSTRACT SUPERTYPE;") + result;
//		}
		// added by RR ends
		
//		result = println("ENTITY " + printBold(definition.getName(null).toLowerCase())+((haveSubOrSup)?"":";"))+result;
		result = println("ENTITY " + printBold(definition.getName(null))+((haveSubOrSup)?"":";"))+result;
		return result;
	}
	

//printing where rules
	private String printWhere_rules(EEntity entity) throws SdaiException {
		String result = "";
        
        // RR - adding the second null parameter - domain. Have to investigate, null may be wrong. 
		// the first impression - current model is OK, where rules of this entity only.
        if (entity instanceof ENamed_type || entity instanceof EGlobal_rule) {
            AWhere_rule wheres=new AWhere_rule();
            if (entity instanceof ENamed_type) {
                ENamed_type nt = (ENamed_type)entity;
                wheres = nt.getWhere_rules(null, null);
            }
            if (entity instanceof EGlobal_rule) {
                EGlobal_rule gr = (EGlobal_rule)entity;
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
								while (iter_swr.hasNext())	{
                    EWhere_rule where = (EWhere_rule)iter_swr.next();
                    //result += (where.testLabel(null)?printTab(where.getLabel(null)+" : "):"");
                    result += "\t"+(where.testLabel(null)?where.getLabel(null)+" : ":"");
                    AEntity aeUsers = new AEntity();
                    where.findEntityInstanceUsers( asmExpressDomain, aeUsers);
                    SdaiIterator iter_users=aeUsers.createIterator();
                    while (iter_users.next()) {
                        EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
                        if (eUser instanceof EExpress_code) {
                            EExpress_code ec = (EExpress_code) eUser;

// System.out.println("<WR>: " + ec); 
                            A_string asECValues = ec.getValues(null);
                            SdaiIterator iter_ecv=asECValues.createIterator();
                            while (iter_ecv.next()) {
                        			String expr = (String)asECValues.getCurrentMember(iter_ecv);  
//													      result += asECValues.getCurrentMember(iter_ecv);
															if (flag_long_form) {
																expr = replaceAll(expr, current_schema.toUpperCase(),  express_long_schema.toUpperCase());
																expr = replaceAll(expr, current_schema.toLowerCase(),  express_long_schema.toLowerCase());
															}				
															
															if (flag_format_expressions) {
																result += format(expr);
															} else {
																result += expr;
															}
//                                result += format(asECValues.getCurrentMember(iter_ecv));
                            }
                        }
                    }
                    result += ";\n";
                }
                //result += println();
            }
        }
		return result;
	}



//prints whatever express type, whitch exist in dictionary
//	private String printType(EEntity type) throws SdaiException {
	private String printTypeLF(EEntity type, EEntity_definition [] entity_definitions, int entity_count, EDefined_type [] defined_types, int type_count) throws SdaiException {
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
            else {  // --VV--
                partType += print("AGGREGATE");
            }
            if (bound1!=null || bound2!=null) {
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
			partType += ((ENamed_type)type).getName(null); // toLowerCase() removed
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
/*
			extensible_select_type - EXTENSIBLE [GENERIC_ENTITY] SELECT;  - may be without list
			                       - EXTENSIBLE [GENERIC_ENTITY] SELECT (x,y); 
			non_extensible_select_type - SELECT (x,y);
			entity_select_type - GENERIC_ENTITY
			extended_select_type - [EXTENSIBLE [GENERIC_ENTITY] ] SELECT BASED_ON xxx WITH (x,y);
			                     - [EXTENSIBLE [GENERIC_ENTITY] ] SELECT BASED_ON xxx;  - may be without WITH 

			optional attribute local_selections can tell us if  (x,y) part is present or not.
			                     
*/			                     
			if (type instanceof EExtensible_select_type) {
				partType += print("EXTENSIBLE ");
			}
			if (type instanceof EEntity_select_type) {
				partType += print("GENERIC_ENTITY ");
			}
			partType += print("SELECT ");
			if (type instanceof EExtended_select_type) {
				// probably need to generate reference, not just the name?
//				EExtensible_select_type based_on = ((EExtended_select_type)type).getIs_based_on(null);
//        EDefined_type def = null;

        EDefined_type def = ((EExtended_select_type)type).getIs_based_on(null);
 

/*
			// no longer needed


				ADefined_type defs = new ADefined_type();
				SdaiIterator it_defs;
				CDefined_type.usedinDomain(null, based_on, schemas, defs);
//if (((EData_type)type).getName(null).equals("_SELECT_c_document_reference_item'")) {
//	System.out.println("type: " + ((EData_type)type).getName(null));
//	System.out.println("based_on: " + based_on.getName(null));
//	System.out.println("schemas: " + schemas);
//	System.out.println("result: " + defs);
//}
				it_defs = defs.createIterator();
				while (it_defs.next()) {
					def = (EDefined_type)defs.getCurrentMemberObject(it_defs);
          // should be one only, but if more than one - which to take, anyway? Unless print internal error message
          if (def != null) break;
  			}

*/


				partType += print("BASED_ON ");
				if (def != null) {
					partType  += def.getName(null); // toLowerCase() removed
				} else {
					System.out.println("ERROR: BASED_ON type not found for " + type); 
				}
			}
			if (((ESelect_type)type).testLocal_selections(null)) {
				String with_part = "";
			
				ANamed_type nts = ((ESelect_type)type).getLocal_selections(null);
				if (nts.getMemberCount() > 0) {
					if (type instanceof EExtended_select_type) {
						with_part += print(" WITH ");
					} 
//					partType += println("(");

					
					// why we are using non-local selections here?
					// also, perhaps it is a good idea to skip WITH part altogether, if there are 0 elements after pruning
					
//					ANamed_type ant = ((ESelect_type) type).getSelections(null);
					ANamed_type ant = nts;
					SdaiIterator it = ant.createIterator();
					int pruned_count = 0;
					String str_select_elements = "";
					while (it.next()) {
						ENamed_type nt = ant.getCurrentMember(it);
						boolean is_present = namedTypePresent(nt, entity_definitions, entity_count, defined_types, type_count, null);
						if (is_present) {
							// add this type to the select list
							if (pruned_count > 0) {
								str_select_elements += print(",");
							}
							str_select_elements += nt.getName(null); // toLowerCase() removed
							pruned_count++;
						} else {
							// prune this type
						}						
					} // while
					// if there are select elements added, print also WITH and parentheses
					// if there are no select elements, skip WITH with parentcheses in the case od extended, but
					// but if not extended, perhaps still print parentheses, because they are not opitional (even they are optional in my current grammar,
					// which also supports based_on_types
					if (pruned_count > 0) {
						partType += with_part;
						partType += println("(");
	          partType += str_select_elements;
	          partType += print(");");
					} else {
						if (type instanceof EExtensible_select_type) {
							// here we can skip the whole list with (possible) WITH section , just print semicolon
							// because the type is extensible, it may have no list, even if it happens after pruning					
		          partType += print(";");
						} else {
							// select is non-extensible, it should have something in the list, here we print an empty list and an error message:
							System.out.println("ERROR - empty non-extensible select, all select elements pruned: " + type); 
							partType += println("(");
		          partType += print(");");
						} 
					}
					
					
				
      	} else {
	      	// no select elements, even before pruning
	        partType += println(";");
					if (!(type instanceof EExtensible_select_type)) {
							System.out.println("ERROR - empty non-extensible select: " + type); 
      		}
      	}

			} else {
        partType += println(";");
			}
			
//			partType += println("SELECT (");
		}

		else if (type instanceof EEnumeration_type) {
			// RR: add support for extensible enumeration types?

			if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
				partType += print("EXTENSIBLE ENUMERATION BASED_ON ");
				EDefined_type based_on = ((EExtended_enumeration_type)type).getIs_based_on(null);
				partType += print(based_on.getName(null));
			} else
			if (type instanceof EExtensible_enumeration_type) {
				partType += print("EXTENSIBLE ENUMERATION");
			} else
			if (type instanceof EExtended_enumeration_type) {
				partType += print("ENUMERATION BASED_ON ");
				EDefined_type based_on = ((EExtended_enumeration_type)type).getIs_based_on(null);
				partType += print(based_on.getName(null));
			} else { // just a regular enumeration
				partType += print("ENUMERATION OF (");
			}

			A_string ant = ((EEnumeration_type) type).getElements(null);
			int iDim =  ant.getMemberCount();
			if (iDim >= 1) {

				if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
					partType += print(" WITH (");
				} else
				if (type instanceof EExtensible_enumeration_type) {
					partType += print(" OF (");
				} else
				if (type instanceof EExtended_enumeration_type) {
					partType += print(" WITH (");
				} else {
				}

				partType += println();
				int i = 1;
				while (i <= iDim) {
					String temp = "";
					temp += print(ant.getByIndex(i));
					if (i < iDim) {
						temp += print(",");
					} else {
						//temp += print(" )");
                        temp += print(" );");
					}
					i++;
					partType += printTab(temp);
				}
			} else {
				//partType += print(" )");
				if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
                partType += print(";\n");
				} else
				if (type instanceof EExtensible_enumeration_type) {
                partType += print(";\n"); // for regular enumeration with have println to handle new line
				} else
				if (type instanceof EExtended_enumeration_type) {
                partType += print(";\n");
				} else {
                partType += print(" );");
				}
			}
		}

    else if (type instanceof EData_type) {

	  	if (((EData_type)type).getName(null).equals("_ENTITY")) {
		 		partType += print("GENERIC_ENTITY");
    	} else 
      if (((EData_type)type).getName(null).equals("_GENERIC")) {
	    	partType += print("GENERIC");
			} else {
		  	partType += print("UNKNOWN_DATA_TYPE");
			}     
  
    } else {
	    partType += print("UNKNOWN_NON_DATA_TYPE");
   	}
		return partType;
	}

//prints whatever express type, whitch exist in dictionary
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
            else {  // --VV--
                partType += print("AGGREGATE");
  
            }
            if (bound1!=null || bound2!=null) {
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
			partType += ((ENamed_type)type).getName(null); // toLowerCase() removed
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
/*
			extensible_select_type - EXTENSIBLE [GENERIC_ENTITY] SELECT;  - may be without list
			                       - EXTENSIBLE [GENERIC_ENTITY] SELECT (x,y); 
			non_extensible_select_type - SELECT (x,y);
			entity_select_type - GENERIC_ENTITY
			extended_select_type - [EXTENSIBLE [GENERIC_ENTITY] ] SELECT BASED_ON xxx WITH (x,y);
			                     - [EXTENSIBLE [GENERIC_ENTITY] ] SELECT BASED_ON xxx;  - may be without WITH 

			optional attribute local_selections can tell us if  (x,y) part is present or not.
			                     
*/			                     
			if (type instanceof EExtensible_select_type) {
				partType += print("EXTENSIBLE ");
			}
			if (type instanceof EEntity_select_type) {
				partType += print("GENERIC_ENTITY ");
			}
			partType += print("SELECT ");
			if (type instanceof EExtended_select_type) {
				// probably need to generate reference, not just the name?
//				EExtensible_select_type based_on = ((EExtended_select_type)type).getIs_based_on(null);
//        EDefined_type def = null;

        EDefined_type def = ((EExtended_select_type)type).getIs_based_on(null);

/*
				no longer needed
	
				ADefined_type defs = new ADefined_type();
				SdaiIterator it_defs;
				CDefined_type.usedinDomain(null, based_on, schemas, defs);
//if (((EData_type)type).getName(null).equals("_SELECT_c_document_reference_item'")) {
//	System.out.println("type: " + ((EData_type)type).getName(null));
//	System.out.println("based_on: " + based_on.getName(null));
//	System.out.println("schemas: " + schemas);
//	System.out.println("result: " + defs);
//}
				it_defs = defs.createIterator();
				while (it_defs.next()) {
					def = (EDefined_type)defs.getCurrentMemberObject(it_defs);
          // should be one only, but if more than one - which to take, anyway? Unless print internal error message
          if (def != null) break;
  			}

*/


				partType += print("BASED_ON ");
				if (def != null) {
					partType  += def.getName(null); // toLowerCase() removed
				} else {
					System.out.println("ERROR: BASED_ON type not found for " + type); 
				}
			}
			
			
			if (((ESelect_type)type).testLocal_selections(null)) {
				ANamed_type nts = ((ESelect_type)type).getLocal_selections(null);
				if (nts.getMemberCount() > 0) {
					if (type instanceof EExtended_select_type) {
						partType += print(" WITH ");
					} 
					partType += println("(");

					
					// why selections used here instead of above nts local_selections?
					// if there actually are additional selections (non-local) they should not be printed in express
					// it is probably wrong, also in ExpressDoc ???


					ANamed_type ant = ((ESelect_type) type).getSelections(null);
					SdaiIterator it = ant.createIterator();
					if (it.next()) {
						boolean fGoOn = true;
						while (fGoOn) {
							String temp = "";
							ENamed_type nt = ant.getCurrentMember(it);
							temp += nt.getName(null); // toLowerCase() removed
							if (it.next()) {
								temp += print(",");
							} else {
								//temp += print(")");
               	temp += print(");");
								fGoOn = false;
							}
							partType += printTab(temp);
						}
					} else {
						//partType += print(")");
                partType += print(");");
					}
				
      	} else {
	        partType += println(";");
      	}

			} else {
        partType += println(";");
			}
			
//			partType += println("SELECT (");
		}
/*
		else if (type instanceof EEnumeration_type) {
			// RR: add support for extensible enumeration types?
			partType += print("ENUMERATION OF (");
			A_string ant = ((EEnumeration_type) type).getElements(null);
			int iDim =  ant.getMemberCount();
			if (iDim >= 1) {
				partType += println();
				int i = 1;
				while (i <= iDim) {
					String temp = "";
					temp += print(ant.getByIndex(i));
					if (i < iDim) {
						temp += print(",");
					} else {
						//temp += print(" )");
                        temp += print(" );");
					}
					i++;
					partType += printTab(temp);
				}
			} else {
				//partType += print(" )");
                partType += print(" );");
			}
		}
*/

		else if (type instanceof EEnumeration_type) {
			// RR: add support for extensible enumeration types?

			if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
				partType += print("EXTENSIBLE ENUMERATION BASED_ON ");
				EDefined_type based_on = ((EExtended_enumeration_type)type).getIs_based_on(null);
				partType += print(based_on.getName(null));
			} else
			if (type instanceof EExtensible_enumeration_type) {
				partType += print("EXTENSIBLE ENUMERATION");
			} else
			if (type instanceof EExtended_enumeration_type) {
				partType += print("ENUMERATION BASED_ON ");
				EDefined_type based_on = ((EExtended_enumeration_type)type).getIs_based_on(null);
				partType += print(based_on.getName(null));
			} else { // just a regular enumeration
				partType += print("ENUMERATION OF (");
			}

			A_string ant = ((EEnumeration_type) type).getElements(null);
			int iDim =  ant.getMemberCount();
			if (iDim >= 1) {

				if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
					partType += print(" WITH (");
				} else
				if (type instanceof EExtensible_enumeration_type) {
					partType += print(" OF (");
				} else
				if (type instanceof EExtended_enumeration_type) {
					partType += print(" WITH (");
				} else {
				}

				partType += println();
				int i = 1;
				while (i <= iDim) {
					String temp = "";
					temp += print(ant.getByIndex(i));
					if (i < iDim) {
						temp += print(",");
					} else {
						//temp += print(" )");
                        temp += print(" );");
					}
					i++;
					partType += printTab(temp);
				}
			} else {
				//partType += print(" )");
				if ((type instanceof EExtensible_enumeration_type) && (type instanceof EExtended_enumeration_type)) {
                partType += print(";\n");
				} else
				if (type instanceof EExtensible_enumeration_type) {
                partType += print(";\n"); // for regular enumeration with have println to handle new line
				} else
				if (type instanceof EExtended_enumeration_type) {
                partType += print(";\n");
				} else {
                partType += print(" );");
				}
			}
		}

    else if (type instanceof EData_type) {
//    	System.out.println("generic or generic_entity type: " + type);

			if (((EData_type)type).getName(null).equals("_ENTITY")) {
	    	partType += print("GENERIC_ENTITY");
  	  } else 
    	if (((EData_type)type).getName(null).equals("_GENERIC")) {
	    	partType += print("GENERIC");
			} else {
		  	partType += print("UNKNOWN_DATA_TYPE");
			}     
  
    } else {
			partType += print("UNKNOWN_NON_DATA_TYPE");
    }
		return partType;
	}

//print boud of aggregation type
	private String printBound(EBound bound) throws SdaiException {
		String partBound = "";
		if (bound == null) {
			partBound += print("?");
		}
		else if (bound instanceof EInteger_bound) {
			partBound += print(String.valueOf(((EInteger_bound)bound).getBound_value(null)));
		}
		else {
//			partBound += print("??");


				boolean done_nothing = true;

        AEntity aeUsers = new AEntity();
        bound.findEntityInstanceUsers(asmExpressDomain, aeUsers);
//System.out.println("pdb: " + bound + ", user nr: " + aeUsers.getMemberCount() + ", users: " + aeUsers);
        SdaiIterator iter_users=aeUsers.createIterator();
        while (iter_users.next()) {
        	EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
//System.out.println("user: " + eUser);
          if (eUser instanceof EExpress_code) {
          	EExpress_code ec = (EExpress_code) eUser;
            A_string asECValues = ec.getValues(null);
            SdaiIterator iter_ecv=asECValues.createIterator();
            while (iter_ecv.next()) {
	          	String expr = (String)asECValues.getCurrentMember(iter_ecv);  
							if (flag_long_form) {
								expr = replaceAll(expr, current_schema.toUpperCase(),  express_long_schema.toUpperCase());
								expr = replaceAll(expr, current_schema.toLowerCase(),  express_long_schema.toLowerCase());
							}				
							if (flag_format_expressions) {
								partBound += print(format(expr));
							} else {
								partBound += print(expr);
							}
							done_nothing = false;
//							break;
            }
          }
        }

				if (done_nothing) {
					partBound += print("??");
				} 




		}
		return partBound;
	}



	private void printSubtypeConstraintLF(PrintWriter pw, ESub_supertype_constraint constraint, ESchema_definition schema, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException { 
		String 	partHeader = "",
            partExpress = "",
            partClass = "",
            partComments = "";

    //Express for subtype constraint
		partExpress += printExpressForSubtypeConstraintLF(constraint, entity_definitions, entity_count);

		//Coments for subtype_constraint
//		partComments += printDocumentationForEntity(constraint);
		if (!partComments.equals("")) {
			partComments += printBreak();
		}

    pw.println(partExpress);
//		pw.print(partComments);
	}


	// is it used in alternate full long form or anywhere else?
	private void printSubtypeConstraint(PrintWriter pw, ESub_supertype_constraint constraint, ESchema_definition schema) throws SdaiException { 
		String 	partHeader = "",
            partExpress = "",
            partClass = "",
            partComments = "";

    //Express for subtype constraint
		partExpress += printExpressForSubtypeConstraint(constraint);

		//Coments for subtype_constraint
//		partComments += printDocumentationForEntity(constraint);
		if (!partComments.equals("")) {
			partComments += printBreak();
		}

    pw.println(partExpress);
//		pw.print(partComments);
	}

	private String printExpressForSubtypeConstraintLF(ESub_supertype_constraint constraint, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException {
		String result = "";
		String parent_entity = "";
		EEntity_definition ed = (EEntity_definition)constraint.getGeneric_supertype(null);
		
		// if the entity itself is not in the short form, just skip the whole subtype_constraint
		// although, on the other hand, how could it be? Let's print a warning
		if (!entityPresent(ed, entity_definitions, entity_count, null)) {
			System.out.println("WARNING: stand-alone subtype_constraint " + constraint.getName(null) + " not generated, because its entity is not in this long form schema: " + ed.getName(null));  
			return "";
		}
    parent_entity += ed.getName(null); // toLowerCase() removed

//    result = println("SUBTYPE_CONSTRAINT " + printBold(constraint.getName(null).toLowerCase())+" FOR "+ parent_entity +";");
    result = println("SUBTYPE_CONSTRAINT " + printBold(constraint.getName(null))+" FOR "+ parent_entity +";");

		if (constraint.testAbstract_supertype(null)) { // it is mandatory, but just in case
			if (constraint.getAbstract_supertype(null)) {
				result += printTab("ABSTRACT SUPERTYPE;");
			}
		}

		// here we can reuse the same method that prints subtype constraints in entities
		ESubtype_expression expression = null;
		if (constraint.testConstraint(null)) {
			expression = constraint.getConstraint(null);
		}

		if (expression != null ) {
			String temp_result = printSubtypeConstraintLF(expression, entity_definitions, entity_count, null);
			temp_result += println(";");
			result += printUnclosedTab(temp_result);
		}	

//    result += printBreak();
		if (flag_end_comment) {
	    result += println("END_SUBTYPE_CONSTRAINT; -- " + constraint.getName(null));
		} else {
  	  result += println("END_SUBTYPE_CONSTRAINT;");
		}
		return result;
	}

	private String printExpressForSubtypeConstraint(ESub_supertype_constraint constraint) throws SdaiException {
		String result = "";
		String parent_entity = "";
		EEntity_definition ed = (EEntity_definition)constraint.getGeneric_supertype(null);

    parent_entity += ed.getName(null); // toLowerCase() removed

//    result = println("SUBTYPE_CONSTRAINT " + printBold(constraint.getName(null).toLowerCase())+" FOR "+ parent_entity +";");
    result = println("SUBTYPE_CONSTRAINT " + printBold(constraint.getName(null))+" FOR "+ parent_entity +";");

		if (constraint.testAbstract_supertype(null)) { // it is mandatory, but just in case
			if (constraint.getAbstract_supertype(null)) {
				result += printTab("ABSTRACT SUPERTYPE;");
			}
		}

		// here we can reuse the same method that prints subtype constraints in entities
		ESubtype_expression expression = null;
		if (constraint.testConstraint(null)) {
			expression = constraint.getConstraint(null);
		}

		if (expression != null ) {
			String temp_result = printSubtypeConstraint(expression);
			temp_result += println(";");
			result += printUnclosedTab(temp_result);
		}	

//    result += printBreak();
		if (flag_end_comment) {
	    result += println("END_SUBTYPE_CONSTRAINT; -- " + constraint.getName(null));
		} else {
  	  result += println("END_SUBTYPE_CONSTRAINT;");
		}
		return result;
	}


	private void printGlobalRuleLF(PrintWriter pw, EGlobal_rule rule, SdaiRepository repo, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException {
    asmExpressDomain.clear();
    SdaiModel express_model = findExpressModel(repo, rule);
    if (express_model != null) {
    	asmExpressDomain.addUnordered(express_model, null);
		}
		current_schema = rule.findEntityInstanceSdaiModel().getName();
		current_schema = current_schema.substring(0, current_schema.lastIndexOf("_DICTIONARY_DATA"));
		printGlobalRuleLF(pw, rule, (ESchema_definition)null, entity_definitions, entity_count);
	}

	private void printGlobalRuleLF(PrintWriter pw, EGlobal_rule rule, ESchema_definition schema, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException {
		String 	partHeader = "",
                partExpress = "",
                partClass = "",
                partComments = "";
        //Express for global rule
		partExpress += printExpressForGlobalRuleLF(rule, entity_definitions, entity_count);
//		partExpress += printBreak();
        //Function for constant
		//Coments for entity
//		partComments += printDocumentationForEntity(rule);
		if (!partComments.equals("")) {
			partComments += printBreak();
		}
        
        pw.println(partExpress);
//		pw.print(partComments);
	}

    //prints global rule
	private void printGlobalRule(PrintWriter pw, EGlobal_rule rule, ESchema_definition schema) throws SdaiException {
		String 	partHeader = "",
                partExpress = "",
                partClass = "",
                partComments = "";
        //Express for global rule
		partExpress += printExpressForGlobalRule(rule);
//		partExpress += printBreak();
        //Function for constant
		//Coments for entity
//		partComments += printDocumentationForEntity(rule);
		if (!partComments.equals("")) {
			partComments += printBreak();
		}
        
        pw.println(partExpress);
//		pw.print(partComments);
	}
    
    private String printExpressForGlobalRuleLF(EGlobal_rule rule, EEntity_definition [] entity_definitions, int entity_count) throws SdaiException {
		String result = "";
        String entity_ref_list = "";
        AEntity_definition aed=rule.getEntities(null);
// System.out.println("global rule entities nr: " + aed.getMemberCount()); 
        
        SdaiIterator iter_aed = aed.createIterator();
        while (iter_aed.next()) {
        	EEntity_definition ed = aed.getCurrentMember(iter_aed);
//System.out.println("global rule entity: " + ed.getName(null)); 
          if (entityPresent(ed, entity_definitions, entity_count, null)) {
          	if (!entity_ref_list.equals("")) {
          		entity_ref_list += ", ";
          	}
          	entity_ref_list += ed.getName(null); // toLowerCase() removed
        	}
        }
        
//        result = println("RULE " + printBold(rule.getName(null).toLowerCase())+" FOR ("+entity_ref_list+");");
//        result = print("RULE " + printBold(rule.getName(null).toLowerCase())+" FOR ("+entity_ref_list+");");
        result = print("RULE " + printBold(rule.getName(null))+" FOR ("+entity_ref_list+");");

// rr start

        AEntity aeUsers = new AEntity();
        rule.findEntityInstanceUsers(asmExpressDomain, aeUsers);
        SdaiIterator iter_users=aeUsers.createIterator();
        while (iter_users.next()) {
            EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
            if (eUser instanceof EExpress_code) {
                EExpress_code ec = (EExpress_code) eUser;
                A_string asECValues = ec.getValues(null);
                SdaiIterator iter_ecv=asECValues.createIterator();
                while (iter_ecv.next()) {

                    String sEC;                    
                    if (flag_format_expressions) {
                    	sEC=format(asECValues.getCurrentMember(iter_ecv));
                    } else {
                    	sEC=asECValues.getCurrentMember(iter_ecv);
                    }	
// some formatting, but better formating would be nice, perhaps to implement parsing of the string

                    if (flag_format_expressions) {
                    	sEC=replaceAll( sEC, "-- LOCAL", "-- L0CAL\n");
                    	sEC=replaceAll( sEC, "LOCAL", "LOCAL\n");
                    	sEC=replaceAll( sEC, ";", ";\n");
                  	} else {
	                  	// trying to allign LOCAL and END_LOCAL
	                  	sEC = "\n" + allignLOCAL(sEC);
                  		
                  	}

/*

                    sEC=replaceAll( sEC, ";", ";<BR>\n");
                    sEC=replaceAll( replaceAll( sEC, "end_local", "END_LOCAL"), "local", "LOCAL<BR>\n");
                    sEC=replaceAll( replaceAll( sEC, "end ;", "END ;"), "begin", "BEGIN<BR>\n");
                    sEC=replaceAll( sEC, "return", "RETURN");
                    
                    for (Iterator iter_ad = vAlgorithmDefinition.iterator(); iter_ad.hasNext(); ) {
								        EAlgorithm_definition ad=(EAlgorithm_definition)iter_ad.next();
                        sEC = replaceAll( sEC, ad.getName(null)+" (", printHRef(ad.getName(null).toLowerCase(), getSchemaNameIfDiffer(ad, schema)+getUpper(ad.getName(null))+".html")+" (");
                    }
                    algorithm_body += sEC;
*/
 										result += println(sEC);
                }
            }
        }


// rr end

        result += printWhere_rules(rule);
				if (flag_end_comment) {
	        result += println("END_RULE; -- " + rule.getName(null));
				} else {
  	      result += println("END_RULE;");
				}
		return result;
	}
    
    private String printExpressForGlobalRule(EGlobal_rule rule) throws SdaiException {
		String result = "";
        String entity_ref_list = "";
        AEntity_definition aed=rule.getEntities(null);
// System.out.println("global rule entities nr: " + aed.getMemberCount()); 
        
        SdaiIterator iter_aed = aed.createIterator();
        while (iter_aed.next()) {
            EEntity_definition ed = aed.getCurrentMember(iter_aed);
//System.out.println("global rule entity: " + ed.getName(null)); 
            if (!entity_ref_list.equals("")) {
                entity_ref_list += ", ";
            }
            entity_ref_list += ed.getName(null); // toLowerCase() removed
        }
        
//        result = println("RULE " + printBold(rule.getName(null).toLowerCase())+" FOR ("+entity_ref_list+");");
//        result = print("RULE " + printBold(rule.getName(null).toLowerCase())+" FOR ("+entity_ref_list+");");
        result = print("RULE " + printBold(rule.getName(null))+" FOR ("+entity_ref_list+");");

// rr start

        AEntity aeUsers = new AEntity();
        rule.findEntityInstanceUsers(asmExpressDomain, aeUsers);
        SdaiIterator iter_users=aeUsers.createIterator();
        while (iter_users.next()) {
            EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
            if (eUser instanceof EExpress_code) {
                EExpress_code ec = (EExpress_code) eUser;
                A_string asECValues = ec.getValues(null);
                SdaiIterator iter_ecv=asECValues.createIterator();
                while (iter_ecv.next()) {
                    
                    String sEC;
										if (flag_format_expressions) {
                    	sEC=format(asECValues.getCurrentMember(iter_ecv));
										} else {
                    	sEC=asECValues.getCurrentMember(iter_ecv);
										}
// some formatting, but better formating would be nice, perhaps to implement parsing of the string

										if (flag_format_expressions) {

	                    sEC=replaceAll( sEC, "-- LOCAL", "-- L0CAL\n");
  	                  sEC=replaceAll( sEC, "LOCAL", "LOCAL\n");
    	                sEC=replaceAll( sEC, ";", ";\n");
                  	} else {
	                  	// trying to allign LOCAL and END_LOCAL
	                  	sEC = "\n" + allignLOCAL(sEC);
                  		
                  	}

/*

                    sEC=replaceAll( sEC, ";", ";<BR>\n");
                    sEC=replaceAll( replaceAll( sEC, "end_local", "END_LOCAL"), "local", "LOCAL<BR>\n");
                    sEC=replaceAll( replaceAll( sEC, "end ;", "END ;"), "begin", "BEGIN<BR>\n");
                    sEC=replaceAll( sEC, "return", "RETURN");
                    
                    for (Iterator iter_ad = vAlgorithmDefinition.iterator(); iter_ad.hasNext(); ) {
								        EAlgorithm_definition ad=(EAlgorithm_definition)iter_ad.next();
                        sEC = replaceAll( sEC, ad.getName(null)+" (", printHRef(ad.getName(null).toLowerCase(), getSchemaNameIfDiffer(ad, schema)+getUpper(ad.getName(null))+".html")+" (");
                    }
                    algorithm_body += sEC;
*/
 										result += println(sEC);
                }
            }
        }


// rr end

        result += printWhere_rules(rule);
				if (flag_end_comment) {
	        result += println("END_RULE; -- " + rule.getName(null));
				} else {
	        result += println("END_RULE;");
				}
		return result;
	}
    
    
    //prints algorithm
	private void printAlgorithm(PrintWriter pw, EAlgorithm_definition algorithm, ESchema_definition schema) throws SdaiException {
		String 	partHeader = "",
                partExpress = "",
                partClass = "",
                partComments = "";
        //Express for algorithm
		partExpress += printExpressForAlgorithm(algorithm);
//		partExpress += printBreak();
        //Function for constant
        //Coments for entity
//		partComments += printDocumentationForEntity(algorithm);
		if (!partComments.equals("")) {
			partComments += printBreak();
		}
        
        pw.println(partExpress);
//		pw.print(partComments);
	}


	  private String insertMultipleLabels(String a_parameter, A_string labels)  throws SdaiException {


//System.out.println("MULTI - parameter: " + a_parameter + ", labels: " + labels);


      String result = a_parameter;
//      String result2 = "";
    	SdaiIterator iter_labels = labels.createIterator();
      String a_label = "";
      int index = 0;
      int current_index = 0;
      boolean first_time = true;
      while (iter_labels.next()) {
      	a_label = labels.getCurrentMember(iter_labels);

//System.out.println("MULTI - current label: " + a_label);
      	/*
      		 this label goes after the current occurence of AGGREGATE,
      		 if a_label is not "", then it must be AGGREGATE,
      		 if a_label is "" then it might be either AGGREGATE, or ARRAY, BAG, LISI, SET, but all of them have OF and the label is not present
      		 
      	*/
				if ((a_label != null) && (!a_label.equals(""))) {
					// this MUST match AGGREGATE OF
				  index = result.indexOf("AGGREGATE OF ",current_index);
      		if (index >= 0) { // it must be
//   			  	String substring_1 = result.substring(index,index + 9);
   			  	String substring_1 = result.substring(0,index + 9);
   			    String substring_2 = result.substring(index + 9);
   			    result = substring_1 + ": " + a_label + substring_2;
                 
//System.out.println("MULTI - index: " + index);
//System.out.println("MULTI - substring 1: " + substring_1);
//System.out.println("MULTI - substring 2: " + substring_2);
//System.out.println("MULTI - result: " + result);
				
				/*
				
				   ... AGGREGATE OF .........
				   substring_1 = ....AGGREGATE
				   substring_2 =  OF .................
				    
				    ...AGGREGATE: a_label OF ............
			
			     should be safe to leave the same index and search in result again
				
				*/
				
				 // needed to make sure that when a label is not present the same OF is not taken again instead of the next one
				 int index2 = result.indexOf(" OF ", current_index);
				 if (index2 > index) {
						index = index2;
				 }
				
				} else {
					// the label is present and AGGREGATE OF not found 
					// that means it must be the label of the final member of the aggregate
					// we need to add this label at the very end.
					// however we could add more checking to catch internal errors:
					// 1) the label must the the last one
					// 2) the final member must be "GENERIC" or "GENERIC ENTITY"
					int index2 = result.lastIndexOf("GENERIC_ENTITY",current_index); // if found, it has to be the end of the string
//						System.out.println("current result: " + result + ", current_index: " + current_index);
//						System.out.println("result.lastIndexOf(\"GENERIC_ENTITY\",current_index): " + result.lastIndexOf("GENERIC_ENTITY",current_index));
//						System.out.println("result.length(): " + result.length());
//						System.out.println("\"GENERIC_ENTITY\".length(): " + "GENERIC_ENTITY".length());
////						System.out.println("result.lastIndexOf(\"GENERIC\",current_index): " + result.lastIndexOf("GENERIC",current_index));
//						System.out.println("result.lastIndexOf(\"GENERIC\",current_index): " + result.indexOf("GENERIC",current_index));
//						System.out.println("result.length(): " + result.length());
//						System.out.println("\"GENERIC\".length(): " + "GENERIC".length());
					
					if (
						 	((result.indexOf("GENERIC_ENTITY",current_index) == result.length() - "GENERIC_ENTITY".length()) ||
						 	(result.lastIndexOf("GENERIC_ENTITY") == result.indexOf("GENERIC_ENTITY",current_index))) || (
						 	(result.indexOf("GENERIC",current_index) == result.length() - "GENERIC".length()) &&
						 	(result.lastIndexOf("GENERIC") == result.indexOf("GENERIC",current_index))) 
						 ) {
						 	// ok, the element is correct, but is the label the last one?
						 	if (!iter_labels.next()) {
						 		// ok, we really can add the label here:
						 		result += ": " + a_label;
						 	} else {
						 		// INTERNAL ERROR not last label but does not match an AGGREGATE
								System.out.println("ExpressGenerator INTERNAL ERROR: the label does not match an AGGREGATE even though it is not the last one: " + a_label);
						 	}
					} else {
						// INTERNAL ERROR wrong element 
						System.out.println("ExpressGenerator INTERNAL ERROR: wrong inner-most member of a nested aggregate, must be GENERIC or GENERIC ENTITY: " + result);
					}
				 
					
					
				} // index < 0 - AGGREGATE OF not found for an existing label, thus, it was the last element
			} else { // if the label is not present
					// this must match AGGREGATE OF, ARRAY [] OF, BAG [] OF, LIST [] OF, SET [] OF
					// skip by one " OF "  because the label is not present
				  index = result.indexOf(" OF ",current_index);
// System.out.println("MULTI - index 3: " + index + ", string: " + result + ", current_index: " + current_index);
			}

		  current_index = index + 1;

	  } // while
		return result;
	}  


   	private String insert2Labels(String a_parameter, String aggr_label, String element_label) {
   		String result = a_parameter;
   		// at least the aggregate has a valid label, therefore also it is AGGREGATE, not SET or anything else
   		// AGGREGATE OF must be transformed into
   		// AGGREGATE: label1 OF element:label2

      // Correction - the last label (the element label) is not included at all if absent,
      // so AGGREGATE: label1 OF AGGREGATE: label2 OF GENERIC is also possible and should be supported


      // so here are the possible cases:
      // if both labels are present:  "label1","label2"
      // 1) AGGREGATE:label1 OF GENERIC:label2
      // 2) AGGREGATE:label1 OF AGGREGATE:label2 of GENERIC

      // empty labels "" are also generated for general sets, etc. so these cases are also possible 
      // if the first label is absent:  "","label2"  
      // 3) AGGREGATE OF GENERIC:label2
			// 3b) SET OF GENERIC:label2  - and other such cases ARRAY, BAG, LIST
			// 3c) SET[0:?] OF GENERIC:label2 - the same but with bound
      // 4) AGGREGATE OF AGGREGATE:label2 OF GENERIC
      // 4b) SET OF AGGREGATE:label2 OF GENERIC
      // 4c) SET[0:?] OF AGGREGATE:label2 OF GENERIC

      // if the 2nd label is absent: "label1",""
      // not sure if such case is generated or always just "label1"
      // but in case it is or may be introduced later by the changes in the express compiler,
      // 5)  AGGREGATE:label1 OF AGGREGATE OF GENERIC
			// 5b) AGGREGATE:label1 OF SET OF GENERIC
			// 5c) AGGREGATE:label1 OF SET[0:?] OF GENERIC
      // 6) AGGREGATE:label1 OF GENERIC - because currently absent label for the final element is not included, still better to support it just in case

      // if both labels are absent: "","" - again, currently this case is probably not in the dictionary, the whole OPTIONAL aggregate is probably absent,
      // however - there is nothing to support for this case anyway, so it is OK.
      // 7) - nothing to do, because you can add an empty label anywhere and that makes no difference

      boolean label_1_present = false;
      boolean label_2_present = false;

   		if ((aggr_label != null) && (!aggr_label.equals(""))) {
				label_1_present = true;
			}
   		if ((element_label != null) && (!element_label.equals(""))) {
				label_2_present = true;
			}

			int index1 = a_parameter.indexOf(" OF ");
			if (index1 >= 0) {
				// at least one aggregate present
				int index2 = a_parameter.indexOf(" OF ", index1+1);
				if (index2 >= 0) {
					// two aggregates present
					int index3 = a_parameter.indexOf(" OF ", index2+1);
					if (index3 >= 0) {
						// INTERNAL ERROR - more than two aggregates with only 2 labels
						System.out.println("ExpressGenerator INTERNAL ERROR - more than two aggregates with two labels: " + a_parameter + ", labels: " + aggr_label + ", " + element_label);
					} else {
						// ok, we have 2 aggregates
						// cases 2),4),4b),4c),5),5b),5c)
	          if (label_1_present && label_2_present) {
  	        	// case 2)
				      // 2) AGGREGATE:label1 OF AGGREGATE:label2 of GENERIC
				   		int index = a_parameter.indexOf("AGGREGATE"); 
   						if (index >= 0) { // it must be
				   			String substring_1 = a_parameter.substring(0,index + 9);
   							int index5 = a_parameter.indexOf(" OF AGGREGATE");
   							if (index5 >= 0) {
   								String substring_2 = a_parameter.substring(index + 9, index5 + 12);
   								String substring_3 = a_parameter.substring(index5 + 12);
   								result = substring_1 + ": " + aggr_label + substring_2 + ": " + element_label + substring_3;
   							} else {
   								// INTERNAL ERROR
		          		System.out.println("ExpressGenerator INTERNAL ERROR - inserting 2 labels 03: " + a_parameter + ", " + aggr_label + ", " + element_label);
   							}
    	      	} else {
    	      		// INTERNAL ERROR
	          		System.out.println("ExpressGenerator INTERNAL ERROR - inserting 2 labels 04: " + a_parameter + ", " + aggr_label + ", " + element_label);
    	      	}
    	      } else
      	    if (!label_1_present && label_2_present) {
        	  	// cases 4),4b),4c)
				      // 4) AGGREGATE OF AGGREGATE:label2 OF GENERIC
				      // 4b) SET OF AGGREGATE:label2 OF GENERIC
      				// 4c) SET[0:?] OF AGGREGATE:label2 OF GENERIC
        	  	int index = a_parameter.indexOf(" OF AGGREGATE OF ");
			   			String substring_1 = null;
 							String substring_2 = null;
 							if (index >= 0) {
				   			substring_1 = a_parameter.substring(0,index + 13);
   							substring_2 = a_parameter.substring(index + 13);
 								result = substring_1 + ": " + element_label + substring_2;
 							}
          	} else
	          if (label_1_present && !label_2_present) {
  	        	// case 5),5b),5c)
				      // 5)  AGGREGATE:label1 OF AGGREGATE OF GENERIC
							// 5b) AGGREGATE:label1 OF SET OF GENERIC
							// 5c) AGGREGATE:label1 OF SET[0:?] OF GENERIC
				   		int index = a_parameter.indexOf("AGGREGATE"); 
   						if (index >= 0) { // it must be
				   			String substring_1 = a_parameter.substring(0,index + 9);
   							String substring_2 = a_parameter.substring(index + 9);
								result = substring_1 + ": " + aggr_label + substring_2;
							}		
    	      }
					}
				} else {
					// only one aggregate present
		   		int index = a_parameter.indexOf("AGGREGATE OF"); 
	   			String substring_1 = null;
 					String substring_2 = null;
		   		if (index >= 0) {
		   			substring_1 = a_parameter.substring(0,index + 9);
   					substring_2 = a_parameter.substring(index + 9);
		   			
		   		}
					// cases 1),3),3b),3c),6)
          if (label_1_present && label_2_present) {
          	// case 1)
			      // 1) AGGREGATE:label1 OF GENERIC:label2
          	if ((substring_1 != null) && (substring_2 != null)) {
			   			result = substring_1 + ": " + aggr_label + substring_2 + ": " + element_label;
          	} else {
          		// INTERNAL ERROR
          		System.out.println("ExpressGenerator INTERNAL ERROR - inserting 2 labels 01: " + a_parameter + ", " + aggr_label + ", " + element_label);
          	} 
          } else
          if (!label_1_present && label_2_present) {
          	// cases 3),3b),3c)
				    // 3) AGGREGATE OF GENERIC:label2
						// 3b) SET OF GENERIC:label2  - and other such cases ARRAY, BAG, LIST
						// 3c) SET[0:?] OF GENERIC:label2 - the same but with bound
      			result = a_parameter + ": " + element_label;
          } else
          if (label_1_present && !label_2_present) {
          	// case 6)
			      // 6) AGGREGATE:label1 OF GENERIC - because currently absent label for the final element is not included, still better to support it just in case
          	if ((substring_1 != null) && (substring_2 != null)) {
			   			result = substring_1 + ": " + aggr_label + substring_2;
          	} else {
          		// INTERNAL ERROR
          		System.out.println("ExpressGenerator INTERNAL ERROR - inserting 2 labels 02: " + a_parameter + ", " + aggr_label + ", " + element_label);
          	} 
          }

				}
					
			} else {
				// INTERNAL ERROR no aggregates at all
				System.out.println("ExpressGenerator INTERNAL ERROR - no aggregates with two labels: " + a_parameter + ", labels: " + aggr_label + ", " + element_label);
			}

			return result;
   	}

   	private String insert1Label(String a_parameter, String a_label) {
   		String result = a_parameter;
   		// there is a valid label, but it may be either a label of a single type, or the aggregate label when AGGREGATE OF is present and the element has no label
   		// if present, AGGREGATE OF must be transformed into
   		// AGGREGATE: label1 OF element
   		// if absent, 
   		// element: label1
//System.out.println("a_parameter 1: " + a_parameter);
   		if (a_parameter.indexOf("AGGREGATE OF") >= 0) { // the label is an aggregate label
	   		int index = a_parameter.lastIndexOf("AGGREGATE"); 
  	 		if (index >= 0) { // it must be
   				String substring_1 = a_parameter.substring(index,index + 9);
   				String substring_2 = a_parameter.substring(index + 9);
   				result = substring_1 + ": " + a_label + substring_2;
   			} else {
   				// seems like an internal error
   			}
   		} else { // the label is a single type label
   			result = a_parameter + ": " + a_label;
   		}	
			return result;
   	}



    private String printExpressForAlgorithm(EAlgorithm_definition algorithm) throws SdaiException {
		String result = "";
        String parameter_list = "";
        String algorithm_body = "";
        if (algorithm.testParameters(null)) {
            AParameter ap=algorithm.getParameters(null);
            SdaiIterator iter_ap = ap.createIterator();
            while (iter_ap.next()) {
                EParameter p = ap.getCurrentMember(iter_ap);
                if (p.testName(null)) {
                    if (!parameter_list.equals("")) {
//                        parameter_list += ", ";
                        parameter_list += "; ";
                    }
                    if (p.testVar_type(null) && p.getVar_type(null)) {
                        parameter_list += "VAR ";
                    }
                    parameter_list += p.getName(null);
                    if (p.testParameter_type(null)) {
//                        parameter_list += " : "+printType(p.getParameter_type(null));
                        String a_parameter = printType(p.getParameter_type(null));
                        if (p.testType_labels(null)) {
                        	  // to support labels for aggregates and nested aggregates, 
                        	  // either printType should take Parameter as argument
                        	  // or here we need to insert labels into the string.
                            A_string atl=p.getType_labels(null);
                            int number_of_labels = atl.getMemberCount();
                            if (number_of_labels < 1) { // nothing to do
                            } else 
                            if (number_of_labels == 1) { // should be a non-aggregate type
                            	// CAUTION - check in the dictionary what is generated for AGGREGATE:label OF GENERIC
                            	// if a single label is generated, then there is an ambiguous situation
                            	// checked - yes, a single label may meen GENERIC:label or AGGREGATE:label OF GENERIC
                            	String stl = atl.getByIndex(1);
				                    	// CORRECTION - it still may be an aggregate
        				            	// as currently implemented, in the dictionary, if an element has no label, then it is not added to the list of labels
                				    	// perhaps we could check if the type is an aggregation type or not - or just by analyzing the type string - the same thing
	
                             	if ((stl != null) && (!stl.equals(""))) { // just an additional protection
//                              	a_parameter += ": " + stl;
        	  	                  a_parameter = insert1Label(a_parameter, stl);
				                    	}



                            } else 
                            if (number_of_labels == 2) { // a simple aggregate - correction, also a nested aggregate with the inner element without label
                            	String st_aggregate_label = atl.getByIndex(1);
                            	String st_element_label = atl.getByIndex(2);
//                            	if ((st_aggregate_label != null) && (!st_aggregate_label.equals(""))){
	                            	// have to insert into a_parameter
  	                          	a_parameter = insert2Labels(a_parameter,st_aggregate_label,st_element_label);
//                            	} else {
                            		// aggregate does not have a label, possibly only the element
//	                             	if ((st_element_label != null) && (!st_element_label.equals(""))) { // just an additional protection
//  	                            	a_parameter += ": " + st_element_label;
//    	                        	}
//                            	}
                            } else
                            if (number_of_labels > 2) { // a nested aggregate
	                          	a_parameter = insertMultipleLabels(a_parameter,atl);
                            } else {
                          		// internal error
                          	}	
                        
                    /*    
                            parameter_list += ":";
                            A_string atl=p.getType_labels(null);
                            SdaiIterator iter_atl=atl.createIterator();
                            String stl="";
                            while (iter_atl.next()) {
                                if (!stl.equals("")) {
//                                    stl += ",";
                                    stl += ";";
                                }
                                stl+=atl.getCurrentMember(iter_atl);
                            }
                            parameter_list += stl;
                        }
                    */
// moved down for the cases without labels as well
//                          parameter_list += ": " + a_parameter;
                  
//                        parameter_list += a_parameter;
                    
                    } // if labels present
                    parameter_list += ": " + a_parameter;

                	} // if parameter_type not null
            		} // if parameter name
            } // loop through parameters
        } // if parameters not null
        
        
        AEntity aeUsers = new AEntity();
        algorithm.findEntityInstanceUsers(asmExpressDomain, aeUsers);
        SdaiIterator iter_users=aeUsers.createIterator();
        while (iter_users.next()) {
            EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
            if (eUser instanceof EExpress_code) {
                EExpress_code ec = (EExpress_code) eUser;
                A_string asECValues = ec.getValues(null);
                SdaiIterator iter_ecv=asECValues.createIterator(); // OO0O0O00O0O00OO0
                while (iter_ecv.next()) {
                    //long lFSt, lFCt;
                    //lFSt=System.currentTimeMillis(); //--VV--
	           			String expr = (String)asECValues.getCurrentMember(iter_ecv);  
									if (flag_long_form) {
										expr = replaceAll(expr, current_schema.toUpperCase(),  express_long_schema.toUpperCase());
										expr = replaceAll(expr, current_schema.toLowerCase(),  express_long_schema.toLowerCase());
									}				
                    
                    String sEC = expr;
                    if (flag_format_expressions) {
                    	sEC = format(expr);


// System.out.println("-OO-: " + sEC);

                    //sEC=sEC.replaceAll( ";", ";<BR>\n");
                    //sEC=sEC.replaceAll( "end_local", "END_LOCAL").replaceAll( "local", "LOCAL<BR>\n");

//RR-2008-02:  ; inside a single line comment splits that comment and generates uncompilable express,
// temporarily disabled
//                    sEC=replaceAll( sEC, ";", ";\n");


//                    sEC=replaceAll( replaceAll( sEC, "end_local", "END_LOCAL"), "local", "LOCAL\n");
//                    sEC=replaceAll2( replaceAll2( sEC, "end_local", "END_LOCAL"), "local", "LOCAL\n");
                    sEC=replaceAll(sEC, "-- LOCAL", "-- L0CAL\n");
                    sEC=replaceAll(sEC, "LOCAL", "LOCAL\n");
										
                    //sEC=sEC.replaceAll( "end_if", "END_IF").replaceAll( "else", "ELSE<BR>\n").replaceAll( "then", "THEN<BR>\n").replaceAll( "if", "IF");
                    //sEC=sEC.replaceAll( "end_case", "END_CASE").replaceAll( "otherwise", "OTHERWISE").replaceAll( "case", "CASE");
                    //sEC=sEC.replaceAll( "end_repeat", "END_REPEAT").replaceAll( "repeat", "REPEAT").replaceAll( "loindex", "LOINDEX").replaceAll( "hiindex", "HIINDEX");
                    //sEC=sEC.replaceAll( " in ", " IN ").replaceAll( " to ", " TO ").replaceAll( "sizeof", "SIZEOF").replaceAll( "typeof", "TYPEOF").replaceAll( "usedin", "USEDIN");
                    
                    //sEC=sEC.replaceAll( "end ;", "END ;").replaceAll( "begin", "BEGIN<BR>\n");
                    //sEC=sEC.replaceAll( "return", "RETURN");
                    sEC=replaceAll( replaceAll( sEC, "end ;", "END ;"), "begin", "BEGIN\n");
                    sEC=replaceAll( sEC, "return", "RETURN");
                    
                    for (Iterator iter_ad = vAlgorithmDefinition.iterator(); iter_ad.hasNext(); ) {
				        EAlgorithm_definition ad=(EAlgorithm_definition)iter_ad.next();
                        //sEC = sEC.replaceAll( ad.getName(null)+" ?\\(", printHRef(ad.getName(null).toLowerCase(), getSchemaNameIfDiffer(ad, schema)+getUpper(ad.getName(null))+".html")+" (");
                        sEC = replaceAll( sEC, ad.getName(null)+" (", ad.getName(null) + " (");
                    }

                  } else { // if NOT format expressions

												        
                  	// we had an issue with LOCAL and END_LOCAL; not always alligned perfectly
                  	// let's try to find END_LOCAL, and if present, double check that LOCAL is at the very beginning of the string
                  	// and then go back from END_LOCAL until we find a new line there, and take everything that is between the new line and END_LOCAL
                  	// and att it also before the LOCAL, i.e, before the string itself.
                  	sEC = allignLOCAL(sEC);
                  } 

                    algorithm_body += sEC;
                    //lFCt=System.currentTimeMillis(); //--VV--
                    //lFDt += lFCt-lFSt;
                }
            }
        }

        if (algorithm instanceof EFunction_definition) {
            EFunction_definition function = (EFunction_definition)algorithm;
//            result = println("FUNCTION " + printBold(function.getName(null).toLowerCase()));
            result = println("FUNCTION " + printBold(function.getName(null)));
            //result += printTab("("+parameter_list+")");
            result += "\t("+parameter_list+")";
            if (function.testReturn_type(null)) {
                String f_return_type = printType(function.getReturn_type(null));
//                result += " : "+printType(function.getReturn_type(null));
//                if (function.testReturn_type_label(null)) {
//                    result += ":"+function.getReturn_type_label(null);
//                }


                if (function.testReturn_type_labels(null)) {
                	A_string atl=function.getReturn_type_labels(null);
//System.out.println("function: " + function.getName(null) + ", labels: " + atl); 
                  int number_of_labels = atl.getMemberCount();
                  if (number_of_labels < 1) { // nothing to do
                  } else 
                  if (number_of_labels == 1) { // should be a non-aggregate type
                  	String stl = atl.getByIndex(1);
                    if ((stl != null) && (!stl.equals(""))) { // just an additional protection
                    	// CORRECTION - it still may be an aggregate
                    	// as currently implemented, in the dictionary, if an element has no label, then it is not added to the list of labels
                    	// perhaps we could check if the type is an aggregation type or not - or just by analyzing the type string - the same thing
                    	// f_return_type += ": " + stl;
  	                  f_return_type = insert1Label(f_return_type, stl);
                    }
                  } else 
                  if (number_of_labels == 2) { // a simple aggregate
                  	String st_aggregate_label = atl.getByIndex(1);
                    String st_element_label = atl.getByIndex(2);
//                    if ((st_aggregate_label != null) && (!st_aggregate_label.equals(""))){
	                  	// have to insert into a_parameter
  	                   f_return_type = insert2Labels(f_return_type, st_aggregate_label, st_element_label);
//                    } else {
                    	// aggregate does not have a label, possibly only the element
//	                  	if ((st_element_label != null) && (!st_element_label.equals(""))) { // just an additional protection
//  	                  	f_return_type += ": " + st_element_label;
//    	                }
//                    }
                  } else
                  if (number_of_labels > 2) { // a nested aggregate
	                 	f_return_type = insertMultipleLabels(f_return_type,atl);
                  } else {
                          		// internal error
                  }	
                        
// moved down
//                  result += " : " + f_return_type;
                  
                   
                    } // if labels present

                  result += " : " + f_return_type;




            }
            //result += println(";");
            result += ";\n";
//            result += print(algorithm_body+"");
            result += print(algorithm_body);
            if (flag_format_expressions) {
							if (flag_end_comment) {
	            	result += println("END_FUNCTION; -- " + function.getName(null));
							} else {
	            	result += println("END_FUNCTION;");
							}
            } else {
							if (flag_end_comment) {
	            	result += println("\nEND_FUNCTION; -- " + function.getName(null));
							} else {
  	          	result += println("\nEND_FUNCTION;");
							}
						}
        }
        if (algorithm instanceof EProcedure_definition) {
            EProcedure_definition procedure = (EProcedure_definition)algorithm;
//            result = println("PROCEDURE " + printBold(procedure.getName(null).toLowerCase()));
            result = println("PROCEDURE " + printBold(procedure.getName(null)));
            //result += printTab("("+parameter_list+")");
            //result += println(";");
            result += printTab("("+parameter_list+");");
            result += println(algorithm_body);
						if (flag_end_comment) {
	            result += println("END_PROCEDURE; -- " + procedure.getName(null));
						} else {
  	          result += println("END_PROCEDURE;");
						}
        }
		return result;
	}


 	// we had an issue with LOCAL and END_LOCAL; not always alligned perfectly
 	// let's try to find END_LOCAL, and if present, double check that LOCAL is at the very beginning of the string
 	// and then go back from END_LOCAL until we find a new line there, and take everything that is between the new line and END_LOCAL
 	// and att it also before the LOCAL, i.e, before the string itself.
	private String allignLOCAL(String source_str) {
		if (flag_do_not_allign_LOCAL) return source_str;
		String result_str = source_str;
		int index_end_local = source_str.indexOf("END_LOCAL");
    if (index_end_local > 0) {
    	int index_local = source_str.indexOf("LOCAL");
    	if (index_local == 0) {
    		int index_stuff = source_str.lastIndexOf("\n",index_end_local);
    		// some stupid double-checks
    		if (index_stuff > 0) {
    			if (index_stuff < index_end_local) {  
						String stuff_str = source_str.substring(index_stuff+1, index_end_local);    			
    				result_str = stuff_str + source_str;
    				return result_str;
    			}
    		}
    	}
    }		
		return result_str;
	}

	

	private String printExpressForAttribute(EAttribute attribute) throws SdaiException {
		String result = "";
		String attribute_name = attribute.getName(null);
		EEntity_definition definition = (EEntity_definition)attribute.getParent(null);
		EEntity domain = getAttrType(attribute);

/*
		result += ((testRedeclaring(attribute))?"SELF\\"+getRedeclaring(attribute).getParent(null).getName(null)+".":"");
		result += printHRef(attribute_name.toLowerCase(), "#"+attribute_name.toLowerCase())+": ";
*/


//		result += ((testRedeclaring(attribute))?"SELF\\"+getRedeclaring(attribute).getParent(null).getName(null)+".":"");
		if (testRedeclaring(attribute)) {
			result += "SELF\\"+getRedeclaring(attribute).getParent(null).getName(null)+".";
			// see if it is renamed
			String current_name = attribute.getName(null);
			String previous_name = getRedeclaring(attribute).getName(null);
			if (current_name.equalsIgnoreCase(previous_name)) {
				// not renamed, we are not interested if renamed even earlier
//				result += printHRef(attribute_name.toLowerCase(), "#"+attribute_name.toLowerCase())+" : ";

//				result += attribute_name.toLowerCase() + " : ";
				result += attribute_name + " : ";
			} else {
				// renamed
//				result += printHRef(previous_name.toLowerCase(), "#"+previous_name.toLowerCase());

//				result += previous_name.toLowerCase();
				result += previous_name;
//				result += " RENAMED " + current_name.toLowerCase() + " : ";

//				result += " RENAMED " + printHRef(current_name.toLowerCase(), "#"+current_name.toLowerCase()) + " : ";

//				result += " RENAMED " + current_name.toLowerCase() + " : ";
				result += " RENAMED " + current_name + " : ";
			}
		} else {
//			result += "";
//			result += printHRef(attribute_name.toLowerCase(), "#"+attribute_name.toLowerCase())+": ";

//			result += attribute_name.toLowerCase()+" : ";
			result += attribute_name+" : ";
		}



		if (attribute instanceof EExplicit_attribute) {
			EExplicit_attribute ea = (EExplicit_attribute)attribute;
			result += (ea.getOptional_flag(null)?"OPTIONAL ":"");
		}
		result += printType(domain);
    if (attribute instanceof EDerived_attribute) {
			EDerived_attribute da = (EDerived_attribute)attribute;
            AEntity aeUsers = new AEntity();
            da.findEntityInstanceUsers(asmExpressDomain, aeUsers);
            SdaiIterator iter_users=aeUsers.createIterator();
            result += " := ";
            while (iter_users.next()) {
                EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
                if (eUser instanceof EExpress_code) {
                    EExpress_code ec = (EExpress_code) eUser;
                    A_string asECValues = ec.getValues(null);
                    SdaiIterator iter_ecv=asECValues.createIterator();
                    while (iter_ecv.next()) {
                        
//                        String sEC = format(asECValues.getCurrentMember(iter_ecv));
                        String sEC = asECValues.getCurrentMember(iter_ecv);
												if (flag_format_expressions) {
													sEC = format(asECValues.getCurrentMember(iter_ecv));
													// sEC = Format(sEC); // or perhaps
												

                        for (Iterator iter_ad = vAlgorithmDefinition.iterator(); iter_ad.hasNext(); ) {
                            EAlgorithm_definition ad=(EAlgorithm_definition)iter_ad.next();
                            //sEC = sEC.replaceAll( ad.getName(null)+" ?\\(", printHRef(ad.getName(null).toLowerCase(), getSchemaNameIfDiffer(ad, schema)+getUpper(ad.getName(null))+".html")+" (");
                            sEC = replaceAll( sEC, ad.getName(null)+" (", ad.getName(null)+" (");
                        }
                        
                        } // format expressions
                        
                        result += sEC;
                    }
                }
            }
		}
        result += ";";
		return printTab(result);
	}

	//printing uniqueness rules
	private String printUniqueness_rules(EEntity_definition entity) throws SdaiException {
		String result = "";
		AUniqueness_rule uniques = entity.getUniqueness_rules(null, null);
        if (uniques.getMemberCount() > 0) {
			result += println("UNIQUE");
			SdaiIterator uniques_it = uniques.createIterator();
			while (uniques_it.next()) {
				EUniqueness_rule unique = uniques.getCurrentMember(uniques_it);
				String temp = ((unique.testLabel(null))?unique.getLabel(null)+" : ":"");
				AAttribute attributes = unique.getAttributes(null);
				SdaiIterator attributes_it = attributes.createIterator();
				boolean first = true;
				while (attributes_it.next()) {
					EAttribute attribute = attributes.getCurrentMember(attributes_it);
          EEntity_or_view_definition parent_def = attribute.getParent(null);
					if (first) {
						first = false;
					} else {
						temp += ", ";
					}
          if (parent_def != entity) {
						temp += "SELF\\" + parent_def.getName(null) + "." + attribute.getName(null);
          } else {
						temp += attribute.getName(null);
          }
				}
				result += printTab(temp+";");
			}
			//result += println("");
		}
		return result;
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
			return printTab(printBold(attribute.getName(null)+": ")+result);
		}
		return "";
	}


	// parameter entity_definitions - if the entity is not in that array, it has to be pruned
	private String printSubtypeConstraintLF(ESubtype_expression expression, EEntity_definition [] entity_definitions, int entity_count, EEntity_definition current) throws SdaiException {
		String result = "";
		if (expression.getGeneric_operands(null).getMemberCount() > 1) {
			if (expression instanceof EOneof_subtype_expression) {
				
				String result2 = printOperandsLF(expression, ", ", entity_definitions, entity_count, current);
				if (result2.equals("")) {
					// if result2 is empty, then ONEOF not needed.
				} else
				if (result2.indexOf(',') < 0) {
					// if result contains only one entity, then ONEOF is not needed - just the entity - check if there is "," in the result
					result += result2;
				} else {
					result += "ONEOF (";
					result += result2;
					result += ")";
				}
			} else if (expression instanceof EAndor_subtype_expression) {
				String result3 = printOperandsLF(expression, " ANDOR ", entity_definitions, entity_count, current);
				if (result3.equals("")) {
					// if result3 is empty, then () not needed
				} else
				if (result3.indexOf("ANDOR") < 0) {
					// only one element, why () ?
					result += result3;
				} else {
					result += "(";
					result += result3;
					result += ")";
				}
			} else if (expression instanceof EAnd_subtype_expression) {
				String result4 = printOperandsLF(expression, " AND ", entity_definitions, entity_count, current);
				if (result4.equals("")) {
					// if result4 is empty, then () not needed
				} else
				if (result4.indexOf("AND") < 0) {
					// only one element, why () ?
					result += result4;
				} else {
					result += "(";
					result += result4;
					result += ")";
				}
			}
		} else {
			 // this is just an entity, but may be pruned as well
		   result += printOperandsLF(expression, "", entity_definitions, entity_count, current);
		}
		return result;
	}



	private String printOperandsLF(ESubtype_expression expression, String separator, EEntity_definition [] entity_definitions, int entity_count, EEntity_definition current) throws SdaiException {
		String result = "";
		AEntity oper = expression.getGeneric_operands(null);
		SdaiIterator it_oper = oper.createIterator();
		boolean first = true;
		while (it_oper.next()) {
			EEntity select = oper.getCurrentMemberEntity(it_oper);
			if (select instanceof EEntity_definition) {
				EEntity_definition entity = (EEntity_definition)select;
				if (entityPresent(entity, entity_definitions, entity_count, current)) {
					if (!first) {
						result += separator;
					}
					result += entity.getName(null); // toLowerCase() removed
					first = false;		
				}
			} else if (select instanceof ESubtype_expression) {
				if (!first) {
					result += separator;
				}
				result += printSubtypeConstraintLF((ESubtype_expression)select, entity_definitions, entity_count, current);
				first = false;		
			}
		}
		return result;
	}

	boolean entityPresent(EEntity_definition entity, EEntity_definition [] entities, int entity_count, EEntity_definition current) throws SdaiException {
		// at is an interesting situation - pruning is not needed, but such a case should not happen,
		// and if happens - pruning could correct it.
		// but perhaps just print error message and not prune 
		if (entity == current) {
			System.out.println("ERROR - an entity has itself as its subtype: " + entity.getName(null));
			return true;
		}
		for (int i = 0; i < entity_count; i++) {
			if (entity == entities[i]) {
				return true;
			}
		}
		return false;
	}

	boolean namedTypePresent(ENamed_type entity, EEntity_definition [] entities, int entity_count, EDefined_type [] types, int type_count, ENamed_type current) throws SdaiException {
		// at is an interesting situation - pruning is not needed, but such a case should not happen,
		// and if happens - pruning could correct it.
		// but perhaps just print error message and not prune 
		if (entity == current) {
			// may not be aplicable in this method
//			System.out.println("ERROR - an entity has itself as its subtype: " + entity.getName(null));
			return true;
		}
		for (int i = 0; i < entity_count; i++) {
			if (entity == entities[i]) {
				return true;
			}
		}
		for (int i = 0; i < type_count; i++) {
			if (entity == types[i]) {
				return true;
			}
		}
		return false;
	}

/*


subtype_expression = ONEOF operands - set of entity | subtype_expression

subtype_expression = ANDOR operands - set of entity | subtype_expression

subtype_expression = AND   operands - set of entity | subtype_expresison

ONEOF (entity1, entity2, entity3)  - ONEOF() - nothing, ONEOF (entity1) - entity1

entity1 ANDOR entity2 ANDOR entity3 -  a simpler case - nothing or entity1  is done by itself, still we would like to have a count returned somehow

entity1 AND entity2 AND entity3   - also the same as above

was:
	SUPERTYPE OF ((analytical_model_vector_port ANDOR ONEOF (analog_analytical_model_port, digital_analytical_model_port)))
now:
	SUPERTYPE OF (analytical_model_vector_portONEOF (analog_analytical_model_port, digital_analytical_model_port))








ENTITY subtype_expression ABSTRACT SUPERTYPE OF 
(ONEOF (andor_subtype_expression, and_subtype_expression, oneof_subtype_expression));
	-- deprecated
--  operands : SET [1:?] OF entity_or_subtype_expression;
	generic_operands : SET [1:?] OF entity_or_view_or_subtype_expression;
DERIVE
 	operands : SET [1:?] OF entity_or_subtype_expression := get_operands(generic_operands);
END_ENTITY;

ENTITY andor_subtype_expression
	SUBTYPE OF (subtype_expression);
END_ENTITY;

ENTITY and_subtype_expression
	SUBTYPE OF (subtype_expression);
END_ENTITY;

ENTITY oneof_subtype_expression
	SUBTYPE OF (subtype_expression);
END_ENTITY;

ENTITY sub_supertype_constraint;
	name : OPTIONAL express_id;
	-- deprecated
--	super_type : entity_definition;
	generic_supertype : entity_or_view_definition;
	total_cover : OPTIONAL SET of entity_definition;
	-- supertype_expression in the grammar
	constraint : OPTIONAL subtype_expression;
DERIVE
	super_type : entity_definition := get_entity_definition(generic_supertype);
END_ENTITY;


*/



	private String printSubtypeConstraint(ESubtype_expression expression) throws SdaiException {
		String result = "";
		if (expression.getGeneric_operands(null).getMemberCount() > 1) {
			if (expression instanceof EOneof_subtype_expression) {
				result += "ONEOF (";
				result += printOperands(expression, ", ");
				result += ")";
			} else if (expression instanceof EAndor_subtype_expression) {
				result += "(";
				result += printOperands(expression, " ANDOR ");
				result += ")";
			} else if (expression instanceof EAnd_subtype_expression) {
				result += "(";
				result += printOperands(expression, " AND ");
				result += ")";
			}
		} else {
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
			} else {
				result += separator;
			}
			EEntity select = oper.getCurrentMemberEntity(it_oper);
			if (select instanceof EEntity_definition) {
				EEntity_definition entity = (EEntity_definition)select;
				result += entity.getName(null); // toLowerCase() removed
			} else if (select instanceof ESubtype_expression) {
				result += printSubtypeConstraint((ESubtype_expression)select);
			}
		}
		return result;
	}



	// some internall support stuff
	
	/* Return name of dictionary entity.*/
	private String getDictionaryEntityName(EEntity entity) throws SdaiException {
		String result = "";
		if (entity instanceof ENamed_type) {
			result = ((ENamed_type)entity).getName(null);
		} else if (entity instanceof ESchema_definition) {
			result = ((ESchema_definition)entity).getName(null);
		} else if (entity instanceof EAttribute) {
			result = ((EAttribute)entity).getName(null);
		} else if (entity instanceof EWhere_rule) {
			result = ((EWhere_rule)entity).getLabel(null);
		} else if (entity instanceof EGlobal_rule) {
			result = ((EGlobal_rule)entity).getName(null);
        } else if (entity instanceof EAlgorithm_definition) {          //--VV--
			result = ((EAlgorithm_definition)entity).getName(null);    //--VV--
		} else if (entity instanceof EConstant_definition) {           //--VV--
			result = ((EConstant_definition)entity).getName(null);     //--VV--
		} else if (entity instanceof ESub_supertype_constraint) {
			if (((ESub_supertype_constraint)entity).testName(null)) {
				result = ((ESub_supertype_constraint)entity).getName(null);
			}
		}
		
		return result;
	}

/**Return domain of attribute.*/
	private EEntity getAttrType(EAttribute attribute) throws SdaiException{
		EEntity domain = null;

//System.out.println("<OO>: " + attribute);
		if (attribute instanceof EExplicit_attribute){
			domain = ((EExplicit_attribute)attribute).getDomain(null);
		}
		else if (attribute instanceof EDerived_attribute){
			domain = ((EDerived_attribute)attribute).getDomain(null);
		}
		else if (attribute instanceof EInverse_attribute){
			domain = ((EInverse_attribute)attribute).getDomain(null);
		}
		return domain;
	}

	private String findDocFor(EEntity entity) throws SdaiException {
		if (docModel != null) {
			Aggregate entities = docModel.getInstances();
			SdaiIterator it = entities.createIterator();
			while (it.next()) {
				EDocumentation doc = (EDocumentation)entities.getCurrentMemberObject(it);
				if (doc.getTarget(null) == entity) {
					// return doc.getDescription(null);
					// instead of STRING description now we have LIST OF STRING values.
				  boolean values_present = doc.testValues(null);
				  if (values_present) {
	          A_string value_strings = doc.getValues(null);
	          int number_of_value_strings = value_strings.getMemberCount();
	          if (number_of_value_strings > 0) {
	          	String a_value_string = (String)value_strings.getByIndex(1);
				  		return a_value_string;
				  	}
				  }	
				}
			}
		}
		return "";
	}
	static public String getUpper(String s) {
		return (s.length()>0)?s.substring(0, 1).toUpperCase() + s.substring(1, s.length()).toLowerCase():"";
	}

	
    static private String replaceAll( String text, String original, String replacement) {
        int curr_pos = 0;
        int find_pos = 0;
        StringBuffer sb = new StringBuffer();
        
        while (find_pos >= 0) {
            find_pos = text.indexOf( original, curr_pos);
            if (find_pos >= 0) {
                sb.append( text.substring( curr_pos, find_pos)).append( replacement);
                curr_pos = find_pos + original.length();
            }
            else {
                sb.append( text.substring( curr_pos));
            }
        }
        return sb.toString();
    }

    static private String replaceAll2( String text, String original, String replacement) {
// System.out.println("==XX== 01: original: " + original + ", replacament: " + replacement);
				String original2 = " " + original + " ";
				String replacement2 = " " + replacement + " ";
        int curr_pos = 0;
        int find_pos = 0;
        boolean in_string = false;
        StringBuffer sb = new StringBuffer();
        String text_l = new String(text);
        String true_original = "";
        text_l = text_l.toLowerCase();
//System.out.println("<>XX<> text : " + text);        
//System.out.println("<>XX<> text_l: " + text_l);        
        
        
        while (find_pos >= 0) {
//            find_pos = text.toLowerCase().indexOf( original2, curr_pos);
            find_pos = text_l.indexOf( original2, curr_pos);
            if (find_pos >= 0) {
							true_original = text.substring(find_pos, find_pos + original2.length());
		
// System.out.println("==XX== 02: find_pos: " + find_pos + ", text: " + text + ", curr_pos: " + curr_pos + ", original: " + original);
            	if (found_not_in_string(find_pos, text)) {
                sb.append( text.substring( curr_pos, find_pos)).append( replacement2);
                curr_pos = find_pos + replacement2.length()-1;
            	}
            	else {
                sb.append( text.substring( curr_pos, find_pos)).append(true_original);
                curr_pos = find_pos + original2.length()-1;
            	}
            } else {
            	if (curr_pos > 0) {
	            	sb.append(text.substring(curr_pos+1));
          		} else {
	            	sb.append(text.substring(curr_pos));
          		}
          	}		
            // curr_pos = find_pos + original2.length()-1;
        }
        return sb.toString();
    }
    
  static private boolean found_not_in_string(int match_pos, String text) {
// System.out.println("==XX== 03: match_pos: " + match_pos + ", text: " + text);
    int curr_pos = 0;
    int find_pos = 0;
		int quote_count = 0;
		if (match_pos < 0) {
			return false;
		}
		for (;;) {
     	find_pos = text.indexOf("\'", curr_pos);
// System.out.println("==XX== 04 find_pos: " + find_pos + ", curr_poss: " + curr_pos);
  		if ((find_pos >= 0) && (find_pos < match_pos)) {
  			quote_count++;
  			curr_pos = find_pos + 1;
  		} else {
  			break;
  		}
  	}
  	// now, if quote_count is odd, then inside a string
// System.out.println("==XX== 05 quote_count: " + quote_count);
		if ((quote_count % 2) == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	String format(String input) {
		
		String result;

		// convert keywords to uppercase, input is always normalized to lowercase, so mixed-case keywords can not happen
		// however, we have to begin from the longest strings so as to avoid converting substrings only and getting mixed-case words that are not later detected
		

		result = replaceAll2(input,  "end_subtype_constraint",  "END_SUBTYPE_CONSTRAINT");

		result = replaceAll2(result, "subtype_constraint",      "SUBTYPE_CONSTRAINT"); 

		result = replaceAll2(result, "generic_entity",          "GENERIC_ENTITY"); 

		result = replaceAll2(result, "end_procedure",           "END_PROCEDURE");

		result = replaceAll2(result, "end_constant",            "END_CONSTANT"); 
		result = replaceAll2(result, "end_function",            "END_FUNCTION"); 
		result = replaceAll2(result, "value_unique",            "VALUE_UNIQUE");

		result = replaceAll2(result, "enumeration",             "ENUMERATION"); 

		result = replaceAll2(result, "end_entity",              "END_ENTITY");
		result = replaceAll2(result, "end_repeat",              "END_REPEAT"); 
		result = replaceAll2(result, "end_schema",              "END_SCHEMA"); 
		result = replaceAll2(result, "extensible",              "EXTENSIBLE"); 
		result = replaceAll2(result, "total_over",              "TOTAL_OVER"); 

		result = replaceAll2(result, "aggregate",               "AGGREGATE"); 
		result = replaceAll2(result, "end_alias",               "END_ALIAS"); 
		result = replaceAll2(result, "end_local",               "END_LOCAL"); 
		result = replaceAll2(result, "otherwise",               "OTHERWISE"); 
		result = replaceAll2(result, "procedure",               "PROCEDURE"); 
		result = replaceAll2(result, "reference",               "REFERENCE"); 
		result = replaceAll2(result, "supertype",               "SUPERTYPE"); 

		result = replaceAll2(result, "abstract",                "ABSTRACT"); 
		result = replaceAll2(result, "based_on",                "BASED_ON"); 
		result = replaceAll2(result, "constant",                "CONSTANT"); 
		result = replaceAll2(result, "end_case",                "END_CASE"); 
		result = replaceAll2(result, "end_rule",                "END_RULE"); 
		result = replaceAll2(result, "end_type",                "END_TYPE"); 
		result = replaceAll2(result, "function",                "FUNCTION"); 
		result = replaceAll2(result, "optional",                "OPTIONAL"); 
		result = replaceAll2(result, "value_in",                "VALUE_IN");

		result = replaceAll2(result, "blength",                 "BLENGTH"); 
		result = replaceAll2(result, "boolean",                 "BOOLEAN"); 
		result = replaceAll2(result, "const_e",                 "CONST_E"); 
		result = replaceAll2(result, "generic",                 "GENERIC"); 
		result = replaceAll2(result, "hibound",                 "HIBOUND"); 
		result = replaceAll2(result, "hiindex",                 "HIINDEX"); 
		result = replaceAll2(result, "integer",                 "INTEGER"); 
		result = replaceAll2(result, "inverse",                 "INVERSE"); 
		result = replaceAll2(result, "lobound",                 "LOBOUND"); 
		result = replaceAll2(result, "logical",                 "LOGICAL"); 
		result = replaceAll2(result, "loindex",                 "LOINDEX"); 
		result = replaceAll2(result, "renamed",                 "RENAMED"); 
		result = replaceAll2(result, "rolesof",                 "ROLESOF");
		result = replaceAll2(result, "subtype",                 "SUBTYPE");
		result = replaceAll2(result, "unknown",                 "UNKNOWN");

		result = replaceAll2(result, "binary",                  "BINARY"); 
		result = replaceAll2(result, "derive",                  "DERIVE"); 
		result = replaceAll2(result, "end_if",                  "END_IF"); 
		result = replaceAll2(result, "entity",                  "ENTITY"); 
		result = replaceAll2(result, "escape",                  "ESCAPE");
		result = replaceAll2(result, "exists",                  "EXISTS"); 
		result = replaceAll2(result, "format",                  "FORMAT"); 
		result = replaceAll2(result, "insert",                  "INSERT"); 
		result = replaceAll2(result, "length",                  "LENGTH");
		result = replaceAll2(result, "number",                  "NUMBER"); 
		result = replaceAll2(result, "remove",                  "REMOVE");
		result = replaceAll2(result, "repeat",                  "REPEAT");
		result = replaceAll2(result, "return",                  "RETURN"); 
		result = replaceAll2(result, "schema",                  "SCHEMA"); 
		result = replaceAll2(result, "select",                  "SELECT");
		result = replaceAll2(result, "sizeof",                  "SIZEOF"); 
		result = replaceAll2(result, "string",                  "STRING"); 
		result = replaceAll2(result, "unique",                  "UNIQUE"); 
		result = replaceAll2(result, "typeof",                  "TYPEOF"); 
		result = replaceAll2(result, "usedin",                  "USEDIN"); 

// added temporarily for debugging
//		result = replaceAll2(result, "design",                  "DESIGN"); 

		result = replaceAll2(result, "alias",                   "ALIAS"); 
		result = replaceAll2(result, "andor",                   "ANDOR"); 
		result = replaceAll2(result, "array",                   "ARRAY");
		result = replaceAll2(result, "begin",                   "BEGIN");
		result = replaceAll2(result, "false",                   "FALSE"); 
		result = replaceAll2(result, "fixed",                   "FIXED"); 
		result = replaceAll2(result, "-- local",                   "-- l0cal");
		result = replaceAll2(result, "local",                   "LOCAL");
		result = replaceAll2(result, "log10",                   "LOG10");
		result = replaceAll2(result, "oneof",                   "ONEOF");
		result = replaceAll2(result, "query",                   "QUERY");
		result = replaceAll2(result, "until",                   "UNTIL");
		result = replaceAll2(result, "value",                   "VALUE"); 
		result = replaceAll2(result, "where",                   "WHERE"); 
		result = replaceAll2(result, "while",                   "WHILE");

		result = replaceAll2(result, "acos",                    "ACOS"); 
		result = replaceAll2(result, "asin",                    "ASIN"); 
		result = replaceAll2(result, "atan",                    "ATAN");
		result = replaceAll2(result, "case",                    "CASE");
		result = replaceAll2(result, "else",                    "ELSE"); 
		result = replaceAll2(result, "from",                    "FROM");
		result = replaceAll2(result, "like",                    "LIKE"); 
		result = replaceAll2(result, "list",                    "LIST"); 
		result = replaceAll2(result, "log2",                    "LOG2"); 
		result = replaceAll2(result, "real",                    "REAL"); 
		result = replaceAll2(result, "rule",                    "RULE"); 
		result = replaceAll2(result, "self",                    "SELF"); 
		result = replaceAll2(result, "skip",                    "SKIP"); 
		result = replaceAll2(result, "sqrt",                    "SQRT"); 
		result = replaceAll2(result, "then",                    "THEN"); 
		result = replaceAll2(result, "true",                    "TRUE"); 
		result = replaceAll2(result, "type",                    "TYPE"); 
		result = replaceAll2(result, "with",		                 "WITH");		

		result = replaceAll2(result, "abs",                     "ABS"); 
		result = replaceAll2(result, "and",                     "AND"); 
		result = replaceAll2(result, "bag",                     "BAG"); 
		result = replaceAll2(result, "cos",                     "COS"); 
		result = replaceAll2(result, "div",                     "DIV"); 
		result = replaceAll2(result, "end",                     "END");
		result = replaceAll2(result, "exp",                     "EXP");
		result = replaceAll2(result, "for",                     "FOR"); 
		result = replaceAll2(result, "log",                     "LOG"); 
		result = replaceAll2(result, "mod",                     "MOD"); 
		result = replaceAll2(result, "not",                     "NOT"); 
		result = replaceAll2(result, "nvl",                     "NVL"); 
		result = replaceAll2(result, "odd",                     "ODD"); 
		result = replaceAll2(result, "set",                     "SET"); 
		result = replaceAll2(result, "sin",                     "SIN"); 
		result = replaceAll2(result, "tan",                     "TAN");
		result = replaceAll2(result, "use",                     "USE"); 
		result = replaceAll2(result, "xor",                     "XOR");
		result = replaceAll2(result, "var",                     "VAR"); 

		result = replaceAll2(result, "as",                      "AS"); 
		result = replaceAll2(result, "by",                      "BY"); 
		result = replaceAll2(result, "if",                      "IF");
		result = replaceAll2(result, "in",                      "IN");
		result = replaceAll2(result, "of",                      "OF"); 
		result = replaceAll2(result, "or",                      "OR");
		result = replaceAll2(result, "pi",                      "PI");
		result = replaceAll2(result, "to",                      "TO");

		result = replaceAll(result, " . ",                      ".");
		result = replaceAll(result, ". ",                      ".");
		result = replaceAll(result, " .",                      ".");
		result = replaceAll(result, " ; ",                      "; ");
		result = replaceAll(result, " , ",                      ", ");
		result = replaceAll(result, " ( ",                      "(");
		result = replaceAll(result, " (",                      "(");
		result = replaceAll(result, "( ",                      "(");
		result = replaceAll(result, " ) ",                      ")");
		result = replaceAll(result, " )",                      ")");
		result = replaceAll(result, ") ",                      ")");

		
		return result;
	}
	
	String printIsoNumber(String schema_name) {
		String iso_number = "";
		if (hm_iso_ids != null) {
			iso_number = (String)hm_iso_ids.get(schema_name.toLowerCase());
			if (iso_number == null) {
				iso_number = "";
			}
		}
		if (!iso_number.equals("")) {
			return "" + iso_number + "";
		}
		return "";
	}

	String printIsoNumberComment(String schema_name) {
		String iso_number = "";
		if (hm_iso_ids != null) {
			iso_number = (String)hm_iso_ids.get(schema_name.toLowerCase());
			if (iso_number == null) {
				iso_number = "";
			}
		}
		if (!iso_number.equals("")) {
			return printUnclosedTab("-- " + iso_number + "");
		}
		return "";
	}

	
	
	
	private String printUnclosedTab(String s) {
		return "\t" + s;
	}	
	
	private String printTab(String s) {
		return "\t" + s + "\n";
	}

	private String println(String s) {
		return s + "\n";
	}

	private String println() {
		return "\n";
	}

	private String printBold(String s) {
		// does nothing
		return s;
	}

	private String print(String s) {
		// does nothing
		return s;
	}

	private String printBreak() {
		return "\n";
	}

	private String getComplexName(String name) {
		return name.replace('+', '$');
	}
	


	/* Finds this entity schema to which it belongs. */
	static public ESchema_definition findSchemaForEntity(EEntity entity) throws SdaiException {
		ADeclaration decs = new ADeclaration();
		CDeclaration.usedinDefinition(null, entity, null, decs); //schemas
		SdaiIterator decs_it = decs.createIterator();
		while (decs_it.next()) {
			EDeclaration declaration = decs.getCurrentMember(decs_it);
			if (declaration instanceof ELocal_declaration) {
				return (ESchema_definition)declaration.getParent(null);
			}
		}
		return null;
	}



	static public ESchema_definition findSchemaForAttribute(EAttribute entity) throws SdaiException {

		ADeclaration decs = new ADeclaration();
		CDeclaration.usedinDefinition(null, entity.getParent(null), null, decs); //schemas
		SdaiIterator decs_it = decs.createIterator();
		while (decs_it.next()) {
			EDeclaration declaration = decs.getCurrentMember(decs_it);
			if (declaration instanceof ELocal_declaration) {
				return (ESchema_definition)declaration.getParent(null);
			}
		}
		return null;
	}

	ESchema_definition getSchema_definitionFromModel(SdaiModel sm)
                                                   throws SdaiException {
    if (sm == null) return null;
    Aggregate ia = sm.getEntityExtentInstances(ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    if (iter_inst.next()) {
      ESchema_definition inst = (ESchema_definition) ia.getCurrentMemberObject(iter_inst);

      return inst;
    }

    return null;
  }


    private boolean testRedeclaring(EAttribute attribute) throws SdaiException {
		if (attribute instanceof EExplicit_attribute) {
			return ((EExplicit_attribute)attribute).testRedeclaring(null);
		}
		else if (attribute instanceof EDerived_attribute) {
			return ((EDerived_attribute)attribute).testRedeclaring(null);
		}
		else if (attribute instanceof EInverse_attribute) {
			return ((EInverse_attribute)attribute).testRedeclaring(null);
		}
		return false;
	}

	private EAttribute getRedeclaring(EAttribute attribute) throws SdaiException {
		if (attribute instanceof EExplicit_attribute) {
			return ((EExplicit_attribute)attribute).getRedeclaring(null);
		}
		else if (attribute instanceof EDerived_attribute) {
			return (EAttribute)((EDerived_attribute)attribute).getRedeclaring(null);
		}
		else if (attribute instanceof EInverse_attribute) {
			return ((EInverse_attribute)attribute).getRedeclaring(null);
		}
		return null;
	}


	// added by RR
	AExplicit_attribute getExplicit_attributes(EEntity_definition ed) throws SdaiException {
		AExplicit_attribute axa = new AExplicit_attribute();

		CompareAttributes ce = new CompareAttributes();
		TreeSet axas = new TreeSet(ce);

    ASdaiModel domain = null; 
		AAttribute aa = ed.getAttributes(null, domain);
		SdaiIterator iaa = aa.createIterator();
		while (iaa.next()) {
			EAttribute an_a = (EAttribute)aa.getCurrentMemberObject(iaa);  
			if (an_a instanceof EExplicit_attribute) {
				EEntity_definition ped = (EEntity_definition)an_a.getParent(null);
				if (ped == ed) {
					// int order = an_a.getOrder(null);
          // if (order != Integer.MIN_VALUE) {			 
			 	 	if (an_a.testOrder(null)) {
			 	 		axas.add(an_a);
					}
				}
			}
		}
		Iterator iter = axas.iterator();
		int i_part = 0;
		while	(iter.hasNext())	{
			EExplicit_attribute xa	=	(EExplicit_attribute)iter.next();
			axa.addByIndex(axa.getMemberCount() + 1, xa);
		}
		return axa;
	}

	public static ESchema_definition findSchema(SdaiModel model) throws SdaiException {
		if (model.getMode() == 0) {
			model.startReadOnlyAccess();
		}
		Aggregate instances = model.getInstances(ESchema_definition.class);
		SdaiIterator it = instances.createIterator();
		if (it.next()) {
			return (ESchema_definition)instances.getCurrentMemberObject(it);
		} else {
			return null;
		}
	}


  PrintWriter getPrintWriter(String file_name) { 
//		if (output_directory != null) {
//			file_name = output_directory + File.separator + file_name;
//		}
try {
    FileOutputStream fos = new FileOutputStream(file_name);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);
    return pw;
	} catch (java.io.IOException exc) {
				exc.printStackTrace(System.err);
				return null;
	}

 }


  String get_time() {
    GregorianCalendar cal = new GregorianCalendar();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    boolean month_one_digit;

    if (month < 10) {
      month_one_digit = true;
    } else {
      month_one_digit = false;
    }

    int day = cal.get(Calendar.DAY_OF_MONTH);
    boolean day_one_digit;

    if (day < 10) {
      day_one_digit = true;
    } else {
      day_one_digit = false;
    }

    int hour = cal.get(Calendar.HOUR_OF_DAY);
    boolean hour_one_digit;

    if (hour < 10) {
      hour_one_digit = true;
    } else {
      hour_one_digit = false;
    }

    int minute = cal.get(Calendar.MINUTE);
    boolean minute_one_digit;

    if (minute < 10) {
      minute_one_digit = true;
    } else {
      minute_one_digit = false;
    }

    int second = cal.get(Calendar.SECOND);
    boolean second_one_digit;

    if (second < 10) {
      second_one_digit = true;
    } else {
      second_one_digit = false;
    }

    String time_stamp = year + "/";

    if (month_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + month + "/";

    if (day_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + day + " ";

    if (hour_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + hour + ":";

    if (minute_one_digit) {
      time_stamp = time_stamp + "0";
    }

    time_stamp = time_stamp + minute + ":";

    if (second_one_digit) {
      time_stamp = time_stamp + "0";
    }

    return time_stamp + second;
  }

	private static boolean includeSchemaModels(SdaiRepository repo) throws SdaiException {
		// here we take associated models from the schema instance with that name
		// associated models are all the models that have definitions in declarations in the specified schema, i.e., 
		// all the models that are needed for the schema
		// it may be wrong implementation, by the way.
		ASchemaInstance sis= repo.getSchemas();
    SdaiIterator iter_sis = sis.createIterator();
    boolean specified_schema_found = false;
    while (iter_sis.next()) {
	    SchemaInstance si = sis.getCurrentMember(iter_sis);
      String si_name = si.getName();
      if (si_name.equalsIgnoreCase(specified_schema)) {
				specified_schema_found = true;
				ASdaiModel associates = si.getAssociatedModels();
        if (associates.getMemberCount() <= 0) {
					break;
				}
			  SdaiIterator iter_model = associates.createIterator();
        while (iter_model.next()) {
	        SdaiModel associate = associates.getCurrentMember(iter_model);
					String associate_name = associate.getName();
					include_models.add(associate_name);
					printIfAsked(associate_name.substring(0, associate_name.length() - 16).toLowerCase());
				}
				break;
			}
		}
		if (!specified_schema_found) {
			return false;
		}
		return true;
		//SchemaInstance si = asi.getByIndex(0);
	}

	private static boolean includeTopSchemaModels(SdaiRepository repo) throws SdaiException {
		 // here we go recursively through interface_specifications and add all the models
		 // it may be not the same as including assotiated models of the schema instance.
		 // this set of models may include models that are not needed for the specified schema but only needed for each other
		 // BTW, perhaps associated models of schema instances are wrongly implemented?
		
		/* 
				get the model for the specified schema, 
				get its interface_specifications,
				go throug each of the specifications and recursively do the same for each schema,
				including each such model into include_models set
		*/
		
//System.out.println("IN TOP");

		ASdaiModel models = repo.getModels();

		SdaiIterator iter = models.createIterator();
		HashSet visited_models = new HashSet();
		boolean top_schema_found = false;
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
      if (model.getMode() == SdaiModel.NO_ACCESS) {
       	model.startReadOnlyAccess();
      }
			String name = model.getName();
			if (name.equalsIgnoreCase(top_schema.toUpperCase() + "_DICTIONARY_DATA")) {
				// should work not only for top schema
//System.out.println("IN TOP - model found, starting recursion: " + model);
				top_schema_found = true;
				recurseSchemasByInterfaceSpecifications(model, visited_models);
				break;
			}
		}
		if (!top_schema_found) {
			return false;
		}
		return true;
	}

	private static void recurseSchemasByInterfaceSpecifications(SdaiModel current_model, HashSet visited_models) throws SdaiException {
		if (!visited_models.add(current_model)) {
			// this model is already visited, cyclic case
			return;
		}
		// include the current model to the list of schemas for which express has to be generated
		String model_name = current_model.getName();
		include_models.add(model_name);
		printIfAsked(model_name.substring(0, model_name.length() - 16).toLowerCase());
		AInterface_specification specifications = (AInterface_specification)current_model.getInstances(EInterface_specification.class);	
		SdaiIterator iter = specifications.createIterator();
		while (iter.next()) {
			EInterface_specification next_specification = (EInterface_specification)specifications.getCurrentMemberObject(iter);
      ESchema_definition next_sd = (ESchema_definition)next_specification.getForeign_schema(null);
			SdaiModel next_model = next_sd.findEntityInstanceSdaiModel();
      recurseSchemasByInterfaceSpecifications(next_model, visited_models);
		}		
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
					String a_model = st.sval.toUpperCase();
					if (!a_model.endsWith("_DICTIONARY_DATA")) {
						a_model += "_DICTIONARY_DATA";
						
					}
					include_models.add(a_model.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				}		
			}
    } // try

    //  catch (FileNotFoundException e) 
    catch (IOException e) {
      System.out.println("the file " + file_name + " with the models to include not found.");
			
      return;
    }
	}
	
	
	private static void excludeInternalDictionaryModels() {
	
	 exclude_models.add("EXTENDED_DICTIONARY_SCHEMA_DICTIONARY_DATA");
	 exclude_models.add("MAPPING_SCHEMA_DICTIONARY_DATA");
	 exclude_models.add("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA");
	 exclude_models.add("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA");
	
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
					String a_model = st.sval.toUpperCase();
					if (!a_model.endsWith("_DICTIONARY_DATA")) {
						a_model += "_DICTIONARY_DATA";
						
					}
					exclude_models.add(a_model.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				}		
			}
    } // try

    //  catch (FileNotFoundException e) 
    catch (IOException e) {
      System.out.println("the file " + file_name + " with the models to exclude not found.");
			
      return;
    }


	}

	static private void printIfAsked(String str) {
		if (flag_print_schemas) {
			if (flag_printed_schema < 1) {	
				System.out.println("--- inluded schemas: ---\n");
			}
			flag_printed_schema++;
			String nothing = "";
			if (flag_printed_schema > 9999) nothing = "";
			else 
			if (flag_printed_schema > 999) nothing = " ";
			else 
			if (flag_printed_schema > 99) nothing = "  ";
			else 
			if (flag_printed_schema > 9) nothing = "   ";
			else nothing = "    ";
			System.out.println(nothing + flag_printed_schema + ": " + str);
		}
	}


	// not needed and, anyway, stupid and unnecessary   
	private boolean haveVector(Vector v, Object o) {
		for (int i = 0; i < v.size(); i ++) {
			if (v.elementAt(i).equals(o)) {
				return true;
			}
		}
		return false;
	}


	static void readIsoNumbersOfSchemas(String iso_file_name) {
		
		// commenting this out just to make sure there is no unwanted caching
		// if (hm_iso_numbers != null) return;

  	final int TK_START_LINE = 0;
  	final int TK_LONG = 1;
  	final int TK_SHORT = 2;
  	final int TK_COMMA = 3;

		hm_iso_numbers = new HashMap();

    try {
      FileInputStream ins = new FileInputStream(iso_file_name);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.wordChars(' ', ' ');
      st.wordChars('-', '-');
      st.wordChars('/', '/');
      st.wordChars('?', '?');
      st.ordinaryChar(',');
      st.commentChar('#');

      int status = TK_START_LINE;
      String current_schema_name = null;
      String current_schema_iso_number = null;

      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();
//System.out.println("<<>><> status: " + status + ", type: " + st.ttype + ", value: " + st.sval);
        if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_schema_name = st.sval.toLowerCase();
          status = TK_LONG;
        } else if ((status == TK_LONG) && (st.ttype == ',')) {
          status = TK_COMMA;
        } else if ((status == TK_COMMA) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_schema_iso_number = st.sval;
          status = TK_SHORT;
					hm_iso_numbers.put(current_schema_name, current_schema_iso_number);
//System.out.println("<<>> schema: " + current_schema_name + ", number: " +  current_schema_iso_number);       	
        } else if (((status == TK_SHORT) && (st.ttype == StreamTokenizer.TT_EOL)) || 
                       (st.ttype == StreamTokenizer.TT_EOF)) {
          // current reading completed. Now, use it.
          if (st.ttype == StreamTokenizer.TT_EOF) {
            status = TK_START_LINE;

            break;
          } else if (st.ttype == StreamTokenizer.TT_EOL) {
            // ok, next complex entity
            status = TK_START_LINE;
          }
        } else if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_EOL)) {
        } else {
          System.out.println("ERROR in input file, line: " + st.lineno());

          break;
        }
      }

    } // try
    catch (IOException e) {
			// no schema - no iso numbers
      return;
   }
  }

//	public static boolean readIsoIdsAndPartNamesOfSchemas(String iso_file_name, MessageConsoleStream constream) {
	static boolean readIsoIdsAndPartNamesOfSchemas(String iso_file_name) {


		final int TK_START             = 0;
		final int TK_SCHEMA_NAME       = 1;
		final int TK_COMMA_1           = 2;
		final int TK_ISO_ID            = 3;
		final int TK_COMMA_2           = 4;
		// final int TK_PART_NAME         = 5;
		// final int TK_COMMA_OTHER       = 6;
		// final int TK_WORD_OTHER        = 7;
		final int TK_SKIPPING_THE_LINE = 5;
		
		
		
		// hm_iso_ids = new HashMap();
		// hm_part_names = new HashMap();

/*		
		if (hm_iso_ids == null) {
			hm_iso_ids = new HashMap();
			hm_part_names = new HashMap();
		} else {
			return true;
		}
*/

		hm_iso_ids = new HashMap();
		hm_part_names = new HashMap();

		try {

			FileInputStream ins = new FileInputStream(iso_file_name);
			InputStreamReader isr = new InputStreamReader(ins);
			StreamTokenizer st = new StreamTokenizer(isr);

			st.eolIsSignificant(true);
			st.slashSlashComments(true);
			st.slashStarComments(false);

			// st.wordChars(0, 31); // these are Ctrl characters, leaving them alone
			// st.wordChars(32, 43);
			st.wordChars(32, 34);
			st.commentChar('#'); // 35
			//st.wordChars('#', '#');
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
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// an empty line - ignore, skip it.
						// do absolutely nothing, status remains the same (TK_START) for the next line
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						break;
					} else {
						// something else (probably a comma) at the begining of the line is so wrong that the line is ignored and skipped, just like a comment line #......
						// but a warning is printed
						status = TK_SKIPPING_THE_LINE;
						if (st.ttype == ',') {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a comma - IGNORED");
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with a number: " + st.nval + " - IGNORED");
						} else {
							// what can it be? impossible?
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " starts with not sure what: " + st.toString() + "  - IGNORED");
						}
					
					}
				} else
				if (status == TK_SCHEMA_NAME) {
					if (st.ttype == ',') {
						status = TK_COMMA_1;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// seems that there is nothing after the schema name - ignore, skip it, but print a warning
							System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " contains only the schema name - IGNORED");
							status = TK_START;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
							System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name - IGNORED");
							status = TK_START; // not really needed
							current_schema_name = null; // not really needed
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
							break;
					} else {
						// it is not even possible - if schema name is not followed by comma, then what can it be?
						if (st.ttype == StreamTokenizer.TT_WORD) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.sval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by " + st.nval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name followed by not sure what: " + st.toString() + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
							current_schema_name = null;
						}
					}
				} else
				if (status == TK_COMMA_1) {
					if (st.ttype == StreamTokenizer.TT_WORD) {
						current_schema_iso_id = st.sval.trim();
						status = TK_ISO_ID;
					} else
					if (st.ttype == ',') {
						// the iso_id is absent, but perhaps part_name is present
						status = TK_COMMA_2;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// incomplete line, no iso_id nor part_name, print a warning
						System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " contains only the schema name and a comma - IGNORED");
						status = TK_START;
						current_schema_name = null;
						current_schema_iso_id = null;  // not really needed
						current_schema_part_name = null; // not really needed
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						// incomplete line, no iso_id nor part_name, print a warning
						System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " contains only the schema name and a comma - IGNORED");
						status = TK_START; // not really needed
						current_schema_name = null; // not really needed
						current_schema_iso_id = null;  // not really needed
						current_schema_part_name = null; // not really needed
						break;
					} else {
						// this should not be even possible
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by " + st.nval + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - schema name and a comma followed by not sure what: " + st.toString() + " - IGNORED");
							status = TK_SKIPPING_THE_LINE;
							current_schema_name = null;
							current_schema_iso_id = null;  // not really needed
							current_schema_part_name = null; // not really needed
						}
					}
				} else
				if (status == TK_ISO_ID) {
					if (st.ttype == ',') {
						status = TK_COMMA_2;
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// iso_id present, part_name absent, ok, add it
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (1) - schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: ABSENT");
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_START;
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						// iso_id present, part_name absent, ok, add it
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (2)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: ABSENT");
						current_schema_name = null; // not really needed
						current_schema_iso_id = null; // not really needed
						current_schema_part_name = null; // not really needed
						status = TK_START; // not really needed
						break;
					} else {
						// something seriosly wrong here, should be not possible, but we can store the line assuming that part_name is absent, but print a warning
						if (st.ttype == StreamTokenizer.TT_WORD) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.sval + " - assumed part_name absent");
						} else 
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by " + st.nval + " - assumed part_name absent");
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - iso_id followed by not sure what: " + st.toString() + " - assumed part_name absent");
						}
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (3)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: ABSENT, the line ends BADLY");
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_SKIPPING_THE_LINE;
					}
				} else 
				if (status == TK_COMMA_2) {
					if (st.ttype == StreamTokenizer.TT_WORD) {
						current_schema_part_name = st.sval.trim();
						// we have now the complete info, can add now.
						if (current_schema_iso_id == null) {
							// iso_id is absent, part_name - present
							current_schema_iso_id = "";
						} 
						previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
						if (previous != null) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
							previous = null;
						}
						hm_part_names.put(current_schema_name, current_schema_part_name); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (4)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						// status = TK_PART_NAME;
						status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_PART_NAME as well
					} else
					if (st.ttype == ',') {
						// the part_name is absent, but perhaps the iso_id part is present
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (5)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name is ABSENT ");
						} else {
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						// status = TK_COMMA_3;
						status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_COMMA_3 as well
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						// the part_name is absent, but perhaps the iso_id part is present
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (6)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						} else {
							
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_START;
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						// the part_name is absent, but perhaps the iso_id part is present
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (7)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name: " + current_schema_part_name);
						} else {
							
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null; // not really needed
						current_schema_iso_id = null; // not really needed
						current_schema_part_name = null; // not really needed
						status = TK_START; // not really needed
						break;
					} else {
						// this should not happen, but just in case
						// whatever happened, part_name is absent, add the line
						if (st.ttype == StreamTokenizer.TT_NUMBER) {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by " + st.nval + " - assumed part_name absent");
						} else {
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - 2nd comma followed by not sure what: " + st.toString() + " - assumed part_name absent");
						}
						if (current_schema_iso_id != null) {
							// add this line
							previous = hm_iso_ids.put(current_schema_name, current_schema_iso_id);
							if (previous != null) {
								System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - a duplicate line for schema: " + current_schema_name + ", previous values replaced");
								previous = null;
							}
							hm_part_names.put(current_schema_name, ""); // because we are doing it in pairs, no need to check the result again
//System.out.println("LINE COMPLETED (8)- schema: "  + current_schema_name + ", iso_id: " + current_schema_iso_id + ", part_name is ABSENT ");
						} else {
							// both iso_id and part_name are absent, so no need to add this line, but perhaps it makes sense to print a warning
							System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - both iso_id and part_name are absent, the line is ignored");
						}
						current_schema_name = null;
						current_schema_iso_id = null;
						current_schema_part_name = null;
						status = TK_SKIPPING_THE_LINE; // perhaps we could have done it with TK_COMMA_3 as well
					}
				} else
				if (status == TK_SKIPPING_THE_LINE) {
					if (st.ttype == StreamTokenizer.TT_EOL) {
						status = TK_START;
						current_schema_name = null; // should not be needed
						current_schema_iso_id = null;  // should not be needed
						current_schema_part_name = null; // should not be needed
					} else 
					if (st.ttype == StreamTokenizer.TT_EOF) {
						status = TK_START; // not really needed
						current_schema_name = null; // should not be needed
						current_schema_iso_id = null;  // should not be needed
						current_schema_part_name = null; // should not be needed
						break;
					} 
				} else {
					status = TK_START;
					current_schema_name = null; 
					current_schema_iso_id = null;  
					current_schema_part_name = null; 
					if (st.ttype == StreamTokenizer.TT_WORD) {
						System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: " + st.sval);
					} else
					if (st.ttype == StreamTokenizer.TT_NUMBER) {
						System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: " + st.nval);
					} else
					if (st.ttype == StreamTokenizer.TT_EOL) {
						System.out.println("ISO_DB WARNING! line: " + (st.lineno()-1) + " - something completely wrong with this line, ignored, current token: EOL");
					} else
					if (st.ttype == StreamTokenizer.TT_EOF) {
						System.out.println("ISO_DB WARNING! (last) line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: EOF");
						break;
					} else
					if (st.ttype == ',') {
						System.out.println("ISO_DB WARNING! line: " + st.lineno() + " - something completely wrong with this line, ignored, current token: comma");
					}
				}
				 
			} // while
      return true;
		} // try
		catch (IOException e) {
			// no schema - no iso numbers
			//System.out.println("ISO_DB WARNING! - file not found: " + iso_file_name);
			hm_iso_ids = null;
			hm_part_names = null;
		
			return false;
	 }
	}




/**Finds documentation model for model.*/
	private SdaiModel findDocModel(ASdaiModel models, SdaiModel mod) throws SdaiException {
		SdaiModel result = null;
		SdaiIterator it = models.createIterator();
		while (it.next()) {
			if (models.getCurrentMember(it).getName().equals("_DOCUMENTATION_"+mod.getName().substring(0, mod.getName().lastIndexOf("_DICTIONARY_DATA")))) {
				result = models.getCurrentMember(it);
				return result;
			}
		}
		return result;
	}
/**Finds express model for model.*/
	private SdaiModel findExpressModel(SdaiRepository repo, EEntity inst) throws SdaiException {
//System.out.println("findExpressModel - inst: " + inst);
		ASdaiModel models = repo.getModels();
		SdaiModel mod = inst.findEntityInstanceSdaiModel();
//System.out.println("findExpressModel - its model: " + mod);
		SdaiModel result = null;
		SdaiIterator it = models.createIterator();
		while (it.next()) {
			if (models.getCurrentMember(it).getName().equals("_EXPRESS_"+mod.getName().substring(0, mod.getName().lastIndexOf("_DICTIONARY_DATA")))) {
				result = models.getCurrentMember(it);
				if (result.getMode() == SdaiModel.NO_ACCESS) {
					result.startReadOnlyAccess();
				}
				
				return result;
			}
		}
		return result;
	}



	class SpecSorterBySchema implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
		    EInterface_specification e1 = (EInterface_specification)o1;
		    EInterface_specification e2 = (EInterface_specification)o2;

		    String e1_schemaName = e1.getForeign_schema(null).getName(null);
		    String e2_schemaName = e2.getForeign_schema(null).getName(null);
			

		    return e1_schemaName.compareToIgnoreCase(e2_schemaName);
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
		    EDeclaration e1 = (EDeclaration)o1;
		    EDeclaration e2 = (EDeclaration)o2;

		    String e1_name = getDictionaryEntityName(e1.getDefinition(null));

		    String e2_name = getDictionaryEntityName(e2.getDefinition(null));

				return e1_name.compareTo(e2_name);

		}
		catch (SdaiException exc)
		    {
			exc.printStackTrace(System.err);
			return 0;
		    }
	    }
	}

	class SorterForModels implements Comparator {
	    public int compare(Object o1, Object o2) {
		try {
		    SdaiModel m1 = (SdaiModel)o1;
		    SdaiModel m2 = (SdaiModel)o2;
		    return findSchema(m1).getName(null).compareToIgnoreCase(findSchema(m2).getName(null));
		}
		catch (SdaiException exc)
		    {
			exc.printStackTrace(System.err);
			return 0;
		    }
	    }
	}

	class SorterForEntities  implements Comparator {
	    public int compare(Object o1, Object o2) {
		try {
		    EEntity e1 = (EEntity)o1;
		    EEntity e2 = (EEntity)o2;	    
		    int result = getDictionaryEntityName(e1).compareToIgnoreCase(getDictionaryEntityName(e2));
				if (result == 0) {
			    String e1_schemaName = findSchemaForEntity(e1).getName(null);
			    String e2_schemaName = findSchemaForEntity(e2).getName(null);
			    result = e1_schemaName.compareToIgnoreCase(e2_schemaName);
				}
				return result;
		}
		catch (SdaiException exc)
		    {
			exc.printStackTrace(System.err);
			return 0;
		    }
	    }
	}


	class SorterForAttributes  implements Comparator {
	    public int compare(Object o1, Object o2) {
		try {
		    EAttribute e1 = (EAttribute)o1;
		    EAttribute e2 = (EAttribute)o2;	    
		    int result = getDictionaryEntityName(e1).compareToIgnoreCase(getDictionaryEntityName(e2));
				if (result == 0) {
			    String e1_schemaName = findSchemaForAttribute(e1).getName(null);
			    String e2_schemaName = findSchemaForAttribute(e2).getName(null);
			    result = e1_schemaName.compareToIgnoreCase(e2_schemaName);
				}
				return result;
		}
		catch (SdaiException exc)
		    {
			exc.printStackTrace(System.err);
			return 0;
		    }
	    }
	}


class CompareAttributes implements Comparator {

	public int compare(Object a1,Object a2) {
		// int order1 = Integer.MIN_VALUE, order2 = Integer.MIN_VALUE;
	  Integer order1 = null, order2 = null;
		try {
			order1 = new Integer(((jsdai.SExtended_dictionary_schema.EAttribute)a1).testOrder(null)?((jsdai.SExtended_dictionary_schema.EAttribute)a1).getOrder(null):Integer.MAX_VALUE);
			order2 = new Integer(((jsdai.SExtended_dictionary_schema.EAttribute)a2).testOrder(null)?((jsdai.SExtended_dictionary_schema.EAttribute)a2).getOrder(null):Integer.MAX_VALUE);
		} catch (jsdai.lang.SdaiException ex) {
			if (order1 == null) order1 = new Integer(Integer.MAX_VALUE);
			if (order2 == null) order2 = new Integer(Integer.MAX_VALUE);
		}
		return order1.compareTo(order2);
	}

	public boolean equals(Object obj) {
		return false;
	}
} 

class SortWhereRules implements Comparator {

	public int compare(Object a1,Object a2) {
	  Integer order1 = null, order2 = null;
		try {
			order1 = new Integer(((jsdai.SExtended_dictionary_schema.EWhere_rule)a1).getOrder(null));
			order2 = new Integer(((jsdai.SExtended_dictionary_schema.EWhere_rule)a2).getOrder(null));
		} catch (jsdai.lang.SdaiException ex) {
		}
		return order1.compareTo(order2);
	}

	public boolean equals(Object obj) {
		return false;
	}
} 



}
