// AnalyseAimInstances.java v. 2.2 2000-10-08
// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty
//
// This program reads physical file and tests it according to ARM schema.
//
// Output consist of two parts:
// ARM based ouput:
//		ARM entity name
//			ARM instance, that is mapping of this ARM entity
//				See also: (only if there are ARM alternatives)
//					names of alternative ARM entities
//				ARM attribute name ("OPT" - if attribute is optional) = value (question mark "$" if attribute is not set, values are separeted by comma "," if there are more that one value for this attribute).
// AIM based output:
// 	AIM instance
//			name of ARM entity that can be mapped to this AIM instance
//				ARM attribute name ("OPT" - if attribute is optional) = value (question mark "$" if attribute is not set, values are separeted by comma "," if there are more that one value for this attribute).
// To run:
// java AnalyseAimInstances input_file arm_schme_name
// Where:
// 	input_file - clear text encoding file (according Part 21) with AIM instances.
// 	arm_schema_name - name of ARM schema according to which input_file will be analyzed.
// Example: java AnalyseAimInstances test.pf AP210_ARM

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import jsdai.util.LangUtils;
import java.util.Comparator;
import java.util.Arrays;
import java.util.ArrayList;
import jsdai.util.MappingOperations;
import java.util.Date;
import jsdai.util.ComparatorOfEntities;
import jsdai.util.ComparatorOfInstances;
import jsdai.lang.SdaiCalendar;

class ArmInfo {
	EEntity aimInstance;
	boolean allAttributesSet;
	StringBuffer output;
	StringBuffer attributes[];

	public void print(String s, String s2) {
		System.out.println(s + s2 + output);
		for (int i = 0; i < attributes.length; i++) {
			System.out.println(s + "	" + attributes[i]);
		}
	}

	public String getShortInfo() throws SdaiException {
		return (allAttributesSet ? "!" : "?") + aimInstance.getPersistentLabel();
	}
}

public class AnalyseAimInstances {

