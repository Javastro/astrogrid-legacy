package org.astrogrid.applications.integrationtest.harnesses;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.swingui.TestRunner;
import org.astrogrid.applications.integrationtest.cases.DefaultApplicationTest;

/**
 *
 * @author Guy Rixon
 */
public class SwingUiTestHarness {
  
  /**
   * Runs the test application.
   */
  public static void main(String[] args) {
    TestRunner.run(DefaultApplicationTest.class);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for CEA");
    suite.addTest(new DefaultApplicationTest());
    return suite;
  }
  
}
