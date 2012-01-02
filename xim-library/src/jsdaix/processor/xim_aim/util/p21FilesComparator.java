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

package jsdaix.processor.xim_aim.util;

import java.lang.reflect.Field;
import java.util.*;

import com.lksoft.util.ArgumentsParser;

import jsdai.SAssembly_module_design_xim.EPackaged_component_join_terminal;
import jsdai.SFeature_and_connection_zone_xim.EShape_feature;
import jsdai.SFunctional_usage_view_xim.EFunctional_unit_usage_view_terminal_definition;
import jsdai.SLayered_interconnect_module_design_xim.EConductive_interconnect_element_terminal_link_armx;
import jsdai.SLayered_interconnect_module_design_xim.EInter_stratum_join_relationship;
import jsdai.SLayered_interconnect_module_design_xim.EIntra_stratum_join_relationship;
import jsdai.SNon_feature_shape_element_xim.ENon_feature_shape_element;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EExplicit_attribute;
import jsdai.lang.*;
import jsdai.util.LangUtils;

public final class p21FilesComparator extends JsdaiLangAccessor {

	private p21FilesComparator() { }

	private static SdaiRepository[] parseArgs(String[] args)throws SdaiException {
		ArgumentsParser.Value sourceArg = new ArgumentsParser.Value("-source", true);
		sourceArg.setValueName("source");
		sourceArg.setDescription("source file, usually original file before any conversions. Example: c:/data/sfm.210");

		ArgumentsParser.Value targetArg = new ArgumentsParser.Value("-target", true);
		targetArg.setValueName("target");
		targetArg.setDescription("Target file, usually after XIM-MIM-XIM conversion. Example: c:/data/sfm.stp_");

		List argList = Arrays.asList(new Object[] {sourceArg, targetArg});
		if (!ArgumentsParser.parse(args, argList)) {
			System.out.println(ArgumentsParser.getUsage(p21FilesComparator.class.getName(), argList));
			return null;
		}
		SdaiSession session = startSession();
		SdaiRepository[] repos = new SdaiRepository[2];
		repos[0] = session.importClearTextEncoding("", sourceArg.getValue(), null);
		repos[1] = session.importClearTextEncoding("", targetArg.getValue(), null);
		return repos;
	}

	public static void main(String[] args)
		throws SdaiException {
		boolean success = false;
		try {
			SdaiRepository[] repos = parseArgs(args);
			if(repos == null){
				return;
			}
			if(repos.length < 2){
				return;
			}
			if ((repos[0] == null)||(repos[1] == null)) {
				return;
			}
			compareRepositories(repos[0], repos[1]);
			success = true;
		} catch (SdaiException e) {
			e.printStackTrace();
		} finally {
			SdaiSession session = SdaiSession.getSession();
			if (session != null) {
				session.closeSession();
			}
		}

		if (success) {
			System.out.println("Success.");
		} else {
			System.out.println("Failed.");
		}
	}

	private static void compareRepositories(SdaiRepository repos1, SdaiRepository repos2)throws SdaiException {
		ASdaiModel models1 = repos1.getModels();
		SdaiIterator iter1 = models1.createIterator();
		while(iter1.next()){
			SdaiModel model1 = models1.getCurrentMember(iter1);
			AEntity instances1 = model1.getInstances();
			SdaiIterator iter11 = instances1.createIterator();
			while(iter11.next()){
				EEntity instance1 = instances1.getCurrentMemberEntity(iter11);
				String label = instance1.getPersistentLabel();
				String s1 = getString(instance1);
				try{
					EEntity instance2 = repos2.getSessionIdentifier(label);
					String s2 = getString(instance2);
					if(!s1.equals(s2)){
						if(!areInstancesTheSameIgnoringOrdering(instance1, instance2)){
							System.err.println(instance1);
							System.err.println("vs");
							System.err.println(instance2);
							System.err.println("---------");
						}
					}
					// System.err.println(" TO Delete "+instance2+" "+instance2.findEntityInstanceSdaiModel().getRepository().getSession());
					// instance2.deleteApplicationInstance();
				}
				catch(SdaiException exception){
					// Not nice to figure out the diffs by exception, but currently there is no faster way
					System.err.println(instance1);
					System.err.println(" was lost ");
					System.err.println("---------");
//					exception.printStackTrace();
//					System.err.println(exception);
				}
				// maybe it will make things faster...
				// instance1.deleteApplicationInstance();
			}
		}
		int count = getInstanceCount(repos2);
		if(count == 0){
			return;
		}
// Wait till Gintaras fix the bug
/*		System.err.println("=====================");
		System.err.println(" Garbage instances : ");
		ASdaiModel models2 = repos2.getModels();
		iter1 = models2.createIterator();
		while(iter1.next()){
			SdaiModel model1 = models2.getCurrentMember(iter1);
			AEntity instances1 = model1.getInstances();
			SdaiIterator iter11 = instances1.createIterator();
			while(iter11.next()){
				EEntity instance1 = instances1.getCurrentMemberEntity(iter11);
				System.err.println(" "+instance1);
			}
		}*/
	}

