/*$Id: XMLFileLocatorTest.java,v 1.3 2004/03/05 16:16:55 nw Exp $
 * Created on 25-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

/** Unit test for XMLFileLocator.
 * basic functioinality is tested by MapLocatorTest. So just verify that we can read a file in correctly.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 *
 */
public class XMLFileLocatorTest extends TestCase {
    /**
     * Constructor for XMLFileLocatorTest.
     * @param arg0
     */
    public XMLFileLocatorTest(String arg0) {
        super(arg0);
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
    
    public void testLoad() throws Exception{
        InputStream is = this.getClass().getResourceAsStream("unit-test-tools.xml");
        assertNotNull(is);
        XMLFileLocator loc = new XMLFileLocator(this.getClass().getResource("unit-test-tools.xml"));
        assertNotNull(loc);
        Map m  = loc.m; // as in same package.
        assertTrue(m.containsKey("testapp"));
        assertEquals(5,m.size());
        MapLocator.ToolInfo info =(MapLocator.ToolInfo)m.get("testapp");
        assertNotNull(info);
        assertEquals("testapp",info.getName());
        assertEquals("http://test",info.getLocation());
        assertEquals("test-interface",info.getInterface());
        
    }
}


/* 
$Log: XMLFileLocatorTest.java,v $
Revision 1.3  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code
 
*/