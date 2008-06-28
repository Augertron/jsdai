// FindExplicitAttributes.java
// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty

// Simple SDAI application program.
// Demonstraits how to find explicit attributes of entity.

import java.io.*;
import jsdai.lang.*;
import jsdai.dictionary.*;
import java.util.*;

public class FindExplicitAttributes {
	public static void main(String args[]) throws SdaiException {
		EDefined_type types[] = new EDefined_type[50];
		if (args == null || args.length != 1) {
			System.out.println("usage: java FindExplicitAttributes inputFile");
			return;
		}
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction transaction = session.startTransactionReadWriteAccess();
		SdaiRepository repo = session.importClearTextEncoding("", args[0], null);	
		ASdaiModel models = repo.getModels();
		SdaiIterator i = models.createIterator();
		while (i.next()) {
			SdaiModel model = models.getCurrentMember(i);
			Aggregate instances = model.getInstances();
			SdaiIterator j = instances.createIterator();
			while (j.next()) {
				EEntity ins = (EEntity)instances.getCurrentMemberObject(j);
				EEntity_definition definition = ins.getInstanceType();
				Vector attributes = new Vector();
//				findAttributes(definition, attributes);
				HashSet entities = new HashSet();
				collectAttributes(definition, definition, attributes, entities);
				System.out.println(ins);
				System.out.println("	" + definition.getName(null));
				int n = attributes.size();
				for (int k = 0; k < n; k++) {
					EExplicit_attribute attribute = (EExplicit_attribute)attributes.elementAt(k);
					try {
						if (ins.testAttribute(attribute, types) != 0) {
							System.out.println("		" + attribute.getName(null) + " = " + ins.get_object(attribute));
						} else {
							System.out.println("		" + attribute.getName(null) + " = ?");
						}
					} catch (SdaiException ex) {
						if (ex.getErrorId() == SdaiException.AT_NDEF) {
							// current implementation of derived attributes just throws this SdaiException in set() and get() methods.
							System.out.println("		" + attribute.getName(null) + " - LATER REDECLARED IN SUBTYPE AS DERIVED"); 
						}	else {
							throw ex;
						}	
					}
				}	
			}
		}
	}
	
	public static void findAttributes(EEntity_definition entity, Vector v) throws SdaiException {
		AEntity_definition supertypes = entity.getSupertypes(null);
		SdaiIterator i = supertypes.createIterator();
		while (i.next()) {
			findAttributes(supertypes.getCurrentMember(i), v);
		}
		AExplicit_attribute attributes = entity.getExplicit_attributes(null);
		i = attributes.createIterator();
		while (i.next()) {
			if (!v.contains(attributes.getCurrentMember(i))) { // To avoid dublication of attributes for multiple inheritance with the same root.
				v.add(attributes.getCurrentMember(i));
			}
		}
	}

	// this is another implementation, handles repeated inheritance differently, also handles complex entities - no recursion for complex entities
	// it is also possible to write a more (less?) optimal implementation - no HashSet, no two entity parameters, etc., with the same functionality
	static void collectAttributes(EEntity_definition current, EEntity_definition start, Vector all_attributes, HashSet entities) throws SdaiException {
		if (!entities.add(current))	{
			// repeated inheritance - do nothing
			return;
		}
		boolean is_complex = start.getComplex(null);
		// for complex entities recursion is not needed, all its supertypes are arranged on one level
		if ((!is_complex) || (is_complex) && (current == start)) {
			AEntity_definition asuper = current.getSupertypes(null);
			SdaiIterator isuper = asuper.createIterator();
			while (isuper.next()) {
				EEntity_definition eds = (EEntity_definition)asuper.getCurrentMemberObject(isuper);
				collectAttributes(eds, start, all_attributes, entities);
			}
		}
		AExplicit_attribute xa = current.getExplicit_attributes(null);
		SdaiIterator ia  = xa.createIterator();
		while (ia.next()) {
			EExplicit_attribute xattr = (EExplicit_attribute)xa.getCurrentMemberObject(ia);
			all_attributes.add(xattr);
		}
	}


}

