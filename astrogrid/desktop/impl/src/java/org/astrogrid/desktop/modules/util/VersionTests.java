/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**  Self-tests to verify we've got the correct versions of Java.
 * 
 * 
 * at the moment it's only the Java version.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 16, 20089:19:27 AM
 */
public class VersionTests extends TestSuite {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(VersionTests.class);
    private static final float REQUIRED_JAVA_VERSION = 1.5f;
    /**
     * 
     */
    public VersionTests() {
        addTest(new TestCase("Java version") {
            @Override
            protected void runTest() throws Throwable {
                assertTrue("Java version is " + SystemUtils.JAVA_VERSION_FLOAT,
                        SystemUtils.isJavaVersionAtLeast(REQUIRED_JAVA_VERSION));
            }
        });
    }
    
    // for stand-alone development.
    public static Test suite() {
        return new VersionTests();
    }
}
