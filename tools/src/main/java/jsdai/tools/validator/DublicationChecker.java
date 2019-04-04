/*
 * @author Viktoras Kovaliovas
 * Created on Oct 20, 2003 5:14:44 PM
 */
package jsdai.tools.validator;

import java.util.*;

import org.w3c.dom.*;

import jsdai.lang.*;

public final class DublicationChecker
    extends EntityChecker {

  public DublicationChecker() {
  }

  public boolean init(TestEnvironment testEnvironment, Element props) {
    return super.init(testEnvironment, props);
  }

  public String getName() {
    return "Dublication checker";
  }

  public String run(TestEnvironment testEnvironment) {
    try {
      Map printMap = new HashMap();

      AEntity aEnt = testEnvironment.dataDomain.getInstances();
      for (SdaiIterator i = aEnt.createIterator(); i.next(); ) {
        EEntity eEnt = aEnt.getCurrentMemberEntity(i);
        if (ignore(eEnt)) {
          continue;
        }

        String s = eEnt.toString();

        s = s.substring(s.indexOf('=') + 1);
        int n[] = (int[]) printMap.get(s);
        if (n == null) {
          printMap.put(s, new int[] { 1 });
        }
        else {
          n[0]++;
        }
      }

      SortedMap sortedMap = new TreeMap(printMap);

      int errors = 0;
      for (Iterator i = sortedMap.keySet().iterator(); i.hasNext(); ) {
        String s = (String) i.next();
        int n = ((int[]) sortedMap.get(s))[0];
        if (n > 1) {
          testEnvironment.logger.severe("n = " + n + "; " + s);
          errors++;
        }
      }

      testEnvironment.logger.fine("Number of dublication errors: " + errors);
      return errors == 0 ? TEST_SUCCESS : TEST_FAILED;
    }
    catch (SdaiException e) {
      testEnvironment.logger.throwing(null, null, e);
    }

    return TEST_EXCEPTION;
  }
}