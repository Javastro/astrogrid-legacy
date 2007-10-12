/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import java.awt.Component;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.easymock.MockControl;

/** Test for the Headless UI alternative.
 * @author Noel Winstanley
 * @since Jun 6, 20061:52:51 PM
 */
public class HeadlessUIComponentUnitTest extends TestCase{

	

	protected void setUp() {
		be = new InThreadExecutor();
		MockControl m = MockControl.createNiceControl(UIContext.class);
		UIContext cxt= (UIContext)m.getMock();
		cxt.getExecutor();
		m.setDefaultReturnValue(be);
		m.replay();		
		ui = new HeadlessUIComponent("test",cxt);
	}
	
	protected BackgroundExecutor be;
	protected HeadlessUIComponent ui;



	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.addBackgroundWorker(BackgroundWorker)'
	 */
	public void testAddBackgroundWorker() {
		MockControl rControl = MockControl.createControl(Runnable.class);
		Runnable r = (Runnable)rControl.getMock();
		rControl.replay();		
		BackgroundWorker bw = new BackgroundWorker(ui,"test") {

			protected Object construct() throws Exception {
				return null;
			}
		};
		ui.addBackgroundWorker(bw);
		ui.removeBackgroundWorker(bw);
		ui.removeBackgroundWorker(bw);
		rControl.verify();
	}

	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.getFrame()'
	 */
	public void testGetFrame() {
		assertNull(ui.getComponent());
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
