package jsdai.mappingUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Class should not be accesible outside of this package.
 */
class ClassDocumentation {

  protected String LABEL_OF_INSERT_START = "<!--ADDON STARTS HERE-->";
  /**
   * Only one ap can be active at the time, so this var
   * stores its number.
   */
  protected String apNumber = "";

  // example, to be inserted into documentation. Empty
  // temporary, untill good example exists.
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

  protected String nameOfGeneratedClass = new String();
  protected String anyFreeDescriptionForClass = new String();
  protected String anyDescriptionForConstants = new String();

  /**
   * Stores immediate supertypes for this ARM entity.
   */
  protected Vector supertypes = new Vector();

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

  protected static final String NO_ATTRIBUTES_WARNING = "There is no known declared attributes for ARM entity, represented by" + " this static class.";

  protected static final String SEE_SUPERTYPES_WARNING = "There is no known declared attributes for ARM entity, represented by"
      + " this class. However, this ARM entity has supertypes. See" + " implementations of the supertypes to find out about available "
      + " attributes and methods: ";

  ClassDocumentation(String apNumber, String startLabel) {
    this.apNumber = apNumber;
    this.LABEL_OF_INSERT_START = startLabel;
  }

  public void clear() {
    nameOfGeneratedClass = new String();
    anyFreeDescriptionForClass = new String();
    anyDescriptionForConstants = new String();
    staticConstants.clear();
    attributes.clear();
    commonMethods.clear();
    supertypes.clear();
  }

  public void addSupertype(String value) {
    if (!supertypes.contains(value)) {
      supertypes.add(value);
    }
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
    printH3(file, printTarget("Early binding static class " + visualize(nameOfGeneratedClass) + " (jsdai.MAp" + apNumber + ")", "classDef"));
    printTableTail(file);
    if (anyFreeDescriptionForClass.length() > 0) {
      print(file, anyFreeDescriptionForClass);
      println(file);
    }
    printTableHeader(file);
    if (!attributes.isEmpty()) {
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
        //println(file);
      }
    }
    else {
      // a case, where entity has no attributes, and we need to report
      // one of two possible strings: one string says there is no attributes
      // and no supertypes to visit for the attributes, another says there is
      // no attributes, but there is supertypes to visit for other attributes.
      //printTableHeader(file);
      if (supertypes.size() == 0) {
        printTab(file, NO_ATTRIBUTES_WARNING);
        //println(file);
        System.out.println("1st case printed for entity " + nameOfGeneratedClass);
      }
      else {
        System.out.println("2nd case printed for entity " + nameOfGeneratedClass);
        print(file, "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ");
        print(file, SEE_SUPERTYPES_WARNING);
        //printTab(file, SEE_SUPERTYPES_WARNING);
        for (int i = 0; i < supertypes.size(); i++) {
          String supertype = (String) supertypes.get(i);
          printHRef(file, visualize(supertype), visualize(supertype) + ".html#classDef");
          if ((i + 1) != supertypes.size()) {
            print(file, ", ");
          }
        }
        print(file, "<BR>");
        //println(file);
      }
      //printTableTail(file);
    }
    printTableTail(file);
    if (!commonMethods.isEmpty()) {
      // ok. Due to request from Lothar, we will print common methods to
      // separate file. So, let's create that file first:
      File fileMGE = new File(docRootFolder + File.separator + "jsdai" + File.separator + "lang" + File.separator + "MGenericEntries.html");
      if (!fileMGE.exists()) {
        FileWriter genericFile = new FileWriter(docRootFolder + File.separator + "jsdai" + File.separator + "lang" + File.separator
            + "MGenericEntries.html");
        printHtmlHead(genericFile, "Generic entries");
        printH2(genericFile, "Common information for classes" + " MXxx, supporting the ARM/AIM mapping");

        printH3(genericFile, "Example of using early binding mapping class");
        print(genericFile, exampleString);
        println(genericFile);

        if (anyDescriptionForConstants.length() > 0) {
          printH3(genericFile, printTarget("The meaning of public constants in each class", "constants"));
          //printTab(genericFile, " ");
          //print(genericFile, anyDescriptionForConstants);
          printTab(genericFile, anyDescriptionForConstants);
          println(genericFile);
        }
        //printTab(genericFile, " ");
        println(genericFile);
        printH3(genericFile, printTarget("Common generic methods", "ComGenMeth"));

        Object[] keys = commonMethods.keySet().toArray();
        Arrays.sort(keys);
        int keyCount = keys.length;
        for (int i = 0; i < keyCount; i++) {
          String method = (String) keys[i];
          String descr = (String) commonMethods.get(method);
          //printTab(genericFile," ");
          //println(genericFile, descr);
          printTab(genericFile, descr);
          //printTableHeader(genericFile);
          print(genericFile, "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ");
          printBold(genericFile, method);
          print(genericFile, "<BR>");
          //printTableTail(genericFile);
          println(genericFile);
        }
        printHtmlTail(genericFile);
        genericFile.close();
      }
      printH4(file, printHRef("Other available methods", "../lang/MGenericEntries.html#ComGenMeth"));
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
        //println(file);
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
    //printTableHeader(file);
    Object[] keys = linkedConsts.keySet().toArray();
    Arrays.sort(keys);
    int keyCount = keys.length;
    for (int i = 0; i < keyCount; i++) {
      String staticConstantName = (String) keys[i];
      //printTab(file, " ");
      //println(file, "static final int " +
      printTab(file, "static final int " + printHRef(staticConstantName, "../lang/MGenericEntries.html#constants"));
    }
    //printTableTail(file);
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
    f.write("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; " + s + "<BR>\n");
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
    f.write("<TABLE BORDER=\"0\" WIDTH=\"100%\"><TR><TD>\n");
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
