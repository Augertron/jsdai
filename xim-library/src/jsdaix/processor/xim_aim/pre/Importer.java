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

package jsdaix.processor.xim_aim.pre;


import java.io.PrintStream;

import jsdai.SAction_schema.CAction_method_relationship;
import jsdai.SApplication_context_schema.CProduct_context;
import jsdai.SApproval_schema.CApproval_role;
import jsdai.SBasic_attribute_schema.CDescription_attribute;
import jsdai.SBasic_attribute_schema.CName_attribute;
import jsdai.SBasic_attribute_schema.CObject_role;
import jsdai.SBasic_attribute_schema.CRole_association;
import jsdai.SDocument_assignment_mim.CDocument_product_equivalence;
import jsdai.SDocument_schema.CDocument;
import jsdai.SDocument_schema.CDocument_type;
import jsdai.SGeometric_tolerance_xim.AGeometric_tolerance_armx;
import jsdai.SGeometric_tolerance_xim.CGeometric_tolerance_armx;
import jsdai.SGeometric_tolerance_xim.EGeometric_tolerance_armx;
import jsdai.SGroup_mim.CApplied_group_assignment;
import jsdai.SIdentification_assignment_mim.CApplied_identification_assignment;
// import jsdai.SManagement_resources_schema.CClassification_role;
import jsdai.SManagement_resources_schema.CIdentification_role;
import jsdai.SManagement_resources_schema.COrganizational_project_role;
import jsdai.SManagement_resources_schema.CPosition_in_organization_role;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMethod_definition_schema.CSequential_method;
import jsdai.SMixed_complex_types.CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context;
import jsdai.SRepresentation_schema.CGlobal_uncertainty_assigned_context$parametric_representation_context;
import jsdai.SProduct_definition_schema.CProduct_definition_context_association;
import jsdai.SProduct_definition_schema.CProduct_definition_context_role;
import jsdai.SProduct_definition_schema.CProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.CGeneral_property_association;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CShape_definition_representation;
import jsdai.SProduct_structure_schema.CProduct_definition_usage;
import jsdai.SQualified_measure_schema.CType_qualifier;
import jsdai.SRepresentation_schema.CItem_defined_transformation;
import jsdai.SRepresentation_schema.CParametric_representation_context;
import jsdai.SRepresentation_schema.CRepresentation_item;
import jsdai.SRequirement_assignment_mim.CAssigned_requirement;
import jsdai.SRequirement_assignment_mim.CRequirement_assigned_object;
import jsdai.SShape_aspect_definition_schema.CShape_aspect_deriving_relationship;
import jsdai.SShape_aspect_definition_schema.CShape_representation_with_parameters;
import jsdai.SShape_dimension_schema.CDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CShape_dimension_representation;
import jsdai.SShape_parameters_mim.CKeepout_design_object_category;
import jsdai.SShape_tolerance_schema.AGeometric_tolerance_with_modifiers;
import jsdai.SShape_tolerance_schema.CGeometric_tolerance_relationship;
import jsdai.SShape_tolerance_schema.CGeometric_tolerance_with_modifiers;
import jsdai.SShape_tolerance_schema.CTolerance_zone;
import jsdai.SShape_tolerance_schema.CTolerance_zone_form;
import jsdai.SShape_tolerance_schema.EGeometric_tolerance_with_modifiers;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.ASdaiModel;
import jsdai.lang.A_enumeration;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.AP203SpecificRepair;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.Ap203ProductCategoryRepair;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.DocumentRepair;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.Item_defined_transformationRepair;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.ProductCategoryFlattener;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.RolesRepair;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.StylingModelRepair;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.TargetDiameterFixer;
import jsdaix.processor.xim_aim.pre.manual_aim_repair.ValueRangeFixer;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.AIMGarbageCleaner;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.AP210SpecificGarbageCleaner;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.AssemblyRepair;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.Closed_curveFixer;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.DatumTargetFixer;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.DefinitionsRepair;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.FakedMappingCleaner;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.GDTRepair;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.IncorrectReuseOfContextRepair;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.MRIDowngrader;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.PartsRepair;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.RepresentationRelationshipRepair;
import jsdaix.processor.xim_aim.pre.manual_arm_repair.ShapeAspectRelationshipCleaner;


// RR
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import jsdai.util.UtilMonitor;

/**
 * Author Giedrius Liutkus
 *
 * Main class for import
 * The import stuff is divided into 3 actions
 * 1. Import MIM data, fix it manually.
 * 2. Run automatic mapping to upgrade input schema to XIM
 * 3. Remove redundant MIM entities
 * 4. Implement manual XIM entities fix.
 *
 */
public class Importer {


