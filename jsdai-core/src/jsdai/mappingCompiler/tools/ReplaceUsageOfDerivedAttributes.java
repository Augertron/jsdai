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

package jsdai.mappingCompiler.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.SMapping_schema.*;
import jsdai.lang.*;
import jsdai.mappingCompiler.util.*;
import jsdai.util.LangUtils;

///%*
//% Reads mapping model and finds usage of derived attributes in it.
//%/
public class ReplaceUsageOfDerivedAttributes {
	static SdaiSession session;
	static SdaiTransaction trans;

	static final String change[][] = {
			{"description", "application_context",			"description_attribute", "described_item",	"attribute_value"},
			{"description", "configuration_design",			"description_attribute", "described_item",	"attribute_value"},
			{"description", "date_role", 					"description_attribute", "described_item",	"attribute_value"},
			{"description", "date_time_role",				"description_attribute", "described_item",	"attribute_value"},
			{"description", "effectivity",					"description_attribute", "described_item",	"attribute_value"},
			{"description", "external_source",				"description_attribute", "described_item",	"attribute_value"},
			{"description", "organization_role",			"description_attribute", "described_item",	"attribute_value"},
			{"description", "person_and_organization_role", "description_attribute", "described_item",	"attribute_value"},
			{"description", "representation",				"description_attribute", "described_item",	"attribute_value"},
			{"description",	"property_definition_representation", "description_attribute", "described_item", "attribute_value"},
			{"description",	"context_dependent_shape_representation", "description_attribute", "described_item", "attribute_value"},
			{"id", 			 "organizational_project",		"id_attribute", 		 "identified_item", "attribute_value"},
			{"id",			 "representation",				"id_attribute",			 "identified_item", "attribute_value"},
			{"id", 			 "action", 						"id_attribute", 		 "identified_item", "attribute_value"},
			{"id", 			 "property_definition",			"id_attribute", 		 "identified_item", "attribute_value"},
			{"id", 			 "shape_aspect_relationship",	"id_attribute", 		 "identified_item", "attribute_value"},
			{"id", 			 "shape_aspect",				"id_attribute", 		 "identified_item", "attribute_value"},
			{"id", 			 "group", 						"id_attribute", 		 "identified_item", "attribute_value"},
			{"name", 	"action_request_solution",		 	"name_attribute", 		 "named_item",		"attribute_value"},
			{"name", 	"person_and_organization", 			"name_attribute", 		 "named_item",		"attribute_value"},
			{"name", 	"product_definition",				"name_attribute", 		 "named_item",		"attribute_value"},
			{"name",	"property_definition_representation", "name_attribute", 	 "named_item",		"attribute_value"},
			{"name",	"configuration_design", 			"name_attribute", 		 "named_item",		"attribute_value"},
			{"name",	"derived_unit", 					"name_attribute", 		 "named_item",		"attribute_value"},
			{"role",	"action_assignment",				"role_association",		 "item_with_role",	"role"},
			{"role",	"approval_date_time",				"role_association",		 "item_with_role",	"role"},
			{"role",	"document_reference",				"role_association",		 "item_with_role",	"role"},
			{"role",	"effectivity_assignment",			"role_association",		 "item_with_role",	"role"},
			{"role",	"action_request_assignment",		"role_association",		 "item_with_role",	"role"},
			{"role",	"group_assignment",					"role_association",		 "item_with_role",	"role"},
			{"role",	"name_assignment",					"role_association",		 "item_with_role",	"role"},
			{"name",	"effectivity",						"name_attribute", 		 "named_item",		"attribute_value"}
		};

	static private Set notFoundAttributes;
	static private Set deletedInstances;

	public static final void main(String args[]) throws SdaiException {
		SdaiRepository repository;
		SchemaInstance schemaInstance;
		SdaiModel mappingModel = null, armDataModel = null, aimDataModel;
		if (args.length < 2) {
			System.out.println("Usage of program: java ReplaceUsageOfDerivedAttributes input_repository mapping_model_name");
			return;
		}
		session = SdaiSession.openSession();
		trans = session.startTransactionReadWriteAccess();
		repository = LangUtils.openRepositoryOrFile(args[0]);
		if (repository == null) {
			System.out.println("Repository \"" + args[0] + "\" can not be found.");
			return;
		}
		mappingModel = LangUtils.findModel(args[1], repository);
		if (mappingModel == null) {
			System.out.println("Model \"" + args[1] + "\" can not be found.");
			return;
		}
		mappingModel.startReadWriteAccess();
		replaceDerivedAttributes(mappingModel);
 		trans.commit();
 	}

