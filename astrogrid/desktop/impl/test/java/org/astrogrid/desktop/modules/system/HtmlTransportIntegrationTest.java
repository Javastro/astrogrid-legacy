/*$Id: HtmlTransportIntegrationTest.java,v 1.8 2009/02/17 13:46:33 nw Exp $
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.jwebunit.WebTestCase;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;

import com.meterware.httpunit.WebClient;

/** tests the html interface;
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class HtmlTransportIntegrationTest extends WebTestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        final ACR reg = getACR();
        serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv); 
        getTestContext().setBaseUrl(serv.getUrlRoot());        
    }
    protected ACR getACR() throws Exception{
        return ARTestSetup.fixture.getACR();
    }
    protected WebServer serv;

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        serv = null;
    }

    // test that the server root is coled down - should just give a 'forbidden'
    public void testFrontClosedDown()  throws Exception{
    	final URL u = serv.getRoot();
    	final URL u1 = new URL("http://" + u.getHost() + ":" + u.getPort()); 
    	// can't find a way to use webtest to test for errors - pity.
    	final HttpURLConnection conn = (HttpURLConnection) u1.openConnection();
    	conn.connect();
    	assertEquals(403,conn.getResponseCode());
    	conn.disconnect();
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
        assertTextNotPresent("ERROR"); // can't find way to test error codes.
    }
    
    // regression test for bz #1647
    public void testNullReturnMethodCall() {
    	beginAt("/");
    	assertLinkPresentWithText("system");
    	clickLinkWithText("system");
        assertLinkPresentWithText("configuration");
        clickLinkWithText("configuration");
        assertLinkPresentWithText("removeKey");
        clickLinkWithText("removeKey");
        assertFormPresent("call");
        setFormElement("string","fred");
        submit();
        assertTextPresent("OK"); 
    }
    

    public void testCheckedException() {
        beginAt("/test/transporttest/"); // need to jump straight here, as test methods are hidden from navigation.
        
        assertLinkPresentWithText("throwCheckedException");
        clickLinkWithText("throwCheckedException");
        assertFormPresent("call");
        // expecting to cause an error now - so need to dive down to lower level api, and disable exceptions on http error.
        final WebClient wc =  getDialog().getWebClient();
        wc.setExceptionsThrownOnErrorStatus(false);
        submit();
        assertEquals(500,wc.getCurrentPage().getResponseCode());
        assertTextPresent("NotFoundException"); 
    }
    
    public void testUncheckedException() throws IOException {
        beginAt("/test/transporttest/"); // need to jump straight here, as test methods are hidden from navigation.
        assertLinkPresentWithText("throwUncheckedException");
        clickLinkWithText("throwUncheckedException");
        assertFormPresent("call");
        // expecting to cause an error now - so need to dive down to lower level api, and disable exceptions on http error.
        final WebClient wc =  getDialog().getWebClient();
        wc.setExceptionsThrownOnErrorStatus(false);
        submit();
        assertEquals(500,wc.getCurrentPage().getResponseCode());
        assertTextPresent("NullPointerException");  
        
    }

    public void testUncheckedExceptionOfUnknownType() throws IOException {
    	beginAt("/test/transporttest/"); // need to jump straight here, as test methods are hidden from navigation.
        assertLinkPresentWithText("throwUncheckedExceptionOfUnknownType");
        clickLinkWithText("throwUncheckedExceptionOfUnknownType");
        assertFormPresent("call");
        // expecting to cause an error now - so need to dive down to lower level api, and disable exceptions on http error.
        final WebClient wc =  getDialog().getWebClient();
        wc.setExceptionsThrownOnErrorStatus(false);
        submit();
        assertEquals(500,wc.getCurrentPage().getResponseCode());
        assertTextPresent("AnUnknownRuntimeException");  
        
    }
    
    public void testByteArrayTransport() { 
        beginAt("/test/transporttest/"); // need to jump straight here, as test methods are hidden from navigation.

        assertLinkPresentWithText("echoByteArray");
        clickLinkWithText("echoByteArray");
        assertFormPresent("call");
        setFormElement("arr","fred"); //can't pass in a byte array - has to be as string.
        submit();
        assertTextPresent("fred"); 
    }
    
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(HtmlTransportIntegrationTest.class));
    }
}


/* 
$Log: HtmlTransportIntegrationTest.java,v $
Revision 1.8  2009/02/17 13:46:33  nw
Complete - taskfix http input of binary parameters.

Revision 1.7  2008/12/30 22:01:42  nw
improved in-program API help.

Revision 1.6  2007/04/18 15:47:04  nw
tidied up voexplorer, removed front pane.

Revision 1.5  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.4  2007/01/29 10:42:48  nw
tidied.

Revision 1.3  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/