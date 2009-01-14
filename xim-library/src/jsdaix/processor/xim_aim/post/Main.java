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

package jsdaix.processor.xim_aim.post;

import java.util.*;

import jsdai.SFabrication_technology_xim.EStratum_technology_occurrence_armx;
import jsdai.SLksoft_extensions_xim.ESequential_stratum_technology_occurrence_group_xim;
import jsdai.SPhysical_unit_design_view_xim.AAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.CProduct_related_product_category;
import jsdai.SProperty_assignment_xim.EApplied_independent_property;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASchemaInstance;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.libutil.XimEntityStandalone;
import jsdai.util.LangUtils;

/**
 * Class for any XIM model data export to appropriate AIM model.
 * 
 * @author Giedrius Liutkus
 * @version $Revision$
 */
public class Main {

	public static void export(SchemaInstance schemaInstance, SdaiContext context) throws SdaiException {
		ASdaiModel allIncludedModels = new ASdaiModel();
		gatherNestedSchemaModels(schemaInstance, allIncludedModels, new HashSet());
		
		ArrayList allXimEntities = firstRun(context, allIncludedModels, null);
		secondRun(context, allIncludedModels, null);
		thirdRun(context, null, allXimEntities);
		
//		export(context, allIncludedModels, null);
	}

//	public static void export(SdaiContext context, ASdaiModel allIncludedModels, SdaiModel modelAIM)
//			throws SdaiException {
//		if (modelAIM == null)
//			modelAIM = context.domain.getByIndex(2);
//		Object[] allOriginalInstances = LangUtils.aggregateToArray(allIncludedModels.getInstances());
//		AEntity allInstancesBasedOnXIM = new AEntity();
//		// System.err.println(" Domain "+context.domain.getMemberCount()+" 1
//		// "+context.domain.getByIndex(1).getInstanceCount()+" ModelCount
//		// "+allIncludedModels.getMemberCount());
//		// Specific exceptional case - need to move all categories to MIM module
//		// if they exist
//		// (because they are used both in mappings and in XIM)
//		AEntity categories = allIncludedModels.getExactInstances(CProduct_related_product_category.definition);
//		while (categories.getMemberCount() > 0) {
//			EEntity category = categories.getByIndexEntity(1);
//			modelAIM.substituteInstance(category);
//		}
//
//		for (int index = 0; index < allOriginalInstances.length; index++) {
//			EEntity instanceExtended = (EEntity) allOriginalInstances[index];
//			EEntity_definition oldType = instanceExtended.getInstanceType();
//			SdaiModel underlyingModel = oldType.findEntityInstanceSdaiModel();
//			String schemaName = underlyingModel.getDefinedSchema().getName(null);
//			// This is not good, as there are some schemas (at least
//			// mixed_complex_types) ending differently
//			// {
//			if (instanceExtended instanceof EMappedXIMEntity) {
//				EMappedXIMEntity instance = (EMappedXIMEntity) instanceExtended;
//				try {
//					;;System.out.println(instance + " create aim data");
//					instance.createAimData(context);
//				}
//				catch (SdaiException ex) {
//					ex.printStackTrace();
//				}
//				// Set link, so that we can faster make a downgrade to AIM type
//				// instanceExtended.setTemp(AIM, newType);
//				// Security check for instances which are deleted or substituted
//				if (instanceExtended.isValid()) {
//					allInstancesBasedOnXIM.addUnordered(instanceExtended);
//				}
//			} else if ((schemaName.substring(schemaName.length() - 3, schemaName.length()).equalsIgnoreCase("XIM"))) {
//				System.err.println("Missing Cx " + instanceExtended.getClass() + " within " + schemaName);
//			}
//			// }
//		}
//		// SECOND RUN
//		allOriginalInstances = LangUtils.aggregateToArray(allIncludedModels.getInstances());
//		for (int index = 0; index < allOriginalInstances.length; index++) {
//			EEntity instanceExtended = (EEntity) allOriginalInstances[index];
//			EEntity_definition oldType = instanceExtended.getInstanceType();
//			if (!(instanceExtended instanceof EMappedXIMEntity)) {
//				try {
////					;;System.out.println(instanceExtended + " substitute");
//					instanceExtended = modelAIM.substituteInstance(instanceExtended);
//				}
//				catch (SdaiException ex) {
//					ex.printStackTrace();
//					System.err.println(" oldType " + oldType + " instance " + instanceExtended);
//				}
//			}
//		}
//
//		// Only now we can downgrade to AIM type, when all ARM specific links
//		// are removed in Cx classes
//		SdaiIterator iter = allInstancesBasedOnXIM.createIterator();
//		while (iter.next()) {
//			EEntity instance = allInstancesBasedOnXIM.getCurrentMemberEntity(iter);
//			// Exceptions
//			// if(instance instanceof EShape_element)
//			// continue;
//			EMappedXIMEntity instanceExtended = (EMappedXIMEntity) instance;
//			EEntity_definition newType = (EEntity_definition) instanceExtended.getTemp("AIM");
//			try {
//				EEntity substitute = modelAIM.substituteInstance(instanceExtended, newType);
////				 ;;System.out.println("Substituting " + instanceExtended);
////				 ;;System.out.println("Result " + substitute);
//			}
//			catch (Exception ex) {
//				ex.printStackTrace();
//				System.err.println(instanceExtended + " AIM1 " + newType + " " + instanceExtended.getClass() + " "
//						+ instanceExtended.getTemp("AIM"));
//				EEntity_definition oldType = instanceExtended.getInstanceType();
//				if (!oldType.isSubtypeOf(newType))
//					System.err.println(newType + " NOT SUB-AIM " + oldType);
//				AEntity result = new AEntity();
//				instanceExtended.findEntityInstanceUsers(context.domain, result);
//				for (int i = 1; i <= result.getMemberCount(); i++) {
//					System.err.print(" " + i + " : " + result.getByIndexEntity(i));
//				}
//				System.err.println();
//			}
//		}
//
//		allInstancesBasedOnXIM.clear();
//	}

