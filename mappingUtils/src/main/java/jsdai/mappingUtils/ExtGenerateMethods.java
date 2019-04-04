package jsdai.mappingUtils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.AEntity_definition;
import jsdai.SExtended_dictionary_schema.AExplicit_attribute;
import jsdai.SExtended_dictionary_schema.ANamed_type;
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
import jsdai.SMapping_schema.AEntity_mapping_relationship;
import jsdai.SMapping_schema.AGeneric_attribute_mapping;
import jsdai.SMapping_schema.CEntity_mapping;
import jsdai.SMapping_schema.CEntity_mapping_relationship;
import jsdai.SMapping_schema.CGeneric_attribute_mapping;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EDerived_variant_entity_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EGeneric_attribute_mapping;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

class ExtGenerateMethods extends ExtUtilMethods {

  protected ExtGenerateMethods(String apPartNumber, String genTargetDir, String mainSchema, boolean genMainFunction, String docSourcePath)
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
    // find the supertypes:
    if (entDef.testGeneric_supertypes(null)) {
      AEntity superTypes = entDef.getGeneric_supertypes(null);
      for (int i = 1; i <= superTypes.getMemberCount(); i++) {
        EEntity_definition supertype = (EEntity_definition) superTypes.getByIndexObject(i);
        doc.addSupertype(supertype.getName(null));
      }
    }

  }

  protected void generateFindCompatibleMethod(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    String docHeader = "static AEntity findCompatible(ASdaiModel domain, AEntity inAggregate) throws SdaiException";
    String description = "Method can be used to build an aggregate of AIM instances, which can be mapped to"
        + " appropriate ARM concept. Only those AIM instances have to be analyzed," + " which are members of given aggregate inAggregate.";
    doc.addCommonMethods(docHeader, description);
    String printString = "\tpublic static AEntity findCompatible(ASdaiModel domain, AEntity inAggregate) throws SdaiException {\n";
    printString += "\t\tAEntity result = new AEntity();\n";
    printString += "\t\tSdaiIterator it = inAggregate.createIterator();\n";
    printString += "\t\twhile (it.next()) {\n";
    printString += "\t\tEEntity test = inAggregate.getCurrentMemberEntity(it);\n";
    printString += "\t\tif (test.testMappedEntity(armEntity, domain, metaDomain, currentMappingMode) != null) \n";
    printString += "\t\t\tresult.addUnordered(test);\n";
    printString += "\t\t}\n";
    printString += "\t\treturn result;\n";
    printString += "\t}\n";

    classMethods.add(printString);
    classMethods.add("\n");
    /*
     * public static AEntity findCompatible(ASdaiModel domain, AEntity inAggregate) throws SdaiException {
     * AEntity result = new AEntity();
     * SdaiIterator it = inAggregate.createIterator();
     * while (it.next()) {
     * EEntity test = inAggregate.getCurrentMemberEntity(it);
     * if (test.testMappedEntity(armEntity, domain, metaDomain, currentMappingMode) != null)
     * result.addUnordered(test);
     * }
     * return result;
     * }
     */
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

    // another variation of "is" method: we need a possibility to test whether instance is 'exactly'
    // that particular arm entity.
    docHeader = "static boolean isExact(ASdaiModel domain, EEntity instance) throws SdaiException ";
    description = "Method tests whether given AIM instance can be mapped to given ARM concept, and to " + "no other ARM concept.";
    doc.addCommonMethods(docHeader, description);
    printString = "\tpublic static boolean isExact(ASdaiModel domain, EEntity instance) \n\tthrows SdaiException " + "\n\t{\n"
        + "\t\tif (instance == null)\n" + "\t\t\treturn false;\n"
        + "\t\tAEntity_mapping availMappings = instance.testMappedEntity(armEntity, domain, metaDomain, currentMappingMode);\n"
        + "\t\tif (availMappings == null)\n" + "\t\t\treturn false;\n"
        + "\t\tAEntity_mapping results = instance.findMostSpecificMappings(domain, metaDomain, availMappings, currentMappingMode);\n"
        + "\t\tif (results == null)\n" + "\t\t\treturn false;\n" + "\t\tif (results.getMemberCount() == 0)\n" + "\t\t\treturn false;\n"
        + "\t\tSdaiIterator it = results.createIterator();\n" + "\t\twhile (it.next()) {\n"
        + "\t\t\tEEntity_mapping test = results.getCurrentMember(it);\n" + "\t\t\tif (test.getSource(null) != armEntity)\n" + "\t\t\t\treturn false;\n"
        + "\t\t}\n" + "\t\treturn true;\n" + "\t}\n";
    classMethods.add(printString);
    classMethods.add("\n");

    /*
     * //AEntity_mapping availMappings = agMappings;
     * AEntity_mapping availMappings = instance.testMappedEntity(armEntity, domain, metaDomain, currentMappingMode);
     * AEntity_mapping results = instance.findMostSpecificMappings(domain, metaDomain, availMappings, currentMappingMode);
     * if (results == null)
     * return false;
     * if (results.getMemberCount() == 0)
     * return false;// given instance does not map to arm entity or any of its subtypes at all.
     * SdaiIterator it = results.createIterator();
     * while (it.next()) {
     * EEntity_mapping test = results.getCurrentMember(it);
     * // if there is at least one entity_mapping, where given instance maps
     * // to something else than armEntity, then we clearly are dealing with
     * // an instance, which maps to subtype of armEntity. Thus, verdict is clear.
     * if (test.getSource(null) != armEntity)
     * return false;
     * }
     * return true;
     */
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
      String aimEntTypeName = "E" + makeValidName(capitalize(superType.getName(null)));

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

  public void generateFindAggInstanceMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    // let's generate a single findInstance with last parameter of Class type. If last parameter equals to null,
    // then all possible instances should be retrieved.
    String documentationHeader = "static public AEntity findInstances(ASdaiModel domain, AEntity agg, Class entityType) throws" + " SdaiException";
    String description = "This method finds all AIM instances that can be mapped to this ARM entity."
        + " It's guaranteed that result set will not contain repeated entries."
        + " Parameter <I>domain</I> identifies which SdaiModels should be searched for AIM instances,"
        + " parameter <I>agg</I> specified, where the lookup of instances has be carried on,"
        + " parameter <I>entityType</I> allows to limit result set only to instances, that"
        + " are 'instanceof' of given class. This parameter can be null - in this case no"
        + " restrictions will be applied. You can specify here a class for simple entity type"
        + " (starting with letter E), or you can specify here a class for mixed complex entity" + " type (starting with letter C).";
    doc.addCommonMethods(documentationHeader, description);
    String printString = "\tstatic public AEntity findInstances(ASdaiModel domain, AEntity agg, Class entityType) throws SdaiException {\n"
        + "\t\tAEntity retValue = new AEntity();\n\t\tboolean canCopy = true;\n";
    printString += "\t\tSdaiIterator it = eMappings.createIterator();\n" + "\t\twhile (it.next()) {\n" + "\t\t\tcanCopy = true;\n"
        + "\t\t\tEEntity_mapping mapping = (EEntity_mapping) eMappings.getCurrentMemberObject(it);\n" + "\t\t\tif (entityType != null) {\n"
        + "\t\t\t\tEEntity target = mapping.getTarget(null);\n" + "\t\t\t\tif (!isCompatible(entityType, target))\n" + "\t\t\t\t\tcanCopy = false;\n"
        + "\t\t\t}\n" + "\t\t\tif(canCopy) {\n" + "\t\t\t\tAEntity retVal = findMappingInstances(mapping, domain, agg, currentMappingMode);\n"
        + "\t\t\t\tcopyEntities(retValue, retVal);\n" + "\t\t\t}\n" + "\t\t}\n" + "\t\treturn retValue;\n\t}\n";
    classMethods.add(printString);
    classMethods.add("\n");
  }

  /**
   *
   */
  public void generateFindInstanceMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    generateFindAggInstanceMethods(classMethods, entDef);

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
    printString += "\t\tSdaiIterator it = eMappings.createIterator();\n" + "\t\twhile (it.next()) {\n" + "\t\t\tcanCopy = true;\n"
        + "\t\t\tEEntity_mapping mapping = (EEntity_mapping) eMappings.getCurrentMemberObject(it);\n" + "\t\t\tif (entityType != null) {\n"
        + "\t\t\t\tEEntity target = mapping.getTarget(null);\n" + "\t\t\t\tif (!isCompatible(entityType, target))\n" + "\t\t\t\t\tcanCopy = false;\n"
        + "\t\t\t}\n" + "\t\t\tif(canCopy) {\n" + "\t\t\t\tAEntity retVal = findMappingInstances(mapping, domain, currentMappingMode);\n"
        + "\t\t\t\tcopyEntities(retValue, retVal);\n" + "\t\t\t}\n" + "\t\t}\n" + "\t\treturn retValue;\n\t}\n";
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
      String atrDisplayName = capitalize(exp_attr.getName(null));
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
      EEntity domain = exp_attr.getDomain(null);
      boolean aggAllowed = isAggregateInside(domain);
      // dont' try to optimize display name by calling makeValidName because we must capture all and
      // discuss all cases when attribute name is unusual.
      String atrDisplayName = capitalize(exp_attr.getName(null));

      documentationHeader = "static int <B>test</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException";
      doc.addAttributeInfo(exp_attr.getName(null), documentationHeader);

      String fullMethod = "\tpublic static int test" + atrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException {\n"
          + "\t\tObject o = null;\n" + "\t\to = getAnyValueForAttribute(domain, metaDomain, at" + atrDisplayName + ", instance);\n"
          + "\t\tif (o == null)\n\t\t\treturn MappingUtil.VA_NSET;\n";
      Hashtable aimTypeToOverload = (Hashtable) attrMethodsInIteration.get(exp_attr.getName(null));
      Enumeration items = aimTypeToOverload.keys();
      while (items.hasMoreElements()) {
        String aimType = (String) items.nextElement();
        Hashtable overloads = (Hashtable) aimTypeToOverload.get(aimType);
        Enumeration exactNames = overloads.keys();
        while (exactNames.hasMoreElements()) {
          String exactName = (String) exactNames.nextElement();
          String staticConstant = (String) overloads.get(exactName);
          String testEntry = "";
          testEntry = "\t\tif (compatibleObjectValue(o, " + aimType + ".class, " + String.valueOf(aggAllowed) + "))\n" + "\t\t\treturn "
              + staticConstant + ";\n";
          fullMethod = fullMethod + testEntry;
        }
      }
      // testMethod should never throw exception: just a silent output that
      // failed to recognize the value:
      fullMethod = fullMethod + "\t\tSystem.out.println(\"WARNING! Failed to recognize " + "exact type of value of attribute "
          + atrDisplayName.toLowerCase() + " on entity " + currentlyGeneratedClassName + ". Tested instance:\""
          + " + instance + \". Tested value: \"+o);\n" + "\t\treturn MappingUtil.VA_NSET;\n" + "\t}\n";
      classMethods.add(fullMethod);

      classMethods.add("\n");
    }
  }

  /**
   * NOTE: this method should generate all attributes, but currently it's limited to explicit ones.
   */
  public void generateAttributesBlock(Vector attributes, EEntity_definition entDef) throws SdaiException, Exception {
    //	output for expl attribute should look like:
    //	private static Aggregate aAngle_measure__Mappings = findAttrMappings(eMappings, atAngle_measure);
    //

    AExplicit_attribute attr = entDef.getExplicit_attributes(null);
    // remember aggregate for using in other methods:
    entityAttributesInIteration = attr;
    SdaiIterator it = attr.createIterator();
    while (it.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) attr.getCurrentMemberObject(it);
      Vector atrMapsVector = new Vector();
      String attrName = exp_attr.getName(null);
      String printStr = "\tstatic EExplicit_attribute at" + makeValidName(capitalize(attrName))
          + " = (EExplicit_attribute) findArmAttribute(armEntity, \"" + attrName + "\");\n";
      attributes.add(printStr);
      String mapText = "\tprivate static Aggregate a" + makeValidName(capitalize(attrName)) + "__Mappings = findAttrMappings(eMappings, " + "at"
          + makeValidName(capitalize(attrName)) + ");\n";
      attributes.add(mapText);
      mapText = "\tprivate static AAttribute_mapping am" + makeValidName(capitalize(attrName)) + "__Mappings = findAttrMappingsAsRef(eMappings, " + "at"
          + makeValidName(capitalize(attrName)) + ");\n";
      attributes.add(mapText);

      // good. Now get aggregate of existing attribute mappings for this attribute:
      AGeneric_attribute_mapping agAtrMap = new AGeneric_attribute_mapping();
      CGeneric_attribute_mapping.usedinSource(null, exp_attr, mappingDomain, agAtrMap);

      if (agAtrMap.getMemberCount() == 0) {
        //System.out.println("WARNING! No attribute_mappings found for explicit "+
        //	"attribute "+exp_attr+"! Attribute belongs to ARM entity "+entDef);
      }
      SdaiIterator atrMapIt = agAtrMap.createIterator();
      while (atrMapIt.next()) {
        EGeneric_attribute_mapping atrMapping = (EGeneric_attribute_mapping) agAtrMap.getCurrentMemberObject(atrMapIt);
        EEntity_mapping entMapping = atrMapping.getParent_entity(null);
        // now test, whether this mapping is in allowed mappings list, as
        // the same attribute can be mapped differently in supertype and subtype.
        if (!acceptableMapping(entMapping)) {
          continue;
        }

        atrMapsVector.add(atrMapping);
      }
      // good, now save info about mappings:
      attrMappingsInIteration.put(attrName, atrMapsVector);
      attributes.add("\n");
    }
  }

  /**
   * There are two goals: generate output string and build vector of
   * available entity mappings for using in other methods.
   */
  public void generateMappingsBlock(Vector entityMappings, EEntity_definition entDef) throws SdaiException, Exception {
    // first get all known entity mappings for given entity definition:
    AEntity_mapping agMappings = new AEntity_mapping();
    CEntity_mapping.usedinSource(null, entDef, mappingDomain, agMappings);

    if (agMappings.getMemberCount() == 0) {
      System.out.println("WARNING! no entity mappings found for ARM " + "entity definition " + entDef + "!");
    }
    SdaiIterator it = agMappings.createIterator();
    while (it.next()) {
      EEntity_mapping mapping = agMappings.getCurrentMember(it);
      if (isComplexEntityType(mapping)) {
//				System.out.println("mapping is complex"+mapping);
      }
    }

    String mappingEntry = "\tprivate static Aggregate eMappings = findEntityMappings(armEntity);\n";
    entityMappings.add(mappingEntry);
    entityMappingsInIteration = agMappings;
  }

  protected void generateGetMethodForARM(Vector getContents, AEntity_definition agEntDef, String armEntityName, String aimEntityName,
      EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter,
      EExplicit_attribute exp_attr, EEntity_mapping relatedEm) throws SdaiException, Exception {
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
//!!			retValueType = "E"+capitalize(retValueType);
      retValueType = bodyPrefix + capitalize(retValueType);
    }
    if (retValueType.equalsIgnoreCase("EEntity")) {
      bodyPrefix = "";
      overlPrefix = "";
      if (isAggregate) {
        retValueType = "AEntity";
      }
    }
    retValueType = makeValidName(retValueType);
    String docHeader = "static " + retValueType + " <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance, M" + capitalize(armEntityName)
        + " a) throws SdaiException";
    getHeader = "\tpublic static " + retValueType + " get" + atrDisplayName + "(ASdaiModel domain, EEntity instance, M" + capitalize(armEntityName)
        + " a) throws SdaiException {\n";

    getBody = "\t\tObject o = null;\n" + "\t\to = getAnyValueForAttribute(domain, metaDomain," + " at" + atrDisplayName + ", instance);\n";
    getBody += "\t\tif (o == null)\n" + "\t\t\tthrow new SdaiException(SdaiException.VA_NSET, instance, " + "\"Attribute " + atrDisplayName.toLowerCase()
        + " for entity " + currentlyGeneratedClassName + " is not set.\");\n";

    if (!isAggregate) {
      getBody = getBody + "\t\tif (o instanceof " + retValueType + ") {\n" + "\t\t\treturn (" + retValueType + ") o;\n" + "\t\t}\n" + "\t\telse\n"
          + "\t\t\tthrow new SdaiException(SdaiException.VA_NVLD, instance," + "getErrorString1+\" Invalid value is:\"+o);\n";

    }
    else {
      getBody = getBody + "\t\treturn (" + retValueType + ") buildAggregate(o, " + retValueType + ".class);\n";
    }
    // ok. Now, we need to check if this method should be generated

    // first, do a check on data_type. Apply this check only when attribute type is
    // select type:
    if (isSelectInside(exp_attr)) {
      // use data_type field to check whether this method is desired and generated correctly.
      String typeNameInMapping = null;
      if (relatedEm != null) {
        typeNameInMapping = getEntMapTargetName(relatedEm, false);
      }
      else {
        typeNameInMapping = getDataTypeName(egaMapping, false);
      }
      if (!typeNameInMapping.equalsIgnoreCase(armEntityName)) {
        return; // we ommit overhead generation.
      }
    }
    else {
      // second, if we have not a select type, apply this check
      EEntity atrDomain = relatedEm;
      if ((egaMapping instanceof EAttribute_mapping) && (atrDomain == null)) {
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

    String staticConst = atrDisplayName.toUpperCase() + "__M" + armEntityName.toUpperCase();
    doc.addStaticConstant(atrDisplayName.toLowerCase(), staticConst);

    String searchKey = overlPrefix + capitalize(leastCommDenom);
    if (aimToOverloadParameter.containsKey(searchKey)) {
      Hashtable overloads = (Hashtable) aimToOverloadParameter.get(searchKey);
      String dataTypeExactName = null;
      if (relatedEm != null) {
        dataTypeExactName = getEntMapTargetName(relatedEm, false);
      }
      else {
        dataTypeExactName = getDataTypeName(egaMapping, false);
      }
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
      String dataTypeExactName = null;
      if (relatedEm != null) {
        dataTypeExactName = getEntMapTargetName(relatedEm, false);
      }
      else {
        dataTypeExactName = getDataTypeName(egaMapping, false);
      }
      overloads.put(dataTypeExactName, staticConst);
      aimToOverloadParameter.put(searchKey, overloads);
    }

    doc.addAttributeInfo(atrDisplayName.toLowerCase(), docHeader);

    getContents.add(getHeader);
    getContents.add(getBody);
    getContents.add(staticConst);
  }

  protected void generateGetMethodForAIM(Vector getContents, AEntity_definition agEntDef, String retTypeName, String aimEntityName,
      EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter,
      EExplicit_attribute exp_attr, EEntity_mapping relatedEm) throws SdaiException, Exception {
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
      retValueType = bodyPrefix + capitalize(retValueType);
    }
    if (retTypeName.equalsIgnoreCase("EEntity")) {
      bodyPrefix = "";
      overlPrefix = "";
      if (isAggregate) {
        retValueType = "AEntity";
      }
    }

    retValueType = makeValidName(retValueType);
    retTypeName = makeValidName(retTypeName);
    String docHeader = "static " + retValueType + " <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance, " + overlPrefix
        + capitalize(retTypeName) + " a) throws SdaiException";
    getHeader = "\tpublic static " + retValueType + " get" + atrDisplayName + "(ASdaiModel domain, EEntity instance, " + overlPrefix
        + capitalize(retTypeName) + " a) throws SdaiException {\n";

    getBody = "\t\tObject o = null;\n" + "\t\to = getAnyValueForAttribute(domain, metaDomain," + " at" + atrDisplayName + ", instance);\n";
    getBody += "\t\tif (o == null)\n" + "\t\t\tthrow new SdaiException(SdaiException.VA_NSET, instance, " + "\"Attribute " + atrDisplayName.toLowerCase()
        + " for entity " + currentlyGeneratedClassName + " is not set.\");\n";

    if (!isAggregate) {
      getBody += "\t\tif (o instanceof " + retValueType + ") {\n" + "\t\t\treturn (" + retValueType + ") o;\n" + "\t\t}\n" + "\t\telse\n"
          + "\t\t\tthrow new SdaiException(SdaiException.VA_NVLD, instance, " + "getErrorString1+\" Invalid value is:\"+o);\n";
    }
    else {
      getBody = getBody + "\t\treturn (" + retValueType + ") buildAggregate(o, " + retValueType + ".class);\n";
    }
    String staticConst = atrDisplayName.toUpperCase() + "__" + retTypeName.toUpperCase();
    doc.addStaticConstant(atrDisplayName.toLowerCase(), staticConst);

    String searchKey = overlPrefix + capitalize(retTypeName);
    if (aimToOverloadParameter.containsKey(searchKey)) {
      Hashtable overloads = (Hashtable) aimToOverloadParameter.get(searchKey);
      String dataTypeExactName = null;
      if (relatedEm != null) {
        dataTypeExactName = getEntMapTargetName(relatedEm, false);
      }
      else {
        dataTypeExactName = getDataTypeName(egaMapping, false);
      }
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
      String dataTypeExactName = null;
      if (relatedEm != null) {
        dataTypeExactName = getEntMapTargetName(relatedEm, false);
      }
      else {
        dataTypeExactName = getDataTypeName(egaMapping, false);
      }
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
      EGeneric_attribute_mapping egaMapping, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter,
      EExplicit_attribute exp_attr, EEntity_mapping relatedEm) throws SdaiException, Exception {
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

    getBody = "\t\tObject o = null;\n" + "\t\to = getAnyValueForAttribute(domain, metaDomain," + " at" + atrDisplayName + ", instance);\n";
    getBody += "\t\tif (o == null)\n" + "\t\t\tthrow new SdaiException(SdaiException.VA_NSET, instance, " + "\"Attribute " + atrDisplayName.toLowerCase()
        + " for entity " + currentlyGeneratedClassName + " is not set.\");\n";

    if (!isAggregate) {
      getBody += "\t\treturn " + returnTypeCastLine(retValueType) + ";\n";
    }
    else {
      getBody = getBody + "\t\t\treturn buildArrayList(o)" + ";\n";
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
      String dataTypeExactName = null;
      if (relatedEm != null) {
        dataTypeExactName = getEntMapTargetName(relatedEm, false);
      }
      else {
        dataTypeExactName = getDataTypeName(egaMapping, false);
      }

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
      String dataTypeExactName = null;
      if (relatedEm != null) {
        dataTypeExactName = getEntMapTargetName(relatedEm, false);
      }
      else {
        dataTypeExactName = getDataTypeName(egaMapping, false);
      }
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
    else if (retType.equalsIgnoreCase("boolean")) {
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

  public boolean processDueToDvem(EGeneric_attribute_mapping ega_mapping, EEntity armAtrType, EExplicit_attribute exp_attr, Hashtable allowedTypes,
      String aimEntityName, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter, Hashtable getMethods) throws SdaiException,
      Exception {
    String getHeader = "";
    String getBody = "";
    String staticConst = "";
    boolean retValue = false;
    Vector suitableUsers = new Vector();
    if (ega_mapping instanceof EAttribute_mapping) {
      EAttribute_mapping test_am = (EAttribute_mapping) ega_mapping;
      // see if domain is present, if not- obviously we should go with old functionality.
      if (test_am.testDomain(null)) {
        EEntity testDomain = test_am.getDomain(null);
        //	System.out.println("test domain is present."+testDomain);
        // we require the domain to be of e_m type, otherwise the dvem functionality
        // is obviously not needed.
        if (testDomain instanceof EEntity_mapping) {
          EEntity_mapping mainMapping = (EEntity_mapping) testDomain;
          // let's check if there are any users of dvem type:
          AEntity_mapping_relationship result = new AEntity_mapping_relationship();
          CEntity_mapping_relationship.usedinRelating(null, mainMapping, null, result);
          //System.out.println("number of members in result:"+result.getMemberCount());
          SdaiIterator testIt = result.createIterator();

          while (testIt.next()) {
            EEntity testUser = result.getCurrentMemberEntity(testIt);
            if (testUser instanceof EDerived_variant_entity_mapping) {
              suitableUsers.add(testUser);
            }
          }
        }
      }
    }
    if (suitableUsers.size() > 0) {
      //	System.out.println("we have discovered suitable users:"+suitableUsers.size());
      // now, for every suitable user, we have to generate an entry in future getxx
      // method tables.
      for (int i = 0, count = suitableUsers.size(); i < count; i++) {
        EDerived_variant_entity_mapping dvem = (EDerived_variant_entity_mapping) suitableUsers.get(i);
        // now, story, similar to processInDefaultWay:
        String[] retValues = determineFinalTgTypeForDvem(dvem, allowedTypes);
        int retCount = retValues.length;
        // what we should do is to push under single getXX method as much as we can different
        // attribute mappings. They should be grouped by return value of getXX method.
        for (int j = 0; j < retCount; j++) {
          //	System.out.println("retvalues[j] is "+j+" "+retValues[j]);
          // first thing to do is to determine what kind of return value we have.
          String retValueType = "";
          AEntity_definition agEntDef = new AEntity_definition();

          Vector getContents = new Vector();
          if (isArmEntityName(retValues[j], allowedTypes, agEntDef)) {
            //System.out.println("recogn as arm entity name");
            generateGetMethodForARM(getContents, agEntDef, retValues[j], aimEntityName, ega_mapping, atrDisplayName, entMap,
                aimToOverloadParameter, exp_attr, dvem.getRelated(null));
          }
          else {
            if (isSimpleReturnType(retValues[j])) {
              //	System.out.println("recogn as simple return type.");
              generateGetMethodForSimple(getContents, agEntDef, retValues[j], aimEntityName, ega_mapping, atrDisplayName, entMap,
                  aimToOverloadParameter, exp_attr, dvem.getRelated(null));
            }
            else {
              //System.out.println("recogn as aim type");
              generateGetMethodForAIM(getContents, agEntDef, retValues[j], aimEntityName, ega_mapping, atrDisplayName, entMap,
                  aimToOverloadParameter, exp_attr, dvem.getRelated(null));
            }
          }
          if (getContents.size() < 3) {
            continue; // no output was generated.
          }
          getHeader = (String) getContents.get(0);
          //System.out.println("aimEntityName is "+aimEntityName);
          //System.out.println("getHeader is "+getHeader);
          getBody = (String) getContents.get(1);
          //System.out.println("getBody is "+getBody);
          staticConst = (String) getContents.get(2);
          //System.out.println("static const is "+staticConst);
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
      retValue = true;
    }

    return retValue;
  }

  protected void processInDefaultWay(EGeneric_attribute_mapping ega_mapping, EEntity armAtrType, EExplicit_attribute exp_attr, Hashtable allowedTypes,
      String aimEntityName, String atrDisplayName, EEntity_mapping entMap, Hashtable aimToOverloadParameter, Hashtable getMethods) throws SdaiException,
      Exception {
    String getHeader = "";
    String getBody = "";
    String staticConst = "";
    //String gamVariableName = makeAttrMappingVariableName(aimEntityName, atrDisplayName, ega_mapping);
    //String gamVariableName = createVarName(ega_mapping, entMap, atrDisplayName, true);
    String[] retValues = determineFinalTargetType(armAtrType, ega_mapping, exp_attr, allowedTypes);

    int retCount = retValues.length;
    // what we should do is to push under single getXX method as much as we can different
    // attribute mappings. They should be grouped by return value of getXX method.
    for (int j = 0; j < retCount; j++) {
      //System.out.println("retvalues[j] is "+j+" "+retValues[j]);
      // first thing to do is to determine what kind of return value we have.
      String retValueType = "";
      AEntity_definition agEntDef = new AEntity_definition();

      Vector getContents = new Vector();

      if (isArmEntityName(retValues[j], allowedTypes, agEntDef)) {
        //	System.out.println("recogn as arm entity name");
        generateGetMethodForARM(getContents, agEntDef, retValues[j], aimEntityName, ega_mapping, atrDisplayName, entMap, aimToOverloadParameter,
            exp_attr, null);
      }
      else {
        if (isSimpleReturnType(retValues[j])) {
          //	System.out.println("recogn as simple return type.");
          generateGetMethodForSimple(getContents, agEntDef, retValues[j], aimEntityName, ega_mapping, atrDisplayName, entMap, aimToOverloadParameter,
              exp_attr, null);
        }
        else {
          //System.out.println("recogn as aim type");
          generateGetMethodForAIM(getContents, agEntDef, retValues[j], aimEntityName, ega_mapping, atrDisplayName, entMap, aimToOverloadParameter,
              exp_attr, null);
        }
      }
      if (getContents.size() < 3) {
        continue; // no output was generated.
      }
      getHeader = (String) getContents.get(0);
      //System.out.println("aimEntityName is "+aimEntityName);
      //System.out.println("getHeader is "+getHeader);
      getBody = (String) getContents.get(1);
      //System.out.println("getBody is "+getBody);
      staticConst = (String) getContents.get(2);
      //System.out.println("static const is "+staticConst);
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

  /**
   *
   */
  public void generateGetXXAttributeMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    //	System.out.println("generating getXXX methods.");
    String getHeader = "";
    String getBody = "";
    String staticConst = "";
    attrMethodsInIteration.clear();

    SdaiIterator atrIt = entityAttributesInIteration.createIterator();
    while (atrIt.next()) {
      EExplicit_attribute exp_attr = entityAttributesInIteration.getCurrentMember(atrIt);
      // now, decl hashtable for storing grouped results of getXX generation:
      Hashtable getMethods = new Hashtable();
      // now, decl hashtable for storing related aim entity type names and overload parameters:
      Hashtable aimToOverloadParameter = new Hashtable();

      // dont' try to optimize display name by calling makeValidName because we must capture and
      // discuss all cases when attribute name is unusual.
      String atrDisplayName = capitalize(exp_attr.getName(null));
      //		System.out.println("visited attribute is "+atrDisplayName);
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
      // Moreover, we should test, if domain of attribute_mapping is of entity_mapping type.
      // If yes, then we have to test, if there are users this e_m, and these interesting
      // users have to be of derived_variant_entity_mapping type (usage through attribute relating).
      // If these many conditions are met, then the attribute_mapping itself should not be
      // analyzed, and as many as users of dvem were found. I.e., for each user a getXX method
      // should be generated.
      int mapCount = atrMappings.size();
//System.out.println("no of atrMappings is "+ mapCount);			
      for (int i = 0; i < mapCount; i++) {
        //	System.out.println("------------------------------------------------");
        // this hashtable stores original EEntities, which were used to build retValues array of Strings.
        Hashtable allowedTypes = new Hashtable();
        EGeneric_attribute_mapping ega_mapping = (EGeneric_attribute_mapping) atrMappings.get(i);
        //			System.out.println("visited egaMaping is "+ega_mapping);
        // now, read associated entity_mapping:
        EEntity_mapping entMap = ega_mapping.getParent_entity(null);
        // now, read ent definition in aim, to be used inside this method for safe typecasting.
        String aimEntityName = getEntMapTargetName(entMap, false);
        //		System.out.println("aimEntityName"+aimEntityName);

        // A change: before activating previous functionality to genenerate a signature for GetXX method,
        // carry on needed test for dvem type users.

        processDueToDvem(ega_mapping, armAtrType, exp_attr, allowedTypes, aimEntityName, atrDisplayName, entMap, aimToOverloadParameter, getMethods);
        processInDefaultWay(ega_mapping, armAtrType, exp_attr, allowedTypes, aimEntityName, atrDisplayName, entMap, aimToOverloadParameter, getMethods);
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
        Hashtable printedBodies = new Hashtable();
        Enumeration body_entries = bodies.keys();
        while (body_entries.hasMoreElements()) {
          String key = (String) body_entries.nextElement();
          printString = (String) bodies.get(key);
          // this final duplication checking is important,
          // because different aim entities can map to the same
          // final value, and as a result we get identical bodies
          // in get method. This should not happen.
          if (printedBodies.containsKey(printString)) {
            continue;
          }
          else {
            printedBodies.put(printString, printString);
            methodString = methodString + printString;
          }
        }
        // append body with exception-based warning:
//				printString = "\t\tthrow new SdaiException(SdaiException.VA_NVLD, getErrorString1);\n"+
//								"\t}\n\n";
        printString = "\t}\n\n";
        methodString = methodString + printString;
        classMethods.add(methodString);
      }

      // we have to generate a generic getXX method for each attribute. Previous implementations
      // were returning Object, as basis for all Java objects. Now we have a request to return
      // AEntity, when object type is aggregate of entities. We should still return Object,
      // if that aggregate is of simple types, or of mixed types (simple + entity + aggregate).

      if (isAggregateInside(exp_attr.getDomain(null))) {
        // let's analyze, if we can support that specific type of aggregate (mixed and simple
        // are not supported).
        boolean supported = false;
        EEntity finalType = getFinalType(exp_attr.getDomain(null));

        if (finalType instanceof EEntity_definition) {
          supported = true;
        }
        else if (finalType instanceof ESelect_type) {
          supported = true;
          // we need further analysis to find out the contents of that select
          ANamed_type sel = ((ESelect_type) finalType).getSelections(null);
          SdaiIterator it = sel.createIterator();
          while (it.next()) {
            ENamed_type type = sel.getCurrentMember(it);
            if (isAggregateInside(type)) {
              // this is not supported for AEntity case. Shall stick to Object return value.
              supported = false;
              break;
            }
            EEntity nestedFinType = getFinalType(type);
            if (nestedFinType instanceof ESelect_type) {
              // we say we are not ready to this case:
              supported = false;
              break;
            }
            if (!(nestedFinType instanceof EEntity_definition)) {
              supported = false;
              break;
            }
          }
        }
        if (supported) {
          // generate case with AEntity
          String genericGet = "\tpublic static AEntity get" + atrDisplayName + "(ASdaiModel domain, EEntity instance)" + "throws SdaiException {\n";
          genericGet += "\t\tObject o = null;\n";
          genericGet += "\t\to = getAllValuesForAttribute(domain, metaDomain, at";
          genericGet += atrDisplayName;
          genericGet += ", instance);\n";
          genericGet += "\t\tif (o == null)\n";
          genericGet += "\t\t\tthrow new SdaiException(SdaiException.VA_NSET, instance," + "\"Attribute " + atrDisplayName.toLowerCase()
              + " for entity " + currentlyGeneratedClassName + " is not set.\");\n";
          genericGet = genericGet + "\t\treturn (AEntity) buildAggregate(o, AEntity.class);\n";
          genericGet += "\t}\n\n";

          String genericGetDocHeader = "static AEntity <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException";
          doc.addAttributeInfo(atrDisplayName.toLowerCase(), genericGetDocHeader);
          classMethods.add(genericGet);
        }
        else {
          // generate signature with Object, but dont use getAnyValueForAttribute inside.
          String genericGet = "\tpublic static Object get" + atrDisplayName + "(ASdaiModel domain, EEntity instance)" + "throws SdaiException {\n";
          genericGet += "\t\tObject o = null;\n";
          genericGet += "\t\to = getAllValuesForAttribute(domain, metaDomain, at";
          genericGet += atrDisplayName;
          genericGet += ", instance);\n";
          genericGet += "\t\tif (o == null)\n";
          genericGet += "\t\t\tthrow new SdaiException(SdaiException.VA_NSET, instance," + "\"Attribute " + atrDisplayName.toLowerCase()
              + " for entity " + currentlyGeneratedClassName + " is not set.\");\n";
          genericGet += "\t\treturn o;\n";
          genericGet += "\t}\n\n";

          String genericGetDocHeader = "static Object <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException";
          doc.addAttributeInfo(atrDisplayName.toLowerCase(), genericGetDocHeader);
          classMethods.add(genericGet);
        }
      }
      else {
        // let's add a generic getXX method for each attribute, which returns Object and has
        // a trivial signature:
        //public Object getAttributename(ASdaiModel domain, EEntity instance) throws SdaiException;
        // This was a request for change from Mx classes users in converters area.
        String genericGet = "\tpublic static Object get" + atrDisplayName + "(ASdaiModel domain, EEntity instance)" + "throws SdaiException {\n";
        genericGet += "\t\tObject o = null;\n";
        genericGet += "\t\to = getAnyValueForAttribute(domain, metaDomain, at";
        genericGet += atrDisplayName;
        genericGet += ", instance);\n";
        genericGet += "\t\tif (o == null)\n";
        genericGet += "\t\t\tthrow new SdaiException(SdaiException.VA_NSET, instance," + "\"Attribute " + atrDisplayName.toLowerCase() + " for entity "
            + currentlyGeneratedClassName + " is not set.\");\n";

        genericGet += "\t\treturn o;\n";
        genericGet += "\t}\n\n";

        String genericGetDocHeader = "static Object <B>get</B>" + atrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException";
        doc.addAttributeInfo(atrDisplayName.toLowerCase(), genericGetDocHeader);
        classMethods.add(genericGet);
      }
    }
  }

  /**
   * Implementation of this method should avoid to generate usedin methods
   * for attributes, which are of simple type, because it makes no sense
   * to have usedin operation for such attributes.
   */
  protected void generateUsedInXXAttributeMethods(Vector classMethods, EEntity_definition entDef) throws Exception {
    usedinAttributesInIteration = new Vector();
    SdaiIterator atrIt = entityAttributesInIteration.createIterator();
    while (atrIt.next()) {
      EExplicit_attribute exp_attr = (EExplicit_attribute) entityAttributesInIteration.getCurrentMemberObject(atrIt);

      EEntity armAtrType = SimpleOperationsOnExtended.getAttributeDomain(exp_attr);

      String attrDisplayName = capitalize(exp_attr.getName(null));

      String docHeader = "static AEntity <B>usedin</B>" + attrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException";
      // don't add header right now: it's possible that this method will not be generated.
      //!!doc.addAttributeInfo(attrDisplayName.toLowerCase(), docHeader);
      String methodHeader = "\tpublic static AEntity usedin" + attrDisplayName + "(ASdaiModel domain, EEntity instance) throws SdaiException {\n"
          + "\t\tAEntity retValue = new AEntity();\n" + "\t\tif (instance == null)\n\t\t\treturn retValue;\n";
      String fullBody = "\t\tAEntity retVal = instance.findMappedUsers(null, am" + attrDisplayName + "__Mappings,"
          + " domain, metaDomain, null, currentMappingMode);\n" + "\t\tcopyEntities(retValue, retVal);\n" + "\t\treturn retValue;\n";
      /*
       * sample generated code.
       * AEntity retVal = instance.findMappedUsers(null, aAssociated_item__Mappings,
       * domain, metaDomain, null, currentMappingMode);
       * copyEntities(retValue, retVal);
       * return retValue;
       */

      /*
       * The generated code is wrong, because method is invoked on class other than the instance matches,
       * and we no longer match the specification of findMappedUsers in jsdai.
       * String fullBody = "\t\tSdaiIterator it = eMappings.createIterator();\n"+
       * "\t\twhile (it.next()) {\n" +
       * "\t\t\tEEntity_mapping mapping = (EEntity_mapping) eMappings.getCurrentMemberObject(it);\n"+
       * "\t\t\tAAttribute_mapping ag = new AAttribute_mapping();\n"+
       * "\t\t\tSdaiIterator aIt = a"+attrDisplayName+"__Mappings.createIterator();\n"+
       * "\t\t\twhile (aIt.next()) {\n"+
       * "\t\t\t\tEGeneric_attribute_mapping aMapping = (EGeneric_attribute_mapping) a"+attrDisplayName+
       * "__Mappings.getCurrentMemberObject(aIt);\n"+
       * "\t\t\t\tif (aMapping.getParent_entity(null) != mapping)\n"+
       * "\t\t\t\t\tcontinue;\n"+
       * "\t\t\t\tif (aMapping instanceof EAttribute_mapping) {\n"+
       * "\t\t\t\t\tag.addUnordered(aMapping);\n"+
       * "\t\t\t\t}\n"+
       * "\t\t\t}\n"+
       * "\t\t\tAEntity retVal = instance.findMappedUsers(mapping, ag, domain, metaDomain, null, currentMappingMode);\n"+
       * "\t\t\tcopyEntities(retValue, retVal);\n"+
       * "\t\t}\n"+
       * "\t\treturn retValue;\n";
       */
      methodHeader = methodHeader + fullBody;
      // the source below must determine, if this method is worth to be generated.
      boolean hasAtLeastOneEntry = false;
      SdaiIterator entMapIt = entityMappingsInIteration.createIterator();
      while (entMapIt.next()) {
        EEntity_mapping entMap = (EEntity_mapping) entityMappingsInIteration.getCurrentMemberObject(entMapIt);
        Vector atrMappings = (Vector) attrMappingsInIteration.get(exp_attr.getName(null));
        if (atrMappings == null) {
          throw new Exception("Malformed hashtable for current attribute!" + exp_attr);
        }
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
          hasAtLeastOneEntry = true;
        }
      }
      methodHeader += "\t}\n";
      if (hasAtLeastOneEntry) {
        doc.addAttributeInfo(attrDisplayName.toLowerCase(), docHeader);
        usedinAttributesInIteration.add(exp_attr);
        classMethods.add(methodHeader);
        classMethods.add("\n");
      }
    }
  }

  protected void generateUsersMethod(Vector classMethods, EEntity_definition entDef) throws Exception {
    String docHeader = "static AEntity findUsers(ASdaiModel domain, EEntity instance) throws SdaiException";
    String description = "Method allows to find all AIM instances that can be mapped to this ARM"
        + " entity and reference given AIM instance through any of their atttibutes.";

    String methodHeader = "\tpublic static AEntity findUsers(ASdaiModel domain, EEntity instance)" + " throws SdaiException {\n"
        + "\t\tAEntity retValue = new AEntity();\n" + "\t\tif (instance == null)\n\t\t\treturn retValue;\n";

    methodHeader += "\t\tSdaiIterator it = eMappings.createIterator();\n" + "\t\twhile (it.next()) {\n"
        + "\t\t\tEEntity_mapping mapping = (EEntity_mapping) eMappings.getCurrentMemberObject(it);\n"
        + "\t\t\tAEntity retVal = instance.findMappedUsers(mapping, null, domain, metaDomain, null, currentMappingMode);\n"
        + "\t\t\tcopyEntities(retValue, retVal);\n" + "\t\t}\n";
    /*
     * too many invocations/people complain it's too slow.
     * int usedinNo = -1;
     * for (int i=0;i<usedinAttributesInIteration.size();i++) {
     * usedinNo++;
     * EExplicit_attribute attr = (EExplicit_attribute) usedinAttributesInIteration.get(i);
     * String attrDisplayName = capitalize(attr.getName(null));
     * String usedinCall = "\t\tAEntity ag"+usedinNo+" = usedin"+attrDisplayName+
     * "(domain, instance);\n"+
     * "\t\tcopyEntities(retValue, ag"+usedinNo+");\n";
     * methodHeader += usedinCall;
     * }
     * if (usedinNo == -1)
     * return;// do not generate findUsers method!
     */
    doc.addCommonMethods(docHeader, description);

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
