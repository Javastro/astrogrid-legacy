/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/security/manager/SecurityManagerMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerMockDelegateTestCase.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/05 16:28:28  dave
 *   Added SecurityManager delegate test casees.
 *   Refactored Maven JUnit properties.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerMockDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerMockDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate ;

import org.astrogrid.community.common.security.manager.SecurityManagerTest ;

/**
 * A JUnit test case for our SecurityManager service.
 *
 */
public class SecurityManagerMockDelegateTestCase
	extends SecurityManagerTest
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Setup our test.
	 * Creates the Mock delegates to test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Create our test targets.
		this.setDatabaseManager(
			new DatabaseManagerMockDelegate()
			) ;
		this.setAccountManager(
			new PolicyManagerMockDelegate()
			) ;
		this.setSecurityManager(
			new SecurityManagerMockDelegate()
			) ;
		}
	}
