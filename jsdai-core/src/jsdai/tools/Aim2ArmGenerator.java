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

package jsdai.tools;

/**

-verbose
	prints more information

-debug
	prints even more information

-schema schema_name
	AIM schema
	default: electronic_assembly_interconnect_and_packaging_design
  
-model application_model_schema_name
	AIM schema name of the application model with the population used to generate ARM 
	default: electronic_assembly_interconnect_and_packaging_design

-p21_in p21_in_file_name
	the p21 file with the AIM application data
	
-p21_out p21_out_file_name
	the p21 file with the generated ARM data	

-p21 p21_file_name
	the p21 file with ExpresCompilerRepo contents, perhaps for debugging

*/

//import jsdai.dictionary.*;
// import jsdai.SExtended_dictionary_schema.*;
// import jsdai.SMapping_schema.*;
import java.io.*;
import java.util.*;
import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;

public class Aim2ArmGenerator {

	static final String rg_version_minor     = "0";
	static final String rg_version_middle     = "9";
	static final String rg_build     = "002";
	static final String rg_date      = "2003-10-22";
	static final String rg_version_major   = "0";
	static final String rg_title     = "JSDAI(TM) AIM to ARM Generator \n\t\tfor generating ARM from AIM and from mapping data";
	static final String rg_copyright = "Copyright (C) 2003 LKSoftWare GmbH";

	static int flag_debug_detail_level = 4;
	static boolean flag_verbose = false;
	static boolean flag_debug = false;
	static boolean flag_comments = false;
	static boolean flag_warnings = false;
	static boolean flag_errors = true;
	static boolean flag_internal_errors = false;
	static boolean flag_print_array = true;
	static boolean flag_p21 = false;
	static boolean flag_p21_out = false;
	static boolean flag_commit = true;
	static boolean flag_on_demand = false;
	static boolean flag_one_entity_mapping_alternative_only = false;
	static boolean flag_one_attribute_mapping_alternative_only = false;
	static boolean flag_disable_attributes = false;
	static boolean flag_one_attribute_only = false;
	static boolean flag_strong_only = false;

	String ARM_entity_name_requested = null;

  final int NR_ATTRIBUTE = 0;
	final int NR_ATTRIBUTE_MAPPING_ALTERNATIVE = 0;
  final int NR_ENTITY_MAPPING_ALTERNATIVE = 0;
	final int NR_ENTITY_MAPPING_START = 6;
	final int NR_ENTITY_MAPPING_END   = 6;
	boolean range_only = true;

	static EEntity [] instance_array;
	final int INSTANCE_ARRAY_INITIAL_SIZE = 1024;
	final int INSTANCE_ARRAY_SIZE_STEP    = 1024;
	int instance_array_count;
	int constraint_level;

  static Integer START_MARK = new Integer(1);

	static String package_name = "SElectronic_assembly_interconnect_and_packaging_design";
	static String schema_name = "electronic_assembly_interconnect_and_packaging_design";
	// String ARM_name = "AP210_ARM";
	String ARM_name = null;
	// String AIM_name = "ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN";
	String AIM_name = null;
	//	String AIM_app_name = "ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN";
	String AIM_app_name = null;
	String AIM_schema_name;
	// String MAP_name = "AP210_ARM";
	String MAP_name = null;
	SdaiModel global_map_model;
	static String physical_file = "generic.pf";
	static String aim_physical_file = "aim.pf";
	static String arm_physical_file = "arm.pf";
	SdaiModel aim_application_model;
	SdaiRepository application_repository;
	ASdaiModel application_domain = null;
	Aim2ArmGeneratorSorterForInstances sorter;
	// PrintfFormat formated_int;
  static long total_start_time, total_end_time, total_duration;
  static long wave_start_time, wave_end_time, wave_duration;
  static long processing_start_time, processing_end_time, processing_duration;
  static long generating_start_time, generating_end_time, generating_duration;
	SdaiContext _context = null;
	SdaiModel work = null;
	
	EDefined_type [] select_path;
	

	public Aim2ArmGenerator(){
	}

