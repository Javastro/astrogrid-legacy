/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIImpl;

/** test for a ui tab contribution.
 * just works oout getter and setter methods.
 * @author Noel Winstanley
 * @since Jun 6, 20065:12:14 PM
 */
public class UITabContributionUnitTest extends TestCase implements ComponentListener{

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		t = new UITabContribution();
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		t = null;
	}
	protected UITabContribution t;
	private boolean hiddenSeen;
	private boolean resizedSeen;
	private boolean movedSeen;
	private boolean shownSeen;
	
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
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UITabContribution.setIconName(String)'
	 */
	public void testSetIconName() {
		assertNull(t.getIcon());
		t.setIconName("collapse.gif");
		assertNotNull(t.getIcon());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UITabContribution.setText(String)'
	 */
	public void testSetText() {
		assertNull(t.getText());
		t.setText("test");
		assertEquals("test",t.getText());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UITabContribution.setToolTipText(String)'
	 */
	public void testSetToolTipTextString() {
		assertNull(t.getToolTipText());
		t.setToolTipText("test");
		assertEquals("test",t.getToolTipText());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.UITabContribution.getParentName()'
	 */
	public void testGetParentName() {
		assertEquals(UIImpl.TABS_NAME,t.getParentName());
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
