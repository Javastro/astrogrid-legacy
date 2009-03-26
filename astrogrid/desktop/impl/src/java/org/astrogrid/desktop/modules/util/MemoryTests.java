/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.io.FileUtils;

/** Self tests for sufficient memory to operate in.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 12, 200812:16:01 PM
 */
public class MemoryTests extends TestSuite {
    private static final long RECOMMENDED_MB =  512L;
    

    public MemoryTests() {
        addTest(new TestCase("Available Memory") {
            @Override
            protected void runTest() throws Throwable {
                final long maxMemory = Runtime.getRuntime().maxMemory();
                
                assertTrue("Available memory " + (maxMemory /  FileUtils.ONE_MB) 
                        + "MB is less than minimum recommended " + RECOMMENDED_MB + "MB"
                        ,maxMemory >= RECOMMENDED_MB * FileUtils.ONE_MB * 0.95
                        );
            }
        });
    }
}
