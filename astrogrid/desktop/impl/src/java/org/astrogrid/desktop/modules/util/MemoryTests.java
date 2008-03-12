/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.io.FileUtils;

/** Check we've got enough memory to operate in.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 12, 200812:16:01 PM
 */
public class MemoryTests extends TestSuite {
    private static final long RECOMMENDED_MB =  1024L;
    

    public MemoryTests() {
        addTest(new TestCase("Available Memory") {
            protected void runTest() throws Throwable {
                long maxMemory = Runtime.getRuntime().maxMemory();
                System.err.println(maxMemory);
                
                assertTrue("Available memory " + (maxMemory /  FileUtils.ONE_MB) 
                        + "MB is less than recommended " + RECOMMENDED_MB + "MB"
                        ,maxMemory >= RECOMMENDED_MB * FileUtils.ONE_MB
                        );
            }
        });
    }
}
