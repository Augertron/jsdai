package jsdai.mappingUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;
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

abstract class PureClassesGeneratorUtilityFunctions {

  protected SdaiModel dictModel = null;
  protected SdaiModel mapModel = null;
  protected ASdaiModel mappingDomain = null;

  protected Hashtable systemRepository = new Hashtable();
  protected String[] excludeSchemaName = { "AP214_ARM", "AP210_ARM", "AP212_ARM", "AP225_ARM", "AP203_ARM" };

  protected String mainSchemaName = "";
  protected String docLocationRoot = new String("D:\\cvs_projects\\api_ref_dev");

  public boolean isGeneratingDocumentation = false;
  public boolean isHidingWriteOperations = false;

  //protected Hashtable classDescriptionInIteration = new Hashtable();
  protected final String COMMON_METHODS_GROUP_NAME = "Common methods";
  protected final String LABEL_OF_INSERT_START = "<!--ADDON STARTS HERE-->";
  protected ClassDocumentation doc = new ClassDocumentation();

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
  // key is attribute name, value is hashtable X. Key for hashtable X is
  // aim entity type name (instance of this aim entity type can be used to get
  // a value of appropriate attribute), and value in hashtable X is type name of
  // overload parameter in getXX methods.
  protected Hashtable attrMethodsInIteration = new Hashtable();

  // directory, where generated files should be placed
  protected String generatedTargetDir = "";
  // variable allows to control whether 'main' function should be added to generated class.
  protected boolean generateMainFunction = false;

  protected String exampleString = "<P><TT><pre>\n" + "	import jsdai.MAp214.*;\n" + "	import jsdai.lang.*;\n" + "	import jsdai.dictionary.*;\n"
      + "	import jsdai.mapping.*;\n" + "	\n" + " public static final void main (String args[])  throws SdaiException, Exception {\n"
      + "		System.out.println(\"Test application of Item\");\n" + "		//Session is opened already by initializing static members.\n"
      + "		SdaiSession session = SdaiSession.getSession();\n" + "		//Transaction is already started by initializing static members.\n"
      + "		//SdaiTransaction trx = session.startTransactionReadWriteAccess();\n" + "		SdaiRepository repo = session.linkRepository(args[0],null);\n"
      + "		repo.openRepository();\n" + "		ASdaiModel domain = repo.getModels();\n" + "		SdaiModel model = domain.getByIndex(1);\n" + "\n"
      + "		model.startReadWriteAccess();\n" + "\n" + "		AEntity instances = MItem.findInstances(domain, null);\n" + "\n"
      + "		int no_of_instances = instances.getMemberCount();\n" + "		System.out.println(\"Number of received instances is \"+no_of_instances);\n"
      + "		System.out.println(instances);\n" + "		int expectedRetType = -1;\n" + "		for (int i=1;i<=no_of_instances;i++) {\n"
      + "			EEntity instance = (EEntity) instances.getByIndexEntity(i);\n" + "			System.out.println(\"\");\n"
      + "			System.out.println(\"instance is \"+instance);\n" + "			expectedRetType = MItem.testId(domain, instance);\n"
      + "			switch (expectedRetType) {\n" + "				case MItem.ID__STRING:\n" + "					//put appropriate call to getXX method here.\n"
      + "				break;\n"
      + "			default:\n" + "				System.out.println(\"Value not set!\");\n" + "			}\n"
      + "			expectedRetType = MItem.testName(domain, instance);\n"
      + "			switch (expectedRetType) {\n" + "				case MItem.NAME__STRING:\n" + "					//put appropriate call to getXX method here.\n"
      + "					String result = MItem.getName(domain, instance, (String) null);\n" + "					System.out.println(\"result for name is\"+result);\n"
      + "					if (result.equalsIgnoreCase(\"nba\")) {\n" + "						result = \"LKL\";\n"
      + "						System.out.println(\"setting new value for name\");\n"
      + "						MItem.setName(domain, instance, result, (String) null);\n" + "						session.getActiveTransaction().commit();\n"
      + "					}\n"
      + "				break;\n" + "			default:\n" + "				System.out.println(\"Value not set!\");\n" + "			}\n"
      + "			expectedRetType = MItem.testDescription(domain, instance);\n" + "			switch (expectedRetType) {\n"
      + "				case MItem.DESCRIPTION__STRING:\n"
      + "					//put appropriate call to getXX method here.\n" + "				break;\n" + "			default:\n"
      + "				System.out.println(\"Value not set!\");\n"
      + "			}\n" + "		}\n" + "	}\n" + "\n" + "</pre></TT>\n";

