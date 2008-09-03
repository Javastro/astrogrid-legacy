/* $Id: TestWebServerTest.java,v 1.3 2008/09/03 14:18:51 pah Exp $
 * Created on Jul 26, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.applications.http.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import fi.iki.elonen.NanoHTTPD;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit Test
 * @TODO we should also make tests via the post method, when the server supports it
 * @author jdt
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Jun 2008
 */
public class TestWebServerTest  {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(TestWebServerTest.class);

    private static final int PORT = 8070;

    private HttpClient client = new HttpClient();

    private HttpMethod method;

    /*
     * @see TestCase#setUp()
     */
    @BeforeClass
    static public void setUpAll() throws Exception {
        //This server should persist for as long as the
        //thread does....so we might need to worry
        //about rebinding to the same port
        log.debug("Starting server...");
        NanoHTTPD server = TestWebServer.getServer(PORT);

        if (log.isTraceEnabled()) {
            log.trace("setUp() - end");
        }
    }
    @org.junit.After
    public void tearDown() throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("tearDown() - start");
        }

        method.releaseConnection();
        method.recycle();

        if (log.isTraceEnabled()) {
            log.trace("tearDown() - end");
        }
    }

    /**
     * Test an echo call with get
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    @Test
    public void testEchoGet() throws HttpException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("testEchoGet() - start");
        }

        method = new GetMethod("http://127.0.0.1:" + PORT + TestWebServer.ECHO_PARAMS_URI+"?foo=bar");
        int status = client.executeMethod(method);
        assertEquals(200, status);
        String response = method.getResponseBodyAsString();
        Properties props= new Properties();
        props.put("foo","bar");
        
        assertEquals(props.toString(),response);

        if (log.isTraceEnabled()) {
            log.trace("testEchoGet() - end");
        }
    }
    
    /**
     * Test a HelloYou call with get
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    @Test
    public void testHelloYou() throws HttpException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("testHelloYou() - start");
        }

        method = new GetMethod("http://127.0.0.1:" + PORT + TestWebServer.HELLO_YOU_URI+"?Message=inabottle");
        int status = client.executeMethod(method);
        assertEquals(200, status);
        String response = method.getResponseBodyAsString();
        
        assertEquals("Hello inabottle",response);

        if (log.isTraceEnabled()) {
            log.trace("testHelloYou() - end");
        }
    }
    
    /**
     * Test the adder service
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    @Test
    public void testAdderGet() throws HttpException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("testAdderGet() - start");
        }

        method = new GetMethod("http://127.0.0.1:" + PORT + TestWebServer.ADDER_URI+"?x=3&y=2");
        int status = client.executeMethod(method);
        assertEquals(200, status);
        String response = method.getResponseBodyAsString();
        assertEquals("5", response);

        if (log.isTraceEnabled()) {
            log.trace("testAdderGet() - end");
        }
    }
    
    /**
     * Test the xml adder service
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    @Test
    public void testAdderXmlGet() throws HttpException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("testAdderGet() - start");
        }

        method = new GetMethod("http://127.0.0.1:" + PORT + TestWebServer.ADDER_URI_XML+"?x=3&y=2");
        int status = client.executeMethod(method);
        assertEquals(200, status);
        String response = method.getResponseBodyAsString();
        assertEquals("<result>5</result>", response);

        if (log.isTraceEnabled()) {
            log.trace("testAdderGet() - end");
        }
    }
    /**
     * Test an Helloworld call with get
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    @Test
    public void testHelloWorldGet() throws HttpException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("testHelloWorldGet() - start");
        }

        method = new GetMethod("http://127.0.0.1:" + PORT + TestWebServer.HELLO_WORLD_URI);
        int status = client.executeMethod(method);
        assertEquals(200, status);
        String response = method.getResponseBodyAsString();
        assertEquals("HelloWorld", response);
        
        if (log.isTraceEnabled()) {
            log.trace("testHelloWorldGet() - end");
        }
    }
    /**
     * Test attempt to use an invalid URL 
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    @Test
    public void testWrong() throws HttpException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("testWrong() - start");
        }

        method = new GetMethod("http://127.0.0.1:" + PORT + "/wrong");
        int status = client.executeMethod(method);
        assertEquals(404, status);
        String response = method.getResponseBodyAsString();
        assertEquals(response, TestWebServer.NOT_FOUND_MSG);

        if (log.isTraceEnabled()) {
            log.trace("testWrong() - end");
        }
    }
}

/*
 * $Log: TestWebServerTest.java,v $
 * Revision 1.3  2008/09/03 14:18:51  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.2.212.1  2008/08/02 13:32:31  pah
 * safety checkin - on vacation
 *
 * Revision 1.2  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *
 * Revision 1.1.2.5  2004/08/12 13:16:58  jdt
 * fixed some tests
 *
 * Revision 1.1.2.4  2004/08/11 22:55:35  jdt
 * Refactoring, and a lot of new unit tests.
 *
 * Revision 1.1.2.3  2004/08/11 14:36:57  jdt
 * refactored the test webserver
 *
 * Revision 1.1.2.2  2004/08/02 18:05:28  jdt
 * Added more tests and refactored the test apps to be set up
 * from xml.
 *
 * Revision 1.1.2.1  2004/07/30 17:03:27  jdt
 * renamed the test web server class
 *
 * Revision 1.1.4.2  2004/07/30 16:59:37  jdt
 * added another test url to the server
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.1  2004/07/26 16:20:30  jdt
 * Added some tests.
 *
 */