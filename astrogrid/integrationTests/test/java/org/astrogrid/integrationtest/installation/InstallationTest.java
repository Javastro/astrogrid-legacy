/* $Id: InstallationTest.java,v 1.4 2004/03/30 16:52:23 jdt Exp $
 * Created on Mar 29, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 */
package org.astrogrid.integrationtest.installation;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.astrogrid.config.Config;
import org.astrogrid.integrationtest.common.ConfManager;

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
    private Config conf;
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
        conf = ConfManager.getConfig();
    }
    
    /**
     * Return only keys in the config which are URLs
     * At the moment this is a hardwired collection
     * because no suitable method exists in Config to 
     * get all the keys.
     * 
     * @return a collection of keys which might be urls
     */
    private Collection getURLKeys() {
        final Collection keys = new ArrayList();
        keys.add("registry.registry.endpoint");
        keys.add("registry.admin.endpoint");
        keys.add("registry.harvest.endpoint");
                  
        keys.add("appController.endPoint");
        keys.add("community.endPoint");
        keys.add("mySpaceEndPoint");
        keys.add("portalWebSite");
        keys.add("jobControlerEndPoint");
        keys.add("stdDatacenterEndPoint");
        return keys;
    }
    
    /**
     * Goes through properties which are URLs and checks
     * they do indeed point to something, and don't return 404s
     * @throws IOException if the URL is malformed, or the website down
     *
     */
    public void testURLsAreLive() throws IOException {
        final Collection keys = getURLKeys();
        final Iterator it = keys.iterator();
        while (it.hasNext()) {
            final String key = (String) it.next();
            final String url = conf.getString(key);
            log.debug("Checking "+key+"="+url);
            checkURL(url);
        }
        
    }
    
    
    
    /**
     * Does this URL exist?
     * @param urlString the url to check in string form
     * @throws IOException if unable to open a connection or URL is malformed.
     */
    private void checkURL(final String urlString) throws IOException {
        final URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();
        conn.connect();
        final Map fields = conn.getHeaderFields();
        final Set keys = fields.keySet();
        final Iterator it =keys.iterator();
        while (it.hasNext()) {
            final String headerKey = (String) it.next();
            log.debug(headerKey+": "+conn.getHeaderField(headerKey));
        }
        final String response = conn.getHeaderField(null);
        assertEquals("Checking "+urlString,"HTTP/1.1 200 OK", response);
       
    }
    
}


/*
 *  $Log: InstallationTest.java,v $
 *  Revision 1.4  2004/03/30 16:52:23  jdt
 *  added registry endpoints
 *
 *  Revision 1.3  2004/03/30 00:17:31  jdt
 *  added a couple more urls to test.
 *
 *  Revision 1.2  2004/03/29 17:47:58  jdt
 *  Made it a bit more general.
 *
 *  Revision 1.1  2004/03/29 12:07:30  jdt
 *  Added a new test for the existence of the services used.
 *
 */