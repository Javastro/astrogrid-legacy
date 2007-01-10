/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.astrogrid.desktop.modules.system.Preference;

import junit.framework.TestCase;

/** test ofr a ui menu contribution.
 * @author Noel Winstanley
 * @since Jun 6, 20065:21:24 PM
 */
public class UIMenuContributionUnitTest extends TestCase implements ComponentListener {

	protected void setUp() throws Exception {
		super.setUp();
		t = new UIMenuContribution();
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		t = null;
	}
	protected UIMenuContribution t;
	private boolean hiddenSeen;
	private boolean movedSeen;
	private boolean resizedSeen;
	private boolean shownSeen;
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIMenuContribution.setIconName(String)'
	 */
	public void testSetIconName() {
		assertNull(t.getIcon());
		t.setIconName("collapse.gif");
		assertNotNull(t.getIcon());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UITabContribution.setAfter(String)'
	 */
	public void testSetAfter() {
		assertNull(t.getAfter());
		t.setAfter("test");
		assertEquals("test",t.getAfter());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UITabContribution.setBefore(String)'
	 */
	public void testSetBefore() {
		assertNull(t.getBefore());
		t.setBefore("test");
		assertEquals("test",t.getBefore());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UIMenuContribution.setParentName(String)'
	 */
	public void testSetParentName() {
		assertNull(t.getParentName());
		t.setParentName("test");
		assertEquals("test",t.getParentName());

	}

	public void testVisibleCondition() throws Exception {
		assertTrue(t.isVisible());
		Preference p = new Preference();
		p.setValue("false");
		t.setVisibleCondition(p);
		assertSame(p,t.getVisibleCondition());
		assertFalse(t.isVisible());
		// check visibility updates on preferrnce change.
		t.addComponentListener(this);
		p.setValue("true");
		assertTrue("doesn't update on preference change",t.isVisible());
		Thread.yield();
		Thread.sleep(200);
		assertTrue(shownSeen); // checks listener interfaces.
	
		// and back again.
		// on non boolean value, goes to invisible.
		p.setValue("blergh");
		assertFalse("should be invisible now",t.isVisible());
		Thread.yield();
		Thread.sleep(200);
		assertTrue(hiddenSeen);
	}
	public void componentHidden(ComponentEvent e) {
		hiddenSeen = true;
	}
	public void componentMoved(ComponentEvent e) {
		movedSeen = true;
	}
	public void componentResized(ComponentEvent e) {
		resizedSeen = true;
	}
	public void componentShown(ComponentEvent e) {
		shownSeen= true;
	}


}
