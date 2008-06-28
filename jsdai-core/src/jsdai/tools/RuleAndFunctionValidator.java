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

command line switches:

-verbose
	prints more information about each executed rule

-schema schema_name
	schema with the rule(s)
	default: electronic_assembly_interconnect_and_packaging_design
  
-model application_model_schema_name
	schema name of the application model on which population the rule(s) is/are to be executed
	default: electronic_assembly_interconnect_and_packaging_design

-rule rule_name 
	to invoke all the domain rules of a global rule
	
-rule rule_name domain_rule_name	
	to invoke a single domain rule of a global rule, for example:
	-rule some_rule WR2

	NOTES: 
	1) for a single rule, -verbose is always on by default and cannot be switched off
	2) rule_name must be its express name for native rules, and express name or java name for generated rules:
	 -rule rule__package_3
	 or
	 -rule RRule_package_3
	 but only
	 -rule package_external_reference_constraint
 
  3) domain_rule_name must be its express name, for example:
		-rule package_external_reference_constraint WR4  
	4) for generated rules, domain_rule_name is ignored, because only one domain rule WR1 is always generated
	5) if domain rule does not have (optional) label, then it cannot be invoked and is skipped

-function function_name instance_number 
	to invoke a single function
	function_name is express name, not java name
	instance_number may include # or not, for example:
	
	-function f108241 #1949
	or
	-function f108241 1949
	
	NOTE: function execution is supported primarily to test generated functions, 
		therefore, only functions with one parameter of entity instance type currently can be executed

-generated
	runs generated rules only
-native
  runs native rules	only
-native -generated
	runs according to the last encountered switch,
	default - if no -native and no -generated, then both native and generated rules are executed

*/

package jsdai.tools;

//import jsdai.dictionary.*;
// import jsdai.SExtended_dictionary_schema.*;
// import jsdai.SMapping_schema.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
// import java.util.*;
import jsdai.lang.*;
import jsdai.dictionary.*;

// import jsdai.SX.*;
// import jsdai.SElectronic_assembly_interconnect_and_packaging_design.*;

public class RuleAndFunctionValidator {

	static boolean flag_generated = true;
	static boolean flag_native    = true;
	static boolean flag_verbose = false;


	static final int _TM_default = 0;
	static final int _TM_single_rule = 1;
	static final int _TM_single_function = 2;

	static String package_name = "SElectronic_assembly_interconnect_and_packaging_design";
	static String schema_name = "electronic_assembly_interconnect_and_packaging_design";
	// static String package_name = "SElectronic_assembly_interconnect_and_packaging_design";

	static String domain_rule_label = null;

	SdaiRepository repository;
	SdaiModel arm_model;
	SdaiModel aim_model;
	SdaiModel map_model;
	SdaiModel rules_model;
	static String rule_name = null;
	static String function_name = null;
	static String instance_id = null;
	static int flag_test_mode = _TM_default;
	SdaiModel work = null;
	SdaiContext _context = null;
	ASdaiModel domain = null;
	ASdaiModel application_domain = null;
	Vector rules_failed = new Vector();
	Vector rules_exceptions = new Vector();

  static long start_time_all, finish_time_all, start_time_one, finish_time_one, elapsed_time_all, elapsed_time_one;
	static String p21_file;

