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

 	 Generator of application assertions for clause 4.3 of AP210 
 
 	 ----------------------------------------------------------------------------------------------------------------------
	 generate for entities that have 
		 explicit attributes, 
		 redeclaring explicit attributes, 
		 derived attributes,
		 also I'm sure - to find examples - redeclaring derived attributes, both redeclaring explicit and derived attributes 
	 without attributes - skip.
	 the type of such an attribute must be an entity or an aggregate of an entity, or a select with an entity in select present,
	 	 or an aggregate of select, probably.
	 In general, complicated nested selects probably should be supported by a recursive algorithm, 
	 	to find all the different entity types.
	 for simple types, defined types - skip. 
	 Check for defined types - aggregates of entities or selects, etc., (if present)
	 Probably should be generated for such cases as well, 
	 for example there probably should not be a difference between a direct aggregate and one, wrapped inside a defined type.
	 
	 
	 ---------------------------------------------------------------
	 here,  the inverse direction is not restricted, so 0, 1 or more

	 ENTITY  e1;
	  attr1 : e2;
	 END_ENTITY;
	 
	 ENTITY e2;
	 END_ENTITY;  

		
	
	4.3.x  E1 to E2
	Each E1 has attr1 defined by exactly one E2. Each E2 defines attr1 for zero, one, or more E1 objects.
	
	----------------------------------------------------------------
	if inverse is declared, it changes the second sentence:
	

	ENTITY  e1;
	  attr1 : e2;
	END_ENTITY;
	 
	ENTITY e2;
		INVERSE
			attr2 : SET[0:1] OF e1 FOR attr1;
	END_ENTITY;  

	4.3.x  E1 to E2
	Each E1 has attr1 defined by exactly one E2. Each E2 has attr2 defined by zero or one E1.        - SET [0:1]
                                               Each E2 has attr2 defined by one or more E1 objects.  - SET [1:?]
                                               Each E2 has attr2 defined by one, two, or three E1 objects - SET [1:3]
	
	
	
	I haven't find examples with more than 3 elements with specific min/max, what if SET [1:50], should I generate
	... by one, two, three, four, five, six, ..., fourty nine, or fifty .... 
	But if such cases are absent in ap210 arm, then this problem is non-existant.
	
	-------------------------------------------------------------------------
	if the attribute is an aggregate:
	
	
	 ENTITY  e1;
	  attr1 : SET [2:?] OF e2;
	 END_ENTITY;
	 
	 ENTITY e2;
	 END_ENTITY;  

	4.3.x  E1 to E2
	Each E1 has attr1 defined by two or more E2 objects. Each E2 defines attr1 for zero, one, or more E1 objects.
	
	
	-------------------------------------------------------------------------
	here is a case of a derived attribute of type SELECT:
	
	 TYPE s1 = SELECT(e2,e3,e4);
	 END_TYPE;
	
	 ENTITY  e1;
	 DERIVE
	  attr1 : s1 := ...;
	 END_ENTITY;
	 
	 ENTITY e2;
	 END_ENTITY;  

	 ENTITY e3;
	 END_ENTITY;  

	 ENTITY e4;
	 END_ENTITY;  
	
	4.3.x  E1 to E2
	Each E1 has attr1 defined by zero or one E2 objects. Each E2 defines attr1 for zero, one, or more E1 objects.
	This assertion is established through s1.

	4.3.x  E1 to E3
	Each E1 has attr1 defined by zero or one E3 objects. Each E3 defines attr1 for zero, one, or more E1 objects.
	This assertion is established through s1.

	4.3.x  E1 to E4
	Each E1 has attr1 defined by zero or one E4 objects. Each E4 defines attr1 for zero, one, or more E1 objects.
	This assertion is established through s1.
	
	--------------------------------------------------------
	
	There also may be more than one attribute of the same entity type, in that case only one assertion is generated
	for all the attributes of the same type, the assertion body includes the usual 2-3 sentences for each attribute.
	

	 ENTITY  e1;
	  attr1 : e2;
	  attr2 : SET [2:?] OF e2;
	 END_ENTITY;
	 
	 ENTITY e2;
	 END_ENTITY;  

	4.3.x  E1 to E2
	Each E1 has attr1 defined by exactly one E2. Each E2 defines attr1 for zero, one, or more E1 objects.
	Each E1 has attr2 defined by two or more E2 objects. Each E2 defines attr2 for zero, one, or more E1 objects.

	--------------------------------------------------------------------------------------------------------------------
	UNCLEAR:  the order of the assertions, in some cases, a group of assertions for a select attribute of one entity
	is split by assertions for other entities/attributes. 
	Even disregarding such cases, the assertions seem to be neither in the order of appearance 
	of the entities in the express schema, nor in the alphabetical order

	Also uncler the order of an assertion body parts for each attribute, in the case of multiple attributes of the same type.
	Does not seem it is accordingly to the original express, more likely - alphabetical 
	
	And this order may be important because it results into the numbers of sub-clauses in clause 4.3 
	
	From the implementation point of view it is best to use the same order as returned by lang from the dictionary sdai model.
	If it is all the same.
	Also, it is not possible to know the order of the entities in the express schema just from the data in the model, unless
	Gintaras preserves the order somehow somewhere
	The returned aggregates from lang, both with entities and with attributes have the element order more or less opposite,
	than in the original express schema, also attribute types are in the opposite order or without order, 
	first derived, later - explicit. Of course, attributes have 'order" field so it is possible to know the original order,
	but not for entities.
	
	As the first step, I am implementing in the order, returned by lang, later perhaps some resorting can be added if needed.
	
	----------------------------------------------------------------------------------------------------------------------------
	
	And how the cardinality in these assertions is affected by nested aggregates - seems to be no nested aggregates in ap210 arm.
	
	-----------------------------------------------------------------------------------------------------------------------------
	
