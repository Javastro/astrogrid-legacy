/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Integration test for preferences.
 * @author Noel Winstanley
 * @since Jan 9, 20073:16:00 PM
 */
public class PreferencesIntegrationTest extends InARTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		ci = (ConfigurationInternal)getHivemindRegistry().getService(ConfigurationInternal.class);
		assertNotNull(ci);
	}

	ConfigurationInternal ci;
	
	protected void tearDown() throws Exception {
		super.tearDown();
		ci = null;
	}
	
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(PreferencesIntegrationTest.class));
    }

}
