/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:17:53 PM
 */
public class InterfaceTest extends TestCase {

    protected Interface i;

    protected void setUp() throws Exception {
        super.setUp();
        i = new Interface();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        i = null;
    }
    
    public void testEquals() throws Exception {
        assertEquals(i,i);
    }
    
    public void testToString() throws Exception {
        assertNotNull(i.toString());
    }
    
    public void testHashcode() throws Exception {
        i.hashCode();
    }
    
    public void testAccessURLS() throws Exception {
        final AccessURL[] ars = new AccessURL[] {
        };
        i.setAccessUrls(ars);
        assertSame(ars,i.getAccessUrls());
    }
    
    public void testRole() throws Exception {
        final String r = "a role";
        i.setRole(r);
        assertEquals(r,i.getRole());
    }
    
    public void testSecurityMethods() throws Exception {
        final SecurityMethod[] me = new SecurityMethod[] {
        };
        i.setSecurityMethods(me);
        assertSame(me,i.getSecurityMethods());
    }
    
    public void testType() throws Exception {
        final String t = "a type";
        i.setType(t);
        assertEquals(t,i.getType());
    }

    public void testVersion() throws Exception {
        final String v= "a version";
        i.setVersion(v);
        assertEquals(v,i.getVersion());
    }
    
    public void testHashCode() throws Exception {
        assertEquals(i.hashCode(),i.hashCode());
    }

}
