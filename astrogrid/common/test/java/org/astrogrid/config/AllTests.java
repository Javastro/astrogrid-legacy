/*
 * Created on 27-Jan-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.astrogrid.config;

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
        TestSuite suite = new TestSuite("Test for org.astrogrid.config");
        //$JUnit-BEGIN$
        suite.addTest(FactoryTest.suite());
        suite.addTest(SimpleTest.suite());
        suite.addTest(XmlTest.suite());
        suite.addTest(AttomTest.suite());
        //$JUnit-END$
        return suite;
    }
}