	public static final void main(String args[]) {
		try {
			if (args.length < 2) {
				System.out.println("Usage of program: java AnalyseAimInstances input_file arm_schema_name");
				return;
			}
			boolean printAllArmTypes = true; // otherwise only populated
			boolean printExtendedArm = false; // otherwise only the AIM instance
			boolean shrinkArmArrays = true;
			int attributeMode = 2; // 0 - no limitations
											// 1 - only ARM entities with all mandatory attributes set
											// 2 - all ARM entities, but with info: "!" - ok, "?" - some mandatory att
			boolean supressValuesForArmAttributes = true;
			boolean printArmAttributeValidationInfo = true; // in ARM output for every AIM instance to print "?" or "!" depending on mandatory attributes of ARM entity

			SdaiSession session = jsdai.lang.SdaiSession.openSession();
			SdaiTransaction trans = session.startTransactionReadWriteAccess();

			Date d = new Date(System.currentTimeMillis());
			String data = d.toString();
			SdaiCalendar calendar =  new SdaiCalendar();
			data = calendar.longToTimeStamp(System.currentTimeMillis());

			System.out.println("*** AnalyseAimInstances " + session.getSdaiImplementation().getLevel() + ", actual time: " + data + " ***");
			System.out.println("*** Copyright 2000-2003, LKSoftWare GmbH ***");

			String schemaName = null;
			boolean modelFound = false;
			SdaiModel mappingModel = null, dataModel = null;
//dataModel.deleteSdaiModel();
			// Import data which will be analyzed.
			SdaiRepository dataRep = session.importClearTextEncoding("", args[0], null);
			ASdaiModel dataSi = dataRep.getSchemas().getAssociatedModels();
			ASdaiModel set = dataRep.getModels();
			SdaiIterator iter = set.createIterator();
			modelFound = false;
			// This program assumest that analyzed data have only one model.
			while (iter.next()) {
				dataModel = set.getCurrentMember(iter);
				modelFound = true;
				break;
			}
			if (!modelFound) {
				System.out.println("No application data found.");
				return;
			}

			// Prepare dictionary data.
			ASdaiModel dictionaryModels = session.getDataDictionary().getRepository().getModels();
			SdaiIterator iterator = dictionaryModels.createIterator();
			SdaiModel dictionaryModel = null;
			modelFound = false;
			schemaName = args[1] + "_MAPPING_DATA";
			// Find mapping model where mapping between AIM and ARM is defined.
			while (iterator.next()) {
				mappingModel = dictionaryModels.getCurrentMember(iterator);
				if (mappingModel.getName().equalsIgnoreCase(schemaName)) {
					modelFound = true;
					break;
				}
			}
			if (!modelFound) {
				System.out.println("No mapping data found.");
				return;
			}
			// It is required to start read access before using model.
			// Dictionary models can be opened only for reading.
			mappingModel.startReadOnlyAccess();
			// Take or create schema instances to colect mapping data.
			// These schema instances needs to include all used ARM, AIM and mapping models.
			// In this case we take all mapping and dictionary models.
			ASdaiModel mappingSi = session.getDataDictionary().getRepository().getSchemas().getAssociatedModels();

			schemaName = args[1] + "_DICTIONARY_DATA";
			iterator = dictionaryModels.createIterator();
			dictionaryModel = null;
			modelFound = false;
			// Find ARM dictionary model.
			while (iterator.next()) {
				dictionaryModel = dictionaryModels.getCurrentMember(iterator);
				if (dictionaryModel.getName().equalsIgnoreCase(schemaName)) {
					modelFound = true;
					break;
				}
			}
			if (!modelFound) {
				System.out.println("No dictionary data found.");
				return;
			}
			// Start read only access for ARM dictionary model.
			dictionaryModel.startReadOnlyAccess();

			ESchema_definition armSchema;
			// This simple method searhces for instance of ESchema_definition.
			// We assume that only one instance of ESceham_definition is in dictionary model.
			armSchema = LangUtils.findArmSchema(dictionaryModel);
			// Another method which finds all entities that belong to one schema.
			EEntity_definition entities[] = LangUtils.getEntitiesOfSchema(armSchema);
			EEntity_definition armEntity = null;
			boolean foundEntity = false;
			int n = entities.length;
			// Iterate through all ARM entites.
			for (int l = 0; l < n; l++) {
				armEntity = entities[l];
				testOneArmEntity(armEntity, dataModel, dataSi, mappingSi, printArmAttributeValidationInfo);
			}
			// Sorting.
			Arrays.sort(entities, new ComparatorOfEntities());
			// Output.
			System.out.println("---------------- ARM Entities ----------------");
			for (int l = 0; l < n; l++) {
				armEntity = entities[l];
				printOneArmEntity(armEntity, dataModel, dataSi, mappingSi, printAllArmTypes, printExtendedArm, shrinkArmArrays, printArmAttributeValidationInfo);
			}
			System.out.println();
			System.out.println("---------------- AIM Instances ---------------");
			printAimInstances(dataModel, dataSi, mappingSi, attributeMode);
			// We do not need to store data. So close session.
			trans.abort();
			session.closeSession();
		} catch(Exception e) {
e.printStackTrace();
System.exit(0);
		}
	}

	public static void testOneArmEntity(EEntity_definition armEntity, SdaiModel dataModel, ASdaiModel dataSi, ASdaiModel mappingSi, boolean printArmAttributeValidationInfo) throws SdaiException {
		if (armEntity.getTemp() != null) {
			return;
		}
		AEntity_definition subtypes = new AEntity_definition();
		CEntity_definition.usedinSupertypes(null, armEntity, null, // for now it is OK to use null. But latter if ARM will be spliten into modules there shuld be a domain where to search.
					subtypes);
		SdaiIterator subi = subtypes.createIterator();
		while (subi.next()) {
			testOneArmEntity(subtypes.getCurrentMember(subi), dataModel, dataSi, mappingSi, printArmAttributeValidationInfo);
		}
		try {
			// Find mappings of one ARM entity.
			AEntity instances = dataModel.findMappingInstances(armEntity, dataSi, mappingSi);
			if (instances == null) {
				return;
			}
			ArrayList mappings = new ArrayList();
			SdaiIterator k = instances.createIterator();
			// Iterate through all instances that are mappings of one ARM entity.
			while (k.next()) {
				EEntity instance = instances.getCurrentMemberEntity(k);
				ArrayList arms = null;
    			if (instance.getTemp() == null) {
       			arms = new ArrayList();
					instance.setTemp(arms);
				} else {
					arms = (ArrayList)instance.getTemp();
     			}
				boolean f = true;
				for (int i = 0; (i < arms.size()) && f; i++) {
					f = !LangUtils.isSupertype(armEntity, (EEntity_definition)arms.get(i));
				}
				if (f) {
					if (printArmAttributeValidationInfo) {
						mappings.add(callculateOutputForArmEntity(armEntity, instance, dataSi, mappingSi));
					} else {
						mappings.add(instance);
					}
					arms.add(armEntity);
				}
//				armEntity.setTemp(mappings);
			}
			if (printArmAttributeValidationInfo) {
				armEntity.setTemp(mappings.toArray(new ArmInfo[mappings.size()]));
			} else {
				armEntity.setTemp(mappings.toArray(new EEntity[mappings.size()]));
			}
		} catch (Exception e) {
e.printStackTrace();
//System.exit(0);
		}
	}

