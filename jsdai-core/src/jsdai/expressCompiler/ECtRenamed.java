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

package jsdai.expressCompiler;

import java.io.*;
import java.util.*;
import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;

/*

  we we'll collect here occurences of RENAMED for an entity
  done in pass 3 only, so that there are no duplications
  for a single occurence of RENAMED, they will include
   - the new name (in RedeclaredAttribute)
   - the attribute, if available (in RedeclaredAttribute, returned by QualifiedAttribute)
   - ed2 (in QualifiedAttribute, returned by GroupQualifier
   -  

*/


public class ECtRenamed  {

	class  RenamedAttribute {
		String old_name;
		String new_name;
		EAttribute old_attribute = null;
		EAttribute new_attribute = null;
		EEntity_definition old_entity = null;
		EEntity_definition new_entity; // this feeld is not really needed because the whole ECtRenamed sits in THIS entity

	}

	boolean active;
	ArrayList renamed_attributes = null;
  RenamedAttribute renamed_attribute = null;
	Integer interfaced_flag = null;
	static boolean flag_print_warning = true;

	public ECtRenamed(EEntity_definition ed) {
		active = true;
		renamed_attribute = new RenamedAttribute();
		renamed_attribute.new_entity = ed; // this information is the same for each line, perhaps not really needed
		interfaced_flag = null;
//		System.out.println("<ECtRenamed> CONSTRUCTOR: " + ed);
	}

	public ECtRenamed(EEntity_definition ed, Integer flag) {
		active = true;
		renamed_attribute = new RenamedAttribute();
		renamed_attribute.new_entity = ed; // this information is the same for each line, perhaps not really needed
//		System.out.println("<ECtRenamed> CONSTRUCTOR: " + ed);
		interfaced_flag = flag;
	}

	public void newAttribute(EEntity_definition ed) {
		active = true;
		renamed_attribute = new RenamedAttribute();
		renamed_attribute.new_entity = ed;
	}


	public void add(String name) {
		if (renamed_attributes == null) {
			renamed_attributes = new ArrayList();
		}
		if (renamed_attribute != null) {
			renamed_attribute.new_name = name;
			renamed_attributes.add(renamed_attribute);
			// this thing moved to a separate method, no longer ready for the new attribute
			// we are ready, in the case there are more redeclared attributes in the same entity
      //			renamed_attribute = new RenamedAttribute();
			renamed_attribute = null;

		} else {
			System.out.println("<ECtRenamed> >INTERNAL ERROR - NULL< add: " + name);
		}
		active = false;
//		System.out.println("<ECtRenamed> add: " + name);
//		print();
	}

	public void addOldAttribute(EAttribute attr) {
		if (renamed_attribute != null) {
			renamed_attribute.old_attribute = attr;
		} else {
			System.out.println("<ECtRenamed> >INTERNAL ERROR - NULL< addOldAttribute: " + attr);
		}
//		System.out.println("<ECtRenamed> addOldAttribute: " + attr);
	}



	public void addNewAttribute(EAttribute attr) {
		if (renamed_attribute != null) {
			renamed_attribute.new_attribute = attr;
		} else {
			System.out.println("<ECtRenamed> >INTERNAL ERROR - NULL< addNewAttribute: " + attr);
		}
//		System.out.println("<ECtRenamed> addNewAttribute: " + attr);
	}

	public void addOld(EEntity_definition ed, String attr_name) {
		if (renamed_attribute != null) {
			renamed_attribute.old_entity = ed;
			renamed_attribute.old_name = attr_name;
		} else {
			System.out.println("<ECtRenamed> >INTERNAL ERROR - NULL< addOld: " + attr_name + ", ed: " + ed);
		}
//		System.out.println("<ECtRenamed> addOld: " + attr_name + ", ed: " + ed);
	}

	public void discard() {
		// no, we are ready, in the case there are more redeclared attributes in the same entity
		// renamed_attribute = null;
		renamed_attribute = new RenamedAttribute();
		active = false;
//		System.out.println("<ECtRenamed> discard");
	}
	
	
	public boolean isActive() {
//		System.out.println("<ECtRenamed> isActive: " + active);
		return active;
	}

	public void activate() {
		active = true;
//		System.out.println("<ECtRenamed> activate");
	}

	// may not be needed
	public void deactivate() {
		active = false;
//		System.out.println("<ECtRenamed> deactivate");
	}

