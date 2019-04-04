package jsdai.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SMapping_schema.AEntity_mapping;
import jsdai.SMapping_schema.AGeneric_attribute_mapping;
import jsdai.SMapping_schema.AUof_mapping;
import jsdai.SMapping_schema.CGeneric_attribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EGeneric_attribute_mapping;
import jsdai.SMapping_schema.ESchema_mapping;
import jsdai.SMapping_schema.EUof_mapping;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.tools.refPath.EntityReferencePath;
import jsdai.util.SimpleOperations;

public class MappingDoc {
  protected static final String PRINT_START_LABEL = "<!--MappingDoc starts here-->";
  protected static final String PRINT_END_LABEL = "<!--MappingDoc ends here-->";

  public String apNumber = "";
  public String rootLocation = "";
  protected String schemaName = "";

  public MappingDoc() {
  }

  private String parseExactAttributeType(EAttribute sourceAttribute, EGeneric_attribute_mapping attrMapping) throws SdaiException, IOException {
    String[] proposedType = new String[1];
    String aggrInfo = MappingDocUtils.getDomainTypeName(sourceAttribute, proposedType);
    String dataTypeName = MappingDocUtils.getDataTypeName(attrMapping);
    if (dataTypeName.length() > 0) {
      return aggrInfo + dataTypeName;
    }
    else {
      return aggrInfo + proposedType[0];
    }
  }

  private String parseAndPrintEntityMapping(EEntity_mapping entMapping) throws SdaiException, IOException {

    String result = ".";
    if (entMapping.testConstraints(null)) {
      EEntity constraint = entMapping.getConstraints(null);
      EntityReferencePath path = new EntityReferencePath();
      MappingDocOperations.currentPathElement = constraint;
      path.parse(constraint, entMapping);
      result = path.display();
    }
    /*
     * EEntity target = entMapping.getTarget(null);
     * EEntity previousEntity = null;
     * if (target instanceof EEntity_definition) {
     * previousEntity = target;
     * } else if (target instanceof EAttribute) {
     * EAttribute ta = (EAttribute)target;
     * previousEntity = ta.getParent_entity(null);
     * }
     *
     * if (entMapping.testConstraints(null)) {
     * EEntity constraint = entMapping.getConstraints(null);
     * result += MappingDocOperations.printSuperTypes(previousEntity,
     * MappingDocOperations.getStartType(constraint));
     * result += MappingDocUtils.parseConstraint(constraint, null, false, false);
     * }
     */
    return result;
  }

  private void parseAndPrintAttributeMapping(Hashtable attrInfoTable, EEntity_mapping entMapping) throws SdaiException, IOException {
    EEntity targetEnt = entMapping.getTarget(null);
    if (!(targetEnt instanceof EEntity_definition)) {
      throw new IOException("Entity mapping target is not entity_definition! Generator not adopted to this case.");
    }

    AGeneric_attribute_mapping agAttrMappings = new AGeneric_attribute_mapping();
    CGeneric_attribute_mapping.usedinParent_entity(null, entMapping, null, agAttrMappings);
    SdaiIterator it = agAttrMappings.createIterator();
    while (it.next()) {
      EGeneric_attribute_mapping attrMapping = agAttrMappings.getCurrentMember(it);
      EAttribute sourceAttribute = attrMapping.getSource(null);
      // read the name of ARM attribute:
      String armAttributeName = sourceAttribute.getName(null);
      Vector attrEntries = MappingDocUtils.getAttrInfoVector(attrInfoTable, armAttributeName);
      AttrInfo info = new AttrInfo();
      info.setAttrName(armAttributeName);
      // find out to what this attribute maps (aggregate info, exact info from select,etc.)
      String exactAttributeType = parseExactAttributeType(sourceAttribute, attrMapping);
      info.setAimTypeName(exactAttributeType);
      if (MappingDocOperations.isSelectInside(sourceAttribute)) {
        AttrInfo selectInfo = new AttrInfo();
        // add this special select to the beginning of vector (if similar special instance was
        // not yet added to vector)
        if (attrEntries.size() == 0) {
          attrEntries.add(selectInfo);
        }
        else {
          AttrInfo testInfo = (AttrInfo) attrEntries.get(0);
          if (!testInfo.isSelectTypeInside()) {
            attrEntries.insertElementAt(selectInfo, 0);
          }
        }
        selectInfo.setAttrName(armAttributeName);
        String[] proposedType = new String[1];
        String aggrInfo = MappingDocUtils.getDomainTypeName(sourceAttribute, proposedType);

        selectInfo.setAimTypeName(aggrInfo + proposedType[0]);
        // we know how to get domain from explicit_attribute. If this is not explicit attribute,
        // then tell this by throwing exception:
        if (!(sourceAttribute instanceof EExplicit_attribute)) {
          throw new IOException("ARM attribute " + sourceAttribute + " is not explicit_attribute!" + " Do not know how to respond.");
        }
        selectInfo.setIsSelectType();
      }
      // now, common stuff for all attributes
      // determine, whether this is optional or required attribute:
      if (sourceAttribute instanceof EExplicit_attribute) {
        boolean isOptional = ((EExplicit_attribute) sourceAttribute).getOptional_flag(null);
        if (isOptional) {
          info.setIsOptional();
        }
      }
      String aimColumn = MappingDocUtils.determineAttrMappingType(attrMapping);
      info.setAimColumn(aimColumn);
      String refPath = MappingDocUtils.parseReferencePath(attrMapping, entMapping);
      info.setRefPath(refPath);
      attrEntries.add(info);
    }
  }

