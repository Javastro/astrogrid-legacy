/*
 * $Id: SimpleMySpaceTest.java,v 1.2 2004/03/12 22:27:19 jdt Exp $ Created on
 * 28-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.astrogrid.integrationtest.myspace;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.integrationtest.common.ConfManager;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
/**
 * Not really an integration test this - just an attempt to see if we can call
 * a few webservices remotely. 
 * 
 * @author john taylor
 */
public final class SimpleMySpaceTest extends TestCase {
  /**
   * Constructor for SimpleMySpaceTest.
   * 
   * @param arg0
   *            test name
   */
  public SimpleMySpaceTest(final String arg0) {
    super(arg0);
  }
  /**
   * fire up the test ui
   * 
   * @param args
   *            ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(SimpleMySpaceTest.class);
  }
  /**
   * Commons logger
   */
  private static final  Log log = LogFactory.getLog(SimpleMySpaceTest.class);

  /**
   * Location of MySpace webservice property file
   */
  private String mySpaceEndPoint;
  /**
   * most tests will require a user
   */
  private String defaultUser = "NeilHamilton";
  /**
   * mosts tests will also require a community
   */
  private String defaultCommunity = "pentonville";
  /**
   * similarly a credential, though this is empty at the mo.
   */
  private String defaultCredential = "any";
  /**
   * default server
   */
  private String defaultServer = "serv1";
  /**
   * Called before each test. Sets up a default user
   * 
   * @throws Exception
   *             on failure to load the config file, or a problem accessing
   *             the web service
   */
  public  void setUp() throws Exception {
    log.debug("\n\n\n\nSetting up...");
    mySpaceEndPoint = ConfManager.getInstance().getMySpaceEndPoint();
    assert(mySpaceEndPoint != null);
    boolean ok = createUser(defaultUser, defaultCommunity, mySpaceEndPoint);
    if (!ok) {
      log.error("Failed to create user - check the mySpace by hand");
    }
  }
  /**
   * Tidy up following test. Deletes the default user
   * 
   * @throws Exception
   *             on failure to load the config file, or a problem accessing
   *             the web service
   */
  public  void tearDown() throws Exception {
    log.debug("Tearing down...");
    boolean ok = deleteUser(defaultUser, defaultCommunity, mySpaceEndPoint);
    if (!ok) {
      log.error("Failed to delete user - check the mySpace by hand");
    }
  }

