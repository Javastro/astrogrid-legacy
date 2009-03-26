/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import junit.framework.TestCase;

/** test for this contribution.
 * @author Noel Winstanley
 * @since Jun 6, 20065:55:16 PM
 */
public class ServletContextContributionUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		c = new ServletContextContribution();
	}
	protected ServletContextContribution c;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		c = null;
	}
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ServletContextContribution.setName(String)'
	 */
	public void testSetName() {
		assertNull(c.getName());
		c.setName("test");
		assertEquals("test",c.getName());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ServletContextContribution.setObject(Object)'
	 */
	public void testSetObject() {
		assertNull(c.getObject());
		Object o = new Object();
		c.setObject(o);
		assertEquals(o,c.getObject());
			
	}

}