        public static void replaceDerivedAttributes(SdaiModel mappingModel) throws SdaiException
        {
            AEntity instances = mappingModel.getInstances();
            SdaiIterator iterator = instances.createIterator();
            ArrayList derivedAttributes = new ArrayList();
            ESchema_mapping mappingSchema = MappingUtils.findMappingSchema(mappingModel);
            ESchema_definition aimSchema = mappingSchema.getTarget(null);
            EEntity_definition aimEntities[] = MappingUtils.getEntitiesOfSchema(aimSchema);
			notFoundAttributes = new TreeSet();
			deletedInstances = new HashSet();
            while (iterator.next())
            {
                EEntity instance = instances.getCurrentMemberEntity(iterator);
                EEntity attribute = null;
                EEntity attribute2 = null;
                if (instance instanceof EInverse_attribute_constraint)
                {
                    attribute = MappingUtils.getAttribute(instance);
                } else if (instance instanceof EString_constraint)
                {
                    attribute = MappingUtils.getAttribute(instance);
                    if (attribute instanceof EDerived_attribute)
                    {
                        replaceAttributeInStringConstraint((EDerived_attribute)attribute, mappingModel, (EString_constraint)instance, aimEntities);
                    }
                } else if (instance instanceof EAttribute_value_constraint)
                {
                    attribute = MappingUtils.getAttribute(instance);
                } else if (instance instanceof ESelect_constraint)
                {
                    attribute = MappingUtils.getAttribute(instance);
                } else if (instance instanceof EEntity_constraint)
                {
                    attribute = MappingUtils.getAttribute(instance);
                } else if (instance instanceof EAggregate_member_constraint)
                {
                    attribute = MappingUtils.getAttribute(instance);
                } else if (instance instanceof EAggregate_size_constraint)
                {
                    attribute = MappingUtils.getAttribute(instance);
                } else if (instance instanceof EConstraint_relationship)
                {
					if(((EConstraint_relationship)instance).testElement2(null)) {
						if(instance instanceof EInstance_constraint
						&& !((EInstance_constraint)instance).testElement1(null)) {
							System.out.print("element1 is unset: ");
							System.out.println(instance);
						} else {
							attribute = MappingUtils.getAttribute(instance);
						}
						attribute2 = MappingUtils.getElement2((EConstraint_relationship)instance);
						if (attribute instanceof EDerived_attribute)
						{
							replaceAttributeInConstraintRelationship((EDerived_attribute)attribute, mappingModel, (EConstraint_relationship)instance, aimEntities, true);
						}
						if (attribute2 instanceof EDerived_attribute)
						{
							replaceAttributeInConstraintRelationship((EDerived_attribute)attribute2, mappingModel, (EConstraint_relationship)instance, aimEntities, false);
						}
					} else {
						System.out.println("Element2 is unset: " + instance);
					}
                } else if (instance instanceof EGeneric_attribute_mapping)
                {
					if (instance instanceof EAttribute_mapping) {
						EAttribute_mapping attributeMapping = (EAttribute_mapping)instance;
						if (attributeMapping.testPath(null)) {
							AEntity pathElements = attributeMapping.getPath(null);
							SdaiIterator j = pathElements.createIterator();
							while (j.next()) {
								EEntity pathElem = pathElements.getCurrentMemberEntity(j);
								if (pathElem instanceof EDerived_attribute) {
									if (!derivedAttributes.contains(pathElem)) {
// 										System.out.println(instance);
										derivedAttributes.add(pathElem);
									}
									boolean f = replaceAttributeInPath((EDerived_attribute)pathElem, mappingModel, pathElements, j, aimEntities);
									if (!f) {
										System.out.println("The attribute " + ((EDerived_attribute)pathElem).getParent(null).getName(null) + "." + ((EDerived_attribute)pathElem).getName(null) + " (" + pathElem + ") can not be replaced in " + attributeMapping);
									}
								}
							}
						}
					}
                    EGeneric_attribute_mapping attributeMapping = (EGeneric_attribute_mapping)instance;
					
                    if (attributeMapping.testConstraints(null))
                    {
						EEntity constraints = attributeMapping.getConstraints(null);
						if (constraints instanceof EDerived_attribute) {
							if (!derivedAttributes.contains(constraints)) {
// 								System.out.println(instance);
								derivedAttributes.add(constraints);
							}
							EDerived_attribute derivedAttribute = (EDerived_attribute)constraints;
							int n = change.length;
							boolean f = false;
							for (int i = 0; i < n; i++) {
								if (derivedAttribute.getName(null).equals(change[i][0]) && derivedAttribute.getParent(null).getName(null).equals(change[i][1])) {
									EEntity_definition ed = MappingUtils.findEntityDefinition(change[i][2], aimEntities);
									if(ed == null) {
										notFoundAttributes.add(change[i][2]);
										continue;
									}
									EInverse_attribute_constraint iac = (EInverse_attribute_constraint)mappingModel.createEntityInstance(CInverse_attribute_constraint.class);
									EAttribute a = MappingUtils.findAttributeDefinition(ed, change[i][3]);
									iac.setInverted_attribute(null, a);
									iac = (EInverse_attribute_constraint)LangUtils.findSubstitute(iac);
									a = MappingUtils.findAttributeDefinition(ed, change[i][4]);
									EPath_constraint path = 
										(EPath_constraint)
										mappingModel.createEntityInstance(CPath_constraint.class);
									path.setElement1(null, iac);
									path.setElement2(null, a);
									path = (EPath_constraint)LangUtils.findSubstitute(path);
									attributeMapping.setConstraints(null, path);
									f = true;
									break;
								}
							}
							if (!f) {
								System.out.println("The attribute " + derivedAttribute.getParent(null).getName(null) + "." + derivedAttribute.getName(null) + " (" + derivedAttribute + ") can not be replaced in " + attributeMapping);
							}
						}
					}
                } else if (instance instanceof EEntity_mapping)
                {
                    jsdai.dictionary.EAttribute a = CEntity_mapping.attributeTarget(null);
                    attribute = (jsdai.lang.EEntity)instance.get_object(a);
                }
                if (attribute instanceof EDerived_attribute)
                {
                    if (!derivedAttributes.contains(attribute))
                    {
// 						System.out.println(instance);
                        derivedAttributes.add(attribute);
                    }
                }
                if (attribute2 instanceof EDerived_attribute)
                {
                    if (!derivedAttributes.contains(attribute))
                    {
// 						System.out.println(instance);
                        derivedAttributes.add(attribute2);
                    }
                }
            }
			Iterator deletedInstanceIter = deletedInstances.iterator();
			while(deletedInstanceIter.hasNext()) {
				((EEntity)deletedInstanceIter.next()).deleteApplicationInstance();
			}
            Collections.sort(derivedAttributes, new ComparatorOfAttributes());
            int n = derivedAttributes.size();
            for (int i = 0; i < n; i++)
            {
                EDerived_attribute derivedAttribute = (EDerived_attribute)derivedAttributes.get(i);
                System.out.println(derivedAttribute.getParent(null).getName(null) + "." + derivedAttribute.getName(null));
            }
			if(notFoundAttributes.size() > 0) {
				System.out.println("The following entities were not found and therefore not replaced:");
				Iterator notFoundAttrIter = notFoundAttributes.iterator();
				while(notFoundAttrIter.hasNext()) {
					System.out.println(" " + notFoundAttrIter.next().toString());
				}
			}
        }
	public static boolean replaceAttributeInPath(EDerived_attribute attribute, SdaiModel mappingModel, AEntity path, SdaiIterator position, EEntity_definition aimEntities[]) throws SdaiException {
		int n = change.length;
  		boolean rv = false;
		for (int i = 0; i < n; i++) {
			if (attribute.getName(null).equals(change[i][0]) && attribute.getParent(null).getName(null).equals(change[i][1])) {
				EEntity_definition ed = MappingUtils.findEntityDefinition(change[i][2], aimEntities);
				if(ed == null) {
					notFoundAttributes.add(change[i][2]);
					continue;
				}
				EInverse_attribute_constraint iac = (EInverse_attribute_constraint)mappingModel.createEntityInstance(CInverse_attribute_constraint.class);
				EAttribute a = MappingUtils.findAttributeDefinition(ed, change[i][3]);
				iac.setInverted_attribute(null, a);
				iac = (EInverse_attribute_constraint)LangUtils.findSubstitute(iac);
				path.setCurrentMember(position, iac, null);
				a = MappingUtils.findAttributeDefinition(ed, change[i][4]);
				path.addAfter(position, a);
    			rv = true;
				break;
			}
		}
  		return rv;
	}