	public static void printOneArmEntity(EEntity_definition armEntity, SdaiModel dataModel, ASdaiModel dataSi, ASdaiModel mappingSi, boolean printAllArmTypes, boolean printExtendedArm, boolean shrinkArmArrays, boolean printArmAttributeValidationInfo) throws SdaiException {
		Object instances[] = (Object[])armEntity.getTemp();
		if (printAllArmTypes) {
			System.out.println(armEntity.getName(null));
		} else if (instances != null) {
			if (instances.length > 0) {
				System.out.println(armEntity.getName(null));
			} else {
				return;
			}
		}
		if (instances == null) {
			return;
		}
		if (shrinkArmArrays) {
			if (instances.length > 0) {
				if (printArmAttributeValidationInfo) {
					StringBuffer buf = new StringBuffer();
					boolean allOk = true;
					ArmInfo armInfo = (ArmInfo)instances[0];
					buf.append(armInfo.getShortInfo());
					allOk = allOk && armInfo.allAttributesSet;
					for (int i = 1; i < instances.length; i++) {
						buf.append(", ");
						armInfo = (ArmInfo)instances[i];
						buf.append(armInfo.getShortInfo());
						allOk = allOk && armInfo.allAttributesSet;
					}
					buf.append(")");
					String s = allOk ? "	! (" : "	? (";
					System.out.println(s + buf);
				} else {
					System.out.println("	" + MappingOperations.getArrayAsString(instances));
				}
			}
		} else {
			// Iterate through all instances that are mappings of one ARM entity.
			for (int j = 0; j < instances.length; j++) {
				EEntity instance = null;
				if (printArmAttributeValidationInfo) {
					instance = ((ArmInfo)instances[j]).aimInstance;
				} else {
					instance = (EEntity)instances[j];
				}
				System.out.print("	" + instance);
				if (!printExtendedArm) {
					System.out.println();
					continue;
				}
				if (instance.getTemp() != null) {
					ArrayList otherArms = (ArrayList)instance.getTemp();
					int n = otherArms.size();
					if (n > 1) {
						System.out.println();
						System.out.println("		See also:");
						boolean printComma = false;
						for (int l = 0; l < n; l++) {
							EEntity_definition otherArm = (EEntity_definition)otherArms.get(l);
							if (otherArm != armEntity) {
								if (printComma) {
									System.out.println(",");
								}
								printComma = true;
								System.out.print("			" + otherArm.getName(null));
							}
						}
					}
				}
				System.out.println();
				EAttribute armAttribute = null;
				ArrayList attributes = new ArrayList();
				LangUtils.findExplicitAttributes(armEntity, attributes);
				// Iterate through all explicit attributes of ARM entity.
				for (int i = 0; i < attributes.size(); i++) {
					armAttribute = (EAttribute)attributes.get(i);
					try {
						// Test if ARM attribute is set.
						System.out.print("		" + armAttribute.getName(null));
						boolean f = instance.testSourceAttribute(armAttribute, dataSi, mappingSi) != null;
						// If ARM attribute is set, then take and print a value of it.
						if (f) {
							Object o[] = instance.getSourceAttribute(armAttribute, dataSi, mappingSi);
							if (o.length > 0) {
								if (o[0] instanceof String) {
									System.out.print(" = '" + o[0] + "'");
								} else {
									System.out.print(" = " + o[0]);
								}
								for (int k = 1; k < o.length; k++) {
									if (o[k] instanceof String) {
										System.out.print(" | '" + o[k] + "'");
									} else {
										System.out.print(" | " + o[k]);
									}
								}
							} else {
								System.out.print(" = $");
							}
						}
						System.out.println();
					} catch (Exception e) {
e.printStackTrace();
System.exit(0);
					}
				}
			}
		}
	}

