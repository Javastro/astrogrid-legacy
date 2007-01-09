/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import junit.framework.TestCase;

/** test ofr a ui menu contribution.
 * just exercises getter and setter mthods
 * @author Noel Winstanley
 * @since Jun 6, 20065:21:24 PM
 */
public class UIMenuContributionUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		t = new UIMenuContribution();
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		t = null;
	}
	protected UIMenuContribution t;
	
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

}
