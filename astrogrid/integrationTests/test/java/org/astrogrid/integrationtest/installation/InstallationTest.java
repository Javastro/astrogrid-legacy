/* $Id: InstallationTest.java,v 1.1 2004/03/29 12:07:30 jdt Exp $
 * Created on Mar 29, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 */
package org.astrogrid.integrationtest.installation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.astrogrid.integrationtest.common.ConfManager;

import junit.framework.TestCase;

/**
 * Check that the test installations are where we expect them to be
 * 
 * @author jdt
 */
public final class InstallationTest extends TestCase {
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(InstallationTest.class);
    /**
     * Configuration holding endpoints of tests
     */
    private ConfManager manager;
    /** 
     * Fire up the text ui
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(InstallationTest.class);
    }
    /**
     * Test setup
     * @see junit.framework.TestCase#setUp()
     *
     */
    public void setUp() {
        manager = ConfManager.getInstance();
    }
    /**
     * Is the portal URL there?
     * @throws IOException if there's a problem finding the portal
     */
    public void testPortal() throws IOException {
        checkURL(manager.getPortalURL());
    }
    /**
     * Is the myspace URL there?
     * @throws IOException if there's a problem finding it
     */
    public void testMySpace() throws IOException {
        checkURL(manager.getMySpaceEndPoint());
    }
    /**
     * Is the JES URL there?
     * @throws IOException if there's a problem finding it
     */
    public void testJES() throws IOException {
        checkURL(manager.getJobControllerEndPoint());
    }
    /**
     * Is the datacenter URL there?
     * @throws IOException if there's a problem finding it
     */
    public void testDatacenter() throws IOException {
        checkURL(manager.getStdDatacenterEndPoint());
    }
    /**
     * Does this URL exist?
     * @param urlString the url to check in string form
     * @throws IOException if unable to open a connection or URL is malformed.
     */
    private void checkURL(final String urlString) throws IOException {
        log.debug("Attempting to open "+urlString);
        final URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();
        conn.connect();
        Map fields = conn.getHeaderFields();
        Set keys = fields.keySet();
        Iterator it =keys.iterator();
        while (it.hasNext()) {
            String headerKey = (String) it.next();
            log.debug(headerKey+": "+conn.getHeaderField(headerKey));
        }
        String response = conn.getHeaderField(null);
        assertEquals("HTTP/1.1 200 OK", response);
        
    }
}


/*
 *  $Log: InstallationTest.java,v $
 *  Revision 1.1  2004/03/29 12:07:30  jdt
 *  Added a new test for the existence of the services used.
 *
 */