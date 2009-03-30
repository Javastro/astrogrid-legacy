/**
 * 
 */
package org.votech;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:10:25 PM
 */
public class VoMonBeanTest extends TestCase {

    private VoMonBean v;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        v = new VoMonBean();
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
    
    public void testCode() throws Exception {
        final int code = 42;
        v.setCode(code);
        assertEquals(code,v.getCode());
    }
    
    public void testId() throws Exception {
        final URI id = new URI("ivo://a.id");
        v.setId(id);
        assertEquals(id,v.getId());
    }
    
    public void testMillis() throws Exception {
        final long m = 2343423;
        v.setMillis(m);
        assertEquals(m,v.getMillis());
    }
    
    public void testStatus() throws Exception {
        final String st = "a status";
        v.setStatus(st);
        assertEquals(st,v.getStatus());
    }
    
    public void testTimestamp() throws Exception {
        final String ts = "a timestamp";
        v.setTimestamp(ts);
        assertEquals(ts,v.getTimestamp());
    }
    
    
}
