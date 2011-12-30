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

package jsdai.util;

import java.io.*;
import java.util.Properties;
//import java.util.concurrent.*;
import java.util.*;

import jsdai.lang.*;
import jsdai.dictionary.*;

public class Validate {

	static PrintStream pout, perr;
	static BufferedOutputStream bout, berr;
	static FileOutputStream fout, ferr;

  static OutputStreamWriter osw;


/**
Validation of STEP files
<P>
<code>Validate</code> is an application program used to validate exchange structures, that is, files 
prepared according to 'ISO 10303-21: Implementation methods: Clear text encoding of the exchange structure'.
After importing the submitted exchange structure to a temporary repository, validation is performed in two stages:
<br>- validation of all entity instances contained in all models of the repository;
<br>- validation of all global and uniqueness rules for all schema instances of the repository.
<br>In the first stage for each entity instance the following operations are performed:
<br>- validate required explicit attributes assigned;
<br>- validate inverse attributes;
<br>- validate explicit attributes references;
<br>- validate aggregates size;
<br>- validate aggregates uniqueness;
<br>- validate array not optional;
<br>- validate where rule.
<br>In the second stage the following two operations on schema instances are performed:
<br>- validate global rule;
<br>- validate uniqueness rule.
<P>
<code>Validate</code> belongs to <code>jsdai.util</code> package and for a given exchange structure with the name, say, 
'abc.stp' could be executed by applying a command like this:
<P><TT><pre>    java jsdai.util.Validate abc.stp -stdout Valid.log</pre></TT>
Here 'Valid.log' is (an arbitrary) name of a log file produced by the program. 
<br><strong>NOTE:</strong> similarly as other JSDAI-based applications, <code>Validate</code> also requires configuration file 
<code>jsdai.properties</code> to be in its disposition.
<br>The log file includes the following information:
<br>- the name of the exchange structure and the name of the repository to which this structure is imported;
<br>- for each model the total number of instances and a list of instances not conforming to the validation; 
  for each such instance also a more specific information (like violated rules or atributes with 
  mandatory values not set) is displayed; in the case when a where rule is violated its parent item 
  (either partial entity data type or defined type) is indicated;
<br>- for each model the number of instances not conforming to the validation;
<br>- for each schema instance the total number of global rules and a list of global rules which are not 
  satisfied by the population associated with this schema instance; for each such global rule 
  violated where rules are given;
<br>- for each schema instance the number of global rules for which validation has failed (<code>false</code> was 
  returned);
<br>- for each schema instance a list of violated uniqueness rules (if any); for each such rule entity instances 
  not conforming to the validation are displayed;
<br>- for each schema instance the number of violated uniqueness rules.
<P>
You should see in the log file something similar to:
<P>
<br>'Validate' program. Copyright 1999-2005, LKSoftWare GmbH
<br>--- Exchange structure: allied_203.stp
<br>--- Imported to the repository: allied_203_org
<br>--- Reading time=4sec
<br>count of instances in model "default": 785
<br>#28=MECHANICAL_CONTEXT('CONFIGURATION CONTROL DESIGN',#26,'MECHANICAL');
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" in entity "mechanical_context" is violated
<br>#36=PRODUCT_DEFINITION_SHAPE('DESIGN SHAPE','SHAPE FOR part',#35);
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
      where rule "wr1" in entity "product_definition_shape" is violated
<br>count of erroneous instances: 2
<br>count of global rules in schema instance "schema1": 79
<br>For the global rule "restrict_date_time_role" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>For the global rule "restrict_person_organization_role" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>For the global rule "restrict_security_classification_level" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>For the global rule "security_classification_requires_date_time" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>For the global rule "restrict_approval_status" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>For the global rule "product_version_requires_person_organization" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr2" is violated
<br>For the global rule "product_requires_product_category" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>For the global rule "restrict_product_category_value" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>For the global rule "design_context_for_property" validation gives FALSE
<br>&nbsp &nbsp &nbsp &nbsp &nbsp
 	   where rule "wr1" is violated
<br>count of violated global rules: 9
<br>count of violated uniqueness rules: 0
<br>--- Validation time=32sec
 */



