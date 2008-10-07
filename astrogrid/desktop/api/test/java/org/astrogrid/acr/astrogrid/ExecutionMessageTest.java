/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import java.util.Date;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200811:54:13 AM
 */
public class ExecutionMessageTest extends TestCase {

    /**
     * 
     */
    private static final String CONTENT = "content";
    /**
     * 
     */
    private static final String STATUS = "a status";
    /**
     * 
     */
    private static final String LEVEL = "a level";
    /**
     * 
     */
    private static final String SOURCE = "a source";

    protected void setUp() throws Exception {
        super.setUp();
        now = new Date();
        msg = new ExecutionMessage(SOURCE,LEVEL,
                    STATUS,now, CONTENT);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    Date now;
    ExecutionMessage msg;
    
    public void testContent() throws Exception {
        assertEquals(CONTENT,msg.getContent());
        
    }

    public void testSource() throws Exception {
        assertEquals(SOURCE,msg.getSource());
        
    }
    
    public void testLevel() throws Exception {
        assertEquals(LEVEL,msg.getLevel());
        
    }
    
    public void testStatus() throws Exception {
        assertEquals(STATUS,msg.getStatus());
    }
    
    public void testTimestamp() throws Exception {
        assertEquals(now,msg.getTimestamp());
        
    }
    
    public void testToString() throws Exception {
        assertNotNull(msg.toString());
        
    }
    
}
