/* $Id: InstallationTest.java,v 1.3 2004/09/21 00:01:03 jdt Exp $
 * Created on Mar 29, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 */
package org.astrogrid.installation.integration;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 * Check that the test installations are where we expect them to be
 * 
 * @author jdt
 */
public final class InstallationTest  {
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(InstallationTest.class);
    /**
     * Configuration holding endpoints of tests
     */
    private static Config conf=SimpleConfig.getSingleton();
    /**
     * Hide
     * Constructor
     *
     */
    private InstallationTest() {}

    /** 
     * Fire up the text ui
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    
    /**
     * Return only keys in the config which are URLs
     * For our purpose a URL is a string beginning with "http:"
     * 
     * @return a collection of keys which might be urls
     */
    private static Collection getURLKeys() {
        final Collection ignoredURLs = new ArrayList();
        ignoredURLs.add("java.vendor.url");
        ignoredURLs.add("java.vendor.url.bug");
        
        final Collection keys = conf.keySet();
        keys.removeAll(ignoredURLs);
        
        final Iterator it = keys.iterator();
        final Collection urlKeys = new HashSet();
        while(it.hasNext()) {
            final String key = (String) it.next();
            final String prop = conf.getString(key);
            if (prop.startsWith("http:")) {
                urlKeys.add(key);
            }
        }
        return urlKeys;
    }
    
    /**
     * Goes through properties which are URLs and checks
     * they do indeed point to something, and don't return 404s
     * @return a sutie containing a test for each URL
     */
    public static Test  suite() {
        final Collection keys = getURLKeys();
        final Iterator it = keys.iterator();
        final TestSuite suite =
            new TestSuite("Test for live urls");
        while (it.hasNext()) {
            final String key = (String) it.next();
            final String url = conf.getString(key);
            log.debug("Checking "+key+"="+url);
            suite.addTest(new URLTest(key,url));
        }
        return suite;
    }
    


    /**
     *  The following class takes care of testing if a URL is live
     */
    private static class URLTest extends TestCase {
        /** the url under test */
        private String testurl;
        /**
         * Constructor
         * @param key used to name the test
         * @param url the url under test
         */
        public URLTest(final String key, final String url) {
            super(key);
            this.testurl = url;
        }
        /**
         * executed by the test runner
         * @see junit.framework.TestCase#runTest()
         * @throws IOException if unable to open a connection or URL is malformed.
         */
        public final void runTest() throws IOException {
            checkURL(testurl);
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
            //Allow 200 (OK) and 401 (Unauthorised)
            assertTrue("Checking "+urlString,(response.indexOf("HTTP/1.1 200 OK")!=-1) ||
            		                         (response.indexOf("HTTP/1.1 401 Unauthorized")!=-1));
        
        }
    } //end class
}


/*
 *  $Log: InstallationTest.java,v $
 *  Revision 1.3  2004/09/21 00:01:03  jdt
 *  The new org.astrogrid.memory.endpoint url requires a login,
 *  so was giving a 401 (unauthorized) when checked.  Relaxed test
 *  to allow this, but really not sure about it.  I guess I'm just checking for
 *  duff URLs, so perhaps this is OK.
 *
 *  Revision 1.2  2004/04/20 13:25:19  jdt
 *  Removed java.vendor.url and java.vendor.url.bug urls
 *
 *  Revision 1.1  2004/04/15 11:48:09  jdt
 *  Moved to auto-integration
 *
 *  Revision 1.8  2004/04/13 10:30:54  jdt
 *  tidied imports
 *
 *  Revision 1.7  2004/03/31 11:29:06  jdt
 *  Now uses keySet to dynamically create the list of urls to test.
 *
 *  Revision 1.6  2004/03/31 01:17:35  jdt
 *  added jes
 *
 *  Revision 1.5  2004/03/31 01:11:56  jdt
 *  now adds test dynamically, one for each url
 *
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