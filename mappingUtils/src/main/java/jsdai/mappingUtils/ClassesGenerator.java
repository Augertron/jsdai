package jsdai.mappingUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;

public class ClassesGenerator extends ClassesGeneratorUtilityFunctions {
  /**
   *
   */
  public ClassesGenerator(String apPartNumber, String genTargetDir, String mainSchema, boolean genMainFunction) throws SdaiException, Exception {
    super(apPartNumber, genTargetDir, mainSchema, genMainFunction, null);

  }

  /**
   *
   */
  public ClassesGenerator(String apPartNumber, String mainSchema, String docSourcePath) throws SdaiException, Exception {
    super(apPartNumber, null, mainSchema, false, docSourcePath);

  }

  /**
   *
   */
  public void generateClassSkeleton(FileWriter outFile, String className, EEntity_definition entDef) throws Exception {
    if (outFile != null) {
      // write out package, import statements class header:
      outFile.write("package jsdai.MAp" + apNumber + ";\n\n");
      outFile.write("import jsdai.lang.*;\n");
      outFile.write("import jsdai.dictionary.*;\n");
      outFile.write("import jsdai.mapping.*;\n\n");
    }

    // prepare for new iteration: clear any old data about classes
    doc.clear();
    importStatements.clear();
    classDeclaration = "public abstract class " + className + " extends Ap" + apNumber + " {\n\n";
    entityDefinitions.clear();
    entityMappings.clear();
    attributes.clear();
    classConstructor = "\tprivate M" + getEntDefTargetName(entDef, true) + "() {}\n\n";
    classMethods.clear();
    classClosingBracket = "}\n";
    staticConstants.clear();

    String constantsDescr = " <DD/>As class can have several similar looking getX methods for each attribute,"
        + " it's important to know which of them can be used to read the value of"
        + " attribute. A set of public constants are generated for each class to help" + " you to handle this problem.<BR>"
        + " <DD/>In class documentation you can see that" + " at least one public constant exists for each attribute." + " These associated constants"
        + " are used as return values from testX methods and helps you to find out" + " at runtime which overloaded getX method should be used.<BR>"
        + " The first part of constant name identifies" + " the attribute, the second part - the last parameter in overloaded"
        + " method. This information is sufficient to find appropriate overloaded" + " getX method and learn what is the type of return value.<BR>"
        + " <DD/>Example:<BR><DD/> let's assume we have the following constant:<BR>" + " <DD/>static final int ACTIVITY_TYPE__STRING<BR>"
        + " Now, let's assume you have called the <B>testActivity_type</B> method, and its return"
        + " value is equal to this constant. This means that for reading value of Activity_type"
        + " attribute you must use <B>getActivity_type</B> method, where the type of"
        + " last parameter is String. To see what is expected return type from this"
        + " <B>getActivity_type</B> method, you must find and study its declaration.<BR>"
        + " <DD/>One constant is implicitly declared and applicable for each attribute:"
        + " it's <A HREF=MappingUtil.html>MappingUtil</A>.VA_NSET constant," + " or just zero. This value is returned"
        + " from any testX method, when attribute value is not set.<BR>";

    doc.setNameOfGeneratedClass(className);
    doc.setAnyDescriptionForConstants(constantsDescr);

    String commonMethodHeader = "static void changeMappingModeTo(int newMappingMode)";
    String description = " As this class (and other similar classes) extensively use mapping operations" + " to fulfill user's requests,"
        + " it needs to know how restrictive mapping operations should be. By default,"
        + " it takes minimalistic approach and assumes that mapping mode is"
        + " EEntity.NO_RESTRICTIONS. <BR><DD/>This method allows to change default mapping"
        + " mode to EEntity.MOST_SPECIFC_ENTITY or EEntity.MANDATORY_ATTRIBUTES_SET."
        + " <BR><DD/><B>NOTE:</B> Changing default mapping mode affects all static classes,"
        + " starting with prefix M and all operations, defined in those classes.";
    doc.addCommonMethods(commonMethodHeader, description);

    generateRequiredToImportClasses(importStatements, entDef);
    generateEntityDefinition(entityDefinitions, entDef);
    generateMappingsBlock(entityMappings, entDef);
    generateAttributesBlock(attributes, entDef);
    generateClassMethods(classMethods, entDef);

    if (outFile == null) {
      // okay, now we are ready to generate documentation for given class
      generateHtmlDocumentation(entDef);
      return;
    }

    // now, output the whole stuff in required order:
    Enumeration statements = importStatements.keys();
    while (statements.hasMoreElements()) {
      String statement = (String) statements.nextElement();
      if (!statement.startsWith("java")) {
        outFile.write("import jsdai.S" + statement + ".*;\n");
      }
      else {
        outFile.write("import " + statement + ";\n");
      }
    }
    outFile.write("\n");
    outFile.write(classDeclaration);

    Enumeration constants = staticConstants.keys();
    int constValue = 1;

    while (constants.hasMoreElements()) {
      String constant = (String) constants.nextElement();
      String printVal = "\tpublic static final int " + constant.toUpperCase() + " = " + constValue++ + ";\n";
      outFile.write(printVal);
    }
    outFile.write("\n");

    int entDefCount = entityDefinitions.size();
    for (int i = 0; i < entDefCount; i++) {
      String entityDefinition = (String) entityDefinitions.get(i);
      outFile.write(entityDefinition);
    }
    outFile.write("\n");

    int entMapCount = entityMappings.size();
    for (int i = 0; i < entMapCount; i++) {
      String entityMapping = (String) entityMappings.get(i);
      outFile.write(entityMapping);
    }
    outFile.write("\n");
    int atrCount = attributes.size();
    for (int i = 0; i < atrCount; i++) {
      String attribute = (String) attributes.get(i);
      outFile.write(attribute);
    }
    outFile.write("\n");
    outFile.write(classConstructor);
    int classMethCount = classMethods.size();
    for (int i = 0; i < classMethCount; i++) {
      String classMethod = (String) classMethods.get(i);
      outFile.write(classMethod);
    }
    outFile.write("\n");
    outFile.write(classClosingBracket);

  }

