/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.jwebunit.WebTestCase;

import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.modules.system.ConfigurationInternal;

/** Integration test that exercises the preferences html interace.
 * @author Noel Winstanley
 * @since Jan 9, 20077:07:05 PM
 */
public class PreferenceEditorServletIntegrationTest	 extends WebTestCase {

	/**
	 * 
	 */
	private static final String TEST_OPTION_PREFERENCE_KEY = "acr.advanced";
	private static final String TEST_PREFERENCE_KEY = "system.webserver.port";
	protected void setUp() throws Exception {
		super.setUp();
		serv = (WebServer)ARTestSetup.fixture.getACR().getService(WebServer.class);
		assertNotNull(serv);
		conf = (ConfigurationInternal)ARTestSetup.fixture.getHivemindRegistry().getService(ConfigurationInternal.class);
		assertNotNull(conf);
		
		pref = conf.find(TEST_PREFERENCE_KEY);
		assertNotNull(pref);
		
		optionPref = conf.find(TEST_OPTION_PREFERENCE_KEY);
		assertNotNull(optionPref);
        getTestContext().setBaseUrl(serv.getUrlRoot());    
		
	}
	protected WebServer serv;
	protected ConfigurationInternal conf;
	protected Preference pref;
	protected Preference optionPref;
	protected void tearDown() throws Exception {
		super.tearDown();
		serv = null;
		conf = null;
		pref = null;
		optionPref = null;
	}
	
	public void testLinks() {
		beginAt("/");
		assertLinkPresent("Preferences");
		clickLink("Preferences");
		assertLinkPresentWithText("up");
		clickLinkWithText("up");
	}
	
	public void testInputPreference() {
		beginAt("/");
        assertLinkPresent("Preferences");
        clickLink("Preferences");
		
		assertFormPresent(TEST_PREFERENCE_KEY);
		setWorkingForm(TEST_PREFERENCE_KEY);
		assertFormElementEquals(TEST_PREFERENCE_KEY,pref.getValue());
		final Watcher watcher = new Watcher();
		pref.addPropertyChangeListener(watcher);
		
		final String orig = pref.getValue();
		
		setFormElement(TEST_PREFERENCE_KEY, "foo");
		submit();
		// new page.
		assertFormPresent(TEST_PREFERENCE_KEY);
		assertFormElementEquals(TEST_PREFERENCE_KEY,pref.getValue());
		
		assertEquals("preference object not changed","foo",pref.getValue());
		assertTrue("no notification",watcher.seen);
		
		// finally, reset back to original value.
		pref.setValue(orig);
	}
	
	/* @fixme further testing  - however, this prference is not an option anymore.
	public void testOptionPreference() {
		beginAt("/");
		assertLinkPresentWithText("Preferences");
		clickLinkWithText("Preferences");
		
		assertFormPresent(TEST_OPTION_PREFERENCE_KEY);
		setWorkingForm(TEST_OPTION_PREFERENCE_KEY);
		assertOptionEquals(TEST_OPTION_PREFERENCE_KEY,optionPref.getValue());
		
		Watcher watcher = new Watcher();
		optionPref.addPropertyChangeListener(watcher);
		
		String orig = optionPref.getValue();		 
		String nuVal = Boolean.toString(! ((new Boolean(orig)).booleanValue()));
	//	1.5 only.String nuVal = Boolean.toString(! Boolean.parseBoolean(orig));
		
		selectOption(TEST_OPTION_PREFERENCE_KEY, nuVal);
		submit();

		assertFormPresent(TEST_OPTION_PREFERENCE_KEY);
		setWorkingForm(TEST_OPTION_PREFERENCE_KEY);
		assertOptionEquals(TEST_OPTION_PREFERENCE_KEY,optionPref.getValue());

		assertEquals("preference object not changed",nuVal,optionPref.getValue());
		assertTrue("no notification",watcher.seen);
		
		// finally, reset back to original value.
		optionPref.setValue(orig);
	}
	*/
	
	class Watcher implements PropertyChangeListener {
		boolean seen;
		public void propertyChange(final PropertyChangeEvent evt) {
			seen = true;
		}
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(PreferenceEditorServletIntegrationTest.class));
    }

}
