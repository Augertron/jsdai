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

import jsdai.SApplication_context_schema.CApplication_context;
import jsdai.SAssociative_draughting_elements_mim.CDraughting_model_item_association;
import jsdai.SAssociative_draughting_elements_mim.EDraughting_model_item_association;
import jsdai.SDraughting_element_schema.ADraughting_callout_element;
import jsdai.SDraughting_element_schema.CDraughting_callout;
import jsdai.SDraughting_element_schema.EDimension_curve;
import jsdai.SDraughting_element_schema.EDimension_curve_directed_callout;
import jsdai.SDraughting_element_schema.EDraughting_callout;
import jsdai.SDraughting_element_schema.ELeader_curve;
import jsdai.SDraughting_element_schema.ELeader_directed_callout;
import jsdai.SDraughting_element_schema.EProjection_curve;
import jsdai.SDraughting_element_schema.EProjection_directed_callout;
import jsdai.SFabrication_technology_xim.EStratum_technology_occurrence_armx;
import jsdai.SGeometric_tolerance_xim.EGeometric_tolerance_armx;
import jsdai.SLksoft_extensions_xim.EIgnorable_xim;
import jsdai.SPhysical_unit_design_view_xim.AAssembly_component_armx;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.CProduct_related_product_category;
import jsdai.SProperty_assignment_xim.EApplied_independent_property;
import jsdai.dictionary.AEntity_or_view_definition;
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

	// heal data, which is needed before running real conversion
	private static void healData(SdaiContext context, ASdaiModel allIncludedModels) throws SdaiException {
		AEntity dmias = allIncludedModels.getInstances(CDraughting_model_item_association.definition);
		// TODO - delete duplicates
//		List dmias2Delete = new ArrayList();
		for(int i=1,count=dmias.getMemberCount(); i<=count; i++){
			EDraughting_model_item_association edmia = (EDraughting_model_item_association)dmias.getByIndexEntity(i);
			if(edmia.testDefinition(null)){
				EEntity ee = edmia.getDefinition(null);
				if(ee instanceof EGeometric_tolerance_armx){
					EGeometric_tolerance_armx egta = (EGeometric_tolerance_armx)ee;
					EEntity appliedTo = egta.getApplied_to(null);
					edmia.setDefinition(null, appliedTo);
//					dmias2Delete.add(edmia);
				}
			}
		}
		healCallouts(context, allIncludedModels);
/*		for(int i=0,count=dmias2Delete.size(); i<count; i++){
			EDraughting_model_item_association edmia = (EDraughting_model_item_association)dmias2Delete.get(i);
			if(!edmia.isValid()){
				continue;
			}
			EEntity ee = edmia.getDefinition(null);
			ADraughting_model_item_association admias = new ADraughting_model_item_association();
			CDraughting_model_item_association.usedinDefinition(null, ee, allIncludedModels, admias);
			ERepresentation_item item = edmia.getIdentified_item(null);
			for(int j=1,count2=admias.getMemberCount(); j<=count2; j++){
				EDraughting_model_item_association edmia2 = (EDraughting_model_item_association)admias.getByIndexEntity(j);
				if(edmia == edmia2){
					continue;
				}
				if(edmia2.testIdentified_item(null)){
					if(edmia2.getIdentified_item(null) == item){
						System.err.println(" Deleting "+edmia2);
						edmia2.deleteApplicationInstance();
					}
				}
			}			
		}*/
	}

	private static void healCallouts(SdaiContext context, ASdaiModel allIncludedModels) throws SdaiException {
		AEntity callouts = allIncludedModels.getInstances(CDraughting_callout.definition);
		ESchema_definition schema = allIncludedModels.getByIndex(1).getUnderlyingSchema();
		// Leader
		final String leaderName = "leader_directed_callout";
		top: for(int i=1,count=callouts.getMemberCount(); i<=count; i++){
			EDraughting_callout edc = (EDraughting_callout)callouts.getByIndexEntity(i);
			if(edc.testContents(null)){
				ADraughting_callout_element adce = edc.getContents(null);
				for(int j=1,countE=adce.getMemberCount(); j<=countE; j++){
					EEntity ee = adce.getByIndex(j);
					if(ee instanceof ELeader_curve){
						if(edc instanceof ELeader_directed_callout){
							continue top;
						}
						createComplexIfPossible(context, schema, edc, leaderName);
						continue top;
					}
				}
			}
		}
		// Projection
		final String projectionName = "projection_directed_callout";
		top: for(int i=1,count=callouts.getMemberCount(); i<=count; i++){
			EDraughting_callout edc = (EDraughting_callout)callouts.getByIndexEntity(i);
			if(edc.testContents(null)){
				ADraughting_callout_element adce = edc.getContents(null);
				for(int j=1,countE=adce.getMemberCount(); j<=countE; j++){
					EEntity ee = adce.getByIndex(j);
					if(ee instanceof EProjection_curve){
						if(edc instanceof EProjection_directed_callout){
							continue top;
						}
						createComplexIfPossible(context, schema, edc, projectionName);
						continue top;
					}
				}
			}
		}
		// Dimension
		final String dimensionName = "dimension_directed_callout";
		top: for(int i=1,count=callouts.getMemberCount(); i<=count; i++){
			EDraughting_callout edc = (EDraughting_callout)callouts.getByIndexEntity(i);
			if(edc.testContents(null)){
				ADraughting_callout_element adce = edc.getContents(null);
				for(int j=1,countE=adce.getMemberCount(); j<=countE; j++){
					EEntity ee = adce.getByIndex(j);
					if(ee instanceof EDimension_curve){
						if(edc instanceof EDimension_curve_directed_callout){
							continue top;
						}
						createComplexIfPossible(context, schema, edc, dimensionName);
						continue top;
					}
				}
			}
		}
		
	}

	private static void createComplexIfPossible(SdaiContext context, ESchema_definition schema, EDraughting_callout edc, String leaderName){
		// Need to make complex
		try {
			EEntity_definition type = edc.getInstanceType();
			EEntity_definition newComplex = null;
			String name;
			if(type.getComplex(null)){
				AEntity_or_view_definition complexLeaves = type.getGeneric_supertypes(null);
				Collection complexNames = new TreeSet();
				complexNames.add(leaderName);
				for(SdaiIterator c = complexLeaves.createIterator(); c.next(); ) {
					EEntity_definition complexLeaf = (EEntity_definition) complexLeaves.getCurrentMember(c);
					complexNames.add(complexLeaf.getName(null));
				}
				StringBuffer complexNameBuffer = new StringBuffer();
				for(Iterator c = complexNames.iterator(); c.hasNext(); ) {
					complexNameBuffer.append(c.next()).append('+');
				}
				complexNameBuffer.setLength(complexNameBuffer.length() - 1);
				name = complexNameBuffer.toString();
			}else{
				name = type.getName(null);
				if(name.compareTo(leaderName) > 0){
					name = leaderName +"+"+ name; 
				}else{
					name = name +"+"+ leaderName;
				}
			}
			newComplex = LangUtils.findEntityDefinition(name, schema);
			if(newComplex == null){
				sessionLog(context, "Missing Complex " + name);
			}else{
				edc.findEntityInstanceSdaiModel().substituteInstance(edc, newComplex);
			}
		} catch (SdaiException e) {
			sessionLog(context, "Failed to create complex for " + edc+" with "+leaderName);
			e.printStackTrace();
		}
	}
	
	private static ArrayList firstRun(SdaiContext context, ASdaiModel allIncludedModels, SdaiModel modelAIM) throws SdaiException {
		if (modelAIM == null){
			modelAIM = context.domain.getByIndex(2);
		}
		healData(context, allIncludedModels);
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
		// the same with application_contexts
		AEntity contexts = allIncludedModels.getExactInstances(CApplication_context.definition);
		while (contexts.getMemberCount() > 0) {
			EEntity ee = contexts.getByIndexEntity(1);
			modelAIM.substituteInstance(ee);
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
				if(instanceExtended instanceof EIgnorable_xim){
					instanceExtended.deleteApplicationInstance();
				}else{
					sessionLog(context, "Missing Cx " + instanceExtended.getClass() + " within " + schemaName);
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
			String log = "Problems with createAimData for "+instance;
			sessionLog(context, log);
			exceptionWraper(context, ex);
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
					String log = "Problems with substituting instances: "+
						" oldType " + oldType + " to new type " + instanceExtended;
					sessionLog(context, log);
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
			if(!instanceExtended.isValid()){
				continue;
			}
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
				exceptionWraper(context, ex);
				String log = instanceExtended + " AIM1 " + newType + " " + instanceExtended.getClass() + " "
						+ instanceExtended.getTemp("AIM");
				sessionLog(context, log);
				EEntity_definition oldType = instanceExtended.getInstanceType();
				if (!oldType.isSubtypeOf(newType)){
					sessionLog(context, "");
					sessionLog(context, newType + " NOT SUB-AIM " + oldType);
				}
				AEntity result = new AEntity();
				if(!instanceExtended.isValid()){
					sessionLog(context, "");
					continue;
				}
				instanceExtended.findEntityInstanceUsers(context.domain, result);
				for (int i = 1; i <= result.getMemberCount(); i++) {
					sessionLog(context, " " + i + " : " + result.getByIndexEntity(i));
				}
				sessionLog(context, "");
			}
		}

		allInstancesBasedOnXIM.clear();
	}

	/**
	 * This method converts specified XIM population to MIM population in place.
	 */
	public void export(SdaiRepository repoXIM, ASdaiModel dataDomain, String schemaName,
			SdaiSession session) throws SdaiException {

		ASdaiModel domainAIM = new ASdaiModel();
		ESchema_definition schema = session.findSchemaDefinition(schemaName);
		// System.err.println(schemaName + " " + schema + " " + dataDomain.getMemberCount());
		SchemaInstance asi = repoXIM.createSchemaInstance("mim2xim", schema);
		SchemaInstance asiARM = repoXIM.createSchemaInstance("mim2xim-mim", schema);

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
		CxAP210ARMUtilities.mostlyUsedInstances.clear();
		// System.err.println(" CLEANING ");
	}

	// New version
	public void export(SdaiRepository repoXIM, ASdaiModel dataDomain, String stepFileName, String schemaName,
			SdaiSession session) throws SdaiException {

		export(repoXIM, dataDomain, schemaName, session);
		repoXIM.exportClearTextEncoding(stepFileName, stepFileName);
	}

	private static void sessionLog(SdaiContext context, String log){
		try{
			SdaiSession session = context.working_model.getRepository().getSession();
			session.printlnSession(log);
		}
		catch (SdaiException ex2) {
			System.err.println("Problems with logging in session ");
			System.err.println(log);
			ex2.printStackTrace();
		}
	}

	private static void exceptionWraper(SdaiContext context, Exception ex) {
		SdaiSession session;
		try{
			session = context.working_model.getRepository().getSession();
			session.printlnSession("  ");
			session.printlnSession("  "+ex.toString());
			StackTraceElement[] stes = ex.getStackTrace();
			for(int i=0;i<stes.length;i++){
				session.printlnSession("  "+stes[i].toString());
			}
			session.printlnSession("  ");
		}catch (Exception ex2) {
			System.err.println("Problems with logging in session ");
			ex2.printStackTrace();
			ex.printStackTrace();
		}
	}

}
