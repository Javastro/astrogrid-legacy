/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20082:17:07 PM
 */
public class UserLoginEventTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testItq() throws Exception {
        final UserLoginEvent e = new UserLoginEvent(true,this);
        assertSame(this,e.getSource());
        assertTrue(e.isLoggedIn());
    }

}
