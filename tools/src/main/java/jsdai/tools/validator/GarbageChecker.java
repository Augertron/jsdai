/*
 * @author Viktoras Kovaliovas
 * Created on Oct 20, 2003 6:51:19 PM
 */
package jsdai.tools.validator;

import java.util.*;

import org.w3c.dom.*;

import jsdai.lang.*;

public final class GarbageChecker
    extends EntityChecker {

  public GarbageChecker() {
  }

  public boolean init(TestEnvironment testEnvironment, Element props) {
    return super.init(testEnvironment, props);
  }

  public String getName() {
    return "Garbage checker";
  }

  public String run(TestEnvironment testEnvironment) {
    try {
      Set unusedSet = new HashSet();

      AEntity aEnt = testEnvironment.dataDomain.getInstances();

      for (SdaiIterator i = aEnt.createIterator(); i.next(); ) {
        EEntity eEnt = aEnt.getCurrentMemberEntity(i);
        if (ignore(eEnt)) {
          continue;
        }

        AEntity aUsers = new AEntity();
        eEnt.findEntityInstanceUsers(testEnvironment.dataDomain, aUsers);
        if (0 == aUsers.getMemberCount()) {
          unusedSet.add(eEnt);
        }
      }

      Set sortedUnusedSet = new TreeSet(new Comparator() {
        public int compare(Object o1, Object o2) {
          EEntity e1 = (EEntity) o1;
          EEntity e2 = (EEntity) o2;

          int r = 0;
          try {
            r = e1.getInstanceType().getName(null).compareTo(e2.getInstanceType().getName(null));
          }
          catch (SdaiException e) {
            e.printStackTrace();
          }

          if (r == 0) {
            r = e1.toString().compareTo(e2.toString());
          }

          return r;
        }
      });
      sortedUnusedSet.addAll(unusedSet);

      for (Iterator i = sortedUnusedSet.iterator(); i.hasNext(); )
        testEnvironment.logger.severe(i.next().toString());

      int n = sortedUnusedSet.size();
      testEnvironment.logger.fine("Number of unused instances: " + n);
      return n == 0 ? TEST_SUCCESS : TEST_FAILED;
    }
    catch (SdaiException e) {
      testEnvironment.logger.throwing(null, null, e);
    }

    return TEST_EXCEPTION;
  }
}