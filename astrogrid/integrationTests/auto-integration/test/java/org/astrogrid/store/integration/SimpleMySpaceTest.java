/*$Id: SimpleMySpaceTest.java,v 1.11 2004/05/11 09:25:30 pah Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.store.integration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate;
/**
 * Junit tests for a deployed MySpace Manager.
 * @TODO this class gets under the hood of the VOSpace delegate,
 * and uses deprecated classes such as User.  Find out whether this
 * is very naughty.
 *
 * @author jdt@roe.ac.uk
 * @since Iteration 5.
 * @version Iteration 5.
 */
public final class SimpleMySpaceTest extends TestCase {
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(SimpleMySpaceTest.class);
    /**
     * Standard constructor for JUnit test classes.
     *
     * @param name test name
     */
    public SimpleMySpaceTest(final String name) {
        super(name);
    }
    /**
     * Fire up the text UI
     *
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SimpleMySpaceTest.class);
    }
    /**
     * most tests will require a user
     */
    private String defaultUser;
    /**
     * mosts tests will also require a community
     */
    private String defaultCommunity = "pentonville";
    //@TODO - this doesn't seem to be used??
    /**
     * Delegate for the myspace that we're testing
     */
    private MySpaceIt05Delegate myspace;
    /**
     * Used to clear out the myspace after each test
     */
    private MySpaceWiper mySpaceWiper;
    /**
     * Called before each test. Connects to a myspace, sets up a default user
     * and creates it
     *
     * @throws IOException a problem accessing the web service
     */
    public void setUp() throws IOException {
        log.info("\n\n\n\nSetting up...");
        final String endPoint =
            SimpleConfig.getSingleton().getString(
                "org.astrogrid.myspace.endpoint");
        log.info("Running test against endpoint " + endPoint);
        final User operator = new User("someuser@somecommunity", "group", "token");
        myspace = new MySpaceIt05Delegate(operator, endPoint);
        //
        //      Configure the delegate to (i) not generate test responses and
        //      (ii) to throw exceptions if errors are returned.
        myspace.setTest(false);
        myspace.setThrow(true);
        mySpaceWiper = new MySpaceWiper(myspace);
        defaultUser =
            "JonathonAitken" + Long.toString(System.currentTimeMillis());
        createUser(defaultUser, defaultCommunity);
    }
    /**
     * Tidy up following test. Deletes the default user
     *
     * @throws Exception on failure to load the config file, or a problem
     *             accessing the web service
     */
    public void tearDown() {
        log.info("Tearing down...");
        try {
            deleteUser(defaultUser, defaultCommunity);
        } catch (IOException e) {
            fail("There was a problem deleting the test user - the database might now be corrupt\n"+e.toString());
        }
    }
    /**
     * Delete the user with the given params
     *
     * @param userID userId
     * @param communityID communityID
     */
    private void deleteUser(final String userID, final String communityID)
        throws IOException {
        // @TODO - change this, it's deprecated
        log.info("Deleting User " + userID);
        User testAccount = new User(userID, communityID, "", "");
        mySpaceWiper.wipe(testAccount);
    }
    /**
     * create a user with the given params
     *
     * @param userID userId
     * @param communityID communityId
     * @param endPoint myspace webservice end point
     * @throws IOException if the mySpace delegate chucks one
     * @return the value from the delegate method. Bad mix of exceptions and
     *         return codes.
     */
    private void createUser(final String userID, final String communityID)
        throws IOException {
        log.info("Attempting to createUser with the following params:");
        log.info("userID: " + userID);
        log.info("communityId: " + communityID);
        // @TODO - change this, it's deprecated
        User testAccount = new User(userID, communityID, "", "");
        myspace.createUser(testAccount);
        log.info("Account created...");
    }
    public void testUpAndRunning() throws IOException {
        assertTrue("Expect heartbeat true", myspace.heartBeat());
    }
    /**
     * Let's start with something trivial - can we save and return text?
     *
     * @throws IOException no idea
     */
    public void testImportExportDeleteSimpleText() throws IOException {
        for (int i = 0; i < 10; ++i) {
            final String name =
                "foo_"
                    + Integer.toString(i)
                    + "_"
                    + Long.toString(System.currentTimeMillis());
            final String string = "argle" + Integer.toString(i);
            importExportDelete(
                getFullPath(defaultUser, defaultCommunity, name),
                string);
        }
    }
    /**
     * Now a bit harder - can we save and return xml?
     *
     * @throws IOException no idea
     */
    public void testImportExportDeleteXMLTextAgain() throws IOException {
        final String name = "bar" + Long.toString(System.currentTimeMillis());
        final String xml =
            "<?xml version=\"1.0\"?><title>Integration Tests</title>";
        importExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            xml);
    }
    /**
     * Again, with single quotes
     *
     * @throws Exception no idea
     */
    public void testImportExportDeleteXMLText() throws IOException {
        final String name = "foo" + Long.toString(System.currentTimeMillis());
        final String xml =
            "<?xml version='1.0'?><properties><author email='jdt@roe.ac.uk'>John Taylor</author></properties>";
        importExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            xml);
    }
    /**
     * Now lets really go mad - multiline text. Note this has just been done by
     * the addition of a few \n chars....maybe need to think about this a bit
     * more
     *
     * @throws IOException nfi
     */
    public void testImportExportDeleteMultilineText() throws IOException {
        final String name = "foo" + Long.toString(System.currentTimeMillis());
        final String xml =
            "<?xml version=\"1.0\"?>\n<properties>\n<title>Integration Tests</title>\n"
                + "<author email=\"jdt@roe.ac.uk\">John Taylor</author>\n</properties>";
        importExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            xml);
    }
    /**
     * Construct the full path name of a myspace artifact
     *
     * @param user user
     * @param community community
     * @param file filename
     * @return full path @TODO a bit weird that community isn't used?
     */
    private String getFullPath(
        final String user,
        final String community,
        final String file) {
        return "/" + user + "/" + file;
    }
    /**
     * Utility method extracting the commonality of the saveDataHolding tests
     *
     * @param name full path name to save into myspace
     * @param text the text to store
     * @throws IOException who knows
     */
    private void importExportDelete(final String name, final String text)
        throws IOException {
        myspace.putString(text, name, false);
        final String result = myspace.getString(name);
        assertNotNull("Returned result from getDataHolding was null", result);
        log.info("Attempted to save '" + text + "' under name " + name);
        log.info("Received back '" + result + "'");
        assertEquals(
            "data returned from myspace not same as saved",
            result,
            text);
        log.info("deleting...");
        myspace.delete(name);
    }
    /**
     * Utility method extracting the commonality of the saveDataHolding tests
     *
     * @param name under which you wish to save the text
     * @param urlString the url of the data you wish to save
     * @throws Exception who knows
     */
    private void importURLExportDelete(
        final String name,
        final String urlString)
        throws Exception {
        URL url = new URL(urlString);
        myspace.putUrl(url, name, false);
        String result = myspace.getString(name);
        assertNotNull("Returned result from getDataHolding was null", result);
        InputStream is = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer textBuff = new StringBuffer();
        int ch;
        while ((ch = reader.read()) != -1) {
            textBuff.append((char) ch);
        }
        String text = textBuff.toString();
        log.info("Attempted to save from'" + url + "' under name " + name);
        log.info("Received back '" + result + "'");
        log.info("Expected: '" + text + "'");
        assertEquals(
            "data returned from myspace not same as saved",
            result,
            text);
        myspace.delete(name);
    }
    /**
     * test reading from url
     *
     * @throws Exception no idea
     */
    public void testImportExportDeleteURL() throws Exception {
        final String name = "foo" + Long.toString(System.currentTimeMillis());
        final String url =
            "http://wiki.astrogrid.org/pub/Main/JohnTaylor/urlTestConfig.xml";
        importURLExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            url);
    }
    /**
     * Let's start with something trivial - can we save and return text?
     *
     * @throws IOException no idea
     */