  /**
   *
   */
  public void generateClassMethods(Vector classMethods, EEntity_definition entDef) throws SdaiException, Exception {
    // NOTE: order of generation is important, because, for example, generateTestXX.. uses results, produced in
    // generateGetXX.. method.

    generateFindInstanceMethods(classMethods, entDef);
    generateGetXXAttributeMethods(classMethods, entDef);
    generateTestXXAttributeMethods(classMethods, entDef);
    generateCanBeMappedFromMethod(classMethods, entDef);
    if ((isHidingWriteOperations) && (isGeneratingDocumentation)) {
      // there should be only one case, when generation of setXX methods should
      // be skipped.
      ;
    }
    else {
      generateSetXXAttributeMethods(classMethods, entDef);
    }
    generateUsedInXXAttributeMethods(classMethods, entDef);
    generateUsersMethod(classMethods, entDef);
    // generate main method for doing a test on specific class:
    if (generateMainFunction) {
      generateMainMethod(classMethods, entDef);
    }

  }

  /**
   *
   */
  public void generateClasses() throws SdaiException, Exception {
    if (!isGeneratingDocumentation) {
      System.out.println("Starting .java files generation stage...");
    }
    else {
      System.out.println("Starting documentation files append/update stage...");
    }
    FileWriter outFile = null;
    FileWriter outTestFile = null;

    if (generatedTargetDir != null) {
      (new File(generatedTargetDir)).mkdirs();
      outTestFile = new FileWriter(generatedTargetDir + File.separator + "TestGenClasses.java");
      String testFileHeader = "package jsdai.MAp" + apNumber + ";\n\n" + "import jsdai.lang.*;\n" + "import jsdai.dictionary.*;\n"
          + "import jsdai.mapping.*;\n\n" + "public class TestGenClasses {\n\n" + "public static final void main (String args[]) {\n"
          + "\tEEntity entity = null;\n";
      outTestFile.write(testFileHeader);
    }

    // what is very important to speed up generation process is to build hash table
    // out of schema names and then use it to access another hashtable with entries
    // for entity_definitions within that schema.
    buildSchemaContentsTable();

    int count = arm_entities.length;
    for (int i = 0; i < count; i++) {
      String defName = arm_entities[i].getName(null);
      // capitalize first letter of name:
      defName = changeFirstLetterToUpperCase(defName);
//			System.out.println("defName is "+defName);
//			if (!defName.equalsIgnoreCase("duration"))
//			if (!defName.equalsIgnoreCase("planned_characteristic"))
//				continue;

      if (outTestFile != null) {
        String testString = "\t\tentity = M" + defName + ".armEntity;\n";
        outTestFile.write(testString);
      }
      if (generatedTargetDir != null) {
        String fileName = "M" + defName + ".java";
        outFile = new FileWriter(generatedTargetDir + File.separator + fileName);
        generateClassSkeleton(outFile, "M" + defName, arm_entities[i]);
        outFile.close();
      }
      else {
        generateClassSkeleton(null, "M" + defName, arm_entities[i]);
      }
    }
    if (outTestFile != null) {
      outTestFile.write("\tSystem.out.println(\"Classes loaded successfully!\");\n}\n}");
      outTestFile.close();
    }
  }

