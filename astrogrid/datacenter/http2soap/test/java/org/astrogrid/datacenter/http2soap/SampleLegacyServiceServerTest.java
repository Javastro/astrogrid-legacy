/*$Id: SampleLegacyServiceServerTest.java,v 1.2 2003/11/11 14:43:33 nw Exp $
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

import junit.framework.TestCase;

import org.w3c.dom.Element;

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
        Float result = serv.getVersion();
        assertNotNull(result);
        assertEquals(1.1,result.floatValue(),0.01);
    }
   
    public void testSlashdotRDF() throws Exception {
        // fails - need special treatment for flags.
        assertNotNull(serv);
        Element result = serv.slashdotRdf();
        assertNotNull(result);
        assertEquals("RDF",result.getLocalName());
    }


}


/* 
$Log: SampleLegacyServiceServerTest.java,v $
Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:40:37  nw
first import
 
*/