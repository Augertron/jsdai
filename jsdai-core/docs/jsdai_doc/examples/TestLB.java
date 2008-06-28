// TestLB.java v. 1.2 2001-03-16
// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty
//
// To run:
// java TestLB data arm_schema_name aim_entity_class_name arm_entity_name
// Where:
// 	data - clear text encoding file (according Part 21) with AIM instances.
// 	arm_schema_name - name of ARM schema according to which input_file will be analyzed.
//      aim_entity_class_name - java class full name of AIM entity
//      arm_entity_name - ARM entity name
// Example: java TestLB ../part21-files/ap210_pcb1.stp AP210_ARM jsdai.SProduct_definition_schema.EProduct ee_product

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import jsdai.util.LangUtils;

public class TestLB {
    public static final void main(String args[]) throws SdaiException, ClassNotFoundException {
        if (args.length < 4) {
            System.out.println("Usage of program: java TestLB data arm_schema_name aim_entity_class_name arm_entity_name");
            System.out.println("Example:");
            System.out.println("  java TestLB ../part21-files/ap210_pcb1.stp AP210_ARM jsdai.SProduct_definition_schema.EProduct ee_product");
            return;
        }
        SdaiSession session = jsdai.lang.SdaiSession.openSession();
        SdaiTransaction trans = session.startTransactionReadWriteAccess();
        
        String schemaName = null;
        SchemaInstance dictionaryData = null;
        boolean modelFound = false;
        
        SdaiModel dataModel = null;
        SdaiRepository dataRep = session.importClearTextEncoding("", args[0], null);
        ASdaiModel dataSi = dataRep.getModels();
        ASdaiModel set = dataRep.getModels();
        SdaiIterator iter = set.createIterator();
        modelFound = false;
        while (iter.next()) {
            dataModel = set.getCurrentMember(iter);
            modelFound = true;
            break;
        }
        if (!modelFound) {
            System.out.println("No application data found.");
            return;
        }
        
        dictionaryData = session.getDataDictionary();
        ASdaiModel dictionaryModels = dictionaryData.getRepository().getModels();
        modelFound = false;
        SdaiModel dictionaryModel = LangUtils.findDictionaryModel(args[1]);
        if (dictionaryModel == null) {
            System.out.println("No dictionary data found.");
            return;
        }
        SdaiModel mappingModel = LangUtils.findMappingModel(args[1]);
        if (mappingModel == null) {
            System.out.println("No mapping data found.");
            return;
        }
        mappingModel.startReadOnlyAccess();
        //dictionaryModel.startReadOnlyAccess();
        ASdaiModel mappingSi = session.getDataMapping().getAssociatedModels();
        
        dictionaryModel.startReadOnlyAccess();
        
        ESchema_definition armSchema = LangUtils.findArmSchema(dictionaryModel);
        EEntity_definition entities[] = LangUtils.getEntitiesOfSchema(armSchema);
        EEntity_definition armEntity = LangUtils.findEntityDefinition(args[3], entities);
        if (armEntity == null) {
            System.out.println("No arm entity found.");
            return;
        }
        Class aimClass = Class.forName(args[2]);
        EEntity a = null; 			// select instance that I need to test
        EAttribute armAttribute = null;
        Aggregate ins;
        
        if (args.length > 4) { // testing attribute
            armAttribute = LangUtils.findExplicitAttribute(armEntity, args[4]);
            if (armAttribute == null) {
                System.out.println("No attribute found.");
                return;
            }
        }
        ins = dataModel.getEntityExtentInstances(aimClass);
        SdaiIterator i = ins.createIterator();
        while(i.next()) {
            a = (EEntity)ins.getCurrentMemberObject(i);
            System.out.println("Test entity returned: " + (a.testMappedEntity(armEntity, dataSi, mappingSi, 0) != null) + " arm definition = " + armEntity + " aim entity = "  + a);
            if (args.length > 4) { // testing attribute
                Object o[] = a.getMappedAttribute(armAttribute, dataSi, mappingSi, 0);
                boolean f = o != null;
                System.out.println("Test attribute returned: " + f);
                if (f) {
                    System.out.println("	Attriutes: ");
                    for (int j = 0; j < o.length; j++) {
                        System.out.println("ei " + o[j]);
                    }
                }
            }
        }
        trans.abort();
    }
}
