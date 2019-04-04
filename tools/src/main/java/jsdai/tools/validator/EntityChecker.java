/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Aug 31, 2004 10:15:38 AM
 */
package jsdai.tools.validator;

import java.util.*;

import jsdai.lang.*;

import org.w3c.dom.*;

public abstract class EntityChecker
    implements ITestSuite {

  protected TestEnvironment env;
  protected List filters;

  public EntityChecker() {
  }

  public boolean init(TestEnvironment testEnvironment, Element props) {
    this.env = testEnvironment;
    filters = new LinkedList();

    NodeList nodes = props.getChildNodes();
    for (int i = 0, n = nodes.getLength(); i < n; i++) {
      if (!(nodes.item(i) instanceof Element)) {
        continue;
      }

      Element node = (Element) nodes.item(i);
      if (!node.getTagName().equals("Filter")) {
        continue;
      }

      boolean apply = Boolean.valueOf(node.getAttribute("apply")).booleanValue();
      if (!apply) {
        continue;
      }

      String className = node.getAttribute("class");
      try {
        EntityFilter entityFilter = (EntityFilter) Class.forName(className).newInstance();
        if (entityFilter.init(testEnvironment, node)) {
          filters.add(entityFilter);
        }
      }
      catch (Exception e) {
        testEnvironment.logger.throwing(null, null, e);
      }
    }

    return true;
  }

  protected boolean ignore(EEntity eEnt) {
    if (filters.size() == 0) {
      return false;
    }

    for (Iterator i = filters.iterator(); i.hasNext(); ) {
      EntityFilter filter = (EntityFilter) i.next();
      if (filter.ignore(eEnt)) {
        return true;
      }
    }

    return false;
  }
}