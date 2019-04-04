/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Aug 31, 2004 9:48:47 AM
 */
package jsdai.tools.validator;

import org.w3c.dom.*;

import jsdai.lang.*;

public interface EntityFilter {
  public boolean init(TestEnvironment env, Element props);

  public boolean ignore(EEntity eEnt);
}
