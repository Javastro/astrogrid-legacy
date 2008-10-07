/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:12:15 PM
 */
public class BaseParamTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        bp = new BaseParam();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    protected BaseParam bp;
    
    public void testToString() throws Exception {
        assertNotNull(bp.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(bp,bp);
    }
    
    public void testHashCode() throws Exception {
        bp.hashCode();                
    }
    
    public void testName() throws Exception {
        final String n = "a name";
        bp.setName(n);
        assertEquals(n,bp.getName());        
    }
    
    public void testDescription() throws Exception {
        final String description = "descr";
        bp.setDescription(description);
        assertEquals(description,bp.getDescription());
    }
    
    public void testUnit() throws Exception {
        final String unit = "a unit";
        bp.setUnit(unit);   
        assertEquals(unit,bp.getUnit());
    }
    
    public void testUcd() throws Exception {
        final String ucd = " a ucd";
        bp.setUcd(ucd);
        assertEquals(ucd,bp.getUcd());
    }
    

}
