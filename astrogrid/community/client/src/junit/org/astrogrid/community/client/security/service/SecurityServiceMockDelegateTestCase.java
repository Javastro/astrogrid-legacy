/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/security/service/SecurityServiceMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceMockDelegateTestCase.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 17:13:30  dave
 *   Added DatabaseManager delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.service ;

import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerMockDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerMockDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate ;

import org.astrogrid.community.common.security.service.SecurityServiceTest ;

/**
 * A JUnit test case for our SecurityService service.
 *
 */
public class SecurityServiceMockDelegateTestCase
	extends SecurityServiceTest
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
		this.setSecurityService(
			new SecurityServiceMockDelegate()
			) ;
		}
	}