  /**
   *  Delete the user with the given params
   * @param userID userId
   * @param communityID communityID
   * @param endPoint end point of myspace server
   * @return returns false if there was an exception thrown by the mySpace
   */
  private boolean deleteUser(
    final String userID,
    final String communityID,
    final String endPoint)
    {
    
    MySpaceWiper wiper;
    try {
        wiper = new MySpaceWiper(endPoint);
        wiper.clear(userID, communityID);
    } catch (IOException e) {
        log.error("Exception: ", e);
        return false;
    } catch (MySpaceDelegateException e1) {
       log.error("Exception: ", e1);
       return false;
    }

    return true;
  }
  /**
   *  create a user with the given params
   * @param userID userId
   * @param communityID communityId
   * @param endPoint myspace webservice end point
   * @throws Exception if the mySpace delegate chucks one
   * @return the value from the delegate method.  Bad mix of exceptions and return codes.
   */
  private boolean createUser(
    final String userID,
    final String communityID,
    final String endPoint) throws Exception
     {

    MySpaceClient client = getDelegate(endPoint);
    // Attempt to add, and then delete a user
    Vector servers = new Vector();
    servers.add(defaultServer);
    String credential = "any";
    boolean ok = false;
    try {
      log.debug("Attempting to createUser with the following params:");
      log.debug("userID: " + userID);
      log.debug("communityId: " + communityID);
      log.debug("credential: " + credential);
      log.debug("servers: " + servers);
      ok = client.createUser(userID, communityID, credential, servers);
      log.debug("Created? " + ok);
    } catch (Exception e) {
      log.error("Error creating user: " + e);
      throw e;
    } 
    return ok;
  }
  /**
   * @param endPoint myspace service endpoint
   * @return a mySpace delegate
   * @throws IOException on failure to obtain delegate
   */
  private MySpaceClient getDelegate(final String endPoint) throws IOException {
    assert endPoint != null;
    MySpaceClient client = null;
    try {
      client = MySpaceDelegateFactory.createDelegate(endPoint);
      log.debug("MySpace delegate obtained: " + client);
    } catch (IOException ex) {
      log.error("Failed to obtain a mySpace delegate");
      throw ex;
    }
    assert(client != null);
    return client;
  }
  /**
   * Let's start with something trivial - can we save and return text?
   * @throws Exception no idea
   */
  public void testImportExportDeleteSimpleText() throws Exception {
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
   * @throws Exception no idea
   */
  public void testImportExportDeleteXMLTextAgain() throws Exception {
    final String name = "bar" + Long.toString(System.currentTimeMillis());
    final String xml =
      "<?xml version=\"1.0\"?><properties><title>Integration Tests</title><author email=\"jdt@roe.ac.uk\">John Taylor</author></properties>";
    importExportDelete(getFullPath(defaultUser, defaultCommunity, name), xml);
  }

  /**
   * Again, with single quotes
   * @throws Exception no idea
   */
  public void testImportExportDeleteXMLText() throws Exception {
    final String name = "foo" + Long.toString(System.currentTimeMillis());
    final String xml =
      "<?xml version='1.0'?><properties><title>Integration Tests</title><author email='jdt@roe.ac.uk'>John Taylor</author></properties>";
    importExportDelete(getFullPath(defaultUser, defaultCommunity, name), xml);
  }

  /**
   * Now lets really go mad - multiline text. Note this has just been done by the addition
   * of a few \n chars....maybe need to think about this a bit more
   * @throws Exception nfi
   */
  public void testImportExportDeleteMultilineText() throws Exception {
    final String name = "foo" + Long.toString(System.currentTimeMillis());
    final String xml =
      "<?xml version=\"1.0\"?>\n<properties>\n<title>Integration Tests</title>\n<author email=\"jdt@roe.ac.uk\">John Taylor</author>\n</properties>";
    importExportDelete(getFullPath(defaultUser, defaultCommunity, name), xml);
  }

  /**
   * Construct the full path name of a myspace artifact
   * @param user user
   * @param community community
   * @param file filename
   * @return full path
   */
  private String getFullPath(
    final String user,
    final String community,
    final String file) {
    return "/" + user + "@" + community + "/" + defaultServer + "/" + file;
  }
  /**
   * Utility method extracting the commonality of the saveDataHolding tests
   * @param name full path name to save into myspace
   * @param text the text to store
   * @throws Exception who knows
   * 
   */
  private void importExportDelete(final String name, final String text)
    throws Exception {
    importExportDeleteDelayed(name, text, 0);
  }

  /**
   * Just as importExportDelete, except a random delay is inserted between the calls to import/export/delete
   * @see importExportDelete(String, String)
   * @param name full myspace path name
   * @param text text to insert
   * @param maxDelay maximum delay in milliseconds between calls
   * @throws Exception who knows
   */
  private void importExportDeleteDelayed(
    final String name,
    final String text,
    final int maxDelay)
    throws Exception {
    try {
      Thread.sleep((int) (maxDelay * Math.random()));
    } catch (InterruptedException e) {
      log.debug(e);//ignore
    }

    final MySpaceClient client = getDelegate(mySpaceEndPoint);
    try {
      Thread.sleep((int) (maxDelay * Math.random()));
    } catch (InterruptedException e) {
        log.debug(e);//ignore
    }

    boolean ok =
      client.saveDataHolding(
        defaultUser,
        defaultCommunity,
        defaultCredential,
        name,
        text,
        "any",
        MySpaceClient.OVERWRITE);
    assertTrue(
      "saveDataHolding problem - note test database may now be corrupt",
      ok);
    try {
      Thread.sleep((int) (maxDelay * Math.random()));
    } catch (InterruptedException e) {
        log.debug(e);//ignore
    }

    String result =
      client.getDataHolding(
        defaultUser,
        defaultCommunity,
        defaultCredential,
        name);
    assertNotNull("Returned result from getDataHolding was null", result);
    log.debug("Attempted to save '" + text + "' under name " + name);
    log.debug("Received back '" + result + "'");
    String testText = text + "\n"; //@TODO see bug 119
    assertEquals(
      "data returned from myspace not same as saved",
      result,
      testText);
    try {
      Thread.sleep((int) (maxDelay * Math.random()));
    } catch (InterruptedException e) {
        log.debug(e);//ignore
    }

    String ok2 =
      client.deleteDataHolding(
        defaultUser,
        defaultCommunity,
        defaultCredential,
        name);
    assertTrue(
      "deleteDataHolding should return success ",
      ok2.indexOf("SUCCESS") != -1);
  }
  /**
   * Utility method extracting the commonality of the saveDataHolding tests
   * @param name under which you wish to save the text
   * @param urlString the url of the data you wish to save
   * @throws Exception who knows
   */
  private void importURLExportDelete(final String name, final String urlString)
    throws Exception {
    MySpaceClient client = getDelegate(mySpaceEndPoint);
    boolean ok =
      client.saveDataHoldingURL(
        defaultUser,
        defaultCommunity,
        defaultCredential,
        name,
        urlString,
        "any",
        MySpaceClient.OVERWRITE);
    assertTrue(
      "saveDataHolding problem - note test database may now be corrupt",
      ok);
    String result =
      client.getDataHolding(
        defaultUser,
        defaultCommunity,
        defaultCredential,
        name);
    assertNotNull("Returned result from getDataHolding was null", result);
    URL url = new URL(urlString);
    InputStream is = url.openStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuffer textBuff = new StringBuffer();
    int ch;
    while ((ch=reader.read())!=-1) {
        textBuff.append((char)ch);
    }
    String text = textBuff.toString();

    log.debug("Attempted to save from'" + url + "' under name " + name);
    log.debug("Received back '" + result + "'");
    log.debug("Expected: '" + text + "'");
    assertEquals("data returned from myspace not same as saved", result, text);
    String ok2 =
      client.deleteDataHolding(
        defaultUser,
        defaultCommunity,
        defaultCredential,
        name);
    log.debug("What is this returning?" + ok2);
  }
  /**
   * test reading from url
   * @throws Exception no idea
   */
  public void testImportExportDeleteURL() throws Exception {
    String name = "foo" + Long.toString(System.currentTimeMillis());
    String url =
      "http://wiki.astrogrid.org/pub/Main/JohnTaylor/urlTestConfig.xml";
    importURLExportDelete(
      getFullPath(defaultUser, defaultCommunity, name),
      url);
  }
}
