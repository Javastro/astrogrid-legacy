/*$Id: DigesterTest.java,v 1.2 2003/11/11 14:43:33 nw Exp $
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
import java.util.List;

import junit.framework.TestCase;

import org.astrogrid.datacenter.http2soap.request.HttpGet;
import org.astrogrid.datacenter.http2soap.response.RegExpConvertor;
import org.astrogrid.datacenter.http2soap.response.ResponseConvertor;

/** tests to check the digester is building an object tree as expected.
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

        LegacyWebMethod getVersion = store.lookupService("getVersion");
        assertNotNull(getVersion);
        List l =  getVersion.requester.getParameters();
        assertNotNull(l);
        System.out.println("parameter list:" + l);
        assertEquals(1,l.size());
        
        assertEquals(getVersion.convertors.size(),1);
        ResponseConvertor conv = (ResponseConvertor)getVersion.convertors.get(0);
        assertNotNull(conv);
        assertTrue(conv instanceof RegExpConvertor);
        assertNotNull( ((RegExpConvertor) conv).getExp());
        
       
    }

}


/* 
$Log: DigesterTest.java,v $
Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:40:37  nw
first import
 
*/