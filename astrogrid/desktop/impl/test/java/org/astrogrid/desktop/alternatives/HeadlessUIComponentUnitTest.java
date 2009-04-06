/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;

import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Test for the Headless UI alternative.
 * @author Noel Winstanley
 * @since Jun 6, 20061:52:51 PM
 */
public class HeadlessUIComponentUnitTest extends TestCase{

	

	@Override
    protected void setUp() {
		be = new InThreadExecutor();
		final UIContext cxt = createNiceMock(UIContext.class);
		expect(cxt.getExecutor()).andReturn(be);
		replay(cxt);		
		ui = new HeadlessUIComponent("test",cxt);
		
	}
	
	protected BackgroundExecutor be;
	protected HeadlessUIComponent ui;



	/*
	 * Test method for 'org.astrogrid.desktop.alternatives.HeadlessUI.addBackgroundWorker(BackgroundWorker)'
	 */
	public void testAddBackgroundWorker() {
	    final Runnable r = createMock(Runnable.class);
		replay(r);		
		final BackgroundWorker bw = new BackgroundWorker(ui,"test") {

			@Override
            protected Object construct() throws Exception {
				return null;
			}
		};
		ui.addBackgroundWorker(bw);
		ui.removeBackgroundWorker(bw);
		ui.removeBackgroundWorker(bw);
		verify(r);
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
	
	public void testGetComponent() throws Exception {
	    assertNull(ui.getComponent());
        
    }
	
	public void testGetTitile() throws Exception {
        assertNull(ui.getTitle());
    }
	
	public void testGetMainPanel() throws Exception {
        assertNull(ui.getMainPanel());
    }

	public void testProgeress() throws Exception {
        assertEquals(0,ui.getProgressValue());
        assertEquals(0,ui.getProgressMax());
        ui.setProgressValue(42);
        assertEquals(42,ui.getProgressValue());
        assertEquals(0,ui.getProgressMax());
        ui.setProgressValue(10);
        ui.setProgressMax(42);
        assertEquals(10,ui.getProgressValue());
        assertEquals(42,ui.getProgressMax());        
    }
}
