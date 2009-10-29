/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2009, LKSoftWare GmbH, Germany
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

package jsdaix.processor.xim_aim.pre.manual_arm_repair;


import jsdai.SGeometry_schema.AGeometric_representation_context;
import jsdai.SGeometry_schema.CGeometric_representation_context;
import jsdai.SGeometry_schema.EGeometric_representation_context;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.ARepresentation_relationship;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.CRepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_relationship;
import jsdai.SRepresentation_schema.ERepresentation_relationship_with_transformation;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * Sometimes the same Geoemtric_context is reused between 2 shape_representations, which are
 * directly or indirectly linked via representation_relationship(s) with transformation.
 * This is not allowed by STEP, and for one of the shape_reps (child) new context has to be created.
 * See also bug #3299 in bugzilla.
 */
public final class IncorrectReuseOfContextRepair {
	
	private IncorrectReuseOfContextRepair() { }
	
	public static void run(ASdaiModel models, Importer importer)
		throws SdaiException {
		// 1) Determine the situation - analyze each geometric_context and collect all the shape_reps pointing to it
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AGeometric_representation_context agrc = (AGeometric_representation_context) model.getInstances(CGeometric_representation_context.definition);
			for (SdaiIterator j = agrc.createIterator(); j.next();) {
				EGeometric_representation_context egrc = agrc.getCurrentMember(j);
				ARepresentation ar = new ARepresentation();
				CRepresentation.usedinContext_of_items(null, egrc, models, ar);
				for (SdaiIterator k = ar.createIterator(); k.next();) {
					ERepresentation er1 = ar.getCurrentMember(k);
					for (SdaiIterator l = ar.createIterator(); l.next();) {
						ERepresentation er2 = ar.getCurrentMember(l);
						// 2) Check pairwise
						makeNewContextIfNeeded(er1, er2, models, importer);
					}
				}
			}
		}
	}

	private static void makeNewContextIfNeeded(ERepresentation er1,
			ERepresentation er2, ASdaiModel models, Importer importer)throws SdaiException {
		if(er1 == er2){
			return;
		}
		// Check if there is some path between those 2 reps 
		if(isPathWithTransformation(er1, er2, models, importer)){
			ERepresentation_context erc = er1.getContext_of_items(null);
			ERepresentation_context erc2 = (ERepresentation_context)erc.copyApplicationInstance(erc.findEntityInstanceSdaiModel());
			er1.setContext_of_items(null, erc2);
			importer.logMessage(" Changing context for shape "+er1+" as it is reusing the same context as "+er2+" while beeing related to it with transformation ");
		}
	}
	
	private static boolean isPathWithTransformation(ERepresentation er1, ERepresentation er2, ASdaiModel models, Importer importer) throws SdaiException{
		ARepresentation_relationship arr = new ARepresentation_relationship();
		CRepresentation_relationship.usedinRep_1(null, er1, models, arr);
		boolean transformationFound = false;
		for (SdaiIterator i = arr.createIterator(); i.next();) {
			ERepresentation_relationship err = arr.getCurrentMember(i);
			if(err instanceof ERepresentation_relationship_with_transformation){
				transformationFound = true;
			}

      /*
				RR: VA_NSET exception on some older stp files, let's avoid it by catching exceptions and/or checking if the attribute value is null
				it is not possible to use test() method for all cases instead, because some attirbutes are redeclared as derived, 
				and absence of java inheritance does not allow to check with attributeRep_2() method if it is explicit without handling each subtype separately
				so catching exception is the simplest approach
				if null - this attribute was redeclared as derived - no need to report
				if VA_NSET exception - this is a mandatory explicit attribute with indeterminate value - reported
				if any other exception - reported (not occured so far and not expected to occur)
			*/
			
			try {	
		
				ERepresentation erx = err.getRep_2(null);

			  if (erx != null) {
			
					if(erx == er2){
						return transformationFound;
					}
					// Go recursively deeper
					if(isPathWithTransformation(erx, er2, models, importer)){
						return true;
					}
				} else {
					// this message is not needed, for derived null just do nothing, no need to report them
					// importer.errorMessage("Mandatory (derived?) attribute Rep_2 is null for " + err);
				}
			} catch (Exception e) {
        String e_message = e.getMessage();
        if (e_message.contains("VA_NSET")) {	
					importer.errorMessage(" Mandatory attribute Rep2 is unset for " + err);
				} else {
					importer.errorMessage(" ERROR - Exception " + e_message + " occured while reading attribute Rep_2  for instance: " + err);
				}	
			}
		}
		// If there is no path - return false
		return false;
	}

	private static boolean isPathWithTransformation_backup(ERepresentation er1, ERepresentation er2, ASdaiModel models, Importer importer) throws SdaiException{
		ARepresentation_relationship arr = new ARepresentation_relationship();
		CRepresentation_relationship.usedinRep_1(null, er1, models, arr);
		boolean transformationFound = false;
		for (SdaiIterator i = arr.createIterator(); i.next();) {
			ERepresentation_relationship err = arr.getCurrentMember(i);
			if(err instanceof ERepresentation_relationship_with_transformation){
				transformationFound = true;
			}

			importer.errorMessage("RR - processing instance: " + err);
			// RR: VA_NSET exception on some older stp files, let's avoid it by using test() - print an error message instead
//			if (((jsdai.SRepresentation_schema.CRepresentation_relationship)err).attributeRep_2(null) instanceof jsdai.SExtended_dictionary_schema.EExplicit_attribute) {
			if (err instanceof CRepresentation_relationship) {
				importer.errorMessage("RR - instance of CRepresentation_relationship ");
				if (((CRepresentation_relationship)err).attributeRep_2(null) instanceof jsdai.dictionary.EExplicit_attribute) {
					importer.errorMessage("RR - explicit attribute");
					if (err.testRep_2(null)) {
						importer.errorMessage("RR - attribute is set - OK");
						ERepresentation erx = err.getRep_2(null);
						if(erx == er2){
							return transformationFound;
						}
						// Go recursively deeper
						if(isPathWithTransformation(erx, er2, models, importer)){
							return true;
						}
		      } else {
						importer.errorMessage("Mandatory attribute Rep2 is unset for " + err);
    		  }
				} else { // RR - not explicit, so leave as it was
					importer.errorMessage("RR - NOT explicit attribute");
	
					ERepresentation erx = err.getRep_2(null);
					if(erx == er2){
						return transformationFound;
					}
					// Go recursively deeper
					if(isPathWithTransformation(erx, er2, models, importer)){
						return true;
					}
				}
  	  } else {
				importer.errorMessage("RR - NOT an instance of CRepresentation_relationship: " + err);
		
				ERepresentation erx = err.getRep_2(null);
				if(erx == er2){
					return transformationFound;
				}
				// Go recursively deeper
				if(isPathWithTransformation(erx, er2, models, importer)){
					return true;
				}
    	}

		}
		// If there is no path - return false
		return false;
	}

	
}
