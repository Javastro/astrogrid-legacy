/*
 * $Id: SimpleMySpaceTest.java,v 1.8 2004/02/14 20:20:52 jdt Exp $ Created on
 * 28-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.astrogrid.integrationTests.mySpace;
import java.io.IOException;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.integrationTests.common.ConfManager;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
/**
 * Not really an integration test this - just an attempt to see if we can call
 * a few webservices remotely. 
 * working again
 * 
 * @author john taylor
 */
public class SimpleMySpaceTest extends TestCase {
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
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SimpleMySpaceTest.class);
	}
	private static Log log = LogFactory.getLog(SimpleMySpaceTest.class);

	/**
	 * Location of MySpace webservice property file
	 */
	private String mySpaceEndPoint;
	/**
	 * most tests will require a user
	 */
	private String defaultUser = "JonathanAitken";
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
	public final void setUp() throws Exception {
		log.debug("\n\nSetting up...");
		mySpaceEndPoint = ConfManager.getInstance().getMySpaceEndPoint();
		assert(mySpaceEndPoint != null);
		boolean ok = createUser(defaultUser, defaultCommunity, mySpaceEndPoint);
		if (!ok){
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
	public final void tearDown() throws Exception {
		log.debug("Tearing down...");
		boolean ok = deleteUser(defaultUser, defaultCommunity, mySpaceEndPoint);
		if (!ok){
			log.error("Failed to delete user - check the mySpace by hand");
		}
	}

	/**
	 * The simplest test. Add a user, and delete it again.
	 *  
	 */
	public void testAddDeleteUser() {
		String communityId = "roe";
		String userId = "jdtTesting";
		boolean ok1 = false;
		boolean ok2 = false;
		try {
			ok1 = createUser(userId, communityId, mySpaceEndPoint);
			log.debug("Created OK?" + ok1);
		} catch (Exception e) {
			log.error("Error creating user: " + e);
			fail("Exception creating user: " + e);
		} finally {
			log.debug("Attempting to delete user");
			try {
				ok2 = deleteUser(userId, communityId, mySpaceEndPoint);
				log.debug("Deleted OK? " + ok2);
			} catch (Exception e) {
				log.error("Error deleting user: " + e);
				log.error(
					"Warning - failed to delete user; mySpace database may now be corrupt and require some TLC");
				fail("Exception deleting user:  " + e);
			}
		}
		assertTrue("Result from createUser was false", ok1);
		assertTrue("Result from deleteUser was false", ok2);
	}

	/**
	 *  
	 */
	private boolean deleteUser(
		String userID,
		String communityID,
		String endPoint)
		throws Exception {

		MySpaceClient client = getDelegate(endPoint);
		// Attempt to add, and then delete a user
		Vector servers = new Vector();
		servers.add(defaultServer);
		String credential = "any";
		boolean ok2 = false;
		try {
			log.debug("Attempting to deleteUser with the following params:");
			log.debug("userID: " + userID);
			log.debug("communityId: " + communityID);
			log.debug("credential: " + credential);
			log.debug("servers: " + servers);
			ok2 = client.deleteUser(userID, communityID, credential);
			log.debug("Deleted? " + ok2);
		} catch (Exception e) {
			log.error("Error deleting user: " + e);
			log.error(
				"Warning - failed to delete user; mySpace database may now be corrupt and require some TLC");
			throw e;
		}

		return ok2;
	}
	/**
	 *  
	 */
	private boolean createUser(
		String userID,
		String communityID,
		String endPoint)
		throws Exception {
		
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
		} finally {

		}
		return ok;
	}
	/**
	 * @param endPoint
	 * @return a mySpace delegate
	 * @throws IOException on failure to obtain delegate
	 */
	private MySpaceClient getDelegate(String endPoint) throws IOException {
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
		String name="foo"+Long.toString(System.currentTimeMillis());
		importExportDelete(getFullPath(defaultUser, defaultCommunity, name), "argle");
	}
	
	/**
	 * Now a bit harder - can we save and return xml?
	 * @throws Exception no idea
	 */
	public void testImportExportDeleteXMLTextAgain() throws Exception {
		String name="foo"+Long.toString(System.currentTimeMillis());
		String xml="<?xml version=\"1.0\"?><properties><title>Integration Tests</title><author email=\"jdt@roe.ac.uk\">John Taylor</author></properties>";
		importExportDelete(getFullPath(defaultUser, defaultCommunity, name), xml);
	}

	/**
	 * Again, with single quotes
	 * @throws Exception no idea
	 */
	public void testImportExportDeleteXMLText() throws Exception {
		String name="foo"+Long.toString(System.currentTimeMillis());
		String xml="<?xml version='1.0'?><properties><title>Integration Tests</title><author email='jdt@roe.ac.uk'>John Taylor</author></properties>";
		importExportDelete(getFullPath(defaultUser, defaultCommunity, name), xml);
	}	
	
	/**
	 * Now lets really go mad - multiline text. Note this has just been done by the addition
	 * of a few \n chars....maybe need to think about this a bit more
	 * @throws Exception nfi
	 */
	public void testImportExportDeleteMultilineText() throws Exception {
		String name="foo"+Long.toString(System.currentTimeMillis());
		String xml="<?xml version=\"1.0\"?>\n<properties>\n<title>Integration Tests</title>\n<author email=\"jdt@roe.ac.uk\">John Taylor</author>\n</properties>";
		importExportDelete(getFullPath(defaultUser, defaultCommunity, name), xml);
	}
	
	/**
	 * Construct the full path name of a myspace artifact
	 * @param user user
	 * @param community community
	 * @param file filename
	 * @return full path
	 */
	private String getFullPath(String user, String community, String file) {
		return "/"+user+"@"+community+"/"+defaultServer+"/"+file;
	}
	/**
	 * Utility method extracting the commonality of the saveDataHolding tests
	 * @throws Exception who knows
	 */
	private void importExportDelete(String name, String text) throws  Exception {
		MySpaceClient client = getDelegate(mySpaceEndPoint);
		boolean ok = client.saveDataHolding(defaultUser, defaultCommunity, defaultCredential, name,text,"any",MySpaceClient.OVERWRITE);
		assertTrue("saveDataHolding problem - note test database may now be corrupt", ok);
		String result = client.getDataHolding(defaultUser, defaultCommunity, defaultCredential, name);
		assertNotNull("Returned result from getDataHolding was null", result);
		log.debug("Attempted to save '"+text+"' under name "+name);
		log.debug("Received back '" + result+"'");
		assertEquals("data returned from myspace not same as saved",result,text);
		String ok2 = client.deleteDataHolding(defaultUser, defaultCommunity, defaultCredential, name);
		log.debug("What is this returning?"+ok2);
	}
}