*/

package jsdai.tools;

//import jsdai.dictionary.*;
import jsdai.SExtended_dictionary_schema.*;
// import jsdai.SMapping_schema.*;
import java.io.*;
import java.util.*;
import jsdai.lang.*;

public class ApplicationAssertionGenerator {

	PrintWriter pw;

	String arm_name = "AP210_ARM";
	SdaiModel arm_model;
	String arm_schema_name;
  String pw_file = "clause4-3.sgm";

	/**
	
	Run with the following command line:

	java jsdai.tools.ApplicationAssertionGenerator [-ARM arm_schema_name] [-sgml sgml_file_name]

	default arm schema name: AP210_ARM
	default sgml file name: clause4-3.sgm
	
	The input data is ExpressCompilerRepo repository with the arm model, 
	it must be available at the location specified in jsdai.properties in the line
	repositories=path 

  The location of jsdai.properties file itself may be provided on the command line too, for example, in the current directory:
  
	java -D"jsdai.properties=." jsdai.tools.ApplicationAssertionGenerator
	
	*/

	public static final void main(String args[]) throws SdaiException, IOException  {

		ApplicationAssertionGenerator gaa = new ApplicationAssertionGenerator();

		
	
		for (int ihi = 0; ihi < args.length; ihi++) {

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
				gaa.arm_name = args[ihi];
			} else
			if (args[ihi].equalsIgnoreCase("-sgml")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("An SGML file name must follow the " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("An SGML file name must follow the " + args[ihi-1] + " switch");
					return;
				}
				gaa.pw_file = args[ihi];
			}
			
		}  // for - loop through all command line parameters and switches

		gaa.arm_name += "_DICTIONARY_DATA";
		gaa.arm_schema_name = gaa.arm_name.substring(0,gaa.arm_name.length()-16);
		
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadOnlyAccess();
		
		gaa.pw = gaa.getPrintWriter(gaa.pw_file);

