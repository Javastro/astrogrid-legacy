/* $Id: GetEchoerHTTPD.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
 * Created on Jul 26, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.applications.http.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import fi.iki.elonen.NanoHTTPD;

/**
 * A simple HTTP server that simply echos back any get parameters
 * 
 * @TODO - consider echoing back the params in xml
 * 
 * @author jdt
 */
public class GetEchoerHTTPD extends fi.iki.elonen.NanoHTTPD {
    /**
     * The only URI supported by this webserver...simply echos any parameters
     */
    public static final String TEST_URI = "/echo";

    /**
     * And if you choose anything else....you get this message
     */
    public static final String NOT_FOUND_MSG = "Not found";

    /**
     * Commons logger
     */
    private org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(GetEchoerHTTPD.class);

    /**
     * Constructor
     * Private to force access through factory method
     * @param port port to bind to 
     * @throws IOException on problems binding
     */
    private GetEchoerHTTPD(final int port) throws IOException {
        super(port);
        log.debug("Starting server on port "+port);
    }
    
    /**
     * 
     * Factory method to get server - ensure that we
     * can only get one instance per port
     *
     * @param port port to bind to 
     * @throws IOException on problems binding
     */
    public static GetEchoerHTTPD getServer(final int port) throws IOException {
        Integer pOrt = new Integer(port);
        if (servers.get(pOrt)!=null) {
            return (GetEchoerHTTPD) servers.get(pOrt);
        } else {
            GetEchoerHTTPD server = new GetEchoerHTTPD(port);
            servers.put(pOrt, server);
            return server;
        }
    }
    /**
     * Internal list of servers already set up
     */
    private static Map servers = new HashMap();
    
    

    /**
     * Overrides superclass to simply serve back the http parameters,
     * but only if we're using the get method, and the uri is correct.
     * 
     * @see fi.iki.elonen.NanoHTTPD#serve(java.lang.String, java.lang.String,
     *      java.util.Properties, java.util.Properties)
     */
    public Response serve(final String uri, final String method, final Properties header, final Properties parms) {
        log.debug("URI: " + uri);
        log.debug("method: " + method);
        log.debug("header: " + header);
        log.debug("parms: " + parms);
        Response response;
        if (TEST_URI.equals(uri) && "GET".equals(method)) {
            response = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, parms.toString());
        } else {
            response = new Response(NanoHTTPD.HTTP_NOTFOUND, NanoHTTPD.MIME_HTML, NOT_FOUND_MSG);
        }

        return response;
    }

    /**
     * For testing the class from the command line
     * @param args ignored
     */
    public static void main(final String[] args) {
        System.out.println("NanoHTTPD 1.0 (c) 2001 Jarno Elonen\n" + "(Command line options: [port] [--licence])\n");

        // Change port if requested
        int port = 80;

        GetEchoerHTTPD server = null;
        try {
            server = new GetEchoerHTTPD(port);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Now serving on port " + port + ", echoing get params");
        System.out.println("Hit Enter to stop.\n");

        try {
            System.in.read();
        } catch (Throwable t) {
        }

    }
}

/*
 * $Log: GetEchoerHTTPD.java,v $
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