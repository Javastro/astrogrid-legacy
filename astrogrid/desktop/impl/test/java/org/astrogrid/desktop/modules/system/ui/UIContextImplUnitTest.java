/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import static org.easymock.EasyMock.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonModel;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.CacheFactory;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.util.SelfTester;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 11, 200712:51:58 AM
 */
public class UIContextImplUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		conf = createNiceMock(Configuration.class);
		exec = createNiceMock(BackgroundExecutor.class);
		help =createNiceMock(HelpServerInternal.class);
		browser = createNiceMock(BrowserControl.class);
		
		monitor = createNiceMock(BackgroundWorkersMonitor.class);
        config = createNiceMock(Runnable.class); // expect these in test.
        about = createNiceMock(Runnable.class);
        windowFactories = new HashMap();       
        cxt = new UIContextImpl(conf,exec,help,browser
		        , createNiceMock(CacheFactory.class)
		        ,createNiceMock(CommunityInternal.class)
		        ,createNiceMock(org.astrogrid.acr.builtin.Shutdown.class)
		        ,createNiceMock(SelfTester.class)
		        ,monitor
		        ,config
		        ,about
		        ,windowFactories
		        ,"test"
		        ,null
		);
	}

	private Configuration conf;
	private BackgroundExecutor exec;
	private HelpServerInternal help;
	private BrowserControl browser;
	private UIContext cxt;
    private BackgroundWorkersMonitor monitor;
    private Runnable config;
    private Runnable about;
    private HashMap windowFactories;
    private BasicEventList plasticList;
	
	@Override
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
	
	public void testGetTasksList() throws Exception {
        assertNotNull(cxt.getTasksList());
        assertTrue(cxt.getTasksList().isEmpty());
    }

	   
	public void testGetBackgroundWorkersMonitor() throws Exception {
        assertNotNull(cxt.getWorkersMonitor());
        assertSame(monitor,cxt.getWorkersMonitor());
    }
	
    public void testGetPlasticList() throws Exception {
        assertSame(plasticList,cxt.getPlasticList());
    }
    
    
	
	public void testWindowFactories() throws Exception {
	    final Map map = cxt.getWindowFactories();
	    assertNotNull(map);
	    assertTrue(map.isEmpty());
    }
	
	public void testShowAbout() throws Exception {
        about.run();
        replay(about);
        cxt.showAboutDialog();
        verify(about);
    }
	
	   public void testShowPreferences() throws Exception {
	        config.run();
	        replay(config);
	        cxt.showPreferencesDialog();
	        verify(config);
	    }

	public void testGetLoggedInModel() {
		final ButtonModel model = cxt.getLoggedInModel();
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
		final ButtonModel model = cxt.getThrobbingModel();
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
		final ButtonModel visibleModel = cxt.getVisibleModel();
		assertFalse(visibleModel.isEnabled());
		cxt.show();
		edtWait();
		assertTrue(visibleModel.isEnabled());
		cxt.hide();
		edtWait();
		assertFalse(visibleModel.isEnabled());
	}

	
	public void testGetVisibleModelWithWindows() {
		final UIComponent w1 = createMock(UIComponent.class);
		w1.setVisible(true);
		w1.setVisible(false);
		replay(w1);
		
		cxt.registerWindow(w1);
		final ButtonModel visibleModel = cxt.getVisibleModel();
		assertFalse(visibleModel.isEnabled());
		cxt.show();
		edtWait();
		assertTrue(visibleModel.isEnabled());
		cxt.hide();
		edtWait();
		assertFalse(visibleModel.isEnabled());
		
		verify(w1);
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
        final UIComponent w1 = createMock(UIComponent.class);
		w1.setStatusMessage("foo");
		replay(w1);
		cxt.registerWindow(w1);
		assertSame(w1,cxt.findMainWindow());
		
		cxt.setStatusMessage("foo");
		edtWait();
		verify(w1);
	}

	// test that status only goes to the first in the list.
	public void testStatusMessageWithWindows() throws Exception {

	      final UIComponent w1 = createMock(UIComponent.class);
		w1.setStatusMessage("foo");
		cxt.registerWindow(w1);
        final UIComponent w2 = createMock(UIComponent.class);	
        replay(w1,w2);
		cxt.registerWindow(w2);
		
		cxt.setStatusMessage("foo");
		edtWait();
		
		verify(w1,w2);
	}	
	
	public void testGetWindowList() {
		assertEquals(0,cxt.getWindowList().size());
		final UIComponent comp = createNiceMock(UIComponent.class);
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
		final EventList windowList = cxt.getWindowList();
		assertEquals(0,windowList.size());
		try {
			windowList.add(new Object());
			fail("expected to chuck");
		} catch (final UnsupportedOperationException e) {
		}
		assertEquals("mutated window list",0,windowList.size());		
	}
	
	public void testFindMainWindow() throws Exception {

		assertEquals(0,cxt.getWindowList().size());
		UIComponent found = cxt.findMainWindow();
		assertNotNull(found);
		assertTrue(found instanceof HeadlessUIComponent);
		// register a real window.
		final UIComponent comp =createNiceMock(UIComponent.class);
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
		} catch (final InterruptedException x) {
			fail(x.getMessage());
		} catch (final InvocationTargetException x) {
			fail(x.getMessage());
		}
	}
}
