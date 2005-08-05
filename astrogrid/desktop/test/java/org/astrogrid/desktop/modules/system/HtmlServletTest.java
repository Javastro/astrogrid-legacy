/*$Id: HtmlServletTest.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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

import net.sourceforge.jwebunit.WebTestCase;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class HtmlServletTest extends WebTestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv); 
        getTestContext().setBaseUrl(serv.getUrlRoot());        
        
    }
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.pico.getACR();
    }
    protected WebServer serv;

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRoot() {
        beginAt("/");
        assertTextPresent("Modules");
        assertLinkPresentWithText("builtin");
        assertLinkPresentWithText("system");
    }
    
    public void testModule() {
        beginAt("/");
        assertLinkPresentWithText("system");
        clickLinkWithText("system");
        assertLinkPresentWithText("up");
        clickLinkWithText("up");
        
    }
    
    public void testComponent() {
        beginAt("/");
        assertLinkPresentWithText("system");
        clickLinkWithText("system");
        assertLinkPresentWithText("configuration");
        clickLinkWithText("configuration");
        assertLinkPresentWithText("up");
        clickLinkWithText("up");
        assertLinkPresentWithText("up");
        clickLinkWithText("up");        
    }
    
    public void testMethod() {
        beginAt("/");
        assertLinkPresentWithText("system");
        clickLinkWithText("system");
        assertLinkPresentWithText("configuration");
        clickLinkWithText("configuration");
        assertLinkPresentWithText("list");
        clickLinkWithText("list");
        assertLinkPresentWithText("up");
        clickLinkWithText("up");
        assertLinkPresentWithText("up");
        clickLinkWithText("up");           
        assertLinkPresentWithText("up");
        clickLinkWithText("up");      
    }
    
    public void testMethodCall() {
        beginAt("/");
        assertLinkPresentWithText("system");
        clickLinkWithText("system");
        assertLinkPresentWithText("configuration");
        clickLinkWithText("configuration");
        assertLinkPresentWithText("getKey");
        clickLinkWithText("getKey");
        assertFormPresent("call");
        setFormElement("key","org.astrogrid.registry.query.endpoint");
        submit();
        assertTextPresent("http://");
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(HtmlServletTest.class));
    }
}


/* 
$Log: HtmlServletTest.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/