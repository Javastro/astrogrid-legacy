/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.util.Date;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 8, 20085:36:01 PM
 */
public class VosiAvailabilityBeanTest extends TestCase {

    private VosiAvailabilityBean v;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        v = new VosiAvailabilityBean();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        v = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(v.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(v,v);
        
    }
    
    public void testHashcode() throws Exception {
        v.hashCode();
    }
    
    public void testGetBackAt() throws Exception {
        final Date d = new Date();
        v.setBackAt(d);
        assertEquals(d,v.getBackAt());
    }
    
    public void testUpSince() throws Exception {
        final Date d = new Date();
        v.setUpSince(d);
        assertEquals(d,v.getUpSince());
    }
    
    public void testDownAt() throws Exception {
        final Date d = new Date();
        v.setDownAt(d);
        assertEquals(d,v.getDownAt());
    }
    
    public void testAvailable() throws Exception {
        assertFalse(v.isAvailable());
        v.setAvailable(true);
        assertTrue(v.isAvailable());
    }

    public void testNotes() throws Exception {
        final String[] notes = new String[] {
        };
        v.setNotes(notes);
        assertSame(notes,v.getNotes());
    }
    
}
