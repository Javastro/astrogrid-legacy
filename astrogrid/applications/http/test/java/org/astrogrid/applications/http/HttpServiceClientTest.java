/* $Id: HttpServiceClientTest.java,v 1.7 2006/03/11 05:57:54 clq2 Exp $
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
import org.astrogrid.applications.http.test.TestWebServer;

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
        NanoHTTPD server = TestWebServer.getServer(PORT);
    }

    /**
     * Test an OK call
     * 
     * @throws IOException on test failure
     * @throws HttpException on test failure
     *  
     */
    public void testCall() throws HttpException, IOException {
        final HttpServiceClient client = new HttpServiceClient(LOCALHOST + TestWebServer.ECHO_PARAMS_URI,
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
    	fail("This test is not ready yet, since the in-process webserver doesn't seem very happy with post");
        try {
            final HttpServiceClient client = new HttpServiceClient(LOCALHOST + TestWebServer.ECHO_PARAMS_URI,
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
     * @throws HttpApplicationWebServiceURLException
     * @throws HttpApplicationNetworkException
     *  
     */
    public void testCallWithXML() throws HttpApplicationNetworkException, HttpApplicationWebServiceURLException {
        final HttpServiceClient client = new HttpServiceClient(LOCALHOST + TestWebServer.ADDER_URI_XML,
                HttpServiceClient.HttpServiceType.GET);
        final Properties params = new Properties();
        params.put("x", "4");
        params.put("y", "5");
        final String response = client.call(params);
        assertEquals("Returned result not correct", "<result>9</result>", response);
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
    
    public void testHTTPType() {
        HttpServiceClient.HttpServiceType type = HttpServiceClient.HttpServiceType.GET;
        assertEquals("get", type.toString());
    }
}

/*
 * $Log: HttpServiceClientTest.java,v $
 * Revision 1.7  2006/03/11 05:57:54  clq2
 * roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489
 *
 * Revision 1.5  2004/09/14 16:26:26  jdt
 * Attempt to get the http-post working.  Upgraded http-client, to no avail.  Either http client
 * or the embedded test webserver isn't handling post correctly.  Flagged tests
 * to ensure this is dealt with.
 *
 * Revision 1.4  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *
 * Revision 1.1.4.6  2004/08/11 22:55:35  jdt
 * Refactoring, and a lot of new unit tests.
 *
 * Revision 1.1.4.5  2004/08/11 14:36:31  jdt
 * added a test for a server that returns a an xml formatted result
 *
 * Revision 1.1.4.4  2004/08/02 18:05:28  jdt
 * Added more tests and refactored the test apps to be set up
 * from xml.
 *
 * Revision 1.1.4.3  2004/07/30 17:03:27  jdt
 * renamed the test web server class
 *
 * Revision 1.1.4.2  2004/07/30 16:59:50  jdt
 * limping along.
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