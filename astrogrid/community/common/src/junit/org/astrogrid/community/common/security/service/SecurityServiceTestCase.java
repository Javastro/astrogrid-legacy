/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/security/service/SecurityServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceTestCase.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.2  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.1.2.1  2004/02/27 16:22:14  dave
 *   Added SecurityService interface, mock and test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.service ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerMock ;

import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.common.policy.manager.AccountManagerMock ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.common.security.manager.SecurityManagerMock ;

/**
 * A JUnit test case for our mock SecurityService.
 *
 */
public class SecurityServiceTestCase
	extends SecurityServiceTest
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Setup our test.
	 * Creates a mock SecurityManager and SecurityService to test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Set out test targets.
		this.setDatabaseManager(
			new DatabaseManagerMock()
			) ;
		this.setAccountManager(
			new AccountManagerMock()
			) ;
		this.setSecurityManager(
			new SecurityManagerMock()
			) ;
		this.setSecurityService(
			new SecurityServiceMock()
			) ;
		//
		// Reset our database tables.
		this.resetDatabase() ;
		}
	}
