/*
 * @author Viktoras Kovaliovas
 * Created on Oct 20, 2003 7:13:40 PM
 */
package jsdai.tools.validator;

import java.util.logging.*;

import org.w3c.dom.*;

import jsdai.dictionary.*;
import jsdai.lang.*;

public final class SchemaValidator
    extends EntityChecker {
  public SchemaValidator() {
  }

  public boolean init(TestEnvironment testEnvironment, Element props) {
    return super.init(testEnvironment, props);
  }

  public String getName() {
    return "Schema validator";
  }

  public String run(TestEnvironment testEnvironment) {
    try {
      int errors = 0;
      AEntity aEnt = testEnvironment.dataDomain.getInstances();
      for (SdaiIterator i = aEnt.createIterator(); i.next(); ) {
        EEntity eEnt = aEnt.getCurrentMemberEntity(i);
        if (!ignore(eEnt) && !validateInstance(testEnvironment.logger, eEnt)) {
          errors++;
        }
      }

      testEnvironment.logger.fine("no. of instances: " + aEnt.getMemberCount());
      testEnvironment.logger.fine("no. of erroneous instances: " + errors);

      return errors == 0 ? TEST_SUCCESS : TEST_FAILED;
    }
    catch (SdaiException e) {
      testEnvironment.logger.throwing(null, null, e);
    }

    return TEST_EXCEPTION;
  }

  private void listAttributes(Logger logger, AAttribute aAtt, String message)
      throws SdaiException {
    for (SdaiIterator i = aAtt.createIterator(); i.next(); ) {
      EAttribute eAtt = aAtt.getCurrentMember(i);
      logger.severe("attribute \"" + eAtt.getName(null) + "\" " + message);
    }
  }

  private boolean validateInstance(Logger logger, EEntity instance)
      throws SdaiException {
    AAttribute aAtt1 = new AAttribute();
    AAttribute aAtt2 = new AAttribute();
    AAttribute aAtt3 = new AAttribute();
    AAttribute aAtt4 = new AAttribute();
    AAttribute aAtt5 = new AAttribute();
    AAttribute aAtt6 = new AAttribute();
    AAttribute aAtt7 = new AAttribute();

    if ((!instance.validateRequiredExplicitAttributesAssigned(aAtt1))
        || (instance.validateExplicitAttributesReferences(aAtt2) == 1)
        || (instance.validateAggregatesSize(aAtt3) == 1)
        || (!instance.validateAggregatesUniqueness(aAtt4))
        || (!instance.validateArrayNotOptional(aAtt5))
        || (!instance.validateInverseAttributes(aAtt6))
        || (!instance.validateArrayNotOptional(aAtt7))) {
      logger.severe(instance.toString());
      listAttributes(logger, aAtt1, "value not set");
      listAttributes(logger, aAtt2, "reference to instance of an incorrect entity type");
      listAttributes(logger, aAtt3, "aggregate size does not meet constraints");
      listAttributes(logger, aAtt4, "aggregate uniqueness is not satisfied");
      listAttributes(logger, aAtt5, "aggregate has an optional instances");
      listAttributes(logger, aAtt6, "cardinality of inverse attribute is violated");
      listAttributes(logger, aAtt7, "array not optional failed");
      return false;
    }
    else {
      return true;
    }
  }
}