	public static final void main(String args[]) throws jsdai.lang.SdaiException {

		String ARM_name = "AP210_ARM";
		String AIM_name = "ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN";
		String AIM_schema_name;
		String MAP_name = "AP210_ARM";
		String p21_name = null;

		RuleAndFunctionValidator test = new RuleAndFunctionValidator();

		for (int ihi = 0; ihi < args.length; ihi++) {


			if (args[ihi].equalsIgnoreCase("-verbose")) { 
				flag_verbose = true;
			}	
			if (args[ihi].equalsIgnoreCase("-native")) { 
				flag_native = true;
				flag_generated = false;
			}	
			if (args[ihi].equalsIgnoreCase("-generated")) { 
				flag_native = false;
				flag_generated = true;
			}	

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
				AIM_name = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-p21")) {
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
				p21_name = args[ihi];
				p21_file = p21_name;
			} else
			if (args[ihi].equalsIgnoreCase("-rule")) {
				flag_test_mode = _TM_single_rule;
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A rule name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A rule name must follow the " + args[ihi-1] + " switch");
					return;
				}
				flag_verbose = true;
				rule_name = args[ihi];
				if (rule_name.substring(0,6).equalsIgnoreCase("rule__")) {
					rule_name = "R" + rule_name.substring(0, 1).toUpperCase() + rule_name.substring(1).toLowerCase();
				} else 
				if (rule_name.substring(0,6).equalsIgnoreCase("RRule_")) {
				 // nothing
				} else {
					rule_name = "R" + rule_name.substring(0, 1).toUpperCase() + rule_name.substring(1).toLowerCase();
				}
				if ((ihi+1) < args.length) {
					if (!(args[ihi+1].substring(0,1).equals("-"))) {
						ihi++;
						domain_rule_label = args[ihi];
					}	
				}
			} else
			if (args[ihi].equalsIgnoreCase("-function")) {
				flag_test_mode = _TM_single_function;
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A function name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A function name must follow the " + args[ihi-1] + " switch");
					return;
				}
				function_name = "F" + args[ihi].substring(0, 1).toUpperCase() + args[ihi].substring(1).toLowerCase();
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("An instance number must follow the the function name");
						return;
					}
				} else {
					System.out.println("An instance number must follow the the function name");
					return;
				}
				instance_id = args[ihi];
// System.out.println("function: " + function_name + ", instance: " + instance_id);	
			}
		} // for - through parameters
 
		if (p21_name == null) {
			System.out.println("physical file name must be provided");
			System.out.println("USAGE: java RuleAndFunctionValidator -p21 p21_file -rule rule_name | -function function_name instance_number");
			return;
		} else 
		if (rule_name == null && function_name == null) {
//			System.out.println("either -rule or -function switch must be used");
//			System.out.println("USAGE: java RuleAndFunctionValidator -p21 p21_file -rule rule_name | -function function_name instance_number");
						
		}
		
		
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadWriteAccess();
		
		test.repository = session.importClearTextEncoding(null, p21_name, null);
	
//----------------------------------------------------------------------------------------------

		SdaiModel dictionary_model = null;
//		SchemaInstance dictionary_data = session.getDataDictionary();
//		ASdaiModel dictionary_models = dictionary_data.getAssociatedModels();
		ASdaiModel dictionary_models = session.getDataDictionary().getRepository().getModels();
		SdaiIterator iterator = dictionary_models.createIterator();
		boolean model_found = false;
		while (iterator.next()) {
			dictionary_model = dictionary_models.getCurrentMember(iterator);
// System.out.println("dictionary - current model: " + dictionary_model.getName() + ", searching: " + schema_name.toUpperCase() + "_DICTIONARY_DATA");		
			if (dictionary_model.getName().equals(schema_name.toUpperCase() + "_DICTIONARY_DATA")) {
				model_found = true;
				test.rules_model = dictionary_model;
	  	  if (test.rules_model.getMode() == SdaiModel.NO_ACCESS) {
  	  		test.rules_model.startReadOnlyAccess();
				}
				break;
			}
		}
		if (!model_found) {
			System.out.println("ERROR: dictionary model for the specified schema not found");
			trans.endTransactionAccessAbort();
			test.repository.closeRepository();
			test.repository.deleteRepository();
			session.closeSession();
			return;
		}
	
//----------------------------------------------------------------------------------------------		

		test.application_domain = test.repository.getSchemas().getAssociatedModels();
System.out.println("domain: " + test.application_domain);

//------------------------------------------------------------------------------------------------------		

		ASdaiModel models = test.repository.getModels();

		SdaiIterator iter = models.createIterator();

		model_found = false;
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			String model_name = model.getUnderlyingSchema().getName(null);
// System.out.println("current model: " + model_name + ", searching: " + schema_name);		
		
			// don't need ARM model
//			if (model_name.equalsIgnoreCase(ARM_name)) {
//				test.arm_model = model;
//		    if (test.arm_model.getMode() == SdaiModel.NO_ACCESS) {
//   			test.arm_model.startReadOnlyAccess();
//  		}
//		} else 
			
			if (model_name.equalsIgnoreCase(AIM_name)) {
				model_found = true;
				test.aim_model = model;
	  	  if (test.aim_model.getMode() == SdaiModel.NO_ACCESS) {
  	  		test.aim_model.startReadOnlyAccess();
    		}
				break;
			}
			// this is stupid
