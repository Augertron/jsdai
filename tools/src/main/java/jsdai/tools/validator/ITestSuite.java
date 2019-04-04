/*
 * Organization: LKSoft Baltic
 * @author Viktoras Kovaliovas
 * Time: 2003.7.18 09.12.29
 */
package jsdai.tools.validator;

import org.w3c.dom.*;

public interface ITestSuite {
  /**
   * This return value of run method indicates that specified environment has not passed the test.
   */
  static final String TEST_FAILED = "Not passed";

  /**
   * This return value of run method indicates that specified environment has passed the test.
   */
  static final String TEST_SUCCESS = "Passed";

  /**
   * This return value of run method indicates that test has encountered some runtime problems.
   */
  static final String TEST_EXCEPTION = "Failed";

  /**
   * This method is called just after loading test suite. It should handle custom properties
   * of this suite defined in XML.
   *
   * @param testEnvironment specified test environment. Same environment should be specified in run method.
   * @param props           XML defined properties of this suite.
   * @return must return true if it succeeds, or false otherwise. If false is returned test is not executed.
   */
  boolean init(TestEnvironment testEnvironment, Element props);

  /**
   * This method must return human interpretable name of this test suite.
   *
   * @return human interpretable name of this test suite.
   */
  String getName();

  /**
   * Test suite should do all it's activity in this method.
   *
   * @param testEnvironment specified test environment.
   * @return test suite execution outcome. Should be one of enumerated values.
   */
  String run(TestEnvironment testEnvironment);
}