/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 20082:58:28 PM
 */
public class SimpleDataTypeTest extends TestCase {

    private SimpleDataType d;

    protected void setUp() throws Exception {
        super.setUp();
        d = new SimpleDataType();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        d = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(d.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(d,d);
    }
    
    public void testType() throws Exception {
        final String t = "a ttype";
        d.setType(t);
        assertEquals(t,d.getType());
    }
    
    public void testArraySize() throws Exception {
        final String a= "an arrauysize";
        d.setArraysize(a);
        assertEquals(a,d.getArraysize());
    }
    

    

}
