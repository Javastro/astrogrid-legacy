/* $Id: GetEchoerHTTPDTest.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
 * Created on Jul 26, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.applications.http.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import fi.iki.elonen.NanoHTTPD;
import junit.framework.TestCase;

/**
 * JUnit Test
 * 
 * @author jdt
 */
public class GetEchoerHTTPDTest extends TestCase {
    /**
     * Commons logger
     */
    private org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(GetEchoerHTTPDTest.class);

    private static final int PORT = 8070;

    private HttpClient client = new HttpClient();

    private HttpMethod method;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        //This server should persist for as long as the
        //thread does....so we might need to worry
        //about rebinding to the same port
        log.debug("Starting server...");
        NanoHTTPD server = GetEchoerHTTPD.getServer(PORT);
    }

    protected void tearDown() throws Exception {
        method.releaseConnection();
        method.recycle();
    }

    /**
     * Test an OK call with get
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    public void testGET() throws HttpException, IOException {
        method = new GetMethod("http://127.0.0.1:" + PORT + "/echo?foo=bar");
        int status = client.executeMethod(method);
        assertEquals(200, status);
        String response = method.getResponseBodyAsString();
        assertFalse(response.equals(GetEchoerHTTPD.NOT_FOUND_MSG));

    }
    /**
     * Test call with POST - we expect a Not Found, since
     * we're not supporting POST just yet
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    public void testPOST() throws HttpException, IOException {
        method = new PostMethod("http://127.0.0.1:" + PORT + "/echo");
        fail("This hangs, I don't know why, but it's not important right now");
        int status = client.executeMethod(method);
        assertEquals(404, status);
        String response = method.getResponseBodyAsString();
        assertEquals(response, GetEchoerHTTPD.NOT_FOUND_MSG);
    }

    /**
     * Test attempt to use an invalid URL 
     * @throws HttpException on probs
     * @throws IOException on probs
     */
    public void testWrong() throws HttpException, IOException {
        method = new GetMethod("http://127.0.0.1:" + PORT + "/wrong");
        int status = client.executeMethod(method);
        assertEquals(404, status);
        String response = method.getResponseBodyAsString();
        assertEquals(response, GetEchoerHTTPD.NOT_FOUND_MSG);
    }
}

/*
 * $Log: GetEchoerHTTPDTest.java,v $
 * Revision 1.2  2004/07/27 17:49:57  jdt
 * merges from case3 branch
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.1  2004/07/26 16:20:30  jdt
 * Added some tests.
 *
 */