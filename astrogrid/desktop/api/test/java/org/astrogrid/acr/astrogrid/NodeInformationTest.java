/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:00:20 PM
 */
public class NodeInformationTest extends AbstractInformationTestAbstract {

    /**
     * 
     */
    private static final URI LOCATION = URI.create("http://a.loction/");
    private final boolean isFile = true;
    private Calendar createDate;
    private Calendar modifyDate;
    private Map attributs;
    private final long size = 20L;

    protected void setUp() throws Exception {
        super.setUp();
        createDate = Calendar.getInstance();
        modifyDate = Calendar.getInstance();
        attributs = new HashMap();
        attributs.put("foo",new Object());
        ni= new NodeInformation(NAME,ID,size
                ,createDate, modifyDate, attributs
                , isFile , LOCATION
                );
        ai = ni;
    }
    
    NodeInformation ni;

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAttributes() throws Exception {
        assertEquals(attributs,ni.getAttributes());
        
    }
    
    public void testCreation() throws Exception {
        assertEquals(createDate,ni.getCreateDate());
        
    }
    
    public void testModify() throws Exception {
        assertEquals(modifyDate,ni.getModifyDate());
        
    }
    
    public void testSize() throws Exception {
        assertEquals(size,ni.getSize());
        
    }
    
    public void testIsFile() throws Exception {
        assertTrue(ni.isFile());
        
    }
    
    public void testIsFolder() throws Exception {
        assertFalse(ni.isFolder());
        
    }
    
    public void testContentLocation() throws Exception {
        assertEquals(LOCATION,ni.getContentLocation());
    }
    
}
