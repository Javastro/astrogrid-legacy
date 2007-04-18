/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.lang.reflect.InvocationTargetException;

import javax.swing.ButtonModel;
import javax.swing.SwingUtilities;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.easymock.MockControl;

import ca.odell.glazedlists.EventList;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 11, 200712:51:58 AM
 */
public class UIContextImplUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		conf = (Configuration)MockControl.createNiceControl(Configuration.class).getMock();
		exec = (BackgroundExecutor)MockControl.createNiceControl(BackgroundExecutor.class).getMock();
		help = (HelpServerInternal)MockControl.createNiceControl(HelpServerInternal.class).getMock();
		browser = (BrowserControl)MockControl.createNiceControl(BrowserControl.class).getMock();
		cxt = new UIContextImpl(conf,exec,help,browser);
	}

	private Configuration conf;
	private BackgroundExecutor exec;
	private HelpServerInternal help;
	private BrowserControl browser;
	private UIContext cxt;
	
	protected void tearDown() throws Exception {
		super.tearDown();
		cxt = null;
		conf = null;
		exec = null;
		help = null;
		browser = null;
	}

	public void testGetConfiguration() {
		assertSame(conf,cxt.getConfiguration());
	}

	public void testGetHelpServer() {
		assertSame(help,cxt.getHelpServer());
	}

	public void testGetExecutor() {
		assertSame(exec,cxt.getExecutor());
	}

	public void testGetBrowser() {
		assertSame(browser,cxt.getBrowser());
	}

	public void testGetLoggedInModel() {
		ButtonModel model = cxt.getLoggedInModel();
		edtWait();
		assertFalse(model.isEnabled());
		cxt.setLoggedIn(true);
		edtWait();
		assertTrue(model.isEnabled());
		cxt.setLoggedIn(false);
		edtWait();
		assertFalse(model.isEnabled());
	}

	public void testGetThrobbingModel() {
		ButtonModel model = cxt.getThrobbingModel();
		assertFalse(model.isEnabled());
		cxt.startThrobbing();
		edtWait();
		assertTrue(model.isEnabled());

		// verify it nests.
		cxt.startThrobbing();
		edtWait();
		assertTrue(model.isEnabled());
		
		cxt.stopThrobbing();
		edtWait();
		assertTrue(model.isEnabled());		
		
		cxt.stopThrobbing();
		edtWait();
		assertFalse(model.isEnabled());		
	}

	public void testGetVisibleModel() {
		ButtonModel visibleModel = cxt.getVisibleModel();
		assertFalse(visibleModel.isEnabled());
		cxt.show();
		edtWait();
		assertTrue(visibleModel.isEnabled());
		cxt.hide();
		edtWait();
		assertFalse(visibleModel.isEnabled());
	}

	
	public void testGetVisibleModelWithWindows() {
		MockControl w1Mock = MockControl.createControl(UIComponent.class);
		UIComponent w1 = (UIComponent)w1Mock.getMock();
		w1.setVisible(true);
		w1Mock.setVoidCallable(1);
		w1.setVisible(false);
		w1Mock.setVoidCallable(1);
		w1Mock.replay();
		
		cxt.registerWindow(w1);
		ButtonModel visibleModel = cxt.getVisibleModel();
		assertFalse(visibleModel.isEnabled());
		cxt.show();
		edtWait();
		assertTrue(visibleModel.isEnabled());
		cxt.hide();
		edtWait();
		assertFalse(visibleModel.isEnabled());
		
		w1Mock.verify();
	}	
	
	// twst we're getting a view on the model - and we can't change it ourselves.
//	public void testVisibleModelImmutable() {
//	ButtonModel visibleModel = cxt.getVisibleModel();
//	assertFalse(visibleModel.isSelected());	
//	visibleModel.setSelected(true);
//	assertFalse("mutated the model",visibleModel.isSelected());
//	}
	public void testStatusMessageWithNoWindow() throws Exception {
	
		cxt.setStatusMessage("foo");
		// just check it doesn't crap out.
	}
	public void testStatusMessageWithWindow() throws Exception {
		MockControl w1Mock = MockControl.createControl(UIComponent.class);
		UIComponent w1 = (UIComponent)w1Mock.getMock();
		w1.setStatusMessage("foo");
		w1Mock.setVoidCallable(1);
		w1Mock.replay();
		cxt.registerWindow(w1);
		assertSame(w1,cxt.findMainWindow());
		
		cxt.setStatusMessage("foo");
		edtWait();
		w1Mock.verify();
	}

	// test that status only goes to the first in the list.
	public void testStatusMessageWithWindows() throws Exception {
		MockControl w1Mock = MockControl.createControl(UIComponent.class);
		UIComponent w1 = (UIComponent)w1Mock.getMock();
		w1.setStatusMessage("foo");
		w1Mock.setVoidCallable(1);
		w1Mock.replay();
		cxt.registerWindow(w1);
		
		MockControl w2Mock = MockControl.createControl(UIComponent.class);
		UIComponent w2 = (UIComponent)w1Mock.getMock();		
		w2Mock.replay();
		cxt.registerWindow(w2);
		
		cxt.setStatusMessage("foo");
		edtWait();
		
		w1Mock.verify();
		w2Mock.verify();
	}	
	
	public void testGetWindowList() {
		assertEquals(0,cxt.getWindowList().size());
		UIComponent comp = (UIComponent)MockControl.createNiceControl(UIComponent.class).getMock();
		cxt.registerWindow(comp);
		assertEquals(1,cxt.getWindowList().size());
		assertSame(comp,cxt.getWindowList().get(0));
		
		// doublle registrations ignored.
		cxt.registerWindow(comp);
		assertEquals(1,cxt.getWindowList().size());
		
		cxt.unregisterWindow(comp);
		assertEquals(0,cxt.getWindowList().size());
	}
	
	public void testWindowListImmutable() throws Exception {
		EventList windowList = cxt.getWindowList();
		assertEquals(0,windowList.size());
		try {
			windowList.add(new Object());
			fail("expected to chuck");
		} catch (UnsupportedOperationException e) {
		}
		assertEquals("mutated window list",0,windowList.size());		
	}
	
	public void testFindMainWindow() throws Exception {

		assertEquals(0,cxt.getWindowList().size());
		UIComponent found = cxt.findMainWindow();
		assertNotNull(found);
		assertTrue(found instanceof HeadlessUIComponent);
		// register a real window.
		UIComponent comp = (UIComponent)MockControl.createNiceControl(UIComponent.class).getMock();
		cxt.registerWindow(comp);
		assertEquals(1,cxt.getWindowList().size());		
		found = cxt.findMainWindow();
		assertNotNull(found);
		assertSame(comp,found);
		
		// when the windows are removed, back to the stop-gap.
		cxt.unregisterWindow(comp);
		assertEquals(0,cxt.getWindowList().size());
		found = cxt.findMainWindow();
		assertNotNull(found);
		assertTrue(found instanceof HeadlessUIComponent);			
	}

	//block to give time for edt to work through..
	private void edtWait() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
				}
			});
		} catch (InterruptedException x) {
			fail(x.getMessage());
		} catch (InvocationTargetException x) {
			fail(x.getMessage());
		}
	}
}
