/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Oct 27, 2003 10:44:45 AM
 */
package jsdai.tools.validator;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import com.lksoft.util.*;
import com.lksoft.util.logging.*;

import jsdai.dictionary.*;
import jsdai.lang.*;

public class StepDiff {
  public static final String VERSION = "Step file differ v1.0 alpha";
  public static final String DATE = "2003.10.27";
  public static final String COPYRIGHT = "2003";

  public static final String ARG_SOURCE1 = "-s1";
  public static final String ARG_SCHEMA1 = "-schema1";
  public static final String ARG_SOURCE2 = "-s2";
  public static final String ARG_SCHEMA2 = "-schema2";
  public static final String ARG_LOG = "-l";

  private ASdaiModel dataDomain1;
  private ASdaiModel dataDomain2;

  public Logger logger;

  public StepDiff(Logger logger) {
    this.logger = logger;
  }

  public boolean beginSession(String p21File1, String schemaName1, String p21File2, String schemaName2)
      throws SdaiException {
    logger.info("Reading: " + p21File1);
    ElapsedTime time = new ElapsedTime();
    dataDomain1 = readSchema(p21File1, schemaName1);
    if (dataDomain1 != null) {
      logger.info("Data read from file 1. Elapsed time: " + time.stop());
    }
    else {
      logger.info("File 1 read errors...");
      return false;
    }

    logger.info("Reading: " + p21File2);
    time = new ElapsedTime();
    dataDomain2 = readSchema(p21File2, schemaName2);
    if (dataDomain2 != null) {
      logger.info("Data read from file 2. Elapsed time: " + time.stop());
    }
    else {
      logger.info("File 2 read errors...");
      return false;
    }

    return true;
  }

  private ASdaiModel readSchema(String p21File, String schemaName)
      throws SdaiException {
    // open step file for reading
    SdaiSession session = SdaiSession.getSession();
    if (session == null) {
      session = SdaiSession.openSession();
      session.startTransactionReadWriteAccess();
    }

    SdaiRepository repo = session.importClearTextEncoding("", p21File, null);

    // extract sdai models built on working schema. Build target domain
    ASchemaInstance aSchemas = repo.getSchemas();
    if (schemaName == null) {
      if (aSchemas.getMemberCount() == 0) {
        logger.fine("Specified repository has no schema instances.");
        return null;
      }

      return aSchemas.getAssociatedModels();
    }
    else {
      for (SdaiIterator i = aSchemas.createIterator(); i.next(); ) {
        SchemaInstance schema = aSchemas.getCurrentMember(i);
        if (schema.getName().equals(schemaName)) {
          return schema.getAssociatedModels();
        }
      }

      logger.fine("There is no schema instance named <" + schemaName + "> in specified repository.");
    }

    return null;
  }

  public boolean compare()
      throws SdaiException {
    boolean differ = false;

    Collection schemaDefs = new HashSet();
    for (SdaiIterator i = dataDomain1.createIterator(); i.next(); ) {
      SdaiModel model = dataDomain1.getCurrentMember(i);
      schemaDefs.add(model.getUnderlyingSchema());
    }
    for (SdaiIterator i = dataDomain2.createIterator(); i.next(); ) {
      SdaiModel model = dataDomain2.getCurrentMember(i);
      schemaDefs.add(model.getUnderlyingSchema());
    }

    Collection entityDefs = new HashSet();
    for (Iterator i = schemaDefs.iterator(); i.hasNext(); ) {
      ESchema_definition eSchemaDef = (ESchema_definition) i.next();
      AEntity aEntDec = eSchemaDef.findEntityInstanceSdaiModel().getInstances(CEntity_declaration.class);
      for (SdaiIterator j = aEntDec.createIterator(); j.next(); ) {
        EEntity_declaration eEntDec = (EEntity_declaration) aEntDec.getCurrentMemberEntity(j);
        EEntity_definition eDef = (EEntity_definition) eEntDec.getDefinition(null);
        entityDefs.add(eDef);
      }
    }

    for (Iterator i = entityDefs.iterator(); i.hasNext(); ) {
      EEntity_definition eDef = (EEntity_definition) i.next();
      int n1 = dataDomain1.getExactInstances(eDef).getMemberCount();
      int n2 = dataDomain2.getExactInstances(eDef).getMemberCount();
      if (n1 != n2) {
        logger.fine((n2 - n1) + " = " + n1 + " : " + n2 + "; " + eDef.getName(null));
        differ = true;
      }
    }

    return differ;
  }

