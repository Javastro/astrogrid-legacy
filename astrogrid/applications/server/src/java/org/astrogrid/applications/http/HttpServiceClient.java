/* $Id: HttpServiceClient.java,v 1.1 2008/09/10 23:27:17 pah Exp $
 * Created on Jul 24, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.applications.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.astrogrid.applications.http.exceptions.HttpApplicationException;
import org.astrogrid.applications.http.exceptions.HttpApplicationNetworkException;
import org.astrogrid.applications.http.exceptions.HttpApplicationWebServiceException;
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
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpServiceClient.class);

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
        /**
         * Logger for this class
         */
        private static final Log log = LogFactory.getLog(HttpServiceType.class);

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
        if (log.isTraceEnabled()) {
            log.trace("HttpServiceClient(String serviceUrl = " + serviceUrl + ", HttpServiceType serviceType = "
                    + serviceType + ") - start");
        }

        if (serviceType==HttpServiceType.GET) {
            method = new GetMethod(serviceUrl);
        } else if (serviceType==HttpServiceType.POST) {
            method = new PostMethod(serviceUrl);
            //throw new UnsupportedOperationException("Post is not supported just yet");
        } else if (serviceType==HttpServiceType.TEST){
            throw new UnsupportedOperationException("Haven't done this yet");
        }
        assert method!=null : "serviceType should be get, post or test";

        if (log.isTraceEnabled()) {
            log.trace("HttpServiceClient(String, HttpServiceType) - end");
        }
    }

    /**
     * Call the web server and return stuff.
     * @return The data-set returned by the HTTP service.
     * @throws HttpApplicationNetworkException
     * @throws URIException
     * @throws HttpApplicationWebServiceURLException
     * @throws IOException
     * @throws HttpException
     */
    public Object call(final Map args) throws HttpApplicationWebServiceException {
        if (log.isTraceEnabled()) {
            log.trace("call(Map args = " + args + ") - start");
        }

        //@TODO refactor this away
        NameValuePair[] argsArray = new NameValuePair[args.size()];
        Set keys = args.keySet();
        Iterator it = keys.iterator();
        for (int i=0; it.hasNext(); ++i) {
            Object key = it.next();
            argsArray[i] = new NameValuePair((String) key, (String) args.get(key));
        }
        
        //Unfortunately, the GetMethod and PostMethods have different
        //ways of setting the queries.  Using instanceof is ugly,
        //but is the simplest way
        if (method instanceof PostMethod) {
        	((PostMethod)method).setRequestBody(argsArray);
        } else {
          this.setHttpGetQueryString(method, argsArray);
        }
        
        int statusCode = -1;
        // We will retry up to MAX_ATTEMPTS times.
        Object results = null;
        for (int attempt = 0; statusCode == -1 && attempt < MAX_ATTEMPTS; attempt++) {
            try {
                // execute the method.
            	log.debug("Executing method: "+method);
                statusCode = client.executeMethod(method);
                results = this.getResponseBody(method);
                log.debug("Method returned with results: "+results);
            }
            catch (MalformedURLException e) {
              throw new HttpApplicationWebServiceURLException("A non-recoverable error occurred connecting to the web site", e);
            } 
            catch (IOException e) {
                log.warn("A recoverable error occured on try " + attempt, e);
            }
            catch (Exception e) {
                throw new HttpApplicationWebServiceException("A non-recoverable error occurred connecting to the web site", e);
            } 
            finally {
            
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
                // thrown by getURI method, strangely - no action needed
                log.error("Unknown URI");
            }
        }
        
        assert results!=null;

        if (log.isTraceEnabled()) {
            log.trace("call(Map) - end - return value = " + results);
        }
        return results;
    }
    
    /**
     * Obtains the response body for an HTTP method that has completed
     * its call.
     * Depending on what is being returned, the body can be got either
     * as a Java string or as an array of bytes. Getting a string implies
     * letting the HTTPMethod deal with the perceived character encoding
     * the response: this typically changes the bit pattern. Hence, this
     * method has to apply sme intelligence to the response headers to
     * figure out whether the response body really is character data.
     * If the body is got as a byte array, it can then be packed into
     * a String for transport, preserving the bit pattern.
     *
     * @param method The object representing the HTTP call.
     * @return The response body, either as a String or as a byte array.
     */
    private Object getResponseBody(HttpMethod method) throws IOException {
      Header contentType = method.getResponseHeader("Content-Type");
      Header contentEncoding = method.getResponseHeader("Content-Encoding");
      
      // If the response is encoded (e.g. gzip'd) return it as a byte array
      // whatever its type.
      if (contentEncoding != null && contentEncoding.toString() != "") {
        return method.getResponseBody();
      }
      
      // If the response is not encoded and is text, or is XML,
      // return it as a string.
      if (contentType != null && 
          (contentType.toString().indexOf("text/") != -1 ||
           contentType.toString().indexOf("xml") != -1)) {
        return method.getResponseBodyAsString();
      }
      
      // Otherwise, return the body as a byte array.
      else {
        return method.getResponseBody();
      }
    }
    
    
    /**
     * Set the query part of the URL in an HTTP-get method.
     * Typically, but not in every case, there are query parameters that
     * are given as an array of name-value pairs. There may also be defaulted
     * query parameters already coded into the URL; these need to be recovered
     * and merged with the former set of parameters.
     *
     * @param method The object aggregating the URL and query parameters.
     * @param params The non-defaulted query-parameters.
     */
    private void setHttpGetQueryString(HttpMethod method, 
                                       NameValuePair[] argsArray) {
      
      // Preserve the query string that came with the URL.
      // This comes out null if the given URL has no query parameters.
      String fixedQueryString = method.getQueryString();
      
      // Combine the other parameters into a query string.
      // This will also come out null if there are no parameters.
      method.setQueryString(argsArray);
      String variableQueryString = method.getQueryString();
      
      // Combine the two query Strings, allowing that either or
      // both may be null.
      String queryString;
      if (fixedQueryString == null && variableQueryString == null) {
        queryString = null;
      }
      else if (fixedQueryString == null) {
        queryString = variableQueryString;
      }
      else if (variableQueryString == null) {
        queryString = fixedQueryString;
      }
      else {
        queryString = fixedQueryString + "&" + variableQueryString;
      }
      
      // Store the final query-string.
      method.setQueryString(queryString);      
    }

}

