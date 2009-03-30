/**
 * 
 */
package org.astrogrid.acr.ivoa;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:08:21 PM
 */
public class AvailabilityBeanTest extends TestCase {

    private AvailabilityBean a;
    private String serverName;
    private String location;
    private String message;
    private String validTo;
    private String upTime;
    private String timeOnServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serverName = "a server";
        location = "a location";
        message = "a message";
        validTo = "to";
        upTime= "uptime";
        timeOnServer = "time on server";
        a = new AvailabilityBean(
                serverName
                ,location
                ,message
                ,validTo
                ,upTime
                ,timeOnServer
                );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        serverName = null;
        location = null;
        message = null;
        validTo = null;
        upTime = null;
        timeOnServer = null;
        a = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(a.toString());
    }
    
    public void testEquals() throws Exception {
        assertEquals(a,a);
    }
    
    public void testLocation() throws Exception {
        assertEquals(location,a.getLocation());
    }
    
    public void testMessage() throws Exception {
        assertEquals(message,a.getMessage());
    }
    
    public void testServerName() throws Exception {
        assertEquals(serverName,a.getServerName());
    }
    
    public void testTimeOnServer() throws Exception {
        assertEquals(timeOnServer,a.getTimeOnServer());
    }
    
    public void testUpTime() throws Exception {
        assertEquals(upTime,a.getUpTime());
    }
    
    
    public void testValidTo() throws Exception {
        assertEquals(validTo,a.getValidTo());
    }
    
    
    

}
