/*$Id: SampleLegacyServiceServerTest.java,v 1.1 2003/10/12 21:40:37 nw Exp $
 * Created on 01-Oct-2003
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

import org.w3c.dom.Element;

import junit.framework.TestCase;

/** test the sample legacy service - from the server side.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Oct-2003
 *
 */
public class SampleLegacyServiceServerTest extends TestCase {

    /**
     * Constructor for SamplpeLegacyServiceServerTest.
     * @param arg0
     */
    public SampleLegacyServiceServerTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SampleLegacyServiceServerTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InputStream configStream = this.getClass().getResourceAsStream("sample-config.xml");
        serv = (SampleLegacyService)LegacyService.createImplementation(SampleLegacyService.class,configStream);
    }
    protected SampleLegacyService serv;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testGetVersion() throws Exception{
        assertNotNull(serv);
        String result = serv.getVersion();
        assertNotNull(result);
        System.out.println(result);
    }

}


/* 
$Log: SampleLegacyServiceServerTest.java,v $
Revision 1.1  2003/10/12 21:40:37  nw
first import
 
*/