  /**
   * @param uofEntries A hashtable with complex internal structure. All generated output
   *                   is passed to this hashtable. Here is description of internal
   *                   structure for it:
   *                   uofEntries [key= fileName (where info from value should be passed to),
   *                   value= HT2]
   *                   HT2 [key= entity_mapping target (aimName), value= Vector]
   *                   Vector[0] = Object, representing reference path for entity_mapping itself.
   *                   Vector[1] = HT3
   *                   HT3 [key= arm attribute name, value= Vector]
   *                   Vector element type = AttrInfo
   */
  private void printEntityMapping(Hashtable uofEntries, EEntity_mapping entMapping, ESchema_mapping schemaMapping) throws SdaiException, IOException {
    // protect ourselves from malformed entity mappings.
    if (!entMapping.testTarget(null)) {
      return;
    }
    // now, locate the file where method output should be directed
    String armEntityName = entMapping.getSource(null).getName(null);
    String fileName = "jsdai" + File.separator + schemaName + File.separator + MappingDocUtils.toSentenceCase(armEntityName);
    // for debugging only:
//		if (!armEntityName.equalsIgnoreCase("accuracy"))
//			return;

    Hashtable myOutput = null;
    if (uofEntries.containsKey(fileName)) {
      myOutput = (Hashtable) uofEntries.get(fileName);
    }
    else {
      myOutput = new Hashtable();
      uofEntries.put(fileName, myOutput);
    }
    String aimEntityName = ((EEntity_definition) entMapping.getTarget(null)).getName(null);
    Vector myEntries = new Vector();
    // create an object for entity_mapping ref-path.
    Object emRefPath = parseAndPrintEntityMapping(entMapping);
    // create a hashtable for storing attributes info
    Hashtable attrInfoTable = new Hashtable();
    // link all together:
    myEntries.add(emRefPath);
    myEntries.add(attrInfoTable);
    myOutput.put(aimEntityName, myEntries);

    // ok. Now move to parse attribute mappings immediately:
    parseAndPrintAttributeMapping(attrInfoTable, entMapping);

  }

  private void printUofMapping(EUof_mapping uof, ESchema_mapping schema) throws IOException, SdaiException {
    Hashtable uofMappings = new Hashtable();
    AEntity_mapping entities = uof.getMappings(null);
    SdaiIterator it_entities = entities.createIterator();
    while (it_entities.next()) {
      EEntity_mapping em = entities.getCurrentMember(it_entities);
//			System.out.println("----------------------------------------------");
//			System.out.println("processing entity_mapping "+ em);			
      printEntityMapping(uofMappings, em, schema);
    }
    // ok, now output things to real files. Clean html files before inserting new mapping tables
    Enumeration keys = uofMappings.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      // good. Key == file name without extension, so now we can find and clean html file
      // from old output. Let's call function to do that.
      FileWriter outFile = MappingDocUtils.removePreviousEntries(key, rootLocation, PRINT_START_LABEL, PRINT_END_LABEL);
      Hashtable entMappingOutput = (Hashtable) uofMappings.get(key);
      // now, print mapping table for single entity_mapping
      MappingDocUtils.printTableForEntityMapping(outFile, key, entMappingOutput, PRINT_START_LABEL, PRINT_END_LABEL);
      // file was opened in removePreviousEntries function.
      outFile.close();
    }
  }

  private void printSchemaMapping(ESchema_mapping schema) throws IOException, SdaiException {
    // let's read schema name- it should be usefull for specifying path to updatable html files.
    schemaName = "S" + ExpressDoc.getUpper(schema.getSource(null).getName(null));
    AUof_mapping uofs = schema.getUofs(null);
    SdaiIterator it_uofs = uofs.createIterator();
    while (it_uofs.next()) {
      EUof_mapping uof = uofs.getCurrentMember(it_uofs);
//			if (!uof.getName(null).equalsIgnoreCase("item"))
//				continue;
//			System.out.println("==========================================================");
//			System.out.println("Expecting to print uof mapping: "+uof);				
      printUofMapping(uof, schema);
//			System.out.println("Successfully completed printing uof mapping "+ uof);
    }
  }

  private void printMappingSchemas(SdaiRepository repo) throws IOException, SdaiException {
    repo.openRepository();
    ASdaiModel models = repo.getModels();
    SdaiIterator it_models = models.createIterator();
    while (it_models.next()) {
      SdaiModel model = models.getCurrentMember(it_models);
      if (model.getUnderlyingSchema().getName(null).equalsIgnoreCase("MAPPING_SCHEMA")) {
        ESchema_mapping schema = MappingDocUtils.findSchemaMapping(model);

        if (model.getName().indexOf(apNumber) == -1) {
          continue; // skip undesired mapping repositories.
        }

        System.out.println("Generating mapping tables for " + model.getName() + ".");
        printSchemaMapping(schema);
      }
    }
  }

  public final static void main(String args[]) throws SdaiException, IOException {
    try {
      if (args.length != 3) {
        System.out.println("Program usage: java MappingDoc ap_number repo_location output_directory");
        System.out.println("Usage example: java MappingDoc 214 ExpressCompilerRepo d:\\cvs_projects\\");
        return;
      }
      System.out.println("Starting mapping tables generation stage...");

      MappingDoc md = new MappingDoc();
      md.apNumber = args[0];
      md.rootLocation = args[2];

      SdaiSession session = SdaiSession.openSession();
      session.startTransactionReadWriteAccess();

      md.printMappingSchemas(SimpleOperations.linkRepositoryOrName("MappingDoc", args[1]));

      session.closeSession();

      System.out.println("Successfully completed generating mapping tables.");
      return;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Mapping tables generation stage was not completed successfully.");
  }
}