/* $Id: HttpServiceClient.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
 * Created on Jul 24, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.applications.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpRecoverableException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.astrogrid.applications.http.exceptions.HttpApplicationNetworkException;
import org.astrogrid.applications.http.exceptions.HttpApplicationWebServiceURLException;
;

/**
 * Talks to the http get or post service and post-processes the response.
 * @TODO think about big results...streaming, char encoding etc
 * @TODO how are we going to test?
 * 
 * @author jdt
 */
public class HttpServiceClient {
    /**
     * Commons logger
     */
    private org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(HttpServiceClient.class);
    /**
     * The get or set method to call on the server.
     */
    private HttpMethod method;
    /**
     * The client that processes the methods.  
     * @TODO can we make this static?
     */
    private HttpClient client = new HttpClient();
    /**
     * Number of attempts we make before giving up.
     * @TODO consider making this a property.
     */
    private static final int MAX_ATTEMPTS = 3;
    private static final int NOT_FOUND = 404;

    /**
     * Represents if the httpservice is a get or a post. java enum idiom
     * 
     * @author jdt
     *  
     */
    public static class HttpServiceType {
        public static HttpServiceType GET = new HttpServiceType("get");

        public static HttpServiceType POST = new HttpServiceType("post");
        
        public static HttpServiceType TEST = new HttpServiceType("mock");

        private String type;

        /**
         * ctor
         * 
         * @param type name of the type
         */
        private HttpServiceType(final String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }

    /**
     * Constructor
     * Note - the constructor may throw an IllegalArgumentException
     * if this URI is invalid.  Not my fault - that's what the underlying
     * commons-http library does.
     */
    public HttpServiceClient(final String serviceUrl, final HttpServiceType serviceType) {
        if (serviceType==HttpServiceType.GET) {
            method = new GetMethod(serviceUrl);
        } else if (serviceType==HttpServiceType.POST) {
            method = new PostMethod(serviceUrl);
            throw new UnsupportedOperationException("Post is not supported just yet");
        } else if (serviceType==HttpServiceType.TEST){
            throw new UnsupportedOperationException("Haven't done this yet");
        }
        assert method!=null : "serviceType should be get, post or test";
    }

    /**
     * Call the web server and return stuff.
     * @return
     * @throws HttpApplicationNetworkException
     * @throws URIException
     * @throws HttpApplicationWebServiceURLException
     * @throws IOException
     * @throws HttpException
     */
    public String call(final Map args) throws HttpApplicationNetworkException, HttpApplicationWebServiceURLException {
        //@TODO refactor this away
        NameValuePair[] argsArray = new NameValuePair[args.size()];
        Set keys = args.keySet();
        Iterator it = keys.iterator();
        for (int i=0; it.hasNext(); ++i) {
            Object key = it.next();
            argsArray[i] = new NameValuePair((String) key, (String) args.get(key));
        }
        
        method.setQueryString(argsArray);
        int statusCode = -1;
        // We will retry up to 3 times.
        String results = null;
        for (int attempt = 0; statusCode == -1 && attempt < MAX_ATTEMPTS; attempt++) {
            try {
                // execute the method.
                statusCode = client.executeMethod(method);
                results = method.getResponseBodyAsString();
            } catch (HttpRecoverableException e) {
                log.warn("A recoverable error occured, retrying..."+attempt,e);
            } catch (MalformedURLException e) {
                log.error(e);
                throw new HttpApplicationWebServiceURLException("A non-recoverable error occurred connecting to the web site", e);
            } catch (IOException e) {
                log.error(e);
                throw new HttpApplicationNetworkException("A non-recoverable error occurred connecting to the web site", e);
            } finally {
            
                //@TODO will this fail if there *was* an exception?
                method.releaseConnection();
                //@TODO consider recycling.
            }
        }

        // Check that we didn't run out of retries.
        if (statusCode == -1) {
            log.error("Failed to connect");
            throw new HttpApplicationNetworkException("Failed to execute after "+MAX_ATTEMPTS+" tries");
        } else if (statusCode == NOT_FOUND) {
            try {
                log.error("Web site not found");
                throw new HttpApplicationWebServiceURLException("Page not found at " + method.getURI());
            } catch (URIException e) {
                // thrown by getURI method, strangely
                log.error("Unknown URI");
            }
        }
        
        assert results!=null;
        return results;
    }

}

/*
 * $Log: HttpServiceClient.java,v $
 * Revision 1.2  2004/07/27 17:49:57  jdt
 * merges from case3 branch
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.3  2004/07/27 17:12:44  jdt
 * refactored exceptions and finished tests for HttpServiceClient
 *
 * Revision 1.1.2.2  2004/07/26 16:20:30  jdt
 * Added some tests.
 *
 * Revision 1.1.2.1  2004/07/24 18:43:29  jdt
 * Started plumbing in the httpclient
 *
 */