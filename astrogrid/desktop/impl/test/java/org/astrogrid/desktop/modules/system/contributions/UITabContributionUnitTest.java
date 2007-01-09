/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import org.astrogrid.desktop.modules.system.UIImpl;

import junit.framework.TestCase;

/** test for a ui tab contribution.
 * just works oout getter and setter methods.
 * @author Noel Winstanley
 * @since Jun 6, 20065:12:14 PM
 */
public class UITabContributionUnitTest extends TestCase {

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

}