  public static void resolve(SdaiRepository repository) throws SdaiException {
  	// let's go through all the entities, check if they have RENAMED to resolve and attempt to resolve them
  	// go  through RW models, go through all the entity definitions, check for presence of ECtRenamed in temp object,
  	// if found, go through all the lines and try to resolve

//System.out.println("=== RESOLVING attribute names in ECtRenamed ===");

    ASdaiModel models = repository.getModels();
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel sml = models.getCurrentMember(iter_model);

      String model_name = sml.getName();

			if (Support.skipModel(model_name)) {
				continue;
			}
      if (sml.getMode() == SdaiModel.READ_WRITE) {
				// do our thing

//		    EEntity_definition entities = models.getEntityExtentInstances(EEntity_definition.class);
		    AEntity_definition entities = (AEntity_definition)sml.getInstances(EEntity_definition.class);
    		SdaiIterator iter_entity = entities.createIterator(); 

    		while (iter_entity.next()) {
      		EEntity_definition entity = (EEntity_definition) entities.getCurrentMemberObject(iter_entity);

      		Object temp_object = entity.getTemp();
					if (temp_object != null) { 	
						if (temp_object instanceof ECtRenamed) {
							// ok, we have an entity with at least one attribute with RENAMED
							ECtRenamed current = (ECtRenamed)temp_object;
//System.out.println("found ECtRenamed object in the entity loop of resolve - entity: " + entity);
//current.print();
							if (current.renamed_attributes != null) {
								Iterator iter = current.renamed_attributes.iterator();
								while (iter.hasNext()) {
									RenamedAttribute rattr = (RenamedAttribute)iter.next();
									// ok, so we should have here the old name, the new name, the old/new entity references and (perhaps) attribute references
									// if one or both attribute references are not present, we will have to resolve them
									// if/when attribute references are present we could change the name to new in the new attribute
									if (rattr != null) {
//System.out.println("@@@ RENAMED attribute in entity loop:\n\told_name : " + rattr.old_name + "\n\tnew_name: " + rattr.new_name + "\n\told_entity: " + rattr.old_entity + "\n\tnew_entity: " + rattr.new_entity + "\n\told_attribute: " + rattr.old_attribute + "\n\tnew_attribute: " + rattr.new_attribute);										
										if (rattr.old_name != null) {
//System.out.println("<old_name NOT null>");
											if (rattr.new_name != null) {
//System.out.println("<new_name NOT null>");
												if (rattr.old_entity != null) {
//System.out.println("<old_entity NOT null>");
													if (rattr.new_entity != null) {
//System.out.println("<new_entity NOT null>");
														// if (false) { //X
														if ((rattr.old_attribute != null) && (rattr.new_attribute != null)) {
//System.out.println("<old_attribute & new_attribute NOT null>");
															// does not require resolving, just rename the attribute
															if (!rattr.new_attribute.getName(null).equalsIgnoreCase(rattr.new_name)) {
//System.out.println("RENAMING - attribute: " + rattr.new_attribute + ", new name: " + rattr.new_name);
																rattr.new_attribute.setName(null,rattr.new_name);
																// we might want to clear the temp object as well - no longer needed
																// we could check if it contains new_name, just interesting to see.
															rattr.new_attribute.setTemp(null);
															}
														} else {
															// at least one attribute is not resolved, so resolving is needed
															HashSet hs_entities = new HashSet();
															EEntity_definition current_e = rattr.new_entity;
															EEntity_definition start = rattr.new_entity;
															resolveAttributeRecursively(rattr, current_e, start, hs_entities, sml);
														
															// seems that after recursively resolving the renaming is not done in the recursive method, so repeat it here
															if ((rattr.old_attribute != null) && (rattr.new_attribute != null)) {
//System.out.println("<old_attribute & new_attribute NOT null 2>");
															// does not require resolving, just rename the attribute
																if (!rattr.new_attribute.getName(null).equalsIgnoreCase(rattr.new_name)) {
//System.out.println("RENAMING (AFTER) - attribute: " + rattr.new_attribute + ", new name: " + rattr.new_name);
																	rattr.new_attribute.setName(null,rattr.new_name);
																// we might want to clear the temp object as well - no longer needed
																// we could check if it contains new_name, just interesting to see.
																rattr.new_attribute.setTemp(null);
																}
															} else {
																// still not resolved?
																/*
																System.out.println("ECtRenamed - STILL NOT RESOLVED: ");
																System.out.println("\told name: " + rattr.old_name);
																System.out.println("\tnew name: " + rattr.new_name);
																System.out.println("\told entity: " + rattr.old_entity);
																System.out.println("\tnew entity: " + rattr.new_entity);
																System.out.println("\told attribute: " + rattr.old_attribute);
																System.out.println("\tnew attribute: " + rattr.new_attribute);
																*/
															}
														
														}
													} else {
														printWarning("<ECtRenamed><E1> resolve - new entity is NULL: " + entity 
																+ "\n\t\t new entity: " + rattr.new_entity 
																+ "\n\t\t old entity: " + rattr.old_entity 
																+ "\n\t\t old name: " + rattr.old_name
																+ "\n\t\t new name: " + rattr.new_name
																+ "\n\t\t old attribute: " + rattr.old_attribute
																+ "\n\t\t new attribute: " + rattr.new_attribute
															);
													}
												} else {
													printWarning("<ECtRenamed><E2> resolve - old entity is NULL: " + rattr);
												}
											} else {
												printWarning("<ECtRenamed><E3> resolve - new name is NULL: " + rattr);
											}	
										} else {
											printWarning("<ECtRenamed><E4> resolve - old name is NULL: " + rattr);
										}
									} else {
										// what the hell?!!!
										printWarning("<ECtRenamed><E5> resolve - RenamedAttribute is NULL: " + current);
									}
								}
						} else { // renamed_attributes is null
							// no renamed_attributes - so <RENAMED> was not present in redeclaring attributes, this is not an error, nothing to print here
							// it is done by design: ECtRenamed is created, a renamed_attribute is created for redeclared attribute, but discarded if RENAMED is not present, and renamed_attributes never created
							// printWarning("<ECtRenamed><E6> resolve - entity: " + entity + ", temp_object: " + temp_object);
						}
					} // temp_object is ECtRenamed
				} // temp_object not null

			} // while - entities

		 } // if RW
		} // while - models
		// we no longer need ECtRenamed in temp object, we could have deleted those instances one-by-one just after resolving, perhaps
		// but this way is safer and gives more flexibility if the implementation has to be tuned up.
		deleteECtRenamed(repository);
  
//System.out.println("=== END OF resolve ===");  
  }

	/*
		 to minimize conflicts with other temp object usage, we remove ECtRenamed instances from temp after the new names are resolved
		 and those instances are no longer needed.
		 this way, any usage of temp_object in later stages is not affected by ECtRenamed, and we convert back all the interfacing flags
		 that were used simultaneously with ECtRenamed
	*/
	static void deleteECtRenamed(SdaiRepository repository) throws SdaiException {

    ASdaiModel models = repository.getModels();
    SdaiIterator iter_model = models.createIterator();

    while (iter_model.next()) {
      SdaiModel sml = models.getCurrentMember(iter_model);

      String model_name = sml.getName();

			if (Support.skipModel(model_name)) {
				continue;
			}
      if (sml.getMode() == SdaiModel.READ_WRITE) {
				// do our thing

//		    EEntity_definition entities = models.getEntityExtentInstances(EEntity_definition.class);
		    AEntity_definition entities = (AEntity_definition)sml.getInstances(EEntity_definition.class);
    		SdaiIterator iter_entity = entities.createIterator(); 

    		while (iter_entity.next()) {
      		EEntity_definition entity = (EEntity_definition) entities.getCurrentMemberObject(iter_entity);
      		Object temp_object = entity.getTemp();
					if (temp_object != null) { 	
						if (temp_object instanceof ECtRenamed) {
							// ok, we have an entity with at least one attribute with RENAMED
							ECtRenamed current = (ECtRenamed)temp_object;
							if (current.interfaced_flag != null) {
								// replace temp_object by this flag
								entity.setTemp(current.interfaced_flag);
							} else {
								entity.setTemp(null);
							}
						} // if temp_object is ECtRenamed - else - nothing to do, it is not our business what is there
					} // if temp_object not null - else - nothing to do
				} // while - through entities
			} // if model is RW
		} // while - through models
	}

	static void resolveAttributeRecursively(RenamedAttribute rattr0, EEntity_definition current, EEntity_definition start, HashSet entities, SdaiModel sml) throws SdaiException {
		// at least one (old and/or new) attribute is not resolved here
		// a recursive or similar approach my be needed, because  the old attribute may also be renamed (but not yet resolved and really renamed)
		// alternative - to go through the entities in the main method resolve() in the order from supertypes to subtypes
		// that could be a simple solution, if we had entities in that order, if not, some sorting or recursion would be needed in that method
		
//System.out.println("--- Entering recursion:\n\tstart: " +  start + "\n\tcurrent: " + current + "\n\tattr: " + rattr0) ;
		
		
		// let's try here going to the supertypes first
    if (!entities.add(current)) {
	    // repeated inheritance
     return;
    }
    AEntity_or_view_definition asuper = current.getGeneric_supertypes(null);
    int count = asuper.getMemberCount();
    SdaiIterator isuper = asuper.createIterator();

    while (isuper.next()) {
      EEntity_definition ed = (EEntity_definition) asuper.getCurrentMemberObject(isuper);
			resolveAttributeRecursively(rattr0, ed, start, entities, sml);
		} // while
		// ok, here we do some real resolving, BTW, not sure why we even needed that ratr0 argument, and perhaps start as well

      		Object temp_object = current.getTemp();
					if (temp_object != null) { 	
						if (temp_object instanceof ECtRenamed) {
							// ok, we have an entity with at least one attribute with RENAMED
							ECtRenamed current_r = (ECtRenamed)temp_object;

//System.out.println(">>>>> PRINTING ECtRenamed in RECURSION, entity: " + current);
//current_r.print();            

							if (current_r.renamed_attributes != null) {
								Iterator iter = current_r.renamed_attributes.iterator();
								while (iter.hasNext()) {
									RenamedAttribute rattr = (RenamedAttribute)iter.next();
									// ok, so we should have here the old name, the new name, the old/new entity references and (perhaps) attribute references
									// if one or both attribute references are not present, we will have to resolve them
									// if/when attribute references are present we could change the name to new in the new attribute
									if (rattr != null) {
										if (rattr.old_name != null) {
											if (rattr.new_name != null) {
												if (rattr.old_entity != null) {
													if (rattr.new_entity != null) {
														//if (false) { //X
														if ((rattr.old_attribute != null) && (rattr.new_attribute != null)) {
															// does not require resolving, just rename the attribute
															if (!rattr.new_attribute.getName(null).equalsIgnoreCase(rattr.new_name)) {
//System.out.println("RENAMING (in RECURSION) - attribute: " + rattr.new_attribute + ", new name: " + rattr.new_name);
																rattr.new_attribute.setName(null,rattr.new_name);
																// we might want to clear the temp object as well - no longer needed
																// we could check if it contains new_name, just interesting to see.
																// rattr.new_attribute.setTemp(null);
															}
														} else {
															// at least one attribute is not resolved, so resolving is needed
															// this time - really resolve, because we are sure that the supertypes were already resolved
															resolveAttribute(rattr, sml);
														}
													} else {
														printWarning("<ECtRenamed><R1> resolve - new entity is NULL: " + current 
																+ "\n\t\t new entity: " + rattr.new_entity 
																+ "\n\t\t old entity: " + rattr.old_entity 
																+ "\n\t\t old name: " + rattr.old_name
																+ "\n\t\t new name: " + rattr.new_name
																+ "\n\t\t old attribute: " + rattr.old_attribute
																+ "\n\t\t new attribute: " + rattr.new_attribute
															);
													}
												} else {
													printWarning("<ECtRenamed><R2> resolve - old entity is NULL: " + rattr);
												}
											} else {
												printWarning("<ECtRenamed><R3> resolve - new name is NULL: " + rattr);
											}	
										} else {
											printWarning("<ECtRenamed><R4> resolve - old name is NULL: " + rattr);
										}
									} else {
										// what the hell?!!!
										printWarning("<ECtRenamed><R5> resolve - RenamedAttribute is NULL: " + current);
									}
								}
						} else {
							// renamed attributes is null
							// no renamed_attributes - so <RENAMED> was not present in redeclaring attributes, this is not an error, nothing to print here
							// it is done by design: ECtRenamed is created, a renamed_attribute is created for redeclared attribute, but discarded if RENAMED is not present, and renamed_attributes never created
							// printWarning("<ECtRenamed><R6> resolve - entity: " + current + ", temp_object: " + temp_object);
						}
					} // temp_object - ECtRenamed

		} // temp_obj not null

	}
	static void resolveAttribute(RenamedAttribute rattr, SdaiModel sml) throws SdaiException {
		// ok, so we are now sure that renamed attributes in supertype entities are already resolved
		// let's resolve this one
		// we know the new name and the entity in which it is introduced, the old entity and the old name, we need to find the old and the new attributes
		// let's go through all the attributes in the model, see if it belongs the old entity, and, if so, see if its name is old_name, if so - write it to old_attribute
		// now let's go through all the attributes in the model, see if it belongs to the new entity, if so, see if its name is new name or old name,
	  // if new name - write it to new_attribute, if old_name - add it to new attribute and replaced the name, and clear temp_obj of the new attribute
    AAttribute attributes = (AAttribute)sml.getInstances(EAttribute.class);
 		SdaiIterator iter_attribute = attributes.createIterator(); 

 		while (iter_attribute.next()) {
	 		EAttribute current_attribute = (EAttribute) attributes.getCurrentMemberObject(iter_attribute);
			EEntity_or_view_definition parent = current_attribute.getParent(null);
			if (rattr.old_entity == parent) {
				if (current_attribute.getName(null).equals(rattr.old_name)) {
					rattr.old_attribute = current_attribute;
					break;
				}
			}
		}	// while - attributes

 		iter_attribute = attributes.createIterator(); 
 		while (iter_attribute.next()) {
	 		EAttribute current_attribute = (EAttribute) attributes.getCurrentMemberObject(iter_attribute);
			EEntity_or_view_definition parent = current_attribute.getParent(null);
			if (rattr.new_entity == parent) {
				if (current_attribute.getName(null).equals(rattr.old_name)) {
					rattr.new_attribute = current_attribute;
					// perhaps we can leave the actual renaming to the parser's old implementation - it also check that there are no more than one attribute with the same name, etc.
					// rattr.new_attribute.setName(null,rattr.new_name);
     			// if so, perhaps we need to put the new name into temp_obj, instead of deleting it
     			// rattr.new_attribute.setTemp(null);
     			rattr.new_attribute.setTemp(rattr.new_name);
					
				} else 
				if (current_attribute.getName(null).equals(rattr.new_name)) {
					// nothing to do, well perhaps this:m
					rattr.new_attribute = current_attribute;
     			// do we want to test it first?
     			// rattr.new_attribute.setTemp(null);
				}
			}
		}	// while - attributes
	}
	
	static void printWarning(String msg) {
		if (flag_print_warning) {
			System.out.println(msg);
		}
	}


	public void print() {

		System.out.println("<ECtRenamed> PRINTING: ==============================");		
		if (renamed_attributes != null) {
			Iterator iter = renamed_attributes.iterator();
			while (iter.hasNext()) {
				RenamedAttribute rattr = (RenamedAttribute)iter.next();
				if (rattr != null) {
					System.out.println("\t<ECtRenamed> --- attribute --------------");		
					System.out.println("\t\t<ECtRenamed> old_name: " + rattr.old_name);	
					System.out.println("\t\t<ECtRenamed> new_name: " + rattr.new_name);	
					System.out.println("\t\t<ECtRenamed> old_attribute: " + rattr.old_attribute);	
					System.out.println("\t\t<ECtRenamed> new_attribute: " + rattr.new_attribute);	
					System.out.println("\t\t<ECtRenamed> old_entity: " + rattr.old_entity);	
					System.out.println("\t\t<ECtRenamed> new_entity: " + rattr.new_entity);	
				} else {
					System.out.println("\t<ECtRenamed> --- attribute NULL ---------");		
				}
			} 
		} else {
			if (renamed_attribute != null) {
				System.out.println("\t<ECtRenamed> SINGLE: ");	
				System.out.println("\t\t<ECtRenamed> old_name: " + renamed_attribute.old_name);	
				System.out.println("\t\t<ECtRenamed> new_name: " + renamed_attribute.new_name);	
				System.out.println("\t\t<ECtRenamed> old_attribute: " + renamed_attribute.old_attribute);	
				System.out.println("\t\t<ECtRenamed> new_attribute: " + renamed_attribute.new_attribute);	
				System.out.println("\t\t<ECtRenamed> old_entity: " + renamed_attribute.old_entity);	
				System.out.println("\t\t<ECtRenamed> new_entity: " + renamed_attribute.new_entity);	
					
			} else {
				System.out.println("\t<ECtRenamed> PRINTING: NOTHING to print!!!");		
			}
		}
		System.out.println("<ECtRenamed> PRINTING === END =======================");		
	}
	

}

