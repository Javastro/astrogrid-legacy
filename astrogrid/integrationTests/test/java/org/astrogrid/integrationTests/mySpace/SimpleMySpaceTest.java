/*
 * $Id: SimpleMySpaceTest.java,v 1.7 2004/02/14 19:11:00 jdt Exp $ Created on
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
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.integrationTests.common.ConfManager;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
/**
 * Not really an integration test this - just an attempt to see if we can call
 * a few webservices remotely. @TODO tidy the damn thing up if eclipse starts
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
	private String defaultUser = "JeffreyArcher";
	/**
	 * mosts tests will also require a community
	 */
	private String defaultCommunity = "pentonville";
	/**
	 * Called before each test. Sets up a default user
	 * 
	 * @throws Exception
	 *             on failure to load the config file, or a problem accessing
	 *             the web service
	 */
	public final void setUp() throws Exception {
		mySpaceEndPoint = ConfManager.getInstance().getMySpaceEndPoint();
		assert(mySpaceEndPoint != null);
		createUser(defaultUser, defaultCommunity, mySpaceEndPoint);
	}
	/**
	 * Tidy up following test. Deletes the default user
	 * 
	 * @throws Exception
	 *             on failure to load the config file, or a problem accessing
	 *             the web service
	 */
	public final void tearDown() throws Exception {
		deleteUser(defaultUser, defaultCommunity, mySpaceEndPoint);
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
			log.debug("Result: " + ok1);
		} catch (Exception e) {
			log.error("Error creating user: " + e);
			fail("Exception creating user: " + e);
		} finally {
			log.debug("Attempting to delete user");
			try {
				ok2 = deleteUser(userId, communityId, mySpaceEndPoint);
				log.debug("Result: " + ok2);
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
		// Attempt to add, and then delete a user
		Vector servers = new Vector();
		servers.add("check with Clive what goes here");
		String credential = "";
		boolean ok2 = false;
		try {
			ok2 = client.deleteUser(userID, communityID, credential);
			log.debug("Result: " + ok2);
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
		// Attempt to add, and then delete a user
		Vector servers = new Vector();
		servers.add("check with Clive what goes here");
		String credential = "";
		boolean ok = false;
		try {
			log.debug("Attempting to createUser with the following params:");
			log.debug("userID: " + userID);
			log.debug("communityId: " + communityID);
			log.debug("credential: " + credential);
			log.debug("servers: " + servers);
			ok = client.createUser(userID, communityID, credential, servers);
			log.debug("Result: " + ok);
		} catch (Exception e) {
			log.error("Error creating user: " + e);
			throw e;
		} finally {

		}
		return ok;
	}
	public void testImportSimpleText() {

	}
}