	/**
	 * @param modelAIM
	 * @param remainingACs
	 * @throws SdaiException
	 */
	/*
	 * We may need it in the future - keep it for now private static void
	 * processDelayedList(SdaiModel modelAIM, ASdaiModel domain, AEntity
	 * remainingACs) throws SdaiException { SdaiIterator iter; iter =
	 * remainingACs.createIterator(); EMappedXIMEntity instanceExtended = null;
	 * EEntity_definition newType = null; while(iter.next()) { try{ EEntity
	 * instance = remainingACs.getCurrentMemberEntity(iter); instanceExtended =
	 * (EMappedXIMEntity)instance; newType =
	 * (EEntity_definition)instanceExtended.getTemp("AIM"); EEntity substitute =
	 * modelAIM.substituteInstance(instanceExtended, newType); } catch(Exception
	 * ex){ ex.printStackTrace(); System.err.println(instanceExtended+" AIM1
	 * "+newType+" "+instanceExtended.getClass()+"
	 * "+instanceExtended.getTemp("AIM")); EEntity_definition oldType =
	 * instanceExtended.getInstanceType(); if (!oldType.isSubtypeOf(newType))
	 * System.err.println(newType+" NOT SUB-AIM "+oldType); AEntity result = new
	 * AEntity(); instanceExtended.findEntityInstanceUsers(domain, result);
	 * for(int i=1;i<=result.getMemberCount();i++){ System.err.print(" "+i+" :
	 * "+result.getByIndexEntity(i)); } System.err.println(); }
	 *  } }
	 */
	private static void gatherNestedSchemaModels(SchemaInstance schemaInstance, ASdaiModel domain,
			HashSet processedSchemas) throws SdaiException {
		if (processedSchemas.contains(schemaInstance)) {
			return;
		}
		processedSchemas.add(schemaInstance);
		ASdaiModel schemaModels = schemaInstance.getAssociatedModels();
		for (SdaiIterator itModels = schemaModels.createIterator(); itModels.next();) {
			SdaiModel model = (SdaiModel) schemaModels.getCurrentMemberObject(itModels);
			if (!domain.isMember(model)) {
				domain.addUnordered(model, null);
			}
			if (!model.getName().equalsIgnoreCase(schemaInstance.getName())) {
				ASchemaInstance schemaInstances = model.getAssociatedWith();
				for (SdaiIterator itShemas = schemaInstances.createIterator(); itShemas.next();) {
					SchemaInstance modelSchema = schemaInstances.getCurrentMember(itShemas);
					if (modelSchema.getName().equalsIgnoreCase(model.getName())) {
						gatherNestedSchemaModels(modelSchema, domain, processedSchemas);
						// break;
					}
				}
			}
		}
	}

