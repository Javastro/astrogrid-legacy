/* $Id: SimpleMySpaceTest.java,v 1.3 2004/01/19 15:02:49 jdt Exp $
 * Created on 28-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.integrationTests.mySpace;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
/**
 * Not really an integration test this - just an attempt to see if we can 
 * call a few webservices remotely.
 * @author john taylor
 */
public class SimpleMySpaceTest extends TestCase {
    /**
     * Constructor for SimpleMySpaceTest.
     * @param arg0 test name
     */
    public SimpleMySpaceTest(final String arg0) {
        super(arg0);
    }
    /** fire up the test ui
     * 
     * @param args ignored
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(SimpleMySpaceTest.class);
    }
    private static Log log = LogFactory.getLog(SimpleMySpaceTest.class);
    /**
     * Name of properties file
     */
    public final String WEBSERVICES_PROPS = "/webservices.properties";
    /**
     * Name of key in property file for endpoint
     * 
     */
    public final String MYSPACE_ENDPOINT = "mySpaceEndPoint";
    /**
     * Location of MySpace webservice property file
     */
    private String mySpaceEndPoint;
    /**
     * Called before each test
     */
    public final void setUp() throws IOException {
        //load properties
        Properties props = new Properties();
        log.debug("Attempting to load " + WEBSERVICES_PROPS);
        InputStream inputStream =
            this.getClass().getResourceAsStream(WEBSERVICES_PROPS);
        assert inputStream != null : "No file found";
        props.load(inputStream);
        mySpaceEndPoint = props.getProperty(MYSPACE_ENDPOINT);
        log.debug("Web service end-point: " + mySpaceEndPoint);
        assert(mySpaceEndPoint != null);
    }
    public void testAddDeleteUser() {
        assert mySpaceEndPoint != null;
        MySpaceClient client = null;
        try {
            client = MySpaceDelegateFactory.createDelegate(mySpaceEndPoint);
            log.debug("MySpace delegate obtained: " + client);
        } catch (IOException ex) {
            log.error("Failed to obtain a mySpace delegate");
            fail("Failed to create a mySpace Delegate: " + ex);
        }
        assert(client != null);
        // Attempt to add, and then delete a user
        Vector servers = new Vector();
        servers.add("check with Clive what goes here");
        String credential = "";
        String communityId = "roe";
        String userId = "jdtTesting";
        boolean ok1=false;
        boolean ok2=false;
        try {
            log.debug("Attempting to createUser with the following params:");
            log.debug("userID: " + userId);
            log.debug("communityId: " + communityId);
            log.debug("credential: " + credential);
            log.debug("servers: " + servers);
            ok1 =
                client.createUser(userId, communityId, credential, servers);
            log.debug("Result: " + ok1);
        } catch (Exception e) {
            log.error("Error creating user: " + e);
            fail("Exception creating user: " + e);
        } finally {
            log.debug("Attempting to delete user");
            try {
                ok2 = client.deleteUser(userId, communityId, credential);
                log.debug("Result: " + ok2);
            } catch (Exception e) {
                log.error("Error deleting user: " + e);
                log.error(
                    "Warning - failed to delete user; mySpace database may now be corrupt and require some TLC");
                fail("Exception deleting user:  " + e);
            }
        }
        assertTrue("Result from createUser was false", ok1);
        assertTrue("Result from deleteUser was false", ok2);
    }
}
