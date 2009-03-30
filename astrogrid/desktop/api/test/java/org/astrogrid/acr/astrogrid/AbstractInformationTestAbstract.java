/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200811:46:03 AM
 */
public abstract class AbstractInformationTestAbstract extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected AbstractInformation ai;
    protected static final String NAME = "a name";
    protected static final URI ID = URI.create("test://id");
    
    public void testName() throws Exception {
        assertEquals(NAME,ai.getName());
    }
    
    public void testID() throws Exception {
        
        assertEquals(ID,ai.getId());
    }
    
    public void testToString() throws Exception {
        assertNotNull(ai.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(ai,ai);
    }

}
