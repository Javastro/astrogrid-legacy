/* $Id: PortalLoginPageHttpUnitTest.java,v 1.1 2004/04/07 15:16:42 jdt Exp $
 * Created on Apr 7, 2004 by jdt@roe.ac.uk
 * The integrationTests project
 * Copyright (c) Astrigrid 2004.  All rights reserved. 
 *
 */
package org.astrogrid.integrationtest.portal;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.TestCase;

import org.astrogrid.integrationtest.common.ConfManager;
import org.xml.sax.SAXException;

import com.meterware.httpunit.*;

/**
 * Test that the portal login page functions correctly
 * using HttpUnit.  Although more verbose than jwebunit
 * it allows access to more detail.
 * @author jdt
 */
public class PortalLoginPageHttpUnitTest extends TestCase {
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(
            PortalLoginPageHttpUnitTest.class);
    private String url;
    private WebConversation conversation;
    /**
     * Kick off the textui
     * @param args ignored
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(PortalLoginPageHttpUnitTest.class);
    }
    /**
     * Get the url of the website and 
     * set it for the remaining tests
     * @throws Exception most likely to throw a RunTime exception if it can't find the property which locates the website
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        url = ConfManager.getConfig().getString("org.astrogrid.portal.site");
        assert url!=null;
        log.debug("Setting portal URL to " + url);
        conversation = new WebConversation();
    }
    /**
     * Check that the login page has the correct forms
     * and links on it
     * @TODO JDT: I expect this to fail at the moment as
     * I'm using get for testing and debugging purposes
     */
    public void testLoginFormIsPost() throws MalformedURLException, IOException, SAXException{
        final String loginPage = url+"/";
        WebResponse response = conversation.getResponse(loginPage);
        WebForm form = response.getFormWithName("");
        assertNotNull(form);
        assertEquals("post",form.getMethod());
    }
}


/*
 *  $Log: PortalLoginPageHttpUnitTest.java,v $
 *  Revision 1.1  2004/04/07 15:16:42  jdt
 *  Initial commit
 *
 */