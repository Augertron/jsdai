package jsdai.mappingUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.ADefined_type;
import jsdai.SExtended_dictionary_schema.AEntity_definition;
import jsdai.SExtended_dictionary_schema.AExplicit_attribute;
import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EArray_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EBag_type;
import jsdai.SExtended_dictionary_schema.EBoolean_type;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EEnumeration_type;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInteger_type;
import jsdai.SExtended_dictionary_schema.EList_type;
import jsdai.SExtended_dictionary_schema.ELogical_type;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SExtended_dictionary_schema.ENumber_type;
import jsdai.SExtended_dictionary_schema.EReal_type;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESet_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SExtended_dictionary_schema.EString_type;
import jsdai.SMapping_schema.AAttribute_mapping_path_select;
import jsdai.SMapping_schema.AEntity_mapping;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EAttribute_mapping_boolean_value;
import jsdai.SMapping_schema.EAttribute_mapping_enumeration_value;
import jsdai.SMapping_schema.EAttribute_mapping_int_value;
import jsdai.SMapping_schema.EAttribute_mapping_logical_value;
import jsdai.SMapping_schema.EAttribute_mapping_real_value;
import jsdai.SMapping_schema.EAttribute_mapping_string_value;
import jsdai.SMapping_schema.EDerived_variant_entity_mapping;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EGeneric_attribute_mapping;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.EPath_constraint;
import jsdai.SMapping_schema.ESchema_mapping;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.lang.AEntity;
import jsdai.lang.AEntityExtent;
import jsdai.lang.ASdaiModel;
import jsdai.lang.ASdaiRepository;
import jsdai.lang.EEntity;
import jsdai.lang.EntityExtent;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.util.SimpleOperations;
//import jsdai.dictionary.*;

abstract class ExtUtilMethods {

  /**
   * The name of currently being generated Mx class - this is used
   * to generate nicer output in Mx classes source code exceptions.
   */
  protected String currentlyGeneratedClassName;

  protected final String LABEL_OF_INSERT_START = "<!--ADDON STARTS HERE-->";
  protected SdaiModel dictModel = null;
  protected SdaiModel mapModel = null;
  protected ASdaiModel mappingDomain = null;

  protected Hashtable systemRepository = new Hashtable();
  protected String[] excludeSchemaName = { "AP214_ARM", "AP210_ARM", "AP212_ARM", "AP225_ARM", "AP203_ARM" };

  protected String mainSchemaName = "";
  protected String docLocationRoot = new String("D:\\cvs_projects\\api_ref_dev");

  public boolean isGeneratingDocumentation = false;
  public boolean isHidingWriteOperations = false;

  protected final String COMMON_METHODS_GROUP_NAME = "Common methods";
  protected ClassDocumentation doc = null;

  // class generator can generate classes for one AP at a time, this variable stores currently
  // active AP number (f.e., 214 or 210, etc.)
  protected String apNumber = "";

  protected Hashtable importStatements = new Hashtable();
  protected String classDeclaration = "";
  protected Vector entityDefinitions = new Vector();
  protected Vector entityMappings = new Vector();
  protected Vector attributes = new Vector();
  protected String classConstructor = "";
  protected Vector classMethods = new Vector();
  protected String classClosingBracket = "";
  protected Hashtable staticConstants = new Hashtable();

  protected EEntity_definition arm_entities[];

  // stores entity mappings, allowed for currently processed entity_definition.
  protected AEntity_mapping entityMappingsInIteration = null;
  // stores explicit attributes of currently processed entity_definition.
  protected AExplicit_attribute entityAttributesInIteration = null;
  // stores generic_attribute_mappings in Vector. Vector is retrievable by key,
  // key is attribute name.
  protected Hashtable attrMappingsInIteration = new Hashtable();
  // stores explicit_attributes, which have usedin method generated for them.
  // used in generateUsers method.
  protected Vector usedinAttributesInIteration = null;
  // key is attribute name, value is hashtable X. Key for hashtable X is
  // aim entity type name (instance of this aim entity type can be used to get
  // a value of appropriate attribute), and value in hashtable X is type name of
  // overload parameter in getXX methods.
  protected Hashtable attrMethodsInIteration = new Hashtable();

  // directory, where generated files should be placed
  protected String generatedTargetDir = "";
  // variable allows to control whether 'main' function should be added to generated class.
  protected boolean generateMainFunction = false;

  public ExtUtilMethods(String apPartNumber, String genTargetDir, String mainSchema, boolean genMainFunction, String docSourcePath) throws SdaiException,
      Exception {
    try {
      mainSchemaName = mainSchema;
      apNumber = apPartNumber;
      if (genTargetDir != null) {
        if (genTargetDir.endsWith(File.separator)) {
          genTargetDir = genTargetDir.substring(0, genTargetDir.length() - 1);
        }
      }
      generatedTargetDir = genTargetDir;
      if (docSourcePath != null) {
        if (docSourcePath.endsWith(File.separator)) {
          docSourcePath = docSourcePath.substring(0, docSourcePath.length() - 1);
        }
      }
      docLocationRoot = docSourcePath;
      generateMainFunction = genMainFunction;
      // find apxxx mapping data model:
      String armSchema = "AP" + apNumber + "_ARM";
      mapModel = findMappingModel(armSchema);
      SimpleOperationsOnExtended.enshureReadOnlyModel(mapModel);

      dictModel = findDictionaryModel(armSchema);
      SimpleOperationsOnExtended.enshureReadOnlyModel(dictModel);

      // now get entity_definition's.
      arm_entities = ClassesGeneratorExtendedDictAccessFunctions.getEntitiesOfSchema(dictModel);

      // now remember mapping domain:
      mappingDomain = new ASdaiModel();
      mappingDomain.addByIndex(1, mapModel, null);

      doc = new ClassDocumentation(apNumber, LABEL_OF_INSERT_START);
    }
    catch (SdaiException exc) {
      processMessage(exc);
    }
  }

