/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.ConfigurationInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Integration test for preferences.
 * tests parsing of preferneces from contributions
 * @author Noel Winstanley
 * @since Jan 9, 20073:16:00 PM
 */
public class PreferencesIntegrationTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		ci = (ConfigurationInternal)getHivemindRegistry().getService(ConfigurationInternal.class);
		assertNotNull(ci);
	}

	ConfigurationInternal ci;
	
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		ci = null;
	}
	
    public void testPreferenceExists() throws Exception {
		Preference p = ci.find("system.webserver.port");
		assertNotNull(p);
		assertNotNull(p.getValue());
		assertNotNull(p.getDefaultValue());
		assertNotNull(p.getName());
		assertTrue(p.isAdvanced());
		assertFalse(p.isPropagateToConfig());
		assertTrue(p.isRequiresRestart());
		assertNotNull(p.getUiName());
		assertNotNull(p.getDescription());
		assertEquals("system",p.getModuleName());
		assertEquals(1,p.getAlternatives().length);
		assertEquals(0,p.getOptions().length);

	}
    
    public void testBooleanPreference() throws Exception {
		Preference p = ci.find("org.votech.plastic.notificationsenabled");
		assertNotNull(p);
		assertNotNull(p.getValue());
		assertNotNull(p.getDefaultValue());
		assertNotNull(p.getName());
		assertFalse(p.isAdvanced());
		assertFalse(p.isPropagateToConfig());
		assertFalse(p.isRequiresRestart());
		assertNotNull(p.getUiName());
		assertNotNull(p.getDescription());
		assertEquals("plastic",p.getModuleName());
		assertEquals(0,p.getAlternatives().length);
		assertEquals(0,p.getOptions().length);		
		assertEquals(Preference.BOOLEAN,p.getUnits());

	}
	
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(PreferencesIntegrationTest.class));
    }

}
