/*$Id: HttpRequestTest.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 10-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.request;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public class HttpRequestTest extends TestCase {

    /**
     * Constructor for HttpRequestTest.
     * @param arg0
     */
    public HttpRequestTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(HttpRequestTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public final static String SIMPLE_ENDPOINT = "http://slashdot.org";
    
    public void testHttpGet()  throws Exception{
        RequestMapper rq = new HttpGet();
        rq.setEndpoint(SIMPLE_ENDPOINT);
        assertEquals(SIMPLE_ENDPOINT,rq.getEndpoint());
        ReadableByteChannel cin = rq.doRequest(new Object[] {});
        checkHTML(cin);
    }


    
    public final static String NEWS_ENDPOINT = "http://news.astrogrid.org/index.php";
    public void testHttpGetWithParams()  throws Exception {
        RequestMapper rq = new HttpGet();
        rq.setEndpoint(NEWS_ENDPOINT);
        Parameter p = new Parameter();
        p.setName("topic");
        p.setType(ParameterType.STRING);
        rq.addParameter(p);
        Object[] params = new Object[]{"VO"};
        ReadableByteChannel cin = rq.doRequest(params);
        checkHTML(cin);        
    }
    
    public final static String NEWS_LOGIN="http://news.astrogrid.org/users.php";
    public void testHttpPost() throws Exception {
        RequestMapper rq = new HttpPost();
        rq.setEndpoint(NEWS_LOGIN);
        Parameter p = new Parameter();
        p.setName("loginname");
        p.setType(ParameterType.STRING);
        rq.addParameter(p);
        
        p = new Parameter();
        p.setName("passwd");
        p.setType(ParameterType.STRING);
        rq.addParameter(p);   
        
        Object[] params = new Object[]{"NoelWinstanley","6f29893d"};
        ReadableByteChannel cin = rq.doRequest(params);
        checkHTML(cin);
       
    }

    private String checkHTML(ReadableByteChannel cin) throws IOException {
        assertNotNull(cin);
        Reader r = Channels.newReader(cin,"UTF-8");
        assertNotNull(r);
        StringWriter sw = new StringWriter();
        int c = 0;
        while (( c = r.read()) != -1) {
            sw.write(c);
        }
        String result = sw.toString();
        assertNotNull(result);        
        assertTrue(result.length() > 0);
        System.out.println(result);
        assertTrue(result.trim().toLowerCase().startsWith("<!doctype html") 
            || result.trim().toLowerCase().startsWith("<html"));
        return result;

    }
}


/* 
$Log: HttpRequestTest.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/