	private static final String SUFIX_XIM = "_XIM";

// Non-persistent lists being submitted as parameters to the methods:
// validateRequiredExplicitAttributesAssigned, validateInverseAttributes, 
// validateExplicitAttributesReferences, validateAggregatesSize, 
// validateAggregatesUniqueness, validateArrayNotOptional, 
// and validateStringWidth, respectively
	AAttribute aat1 = new AAttribute();
	AAttribute aat2 = new AAttribute();
	AAttribute aat3 = new AAttribute();
	AAttribute aat4 = new AAttribute();
	AAttribute aat5 = new AAttribute();
	AAttribute aat6 = new AAttribute();
	AAttribute aat7 = new AAttribute();
// Non-persistent list being submitted as a parameter to the validateWhereRule and 
// validateGlobalRule methods
	AWhere_rule w_rules_viol = new AWhere_rule();
// Output stream
	static PrintWriter pw;
// Iterator used for aggregates of various types
	SdaiIterator iterator;
	SdaiIterator it_rules;
// Iterator used for aggregates of entity instances
	SdaiIterator inst_iterator;
// Variable used to designate either underlying schema for a model or 
// native schema for a schema instance
	ESchema_definition old_schema = null;
// Working model used within a SdaiContext
	SdaiModel work_model = null;

	SdaiModel cur_model;
	EEntity_definition cur_type = null;
	String cur_name;
	int cur_inst_count;
	int val_inst_count;
	long inst_count;
	int gl_rules_count;
	int un_rules_count;
	long all_steps_count;
	long inst_counter = 0;
	boolean only_xim;
//	List queue;


	private void identifyPartialEntities(EEntity_definition entity, AEntity_definition result)
			throws SdaiException {
// all supertypes are processed recursively
		if (!(result.isMember(entity))) {
			AEntity supertypes = entity.getSupertypes(null);
			SdaiIterator iter = supertypes.createIterator();
			while (iter.next()) {
				EEntity_definition sprtp = (EEntity_definition)supertypes.getCurrentMemberEntity(iter);
				identifyPartialEntities(sprtp, result);
			}
			if (!entity.getComplex(null)) {
				int ind = ((AEntity)result).getMemberCount() + 1;
				result.addByIndex(ind, entity);
			}
		}
	}


	private int validateWhereRulesXim(EEntity instance, EEntity_definition type, AWhere_rule w_rules_viol, 
				ASdaiModel models_dom) throws SdaiException {
		AEntity_definition result = new AEntity_definition();
//System.out.println("Validate *** type: " + type.getName(null));
		identifyPartialEntities(type, result);
		int res = ELogical.TRUE;
		SdaiIterator iter = result.createIterator();
		while (iter.next()) {
			EEntity_definition part_type = (EEntity_definition)result.getCurrentMemberEntity(iter);
//System.out.println("       part_type: " + part_type.getName(null));
			SdaiModel own_mod = ((CEntity)part_type).findEntityInstanceSdaiModel();
			String mod_name = own_mod.getName();
//System.out.println("            belongs to model: " + mod_name);
      int xim_ind = mod_name.lastIndexOf(SUFIX_XIM);
			if (xim_ind < 0) {
				continue;
			}
//System.out.println("            it is xim model: " + mod_name);
			AWhere_rule w_rules = part_type.getWhere_rules(null, null);
			if (w_rules.getMemberCount() <= 0) {
				continue;
			}
			if (it_rules == null) {
				it_rules = w_rules.createIterator();
			} else {
				w_rules.attachIterator(it_rules);
			}
			while(it_rules.next()) {
				EWhere_rule w_rule = w_rules.getCurrentMember(it_rules);
				int check_res = instance.validateWhereRule(w_rule, models_dom);
				if (check_res == ELogical.FALSE) {
					res = check_res;
					int ind = w_rules_viol.getMemberCount() + 1;
					w_rules_viol.addByIndex(ind, w_rule);
				} else if (check_res == ELogical.UNKNOWN) {
					if (res == ELogical.TRUE) {
						res = check_res;
					}
				}
			}
		}
		return res;
	}

