/* $Id: RegisterPageTest.java,v 1.1 2004/04/07 15:51:36 jdt Exp $
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

/**
 * Test that the portal login page functions correctly
 * using jwebunit
 * @TODO add tests that use a real registry
 * @author jdt
 */
public final class RegisterPageTest extends WebTestCase {
    /**
     * Form parameter name
     */
    private static final String EMAIL = "email";
    /**
     * Form parameter name
     */
    private static final String NAME = "name";
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(RegisterPageTest.class);
    
    /**
     * Kick off the textui
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(RegisterPageTest.class);
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
        beginAt("/");
        clickLinkWithText("register");
    }


    /**
     * Check that the login page has the correct forms
     * and links on it
     *
     */
    public void testRegistryPageCorrect(){

        assertFormPresent();
        assertFormElementPresent(NAME);
        assertFormElementEmpty(NAME);
        assertFormElementPresent(EMAIL);
        assertFormElementEmpty(EMAIL);
        assertFormElementPresent("action");
    }
    
    /**
     * Should get an error if a blank name supplied
     *
     */
    public void testBlankName() {
        registerWithDuffValues("Name must be filled in", NAME, "");
    }
    /**
     * Should get an error if a blank email supplied
     *
     */
    public void testBlankEmail() {
        registerWithDuffValues("Email address must be filled in", EMAIL, "");
    }
    /**
     * Should get an error if a bad email supplied
     *
     */
    public void testBadEmail() {
        registerWithDuffValues("not a valid email address", EMAIL, "Oh those russians");
    }
    /**
     * Utility method factoring commonality of testLoginBadValues*
     * @param textToFind text to look for on error page
     * @param setMeBlank parameter to set to ""
     */
    private void registerWithDuffValues(final String textToFind, final String setMeBad, final String badValue) {
        setFormElement(NAME,"John the Tester");
        setFormElement(EMAIL,"jdt@roe.ac.uk"); //I'm going to regret this
        setFormElement(setMeBad,badValue);
        submit();
        assertTextPresent(textToFind);
    }
    
    /**
     * Sends an email to the adminstrator every time
     * this is called. I think I might regret this one.
     *
     */
    public void testRegister() {
        setFormElement(NAME,"Rasputin");
        setFormElement(EMAIL,"alexandra@madmonk.com"); 
        submit();
        assertTextPresent("Registration completed");        
    }
    
}


/*
 *  $Log: RegisterPageTest.java,v $
 *  Revision 1.1  2004/04/07 15:51:36  jdt
 *  initial commit
 *
 */