	public static EEntity replaceAttributeInStringConstraint(EDerived_attribute attribute, SdaiModel mappingModel, EString_constraint constraint, EEntity_definition aimEntities[]) throws SdaiException {
		EEntity rv = constraint;
		int n = change.length;
		for (int i = 0; i < n; i++) {
			if (attribute.getName(null).equals(change[i][0]) && attribute.getParent(null).getName(null).equals(change[i][1])) {
				EEntity_definition ed = MappingUtils.findEntityDefinition(change[i][2], aimEntities);
				if(ed == null) {
					notFoundAttributes.add(change[i][2]);
					continue;
				}
				EInverse_attribute_constraint iac = (EInverse_attribute_constraint)mappingModel.createEntityInstance(CInverse_attribute_constraint.class);
				EAttribute a = MappingUtils.findAttributeDefinition(ed, change[i][3]);
				iac.setInverted_attribute(null, a);
				iac = (EInverse_attribute_constraint)LangUtils.findSubstitute(iac);
				a = MappingUtils.findAttributeDefinition(ed, change[i][4]);
				EString_constraint sc = (EString_constraint)mappingModel.createEntityInstance(EString_constraint.class);
				sc.setConstraint_value(null, constraint.getConstraint_value(null));
				sc.setAttribute(null, a);
				sc = (EString_constraint)LangUtils.findSubstitute(sc);
				EPath_constraint path = (EPath_constraint)mappingModel.createEntityInstance(CPath_constraint.class);
				path.setElement1(null, iac);
				path.setElement2(null, sc);
				path = (EPath_constraint)LangUtils.findSubstitute(path);
				rv = path;
    			AEntity users = new AEntity();
       		constraint.findEntityInstanceUsers(null, users);
         	SdaiIterator j = users.createIterator();
          	while (j.next()) {
              	EEntity user = users.getCurrentMemberEntity(j);
              	if (user == path) {
              		continue;
              	}
               if (user instanceof EEntity_mapping) {
                  EEntity_mapping tmp = (EEntity_mapping)user;
                  tmp.setConstraints(null, rv);
               } else if (user instanceof EGeneric_attribute_mapping) {
				   EGeneric_attribute_mapping gam = (EGeneric_attribute_mapping)user;
				   gam.setConstraints(null, rv);
               } else if (user instanceof EConstraint) {
				   if (user instanceof EConstraint_relationship) {
					   EConstraint_relationship tmp = (EConstraint_relationship)user;
					   EEntity element2 = tmp.getElement2(null);
					   if (element2 == constraint) {
						   tmp.setElement2(null, rv);
					   }
					   if (tmp instanceof EInstance_constraint) {
						   EInstance_constraint ic = (EInstance_constraint)tmp;
						   EEntity element1 = ic.getElement1(null);
						   if (element1 == constraint) {
							   ((EInstance_constraint)tmp).setElement1(null, rv);
						   }
					   }
				   } else if(user instanceof ENegation_constraint) {
					   ENegation_constraint negation = (ENegation_constraint)user;
					   negation.setConstraints(null, path);
				   } else if(user instanceof EEnd_of_path_constraint) {
					   EEnd_of_path_constraint eop = (EEnd_of_path_constraint)user;
					   eop.setConstraints(null, path);
				   } else {
					   throw new
						   SdaiException(SdaiException.SY_ERR,
										 "Unexpected constraint in replaceAttributeInStringConstraint: " +
										 user.toString());
				   }
			   }
            }
			users.clear();
       		constraint.findEntityInstanceUsers(null, users);
			if(users.getMemberCount() == 0) {
				deletedInstances.add(constraint);
			}
				break;
			}
		}
		return rv;
	}

