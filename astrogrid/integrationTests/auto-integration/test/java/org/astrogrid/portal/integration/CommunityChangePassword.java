/*
 * $Id: CommunityChangePassword.java,v 1.1 2004/06/07 14:39:16 jdt Exp $ Created on Jun 7, 2004 by jdt@roe.ac.uk The auto-integration project
 * Copyright (c) Astrigrid 2004. All rights reserved.
 *  
 */
package org.astrogrid.portal.integration;
/**
 * Check that we can create a new user
 * 
 * @author jdt
 */
public final class CommunityChangePassword extends AstrogridPortalWebTestCase {
    /**
     * A known registered community
     */
    private static final String TEST_COMMUNITY = "org.astrogrid.mock";
    /**
     * A known registered user
     */
    private static final String TEST_USER = RegisteredUsers.USERNAME;
    /**
     * A known registered password
     */
    private static final String TEST_PASSWORD = "secret";//RegisteredUsers.PASSWORD;
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(
            CommunityChangePassword.class);
    /**
     * Names of items on webpage
     * Some of these are the display text, some the names of the html
     * items.  Alas, jwebtest seems to favour the former, which are more
     * vulnerable to change.
     */
    private static final String GO_BUTTON = "admintaskselect";
    /**
     * Names of items on webpage
     */
    private static final String ADMINTASK_SELECT = "action";
    /**
     * Names of items on webpage
     */
    
    private static final String INSERT_NEW_ACCOUNT_OPTION = "insertaccount";
    /**
     * Names of items on webpage
     */
    private static final String INSERT_NEW_ACCOUNT_DISPLAY = "Insert Account";
    /**
     * Names of items on webpage
     */    
    private static final String CHANGE_PASSWORD_OPTION = "changeofpassword";
    /**
     * Names of items on webpage
     */
    private static final String CHANGE_PASSWORD_DISPLAY = "Change Password";
    /**
     * Names of items on webpage
     */
    private static final String CHANGE_PASSWORD_FORM = "ChangeOfPassword";
    /**
     * Names of items on webpage
     */
    private static final String CURRENT_PASSWORD = "current_password";
    /**
     * Names of items on webpage
     */
    private static final String NEW_PASSWORD = "new_password";
    /**
     * Names of items on webpage
     */
    private static final String NEW_PASSWORD2 = "verify_password";
    /**
     * Names of items on webpage
     */
    private static final String CHANGE_PASSWORD_BUTTON = "changepassword";
    

    /**
     * Constructor
     * 
     * @param arg0 testname
     */
    public CommunityChangePassword(final String arg0) {
        super(arg0);
    }
    /**
     * Constructor
     *  
     */
    public CommunityChangePassword() {
        super();
    }
    /**
     * Get the url of the website and 
     * set it for the remaining tests
     * @throws Exception most likely to throw a RunTime exception if it can't find the property which locates the website
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        login(TEST_USER, TEST_COMMUNITY, TEST_PASSWORD);
        beginAt("/main/mount/community/administration.html");
    }
    /**
     * Log out
     */
    private void logout() {
        beginAt("/main/mount/login/logout"); 
    }
    /**
     * Check that the Community page has the correct forms 
     * Since we haven't logged in, the only option should be
     * to select a new account
     */
    public void testPageCorrect() {
        assertFormPresent();
        assertFormElementPresent(GO_BUTTON);
        assertFormElementPresent(ADMINTASK_SELECT);
        final String[] validOptions = { CHANGE_PASSWORD_OPTION,
                                        INSERT_NEW_ACCOUNT_OPTION };
        //assertOptionValuesEqual(ADMINTASK_SELECT, validOptions);
        assertOptionEquals(ADMINTASK_SELECT, CHANGE_PASSWORD_DISPLAY);
    }
    /**
     * Change our password
     *
     */
    public void testChangePassword() {
        final String newPassword="abraracourcix";
        changePassword(TEST_PASSWORD, newPassword, newPassword);
        assertTextPresent("Account's password changed.");
        
        //Can we log in with the old password?
        logout();
        loginUnchecked(TEST_USER, TEST_COMMUNITY, TEST_PASSWORD);
        assertTextNotPresent(PortalLoginPageTest.LOGIN_SUCCESS_TEXT);
        //can we log in with new password?
        login(TEST_USER, TEST_COMMUNITY, newPassword);
        //@TODO if we have a failure, we could end up with a screwed up user that other
        //tests can't use.   Oh well.
        
        //restore the old password.
        changePassword(newPassword, TEST_PASSWORD, TEST_PASSWORD);
    }
    
    /**
     * Try changing the password, without knowing the original
     *
     */
    public void testChangePasswordOriginalWrong() {
        final String newPassword="abraracourcix";
        changePassword("badGuess", newPassword, newPassword);
        assertTextNotPresent("Account's password changed.");
    }
    
    /**
     * Login to the portal and check success
     * @param user username
     * @param community community
     * @param password password
     */
    private void login(final String user, final String community, final String password) {
        loginUnchecked(user, community, password);
        assertTextPresent(PortalLoginPageTest.LOGIN_SUCCESS_TEXT);
        assertLinkPresentWithText("Log out");
    }
    
    /**
     * Login to the portal, without checking success
     * @param user username
     * @param community community
     * @param password password
     */
    private void loginUnchecked(final String user, final String community, final String password) {
        beginAt("/");
        setFormElement(PortalLoginPageTest.USER,user);
        setFormElement(PortalLoginPageTest.COMMUNITY,community);     
        setFormElement(PortalLoginPageTest.PASS,password);
        submit();
    }
    /**
     * This method
     * will change your password, assuming you're logged in.
     * @param currentPassword old
     * @param newPassword new
     * @param newPassword2 confirm
     */
    private void changePassword(final String currentPassword, 
                                final String newPassword,
                                final String newPassword2) {
        beginAt("/main/mount/community/administration.html");
        selectOption(ADMINTASK_SELECT, CHANGE_PASSWORD_DISPLAY);
        submit();
        assertFormPresent(CHANGE_PASSWORD_FORM);
        setWorkingForm(CHANGE_PASSWORD_FORM);
        assertFormElementPresent(CHANGE_PASSWORD_BUTTON);
        assertTextBoxPresentAndEmpty(CURRENT_PASSWORD);
        assertTextBoxPresentAndEmpty(NEW_PASSWORD);
        assertTextBoxPresentAndEmpty(NEW_PASSWORD2);

        setFormElement(CURRENT_PASSWORD, currentPassword);
        setFormElement(NEW_PASSWORD, newPassword);
        setFormElement(NEW_PASSWORD2,newPassword2);
        submit();
    }
    /**
     * Check that a named textbox exists, and it is empty.
     * @param box the name of the textbox to check
     */
    private void assertTextBoxPresentAndEmpty(final String box) {
        assertFormElementPresent(box);
        assertFormElementEmpty(box);
    }
        
    
}
/*
 * $Log: CommunityChangePassword.java,v $
 * Revision 1.1  2004/06/07 14:39:16  jdt
 * added new tests
 *
 */