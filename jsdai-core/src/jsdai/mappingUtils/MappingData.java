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

package jsdai.mappingUtils;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import javax.xml.parsers.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.SMapping_schema.*;
import jsdai.lang.*;
import jsdai.tools.RepositoryChanges;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.util.regex.Matcher;

/**
 *
 * @author Vaidas Nargėlas
 * @version $Revision$
 */
public class MappingData {

    /** Specifies operation we want to perform: import */
    public static final int IMPORT_OPERATION = 1;
    /** Specifies operation we want to perform: export */
    public static final int EXPORT_OPERATION = 2;
    /** Specifies operation we want to perform: copy */
    public static final int COPY_OPERATION = 3;
    /** Specifies operation we want to perform: find derived */
    public static final int FIND_DERIVED_OPERATION = 4;
    /** Specifies operation we want to perform: create schema instance */
    public static final int CREATE_SCHEMA_INSTANCE_OPERATION = 5;
    /** Specifies operation we want to perform: check and fix superclass attribute mappings */
    public static final int CHECK_SUPERCLASS_ATTRIBUTE_MAPPINGS_OPERATION = 6;
    /** Specifies operation we want to perform: missing mappings */
    public static final int FIND_MISSING_MAPPINGS_OPERATION = 7;
    /** Specifies operation we want to perform: compare two mapping models */
    public static final int COMPARE_MAPPINGS_OPERATION = 8;
    /** Specifies operation we want to perform: collect missing complex types */
    public static final int COLLECT_MISSING_COMPLEX_TYPES_OPERATION = 9;

	private static final int DICTIONARY_MODEL_SUFFIX_LENGTH = "_DICTIONARY_DATA".length();

    /** Holds session we are using
     */
    protected SdaiSession sdaiSession;

    /** Express Compiler Repository name
     */
    protected static final String repositoryName = "ExpressCompilerRepo";

    protected Document configDocument;

    protected SdaiRepository expressRepository;

    protected SdaiModel expressModel;

    protected SdaiModel mappingModel;

    protected SdaiModel linksModel;

    protected HashMap usedAttributesMap;

    protected HashMap usedNamedTypesMap;

    protected HashMap usedSchemaDefinitionsMap;

    protected HashMap originalInstancesMap;

    /** Creates new MappingData
     * @param sdaiSession The JSDAI session to use
     * @throws SdaiException This exception can be thrown in many cases
     */
    public MappingData(SdaiSession sdaiSession) throws SdaiException {
        this.sdaiSession = sdaiSession;
    }