	public static EEntity replaceAttributeInConstraintRelationship(EDerived_attribute attribute, SdaiModel mappingModel, EConstraint_relationship constraint, EEntity_definition aimEntities[], boolean element1) throws SdaiException {
		EEntity rv = constraint;
		int n = change.length;
		for (int i = 0; i < n; i++) {
			if (attribute.getName(null).equals(change[i][0]) && attribute.getParent(null).getName(null).equals(change[i][1])) {
				EEntity_definition ed = MappingUtils.findEntityDefinition(change[i][2], aimEntities);
				if(ed == null) {
					notFoundAttributes.add(change[i][2]);
					continue;
				}
				EInverse_attribute_constraint iac = (EInverse_attribute_constraint)mappingModel.createEntityInstance(CInverse_attribute_constraint.class);
				EAttribute a = MappingUtils.findAttributeDefinition(ed, change[i][3]);
				iac.setInverted_attribute(null, a);
				iac = (EInverse_attribute_constraint)LangUtils.findSubstitute(iac);
				a = MappingUtils.findAttributeDefinition(ed, change[i][4]);
				EPath_constraint path = (EPath_constraint)mappingModel.createEntityInstance(CPath_constraint.class);
    			boolean orderChanged = false;
    			if (element1) {
          		if (constraint instanceof EPath_constraint) {
               	// The new element inserted before this path constraint.
						((EPath_constraint)constraint).setElement1(null, a);
						path.setElement1(null, iac);
						path.setElement2(null, constraint);
						path = (EPath_constraint)LangUtils.findSubstitute(path);
						rv = path;
      				orderChanged = true;
	     			} else {
            		// Only the derived attribute changed with the secuence of constraints.
						path.setElement2(null, a);
						path.setElement1(null, iac);
						path = (EPath_constraint)LangUtils.findSubstitute(path);
						((EInstance_constraint)constraint).setElement1(null, path);
						rv = path;
	     			}
     			} else {
           		// Only the derived attribute changed with the secuence of constraints.
					path.setElement2(null, a);
					//constraint.setElement2(null, path);
					path.setElement1(null, iac);
					path = (EPath_constraint)LangUtils.findSubstitute(path);
					constraint.setElement2(null, path);
					rv = path;
     			}
    			AEntity users = new AEntity();
       		constraint.findEntityInstanceUsers(null, users);
         	SdaiIterator j = users.createIterator();
			EEntity prevUser = null;
          	while (j.next()) {
              	EEntity user = users.getCurrentMemberEntity(j);
				EEntity prevUserSave = prevUser;
				prevUser = user;
				if(prevUserSave == user) continue;
              	if (user == path) {
              		continue;
              	}
               if (orderChanged) {
	               if (user instanceof EEntity_mapping) {
					   if (element1) {
						   EEntity_mapping tmp = (EEntity_mapping)user;
						   tmp.setConstraints(null, rv);
					   }
				   } else if (user instanceof EConstraint) {
					   if (user instanceof EConstraint_relationship) {
						   EConstraint_relationship tmp = (EConstraint_relationship)user;
						   EEntity element2 = tmp.getElement2(null);
						   if (element2 == constraint) {
							   tmp.setElement2(null, rv);
						   }
						   if (tmp instanceof EInstance_constraint) {
							   EInstance_constraint ic = (EInstance_constraint)tmp;
							   EEntity element = ic.getElement1(null);
							   if (element == constraint) {
								   tmp.setElement2(null, rv);
							   }
						   }
					   } else if(user instanceof ENegation_constraint) {
						   ENegation_constraint negation = (ENegation_constraint)user;
						   negation.setConstraints(null, path);
					   } else if(user instanceof EEnd_of_path_constraint) {
						   EEnd_of_path_constraint eop = (EEnd_of_path_constraint)user;
						   eop.setConstraints(null, path);
					   } else {
						   throw new
							   SdaiException(SdaiException.SY_ERR,
								   "Unexpected constraint in replaceAttributeInConstraintRelationship: " +
											 user.toString());
					   }
				   }
               }
			   if (user instanceof EGeneric_attribute_mapping) {
				   if (user instanceof EAttribute_mapping && ((EAttribute_mapping)user).testPath(null)) {
					   EAttribute_mapping tmp = (EAttribute_mapping)user;
					   AAttribute_mapping_path_select pathElements = tmp.getPath(null);
					   SdaiIterator pei = pathElements.createIterator();
					   while (pei.next()) {
						   EEntity pathElement = pathElements.getCurrentMember(pei);
						   if (pathElement == constraint) {
							   if (orderChanged) {
								   pathElements.addBefore(pei, rv);
							   } else {
								   pathElements.addAfter(pei, rv);
								   pei.next();
							   }
						   }
					   }
				   }
				   if (element1) {
	   	               EGeneric_attribute_mapping tmp = (EGeneric_attribute_mapping)user;
					   if(tmp.testConstraints(null)) {
						   EEntity gamConstraints = tmp.getConstraints(null);
						   if(gamConstraints == constraint) {
							   tmp.setConstraints(null, rv);
						   }
					   }
				   }
               }
            }
			break;
			}
		}
		return rv;
	}
}

/* Available replacements.

application_context.description

application_context =
description_attribute_select description_attribute_select <-
description_attribute.described_item description_attribute
description_attribute.attribute_value

action.id

action = id_attribute_select
id_attribute_select <- description_attribute.described_item
description_attribute
description_attribute.attribute_value

action_assignment.role

action_assignment = role_attribute_select
item_with_role <- role_association.described_item
role_association
role_association.role

*/
