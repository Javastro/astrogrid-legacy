/*
 * Created on 27-Jan-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author john
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * TODO Change This Javadoc Comment
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(AstroGridExceptionTest.class));
        //suite.addTest(new TestSuite(ConfiguratorTest.class));
      suite.addTest(org.astrogrid.common.test.AllTests.suite());
      suite.addTest(org.astrogrid.config.AllTests.suite());
      suite.addTest(org.astrogrid.community.AllTests.suite());
      suite.addTest(org.astrogrid.test.AllTests.suite());
      suite.addTest(org.astrogrid.testutils.naming.AllTests.suite());
      suite.addTest(org.astrogrid.util.AllTests.suite());
      
        //$JUnit-END$
        return suite;
    }
}
