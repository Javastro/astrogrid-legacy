/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/security/service/SecurityServiceSoapDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceSoapDelegateTestCase.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.service ;

import java.net.URL ;

import org.apache.axis.client.Call ;
import org.apache.axis.client.AdminClient ;

import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerSoapDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerSoapDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceSoapDelegate ;

import org.astrogrid.community.common.security.service.SecurityServiceTest ;

/**
 * A JUnit test case for our SecurityService service.
 *
 */
public class SecurityServiceSoapDelegateTestCase
	extends SecurityServiceTest
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Base URL for our SOAP endpoints.
	 *
	 */
	private static String URL_BASE = "local://" ;

	/**
	 * Setup our test.
	 * Creates the Mock delegates to test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Initialise the Axis 'local:' URL protocol.
		Call.initialize() ;
		//
		// Create our test targets.
		this.setDatabaseManager(
			new DatabaseManagerSoapDelegate(
				URL_BASE + "/DatabaseManager"
				)
			) ;
		this.setAccountManager(
			new PolicyManagerSoapDelegate(
				URL_BASE + "/PolicyManager"
				)
			) ;
		this.setSecurityManager(
			new SecurityManagerSoapDelegate(
				URL_BASE + "/SecurityManager"
				)
			) ;
		this.setSecurityService(
			new SecurityServiceSoapDelegate(
				URL_BASE + "/SecurityService"
				)
			) ;
		//
		// Reset our database tables.
		this.resetDatabase() ;
		}
	}