  protected void generateHtmlDocumentation(EEntity_definition entDef) throws Exception {
    if (docLocationRoot == null) {
      return;
    }
    // first, find file, that should be appended with new information:
    String entDisplayName = getEntDefTargetName(entDef, true);
    String targetFileName = "jsdai" + File.separator + "SAp" + apNumber + "_arm" + File.separator + entDisplayName + ".html";
    File targetFile = new File(docLocationRoot + File.separator + targetFileName);
    if (!targetFile.exists()) {
      throw new Exception("Target file for documentation was not found! File name tried: " + docLocationRoot + File.separator + targetFileName);
    }
    // construct file reader and read contents of existing file:
    FileReader readTargetFile = new FileReader(docLocationRoot + File.separator + targetFileName);
    StringBuffer buffer = new StringBuffer();
    char fileChar = '0';
    int retValue = 0;
    while (retValue != -1) {
      retValue = readTargetFile.read();
      if (retValue != -1) {
        fileChar = (char) retValue;
        buffer.append(fileChar);
      }
    }
    readTargetFile.close();

    // now, remove ending entries for tags </html> and </body>
    String fullOriginalFile = buffer.toString();
    int htmlEndIndex = fullOriginalFile.lastIndexOf("</HTML>");
    if (htmlEndIndex == -1) {
      throw new Exception("Closing tag </HTML> was not found in given file!");
    }
    fullOriginalFile = fullOriginalFile.substring(0, htmlEndIndex);
    int htmlBodyEndIndex = fullOriginalFile.lastIndexOf("</BODY>");
    if (htmlBodyEndIndex == -1) {
      throw new Exception("Closing tag </BODY> was not found in given file!");
    }
    fullOriginalFile = fullOriginalFile.substring(0, htmlBodyEndIndex);
    // now, check whether file was modified by us already:
    int htmlLabelIndex = fullOriginalFile.lastIndexOf(LABEL_OF_INSERT_START);
    if (htmlLabelIndex != -1) {
      fullOriginalFile = fullOriginalFile.substring(0, htmlLabelIndex);
    }

    // now, we can output our own information:
    FileWriter writeTargetFile = new FileWriter(docLocationRoot + File.separator + targetFileName);
    writeTargetFile.write(fullOriginalFile);
    doc.printClassDocumentation(writeTargetFile, docLocationRoot);
    // add removed tags back again and write whole stuff to file:
    writeTargetFile.write("</BODY>\n</HTML>\n");
    writeTargetFile.close();
  }

  /**
   *
   */
  protected String makeValidName(String name) {
    String retValue = new String(name);
    retValue = retValue.replace('+', '$');
    return retValue;
  }

  /**
   *
   */
  protected String capitalize(String name) {
    StringBuffer nameBuffer = new StringBuffer(name);
    nameBuffer.setCharAt(0, nameBuffer.substring(0, 1).toUpperCase().charAt(0));
    String retName = nameBuffer.toString();
    return retName;
  }

  /**
   * if the last parameter is set to true, display-ready name shall be generated.
   */
  protected String getEntDefTargetName(EEntity_definition entDef, boolean readyForDisplay) throws SdaiException {
    String targetName = entDef.getName(null);
    if (readyForDisplay) {
      targetName = makeValidName(capitalize(targetName));
    }
    return targetName;
  }

  /**
   * if the last parameter is set to true, display-ready name shall be generated.
   */
  protected String getEntMapTargetName(EEntity_mapping entMap, boolean readyForDisplay) throws SdaiException, Exception {
    EEntity target = entMap.getTarget(null);
    if (!(target instanceof EEntity_definition)) {
      throw new Exception("Entity mapping target is not entity_definition! Don't know how to respond.");
    }
    EEntity_definition targetEntDef = (EEntity_definition) target;
    return getEntDefTargetName(targetEntDef, readyForDisplay);
  }

  /**
   *
   */
  protected String getDataTypeName(EGeneric_attribute_mapping atrMapping, boolean readyForDisplay) throws SdaiException, Exception {
    String dataTypesName = "";
    if (atrMapping.testData_type(null)) {
      ANamed_type namedTypes = atrMapping.getData_type(null);
      SdaiIterator it = namedTypes.createIterator();
      if (it.next()) {
        ENamed_type namedType = (ENamed_type) namedTypes.getCurrentMemberObject(it);
        dataTypesName = namedType.getName(null);
        if (readyForDisplay) {
          dataTypesName = capitalize(dataTypesName);
        }
        // check if there are several named_types set:
        if (namedTypes.getMemberCount() > 1) {
          throw new Exception("Several named data types in data_types list detected!");
        }
      }
    }
    return dataTypesName;
  }

