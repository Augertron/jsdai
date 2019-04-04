/*
 * Organization: LKSoft Baltic
 * @author Viktoras Kovaliovas
 * Time: 2002.10.18 14.42.39
 */
package jsdai.tools.validator;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import jsdai.lang.*;

import com.lksoft.util.*;
import com.lksoft.util.logging.*;

public class TestEnvironment {
  public static final String VERSION = "Step file validator v1.0 alpha";
  public static final String DATE = "2003.10.20";
  public static final String COPYRIGHT = "2003";

  public static final String ARG_SOURCE = "-s";
  public static final String ARG_PROP = "-p";
  public static final String ARG_SCHEMA = "-schema";
  public static final String ARG_LOG = "-l";

  private SdaiSession session;
  //	private SdaiTransaction trans;
  private SdaiRepository repo;

  /**
   * Name of source file.
   */
  public String stepFileName;

  /**
   * Source schema of environment.
   */
  public SchemaInstance sourceSchema;

  /**
   * Source data domain of environment.
   */
  public ASdaiModel dataDomain;

  /**
   * Logger used in environment. All test suite's output should be done using this logger.
   */
  public Logger logger;

  public TestEnvironment(Logger logger) {
    this.logger = logger;
  }

  public String beginSession(String p21File, String schemaName)
      throws SdaiException {
    stepFileName = p21File;

    // open step file for reading
    session = SdaiSession.openSession();
//		trans = session.startTransactionReadWriteAccess();
    session.startTransactionReadWriteAccess();
    repo = session.importClearTextEncoding("", p21File, null);

    // extract sdai models built on working schema. Build target domain
    ASchemaInstance aSchemas = repo.getSchemas();
    if (schemaName == null) {
      if (aSchemas.getMemberCount() == 0) {
        logger.fine("Specified repository has no schema instances.");
        return null;
      }

      sourceSchema = aSchemas.getByIndex(1);
    }
    else {
      for (SdaiIterator i = aSchemas.createIterator(); i.next(); ) {
        SchemaInstance schema = aSchemas.getCurrentMember(i);
        if (schema.getName().equals(schemaName)) {
          sourceSchema = schema;
          break;
        }
      }

      if (sourceSchema == null) {
        logger.fine("There is no schema instance named <" + schemaName + "> in specified repository.");
        return null;
      }
    }

    dataDomain = sourceSchema.getAssociatedModels();
    if (dataDomain.getMemberCount() == 0) {
      logger.fine("Specified schema has no models assciated with it.");
      return null;
    }

    SdaiContext context = new SdaiContext();
    context.domain = dataDomain;
    context.schema = sourceSchema.getNativeSchema();
    session.setSdaiContext(context);

    return sourceSchema.getName();
  }

  public void endSession()
      throws SdaiException {
//		trans.endTransactionAccessAbort();
//		repo.closeRepository();
//		repo.deleteRepository();
//		session.closeSession();
  }

  public void run(String propFile) {
    try {
      List testSuites = new LinkedList();

      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(propFile);
      Element elem = doc.getDocumentElement();

      NodeList nl = elem.getChildNodes();
      for (int i = 0, n = nl.getLength(); i < n; i++) {
        if (!(nl.item(i) instanceof Element)) {
          continue;
        }

        Element node = (Element) nl.item(i);
        boolean run = Boolean.valueOf(node.getAttribute("run")).booleanValue();
        if (!run) {
          continue;
        }

        String className = node.getAttribute("class");
        try {
          ITestSuite testSuite = (ITestSuite) Class.forName(className).newInstance();
          if (testSuite.init(this, node)) {
            testSuites.add(testSuite);
          }
        }
        catch (ClassNotFoundException e) {
          logger.throwing(null, null, e);
        }
        catch (ClassCastException e) {
          logger.throwing(null, null, e);
        }
      }

      run(testSuites);
    }
    catch (Exception e) {
      logger.throwing(null, null, e);
    }
  }

  public void run(List testSuites) {
    // run tests
    for (ListIterator i = testSuites.listIterator(); i.hasNext(); ) {
      ElapsedTime time = new ElapsedTime();
      ITestSuite testSuite = (ITestSuite) i.next();
      logger.info("Test suite: " + testSuite.getName());
      try {
        String r = testSuite.run(this);
        logger.info(r + ". Elapsed time: " + time.stop());
      }
      catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String args[])
      throws SdaiException {
    ElapsedTime totalTime = new ElapsedTime();
    String className = TestEnvironment.class.getName();

    // print logo
    String logo = ArgumentsParser.getLogo(VERSION, DATE, COPYRIGHT);

    // parse command line args
    ArgumentsParser.Value sourceArg = new ArgumentsParser.Value(ARG_SOURCE, true);
    sourceArg.setValueName("sourceFile");
    sourceArg.setDescription("STEP source file name.");

    ArgumentsParser.Value propArg = new ArgumentsParser.Value(ARG_PROP, true);
    propArg.setValueName("propFile");
    propArg.setDescription("Properties file.");

    ArgumentsParser.Value schemaArg = new ArgumentsParser.Value(ARG_SCHEMA, false);
    schemaArg.setValueName("schemaName");
    schemaArg.setDescription("Name of schema that will be tested. If not specified, any one schema is tested.");

    ArgumentsParser.Value logArg = new ArgumentsParser.Value(ARG_LOG, false);
    logArg.setValueName("logFile");
    logArg.setDescription("File for logging. If not specified, the same name as of source file is used with .log extension.");

    List argList = Arrays.asList(new Object[] { sourceArg, propArg, schemaArg, logArg });
    if (!ArgumentsParser.parse(args, argList)) {
      System.out.println(logo);
      System.out.println();
      System.out.println("Usage:");
      System.out.println(ArgumentsParser.getUsage(className, argList));
      return;
    }

    String sourceFile = sourceArg.getValue();
    String propFile = propArg.getValue();

    String logFile;
    if (logArg.getIsSet()) {
      logFile = logArg.getValue();
    }
    else {
      logFile = sourceFile + ".log";
    }

    String schemaName = schemaArg.getValue();

    // create logger
    Logger logger = LoggerFactory.createLogger(className, logFile);
    logger.info(logo + ArgumentsParser.LINE_SEPARATOR);
    TestEnvironment testEnvironment = new TestEnvironment(logger);

    // check if files exist
    if (!(new File(sourceFile).exists())) {
      logger.info("Specified source file does not exist: " + sourceFile);
      return;
    }

    if (!(new File(propFile).exists())) {
      logger.info("Specified properties file does not exist: " + propFile);
      return;
    }

    // init jsdai
    ElapsedTime time = new ElapsedTime();
    logger.info("Reading: " + sourceFile);
    if (null != (schemaName = testEnvironment.beginSession(sourceFile, schemaArg.getValue()))) {
      logger.info("Schema \"" + schemaName + "\" has been read. Elapsed time: " + time.stop());
      logger.info("Properties file: " + propFile);
      testEnvironment.run(propFile);
    }

    // close session
    time = new ElapsedTime();
    logger.info("Closing session...");
    testEnvironment.endSession();
    logger.info("Session closed. Elapsed time: " + time.stop());
    logger.info("Log file available: " + logFile);
    logger.info((schemaName == null ? "Failed" : "Success") + ". Elapsed time: " + totalTime.stop());

    LoggerFactory.close(logger, logFile);
  }
}