package org.astrogrid.config;


import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests SimpleConfig class.  As this is a static singleton wrapped around
 * PropertyConfig, it also exercises PropertyConfig
 */

public class SimpleTest extends TestCase
{
//@TODO restore me - temporary delete
   /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(SimpleTest.class);
    }
}
