package org.astrogrid.config;


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Abstract JUnit test case for ConfigurableTest
 * - extend to test implementations of Configurable.
 */

public class FactoryTest extends TestCase {
//@TODO restore me - temporary delete
	/**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(FactoryTest.class);
    }
}
