/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import junit.framework.TestCase;

/** tests this configuraiton bean.
 * @author Noel Winstanley
 * @since Jun 6, 20065:49:45 PM
 */
public class ServletsContributionUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		s = new ServletsContribution();
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		s = null;
	}
	protected ServletsContribution s;

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ServletsContribution.setName(String)'
	 */
	public void testSetName() {
		assertNull(s.getName());
		s.setName("test");
		assertEquals("test",s.getName());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ServletsContribution.setPath(String)'
	 */
	public void testSetPath() {
		assertNull(s.getPath());
		s.setPath("test");
		assertEquals("test",s.getPath());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ServletsContribution.setServletClass(Class)'
	 */
	public void testSetServletClass() {
		assertNull(s.getServletClass());
		s.setServletClass(Object.class);
		assertEquals(Object.class,s.getServletClass());

	}

}
