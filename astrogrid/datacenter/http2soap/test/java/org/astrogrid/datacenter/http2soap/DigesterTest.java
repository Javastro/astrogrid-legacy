/*$Id: DigesterTest.java,v 1.1 2003/10/12 21:40:37 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

import java.io.InputStream;
import java.util.*;

import org.astrogrid.datacenter.http2soap.request.HttpGet;
import org.astrogrid.datacenter.http2soap.response.RegExpConvertor;
import org.astrogrid.datacenter.http2soap.response.ScriptConvertor;
import org.astrogrid.datacenter.http2soap.response.XsltConvertor;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class DigesterTest extends TestCase {

    /**
     * Constructor for DigesterTest.
     * @param arg0
     */
    public DigesterTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DigesterTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        builder = new ServiceStoreBuilder();
    }
    protected ServiceStoreBuilder builder;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testBuild() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("sample-config.xml");
        assertNotNull(is);
        LegacyWebMethod.Store store = builder.buildStore(is);
        assertNotNull(store);
        LegacyWebMethod slashdot = store.lookupService("slashdotRdf");
        assertNotNull(slashdot);
        assertNotNull(slashdot.requester.getEndpoint());
        assertTrue( slashdot.requester instanceof HttpGet);

        LegacyWebMethod google = store.lookupService("searchGoogle");
        assertNotNull(google);
        List l =  google.requester.getParameters();
        assertNotNull(l);
        System.out.println("parameter list:" + l);
        assertEquals(1,l.size());
        assertEquals("string", ((Parameter)l.get(0)).getType());
        
        assertEquals(google.convertors.size(),1);
        ResponseConvertor conv = (ResponseConvertor)google.convertors.get(0);
        assertNotNull(conv);
        assertTrue(conv instanceof RegExpConvertor);
        assertNotNull( ((RegExpConvertor) conv).getExp());
        assertEquals("(.*)",((RegExpConvertor) conv).getExp());
        
        
        List convertors = slashdot.convertors;
        assertNotNull(convertors);
        assertEquals(2,convertors.size());      
        
        ScriptConvertor c1 = (ScriptConvertor)convertors.get(0);
        assertNotNull(c1);
        assertNotNull(c1.getScript());
        System.out.println(c1.getScript()); 
        
        XsltConvertor c2 = (XsltConvertor)convertors.get(1);
        assertNotNull(c2);
        assertNotNull(c2.getXslt());
        Element e = c2.getXslt().getDocumentElement();
        assertNotNull(e);
        assertEquals("some",e.getNodeName());
       
    }

}


/* 
$Log: DigesterTest.java,v $
Revision 1.1  2003/10/12 21:40:37  nw
first import
 
*/