	public static void printAimInstances(SdaiModel dataModel, ASdaiModel dataSi, ASdaiModel mappingSi, int mode) throws SdaiException {
		// iteration through all instances
		EEntity[] instances = (EEntity[])LangUtils.aggregateToArray(dataModel.getInstances(), EEntity.class);
		Arrays.sort(instances, new ComparatorOfInstances());
		int numberOfInstances = instances.length;
		for (int j = 0; j < numberOfInstances; j++) {
			EEntity instance = instances[j];
			System.out.println(instance);
			if (instance.getTemp() != null) {
				ArrayList arms = (ArrayList)instance.getTemp();
				removeSupertypes(arms);
				int n = arms.size();
				nextArm: for (int l = 0; l < n; l++) {
					EEntity_definition armEntity = (EEntity_definition)arms.get(l);
//					ArmInfo ifos[] = (ArmInfo[])armEntity.getTemp();
					//for (int k = 0; k < ifos.length; k++) {
						ArmInfo info = callculateOutputForArmEntity(armEntity, instance, dataSi, mappingSi);
						if (info.aimInstance == instance) {
							String s2 = "";
							if (mode == 1) {
								if (!info.allAttributesSet) {
									continue nextArm;
								}
								s2 = "! ";
							} else if (mode == 2) {
								if (info.allAttributesSet) {
									s2 = "! ";
								} else {
									s2 = "? ";
								}
							}
							info.print("	", s2);
							continue nextArm;
						}
					//}
				}
			}
		}
	}

	public static void removeSupertypes(ArrayList arms) throws SdaiException {
		nextArmEntity: for (int i = 0; i < arms.size(); i++) {
			EEntity_definition ed1 = (EEntity_definition)arms.get(i);
			for (int j = i+1; j < arms.size(); j++) {
				EEntity_definition ed2 = (EEntity_definition)arms.get(j);
				if (LangUtils.isSupertype(ed1, ed2)) {
					arms.remove(i);
					i--;
					continue nextArmEntity;
				}
			}
		}
	}

	static ArmInfo callculateOutputForArmEntity(EEntity_definition armEntity, EEntity instance, ASdaiModel dataSi, ASdaiModel mappingSi) throws SdaiException {
		ArmInfo rv = new ArmInfo();
		rv.aimInstance = instance;
		rv.output = new StringBuffer(armEntity.getName(null));
		rv.allAttributesSet = true;
		EAttribute armAttribute = null;
		ArrayList attributes = new ArrayList();
		LangUtils.findExplicitAttributes(armEntity, attributes);
		rv.attributes = new StringBuffer[attributes.size()];
		// Iterate through all explicit attributes of ARM entity.
		for (int i = 0; i < attributes.size(); i++) {
			armAttribute = (EAttribute)attributes.get(i);
			try {
				// Test if ARM attribute is set.
				rv.attributes[i] = new StringBuffer("		" + armAttribute.getName(null));
				boolean opt = false;
				if (armAttribute instanceof EExplicit_attribute) {
					if (((EExplicit_attribute)armAttribute).getOptional_flag(null)) {
						rv.attributes[i].append(" (OPT)");
						opt = true;
					}
				}
				boolean f = instance.testSourceAttribute(armAttribute, dataSi, mappingSi) != null;
				// If ARM attribute is set, then take and print a value of it.
				if (f) {
					Object o[] = instance.getSourceAttribute(armAttribute, dataSi, mappingSi);
					if (o.length > 0) {
						rv.attributes[i].append(" = ");
						if (o.length < 2) {
							if (o[0] instanceof String) {
								rv.attributes[i].append("'" + o[0] + "'");
							} else if (o[0] instanceof EEntity) {
								rv.attributes[i].append(((EEntity)o[0]).getPersistentLabel());
							} else {
								rv.attributes[i].append(o[0]);
							}
						} else {
  							rv.attributes[i].append(MappingOperations.getArrayAsString(o));
						}
					} else {
						rv.attributes[i].append(checkNoMapping(armAttribute, mappingSi) ? " = %" : " = $");
						f = false;
					}
				} else {
					rv.attributes[i].append(checkNoMapping(armAttribute, mappingSi) ? " = %" : " = $");
				}
				if (!f && !opt) {
					rv.allAttributesSet = false;
				}
			} catch (Exception e) {
e.printStackTrace();
//System.exit(0);
			}
		}
		return rv;
	}

	public static boolean checkNoMapping(EAttribute attribute, ASdaiModel mappingSi) throws SdaiException {
		boolean rv;
		AAttribute_mapping mappings = new AAttribute_mapping();
		CAttribute_mapping.usedinSource(null, attribute, mappingSi, mappings);
		rv = mappings.getMemberCount() < 1;
		return rv;
	}
}