	public static void main(String[] s) throws SdaiException {
		long start = System.currentTimeMillis();
		System.out.println(" Start conversion ");
		Main m = new Main();
		System.out.println(" ... ");
		// TEMPORARY TEST
		// m.convert(s);
		m.createANDexport(s);
		System.out.println(" Successfully finished ");
		System.out.println(" Total conversion time " + (System.currentTimeMillis() - start) / 1000 + " seconds ");
	}

	private void createANDexport(String[] s) throws SdaiException {
		// -- GENERIC Preparations --
		if (s.length < 3) {
			System.err
					.println(" utility usage: java jsdaix.processor.xim_aim.post.Main fileNameXIM fileNameAIM schemaNameAIM ");
			return;
		}
		String fileName = s[0];
		String stepFileName = s[1];
		String schemaName = s[2];
		SdaiSession session = SdaiSession.openSession();
		session.startTransactionReadWriteAccess();

		// createXIMTestData(modelAP210ARMExtended);
		// NOW Read real file
		SdaiRepository currentRepository = session.importClearTextEncoding("", fileName, null);
		// extract sdai models built on working schema. Build target domain
		ASdaiModel allModels = currentRepository.getModels();
		ASdaiModel dataDomain = new ASdaiModel();
		SdaiModel modelAP210ARMExtended = null;
		for (int index = 1, count = allModels.getMemberCount(); index <= count; index++) {
			SdaiModel model = allModels.getByIndex(index);
			// String schemaString = model.getUnderlyingSchemaString();
			// if (schemaString.equalsIgnoreCase(schemaName)) {
			dataDomain.addByIndex(index, model, null);
			modelAP210ARMExtended = model;
			// }
		}

		// set context
		SdaiContext context = new SdaiContext(dataDomain.getByIndex(1).getUnderlyingSchema(), dataDomain, dataDomain
				.getByIndex(1));
		session.setSdaiContext(context);

		// mainFrame.contextARM = new
		// SdaiContext(mainFrame.modelAP210ARM.getUnderlyingSchema(), null,
		// mainFrame.modelAP210ARM);
		context.mappedWorkingModel = modelAP210ARMExtended;

		export(currentRepository, dataDomain, stepFileName, schemaName, session);

		// session.closeSession();
	}
	
	private static ArrayList firstRun(SdaiContext context, ASdaiModel allIncludedModels, SdaiModel modelAIM) throws SdaiException {
		if (modelAIM == null)
			modelAIM = context.domain.getByIndex(2);
		Object[] allOriginalInstances = LangUtils.aggregateToArray(allIncludedModels.getInstances());
		ArrayList allInstancesBasedOnXIM = new ArrayList();
		// System.err.println(" Domain "+context.domain.getMemberCount()+" 1
		// "+context.domain.getByIndex(1).getInstanceCount()+" ModelCount
		// "+allIncludedModels.getMemberCount());
		// Specific exceptional case - need to move all categories to MIM module
		// if they exist
		// (because they are used both in mappings and in XIM)
		AEntity categories = allIncludedModels.getExactInstances(CProduct_related_product_category.definition);
		while (categories.getMemberCount() > 0) {
			EEntity category = categories.getByIndexEntity(1);
			modelAIM.substituteInstance(category);
		}
		// NAUO&co uses some stuff from Assembly_components, so assure that Cx for AC is invoked at the end of process
		AAssembly_component_armx aac = new AAssembly_component_armx(); 
		for (int index = 0; index < allOriginalInstances.length; index++) {
			EEntity instanceExtended = (EEntity) allOriginalInstances[index];
			EEntity_definition oldType = instanceExtended.getInstanceType();
			SdaiModel underlyingModel = oldType.findEntityInstanceSdaiModel();
			String schemaName = underlyingModel.getDefinedSchema().getName(null);
			if(instanceExtended instanceof EAssembly_component_armx){
				aac.addUnordered(instanceExtended);
				continue;
			}
			// This is not good, as there are some schemas (at least
			// mixed_complex_types) ending differently
			// {
			if (instanceExtended instanceof EMappedXIMEntity) {
				createAimData(context, allInstancesBasedOnXIM, instanceExtended);
				// WORKARROUND !!!
				if(instanceExtended instanceof EApplied_independent_property){
					EApplied_independent_property eaip = (EApplied_independent_property)instanceExtended;
					if(eaip.testDefinition(null)){
						if(eaip.getDefinition(null) instanceof EStratum_technology_occurrence_armx){
							eaip.unsetDefinition(null);
						}
					}
				}
			} else if ((schemaName.substring(schemaName.length() - 3, schemaName.length()).equalsIgnoreCase("XIM"))) {
				if(instanceExtended instanceof ESequential_stratum_technology_occurrence_group_xim){
					instanceExtended.deleteApplicationInstance();
				}else{
					System.err.println("Missing Cx " + instanceExtended.getClass() + " within " + schemaName);
				}
			}
		}
		for (int index=1,count=aac.getMemberCount(); index<=count ; index++) {
			EAssembly_component_armx instanceExtended = aac.getByIndex(index);
			createAimData(context, allInstancesBasedOnXIM, instanceExtended);
		}		
		
		return allInstancesBasedOnXIM;
	}

