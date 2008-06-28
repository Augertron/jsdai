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

command line switches:

-ARM arm_schema_name
	default: AP210_ARM

-AIM aim_schema_name
	default: ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN

-map map_name
	default: AP210_ARM
	
-entity ARM_entity_name
	generated a rule and a set of functions for this one entity only
	
-entities ARM_entity1_name [ARM_entity2_name] [...]
  similar to above, but for more than one entity	
	
-root_entity ARM_entity_name
	not yet supported
 	
-swap
	makes supertype entity_mapping constraints, if false, to skip the instance instead of failing the rule 	

The following switches control generation of additional print procedures in express code:

-cardinality - prints the number of results for an attribute

-result - prints the aggregate with results for an attribute

 	 	
The following switches control the informative express comments in the generated schema 	
 		
-verbose

-comments

-debug

-no_errors

*/


//import jsdai.dictionary.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.SMapping_schema.*;
import java.io.*;
import java.util.*;
import jsdai.lang.*;

public class RuleGenerator {

	static final String rg_version_minor     = "1";
	static final String rg_version_middle     = "9";
	static final String rg_build     = "053";
	static final String rg_date      = "2003-07-30";
	static final String rg_version_major   = "0";
	static final String rg_title     = "JSDAI(TM) EXPRESS Rule Generator \n\t\tfor checking AP210 AIM data (clause 5.2) for constraints introduced by \n\t\t\t- clause 4.2  Application objects, \n\t\t\t- clause 4.3  Application assertions and \n\t\t\t- clause 5.1 Mapping Tables";
	static final String rg_copyright = "Copyright (C) 2003 LKSoftWare GmbH";

//    for checking AP210 AIM data (clause 5.2) for constraints introduced by 
//   - clauses 4.2  Application objects, 
//   - clause 4.3  Application assertions and 
//   - clause 5.1 Mapping Tables 




	PrintWriter pw;
	PrintWriter pw1;
	int indentation;
	static boolean flag_print_attributes = false;
	static boolean flag_print_errors = false;
	static boolean flag_print_comments = false;
	static boolean flag_print_debug = false;
  static boolean flag_display_cardinality = false;
  static boolean flag_display_result = false;

	String ARM_name = "AP210_ARM";
	String AIM_name = "ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN";
	String AIM_schema_name;
	String MAP_name = "AP210_ARM";
	String ARM_entity_name_requested = null;
	Vector ARM_entities;
	SdaiModel global_map_model;
	
	boolean flag_all_on_demand = true;
	boolean flag_path_constraint_suppresses = false;
	boolean flag_on_demand = false;
	boolean flag_many_on_demand = false;
	boolean flag_root = false;
	static boolean flag_swap_supertype_constraints = false;

	public RuleGenerator() throws IOException {

    String pw_file = "x.exp";
		//pw = new PrintWriter(System.out, true);
		pw = getPrintWriter(pw_file);
	  pw1 = getPrintWriter("__RunALLRulesSeparately.bat");

	}

	public static final void main(String args[]) throws SdaiException, IOException  {

		RuleGenerator ge = new RuleGenerator();

		boolean flag_many_on_demand_active = false;
		
		// if both switches -entity and -entities are present simultaneuosly, the entity provided with -entity switch is ignored
	
		for (int ihi = 0; ihi < args.length; ihi++) {

			if (args[ihi].equalsIgnoreCase("-verbose")) flag_print_attributes = true;
			if (args[ihi].equalsIgnoreCase("-debug")) flag_print_debug = true;
			if (args[ihi].equalsIgnoreCase("-comments")) flag_print_comments = true;
			if (args[ihi].equalsIgnoreCase("-swap")) flag_swap_supertype_constraints = true;
			if (args[ihi].equalsIgnoreCase("-no_errors")) flag_print_errors = false;
			if (args[ihi].equalsIgnoreCase("-cardinality")) flag_display_cardinality = true;
			if (args[ihi].equalsIgnoreCase("-result")) flag_display_result = true;

			if (args[ihi].equalsIgnoreCase("-ARM")) {
				flag_many_on_demand_active = false;
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
				flag_many_on_demand_active = false;
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
				flag_many_on_demand_active = false;
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
			if (args[ihi].equalsIgnoreCase("-entity")) {
				flag_many_on_demand_active = false;
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("An ARM entity name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("An ARM entity name must follow the " + args[ihi-1] + " switch");
					return;
				}
				ge.ARM_entity_name_requested = args[ihi];
				ge.flag_on_demand = true;
			} else
			if (args[ihi].equalsIgnoreCase("-root_entity")) {
				flag_many_on_demand_active = false;
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("An ARM entity name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("An ARM entity name must follow the " + args[ihi-1] + " switch");
					return;
				}
				ge.ARM_entity_name_requested = args[ihi];
				ge.flag_on_demand = true;
				ge.flag_root = true;
			} else
			if (args[ihi].equalsIgnoreCase("-entities")) {
				flag_many_on_demand_active = false;
				ihi++;
				int entity_count = 0;
				ge.ARM_entities = new Vector(); 

				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						if (entity_count == 0) {
							System.out.println("At least one ARM entity name must follow the " + args[ihi-1] + " switch");
							return;
						}
					}
				} else {
					if (entity_count == 0) {
						System.out.println("At least one ARM entity name must follow the " + args[ihi-1] + " switch");
						return;
					}
				}
				ge.ARM_entities.addElement(args[ihi]);
				ge.flag_on_demand = true;
				ge.flag_many_on_demand = true;
				flag_many_on_demand_active = true;
			} else {
				if (args[ihi].substring(0,1).equals("-")) {
					flag_many_on_demand_active = false;
				} else {
					if (flag_many_on_demand_active) {
						ge.ARM_entities.addElement(args[ihi]);
					}
				}
			}
			
		}  // for - loop through all command line parameters and switches

		System.out.println("");
		System.out.println(rg_title);
		System.out.println(rg_copyright);
		System.out.println("version " + rg_version_major + "." + rg_version_middle + "." + rg_version_minor + " build " + rg_build + ", " + rg_date );
		System.out.println("-----------------------------------------------------------------------");




		ge.ARM_name += "_DICTIONARY_DATA";
		ge.AIM_name += "_DICTIONARY_DATA";
		ge.MAP_name += "_MAPPING_DATA";
		ge.AIM_schema_name = ge.AIM_name.substring(0,ge.AIM_name.length()-16);
		
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();

		// need RW for setting SdaiContext
		SdaiTransaction trans = session.startTransactionReadOnlyAccess();
//		SdaiTransaction trans = session.startTransactionReadWriteAccess();
		
		ge.printAllSchemas();
		session.closeSession();
	}

	public void printAllSchemas() throws SdaiException {

		SdaiSession session = SdaiSession.getSession();
		SchemaInstance si = session.getDataDictionary();

		SdaiRepository repository = null;
		ASdaiRepository repos = session.getKnownServers();
		SdaiIterator iter = repos.createIterator();

		while (iter.next()) {

			SdaiRepository rp = repos.getCurrentMember(iter);
			if (rp.getName().equalsIgnoreCase("ExpressCompilerRepo")) {
				repository = rp;
				break;
			} 
		}
		if (!repository.isActive()) {  // open repo if not open
				repository.openRepository();
		}

	
    //  setting context, alternative - to replace all instances of getParent(null) with getParent(null)
    // adding casts, and, perhaps, checking with instanceof to be safe, where unclear

//		ASdaiModel a_domain = repository.getSchemas().getAssociatedModels();
//		SdaiModel work = repository.createSdaiModel("working", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
//		jsdai.dictionary.ESchema_definition a_schema = work.getUnderlyingSchema();
//		SdaiContext _context = new SdaiContext(a_schema, a_domain, work);
//		session.setSdaiContext(_context);




   ASdaiModel models2 = repository.getModels();
		printSchemas(models2);
//		pw.println("END_SCHEMA; -- x");
    pw.flush();
    pw.close();
	  pw1.flush();
   	pw1.close();
	}

	public void printSchemas(ASdaiModel models) throws SdaiException {
		SdaiIterator iter = models.createIterator();
		SdaiModel aim_model = null;
		SdaiModel arm_model = null;
		SdaiModel map_model = null;
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			if (model.getName().equalsIgnoreCase(ARM_name)) {
				arm_model = model;
		    if (arm_model.getMode() == SdaiModel.NO_ACCESS) {
    			arm_model.startReadOnlyAccess();
    		}
			} else 
			if (model.getName().equalsIgnoreCase(AIM_name)) {
				aim_model = model;
	  	  if (aim_model.getMode() == SdaiModel.NO_ACCESS) {
  	  		aim_model.startReadOnlyAccess();
    		}
			} else 
			if (model.getName().equalsIgnoreCase(MAP_name)) {
				map_model = model;
		    if (map_model.getMode() == SdaiModel.NO_ACCESS) {
    			map_model.startReadOnlyAccess();
    		}
			} else {
			}
		}

		global_map_model = map_model;

		// just in case
		Aggregate constraints = map_model.getEntityExtentInstances(EConstraint.class);
		SdaiIterator constr_iter = constraints.createIterator();
		while (constr_iter.next()) {
			EConstraint constraint = (EConstraint)constraints.getCurrentMemberObject(constr_iter);
			constraint.setTemp(null);
		}

		loopARM(arm_model, aim_model, map_model);
		if (!((flag_on_demand) || (flag_all_on_demand))) {
			generateFunctionsInSequence(arm_model, aim_model, map_model);
		}
		
		
		pw.println("\nEND_SCHEMA; -- x");
	}


	void generateFxxFunction(EEntity perhaps_constraint, SdaiModel map_model) throws SdaiException {
		EConstraint constraint = null;

// System.out.println("generate constraint function: " + perhaps_constraint + ", flag on demand: " + flag_on_demand);
		
		if (!flag_all_on_demand) {
			if (!flag_on_demand) {
				return;
			}
		}
		if (perhaps_constraint == null) {
			return;
		}
		if (perhaps_constraint instanceof EConstraint) {
// System.out.println("generate constraint function: " + perhaps_constraint);
			constraint = (EConstraint)perhaps_constraint;
		} else {
// System.out.println("DO NOT generate constraint function: " + perhaps_constraint);
			// will have to see what to do, if attribute_mapping constraint = attribute, etc.
			return;
		}
    	
    	
    Object already_generated_for_this_constraint = constraint.getTemp();
    if (already_generated_for_this_constraint != null) {
// System.out.println("ALREADY GENERATED for this constraint: " + constraint);
    	return;
    }
   	constraint.setTemp(new Integer(1));


		Vector more_fxx = null;
		if (constraint instanceof EPath_constraint) {
			more_fxx = gHandlePath_constraint((EPath_constraint)constraint, map_model);
		} else {
			// perhaps better to generate as well, in the case the same constraint is used from several constraints or mappings
//    	Object mark_used_in_path_constraint_as_element1 = constraint.getTemp();
//    	if (mark_used_in_path_constraint_as_element1 != null) continue;

			pw.println("\n-- " + constraint);
			if (constraint instanceof EConstraint_attribute) { // ABSTRACT 
// System.out.println("generate constraint function for constraint_attribute: " + constraint);
				more_fxx = handleConstraint_attribute(constraint);
			} else
			if (constraint instanceof EConstraint_relationship) { // ABSTRACT
				more_fxx = handleConstraint_relationship(constraint);
			} else
			if (constraint instanceof EEnd_of_path_constraint) {
				more_fxx = handleEnd_of_path_constraint(constraint);
			} else
			if (constraint instanceof EIntersection_constraint) {
				more_fxx = handleIntersection_constraint(constraint);
			} else
			if (constraint instanceof EInverse_attribute_constraint) {
				more_fxx = handleInverse_attribute_constraint(constraint);
			} else
			if (constraint instanceof ENegation_constraint) {
				more_fxx = handleNegation_constraint(constraint);
			} else
			if (constraint instanceof EType_constraint) {
				more_fxx = handleType_constraint(constraint);
      } else {
      	// internal error - unknown constraint type
				String parameter_type_name = getParameterTypeName(constraint, map_model);
				String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
				pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
				System.out.println("-- ERROR: unknown constraint type: " + constraint);
				pw.println("\tRETURN (?);");
//				pw.println("END_FUNCTION;");
      }

			pw.println("END_FUNCTION;");
			

		} // if path

		if (more_fxx != null) {
			Iterator fxx_iterator = more_fxx.iterator();
			while (fxx_iterator.hasNext()) {
				EEntity next_constraint = (EEntity)fxx_iterator.next();
// System.out.println("ENDING, more from inside: " + next_constraint);
				generateFxxFunction(next_constraint, map_model);
			}
		} 

	}


	void generateFunctionsInSequence(SdaiModel arm_model, SdaiModel aim_model, SdaiModel map_model) throws SdaiException {

		/*	
		 		making it in two passes:
				1. path_constraints that also incorporate their elemet1 constraints together
				2. all the other constraints (except path_constraints, and their element1 constraints
		
		*/

		// just in case, unsetting all marks
		Aggregate constraints = map_model.getEntityExtentInstances(EConstraint.class);
		SdaiIterator iter = constraints.createIterator();
		while (iter.next()) {
			EConstraint constraint = (EConstraint)constraints.getCurrentMemberObject(iter);
			constraint.setTemp(null);
		}
		Aggregate path_constraints = map_model.getEntityExtentInstances(EPath_constraint.class);
		SdaiIterator path_iter = path_constraints.createIterator();
		while (path_iter.next()) {
			EPath_constraint path_constraint = (EPath_constraint)path_constraints.getCurrentMemberObject(path_iter);

			gHandlePath_constraint(path_constraint, map_model);
		}

		iter = constraints.createIterator();
		while (iter.next()) {
			EConstraint constraint = (EConstraint)constraints.getCurrentMemberObject(iter);

			// take care of path_constraint and its element1 constraint
			if (constraint instanceof EPath_constraint) continue;
    	Object mark_used_in_path_constraint_as_element1 = constraint.getTemp();
			
			/*
				generate functions even if the constraint is a part of a path_constraint combination
    		because now the constraint can be used multiple times, also not in path_constraint
    		Perhaps constraints can be tested for such multiple usage before generating functions for them
    	*/
    	// better do not mark in path_constraint itself, here, ok to test, no difference
    	
    	 if (mark_used_in_path_constraint_as_element1 != null) continue;
      
			

			/* constraint supertype-subtype hierarchy
			
			constraint (ABSTRACT)
				constraint_attribute (ABSTRACT) 
			 		aggregate_member_constraint
					aggregate_size_constraint
					attribute_value_constraint
						boolean_constraint
						enumeration_constraint
						integer_constraint
			 			logical_constraint
			 			non_optional_constraint
		 				real_constraint
		 				string_constraint
		 			
			 		entity_constraint
			 			exact_entity_constraint
		 		
					select_constraint

				constraint_relationship (ABSTRACT)
					instance_constraint (ABSTRACT)
						and_constraint_relationship
						instance_equal
						or_constraint_relationship
					
					path_constraint			
			
				end_of_path_constraint
				intersection_constraint
				inverse_attribute_constraint
				negation_constraint
				type_constraint
					exact_type_constraint

			



				if (constraint instanceof EConstraint_attribute) { // ABSTRACT 
			 		if (constraint instanceof EAggregate_member_constraint) {
					} else
					if (constraint instanceof EAggregate_size_constraint) {
					} else
					if (constraint instanceof EAttribute_value_constraint) {
						if (constraint instanceof EBoolean_constraint) {
						} else
						if (constraint instanceof EEnumeration_constraint) {
						} else
						if (constraint instanceof EInteger_constraint) {
			 			} else
			 			if (constraint instanceof ELogical_constraint) {
			 			} else
			 			if (constraint instanceof ENon_optional_constraint) {
		 				} else
		 				if (constraint instanceof EReal_constraint) {
		 				} else
		 				if (constraint instanceof EString_constraint) {
						}		 			
			 		} else
			 		if (constraint instanceof EEntity_constraint) {
			 			if (constraint instanceof EExact_entity_constraint) {
		 				}
					} else
					if (constraint instanceof ESelect_constraint) {
					} else {
						// internal error - unknown constraint_attribute
					}	
				} else
				if (constraint instanceof EConstraint_relationship) { // ABSTRACT
					if (constraint instanceof EInstance_constraint) { // ABSTRACT
						if (constraint instanceof EAnd_constraint_relationship) {
						} else
						if (constraint instanceof EInstance_equal) {
						} else
						if (constraint instanceof EOr_constraint_relationship) {
						}	else {
							// internal error - unknown instance_constraint
						}				
					} else
					if (constraint instanceof EPath_constraint) {			
					}	else {
						// internal error - unknown constraint_relationship
					}	
				} else
				if (constraint instanceof EEnd_of_path_constraint) {
				} else
				if (constraint instanceof EIntersection_constraint) {
				} else
				if (constraint instanceof EInverse_attribute_constraint) {
				} else
				if (constraint instanceof ENegation_constraint) {
				} else
				if (constraint instanceof EType_constraint) {
					if (constraint instanceof EExact_type_constraint) {
					} 
        } else {
        	// internal error - unknown constraint type
        }

*/


			pw.println("\n-- " + constraint);
			if (constraint instanceof EConstraint_attribute) { // ABSTRACT 
				handleConstraint_attribute(constraint);
			} else
			if (constraint instanceof EConstraint_relationship) { // ABSTRACT
				handleConstraint_relationship(constraint);
			} else
			if (constraint instanceof EEnd_of_path_constraint) {
				handleEnd_of_path_constraint(constraint);
			} else
			if (constraint instanceof EIntersection_constraint) {
				handleIntersection_constraint(constraint);
			} else
			if (constraint instanceof EInverse_attribute_constraint) {
				handleInverse_attribute_constraint(constraint);
			} else
			if (constraint instanceof ENegation_constraint) {
				handleNegation_constraint(constraint);
			} else
			if (constraint instanceof EType_constraint) {
				handleType_constraint(constraint);
      } else {
      	// internal error - unknown constraint type
				String parameter_type_name = getParameterTypeName(constraint, map_model);
				String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
				pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
				System.out.println("-- ERROR: unknown constraint type: " + constraint);
				pw.println("\tRETURN (?);");
//				pw.println("END_FUNCTION;");
      }

/*
			if (constraint instanceof EAttribute_value_constraint) {
				EEntity attribute = ((EAttribute_value_constraint)constraint).getAttribute(null);
				if (attribute instanceof EAttribute) {
					String attribute_name = ((EAttribute)attribute).getName(null);
					if (constraint instanceof EString_constraint) {
						// printDebug("-- string constraint");
						String constraint_value = ((EString_constraint)constraint).getConstraint_value(null);
						pw.println("\tRETURN (e." + attribute_name + " = \'" + constraint_value + "\');");
					} else
					if (constraint instanceof ELogical_constraint) {
					// printDebug("-- logical constraint");
						int constraint_value = ((ELogical_constraint)constraint).getConstraint_value(null);
						String constraint_value_str = null;
						switch (constraint_value) {
							case 1: 
								constraint_value_str = "FALSE";
								break;
							case 2:
								constraint_value_str = "TRUE";
								break;
							case 3:
								constraint_value_str = "UNKNOWN";
								break;
							case 0:
							default:
								constraint_value_str = "?";
							break;
						}
						pw.println("\tRETURN (e." + attribute_name + " = \'" + constraint_value_str + "\');");
					}
				} // EAttribute
			} else {
				pw.println("\tRETURN (TRUE);");
			}

*/
			pw.println("END_FUNCTION;");
		}
	}

	Vector handleConstraint_attribute(EConstraint constraint) throws SdaiException {
		Vector result = null;
		/*
			The constraint_attribute further constrains an Express-attribute, 
			an inverse_attribute_constraint or another constraint_attribute.
		*/

		EConstraint_attribute constraint_attribute = (EConstraint_attribute)constraint;
 		if (constraint instanceof EAggregate_member_constraint) {
			result = handleAggregate_member_constraint(constraint);
		} else
		if (constraint instanceof EAggregate_size_constraint) {
			result = handleAggregate_size_constraint(constraint);
		} else
		if (constraint instanceof EAttribute_value_constraint) {
			result = handleAttribute_value_constraint(constraint);
 		} else
 		if (constraint instanceof EEntity_constraint) {
// System.out.println("2 generate entity_constraint function: " + constraint);
			result = handleEntity_constraint(constraint);
		} else
		if (constraint instanceof ESelect_constraint) {
			result = handleSelect_constraint(constraint);
		} else {
			// internal error - unknown constraint_attribute
			SdaiModel map_model = global_map_model;
			String parameter_type_name = getParameterTypeName(constraint, map_model);
			String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
			pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
			System.out.println("ERROR: unknown constraint_attribute: " + constraint);
			pw.println("\tRETURN (?);");	
		}	
		return result;
	}			

	Vector handleAggregate_member_constraint(EConstraint constraint) throws SdaiException {
		printDebug("\n-- function implementing aggregate_member_constraint");
		/*
			An aggregate_member_constraint is a constraint that constraint aggregates. 
			It selects one specific or arbitrary element from aggregate. 
			attribute must point to an attribute of type aggregate.
		
			member
				If set, defines element in aggregate that must meet requirement, 
					e.g. [1], only valid for LIST and ARRAY but misused by APs.
				If unset, there shall be at least one member. Notation [i]
			
			attribute
				must be of aggregate type. 
				One element of is this aggregate is selected using aggregate_member_constraint.
			
		*/
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");

		EAggregate_member_constraint aggregate_member_constraint = (EAggregate_member_constraint)constraint;
  	String attribute_name = "_UNKNOWN__attribute_name";
  	String parent_entity_name = "_UNKNOWN_parent_";
  	String aggregate_type_name = "BAG OF GENERIC";

		// member : OPTIONAL INTEGER;
		int member = Integer.MIN_VALUE;
		if (aggregate_member_constraint.testMember(null)) {
			member = aggregate_member_constraint.getMember(null);
			printAttribute("\t-- member : OPTIONAL INTEGER = " + member); 
		}
		// attribute : aggregate_member_constraint_select;
		EEntity attribute = null;
		if (aggregate_member_constraint.testAttribute(null)) {
			attribute = aggregate_member_constraint.getAttribute(null);
			printAttribute("\t-- attribute : aggregate_member_constraint_select = " + attribute);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET attribute in " + constraint);
			printError("\t-- ERROR!!! UNSET attribute : aggregate_member_constraint_select");
		}

		// temp test
		// attribuete: aggregate_member_constraint_select
		if (attribute instanceof EAttribute) {
			if (member == Integer.MIN_VALUE) {
				// must be supported (implemented)
				printDebug("\t-- implementing aggregate_member_constraint, attribute: attribute, member unset");      
			} else {
				// does not occur in the current ap210 (implemented but not tested)
				printDebug("\t-- implementing aggregate_member_constraint, attribute: attribute, member set");      
			}
		} else
		if (attribute instanceof EInverse_attribute_constraint) {
			if (member == Integer.MIN_VALUE) {
				// does not occur in the current ap210
				printDebug("\t-- implementing aggregate_member_constraint, attribute: inverse_attribute_constraint, member unset");      
			} else {
				// does not occur in the current ap210
				printDebug("\t-- implementing aggregate_member_constraint, attribute: inverse_attribute_constraint, member set");      
			}
		} else
		if (attribute instanceof EAggregate_member_constraint) {
			if (member == Integer.MIN_VALUE) {
				// does not occur in the current ap210
				printDebug("\t-- implementing aggregate_member_constraint, attribute: aggregate_member_constraint, member unset");      
			} else {
				// does not occur in the current ap210
				printDebug("\t-- implementing aggregate_member_constraint, attribute: aggregate_member_constraint, member set");      
			}
		} else
		if (attribute instanceof EEntity_constraint) {
			if (member == Integer.MIN_VALUE) {
				// does not occur in the current ap210
				printDebug("\t-- implementing aggregate_member_constraint, attribute: entity_constraint, member unset");      
			} else {
				// does not occur in the current ap210
				printDebug("\t-- implementing aggregate_member_constraint, attribute: entity_constraint, member set");      
			}
		} else
		if (attribute instanceof ESelect_constraint) {
			if (member == Integer.MIN_VALUE) {
				// must be supported (implemented)
				printDebug("\t-- implementing aggregate_member_constraint, attribute: select_constraint, member unset");      
			} else {
				// does not occur in the current ap210 (implemented but not tested)
				printDebug("\t-- implementing aggregate_member_constraint, attribute: select_constraint, member set");      
			}
		} else {
				// does not occur in the current ap210
			printDebug("\t-- implementing aggregate_member_constraint, attribute: impossible type");      
		}





		if (attribute instanceof EAttribute) {
			attribute_name = ((EAttribute)attribute).getName(null).toLowerCase();
			parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
			EEntity_definition attribute_parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
			printDebug("\t-- attribute parent: " + attribute_parent_entity);      
	 			
			EEntity attribute_domain = null;
			if (attribute instanceof EExplicit_attribute) {
				printDebug("-- aggregate_member_constraint, attribute: explicit_attribute");
				attribute_domain = ((EExplicit_attribute)attribute).getDomain(null);
// printDebug("\t-- ROCOFOCO ");      
				printDebug("\t-- attribute domain: " + attribute_domain);      
	  		if (attribute_domain instanceof EAggregation_type) {
	  			aggregate_type_name = getAggregateTypeName((EAggregation_type)attribute_domain);
	  		}


				if (!(attribute_name.equals("_UNKNOWN__attribute_name"))) {
					if (member == Integer.MIN_VALUE) {

						pw.println("\tIF EXISTS(e) THEN"); 
//							pw.println("\t\tRETURN (e." + attribute_name + ");");
							pw.println("\t\tRETURN (e\\" + parent_entity_name + "." + attribute_name + ");");
				  	pw.println("\tELSE");
							pw.println("\t\tRETURN (?);");
						pw.println("\tEND_IF;");	
					} else {
						pw.println("\tLOCAL");      
						pw.println("\t\taggr : " + aggregate_type_name + ";");
						pw.println("\tEND_LOCAL;");      

						pw.println("\tIF NOT EXISTS(e) THEN"); 
							pw.println("\t\tRETURN (?);");
						pw.println("\tEND_IF;");	
//						pw.println("\taggr := e." + attribute_name + ";");
						pw.println("\taggr := e\\" + parent_entity_name + "." + attribute_name + ";");
						pw.println("\tIF SIZEOF(aggr) < " + member + " THEN"); 
							pw.println("\t\tRETURN (?);");
						pw.println("\tEND_IF;");	
						pw.println("\tRETURN (aggr[" + member + "]);");
					}
				} else {
					pw.println("\tRETURN (?);");
				}

	  	} else {
				// not in the current ap210
				printDebug("-- aggregate_member_constraint, attribute: attribute, not explicit -> to be implemented");
				pw.println("\tRETURN (?);");
	  	}
		} else
		if (attribute instanceof EInverse_attribute_constraint) {
				// not in the current ap210
				printDebug("-- aggregate_member_constraint, attribute: inverse_attribute_constraint -> to be implemented");
				pw.println("\tRETURN (?);");
		} else
		if (attribute instanceof EAggregate_member_constraint) {
				// not in the current ap210
				printDebug("-- aggregate_member_constraint, attribute: aggregate_member_constraint -> to be implemented");
				pw.println("\tRETURN (?);");
		} else
		if (attribute instanceof EEntity_constraint) {
				// not in the current ap210
				printDebug("-- aggregate_member_constraint, attribute: entity_constraint -> to be implemented");
				pw.println("\tRETURN (?);");
		} else
		if (attribute instanceof ESelect_constraint) {
				// must implement !!! present in ap210
				printDebug("-- aggregate_member_constraint, attribute: select_constraint");

//################################### start

			ESelect_constraint select_constraint = (ESelect_constraint)attribute;
			// attribute : select_constraint_select;
			EEntity attribute2 = null;
			String attribute2_name = "_UNKNOWN_ATTRIBUTE_NAME_";
			String parent_entity2_name = "_UNKNOWN_PARENT_";
			if (select_constraint.testAttribute(null)) {
				attribute2 = select_constraint.getAttribute(null);
				printAttribute("\t-- attribute : select_constraint_select = " + attribute2);
				if (attribute2 instanceof EAttribute) {
					attribute2_name = ((EAttribute)attribute2).getName(null).toLowerCase();
				  parent_entity2_name = ((EAttribute)attribute2).getParent(null).getName(null).toLowerCase();
				}
			} else {
				// ERROR - not OPTIONAL - must be set
				System.out.println("ERROR: UNSET attribute in " + attribute);
				printError("\t-- ERROR!!! UNSET attribute : select_constraint_select");
			} 

			if (!(attribute2_name.equals("_UNKNOWN__attribute_name"))) {

				// data_type : LIST [1:?] OF defined_type;
				ADefined_type data_type = null;
				if (select_constraint.testData_type(null)) {
					data_type = select_constraint.getData_type(null);
					printAttribute("\t-- data_type : LIST [1:?] OF defined_type = " + data_type);
					SdaiIterator itdt = data_type.createIterator();
					boolean first_time = true;
		
//					if (member != Integer.MIN_VALUE) {
//						pw.println("\tLOCAL");      
//						pw.println("\t\taggr : " + aggregate_type_name + ";");
//						pw.println("\tEND_LOCAL;");      
//					}
					
					pw.println("\tIF NOT EXISTS(e) THEN");
						pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");	


		
					while(itdt.next()) {
						EDefined_type dt = (EDefined_type)data_type.getCurrentMember(itdt);
						printDebug("-- select path element: " + dt.getName(null));
						if (first_time) {
							first_time = false;
//							pw.println("\tIF (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e." + attribute2_name + "))");
							pw.println("\tIF (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e\\" + parent_entity2_name + "." + attribute2_name + "))");
						} else {
//							pw.println("\tAND (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e." + attribute2_name + "))");
							pw.println("\tAND (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e\\" + parent_entity2_name + "." + attribute2_name + "))");
						}
					}
					if (!first_time) {
				
						pw.println("\tTHEN");



					if (member == Integer.MIN_VALUE) {
//							pw.println("\t\tRETURN (e." + attribute2_name + ");");
							pw.println("\t\tRETURN (e\\" + parent_entity2_name + "." + attribute2_name + ");");
							// perhaps this is the correct one :
							// pw.println("\t\tRETURN (e." + attribute_name + ");");
					} else {
						pw.println("\t\tIF NOT EXISTS(e\\" + parent_entity2_name + "." + attribute2_name + ")  THEN");
							pw.println("\t\t\tRETURN (?);");
						pw.println("\t\tEND_IF;");	
						pw.println("\t\tIF SIZEOF(e\\" + parent_entity2_name + "." + attribute2_name + ") < " + member + " THEN");
							pw.println("\t\t\tRETURN (?);");
						pw.println("\t\tEND_IF;");	
						pw.println("\t\tRETURN (e\\" + parent_entity2_name + "." + attribute2_name + "[" + member + "]);");
					}

						pw.println("\tEND_IF;");
					} else {
						pw.println("\tRETURN (?);");
					}
			
				}	else {
					// ERROR - not OPTIONAL - must be set
					System.out.println("ERROR: UNSET data_type in " + constraint);
					printError("\t-- ERROR!!! UNSET data_type : LIST [1:?] OF defined_type");
					pw.println("\tRETURN (?);");
				}

			} else {
				printDebug("\t-- incomplete implementation or corrupt data");
				pw.println("\tRETURN (?);");
			}

//################################### end

				pw.println("\tRETURN (?);");
		} else {
				printDebug("-- aggregate_member_constraint, attribute: incorret type -> to be implemented");
			// should not happen
			System.out.println("ERROR: incorrect attribute type in aggregate_member_constraint: " + attribute);
			printError("\t-- ERROR!!! incorrect attribute type in aggregate_member_constraint: " + attribute);
			pw.println("\tRETURN (?);");
		}

		return null;
	}				

	Vector handleAggregate_size_constraint(EConstraint constraint) throws SdaiException {
		printDebug("\n-- function implementing aggregate_size_constraint");
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		EAggregate_size_constraint aggregate_size_constraint = (EAggregate_size_constraint)constraint;
		// size : INTEGER;
		int size = Integer.MIN_VALUE;
		if (aggregate_size_constraint.testSize(null)) {
			size = aggregate_size_constraint.getSize(null);
			printAttribute("\t-- size : INTEGER = " + size);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET size in " + constraint);
			printError("\t-- ERROR!!! UNSET size : INTEGER");
		}
		// attribute : aggregate_member_constraint_select;
		EEntity attribute = null;
		if (aggregate_size_constraint.testAttribute(null)) {
			attribute = aggregate_size_constraint.getAttribute(null);
			printAttribute("\t-- attribute : aggregate_size_constraint_select = " + attribute);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET attribute in " + constraint);
			printError("\t-- ERROR!!! UNSET attribute : aggregate_size_constraint_select");
		}
		
		// attribute: aggregate_member_constraint_select
	  // attribute
	  // inverse_attribute_constraint
	  // aggregate_member_constraint
	  // entity_constraint
    // select_constraint
		
		
		
		return null;
	}				



	Vector handleAttribute_value_constraint(EConstraint constraint) throws SdaiException {
//		printDebug("\n-- function implementing attribute_value_constraint");
		Vector result = null;
		EAttribute_value_constraint attribute_value_constraint = (EAttribute_value_constraint)constraint;
		// attribute : attribute_value_constraint_select;
		EEntity attribute = null;
		if (attribute_value_constraint.testAttribute(null)) {
			attribute = attribute_value_constraint.getAttribute(null);
			printAttribute("\t-- attribute : attribute_value_constraint_select = " + attribute);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET attribute in " + constraint);
			printError("\t-- ERROR!!! UNSET attribute : attribute_value_constraint_select");
		} 
		String attribute_name = "_NOT_ATTRIBUTE_";
		String parent_entity_name = "_not_parent_";
		if (attribute instanceof EAttribute) {
			attribute_name = ((EAttribute)attribute).getName(null);
		  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
		} else
		if (attribute instanceof EAggregate_member_constraint) {
//			printDebug("\t -- attribute - aggregate_member_constraint: " + attribute);
		} else
		if (attribute instanceof ESelect_constraint) {
//			printDebug("\t -- attribute - select_constraint: " + attribute);
		} else {
			// internal error - illegal attribute type
			System.out.println("ERROR: attribute_value_constraint, illegal attribute type: " + attribute);
			printDebug("\t -- attribute - illegal type: " + attribute);
		}
		if (constraint instanceof EBoolean_constraint) {
			result = handleBoolean_constraint(constraint, attribute_name, attribute);
		} else
		if (constraint instanceof EEnumeration_constraint) {
			result = handleEnumeration_constraint(constraint, attribute_name, attribute);
		} else
		if (constraint instanceof EInteger_constraint) {
			result = handleInteger_constraint(constraint, attribute_name, attribute);
		} else
		if (constraint instanceof ELogical_constraint) {
			result = handleLogical_constraint(constraint, attribute_name, attribute);
		} else
		if (constraint instanceof ENon_optional_constraint) {
			// does not occur in the current ap210
			printDebug("\t -- implementing non_optional_constraint");
		} else
		if (constraint instanceof EReal_constraint) {
			result = handleReal_constraint(constraint, attribute_name, attribute);
		} else
		if (constraint instanceof EString_constraint) {
			result = handleString_constraint(constraint, attribute_name, attribute);
		} else {
			SdaiModel map_model = global_map_model;
			String parameter_type_name = getParameterTypeName(constraint, map_model);
			String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
			pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
			// perhaps everything below is not true, attribute_value_constraint must be ABSTRACT 
			/*
					attribute_value_constraint is not ABSTRACT
					perhaps direct its own instance can exist, 
					perhaps it could be handled here, or perhaps this else is not needed, 
					if only additional handling commont to all subtypes too
			*/
			System.out.println("ERROR (possibly) - this type should be ABSTRACT: " + constraint);
			printDebug("\t-- attribute_value_constraint, attribute: " + attribute);
			pw.println("\tRETURN (?);");
		}		 			
		return result;
	}		 		

	Vector handleBoolean_constraint(EConstraint constraint, String attribute_name, EEntity attribute) throws SdaiException {
		printDebug("\n-- function implementing boolean_constraint");
	
		String parent_entity_name =	"_unknown_parent_";
		// temp test
 		// attribute_value_constraint_select = SELECT
		if (attribute instanceof EAttribute) {
			printDebug("\n-- function implementing boolean_constraint, attribute: attribute");
			parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
		} else 
		if (attribute instanceof EAggregate_member_constraint) {
			printDebug("\n-- function implementing boolean_constraint, attribute: aggregate_member_constraint");
		} else 
		if (attribute instanceof ESelect_constraint) {
			printDebug("\n-- function implementing boolean_constraint, attribute: select_constraint");
		} else {
			printDebug("\n-- function implementing boolean_constraint, attribute: impossible type");
		} 


		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();

		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : BOOLEAN;");
		EBoolean_constraint boolean_constraint = (EBoolean_constraint)constraint;
		// constraint_value : BOOLEAN;
		boolean constraint_value = false;
		if (boolean_constraint.testConstraint_value(null)) {
			constraint_value = boolean_constraint.getConstraint_value(null);
			printAttribute("\t-- constraint_value : BOOLEAN = " + constraint_value);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraint_value in " + constraint);
			printError("\t-- ERROR!!! UNSET constraint_value : BOOLEAN");
		}
		String constraint_value_str = null;
		if (constraint_value) {
				constraint_value_str = "TRUE";
		} else {
				constraint_value_str = "FALSE";
		}
//		pw.println("\tRETURN (e." + attribute_name + " = \'" + constraint_value_str + "\');");
		
		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	
		

		pw.println("\tIF e\\" + parent_entity_name + "." + attribute_name + " = " + constraint_value_str + " THEN");
		pw.println("\t\tRETURN (TRUE);");
		pw.println("\tELSE");
		pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");
		return null;
	}					

	Vector handleEnumeration_constraint(EConstraint constraint, String attribute_name, EEntity attribute) throws SdaiException {
		printDebug("\n-- function implementing enumeration_constraint");
		// temp test
 		// attribute_value_constraint_select = SELECT
		String parent_entity_name = "_unknown_parent_";
		
		if (attribute instanceof EAttribute) {
			// does not occur in the current ap210
		  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
			printDebug("\n-- function implementing enumeration_constraint, attribute: attribute");
		} else 
		if (attribute instanceof EAggregate_member_constraint) {
			printDebug("\n-- function implementing enumeration_constraint, attribute: aggregate_member_constraint");
		} else 
		if (attribute instanceof ESelect_constraint) {
			printDebug("\n-- function implementing enumeration_constraint, attribute: select_constraint");
		} else {
			printDebug("\n-- function implementing enumeration_constraint, attribute: impossible type");
		} 




		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : BOOLEAN;");
		EEnumeration_constraint enumeration_constraint = (EEnumeration_constraint)constraint;
		// constraint_value : express_id;
		String constraint_value = null;
		if (enumeration_constraint.testConstraint_value(null)) {
			constraint_value = enumeration_constraint.getConstraint_value(null);
			printAttribute("\t-- constraint_value : express_id = " + constraint_value);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraint_value in " + constraint);
			printError("\t-- ERROR!!! UNSET constraint_value : express_id");
		}
//		pw.println("\tIF e." + attribute_name + " = \'" + constraint_value + "\' THEN");

		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	

		pw.println("\tIF e\\" + parent_entity_name + "." + attribute_name + " = " + constraint_value + " THEN");
		pw.println("\t\tRETURN (TRUE);");
		pw.println("\tELSE");
		pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");
		return null;
	}					

	Vector handleInteger_constraint(EConstraint constraint, String attribute_name, EEntity attribute) throws SdaiException {
		printDebug("\n-- function implementing integer_constraint");

		String parent_entity_name = "_unknown_parent_";
		// temp test
 		// attribute_value_constraint_select = SELECT
		if (attribute instanceof EAttribute) {
			parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
			// must be supported (implemented)
			printDebug("\n-- function implementing integer_constraint, attribute: attribute");
		} else 
		if (attribute instanceof EAggregate_member_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- function implementing integer_constraint, attribute: aggregate_member_constraint");
		} else 
		if (attribute instanceof ESelect_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- function implementing integer_constraint, attribute: select_constraint");
		} else {
			// does not occur in the current ap210
			printDebug("\n-- function implementing integer_constraint, attribute: impossible type");
		} 


		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : BOOLEAN;");
		EInteger_constraint integer_constraint = (EInteger_constraint)constraint;
		// constraint_value : INTEGER;
		int constraint_value = Integer.MIN_VALUE;
		if (integer_constraint.testConstraint_value(null)) {
			constraint_value = integer_constraint.getConstraint_value(null);
			printAttribute("\t-- constraint_value : INTEGER = " + constraint_value);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraint_value in " + constraint);
			printError("\t-- ERROR!!! UNSET constraint_value : INTEGER");
		}
//		pw.println("\tRETURN (e." + attribute_name + " = \'" + constraint_value + "\');");

		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	

		pw.println("\tIF e\\" + parent_entity_name + "." + attribute_name + " = " + constraint_value + " THEN");
		pw.println("\t\tRETURN (TRUE);");
		pw.println("\tELSE");
		pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");
		return null;
	}		 			



	Vector handleLogical_constraint(EConstraint constraint, String attribute_name, EEntity attribute) throws SdaiException {
		printDebug("\n-- function implementing logical_constraint");

		String parent_entity_name = "_unknown_parent_";
		// temp test
 		// attribute_value_constraint_select = SELECT
		if (attribute instanceof EAttribute) {
		  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
			// must be supported - implemented
			printDebug("\n-- function implementing logical_constraint, attribute: attribute");
		} else 
		if (attribute instanceof EAggregate_member_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- function implementing logical_constraint, attribute: aggregate_member_constraint");
		} else 
		if (attribute instanceof ESelect_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- function implementing logical_constraint, attribute: select_constraint");
		} else {
			// does not occur in the current ap210
			printDebug("\n-- function implementing logical_constraint, attribute: impossible type");
		} 

		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : BOOLEAN;");
		/*
			A logical_constraint specifies a constraint on logical value. 
			
			attribute 
				must point to attribute that is of type logical.
		*/
		ELogical_constraint logical_constraint = (ELogical_constraint)constraint;
		// constraint_value : LOGICAL;
		int constraint_value = 0;
		if (logical_constraint.testConstraint_value(null)) {
			constraint_value = logical_constraint.getConstraint_value(null);
			printAttribute("\t-- constraint_value : LOGICAL = " + constraint_value);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraint_value in " + constraint);
			printError("\t-- ERROR!!! UNSET constraint_value : LOGICAL");
		}
		String constraint_value_str = null;
		switch (constraint_value) {
			case 1: 
				constraint_value_str = "FALSE";
				break;
			case 2:
				constraint_value_str = "TRUE";
				break;
			case 3:
				constraint_value_str = "UNKNOWN";
				break;
			case 0:
			default:
				constraint_value_str = "?";
			break;
		}
//		pw.println("\tRETURN (e." + attribute_name + " = \'" + constraint_value_str + "\');");


		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	


		pw.println("\tIF e\\" + parent_entity_name + "." + attribute_name + " = " + constraint_value_str + " THEN");
//		pw.println("\tIF e." + attribute_name + " = \'" + constraint_value_str + "\' THEN");
		pw.println("\t\tRETURN (TRUE);");
		pw.println("\tELSE");
		pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");
		return null;
	}		 			


	Vector handleReal_constraint(EConstraint constraint, String attribute_name, EEntity attribute) throws SdaiException {
		// does not occur in the current ap210
		printDebug("\n-- function implementing real_constraint");

		String parent_entity_name = "_unknown_parent_";
		// temp test
 		// attribute_value_constraint_select = SELECT
		if (attribute instanceof EAttribute) {
		  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
			printDebug("\n-- function implementing real_constraint, attribute: attribute");
		} else 
		if (attribute instanceof EAggregate_member_constraint) {
			printDebug("\n-- function implementing real_constraint, attribute: aggregate_member_constraint");
		} else 
		if (attribute instanceof ESelect_constraint) {
			printDebug("\n-- function implementing real_constraint, attribute: select_constraint");
		} else {
			printDebug("\n-- function implementing real_constraint, attribute: impossible type");
		} 

		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : BOOLEAN;");
		EReal_constraint real_constraint = (EReal_constraint)constraint;
		// constraint_value : REAL;
		double constraint_value = Double.NaN;
		if (real_constraint.testConstraint_value(null)) {
			constraint_value = real_constraint.getConstraint_value(null);
			printAttribute("\t-- constraint_value : REAL = " + constraint_value);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraint_value in " + constraint);
			printError("\t-- ERROR!!! UNSET constraint_value : REAL");
		}
		
		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\tRETURN (?);");
		pw.println("\tEND_IF;");	

		if (attribute instanceof EAttribute) {

			pw.println("\tIF e\\" + parent_entity_name + "." + attribute_name + " = " + constraint_value + " THEN");
			pw.println("\t\tRETURN (TRUE);");
			pw.println("\tELSE");
			pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");

		} else 
		if (attribute instanceof EAggregate_member_constraint) {
			int member = Integer.MIN_VALUE;
			if (((EAggregate_member_constraint)attribute).testMember(null)) {
				member = ((EAggregate_member_constraint)attribute).getMember(null);
				// printAttribute("\t-- member : OPTIONAL INTEGER = " + member); 
				printDebug("\n-- function implementing real_constraint, attribute: aggregate_member_constraint, member: " + member);
			} else {
				// does not occur in ap210 wd36
				printDebug("\n-- function implementing real_constraint, attribute: aggregate_member_constraint, member is unset");
			}
			EEntity attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
			if (attribute2 instanceof EExplicit_attribute) {
				printDebug("\n-- function implementing real_constraint, attribute: aggregate_member_constraint, its attribute - explicit_attribute");
			
				String attribute2_name = ((EAttribute)attribute2).getName(null).toLowerCase();
			  String parent_entity2_name = ((EAttribute)attribute2).getParent(null).getName(null).toLowerCase();

				if (!(member == Integer.MIN_VALUE)) {

					pw.println("\tIF e\\" + parent_entity2_name + "." + attribute2_name + "[" + member + "] = " + constraint_value + " THEN");
					pw.println("\t\tRETURN (TRUE);");
					pw.println("\tELSE");
					pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");

				} else {
					// not implemented, does not occur in ap210 wd36
					pw.println("NOT IMPLEMENTED in real_constraint 001");
				}
			
			} else {
				// does not occur in ap210 wd36
				printDebug("\n-- function implementing real_constraint, attribute: aggregate_member_constraint, its attribute - some other type");
			}

		} else {
			// select or error, but both do not occur in ap210 up to and including wd36
		}		
		
		return null;
	}	 				


	Vector handleString_constraint(EConstraint constraint, String attribute_name, EEntity attribute) throws SdaiException {
		printDebug("\n-- function implementing string_constraint");

		String parent_entity_name = "_unknown_parent_";
		// temp test
 		// attribute_value_constraint_select = SELECT
		if (attribute instanceof EAttribute) {
		  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
			// must be supported (implemented)
			printDebug("\n-- function implementing string_constraint, attribute: attribute");
		} else 
		if (attribute instanceof EAggregate_member_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- function implementing string_constraint, attribute: aggregate_member_constraint");
		} else 
		if (attribute instanceof ESelect_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- function implementing string_constraint, attribute: select_constraint");
		} else {
			printDebug("\n-- function implementing string_constraint, attribute: impossible type");
		} 

		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : BOOLEAN;");
		EString_constraint string_constraint = (EString_constraint)constraint;
		// constraint_value : STRING;
		String constraint_value = null;
		if (string_constraint.testConstraint_value(null)) {
			constraint_value = string_constraint.getConstraint_value(null);
			printAttribute("\t-- constraint_value : STRING = " + constraint_value);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraint_value in " + constraint);
			printError("\t-- ERROR!!! UNSET constraint_value : STRING");
		}
//		pw.println("\tRETURN (e." + attribute_name + " = \'" + constraint_value + "\');");
		if (attribute instanceof EAttribute) {
		
		
		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	
		
			pw.println("\tIF e\\" + parent_entity_name + "." + attribute_name + " = \'" + constraint_value + "\' THEN");
			pw.println("\t\tRETURN (TRUE);");
			pw.println("\tELSE");
			pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");
		} else {
			// does not occur in the current ap210
			printDebug("\t-- string_constraint, attribute is not attribute");
			pw.println("\tRETURN (?);");
		}
		return null;
	}

	Vector handleEntity_constraint(EConstraint constraint) throws SdaiException {
		printDebug("\n-- function implementing entity_constraint");
		/*
			An entity_constraint specifies a constraint on entity type. 
			The attribute must point to an attribute or constraint definition that is of an entity type. 
			This constraint restricts to subtypes or select types (maybe to complex types).
	
			domain 
				defines an entity type. 
				The value of constraint attribute (it also may be element of aggregate) must by of this type.

 			attribute 
 				is a definition of attribute that is constraint to some entity type.

			----------------
			
			e.attribute type must be domain
		
		*/
// System.out.println("IN entity_constraint function: " + constraint);

		EEntity_constraint entity_constraint = (EEntity_constraint)constraint;
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
//		String domain_name = "_UNKNOWN_DOMAIN_";
		String domain_name = "GENERIC";
		String domain_schema_name = AIM_schema_name;
		String attribute_name = "_UNKNOWN_ATTRIBUTE_";
		String parent_entity_name = "_unknown_parent_";
		// domain : entity_definition;
		EEntity_definition domain = null;
		if (entity_constraint.testDomain(null)) {
			domain = entity_constraint.getDomain(null);
			printAttribute("\t-- domain : entity_definition = " + domain);
			domain_name = domain.getName(null).toUpperCase();
			String domain_model_name = domain.findEntityInstanceSdaiModel().getName();	
      domain_schema_name = domain_model_name.substring(0, domain_model_name.length() - 16).toUpperCase();
			if (domain_schema_name.equals("MIXED_COMPLEX_TYPES")) {
				domain_schema_name = AIM_schema_name;
			}
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET domain in " + constraint);
			printError("\t-- ERROR!!! UNSET domain : entity_definition");
		}
		// attribute : attribute_select;
		EEntity attribute = null;
		if (entity_constraint.testAttribute(null)) {
			attribute = entity_constraint.getAttribute(null);
			printAttribute("\t-- attribute : attribute_select = " + attribute);



			if (attribute instanceof EAttribute) {
				printDebug("\n-- implementing entity_constraint, attribute: attribute");
				attribute_name = ((EAttribute)attribute).getName(null).toLowerCase();
			  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();

				pw.println("\tIF NOT EXISTS (e) THEN");
					pw.println("\t\tRETURN (?);");
				pw.println("\tEND_IF;");	

				if (!attribute_name.equalsIgnoreCase("_UNKNOWN_ATTRIBUTE_")) {
					pw.println("\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (e\\" + parent_entity_name + "." + attribute_name + ") THEN");
// experiment-entity
					pw.println("\t\tRETURN (e\\" + parent_entity_name + "." + attribute_name + ");");
//					pw.println("\t\tRETURN (e);");
					pw.println("\tELSE");
					pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");
				} else {
					printDebug("\t-- unknown attribute name, to be implemented - TYPEOF(e.attribute_name)");
					pw.println("\t\tRETURN (?);");
					// pw.println("\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (e) THEN");
				}

			} else
			if (attribute instanceof EAggregate_member_constraint) {
				// member : OPTIONAL INTEGER;
				int member = Integer.MIN_VALUE;
				if (((EAggregate_member_constraint)attribute).testMember(null)) {
					member = ((EAggregate_member_constraint)attribute).getMember(null);
					// printAttribute("\t-- member : OPTIONAL INTEGER = " + member); 
				}
		  	String aggregate_type_name = "BAG OF GENERIC";
				EEntity attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
printAttribute("\t-- attribute in aggregate_member_constraint : attribute_select = " + attribute2);
				if (attribute2 instanceof EExplicit_attribute) {
					printDebug("\n-- implementing entity_constraint, attribute: aggregate_member_constraint, attribute: attribute");
						EEntity xa_domain = ((EExplicit_attribute)attribute2).getDomain(null);
printDebug("-- attribute in  aggregate_member_constraint, domain: " + xa_domain );						
					if (xa_domain instanceof EAggregation_type) {
	  					aggregate_type_name = getAggregateTypeName((EAggregation_type)xa_domain);
// printDebug("-- attribute (aggregate_member_constraint) domain (aggregate): " + xa_domain );						
						EEntity xa_element_domain = ((EAggregation_type)xa_domain).getElement_type(null);
						if (xa_element_domain instanceof EEntity_definition) {
							attribute_name = ((EEntity_definition)xa_element_domain).getName(null).toLowerCase();

						} else
						if (xa_element_domain instanceof EDefined_type) {
							attribute_name = ((EDefined_type)xa_element_domain).getName(null).toLowerCase();
						} else {
							printDebug("-- element domain not entity or defined type: " + xa_element_domain );						
						}
					} else 
					if (xa_domain instanceof EEntity_definition) {
						attribute_name = ((EEntity_definition)xa_domain).getName(null).toLowerCase();
					} else {
						printDebug("-- attribute (aggregate_member_constraint) domain: " + xa_domain );						
					}
				
					// skip the above garbage, probably done too much there
					attribute_name = ((EAttribute)attribute2).getName(null).toLowerCase();
				  parent_entity_name = ((EAttribute)attribute2).getParent(null).getName(null).toLowerCase();

				} else {
					printDebug("\n-- implementing entity_constraint, attribute: aggregate_member_constraint, attribute: other types");
					printDebug("-- attribute of aggregate_member_constraint not explicit: " + attribute2 );						
				}

				pw.println("\tLOCAL");      
				pw.println("\t\tresult : BAG OF GENERIC;");
				pw.println("\t\tr : GENERIC;");
//				pw.println("\t\taggr : BAG OF " + bag_member_type + " := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
				pw.println("\t\taggr : " + aggregate_type_name + ";");
				pw.println("\tEND_LOCAL;");      

				pw.println("\tIF NOT EXISTS (e) THEN");
					pw.println("\t\tRETURN (?);");
				pw.println("\tEND_IF;");	

				if (!attribute_name.equalsIgnoreCase("_UNKNOWN_ATTRIBUTE_")) {

					pw.println("\taggr := e\\" + parent_entity_name + "." + attribute_name + ";");	
	
					pw.println("\tIF NOT EXISTS (aggr) THEN");
						pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");	
				
					if (member == Integer.MIN_VALUE) {

						pw.println("\tREPEAT i:=1 TO SIZEOF(aggr);");
							pw.println("\t\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (aggr[i]) THEN");
								pw.println("\t\t\tr := aggr[i];");
								pw.println("\t\t\tIF EXISTS(r) THEN");
									pw.println("\t\t\t\tIF NOT EXISTS(result) THEN");
										pw.println("\t\t\t\t\tresult := [];");
									pw.println("\t\t\t\tEND_IF;");
									pw.println("\t\t\t\tresult := result + r;");
								pw.println("\t\t\tEND_IF;");
							pw.println("\t\tEND_IF;");
						pw.println("\tEND_REPEAT;");
						pw.println("\tIF SIZEOF(result) = 1 THEN");
							pw.println("\t\tRETURN (result[1]);");      
						pw.println("\tELSE");
							pw.println("\t\tRETURN (result);");      
						pw.println("\tEND_IF;");

					} else {	
						printDebug("\n-- implementing entity_constraint, attribute: aggregate_member_constraint, membmer set");
						pw.println("\tIF SIZEOF(aggr) < " + member + " THEN");
							pw.println("\t\tRETURN (?);");
						pw.println("\tEND_IF;");	

						pw.println("\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (aggr[" + member + "]) THEN");
						pw.println("\t\tRETURN (aggr[" + member + "]);");
						pw.println("\tELSE");
						pw.println("\t\tRETURN (?);");
						pw.println("\tEND_IF;");
					}

	
				} else {
					printDebug("\t-- unknown attribute name, to be implemented - TYPEOF(e.attribute_name)");
					pw.println("\t\tRETURN (?);");
					// pw.println("\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (e) THEN");
				}
		
			} else 
			if (attribute instanceof EInverse_attribute_constraint) {
				// must be implemented !!!
				printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint");

				EInverse_attribute_constraint inverse_attribute_constraint = (EInverse_attribute_constraint)attribute;
				// inverted_attribute : inverse_attribute_constraint_select;
				EEntity inverted_attribute = null;
				if (inverse_attribute_constraint.testInverted_attribute(null)) {
					inverted_attribute = inverse_attribute_constraint.getInverted_attribute(null);
					printAttribute("\t-- inverted_attribute : inverse_attribute_constraint_select = " + inverted_attribute);
				} else {
					// ERROR - not OPTIONAL - must be set
					System.out.println("ERROR: UNSET inverted_attribute in " + constraint);
					printError("\t-- ERROR!!! UNSET inverted_attribute : inverse_attribute_constraint_select");
				}

				String inverted_attribute_name = "UNKNOWN__inverted_attribute_name";
				String inverted_attribute_parent_entity_name = "UNKNOWN_inverted_attribute_parent_entity_name";
				String inverted_attribute_parent_entity_schema_name = "x";

  			if (inverted_attribute instanceof EAttribute) {

					printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint, inverted: attribute");
					inverted_attribute_name = ((EAttribute)inverted_attribute).getName(null).toLowerCase();
					inverted_attribute_parent_entity_name = ((EAttribute)inverted_attribute).getParent(null).getName(null).toLowerCase();
					printAttribute("\t-- inverted_attribute parent entity: " + inverted_attribute_parent_entity_name);
					String inverted_attribute_parent_entity_model_name = ((EAttribute)inverted_attribute).getParent(null).findEntityInstanceSdaiModel().getName();	
	    		inverted_attribute_parent_entity_schema_name = inverted_attribute_parent_entity_model_name.substring(0, inverted_attribute_parent_entity_model_name.length() - 16).toLowerCase();


					pw.println("\tLOCAL");
						pw.println("\t\tresult : BAG OF GENERIC;");
						pw.println("\t\tr : GENERIC;");
						pw.println("\t\taggr : BAG OF GENERIC;");
					pw.println("\tEND_LOCAL;");

					pw.println("\tIF NOT EXISTS (e) THEN");
						pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");

					pw.println("\taggr := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");

					pw.println("\tIF NOT EXISTS (aggr) THEN");
						pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");

					pw.println("\tREPEAT i:=1 TO SIZEOF(aggr);");
					pw.println("\t\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (aggr[i]) THEN");
							pw.println("\t\t\tr := aggr[i];");
							pw.println("\t\t\tIF EXISTS(r) THEN");
								pw.println("\t\t\t\tIF NOT EXISTS(result) THEN");
									pw.println("\t\t\t\t\tresult := [];");
								pw.println("\t\t\t\tEND_IF;");
								pw.println("\t\t\t\tresult := result + r;");
							pw.println("\t\t\tEND_IF;");
						pw.println("\t\tEND_IF;");
					pw.println("\tEND_REPEAT;");
					pw.println("\tIF SIZEOF(result) = 1 THEN");
						pw.println("\t\tRETURN (result[1]);");
					pw.println("\tELSE");
						pw.println("\t\tRETURN (result);");
					pw.println("\tEND_IF;");



				} else
				if (inverted_attribute instanceof EEntity_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint, inverted: entity_constraint");
				} else
				if (inverted_attribute instanceof EAggregate_member_constraint) {
					// must be implemented !!!
					printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint, inverted: aggregate_member_constraint");


//4444444444444444444444444444444444444444444444444 start



				int member = Integer.MIN_VALUE;
				if (((EAggregate_member_constraint)inverted_attribute).testMember(null)) {
					member = ((EAggregate_member_constraint)inverted_attribute).getMember(null);
					// printAttribute("\t-- member : OPTIONAL INTEGER = " + member); 
				}
				if (member != Integer.MIN_VALUE) {
					printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint, inverted_attribute: aggregate_member_constraint,  member set");
				}
		  	String aggregate_type_name = "BAG OF GENERIC";
				EEntity attribute3 = ((EAggregate_member_constraint)inverted_attribute).getAttribute(null);
printAttribute("\t-- attribute in aggregate_member_constraint : attribute_select = " + attribute3);
				if (attribute3 instanceof EExplicit_attribute) {
						EEntity xa_domain = ((EExplicit_attribute)attribute3).getDomain(null);
printDebug("-- attribute in  aggregate_member_constraint, domain: " + xa_domain );						
					if (xa_domain instanceof EAggregation_type) {
	  				aggregate_type_name = getAggregateTypeName((EAggregation_type)xa_domain);
// printDebug("-- attribute (aggregate_member_constraint) domain (aggregate): " + xa_domain );						
						EEntity xa_element_domain = ((EAggregation_type)xa_domain).getElement_type(null);
						if (xa_element_domain instanceof EEntity_definition) {
							attribute_name = ((EEntity_definition)xa_element_domain).getName(null).toLowerCase();
						} else
						if (xa_element_domain instanceof EDefined_type) {
							attribute_name = ((EDefined_type)xa_element_domain).getName(null).toLowerCase();
						} else {
							printDebug("-- element domain not entity or defined type: " + xa_element_domain );						
						}
					} else 
					if (xa_domain instanceof EEntity_definition) {
						attribute_name = ((EEntity_definition)xa_domain).getName(null).toLowerCase();
					} else {
						printDebug("-- attribute (aggregate_member_constraint) domain: " + xa_domain );						
					}
				
					// skip the above garbage, probably done too much there
					attribute_name = ((EAttribute)attribute3).getName(null).toLowerCase();
				  parent_entity_name = ((EAttribute)attribute3).getParent(null).getName(null).toLowerCase();




//-111111111111111111111111111111 start


				printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint, inverted: attribute");
					inverted_attribute_name = ((EAttribute)attribute3).getName(null).toLowerCase();
					inverted_attribute_parent_entity_name = ((EAttribute)attribute3).getParent(null).getName(null).toLowerCase();
//					printAttribute("\t-- inverted_attribute parent entity: " + inverted_attribute_parent_entity_name);
//					String inverted_attribute_parent_entity_model_name = ((EAttribute)inverted_attribute).getParent(null).findEntityInstanceSdaiModel().getName();	
//	    		inverted_attribute_parent_entity_schema_name = inverted_attribute_parent_entity_model_name.substring(0, inverted_attribute_parent_entity_model_name.length() - 16).toLowerCase();


					pw.println("\tLOCAL");
						pw.println("\t\tresult : BAG OF GENERIC;");
						pw.println("\t\tr : GENERIC;");
						pw.println("\t\taggr : BAG OF GENERIC;");
					pw.println("\tEND_LOCAL;");

					pw.println("\tIF NOT EXISTS (e) THEN");
						pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");

					pw.println("\taggr := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");

					pw.println("\tIF NOT EXISTS (aggr) THEN");
						pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");

					pw.println("\tREPEAT i:=1 TO SIZEOF(aggr);");
					pw.println("\t\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (aggr[i]) THEN");
							pw.println("\t\t\tr := aggr[i];");
							pw.println("\t\t\tIF EXISTS(r) THEN");
								pw.println("\t\t\t\tIF NOT EXISTS(result) THEN");
									pw.println("\t\t\t\t\tresult := [];");
								pw.println("\t\t\t\tEND_IF;");
								pw.println("\t\t\t\tresult := result + r;");
							pw.println("\t\t\tEND_IF;");
						pw.println("\t\tEND_IF;");
					pw.println("\tEND_REPEAT;");
					pw.println("\tIF SIZEOF(result) = 1 THEN");
						pw.println("\t\tRETURN (result[1]);");
					pw.println("\tELSE");
						pw.println("\t\tRETURN (result);");
					pw.println("\tEND_IF;");





//--22222222222222222222222222222 end


				} else {
						printDebug("-- entity_constraint, attribute = inverse_attribute, inverted_attribute = aggregate_member_constraint, its attribute not explicit: " + attribute3 );						
				}





//4444444444444444444444444444444444444444444444444444 end






				} else
				if (inverted_attribute instanceof ESelect_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint, inverted: select_constraint");
				} else {
					// all possible types already above 
					// does not occur in the current ap210
					printDebug("\n-- implementing entity_constraint, attribute = inverse_attribute_constraint, inverted: impossible type");
				}

//			pw.println("\tLOCAL");      
//			pw.println("\t\tresult : BAG OF GENERIC := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
//			pw.println("\tEND_LOCAL;");      
//			pw.println("\tIF EXISTS(result) THEN");
//			pw.println("\t\tIF SIZEOF(result) > 0 THEN");
//			pw.println("\t\t\tRETURN (result);");      
//			pw.println("\t\tELSE");
//				pw.println("\t\t\tRETURN (?);");
//				pw.println("\t\tEND_IF;");
//			pw.println("\tELSE");
//				pw.println("\t\tRETURN (?);");
//			pw.println("\tEND_IF;");





			} else {
				// do not occur in the current ap210
				printDebug("\n-- implementing other entity_constraint attribute types - not implemented");
			}
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET attribute in " + constraint);
			printError("\t-- ERROR!!! UNSET attribute : attribute_select");
		} 
		if (constraint instanceof EExact_entity_constraint) {
			/*
				An exact_entity_constraint is entity constraint that constraints the attribute to be 
				of some specific entity type, excluding subtypes of it.
			*/

			// does not occur in the current ap210
			printDebug("\n-- implementing exact_entity_constraint - not implemented");
			
		}



		return null;
	}				



	Vector handleSelect_constraint(EConstraint constraint) throws SdaiException {
		printDebug("\n-- function implementing select_constraint");
		/*
		
			A select_constraint specifies a constraint on select type. Attribute must point to attribute that is of select type.

			Complex instances may exist with logical_, boolean_, string_, enumeration_, integer_ and real_constraint.
		
			data_type 
				defines the path to go throw select.
		
			attribute 
				is definition of attribute or other element that is of select type and is constraint to some specific selection.
		
		*/
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		ESelect_constraint select_constraint = (ESelect_constraint)constraint;

		// attribute : select_constraint_select;
		EEntity attribute = null;
		String attribute_name = "_UNKNOWN_ATTRIBUTE_NAME_";
		String parent_entity_name = "_UNKNOWN_PARENT_";
		if (select_constraint.testAttribute(null)) {
			attribute = select_constraint.getAttribute(null);
			printAttribute("\t-- attribute : select_constraint_select = " + attribute);
			if (attribute instanceof EAttribute) {
				attribute_name = ((EAttribute)attribute).getName(null).toLowerCase();
			  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
			}
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET attribute in " + constraint);
			printError("\t-- ERROR!!! UNSET attribute : select_constraint_select");
		} 

		// temp testing
		if (attribute instanceof EAttribute) {
			printDebug("\t-- implementing select_constraint, attribute: attribute");
		} else
		if (attribute instanceof EAggregate_member_constraint) {
			printDebug("\t-- implementing select_constraint, attribute: aggregate_member_constraint");
		} else {
			printDebug("\t-- implementing select_constraint, attribute: impossible type");
		}


		// data_type : LIST [1:?] OF defined_type;
		ADefined_type data_type = null;
		if (select_constraint.testData_type(null)) {
			data_type = select_constraint.getData_type(null);
			printAttribute("\t-- data_type : LIST [1:?] OF defined_type = " + data_type);
			SdaiIterator itdt = data_type.createIterator();
			boolean first_time = true;
		
			pw.println("\tIF NOT EXISTS(e) THEN");
				pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");	


		
			while(itdt.next()) {
				EDefined_type dt = (EDefined_type)data_type.getCurrentMember(itdt);
				printDebug("-- select path element: " + dt.getName(null));
				if (first_time) {
					first_time = false;
					pw.println("\tIF (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e\\" + parent_entity_name + "." + attribute_name + "))");
				} else {
					pw.println("\tAND (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e\\" + parent_entity_name + "." + attribute_name + "))");
				}
			}
			if (!first_time) {
				
				pw.println("\tTHEN");
					pw.println("\t\tRETURN (e);");
					// perhaps this is the correct one :
//					pw.println("\t\tRETURN (e." + attribute_name + ");");

				pw.println("\tEND_IF;");
			}
//			} else {
//				pw.println("\tRETURN (?);");
//			}
			
		}	else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET data_type in " + constraint);
			printError("\t-- ERROR!!! UNSET data_type : LIST [1:?] OF defined_type");
		}
		pw.println("\tRETURN (?);");
		return null;
	}		
	
		

	Vector handleConstraint_relationship(EConstraint constraint) throws SdaiException {
		Vector result = null;
		/*
			
			A constraint_relationship defines pair of constraints. 
			It does not specify the meaning of relationship nor a direction to go.
		
			element2 
				is the second constraint that is related by this constraint relationship.

		*/
		EConstraint_relationship constraint_relationship = (EConstraint_relationship)constraint;
		// element2 : constraint_select;
		EEntity element2 = null;
		if (constraint_relationship.testElement2(null)) {
			element2 = constraint_relationship.getElement2(null);
			printAttribute("\t-- element2 : constraint_select = " + element2);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET element2 in " + constraint);
			printError("\t-- ERROR!!! UNSET element2 : constraint_select");
		}
		if (constraint instanceof EInstance_constraint) { // ABSTRACT
			result = handleInstance_constraint(constraint, element2);
		} else
		if (constraint instanceof EPath_constraint) {			
			result = handlePath_constraint(constraint, element2);
		}	else {
			SdaiModel map_model = global_map_model;
			String parameter_type_name = getParameterTypeName(constraint, map_model);
			String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
			pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
			// internal error - unknown constraint_relationship
			System.out.println("ERROR: unknown constraint_relationship: " + constraint);
			pw.println("\tRETURN (?);");
		}	
		return result;
	}			

	Vector handleInstance_constraint(EConstraint constraint, EEntity element2) throws SdaiException {
		printDebug("\n-- function implementing instance_constraint");
		/*
		
			An instances_constraint is a consraint_relationship that defined constraints on instances of one type.
		
			element1 
				is the first constraint that is related by this constraint relationship.
		
		*/
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		EInstance_constraint instance_constraint = (EInstance_constraint)constraint;
		// element1 : constraint_select;
		EEntity element1 = null;
		if (instance_constraint.testElement1(null)) {
			element1 = instance_constraint.getElement1(null);
			printAttribute("\t-- element1 : constraint_select = " + element1);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET element1 in " + constraint);
			printError("\t-- ERROR!!! UNSET element1 : constraint_select");
		}
		String function1_name = "f" + element1.getPersistentLabel().substring(1).toLowerCase();
		String function2_name = "f" + element2.getPersistentLabel().substring(1).toLowerCase();
		if (constraint instanceof EAnd_constraint_relationship) {
			printDebug("\n-- function implementing and_constraint_relationship");

			if (element1 instanceof EAttribute) {
				// must be supported (implemented)
				printDebug("\n-- function implementing and_constraint_relationship, element1: attribute");
			} else {
				// must be supported (implemented)
				printDebug("\n-- function implementing and_constraint_relationship, element1: not attribute");
			}
			if (element2 instanceof EAttribute) {
				// does not occur in the current ap210
				printDebug("\n-- function implementing and_constraint_relationship, element2: attribute");
			} else {
				printDebug("\n-- function implementing and_constraint_relationship, element2: not attribute");
			}
			// element1 attribute, element2 - not attribute
			// element1 not attribute, element2 - not attribute


//			pw.println("\tRETURN (" + function1_name + "(e) AND " + function2_name + "(e));");

//			pw.println("\tIF EXISTS(" + function1_name + "(e)) THEN");
//		  pw.println("\t\tRETURN (" + function2_name + "(e));");
			// swapping element1 and element2 functions, needed for attribute_mapping
			pw.println("\tIF EXISTS(" + function2_name + "(e)) THEN");
			if (element1 instanceof EAttribute) {

		pw.println("\t\tIF NOT EXISTS (e) THEN");
			pw.println("\t\t\tRETURN (?);");
		pw.println("\t\tEND_IF;");	




		  	String attribute_name = ((EAttribute)element1).getName(null);
			  String parent_entity_name = ((EAttribute)element1).getParent(null).getName(null).toLowerCase();
		  	pw.println("\t\tRETURN (e\\" + parent_entity_name + "." + attribute_name + ");");
			} else {
		  	pw.println("\t\tRETURN (" + function1_name + "(e));");
			}
		  pw.println("\tELSE;");
		  pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");
		} else
		if (constraint instanceof EInstance_equal) {
			// does not occur in ap210
			printDebug("\n-- function implementing instance_equal");
			/*
				An instance_equal is instance_constraint that requires two constraints to end up with the same instance.
			*/
			pw.println("\tRETURN (" + function1_name + "(e) :=: " + function2_name + "(e));");
		} else
		if (constraint instanceof EOr_constraint_relationship) {
			printDebug("\n-- function implementing or_constraint_relationship");
			/*
				An or_constraint_relationship is an instance_constraint where it is enough, 
				that requirements of at least one constraint are met.
			*/
//			pw.println("\tRETURN (" + function1_name + "(e) OR " + function2_name + "(e));");

			if (element1 instanceof EAttribute) {
				// does not occur in the current ap210
				printDebug("\n-- function implementing or_constraint_relationship, element1: attribute");
			} else {
				// must be supported (implemented)
				printDebug("\n-- function implementing or_constraint_relationship, element1: not attribute");
			}
			if (element2 instanceof EAttribute) {
				// does not occur in the current ap210
				printDebug("\n-- function implementing or_constraint_relationship, element2: attribute");
			} else {
				// must be supported (implemented)
				printDebug("\n-- function implementing or_constraint_relationship, element2: not attribute");
			}

			pw.println("\tLOCAL");
			pw.println("\t\tr : GENERIC;");
			pw.println("\tEND_LOCAL;");
			pw.println("\tr :=" + function2_name + "(e);");
			pw.println("\tIF EXISTS(r) THEN");
		  pw.println("\t\tRETURN (r);");
			pw.println("\tEND_IF;");
		  pw.println("\tRETURN (" + function1_name + "(e));");

		}	else {
			// internal error - unknown instance_constraint
			System.out.println("ERROR: unknown instance_constraint: " + constraint);
			pw.println("\tRETURN (?);");
		}				
		// generateFxxFunction(element1, map_model);
		// generateFxxFunction(element2, map_model);
		Vector result = new Vector();
		result.addElement(element1);
		result.addElement(element2);
		return result;
	}				

	Vector gHandlePath_constraint(EPath_constraint path_constraint, SdaiModel map_model) throws SdaiException {
		printDebug("\n-- function implementing path_constraint");
		
		Vector result = new Vector();
		pw.println("\n-- " + path_constraint);
 		/*
				A path_constraint is a constraint_relationship that does following: 
				requires to make a step using element1 and meet requirements of element2. 
				element1 shall be a constraint on an attribute of an entity-type. 
				element2 defines a constraint on the entity reached by following element1.
				
				element1 
					is an attribute or other element that allows to make step from one entity instance to instance of other entity.

				
				element_1 - path_constraint_select -- for making a step
				possible types:
					aggregate_member_constraint
	 				inverse_attribute_constraint
	 				entity_constraint
	 				intersection_constraint
	 				attribute
				present in ap210 (at least these):
					entity_constraint
					explicit_attribute
					inverse_attribute_constraint
		
				element2 -- constraint_select -- must meet its requiremens after making a step indicated by element1
		*/

		// element1 : path_constraint_select;
		EEntity element1 = null;
		if (path_constraint.testElement1(null)) {
			element1 = path_constraint.getElement1(null);
			printAttribute("\t-- element1 : path_constraint_select = " + element1);
			// do not generate function for the element1 constraint separately
   
   		if (flag_path_constraint_suppresses) {
    		element1.setTemp(path_constraint);
			}
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET element1 in " + path_constraint);
			printError("\t-- ERROR!!! UNSET element1 : path_constraint_select");
		}

		EEntity element2 = null;
		String element2_function_name = "_UNKNOWN__element2_of_path_constraint_unset";
		if (path_constraint.testElement2(null)) {
			element2 = path_constraint.getElement2(null);
			printAttribute("\t-- element2 : constraint_select = " + element2);
			element2_function_name = "f" + element2.getPersistentLabel().substring(1).toLowerCase();
// System.out.println("X01: " + element2_function_name);
			result.addElement(element2);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET element2 in " + path_constraint);
			printError("\t-- ERROR!!! UNSET element2 : constraint_select");
		}

		String parameter_type_name = getParameterTypeName(path_constraint, map_model);
//		String return_type_name = getReturnTypeName(path_constraint, map_model);
		String return_type_name = "GENERIC";
		String function_name = "f" + path_constraint.getPersistentLabel().substring(1).toLowerCase();
//		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : BAG OF " + return_type_name + ";");
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : " + return_type_name + ";");
//		pw.println("\tLOCAL");      
//		pw.println("\t\tresult : BAG OF GENERIC := [];");


		// temporarily here, for debugging what happens in ap210 
		// element1 : path_constraint_select
	  if (element1 instanceof EAggregate_member_constraint) { 
			// does occur in ap210 (fully implemented all the sub-cases)
			printDebug("\n-- WOW! implementing path_constraint, element1: aggregate_member_constraint");
		} else
	  if (element1 instanceof EInverse_attribute_constraint) { 
			// does occur in ap210 (fully implemented all the sub-cases)
			printDebug("\n-- WOW! implementing path_constraint, element1: inverse_attribute_constraint");
		} else
	  if (element1 instanceof EEntity_constraint) { 
			// does occur in ap210 (fully implemented all the sub-cases)
			printDebug("\n-- WOW! implementing path_constraint, element1: entity_constraint");
		} else
	  if (element1 instanceof EIntersection_constraint) { 
			// does occur in ap210 (fully implemented all the sub-cases)
			printDebug("\n-- WOW! implementing path_constraint, element1: intersection_constraint");
		} else
	  if (element1 instanceof EAttribute) { 
	  	// does occur in ap210 (fully implemented)
			printDebug("\n-- WOW! implementing path_constraint, element1: attribute");
		} else {
			// does not occur in the current ap210
			printDebug("\n-- WOW! implementing path_constraint, element1: impossible type");
		}







		// ################################################################################################## element 1 ###################
	  if (element1 instanceof EAggregate_member_constraint) { 
			// must be supported, occurs in ap210
			printDebug("-- path_constraint, element1: aggregate_member_constraint");

	  	

	  	
	  	
/* 
    
    if member is set to n, get member aggr[n] and invoke for it fxxx for element2
    
	  if member unset,  make a loop through all the members of the aggregate, 
	  and for every one of them invoke  fxxx for element2 and if the constraint is met,
	  add to the result aggregate

	-- if attribute = attribute, may be a further constraint
	aggr : get_aggregate_type_or_generic := e.attribute_name; 

	REPEAT i:=1 TO SIZEOF(aggr);
		r := f100325(aggr[i]);
		IF EXISTS(r) THEN
			result := result + r;
		END_IF;
	END_REPEAT;
		


*/


//	  	String aggregate_type_name = "_UNKNOWN__aggregate_type_name";
	  	String aggregate_type_name = "BAG OF GENERIC";
	  	String attribute_name = "_UNKNOWN__attribute_name";
	  	String parent_entity_name = "_unknown_parent_";
	  	int member = Integer.MIN_VALUE;
	  	EEntity attribute = null;
	  	if (((EAggregate_member_constraint)element1).testMember(null)) {
	  		member = ((EAggregate_member_constraint)element1).getMember(null);
				printDebug("\t-- member is set: " + member);      
	  	} else {
				printDebug("\t-- optional member is not set");      
	  	}
	  	if (((EAggregate_member_constraint)element1).testAttribute(null)) {
	  		attribute = ((EAggregate_member_constraint)element1).getAttribute(null);
				printDebug("\t-- attribute = " + attribute);      

				// tmp testing
	  		// attribute: aggregate_member_constraint_select
  			if (attribute instanceof EAttribute) {
					// must be supported (is supported)
					printDebug("-- path_constraint, element1: aggregate_member_constraint, attribute: attribute");
				} else
  			if (attribute instanceof EInverse_attribute_constraint) {
					// does not occur in the current ap210
					printDebug("-- path_constraint, element1: aggregate_member_constraint, attribute: inverse_attribute_constraint");
				} else
  			if (attribute instanceof EAggregate_member_constraint) {
					// does not occur in the current ap210
					printDebug("-- path_constraint, element1: aggregate_member_constraint, attribute: aggregate_member_constraint");
				} else
  			if (attribute instanceof EEntity_constraint) {
					// does not occur in the current ap210
					printDebug("-- path_constraint, element1: aggregate_member_constraint, attribute: entity_constraint");
				} else
  			if (attribute instanceof ESelect_constraint) {	
					// must be supported (is supported)
					printDebug("-- path_constraint, element1: aggregate_member_constraint, attribute: select_constraint");
				} else {
					// does not occur in the current ap210
					printDebug("-- path_constraint, element1: aggregate_member_constraint, attribute: impossible type");
				}
			
			
			
			
			
			
			
	  		if (attribute instanceof EAttribute) {
					printDebug("\t-- implementing path_constraint+aggregate_member_constraint attribute = (explicit)attribute");      
	  			attribute_name = ((EAttribute)attribute).getName(null).toLowerCase();
				  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
	  			EEntity_definition attribute_parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
					printDebug("\t-- attribute parent: " + attribute_parent_entity);      
	  			
	  			EEntity attribute_domain = null;
	  			if (attribute instanceof EExplicit_attribute) {
	  				attribute_domain = ((EExplicit_attribute)attribute).getDomain(null);
						printDebug("\t-- attribute domain: " + attribute_domain);      
	  				if (attribute_domain instanceof EAggregation_type) {
	  					aggregate_type_name = getAggregateTypeName((EAggregation_type)attribute_domain);
	  				}
	  			}
			
			
			
			
					if (member == Integer.MIN_VALUE) {
								
						pw.println("\tLOCAL");      
						pw.println("\t\tresult : BAG OF GENERIC;");
						pw.println("\t\taggr : " + aggregate_type_name + ";");
						pw.println("\t\tr : GENERIC;");
						pw.println("\tEND_LOCAL;");      

						if (!(attribute_name.equals("_UNKNOWN__attribute_name"))) {

							pw.println("\tIF NOT EXISTS (e) THEN");
							pw.println("\t\tRETURN (?);");
							pw.println("\tEND_IF;");	
					
							pw.println("\taggr := e\\" + parent_entity_name + "." + attribute_name + ";");

							pw.println("\tREPEAT i:=1 TO SIZEOF(aggr);");
							
							if (element2 instanceof EAttribute) {
//								pw.println("\t\tr := aggr[i]." + ((EAttribute)element2).getName(null) + ";");
								pw.println("\t\tr := aggr[i]\\" + ((EAttribute)element2).getParent(null).getName(null).toLowerCase()  + "." + ((EAttribute)element2).getName(null) + ";");
							} else {
								pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
							}
							pw.println("\t\tIF EXISTS(r) THEN");
							pw.println("\t\t\tIF NOT EXISTS(result) THEN");
							pw.println("\t\t\t\tresult := [];");
							pw.println("\t\t\tEND_IF;");
							pw.println("\t\t\tresult := result + r;");
							pw.println("\t\tEND_IF;");
							pw.println("\tEND_REPEAT;");
			
							pw.println("\tIF SIZEOF(result) = 1 THEN");
							pw.println("\t\tRETURN (result[1]);");      
							pw.println("\tELSE");
							pw.println("\t\tRETURN (result);");      
							pw.println("\tEND_IF;");
			
			 			} else {
			   			pw.println("\tRETURN (?);");
			 			}
			
					} else {

						pw.println("\tLOCAL");      
						pw.println("\t\taggr : " + aggregate_type_name + ";");
						pw.println("\tEND_LOCAL;");      

						if (!(attribute_name.equals("_UNKNOWN__attribute_name"))) {
							pw.println("\tIF NOT EXISTS (e) THEN");
							pw.println("\t\tRETURN (?);");
							pw.println("\tEND_IF;");	
							pw.println("\taggr := e\\" + parent_entity_name + "." + attribute_name + ";");
		
		
		
							if (element2 instanceof EAttribute) {
								pw.println("\t\tRETURN (aggr[" + member + "]\\" +  ((EAttribute)element2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)element2).getName(null) + ");");
							} else {
								pw.println("\t\tRETURN (" + element2_function_name + "(aggr[" + member + "]));");
							}

						} else {
							printDebug("\t\t-- unknown attribute name, to be implemented: aggregate := e.attribute_name");
							pw.println("\tRETURN (?);");
						}

					}
	  		
	  		} else
	  		if (attribute instanceof EInverse_attribute_constraint) {
					// does not occur in the current ap210
					printDebug("\t-- To Be Done: path_constraint+aggregate_member_constraint attribute = inverse_attribute_constraint");      
	  		} else 
	  		if (attribute instanceof EAggregate_member_constraint) {
					// does not occur in the current ap210
					printDebug("\t-- To Be Done: path_constraint+aggregate_member_constraint attribute = aggregate_member_constraint");      
	  		} else
	  		if (attribute instanceof EEntity_constraint) {
					// does not occur in the current ap210
					printDebug("\t-- To Be Done: path_constraint+aggregate_member_constraint attribute = entity_constraint");      
	  		} else 
	  		if (attribute instanceof ESelect_constraint) {
					// must be implemented
					printDebug("\t-- path_constraint+aggregate_member_constraint attribute =  select_constraint");      
	  		

					ESelect_constraint select_constraint = (ESelect_constraint)attribute;
			// attribute : select_constraint_select;
					EEntity attribute2 = null;
					// String aggregate_type_name = "_UNKNOWN_AGGREGATE_TYPE_NAME_";
					String attribute2_name = "_UNKNOWN_ATTRIBUTE_NAME_";
					String parent_entity2_name = "_unknown_parent_";
					if (select_constraint.testAttribute(null)) {
						attribute2 = select_constraint.getAttribute(null);
						printAttribute("\t-- attribute : select_constraint_select = " + attribute2);
						if (attribute2 instanceof EAttribute) {
							attribute2_name = ((EAttribute)attribute2).getName(null).toLowerCase();
						  parent_entity2_name = ((EAttribute)attribute2).getParent(null).getName(null).toLowerCase();
//			  			attribute_name = attribute2_name;
			  			if (attribute2 instanceof EExplicit_attribute) {
	  						EEntity attribute2_domain = ((EExplicit_attribute)attribute2).getDomain(null);
								printDebug("\t-- attribute domain: " + attribute2_domain);      
	  						if (attribute2_domain instanceof EAggregation_type) {
	  							aggregate_type_name = getAggregateTypeName((EAggregation_type)attribute2_domain);
	  						} else 
	  						if (attribute2_domain instanceof ENamed_type) {
	  							aggregate_type_name = ((ENamed_type)attribute2_domain).getName(null).toLowerCase();
	  						}
							}
						}
					} else {
						// ERROR - not OPTIONAL - must be set
						System.out.println("ERROR: UNSET attribute in " + attribute);
						printError("\t-- ERROR!!! UNSET attribute : select_constraint_select");
					} 




					if (!(attribute2_name.equals("_UNKNOWN__attribute_name"))) {

						// data_type : LIST [1:?] OF defined_type;
						ADefined_type data_type = null;
						if (select_constraint.testData_type(null)) {
							data_type = select_constraint.getData_type(null);
							printAttribute("\t-- data_type : LIST [1:?] OF defined_type = " + data_type);
							SdaiIterator itdt = data_type.createIterator();
							boolean first_time = true;
							
							pw.println("\tLOCAL");      
							if (member == Integer.MIN_VALUE) {
								pw.println("\t\tresult : BAG OF GENERIC;");
								pw.println("\t\tr : GENERIC;");
							}	
							pw.println("\t\taggr : " + aggregate_type_name + ";");
							pw.println("\tEND_LOCAL;");      

							pw.println("\tIF NOT EXISTS (e) THEN");
							pw.println("\t\tRETURN (?);");
							pw.println("\tEND_IF;");	
		
							if (!(attribute2_name.equals("_UNKNOWN__attribute_name"))) {

								pw.println("\taggr := e\\" + parent_entity2_name + "." + attribute2_name + ";");

								pw.println("\tIF NOT EXISTS (aggr) THEN");
								pw.println("\t\tRETURN (?);");
								pw.println("\tEND_IF;");	
	
								while(itdt.next()) {
									EDefined_type dt = (EDefined_type)data_type.getCurrentMember(itdt);
									printDebug("-- select path element: " + dt.getName(null));
									if (first_time) {
										first_time = false;
//										pw.println("\tIF (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e." + attribute2_name + "))");
										pw.println("\tIF (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (aggr))");
									} else {
//										pw.println("\tAND (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (e." + attribute2_name + "))");
										pw.println("\tAND (\'" + AIM_schema_name + "." + dt.getName(null).toUpperCase() + "\' IN TYPEOF (aggr))");
									}
								}
								if (!first_time) {
				
									pw.println("\tTHEN");


									if (member == Integer.MIN_VALUE) {

										pw.println("\t\tREPEAT i:=1 TO SIZEOF(aggr);");
							
										if (element2 instanceof EAttribute) {
//											pw.println("\t\t\tr := aggr[i]." + ((EAttribute)element2).getName(null) + ";");
											pw.println("\t\t\tr := aggr[i]\\" + ((EAttribute)element2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)element2).getName(null) + ";");
										} else {
											pw.println("\t\t\tr := " + element2_function_name + "(aggr[i]);");
										}
										pw.println("\t\t\tIF EXISTS(r) THEN");
										pw.println("\t\t\t\tIF NOT EXISTS(result) THEN");
										pw.println("\t\t\t\t\tresult := [];");
										pw.println("\t\t\t\tEND_IF;");
										pw.println("\t\t\t\tresult := result + r;");
										pw.println("\t\t\tEND_IF;");
										pw.println("\t\tEND_REPEAT;");
			
										pw.println("\t\tIF SIZEOF(result) = 1 THEN");
										pw.println("\t\t\tRETURN (result[1]);");      
										pw.println("\t\tELSE");
										pw.println("\t\t\tRETURN (result);");      
										pw.println("\t\tEND_IF;");

									} else {



										if (element2 instanceof EAttribute) {
											pw.println("\t\tRETURN (aggr[" + member + "]\\" + ((EAttribute)element2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)element2).getName(null) + ");");
										} else {
											pw.println("\t\tRETURN (" + element2_function_name + "(aggr[" + member + "]));");
										}

									}
									pw.println("\tELSE");
									pw.println("\t\tRETURN (?);");
									pw.println("\tEND_IF;");
								
								} else { // no select path
									pw.println("\tRETURN (?);");
								}

			
			 				} else { // _UKNOWN_ATTRIBUTE_NAME_
			   				pw.println("\tRETURN (?);");
			 				}
			
						}	else {
							// ERROR - not OPTIONAL - must be set
							System.out.println("ERROR: UNSET data_type in " + attribute);
							printError("\t-- ERROR!!! UNSET data_type : LIST [1:?] OF defined_type");
							pw.println("\tRETURN (?);");
						}

					} else {
						printDebug("\t-- incomplete implementation or corrupt data");
						pw.println("\tRETURN (?);");
					}
	  		
	  		} else {
	  			// must be one of the above
					// does not occur in the current ap210
					pw.println("\t-- ERROR: incorrect type - path_constraint+aggregate_member_constraint attribute: " + attribute);      
	  		}
	  	} else {
	  		// error, non-optional
					// does not occur in the current ap210
				pw.println("\t-- ERROR: attribute unset !!! - path_constraint+aggregate_member_constraint");      
	  	}
	  	
	  	
	  	

//			pw.println("\tRETURN (result);");      
	
	  } else
	  if (element1 instanceof EInverse_attribute_constraint) {
	
			String inverted_attribute_name = "UNKNOWN__inverted_attribute_name";
			String inverted_attribute_parent_entity_name = "UNKNOWN_inverted_attribute_parent_entity_name";
			String inverted_attribute_parent_entity_schema_name = "x";
			EEntity inverted_attribute = ((EInverse_attribute_constraint)element1).getInverted_attribute(null);
			
			// temp for debugging
			// inverted_attribute: inverse_attribute_constraint_select
			if (inverted_attribute instanceof EAttribute) {
				// must be supported (is supported)
				printDebug("\t-- implementing path_constraint, element1: inverse_attribute_constraint, inverted_attribute: attribute");      
			} else
			if (inverted_attribute instanceof EEntity_constraint) {
				// does not occur in the current ap210
				printDebug("\t-- implementing path_constraint, element1: inverse_attribute_constraint, inverted_attribute: entity_constraint");      
			} else
			if (inverted_attribute instanceof EAggregate_member_constraint) {
				// must be supported (is supported)
				printDebug("\t-- implementing path_constraint, element1: inverse_attribute_constraint, inverted_attribute: aggregate_member_constraint");      
			} else
			if (inverted_attribute instanceof ESelect_constraint) {
				// does not occur in the current ap210
				printDebug("\t-- implementing path_constraint, element1: inverse_attribute_constraint, inverted_attribute: select_constraint");      
			} else {
				// does not occur in the current ap210
				printDebug("\t-- implementing path_constraint, element1: inverse_attribute_constraint, inverted_attribute: impossible type");      
			}

			
			
			if (inverted_attribute instanceof EAttribute) {
				inverted_attribute_name = ((EAttribute)inverted_attribute).getName(null).toLowerCase();
				inverted_attribute_parent_entity_name = ((EAttribute)inverted_attribute).getParent(null).getName(null).toLowerCase();
				String inverted_attribute_parent_entity_model_name = ((EAttribute)inverted_attribute).getParent(null).findEntityInstanceSdaiModel().getName();	
	      inverted_attribute_parent_entity_schema_name = inverted_attribute_parent_entity_model_name.substring(0, inverted_attribute_parent_entity_model_name.length() - 16).toLowerCase();

			} else
			if (inverted_attribute instanceof EEntity_constraint) {
				pw.println("-- ERROR: path_constraint + inverse_attribute_constraint - inverted_attribute - entity_constraint - not yet implemented");
			} else
			if (inverted_attribute instanceof EAggregate_member_constraint) {
//				pw.println("-- ERROR: path_constraint + inverse_attribute_constraint - inverted_attribute = aggregate_member_constraint - not yet implemented");


				EAggregate_member_constraint aggregate_member_constraint = (EAggregate_member_constraint)inverted_attribute;
		  	int member = Integer.MIN_VALUE;
	  		EEntity attribute = null;
		  	if (aggregate_member_constraint.testMember(null)) {
	  			member = aggregate_member_constraint.getMember(null);
//					printDebug("\t-- inverted_attribute = aggregate_member_constraint, its member is set: " + member);      
	  		} else {
//					printDebug("\t-- inverted_attribute = aggregate_member_constraint, its optional member is not set");      
	  		}
	  		if (aggregate_member_constraint.testAttribute(null)) {
	  			attribute = aggregate_member_constraint.getAttribute(null);
//					printDebug("\t-- inverted_attribute = aggregate_member_constraint, its attribute = " + attribute);      


	  			if (attribute instanceof EAttribute) {
	  				String attribute_name = ((EAttribute)attribute).getName(null).toLowerCase();
					  String parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
	  				inverted_attribute_name = attribute_name;
	  				EEntity_definition attribute_parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
						inverted_attribute_parent_entity_name = attribute_parent_entity.getName(null).toLowerCase();
						String inverted_attribute_parent_entity_model_name = attribute_parent_entity.findEntityInstanceSdaiModel().getName();	
	      		inverted_attribute_parent_entity_schema_name = inverted_attribute_parent_entity_model_name.substring(0, inverted_attribute_parent_entity_model_name.length() - 16).toLowerCase();
//						printDebug("\t-- inverted_attribute = aggregate_member_constraint, its attribute parent: " + attribute_parent_entity);      
	  			
	  				EEntity attribute_domain = null;
	  				if (attribute instanceof EExplicit_attribute) {
	  					attribute_domain = ((EExplicit_attribute)attribute).getDomain(null);
// printDebug("\t-- ROCOFOCO ");      
//							printDebug("\t-- inverted_attribute = aggregate_member_constraint, its attribute domain: " + attribute_domain);      
	  					if (attribute_domain instanceof EAggregation_type) {
	  						String aggregate_type_name = getAggregateTypeName((EAggregation_type)attribute_domain);
	  					}
	  				}
	  			} else // attribute is not attribute
	  			if (attribute instanceof EInverse_attribute_constraint) {
						printDebug("\t-- To Be Done: path_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute = inverse_attribute_constraint");      
	  			} else 
	  			if (attribute instanceof EAggregate_member_constraint) {
						printDebug("\t-- To Be Done: path_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute = aggregate_member_constraint");      
	  			} else
	  			if (attribute instanceof EEntity_constraint) {
						printDebug("\t-- To Be Done: path_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute = entity_constraint");      
	  			} else 
	  			if (attribute instanceof ESelect_constraint) {
						printDebug("\t-- To Be Done: path_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute =  select_constraint");      
	  			} else {
	  				// must be one of the above
						pw.println("\t-- ERROR: incorrect type - path_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute: " + attribute);      
	  			}
	  		} else {
	  			// error, non-optional
					pw.println("\t-- ERROR: attribute unset !!! - path_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint");      
	  		}






			} else
			if (inverted_attribute instanceof ESelect_constraint) {
				pw.println("-- ERROR: path_constraint + inverse_attribute_constraint - inverted_attribute - select_constraint - not yet implemented");
			} else {
				// all possible types already above 
			}
			
			
			String bag_member_type = "GENERIC";
			if (element2 instanceof EAttribute) {
				bag_member_type = ((EAttribute)element2).getParent(null).getName(null).toLowerCase();  	
				
			}
			
			pw.println("\tLOCAL");      
			pw.println("\t\tresult : BAG OF GENERIC;");
			pw.println("\t\tr : GENERIC;");
//			pw.println("\t\taggr : BAG OF GENERIC;");
			
			// bag of element2 parent
//			pw.println("\t\taggr : BAG OF GENERIC := USEDIN(e, \'" + inverted_attribute_parent_entity_schema_name.toUpperCase() + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
//			pw.println("\t\taggr : BAG OF " + bag_member_type + " := USEDIN(e, \'" + inverted_attribute_parent_entity_schema_name.toUpperCase() + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
			pw.println("\t\taggr : BAG OF " + bag_member_type + " := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
			pw.println("\tEND_LOCAL;");      
			pw.println("\tREPEAT i:=1 TO SIZEOF(aggr);");




			if (element2 instanceof EAttribute) {
//				printDebug("\t\t-- r := e." + ((EAttribute)element2).getName(null) + ";");
//				pw.println("\t\tr := aggr[i]." + ((EAttribute)element2).getName(null) + ";");
				pw.println("\t\tr := aggr[i]\\" + ((EAttribute)element2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)element2).getName(null) + ";");
			} else {
				pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
			}

//			pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
			pw.println("\t\tIF EXISTS(r) THEN");
			pw.println("\t\t\tIF NOT EXISTS(result) THEN");
			pw.println("\t\t\t\tresult := [];");
			pw.println("\t\t\tEND_IF;");
			pw.println("\t\t\tresult := result + r;");
			pw.println("\t\tEND_IF;");
			pw.println("\tEND_REPEAT;");
			pw.println("\tIF SIZEOF(result) = 1 THEN");
			pw.println("\t\tRETURN (result[1]);");      
			pw.println("\tELSE");
			pw.println("\t\tRETURN (result);");      
			pw.println("\tEND_IF;");


	  } else
	  if (element1 instanceof EEntity_constraint) {

			EEntity_constraint entity_constraint = (EEntity_constraint)element1;
//			String domain_name = "_UNKNOWN__domain";
			String domain_name = "GENERIC";
			String domain_schema_name = AIM_schema_name;
			String attribute_name = "_UNKNOWN__attribute";
			String parent_entity_name = "_unknown_parent_";
			// attribute_name - extract from inside of aggregate_member_constraint (if attribute = attribute) for now
			



//			String attribute_domain_name = "_UNKNOWN__attribute_domain";
			// for example, SELECT

			String attribute_domain_name = "GENERIC";
			// attribute_domain_name - element 2 (if attribute) parent entity name
			if (element2 instanceof EAttribute) {
				attribute_domain_name = ((EAttribute)element2).getParent(null).getName(null).toLowerCase();
			}
			
			// domain : entity_definition;
			EEntity_definition domain = null;
			if (entity_constraint.testDomain(null)) {
				domain = entity_constraint.getDomain(null);
				printAttribute("\t-- domain : entity_definition = " + domain);
				String domain_model_name = domain.findEntityInstanceSdaiModel().getName();	
	      domain_schema_name = domain_model_name.substring(0, domain_model_name.length() - 16).toUpperCase();
				if (domain_schema_name.equals("MIXED_COMPLEX_TYPES")) {
					domain_schema_name = AIM_schema_name;
				}
				
				domain_name = domain.getName(null).toUpperCase();
			} else {
				// ERROR - not OPTIONAL - must be set
				System.out.println("ERROR: UNSET domain in " + entity_constraint);
				printError("\t-- ERROR!!! UNSET domain : entity_definition");
			}
			// attribute : attribute_select;
			EEntity attribute = null;
//			String ee_name = "_NOT_IMPLEMENTED_";
			String ee_name = "GENERIC";
			if (entity_constraint.testAttribute(null)) {
				attribute = entity_constraint.getAttribute(null);
			
				// temp, testing
				// attribute: attribute_select
				if (attribute instanceof EAttribute) {
					// must be supported (is supported)
					printDebug("\t -- implementing path_constraint, element1: entity_constraint, attribute: attribute");
				} else
				if (attribute instanceof EInverse_attribute_constraint) {
					// must be supported (is supported)
					printDebug("\t -- implementing path_constraint, element1: entity_constraint, attribute: inverse_attribute_constraint");
				} else
				if (attribute instanceof EAggregate_member_constraint) {
					// must be supported (is supported)
					printDebug("\t -- implementing path_constraint, element1: entity_constraint, attribute: aggregate_member_constraint");
				} else {
					// does not occur in the current ap210
					printDebug("\t -- implementing path_constraint, element1: entity_constraint, attribute: impossible type");
				}
								


				
				printAttribute("\t-- attribute : attribute_select = " + attribute);
				if (attribute instanceof EExplicit_attribute) {
					attribute_name = ((EExplicit_attribute)attribute).getName(null).toLowerCase();
				  parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
					EEntity attribute_domain = ((EExplicit_attribute)attribute).getDomain(null);
					if (attribute_domain instanceof EEntity_definition) {
						attribute_domain_name = ((EEntity_definition)attribute_domain).getName(null).toLowerCase();
					} else {
						// for example SELECT, leaving GENERIC
					}
//					ee_name = "e." + ((EAttribute)attribute).getName(null).toLowerCase();	
					ee_name = "e\\" + ((EAttribute)attribute).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)attribute).getName(null).toLowerCase();	
				} else 

				if (attribute instanceof EAggregate_member_constraint) {

			  	int member = Integer.MIN_VALUE;
					EEntity attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
			  	if (((EAggregate_member_constraint)attribute).testMember(null)) {
	  				member = ((EAggregate_member_constraint)attribute).getMember(null);
						// printDebug("\t-- member is set: " + member);      
	  			} else {
						// printDebug("\t-- optional member is not set");      
	  			}
					String element_type_string = "GENERIC";
					if (attribute2 instanceof EAttribute) {
						attribute_name = ((EAttribute)attribute2).getName(null).toLowerCase();
					  parent_entity_name = ((EAttribute)attribute2).getParent(null).getName(null).toLowerCase();
//						printDebug("-- may not be correct solution");						
			  		// parent_entity = ((EAttribute)attribute2).getParent(null);
			  		// result = parent_entity.getName(null).toLowerCase();
//						ee_name = "e." + ((EAttribute)attribute2).getName(null).toLowerCase() + "[1]";	
						ee_name = "e\\" + ((EAttribute)attribute2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)attribute2).getName(null).toLowerCase() + "[1]";	
						EEntity attr_domain = null;
						if (attribute2 instanceof EExplicit_attribute) {
							attr_domain = ((EExplicit_attribute)attribute2).getDomain(null);
						} else 
						if (attribute2 instanceof EDerived_attribute) {
							attr_domain = ((EDerived_attribute)attribute2).getDomain(null);
						} 
						if (attr_domain != null) {
							// pw.println("\t=== domain: " + attr_domain);      
							if (attr_domain instanceof EAggregation_type) {
								EData_type element_type = ((EAggregation_type)attr_domain).getElement_type(null);
								// entity_definition and defined_type could be put into one named_type
								if (element_type instanceof EEntity_definition) {
									// pw.println("\t=== element_type: " + element_type);      
									element_type_string = ((EEntity_definition)element_type).getName(null).toLowerCase();
								} else
								if (element_type instanceof EDefined_type) {
									element_type_string = ((EDefined_type)element_type).getName(null).toLowerCase();
								} else {
									// does not happen in ap210
									printDebug("-- not implemented - 01: " + element_type);      
								
								}
							} else {
									// does not happen in ap210
								printDebug("-- not implemented - 02: " + attr_domain);      
								
							}
							
						} else {
									// does not happen in ap210
								printDebug("-- not implemented - 03: " + attribute2);      
						}
				
					}
					// perhaps directly here

					pw.println("\tLOCAL");      
					pw.println("\t\tresult : BAG OF GENERIC;");
					pw.println("\t\tr : GENERIC;");
					//  aggr : SET OF representation_item;	
					pw.println("\t\taggr : SET OF " + element_type_string + ";"); 
					pw.println("\tEND_LOCAL;");      


					pw.println("\tIF NOT EXISTS (e) THEN");
						pw.println("\t\tRETURN (?);");
					pw.println("\tEND_IF;");	

	

					pw.println("\taggr := e\\" + parent_entity_name + "." + attribute_name + ";");


 // if member unset
					if (member == Integer.MIN_VALUE) {

						pw.println("\tREPEAT i:=1 TO SIZEOF(aggr);");

//		IF 'ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN.LENGTH_MEASURE_WITH_UNIT+MEASURE_REPRESENTATION_ITEM' IN TYPEOF (e.items[1]) THEN
//			r := f124735(e.items[1]);
//		END_IF;




//			pw.println("\t\tIF \'" +    AIM_schema_name + "." + domain_name + "\' IN TYPEOF (" + ee_name + ") THEN");
			pw.println("\t\tIF \'" +    AIM_schema_name + "." + domain_name + "\' IN TYPEOF (aggr[i]) THEN");



			if (element2 instanceof EAttribute) {
//				pw.println("\t\t\tr := aggr[i]." + ((EAttribute)element2).getName(null) + ";");
				pw.println("\t\t\tr := aggr[i]\\" + ((EAttribute)element2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)element2).getName(null) + ";");
//				pw.println("\t\t\tr := " + ee_name + "." + ((EAttribute)element2).getName(null) + ";");

			} else {

				pw.println("\t\t\tr := " + element2_function_name + "(aggr[i]);");

//				pw.println("\t\t\tr :=  " + element2_function_name + "(" + ee_name + ");");



			}










//			pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
			
						pw.println("\t\t\tIF EXISTS(r) THEN");
						pw.println("\t\t\t\tIF NOT EXISTS(result) THEN");
						pw.println("\t\t\t\t\tresult := [];");
						pw.println("\t\t\t\tEND_IF;");
						pw.println("\t\t\t\tresult := result + r;");
						pw.println("\t\t\tEND_IF;");
			pw.println("\t\tEND_IF;");

						pw.println("\tEND_REPEAT;");
						pw.println("\tIF SIZEOF(result) = 1 THEN");
						pw.println("\t\tRETURN (result[1]);");      
						pw.println("\tELSE");
						pw.println("\t\tRETURN (result);");      
						pw.println("\tEND_IF;");
					
					} else {  // member is set, no loop, just this one member
					}
					

				
				
				} else 						
				if (attribute instanceof EInverse_attribute_constraint) {
//						printDebug("-- not implemented - attribute = inverse_attribute_constraint");						
				
// ###################################


			String inverted_attribute_name = "UNKNOWN__inverted_attribute_name";
			String inverted_attribute_parent_entity_name = "UNKNOWN_inverted_attribute_parent_entity_name";
			String inverted_attribute_parent_entity_schema_name = "x";
			EEntity inverted_attribute = ((EInverse_attribute_constraint)attribute).getInverted_attribute(null);
			printDebug("-- in inverse_attribute_constraint - inverted_attribute  = " + inverted_attribute);						
			
			if (inverted_attribute instanceof EAttribute) {
				inverted_attribute_name = ((EAttribute)inverted_attribute).getName(null).toLowerCase();
				inverted_attribute_parent_entity_name = ((EAttribute)inverted_attribute).getParent(null).getName(null).toLowerCase();
				String inverted_attribute_parent_entity_model_name = ((EAttribute)inverted_attribute).getParent(null).findEntityInstanceSdaiModel().getName();	
	      inverted_attribute_parent_entity_schema_name = inverted_attribute_parent_entity_model_name.substring(0, inverted_attribute_parent_entity_model_name.length() - 16).toLowerCase();

			} else
			if (inverted_attribute instanceof EEntity_constraint) {
				pw.println("-- ERROR: path_constraint + entity_constraint + inverse_attribute_constraint - inverted_attribute - entity_constraint - not yet implemented");
			} else
			if (inverted_attribute instanceof EAggregate_member_constraint) {
//				pw.println("-- ERROR: path_constraint + inverse_attribute_constraint - inverted_attribute = aggregate_member_constraint - not yet implemented");


				EAggregate_member_constraint aggregate_member_constraint = (EAggregate_member_constraint)inverted_attribute;
		  	int member = Integer.MIN_VALUE;
	  		EEntity attribute2 = null;
		  	if (aggregate_member_constraint.testMember(null)) {
	  			member = aggregate_member_constraint.getMember(null);
//					printDebug("\t-- inverted_attribute = aggregate_member_constraint, its member is set: " + member);      
	  		} else {
//					printDebug("\t-- inverted_attribute = aggregate_member_constraint, its optional member is not set");      
	  		}
	  		if (aggregate_member_constraint.testAttribute(null)) {
	  			attribute2 = aggregate_member_constraint.getAttribute(null);
//					printDebug("\t-- inverted_attribute = aggregate_member_constraint, its attribute = " + attribute);      


	  			if (attribute2 instanceof EAttribute) {
	  				attribute_name = ((EAttribute)attribute2).getName(null).toLowerCase();
					  parent_entity_name = ((EAttribute)attribute2).getParent(null).getName(null).toLowerCase();
	  				inverted_attribute_name = attribute_name;
	  				EEntity_definition attribute_parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
						inverted_attribute_parent_entity_name = attribute_parent_entity.getName(null).toLowerCase();
						String inverted_attribute_parent_entity_model_name = attribute_parent_entity.findEntityInstanceSdaiModel().getName();	
	      		inverted_attribute_parent_entity_schema_name = inverted_attribute_parent_entity_model_name.substring(0, inverted_attribute_parent_entity_model_name.length() - 16).toLowerCase();
//						printDebug("\t-- inverted_attribute = aggregate_member_constraint, its attribute parent: " + attribute_parent_entity);      
	  			
	  				EEntity attribute_domain = null;
	  				if (attribute2 instanceof EExplicit_attribute) {
	  					attribute_domain = ((EExplicit_attribute)attribute2).getDomain(null);
// printDebug("\t-- ROCOFOCO ");      
//							printDebug("\t-- inverted_attribute = aggregate_member_constraint, its attribute domain: " + attribute_domain);      
	  					if (attribute_domain instanceof EAggregation_type) {
	  						String aggregate_type_name = getAggregateTypeName((EAggregation_type)attribute_domain);
	  					}
	  				}
	  			} else // attribute2 is not attribute
	  			if (attribute instanceof EInverse_attribute_constraint) {
						printDebug("\t-- To Be Done: path_constraint+entity_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute = inverse_attribute_constraint");      
	  			} else 
	  			if (attribute instanceof EAggregate_member_constraint) {
						printDebug("\t-- To Be Done: path_constraint+entity_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute = aggregate_member_constraint");      
	  			} else
	  			if (attribute instanceof EEntity_constraint) {
						printDebug("\t-- To Be Done: path_constraint+entity_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute = entity_constraint");      
	  			} else 
	  			if (attribute instanceof ESelect_constraint) {
						printDebug("\t-- To Be Done: path_constraint+entity_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute =  select_constraint");      
	  			} else {
	  				// must be one of the above
						pw.println("\t-- ERROR: incorrect type - path_constraint+entity_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint attribute: " + attribute);      
	  			}
	  		} else {
	  			// error, non-optional
					pw.println("\t-- ERROR: attribute unset !!! - path_constraint+entity_constraint+inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint");      
	  		}






			} else
			if (inverted_attribute instanceof ESelect_constraint) {
				pw.println("-- ERROR: path_constraint + inverse_attribute_constraint - inverted_attribute - select_constraint - not yet implemented");
			} else {
				// all possible types already above 
			}
			

//3333333333333
			String bag_member_type = "GENERIC";
			if (element2 instanceof EAttribute) {
				bag_member_type = ((EAttribute)element2).getParent(null).getName(null).toLowerCase();  	
				
			}
			pw.println("\tLOCAL");      
			pw.println("\t\tresult : BAG OF GENERIC;");
			pw.println("\t\tr : GENERIC;");
			pw.println("\t\taggr : BAG OF " + bag_member_type + " := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
			pw.println("\tEND_LOCAL;");      
			
		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	

			
			pw.println("\tREPEAT i:=1 TO SIZEOF(aggr);");
			pw.println("\t\tIF \'" +    AIM_schema_name + "." + domain_name + "\' IN TYPEOF (aggr[i]) THEN");



			if (element2 instanceof EAttribute) {
//				printDebug("\t\t-- r := e." + ((EAttribute)element2).getName(null) + ";");
//				pw.println("\t\t\tr := aggr[i]." + ((EAttribute)element2).getName(null) + ";");
				pw.println("\t\t\tr := aggr[i]\\" + ((EAttribute)element2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)element2).getName(null) + ";");
			} else {
				pw.println("\t\t\tr := " + element2_function_name + "(aggr[i]);");
			}

//			pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
			pw.println("\t\t\tIF EXISTS(r) THEN");
			pw.println("\t\t\t\tIF NOT EXISTS(result) THEN");
			pw.println("\t\t\t\t\tresult := [];");
			pw.println("\t\t\t\tEND_IF;");
			pw.println("\t\t\t\tresult := result + r;");
			pw.println("\t\t\tEND_IF;");
			pw.println("\t\tEND_IF;");
			pw.println("\tEND_REPEAT;");
			pw.println("\tIF SIZEOF(result) = 1 THEN");
			pw.println("\t\tRETURN (result[1]);");      
			pw.println("\tELSE");
			pw.println("\t\tRETURN (result);");      
			pw.println("\tEND_IF;");
// ############################################################################				
				
				} else {
						// not in ap210
						printDebug("-- not implemented - attribute = not implemented case");						
				}	
			} else {
				// ERROR - not OPTIONAL - must be set
				System.out.println("ERROR: UNSET attribute in " + entity_constraint);
				printError("\t-- ERROR!!! UNSET attribute : attribute_select");
			} 

		if (element1 instanceof EExact_entity_constraint) {
			/*
				An exact_entity_constraint is entity constraint that constraints the attribute to be 
				of some specific entity type, excluding subtypes of it.
			*/
			
				pw.println("-- ERROR: path_constraint + exact_entity_constraint - not yet fully implemented, treated as entity_constraint");
		} 

		if ((!(attribute instanceof EInverse_attribute_constraint)) && (!(attribute instanceof  EAggregate_member_constraint))) {



		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	
		

//		pw.println("\tRETURN (\'" + AIM__schema_name + "\' + \'" + domain_name + "\' IN TYPEOF (e." + attribute_name + "));");



//			pw.println("\t\tresult : BAG OF GENERIC := [];");
//			pw.println("\t\tr : GENERIC;");

//#			pw.println("\tLOCAL");      
//#			if (!(attribute_name.equals("_UNKNOWN__attribute"))) {
//#				pw.println("\t\tee : " + attribute_domain_name + " := e." + attribute_name + ";");
//#			} else {
//#				pw.println("\t\t -- unknown attribute name, to be implemented: ee : .. := e.attribute_name");
//#
//# //  let's make not an aggregate for now
//# //				pw.println("\t\tee : " + attribute_domain_name + " := [];");
//# 				pw.println("\t\tee : " + attribute_domain_name + ";");
//# 			}
//#			pw.println("\tEND_LOCAL;");      


//#			pw.println("\tIF \'" +    AIM_schema_name + "." + domain_name + "\' IN TYPEOF (ee) THEN");
			pw.println("\tIF \'" +    AIM_schema_name + "." + domain_name + "\' IN TYPEOF (" + ee_name + ") THEN");


//			pw.println("\tIF \'" + domain_schema_name + "." + domain_name + "\' IN TYPEOF (ee) THEN");

			if (element2 instanceof EAttribute) {
//				printDebug("\t\t-- r := e." + ((EAttribute)element2).getName(null) + ";");

//#				pw.println("\t\tRETURN (ee." + ((EAttribute)element2).getName(null) + ");");

// experiment-entity
				pw.println("\t\tRETURN (" + ee_name + "." + ((EAttribute)element2).getName(null) + ");");
//				pw.println("\t\tRETURN (e." + ((EAttribute)element2).getName(null) + ");");

			} else {


//#				pw.println("\t\tRETURN (" + element2_function_name + "(ee));");

// experiment-entity
				pw.println("\t\tRETURN (" + element2_function_name + "(" + ee_name + "));");
//				pw.println("\t\tRETURN (" + element2_function_name + "(e));");



	//			pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
			}

//			pw.println("\t\tRETURN (" + element2_function_name + "(ee));");
			pw.println("\tEND_IF;");
			pw.println("\tRETURN (?);");      



      // a non-optimized approach
			// pw.println("\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (ee) THEN");
			// pw.println("\t\tr :=  " + element2_function_name + "(ee);");
			// pw.println("\t\tIF EXISTS(r) THEN");
			// pw.println("\t\t\tRETURN (r);");
			// pw.println("\t\tEND_IF;");
			// pw.println("\tEND_IF;");
			// pw.println("\tRETURN (?);");      

		} // attribute not inverse_attribute_constraint
	
	  } else 
	  if (element1 instanceof EIntersection_constraint) {
				printDebug("-- WOA! implementing path_constraint, element1: intersection_constraint");


			AConstraint_select subpaths = null;
			if (((EIntersection_constraint)element1).testSubpaths(null)) {
				subpaths = ((EIntersection_constraint)element1).getSubpaths(null);
			} else {
				// unset non-optional
				System.out.println("ERROR: UNSET subpaths in " + element1);
				printError("\t-- ERROR!!! UNSET subpaths : constraint_select");
			}
			pw.println("\tLOCAL");      
			pw.println("\t\tintersection : BAG OF GENERIC := [];");
			pw.println("\t\tr : GENERIC;");
			pw.println("\t\tresult : BAG OF GENERIC := [];");
			pw.println("\t\tsubpath : BAG OF GENERIC;");
			pw.println("\tEND_LOCAL;");      

		pw.println("\tIF NOT EXISTS (e) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");	

			// subpaths : SET [2:?] OF constraint_select;
			SdaiIterator subpaths_iter = subpaths.createIterator();
			// at least two members must be present
			boolean first_time = true;
			
			while (subpaths_iter.next()) {
				EEntity subpath_constraint = (EEntity)subpaths.getCurrentMemberObject(subpaths_iter);
				// what if it is not constraint but attribute? - indicates that it must have a value, ok get that value
				printDebug("-- subpath_constraint element: " + subpath_constraint);
				String subpath_constraint_function_name = "f" + subpath_constraint.getPersistentLabel().substring(1).toLowerCase();
		    /* possible subpaths member types:
	    			constraint_attribute
	   				constraint_relationship
	   				inverse_attribute_constraint
	   				attribute
	 					end_of_path_constraint
	    			intersection_constraint
	    			type_constraint
	    			negation_constraint
		    */		
		    
		    
		    
				if (subpath_constraint instanceof EAttribute) {
					printDebug("-- WOA! implementing path_constraint, element1: intersection_constraint, subpath - attribute");
//					pw.println("\tr := e." +  ((EAttribute)subpath_constraint).getName(null) + ";");
					pw.println("\tr := e\\" + ((EAttribute)subpath_constraint).getParent(null).getName(null).toLowerCase() + "." +  ((EAttribute)subpath_constraint).getName(null) + ";");
				} else {
					pw.println("\tr := " + subpath_constraint_function_name + "(e);");
					result.addElement(subpath_constraint);
				}
				if (first_time) {
					pw.println("\tintersection := intersection + r;");
					first_time = false;
				} else {
					pw.println("\tsubpath := [];");
					pw.println("\tsubpath := subpath + r;");
					pw.println("\tintersection := intersection * subpath;");
				}
			}
			pw.println("\tIF NOT EXISTS(intersection) THEN");
				pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");
			pw.println("\tREPEAT i:= 1 TO SIZEOF(intersection);");

			if (element2 instanceof EAttribute) {
//				printDebug("\t\t-- r := e." + ((EAttribute)element2).getName(null) + ";");
//				pw.println("\t\tr := intersection[i]." + ((EAttribute)element2).getName(null) + ";");
				pw.println("\t\tr := intersection[i]\\" + ((EAttribute)element2).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)element2).getName(null) + ";");
			} else {
				pw.println("\t\tr := " + element2_function_name + "(intersection[i]);");
//				pw.println("\t\tRETURN (" + element2_function_name + "(ee));");
	//			pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
			}


//				pw.println("\t\tr := " + element2_function_name + "(intersection[i]);");
				pw.println("\t\tIF EXISTS (r) THEN");
					pw.println("\t\t\tresult := result + r;");
				pw.println("\t\tEND_IF;");
			pw.println("\tEND_REPEAT;");	
			pw.println("\tIF SIZEOF(result) > 0 THEN");
				pw.println("\t\tRETURN (result);");
      pw.println("\tELSE");
        pw.println("\t\tRETURN (?);");
      pw.println("\tEND_IF;");  

//				pw.println("-- ERROR: path_constraint + intersection_constraint - not yet implemented");
//			pw.println("\tRETURN (result);");      
		
	
	
//			pw.println("\tRETURN (?);");      
		} else
		if (element1 instanceof EAttribute) {
				// must be implemented (is implemented)
				printDebug("\t-- implementing path_constraint, element1: attribute");

// FUNCTION f99574(e : product_definiiton) : GENERIC; --boolean;

//			pw.println("\t\tresult : BAG OF GENERIC := [];");
 

//			pw.println("\tEND_LOCAL;");      
				
				pw.println("\tIF NOT EXISTS(e) THEN");
					pw.println("\t\tRETURN (?);");
				pw.println("\tEND_IF;");	

			String attribute_name = ((EAttribute)element1).getName(null).toLowerCase();
			String parent_entity_name = ((EAttribute)element1).getParent(null).getName(null).toLowerCase();
// enough to return function, it will be either ? or with value, the return type is no longer aggregate



			if (element2 instanceof EAttribute) {
//				printDebug("\t\t-- r := e." + ((EAttribute)element2).getName(null) + ";");
				pw.println("\tRETURN (e\\" + parent_entity_name + "." + attribute_name + "." + ((EAttribute)element2).getName(null) + ");");
			} else {
//				pw.println("\t\tr := " + element2_function_name + "(intersection[i]);");
//				pw.println("\t\tRETURN (" + element2_function_name + "(ee));");
	//			pw.println("\t\tr := " + element2_function_name + "(aggr[i]);");
				pw.println("\tRETURN (" + element2_function_name + "(e\\" + parent_entity_name + "." + attribute_name + "));");
			}





//			pw.println("\tRETURN (" + element2_function_name + "(e." + attribute_name + "));");

//			pw.println("\tr := " + element2_function_name + "(e." + attribute_name + ");");
//			pw.println("\t\tIF EXISTS(r) THEN");
//			pw.println("\t\t\tresult := result + r;");
//			pw.println("\t\tEND_IF;");
//			pw.println("\tRETURN (result);");      
		} else {
			// all possible types already above
			pw.println("\t-- ERROR: INCORRECT elemet1 of path_constraint;");      
//			pw.println("\tEND_LOCAL;");      
			pw.println("\tRETURN (?);");      
		}

//		pw.println("\tRETURN (result);");      
		pw.println("END_FUNCTION;");
	
		return result;
	}				

	Vector handlePath_constraint(EConstraint constraint, EEntity element2) throws SdaiException {
 		/*
				A path_constraint is a constraint_relationship that does following: 
				requires to make a step using element1 and meet requirements of element2. 
				element1 shall be a constraint on an attribute of an entity-type. 
				element2 defines a constraint on the entity reached by following element1.
				
				element1 
					is an attribute or other element that allows to make step from one entity instance to instance of other entity.

				
				element_1 - path_constraint_select -- for making a step
				possible types:
					aggregate_member_constraint
	 				inverse_attribute_constraint
	 				entity_constraint
	 				intersection_constraint
	 				attribute
				present in ap210 (at least these):
					entity_constraint
					explicit_attribute
					inverse_attribute_constraint
		
				element2 -- constraint_select -- must meet its requiremens after making a step indicated by element1
		*/
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		
		EPath_constraint path_constraint = (EPath_constraint)constraint;
		// element1 : path_constraint_select;
		EEntity element1 = null;
		if (path_constraint.testElement1(null)) {
			element1 = path_constraint.getElement1(null);
			printAttribute("\t-- element1 : path_constraint_select = " + element1);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET element1 in " + constraint);
			printError("\t-- ERROR!!! UNSET element1 : path_constraint_select");
		}

		pw.println("\tRETURN (?)");		
		return null;
	}				

	Vector handleEnd_of_path_constraint(EConstraint constraint) throws SdaiException {				
		printDebug("\n-- function implementing end_of_path_constraint");
		/*
			
			End_of_path_constraint is intended as a replacement for
			attribute_constraint.path list. It allows to identify end of the main path
			condition. See comments at attribute_constraint.path<br>
			End_of_path_constraint is used as value of path_constraint.element2 or at the top of
			constraint tree.<br>
			Introduced in version 31.1.
		
			constraints
				The remaining part of constraint tree which doesn't belong to the main path.
		
		*/
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		EEnd_of_path_constraint end_of_path_constraint = (EEnd_of_path_constraint)constraint;
		// constraints : constraint_select;
		EEntity constraints = null;
		if (end_of_path_constraint.testConstraints(null)) {
			constraints = end_of_path_constraint.getConstraints(null);
			printAttribute("\t-- constraints : constraint_select = " + constraints);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraints in " + constraint);
			printError("\t-- ERROR!!! UNSET constraints : constraint_select");
		}
		
		// temp test
		// constraint_select
	  /*
	  constraint_attribute
	  constraint_relationship
	  inverse_attribute_constraint
		attribute
		end_of_path_constraint
	  intersection_constraint
	  type_constraint
	  negation_constraint
		*/
		
		
		
		String f_constraints_name = "f" + constraints.getPersistentLabel().substring(1).toLowerCase();

// previously:
/*
-- #99575=END_OF_PATH_CONSTRAINT(...)
FUNCTION f99575(e : product_definiiton) : GENERIC; --boolean;
	IF f99574(e) = TRUE THEN -- follow constraint
		RETURN (e);
	ELSE
		RETURN (?);
	END_IF;
END_FUNCTION;
*/




// no need to check, because, if not true, it itself returns ?
		
		if (constraints instanceof EAttribute) {
		 printDebug("\n-- implementing end_of_path_constraint, attribute: attribute");
//			pw.println("\tIF EXISTS(e." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
			pw.println("\tIF EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
//			pw.println("\tRETURN (e." + ((EAttribute)constraints).getName(null).toLowerCase() + ");");
		} else {
			pw.println("\tIF EXISTS(" + f_constraints_name + "(e)) THEN");
//			pw.println("\tRETURN (" + f_constraints_name + "(e));");
		}
			pw.println("\t\tRETURN (e);");
		pw.println("\tEND_IF;");
		pw.println("\tRETURN (?);");	

		Vector result = new Vector();
		result.addElement(constraints);
		return result;
//		return null;
	}			

/*

Instead of:

FUNCTION f104851(e : GENERIC) : GENERIC;
	RETURN (f104850(e));
END_FUNCTION;

FUNCTION f113345(e : placement) : GENERIC;
	RETURN (e.location);
END_FUNCTION;


Do this:


FUNCTION f104851(e : GENERIC) : GENERIC;
	IF EXISTS(f104850(e)) THEN
		RETURN (e);
	END_IF;
	RETURN (?);	
END_FUNCTION;

FUNCTION f113345(e : placement) : GENERIC;
	IF EXISTS(e.location) THEN
		RETURN (e);
	END_IF;
	RETURN (?);	
END_FUNCTION;

*/




	Vector handleIntersection_constraint(EConstraint constraint) throws SdaiException {				
		// does not happin in the current ap210 at all, at least separately, perhaps inside other constraints
		printDebug("\n-- function implementing intersection_constraint");
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		EIntersection_constraint intersection_constraint = (EIntersection_constraint)constraint;
		// subpaths : SET [2:?] OF constraint_select;
		AConstraint_select subpaths = null;
		if (intersection_constraint.testSubpaths(null)) {
			subpaths = intersection_constraint.getSubpaths(null);
			printAttribute("\t-- subpaths : SET [2:?] OF constraint_select = " + subpaths);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET subpaths in " + constraint);
			printError("\t-- ERROR!!! UNSET subpaths : SET [2:?] OF constraint_select");
		}
		pw.println("\tRETURN (?)");
		return null;
	}			

	Vector handleInverse_attribute_constraint(EConstraint constraint) throws SdaiException {				
		printDebug("\n-- function implementing inverse_attribute_constraint");
		/*
		
			An inverse_attribute_constraint defines a needed inverse attribute that is missing in the target schema.
			This is needed to travel reverse to attribute definition.

			NOTE 1 - In the case that the AIM express already contains the proper inverse attribute this should be used.
		
			An inverted_attribute is a definition of attribute that is inverted.
		
		*/
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		EInverse_attribute_constraint inverse_attribute_constraint = (EInverse_attribute_constraint)constraint;
		// inverted_attribute : inverse_attribute_constraint_select;
		EEntity inverted_attribute = null;
		if (inverse_attribute_constraint.testInverted_attribute(null)) {
			inverted_attribute = inverse_attribute_constraint.getInverted_attribute(null);
			printAttribute("\t-- inverted_attribute : inverse_attribute_constraint_select = " + inverted_attribute);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET inverted_attribute in " + constraint);
			printError("\t-- ERROR!!! UNSET inverted_attribute : inverse_attribute_constraint_select");
		}



		String inverted_attribute_name = "UNKNOWN__inverted_attribute_name";
		String inverted_attribute_parent_entity_name = "UNKNOWN_inverted_attribute_parent_entity_name";
		String inverted_attribute_parent_entity_schema_name = "x";
  	if (inverted_attribute instanceof EAttribute) {
			inverted_attribute_name = ((EAttribute)inverted_attribute).getName(null).toLowerCase();
			inverted_attribute_parent_entity_name = ((EAttribute)inverted_attribute).getParent(null).getName(null).toLowerCase();
			String inverted_attribute_parent_entity_model_name = ((EAttribute)inverted_attribute).getParent(null).findEntityInstanceSdaiModel().getName();	
	    inverted_attribute_parent_entity_schema_name = inverted_attribute_parent_entity_model_name.substring(0, inverted_attribute_parent_entity_model_name.length() - 16).toLowerCase();

			pw.println("\tLOCAL");      
			pw.println("\t\tresult : BAG OF GENERIC := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
			pw.println("\tEND_LOCAL;");      
			pw.println("\tIF EXISTS(result) THEN");
			pw.println("\t\tIF SIZEOF(result) > 0 THEN");
			pw.println("\t\t\tRETURN (result);");      
			pw.println("\t\tELSE");
				pw.println("\t\t\tRETURN (?);");
				pw.println("\t\tEND_IF;");
			pw.println("\tELSE");
				pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");
	
		} else
		if (inverted_attribute instanceof EEntity_constraint) {
				// does not occur in the current ap210
				pw.println("-- ERROR: inverse_attribute_constraint - inverted_attribute - entity_constraint - not yet implemented");
		} else
		if (inverted_attribute instanceof EAggregate_member_constraint) {
			// must be implemented
			printDebug("-- implementing inverse_attribute_constraint - inverted_attribute = aggregate_member_constraint");
		
			EAggregate_member_constraint aggregate_member_constraint = (EAggregate_member_constraint)inverted_attribute;
		  int member = Integer.MIN_VALUE;
	  	EEntity attribute = null;
		  if (aggregate_member_constraint.testMember(null)) {
	  		member = aggregate_member_constraint.getMember(null);
				// does not occur in the current ap210
				printDebug("\t-- implementing inverse_attribute_constraint, inverted_attribute = aggregate_member_constraint, its member is set: " + member);      
	  	} else {
//				printDebug("\t-- inverted_attribute = aggregate_member_constraint, its optional member is not set");      
	  	}
	  	if (aggregate_member_constraint.testAttribute(null)) {
	  		attribute = aggregate_member_constraint.getAttribute(null);
//					printDebug("\t-- inverted_attribute = aggregate_member_constraint, its attribute = " + attribute);      


	  		if (attribute instanceof EAttribute) {
	  			String attribute_name = ((EAttribute)attribute).getName(null).toLowerCase();
				  String parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();
	  			inverted_attribute_name = attribute_name;
	  			EEntity_definition attribute_parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
					inverted_attribute_parent_entity_name = attribute_parent_entity.getName(null).toLowerCase();
					
	  		} else {
					// does not occur in the current ap210
					printDebug("-- implementing inverse_attribute_constraint - inverted_attribute = aggregate_member_constraint, attribute: not attribute");
	  		}	
			}
		
			pw.println("\tLOCAL");      
			pw.println("\t\tresult : BAG OF GENERIC := USEDIN(e, \'" + AIM_schema_name + "." + inverted_attribute_parent_entity_name.toUpperCase() + "." + inverted_attribute_name.toUpperCase() + "\');");
			pw.println("\tEND_LOCAL;");      
			pw.println("\tIF EXISTS(result) THEN");
			pw.println("\t\tIF SIZEOF(result) > 0 THEN");
			pw.println("\t\t\tRETURN (result);");      
			pw.println("\t\tELSE");
				pw.println("\t\t\tRETURN (?);");
				pw.println("\t\tEND_IF;");
			pw.println("\tELSE");
				pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");
		
		
		} else
		if (inverted_attribute instanceof ESelect_constraint) {
			// does not occur in the current ap210
			pw.println("-- ERROR: inverse_attribute_constraint - inverted_attribute - select_constraint - not yet implemented");
		} else {
			// all possible types already above 
			// does not occur in the current ap210
			pw.println("-- ERROR: inverse_attribute_constraint - not a possible type");
		}

		return null;
	}			

	Vector handleNegation_constraint(EConstraint constraint) throws SdaiException {				
		printDebug("\n-- function implementing negation_constraint");
		Vector result = new Vector();

		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		ENegation_constraint negation_constraint = (ENegation_constraint)constraint;
		// constraints : constraint_select;
		EEntity constraints = null;
		String constraints_function_name = "_UNKNOWN_";
		if (negation_constraint.testConstraints(null)) {
			constraints = negation_constraint.getConstraints(null);
			printAttribute("\t-- constraints : constraint_select = " + constraints);
			constraints_function_name = "f" + constraints.getPersistentLabel().substring(1).toLowerCase();
			result.addElement(constraints);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET constraints in " + constraint);
			printError("\t-- ERROR!!! UNSET constraints : constraint_select");
		}
		// constraint select
		if (constraints instanceof EConstraint_attribute) {
			// must be implemented !!!
			printDebug("\n-- implementing negation_constraint, constraints: constraints_attribute");
			if (constraints instanceof EAttribute_value_constraint) {
				// must be implemented !!!
				printDebug("\n-- implementing negation_constraint, constraints: attribute_value_constraint");
				if (constraints instanceof EBoolean_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing negation_constraint, constraints: boolean_constraint");
				} else
				if (constraints instanceof EInteger_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing negation_constraint, constraints: integer_constraint");
				} else
				if (constraints instanceof ENon_optional_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing negation_constraint, constraints: non_optional_constraint");
				} else
				if (constraints instanceof EString_constraint) {
					// must be implemented !!
 					printDebug("\n-- implementing negation_constraint, constraints: string_constraint");
				} else
				if (constraints instanceof EReal_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing negation_constraint, constraints: real_constraint");
				} else
				if (constraints instanceof EEnumeration_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing negation_constraint, constraints: enumeration_constraint");
				} else
				if (constraints instanceof ELogical_constraint) {
					// does not occur in the current ap210
					printDebug("\n-- implementing negation_constraint, constraints: logical_constraint");
				} else {
					// does not occur in the current ap210
					printDebug("\n-- implementing negation_constraint, constraints: impossible constraint type");
				}

			} else 
			if (constraints instanceof ESelect_constraint) {
				// does not occur in the current ap210
				printDebug("\n-- implementing negation_constraint, constraints: select_constraint");
			} else 
			if (constraints instanceof EAggregate_member_constraint) {
				// does not occur in the current ap210
				printDebug("\n-- implementing negation_constraint, constraints: aggregate_member_constraint");
			} else 
			if (constraints instanceof EEntity_constraint) {
				// does not occur in the current ap210
				printDebug("\n-- implementing negation_constraint, constraints: entity_constraint");
			} else {
				// does not occur in the current ap210
				printDebug("\n-- implementing negation_constraint, constraints: impossible constraint type: " + constraints);
			} 


		} else 
		if (constraints instanceof EConstraint_relationship) {
			// must be implemented !!!
			printDebug("\n-- implementing negation_constraint, constraints: constraints_relationship");
			if (constraints instanceof EPath_constraint) {
				// must be implemented !!!
				printDebug("\n-- implementing negation_constraint, constraints: path_constraints");
			} else 
			if (constraints instanceof EInstance_constraint) {
				// does not occur in the current ap210
				printDebug("\n-- implementing negation_constraint, constraints: instance_constraints");
			} else {
				// does not occur in the current ap210
				printDebug("\n-- implementing negation_constraint, constraints: impossible constraint type");
			} 
		} else 
		if (constraints instanceof EInverse_attribute_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- implementing negation_constraint, constraints: inverse_attribute_constraint");
		} else 
		if (constraints instanceof EAttribute) {
			// does not occur in the current ap210
			printDebug("\n-- implementing negation_constraint, constraints: attribute");
		} else 
		if (constraints instanceof EEnd_of_path_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- implementing negation_constraint, constraints: end_of_path_constraint");
		} else 
		if (constraints instanceof EIntersection_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- implementing negation_constraint, constraints: intersection_constraint");
		} else 
		if (constraints instanceof EType_constraint) {
			// must be implemented !!!
			printDebug("\n-- implementing negation_constraint, constraints: type_constraint");
			if (constraints instanceof EExact_type_constraint) {
				// does not occur in the current ap210
				printDebug("\n-- implementing negation_constraint, constraints: exact_type_constraint");
			}
		} else 
		if (constraints instanceof ENegation_constraint) {
			// does not occur in the current ap210
			printDebug("\n-- implementing negation_constraint, constraints: negation_constraint");
		} else {
			// does not occur in the current ap210
			printDebug("\n-- implementing negation_constraint, constraints: impossible case");
		}

		
		
		// - should return ? instead of some specific value
		// but what should be returned if ?, what specific value to return, it should be anything but ?
		pw.println("\tIF EXISTS(" + constraints_function_name + "(e)) THEN");
			pw.println("\t\tRETURN (?);");
		pw.println("\tELSE");
//			pw.println("\t\tRETURN (FALSE);");
			pw.println("\t\tRETURN (TRUE);");
		pw.println("\tEND_IF;");
		
		return result;
//		return null;
	}		

	Vector handleType_constraint(EConstraint constraint) throws SdaiException {
		printDebug("\n-- function implementing type_constraint");
		Vector result = new Vector();
		SdaiModel map_model = global_map_model;
		String parameter_type_name = getParameterTypeName(constraint, map_model);
		String function_name = "f" + constraint.getPersistentLabel().substring(1).toLowerCase();
		pw.println("FUNCTION " + function_name + "(e : " + parameter_type_name + ") : GENERIC;");
		EType_constraint type_constraint = (EType_constraint)constraint;
		// domain : entity_definition;
		EEntity_definition domain = null;
		String domain_name = "GENERIC";
		String domain_schema_name = AIM_schema_name;
		if (type_constraint.testDomain(null)) {
			domain = type_constraint.getDomain(null);
			domain_name = domain.getName(null).toUpperCase();
			String domain_model_name = domain.findEntityInstanceSdaiModel().getName();	
      domain_schema_name = domain_model_name.substring(0, domain_model_name.length() - 16).toUpperCase();
			if (domain_schema_name.equals("MIXED_COMPLEX_TYPES")) {
				domain_schema_name = AIM_schema_name;
			}
			printAttribute("\t-- domain : entity_definition = " + domain);
		} else {
			// ERROR - not OPTIONAL - must be set
			System.out.println("ERROR: UNSET domain in " + constraint);
			printError("\t-- ERROR!!! UNSET domain : entity_definition");
		}
		// constraints : OPTIONAL constraint_select;
		EEntity constraints = null;
		String constraints_function_name = "_UNKNOWN_";
		if (type_constraint.testConstraints(null)) {
			constraints = type_constraint.getConstraints(null);
			constraints_function_name = "f" + constraints.getPersistentLabel().substring(1).toLowerCase();
			result.addElement(constraints);
			printAttribute("\t-- constraints : OPTIONAL constraint_select = " + constraints);
		} 
		if (constraint instanceof EExact_type_constraint) {
			// does not happen in ap210, but it should not allow subtypes, exact types only
		} 

		if (constraints instanceof EAttribute) {
		  printDebug("\n-- implementing type_constraint, constraints: attribute");
			pw.println("\tLOCAL");

			String ee_type = ((EAttribute)constraints).getParent(null).getName(null).toLowerCase();
		
			pw.println("\t\tee : " + ee_type + ";");

			pw.println("\tEND_LOCAL;");
			pw.println("\tIF NOT EXISTS(e) THEN");
				pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");	
			pw.println("\tIF \'" + AIM_schema_name + "." + ee_type.toUpperCase() + "\' IN TYPEOF (e) THEN");
			pw.println("\t\tee := e;");	
			pw.println("\tELSE");	
				pw.println("\t\tRETURN (?);");
			pw.println("\tEND_IF;");	
		}
//		pw.println("\tIF \'" + domain_schema_name + "." + domain_name + "\' IN TYPEOF (e) THEN");
		pw.println("\tIF \'" + AIM_schema_name + "." + domain_name + "\' IN TYPEOF (e) THEN");
		if (constraints == null) { // optional
  		pw.println("\t\tRETURN (e);");
  	} else 
  	if (constraints instanceof EAttribute) {
  		String attr_name = ((EAttribute)constraints).getName(null).toLowerCase();
  		pw.println("\t\tRETURN (ee." + attr_name + ");");
  	} else {
  		pw.println("\t\tRETURN (" + constraints_function_name + "(e));");
		}
		pw.println("\tELSE");
  		pw.println("\t\tRETURN (?);");
		pw.println("\tEND_IF;");

		// pw.println("\tRETURN (?)");
		return result;
//  	return null;
  }    

	String	getAggregateTypeName(EAggregation_type rt1)  throws SdaiException {
		String result = "AGGREGATE";

    String type_name = "";
    String generic_name = "";


    if (rt1 instanceof EArray_type) {
      type_name += ("ARRAY");
      // as a local variable, array does not need to have bounds
      // type_name += constructArrayBoundNames((EArray_type) rt1);

/*
      // additionally - OPTIONAL and UNIQUE.
      if (((EArray_type) rt1).testOptional_flag(null)) {
        if (((EArray_type) rt1).getOptional_flag(null)) {
          type_name += "_OPTIONAL";
        }
      }

      if (((EArray_type) rt1).testUnique_flag(null)) {
        if (((EArray_type) rt1).getUnique_flag(null)) {
          type_name += "_UNIQUE";
        }
      }
*/
    } else if (rt1 instanceof EBag_type) {
      type_name += ("BAG");
      // type_name += constructBagListSetBoundNames((EVariable_size_aggregation_type) rt1);
    } else if (rt1 instanceof ESet_type) {
// set->bag      type_name += ("SET");
      // at least a temporary solution
      type_name += ("BAG");
      // type_name += constructBagListSetBoundNames((EVariable_size_aggregation_type) rt1);
    } else if (rt1 instanceof EList_type) {
      type_name += ("LIST");
      // type_name += constructBagListSetBoundNames((EVariable_size_aggregation_type) rt1);

/*
      // UNIQUE
      if (((EList_type) rt1).testUnique_flag(null)) {
        if (((EList_type) rt1).getUnique_flag(null)) {
          type_name += "_UNIQUE";
        }
      }
*/      
    } else {
      // maybe AGGREGATE
      String aggr_name;

      if (rt1.testName(null)) {
        aggr_name = rt1.getName(null);

        if (aggr_name.substring(0, "_AGGREGATE".length()).equals("_AGGREGATE")) {
          type_name = "AGGREGATE"; //aggr_name; 
        } else {
          type_name += "_UNKNOWNAGGREGATE";
        }
      } else {
        type_name += "_NONAMEAGGREGATE";
      }
    }

    EEntity rt = null;
    int aggregate_depth = 1;
    EEntity an_ss = null;
    EEntity ass = rt1;
    String aggr_prefices = "";

    // an_ss = rt1.getElement_type(null);
    boolean element_type_set = rt1.testElement_type(null);

    if (element_type_set) {
      an_ss = rt1.getElement_type(null);
      rt = an_ss;
    } else {
      System.out.println("RG: In LocalVariable, aggregate element type value unset:" + rt1);
    }

    for (;;) {
      boolean done_something = false;

      if (an_ss instanceof EDefined_type) {
        //            ass = an_ss;
        //            boolean domain_set = ((EDefined_type)an_ss).testDomain(null);
        //            if (domain_set) {
        //               an_ss = ((EDefined_type)an_ss).getDomain(null);
        //               done_something = true;
        //            } else {
        //               System.out.println("XP: constructing type name, in aggregate nested defined type domain unset:" + an_ss);
        //            }
      } else if (an_ss instanceof EAggregation_type) {
        aggr_prefices += "a";
        aggregate_depth++;
        ass = an_ss;

        if (ass instanceof EArray_type) {
          type_name += ("OF ARRAY");
          // type_name += constructArrayBoundNames((EArray_type) ass);
					

					/*
          if (((EArray_type) ass).testOptional_flag(null)) {
            if (((EArray_type) ass).getOptional_flag(null)) {
              type_name += "_OPTIONAL";
            }
          }

          if (((EArray_type) ass).testUnique_flag(null)) {
            if (((EArray_type) ass).getUnique_flag(null)) {
              type_name += "_UNIQUE";
            }
          }
        */
        } else if (ass instanceof EBag_type) {
          type_name += ("OF BAG");
          // type_name += constructBagListSetBoundNames((EVariable_size_aggregation_type) ass);
        } else if (ass instanceof ESet_type) {
// set -> bag          type_name += ("OF SET");
          type_name += ("OF BAG");
          // type_name += constructBagListSetBoundNames((EVariable_size_aggregation_type) ass);
        } else if (ass instanceof EList_type) {
          type_name += ("OF LIST");
          // type_name += constructBagListSetBoundNames((EVariable_size_aggregation_type) ass);

					/*
          if (((EList_type) ass).testUnique_flag(null)) {
            if (((EList_type) ass).getUnique_flag(null)) {
              type_name += "_UNIQUE";
            }
          }
         */ 
        } else if (ass instanceof EAggregation_type) {
          // maybe AGGREGATE
          String aggr_name;

          if (((EAggregation_type) ass).testName(null)) {
            aggr_name = ((EAggregation_type) ass).getName(null);

            if (aggr_name.substring(0, "_AGGREGATE".length()).equals("_AGGREGATE")) {
              type_name = "OF AGGREGATE"; //aggr_name;
            } else {
              type_name += "_UNKNOWNAGGREGATE";
            }
          } else {
            type_name += "_NONAMEAGGREGATE";
          }
        } else {
          type_name += "_NOTAGGREGATE";
        }

        element_type_set = ((EAggregation_type) an_ss).testElement_type(null);

        if (element_type_set) {
          an_ss = ((EAggregation_type) an_ss).getElement_type(null);
          done_something = true;
        } else {
          System.out.println(
                "RG: constructing type names, nested aggregate element type value unset:" + 
                an_ss);
        }
      }

      if (!done_something) {
        break;
      }
    }

    String aggr_prefix = "A" + aggr_prefices;
    rt = an_ss;

    if (rt == null) {
      type_name += "_NULL";
      System.out.println("RG: constructing type names: base type of aggregation type is null: " + 
                         rt1);
    } else if (rt instanceof EAggregation_type) {
//      type_name += constructAggregationTypeBaseTypeNameString((EAggregation_type) rt, is_generic);
    } else
    //      if (rt instanceof EAggregation_type) {
    //         type_name += "_AGGREGATE";
    //    } else
    if (rt instanceof EDefined_type) {
//      type_name += constructDefinedTypeUnderlyingTypeNameString((EDefined_type) rt);
      type_name += " OF " + ((EDefined_type)rt).getName(null).toLowerCase();
    } else if (rt instanceof ESelect_type) {
      // will not reach this case
      // type_name += constructNameWithAggregatePackage(aggr_prefix, ass);
    } else if (rt instanceof EEntity_definition) {
//      type_name += constructNameWithAggregatePackage(aggr_prefix, rt);
      type_name += " OF " + ((EEntity_definition)rt).getName(null).toLowerCase();
    } else if (rt instanceof EInteger_type) {
      type_name += "OF INTEGER";
    } else if (rt instanceof EReal_type) {
      type_name += "OF REAL";

      EReal_type real_type = (EReal_type) rt;
      boolean precision_present = real_type.testPrecision(null);

			/*
      if (precision_present) {
        EBound b_precision = real_type.getPrecision(null);

        if (b_precision instanceof EInteger_bound) {
          EInteger_bound i_precision = (EInteger_bound) b_precision;
          boolean integer_bound_set = i_precision.testBound_value(null);

          if (integer_bound_set) {
            int precision_value = i_precision.getBound_value(null);
            type_name += ("_" + precision_value);
          }
        }
      }
     */ 
    } else if (rt instanceof ENumber_type) {
      type_name += "OF NUMBER";
    } else if (rt instanceof EBoolean_type) {
      type_name += "OF BOOLEAN";
    } else if (rt instanceof EBinary_type) {
      type_name += "OF BINARY";

      EBinary_type binary_type = (EBinary_type) rt;
      boolean width_present = binary_type.testWidth(null);

      /*
      if (width_present) {
        EBound b_width = binary_type.getWidth(null);

        if (b_width instanceof EInteger_bound) {
          EInteger_bound i_width = (EInteger_bound) b_width;
          boolean integer_bound_set = i_width.testBound_value(null);

          if (integer_bound_set) {
            int width_value = i_width.getBound_value(null);
            type_name += ("_" + width_value);
          }
        }

        boolean fixed_present = binary_type.testFixed_width(null);

        if (fixed_present) {
          boolean fixed_value = binary_type.getFixed_width(null);

          if (fixed_value) {
            type_name += "_FIXED";
          }
        }
      }
     */ 
    } else if (rt instanceof EEnumeration_type) {
      // will not reach
      type_name += "OF ENUMERATION";
    } else if (rt instanceof ELogical_type) {
      type_name += "OF LOGICAL";
    } else if (rt instanceof EString_type) {
      type_name += "OF STRING";

      EString_type string_type = (EString_type) rt;
      boolean width_present = string_type.testWidth(null);
	
			/*
      if (width_present) {
        EBound b_width = string_type.getWidth(null);

        if (b_width instanceof EInteger_bound) {
          EInteger_bound i_width = (EInteger_bound) b_width;
          boolean integer_bound_set = i_width.testBound_value(null);

          if (integer_bound_set) {
            int width_value = i_width.getBound_value(null);
            type_name += ("_" + width_value);
          }
        }

        boolean fixed_present = string_type.testFixed_width(null);

        if (fixed_present) {
          boolean fixed_value = string_type.getFixed_width(null);

          if (fixed_value) {
            type_name += "_FIXED";
          }
        }
      }
			*/
    } else if (rt instanceof EData_type) {
      type_name += "OF GENERIC";
    } else {
      System.out.println(
            "RG: constructing type names, not yet supported element type in aggregate: " + rt);
      type_name += "OF _UNSUPPORTED_";
    }

    return type_name;
  }

  String constructNameWithAggregatePackage(String aggr_prefix, EEntity ee)
                                    throws SdaiException {
    String a_package = "";
    String a_name = "";

    if (ee instanceof ENamed_type) {
      a_name = ((ENamed_type) ee).getName(null);

      // a_name = a_name.substring(0,1).toUpperCase() + a_name.substring(1).toLowerCase();
    }

//    SdaiModel a_model = ee.findEntityInstanceSdaiModel();

/*
    if (a_model != model) {
      String a_schema_name = getSchema_definitionFromModel(a_model).getName(null);

      if (a_schema_name.equalsIgnoreCase("Sdai_dictionary_schema")) {
        a_package = "jsdai.dictionary.";
      } else {
        a_package = "jsdai.S" + a_schema_name.substring(0, 1).toUpperCase() + 
                    a_schema_name.substring(1).toLowerCase() + ".";
      }
    }
*/
    //      return a_package;
    // the package must be handled differently.
    // return a_package + aggr_prefix + a_name;
//    return "_" + a_name;
    return a_name;
  }




	String getParameterTypeName(EConstraint constraint, SdaiModel map_model) throws SdaiException {
//		String result = "GENERIC_ENTITY";
		String result = "GENERIC";
		EEntity attribute = null;
		EEntity attribute2 = null;
		EEntity_definition parent_entity = null;
		if (constraint instanceof EAttribute_value_constraint) {
			attribute = ((EAttribute_value_constraint)constraint).getAttribute(null);
			if (attribute instanceof EAttribute) {
			  parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
		  	result = parent_entity.getName(null).toLowerCase();
			} else
			if (attribute instanceof EAggregate_member_constraint) {
				attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
				if (attribute2 instanceof EAttribute) {
//						printDebug("-- may not be correct solution");						
			  	parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
			  	result = parent_entity.getName(null).toLowerCase();

				} else {
//						printDebug("-- attribute of aggregate_member_constraint not explicit: " + attribute2 );						
				}
			}
		} else
		if (constraint instanceof EEntity_constraint) {
			attribute = ((EEntity_constraint)constraint).getAttribute(null);
			if (attribute instanceof EAttribute) {
			  parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
		  	result = parent_entity.getName(null).toLowerCase();
			} else
			if (attribute instanceof EAggregate_member_constraint) {
				attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
				if (attribute2 instanceof EAttribute) {
//						printDebug("-- may not be correct solution");						
			  	parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
			  	result = parent_entity.getName(null).toLowerCase();

				} else {
//						printDebug("-- attribute of aggregate_member_constraint not explicit: " + attribute2 );						
				}
			}
		} else 
		if (constraint instanceof EPath_constraint) {
			EEntity element1 = null;
			if (((EPath_constraint)constraint).testElement1(null)) {
				element1 = ((EPath_constraint)constraint).getElement1(null);
				if (element1 instanceof EAttribute) {
				  parent_entity = (EEntity_definition)((EAttribute)element1).getParent(null);
			  	result = parent_entity.getName(null).toLowerCase();
				} else
				if (element1 instanceof EInverse_attribute_constraint) {
					EEntity inverted_attribute = ((EInverse_attribute_constraint)element1).getInverted_attribute(null);
					if (inverted_attribute instanceof EExplicit_attribute) {
						EEntity domain = ((EExplicit_attribute)inverted_attribute).getDomain(null);
						if (domain instanceof EEntity_definition) {
							result = ((EEntity_definition)domain).getName(null).toLowerCase();
						}
					}
				} else
				if (element1 instanceof EEntity_constraint) {
					attribute = ((EEntity_constraint)element1).getAttribute(null);			
					if (attribute instanceof EAttribute) {
					  parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
				  	result = parent_entity.getName(null).toLowerCase();
					} else
					if (attribute instanceof EAggregate_member_constraint) {
						attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
						if (attribute2 instanceof EAttribute) {
//							printDebug("-- may not be correct solution");						
			  			parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
			  			result = parent_entity.getName(null).toLowerCase();

						} else {
//						printDebug("-- attribute of aggregate_member_constraint not explicit: " + attribute2 );						
						}
					}
				} else 
				if (element1 instanceof EAggregate_member_constraint) {
		  		attribute = ((EAggregate_member_constraint)element1).getAttribute(null);
					if (attribute instanceof EAttribute) {
					  parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
				  	result = parent_entity.getName(null).toLowerCase();
					} else
					if (attribute instanceof EAggregate_member_constraint) {
						attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
						if (attribute2 instanceof EAttribute) {
//						printDebug("-- may not be correct solution");						
			  			parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
			  			result = parent_entity.getName(null).toLowerCase();

						} else {
//						printDebug("-- attribute of aggregate_member_constraint not explicit: " + attribute2 );						
						}
					} else
					if (attribute instanceof ESelect_constraint) {
						ESelect_constraint select_constraint = (ESelect_constraint)attribute;
						if (select_constraint.testAttribute(null)) {
							attribute2 = select_constraint.getAttribute(null);
			  			if (attribute2 instanceof EAttribute) {
			  				parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
		  					result = parent_entity.getName(null).toLowerCase();
							}
						}	
					}			
				} else 
				if (element1 instanceof EIntersection_constraint) {

					AConstraint_select subpaths = null;
					if (((EIntersection_constraint)element1).testSubpaths(null)) {
						subpaths = ((EIntersection_constraint)element1).getSubpaths(null);
					} else {
						// unset non-optional
						System.out.println("ERROR: UNSET subpaths in " + element1);
						printError("\t-- ERROR!!! UNSET subpaths : constraint_select");
					}
					// subpaths : SET [2:?] OF constraint_select;
					SdaiIterator subpaths_iter = subpaths.createIterator();
					// at least two members must be present
			
					while (subpaths_iter.next()) {
						EEntity subpath_constraint = (EEntity)subpaths.getCurrentMemberObject(subpaths_iter);
						if (subpath_constraint instanceof EAttribute) {
							parent_entity = (EEntity_definition)((EAttribute)subpath_constraint).getParent(null);
					  	result = parent_entity.getName(null).toLowerCase();
							break;
						}
					}
				}
			} else {
				// ERROR - not OPTIONAL - must be set
				// just leave it GENERIC_ENTITY for now, it is reported anyway
			}
			
		} else
		if (constraint instanceof EAggregate_member_constraint) {

			EAggregate_member_constraint aggregate_member_constraint = (EAggregate_member_constraint)constraint;
			if (aggregate_member_constraint.testAttribute(null)) {
				attribute = aggregate_member_constraint.getAttribute(null);
				printAttribute("\t-- attribute : aggregate_member_constraint_select = " + attribute);
			} else {
				// ERROR - not OPTIONAL - must be set
				System.out.println("ERROR: UNSET attribute in " + constraint);
				printError("\t-- ERROR!!! UNSET attribute : aggregate_member_constraint_select");
			}

			if (attribute instanceof EAttribute) {
			  parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
		  	result = parent_entity.getName(null).toLowerCase();
			} else
			if (attribute instanceof EInverse_attribute_constraint) {
			} else
			if (attribute instanceof EAggregate_member_constraint) {
				attribute2 = ((EAggregate_member_constraint)attribute).getAttribute(null);
				if (attribute2 instanceof EAttribute) {
//						printDebug("-- may not be correct solution");						
			  	parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
			  	result = parent_entity.getName(null).toLowerCase();

				} else {
//						printDebug("-- attribute of aggregate_member_constraint not explicit: " + attribute2 );						
						printDebug("-- attribute - aggregate_member_constraint - its attribute not attribute, not yet supported in parameter");						
				}
			} else
			if (attribute instanceof EEntity_constraint) {
						printDebug("-- attribute - entity_constraint - not yet supported in parameter");						
			} else
			if (attribute instanceof ESelect_constraint) {
//						printDebug("-- attribute - select_constraint - not yet supported in parameter");						

				ESelect_constraint select_constraint = (ESelect_constraint)attribute;
				if (select_constraint.testAttribute(null)) {
					attribute2 = select_constraint.getAttribute(null);
			  	if (attribute2 instanceof EAttribute) {
			  		parent_entity = (EEntity_definition)((EAttribute)attribute2).getParent(null);
		  			result = parent_entity.getName(null).toLowerCase();
					}
				}	

			} else {
				// should not happen
				System.out.println("ERROR: incorrect attribute type in aggregate_member_constraint: " + attribute);
				printError("\t-- ERROR!!! incorrect attribute type in aggregate_member_constraint: " + attribute);
			}
		} else
		if (constraint instanceof EInstance_constraint) {
			EInstance_constraint instance_constraint = (EInstance_constraint)constraint;
			EEntity element1 = null;
			if (instance_constraint.testElement1(null)) {
				element1 = instance_constraint.getElement1(null);
			} else {
			// ERROR - not OPTIONAL - must be set
				System.out.println("ERROR: UNSET element1 in " + constraint);
				printError("\t-- ERROR!!! UNSET element1 : constraint_select");
			}
			if (element1 instanceof EAttribute) {
			  parent_entity = (EEntity_definition)((EAttribute)element1).getParent(null);
		  	result = parent_entity.getName(null).toLowerCase();
			}
		} else
		if (constraint instanceof EEnd_of_path_constraint) { 
			EEnd_of_path_constraint end_of_path_constraint = (EEnd_of_path_constraint)constraint;
			EEntity constraints = null;
			if (end_of_path_constraint.testConstraints(null)) {
				constraints = end_of_path_constraint.getConstraints(null);
			} else {
			// ERROR - not OPTIONAL - must be set
				System.out.println("ERROR: UNSET constraints in " + constraint);
				printError("\t-- ERROR!!! UNSET constraints : constraint_select");
			}
			if (constraints instanceof EAttribute) {
			  parent_entity = (EEntity_definition)((EAttribute)constraints).getParent(null);
		  	result = parent_entity.getName(null).toLowerCase();
			}
		} else
		if (constraint instanceof EType_constraint) { 
			EType_constraint type_constraint = (EType_constraint)constraint;
			if (type_constraint.testConstraints(null)) {
				EEntity constraints = type_constraint.getConstraints(null);
				if (constraints instanceof EAttribute) {
			  	parent_entity = (EEntity_definition)((EAttribute)constraints).getParent(null);
//		  		result = parent_entity.getName(null).toLowerCase();
				}
	 		} 
		} else
		if (constraint instanceof ESelect_constraint) { 
			ESelect_constraint select_constraint = (ESelect_constraint)constraint;
			if (select_constraint.testAttribute(null)) {
				attribute = select_constraint.getAttribute(null);
			  if (attribute instanceof EAttribute) {
			  	parent_entity = (EEntity_definition)((EAttribute)attribute).getParent(null);
		  		result = parent_entity.getName(null).toLowerCase();
				}
			}	
		
		}
		return result;
	}



	String getReturnTypeName(EConstraint constraint, SdaiModel map_model) throws SdaiException {
		String result = "GENERIC";
		EEntity attribute = null;
		EEntity_definition parent_entity = null;

/*
		if (constraint instanceof EAttribute_value_constraint) {
			attribute = ((EAttribute_value_constraint)constraint).getAttribute(null);
			if (attribute instanceof EAttribute) {
			  parent_entity = ((EAttribute)attribute).getParent(null);
		  	result = parent_entity.getName(null).toLowerCase();
			}
		} else
		if (constraint instanceof EEntity_constraint) {
			attribute = ((EEntity_constraint)constraint).getAttribute(null);
			if (attribute instanceof EAttribute) {
			  parent_entity = ((EAttribute)attribute).getParent(null);
		  	result = parent_entity.getName(null).toLowerCase();
			}
		} else 
*/	
		if (constraint instanceof EPath_constraint) {
			EEntity element1 = null;
			if (((EPath_constraint)constraint).testElement1(null)) {
				element1 = ((EPath_constraint)constraint).getElement1(null);
				/*
				if (element1 instanceof EAttribute) {
				  parent_entity = ((EAttribute)element1).getParent(null);
			  	result = parent_entity.getName(null).toLowerCase();
				} else
				*/
				if (element1 instanceof EInverse_attribute_constraint) {
					EEntity inverted_attribute = ((EInverse_attribute_constraint)element1).getInverted_attribute(null);
					if (inverted_attribute instanceof EExplicit_attribute) {
						EEntity domain = ((EExplicit_attribute)inverted_attribute).getDomain(null);
						if (domain instanceof EEntity_definition) {
							result = ((EEntity_definition)domain).getName(null).toLowerCase();
						}
					}
				}
			} else {
				// ERROR - not OPTIONAL - must be set
				// just leave it GENERIC for now, it is reported anyway
			}
			
		}
		return result;
	}

	void loopARM(SdaiModel arm_model, SdaiModel aim_model, SdaiModel map_model) throws SdaiException {

		ESchema_definition schema = getSchema_definitionFromModel(arm_model);

		pw.println("\n(*");
		pw.println("\tGenerated by " + rg_title);
		pw.println("\t" + rg_copyright);
		pw.println("\tversion " + rg_version_major + "." + rg_version_middle + "." + rg_version_minor + " build " + rg_build + ", " + rg_date );
		pw.println("*)\n");
		

		pw.println("SCHEMA x;\n");
//		pw.println("USE FROM electronic_assembly_interconnect_and_packaging_design;\n");
		pw.println("USE FROM " + AIM_schema_name.toLowerCase() + ";\n");

		// these USE FROMs are specific to the current set of short form ap210 only, adding for now
		// they are needed because some entities/types are interfaced to ap210 aim by REFERENCE FROM
		// and therefore their interfacing is not chained into X, but they are referenced in X
 
	 	pw.println("USE FROM basic_attribute_schema (");
			pw.println("\tobject_role);\n");

		pw.println("USE FROM date_time_schema (");
			pw.println("\tcoordinated_universal_time_offset,");
			pw.println("\tlocal_time);\n");

		pw.println("USE FROM geometric_model_schema (");
			pw.println("\tgeometric_set_select,");
			pw.println("\tswept_area_solid);\n");

		pw.println("USE FROM management_resources_schema (");
			pw.println("\tidentification_role);\n");

		pw.println("USE FROM measure_schema (");
			pw.println("\tunit);\n");

		pw.println("USE FROM presentation_appearance_schema (");
			pw.println("\tfill_area_style_tile_shape_select,");
			pw.println("\tfill_style_select,");
			pw.println("\tpresentation_style_assignment,");
			pw.println("\tpresentation_style_select);\n");

		pw.println("USE FROM product_definition_schema(");
			pw.println("\tproduct_definition_effectivity);\n");
	
		pw.println("USE FROM product_property_definition_schema (");
			pw.println("\tcharacterized_definition);\n");	

		pw.println("USE FROM qualified_measure_schema ("); 
			pw.println("\tuncertainty_qualifier,");
			pw.println("\tstandard_uncertainty,");
			pw.println("\tvalue_qualifier);\n");

		pw.println("REFERENCE FROM electronic_assembly_interconnect_and_packaging_design (");
			pw.println("\tbag_to_set,");
			pw.println("\tinstance_unique);\n");
 
 
		pw.println("TYPE check_mode = ENUMERATION OF (");
				pw.println("\t\tbasic,"); 
				pw.println("\t\tmandatory,"); 
  			pw.println("\t\tdetailed,"); 
  			pw.println("\t\trecursive");
			pw.println("\t);");
		pw.println("END_TYPE;\n");
 
 
 	 pw.println("\nPROCEDURE print(par: GENERIC);");
 	 pw.println("END_PROCEDURE;");
 	 pw.println("\nPROCEDURE println(par: GENERIC);");
 	 pw.println("END_PROCEDURE;\n");
 
 
// 		pw.println("ENTITY UNKNOWN_inverted_attribute_parent_entity_name;");
//			pw.println("\tUNKNOWN__inverted_attribute_name : GENERIC;");
//		pw.println("END_ENTITY;\n");



		if (flag_root) {

			// unfortunately, here we have to work on the dictionary, no help from lang	
		
		}
 
 
		AEntity_declaration entities = schema.getEntity_declarations(null, null);
		SdaiIterator iter2 = entities.createIterator();
		while (iter2.next()) {
			EEntity_definition arm_entity = (EEntity_definition)entities.getCurrentMember(iter2).getDefinition(null);
      if (!(arm_entity.getInstantiable(null))) {
System.out.println("ABSTRACT ARM entity, skipped: " + arm_entity.getName(null));
      	continue;
      }
			if (flag_on_demand) {
				String current_arm_entity_name = arm_entity.getName(null);
				if (flag_many_on_demand) {
					boolean proceed_with_this_entity = false;
					for (int k = 0; k < ARM_entities.size(); k++) {
						String current_requested = (String) ARM_entities.elementAt(k);
						if (current_arm_entity_name.equalsIgnoreCase(current_requested)) {
							proceed_with_this_entity = true;
							break;
						}	
					}
					if (!proceed_with_this_entity) {
						continue;
					}
				} else {
					if (!current_arm_entity_name.equalsIgnoreCase(ARM_entity_name_requested)) {
						continue;
					}
				}
			}			
			AEntity all_entity_mapping = map_model.getEntityExtentInstances(EEntity_mapping.class);
			Vector own_entity_mapping = new Vector();
			SdaiIterator iter = all_entity_mapping.createIterator();
			while (iter.next()) {
				EEntity_mapping entity_mapping  = (EEntity_mapping)all_entity_mapping.getCurrentMemberObject(iter);
				EEntity_definition source = entity_mapping.getSource(null);
				if (source == arm_entity) {
					own_entity_mapping.addElement(entity_mapping);
				}
			}
			for (int i = 0; i < own_entity_mapping.size(); i++) {
				EEntity target = ((EEntity_mapping)own_entity_mapping.elementAt(i)).getTarget(null);
//				printRule1(arm_entity, target, i, own_entity_mapping.size());			

				printBatchForRules(arm_entity, target, i, own_entity_mapping.size());	
				printRule2(arm_entity, target, i, own_entity_mapping.size());			
				pw.println();
			}
		
		}
		entities = schema.getEntity_declarations(null, null);
		iter2 = entities.createIterator();
		while (iter2.next()) {
			EEntity_definition arm_entity = (EEntity_definition)entities.getCurrentMember(iter2).getDefinition(null);
      if (!(arm_entity.getInstantiable(null))) {
// System.out.println("ABSTRACT ARM entity, skipped: " + arm_entity.getName(null));
// need functions for attributes to be invoked from subtypes, either generate all, or make on demand
// the simplest way - to generate all, including the method for the entity itself which is clearly not needed.
//      	continue;
      }
			if (flag_on_demand) {
				String current_arm_entity_name = arm_entity.getName(null);
				if (flag_many_on_demand) {
					boolean proceed_with_this_entity = false;
					for (int k = 0; k < ARM_entities.size(); k++) {
						String current_requested = (String) ARM_entities.elementAt(k);
						if (current_arm_entity_name.equalsIgnoreCase(current_requested)) {
							proceed_with_this_entity = true;
							break;
						}	
					}
					if (!proceed_with_this_entity) {
						continue;
					}
				} else {
					if (!current_arm_entity_name.equalsIgnoreCase(ARM_entity_name_requested)) {
						continue;
					}
				}
			}			
			printDebug("\n-- for ARM entity " + arm_entity.getName(null) + "\n");
			AEntity all_entity_mapping = map_model.getEntityExtentInstances(EEntity_mapping.class);
			Vector own_entity_mapping = new Vector();
			SdaiIterator iter = all_entity_mapping.createIterator();
			while (iter.next()) {
				EEntity_mapping entity_mapping  = (EEntity_mapping)all_entity_mapping.getCurrentMemberObject(iter);
				EEntity_definition source = entity_mapping.getSource(null);
				if (source == arm_entity) {
					own_entity_mapping.addElement(entity_mapping);
				}
			}
			for (int i = 0; i < own_entity_mapping.size(); i++) {
				EEntity_mapping the_entity_mapping = (EEntity_mapping)own_entity_mapping.elementAt(i);
//				printFunction1(arm_entity, the_entity_mapping, i, own_entity_mapping.size());			

      if (arm_entity.getInstantiable(null)) {

				printFunction2(arm_entity, the_entity_mapping, i, own_entity_mapping.size(), arm_model, aim_model, map_model);			

			} else {

				printFunction2NoFunction(arm_entity, the_entity_mapping, i, own_entity_mapping.size(), arm_model, aim_model, map_model);			
			 

			}
				pw.println();
			}
		}
//		pw.println("END_SCHEMA; -- x");
	
	}


	void printFunction2NoFunction(EEntity_definition arm_entity, EEntity_mapping the_entity_mapping, int i, int count, SdaiModel arm_model, SdaiModel aim_model, SdaiModel map_model) throws SdaiException {

		Vector result = new Vector();
		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		indentation = 0;
		EEntity aim_target = the_entity_mapping.getTarget(null);
		if (count > 1) {
			arm_entity_name += "_" + (i + 1);
		}
		
		String aim_entity_name = "_NONAME_";
		if (aim_target instanceof EEntity_definition) {
			if (((EEntity_definition)aim_target).getComplex(null)) {
				// complex entity
//				System.out.println("complex entity: " + aim_target);
				aim_entity_name = getRootSupertypeName((EEntity_definition)aim_target);
			} else {
				aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase();
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '$');
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '_');
			}
		} else {
			aim_entity_name = "_ATTRIBUTE_";
		}


boolean empty_if = true;
		// go through mandatory attributes
		Vector arm_attributes = new Vector();
		Aggregate attributes = arm_model.getEntityExtentInstances(EAttribute.class);
		SdaiIterator iter = attributes.createIterator();
//		Aggregate attribute_mappings = map_model.getEntityExtentInstances(EAttribute_mapping.class);
		Aggregate attribute_mappings = map_model.getEntityExtentInstances(EGeneric_attribute_mapping.class);
		SdaiIterator iter_map;
		while (iter.next()) {
			EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
			EEntity_definition parent = (EEntity_definition)an_attribute.getParent(null);
 			if (parent != arm_entity) continue;
			Vector attribute_mapping_alternatives = new Vector();
			iter_map = attribute_mappings.createIterator();
			while (iter_map.next()) {
//				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
				EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
				EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
				if (an_entity_mapping == the_entity_mapping) {
					EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
					if (an_attribute == a_mapped_attribute) {
						attribute_mapping_alternatives.addElement(an_attribute_mapping);
					}
				}	
			}	

			if (attribute_mapping_alternatives.size() > 0) {
				RuleGeneratorArmAttribute arm_attribute = new RuleGeneratorArmAttribute(an_attribute, attribute_mapping_alternatives);
				arm_attributes.addElement(arm_attribute);
			}
			
			
			
		
			// probably no need to generate anything for ARM attributes that do not have attirbute_mapping at all.
			// old version, to be removed
//			if (attribute_mapping_alternatives.size() > 0) {
			if (attribute_mapping_alternatives.size() == 1234567890) {
				// assume that all are stron or not strong. This assumption is not safe, though !!!!!!!!!!!!!!!!
				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mapping_alternatives.elementAt(0);
				if (an_attribute instanceof EExplicit_attribute) {
					EExplicit_attribute xa = (EExplicit_attribute)an_attribute;
//					if (!xa.getOptional_flag(null)) {
//       			pw.println("\t\tsize := (get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e,a));");
//       			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						empty_if = false;
				} 
			}
		}
		




				result = generateSingleAttribute_mappingFunctions(arm_attributes, arm_entity_name, aim_entity_name, result);
				if (result != null) {
					Iterator fxx_iterator = result.iterator();
					while (fxx_iterator.hasNext()) {
						EEntity next_constraint = (EEntity)fxx_iterator.next();
// System.out.println("--REMOVE - generate function: " + next_constraint);
// printDebug("--REMOVE - generate function: " + next_constraint);
						generateFxxFunction(next_constraint, map_model);
					}
				} 


}




	// version 1
	void printRule1(EEntity_definition arm_entity, EEntity aim_target, int i, int count) throws SdaiException {
		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		if (count > 1) {
			arm_entity_name += "_" + (i + 1);
		}
		
		boolean is_complex = false;
		Vector complex_part_names = null;
		String aim_entity_name = "_NONAME_";
		if (aim_target instanceof EEntity_definition) {
			if (((EEntity_definition)aim_target).getComplex(null)) {
				// complex entity
				is_complex = true;
				complex_part_names = new Vector();
		    AEntity_or_view_definition its_supertypes = ((EEntity_definition)aim_target).getGeneric_supertypes(null);
    		SdaiIterator iter_super = its_supertypes.createIterator();
		    while (iter_super.next()) {
      		EEntity_definition super_entity = (EEntity_definition) its_supertypes.getCurrentMember(iter_super);
					String super_name = super_entity.getName(null);
					complex_part_names.addElement(super_name);
				}
// 				System.out.println("complex entity: " + aim_target);
				aim_entity_name = getRootSupertypeName((EEntity_definition)aim_target);
			} else {
				aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase();
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '$');
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '_');
			}
		} else {
			aim_entity_name = "_ATTRIBUTE_";
		}
		pw.println("RULE check_" + arm_entity_name + " FOR (" + aim_entity_name + ");");
		pw.println("WHERE");
		if (is_complex) {
			pw.println("\tWR1: SIZEOF (QUERY (each <* " + aim_entity_name + " |");
			boolean first_time = true;
			Iterator it_super = complex_part_names.iterator();						
			String rule_schema_name = AIM_schema_name.toUpperCase();
			String a_line;
			while (it_super.hasNext()) {
				String a_name = ((String)it_super.next()).toUpperCase();
				a_line = "\t\t";
				if (!first_time) {
					a_line += "AND ";
				} else {
					first_time = false;
				}
//				a_line += "(\'" + rule_schema_name + ".\' + \'" + a_name + "\' IN TYPEOF(each))"; 
				a_line += "(\'" + AIM_schema_name + ".\' + \'" + a_name + "\' IN TYPEOF(each))"; 
				pw.println(a_line);
			}
			a_line = "\t\tAND NOT (is_" + arm_entity_name + "(each)))) = 0;";
			pw.println(a_line);
		} else {
			pw.println("\tWR1: SIZEOF (QUERY (each <* " + aim_entity_name + " | NOT (is_" + arm_entity_name + "(each)))) = 0;");
		}

		pw.println("END_RULE;");
		
	}


	void printBatchForRules(EEntity_definition arm_entity, EEntity aim_target, int i, int count) throws SdaiException {
		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		if (count > 1) {
			arm_entity_name += "_" + (i + 1);
		}

		String to_print = "java  -D\"jsdai.properties=.\" jsdai.tools.RuleAndFunctionValidator -verbose -rule rule__" + arm_entity_name + " -p21 design5.stp  >> _LOG_Multiple_Launch 2>>_LOG_Multtiple_Lautch_EXEPTIONS";
		pw1.println(to_print);
	}


	// version 2
	void printRule2(EEntity_definition arm_entity, EEntity aim_target, int i, int count) throws SdaiException {
		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		if (count > 1) {
			arm_entity_name += "_" + (i + 1);
		}
		
		String aim_entity_name = "_NONAME_";
		boolean is_complex = false;
		Vector complex_part_names = null;
		if (aim_target instanceof EEntity_definition) {
			if (((EEntity_definition)aim_target).getComplex(null)) {
				// complex entity
				is_complex = true;
				complex_part_names = new Vector();
			  AEntity_or_view_definition its_supertypes = ((EEntity_definition)aim_target).getGeneric_supertypes(null);
    		SdaiIterator iter_super = its_supertypes.createIterator();
		    while (iter_super.next()) {
      		EEntity_definition super_entity = (EEntity_definition) its_supertypes.getCurrentMember(iter_super);
					String super_name = super_entity.getName(null);
					complex_part_names.addElement(super_name);
				}
//				System.out.println("complex entity: " + aim_target);
				aim_entity_name = getRootSupertypeName((EEntity_definition)aim_target);
			} else {
				aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase();
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '$');
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '_');
			}
		} else {
			aim_entity_name = "_ATTRIBUTE_";
		}
		pw.println("RULE rule__" + arm_entity_name + " FOR (" + aim_entity_name + ");");
		pw.println("WHERE");
		
		// moved the complex stuff from the rule into the function
		is_complex = false;
		if (is_complex) {
			pw.println("\tWR1: SIZEOF (QUERY (i <* " + aim_entity_name + " |");
			boolean first_time = true;
			Iterator it_super = complex_part_names.iterator();						
			String rule_schema_name = AIM_schema_name.toUpperCase();
			String a_line;
			while (it_super.hasNext()) {
				String a_name = ((String)it_super.next()).toUpperCase();
				a_line = "\t\t";
				if (!first_time) {
					a_line += "AND ";
				} else {
					first_time = false;
				}
//				a_line += "(\'" + rule_schema_name + ".\' + \'" + a_name + "\' IN TYPEOF(i))"; 
				a_line += "(\'" + AIM_schema_name + ".\' + \'" + a_name + "\' IN TYPEOF(i))"; 
				pw.println(a_line);
			}
			a_line = "\t\tAND (check__" + arm_entity_name + "(i, mandatory) = FALSE))) = 0;";
			pw.println(a_line);
		} else {
			pw.println("\tWR1: SIZEOF (QUERY (i <* " + aim_entity_name + " | check__" + arm_entity_name + "(i, mandatory) = FALSE)) = 0;");
		}
		pw.println("END_RULE;");
		
	}


	static String getRootSupertypeName(EEntity_definition cx) throws SdaiException {
    HashSet result = new HashSet();
		getLeafSupertypes(cx, result);
		pruneSupertypes(result);
		EEntity_definition root = findCommonRoot(result);
		if (root != null) {
			return root.getName(null).toLowerCase();
		} else {
			return "_NO_ROOT_SUPERTYPE_";
		} 
		
	}

  static void getLeafSupertypes(EEntity_definition leaf, HashSet result) throws SdaiException {
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

  static EEntity_definition findCommonRoot(HashSet leaves_ed) throws SdaiException {
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

		EEntity_definition a_root = null;
		EEntity_definition a_root2 = null;
 // 	System.out.println("complex entity with " + roots.size() + " root(s):");
		HashSet roots2 = new HashSet(roots);
		Iterator iter_root2;
		if (roots.size() > 1) {
			// try at least to eliminate supertypes from X
			Iterator iter_root = roots.iterator();
			while (iter_root.hasNext()) {
				a_root = (EEntity_definition)iter_root.next();
//				System.out.println("root supertype: " + a_root.getName(null));
				HashSet my_supertypes = new HashSet();
				getLeafSupertypes(a_root, my_supertypes);
				// check if any of other members from roots are in my_supertypes, if so, remove them.
				iter_root2 = roots.iterator();
				while (iter_root2.hasNext()) {
					a_root2 = (EEntity_definition)iter_root2.next();
					if (a_root2 == a_root) continue;
					if (my_supertypes.contains(a_root2)) {
						roots2.remove(a_root2);
//  	System.out.println("removed from supertypes: " + a_root2.getName(null));
					}
				}			
				// break;
				
			}
			
			iter_root2 = roots2.iterator();
			while (iter_root2.hasNext()) {
				a_root = (EEntity_definition)iter_root2.next();
			}	
			if (roots2.size() > 1) {
				System.out.println("Warning: more than one root supertype, taken the last one"); 
	  	} else 
	  	if (roots2.size() < 1) {
				System.out.println("Warning: root supertype not found"); 
    	} else {
//				System.out.println("Warning: number of root supertypes reduced to 1");     	
    	}
		} else {
			Iterator iter_root = roots.iterator();
			while (iter_root.hasNext()) {
				a_root = (EEntity_definition)iter_root.next();
			}			
		}
		
// System.out.println("Warning: complex entity replaced by the common supertype: " + a_root);
		return a_root;
  }


  static void pruneSupertypes(HashSet leaves_ed) throws SdaiException {
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
    }
  }

	// version 1
	void printFunction1(EEntity_definition arm_entity, EEntity_mapping the_entity_mapping, int i, int count) throws SdaiException {
		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		indentation = 0;
		EEntity aim_target = the_entity_mapping.getTarget(null);
		if (count > 1) {
			arm_entity_name += "_" + (i + 1);
		}
		
		String aim_entity_name = "_NONAME_";
		if (aim_target instanceof EEntity_definition) {
			if (((EEntity_definition)aim_target).getComplex(null)) {
				// complex entity
//				System.out.println("complex entity: " + aim_target);
				aim_entity_name = getRootSupertypeName((EEntity_definition)aim_target);
			} else {
				aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase();
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '$');
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '_');
			}
		} else {
			aim_entity_name = "_ATTRIBUTE_";
		}
		pw.println("FUNCTION is_" + arm_entity_name + "(e : " + aim_entity_name + ") : boolean;");

		EEntity constraints = null;
		if (the_entity_mapping.testConstraints(null)) {
			constraints = the_entity_mapping.getConstraints(null);
			processNext(constraints);
		} else {
			// perhaps no need to generate functions and rules at all if no constraints?
//			System.out.println("no constraints for ARM entity " + arm_entity_name);
			printDebug("\t-- no constraints: " + constraints);
		} 
		pw.println("\tRETURN (TRUE);");
		pw.println("END_FUNCTION;");
		
	}


		Vector generateAttribute_mappingFunctions(Vector arm_attributes, String arm_entity_name, String aim_entity_name, Vector result) throws SdaiException {
		  // RuleGeneratorArmAttribute arm_attribute
			Iterator attribute_iterator = arm_attributes.iterator();
			// pw.println("\t-- ");
			while (attribute_iterator.hasNext()) {
				RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();
				String the_attribute_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
				Iterator alternative_iterator = arm_attribute.mapping_alternatives.iterator();
				int alternative_count = 1;
				while (alternative_iterator.hasNext()) {
					EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)alternative_iterator.next();
			
					// if no constraints, genernate nothing? is it possible?
          if ( an_attribute_mapping.testConstraints(null)) {
	
						if (arm_attribute.mapping_alternatives.size() > 1) {
//							pw.println("\nFUNCTION get__" + arm_entity_name + "__" + the_attribute_name + "_" + alternative_count + "(e : " + aim_entity_name + ") : BAG OF GENERIC;");
							pw.println("\nFUNCTION get__" + arm_entity_name + "__" + the_attribute_name + "_" + alternative_count + "(e : " + aim_entity_name + ") : AGGREGATE OF GENERIC;");
						} else if (arm_attribute.mapping_alternatives.size() == 1) {
//							pw.println("\nFUNCTION get__" + arm_entity_name + "__" + the_attribute_name + "(e : " + aim_entity_name + ") : BAG OF GENERIC;");
							pw.println("\nFUNCTION get__" + arm_entity_name + "__" + the_attribute_name + "(e : " + aim_entity_name + ") : AGGREGATE OF GENERIC;");
						} else {
							// should not happen
						}


						pw.println("LOCAL");
//						pw.println("\tresult_aggregate : BAG OF GENERIC;");
						pw.println("\tresult_aggregate : AGGREGATE OF GENERIC;");
						pw.println("\tresult : GENERIC;");
						pw.println("END_LOCAL;");

						String parameter_type_name = "GENERIC";
						EEntity_definition target_attribute_value_domain = null;
						EEntity target_attribute = null;

            
						EEntity attribute_constraints = null;
							attribute_constraints = an_attribute_mapping.getConstraints(null);
							String function_name = null;
							String attribute_name = null;
							String parent_entity_name = null;
							if (attribute_constraints instanceof EAttribute) {
								attribute_name = ((EAttribute)attribute_constraints).getName(null).toLowerCase();
							  parent_entity_name = ((EAttribute)attribute_constraints).getParent(null).getName(null).toLowerCase();
							} else {
            		function_name = "f" + attribute_constraints.getPersistentLabel().substring(1).toLowerCase();
								result.addElement(attribute_constraints);
							}




            	if (attribute_constraints instanceof EAttribute) {

								pw.println("\tIF EXISTS (e) THEN");
  	          		pw.println("\t\tresult := e\\" + parent_entity_name + "." + attribute_name + ";");
								pw.println("\tEND_IF;");	

							} else {
  	          		pw.println("\tresult := " + function_name + "(e);");
							}
	
							pw.println("\tIF NOT EXISTS(result) THEN");
								pw.println("\t\tresult_aggregate := [];");
								pw.println("\t\tRETURN (result_aggregate);");
							pw.println("\tEND_IF;");

//							pw.println("\tIF 'BAG' IN TYPEOF(result) THEN");
							
							pw.println("\tIF ('BAG' IN TYPEOF(result)) OR ('SET' IN TYPEOF(result)) OR ('LIST' IN TYPEOF(result)) OR ('ARRAY' IN TYPEOF(result)) OR ('AGGREGATE' IN TYPEOF(result)) THEN");
							
							
								pw.println("\t\tRETURN (result);");
							pw.println("\tELSE");
								pw.println("\t\tresult_aggregate := [];");
								// pw.println("\t\tIF EXISTS(result) THEN");
									pw.println("\t\tresult_aggregate := result_aggregate + result;");
								// pw.println("\t\tEND_IF;");	
								pw.println("\t\tRETURN (result_aggregate);");
							pw.println("\tEND_IF;");

						pw.println("END_FUNCTION;");
						alternative_count++;
					} // constraints exist
				} // while mappings
			} // while attributes
	  	return result;
	  }


// single function for all attribute alternatives:

		Vector generateSingleAttribute_mappingFunctions(Vector arm_attributes, String arm_entity_name, String aim_entity_name, Vector result) throws SdaiException {
		  // RuleGeneratorArmAttribute arm_attribute
			Iterator attribute_iterator = arm_attributes.iterator();
			// pw.println("\t-- ");
			while (attribute_iterator.hasNext()) {
				RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();
				String the_attribute_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
				
				pw.println("\nFUNCTION get__" + arm_entity_name + "__" + the_attribute_name + "(e : " + aim_entity_name + ") : AGGREGATE OF GENERIC;");
				pw.println("LOCAL");
//				pw.println("\tresult_aggregate : BAG OF GENERIC;");
				pw.println("\tresult_aggregate : AGGREGATE OF GENERIC := [];");
				pw.println("\tresult : GENERIC;");
				pw.println("END_LOCAL;");
				// pw.println("\n\tresult_aggregate := [];");

				Iterator alternative_iterator = arm_attribute.mapping_alternatives.iterator();
				int alternative_count = 1;
				while (alternative_iterator.hasNext()) {
	
					EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)alternative_iterator.next();
			
					// if no constraints, genernate nothing? is it possible?
          if ( an_attribute_mapping.testConstraints(null)) {
	
						String parameter_type_name = "GENERIC";
						EEntity_definition target_attribute_value_domain = null;
						EEntity target_attribute = null;

            
						EEntity attribute_constraints = null;
						attribute_constraints = an_attribute_mapping.getConstraints(null);
						String function_name = null;
						String attribute_name = null;
						String parent_entity_name = null;
						if (attribute_constraints instanceof EAttribute) {
							attribute_name = ((EAttribute)attribute_constraints).getName(null).toLowerCase();
						  parent_entity_name = ((EAttribute)attribute_constraints).getParent(null).getName(null).toLowerCase();
						} else {
           		function_name = "f" + attribute_constraints.getPersistentLabel().substring(1).toLowerCase();
							result.addElement(attribute_constraints);
						}
           	if (attribute_constraints instanceof EAttribute) {
							pw.println("\tIF EXISTS (e) THEN");
 	          		pw.println("\t\tresult := e\\" + parent_entity_name + "." + attribute_name + ";");
							pw.println("\tEND_IF;");	
						} else {
 	          		pw.println("\tresult := " + function_name + "(e);");
						}
	
						pw.println("\tIF EXISTS(result) THEN");
							pw.println("\t\tresult_aggregate := result_aggregate + result;");
						pw.println("\tEND_IF;");

//							pw.println("\tIF 'BAG' IN TYPEOF(result) THEN");
							
							
							
						alternative_count++;
					} // constraints exist
				} // while mappings
				
				pw.println("\tRETURN (result_aggregate);");
				pw.println("END_FUNCTION;");

			} // while attributes

	  	return result;
	  }



// FUNCTION get__geometric_tolerance__tolerance_zone_or_boundary_definition


 private String indentString(int indent) {
 	StringBuffer sb = new StringBuffer();

   for (int i = 0; i < indent; ++i) {
      sb.append("\t");
   }

   return sb.toString();
 }


	// strong
	
	boolean generateArmStrongAttributeChecks(RuleGeneratorArmAttribute arm_attribute, int alternative_nr, String arm_entity_name) throws SdaiException {
 			
			boolean int_empty_if = true;
			boolean empty_if = true;
			
 			if (arm_attribute.mapping_alternatives.size() < 1) {
 				return true;
 			}
 			boolean go_deeper = false;
 			if (alternative_nr < arm_attribute.mapping_alternatives.size()) {
 				go_deeper = true;
 			}
 			String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
 		 	String size_expression = "\t" + indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
 			if (arm_attribute.mapping_alternatives.size() == 1) {
 				size_expression += "(e));";
 			} else {
 				// must be > 1
 				size_expression += "_" + alternative_nr + "(e));";
 			} 
 		 	alternative_nr++;

 		 
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {

				EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						return true;
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
						if (!xa.getOptional_flag(null)) {
		   				pw.println(size_expression);
							empty_if = false;
							//  [something : ?] - checking only min:
							printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
							pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
								if (go_deeper) {
									int_empty_if = generateArmStrongAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
								} else {
									pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
								}
							pw.println(indentString(alternative_nr) + "END_IF;");
						} else {
							// for optional nothing to check, always ok ?
							printDebug("\t\t-- OPTIONAL [n:?] any size ok - not generated: ");
			   			printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
							return true;
						}
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						if (!xa.getOptional_flag(null)) {
							printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (SIZE > " + arm_attribute.max_cardinality + ") THEN");
						} else {
							printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						}
							if (go_deeper) {
								int_empty_if = generateArmStrongAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
							} else {
								pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
							}
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					if (!xa.getOptional_flag(null)) {
						pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					} else {
						printDebug("\t\t-- OPTIONAL");
						pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
					}
					if (go_deeper) {
						int_empty_if = generateArmStrongAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
					} else {
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					}
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} else // derived attribute, can not be optional
			if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
				
				EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
//				EAttribute xa = (EAttribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						return true;
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
	   				pw.println(size_expression);
						empty_if = false;
						//  [something : ?] - checking only min:
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":?]");
						pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						if (go_deeper) {
							int_empty_if = generateArmStrongAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
						} else {
							pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
						}
						pw.println(indentString(alternative_nr) + "END_IF;");
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
						pw.println("indentString(alternative_nr) + IF (size < " + arm_attribute.min_cardinality + ") OR (SIZE > " + arm_attribute.max_cardinality + ") THEN");
						if (go_deeper) {
							int_empty_if = generateArmStrongAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
						} else {
							pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
						}
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					if (go_deeper) {
						int_empty_if = generateArmStrongAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
					} else {
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					}
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} // derived attribute
	
			return (empty_if && int_empty_if);
	} // function end
	

	// non-strong
	boolean generateArmAttributeChecks(RuleGeneratorArmAttribute arm_attribute, int alternative_nr, String arm_entity_name) throws SdaiException {
 			
			boolean int_empty_if = true;
			boolean empty_if = true;
			
 			if (arm_attribute.mapping_alternatives.size() < 1) {
 				return true;
 			}
 			boolean go_deeper = false;
 			if (alternative_nr < arm_attribute.mapping_alternatives.size()) {
 				go_deeper = true;
 			}
 			String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
 		 	String size_expression = "\t" + indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
 			if (arm_attribute.mapping_alternatives.size() == 1) {
 				size_expression += "(e));";
 			} else {
 				// must be > 1
 				size_expression += "_" + alternative_nr + "(e));";
 			} 
 		 	alternative_nr++;

 		 
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {

				EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						return true;
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
						if (!xa.getOptional_flag(null)) {
		   				pw.println(size_expression);
							empty_if = false;
							//  [something : ?] - checking only min:
							printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
							pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
								if (go_deeper) {
									int_empty_if = generateArmAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
								} else {
									pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
								}
							pw.println(indentString(alternative_nr) + "END_IF;");
						} else {
							// for optional nothing to check, always ok ?
							printDebug("\t\t-- OPTIONAL [n:?] any size ok - not generated: ");
			   			printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
							return true;
						}
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						if (!xa.getOptional_flag(null)) {
							printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (SIZE > " + arm_attribute.max_cardinality + ") THEN");
						} else {
							printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						}
							if (go_deeper) {
								int_empty_if = generateArmAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
							} else {
								pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
							}
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					if (!xa.getOptional_flag(null)) {
						pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					} else {
						printDebug("\t\t-- OPTIONAL");
						pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
					}
					if (go_deeper) {
						int_empty_if = generateArmAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
					} else {
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					}
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} else // derived attribute, can not be optional
			if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
				
				EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
//				EAttribute xa = (EAttribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						return true;
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
	   				pw.println(size_expression);
						empty_if = false;
						//  [something : ?] - checking only min:
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":?]");
						pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						if (go_deeper) {
							int_empty_if = generateArmAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
						} else {
							pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
						}
						pw.println(indentString(alternative_nr) + "END_IF;");
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
						pw.println("indentString(alternative_nr) + IF (size < " + arm_attribute.min_cardinality + ") OR (SIZE > " + arm_attribute.max_cardinality + ") THEN");
						if (go_deeper) {
							int_empty_if = generateArmAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
						} else {
							pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
						}
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					if (go_deeper) {
						int_empty_if = generateArmAttributeChecks(arm_attribute, alternative_nr, arm_entity_name);
					} else {
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					}
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} // derived attribute
	
			return (empty_if && int_empty_if);
	} // function end



  

	// for attribute_mapping_value
	
	// strong
	
	// for attribute_mapping_value
	
	boolean generateArmStrongAttributeChecksValue(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;


		pw.println(indentString(alternative_nr) + "count := 0;");
		for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			

 			if (arm_attribute.mapping_alternatives.size() == 1) {
				size_expression = size_expression_0 + "(e));";
 			} else {
 				// must be > 1
 				size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 			} 
 		 
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
				EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						pw.println(indentString(alternative_nr) + "count := count + 1;");
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
						if (!xa.getOptional_flag(null)) {
		   				pw.println(size_expression);
							empty_if = false;
							//  [something : ?] - checking only min:
							printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
							pw.println(indentString(alternative_nr) + "IF size >= " + arm_attribute.min_cardinality + " THEN");
							pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
							pw.println(indentString(alternative_nr) + "END_IF;");
						} else {
							// for optional nothing to check, always ok ?
							printDebug("\t\t-- OPTIONAL [n:?] any size ok - not generated: ");
			   			printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
							pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						}
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						if (!xa.getOptional_flag(null)) {
							printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF (size >= " + arm_attribute.min_cardinality + ") AND (SIZE <= " + arm_attribute.max_cardinality + ") THEN");
						} else {
							printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF size <= " + arm_attribute.max_cardinality + " THEN");
						}
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					if (!xa.getOptional_flag(null)) {
						pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
					} else {
						printDebug("\t\t-- OPTIONAL");
						pw.println(indentString(alternative_nr) + "IF size <= 1 THEN");
					}
					pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} else // derived attribute, can not be optional
			if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
				EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
//				EAttribute xa = (EAttribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						pw.println(indentString(alternative_nr) + "count := count + 1;");
						return true;
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
	   				pw.println(size_expression);
						empty_if = false;
						//  [something : ?] - checking only min:
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":?]");
						pw.println(indentString(alternative_nr) + "IF size >= " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "END_IF;");
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
						pw.println("indentString(alternative_nr) + IF (size >= " + arm_attribute.min_cardinality + ") AND (SIZE <= " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} // derived attribute
	
		} // loop through all alternatives
		
		pw.println("\t\tIF count <> 1 THEN");
			pw.println("\t\t\tRETURN (TRUE);");
		pw.println("\t\tEND_IF;");	

		
		return (false);
	} // function end

	// strong, for non-aggregate mandatory and optional, attribute_mapping and attribute_mapping_value now the same
	
	boolean generateMandatoryOrOptionalStrongARMAttributeChecks(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;



		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
			if (xa.getOptional_flag(null)) {
 				printDebug("\t\t-- OPTIONAL");
				pw.println(indentString(alternative_nr) + "count := -1;");
			} else {
				pw.println(indentString(alternative_nr) + "count := 0;");
			}
	
			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			
	
 				if (arm_attribute.mapping_alternatives.size() == 1) {
					size_expression = size_expression_0 + "(e));";
 				} else {
 					// must be > 1
 					size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 				} 
 			 
  	 		pw.println(size_expression);
				empty_if = false;
				if (!xa.getOptional_flag(null)) {
					pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
					pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
					pw.println(indentString(alternative_nr) + "END_IF;");
				} else {

					pw.println(indentString(alternative_nr) + "IF (size = 0) AND (count < 0) THEN");
					pw.println(indentString(alternative_nr) + "\tcount := 0;");
					pw.println(indentString(alternative_nr) + "END_IF;");

					pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
						pw.println(indentString(alternative_nr) + "\tIF count < 0 THEN");
							pw.println(indentString(alternative_nr) + "\t\tcount := 1;");
						pw.println(indentString(alternative_nr) + "\tELSE;");
							pw.println(indentString(alternative_nr) + "\t\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "\tEND_IF;");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
				
				
			} // for, if explicit	

			if (xa.getOptional_flag(null)) {
				pw.println("\t\tIF (count < 0) OR (count > 1) THEN");
					pw.println("\t\t\tRETURN (TRUE);");
				pw.println("\t\tEND_IF;");	
			} else {
				pw.println("\t\tIF count <> 1 THEN");
					pw.println("\t\t\tRETURN (TRUE);");
				pw.println("\t\tEND_IF;");	
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;

			pw.println(indentString(alternative_nr) + "count := 0;");
	
			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			

 				if (arm_attribute.mapping_alternatives.size() == 1) {
					size_expression = size_expression_0 + "(e));";
 				} else {
 					// must be > 1
 					size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 				} 

 				pw.println(size_expression);
				empty_if = false;
				pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
					pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
				pw.println(indentString(alternative_nr) + "END_IF;");
	
			} // loop through all alternatives
		
			pw.println("\t\tIF count <> 1 THEN");
				pw.println("\t\t\tRETURN (TRUE);");
			pw.println("\t\tEND_IF;");	
		
		} // derived
		
		return (false);
	} // function end
		

	// non-strong, for non-aggregate mandatory and optional, attribute_mapping and attribute_mapping_value now the same

	boolean generateMandatoryOrOptionalARMAttributeChecks(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;



		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
			if (xa.getOptional_flag(null)) {
 				printDebug("\t\t-- OPTIONAL");
				pw.println(indentString(alternative_nr) + "count := -1;");
			} else {
				pw.println(indentString(alternative_nr) + "count := 0;");
			}
	
			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			
	
 				if (arm_attribute.mapping_alternatives.size() == 1) {
					size_expression = size_expression_0 + "(e));";
 				} else {
 					// must be > 1
 					size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 				} 
 			 
  	 		pw.println(size_expression);
				empty_if = false;
				if (!xa.getOptional_flag(null)) {
					pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
					pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
					pw.println(indentString(alternative_nr) + "END_IF;");
				} else {

					pw.println(indentString(alternative_nr) + "IF (size = 0) AND (count < 0) THEN");
					pw.println(indentString(alternative_nr) + "\tcount := 0;");
					pw.println(indentString(alternative_nr) + "END_IF;");

					pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
						pw.println(indentString(alternative_nr) + "\tIF count < 0 THEN");
							pw.println(indentString(alternative_nr) + "\t\tcount := 1;");
						pw.println(indentString(alternative_nr) + "\tELSE;");
							pw.println(indentString(alternative_nr) + "\t\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "\tEND_IF;");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
				
				
			} // for, if explicit	

			if (xa.getOptional_flag(null)) {
				pw.println("\t\tIF (count < 0) OR (count > 1) THEN");
					pw.println("\t\t\tRETURN (FALSE);");
				pw.println("\t\tEND_IF;");	
			} else {
				pw.println("\t\tIF count <> 1 THEN");
					pw.println("\t\t\tRETURN (FALSE);");
				pw.println("\t\tEND_IF;");	
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;

			pw.println(indentString(alternative_nr) + "count := 0;");
	
			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			

 				if (arm_attribute.mapping_alternatives.size() == 1) {
					size_expression = size_expression_0 + "(e));";
 				} else {
 					// must be > 1
 					size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 				} 

 				pw.println(size_expression);
				empty_if = false;
				pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
					pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
				pw.println(indentString(alternative_nr) + "END_IF;");
	
			} // loop through all alternatives
		
			pw.println("\t\tIF count <> 1 THEN");
				pw.println("\t\t\tRETURN (FALSE);");
			pw.println("\t\tEND_IF;");	
		
		} // derived
		
		return (false);
	} // function end
	
	// non-strong
	boolean generateArmAttributeChecksValue(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;


		pw.println(indentString(alternative_nr) + "count := 0;");
		for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			

 			if (arm_attribute.mapping_alternatives.size() == 1) {
				size_expression = size_expression_0 + "(e));";
 			} else {
 				// must be > 1
 				size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 			} 
 		 
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
				EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						pw.println(indentString(alternative_nr) + "count := count + 1;");
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
						if (!xa.getOptional_flag(null)) {
		   				pw.println(size_expression);
							empty_if = false;
							//  [something : ?] - checking only min:
							printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
							pw.println(indentString(alternative_nr) + "IF size >= " + arm_attribute.min_cardinality + " THEN");
							pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
							pw.println(indentString(alternative_nr) + "END_IF;");
						} else {
							// for optional nothing to check, always ok ?
							printDebug("\t\t-- OPTIONAL [n:?] any size ok - not generated: ");
			   			printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
							pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						}
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						if (!xa.getOptional_flag(null)) {
							printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF (size >= " + arm_attribute.min_cardinality + ") AND (SIZE <= " + arm_attribute.max_cardinality + ") THEN");
						} else {
							printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println(indentString(alternative_nr) + "IF size <= " + arm_attribute.max_cardinality + " THEN");
						}
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					if (!xa.getOptional_flag(null)) {
						pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
					} else {
						printDebug("\t\t-- OPTIONAL");
						pw.println(indentString(alternative_nr) + "IF size <= 1 THEN");
					}
					pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} else // derived attribute, can not be optional
			if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
				EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
//				EAttribute xa = (EAttribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						pw.println(indentString(alternative_nr) + "count := count + 1;");
						return true;
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
	   				pw.println(size_expression);
						empty_if = false;
						//  [something : ?] - checking only min:
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":?]");
						pw.println(indentString(alternative_nr) + "IF size >= " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "END_IF;");
					} else {
	   				pw.println(size_expression);
						empty_if = false;
						// both defined
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
						pw.println("indentString(alternative_nr) + IF (size >= " + arm_attribute.min_cardinality + ") AND (SIZE <= " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
						pw.println(indentString(alternative_nr) + "END_IF;");
					}
				} else {
   				pw.println(size_expression);
					empty_if = false;
					pw.println(indentString(alternative_nr) + "IF size = 1 THEN");
						pw.println(indentString(alternative_nr) + "\tcount := count + 1;");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			} // derived attribute
	
		} // loop through all alternatives
		
		pw.println("\t\tIF count <> 1 THEN");
			pw.println("\t\t\tRETURN (FALSE);");
		pw.println("\t\tEND_IF;");	

		
		return (false);
	} // function end

// strong, aggregate, single function attribute alternatives


	boolean generateAggregateStrongARMAttributeChecksSingleFunction(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
//		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
//		String size_expression;

		String result_expression = indentString(alternative_nr) + "results := get__" + arm_entity_name  + "__" + attr_name + "(e);";



	  pw.println(result_expression);
		
		pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
		
		
	/*
		the logic to check cardinality:
		
		if not array
		
		[min : max], min != 0 - must be  min <= size <= max                    -> IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : max], min != 0 - size must be  min <= size <= max or 0 -> IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
		
		[0 : max], min == 0 - must be size <= max          ->  IF size > max THEN RETURN (FALSE); END_IF;
		OPTIONAL [0 : max], min == 0 - must be size <= max ->  IF size > max THEN RETURN (FALSE); END_IF;

		[min : ?], min != 0 - must be size >= min -> IF size < min THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : ?], min != 0,  must be size >= min or size = 0 ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;

		[0 : ?], min == 0          - any size is ok, nothing to check, never returns FALSE
		OPTIONAL [0 : ?], min == 0 - any size is ok, nothing to check, never returns FALSE

		[0 : ?], min absent          - any size is ok, nothing to check, never returns FALSE
	  OPTIONAL [0 : ?], min absent - any size is ok, nothing to check, never returns FALSE

		
		if array:
		
		[max : min]  - size must be = max - min + 1 ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
		OPTIONAL [max : min] - size must be 0 or max - min + 1 ->  IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;

	*/

	if (arm_attribute.is_array) {

		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				printDebug("\t\t-- OPTIONAL ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				// OPTIONAL ARRAY [min : max] -> IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF (size <> 0) AND (size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + ") THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// ARRAY [min : max] ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
				printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
			
				pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
			// ARRAY [min : max] -> IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
			printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

			pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
	} else {

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				// [min : ?]
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else
				if (!xa.getOptional_flag(null)) {
					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				} else {
					empty_if = false;
					// OPTIONAL [min : ?], min != 0  ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") AND (size <> 0) THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				}
			} else {
				empty_if = false;
				// both defined
				if (arm_attribute.min_cardinality == 0) {
					// the same for both OPTINAL and non-OPTIONAL
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else
				if (!xa.getOptional_flag(null)) {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// OPTIONAL [min : max], min != 0 ->  IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

					pw.println(indentString(alternative_nr) + "IF ((size < " + arm_attribute.min_cardinality + ") AND (size <> 0)) OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
			}
		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else {

					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");

				}
			} else {
				empty_if = false;
				if (arm_attribute.min_cardinality == 0) {
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			}
		} // derived attribute
	
	} // not ARRAY
		
		if (arm_attribute.is_unique) {
			// generate check if all the elements of the result aggregate are unique
//			pw.println(indentString(alternative_nr) + "IF NOT VALUE_UNIQUE(results) THEN");
			pw.println(indentString(alternative_nr) + "IF NOT instance_unique(results) THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");
		}
		
		return (false);
	} // function end



	// strong, aggregate
	
		boolean generateAggregateStrongARMAttributeChecks(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;

		String result_expression_0 = indentString(alternative_nr) + "results := get__" + arm_entity_name  + "__" + attr_name;
		String result_expression_2 = indentString(alternative_nr) + "results := results + get__" + arm_entity_name  + "__" + attr_name;
		String result_expression;



		if (arm_attribute.mapping_alternatives.size() > 1) {

//			pw.println(indentString(alternative_nr) + "results := [];");

			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			

//				result_expression = result_expression_0 +  "_" + (i+1) + "(e);";
	
				if (i == 0) {
					result_expression = result_expression_0 +  "_" + (i+1) + "(e);";
 		 		} else {
					result_expression = result_expression_2 +  "_" + (i+1) + "(e);";
 		 		}
 		 
		  	pw.println(result_expression);
//				pw.println(indentString(alternative_nr) + "results := results + result;");

			} // for
		
		} else {
			// one alternative only
			result_expression = result_expression_0 + "(e);";
	  	pw.println(result_expression);
		}
		
		pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
		
	/*
		the logic to check cardinality:
		
		if not array
		
		[min : max], min != 0 - must be  min <= size <= max                    -> IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : max], min != 0 - size must be  min <= size <= max or 0 -> IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
		
		[0 : max], min == 0 - must be size <= max          ->  IF size > max THEN RETURN (FALSE); END_IF;
		OPTIONAL [0 : max], min == 0 - must be size <= max ->  IF size > max THEN RETURN (FALSE); END_IF;

		[min : ?], min != 0 - must be size >= min -> IF size < min THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : ?], min != 0,  must be size >= min or size = 0 ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;

		[0 : ?], min == 0          - any size is ok, nothing to check, never returns FALSE
		OPTIONAL [0 : ?], min == 0 - any size is ok, nothing to check, never returns FALSE

		[0 : ?], min absent          - any size is ok, nothing to check, never returns FALSE
	  OPTIONAL [0 : ?], min absent - any size is ok, nothing to check, never returns FALSE

		
		if array:
		
		[max : min]  - size must be = max - min + 1 ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
		OPTIONAL [max : min] - size must be 0 or max - min + 1 ->  IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;

	*/

	if (arm_attribute.is_array) {

		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				printDebug("\t\t-- OPTIONAL ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				// OPTIONAL ARRAY [min : max] -> IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF (size <> 0) AND (size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + ") THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// ARRAY [min : max] ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
				printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
			
				pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
			// ARRAY [min : max] -> IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
			printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

			pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
	} else {

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				// [min : ?]
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else
				if (!xa.getOptional_flag(null)) {
					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				} else {
					empty_if = false;
					// OPTIONAL [min : ?], min != 0  ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") AND (size <> 0) THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				}
			} else {
				empty_if = false;
				// both defined
				if (arm_attribute.min_cardinality == 0) {
					// the same for both OPTINAL and non-OPTIONAL
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else
				if (!xa.getOptional_flag(null)) {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// OPTIONAL [min : max], min != 0 ->  IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

					pw.println(indentString(alternative_nr) + "IF ((size < " + arm_attribute.min_cardinality + ") AND (size <> 0)) OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
			}
		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else {

					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");

				}
			} else {
				empty_if = false;
				if (arm_attribute.min_cardinality == 0) {
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			}
		} // derived attribute
	
	} // not ARRAY
		
		if (arm_attribute.is_unique) {
			// generate check if all the elements of the result aggregate are unique
//			pw.println(indentString(alternative_nr) + "IF NOT VALUE_UNIQUE(results) THEN");
			pw.println(indentString(alternative_nr) + "IF NOT instance_unique(results) THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");
		}
		
		return (false);
	} // function end


 // non strong, aggregate, single-function attribute alternatives

	boolean generateAggregateARMAttributeChecksSingleFunction(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name, boolean is_supertype) throws SdaiException {
 			
 		String supertype_message = "SUPERTYPE: " + arm_entity_name + " ";	
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
//		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
//		String size_expression;

		String result_expression = indentString(alternative_nr) + "results := get__" + arm_entity_name  + "__" + attr_name + "(e);";


	  pw.println(result_expression);
		
		pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
		
	/*
		the logic to check cardinality:
		
		if not array
		
		[min : max], min != 0 - must be  min <= size <= max                    -> IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : max], min != 0 - size must be  min <= size <= max or 0 -> IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
		
		[0 : max], min == 0 - must be size <= max          ->  IF size > max THEN RETURN (FALSE); END_IF;
		OPTIONAL [0 : max], min == 0 - must be size <= max ->  IF size > max THEN RETURN (FALSE); END_IF;

		[min : ?], min != 0 - must be size >= min -> IF size < min THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : ?], min != 0,  must be size >= min or size = 0 ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;

		[0 : ?], min == 0          - any size is ok, nothing to check, never returns FALSE
		OPTIONAL [0 : ?], min == 0 - any size is ok, nothing to check, never returns FALSE

		[0 : ?], min absent          - any size is ok, nothing to check, never returns FALSE
	  OPTIONAL [0 : ?], min absent - any size is ok, nothing to check, never returns FALSE

		
		if array:
		
		[max : min]  - size must be = max - min + 1 ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
		OPTIONAL [max : min] - size must be 0 or max - min + 1 ->  IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;

	*/

	if (arm_attribute.is_array) {

		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				printDebug("\t\t-- OPTIONAL ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				// OPTIONAL ARRAY [min : max] -> IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF (size <> 0) AND (size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + ") THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}
					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// ARRAY [min : max] ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
				printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
			
				pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
			// ARRAY [min : max] -> IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
			printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

			pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
	} else {

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				// [min : ?]
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else
				if (!xa.getOptional_flag(null)) {
					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				} else {
					empty_if = false;
					// OPTIONAL [min : ?], min != 0  ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") AND (size <> 0) THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");

					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				}
			} else {
				empty_if = false;
				// both defined
				if (arm_attribute.min_cardinality == 0) {
					// the same for both OPTINAL and non-OPTIONAL
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else
				if (!xa.getOptional_flag(null)) {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// OPTIONAL [min : max], min != 0 ->  IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

					pw.println(indentString(alternative_nr) + "IF ((size < " + arm_attribute.min_cardinality + ") AND (size <> 0)) OR (size > " + arm_attribute.max_cardinality + ") THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
			}
		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else {

					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");

				}
			} else {
				empty_if = false;
				if (arm_attribute.min_cardinality == 0) {
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			}
		} // derived attribute
	
	} // not ARRAY
		
		if (arm_attribute.is_unique) {
			// generate check if all the elements of the result aggregate are unique
//			pw.println(indentString(alternative_nr) + "IF NOT VALUE_UNIQUE(results) THEN");
			pw.println(indentString(alternative_nr) + "IF NOT instance_unique(results) THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " - NOT UNIQUE \');");
					if (flag_display_result) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						pw.println(indentString(alternative_nr) + "\tprintln(results);");
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}

				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");
		}



		
		return (false);
	} // function end

	// non strong, aggregate

	boolean generateAggregateARMAttributeChecks(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;

		String result_expression_0 = indentString(alternative_nr) + "results := get__" + arm_entity_name  + "__" + attr_name;
		String result_expression_2 = indentString(alternative_nr) + "results := results + get__" + arm_entity_name  + "__" + attr_name;
		String result_expression;



		if (arm_attribute.mapping_alternatives.size() > 1) {

//			pw.println(indentString(alternative_nr) + "results := [];");

			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			

//				result_expression = result_expression_0 +  "_" + (i+1) + "(e);";
	
				if (i == 0) {
					result_expression = result_expression_0 +  "_" + (i+1) + "(e);";
 		 		} else {
					result_expression = result_expression_2 +  "_" + (i+1) + "(e);";
 		 		}
 		 
		  	pw.println(result_expression);
//				pw.println(indentString(alternative_nr) + "results := results + result;");

			} // for
		
		} else {
			// one alternative only
			result_expression = result_expression_0 + "(e);";
	  	pw.println(result_expression);
		}
		
		pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
		
	/*
		the logic to check cardinality:
		
		if not array
		
		[min : max], min != 0 - must be  min <= size <= max                    -> IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : max], min != 0 - size must be  min <= size <= max or 0 -> IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
		
		[0 : max], min == 0 - must be size <= max          ->  IF size > max THEN RETURN (FALSE); END_IF;
		OPTIONAL [0 : max], min == 0 - must be size <= max ->  IF size > max THEN RETURN (FALSE); END_IF;

		[min : ?], min != 0 - must be size >= min -> IF size < min THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : ?], min != 0,  must be size >= min or size = 0 ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;

		[0 : ?], min == 0          - any size is ok, nothing to check, never returns FALSE
		OPTIONAL [0 : ?], min == 0 - any size is ok, nothing to check, never returns FALSE

		[0 : ?], min absent          - any size is ok, nothing to check, never returns FALSE
	  OPTIONAL [0 : ?], min absent - any size is ok, nothing to check, never returns FALSE

		
		if array:
		
		[max : min]  - size must be = max - min + 1 ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
		OPTIONAL [max : min] - size must be 0 or max - min + 1 ->  IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;

	*/

	if (arm_attribute.is_array) {

		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				printDebug("\t\t-- OPTIONAL ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				// OPTIONAL ARRAY [min : max] -> IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF (size <> 0) AND (size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + ") THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// ARRAY [min : max] ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
				printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
			
				pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
			// ARRAY [min : max] -> IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
			printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

			pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
	} else {

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				// [min : ?]
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else
				if (!xa.getOptional_flag(null)) {
					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				} else {
					empty_if = false;
					// OPTIONAL [min : ?], min != 0  ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") AND (size <> 0) THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				}
			} else {
				empty_if = false;
				// both defined
				if (arm_attribute.min_cardinality == 0) {
					// the same for both OPTINAL and non-OPTIONAL
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else
				if (!xa.getOptional_flag(null)) {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// OPTIONAL [min : max], min != 0 ->  IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

					pw.println(indentString(alternative_nr) + "IF ((size < " + arm_attribute.min_cardinality + ") AND (size <> 0)) OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
			}
		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else {

					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");

				}
			} else {
				empty_if = false;
				if (arm_attribute.min_cardinality == 0) {
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			}
		} // derived attribute
	
	} // not ARRAY
		
		if (arm_attribute.is_unique) {
			// generate check if all the elements of the result aggregate are unique
//			pw.println(indentString(alternative_nr) + "IF NOT VALUE_UNIQUE(results) THEN");
			pw.println(indentString(alternative_nr) + "IF NOT instance_unique(results) THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");
		}
		
		return (false);
	} // function end


// non-strong, non-aggregate, attribute alternatives in single function

	boolean generateNonAggregateARMAttributeChecksSingleFunction(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name, boolean is_supertype) throws SdaiException {
 			
		String supertype_message = "SUPERTYPE: " + arm_entity_name + " ";
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression   = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name + "(e));";
		String result_expression = indentString(alternative_nr) + "results := get__" + arm_entity_name  + "__" + attr_name + "(e);";


//		String result_expression_0 = indentString(alternative_nr) + "result := get__" + arm_entity_name  + "__" + attr_name;
//		String result_expression;


//		pw.println(indentString(alternative_nr) + "results := [];");

		// one alternative only
 		
 		if (flag_display_result) {
		  pw.println(result_expression);
			pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
 		} else {
 			pw.println(size_expression);
		}






		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				// OPTIONAL, size must be 0 or 1 ->  IF size > 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}
					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}
					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;

			pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					if (is_supertype) pw.println(indentString(alternative_nr) + "\tprint(\'" + supertype_message + " \');");
					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
					if (flag_display_result || flag_display_cardinality) {
						pw.println(indentString(alternative_nr) + "\tprint(e);");
						if (flag_display_result && flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprint(size);");
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else
						if (flag_display_cardinality) {
							pw.println(indentString(alternative_nr) + "\tprintln(size);");
						} else 
						if (flag_display_result) {
							pw.println(indentString(alternative_nr) + "\tprintln(results);");
						} else {
							// internal error
						}
					} else {
						pw.println(indentString(alternative_nr) + "\tprintln(e);");
					}
				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
		
		
		return (false);
	} // function end



// non-strong, non-aggregate

	boolean generateNonAggregateARMAttributeChecks(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := size + SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression_1 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;

		String result_expression_0 = indentString(alternative_nr) + "result := get__" + arm_entity_name  + "__" + attr_name;
		String result_expression;


//		pw.println(indentString(alternative_nr) + "results := [];");

 		if (arm_attribute.mapping_alternatives.size() > 1) {

			// pw.println(indentString(alternative_nr) + "size := 0;");
			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			
	
 					if (i == 0) {
 					size_expression = size_expression_1 +  "_" + (i+1) + "(e));";
 					} else {
	 					size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 					} 
		  	pw.println(size_expression);
//			pw.println(indentString(alternative_nr) + "results := results + result;");

			} // for
		
 		} else {
				// one alternative only
	  		size_expression = size_expression_1 + "(e));";
	  		pw.println(size_expression);
		}
		


		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				// OPTIONAL, size must be 0 or 1 ->  IF size > 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;

			pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
		
		
		return (false);
	} // function end


// strong, non-aggregate, attribute alternatives in single function

	boolean generateNonAggregateStrongARMAttributeChecksSingleFunction(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name +  "(e));";

//		String result_expression_0 = indentString(alternative_nr) + "result := get__" + arm_entity_name  + "__" + attr_name;
//		String result_expression;


//		pw.println(indentString(alternative_nr) + "results := [];");

		// one alternative only
 		pw.println(size_expression);
		


		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				// OPTIONAL, size must be 0 or 1 ->  IF size > 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;

			pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
		
		
		return (false);
	} // function end



// strong, non-aggregate

	boolean generateNonAggregateStrongARMAttributeChecks(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := size + SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression_1 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;

		String result_expression_0 = indentString(alternative_nr) + "result := get__" + arm_entity_name  + "__" + attr_name;
		String result_expression;


//		pw.println(indentString(alternative_nr) + "results := [];");

 		if (arm_attribute.mapping_alternatives.size() > 1) {

			// pw.println(indentString(alternative_nr) + "size := 0;");
			for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			
	
 					if (i == 0) {
 					size_expression = size_expression_1 +  "_" + (i+1) + "(e));";
 					} else {
	 					size_expression = size_expression_0 +  "_" + (i+1) + "(e));";
 					} 
 		 
		  	pw.println(size_expression);
//			pw.println(indentString(alternative_nr) + "results := results + result;");

			} // for
		
 		} else {
				// one alternative only
				size_expression = size_expression_1 +  "(e));";
	  		pw.println(size_expression);
		}
		


		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				// OPTIONAL, size must be 0 or 1 ->  IF size > 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;

			pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
		
		
		return (false);
	} // function end



// strong, aggregate

	boolean generateAggregateStrongARMAttributeChecks_old(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
		String size_expression;

		String result_expression_0 = indentString(alternative_nr) + "result := get__" + arm_entity_name  + "__" + attr_name;
		String result_expression;


		pw.println(indentString(alternative_nr) + "results := [];");
		for (int i = 0; i < arm_attribute.mapping_alternatives.size(); i++) {			

 			if (arm_attribute.mapping_alternatives.size() == 1) {
				result_expression = result_expression_0 + "(e);";
 			} else {
 				// must be > 1
 				result_expression = result_expression_0 +  "_" + (i+1) + "(e);";
 			} 
 		 
		  pw.println(result_expression);
			pw.println(indentString(alternative_nr) + "results := results + result;");

		} // for
		
		
		pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
		
	/*
		the logic to check cardinality:
		
		if not array
		
		[min : max], min != 0 - must be  min <= size <= max                    -> IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : max], min != 0 - size must be  min <= size <= max or 0 -> IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
		
		[0 : max], min == 0 - must be size <= max          ->  IF size > max THEN RETURN (FALSE); END_IF;
		OPTIONAL [0 : max], min == 0 - must be size <= max ->  IF size > max THEN RETURN (FALSE); END_IF;

		[min : ?], min != 0 - must be size >= min -> IF size < min THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : ?], min != 0,  must be size >= min or size = 0 ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;

		[0 : ?], min == 0          - any size is ok, nothing to check, never returns FALSE
		OPTIONAL [0 : ?], min == 0 - any size is ok, nothing to check, never returns FALSE

		[0 : ?], min absent          - any size is ok, nothing to check, never returns FALSE
	  OPTIONAL [0 : ?], min absent - any size is ok, nothing to check, never returns FALSE

		
		if array:
		
		[max : min]  - size must be = max - min + 1 ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
		OPTIONAL [max : min] - size must be 0 or max - min + 1 ->  IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;

	*/

	if (arm_attribute.is_array) {

		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				printDebug("\t\t-- OPTIONAL ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				// OPTIONAL ARRAY [min : max] -> IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF (size <> 0) AND (size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + ") THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// ARRAY [min : max] ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
				printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
			
				pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
			// ARRAY [min : max] -> IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
			printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

			pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
	} else {

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				// [min : ?]
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else
				if (!xa.getOptional_flag(null)) {
					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				} else {
					empty_if = false;
					// OPTIONAL [min : ?], min != 0  ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") AND (size <> 0) THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				}
			} else {
				empty_if = false;
				// both defined
				if (arm_attribute.min_cardinality == 0) {
					// the same for both OPTINAL and non-OPTIONAL
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else
				if (!xa.getOptional_flag(null)) {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// OPTIONAL [min : max], min != 0 ->  IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

					pw.println(indentString(alternative_nr) + "IF ((size < " + arm_attribute.min_cardinality + ") AND (size <> 0)) OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
			}
		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else {

					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");

				}
			} else {
				empty_if = false;
				if (arm_attribute.min_cardinality == 0) {
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			}
		} // derived attribute
	
	} // not ARRAY
		
		if (arm_attribute.is_unique) {
			// generate check if all the elements of the result aggregate are unique
//			pw.println(indentString(alternative_nr) + "IF NOT VALUE_UNIQUE(results) THEN");
			pw.println(indentString(alternative_nr) + "IF NOT instance_unique(results) THEN");
				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");
		}
		
		return (false);
	} // function end



	Vector collectArmAttributes(EEntity_mapping the_entity_mapping, EEntity_definition arm_entity, SdaiModel arm_model, SdaiModel map_model) throws SdaiException {

			Vector arm_attributes = new Vector();
			Aggregate attributes = arm_model.getEntityExtentInstances(EAttribute.class);
			SdaiIterator iter = attributes.createIterator();
//		Aggregate attribute_mappings = map_model.getEntityExtentInstances(EAttribute_mapping.class);
			Aggregate attribute_mappings = map_model.getEntityExtentInstances(EGeneric_attribute_mapping.class);
			SdaiIterator iter_map;
			while (iter.next()) {
				EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
				EEntity_definition parent = (EEntity_definition)an_attribute.getParent(null);
 				if (parent != arm_entity) continue;
				Vector attribute_mapping_alternatives = new Vector();
				iter_map = attribute_mappings.createIterator();
				while (iter_map.next()) {
//				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
					EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
					EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
					if (an_entity_mapping == the_entity_mapping) {
						EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
						if (an_attribute == a_mapped_attribute) {
							attribute_mapping_alternatives.addElement(an_attribute_mapping);
						}
					}	
				}	

				if (attribute_mapping_alternatives.size() > 0) {
					RuleGeneratorArmAttribute arm_attribute = new RuleGeneratorArmAttribute(an_attribute, attribute_mapping_alternatives);
					arm_attributes.addElement(arm_attribute);
				}

			}
			
			return arm_attributes;


	} // end function








// EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE start



// non-strong, non-aggregate, attribute alternatives for multiple compatible entity_mapping alternatives in supertypes

	boolean generateNonAggregateARMAttributeChecksMultipleCompatibleAlternatives(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name + "(e));";

//		String result_expression_0 = indentString(alternative_nr) + "result := get__" + arm_entity_name  + "__" + attr_name;
//		String result_expression;


//		pw.println(indentString(alternative_nr) + "results := [];");

		// one alternative only
 		pw.println(size_expression);
		


		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				// OPTIONAL, size must be 0 or 1 ->  IF size > 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
					// pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
					// pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;

			pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
				// pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
		
		
		return (false);
	} // function end


 // non strong, aggregate, single-function attribute alternatives with multiple compatible entity_mapping alternatives 

	boolean generateAggregateARMAttributeChecksMultipleCompatibleAlternatives(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
//		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
//		String size_expression;

		String result_expression = indentString(alternative_nr) + "results := get__" + arm_entity_name  + "__" + attr_name + "(e);";


	  pw.println(result_expression);
		
		pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
		
	/*
		the logic to check cardinality:
		
		if not array
		
		[min : max], min != 0 - must be  min <= size <= max                    -> IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : max], min != 0 - size must be  min <= size <= max or 0 -> IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
		
		[0 : max], min == 0 - must be size <= max          ->  IF size > max THEN RETURN (FALSE); END_IF;
		OPTIONAL [0 : max], min == 0 - must be size <= max ->  IF size > max THEN RETURN (FALSE); END_IF;

		[min : ?], min != 0 - must be size >= min -> IF size < min THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : ?], min != 0,  must be size >= min or size = 0 ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;

		[0 : ?], min == 0          - any size is ok, nothing to check, never returns FALSE
		OPTIONAL [0 : ?], min == 0 - any size is ok, nothing to check, never returns FALSE

		[0 : ?], min absent          - any size is ok, nothing to check, never returns FALSE
	  OPTIONAL [0 : ?], min absent - any size is ok, nothing to check, never returns FALSE

		
		if array:
		
		[max : min]  - size must be = max - min + 1 ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
		OPTIONAL [max : min] - size must be 0 or max - min + 1 ->  IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;

	*/

	if (arm_attribute.is_array) {

		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				printDebug("\t\t-- OPTIONAL ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				// OPTIONAL ARRAY [min : max] -> IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF (size <> 0) AND (size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + ") THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
					// pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// ARRAY [min : max] ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
				printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
			
				pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
					// pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
			// ARRAY [min : max] -> IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
			printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

			pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
	} else {

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				// [min : ?]
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else
				if (!xa.getOptional_flag(null)) {
					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				} else {
					empty_if = false;
					// OPTIONAL [min : ?], min != 0  ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") AND (size <> 0) THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				}
			} else {
				empty_if = false;
				// both defined
				if (arm_attribute.min_cardinality == 0) {
					// the same for both OPTINAL and non-OPTIONAL
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else
				if (!xa.getOptional_flag(null)) {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// OPTIONAL [min : max], min != 0 ->  IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

					pw.println(indentString(alternative_nr) + "IF ((size < " + arm_attribute.min_cardinality + ") AND (size <> 0)) OR (size > " + arm_attribute.max_cardinality + ") THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
			}
		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else {

					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");

				}
			} else {
				empty_if = false;
				if (arm_attribute.min_cardinality == 0) {
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			}
		} // derived attribute
	
	} // not ARRAY
		
		if (arm_attribute.is_unique) {
			// generate check if all the elements of the result aggregate are unique
//			pw.println(indentString(alternative_nr) + "IF NOT VALUE_UNIQUE(results) THEN");
			pw.println(indentString(alternative_nr) + "IF NOT instance_unique(results) THEN");
//					pw.println(indentString(alternative_nr) + "\tprint(\'" + attr_name + " - NOT UNIQUE \');");
//					pw.println(indentString(alternative_nr) + "\tprintln(e);");
					pw.println(indentString(alternative_nr) + "\tall_non_strong_passed := FALSE;");
//				pw.println(indentString(alternative_nr) + "\tRETURN (FALSE);");
			pw.println(indentString(alternative_nr) + "END_IF;");
		}
		
		return (false);
	} // function end



// strong, non-aggregate, attribute alternatives in single function for multiple compatible entity_mapping alternatives

	boolean generateNonAggregateStrongARMAttributeChecksMultipleCompatibleAlternatives(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
		String size_expression = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name +  "(e));";

//		String result_expression_0 = indentString(alternative_nr) + "result := get__" + arm_entity_name  + "__" + attr_name;
//		String result_expression;


//		pw.println(indentString(alternative_nr) + "results := [];");

		// one alternative only
 		pw.println(size_expression);
		


		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				// OPTIONAL, size must be 0 or 1 ->  IF size > 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size > 1 THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");

//					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
				// size must be 1 -> IF size <> 1 THEN RETURN (FALSE); END_IF;

			pw.println(indentString(alternative_nr) + "IF size <> 1 THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
		
		
		return (false);
	} // function end



// strong, aggregate, single function attribute alternatives for multiple compatible entity_mapping alternatives


	boolean generateAggregateStrongARMAttributeChecksMultipleCompatibleAlternatives(RuleGeneratorArmAttribute arm_attribute, String arm_entity_name) throws SdaiException {
 			
		boolean empty_if = true;
		int alternative_nr = 2;
			
 		if (arm_attribute.mapping_alternatives.size() < 1) {
 			return true;
 		}

 		String attr_name = arm_attribute.arm_attribute.getName(null).toLowerCase();
//		String size_expression_0 = indentString(alternative_nr) + "size := SIZEOF(get__" + arm_entity_name  + "__" + attr_name;
//		String size_expression;

		String result_expression = indentString(alternative_nr) + "results := get__" + arm_entity_name  + "__" + attr_name + "(e);";



	  pw.println(result_expression);
		
		pw.println(indentString(alternative_nr) + "size := SIZEOF(results);");
		
		
	/*
		the logic to check cardinality:
		
		if not array
		
		[min : max], min != 0 - must be  min <= size <= max                    -> IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : max], min != 0 - size must be  min <= size <= max or 0 -> IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
		
		[0 : max], min == 0 - must be size <= max          ->  IF size > max THEN RETURN (FALSE); END_IF;
		OPTIONAL [0 : max], min == 0 - must be size <= max ->  IF size > max THEN RETURN (FALSE); END_IF;

		[min : ?], min != 0 - must be size >= min -> IF size < min THEN RETURN (FALSE); END_IF;
		OPTIONAL [min : ?], min != 0,  must be size >= min or size = 0 ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;

		[0 : ?], min == 0          - any size is ok, nothing to check, never returns FALSE
		OPTIONAL [0 : ?], min == 0 - any size is ok, nothing to check, never returns FALSE

		[0 : ?], min absent          - any size is ok, nothing to check, never returns FALSE
	  OPTIONAL [0 : ?], min absent - any size is ok, nothing to check, never returns FALSE

		
		if array:
		
		[max : min]  - size must be = max - min + 1 ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
		OPTIONAL [max : min] - size must be 0 or max - min + 1 ->  IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;

	*/

	if (arm_attribute.is_array) {

		empty_if = false;

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
	
			if (xa.getOptional_flag(null)) {
				printDebug("\t\t-- OPTIONAL ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				// OPTIONAL ARRAY [min : max] -> IF (size <> 0) AND (size <> max-min+1) THEN RETURN (FALSE); END_IF;
			
				pw.println(indentString(alternative_nr) + "IF (size <> 0) AND (size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + ") THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			} else {
				// not OPTIONAL
				// ARRAY [min : max] ->  IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
				printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
			
				pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//					pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
				pw.println(indentString(alternative_nr) + "END_IF;");
				
			}

		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			
			// ARRAY [min : max] -> IF size <> (max-min+1) THEN RETURN (FALSE); END_IF;
			printDebug("\t\t-- ARRAY [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

			pw.println(indentString(alternative_nr) + "IF size <> " + (arm_attribute.max_cardinality-arm_attribute.min_cardinality+1) + " THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");

		} // derived
	
	} else {

		if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			
			EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				// [min : ?]
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else
				if (!xa.getOptional_flag(null)) {
					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				} else {
					empty_if = false;
					// OPTIONAL [min : ?], min != 0  ->  IF (size < min) AND (size <> 0) THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") AND (size <> 0) THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				
				}
			} else {
				empty_if = false;
				// both defined
				if (arm_attribute.min_cardinality == 0) {
					// the same for both OPTINAL and non-OPTIONAL
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else
				if (!xa.getOptional_flag(null)) {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// OPTIONAL [min : max], min != 0 ->  IF ((size < min) AND (size <> 0)) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");

					pw.println(indentString(alternative_nr) + "IF ((size < " + arm_attribute.min_cardinality + ") AND (size <> 0)) OR (size > " + arm_attribute.max_cardinality + ") THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				}
			}
		} else // derived attribute, can not be optional
		if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
			EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
			if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
				// both must be unset, nothing to check
				printDebug("\t\t-- [0:?] any size ok");
			} else
			if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
				if (arm_attribute.min_cardinality == 0) {
					printDebug("\t\t-- [0:?] any size ok");
				} else {

					empty_if = false;
					//  [min : ?], min != 0 ->  IF size < min THEN RETURN (FALSE); END_IF;
					printDebug(indentString(alternative_nr) + "-- aggregate [" + arm_attribute.min_cardinality + ":?]");
					
					pw.println(indentString(alternative_nr) + "IF size < " + arm_attribute.min_cardinality + " THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");

				}
			} else {
				empty_if = false;
				if (arm_attribute.min_cardinality == 0) {
				  // [0 : max] ->  IF size > max THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [0:" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF size > " + arm_attribute.max_cardinality + " THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
					
				} else {
					// [min : max], min !=0 ->  IF (size < min) OR (size > max) THEN RETURN (FALSE); END_IF;
					printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
				
					pw.println(indentString(alternative_nr) + "IF (size < " + arm_attribute.min_cardinality + ") OR (size > " + arm_attribute.max_cardinality + ") THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//						pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
					pw.println(indentString(alternative_nr) + "END_IF;");
				}
			}
		} // derived attribute
	
	} // not ARRAY
		
		if (arm_attribute.is_unique) {
			// generate check if all the elements of the result aggregate are unique
//			pw.println(indentString(alternative_nr) + "IF NOT VALUE_UNIQUE(results) THEN");
			pw.println(indentString(alternative_nr) + "IF NOT instance_unique(results) THEN");
					pw.println(indentString(alternative_nr) + "\tskip_alternative := TRUE;");
//				pw.println(indentString(alternative_nr) + "\tRETURN (TRUE);");
			pw.println(indentString(alternative_nr) + "END_IF;");
		}
		
		return (false);
	} // function end




// EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE end





	// generate checks for ARM supertype entities	

	// strong supertype attributes
	boolean recurseSupertypesForSkipping(EEntity_definition arm_entity, EEntity_definition start_entity, SdaiModel arm_model, SdaiModel map_model, EEntity_mapping the_entity_mapping, int number_entity_alternatives, int index_entity_alternatives, HashSet processed) throws SdaiException {
		boolean result = true;
		boolean empty_if = true;
		int multiple_status = 0;
		// - if actions for current inside: check current entity_mapping constraints, current strong attributes
		// check supertype strong attributes, do not check supertype entity_mapping constraints - controversial?

		if (!processed.add(arm_entity)) {
			// repeated inheritance, already done
			return true;
		}
	
		AEntity_or_view_definition supertypes = arm_entity.getGeneric_supertypes(null);
  	SdaiIterator isuper = supertypes.createIterator();

  	while (isuper.next()) {
  		EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(isuper);
			// recurseSupertypesForSkipping(supertype, processed);
//			pw.println("\t\t-- recursing supertypes: from " + arm_entity.getName(null) + " to " + supertype.getName(null));
			result = recurseSupertypesForSkipping(supertype, start_entity, arm_model, map_model, the_entity_mapping, number_entity_alternatives, index_entity_alternatives, processed);
			empty_if = empty_if && result;

		}
		// here we add our actions

		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		// indentation = 0;
		// EEntity aim_target = the_entity_mapping.getTarget(null);
		if (number_entity_alternatives > 1) {
			arm_entity_name += "_" + (index_entity_alternatives + 1);
		}

		if (arm_entity == start_entity) {
		// if (processed.size() == 1) {
			// actions for the current entity
			// we may move actions from the main body here, or may not
		} else {
			// actions for the supertype
			// generate checks for strong attributes, their functions should be generated at the current level, no need to generate
			// here, we need to do all the preparations, for each attribute find alternatives, etc.


		  //  we need to find the appropriate entity_mapping alternative(s) for the supertype
		  // the target entity in the entity_mapping of the supertype must be compatible with the target entity in the entity_mapping
		  // of the initial arm entity - either the same entity or its supertype
		  
		  EEntity current_target = null;
		  EEntity_definition current_target_entity = null;
		  if (the_entity_mapping.testTarget(null)) {
		  	current_target = the_entity_mapping.getTarget(null);
		  } 
		  if (current_target instanceof EEntity_definition) {
		  	current_target_entity = (EEntity_definition)current_target;
		  }
			if (current_target_entity == null) {
				// will not happen
				return empty_if;
			}
			// go through the entity_mapping alternatives of the supertype:
			AEntity all_entity_mappings = map_model.getEntityExtentInstances(EEntity_mapping.class);
			Vector entity_mappings = new Vector();
			Vector compatible_entity_mappings = new Vector();
			Vector compatible_entity_mappings__attributes = new Vector();
			SdaiIterator iter = all_entity_mappings.createIterator();
			while (iter.next()) {
				EEntity_mapping entity_mapping  = (EEntity_mapping)all_entity_mappings.getCurrentMemberObject(iter);
				EEntity_definition source = entity_mapping.getSource(null);
				if (source == arm_entity) {
					// had to add it, because I need the same index values, otherwise I would add only target-compatible ones
					entity_mappings.addElement(entity_mapping);
					// found an entity_mapping, but is it target-compatible?
					EEntity supertype_target = null;
					EEntity_definition supertype_target_entity = null;
				  if (entity_mapping.testTarget(null)) {
				  	supertype_target = entity_mapping.getTarget(null);
		  		}
		  		if (supertype_target instanceof EEntity_definition) {
		  			supertype_target_entity = (EEntity_definition)supertype_target;
		  		}
					if (supertype_target_entity == null) {
						// will not happen, but if happens - skip
						continue;
					}
					// is supertype_target_entity the same or supertype of the current_target_entity?
//					pw.println("-- current target: " + current_target_entity.getName(null) + ", supertype target: " + supertype_target_entity.getName(null));
					
						// better use jsdai ways to determine the compatibility, this one will not work for multiple inheritance, etc.

//		pw.println("-- checking compatibility of ARM alternative, supertype " + arm_entity.getName(null) + " of  " + start_entity.getName(null));
						if (isCompatible(supertype_target_entity, current_target_entity)) {
							// here goes something, perhaps creating a separate Vector of indices, it would be good to know in advance
							// if there are more than one alternative, so better not to do the checking directly in this loop	
							compatible_entity_mappings.addElement(new Integer(entity_mappings.size()));

							compatible_entity_mappings__attributes.addElement(collectArmAttributes(entity_mapping, arm_entity, arm_model, map_model));

// get attributes for this compatible alternative here

//							pw.println("-- compatible targets: " + current_target_entity.getName(null) + ", supertype target: " + supertype_target_entity.getName(null));
					
						}
//					if (supertype_target_class.isAssignableFrom(current_target_cluss)) {
//						entity_mappings.addElement(entity_mapping);
//					
//						}

				} // entity_mapping found for this arm entity
			} // go through all the mappings
			
			EEntity_mapping supertype_entity_mapping = null;
			EEntity constraints = null;
			String constraint_label = null;

			// if only one entity_mapping - no alternatives - one check, function name without suffix
			// if more than one entity_mapping, but only one compatible, - one check, add suffix to the function name
			// if more than one suitable entity_mapping, have to do something, perhaps to use an additional function,
			// or generate directly code
			if (compatible_entity_mappings.size() > 1) {
				multiple_status = 3;
				
				// the worst case, 
				// here we will handle everything: entity_mapping constraints, strong-attributes and non-strong attributes

			// initialize for each supertype before alternatives
			pw.println("\tskipping_instance_in_supertype := TRUE;");
			pw.println("\tok_alternative_found := FALSE;");

			pw.println("-- multiple compatible supertype alternatives found");
			for (int ix = 0; ix < compatible_entity_mappings.size(); ix++) {

				// initialize before each alternative
 	 			pw.println("\tskip_alternative := FALSE;");
	 			pw.println("\tall_non_strong_passed := TRUE;");






// after all attributes of alternative:



					//-------
				int valid_index = ((Integer)compatible_entity_mappings.elementAt(ix)).intValue() - 1;
				supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(valid_index);
				// arm_entity_name += "_" + (valid_index+1);
				arm_entity_name = arm_entity.getName(null).toLowerCase() +  "_" + (valid_index+1);
					
				if (supertype_entity_mapping.testConstraints(null)) {
					// count_present++;
					constraints = supertype_entity_mapping.getConstraints(null);
					constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();
					printComments("\t-- entity_mapping: " + supertype_entity_mapping);
					printComments("\t-- constraints: " + constraints);
					if (constraints instanceof EAttribute) {
//						pw.println(indentString(1) + "\t\tIF NOT EXISTS(e." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
						pw.println(indentString(1) + "\t\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
					} else {
						pw.println(indentString(1) + "\t\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
					}
					pw.println(indentString(1) + "\t\t\tall_non_strong_passed := FALSE;");
					pw.println(indentString(1) + "\t\tEND_IF;");
				}	

					
					//--------

				Vector the_arm_attributes = (Vector)compatible_entity_mappings__attributes.elementAt(ix);


// ########################################## start

				if (the_arm_attributes.size() >0) {
					empty_if = false;
					boolean empty_if2; // probably not needed here 
					
					
					Iterator the_attribute_iterator = the_arm_attributes.iterator();
					while (the_attribute_iterator.hasNext()) {
						RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)the_attribute_iterator.next();
						if (arm_attribute.strong_count == 0) {
							
							pw.println("-- non-strong supertype attribute, multiple compatible alternatives: " + arm_attribute.arm_attribute.getName(null));

							if (arm_attribute.is_aggregate) {
								empty_if2 = generateAggregateARMAttributeChecksMultipleCompatibleAlternatives(arm_attribute, arm_entity_name);
							} else {
								empty_if2 = generateNonAggregateARMAttributeChecksMultipleCompatibleAlternatives(arm_attribute, arm_entity_name);
							}
						
						} else {
			

// component_part_2d_non_planar_geometric_representation_relationship, attributes shape_1, shape_2 - rule passed

//						boolean empty_if2 = true;

							pw.println("-- strong supertype attribute, multiple compatible alternatives: " + arm_attribute.arm_attribute.getName(null));
							

							if (arm_attribute.is_aggregate) {
								empty_if2 = generateAggregateStrongARMAttributeChecksMultipleCompatibleAlternatives(arm_attribute, arm_entity_name);
							} else {
								empty_if2 = generateNonAggregateStrongARMAttributeChecksMultipleCompatibleAlternatives(arm_attribute, arm_entity_name);
							}
						} // strong ends here
			
///						pw.println("");
		
//		  			empty_if = empty_if && empty_if2;	
					}	

				} // not 0 attributes

// ##################################### end
			
			// do after each alternative

			pw.println("\tskipping_instance_in_supertype := skipping_instance_in_supertype AND skip_alternative;");
			
			pw.println("\t\tIF (NOT skip_alternative) AND all_non_strong_passed THEN");
				// alternative does not skip and non-strongs are ok, so this alternative makes this supertype OK
				pw.println("\t\t\tok_alternative_found := TRUE;");
			pw.println("\t\tEND_IF;");		   

			
/*
			pw.println("\tIF all_strong_passed THEN");
				// at least one alternative that does not force the instance to be skipped
				pw.println("\t\tskipping_instance_in_supertype := FALSE;");
				pw.println("\t\tIF all_non_strong_passed THEN");
					// alternative does not skip and non-strongs are ok, so this alternative makes this supertype OK
					pw.println("\t\t\tok_alternative_found := TRUE;");
				pw.println("\t\tEND_IF;");		   
			pw.println("\tEND_IF;");		   
*/			
			
			
			} // for


			// after all the alternatives

			pw.println("\tIF skipping_instance_in_supertype THEN");
				pw.println("\t\tRETURN (TRUE);");
			pw.println("\tEND_IF;");	   
   
			pw.println("\tIF NOT ok_alternative_found THEN"); 
				pw.println("\t\tfailure_in_supertypes := TRUE;");
			pw.println("\tEND_IF;");	  


			 if (flag_swap_supertype_constraints) { // supertype entity_mapping constraints, if true, skips the instance
				int count_present = 0;
				for (int ii = 0; ii < compatible_entity_mappings.size(); ii++) {
					int valid_index = ((Integer)compatible_entity_mappings.elementAt(ii)).intValue() - 1;
					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(valid_index);
					if (supertype_entity_mapping.testConstraints(null)) {
						empty_if = false;
						count_present++;
						constraints = supertype_entity_mapping.getConstraints(null);
						constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();
						printComments("\t-- entity_mapping: " + supertype_entity_mapping);
						printComments("\t-- constraints: " + constraints);
						if (constraints instanceof EAttribute) {
							pw.println(indentString(ii) + "\t\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
						} else {
							pw.println(indentString(ii) + "\t\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
						}
//						if (ii == (compatible_entity_mappings.size()-1)) {
//							pw.println(indentString(ii) + "\t\tRETURN (FALSE);");
//						}
//						pw.println("\tEND_IF;");
					}	
				}
// check__altered_package_3 - no loops only return false - should not do this if constraints not present
 				if (count_present > 0) {
					pw.println(indentString(count_present) + "\t\tRETURN (TRUE);");
				}
				for (int ii = compatible_entity_mappings.size()-1; ii >= 0; ii--) {
					int valid_index = ((Integer)compatible_entity_mappings.elementAt(ii)).intValue() - 1;
					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(valid_index);
					if (supertype_entity_mapping.testConstraints(null)) {
						pw.println(indentString(ii) + "\t\tEND_IF;");
					}	
				}
			 } // generate according to flag


			} else 
			if (compatible_entity_mappings.size() == 1) {
				// may be one compatible or one altogether
				if (entity_mappings.size() > 1) {
					multiple_status = 2;
					// one compatible from several, the name of the function has to be modified
//					pw.println("-- one target-compatible supertype alternative found");
					// arm_entity_name += "_" + ((Integer)compatible_entity_mappings.elementAt(0)).intValue();

					int alternative_index = ((Integer)compatible_entity_mappings.elementAt(0)).intValue() - 1;
					arm_entity_name += "_" + (alternative_index+1);

			 	 if (flag_swap_supertype_constraints) { // supertype entity_mapping constraints, if true, skips the instance


					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(alternative_index);
					if (supertype_entity_mapping.testConstraints(null)) {
						empty_if = false;
						constraints = supertype_entity_mapping.getConstraints(null);
						constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();
						printComments("\t-- entity_mapping: " + supertype_entity_mapping);
						printComments("\t-- constraints: " + constraints);
						if (constraints instanceof EAttribute) {
							pw.println("\t\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
						} else {
							pw.println("\t\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
						}
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;");
					}	
				 } // if flag - swap is true


				} else {
					multiple_status = 1;
					// no alternatives, the name is without suffix
//					pw.println("-- one supertype entity_mapping found, no alternatives");



			 	 if (flag_swap_supertype_constraints) { // supertype entity_mapping constraints, if true, skips the instance

					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(0);
					if (supertype_entity_mapping.testConstraints(null)) {
						empty_if = false;
						constraints = supertype_entity_mapping.getConstraints(null);
						constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();
						printComments("\t-- entity_mapping: " + supertype_entity_mapping);
						printComments("\t-- constraints: " + constraints);
						if (constraints instanceof EAttribute) {
							pw.println("\t\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
						} else {
							pw.println("\t\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
						}
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;");
					}	
				
         } // flag - swap is true				

				}
			} else {
				multiple_status = -1;
				// something wrong here
//					pw.println("--  no compatible supertype entity_mapping at all, from: " + entity_mappings.size());
			}					 

// ###############################################################################################################

		  if (multiple_status == 3) {
		  	// handled separately
		  	return false;
		  }

			Vector arm_attributes = new Vector();
			Aggregate attributes = arm_model.getEntityExtentInstances(EAttribute.class);
			iter = attributes.createIterator();
//		Aggregate attribute_mappings = map_model.getEntityExtentInstances(EAttribute_mapping.class);
			Aggregate attribute_mappings = map_model.getEntityExtentInstances(EGeneric_attribute_mapping.class);
			SdaiIterator iter_map;
			while (iter.next()) {
				EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
				EEntity_definition parent = (EEntity_definition)an_attribute.getParent(null);
 				if (parent != arm_entity) continue;
				Vector attribute_mapping_alternatives = new Vector();
				iter_map = attribute_mappings.createIterator();
				while (iter_map.next()) {
//				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
					EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
					EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
//					if (an_entity_mapping == the_entity_mapping) {
					if (an_entity_mapping == supertype_entity_mapping) {
						EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
						if (an_attribute == a_mapped_attribute) {
							attribute_mapping_alternatives.addElement(an_attribute_mapping);
						}
					}	
				}	

				if (attribute_mapping_alternatives.size() > 0) {
					RuleGeneratorArmAttribute arm_attribute = new RuleGeneratorArmAttribute(an_attribute, attribute_mapping_alternatives);
					arm_attributes.addElement(arm_attribute);
				}

			}
// ######################

// new version, attribute mapping alternatives in different functions

    	// strong, mandatory or optional, but optional does not happen
			Iterator attribute_iterator = arm_attributes.iterator();
//		printDebug("\t\t-- strong, if present in supertype " + arm_entity.getName(null));
			pw.println("\t\t-- strong, if present in supertype " + arm_entity.getName(null));
			while (attribute_iterator.hasNext()) {
				RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();
				if (arm_attribute.strong_count == 0) {
					continue;
				}
			
// pw.println("-- attribute: " + arm_attribute.arm_attribute.getName(null) + ", alternatives: " + arm_attribute.mapping_alternatives.size() + ", value: " + arm_attribute.is_attribute_mapping_value);

//				pw.println("-- strong supertype attributes");
				printDebug("-- strong supertype attributes, multiple alternatives: " + multiple_status);
				boolean empty_if2 = true;

				if (arm_attribute.is_aggregate) {
//				empty_if2 = generateAggregateStrongARMAttributeChecks(arm_attribute, arm_entity_name);
					empty_if2 = generateAggregateStrongARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name);
				} else {
//				empty_if2 = generateMandatoryOrOptionalStrongARMAttributeChecks(arm_attribute, arm_entity_name);
//				empty_if2 = generateNonAggregateStrongARMAttributeChecks(arm_attribute, arm_entity_name);
					empty_if2 = generateNonAggregateStrongARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name);
				}

			
				pw.println("");
		
		  	empty_if = empty_if && empty_if2;	
			}	



// ########################################################################################################
		} // a supertype
	
		return empty_if;
	}

// non-strong



	// at this moment void, but may not be void eventually
	boolean recurseSupertypesForFailure(EEntity_definition arm_entity, EEntity_definition start_entity, SdaiModel arm_model, SdaiModel map_model, EEntity_mapping the_entity_mapping, int number_entity_alternatives, int index_entity_alternatives, HashSet processed) throws SdaiException {
		boolean result = true;
		boolean empty_if = true;
		int multiple_status = 0;
		// - if actions for current inside: check current entity_mapping constraints, current strong attributes
		// check supertype strong attributes, do not check supertype entity_mapping constraints - controversial?

		if (!processed.add(arm_entity)) {
			// repeated inheritance, already done
			return true;
		}
	
		AEntity_or_view_definition supertypes = arm_entity.getGeneric_supertypes(null);
  	SdaiIterator isuper = supertypes.createIterator();

  	while (isuper.next()) {
  		EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(isuper);
			// recurseSupertypesForSkipping(supertype, processed);
//			pw.println("\t\t-- recursing supertypes: from " + arm_entity.getName(null) + " to " + supertype.getName(null));

			result = recurseSupertypesForFailure(supertype, start_entity, arm_model, map_model, the_entity_mapping, number_entity_alternatives, index_entity_alternatives, processed);
			empty_if = empty_if && result;
		}
		// here we add our actions

		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		String supertype_message = "SUPERTYPE: " + arm_entity_name + " "; 
		// indentation = 0;
		// EEntity aim_target = the_entity_mapping.getTarget(null);
//		if (number_entity_alternatives > 1) {
//			arm_entity_name += "_" + (index_entity_alternatives + 1);
//		}
			
			
		if (arm_entity == start_entity) {
			// actions for the current entity
			// we may move actions from the main body here, or may not
		} else {
			// actions for the supertype
			// generate checks for strong attributes, their functions should be generated at the current level, no need to generate
			// here, we need to do all the preparations, for each attribute find alternatives, etc.


		  //  we need to find the appropriate entity_mapping alternative(s) for the supertype
		  // the target entity in the entity_mapping of the supertype must be compatible with the target entity in the entity_mapping
		  // of the initial arm entity - either the same entity or its supertype
		  
		  
		  EEntity current_target = null;
		  EEntity_definition current_target_entity = null;
		  if (the_entity_mapping.testTarget(null)) {
		  	current_target = the_entity_mapping.getTarget(null);
		  } 
		  if (current_target instanceof EEntity_definition) {
		  	current_target_entity = (EEntity_definition)current_target;
		  }
			if (current_target_entity == null) {
				// will not happen
				return  empty_if;
			}
			// go through the entity_mapping alternatives of the supertype:
			AEntity all_entity_mappings = map_model.getEntityExtentInstances(EEntity_mapping.class);
			Vector entity_mappings = new Vector();
			Vector compatible_entity_mappings = new Vector();
			Vector compatible_entity_mappings__attributes = new Vector();
			SdaiIterator iter = all_entity_mappings.createIterator();
			while (iter.next()) {
				EEntity_mapping entity_mapping  = (EEntity_mapping)all_entity_mappings.getCurrentMemberObject(iter);
				EEntity_definition source = entity_mapping.getSource(null);
				if (source == arm_entity) {
					// had to add it, because I need the same index values, otherwise I would add only target-compatible ones
					entity_mappings.addElement(entity_mapping);
					// found an entity_mapping, but is it target-compatible?
					EEntity supertype_target = null;
					EEntity_definition supertype_target_entity = null;
				  if (entity_mapping.testTarget(null)) {
				  	supertype_target = entity_mapping.getTarget(null);
		  		}
		  		if (supertype_target instanceof EEntity_definition) {
		  			supertype_target_entity = (EEntity_definition)supertype_target;
		  		}
					if (supertype_target_entity == null) {
						// will not happen, but if happens - skip
						continue;
					}
					// is supertype_target_entity the same or supertype of the current_target_entity?
//					pw.println("-- current target: " + current_target_entity.getName(null) + ", supertype target: " + supertype_target_entity.getName(null));
					
						// better use jsdai ways to determine the compatibility, this one will not work for multiple inheritance, etc.

//		pw.println("-- checking compatibility of ARM alternative, supertype " + arm_entity.getName(null) + " of  " + start_entity.getName(null));
						if (isCompatible(supertype_target_entity, current_target_entity)) {
							// here goes something, perhaps creating a separate Vector of indices, it would be good to know in advance
							// if there are more than one alternative, so better not to do the checking directly in this loop	
							compatible_entity_mappings.addElement(new Integer(entity_mappings.size()));
							compatible_entity_mappings__attributes.addElement(collectArmAttributes(entity_mapping, arm_entity, arm_model, map_model));
//							pw.println("-- compatible targets: " + current_target_entity.getName(null) + ", supertype target: " + supertype_target_entity.getName(null));
					
						}
//					if (supertype_target_class.isAssignableFrom(current_target_cluss)) {
//						entity_mappings.addElement(entity_mapping);
//					
//						}

				} // entity_mapping found for this arm entity
			} // go through all the mappings
			
			// if only one entity_mapping - no alternatives - one check, function name without suffix
			// if more than one entity_mapping, but only one compatible, - one check, add suffix to the function name
			// if more than one suitable entity_mapping, have to do something, perhaps to use an additional function,
			// or generate directly code

			EEntity_mapping supertype_entity_mapping = null;
			EEntity constraints = null;
			String constraint_label = null;
				
			if (compatible_entity_mappings.size() > 1) {
				// the worst case, not yet supported
//				pw.println("-- multiple compatible supertype alternatives found");
				multiple_status = 3;

			pw.println("-- multiple compatible supertype alternatives found");
			for (int ix = 0; ix < compatible_entity_mappings.size(); ix++) {
				Vector the_arm_attributes = (Vector)compatible_entity_mappings__attributes.elementAt(ix);


// ########################################## start

				if (the_arm_attributes.size() >0) {
					Iterator the_attribute_iterator = the_arm_attributes.iterator();
					while (the_attribute_iterator.hasNext()) {
						RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)the_attribute_iterator.next();
						if (arm_attribute.strong_count > 0) {
							continue;
						}
			
//						pw.println("-- non-strong supertype attribute, multiple compatible alternatives: " + arm_attribute.arm_attribute.getName(null));

//						boolean empty_if2 = true;

						if (arm_attribute.is_aggregate) {
//							empty_if2 = generateAggregateStrongARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name);
						} else {
//							empty_if2 = generateNonAggregateStrongARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name);
						}

			
///						pw.println("");
		
//		  			empty_if = empty_if && empty_if2;	
					}	

				} // not 0 attributes

// ##################################### end
			}
			
			
			
			 
			 if (false) { // this thing is handled in the method for strongs, in a different way
//			 if (!flag_swap_supertype_constraints) { // default - supertype entity_mapping constraints, if false, fails the rule
				int count_present = 0;
				for (int ii = 0; ii < compatible_entity_mappings.size(); ii++) {
					int valid_index = ((Integer)compatible_entity_mappings.elementAt(ii)).intValue() - 1;
					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(valid_index);
					if (supertype_entity_mapping.testConstraints(null)) {
						empty_if = false;
						count_present++;
						constraints = supertype_entity_mapping.getConstraints(null);
						constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();
						printComments("\t-- entity_mapping: " + supertype_entity_mapping);
						printComments("\t-- constraints: " + constraints);
						if (constraints instanceof EAttribute) {
							pw.println(indentString(ii) + "\t\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
						} else {
							pw.println(indentString(ii) + "\t\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
						}
//						if (ii == (compatible_entity_mappings.size()-1)) {
//							pw.println(indentString(ii) + "\t\tRETURN (FALSE);");
//						}
//						pw.println("\tEND_IF;");
					}	
				}
// check__altered_package_3 - no loops only return false - should not do this if constraints not present
 				if (count_present > 0) {
					pw.println(indentString(count_present) + "\tprintln(e);");
					pw.println(indentString(count_present) + "\t\tRETURN (FALSE);");
				}
				for (int ii = compatible_entity_mappings.size()-1; ii >= 0; ii--) {
					int valid_index = ((Integer)compatible_entity_mappings.elementAt(ii)).intValue() - 1;
					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(valid_index);
					if (supertype_entity_mapping.testConstraints(null)) {
						pw.println(indentString(ii) + "\t\tEND_IF;");
					}	
				}
			 } // generate according to flag
			} else 
			if (compatible_entity_mappings.size() == 1) {
				// may be one compatible or one altogether
				if (entity_mappings.size() > 1) {
					multiple_status = 2;

					// one compatible from several, the name of the function has to be modified
//					pw.println("-- one target-compatible supertype alternative found");
					int alternative_index = ((Integer)compatible_entity_mappings.elementAt(0)).intValue() - 1;
					arm_entity_name += "_" + (alternative_index+1);
					supertype_message = "SUPERTYPE: " + arm_entity_name + " ";
	
			 	 if (!flag_swap_supertype_constraints) { // default - supertype entity_mapping constraints, if false, fails the rule



					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(alternative_index);
					if (supertype_entity_mapping.testConstraints(null)) {
						empty_if = false;
						constraints = supertype_entity_mapping.getConstraints(null);
						constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();
						printComments("\t-- entity_mapping: " + supertype_entity_mapping);
						printComments("\t-- constraints: " + constraints);
						printComments("\t-- supertype: " + supertype_entity_mapping.getSource(null));
						if (constraints instanceof EAttribute) {
							pw.println("\t\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
						} else {
							pw.println("\t\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
						}
					  pw.println("\t\t\tprint(\'" + supertype_message + " \');");
						pw.println("\t\t\tprintln(e);");
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;");
					}	
				 } // if flag - swap is false
				} else {
					multiple_status = 1;
		
					// no alternatives, the name is without suffix
//					pw.println("-- one supertype entity_mapping found, no alternatives");

			 	 if (!flag_swap_supertype_constraints) { // default - supertype entity_mapping constraints, if false, fails the rule



					supertype_entity_mapping = (EEntity_mapping)entity_mappings.elementAt(0);
					if (supertype_entity_mapping.testConstraints(null)) {
						empty_if = false;
						constraints = supertype_entity_mapping.getConstraints(null);
						constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();
						printComments("\t-- entity_mapping: " + supertype_entity_mapping);
						printComments("\t-- constraints: " + constraints);
						if (constraints instanceof EAttribute) {
							pw.println("\t\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
						} else {
							pw.println("\t\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
						}
					  pw.println("\t\t\tprint(\'" + supertype_message + " \');");
						pw.println("\t\t\tprintln(e);");
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;");
					}	
				
         } // flag - swap is false				
				
				}
			} else {
					multiple_status = -1;

				// something wrong here
//					pw.println("--  no compatible supertype entity_mapping at all, from: " + entity_mappings.size());
			}					 
					 

// ###############################################################################################################

			if (multiple_status == 3) {
				// handled differently
				return false;
			}
	
			Vector arm_attributes = new Vector();
			Aggregate attributes = arm_model.getEntityExtentInstances(EAttribute.class);
			iter = attributes.createIterator();
//		Aggregate attribute_mappings = map_model.getEntityExtentInstances(EAttribute_mapping.class);
			Aggregate attribute_mappings = map_model.getEntityExtentInstances(EGeneric_attribute_mapping.class);
			SdaiIterator iter_map;
			while (iter.next()) {
				EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
				EEntity_definition parent = (EEntity_definition)an_attribute.getParent(null);
 				if (parent != arm_entity) continue;
				Vector attribute_mapping_alternatives = new Vector();
				iter_map = attribute_mappings.createIterator();
				while (iter_map.next()) {
//				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
					EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
					EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
//					if (an_entity_mapping == the_entity_mapping) {
					if (an_entity_mapping == supertype_entity_mapping) {
						EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
						if (an_attribute == a_mapped_attribute) {
							attribute_mapping_alternatives.addElement(an_attribute_mapping);
						}
					}	
				}	

				if (attribute_mapping_alternatives.size() > 0) {
					RuleGeneratorArmAttribute arm_attribute = new RuleGeneratorArmAttribute(an_attribute, attribute_mapping_alternatives);
					arm_attributes.addElement(arm_attribute);
				}

			}
// ######################

// new version, attribute mapping alternatives in different functions

    	// strong, mandatory or optional, but optional does not happen
			Iterator attribute_iterator = arm_attributes.iterator();
//		printDebug("\t\t-- strong, if present");
			pw.println("\t\t-- non-strong, if present in supertype " + arm_entity.getName(null));
			while (attribute_iterator.hasNext()) {
				RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();
				if (arm_attribute.strong_count > 0) {
					continue;
				}
			
// pw.println("-- attribute: " + arm_attribute.arm_attribute.getName(null) + ", alternatives: " + arm_attribute.mapping_alternatives.size() + ", value: " + arm_attribute.is_attribute_mapping_value);

				printDebug("-- non-strong supertype attributes, multiple alternatives: " + multiple_status);
				boolean empty_if2 = true;

				if (arm_attribute.is_aggregate) {
//				empty_if2 = generateAggregateStrongARMAttributeChecks(arm_attribute, arm_entity_name);
					empty_if2 = generateAggregateARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name, true);
				} else {
//				empty_if2 = generateMandatoryOrOptionalStrongARMAttributeChecks(arm_attribute, arm_entity_name);
//				empty_if2 = generateNonAggregateStrongARMAttributeChecks(arm_attribute, arm_entity_name);
					empty_if2 = generateNonAggregateARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name, true);
				}

			
				pw.println("");
		
		  	empty_if = empty_if && empty_if2;	
			}	



// ########################################################################################################
		} // a supertype
		return empty_if;
	
	}




	boolean isCompatible(EEntity_definition supertype_target_entity, EEntity_definition current_target_entity) throws SdaiException {
		if (supertype_target_entity == current_target_entity) {
			return true;
		}
		// see if supertype_target_entity is a supertype of current_target_entity
//		pw.println("-- checking compatibility between AIM " + current_target_entity.getName(null) + " and supertype " + supertype_target_entity.getName(null));

		HashSet processed = new HashSet();
		return recurseSupertypes(current_target_entity, supertype_target_entity, current_target_entity, processed);
	}
	
	boolean recurseSupertypes(EEntity_definition current_entity, EEntity_definition supertype_entity, EEntity_definition start_entity, HashSet processed) throws SdaiException {
		
		boolean result = false;

		if (!processed.add(current_entity)) {
			// repeated inheritance, already done
			return false;
		}
	
		AEntity_or_view_definition supertypes = current_entity.getGeneric_supertypes(null);
  	SdaiIterator isuper = supertypes.createIterator();

  	while (isuper.next()) {
  		EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(isuper);
//			pw.println("\t\t-- recursing supertypes: from " + arm_entity.getName(null) + " to " + supertype.getName(null));
			result = recurseSupertypes(supertype, supertype_entity, start_entity, processed);

			// should be here
			if (result) {
				return true;
			}
		}
		
		// should not be here
//		if (result) {
//			return true;
//		}
		
		if (current_entity == supertype_entity) { 
			return true;
		}
	
	
		return false;	
	
	} // end of function
	


	// version 2
	void printFunction2(EEntity_definition arm_entity, EEntity_mapping the_entity_mapping, int i, int count, SdaiModel arm_model, SdaiModel aim_model, SdaiModel map_model) throws SdaiException {
		Vector result = new Vector();
		String arm_entity_name = arm_entity.getName(null).toLowerCase();
		indentation = 0;
		EEntity aim_target = the_entity_mapping.getTarget(null);
		if (count > 1) {
			arm_entity_name += "_" + (i + 1);
		}
		
		String aim_entity_name = "_NONAME_";
		if (aim_target instanceof EEntity_definition) {
			if (((EEntity_definition)aim_target).getComplex(null)) {
				// complex entity
//				System.out.println("complex entity: " + aim_target);
				aim_entity_name = getRootSupertypeName((EEntity_definition)aim_target);
			} else {
				aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase();
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '$');
//			aim_entity_name = ((EEntity_definition)aim_target).getName(null).toLowerCase().replace('+', '_');
			}
		} else {
			aim_entity_name = "_ATTRIBUTE_";
		}
		pw.println("FUNCTION check__" + arm_entity_name + "(e : " + aim_entity_name + "; mode : check_mode) : boolean;");
		pw.println("\tLOCAL");
		// pw.println("\t\tresult : AGGREGATE OF GENERIC;");
		pw.println("\t\tresults : AGGREGATE OF GENERIC;");
		pw.println("\t\tsize : INTEGER;");
		// if attribute_mapping_value, but not so easy to check here, perhaps just have count, needed or not.
		pw.println("\t\tcount : INTEGER;");
		
		pw.println("\t\tfailure_in_supertypes : LOGICAL;");
		pw.println("\t\tskipping_instance_in_supertype : LOGICAL;");
		pw.println("\t\tok_alternative_found : LOGICAL;");
		pw.println("\t\tskip_alternative : LOGICAL;");
		pw.println("\t\tall_non_strong_passed : LOGICAL;");
		pw.println("\t\tfailing_in_supertypes : STRING;");
//		pw.println("\t\tfailure_in_supertypes : BOOLEAN;");
//		pw.println("\t\tskipping_instance_in_supertype : BOOLEAN;");
//		pw.println("\t\tok_alternative_found : BOOLEAN;");
//		pw.println("\t\tskip_alternative : BOOLEAN;");
//		pw.println("\t\tall_non_strong_passed : BOOLEAN;");

		pw.println("\tEND_LOCAL;");

		// check here in the case of complex entity if it is an instance of the correct complex entity
		EEntity the_aim_target = the_entity_mapping.getTarget(null);
		boolean is_complex = false;
		Vector complex_part_names = null;
		if (aim_target instanceof EEntity_definition) {
			if (((EEntity_definition)the_aim_target).getComplex(null)) {
				// complex entity
				is_complex = true;
				complex_part_names = new Vector();
			  AEntity_or_view_definition its_supertypes = ((EEntity_definition)aim_target).getGeneric_supertypes(null);
    		SdaiIterator iter_super = its_supertypes.createIterator();
		    while (iter_super.next()) {
      		EEntity_definition super_entity = (EEntity_definition) its_supertypes.getCurrentMember(iter_super);
					String super_name = super_entity.getName(null);
					complex_part_names.addElement(super_name);
				}
 			}	
 		}
		if (is_complex) {
			boolean first_time = true;
			Iterator it_super = complex_part_names.iterator();						
			String rule_schema_name = AIM_schema_name.toUpperCase();
			String a_line;
			pw.println("\n\tIF NOT (");
			while (it_super.hasNext()) {
				String a_name = ((String)it_super.next()).toUpperCase();
				a_line = "\t\t";
				if (!first_time) {
					a_line += "AND ";
				} else {
					first_time = false;
				}
//				a_line += "(\'" + rule_schema_name + ".\' + \'" + a_name + "\' IN TYPEOF(i))"; 
				a_line += "(\'" + AIM_schema_name + ".\' + \'" + a_name + "\' IN TYPEOF(e))"; 
				pw.println(a_line);
			}
	
			pw.println("\t) THEN");
				pw.println("\t\tRETURN (TRUE);");
			pw.println("\tEND_IF;");
		}


		EEntity constraints = null;
		if (the_entity_mapping.testConstraints(null)) {
			constraints = the_entity_mapping.getConstraints(null);
			result.addElement(constraints);
//			processNext(constraints);
			String constraint_label = constraints.getPersistentLabel().substring(1).toLowerCase();


		printComments("\t-- entity_mapping: " + the_entity_mapping);
		printComments("\t-- constraints: " + constraints);
//		pw.println("\tIF NOT f" + constraint_label + "(e) THEN");
		if (constraints instanceof EAttribute) {
			pw.println("\tIF NOT EXISTS(e\\" + ((EAttribute)constraints).getParent(null).getName(null).toLowerCase() + "." + ((EAttribute)constraints).getName(null).toLowerCase() + ") THEN");
		} else {
			pw.println("\tIF NOT EXISTS(f" + constraint_label + "(e)) THEN");
		}
//		pw.println("\t\tRETURN (?);");
		// if entity_mapping fails, then this instance is not in the mapped population, skip it
		pw.println("\t\tRETURN (TRUE);");
		pw.println("\tEND_IF;");
//		generateFxxFunction(constraints, map_model);

		} else {
			// perhaps no need to generate functions and rules at all if no constraints?
//			System.out.println("no constraints for ARM entity " + arm_entity_name);
			printDebug("\t-- no constraints: " + constraints);
		} 
		
//  changed (temporarily) because not all express tools support >= with enumerations
//		pw.println("\n\tIF mode >= mandatory THEN\n");
		pw.println("\n\tIF (mode = mandatory) OR (mode = detailed) OR (mode = recursive) THEN\n");

		boolean empty_if = true;
		// go through mandatory attributes
		Vector arm_attributes = new Vector();
		Aggregate attributes = arm_model.getEntityExtentInstances(EAttribute.class);
		SdaiIterator iter = attributes.createIterator();
//		Aggregate attribute_mappings = map_model.getEntityExtentInstances(EAttribute_mapping.class);
		Aggregate attribute_mappings = map_model.getEntityExtentInstances(EGeneric_attribute_mapping.class);
		SdaiIterator iter_map;
		while (iter.next()) {
			EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
			EEntity_definition parent = (EEntity_definition)an_attribute.getParent(null);
 			if (parent != arm_entity) continue;
			Vector attribute_mapping_alternatives = new Vector();
			iter_map = attribute_mappings.createIterator();
			while (iter_map.next()) {
//				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
				EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
				EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
				if (an_entity_mapping == the_entity_mapping) {
					EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
					if (an_attribute == a_mapped_attribute) {
						attribute_mapping_alternatives.addElement(an_attribute_mapping);
					}
				}	
			}	

			if (attribute_mapping_alternatives.size() > 0) {
				RuleGeneratorArmAttribute arm_attribute = new RuleGeneratorArmAttribute(an_attribute, attribute_mapping_alternatives);
				arm_attributes.addElement(arm_attribute);
			}
			
			
			
		
			// probably no need to generate anything for ARM attributes that do not have attirbute_mapping at all.
			// old version, to be removed
//			if (attribute_mapping_alternatives.size() > 0) {
			if (attribute_mapping_alternatives.size() == 1234567890) {
				// assume that all are stron or not strong. This assumption is not safe, though !!!!!!!!!!!!!!!!
				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mapping_alternatives.elementAt(0);
				if (an_attribute instanceof EExplicit_attribute) {
					EExplicit_attribute xa = (EExplicit_attribute)an_attribute;
//					if (!xa.getOptional_flag(null)) {
//       			pw.println("\t\tsize := (get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e,a));");
       			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						empty_if = false;
					if (an_attribute_mapping.getStrong(null)) {
//						pw.println("\t\tIF mode >= mandatory THEN");
//						pw.println("\t\tIF SIZEOF (get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e,a) <> 1)");
						pw.println("\t\tIF SIZE <> 1 THEN");
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;\n");
					} else { 
						// not strong mandatory
//						pw.println("\t\tELSE");
//						pw.println("\t\t\tELSIF SIZE > 1");
						pw.println("\t\tIF SIZE > 1 THEN");
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;\n");
//						pw.println("\t\tEND_IF;");
					}
				} 
			}
		}
		


// new version, attribute mapping alternatives in different functions

    // strong, mandatory or optional, but optional does not happen
		Iterator attribute_iterator = arm_attributes.iterator();
//		printDebug("\t\t-- strong, if present");
		pw.println("\t\t-- strong, if present");
		while (attribute_iterator.hasNext()) {
			RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();
			if (arm_attribute.strong_count == 0) {
				continue;
			}
			
// pw.println("-- attribute: " + arm_attribute.arm_attribute.getName(null) + ", alternatives: " + arm_attribute.mapping_alternatives.size() + ", value: " + arm_attribute.is_attribute_mapping_value);

			boolean empty_if2 = true;

		if (arm_attribute.is_aggregate) {
//			empty_if2 = generateAggregateStrongARMAttributeChecks(arm_attribute, arm_entity_name);
			empty_if2 = generateAggregateStrongARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name);
		} else {
//			empty_if2 = generateMandatoryOrOptionalStrongARMAttributeChecks(arm_attribute, arm_entity_name);
//			empty_if2 = generateNonAggregateStrongARMAttributeChecks(arm_attribute, arm_entity_name);
			empty_if2 = generateNonAggregateStrongARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name);
		}

			
/*
      if (arm_attribute.is_attribute_mapping_value) {
				empty_if2 = generateArmStrongAttributeChecksValue(arm_attribute, arm_entity_name);
      } else {
				empty_if2 = generateArmStrongAttributeChecks(arm_attribute, 1, arm_entity_name);
			}
*/
			pw.println("");
		
		  empty_if = empty_if && empty_if2;	
		}	
		
		pw.println("\tfailure_in_supertypes := FALSE;");		

		HashSet processed = new HashSet();
		boolean empty_if2 = recurseSupertypesForSkipping(arm_entity, arm_entity, arm_model, map_model, the_entity_mapping, count, i, processed);
	  empty_if = empty_if && empty_if2;	

		
		// after current entity_mapping constraints and strong attributes but before anything else:
		pw.println("\tIF failure_in_supertypes THEN");
			pw.println("\t\tprint(\'failure in supertypes on instance \');");
			pw.println("\t\tprintln(e);");
			pw.println("\t\tRETURN (FALSE);");
		pw.println("\tEND_IF;");	

		
		// older version, with multiple attribute mapping alternatives in one function
	if (false) {	
		// strong, does not happen optional in ap210
		attribute_iterator = arm_attributes.iterator();
//		printDebug("\t\t-- strong, if present");
		pw.println("\t\t-- strong, if present");
		while (attribute_iterator.hasNext()) {
			RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();

			if (arm_attribute.is_attribute_mapping && arm_attribute.is_attribute_mapping_value) {
				// does not happen in current ap210
				pw.println("-- both attribute_mapping and attribute_mapping_value in different alternatives: " + arm_attribute.arm_attribute);
			}			

			if (arm_attribute.strong_count == 0) {
				continue;
			}
			if (arm_attribute.strong_count < arm_attribute.number_of_alternatives) {
				printDebug("-- both strong and not strong in different alternatives: " + arm_attribute.arm_attribute);
			} 
			
			/*
			
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
				if (((EExplicit_attribute)arm_attribute.arm_attribute).getOptional_flag(null)) {
					pw.println("-- strong optional attribute: " + arm_attribute.arm_attribute.getName(null));
				}
			}
			
			if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
				pw.println("-- derived attribute: " + arm_attribute.arm_attribute.getName(null));
				if (((EDerived_attribute)arm_attribute.arm_attribute).testRedeclaring(null)) {
					// not in ap210
					pw.println("-- redeclaring derived attribute: " + arm_attribute.arm_attribute.getName(null));
				}
			} else
			if (arm_attribute.arm_attribute instanceof EInverse_attribute) {
				// not in ap210
				pw.println("-- inverse attribute: " + arm_attribute.arm_attribute.getName(null));
				if (((EInverse_attribute)arm_attribute.arm_attribute).testRedeclaring(null)) {
					// not in ap210
					pw.println("-- redeclaring inverse attribute: " + arm_attribute.arm_attribute.getName(null));
				}
			} else
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
				if (((EExplicit_attribute)arm_attribute.arm_attribute).testRedeclaring(null)) {
					pw.println("-- redeclaring explicit attribute: " + arm_attribute.arm_attribute.getName(null));
				}
			}

			*/
			
//			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
			if ((arm_attribute.arm_attribute instanceof EExplicit_attribute) || (arm_attribute.arm_attribute instanceof EDerived_attribute)) {
//				EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
				EAttribute xa = (EAttribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
		   			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						empty_if = false;
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":?]");
						//  [something : ?] - checking only min:
						pw.println("\t\tIF size < " + arm_attribute.min_cardinality + " THEN");
							pw.println("\t\t\tRETURN (TRUE);");
						pw.println("\t\tEND_IF;\n");
					} else {
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
		   			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						empty_if = false;
						// both defined
						pw.println("\t\tIF (size < " + arm_attribute.min_cardinality + ") OR (SIZE > " + arm_attribute.max_cardinality + ") THEN");
							pw.println("\t\t\tRETURN (TRUE);");
						pw.println("\t\tEND_IF;\n");
					}
				} else {
		   		pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
					empty_if = false;
					pw.println("\t\tIF size <> 1 THEN");
					pw.println("\t\t\tRETURN (TRUE);");
					pw.println("\t\tEND_IF;\n");
				}
			}
		}
	
	} // if (false) - commented out	

// new version, attribute mapping alternatives in different functions

    // non-strong, mandatory or optional
		attribute_iterator = arm_attributes.iterator();
//		printDebug("\t\t-- non-strong, if present");
		pw.println("\t\t-- non-strong, if present");
		while (attribute_iterator.hasNext()) {
			RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();
			if (arm_attribute.strong_count > 0) {
				continue;
			}
			
// pw.println("-- attribute: " + arm_attribute.arm_attribute.getName(null) + ", alternatives: " + arm_attribute.mapping_alternatives.size() + ", value: " + arm_attribute.is_attribute_mapping_value);


/*
			boolean empty_if2 = true;
      if (arm_attribute.is_attribute_mapping_value) {
				empty_if2 = generateArmAttributeChecksValue(arm_attribute, arm_entity_name);
      } else {
				empty_if2 = generateArmAttributeChecks(arm_attribute, 1, arm_entity_name);
			}
			pw.println("");
		
		  empty_if = empty_if && empty_if2;	
		}	

*/

		empty_if2 = true;
		if (arm_attribute.is_aggregate) {
			empty_if2 = generateAggregateARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name, false);
		} else {
//			empty_if2 = generateMandatoryOrOptionalARMAttributeChecks(arm_attribute, arm_entity_name);
//			empty_if2 = generateNonAggregateARMAttributeChecks(arm_attribute, arm_entity_name);
			empty_if2 = generateNonAggregateARMAttributeChecksSingleFunction(arm_attribute, arm_entity_name, false);
		}

		pw.println("");

		
	  empty_if = empty_if && empty_if2;	

	}
	
		processed.clear();
		empty_if2 = recurseSupertypesForFailure(arm_entity, arm_entity, arm_model, map_model, the_entity_mapping, count, i, processed);
	  empty_if = empty_if && empty_if2;	
	


// previous version with attribute mapping alternatives in a single function
if (false) {

    // non-strong, mandatory or optional
		attribute_iterator = arm_attributes.iterator();
//		printDebug("\t\t-- non-strong, if present");
		pw.println("\t\t-- non-strong, if present");
		while (attribute_iterator.hasNext()) {
			RuleGeneratorArmAttribute arm_attribute = (RuleGeneratorArmAttribute)attribute_iterator.next();
			if (arm_attribute.strong_count > 0) {
				continue;
			}

			/*
			if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
				pw.println("-- derived attribute: " + arm_attribute.arm_attribute.getName(null));
				if (((EDerived_attribute)arm_attribute.arm_attribute).testRedeclaring(null)) {
					pw.println("-- redeclaring derived attribute: " + arm_attribute.arm_attribute.getName(null));
				}
			} else
			if (arm_attribute.arm_attribute instanceof EInverse_attribute) {
				pw.println("-- inverse attribute: " + arm_attribute.arm_attribute.getName(null));
				if (((EInverse_attribute)arm_attribute.arm_attribute).testRedeclaring(null)) {
					pw.println("-- redeclaring inverse attribute: " + arm_attribute.arm_attribute.getName(null));
				}
			} else
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
				if (((EExplicit_attribute)arm_attribute.arm_attribute).testRedeclaring(null)) {
					pw.println("-- redeclaring explicit attribute: " + arm_attribute.arm_attribute.getName(null));
				}
			}
			*/

//			if ((arm_attribute.arm_attribute instanceof EExplicit_attribute) || (arm_attribute.arm_attribute instanceof EDerived_attribute)) {
			if (arm_attribute.arm_attribute instanceof EExplicit_attribute) {
				EExplicit_attribute xa = (EExplicit_attribute)arm_attribute.arm_attribute;
//				EAttribute xa = (EAttribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
						if (!xa.getOptional_flag(null)) {
			   			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
							empty_if = false;
							//  [something : ?] - checking only min:
							printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":?]");
							pw.println("\t\tIF size < " + arm_attribute.min_cardinality + " THEN");
								pw.println("\t\t\tRETURN (FALSE);");
							pw.println("\t\tEND_IF;\n");
						} else {
							// for optional nothing to check, always ok ?
							printDebug("\t\t-- OPTIONAL [n:?] any size ok - not generated: ");
			   			printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						}
					} else {
		   			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						empty_if = false;
						// both defined
						if (!xa.getOptional_flag(null)) {
							printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println("\t\tIF (size < " + arm_attribute.min_cardinality + ") OR (SIZE > " + arm_attribute.max_cardinality + ") THEN");
						} else {
							printDebug("\t\t-- OPTIONAL aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
							pw.println("\t\tIF size > " + arm_attribute.max_cardinality + " THEN");
						}
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;\n");
					}
				} else {
		   		pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
					empty_if = false;
					if (!xa.getOptional_flag(null)) {
						pw.println("\t\tIF size <> 1 THEN");
					} else {
						printDebug("\t\t-- OPTIONAL");
						pw.println("\t\tIF size > 1 THEN");
					}
					pw.println("\t\t\tRETURN (FALSE);");
					pw.println("\t\tEND_IF;\n");
				}
			} else // derived attribute, can not be optional
			if (arm_attribute.arm_attribute instanceof EDerived_attribute) {
				EDerived_attribute xa = (EDerived_attribute)arm_attribute.arm_attribute;
//				EAttribute xa = (EAttribute)arm_attribute.arm_attribute;
				if (arm_attribute.is_aggregate) {
					if (arm_attribute.min_cardinality == Integer.MIN_VALUE) {
						// both must be unset, nothing to check
						printDebug("\t\t-- [0:?] any size ok - not generated: ");
				   	printDebug("\t\t-- size := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
					} else
					if (arm_attribute.max_cardinality == Integer.MIN_VALUE) {
		   			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						empty_if = false;
						//  [something : ?] - checking only min:
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":?]");
						pw.println("\t\tIF size < " + arm_attribute.min_cardinality + " THEN");
							pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;\n");
					} else {
		   			pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
						empty_if = false;
						// both defined
						printDebug("\t\t-- aggregate [" + arm_attribute.min_cardinality + ":" + arm_attribute.max_cardinality + "]");
						pw.println("\t\tIF (size < " + arm_attribute.min_cardinality + ") OR (SIZE > " + arm_attribute.max_cardinality + ") THEN");
						pw.println("\t\t\tRETURN (FALSE);");
						pw.println("\t\tEND_IF;\n");
					}
				} else {
		   		pw.println("\t\tsize := SIZEOF(get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e));");
					empty_if = false;
					pw.println("\t\tIF size <> 1 THEN");
					pw.println("\t\t\tRETURN (FALSE);");
					pw.println("\t\tEND_IF;\n");
				}
			} // derived attribute
		}

} // commented out with if (false) - older version with attribute mapping alternatives in a single function

		if (empty_if) {
			pw.println("\t\t;");
		}
		pw.println("\tEND_IF;\n");
		
		pw.println("\tRETURN (TRUE);");
		pw.println("END_FUNCTION;");

//		generateFxxFunction(constraints, map_model);
	
		// generate get.. functions for attributes
		
		// new version with a separate function for each alternative
		
		
		

//		result = generateAttribute_mappingFunctions(arm_attributes, arm_entity_name, aim_entity_name, result);
		result = generateSingleAttribute_mappingFunctions(arm_attributes, arm_entity_name, aim_entity_name, result);


		// newer shorter version, but still with alternatives inside a single function

if (false) {


		iter = attributes.createIterator();
		while (iter.next()) {
			EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
			EEntity_definition parent = (EEntity_definition)an_attribute.getParent(null);
 			if (parent != arm_entity) continue;
			Vector attribute_mapping_alternatives = new Vector();
			iter_map = attribute_mappings.createIterator();
			while (iter_map.next()) {
//				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
				EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
				EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
				if (an_entity_mapping == the_entity_mapping) {
					EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
					if (an_attribute == a_mapped_attribute) {
						attribute_mapping_alternatives.addElement(an_attribute_mapping);
					}
				}	
			}	
// System.out.println("--REMOVE-02 - before mapping_alternatives: " + an_attribute);

			if (attribute_mapping_alternatives.size() > 0) {
// System.out.println("--REMOVE-03 - mapping_alternatives > 0");
	    
//				if (an_attribute instanceof EExplicit_attribute) {
				if ((an_attribute instanceof EExplicit_attribute) || (an_attribute instanceof EDerived_attribute)){
//					EExplicit_attribute xa = (EExplicit_attribute)an_attribute;
					EAttribute xa = (EAttribute)an_attribute;
//					if (!xa.getOptional_flag(null)) {
					pw.println("");
					printComments("-- explicit attribute " + xa.getName(null).toLowerCase());
//					pw.println("FUNCTION get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e : " + aim_entity_name + ") : BAG OF " + aim_entity_name + ";");
//					pw.println("FUNCTION get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e : " + aim_entity_name + ") : GENERIC;");
					pw.println("FUNCTION get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e : " + aim_entity_name + ") : BAG OF GENERIC;");

					pw.println("LOCAL");
//					pw.println("\tresult : BAG OF " + aim_entity_name + " := [];");
					pw.println("\tresult_aggregate : BAG OF GENERIC;");
					pw.println("\tresult : GENERIC;");
					pw.println("\tsize : INTEGER := ?;");
					
					pw.println("END_LOCAL;");

					boolean at_least_one = false;
					for (i = 0; i < attribute_mapping_alternatives.size(); i++) {
//						EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mapping_alternatives.elementAt(i);
						EGeneric_attribute_mapping an_attribute_mapping = (EGeneric_attribute_mapping)attribute_mapping_alternatives.elementAt(i);

//						String parameter_type_name = "GENERIC_ENTITY";
						String parameter_type_name = "GENERIC";
						EEntity_definition target_attribute_value_domain = null;
						EEntity target_attribute = null;

            
						EEntity attribute_constraints = null;
            if ( an_attribute_mapping.testConstraints(null)) {
							attribute_constraints = an_attribute_mapping.getConstraints(null);
							String function_name = null;
							String attribute_name = null;
							String parent_entity_name = null;
printDebug("--REMOVE-01 attribute_constraints: " + attribute_constraints);
							if (attribute_constraints instanceof EAttribute) {
								attribute_name = ((EAttribute)attribute_constraints).getName(null).toLowerCase();
							  parent_entity_name = ((EAttribute)attribute_constraints).getParent(null).getName(null).toLowerCase();
							} else {
	            	function_name = "f" + attribute_constraints.getPersistentLabel().substring(1).toLowerCase();
								result.addElement(attribute_constraints);
							}
		

	            at_least_one = true;
	            if (i < attribute_mapping_alternatives.size()-1) {
//  	          	pw.println("\t\t\tresult := " + function_name + "(e);");
//								pw.println("\tIF SIZEOF(result) > 0 THEN");
//								pw.println("\t\tRETURN (result);");
//								pw.println("\tEND_IF;");



              if (attribute_constraints instanceof EAttribute) {

		pw.println("\tIF EXISTS (e) THEN");
  	          	pw.println("\t\tresult := e\\" + parent_entity_name + "." + attribute_name + ";");
		pw.println("\tEND_IF;");	

              } else {
  	          	pw.println("\tresult := " + function_name + "(e);");
							}
	pw.println("\tIF \'BAG\' IN TYPEOF(result) THEN");
		pw.println("\t\tsize := SIZEOF(result);");
	pw.println("\tEND_IF;");
	pw.println("\tIF EXISTS(size) THEN");
		pw.println("\t\tIF SIZE > 0 THEN");
			pw.println("\t\t\tRETURN (result);");
		pw.println("\t\tELSE");
			pw.println("\t\t\tSIZE := ?;");
		pw.println("\t\tEND_IF;");
	pw.println("\tELSE");
		pw.println("\t\tIF EXISTS(result) THEN");
			pw.println("\t\t\tresult_aggregate := [];");
			pw.println("\t\t\tresult_aggregate := result_aggregate + result;");
			pw.println("\t\t\tRETURN (result_aggregate);");
		pw.println("\t\t\tEND_IF;");
	pw.println("\tEND_IF;");




							} else {
//	          	  pw.println("\t\tRETURN (" + function_name + "(e));");

              if (attribute_constraints instanceof EAttribute) {

		pw.println("\tIF EXISTS (e) THEN");
  	          	pw.println("\t\tresult := e\\" + parent_entity_name + "." + attribute_name + ";");
		pw.println("\tEND_IF;");	

              } else {
  	          	pw.println("\tresult := " + function_name + "(e);");
							}
	pw.println("\tIF NOT EXISTS(result) THEN");
		pw.println("\t\tresult_aggregate := [];");
		pw.println("\t\tRETURN (result_aggregate);");
	pw.println("\tEND_IF;");						
	pw.println("\tIF 'BAG' IN TYPEOF(result) THEN");
		pw.println("\t\tRETURN (result);");
	pw.println("\tELSE");
		pw.println("\t\tresult_aggregate := [];");
		pw.println("\t\tresult_aggregate := result_aggregate + result;");
		pw.println("\t\tRETURN (result_aggregate);");
	pw.println("\tEND_IF;");



							}
//							generateFxxFunction(an_attribute_mapping.getConstraints(null), map_model);

				    } else {
				    	// optional constraints unset - attribute mapping not constrained
				    }
				        


					} // for
					if (!at_least_one) {
						// no return generated - what to return ???  - a bit of a problem, better not to test at all if no constraints.
						// the problem is this - if one alternative has constraints and another - does not. Probably does not happen. 
						
         	  pw.println("\t\tRETURN (?);");
					}
					pw.println("END_FUNCTION;");
				}
			} // attribute_mapping_alternatives.size() > 0
		} // while iter through all arm attributes

} // if  newer shorter version but still with alternatives inside, a single function for all the alternatives



    // old version
   if (false) {
		iter = attributes.createIterator();
		while (iter.next()) {
			EAttribute an_attribute = (EAttribute)attributes.getCurrentMemberObject(iter);
			EEntity_definition parent = (EEntity_definition)an_attribute.getParent(null);
 			if (parent != arm_entity) continue;
			Vector attribute_mapping_alternatives = new Vector();
			iter_map = attribute_mappings.createIterator();
			while (iter_map.next()) {
				EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mappings.getCurrentMemberObject(iter_map);
				EEntity_mapping an_entity_mapping = an_attribute_mapping.getParent_entity(null);
				if (an_entity_mapping == the_entity_mapping) {
					EAttribute a_mapped_attribute = an_attribute_mapping.getSource(null);
					if (an_attribute == a_mapped_attribute) {
						attribute_mapping_alternatives.addElement(an_attribute_mapping);
					}
				}	
			}	

			if (attribute_mapping_alternatives.size() > 0) {

				if (an_attribute instanceof EExplicit_attribute) {
					EExplicit_attribute xa = (EExplicit_attribute)an_attribute;
//					if (!xa.getOptional_flag(null)) {
						pw.println("");
						printComments("-- explicit attribute " + xa.getName(null).toLowerCase());
// set->bag						pw.println("FUNCTION get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e : " + aim_entity_name + ") : SET OF " + aim_entity_name + ";");
						pw.println("FUNCTION get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "(e : " + aim_entity_name + ") : BAG OF " + aim_entity_name + ";");

						for (i = 0; i < attribute_mapping_alternatives.size(); i++) {
							EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mapping_alternatives.elementAt(i);
							String parameter_type_name = "GENERIC";

							EEntity_definition target_attribute_value_domain = null;
							EEntity target_attribute = null;

							// calculate specific parameter_type_name here, if possible - TBD


/*
							// RRObjectMapping om = new RRObjectMapping();
    					java.util.HashSet touched_instances = new java.util.HashSet(); 
//    					RRObjectMapping om = new RRObjectMapping(ASdaiModel data, ASdaiModel mapping, touched_instances);
    					RRObjectMapping om = new RRObjectMapping(null, null, touched_instances);
							int mode = 0;
//	public boolean rrTestMappedAttribute(jsdai.lang.EEntity targetInstance, EGeneric_attribute_mapping sourceAttribute, int mode) throws jsdai.lang.SdaiException {
//							boolean tested_attribute_mapping = om.rrTestMappedAttribute((EEntity)aim_target, (EGeneric_attribute_mapping)an_attribute_mapping, (int)0);
						
								boolean tested_attribute_mapping = om.rrTestMappedAttribute((jsdai.lang.EEntity) null, (EGeneric_attribute_mapping) null, (int) mode);

System.out.println("test: " + tested_attribute_mapping);							

*/

					   // an_attribute_mapping.path[last]
							if (an_attribute_mapping.testPath(null)) {
								AAttribute_mapping_path_select path = an_attribute_mapping.getPath(null);
								EEntity last = path.getByIndex(path.getMemberCount());
								if (last instanceof EPath_constraint) {
									EEntity last_path_element1 = ((EPath_constraint)last).getElement1(null); 
									if (last_path_element1 instanceof EEntity_constraint) {
										target_attribute_value_domain = ((EEntity_constraint)last_path_element1).getDomain(null);
										target_attribute = ((EEntity_constraint)last_path_element1).getAttribute(null);
										if (target_attribute instanceof EExplicit_attribute) {
											EEntity_definition target_attribute_parent_entity = (EEntity_definition)((EExplicit_attribute)target_attribute).getParent(null); 
//											EEntity_definition target_attribute_parent_entity = (EEntity_definition)((EExplicit_attribute)target_attribute).getParent(null); 
											parameter_type_name = target_attribute_parent_entity.getName(null).toLowerCase();
										} else {
											// not yet supported
										}
									} else {
										// not yet supported
									}
								} else {
									// other cases to be implemented
									// but perhaps the whole implementation should be based on the constraints below
								} 
							} else {
								// path is optional and deprecated, obviously not available, have to use constraints
							}



							pw.println("\tFUNCTION get_alt" + (i+1) + "_step2(e : " + parameter_type_name + ") : boolean;");
              if ((target_attribute_value_domain != null) && (target_attribute != null)) {
								pw.println("\t\tLOCAL");
              	if (target_attribute instanceof EExplicit_attribute) {
//	              	pw.println("\t\t\tee : " + target_attribute_value_domain.getName(null).toLowerCase() + " := e." + ((EExplicit_attribute)target_attribute).getName(null).toLowerCase() + ";");
	              	pw.println("\t\t\tee : " + target_attribute_value_domain.getName(null).toLowerCase() + " := e\\" + ((EAttribute)target_attribute).getParent(null).getName(null).toLowerCase()  + "." + ((EExplicit_attribute)target_attribute).getName(null).toLowerCase() + ";");
								} else {
									// making it GENERIC_ENTITY for now.
//	              	pw.println("\t\t\tee : GENERIC_ENTITY;");
	              	pw.println("\t\t\tee : GENERIC;");
								}
								pw.println("\t\tEND_LOCAL;");
    

/*
              	// I think here we need to invoke the function that tests the ARM entity which is the domain of the attribute
              	String function_name = "f_NOT_ENTITY"; 
              	EEntity arm_attribute_domain = xa.getDomain(null);
              	if (arm_attribute_domain instanceof EEntity_definition) {
	              	function_name = "f" + arm_attribute_domain.getPersistentLabel().substring(1).toLowerCase();
              	}
*/				        
								// no, wrong! it will be tested separatelly. here, test the constraints for the mapping of this attribute	

	              String function_name = "f" + an_attribute_mapping.getConstraints(null).getPersistentLabel().substring(1).toLowerCase();
				        
				        pw.println("\t\tIF " + function_name + "(ee) THEN");
                pw.println("\t\t\tresult := result + ee;");
                pw.println("\t\t\tRETURN (TRUE);");
        				pw.println("\t\tEND_IF;");
//								generateFxxFunction(an_attribute_mapping.getConstraints(null), map_model);
                result.addElement(an_attribute_mapping.getConstraints(null));  
             }

							pw.println("\t\tRETURN (FALSE);");
							pw.println("\tEND_FUNCTION;");
						}

						for (i = 0; i < attribute_mapping_alternatives.size(); i++) {
							EAttribute_mapping an_attribute_mapping = (EAttribute_mapping)attribute_mapping_alternatives.elementAt(i);
							String parameter_type_name = aim_entity_name;
							// calculate specific parameter_type_name here, if possible - TBD
							pw.println("\tFUNCTION get_alt" + (i+1) + "_step1(e : " + parameter_type_name + ") : boolean;");
							pw.println("\tEND_FUNCTION;");
						}


						pw.println("LOCAL");
// set->bag						pw.println("\tresult : SET OF " + aim_entity_name + " := [];");
						pw.println("\tresult : BAG OF " + aim_entity_name + " := [];");
						pw.println("END_LOCAL;");
//						pw.println("\tIF get__" + arm_entity_name + "__" + xa.getName(null).toLowerCase() + "_alt1_step1(e) THEN");
						pw.println("\tIF get_alt1_step1(e) THEN");
						pw.println("\t\tRETURN (result);");
						pw.println("\tEND_IF");
						pw.println("\tRETURN (?);");
						pw.println("END_FUNCTION;");
						
//					}
				} 
			}
		}
	 } // switching out with if (false)	
	 // generate needed fxxx functions	
		if (result != null) {
			Iterator fxx_iterator = result.iterator();
			while (fxx_iterator.hasNext()) {
				EEntity next_constraint = (EEntity)fxx_iterator.next();
// System.out.println("--REMOVE - generate function: " + next_constraint);
// printDebug("--REMOVE - generate function: " + next_constraint);
				generateFxxFunction(next_constraint, map_model);
			}
		} 

	}


	void processNext(EEntity current)  throws SdaiException {
		indentation++;
		if (current instanceof EEntity_constraint) {
			processEntity_constraint((EEntity_constraint)current);
		} else
		if (current instanceof EExplicit_attribute) {
			processExplicit_attribute((EExplicit_attribute)current);
		} else
		if (current instanceof EAnd_constraint_relationship) {
			processAnd_constraint_relationship((EAnd_constraint_relationship)current);
		} else
		if (current instanceof EOr_constraint_relationship) {
			processOr_constraint_relationship((EOr_constraint_relationship)current);
		} else
		if (current instanceof EPath_constraint) {
			processPath_constraint((EPath_constraint)current);
		} else
		if (current instanceof ENegation_constraint) {
			processNegation_constraint((ENegation_constraint)current);
		} else
		if (current instanceof EAttribute_value_constraint) {
			processAttribute_value_constraint((EAttribute_value_constraint)current);
		} else 
		if (current instanceof EInverse_attribute_constraint) {
			processInverse_attribute_constraint((EInverse_attribute_constraint)current);
		} else 
		if (current instanceof EAggregate_member_constraint) {
			processAggregate_member_constraint((EAggregate_member_constraint)current);
		} else 
		if (current instanceof EInstance_equal) {
			processInstance_equal((EInstance_equal)current);
		} else {
			// internal error - or implementation must be expanded
			System.out.println("ERROR - other constraint: " + current);
			pw.println(addTabs() + "-- unknown constraint !!!: " + current);
		}	
		indentation--;
	}

	void processEntity_constraint(EEntity_constraint entity_constraint)  throws SdaiException {
		pw.println(addTabs() + "-- entity_constraint: " + entity_constraint);
		EEntity_definition domain = null; 
		EEntity attribute = null;
		if (entity_constraint.testDomain(null)) {
			domain = entity_constraint.getDomain(null);
		} else {
			System.out.println("entity_constraint domain unset: " + entity_constraint);
		}
		if (entity_constraint.testAttribute(null)) {
			attribute = entity_constraint.getAttribute(null);
		} else {
			System.out.println("entity_constraint attribute unset: " + entity_constraint);
		}
		pw.println(addTabs() + "-- domain: " + domain);
		pw.println(addTabs() + "-- attribute: " + attribute);
		if (attribute instanceof EAttribute) {
			// can be not explicit? Is there a need to branch into subtypes?
			if (attribute instanceof EExplicit_attribute) {
			}
		} else
		if (attribute instanceof EInverse_attribute_constraint) {
		} else
		if (attribute instanceof EAggregate_member_constraint) {
		} else {
		}
	}

	void processExplicit_attribute(EExplicit_attribute explicit_attribute)  throws SdaiException {
		pw.println(addTabs() + "-- explicit_attribute constraint: " + explicit_attribute);
	}

	void processAnd_constraint_relationship(EAnd_constraint_relationship and_constraint_relationship)  throws SdaiException {
		pw.println(addTabs() + "-- and_constraint_relationship: " + and_constraint_relationship);
		// element1 -- constraint_select
		EEntity element1 = null;
		if (and_constraint_relationship.testElement1(null)) {
			element1 = and_constraint_relationship.getElement1(null);
		}
		// element2 -- constraint_select
		EEntity element2 = null;
		if (and_constraint_relationship.testElement2(null)) {
			element2 = and_constraint_relationship.getElement2(null);
		}
		pw.println(addTabs() + "-- element1: " + element1);
		pw.println(addTabs() + "-- element2: " + element2);
		processNext(element1);
		processNext(element2);
	}

	void processOr_constraint_relationship(EOr_constraint_relationship or_constraint_relationship) throws SdaiException {
		pw.println(addTabs() + "-- or_constraint_relationship: " + or_constraint_relationship);
		// element1 -- constraint_select
		EEntity element1 = null;
		if (or_constraint_relationship.testElement1(null)) {
			element1 = or_constraint_relationship.getElement1(null);
		}
		// element2 -- constraint_select
		EEntity element2 = null;
		if (or_constraint_relationship.testElement2(null)) {
			element2 = or_constraint_relationship.getElement2(null);
		}
		pw.println(addTabs() + "-- element1: " + element1);
		pw.println(addTabs() + "-- element2: " + element2);
		processNext(element1);
		processNext(element2);
	}

	void processPath_constraint(EPath_constraint path_constraint)  throws SdaiException {
		pw.println(addTabs() + "-- path_constraint: " + path_constraint);
 		// element_1 - path_constraint_select -- for making a step
		EEntity element1 = null;
		if (path_constraint.testElement1(null)) {
			element1 = path_constraint.getElement1(null);
		}
		// element2 -- constraint_select -- must meet its requiremens after making a step indicated by element1
		EEntity element2 = null;
		if (path_constraint.testElement2(null)) {
			element2 = path_constraint.getElement2(null);
		}
		pw.println(addTabs() + "-- element1: " + element1);
		pw.println(addTabs() + "-- element2: " + element2);
		// no harm in using the same method for element1
		processNext(element1);
		processNext(element2);
	}

	void processNegation_constraint(ENegation_constraint negation_constraint)  throws SdaiException {
		pw.println(addTabs() + "-- negation_constraint: " + negation_constraint);
		// constraints -- constraint_select
		EEntity constraints = null;
		if (negation_constraint.testConstraints(null)) {
			constraints = negation_constraint.getConstraints(null);
		}
		pw.println(addTabs() + "-- constraints: " + constraints);
		processNext(constraints);
	}


	void processAttribute_value_constraint(EAttribute_value_constraint attribute_value_constraint) throws SdaiException { 
		pw.println(addTabs() + "-- attribute_value_constraint: " + attribute_value_constraint);
		EEntity attribute = attribute_value_constraint.getAttribute(null);
		if (attribute instanceof EAttribute) {
			String attribute_name = ((EAttribute)attribute).getName(null);
			String parent_entity_name = ((EAttribute)attribute).getParent(null).getName(null).toLowerCase();


			if (attribute_value_constraint instanceof EString_constraint) {
				String constraint_value = ((EString_constraint)attribute_value_constraint).getConstraint_value(null);
				pw.println(addTabs() + "-- string constraint");
				pw.println("\tIF (NOT (e\\" + parent_entity_name + "." + attribute_name + " = \'" + constraint_value + "\')) RETURN (FALSE);");
			} else 
			if (attribute_value_constraint instanceof EBoolean_constraint) {
				System.out.println("boolean constraint - not implemented");
				pw.println(addTabs() + "-- boolean constraint - not implemented");
			} else					
			if (attribute_value_constraint instanceof EInteger_constraint) {
				System.out.println("integer constraint - not implemented");
				pw.println(addTabs() + "--integer constraint");
			} else					
			if (attribute_value_constraint instanceof EReal_constraint) {
				System.out.println("real constraint - not implemented");
				pw.println(addTabs() + "-- real constraint");
			} else					
			if (attribute_value_constraint instanceof EEnumeration_constraint) {
				System.out.println("enumeration constraint - not implemented");
				pw.println(addTabs() + "-- enumeration constraint");
			} else					
			if (attribute_value_constraint instanceof ELogical_constraint) {
				pw.println(addTabs() + "-- logical constraint");
				int constraint_value = ((ELogical_constraint)attribute_value_constraint).getConstraint_value(null);
				String constraint_value_str = null;
				switch (constraint_value) {
					case 1: 
						constraint_value_str = "FALSE";
						break;
					case 2:
						constraint_value_str = "TRUE";
						break;
					case 3:
						constraint_value_str = "UNKNOWN";
						break;
					case 0:
					default:
						constraint_value_str = "?";
						break;
				}


				pw.println("\tIF (NOT (e\\" + parent_entity_name + "." + attribute_name + " = " + constraint_value_str + ")) RETURN (FALSE);");
			} else					
			if (attribute_value_constraint instanceof ENon_optional_constraint) {
				System.out.println("non_optional constraint - not implemented");
				pw.println(addTabs() + "-- non_optional constraint");
			} else {
				System.out.println("ERROR - unknown constraint!!! : " + attribute_value_constraint);
				pw.println(addTabs() + "-- other constraint: " + attribute_value_constraint);
			} 					
		} else {
			System.out.println("not attribute - not implemented: " + attribute);
			pw.println(addTabs() + "-- not attribute: " + attribute);
		}
	}		

	void processInverse_attribute_constraint(EInverse_attribute_constraint inverse_attribute_constraint) throws SdaiException {
		pw.println(addTabs() + "-- inverse_attribute_constraint: " + inverse_attribute_constraint);
		// inverted_attribute : inverse_attribute_constraint_select;
		EEntity inverted_attribute = null;
		if (inverse_attribute_constraint.testInverted_attribute(null)) {
			inverted_attribute = inverse_attribute_constraint.getInverted_attribute(null);
		}
		pw.println(addTabs() + "-- inverted_attribute: " + inverted_attribute);
		processNext(inverted_attribute);
	}

	void processAggregate_member_constraint(EAggregate_member_constraint aggregate_member_constraint) throws SdaiException {
		pw.println(addTabs() + "-- aggregate_member_constraint: " + aggregate_member_constraint);
		// member : OPTIONAL INTEGER;
		int member;
		// attribute : aggregate_member_constraint_select;
		EEntity attribute = null;
		if (aggregate_member_constraint.testAttribute(null)) {
			attribute = aggregate_member_constraint.getAttribute(null);
		}
		pw.println(addTabs() + "-- attribute: " + attribute);
		processNext(attribute);
	}

	void processInstance_equal(EInstance_equal instance_equal) throws SdaiException {
		pw.println(addTabs() + "-- instance_equal: " + instance_equal);
		// may not be present in ap210
	}

	String addTabs() {
		String result = "";
		for (int i = 0; i < indentation; i++) {
			result+= "\t";
		}
		return result;
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

  PrintWriter getPrintWriter(String file_name) throws IOException {
    FileOutputStream fos = new FileOutputStream(file_name);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    return pw;
  }

	void printDebug(String message) {
		if (flag_print_debug) {
			pw.println(message);
		}
	}

	void printAttribute(String message) {
		if (flag_print_attributes) {
			pw.println(message);
		}
	}

	void printComments(String message) {
		if (flag_print_comments) {
			pw.println(message);
		}
	}
	
	void printError(String message) {
		if (flag_print_errors) {
			pw.println(message);
		}
	}
}

class RuleGeneratorArmAttribute {
	EAttribute arm_attribute;
	Vector mapping_alternatives;
		    
	// not sure if an attribute can be strong only in part of mapping alternatives, and what to do then?
	// so, if strong_count is > 0 but < of the number of attribute mapping alternatives, then what?
	// current implementation does not allow to check alternatives separately
	int strong_count;     // the number of alternatives (perhaps can be different in different alternatives?)
//	int mandatory_count;  // mandatory but not strong - number of alternatives
//	int optional_count;   // optional but not strong - number of alternatives

	int number_of_alternatives;
	boolean is_aggregate;
	boolean is_array;
	boolean is_unique;
	boolean is_attribute_mapping_value = false;
	boolean is_attribute_mapping = false;
	int min_cardinality = Integer.MIN_VALUE;
	int max_cardinality = Integer.MIN_VALUE;

	RuleGeneratorArmAttribute(EAttribute an_attribute, Vector attribute_mapping_alternatives) throws SdaiException {
		arm_attribute = an_attribute;
		mapping_alternatives = attribute_mapping_alternatives;
		number_of_alternatives = mapping_alternatives.size();
		Iterator alternative_iterator = mapping_alternatives.iterator();	
		strong_count = 0;
		while (alternative_iterator.hasNext()) {
//			EAttribute_mapping an_alternative = (EAttribute_mapping)alternative_iterator.next();
			EGeneric_attribute_mapping an_alternative = (EGeneric_attribute_mapping)alternative_iterator.next();
			if (an_alternative.getStrong(null)) {
				strong_count++;
			}
			if (an_alternative instanceof EAttribute_mapping) {
				is_attribute_mapping = true;
			} else
			if (an_alternative instanceof EAttribute_mapping_value) {
				is_attribute_mapping_value = true;
			} else {
				// error
			}
		}
		EEntity domain = null;
		if (arm_attribute instanceof EExplicit_attribute) {
			domain = ((EExplicit_attribute)arm_attribute).getDomain(null);
		} else
 		if (arm_attribute instanceof EDerived_attribute) {
			domain = ((EDerived_attribute)arm_attribute).getDomain(null);
		} else
		if (arm_attribute instanceof EInverse_attribute) {
			domain = ((EInverse_attribute)arm_attribute).getDomain(null);
		}
 		if (domain instanceof EAggregation_type) {
			is_aggregate = true;
			if (domain instanceof EVariable_size_aggregation_type) {
				EBound lower_bound = null;
				EBound upper_bound = null;
				if (((EVariable_size_aggregation_type)domain).testLower_bound(null)) {
					lower_bound = ((EVariable_size_aggregation_type)domain).getLower_bound(null);
					min_cardinality = lower_bound.getBound_value(null);
				}
				if (((EVariable_size_aggregation_type)domain).testUpper_bound(null)) {
					upper_bound = ((EVariable_size_aggregation_type)domain).getUpper_bound(null);
					max_cardinality = upper_bound.getBound_value(null);
				}
			
				if (domain instanceof ESet_type) {
					is_unique = true;
				}	else
				if (domain instanceof EBag_type) {
					is_unique = false;
				} else {
					// must be LIST, check if its elements are unique
					if (((EList_type)domain).getUnique_flag(null)) {
						is_unique = true;
					} else {
						is_unique = false;
					}
				}	
			} else 
			if (domain instanceof EArray_type) {
				EBound lower_index = null;
				EBound upper_index = null;
				if (((EArray_type)domain).testLower_index(null)) {
					lower_index = ((EArray_type)domain).getLower_index(null);
					min_cardinality = lower_index.getBound_value(null);
				}
				if (((EArray_type)domain).testUpper_index(null)) {
					upper_index = ((EArray_type)domain).getUpper_index(null);
					max_cardinality = upper_index.getBound_value(null);
				}
				is_array = true;
				if (((EArray_type)domain).getUnique_flag(null)) {
					is_unique = true;
				} else {
					is_unique = false;
				}
			} else {
				// internal error
			}
		} else {
			is_aggregate = false;
			is_array = false;
			is_unique = false;
		}
	}

}

	