	private void listAttributes(AAttribute aat, String message) throws SdaiException, java.lang.InterruptedException {
// Preparing iterator for running through the attributes list
		if (iterator == null) {
			iterator = aat.createIterator();
		} else {
			aat.attachIterator(iterator);
		}
		while (iterator.next()){
// Directing the name of the attribute to the output stream
			EAttribute attr = aat.getCurrentMember(iterator);
			pw.println(" \t   Violation for attribute \"" + attr.getName(null) + "\": " + message);
			// queue.add(" \t   Violation for attribute \"" + attr.getName(null) + "\": " + message);
		}
	}

	private void listWhereRules(AWhere_rule wr) throws SdaiException, java.lang.InterruptedException  {
// Preparing iterator for running through the list of where rules
		if (iterator == null) {
			iterator = wr.createIterator();
		} else {
			wr.attachIterator(iterator);
		}
		String str;
		Throwable e;
		String mess;
		int sizeof_case;
		AEntity viol_inst_aggr = null;
		while (iterator.next()){
// Taking a where rule
			CWhere_rule rule = (CWhere_rule)wr.getCurrentMember(iterator);
// Getting the parent item of the rule
			EEntity parent = rule.getParent_item(null);
			e = rule.getError();
			mess = null;
			if (e != null) {
				mess = e.getMessage();
			}
// Preparing a string for output in the case when parent is of an entity data type
			sizeof_case = 0;
			if (parent instanceof CEntity_definition) {
				viol_inst_aggr = rule.getInstancesSizeof(true);
				if (viol_inst_aggr != null) {
					sizeof_case = 1;
				} else {
					viol_inst_aggr = rule.getInstancesSizeof(false);
					if (viol_inst_aggr != null) {
						sizeof_case = 2;
					}
				}
				if (rule.testLabel(null)) {
					if (e == null) {
						str = " \t   Violation of where rule \"" + rule.getLabel(null) + 
							"\" in entity \"" + ((CEntity_definition)parent).getName(null) + "\" ";
					} else {
						str = " \t   An outcome from where rule \"" + rule.getLabel(null) + 
							"\" in entity \"" + ((CEntity_definition)parent).getName(null) + "\" is an error: " + mess;
					}
				} else {
					if (e == null) {
						str = " \t   Violation of where rule without name in entity \"" + 
							((CEntity_definition)parent).getName(null) + "\" ";
					} else {
						str = " \t   An outcome from where rule without name in entity \"" + 
							((CEntity_definition)parent).getName(null) + "\" is an error: " + mess;
					}
				}
// Preparing a string for output in the case when parent is of a defined data type
			} else if (parent instanceof CDefined_type) {
				if (rule.testLabel(null)) {
					if (e == null) {
						str = " \t   Violation of where rule \"" + rule.getLabel(null) + 
							"\" in defined type \"" + ((CDefined_type)parent).getName(null) + "\" ";
					} else {
						str = " \t   An outcome from where rule \"" + rule.getLabel(null) + 
							"\" in defined type \"" + ((CDefined_type)parent).getName(null) + "\" is an error: " + mess;
					}
				} else {
					if (e == null) {
						str = " \t   Violation of where rule without name in defined type \"" + 
							((CDefined_type)parent).getName(null) + "\" ";
					} else {
						str = " \t   An outcome from where rule without name in defined type \"" + 
							((CDefined_type)parent).getName(null) + "\" is an error: " + mess;
					}
				}
// Preparing a string for output in the case when parent is a global rule
			} else {
				viol_inst_aggr = rule.getInstancesSizeof(true);
				if (viol_inst_aggr != null) {
					sizeof_case = 1;
				} else {
					viol_inst_aggr = rule.getInstancesSizeof(false);
					if (viol_inst_aggr != null) {
						sizeof_case = 2;
					}
				}
				if (rule.testLabel(null)) {
					if (e == null) {
						str = " \t   where rule \"" + rule.getLabel(null) + "\" is violated";
					} else {
						str = " \t   An outcome from where rule \"" + rule.getLabel(null) + "\" is an error: " + mess;
					}
				} else {
					if (e == null) {
						str = " \t   where rule without name is violated";
					} else {
						str = " \t   An outcome from where rule without name is an error: " + mess;
					}
				}
			}
// Directing the constructed string to the output stream
			pw.println(str);
			// queue.add(str);
			if (sizeof_case == 1) {
				str = " \t      Instances set making SIZEOF value nonzero:";
				pw.println(str);
				// queue.add(str);
			} else if (sizeof_case == 2) {
				str = " \t      Instances set violating SIZEOF equation:";
				pw.println(str);
				// queue.add(str);
			}
			if (sizeof_case > 0) {
				if (inst_iterator == null) {
					inst_iterator = viol_inst_aggr.createIterator();
				} else {
					viol_inst_aggr.attachIterator(inst_iterator);
				}
				while (inst_iterator.next()){
					EEntity inst = viol_inst_aggr.getCurrentMemberEntity(inst_iterator);
					pw.println(" \t      " + inst);
					// queue.add(" \t      " + inst);
				}
			}
		}
	}

