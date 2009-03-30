/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.astrogrid.acr.astrogrid.TableBean;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:12:55 PM
 */
public class CatalogTest extends TestCase {

    private Catalog c;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        c = new Catalog();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        c = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(c.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(c,c);
    }
    
    public void testName() throws Exception {
        final String n = "a name";
        c.setName(n);
        assertEquals(n,c.getName());
    }
    
    public void testDescription() throws Exception {
        final String d = "a descrip";
        c.setDescription(d);
        assertEquals(d,c.getDescription());
    }
    
    public void testTables() throws Exception {
        final TableBean[] tb = new TableBean[]{
        };
        c.setTables(tb);
        assertSame(tb,c.getTables());
    }

}
