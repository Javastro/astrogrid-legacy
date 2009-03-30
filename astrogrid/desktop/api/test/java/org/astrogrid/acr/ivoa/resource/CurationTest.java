/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:15:03 PM
 */
public class CurationTest extends TestCase {



    private Curation cur;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cur = new Curation();
        
    }

    @Override
    protected void tearDown() throws Exception {
           super.tearDown();
           cur = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(cur.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(cur,cur);
    }
    
    public void testContacts() throws Exception {
        final Contact[] ct = new Contact[] {
        };
        cur.setContacts(ct);
        assertSame(ct,cur.getContacts());
        
    }
    
    public void testContributors() throws Exception {
        final ResourceName[] rn = new ResourceName[] {
        };
        cur.setContributors(rn);
        assertSame(rn,cur.getContributors());
    }

    public void testCreators() throws Exception {
        final Creator[] c = new Creator[] {
        };
        cur.setCreators(c);
        assertSame(c,cur.getCreators());
    }
    
    public void testPublisher() throws Exception {
        final ResourceName rn = new ResourceName();
        cur.setPublisher(rn);
        assertSame(rn,cur.getPublisher());
    }

    public void testVersion() throws Exception {
        final String v = "ver";
        cur.setVersion(v);
        assertSame(v,cur.getVersion());
    }
    
    public void testDates() throws Exception {
        final Date[] d = new Date[] {
        };
        cur.setDates(d);
        assertSame(d,cur.getDates());
    }
 
    
    
}