		gaa.generateSGML();
		session.closeSession();
	}

	void generateSGML() throws SdaiException {
	
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
		if (repository == null) {
			System.out.println("ExpressCompilerRepo repository not found");
			return;
		}
	
		if (!repository.isActive()) {  // open repo if not open
				repository.openRepository();
		}


   	ASdaiModel models = repository.getModels();

		iter = models.createIterator();
		arm_model = null;
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			if (model.getName().equalsIgnoreCase(arm_name)) {
				arm_model = model;
		    if (arm_model.getMode() == SdaiModel.NO_ACCESS) {
    			arm_model.startReadOnlyAccess();
    		}
			} 
		}
		if (arm_model == null) {
			System.out.println("arm model not found: " + arm_name);
			return;
		}
	
		printSGMLheader();
		// a loop through all the arm entities
		ESchema_definition schema = getSchema_definitionFromModel(arm_model);
		AEntity_declaration entities = schema.getEntity_declarations(null, null);
		iter = entities.createIterator();

    CompareUppercaseEntities ce = new CompareUppercaseEntities();
    TreeSet all_entities = new TreeSet(ce);


		while (iter.next()) {
			EEntity_definition arm_entity = (EEntity_definition)entities.getCurrentMember(iter).getDefinition(null);
			all_entities.add(arm_entity);
		}	

    Iterator i_entities = all_entities.iterator();