	public static final void main(String args[]) throws SdaiException, IOException  {

		Aim2ArmGenerator ge = new Aim2ArmGenerator();

		boolean flag_many_on_demand_active = false;
		
		// if both switches -entity and -entities are present simultaneuosly, the entity provided with -entity switch is ignored
	
		for (int ihi = 0; ihi < args.length; ihi++) {

			if (args[ihi].equalsIgnoreCase("-verbose")) flag_verbose = true;
			if (args[ihi].equalsIgnoreCase("-debug")) flag_debug = true;
			if (args[ihi].equalsIgnoreCase("-comments")) flag_comments = true;
			if (args[ihi].equalsIgnoreCase("-no_binary")) flag_commit = false;

			if (args[ihi].equalsIgnoreCase("-ARM")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("An ARM schema name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("An ARM schema name must follow the " + args[ihi-1] + " switch");
					return;
				}
				ge.ARM_name = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-AIM")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("An AIM schema name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("An AIM schema name must follow the " + args[ihi-1] + " switch");
					return;
				}
				ge.AIM_name = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-map")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A map name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A map name must follow the " + args[ihi-1] + " switch");
					return;
				}
				ge.MAP_name = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-schema")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A schema name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A schema name must follow the " + args[ihi-1] + " switch");
					return;
				}
				package_name = "S" + args[ihi].substring(0, 1).toUpperCase() + args[ihi].substring(1).toLowerCase();
				schema_name = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-model")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("The schema name of an application model must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("The schema name of an application model must follow the " + args[ihi-1] + " switch");
					return;
				}
				ge.AIM_app_name = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-p21_in")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A p21 file name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A p21 file name must follow the " + args[ihi-1] + " switch");
					return;
				}
				aim_physical_file = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-p21_out")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A file name must follow " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A file name must follow " + args[ihi-1] + " switch");
					return;
				}
				arm_physical_file = args[ihi];
				flag_p21_out = true;
			} else
			if (args[ihi].equalsIgnoreCase("-p21")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A file name must follow " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A file name must follow " + args[ihi-1] + " switch");
					return;
				}
				physical_file = args[ihi];
				flag_p21 = true;
			}
			
		}  // for - loop through all command line parameters and switches

		if (aim_physical_file == null) {
			System.out.println("physical file name with AIM input data must be provided");
			System.out.println("USAGE: java Aim2ArmGenerator -p21_in aim_physical_file_name");
			return;
		} else 


		System.out.println("");
		System.out.println(rg_title);
		System.out.println(rg_copyright);
		System.out.println("version " + rg_version_major + "." + rg_version_middle + "." + rg_version_minor + " build " + rg_build + ", " + rg_date );
		System.out.println("-----------------------------------------------------------------------");




		
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();
//		SdaiTransaction trans = session.startTransactionReadOnlyAccess();
		
		SdaiTransaction trans = session.startTransactionReadWriteAccess();
		
		ge.application_repository = session.importClearTextEncoding("", aim_physical_file, null);
		ge.application_domain = ge.application_repository.getSchemas().getAssociatedModels();
		

		ASdaiModel models = ge.application_repository.getModels();

		SdaiIterator iter = models.createIterator();

		boolean model_found = false;

	// if AIM_app_name is null, take the first model from the p21 input file
	// otherwise (if the name provided by a command line switch -model), search for that specific model

		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			String model_name = model.getUnderlyingSchema().getName(null);
	ge.printDebug("model: " + model_name + ", aim name: " + ge.AIM_name);			
			if (ge.AIM_app_name == null) {
				model_found = true;
				ge.aim_application_model = model;
				ge.AIM_app_name = model_name;
				break;
			} else {
				if (model_name.equalsIgnoreCase(ge.AIM_app_name)) {
					model_found = true;
					ge.aim_application_model = model;
	  	  	if (ge.aim_application_model.getMode() == SdaiModel.NO_ACCESS) {
  	  			ge.aim_application_model.startReadOnlyAccess();
    			}
					break;
				}
			}		
		}
		if (!model_found) {
			ge.printError("specified application model not found: " + ge.AIM_name);
			trans.endTransactionAccessAbort();
			ge.application_repository.closeRepository();
			ge.application_repository.deleteRepository();
			session.closeSession();
			return;
		}
	
		if (ge.AIM_name == null) {
			ge.AIM_name = ge.AIM_app_name;
// System.out.println("AIM name: " + ge.AIM_name);
		}
		if (ge.ARM_name == null) {
			if (ge.AIM_name.equalsIgnoreCase("ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN")) {
				ge.ARM_name = "AP210_ARM";
			} else
			if (ge.AIM_name.equalsIgnoreCase("AUTOMOTIVE_DESIGN")) {
				ge.ARM_name = "AP214_ARM";
			} else
			if (ge.AIM_name.equalsIgnoreCase("ELECTROTECHNICAL_DESIGN")) {
				ge.ARM_name = "AP212_ARM";
			} else {
				ge.ARM_name = "AP210_ARM"; // default value
				ge.printError("unkonwn aim schema");
			}
		}
 		if (ge.MAP_name == null) {
			if (ge.AIM_name.equalsIgnoreCase("ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN")) {
				ge.MAP_name = "AP210_ARM";	
			} else
			if (ge.AIM_name.equalsIgnoreCase("AUTOMOTIVE_DESIGN")) {
				ge.MAP_name = "AP214_ARM";
			} else
			if (ge.AIM_name.equalsIgnoreCase("ELECTROTECHNICAL_DESIGN")) {
				ge.MAP_name = "AP212_ARM";
			} else {
				ge.MAP_name = "AP210_ARM"; // default value
				ge.printError("unkonwn aim schema");
			}
 		}
		ge.ARM_name += "_DICTIONARY_DATA";
		ge.AIM_name += "_DICTIONARY_DATA";
		ge.MAP_name += "_MAPPING_DATA";
		ge.AIM_schema_name = ge.AIM_name.substring(0,ge.AIM_name.length()-16);
		

		
		ge.generateArm(ge.aim_application_model);

		if (flag_commit) {
			trans.commit();
		}
		session.closeSession();
		
		System.out.println("AIM2ARM generator ended");
	
	}

	public void generateArm(SdaiModel aim_application_model) throws SdaiException {

		SdaiSession session = SdaiSession.getSession();
		SchemaInstance si = session.getDataDictionary();

		SdaiRepository repository = null;
		ASdaiRepository repos = session.getKnownServers();
		SdaiIterator iter_repo = repos.createIterator();

		while (iter_repo.next()) {

			SdaiRepository rp = repos.getCurrentMember(iter_repo);
			if (rp.getName().equalsIgnoreCase("SystemRepository")) {
				repository = rp;
				break;
			} 
		}
		if (!repository.isActive()) {  // open repo if not open
				repository.openRepository();
		}


   ASdaiModel models = repository.getModels();
	 // generate here

		SdaiIterator model_iterator = models.createIterator();
		SdaiModel aim_model = null;
		SdaiModel arm_model = null;
		SdaiModel map_model = null;
		SdaiModel generated_arm_model = null;
		SdaiRepository generated_repository = null;
// printDebug("ARM_name: " + ARM_name);		
// printDebug("AIM_name: " + AIM_name);		
// printDebug("MAP_name: " + MAP_name);		

		
		while (model_iterator.next()) {
			SdaiModel model = models.getCurrentMember(model_iterator);
			String model_name = model.getName();
// printDebug("current model :" + model.getName());
			if (model_name.equalsIgnoreCase(ARM_name)) {
				arm_model = model;
		    if (arm_model.getMode() == SdaiModel.NO_ACCESS) {
    			arm_model.startReadOnlyAccess();
    		}
// printDebug("ARM model found:" + arm_model.getName());
			} else 
			if (model_name.equalsIgnoreCase(AIM_name)) {
				aim_model = model;
	  	  if (aim_model.getMode() == SdaiModel.NO_ACCESS) {
  	  		aim_model.startReadOnlyAccess();
    		}
// printDebug("AIM model found:" + aim_model.getName());
			} else 
			if (model_name.equalsIgnoreCase(MAP_name)) {
				map_model = model;
		    if (map_model.getMode() == SdaiModel.NO_ACCESS) {
    			map_model.startReadOnlyAccess();
    		}
// printDebug("MAP model found:" + map_model.getName());
			} else {
			}
		}

		global_map_model = map_model;

		// clear temp for all constraints
		Aggregate aggr_constraints = map_model.getEntityExtentInstances(EConstraint.class);
		SdaiIterator constraint_iterator = aggr_constraints.createIterator();
		while (constraint_iterator.next()) {
			EConstraint constraint = (EConstraint)aggr_constraints.getCurrentMemberObject(constraint_iterator);
			constraint.setTemp(null);
		}


		/* 
				spread the wave from roots to leaves and mark the constraints
				perhaps this way: go through all entity_mapping instances,
				for each entity_mapping instance, get its constraints which is of SELECT type:
				
				TYPE constraint_select = SELECT
					(constraint_attribute,
	 				constraint_relationship,
		 				inverse_attribute_constraint, (* there must be at least one inverse related instance. Could be non_optional_constraint instead *)
	 				attribute, (* this may indicate that this attribute must have a value *)
	 				end_of_path_constraint, 
	 				intersection_constraint,
	 				type_constraint,
	 				negation_constraint );
				END_TYPE;
	
				Mark the instances of that constraint as the start points for the wave
				Then go through all the constraint instances and spread the wave from the marked instances.
				Repeat untill nothing more is spread
			
				Actually, it is possible to start spreading the wave immediately when going through entity_mappig instances.
				Start with one instance and spread from it until the leaves are reached.
				Or to spread the wave in a separate loop through all the constraint instances, in parallel, from the front
				It seems that there is no difference because for the longest path spreading the wave multiple times may be needed in both cases.
				But with the front approach more loops are needed through all the instances or through the front instances in a separate aggregate.
	
		*/  

// ################### new stuff for wave ### start

  	total_start_time = System.currentTimeMillis();
  	wave_start_time = total_start_time;

		int max_number_of_steps = 0;
		EEntity mapping_constraints = null;
		ESchema_definition arm_schema = getSchema_definitionFromModel(arm_model);
		AEntity_declaration arm_entities = arm_schema.getEntity_declarations(null, null);
		SdaiIterator arm_iter = arm_entities.createIterator();
		while (arm_iter.next()) {
			EEntity_definition arm_entity = (EEntity_definition)arm_entities.getCurrentMember(arm_iter).getDefinition(null);
			if (flag_on_demand) {
				String current_arm_entity_name = arm_entity.getName(null);
				if (!current_arm_entity_name.equalsIgnoreCase(ARM_entity_name_requested)) {
					continue;
				}
				System.out.println("ARM entity: " + current_arm_entity_name);
			}			
			AEntity all_entity_mapping = map_model.getEntityExtentInstances(EEntity_mapping.class);
			// needed if want to know in advance how many, as, for example, one or more, in this case perhaps unneccessary
			Vector own_entity_mapping = new Vector();
			SdaiIterator iter_mapping = all_entity_mapping.createIterator();
			while (iter_mapping.next()) {
				EEntity_mapping entity_mapping  = (EEntity_mapping)all_entity_mapping.getCurrentMemberObject(iter_mapping);
				EEntity_definition source = entity_mapping.getSource(null);
				if (source == arm_entity) {
					own_entity_mapping.addElement(entity_mapping);
				}
			}
			for (int i = 0; i < own_entity_mapping.size(); i++) {
				if (flag_one_entity_mapping_alternative_only) {
					if (i < NR_ENTITY_MAPPING_ALTERNATIVE) {
						continue;
					} else {
						if (i > NR_ENTITY_MAPPING_ALTERNATIVE) {
							break;
						}
					}
				}
				EEntity_mapping the_entity_mapping = (EEntity_mapping)own_entity_mapping.elementAt(i);
				if (the_entity_mapping.testConstraints(null)) {
					mapping_constraints = the_entity_mapping.getConstraints(null);
					if (mapping_constraints instanceof EConstraint) {
						int number_of_steps = waveInst((EConstraint)mapping_constraints, 1);
						if (number_of_steps > max_number_of_steps) max_number_of_steps = number_of_steps;
					}
				}
				// arm attributes ------ start
					
				// Vector arm_attributes = new Vector();
				if (flag_disable_attributes) continue;
				Aggregate attributes = arm_model.getEntityExtentInstances(EAttribute.class);
				SdaiIterator iter = attributes.createIterator();
				Aggregate attribute_mappings = map_model.getEntityExtentInstances(EGeneric_attribute_mapping.class);
				SdaiIterator iter_map;
				int attribute_count = 0;
				while (iter.next()) {
					EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
					if (flag_one_attribute_only) {
						if (attribute_count < NR_ATTRIBUTE) {
							attribute_count++;
							continue;
						} else 
						if (attribute_count > NR_ATTRIBUTE) {
							break;
						}
					}
					EEntity_definition parent = an_attribute.getParent_entity(null);
 					if (parent != arm_entity) continue;
					// needed if want to know in advance how many, as, for example, one or more, in this case perhaps unneccessary
					Vector attribute_mapping_alternatives = new Vector();
					iter_map = attribute_mappings.createIterator();
					while (iter_map.next()) {
						EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
						EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
						if (an_entity_mapping == the_entity_mapping) {
							EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
							if (an_attribute == a_mapped_attribute) {
								if (flag_strong_only) {
									if (an_attribute_mapping.getStrong(null)) {
										attribute_mapping_alternatives.addElement(an_attribute_mapping);
									} 
								} else {
									attribute_mapping_alternatives.addElement(an_attribute_mapping);
								}
							}
						}	
					}	// while mapping alternatives of an attribute
					for (int j = 0; j < attribute_mapping_alternatives.size(); j++) {
						if (flag_one_attribute_mapping_alternative_only) {
							if (j < NR_ATTRIBUTE_MAPPING_ALTERNATIVE) {
								continue;
							} else
							if (j > NR_ATTRIBUTE_MAPPING_ALTERNATIVE) {
								break;
							}
						}
						EGeneric_attribute_mapping the_attribute_mapping = (EGeneric_attribute_mapping)attribute_mapping_alternatives.elementAt(j);
         		if (the_attribute_mapping.testConstraints(null)) {
							EEntity attribute_constraints = the_attribute_mapping.getConstraints(null);
							if (attribute_constraints instanceof EConstraint) {
								int number_of_steps = waveInst((EConstraint)attribute_constraints, 1);
								if (number_of_steps > max_number_of_steps) max_number_of_steps = number_of_steps;
							}
						} 
					}
					attribute_count++;
				} // while arm attributes
				// arm attributes ------ end
			} // through entity_mapping alternatives
		} // through arm entities


// ################### new stuff for wave ### end

// ################### original stuff for wave ### start
/*	

		EEntity mapping_constraints = null;

		AEntity all_entity_mapping = map_model.getEntityExtentInstances(EEntity_mapping.class);
		SdaiIterator mapping_iterator = all_entity_mapping.createIterator();
		int max_number_of_steps = 0;
		int entity_mapping_number = 0;
		int generate_number_start = NR_ENTITY_MAPPING_START;
		int generate_number_end = NR_ENTITY_MAPPING_END;
		while (mapping_iterator.next()) {
			EEntity_mapping entity_mapping  = (EEntity_mapping)all_entity_mapping.getCurrentMemberObject(mapping_iterator);
			
			if (range_only) {
				if (entity_mapping_number < generate_number_start) {
					entity_mapping_number++;
					continue;
				} else 
				if (entity_mapping_number > generate_number_end) {
					break;
				}
				// else - spread the wave
				System.out.println("\nARM source entity: " + entity_mapping.getSource(null).getName(null));
				EEntity target = entity_mapping.getTarget(null);
				if (target instanceof EEntity_definition) {
					System.out.println("AIM target entity: " + ((EEntity_definition)target).getName(null));
				} else 
				if (target instanceof EAttribute) {
					System.out.println("target attribute: " + ((EAttribute)target).getName(null));
				} else {
					System.out.println("AIM target: " + target);
				}				
			}
			
			if (entity_mapping.testConstraints(null)) {
				mapping_constraints = entity_mapping.getConstraints(null);
				// we could mark only if it is an instance of an actual constraint, 
				// not clear at this point if it is useful for some purpose to mark an attribute
				if (mapping_constraints instanceof EConstraint) {
					// constraints.setTemp(START_MARK);
//					int number_of_steps = spreadWave((EConstraint)constraints);
					int number_of_steps = waveInst((EConstraint)mapping_constraints, 1);
					if (number_of_steps > max_number_of_steps) max_number_of_steps = number_of_steps;
				}
			}
			entity_mapping_number++;
		}

*/

// ################### original stuff for wave ### end

  	wave_end_time = System.currentTimeMillis();
		processing_start_time = wave_end_time;
		wave_duration = wave_end_time - wave_start_time;

	// at this stage we have all the revelent constraints marked and also we have the highest mark number: max_number_of_steps
	// so now we go through all the constraint instances, and process those that have the mark = max_number_of_steps
		

	  // some initialization


		ESchema_definition aim_schema = aim_application_model.getUnderlyingSchema();
		work = application_repository.createSdaiModel("working", aim_schema);
		_context = new SdaiContext(aim_schema, application_domain, work);
		session.setSdaiContext(_context);
	  
	  // for late binding test methods
	  select_path = new EDefined_type[10];

		sorter = new Aim2ArmGeneratorSorterForInstances();
		// formated_int = new PrintfFormat("%4d");
		initInstanceArray();

		// Aggregate aggr_constraints = map_model.getEntityExtentInstances(EConstraint.class);
		// SdaiIterator constraint_iterator = aggr_constraints.createIterator();
		// max_number_of_steps--;
		System.out.println("");
		printVerbose("Max number of steps: " + max_number_of_steps);
		System.out.println("");
		
		int processed_constraint_instances_count = 0;
		for (int level = max_number_of_steps; level > 0; level--) {
			constraint_level = level;
			printDebug("####### level: " + level);
			// int constraint_count = 0;
			// int leaf_count = 0; 
			// int entity_mapping_constraint_count = 0;
			constraint_iterator.beginning();
			
			// for debugging
			boolean printed_one_array = false;
			
			while (constraint_iterator.next()) {
				EConstraint constraint = (EConstraint)aggr_constraints.getCurrentMemberObject(constraint_iterator);
				// constraint_count++;
				Object temp_obj = constraint.getTemp();
				if (temp_obj == null) continue;
				
				// may be already processed and hold arrays with results
				if (!(temp_obj instanceof Integer)) {
					// is it array here?
					if (printed_one_array) continue;	
								
					EEntity temp_array [] = (EEntity [])temp_obj;
//					printDebug("printing one temp array");
//					for (int j = 0; j < temp_array.length; j++) {
//						printDebug(" " + j + ": " + temp_array[j]); 
//					}
					printed_one_array = true;
					continue;
				}

				if  (((Integer) temp_obj).intValue() == level) {

//					System.out.println("Leaf constraint: " + constraint);
					// leaf_count++;
//					EEntity entity_array [] = new EEntity [5]; 
//					constraint.setTemp(entity_array);
					
					// clear  the working array, perhaps
						
					// clearInstanceArray();				
	
          // ##################### processing of a constraint instance according to the constraint type #################################

					processed_constraint_instances_count++;
		 			printDebug(">>>>> constraint level: " + constraint_level + ", count: " + processed_constraint_instances_count + ", constraint: " + constraint, 3);
					// constraint - Abstract supertype for all constraints except attribute because it is taken from dictionary
					if (constraint instanceof EConstraint_attribute) { // ABSTRACT 
				 		// The constraint_attribute further constrains an Express-attribute, 
				 		// an inverse_attribute_constraint or another constraint_attribute.
						// -----------------------------------------------------------------------------------------------------------------------------
			 			
			 			
			 			if (constraint instanceof EAggregate_member_constraint) {
							processAggregate_member_constraint((EAggregate_member_constraint)constraint);
						} else
						if (constraint instanceof EAggregate_size_constraint) {
							processAggregate_size_constraint((EAggregate_size_constraint)constraint);
						} else
						if (constraint instanceof EAttribute_value_constraint) {
							processAttribute_value_constraint((EAttribute_value_constraint)constraint);
				 		} else
				 		if (constraint instanceof EEntity_constraint) {
							processEntity_constraint((EEntity_constraint)constraint);
						} else
						if (constraint instanceof ESelect_constraint) {
							processSelect_constraint((ESelect_constraint)constraint);
						} else {
							// internal error - unknown constraint_attribute
							printError("unknown constraint_attribute: " + constraint);
						}	
					} else
					if (constraint instanceof EConstraint_relationship) { // ABSTRACT
						processConstraint_relationship((EConstraint_relationship)constraint);
					} else
					if (constraint instanceof EEnd_of_path_constraint) {
						processEnd_of_path_constraint((EEnd_of_path_constraint)constraint);
					} else
					if (constraint instanceof EIntersection_constraint) {
						processIntersection_constraint((EIntersection_constraint)constraint);
					} else
					if (constraint instanceof EInverse_attribute_constraint) {
						processInverse_attribute_constraint((EInverse_attribute_constraint)constraint);
					} else
					if (constraint instanceof ENegation_constraint) {
						processNegation_constraint((ENegation_constraint)constraint);
					} else
					if (constraint instanceof EType_constraint) {
						processType_constraint((EType_constraint)constraint);
    	    } else {
      	  	// internal error - unknown constraint type
						printError("unknown constraint type: " + constraint);
        	}

          // ############### end of processing of a constraint instance according to the constraint type #########


				}
				// entity_mapping_constraint_count++;
			} // while
			// System.out.println("Total number of constraints: " + constraint_count + ", constraints for entity mapping: " + entity_mapping_constraint_count + ", constraints with mark " + i + ": " + leaf_count);

//  for sort debugging, one loop is enough here
//			break;

		} // for
		
  	processing_end_time = System.currentTimeMillis();
		processing_duration = processing_end_time - processing_start_time;
  	generating_start_time = processing_end_time;


		// let's create new model for arm instances, just for ap210_arm for now, for some testing

		int generated_arm_instances_count = 0;
		try {
			generated_repository = session.linkRepository("AIM2ARM", null);
			if(generated_repository != null) {
				generated_repository.deleteRepository();
			}
		} catch(SdaiException e) {
			if(e.getErrorId() != SdaiException.RP_NEXS) {
				throw e;
			}
		}
		generated_repository = session.createRepository("AIM2ARM", null);
		generated_repository.openRepository();
		generated_arm_model = generated_repository.createSdaiModel("Generated ARM instances", arm_schema);
		generated_arm_model.startReadWriteAccess();
//				sd = (jsdai.SExtended_dictionary_schema.ESchema_definition)model.createEntityInstance(jsdai.SExtended_dictionary_schema.CSchema_definition.class);
//				sd.setName(null, schema_name.toUpperCase());
		


		// hope its contents not gone
		// arm_entities = schema.getEntity_declarations(null, null);
		arm_iter.beginning();
		// going through arm entities perhaps not needed, possible to process entity_mappings directly, but why not, may be handy when generating instances, etc.
		while (arm_iter.next()) {
			EEntity_definition arm_entity = (EEntity_definition)arm_entities.getCurrentMember(arm_iter).getDefinition(null);
			if (!arm_entity.getInstantiable(null)) continue;

			// move out of loop and do only once, start iterator again from beginning()
			AEntity all_entity_mapping = map_model.getEntityExtentInstances(EEntity_mapping.class);
			// probably no need to group all the alternatives together, but perhaps there will be some advantages later
			Vector entity_mapping_alternatives = new Vector();
			SdaiIterator iter_mapping = all_entity_mapping.createIterator();
			while (iter_mapping.next()) {
				EEntity_mapping entity_mapping  = (EEntity_mapping)all_entity_mapping.getCurrentMemberObject(iter_mapping);
				EEntity_definition source = entity_mapping.getSource(null);
				if (source == arm_entity) {
					entity_mapping_alternatives.addElement(entity_mapping);
				}
			}
			EEntity [] arm_instances = null;
			for (int i = 0; i < entity_mapping_alternatives.size(); i++) {
				EEntity_mapping the_entity_mapping = (EEntity_mapping)entity_mapping_alternatives.elementAt(i);
				if (the_entity_mapping.testConstraints(null)) {
					mapping_constraints = the_entity_mapping.getConstraints(null);
					if (mapping_constraints instanceof EConstraint) {
						// take the array from temp
						arm_instances = getEntityInstancesExt(mapping_constraints);
						// should we check if among the instances are present instances of supertypes of target,
						// and take only the instances of the target type? 
						// filtering done later
					} else 
					if (mapping_constraints instanceof EExplicit_attribute) {
						arm_instances = getEntityInstancesExt(mapping_constraints);
						// here we have cases when the parent entity of the attribute is supertype of target of entity_mapping
						// question - should we take only the instances that are actually  of the target type? 
						// filtering done later
						System.out.println("XXX mapping constraints = explicit_attribute: " + mapping_constraints + ", source: " + the_entity_mapping.getSource(null));
					} else {
						// not sure, but not processed anyway
						printError("NOT SUPPORTED mapping constraints, not a constraint or an explicit_attribute: " + mapping_constraints);
						
					}
					// lets remove all supertypes that are not target.
					// or perhaps do it only once after strong attributes are taken into account as well
				} else {
					// take the whole population - not constrained
					arm_instances = getEntityInstancesExt(the_entity_mapping.getTarget(null));
					// filtering by target not needed, obviously
				}
				printInstanceArray("before attributes - arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null), arm_instances, null, 1);
				// ok, now we have the instances from entity_mapping, try to intersect them with those that produce valid strong attributes
				// here, we could just intersect with arrays of own attribute_mapppings with strong = true, but we want also to group attribute_mappings, who knows why
				// NO!! we need to group them because intersecting with each alternative separately will give different result
// if (arm_instances.length > 0) {
	// System.out.println(arm_entity.getName(null) + ": number of arm instances without attributes: " + arm_instances.length);
// }
				Aggregate attributes = arm_model.getEntityExtentInstances(EAttribute.class);
				SdaiIterator iter = attributes.createIterator();
				Aggregate attribute_mappings = map_model.getEntityExtentInstances(EGeneric_attribute_mapping.class);
				SdaiIterator iter_map;
				while (iter.next()) {
					EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
					EEntity_definition parent = an_attribute.getParent_entity(null);
 					if (parent != arm_entity) continue;
					// finding attribute mapping alternatives
					Vector attribute_mapping_alternatives = new Vector();
					iter_map = attribute_mappings.createIterator();
					while (iter_map.next()) {
						EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
						EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
						if (an_entity_mapping == the_entity_mapping) {
							EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
							if (an_attribute == a_mapped_attribute) {
								// so far,  we are interested in strong attributes only
								if (an_attribute_mapping.getStrong(null)) {
									attribute_mapping_alternatives.addElement(an_attribute_mapping);
								} 
							}
						}	
					}	// while mapping alternatives of an attribute
					
					boolean no_constraints = false;
					EEntity [] attribute_arm_instances = new EEntity [0];
					for (int j = 0; j < attribute_mapping_alternatives.size(); j++) {
						EGeneric_attribute_mapping the_attribute_mapping = (EGeneric_attribute_mapping)attribute_mapping_alternatives.elementAt(j);
         		EEntity [] attribute_alternative_instances = new EEntity [0];
         		if (the_attribute_mapping.testConstraints(null)) {
							EEntity attribute_constraints = the_attribute_mapping.getConstraints(null);
							attribute_alternative_instances = getEntityInstancesExt(attribute_constraints);
// if (attribute_alternative_instances.length > 0) {
	// System.out.println("### number: " + attribute_alternative_instances.length);
// }							
						} else {
							// this strong attribute is not restricted by constraints. No path either. Identical mapping
							// so, the union of the alternatives should also be not restricted, do nothing for all the alternatives
							// one way to do it - to take the population, another to have a flag and skip 
							no_constraints = true;
							break;
						} 
				printInstanceArray("union op1 - arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null) + ", attribute: " + an_attribute.getName(null), attribute_arm_instances, null, 1);
				printInstanceArray("union op2 - arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null) + ", attribute: " + an_attribute.getName(null), attribute_alternative_instances, null, 1);
						attribute_arm_instances = performUnion(attribute_arm_instances, attribute_alternative_instances);
				printInstanceArray("union result - arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null) + ", attribute: " + an_attribute.getName(null), attribute_arm_instances, null, 1);
					} // for 
					if (!no_constraints) {
						// intersect with the attribute union array, else - do nothing
				printInstanceArray("op1 - arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null) + ", attribute: " + an_attribute.getName(null), arm_instances, null, 1);
				printInstanceArray("op2 - arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null) + ", attribute: " + an_attribute.getName(null), attribute_arm_instances, null, 1);

						arm_instances = performAnd(arm_instances, attribute_arm_instances);
				printInstanceArray("result - arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null) + ", attribute: " + an_attribute.getName(null), arm_instances, null, 1);
					}
				} // while arm attributes


				// generate arm instances ---------------------------------------------------------------------------------------

// if (arm_instances.length > 0) {
	// System.out.println(arm_entity.getName(null) + ": number of arm instances with attributes: " + arm_instances.length);
// }
				// lets remove instances that are not target but rather its supertype, perhaps check also for errors - incompatible types?
				arm_instances = filterByTarget(arm_instances, the_entity_mapping.getTarget(null));

				printInstanceArray("arm instances for entity: " + arm_entity.getName(null) + ", alternative target: " + ((EEntity_definition)the_entity_mapping.getTarget(null)).getName(null) + ", source: " + the_entity_mapping.getSource(null).getName(null), arm_instances, null, 1);
				
				// if (arm_instances == null) continue;
				for (int j = 0; j < arm_instances.length; j++) {
					EEntity arm_instance = arm_instances[j];
					EEntity created_inst = generated_arm_model.createEntityInstance(arm_entity);
					generated_arm_instances_count++;
					// go down get attribute values for this arm_instance and set them					
				} 
			} // for - through entity_mapping alternatives
		
		} // through arm entities


  	generating_end_time = System.currentTimeMillis();
		generating_duration = generating_end_time - generating_start_time;
		total_end_time = generating_end_time;
		total_duration = total_end_time - total_start_time;
		System.out.println("\nDuration of marking with wave: " + wave_duration + "  milliseconds");
		System.out.println("Duration of processing of " + processed_constraint_instances_count + " constraint instances: " + processing_duration + "  milliseconds");
		System.out.println("Duration of generating of " + generated_arm_instances_count + " arm instances: " + generating_duration + "  milliseconds");
		System.out.println("Total duration: " + total_duration + "  milliseconds\n");

		
		generated_repository.exportClearTextEncoding("generated_arm.stp");
		if (flag_p21) {
			repository.exportClearTextEncoding(physical_file);
		}		
	}

	
//############################# MARKING CONSTRAINT LEVELS WITH WAVE ###############################################################
	
	int waveInst(EConstraint constraint, int start_step) throws SdaiException {
		
		int end_step = start_step;
		int current_step;
						
		Object temp_obj = constraint.getTemp();
    
    if (temp_obj != null) {
			if (((Integer) temp_obj).intValue() >= start_step) {
  			return start_step;
  		}
  	}
    
    constraint.setTemp(new Integer(start_step));
		start_step++;				
						
		// constraint - Abstract supertype for all constraints except attribute because it is taken from dictionary
		if (constraint instanceof EConstraint_attribute) { // ABSTRACT 
	 		// The constraint_attribute further constrains an Express-attribute, 
	 		// an inverse_attribute_constraint or another constraint_attribute.
			// -----------------------------------------------------------------------------------------------------------------------------

	 		if (constraint instanceof EAggregate_member_constraint) {
				// An aggregate_member_constraint is a constraint that constraint aggregates. 
				// It selects one specific or arbitrary element from aggregate. The attribute must point to an attribute of type aggregate.
				// ---------------------------------------------------------------------------------------------------------------------------

				// If set, defines element in aggregate that must meet requirement, e.g. [1], only valid for LIST and ARRAY but misused by APs.
				// If unset, there shall be at least one member. Notation [i]
				// member : OPTIONAL INTEGER;

				// An attribute must be of aggregate type. One element of is this aggregate is selected using aggregate_member_constraint.
				// attribute : aggregate_member_constraint_select;
				EEntity attribute = null;
				if (((EAggregate_member_constraint)constraint).testAttribute(null)) {
					attribute = ((EAggregate_member_constraint)constraint).getAttribute(null);
					if (attribute instanceof EConstraint) {
						current_step = waveInst((EConstraint)attribute, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional attribute in aggregate_member_constraint: " + constraint);
				}

			} else
			if (constraint instanceof EAggregate_size_constraint) {
				// An aggregate_member_constraint is a constraint that constraint how many elements may be in aggregate. 
				// It selects one specific or arbitrary element from aggregate. The attribute must point to an attribute of type aggregate.
				// Since v25.
				// This constraint may introduce some changes in selects where valind constraints for other constraints are listed.
				// ---------------------------------------------------------------------------------------------------------------------------

				// Defines size of aggregate.
				// size : INTEGER;

				// An attribute must be of aggregate type. One element of is this aggregate is selected using aggregate_member_constraint.
				// attribute : aggregate_member_constraint_select;

				EEntity attribute = null;
				if (((EAggregate_size_constraint)constraint).testAttribute(null)) {
					attribute = ((EAggregate_size_constraint)constraint).getAttribute(null);
					if (attribute instanceof EConstraint) {
						current_step = waveInst((EConstraint)attribute, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional attribute in aggregate_size_constraint: " + constraint);
				}
					
			} else
			if (constraint instanceof EAttribute_value_constraint) {
				// An attirubute_value_constraint is constraint that is used to restrict attributes 
				// or elements of aggregates to some specific simple value.
				// ---------------------------------------------------------------------------------------------------------------------------
						
				// An attribute is definition of attribute or aggregate that is constraint to some value.
				// attribute : attribute_value_constraint_select;
				EEntity attribute = null;
				if (((EAttribute_value_constraint)constraint).testAttribute(null)) {
					attribute = ((EAttribute_value_constraint)constraint).getAttribute(null);
					if (attribute instanceof EConstraint) {
						current_step = waveInst((EConstraint)attribute, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional attribute in attribute_value_constraint: " + constraint);
				}
						
				if (constraint instanceof EBoolean_constraint) {
					// A booleant_constraint specifies a constraint on boolean value. 
					// Attribute must point to attribute definition or aggregate or select element 
					// (in this case select element that is defined type) that is of type boolean.
					// -------------------------------------------------------------------------------------------------------------------------

					// A constraint_value is value to which the attribute is constraint.
					// constraint_value : BOOLEAN;
						
				} else
				if (constraint instanceof EEnumeration_constraint) {
					// An enumeration_constraint specifies a constraint on enumeration value. 
					// Attribute must point to attribute that is of type enumeration.
					// -------------------------------------------------------------------------------------------------------------------------

					// A constraint_value defines a constant to which constraint attribute must be equal.
					// constraint_value : express_id;

				} else
				if (constraint instanceof EInteger_constraint) {
					// An integer_constraint specifies a constraint on integer value. 
					// Attribute must point to attribute that is of type integer.
	 				// -------------------------------------------------------------------------------------------------------------------------

					// A constraint_value defines a constant to which constraint attribute must be equal.
					// constraint_value : INTEGER;

	 			} else
	 			if (constraint instanceof ELogical_constraint) {
					// A logical_constraint specifies a constraint on logical value. 
					// Attribute must point to attribute that is of type logical.
	 				// -------------------------------------------------------------------------------------------------------------------------

					// A constraint_value defines a constant to which constraint attribute must be equal.
					// constraint_value : LOGICAL;
		 				
	 			} else
	 			if (constraint instanceof ENon_optional_constraint) {
					// A non_optional_constraint restricts optional attribute of entity to be non-optional.
 					// -------------------------------------------------------------------------------------------------------------------------

 				} else
 				if (constraint instanceof EReal_constraint) {
					// A real_constraint specifies a constraint on real value. Attribute must point to attribute that is of type real.
 					// -------------------------------------------------------------------------------------------------------------------------

					// A constraint_value defines a constant to which constraint attribute must be equal.
					// constraint_value : REAL;

 				} else
 				if (constraint instanceof EString_constraint) {
					// A String_constraint specifies a constraint on string value. Attribute must point to attribute that is of type string.
					// -------------------------------------------------------------------------------------------------------------------------

					// A constraint_value defines a constant to which constraint attribute must be equal.
					// constraint_value : STRING;

				}		 			
	 		} else
	 		if (constraint instanceof EEntity_constraint) {
				// An entity_constraint specifies a constraint on entity type. 
				// The attribute must point to an attribute or constraint definition that is of an entity type. 
				// This constraint restricts to subtypes or select types (maybe to complex types).
	 			// ---------------------------------------------------------------------------------------------------------------------------
						
				// A domain defines an entity type. The value of constraint attribute (it also may be element of aggregate) must by of this type.
				// domain : entity_definition;
						
				// An attribute is a definition of attribute that is constraint to some entity type.
				// attribute : attribute_select;
				EEntity attribute = null;
				if (((EEntity_constraint)constraint).testAttribute(null)) {
					attribute = ((EEntity_constraint)constraint).getAttribute(null);
					if (attribute instanceof EConstraint) {
						current_step = waveInst((EConstraint)attribute, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional attribute in entity_constraint: " + constraint);
				}
			 			
	 			if (constraint instanceof EExact_entity_constraint) {
					// An exact_entity_constraint is entity constraint that constraints the attribute to be of some specific entity type, 
					// excluding subtypes of it.
 					// -------------------------------------------------------------------------------------------------------------------------
				}
			} else
			if (constraint instanceof ESelect_constraint) {
				// A select_constraint specifies a constraint on select type. Attribute must point to attribute that is of select type.
				// Complex instances may exist with logical_, boolean_, string_, enumeration_, integer_ and real_constraint.
				// ---------------------------------------------------------------------------------------------------------------------------

				// A data_type defines a path to go throw select.
				// data_type : LIST [1:?] OF defined_type;

				// An attribute is definition of attribute or other element that is of select type and is constraint to some specific selection.
				// attribute : select_constraint_select;

				EEntity attribute = null;
				if (((ESelect_constraint)constraint).testAttribute(null)) {
					attribute = ((ESelect_constraint)constraint).getAttribute(null);
					if (attribute instanceof EConstraint) {
						current_step = waveInst((EConstraint)attribute, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional attribute in select_constraint: " + constraint);
				}
					
			} else {
				// internal error - unknown constraint_attribute
			}	
		} else
		if (constraint instanceof EConstraint_relationship) { // ABSTRACT
			// A constraint_relationship defines pair of constraints. It does not specify the meaning of relationship nor a direction to go.
			// -----------------------------------------------------------------------------------------------------------------------------

			// An element2 is a second constraint that is related by this constraint relationship.
			// element2 : constraint_select;
			EEntity element2 = null;
			if (((EConstraint_relationship)constraint).testElement2(null)) {
				element2 = ((EConstraint_relationship)constraint).getElement2(null);
				if (element2 instanceof EConstraint) {
					current_step = waveInst((EConstraint)element2, start_step);
					if (current_step > end_step) end_step = current_step;
				}
			} else {
				// ERROR - not OPTIONAL - must be set
				printError("UNSET non-optional element2 in constraint_relationship: " + constraint);
			}

			if (constraint instanceof EInstance_constraint) { // ABSTRACT
				// An instances_constraint is a consraint_relationship that defined constraints on instances of one type.
				// ---------------------------------------------------------------------------------------------------------------------------

				// An element1 is first constraint that is related by this constraint relationship.
				// element1 : constraint_select;

				EEntity element1 = null;
				if (((EInstance_constraint)constraint).testElement1(null)) {
					element1 = ((EInstance_constraint)constraint).getElement1(null);
					if (element1 instanceof EConstraint) {
						current_step = waveInst((EConstraint)element1, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional element1 in instance_constraint: " + constraint);
				}

				if (constraint instanceof EAnd_constraint_relationship) {
					// An and_constraint_relationship is relationship of two constraints and both these constraints 
					// must be met to fulfil and_constraint_relationship.
					// -------------------------------------------------------------------------------------------------------------------------
				} else
				if (constraint instanceof EInstance_equal) {
					// An instance_equal is instance_constraint that requires to constraints to end with the same instance.
					// -------------------------------------------------------------------------------------------------------------------------
				} else
				if (constraint instanceof EOr_constraint_relationship) {
					// An or_constraint_relationship is an instance_constraint where it is enough, 
					// that requirements of at least one constraint are meet.
					// -------------------------------------------------------------------------------------------------------------------------
				}	else {
					// internal error - unknown instance_constraint
				}				
			} else
			if (constraint instanceof EPath_constraint) {			
				// A path_constraint is a constraint_relationship that does following: 
				// requires make step using element1 and meet requirements of element2. 
				// The element1 shall be a constraint on an attribute of an entity-type. 
				// The element2 defines a constraint on the entity reached by following element1.
				// ---------------------------------------------------------------------------------------------------------------------------

				// An element1 is attribute or other element that allows to make step from one entity instance to instance of other entity.
				// element1 : path_constraint_select;

				EEntity element1 = null;
				if (((EPath_constraint)constraint).testElement1(null)) {
					element1 = ((EPath_constraint)constraint).getElement1(null);
					if (element1 instanceof EConstraint) {
						current_step = waveInst((EConstraint)element1, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional element1 in path_constraint: " + constraint);
				}

			}	else {
				// internal error - unknown constraint_relationship
			}	
		} else
		if (constraint instanceof EEnd_of_path_constraint) {
			/*
				End_of_path_constraint is intended as a replacement for
				attribute_constraint.path list. It allows to identify end of the main path
				condition. See comments at attribute_constraint.path<br>
				End_of_path_constraint is used as value of path_constraint.element2 or at the top of
				constraint tree.<br>
				Introduced in version 31.1.
			*/ // --------------------------------------------------------------------------------------------------------------------------

			// The remaining part of constraint tree which doesn't belong to the main path.
			// constraints : constraint_select;

				EEntity constraints = null;
				if (((EEnd_of_path_constraint)constraint).testConstraints(null)) {
					constraints = ((EEnd_of_path_constraint)constraint).getConstraints(null);
					if (constraints instanceof EConstraint) {
						current_step = waveInst((EConstraint)constraints, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional constraints in end_of_path_constraint: " + constraint);
				}
		

		} else
		if (constraint instanceof EIntersection_constraint) {
			/*
				INTERSECTION_CONSTRAINT allows to get only common (intersecting) instances 
				from subpaths specified by this instance. This can be used in particularly when
				several AND paths point to same instance. It effectively outdates 
				instance_equal entity.
				Introduced in version 31.0.
			*/ // --------------------------------------------------------------------------------------------------------------------------

			// Subpaths to be intersected.
			// subpaths : SET [2:?] OF constraint_select;

				AConstraint_select subpaths = null;
				if (((EIntersection_constraint)constraint).testSubpaths(null)) {
					subpaths = ((EIntersection_constraint)constraint).getSubpaths(null);
					SdaiIterator subpaths_iterator = subpaths.createIterator();
					while (subpaths_iterator.next()) {
						EEntity subpath = (EEntity)subpaths.getCurrentMemberObject(subpaths_iterator);
						if (subpath instanceof EConstraint) {
							current_step = waveInst((EConstraint)subpath, start_step);
							if (current_step > end_step) end_step = current_step;
						}
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional subpaths in intersection_constraint: " + constraint);
				}

		} else
		if (constraint instanceof EInverse_attribute_constraint) {
			/*
				An inverse_attribute_constraint defines a needed inverse attribute that is missing in the target schema.
				This is needed to travel reverse to attribute definition.

				NOTE 1 - In the case that the AIM express already contains the proper inverse attribute this should be used.
			*/ // --------------------------------------------------------------------------------------------------------------------------
					
			// An inverted_attribute is a definition of attribute that is inverted.
			// inverted_attribute : inverse_attribute_constraint_select;

				EEntity inverted_attribute = null;
				if (((EInverse_attribute_constraint)constraint).testInverted_attribute(null)) {
					inverted_attribute = ((EInverse_attribute_constraint)constraint).getInverted_attribute(null);
					if (inverted_attribute instanceof EConstraint) {
						current_step = waveInst((EConstraint)inverted_attribute, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional inverted_attribute in EInverse_attribute_constraint: " + constraint);
				}
					
		} else
		if (constraint instanceof ENegation_constraint) {
			// NEGATION_CONSTRAINT is used to specify that result of constraint has to be taken negated.
			// It can be used only in the addition constraint but never in the main path.
			// -----------------------------------------------------------------------------------------------------------------------------

			// constraints result of whose has to negated.
			// constraints : constraint_select;

				EEntity constraints = null;
				if (((ENegation_constraint)constraint).testConstraints(null)) {
					constraints = ((ENegation_constraint)constraint).getConstraints(null);
					if (constraints instanceof EConstraint) {
						current_step = waveInst((EConstraint)constraints, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// ERROR - not OPTIONAL - must be set
					printError("UNSET non-optional constraints in negation_constraint: " + constraint);
				}

		} else
		if (constraint instanceof EType_constraint) {
			// TYPE_CONSTRAINT requires that starting AIM instance is of type defined in domain attribute.
			// -----------------------------------------------------------------------------------------------------------------------------

			// Required type of AIM instance.
			// domain : entity_definition;
					
			// The remaining constraints in a similar way as path.element2 attribute.
			// constraints : OPTIONAL constraint_select;

				EEntity constraints = null;
				if (((EType_constraint)constraint).testConstraints(null)) {
					constraints = ((EType_constraint)constraint).getConstraints(null);
					if (constraints instanceof EConstraint) {
						current_step = waveInst((EConstraint)constraints, start_step);
						if (current_step > end_step) end_step = current_step;
					}
				} else {
					// nothing here, because constraints is OPTIONAL
				}

			if (constraint instanceof EExact_type_constraint) {
				// EXACT_TYPE_CONSTRAINT requires that starting AIM instance is of specific type but not it's subtype.
				// ---------------------------------------------------------------------------------------------------------------------------
			} 
    } else {
      	// internal error - unknown constraint type
		}

		return end_step;
	}
	

//#################################### INTERNALLY USED METHODS ##################################################
	
	/*
		tries to read the array of entity instances from the temp of instance of the corresponding entity_definition	
		if not yet there, reads the entity definitions by the jsdai means into an array and stores them in temp of the 
		entity definition first.
	
	  An advantage is that each constraint that needs these instances can get the array fast from the entity definition
	  without ceating new copy in the memory.
	  An additional advantage is in the implementation - in both cases when a constraint points to an attribute or to another constraint
	  the implementation of the loop through instances is now the same, and can be used for both
	*/
	EEntity [] getEntityInstances(EEntity_definition ed) throws SdaiException {
		Object temp = ed.getTemp();
		if (temp instanceof EEntity []) {
			return (EEntity [])temp;
		} else {
			AEntity instances = aim_application_model.getInstances(ed);
			int count = instances.getMemberCount();
			EEntity [] temp_array = new EEntity [count];
			for (int i = 0; i < count; i++) {
				// check if from 0 or from 1, because getByIndex(), likely list, so from 1
				temp_array[i] = (EEntity)instances.getByIndexObject(i+1);
				sortInstanceArray(temp_array, count);
				ed.setTemp(temp_array);
			}
			return temp_array;	
			
			// SdaiIterator inst_iterator = instances.createIterator();
			// while (inst_iterator.next()) {
				// EEntity instance = (EEntity)instances.getCurrentMemberObject(inst_iterator);
			
		}
	}

	void copyInstancesFromAEntity(AEntity instances) throws SdaiException {
		int count = instances.getMemberCount();
		for (int i = 0; i < count; i++) {
			// check if from 0 or from 1, because getByIndex(), likely list, so from 1
			add2InstanceArray((EEntity)instances.getByIndexObject(i+1));										
		}
	}	

//	EEntity [] getEntityInstancesExt(EEntity ee) throws SdaiException {
//		return getEntityInstancesExtNR(ee);
//	}

	// resolving so far only entity_constraint+inverse_attribute_constraint+... combinations
	// some other things may need resolving, such as inverse_attribute_constraint+...
	
	// entity_constraint filters by type without delay, which may be wrong unless the final result is the same, look into it.
	// if needed, modify entity_constraint, add resolving of all entity_constraints here, modify path_constraint element1.
	// so, currently entity_constraint+ something else than inverse_attribute_constraint has no delay
	// entity_constraint+inverse_attribute_constraint+... has delay only for resolving the inverse_attribute_constraint
	// but not the entity_constraint itself.
	// in other words, instead of performing comparison (intersection) of element2 array and element1 array and afterwards filtering by type,
	// I currently first filter by type element1 and then compare (intersect) it with element2 arary
	// question: is the result the same? - it seems to be the same 
	
	// ok, added also resolving of inverse_attribute_constraint
	
	EEntity [] getEntityInstancesExt(EEntity ee) throws SdaiException {
		if (ee instanceof EEntity_constraint) {
			// see if its attribute is inverse_attribute_constraint, if so, resolve
						
			EEntity attribute = null;
			if (((EEntity_constraint)ee).testAttribute(null)) {
				attribute = ((EEntity_constraint)ee).getAttribute(null);
			} else {
				printError("mandatory attribute attribute unset in entity_constraint: " + ee);
			}			 				
			if (attribute instanceof EInverse_attribute_constraint) {
		
			  EDefined_type [] select_path = new EDefined_type[10];
				// now really resolve
				// BTW, the domain filtering is already done

			 	Object temp = attribute.getTemp();
				// already must be processed and contain an array of suitable filtered instances
				if (!(temp instanceof EEntity [])) {
					printError("internal error, the constraint should have been already processed: " + attribute);
					return (new EEntity [0]);
				} 
				EEntity [] instances = (EEntity [])temp;

				EEntity inverted_attribute = null;
				if (((EInverse_attribute_constraint)attribute).testInverted_attribute(null)) {
					inverted_attribute = ((EInverse_attribute_constraint)attribute).getInverted_attribute(null);
				} else {
					printError("mandatory attribute inverted_attribute unset in inverse_attribute_constraint: " + attribute);
				}

				if (inverted_attribute instanceof EExplicit_attribute) {
								
								
					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];

						if (instance.testAttribute((EAttribute)inverted_attribute, select_path) == 1) {
							EEntity value = (EEntity)instance.get_object((EAttribute)inverted_attribute);
							add2InstanceArray(value);										
						} else {
							printWarning("inverse_attribute_constraint result instance has inverted_attribute unset: " + instance);
 						}
					} // for	
					// requires sorting!! 
					sortInstanceArray();
					// printInstanceArray("entity_constraint after resolving, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
//					moveInstanceArray2Constraint((EConstraint)ee);
					EEntity [] new_array = new EEntity [instance_array_count];
					System.arraycopy(instance_array, 0, new_array, 0, instance_array_count);
					clearInstanceArray();
					return new_array;
				} else 
				if (inverted_attribute instanceof EAggregate_member_constraint) {

					int member = Integer.MIN_VALUE;
					if (((EAggregate_member_constraint)inverted_attribute).testMember(null)) {
						member = ((EAggregate_member_constraint)inverted_attribute).getMember(null);
					}
					EEntity attribute2 = null;
					if (((EAggregate_member_constraint)inverted_attribute).testAttribute(null)) {
						attribute2 = ((EAggregate_member_constraint)inverted_attribute).getAttribute(null);
					} else {
						printError("mandatory attribute attribute unset in aggregate_member_constraint: " + inverted_attribute);
					}

					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];

						if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
							AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
							if (member != Integer.MIN_VALUE) {
								if (member <= agg_elements.getMemberCount()) {
									EEntity value = (EEntity)agg_elements.getByIndexObject(member);
									add2InstanceArray(value);										
								}
							} else {
								SdaiIterator it = agg_elements.createIterator();
								while (it.next()) {
									EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
									add2InstanceArray(value);										
								}
							}
											
						} else {
							printWarning("aggregate_member_constraint result instance has attribute unset: " + instance);
						}
											
					} // for	
					// requires sorting !! - not if we do not resolve values here
					sortInstanceArray();
					// printInstanceArray("exact_entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
//				 	moveInstanceArray2Constraint((EConstraint)ee);
					EEntity [] new_array = new EEntity [instance_array_count];
					System.arraycopy(instance_array, 0, new_array, 0, instance_array_count);
					clearInstanceArray();
					return new_array;
//					return getEntityInstancesExtNR(ee);

				} else {
					printError("NOT YET SUPPORTED OR WRONG inverted_attribute_type in inverse_attribute_constraint: " + inverted_attribute);
					return (new EEntity [0]);
				}
				
			} else {
				return getEntityInstancesExtNR(ee);
			}

		} else 
		if (ee instanceof EInverse_attribute_constraint) {

			  EDefined_type [] select_path = new EDefined_type[10];
				// now really resolve
				// BTW, the domain filtering is already done

			 	Object temp = ee.getTemp();
				// already must be processed and contain an array of suitable filtered instances
				if (!(temp instanceof EEntity [])) {
					printError("internal error, the constraint should have been already processed: " + ee);
					return (new EEntity [0]);
				} 
				EEntity [] instances = (EEntity [])temp;

				EEntity inverted_attribute = null;
				if (((EInverse_attribute_constraint)ee).testInverted_attribute(null)) {
					inverted_attribute = ((EInverse_attribute_constraint)ee).getInverted_attribute(null);
				} else {
					printError("mandatory attribute inverted_attribute unset in inverse_attribute_constraint: " + ee);
				}

				if (inverted_attribute instanceof EExplicit_attribute) {
								
								
					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];

						if (instance.testAttribute((EAttribute)inverted_attribute, select_path) == 1) {
							EEntity value = (EEntity)instance.get_object((EAttribute)inverted_attribute);
							add2InstanceArray(value);										
						} else {
							printWarning("inverse_attribute_constraint result instance has inverted_attribute unset: " + instance);
 						}
					} // for	
					// requires sorting!! 
					sortInstanceArray();
					// printInstanceArray("entity_constraint after resolving, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
//					moveInstanceArray2Constraint((EConstraint)ee);
					EEntity [] new_array = new EEntity [instance_array_count];
					System.arraycopy(instance_array, 0, new_array, 0, instance_array_count);
					clearInstanceArray();
					return new_array;
				} else 
				if (inverted_attribute instanceof EAggregate_member_constraint) {

					int member = Integer.MIN_VALUE;
					if (((EAggregate_member_constraint)inverted_attribute).testMember(null)) {
						member = ((EAggregate_member_constraint)inverted_attribute).getMember(null);
					}
					EEntity attribute2 = null;
					if (((EAggregate_member_constraint)inverted_attribute).testAttribute(null)) {
						attribute2 = ((EAggregate_member_constraint)inverted_attribute).getAttribute(null);
					} else {
						printError("mandatory attribute attribute unset in aggregate_member_constraint: " + inverted_attribute);
					}

					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];

						if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
							AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
							if (member != Integer.MIN_VALUE) {
								if (member <= agg_elements.getMemberCount()) {
									EEntity value = (EEntity)agg_elements.getByIndexObject(member);
									add2InstanceArray(value);										
								}
							} else {
								SdaiIterator it = agg_elements.createIterator();
								while (it.next()) {
									EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
									add2InstanceArray(value);										
								}
							}
											
						} else {
							printWarning("aggregate_member_constraint result instance has attribute unset: " + instance);
						}
											
					} // for	
					// requires sorting !! - not if we do not resolve values here
					sortInstanceArray();
					// printInstanceArray("exact_entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
//				 	moveInstanceArray2Constraint((EConstraint)ee);
					EEntity [] new_array = new EEntity [instance_array_count];
					System.arraycopy(instance_array, 0, new_array, 0, instance_array_count);
					clearInstanceArray();
					return new_array;
//					return getEntityInstancesExtNR(ee);

				} else {
					printError("NOT YET SUPPORTED OR WRONG inverted_attribute_type in inverse_attribute_constraint: " + inverted_attribute);
					return (new EEntity [0]);
				}
				
			
		} else {
			return getEntityInstancesExtNR(ee);
		}
	}

	EEntity [] getEntityInstancesExtNR(EEntity ee) throws SdaiException {
		Object temp;
		if (ee instanceof EConstraint) {
			temp = ee.getTemp();
			// already must be processed and contain an array of suitable filtered instances
			if (!(temp instanceof EEntity [])) {
				printError("internal error, the constraint should have been already processed: " + ee);
				// return null;
				// to avoid null pointer exceptions
				return (new EEntity [0]);
			} else {
				return (EEntity [])temp;
			}
		} else 
		if (ee instanceof EAttribute) {
			temp = ee.getTemp();
			if (temp instanceof EEntity []) {
				return (EEntity [])temp;
			} else {
				EEntity_definition ed = ((EAttribute)ee).getParent_entity(null);
				temp = ed.getTemp();
				if (temp instanceof EEntity []) {
					ee.setTemp(temp);
					return (EEntity [])temp;
				} else {
					AEntity instances = aim_application_model.getInstances(ed);
					int count = instances.getMemberCount();
					EEntity [] temp_array = new EEntity [count];
					int actual_count = 0;
					if (ee instanceof EExplicit_attribute) {
						for (int i = 0; i < count; i++) {
							// check if from 0 or from 1, because getByIndex(), likely list, so from 1
							EEntity current = (EEntity)instances.getByIndexObject(i+1);
							if (current.testAttribute((EExplicit_attribute)ee, select_path) > 0) {
								temp_array[actual_count++] = current;
// System.out.println("actual_count: " + actual_count + ", current: " + current);
							}
						}
					} else {
						for (int i = 0; i < count; i++) {
							// check if from 0 or from 1, because getByIndex(), likely list, so from 1
							temp_array[actual_count++] = (EEntity)instances.getByIndexObject(i+1);
						}
					}
// System.out.println("final actual_count: " + actual_count);
					EEntity [] used_temp_array = new EEntity [actual_count];
					System.arraycopy(temp_array, 0, used_temp_array, 0, actual_count);
					sortInstanceArray(used_temp_array, actual_count);
//				printInstanceArray("after sorting: ", temp_array, ee, 0);
					ee.setTemp(used_temp_array);
					ed.setTemp(used_temp_array);
					return used_temp_array;	
				}
			}
		} else
		if (ee instanceof EEntity_definition) {
			temp = ee.getTemp();
			if (temp instanceof EEntity []) {
				return (EEntity [])temp;
			} else {
				AEntity instances = aim_application_model.getInstances((EEntity_definition)ee);
				int count = instances.getMemberCount();
				EEntity [] temp_array = new EEntity [count];
				for (int i = 0; i < count; i++) {
					// check if from 0 or from 1, because getByIndex(), likely list, so from 1
					temp_array[i] = (EEntity)instances.getByIndexObject(i+1);
				}
				sortInstanceArray(temp_array, count);
				ee.setTemp(temp_array);
				return temp_array;	
			}
		} else
		if (ee instanceof EDefined_type) {
			temp = ee.getTemp();
			if (temp instanceof EEntity []) {
				return (EEntity [])temp;
			}
			// if it is SELECT, then take each element that is entity, ignore the others (or report as an error?)
			// we need also to support nested selects here
			HashSet entities = new HashSet();
			exploreDefinedType((EDefined_type)ee, entities);
			Iterator it = entities.iterator();
			EEntity [] all_instances = new EEntity [INSTANCE_ARRAY_INITIAL_SIZE]; 
			int all_instances_count = 0;
			while (it.hasNext()) {
				EEntity_definition ed = (EEntity_definition)it.next();
				temp = ed.getTemp();
				if (temp instanceof EEntity []) {
//					all_instances_count = addArray(all_instances, all_instances_count, (EEntity [])temp);
					all_instances = addArray(all_instances, all_instances_count, (EEntity [])temp);
					all_instances_count += ((EEntity [])temp).length;
				} else {
					AEntity instances = aim_application_model.getInstances(ed);
					int count = instances.getMemberCount();
					EEntity [] temp_array = new EEntity [count];
					for (int i = 0; i < count; i++) {
						// check if from 0 or from 1, because getByIndex(), likely list, so from 1
						temp_array[i] = (EEntity)instances.getByIndexObject(i+1);
//						all_instances_count = add2array(all_instances, all_instances_count, (EEntity)instances.getByIndexObject(i+1));
					}
					sortInstanceArray(temp_array, count);
					ed.setTemp(temp_array);
//					all_instances_count = addArray(all_instances, all_instances_count, temp_array);
					all_instances = addArray(all_instances, all_instances_count, temp_array);
					all_instances_count += temp_array.length;
				}
			} // while
			sortInstanceArray(all_instances, all_instances_count);
			EEntity [] used_instances = new EEntity [all_instances_count];
			System.arraycopy(all_instances, 0, used_instances, 0, all_instances_count);
			ee.setTemp(used_instances);
			return used_instances;	

		} else 
		if (ee instanceof EAggregation_type) {
			temp = ee.getTemp();
			if (temp instanceof EEntity []) {
				return (EEntity [])temp;
			}
			HashSet entities = new HashSet();
			exploreAggregate((EAggregation_type) ee, entities);
			Iterator it = entities.iterator();
			EEntity [] all_instances = new EEntity [INSTANCE_ARRAY_INITIAL_SIZE]; 
			int all_instances_count = 0;
			while (it.hasNext()) {
				EEntity_definition ed = (EEntity_definition)it.next();
				temp = ed.getTemp();
				if (temp instanceof EEntity []) {
//					all_instances_count = addArray(all_instances, all_instances_count, (EEntity [])temp);
					all_instances = addArray(all_instances, all_instances_count, (EEntity [])temp);
					all_instances_count += ((EEntity [])temp).length;
				} else {
					AEntity instances = aim_application_model.getInstances(ed);
					int count = instances.getMemberCount();
					EEntity [] temp_array = new EEntity [count];
					for (int i = 0; i < count; i++) {
						// check if from 0 or from 1, because getByIndex(), likely list, so from 1
						temp_array[i] = (EEntity)instances.getByIndexObject(i+1);
//						all_instances_count = add2array(all_instances, all_instances_count, (EEntity)instances.getByIndexObject(i+1));
					}
					sortInstanceArray(temp_array, count);
					ed.setTemp(temp_array);
//					all_instances_count = addArray(all_instances, all_instances_count, temp_array);
					all_instances = addArray(all_instances, all_instances_count, temp_array);
					all_instances_count += temp_array.length;
				}
			} // while
			sortInstanceArray(all_instances, all_instances_count);
			EEntity [] used_instances = new EEntity [all_instances_count];
			System.arraycopy(all_instances, 0, used_instances, 0, all_instances_count);
			ee.setTemp(used_instances);
			return used_instances;	
		} else {
			printError("getEntityInstancesExtNR() - UNEXPECTED argument type: " + ee);
			return null;
		}
	}
	
	
	void exploreDefinedType(EDefined_type dt, HashSet entities) throws SdaiException {
		EEntity domain = dt.getDomain(null);
		if (domain instanceof EDefined_type) {
			exploreDefinedType((EDefined_type)domain, entities);
		} else 
		if (domain instanceof EAggregation_type) {
			exploreAggregate((EAggregation_type)domain, entities);
		} else
		if (domain instanceof ESimple_type) {
			//  do nothing, or perhaps a warning
		} else
		if (domain instanceof EEnumeration_type) {
			// do nothing, or perhaps a warning
		} else
		if (domain instanceof ESelect_type) {
			exploreSelectType((ESelect_type)domain, entities);
		} else {
			printError("exploreDefinedType() - internal error - wrong domain type: " + domain);
		}
	}
	
	void exploreAggregate(EAggregation_type at, HashSet entities) throws SdaiException {
		EEntity element = at.getElement_type(null);
		if (element instanceof EAggregation_type) {
			exploreAggregate((EAggregation_type)element, entities);
		} else 
		if (element instanceof EEntity_definition) {
			// add instances
			entities.add(element);
		} else
		if (element instanceof EDefined_type) {
			exploreDefinedType((EDefined_type)element, entities);
		} else
		if (element instanceof ESimple_type) {
			// do nothing, or warning?
		} else {
			printError("exploreAggregate() - internal error - wrong element type: " + element);
		}
	}
	
	void exploreSelectType(ESelect_type st, HashSet entities) throws SdaiException {
		ANamed_type selections = st.getSelections(null);
		SdaiIterator iter = selections.createIterator();		
		while (iter.next()) {
			ENamed_type nt = selections.getCurrentMember(iter);
			if (nt instanceof EEntity_definition) {
				// add instances
				entities.add(nt);
			} else 
			if (nt instanceof EDefined_type) {
				exploreDefinedType((EDefined_type)nt, entities);
			} else {
				printError("exploreSelectType() - internal error - wrong element type: " + nt);
			}	
		}
	}
	
	
	


	void initInstanceArray() {
		instance_array = new EEntity [INSTANCE_ARRAY_INITIAL_SIZE];
		instance_array_count = 0;
	}

	void clearInstanceArray() {
		for (int i = 0; i < instance_array_count; i++) {
			instance_array[i] = null;
		}		
		instance_array_count = 0;
	}

	void add2InstanceArray(EEntity inst) {
		ensureInstanceArrayCapacity();
		instance_array[instance_array_count++] = (EEntity)inst;
	}
	
	
	void moveInstanceArray2Constraint_no_duplicate_removal(EConstraint constraint) {
		EEntity [] new_array = new EEntity [instance_array_count];
		System.arraycopy(instance_array, 0, new_array, 0, instance_array_count);
		constraint.setTemp(new_array);
		clearInstanceArray();
	}

	// another implementation of the above method, but with removal of duplicate instances
	// The instance_array is sorted by hashcodes at this point
	// I am assuming here that duplicate instances have the same hashcode and are therefore side-by-side
	void moveInstanceArray2Constraint(EConstraint constraint) {
		if (instance_array_count > 0) {
			EEntity [] no_duplicates = new EEntity [instance_array_count];
			no_duplicates[0] = instance_array[0];
			int i = 1;
			int j = 0;
			while (i < instance_array_count) {
				if (no_duplicates[j] == instance_array[i]) {
					i++;
				} else {
					no_duplicates[++j] = instance_array[i++];
				}
			}
			j++;
			EEntity [] new_array = new EEntity [j];
			System.arraycopy(no_duplicates, 0, new_array, 0, j);
			constraint.setTemp(new_array);
			clearInstanceArray();
		} else {
			constraint.setTemp(new EEntity [0]);
		}
	}

	

	EEntity [] addArray(EEntity [] to, int count, EEntity [] from) {
		to = ensureArrayCapacity(to, count, from);
		System.arraycopy(from, 0, to, count, from.length);
		return to;
	}
	
	EEntity [] ensureArrayCapacity(EEntity [] to, int count, EEntity [] from) {
	
		int new_length;
		if (to.length - count < from.length) {
			new_length = to.length + from.length - (to.length - count) + INSTANCE_ARRAY_SIZE_STEP;
			EEntity [] new_array = new EEntity [new_length];
			System.arraycopy(to, 0, new_array, 0, count);
			for (int i = 0; i < count; i++) {
				to[i] = null;
			}		
			to = new_array;
		}
		return to;
	}	


	
	// the implementation here can be replaced by a faster one, if available
	void sortInstanceArray(EEntity [] an_instance_array, int an_instance_array_count) {
		Arrays.sort(an_instance_array,0,an_instance_array_count, sorter);
	}

	void sortInstanceArray() {
		sortInstanceArray(instance_array, instance_array_count);
	}
	
	// the implementation here can be replaced by a faster one, if available
	int searchInstanceArray(EEntity [] instance_array, EEntity inst) {
				return (Arrays.binarySearch(instance_array, inst, sorter));
	}
	
	
	void sortInstanceArrayG() throws SdaiException {
		if (instance_array_count < 2) return;
		
		EEntity [] sorted_instances = new EEntity [instance_array_count];
		
		System.arraycopy(instance_array, 0, sorted_instances, 0, instance_array_count);
		

		sortInstances(sorted_instances, instance_array, 0, instance_array_count);

		// for (int i = 0; i < instance_array_count; i++) {
			// instance_array[i] = null;
		// }		
		// instance_array = sorted_instances;
		

	}

 
  EEntity [] filterByTarget(EEntity [] instances, EEntity target) throws SdaiException {
  	if (target instanceof EEntity_definition) {
  		EEntity_definition target_entity = (EEntity_definition)target;
  		for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				if (instance.isKindOf(target_entity)) {
					add2InstanceArray(instance);										
  			}
  		}
			EEntity [] result = new EEntity [instance_array_count];
			System.arraycopy(instance_array, 0, result, 0, instance_array_count);
			clearInstanceArray();
			return result;
	 	} else {
  		// it may also be an attribute, but what does it mean?
  		printError("NOT YET SUPPORTED target type: " + target);
  		return instances;
  	}
  }


	// difference between from array and what array - substract what from from
	// the result is an array of instances of from that are not present in what
	
	// possible further optimizations - to performe hashCode() only when an index changes, copy the remainder of the array with System.arraycopy(), but first - ensure capacity
	EEntity [] performDifference(EEntity [] from, EEntity [] what) {
		if (from.length == 0) {
			//  whatever substracted from nothing = nothing
			return from;
		} else
		if (what.length == 0) {
			// substracting nothing does not change the array
			return from;
		} else {
			int hash1; int hash2;
			int i1 = 0;
			int i2 = 0;
			while ((i1 < from.length) && (i2 < what.length)) {
				hash1 = from[i1].hashCode();								
				hash2 = what[i2].hashCode();								
				if (hash1 == hash2) {
					i1++;
					i2++;
				} else 
				if (hash1 < hash2) {
					add2InstanceArray(from[i1]);										
					i1++;
				} else {
					i2++;
				}
			}
			// the case when from array has (ends with) higher hash values than what array
			// in that case continue - the remainder of from all belongs to the result
			while (i1 < from.length) {
				add2InstanceArray(from[i1]);
				i1++;
			}
			
			EEntity [] result = new EEntity [instance_array_count];
			System.arraycopy(instance_array, 0, result, 0, instance_array_count);
			clearInstanceArray();
			return result;
		}
		
	}


	// possible further optimization - to perform hashCode() only when an index changes
	EEntity [] performAnd(EEntity [] elements1, EEntity [] elements2)  {							
		int hash1; int hash2;
		int i1 = 0;
		int i2 = 0;
		while ((i1 < elements1.length) && (i2 < elements2.length)) {
			hash1 = elements1[i1].hashCode();								
			hash2 = elements2[i2].hashCode();								
			if (hash1 == hash2) {
				add2InstanceArray(elements1[i1]);										
				i1++;
				i2++;
			} else 
			if (hash1 < hash2) {
				i1++;
			} else {
				i2++;
			}
		}
		EEntity [] result = new EEntity [instance_array_count];
		System.arraycopy(instance_array, 0, result, 0, instance_array_count);
		clearInstanceArray();
		return result;
	} // end of method	



	// perhaps this is faster - check. 
	// further possible optimization - to write the remainder of the longer array all at once with System.arraycopy,
	// buf first, increase the array if needed
	EEntity [] performUnion_optimized(EEntity [] elements1, EEntity [] elements2)  {							
//System.out.println("### count1: " + elements1.length + ", count2: " + elements2.length);		

		if (elements1.length == 0) {
			return elements2;
		} else 
		if (elements2.length == 0) {
			return elements1;
		} else {
			int hash1; int hash2;
			int i1 = 0;
			int i2 = 0;
			hash1 = elements1[i1].hashCode();								
			hash2 = elements2[i2].hashCode();								
			for (;;) {
				if (hash1 == hash2) {
					add2InstanceArray(elements1[i1]);										
					i1++;
					i2++;
					if (i1 < elements1.length) {
						hash1 = elements1[i1].hashCode();								
					} else {
						break;
					}
					if (i2 < elements2.length) {
						hash2 = elements2[i2].hashCode();								
					} else {
						break;
					}
				} else 
				if (hash1 < hash2) {
					add2InstanceArray(elements1[i1]);										
					i1++;
					if (i1 < elements1.length) {
						hash1 = elements1[i1].hashCode();								
					} else {
						break;
					}
				} else {
					add2InstanceArray(elements2[i2]);										
					i2++;
					if (i2 < elements2.length) {
						hash2 = elements2[i2].hashCode();								
					} else {
						break;
					}
				}
			}
			// at this point, one operand array ended, but the other one - probably did not.
			// so copy the remainder of the other array
			if (i1 < elements1.length) {
				while (i1 < elements1.length) {
					add2InstanceArray(elements1[i1]);										
					i1++;
				}
			} else {
				// must be i2 < element2.length
				while (i2 < elements2.length) {
					add2InstanceArray(elements2[i2]);										
					i2++;
				}
			}
// System.out.println("### count after: " + instance_array_count);		
			EEntity [] result = new EEntity [instance_array_count];
			System.arraycopy(instance_array, 0, result, 0, instance_array_count);
			clearInstanceArray();
			return result;
		}	
	} // end of method	


	EEntity [] performUnion(EEntity [] elements1, EEntity [] elements2)  {							
//System.out.println("### count1: " + elements1.length + ", count2: " + elements2.length);		

		if (elements1.length == 0) {
			return elements2;
		} else 
		if (elements2.length == 0) {
			return elements1;
		} else {
			int hash1; int hash2;
			int i1 = 0;
			int i2 = 0;
			while ((i1 < elements1.length) && (i2 < elements2.length)) {
				hash1 = elements1[i1].hashCode();								
				hash2 = elements2[i2].hashCode();								
				if (hash1 == hash2) {
					add2InstanceArray(elements1[i1]);										
					i1++;
					i2++;
				} else 
				if (hash1 < hash2) {
					add2InstanceArray(elements1[i1]);										
					i1++;
				} else {
					add2InstanceArray(elements2[i2]);										
					i2++;
				}
			}
			// at this point, one operand array ended, but the other one - probably did not.
			// so copy the remainder of the other array
			if (i1 < elements1.length) {
				while (i1 < elements1.length) {
					add2InstanceArray(elements1[i1]);										
					i1++;
				}
			} else {
				// must be i2 < element2.length
				while (i2 < elements2.length) {
					add2InstanceArray(elements2[i2]);										
					i2++;
				}
			}
// System.out.println("### count after: " + instance_array_count);		
			EEntity [] result = new EEntity [instance_array_count];
			System.arraycopy(instance_array, 0, result, 0, instance_array_count);
			clearInstanceArray();
			return result;
		}	
	} // end of method	


	void ensureInstanceArrayCapacity() {
		
		int new_length;
		if (instance_array_count >= instance_array.length) {
			if (instance_array_count < INSTANCE_ARRAY_SIZE_STEP) {
				new_length = instance_array_count * 2;
			} else {
				new_length = instance_array_count + INSTANCE_ARRAY_SIZE_STEP;
			}
			EEntity [] new_array = new EEntity [new_length];
			System.arraycopy(instance_array, 0, new_array, 0, instance_array_count);
			for (int i = 0; i < instance_array_count; i++) {
				instance_array[i] = null;
			}		
			instance_array = new_array;
		}
	}	

		

	void sortInstances(EEntity [] s_instances, EEntity [] instances, int start_index, int end_index) throws SdaiException  {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; (j > start_index) && (getInstance_identifier(instances[j-1]) > getInstance_identifier(instances[j])); j--) {
					EEntity obj = instances[j-1];
					instances[j-1] = instances[j];
					instances[j] = obj;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortInstances(instances, s_instances, start_index, middle);
		sortInstances(instances, s_instances, middle, end_index);
		if (getInstance_identifier(s_instances[middle-1]) <= getInstance_identifier(s_instances[middle])) {
			System.arraycopy(s_instances, start_index, instances, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (((n>=end_index) || (m<middle) && (getInstance_identifier(s_instances[m]) <= getInstance_identifier(s_instances[n])))) {
				instances[i] = s_instances[m++];
			} else {
				instances[i] = s_instances[n++];
			}
		}
	}

	void sortInstancesBackup(EEntity [] s_instances, EEntity [] instances, int start_index, int end_index) throws SdaiException  {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j > start_index && getInstance_identifier(instances[j-1]) > getInstance_identifier(instances[j]); j--) {
					EEntity obj = instances[j-1];
					instances[j-1] = instances[j];
					instances[j] = obj;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortInstances(instances, s_instances, start_index, middle);
		sortInstances(instances, s_instances, middle, end_index);
		if (getInstance_identifier(s_instances[middle-1]) <= getInstance_identifier(s_instances[middle])) {
			System.arraycopy(s_instances, start_index, instances, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle && getInstance_identifier(s_instances[m]) <= getInstance_identifier(s_instances[n])) {
				instances[i] = s_instances[m++];
			} else {
				instances[i] = s_instances[n++];
			}
		}
	}

	void sortInstancesM(EEntity [] s_instances, EEntity [] instances, int start_index, int end_index) throws SdaiException  {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j > start_index && getInstance_identifier(instances[j-1]) > getInstance_identifier(instances[j]); j--) {
					EEntity obj = instances[j-1];
					instances[j-1] = instances[j];
					instances[j] = obj;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortInstances(instances, s_instances, start_index, middle);
		sortInstances(instances, s_instances, middle, end_index);
		if (getInstance_identifier(s_instances[middle-1]) <= getInstance_identifier(s_instances[middle])) {
			System.arraycopy(s_instances, start_index, instances, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle && getInstance_identifier(s_instances[m]) <= getInstance_identifier(s_instances[n])) {
				instances[i] = s_instances[m++];
			} else {
				instances[i] = s_instances[n++];
			}
		}
	}

	void sortInstancesR(EEntity [] s_instances, EEntity [] instances, int start_index, int end_index) throws SdaiException  {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j > start_index && getInstance_identifier(instances[j-1]) < getInstance_identifier(instances[j]); j--) {
					EEntity obj = instances[j-1];
					instances[j-1] = instances[j];
					instances[j] = obj;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortInstances(instances, s_instances, start_index, middle);
		sortInstances(instances, s_instances, middle, end_index);
		if (getInstance_identifier(s_instances[middle-1]) >= getInstance_identifier(s_instances[middle])) {
			System.arraycopy(s_instances, start_index, instances, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle && getInstance_identifier(s_instances[m]) >= getInstance_identifier(s_instances[n])) {
				instances[i] = s_instances[m++];
			} else {
				instances[i] = s_instances[n++];
			}
		}
	}



	int getInstance_identifier(EEntity inst) throws SdaiException {
	//		String str = inst.getPersistentLabel().substring(1);
	//		int str_int = Integer.parseInt(str, 10);
			

		//	printDebug("in getInstance_identifier, str: " + str +  ", int: " + str_int + " inst: " + inst);
  		return Integer.parseInt(inst.getPersistentLabel().substring(1), 10);
	}


  static ESchema_definition getSchema_definitionFromModel(SdaiModel sm) throws SdaiException {
    Aggregate ia = sm.getEntityExtentInstances(ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    while (iter_inst.next()) {
      ESchema_definition inst = (ESchema_definition) ia.getCurrentMemberObject(iter_inst);

      return inst;
    }

    return null;
  }

	// ####################### PROCESSING CONSTRAINTS bottom-up ###########
	
	void processAggregate_member_constraint(EAggregate_member_constraint constraint) throws SdaiException {
		printDebug("aggregate_member_constraint: " + constraint);
		// An aggregate_member_constraint is a constraint that constraint aggregates. 
		// It selects one specific or arbitrary element from aggregate. The attribute must point to an attribute of type aggregate.
		// ---------------------------------------------------------------------------------------------------------------------------
	
		// If set, defines element in aggregate that must meet requirement, e.g. [1], only valid for LIST and ARRAY but misused by APs.
		// If unset, there shall be at least one member. Notation [i]
		// member : OPTIONAL INTEGER;

		// may not be needed, see later
		int member = Integer.MIN_VALUE;
		if (constraint.testMember(null)) {
			member = constraint.getMember(null);
		}

		// An attribute must be of aggregate type. One element of is this aggregate is selected using aggregate_member_constraint.
		// attribute : aggregate_member_constraint_select;
		EEntity attribute = null;
		if (constraint.testAttribute(null)) {
			attribute = constraint.getAttribute(null);
		} else {
			printError("mandatory attribute attribute unset in aggregate_member_constraint: " + constraint);
		}

			
		EEntity [] instances = getEntityInstancesExt(attribute);
		printInstanceArray("aggregate_member_constraint, attribute: " + attribute + ", member: " + member, instances, constraint, 3);
		constraint.setTemp(instances);
	} // processAggregate_member_constraint();

	void processAggregate_size_constraint(EAggregate_size_constraint constraint) throws SdaiException {
		printDebug("aggregate_size_constraint: " + constraint);
		// An aggregate_size_constraint is a constraint that constraint how many elements may be in aggregate. 
		// It selects one specific or arbitrary element from aggregate. The attribute must point to an attribute of type aggregate.
		// Since v25.
		// This constraint may introduce some changes in selects where valind constraints for other constraints are listed.
		// ---------------------------------------------------------------------------------------------------------------------------

		// Defines size of aggregate.
		// size : INTEGER;
		int size = Integer.MIN_VALUE;
		if (constraint.testSize(null)) {
			size = constraint.getSize(null);
		} else {
			printError("mandatory attribute size unset in aggregate_size_constraint: " + constraint);
		}

		// An attribute must be of aggregate type. One element of is this aggregate is selected using aggregate_member_constraint.
		// attribute : aggregate_member_constraint_select;
		EEntity attribute = null;
		if (constraint.testAttribute(null)) {
			attribute = constraint.getAttribute(null);
		} else {
			printError("mandatory attribute attribute unset in aggregate_size_constraint: " + constraint);
		}
		
		printError("NOT YET SUPPORTED aggregate_size_constraint: " + constraint);

	} // processAggregate_size_constraint()

	void processAttribute_value_constraint(EAttribute_value_constraint constraint) throws SdaiException {
		// An attirubute_value_constraint is constraint that is used to restrict attributes 
		// or elements of aggregates to some specific simple value.
		// ---------------------------------------------------------------------------------------------------------------------------
		
		// An attribute is definition of attribute or aggregate that is constraint to some value.
		// attribute : attribute_value_constraint_select;
		// attribute
	 	// aggregate_member_constraint
	 	// select_constraint


		EEntity attribute = null;
		if (constraint.testAttribute(null)) {
			attribute = constraint.getAttribute(null);
		} else {
			printError("mandatory attribute attribute unset in attribute_value_constraint: " + constraint);
		}
		
		if (constraint instanceof EBoolean_constraint) {
			processBoolean_constraint((EBoolean_constraint) constraint, attribute);  
		} else
		if (constraint instanceof EEnumeration_constraint) {
			processEnumeration_constraint((EEnumeration_constraint)constraint, attribute);
		} else
		if (constraint instanceof EInteger_constraint) {
			processInteger_constraint((EInteger_constraint)constraint, attribute);
		} else
		if (constraint instanceof ELogical_constraint) {
			processLogical_constraint((ELogical_constraint)constraint, attribute);
		} else
		if (constraint instanceof ENon_optional_constraint) {
			processNon_optional_constraint((ENon_optional_constraint)constraint, attribute);
		} else
		if (constraint instanceof EReal_constraint) {
			processReal_constraint((EReal_constraint)constraint, attribute);
		} else
		if (constraint instanceof EString_constraint) {
			processString_constraint((EString_constraint)constraint, attribute);
		}		 			
	} // processAttribute_value_constraint()				 		


	void processBoolean_constraint(EBoolean_constraint constraint, EEntity attribute) throws SdaiException {  
		printDebug("boolean_constraint: " + constraint);
		// A booleant_constraint specifies a constraint on boolean value. 
		// Attribute must point to attribute definition or aggregate or select element 
		// (in this case select element that is defined type) that is of type boolean.
		// -------------------------------------------------------------------------------------------------------------------------

		// A constraint_value is value to which the attribute is constraint.
		// constraint_value : BOOLEAN;
		boolean constraint_value = false;
		if (constraint.testConstraint_value(null)) {
			constraint_value = constraint.getConstraint_value(null);
		} else {
			printError("mandatory attribute constraint_value unset in boolean_constraint: " + constraint);
		}

		printDebug("attribute: " + attribute);
		printDebug("constraint_value: " + constraint_value);

		if (attribute instanceof EAttribute) {
			AEntity instances = aim_application_model.getInstances(((EAttribute)attribute).getParent_entity(null));
			SdaiIterator inst_iterator = instances.createIterator();

			while (inst_iterator.next()) {
				EEntity instance = (EEntity)instances.getCurrentMemberObject(inst_iterator);
				// printDebug("instance: " + instance);	
				// read the specified attribute value:
				if (instance.testAttribute((EAttribute)attribute, select_path) == 2) {
					boolean value = instance.get_boolean((EAttribute)attribute);
					if (value == constraint_value) {
//						printDebug("include this instance: " + instance);
						add2InstanceArray(instance);										
					}
				} else {
					printWarning("in boolean_constraint, attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
				}
			}
			// sorting not needed, the result is already sorted
			printInstanceArray("boolean_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);

		} else {
			printError("NOT YET SUPPORTED OR WRONG boolean_constraint attribute type: " + attribute);
		}
	} // processBoolean_constraint()							

	void processEnumeration_constraint(EEnumeration_constraint constraint, EEntity attribute) throws SdaiException {
		printDebug("enumeration_constraint: " + constraint);
		// An enumeration_constraint specifies a constraint on enumeration value. 
		// Attribute must point to attribute that is of type enumeration.
		// -------------------------------------------------------------------------------------------------------------------------
	
		// A constraint_value defines a constant to which constraint attribute must be equal.
		// constraint_value : express_id;
		String constraint_value = null;
		if (constraint.testConstraint_value(null)) {
			constraint_value = constraint.getConstraint_value(null);
		} else {
			printError("mandatory attribute constraint_value unset in enumeration_constraint: " + constraint);
		}
		printDebug("enumeration_constraint");
		printDebug("attribute: " + attribute);
		printDebug("constraint_value: " + constraint_value);

		EEntity [] instances = getEntityInstancesExt(attribute); 
		if (attribute instanceof EAttribute) {
			for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				if (instance.testAttribute((EAttribute)attribute, select_path) == 2) {
					int value = instance.get_int((EAttribute)attribute);

					EEntity enum_domain = ((EExplicit_attribute)attribute).getDomain(null);
					// System.out.println("DOMAIN: " + enum_domain);
					if (enum_domain instanceof EDefined_type) {
						enum_domain = ((EDefined_type)enum_domain).getDomain(null);
					}

					EEnumeration_type enumType = (EEnumeration_type)enum_domain;
					// EEnumeration_type enum = (EEnumeration_type)((EExplicit_attribute)attribute).getDomain(null);
					A_string	ee = enumType.getElements(null, null);
					if (ee.getByIndex(value).equals(constraint_value)) {
						add2InstanceArray(instance);										
					}
				} else {
					printWarning("in enumeration_constraint, attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
				}
			}
			// sorting not needed, the result is already sorted
			printInstanceArray("enumeration_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);
		} else {
			printError("NOT YET SUPPORTED OR WRONG enumeration_constraint attribute type: " + attribute);
		}
	} // processEnumeration_constraint()

	void processInteger_constraint(EInteger_constraint constraint, EEntity attribute) throws SdaiException {
		printDebug("integer_constraint: " + constraint);
		// An integer_constraint specifies a constraint on integer value. 
		// Attribute must point to attribute that is of type integer.
		// -------------------------------------------------------------------------------------------------------------------------
	
		// A constraint_value defines a constant to which constraint attribute must be equal.
		// constraint_value : INTEGER;
		int constraint_value = Integer.MIN_VALUE;
		if (constraint.testConstraint_value(null)) {
			constraint_value = constraint.getConstraint_value(null);
		} else {
			printError("mandatory attribute constraint_value unset in integer_constraint: " + constraint);
		}
		printDebug("attribute: " + attribute);
		printDebug("constraint_value: " + constraint_value);

		EEntity [] instances = getEntityInstancesExt(attribute); 
//		if (attribute instanceof EExplicit_attribute) {
		if (attribute instanceof EAttribute) {
			for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				if (instance.testAttribute((EAttribute)attribute, select_path) == 2) {
					int value = instance.get_int((EAttribute)attribute);
					if (value == constraint_value) {
						add2InstanceArray(instance);										
					}
				} else {
					printWarning("in integer_constraint, attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
				}
			}
			// sorting not needed, the result is already sorted
			printInstanceArray("integer_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);

		} else {
			printError("NOT YET SUPPORTED OR WRONG integer_constraint attribute type: " + attribute);
		}
	} // processInteger_constraint()	

	void processLogical_constraint(ELogical_constraint constraint, EEntity attribute) throws SdaiException {
		printDebug("logical_constraint: " + constraint);
		// A logical_constraint specifies a constraint on logical value. 
		// Attribute must point to attribute that is of type logical.
		// -------------------------------------------------------------------------------------------------------------------------
	
		// A constraint_value defines a constant to which constraint attribute must be equal.
		// constraint_value : LOGICAL;
		int constraint_value = 0;
		if (constraint.testConstraint_value(null)) {
			constraint_value = constraint.getConstraint_value(null);
		} else {
			printError("mandatory attribute constraint_value unset in logical_constraint: " + constraint);
		}
		printDebug("attribute: " + attribute);
		printDebug("constraint_value: " + constraint_value);

		EEntity [] instances = getEntityInstancesExt(attribute); 
//		if (attribute instanceof EExplicit_attribute) {
		if (attribute instanceof EAttribute) {
			for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				if (instance.testAttribute((EAttribute)attribute, select_path) == 2) {
					int value = instance.get_int((EAttribute)attribute);
					if (value == constraint_value) {
						add2InstanceArray(instance);										
					}
				} else {
					printWarning("in logical_constraint, attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
				}
			}
			// sorting not needed, the result is already sorted
			printInstanceArray("logical_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);

		} else {
			printError("NOT YET SUPPORTED OR WRONG logical_constraint attribute type: " + attribute);
		}
	} // processLogical_constraint()

	void processNon_optional_constraint(ENon_optional_constraint constraint, EEntity attribute) throws SdaiException {
		printDebug("non_optional_constraint: " + constraint);
		// A non_optional_constraint restricts optional attribute of entity to be non-optional.
		// -------------------------------------------------------------------------------------------------------------------------
		
		// hopefully, this attribute is declared as optional, here we need to take all the instaces that have it set to a value
		printDebug("attribute: " + attribute);

		EEntity [] instances = getEntityInstancesExt(attribute); 
		if (attribute instanceof EAttribute) {
			for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				if (instance.testAttribute((EAttribute)attribute, select_path) > 0) {
					add2InstanceArray(instance);										
				}
			}
			// sorting not needed, the result is already sorted
			printInstanceArray("non_optional_constraint, attribute: " + attribute, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);
		} else {
			printError("NOT YET SUPPORTED OR WRONG non_optional_constraint attribute type: " + attribute);
		}
	} // processNon_optional_constraint		

	void processReal_constraint(EReal_constraint constraint, EEntity attribute) throws SdaiException {
		printDebug("real_constraint: " + constraint);
		// A real_constraint specifies a constraint on real value. Attribute must point to attribute that is of type real.
		// -------------------------------------------------------------------------------------------------------------------------
	
		// A constraint_value defines a constant to which constraint attribute must be equal.
		// constraint_value : REAL;
		double constraint_value = Double.NaN;
		if (constraint.testConstraint_value(null)) {
			constraint_value = constraint.getConstraint_value(null);
		} else {
			printError("mandatory attribute constraint_value unset in real_constraint: " + constraint);
		}
		printDebug("attribute: " + attribute);
		printDebug("constraint_value: " + constraint_value);
		EEntity [] instances = getEntityInstancesExt(attribute); 
		if (attribute instanceof EAttribute) {
			for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				if (instance.testAttribute((EAttribute)attribute, select_path) == 3) {
					double value = instance.get_double((EAttribute)attribute);
					if (value == constraint_value) {
						add2InstanceArray(instance);										
					}
				} else {
					printWarning("in real_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
				}
			}
			// sorting not needed, the result is already sorted
			printInstanceArray("real_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);
		} else							
		if (attribute instanceof EAggregate_member_constraint) {
			int member = Integer.MIN_VALUE;
			if (((EAggregate_member_constraint)attribute).testMember(null)) {
				member = ((EAggregate_member_constraint)attribute).getMember(null);
				
			}
			EEntity attribute2 = null;
			if (((EAggregate_member_constraint)attribute).testAttribute(null)) {
				attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
			} else {
				printError("mandatory attribute attribute unset in aggregate_member_constraint in real_constraint: " + constraint);
			}
			// printDebug("member is: " + member + ", attribute: " + attribute2);
			if (attribute2 instanceof EAttribute) {
				for (int i = 0; i < instances.length; i++) {
					EEntity instance = instances[i];
					if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
						A_double agg_elements = (A_double)instance.get_object((EAttribute)attribute2);
						if (member != Integer.MIN_VALUE) {
							// printDebug("aggr: " + agg_elements);
							// printDebug("member: " + member + ", count: " + agg_elements.getMemberCount());
							//  member = 0 - exception (as should be), member = 2 - exception (why?), only member = 1 seems to be ok, but member count is 2
							if (member <= agg_elements.getMemberCount()) {
								double value = agg_elements.getByIndex(member);
								if (value == constraint_value) {
									add2InstanceArray(instance);										
								}
							}
						} else {
							// check all elements of the aggregate, but when to return the instance, if at least one is ok?
							// this case does not occur so far
							SdaiIterator it = agg_elements.createIterator();
							while (it.next()) {
								double value = agg_elements.getCurrentMember(it);
								if (value == constraint_value) {
									add2InstanceArray(instance);										
									break;
								}
							}
						}
					} else {
						printWarning("in real_constraint, in aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute2);
					}
				} // for
				// sorting not needed, the result is already sorted
				printInstanceArray("real_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
				moveInstanceArray2Constraint(constraint);
			} else {
				printError("NOT YET SUPPORTED OR WRONG real_constraint attribute = aggregate_member_constraint attribute type: " + attribute);
			}
		} else {
			printError("NOT YET SUPPORTED OR WRONG real_constraint attribute type: " + attribute);
		}
	} // processReal_constraint()		

	void processString_constraint(EString_constraint constraint, EEntity attribute) throws SdaiException {
		printDebug("string_constraint: " + constraint);
		// A String_constraint specifies a constraint on string value. Attribute must point to attribute that is of type string.
		// -------------------------------------------------------------------------------------------------------------------------
	
		// A constraint_value defines a constant to which constraint attribute must be equal.
		// constraint_value : STRING;
		String constraint_value = null;
		if (constraint.testConstraint_value(null)) {
			constraint_value = constraint.getConstraint_value(null);
		} else {
			printError("mandatory attribute constraint_value unset in string_constraint: " + constraint);
		}
		printDebug("attribute: " + attribute);
		printDebug("constraint_value: " + constraint_value);
		
		EEntity [] instances = getEntityInstancesExt(attribute); 
		if (attribute instanceof EAttribute) {
			for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				if (instance.testAttribute((EAttribute)attribute, select_path) == 1) {
					String value = (String)instance.get_object((EAttribute)attribute);
					if (value.equals(constraint_value)) {
						add2InstanceArray(instance);										
					}
				} else {
					printWarning("in string_constraint, attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
				}
			}
			// sorting not needed, the result is already sorted
			printInstanceArray("string_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
			// sortInstanceArrayG();
			// printInstanceArray("GINTARO: string_constraint, constraint_value: " + constraint_value + ", attribute: " + attribute, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);
		} else {
			printError("NOT YET SUPPORTED OR WRONG string_constraint attribute type: " + attribute);
		}
	} // processString_constraint()	

	void processEntity_constraint(EEntity_constraint constraint) throws SdaiException {
		printDebug("entity_constraint: " + constraint);
		// An entity_constraint specifies a constraint on entity type. 
		// The attribute must point to an attribute or constraint definition that is of an entity type. 
		// This constraint restricts to subtypes or select types (maybe to complex types).
		// ---------------------------------------------------------------------------------------------------------------------------
		
		// A domain defines an entity type. The value of constraint attribute (it also may be element of aggregate) must by of this type.
		// domain : entity_definition;
		EEntity_definition domain = null;
		if (constraint.testDomain(null)) {
			domain = constraint.getDomain(null);
		} else {
			printError("mandatory attribute domain unset in entity_constraint: " + constraint);
		}
		
		// An attribute is a definition of attribute that is constraint to some entity type.
		// attribute : attribute_select;
		EEntity attribute = null;
		if (constraint.testAttribute(null)) {
			attribute = constraint.getAttribute(null);
		} else {
			printError("mandatory attribute attribute unset in entity_constraint: " + constraint);
		}			 				

		// occuring cases: attribute = attribute, aggregate_member_constraint, inverse_attribute_constraint

		EEntity [] instances = getEntityInstancesExt(attribute);

		if (constraint instanceof EExact_entity_constraint) {
			// An exact_entity_constraint is entity constraint that constraints the attribute to be of some specific entity type, 
			// excluding subtypes of it.
			// -------------------------------------------------------------------------------------------------------------------------
			printDebug("exact_entity_constraint - domain: " + domain + ", attribute: " + attribute, 4);

			if (attribute instanceof EExplicit_attribute) {
				// check this implementation with the big picture in mind
				for (int i = 0; i < instances.length; i++) {
					EEntity instance = instances[i];
					if (instance.testAttribute((EAttribute)attribute, select_path) == 1) {
						EEntity value = (EEntity)instance.get_object((EAttribute)attribute);
						if (value.isInstanceOf(domain)) {
							// do we add here instance (instance of the attribute parent entity) or value (instance to which the attribute points)?
							add2InstanceArray(instance);										
						}
					} else {
						printWarning("attribute value unset in exact_entity_constraint");
					}
				}
				printInstanceArray("exact_entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
				// the result is sorted, does not require sorting 
				moveInstanceArray2Constraint(constraint);
			} else 
			if (attribute instanceof EInverse_attribute_constraint) {
				// implemented with the big picture in mind, inverse_attribute_constraint implemented accordingly

				/*
					 it may be difficult to take directly the popualation of domain in this case, it must contain no subtypes
				*/
			
				EEntity inverted_attribute = null;
				if (((EInverse_attribute_constraint)attribute).testInverted_attribute(null)) {
					inverted_attribute = ((EInverse_attribute_constraint)attribute).getInverted_attribute(null);
				} else {
					printError("mandatory attribute inverted_attribute unset in exact_entity_constraint, where attribute is inverse_attribute_constraint: " + attribute);
				}
			
			
				if (inverted_attribute instanceof EExplicit_attribute) {
			
			
					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];
						if (instance.isInstanceOf(domain)) {

							// resolving inverse_attribute for the resulting instances of domain type
							// if (instance.testAttribute((EAttribute)inverted_attribute, select_path) == 1) {
							// EEntity value = (EEntity)instance.get_object((EAttribute)inverted_attribute);
							// add2InstanceArray(value);										
								add2InstanceArray(instance);										
							// } else {
							// printWarning("inverted_attribute value unset in exact_entity_constraint where attribute is inverse_attribute_constraint: " + instance);
							// }
						}
					} // for	
					// requires sorting!! - not if we do not resolve values here
					// sortInstanceArray();
					printInstanceArray("exact_entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);

				} else 
				if (inverted_attribute instanceof EAggregate_member_constraint) {

					int member = Integer.MIN_VALUE;
					if (((EAggregate_member_constraint)inverted_attribute).testMember(null)) {
						member = ((EAggregate_member_constraint)inverted_attribute).getMember(null);
					}
					EEntity attribute2 = null;
					if (((EAggregate_member_constraint)inverted_attribute).testAttribute(null)) {
						attribute2 = ((EAggregate_member_constraint)inverted_attribute).getAttribute(null);
					} else {
						printError("mandatory attribute attribute unset in aggregate_member_constraint in inverse_attribute_constraint in exact_entity_constraint: " + inverted_attribute);
					}

					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];
						if (instance.isInstanceOf(domain)) {
							add2InstanceArray(instance);										
							/*
							if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
								AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
								if (member != Integer.MIN_VALUE) {
									if (member <= agg_elements.getMemberCount()) {
										EEntity value = (EEntity)agg_elements.getByIndexObject(member);
										add2InstanceArray(value);										
									}
								} else {
									SdaiIterator it = agg_elements.createIterator();
									while (it.next()) {
										EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
										add2InstanceArray(value);										
									}
								}
							
							} else {
								printWarning("in exact_entity_constraint, in attribute = inverse_attribute_constraint, inverse_attribute = aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute2);
							}
							*/											
						}
					} // for	
					// requires sorting !! - not if we do not resolve values here
					// sortInstanceArray();
					printInstanceArray("exact_entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);

				} else {
					printError("NOT YET SUPPORTED OR WRONG inverted_attribute_type in exact_entity_constraint where attribute is inverse_attribute_constraint: " + inverted_attribute);
				}
			} else 
			if (attribute instanceof EAggregate_member_constraint) {
				
			
				int member = Integer.MIN_VALUE;
				if (((EAggregate_member_constraint)attribute).testMember(null)) {
					member = ((EAggregate_member_constraint)attribute).getMember(null);
				}

				EEntity attribute2 = null;
				if (((EAggregate_member_constraint)attribute).testAttribute(null)) {
					attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
				} else {
					printError("mandatory attribute attribute unset in aggregate_member_constraint in exact_entity_constraint: " + attribute);
				}


				if (attribute2 instanceof EExplicit_attribute) {
					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];
						if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
							AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
							if (member != Integer.MIN_VALUE) {
								if (member <= agg_elements.getMemberCount()) {
									EEntity value = (EEntity)agg_elements.getByIndexObject(member);
									if (value.isInstanceOf(domain)) {
										// add2InstanceArray(value);										
										add2InstanceArray(instance);										
									}
								}                        
							} else {
								SdaiIterator it = agg_elements.createIterator();
								while (it.next()) {
									EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
									if (value.isInstanceOf(domain)) {
										// add2InstanceArray(value);										
										add2InstanceArray(instance);										
										//  path goes through any member, one member is enough
										break;
									}
								}
        			}
        		} else {
							printWarning("in exact_entity_constraint, in attribute = aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute2);
        		}	
					}
					// requires sorting !!
					sortInstanceArray();
					printInstanceArray("exact_entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);
				} else {
					printError("NOT YET SUPPORTED OR WRONG attribute = aggregate_member_constraint attribute type in exact_entity_constraint: " + attribute2);
				}
				
			} else {
				printError("NOT YET SUPPORTED OR WRONG exact_entity_constraint attribute type: " + attribute);
			}
		} else { // not exact_entity_constraint, subtypes are allowed
			printDebug("entity_constraint - domain: " + domain + ", attribute: " + attribute, 4);

			if (attribute instanceof EExplicit_attribute) {
				// check this implementation with the big picture in mind
				for (int i = 0; i < instances.length; i++) {
					EEntity instance = instances[i];
					if (instance.testAttribute((EAttribute)attribute, select_path) == 1) {
						EEntity value = (EEntity)instance.get_object((EAttribute)attribute);
						if (value.isKindOf(domain)) {
							// do we add here instance (instance of the attribute parent entity) or value (instance to which the attribute points)?
							// RuleGenerator currently returns value, although instance version is also present but commented out
							// check with some real mapping examples
							add2InstanceArray(instance);										
						}
					} else {
						printWarning("attribute value unset in entity_constraint");
					}
				}
				printInstanceArray("entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
				// the result is sorted, does not require sorting 
				moveInstanceArray2Constraint(constraint);
			} else 
			if (attribute instanceof EInverse_attribute_constraint) {
				// implemented with the big picture in mind, inverse_attribute_constraint implemented accordingly

				/*
					perhaps for inverse_attribute_constraint, at least where inverted_attribute = explicit_attribute,
					it is possible to take the population of the domain directly instead of comparing the type of each instance
					of the result of the inverse_attribute_constraint to domain. Would be faster.
				*/
				EEntity inverted_attribute = null;
				if (((EInverse_attribute_constraint)attribute).testInverted_attribute(null)) {
					inverted_attribute = ((EInverse_attribute_constraint)attribute).getInverted_attribute(null);
				} else {
					printError("mandatory attribute inverted_attribute unset in entity_constraint, where attribute is inverse_attribute_constraint: " + attribute);
				}
			
				if (inverted_attribute instanceof EExplicit_attribute) {
				
					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];
						if (instance.isKindOf(domain)) {


							// resolving inverse_attribute for the resulting instances of domain type
							// if (instance.testAttribute((EAttribute)inverted_attribute, select_path) == 1) {
							// EEntity value = (EEntity)instance.get_object((EAttribute)inverted_attribute);
							// add2InstanceArray(value);										
								add2InstanceArray(instance);										
							// } else {
							// printWarning("inverted_attribute value unset in entity_constraint where attribute is inverse_attribute_constraint: " + instance);
							// }
						}
					} // for	
					// requires sorting !! - does not require sorting, if we do not resolve values here
					// sortInstanceArray();
					printInstanceArray("entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);
				} else 
				if (inverted_attribute instanceof EAggregate_member_constraint) {

					int member = Integer.MIN_VALUE;
					if (((EAggregate_member_constraint)inverted_attribute).testMember(null)) {
						member = ((EAggregate_member_constraint)inverted_attribute).getMember(null);
					}
					EEntity attribute2 = null;
					if (((EAggregate_member_constraint)inverted_attribute).testAttribute(null)) {
						attribute2 = ((EAggregate_member_constraint)inverted_attribute).getAttribute(null);
					} else {
						printError("mandatory attribute attribute unset in aggregate_member_constraint in inverse_attribute_constraint in entity_constraint: " + inverted_attribute);
					}

					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];
						if (instance.isKindOf(domain)) {
							add2InstanceArray(instance);										

							/*
							if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
								AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
								if (member != Integer.MIN_VALUE) {
									if (member <= agg_elements.getMemberCount()) {
										EEntity value = (EEntity)agg_elements.getByIndexObject(member);
										add2InstanceArray(value);										
									}
								} else {
									SdaiIterator it = agg_elements.createIterator();
									while (it.next()) {
										EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
										add2InstanceArray(value);										
									}
								}
							
							} else {
								printWarning("in entity_constraint, in attribute = inverse_attribute_constraint, inverse_attribute = aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute2);
							}
							*/												
						}	
					} // for	
					// requires sorting !! - does not require sorting, if we don not resolve values here
					// sortInstanceArray();
					printInstanceArray("entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);
					
				} else {
					printError("NOT YET SUPPORTED OR WRONG inverted_attribute_type in entity_constraint where attribute is inverse_attribute_constraint: " + inverted_attribute);
				}
			} else 
			if (attribute instanceof EAggregate_member_constraint) {
    		
    		/*
    			take as an input the instance array from the aggregate_member_constraint (parent entity population of its attribute)
		    	for each element of the aggregate, read the attribute value 
		    	(get the aggregate of instances - the value of the aggregate attribute)
    			check if it is an instance of domain type of entity_constraint, 
    			if so, put into the result aggregate.
    			Duplicates allowed?
				*/
				int member = Integer.MIN_VALUE;
				if (((EAggregate_member_constraint)attribute).testMember(null)) {
					member = ((EAggregate_member_constraint)attribute).getMember(null);
				}

				EEntity attribute2 = null;
				if (((EAggregate_member_constraint)attribute).testAttribute(null)) {
					attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
				} else {
					printError("mandatory attribute attribute unset in aggregate_member_constraint in entity_constraint: " + attribute);
				}


				if (attribute2 instanceof EExplicit_attribute) {
					for (int i = 0; i < instances.length; i++) {
						EEntity instance = instances[i];
						if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
							AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
							if (member != Integer.MIN_VALUE) {
								if (member <= agg_elements.getMemberCount()) {
									EEntity value = (EEntity)agg_elements.getByIndexObject(member);
									if (value.isKindOf(domain)) {
										// add2InstanceArray(value);										
										add2InstanceArray(instance);										
									}
								}                        
							} else {
								SdaiIterator it = agg_elements.createIterator();
								while (it.next()) {
									EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
									if (value.isKindOf(domain)) {
										// add2InstanceArray(value);										
										add2InstanceArray(instance);										
										// if at least one member of the aggregate has correct type, add instance, because path goes through any member
										break;
									}
								}
        			}
        		} else {
							printWarning("in entity_constraint, in attribute = aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute2);
        		}	
					}
					// requires sorting !!
					sortInstanceArray();
					printInstanceArray("entity_constraint, attribute: " + attribute + ", domain: " + domain, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);
				} else {
					printError("NOT YET SUPPORTED OR WRONG attribute = aggregate_member_constraint attribute type in entity_constraint: " + attribute2);
				}


			} else {
				printError("NOT YET SUPPORTED OR WRONG entity_constraint attribute type: " + attribute);
			}

		}
	} // processEntity_constraint()

	void processSelect_constraint(ESelect_constraint constraint) throws SdaiException {
		printDebug("select_constraint: " + constraint);
		// A select_constraint specifies a constraint on select type. Attribute must point to attribute that is of select type.
		// Complex instances may exist with logical_, boolean_, string_, enumeration_, integer_ and real_constraint.
		// ---------------------------------------------------------------------------------------------------------------------------
	
		// A data_type defines a path to go throw select.
		// data_type : LIST [1:?] OF defined_type;
		ADefined_type data_type = null;
		if (constraint.testData_type(null)) {
			data_type = constraint.getData_type(null);
		} else {
			printError("mandatory attribute data_type unset in select_constraint: " + constraint);
		}
		// An attribute is definition of attribute or other element that is of select type and is constraint to some specific selection.
		// attribute : select_constraint_select;
		EEntity attribute = null;
		if (constraint.testAttribute(null)) {
			attribute = constraint.getAttribute(null);
		} else {
			printError("mandatory attribute attribute unset in select_constraint: " + constraint);
		} 
		
		printError("NOT YET SUPPORTED select_constraint type: " + constraint);
	
	} // processSelect_constraint()					

	void processConstraint_relationship(EConstraint_relationship constraint) throws SdaiException {
		// A constraint_relationship defines pair of constraints. It does not specify the meaning of relationship nor a direction to go.
		// -----------------------------------------------------------------------------------------------------------------------------
	
		// An element2 is a second constraint that is related by this constraint relationship.
		// element2 : constraint_select;
		EEntity element2 = null;
		if (constraint.testElement2(null)) {
				element2 = constraint.getElement2(null);
		} else {
			printError("mandatory attribute element2 unset in constraint_relationship: " + constraint);
		}

		if (constraint instanceof EInstance_constraint) { // ABSTRACT
			processInstance_constraint((EInstance_constraint)constraint, element2);
		} else
		if (constraint instanceof EPath_constraint) {			
			processPath_constraint((EPath_constraint)constraint, element2);
		}	else {
			// internal error - unknown constraint_relationship
			printError("unknown constraint_relationship: " + constraint);
		}	
	} // processConstraint_relationship()

	void processInstance_constraint(EInstance_constraint constraint, EEntity element2) throws SdaiException {
		// printDebug("instance_constraint: " + constraint);
		// An instances_constraint is a consraint_relationship that defined constraints on instances of one type.
		// ---------------------------------------------------------------------------------------------------------------------------
	
		// An element1 is first constraint that is related by this constraint relationship.
		// element1 : constraint_select;
		EEntity element1 = null;
		if (constraint.testElement1(null)) {
			element1 = constraint.getElement1(null);
		} else {
			printError("mandatory attribute element1 unset in instance_constraint: " + constraint);
		}
	
		if (constraint instanceof EAnd_constraint_relationship) {
			printDebug("and_constraint_relationship: " + constraint);
			// An and_constraint_relationship is relationship of two constraints and both these constraints 
			// must be met to fulfil and_constraint_relationship.
			EEntity [] elements1 = getEntityInstancesExt(element1);
			EEntity [] elements2 = getEntityInstancesExt(element2);
			int hash1; int hash2;
			int i1 = 0;
			int i2 = 0;
			
			// possible optimization - eliminate hasCode() in the loop for the index if it does not change - see performUnion new version
			while ((i1 < elements1.length) && (i2 < elements2.length)) {
				hash1 = elements1[i1].hashCode();								
				hash2 = elements2[i2].hashCode();								
				if (hash1 == hash2) {
					add2InstanceArray(elements1[i1]);										
					i1++;
					i2++;
				} else 
				if (hash1 < hash2) {
					i1++;
				} else {
					i2++;
				}
			}
			printInstanceArray("and_constraint_relationship, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
			// the result is sorted, does not require sorting 
			moveInstanceArray2Constraint(constraint);
			
 			// -------------------------------------------------------------------------------------------------------------------------
		} else
		if (constraint instanceof EInstance_equal) {
			printDebug("instance_equal: " + constraint);
			// An instance_equal is instance_constraint that requires two constraints to end with the same instance.
			// -------------------------------------------------------------------------------------------------------------------------
		
			// does not occur at least in ap210 up to wd36
			printError("NOT YET SUPPORTED constraint type - instance_equal: " + constraint);
		} else
		if (constraint instanceof EOr_constraint_relationship) {
			printDebug("or_constraint_relationship: " + constraint);
			// An or_constraint_relationship is an instance_constraint where it is enough, 
			// that requirements of at least one constraint are meet.
			// -------------------------------------------------------------------------------------------------------------------------
			EEntity [] elements1 = getEntityInstancesExt(element1);
			EEntity [] elements2 = getEntityInstancesExt(element2);
			
			if (elements1.length == 0) {
				// if both == 0, also ok, need to make the result 0 too.
				printInstanceArray("or_constraint_relationship - (at least) elements1 is empty, element1: " + element1 + ", element2: " + element2, elements2, constraint, 3);
				constraint.setTemp(elements2);
			} else
			if (elements2.length == 0) {
				printInstanceArray("or_constraint_relationship - element2 is empty, element1: " + element1 + ", element2: " + element2, elements1, constraint, 3);
				constraint.setTemp(elements1);
			} else {
			
				// possible optimization - eliminate hasCode() in the loop for the index if it does not change - see performUnion new version
				int hash1; int hash2;
				int i1 = 0;
				int i2 = 0;
				while ((i1 < elements1.length) && (i2 < elements2.length)) {
					hash1 = elements1[i1].hashCode();								
					hash2 = elements2[i2].hashCode();								
					if (hash1 == hash2) {
						add2InstanceArray(elements1[i1]);										
						i1++;
						i2++;
					} else 
					if (hash1 < hash2) {
						add2InstanceArray(elements1[i1]);										
						i1++;
					} else {
						add2InstanceArray(elements2[i2]);										
						i2++;
					}
				}

				// at this point, one operand array ended, but the other one - probably did not.
				// so copy the remainder of the other array
				
				// the copying could be done at once instead of element-by-element, but for that the capacity must be ensured first -
				// possible further optimization
				if (i1 < elements1.length) {
					while (i1 < elements1.length) {
						add2InstanceArray(elements1[i1]);										
						i1++;
					}
				} else {
					// must be i2 < element2.length
					while (i2 < elements2.length) {
						add2InstanceArray(elements2[i2]);										
						i2++;
					}
				}

				printInstanceArray("or_constraint_relationship, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
				// the result is sorted, does not require sorting 
				moveInstanceArray2Constraint(constraint);
			}
		
		}	else {
			// internal error - unknown instance_constraint
			printError("NOT YET SUPPORTED OR WRONG instance_constraint type: " + constraint);
		}				
	} // processInstance_constraint()		

	void processPath_constraint(EPath_constraint constraint, EEntity element2) throws SdaiException {
		printDebug("path_constraint: " + constraint);
		// A path_constraint is a constraint_relationship that does following: 
		// requires make step using element1 and meet requirements of element2. 
		// The element1 shall be a constraint on an attribute of an entity-type. 
		// The element2 defines a constraint on the entity reached by following element1.
		// ---------------------------------------------------------------------------------------------------------------------------
		/*
				element1 - explicit attribute
				element2 - string constraint
				take the array of the parent entity of the explicit attribute of element1,
				for each element instance of that population array, read the value of the explicit attribute 
				(it should be an antity instance) search (binary search) for that value instance in the array of element2 
				if present, take that element from the population of (parent entity of explicit attribute of) element1 
				and put it into the path_constraint result array
				The resulting array will not need sorting.
				(the parent entity type of element2 string_constraint attribute should be the same or in sub/super relationship
				with the type of entity of value of explicit attribute of element1)
		*/		  
		// -------------------------------------------------------------------------------------------------------------------------
		// An element1 is attribute or other element that allows to make step from one entity instance to instance of other entity.
		// element1 : path_constraint_select;
		EEntity element1 = null;
		if (constraint.testElement1(null)) {
			element1 = constraint.getElement1(null);
		} else {
			printError("mandatory attribute element1 unset in path_constraint: " + constraint);
		}

		// debugging print to see what element1 types are present
		// printElement1(element1);


		EEntity [] elements1 = getEntityInstancesExtNR(element1);
		EEntity [] elements2 = getEntityInstancesExt(element2);
		
	  if (element1 instanceof EAggregate_member_constraint) { 
			// check with the big picture in mind
			int member = Integer.MIN_VALUE;
			if (((EAggregate_member_constraint)element1).testMember(null)) {
				member = ((EAggregate_member_constraint)element1).getMember(null);
			}
			EEntity attribute = null;
			if (((EAggregate_member_constraint)element1).testAttribute(null)) {
				attribute = ((EAggregate_member_constraint)element1).getAttribute(null);
			} else {
				printError("mandatory attribute attribute unset in aggregate_member_constraint in element1 of path_constraint: " + element1);
			}

			for (int i = 0; i < elements1.length; i++) {
				EEntity instance = elements1[i];
				if (attribute instanceof EExplicit_attribute) {
					if (instance.testAttribute((EAttribute)attribute, select_path) == 1) {
						AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute);
						if (member != Integer.MIN_VALUE) {
							if (member <= agg_elements.getMemberCount()) {
								EEntity value = (EEntity)agg_elements.getByIndexObject(member);
								if (searchInstanceArray(elements2, value) >= 0) {
									add2InstanceArray(instance);										
								}
							}                        
						} else {
							SdaiIterator it = agg_elements.createIterator();
							while (it.next()) {
								EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
								if (searchInstanceArray(elements2, value) >= 0) {
									add2InstanceArray(instance);										
									// continue searching for next aggregate elements and add the same instance multiple times?
								}
							}
        		}
        	} else {
						printWarning("in path_constraint, in element1 aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
        	}	
				} else {
					printError("NOT YET SUPPORTED OR WRONG element1 = aggregate_member_constraint attribute type in path_constraint: " + attribute);
				}
			}
			printInstanceArray("path_constraint, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
			// sorting not needed, the result is already sorted
			moveInstanceArray2Constraint(constraint);

		} else
	  if (element1 instanceof EInverse_attribute_constraint) { 
			// implemented with the big picture in mind, inverse_attribute_constraint implemented accordingly

			/*
				perhaps for inverse_attribute_constraint, at least where inverted_attribute = explicit_attribute,
				it is possible to take the population of the element2 directly instead of 
				filtering the population of element1 by element2. Faster

				*/
			EEntity inverted_attribute = null;
			if (((EInverse_attribute_constraint)element1).testInverted_attribute(null)) {
				inverted_attribute = ((EInverse_attribute_constraint)element1).getInverted_attribute(null);
			} else {
				printError("mandatory attribute inverted_attribute unset in path_constraint where element1 is inverse_attribute_constraint:: " + element1);
			}

			// let's use the same AND for both (so far) cases of inverted_attribute
			
			EEntity and_array [] = performAnd(elements1, elements2);

			if (inverted_attribute instanceof EExplicit_attribute) {

				for (int i = 0; i < and_array.length; i++) {
					EEntity instance = and_array[i];
					
					// resolving inverse_attribute for the resulting instances of domain type
					if (instance.testAttribute((EAttribute)inverted_attribute, select_path) == 1) {
						EEntity value = (EEntity)instance.get_object((EAttribute)inverted_attribute);
						add2InstanceArray(value);										
					} else {
						printWarning("instance value unset in path_constraint, element1 - inverse_attribute_constraint: " + instance);
					}
				} // for	
				// requires sorting!!
				sortInstanceArray();
				printInstanceArray("path_constraint, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
				moveInstanceArray2Constraint(constraint);
			} else 
			if (inverted_attribute instanceof EAggregate_member_constraint) {

		 		// with big picture in mind
		 		/*
		 	
		 			ACTION: compare element1 instances to element2 instances, (AND perhaps) if present in element2 - 
		 			resolve the attribute,  read the attribute value and store it in result array.
				*/
				
				int member = Integer.MIN_VALUE;
				if (((EAggregate_member_constraint)inverted_attribute).testMember(null)) {
					member = ((EAggregate_member_constraint)inverted_attribute).getMember(null);
				}
				EEntity attribute = null;
				if (((EAggregate_member_constraint)inverted_attribute).testAttribute(null)) {
					attribute = ((EAggregate_member_constraint)inverted_attribute).getAttribute(null);
				} else {
					printError("mandatory attribute attribute unset in aggregate_member_constraint in inverse_attribute_constraint in element1 of path_constraint: " + inverted_attribute);
				}

				if (attribute instanceof EExplicit_attribute) {
					for (int i = 0; i < and_array.length; i++) {
						EEntity instance = and_array[i];
						if (instance.testAttribute((EAttribute)attribute, select_path) == 1) {
							AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute);
							if (member != Integer.MIN_VALUE) {
								if (member <= agg_elements.getMemberCount()) {
									EEntity value = (EEntity)agg_elements.getByIndexObject(member);
									add2InstanceArray(value);										
								}
							} else {
								SdaiIterator it = agg_elements.createIterator();
								while (it.next()) {
									EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
									add2InstanceArray(value);										
								}
							}
						
  	      	} else {
							printWarning("in path_constraint, in element1 = inverse_attribute_constraint, inverse_attribute = aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute);
          	}	
					} // for	
					// requires sorting!!
					sortInstanceArray();
					printInstanceArray("path_constraint, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);
				} else {
					printError("NOT YET SUPPORTED OR WRONG inverted_attribute = aggregate_member_constraint attribute type in path_constraint where element1 is inverse_attribute_constraint: " + attribute);
				}
				
			} else {
				printError("NOT YET SUPPORTED OR WRONG inverted_attribute type in path_constraint where element1 is inverse_attribute_constraint: " + inverted_attribute);
			}
		
		} else
		if (element1 instanceof EEntity_constraint) { 

			// An entity_constraint specifies a constraint on entity type. 
			// The attribute must point to an attribute or constraint definition that is of an entity type. 
			// This constraint restricts to subtypes or select types (maybe to complex types).
			// ---------------------------------------------------------------------------------------------------------------------------
			
		
			// An attribute is a definition of attribute that is constraint to some entity type.
			// attribute : attribute_select;
			
			// check with the big picture in mind - earlier implementation here!!!
			EEntity attribute = null;
			if (((EEntity_constraint)element1).testAttribute(null)) {
				attribute = ((EEntity_constraint)element1).getAttribute(null);
			} else {
				// should be already reported while processing the entity_constraint
				printError("mandatory attribute attribute unset in entity_constraint: " + constraint);
			}			 				

			if (attribute instanceof EExplicit_attribute) {
				for (int i = 0; i < elements1.length; i++) {
					EEntity instance = elements1[i];
					if (instance.testAttribute((EAttribute)attribute, select_path) == 1) {
						EEntity value = (EEntity)instance.get_object((EAttribute)attribute);
						if (searchInstanceArray(elements2, value) >= 0) {
							add2InstanceArray(instance);										
						}
					} else {
						printWarning("attribute value unset in exact_entity_constraint");
					}	
				}
				printInstanceArray("path_constraint, element1 - entity_constraint, attribute: " + attribute, null, constraint, 3);
				// the result is sorted, does not require sorting 
				moveInstanceArray2Constraint(constraint);
			} else
			if (attribute instanceof EAggregate_member_constraint) {
				// implementation according to the big picture
				/*
						entity_constraint also implemented according to the big picture, so no need to do anything here,
						just take the instances that are both in element1 and in element2
						better not to use search, just use the algorithm as in and_constraint_relationship, then the result
						will not need sorting either.
					
				*/

			// new specification:
			// aggregate                      search all values, add instance if at least one found (or [member])


				int member = Integer.MIN_VALUE;
				if (((EAggregate_member_constraint)attribute).testMember(null)) {
					member = ((EAggregate_member_constraint)attribute).getMember(null);
				}

				EEntity attribute2 = null;
				if (((EAggregate_member_constraint)attribute).testAttribute(null)) {
					attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
				} else {
					printError("mandatory attribute attribute unset in aggregate_member_constraint in entity_constraint - element1 of path_constraint: " + attribute);
				}


				if (attribute2 instanceof EExplicit_attribute) {
					for (int i = 0; i < elements1.length; i++) {
						EEntity instance = elements1[i];
						if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
							AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
							if (member != Integer.MIN_VALUE) {
								if (member <= agg_elements.getMemberCount()) {
									EEntity value = (EEntity)agg_elements.getByIndexObject(member);
									if (searchInstanceArray(elements2, value) >= 0) {
										add2InstanceArray(instance);										
									}
								}
							} else {
								SdaiIterator it = agg_elements.createIterator();
								while (it.next()) {
									EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
									if (searchInstanceArray(elements2, value) >= 0) {
										add2InstanceArray(instance);										
										// if at least one member of the aggregate has been found, add instance, because path goes through any member
										break;
									}
								}
        			}
        		} else {
							printWarning("in path_constraint in element1 = entity_constraint, in attribute = aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute2);
        		}	
					}
					printInstanceArray("path_constraint, element1 = entity_constraint, attribute = aggregate_member_constraint, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
					// the result is sorted, does not require sorting 
					moveInstanceArray2Constraint(constraint);
				} else {
					printError("NOT YET SUPPORTED OR WRONG attribute type in aggregate_member_constraint in entity_constraint in path_constraint element1: " + attribute2);
				}

			} else
			if (attribute instanceof EInverse_attribute_constraint) {

				EEntity inverted_attribute = null;
				if (((EInverse_attribute_constraint)attribute).testInverted_attribute(null)) {
					inverted_attribute = ((EInverse_attribute_constraint)attribute).getInverted_attribute(null);
				} else {
					printError("mandatory attribute inverted_attribute unset in path_constraint where element1 is entity_constraint and its attribute is inverse_attribute_constraint:: " + attribute);
				}

				EEntity and_array [] = performAnd(elements1, elements2);

				if (inverted_attribute instanceof EExplicit_attribute) {

					for (int i = 0; i < and_array.length; i++) {
						EEntity instance = and_array[i];
					
						// resolving inverse_attribute for the resulting instances of domain type
						if (instance.testAttribute((EAttribute)inverted_attribute, select_path) == 1) {
							EEntity value = (EEntity)instance.get_object((EAttribute)inverted_attribute);
							add2InstanceArray(value);										
						} else {
							printWarning("instance value unset in path_constraint, element1 = entity_constraint, attribute = inverse_attribute_constraint: " + instance);
						}
					} // for	
					// requires sorting!!
					sortInstanceArray();
					printInstanceArray("path_constraint, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
					moveInstanceArray2Constraint(constraint);
				} else 
				if (inverted_attribute instanceof EAggregate_member_constraint) {
				
					int member = Integer.MIN_VALUE;
					if (((EAggregate_member_constraint)inverted_attribute).testMember(null)) {
						member = ((EAggregate_member_constraint)inverted_attribute).getMember(null);
					}
					EEntity attribute2 = null;
					if (((EAggregate_member_constraint)inverted_attribute).testAttribute(null)) {
						attribute2 = ((EAggregate_member_constraint)inverted_attribute).getAttribute(null);
					} else {
						printError("mandatory attribute attribute unset in aggregate_member_constraint in inverse_attribute_constraint in entity_constraint in element1 of path_constraint: " + inverted_attribute);
					}

					if (attribute2 instanceof EExplicit_attribute) {
						for (int i = 0; i < and_array.length; i++) {
							EEntity instance = and_array[i];
							if (instance.testAttribute((EAttribute)attribute2, select_path) == 1) {
								AEntity agg_elements = (AEntity)instance.get_object((EAttribute)attribute2);
								if (member != Integer.MIN_VALUE) {
									if (member <= agg_elements.getMemberCount()) {
										EEntity value = (EEntity)agg_elements.getByIndexObject(member);
										add2InstanceArray(value);										
									}
								} else {
									SdaiIterator it = agg_elements.createIterator();
									while (it.next()) {
										EEntity value = (EEntity)agg_elements.getCurrentMemberObject(it);
										add2InstanceArray(value);										
									}
								}
							
  	        	} else {
								printWarning("in path_constraint, in element1 = entity_constraint, attribute = inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint, instance attribute unset or of wrong type, instance: " + instance + ", attribute: " + attribute2);
            	}	
						} // for	
						// requires sorting!!
						sortInstanceArray();
						printInstanceArray("path_constraint, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
						moveInstanceArray2Constraint(constraint);
					} else {
						printError("NOT YET SUPPORTED OR WRONG attribute type in aggregate_member_constraint in inverse_attribute_constraint in entity_constraint in path_constraint element1: " + attribute2);
					}
				} else {
					printError("NOT YET SUPPORTED OR WRONG inverted attribute type in inverse_attribute_constraint in entity_constraint in path_constraint element1: " + inverted_attribute);
				}

			} else {
				printError("NOT YET SUPPORTED OR WRONG entity_constraint attribute type in path_constraint element1: " + attribute);
			}

		} else
	  if (element1 instanceof EIntersection_constraint) { 
			printError("NOT YET SUPPORTED path_constraint element1 type intersection_constraint: " + element1);
		} else
		if (element1 instanceof EAttribute) { 
			for (int i = 0; i < elements1.length; i++) {
				EEntity instance = elements1[i];
				if (instance.testAttribute((EAttribute)element1, select_path) == 1) {
					EEntity value = (EEntity)instance.get_object((EAttribute)element1);
					if (searchInstanceArray(elements2, value) >= 0) {
						add2InstanceArray(instance);										
					}
				} else {
					printWarning("attribute value unset in path_constraint element1");
				}
			}
			printInstanceArray("path_constraint, element1: " + element1 + ", element2: " + element2, null, constraint, 3);
			// sorting not needed, the result is already sorted
			moveInstanceArray2Constraint(constraint);
		} else {
			// does not occur in the current ap210
			printError("NOT YET SUPPORTED OR WRONG element1 type in path_constraint: " + element1);
		}
	} // processPath_constraint()

	void processEnd_of_path_constraint(EEnd_of_path_constraint constraint) throws SdaiException {
		printDebug("end_of_path_constraint: " + constraint);
		/*
			End_of_path_constraint is intended as a replacement for
			attribute_constraint.path list. It allows to identify end of the main path
			condition. See comments at attribute_constraint.path<br>
			End_of_path_constraint is used as value of path_constraint.element2 or at the top of
			constraint tree.<br>
			Introduced in version 31.1.
		*/ // --------------------------------------------------------------------------------------------------------------------------
	
		// The remaining part of constraint tree which doesn't belong to the main path.
		// constraints : constraint_select;
		EEntity constraints = null;
		if (constraint.testConstraints(null)) {
			constraints = constraint.getConstraints(null);
		} else {
			printError("mandatory attribute constraints unset in end_of_path_constraint: " + constraint);
		}
		
		EEntity instances [] = getEntityInstancesExt(constraints);
		printInstanceArray("end_of_path_constraint, constraints: " + constraints, instances, constraint, 3);
		constraint.setTemp(instances);
	} // processEnd_of_path_constraint()

	void processIntersection_constraint(EIntersection_constraint constraint) throws SdaiException {
		printDebug("intersection_constraint: " + constraint);
		/*
			INTERSECTION_CONSTRAINT allows to get only common (intersecting) instances 
			from subpaths specified by this instance. This can be used in particularly when
			several AND paths point to same instance. It effectively outdates 
			instance_equal entity.
			Introduced in version 31.0.
		*/ // --------------------------------------------------------------------------------------------------------------------------

		// Subpaths to be intersected.
		AConstraint_select subpaths = null;
		if (constraint.testSubpaths(null)) {
			subpaths = constraint.getSubpaths(null);
		} else {
			printError("mandatory attribute subpaths unset in intersection_constraint: " + constraint);
		}
		// subpaths : SET [2:?] OF constraint_select;
		printError("NOT YET SUPPORTED intersection_constraint: " + constraint);
	} // processIntersection_constraint()

	void processInverse_attribute_constraint(EInverse_attribute_constraint constraint) throws SdaiException {
		printDebug("inverse_attribute_constraint: " + constraint);
		//     if (constraint.getPersistentLabel().equals("#3940649673955927")) {
		//    	System.out.println("HEYHEYFOUND");
		//     }	
		/*
			An inverse_attribute_constraint defines a needed inverse attribute that is missing in the target schema.
			This is needed to travel reverse to attribute definition.
	
			NOTE 1 - In the case that the AIM express already contains the proper inverse attribute this should be used.
		*/ // --------------------------------------------------------------------------------------------------------------------------
		
		// An inverted_attribute is a definition of attribute that is inverted.
		// inverted_attribute : inverse_attribute_constraint_select;
		EEntity inverted_attribute = null;
		if (constraint.testInverted_attribute(null)) {
			inverted_attribute = constraint.getInverted_attribute(null);
		} else {
			printError("mandatory attribute inverted_attribute unset in inverse_attribute_constraint: " + constraint);
		}

		// occuring cases: inverted_attribute = attribute or aggregate_member_constraint
							

		/*
				 some old ideas. ignore, USEDIN is not needed
				 
				 example when  inverted_attribute = explicit_attribute
				 take the population of the entity of the domain type of the attribute (not parent_entity, but domain)
				 then for every instance of the population, with USEDIN (findEntityInstanceUsedin(attribute, where_to_search, result_aggregate)
				 find the instances of the parent_entity of the inverted_attribute and put them into array

				 if  inverted_attribute = aggregate_member_constraint, where attribute = explicit_attribute,
				 
				 
				 perhaps no difference? Just take the population from the temp of aggregate_member_constraint,
				 and apply usedin for each instance, using attribute of aggregate member constraint as inverted_attribute,
				 and the usedin method itself will handle  aggregate attribute.
				 But that is problematic, what about taking not parent but domain? If we attempt to do that, how we use the
				 result of the already processed aggregate_member_constraint

		*/

		if (inverted_attribute instanceof EExplicit_attribute) {
		
			//  now new interpretation, that requires special processing in upper constraints
			// EEntity domain = ((EExplicit_attribute)inverted_attribute).getDomain(null);
			// EEntity instances [] = getEntityInstancesExt(domain);
		
			EEntity instances [] = getEntityInstancesExt(inverted_attribute);
			printInstanceArray("inverse_attribute_constraint, inverted_attribute: " + inverted_attribute, instances, constraint, 3);
			constraint.setTemp(instances);

			// this straightforward implementation is no longer used
			// AEntity result = new AEntity();
			// for (int i = 0; i < instances.length; i++) {
				// EEntity instance = instances[i];
				// instance.findEntityInstanceUsedin((EAttribute)inverted_attribute, application_domain, result);
			
			// }
			
			// add everything from result and sort
			// copyInstancesFromAEntity(result);
			// sortInstanceArray();
			// printInstanceArray("inverse_attribute_constraint, inverted_attribute: " + inverted_attribute, null, constraint, 3);
			// moveInstanceArray2Constraint(constraint);
		} else
		if (inverted_attribute instanceof EAggregate_member_constraint) {
				
				// according to the big picture, nothing else is needed
	 			// ACTION: just copy the input array to result
				EEntity instances [] = getEntityInstancesExt(inverted_attribute);
				printInstanceArray("inverse_attribute_constraint, inverted_attribute: " + inverted_attribute, instances, constraint, 3);
				constraint.setTemp(instances);

		} else {
			printError("NOT YET SUPPORTED OR WRONG inverted_attribute type in inverse_attribute_constraint: " + inverted_attribute);
		}
	} // processInverse_attribute_constraint()					

	void processNegation_constraint(ENegation_constraint constraint) throws SdaiException {
		printDebug("negation_constraint: " + constraint);
		// NEGATION_CONSTRAINT is used to specify that result of constraint has to be taken negated.
		// It can be used only in the addition constraint but never in the main path.
		// -----------------------------------------------------------------------------------------------------------------------------
	
		// constraints result of whose has to negated.
		// constraints : constraint_select;
		EEntity constraints = null;
		if (constraint.testConstraints(null)) {
			constraints = constraint.getConstraints(null);
		} else {
			printError("mandatory attribute constraints unset in negation_constraint: " + constraint);
		}
		
		
		if (constraints instanceof EExplicit_attribute) {
			// get the whole population and take only those instances that have this attribute unset
			EEntity_definition ed = ((EAttribute)constraints).getParent_entity(null);
			AEntity all_instances = aim_application_model.getInstances(ed);
			for (int i = 0; i < all_instances.getMemberCount(); i++) {
				EEntity current = (EEntity)all_instances.getByIndexObject(i+1);
				if (current.testAttribute((EExplicit_attribute)constraints, select_path) <= 0) {
					add2InstanceArray(current);										
				}
			}
			// requires sorting!!
			sortInstanceArray();
			printInstanceArray("negation_constraint, constraints: " + constraints, null, constraint, 3);
			moveInstanceArray2Constraint(constraint);
		
		} else 
		if (constraints instanceof EEntity_constraint) {
			// may have delayed resolving if + inverse_attribute_constraint
			moveInstanceArray2Constraint(constraint);
			printError("NOT YET SUPPORTED negation_constraint constraints type: " + constraints);
		} else 
		if (constraints instanceof EInverse_attribute_constraint) {
			// may need resolving first
			moveInstanceArray2Constraint(constraint);
			printError("NOT YET SUPPORTED negation_constraint constraints type: " + constraints);
		} else 
		if (constraints instanceof EAggregate_member_constraint) {
			// may need handling similar to explicit_attribute_constraint
			moveInstanceArray2Constraint(constraint);
			printError("NOT YET SUPPORTED negation_constraint constraints type: " + constraints);
		} else 
		if (constraints instanceof EConstraint) {
			// assume that this is a resolved constraint, just make negation = population - array for each instance type in the array
			// go through all the instances in the array, take their entity types, put them into a HashSet, so there is no repetition,
			// then take one at a time, get the population for it, and remove all the instaces in the population that are present in the 
			// array for that constraint.
			// Add the resulting array to the new result array
			
		  HashSet entity_types = new HashSet();
			EEntity [] negation_result = new EEntity [0];
			EEntity [] instances = getEntityInstancesExt(constraints);
			for (int i = 0; i < instances.length; i++) {
				EEntity instance = instances[i];
				EEntity_definition ed = instance.getInstanceType();
				entity_types.add(ed);
			}
			Iterator iter = entity_types.iterator();
			while (iter.hasNext()) {
				EEntity_definition ed = (EEntity_definition)iter.next();
				EEntity [] current_instances =  getEntityInstancesExt(ed);
				current_instances = performDifference(current_instances, instances);
				negation_result = performUnion(negation_result, current_instances);
			}
			
			// does not require sorting
			printInstanceArray("negation_constraint, constraints: " + constraints, negation_result, constraint, 3);
			constraint.setTemp(negation_result);
			
		} else {
			// do nothing for now
			moveInstanceArray2Constraint(constraint);
			printError("NOT YET SUPPORTED negation_constraint constraints type: " + constraints);
		}


	} // processNegation_constraint()


	void processType_constraint(EType_constraint constraint) throws SdaiException {
		printDebug("type_constraint: " + constraint);
		// TYPE_CONSTRAINT requires that starting AIM instance is of type defined in domain attribute.
		// -----------------------------------------------------------------------------------------------------------------------------
	
		// Required type of AIM instance.
		// domain : entity_definition;
		EEntity_definition domain = null;
		if (constraint.testDomain(null)) {
			domain = constraint.getDomain(null);
		} else {
			printError("mandatory attribute domain unset in type_constraint: " + constraint);
		}
		
	  printDebug("type_constraint, domain: " + domain, 4);				
 		// The remaining constraints in a similar way as path.element2 attribute.
		// constraints : OPTIONAL constraint_select;
		EEntity constraints = null;
		if (constraint.testConstraints(null)) {
			constraints = constraint.getConstraints(null);
		} 
		
		if (constraint instanceof EExact_type_constraint) {
			printDebug("\texact_type_constraint: " + constraint, 4);
			// EXACT_TYPE_CONSTRAINT requires that starting AIM instance is of specific type but not it's subtype.
			// ---------------------------------------------------------------------------------------------------------------------------
			if (constraints != null) {
				EEntity constraints_array [] = getEntityInstancesExt(constraints);
				for (int i = 0; i < constraints_array.length; i++) {
					EEntity instance = constraints_array[i];
					if (instance.isInstanceOf(domain)) {
						add2InstanceArray(instance);										
					}
				}
				printInstanceArray("exact_type_constraint, domain: " + domain + ", constraints: " + constraints, null, constraint, 3);
			} else {
				EEntity instances [] = getEntityInstancesExt(domain);
				for (int i = 0; i < instances.length; i++) {
					EEntity instance = instances[i];
					// excluding subtype instances, hopefully
					if (instance.isInstanceOf(domain)) {
						add2InstanceArray(instance);										
					}
				}
				printInstanceArray("exact_type_constraint, constraints is NULL, domain: " + domain, null, constraint, 3);
			}
			// sorting not needed, the result is already sorted
			moveInstanceArray2Constraint(constraint);
		} else { // not exact
			if (constraints != null) {
				// if constraints not null, then take all constraints instances that are of domain type
				// if domain already holds its popualation, then a simple intersection with constrains should be equivalent with
				EEntity constraints_array [] = getEntityInstancesExt(constraints);
				for (int i = 0; i < constraints_array.length; i++) {
					EEntity instance = constraints_array[i];
					if (instance.isKindOf(domain)) {
						add2InstanceArray(instance);										
					}
				}
				printInstanceArray("type_constraint, domain: " + domain + ", constraints: " + constraints, null, constraint, 3);
				// sorting not needed, the result is already sorted
				moveInstanceArray2Constraint(constraint);
			} else {
				// constrains does not exist, take the whole population of domain
				EEntity instances [] = getEntityInstancesExt(domain);
				printInstanceArray("type_constraint, constraints is NULL, domain: " + domain, instances, constraint, 3);
				constraint.setTemp(instances);
			}
		}
	} // processType_constraint()



//##################################### DEBUGGING #############################

	void printError(String message) {
		if (flag_errors) {
			System.out.println("ERROR: " + message);
		}
	}

	void printWarning(String message) {
		if (flag_warnings) {
			System.out.println("WARNING: " + message);
		}
	}

	void printDebug(String message) {
		if (flag_debug) {
			System.out.println("DEBUG: " + message);
		}
	}

	void printDebug(String message, int debug_detail_level) {
		if (debug_detail_level <= flag_debug_detail_level) {
			System.out.println("DEBUG: " + message);
		}
	}

	void printVerbose(String message) {
		if (flag_verbose) {
			System.out.println(message);
		}
	}

	void printInstanceArray(String message, EEntity [] array, EEntity constraint, int debug_detail_level) {
		if (!flag_print_array) return;
		if (flag_debug_detail_level < debug_detail_level) return;
		int count;
		if (array == null) {
			array = instance_array;
			count = instance_array_count;
//  return;
		} else {
			count = array.length;
// return;	
		}
		System.out.println("\n################################################################################");
		System.out.println(message);
		System.out.println("constraint level: " + constraint_level + ", constraint: " + constraint);
		System.out.println("--------------------------------------------------------------------------START-");
		for (int i = 0; i < count; i++) {
			//  this version uses an outside public class written by Sun, free for commercial use, but not a part of the java library
			//	System.out.println(formated_int.sprintf(i) + ": " + array[i]);

			// this version uses a simple method below
			System.out.println(formatInt2FixedWidth(i, 5) + ": " + formatInt2FixedWidth(array[i].hashCode(),9) + ": " + array[i]);
		}
		System.out.println("--------------------------------------------------------------------------END---\n");
	}

	String formatInt2FixedWidth(int k, int width) {
		// probably a more optimal implementation could be done
		String result = String.valueOf(k);
		while (result.length() < width) {
			result = " " + result;
		}
		return result;
	}


	void printElement1(EEntity element1) {							

		/* just checking what types of constraints occur in element1 of path_constraint */							
		if (element1 instanceof EConstraint) {
			if (element1 instanceof EConstraint_attribute) { // ABSTRACT 
		 		if (element1 instanceof EAggregate_member_constraint) {
					System.out.println("@@@###-02 ELEMENT1: aggregate_member_constraint: " + element1);
				} else
				if (element1 instanceof EAggregate_size_constraint) {
					System.out.println("@@@###-03 ELEMENT1: aggregate_size_constraint: " + element1);
				} else
				if (element1 instanceof EAttribute_value_constraint) {
					if (element1 instanceof EBoolean_constraint) {
						System.out.println("@@@###-04 ELEMENT1: boolean_constraint: " + element1);
					} else
					if (element1 instanceof EEnumeration_constraint) {
						System.out.println("@@@###-05 ELEMENT1: enumeration_constraint: " + element1);
					} else
					if (element1 instanceof EInteger_constraint) {
						System.out.println("@@@###-06 ELEMENT1: integer_constraint: " + element1);
		 			} else
		 			if (element1 instanceof ELogical_constraint) {
						System.out.println("@@@###-07 ELEMENT1: logical_constraint: " + element1);
		 			} else
		 			if (element1 instanceof ENon_optional_constraint) {
						System.out.println("@@@###-08 ELEMENT1: non_optional_constraint: " + element1);
					} else
					if (element1 instanceof EReal_constraint) {
						System.out.println("@@@###-09 ELEMENT1: real_constraint: " + element1);
					} else
					if (element1 instanceof EString_constraint) {
						System.out.println("@@@###-10 ELEMENT1: string_constraint: " + element1);
					} else {
						System.out.println("@@@###-11 ELEMENT1: unknown attribute_value_constraint: " + element1);
					}		 			
		 		} else
		 		if (element1 instanceof EEntity_constraint) {
		 			if (element1 instanceof EExact_entity_constraint) {
						System.out.println("@@@###-12 ELEMENT1: exact_entity_constraint: " + element1);
 					} else {
						System.out.println("@@@###-13 ELEMENT1: entity_constraint: " + element1);
 					}
				} else
				if (element1 instanceof ESelect_constraint) {
					System.out.println("@@@###-14 ELEMENT1: select_constraint: " + element1);
				} else {
					System.out.println("@@@###-15 ELEMENT1: unknown constraint_attribute: " + element1);
				}	
			} else
			if (element1 instanceof EConstraint_relationship) { // ABSTRACT
				if (element1 instanceof EInstance_constraint) { // ABSTRACT
					if (element1 instanceof EAnd_constraint_relationship) {
						System.out.println("@@@###-16 ELEMENT1: and_constraint_relationship: " + element1);
					} else
					if (element1 instanceof EInstance_equal) {
						System.out.println("@@@###-17 ELEMENT1: instance_equal: " + element1);
					} else
					if (element1 instanceof EOr_constraint_relationship) {
						System.out.println("@@@###-18 ELEMENT1: or_constraint_relationship: " + element1);
					}	else {
						System.out.println("@@@###-19 ELEMENT1: instance_constraint: " + element1);
					}				
				} else
				if (element1 instanceof EPath_constraint) {			
					System.out.println("@@@###-20 ELEMENT1: path_constraint: " + element1);
				}	else {
					System.out.println("@@@###-21 ELEMENT1: unknown constraint_relationship: " + element1);
				}	
			} else
			if (element1 instanceof EEnd_of_path_constraint) {
				System.out.println("@@@###-22 ELEMENT1: end_of_path_constraint: " + element1);
			} else
			if (element1 instanceof EIntersection_constraint) {
				System.out.println("@@@###-23 ELEMENT1: intersection_constraint: " + element1);
			} else
			if (element1 instanceof EInverse_attribute_constraint) {
				System.out.println("@@@###-24 ELEMENT1: inverse_attribute_constraint: " + element1);
			} else
			if (element1 instanceof ENegation_constraint) {
				System.out.println("@@@###-25 ELEMENT1: negation_constraint: " + element1);
			} else
			if (element1 instanceof EType_constraint) {
				if (element1 instanceof EExact_type_constraint) {
					System.out.println("@@@###-26 ELEMENT1: exact_type_constraint: " + element1);
				} else {
					System.out.println("@@@###-27 ELEMENT1: type_constraint: " + element1);
				}
      } else {
				System.out.println("@@@###-28 ELEMENT1: unknown constraint: " + element1);
      }
		} else
		if (element1 instanceof EExplicit_attribute) {
			System.out.println("@@@###-01 ELEMENT1: explicit_attribute: " + element1);
		} else {
			System.out.println("@@@###-29 ELEMENT1: unknown type: " + element1);
		}

		/* end of checking of possible element1 types */
	}


} // class


//####################################### OTHER CLASSES #######################

class Aim2ArmGeneratorSorterForInstances implements Comparator {
  public int compare(Object o1, Object o2) {
			int int1 = o1.hashCode();
			int int2 = o2.hashCode();

			if (int1 < int2) return -1;
			else if (int1 > int2) return 1;
			else return 0;

  }

}

class Aim2ArmGeneratorSorterForInstances2 implements Comparator {
  public int compare(Object o1, Object o2) {
    try {
      EEntity  m1 = (EEntity) o1;
      EEntity  m2 = (EEntity) o2;
			String str1 = m1.getPersistentLabel().substring(1);
			int int1 = Integer.parseInt(str1, 10);
			String str2 = m2.getPersistentLabel().substring(1);
			int int2 = Integer.parseInt(str2, 10);

			if (int1 < int2) return -1;
			else if (int1 > int2) return 1;
			else return 0;

    } catch (SdaiException exc) {
      exc.printStackTrace(System.err);

      return 0;
    }
  }
}

//					sortInstanceArray(instance_array, instance_array_count);
// 					moveInstanceArray2Constraint(constraint);