  /**
   *
   */
  public static final void main(String args[]) throws SdaiException, Exception {
    // start sdai session:
    SdaiSession session = SdaiSession.openSession();
    SdaiTransaction trx = session.startTransactionReadWriteAccess();

    if (args.length < 3) {
      System.out.println("USAGE: java jsdai.mappingUtils.ClassesGenerator apnumber mainschemaname targetpath");
      System.out.println("OR USAGE: java jsdai.mappingUtils.ClassesGenerator apnumber mainschemaname -documentation docSourcePath [-read_only_op]");
      return;
    }
    // declare a bunch of variables to store parsed info from args list:
    String apNumber = null;
    String mainSchemaName = null;
    String outputPath = null;
    String docSourcePath = null;
    boolean isDocumenting = false;
    boolean isHidingSetOperations = false;
    boolean generateMain = false;

    System.out.println("Early binding classes generator for AP" + args[0] + " is starting...");
    ClassesGenerator gen = null;

    for (int i = 0; i < args.length; i++) {
      if (i == 0) {
        apNumber = args[0];
      }
      if (i == 1) {
        mainSchemaName = args[1];
      }
      if (i == 2) {
        if (args[2].equalsIgnoreCase("-documentation")) {
          isDocumenting = true;
        }
        else {
          outputPath = args[2];
        }
      }
      if (i == 3) {
        if (!isDocumenting) {
          generateMain = true; // this is undocumented feature: if we are generating classes
        }
        // (not doc!), and we specify >= 4 input parameters, then
        // special Main function is added to each generated class.
        else {
          docSourcePath = args[3];
        }
      }
      if (i == 4) {
        if ((isDocumenting) && (args[4].equalsIgnoreCase("-read_only_op"))) {
          isHidingSetOperations = true;
        }
      }
    }
    // now, let's recognize which constructor should be called:
    if (!isDocumenting) {
      gen = new ClassesGenerator(apNumber, outputPath, mainSchemaName, generateMain);
    }
    else {
      // we are documenting. So:
      gen = new ClassesGenerator(apNumber, mainSchemaName, docSourcePath);
      gen.isGeneratingDocumentation = isDocumenting;
      gen.isHidingWriteOperations = isHidingSetOperations;
    }

    gen.generateClasses();

    System.out.println("Earling binding classes generator has finished.");
  }

}