  // RR - perhaps don't have to be here (or at all - for debugging for now, but perhaps will be used permanently)
	static PrintStream pout, perr;
	static BufferedOutputStream bout, berr;
	static FileOutputStream fout, ferr;
  static boolean flag_from_initAsRunnable = false;


	/**
	 *	Log stream
	 */
	protected PrintStream outputLog = null;

	public static final String messagePrefix = "MIM2XIM Healing:";

	SdaiSession session;

	public void logMessage(String message)throws SdaiException{

		if (flag_from_initAsRunnable) {
			System.out.println(message);
		} else {
			session.printlnSession(message);
		}
	}

	public void errorMessage(String message)throws SdaiException{
		if (flag_from_initAsRunnable) {
			System.err.println(messagePrefix+message);
		} else {
			session.printlnSession(messagePrefix+message);
		}
	}

	private final EEntity_definition[] typesToRemove = {
			// CRepresentation_relationship.definition,
			CProduct_definition_relationship.definition,
			// After removal of Shape_element this is likely no longer have to be removed
			// CShape_aspect.definition,
			CProperty_definition_representation.definition,
			CProperty_definition_relationship.definition,
			//water_dispenser CRepresentation_map.definition,
			CProperty_definition.definition,
			CRepresentation_item.definition, //??
			CProduct_definition_context_role.definition, // it participates only in the middle of some mapping paths and there's no XIM entity mapping to it
//			COrganization_role.definition,
			//wd CDate_role.definition,
	//				CRepresentation.definition,
			CAssigned_requirement.definition, // ok - as nothing maps to it and it does not substitute anything
			CRequirement_assigned_object.definition, // ok - as nothing maps to it and it does not substitute anything
	//				CProduct_related_product_category.definition,
			CApplied_group_assignment.definition,
			CParametric_representation_context.definition,
			CGlobal_uncertainty_assigned_context$global_unit_assigned_context$parametric_representation_context.definition,
			CGlobal_uncertainty_assigned_context$parametric_representation_context.definition,
			CProduct_definition_usage.definition,
			// After removal of Item_shape this is likely no longer have to be removed
			// CProduct_definition_shape.definition,
			// CProduct_definition_context.definition, // Have it here, because we add some constraints in the mapping of product_definition_context and if it is not satisfied - simply we need to ignore it - more information at SEDS-1491.
			CProduct_definition_context_association.definition,
			CProduct_context.definition, // No entity in stepmod mapps to this entity.
			COrganizational_project_role.definition, // Needed by AP233
			CAction_method_relationship.definition, // Needed by AP233
			CDocument_type.definition,
			CDocument.definition,
			CType_qualifier.definition,
			CDocument_product_equivalence.definition,
			// At least in AP210 context it is impossible to distinguish this from garbage
	//				CItem_shape.definition,
			CItem_defined_transformation.definition,
	//IDA		CShape_element.definition,
	//IDA		CShape_representation.definition,
			CRole_association.definition,
			CObject_role.definition,
			CApproval_role.definition,
			CGeneral_property_association.definition,

			// Cpart_view_definition.definition,

			// Types, which are most likely dependent and should be copied only if there are XIM users.
			// For now - simply not copy them
			CDescription_attribute.definition,
			// After we have id on shape_aspect and product_definition_shape set via derived attributes, we no longer can delete this
			// CId_attribute.definition,
			CName_attribute.definition,
	//				CDescriptive_representation_item.definition,
			CKeepout_design_object_category.definition,
			//water_dispenser CCharacterized_object.definition, // used as "middle" entity in some mappings
			// IDA STEP specific
			CSequential_method.definition,
			CPosition_in_organization_role.definition,
			CShape_definition_representation.definition,
			CShape_aspect_deriving_relationship.definition,
			CDimensional_characteristic_representation.definition,
			CShape_dimension_representation.definition,
			CTolerance_zone_form.definition,
//			CTolerance_zone.definition,
			CGeometric_tolerance_relationship.definition,
			CIdentification_role.definition,
			CShape_representation_with_parameters.definition,
			CApplied_identification_assignment.definition
			// CPerson_and_organization_role.definition,
	};

	/**
	 *	Set the log stream to other. By default it is System.out
	 */
	public void setOutputLog(PrintStream stream) {
		this.outputLog = stream;
	}

	public void setLocation(String location) {
	}


	public void output(String msg) {
		if (outputLog != null) {
			outputLog.println(msg);
		}
	}

	///////////////////////////////////////////////////////////
	// Stuff basically needed, because this is standalone application
	///////////////////////////////////////////////////////////