	private void listEntityInstances(AEntity insts, EEntity_definition edef, EUniqueness_rule rule) throws SdaiException, java.lang.InterruptedException  {
// Directing the name of the violated uniqueness rule and its parent to the output stream
		if (rule.testLabel(null)) {
			pw.println("For the uniqueness rule \"" + rule.getLabel(null) + "\" defined in entity data type \"" + 
				edef.getName(null) + "\" validation gives FALSE");
			// queue.add("For the uniqueness rule \"" + rule.getLabel(null) + "\" defined in entity data type \"" + edef.getName(null) + "\" validation gives FALSE");
		} else {
			pw.println("For the uniqueness rule without name defined in entity data type \"" + 
				edef.getName(null) + "\" validation gives FALSE");
			// queue.add("For the uniqueness rule without name defined in entity data type \"" + edef.getName(null) + "\" validation gives FALSE");
		}
// Preparing iterator for running through the list of entity instances
		if (iterator == null) {
			iterator = insts.createIterator();
		} else {
			insts.attachIterator(iterator);
		}
// Directing the entity instances to the output stream
		pw.println(" \t   instances not conforming to the validation:");
		// queue.add(" \t   instances not conforming to the validation:");
		while (iterator.next()){
			EEntity inst = insts.getCurrentMemberEntity(iterator);
			pw.println(" \t   " + inst);
			// queue.add(" \t   " + inst);
		}
	}

