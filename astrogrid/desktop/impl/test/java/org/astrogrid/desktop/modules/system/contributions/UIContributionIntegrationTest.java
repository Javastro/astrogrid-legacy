/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.PreferencesIntegrationTest;

/** test that ui components are getting parsed correctly, 
 * and try to invoke one.
 * @author Noel Winstanley
 * @since Jan 10, 20071:58:57 AM
 */
public class UIContributionIntegrationTest extends InARTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		assertTrue(getHivemindRegistry().containsConfiguration("system.uiStructure"));
		contrib= getHivemindRegistry().getConfigurationAsMap("system.uiStructure");
	}
	Map contrib;
	protected void tearDown() throws Exception {
		super.tearDown();
		contrib = null;
	}

    public static Test suite() {
        return new ARTestSetup(new TestSuite(UIContributionIntegrationTest.class));
    }
	
    public void testContribution() throws Exception {
    	assertNotNull(contrib);
    	assertTrue(contrib.size() > 0);
    	System.out.println(contrib);
    }
    
	public void testMenu() throws Exception {
		UIMenuContribution m = (UIMenuContribution) contrib.get("debug");
		assertNotNull(m);
		assertEquals("debug",m.getName());
		assertEquals("Debug",m.getText());
		assertEquals("edit",m.getAfter());
		assertEquals("help",m.getBefore());
		assertNotNull(m.getVisibleCondition());
		assertEquals("acr.advanced",m.getVisibleCondition().getName());
	}
	
	public void testTab() throws Exception {
		UITabContribution m = (UITabContribution) contrib.get("discovery");
		assertNotNull(m);
		assertEquals("discovery",m.getName());
		assertEquals("Data Discovery",m.getText());
		assertEquals("*",m.getBefore());
		assertNull(m.getVisibleCondition());
	}

	public void testAction() throws Exception {
		UIActionContribution m = (UIActionContribution) contrib.get("astroscope");
		assertNotNull(m);
		assertEquals("astroscope",m.getName());
		assertEquals("AstroScope",m.getText());
		assertNull(m.getVisibleCondition());
	}
}