// String testing_str = Integer.toString(6,10);
// String testing_str2 = Integer.toString(46,10);
// System.out.println("Testing: " + testing_str);
// System.out.println("Testing2: " + testing_str2);

    while (i_entities.hasNext()) {

      EEntity_definition arm_entity = (EEntity_definition) i_entities.next();
			String current_arm_entity_name = arm_entity.getName(null);
			String current_arm_entity_name_normalized =  current_arm_entity_name.substring(0,1).toUpperCase() + current_arm_entity_name.substring(1).toLowerCase();
			// finding all the attributes of this entity, 
			// as ARM is not a short form, no need for domain
// pw.println("<!--  entity: " + current_arm_entity_name + " -->");
			AAttribute attributes = arm_entity.getAttributes(null, null);
			SdaiIterator iter2 = attributes.createIterator();
			HashSet already_processed_entity_types = new HashSet();
			while (iter2.next()) {
				EAttribute attribute = (EAttribute)attributes.getCurrentMember(iter2);
				String current_attribute_name = attribute.getName(null).toLowerCase(); 
// pw.println("<!--  \tattribute: " + current_attribute_name + " -->");
				// for each attribute, check if its type is an entity or a select that contains at least one entity.
				// for an entity - generate an assertion, for a select - 
				// generate as many assertions as many different entities found in the (possibly nested) select type.
				// also it would be nice to check defined types (also possibly nested, etc, if underlying type is a select with entities,
				// or an aggregate of entity type or an aggregate of a select with entities type - is it needed?
				// get the cardinality of the attribute type
				// now we can generate the first part of the body of the assertion, and also the third pard for selects 
				
				// returns a vector with an entity or more than one entity if select, together with min and max cardinalities if aggregate 
				Vector entity_types = getEntityTypesAndCardinalities(attribute);
				for (int i = 0; i < entity_types.size(); i++) {
					AttributeInfo info = (AttributeInfo)entity_types.elementAt(i);
					EEntity_definition ed = info.ed;
					if (already_processed_entity_types.add(ed)) {
						String attribute_type_entity_name = ed.getName(null);
						String attribute_type_entity_name_normalized =  attribute_type_entity_name.substring(0,1).toUpperCase() + attribute_type_entity_name.substring(1).toLowerCase();
						printAssertionHeader(current_arm_entity_name_normalized, attribute_type_entity_name_normalized);
						
						print1stSentence(current_arm_entity_name_normalized, attribute_type_entity_name_normalized, current_attribute_name, info);
						// for the second part, find the entity of the attribute type and see if it has an inverse attribute which
						// restricts the inverse cardinality.
						// Now the second part can be generated.
		
						AAttribute attributes2 = ed.getAttributes(null, null);
						AttributeInfo info1 = getInverseInfo(arm_entity, ed, attribute);
						print2ndSentence(current_arm_entity_name_normalized, attribute_type_entity_name_normalized, current_attribute_name, info1);
						print3rdSentence(info);
						
						// loop  also possible other attributes with the same entity type
						// it is possible not to avoid the attributes that are already processed, because they cannot have the same entity type.
						SdaiIterator iter3 = attributes.createIterator();
						while (iter3.next()) {
							EAttribute attribute3 = (EAttribute)attributes.getCurrentMember(iter3);
							if (attribute3 == attribute) continue;
							String attribute3_name = attribute3.getName(null).toLowerCase();
							Vector entity_types3 = getEntityTypesAndCardinalities(attribute3);
							for (int j = 0; j < entity_types3.size(); j++) {
								AttributeInfo info3 = (AttributeInfo)entity_types3.elementAt(j);
								EEntity_definition ed3 = info3.ed;
								if (ed3 == ed) {
									print1stSentence(current_arm_entity_name_normalized, attribute_type_entity_name_normalized, attribute3_name, info3);
									AttributeInfo info2 = getInverseInfo(arm_entity, ed, attribute3);
									print2ndSentence(current_arm_entity_name_normalized, attribute_type_entity_name_normalized, attribute3_name, info2);
									print3rdSentence(info3);
								}
							}
						}
						
						printAssertionFooter();
					} else {
						// already processed
						continue;
					}
				}
			}
		}	
    printSGMLfooter();
    pw.flush();
    pw.close();
	}
	
	AttributeInfo getInverseInfo(EEntity_definition main_entity, EEntity_definition attribute_entity, EAttribute direct_attribute) throws SdaiException {
		AttributeInfo result = new AttributeInfo(); 

		AAttribute attributes = attribute_entity.getAttributes(null, null);
		SdaiIterator iter = attributes.createIterator();
		while (iter.next()) {
			EAttribute attribute = (EAttribute)attributes.getCurrentMember(iter);
			if (attribute instanceof EInverse_attribute) {
				EInverse_attribute inverse_attribute = (EInverse_attribute)attribute;
				if (inverse_attribute.testInverted_attr(null)) {
					EExplicit_attribute inverted_attribute = inverse_attribute.getInverted_attr(null);
					if (inverted_attribute == direct_attribute) {
						// probably no need to test also the domain, but just ta catch bugs:
						if (inverse_attribute.testDomain(null)) {
							EEntity_definition domain = inverse_attribute.getDomain(null);
							if (domain == main_entity) {
								EBound min_cardinality = null;
								EBound max_cardinality = null;
								int min_cardinality_value = Integer.MIN_VALUE;
								int max_cardinality_value = Integer.MIN_VALUE;
								if (inverse_attribute.testMin_cardinality(null)) {
									min_cardinality = inverse_attribute.getMin_cardinality(null);
									if (min_cardinality.testBound_value(null)) {
										min_cardinality_value = min_cardinality.getBound_value(null);
									}
								}
								if (inverse_attribute.testMax_cardinality(null)) {
									max_cardinality = inverse_attribute.getMax_cardinality(null);
									if (max_cardinality.testBound_value(null)) {
										max_cardinality_value = max_cardinality.getBound_value(null);
									}
								}
								result.ed = domain;				
								result.is_aggregate = true;
								result.min = min_cardinality_value;
								result.max = max_cardinality_value;
								break; // there shoud not be more than one inverse attribute for the same inverted attribute, I think
							}
						}
					}
				}
			}
		}	
		return result;
	}

	Vector getEntityTypesAndCardinalities(EAttribute attribute) throws SdaiException {
		Vector result = new Vector();
		if (attribute instanceof EExplicit_attribute) {
			EExplicit_attribute xa = (EExplicit_attribute)attribute;
			EEntity domain = xa.getDomain(null);
			if (domain instanceof EEntity_definition) {
				AttributeInfo info = new AttributeInfo();
				info.ed = (EEntity_definition)domain;				
				result.addElement(info);
			} else
			if (domain instanceof EDefined_type) {
				handleDefinedType((EDefined_type)domain, result, null);
			} else 
			if (domain instanceof EAggregation_type) {
				handleAggregationType((EAggregation_type)domain, result, null);
			}
		}
		return result;
	}
	
	void handleDefinedType(EDefined_type dt, Vector result, AdditionalInfo data) throws SdaiException {

    EEntity ut = dt.getDomain(null);
    if (ut instanceof EAggregation_type) {
			handleAggregationType((EAggregation_type)ut, result, data);
    } else 
    if (ut instanceof EDefined_type) {
    	handleDefinedType((EDefined_type)ut, result, data);
    } else 
    if (ut instanceof ESelect_type) {
    	handleSelectType((ESelect_type)ut, result, data);
		}		
	}
	
	void handleAggregationType(EAggregation_type at, Vector result, AdditionalInfo data) throws SdaiException {
		EEntity element_type = at.getElement_type(null);
		if (element_type instanceof EEntity_definition) {
			AttributeInfo info = new AttributeInfo();
			info.ed = (EEntity_definition)element_type;				
			info.is_aggregate = true;
			if (at instanceof EVariable_size_aggregation_type) {
				EVariable_size_aggregation_type var = (EVariable_size_aggregation_type)at; 
				EBound lower_bound = null;
				EBound upper_bound = null;
				int lower_bound_value = Integer.MIN_VALUE;
				int upper_bound_value = Integer.MIN_VALUE;
				if (var.testLower_bound(null)) {
					lower_bound = var.getLower_bound(null);
					if (lower_bound.testBound_value(null)) {
						lower_bound_value = lower_bound.getBound_value(null);
					}
				}
				if (var.testUpper_bound(null)) {
					upper_bound = var.getUpper_bound(null);
					if (upper_bound.testBound_value(null)) {
						upper_bound_value = upper_bound.getBound_value(null);
					}
				}
				info.min = lower_bound_value;
				info.max = upper_bound_value;
			} else 
			if (at instanceof EArray_type) {
				EArray_type art = (EArray_type)at;
				EBound lower_index = null;
				EBound upper_index = null;
				int lower_index_value = Integer.MIN_VALUE;
				int upper_index_value = Integer.MIN_VALUE;
				if (art.testLower_index(null)) {
					lower_index = art.getLower_index(null);
					if (lower_index.testBound_value(null)) {
						lower_index_value = lower_index.getBound_value(null);
					}
				}
				if (art.testUpper_index(null)) {
					upper_index = art.getUpper_index(null);
					if (upper_index.testBound_value(null)) {
						upper_index_value = upper_index.getBound_value(null);
					}
				}
				info.min = lower_index_value;
				info.max = upper_index_value;
				info.is_array = true;
			} else {
				// should not get here
			}
			result.addElement(info);
		} else
		if (element_type instanceof EDefined_type) {
    	handleDefinedType((EDefined_type)element_type, result, data);
		} else
		if (element_type instanceof EAggregation_type) {
			// do not support nested aggregates for now.
			System.out.println("Nested aggregate, not supported: " + at);
		}
		
	}

	void handleSelectType(ESelect_type st, Vector result, AdditionalInfo data) throws SdaiException {
    ANamed_type nt = st.getSelections(null);
		SdaiIterator iter = nt.createIterator();
		while (iter.next()) {
			ENamed_type element = (ENamed_type)nt.getCurrentMember(iter);
			if (element instanceof EEntity_definition) {
				AttributeInfo info = new AttributeInfo();
				info.ed = (EEntity_definition)element;				
				info.is_select = true;
				result.addElement(info);
			} else 
			if (element instanceof EDefined_type) {
	    	handleDefinedType((EDefined_type)element, result, data);
			} 
		}
	}
	
			/*
				Each E1 has attr1 defined by two or more E2 objects. [2:?]
			                  defined by zero or one E2           [0:1]
			                  defined by one or more E2 objects  [1:0]
			                  defined by one, two, or three E2 objects - SET [1:3]
												defined by zero, one, or more E2 objects - SET without [] -- my guess
												defined by exactly 2 E2 objects - SET[2:2] 
												defined by exactly 1 E2  - SET[1:1]  - guess?
												defined by exactly 3 E2 objects - ARRAY [2:4] - my guess, all arrays - "exactly" 
 			*/

	void print1stSentence(String main_entity_name, String attribute_entity_name, String attribute_name, AttributeInfo info) {
		if (info.is_aggregate) {
		
			if (info.is_array) {
				int number = info.max - info.min + 1;
				if (number == 1) { 
					pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by exactly one " + attribute_entity_name + ".");
				} else {
					pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by exactly " + number2words(number) + " " + attribute_entity_name + " objects.");
				}
			} else {
				if (info.min == Integer.MIN_VALUE) {
					if (info.max == Integer.MIN_VALUE) {
						pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero, one, or more " + attribute_entity_name + " objects.");
					} else {
						// veird, it is not possible?
					}
				} else
				if (info.min == info.max) {
					switch (info.min) {
						case 1:
							pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by exactly one " + attribute_entity_name + ".");
							break;
						default:
							pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by exactly " + number2words(info.min) + " " + attribute_entity_name + " objects.");
							break;
					}
				} else 
				if (info.max == Integer.MIN_VALUE) {
					if (info.min == 0) {
						pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero, one, or more " + attribute_entity_name + " objects.");
					} else {
						pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by " + number2words(info.min) + " or more " + attribute_entity_name + " objects.");
					}
				} else {
					if (info.min == 0) {
						 switch (info.max) {
						 	case 1:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero or one " + attribute_entity_name + " objects.");
						 		break;
						 	case 2:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero, one, or two " + attribute_entity_name + " objects.");
						 		break;
						 	case 3:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero, one, two, or three " + attribute_entity_name + " objects.");
						 		break;
						 	case 4:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero, one, two, three, or four " + attribute_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero, or up to " + number2words(info.max) + " " + attribute_entity_name + " objects.");
						 		break;	
						 }
					} else 
					if (info.min == 1) {
						 switch (info.max) {
						 	case 2:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by one or two " + attribute_entity_name + " objects.");
						 		break;
						 	case 3:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by one, two, or three " + attribute_entity_name + " objects.");
						 		break;
						 	case 4:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by one, two, three, or four " + attribute_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by one, two, and up to " + number2words(info.max) + " " + attribute_entity_name + " objects.");
						 		break;	
						 }
					} else 
					if (info.min == 2) {
						 switch (info.max) {
						 	case 3:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by two or three " + attribute_entity_name + " objects.");
						 		break;
						 	case 4:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by two, three, or four " + attribute_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by two, three, and up to " + number2words(info.max) + " " + attribute_entity_name + " objects.");
						 		break;	
						 }
					} else 
					if (info.min == 3) {
						 switch (info.max) {
						 	case 4:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by three or four " + attribute_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by three, four, and up to " + number2words(info.max) + " " + attribute_entity_name + " objects.");
						 		break;	
						 }
					} else {
						pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by from " + number2words(info.min) + " to " + number2words(info.max) + " " + attribute_entity_name + " objects.");
					}
					
				}	
			}
												
		} else
		if (info.is_select) {
 			pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by zero or one " + attribute_entity_name + " objects.");
		} else {
			// just an entity definition
			pw.println("Each " + main_entity_name + " has " + attribute_name + " defined by exactly one " + attribute_entity_name + "."); 
		}
	}
	
	void print2ndSentence(String main_entity_name, String attribute_entity_name, String attribute_name, AttributeInfo info) {
		if (info.is_aggregate) {
			// inverse attribute
// Each E2 defines attr1 for zero, or one E1 objects

			if (info.min == Integer.MIN_VALUE) {
				// should not be unset in inverse attribute, but anyway.
				if (info.max == Integer.MIN_VALUE) {
					pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero, one, or more " + main_entity_name + " objects.");
				} else {
					// weirdly weird
				}	
			} else	
			if (info.min == info.max) {
				switch (info.min) {
					case 1:
						pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for exactly one " + main_entity_name + ".");
						break;
					default:
						pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for exactly " + number2words(info.min) + " " + main_entity_name + " objects.");
						break;
					}
			} else
			if (info.max == Integer.MIN_VALUE) {
				if (info.min == 0) {
					// hardly makes sense
					pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero, one, or more " + main_entity_name + " objects.");
				} else {
					pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for " + number2words(info.min) + " or more " + main_entity_name + " objects.");
				}
			} else {


					if (info.min == 0) {
						 switch (info.max) {
						 	case 1:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero or one " + main_entity_name + " objects.");
						 		break;
						 	case 2:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero, one, or two " + main_entity_name + " objects.");
						 		break;
						 	case 3:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero, one, two, or three " + main_entity_name + " objects.");
						 		break;
						 	case 4:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero, one, two, three, or four " + main_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero, or up to " + number2words(info.max) + " " + main_entity_name + " objects.");
						 		break;	
						 }
					} else 
					if (info.min == 1) {
						 switch (info.max) {
						 	case 2:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for one or two " + main_entity_name + " objects.");
						 		break;
						 	case 3:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for one, two, or three " + main_entity_name + " objects.");
						 		break;
						 	case 4:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for one, two, three, or four " + main_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for one, two, and up to " + number2words(info.max) + " " + main_entity_name + " objects.");
						 		break;	
						 }
					} else 
					if (info.min == 2) {
						 switch (info.max) {
						 	case 3:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for two or three " + main_entity_name + " objects.");
						 		break;
						 	case 4:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for two, three, or four " + main_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for two, three, and up to " + number2words(info.max) + " " + main_entity_name + " objects.");
						 		break;	
						 }
					} else 
					if (info.min == 3) {
						 switch (info.max) {
						 	case 4:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for three or four " + main_entity_name + " objects.");
						 		break;
						 	default:
								pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for three, four, and up to " + number2words(info.max) + " " + main_entity_name + " objects.");
						 		break;	
						 }
					} else {
						pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for from " + number2words(info.min) + " to " + number2words(info.max) + " " + main_entity_name + " objects.");
					}

			}
		} else {
			// no inverse attribute
			pw.println("Each " + attribute_entity_name + " defines " + attribute_name + " for zero, one, or more " + main_entity_name + " objects.");
		}
	}

	void print3rdSentence(AttributeInfo info) {
		if (info.is_select) {
			pw.println("This assertion is established through a SELECT type.");	
		}
	}
	
	void printAssertionHeader(String name1, String name2) {
		pw.println();
		pw.println("<application.assertion.cl3");
		pw.println("from.appobj.linkend=\"" + name1 + "\" to.appobj.linkend=\"" + name2 + "\">");
		pw.println("<description>");
	}

	void printAssertionFooter() {
		pw.println("</description>");
		pw.println("</application.assertion.cl3>");
	}

	void printSGMLheader() {
		pw.println("<!DOCTYPE APPLICATION.ASSERTIONS.SUBC PUBLIC \"-//NIST//DTD Publish - STEP Application Protocols V 2.2//EN\"");
		pw.println();
		pw.println("[");
		pw.println("<!ENTITY % figents PUBLIC \"-//SCRA//SGML Part 210 AP210 Illustrations//EN\">");
		pw.println("%figents;");
		pw.println("]>");
		pw.println("<application.assertions.subc>");
		pw.println("<application.assertions.subc.os>");
		pw.println("<!-- This file complies with version 299_35 of annexg. -->");
	}

	void printSGMLfooter() {
		pw.println("</application.assertions.subc>");
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


	String number2words(int number) {
		String english_0_19[] = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
		String english_tens[] = {"twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninety"};
		
		String sign = "";
		if (number < 0) {
			sign = "minus ";
			number = -number;
		}
		if (number < 20) {
			return sign + english_0_19[number];
		} else 
		if (number < 100) {
			int tens = number / 10;
			int remainder = number % 10;
			return sign + english_tens[tens-2] + " " + english_0_19[remainder];
		} else {
			// should implement a general case later, if needed.
			return Integer.toString(number, 10);
		}	
		
	}

  PrintWriter getPrintWriter(String file_name) throws IOException {
    FileOutputStream fos = new FileOutputStream(file_name);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    return pw;
  }


static class AttributeInfo {
	EEntity_definition ed;
	boolean is_aggregate;
	boolean is_select;
	boolean is_array;
	int min;
	int max;
	String select_name;
	// String entity_name;

	AttributeInfo() {
		is_aggregate = false;
		is_array = false;
		is_select = false;
		min = Integer.MIN_VALUE;
		max = Integer.MIN_VALUE;
		select_name = null;
	}
}

static class AdditionalInfo {
}

static class CompareUppercaseEntities implements Comparator {
  public int compare(Object ed1, Object ed2) {
    String name1 = null;
    String name2 = null;

    try {
      name1 = ((EEntity_definition) ed1).getName(null).toUpperCase();
      name2 = ((EEntity_definition) ed2).getName(null).toUpperCase();
    } catch (SdaiException ex) {
    }

    return name1.compareTo(name2);
  }

  public boolean equals(Object obj) {
    return false;
  }
}

}