	boolean validateInstance(EEntity instance, ASdaiModel models_dom, UtilMonitor monitor) throws SdaiException, java.lang.InterruptedException  {
// Aggregates for storing attributes not conforming to the validation are emptied
		aat1.clear();
		aat2.clear();
		aat3.clear();
		aat4.clear();
		aat5.clear();
		aat6.clear();
		aat7.clear();
// Aggregate for storing violated where rules is emptied
		w_rules_viol.clear();
// Is 'true' if at least one violation for the current instance was reported
		boolean inst_printed = false;
		EEntity_definition type = instance.getInstanceType();
		if (type != cur_type) {
			cur_type = type;
			cur_name = type.getName(null);
			cur_inst_count = cur_model.getExactInstanceCount(type);
			val_inst_count = 0;
		}
		val_inst_count++;
		inst_counter++;
		if (monitor != null) {
			monitor.worked(1);
			monitor.subTask("Entity instance " + inst_counter + " out of " + inst_count + 
				", type " + cur_name, all_steps_count, inst_counter);
		}
// Invocation of the validation methods and 
// output of attributes not conforming to the validation
		boolean res2 = instance.validateRequiredExplicitAttributesAssigned(aat1);
		if (!res2) {
			pw.println(instance.toString());
			// queue.add(instance.toString());
			inst_printed = true;
			listAttributes(aat1, "value not set");
		}
		boolean res3 = instance.validateInverseAttributes(aat2);
		if (!res3) {
			if (!inst_printed) {
				pw.println(instance.toString());
				// queue.add(instance.toString());
				inst_printed = true;
			}
			listAttributes(aat2, "inverse constraint is not met");
		}
		int res4 = instance.validateExplicitAttributesReferences(aat3);
		if (res4 == ELogical.FALSE) {
			if (!inst_printed) {
				pw.println(instance.toString());
				// queue.add(instance.toString());
				inst_printed = true;
			}
			listAttributes(aat3, "reference to instance of an incorrect entity type");
		}
		int res5 = instance.validateAggregatesSize(aat4);
		if (res5 == ELogical.FALSE) {
			if (!inst_printed) {
				pw.println(instance.toString());
				// queue.add(instance.toString());
				inst_printed = true;
			}
			listAttributes(aat4, "aggregate size constraint is not met");
		}
		boolean res6 = instance.validateAggregatesUniqueness(aat5);
		if (!res6) {
			if (!inst_printed) {
				pw.println(instance.toString());
				// queue.add(instance.toString());
				inst_printed = true;
			}
			listAttributes(aat5, "aggregate uniqueness is not satisfied");
		}
		boolean res7 = instance.validateArrayNotOptional(aat6);
		if (!res7) {
			if (!inst_printed) {
				pw.println(instance.toString());
				// queue.add(instance.toString());
				inst_printed = true;
			}
			listAttributes(aat6, "at least one element of array is missing");
		}
		int res8 = ELogical.TRUE; // instance.validateStringWidth(aat7);
		int res9 = ELogical.TRUE; // reserved for instance.validateBinaryWidth()
		int res1;
		if (only_xim) {
			res1 = validateWhereRulesXim(instance, type, w_rules_viol, models_dom);
		} else {
			res1 = instance.validateWhereRule(w_rules_viol, models_dom);
		}

// Selection of the value (true/false) to be returned
		if (res1 == ELogical.FALSE || !res2 || !res3 || res4 == ELogical.FALSE || 
				res5 == ELogical.FALSE || !res6 || !res7 || res8 == ELogical.FALSE) {
			if (res8 == ELogical.FALSE) {
				listAttributes(aat7, "string width constraint is not met");
			}
// Output of violated where rules
			if (!inst_printed) {
				pw.println(instance.toString());
				// queue.add(instance.toString());
			}
			if (res1 == ELogical.FALSE) {
				listWhereRules(w_rules_viol);
			}
// Returns 'false' to indicate validation failure for this instance
			return false;
		} else {
// Returns 'true' if all validation methods have passed with no violation detected
			return true;
		}
	}

// Returns count of instances with errors
	int validateInstances(Aggregate instances, ASdaiModel models_dom, UtilMonitor monitor) throws SdaiException, java.lang.InterruptedException  {
		int errors = 0;
// Running through all instances in the model
		SdaiIterator iterator = instances.createIterator();
		while (iterator.next()){
			EEntity instance = (EEntity)instances.getCurrentMemberObject(iterator);
// Validation of an instance
			if (!validateInstance(instance, models_dom, monitor)) {
				errors++;
			}
		}
		return errors;
	}

	private long validationInstances(SdaiRepository repo, UtilMonitor monitor) throws SdaiException, java.lang.InterruptedException  {
		long tot_errors = 0;
		SdaiSession ss = repo.getSession();
		ASdaiModel models = repo.getModels();
		SdaiIterator modIter = models.createIterator();
// Running through all models in the repository
		while (modIter.next()) {
			SdaiModel model = models.getCurrentMember(modIter);
			if (model == work_model) {
				continue;
			}
			cur_model = model;
			cur_type = null;
// Getting instances contained in the model
			Aggregate instances = model.getInstances();
			pw.println("count of instances in model \"" + model.getName() + "\": " + instances.getMemberCount());
// Preparing domain for validation of instances
			ASdaiModel models_dom = new ASdaiModel();
			models_dom.addByIndex(1, model, null);
// Preparing SdaiContext with an appropriate working model (only in the case where the 
// underlying schema for the current model is different from that for the previous one)
			ESchema_definition schema = model.getUnderlyingSchema();
			if (schema != old_schema) {
				if (old_schema != null) {
					work_model.deleteSdaiModel();
				}
				work_model = repo.createSdaiModel("working", schema);
				work_model.startReadWriteAccess();
				ss.setSdaiContext(new SdaiContext(schema, null, work_model));
				old_schema = schema;
			}
// Validation of the instances; errors counter is returned
			long errors = validateInstances(instances, models_dom, monitor);
			tot_errors += errors;
		}
		return tot_errors;
	}