/*    public void testImportExportDeleteSomethingHuge() throws IOException {
        final String sentence = "<foo>bar this is bad xml<";
        final StringBuffer buff = new StringBuffer();
        for (int i = 0; i < 10000; ++i) {
            buff.append(sentence);
        }
        final String name = "this_is_a_biggie";
        importExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            buff.toString());
    }*/
    /**
     * For mucking out the myspace following a test. Will get a list of all a
     * user's holdings and delete the lot.
     *
     * @author jdt
     */
    private static class MySpaceWiper {
        /**
         * Commons logger
         */
        private static final org.apache.commons.logging.Log log =
            org.apache.commons.logging.LogFactory.getLog(MySpaceWiper.class);
        /**
         * Which myspace are we wiping?
         */
        private MySpaceIt05Delegate myspace;
        /**
         *
         * Constructor
         *
         * @param myspace The MySpace to wipe
         */
        public MySpaceWiper(final MySpaceIt05Delegate myspace) {
            this.myspace = myspace;
        }
        /**
         * Wipe the account's holding, then delete the account
         *
         * @param account User's holdings to wipe
         * @throws IOException if something bad happens
         */
        private void wipe(final User account) throws IOException {
            myspace.deleteUser(account);
        }
    }
}
/*
 $Log: SimpleMySpaceTest.java,v $
 Revision 1.11  2004/05/11 09:25:30  pah
 make sure that the user object is created properly

 Revision 1.10  2004/05/04 14:25:49  jdt
 Updated MySpaceWiper.  To delete a user it's no longer necessary
 to delete all his files...can just delete the user.   ahhh, much easier.

 */
