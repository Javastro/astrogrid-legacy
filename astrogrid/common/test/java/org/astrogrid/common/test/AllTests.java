/*
 * Created on 27-Jan-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.astrogrid.common.test;

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
        TestSuite suite = new TestSuite("Test for org.astrogrid.common.test");
        //$JUnit-BEGIN$
        suite.addTest(ConfiguratorTestSuite.suite());
        suite.addTest(MessageTestSuite.suite());
        suite.addTest(TemplateTestSuite.suite());
        //$JUnit-END$
        return suite;
    }
}