	public static void main(String[] args) throws SdaiException{
		long time = System.currentTimeMillis();
		String stepFileName = args[0];
		SdaiSession session = SdaiSession.openSession();
		Importer importer = new Importer();
		SdaiTransaction transaction = importer.startTransactionReadWriteAccess(session);

		SdaiRepository repo = session.importClearTextEncoding("", stepFileName, null);
// RR - moving errorMesssage down after runImport() so that session is set correctly before errorMessage() is used
//    importer.errorMessage(" Importing time is "+(System.currentTimeMillis()-time)/1000+" seconds");
    long time2 = System.currentTimeMillis();
		importer.runImport(repo);
    importer.errorMessage(" Importing time is "+(time2-time)/1000+" seconds");
		importer.errorMessage(" Time after processing is "+(System.currentTimeMillis()-time)/1000+" seconds");
		transaction.commit();
		// -- Wrap up --
		repo.exportClearTextEncoding(stepFileName+"_");
		importer.errorMessage(" Overall conversion time is "+(System.currentTimeMillis()-time)/1000+" seconds");
	}

	public void runImport(SdaiRepository inputRepo) throws SdaiException {
		if (inputRepo == null) {
			throw new SdaiException(SdaiException.FN_NAVL, "No source data found!");
		}

		runImport(inputRepo.getModels());
	}

	public void runImport(SchemaInstance schema)throws SdaiException{
		if (schema == null) {
			throw new SdaiException(SdaiException.FN_NAVL, "No source data found!");
		}

		runImport(schema.getAssociatedModels());
	}

	public void runImport(ASdaiModel models)throws SdaiException{
		if (models == null || models.getMemberCount() == 0) {
			throw new SdaiException(SdaiException.FN_NAVL, "No source data found!");
		}
		SdaiModel model = models.getByIndex(1);
		SdaiContext context = new SdaiContext(model);
		context.working_modelAggr = models;
		context.working_model = null;
		session = model.getRepository().getSession();
		session.setSdaiContext(context);
		DocumentRepair.run(models, this);

		// modifications and fixes before automatic mapping
		AP203SpecificRepair.run(models, this);
		// this fixer must be ran after AP203SpecificRepair,
		// because it does not "know" about AP203 specific AOs
		RolesRepair.run(models, this);
		ProductCategoryFlattener.run(models, this);
		// this fixer must be ran after ProductCategoryFlattener,
		// because some AP203 designs have part category indirectly
		// assgined to product. Since ProductCategoryFlattener
		// makes this part category directly assigned to the product,
		// Ap203ProductCategoryRepair does not need to repair anything
		// in this case.
		Ap203ProductCategoryRepair.run(models, this);
		Item_defined_transformationRepair.run(models, this);
		ValueRangeFixer.run(models, this);
		StylingModelRepair.run(models, this);
		TargetDiameterFixer.run(models, this);
		Map instances2Values = preProcessModifiers(models);

		// automatic mapping launch
		AutomaticXimPopulationCreator ximPopulationCreator = new AutomaticXimPopulationCreator(context, this);
//		long timePure = System.currentTimeMillis();
		// -> AIMGarbageCleaner.run(models, typesToRemove);

		try {
			SdaiSession.convertMapping(ximPopulationCreator.getMappingContext());
		} catch(SdaiException ex){
			ex.printStackTrace();
//			SdaiSession session = model.getRepository().getSession();
			logMessage("--------");
			logMessage(ex.getMessage());
			logMessage("--------");
		} catch (Throwable tt) { //RR - there was a case with class definition not found and I was left completely in the dark without this
			tt.printStackTrace();
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR, tt.getMessage()).initCause(tt);
		}
		
		postProcessModifiers(models, instances2Values);
		
		GDTRepair.run(context, models, this);
		//System.out.println(" Pure mapping operations time is "+(System.currentTimeMillis()-timePure)/1000+" seconds"+inputRepo.getSessionIdentifier("#436"));
		FakedMappingCleaner.run(models, ximPopulationCreator, this);
		// For now we do not really need to check the original schema as we are checking
		// for specific patterns, which happens in AP210 schema only anyway
		AP210SpecificGarbageCleaner.run(models, this);
		AIMGarbageCleaner.run(models, typesToRemove, this);
		MRIDowngrader.run(models, this);
		Closed_curveFixer.run(models, this);
		ShapeAspectRelationshipCleaner.run(models, this);
		// System.out.println(" Cleaning E "+(System.currentTimeMillis()-timePure)/1000+" seconds");
		// RepItemsInRepCleaner.run(repoAP210AIM);