//			if (model_name.equalsIgnoreCase(schema_name)) {
//				SdaiModel a_model = model;
//	  	  ESchema_definition sd = a_model.getUnderlyingSchema();
//	  	  test.rules_model = sd.findEntityInstanceSdaiModel();
//	  	  if (test.rules_model.getMode() == SdaiModel.NO_ACCESS) {
// 	  		test.rules_model.startReadOnlyAccess();
//   		}
//			}
		}
		if (!model_found) {
			System.out.println("ERROR: specified application model not found: " + AIM_name);
			trans.endTransactionAccessAbort();
			test.repository.closeRepository();
			test.repository.deleteRepository();
			session.closeSession();
			return;
		}
		
		
//--------------------------------------------------------------------------------------		
		
		switch (flag_test_mode) {
			case _TM_single_rule :
				if (rule_name.substring(0,6).equalsIgnoreCase("rule__")) {
					// generated rule
					// because all generated domain rules are only WR1, don't care if domain rule is on the command line or not
					test.runSingleRule("WR1");
				} else {
					// native rule
					if (domain_rule_label != null) {
						test.runSingleRule(domain_rule_label);
					} else {
						AGlobal_rule rules = (AGlobal_rule)test.rules_model.getEntityExtentInstances(CGlobal_rule.class);
						SdaiIterator iterr = rules.createIterator();
						boolean rule_found = false;
						while (iterr.next()) {
							EGlobal_rule rule = rules.getCurrentMember(iterr);
							String a_rule_name = rule.getName(null);
					    a_rule_name = "R" + a_rule_name.substring(0, 1).toUpperCase() + a_rule_name.substring(1).toLowerCase();
							if (a_rule_name.equalsIgnoreCase(rule_name)) {
								rule_found = true;

								// have to run all domain rules
								String wr_label;
								AWhere_rule wrules = rule.getWhere_rules(null, null);
								SdaiIterator iwr = wrules.createIterator();
								while (iwr.next()) {
									EWhere_rule wrule = wrules.getCurrentMember(iwr);
									if (wrule.testLabel(null)) {
										wr_label = wrule.getLabel(null);
										test.runSingleRule(wr_label);
									}
								}
								break;
							}
						}
						if (!rule_found) {
							System.out.println("ERROR: rule not found: " + rule_name.substring(1).toLowerCase());
							trans.endTransactionAccessAbort();
							test.repository.closeRepository();
							test.repository.deleteRepository();
							session.closeSession();
							return;
						} 
					}
				}
				break;
			case _TM_single_function:
				test.runSingleFunction();
				break;
			case _TM_default:
			default:
				test.runAllRules();
				break;
		}			

		// test.repository.exportClearTextEncoding("after_validation.stp");

		trans.endTransactionAccessAbort();
		test.repository.closeRepository();
		test.repository.deleteRepository();
		session.closeSession();
	}
	
	void runAllRules() throws SdaiException {


		AGlobal_rule rules = (AGlobal_rule)rules_model.getEntityExtentInstances(CGlobal_rule.class);
		SdaiIterator iter = rules.createIterator();
		int count = 0;
		int exception_count = 0;
		int violation_count = 0;
		int passed_count = 0;
		int internal_error_count = 0;
		int result;

		System.out.println("\nStarting all rules, p21 file: " + p21_file + " ==========================\n"); 
  	start_time_all = System.currentTimeMillis();

		while (iter.next()) {
			EGlobal_rule rule = rules.getCurrentMember(iter);
			rule_name = rule.getName(null);
			String wr_label;

			// System.out.println(rule_name.substring(0,6));
			if (rule_name.substring(0,6).equalsIgnoreCase("rule__")) {
				// generated rule
				if (!flag_generated) continue;
				wr_label = "WR1";

				rule_name = "R" + rule_name.substring(0, 1).toUpperCase() + rule_name.substring(1).toLowerCase();
				result = runSingleRule(wr_label);
				if      (result  < 0) exception_count++;
				else if (result == 0) violation_count++;
				else if (result  > 0) passed_count++; 
				else { 
					System.out.println("Of course it will happen");
					internal_error_count++;
				}
				count++;

			} else {
				// native rule
				if (!flag_native) continue;
				rule_name = "R" + rule_name.substring(0, 1).toUpperCase() + rule_name.substring(1).toLowerCase();
				AWhere_rule wrules = rule.getWhere_rules(null, null);
				SdaiIterator iwr = wrules.createIterator();
				while (iwr.next()) {
					EWhere_rule wrule = wrules.getCurrentMember(iwr);
					if (wrule.testLabel(null)) {
						wr_label = wrule.getLabel(null);

						result = runSingleRule(wr_label);
						if      (result  < 0) exception_count++;
						else if (result == 0) violation_count++;
						else if (result  > 0) passed_count++; 
						else { 
							System.out.println("Of course it will happen");
							internal_error_count++;
						}
						count++;

					}
					
				}
			}
			
		}

	  finish_time_all = System.currentTimeMillis();
  	elapsed_time_all = finish_time_all - start_time_all;
		System.out.println("\nDuration of validation of all the rules: " + elapsed_time_all + " ================="); 
		
		System.out.println("\nFinished ===================================="); 
		System.out.println("Number of evaluated rules: " + count);
		System.out.println("Number of passed rules: " + passed_count);
		System.out.println("Number of failed rules: " + violation_count);
		System.out.println("Number of exceptions: " + exception_count);
		System.out.println("Number of internal errors: " + internal_error_count);
		System.out.println("");
		System.out.println("Failed rules:\n");
		for (int i= 0; i < rules_failed.size(); i++) {
			String name_of_rule = (String)rules_failed.elementAt(i);
			// if (name_of_rule.substring(0,7).equalsIgnoreCase("RRule__")) {
			name_of_rule = name_of_rule.substring(1).toLowerCase();
			// }
			System.out.println(name_of_rule);
		}
		System.out.println("");
		System.out.println("Rules that caused exceptions:\n");
		for (int i= 0; i < rules_exceptions.size(); i++) {
			String name_of_rule = (String)rules_exceptions.elementAt(i);
			// if (name_of_rule.substring(0,7).equalsIgnoreCase("RRule__")) {
			name_of_rule = name_of_rule.substring(1).toLowerCase();
			// }
			System.out.println(name_of_rule);
		}

/*
		ESchema_definition aim_schema = aim_model.getUnderlyingSchema();
		ASdaiModel domain = new ASdaiModel();
		domain.addByIndex(1, aim_model);
		SdaiModel work = repository.createSdaiModel("working", aim_schema);
		SdaiContext _context = new SdaiContext(aim_schema, domain, work);
System.out.println("rules model: " + rules_model);
		AGlobal_rule rules = (AGlobal_rule)rules_model.getEntityExtentInstances(CGlobal_rule.class);
		SdaiIterator iter = rules.createIterator();
		while (iter.next()) {
			EGlobal_rule rule = rules.getCurrentMember(iter);
			rule_name = rule.getName(null);
			if (rule_name.substring(0,6).equalsIgnoreCase("rule__")) {

	    	Class[] paramTypes = new Class[1];
  	  	paramTypes[0] = SdaiContext.class;
				Method wr1_method = null;
				try {
	    		wr1_method = rule.getClass().getMethod("rWr1", paramTypes);
				} catch (Exception e) {
System.out.println("no rWr1 in rule: " + rule.getName(null));
					e.printStackTrace();
				}
    		Object[] params = new Object[1];
    		params[0] = _context;
    		int result = Integer.MIN_VALUE;
				try {
q					result = ((Integer)wr1_method.invoke(rule, params)).intValue();
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("result: " + result);
			}

		}
*/
	
	}

	int runSingleRule(String wr_label) throws SdaiException {
		
		printVerbose("\nTesting rule: " + rule_name.substring(1).toLowerCase() + ", " + wr_label);
		// System.out.println("aim model: " + aim_model);
		ESchema_definition aim_schema = aim_model.getUnderlyingSchema();
// domain = null;
		if (domain == null) {
//			domain = new ASdaiModel();
//			domain.addByIndex(1, aim_model);
			domain = application_domain;
		}
// work = null;
		if (work == null) {
			work = repository.createSdaiModel("working", aim_schema);
		}
// _context = null;
		if (_context == null) {
			_context = new SdaiContext(aim_schema, domain, work);
		}
		
		Class rule_class = null;
		try {
//			rule_class = Class.forName("jsdai.SX." + rule_name);
			rule_class = Class.forName("jsdai." + package_name + "." + rule_name);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rule class not found: jsdai." + package_name + "." + rule_name);
			return -1;
		}
    Object rule_inst = null;
		try {
	    rule_inst = rule_class.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rule instance not created: jsdai." + package_name + "." + rule_name);
			return -1;
		}
    Class[] paramTypes = new Class[1];
	  paramTypes[0] = SdaiContext.class;
		Method wr_method = null;
		String rwr_label = "r" + wr_label.substring(0,1).toUpperCase() + wr_label.substring(1).toLowerCase();
		try {
	    wr_method = rule_class.getMethod(rwr_label, paramTypes);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("method " + wr_label + " in rule not found: jsdai." + package_name + "." + rule_name);
			return -1;
		}
    Object[] params = new Object[1];
    params[0] = _context;
    int result = Integer.MIN_VALUE;
	  start_time_one = System.currentTimeMillis();
		try {
			result = ((Integer)wr_method.invoke(rule_inst, params)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			printVerbose("method " + wr_label + " in rule not invoked or exeption occured inside: jsdai." + package_name + "." + rule_name);
		  finish_time_one = System.currentTimeMillis();
  		elapsed_time_one = finish_time_one - start_time_one;
			printVerbose("Duration of validation of rule: " + rule_name.substring(1).toLowerCase() + ", " + wr_label + " until the exception: " + elapsed_time_one); 
			rules_exceptions.addElement(rule_name + ", " + wr_label);
			return -1;
		}

//		RRule__assembly_component_2d_shape rule = new RRule__assembly_component_2d_shape();
//		int result = rule.rWr1(_context);

//		System.out.println("result: " + result);
	  finish_time_one = System.currentTimeMillis();
  	elapsed_time_one = finish_time_one - start_time_one;
		printVerbose("Duration of validation of rule: " + rule_name.substring(1).toLowerCase() + ", " + wr_label  + " is " + elapsed_time_one); 
		if (result == 2) {
			printVerbose("Rule " + rule_name.substring(1).toLowerCase() + ", " + wr_label + " passed");
			return 1;
		} else {
			rules_failed.addElement(rule_name + ", " + wr_label);
		System.out.println("RULE FAILED: " + rule_name.substring(1).toLowerCase() + ", " + wr_label + "\n");
			printVerbose("Rule " + rule_name.substring(1).toLowerCase() + ", " + wr_label + " failed");
			return 0;
		}
	}

	void runSingleFunction() throws SdaiException {

		System.out.println("Testing function: " + function_name + " with argument " + instance_id + "\n");
//		System.out.println("aim model: " + aim_model);
		ESchema_definition aim_schema = aim_model.getUnderlyingSchema();
		ASdaiModel domain = new ASdaiModel();
		domain.addByIndex(1, aim_model);
		SdaiModel work = repository.createSdaiModel("working", aim_schema);
		SdaiContext _context = new SdaiContext(aim_schema, domain, work);
		if (!(instance_id.substring(0,1).equals("#"))) {
			instance_id = "#" + instance_id;
		}
		Value _instance = Value.alloc(ExpressTypes.GENERIC_TYPE).set(_context, repository.getSessionIdentifier(instance_id)); 
		Class function_class = null;
		try {
//			function_class = Class.forName("jsdai.SX." + function_name);
			function_class = Class.forName("jsdai." + package_name + "." + function_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
    Class[] paramTypes = new Class[2];
    paramTypes[0] = SdaiContext.class;
    paramTypes[1] = Value.class;
		Method run_method = null;
		try {
	    run_method = function_class.getMethod("run", paramTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}
    Object[] params = new Object[2];
    params[0] = _context;
    params[1] = _instance;
    Value result = null;
		try {
			result = (Value)run_method.invoke(null, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("result: " + result);
		
	}
	

	void printVerbose(String message) {
		if (flag_verbose) {
			System.out.println(message);
		}
	}



}
