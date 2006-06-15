/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.BackgroundExecutorImpl;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.easymock.MockControl;

import junit.framework.TestCase;

/** Test for the Headless UI alternative.
 * @author Noel Winstanley
 * @since Jun 6, 20061:52:51 PM
 */
public class HeadlessUIUnitTest extends TestCase{

	

	protected void setUp() {
		fac = new HeadlessUIFactory();
		be = fac.getExecutor();
		ui = fac.getUI();
	}
	
	protected HeadlessUIFactory fac;
	protected BackgroundExecutorImpl be;
	protected UIInternal ui;

	
	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.getComponent()'
	 */
	public void testGetComponent() {
		assertNull(ui.getComponent());
	}

	/* returned executor will be the factory, as proxyies the real executor
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.getExecutor()'
	 */
	public void testGetExecutor() {
		assertEquals(fac,ui.getExecutor());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.wrap(Runnable)'
	 */
	public void testWrap() throws InterruptedException {
		MockControl rControl = MockControl.createControl(Runnable.class);
		Runnable r = (Runnable)rControl.getMock();
		r.run();
		rControl.replay();
		BackgroundWorker bw = ui.wrap(r);
		assertNotNull(bw);
		bw.start();
		// need to wait a moment.
		Thread.currentThread().sleep(1000);
		rControl.verify();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.hide()'
	 */
	public void testHide() {
		ui.hide();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.setLoggedIn(boolean)'
	 */
	public void testSetLoggedIn() {
		ui.setLoggedIn(true);
		ui.setLoggedIn(false);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.show()'
	 */
	public void testShow() {
		ui.show();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.startThrobbing()'
	 */
	public void testStartThrobbing() {
		ui.startThrobbing();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.stopThrobbing()'
	 */
	public void testStopThrobbing() {
		ui.stopThrobbing();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.addBackgroundWorker(BackgroundWorker)'
	 */
	public void testAddBackgroundWorker() {
		MockControl rControl = MockControl.createControl(Runnable.class);
		Runnable r = (Runnable)rControl.getMock();
		rControl.replay();		
		BackgroundWorker bw = ui.wrap(r);
		ui.addBackgroundWorker(bw);
		ui.removeBackgroundWorker(bw);
		ui.removeBackgroundWorker(bw);
		rControl.verify();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.getFrame()'
	 */
	public void testGetFrame() {
		assertNull(ui.getFrame());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.getProgressMax()'
	 */
	public void testProgressMax() {
		assertEquals(0,ui.getProgressMax());
		ui.setProgressMax(100);
		assertEquals(100,ui.getProgressMax());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.getProgressValue()'
	 */
	public void testProgressValue() {
		assertEquals(0,ui.getProgressValue());
		ui.setProgressValue(100);
		assertEquals(100,ui.getProgressValue());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.getUI()'
	 */
	public void testGetUI() {
		assertEquals(ui,ui.getUI());
	}


	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.setBusy(boolean)'
	 */
	public void testSetBusy() {
		ui.setBusy(true);
		ui.setBusy(false);
	}

	

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.setStatusMessage(String)'
	 */
	public void testSetStatusMessage() {
		ui.setStatusMessage("test");
		ui.setStatusMessage(null);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.showError(String, Throwable)'
	 */
	public void testShowError() {
		ui.showError("Error",new Exception());
		ui.showError("Error",null);
		ui.showError(null,null);
	}

}