  public PureClassesGeneratorUtilityFunctions(String apPartNumber, String genTargetDir, String mainSchema, boolean genMainFunction, String docSourcePath)
      throws SdaiException, Exception {
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
  protected String generateValidName(String name) {
    String retValue = new String(name);
    retValue = retValue.replace('+', '$');
    return retValue;
  }

  /**
   *
   */
  protected String changeFirstLetterToUpperCase(String name) {
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
      targetName = generateValidName(changeFirstLetterToUpperCase(targetName));
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
          dataTypesName = changeFirstLetterToUpperCase(dataTypesName);
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
  protected String makeAttrMappingVariableName(String entMappingTargetName, String attrName, EGeneric_attribute_mapping ega_mapping) throws SdaiException,
      Exception {
    String retValue = "";
    String dataTypeName = getDataTypeName(ega_mapping, true);
    retValue = "am"
        + generateValidName(changeFirstLetterToUpperCase(entMappingTargetName) + "__" + ((dataTypeName.length() == 0) ? "" : dataTypeName + "__")
        + generateValidName(changeFirstLetterToUpperCase(attrName)));
    return retValue;
  }

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
  protected String[] determineFinalTargetType(EEntity armAtrFinalType, EGeneric_attribute_mapping atrMapping, EExplicit_attribute expAtr,
      Hashtable allowedTypes) throws SdaiException, Exception {
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
            if (ntFinalType.getName(null).equalsIgnoreCase("undefined_object")) {
//							System.out.println("parsing undefined_object, when mapping path ends not with"+
//								" EAttribute."+last+atrMapping);
              finalType = determineFinalTargetTypeForUndefinedObject(ntFinalType, last, expAtr);
            }
          }
        }
      }
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
        System.out.println("Complex class is returned.");
        // because for ap210 and ap214 cases complex mixed class are never returned,
        // there is no chance to test how it works. So throwing an exception to force
        // programmer to test this functionality, when it appears.
        throw new Exception("Complex mixed type is used as return type for getXX method!"
            + " Never seen it before, so please test this case carefully.");
      }
      String result = ed_type.getName(null);
      return result;
    }
    if (convertType instanceof EString_type) {
      return "String";
    }
    if (convertType instanceof EBoolean_type) {
      return "EEntity"; // at the moment, a typical return value from getMappedAttribute in case
    }
    // of boolean value is instance of EEntity. This might be treated as bug,
    // but let's adopt to this for now.
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
    if (convertType instanceof ELogical_type) {
      return "EEntity";
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
    /*
     * // the source below can be used to access system repository.
     * systemRepository.clear();
     * SdaiRepository systRep = SdaiSession.getSession().getSystemRepository();
     * ASdaiModel allSysModels = systRep.getModels();
     * // iterate thru all:
     * SdaiIterator mIt = allSysModels.createIterator();
     * EEntity_definition ent_def = null;
     * ESchema_definition schema = null;
     * while (mIt.next()) {
     * SdaiModel model = (SdaiModel) allSysModels.getCurrentMemberObject(mIt);
     * try {
     * SimpleOperations.enshureReadOnlyModel(model);
     * schema = model.getDefinedSchema();
     * if (schema == null)
     * continue;//skip malformed models
     * String schemaName = schema.getName(null);
     * schemaName = schemaName.toUpperCase();
     * // test whether this schema is forbidden to use:
     * int count = excludeSchemaName.length;
     * boolean skip = false;
     * for (int i=0;i<count;i++) {
     * if (schemaName.indexOf (excludeSchemaName[i]) != -1) {
     * skip = true;
     * break;
     * }
     * }
     * if (skip)
     * continue;
     * Hashtable schemaContents = new Hashtable();
     *
     * AEntityExtent modelExtents = model.getFolders();
     * SdaiIterator eeIt = modelExtents.createIterator();
     * while (eeIt.next()) {
     * EntityExtent entExtent = (EntityExtent) modelExtents.getCurrentMemberObject(eeIt);
     * String defString = entExtent.getDefinitionString();
     * if (!defString.equalsIgnoreCase("entity_definition"))
     * continue;
     * AEntity entDefInstances = entExtent.getInstances();
     * SdaiIterator edIt = entDefInstances.createIterator();
     * while (edIt.next()) {
     * ent_def = (EEntity_definition) entDefInstances.getCurrentMemberObject(edIt);
     * String entDefName = ent_def.getName(null);
     * entDefName = entDefName.toUpperCase();
     * schemaContents.put (entDefName, entDefName);
     * }
     * break;
     * }
     * systemRepository.put(schemaName, schemaContents);
     * }
     * catch (SdaiException e) {
     * continue;
     * }
     * }
     */
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
          String result = changeFirstLetterToUpperCase(schemaName.toLowerCase());
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
      String result = changeFirstLetterToUpperCase(candidateSchemaName.toLowerCase());
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
      name = "E" + changeFirstLetterToUpperCase(name);
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
        continue;
      }
    }
    throw new Exception("Requested model not found!");
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

  protected class ClassDocumentation {
    protected String nameOfGeneratedClass = new String();
    protected String anyFreeDescriptionForClass = new String();
    protected String anyDescriptionForConstants = new String();
    /**
     * key: attribute, that is linked with this constant, name ('as is').
     * value: Hashtable. Where key: constant name, value: not important.
     */
    protected Hashtable staticConstants = new Hashtable();
    /**
     * key: attribute name 'as is' - without uppercase, etc.
     * value: Hashtable. Where key: method signature, value: not important
     */
    protected Hashtable attributes = new Hashtable();
    /**
     * key: method signature
     * value: description of method
     */
    protected Hashtable commonMethods = new Hashtable();

    ClassDocumentation() {
    }

    public void clear() {
      nameOfGeneratedClass = new String();
      anyFreeDescriptionForClass = new String();
      anyDescriptionForConstants = new String();
      staticConstants.clear();
      attributes.clear();
      commonMethods.clear();
    }

    public void setNameOfGeneratedClass(String newValue) {
      nameOfGeneratedClass = newValue;
    }

    public void setAnyFreeDescriptionForClass(String newValue) {
      anyFreeDescriptionForClass = newValue;
    }

    public void setAnyDescriptionForConstants(String newValue) {
      anyDescriptionForConstants = newValue;
    }

    public void addStaticConstant(String attrName, String staticConst) {
      if (staticConstants.containsKey(attrName)) {
        Hashtable child = (Hashtable) staticConstants.get(attrName);
        child.put(staticConst, staticConst);
      }
      else {
        Hashtable child = new Hashtable();
        child.put(staticConst, staticConst);
        staticConstants.put(attrName, child);
      }
    }

    public void addAttributeInfo(String attrName, String methodString) {
      if (attributes.containsKey(attrName)) {
        Hashtable child = (Hashtable) attributes.get(attrName);
        child.put(methodString, methodString);
      }
      else {
        Hashtable child = new Hashtable();
        child.put(methodString, methodString);
        attributes.put(attrName, child);
      }
    }

    public void addCommonMethods(String methodString, String description) {
      commonMethods.put(methodString, description);
    }

    public void printClassDocumentation(FileWriter file, String docRootFolder) throws Exception {
      printStartLabel(file);
      println(file);
      printTableHeader(file);
      printH3(file, "Early binding static class " + visualize(nameOfGeneratedClass) + " (jsdai.MAp" + apNumber + ")");
      printTableTail(file);
      if (anyFreeDescriptionForClass.length() > 0) {
        print(file, anyFreeDescriptionForClass);
        println(file);
      }
      if (!attributes.isEmpty()) {
        printTableHeader(file);
        Object[] keys = attributes.keySet().toArray();
        Arrays.sort(keys);
        int keyCount = keys.length;
        for (int i = 0; i < keyCount; i++) {
          String attributeName = (String) keys[i];
          printH4(file, attributeName + " (explicit attribute) ");
          // now, print constants for this attribute
          printStaticConstants(file, attributeName);
          // now, print methods for this attribute in special order
          printMethods(file, attributeName);
          println(file);
        }
        printTableTail(file);
      }
      if (!commonMethods.isEmpty()) {
        // ok. Due to request from Lothar, we will print common methods to
        // separate file. So, let's create that file first:
        FileWriter genericFile = new FileWriter(docRootFolder + File.separator + "jsdai" + File.separator + "lang" + File.separator
            + "MGenericEntries.html");
        printHtmlHead(genericFile, "Generic entries");
        printH2(genericFile, "Common information for classes" + " MXxx, supporting the ARM/AIM mapping");

        printH3(genericFile, "Example of using early binding mapping class");
        print(genericFile, exampleString);
        println(genericFile);

        if (anyDescriptionForConstants.length() > 0) {
          printH3(genericFile, printTarget("The meaning of public constants in each class", "constants"));
          printTab(genericFile, " ");
          print(genericFile, anyDescriptionForConstants);
          println(genericFile);
        }
        printTab(genericFile, " ");
        println(genericFile);
        printH3(genericFile, printTarget("Common generic methods", "ComGenMeth"));
        printH4(file, printHRef("Other available methods", "../lang/MGenericEntries.html#ComGenMeth"));
        Object[] keys = commonMethods.keySet().toArray();
        Arrays.sort(keys);
        int keyCount = keys.length;
        for (int i = 0; i < keyCount; i++) {
          String method = (String) keys[i];
          String descr = (String) commonMethods.get(method);
          printTab(genericFile, " ");
          println(genericFile, descr);
          printTableHeader(genericFile);
          printTab(genericFile, " ");
          printBold(genericFile, method);
          println(genericFile);
          printTableTail(genericFile);
          println(genericFile);
        }
        printHtmlTail(genericFile);
        genericFile.close();
      }
      printBreak(file);
      println(file);
    }

    private void printSpecificMethods(FileWriter file, String atrDisplayName, TreeSet tree, String keyWord) throws Exception {
      String testString = "<B>" + keyWord + "</B>" + atrDisplayName;
      Iterator it = tree.iterator();
      boolean wasFound = false;
      while (it.hasNext()) {
        String method = (String) it.next();
        if (method.indexOf(testString) != -1) {
          printTab(file, method);
          println(file);
          wasFound = true;
          it.remove();
        }
      }
      if (wasFound) {
        println(file);
      }
    }

    private void printMethods(FileWriter file, String attributeName) throws Exception {
      String atrDisplayName = visualize(attributeName);
      TreeSet tree = new TreeSet();
      Hashtable methods = (Hashtable) attributes.get(attributeName);
      tree.addAll(methods.keySet());
      boolean allowBreak = true;
      // print methods in following order:
      // test, get, set, usedin. Others will be decided when they appear.
      // Recognition of methods is done by name of method
      printSpecificMethods(file, atrDisplayName, tree, "test");
      printSpecificMethods(file, atrDisplayName, tree, "get");
      printSpecificMethods(file, atrDisplayName, tree, "set");
      printSpecificMethods(file, atrDisplayName, tree, "usedin");
      // print the rest methods
      printSpecificMethods(file, atrDisplayName, tree, "");
    }

    private void printStaticConstants(FileWriter file, String attributeName) throws Exception {
      if (!staticConstants.containsKey(attributeName)) {
        return;
      }
      Hashtable linkedConsts = (Hashtable) staticConstants.get(attributeName);
      if (linkedConsts.isEmpty()) {
        return;
      }
      printTableHeader(file);
      Object[] keys = linkedConsts.keySet().toArray();
      Arrays.sort(keys);
      int keyCount = keys.length;
      for (int i = 0; i < keyCount; i++) {
        String staticConstantName = (String) keys[i];
        printTab(file, " ");
        println(file, "static final int " + printHRef(staticConstantName, "../lang/MGenericEntries.html#constants"));
      }
      printTableTail(file);
      println(file);
    }

    private String visualize(String s) {
      StringBuffer nameBuffer = new StringBuffer(s);
      nameBuffer.setCharAt(0, nameBuffer.substring(0, 1).toUpperCase().charAt(0));
      String retString = nameBuffer.toString();
      return retString;
    }

    private void printStartLabel(FileWriter f) throws Exception {
      f.write(LABEL_OF_INSERT_START);
      f.write("\n");
    }

    private void println(FileWriter f, String s) throws Exception {
      f.write(s + "<BR>\n");
    }

    private void println(FileWriter f) throws Exception {
      f.write("<BR>\n");
    }

    private void printBold(FileWriter f, String s) throws Exception {
      f.write("<B>" + s + "</B>");
    }

    private void print(FileWriter f, String s) throws Exception {
      f.write(s);
    }

    private void printBreak(FileWriter f) throws Exception {
      f.write("<HR>\n");
    }

    private void printH2(FileWriter f, String s) throws Exception {
      f.write("<H3>\n" + s + "</H3>\n");
    }

    private void printH3(FileWriter f, String s) throws Exception {
      f.write("<H4>\n" + s + "</H4>\n");
    }

    private void printH4(FileWriter f, String s) throws Exception {
      f.write("<H4><I>\n" + s + "</I></H4>\n");
    }

    private void printHRef(FileWriter f, String s, String ref) throws Exception {
      f.write("<A HREF=" + ref + ">" + s + "</A>");
    }

    private String printHRef(String s, String ref) throws Exception {
      String result = "<A HREF=" + ref + ">" + s + "</A>";
      return result;
    }

    private void printTab(FileWriter f, String s) throws Exception {
      f.write("<DD>" + s + "</DD>\n");
    }

    private void printName(FileWriter f, String s, String name) throws Exception {
      f.write("<A NAME=\"" + name + "\">" + s + "</A>\n");
    }

    private void printHRefandTarget(FileWriter f, String s, String ref, String target) throws Exception {
      f.write("<A HREF=" + ref + " TARGET=" + target + ">" + s + "</A>\n");
    }

    private void printTarget(FileWriter f, String s, String target) throws Exception {
      f.write("<A NAME=" + target + ">" + s + "</A>\n");
    }

    private String printTarget(String s, String target) throws Exception {
      String result = "<A NAME=" + target + ">" + s + "</A>\n";
      return result;
    }

    private void printTableHeader(FileWriter f) throws Exception {
      f.write("<TABLE BORDER=\"0\" WIDTH=\"100%\"><TR><TD NOWRAP>\n");
    }

    private void printTableTail(FileWriter f) throws Exception {
      f.write("</TD></TR></TABLE>\n");
    }

    private void printHtmlHead(FileWriter f, String s) throws Exception {
      f.write("<HTML>\n<HEAD>\n\t<TITLE>" + s + "</TITLE>\n</HEAD>\n<BODY>\n");
    }

    private void printHtmlTail(FileWriter f) throws Exception {
      f.write("</BODY>\n</HTML>\n");
    }
  }
}