	private long [] validationRules(SdaiRepository repo, UtilMonitor monitor) throws SdaiException, java.lang.InterruptedException  {
		long [] tot_rules = new long [2];
		SdaiSession ss = repo.getSession();
// Getting all schema instances of the specified repository
		ASchemaInstance schemas = repo.getSchemas();
		SdaiIterator schemaIter = schemas.createIterator();
		SdaiIterator entityIter = null;
		SdaiIterator it_decls = null;
		tot_rules[0] = 0;
		tot_rules[1] = 0;

		int gl_rules_index = 0;
		int un_rules_index = 0;
		long tot_counter = inst_count;

// Creating a non-persistent list for storing entity instances not conforming to the uniqueness validation
		AEntity nonConf_ent = new AEntity();
// Running through all schema instances in the repository
		while(schemaIter.next()) {
			SchemaInstance schema_inst = schemas.getCurrentMember(schemaIter);
// Preparing SdaiContext with an appropriate working model (only in the case when the 
// native schema for the current schema instance is different from that for the previous one)
			ESchema_definition schema = schema_inst.getNativeSchema();
			if (only_xim) {
				String sch_name = schema.getName(null);
//System.out.println("Validate            schema name: " + sch_name);
				int xim_ind = sch_name.lastIndexOf(SUFIX_XIM);
				if (xim_ind < 0) {
					continue;
				}
			}
			if (schema != old_schema) {
				if (old_schema != null) {
					work_model.deleteSdaiModel();
				}
				work_model = repo.createSdaiModel("working", schema);
				work_model.startReadWriteAccess();
				ss.setSdaiContext(new SdaiContext(schema, null, work_model));
				old_schema = schema;
			}
// Getting global rule declarations for the schema instance
			ARule_declaration decls = schema.getRule_declarations(null, null);
			pw.println("count of global rules in schema instance \"" + schema_inst.getName() + "\": " + decls.getMemberCount());
// Preparing iterator for running through the set of rule declarations
			if (it_decls == null) {
				it_decls = decls.createIterator();
			} else {
				decls.attachIterator(it_decls);
			}
			int false_count = 0;
// Running through the set of rule declarations
			while (it_decls.next()) {
				ERule_declaration decl = decls.getCurrentMember(it_decls);
				EGlobal_rule gl_rule = (EGlobal_rule)decl.getDefinition(null);
				gl_rules_index++;
				tot_counter++;
				if (monitor != null) {
					monitor.worked(1);
					monitor.subTask("Global rule: " + gl_rule.getName(null) + " (" + gl_rules_index + " out of " + 
					gl_rules_count + ")", all_steps_count, tot_counter);
				}
// Validation of the global rule
				w_rules_viol.clear();
				if (schema_inst.validateGlobalRule(gl_rule, w_rules_viol) != ELogical.FALSE) {
					continue;
				}
// Output of violated where rules for the validated global rule
				false_count++;
				pw.println("For the global rule \"" + gl_rule.getName(null) + "\" validation gives FALSE");
				// queue.add("For the global rule \"" + gl_rule.getName(null) + "\" validation gives FALSE");
				listWhereRules(w_rules_viol);
			}
			tot_rules[0] += false_count;

			false_count = 0;
// Getting entity definition declarations for the schema instance
			AEntity_declaration e_decls = schema.getEntity_declarations(null, null);
// Preparing iterator for running through the set of entity definition declarations
			if (entityIter == null) {
				entityIter = e_decls.createIterator();
			} else {
				e_decls.attachIterator(entityIter);
			}
// Running through the set of entity definition declarations
			while (entityIter.next()) {
				EEntity_declaration edecl = e_decls.getCurrentMember(entityIter);
// Extracting an entity definition
				EEntity_definition edef = (EEntity_definition)edecl.getDefinition(null);
// Getting uniqueness rules for the considered entity definition
				AUniqueness_rule u_rules = edef.getUniqueness_rules(null, null);
				if (u_rules.getMemberCount() == 0) {
					continue;
				}
// Preparing iterator for running through the set of uniqueness rules
				if (it_decls == null) {
					it_decls = u_rules.createIterator();
				} else {
					u_rules.attachIterator(it_decls);
				}
// Running through the set of uniqueness rules
				while(it_decls.next()) {
					EUniqueness_rule u_rule = u_rules.getCurrentMember(it_decls);
// Validation of the uniqueness rule
					un_rules_index++;
					tot_counter++;
					if (monitor != null) {
						monitor.worked(1);
						if (u_rule.testLabel(null)) {
							monitor.subTask("Uniqueness rule: " + u_rule.getLabel(null) + " (" + un_rules_index + " out of " + 
							un_rules_count + ")", all_steps_count, tot_counter);
						} else {
							monitor.subTask("Uniqueness rule: " + "no name" + " (" + un_rules_index + " out of " + 
							un_rules_count + ")", all_steps_count, tot_counter);
						}
					}
					nonConf_ent.clear();
					if (schema_inst.validateUniquenessRule(u_rule, nonConf_ent) != ELogical.FALSE) {
						continue;
					}
// Output of entity instances not conforming to the validation
					false_count++;
					listEntityInstances(nonConf_ent, edef, u_rule);
				}
			}
			tot_rules[1] += false_count;
		}
		return tot_rules;
	}


