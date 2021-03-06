/*
 * $Id: CommunityPageNewUserTest.java,v 1.5 2004/09/02 10:56:26 jdt Exp $ Created on Jun 7, 2004 by jdt@roe.ac.uk The auto-integration project
 * Copyright (c) Astrigrid 2004. All rights reserved.
 *  
 */
package org.astrogrid.portal.integration;

import java.util.Date;

/**
 * Check that we can create a new user
 * 
 * @author jdt
 */
public final class CommunityPageNewUserTest extends AstrogridPortalWebTestCase {
    /**
     * A known registered community
     */
    private static final String TEST_COMMUNITY = RegisteredUsers.LOCAL_COMMUNITY;
    
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(
            CommunityPageNewUserTest.class);
    /**
     * Names of items on webpage
     */
    private static final String INSERT_NEW_ACCOUNT_DISPLAY = "Insert Account";
    /**
     * Names of items on webpage
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
    private static final String INSERT_ACCOUNT_FORM = "InsertAccount";
    /**
     * Names of items on webpage
     */
    private static final String USERNAME = "ident";
    /**
     * Names of items on webpage
     */
    private static final String COMMUNITY_NAME = "community_belong";
    /**
     * Names of items on webpage
     */
    private static final String PASSWORD = "password";
    /**
     * Names of items on webpage
     */
    private static final String DISPLAY_NAME = ""; //@TODO bug in community page - this is missing
    /**
     * Names of items on webpage
     */
    private static final String DESCRIPTION = "description";
    /**
     * Names of items on webpage
     */
    private static final String INSERT_ACCOUNT_BUTTON = "insertaccount";
    /**
     * Constructor
     * 
     * @param arg0 testname
     */
    public CommunityPageNewUserTest(final String arg0) {
        super(arg0);
    }
    /**
     * Constructor
     *  
     */
    public CommunityPageNewUserTest() {
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
        logout(); //just in case - these tests assume we start logged out
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
     *  
     */
    public void testPageCorrect() {
        assertFormPresent();
        assertFormElementPresent(GO_BUTTON);
        assertFormElementPresent(ADMINTASK_SELECT);
        final String[] validOptions = { INSERT_NEW_ACCOUNT_OPTION };
        assertOptionValuesEqual(ADMINTASK_SELECT, validOptions);
        assertOptionEquals(ADMINTASK_SELECT, INSERT_NEW_ACCOUNT_DISPLAY);
    }
    /**
     * Create a new account and ensure we can login.
     * @TODO check that homespace etc is created.
     *
     */
    public void testCreateNewAccount() {
        final String testUser = "schumie";//+Long.toString(new Date().getTime());
        final String testCommunity = TEST_COMMUNITY;        
        final String testPassword = "sapo";
        final String testDisplayName = "";//"Hyla Cinerea";       
        final String testDescription = "";//"Herbert is an American Green Tree Frog";
 
        System.out.println("Trying to create user:" + testUser);
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        assertTextPresent("Account inserted");
        
        //Pause to allow it to digest that
        System.out.println("Waiting");
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Trying to log in");
        //Can we log in?
        login(testUser, testCommunity, testPassword);
    }
    /**
     * Create a new account and ensure we can login.
     * At the moment the community sends all usernames to lowercase,
     * but doesn't do the same when you log in.  This will confuse
     * our poor user.  Well, it confused poor me anyway. JDT
     * @TODO check that homespace etc is created.
     *
     */
    public void testCreateNewAccountWithUpperCaseUsername() {
        final String testUser = "Bunbury";//+Long.toString(new Date().getTime());
        final String testCommunity = TEST_COMMUNITY;        
        final String testPassword = "sapo";
        final String testDisplayName = "";//"Hyla Cinerea";       
        final String testDescription = "";//"Herbert is an American Green Tree Frog";
 
        System.out.println("Trying to create user:" + testUser);
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        assertTextPresent("Account inserted");
        
        //Pause to allow it to digest that
        System.out.println("Waiting");
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Trying to log in");
        //Can we log in?
        login(testUser, testCommunity, testPassword);
    }   
    /**
     * Try to create an account without a username
     *
     */
    public void testCreateNewAccountNoUser() {
        final String testUser = "";
        final String testCommunity = TEST_COMMUNITY;        
        final String testPassword = "Sapo";
        final String testDisplayName = "Hyla Cinerea";       
        final String testDescription = "Herbert is an American Green Tree Frog";
        
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        assertTextNotPresent("Account inserted");
        assertTextPresent("No account name or community name given to insert");
    }
    
    /**
     * Try to create an account without a community
     *
     */
    public void testCreateNewAccountNoCommunity() {
        final String testUser = "hedley";
        final String testCommunity = "";        
        final String testPassword = "Sapo";
        final String testDisplayName = "Hyla Cinerea";       
        final String testDescription = "Herbert is an American Green Tree Frog";
        
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        assertTextNotPresent("Account inserted");
        assertTextPresent("No account name or community name given to insert");
    }
    
    /**
     * Try to create an account without a password
     *
     */
    public void testCreateNewAccountNoPassword() {
        final String testUser = "hedley";
        final String testCommunity = TEST_COMMUNITY;        
        final String testPassword = "";
        final String testDisplayName = "Hyla Cinerea";       
        final String testDescription = "Herbert is an American Green Tree Frog";
        
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        assertTextNotPresent("Account inserted");
        assertTextPresent("You cannot have no or empty password");
    }
    
    /**
     * Try to create an account without a display name and description
     * (should be OK)
     *
     */
    public void testCanCreateNewAccountNoDescriptionOrDisplayName() {
        final String testUser = "lillian";
        final String testCommunity = TEST_COMMUNITY;        
        final String testPassword = "Brian";
        final String testDisplayName = "";       
        final String testDescription = "";
        
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        assertTextPresent("Account inserted");
        
        //Can we log in?
        login(testUser, testCommunity, testPassword);
    }
    
    /**
     * Login to the portal
     * @param user username
     * @param community community
     * @param password password
     */
    private void login(final String user, final String community, final String password) {
        beginAt("/");
        setFormElement(PortalLoginPageTest.USER,user);
        setFormElement(PortalLoginPageTest.COMMUNITY,community);     
        setFormElement(PortalLoginPageTest.PASS,password);
        submit();
        assertTextPresent(PortalLoginPageTest.LOGIN_SUCCESS_TEXT);
        assertLinkPresentWithText("Log out");
    }
    
    /**
     *   Check that we can't create two accounts with the same username
     */
    public void testCantDuplicateAccount() {
        final String testUser = "rana";
        final String testCommunity = TEST_COMMUNITY;        
        final String testPassword = "Sapo";
        final String testDisplayName = "Hyla Cinerea";       
        final String testDescription = "Rans is also an American Green Tree Frog";
        
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        createUser(testUser, testCommunity, testPassword, testDisplayName, testDescription);
        assertTextNotPresent("Account inserted");
        assertTextPresent("Something sensible here");
    }
    /**
     * Assuming you're on the community page, this method
     * will create an account for you
     * @param testUser who
     * @param testCommunity who with
     * @param testPassword keepout
     * @param testDisplayName full name
     * @param testDescription what
     */
    private void createUser(final String testUser, final String testCommunity, final String testPassword, final String testDisplayName, final String testDescription) {
        beginAt("/main/mount/community/administration.html");
        selectOption(ADMINTASK_SELECT, INSERT_NEW_ACCOUNT_DISPLAY);
        submit();
        assertFormPresent(INSERT_ACCOUNT_FORM);
        setWorkingForm(INSERT_ACCOUNT_FORM);
        assertFormElementPresent(INSERT_ACCOUNT_BUTTON);
        assertTextBoxPresentAndEmpty(USERNAME);
        assertTextBoxPresentAndEmpty(COMMUNITY_NAME);
        assertTextBoxPresentAndEmpty(PASSWORD);
        //assertTextBoxPresentAndEmpty(DISPLAY_NAME); @TODO bug on page
        assertTextBoxPresentAndEmpty(DESCRIPTION);
        

        setFormElement(USERNAME, testUser);
        setFormElement(COMMUNITY_NAME, testCommunity);
        setFormElement(PASSWORD,testPassword);
        //@TODO setFormElement(DISPLAY_NAME,testDisplayName);
        setFormElement(DESCRIPTION, testDescription);
        submit();
    }
    /**
     * Check that a textbox exists on the form, and it is empty.
     * @param box the name of the textbox
     */
    private void assertTextBoxPresentAndEmpty(final String box) {
        assertFormElementPresent(box);
        assertFormElementEmpty(box);
    }
        
    
}
/*
 * $Log: CommunityPageNewUserTest.java,v $
 * Revision 1.5  2004/09/02 10:56:26  jdt
 * Community is currently case-insensitive to account creation,
 * but case sensitive to logins.  Altered the tests to use lower case
 * usernames, but added a test (that I expect to fail for now)
 * to pick up the case problems.
 *
 * Revision 1.4  2004/07/05 15:48:16  jdt
 * another attempt to get this test to pass....though there seems to be something wrong with
 * the community....maybe an installation problem?
 *
 * Revision 1.3  2004/07/05 14:27:45  anoncvs
 * JDT:  Futile attempt to fix the new user test.  Why oh why oh why.
 *
 * rVS: ----------------------------------------------------------------------
 *
 * Revision 1.2  2004/07/05 11:53:15  jdt
 * Attempt to fix CreateNewAccount test.  It seems that user names
 * might be persisting between installs....
 *
 * Revision 1.1  2004/06/07 18:51:12  jdt
 * Name change
 *
 * Revision 1.1  2004/06/07 14:39:16  jdt
 * added new tests
 *
 */