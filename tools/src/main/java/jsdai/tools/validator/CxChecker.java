/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Jul 1, 2004 3:52:24 PM
 */
package jsdai.tools.validator;

import jsdai.dictionary.*;
import jsdai.lang.*;

import org.w3c.dom.*;

public class CxChecker
    implements ITestSuite {

  private Object ignoreParams;

  public CxChecker() {
  }

  public boolean init(TestEnvironment testEnvironment, Element props) {
    try {
      ignoreParams = EntityListParser.parseEntityList(testEnvironment.sourceSchema.getNativeSchema(), props);
    }
    catch (SdaiException e) {
      testEnvironment.logger.warning("Failed to read ignore list.");
      testEnvironment.logger.throwing(null, null, e);
    }

    return true;
  }

  public String getName() {
    return "Cx checker";
  }

  public String run(TestEnvironment testEnvironment) {
    try {
      int n = 0;

      if (testEnvironment.dataDomain.getMemberCount() > 1) {
        testEnvironment.logger.warning("multiple models not supported yet.");
      }

      ESchema_definition eSchema = testEnvironment.sourceSchema.getNativeSchema();
      String schemaName = eSchema.getName(null);
      String classPrefix = "jsdai.S" + schemaName.charAt(0) + schemaName.toLowerCase().substring(1) + ".Cx";

      AEntity aEnt = testEnvironment.dataDomain.getInstances();
      AEntityExtent aEe = testEnvironment.dataDomain.getByIndex(1).getPopulatedFolders();
      for (SdaiIterator i = aEe.createIterator(); i.next(); ) {
        EntityExtent eEe = aEe.getCurrentMember(i);
        EEntity_definition eEd = eEe.getDefinition();
        if (ignoreParams != null && EntityListParser.belongs(ignoreParams, eEd)) {
          continue;
        }

        if (testEnvironment.dataDomain.getExactInstanceCount(eEd) == 0) {
          continue;
        }

        String name = eEd.getName(null);
        SdaiModel model = eEd.findEntityInstanceSdaiModel();
        if (!model.getName().startsWith(schemaName)) {
          continue;
        }

        String className = classPrefix + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        className = className.replace('+', '$');
        try {
          Class.forName(className);
        }
        catch (ClassNotFoundException e) {
          testEnvironment.logger.severe("No Cx for " + name);
          n++;
        }
      }

      return n == 0 ? TEST_SUCCESS : TEST_FAILED;
    }
    catch (SdaiException e) {
      testEnvironment.logger.throwing(null, null, e);
    }

    return TEST_EXCEPTION;
  }
}