/*
 * $Log: HttpServiceClient.java,v $
 * Revision 1.1  2008/09/10 23:27:17  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.10  2007/08/29 09:55:53  gtr
 * I eliminated use of HttpRecoverableException which is deprecated in HttpClient 3.x.
 *
 * Revision 1.9  2007/08/29 09:21:01  gtr
 * Some private messages now throw IOException, reflecting changes in commons-httpclient.
 *
 * Revision 1.8  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.6  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.5.142.4  2006/02/07 16:34:22  gtr
 * Binary output parameters are now delivered to the ParameterAdapter as byte[] and treated accordingly.
 *
 * Revision 1.5.142.3  2006/01/30 19:16:15  gtr
 * I adjusted the HTTP code to return the response body either as a String or as byte[] depending on whether the response appears to be binary.
 *
 * Revision 1.5.142.2  2006/01/26 13:17:31  gtr
 * *** empty log message ***
 *
 * Revision 1.5.142.1  2006/01/12 17:58:18  gtr
 * I fixed the handling of the HTTP query string in the URL for HTTP-get methods. URLs may now be configured that have parameters embedded: these are defaulted parameters that are never visible or mutable in the UI. The defaulted parameters are merged with the regular parameters when the HTTP method is executed.
 *
 * Revision 1.5  2004/09/14 16:26:26  jdt
 * Attempt to get the http-post working.  Upgraded http-client, to no avail.  Either http client
 * or the embedded test webserver isn't handling post correctly.  Flagged tests
 * to ensure this is dealt with.
 *
 * Revision 1.4  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *
 * Revision 1.1.4.2  2004/08/11 22:55:35  jdt
 * Refactoring, and a lot of new unit tests.
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