  /**
   *
   */
  protected String getTargetSchemaMappingName(SdaiModel model) throws SdaiException, Exception {
    SimpleOperationsOnExtended.enshureReadOnlyModel(model);
    AEntityExtent workFolders = model.getFolders();
    SdaiIterator mIt = workFolders.createIterator();
    while (mIt.next()) {
      EntityExtent entExtent = (EntityExtent) workFolders.getCurrentMemberObject(mIt);
      String defString = entExtent.getDefinitionString();
      if (!defString.equalsIgnoreCase("schema_mapping")) {
        continue;
      }
      AEntity entDefInstances = entExtent.getInstances();
      SdaiIterator edIt = entDefInstances.createIterator();
      while (edIt.next()) {
        // there should be only one instance.
        ESchema_mapping ee_mapping = (ESchema_mapping) entDefInstances.getCurrentMemberObject(edIt);
        ESchema_definition target = ee_mapping.getTarget(null);
        String targetSchemaName = target.getName(null);
        return targetSchemaName;
      }
    }
    throw new Exception("Target schema in schema_mapping for model " + model + " is not available!");
  }

  /**
   * Please note: this method is constructed per empirical basis, experimenting with ap214
   * data; if you get an exception during runtime with some other data, please extend this
   * method further, as needed.
   */
  protected EEntity determineFinalTargetTypeForUndefinedObject(ENamed_type armAtrFinalType, EEntity pathElement, EExplicit_attribute expAtr)
      throws SdaiException, Exception {
    EEntity furtherElement = null;
    if (pathElement instanceof EPath_constraint) {
      //System.out.println("I have path constraint"+pathElement);
      EPath_constraint pcPath = (EPath_constraint) pathElement;
      furtherElement = pcPath.getElement1(null);
      return determineFinalTargetTypeForUndefinedObject(armAtrFinalType, furtherElement, expAtr);
    }
    if (pathElement instanceof EInverse_attribute_constraint) {
      //System.out.println ("I have inverse atr constraint"+pathElement);
      EInverse_attribute_constraint iacPath = (EInverse_attribute_constraint) pathElement;
      furtherElement = iacPath.getInverted_attribute(null);
      return determineFinalTargetTypeForUndefinedObject(armAtrFinalType, furtherElement, expAtr);
    }
    if (pathElement instanceof EAggregate_member_constraint) {

      EAggregate_member_constraint amcPath = (EAggregate_member_constraint) pathElement;
      furtherElement = amcPath.getAttribute(null);
      return determineFinalTargetTypeForUndefinedObject(armAtrFinalType, furtherElement, expAtr);
    }
    if (pathElement instanceof EAttribute) {
      EAttribute aPath = (EAttribute) pathElement;
      // read parent entity_definition:
      return aPath.getParent(null);
    }
    if (pathElement instanceof EEntity_constraint) {
      EEntity_constraint ecPath = (EEntity_constraint) pathElement;
      // take domain:
      return ecPath.getDomain(null);
    }
    if (pathElement instanceof ESelect_constraint) {
      ESelect_constraint scPath = (ESelect_constraint) pathElement;
      // take values which should lead us through selects, use the last available value:
      ADefined_type dtList = scPath.getData_type(null);
      SdaiIterator it = dtList.createIterator();
      it.end();
      it.previous();
      furtherElement = dtList.getCurrentMember(it);
      return SimpleOperationsOnExtended.getDefined_typeDomain((EDefined_type) furtherElement);
    }
    return armAtrFinalType;
  }

  /**
   *
   */
  private EEntity findFinalTypeFromPath(EAttribute_mapping testAtrMapping, EEntity armAtrFinalType, EExplicit_attribute expAtr) throws SdaiException,
      Exception {
    EEntity finalType = null;
    // mapping path is optional attribute, so be carefull:
    if (testAtrMapping.testPath(null)) {
      AAttribute_mapping_path_select agPath = testAtrMapping.getPath(null);
      int membCount = agPath.getMemberCount();
      EEntity last = agPath.getByIndex(membCount);
      if (last instanceof EAttribute) {
        finalType = SimpleOperationsOnExtended.getAttributeDomain((EAttribute) last);
      }
      else {
        // now, parse path_constraint only when we deal with undefined_object:
        if (armAtrFinalType instanceof ENamed_type) {
          ENamed_type ntFinalType = (ENamed_type) armAtrFinalType;
          if (ntFinalType.getName(null).equalsIgnoreCase("undefined_object")) {
//							System.out.println("parsing undefined_object, when mapping path ends not with"+
//								" EAttribute."+last+atrMapping);
            finalType = determineFinalTargetTypeForUndefinedObject(ntFinalType, last, expAtr);
          }
        }
      }
      if (finalType == null) {
        System.out.println("finalType is null. Trying to recover." + armAtrFinalType.getClass());
        // now, if finalType is null- we have nothing to do except for rely on parsing path:
        if (armAtrFinalType instanceof ENamed_type) {
          System.out.println("It will be recovered");
          finalType = determineFinalTargetTypeForUndefinedObject((ENamed_type) armAtrFinalType, last, expAtr);
        }
        else {
          System.out.println("finalType was not recovered! The method will probably be generated" + "incorrectly.");
        }
      }
    }
    else {
      System.out.println("BE CAREFULL! Mapping path is not set. An exception is expected.");
    }
    return finalType;
  }

