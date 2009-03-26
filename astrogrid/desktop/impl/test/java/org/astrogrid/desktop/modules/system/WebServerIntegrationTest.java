/*$Id: WebServerIntegrationTest.java,v 1.6 2009/03/26 18:01:21 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class WebServerIntegrationTest extends InARTestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv);
    } 
    @Override
    protected void tearDown() throws Exception {
    	super.tearDown();
    	serv = null;
    }

    protected WebServer serv;


    public void testGetKey() {
        assertNotNull(serv.getKey());
    }

    public void testGetPort() {
        assertTrue(serv.getPort() > 0);
        
    }

    public void testGetUrlRoot() throws MalformedURLException {
        assertNotNull(serv.getUrlRoot());
        new URL(serv.getUrlRoot()); // will throw a malformed thingie if not a url.
    }
    

    public void testGetRoot(){
        assertNotNull(serv.getRoot());
    }
    
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(WebServerIntegrationTest.class));
    }

}


/* 
$Log: WebServerIntegrationTest.java,v $
Revision 1.6  2009/03/26 18:01:21  nw
added override annotations

Revision 1.5  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.4  2007/01/29 10:42:48  nw
tidied.

Revision 1.3  2007/01/23 11:53:36  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/