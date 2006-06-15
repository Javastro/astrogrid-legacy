/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import junit.framework.TestCase;

/** tests this contribution.
 * @author Noel Winstanley
 * @since Jun 6, 20065:53:14 PM
 */
public class HelpsetContributionUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		c = new HelpsetContribution();
	}
	protected HelpsetContribution c;
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.HelpsetContribution.setPath(String)'
	 */
	public void testSetPath() {
		assertNull(c.getPath());
		c.setPath("test");
		assertEquals("test",c.getPath());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.HelpsetContribution.setResourceAnchor(Class)'
	 */
	public void testSetResourceAnchor() {
		assertNull(c.getResourceAnchor());
		c.setResourceAnchor(Object.class);
		assertEquals(Object.class,c.getResourceAnchor());

	}

}
