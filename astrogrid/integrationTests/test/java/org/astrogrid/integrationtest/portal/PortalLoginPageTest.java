/* $Id: PortalLoginPageTest.java,v 1.2 2004/04/07 15:51:22 jdt Exp $
 * Created on Apr 7, 2004 by jdt
 * 
 * Copyright (C) AstroGrid. All rights reserved. 
 * 
 * This software is published under the terms of the AstroGrid Software
 * License version 1.2, a copy of which has been included with this distribution 
 * in the LICENSE.txt file.
 *
 */
package org.astrogrid.integrationtest.portal;

import net.sourceforge.jwebunit.WebTestCase;

import org.astrogrid.integrationtest.common.ConfManager;

import com.meterware.httpunit.HttpException;
import com.meterware.httpunit.HttpInternalErrorException;

/**
 * Test that the portal login page functions correctly
 * using jwebunit
 * @TODO add tests that use a real registry
 * @author jdt
 */
public final class PortalLoginPageTest extends WebTestCase {
    /**
     * Form parameter name
     */
    private static final String PASS = "pass";
    /**
     * Form parameter name
     */
    private static final String COMMUNITY = "community";
    /**
     * Form parameter name
     */
    private static final String USER = "user";
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(PortalLoginPageTest.class);
    
    /**
     * Kick off the textui
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PortalLoginPageTest.class);
    }
    /**
     * Get the url of the website and 
     * set it for the remaining tests
     * @throws Exception most likely to throw a RunTime exception if it can't find the property which locates the website
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        final String url = ConfManager.getConfig().getString("org.astrogrid.portal.site");
        assert url!=null;
        log.debug("Setting portal URL to " + url);
        // Set up for jwebtest
        getTestContext().setBaseUrl(url);
    }

    /**
     * Check that the root diverts to the login page
     *
     */
    public void testDivert() {
        beginAt("/");
        assertTextPresent("Welcome to AstroGrid");
        assertTextPresent("Log On");
    }
    /**
     * Check that the login page has the correct forms
     * and links on it
     *
     */
    public void testLoginPageCorrect(){
        beginAt("/");
        assertFormPresent();
        assertFormElementPresent(USER);
        assertFormElementEmpty(USER);
        assertFormElementPresent(COMMUNITY);
        assertFormElementEmpty(COMMUNITY);
        assertFormElementPresent(PASS);
        assertFormElementEmpty(PASS);
        assertFormElementPresent("action");
    }
    
    /**
     *  If we try to login with a field blank,
     *  we should get an error message
     *
     */
    public void testLoginBadValues1(){
        loginWithBlankValues("User name must be filled in", USER);
    }
    /**
     *  If we try to login with a field blank,
     *  we should get an error message
     *
     */
    public void testLoginBadValues2(){
        loginWithBlankValues("Community must be filled in", COMMUNITY);
    }
    /**
     *  If we try to login with a field blank,
     *  we should get an error message
     *
     */
    public void testLoginBadValues3(){
        loginWithBlankValues("Password must be filled in", PASS);
    }
    /**
     * Utility method factoring commonality of testLoginBadValues*
     * @param textToFind text to look for on error page
     * @param setMeBlank parameter to set to ""
     */
    private void loginWithBlankValues(final String textToFind, final String setMeBlank) {
        beginAt("/");
        setFormElement(USER,"jdt");
        setFormElement(COMMUNITY,"jdt");
        setFormElement(PASS,"secret");
        setFormElement(setMeBlank,"");
        submit();
        assertTextPresent(textToFind);
    }
    /**
     * Exactly what it says on the tin
     *
     */
    public void testSuccessfulLogin() {
        beginAt("/");
        setFormElement(USER,"jdt");
        setFormElement(COMMUNITY,"org.astrogrid.mock");
        setFormElement(PASS,"secret");
        submit();
        assertTextPresent("You have successfully logged in.");
        assertLinkPresentWithText("Log out");
    }
    /**
     * Exactly what it says on the tin
     *
     */
    public void testUnsuccessfulLogin() {
        beginAt("/");
        setFormElement(USER,"jdt");
        setFormElement(COMMUNITY,"org.astrogrid.mock");
        setFormElement(PASS,"wrong");
        submit();
        assertTextPresent("Log On failed.");
        final String string = "try again";
        assertLinkPresentWithText(string);
        clickLinkWithText(string);
        assertTextPresent("Log On");
    }
    /**
     * If the user enters a community
     * which does not exist in the registry
     * an exception will be thrown
     *
     */
    public void testLoginWithBadCommunity() {
        beginAt("/");
        setFormElement(USER,"jdt");
        setFormElement(COMMUNITY,"imadethisup");
        setFormElement(PASS,"wrong");
        try {
            submit();
            fail("Expected an exception due to an http 500 error");
        } catch (RuntimeException he) {
            //expected @TODO  surely we can test this less clumsily?
            log.debug("expected this exception", he);
            return;
        }
    }
    /**
     * Exactly what it says on the tin.
     *
     */
    public void testLogout() {
        testSuccessfulLogin();
        final String string = "Log out";
        assertLinkPresentWithText(string);
        clickLinkWithText(string);
        assertTextPresent("Logged Out");
        final String string2 = "Log in";
        assertLinkPresentWithText(string2);
        clickLinkWithText(string2);
        assertTextPresent("Log On");
    }
    
}


/*
 *  $Log: PortalLoginPageTest.java,v $
 *  Revision 1.2  2004/04/07 15:51:22  jdt
 *  added logout test
 *
 *  Revision 1.1  2004/04/07 15:16:42  jdt
 *  Initial commit
 *
 */