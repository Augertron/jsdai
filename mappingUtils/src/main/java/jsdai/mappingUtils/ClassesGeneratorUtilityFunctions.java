package jsdai.mappingUtils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.AEntity_definition;
import jsdai.SExtended_dictionary_schema.AExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EEnumeration_type;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SMapping_schema.AEntity_mapping;
import jsdai.SMapping_schema.AGeneric_attribute_mapping;
import jsdai.SMapping_schema.CEntity_mapping;
import jsdai.SMapping_schema.CGeneric_attribute_mapping;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EGeneric_attribute_mapping;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

class ClassesGeneratorUtilityFunctions extends PureClassesGeneratorUtilityFunctions {

  protected ClassesGeneratorUtilityFunctions(String apPartNumber, String genTargetDir, String mainSchema, boolean genMainFunction, String docSourcePath)
      throws SdaiException, Exception {
    super(apPartNumber, genTargetDir, mainSchema, genMainFunction, docSourcePath);
  }

  /**
   *
   */
  public void generateRequiredToImportClasses(Hashtable importStatements, EEntity_definition entDef) throws SdaiException, Exception {
    // first get all known entity mappings for given entity definition:
    AEntity_mapping agMappings = new AEntity_mapping();
    CEntity_mapping.usedinSource(null, entDef, mappingDomain, agMappings);

    SdaiIterator it = agMappings.createIterator();
    while (it.next()) {
      EEntity_mapping mapping = (EEntity_mapping) agMappings.getCurrentMemberObject(it);
      String targetName = getEntMapTargetName(mapping, false);
      String importClassName = findFullClassName(targetName, importStatements);
      if (importClassName.length() == 0) {
        System.out.println("ERROR: Failed to create import statement for AIM entity definition" + targetName);
        continue;
      }
      if (!importStatements.containsKey(importClassName)) {
        importStatements.put(importClassName, importClassName);
      }
      // now, additional analysis: entity_mapping target can be mixed complex entity type, so we
      // must process it separately:
      if (isComplexEntityType(mapping)) {
        // get target entity definition first:
        EEntity_definition targetEntDef = (EEntity_definition) mapping.getTarget(null);
        // get list of super types for it:
        AEntity supertypes = targetEntDef.getGeneric_supertypes(null);
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
          // now, find import statement:
          targetName = getEntDefTargetName(superType, true);
          String supertypeImportClassName = findFullClassName(targetName, importStatements);
          if (supertypeImportClassName.length() == 0) {
            System.out.println("ERROR: Failed to create import statement for AIM entity definition" + targetName);
            continue;
          }
          if (!importStatements.containsKey(supertypeImportClassName)) {
            importStatements.put(supertypeImportClassName, supertypeImportClassName);
          }
        }
      }
    }
  }

  /**
   *
   */
  public void generateEntityDefinition(Vector entityDefinitions, EEntity_definition entDef) throws SdaiException, Exception {
    String result = "\tstatic EEntity_definition armEntity = findArmEntity(\"" + entDef.getName(null) + "\");\n";
    entityDefinitions.add(result);
  }

  public void generateCanBeMappedFromMethod(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    String docHeader = "static boolean is(ASdaiModel domain, EEntity instance, AEntity_mapping" + " outAgAvailMappings) throws SdaiException ";
    String description = "Method tests whether given AIM instance can be mapped to appropriate ARM entity instance."
        + " If it can - then true is returned. You can pass null value - in this case false will be" + " returned.";
    doc.addCommonMethods(docHeader, description);
    String printString = "\tpublic static boolean is(ASdaiModel domain, EEntity instance, AEntity_mapping"
        + " outAgAvailMappings) throws SdaiException {\n" + "\t\tif (instance == null)\n\t\t\treturn false;\n"
        + "\t\tAEntity_mapping availMappings = instance.testMappedEntity(armEntity, domain, metaDomain," + " currentMappingMode);\n"
        + "\t\tif (availMappings == null)\n\t\t\treturn false;\n" + "\t\telse {\n" + "\t\t\tif (outAgAvailMappings != null) {\n"
        + "\t\t\t\tSdaiIterator it = availMappings.createIterator();\n" + "\t\t\t\twhile (it.next()) {\n"
        + "\t\t\t\t\tEEntity ent = (EEntity) availMappings.getCurrentMemberObject(it);\n" + "\t\t\t\t\toutAgAvailMappings.addUnordered(ent);\n"
        + "\t\t\t\t}\n" + "\t\t\t}\n" + "\t\t\treturn true;\n" + "\t\t}\n" + "\t}\n";
    classMethods.add(printString);
    classMethods.add("\n");
  }

  /**
   *
   */
  protected String generateFindInstanceEntryForComplexEntityType(EEntity_mapping entMapping) throws SdaiException, Exception {
    // we know that there is no E interface for complex entity type, but this entity type is made out of
    // two or more simple entity types. We must find all those simple entity types (or there can be any complex
    // types as well?) and generate if statement appropriately.
    // NOTE: it is assumed that entMapping is of complex type and no checking is done.

    // get target entity definition first:
    EEntity_definition targetEntDef = (EEntity_definition) entMapping.getTarget(null);
    // get list of super types for it:
    AEntity supertypes = targetEntDef.getGeneric_supertypes(null);
    // now use them to construct if statement:

    String ifStatement = "\t\t\tif (!(";
    SdaiIterator it = supertypes.createIterator();

    if (supertypes.getMemberCount() == 0) {
      throw new Exception("An invalid complex mixed entity type was detected: no supertypes for it were found!");
    }

    while (it.next()) {
      EEntity_definition superType = (EEntity_definition) supertypes.getCurrentMemberObject(it);
      String aimEntTypeName = "E" + generateValidName(changeFirstLetterToUpperCase(superType.getName(null)));

      // a fuse: if this is a complex type, then warn programmer- as source is not adopted to that case:
      if (superType.getComplex(null)) {
        throw new Exception("A mixed complex entity type was detected as supertype! Don't know how to respond.");
      }

      String checkString = "(" + aimEntTypeName + ".class.isAssignableFrom(entityType)) && ";
      ifStatement += checkString;
    }
    // now remove last occurence of '&& '
    if (ifStatement.endsWith("&& ")) {
      int endIndex = ifStatement.length() - 3;
      ifStatement = ifStatement.substring(0, endIndex);
    }
    // now, complete if statement:
    ifStatement += ")) \n\t\t\t\tcanCopy = false;\n";
    return ifStatement;
  }

  /**
   *
   */
  public void generateFindInstanceMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    // let's generate a single findInstance with last parameter of Class type. If last parameter equals to null,
    // then all possible instances should be retrieved.
    String documentationHeader = "static public AEntity findInstances(ASdaiModel domain, Class entityType) throws" + " SdaiException";
    String description = "This method finds all AIM instances that can be mapped to this ARM entity."
        + " It's guaranteed that result set will not contain repeated entries."
        + " Parameter <I>domain</I> identifies which SdaiModels should be searched for AIM instances,"
        + " parameter <I>entityType</I> allows to limit result set only to instances, that"
        + " are 'instanceof' of given class. This parameter can be null - in this case no"
        + " restrictions will be applied. You can specify here a class for simple entity type"
        + " (starting with letter E), or you can specify here a class for mixed complex entity" + " type (starting with letter C).";
    doc.addCommonMethods(documentationHeader, description);
    String printString = "\tstatic public AEntity findInstances(ASdaiModel domain, Class entityType) throws SdaiException {\n"
        + "\t\tAEntity retValue = new AEntity();\n\t\tboolean canCopy = true;\n";
    int mapsCount = 0;
    String addString = "";
    SdaiIterator it = entityMappingsInIteration.createIterator();
    while (it.next()) {
      EEntity_mapping entMapping = (EEntity_mapping) entityMappingsInIteration.getCurrentMemberObject(it);
      String aimEntityClassName = "E" + getEntMapTargetName(entMapping, true);
      String checkString = null;
      if (isComplexEntityType(entMapping)) {
        checkString = generateFindInstanceEntryForComplexEntityType(entMapping);
      }
      else {
        checkString = "\t\t\tif (!entityType.isAssignableFrom(" + aimEntityClassName + ".class)) \n" + "\t\t\t\tcanCopy = false;\n";
      }
      String resultString = "\t\tcanCopy = true;\n\t\tif (entityType != null) \n" + checkString + "\t\tif (canCopy) {\n";
      addString = "\t\t\tAEntity retVal" + mapsCount + " = findMappingInstances(emTo" + getEntMapTargetName(entMapping, true)
          + ", domain, currentMappingMode);\n" + "\t\t\tcopyEntities(retValue, retVal" + mapsCount + ");\n" + "\t\t}\n\n";
      printString = printString + resultString + addString;
      mapsCount++;
    }
    addString = "\t\treturn retValue;\n\t}\n";
    printString = printString + addString;

    classMethods.add(printString);
    classMethods.add("\n");
  }

  /**
   *
   */
  protected boolean acceptableMapping(EEntity_mapping entMap) throws SdaiException, Exception {
    SdaiIterator it = entityMappingsInIteration.createIterator();
    while (it.next()) {
      EEntity_mapping mapping = (EEntity_mapping) entityMappingsInIteration.getCurrentMemberObject(it);
      if (mapping == entMap) {
        return true;
      }
    }
    return false;
  }

  /**
   *
   */
  public void generateMainMethod(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    String armEntityDispName = getEntDefTargetName(entDef, true);
    // let's generate a test method at the end of file:
    String printString = "\tpublic static final void main (String args[]) " + " throws SdaiException, Exception {\n"
        + "\t\tSystem.out.println(\"Test application of "
        + armEntityDispName
        + "\");\n"
        + "\t\t//Session is opened already by initializing static members.\n"
        + "\t\tSdaiSession session = SdaiSession.getSession();\n"
        + "\t\t//Transaction is already started by initializing static members.\n"
        + "\t\t//SdaiTransaction trx = session.startTransactionReadWriteAccess();\n"
        + "\t\tSdaiRepository repo = session.linkRepository(args[0],null);\n"
        + "\t\trepo.openRepository();\n"
        + "\t\tASdaiModel domain = repo.getModels();\n"
        + "\t\tSdaiModel model = domain.getByIndex(1);\n\n"
        + "\t\tmodel.startReadWriteAccess();\n\n"
        + "\t\tAEntity instances = M"
        + armEntityDispName
        + ".findInstances(domain, null);\n\n"
        + "\t\tint no_of_instances = instances.getMemberCount();\n"
        + "\t\tSystem.out.println(\"Number of received instances is \"+no_of_instances);\n"
        + "\t\tSystem.out.println(instances);\n"
        + "\t\tint expectedRetType = -1;\n"
        + "\t\tfor (int i=1;i<=no_of_instances;i++) {\n"
        + "\t\t\tEEntity instance = (EEntity) instances.getByIndexEntity(i);\n"
        + "\t\t\tSystem.out.println(\"\");\n"
        + "\t\t\tSystem.out.println(\"instance is \"+instance);\n";
    // now run a cycle to demonstrate how attributes are read.
    SdaiIterator it = entityAttributesInIteration.createIterator();
    while (it.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) entityAttributesInIteration.getCurrentMemberObject(it);
      String atrDisplayName = changeFirstLetterToUpperCase(exp_attr.getName(null));
      String testString = "\t\t\texpectedRetType = M" + armEntityDispName + ".test" + atrDisplayName + "(domain, instance);\n";
      String caseString = "\t\t\tswitch (expectedRetType) {\n";
      Enumeration constants = staticConstants.keys();
      String atrNameUpperCase = atrDisplayName.toUpperCase();
      while (constants.hasMoreElements()) {
        String constant = (String) constants.nextElement();
        constant = constant.toUpperCase();
        if (constant.startsWith(atrNameUpperCase)) {
          caseString = caseString + "\t\t\t\tcase M" + armEntityDispName + "." + constant + ":\n"
              + "\t\t\t\t\t//put appropriate call to getXX method here.\n" + "\t\t\t\tbreak;\n";
        }
      }

      caseString = caseString + "\t\t\tdefault:\n" + "\t\t\t\tSystem.out.println(\"Value not set!\");\n" + "\t\t\t}\n";
      printString = printString + testString + caseString;
    }
    printString = printString + "\t\t}\n" + "\t}\n";

    classMethods.add(printString);
    classMethods.add("\n");
  }

  protected boolean isSubtypeOfOthers(String typeName, Vector candidateTypes, Hashtable relatedEntityDefs) throws SdaiException, Exception {

    EEntity_definition testDefinition = (EEntity_definition) relatedEntityDefs.get(typeName);
    AEntity agDefs = testDefinition.getGeneric_supertypes(null);

    SdaiIterator it = agDefs.createIterator();
    while (it.next()) {
      // grab ordinary supertype
      EEntity_definition supertype = (EEntity_definition) agDefs.getCurrentMemberObject(it);
      // test, whether this supertype is in vector of candidates.
      // If yes, then remove myself from vector and move to its supertypes by recursion
      // watch for return value. If it's true- then return true. Otherwise proceed to next
      // iterations.
      // If not, then pass current vector further to my supertypes. watch for return value
      //	If it's true, then return true. Otherwise proceed to next iterations.
      int count = candidateTypes.size();
      for (int i = 0; i < count; i++) {
        String currentName = (String) candidateTypes.get(i);
        if (currentName == typeName) {
          continue;
        }
        EEntity_definition currentDefinition = (EEntity_definition) relatedEntityDefs.get(currentName);
        if (supertype == currentDefinition) {
          Vector newVector = new Vector();
          int newCount = candidateTypes.size();
          for (int j = 0; j < newCount; j++) {
            String value = (String) candidateTypes.get(j);
            if ((value.equalsIgnoreCase(currentName)) || (value.equalsIgnoreCase(typeName))) {
              continue;
            }
            newVector.add(candidateTypes.get(j));
          }
          if (isSubtypeOfOthers(currentName, newVector, relatedEntityDefs)) {
            return true;
          }
        }
      }
      // okay, it seems this supertype is not in candidates list directly. See what's about
      // its supertypes:
      String myName = getEntDefTargetName(supertype, false);
      if (isSubtypeOfOthers(myName, candidateTypes, relatedEntityDefs)) {
        return true;
      }
    }
    if (candidateTypes.size() == 1) {
      return true;
    }

    return false;

  }

  protected String findLeastCommonDenominator(String armEntityName, AEntity_definition agEntDef) throws SdaiException, Exception {
    // there is only one entity_definition, so take it:
    EEntity_definition ent_def = agEntDef.getByIndex(1);

    // 1. find entity mappings for given arm entity name.
    // first get all known entity mappings for given entity definition:
    AEntity_mapping agMappings = new AEntity_mapping();
    CEntity_mapping.usedinSource(null, ent_def, mappingDomain, agMappings);

    Hashtable commonSupertypes = new Hashtable();
    Hashtable relatedEntityDefs = new Hashtable();

    SdaiIterator it = agMappings.createIterator();
    while (it.next()) {
      EEntity_mapping mapping = (EEntity_mapping) agMappings.getCurrentMemberObject(it);
      EEntity target = mapping.getTarget(null);
      if (!(target instanceof EEntity_definition)) {
        throw new Exception("Entity mapping target is not entity_definition! Don't know how to respond.");
      }
      EEntity_definition targetEntDef = (EEntity_definition) target;

      if (isComplexEntityType(mapping)) {
        AEntity supertypes = targetEntDef.getGeneric_supertypes(null);
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
          addSupertypesToHashtable(commonSupertypes, superType, relatedEntityDefs, 0);
        }
      }
      else {
        addSupertypesToHashtable(commonSupertypes, targetEntDef, relatedEntityDefs, 1);
      }

    }
    // ok, now analyze generated hash table: try to find entries, where
    // stored value is equal to number of entity_mappings processed.
    Vector candidateTypes = new Vector();
    int noOfMappings = agMappings.getMemberCount();
    Enumeration keys = commonSupertypes.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      Integer value = (Integer) commonSupertypes.get(key);
      if (value.intValue() == noOfMappings) {
        candidateTypes.add(key);
      }
    }
    // now run a cycle to print out types:
    int count = candidateTypes.size();

    if (count == 1) {
      return (String) candidateTypes.get(0);
    }

    if (count == 0) {
      return "EEntity";
    }

    for (int i = 0; i < count; i++) {
      // idea is simple: iterate through vector, and for each element ask whether it is
      // subtype of all other elements in vector. If such element is found, return it.
      String typeName = (String) candidateTypes.get(i);
      if (isSubtypeOfOthers(typeName, candidateTypes, relatedEntityDefs)) {
        return typeName;
      }
      //System.out.println("i="+i+" type ="+typeName);
    }
    System.out.println("NO COMMON denominator for:");
    for (int i = 0; i < count; i++) {
      // idea is simple: iterate through vector, and for each element ask whether it is
      // subtype of all other elements in vector. If such element is found, return it.
      String typeName = (String) candidateTypes.get(i);
      System.out.println("i=" + i + " type =" + typeName);
    }
    throw new Exception("List of candidates had no common denominator! That should not happen!");
  }

  protected boolean checkIfComplexAndGetInterfaces(String aimType, Vector implInterfaces) throws SdaiException, Exception {
    SdaiIterator it = entityMappingsInIteration.createIterator();
    while (it.next()) {
      EEntity_mapping entMap = (EEntity_mapping) entityMappingsInIteration.getCurrentMemberObject(it);
      String currentTargetName = getEntMapTargetName(entMap, false);
      if (!currentTargetName.equalsIgnoreCase(aimType)) {
        continue;
      }
      if (isComplexEntityType(entMap)) {
        getImplementedInterfaces(implInterfaces, entMap);
        return true;
      }
      else {
        return false;
      }
    }
    throw new Exception("Given aimType " + aimType + " was not found in set of allowable entity mappings!");
  }

  /**
   *
   */
  public void generateTestXXAttributeMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    String documentationHeader = "";
    SdaiIterator atrIt = entityAttributesInIteration.createIterator();
    while (atrIt.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) entityAttributesInIteration.getCurrentMemberObject(atrIt);
      // dont' try to optimize display name by calling generateValidName because we must capture all and
      // discuss all cases when attribute name is unusual.
      String atrDisplayName = changeFirstLetterToUpperCase(exp_attr.getName(null));

      documentationHeader = "static int <B>test</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException";
      doc.addAttributeInfo(exp_attr.getName(null), documentationHeader);

      String fullMethod = "\tpublic static int test" + atrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException {\n"
          + "\t\tString linkedAttrMappingName[] = new String[1];\n" + "\t\tObject o = null;\n"
          + "\t\to = getAnyValueForAttribute(domain, metaDomain, at" + atrDisplayName + ", instance, linkedAttrMappingName);\n"
          + "\t\tif (o == null)\n\t\t\treturn MappingUtil.VA_NSET;\n";
      Hashtable aimTypeToOverload = (Hashtable) attrMethodsInIteration.get(exp_attr.getName(null));
      Enumeration items = aimTypeToOverload.keys();
      //String singleEntry ="";
      while (items.hasMoreElements()) {
        String aimType = (String) items.nextElement();
        Hashtable overloads = (Hashtable) aimTypeToOverload.get(aimType);
        Enumeration exactNames = overloads.keys();
        while (exactNames.hasMoreElements()) {
          String exactName = (String) exactNames.nextElement();
          String staticConstant = (String) overloads.get(exactName);
          String testEntry = "";
          // by agreement, in cases when exactName is empty string, use staticConstant value for key and
          // for value.
          //if (exactName.equalsIgnoreCase(staticConstant))
          if (exactName.length() == 0)
//						testEntry = "\t\tif (o instanceof "+aimType+")\n"+
//									   "\t\t\treturn "+staticConstant+";\n";
          {
            testEntry = "\t\tif (compatibleObjectValue(o, " + aimType + ".class))\n" + "\t\t\treturn " + staticConstant + ";\n";
          }
          else
//						testEntry = "\t\tif ((o instanceof "+aimType+") && (linkedAttrMappingName[0].equalsIgnoreCase(\""+
//									exactName+"\")))\n"+
//									 "\t\t\treturn "+staticConstant+";\n";
          {
            testEntry = "\t\tif ((compatibleObjectValue(o, " + aimType + ".class)) && (linkedAttrMappingName[0].equalsIgnoreCase(\"" + exactName
                + "\")))\n" + "\t\t\treturn " + staticConstant + ";\n";
          }
          fullMethod = fullMethod + testEntry;
        }
      }
      fullMethod = fullMethod + "\t\tthrow new SdaiException (SdaiException.VA_NVLD, testErrorString1);\n" + "\t}\n";
      classMethods.add(fullMethod);

      classMethods.add("\n");
    }
  }

  /**
   * NOTE: this method should generate all attributes, but currently it's limited to explicit ones.
   */
  public void generateAttributesBlock(Vector attributes, EEntity_definition entDef) throws SdaiException, Exception {
    // declare a hash table to verify whether generated string is contained in output already.
    Hashtable table = new Hashtable();

    // from given entity definition we should parse all explicit attributes and declare them:
    AExplicit_attribute attr = entDef.getExplicit_attributes(null);
    // remember aggregate for using in other methods:
    entityAttributesInIteration = attr;
    SdaiIterator it = attr.createIterator();
    // generate attribute and its mappings:
//		System.out.println("=========================================");
//		System.out.println("We are working on ent def: "+entDef);
    while (it.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) attr.getCurrentMemberObject(it);
      Vector atrMapsVector = new Vector();
      String attrName = exp_attr.getName(null);
      String printStr = "\tstatic EExplicit_attribute at" + generateValidName(changeFirstLetterToUpperCase(attrName))
          + " = (EExplicit_attribute) findArmAttribute(armEntity, \"" + attrName + "\");\n";
      attributes.add(printStr);
      table.put(printStr, printStr);

      // good. Now get aggregate of existing attribute mappings for this attribute:
      AGeneric_attribute_mapping agAtrMap = new AGeneric_attribute_mapping();
      CGeneric_attribute_mapping.usedinSource(null, exp_attr, mappingDomain, agAtrMap);

      SdaiIterator atrMapIt = agAtrMap.createIterator();
      while (atrMapIt.next()) {
        EGeneric_attribute_mapping atrMapping = (EGeneric_attribute_mapping) agAtrMap.getCurrentMemberObject(atrMapIt);
        // read what entity_mapping is associated with this atr_mapping:
        EEntity_mapping entMapping = atrMapping.getParent_entity(null);
        // now test, whether this mapping is in allowed mappings list, as
        // the same attribute can be mapped differently in supertype and subtype.
        if (!acceptableMapping(entMapping)) {
          continue;
        }

        atrMapsVector.add(atrMapping);
        String targetDisplayName = getEntMapTargetName(entMapping, true);
        // check if there is data_type related with this attribute mapping:
        String dataTypeName = getDataTypeName(atrMapping, true);
        String dataTypeExactName = getDataTypeName(atrMapping, false);
        // NOTE: mapping variable name should reflect entity_mapping to allow us to find correct mapping
        // later.
        String mapText = "\tprivate static EGeneric_attribute_mapping am" + targetDisplayName + "__"
            + ((dataTypeName.length() == 0) ? "" : dataTypeName + "__") + generateValidName(changeFirstLetterToUpperCase(attrName))
            + " = findAttributeMapping(emTo" + targetDisplayName + ", at" + generateValidName(changeFirstLetterToUpperCase(attrName)) + ", \""
            + ((dataTypeExactName.length() == 0) ? "" : dataTypeExactName) + "\");\n";
        // this should help us to avoid mappings that are identical in subtypes of supertype,
        // and we are generating supertype early binding class.
        if (!table.containsKey(mapText)) {
          attributes.add(mapText);
          table.put(mapText, mapText);
        }

      }
      // good, now save info about mappings:
      attrMappingsInIteration.put(attrName, atrMapsVector);
      attributes.add("\n");
    }
  }

  /**
   *
   */
  public void generateMappingsBlock(Vector entityMappings, EEntity_definition entDef) throws SdaiException, Exception {
    // declare a hash table to verify whether generated string is contained in output already.
    Hashtable table = new Hashtable();

    // first get all known entity mappings for given entity definition:
    AEntity_mapping agMappings = new AEntity_mapping();
    CEntity_mapping.usedinSource(null, entDef, mappingDomain, agMappings);

    SdaiIterator it = agMappings.createIterator();
    while (it.next()) {
      EEntity_mapping mapping = (EEntity_mapping) agMappings.getCurrentMemberObject(it);

      String targetName = getEntMapTargetName(mapping, false);
      String mappingEntry = "\tprivate static EEntity_mapping emTo" + generateValidName(changeFirstLetterToUpperCase(targetName))
          + " = findEntityMapping(armEntity, \"" + targetName + "\");\n";
      if (!table.containsKey(mappingEntry)) {
        entityMappings.add(mappingEntry);
        table.put(mappingEntry, mappingEntry);
      }
    }
    entityMappingsInIteration = agMappings;
  }

  protected void generateGetMethodForARM(Vector getContents, AEntity_definition agEntDef, String armEntityName, String aimEntityName, String gamVariableName,
      EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter, EExplicit_attribute exp_attr)
      throws SdaiException, Exception {
//System.out.println("get for ARM");										   	
//System.out.println("attrMapping: "+ egaMapping);
//System.out.println("gamVariablename:"+gamVariableName);
//System.out.println("armEntityName:"+ armEntityName);
    String getHeader = "";
    String getBody = "";
    String bodyPrefix = "E"; // default prefix.
    String overlPrefix = "E";
    boolean isAggregate = false;

    EEntity domain = exp_attr.getDomain(null);
    if (domain instanceof EAggregation_type) {
      bodyPrefix = "A";
//			overlPrefix = "E";
      isAggregate = true;
    }

    String retValueType = findLeastCommonDenominator(armEntityName, agEntDef);
    String leastCommDenom = retValueType;
//System.out.println("leastCommonDenominator is "+ retValueType);		

    if (!retValueType.equalsIgnoreCase("EEntity")) {
      // append importStatements with new entry, if required:
      String importClassName = findFullClassName(retValueType, importStatements);
      if (!importStatements.containsKey(importClassName)) {
        importStatements.put(importClassName, importClassName);
      }
//!!			retValueType = "E"+changeFirstLetterToUpperCase(retValueType);
      retValueType = bodyPrefix + changeFirstLetterToUpperCase(retValueType);
    }
    if (retValueType.equalsIgnoreCase("EEntity")) {
      bodyPrefix = "";
      overlPrefix = "";
      if (isAggregate) {
        retValueType = "AEntity";
      }
    }
    String docHeader = "static " + retValueType + " <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance, M"
        + changeFirstLetterToUpperCase(armEntityName) + " a) throws SdaiException";
    getHeader = "\tpublic static " + retValueType + " get" + atrDisplayName + "(ASdaiModel domain, EEntity instance, M"
        + changeFirstLetterToUpperCase(armEntityName) + " a) throws SdaiException {\n";
//System.out.println("getHeader is "+getHeader);
    if (!isComplexEntityType(entMap)) {
      getBody = "\t\tif (instance instanceof E" + changeFirstLetterToUpperCase(aimEntityName) + ") {\n" + "\t\t\tObject o = instance.getMappedAttribute("
          + gamVariableName + ", domain, metaDomain, currentMappingMode);\n";
      if (!isAggregate) {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") o;\n" + "\t\t}\n";
      }
      else {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") buildAggregate(o, " + retValueType + ".class);\n" + "\t\t}\n";
      }
//System.out.println("get body is "+getBody);			
    }
    else {
      // we have a mixed complex aim entity type, so a get body should be special:
      // given instance should be 'instanceof' all three(or two, or whatever) interfaces
      // that this type implements, only then we can use our attribute_mapping.
      Vector implInterfaces = new Vector();
      getImplementedInterfaces(implInterfaces, entMap);
      getBody = "\t\tif (";
      for (int i = 0; i < implInterfaces.size(); i++) {
        getBody = getBody + "instance instanceof " + (String) implInterfaces.get(i) + " && ";
      }
      // clear last 4 characters from output:
      if (getBody.endsWith(" && ")) {
        int endIndex = getBody.length() - 4;
        getBody = getBody.substring(0, endIndex);
      }
      getBody = getBody + ") {\n\t\t\tObject o = instance.getMappedAttribute(" + gamVariableName + ", domain, metaDomain, currentMappingMode);\n";
      if (!isAggregate) {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") o;\n" + "\t\t}\n";
      }
      else {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") buildAggregate(o, " + retValueType + ".class);\n" + "\t\t}\n";
      }
    }
    // ok. Now, we need to check if this method should be generated

    // first, do a check on data_type. Apply this check only when attribute type is
    // select type:
    if (isSelectInside(exp_attr)) {
      // use data_type field to check whether this method is desired and generated correctly.
      String typeNameInMapping = getDataTypeName(egaMapping, false);
      if (!typeNameInMapping.equalsIgnoreCase(armEntityName)) {
        return; // we ommit overhead generation.
      }
    }
    else {
      // second, if we have not a select type, apply this check
      EEntity atrDomain = null;
      if (egaMapping instanceof EAttribute_mapping) {
        EAttribute_mapping eaMapping = (EAttribute_mapping) egaMapping;
        if (eaMapping.testDomain(null)) {
          atrDomain = eaMapping.getDomain(null);
        }
      }
      if (atrDomain instanceof EEntity_mapping) {
        EEntity_mapping domEntMapping = (EEntity_mapping) atrDomain;
        EEntity_definition source = domEntMapping.getSource(null);
        if (!source.getName(null).equalsIgnoreCase(armEntityName)) {
          return;
        }
      }
    }
    /*
     * System.out.println("egaMapping is "+ egaMapping);
     * String typeNameInMapping = getDataTypeName(egaMapping, false);
     * System.out.println("typeName in mapping is "+typeNameInMapping);
     * System.out.println("armEntityName is "+armEntityName);
     * if (!typeNameInMapping.equalsIgnoreCase(armEntityName)) {
     * if (typeNameInMapping.length() == 0) {
     * System.out.println("Special case detected:");
     * System.out.println("AttributeName: "+ atrDisplayName);
     * System.out.println("AtrMapping "+egaMapping);
     * }
     * return; // we ommit overhead generation.
     * }
     */
    String staticConst = atrDisplayName.toUpperCase() + "__M" + armEntityName.toUpperCase();
    doc.addStaticConstant(atrDisplayName.toLowerCase(), staticConst);

    String searchKey = overlPrefix + changeFirstLetterToUpperCase(leastCommDenom);
    if (aimToOverloadParameter.containsKey(searchKey)) {
      Hashtable overloads = (Hashtable) aimToOverloadParameter.get(searchKey);
      String dataTypeExactName = getDataTypeName(egaMapping, false);
      if (overloads.containsKey(dataTypeExactName)) {
        String value = (String) overloads.get(dataTypeExactName);
        if (!value.equalsIgnoreCase(staticConst)) {
          System.out.println("ARM: Something wrong: value=" + value + "dataTypeExactName=" + dataTypeExactName + "staticConst=" + staticConst
              + " searchKey =" + searchKey + " atrDisplayName=" + atrDisplayName + "hashtable is" + overloads);
        }
      }
      else {
        overloads.put(dataTypeExactName, staticConst);
      }
    }
    else {
      Hashtable overloads = new Hashtable();
      String dataTypeExactName = getDataTypeName(egaMapping, false);
      overloads.put(dataTypeExactName, staticConst);
      aimToOverloadParameter.put(searchKey, overloads);
    }

    doc.addAttributeInfo(atrDisplayName.toLowerCase(), docHeader);

    getContents.add(getHeader);
    getContents.add(getBody);
    getContents.add(staticConst);
  }

  protected void generateGetMethodForAIM(Vector getContents, AEntity_definition agEntDef, String retTypeName, String aimEntityName, String gamVariableName,
      EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter, EExplicit_attribute exp_attr)
      throws SdaiException, Exception {
//System.out.println("get for AIM");										   	
//System.out.println("attrMapping: "+ egaMapping);
//System.out.println("gamVariablename:"+gamVariableName);
//System.out.println("retTypeName:"+ retTypeName);
    String getHeader = "";
    String getBody = "";
    String retValueType = retTypeName;
    String bodyPrefix = "E"; // default prefix.
    String overlPrefix = "E";
    boolean isAggregate = false;

    EEntity domain = exp_attr.getDomain(null);
    if (domain instanceof EAggregation_type) {
      bodyPrefix = "A";
      overlPrefix = "E";
      isAggregate = true;
    }

    if (!retValueType.equalsIgnoreCase("EEntity")) {
      // append importStatements with new entry, if required:
      String importClassName = findFullClassName(retValueType, importStatements);
      if (!importStatements.containsKey(importClassName)) {
        importStatements.put(importClassName, importClassName);
      }
      retValueType = bodyPrefix + changeFirstLetterToUpperCase(retValueType);
    }
    if (retTypeName.equalsIgnoreCase("EEntity")) {
      bodyPrefix = "";
      overlPrefix = "";
      if (isAggregate) {
        retValueType = "AEntity";
      }
    }

    String docHeader = "static " + retValueType + " <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance, " + overlPrefix
        + changeFirstLetterToUpperCase(retTypeName) + " a) throws SdaiException";
    getHeader = "\tpublic static " + retValueType + " get" + atrDisplayName + "(ASdaiModel domain, EEntity instance, " + overlPrefix
        + changeFirstLetterToUpperCase(retTypeName) + " a) throws SdaiException {\n";
    if (!isComplexEntityType(entMap)) {
      getBody = "\t\tif (instance instanceof E" + changeFirstLetterToUpperCase(aimEntityName) + ") {\n" + "\t\t\tObject o = instance.getMappedAttribute("
          + gamVariableName + ", domain, metaDomain, currentMappingMode);\n";
      if (!isAggregate) {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") o;\n" + "\t\t}\n";
      }
      else {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") buildAggregate(o, " + retValueType + ".class);\n" + "\t\t}\n";
      }
    }
    else {
      // we have a mixed complex aim entity type, so a get body should be special:
      // given instance should be 'instanceof' all three(or two, or whatever) interfaces
      // that this type implements, only then we can use our attribute_mapping.
      Vector implInterfaces = new Vector();
      getImplementedInterfaces(implInterfaces, entMap);
      getBody = "\t\tif (";
      for (int i = 0; i < implInterfaces.size(); i++) {
        getBody = getBody + "instance instanceof " + (String) implInterfaces.get(i) + " && ";
      }
      // clear last 4 characters from output:
      if (getBody.endsWith(" && ")) {
        int endIndex = getBody.length() - 4;
        getBody = getBody.substring(0, endIndex);
      }
      getBody = getBody + ") {\n\t\t\tObject o = instance.getMappedAttribute(" + gamVariableName + ", domain, metaDomain, currentMappingMode);\n";
      if (!isAggregate) {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") o;\n" + "\t\t}\n";
      }
      else {
        getBody = getBody + "\t\t\treturn (" + retValueType + ") buildAggregate(o, " + retValueType + ".class);\n\t\t}\n";
      }
    }
    String staticConst = atrDisplayName.toUpperCase() + "__" + retTypeName.toUpperCase();
    doc.addStaticConstant(atrDisplayName.toLowerCase(), staticConst);

    String searchKey = overlPrefix + changeFirstLetterToUpperCase(retTypeName);
    if (aimToOverloadParameter.containsKey(searchKey)) {
      Hashtable overloads = (Hashtable) aimToOverloadParameter.get(searchKey);
      String dataTypeExactName = getDataTypeName(egaMapping, false);
      if (overloads.containsKey(dataTypeExactName)) {
        String value = (String) overloads.get(dataTypeExactName);
        if (!value.equalsIgnoreCase(staticConst)) {
          System.out.println("AIM: Something wrong: dataTypeExactName=" + dataTypeExactName + "staticConst=" + staticConst + " searchKey ="
              + searchKey + " atrDisplayName=" + atrDisplayName + "hashtable is" + overloads);
        }
      }
      else {
        overloads.put(dataTypeExactName, staticConst);
      }
    }
    else {
      Hashtable overloads = new Hashtable();
      String dataTypeExactName = getDataTypeName(egaMapping, false);
      overloads.put(dataTypeExactName, staticConst);
      aimToOverloadParameter.put(searchKey, overloads);
    }
    doc.addAttributeInfo(atrDisplayName.toLowerCase(), docHeader);

    getContents.add(getHeader);
    getContents.add(getBody);
    getContents.add(staticConst);
  }

  protected String returnTypeCastLine(String retValueType) {
    if (retValueType.equalsIgnoreCase("int")) {
      return "((Integer) o).intValue()";
    }
    if (retValueType.equalsIgnoreCase("double")) {
      return "((Double) o).doubleValue()";
    }
    if (retValueType.equalsIgnoreCase("boolean")) {
      return "((Boolean) o).booleanValue()";
    }
    return "(" + retValueType + ") o";
  }

  protected void generateGetMethodForSimple(Vector getContents, AEntity_definition agEntDef, String retTypeName, String aimEntityName,
      String gamVariableName, EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter,
      EExplicit_attribute exp_attr) throws SdaiException, Exception {
    String getHeader = "";
    String getBody = "";
    String retValueType = retTypeName;
    boolean isAggregate = false;

    EEntity domain = exp_attr.getDomain(null);
    if (domain instanceof EAggregation_type) {
      isAggregate = true;
      retValueType = "ArrayList";
      if (!importStatements.containsKey("java.util.ArrayList")) {
        importStatements.put("java.util.ArrayList", "java.util.ArrayList");
      }
    }

    String docHeader = "static " + retValueType + " <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance, " + retTypeName
        + " a) throws SdaiException";
    getHeader = "\tpublic static " + retValueType + " get" + atrDisplayName + "(ASdaiModel domain, EEntity instance, " + retTypeName
        + " a) throws SdaiException {\n";

    if (!isComplexEntityType(entMap)) {
      getBody = "\t\tif (instance instanceof E" + changeFirstLetterToUpperCase(aimEntityName) + ") {\n" + "\t\t\tObject o = instance.getMappedAttribute("
          + gamVariableName + ", domain, metaDomain, currentMappingMode);\n";
      if (!isAggregate) {
        getBody = getBody + "\t\t\treturn " + returnTypeCastLine(retValueType) + ";\n" + "\t\t}\n";
      }
      else {
        getBody = getBody + "\t\t\treturn buildArrayList(o);\n" + "\t\t}\n";
      }
    }
    else {
      // we have a mixed complex aim entity type, so a get body should be special:
      // given instance should be 'instanceof' all three(or two, or whatever) interfaces
      // that this type implements, only then we can use our attribute_mapping.
      Vector implInterfaces = new Vector();
      getImplementedInterfaces(implInterfaces, entMap);
      getBody = "\t\tif (";
      for (int i = 0; i < implInterfaces.size(); i++) {
        getBody = getBody + "instance instanceof " + (String) implInterfaces.get(i) + " && ";
      }
      // clear last 4 characters from output:
      if (getBody.endsWith(" && ")) {
        int endIndex = getBody.length() - 4;
        getBody = getBody.substring(0, endIndex);
      }
      getBody = getBody + ") {\n\t\t\tObject o = instance.getMappedAttribute(" + gamVariableName + ", domain, metaDomain, currentMappingMode);\n";

      if (!isAggregate) {
        getBody = getBody + "\t\t\treturn " + returnTypeCastLine(retValueType) + ";\n" + "\t\t}\n";
      }
      else {
        getBody = getBody + "\t\t\treturn buildArrayList(o);\n" + "\t\t}\n";
      }
    }
    String staticConst = atrDisplayName.toUpperCase() + "__" + retTypeName.toUpperCase();
    doc.addStaticConstant(atrDisplayName.toLowerCase(), staticConst);

    // now, for int, double, boolean generate Object names:
    if (retValueType.equalsIgnoreCase("int")) {
      retValueType = "Integer";
    }
    if (retValueType.equalsIgnoreCase("double")) {
      retValueType = "Double";
    }
    if (retValueType.equalsIgnoreCase("boolean")) {
      retValueType = "Boolean";
    }

    String baseTypeForTest = "";
    if (isAggregate) {
      if (retTypeName.equalsIgnoreCase("int")) {
        baseTypeForTest = "Integer";
      }
      if (retTypeName.equalsIgnoreCase("double")) {
        baseTypeForTest = "Double";
      }
      if (retTypeName.equalsIgnoreCase("boolean")) {
        baseTypeForTest = "Boolean";
      }
      else {
        baseTypeForTest = retTypeName;
      }
    }
    else {
      baseTypeForTest = retValueType;
    }

    if (aimToOverloadParameter.containsKey(baseTypeForTest)) {
      Hashtable overloads = (Hashtable) aimToOverloadParameter.get(baseTypeForTest);
      String dataTypeExactName = getDataTypeName(egaMapping, false);
      if (overloads.containsKey(dataTypeExactName)) {
        String value = (String) overloads.get(dataTypeExactName);
        if (!value.equalsIgnoreCase(staticConst)) {
          System.out.println("Simple: Something wrong: dataTypeExactName=" + dataTypeExactName + "staticConst=" + staticConst + " baseTypeForTest ="
              + baseTypeForTest + " atrDisplayName=" + atrDisplayName + "hashtable is" + overloads);
        }
      }
      else {
        overloads.put(dataTypeExactName, staticConst);
      }

    }
    else {
      Hashtable overloads = new Hashtable();
      String dataTypeExactName = getDataTypeName(egaMapping, false);
      overloads.put(dataTypeExactName, staticConst);
      aimToOverloadParameter.put(baseTypeForTest, overloads);
    }

    doc.addAttributeInfo(atrDisplayName.toLowerCase(), docHeader);

    getContents.add(getHeader);
    getContents.add(getBody);
    getContents.add(staticConst);
  }

  protected boolean isSimpleReturnType(String retType) {
    if (retType.equalsIgnoreCase("String")) {
      return true;
    }
    else if (retType.equalsIgnoreCase("Number")) {
      return true;
    }
    else if (retType.equalsIgnoreCase("double")) {
      return true;
    }
    else if (retType.equalsIgnoreCase("Integer")) {
      return true;
    }
    else if (retType.equalsIgnoreCase("int")) {
      return true;
    }
    else if (retType.startsWith("E")) {
      return false;
    }

    return false;
  }

  /**
   *
   */
  public void generateGetXXAttributeMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    String getHeader = "";
    String getBody = "";
    String staticConst = "";
    attrMethodsInIteration.clear();

    SdaiIterator atrIt = entityAttributesInIteration.createIterator();
    while (atrIt.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) entityAttributesInIteration.getCurrentMemberObject(atrIt);

