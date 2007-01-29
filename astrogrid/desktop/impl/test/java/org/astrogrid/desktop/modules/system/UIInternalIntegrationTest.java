/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Desparate attempt to test some functionality of the main UI.
 * limited on what I can do until work out how to drive UI.
 * exercises functions of the UIComponentImpl baseclass too.
 * @author Noel Winstanley
 * @since Jun 9, 20065:12:40 PM
 */
public class UIInternalIntegrationTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ui = (UIInternal)getHivemindRegistry().getService(UIInternal.class);
		assertNotNull(ui);
	}
	
	protected UIInternal ui;
	protected void tearDown() throws Exception {
		super.tearDown();
		ui = null;
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.UIInternal.getComponent()'
	 */
	public void testGetComponent() {
		if (! Boolean.getBoolean("asr.mode")) { // behaviour differs in asr mode.
			assertNotNull(ui.getComponent());
		} else {
		assertNull(ui.getComponent());
		}

	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.UIInternal.getExecutor()'
	 */
	public void testGetExecutor() {
		assertNotNull(ui.getExecutor());

	}

	// already tested in background executor.
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.UIInternal.wrap(Runnable)'
	 */
	//public void testWrap() {
//
//	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ui.UIComponent.setProgressMax(int)'
	 */
	public void testSetProgressMax() {
		int i = ui.getProgressMax();
		ui.setProgressMax(100);
		assertEquals(100,ui.getProgressMax());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ui.UIComponent.setProgressValue(int)'
	 */
	public void testSetProgressValue() {
		int i = ui.getProgressValue();
		ui.setProgressValue(i+1);
		assertEquals(i+1,ui.getProgressValue());
	}


	/*
	 * Test method for 'org.astrogrid.desktop.modules.ui.UIComponent.getUI()'
	 */
	public void testGetUI() {
		assertNotNull(ui.getUI()); // not same - as one is a hivemind proxy.
		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ui.UIComponent.getFrame()'
	 */
	public void testGetFrame() {
		if (! Boolean.getBoolean("asr.mode")) { // behaviour differs here..
			assertNotNull(ui.getFrame());
		} else {
			assertNull(ui.getFrame());
		}
	}
	
	
	public void testHide() {
		ui.hide();
	}
	
	public void testShow() {
		ui.show();
	}
	public void testSetBusy() {
		ui.setBusy(true);
		ui.setBusy(false);
	}
	public void testSetLoggedIn() {
		ui.setLoggedIn(true);
		ui.setLoggedIn(false);
	}
	public void testSetStatusMessage() {
		ui.setStatusMessage("test");
		ui.setStatusMessage(null);
	}
	
	public void dontTestShowError() {
		ui.showError("test",new Exception());
	}
	
	public void testThrobbing() {
		ui.startThrobbing();
		ui.stopThrobbing();
	}
	
	public static Test suite() {
		return new ARTestSetup(new TestSuite(UIInternalIntegrationTest.class));
	}

}