  /*
   * An equalent of determineFinalTargetType, except that it's not working with undefined_object!
   */
  protected String[] determineFinalTgTypeForDvem(EDerived_variant_entity_mapping dvem, Hashtable allowedTypes) throws SdaiException, Exception {
    EEntity finalType = null;
    EEntity_mapping relatedEm = dvem.getRelated(null);
    finalType = getFinalType(relatedEm);
    String[] retTypes = null;
    if (finalType == null) {
      // let's keep this situation open and generate a warning:
      System.out.println("ERROR! DVEM based final type is recognized as null: generator not yet supports this!");
      System.out.println("dvem:" + dvem);
      Thread.dumpStack();
      return retTypes;
    }
    String suggestedType = convertTypeToString(finalType);
    if (!suggestedType.equalsIgnoreCase("ESelect_type")) {
      retTypes = new String[1];
      retTypes[0] = suggestedType;
      allowedTypes.put(suggestedType, finalType);
    }
    else {
      retTypes = parseESelect_type(finalType, allowedTypes);
    }
    return retTypes;
  }

  /**
   *
   */
  protected String[] determineFinalTargetType(EEntity armAtrFinalType, EGeneric_attribute_mapping atrMapping, EExplicit_attribute expAtr,
      Hashtable allowedTypes) throws SdaiException, Exception {
    //System.out.println("attr mapping is "+atrMapping);
    // let's declare variable for storing final type of mapping op.:
    EEntity finalType = armAtrFinalType;

    // let us prepare for cases of undefined_object
    ENamed_type ntFinalType = null;
    if (armAtrFinalType instanceof ENamed_type) {
      ntFinalType = (ENamed_type) armAtrFinalType;
    }

    // there are some cases, when mapping path simply does not exist. When this
    // circumstance is appended with undefined_object as armAtrFinalType, then
    // we must assign to finalType the reference to entity_definition on AIM side,
    // which is retrieved from atrMapping->entity_mapping->target. I.e., this would
    // be reference to instance itself.

    // the following IF statement will work for cases, when mapping path does not
    // exist.
    if (ntFinalType != null) {
      if (ntFinalType.getName(null).equalsIgnoreCase("undefined_object")) {
        // get entity_mapping:
        EEntity_mapping eMap = atrMapping.getParent_entity(null);
        // now, get target from it:
        finalType = eMap.getTarget(null);
      }
    }

    if (atrMapping instanceof EAttribute_mapping) {
      EAttribute_mapping testAtrMapping = (EAttribute_mapping) atrMapping;
      // if domain is set, then use it. if not set, try to reuse previous
      // implementation.
      if (!testAtrMapping.testDomain(null)) {
        finalType = findFinalTypeFromPath(testAtrMapping, armAtrFinalType, expAtr);
      }
      else {
        // take domain:
        EEntity domain = testAtrMapping.getDomain(null);
        finalType = getFinalType(domain);
      }
      // in case when domain is not set and mapping path does not exist, try to use
      // data_type to get info about final type:
      if (finalType == null) {
        if (testAtrMapping.testData_type(null)) {
          ANamed_type dataType = testAtrMapping.getData_type(null);
          // use only the first
          if (dataType.getMemberCount() != 1) {
            throw new Exception("Too many elements in data_type!");
          }
          EEntity domain = dataType.getByIndex(1);
          finalType = getFinalType(domain);
        }
      }
    }
    if (finalType == null) {
      System.out.println("atrMapping is " + atrMapping);
    }
    String[] retTypes = null;
    String suggestedType = convertTypeToString(finalType);
    if (!suggestedType.equalsIgnoreCase("ESelect_type")) {
      retTypes = new String[1];
      retTypes[0] = suggestedType;
      allowedTypes.put(suggestedType, finalType);
    }
    else {
      retTypes = parseESelect_type(finalType, allowedTypes);
    }

    return retTypes;
  }

  protected String[] parseESelect_type(EEntity selectType, Hashtable allowedTypes) throws SdaiException, Exception {
    // this hashtable actually duplicates vector useTypes, but it speeds up the lookup.
    Hashtable knownTypes = new Hashtable();
    Vector useTypes = new Vector();
    String[] retValues = null;

    readTypeRecursive(selectType, knownTypes, useTypes, allowedTypes);

    int count = useTypes.size();
    retValues = new String[count];
    for (int i = 0; i < count; i++) {
      retValues[i] = (String) useTypes.get(i);
    }
    return retValues;
  }