	long getStepsCount(SdaiRepository repo) throws SdaiException {
		inst_count = 0;
		gl_rules_count = 0;
		un_rules_count = 0;

		ASdaiModel models = repo.getModels();
		SdaiIterator modIter = models.createIterator();
// Running through all models in the repository
		while (modIter.next()) {
			SdaiModel model = models.getCurrentMember(modIter);
			if (model == work_model) {
				continue;
			}
			inst_count += model.getInstanceCount();
		}

		ASchemaInstance schemas = repo.getSchemas();
		SdaiIterator schemaIter = schemas.createIterator();
		SdaiIterator entityIter = null;
		while (schemaIter.next()) {
			SchemaInstance schema_inst = schemas.getCurrentMember(schemaIter);
			ESchema_definition schema = schema_inst.getNativeSchema();
			ARule_declaration decls = schema.getRule_declarations(null, null);
			gl_rules_count += decls.getMemberCount();
			AEntity_declaration e_decls = schema.getEntity_declarations(null, null);
			if (entityIter == null) {
				entityIter = e_decls.createIterator();
			} else {
				e_decls.attachIterator(entityIter);
			}
			while (entityIter.next()) {
				EEntity_declaration edecl = e_decls.getCurrentMember(entityIter);
				EEntity_definition edef = (EEntity_definition)edecl.getDefinition(null);
				AUniqueness_rule u_rules = edef.getUniqueness_rules(null, null);
				un_rules_count += u_rules.getMemberCount();
			}
		}

		all_steps_count = inst_count + gl_rules_count + un_rules_count;
		return all_steps_count;
	}


	private String identifyMissingSchema(SdaiException e) {
		Object b = e.getErrorBase();
		if (b == null || !(b instanceof Object[])) {
			return null;
		}
		Object[] base = (Object[])b;
		if (base.length != 2 || !(base[0] instanceof Integer)) {
			return null;
		}
		int mark = ((Integer)base[0]).intValue();
		if (mark != 1 || !(base[1] instanceof String)) {
			return null;
		}
		return (String)base[1];
	}

	static public void main(String args[]) {
		main(args, null);
	}