	private static String getString(EEntity instance)throws SdaiException {
		// For comparison we treat .U. the same as $ for shape_aspect
		try {
			if(instance instanceof EShape_aspect){
				EShape_aspect esa = (EShape_aspect)instance;
				if(esa.testProduct_definitional(null)){
					if(esa.getProduct_definitional(null) == ELogical.UNKNOWN){
						esa.unsetProduct_definitional(null);
					}
				}
			}
		// For regular application this is unacceptable, but here we simply avoid enumerating all the types having derived attribute
		} catch (SdaiException e) {
			if((e.getErrorId() != SdaiException.FN_NAVL)){
				e.printStackTrace();
			}
		}
		return instance.toString();
	}

	private static int getInstanceCount(SdaiRepository repos2)throws SdaiException {
		int count = 0;
		ASdaiModel models2 = repos2.getModels();
		SdaiIterator iter1 = models2.createIterator();
		while(iter1.next()){
			SdaiModel model1 = models2.getCurrentMember(iter1);
			AEntity instances1 = model1.getInstances();
			count += instances1.getMemberCount();
		}
		return count;
	}

	// Compare 2 instances ignoring internal ordering of values inside aggregates
	// Idea - attributes of type aggregate are unset (it they are equal) and than instances are compared again
	private static boolean areInstancesTheSameIgnoringOrdering(EEntity instance1, EEntity instance2)throws SdaiException {
		EEntity_definition type1 = instance1.getInstanceType();
		EEntity_definition type2 = instance2.getInstanceType();
		if(type1 != type2){
			return false;
		}
		EExplicit_attribute[] attributes = getEntityExplicitAttributes(type1);
		Field[] explicitAttributeFields = getEntityAttributeFields(type1);
		for (int i = 0; i < attributes.length; i++) {
			if(explicitAttributeFields[i] != null) {
				EExplicit_attribute attribute = attributes[i];
				EEntity domain = attribute.getDomain(null);
				if(domain instanceof AggregationType){
					if(instance1.testAttribute(attribute, null) == 0){
						continue;
					}
					if(instance2.testAttribute(attribute, null) == 0){
						continue;
					}
					AEntity value1 = (AEntity)instance1.get_object(attribute);
					Set set1 = getSetFromAEntity(value1);
					AEntity value2 = (AEntity)instance2.get_object(attribute);
					Set set2 = getSetFromAEntity(value2);
					if(set1.equals(set2)){
						instance1.unsetAttributeValue(attribute);
						instance2.unsetAttributeValue(attribute);
					}else{
						System.err.println(" aggreagte in problem "+attribute);
						return false;
					}
				}
			}
		}
		return (instance1.toString().equals(instance2.toString()));
	}

	private static Set getSetFromAEntity(AEntity input)throws SdaiException {
		HashSet set = new HashSet(input.getMemberCount());
		for(int i=1,count=input.getMemberCount(); i<=count; i++){
			set.add(input.getByIndexEntity(i).getPersistentLabel());
		}
		return set;
	}

	private static SdaiSession startSession() throws SdaiException {
		// General stuff later likely to be moved to other method
		SdaiSession session = SdaiSession.openSession();
		// For now process ARM only
		// General stuff
		session.startTransactionReadWriteAccess();
		return session;
	}

}