		// the order of following two repairs used to be different,
		// but per test case sbm_Schrumpfverp_08, it seems that
		// this new order is correct. First of all PartsRepair
		// changes some products to parts and only after that
		// DefinitionsRepair can change some product_view_definitions
		// to part_view_definition
		PartsRepair.run(models, this);
		// System.out.println(" Cleaning F "+(System.currentTimeMillis()-timePure)/1000+" seconds"+inputRepo.getSessionIdentifier("#436"));
		DefinitionsRepair.run(models, this);
		AssemblyRepair.run(models, this);
		RepresentationRelationshipRepair.run(models, this);
		IncorrectReuseOfContextRepair.run(models, this);
		StylingModelRepair.runAfterMapping(models, this);
		DatumTargetFixer.run(models, this);
		// System.out.println(" mapping operations + POST processing time is "+(System.currentTimeMillis()-timePure)/1000+" seconds"+inputRepo.getSessionIdentifier("#436"));
		// repoAP210Extended.deleteRepository();
		// session.closeSession();
	}

	private void postProcessModifiers(ASdaiModel models, Map instances2Values) throws SdaiException {
		AGeometric_tolerance_armx agt = (AGeometric_tolerance_armx)models.getInstances(CGeometric_tolerance_armx.definition);
		for (SdaiIterator j = agt.createIterator(); j.next();) {
			EGeometric_tolerance_armx egt = agt.getCurrentMember(j);
			int[] values = (int[])instances2Values.get(egt.getPersistentLabel());
			if(values != null){
				A_enumeration enums = egt.createModification_new(null);
				for(int i=0;i<values.length;i++){
					enums.addUnordered(values[i]);
				}
			}
		}
	}

	private Map preProcessModifiers(ASdaiModel models) throws SdaiException {
		Map instances2Values = new HashMap(); 
		AGeometric_tolerance_with_modifiers agtwm = (AGeometric_tolerance_with_modifiers)models.getInstances(CGeometric_tolerance_with_modifiers.definition);
		for (SdaiIterator j = agtwm.createIterator(); j.next();) {
			EGeometric_tolerance_with_modifiers egtwm = agtwm.getCurrentMember(j);
			A_enumeration ae = egtwm.getModifiers(null);
			int[] values = new int[ae.getMemberCount()];
			for(int i=1;i<=values.length;i++){
				values[i-1] = ae.getByIndex(i);
			}
			instances2Values.put(egtwm.getPersistentLabel(), values);
		}
		return instances2Values;
	}

	private SdaiTransaction startTransactionReadWriteAccess(SdaiSession session) throws SdaiException
	{
		if(session.testActiveTransaction()){
			SdaiTransaction transaction = session.getActiveTransaction();
			if(transaction.getMode() == SdaiTransaction.READ_ONLY){
				transaction.endTransactionAccessAbort();
				transaction = session.startTransactionReadWriteAccess();
			}
			return transaction;
		} else
			return session.startTransactionReadWriteAccess();
	}



  	public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args, final UtilMonitor monitor) throws jsdai.lang.SdaiException {
  		Properties jsdaiProperties = new Properties();
  		jsdaiProperties.setProperty("repositories", sdaireposDirectory);

  		// jsdaiProperties.setProperty("jsdai.SIda_step_schema_xim","AC*;AI*;AP*;ASS*;AU*;B*;C*;D*;E*;F*;G*;H*;IDA_STEP_AIM*;ISO*;IN*;J*;K*;L*;M*;N*;O*;P*;Q*;R*;S*;T*;U*;V*;W*;X*;Y*;Z*;");

	  	jsdaiProperties.setProperty("mapping.schema.IDA_STEP_SCHEMA_XIM","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
  		jsdaiProperties.setProperty("mapping.schema.IDA_STEP_AIM_SCHEMA","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
			jsdaiProperties.setProperty("mapping.schema.AUTOMOTIVE_DESIGN","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
			jsdaiProperties.setProperty("jsdai.SIda_step_schema_xim","AC*;AI*;AP*;ASS*;AU*;B*;C*;D*;E*;F*;G*;H*;IDA_STEP_AIM*;ISO*;IN*;J*;K*;L*;M*;N*;O*;P*;Q*;R*;S*;T*;U*;V*;W*;X*;Y*;Z*;");


  		SdaiSession.setSessionProperties(jsdaiProperties);
  		return new Runnable() {
  			public void run() {
  				// main(args, null);
  				try {

	  				flag_from_initAsRunnable = true;

						fout = new FileOutputStream(args[0]+"_log_conversion");
						bout = new BufferedOutputStream(fout);
						pout = new PrintStream(bout);
						System.setOut(pout);

						ferr = new FileOutputStream(args[0]+"_log_conversion_err");
						berr = new BufferedOutputStream(ferr);
						perr = new PrintStream(berr);
						System.setErr(perr);

						System.out.println("Conversion: invoking main: " + args);
  					main(args);
						System.out.println("Conversion: main ended ");
					} catch (Exception eee) {

						System.out.println("Conversion: exception occured: " + eee);

						eee.printStackTrace();

        	} finally {

	  				flag_from_initAsRunnable = false;

						pout.flush();
						pout.close();

						perr.flush();
						perr.close();

					}
  			}
  		};
  	}


}