	static public void main(String args[], UtilMonitor monitor) {
		if (args.length < 1) { 
			System.out.println("Part 21 file (exchange structure) must be indicated.");
			return;
		}
		boolean use_xim = false;
		Validate validation = null;
		try {
			for (int ihi = 0; ihi < args.length-1; ihi++) {
				if (args[ihi].equalsIgnoreCase("-stdout")) {
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
 					 fout = new FileOutputStream(args[ihi]);
					 bout = new BufferedOutputStream(fout);
					 pout = new PrintStream(bout);
					 System.setOut(pout);
    			// RR - unsuccessful experiment
    			//osw = new OutputStreamWriter(fout);
    		  //pw = new QueueWriter(osw, true, monitor);
				}
				if (args[ihi].equalsIgnoreCase("-stderr")) {
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
					ferr = new FileOutputStream(args[ihi]);
					berr = new BufferedOutputStream(ferr);
					perr = new PrintStream(berr);
					System.setErr(perr);
				}
				if (args[ihi].equalsIgnoreCase("-xim")) {
					use_xim = true;
				}
			}	

// Output stream is initialized
			if (monitor == null)
				pw = new PrintWriter(System.out, true);
			else
				pw = new QueueWriter(System.out, true, monitor);
			SdaiSession.setLogWriter(pw);
			pw.println("'Validate' program. Copyright 1999-2005, LKSoftWare GmbH");
// Opening session, starting a transaction, importing an exchange structure
			SdaiSession session = SdaiSession.openSession();
			SdaiTransaction trans = session.startTransactionReadWriteAccess();
			validation = new Validate();
			validation.only_xim = use_xim;
			//validation.only_xim = true;
			SdaiRepository repo = session.importClearTextEncoding("", args[0], null);
// did not work, Vaidas says - to set to null first, to try
//			pw.close();
//			pw = new PrintWriter(System.out, true);
//			SdaiSession.setLogWriter(pw);

			long time_before_validation = System.currentTimeMillis();
			long steps_count = validation.getStepsCount(repo);
			if (monitor != null) {
				monitor.worked(1);
				monitor.subTask("", steps_count, 0);
			}
// Validation of instances in repository 'repo'
			long tot_errors = validation.validationInstances(repo, monitor);
// Validation of global rules and uniqueness rules in repository 'repo'
			long [] tot_rules = validation.validationRules(repo, monitor);
// Printing validation time
			pw.println("count of erroneous instances: " + tot_errors);
			pw.println("count of violated global rules: " + tot_rules[0]);
			pw.println("count of violated uniqueness rules: " + tot_rules[1]);
			long time_after_validation = System.currentTimeMillis();
			long time_in_sec = (time_after_validation-time_before_validation) / 1000;
			pw.println("--- Validation time=" + time_in_sec + "sec");

// Ending the transaction, deleting the repository, closing the session
			trans.endTransactionAccessAbort();
			repo.closeRepository();
			repo.deleteRepository();
			session.closeSession();
		} catch (Exception eee) {
			if (eee instanceof SdaiException && ((SdaiException)eee).getErrorId() == SdaiException.SY_ERR && 
					validation != null && validation.identifyMissingSchema((SdaiException)eee) != null) {
				String sch = validation.identifyMissingSchema((SdaiException)eee);
				pw.println("The schema \"" + sch + "\" required to import the selected exchange structure" + 
					" does not exist in your library jar file." + System.getProperty("line.separator") + 
				"Please try again with appropriately chosen jar file.");
			} else {
				System.err.println("Exception occurred: " + eee);
				eee.printStackTrace();
			}
		} finally {
			if (pout != null) {
				pout.flush();
				pout.close();
			}
			if (perr != null) {
				perr.flush();
				perr.close();
			}
		}
	}

  	public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args, final UtilMonitor monitor)
  	throws SdaiException {
  		Properties jsdaiProperties = new Properties();
  		jsdaiProperties.setProperty("repositories", sdaireposDirectory);
			
			//      when always set, the following property causes non-xim validation not to work properly when run not with the xim library but with any library with any schemas
			//  		jsdaiProperties.setProperty("jsdai.SIda_step_schema_xim","AC*;AI*;AP*;ASS*;AU*;B*;C*;D*;E*;F*;G*;H*;IDA_STEP_AIM*;ISO*;IN*;J*;K*;L*;M*;N*;O*;P*;Q*;R*;S*;T*;U*;V*;W*;X*;Y*;Z*;");
			
			// here is one way to recognize if this is an ordinary validation with any library, or mim-xim validation with a xim library.
			for (int ihi = 0; ihi < args.length-1; ihi++) {
				/*
					if this property is needed only for xim or only for mim, or for everything except xim, 
					the following check may be narrowed to include only -XIM, only -MIM or -MIM and -dummy, but no -XIM
					NOTICE: it is quite possible, that -dummy is never used, for mim-xim validation perhaps -MIM or -XIM is always present
					for regular validation with an arbitrary library neither of these three switches is used
				*/
				if ((args[ihi].equals("-XIM")) || (args[ihi].equals("-MIM")) || (args[ihi].equals("-dummy"))) {
		  		jsdaiProperties.setProperty("jsdai.SIda_step_schema_xim","AC*;AI*;AP*;ASS*;AU*;B*;C*;D*;E*;F*;G*;H*;IDA_STEP_AIM*;ISO*;IN*;J*;K*;L*;M*;N*;O*;P*;Q*;R*;S*;T*;U*;V*;W*;X*;Y*;Z*;");
					break;
				}
			} // end for

  		SdaiSession.setSessionProperties(jsdaiProperties);
  		return new Runnable() {
  			public void run() {
  				main(args, monitor);
  			}
  		};
  	}

}