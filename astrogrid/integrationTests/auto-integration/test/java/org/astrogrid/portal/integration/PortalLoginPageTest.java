/* $Id: PortalLoginPageTest.java,v 1.3 2004/06/07 14:39:36 jdt Exp $
 * Created on Apr 7, 2004 by jdt
 * 
 * Copyright (C) AstroGrid. All rights reserved. 
 * 
 * This software is published under the terms of the AstroGrid Software
 * License version 1.2, a copy of which has been included with this distribution 
 * in the LICENSE.txt file.
 *
 */
package org.astrogrid.portal.integration;


/**
 * Test that the portal login page functions correctly
 * using jwebunit
 * @TODO add tests that use a real registry
 * @author jdt
 */
public final class PortalLoginPageTest extends AstrogridPortalWebTestCase {
    /**
     * Form parameter name
     */
    public static final String PASS = "pass";
    /**
     * Form parameter name
     */
    public static final String COMMUNITY = "community";
    /**
     * Form parameter name
     */
    public static final String USER = "user";
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(PortalLoginPageTest.class);
    /**
     * How we determine a successful login///
     */
    public static final String LOGIN_SUCCESS_TEXT = "You have successfully logged in.";
    
    /**
     * Kick off the textui
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PortalLoginPageTest.class);
    }


    /**
     * Check that the root diverts to the login page
     *
     */
    public void testDivert() {
        beginAt("/");
        assertTitleEquals("Astrogrid Portal");
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
     * The mock community is a back door that accepts any username and
     * password secret.  It might be removed in the future,
     * invalidating this test
     *
     */
    public void testSuccessfulLogin() {
        final String user = "jdt";
        final String community = "org.astrogrid.mock";
        final String password = "secret";
        login(user, community, password);
    }
    /**
     * Login to the portal
     * @param user username
     * @param community community
     * @param password password
     */
    private void login(final String user, final String community, final String password) {
        beginAt("/");
        setFormElement(USER,user);
        setFormElement(COMMUNITY,community);     
        setFormElement(PASS,password);
        submit();
        assertTextPresent(LOGIN_SUCCESS_TEXT);
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
        logout();
    }
    
    /**
     * Logs out from the webpage - assumes you're on the login page.
     */
    private void logout() {
        final String string = "Log out";
        assertLinkPresentWithText(string);
        clickLinkWithText(string);
        assertTextPresent("Logged Out");
        final String string2 = "Log in";
        assertLinkPresentWithText(string2);
        clickLinkWithText(string2);
        assertTextPresent("Log On");
    }


    /**
     * Test logging in to a real community
     * Woohoo.
     * 
     */
    public void testRealLogin() {
        beginAt("/");
        setFormElement(USER,RegisteredUsers.USERNAME);
        setFormElement(COMMUNITY,RegisteredUsers.LOCAL_COMMUNITY);
        setFormElement(PASS,RegisteredUsers.PASSWORD);
        submit();
        assertTextPresent(LOGIN_SUCCESS_TEXT);
        assertLinkPresentWithText("Log out");
    }
    
    /**
     * Test failing to log in to a real community - bad password
     * Woohoo.
     * 
     */
    public void testRealFailedLoginBadPassword() {
        beginAt("/");
        setFormElement(USER,RegisteredUsers.USERNAME);
        setFormElement(COMMUNITY,RegisteredUsers.LOCAL_COMMUNITY);
        setFormElement(PASS,"wrong");
        submit();
        assertTextPresent("Log On failed.");
        final String string = "try again";
        assertLinkPresentWithText(string);
        clickLinkWithText(string);
        assertTextPresent("Log On");
    }
    /**
     * Test failing to log in to a real community - bad password
     * Woohoo.
     * 
     */
    public void testRealFailedLoginBadUser() {
        beginAt("/");
        setFormElement(USER,"hylacinerea");
        setFormElement(COMMUNITY,RegisteredUsers.LOCAL_COMMUNITY);
        setFormElement(PASS,RegisteredUsers.PASSWORD);
        submit();
        assertTextPresent("Log On failed.");
        final String string = "try again";
        assertLinkPresentWithText(string);
        clickLinkWithText(string);
        assertTextPresent("Log On");
    }
    
}


/*
 *  $Log: PortalLoginPageTest.java,v $
 *  Revision 1.3  2004/06/07 14:39:36  jdt
 *  Refactored out some common stuff
 *
 *  Revision 1.2  2004/04/21 14:24:09  jdt
 *  Added some tests for connecting to a real registry.  
 *  Changed another test to keep pace with the 
 *  changes to the portal.
 *
 *  Revision 1.1  2004/04/15 11:48:09  jdt
 *  Moved to auto-integration
 *
 *  Revision 1.2  2004/04/07 15:51:22  jdt
 *  added logout test
 *
 *  Revision 1.1  2004/04/07 15:16:42  jdt
 *  Initial commit
 *
 */