  protected void readTypeRecursive(EEntity type, Hashtable knownTypes, Vector useTypes, Hashtable allowedTypes) throws SdaiException, Exception {
    if (type instanceof ESelect_type) {
      ESelect_type es_type = (ESelect_type) type;
// 			if (!es_type.testSelections(null)) {
// 				throw new Exception ("I believe a problem in mapping data was found! Please check and correct if required.");
// 				//return; // recursion complete with error.
// 			}
      ANamed_type selections = es_type.getSelections(null);
      SdaiIterator it = selections.createIterator();
      while (it.next()) {
        ENamed_type en_type = (ENamed_type) selections.getCurrentMemberObject(it);
        readTypeRecursive(en_type, knownTypes, useTypes, allowedTypes);
      }
      return;
    }
    if (type instanceof EDefined_type) {
      EDefined_type ed_type = (EDefined_type) type;
      if (!ed_type.testDomain(null)) {
        throw new Exception("I believe a problem in mapping data was found! Please check and correct if required.");
      }
      EEntity domain = ed_type.getDomain(null);
      readTypeRecursive(domain, knownTypes, useTypes, allowedTypes);
      return;
    }
    if (type instanceof EEntity_definition) {
      String entDefRepresentation = convertTypeToString(type);
      if (!knownTypes.containsKey(entDefRepresentation)) {
        knownTypes.put(entDefRepresentation, entDefRepresentation);
        useTypes.add(entDefRepresentation);
        allowedTypes.put(entDefRepresentation, type);
      }
      return; // recursion complete.
    }
    if (type instanceof ESimple_type) {
      String entDefRepresentation = convertTypeToString(type);
      if (!knownTypes.containsKey(entDefRepresentation)) {
        knownTypes.put(entDefRepresentation, entDefRepresentation);
        useTypes.add(entDefRepresentation);
        allowedTypes.put(entDefRepresentation, type);
      }
      return; // recursion successfully completed.
    }
    if (type instanceof EAggregation_type) {
      EAggregation_type ea_type = (EAggregation_type) type;
      if (!ea_type.testElement_type(null)) {
        throw new Exception("I believe a problem in mapping data was found! Please check and correct if required.");
      }
      EEntity element_type = ea_type.getElement_type(null);
      readTypeRecursive(element_type, knownTypes, useTypes, allowedTypes);
      return;
    }
    if (type instanceof EEnumeration_type) {
      String entDefRepresentation = convertTypeToString(type);
      if (!knownTypes.containsKey(entDefRepresentation)) {
        knownTypes.put(entDefRepresentation, entDefRepresentation);
        useTypes.add(entDefRepresentation);
        allowedTypes.put(entDefRepresentation, type);
      }
      return; // recursion successfully completed.
    }
    System.out.println("Unforseen case was found:" + type);
    throw new Exception("I believe a problem in mapping data was found, or generator needs be updated!" + " Please check and correct if required.");
  }

  /**
   * @param convertType This parameter represents the type in jsdai, and we must find
   *                    appropriate representation for it in Java.
   */
  protected String convertTypeToString(EEntity convertType) throws SdaiException, Exception {
    if (convertType instanceof EEntity_definition) {
      // be as specific as possible:
      EEntity_definition ed_type = (EEntity_definition) convertType;
      if (!ed_type.testName(null)) {
        return "EEntity";
      }

      if (!ed_type.testComplex(null)) {
        return "EEntity";
      }
      if (ed_type.getComplex(null)) {
        //System.out.println("Complex class is returned.");
        return "EEntity"; // because there is no other common denominator
        /*
         * // because for ap210 and ap214 cases complex mixed class are never returned,
         * // there is no chance to test how it works. So throwing an exception to force
         * // programmer to test this functionality, when it appears.
         * throw new Exception("Complex mixed type is used as return type for getXX method!"+
         * " Never seen it before, so please test this case carefully.");
         */
      }

      String result = ed_type.getName(null);
      return result;
    }
    if (convertType instanceof EString_type) {
      return "String";
    }
    if (convertType instanceof EBoolean_type)
    //return "EEntity"; // at the moment, a typical return value from getMappedAttribute in case
    // of boolean value is instance of EEntity. This might be treated as bug,
    // but let's adopt to this for now.
    {
      return "boolean"; // This is with newer support for mapping operations(year 2002, 4Q)
    }
    if (convertType instanceof EReal_type) {
      return "double"; // NOTE: not a big double is returned- just double.
    }
    if (convertType instanceof ESelect_type) {
      return "ESelect_type";
    }
    if (convertType instanceof EInteger_type) {
      return "int";
    }
    if (convertType instanceof EEnumeration_type) {
      return "String"; // attribute with enumeration_type will be read with getMappedAttribute as
    }
    // String, and nothing else.
    if (convertType instanceof ENumber_type) {
      return "Number";
    }
    if (convertType instanceof ESet_type) {
      return "EEntity";
    }
    if (convertType instanceof EBag_type) {
      return "EEntity";
    }
    if (convertType instanceof EList_type) {
      return "EEntity";
    }
    if (convertType instanceof EArray_type) {
      return "EEntity";
    }
    if (convertType instanceof ELogical_type)
    //return "EEntity"; This was with older support for mapping operations(year 2001 2nd Q)
    {
      return "int"; // This is with newer support for mapping operations (year 2002 4th Q)
    }

    throw new Exception("Unknown type " + convertType + " for converting to String representation!");
  }

