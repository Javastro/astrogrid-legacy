/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Integration test for preferences.
 * tests parsing of preferneces from contributions
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
	
    public void testPreferenceExists() throws Exception {
		Preference p = ci.find("org.astrogrid.registry.query.endpoint");
		assertNotNull(p);
		assertNotNull(p.getValue());
		assertNotNull(p.getDefaultValue());
		assertNotNull(p.getName());
		assertTrue(p.isAdvanced());
		assertTrue(p.isPropagateToConfig());
		assertTrue(p.isRequiresRestart());
		assertNotNull(p.getUiName());
		assertNotNull(p.getDescription());
	}
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(PreferencesIntegrationTest.class));
    }

}
