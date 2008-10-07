/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:04:29 PM
 */
public class TableBeanTest extends TestCase {

    private String name;
    private String description;
    private ColumnBean[] cols;
    private String role;
    private TableBean t;

    protected void setUp() throws Exception {
        super.setUp();
        name = "a name";
        description = "a descript";
        cols = new ColumnBean[] {
        };
        role = " a role";
        t = new TableBean(name,description,cols,role);
        
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        name = null;
        description = null;
        cols = null;
        role = null;
        t = null;
    }
    
    public void testEquals() throws Exception {
        assertEquals(t,t);
    }
    
    public void testToString() throws Exception {
        assertNotNull(t.toString());
    }
    
    public void testColumns() throws Exception {
        assertSame(cols,t.getColumns());
    }
    
    public void testName() throws Exception {
        assertEquals(name,t.getName());
    }
    public void testDwesc() throws Exception {
        assertEquals(description,t.getDescription());
    }
    public void testRole() throws Exception {
        assertEquals(role,t.getRole());
    }

}