//System.out.println("-------------------------------------");			
//System.out.println("exp atr is "+exp_attr);			
      // now, decl hashtable for storing grouped results of getXX generation:
      Hashtable getMethods = new Hashtable();
      // now, decl hashtable for storing related aim entity type names and overload parameters:
      Hashtable aimToOverloadParameter = new Hashtable();

      // dont' try to optimize display name by calling generateValidName because we must capture and
      // discuss all cases when attribute name is unusual.
      String atrDisplayName = changeFirstLetterToUpperCase(exp_attr.getName(null));

      // now, try to determine final type of arm attribute. It shall be used to generate
      // return type of getXX method,if attribute mapping won't be available for determining
      // more exact return type of getXX method.
      EEntity armAtrType = SimpleOperationsOnExtended.getAttributeDomain(exp_attr);
//System.out.println("armAtrType is "+ armAtrType);			
      Vector atrMappings = (Vector) attrMappingsInIteration.get(exp_attr.getName(null));
      if (atrMappings == null) {
        throw new Exception("Malformed hashtable for current attribute!" + exp_attr);
      }

      // each attribute mapping theoretically leads us to a separate getXX method.
      int mapCount = atrMappings.size();
//System.out.println("no of atrMappings is "+ mapCount);			
      for (int i = 0; i < mapCount; i++) {
        // this hashtable stores original EEntities, which were used to build retValues array of Strings.
        Hashtable allowedTypes = new Hashtable();
        EGeneric_attribute_mapping ega_mapping = (EGeneric_attribute_mapping) atrMappings.get(i);
        // now, read associated entity_mapping:
        EEntity_mapping entMap = ega_mapping.getParent_entity(null);
        // now, read ent definition in aim, to be used inside this method for safe typecasting.
        String aimEntityName = getEntMapTargetName(entMap, false);
        String gamVariableName = makeAttrMappingVariableName(aimEntityName, atrDisplayName, ega_mapping);
        String[] retValues = determineFinalTargetType(armAtrType, ega_mapping, exp_attr, allowedTypes);

        int retCount = retValues.length;
//System.out.println("retCount is "+ retCount);				
        // what we should do is to push under single getXX method as much as we can different
        // attribute mappings. They should be grouped by return value of getXX method.
        for (int j = 0; j < retCount; j++) {
//					System.out.println("retvalues[j] is "+j+" "+retValues[j]);
          // first thing to do is to determine what kind of return value we have.
          String retValueType = "";
          AEntity_definition agEntDef = new AEntity_definition();

          Vector getContents = new Vector();

          if (isArmEntityName(retValues[j], allowedTypes, agEntDef)) {
//						System.out.println("arm entity name");
            generateGetMethodForARM(getContents, agEntDef, retValues[j], aimEntityName, gamVariableName, ega_mapping, atrDisplayName, entMap,
                aimToOverloadParameter, exp_attr);
          }
          else {
            if (isSimpleReturnType(retValues[j])) {
//							System.out.println("simple entity name");
              generateGetMethodForSimple(getContents, agEntDef, retValues[j], aimEntityName, gamVariableName, ega_mapping, atrDisplayName,
                  entMap, aimToOverloadParameter, exp_attr);
            }
            else {
//							System.out.println("aim entity name");
              generateGetMethodForAIM(getContents, agEntDef, retValues[j], aimEntityName, gamVariableName, ega_mapping, atrDisplayName, entMap,
                  aimToOverloadParameter, exp_attr);
            }
          }
          if (getContents.size() < 3) {
            continue; // no output was generated.
          }
          getHeader = (String) getContents.get(0);
          getBody = (String) getContents.get(1);
          staticConst = (String) getContents.get(2);
          // it's not important, what value will be stored in values entry for hashtable,
          // it was planned to be used in generating main method for testing, but not
          // used now.
          staticConstants.put(staticConst, retValues[j]);

          // try to find hashtable to place getBody value:
          if (getMethods.containsKey(getHeader)) {
            Hashtable bodies = (Hashtable) getMethods.get(getHeader);
            if (!bodies.containsKey(aimEntityName)) {
              bodies.put(aimEntityName, getBody);
            }
            getMethods.put(getHeader, bodies);
          }
          else {
            // create new child hashtable:
            Hashtable bodies = new Hashtable();
            bodies.put(aimEntityName, getBody);
            getMethods.put(getHeader, bodies);
          }

        }

      }
      attrMethodsInIteration.put(exp_attr.getName(null), aimToOverloadParameter);

      // well, now we are ready to print/generate real get methods:
      // each key becomes a decl of real method, and each values vector becomes
      // body of that method. After values vector is printed, a closing bracket is
      // added.
      Enumeration headers = getMethods.keys();
      String methodString = "";
      while (headers.hasMoreElements()) {
        String printString = (String) headers.nextElement();
        methodString = printString;
        // now get values hashtable for this key:
        Hashtable bodies = (Hashtable) getMethods.get(printString);
        Enumeration body_entries = bodies.keys();
        while (body_entries.hasMoreElements()) {
          String key = (String) body_entries.nextElement();
          printString = (String) bodies.get(key);
          methodString = methodString + printString;
        }
        // append body with exception-based warning:
        printString = "\t\tthrow new SdaiException(SdaiException.VA_NVLD, getErrorString1);\n" + "\t}\n\n";
        methodString = methodString + printString;
        classMethods.add(methodString);
      }

    }
  }

  protected void generateSetMethodForARM(Vector setContents, AEntity_definition agEntDef, String armEntityName, String aimEntityName, String gamVariableName,
      EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, boolean shouldBeFirst[], EExplicit_attribute exp_attr)
      throws SdaiException, Exception {
    String typeNameInMapping = getDataTypeName(egaMapping, false);
    if (!typeNameInMapping.equalsIgnoreCase(armEntityName)) {
      return; // we ommit overhead generation.
    }

    EEntity domain = exp_attr.getDomain(null);
    if (domain instanceof EAggregation_type) {
      return; // don't generate set method for aggregate attribute
    }

    String setHeader = "";
    String setBody = "";
    String docHeader = "static EEntity <B>set</B>" + atrDisplayName + "(ASdaiModel domain," + " EEntity instance, Object newValue, M"
        + changeFirstLetterToUpperCase(armEntityName) + " a) throws SdaiException";

    setHeader = "\tpublic static EEntity set" + atrDisplayName + "(ASdaiModel domain, EEntity instance, Object newValue, M"
        + changeFirstLetterToUpperCase(armEntityName) + " a) throws SdaiException {\n";

    if (!isComplexEntityType(entMap)) {
      setBody = "\t\tif (instance instanceof E" + changeFirstLetterToUpperCase(aimEntityName) + ") {\n" + "\t\t\tinstance.setMappedAttribute("
          + gamVariableName + ", domain, metaDomain, newValue);\n" + "\t\t\treturn instance;\n" + "\t\t}\n";
      shouldBeFirst[0] = false;
    }
    else {
      // we have a mixed complex aim entity type, so a get body should be special:
      // given instance should be 'instanceof' all three(or two, or whatever) interfaces
      // that this type implements, only then we can use our attribute_mapping.
      Vector implInterfaces = new Vector();
      getImplementedInterfaces(implInterfaces, entMap);
      setBody = "\t\tif (";
      for (int i = 0; i < implInterfaces.size(); i++) {
        setBody = setBody + "instance instanceof " + (String) implInterfaces.get(i) + " && ";
      }
      // clear last 4 characters from output:
      if (setBody.endsWith(" && ")) {
        int endIndex = setBody.length() - 4;
        setBody = setBody.substring(0, endIndex);
      }
      setBody = setBody + ") {\n\t\t\tinstance.setMappedAttribute(" + gamVariableName + ", domain, metaDomain, newValue);\n" + "\t\t\treturn instance;\n"
          + "\t\t}\n";
      shouldBeFirst[0] = true;
    }
    doc.addAttributeInfo(atrDisplayName.toLowerCase(), docHeader);
    setContents.add(setHeader);
    setContents.add(setBody);
  }

  protected void generateSetMethodForAIM(Vector setContents, AEntity_definition agEntDef, String retTypeName, String aimEntityName, String gamVariableName,
      EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, boolean shouldBeFirst[], EExplicit_attribute exp_attr)
      throws SdaiException, Exception {
    EEntity domain = exp_attr.getDomain(null);
    if (domain instanceof EAggregation_type) {
      return; // don't generate set method for aggregate attribute
    }

    String setHeader = "";
    String setBody = "";
    String prefix = "E";
    if (retTypeName.equalsIgnoreCase("EEntity")) {
      prefix = "";
    }

    String docHeader = "static EEntity <B>set</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance, Object newValue, " + prefix
        + changeFirstLetterToUpperCase(retTypeName) + " a) throws SdaiException";

    setHeader = "\tpublic static EEntity set" + atrDisplayName + "(ASdaiModel domain, EEntity instance, Object newValue, " + prefix
        + changeFirstLetterToUpperCase(retTypeName) + " a) throws SdaiException {\n";
    if (!isComplexEntityType(entMap)) {
      setBody = "\t\tif (instance instanceof E" + changeFirstLetterToUpperCase(aimEntityName) + ") {\n" + "\t\t\tinstance.setMappedAttribute("
          + gamVariableName + ", domain, metaDomain, newValue);\n" + "\t\t\treturn instance;\n" + "\t\t}\n";
      shouldBeFirst[0] = false;
    }
    else {
      // we have a mixed complex aim entity type, so a set body should be special:
      // given instance should be 'instanceof' all three(or two, or whatever) interfaces
      // that this type implements, only then we can use our attribute_mapping.
      Vector implInterfaces = new Vector();
      getImplementedInterfaces(implInterfaces, entMap);
      setBody = "\t\tif (";
      for (int i = 0; i < implInterfaces.size(); i++) {
        setBody = setBody + "instance instanceof " + (String) implInterfaces.get(i) + " && ";
      }
      // clear last 4 characters from output:
      if (setBody.endsWith(" && ")) {
        int endIndex = setBody.length() - 4;
        setBody = setBody.substring(0, endIndex);
      }
      setBody = setBody + ") {\n\t\t\tinstance.setMappedAttribute(" + gamVariableName + ", domain, metaDomain, newValue);\n" + "\t\t\treturn instance;\n"
          + "\t\t}\n";
      shouldBeFirst[0] = true;
    }
    doc.addAttributeInfo(atrDisplayName.toLowerCase(), docHeader);
    setContents.add(setHeader);
    setContents.add(setBody);
  }

  protected void generateSetMethodForSimple(Vector setContents, AEntity_definition agEntDef, String retTypeName, String aimEntityName,
      String gamVariableName, EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, boolean shouldBeFirst[],
      EExplicit_attribute exp_attr) throws SdaiException, Exception {
    EEntity domain = exp_attr.getDomain(null);
    if (domain instanceof EAggregation_type) {
      return; // don't generate set method for aggregate attribute
    }

    String setHeader = "";
    String setBody = "";
    String docHeader = "static EEntity <B>set</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance, Object newValue, " + retTypeName
        + " a) throws SdaiException";

    setHeader = "\tpublic static EEntity set" + atrDisplayName + "(ASdaiModel domain, EEntity instance, Object newValue, " + retTypeName
        + " a) throws SdaiException {\n";

    if (!isComplexEntityType(entMap)) {
      setBody = "\t\tif (instance instanceof E" + changeFirstLetterToUpperCase(aimEntityName) + ") {\n" + "\t\t\tinstance.setMappedAttribute("
          + gamVariableName + ", domain, metaDomain, newValue);\n" + "\t\t\treturn instance;\n" + "\t\t}\n";
      shouldBeFirst[0] = false;
    }
    else {
      // we have a mixed complex aim entity type, so a get body should be special:
      // given instance should be 'instanceof' all three(or two, or whatever) interfaces
      // that this type implements, only then we can use our attribute_mapping.
      Vector implInterfaces = new Vector();
      getImplementedInterfaces(implInterfaces, entMap);
      setBody = "\t\tif (";
      for (int i = 0; i < implInterfaces.size(); i++) {
        setBody = setBody + "instance instanceof " + (String) implInterfaces.get(i) + " && ";
      }
      // clear last 4 characters from output:
      if (setBody.endsWith(" && ")) {
        int endIndex = setBody.length() - 4;
        setBody = setBody.substring(0, endIndex);
      }
      setBody = setBody + ") {\n\t\t\tinstance.setMappedAttribute(" + gamVariableName + ", domain, metaDomain, newValue);\n" + "\t\t\treturn instance;\n"
          + "\t\t}\n";
      shouldBeFirst[0] = true;
    }
    doc.addAttributeInfo(atrDisplayName.toLowerCase(), docHeader);
    setContents.add(setHeader);
    setContents.add(setBody);
  }

  /**
   *
   */
  public void generateSetXXAttributeMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    String setHeader = "";
    String setBody = "";

    SdaiIterator atrIt = entityAttributesInIteration.createIterator();
    while (atrIt.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) entityAttributesInIteration.getCurrentMemberObject(atrIt);
      // now, decl hashtable for storing grouped results of setXX generation:
      Hashtable setMethods = new Hashtable();

      // key- set header, value - vector of entries.
      Hashtable bodiesOfSetMethods = new Hashtable();
      // dont' try to optimize display name by calling generateValidName because we must capture and
      // discuss all cases when attribute name is unusual.
      String atrDisplayName = changeFirstLetterToUpperCase(exp_attr.getName(null));

      EEntity armAtrType = SimpleOperationsOnExtended.getAttributeDomain(exp_attr);
      Vector atrMappings = (Vector) attrMappingsInIteration.get(exp_attr.getName(null));
      if (atrMappings == null) {
        throw new Exception("Malformed hashtable for current attribute!" + exp_attr);
      }

      // each attribute mapping theoretically leads us to a separate setXX method.
      int mapCount = atrMappings.size();
      for (int i = 0; i < mapCount; i++) {
        Hashtable allowedTypes = new Hashtable();
        EGeneric_attribute_mapping ega_mapping = (EGeneric_attribute_mapping) atrMappings.get(i);
        // now, read associated entity_mapping:
        EEntity_mapping entMap = ega_mapping.getParent_entity(null);
        // now, read ent definition in aim, to be used inside this method for safe typecasting.
        String aimEntityName = getEntMapTargetName(entMap, false);
        String gamVariableName = makeAttrMappingVariableName(aimEntityName, atrDisplayName, ega_mapping);
        String[] retValues = determineFinalTargetType(armAtrType, ega_mapping, exp_attr, allowedTypes);

        int retCount = retValues.length;
        // what we should do is to push under single setXX method as much as we can different
        // attribute mappings. They should be grouped by return value of setXX method.
        for (int j = 0; j < retCount; j++) {
          // first thing to do is to determine what kind of return value we have.
          String retValueType = "";
          AEntity_definition agEntDef = new AEntity_definition();

          Vector setContents = new Vector();
          boolean shouldBeFirst[] = new boolean[1];
          shouldBeFirst[0] = false;

          if (isArmEntityName(retValues[j], allowedTypes, agEntDef)) {
            generateSetMethodForARM(setContents, agEntDef, retValues[j], aimEntityName, gamVariableName, ega_mapping, atrDisplayName, entMap,
                shouldBeFirst, exp_attr);
          }
          else {
            if (isSimpleReturnType(retValues[j])) {
              generateSetMethodForSimple(setContents, agEntDef, retValues[j], aimEntityName, gamVariableName, ega_mapping, atrDisplayName,
                  entMap, shouldBeFirst, exp_attr);
            }
            else {
              generateSetMethodForAIM(setContents, agEntDef, retValues[j], aimEntityName, gamVariableName, ega_mapping, atrDisplayName, entMap,
                  shouldBeFirst, exp_attr);
            }
          }
          if (setContents.size() < 2) {
            continue; // no output was generated.
          }
          setHeader = (String) setContents.get(0);
          setBody = (String) setContents.get(1);

          // try to find hashtable to place getBody value:
          if (setMethods.containsKey(setHeader)) {
            Hashtable bodies = (Hashtable) setMethods.get(setHeader);
            if (!bodies.containsKey(aimEntityName)) {
              bodies.put(aimEntityName, setBody);
              Vector entries = (Vector) bodiesOfSetMethods.get(setHeader);
              if (shouldBeFirst[0] == true) {
                entries.insertElementAt(setBody, 0);
              }
              else {
                entries.add(setBody);
              }
            }
          }
          else {
            // create new child hashtable:
            Hashtable bodies = new Hashtable();
            bodies.put(aimEntityName, setBody);
            setMethods.put(setHeader, bodies);
            Vector entries = new Vector();
            entries.add(setBody);
            bodiesOfSetMethods.put(setHeader, entries);
          }
        }

      }
      // well, now we are ready to print/generate real get methods:
      // each key becomes a decl of real method, and each values vector becomes
      // body of that method. After values vector is printed, a closing bracket is
      // added.
      Enumeration headers = setMethods.keys();
      String methodString = "";
      while (headers.hasMoreElements()) {
        String printString = (String) headers.nextElement();
        methodString = printString;
        Vector entries = (Vector) bodiesOfSetMethods.get(printString);
        for (int i = 0; i < entries.size(); i++) {
          String entry = (String) entries.get(i);
          methodString = methodString + entry;
        }
        // append body with exception-based warning:
        printString = "\t\tthrow new SdaiException(SdaiException.VA_NVLD, setErrorString1);\n" + "\t}\n\n";
        methodString = methodString + printString;
        classMethods.add(methodString);
      }

    }
  }

  protected void generateUsedInXXAttributeMethods(Vector classMethods, EEntity_definition entDef) throws Exception {
    SdaiIterator atrIt = entityAttributesInIteration.createIterator();
    while (atrIt.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) entityAttributesInIteration.getCurrentMemberObject(atrIt);

      EEntity armAtrType = SimpleOperationsOnExtended.getAttributeDomain(exp_attr);

      String attrDisplayName = changeFirstLetterToUpperCase(exp_attr.getName(null));

      String docHeader = "static AEntity <B>usedin</B>" + attrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException";
      // don't add header right now: it's possible that this method will not be generated.
      //!!doc.addAttributeInfo(attrDisplayName.toLowerCase(), docHeader);
      String methodHeader = "\tpublic static AEntity usedin" + attrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException {\n"
          + "\t\tAEntity retValue = new AEntity();\n" + "\t\tif (instance == null)\n\t\t\treturn retValue;\n"
          + "\t\tAAttribute_mapping users = new AAttribute_mapping();\n";
      boolean hasAtLeastOneEntry = false;
      SdaiIterator entMapIt = entityMappingsInIteration.createIterator();
      int entMappingNo = 0;
      while (entMapIt.next()) {
        EEntity_mapping entMap = (EEntity_mapping) entityMappingsInIteration.getCurrentMemberObject(entMapIt);
        // generate name of variable that is assigned with this entity mapping:
        String targetName = getEntMapTargetName(entMap, false);
        String entMapVariableName = "emTo" + generateValidName(changeFirstLetterToUpperCase(targetName));
        // now, move through attribute mappings for current attribute, select those which are related with
        // current entity_mapping and generate source for adding them to single aggregate.
        Vector atrMappings = (Vector) attrMappingsInIteration.get(exp_attr.getName(null));
        if (atrMappings == null) {
          throw new Exception("Malformed hashtable for current attribute!" + exp_attr);
        }
        String aggrBuildEntries = "\t\tAAttribute_mapping ag" + entMappingNo + " =  new AAttribute_mapping();\n";
        int relatedAttrMappingsCount = 0;
        Hashtable aggrEntries = new Hashtable();
        int mapCount = atrMappings.size();
        for (int i = 0; i < mapCount; i++) {
          Hashtable allowedTypes = new Hashtable();
          EGeneric_attribute_mapping ega_mapping = (EGeneric_attribute_mapping) atrMappings.get(i);
          EEntity_mapping myEntMap = ega_mapping.getParent_entity(null);
          if (myEntMap != entMap) {
            continue;
          }
          String[] retValues = determineFinalTargetType(armAtrType, ega_mapping, exp_attr, allowedTypes);
          // if all retValues leads us to simple type, then skip this attribute mapping.
          boolean canContinue = true;
          for (int j = 0; j < retValues.length; j++) {
            if (isSimpleReturnType(retValues[j])) {
              canContinue = false;
              break;
            }
          }
          if (!canContinue) {
            continue;
          }

          String dataTypeName = getDataTypeName(ega_mapping, true);
          String dataTypeExactName = getDataTypeName(ega_mapping, false);
          String atrMappingVariableName = "am" + generateValidName(changeFirstLetterToUpperCase(targetName)) + "__"
              + ((dataTypeName.length() == 0) ? "" : dataTypeName + "__") + attrDisplayName;
          String thisEntry = "\t\tag" + entMappingNo + ".addUnordered(" + atrMappingVariableName + ");\n";
          if (aggrEntries.containsKey(thisEntry)) {
            continue;
          }
          else {
            aggrEntries.put(thisEntry, thisEntry);
          }
          relatedAttrMappingsCount++;
          hasAtLeastOneEntry = true;
          aggrBuildEntries = aggrBuildEntries + thisEntry;
        }
        if (relatedAttrMappingsCount == 0) {
          entMappingNo++;
          continue;
        }
        aggrBuildEntries += "\t\tAEntity retVal" + entMappingNo + " = instance.findMappedUsers(" + entMapVariableName + ", ag" + entMappingNo
            + ", domain, metaDomain," + " users, currentMappingMode);\n" + "\t\tcopyEntities(retValue, retVal" + entMappingNo + ");\n\n";
        methodHeader += aggrBuildEntries;
        entMappingNo++;
      }
      methodHeader += "\t\treturn retValue;\n";
      methodHeader += "\t}\n";
      if (hasAtLeastOneEntry) {
        doc.addAttributeInfo(attrDisplayName.toLowerCase(), docHeader);
        classMethods.add(methodHeader);
        classMethods.add("\n");
      }
    }
  }

  protected void generateUsersMethod(Vector classMethods, EEntity_definition entDef) throws Exception {
    String docHeader = "static AEntity findUsers(ASdaiModel domain, EEntity instance) throws SdaiException";
    String description = "Method allows to find all AIM instances that can be mapped to this ARM"
        + " entity and reference given AIM instance through any of their atttibutes.";
    doc.addCommonMethods(docHeader, description);
    String methodHeader = "\tpublic static AEntity findUsers(ASdaiModel domain, EEntity instance)" + " throws SdaiException {\n"
        + "\t\tAEntity retValue = new AEntity();\n" + "\t\tif (instance == null)\n\t\t\treturn retValue;\n"
        + "\t\tAAttribute_mapping users = new AAttribute_mapping();\n";
    SdaiIterator entMapIt = entityMappingsInIteration.createIterator();
    int entMappingNo = 0;
    while (entMapIt.next()) {
      EEntity_mapping entMap = (EEntity_mapping) entityMappingsInIteration.getCurrentMemberObject(entMapIt);
      String targetName = getEntMapTargetName(entMap, false);
      String aggrBuildEntries = "";
      String createAggregateString = "\t\tAAttribute_mapping ag" + entMappingNo + " =  new AAttribute_mapping();\n";
      // find all attribute mappings, that are related with this entity_mapping, and push them to aggregate.
      SdaiIterator atrIt = entityAttributesInIteration.createIterator();
      while (atrIt.next()) {
        EExplicit_attribute exp_attr = (EExplicit_attribute) entityAttributesInIteration.getCurrentMemberObject(atrIt);
        String attrDisplayName = changeFirstLetterToUpperCase(exp_attr.getName(null));

        Vector atrMappings = (Vector) attrMappingsInIteration.get(exp_attr.getName(null));
        if (atrMappings == null) {
          throw new Exception("Malformed hashtable for current attribute!" + exp_attr);
        }
        int relatedAttrMappingsCount = 0;
        Hashtable aggrEntries = new Hashtable();
        int mapCount = atrMappings.size();
        for (int i = 0; i < mapCount; i++) {
          EGeneric_attribute_mapping ega_mapping = (EGeneric_attribute_mapping) atrMappings.get(i);
          EEntity_mapping myEntMap = ega_mapping.getParent_entity(null);
          if (myEntMap != entMap) {
            continue;
          }
          String dataTypeName = getDataTypeName(ega_mapping, true);
          String dataTypeExactName = getDataTypeName(ega_mapping, false);
          String atrMappingVariableName = "am" + generateValidName(changeFirstLetterToUpperCase(targetName)) + "__"
              + ((dataTypeName.length() == 0) ? "" : dataTypeName + "__") + attrDisplayName;
          String thisEntry = "\t\tag" + entMappingNo + ".addUnordered(" + atrMappingVariableName + ");\n";
          if (aggrEntries.containsKey(thisEntry)) {
            continue;
          }
          else {
            aggrEntries.put(thisEntry, thisEntry);
          }
          relatedAttrMappingsCount++;
          aggrBuildEntries = aggrBuildEntries + thisEntry;
        }
      }

      String entMapVariableName = "emTo" + generateValidName(changeFirstLetterToUpperCase(targetName));
      String methodEntry = "\t\tAEntity retVal" + entMappingNo + " = instance.findMappedUsers(" + entMapVariableName + ", ag" + entMappingNo
          + ", domain, metaDomain," + " users, currentMappingMode);\n" + "\t\tcopyEntities(retValue, retVal" + entMappingNo + ");\n\n";
      methodHeader = methodHeader + createAggregateString + aggrBuildEntries + methodEntry;
      entMappingNo++;
    }
    methodHeader += "\t\treturn retValue;\n";
    methodHeader += "\t}\n";
    classMethods.add(methodHeader);
    classMethods.add("\n");
  }

  /*
   * ******************************************************************
   * isSelectInside methods
   * ******************************************************************
   */

  public static boolean isSelectInside(EEntity type) throws SdaiException {
    if (type instanceof EAttribute) {
      return isSelectInside((EAttribute) type);
    }
    if (type instanceof EExplicit_attribute) {
      return isSelectInside((EExplicit_attribute) type);
    }
    if (type instanceof ESimple_type) {
      return false;
    }
    if (type instanceof ESelect_type) {
      return true;
    }
    if (type instanceof EEnumeration_type) {
      return false;
    }
    if (type instanceof EEntity_definition) {
      return false;
    }
    if (type instanceof ENamed_type) {
      return isSelectInside((ENamed_type) type);
    }
    if (type instanceof EAggregation_type) {
      return isSelectInside((EAggregation_type) type);
    }

    System.out.println("PROBLEM: unknown type passed to isSelectInside(EEntity)");
    System.out.println("type is " + type);
    System.out.println("its class is " + type.getClass());
    return false;
  }

  private static boolean isSelectInside(EAttribute type) throws SdaiException {
    boolean rv = false;
    EEntity domain = null;
    if (type instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) type;
      if (eAttr.testDomain(null)) {
        domain = eAttr.getDomain(null);
        rv = isSelectInside(domain);
      }
      else {
        System.out.println("PROBLEM: 1st case:domain not set. isSelectInside(EAttribute)");
      }
    }
    else if (type instanceof EDerived_attribute) {
      EDerived_attribute dAttr = (EDerived_attribute) type;
      if (dAttr.testDomain(null)) {
        domain = dAttr.getDomain(null);
        rv = isSelectInside(domain);
      }
      else {
        System.out.println("PROBLEM: 2nd case:domain not set. isSelectInside(EAttribute)");
      }
    }
    else if (type instanceof EInverse_attribute) {
      EInverse_attribute iAttr = (EInverse_attribute) type;
      if (iAttr.testInverted_attr(null)) {
        EExplicit_attribute expAttr = iAttr.getInverted_attr(null);
        rv = isSelectInside(expAttr);
      }
      else {
        System.out.println("PROBLEM: 3rd case: inverse not set. isSelectInside(EAttribute)");
      }
    }
    else {
      System.out.println("PROBLEM: unknown case. isSelectInside(EAttribute)");
    }

    return rv;
  }

  private static boolean isSelectInside(EExplicit_attribute attr) throws SdaiException {
    boolean a = false;
    EEntity domain = attr.getDomain(null);
    if (domain instanceof ESimple_type) {
      a = false;
    }
    else {
      a = isSelectInside(domain);
    }
    return a;
  }

  private static boolean isSelectInside(EAggregation_type at) throws SdaiException {
    boolean rv = false;
    if (at.testElement_type(null)) {
      EEntity type = at.getElement_type(null);
      rv = isSelectInside(type);
    }
    else {
      System.out.println("PROBLEM: element_type not set. isSelectInside(EAggregation_type)");
    }
    return rv;
  }

  private static boolean isSelectInside(ENamed_type nt) throws SdaiException {
    boolean rv = false;
    if (nt instanceof EDefined_type) {
      EDefined_type dt = (EDefined_type) nt;
      if (dt.testDomain(null)) {
        EEntity type = dt.getDomain(null);
        rv = isSelectInside(type);
      }
      else {
        System.out.println("PROBLEM:  1st case: domain not set. isSelectInside(ENamed_type)");
      }
    }
    else if (nt instanceof EEntity_definition) {
      rv = false;
    }
    else {
      System.out.println("PROBLEM: unknown type. isSelectInside(ENamed_type)");
    }

    return rv;
  }

}