  protected boolean isComplexEntityType(EEntity_mapping entMapping) throws SdaiException, Exception {
    // get target of this entity mapping and query it whether it is of complex type:
    EEntity target = entMapping.getTarget(null);
    if (!(target instanceof EEntity_definition)) {
      throw new Exception("Entity mapping target is not entity_definition! Don't know how to respond.");
    }
    EEntity_definition targetEntDef = (EEntity_definition) target;
    if (targetEntDef.getComplex(null)) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *
   */
  public static void processMessage(SdaiException exception) {
    exception.printStackTrace();
  }

  /**
   * This method accesses system repository and moves its schemas and entity definitions
   * from schemas to hashtable. It is used later to determine where given entity definition
   * name belongs very fast.
   * Important note: both schema names and entity definition names are stored in upper case
   * only, so when using built hashtable be attentive.
   */
  void buildSchemaContentsTable() throws SdaiException, Exception {
    ClassesGeneratorExtendedDictAccessFunctions.buildSchemaContentsTable(systemRepository, mainSchemaName);
  }

  protected boolean isArmEntityName(String testName, Hashtable allowedTypes, AEntity_definition outAgEntDef) throws SdaiException, Exception {
    // we have dictionary data model loaded already, so just take it and check there:
    SdaiModel model = dictModel;
//		ESchema_definition schema = null;
    EEntity_definition ent_def = null;
    SimpleOperations.enshureReadOnlyModel(model);
//		schema = model.getDefinedSchema();
//		if (schema == null)
//			throw new Exception ("Erroneous dictionary model was found: schema is null!");//avoid malformed models
//		String schemaName = schema.getName(null);
//		schemaName = schemaName.toUpperCase();
    // okay, get all entity extents:
    AEntityExtent modelExtents = model.getFolders();
    // now, find in there entity_definition entry:
    SdaiIterator eeIt = modelExtents.createIterator();
    while (eeIt.next()) {
      EntityExtent entExtent = (EntityExtent) modelExtents.getCurrentMemberObject(eeIt);
      String defString = entExtent.getDefinitionString();
      if (!defString.equalsIgnoreCase("entity_definition")) {
        continue;
      }
      AEntity entDefInstances = entExtent.getInstances();
      SdaiIterator edIt = entDefInstances.createIterator();
      while (edIt.next()) {
        ent_def = (EEntity_definition) entDefInstances.getCurrentMemberObject(edIt);
        if (ent_def.getName(null).equalsIgnoreCase(testName)) {
          // before making final decision, must consult given allowedTypes hashtable:
          // it's possible that the same name exists in AIM and ARM- and this hashtable
          // allows us to avoid wrong decisions.
          EEntity consultInstance = (EEntity) allowedTypes.get(testName);
          if (consultInstance == ent_def) {
            // given testName is arm entity name, so can return now
            outAgEntDef.addUnordered(ent_def);
            return true;
          }
          else
          // sorry, must proceed
          {
            continue;
          }
        }
      }
    }
    return false;
  }

  /**
   * @param aim_entity_def_name  Name of aim entity_definition to be searched for
   * @param preferredSchemaNames Table of preferred schema names(list of existing ones);
   *                             Method first tries to determine
   *                             whether given aim entity def presents in preferred schemas list.
   * @return The name of recommended schema or empty string if nothing
   * suitable was found.
   */
  protected String findFullClassName(String aim_entity_def_name, Hashtable preferredSchemaNames) throws SdaiException, Exception {
    // first let's retrieve hashtables for each preferred schema name and look for given search name
    // there. If not found, then iterate through every hashtable entry and look there
    String searchString = aim_entity_def_name.toUpperCase();

    Enumeration schemaNames = preferredSchemaNames.keys();
    while (schemaNames.hasMoreElements()) {
      String schemaName = (String) schemaNames.nextElement();
      schemaName = schemaName.toUpperCase();
      if (systemRepository.containsKey(schemaName)) {
        Hashtable schemaContents = (Hashtable) systemRepository.get(schemaName);
        if (schemaContents.containsKey(searchString)) {
          String result = capitalize(schemaName.toLowerCase());
          return result;
        }
      }
    }
    // no, requested definition is not in preferred list. Let's visit all schemas:
    String candidateSchemaName = null;
    Enumeration allSchemasNames = systemRepository.keys();
    while (allSchemasNames.hasMoreElements()) {
      String schemaName = (String) allSchemasNames.nextElement();
      Hashtable schemaContents = (Hashtable) systemRepository.get(schemaName);
      if (schemaContents.containsKey(searchString)) {
        if (candidateSchemaName != null) {
          throw new Exception("Too many candidates found for entity " + aim_entity_def_name + " :" + candidateSchemaName + " and " + schemaName);
        }
        else {
          candidateSchemaName = schemaName;
          continue;
        }
      }
    }
    if (candidateSchemaName != null) {
      String result = capitalize(candidateSchemaName.toLowerCase());
      return result;
    }
    throw new Exception("No owner schema was found for given entity definition " + aim_entity_def_name);
  }

  /**
   * Recursive method finds all supertypes of given entity definition and adds them to given hashtable
   * for processing it somewhere outside this method.
   */
  protected void addSupertypesToHashtable(Hashtable commonSupertypes, EEntity_definition targetEntDef, Hashtable relatedEntityDefs, int initialValue)
      throws SdaiException, Exception {
    String ent_name = getEntDefTargetName(targetEntDef, false);

    if (!commonSupertypes.containsKey(ent_name)) {
      commonSupertypes.put(ent_name, new Integer(initialValue));
      relatedEntityDefs.put(ent_name, targetEntDef);
    }
    else {
      Integer value = (Integer) commonSupertypes.get(ent_name);

      commonSupertypes.put(ent_name, new Integer(value.intValue() + 1));
    }

    AEntity supertypes = targetEntDef.getGeneric_supertypes(null);
    SdaiIterator it = supertypes.createIterator();
    while (it.next()) {
      EEntity_definition currentDef = (EEntity_definition) supertypes.getCurrentMemberObject(it);

      addSupertypesToHashtable(commonSupertypes, currentDef, relatedEntityDefs, 0);
    }
  }

  /**
   * Method takes given entity mapping and determines what interfaces does it implement. Method
   * visits only direct supertypes of entity_definition (that is related with this entity_mapping),
   * and it's main purpose is to get interfaces that are implemented by complex mixed types.
   *
   * @param entMap         Entity mapping, should be entity_mapping to complex_mixed entity type, although
   *                       method accepts mappings to simple types as well.
   * @param implInterfaces Names of implemented interfaces are returned in this vector. Note, that
   *                       returned names already contain letter E and first letter in upper case.
   */
  protected void getImplementedInterfaces(Vector implInterfaces, EEntity_mapping entMap) throws SdaiException, Exception {
    //from ent-mapping read target entity definition, then take its direct supertypes.
    // read names of those direct supertypes and push them to vector. That's all for this method.
    EEntity_definition entDefinition = (EEntity_definition) entMap.getTarget(null);
    AEntity supertypes = entDefinition.getGeneric_supertypes(null);

    SdaiIterator entDefIt = supertypes.createIterator();
    if (supertypes.getMemberCount() == 0) {
      throw new Exception("An invalid complex mixed entity type was detected: no supertypes for it were found!");
    }

    while (entDefIt.next()) {
      EEntity_definition superType = (EEntity_definition) supertypes.getCurrentMemberObject(entDefIt);
      // a fuse: if this is a complex type, then warn programmer- as source is not adopted to that case:
      if (superType.getComplex(null)) {
        throw new Exception("A mixed complex entity type was detected as supertype! Don't know how to respond.");
      }
      String name = superType.getName(null);
      name = "E" + capitalize(name);
      implInterfaces.add(name);
    }
  }

  /**
   *
   */
  protected SdaiModel findMappingModel(String armSchemaName) throws SdaiException, Exception {
    SdaiRepository systRep = null;

    ASdaiRepository agRepositories = SdaiSession.getSession().getKnownServers();
    SdaiIterator repIt = agRepositories.createIterator();
    while (repIt.next()) {
      SdaiRepository rep = (SdaiRepository) agRepositories.getCurrentMemberObject(repIt);
      if (rep.getName().equalsIgnoreCase("ExpressCompilerRepo")) {
        systRep = rep;
        break;
      }

    }
    if (systRep == null) {
      throw new Exception("ExpressCompilerRepo was not found!");
    }

    if (!systRep.isActive()) {
      systRep.openRepository();
    }
    ASdaiModel allSysModels = systRep.getModels();

    if (allSysModels == null) {
      throw new Exception("No associated models found in main schema!");
    }

    // iterate thru all:
    SdaiIterator mIt = allSysModels.createIterator();
    while (mIt.next()) {
      SdaiModel model = (SdaiModel) allSysModels.getCurrentMemberObject(mIt);
      try {
        SimpleOperations.enshureReadOnlyModel(model);
        String modelName = model.getName();
        if (modelName.endsWith(SdaiSession.MAPPING_NAME_SUFIX)) {
          int endIndex = modelName.length() - SdaiSession.MAPPING_NAME_SUFIX.length();
          modelName = modelName.substring(0, endIndex);
        }
        modelName = modelName.toUpperCase();
        if (!modelName.equalsIgnoreCase(armSchemaName)) {
          continue;
        }
        else {
          return model;
        }
      }
      catch (SdaiException e) {
        e.printStackTrace();
        continue;
      }
    }

    throw new Exception("Requested model not found!" + armSchemaName);
  }

  /**
   *
   */
  protected SdaiModel findDictionaryModel(String armSchemaName) throws SdaiException, Exception {
    SdaiRepository systRep = null;

    ASdaiRepository agRepositories = SdaiSession.getSession().getKnownServers();
    SdaiIterator repIt = agRepositories.createIterator();
    while (repIt.next()) {
      SdaiRepository rep = (SdaiRepository) agRepositories.getCurrentMemberObject(repIt);
      if (rep.getName().equalsIgnoreCase("ExpressCompilerRepo")) {
        systRep = rep;
        break;
      }

    }
    if (systRep == null) {
      throw new Exception("ExpressCompilerRepo was not found!");
    }

    if (!systRep.isActive()) {
      systRep.openRepository();
    }
    ASdaiModel allSysModels = systRep.getModels();

    if (allSysModels == null) {
      throw new Exception("No associated models found in main schema!");
    }

    // iterate thru all:
    SdaiIterator mIt = allSysModels.createIterator();
    while (mIt.next()) {
      SdaiModel model = (SdaiModel) allSysModels.getCurrentMemberObject(mIt);
      try {
        SimpleOperations.enshureReadOnlyModel(model);
        String modelName = model.getName();
        if (modelName.endsWith(SdaiSession.DICTIONARY_NAME_SUFIX)) {
          int endIndex = modelName.length() - SdaiSession.DICTIONARY_NAME_SUFIX.length();
          modelName = modelName.substring(0, endIndex);
        }
        modelName = modelName.toUpperCase();
        if (!modelName.equalsIgnoreCase(armSchemaName)) {
          continue;
        }
        else {
          return model;
        }
      }
      catch (SdaiException e) {
        continue;
      }
    }
    throw new Exception("Requested model not found!");
  }

  protected String getMappedValue(EGeneric_attribute_mapping atrMapping) throws SdaiException, Exception {
    String result = "";

    if (atrMapping instanceof EAttribute_mapping_enumeration_value) {
      EAttribute_mapping_enumeration_value ameValue = (EAttribute_mapping_enumeration_value) atrMapping;
      result = ameValue.getMapped_value(null);
    }
    if (atrMapping instanceof EAttribute_mapping_real_value) {
      EAttribute_mapping_real_value ameValue = (EAttribute_mapping_real_value) atrMapping;
      double value = ameValue.getMapped_value(null);
      result = String.valueOf(value);
    }
    if (atrMapping instanceof EAttribute_mapping_logical_value) {
      EAttribute_mapping_logical_value ameValue = (EAttribute_mapping_logical_value) atrMapping;
      int value = ameValue.getMapped_value(null);
      result = String.valueOf(value);
    }
    if (atrMapping instanceof EAttribute_mapping_int_value) {
      EAttribute_mapping_int_value ameValue = (EAttribute_mapping_int_value) atrMapping;
      int value = ameValue.getMapped_value(null);
      result = String.valueOf(value);
    }
    if (atrMapping instanceof EAttribute_mapping_boolean_value) {
      EAttribute_mapping_boolean_value ameValue = (EAttribute_mapping_boolean_value) atrMapping;
      boolean value = ameValue.getMapped_value(null);
      result = String.valueOf(value);
    }
    if (atrMapping instanceof EAttribute_mapping_string_value) {
      EAttribute_mapping_string_value ameValue = (EAttribute_mapping_string_value) atrMapping;
      result = ameValue.getMapped_value(null);
    }
    return result;
  }

  protected String getDomainName(EGeneric_attribute_mapping atrMapping) throws SdaiException, Exception {
    String domName = "";
    if (atrMapping instanceof EAttribute_mapping) {
      EAttribute_mapping exactMap = (EAttribute_mapping) atrMapping;
      if (exactMap.testDomain(null)) {
        EEntity domain = exactMap.getDomain(null);
        EEntity finalType = getFinalType(domain);
        if (finalType instanceof ESelect_type) {
          System.out.println("Select type detected in domain!" + "Dont know how to respond!" + atrMapping);
        }
        else if (finalType instanceof EEntity_definition) {
          EEntity_definition eDef = (EEntity_definition) finalType;
          domName = eDef.getName(null);
        }
        else if (finalType instanceof ESimple_type) {
          System.out.println("Simple type detected in domain!" + "Dont know how to respond!" + atrMapping);
        }
        else if (finalType instanceof EEnumeration_type) {
          System.out.println("Enumeration type detected in domain!" + "Dont know how to respond!" + atrMapping);
        }
        else {
          System.out.println("Unforseen case was found:" + finalType);
          throw new Exception("I believe generator needs be updated!" + " Please check and correct if required.");
        }
      }
    }
    return domName;
  }

  protected EEntity getFinalType(EEntity type) throws SdaiException, Exception {
    if (type instanceof ESelect_type) {
      return type;
    }
    if (type instanceof EDefined_type) {
      EDefined_type ed_type = (EDefined_type) type;
      if (!ed_type.testDomain(null)) {
        throw new Exception("I believe a problem in mapping data was found! Please check and correct if required.");
      }
      EEntity domain = ed_type.getDomain(null);
      return getFinalType(domain);
    }
    if (type instanceof EEntity_definition) {
      return type;
    }
    if (type instanceof ESimple_type) {
      return type;
    }
    if (type instanceof EAggregation_type) {
      EAggregation_type ea_type = (EAggregation_type) type;
      if (!ea_type.testElement_type(null)) {
        throw new Exception("I believe a problem in mapping data was found! Please check and correct if required.");
      }
      EEntity element_type = ea_type.getElement_type(null);
      return getFinalType(element_type);
    }
    if (type instanceof EEnumeration_type) {
      return type;
    }
    if (type instanceof EEntity_mapping) {
      EEntity target = ((EEntity_mapping) type).getTarget(null);
      return getFinalType(target);
    }

    System.out.println("Unforseen case was found:" + type);
    throw new Exception("I believe a problem in mapping data was found, or generator needs be updated!" + " Please check and correct if required.");
  }

  /**
   * Method determines if there is a potential capability to get aggregate in
   * testXX or getXX methods. This information is essential, because due to
   * bad mapping data/operations it can happen that testXX method will get
   * aggregate of allowed types, where single value is expected. Thus testXX
   * method must know, if having aggregate is OK for tested attribute.
   */
  protected boolean isAggregateInside(EEntity type) throws SdaiException, Exception {
    if (type instanceof EEntity_definition) {
      return false;
    }
    if (type instanceof EAggregation_type) {
      return true;
    }
    if (type instanceof ESimple_type) {
      return false;
    }
    if (type instanceof EDefined_type) {
      EDefined_type defType = (EDefined_type) type;
      EEntity domain = defType.getDomain(null);
      return isAggregateInside(domain);
    }
    if (type instanceof EEnumeration_type) {
      return false;
    }
    if (type instanceof ESelect_type) {
      ESelect_type selType = (ESelect_type) type;
      ANamed_type selections = selType.getSelections(null);
      SdaiIterator it = selections.createIterator();
      while (it.next()) {
        ENamed_type nType = selections.getCurrentMember(it);
        boolean testValue = isAggregateInside(nType);
        if (testValue) {
          return true;
        }
      }
      return false;
    }
    throw new Exception("Unknown type received: please update generator!" + type);
  }

}