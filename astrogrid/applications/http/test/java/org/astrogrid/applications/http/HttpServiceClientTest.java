/* $Id: HttpServiceClientTest.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
 * Created on Jul 26, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.applications.http;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;
import org.astrogrid.applications.http.exceptions.HttpApplicationNetworkException;
import org.astrogrid.applications.http.exceptions.HttpApplicationWebServiceException;
import org.astrogrid.applications.http.exceptions.HttpApplicationWebServiceURLException;
import org.astrogrid.applications.http.test.GetEchoerHTTPD;

import fi.iki.elonen.NanoHTTPD;

/**
 * JUnit Test
 * 
 * @author jdt
 */
public class HttpServiceClientTest extends TestCase {
    /**
     * Commons logger
     */
    private org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(HttpServiceClientTest.class);

    /**
     * Port on which to bind our test webserver
     */
    private static final int PORT = 8070;

    private static final String LOCALHOST = "http://127.0.0.1" + ":" + PORT;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        log.debug("Attempting to get test server...");
        NanoHTTPD server = GetEchoerHTTPD.getServer(PORT);
    }

    /**
     * Test an OK call
     * 
     * @throws IOException on test failure
     * @throws HttpException on test failure
     *  
     */
    public void testCall() throws HttpException, IOException {
        final HttpServiceClient client = new HttpServiceClient(LOCALHOST + GetEchoerHTTPD.TEST_URI,
                HttpServiceClient.HttpServiceType.GET);
        final Properties params = new Properties();
        params.put("George", "Bush");
        params.put("Genocidal", "Maniac");
        final String response = client.call(params);
        //@TODO...careful...what if they're not in the same order?
        assertEquals("Returned parameters not correct", response, params.toString());
    }

    /**
     * Post is not supported just yet...check that.
     * 
     * @throws IOException
     * @throws HttpException
     *  
     */
    public void testPostIsDuff() throws HttpException, IOException {
        try {
            final HttpServiceClient client = new HttpServiceClient(LOCALHOST + GetEchoerHTTPD.TEST_URI,
                    HttpServiceClient.HttpServiceType.POST);
            final Properties params = new Properties();
            params.put("George", "Bush");
            params.put("Genocidal", "Maniac");

            final String response = client.call(params);
        } catch (UnsupportedOperationException e) {
            //good
            return;
        }
        fail("Expected an UnsupportedOperationException here");
    }

    /**
     * Should behave nicely if XML is returned...
     *  
     */
    public void testCallWithXML() {
        fail("To be done");
    }

    /**
     * Should fail nicely if we provide a stupid URL
     *  
     */
    public void testDuffURL1() throws HttpApplicationNetworkException {
        final HttpServiceClient client = new HttpServiceClient(LOCALHOST + "/somewherestupid",
                HttpServiceClient.HttpServiceType.GET);
        final Properties params = new Properties();

        final String response;
        try {
            response = client.call(params);
        } catch (HttpApplicationWebServiceURLException e) {
            // expected
            return;
        }
        fail("Expected a HttpApplicationWebServiceURLException");
    }

    /**
     * Should fail nicely if we provide a malformed URL
     *  
     */
    public void testDuffURL2() throws HttpApplicationWebServiceException   {
        try {
        final HttpServiceClient client = new HttpServiceClient("blah:ceci n'est pas un URL",
                HttpServiceClient.HttpServiceType.GET);
        final Properties params = new Properties();


            final String response = client.call(params);
        } catch (IllegalArgumentException e) {
            //good
            return;
        }

        fail("Expected a IllegalArgumentException exception");
    }

    /**
     * Should fail nicely if we provide a URL that times out
     * @throws HttpApplicationWebServiceURLException
     * 
     * @throws IOException
     * @throws HttpException
     * @throws IOException
     * @throws HttpException
     *  
     */
    public void testDuffURL3() throws HttpApplicationWebServiceURLException  {
        //hope this port isn't in use
        final HttpServiceClient client = new HttpServiceClient("http://127.0.0.1:2304/somewherestupid",
                HttpServiceClient.HttpServiceType.GET);
        final Properties params = new Properties();
        params.put("George", "Bush");
        params.put("Genocidal", "Maniac");
        final String response;
        try {
            response = client.call(params);
        } catch (HttpApplicationNetworkException e) {
            // expected
            return;
        }
        fail("Expected a HttpApplicationNetworkException");
    }
}

/*
 * $Log: HttpServiceClientTest.java,v $
 * Revision 1.2  2004/07/27 17:49:57  jdt
 * merges from case3 branch
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.2  2004/07/27 17:12:44  jdt
 * refactored exceptions and finished tests for HttpServiceClient
 * Revision 1.1.2.1 2004/07/26 16:20:30 jdt
 * Added some tests.
 *  
 */