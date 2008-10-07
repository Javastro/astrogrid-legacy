/**
 * 
 */
package org.astrogrid.acr.builtin;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:05:38 PM
 */
public abstract class DescriptorTestAbstract extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        d = null;
    }
    
    Descriptor d;
    
    public void testToString() throws Exception {
        assertNotNull(d.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(d,d);
    }
    
    public void testName() throws Exception {
        final String n = " a name";
        d.setName(n);
        assertEquals(n,d.getName());
    }
    
    public void testDesciption() throws Exception {
        final String de = "a descritp";
        d.setDescription(de);
        assertEquals(de,d.getDescription());
    }
    
    

}