	private static void createAimData(SdaiContext context,
			ArrayList allInstancesBasedOnXIM, EEntity instanceExtended) {
		EMappedXIMEntity instance = (EMappedXIMEntity) instanceExtended;
		try {
			instance.createAimData(context);
		}
		catch (SdaiException ex) {
			ex.printStackTrace();
		}
		// Set link, so that we can faster make a downgrade to AIM type
		// instanceExtended.setTemp(AIM, newType);
		// Security check for instances which are deleted or substituted
		if (instanceExtended.isValid()) {
			allInstancesBasedOnXIM.add(instanceExtended);
		}
	}
	
	private static void secondRun(SdaiContext context, ASdaiModel allIncludedModels, SdaiModel modelAIM) throws SdaiException {
		//		 SECOND RUN
		if (modelAIM == null)
			modelAIM = context.domain.getByIndex(2);
		Object[] allOriginalInstances = LangUtils.aggregateToArray(allIncludedModels.getInstances());
		for (int index = 0; index < allOriginalInstances.length; index++) {
			EEntity instanceExtended = (EEntity) allOriginalInstances[index];
			EEntity_definition oldType = instanceExtended.getInstanceType();
			if (!(instanceExtended instanceof EMappedXIMEntity)) {
				try {
					instanceExtended = modelAIM.substituteInstance(instanceExtended);
				}
				catch (SdaiException ex) {
					ex.printStackTrace();
					System.err.println(" oldType " + oldType + " instance " + instanceExtended);
				}
			}
		}
	}
	
	private static void thirdRun(SdaiContext context, SdaiModel modelAIM, ArrayList allInstancesBasedOnXIM) throws SdaiException {
		Iterator iter = allInstancesBasedOnXIM.iterator();
		while (iter.hasNext()) {
			EEntity instance = (EEntity) iter.next();
			// Exceptions
			// if(instance instanceof EShape_element)
			// continue;
			EMappedXIMEntity instanceExtended = (EMappedXIMEntity) instance;
			EEntity_definition newType = (EEntity_definition) instanceExtended.getTemp("AIM");
			try {
				EEntity temporaryInstance = null;
				if(instanceExtended instanceof XimEntityStandalone){
					XimEntityStandalone ximInstance = (XimEntityStandalone)instanceExtended;
					temporaryInstance = ximInstance.getAimInstance(context);
					ximInstance.unsetAimInstance(context);
				}	
				AEntity instances = new AEntity();
				instance.findEntityInstanceUsers(context.domain, instances);
				
				// System.err.println(instanceExtended+" "+instanceExtended.getAllReferences()+" "+instances);
				EEntity substitute = modelAIM.substituteInstance(instanceExtended, newType);
				// this is needed in order to preserve instance numbers
				if(temporaryInstance != null){
					//substitute.moveUsersFrom(temporaryInstance);
					boolean successfulCopy = LangUtils.copyAttributes(temporaryInstance, substitute);
					// System.err.println(temporaryInstance+"->"+substitute+" "+successfulCopy);
					if(successfulCopy){
						temporaryInstance.deleteApplicationInstance();
					}
				}
				
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println(instanceExtended + " AIM1 " + newType + " " + instanceExtended.getClass() + " "
						+ instanceExtended.getTemp("AIM"));
				EEntity_definition oldType = instanceExtended.getInstanceType();
				if (!oldType.isSubtypeOf(newType))
					System.err.println(newType + " NOT SUB-AIM " + oldType);
				AEntity result = new AEntity();
				instanceExtended.findEntityInstanceUsers(context.domain, result);
				for (int i = 1; i <= result.getMemberCount(); i++) {
					System.err.print(" " + i + " : " + result.getByIndexEntity(i));
				}
				System.err.println();
			}
		}

		allInstancesBasedOnXIM.clear();
	}

