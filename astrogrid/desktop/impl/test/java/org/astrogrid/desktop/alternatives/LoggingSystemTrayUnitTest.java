/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import junit.framework.TestCase;

import org.astrogrid.acr.system.SystemTray;

/** tests the loggin system tray. not much to see here - just checking for npes
 * @author Noel Winstanley
 * @since Jun 6, 20061:37:52 PM
 */
public class LoggingSystemTrayUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		st = new LoggingSystemTray("TESTING");
	}
	
	protected SystemTray st;

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.LoggingSystemTray.displayErrorMessage(String, String)'
	 */
	public void testDisplayErrorMessage() {
		st.displayErrorMessage("a","b");
		st.displayErrorMessage(null,null);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.LoggingSystemTray.displayInfoMessage(String, String)'
	 */
	public void testDisplayInfoMessage() {
		st.displayInfoMessage("a","b");
		st.displayInfoMessage(null,null);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.LoggingSystemTray.displayWarningMessage(String, String)'
	 */
	public void testDisplayWarningMessage() {
		st.displayWarningMessage("a","b");
		st.displayWarningMessage(null,null);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.LoggingSystemTray.startThrobbing()'
	 */
	public void testStartThrobbing() {
		st.startThrobbing();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.LoggingSystemTray.stopThrobbing()'
	 */
	public void testStopThrobbing() {
		st.stopThrobbing();
	}

}
