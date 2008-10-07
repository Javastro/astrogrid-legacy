/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:12:32 PM
 */
public class CapabilityTest extends TestCase {

    protected Capability cap;

    protected void setUp() throws Exception {
        super.setUp();
        cap = new Capability();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        cap = null;
    }

    public void testToString() throws Exception {
        assertNotNull(cap.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(cap,cap);
    }
    
    public void testHashCode() throws Exception {
        cap.hashCode();
    }
    
    public void testDescription() throws Exception {
        final String d = "foo bar choo";
        cap.setDescription(d);
        assertSame(d,cap.getDescription());
    }
    
    public void testValidationLevel() throws Exception {
        final Validation[] v = new Validation[] {
        };
        cap.setValidationLevel(v);
        assertSame(v,cap.getValidationLevel());
    }
    
    public void testInterfaces() throws Exception {
        final Interface[] ifa = new Interface[] {
        };
        cap.setInterfaces(ifa);
        assertSame(ifa,cap.getInterfaces());
    }
    public void testStandardID() throws Exception {
        final URI st = new URI("test://foo");
        cap.setStandardID(st);
        assertSame(st,cap.getStandardID());
    }
    public void testType() throws Exception {
        final String t = "foo";
        cap.setType(t);
        assertSame(t,cap.getType());
    }
    
}