  public static void main(String[] args) {
    ElapsedTime totalTime = new ElapsedTime();
    String className = TestEnvironment.class.getName();

    // print logo
    String logo = ArgumentsParser.getLogo(VERSION, DATE, COPYRIGHT);

    // parse command line args
    ArgumentsParser.Value sourceArg1 = new ArgumentsParser.Value(ARG_SOURCE1, true);
    sourceArg1.setValueName("sourceFile1");
    sourceArg1.setDescription("STEP source file name of first schema.");

    ArgumentsParser.Value schemaArg1 = new ArgumentsParser.Value(ARG_SCHEMA1, false);
    schemaArg1.setValueName("schemaName1");
    schemaArg1.setDescription("Name of first schema that will be compared. If not specified, first schema is taken.");

    ArgumentsParser.Value sourceArg2 = new ArgumentsParser.Value(ARG_SOURCE2, true);
    sourceArg2.setValueName("sourceFile2");
    sourceArg2.setDescription("STEP source file name of second schema.");

    ArgumentsParser.Value schemaArg2 = new ArgumentsParser.Value(ARG_SCHEMA2, false);
    schemaArg2.setValueName("schemaName2");
    schemaArg2.setDescription("Name of second schema that will be compared. If not specified, first schema is taken.");

    ArgumentsParser.Value logArg = new ArgumentsParser.Value(ARG_LOG, false);
    logArg.setValueName("logFile");
    logArg.setDescription("File for logging.");

    List argList = Arrays.asList(new Object[] { sourceArg1, schemaArg1, sourceArg2, schemaArg2, logArg });
    if (!ArgumentsParser.parse(args, argList)) {
      System.out.println(logo);
      System.out.println();
      System.out.println("Usage:");
      System.out.println(ArgumentsParser.getUsage(className, argList));
      return;
    }

    String sourceFile1 = sourceArg1.getValue();
    String schemaName1 = schemaArg1.getValue();
    String sourceFile2 = sourceArg2.getValue();
    String schemaName2 = schemaArg2.getValue();

    String logFile;
    if (logArg.getIsSet()) {
      logFile = logArg.getValue();
    }
    else {
      logFile = "diff.log";
    }

    // create logger
    Logger logger = LoggerFactory.createLogger(logFile);
    logger.info(logo + ArgumentsParser.LINE_SEPARATOR);
    StepDiff stepDiff = new StepDiff(logger);

    // check if files exist
    if (!(new File(sourceFile1).exists())) {
      logger.info("Specified source file does not exist: " + sourceFile1);
      return;
    }

    if (!(new File(sourceFile2).exists())) {
      logger.info("Specified source file does not exist: " + sourceFile2);
      return;
    }

    // init jsdai
    boolean success = false;
    boolean differ = false;

    try {
      if (success = stepDiff.beginSession(sourceFile1, schemaName1, sourceFile2, schemaName2)) {
        differ = stepDiff.compare();
      }
    }
    catch (SdaiException e) {
      logger.throwing(null, null, e);
      success = false;
    }

    // close session
    logger.info("Log file available: " + logFile);
    logger.info(differ ? "Schemas differ." : "Schemas are equal.");
    logger.info((success ? "Success" : "Failed") + ". Elapsed time: " + totalTime.stop());

    LoggerFactory.close(logger, logFile);
  }
}