    /** Execution starts here.
     * @param args the command line arguments
     * @throws ClassNotFoundException
     * @throws SdaiException May be thrown in many cases */
    public static void main(String[] args)
    throws SdaiException, ClassNotFoundException, FileNotFoundException, IOException {
        boolean showHelp = false;

        int operation =  -1;
        String binaryDir = null;
        String modelName = null;
        String modelName2 = null;
        String stepFile = null;
		String trackChangesFile = null;
		Collection missingSchemaMatchers = null;

        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("import"))
                operation = IMPORT_OPERATION;
            else if(args[0].equalsIgnoreCase("export"))
                operation = EXPORT_OPERATION;
            else if(args[0].equalsIgnoreCase("copy"))
                operation = COPY_OPERATION;
            else if(args[0].equalsIgnoreCase("find-derived"))
                operation = FIND_DERIVED_OPERATION;
            else if(args[0].equalsIgnoreCase("schema-instance"))
                operation = CREATE_SCHEMA_INSTANCE_OPERATION;
            else if(args[0].equalsIgnoreCase("superclass-attribute-mapping"))
                operation = CHECK_SUPERCLASS_ATTRIBUTE_MAPPINGS_OPERATION;
            else if(args[0].equalsIgnoreCase("find-missing-mappings"))
                operation = FIND_MISSING_MAPPINGS_OPERATION;
            else if(args[0].equalsIgnoreCase("compare-mappings"))
                operation = COMPARE_MAPPINGS_OPERATION;
            else if(args[0].equalsIgnoreCase("collect-missing-complex-types"))
                operation = COLLECT_MISSING_COMPLEX_TYPES_OPERATION;
            else {
                System.out.println("Unrecognized operation: " + args[0]);
                return;
            }
            for (int i = 1; i < args.length; i++) {
                if(args[i].equals("-binary") &&
					(operation == IMPORT_OPERATION || operation == COPY_OPERATION)) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
                    binaryDir = args[i];
                } else if(args[i].equals("-model") && operation != COMPARE_MAPPINGS_OPERATION) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
                    modelName = args[i];
                } else if(args[i].equals("-file") && operation <= EXPORT_OPERATION) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
                    stepFile = args[i];
                } else if(args[i].equals("-track") && operation == CREATE_SCHEMA_INSTANCE_OPERATION) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
                    trackChangesFile = args[i];
                } else if(args[i].equals("-for-schemas") && operation == FIND_MISSING_MAPPINGS_OPERATION) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
					if(missingSchemaMatchers == null) {
						missingSchemaMatchers = new ArrayList();
					}
                    missingSchemaMatchers.add(Pattern.compile(args[i], Pattern.CASE_INSENSITIVE).matcher(""));
                    trackChangesFile = args[i];
                } else if(operation == COMPARE_MAPPINGS_OPERATION) {
					if(args[i].equals("-model1")) {
						if(++i == args.length) {
							showHelp = true;
							break;
						}
						modelName = args[i];
					} else if(args[i].equals("-model2")) {
						if(++i == args.length) {
							showHelp = true;
							break;
						}
						modelName2 = args[i];
					}
                } else {
                    showHelp = true;
                    break;
                }
            }
            if(operation == COLLECT_MISSING_COMPLEX_TYPES_OPERATION && args.length > 1) {
            	showHelp = true;
            }
            if(operation != COLLECT_MISSING_COMPLEX_TYPES_OPERATION && args.length == 1) {
            	showHelp = true;
            }
            if(modelName == null && operation != COLLECT_MISSING_COMPLEX_TYPES_OPERATION) {
            	showHelp = true;
            }
            if(binaryDir == null && operation == COPY_OPERATION) {
            	showHelp = true;
            }
            if(modelName2 == null && operation == COMPARE_MAPPINGS_OPERATION) {
            	showHelp = true;
            }
        } else
            showHelp = true;
        if(showHelp) {
            System.out.println("Usage:");
            System.out.println("  java jsdai.mappingUtils.MappingData {export|import} " +
                               "-model <ARM_MAPPING_MODEL> [-file <step_file>] [-binary <dir>]");
            System.out.println("  java jsdai.mappingUtils.MappingData copy " +
                               "-model <ARM_MAPPING_MODEL> -binary <dir>");
            System.out.println("  java jsdai.mappingUtils.MappingData schema-instance " +
                               "-model <ARM_MAPPING_MODEL> [-track track_changes_property_file]");
            System.out.println("  java jsdai.mappingUtils.MappingData find-derived " +
                               "-model <ARM_MAPPING_MODEL>");
            System.out.println("  java jsdai.mappingUtils.MappingData superclass-attribute-mapping " +
                               "-model <ARM_MAPPING_MODEL>");
            System.out.println("  java jsdai.mappingUtils.MappingData find-missing-mappings " +
                               "-model <ARM_MAPPING_MODEL> [-for-schemas schema_name_regexp [...]]");
            System.out.println("  java jsdai.mappingUtils.MappingData compare-mappings " +
                               "-model1 <ARM_MAPPING_MODEL> -model2 <ARM_MAPPING_MODEL>");
            System.out.println("  java jsdai.mappingUtils.MappingData collect-missing-complex-types");
            return;
        }

        if(stepFile == null) stepFile = modelName + ".p21";
        SdaiSession sdaiSession = SdaiSession.openSession();
        MappingData mappingData = new MappingData(sdaiSession);

        switch(operation) {
            case IMPORT_OPERATION:
                mappingData.importOperation(modelName, stepFile, binaryDir);
                break;
            case EXPORT_OPERATION:
                mappingData.exportOperation(modelName, stepFile);
                break;
            case COPY_OPERATION:
                mappingData.copyOperation(modelName, binaryDir);
                break;
            case FIND_DERIVED_OPERATION:
                mappingData.findDerivedOperation(modelName);
                break;
            case CREATE_SCHEMA_INSTANCE_OPERATION:
                mappingData.createSchemaInstanceOperation(modelName, trackChangesFile);
                break;
            case CHECK_SUPERCLASS_ATTRIBUTE_MAPPINGS_OPERATION:
                mappingData.checkSuperclassAttributeMappingsOperation(modelName);
                break;
            case FIND_MISSING_MAPPINGS_OPERATION:
                mappingData.findMissingMappingsOperation(modelName, missingSchemaMatchers);
                break;
            case COMPARE_MAPPINGS_OPERATION:
                mappingData.compareMappingsOperation(modelName, modelName2);
                break;
            case COLLECT_MISSING_COMPLEX_TYPES_OPERATION:
                mappingData.collectMissingComplexTypesOperation();
                break;
        }

    }

	/** Performs export operation.
     *
     * <P><U>Replaces the following attributes:</U>
     * </P>
     * <UL>
     * <LI><B>attribute</B> is used in attribute_mapping_path_select, entity_or_attribute,
     * aggregate_member_constraint_select, attribute_select, attribute_value_constraint_select,
     * constraint_select, inverse_attribute_constraint_select, path_constraint_select,
     * select_constraint_select, generic_attribute_mapping.source
     * </LI>
     * <LI>
     * <I>attribute_mapping_path_select</I> is used in attribute_mapping.path
     * </LI>
     * <LI>
     * <I>aggregate_member_constraint_select</I> is used in aggregate_member_constraint.attribute,
     * aggregate_size_constraint.attribute.
     * </LI>
     * <LI>
     * <I>attribute_select</I> is used in entity_constraint.attribute
     * </LI>
     * <LI>
     * <I>attribute_value_constraint_select</I> is used in attribute_value_constraint.attribute
     * </LI>
     * <LI>
     * <I>constraint_select</I> is used in entity_mapping.constraints, constraint_relationship.element2
     * instance_constraint.element1, constraint_path.constraint, generic_attribute_mapping.constraints
     * </LI>
     * <LI>
     * <I>inverse_attribute_constraint_select</I> is used in inverse_attribute_constraint.inverted_attribute
     * </LI>
     * <LI>
     * <I>path_constraint_select</I> is used in path_constraint.element1, constraint_path.path
     * </LI>
     * <LI>
     * <I>select_constraint_select</I> is used in select_constraint.attribute
     * </LI>
     * <LI>
     * <B>entity_definition</B> is used in entity_or_attribute,
     * entity_mapping.source, entity_constraint.domain
     * </LI>
     * <LI>
     * <I>entity_or_attribute</I> is used in entity_mapping.target
     * </LI>
     * <LI>
     * <B>named_type</B> is used in attribute_mapping_domain_select,
     * generic_attribute_mapping.data_type.
     * </LI>
     * <LI>
     * <I>attribute_mapping_domain_select</I> is used in attribute_mapping.domain
     * </LI>
     * <LI>
     * <B>schema_definition</B> is used in schema_mapping.source, schema_mapping.target
     * </LI>
     * <LI>
     * <B>express_id</B> Hopefully we don't have to replace this
     * </LI>
     * <LI>
     * <B>defined_type</B> is used in select_constraint.data_type
     * </LI>
     * <LI>
     * <B>base_type</B> is used in attribute_mapping.target which is derived (nothing to replace)
     * </LI>
     * </UL>
     *
     * <P><U>The different view:</U></P>
     * <DL>
     * <DT>generic_attribute_mapping</DT>
     *   <DD><DL><DT>source</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     *   <DD><DL><DT>constraints</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     *   <DD><DL><DT>data_type</DT>
     *       <DD><I>named_type</I></DD></DL></DD>
     * <DT>attribute_mapping</DT>
     *   <DD><DL><DT>path</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     *   <DD><DL><DT>domain</DT>
     *       <DD><I>named_type</I></DD></DL></DD>
     * <DT>aggregate_member_constraint</DT>
     *   <DD><DL><DT>attribute</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>aggregate_size_constraint</DT>
     *   <DD><DL><DT>attribute</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>entity_constraint</DT>
     *   <DD><DL><DT>attribute</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     *   <DD><DL><DT>domain</DT>
     *       <DD><I>entity_definition</I></DD></DL></DD>
     * <DT>attribute_value_constraint</DT>
     *   <DD><DL><DT>attribute</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>entity_mapping</DT>
     *   <DD><DL><DT>source</DT>
     *       <DD><I>entity_definition</I></DD></DL></DD>
     *   <DD><DL><DT>target</DT>
     *       <DD><I>attribute</I></DD>
     *       <DD><I>entity_definition</I></DD></DL></DD>
     *   <DD><DL><DT>constraints</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>constraint_relationship</DT>
     *   <DD><DL><DT>element2</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>instance_constraint</DT>
     *   <DD><DL><DT>element1</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>constraint_path</DT>
     *   <DD><DL><DT>constraint</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     *   <DD><DL><DT>path</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>inverse_attribute_constraint</DT>
     *   <DD><DL><DT>inverted_attribute</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>path_constraint</DT>
     *   <DD><DL><DT>element1</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     * <DT>select_constraint</DT>
     *   <DD><DL><DT>attribute</DT>
     *       <DD><I>attribute</I></DD></DL></DD>
     *   <DD><DL><DT>data_type</DT>
     *       <DD><I>defined_type</I></DD></DL></DD>
     * <DT>schema_mapping</DT>
     *   <DD><DL><DT>source</DT>
     *       <DD><I>schema_definition</I></DD></DL></DD>
     *   <DD><DL><DT>target</DT>
     *       <DD><I>schema_definition</I></DD></DL></DD>
     * </DL>
     * </P>
     * @param modelName Mapping data model name
     * @throws SdaiException Can be thrown in many cases
     */
    public void exportOperation(String modelName, String stepFile)
    throws SdaiException, ClassNotFoundException {
        System.out.println(" Performing export operation");
        System.out.println("  Model " + modelName);
        System.out.println("  Step file " + (new File(stepFile)).getAbsolutePath());

        SdaiTransaction transaction = sdaiSession.startTransactionReadWriteAccess();
        expressRepository = findRepository(repositoryName);
        expressRepository.openRepository();
        expressModel = findModel(expressRepository, modelName);
        expressModel.startReadOnlyAccess();

        SdaiRepository exportRepository = sdaiSession.createRepository("", null);
        exportRepository.openRepository();
        mappingModel = exportRepository.createSdaiModel(modelName, expressModel.getUnderlyingSchema());
        mappingModel.startReadWriteAccess();

        System.out.println(" Copying instances...");
        AEntity allInstances = expressModel.getInstances();
        mappingModel.copyInstances(allInstances);

        linksModel = exportRepository.createSdaiModel("Links", SExtended_dictionary_schema.class);
        linksModel.startReadWriteAccess();

        System.out.println(" Unlinking external references...");
        scanExportLinks();

        //transaction.endTransactionAccessCommit();
        //transaction = sdaiSession.startTransactionReadWriteAccess();
        //mappingModel.endReadWriteAccess();
        exportRepository.exportClearTextEncoding(stepFile);
        transaction.endTransactionAccessCommit();
        mappingModel.endReadWriteAccess();
        exportRepository.closeRepository();
        expressModel.endReadOnlyAccess();
        expressRepository.closeRepository();
        //transaction.endTransactionAccessCommit();
        System.out.println(" Export operation completed successfully");
    }

    public void scanExportLinks() throws SdaiException, ClassNotFoundException {
        getConfiguration();

        usedAttributesMap = new HashMap();
        usedNamedTypesMap = new HashMap();
        usedSchemaDefinitionsMap = new HashMap();

        Element mappingSchemaEl = configDocument.getDocumentElement();
        for(org.w3c.dom.Node configurationNode = mappingSchemaEl.getFirstChild();
        configurationNode != null; configurationNode = configurationNode.getNextSibling()) {
            if(!(configurationNode instanceof Element)) continue;
            Element mappingEntityEl = (Element)configurationNode;

            String entityName = mappingEntityEl.getAttribute("name");
            AEntity entities =
                mappingModel.getInstances(Class.forName("jsdai.SMapping_schema.E" +
                                                       capitalizeString(entityName)));
            SdaiIterator entityIterator = entities.createIterator();
            while(entityIterator.next()) {
                EEntity entity = entities.getCurrentMemberEntity(entityIterator);

                for(org.w3c.dom.Node attributeNode = mappingEntityEl.getFirstChild();
                attributeNode != null; attributeNode = attributeNode.getNextSibling()) {
                    if(!(attributeNode instanceof Element)) continue;
                    Element attributeEl = (Element)attributeNode;

                    ArrayList dicEntityList = new ArrayList();
                    for(org.w3c.dom.Node dicEntityNode = attributeEl.getFirstChild();
                    dicEntityNode != null; dicEntityNode = dicEntityNode.getNextSibling()) {
                        if(!(dicEntityNode instanceof Element)) continue;
                        Element dicEntityEl = (Element)dicEntityNode;
                        dicEntityList.add(Class.forName("jsdai.SExtended_dictionary_schema.E" +
                                          capitalizeString(dicEntityEl.getAttribute("name"))));
                    }
                    unlinkLinks(entity, attributeEl.getAttribute("name"), dicEntityList);
                }
            }
        }
    }

    public void unlinkLinks(EEntity entity, String attributeName, ArrayList dicEntityList)
    throws SdaiException {
		jsdai.dictionary.EAttribute attribute;
		try {
			attribute = entity.getAttributeDefinition(attributeName);
		} catch(SdaiException e) {
			System.err.println("Entity " + entity + ", attribute " + attributeName);
			throw e;
		}
        jsdai.dictionary.EDefined_type selectPath[] = new jsdai.dictionary.EDefined_type[20];
        if(entity.testAttribute(attribute, selectPath) == 1/*Object*/) {

            Object attributeObject = entity.get_object(attribute);
            if(attributeObject instanceof EEntity) {
                EEntity attributeEntity = (EEntity)attributeObject;
                EEntity newEntity =
                    findLinkReplacement(attributeEntity, dicEntityList);
                if(newEntity != null) {
                    entity.set(attribute, newEntity, selectPath);
                }
            } else if(attributeObject instanceof AEntity) {
                AEntity attributeAggregate = (AEntity)attributeObject;
                SdaiIterator iterator = attributeAggregate.createIterator();
                while(iterator.next()) {
                    EEntity aggregateEntity = attributeAggregate.getCurrentMemberEntity(iterator);
                    EEntity newEntity =
                        findLinkReplacement(aggregateEntity, dicEntityList);
                    if(newEntity != null) {
                        attributeAggregate.setCurrentMember(iterator, newEntity);
                    }
                }
            }

        }
    }

    public EEntity findLinkReplacement(EEntity dictionaryEntity, ArrayList dicEntityList)
    throws SdaiException {
        Iterator dicEntityIter = dicEntityList.iterator();
        while(dicEntityIter.hasNext()) {
            Class dicEntityClass = (Class)dicEntityIter.next();
            if(dictionaryEntity.isKindOf(dicEntityClass)) {
                if(dicEntityClass.equals(EAttribute.class)) {
                    EAttribute dictionaryAttribute = (EAttribute)dictionaryEntity;
                    String compoundName =
                        dictionaryAttribute.findEntityInstanceSdaiModel().getName() + "." +
                        dictionaryAttribute.getParent(null).getName(null) + "." +
                        dictionaryAttribute.getName(null);
                    EAttribute newAttribute = (EAttribute)usedAttributesMap.get(compoundName);
                    if(newAttribute == null) {
                        newAttribute =
                            (EAttribute)linksModel.createEntityInstance(EExplicit_attribute.class);
                        newAttribute.setName(null, compoundName);
                        usedAttributesMap.put(compoundName, newAttribute);
                    }
                    return newAttribute;

                } else if(dicEntityClass.equals(ENamed_type.class) ||
                          dicEntityClass.equals(EEntity_definition.class) ||
                          dicEntityClass.equals(EDefined_type.class)) {
                    ENamed_type dictionaryNamedType = (ENamed_type)dictionaryEntity;
                    String compoundName =
                        dictionaryNamedType.findEntityInstanceSdaiModel().getName() + "." +
                        dictionaryNamedType.getName(null);
                    ENamed_type newNamedType = (ENamed_type)usedNamedTypesMap.get(compoundName);
                    if(newNamedType == null) {
                        newNamedType =
                            (ENamed_type)linksModel.createEntityInstance
                                (dictionaryNamedType instanceof EDefined_type ?
                                EDefined_type.class : EEntity_definition.class);
                        newNamedType.setName(null, compoundName);
                        usedNamedTypesMap.put(compoundName, newNamedType);
                    }
                    return newNamedType;

                } else if(dicEntityClass.equals(ESchema_definition.class)) {
                    ESchema_definition dictionarySchemDefinition =
                        (ESchema_definition)dictionaryEntity;
                    String compoundName =
                        dictionarySchemDefinition.findEntityInstanceSdaiModel().getName() + "." +
                        dictionarySchemDefinition.getName(null);
                    ESchema_definition newSchemDefinition =
                        (ESchema_definition)usedSchemaDefinitionsMap.get(compoundName);
                    if(newSchemDefinition == null) {
                        newSchemDefinition =
                            (ESchema_definition)linksModel.createEntityInstance(ESchema_definition.class);
                        newSchemDefinition.setName(null, compoundName);
                        usedSchemaDefinitionsMap.put(compoundName, newSchemDefinition);
                    }
                    return newSchemDefinition;

                } else {
                    System.out.println("Unsupported type: " + dicEntityClass);
                }
            }
        }
        return null;
    }

    /** Performs import operation
     * @param modelName Mapping data model name
     * @throws SdaiException Can be thrown in many cases
     */
    public void importOperation(String modelName, String stepFile, String binaryDir)
    throws SdaiException, ClassNotFoundException, FileNotFoundException, IOException {
        System.out.println(" Performing import operation");
        System.out.println("  Model " + modelName);
        System.out.println("  Step file " + (new File(stepFile)).getAbsolutePath());

        SdaiTransaction transaction = sdaiSession.startTransactionReadWriteAccess();
        expressRepository = findRepository(repositoryName);
        expressRepository.openRepository();

        SdaiRepository importRepository =
            sdaiSession.importClearTextEncoding("", stepFile, null);

        mappingModel = findModel(importRepository, modelName);
        mappingModel.reduceSdaiModelToRO();
        linksModel = findModel(importRepository, "Links");
        linksModel.reduceSdaiModelToRO();

        expressModel = testAndFindModel(expressRepository, modelName);
        if(expressModel != null) expressModel.deleteSdaiModel();
        expressModel = expressRepository.createSdaiModel(modelName, mappingModel.getUnderlyingSchema());
        expressModel.startReadWriteAccess();

        System.out.println(" Copying instances...");
        AEntity allInstances = mappingModel.getInstances();
        expressModel.copyInstances(allInstances);

        System.out.println(" Linking external references...");
        scanImportLinks();

        transaction.endTransactionAccessCommit();
        if(binaryDir != null) {
            System.out.println(" Copying binary file...");

            File fromFile = new File(expressRepository.getLocation(),
                                     expressModel.getId());
            (new File(binaryDir)).mkdirs();
            File toFile = new File(binaryDir, modelName);

            System.out.println("  From " + fromFile.getAbsolutePath());
            System.out.println("  To " + toFile.getAbsolutePath());

			FileInputStream in =
                new FileInputStream(fromFile);
			FileOutputStream out =
                new FileOutputStream(toFile);

			byte[] buffer = new byte[8 * 1024];
			int count = 0;
			do {
			    out.write(buffer, 0, count);
			    count = in.read(buffer, 0, buffer.length);
			} while (count != -1);

			in.close();
			out.close();
        }
        expressModel.endReadWriteAccess();
        importRepository.closeRepository();
        expressRepository.closeRepository();
        System.out.println(" Import operation completed successfully");
    }

    public void scanImportLinks() throws SdaiException, ClassNotFoundException {
        getConfiguration();

        fillOriginalMaps();

        Element mappingSchemaEl = configDocument.getDocumentElement();
        for(org.w3c.dom.Node configurationNode = mappingSchemaEl.getFirstChild();
        configurationNode != null; configurationNode = configurationNode.getNextSibling()) {
            if(!(configurationNode instanceof Element)) continue;
            Element mappingEntityEl = (Element)configurationNode;

            String entityName = mappingEntityEl.getAttribute("name");
            AEntity entities =
                expressModel.getInstances(Class.forName("jsdai.SMapping_schema.E" +
                                                       capitalizeString(entityName)));
            SdaiIterator entityIterator = entities.createIterator();
            while(entityIterator.next()) {
                EEntity entity = entities.getCurrentMemberEntity(entityIterator);

                for(org.w3c.dom.Node attributeNode = mappingEntityEl.getFirstChild();
                attributeNode != null; attributeNode = attributeNode.getNextSibling()) {
                    if(!(attributeNode instanceof Element)) continue;
                    Element attributeEl = (Element)attributeNode;

                    relinkLinks(entity, attributeEl.getAttribute("name"));
                }
            }
        }
    }

    public void fillOriginalMaps() throws SdaiException {
        originalInstancesMap = new HashMap();
        HashMap modelMap = new HashMap();

        AEntity instances;
        SdaiIterator instanceIterator;

        instances = linksModel.getInstances();
        instanceIterator = instances.createIterator();
        while(instanceIterator.next()) {
            EEntity linkInstance = instances.getCurrentMemberEntity(instanceIterator);
            String name = (String)
                linkInstance.get_object(linkInstance.getAttributeDefinition("name"));
            HashMap modelInstanceData = findModelInstanceData(modelMap, name);
            EEntity expressInstance =
                (EEntity)modelInstanceData.get(name);
            originalInstancesMap.put(linkInstance.getPersistentLabel(), expressInstance);
        }

    }

    public HashMap findModelInstanceData(HashMap modelMap, String compoundLinkName)
    throws SdaiException {
        String modelName = compoundLinkName.substring(0, compoundLinkName.indexOf('.'));
        HashMap modelInstanceData = (HashMap)modelMap.get(modelName);
        if(modelInstanceData == null) {
            SdaiModel model = findModel(expressRepository, modelName);
            if(model.getMode() == SdaiModel.NO_ACCESS) model.startReadOnlyAccess();
            modelInstanceData = new HashMap();

            AEntity instances;
            SdaiIterator instanceIterator;

            instances = model.getInstances(EAttribute.class);
            instanceIterator = instances.createIterator();
            while(instanceIterator.next()) {
                EAttribute instance =
                    (EAttribute)instances.getCurrentMemberEntity(instanceIterator);
                String compoundName =
                    instance.findEntityInstanceSdaiModel().getName() + "." +
                    instance.getParent(null).getName(null) + "." +
                    instance.getName(null);
                modelInstanceData.put(compoundName, instance);
            }

            instances = model.getInstances(ENamed_type.class);
            instanceIterator = instances.createIterator();
            while(instanceIterator.next()) {
                ENamed_type instance =
                    (ENamed_type)instances.getCurrentMemberEntity(instanceIterator);
                String compoundName =
                    instance.findEntityInstanceSdaiModel().getName() + "." +
                    instance.getName(null);
                modelInstanceData.put(compoundName, instance);
            }

            instances = model.getInstances(ESchema_definition.class);
            instanceIterator = instances.createIterator();
            while(instanceIterator.next()) {
                ESchema_definition instance =
                    (ESchema_definition)instances.getCurrentMemberEntity(instanceIterator);
                String compoundName =
                    instance.findEntityInstanceSdaiModel().getName() + "." +
                    instance.getName(null);
                modelInstanceData.put(compoundName, instance);
            }

            modelMap.put(modelName, modelInstanceData);
        }

        return modelInstanceData;
    }

    public void relinkLinks(EEntity entity, String attributeName)
    throws SdaiException {
        jsdai.dictionary.EAttribute attribute =
            entity.getAttributeDefinition(attributeName);
        jsdai.dictionary.EDefined_type selectPath[] = new jsdai.dictionary.EDefined_type[20];
        if(entity.testAttribute(attribute, selectPath) == 1/*Object*/) {

            Object attributeObject = entity.get_object(attribute);
            if(attributeObject instanceof EEntity) {
                EEntity attributeEntity = (EEntity)attributeObject;
                if(attributeEntity.findEntityInstanceSdaiModel() == linksModel) {
                    EEntity newEntity = (EEntity)
                        originalInstancesMap.get(attributeEntity.getPersistentLabel());
		    if(newEntity == null) {
			throw new SdaiException(SdaiException.SY_ERR, attribute,
						"Entity " + attributeEntity + " not found for " +
						attribute);
		    }
                    entity.set(attribute, newEntity, selectPath);
                }
            } else if(attributeObject instanceof AEntity) {
                AEntity attributeAggregate = (AEntity)attributeObject;
                SdaiIterator iterator = attributeAggregate.createIterator();
                while(iterator.next()) {
                    EEntity aggregateEntity = attributeAggregate.getCurrentMemberEntity(iterator);
                    if(aggregateEntity.findEntityInstanceSdaiModel() == linksModel) {
                        EEntity newEntity = (EEntity)
                            originalInstancesMap.get(aggregateEntity.getPersistentLabel());
                        attributeAggregate.setCurrentMember(iterator, newEntity);
                    }
                }
            }

        }
    }

	public void copyOperation(String modelName, String binaryDir)
	throws SdaiException, FileNotFoundException, IOException {
		System.out.println(" Performing copy operation");
		System.out.println("  Model " + modelName);

		SdaiTransaction transaction = sdaiSession.startTransactionReadOnlyAccess();
		expressRepository = findRepository(repositoryName);
		expressRepository.openRepository();

		expressModel = findModel(expressRepository, modelName);
		expressModel.startReadOnlyAccess();

		URL fromURL = expressModel.getLocationURL();
		(new File(binaryDir)).mkdirs();
		File toFile = new File(binaryDir, modelName);

		System.out.println("  From " + fromURL);
		System.out.println("  To " + toFile.getAbsolutePath());

		InputStream in = fromURL.openStream();
		FileOutputStream out = new FileOutputStream(toFile);

		byte[] buffer = new byte[8 * 1024];
		int count = 0;
		do {
			out.write(buffer, 0, count);
			count = in.read(buffer, 0, buffer.length);
		} while (count != -1);

		in.close();
		out.close();
		transaction.commit();
		expressModel.endReadOnlyAccess();
		expressRepository.closeRepository();
		System.out.println(" Copy operation completed successfully");
	}

    public void findDerivedOperation(String modelName)
    throws SdaiException {
       System.out.println(" Finding derived attributes");
       System.out.println("  Model " + modelName);

       SdaiTransaction transaction = sdaiSession.startTransactionReadWriteAccess();
       expressRepository = findRepository(repositoryName);
       expressRepository.openRepository();
       expressModel = findModel(expressRepository, modelName);
       expressModel.startReadOnlyAccess();
	   int referencedCount = 0;
	   AEntity instances = expressModel.getInstances();
	   SdaiIterator instanceIter = instances.createIterator();
	   while(instanceIter.next()) {
		   EEntity instance = instances.getCurrentMemberEntity(instanceIter);
		   AEntity referencedInstances;
		   try {
			   referencedInstances = instance.getAllReferences();
		   } catch(Exception e) {
			   e.printStackTrace(System.out);
			   continue;
		   }

		   SdaiIterator referencedInstanceIter = referencedInstances.createIterator();
		   while(referencedInstanceIter.next()) {
			   EEntity referencedInstance =
					   referencedInstances.getCurrentMemberEntity(referencedInstanceIter);
			   if(referencedInstance instanceof EDerived_attribute) {
				   String refInstModelName = referencedInstance.findEntityInstanceSdaiModel().getName();
				   System.out.println("  " + referencedInstance + " in " +
									  refInstModelName.substring(0, refInstModelName.length() -
																 "_DICTIONARY_DATA".length()) +
									  " is referenced by " + instance);
				   referencedCount++;
			   }
		   }
	   }
       expressModel.endReadOnlyAccess();
       expressRepository.closeRepository();
       System.out.println(" Found: " + referencedCount + " derived attributes");
    }

    public void createSchemaInstanceOperation(String modelName, String trackChangesFile)
    throws SdaiException {
        System.out.println(" Creating schema instance");
        System.out.println("  Name " + modelName);

        SdaiTransaction transaction = sdaiSession.startTransactionReadWriteAccess();
        expressRepository = findRepository(repositoryName);
        expressRepository.openRepository();
		if(trackChangesFile != null) {
			RepositoryChanges.rememberRepositoryState(expressRepository);
		}
        expressModel = findModel(expressRepository, modelName);
        expressModel.startReadOnlyAccess();

        String mappingSchemaInstanceName = modelName.toLowerCase();
        SchemaInstance mappingSchemaInstance;
        mappingSchemaInstance = expressRepository.findSchemaInstance(mappingSchemaInstanceName);
        if(mappingSchemaInstance != null) mappingSchemaInstance.delete();

        ESchema_mapping schemaMapping =
            (ESchema_mapping)expressModel.getInstances(ESchema_mapping.class).getByIndexEntity(1);
        String armSchemaName = schemaMapping.getSource(null).getName(null);
        String armSchemaInstanceName = armSchemaName.toLowerCase();
        SchemaInstance armSchemaInstance =
            findSchemaInstance(expressRepository, armSchemaInstanceName);
        String aimSchemaName = schemaMapping.getTarget(null).getName(null);
        String aimSchemaInstanceName = aimSchemaName.toLowerCase();
        SchemaInstance aimSchemaInstance =
            findSchemaInstance(expressRepository, aimSchemaInstanceName);
        mappingSchemaInstance =
            expressRepository.createSchemaInstance(mappingSchemaInstanceName, jsdai.mapping.SMapping.class);
        mappingSchemaInstance.addSdaiModel(expressModel);
        ASdaiModel armModels = armSchemaInstance.getAssociatedModels();
        SdaiIterator modelIterator = armModels.createIterator();
        while(modelIterator.next()) {
            SdaiModel model = armModels.getCurrentMember(modelIterator);
            mappingSchemaInstance.addSdaiModel(model);
        }
        ASdaiModel aimModels = aimSchemaInstance.getAssociatedModels();
        modelIterator = aimModels.createIterator();
        while(modelIterator.next()) {
            SdaiModel model = aimModels.getCurrentMember(modelIterator);
            mappingSchemaInstance.addSdaiModel(model);
        }

        transaction.commit();
        expressModel.endReadOnlyAccess();

		if(trackChangesFile != null) {
			RepositoryChanges.trackRepositoryChanges(expressRepository, trackChangesFile);
		}
        expressRepository.closeRepository();
        transaction.endTransactionAccessCommit();
        System.out.println(" Schema instance created successfully");
    }

    public void checkSuperclassAttributeMappingsOperation(String modelName)
    throws SdaiException {
        System.out.println(" Checking and fixing superclass attribute mappings");
        System.out.println("  Name " + modelName);

        SdaiTransaction transaction = sdaiSession.startTransactionReadWriteAccess();
        expressRepository = findRepository(repositoryName);
        expressRepository.openRepository();
        expressModel = findModel(expressRepository, modelName);
        expressModel.startReadWriteAccess();
		ASdaiModel mappingDomain = new ASdaiModel();
		mappingDomain.addByIndex(1, expressModel, null);

		AEntity_mapping entityMappings = (AEntity_mapping)expressModel.getInstances(EEntity_mapping.class);
        SdaiIterator entityMappingIterator = entityMappings.createIterator();
        while(entityMappingIterator.next()) {
            EEntity_mapping entityMapping = entityMappings.getCurrentMember(entityMappingIterator);
            EEntity_definition armEntity = entityMapping.getSource(null);
			HashSet aimSupertypes = new HashSet();
			collectSupertypes(entityMapping.getTarget(null), aimSupertypes);
			HashSet mappedAttributes = new HashSet();
			collectRedeclaredDerivedAttributeMappings(mappedAttributes,
					entityMapping, mappingDomain, aimSupertypes);
			checkSupertypeAttributeMappings(null, mappedAttributes, armEntity,
					mappingDomain, aimSupertypes, 1);
			checkAndFixSuperclassAttributeMappings(entityMapping, mappedAttributes,
					entityMapping, mappingDomain, aimSupertypes);
			checkSupertypeAttributeMappings(entityMapping, mappedAttributes, armEntity,
					mappingDomain, aimSupertypes, 2);
		}

        transaction.endTransactionAccessCommit();
        expressModel.endReadWriteAccess();
        expressRepository.closeRepository();
        System.out.println(" Superclass attribute mappings checked and fixed successfully");
    }

	void collectRedeclaredDerivedAttributeMappings(HashSet mappedAttributes,
			EEntity_mapping startMapping, ASdaiModel mappingDomain,
			HashSet aimSupertypes) throws SdaiException {
		EEntity_definition startArmEntity = startMapping.getSource(null);
		AAttribute startArmAttrs = startArmEntity.getAttributes(null, null);
		for(SdaiIterator i = startArmAttrs.createIterator(); i.next(); ) {
			EAttribute attribute = startArmAttrs.getCurrentMember(i);
			if (attribute instanceof EDerived_attribute) {
				EDerived_attribute derivedAttribute = (EDerived_attribute) attribute;
				attribute = null;
				while(derivedAttribute.testRedeclaring(null)) {
					EEntity redeclaringAttr = derivedAttribute.getRedeclaring(null);
					if(redeclaringAttr instanceof EDerived_attribute) {
						derivedAttribute = (EDerived_attribute) redeclaringAttr;
						attribute = derivedAttribute;
					} else {
						 EExplicit_attribute explicitAttribute = (EExplicit_attribute) redeclaringAttr;
						 while(explicitAttribute.testRedeclaring(null)) {
							 explicitAttribute = explicitAttribute.getRedeclaring(null);
						 }
						 attribute = explicitAttribute;
						 break;
					}
				}
				if(attribute != null) {
					String attributeName = attribute.getParent(null).getName(null) + "." +
						attribute.getName(null);
					mappedAttributes.add(attributeName);
				}
			}
		}
	}

	void checkAndFixSuperclassAttributeMappings(EEntity_mapping fixMapping, HashSet mappedAttributes,
			EEntity_mapping startMapping, ASdaiModel mappingDomain, HashSet aimSupertypes)
	throws SdaiException {
		AGeneric_attribute_mapping attributeMappings = new AGeneric_attribute_mapping();
		CGeneric_attribute_mapping.usedinParent_entity(null, startMapping,
														mappingDomain, attributeMappings);
		SdaiIterator attributeMappingsIter = attributeMappings.createIterator();
		HashSet newMappedAttributes = new HashSet();
		while (attributeMappingsIter.next()) {
			 EGeneric_attribute_mapping attributeMapping =
				attributeMappings.getCurrentMember(attributeMappingsIter);
			 EAttribute attribute = attributeMapping.getSource(null);
			 if(attribute instanceof EExplicit_attribute) {
				 EExplicit_attribute explicitAttribute = (EExplicit_attribute)attribute;
				 while(explicitAttribute.testRedeclaring(null)) {
					 explicitAttribute = explicitAttribute.getRedeclaring(null);
				 }
				 attribute = explicitAttribute;
			 }
			 String attributeName = attribute.getParent(null).getName(null) + "." +
									attribute.getName(null);
			 if(fixMapping != startMapping && !mappedAttributes.contains(attributeName)) {
				 EGeneric_attribute_mapping newAttributeMapping =
					(EGeneric_attribute_mapping)attributeMapping.copyApplicationInstance(expressModel);
				 newAttributeMapping.setParent_entity(null, fixMapping);
//				 System.out.println("  Fixed " + attributeName + " mapping for " +
//									fixMapping.getSource(null).getName(null));
			 }
			 newMappedAttributes.add(attributeName);
		}
		mappedAttributes.addAll(newMappedAttributes);
	}

	void checkSupertypeAttributeMappings(EEntity_mapping fixMapping, HashSet mappedAttributes,
			EEntity_definition startArmEntity, ASdaiModel mappingDomain,
			HashSet aimSupertypes, int callMethodSwitch) throws SdaiException {
		AEntity supertypes = startArmEntity.getGeneric_supertypes(null);
		SdaiIterator supertypesIter = supertypes.createIterator();
		while (supertypesIter.next()) {
			EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(supertypesIter);
			AEntity_mapping supertypeMappings = new AEntity_mapping();
			CEntity_mapping.usedinSource(null, supertype, mappingDomain, supertypeMappings);
			if(supertypeMappings.getMemberCount() > 0) {
				List supertypeMappingList = new ArrayList();
				SdaiIterator mappingsIter = supertypeMappings.createIterator();
				while (mappingsIter.next()) {
					EEntity_mapping supertypeMapping =
						supertypeMappings.getCurrentMember(mappingsIter);
					supertypeMappingList.add(supertypeMapping);
				}
				Collections.sort(supertypeMappingList,
					new Comparator() {
						public int compare(Object o1, Object o2) {
							try {
								EEntity_mapping m1 = (EEntity_mapping)o1;
								EEntity_mapping m2 = (EEntity_mapping)o2;
								EEntity t1 = m1.getTarget(null);
								EEntity t2 = m2.getTarget(null);
								if(t1 instanceof EEntity_definition
								&& t2 instanceof EEntity_definition) {
									EEntity_definition e1 = (EEntity_definition)t1;
									EEntity_definition e2 = (EEntity_definition)t2;
									if(isSupertypeOf(e2, e1)) return -1;
									if(isSupertypeOf(e1, e2)) return 1;
								}
								return m1.getPersistentLabel().compareTo(m2.getPersistentLabel());
							} catch(SdaiException e) {
								return 0;
							}
						}
					}
				);
				Iterator listIter = supertypeMappingList.iterator();
				while(listIter.hasNext()) {
					EEntity_mapping supertypeMapping = (EEntity_mapping)listIter.next();
					EEntity supertypeTarget = supertypeMapping.getTarget(null);
					boolean isAimSupertype = aimSupertypes.contains(supertypeTarget.getPersistentLabel());
					if(!isAimSupertype && supertypeTarget instanceof EEntity_definition
							&& ((EEntity_definition) supertypeTarget).getComplex(null)) {
						isAimSupertype = true;
						AEntity leaves = ((EEntity_definition) supertypeTarget).getGeneric_supertypes(null);
						SdaiIterator leafIterator = leaves.createIterator();
						while(leafIterator.next()) {
							EEntity_definition leaf = (EEntity_definition)leaves.getCurrentMemberObject(leafIterator);
							if(!aimSupertypes.contains(leaf.getPersistentLabel())) {
								isAimSupertype = false;
								break;
							}
						}
					}
					if(isAimSupertype) {
						switch (callMethodSwitch) {
						case 1:
							collectRedeclaredDerivedAttributeMappings(mappedAttributes,
									supertypeMapping, mappingDomain, aimSupertypes);
							break;

						case 2:
							checkAndFixSuperclassAttributeMappings(fixMapping, mappedAttributes,
									supertypeMapping, mappingDomain, aimSupertypes);
							break;
						}
					}
				}
			}
			checkSupertypeAttributeMappings(fixMapping, mappedAttributes, supertype,
					mappingDomain, aimSupertypes, callMethodSwitch);
		}
	}

	void collectSupertypes(EEntity entity, HashSet aimSupertypes) throws SdaiException {
		aimSupertypes.add(entity.getPersistentLabel());
		if(entity instanceof EEntity_definition) {
			AEntity supertypes = ((EEntity_definition)entity).getGeneric_supertypes(null);
			SdaiIterator supertypesIter = supertypes.createIterator();
			while (supertypesIter.next()) {
				EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(supertypesIter);
				aimSupertypes.add(supertype.getPersistentLabel());
				collectSupertypes(supertype, aimSupertypes);
			}
		}
	}

	protected static boolean
	isSupertypeOf(EEntity_definition thisEntity, EEntity_definition otherEntity)
	throws SdaiException {
		if(thisEntity.getComplex(null)) {
			AEntity leaves = thisEntity.getGeneric_supertypes(null);
			SdaiIterator leafIterator = leaves.createIterator();
			while(leafIterator.next()) {
				EEntity_definition leaf = (EEntity_definition)leaves.getCurrentMemberObject(leafIterator);
				if(!isSupertypeOfEntity(leaf, otherEntity)) {
					return false;
				}
			}
			return true;
		} else {
			return isSupertypeOfEntity(thisEntity, otherEntity);
		}
	}

	private static boolean
	isSupertypeOfEntity(EEntity_definition thisEntity, EEntity_definition otherEntity)
	throws SdaiException {
		if(otherEntity.testGeneric_supertypes(null)) {
			AEntity supertypes = otherEntity.getGeneric_supertypes(null);
			SdaiIterator supertypeIterator = supertypes.createIterator();
			while(supertypeIterator.next()) {
				EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(supertypeIterator);
				if(supertype == thisEntity) {
					return true;
				}
				if(isSupertypeOfEntity(thisEntity, supertype)) {
					return true;
				}
			}
		}
		return false;
	}

    /**
	 * Method <code>findMissingMappingsOperation</code> executes find-missing-mappings
	 * operation. It finds which ARM entities and their attributes have no mappings in
	 * model specified in <code>modelName</code>.
	 *
	 * @param modelName mapping model name as <code>String</code> value
	 * @exception SdaiException if an error occurs
	 */
	public void findMissingMappingsOperation(String modelName, Collection missingSchemaMatchers) throws SdaiException {
        System.out.println(" Looking for missing mappings");
        System.out.println("  Name " + modelName);
		int missingEntityMapCount = 0;
		int missingAttributeMapCount = 0;

        SdaiTransaction transaction = sdaiSession.startTransactionReadOnlyAccess();
        expressRepository = findRepository(repositoryName);
        expressRepository.openRepository();
        expressModel = findModel(expressRepository, modelName);
        expressModel.startReadOnlyAccess();
		ASdaiModel mappingDomain = new ASdaiModel();
		mappingDomain.addByIndex(1, expressModel, null);
		ESchema_mapping schemaMapping = (ESchema_mapping)
			expressModel.getInstances(ESchema_mapping.class).getByIndexEntity(1);
		SdaiModel armModel = schemaMapping.getSource(null).findEntityInstanceSdaiModel();
		ASdaiModel armDomain = new ASdaiModel();
		armDomain.addByIndex(1, armModel, null);

		AEntity_declaration entityDeclarations = (AEntity_declaration)armModel.getInstances(EEntity_declaration.class);
		SdaiIterator entityDecIter = entityDeclarations.createIterator();
		while(entityDecIter.next()) {
			EEntity_definition entity = (EEntity_definition)entityDeclarations.getCurrentMember(entityDecIter).getDefinition(null);
			if(!entity.getComplex(null)) {
				AEntity_mapping entityMaps = new AEntity_mapping();
				CEntity_mapping.usedinSource(null, entity, mappingDomain, entityMaps);
				if(entityMaps.getMemberCount() > 0) {
					AAttribute entityAttrs = new AAttribute();
					CAttribute.usedinParent(null, entity, armDomain, entityAttrs);
					SdaiIterator entityAttrIter = entityAttrs.createIterator();
					while(entityAttrIter.next()) {
						EAttribute entityAttr = entityAttrs.getCurrentMember(entityAttrIter);
						if(entityAttr instanceof EExplicit_attribute
						   && !((EExplicit_attribute)entityAttr).testRedeclaring(null)) {
							AGeneric_attribute_mapping attrMaps = new AGeneric_attribute_mapping();
							CGeneric_attribute_mapping.usedinSource(null, entityAttr,
																	mappingDomain, attrMaps);
							if(attrMaps.getMemberCount() == 0) {
								System.out.println("  Mapping missing for attribute: " +
												   entity.getName(null) + "." + entityAttr.getName(null));
								missingAttributeMapCount++;
							}
						}
					}
				} else {
					if(missingSchemaMatchers != null) {
						String entityModelName = entity.findEntityInstanceSdaiModel().getName();
						String entitySchemaName = entityModelName.substring(0, entityModelName.length() - DICTIONARY_MODEL_SUFFIX_LENGTH);
						for(Iterator i = missingSchemaMatchers.iterator(); i.hasNext(); ) {
							Matcher missingSchemaMatcher = (Matcher)i.next();
							missingSchemaMatcher.reset(entitySchemaName);
							if(missingSchemaMatcher.matches()) {
								System.out.println("  Mapping missing for entity: " + entity.getName(null));
								missingEntityMapCount++;
								break;
							}
						}
					} else {
						System.out.println("  Mapping missing for entity: " + entity.getName(null));
						missingEntityMapCount++;
					}
				}
			}
		}
        transaction.endTransactionAccessCommit();
        expressModel.endReadOnlyAccess();
        expressRepository.closeRepository();
        System.out.println(" Number of missing entity mappings: " + missingEntityMapCount);
        System.out.println(" Number of missing attribute mappings: " + missingAttributeMapCount);
    }

	public void compareMappingsOperation(String modelName1, String modelName2)
    throws SdaiException {
        System.out.println(" Comparing mapping models");
        System.out.println("  Name1 " + modelName1);
        System.out.println("  Name2 " + modelName2);
		System.out.println();

        SdaiTransaction transaction = sdaiSession.startTransactionReadOnlyAccess();
        expressRepository = findRepository(repositoryName);
        expressRepository.openRepository();
        SdaiModel model1 = findModel(expressRepository, modelName1);
        model1.startReadOnlyAccess();
        SdaiModel model2 = findModel(expressRepository, modelName2);
        model2.startReadOnlyAccess();

		HashMap entityMappings1 = new HashMap();
		getEntityMappings(entityMappings1, model1);
		HashMap entityMappings2 = new HashMap();
		getEntityMappings(entityMappings2, model2);
		Map commonEntMappings = (Map)entityMappings1.clone();
		if(commonEntMappings.keySet().retainAll(entityMappings2.keySet())) {
			// There are different entity mappings
			Map onlyEntityMappings1 = (Map)entityMappings1.clone();
			onlyEntityMappings1.keySet().removeAll(entityMappings2.keySet());
			Map onlyEntityMappings2 = (Map)entityMappings2.clone();
			onlyEntityMappings2.keySet().removeAll(entityMappings1.keySet());
			Iterator entMapIter = onlyEntityMappings1.keySet().iterator();
			boolean first = true;
			while(entMapIter.hasNext()) {
				if(first) {
					System.out.println("  Only in model " + modelName1);
					first = false;
				}
				String mappingName = entMapIter.next().toString();
				System.out.println("   " + mappingName);
			}
			if(!first) System.out.println();
			entMapIter = onlyEntityMappings2.keySet().iterator();
			first = true;
			while(entMapIter.hasNext()) {
				if(first) {
					System.out.println("  Only in model " + modelName2);
					first = false;
				}
				String mappingName = entMapIter.next().toString();
				System.out.println("   " + mappingName);
			}
			if(!first) System.out.println();
		}
		HashMap attMappings1 = new HashMap();
		HashMap attMappings2 = new HashMap();
		Iterator entMapIter = commonEntMappings.entrySet().iterator();
		while(entMapIter.hasNext()) {
			Map.Entry entry = (Map.Entry)entMapIter.next();
			Object entMappingKey = entry.getKey();
			EEntity_mapping entMapping1 = (EEntity_mapping)entry.getValue();
			EEntity_mapping entMapping2 = (EEntity_mapping)entityMappings2.get(entMappingKey);
			StringBuffer entMessage = new StringBuffer(" Entity mapping " + entMappingKey.toString());
			compareConstraints(entMapping1.testConstraints(null) ? entMapping1.getConstraints(null) : null,
							   entMapping2.testConstraints(null) ? entMapping2.getConstraints(null) : null,
							   entMessage, new StringBuffer(""));
			attMappings1.clear();
			getAttributeMappings(attMappings1, entMapping1);
			attMappings2.clear();
			getAttributeMappings(attMappings2, entMapping2);
			Map commonAttMappings = (Map)attMappings1.clone();
			if(commonAttMappings.keySet().retainAll(attMappings2.keySet())) {
				// There are different attribute mappings
				Map onlyAttMappings1 = (Map)attMappings1.clone();
				onlyAttMappings1.keySet().removeAll(attMappings2.keySet());
				Map onlyAttMappings2 = (Map)attMappings2.clone();
				onlyAttMappings2.keySet().removeAll(attMappings1.keySet());
				Iterator attMapIter = onlyAttMappings1.keySet().iterator();
				System.out.println();
				boolean first = true;
				while(attMapIter.hasNext()) {
					if(first) {
						printMessageHeader(entMessage);
						System.out.println("  Attribute mapping(s) only in model " + modelName1);
						first = false;
					}
					String mappingName = attMapIter.next().toString();
					System.out.println("   " + mappingName);
				}
				if(!first) System.out.println();
				attMapIter = onlyAttMappings2.keySet().iterator();
				first = true;
				while(attMapIter.hasNext()) {
					if(first) {
						printMessageHeader(entMessage);
						System.out.println("  Attribute mapping(s) only in model " + modelName2);
						first = false;
					}
					String mappingName = attMapIter.next().toString();
					System.out.println("   " + mappingName);
				}
				if(!first) System.out.println();
			}
			Iterator attMapIter = commonAttMappings.entrySet().iterator();
			while(attMapIter.hasNext()) {
				Map.Entry attEntry = (Map.Entry)attMapIter.next();
				Object attMappingKey = attEntry.getKey();
				EGeneric_attribute_mapping attMapping1 =
					(EGeneric_attribute_mapping)attEntry.getValue();
				EGeneric_attribute_mapping attMapping2 =
					(EGeneric_attribute_mapping)attMappings2.get(attMappingKey);
				StringBuffer attMessage = new StringBuffer(" Attribute mapping " +
														   attMappingKey.toString());
				compareConstraints(attMapping1.testConstraints(null) ?
								   attMapping1.getConstraints(null) : null,
								   attMapping2.testConstraints(null) ?
								   attMapping2.getConstraints(null) : null,
								   entMessage, attMessage);
				if(attMapping1 instanceof EAttribute_mapping &&
				   attMapping2 instanceof EAttribute_mapping) {
					EAttribute_mapping attAttMapping1 = (EAttribute_mapping)attMapping1;
					EAttribute_mapping attAttMapping2 = (EAttribute_mapping)attMapping2;
					EEntity domain1 = attAttMapping1.testDomain(null) ?
						attAttMapping1.getDomain(null) : attAttMapping1;
					EEntity domain2 = attAttMapping2.testDomain(null) ?
						attAttMapping2.getDomain(null) : attAttMapping2;
					if(domain1.getInstanceType() != domain2.getInstanceType()) {
						printMessageHeader(entMessage);
						printMessageHeader(attMessage);
						System.out.println("  Have different domains: " + domain1 +
							   " in model1, and " + domain2 + " in model2");
					}
				}
			}
		}
	}

	private void getEntityMappings(Map mappingMap, SdaiModel model)
	throws SdaiException {
		AEntity_mapping entMappings = (AEntity_mapping)model.getInstances(EEntity_mapping.class);
		SdaiIterator entMappIter = entMappings.createIterator();
		while(entMappIter.next()) {
			EEntity_mapping entMapping = entMappings.getCurrentMember(entMappIter);
			String mappName = entMapping.getSource(null).getName(null) + " " +
				((EEntity_definition)entMapping.getTarget(null)).getName(null);
			mappingMap.put(mappName, entMapping);
		}
	}

	private void getAttributeMappings(Map mappingMap, EEntity_mapping entMapping)
	throws SdaiException {
		AGeneric_attribute_mapping attMappings = new AGeneric_attribute_mapping();
		CGeneric_attribute_mapping.usedinParent_entity(null, entMapping, null, attMappings);
		SdaiIterator attMappIter = attMappings.createIterator();
		while(attMappIter.next()) {
			EGeneric_attribute_mapping attMapping = attMappings.getCurrentMember(attMappIter);

			//FIXME: key name should be able to handle cases when there are two attribute
			//alternatives for the same attribute with the same data type but different constraints.
			//This is pretty complicated because I don't know how to match alternatives like this
			//in two mappings
			String mappName = attMapping.getSource(null).getName(null) +
				(attMapping.testData_type(null) ? " " + attMapping.getData_type(null) : "");
			mappingMap.put(mappName, attMapping);
		}
	}

	private void compareConstraints(EEntity constraint1, EEntity constraint2,
									StringBuffer message1, StringBuffer message2)
	throws SdaiException {
		if(constraint1 == null && constraint2 == null) return;
		if(constraint1 == null) {
			printMessageHeader(message1);
			printMessageHeader(message2);
			System.out.println("  Has constraints only in model2");
			return;
		}
		if(constraint2 == null) {
			printMessageHeader(message1);
			printMessageHeader(message2);
			System.out.println("  Has constraints only in model1");
			return;
		}
		compareOneConstraint(constraint1, constraint2, message1, message2);
	}

	private void compareOneConstraint(EEntity constraint1, EEntity constraint2,
									  StringBuffer message1, StringBuffer message2)
	throws SdaiException {
		if(constraint1.getClass() != constraint2.getClass()) {
			printMessageHeader(message1);
			printMessageHeader(message2);
			System.out.println("  Constraints are of different types: " + constraint1 +
							   " in model1, and " + constraint2 + " in model2");
		} else if(constraint1 instanceof EAttribute_value_constraint) {
			EAttribute_value_constraint avc1 = (EAttribute_value_constraint)constraint1;
			EAttribute_value_constraint avc2 = (EAttribute_value_constraint)constraint2;
			compareOneConstraint(avc1.getAttribute(null), avc2.getAttribute(null), message1, message2);
			if(!(constraint1 instanceof ENon_optional_constraint)
			&& !avc1.get_object(avc1.getAttributeDefinition("constraint_value"))
			   .equals(avc2.get_object(avc1.getAttributeDefinition("constraint_value")))) {
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  constraint_values are different: " + constraint1 +
								   " in model1, and " + constraint2 + " in model2");
			}
		} else if(constraint1 instanceof ESelect_constraint) {
			ESelect_constraint sc1 = (ESelect_constraint)constraint1;
			ESelect_constraint sc2 = (ESelect_constraint)constraint2;
			String dataTypeString1 = sc1.getData_type(null).toString();
			String dataTypeString2 = sc2.getData_type(null).toString();
			if(!dataTypeString1.equals(dataTypeString2)) {
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  data_types are different: " + constraint1 +
								   " in model1, and " + constraint2 + " in model2");
			}
			compareOneConstraint(sc1.getAttribute(null), sc2.getAttribute(null), message1, message2);
		} else if(constraint1 instanceof EAggregate_member_constraint) {
			EAggregate_member_constraint amc1 = (EAggregate_member_constraint)constraint1;
			EAggregate_member_constraint amc2 = (EAggregate_member_constraint)constraint2;
			if(amc1.testMember(null) == amc2.testMember(null)) {
				if(amc1.testMember(null) && amc1.getMember(null) != amc2.getMember(null)) {
					printMessageHeader(message1);
					printMessageHeader(message2);
					System.out.println("  member values are different: " + constraint1 +
									   " in model1, and " + constraint2 + " in model2");
				}
			} else {
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  member is not set in one model: " + constraint1 +
									   " in model1, and " + constraint2 + " in model2");
			}
			compareOneConstraint(amc1.getAttribute(null), amc2.getAttribute(null), message1, message2);
		} else if(constraint1 instanceof EEntity_constraint) {
			EEntity_constraint ec1 = (EEntity_constraint)constraint1;
			EEntity_constraint ec2 = (EEntity_constraint)constraint2;
			if(ec1.getDomain(null) != ec2.getDomain(null)) {
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  domains are different: " + constraint1 +
								   " in model1, and " + constraint2 + " in model2");
				}
			compareOneConstraint(ec1.getAttribute(null), ec2.getAttribute(null), message1, message2);
		} else if(constraint1 instanceof EAggregate_size_constraint) {
			EAggregate_size_constraint asc1 = (EAggregate_size_constraint)constraint1;
			EAggregate_size_constraint asc2 = (EAggregate_size_constraint)constraint2;
			if(asc1.getSize(null) != asc2.getSize(null)) {
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  sizes are different: " + constraint1 +
								   " in model1, and " + constraint2 + " in model2");
				}
			compareOneConstraint(asc1.getAttribute(null), asc2.getAttribute(null), message1, message2);
		} else if(constraint1 instanceof EPath_constraint) {
			EPath_constraint pc1 = (EPath_constraint)constraint1;
			EPath_constraint pc2 = (EPath_constraint)constraint2;
			compareOneConstraint(pc1.getElement1(null), pc2.getElement1(null), message1, message2);
			compareOneConstraint(pc1.getElement2(null), pc2.getElement2(null), message1, message2);
		} else if(constraint1 instanceof EInstance_constraint) {
			EInstance_constraint ic1 = (EInstance_constraint)constraint1;
			EInstance_constraint ic2 = (EInstance_constraint)constraint2;
			compareOneConstraint(ic1.getElement1(null), ic2.getElement1(null), message1, message2);
			compareOneConstraint(ic1.getElement2(null), ic2.getElement2(null), message1, message2);
		} else if(constraint1 instanceof EInverse_attribute_constraint) {
			EInverse_attribute_constraint iac1 = (EInverse_attribute_constraint)constraint1;
			EInverse_attribute_constraint iac2 = (EInverse_attribute_constraint)constraint2;
			compareOneConstraint(iac1.getInverted_attribute(null), iac2.getInverted_attribute(null),
								 message1, message2);
		} else if(constraint1 instanceof EType_constraint) {
			EType_constraint tc1 = (EType_constraint)constraint1;
			EType_constraint tc2 = (EType_constraint)constraint2;
			if(tc1.getDomain(null) != tc2.getDomain(null)) {
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  domains are different: " + constraint1 +
								   " in model1, and " + constraint2 + " in model2");
				}
			if(tc1.testConstraints(null) == tc2.testConstraints(null)) {
				if(tc1.testConstraints(null)) {
					compareOneConstraint(tc1.getConstraints(null), tc2.getConstraints(null),
										 message1, message2);
				}
			} else {
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  constraints is not set in one model: " + constraint1 +
									   " in model1, and " + constraint2 + " in model2");
			}
		} else if(constraint1 instanceof EIntersection_constraint) {
			EIntersection_constraint ic1 = (EIntersection_constraint)constraint1;
			EIntersection_constraint ic2 = (EIntersection_constraint)constraint2;
			AEntity subpaths1 = ic1.getSubpaths(null);
			AEntity subpaths2 = ic2.getSubpaths(null);
			SdaiIterator subpathIter1 = subpaths1.createIterator();
			SdaiIterator subpathIter2 = subpaths2.createIterator();
			boolean hasNext1, hasNext2;
			while((hasNext1 = subpathIter1.next()) && (hasNext2 = subpathIter2.next())) {
				EEntity subpath1 = subpaths1.getCurrentMemberEntity(subpathIter1);
				EEntity subpath2 = subpaths2.getCurrentMemberEntity(subpathIter2);
				compareOneConstraint(subpath1, subpath2, message1, message2);
			}
			while(hasNext1) {
				EEntity subpath1 = subpaths1.getCurrentMemberEntity(subpathIter1);
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  subpath only in model1: " + subpath1);
				hasNext1 = subpathIter1.next();
			}
			while(hasNext1) {
				EEntity subpath2 = subpaths2.getCurrentMemberEntity(subpathIter1);
				printMessageHeader(message1);
				printMessageHeader(message2);
				System.out.println("  subpath only in model2: " + subpath2);
				hasNext2 = subpathIter2.next();
			}
		} else if(constraint1 instanceof ENegation_constraint) {
			ENegation_constraint ic1 = (ENegation_constraint)constraint1;
			ENegation_constraint ic2 = (ENegation_constraint)constraint2;
			compareOneConstraint(ic1.getConstraints(null), ic2.getConstraints(null),
								 message1, message2);
		} else if(constraint1 instanceof EEnd_of_path_constraint) {
			EEnd_of_path_constraint eopc1 = (EEnd_of_path_constraint)constraint1;
			EEnd_of_path_constraint eopc2 = (EEnd_of_path_constraint)constraint2;
			compareOneConstraint(eopc1.getConstraints(null), eopc2.getConstraints(null),
								 message1, message2);
		} else if(constraint1 instanceof EAttribute) {
			if(constraint1 != constraint2) {
				System.out.println("  attributes are different: " + constraint1 +
								   " in model1, and " + constraint2 + " in model2");
			}
		} else {
			printMessageHeader(message1);
			printMessageHeader(message2);
			System.out.println("  Unknown constraint type: " + constraint1 +
							   " in model1, and " + constraint2 + " in model2");
		}
	}

    public void collectMissingComplexTypesOperation() throws SdaiException {
        System.out.println(" Collecting missing complex types");

        SdaiTransaction transaction = sdaiSession.startTransactionReadOnlyAccess();
        expressRepository = findRepository(repositoryName);
        expressRepository.openRepository();
        ASdaiModel models = expressRepository.getModels();
        for(SdaiIterator i = models.createIterator(); i.next(); ) {
        	SdaiModel mappingModel = models.getCurrentMember(i);
        	if(mappingModel.getUnderlyingSchema().getName(null).equals("MAPPING_SCHEMA")) {
                collectMissingComplexTypes(mappingModel);
        	}
        }
        transaction.endTransactionAccessAbort();
        expressRepository.closeRepository();
	}

	private void collectMissingComplexTypes(SdaiModel mappingModel) throws SdaiException {
		System.out.println("  for mapping " + mappingModel.getName());
		mappingModel.startReadOnlyAccess();
		ASdaiModel mappingModelDomain = new ASdaiModel();
		mappingModelDomain.addByIndex(1, mappingModel);

		ESchema_mapping schemaMapping =
			(ESchema_mapping) mappingModel.getInstances(ESchema_mapping.class).getByIndexEntity(1);
		SdaiModel targetModel = schemaMapping.getTarget(null).findEntityInstanceSdaiModel();
		final Map tEntitiesToCplxEntities = getEntitiesToCplxEntities(targetModel);
		SdaiModel sourceModel = schemaMapping.getSource(null).findEntityInstanceSdaiModel();
		final Map sEntitiesToCplxEntities = getEntitiesToCplxEntities(sourceModel);
		Map missingCplxTypes = new HashMap();
		for (Iterator i = tEntitiesToCplxEntities.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			EEntity_definition entDef = (EEntity_definition) entry.getKey();
			Collection cplxEntities = (Collection) entry.getValue();

			AEntity_mapping entityMappings = new AEntity_mapping();
			CEntity_mapping.usedinTarget(null, entDef, mappingModelDomain, entityMappings);
			for(SdaiIterator j = entityMappings.createIterator(); j.next(); ) {
				EEntity_mapping entityMapping = entityMappings.getCurrentMember(j);
				EEntity_definition srcEntity = entityMapping.getSource(null);
				Collection srcCplxEntities =
					(Collection)sEntitiesToCplxEntities.get(srcEntity);
				if(srcCplxEntities != null) {
					collectMissingComplexTypes(mappingModelDomain, sEntitiesToCplxEntities,
							entityMapping, cplxEntities, missingCplxTypes);
				}
			}
		}
		for (Iterator i = missingCplxTypes.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			System.out.print(entry.getKey().toString());
			System.out.print(" from ");
			System.out.println(entry.getValue());
		}
	}

	private static void collectMissingComplexTypes(ASdaiModel mappingModelDomain,
			Map sEntitiesToCplxEntities, EEntity_mapping entityMapping,
			Collection cplxEntities, Map missingCplxTypes) throws SdaiException {
		EEntity_definition sEntDef = entityMapping.getSource(null);
		EEntity_definition entDef = (EEntity_definition) entityMapping.getTarget(null);
		for (Iterator i = cplxEntities.iterator(); i.hasNext();) {
			EEntity_definition tCplxEntity = (EEntity_definition) i.next();
			AEntity leafEntDefs = tCplxEntity.getGeneric_supertypes(null);
			if(leafEntDefs.getMemberCount() != 2) {
//				System.out.println(new SdaiException(SdaiException.FN_NAVL,
//						"Complex types of more than two leaves are not supported: " + tCplxEntity).getMessage());
				continue;
			}
			for(SdaiIterator l = leafEntDefs.createIterator(); l.next(); ) {
				EEntity_definition leafEntDef =
					(EEntity_definition) leafEntDefs.getCurrentMemberEntity(l);
				if(leafEntDef != entDef) {
					AEntity_mapping leafEntMappings = new AEntity_mapping();
					CEntity_mapping.usedinTarget(null, leafEntDef, mappingModelDomain, leafEntMappings);
					for(SdaiIterator j = leafEntMappings.createIterator(); j.next(); ) {
						EEntity_mapping leafEntMapping = leafEntMappings.getCurrentMember(j);
						EEntity_definition sLeafEntDef = leafEntMapping.getSource(null);
						if(sEntDef != sLeafEntDef) {
							EEntity_definition cplxEntity = findCplxEntity(sEntitiesToCplxEntities,
									new EEntity_definition[] { sEntDef, sLeafEntDef});
							if(cplxEntity == null) {
								Set missingCplxType = new HashSet();
								missingCplxType.add(sEntDef.getName(null));
								missingCplxType.add(sLeafEntDef.getName(null));
								Map fromTypes =
									(Map) missingCplxTypes.get(missingCplxType);
								if(fromTypes == null) {
									fromTypes = new HashMap();
									missingCplxTypes.put(missingCplxType, fromTypes);
								}
								Collection fromTypesMappings =
									(Collection) fromTypes.get(tCplxEntity.getName(null));
								if(fromTypesMappings == null) {
									fromTypesMappings = new HashSet();
									fromTypes.put(tCplxEntity.getName(null), fromTypesMappings);
								}
								fromTypesMappings.add(entityMapping.getPersistentLabel());
								fromTypesMappings.add(leafEntMapping.getPersistentLabel());
//								System.out.println(sEntDef.getName(null) + "+" + sLeafEntDef.getName(null) +
//										" from " + tCplxEntity.getName(null));
							}
						}
					}
				}
			}
		}
	}

	private Map getEntitiesToCplxEntities(SdaiModel dictModel) throws SdaiException {
		Map entitiesToCplxEntities = new HashMap();
		AEntity_declaration declarations =
			(AEntity_declaration) dictModel.getInstances(EEntity_declaration.class);
		for(SdaiIterator i = declarations.createIterator(); i.next(); ) {
			EEntity_declaration declaration = declarations.getCurrentMember(i);
			EEntity_definition entityDef =
				(EEntity_definition) declaration.getDefinition(null);
			if(entityDef.getComplex(null)) {
				AEntity supertypes = entityDef.getGeneric_supertypes(null);
				for(SdaiIterator j = supertypes.createIterator(); j.next(); ) {
					EEntity_definition supertype =
						(EEntity_definition) supertypes.getCurrentMemberEntity(j);
					Collection cplxEntities =
						(Collection) entitiesToCplxEntities.get(supertype);
					if(cplxEntities == null) {
						cplxEntities = new ArrayList();
						entitiesToCplxEntities.put(supertype, cplxEntities);
					}
					cplxEntities.add(entityDef);
				}
			}
		}
		return entitiesToCplxEntities;
	}

	private static EEntity_definition findCplxEntity(Map entitiesToCplxEntities,
			EEntity_definition[] entDefs) throws SdaiException {
		Collection cplxEntities =
			(Collection) entitiesToCplxEntities.get(entDefs[0]);
		if(cplxEntities != null) {
			for (Iterator i = cplxEntities.iterator(); i.hasNext();) {
				EEntity_definition cplxEntDef = (EEntity_definition) i.next();
				AEntity supertypes = cplxEntDef.getGeneric_supertypes(null);
				int foundCnt = 0;
				if(supertypes.getMemberCount() == entDefs.length) {
					for(SdaiIterator j = supertypes.createIterator(); j.next(); ) {
						EEntity_definition supertype =
							(EEntity_definition) supertypes.getCurrentMemberEntity(j);
						for (int k = 0; k < entDefs.length; k++) {
							if(supertype == entDefs[k]) {
								foundCnt++;
								break;
							}
						}
					}
					if(foundCnt == entDefs.length) {
						return cplxEntDef;
					}
				}
			}
		}
		return null;
	}

	private void printMessageHeader(StringBuffer message) {
		if(message.length() != 0) {
			System.out.println(message.toString());
			message.delete(0, Integer.MAX_VALUE);
		}
	}

	/** Finds repository with given name
     * @param findRepName repository name to find
     * @throws SdaiException Exception RP_NAVL in thrown if repository with this name is not found.
     * Other exceptions can be thrown as well.
     * @return Repostitory which was found
     */
    public SdaiRepository findRepository(String findRepName) throws SdaiException {
        ASdaiRepository repositories = sdaiSession.getKnownServers();
        SdaiIterator repIterator = repositories.createIterator();
        while(repIterator.next()) {
            SdaiRepository repository = repositories.getCurrentMember(repIterator);
            String repositoryName = repository.getName();
            if(repositoryName.equals(findRepName))
                return repository;
        }
        throw new SdaiException(SdaiException.RP_NAVL, sdaiSession,
                                "Repository not found: " + findRepName);
    }

    /** Finds model with given name
     * @param repository Repository to find model in.
     * @param findModelName model name to find
     * @throws SdaiException Exception MO_NVLD in thrown if model with this name is not found.
     * Other exceptions can be thrown as well.
     * @return Model which was found
     */
    public SdaiModel findModel(SdaiRepository repository, String findModelName) throws SdaiException {
        SdaiModel model = testAndFindModel(repository, findModelName);
        if(model != null) return model;
        throw new SdaiException(SdaiException.MO_NVLD, repository,
                                "Model not found: " + findModelName);
    }

    public SdaiModel testAndFindModel(SdaiRepository repository, String findModelName)
    throws SdaiException {
        ASdaiModel models = repository.getModels();
        SdaiIterator modelIterator = models.createIterator();
        while(modelIterator.next()) {
            SdaiModel model = models.getCurrentMember(modelIterator);
            String modelName = model.getName();
            if(modelName.equals(findModelName))
                return model;
        }
        return null;
    }

    public SchemaInstance findSchemaInstance(SdaiRepository repository, String findInstanceName)
    throws SdaiException {
        SchemaInstance schemaInstance = repository.findSchemaInstance(findInstanceName);
        if(schemaInstance != null) return schemaInstance;
        throw new SdaiException(SdaiException.SI_NEXS, repository,
                                "Schema instance not found: " + findInstanceName);
    }

    public void getConfiguration() {
        DocumentBuilderFactory factory =
           DocumentBuilderFactory.newInstance();
        try {
           DocumentBuilder builder = factory.newDocumentBuilder();
           configDocument = builder.parse( new InputSource(MappingData.class
                                           .getResource("MappingDataConf.xml").toString()) );
        } catch (SAXParseException spe) {
           // Error generated by the parser
           System.out.println ("\n** Parsing error"
              + ", line " + spe.getLineNumber ()
              + ", uri " + spe.getSystemId ());
           System.out.println("   " + spe.getMessage() );

           // Use the contained exception, if any
           Exception  x = spe;
           if (spe.getException() != null)
               x = spe.getException();
           x.printStackTrace();

        } catch (SAXException sxe) {
           // Error generated by this application
           // (or a parser-initialization error)
           Exception  x = sxe;
           if (sxe.getException() != null)
               x = sxe.getException();
           x.printStackTrace();

        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();

        } catch (IOException ioe) {
           // I/O error
           ioe.printStackTrace();
        }
    }

    public String capitalizeString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

}
