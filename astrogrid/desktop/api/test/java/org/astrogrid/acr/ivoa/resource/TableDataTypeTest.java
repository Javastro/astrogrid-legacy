/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:22:26 PM
 */
public class TableDataTypeTest extends TestCase {

    private TableDataType t;

    protected void setUp() throws Exception {
        super.setUp();
        t = new TableDataType();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        t = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(t.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(t,t);
    }

    public void testArraySize() throws Exception {
        final String sa = "1x3x1";
        t.setArraysize(sa);
        assertEquals(sa,t.getArraysize());
    }
    
    public void testType() throws Exception {
        final String ty = "type";
        t.setType(ty);
        assertEquals(ty,t.getType());
    }
}
