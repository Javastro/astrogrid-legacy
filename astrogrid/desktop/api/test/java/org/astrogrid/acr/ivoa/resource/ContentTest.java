/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:13:55 PM
 */
public class ContentTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        cn = new Content();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    Content cn;
    
    public void testContentLevel() throws Exception {
        final String[] cl = new String[] {"foo","bar"};
        cn.setContentLevel(cl);
        assertSame(cl,cn.getContentLevel());
        
    }
    
    public void testDescription() throws Exception {
        final String descr="foo";
        cn.setDescription(descr);
        assertEquals(descr,cn.getDescription());
    }
    
    public void testToString() throws Exception {
        assertNotNull(cn.toString());
        
    }
    
    public void testEquals() throws Exception {
        assertEquals(cn,cn);
        
    }
    
    public void testHashCode() throws Exception {
        cn.hashCode();
        
    }
    
    public void testReferenceURI() throws Exception {
        final URI u = new URI("test://foo");
        cn.setReferenceURI(u);
        assertEquals(u,cn.getReferenceURI());
    }
    
    public void testRelationships() throws Exception {
        final Relationship[] rs = new Relationship[] {
                new Relationship()
                ,new Relationship()
        };
        cn.setRelationships(rs);
        assertSame(rs,cn.getRelationships());
    }
    
    public void testSubject() throws Exception {
        final String[] ss = new String[] {
                "foo"
                ,"bar"
        };
        cn.setSubject(ss);
        assertSame(ss,cn.getSubject());
    }
    
    public void testType() throws Exception {
        final String[] ts = new String[] {
                "n","bar"
        };
        cn.setType(ts);
        assertSame(ts,cn.getType());
    }
    
    public void testSource() throws Exception {
        final Source src = new Source();
        cn.setSource(src);
        assertEquals(src,cn.getSource());
    }
}
