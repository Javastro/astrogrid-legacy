/*$Id: WebServerTest.java,v 1.3 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRTestSetup;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class WebServerTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv);
    } 
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.pico.getACR();
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
    
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(WebServerTest.class));
    }

}


/* 
$Log: WebServerTest.java,v $
Revision 1.3  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/