/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20082:59:03 PM
 */
public class SourceTest extends TestCase {

    private Source s;

    protected void setUp() throws Exception {
        super.setUp();
        s = new Source();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        s = null;
    }

    public void testToString() throws Exception {
        assertNotNull(s.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(s,s);
    }
    public void testFormat() throws Exception {
       final String f= "a format";
       s.setFormat(f);
       assertEquals(f,s.getFormat());
    }
    
    public void testValue() throws Exception {
        final String v = "valiue";
        s.setValue(v);
        assertEquals(v,s.getValue());
        
    }
}
