/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

/** unit test for help item contribution.
 * @author Noel Winstanley
 * @since Jan 11, 200712:48:21 PM
 */
public class HelpItemContributionUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		i = new HelpItemContribution();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		i = null;
	}
	HelpItemContribution i;

	/**
	 * Test method for {@link org.astrogrid.desktop.modules.system.contributions.HelpItemContribution#getId()}.
	 */
	public void testGetId() {
		assertNull(i.getId());
		i.setId("foo");
		assertNotNull(i.getId());
		assertEquals("foo",i.getId());
		i.setId(null);
		assertNull(i.getId());
	}

	/**
		 * Test method for {@link org.astrogrid.desktop.modules.system.contributions.HelpItemContribution#getUrlObject()}.
		 */
		public void testGetUrlObject() throws Exception {
			assertNull(i.getUrlObject());
			final String u = "http://www.slashdot.org";
			final URL url = new URL(u);
			i.setUrl(u);
			assertNotNull(i.getUrlObject());
			assertEquals(url,i.getUrlObject());
			
			try {
				i.setUrl("malformed");
				fail("expected to fail");
			} catch (MalformedURLException e) {
				// ok
				assertEquals(url,i.getUrlObject());
			}
	
			try {
				i.setUrl(null);
				fail("expected to fail");
			} catch (MalformedURLException e) {
				// ok
				assertEquals(url,i.getUrlObject());
			}	
		}
		
		public void testEquals() throws Exception {
            assertTrue(i.equals(i));
            HelpItemContribution j  = new HelpItemContribution();
            j.setId("fink");
            assertFalse(i.equals(j));
            
            // run toString
            assertNotNull(i.toString());
            assertNotNull(j.toString());
            assertTrue(i.hashCode() != 0);
           
        }

}
