/*$Id: AbstractHttpRequest.java,v 1.1 2003/11/18 11:48:14 nw Exp $
 * Created on 01-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.StatusLine;

/** Abstract class that handles much of the work of performing a HTTP request.
 * 
 * <p> uses the '<tt>commons-httpclient</tt>' libraries.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Oct-2003
 *
 */
public abstract class AbstractHttpRequest extends AbstractRequest {

        /** create and initialize a http-client 
         * 
         * @return a http client initialized to the URL {@link #endpoint} 
         * @throws MalformedURLException if {@link #endpoint} does not form a valid URL
         */
        protected HttpClient createClient() throws MalformedURLException {
            HttpClient client = new HttpClient();
            HostConfiguration conf = new HostConfiguration();
            URL u = new URL(this.getEndpoint());
            conf.setHost(u.getHost(),u.getPort(),u.getProtocol());
            client.setHostConfiguration(conf);            
            return client;
        }
        
        /** 
         * Create a httpMethod object that will perform the actual call.
         * <p>
         * 
         * up to subclasses to implement this - differs according to protocol. 
         * @param params - parameters for the method call*/
        
        protected abstract HttpMethod createMethod(NameValuePair[] params) throws RequestMapperException;


    public ReadableByteChannel doRequest(Object[] args)
        throws RequestMapperException, IOException {
            HttpClient client = createClient();
            NameValuePair[] params = buildParams(args);
            HttpMethod method = createMethod(params);
            client.executeMethod(method);
            StatusLine stat = method.getStatusLine();
            if (stat.getStatusCode() >= 400) { // think 400 is the start of the  error codes.
                throw new RequestMapperException("Legacy HTTP service failed with code " + stat.getStatusCode() + " : " + stat.getReasonPhrase());
            }           
            return Channels.newChannel(method.getResponseBodyAsStream()); // problem with post?
           //return Channels.newChannel(new ByteArrayInputStream(method.getResponseBody()));
    }

    /** Convert method call parameters to the representation required by http-client
        and insert fixed parameters.
     * @param args actual parameters for this call.
     * @return representation of parameters required by http-client.
     */
    protected NameValuePair[] buildParams(Object[] args) throws RequestMapperException {
        /*
         * goes though formal parameters, transforming to name/ value pairs
         * value is either fixed, or comes from next value in object array
         */
        int argsPos = 0;
        List parameters = this.getParameters();
        NameValuePair[] result = new NameValuePair[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            Parameter p = (Parameter)parameters.get(i);
            NameValuePair n = new NameValuePair();
            result[i] = n;            
            n.setName(p.getName());
            if (p.isFixed()) { // handle fixed params first.
                if (ParameterType.FLAG.equalsIgnoreCase(p.getType())) { 
                    /* flags don't have an associated value - 
                     * e.g. 'http://..../AxisDataService?wsdl'
                     * is different to 'http://.../AxisDataService?wsdl=true' 
                     */
                    n.setValue("") ; // lets hope this works
                } else {
                    n.setValue(p.getValue());
                }
            } else {
                if (! (argsPos < args.length)) {
                    throw new RequestMapperException("Run out of supplied arguments when constructing query for " + this.getEndpoint());
                }
                n.setValue(args[argsPos++].toString());
            }
        }
        return result;
    }

}


/* 
$Log: AbstractHttpRequest.java,v $
Revision 1.1  2003/11/18 11:48:14  nw
mavenized http2java

Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/