	// New version
	public void export(SdaiRepository repoXIM, ASdaiModel dataDomain, String stepFileName, String schemaName,
			SdaiSession session) throws SdaiException {
		ASdaiModel domainAIM = new ASdaiModel();
		ESchema_definition schema = session.findSchemaDefinition(schemaName);
		// System.err.println(schemaName + " " + schema + " " + dataDomain.getMemberCount());
		SchemaInstance asi = repoXIM.createSchemaInstance(stepFileName, schema);
		SchemaInstance asiARM = repoXIM.createSchemaInstance(stepFileName + "-mim", schema);
		
		HashMap ximToAim = new HashMap(); 
		HashMap ximEntitiesToAim = new HashMap();
		
		ArrayList modelsXim = new ArrayList(); 
		for (int i = 1, count = dataDomain.getMemberCount(); i <= count; i++) {
			modelsXim.add(dataDomain.getByIndex(i));
		}
		
		for (int i = 0; i < modelsXim.size(); ++i) {
			SdaiModel modelXIM = (SdaiModel) modelsXim.get(i);
			String modelName = modelXIM.getName();

			SdaiModel modelAIM = repoXIM.createSdaiModel(modelName + "-MIM", schema);
			
			ximToAim.put(modelXIM, modelAIM);

			domainAIM.addByIndex(i + 1, modelAIM, null);
			asi.addSdaiModel(modelAIM);
			SdaiContext context = new SdaiContext(modelAIM.getUnderlyingSchema(), domainAIM, modelAIM);

			modelAIM.startReadWriteAccess();

			ASdaiModel domainARM = new ASdaiModel();
			domainARM.addByIndex(1, modelXIM, null);
			asiARM.addSdaiModel(modelXIM);
			
			ximEntitiesToAim.put(modelXIM, firstRun(context, domainARM, modelAIM));
			secondRun(context, domainARM, modelAIM);
		}
		

		for (int i = 0; i < modelsXim.size(); ++i) {
			SdaiModel modelXIM = (SdaiModel) modelsXim.get(i);
			String modelName = modelXIM.getName();
			
			SdaiModel modelAIM = (SdaiModel) ximToAim.get(modelXIM);
			
			SdaiContext context = new SdaiContext(modelAIM.getUnderlyingSchema(), domainAIM, modelAIM);
			
			thirdRun(context, modelAIM, (ArrayList) ximEntitiesToAim.get(modelXIM));
			

			// -- Wrap up --
			ASchemaInstance associatedSchemas = modelXIM.getAssociatedWith();
			for (SdaiIterator it = associatedSchemas.createIterator(); it.next();) {
				SchemaInstance schemaInstance = associatedSchemas.getCurrentMember(it);
				schemaInstance.setNativeSchema(schema);
				schemaInstance.addSdaiModel(modelAIM);
			}

			modelXIM.deleteSdaiModel();

			modelAIM.renameSdaiModel(modelName);
		}
		asi.delete();
		asiARM.delete();
		repoXIM.exportClearTextEncoding(stepFileName, stepFileName);
		CxAP210ARMUtilities.mostlyUsedInstances.clear();
		// System.err.println(" CLEANING ");
	}

}
