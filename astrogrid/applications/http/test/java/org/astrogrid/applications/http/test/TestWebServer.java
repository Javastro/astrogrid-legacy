/* $Id: TestWebServer.java,v 1.3 2008/09/03 14:18:51 pah Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fi.iki.elonen.NanoHTTPD;

/**
 * A simple HTTP server that offers a number of test services.
 * 
 * @TODO - consider echoing back the params in xml
 * 
 * @author jdt
 */
public class TestWebServer extends fi.iki.elonen.NanoHTTPD {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(TestWebServer.class);

    /**
     * A URI supported by this webserver...simply echos any parameters
     */
    public static final String ECHO_PARAMS_URI = "/echo";
    /**
     * A URI supported by this webserver...echos the value of message
     */
    public static final Object HELLO_YOU_URI = "/helloYou";
    /**
     * A URI supported by this webserver...returns "HelloWorld"...the original and still the best.
     */    
    public static final String HELLO_WORLD_URI = "/helloWorld";
    
    /**
     * A URI supported by this webserver...returns the sum of the args x and y
     */    
    public static final String ADDER_URI = "/add";
    /**
     * A URI supported by this webserver...returns the sum of the args x and y, wrapped in an xml snipped
     */    
    public static final String ADDER_URI_XML = "/add-xml";
    /**
     * And if you choose anything else....you get this message
     */
    public static final String NOT_FOUND_MSG = "Not found";





    /**
     * Constructor
     * Private to force access through factory method
     * @param port port to bind to 
     * @throws IOException on problems binding
     */
    private TestWebServer(final int port) throws IOException {
        super(port);
        log.debug("Starting server on port "+port);
        responders.put(ADDER_URI, new AdderResponder());
        responders.put(ADDER_URI_XML, new AdderXMLResponder());
        responders.put(ECHO_PARAMS_URI, new EchoResponder());
        responders.put(HELLO_YOU_URI, new HelloYouResponder());
        responders.put(HELLO_WORLD_URI, new HelloWorldResponder());

        if (log.isTraceEnabled()) {
            log.trace("TestWebServer(int) - end");
        }
    }
    
    /**
     * 
     * Factory method to get server - ensure that we
     * can only get one instance per port
     *
     * @param port port to bind to 
     * @throws IOException on problems binding
     */
    public static TestWebServer getServer(final int port) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("getServer(int port = " + port + ") - start");
        }

        Integer pOrt = new Integer(port);
        if (servers.get(pOrt)!=null) {
            TestWebServer returnTestWebServer = (TestWebServer) servers.get(pOrt);
            if (log.isTraceEnabled()) {
                log.trace("getServer(int) - end - return value = " + returnTestWebServer);
            }
            return returnTestWebServer;
        } else {
            TestWebServer server = new TestWebServer(port);
            servers.put(pOrt, server);

            if (log.isTraceEnabled()) {
                log.trace("getServer(int) - end - return value = " + server);
            }
            return server;
        }
    }
    /**
     * Internal list of servers already set up
     */
    private static Map servers = new HashMap();
    /**
     * Map of URI's and the responder that does the work
     * 
     */
    private Map responders = new HashMap();

    
    

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
        // We're not currently supporting post
        if (!"GET".equals(method)) {
            Response returnResponse = new Response(NanoHTTPD.HTTP_NOTFOUND, NanoHTTPD.MIME_HTML, NOT_FOUND_MSG);
            if (log.isTraceEnabled()) {
                log.trace("serve(String, String, Properties, Properties) - end - return value = " + returnResponse);
            }
            return returnResponse;
        }
        // Do we recognise the URI?
        Responder responder = (Responder) responders.get(uri);
        if (responder==null) {
            response = new Response(NanoHTTPD.HTTP_NOTFOUND, NanoHTTPD.MIME_HTML, NOT_FOUND_MSG);
        } else {
            response = responder.getResponse(header, parms);
        }

        if (log.isTraceEnabled()) {
            log.trace("serve(String, String, Properties, Properties) - end - return value = " + response);
        }
        return response;
    }
    
    private static interface Responder {
        public Response getResponse(Properties header, Properties parms);
    }
    private class AdderResponder implements Responder {

        /* (non-Javadoc)
         * @see org.astrogrid.applications.http.test.TestWebServer.Responder#getResponse(java.util.Properties, java.util.Properties)
         */
        public Response getResponse(Properties header, Properties parms) {
            String sum = calcSum(header, parms);
            Response returnResponse = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, sum);
            return returnResponse;
        }

        /**
         * Adamup
         * @param header from request
         * @param parms from request
         * @return string containing sum of parms "x" and "y"
         * @throws NumberFormatException if the user is particularly thick
         */
        protected String calcSum(Properties header, Properties parms) throws NumberFormatException {
            int x = Integer.parseInt((String)parms.get("x"));
            int y = Integer.parseInt((String)parms.get("y"));
            String sum = new Integer(x+y).toString();
            return sum;
        }
        
    }
    private class AdderXMLResponder extends AdderResponder {
        /* (non-Javadoc)
         * @see org.astrogrid.applications.http.test.TestWebServer.Responder#getResponse(java.util.Properties, java.util.Properties)
         */
        public Response getResponse(Properties header, Properties parms) {
            String sum = "<result>"+calcSum(header, parms)+"</result>";
            Response returnResponse = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, sum);
            return returnResponse;
        }        
    }
    private class EchoResponder implements Responder {
        /* (non-Javadoc)
         * @see org.astrogrid.applications.http.test.TestWebServer.Responder#getResponse(java.util.Properties, java.util.Properties)
         */
        public Response getResponse(Properties header, Properties parms) {
            Response returnResponse = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, parms.toString());
            return returnResponse;
        }
        
    }
    private class HelloWorldResponder implements Responder {
        /* (non-Javadoc)
         * @see org.astrogrid.applications.http.test.TestWebServer.Responder#getResponse(java.util.Properties, java.util.Properties)
         */
        public Response getResponse(Properties header, Properties parms) {
            Response returnResponse = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "HelloWorld");
            return returnResponse;
        }
        
    }
    
    private class HelloYouResponder implements Responder {
        /* (non-Javadoc)
         * @see org.astrogrid.applications.http.test.TestWebServer.Responder#getResponse(java.util.Properties, java.util.Properties)
         */
        public Response getResponse(Properties header, Properties parms) {
            String message =  (String) parms.get("Message");
            Response response = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "Hello "+message);
            return  response;
        }     
    }


    /**
     * For testing the class from the command line
     * @param args ignored
     */
    public static void main(final String[] args) {
        if (log.isTraceEnabled()) {
            log.trace("main(String[] args = " + args + ") - start");
        }

        System.out.println("NanoHTTPD 1.0 (c) 2001 Jarno Elonen\n" + "(Command line options: [port] [--licence])\n");

        // Change port if requested
        int port = 80;

        TestWebServer server = null;
        try {
            server = new TestWebServer(port);
        } catch (IOException ioe) {
            log.error("main(String[])", ioe);

            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Now serving on port " + port + ", echoing get params");
        System.out.println("Hit Enter to stop.\n");

        try {
            System.in.read();
        } catch (Throwable t) {
            log.error("main(String[])", t);
        }

        if (log.isTraceEnabled()) {
            log.trace("main(String[]) - end");
        }
    }
}

/*
 * $Log: TestWebServer.java,v $
 * Revision 1.3  2008/09/03 14:18:51  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.2.212.1  2008/04/01 13:50:06  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.2  2004/09/01 15:42:26  jdt
 * Merged in Case 3
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